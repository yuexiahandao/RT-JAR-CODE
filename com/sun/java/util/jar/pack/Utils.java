/*     */ package com.sun.java.util.jar.pack;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FilterOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Map;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.JarInputStream;
/*     */ import java.util.jar.JarOutputStream;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.zip.ZipEntry;
/*     */ import sun.util.logging.PlatformLogger;
/*     */ 
/*     */ class Utils
/*     */ {
/*     */   static final String COM_PREFIX = "com.sun.java.util.jar.pack.";
/*     */   static final String METAINF = "META-INF";
/*     */   static final String DEBUG_VERBOSE = "com.sun.java.util.jar.pack.verbose";
/*     */   static final String DEBUG_DISABLE_NATIVE = "com.sun.java.util.jar.pack.disable.native";
/*     */   static final String PACK_DEFAULT_TIMEZONE = "com.sun.java.util.jar.pack.default.timezone";
/*     */   static final String UNPACK_MODIFICATION_TIME = "com.sun.java.util.jar.pack.unpack.modification.time";
/*     */   static final String UNPACK_STRIP_DEBUG = "com.sun.java.util.jar.pack.unpack.strip.debug";
/*     */   static final String UNPACK_REMOVE_PACKFILE = "com.sun.java.util.jar.pack.unpack.remove.packfile";
/*     */   static final String NOW = "now";
/*     */   static final String PACK_KEEP_CLASS_ORDER = "com.sun.java.util.jar.pack.keep.class.order";
/*     */   static final String PACK_ZIP_ARCHIVE_MARKER_COMMENT = "PACK200";
/* 135 */   static final ThreadLocal<TLGlobals> currentInstance = new ThreadLocal();
/*     */ 
/* 175 */   static final boolean nolog = Boolean.getBoolean("com.sun.java.util.jar.pack.nolog");
/*     */ 
/* 220 */   static final Pack200Logger log = new Pack200Logger("java.util.jar.Pack200");
/*     */ 
/*     */   static TLGlobals getTLGlobals()
/*     */   {
/* 139 */     return (TLGlobals)currentInstance.get();
/*     */   }
/*     */ 
/*     */   static Map<String, ConstantPool.Utf8Entry> getUtf8Entries() {
/* 143 */     return getTLGlobals().getUtf8Entries();
/*     */   }
/*     */ 
/*     */   static Map<String, ConstantPool.ClassEntry> getClassEntries() {
/* 147 */     return getTLGlobals().getClassEntries();
/*     */   }
/*     */ 
/*     */   static Map<Object, ConstantPool.LiteralEntry> getLiteralEntries() {
/* 151 */     return getTLGlobals().getLiteralEntries();
/*     */   }
/*     */ 
/*     */   static Map<String, ConstantPool.DescriptorEntry> getDescriptorEntries() {
/* 155 */     return getTLGlobals().getDescriptorEntries();
/*     */   }
/*     */ 
/*     */   static Map<String, ConstantPool.SignatureEntry> getSignatureEntries() {
/* 159 */     return getTLGlobals().getSignatureEntries();
/*     */   }
/*     */ 
/*     */   static Map<String, ConstantPool.MemberEntry> getMemberEntries() {
/* 163 */     return getTLGlobals().getMemberEntries();
/*     */   }
/*     */ 
/*     */   static PropMap currentPropMap() {
/* 167 */     Object localObject = currentInstance.get();
/* 168 */     if ((localObject instanceof PackerImpl))
/* 169 */       return ((PackerImpl)localObject).props;
/* 170 */     if ((localObject instanceof UnpackerImpl))
/* 171 */       return ((UnpackerImpl)localObject).props;
/* 172 */     return null;
/*     */   }
/*     */ 
/*     */   static String getVersionString()
/*     */   {
/* 225 */     return "Pack200, Vendor: " + System.getProperty("java.vendor") + ", Version: " + 160 + "." + 1;
/*     */   }
/*     */ 
/*     */   static void markJarFile(JarOutputStream paramJarOutputStream)
/*     */     throws IOException
/*     */   {
/* 233 */     paramJarOutputStream.setComment("PACK200");
/*     */   }
/*     */ 
/*     */   static void copyJarFile(JarInputStream paramJarInputStream, JarOutputStream paramJarOutputStream) throws IOException
/*     */   {
/* 238 */     if (paramJarInputStream.getManifest() != null) {
/* 239 */       localObject = new ZipEntry("META-INF/MANIFEST.MF");
/* 240 */       paramJarOutputStream.putNextEntry((ZipEntry)localObject);
/* 241 */       paramJarInputStream.getManifest().write(paramJarOutputStream);
/* 242 */       paramJarOutputStream.closeEntry();
/*     */     }
/* 244 */     Object localObject = new byte[16384];
/*     */     JarEntry localJarEntry;
/* 245 */     while ((localJarEntry = paramJarInputStream.getNextJarEntry()) != null) {
/* 246 */       paramJarOutputStream.putNextEntry(localJarEntry);
/*     */       int i;
/* 247 */       while (0 < (i = paramJarInputStream.read((byte[])localObject))) {
/* 248 */         paramJarOutputStream.write((byte[])localObject, 0, i);
/*     */       }
/*     */     }
/* 251 */     paramJarInputStream.close();
/* 252 */     markJarFile(paramJarOutputStream);
/*     */   }
/*     */   static void copyJarFile(JarFile paramJarFile, JarOutputStream paramJarOutputStream) throws IOException {
/* 255 */     byte[] arrayOfByte = new byte[16384];
/* 256 */     for (Enumeration localEnumeration = paramJarFile.entries(); localEnumeration.hasMoreElements(); ) {
/* 257 */       JarEntry localJarEntry = (JarEntry)localEnumeration.nextElement();
/* 258 */       paramJarOutputStream.putNextEntry(localJarEntry);
/* 259 */       InputStream localInputStream = paramJarFile.getInputStream(localJarEntry);
/*     */       int i;
/* 260 */       while (0 < (i = localInputStream.read(arrayOfByte))) {
/* 261 */         paramJarOutputStream.write(arrayOfByte, 0, i);
/*     */       }
/*     */     }
/* 264 */     paramJarFile.close();
/* 265 */     markJarFile(paramJarOutputStream);
/*     */   }
/*     */ 
/*     */   static void copyJarFile(JarInputStream paramJarInputStream, OutputStream paramOutputStream) throws IOException {
/* 269 */     paramOutputStream = new BufferedOutputStream(paramOutputStream);
/* 270 */     paramOutputStream = new NonCloser(paramOutputStream);
/* 271 */     JarOutputStream localJarOutputStream = new JarOutputStream(paramOutputStream); Object localObject1 = null;
/*     */     try { copyJarFile(paramJarInputStream, localJarOutputStream); }
/*     */     catch (Throwable localThrowable2)
/*     */     {
/* 271 */       localObject1 = localThrowable2; throw localThrowable2;
/*     */     } finally {
/* 273 */       if (localJarOutputStream != null) if (localObject1 != null) try { localJarOutputStream.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localJarOutputStream.close();  
/*     */     }
/*     */   }
/*     */ 
/*     */   static void copyJarFile(JarFile paramJarFile, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 278 */     paramOutputStream = new BufferedOutputStream(paramOutputStream);
/* 279 */     paramOutputStream = new NonCloser(paramOutputStream);
/* 280 */     JarOutputStream localJarOutputStream = new JarOutputStream(paramOutputStream); Object localObject1 = null;
/*     */     try { copyJarFile(paramJarFile, localJarOutputStream); }
/*     */     catch (Throwable localThrowable2)
/*     */     {
/* 280 */       localObject1 = localThrowable2; throw localThrowable2;
/*     */     } finally {
/* 282 */       if (localJarOutputStream != null) if (localObject1 != null) try { localJarOutputStream.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localJarOutputStream.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   static String getJarEntryName(String paramString)
/*     */   {
/* 291 */     if (paramString == null) return null;
/* 292 */     return paramString.replace(File.separatorChar, '/');
/*     */   }
/*     */ 
/*     */   static String zeString(ZipEntry paramZipEntry) {
/* 296 */     int i = paramZipEntry.getCompressedSize() > 0L ? (int)((1.0D - paramZipEntry.getCompressedSize() / paramZipEntry.getSize()) * 100.0D) : 0;
/*     */ 
/* 300 */     return paramZipEntry.getSize() + "\t" + paramZipEntry.getMethod() + "\t" + paramZipEntry.getCompressedSize() + "\t" + i + "%\t" + new Date(paramZipEntry.getTime()) + "\t" + Long.toHexString(paramZipEntry.getCrc()) + "\t" + paramZipEntry.getName();
/*     */   }
/*     */ 
/*     */   static byte[] readMagic(BufferedInputStream paramBufferedInputStream)
/*     */     throws IOException
/*     */   {
/* 311 */     paramBufferedInputStream.mark(4);
/* 312 */     byte[] arrayOfByte = new byte[4];
/* 313 */     for (int i = 0; i < arrayOfByte.length; i++)
/*     */     {
/* 315 */       if (1 != paramBufferedInputStream.read(arrayOfByte, i, 1))
/*     */         break;
/*     */     }
/* 318 */     paramBufferedInputStream.reset();
/* 319 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   static boolean isJarMagic(byte[] paramArrayOfByte)
/*     */   {
/* 324 */     return (paramArrayOfByte[0] == 80) && (paramArrayOfByte[1] == 75) && (paramArrayOfByte[2] >= 1) && (paramArrayOfByte[2] < 8) && (paramArrayOfByte[3] == paramArrayOfByte[2] + 1);
/*     */   }
/*     */ 
/*     */   static boolean isPackMagic(byte[] paramArrayOfByte)
/*     */   {
/* 331 */     return (paramArrayOfByte[0] == -54) && (paramArrayOfByte[1] == -2) && (paramArrayOfByte[2] == -48) && (paramArrayOfByte[3] == 13);
/*     */   }
/*     */ 
/*     */   static boolean isGZIPMagic(byte[] paramArrayOfByte)
/*     */   {
/* 337 */     return (paramArrayOfByte[0] == 31) && (paramArrayOfByte[1] == -117) && (paramArrayOfByte[2] == 8);
/*     */   }
/*     */ 
/*     */   private static class NonCloser extends FilterOutputStream
/*     */   {
/*     */     NonCloser(OutputStream paramOutputStream)
/*     */     {
/* 287 */       super(); } 
/* 288 */     public void close() throws IOException { flush(); }
/*     */ 
/*     */   }
/*     */ 
/*     */   static class Pack200Logger
/*     */   {
/*     */     private final String name;
/*     */     private PlatformLogger log;
/*     */ 
/*     */     Pack200Logger(String paramString)
/*     */     {
/* 183 */       this.name = paramString;
/*     */     }
/*     */ 
/*     */     private synchronized PlatformLogger getLogger() {
/* 187 */       if (this.log == null) {
/* 188 */         this.log = PlatformLogger.getLogger(this.name);
/*     */       }
/* 190 */       return this.log;
/*     */     }
/*     */ 
/*     */     public void warning(String paramString, Object paramObject) {
/* 194 */       getLogger().warning(paramString, new Object[] { paramObject });
/*     */     }
/*     */ 
/*     */     public void warning(String paramString) {
/* 198 */       warning(paramString, null);
/*     */     }
/*     */ 
/*     */     public void info(String paramString) {
/* 202 */       int i = Utils.currentPropMap().getInteger("com.sun.java.util.jar.pack.verbose");
/* 203 */       if (i > 0)
/* 204 */         if (Utils.nolog)
/* 205 */           System.out.println(paramString);
/*     */         else
/* 207 */           getLogger().info(paramString);
/*     */     }
/*     */ 
/*     */     public void fine(String paramString)
/*     */     {
/* 213 */       int i = Utils.currentPropMap().getInteger("com.sun.java.util.jar.pack.verbose");
/* 214 */       if (i > 0)
/* 215 */         System.out.println(paramString);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.Utils
 * JD-Core Version:    0.6.2
 */