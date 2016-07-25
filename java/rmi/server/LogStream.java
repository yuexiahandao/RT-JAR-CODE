/*     */ package java.rmi.server;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Date;
/*     */ import java.util.Hashtable;
/*     */ import java.util.logging.LoggingPermission;
/*     */ 
/*     */ @Deprecated
/*     */ public class LogStream extends PrintStream
/*     */ {
/*  42 */   private static Hashtable known = new Hashtable(5);
/*     */ 
/*  44 */   private static PrintStream defaultStream = System.err;
/*     */   private String name;
/*     */   private OutputStream logOut;
/*     */   private OutputStreamWriter logWriter;
/*  56 */   private StringBuffer buffer = new StringBuffer();
/*     */   private ByteArrayOutputStream bufOut;
/*     */   public static final int SILENT = 0;
/*     */   public static final int BRIEF = 10;
/*     */   public static final int VERBOSE = 20;
/*     */ 
/*     */   @Deprecated
/*     */   private LogStream(String paramString, OutputStream paramOutputStream)
/*     */   {
/*  73 */     super(new ByteArrayOutputStream());
/*  74 */     this.bufOut = ((ByteArrayOutputStream)this.out);
/*     */ 
/*  76 */     this.name = paramString;
/*  77 */     setOutputStream(paramOutputStream);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static LogStream log(String paramString)
/*     */   {
/*     */     LogStream localLogStream;
/*  92 */     synchronized (known) {
/*  93 */       localLogStream = (LogStream)known.get(paramString);
/*  94 */       if (localLogStream == null) {
/*  95 */         localLogStream = new LogStream(paramString, defaultStream);
/*     */       }
/*  97 */       known.put(paramString, localLogStream);
/*     */     }
/*  99 */     return localLogStream;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static synchronized PrintStream getDefaultStream()
/*     */   {
/* 111 */     return defaultStream;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static synchronized void setDefaultStream(PrintStream paramPrintStream)
/*     */   {
/* 123 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*     */ 
/* 125 */     if (localSecurityManager != null) {
/* 126 */       localSecurityManager.checkPermission(new LoggingPermission("control", null));
/*     */     }
/*     */ 
/* 130 */     defaultStream = paramPrintStream;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public synchronized OutputStream getOutputStream()
/*     */   {
/* 143 */     return this.logOut;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public synchronized void setOutputStream(OutputStream paramOutputStream)
/*     */   {
/* 156 */     this.logOut = paramOutputStream;
/*     */ 
/* 159 */     this.logWriter = new OutputStreamWriter(this.logOut);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void write(int paramInt)
/*     */   {
/* 173 */     if (paramInt == 10)
/*     */     {
/* 175 */       synchronized (this) {
/* 176 */         synchronized (this.logOut)
/*     */         {
/* 178 */           this.buffer.setLength(0);
/* 179 */           this.buffer.append(new Date().toString());
/*     */ 
/* 181 */           this.buffer.append(':');
/* 182 */           this.buffer.append(this.name);
/* 183 */           this.buffer.append(':');
/* 184 */           this.buffer.append(Thread.currentThread().getName());
/* 185 */           this.buffer.append(':');
/*     */           try
/*     */           {
/* 189 */             this.logWriter.write(this.buffer.toString());
/* 190 */             this.logWriter.flush();
/*     */ 
/* 194 */             this.bufOut.writeTo(this.logOut);
/* 195 */             this.logOut.write(paramInt);
/* 196 */             this.logOut.flush();
/*     */           } catch (IOException localIOException) {
/* 198 */             setError();
/*     */           } finally {
/* 200 */             this.bufOut.reset();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     else
/* 206 */       super.write(paramInt);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 217 */     if (paramInt2 < 0)
/* 218 */       throw new ArrayIndexOutOfBoundsException(paramInt2);
/* 219 */     for (int i = 0; i < paramInt2; i++)
/* 220 */       write(paramArrayOfByte[(paramInt1 + i)]);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public String toString()
/*     */   {
/* 232 */     return this.name;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static int parseLevel(String paramString)
/*     */   {
/* 253 */     if ((paramString == null) || (paramString.length() < 1))
/* 254 */       return -1;
/*     */     try
/*     */     {
/* 257 */       return Integer.parseInt(paramString);
/*     */     }
/*     */     catch (NumberFormatException localNumberFormatException) {
/* 260 */       if (paramString.length() < 1) {
/* 261 */         return -1;
/*     */       }
/* 263 */       if ("SILENT".startsWith(paramString.toUpperCase()))
/* 264 */         return 0;
/* 265 */       if ("BRIEF".startsWith(paramString.toUpperCase()))
/* 266 */         return 10;
/* 267 */       if ("VERBOSE".startsWith(paramString.toUpperCase()))
/* 268 */         return 20;
/*     */     }
/* 270 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.server.LogStream
 * JD-Core Version:    0.6.2
 */