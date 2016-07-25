/*      */ package com.sun.java.util.jar.pack;
/*      */ 
/*      */ import java.io.BufferedInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.EOFException;
/*      */ import java.io.FilterInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ 
/*      */ class PackageReader extends BandStructure
/*      */ {
/*      */   Package pkg;
/*      */   byte[] bytes;
/*      */   LimitedBuffer in;
/*  201 */   int[] tagCount = new int[14];
/*      */   int numFiles;
/*      */   int numAttrDefs;
/*      */   int numInnerClasses;
/*      */   int numClasses;
/*      */   static final int MAGIC_BYTES = 4;
/*      */   Map<ConstantPool.Utf8Entry, ConstantPool.SignatureEntry> utf8Signatures;
/*      */   static final int NO_FLAGS_YET = 0;
/* 1015 */   Comparator<ConstantPool.Entry> entryOutputOrder = new Comparator() {
/*      */     public int compare(ConstantPool.Entry paramAnonymousEntry1, ConstantPool.Entry paramAnonymousEntry2) {
/* 1017 */       int i = PackageReader.this.getOutputIndex(paramAnonymousEntry1);
/* 1018 */       int j = PackageReader.this.getOutputIndex(paramAnonymousEntry2);
/* 1019 */       if ((i >= 0) && (j >= 0))
/*      */       {
/* 1021 */         return i - j;
/* 1022 */       }if (i == j)
/*      */       {
/* 1024 */         return paramAnonymousEntry1.compareTo(paramAnonymousEntry2);
/*      */       }
/* 1026 */       return i >= 0 ? -1 : 1;
/*      */     }
/* 1015 */   };
/*      */   Code[] allCodes;
/*      */   List<Code> codesWithFlags;
/* 1198 */   Map<Package.Class, Set<ConstantPool.Entry>> ldcRefMap = new HashMap();
/*      */ 
/*      */   PackageReader(Package paramPackage, InputStream paramInputStream)
/*      */     throws IOException
/*      */   {
/*   70 */     this.pkg = paramPackage;
/*   71 */     this.in = new LimitedBuffer(paramInputStream);
/*      */   }
/*      */ 
/*      */   void read()
/*      */     throws IOException
/*      */   {
/*  156 */     int i = 0;
/*      */     try
/*      */     {
/*  167 */       readFileHeader();
/*  168 */       readBandHeaders();
/*  169 */       readConstantPool();
/*  170 */       readAttrDefs();
/*  171 */       readInnerClasses();
/*  172 */       Package.Class[] arrayOfClass = readClasses();
/*  173 */       readByteCodes();
/*  174 */       readFiles();
/*  175 */       assert ((this.archiveSize1 == 0L) || (this.in.atLimit()));
/*  176 */       assert ((this.archiveSize1 == 0L) || (this.in.getBytesServed() == this.archiveSize0 + this.archiveSize1));
/*      */ 
/*  178 */       this.all_bands.doneDisbursing();
/*      */ 
/*  181 */       for (int j = 0; j < arrayOfClass.length; j++) {
/*  182 */         reconstructClass(arrayOfClass[j]);
/*      */       }
/*      */ 
/*  185 */       i = 1;
/*      */     } catch (Exception localException) {
/*  187 */       Utils.log.warning("Error on input: " + localException, localException);
/*  188 */       if (this.verbose > 0) {
/*  189 */         Utils.log.info("Stream offsets: served=" + this.in.getBytesServed() + " buffered=" + this.in.buffered + " limit=" + this.in.limit);
/*      */       }
/*      */ 
/*  194 */       if ((localException instanceof IOException)) throw ((IOException)localException);
/*  195 */       if ((localException instanceof RuntimeException)) throw ((RuntimeException)localException);
/*  196 */       throw new Error("error unpacking", localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   void readFileHeader()
/*      */     throws IOException
/*      */   {
/*  210 */     readArchiveMagic();
/*  211 */     readArchiveHeader();
/*      */   }
/*      */ 
/*      */   private int getMagicInt32()
/*      */     throws IOException
/*      */   {
/*  217 */     int i = 0;
/*  218 */     for (int j = 0; j < 4; j++) {
/*  219 */       i <<= 8;
/*  220 */       i |= this.archive_magic.getByte() & 0xFF;
/*      */     }
/*  222 */     return i;
/*      */   }
/*      */ 
/*      */   void readArchiveMagic()
/*      */     throws IOException
/*      */   {
/*  230 */     this.in.setReadLimit(19L);
/*      */ 
/*  234 */     this.archive_magic.expectLength(4);
/*  235 */     this.archive_magic.readFrom(this.in);
/*      */ 
/*  238 */     this.pkg.magic = getMagicInt32();
/*  239 */     this.archive_magic.doneDisbursing();
/*      */   }
/*      */ 
/*      */   void readArchiveHeader()
/*      */     throws IOException
/*      */   {
/*  269 */     assert (26 == 8 + ConstantPool.TAGS_IN_ORDER.length + 6);
/*  270 */     this.archive_header_0.expectLength(3);
/*  271 */     this.archive_header_0.readFrom(this.in);
/*      */ 
/*  273 */     this.pkg.package_minver = this.archive_header_0.getInt();
/*  274 */     this.pkg.package_majver = this.archive_header_0.getInt();
/*  275 */     this.pkg.checkVersion();
/*  276 */     initPackageMajver(this.pkg.package_majver);
/*      */ 
/*  278 */     this.archiveOptions = this.archive_header_0.getInt();
/*  279 */     this.archive_header_0.doneDisbursing();
/*      */ 
/*  282 */     boolean bool1 = testBit(this.archiveOptions, 1);
/*  283 */     boolean bool2 = testBit(this.archiveOptions, 16);
/*  284 */     boolean bool3 = testBit(this.archiveOptions, 2);
/*  285 */     initAttrIndexLimit();
/*      */ 
/*  288 */     this.archive_header_S.expectLength(bool2 ? 2 : 0);
/*  289 */     this.archive_header_S.readFrom(this.in);
/*  290 */     if (bool2) {
/*  291 */       long l1 = this.archive_header_S.getInt();
/*  292 */       long l2 = this.archive_header_S.getInt();
/*  293 */       this.archiveSize1 = ((l1 << 32) + (l2 << 32 >>> 32));
/*      */ 
/*  295 */       this.in.setReadLimit(this.archiveSize1);
/*      */     } else {
/*  297 */       this.archiveSize1 = 0L;
/*  298 */       this.in.setReadLimit(-1L);
/*      */     }
/*  300 */     this.archive_header_S.doneDisbursing();
/*  301 */     this.archiveSize0 = this.in.getBytesServed();
/*      */ 
/*  303 */     int i = 21;
/*  304 */     if (!bool2) i -= 3;
/*  305 */     if (!bool1) i -= 2;
/*  306 */     if (!bool3) i -= 4;
/*  307 */     assert (i >= 12);
/*  308 */     this.archive_header_1.expectLength(i);
/*  309 */     this.archive_header_1.readFrom(this.in);
/*      */ 
/*  311 */     if (bool2) {
/*  312 */       this.archiveNextCount = this.archive_header_1.getInt();
/*  313 */       this.pkg.default_modtime = this.archive_header_1.getInt();
/*  314 */       this.numFiles = this.archive_header_1.getInt();
/*      */     } else {
/*  316 */       this.archiveNextCount = 0;
/*  317 */       this.numFiles = 0;
/*      */     }
/*      */ 
/*  320 */     if (bool1) {
/*  321 */       this.band_headers.expectLength(this.archive_header_1.getInt());
/*  322 */       this.numAttrDefs = this.archive_header_1.getInt();
/*      */     } else {
/*  324 */       this.band_headers.expectLength(0);
/*  325 */       this.numAttrDefs = 0;
/*      */     }
/*      */ 
/*  328 */     readConstantPoolCounts(bool3);
/*      */ 
/*  330 */     this.numInnerClasses = this.archive_header_1.getInt();
/*      */ 
/*  332 */     this.pkg.default_class_minver = ((short)this.archive_header_1.getInt());
/*  333 */     this.pkg.default_class_majver = ((short)this.archive_header_1.getInt());
/*  334 */     this.numClasses = this.archive_header_1.getInt();
/*      */ 
/*  336 */     this.archive_header_1.doneDisbursing();
/*      */ 
/*  339 */     if (testBit(this.archiveOptions, 32))
/*  340 */       this.pkg.default_options |= 1;
/*      */   }
/*      */ 
/*      */   void readBandHeaders() throws IOException
/*      */   {
/*  345 */     this.band_headers.readFrom(this.in);
/*  346 */     this.bandHeaderBytePos = 1;
/*  347 */     this.bandHeaderBytes = new byte[this.bandHeaderBytePos + this.band_headers.length()];
/*  348 */     for (int i = this.bandHeaderBytePos; i < this.bandHeaderBytes.length; i++) {
/*  349 */       this.bandHeaderBytes[i] = ((byte)this.band_headers.getByte());
/*      */     }
/*  351 */     this.band_headers.doneDisbursing();
/*      */   }
/*      */ 
/*      */   void readConstantPoolCounts(boolean paramBoolean) throws IOException
/*      */   {
/*  356 */     for (int i = 0; i < ConstantPool.TAGS_IN_ORDER.length; i++)
/*      */     {
/*  374 */       int j = ConstantPool.TAGS_IN_ORDER[i];
/*  375 */       if (!paramBoolean)
/*      */       {
/*  377 */         switch (j) {
/*      */         case 3:
/*      */         case 4:
/*      */         case 5:
/*      */         case 6:
/*  382 */           break;
/*      */         }
/*      */       }
/*  385 */       else this.tagCount[j] = this.archive_header_1.getInt(); 
/*      */     }
/*      */   }
/*      */ 
/*      */   protected ConstantPool.Index getCPIndex(byte paramByte)
/*      */   {
/*  390 */     return this.pkg.cp.getIndexByTag(paramByte);
/*      */   }
/*      */   ConstantPool.Index initCPIndex(byte paramByte, ConstantPool.Entry[] paramArrayOfEntry) {
/*  393 */     if (this.verbose > 3) {
/*  394 */       for (int i = 0; i < paramArrayOfEntry.length; i++) {
/*  395 */         Utils.log.fine("cp.add " + paramArrayOfEntry[i]);
/*      */       }
/*      */     }
/*  398 */     ConstantPool.Index localIndex = ConstantPool.makeIndex(ConstantPool.tagName(paramByte), paramArrayOfEntry);
/*  399 */     if (this.verbose > 1) Utils.log.fine("Read " + localIndex);
/*  400 */     this.pkg.cp.initIndexByTag(paramByte, localIndex);
/*  401 */     return localIndex;
/*      */   }
/*      */ 
/*      */   void readConstantPool()
/*      */     throws IOException
/*      */   {
/*  419 */     if (this.verbose > 0) Utils.log.info("Reading CP");
/*      */ 
/*  421 */     for (int i = 0; i < ConstantPool.TAGS_IN_ORDER.length; i++) {
/*  422 */       byte b = ConstantPool.TAGS_IN_ORDER[i];
/*  423 */       int j = this.tagCount[b];
/*      */ 
/*  425 */       ConstantPool.Entry[] arrayOfEntry = new ConstantPool.Entry[j];
/*  426 */       if (this.verbose > 0)
/*  427 */         Utils.log.info("Reading " + arrayOfEntry.length + " " + ConstantPool.tagName(b) + " entries...");
/*      */       int k;
/*      */       int m;
/*      */       long l1;
/*      */       long l2;
/*      */       long l3;
/*      */       Object localObject1;
/*      */       Object localObject2;
/*  429 */       switch (b) {
/*      */       case 1:
/*  431 */         readUtf8Bands(arrayOfEntry);
/*  432 */         break;
/*      */       case 3:
/*  434 */         this.cp_Int.expectLength(arrayOfEntry.length);
/*  435 */         this.cp_Int.readFrom(this.in);
/*  436 */         for (k = 0; k < arrayOfEntry.length; k++) {
/*  437 */           m = this.cp_Int.getInt();
/*  438 */           arrayOfEntry[k] = ConstantPool.getLiteralEntry(Integer.valueOf(m));
/*      */         }
/*  440 */         this.cp_Int.doneDisbursing();
/*  441 */         break;
/*      */       case 4:
/*  443 */         this.cp_Float.expectLength(arrayOfEntry.length);
/*  444 */         this.cp_Float.readFrom(this.in);
/*  445 */         for (k = 0; k < arrayOfEntry.length; k++) {
/*  446 */           m = this.cp_Float.getInt();
/*  447 */           float f = Float.intBitsToFloat(m);
/*  448 */           arrayOfEntry[k] = ConstantPool.getLiteralEntry(Float.valueOf(f));
/*      */         }
/*  450 */         this.cp_Float.doneDisbursing();
/*  451 */         break;
/*      */       case 5:
/*  456 */         this.cp_Long_hi.expectLength(arrayOfEntry.length);
/*  457 */         this.cp_Long_hi.readFrom(this.in);
/*  458 */         this.cp_Long_lo.expectLength(arrayOfEntry.length);
/*  459 */         this.cp_Long_lo.readFrom(this.in);
/*  460 */         for (k = 0; k < arrayOfEntry.length; k++) {
/*  461 */           l1 = this.cp_Long_hi.getInt();
/*  462 */           l2 = this.cp_Long_lo.getInt();
/*  463 */           l3 = (l1 << 32) + (l2 << 32 >>> 32);
/*  464 */           arrayOfEntry[k] = ConstantPool.getLiteralEntry(Long.valueOf(l3));
/*      */         }
/*  466 */         this.cp_Long_hi.doneDisbursing();
/*  467 */         this.cp_Long_lo.doneDisbursing();
/*  468 */         break;
/*      */       case 6:
/*  473 */         this.cp_Double_hi.expectLength(arrayOfEntry.length);
/*  474 */         this.cp_Double_hi.readFrom(this.in);
/*  475 */         this.cp_Double_lo.expectLength(arrayOfEntry.length);
/*  476 */         this.cp_Double_lo.readFrom(this.in);
/*  477 */         for (k = 0; k < arrayOfEntry.length; k++) {
/*  478 */           l1 = this.cp_Double_hi.getInt();
/*  479 */           l2 = this.cp_Double_lo.getInt();
/*  480 */           l3 = (l1 << 32) + (l2 << 32 >>> 32);
/*  481 */           double d = Double.longBitsToDouble(l3);
/*  482 */           arrayOfEntry[k] = ConstantPool.getLiteralEntry(Double.valueOf(d));
/*      */         }
/*  484 */         this.cp_Double_hi.doneDisbursing();
/*  485 */         this.cp_Double_lo.doneDisbursing();
/*  486 */         break;
/*      */       case 8:
/*  488 */         this.cp_String.expectLength(arrayOfEntry.length);
/*  489 */         this.cp_String.readFrom(this.in);
/*  490 */         this.cp_String.setIndex(getCPIndex((byte)1));
/*  491 */         for (k = 0; k < arrayOfEntry.length; k++) {
/*  492 */           arrayOfEntry[k] = ConstantPool.getLiteralEntry(this.cp_String.getRef().stringValue());
/*      */         }
/*  494 */         this.cp_String.doneDisbursing();
/*  495 */         break;
/*      */       case 7:
/*  497 */         this.cp_Class.expectLength(arrayOfEntry.length);
/*  498 */         this.cp_Class.readFrom(this.in);
/*  499 */         this.cp_Class.setIndex(getCPIndex((byte)1));
/*  500 */         for (k = 0; k < arrayOfEntry.length; k++) {
/*  501 */           arrayOfEntry[k] = ConstantPool.getClassEntry(this.cp_Class.getRef().stringValue());
/*      */         }
/*  503 */         this.cp_Class.doneDisbursing();
/*  504 */         break;
/*      */       case 13:
/*  506 */         readSignatureBands(arrayOfEntry);
/*  507 */         break;
/*      */       case 12:
/*  512 */         this.cp_Descr_name.expectLength(arrayOfEntry.length);
/*  513 */         this.cp_Descr_name.readFrom(this.in);
/*  514 */         this.cp_Descr_name.setIndex(getCPIndex((byte)1));
/*  515 */         this.cp_Descr_type.expectLength(arrayOfEntry.length);
/*  516 */         this.cp_Descr_type.readFrom(this.in);
/*  517 */         this.cp_Descr_type.setIndex(getCPIndex((byte)13));
/*  518 */         for (k = 0; k < arrayOfEntry.length; k++) {
/*  519 */           localObject1 = this.cp_Descr_name.getRef();
/*  520 */           localObject2 = this.cp_Descr_type.getRef();
/*  521 */           arrayOfEntry[k] = ConstantPool.getDescriptorEntry((ConstantPool.Utf8Entry)localObject1, (ConstantPool.SignatureEntry)localObject2);
/*      */         }
/*      */ 
/*  524 */         this.cp_Descr_name.doneDisbursing();
/*  525 */         this.cp_Descr_type.doneDisbursing();
/*  526 */         break;
/*      */       case 9:
/*  528 */         readMemberRefs(b, arrayOfEntry, this.cp_Field_class, this.cp_Field_desc);
/*  529 */         break;
/*      */       case 10:
/*  531 */         readMemberRefs(b, arrayOfEntry, this.cp_Method_class, this.cp_Method_desc);
/*  532 */         break;
/*      */       case 11:
/*  534 */         readMemberRefs(b, arrayOfEntry, this.cp_Imethod_class, this.cp_Imethod_desc);
/*  535 */         break;
/*      */       case 2:
/*      */       default:
/*  537 */         if (!$assertionsDisabled) throw new AssertionError();
/*      */         break;
/*      */       }
/*  540 */       ConstantPool.Index localIndex = initCPIndex(b, arrayOfEntry);
/*      */ 
/*  542 */       if (this.optDumpBands) {
/*  543 */         localObject1 = new PrintStream(getDumpStream(localIndex, ".idx")); localObject2 = null;
/*      */         try { printArrayTo((PrintStream)localObject1, localIndex.cpMap, 0, localIndex.cpMap.length); }
/*      */         catch (Throwable localThrowable2)
/*      */         {
/*  543 */           localObject2 = localThrowable2; throw localThrowable2;
/*      */         } finally {
/*  545 */           if (localObject1 != null) if (localObject2 != null) try { ((PrintStream)localObject1).close(); } catch (Throwable localThrowable3) { ((Throwable)localObject2).addSuppressed(localThrowable3); } else ((PrintStream)localObject1).close();
/*      */         }
/*      */       }
/*      */     }
/*  549 */     this.cp_bands.doneDisbursing();
/*      */ 
/*  551 */     setBandIndexes();
/*      */   }
/*      */ 
/*      */   void readUtf8Bands(ConstantPool.Entry[] paramArrayOfEntry)
/*      */     throws IOException
/*      */   {
/*  562 */     int i = paramArrayOfEntry.length;
/*  563 */     if (i == 0) {
/*  564 */       return;
/*      */     }
/*      */ 
/*  571 */     this.cp_Utf8_prefix.expectLength(Math.max(0, i - 2));
/*  572 */     this.cp_Utf8_prefix.readFrom(this.in);
/*      */ 
/*  575 */     this.cp_Utf8_suffix.expectLength(Math.max(0, i - 1));
/*  576 */     this.cp_Utf8_suffix.readFrom(this.in);
/*      */ 
/*  578 */     char[][] arrayOfChar = new char[i][];
/*  579 */     int j = 0;
/*      */ 
/*  582 */     this.cp_Utf8_chars.expectLength(this.cp_Utf8_suffix.getIntTotal());
/*  583 */     this.cp_Utf8_chars.readFrom(this.in);
/*      */     int i1;
/*  584 */     for (int k = 0; k < i; k++) {
/*  585 */       m = k < 1 ? 0 : this.cp_Utf8_suffix.getInt();
/*  586 */       if ((m == 0) && (k >= 1))
/*      */       {
/*  588 */         j++;
/*      */       }
/*      */       else {
/*  591 */         arrayOfChar[k] = new char[m];
/*  592 */         for (n = 0; n < m; n++) {
/*  593 */           i1 = this.cp_Utf8_chars.getInt();
/*  594 */           assert (i1 == (char)i1);
/*  595 */           arrayOfChar[k][n] = ((char)i1);
/*      */         }
/*      */       }
/*      */     }
/*  598 */     this.cp_Utf8_chars.doneDisbursing();
/*      */ 
/*  601 */     k = 0;
/*  602 */     this.cp_Utf8_big_suffix.expectLength(j);
/*  603 */     this.cp_Utf8_big_suffix.readFrom(this.in);
/*  604 */     this.cp_Utf8_suffix.resetForSecondPass();
/*  605 */     for (int m = 0; m < i; m++) {
/*  606 */       n = m < 1 ? 0 : this.cp_Utf8_suffix.getInt();
/*  607 */       i1 = m < 2 ? 0 : this.cp_Utf8_prefix.getInt();
/*  608 */       if ((n == 0) && (m >= 1)) {
/*  609 */         assert (arrayOfChar[m] == null);
/*  610 */         n = this.cp_Utf8_big_suffix.getInt();
/*      */       } else {
/*  612 */         assert (arrayOfChar[m] != null);
/*      */       }
/*  614 */       if (k < i1 + n)
/*  615 */         k = i1 + n;
/*      */     }
/*  617 */     char[] arrayOfChar1 = new char[k];
/*      */ 
/*  620 */     this.cp_Utf8_suffix.resetForSecondPass();
/*  621 */     this.cp_Utf8_big_suffix.resetForSecondPass();
/*  622 */     for (int n = 0; n < i; n++)
/*  623 */       if (n >= 1) {
/*  624 */         i1 = this.cp_Utf8_suffix.getInt();
/*  625 */         if (i1 == 0) {
/*  626 */           i1 = this.cp_Utf8_big_suffix.getInt();
/*  627 */           arrayOfChar[n] = new char[i1];
/*  628 */           if (i1 != 0)
/*      */           {
/*  632 */             BandStructure.IntBand localIntBand = this.cp_Utf8_big_chars.newIntBand("(Utf8_big_" + n + ")");
/*  633 */             localIntBand.expectLength(i1);
/*  634 */             localIntBand.readFrom(this.in);
/*  635 */             for (int i3 = 0; i3 < i1; i3++) {
/*  636 */               int i4 = localIntBand.getInt();
/*  637 */               assert (i4 == (char)i4);
/*  638 */               arrayOfChar[n][i3] = ((char)i4);
/*      */             }
/*  640 */             localIntBand.doneDisbursing();
/*      */           }
/*      */         }
/*      */       }
/*  642 */     this.cp_Utf8_big_chars.doneDisbursing();
/*      */ 
/*  645 */     this.cp_Utf8_prefix.resetForSecondPass();
/*  646 */     this.cp_Utf8_suffix.resetForSecondPass();
/*  647 */     this.cp_Utf8_big_suffix.resetForSecondPass();
/*  648 */     for (n = 0; n < i; n++) {
/*  649 */       i1 = n < 2 ? 0 : this.cp_Utf8_prefix.getInt();
/*  650 */       int i2 = n < 1 ? 0 : this.cp_Utf8_suffix.getInt();
/*  651 */       if ((i2 == 0) && (n >= 1)) {
/*  652 */         i2 = this.cp_Utf8_big_suffix.getInt();
/*      */       }
/*      */ 
/*  655 */       System.arraycopy(arrayOfChar[n], 0, arrayOfChar1, i1, i2);
/*      */ 
/*  657 */       paramArrayOfEntry[n] = ConstantPool.getUtf8Entry(new String(arrayOfChar1, 0, i1 + i2));
/*      */     }
/*      */ 
/*  660 */     this.cp_Utf8_prefix.doneDisbursing();
/*  661 */     this.cp_Utf8_suffix.doneDisbursing();
/*  662 */     this.cp_Utf8_big_suffix.doneDisbursing();
/*      */   }
/*      */ 
/*      */   void readSignatureBands(ConstantPool.Entry[] paramArrayOfEntry)
/*      */     throws IOException
/*      */   {
/*  671 */     this.cp_Signature_form.expectLength(paramArrayOfEntry.length);
/*  672 */     this.cp_Signature_form.readFrom(this.in);
/*  673 */     this.cp_Signature_form.setIndex(getCPIndex((byte)1));
/*  674 */     int[] arrayOfInt = new int[paramArrayOfEntry.length];
/*      */     ConstantPool.Utf8Entry localUtf8Entry;
/*  675 */     for (int i = 0; i < paramArrayOfEntry.length; i++) {
/*  676 */       localUtf8Entry = (ConstantPool.Utf8Entry)this.cp_Signature_form.getRef();
/*  677 */       arrayOfInt[i] = ConstantPool.countClassParts(localUtf8Entry);
/*      */     }
/*  679 */     this.cp_Signature_form.resetForSecondPass();
/*  680 */     this.cp_Signature_classes.expectLength(getIntTotal(arrayOfInt));
/*  681 */     this.cp_Signature_classes.readFrom(this.in);
/*  682 */     this.cp_Signature_classes.setIndex(getCPIndex((byte)7));
/*  683 */     this.utf8Signatures = new HashMap();
/*  684 */     for (i = 0; i < paramArrayOfEntry.length; i++) {
/*  685 */       localUtf8Entry = (ConstantPool.Utf8Entry)this.cp_Signature_form.getRef();
/*  686 */       ConstantPool.ClassEntry[] arrayOfClassEntry = new ConstantPool.ClassEntry[arrayOfInt[i]];
/*  687 */       for (int j = 0; j < arrayOfClassEntry.length; j++) {
/*  688 */         arrayOfClassEntry[j] = ((ConstantPool.ClassEntry)this.cp_Signature_classes.getRef());
/*      */       }
/*  690 */       ConstantPool.SignatureEntry localSignatureEntry = ConstantPool.getSignatureEntry(localUtf8Entry, arrayOfClassEntry);
/*  691 */       paramArrayOfEntry[i] = localSignatureEntry;
/*  692 */       this.utf8Signatures.put(localSignatureEntry.asUtf8Entry(), localSignatureEntry);
/*      */     }
/*  694 */     this.cp_Signature_form.doneDisbursing();
/*  695 */     this.cp_Signature_classes.doneDisbursing();
/*      */   }
/*      */ 
/*      */   void readMemberRefs(byte paramByte, ConstantPool.Entry[] paramArrayOfEntry, BandStructure.CPRefBand paramCPRefBand1, BandStructure.CPRefBand paramCPRefBand2)
/*      */     throws IOException
/*      */   {
/*  708 */     paramCPRefBand1.expectLength(paramArrayOfEntry.length);
/*  709 */     paramCPRefBand1.readFrom(this.in);
/*  710 */     paramCPRefBand1.setIndex(getCPIndex((byte)7));
/*  711 */     paramCPRefBand2.expectLength(paramArrayOfEntry.length);
/*  712 */     paramCPRefBand2.readFrom(this.in);
/*  713 */     paramCPRefBand2.setIndex(getCPIndex((byte)12));
/*  714 */     for (int i = 0; i < paramArrayOfEntry.length; i++) {
/*  715 */       ConstantPool.ClassEntry localClassEntry = (ConstantPool.ClassEntry)paramCPRefBand1.getRef();
/*  716 */       ConstantPool.DescriptorEntry localDescriptorEntry = (ConstantPool.DescriptorEntry)paramCPRefBand2.getRef();
/*  717 */       paramArrayOfEntry[i] = ConstantPool.getMemberEntry(paramByte, localClassEntry, localDescriptorEntry);
/*      */     }
/*  719 */     paramCPRefBand1.doneDisbursing();
/*  720 */     paramCPRefBand2.doneDisbursing();
/*      */   }
/*      */ 
/*      */   void readFiles()
/*      */     throws IOException
/*      */   {
/*  731 */     if (this.verbose > 0)
/*  732 */       Utils.log.info("  ...building " + this.numFiles + " files...");
/*  733 */     this.file_name.expectLength(this.numFiles);
/*  734 */     this.file_size_lo.expectLength(this.numFiles);
/*  735 */     int i = this.archiveOptions;
/*  736 */     boolean bool1 = testBit(i, 256);
/*  737 */     boolean bool2 = testBit(i, 64);
/*  738 */     boolean bool3 = testBit(i, 128);
/*  739 */     if (bool1)
/*  740 */       this.file_size_hi.expectLength(this.numFiles);
/*  741 */     if (bool2)
/*  742 */       this.file_modtime.expectLength(this.numFiles);
/*  743 */     if (bool3) {
/*  744 */       this.file_options.expectLength(this.numFiles);
/*      */     }
/*  746 */     this.file_name.readFrom(this.in);
/*  747 */     this.file_size_hi.readFrom(this.in);
/*  748 */     this.file_size_lo.readFrom(this.in);
/*  749 */     this.file_modtime.readFrom(this.in);
/*  750 */     this.file_options.readFrom(this.in);
/*  751 */     this.file_bits.setInputStreamFrom(this.in);
/*      */ 
/*  753 */     Iterator localIterator = this.pkg.getClasses().iterator();
/*      */ 
/*  756 */     long l1 = 0L;
/*  757 */     long[] arrayOfLong = new long[this.numFiles];
/*  758 */     for (int j = 0; j < this.numFiles; j++) {
/*  759 */       long l2 = this.file_size_lo.getInt() << 32 >>> 32;
/*  760 */       if (bool1)
/*  761 */         l2 += (this.file_size_hi.getInt() << 32);
/*  762 */       arrayOfLong[j] = l2;
/*  763 */       l1 += l2;
/*      */     }
/*  765 */     assert ((this.in.getReadLimit() == -1L) || (this.in.getReadLimit() == l1));
/*      */ 
/*  767 */     byte[] arrayOfByte = new byte[65536];
/*  768 */     for (int k = 0; k < this.numFiles; k++)
/*      */     {
/*  770 */       ConstantPool.Utf8Entry localUtf8Entry = (ConstantPool.Utf8Entry)this.file_name.getRef();
/*  771 */       long l3 = arrayOfLong[k];
/*      */       Package tmp382_379 = this.pkg; tmp382_379.getClass(); Package.File localFile = new Package.File(tmp382_379, localUtf8Entry);
/*  773 */       localFile.modtime = this.pkg.default_modtime;
/*  774 */       localFile.options = this.pkg.default_options;
/*  775 */       if (bool2)
/*  776 */         localFile.modtime += this.file_modtime.getInt();
/*  777 */       if (bool3)
/*  778 */         localFile.options |= this.file_options.getInt();
/*  779 */       if (this.verbose > 1)
/*  780 */         Utils.log.fine("Reading " + l3 + " bytes of " + localUtf8Entry.stringValue());
/*  781 */       long l4 = l3;
/*  782 */       while (l4 > 0L) {
/*  783 */         int m = arrayOfByte.length;
/*  784 */         if (m > l4) m = (int)l4;
/*  785 */         m = this.file_bits.getInputStream().read(arrayOfByte, 0, m);
/*  786 */         if (m < 0) throw new EOFException();
/*  787 */         localFile.addBytes(arrayOfByte, 0, m);
/*  788 */         l4 -= m;
/*      */       }
/*  790 */       this.pkg.addFile(localFile);
/*  791 */       if (localFile.isClassStub()) {
/*  792 */         assert (localFile.getFileLength() == 0L);
/*  793 */         Package.Class localClass2 = (Package.Class)localIterator.next();
/*  794 */         localClass2.initFile(localFile);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  799 */     while (localIterator.hasNext()) {
/*  800 */       Package.Class localClass1 = (Package.Class)localIterator.next();
/*  801 */       localClass1.initFile(null);
/*  802 */       localClass1.file.modtime = this.pkg.default_modtime;
/*      */     }
/*      */ 
/*  805 */     this.file_name.doneDisbursing();
/*  806 */     this.file_size_hi.doneDisbursing();
/*  807 */     this.file_size_lo.doneDisbursing();
/*  808 */     this.file_modtime.doneDisbursing();
/*  809 */     this.file_options.doneDisbursing();
/*  810 */     this.file_bits.doneDisbursing();
/*  811 */     this.file_bands.doneDisbursing();
/*      */ 
/*  813 */     if ((this.archiveSize1 != 0L) && (!this.in.atLimit()))
/*  814 */       throw new RuntimeException("Predicted archive_size " + this.archiveSize1 + " != " + (this.in.getBytesServed() - this.archiveSize0));
/*      */   }
/*      */ 
/*      */   void readAttrDefs()
/*      */     throws IOException
/*      */   {
/*  825 */     this.attr_definition_headers.expectLength(this.numAttrDefs);
/*  826 */     this.attr_definition_name.expectLength(this.numAttrDefs);
/*  827 */     this.attr_definition_layout.expectLength(this.numAttrDefs);
/*  828 */     this.attr_definition_headers.readFrom(this.in);
/*  829 */     this.attr_definition_name.readFrom(this.in);
/*  830 */     this.attr_definition_layout.readFrom(this.in);
/*  831 */     PrintStream localPrintStream = !this.optDumpBands ? null : new PrintStream(getDumpStream(this.attr_definition_headers, ".def")); Object localObject1 = null;
/*      */     try
/*      */     {
/*  834 */       for (int i = 0; i < this.numAttrDefs; i++) {
/*  835 */         int j = this.attr_definition_headers.getByte();
/*  836 */         ConstantPool.Utf8Entry localUtf8Entry1 = (ConstantPool.Utf8Entry)this.attr_definition_name.getRef();
/*  837 */         ConstantPool.Utf8Entry localUtf8Entry2 = (ConstantPool.Utf8Entry)this.attr_definition_layout.getRef();
/*  838 */         int k = j & 0x3;
/*  839 */         int m = (j >> 2) - 1;
/*  840 */         Attribute.Layout localLayout = new Attribute.Layout(k, localUtf8Entry1.stringValue(), localUtf8Entry2.stringValue());
/*      */ 
/*  844 */         String str = localLayout.layoutForPackageMajver(getPackageMajver());
/*  845 */         if (!str.equals(localLayout.layout())) {
/*  846 */           throw new IOException("Bad attribute layout in version 150 archive: " + localLayout.layout());
/*      */         }
/*  848 */         setAttributeLayoutIndex(localLayout, m);
/*  849 */         if (localPrintStream != null) localPrintStream.println(m + " " + localLayout);
/*      */       }
/*      */     }
/*      */     catch (Throwable localThrowable2)
/*      */     {
/*  831 */       localObject1 = localThrowable2; throw localThrowable2;
/*      */     }
/*      */     finally
/*      */     {
/*  851 */       if (localPrintStream != null) if (localObject1 != null) try { localPrintStream.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localPrintStream.close(); 
/*      */     }
/*  852 */     this.attr_definition_headers.doneDisbursing();
/*  853 */     this.attr_definition_name.doneDisbursing();
/*  854 */     this.attr_definition_layout.doneDisbursing();
/*      */ 
/*  857 */     makeNewAttributeBands();
/*  858 */     this.attr_definition_bands.doneDisbursing();
/*      */   }
/*      */ 
/*      */   void readInnerClasses()
/*      */     throws IOException
/*      */   {
/*  867 */     this.ic_this_class.expectLength(this.numInnerClasses);
/*  868 */     this.ic_this_class.readFrom(this.in);
/*  869 */     this.ic_flags.expectLength(this.numInnerClasses);
/*  870 */     this.ic_flags.readFrom(this.in);
/*  871 */     int i = 0;
/*      */     int m;
/*  872 */     for (int j = 0; j < this.numInnerClasses; j++) {
/*  873 */       k = this.ic_flags.getInt();
/*  874 */       m = (k & 0x10000) != 0 ? 1 : 0;
/*  875 */       if (m != 0) {
/*  876 */         i++;
/*      */       }
/*      */     }
/*  879 */     this.ic_outer_class.expectLength(i);
/*  880 */     this.ic_outer_class.readFrom(this.in);
/*  881 */     this.ic_name.expectLength(i);
/*  882 */     this.ic_name.readFrom(this.in);
/*  883 */     this.ic_flags.resetForSecondPass();
/*  884 */     ArrayList localArrayList = new ArrayList(this.numInnerClasses);
/*  885 */     for (int k = 0; k < this.numInnerClasses; k++) {
/*  886 */       m = this.ic_flags.getInt();
/*  887 */       int n = (m & 0x10000) != 0 ? 1 : 0;
/*  888 */       m &= -65537;
/*  889 */       ConstantPool.ClassEntry localClassEntry1 = (ConstantPool.ClassEntry)this.ic_this_class.getRef();
/*      */       ConstantPool.ClassEntry localClassEntry2;
/*      */       ConstantPool.Utf8Entry localUtf8Entry;
/*  892 */       if (n != 0) {
/*  893 */         localClassEntry2 = (ConstantPool.ClassEntry)this.ic_outer_class.getRef();
/*  894 */         localUtf8Entry = (ConstantPool.Utf8Entry)this.ic_name.getRef();
/*      */       } else {
/*  896 */         localObject = localClassEntry1.stringValue();
/*  897 */         String[] arrayOfString = Package.parseInnerClassName((String)localObject);
/*  898 */         assert (arrayOfString != null);
/*  899 */         String str1 = arrayOfString[0];
/*      */ 
/*  901 */         String str2 = arrayOfString[2];
/*  902 */         if (str1 == null)
/*  903 */           localClassEntry2 = null;
/*      */         else
/*  905 */           localClassEntry2 = ConstantPool.getClassEntry(str1);
/*  906 */         if (str2 == null)
/*  907 */           localUtf8Entry = null;
/*      */         else
/*  909 */           localUtf8Entry = ConstantPool.getUtf8Entry(str2);
/*      */       }
/*  911 */       Object localObject = new Package.InnerClass(localClassEntry1, localClassEntry2, localUtf8Entry, m);
/*      */ 
/*  913 */       assert ((n != 0) || (((Package.InnerClass)localObject).predictable));
/*  914 */       localArrayList.add(localObject);
/*      */     }
/*  916 */     this.ic_flags.doneDisbursing();
/*  917 */     this.ic_this_class.doneDisbursing();
/*  918 */     this.ic_outer_class.doneDisbursing();
/*  919 */     this.ic_name.doneDisbursing();
/*  920 */     this.pkg.setAllInnerClasses(localArrayList);
/*  921 */     this.ic_bands.doneDisbursing();
/*      */   }
/*      */ 
/*      */   void readLocalInnerClasses(Package.Class paramClass) throws IOException {
/*  925 */     int i = this.class_InnerClasses_N.getInt();
/*  926 */     ArrayList localArrayList = new ArrayList(i);
/*  927 */     for (int j = 0; j < i; j++) {
/*  928 */       ConstantPool.ClassEntry localClassEntry = (ConstantPool.ClassEntry)this.class_InnerClasses_RC.getRef();
/*  929 */       int k = this.class_InnerClasses_F.getInt();
/*      */       Object localObject;
/*  930 */       if (k == 0)
/*      */       {
/*  932 */         localObject = this.pkg.getGlobalInnerClass(localClassEntry);
/*  933 */         assert (localObject != null);
/*  934 */         localArrayList.add(localObject);
/*      */       } else {
/*  936 */         if (k == 65536)
/*  937 */           k = 0;
/*  938 */         localObject = (ConstantPool.ClassEntry)this.class_InnerClasses_outer_RCN.getRef();
/*  939 */         ConstantPool.Utf8Entry localUtf8Entry = (ConstantPool.Utf8Entry)this.class_InnerClasses_name_RUN.getRef();
/*  940 */         localArrayList.add(new Package.InnerClass(localClassEntry, (ConstantPool.ClassEntry)localObject, localUtf8Entry, k));
/*      */       }
/*      */     }
/*  943 */     paramClass.setInnerClasses(localArrayList);
/*      */   }
/*      */ 
/*      */   Package.Class[] readClasses()
/*      */     throws IOException
/*      */   {
/*  961 */     Package.Class[] arrayOfClass = new Package.Class[this.numClasses];
/*  962 */     if (this.verbose > 0) {
/*  963 */       Utils.log.info("  ...building " + arrayOfClass.length + " classes...");
/*      */     }
/*  965 */     this.class_this.expectLength(this.numClasses);
/*  966 */     this.class_super.expectLength(this.numClasses);
/*  967 */     this.class_interface_count.expectLength(this.numClasses);
/*      */ 
/*  969 */     this.class_this.readFrom(this.in);
/*  970 */     this.class_super.readFrom(this.in);
/*  971 */     this.class_interface_count.readFrom(this.in);
/*  972 */     this.class_interface.expectLength(this.class_interface_count.getIntTotal());
/*  973 */     this.class_interface.readFrom(this.in);
/*  974 */     for (int i = 0; i < arrayOfClass.length; i++) {
/*  975 */       ConstantPool.ClassEntry localClassEntry1 = (ConstantPool.ClassEntry)this.class_this.getRef();
/*  976 */       ConstantPool.ClassEntry localClassEntry2 = (ConstantPool.ClassEntry)this.class_super.getRef();
/*  977 */       ConstantPool.ClassEntry[] arrayOfClassEntry = new ConstantPool.ClassEntry[this.class_interface_count.getInt()];
/*  978 */       for (int j = 0; j < arrayOfClassEntry.length; j++) {
/*  979 */         arrayOfClassEntry[j] = ((ConstantPool.ClassEntry)this.class_interface.getRef());
/*      */       }
/*      */ 
/*  982 */       if (localClassEntry2 == localClassEntry1) localClassEntry2 = null;
/*      */       Package tmp230_227 = this.pkg; tmp230_227.getClass(); Package.Class localClass = new Package.Class(tmp230_227, 0, localClassEntry1, localClassEntry2, arrayOfClassEntry);
/*      */ 
/*  985 */       arrayOfClass[i] = localClass;
/*      */     }
/*  987 */     this.class_this.doneDisbursing();
/*  988 */     this.class_super.doneDisbursing();
/*  989 */     this.class_interface_count.doneDisbursing();
/*  990 */     this.class_interface.doneDisbursing();
/*  991 */     readMembers(arrayOfClass);
/*  992 */     countAndReadAttrs(0, Arrays.asList(arrayOfClass));
/*  993 */     this.pkg.trimToSize();
/*  994 */     readCodeHeaders();
/*      */ 
/*  997 */     return arrayOfClass;
/*      */   }
/*      */ 
/*      */   private int getOutputIndex(ConstantPool.Entry paramEntry)
/*      */   {
/* 1002 */     assert (paramEntry.tag != 13);
/* 1003 */     int i = this.pkg.cp.untypedIndexOf(paramEntry);
/*      */ 
/* 1006 */     if (i >= 0)
/* 1007 */       return i;
/* 1008 */     if (paramEntry.tag == 1) {
/* 1009 */       ConstantPool.Entry localEntry = (ConstantPool.Entry)this.utf8Signatures.get(paramEntry);
/* 1010 */       return this.pkg.cp.untypedIndexOf(localEntry);
/*      */     }
/* 1012 */     return -1;
/*      */   }
/*      */ 
/*      */   void reconstructClass(Package.Class paramClass)
/*      */   {
/* 1031 */     if (this.verbose > 1) Utils.log.fine("reconstruct " + paramClass);
/*      */ 
/* 1034 */     Attribute localAttribute = paramClass.getAttribute(this.attrClassFileVersion);
/* 1035 */     if (localAttribute != null) {
/* 1036 */       paramClass.removeAttribute(localAttribute);
/* 1037 */       short[] arrayOfShort = parseClassFileVersionAttr(localAttribute);
/* 1038 */       paramClass.minver = arrayOfShort[0];
/* 1039 */       paramClass.majver = arrayOfShort[1];
/*      */     } else {
/* 1041 */       paramClass.minver = this.pkg.default_class_minver;
/* 1042 */       paramClass.majver = this.pkg.default_class_majver;
/*      */     }
/*      */ 
/* 1046 */     paramClass.expandSourceFile();
/*      */ 
/* 1049 */     paramClass.setCPMap(reconstructLocalCPMap(paramClass));
/*      */   }
/*      */ 
/*      */   ConstantPool.Entry[] reconstructLocalCPMap(Package.Class paramClass) {
/* 1053 */     Set localSet = (Set)this.ldcRefMap.get(paramClass);
/* 1054 */     HashSet localHashSet = new HashSet();
/*      */ 
/* 1057 */     paramClass.visitRefs(0, localHashSet);
/*      */ 
/* 1060 */     ConstantPool.completeReferencesIn(localHashSet, true);
/*      */ 
/* 1064 */     int i = paramClass.expandLocalICs();
/*      */ 
/* 1066 */     if (i != 0) {
/* 1067 */       if (i > 0)
/*      */       {
/* 1069 */         paramClass.visitInnerClassRefs(0, localHashSet);
/*      */       }
/*      */       else {
/* 1072 */         localHashSet.clear();
/* 1073 */         paramClass.visitRefs(0, localHashSet);
/*      */       }
/*      */ 
/* 1077 */       ConstantPool.completeReferencesIn(localHashSet, true);
/*      */     }
/*      */ 
/* 1081 */     int j = 0;
/* 1082 */     for (Object localObject1 = localHashSet.iterator(); ((Iterator)localObject1).hasNext(); ) { ConstantPool.Entry localEntry1 = (ConstantPool.Entry)((Iterator)localObject1).next();
/* 1083 */       if (localEntry1.isDoubleWord()) j++;
/* 1084 */       assert (localEntry1.tag != 13) : localEntry1;
/*      */     }
/* 1086 */     localObject1 = new ConstantPool.Entry[1 + j + localHashSet.size()];
/* 1087 */     ConstantPool.Entry localEntry2 = 1;
/*      */ 
/* 1090 */     if (localSet != null) {
/* 1091 */       assert (localHashSet.containsAll(localSet));
/* 1092 */       for (localObject2 = localSet.iterator(); ((Iterator)localObject2).hasNext(); ) { localEntry3 = (ConstantPool.Entry)((Iterator)localObject2).next();
/* 1093 */         localObject1[(localEntry2++)] = localEntry3;
/*      */       }
/* 1095 */       assert (localEntry2 == 1 + localSet.size());
/* 1096 */       localHashSet.removeAll(localSet);
/* 1097 */       localSet = null;
/*      */     }
/*      */ 
/* 1101 */     Object localObject2 = localHashSet;
/* 1102 */     localHashSet = null;
/* 1103 */     ConstantPool.Entry localEntry3 = localEntry2;
/* 1104 */     for (Iterator localIterator = ((Set)localObject2).iterator(); localIterator.hasNext(); ) { localEntry4 = (ConstantPool.Entry)localIterator.next();
/* 1105 */       localObject1[(localEntry2++)] = localEntry4;
/*      */     }
/* 1107 */     assert (localEntry2 == localEntry3 + ((Set)localObject2).size());
/* 1108 */     Arrays.sort((Object[])localObject1, 1, localEntry3, this.entryOutputOrder);
/* 1109 */     Arrays.sort((Object[])localObject1, localEntry3, localEntry2, this.entryOutputOrder);
/*      */ 
/* 1111 */     if (this.verbose > 3) {
/* 1112 */       Utils.log.fine("CP of " + this + " {");
/* 1113 */       for (k = 0; k < localEntry2; k++) {
/* 1114 */         localEntry4 = localObject1[k];
/* 1115 */         Utils.log.fine("  " + (localEntry4 == null ? -1 : getOutputIndex(localEntry4)) + " : " + localEntry4);
/*      */       }
/*      */ 
/* 1118 */       Utils.log.fine("}");
/*      */     }
/*      */ 
/* 1122 */     int k = localObject1.length;
/* 1123 */     ConstantPool.Entry localEntry4 = localEntry2;
/*      */     while (true) { localEntry4--; if (localEntry4 < 1) break;
/* 1124 */       Object localObject3 = localObject1[localEntry4];
/* 1125 */       if (localObject3.isDoubleWord())
/* 1126 */         localObject1[(--k)] = null;
/* 1127 */       localObject1[(--k)] = localObject3;
/*      */     }
/* 1129 */     assert (k == 1);
/*      */ 
/* 1131 */     return localObject1;
/*      */   }
/*      */ 
/*      */   void readMembers(Package.Class[] paramArrayOfClass)
/*      */     throws IOException
/*      */   {
/* 1146 */     assert (paramArrayOfClass.length == this.numClasses);
/* 1147 */     this.class_field_count.expectLength(this.numClasses);
/* 1148 */     this.class_method_count.expectLength(this.numClasses);
/* 1149 */     this.class_field_count.readFrom(this.in);
/* 1150 */     this.class_method_count.readFrom(this.in);
/*      */ 
/* 1153 */     int i = this.class_field_count.getIntTotal();
/* 1154 */     int j = this.class_method_count.getIntTotal();
/* 1155 */     this.field_descr.expectLength(i);
/* 1156 */     this.method_descr.expectLength(j);
/* 1157 */     if (this.verbose > 1) Utils.log.fine("expecting #fields=" + i + " and #methods=" + j + " in #classes=" + this.numClasses);
/*      */ 
/* 1159 */     ArrayList localArrayList1 = new ArrayList(i);
/* 1160 */     this.field_descr.readFrom(this.in);
/*      */     int i1;
/* 1161 */     for (int k = 0; k < paramArrayOfClass.length; k++) {
/* 1162 */       Package.Class localClass1 = paramArrayOfClass[k];
/* 1163 */       int n = this.class_field_count.getInt();
/* 1164 */       for (i1 = 0; i1 < n; i1++)
/*      */       {
/*      */         Package.Class tmp218_216 = localClass1; tmp218_216.getClass(); Package.Class.Field localField = new Package.Class.Field(tmp218_216, 0, (ConstantPool.DescriptorEntry)this.field_descr.getRef());
/*      */ 
/* 1167 */         localArrayList1.add(localField);
/*      */       }
/*      */     }
/* 1170 */     this.class_field_count.doneDisbursing();
/* 1171 */     this.field_descr.doneDisbursing();
/* 1172 */     countAndReadAttrs(1, localArrayList1);
/* 1173 */     localArrayList1 = null;
/*      */ 
/* 1175 */     ArrayList localArrayList2 = new ArrayList(j);
/* 1176 */     this.method_descr.readFrom(this.in);
/* 1177 */     for (int m = 0; m < paramArrayOfClass.length; m++) {
/* 1178 */       Package.Class localClass2 = paramArrayOfClass[m];
/* 1179 */       i1 = this.class_method_count.getInt();
/* 1180 */       for (int i2 = 0; i2 < i1; i2++)
/*      */       {
/*      */         Package.Class tmp347_345 = localClass2; tmp347_345.getClass(); Package.Class.Method localMethod = new Package.Class.Method(tmp347_345, 0, (ConstantPool.DescriptorEntry)this.method_descr.getRef());
/*      */ 
/* 1183 */         localArrayList2.add(localMethod);
/*      */       }
/*      */     }
/* 1186 */     this.class_method_count.doneDisbursing();
/* 1187 */     this.method_descr.doneDisbursing();
/* 1188 */     countAndReadAttrs(2, localArrayList2);
/*      */ 
/* 1193 */     this.allCodes = buildCodeAttrs(localArrayList2);
/*      */   }
/*      */ 
/*      */   Code[] buildCodeAttrs(List<Package.Class.Method> paramList)
/*      */   {
/* 1201 */     ArrayList localArrayList = new ArrayList(paramList.size());
/* 1202 */     for (Object localObject = paramList.iterator(); ((Iterator)localObject).hasNext(); ) { Package.Class.Method localMethod = (Package.Class.Method)((Iterator)localObject).next();
/* 1203 */       if (localMethod.getAttribute(this.attrCodeEmpty) != null) {
/* 1204 */         localMethod.code = new Code(localMethod);
/* 1205 */         localArrayList.add(localMethod.code);
/*      */       }
/*      */     }
/* 1208 */     localObject = new Code[localArrayList.size()];
/* 1209 */     localArrayList.toArray((Object[])localObject);
/* 1210 */     return localObject;
/*      */   }
/*      */ 
/*      */   void readCodeHeaders()
/*      */     throws IOException
/*      */   {
/* 1222 */     boolean bool = testBit(this.archiveOptions, 4);
/* 1223 */     this.code_headers.expectLength(this.allCodes.length);
/* 1224 */     this.code_headers.readFrom(this.in);
/* 1225 */     ArrayList localArrayList = new ArrayList(this.allCodes.length / 10);
/*      */     Code localCode;
/* 1226 */     for (int i = 0; i < this.allCodes.length; i++) {
/* 1227 */       localCode = this.allCodes[i];
/* 1228 */       int j = this.code_headers.getByte();
/* 1229 */       assert (j == (j & 0xFF));
/* 1230 */       if (this.verbose > 2)
/* 1231 */         Utils.log.fine("codeHeader " + localCode + " = " + j);
/* 1232 */       if (j == 0)
/*      */       {
/* 1234 */         localArrayList.add(localCode);
/*      */       }
/*      */       else
/*      */       {
/* 1238 */         localCode.setMaxStack(shortCodeHeader_max_stack(j));
/* 1239 */         localCode.setMaxNALocals(shortCodeHeader_max_na_locals(j));
/* 1240 */         localCode.setHandlerCount(shortCodeHeader_handler_count(j));
/* 1241 */         assert (shortCodeHeader(localCode) == j);
/*      */       }
/*      */     }
/* 1243 */     this.code_headers.doneDisbursing();
/* 1244 */     this.code_max_stack.expectLength(localArrayList.size());
/* 1245 */     this.code_max_na_locals.expectLength(localArrayList.size());
/* 1246 */     this.code_handler_count.expectLength(localArrayList.size());
/*      */ 
/* 1249 */     this.code_max_stack.readFrom(this.in);
/* 1250 */     this.code_max_na_locals.readFrom(this.in);
/* 1251 */     this.code_handler_count.readFrom(this.in);
/* 1252 */     for (Iterator localIterator = localArrayList.iterator(); localIterator.hasNext(); ) { localCode = (Code)localIterator.next();
/* 1253 */       localCode.setMaxStack(this.code_max_stack.getInt());
/* 1254 */       localCode.setMaxNALocals(this.code_max_na_locals.getInt());
/* 1255 */       localCode.setHandlerCount(this.code_handler_count.getInt());
/*      */     }
/* 1257 */     this.code_max_stack.doneDisbursing();
/* 1258 */     this.code_max_na_locals.doneDisbursing();
/* 1259 */     this.code_handler_count.doneDisbursing();
/*      */ 
/* 1261 */     readCodeHandlers();
/*      */ 
/* 1263 */     if (bool)
/*      */     {
/* 1265 */       this.codesWithFlags = Arrays.asList(this.allCodes);
/*      */     }
/*      */     else {
/* 1268 */       this.codesWithFlags = localArrayList;
/*      */     }
/* 1270 */     countAttrs(3, this.codesWithFlags);
/*      */   }
/*      */ 
/*      */   void readCodeHandlers()
/*      */     throws IOException
/*      */   {
/* 1282 */     int i = 0;
/* 1283 */     for (int j = 0; j < this.allCodes.length; j++) {
/* 1284 */       Code localCode1 = this.allCodes[j];
/* 1285 */       i += localCode1.getHandlerCount();
/*      */     }
/*      */ 
/* 1288 */     BandStructure.ValueBand[] arrayOfValueBand = { this.code_handler_start_P, this.code_handler_end_PO, this.code_handler_catch_PO, this.code_handler_class_RCN };
/*      */ 
/* 1295 */     for (int k = 0; k < arrayOfValueBand.length; k++) {
/* 1296 */       arrayOfValueBand[k].expectLength(i);
/* 1297 */       arrayOfValueBand[k].readFrom(this.in);
/*      */     }
/*      */ 
/* 1300 */     for (k = 0; k < this.allCodes.length; k++) {
/* 1301 */       Code localCode2 = this.allCodes[k];
/* 1302 */       int m = 0; for (int n = localCode2.getHandlerCount(); m < n; m++) {
/* 1303 */         localCode2.handler_class[m] = this.code_handler_class_RCN.getRef();
/*      */ 
/* 1306 */         localCode2.handler_start[m] = this.code_handler_start_P.getInt();
/* 1307 */         localCode2.handler_end[m] = this.code_handler_end_PO.getInt();
/* 1308 */         localCode2.handler_catch[m] = this.code_handler_catch_PO.getInt();
/*      */       }
/*      */     }
/* 1311 */     for (k = 0; k < arrayOfValueBand.length; k++)
/* 1312 */       arrayOfValueBand[k].doneDisbursing();
/*      */   }
/*      */ 
/*      */   void fixupCodeHandlers()
/*      */   {
/* 1318 */     for (int i = 0; i < this.allCodes.length; i++) {
/* 1319 */       Code localCode = this.allCodes[i];
/* 1320 */       int j = 0; for (int k = localCode.getHandlerCount(); j < k; j++) {
/* 1321 */         int m = localCode.handler_start[j];
/* 1322 */         localCode.handler_start[j] = localCode.decodeBCI(m);
/* 1323 */         m += localCode.handler_end[j];
/* 1324 */         localCode.handler_end[j] = localCode.decodeBCI(m);
/* 1325 */         m += localCode.handler_catch[j];
/* 1326 */         localCode.handler_catch[j] = localCode.decodeBCI(m);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void countAndReadAttrs(int paramInt, Collection paramCollection)
/*      */     throws IOException
/*      */   {
/* 1383 */     countAttrs(paramInt, paramCollection);
/* 1384 */     readAttrs(paramInt, paramCollection);
/*      */   }
/*      */ 
/*      */   void countAttrs(int paramInt, Collection paramCollection)
/*      */     throws IOException
/*      */   {
/* 1391 */     BandStructure.MultiBand localMultiBand = this.attrBands[paramInt];
/* 1392 */     long l1 = this.attrFlagMask[paramInt];
/* 1393 */     if (this.verbose > 1) {
/* 1394 */       Utils.log.fine("scanning flags and attrs for " + Attribute.contextName(paramInt) + "[" + paramCollection.size() + "]");
/*      */     }
/*      */ 
/* 1399 */     List localList = (List)this.attrDefs.get(paramInt);
/* 1400 */     Attribute.Layout[] arrayOfLayout = new Attribute.Layout[localList.size()];
/* 1401 */     localList.toArray(arrayOfLayout);
/* 1402 */     BandStructure.IntBand localIntBand1 = getAttrBand(localMultiBand, 0);
/* 1403 */     BandStructure.IntBand localIntBand2 = getAttrBand(localMultiBand, 1);
/* 1404 */     BandStructure.IntBand localIntBand3 = getAttrBand(localMultiBand, 2);
/* 1405 */     BandStructure.IntBand localIntBand4 = getAttrBand(localMultiBand, 3);
/* 1406 */     BandStructure.IntBand localIntBand5 = getAttrBand(localMultiBand, 4);
/*      */ 
/* 1409 */     int i = this.attrOverflowMask[paramInt];
/* 1410 */     int j = 0;
/* 1411 */     boolean bool1 = haveFlagsHi(paramInt);
/* 1412 */     localIntBand1.expectLength(bool1 ? paramCollection.size() : 0);
/* 1413 */     localIntBand1.readFrom(this.in);
/* 1414 */     localIntBand2.expectLength(paramCollection.size());
/* 1415 */     localIntBand2.readFrom(this.in);
/* 1416 */     assert ((l1 & i) == i);
/* 1417 */     for (Object localObject1 = paramCollection.iterator(); ((Iterator)localObject1).hasNext(); ) {
/* 1418 */       localObject2 = (Attribute.Holder)((Iterator)localObject1).next();
/* 1419 */       int m = localIntBand2.getInt();
/* 1420 */       ((Attribute.Holder)localObject2).flags = m;
/* 1421 */       if ((m & i) != 0) {
/* 1422 */         j++;
/*      */       }
/*      */     }
/*      */ 
/* 1426 */     localIntBand3.expectLength(j);
/* 1427 */     localIntBand3.readFrom(this.in);
/* 1428 */     localIntBand4.expectLength(localIntBand3.getIntTotal());
/* 1429 */     localIntBand4.readFrom(this.in);
/*      */ 
/* 1435 */     localObject1 = new int[arrayOfLayout.length];
/* 1436 */     for (Object localObject2 = paramCollection.iterator(); ((Iterator)localObject2).hasNext(); ) {
/* 1437 */       Attribute.Holder localHolder = (Attribute.Holder)((Iterator)localObject2).next();
/* 1438 */       assert (localHolder.attributes == null);
/*      */ 
/* 1440 */       long l2 = (localHolder.flags & l1) << 32 >>> 32;
/*      */ 
/* 1442 */       localHolder.flags -= (int)l2;
/* 1443 */       assert (localHolder.flags == (char)localHolder.flags);
/* 1444 */       assert ((paramInt != 3) || (localHolder.flags == 0));
/* 1445 */       if (bool1)
/* 1446 */         l2 += (localIntBand1.getInt() << 32);
/* 1447 */       if (l2 != 0L)
/*      */       {
/* 1449 */         i2 = 0;
/* 1450 */         long l3 = l2 & i;
/* 1451 */         assert (l3 >= 0L);
/* 1452 */         l2 -= l3;
/* 1453 */         if (l3 != 0L) {
/* 1454 */           i2 = localIntBand3.getInt();
/*      */         }
/*      */ 
/* 1457 */         int i4 = 0;
/* 1458 */         long l4 = l2;
/* 1459 */         for (int i6 = 0; l4 != 0L; i6++)
/* 1460 */           if ((l4 & 1L << i6) != 0L) {
/* 1461 */             l4 -= (1L << i6);
/* 1462 */             i4++;
/*      */           }
/* 1464 */         ArrayList localArrayList = new ArrayList(i4 + i2);
/* 1465 */         localHolder.attributes = localArrayList;
/* 1466 */         l4 = l2;
/*      */         Attribute localAttribute;
/* 1467 */         for (int i8 = 0; l4 != 0L; i8++)
/* 1468 */           if ((l4 & 1L << i8) != 0L) {
/* 1469 */             l4 -= (1L << i8);
/* 1470 */             localObject1[i8] += 1;
/*      */ 
/* 1472 */             if (arrayOfLayout[i8] == null) badAttrIndex(i8, paramInt);
/* 1473 */             localAttribute = arrayOfLayout[i8].canonicalInstance();
/* 1474 */             localArrayList.add(localAttribute);
/* 1475 */             i4--;
/*      */           }
/* 1477 */         assert (i4 == 0);
/* 1478 */         for (; i2 > 0; i2--) {
/* 1479 */           i8 = localIntBand4.getInt();
/* 1480 */           localObject1[i8] += 1;
/*      */ 
/* 1482 */           if (arrayOfLayout[i8] == null) badAttrIndex(i8, paramInt);
/* 1483 */           localAttribute = arrayOfLayout[i8].canonicalInstance();
/* 1484 */           localArrayList.add(localAttribute);
/*      */         }
/*      */       }
/*      */     }
/*      */     int i2;
/* 1488 */     localIntBand1.doneDisbursing();
/* 1489 */     localIntBand2.doneDisbursing();
/* 1490 */     localIntBand3.doneDisbursing();
/* 1491 */     localIntBand4.doneDisbursing();
/*      */ 
/* 1500 */     int k = 0;
/*      */     int i1;
/*      */     Attribute.Layout localLayout;
/*      */     Object localObject3;
/*      */     int i3;
/* 1501 */     for (int n = 1; ; n = 0) {
/* 1502 */       for (i1 = 0; i1 < arrayOfLayout.length; i1++) {
/* 1503 */         localLayout = arrayOfLayout[i1];
/* 1504 */         if ((localLayout != null) && 
/* 1505 */           (n == isPredefinedAttr(paramInt, i1)))
/*      */         {
/* 1507 */           i2 = localObject1[i1];
/* 1508 */           if (i2 != 0)
/*      */           {
/* 1510 */             localObject3 = localLayout.getCallables();
/* 1511 */             for (i3 = 0; i3 < localObject3.length; i3++) {
/* 1512 */               assert (localObject3[i3].kind == 10);
/* 1513 */               if (localObject3[i3].flagTest((byte)8))
/* 1514 */                 k++; 
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1517 */       if (n == 0) break;
/*      */     }
/* 1519 */     localIntBand5.expectLength(k);
/* 1520 */     localIntBand5.readFrom(this.in);
/*      */ 
/* 1523 */     for (n = 1; ; n = 0) {
/* 1524 */       for (i1 = 0; i1 < arrayOfLayout.length; i1++) {
/* 1525 */         localLayout = arrayOfLayout[i1];
/* 1526 */         if ((localLayout != null) && 
/* 1527 */           (n == isPredefinedAttr(paramInt, i1)))
/*      */         {
/* 1529 */           i2 = localObject1[i1];
/* 1530 */           localObject3 = (BandStructure.Band[])this.attrBandTable.get(localLayout);
/* 1531 */           if (localLayout == this.attrInnerClassesEmpty)
/*      */           {
/* 1535 */             this.class_InnerClasses_N.expectLength(i2);
/* 1536 */             this.class_InnerClasses_N.readFrom(this.in);
/* 1537 */             i3 = this.class_InnerClasses_N.getIntTotal();
/* 1538 */             this.class_InnerClasses_RC.expectLength(i3);
/* 1539 */             this.class_InnerClasses_RC.readFrom(this.in);
/* 1540 */             this.class_InnerClasses_F.expectLength(i3);
/* 1541 */             this.class_InnerClasses_F.readFrom(this.in);
/*      */ 
/* 1543 */             i3 -= this.class_InnerClasses_F.getIntCount(0);
/* 1544 */             this.class_InnerClasses_outer_RCN.expectLength(i3);
/* 1545 */             this.class_InnerClasses_outer_RCN.readFrom(this.in);
/* 1546 */             this.class_InnerClasses_name_RUN.expectLength(i3);
/* 1547 */             this.class_InnerClasses_name_RUN.readFrom(this.in);
/* 1548 */           } else if (i2 == 0)
/*      */           {
/* 1550 */             for (i3 = 0; i3 < localObject3.length; i3++)
/* 1551 */               localObject3[i3].doneWithUnusedBand();
/*      */           }
/*      */           else
/*      */           {
/* 1555 */             boolean bool2 = localLayout.hasCallables();
/* 1556 */             if (!bool2) {
/* 1557 */               readAttrBands(localLayout.elems, i2, new int[0], (BandStructure.Band[])localObject3);
/*      */             } else {
/* 1559 */               Attribute.Layout.Element[] arrayOfElement = localLayout.getCallables();
/*      */ 
/* 1562 */               int[] arrayOfInt = new int[arrayOfElement.length];
/* 1563 */               arrayOfInt[0] = i2;
/* 1564 */               for (int i5 = 0; i5 < arrayOfElement.length; i5++) {
/* 1565 */                 assert (arrayOfElement[i5].kind == 10);
/* 1566 */                 int i7 = arrayOfInt[i5];
/* 1567 */                 arrayOfInt[i5] = -1;
/* 1568 */                 if (arrayOfElement[i5].flagTest((byte)8))
/* 1569 */                   i7 += localIntBand5.getInt();
/* 1570 */                 readAttrBands(arrayOfElement[i5].body, i7, arrayOfInt, (BandStructure.Band[])localObject3);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/* 1575 */       if (n == 0) break;
/*      */     }
/* 1577 */     localIntBand5.doneDisbursing();
/*      */   }
/*      */ 
/*      */   void badAttrIndex(int paramInt1, int paramInt2) throws IOException {
/* 1581 */     throw new IOException("Unknown attribute index " + paramInt1 + " for " + Constants.ATTR_CONTEXT_NAME[paramInt2] + " attribute");
/*      */   }
/*      */ 
/*      */   void readAttrs(int paramInt, Collection paramCollection)
/*      */     throws IOException
/*      */   {
/* 1588 */     HashSet localHashSet = new HashSet();
/* 1589 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 1590 */     for (Object localObject1 = paramCollection.iterator(); ((Iterator)localObject1).hasNext(); ) {
/* 1591 */       localObject2 = (Attribute.Holder)((Iterator)localObject1).next();
/* 1592 */       if (((Attribute.Holder)localObject2).attributes != null)
/* 1593 */         for (localObject3 = ((Attribute.Holder)localObject2).attributes.listIterator(); ((ListIterator)localObject3).hasNext(); ) {
/* 1594 */           Attribute localAttribute = (Attribute)((ListIterator)localObject3).next();
/* 1595 */           Attribute.Layout localLayout = localAttribute.layout();
/* 1596 */           if (localLayout.bandCount == 0) {
/* 1597 */             if (localLayout == this.attrInnerClassesEmpty)
/*      */             {
/* 1599 */               readLocalInnerClasses((Package.Class)localObject2);
/*      */             }
/*      */ 
/*      */           }
/*      */           else
/*      */           {
/* 1605 */             localHashSet.add(localLayout);
/* 1606 */             int k = (paramInt == 1) && (localLayout == this.attrConstantValue) ? 1 : 0;
/* 1607 */             if (k != 0) setConstantValueIndex((Package.Class.Field)localObject2);
/* 1608 */             if (this.verbose > 2)
/* 1609 */               Utils.log.fine("read " + localAttribute + " in " + localObject2);
/* 1610 */             final BandStructure.Band[] arrayOfBand = (BandStructure.Band[])this.attrBandTable.get(localLayout);
/*      */ 
/* 1612 */             localByteArrayOutputStream.reset();
/* 1613 */             Object localObject4 = localAttribute.unparse(new Attribute.ValueStream() {
/*      */               public int getInt(int paramAnonymousInt) {
/* 1615 */                 return ((BandStructure.IntBand)arrayOfBand[paramAnonymousInt]).getInt();
/*      */               }
/*      */               public ConstantPool.Entry getRef(int paramAnonymousInt) {
/* 1618 */                 return ((BandStructure.CPRefBand)arrayOfBand[paramAnonymousInt]).getRef();
/*      */               }
/*      */               public int decodeBCI(int paramAnonymousInt) {
/* 1621 */                 Code localCode = (Code)this.val$h;
/* 1622 */                 return localCode.decodeBCI(paramAnonymousInt);
/*      */               }
/*      */             }
/* 1627 */             , localByteArrayOutputStream);
/*      */ 
/* 1626 */             ((ListIterator)localObject3).set(localAttribute.addContent(localByteArrayOutputStream.toByteArray(), localObject4));
/* 1627 */             if (k != 0) setConstantValueIndex(null);
/*      */           }
/*      */         }
/*      */     }
/* 1632 */     Object localObject2;
/*      */     Object localObject3;
/* 1632 */     for (localObject1 = localHashSet.iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Attribute.Layout)((Iterator)localObject1).next();
/* 1633 */       if (localObject2 != null) {
/* 1634 */         localObject3 = (BandStructure.Band[])this.attrBandTable.get(localObject2);
/* 1635 */         for (int j = 0; j < localObject3.length; j++) {
/* 1636 */           localObject3[j].doneDisbursing();
/*      */         }
/*      */       }
/*      */     }
/* 1640 */     if (paramInt == 0) {
/* 1641 */       this.class_InnerClasses_N.doneDisbursing();
/* 1642 */       this.class_InnerClasses_RC.doneDisbursing();
/* 1643 */       this.class_InnerClasses_F.doneDisbursing();
/* 1644 */       this.class_InnerClasses_outer_RCN.doneDisbursing();
/* 1645 */       this.class_InnerClasses_name_RUN.doneDisbursing();
/*      */     }
/*      */ 
/* 1648 */     localObject1 = this.attrBands[paramInt];
/* 1649 */     for (int i = 0; i < ((BandStructure.MultiBand)localObject1).size(); i++) {
/* 1650 */       localObject3 = ((BandStructure.MultiBand)localObject1).get(i);
/* 1651 */       if ((localObject3 instanceof BandStructure.MultiBand))
/* 1652 */         ((BandStructure.Band)localObject3).doneDisbursing();
/*      */     }
/* 1654 */     ((BandStructure.MultiBand)localObject1).doneDisbursing();
/*      */   }
/*      */ 
/*      */   private void readAttrBands(Attribute.Layout.Element[] paramArrayOfElement, int paramInt, int[] paramArrayOfInt, BandStructure.Band[] paramArrayOfBand)
/*      */     throws IOException
/*      */   {
/* 1662 */     for (int i = 0; i < paramArrayOfElement.length; i++) {
/* 1663 */       Attribute.Layout.Element localElement = paramArrayOfElement[i];
/* 1664 */       BandStructure.Band localBand = null;
/* 1665 */       if (localElement.hasBand()) {
/* 1666 */         localBand = paramArrayOfBand[localElement.bandIndex];
/* 1667 */         localBand.expectLength(paramInt);
/* 1668 */         localBand.readFrom(this.in);
/*      */       }
/* 1670 */       switch (localElement.kind)
/*      */       {
/*      */       case 5:
/* 1673 */         int j = ((BandStructure.IntBand)localBand).getIntTotal();
/*      */ 
/* 1675 */         readAttrBands(localElement.body, j, paramArrayOfInt, paramArrayOfBand);
/* 1676 */         break;
/*      */       case 7:
/* 1678 */         int k = paramInt;
/* 1679 */         for (int m = 0; m < localElement.body.length; m++)
/*      */         {
/*      */           int n;
/* 1681 */           if (m == localElement.body.length - 1) {
/* 1682 */             n = k;
/*      */           } else {
/* 1684 */             n = 0;
/* 1685 */             int i1 = m;
/*      */ 
/* 1689 */             for (; (m == i1) || ((m < localElement.body.length) && (localElement.body[m].flagTest((byte)8))); 
/* 1689 */               m++) {
/* 1690 */               n += ((BandStructure.IntBand)localBand).getIntCount(localElement.body[m].value);
/*      */             }
/* 1692 */             m--;
/*      */           }
/* 1694 */           k -= n;
/* 1695 */           readAttrBands(localElement.body[m].body, n, paramArrayOfInt, paramArrayOfBand);
/*      */         }
/* 1697 */         if ((!$assertionsDisabled) && (k != 0)) throw new AssertionError();
/*      */         break;
/*      */       case 9:
/* 1700 */         assert (localElement.body.length == 1);
/* 1701 */         assert (localElement.body[0].kind == 10);
/* 1702 */         if (!localElement.flagTest((byte)8))
/*      */         {
/* 1705 */           assert (paramArrayOfInt[localElement.value] >= 0);
/* 1706 */           paramArrayOfInt[localElement.value] += paramInt; } break;
/*      */       case 10:
/* 1710 */         if (!$assertionsDisabled) throw new AssertionError();
/*      */         break;
/*      */       case 6:
/*      */       case 8:
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   void readByteCodes()
/*      */     throws IOException
/*      */   {
/* 1743 */     this.bc_codes.elementCountForDebug = this.allCodes.length;
/* 1744 */     this.bc_codes.setInputStreamFrom(this.in);
/* 1745 */     readByteCodeOps();
/* 1746 */     this.bc_codes.doneDisbursing();
/*      */ 
/* 1749 */     BandStructure.Band[] arrayOfBand = { this.bc_case_value, this.bc_byte, this.bc_short, this.bc_local, this.bc_label, this.bc_intref, this.bc_floatref, this.bc_longref, this.bc_doubleref, this.bc_stringref, this.bc_classref, this.bc_fieldref, this.bc_methodref, this.bc_imethodref, this.bc_thisfield, this.bc_superfield, this.bc_thismethod, this.bc_supermethod, this.bc_initref, this.bc_escref, this.bc_escrefsize, this.bc_escsize };
/*      */ 
/* 1762 */     for (int i = 0; i < arrayOfBand.length; i++) {
/* 1763 */       arrayOfBand[i].readFrom(this.in);
/*      */     }
/* 1765 */     this.bc_escbyte.expectLength(this.bc_escsize.getIntTotal());
/* 1766 */     this.bc_escbyte.readFrom(this.in);
/*      */ 
/* 1768 */     expandByteCodeOps();
/*      */ 
/* 1771 */     this.bc_case_count.doneDisbursing();
/* 1772 */     for (i = 0; i < arrayOfBand.length; i++) {
/* 1773 */       arrayOfBand[i].doneDisbursing();
/*      */     }
/* 1775 */     this.bc_escbyte.doneDisbursing();
/* 1776 */     this.bc_bands.doneDisbursing();
/*      */ 
/* 1780 */     readAttrs(3, this.codesWithFlags);
/*      */ 
/* 1782 */     fixupCodeHandlers();
/*      */ 
/* 1784 */     this.code_bands.doneDisbursing();
/* 1785 */     this.class_bands.doneDisbursing();
/*      */   }
/*      */ 
/*      */   private void readByteCodeOps() throws IOException
/*      */   {
/* 1790 */     byte[] arrayOfByte = new byte[4096];
/*      */ 
/* 1792 */     ArrayList localArrayList = new ArrayList();
/*      */     Object localObject;
/*      */     int j;
/*      */     int k;
/* 1793 */     label529: for (int i = 0; i < this.allCodes.length; i++) {
/* 1794 */       localObject = this.allCodes[i];
/*      */ 
/* 1796 */       for (j = 0; ; j++) {
/* 1797 */         k = this.bc_codes.getByte();
/* 1798 */         if (j + 10 > arrayOfByte.length) arrayOfByte = realloc(arrayOfByte);
/* 1799 */         arrayOfByte[j] = ((byte)k);
/* 1800 */         int m = 0;
/* 1801 */         if (k == 196) {
/* 1802 */           k = this.bc_codes.getByte();
/* 1803 */           arrayOfByte[(++j)] = ((byte)k);
/* 1804 */           m = 1;
/*      */         }
/* 1806 */         assert (k == (0xFF & k));
/*      */ 
/* 1808 */         switch (k) {
/*      */         case 170:
/*      */         case 171:
/* 1811 */           this.bc_case_count.expectMoreLength(1);
/* 1812 */           localArrayList.add(Integer.valueOf(k));
/* 1813 */           break;
/*      */         case 132:
/* 1815 */           this.bc_local.expectMoreLength(1);
/* 1816 */           if (m != 0)
/* 1817 */             this.bc_short.expectMoreLength(1);
/*      */           else
/* 1819 */             this.bc_byte.expectMoreLength(1);
/* 1820 */           break;
/*      */         case 17:
/* 1822 */           this.bc_short.expectMoreLength(1);
/* 1823 */           break;
/*      */         case 16:
/* 1825 */           this.bc_byte.expectMoreLength(1);
/* 1826 */           break;
/*      */         case 188:
/* 1828 */           this.bc_byte.expectMoreLength(1);
/* 1829 */           break;
/*      */         case 197:
/* 1831 */           assert (getCPRefOpBand(k) == this.bc_classref);
/* 1832 */           this.bc_classref.expectMoreLength(1);
/* 1833 */           this.bc_byte.expectMoreLength(1);
/* 1834 */           break;
/*      */         case 253:
/* 1836 */           this.bc_escrefsize.expectMoreLength(1);
/* 1837 */           this.bc_escref.expectMoreLength(1);
/* 1838 */           break;
/*      */         case 254:
/* 1840 */           this.bc_escsize.expectMoreLength(1);
/*      */ 
/* 1842 */           break;
/*      */         default:
/* 1844 */           if (Instruction.isInvokeInitOp(k)) {
/* 1845 */             this.bc_initref.expectMoreLength(1);
/*      */           }
/*      */           else
/*      */           {
/*      */             BandStructure.CPRefBand localCPRefBand;
/* 1848 */             if (Instruction.isSelfLinkerOp(k)) {
/* 1849 */               localCPRefBand = selfOpRefBand(k);
/* 1850 */               localCPRefBand.expectMoreLength(1);
/*      */             }
/* 1853 */             else if (Instruction.isBranchOp(k)) {
/* 1854 */               this.bc_label.expectMoreLength(1);
/*      */             }
/* 1857 */             else if (Instruction.isCPRefOp(k)) {
/* 1858 */               localCPRefBand = getCPRefOpBand(k);
/* 1859 */               localCPRefBand.expectMoreLength(1);
/* 1860 */               if ((!$assertionsDisabled) && (k == 197)) throw new AssertionError();
/*      */ 
/*      */             }
/* 1863 */             else if (Instruction.isLocalSlotOp(k)) {
/* 1864 */               this.bc_local.expectMoreLength(1); } 
/* 1865 */           }break;
/*      */         case 255:
/* 1871 */           ((Code)localObject).bytes = realloc(arrayOfByte, j);
/* 1872 */           break label529;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1879 */     this.bc_case_count.readFrom(this.in);
/* 1880 */     for (Iterator localIterator = localArrayList.iterator(); localIterator.hasNext(); ) { localObject = (Integer)localIterator.next();
/* 1881 */       j = ((Integer)localObject).intValue();
/* 1882 */       k = this.bc_case_count.getInt();
/* 1883 */       this.bc_label.expectMoreLength(1 + k);
/* 1884 */       this.bc_case_value.expectMoreLength(j == 170 ? 1 : k);
/*      */     }
/* 1886 */     this.bc_case_count.resetForSecondPass();
/*      */   }
/*      */ 
/*      */   private void expandByteCodeOps() throws IOException
/*      */   {
/* 1891 */     byte[] arrayOfByte1 = new byte[4096];
/*      */ 
/* 1893 */     int[] arrayOfInt1 = new int[4096];
/*      */ 
/* 1895 */     int[] arrayOfInt2 = new int[1024];
/*      */ 
/* 1897 */     Fixups localFixups = new Fixups();
/*      */ 
/* 1899 */     for (int i = 0; i < this.allCodes.length; i++) {
/* 1900 */       Code localCode = this.allCodes[i];
/* 1901 */       byte[] arrayOfByte2 = localCode.bytes;
/* 1902 */       localCode.bytes = null;
/*      */ 
/* 1904 */       Package.Class localClass = localCode.thisClass();
/*      */ 
/* 1906 */       Object localObject1 = (Set)this.ldcRefMap.get(localClass);
/* 1907 */       if (localObject1 == null) {
/* 1908 */         this.ldcRefMap.put(localClass, localObject1 = new HashSet());
/*      */       }
/* 1910 */       ConstantPool.ClassEntry localClassEntry1 = localClass.thisClass;
/* 1911 */       ConstantPool.ClassEntry localClassEntry2 = localClass.superClass;
/* 1912 */       ConstantPool.ClassEntry localClassEntry3 = null;
/*      */ 
/* 1914 */       int j = 0;
/* 1915 */       int k = 0;
/* 1916 */       int m = 0;
/* 1917 */       int n = 0;
/* 1918 */       localFixups.clear();
/*      */       int i3;
/*      */       int i6;
/*      */       int i9;
/* 1919 */       for (int i1 = 0; i1 < arrayOfByte2.length; i1++) {
/* 1920 */         i2 = Instruction.getByte(arrayOfByte2, i1);
/* 1921 */         i3 = j;
/* 1922 */         arrayOfInt1[(k++)] = i3;
/* 1923 */         if (j + 10 > arrayOfByte1.length) arrayOfByte1 = realloc(arrayOfByte1);
/* 1924 */         if (k + 10 > arrayOfInt1.length) arrayOfInt1 = realloc(arrayOfInt1);
/* 1925 */         if (m + 10 > arrayOfInt2.length) arrayOfInt2 = realloc(arrayOfInt2);
/* 1926 */         int i4 = 0;
/* 1927 */         if (i2 == 196) {
/* 1928 */           arrayOfByte1[(j++)] = ((byte)i2);
/* 1929 */           i2 = Instruction.getByte(arrayOfByte2, ++i1);
/* 1930 */           i4 = 1;
/*      */         }
/*      */         int i5;
/*      */         int i10;
/* 1932 */         switch (i2)
/*      */         {
/*      */         case 170:
/*      */         case 171:
/* 1936 */           i5 = this.bc_case_count.getInt();
/* 1937 */           while (j + 30 + i5 * 8 > arrayOfByte1.length)
/* 1938 */             arrayOfByte1 = realloc(arrayOfByte1);
/* 1939 */           arrayOfByte1[(j++)] = ((byte)i2);
/*      */ 
/* 1941 */           Arrays.fill(arrayOfByte1, j, j + 30, (byte)0);
/* 1942 */           Instruction.Switch localSwitch2 = (Instruction.Switch)Instruction.at(arrayOfByte1, i3);
/*      */ 
/* 1945 */           localSwitch2.setCaseCount(i5);
/* 1946 */           if (i2 == 170)
/* 1947 */             localSwitch2.setCaseValue(0, this.bc_case_value.getInt());
/*      */           else {
/* 1949 */             for (i10 = 0; i10 < i5; i10++) {
/* 1950 */               localSwitch2.setCaseValue(i10, this.bc_case_value.getInt());
/*      */             }
/*      */           }
/*      */ 
/* 1954 */           arrayOfInt2[(m++)] = i3;
/* 1955 */           j = localSwitch2.getNextPC();
/* 1956 */           break;
/*      */         case 132:
/* 1960 */           arrayOfByte1[(j++)] = ((byte)i2);
/* 1961 */           i5 = this.bc_local.getInt();
/*      */           int i7;
/* 1963 */           if (i4 != 0) {
/* 1964 */             i7 = this.bc_short.getInt();
/* 1965 */             Instruction.setShort(arrayOfByte1, j, i5); j += 2;
/* 1966 */             Instruction.setShort(arrayOfByte1, j, i7); j += 2;
/*      */           } else {
/* 1968 */             i7 = (byte)this.bc_byte.getByte();
/* 1969 */             arrayOfByte1[(j++)] = ((byte)i5);
/* 1970 */             arrayOfByte1[(j++)] = ((byte)i7);
/*      */           }
/* 1972 */           break;
/*      */         case 17:
/* 1976 */           i5 = this.bc_short.getInt();
/* 1977 */           arrayOfByte1[(j++)] = ((byte)i2);
/* 1978 */           Instruction.setShort(arrayOfByte1, j, i5); j += 2;
/* 1979 */           break;
/*      */         case 16:
/*      */         case 188:
/* 1984 */           i5 = this.bc_byte.getByte();
/* 1985 */           arrayOfByte1[(j++)] = ((byte)i2);
/* 1986 */           arrayOfByte1[(j++)] = ((byte)i5);
/* 1987 */           break;
/*      */         case 253:
/* 1992 */           n = 1;
/* 1993 */           i5 = this.bc_escrefsize.getInt();
/* 1994 */           ConstantPool.Entry localEntry = this.bc_escref.getRef();
/* 1995 */           if (i5 == 1) ((Set)localObject1).add(localEntry);
/*      */ 
/* 1997 */           switch (i5) { case 1:
/* 1998 */             i10 = 1; break;
/*      */           case 2:
/* 1999 */             i10 = 0; break;
/*      */           default:
/* 2000 */             if (!$assertionsDisabled) throw new AssertionError(); i10 = 0;
/*      */           }
/* 2002 */           localFixups.add(j, i10, localEntry);
/*      */           int tmp755_754 = 0; arrayOfByte1[(j + 1)] = tmp755_754; arrayOfByte1[(j + 0)] = tmp755_754;
/* 2004 */           j += i5;
/*      */ 
/* 2006 */           break;
/*      */         case 254:
/* 2010 */           n = 1;
/* 2011 */           i5 = this.bc_escsize.getInt();
/* 2012 */           while (j + i5 > arrayOfByte1.length)
/* 2013 */             arrayOfByte1 = realloc(arrayOfByte1);
/* 2014 */           while (i5-- > 0) {
/* 2015 */             arrayOfByte1[(j++)] = ((byte)this.bc_escbyte.getByte());
/*      */           }
/*      */ 
/* 2018 */           break;
/*      */         default:
/*      */           int i8;
/*      */           int i12;
/* 2020 */           if (Instruction.isInvokeInitOp(i2)) {
/* 2021 */             i5 = i2 - 230;
/* 2022 */             i8 = 183;
/*      */             ConstantPool.ClassEntry localClassEntry4;
/* 2024 */             switch (i5) {
/*      */             case 0:
/* 2026 */               localClassEntry4 = localClassEntry1; break;
/*      */             case 1:
/* 2028 */               localClassEntry4 = localClassEntry2; break;
/*      */             default:
/* 2030 */               assert (i5 == 2);
/* 2031 */               localClassEntry4 = localClassEntry3;
/*      */             }
/* 2033 */             arrayOfByte1[(j++)] = ((byte)i8);
/* 2034 */             i12 = this.bc_initref.getInt();
/*      */ 
/* 2036 */             ConstantPool.MemberEntry localMemberEntry1 = this.pkg.cp.getOverloadingForIndex((byte)10, localClassEntry4, "<init>", i12);
/* 2037 */             localFixups.add(j, 0, localMemberEntry1);
/*      */             int tmp975_974 = 0; arrayOfByte1[(j + 1)] = tmp975_974; arrayOfByte1[(j + 0)] = tmp975_974;
/* 2039 */             j += 2;
/* 2040 */             if ((!$assertionsDisabled) && (Instruction.opLength(i8) != j - i3)) throw new AssertionError();
/*      */           }
/*      */           else
/*      */           {
/*      */             int i11;
/*      */             int i13;
/* 2043 */             if (Instruction.isSelfLinkerOp(i2)) {
/* 2044 */               i5 = i2 - 202;
/* 2045 */               i8 = i5 >= 14 ? 1 : 0;
/* 2046 */               if (i8 != 0) i5 -= 14;
/* 2047 */               i11 = i5 >= 7 ? 1 : 0;
/* 2048 */               if (i11 != 0) i5 -= 7;
/* 2049 */               i12 = 178 + i5;
/* 2050 */               i13 = Instruction.isFieldOp(i12);
/*      */ 
/* 2052 */               ConstantPool.ClassEntry localClassEntry5 = i8 != 0 ? localClassEntry2 : localClassEntry1;
/*      */               BandStructure.CPRefBand localCPRefBand2;
/*      */               ConstantPool.Index localIndex;
/* 2054 */               if (i13 != 0) {
/* 2055 */                 localCPRefBand2 = i8 != 0 ? this.bc_superfield : this.bc_thisfield;
/* 2056 */                 localIndex = this.pkg.cp.getMemberIndex((byte)9, localClassEntry5);
/*      */               } else {
/* 2058 */                 localCPRefBand2 = i8 != 0 ? this.bc_supermethod : this.bc_thismethod;
/* 2059 */                 localIndex = this.pkg.cp.getMemberIndex((byte)10, localClassEntry5);
/*      */               }
/* 2061 */               assert (localCPRefBand2 == selfOpRefBand(i2));
/* 2062 */               ConstantPool.MemberEntry localMemberEntry2 = (ConstantPool.MemberEntry)localCPRefBand2.getRef(localIndex);
/* 2063 */               if (i11 != 0) {
/* 2064 */                 arrayOfByte1[(j++)] = 42;
/* 2065 */                 i3 = j;
/*      */ 
/* 2067 */                 arrayOfInt1[(k++)] = i3;
/*      */               }
/* 2069 */               arrayOfByte1[(j++)] = ((byte)i12);
/* 2070 */               localFixups.add(j, 0, localMemberEntry2);
/*      */               int tmp1268_1267 = 0; arrayOfByte1[(j + 1)] = tmp1268_1267; arrayOfByte1[(j + 0)] = tmp1268_1267;
/* 2072 */               j += 2;
/* 2073 */               if ((!$assertionsDisabled) && (Instruction.opLength(i12) != j - i3)) throw new AssertionError(); 
/*      */             }
/*      */             else
/*      */             {
/* 2076 */               if (Instruction.isBranchOp(i2)) {
/* 2077 */                 arrayOfByte1[(j++)] = ((byte)i2);
/* 2078 */                 assert (i4 == 0);
/* 2079 */                 i5 = i3 + Instruction.opLength(i2);
/*      */ 
/* 2081 */                 arrayOfInt2[(m++)] = i3;
/*      */ 
/* 2083 */                 while (j < i5) arrayOfByte1[(j++)] = 0;
/*      */               }
/*      */ 
/* 2086 */               if (Instruction.isCPRefOp(i2)) {
/* 2087 */                 BandStructure.CPRefBand localCPRefBand1 = getCPRefOpBand(i2);
/* 2088 */                 Object localObject2 = localCPRefBand1.getRef();
/* 2089 */                 if (localObject2 == null) {
/* 2090 */                   if (localCPRefBand1 == this.bc_classref)
/*      */                   {
/* 2092 */                     localObject2 = localClassEntry1;
/*      */                   }
/* 2094 */                   else if (!$assertionsDisabled) throw new AssertionError();
/*      */                 }
/*      */ 
/* 2097 */                 i11 = i2;
/* 2098 */                 i12 = 2;
/* 2099 */                 switch (i2) {
/*      */                 case 18:
/*      */                 case 233:
/*      */                 case 234:
/*      */                 case 235:
/* 2104 */                   i11 = 18;
/* 2105 */                   i12 = 1;
/* 2106 */                   ((Set)localObject1).add(localObject2);
/* 2107 */                   break;
/*      */                 case 19:
/*      */                 case 236:
/*      */                 case 237:
/*      */                 case 238:
/* 2112 */                   i11 = 19;
/* 2113 */                   break;
/*      */                 case 20:
/*      */                 case 239:
/* 2116 */                   i11 = 20;
/* 2117 */                   break;
/*      */                 case 187:
/* 2119 */                   localClassEntry3 = (ConstantPool.ClassEntry)localObject2;
/*      */                 }
/*      */ 
/* 2122 */                 arrayOfByte1[(j++)] = ((byte)i11);
/*      */ 
/* 2124 */                 switch (i12) { case 1:
/* 2125 */                   i13 = 1; break;
/*      */                 case 2:
/* 2126 */                   i13 = 0; break;
/*      */                 default:
/* 2127 */                   if (!$assertionsDisabled) throw new AssertionError(); i13 = 0;
/*      */                 }
/* 2129 */                 localFixups.add(j, i13, (ConstantPool.Entry)localObject2);
/*      */                 int tmp1671_1670 = 0; arrayOfByte1[(j + 1)] = tmp1671_1670; arrayOfByte1[(j + 0)] = tmp1671_1670;
/* 2131 */                 j += i12;
/*      */                 int i14;
/* 2132 */                 if (i11 == 197)
/*      */                 {
/* 2134 */                   i14 = this.bc_byte.getByte();
/* 2135 */                   arrayOfByte1[(j++)] = ((byte)i14);
/* 2136 */                 } else if (i11 == 185) {
/* 2137 */                   i14 = ((ConstantPool.MemberEntry)localObject2).descRef.typeRef.computeSize(true);
/* 2138 */                   arrayOfByte1[(j++)] = ((byte)(1 + i14));
/* 2139 */                   arrayOfByte1[(j++)] = 0;
/*      */                 }
/* 2141 */                 if ((!$assertionsDisabled) && (Instruction.opLength(i11) != j - i3)) throw new AssertionError();
/*      */ 
/*      */               }
/* 2144 */               else if (Instruction.isLocalSlotOp(i2)) {
/* 2145 */                 arrayOfByte1[(j++)] = ((byte)i2);
/* 2146 */                 i6 = this.bc_local.getInt();
/* 2147 */                 if (i4 != 0) {
/* 2148 */                   Instruction.setShort(arrayOfByte1, j, i6);
/* 2149 */                   j += 2;
/* 2150 */                   if (i2 == 132) {
/* 2151 */                     i9 = this.bc_short.getInt();
/* 2152 */                     Instruction.setShort(arrayOfByte1, j, i9);
/* 2153 */                     j += 2;
/*      */                   }
/*      */                 } else {
/* 2156 */                   Instruction.setByte(arrayOfByte1, j, i6);
/* 2157 */                   j++;
/* 2158 */                   if (i2 == 132) {
/* 2159 */                     i9 = this.bc_byte.getByte();
/* 2160 */                     Instruction.setByte(arrayOfByte1, j, i9);
/* 2161 */                     j++;
/*      */                   }
/*      */                 }
/* 2164 */                 if ((!$assertionsDisabled) && (Instruction.opLength(i2) != j - i3)) throw new AssertionError();
/*      */               }
/*      */               else
/*      */               {
/* 2168 */                 if (i2 >= 202) {
/* 2169 */                   Utils.log.warning("unrecognized bytescode " + i2 + " " + Instruction.byteName(i2));
/*      */                 }
/* 2171 */                 assert (i2 < 202);
/* 2172 */                 arrayOfByte1[(j++)] = ((byte)i2);
/* 2173 */                 assert (Instruction.opLength(i2) == j - i3);
/*      */               }
/*      */             }
/*      */           }
/*      */           break;
/*      */         }
/*      */       }
/* 2178 */       localCode.setBytes(realloc(arrayOfByte1, j));
/* 2179 */       localCode.setInstructionMap(arrayOfInt1, k);
/*      */ 
/* 2181 */       Instruction localInstruction = null;
/* 2182 */       for (int i2 = 0; i2 < m; i2++) {
/* 2183 */         i3 = arrayOfInt2[i2];
/*      */ 
/* 2185 */         localInstruction = Instruction.at(localCode.bytes, i3, localInstruction);
/* 2186 */         if ((localInstruction instanceof Instruction.Switch)) {
/* 2187 */           Instruction.Switch localSwitch1 = (Instruction.Switch)localInstruction;
/* 2188 */           localSwitch1.setDefaultLabel(getLabel(this.bc_label, localCode, i3));
/* 2189 */           i6 = localSwitch1.getCaseCount();
/* 2190 */           for (i9 = 0; i9 < i6; i9++)
/* 2191 */             localSwitch1.setCaseLabel(i9, getLabel(this.bc_label, localCode, i3));
/*      */         }
/*      */         else {
/* 2194 */           localInstruction.setBranchLabel(getLabel(this.bc_label, localCode, i3));
/*      */         }
/*      */       }
/* 2197 */       if (localFixups.size() > 0) {
/* 2198 */         if (this.verbose > 2)
/* 2199 */           Utils.log.fine("Fixups in code: " + localFixups);
/* 2200 */         localCode.addFixups(localFixups);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   static class LimitedBuffer extends BufferedInputStream
/*      */   {
/*      */     long served;
/*      */     int servedPos;
/*      */     long limit;
/*      */     long buffered;
/*      */ 
/*      */     public boolean atLimit()
/*      */     {
/*   87 */       boolean bool = getBytesServed() == this.limit;
/*   88 */       assert ((!bool) || (this.limit == this.buffered));
/*   89 */       return bool;
/*      */     }
/*      */     public long getBytesServed() {
/*   92 */       return this.served + (this.pos - this.servedPos);
/*      */     }
/*      */     public void setReadLimit(long paramLong) {
/*   95 */       if (paramLong == -1L)
/*   96 */         this.limit = -1L;
/*      */       else
/*   98 */         this.limit = (getBytesServed() + paramLong); 
/*      */     }
/*      */ 
/*  101 */     public long getReadLimit() { if (this.limit == -1L) {
/*  102 */         return this.limit;
/*      */       }
/*  104 */       return this.limit - getBytesServed(); }
/*      */ 
/*      */     public int read() throws IOException {
/*  107 */       if (this.pos < this.count)
/*      */       {
/*  109 */         return this.buf[(this.pos++)] & 0xFF;
/*      */       }
/*  111 */       this.served += this.pos - this.servedPos;
/*  112 */       int i = super.read();
/*  113 */       this.servedPos = this.pos;
/*  114 */       if (i >= 0) this.served += 1L;
/*  115 */       assert ((this.served <= this.limit) || (this.limit == -1L));
/*  116 */       return i;
/*      */     }
/*      */     public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/*  119 */       this.served += this.pos - this.servedPos;
/*  120 */       int i = super.read(paramArrayOfByte, paramInt1, paramInt2);
/*  121 */       this.servedPos = this.pos;
/*  122 */       if (i >= 0) this.served += i;
/*  123 */       assert ((this.served <= this.limit) || (this.limit == -1L));
/*  124 */       return i;
/*      */     }
/*      */     public long skip(long paramLong) throws IOException {
/*  127 */       throw new RuntimeException("no skipping");
/*      */     }
/*      */     LimitedBuffer(InputStream paramInputStream) {
/*  130 */       super(16384);
/*  131 */       this.servedPos = this.pos;
/*  132 */       this.in = new FilterInputStream(paramInputStream) {
/*      */         public int read() throws IOException {
/*  134 */           if (PackageReader.LimitedBuffer.this.buffered == PackageReader.LimitedBuffer.this.limit)
/*  135 */             return -1;
/*  136 */           PackageReader.LimitedBuffer.this.buffered += 1L;
/*  137 */           return super.read();
/*      */         }
/*      */         public int read(byte[] paramAnonymousArrayOfByte, int paramAnonymousInt1, int paramAnonymousInt2) throws IOException {
/*  140 */           if (PackageReader.LimitedBuffer.this.buffered == PackageReader.LimitedBuffer.this.limit)
/*  141 */             return -1;
/*  142 */           if (PackageReader.LimitedBuffer.this.limit != -1L) {
/*  143 */             long l = PackageReader.LimitedBuffer.this.limit - PackageReader.LimitedBuffer.this.buffered;
/*  144 */             if (paramAnonymousInt2 > l)
/*  145 */               paramAnonymousInt2 = (int)l;
/*      */           }
/*  147 */           int i = super.read(paramAnonymousArrayOfByte, paramAnonymousInt1, paramAnonymousInt2);
/*  148 */           if (i >= 0) PackageReader.LimitedBuffer.this.buffered += i;
/*  149 */           return i;
/*      */         }
/*      */       };
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.PackageReader
 * JD-Core Version:    0.6.2
 */