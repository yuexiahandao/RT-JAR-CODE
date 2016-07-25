/*     */ package com.sun.java.util.jar.pack;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.security.AccessController;
/*     */ import java.util.jar.JarOutputStream;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.ZipEntry;
/*     */ import sun.security.action.LoadLibraryAction;
/*     */ 
/*     */ class NativeUnpack
/*     */ {
/*     */   private long unpackerPtr;
/*     */   private BufferedInputStream in;
/*     */   private int _verbose;
/*     */   private long _byteCount;
/*     */   private int _segCount;
/*     */   private int _fileCount;
/*     */   private long _estByteLimit;
/*     */   private int _estSegLimit;
/*     */   private int _estFileLimit;
/*  78 */   private int _prevPercent = -1;
/*     */ 
/*  80 */   private final CRC32 _crc32 = new CRC32();
/*  81 */   private byte[] _buf = new byte[16384];
/*     */   private UnpackerImpl _p200;
/*     */   private PropMap _props;
/*     */ 
/*     */   private static synchronized native void initIDs();
/*     */ 
/*     */   private synchronized native long start(ByteBuffer paramByteBuffer, long paramLong);
/*     */ 
/*     */   private synchronized native boolean getNextFile(Object[] paramArrayOfObject);
/*     */ 
/*     */   private synchronized native ByteBuffer getUnusedInput();
/*     */ 
/*     */   private synchronized native long finish();
/*     */ 
/*     */   protected synchronized native boolean setOption(String paramString1, String paramString2);
/*     */ 
/*     */   protected synchronized native String getOption(String paramString);
/*     */ 
/*     */   NativeUnpack(UnpackerImpl paramUnpackerImpl)
/*     */   {
/*  96 */     this._p200 = paramUnpackerImpl;
/*  97 */     this._props = paramUnpackerImpl.props;
/*  98 */     paramUnpackerImpl._nunp = this;
/*     */   }
/*     */ 
/*     */   private static Object currentInstance()
/*     */   {
/* 103 */     UnpackerImpl localUnpackerImpl = (UnpackerImpl)Utils.getTLGlobals();
/* 104 */     return localUnpackerImpl == null ? null : localUnpackerImpl._nunp;
/*     */   }
/*     */ 
/*     */   private synchronized long getUnpackerPtr() {
/* 108 */     return this.unpackerPtr;
/*     */   }
/*     */ 
/*     */   private long readInputFn(ByteBuffer paramByteBuffer, long paramLong) throws IOException
/*     */   {
/* 113 */     if (this.in == null) return 0L;
/* 114 */     long l1 = paramByteBuffer.capacity() - paramByteBuffer.position();
/* 115 */     assert (paramLong <= l1);
/* 116 */     long l2 = 0L;
/* 117 */     int i = 0;
/* 118 */     while (l2 < paramLong) {
/* 119 */       i++;
/*     */ 
/* 121 */       int j = this._buf.length;
/* 122 */       if (j > l1 - l2)
/* 123 */         j = (int)(l1 - l2);
/* 124 */       int k = this.in.read(this._buf, 0, j);
/* 125 */       if (k <= 0) break;
/* 126 */       l2 += k;
/* 127 */       assert (l2 <= l1);
/*     */ 
/* 129 */       paramByteBuffer.put(this._buf, 0, k);
/*     */     }
/* 131 */     if (this._verbose > 1)
/* 132 */       Utils.log.fine("readInputFn(" + paramLong + "," + l1 + ") => " + l2 + " steps=" + i);
/* 133 */     if (l1 > 100L)
/* 134 */       this._estByteLimit = (this._byteCount + l1);
/*     */     else {
/* 136 */       this._estByteLimit = ((this._byteCount + l2) * 20L);
/*     */     }
/* 138 */     this._byteCount += l2;
/* 139 */     updateProgress();
/* 140 */     return l2;
/*     */   }
/*     */ 
/*     */   private void updateProgress()
/*     */   {
/* 147 */     double d1 = this._segCount;
/* 148 */     if ((this._estByteLimit > 0L) && (this._byteCount > 0L))
/* 149 */       d1 += this._byteCount / this._estByteLimit;
/* 150 */     double d2 = this._fileCount;
/* 151 */     double d3 = 0.33D * d1 / Math.max(this._estSegLimit, 1) + 0.67D * d2 / Math.max(this._estFileLimit, 1);
/*     */ 
/* 154 */     int i = (int)Math.round(100.0D * d3);
/* 155 */     if (i > 100) i = 100;
/* 156 */     if (i > this._prevPercent) {
/* 157 */       this._prevPercent = i;
/* 158 */       this._props.setInteger("unpack.progress", i);
/* 159 */       if (this._verbose > 0)
/* 160 */         Utils.log.info("progress = " + i);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void copyInOption(String paramString) {
/* 165 */     String str = this._props.getProperty(paramString);
/* 166 */     if (this._verbose > 0)
/* 167 */       Utils.log.info("set " + paramString + "=" + str);
/* 168 */     if (str != null) {
/* 169 */       boolean bool = setOption(paramString, str);
/* 170 */       if (!bool)
/* 171 */         Utils.log.warning("Invalid option " + paramString + "=" + str);
/*     */     }
/*     */   }
/*     */ 
/*     */   void run(InputStream paramInputStream, JarOutputStream paramJarOutputStream, ByteBuffer paramByteBuffer) throws IOException
/*     */   {
/* 177 */     BufferedInputStream localBufferedInputStream = new BufferedInputStream(paramInputStream);
/* 178 */     this.in = localBufferedInputStream;
/* 179 */     this._verbose = this._props.getInteger("com.sun.java.util.jar.pack.verbose");
/*     */ 
/* 183 */     int i = "keep".equals(this._props.getProperty("com.sun.java.util.jar.pack.unpack.modification.time", "0")) ? 0 : this._props.getTime("com.sun.java.util.jar.pack.unpack.modification.time");
/*     */ 
/* 186 */     copyInOption("com.sun.java.util.jar.pack.verbose");
/* 187 */     copyInOption("unpack.deflate.hint");
/* 188 */     if (i == 0)
/* 189 */       copyInOption("com.sun.java.util.jar.pack.unpack.modification.time");
/* 190 */     updateProgress();
/*     */     while (true)
/*     */     {
/* 193 */       long l1 = start(paramByteBuffer, 0L);
/* 194 */       this._byteCount = (this._estByteLimit = 0L);
/* 195 */       this._segCount += 1;
/* 196 */       int j = (int)(l1 >>> 32);
/* 197 */       int k = (int)(l1 >>> 0);
/*     */ 
/* 200 */       this._estSegLimit = (this._segCount + j);
/* 201 */       double d = this._fileCount + k;
/* 202 */       this._estFileLimit = ((int)(d * this._estSegLimit / this._segCount));
/*     */ 
/* 206 */       int[] arrayOfInt = { 0, 0, 0, 0 };
/*     */ 
/* 208 */       Object[] arrayOfObject = { arrayOfInt, null, null, null };
/*     */ 
/* 210 */       while (getNextFile(arrayOfObject))
/*     */       {
/* 212 */         String str = (String)arrayOfObject[1];
/* 213 */         long l3 = (arrayOfInt[0] << 32) + (arrayOfInt[1] << 32 >>> 32);
/*     */ 
/* 216 */         long l4 = i != 0 ? i : arrayOfInt[2];
/*     */ 
/* 218 */         boolean bool = arrayOfInt[3] != 0;
/* 219 */         ByteBuffer localByteBuffer1 = (ByteBuffer)arrayOfObject[2];
/* 220 */         ByteBuffer localByteBuffer2 = (ByteBuffer)arrayOfObject[3];
/* 221 */         writeEntry(paramJarOutputStream, str, l4, l3, bool, localByteBuffer1, localByteBuffer2);
/*     */ 
/* 223 */         this._fileCount += 1;
/* 224 */         updateProgress();
/*     */       }
/* 226 */       paramByteBuffer = getUnusedInput();
/* 227 */       long l2 = finish();
/* 228 */       if (this._verbose > 0)
/* 229 */         Utils.log.info("bytes consumed = " + l2);
/* 230 */       if ((paramByteBuffer == null) && (!Utils.isPackMagic(Utils.readMagic(localBufferedInputStream))))
/*     */       {
/*     */         break;
/*     */       }
/* 234 */       if ((this._verbose > 0) && 
/* 235 */         (paramByteBuffer != null))
/* 236 */         Utils.log.info("unused input = " + paramByteBuffer);
/*     */     }
/*     */   }
/*     */ 
/*     */   void run(InputStream paramInputStream, JarOutputStream paramJarOutputStream) throws IOException
/*     */   {
/* 242 */     run(paramInputStream, paramJarOutputStream, null);
/*     */   }
/*     */ 
/*     */   void run(File paramFile, JarOutputStream paramJarOutputStream) throws IOException
/*     */   {
/* 247 */     ByteBuffer localByteBuffer = null;
/* 248 */     FileInputStream localFileInputStream = new FileInputStream(paramFile); Object localObject1 = null;
/*     */     try { run(localFileInputStream, paramJarOutputStream, localByteBuffer); }
/*     */     catch (Throwable localThrowable2)
/*     */     {
/* 248 */       localObject1 = localThrowable2; throw localThrowable2;
/*     */     } finally {
/* 250 */       if (localFileInputStream != null) if (localObject1 != null) try { localFileInputStream.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localFileInputStream.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeEntry(JarOutputStream paramJarOutputStream, String paramString, long paramLong1, long paramLong2, boolean paramBoolean, ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2)
/*     */     throws IOException
/*     */   {
/* 257 */     int i = (int)paramLong2;
/* 258 */     if (i != paramLong2) {
/* 259 */       throw new IOException("file too large: " + paramLong2);
/*     */     }
/* 261 */     CRC32 localCRC32 = this._crc32;
/*     */ 
/* 263 */     if (this._verbose > 1) {
/* 264 */       Utils.log.fine("Writing entry: " + paramString + " size=" + i + (paramBoolean ? " deflated" : ""));
/*     */     }
/*     */ 
/* 267 */     if (this._buf.length < i) {
/* 268 */       j = i;
/* 269 */       while (j < this._buf.length) {
/* 270 */         j <<= 1;
/* 271 */         if (j <= 0) {
/* 272 */           j = i;
/*     */         }
/*     */       }
/*     */ 
/* 276 */       this._buf = new byte[j];
/*     */     }
/* 278 */     assert (this._buf.length >= i);
/*     */ 
/* 280 */     int j = 0;
/*     */     int k;
/* 281 */     if (paramByteBuffer1 != null) {
/* 282 */       k = paramByteBuffer1.capacity();
/* 283 */       paramByteBuffer1.get(this._buf, j, k);
/* 284 */       j += k;
/*     */     }
/* 286 */     if (paramByteBuffer2 != null) {
/* 287 */       k = paramByteBuffer2.capacity();
/* 288 */       paramByteBuffer2.get(this._buf, j, k);
/* 289 */       j += k;
/*     */     }
/* 291 */     while (j < i)
/*     */     {
/* 293 */       k = this.in.read(this._buf, j, i - j);
/* 294 */       if (k <= 0) throw new IOException("EOF at end of archive");
/* 295 */       j += k;
/*     */     }
/*     */ 
/* 298 */     ZipEntry localZipEntry = new ZipEntry(paramString);
/* 299 */     localZipEntry.setTime(paramLong1 * 1000L);
/*     */ 
/* 301 */     if (i == 0) {
/* 302 */       localZipEntry.setMethod(0);
/* 303 */       localZipEntry.setSize(0L);
/* 304 */       localZipEntry.setCrc(0L);
/* 305 */       localZipEntry.setCompressedSize(0L);
/* 306 */     } else if (!paramBoolean) {
/* 307 */       localZipEntry.setMethod(0);
/* 308 */       localZipEntry.setSize(i);
/* 309 */       localZipEntry.setCompressedSize(i);
/* 310 */       localCRC32.reset();
/* 311 */       localCRC32.update(this._buf, 0, i);
/* 312 */       localZipEntry.setCrc(localCRC32.getValue());
/*     */     } else {
/* 314 */       localZipEntry.setMethod(8);
/* 315 */       localZipEntry.setSize(i);
/*     */     }
/*     */ 
/* 318 */     paramJarOutputStream.putNextEntry(localZipEntry);
/*     */ 
/* 320 */     if (i > 0) {
/* 321 */       paramJarOutputStream.write(this._buf, 0, i);
/*     */     }
/* 323 */     paramJarOutputStream.closeEntry();
/* 324 */     if (this._verbose > 0) Utils.log.info("Writing " + Utils.zeString(localZipEntry));
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  89 */     AccessController.doPrivileged(new LoadLibraryAction("unpack"));
/*     */ 
/*  91 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.NativeUnpack
 * JD-Core Version:    0.6.2
 */