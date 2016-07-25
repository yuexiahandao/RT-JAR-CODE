/*      */ package com.sun.java.util.jar.pack;
/*      */ 
/*      */ import [C;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ 
/*      */ class PackageWriter extends BandStructure
/*      */ {
/*      */   Package pkg;
/*      */   OutputStream finalOut;
/*      */   Set<ConstantPool.Entry> requiredEntries;
/*      */   Map<Attribute.Layout, int[]> backCountTable;
/*      */   int[][] attrCounts;
/*      */   int[] maxFlags;
/*      */   List<Map<Attribute.Layout, int[]>> allLayouts;
/*      */   Attribute.Layout[] attrDefsWritten;
/*      */   private Code curCode;
/*      */   private Package.Class curClass;
/*      */   private ConstantPool.Entry[] curCPMap;
/* 1586 */   int[] codeHist = new int[256];
/* 1587 */   int[] ldcHist = new int[20];
/*      */ 
/*      */   PackageWriter(Package paramPackage, OutputStream paramOutputStream)
/*      */     throws IOException
/*      */   {
/*   62 */     this.pkg = paramPackage;
/*   63 */     this.finalOut = paramOutputStream;
/*      */ 
/*   65 */     initPackageMajver(paramPackage.package_majver);
/*      */   }
/*      */ 
/*      */   void write() throws IOException {
/*   69 */     int i = 0;
/*      */     try {
/*   71 */       if (this.verbose > 0) {
/*   72 */         Utils.log.info("Setting up constant pool...");
/*      */       }
/*   74 */       setup();
/*      */ 
/*   76 */       if (this.verbose > 0) {
/*   77 */         Utils.log.info("Packing...");
/*      */       }
/*      */ 
/*   82 */       writeConstantPool();
/*   83 */       writeFiles();
/*   84 */       writeAttrDefs();
/*   85 */       writeInnerClasses();
/*   86 */       writeClassesAndByteCodes();
/*   87 */       writeAttrCounts();
/*      */ 
/*   89 */       if (this.verbose > 1) printCodeHist();
/*      */ 
/*   92 */       if (this.verbose > 0) {
/*   93 */         Utils.log.info("Coding...");
/*      */       }
/*   95 */       this.all_bands.chooseBandCodings();
/*      */ 
/*   98 */       writeFileHeader();
/*      */ 
/*  100 */       writeAllBandsTo(this.finalOut);
/*      */ 
/*  102 */       i = 1;
/*      */     } catch (Exception localException) {
/*  104 */       Utils.log.warning("Error on output: " + localException, localException);
/*      */ 
/*  107 */       if (this.verbose > 0) this.finalOut.close();
/*  108 */       if ((localException instanceof IOException)) throw ((IOException)localException);
/*  109 */       if ((localException instanceof RuntimeException)) throw ((RuntimeException)localException);
/*  110 */       throw new Error("error packing", localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   void setup()
/*      */   {
/*  119 */     this.requiredEntries = new HashSet();
/*  120 */     setArchiveOptions();
/*  121 */     trimClassAttributes();
/*  122 */     collectAttributeLayouts();
/*  123 */     this.pkg.buildGlobalConstantPool(this.requiredEntries);
/*  124 */     setBandIndexes();
/*  125 */     makeNewAttributeBands();
/*  126 */     collectInnerClasses();
/*      */   }
/*      */ 
/*      */   void setArchiveOptions()
/*      */   {
/*  134 */     int i = this.pkg.default_modtime;
/*  135 */     int j = this.pkg.default_modtime;
/*  136 */     int k = -1;
/*  137 */     int m = 0;
/*      */ 
/*  140 */     this.archiveOptions |= this.pkg.default_options;
/*      */ 
/*  142 */     for (Object localObject1 = this.pkg.files.iterator(); ((Iterator)localObject1).hasNext(); ) { Package.File localFile = (Package.File)((Iterator)localObject1).next();
/*  143 */       i1 = localFile.modtime;
/*  144 */       int i2 = localFile.options;
/*      */ 
/*  146 */       if (i == 0) {
/*  147 */         i = j = i1;
/*      */       } else {
/*  149 */         if (i > i1) i = i1;
/*  150 */         if (j < i1) j = i1;
/*      */       }
/*  152 */       k &= i2;
/*  153 */       m |= i2;
/*      */     }
/*  155 */     if (this.pkg.default_modtime == 0)
/*      */     {
/*  157 */       this.pkg.default_modtime = i;
/*      */     }
/*  159 */     if ((i != 0) && (i != j))
/*      */     {
/*  161 */       this.archiveOptions |= 64;
/*      */     }
/*      */ 
/*  164 */     if ((!testBit(this.archiveOptions, 32)) && (k != -1)) {
/*  165 */       if (testBit(k, 1))
/*      */       {
/*  168 */         this.archiveOptions |= 32;
/*  169 */         k--;
/*  170 */         m--;
/*      */       }
/*  172 */       this.pkg.default_options |= k;
/*  173 */       if ((k != m) || (k != this.pkg.default_options))
/*      */       {
/*  175 */         this.archiveOptions |= 128;
/*      */       }
/*      */     }
/*      */ 
/*  179 */     localObject1 = new HashMap();
/*  180 */     int n = 0;
/*  181 */     int i1 = -1;
/*  182 */     for (Package.Class localClass : this.pkg.classes) {
/*  183 */       int i5 = localClass.getVersion();
/*  184 */       localObject2 = (int[])((Map)localObject1).get(Integer.valueOf(i5));
/*  185 */       if (localObject2 == null) {
/*  186 */         localObject2 = new int[1];
/*  187 */         ((Map)localObject1).put(Integer.valueOf(i5), localObject2);
/*      */       }
/*  189 */       int i7 = localObject2[0] += 1;
/*      */ 
/*  191 */       if (n < i7) {
/*  192 */         n = i7;
/*  193 */         i1 = i5;
/*      */       }
/*      */     }
/*  196 */     ((Map)localObject1).clear();
/*  197 */     if (i1 == -1) i1 = 0;
/*  198 */     int i3 = (char)(i1 >>> 16);
/*  199 */     int i4 = (char)i1;
/*  200 */     this.pkg.default_class_majver = ((short)i3);
/*  201 */     this.pkg.default_class_minver = ((short)i4);
/*  202 */     String str = Package.versionStringOf(i3, i4);
/*  203 */     if (this.verbose > 0)
/*  204 */       Utils.log.info("Consensus version number in segment is " + str);
/*  205 */     if (this.verbose > 0) {
/*  206 */       Utils.log.info("Highest version number in segment is " + Package.versionStringOf(this.pkg.getHighestClassVersion()));
/*      */     }
/*      */ 
/*  210 */     for (Object localObject2 = this.pkg.classes.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (Package.Class)((Iterator)localObject2).next();
/*  211 */       if (((Package.Class)localObject3).getVersion() != i1) {
/*  212 */         Attribute localAttribute = makeClassFileVersionAttr(((Package.Class)localObject3).minver, ((Package.Class)localObject3).majver);
/*  213 */         if (this.verbose > 1) {
/*  214 */           localObject4 = ((Package.Class)localObject3).getVersionString();
/*  215 */           localObject5 = str;
/*  216 */           Utils.log.fine("Version " + (String)localObject4 + " of " + localObject3 + " doesn't match package version " + (String)localObject5);
/*      */         }
/*      */ 
/*  221 */         ((Package.Class)localObject3).addAttribute(localAttribute);
/*      */       }
/*      */     }
/*  226 */     Object localObject3;
/*      */     Object localObject4;
/*      */     Object localObject5;
/*  226 */     for (localObject2 = this.pkg.files.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (Package.File)((Iterator)localObject2).next();
/*  227 */       long l = ((Package.File)localObject3).getFileLength();
/*  228 */       if (l != (int)l) {
/*  229 */         this.archiveOptions |= 256;
/*  230 */         if (this.verbose <= 0) break;
/*  231 */         Utils.log.info("Note: Huge resource file " + ((Package.File)localObject3).getFileName() + " forces 64-bit sizing"); break;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  239 */     int i6 = 0;
/*  240 */     int i8 = 0;
/*  241 */     for (Iterator localIterator2 = this.pkg.classes.iterator(); localIterator2.hasNext(); ) { localObject4 = (Package.Class)localIterator2.next();
/*  242 */       for (localObject5 = ((Package.Class)localObject4).getMethods().iterator(); ((Iterator)localObject5).hasNext(); ) { Package.Class.Method localMethod = (Package.Class.Method)((Iterator)localObject5).next();
/*  243 */         if (localMethod.code != null) {
/*  244 */           if (localMethod.code.attributeSize() == 0)
/*      */           {
/*  246 */             i8++;
/*  247 */           } else if (shortCodeHeader(localMethod.code) != 0)
/*      */           {
/*  249 */             i6 += 3;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  254 */     if (i6 > i8) {
/*  255 */       this.archiveOptions |= 4;
/*      */     }
/*  257 */     if (this.verbose > 0)
/*  258 */       Utils.log.info("archiveOptions = 0b" + Integer.toBinaryString(this.archiveOptions));
/*      */   }
/*      */ 
/*      */   void writeFileHeader() throws IOException
/*      */   {
/*  263 */     this.pkg.checkVersion();
/*  264 */     writeArchiveMagic();
/*  265 */     writeArchiveHeader();
/*      */   }
/*      */ 
/*      */   private void putMagicInt32(int paramInt)
/*      */     throws IOException
/*      */   {
/*  271 */     int i = paramInt;
/*  272 */     for (int j = 0; j < 4; j++) {
/*  273 */       this.archive_magic.putByte(0xFF & i >>> 24);
/*  274 */       i <<= 8;
/*      */     }
/*      */   }
/*      */ 
/*      */   void writeArchiveMagic() throws IOException {
/*  279 */     putMagicInt32(this.pkg.magic);
/*      */   }
/*      */ 
/*      */   void writeArchiveHeader() throws IOException
/*      */   {
/*  284 */     int i = 0;
/*      */ 
/*  289 */     boolean bool1 = testBit(this.archiveOptions, 1);
/*  290 */     if (!bool1) {
/*  291 */       bool1 |= this.band_headers.length() != 0;
/*  292 */       bool1 |= this.attrDefsWritten.length != 0;
/*  293 */       if (bool1)
/*  294 */         this.archiveOptions |= 1;
/*      */     }
/*  296 */     if (!bool1) {
/*  297 */       i += 2;
/*      */     }
/*      */ 
/*  301 */     boolean bool2 = testBit(this.archiveOptions, 16);
/*  302 */     if (!bool2) {
/*  303 */       bool2 |= this.archiveNextCount > 0;
/*  304 */       bool2 |= this.pkg.default_modtime != 0;
/*  305 */       if (bool2)
/*  306 */         this.archiveOptions |= 16;
/*      */     }
/*  308 */     if (!bool2) {
/*  309 */       i += 5;
/*      */     }
/*      */ 
/*  313 */     boolean bool3 = testBit(this.archiveOptions, 2);
/*  314 */     if (!bool3) {
/*  315 */       bool3 |= this.pkg.cp.haveNumbers();
/*  316 */       if (bool3)
/*  317 */         this.archiveOptions |= 2;
/*      */     }
/*  319 */     if (!bool3) {
/*  320 */       i += 4;
/*      */     }
/*  322 */     assert (this.pkg.package_majver > 0);
/*  323 */     this.archive_header_0.putInt(this.pkg.package_minver);
/*  324 */     this.archive_header_0.putInt(this.pkg.package_majver);
/*  325 */     if (this.verbose > 0) {
/*  326 */       Utils.log.info("Package Version for this segment:" + Package.versionStringOf(this.pkg.getPackageVersion()));
/*      */     }
/*  328 */     this.archive_header_0.putInt(this.archiveOptions);
/*  329 */     assert (this.archive_header_0.length() == 3);
/*      */ 
/*  332 */     if (bool2) {
/*  333 */       assert (this.archive_header_S.length() == 0);
/*  334 */       this.archive_header_S.putInt(0);
/*  335 */       assert (this.archive_header_S.length() == 1);
/*  336 */       this.archive_header_S.putInt(0);
/*  337 */       assert (this.archive_header_S.length() == 2);
/*      */     }
/*      */ 
/*  342 */     if (bool2) {
/*  343 */       this.archive_header_1.putInt(this.archiveNextCount);
/*  344 */       this.archive_header_1.putInt(this.pkg.default_modtime);
/*  345 */       this.archive_header_1.putInt(this.pkg.files.size());
/*      */     } else {
/*  347 */       assert (this.pkg.files.isEmpty());
/*      */     }
/*      */ 
/*  350 */     if (bool1) {
/*  351 */       this.archive_header_1.putInt(this.band_headers.length());
/*  352 */       this.archive_header_1.putInt(this.attrDefsWritten.length);
/*      */     } else {
/*  354 */       assert (this.band_headers.length() == 0);
/*  355 */       assert (this.attrDefsWritten.length == 0);
/*      */     }
/*      */ 
/*  358 */     writeConstantPoolCounts(bool3);
/*      */ 
/*  360 */     this.archive_header_1.putInt(this.pkg.getAllInnerClasses().size());
/*  361 */     this.archive_header_1.putInt(this.pkg.default_class_minver);
/*  362 */     this.archive_header_1.putInt(this.pkg.default_class_majver);
/*  363 */     this.archive_header_1.putInt(this.pkg.classes.size());
/*      */ 
/*  366 */     assert (this.archive_header_0.length() + this.archive_header_S.length() + this.archive_header_1.length() == 26 - i);
/*      */ 
/*  372 */     this.archiveSize0 = 0L;
/*  373 */     this.archiveSize1 = this.all_bands.outputSize();
/*      */ 
/*  375 */     this.archiveSize0 += this.archive_magic.outputSize();
/*  376 */     this.archiveSize0 += this.archive_header_0.outputSize();
/*  377 */     this.archiveSize0 += this.archive_header_S.outputSize();
/*      */ 
/*  379 */     this.archiveSize1 -= this.archiveSize0;
/*      */ 
/*  382 */     if (bool2) {
/*  383 */       int j = (int)(this.archiveSize1 >>> 32);
/*  384 */       int k = (int)(this.archiveSize1 >>> 0);
/*  385 */       this.archive_header_S.patchValue(0, j);
/*  386 */       this.archive_header_S.patchValue(1, k);
/*  387 */       int m = UNSIGNED5.getLength(0);
/*  388 */       this.archiveSize0 += UNSIGNED5.getLength(j) - m;
/*  389 */       this.archiveSize0 += UNSIGNED5.getLength(k) - m;
/*      */     }
/*  391 */     if (this.verbose > 1) {
/*  392 */       Utils.log.fine("archive sizes: " + this.archiveSize0 + "+" + this.archiveSize1);
/*      */     }
/*  394 */     assert (this.all_bands.outputSize() == this.archiveSize0 + this.archiveSize1);
/*      */   }
/*      */ 
/*      */   void writeConstantPoolCounts(boolean paramBoolean) throws IOException {
/*  398 */     for (int i = 0; i < ConstantPool.TAGS_IN_ORDER.length; i++) {
/*  399 */       byte b = ConstantPool.TAGS_IN_ORDER[i];
/*  400 */       int j = this.pkg.cp.getIndexByTag(b).size();
/*  401 */       switch (b)
/*      */       {
/*      */       case 1:
/*  404 */         if ((j > 0) && 
/*  405 */           (!$assertionsDisabled) && (this.pkg.cp.getIndexByTag(b).get(0) != ConstantPool.getUtf8Entry(""))) throw new AssertionError();
/*      */ 
/*      */         break;
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*  414 */         if (!paramBoolean) {
/*  415 */           if (($assertionsDisabled) || (j == 0)) continue; throw new AssertionError();
/*      */         }
/*      */         break;
/*      */       case 2:
/*      */       }
/*  420 */       this.archive_header_1.putInt(j);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected ConstantPool.Index getCPIndex(byte paramByte) {
/*  425 */     return this.pkg.cp.getIndexByTag(paramByte);
/*      */   }
/*      */ 
/*      */   void writeConstantPool()
/*      */     throws IOException
/*      */   {
/*  448 */     ConstantPool.IndexGroup localIndexGroup = this.pkg.cp;
/*      */ 
/*  450 */     if (this.verbose > 0) Utils.log.info("Writing CP");
/*      */ 
/*  452 */     for (int i = 0; i < ConstantPool.TAGS_IN_ORDER.length; i++) {
/*  453 */       byte b = ConstantPool.TAGS_IN_ORDER[i];
/*  454 */       ConstantPool.Index localIndex = localIndexGroup.getIndexByTag(b);
/*      */ 
/*  456 */       ConstantPool.Entry[] arrayOfEntry = localIndex.cpMap;
/*  457 */       if (this.verbose > 0)
/*  458 */         Utils.log.info("Writing " + arrayOfEntry.length + " " + ConstantPool.tagName(b) + " entries...");
/*      */       Object localObject1;
/*  460 */       if (this.optDumpBands) {
/*  461 */         PrintStream localPrintStream = new PrintStream(getDumpStream(localIndex, ".idx")); localObject1 = null;
/*      */         try { printArrayTo(localPrintStream, arrayOfEntry, 0, arrayOfEntry.length); }
/*      */         catch (Throwable localThrowable2)
/*      */         {
/*  461 */           localObject1 = localThrowable2; throw localThrowable2;
/*      */         } finally {
/*  463 */           if (localPrintStream != null) if (localObject1 != null) try { localPrintStream.close(); } catch (Throwable localThrowable3) { ((Throwable)localObject1).addSuppressed(localThrowable3); } else localPrintStream.close();
/*      */         }
/*      */       }
/*      */       int j;
/*  466 */       switch (b) {
/*      */       case 1:
/*  468 */         writeUtf8Bands(arrayOfEntry);
/*  469 */         break;
/*      */       case 3:
/*  471 */         for (j = 0; j < arrayOfEntry.length; j++) {
/*  472 */           localObject1 = (ConstantPool.NumberEntry)arrayOfEntry[j];
/*  473 */           int k = ((Integer)((ConstantPool.NumberEntry)localObject1).numberValue()).intValue();
/*  474 */           this.cp_Int.putInt(k);
/*      */         }
/*  476 */         break;
/*      */       case 4:
/*  478 */         for (j = 0; j < arrayOfEntry.length; j++) {
/*  479 */           localObject1 = (ConstantPool.NumberEntry)arrayOfEntry[j];
/*  480 */           float f = ((Float)((ConstantPool.NumberEntry)localObject1).numberValue()).floatValue();
/*  481 */           int m = Float.floatToIntBits(f);
/*  482 */           this.cp_Float.putInt(m);
/*      */         }
/*  484 */         break;
/*      */       case 5:
/*  486 */         for (j = 0; j < arrayOfEntry.length; j++) {
/*  487 */           localObject1 = (ConstantPool.NumberEntry)arrayOfEntry[j];
/*  488 */           long l1 = ((Long)((ConstantPool.NumberEntry)localObject1).numberValue()).longValue();
/*  489 */           this.cp_Long_hi.putInt((int)(l1 >>> 32));
/*  490 */           this.cp_Long_lo.putInt((int)(l1 >>> 0));
/*      */         }
/*  492 */         break;
/*      */       case 6:
/*  494 */         for (j = 0; j < arrayOfEntry.length; j++) {
/*  495 */           localObject1 = (ConstantPool.NumberEntry)arrayOfEntry[j];
/*  496 */           double d = ((Double)((ConstantPool.NumberEntry)localObject1).numberValue()).doubleValue();
/*  497 */           long l2 = Double.doubleToLongBits(d);
/*  498 */           this.cp_Double_hi.putInt((int)(l2 >>> 32));
/*  499 */           this.cp_Double_lo.putInt((int)(l2 >>> 0));
/*      */         }
/*  501 */         break;
/*      */       case 8:
/*  503 */         for (j = 0; j < arrayOfEntry.length; j++) {
/*  504 */           localObject1 = (ConstantPool.StringEntry)arrayOfEntry[j];
/*  505 */           this.cp_String.putRef(((ConstantPool.StringEntry)localObject1).ref);
/*      */         }
/*  507 */         break;
/*      */       case 7:
/*  509 */         for (j = 0; j < arrayOfEntry.length; j++) {
/*  510 */           localObject1 = (ConstantPool.ClassEntry)arrayOfEntry[j];
/*  511 */           this.cp_Class.putRef(((ConstantPool.ClassEntry)localObject1).ref);
/*      */         }
/*  513 */         break;
/*      */       case 13:
/*  515 */         writeSignatureBands(arrayOfEntry);
/*  516 */         break;
/*      */       case 12:
/*  518 */         for (j = 0; j < arrayOfEntry.length; j++) {
/*  519 */           localObject1 = (ConstantPool.DescriptorEntry)arrayOfEntry[j];
/*  520 */           this.cp_Descr_name.putRef(((ConstantPool.DescriptorEntry)localObject1).nameRef);
/*  521 */           this.cp_Descr_type.putRef(((ConstantPool.DescriptorEntry)localObject1).typeRef);
/*      */         }
/*  523 */         break;
/*      */       case 9:
/*  525 */         writeMemberRefs(b, arrayOfEntry, this.cp_Field_class, this.cp_Field_desc);
/*  526 */         break;
/*      */       case 10:
/*  528 */         writeMemberRefs(b, arrayOfEntry, this.cp_Method_class, this.cp_Method_desc);
/*  529 */         break;
/*      */       case 11:
/*  531 */         writeMemberRefs(b, arrayOfEntry, this.cp_Imethod_class, this.cp_Imethod_desc);
/*  532 */         break;
/*      */       case 2:
/*      */       default:
/*  534 */         if (!$assertionsDisabled) throw new AssertionError(); break;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void writeUtf8Bands(ConstantPool.Entry[] paramArrayOfEntry) throws IOException {
/*  540 */     if (paramArrayOfEntry.length == 0) {
/*  541 */       return;
/*      */     }
/*      */ 
/*  544 */     assert (paramArrayOfEntry[0].stringValue().equals(""));
/*      */ 
/*  549 */     char[][] arrayOfChar = new char[paramArrayOfEntry.length][];
/*  550 */     for (int i = 0; i < arrayOfChar.length; i++) {
/*  551 */       arrayOfChar[i] = paramArrayOfEntry[i].stringValue().toCharArray();
/*      */     }
/*      */ 
/*  555 */     int[] arrayOfInt = new int[paramArrayOfEntry.length];
/*  556 */     Object localObject = new char[0];
/*      */     int i1;
/*  557 */     for (int j = 0; j < arrayOfChar.length; j++) {
/*  558 */       int k = 0;
/*  559 */       [C local[C2 = arrayOfChar[j];
/*  560 */       i1 = Math.min(local[C2.length, localObject.length);
/*  561 */       while ((k < i1) && (local[C2[k] == localObject[k]))
/*  562 */         k++;
/*  563 */       arrayOfInt[j] = k;
/*  564 */       if (j >= 2)
/*  565 */         this.cp_Utf8_prefix.putInt(k);
/*      */       else
/*  567 */         assert (k == 0);
/*  568 */       localObject = local[C2;
/*      */     }
/*      */     int n;
/*  573 */     for (j = 0; j < arrayOfChar.length; j++) {
/*  574 */       [C local[C1 = arrayOfChar[j];
/*  575 */       n = arrayOfInt[j];
/*  576 */       i1 = local[C1.length - arrayOfInt[j];
/*  577 */       boolean bool = false;
/*      */       int i2;
/*      */       int i3;
/*  578 */       if (i1 == 0)
/*      */       {
/*  587 */         bool = j >= 1;
/*      */       }
/*  590 */       else if ((this.optBigStrings) && (this.effort > 1) && (i1 > 100)) {
/*  591 */         i2 = 0;
/*  592 */         for (i3 = 0; i3 < i1; i3++) {
/*  593 */           if (local[C1[(n + i3)] > '') {
/*  594 */             i2++;
/*      */           }
/*      */         }
/*  597 */         if (i2 > 100)
/*      */         {
/*  599 */           bool = tryAlternateEncoding(j, i2, local[C1, n);
/*      */         }
/*      */       }
/*  602 */       if (j < 1)
/*      */       {
/*  604 */         assert (!bool);
/*  605 */         if ((!$assertionsDisabled) && (i1 != 0)) throw new AssertionError(); 
/*      */       }
/*  606 */       else if (bool)
/*      */       {
/*  610 */         this.cp_Utf8_suffix.putInt(0);
/*  611 */         this.cp_Utf8_big_suffix.putInt(i1);
/*      */       } else {
/*  613 */         assert (i1 != 0);
/*      */ 
/*  615 */         this.cp_Utf8_suffix.putInt(i1);
/*  616 */         for (i2 = 0; i2 < i1; i2++) {
/*  617 */           i3 = local[C1[(n + i2)];
/*  618 */           this.cp_Utf8_chars.putInt(i3);
/*      */         }
/*      */       }
/*      */     }
/*  622 */     if (this.verbose > 0) {
/*  623 */       j = this.cp_Utf8_chars.length();
/*  624 */       int m = this.cp_Utf8_big_chars.length();
/*  625 */       n = j + m;
/*  626 */       Utils.log.info("Utf8string #CHARS=" + n + " #PACKEDCHARS=" + m);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean tryAlternateEncoding(int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3)
/*      */   {
/*  632 */     int i = paramArrayOfChar.length - paramInt3;
/*  633 */     int[] arrayOfInt1 = new int[i];
/*  634 */     for (int j = 0; j < i; j++) {
/*  635 */       arrayOfInt1[j] = paramArrayOfChar[(paramInt3 + j)];
/*      */     }
/*  637 */     CodingChooser localCodingChooser = getCodingChooser();
/*  638 */     Coding localCoding1 = this.cp_Utf8_big_chars.regularCoding;
/*  639 */     String str = "(Utf8_big_" + paramInt1 + ")";
/*  640 */     int[] arrayOfInt2 = { 0, 0 };
/*      */ 
/*  643 */     if ((this.verbose > 1) || (localCodingChooser.verbose > 1)) {
/*  644 */       Utils.log.fine("--- chooseCoding " + str);
/*      */     }
/*  646 */     CodingMethod localCodingMethod = localCodingChooser.choose(arrayOfInt1, localCoding1, arrayOfInt2);
/*  647 */     Coding localCoding2 = this.cp_Utf8_chars.regularCoding;
/*  648 */     if (this.verbose > 1)
/*  649 */       Utils.log.fine("big string[" + paramInt1 + "] len=" + i + " #wide=" + paramInt2 + " size=" + arrayOfInt2[0] + "/z=" + arrayOfInt2[1] + " coding " + localCodingMethod);
/*  650 */     if (localCodingMethod != localCoding2) {
/*  651 */       int k = arrayOfInt2[1];
/*  652 */       int[] arrayOfInt3 = localCodingChooser.computeSize(localCoding2, arrayOfInt1);
/*  653 */       int m = arrayOfInt3[1];
/*  654 */       int n = Math.max(5, m / 1000);
/*  655 */       if (this.verbose > 1)
/*  656 */         Utils.log.fine("big string[" + paramInt1 + "] normalSize=" + arrayOfInt3[0] + "/z=" + arrayOfInt3[1] + " win=" + (k < m - n));
/*  657 */       if (k < m - n) {
/*  658 */         BandStructure.IntBand localIntBand = this.cp_Utf8_big_chars.newIntBand(str);
/*  659 */         localIntBand.initializeValues(arrayOfInt1);
/*  660 */         return true;
/*      */       }
/*      */     }
/*  663 */     return false;
/*      */   }
/*      */ 
/*      */   void writeSignatureBands(ConstantPool.Entry[] paramArrayOfEntry) throws IOException {
/*  667 */     for (int i = 0; i < paramArrayOfEntry.length; i++) {
/*  668 */       ConstantPool.SignatureEntry localSignatureEntry = (ConstantPool.SignatureEntry)paramArrayOfEntry[i];
/*  669 */       this.cp_Signature_form.putRef(localSignatureEntry.formRef);
/*  670 */       for (int j = 0; j < localSignatureEntry.classRefs.length; j++)
/*  671 */         this.cp_Signature_classes.putRef(localSignatureEntry.classRefs[j]);
/*      */     }
/*      */   }
/*      */ 
/*      */   void writeMemberRefs(byte paramByte, ConstantPool.Entry[] paramArrayOfEntry, BandStructure.CPRefBand paramCPRefBand1, BandStructure.CPRefBand paramCPRefBand2) throws IOException
/*      */   {
/*  677 */     for (int i = 0; i < paramArrayOfEntry.length; i++) {
/*  678 */       ConstantPool.MemberEntry localMemberEntry = (ConstantPool.MemberEntry)paramArrayOfEntry[i];
/*  679 */       paramCPRefBand1.putRef(localMemberEntry.classRef);
/*  680 */       paramCPRefBand2.putRef(localMemberEntry.descRef);
/*      */     }
/*      */   }
/*      */ 
/*      */   void writeFiles() throws IOException {
/*  685 */     int i = this.pkg.files.size();
/*  686 */     if (i == 0) return;
/*  687 */     int j = this.archiveOptions;
/*  688 */     boolean bool1 = testBit(j, 256);
/*  689 */     boolean bool2 = testBit(j, 64);
/*  690 */     boolean bool3 = testBit(j, 128);
/*  691 */     if (!bool3)
/*  692 */       for (localIterator = this.pkg.files.iterator(); localIterator.hasNext(); ) { localFile = (Package.File)localIterator.next();
/*  693 */         if (localFile.isClassStub()) {
/*  694 */           bool3 = true;
/*  695 */           j |= 128;
/*  696 */           this.archiveOptions = j;
/*  697 */           break;
/*      */         }
/*      */       }
/*      */     Package.File localFile;
/*  701 */     if ((bool1) || (bool2) || (bool3) || (!this.pkg.files.isEmpty())) {
/*  702 */       j |= 16;
/*  703 */       this.archiveOptions = j;
/*      */     }
/*  705 */     for (Iterator localIterator = this.pkg.files.iterator(); localIterator.hasNext(); ) { localFile = (Package.File)localIterator.next();
/*  706 */       this.file_name.putRef(localFile.name);
/*  707 */       long l = localFile.getFileLength();
/*  708 */       this.file_size_lo.putInt((int)l);
/*  709 */       if (bool1)
/*  710 */         this.file_size_hi.putInt((int)(l >>> 32));
/*  711 */       if (bool2)
/*  712 */         this.file_modtime.putInt(localFile.modtime - this.pkg.default_modtime);
/*  713 */       if (bool3)
/*  714 */         this.file_options.putInt(localFile.options);
/*  715 */       localFile.writeTo(this.file_bits.collectorStream());
/*  716 */       if (this.verbose > 1)
/*  717 */         Utils.log.fine("Wrote " + l + " bytes of " + localFile.name.stringValue());
/*      */     }
/*  719 */     if (this.verbose > 0)
/*  720 */       Utils.log.info("Wrote " + i + " resource files");
/*      */   }
/*      */ 
/*      */   void collectAttributeLayouts()
/*      */   {
/*  725 */     this.maxFlags = new int[4];
/*  726 */     this.allLayouts = new FixedList(4);
/*  727 */     for (int i = 0; i < 4; i++) {
/*  728 */       this.allLayouts.set(i, new HashMap());
/*      */     }
/*      */ 
/*  731 */     for (Package.Class localClass : this.pkg.classes) {
/*  732 */       visitAttributeLayoutsIn(0, localClass);
/*  733 */       for (localIterator2 = localClass.getFields().iterator(); localIterator2.hasNext(); ) { localObject = (Package.Class.Field)localIterator2.next();
/*  734 */         visitAttributeLayoutsIn(1, (Attribute.Holder)localObject);
/*      */       }
/*  736 */       for (localIterator2 = localClass.getMethods().iterator(); localIterator2.hasNext(); ) { localObject = (Package.Class.Method)localIterator2.next();
/*  737 */         visitAttributeLayoutsIn(2, (Attribute.Holder)localObject);
/*  738 */         if (((Package.Class.Method)localObject).code != null)
/*  739 */           visitAttributeLayoutsIn(3, ((Package.Class.Method)localObject).code);
/*      */       }
/*      */     }
/*      */     Iterator localIterator2;
/*      */     Object localObject;
/*  744 */     for (int j = 0; j < 4; j++) {
/*  745 */       int k = ((Map)this.allLayouts.get(j)).size();
/*  746 */       boolean bool = haveFlagsHi(j);
/*      */ 
/*  750 */       if (k >= 24) {
/*  751 */         int n = 1 << 9 + j;
/*  752 */         this.archiveOptions |= n;
/*  753 */         bool = true;
/*  754 */         if (this.verbose > 0)
/*  755 */           Utils.log.info("Note: Many " + Attribute.contextName(j) + " attributes forces 63-bit flags");
/*      */       }
/*  757 */       if (this.verbose > 1) {
/*  758 */         Utils.log.fine(Attribute.contextName(j) + ".maxFlags = 0x" + Integer.toHexString(this.maxFlags[j]));
/*  759 */         Utils.log.fine(Attribute.contextName(j) + ".#layouts = " + k);
/*      */       }
/*  761 */       assert (haveFlagsHi(j) == bool);
/*      */     }
/*  763 */     initAttrIndexLimit();
/*      */ 
/*  766 */     for (j = 0; j < 4; j++) {
/*  767 */       assert ((this.attrFlagMask[j] & this.maxFlags[j]) == 0L);
/*      */     }
/*      */ 
/*  771 */     this.backCountTable = new HashMap();
/*  772 */     this.attrCounts = new int[4][];
/*  773 */     for (j = 0; j < 4; j++)
/*      */     {
/*  778 */       long l = (this.maxFlags[j] | this.attrFlagMask[j]) ^ 0xFFFFFFFF;
/*  779 */       assert (this.attrIndexLimit[j] > 0);
/*  780 */       assert (this.attrIndexLimit[j] < 64);
/*  781 */       l &= (1L << this.attrIndexLimit[j]) - 1L;
/*  782 */       int m = 0;
/*  783 */       Map localMap = (Map)this.allLayouts.get(j);
/*  784 */       Map.Entry[] arrayOfEntry = new Map.Entry[localMap.size()];
/*  785 */       localMap.entrySet().toArray(arrayOfEntry);
/*      */ 
/*  788 */       Arrays.sort(arrayOfEntry, new Comparator() {
/*      */         public int compare(Object paramAnonymousObject1, Object paramAnonymousObject2) {
/*  790 */           Map.Entry localEntry1 = (Map.Entry)paramAnonymousObject1;
/*  791 */           Map.Entry localEntry2 = (Map.Entry)paramAnonymousObject2;
/*      */ 
/*  793 */           int i = -(((int[])(int[])localEntry1.getValue())[0] - ((int[])(int[])localEntry2.getValue())[0]);
/*      */ 
/*  795 */           if (i != 0) return i;
/*  796 */           return ((Comparable)localEntry1.getKey()).compareTo(localEntry2.getKey());
/*      */         }
/*      */       });
/*  799 */       this.attrCounts[j] = new int[this.attrIndexLimit[j] + arrayOfEntry.length];
/*  800 */       for (int i1 = 0; i1 < arrayOfEntry.length; i1++) {
/*  801 */         Map.Entry localEntry = arrayOfEntry[i1];
/*  802 */         Attribute.Layout localLayout = (Attribute.Layout)localEntry.getKey();
/*  803 */         int i2 = ((int[])(int[])localEntry.getValue())[0];
/*      */ 
/*  805 */         Integer localInteger = (Integer)this.attrIndexTable.get(localLayout);
/*      */         int i3;
/*  806 */         if (localInteger != null)
/*      */         {
/*  808 */           i3 = localInteger.intValue();
/*  809 */         } else if (l != 0L) {
/*  810 */           while ((l & 1L) == 0L) {
/*  811 */             l >>>= 1;
/*  812 */             m++;
/*      */           }
/*  814 */           l -= 1L;
/*      */ 
/*  816 */           i3 = setAttributeLayoutIndex(localLayout, m);
/*      */         }
/*      */         else {
/*  819 */           i3 = setAttributeLayoutIndex(localLayout, -1);
/*      */         }
/*      */ 
/*  823 */         this.attrCounts[j][i3] = i2;
/*      */ 
/*  826 */         Attribute.Layout.Element[] arrayOfElement = localLayout.getCallables();
/*  827 */         int[] arrayOfInt = new int[arrayOfElement.length];
/*  828 */         for (int i4 = 0; i4 < arrayOfElement.length; i4++) {
/*  829 */           assert (arrayOfElement[i4].kind == 10);
/*  830 */           if (!arrayOfElement[i4].flagTest((byte)8)) {
/*  831 */             arrayOfInt[i4] = -1;
/*      */           }
/*      */         }
/*  834 */         this.backCountTable.put(localLayout, arrayOfInt);
/*      */ 
/*  836 */         if (localInteger == null)
/*      */         {
/*  838 */           ConstantPool.Utf8Entry localUtf8Entry1 = ConstantPool.getUtf8Entry(localLayout.name());
/*  839 */           String str = localLayout.layoutForPackageMajver(getPackageMajver());
/*  840 */           ConstantPool.Utf8Entry localUtf8Entry2 = ConstantPool.getUtf8Entry(str);
/*  841 */           this.requiredEntries.add(localUtf8Entry1);
/*  842 */           this.requiredEntries.add(localUtf8Entry2);
/*  843 */           if (this.verbose > 0) {
/*  844 */             if (i3 < this.attrIndexLimit[j])
/*  845 */               Utils.log.info("Using free flag bit 1<<" + i3 + " for " + i2 + " occurrences of " + localLayout);
/*      */             else {
/*  847 */               Utils.log.info("Using overflow index " + i3 + " for " + i2 + " occurrences of " + localLayout);
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  857 */     this.maxFlags = null;
/*  858 */     this.allLayouts = null;
/*      */   }
/*      */ 
/*      */   void visitAttributeLayoutsIn(int paramInt, Attribute.Holder paramHolder)
/*      */   {
/*  868 */     this.maxFlags[paramInt] |= paramHolder.flags;
/*  869 */     for (Attribute localAttribute : paramHolder.getAttributes()) {
/*  870 */       Attribute.Layout localLayout = localAttribute.layout();
/*  871 */       Map localMap = (Map)this.allLayouts.get(paramInt);
/*  872 */       int[] arrayOfInt = (int[])localMap.get(localLayout);
/*  873 */       if (arrayOfInt == null) {
/*  874 */         localMap.put(localLayout, arrayOfInt = new int[1]);
/*      */       }
/*  876 */       if (arrayOfInt[0] < 2147483647)
/*  877 */         arrayOfInt[0] += 1;
/*      */     }
/*      */   }
/*      */ 
/*      */   void writeAttrDefs()
/*      */     throws IOException
/*      */   {
/*  886 */     ArrayList localArrayList = new ArrayList();
/*      */     Object localObject2;
/*  887 */     for (int i = 0; i < 4; i++) {
/*  888 */       int j = ((List)this.attrDefs.get(i)).size();
/*  889 */       for (int k = 0; k < j; k++) {
/*  890 */         int m = i;
/*  891 */         if (k < this.attrIndexLimit[i]) {
/*  892 */           m |= k + 1 << 2;
/*  893 */           assert (m < 256);
/*      */ 
/*  895 */           if (!testBit(this.attrDefSeen[i], 1L << k));
/*      */         }
/*      */         else {
/*  900 */           localObject2 = (Attribute.Layout)((List)this.attrDefs.get(i)).get(k);
/*  901 */           localArrayList.add(new Object[] { Integer.valueOf(m), localObject2 });
/*  902 */           assert (Integer.valueOf(k).equals(this.attrIndexTable.get(localObject2)));
/*      */         }
/*      */       }
/*      */     }
/*  906 */     i = localArrayList.size();
/*  907 */     Object[][] arrayOfObject; = new Object[i][];
/*  908 */     localArrayList.toArray(arrayOfObject;);
/*  909 */     Arrays.sort(arrayOfObject;, new Comparator() {
/*      */       public int compare(Object paramAnonymousObject1, Object paramAnonymousObject2) {
/*  911 */         Object[] arrayOfObject1 = (Object[])paramAnonymousObject1;
/*  912 */         Object[] arrayOfObject2 = (Object[])paramAnonymousObject2;
/*      */ 
/*  914 */         int i = ((Comparable)arrayOfObject1[0]).compareTo(arrayOfObject2[0]);
/*  915 */         if (i != 0) return i;
/*  916 */         Object localObject1 = PackageWriter.this.attrIndexTable.get(arrayOfObject1[1]);
/*  917 */         Object localObject2 = PackageWriter.this.attrIndexTable.get(arrayOfObject2[1]);
/*      */ 
/*  920 */         assert (localObject1 != null);
/*  921 */         assert (localObject2 != null);
/*  922 */         return ((Comparable)localObject1).compareTo(localObject2);
/*      */       }
/*      */     });
/*  925 */     this.attrDefsWritten = new Attribute.Layout[i];
/*  926 */     PrintStream localPrintStream = !this.optDumpBands ? null : new PrintStream(getDumpStream(this.attr_definition_headers, ".def")); Object localObject1 = null;
/*      */     try
/*      */     {
/*  929 */       localObject2 = Arrays.copyOf(this.attrIndexLimit, 4);
/*  930 */       for (int n = 0; n < arrayOfObject;.length; n++) {
/*  931 */         int i1 = ((Integer)arrayOfObject;[n][0]).intValue();
/*  932 */         Attribute.Layout localLayout = (Attribute.Layout)arrayOfObject;[n][1];
/*  933 */         this.attrDefsWritten[n] = localLayout;
/*  934 */         assert ((i1 & 0x3) == localLayout.ctype());
/*  935 */         this.attr_definition_headers.putByte(i1);
/*  936 */         this.attr_definition_name.putRef(ConstantPool.getUtf8Entry(localLayout.name()));
/*  937 */         String str = localLayout.layoutForPackageMajver(getPackageMajver());
/*  938 */         this.attr_definition_layout.putRef(ConstantPool.getUtf8Entry(str));
/*      */ 
/*  940 */         int i2 = 0;
/*  941 */         assert ((i2 = 1) != 0);
/*      */         int i3;
/*  942 */         if (i2 != 0) {
/*  943 */           i3 = (i1 >> 2) - 1;
/*  944 */           if (i3 < 0)
/*      */           {
/*      */             int tmp455_452 = localLayout.ctype();
/*      */             Object tmp455_448 = localObject2;
/*      */             int tmp457_456 = tmp455_448[tmp455_452]; tmp455_448[tmp455_452] = (tmp457_456 + 1); i3 = tmp457_456;
/*  945 */           }int i4 = ((Integer)this.attrIndexTable.get(localLayout)).intValue();
/*  946 */           assert (i3 == i4);
/*      */         }
/*  948 */         if (localPrintStream != null) {
/*  949 */           i3 = (i1 >> 2) - 1;
/*  950 */           localPrintStream.println(i3 + " " + localLayout);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Throwable localThrowable2)
/*      */     {
/*  926 */       localObject1 = localThrowable2; throw localThrowable2;
/*      */     }
/*      */     finally
/*      */     {
/*  953 */       if (localPrintStream != null) if (localObject1 != null) try { localPrintStream.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localPrintStream.close();  
/*      */     }
/*      */   }
/*      */ 
/*      */   void writeAttrCounts()
/*      */     throws IOException
/*      */   {
/*  958 */     for (int i = 0; i < 4; i++) {
/*  959 */       BandStructure.MultiBand localMultiBand = this.attrBands[i];
/*  960 */       BandStructure.IntBand localIntBand = getAttrBand(localMultiBand, 4);
/*  961 */       Attribute.Layout[] arrayOfLayout = new Attribute.Layout[((List)this.attrDefs.get(i)).size()];
/*  962 */       ((List)this.attrDefs.get(i)).toArray(arrayOfLayout);
/*  963 */       for (int j = 1; ; j = 0) {
/*  964 */         for (int k = 0; k < arrayOfLayout.length; k++) {
/*  965 */           Attribute.Layout localLayout = arrayOfLayout[k];
/*  966 */           if ((localLayout != null) && 
/*  967 */             (j == isPredefinedAttr(i, k)))
/*      */           {
/*  969 */             int m = this.attrCounts[i][k];
/*  970 */             if (m != 0)
/*      */             {
/*  972 */               int[] arrayOfInt = (int[])this.backCountTable.get(localLayout);
/*  973 */               for (int n = 0; n < arrayOfInt.length; n++)
/*  974 */                 if (arrayOfInt[n] >= 0) {
/*  975 */                   int i1 = arrayOfInt[n];
/*  976 */                   arrayOfInt[n] = -1;
/*  977 */                   localIntBand.putInt(i1);
/*  978 */                   assert (localLayout.getCallables()[n].flagTest((byte)8));
/*      */                 } else {
/*  980 */                   assert (!localLayout.getCallables()[n].flagTest((byte)8));
/*      */                 }
/*      */             }
/*      */           }
/*      */         }
/*  984 */         if (j == 0) break;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void trimClassAttributes() {
/*  990 */     for (Package.Class localClass : this.pkg.classes)
/*      */     {
/*  992 */       localClass.minimizeSourceFile();
/*      */     }
/*      */   }
/*      */ 
/*      */   void collectInnerClasses()
/*      */   {
/*  999 */     HashMap localHashMap = new HashMap();
/*      */ 
/* 1001 */     for (Object localObject1 = this.pkg.classes.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Package.Class)((Iterator)localObject1).next();
/* 1002 */       if (((Package.Class)localObject2).hasInnerClasses())
/* 1003 */         for (localObject3 = ((Package.Class)localObject2).getInnerClasses().iterator(); ((Iterator)localObject3).hasNext(); ) { Package.InnerClass localInnerClass1 = (Package.InnerClass)((Iterator)localObject3).next();
/* 1004 */           Package.InnerClass localInnerClass2 = (Package.InnerClass)localHashMap.put(localInnerClass1.thisClass, localInnerClass1);
/* 1005 */           if ((localInnerClass2 != null) && (!localInnerClass2.equals(localInnerClass1)) && (localInnerClass2.predictable))
/*      */           {
/* 1007 */             localHashMap.put(localInnerClass2.thisClass, localInnerClass2);
/*      */           }
/*      */         }
/*      */     }
/* 1025 */     Object localObject3;
/* 1012 */     localObject1 = new Package.InnerClass[localHashMap.size()];
/* 1013 */     localHashMap.values().toArray((Object[])localObject1);
/* 1014 */     localHashMap = null;
/*      */ 
/* 1019 */     Arrays.sort((Object[])localObject1);
/* 1020 */     this.pkg.setAllInnerClasses(Arrays.asList((Object[])localObject1));
/*      */ 
/* 1025 */     for (Object localObject2 = this.pkg.classes.iterator(); ((Iterator)localObject2).hasNext(); ) { localObject3 = (Package.Class)((Iterator)localObject2).next();
/* 1026 */       ((Package.Class)localObject3).minimizeLocalICs(); }
/*      */   }
/*      */ 
/*      */   void writeInnerClasses() throws IOException
/*      */   {
/* 1031 */     for (Package.InnerClass localInnerClass : this.pkg.getAllInnerClasses()) {
/* 1032 */       int i = localInnerClass.flags;
/* 1033 */       assert ((i & 0x10000) == 0);
/* 1034 */       if (!localInnerClass.predictable) {
/* 1035 */         i |= 65536;
/*      */       }
/* 1037 */       this.ic_this_class.putRef(localInnerClass.thisClass);
/* 1038 */       this.ic_flags.putInt(i);
/* 1039 */       if (!localInnerClass.predictable) {
/* 1040 */         this.ic_outer_class.putRef(localInnerClass.outerClass);
/* 1041 */         this.ic_name.putRef(localInnerClass.name);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void writeLocalInnerClasses(Package.Class paramClass)
/*      */     throws IOException
/*      */   {
/* 1051 */     List localList = paramClass.getInnerClasses();
/* 1052 */     this.class_InnerClasses_N.putInt(localList.size());
/* 1053 */     for (Package.InnerClass localInnerClass : localList) {
/* 1054 */       this.class_InnerClasses_RC.putRef(localInnerClass.thisClass);
/*      */ 
/* 1056 */       if (localInnerClass.equals(this.pkg.getGlobalInnerClass(localInnerClass.thisClass)))
/*      */       {
/* 1058 */         this.class_InnerClasses_F.putInt(0);
/*      */       } else {
/* 1060 */         int i = localInnerClass.flags;
/* 1061 */         if (i == 0)
/* 1062 */           i = 65536;
/* 1063 */         this.class_InnerClasses_F.putInt(i);
/* 1064 */         this.class_InnerClasses_outer_RCN.putRef(localInnerClass.outerClass);
/* 1065 */         this.class_InnerClasses_name_RUN.putRef(localInnerClass.name);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void writeClassesAndByteCodes() throws IOException {
/* 1071 */     Package.Class[] arrayOfClass = new Package.Class[this.pkg.classes.size()];
/* 1072 */     this.pkg.classes.toArray(arrayOfClass);
/*      */ 
/* 1074 */     if (this.verbose > 0) {
/* 1075 */       Utils.log.info("  ...scanning " + arrayOfClass.length + " classes...");
/*      */     }
/* 1077 */     int i = 0;
/* 1078 */     for (int j = 0; j < arrayOfClass.length; j++)
/*      */     {
/* 1080 */       Package.Class localClass = arrayOfClass[j];
/* 1081 */       if (this.verbose > 1) {
/* 1082 */         Utils.log.fine("Scanning " + localClass);
/*      */       }
/* 1084 */       ConstantPool.ClassEntry localClassEntry1 = localClass.thisClass;
/* 1085 */       ConstantPool.ClassEntry localClassEntry2 = localClass.superClass;
/* 1086 */       ConstantPool.ClassEntry[] arrayOfClassEntry = localClass.interfaces;
/*      */ 
/* 1088 */       assert (localClassEntry2 != localClassEntry1);
/* 1089 */       if (localClassEntry2 == null) localClassEntry2 = localClassEntry1;
/* 1090 */       this.class_this.putRef(localClassEntry1);
/* 1091 */       this.class_super.putRef(localClassEntry2);
/* 1092 */       this.class_interface_count.putInt(localClass.interfaces.length);
/* 1093 */       for (int k = 0; k < arrayOfClassEntry.length; k++) {
/* 1094 */         this.class_interface.putRef(arrayOfClassEntry[k]);
/*      */       }
/*      */ 
/* 1097 */       writeMembers(localClass);
/* 1098 */       writeAttrs(0, localClass, localClass);
/*      */ 
/* 1100 */       i++;
/* 1101 */       if ((this.verbose > 0) && (i % 1000 == 0))
/* 1102 */         Utils.log.info("Have scanned " + i + " classes...");
/*      */     }
/*      */   }
/*      */ 
/*      */   void writeMembers(Package.Class paramClass) throws IOException {
/* 1107 */     List localList = paramClass.getFields();
/* 1108 */     this.class_field_count.putInt(localList.size());
/* 1109 */     for (Object localObject1 = localList.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Package.Class.Field)((Iterator)localObject1).next();
/* 1110 */       this.field_descr.putRef(((Package.Class.Field)localObject2).getDescriptor());
/* 1111 */       writeAttrs(1, (Attribute.Holder)localObject2, paramClass);
/*      */     }
/*      */ 
/* 1114 */     localObject1 = paramClass.getMethods();
/* 1115 */     this.class_method_count.putInt(((List)localObject1).size());
/* 1116 */     for (Object localObject2 = ((List)localObject1).iterator(); ((Iterator)localObject2).hasNext(); ) { Package.Class.Method localMethod = (Package.Class.Method)((Iterator)localObject2).next();
/* 1117 */       this.method_descr.putRef(localMethod.getDescriptor());
/* 1118 */       writeAttrs(2, localMethod, paramClass);
/* 1119 */       if (!$assertionsDisabled) if ((localMethod.code != null ? 1 : 0) != (localMethod.getAttribute(this.attrCodeEmpty) != null ? 1 : 0)) throw new AssertionError();
/* 1120 */       if (localMethod.code != null) {
/* 1121 */         writeCodeHeader(localMethod.code);
/* 1122 */         writeByteCodes(localMethod.code);
/*      */       } }
/*      */   }
/*      */ 
/*      */   void writeCodeHeader(Code paramCode) throws IOException
/*      */   {
/* 1128 */     boolean bool = testBit(this.archiveOptions, 4);
/* 1129 */     int i = paramCode.attributeSize();
/* 1130 */     int j = shortCodeHeader(paramCode);
/* 1131 */     if ((!bool) && (i > 0))
/*      */     {
/* 1133 */       j = 0;
/* 1134 */     }if (this.verbose > 2) {
/* 1135 */       int k = paramCode.getMethod().getArgumentSize();
/* 1136 */       Utils.log.fine("Code sizes info " + paramCode.max_stack + " " + paramCode.max_locals + " " + paramCode.getHandlerCount() + " " + k + " " + i + (j > 0 ? " SHORT=" + j : ""));
/*      */     }
/* 1138 */     this.code_headers.putByte(j);
/* 1139 */     if (j == 0) {
/* 1140 */       this.code_max_stack.putInt(paramCode.getMaxStack());
/* 1141 */       this.code_max_na_locals.putInt(paramCode.getMaxNALocals());
/* 1142 */       this.code_handler_count.putInt(paramCode.getHandlerCount());
/*      */     } else {
/* 1144 */       assert ((bool) || (i == 0));
/* 1145 */       assert (paramCode.getHandlerCount() < this.shortCodeHeader_h_limit);
/*      */     }
/* 1147 */     writeCodeHandlers(paramCode);
/* 1148 */     if ((j == 0) || (bool))
/* 1149 */       writeAttrs(3, paramCode, paramCode.thisClass());
/*      */   }
/*      */ 
/*      */   void writeCodeHandlers(Code paramCode) throws IOException
/*      */   {
/* 1154 */     int k = 0; for (int m = paramCode.getHandlerCount(); k < m; k++) {
/* 1155 */       this.code_handler_class_RCN.putRef(paramCode.handler_class[k]);
/*      */ 
/* 1158 */       int i = paramCode.encodeBCI(paramCode.handler_start[k]);
/* 1159 */       this.code_handler_start_P.putInt(i);
/* 1160 */       int j = paramCode.encodeBCI(paramCode.handler_end[k]) - i;
/* 1161 */       this.code_handler_end_PO.putInt(j);
/* 1162 */       i += j;
/* 1163 */       j = paramCode.encodeBCI(paramCode.handler_catch[k]) - i;
/* 1164 */       this.code_handler_catch_PO.putInt(j);
/*      */     }
/*      */   }
/*      */ 
/*      */   void writeAttrs(int paramInt, final Attribute.Holder paramHolder, Package.Class paramClass)
/*      */     throws IOException
/*      */   {
/* 1173 */     BandStructure.MultiBand localMultiBand = this.attrBands[paramInt];
/* 1174 */     BandStructure.IntBand localIntBand1 = getAttrBand(localMultiBand, 0);
/* 1175 */     BandStructure.IntBand localIntBand2 = getAttrBand(localMultiBand, 1);
/* 1176 */     boolean bool = haveFlagsHi(paramInt);
/* 1177 */     if (!$assertionsDisabled) if (this.attrIndexLimit[paramInt] != (bool ? 63 : 32)) throw new AssertionError();
/* 1178 */     if (paramHolder.attributes == null) {
/* 1179 */       localIntBand2.putInt(paramHolder.flags);
/* 1180 */       if (bool)
/* 1181 */         localIntBand1.putInt(0);
/* 1182 */       return;
/*      */     }
/* 1184 */     if (this.verbose > 3) {
/* 1185 */       Utils.log.fine("Transmitting attrs for " + paramHolder + " flags=" + Integer.toHexString(paramHolder.flags));
/*      */     }
/* 1187 */     long l1 = this.attrFlagMask[paramInt];
/* 1188 */     long l2 = 0L;
/* 1189 */     int i = 0;
/* 1190 */     for (Object localObject1 = paramHolder.attributes.iterator(); ((Iterator)localObject1).hasNext(); ) { Attribute localAttribute = (Attribute)((Iterator)localObject1).next();
/* 1191 */       Attribute.Layout localLayout = localAttribute.layout();
/* 1192 */       int j = ((Integer)this.attrIndexTable.get(localLayout)).intValue();
/* 1193 */       assert (((List)this.attrDefs.get(paramInt)).get(j) == localLayout);
/* 1194 */       if (this.verbose > 3)
/* 1195 */         Utils.log.fine("add attr @" + j + " " + localAttribute + " in " + paramHolder);
/*      */       Object localObject2;
/* 1196 */       if ((j < this.attrIndexLimit[paramInt]) && (testBit(l1, 1L << j))) {
/* 1197 */         if (this.verbose > 3)
/* 1198 */           Utils.log.fine("Adding flag bit 1<<" + j + " in " + Long.toHexString(l1));
/* 1199 */         assert (!testBit(paramHolder.flags, 1L << j));
/* 1200 */         l2 |= 1L << j;
/* 1201 */         l1 -= (1L << j);
/*      */       }
/*      */       else {
/* 1204 */         l2 |= 65536L;
/* 1205 */         i++;
/* 1206 */         if (this.verbose > 3)
/* 1207 */           Utils.log.fine("Adding overflow attr #" + i);
/* 1208 */         localObject2 = getAttrBand(localMultiBand, 3);
/* 1209 */         ((BandStructure.IntBand)localObject2).putInt(j);
/*      */       }
/*      */ 
/* 1212 */       if (localLayout.bandCount == 0) {
/* 1213 */         if (localLayout == this.attrInnerClassesEmpty)
/*      */         {
/* 1215 */           writeLocalInnerClasses((Package.Class)paramHolder);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1221 */         assert (localAttribute.fixups == null);
/* 1222 */         localObject2 = (BandStructure.Band[])this.attrBandTable.get(localLayout);
/* 1223 */         assert (localObject2 != null);
/* 1224 */         assert (localObject2.length == localLayout.bandCount);
/* 1225 */         final int[] arrayOfInt = (int[])this.backCountTable.get(localLayout);
/* 1226 */         assert (arrayOfInt != null);
/* 1227 */         assert (arrayOfInt.length == localLayout.getCallables().length);
/*      */ 
/* 1229 */         if (this.verbose > 2) Utils.log.fine("writing " + localAttribute + " in " + paramHolder);
/* 1230 */         int k = (paramInt == 1) && (localLayout == this.attrConstantValue) ? 1 : 0;
/* 1231 */         if (k != 0) setConstantValueIndex((Package.Class.Field)paramHolder);
/* 1232 */         localAttribute.parse(paramClass, localAttribute.bytes(), 0, localAttribute.size(), new Attribute.ValueStream()
/*      */         {
/*      */           public void putInt(int paramAnonymousInt1, int paramAnonymousInt2) {
/* 1235 */             ((BandStructure.IntBand)this.val$ab[paramAnonymousInt1]).putInt(paramAnonymousInt2);
/*      */           }
/*      */           public void putRef(int paramAnonymousInt, ConstantPool.Entry paramAnonymousEntry) {
/* 1238 */             ((BandStructure.CPRefBand)this.val$ab[paramAnonymousInt]).putRef(paramAnonymousEntry);
/*      */           }
/*      */           public int encodeBCI(int paramAnonymousInt) {
/* 1241 */             Code localCode = (Code)paramHolder;
/* 1242 */             return localCode.encodeBCI(paramAnonymousInt);
/*      */           }
/*      */           public void noteBackCall(int paramAnonymousInt) {
/* 1245 */             assert (arrayOfInt[paramAnonymousInt] >= 0);
/* 1246 */             arrayOfInt[paramAnonymousInt] += 1;
/*      */           }
/*      */         });
/* 1249 */         if (k != 0) setConstantValueIndex(null);
/*      */       }
/*      */     }
/* 1252 */     if (i > 0) {
/* 1253 */       localObject1 = getAttrBand(localMultiBand, 2);
/* 1254 */       ((BandStructure.IntBand)localObject1).putInt(i);
/*      */     }
/*      */ 
/* 1257 */     localIntBand2.putInt(paramHolder.flags | (int)l2);
/* 1258 */     if (bool)
/* 1259 */       localIntBand1.putInt((int)(l2 >>> 32));
/*      */     else {
/* 1261 */       assert (l2 >>> 32 == 0L);
/*      */     }
/* 1263 */     assert ((paramHolder.flags & l2) == 0L) : (paramHolder + ".flags=" + Integer.toHexString(paramHolder.flags) + "^" + Long.toHexString(l2));
/*      */   }
/*      */ 
/*      */   private void beginCode(Code paramCode)
/*      */   {
/* 1273 */     assert (this.curCode == null);
/* 1274 */     this.curCode = paramCode;
/* 1275 */     this.curClass = paramCode.m.thisClass();
/* 1276 */     this.curCPMap = paramCode.getCPMap();
/*      */   }
/*      */   private void endCode() {
/* 1279 */     this.curCode = null;
/* 1280 */     this.curClass = null;
/* 1281 */     this.curCPMap = null;
/*      */   }
/*      */ 
/*      */   private int initOpVariant(Instruction paramInstruction, ConstantPool.Entry paramEntry)
/*      */   {
/* 1287 */     if (paramInstruction.getBC() != 183) return -1;
/* 1288 */     ConstantPool.MemberEntry localMemberEntry = (ConstantPool.MemberEntry)paramInstruction.getCPRef(this.curCPMap);
/* 1289 */     if (!"<init>".equals(localMemberEntry.descRef.nameRef.stringValue()))
/* 1290 */       return -1;
/* 1291 */     ConstantPool.ClassEntry localClassEntry = localMemberEntry.classRef;
/* 1292 */     if (localClassEntry == this.curClass.thisClass)
/* 1293 */       return 230;
/* 1294 */     if (localClassEntry == this.curClass.superClass)
/* 1295 */       return 231;
/* 1296 */     if (localClassEntry == paramEntry)
/* 1297 */       return 232;
/* 1298 */     return -1;
/*      */   }
/*      */ 
/*      */   private int selfOpVariant(Instruction paramInstruction)
/*      */   {
/* 1304 */     int i = paramInstruction.getBC();
/* 1305 */     if ((i < 178) || (i > 184)) return -1;
/* 1306 */     ConstantPool.MemberEntry localMemberEntry = (ConstantPool.MemberEntry)paramInstruction.getCPRef(this.curCPMap);
/* 1307 */     ConstantPool.ClassEntry localClassEntry = localMemberEntry.classRef;
/* 1308 */     int j = 202 + (i - 178);
/* 1309 */     if (localClassEntry == this.curClass.thisClass)
/* 1310 */       return j;
/* 1311 */     if (localClassEntry == this.curClass.superClass)
/* 1312 */       return j + 14;
/* 1313 */     return -1;
/*      */   }
/*      */ 
/*      */   void writeByteCodes(Code paramCode) throws IOException {
/* 1317 */     beginCode(paramCode);
/* 1318 */     ConstantPool.IndexGroup localIndexGroup = this.pkg.cp;
/*      */ 
/* 1321 */     int i = 0;
/*      */ 
/* 1324 */     Object localObject = null;
/*      */ 
/* 1326 */     for (Instruction localInstruction1 = paramCode.instructionAt(0); localInstruction1 != null; localInstruction1 = localInstruction1.next())
/*      */     {
/* 1328 */       if (this.verbose > 3) Utils.log.fine(localInstruction1.toString());
/*      */ 
/* 1330 */       if ((localInstruction1.isNonstandard()) && ((!this.p200.getBoolean("com.sun.java.util.jar.pack.invokedynamic")) || (localInstruction1.getBC() != 186)))
/*      */       {
/* 1335 */         String str = paramCode.getMethod() + " contains an unrecognized bytecode " + localInstruction1 + "; please use the pass-file option on this class.";
/*      */ 
/* 1338 */         Utils.log.warning(str);
/* 1339 */         throw new IOException(str);
/*      */       }
/*      */ 
/* 1342 */       if (localInstruction1.isWide()) {
/* 1343 */         if (this.verbose > 1) {
/* 1344 */           Utils.log.fine("_wide opcode in " + paramCode);
/* 1345 */           Utils.log.fine(localInstruction1.toString());
/*      */         }
/* 1347 */         this.bc_codes.putByte(196);
/* 1348 */         this.codeHist['Ä'] += 1;
/*      */       }
/*      */ 
/* 1351 */       int j = localInstruction1.getBC();
/*      */ 
/* 1354 */       if (j == 42)
/*      */       {
/* 1356 */         Instruction localInstruction2 = paramCode.instructionAt(localInstruction1.getNextPC());
/* 1357 */         if (selfOpVariant(localInstruction2) >= 0) {
/* 1358 */           i = 1;
/* 1359 */           continue;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1364 */       int k = initOpVariant(localInstruction1, (ConstantPool.Entry)localObject);
/* 1365 */       if (k >= 0) {
/* 1366 */         if (i != 0)
/*      */         {
/* 1368 */           this.bc_codes.putByte(42);
/* 1369 */           this.codeHist[42] += 1;
/* 1370 */           i = 0;
/*      */         }
/*      */ 
/* 1373 */         this.bc_codes.putByte(k);
/* 1374 */         this.codeHist[k] += 1;
/* 1375 */         ConstantPool.MemberEntry localMemberEntry1 = (ConstantPool.MemberEntry)localInstruction1.getCPRef(this.curCPMap);
/*      */ 
/* 1377 */         int n = localIndexGroup.getOverloadingIndex(localMemberEntry1);
/* 1378 */         this.bc_initref.putInt(n);
/*      */       }
/*      */       else
/*      */       {
/* 1382 */         int m = selfOpVariant(localInstruction1);
/*      */         int i2;
/*      */         int i3;
/* 1383 */         if (m >= 0) {
/* 1384 */           boolean bool = Instruction.isFieldOp(j);
/* 1385 */           i2 = m >= 216 ? 1 : 0;
/* 1386 */           i3 = i;
/* 1387 */           i = 0;
/* 1388 */           if (i3 != 0) {
/* 1389 */             m += 7;
/*      */           }
/* 1391 */           this.bc_codes.putByte(m);
/* 1392 */           this.codeHist[m] += 1;
/*      */ 
/* 1394 */           ConstantPool.MemberEntry localMemberEntry2 = (ConstantPool.MemberEntry)localInstruction1.getCPRef(this.curCPMap);
/* 1395 */           BandStructure.CPRefBand localCPRefBand2 = selfOpRefBand(m);
/* 1396 */           ConstantPool.Index localIndex = localIndexGroup.getMemberIndex(localMemberEntry2.tag, localMemberEntry2.classRef);
/* 1397 */           localCPRefBand2.putRef(localMemberEntry2, localIndex);
/*      */         }
/*      */         else {
/* 1400 */           assert (i == 0);
/*      */ 
/* 1404 */           this.codeHist[j] += 1;
/*      */           int i5;
/*      */           int i6;
/* 1405 */           switch (j) {
/*      */           case 170:
/*      */           case 171:
/* 1408 */             this.bc_codes.putByte(j);
/* 1409 */             Instruction.Switch localSwitch = (Instruction.Switch)localInstruction1;
/*      */ 
/* 1411 */             i2 = localSwitch.getAlignedPC();
/* 1412 */             i3 = localSwitch.getNextPC();
/*      */ 
/* 1414 */             i5 = localSwitch.getCaseCount();
/* 1415 */             this.bc_case_count.putInt(i5);
/* 1416 */             putLabel(this.bc_label, paramCode, localInstruction1.getPC(), localSwitch.getDefaultLabel());
/* 1417 */             for (i6 = 0; i6 < i5; i6++) {
/* 1418 */               putLabel(this.bc_label, paramCode, localInstruction1.getPC(), localSwitch.getCaseLabel(i6));
/*      */             }
/*      */ 
/* 1421 */             if (j == 170)
/* 1422 */               this.bc_case_value.putInt(localSwitch.getCaseValue(0));
/*      */             else {
/* 1424 */               for (i6 = 0; i6 < i5; i6++) {
/* 1425 */                 this.bc_case_value.putInt(localSwitch.getCaseValue(i6));
/*      */               }
/*      */             }
/*      */ 
/* 1429 */             break;
/*      */           default:
/*      */             int i1;
/*      */             ConstantPool.Entry localEntry;
/* 1432 */             switch (j)
/*      */             {
/*      */             case 186:
/* 1435 */               localInstruction1.setNonstandardLength(3);
/* 1436 */               i1 = localInstruction1.getShortAt(1);
/* 1437 */               localEntry = i1 == 0 ? null : this.curCPMap[i1];
/*      */ 
/* 1439 */               this.bc_codes.putByte(254);
/* 1440 */               this.bc_escsize.putInt(1);
/* 1441 */               this.bc_escbyte.putByte(j);
/*      */ 
/* 1443 */               this.bc_codes.putByte(253);
/* 1444 */               this.bc_escrefsize.putInt(2);
/* 1445 */               this.bc_escref.putRef(localEntry);
/* 1446 */               break;
/*      */             default:
/* 1450 */               i1 = localInstruction1.getBranchLabel();
/* 1451 */               if (i1 >= 0) {
/* 1452 */                 this.bc_codes.putByte(j);
/* 1453 */                 putLabel(this.bc_label, paramCode, localInstruction1.getPC(), i1);
/*      */               }
/*      */               else {
/* 1456 */                 localEntry = localInstruction1.getCPRef(this.curCPMap);
/* 1457 */                 if (localEntry != null) {
/* 1458 */                   if (j == 187) localObject = localEntry;
/* 1459 */                   if (j == 18) this.ldcHist[localEntry.tag] += 1;
/*      */ 
/* 1461 */                   i5 = j;
/*      */                   BandStructure.CPRefBand localCPRefBand1;
/* 1462 */                   switch (localInstruction1.getCPTag()) {
/*      */                   case 20:
/* 1464 */                     switch (localEntry.tag) {
/*      */                     case 3:
/* 1466 */                       localCPRefBand1 = this.bc_intref;
/* 1467 */                       switch (j) { case 18:
/* 1468 */                         i5 = 234; break;
/*      */                       case 19:
/* 1469 */                         i5 = 237; break;
/*      */                       default:
/* 1470 */                         if (!$assertionsDisabled) throw new AssertionError(); break;
/*      */                       }
/*      */                       break;
/*      */                     case 4:
/* 1474 */                       localCPRefBand1 = this.bc_floatref;
/* 1475 */                       switch (j) { case 18:
/* 1476 */                         i5 = 235; break;
/*      */                       case 19:
/* 1477 */                         i5 = 238; break;
/*      */                       default:
/* 1478 */                         if (!$assertionsDisabled) throw new AssertionError(); break;
/*      */                       }
/*      */                       break;
/*      */                     case 5:
/* 1482 */                       localCPRefBand1 = this.bc_longref;
/* 1483 */                       assert (j == 20);
/* 1484 */                       i5 = 20;
/* 1485 */                       break;
/*      */                     case 6:
/* 1487 */                       localCPRefBand1 = this.bc_doubleref;
/* 1488 */                       assert (j == 20);
/* 1489 */                       i5 = 239;
/* 1490 */                       break;
/*      */                     case 8:
/* 1492 */                       localCPRefBand1 = this.bc_stringref;
/* 1493 */                       switch (j) { case 18:
/* 1494 */                         i5 = 18; break;
/*      */                       case 19:
/* 1495 */                         i5 = 19; break;
/*      */                       default:
/* 1496 */                         if (!$assertionsDisabled) throw new AssertionError(); break;
/*      */                       }
/*      */                       break;
/*      */                     case 7:
/* 1500 */                       localCPRefBand1 = this.bc_classref;
/* 1501 */                       switch (j) { case 18:
/* 1502 */                         i5 = 233; break;
/*      */                       case 19:
/* 1503 */                         i5 = 236; break;
/*      */                       default:
/* 1504 */                         if (!$assertionsDisabled) throw new AssertionError(); break;
/*      */                       }
/*      */                       break;
/*      */                     default:
/* 1508 */                       localCPRefBand1 = null;
/* 1509 */                       if (!$assertionsDisabled) throw new AssertionError();
/*      */                       break;
/*      */                     }
/*      */                     break;
/*      */                   case 7:
/* 1514 */                     if (localEntry == this.curClass.thisClass) localEntry = null;
/* 1515 */                     localCPRefBand1 = this.bc_classref; break;
/*      */                   case 9:
/* 1517 */                     localCPRefBand1 = this.bc_fieldref; break;
/*      */                   case 10:
/* 1519 */                     localCPRefBand1 = this.bc_methodref; break;
/*      */                   case 11:
/* 1521 */                     localCPRefBand1 = this.bc_imethodref; break;
/*      */                   case 8:
/*      */                   case 12:
/*      */                   case 13:
/*      */                   case 14:
/*      */                   case 15:
/*      */                   case 16:
/*      */                   case 17:
/*      */                   case 18:
/*      */                   case 19:
/*      */                   default:
/* 1523 */                     localCPRefBand1 = null;
/* 1524 */                     if (!$assertionsDisabled) throw new AssertionError(); break;
/*      */                   }
/* 1526 */                   this.bc_codes.putByte(i5);
/* 1527 */                   localCPRefBand1.putRef(localEntry);
/*      */ 
/* 1529 */                   if (j == 197) {
/* 1530 */                     assert (localInstruction1.getConstant() == paramCode.getByte(localInstruction1.getPC() + 3));
/*      */ 
/* 1532 */                     this.bc_byte.putByte(0xFF & localInstruction1.getConstant());
/* 1533 */                   } else if (j == 185) {
/* 1534 */                     assert (localInstruction1.getLength() == 5);
/*      */ 
/* 1536 */                     if ((!$assertionsDisabled) && (localInstruction1.getConstant() != 1 + ((ConstantPool.MemberEntry)localEntry).descRef.typeRef.computeSize(true) << 8)) throw new AssertionError();
/*      */ 
/*      */                   }
/* 1539 */                   else if (!$assertionsDisabled) { if (localInstruction1.getLength() != (j == 18 ? 2 : 3)) throw new AssertionError(); 
/*      */                   }
/*      */                 }
/*      */                 else
/*      */                 {
/* 1543 */                   int i4 = localInstruction1.getLocalSlot();
/* 1544 */                   if (i4 >= 0) {
/* 1545 */                     this.bc_codes.putByte(j);
/* 1546 */                     this.bc_local.putInt(i4);
/* 1547 */                     i5 = localInstruction1.getConstant();
/* 1548 */                     if (j == 132) {
/* 1549 */                       if (!localInstruction1.isWide())
/* 1550 */                         this.bc_byte.putByte(0xFF & i5);
/*      */                       else {
/* 1552 */                         this.bc_short.putInt(0xFFFF & i5);
/*      */                       }
/*      */                     }
/* 1555 */                     else if ((!$assertionsDisabled) && (i5 != 0)) throw new AssertionError();
/*      */ 
/*      */                   }
/*      */                   else
/*      */                   {
/* 1560 */                     this.bc_codes.putByte(j);
/* 1561 */                     i5 = localInstruction1.getPC() + 1;
/* 1562 */                     i6 = localInstruction1.getNextPC();
/* 1563 */                     if (i5 < i6)
/*      */                     {
/* 1565 */                       switch (j) {
/*      */                       case 17:
/* 1567 */                         this.bc_short.putInt(0xFFFF & localInstruction1.getConstant());
/* 1568 */                         break;
/*      */                       case 16:
/* 1570 */                         this.bc_byte.putByte(0xFF & localInstruction1.getConstant());
/* 1571 */                         break;
/*      */                       case 188:
/* 1573 */                         this.bc_byte.putByte(0xFF & localInstruction1.getConstant());
/* 1574 */                         break;
/*      */                       default:
/* 1576 */                         if (!$assertionsDisabled) throw new AssertionError(); break; }  }  }  }  } break; } break; } 
/*      */         }
/*      */       }
/*      */     }
/* 1580 */     this.bc_codes.putByte(255);
/* 1581 */     this.bc_codes.elementCountForDebug += 1;
/* 1582 */     this.codeHist['ÿ'] += 1;
/* 1583 */     endCode();
/*      */   }
/*      */ 
/*      */   void printCodeHist()
/*      */   {
/* 1589 */     assert (this.verbose > 0);
/* 1590 */     String[] arrayOfString = new String[this.codeHist.length];
/* 1591 */     int i = 0;
/* 1592 */     for (int j = 0; j < this.codeHist.length; j++) {
/* 1593 */       i += this.codeHist[j];
/*      */     }
/* 1595 */     for (j = 0; j < this.codeHist.length; j++)
/* 1596 */       if (this.codeHist[j] == 0) { arrayOfString[j] = ""; } else {
/* 1597 */         String str1 = Instruction.byteName(j);
/* 1598 */         String str2 = "" + this.codeHist[j];
/* 1599 */         str2 = "         ".substring(str2.length()) + str2;
/* 1600 */         String str3 = "" + this.codeHist[j] * 10000 / i;
/* 1601 */         while (str3.length() < 4) {
/* 1602 */           str3 = "0" + str3;
/*      */         }
/* 1604 */         str3 = str3.substring(0, str3.length() - 2) + "." + str3.substring(str3.length() - 2);
/* 1605 */         arrayOfString[j] = (str2 + "  " + str3 + "%  " + str1);
/*      */       }
/* 1607 */     Arrays.sort(arrayOfString);
/* 1608 */     System.out.println("Bytecode histogram [" + i + "]");
/* 1609 */     j = arrayOfString.length;
/*      */     while (true) { j--; if (j < 0) break;
/* 1610 */       if (!"".equals(arrayOfString[j]))
/* 1611 */         System.out.println(arrayOfString[j]);
/*      */     }
/* 1613 */     for (j = 0; j < this.ldcHist.length; j++) {
/* 1614 */       int k = this.ldcHist[j];
/* 1615 */       if (k != 0)
/* 1616 */         System.out.println("ldc " + ConstantPool.tagName(j) + " " + k);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.PackageWriter
 * JD-Core Version:    0.6.2
 */