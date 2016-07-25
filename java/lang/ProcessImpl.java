/*     */ package java.lang;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Map;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import sun.misc.JavaIOFileDescriptorAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ 
/*     */ final class ProcessImpl extends Process
/*     */ {
/*  52 */   private static final JavaIOFileDescriptorAccess fdAccess = SharedSecrets.getJavaIOFileDescriptorAccess();
/*     */   private static final int VERIFICATION_CMD_BAT = 0;
/*     */   private static final int VERIFICATION_WIN32 = 1;
/*     */   private static final int VERIFICATION_LEGACY = 2;
/* 175 */   private static final char[][] ESCAPE_VERIFICATION = { { ' ', '\t', '<', '>', '&', '|', '^' }, { ' ', '\t', '<', '>' }, { ' ', '\t' } };
/*     */ 
/* 308 */   private long handle = 0L;
/*     */   private OutputStream stdin_stream;
/*     */   private InputStream stdout_stream;
/*     */   private InputStream stderr_stream;
/* 436 */   private static final int STILL_ACTIVE = getStillActive();
/*     */ 
/*     */   private static FileOutputStream newFileOutputStream(File paramFile, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*  65 */     if (paramBoolean) {
/*  66 */       String str = paramFile.getPath();
/*  67 */       SecurityManager localSecurityManager = System.getSecurityManager();
/*  68 */       if (localSecurityManager != null)
/*  69 */         localSecurityManager.checkWrite(str);
/*  70 */       long l = openForAtomicAppend(str);
/*  71 */       FileDescriptor localFileDescriptor = new FileDescriptor();
/*  72 */       fdAccess.setHandle(localFileDescriptor, l);
/*  73 */       return (FileOutputStream)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public FileOutputStream run() {
/*  76 */           return new FileOutputStream(this.val$fd);
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/*  81 */     return new FileOutputStream(paramFile);
/*     */   }
/*     */ 
/*     */   static Process start(String[] paramArrayOfString, Map<String, String> paramMap, String paramString, ProcessBuilder.Redirect[] paramArrayOfRedirect, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*  93 */     String str = ProcessEnvironment.toEnvironmentBlock(paramMap);
/*     */ 
/*  95 */     FileInputStream localFileInputStream = null;
/*  96 */     FileOutputStream localFileOutputStream1 = null;
/*  97 */     FileOutputStream localFileOutputStream2 = null;
/*     */     try
/*     */     {
/*     */       long[] arrayOfLong;
/* 101 */       if (paramArrayOfRedirect == null) {
/* 102 */         arrayOfLong = new long[] { -1L, -1L, -1L };
/*     */       } else {
/* 104 */         arrayOfLong = new long[3];
/*     */ 
/* 106 */         if (paramArrayOfRedirect[0] == ProcessBuilder.Redirect.PIPE) {
/* 107 */           arrayOfLong[0] = -1L;
/* 108 */         } else if (paramArrayOfRedirect[0] == ProcessBuilder.Redirect.INHERIT) {
/* 109 */           arrayOfLong[0] = fdAccess.getHandle(FileDescriptor.in);
/*     */         } else {
/* 111 */           localFileInputStream = new FileInputStream(paramArrayOfRedirect[0].file());
/* 112 */           arrayOfLong[0] = fdAccess.getHandle(localFileInputStream.getFD());
/*     */         }
/*     */ 
/* 115 */         if (paramArrayOfRedirect[1] == ProcessBuilder.Redirect.PIPE) {
/* 116 */           arrayOfLong[1] = -1L;
/* 117 */         } else if (paramArrayOfRedirect[1] == ProcessBuilder.Redirect.INHERIT) {
/* 118 */           arrayOfLong[1] = fdAccess.getHandle(FileDescriptor.out);
/*     */         } else {
/* 120 */           localFileOutputStream1 = newFileOutputStream(paramArrayOfRedirect[1].file(), paramArrayOfRedirect[1].append());
/*     */ 
/* 122 */           arrayOfLong[1] = fdAccess.getHandle(localFileOutputStream1.getFD());
/*     */         }
/*     */ 
/* 125 */         if (paramArrayOfRedirect[2] == ProcessBuilder.Redirect.PIPE) {
/* 126 */           arrayOfLong[2] = -1L;
/* 127 */         } else if (paramArrayOfRedirect[2] == ProcessBuilder.Redirect.INHERIT) {
/* 128 */           arrayOfLong[2] = fdAccess.getHandle(FileDescriptor.err);
/*     */         } else {
/* 130 */           localFileOutputStream2 = newFileOutputStream(paramArrayOfRedirect[2].file(), paramArrayOfRedirect[2].append());
/*     */ 
/* 132 */           arrayOfLong[2] = fdAccess.getHandle(localFileOutputStream2.getFD());
/*     */         }
/*     */       }
/*     */ 
/* 136 */       return new ProcessImpl(paramArrayOfString, str, paramString, arrayOfLong, paramBoolean);
/*     */     }
/*     */     finally
/*     */     {
/*     */       try {
/* 141 */         if (localFileInputStream != null) localFileInputStream.close(); 
/*     */       } finally {
/*     */         try { if (localFileOutputStream1 != null) localFileOutputStream1.close();  } finally {
/* 144 */           if (localFileOutputStream2 != null) localFileOutputStream2.close();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String[] getTokensFromCommand(String paramString)
/*     */   {
/* 165 */     ArrayList localArrayList = new ArrayList(8);
/* 166 */     Matcher localMatcher = LazyPattern.PATTERN.matcher(paramString);
/* 167 */     while (localMatcher.find())
/* 168 */       localArrayList.add(localMatcher.group());
/* 169 */     return (String[])localArrayList.toArray(new String[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   private static String createCommandLine(int paramInt, String paramString, String[] paramArrayOfString)
/*     */   {
/* 188 */     StringBuilder localStringBuilder = new StringBuilder(80);
/*     */ 
/* 190 */     localStringBuilder.append(paramString);
/*     */ 
/* 192 */     for (int i = 1; i < paramArrayOfString.length; i++) {
/* 193 */       localStringBuilder.append(' ');
/* 194 */       String str = paramArrayOfString[i];
/* 195 */       if (needsEscaping(paramInt, str)) {
/* 196 */         localStringBuilder.append('"').append(str);
/*     */ 
/* 210 */         if ((paramInt != 0) && (str.endsWith("\\"))) {
/* 211 */           localStringBuilder.append('\\');
/*     */         }
/* 213 */         localStringBuilder.append('"');
/*     */       } else {
/* 215 */         localStringBuilder.append(str);
/*     */       }
/*     */     }
/* 218 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private static boolean isQuoted(boolean paramBoolean, String paramString1, String paramString2)
/*     */   {
/* 223 */     int i = paramString1.length() - 1;
/* 224 */     if ((i >= 1) && (paramString1.charAt(0) == '"') && (paramString1.charAt(i) == '"'))
/*     */     {
/* 226 */       if ((paramBoolean) && 
/* 227 */         (paramString1.indexOf('"', 1) != i))
/*     */       {
/* 229 */         throw new IllegalArgumentException(paramString2);
/*     */       }
/*     */ 
/* 232 */       return true;
/*     */     }
/* 234 */     if ((paramBoolean) && 
/* 235 */       (paramString1.indexOf('"') >= 0))
/*     */     {
/* 237 */       throw new IllegalArgumentException(paramString2);
/*     */     }
/*     */ 
/* 240 */     return false;
/*     */   }
/*     */ 
/*     */   private static boolean needsEscaping(int paramInt, String paramString)
/*     */   {
/* 251 */     boolean bool = isQuoted(paramInt == 0, paramString, "Argument has embedded quote, use the explicit CMD.EXE call.");
/*     */ 
/* 255 */     if (!bool) {
/* 256 */       char[] arrayOfChar = ESCAPE_VERIFICATION[paramInt];
/* 257 */       for (int i = 0; i < arrayOfChar.length; i++) {
/* 258 */         if (paramString.indexOf(arrayOfChar[i]) >= 0) {
/* 259 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 263 */     return false;
/*     */   }
/*     */ 
/*     */   private static String getExecutablePath(String paramString)
/*     */     throws IOException
/*     */   {
/* 269 */     boolean bool = isQuoted(true, paramString, "Executable name has embedded quote, split the arguments");
/*     */ 
/* 273 */     File localFile = new File(bool ? paramString.substring(1, paramString.length() - 1) : paramString);
/*     */ 
/* 293 */     return localFile.getPath();
/*     */   }
/*     */ 
/*     */   private boolean isShellFile(String paramString)
/*     */   {
/* 298 */     String str = paramString.toUpperCase();
/* 299 */     return (str.endsWith(".CMD")) || (str.endsWith(".BAT"));
/*     */   }
/*     */ 
/*     */   private String quoteString(String paramString) {
/* 303 */     StringBuilder localStringBuilder = new StringBuilder(paramString.length() + 2);
/* 304 */     return '"' + paramString + '"';
/*     */   }
/*     */ 
/*     */   private ProcessImpl(String[] paramArrayOfString, String paramString1, String paramString2, final long[] paramArrayOfLong, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 321 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 322 */     int i = 0;
/*     */     String str2;
/* 323 */     if (localSecurityManager == null) {
/* 324 */       i = 1;
/* 325 */       str2 = System.getProperty("jdk.lang.Process.allowAmbiguousCommands");
/* 326 */       if (str2 != null)
/* 327 */         i = !"false".equalsIgnoreCase(str2) ? 1 : 0;
/*     */     }
/*     */     String str1;
/* 329 */     if (i != 0)
/*     */     {
/* 333 */       str2 = new File(paramArrayOfString[0]).getPath();
/*     */ 
/* 336 */       if (needsEscaping(2, str2)) {
/* 337 */         str2 = quoteString(str2);
/*     */       }
/* 339 */       str1 = createCommandLine(2, str2, paramArrayOfString);
/*     */     }
/*     */     else
/*     */     {
/*     */       try
/*     */       {
/* 347 */         str2 = getExecutablePath(paramArrayOfString[0]);
/*     */       }
/*     */       catch (IllegalArgumentException localIllegalArgumentException)
/*     */       {
/* 359 */         StringBuilder localStringBuilder = new StringBuilder();
/*     */ 
/* 361 */         for (String str3 : paramArrayOfString) {
/* 362 */           localStringBuilder.append(str3).append(' ');
/*     */         }
/*     */ 
/* 365 */         paramArrayOfString = getTokensFromCommand(localStringBuilder.toString());
/* 366 */         str2 = getExecutablePath(paramArrayOfString[0]);
/*     */ 
/* 369 */         if (localSecurityManager != null) {
/* 370 */           localSecurityManager.checkExec(str2);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 376 */       str1 = createCommandLine(isShellFile(str2) ? 0 : 1, quoteString(str2), paramArrayOfString);
/*     */     }
/*     */ 
/* 385 */     this.handle = create(str1, paramString1, paramString2, paramArrayOfLong, paramBoolean);
/*     */ 
/* 388 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Void run()
/*     */       {
/*     */         FileDescriptor localFileDescriptor;
/* 391 */         if (paramArrayOfLong[0] == -1L) {
/* 392 */           ProcessImpl.this.stdin_stream = ProcessBuilder.NullOutputStream.INSTANCE;
/*     */         } else {
/* 394 */           localFileDescriptor = new FileDescriptor();
/* 395 */           ProcessImpl.fdAccess.setHandle(localFileDescriptor, paramArrayOfLong[0]);
/* 396 */           ProcessImpl.this.stdin_stream = new BufferedOutputStream(new FileOutputStream(localFileDescriptor));
/*     */         }
/*     */ 
/* 400 */         if (paramArrayOfLong[1] == -1L) {
/* 401 */           ProcessImpl.this.stdout_stream = ProcessBuilder.NullInputStream.INSTANCE;
/*     */         } else {
/* 403 */           localFileDescriptor = new FileDescriptor();
/* 404 */           ProcessImpl.fdAccess.setHandle(localFileDescriptor, paramArrayOfLong[1]);
/* 405 */           ProcessImpl.this.stdout_stream = new BufferedInputStream(new FileInputStream(localFileDescriptor));
/*     */         }
/*     */ 
/* 409 */         if (paramArrayOfLong[2] == -1L) {
/* 410 */           ProcessImpl.this.stderr_stream = ProcessBuilder.NullInputStream.INSTANCE;
/*     */         } else {
/* 412 */           localFileDescriptor = new FileDescriptor();
/* 413 */           ProcessImpl.fdAccess.setHandle(localFileDescriptor, paramArrayOfLong[2]);
/* 414 */           ProcessImpl.this.stderr_stream = new FileInputStream(localFileDescriptor);
/*     */         }
/*     */ 
/* 417 */         return null;
/*     */       } } );
/*     */   }
/*     */ 
/* 421 */   public OutputStream getOutputStream() { return this.stdin_stream; }
/*     */ 
/*     */   public InputStream getInputStream()
/*     */   {
/* 425 */     return this.stdout_stream;
/*     */   }
/*     */ 
/*     */   public InputStream getErrorStream() {
/* 429 */     return this.stderr_stream;
/*     */   }
/*     */ 
/*     */   public void finalize() {
/* 433 */     closeHandle(this.handle);
/*     */   }
/*     */ 
/*     */   private static native int getStillActive();
/*     */ 
/*     */   public int exitValue()
/*     */   {
/* 440 */     int i = getExitCodeProcess(this.handle);
/* 441 */     if (i == STILL_ACTIVE)
/* 442 */       throw new IllegalThreadStateException("process has not exited");
/* 443 */     return i;
/*     */   }
/*     */   private static native int getExitCodeProcess(long paramLong);
/*     */ 
/*     */   public int waitFor() throws InterruptedException {
/* 448 */     waitForInterruptibly(this.handle);
/* 449 */     if (Thread.interrupted())
/* 450 */       throw new InterruptedException();
/* 451 */     return exitValue();
/*     */   }
/*     */   private static native void waitForInterruptibly(long paramLong);
/*     */ 
/* 455 */   public void destroy() { terminateProcess(this.handle); }
/*     */ 
/*     */ 
/*     */   private static native void terminateProcess(long paramLong);
/*     */ 
/*     */   private static synchronized native long create(String paramString1, String paramString2, String paramString3, long[] paramArrayOfLong, boolean paramBoolean)
/*     */     throws IOException;
/*     */ 
/*     */   private static native long openForAtomicAppend(String paramString)
/*     */     throws IOException;
/*     */ 
/*     */   private static native boolean closeHandle(long paramLong);
/*     */ 
/*     */   private static class LazyPattern
/*     */   {
/* 153 */     private static final Pattern PATTERN = Pattern.compile("[^\\s\"]+|\"[^\"]*\"");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.ProcessImpl
 * JD-Core Version:    0.6.2
 */