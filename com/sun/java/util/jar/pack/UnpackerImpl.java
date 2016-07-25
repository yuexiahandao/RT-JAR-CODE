/*     */ package com.sun.java.util.jar.pack;
/*     */ 
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.HashSet;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TimeZone;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarInputStream;
/*     */ import java.util.jar.JarOutputStream;
/*     */ import java.util.jar.Pack200.Unpacker;
/*     */ import java.util.zip.CRC32;
/*     */ import java.util.zip.CheckedOutputStream;
/*     */ 
/*     */ public class UnpackerImpl extends TLGlobals
/*     */   implements Pack200.Unpacker
/*     */ {
/*     */   Object _nunp;
/*     */ 
/*     */   public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/*  64 */     this.props.addListener(paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/*  73 */     this.props.removeListener(paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public SortedMap properties()
/*     */   {
/*  86 */     return this.props;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  94 */     return Utils.getVersionString();
/*     */   }
/*     */ 
/*     */   public synchronized void unpack(InputStream paramInputStream, JarOutputStream paramJarOutputStream)
/*     */     throws IOException
/*     */   {
/* 111 */     if (paramInputStream == null) {
/* 112 */       throw new NullPointerException("null input");
/*     */     }
/* 114 */     if (paramJarOutputStream == null) {
/* 115 */       throw new NullPointerException("null output");
/*     */     }
/* 117 */     assert (Utils.currentInstance.get() == null);
/* 118 */     TimeZone localTimeZone = this.props.getBoolean("com.sun.java.util.jar.pack.default.timezone") ? null : TimeZone.getDefault();
/*     */     try
/*     */     {
/* 123 */       Utils.currentInstance.set(this);
/* 124 */       if (localTimeZone != null) TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
/* 125 */       int i = this.props.getInteger("com.sun.java.util.jar.pack.verbose");
/* 126 */       BufferedInputStream localBufferedInputStream = new BufferedInputStream(paramInputStream);
/* 127 */       if (Utils.isJarMagic(Utils.readMagic(localBufferedInputStream))) {
/* 128 */         if (i > 0)
/* 129 */           Utils.log.info("Copying unpacked JAR file...");
/* 130 */         Utils.copyJarFile(new JarInputStream(localBufferedInputStream), paramJarOutputStream);
/* 131 */       } else if (this.props.getBoolean("com.sun.java.util.jar.pack.disable.native")) {
/* 132 */         new DoUnpack(null).run(localBufferedInputStream, paramJarOutputStream);
/* 133 */         localBufferedInputStream.close();
/* 134 */         Utils.markJarFile(paramJarOutputStream);
/*     */       } else {
/* 136 */         new NativeUnpack(this).run(localBufferedInputStream, paramJarOutputStream);
/* 137 */         localBufferedInputStream.close();
/* 138 */         Utils.markJarFile(paramJarOutputStream);
/*     */       }
/*     */     } finally {
/* 141 */       this._nunp = null;
/* 142 */       Utils.currentInstance.set(null);
/* 143 */       if (localTimeZone != null) TimeZone.setDefault(localTimeZone);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void unpack(File paramFile, JarOutputStream paramJarOutputStream)
/*     */     throws IOException
/*     */   {
/* 156 */     if (paramFile == null) {
/* 157 */       throw new NullPointerException("null input");
/*     */     }
/* 159 */     if (paramJarOutputStream == null) {
/* 160 */       throw new NullPointerException("null output");
/*     */     }
/*     */ 
/* 164 */     FileInputStream localFileInputStream = new FileInputStream(paramFile); Object localObject1 = null;
/*     */     try { unpack(localFileInputStream, paramJarOutputStream); }
/*     */     catch (Throwable localThrowable2)
/*     */     {
/* 164 */       localObject1 = localThrowable2; throw localThrowable2;
/*     */     } finally {
/* 166 */       if (localFileInputStream != null) if (localObject1 != null) try { localFileInputStream.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localFileInputStream.close(); 
/*     */     }
/* 167 */     if (this.props.getBoolean("com.sun.java.util.jar.pack.unpack.remove.packfile"))
/* 168 */       paramFile.delete();  } 
/*     */   private class DoUnpack { final int verbose;
/*     */     final Package pkg;
/*     */     final boolean keepModtime;
/*     */     final boolean keepDeflateHint;
/*     */     final int modtime;
/*     */     final boolean deflateHint;
/*     */     final CRC32 crc;
/*     */     final ByteArrayOutputStream bufOut;
/*     */     final OutputStream crcOut;
/*     */ 
/* 173 */     private DoUnpack() { this.verbose = UnpackerImpl.this.props.getInteger("com.sun.java.util.jar.pack.verbose");
/*     */ 
/* 176 */       UnpackerImpl.this.props.setInteger("unpack.progress", 0);
/*     */ 
/* 180 */       this.pkg = new Package();
/*     */ 
/* 182 */       this.keepModtime = "keep".equals(UnpackerImpl.this.props.getProperty("com.sun.java.util.jar.pack.unpack.modification.time", "keep"));
/*     */ 
/* 185 */       this.keepDeflateHint = "keep".equals(UnpackerImpl.this.props.getProperty("unpack.deflate.hint", "keep"));
/*     */ 
/* 191 */       if (!this.keepModtime)
/* 192 */         this.modtime = UnpackerImpl.this.props.getTime("com.sun.java.util.jar.pack.unpack.modification.time");
/*     */       else {
/* 194 */         this.modtime = this.pkg.default_modtime;
/*     */       }
/*     */ 
/* 197 */       this.deflateHint = (this.keepDeflateHint ? false : UnpackerImpl.this.props.getBoolean("unpack.deflate.hint"));
/*     */ 
/* 202 */       this.crc = new CRC32();
/* 203 */       this.bufOut = new ByteArrayOutputStream();
/* 204 */       this.crcOut = new CheckedOutputStream(this.bufOut, this.crc); }
/*     */ 
/*     */     public void run(BufferedInputStream paramBufferedInputStream, JarOutputStream paramJarOutputStream) throws IOException {
/* 207 */       if (this.verbose > 0) {
/* 208 */         UnpackerImpl.this.props.list(System.out);
/*     */       }
/* 210 */       for (int i = 1; ; i++) {
/* 211 */         unpackSegment(paramBufferedInputStream, paramJarOutputStream);
/*     */ 
/* 214 */         if (!Utils.isPackMagic(Utils.readMagic(paramBufferedInputStream))) break;
/* 215 */         if (this.verbose > 0)
/* 216 */           Utils.log.info("Finished segment #" + i);
/*     */       }
/*     */     }
/*     */ 
/*     */     private void unpackSegment(InputStream paramInputStream, JarOutputStream paramJarOutputStream) throws IOException {
/* 221 */       UnpackerImpl.this.props.setProperty("unpack.progress", "0");
/*     */ 
/* 223 */       new PackageReader(this.pkg, paramInputStream).read();
/*     */ 
/* 225 */       if (UnpackerImpl.this.props.getBoolean("unpack.strip.debug")) this.pkg.stripAttributeKind("Debug");
/* 226 */       if (UnpackerImpl.this.props.getBoolean("unpack.strip.compile")) this.pkg.stripAttributeKind("Compile");
/* 227 */       UnpackerImpl.this.props.setProperty("unpack.progress", "50");
/* 228 */       this.pkg.ensureAllClassFiles();
/*     */ 
/* 230 */       HashSet localHashSet = new HashSet(this.pkg.getClasses());
/* 231 */       for (Package.File localFile : this.pkg.getFiles()) {
/* 232 */         String str = localFile.nameString;
/* 233 */         JarEntry localJarEntry = new JarEntry(Utils.getJarEntryName(str));
/*     */ 
/* 236 */         boolean bool = this.keepDeflateHint ? false : ((localFile.options & 0x1) != 0) || ((this.pkg.default_options & 0x20) != 0) ? true : this.deflateHint;
/*     */ 
/* 241 */         int i = !bool ? 1 : 0;
/*     */ 
/* 243 */         if (i != 0) this.crc.reset();
/* 244 */         this.bufOut.reset();
/* 245 */         if (localFile.isClassStub()) {
/* 246 */           Package.Class localClass = localFile.getStubClass();
/* 247 */           assert (localClass != null);
/* 248 */           new ClassWriter(localClass, i != 0 ? this.crcOut : this.bufOut).write();
/* 249 */           localHashSet.remove(localClass);
/*     */         }
/*     */         else {
/* 252 */           localFile.writeTo(i != 0 ? this.crcOut : this.bufOut);
/*     */         }
/* 254 */         localJarEntry.setMethod(bool ? 8 : 0);
/* 255 */         if (i != 0) {
/* 256 */           if (this.verbose > 0) {
/* 257 */             Utils.log.info("stored size=" + this.bufOut.size() + " and crc=" + this.crc.getValue());
/*     */           }
/* 259 */           localJarEntry.setMethod(0);
/* 260 */           localJarEntry.setSize(this.bufOut.size());
/* 261 */           localJarEntry.setCrc(this.crc.getValue());
/*     */         }
/* 263 */         if (this.keepModtime) {
/* 264 */           localJarEntry.setTime(localFile.modtime);
/*     */ 
/* 266 */           localJarEntry.setTime(localFile.modtime * 1000L);
/*     */         } else {
/* 268 */           localJarEntry.setTime(this.modtime * 1000L);
/*     */         }
/* 270 */         paramJarOutputStream.putNextEntry(localJarEntry);
/* 271 */         this.bufOut.writeTo(paramJarOutputStream);
/* 272 */         paramJarOutputStream.closeEntry();
/* 273 */         if (this.verbose > 0)
/* 274 */           Utils.log.info("Writing " + Utils.zeString(localJarEntry));
/*     */       }
/* 276 */       assert (localHashSet.isEmpty());
/* 277 */       UnpackerImpl.this.props.setProperty("unpack.progress", "100");
/* 278 */       this.pkg.reset();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.UnpackerImpl
 * JD-Core Version:    0.6.2
 */