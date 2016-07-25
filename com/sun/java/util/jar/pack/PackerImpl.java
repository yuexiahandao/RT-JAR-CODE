/*     */ package com.sun.java.util.jar.pack;
/*     */ 
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.ListIterator;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.SortedMap;
/*     */ import java.util.TimeZone;
/*     */ import java.util.jar.JarEntry;
/*     */ import java.util.jar.JarFile;
/*     */ import java.util.jar.JarInputStream;
/*     */ import java.util.jar.Manifest;
/*     */ import java.util.jar.Pack200.Packer;
/*     */ 
/*     */ public class PackerImpl extends TLGlobals
/*     */   implements Pack200.Packer
/*     */ {
/*     */   public SortedMap properties()
/*     */   {
/*  74 */     return this.props;
/*     */   }
/*     */ 
/*     */   public synchronized void pack(JarFile paramJarFile, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/*  88 */     assert (Utils.currentInstance.get() == null);
/*  89 */     TimeZone localTimeZone = this.props.getBoolean("com.sun.java.util.jar.pack.default.timezone") ? null : TimeZone.getDefault();
/*     */     try
/*     */     {
/*  93 */       Utils.currentInstance.set(this);
/*  94 */       if (localTimeZone != null) TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
/*     */ 
/*  96 */       if ("0".equals(this.props.getProperty("pack.effort")))
/*  97 */         Utils.copyJarFile(paramJarFile, paramOutputStream);
/*     */       else
/*  99 */         new DoPack(null).run(paramJarFile, paramOutputStream);
/*     */     }
/*     */     finally {
/* 102 */       Utils.currentInstance.set(null);
/* 103 */       if (localTimeZone != null) TimeZone.setDefault(localTimeZone);
/* 104 */       paramJarFile.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void pack(JarInputStream paramJarInputStream, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 123 */     assert (Utils.currentInstance.get() == null);
/* 124 */     TimeZone localTimeZone = this.props.getBoolean("com.sun.java.util.jar.pack.default.timezone") ? null : TimeZone.getDefault();
/*     */     try
/*     */     {
/* 127 */       Utils.currentInstance.set(this);
/* 128 */       if (localTimeZone != null) TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
/* 129 */       if ("0".equals(this.props.getProperty("pack.effort")))
/* 130 */         Utils.copyJarFile(paramJarInputStream, paramOutputStream);
/*     */       else
/* 132 */         new DoPack(null).run(paramJarInputStream, paramOutputStream);
/*     */     }
/*     */     finally {
/* 135 */       Utils.currentInstance.set(null);
/* 136 */       if (localTimeZone != null) TimeZone.setDefault(localTimeZone);
/* 137 */       paramJarInputStream.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addPropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 145 */     this.props.addListener(paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public void removePropertyChangeListener(PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 153 */     this.props.removeListener(paramPropertyChangeListener); } 
/*     */   private class DoPack { final int verbose;
/*     */     final Package pkg;
/*     */     final String unknownAttrCommand;
/*     */     final Map<Attribute.Layout, Attribute> attrDefs;
/*     */     final Map<Attribute.Layout, String> attrCommands;
/*     */     final boolean keepFileOrder;
/*     */     final boolean keepClassOrder;
/*     */     final boolean keepModtime;
/*     */     final boolean latestModtime;
/*     */     final boolean keepDeflateHint;
/*     */     long totalOutputSize;
/*     */     int segmentCount;
/*     */     long segmentTotalSize;
/*     */     long segmentSize;
/*     */     final long segmentLimit;
/*     */     final List<String> passFiles;
/*     */     private int nread;
/*     */ 
/* 162 */     private DoPack() { this.verbose = PackerImpl.this.props.getInteger("com.sun.java.util.jar.pack.verbose");
/*     */ 
/* 165 */       PackerImpl.this.props.setInteger("pack.progress", 0);
/* 166 */       if (this.verbose > 0) Utils.log.info(PackerImpl.this.props.toString());
/*     */ 
/* 170 */       this.pkg = new Package();
/*     */ 
/* 174 */       Object localObject1 = PackerImpl.this.props.getProperty("pack.unknown.attribute", "pass");
/* 175 */       if ((!"strip".equals(localObject1)) && (!"pass".equals(localObject1)) && (!"error".equals(localObject1)))
/*     */       {
/* 178 */         throw new RuntimeException("Bad option: pack.unknown.attribute = " + (String)localObject1);
/*     */       }
/* 180 */       this.unknownAttrCommand = ((String)localObject1).intern();
/*     */ 
/* 186 */       localObject1 = new HashMap();
/* 187 */       Object localObject2 = new HashMap();
/* 188 */       String[] arrayOfString = { "pack.class.attribute.", "pack.field.attribute.", "pack.method.attribute.", "pack.code.attribute." };
/*     */ 
/* 194 */       int[] arrayOfInt = { 0, 1, 2, 3 };
/*     */       String str1;
/*     */       Iterator localIterator;
/* 200 */       for (int k = 0; k < arrayOfInt.length; k++) {
/* 201 */         str1 = arrayOfString[k];
/* 202 */         SortedMap localSortedMap = PackerImpl.this.props.prefixMap(str1);
/* 203 */         for (localIterator = localSortedMap.keySet().iterator(); localIterator.hasNext(); ) { Object localObject3 = localIterator.next();
/* 204 */           String str2 = (String)localObject3;
/* 205 */           assert (str2.startsWith(str1));
/* 206 */           String str3 = str2.substring(str1.length());
/* 207 */           String str4 = PackerImpl.this.props.getProperty(str2);
/* 208 */           Attribute.Layout localLayout = Attribute.keyForLookup(arrayOfInt[k], str3);
/* 209 */           if (("strip".equals(str4)) || ("pass".equals(str4)) || ("error".equals(str4)))
/*     */           {
/* 212 */             ((Map)localObject2).put(localLayout, str4.intern());
/*     */           } else {
/* 214 */             Attribute.define((Map)localObject1, arrayOfInt[k], str3, str4);
/* 215 */             if (this.verbose > 1) {
/* 216 */               Utils.log.fine("Added layout for " + Constants.ATTR_CONTEXT_NAME[k] + " attribute " + str3 + " = " + str4);
/*     */             }
/* 218 */             assert (((Map)localObject1).containsKey(localLayout));
/*     */           }
/*     */         }
/*     */       }
/* 222 */       this.attrDefs = (((Map)localObject1).isEmpty() ? null : (Map)localObject1);
/* 223 */       this.attrCommands = (((Map)localObject2).isEmpty() ? null : (Map)localObject2);
/*     */ 
/* 226 */       this.keepFileOrder = PackerImpl.this.props.getBoolean("pack.keep.file.order");
/*     */ 
/* 228 */       this.keepClassOrder = PackerImpl.this.props.getBoolean("com.sun.java.util.jar.pack.keep.class.order");
/*     */ 
/* 231 */       this.keepModtime = "keep".equals(PackerImpl.this.props.getProperty("pack.modification.time"));
/*     */ 
/* 233 */       this.latestModtime = "latest".equals(PackerImpl.this.props.getProperty("pack.modification.time"));
/*     */ 
/* 235 */       this.keepDeflateHint = "keep".equals(PackerImpl.this.props.getProperty("pack.deflate.hint"));
/*     */ 
/* 238 */       if ((!this.keepModtime) && (!this.latestModtime)) {
/* 239 */         int i = PackerImpl.this.props.getTime("pack.modification.time");
/* 240 */         if (i != 0) {
/* 241 */           this.pkg.default_modtime = i;
/*     */         }
/*     */       }
/* 244 */       if (!this.keepDeflateHint) {
/* 245 */         boolean bool = PackerImpl.this.props.getBoolean("pack.deflate.hint");
/* 246 */         if (bool) {
/* 247 */           this.pkg.default_options |= 32;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 252 */       this.totalOutputSize = 0L;
/* 253 */       this.segmentCount = 0;
/* 254 */       this.segmentTotalSize = 0L;
/* 255 */       this.segmentSize = 0L;
/*     */ 
/* 259 */       if (PackerImpl.this.props.getProperty("pack.segment.limit", "").equals(""))
/* 260 */         l = -1L;
/*     */       else
/* 262 */         l = PackerImpl.this.props.getLong("pack.segment.limit");
/* 263 */       long l = Math.min(2147483647L, l);
/* 264 */       l = Math.max(-1L, l);
/* 265 */       if (l == -1L)
/* 266 */         l = 9223372036854775807L;
/* 267 */       this.segmentLimit = l;
/*     */ 
/* 273 */       this.passFiles = PackerImpl.this.props.getProperties("pack.pass.file.");
/* 274 */       for (ListIterator localListIterator = this.passFiles.listIterator(); localListIterator.hasNext(); ) {
/* 275 */         localObject2 = (String)localListIterator.next();
/* 276 */         if (localObject2 == null) { localListIterator.remove(); } else {
/* 277 */           localObject2 = Utils.getJarEntryName((String)localObject2);
/* 278 */           if (((String)localObject2).endsWith("/"))
/* 279 */             localObject2 = ((String)localObject2).substring(0, ((String)localObject2).length() - 1);
/* 280 */           localListIterator.set(localObject2);
/*     */         }
/*     */       }
/* 282 */       if (this.verbose > 0) Utils.log.info("passFiles = " + this.passFiles);
/*     */ 
/* 288 */       if ((j = PackerImpl.this.props.getInteger("com.sun.java.util.jar.pack.min.class.majver")) != 0)
/* 289 */         this.pkg.min_class_majver = ((short)j);
/* 290 */       if ((j = PackerImpl.this.props.getInteger("com.sun.java.util.jar.pack.min.class.minver")) != 0)
/* 291 */         this.pkg.min_class_minver = ((short)j);
/* 292 */       if ((j = PackerImpl.this.props.getInteger("com.sun.java.util.jar.pack.max.class.majver")) != 0)
/* 293 */         this.pkg.max_class_majver = ((short)j);
/* 294 */       if ((j = PackerImpl.this.props.getInteger("com.sun.java.util.jar.pack.max.class.minver")) != 0)
/* 295 */         this.pkg.max_class_minver = ((short)j);
/* 296 */       if ((j = PackerImpl.this.props.getInteger("com.sun.java.util.jar.pack.package.minver")) != 0)
/* 297 */         this.pkg.package_minver = ((short)j);
/* 298 */       if ((j = PackerImpl.this.props.getInteger("com.sun.java.util.jar.pack.package.majver")) != 0) {
/* 299 */         this.pkg.package_majver = ((short)j);
/*     */       }
/*     */ 
/* 304 */       int j = PackerImpl.this.props.getInteger("com.sun.java.util.jar.pack.archive.options");
/* 305 */       if (j != 0) {
/* 306 */         this.pkg.default_options |= j;
/*     */       }
/*     */ 
/* 402 */       this.nread = 0;
/*     */     }
/*     */ 
/*     */     boolean isClassFile(String paramString)
/*     */     {
/* 312 */       if (!paramString.endsWith(".class")) return false;
/* 313 */       String str = paramString;
/*     */       while (true) { if (this.passFiles.contains(str)) return false;
/* 315 */         int i = str.lastIndexOf('/');
/* 316 */         if (i < 0) break;
/* 317 */         str = str.substring(0, i);
/*     */       }
/* 319 */       return true;
/*     */     }
/*     */ 
/*     */     boolean isMetaInfFile(String paramString) {
/* 323 */       return (paramString.startsWith("/META-INF")) || (paramString.startsWith("META-INF"));
/*     */     }
/*     */ 
/*     */     private void makeNextPackage()
/*     */     {
/* 329 */       this.pkg.reset();
/*     */     }
/*     */ 
/*     */     private void noteRead(InFile paramInFile)
/*     */     {
/* 404 */       this.nread += 1;
/* 405 */       if (this.verbose > 2)
/* 406 */         Utils.log.fine("...read " + paramInFile.name);
/* 407 */       if ((this.verbose > 0) && (this.nread % 1000 == 0))
/* 408 */         Utils.log.info("Have read " + this.nread + " files...");
/*     */     }
/*     */ 
/*     */     void run(JarInputStream paramJarInputStream, OutputStream paramOutputStream)
/*     */       throws IOException
/*     */     {
/*     */       Object localObject1;
/*     */       Object localObject2;
/* 414 */       if (paramJarInputStream.getManifest() != null) {
/* 415 */         localObject1 = new ByteArrayOutputStream();
/* 416 */         paramJarInputStream.getManifest().write((OutputStream)localObject1);
/* 417 */         localObject2 = new ByteArrayInputStream(((ByteArrayOutputStream)localObject1).toByteArray());
/* 418 */         this.pkg.addFile(readFile("META-INF/MANIFEST.MF", (InputStream)localObject2));
/*     */       }
/* 420 */       while ((localObject1 = paramJarInputStream.getNextJarEntry()) != null) {
/* 421 */         localObject2 = new InFile((JarEntry)localObject1);
/*     */ 
/* 423 */         String str = ((InFile)localObject2).name;
/* 424 */         Package.File localFile1 = readFile(str, paramJarInputStream);
/* 425 */         Package.File localFile2 = null;
/*     */ 
/* 428 */         long l = isMetaInfFile(str) ? 0L : ((InFile)localObject2).getInputLength();
/*     */ 
/* 432 */         if (this.segmentSize += l > this.segmentLimit) {
/* 433 */           this.segmentSize -= l;
/* 434 */           int i = -1;
/* 435 */           flushPartial(paramOutputStream, i);
/*     */         }
/* 437 */         if (this.verbose > 1) {
/* 438 */           Utils.log.fine("Reading " + str);
/*     */         }
/*     */ 
/* 441 */         assert (((JarEntry)localObject1).isDirectory() == str.endsWith("/"));
/*     */ 
/* 443 */         if (isClassFile(str)) {
/* 444 */           localFile2 = readClass(str, localFile1.getInputStream());
/*     */         }
/* 446 */         if (localFile2 == null) {
/* 447 */           localFile2 = localFile1;
/* 448 */           this.pkg.addFile(localFile2);
/*     */         }
/* 450 */         ((InFile)localObject2).copyTo(localFile2);
/* 451 */         noteRead((InFile)localObject2);
/*     */       }
/* 453 */       flushAll(paramOutputStream);
/*     */     }
/*     */ 
/*     */     void run(JarFile paramJarFile, OutputStream paramOutputStream) throws IOException {
/* 457 */       List localList = scanJar(paramJarFile);
/*     */ 
/* 459 */       if (this.verbose > 0) {
/* 460 */         Utils.log.info("Reading " + localList.size() + " files...");
/*     */       }
/* 462 */       int i = 0;
/* 463 */       for (InFile localInFile : localList) {
/* 464 */         String str = localInFile.name;
/*     */ 
/* 466 */         long l = isMetaInfFile(str) ? 0L : localInFile.getInputLength();
/*     */ 
/* 469 */         if (this.segmentSize += l > this.segmentLimit) {
/* 470 */           this.segmentSize -= l;
/*     */ 
/* 472 */           float f1 = i + 1;
/* 473 */           float f2 = this.segmentCount + 1;
/* 474 */           float f3 = localList.size() - f1;
/* 475 */           float f4 = f3 * (f2 / f1);
/* 476 */           if (this.verbose > 1)
/* 477 */             Utils.log.fine("Estimated segments to do: " + f4);
/* 478 */           flushPartial(paramOutputStream, (int)Math.ceil(f4));
/*     */         }
/* 480 */         InputStream localInputStream = localInFile.getInputStream();
/* 481 */         if (this.verbose > 1)
/* 482 */           Utils.log.fine("Reading " + str);
/* 483 */         Package.File localFile = null;
/* 484 */         if (isClassFile(str)) {
/* 485 */           localFile = readClass(str, localInputStream);
/* 486 */           if (localFile == null) {
/* 487 */             localInputStream.close();
/* 488 */             localInputStream = localInFile.getInputStream();
/*     */           }
/*     */         }
/* 491 */         if (localFile == null) {
/* 492 */           localFile = readFile(str, localInputStream);
/* 493 */           this.pkg.addFile(localFile);
/*     */         }
/* 495 */         localInFile.copyTo(localFile);
/* 496 */         localInputStream.close();
/* 497 */         noteRead(localInFile);
/* 498 */         i++;
/*     */       }
/* 500 */       flushAll(paramOutputStream);
/*     */     }
/*     */ 
/*     */     Package.File readClass(String paramString, InputStream paramInputStream)
/*     */       throws IOException
/*     */     {
/*     */       Package tmp8_5 = this.pkg; tmp8_5.getClass(); Package.Class localClass = new Package.Class(tmp8_5, paramString);
/* 505 */       paramInputStream = new BufferedInputStream(paramInputStream);
/* 506 */       ClassReader localClassReader = new ClassReader(localClass, paramInputStream);
/* 507 */       localClassReader.setAttrDefs(this.attrDefs);
/* 508 */       localClassReader.setAttrCommands(this.attrCommands);
/* 509 */       localClassReader.unknownAttrCommand = this.unknownAttrCommand;
/*     */       try {
/* 511 */         localClassReader.read();
/*     */       } catch (IOException localIOException) {
/* 513 */         String str = "Passing class file uncompressed due to";
/*     */         Object localObject;
/* 514 */         if ((localIOException instanceof Attribute.FormatException)) {
/* 515 */           localObject = (Attribute.FormatException)localIOException;
/*     */ 
/* 517 */           if (((Attribute.FormatException)localObject).layout.equals("pass")) {
/* 518 */             Utils.log.info(((Attribute.FormatException)localObject).toString());
/* 519 */             Utils.log.warning(str + " unrecognized attribute: " + paramString);
/*     */ 
/* 521 */             return null;
/*     */           }
/* 523 */         } else if ((localIOException instanceof ClassReader.ClassFormatException)) {
/* 524 */           localObject = (ClassReader.ClassFormatException)localIOException;
/*     */ 
/* 526 */           if (this.unknownAttrCommand.equals("pass")) {
/* 527 */             Utils.log.info(((ClassReader.ClassFormatException)localObject).toString());
/* 528 */             Utils.log.warning(str + " unknown class format: " + paramString);
/*     */ 
/* 530 */             return null;
/*     */           }
/*     */         }
/*     */ 
/* 534 */         throw localIOException;
/*     */       }
/* 536 */       this.pkg.addClass(localClass);
/* 537 */       return localClass.file;
/*     */     }
/*     */ 
/*     */     Package.File readFile(String paramString, InputStream paramInputStream)
/*     */       throws IOException
/*     */     {
/*     */       Package tmp8_5 = this.pkg; tmp8_5.getClass(); Package.File localFile = new Package.File(tmp8_5, paramString);
/* 544 */       localFile.readFrom(paramInputStream);
/* 545 */       if ((localFile.isDirectory()) && (localFile.getFileLength() != 0L))
/* 546 */         throw new IllegalArgumentException("Non-empty directory: " + localFile.getFileName());
/* 547 */       return localFile;
/*     */     }
/*     */ 
/*     */     void flushPartial(OutputStream paramOutputStream, int paramInt) throws IOException {
/* 551 */       if ((this.pkg.files.isEmpty()) && (this.pkg.classes.isEmpty())) {
/* 552 */         return;
/*     */       }
/* 554 */       flushPackage(paramOutputStream, Math.max(1, paramInt));
/* 555 */       PackerImpl.this.props.setInteger("pack.progress", 25);
/*     */ 
/* 557 */       makeNextPackage();
/* 558 */       this.segmentCount += 1;
/* 559 */       this.segmentTotalSize += this.segmentSize;
/* 560 */       this.segmentSize = 0L;
/*     */     }
/*     */ 
/*     */     void flushAll(OutputStream paramOutputStream) throws IOException {
/* 564 */       PackerImpl.this.props.setInteger("pack.progress", 50);
/* 565 */       flushPackage(paramOutputStream, 0);
/* 566 */       paramOutputStream.flush();
/* 567 */       PackerImpl.this.props.setInteger("pack.progress", 100);
/* 568 */       this.segmentCount += 1;
/* 569 */       this.segmentTotalSize += this.segmentSize;
/* 570 */       this.segmentSize = 0L;
/* 571 */       if ((this.verbose > 0) && (this.segmentCount > 1))
/* 572 */         Utils.log.info("Transmitted " + this.segmentTotalSize + " input bytes in " + this.segmentCount + " segments totaling " + this.totalOutputSize + " bytes");
/*     */     }
/*     */ 
/*     */     void flushPackage(OutputStream paramOutputStream, int paramInt)
/*     */       throws IOException
/*     */     {
/* 584 */       int i = this.pkg.files.size();
/* 585 */       if (!this.keepFileOrder)
/*     */       {
/* 588 */         if (this.verbose > 1) Utils.log.fine("Reordering files.");
/* 589 */         boolean bool = true;
/* 590 */         this.pkg.reorderFiles(this.keepClassOrder, bool);
/*     */       }
/*     */       else {
/* 593 */         assert (this.pkg.files.containsAll(this.pkg.getClassStubs()));
/*     */ 
/* 595 */         localObject = this.pkg.files;
/* 596 */         if ((($assertionsDisabled) || ((localObject = new ArrayList(this.pkg.files)).retainAll(this.pkg.getClassStubs()))) || (
/* 598 */           (!$assertionsDisabled) && (!((List)localObject).equals(this.pkg.getClassStubs())))) throw new AssertionError();
/*     */       }
/* 600 */       this.pkg.trimStubs();
/*     */ 
/* 603 */       if (PackerImpl.this.props.getBoolean("com.sun.java.util.jar.pack.strip.debug")) this.pkg.stripAttributeKind("Debug");
/* 604 */       if (PackerImpl.this.props.getBoolean("com.sun.java.util.jar.pack.strip.compile")) this.pkg.stripAttributeKind("Compile");
/* 605 */       if (PackerImpl.this.props.getBoolean("com.sun.java.util.jar.pack.strip.constants")) this.pkg.stripAttributeKind("Constant");
/* 606 */       if (PackerImpl.this.props.getBoolean("com.sun.java.util.jar.pack.strip.exceptions")) this.pkg.stripAttributeKind("Exceptions");
/* 607 */       if (PackerImpl.this.props.getBoolean("com.sun.java.util.jar.pack.strip.innerclasses")) this.pkg.stripAttributeKind("InnerClasses");
/*     */ 
/* 610 */       if (this.pkg.package_majver <= 0) this.pkg.choosePackageVersion();
/*     */ 
/* 612 */       Object localObject = new PackageWriter(this.pkg, paramOutputStream);
/* 613 */       ((PackageWriter)localObject).archiveNextCount = paramInt;
/* 614 */       ((PackageWriter)localObject).write();
/* 615 */       paramOutputStream.flush();
/* 616 */       if (this.verbose > 0) {
/* 617 */         long l1 = ((PackageWriter)localObject).archiveSize0 + ((PackageWriter)localObject).archiveSize1;
/* 618 */         this.totalOutputSize += l1;
/* 619 */         long l2 = this.segmentSize;
/* 620 */         Utils.log.info("Transmitted " + i + " files of " + l2 + " input bytes in a segment of " + l1 + " bytes");
/*     */       }
/*     */     }
/*     */ 
/*     */     List<InFile> scanJar(JarFile paramJarFile)
/*     */       throws IOException
/*     */     {
/* 629 */       ArrayList localArrayList = new ArrayList();
/*     */       try {
/* 631 */         for (JarEntry localJarEntry : Collections.list(paramJarFile.entries())) {
/* 632 */           InFile localInFile = new InFile(paramJarFile, localJarEntry);
/* 633 */           assert (localJarEntry.isDirectory() == localInFile.name.endsWith("/"));
/* 634 */           localArrayList.add(localInFile);
/*     */         }
/*     */       } catch (IllegalStateException localIllegalStateException) {
/* 637 */         throw new IOException(localIllegalStateException.getLocalizedMessage(), localIllegalStateException);
/*     */       }
/* 639 */       return localArrayList;
/*     */     }
/*     */ 
/*     */     final class InFile
/*     */     {
/*     */       final String name;
/*     */       final JarFile jf;
/*     */       final JarEntry je;
/*     */       final File f;
/* 337 */       int modtime = 0;
/*     */       int options;
/*     */ 
/*     */       InFile(String arg2)
/*     */       {
/*     */         String str;
/* 340 */         this.name = Utils.getJarEntryName(str);
/* 341 */         this.f = new File(str);
/* 342 */         this.jf = null;
/* 343 */         this.je = null;
/* 344 */         int i = getModtime(this.f.lastModified());
/* 345 */         if ((PackerImpl.DoPack.this.keepModtime) && (i != 0))
/* 346 */           this.modtime = i;
/* 347 */         else if ((PackerImpl.DoPack.this.latestModtime) && (i > PackerImpl.DoPack.this.pkg.default_modtime))
/* 348 */           PackerImpl.DoPack.this.pkg.default_modtime = i;
/*     */       }
/*     */ 
/*     */       InFile(JarFile paramJarEntry, JarEntry arg3)
/*     */       {
/*     */         Object localObject;
/* 352 */         this.name = Utils.getJarEntryName(localObject.getName());
/* 353 */         this.f = null;
/* 354 */         this.jf = paramJarEntry;
/* 355 */         this.je = localObject;
/* 356 */         int i = getModtime(localObject.getTime());
/* 357 */         if ((PackerImpl.DoPack.this.keepModtime) && (i != 0))
/* 358 */           this.modtime = i;
/* 359 */         else if ((PackerImpl.DoPack.this.latestModtime) && (i > PackerImpl.DoPack.this.pkg.default_modtime)) {
/* 360 */           PackerImpl.DoPack.this.pkg.default_modtime = i;
/*     */         }
/* 362 */         if ((PackerImpl.DoPack.this.keepDeflateHint) && (localObject.getMethod() == 8))
/* 363 */           this.options |= 1;
/*     */       }
/*     */ 
/*     */       InFile(JarEntry arg2) {
/* 367 */         this(null, localJarEntry);
/*     */       }
/*     */       long getInputLength() {
/* 370 */         long l = this.je != null ? this.je.getSize() : this.f.length();
/* 371 */         assert (l >= 0L) : (this + ".len=" + l);
/*     */ 
/* 373 */         return Math.max(0L, l) + this.name.length() + 5L;
/*     */       }
/*     */ 
/*     */       int getModtime(long paramLong) {
/* 377 */         long l = (paramLong + 500L) / 1000L;
/* 378 */         if ((int)l == l) {
/* 379 */           return (int)l;
/*     */         }
/* 381 */         Utils.log.warning("overflow in modtime for " + this.f);
/* 382 */         return 0;
/*     */       }
/*     */ 
/*     */       void copyTo(Package.File paramFile) {
/* 386 */         if (this.modtime != 0)
/* 387 */           paramFile.modtime = this.modtime;
/* 388 */         paramFile.options |= this.options;
/*     */       }
/*     */       InputStream getInputStream() throws IOException {
/* 391 */         if (this.jf != null) {
/* 392 */           return this.jf.getInputStream(this.je);
/*     */         }
/* 394 */         return new FileInputStream(this.f);
/*     */       }
/*     */ 
/*     */       public String toString() {
/* 398 */         return this.name;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.PackerImpl
 * JD-Core Version:    0.6.2
 */