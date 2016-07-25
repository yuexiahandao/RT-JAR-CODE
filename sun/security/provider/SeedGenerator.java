/*     */ package sun.security.provider;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.nio.file.DirectoryStream;
/*     */ import java.nio.file.Files;
/*     */ import java.nio.file.Path;
/*     */ import java.security.AccessController;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Properties;
/*     */ import java.util.Random;
/*     */ import sun.security.util.Debug;
/*     */ 
/*     */ abstract class SeedGenerator
/*     */ {
/*     */   private static SeedGenerator instance;
/*  82 */   private static final Debug debug = Debug.getInstance("provider");
/*     */   static final String URL_DEV_RANDOM = "file:/dev/random";
/*     */   static final String URL_DEV_URANDOM = "file:/dev/urandom";
/*     */ 
/*     */   public static void generateSeed(byte[] paramArrayOfByte)
/*     */   {
/* 139 */     instance.getSeedBytes(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   abstract void getSeedBytes(byte[] paramArrayOfByte);
/*     */ 
/*     */   static byte[] getSystemEntropy()
/*     */   {
/*     */     MessageDigest localMessageDigest;
/*     */     try
/*     */     {
/* 152 */       localMessageDigest = MessageDigest.getInstance("SHA");
/*     */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 154 */       throw new InternalError("internal error: SHA-1 not available.");
/*     */     }
/*     */ 
/* 158 */     byte b = (byte)(int)System.currentTimeMillis();
/* 159 */     localMessageDigest.update(b);
/*     */ 
/* 161 */     AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Void run()
/*     */       {
/*     */         try
/*     */         {
/* 168 */           localObject1 = System.getProperties();
/* 169 */           Enumeration localEnumeration = ((Properties)localObject1).propertyNames();
/* 170 */           while (localEnumeration.hasMoreElements()) {
/* 171 */             String str = (String)localEnumeration.nextElement();
/* 172 */             this.val$md.update(str.getBytes());
/* 173 */             this.val$md.update(((Properties)localObject1).getProperty(str).getBytes());
/*     */           }
/*     */ 
/* 177 */           SeedGenerator.addNetworkAdapterInfo(this.val$md);
/*     */ 
/* 180 */           File localFile = new File(((Properties)localObject1).getProperty("java.io.tmpdir"));
/* 181 */           int i = 0;
/* 182 */           DirectoryStream localDirectoryStream = Files.newDirectoryStream(localFile.toPath()); Object localObject2 = null;
/*     */           try
/*     */           {
/* 188 */             localRandom = new Random();
/* 189 */             for (Path localPath : localDirectoryStream) {
/* 190 */               if ((i < 512) || (localRandom.nextBoolean())) {
/* 191 */                 this.val$md.update(localPath.getFileName().toString().getBytes());
/*     */               }
/* 193 */               if (i++ > 1024)
/*     */                 break;
/*     */             }
/*     */           }
/*     */           catch (Throwable localThrowable2)
/*     */           {
/*     */             Random localRandom;
/* 182 */             localObject2 = localThrowable2; throw localThrowable2;
/*     */           }
/*     */           finally
/*     */           {
/* 197 */             if (localDirectoryStream != null) if (localObject2 != null) try { localDirectoryStream.close(); } catch (Throwable localThrowable3) { localObject2.addSuppressed(localThrowable3); } else localDirectoryStream.close();  
/*     */           }
/*     */         } catch (Exception localException) { this.val$md.update((byte)localException.hashCode()); }
/*     */ 
/*     */ 
/* 203 */         Runtime localRuntime = Runtime.getRuntime();
/* 204 */         Object localObject1 = SeedGenerator.longToByteArray(localRuntime.totalMemory());
/* 205 */         this.val$md.update((byte[])localObject1, 0, localObject1.length);
/* 206 */         localObject1 = SeedGenerator.longToByteArray(localRuntime.freeMemory());
/* 207 */         this.val$md.update((byte[])localObject1, 0, localObject1.length);
/*     */ 
/* 209 */         return null;
/*     */       }
/*     */     });
/* 212 */     return localMessageDigest.digest();
/*     */   }
/*     */ 
/*     */   private static void addNetworkAdapterInfo(MessageDigest paramMessageDigest)
/*     */   {
/*     */     try
/*     */     {
/* 223 */       Enumeration localEnumeration = NetworkInterface.getNetworkInterfaces();
/*     */ 
/* 225 */       while (localEnumeration.hasMoreElements()) {
/* 226 */         NetworkInterface localNetworkInterface = (NetworkInterface)localEnumeration.nextElement();
/* 227 */         paramMessageDigest.update(localNetworkInterface.toString().getBytes());
/* 228 */         if (!localNetworkInterface.isVirtual()) {
/* 229 */           byte[] arrayOfByte = localNetworkInterface.getHardwareAddress();
/* 230 */           if (arrayOfByte != null) {
/* 231 */             paramMessageDigest.update(arrayOfByte);
/* 232 */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private static byte[] longToByteArray(long paramLong)
/*     */   {
/* 245 */     byte[] arrayOfByte = new byte[8];
/*     */ 
/* 247 */     for (int i = 0; i < 8; i++) {
/* 248 */       arrayOfByte[i] = ((byte)(int)paramLong);
/* 249 */       paramLong >>= 8;
/*     */     }
/*     */ 
/* 252 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  89 */     String str = SunEntries.getSeedSource();
/*     */ 
/* 100 */     if ((str.equals("file:/dev/random")) || (str.equals("file:/dev/urandom"))) {
/*     */       try {
/* 102 */         instance = new NativeSeedGenerator();
/* 103 */         if (debug != null)
/* 104 */           debug.println("Using operating system seed generator");
/*     */       }
/*     */       catch (IOException localIOException1) {
/* 107 */         if (debug != null) {
/* 108 */           debug.println("Failed to use operating system seed generator: " + localIOException1.toString());
/*     */         }
/*     */       }
/*     */     }
/* 112 */     else if (str.length() != 0) {
/*     */       try {
/* 114 */         instance = new URLSeedGenerator(str);
/* 115 */         if (debug != null)
/* 116 */           debug.println("Using URL seed generator reading from " + str);
/*     */       }
/*     */       catch (IOException localIOException2)
/*     */       {
/* 120 */         if (debug != null) {
/* 121 */           debug.println("Failed to create seed generator with " + str + ": " + localIOException2.toString());
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 127 */     if (instance == null) {
/* 128 */       if (debug != null) {
/* 129 */         debug.println("Using default threaded seed generator");
/*     */       }
/* 131 */       instance = new ThreadedSeedGenerator();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ThreadedSeedGenerator extends SeedGenerator
/*     */     implements Runnable
/*     */   {
/*     */     private byte[] pool;
/*     */     private int start;
/*     */     private int end;
/*     */     private int count;
/*     */     ThreadGroup seedGroup;
/* 419 */     private static byte[] rndTab = { 56, 30, -107, -6, -86, 25, -83, 75, -12, -64, 5, -128, 78, 21, 16, 32, 70, -81, 37, -51, -43, -46, -108, 87, 29, 17, -55, 22, -11, -111, -115, 84, -100, 108, -45, -15, -98, 72, -33, -28, 31, -52, -37, -117, -97, -27, 93, -123, 47, 126, -80, -62, -93, -79, 61, -96, -65, -5, -47, -119, 14, 89, 81, -118, -88, 20, 67, -126, -113, 60, -102, 55, 110, 28, 85, 121, 122, -58, 2, 45, 43, 24, -9, 103, -13, 102, -68, -54, -101, -104, 19, 13, -39, -26, -103, 62, 77, 51, 44, 111, 73, 18, -127, -82, 4, -30, 11, -99, -74, 40, -89, 42, -76, -77, -94, -35, -69, 35, 120, 76, 33, -73, -7, 82, -25, -10, 88, 125, -112, 58, 83, 95, 6, 10, 98, -34, 80, 15, -91, 86, -19, 52, -17, 117, 49, -63, 118, -90, 36, -116, -40, -71, 97, -53, -109, -85, 109, -16, -3, 104, -95, 68, 54, 34, 26, 114, -1, 106, -121, 3, 66, 0, 100, -84, 57, 107, 119, -42, 112, -61, 1, 48, 38, 12, -56, -57, 39, -106, -72, 41, 7, 71, -29, -59, -8, -38, 79, -31, 124, -124, 8, 91, 116, 99, -4, 9, -36, -78, 63, -49, -67, -87, 59, 101, -32, 92, 94, 53, -41, 115, -66, -70, -122, 50, -50, -22, -20, -18, -21, 23, -2, -48, 96, 65, -105, 123, -14, -110, 69, -24, -120, -75, 74, 127, -60, 113, 90, -114, 105, 46, 27, -125, -23, -44, 64 };
/*     */ 
/*     */     ThreadedSeedGenerator()
/*     */     {
/* 278 */       this.pool = new byte[20];
/* 279 */       this.start = (this.end = 0);
/*     */       try
/*     */       {
/* 284 */         MessageDigest localMessageDigest = MessageDigest.getInstance("SHA");
/*     */       } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 286 */         throw new InternalError("internal error: SHA-1 not available.");
/*     */       }
/*     */ 
/* 289 */       final ThreadGroup[] arrayOfThreadGroup = new ThreadGroup[1];
/* 290 */       Thread localThread = (Thread)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public Thread run() {
/* 293 */           Object localObject = Thread.currentThread().getThreadGroup();
/*     */           ThreadGroup localThreadGroup;
/* 295 */           while ((localThreadGroup = ((ThreadGroup)localObject).getParent()) != null)
/* 296 */             localObject = localThreadGroup;
/* 297 */           arrayOfThreadGroup[0] = new ThreadGroup((ThreadGroup)localObject, "SeedGenerator ThreadGroup");
/*     */ 
/* 299 */           Thread localThread = new Thread(arrayOfThreadGroup[0], SeedGenerator.ThreadedSeedGenerator.this, "SeedGenerator Thread");
/*     */ 
/* 302 */           localThread.setPriority(1);
/* 303 */           localThread.setDaemon(true);
/* 304 */           return localThread;
/*     */         }
/*     */       });
/* 307 */       this.seedGroup = arrayOfThreadGroup[0];
/* 308 */       localThread.start();
/*     */     }
/*     */ 
/*     */     public final void run()
/*     */     {
/*     */       try
/*     */       {
/*     */         while (true)
/*     */         {
/* 319 */           synchronized (this) {
/* 320 */             if (this.count >= this.pool.length) {
/* 321 */               wait(); continue;
/*     */             }
/*     */           }
/*     */ 
/* 325 */           int k = 0;
/*     */           int j;
/* 328 */           for (int i = j = 0; (i < 64000) && (j < 6); 
/* 329 */             j++)
/*     */           {
/*     */             try
/*     */             {
/* 333 */               BogusThread localBogusThread = new BogusThread(null);
/* 334 */               Thread localThread = new Thread(this.seedGroup, localBogusThread, "SeedGenerator Thread");
/*     */ 
/* 336 */               localThread.start();
/*     */             } catch (Exception localException2) {
/* 338 */               throw new InternalError("internal error: SeedGenerator thread creation error.");
/*     */             }
/*     */ 
/* 344 */             int m = 0;
/* 345 */             m = 0;
/* 346 */             long l = System.currentTimeMillis() + 250L;
/* 347 */             while (System.currentTimeMillis() < l) {
/* 348 */               synchronized (this) {
/* 349 */               }m++;
/*     */             }
/*     */ 
/* 354 */             k = (byte)(k ^ rndTab[(m % 255)]);
/* 355 */             i += m;
/*     */           }
/*     */ 
/* 360 */           synchronized (this) {
/* 361 */             this.pool[this.end] = k;
/* 362 */             this.end += 1;
/* 363 */             this.count += 1;
/* 364 */             if (this.end >= this.pool.length) {
/* 365 */               this.end = 0;
/*     */             }
/* 367 */             notifyAll();
/*     */           }
/*     */         }
/*     */       } catch (Exception localException1) {  }
/*     */ 
/* 371 */       throw new InternalError("internal error: SeedGenerator thread generated an exception.");
/*     */     }
/*     */ 
/*     */     void getSeedBytes(byte[] paramArrayOfByte)
/*     */     {
/* 378 */       for (int i = 0; i < paramArrayOfByte.length; i++)
/* 379 */         paramArrayOfByte[i] = getSeedByte();
/*     */     }
/*     */ 
/*     */     byte getSeedByte()
/*     */     {
/* 384 */       byte b = 0;
/*     */       try
/*     */       {
/* 388 */         synchronized (this) {
/* 389 */           while (this.count <= 0)
/* 390 */             wait();
/*     */         }
/*     */       } catch (Exception ) {
/* 393 */         if (this.count <= 0) {
/* 394 */           throw new InternalError("internal error: SeedGenerator thread generated an exception.");
/*     */         }
/*     */       }
/*     */ 
/* 398 */       synchronized (this)
/*     */       {
/* 400 */         b = this.pool[this.start];
/* 401 */         this.pool[this.start] = 0;
/* 402 */         this.start += 1;
/* 403 */         this.count -= 1;
/* 404 */         if (this.start == this.pool.length) {
/* 405 */           this.start = 0;
/*     */         }
/*     */ 
/* 409 */         notifyAll();
/*     */       }
/*     */ 
/* 412 */       return b;
/*     */     }
/*     */ 
/*     */     private static class BogusThread
/*     */       implements Runnable
/*     */     {
/*     */       public final void run()
/*     */       {
/*     */         try
/*     */         {
/* 457 */           for (int i = 0; i < 5; i++)
/* 458 */             Thread.sleep(50L);
/*     */         }
/*     */         catch (Exception localException)
/*     */         {
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   static class URLSeedGenerator extends SeedGenerator
/*     */   {
/*     */     private String deviceName;
/*     */     private InputStream devRandom;
/*     */ 
/*     */     URLSeedGenerator(String paramString)
/*     */       throws IOException
/*     */     {
/* 478 */       if (paramString == null) {
/* 479 */         throw new IOException("No random source specified");
/*     */       }
/* 481 */       this.deviceName = paramString;
/* 482 */       init();
/*     */     }
/*     */ 
/*     */     URLSeedGenerator() throws IOException {
/* 486 */       this("file:/dev/random");
/*     */     }
/*     */ 
/*     */     private void init() throws IOException {
/* 490 */       final URL localURL = new URL(this.deviceName);
/*     */       try {
/* 492 */         this.devRandom = ((InputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */         {
/*     */           public InputStream run()
/*     */             throws IOException
/*     */           {
/* 503 */             if (localURL.getProtocol().equalsIgnoreCase("file")) {
/* 504 */               File localFile = SeedGenerator.URLSeedGenerator.this.getDeviceFile(localURL);
/* 505 */               return new FileInputStream(localFile);
/*     */             }
/* 507 */             return localURL.openStream();
/*     */           }
/*     */         }));
/*     */       }
/*     */       catch (Exception localException) {
/* 512 */         throw new IOException("Failed to open " + this.deviceName, localException.getCause());
/*     */       }
/*     */     }
/*     */ 
/*     */     private File getDeviceFile(URL paramURL)
/*     */       throws IOException
/*     */     {
/*     */       try
/*     */       {
/* 526 */         URI localURI1 = paramURL.toURI();
/* 527 */         if (localURI1.isOpaque())
/*     */         {
/* 529 */           URI localURI2 = new File(System.getProperty("user.dir")).toURI();
/* 530 */           String str = localURI2.toString() + localURI1.toString().substring(5);
/*     */ 
/* 532 */           return new File(URI.create(str));
/*     */         }
/* 534 */         return new File(localURI1);
/*     */       }
/*     */       catch (URISyntaxException localURISyntaxException)
/*     */       {
/*     */       }
/*     */ 
/* 541 */       return new File(paramURL.getPath());
/*     */     }
/*     */ 
/*     */     void getSeedBytes(byte[] paramArrayOfByte)
/*     */     {
/* 547 */       int i = paramArrayOfByte.length;
/* 548 */       int j = 0;
/*     */       try {
/* 550 */         while (j < i) {
/* 551 */           int k = this.devRandom.read(paramArrayOfByte, j, i - j);
/*     */ 
/* 553 */           if (k < 0) {
/* 554 */             throw new InternalError("URLSeedGenerator " + this.deviceName + " reached end of file");
/*     */           }
/* 556 */           j += k;
/*     */         }
/*     */       } catch (IOException localIOException) {
/* 559 */         throw new InternalError("URLSeedGenerator " + this.deviceName + " generated exception: " + localIOException.getMessage());
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.SeedGenerator
 * JD-Core Version:    0.6.2
 */