/*     */ package sun.nio.fs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.AccessDeniedException;
/*     */ import java.nio.file.FileAlreadyExistsException;
/*     */ import java.nio.file.FileSystemException;
/*     */ import java.nio.file.NoSuchFileException;
/*     */ 
/*     */ class WindowsException extends Exception
/*     */ {
/*     */   static final long serialVersionUID = 2765039493083748820L;
/*     */   private int lastError;
/*     */   private String msg;
/*     */ 
/*     */   WindowsException(int paramInt)
/*     */   {
/*  44 */     this.lastError = paramInt;
/*  45 */     this.msg = null;
/*     */   }
/*     */ 
/*     */   WindowsException(String paramString) {
/*  49 */     this.lastError = 0;
/*  50 */     this.msg = paramString;
/*     */   }
/*     */ 
/*     */   int lastError() {
/*  54 */     return this.lastError;
/*     */   }
/*     */ 
/*     */   String errorString() {
/*  58 */     if (this.msg == null) {
/*  59 */       this.msg = WindowsNativeDispatcher.FormatMessage(this.lastError);
/*  60 */       if (this.msg == null) {
/*  61 */         this.msg = ("Unknown error: 0x" + Integer.toHexString(this.lastError));
/*     */       }
/*     */     }
/*  64 */     return this.msg;
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/*  69 */     return errorString();
/*     */   }
/*     */ 
/*     */   private IOException translateToIOException(String paramString1, String paramString2)
/*     */   {
/*  74 */     if (lastError() == 0) {
/*  75 */       return new IOException(errorString());
/*     */     }
/*     */ 
/*  78 */     if ((lastError() == 2) || (lastError() == 3))
/*  79 */       return new NoSuchFileException(paramString1, paramString2, null);
/*  80 */     if ((lastError() == 80) || (lastError() == 183))
/*  81 */       return new FileAlreadyExistsException(paramString1, paramString2, null);
/*  82 */     if (lastError() == 5) {
/*  83 */       return new AccessDeniedException(paramString1, paramString2, null);
/*     */     }
/*     */ 
/*  86 */     return new FileSystemException(paramString1, paramString2, errorString());
/*     */   }
/*     */ 
/*     */   void rethrowAsIOException(String paramString) throws IOException {
/*  90 */     IOException localIOException = translateToIOException(paramString, null);
/*  91 */     throw localIOException;
/*     */   }
/*     */ 
/*     */   void rethrowAsIOException(WindowsPath paramWindowsPath1, WindowsPath paramWindowsPath2) throws IOException {
/*  95 */     String str1 = paramWindowsPath1 == null ? null : paramWindowsPath1.getPathForExceptionMessage();
/*  96 */     String str2 = paramWindowsPath2 == null ? null : paramWindowsPath2.getPathForExceptionMessage();
/*  97 */     IOException localIOException = translateToIOException(str1, str2);
/*  98 */     throw localIOException;
/*     */   }
/*     */ 
/*     */   void rethrowAsIOException(WindowsPath paramWindowsPath) throws IOException {
/* 102 */     rethrowAsIOException(paramWindowsPath, null);
/*     */   }
/*     */ 
/*     */   IOException asIOException(WindowsPath paramWindowsPath) {
/* 106 */     return translateToIOException(paramWindowsPath.getPathForExceptionMessage(), null);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.WindowsException
 * JD-Core Version:    0.6.2
 */