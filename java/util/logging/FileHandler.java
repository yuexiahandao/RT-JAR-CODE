/*     */ package java.util.logging;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.nio.channels.FileChannel;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class FileHandler extends StreamHandler
/*     */ {
/*     */   private MeteredStream meter;
/*     */   private boolean append;
/*     */   private int limit;
/*     */   private int count;
/*     */   private String pattern;
/*     */   private String lockFileName;
/*     */   private FileOutputStream lockStream;
/*     */   private File[] files;
/*     */   private static final int MAX_LOCKS = 100;
/* 130 */   private static HashMap<String, String> locks = new HashMap();
/*     */ 
/*     */   private void open(File paramFile, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 169 */     int i = 0;
/* 170 */     if (paramBoolean) {
/* 171 */       i = (int)paramFile.length();
/*     */     }
/* 173 */     FileOutputStream localFileOutputStream = new FileOutputStream(paramFile.toString(), paramBoolean);
/* 174 */     BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(localFileOutputStream);
/* 175 */     this.meter = new MeteredStream(localBufferedOutputStream, i);
/* 176 */     setOutputStream(this.meter);
/*     */   }
/*     */ 
/*     */   private void configure()
/*     */   {
/* 183 */     LogManager localLogManager = LogManager.getLogManager();
/*     */ 
/* 185 */     String str = getClass().getName();
/*     */ 
/* 187 */     this.pattern = localLogManager.getStringProperty(str + ".pattern", "%h/java%u.log");
/* 188 */     this.limit = localLogManager.getIntProperty(str + ".limit", 0);
/* 189 */     if (this.limit < 0) {
/* 190 */       this.limit = 0;
/*     */     }
/* 192 */     this.count = localLogManager.getIntProperty(str + ".count", 1);
/* 193 */     if (this.count <= 0) {
/* 194 */       this.count = 1;
/*     */     }
/* 196 */     this.append = localLogManager.getBooleanProperty(str + ".append", false);
/* 197 */     setLevel(localLogManager.getLevelProperty(str + ".level", Level.ALL));
/* 198 */     setFilter(localLogManager.getFilterProperty(str + ".filter", null));
/* 199 */     setFormatter(localLogManager.getFormatterProperty(str + ".formatter", new XMLFormatter()));
/*     */     try {
/* 201 */       setEncoding(localLogManager.getStringProperty(str + ".encoding", null));
/*     */     } catch (Exception localException1) {
/*     */       try {
/* 204 */         setEncoding(null);
/*     */       }
/*     */       catch (Exception localException2)
/*     */       {
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public FileHandler()
/*     */     throws IOException, SecurityException
/*     */   {
/* 223 */     checkPermission();
/* 224 */     configure();
/* 225 */     openFiles();
/*     */   }
/*     */ 
/*     */   public FileHandler(String paramString)
/*     */     throws IOException, SecurityException
/*     */   {
/* 246 */     if (paramString.length() < 1) {
/* 247 */       throw new IllegalArgumentException();
/*     */     }
/* 249 */     checkPermission();
/* 250 */     configure();
/* 251 */     this.pattern = paramString;
/* 252 */     this.limit = 0;
/* 253 */     this.count = 1;
/* 254 */     openFiles();
/*     */   }
/*     */ 
/*     */   public FileHandler(String paramString, boolean paramBoolean)
/*     */     throws IOException, SecurityException
/*     */   {
/* 278 */     if (paramString.length() < 1) {
/* 279 */       throw new IllegalArgumentException();
/*     */     }
/* 281 */     checkPermission();
/* 282 */     configure();
/* 283 */     this.pattern = paramString;
/* 284 */     this.limit = 0;
/* 285 */     this.count = 1;
/* 286 */     this.append = paramBoolean;
/* 287 */     openFiles();
/*     */   }
/*     */ 
/*     */   public FileHandler(String paramString, int paramInt1, int paramInt2)
/*     */     throws IOException, SecurityException
/*     */   {
/* 315 */     if ((paramInt1 < 0) || (paramInt2 < 1) || (paramString.length() < 1)) {
/* 316 */       throw new IllegalArgumentException();
/*     */     }
/* 318 */     checkPermission();
/* 319 */     configure();
/* 320 */     this.pattern = paramString;
/* 321 */     this.limit = paramInt1;
/* 322 */     this.count = paramInt2;
/* 323 */     openFiles();
/*     */   }
/*     */ 
/*     */   public FileHandler(String paramString, int paramInt1, int paramInt2, boolean paramBoolean)
/*     */     throws IOException, SecurityException
/*     */   {
/* 354 */     if ((paramInt1 < 0) || (paramInt2 < 1) || (paramString.length() < 1)) {
/* 355 */       throw new IllegalArgumentException();
/*     */     }
/* 357 */     checkPermission();
/* 358 */     configure();
/* 359 */     this.pattern = paramString;
/* 360 */     this.limit = paramInt1;
/* 361 */     this.count = paramInt2;
/* 362 */     this.append = paramBoolean;
/* 363 */     openFiles();
/*     */   }
/*     */ 
/*     */   private void openFiles()
/*     */     throws IOException
/*     */   {
/* 369 */     LogManager localLogManager = LogManager.getLogManager();
/* 370 */     localLogManager.checkPermission();
/* 371 */     if (this.count < 1) {
/* 372 */       throw new IllegalArgumentException("file count = " + this.count);
/*     */     }
/* 374 */     if (this.limit < 0) {
/* 375 */       this.limit = 0;
/*     */     }
/*     */ 
/* 380 */     InitializationErrorManager localInitializationErrorManager = new InitializationErrorManager(null);
/* 381 */     setErrorManager(localInitializationErrorManager);
/*     */ 
/* 385 */     int i = -1;
/*     */     while (true) {
/* 387 */       i++;
/* 388 */       if (i > 100) {
/* 389 */         throw new IOException("Couldn't get lock for " + this.pattern);
/*     */       }
/*     */ 
/* 392 */       this.lockFileName = (generate(this.pattern, 0, i).toString() + ".lck");
/*     */ 
/* 397 */       synchronized (locks) {
/* 398 */         if (locks.get(this.lockFileName) == null)
/*     */         {
/*     */           FileChannel localFileChannel;
/*     */           try
/*     */           {
/* 405 */             this.lockStream = new FileOutputStream(this.lockFileName);
/* 406 */             localFileChannel = this.lockStream.getChannel();
/*     */           }
/*     */           catch (IOException localIOException1) {
/*     */           }
/* 410 */           continue;
/*     */           int k;
/*     */           try {
/* 414 */             k = localFileChannel.tryLock() != null ? 1 : 0;
/*     */           }
/*     */           catch (IOException localIOException2)
/*     */           {
/* 421 */             k = 1;
/*     */           }
/* 423 */           if (k != 0)
/*     */           {
/* 425 */             locks.put(this.lockFileName, this.lockFileName);
/* 426 */             break;
/*     */           }
/*     */ 
/* 430 */           localFileChannel.close();
/*     */         }
/*     */       }
/*     */     }
/* 434 */     this.files = new File[this.count];
/* 435 */     for (int j = 0; j < this.count; j++) {
/* 436 */       this.files[j] = generate(this.pattern, j, i);
/*     */     }
/*     */ 
/* 440 */     if (this.append)
/* 441 */       open(this.files[0], true);
/*     */     else {
/* 443 */       rotate();
/*     */     }
/*     */ 
/* 447 */     Exception localException = localInitializationErrorManager.lastException;
/* 448 */     if (localException != null) {
/* 449 */       if ((localException instanceof IOException))
/* 450 */         throw ((IOException)localException);
/* 451 */       if ((localException instanceof SecurityException)) {
/* 452 */         throw ((SecurityException)localException);
/*     */       }
/* 454 */       throw new IOException("Exception: " + localException);
/*     */     }
/*     */ 
/* 459 */     setErrorManager(new ErrorManager());
/*     */   }
/*     */ 
/*     */   private File generate(String paramString, int paramInt1, int paramInt2) throws IOException
/*     */   {
/* 464 */     File localFile = null;
/* 465 */     String str1 = "";
/* 466 */     int i = 0;
/* 467 */     int j = 0;
/* 468 */     int k = 0;
/* 469 */     while (i < paramString.length()) {
/* 470 */       char c = paramString.charAt(i);
/* 471 */       i++;
/* 472 */       int m = 0;
/* 473 */       if (i < paramString.length()) {
/* 474 */         m = Character.toLowerCase(paramString.charAt(i));
/*     */       }
/* 476 */       if (c == '/') {
/* 477 */         if (localFile == null)
/* 478 */           localFile = new File(str1);
/*     */         else {
/* 480 */           localFile = new File(localFile, str1);
/*     */         }
/* 482 */         str1 = "";
/*     */       }
/* 484 */       else if (c == '%') {
/* 485 */         if (m == 116) {
/* 486 */           String str2 = System.getProperty("java.io.tmpdir");
/* 487 */           if (str2 == null) {
/* 488 */             str2 = System.getProperty("user.home");
/*     */           }
/* 490 */           localFile = new File(str2);
/* 491 */           i++;
/* 492 */           str1 = "";
/*     */         }
/* 494 */         else if (m == 104) {
/* 495 */           localFile = new File(System.getProperty("user.home"));
/* 496 */           if (isSetUID())
/*     */           {
/* 499 */             throw new IOException("can't use %h in set UID program");
/*     */           }
/* 501 */           i++;
/* 502 */           str1 = "";
/*     */         }
/* 504 */         else if (m == 103) {
/* 505 */           str1 = str1 + paramInt1;
/* 506 */           j = 1;
/* 507 */           i++;
/*     */         }
/* 509 */         else if (m == 117) {
/* 510 */           str1 = str1 + paramInt2;
/* 511 */           k = 1;
/* 512 */           i++;
/*     */         }
/* 514 */         else if (m == 37) {
/* 515 */           str1 = str1 + "%";
/* 516 */           i++;
/*     */         }
/*     */       }
/*     */       else {
/* 520 */         str1 = str1 + c;
/*     */       }
/*     */     }
/* 522 */     if ((this.count > 1) && (j == 0)) {
/* 523 */       str1 = str1 + "." + paramInt1;
/*     */     }
/* 525 */     if ((paramInt2 > 0) && (k == 0)) {
/* 526 */       str1 = str1 + "." + paramInt2;
/*     */     }
/* 528 */     if (str1.length() > 0) {
/* 529 */       if (localFile == null)
/* 530 */         localFile = new File(str1);
/*     */       else {
/* 532 */         localFile = new File(localFile, str1);
/*     */       }
/*     */     }
/* 535 */     return localFile;
/*     */   }
/*     */ 
/*     */   private synchronized void rotate()
/*     */   {
/* 540 */     Level localLevel = getLevel();
/* 541 */     setLevel(Level.OFF);
/*     */ 
/* 543 */     super.close();
/* 544 */     for (int i = this.count - 2; i >= 0; i--) {
/* 545 */       File localFile1 = this.files[i];
/* 546 */       File localFile2 = this.files[(i + 1)];
/* 547 */       if (localFile1.exists()) {
/* 548 */         if (localFile2.exists()) {
/* 549 */           localFile2.delete();
/*     */         }
/* 551 */         localFile1.renameTo(localFile2);
/*     */       }
/*     */     }
/*     */     try {
/* 555 */       open(this.files[0], false);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 559 */       reportError(null, localIOException, 4);
/*     */     }
/*     */ 
/* 562 */     setLevel(localLevel);
/*     */   }
/*     */ 
/*     */   public synchronized void publish(LogRecord paramLogRecord)
/*     */   {
/* 572 */     if (!isLoggable(paramLogRecord)) {
/* 573 */       return;
/*     */     }
/* 575 */     super.publish(paramLogRecord);
/* 576 */     flush();
/* 577 */     if ((this.limit > 0) && (this.meter.written >= this.limit))
/*     */     {
/* 583 */       AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public Object run() {
/* 585 */           FileHandler.this.rotate();
/* 586 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void close()
/*     */     throws SecurityException
/*     */   {
/* 599 */     super.close();
/*     */ 
/* 601 */     if (this.lockFileName == null) {
/* 602 */       return;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 607 */       this.lockStream.close();
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 611 */     synchronized (locks) {
/* 612 */       locks.remove(this.lockFileName);
/*     */     }
/* 614 */     new File(this.lockFileName).delete();
/* 615 */     this.lockFileName = null;
/* 616 */     this.lockStream = null;
/*     */   }
/*     */   private static native boolean isSetUID();
/*     */ 
/*     */   private static class InitializationErrorManager extends ErrorManager { Exception lastException;
/*     */ 
/* 622 */     public void error(String paramString, Exception paramException, int paramInt) { this.lastException = paramException; }
/*     */ 
/*     */   }
/*     */ 
/*     */   private class MeteredStream extends OutputStream
/*     */   {
/*     */     OutputStream out;
/*     */     int written;
/*     */ 
/*     */     MeteredStream(OutputStream paramInt, int arg3)
/*     */     {
/* 140 */       this.out = paramInt;
/*     */       int i;
/* 141 */       this.written = i;
/*     */     }
/*     */ 
/*     */     public void write(int paramInt) throws IOException {
/* 145 */       this.out.write(paramInt);
/* 146 */       this.written += 1;
/*     */     }
/*     */ 
/*     */     public void write(byte[] paramArrayOfByte) throws IOException {
/* 150 */       this.out.write(paramArrayOfByte);
/* 151 */       this.written += paramArrayOfByte.length;
/*     */     }
/*     */ 
/*     */     public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 155 */       this.out.write(paramArrayOfByte, paramInt1, paramInt2);
/* 156 */       this.written += paramInt2;
/*     */     }
/*     */ 
/*     */     public void flush() throws IOException {
/* 160 */       this.out.flush();
/*     */     }
/*     */ 
/*     */     public void close() throws IOException {
/* 164 */       this.out.close();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.logging.FileHandler
 * JD-Core Version:    0.6.2
 */