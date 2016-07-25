/*      */ package com.sun.java.util.jar.pack;
/*      */ 
/*      */ import java.io.BufferedOutputStream;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.EOFException;
/*      */ import java.io.File;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.FilterInputStream;
/*      */ import java.io.FilterOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.reflect.Array;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ 
/*      */ abstract class BandStructure
/*      */ {
/*      */   static final int MAX_EFFORT = 9;
/*      */   static final int MIN_EFFORT = 1;
/*      */   static final int DEFAULT_EFFORT = 5;
/*   63 */   PropMap p200 = Utils.currentPropMap();
/*      */ 
/*   65 */   int verbose = this.p200.getInteger("com.sun.java.util.jar.pack.verbose");
/*   66 */   int effort = this.p200.getInteger("pack.effort");
/*      */   boolean optDumpBands;
/*      */   boolean optDebugBands;
/*      */   boolean optVaryCodings;
/*      */   boolean optBigStrings;
/*      */   private int packageMajver;
/*      */   private final boolean isReader;
/*      */   static final Coding BYTE1;
/*      */   static final Coding CHAR3;
/*      */   static final Coding BCI5;
/*      */   static final Coding BRANCH5;
/*      */   static final Coding UNSIGNED5;
/*      */   static final Coding UDELTA5;
/*      */   static final Coding SIGNED5;
/*      */   static final Coding DELTA5;
/*      */   static final Coding MDELTA5;
/*      */   private static final Coding[] basicCodings;
/*      */   private static final Map<Coding, Integer> basicCodingIndexes;
/*      */   protected byte[] bandHeaderBytes;
/*      */   protected int bandHeaderBytePos;
/*      */   protected int bandHeaderBytePos0;
/*      */   static final int SHORT_BAND_HEURISTIC = 100;
/*      */   public static final int NO_PHASE = 0;
/*      */   public static final int COLLECT_PHASE = 1;
/*      */   public static final int FROZEN_PHASE = 3;
/*      */   public static final int WRITE_PHASE = 5;
/*      */   public static final int EXPECT_PHASE = 2;
/*      */   public static final int READ_PHASE = 4;
/*      */   public static final int DISBURSE_PHASE = 6;
/*      */   public static final int DONE_PHASE = 8;
/*      */   private final List<CPRefBand> allKQBands;
/*      */   private List<Object[]> needPredefIndex;
/*      */   private CodingChooser codingChooser;
/*      */   static final byte[] defaultMetaCoding;
/*      */   static final byte[] noMetaCoding;
/*      */   ByteCounter outputCounter;
/*      */   protected int archiveOptions;
/*      */   protected long archiveSize0;
/*      */   protected long archiveSize1;
/*      */   protected int archiveNextCount;
/*      */   static final int AH_LENGTH_0 = 3;
/*      */   static final int AH_ARCHIVE_SIZE_HI = 0;
/*      */   static final int AH_ARCHIVE_SIZE_LO = 1;
/*      */   static final int AH_LENGTH_S = 2;
/*      */   static final int AH_LENGTH = 26;
/*      */   static final int AH_FILE_HEADER_LEN = 5;
/*      */   static final int AH_SPECIAL_FORMAT_LEN = 2;
/*      */   static final int AH_CP_NUMBER_LEN = 4;
/*      */   static final int AH_LENGTH_MIN = 15;
/*      */   static final int AB_FLAGS_HI = 0;
/*      */   static final int AB_FLAGS_LO = 1;
/*      */   static final int AB_ATTR_COUNT = 2;
/*      */   static final int AB_ATTR_INDEXES = 3;
/*      */   static final int AB_ATTR_CALLS = 4;
/*      */   private static final boolean NULL_IS_OK = true;
/*      */   MultiBand all_bands;
/*      */   ByteBand archive_magic;
/*      */   IntBand archive_header_0;
/*      */   IntBand archive_header_S;
/*      */   IntBand archive_header_1;
/*      */   ByteBand band_headers;
/*      */   MultiBand cp_bands;
/*      */   IntBand cp_Utf8_prefix;
/*      */   IntBand cp_Utf8_suffix;
/*      */   IntBand cp_Utf8_chars;
/*      */   IntBand cp_Utf8_big_suffix;
/*      */   MultiBand cp_Utf8_big_chars;
/*      */   IntBand cp_Int;
/*      */   IntBand cp_Float;
/*      */   IntBand cp_Long_hi;
/*      */   IntBand cp_Long_lo;
/*      */   IntBand cp_Double_hi;
/*      */   IntBand cp_Double_lo;
/*      */   CPRefBand cp_String;
/*      */   CPRefBand cp_Class;
/*      */   CPRefBand cp_Signature_form;
/*      */   CPRefBand cp_Signature_classes;
/*      */   CPRefBand cp_Descr_name;
/*      */   CPRefBand cp_Descr_type;
/*      */   CPRefBand cp_Field_class;
/*      */   CPRefBand cp_Field_desc;
/*      */   CPRefBand cp_Method_class;
/*      */   CPRefBand cp_Method_desc;
/*      */   CPRefBand cp_Imethod_class;
/*      */   CPRefBand cp_Imethod_desc;
/*      */   MultiBand attr_definition_bands;
/*      */   ByteBand attr_definition_headers;
/*      */   CPRefBand attr_definition_name;
/*      */   CPRefBand attr_definition_layout;
/*      */   MultiBand ic_bands;
/*      */   CPRefBand ic_this_class;
/*      */   IntBand ic_flags;
/*      */   CPRefBand ic_outer_class;
/*      */   CPRefBand ic_name;
/*      */   MultiBand class_bands;
/*      */   CPRefBand class_this;
/*      */   CPRefBand class_super;
/*      */   IntBand class_interface_count;
/*      */   CPRefBand class_interface;
/*      */   IntBand class_field_count;
/*      */   IntBand class_method_count;
/*      */   CPRefBand field_descr;
/*      */   MultiBand field_attr_bands;
/*      */   IntBand field_flags_hi;
/*      */   IntBand field_flags_lo;
/*      */   IntBand field_attr_count;
/*      */   IntBand field_attr_indexes;
/*      */   IntBand field_attr_calls;
/*      */   CPRefBand field_ConstantValue_KQ;
/*      */   CPRefBand field_Signature_RS;
/*      */   MultiBand field_metadata_bands;
/*      */   CPRefBand method_descr;
/*      */   MultiBand method_attr_bands;
/*      */   IntBand method_flags_hi;
/*      */   IntBand method_flags_lo;
/*      */   IntBand method_attr_count;
/*      */   IntBand method_attr_indexes;
/*      */   IntBand method_attr_calls;
/*      */   IntBand method_Exceptions_N;
/*      */   CPRefBand method_Exceptions_RC;
/*      */   CPRefBand method_Signature_RS;
/*      */   MultiBand method_metadata_bands;
/*      */   MultiBand class_attr_bands;
/*      */   IntBand class_flags_hi;
/*      */   IntBand class_flags_lo;
/*      */   IntBand class_attr_count;
/*      */   IntBand class_attr_indexes;
/*      */   IntBand class_attr_calls;
/*      */   CPRefBand class_SourceFile_RUN;
/*      */   CPRefBand class_EnclosingMethod_RC;
/*      */   CPRefBand class_EnclosingMethod_RDN;
/*      */   CPRefBand class_Signature_RS;
/*      */   MultiBand class_metadata_bands;
/*      */   IntBand class_InnerClasses_N;
/*      */   CPRefBand class_InnerClasses_RC;
/*      */   IntBand class_InnerClasses_F;
/*      */   CPRefBand class_InnerClasses_outer_RCN;
/*      */   CPRefBand class_InnerClasses_name_RUN;
/*      */   IntBand class_ClassFile_version_minor_H;
/*      */   IntBand class_ClassFile_version_major_H;
/*      */   MultiBand code_bands;
/*      */   ByteBand code_headers;
/*      */   IntBand code_max_stack;
/*      */   IntBand code_max_na_locals;
/*      */   IntBand code_handler_count;
/*      */   IntBand code_handler_start_P;
/*      */   IntBand code_handler_end_PO;
/*      */   IntBand code_handler_catch_PO;
/*      */   CPRefBand code_handler_class_RCN;
/*      */   MultiBand code_attr_bands;
/*      */   IntBand code_flags_hi;
/*      */   IntBand code_flags_lo;
/*      */   IntBand code_attr_count;
/*      */   IntBand code_attr_indexes;
/*      */   IntBand code_attr_calls;
/*      */   MultiBand stackmap_bands;
/*      */   IntBand code_StackMapTable_N;
/*      */   IntBand code_StackMapTable_frame_T;
/*      */   IntBand code_StackMapTable_local_N;
/*      */   IntBand code_StackMapTable_stack_N;
/*      */   IntBand code_StackMapTable_offset;
/*      */   IntBand code_StackMapTable_T;
/*      */   CPRefBand code_StackMapTable_RC;
/*      */   IntBand code_StackMapTable_P;
/*      */   IntBand code_LineNumberTable_N;
/*      */   IntBand code_LineNumberTable_bci_P;
/*      */   IntBand code_LineNumberTable_line;
/*      */   IntBand code_LocalVariableTable_N;
/*      */   IntBand code_LocalVariableTable_bci_P;
/*      */   IntBand code_LocalVariableTable_span_O;
/*      */   CPRefBand code_LocalVariableTable_name_RU;
/*      */   CPRefBand code_LocalVariableTable_type_RS;
/*      */   IntBand code_LocalVariableTable_slot;
/*      */   IntBand code_LocalVariableTypeTable_N;
/*      */   IntBand code_LocalVariableTypeTable_bci_P;
/*      */   IntBand code_LocalVariableTypeTable_span_O;
/*      */   CPRefBand code_LocalVariableTypeTable_name_RU;
/*      */   CPRefBand code_LocalVariableTypeTable_type_RS;
/*      */   IntBand code_LocalVariableTypeTable_slot;
/*      */   MultiBand bc_bands;
/*      */   ByteBand bc_codes;
/*      */   IntBand bc_case_count;
/*      */   IntBand bc_case_value;
/*      */   ByteBand bc_byte;
/*      */   IntBand bc_short;
/*      */   IntBand bc_local;
/*      */   IntBand bc_label;
/*      */   CPRefBand bc_intref;
/*      */   CPRefBand bc_floatref;
/*      */   CPRefBand bc_longref;
/*      */   CPRefBand bc_doubleref;
/*      */   CPRefBand bc_stringref;
/*      */   CPRefBand bc_classref;
/*      */   CPRefBand bc_fieldref;
/*      */   CPRefBand bc_methodref;
/*      */   CPRefBand bc_imethodref;
/*      */   CPRefBand bc_thisfield;
/*      */   CPRefBand bc_superfield;
/*      */   CPRefBand bc_thismethod;
/*      */   CPRefBand bc_supermethod;
/*      */   IntBand bc_initref;
/*      */   CPRefBand bc_escref;
/*      */   IntBand bc_escrefsize;
/*      */   IntBand bc_escsize;
/*      */   ByteBand bc_escbyte;
/*      */   MultiBand file_bands;
/*      */   CPRefBand file_name;
/*      */   IntBand file_size_hi;
/*      */   IntBand file_size_lo;
/*      */   IntBand file_modtime;
/*      */   IntBand file_options;
/*      */   ByteBand file_bits;
/*      */   protected MultiBand[] metadataBands;
/*      */   public static final int ADH_CONTEXT_MASK = 3;
/*      */   public static final int ADH_BIT_SHIFT = 2;
/*      */   public static final int ADH_BIT_IS_LSB = 1;
/*      */   public static final int ATTR_INDEX_OVERFLOW = -1;
/*      */   public int[] attrIndexLimit;
/*      */   protected long[] attrFlagMask;
/*      */   protected long[] attrDefSeen;
/*      */   protected int[] attrOverflowMask;
/*      */   protected int attrClassFileVersionMask;
/*      */   protected Map<Attribute.Layout, Band[]> attrBandTable;
/*      */   protected final Attribute.Layout attrCodeEmpty;
/*      */   protected final Attribute.Layout attrInnerClassesEmpty;
/*      */   protected final Attribute.Layout attrClassFileVersion;
/*      */   protected final Attribute.Layout attrConstantValue;
/*      */   Map<Attribute.Layout, Integer> attrIndexTable;
/*      */   protected List<List<Attribute.Layout>> attrDefs;
/*      */   protected MultiBand[] attrBands;
/* 2250 */   private static final int[][] shortCodeLimits = { { 12, 12 }, { 8, 8 }, { 7, 7 } };
/*      */   public final int shortCodeHeader_h_limit;
/*      */   static final int LONG_CODE_HEADER = 0;
/*      */   static int nextSeqForDebug;
/* 2366 */   static File dumpDir = null;
/*      */   private Map<Band, Band> prevForAssertMap;
/*      */ 
/*      */   protected abstract ConstantPool.Index getCPIndex(byte paramByte);
/*      */ 
/*      */   public void initPackageMajver(int paramInt)
/*      */     throws IOException
/*      */   {
/*   82 */     assert ((paramInt > 0) && (paramInt < 65536));
/*   83 */     if (this.packageMajver > 0) {
/*   84 */       throw new IOException("Package majver is already initialized to " + this.packageMajver + "; new setting is " + paramInt);
/*      */     }
/*      */ 
/*   88 */     this.packageMajver = paramInt;
/*   89 */     adjustToMajver();
/*      */   }
/*      */   public int getPackageMajver() {
/*   92 */     if (this.packageMajver < 0) {
/*   93 */       throw new RuntimeException("Package majver not yet initialized");
/*      */     }
/*   95 */     return this.packageMajver;
/*      */   }
/*      */ 
/*      */   protected BandStructure()
/*      */   {
/*   67 */     if (this.effort == 0) this.effort = 5;
/*   68 */     this.optDumpBands = this.p200.getBoolean("com.sun.java.util.jar.pack.dump.bands");
/*   69 */     this.optDebugBands = this.p200.getBoolean("com.sun.java.util.jar.pack.debug.bands");
/*      */ 
/*   72 */     this.optVaryCodings = (!this.p200.getBoolean("com.sun.java.util.jar.pack.no.vary.codings"));
/*   73 */     this.optBigStrings = (!this.p200.getBoolean("com.sun.java.util.jar.pack.no.big.strings"));
/*      */ 
/*   78 */     this.packageMajver = -1;
/*      */ 
/*   98 */     this.isReader = (this instanceof PackageReader);
/*      */ 
/* 1052 */     this.allKQBands = new ArrayList();
/* 1053 */     this.needPredefIndex = new ArrayList();
/*      */ 
/* 1416 */     this.all_bands = ((MultiBand)new MultiBand("(package)", UNSIGNED5).init());
/*      */ 
/* 1419 */     this.archive_magic = this.all_bands.newByteBand("archive_magic");
/* 1420 */     this.archive_header_0 = this.all_bands.newIntBand("archive_header_0", UNSIGNED5);
/* 1421 */     this.archive_header_S = this.all_bands.newIntBand("archive_header_S", UNSIGNED5);
/* 1422 */     this.archive_header_1 = this.all_bands.newIntBand("archive_header_1", UNSIGNED5);
/* 1423 */     this.band_headers = this.all_bands.newByteBand("band_headers");
/*      */ 
/* 1426 */     this.cp_bands = this.all_bands.newMultiBand("(constant_pool)", DELTA5);
/* 1427 */     this.cp_Utf8_prefix = this.cp_bands.newIntBand("cp_Utf8_prefix");
/* 1428 */     this.cp_Utf8_suffix = this.cp_bands.newIntBand("cp_Utf8_suffix", UNSIGNED5);
/* 1429 */     this.cp_Utf8_chars = this.cp_bands.newIntBand("cp_Utf8_chars", CHAR3);
/* 1430 */     this.cp_Utf8_big_suffix = this.cp_bands.newIntBand("cp_Utf8_big_suffix");
/* 1431 */     this.cp_Utf8_big_chars = this.cp_bands.newMultiBand("(cp_Utf8_big_chars)", DELTA5);
/* 1432 */     this.cp_Int = this.cp_bands.newIntBand("cp_Int", UDELTA5);
/* 1433 */     this.cp_Float = this.cp_bands.newIntBand("cp_Float", UDELTA5);
/* 1434 */     this.cp_Long_hi = this.cp_bands.newIntBand("cp_Long_hi", UDELTA5);
/* 1435 */     this.cp_Long_lo = this.cp_bands.newIntBand("cp_Long_lo");
/* 1436 */     this.cp_Double_hi = this.cp_bands.newIntBand("cp_Double_hi", UDELTA5);
/* 1437 */     this.cp_Double_lo = this.cp_bands.newIntBand("cp_Double_lo");
/* 1438 */     this.cp_String = this.cp_bands.newCPRefBand("cp_String", UDELTA5, (byte)1);
/* 1439 */     this.cp_Class = this.cp_bands.newCPRefBand("cp_Class", UDELTA5, (byte)1);
/* 1440 */     this.cp_Signature_form = this.cp_bands.newCPRefBand("cp_Signature_form", (byte)1);
/* 1441 */     this.cp_Signature_classes = this.cp_bands.newCPRefBand("cp_Signature_classes", UDELTA5, (byte)7);
/* 1442 */     this.cp_Descr_name = this.cp_bands.newCPRefBand("cp_Descr_name", (byte)1);
/* 1443 */     this.cp_Descr_type = this.cp_bands.newCPRefBand("cp_Descr_type", UDELTA5, (byte)13);
/* 1444 */     this.cp_Field_class = this.cp_bands.newCPRefBand("cp_Field_class", (byte)7);
/* 1445 */     this.cp_Field_desc = this.cp_bands.newCPRefBand("cp_Field_desc", UDELTA5, (byte)12);
/* 1446 */     this.cp_Method_class = this.cp_bands.newCPRefBand("cp_Method_class", (byte)7);
/* 1447 */     this.cp_Method_desc = this.cp_bands.newCPRefBand("cp_Method_desc", UDELTA5, (byte)12);
/* 1448 */     this.cp_Imethod_class = this.cp_bands.newCPRefBand("cp_Imethod_class", (byte)7);
/* 1449 */     this.cp_Imethod_desc = this.cp_bands.newCPRefBand("cp_Imethod_desc", UDELTA5, (byte)12);
/*      */ 
/* 1452 */     this.attr_definition_bands = this.all_bands.newMultiBand("(attr_definition_bands)", UNSIGNED5);
/* 1453 */     this.attr_definition_headers = this.attr_definition_bands.newByteBand("attr_definition_headers");
/* 1454 */     this.attr_definition_name = this.attr_definition_bands.newCPRefBand("attr_definition_name", (byte)1);
/* 1455 */     this.attr_definition_layout = this.attr_definition_bands.newCPRefBand("attr_definition_layout", (byte)1);
/*      */ 
/* 1458 */     this.ic_bands = this.all_bands.newMultiBand("(ic_bands)", DELTA5);
/* 1459 */     this.ic_this_class = this.ic_bands.newCPRefBand("ic_this_class", UDELTA5, (byte)7);
/* 1460 */     this.ic_flags = this.ic_bands.newIntBand("ic_flags", UNSIGNED5);
/*      */ 
/* 1462 */     this.ic_outer_class = this.ic_bands.newCPRefBand("ic_outer_class", DELTA5, (byte)7, true);
/* 1463 */     this.ic_name = this.ic_bands.newCPRefBand("ic_name", DELTA5, (byte)1, true);
/*      */ 
/* 1466 */     this.class_bands = this.all_bands.newMultiBand("(class_bands)", DELTA5);
/* 1467 */     this.class_this = this.class_bands.newCPRefBand("class_this", (byte)7);
/* 1468 */     this.class_super = this.class_bands.newCPRefBand("class_super", (byte)7);
/* 1469 */     this.class_interface_count = this.class_bands.newIntBand("class_interface_count");
/* 1470 */     this.class_interface = this.class_bands.newCPRefBand("class_interface", (byte)7);
/*      */ 
/* 1473 */     this.class_field_count = this.class_bands.newIntBand("class_field_count");
/* 1474 */     this.class_method_count = this.class_bands.newIntBand("class_method_count");
/*      */ 
/* 1476 */     this.field_descr = this.class_bands.newCPRefBand("field_descr", (byte)12);
/* 1477 */     this.field_attr_bands = this.class_bands.newMultiBand("(field_attr_bands)", UNSIGNED5);
/* 1478 */     this.field_flags_hi = this.field_attr_bands.newIntBand("field_flags_hi");
/* 1479 */     this.field_flags_lo = this.field_attr_bands.newIntBand("field_flags_lo");
/* 1480 */     this.field_attr_count = this.field_attr_bands.newIntBand("field_attr_count");
/* 1481 */     this.field_attr_indexes = this.field_attr_bands.newIntBand("field_attr_indexes");
/* 1482 */     this.field_attr_calls = this.field_attr_bands.newIntBand("field_attr_calls");
/*      */ 
/* 1485 */     this.field_ConstantValue_KQ = this.field_attr_bands.newCPRefBand("field_ConstantValue_KQ", (byte)20);
/* 1486 */     this.field_Signature_RS = this.field_attr_bands.newCPRefBand("field_Signature_RS", (byte)13);
/* 1487 */     this.field_metadata_bands = this.field_attr_bands.newMultiBand("(field_metadata_bands)", UNSIGNED5);
/*      */ 
/* 1489 */     this.method_descr = this.class_bands.newCPRefBand("method_descr", MDELTA5, (byte)12);
/* 1490 */     this.method_attr_bands = this.class_bands.newMultiBand("(method_attr_bands)", UNSIGNED5);
/* 1491 */     this.method_flags_hi = this.method_attr_bands.newIntBand("method_flags_hi");
/* 1492 */     this.method_flags_lo = this.method_attr_bands.newIntBand("method_flags_lo");
/* 1493 */     this.method_attr_count = this.method_attr_bands.newIntBand("method_attr_count");
/* 1494 */     this.method_attr_indexes = this.method_attr_bands.newIntBand("method_attr_indexes");
/* 1495 */     this.method_attr_calls = this.method_attr_bands.newIntBand("method_attr_calls");
/*      */ 
/* 1497 */     this.method_Exceptions_N = this.method_attr_bands.newIntBand("method_Exceptions_N");
/* 1498 */     this.method_Exceptions_RC = this.method_attr_bands.newCPRefBand("method_Exceptions_RC", (byte)7);
/* 1499 */     this.method_Signature_RS = this.method_attr_bands.newCPRefBand("method_Signature_RS", (byte)13);
/* 1500 */     this.method_metadata_bands = this.method_attr_bands.newMultiBand("(method_metadata_bands)", UNSIGNED5);
/*      */ 
/* 1502 */     this.class_attr_bands = this.class_bands.newMultiBand("(class_attr_bands)", UNSIGNED5);
/* 1503 */     this.class_flags_hi = this.class_attr_bands.newIntBand("class_flags_hi");
/* 1504 */     this.class_flags_lo = this.class_attr_bands.newIntBand("class_flags_lo");
/* 1505 */     this.class_attr_count = this.class_attr_bands.newIntBand("class_attr_count");
/* 1506 */     this.class_attr_indexes = this.class_attr_bands.newIntBand("class_attr_indexes");
/* 1507 */     this.class_attr_calls = this.class_attr_bands.newIntBand("class_attr_calls");
/*      */ 
/* 1509 */     this.class_SourceFile_RUN = this.class_attr_bands.newCPRefBand("class_SourceFile_RUN", UNSIGNED5, (byte)1, true);
/* 1510 */     this.class_EnclosingMethod_RC = this.class_attr_bands.newCPRefBand("class_EnclosingMethod_RC", (byte)7);
/* 1511 */     this.class_EnclosingMethod_RDN = this.class_attr_bands.newCPRefBand("class_EnclosingMethod_RDN", UNSIGNED5, (byte)12, true);
/* 1512 */     this.class_Signature_RS = this.class_attr_bands.newCPRefBand("class_Signature_RS", (byte)13);
/* 1513 */     this.class_metadata_bands = this.class_attr_bands.newMultiBand("(class_metadata_bands)", UNSIGNED5);
/* 1514 */     this.class_InnerClasses_N = this.class_attr_bands.newIntBand("class_InnerClasses_N");
/* 1515 */     this.class_InnerClasses_RC = this.class_attr_bands.newCPRefBand("class_InnerClasses_RC", (byte)7);
/* 1516 */     this.class_InnerClasses_F = this.class_attr_bands.newIntBand("class_InnerClasses_F");
/* 1517 */     this.class_InnerClasses_outer_RCN = this.class_attr_bands.newCPRefBand("class_InnerClasses_outer_RCN", UNSIGNED5, (byte)7, true);
/* 1518 */     this.class_InnerClasses_name_RUN = this.class_attr_bands.newCPRefBand("class_InnerClasses_name_RUN", UNSIGNED5, (byte)1, true);
/* 1519 */     this.class_ClassFile_version_minor_H = this.class_attr_bands.newIntBand("class_ClassFile_version_minor_H");
/* 1520 */     this.class_ClassFile_version_major_H = this.class_attr_bands.newIntBand("class_ClassFile_version_major_H");
/*      */ 
/* 1522 */     this.code_bands = this.class_bands.newMultiBand("(code_bands)", UNSIGNED5);
/* 1523 */     this.code_headers = this.code_bands.newByteBand("code_headers");
/* 1524 */     this.code_max_stack = this.code_bands.newIntBand("code_max_stack", UNSIGNED5);
/* 1525 */     this.code_max_na_locals = this.code_bands.newIntBand("code_max_na_locals", UNSIGNED5);
/* 1526 */     this.code_handler_count = this.code_bands.newIntBand("code_handler_count", UNSIGNED5);
/* 1527 */     this.code_handler_start_P = this.code_bands.newIntBand("code_handler_start_P", BCI5);
/* 1528 */     this.code_handler_end_PO = this.code_bands.newIntBand("code_handler_end_PO", BRANCH5);
/* 1529 */     this.code_handler_catch_PO = this.code_bands.newIntBand("code_handler_catch_PO", BRANCH5);
/* 1530 */     this.code_handler_class_RCN = this.code_bands.newCPRefBand("code_handler_class_RCN", UNSIGNED5, (byte)7, true);
/*      */ 
/* 1532 */     this.code_attr_bands = this.class_bands.newMultiBand("(code_attr_bands)", UNSIGNED5);
/* 1533 */     this.code_flags_hi = this.code_attr_bands.newIntBand("code_flags_hi");
/* 1534 */     this.code_flags_lo = this.code_attr_bands.newIntBand("code_flags_lo");
/* 1535 */     this.code_attr_count = this.code_attr_bands.newIntBand("code_attr_count");
/* 1536 */     this.code_attr_indexes = this.code_attr_bands.newIntBand("code_attr_indexes");
/* 1537 */     this.code_attr_calls = this.code_attr_bands.newIntBand("code_attr_calls");
/*      */ 
/* 1539 */     this.stackmap_bands = this.code_attr_bands.newMultiBand("StackMapTable_bands", UNSIGNED5);
/* 1540 */     this.code_StackMapTable_N = this.stackmap_bands.newIntBand("code_StackMapTable_N");
/* 1541 */     this.code_StackMapTable_frame_T = this.stackmap_bands.newIntBand("code_StackMapTable_frame_T", BYTE1);
/* 1542 */     this.code_StackMapTable_local_N = this.stackmap_bands.newIntBand("code_StackMapTable_local_N");
/* 1543 */     this.code_StackMapTable_stack_N = this.stackmap_bands.newIntBand("code_StackMapTable_stack_N");
/* 1544 */     this.code_StackMapTable_offset = this.stackmap_bands.newIntBand("code_StackMapTable_offset", UNSIGNED5);
/* 1545 */     this.code_StackMapTable_T = this.stackmap_bands.newIntBand("code_StackMapTable_T", BYTE1);
/* 1546 */     this.code_StackMapTable_RC = this.stackmap_bands.newCPRefBand("code_StackMapTable_RC", (byte)7);
/* 1547 */     this.code_StackMapTable_P = this.stackmap_bands.newIntBand("code_StackMapTable_P", BCI5);
/*      */ 
/* 1550 */     this.code_LineNumberTable_N = this.code_attr_bands.newIntBand("code_LineNumberTable_N");
/* 1551 */     this.code_LineNumberTable_bci_P = this.code_attr_bands.newIntBand("code_LineNumberTable_bci_P", BCI5);
/* 1552 */     this.code_LineNumberTable_line = this.code_attr_bands.newIntBand("code_LineNumberTable_line");
/*      */ 
/* 1555 */     this.code_LocalVariableTable_N = this.code_attr_bands.newIntBand("code_LocalVariableTable_N");
/* 1556 */     this.code_LocalVariableTable_bci_P = this.code_attr_bands.newIntBand("code_LocalVariableTable_bci_P", BCI5);
/* 1557 */     this.code_LocalVariableTable_span_O = this.code_attr_bands.newIntBand("code_LocalVariableTable_span_O", BRANCH5);
/* 1558 */     this.code_LocalVariableTable_name_RU = this.code_attr_bands.newCPRefBand("code_LocalVariableTable_name_RU", (byte)1);
/* 1559 */     this.code_LocalVariableTable_type_RS = this.code_attr_bands.newCPRefBand("code_LocalVariableTable_type_RS", (byte)13);
/* 1560 */     this.code_LocalVariableTable_slot = this.code_attr_bands.newIntBand("code_LocalVariableTable_slot");
/* 1561 */     this.code_LocalVariableTypeTable_N = this.code_attr_bands.newIntBand("code_LocalVariableTypeTable_N");
/* 1562 */     this.code_LocalVariableTypeTable_bci_P = this.code_attr_bands.newIntBand("code_LocalVariableTypeTable_bci_P", BCI5);
/* 1563 */     this.code_LocalVariableTypeTable_span_O = this.code_attr_bands.newIntBand("code_LocalVariableTypeTable_span_O", BRANCH5);
/* 1564 */     this.code_LocalVariableTypeTable_name_RU = this.code_attr_bands.newCPRefBand("code_LocalVariableTypeTable_name_RU", (byte)1);
/* 1565 */     this.code_LocalVariableTypeTable_type_RS = this.code_attr_bands.newCPRefBand("code_LocalVariableTypeTable_type_RS", (byte)13);
/* 1566 */     this.code_LocalVariableTypeTable_slot = this.code_attr_bands.newIntBand("code_LocalVariableTypeTable_slot");
/*      */ 
/* 1569 */     this.bc_bands = this.all_bands.newMultiBand("(byte_codes)", UNSIGNED5);
/* 1570 */     this.bc_codes = this.bc_bands.newByteBand("bc_codes");
/*      */ 
/* 1573 */     this.bc_case_count = this.bc_bands.newIntBand("bc_case_count");
/* 1574 */     this.bc_case_value = this.bc_bands.newIntBand("bc_case_value", DELTA5);
/* 1575 */     this.bc_byte = this.bc_bands.newByteBand("bc_byte");
/* 1576 */     this.bc_short = this.bc_bands.newIntBand("bc_short", DELTA5);
/* 1577 */     this.bc_local = this.bc_bands.newIntBand("bc_local");
/* 1578 */     this.bc_label = this.bc_bands.newIntBand("bc_label", BRANCH5);
/*      */ 
/* 1584 */     this.bc_intref = this.bc_bands.newCPRefBand("bc_intref", DELTA5, (byte)3);
/* 1585 */     this.bc_floatref = this.bc_bands.newCPRefBand("bc_floatref", DELTA5, (byte)4);
/* 1586 */     this.bc_longref = this.bc_bands.newCPRefBand("bc_longref", DELTA5, (byte)5);
/* 1587 */     this.bc_doubleref = this.bc_bands.newCPRefBand("bc_doubleref", DELTA5, (byte)6);
/* 1588 */     this.bc_stringref = this.bc_bands.newCPRefBand("bc_stringref", DELTA5, (byte)8);
/*      */ 
/* 1591 */     this.bc_classref = this.bc_bands.newCPRefBand("bc_classref", UNSIGNED5, (byte)7, true);
/* 1592 */     this.bc_fieldref = this.bc_bands.newCPRefBand("bc_fieldref", DELTA5, (byte)9);
/* 1593 */     this.bc_methodref = this.bc_bands.newCPRefBand("bc_methodref", (byte)10);
/* 1594 */     this.bc_imethodref = this.bc_bands.newCPRefBand("bc_imethodref", DELTA5, (byte)11);
/*      */ 
/* 1597 */     this.bc_thisfield = this.bc_bands.newCPRefBand("bc_thisfield", (byte)0);
/* 1598 */     this.bc_superfield = this.bc_bands.newCPRefBand("bc_superfield", (byte)0);
/* 1599 */     this.bc_thismethod = this.bc_bands.newCPRefBand("bc_thismethod", (byte)0);
/* 1600 */     this.bc_supermethod = this.bc_bands.newCPRefBand("bc_supermethod", (byte)0);
/*      */ 
/* 1602 */     this.bc_initref = this.bc_bands.newIntBand("bc_initref");
/*      */ 
/* 1604 */     this.bc_escref = this.bc_bands.newCPRefBand("bc_escref", (byte)19);
/* 1605 */     this.bc_escrefsize = this.bc_bands.newIntBand("bc_escrefsize");
/* 1606 */     this.bc_escsize = this.bc_bands.newIntBand("bc_escsize");
/* 1607 */     this.bc_escbyte = this.bc_bands.newByteBand("bc_escbyte");
/*      */ 
/* 1610 */     this.file_bands = this.all_bands.newMultiBand("(file_bands)", UNSIGNED5);
/* 1611 */     this.file_name = this.file_bands.newCPRefBand("file_name", (byte)1);
/* 1612 */     this.file_size_hi = this.file_bands.newIntBand("file_size_hi");
/* 1613 */     this.file_size_lo = this.file_bands.newIntBand("file_size_lo");
/* 1614 */     this.file_modtime = this.file_bands.newIntBand("file_modtime", DELTA5);
/* 1615 */     this.file_options = this.file_bands.newIntBand("file_options");
/* 1616 */     this.file_bits = this.file_bands.newByteBand("file_bits");
/*      */ 
/* 1664 */     this.metadataBands = new MultiBand[4];
/*      */ 
/* 1666 */     this.metadataBands[0] = this.class_metadata_bands;
/* 1667 */     this.metadataBands[1] = this.field_metadata_bands;
/* 1668 */     this.metadataBands[2] = this.method_metadata_bands;
/*      */ 
/* 1677 */     this.attrIndexLimit = new int[4];
/*      */ 
/* 1681 */     this.attrFlagMask = new long[4];
/*      */ 
/* 1683 */     this.attrDefSeen = new long[4];
/*      */ 
/* 1686 */     this.attrOverflowMask = new int[4];
/*      */ 
/* 1690 */     this.attrBandTable = new HashMap();
/*      */ 
/* 1699 */     this.attrIndexTable = new HashMap();
/*      */ 
/* 1702 */     this.attrDefs = new FixedList(4);
/*      */ 
/* 1705 */     for (int i = 0; i < 4; i++) {
/* 1706 */       assert (this.attrIndexLimit[i] == 0);
/* 1707 */       this.attrIndexLimit[i] = 32;
/* 1708 */       this.attrDefs.set(i, new ArrayList(Collections.nCopies(this.attrIndexLimit[i], (Attribute.Layout)null)));
/*      */     }
/*      */ 
/* 1714 */     this.attrInnerClassesEmpty = predefineAttribute(23, 0, null, "InnerClasses", "");
/*      */ 
/* 1717 */     assert (this.attrInnerClassesEmpty == Package.attrInnerClassesEmpty);
/* 1718 */     predefineAttribute(17, 0, new Band[] { this.class_SourceFile_RUN }, "SourceFile", "RUNH");
/*      */ 
/* 1721 */     predefineAttribute(18, 0, new Band[] { this.class_EnclosingMethod_RC, this.class_EnclosingMethod_RDN }, "EnclosingMethod", "RCHRDNH");
/*      */ 
/* 1727 */     this.attrClassFileVersion = predefineAttribute(24, 0, new Band[] { this.class_ClassFile_version_minor_H, this.class_ClassFile_version_major_H }, ".ClassFile.version", "HH");
/*      */ 
/* 1734 */     predefineAttribute(19, 0, new Band[] { this.class_Signature_RS }, "Signature", "RSH");
/*      */ 
/* 1737 */     predefineAttribute(20, 0, null, "Deprecated", "");
/*      */ 
/* 1741 */     predefineAttribute(16, 0, null, ".Overflow", "");
/*      */ 
/* 1743 */     this.attrConstantValue = predefineAttribute(17, 1, new Band[] { this.field_ConstantValue_KQ }, "ConstantValue", "KQH");
/*      */ 
/* 1747 */     predefineAttribute(19, 1, new Band[] { this.field_Signature_RS }, "Signature", "RSH");
/*      */ 
/* 1750 */     predefineAttribute(20, 1, null, "Deprecated", "");
/*      */ 
/* 1754 */     predefineAttribute(16, 1, null, ".Overflow", "");
/*      */ 
/* 1756 */     this.attrCodeEmpty = predefineAttribute(17, 2, null, "Code", "");
/*      */ 
/* 1759 */     predefineAttribute(18, 2, new Band[] { this.method_Exceptions_N, this.method_Exceptions_RC }, "Exceptions", "NH[RCH]");
/*      */ 
/* 1765 */     assert (this.attrCodeEmpty == Package.attrCodeEmpty);
/* 1766 */     predefineAttribute(19, 2, new Band[] { this.method_Signature_RS }, "Signature", "RSH");
/*      */ 
/* 1769 */     predefineAttribute(20, 2, null, "Deprecated", "");
/*      */ 
/* 1773 */     predefineAttribute(16, 2, null, ".Overflow", "");
/*      */ 
/* 1776 */     for (i = 0; i < 4; i++) {
/* 1777 */       MultiBand localMultiBand = this.metadataBands[i];
/* 1778 */       if (localMultiBand != null)
/*      */       {
/* 1783 */         predefineAttribute(21, Constants.ATTR_CONTEXT_NAME[i] + "_RVA_", localMultiBand, Attribute.lookup(null, i, "RuntimeVisibleAnnotations"));
/*      */ 
/* 1788 */         predefineAttribute(22, Constants.ATTR_CONTEXT_NAME[i] + "_RIA_", localMultiBand, Attribute.lookup(null, i, "RuntimeInvisibleAnnotations"));
/*      */ 
/* 1793 */         if (i == 2)
/*      */         {
/* 1796 */           predefineAttribute(23, "method_RVPA_", localMultiBand, Attribute.lookup(null, i, "RuntimeVisibleParameterAnnotations"));
/*      */ 
/* 1800 */           predefineAttribute(24, "method_RIPA_", localMultiBand, Attribute.lookup(null, i, "RuntimeInvisibleParameterAnnotations"));
/*      */ 
/* 1804 */           predefineAttribute(25, "method_AD_", localMultiBand, Attribute.lookup(null, i, "AnnotationDefault"));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1811 */     Attribute.Layout localLayout = Attribute.lookup(null, 3, "StackMapTable").layout();
/* 1812 */     predefineAttribute(0, 3, this.stackmap_bands.toArray(), localLayout.name(), localLayout.layout());
/*      */ 
/* 1816 */     predefineAttribute(1, 3, new Band[] { this.code_LineNumberTable_N, this.code_LineNumberTable_bci_P, this.code_LineNumberTable_line }, "LineNumberTable", "NH[PHH]");
/*      */ 
/* 1823 */     predefineAttribute(2, 3, new Band[] { this.code_LocalVariableTable_N, this.code_LocalVariableTable_bci_P, this.code_LocalVariableTable_span_O, this.code_LocalVariableTable_name_RU, this.code_LocalVariableTable_type_RS, this.code_LocalVariableTable_slot }, "LocalVariableTable", "NH[PHOHRUHRSHH]");
/*      */ 
/* 1833 */     predefineAttribute(3, 3, new Band[] { this.code_LocalVariableTypeTable_N, this.code_LocalVariableTypeTable_bci_P, this.code_LocalVariableTypeTable_span_O, this.code_LocalVariableTypeTable_name_RU, this.code_LocalVariableTypeTable_type_RS, this.code_LocalVariableTypeTable_slot }, "LocalVariableTypeTable", "NH[PHOHRUHRSHH]");
/*      */ 
/* 1843 */     predefineAttribute(16, 3, null, ".Overflow", "");
/*      */ 
/* 1848 */     for (int j = 0; j < 4; j++) {
/* 1849 */       this.attrDefSeen[j] = 0L;
/*      */     }
/*      */ 
/* 1853 */     for (j = 0; j < 4; j++) {
/* 1854 */       this.attrOverflowMask[j] = 65536;
/* 1855 */       this.attrIndexLimit[j] = 0;
/*      */     }
/* 1857 */     this.attrClassFileVersionMask = 16777216;
/*      */ 
/* 2072 */     this.attrBands = new MultiBand[4];
/*      */ 
/* 2074 */     this.attrBands[0] = this.class_attr_bands;
/* 2075 */     this.attrBands[1] = this.field_attr_bands;
/* 2076 */     this.attrBands[2] = this.method_attr_bands;
/* 2077 */     this.attrBands[3] = this.code_attr_bands;
/*      */ 
/* 2255 */     this.shortCodeHeader_h_limit = shortCodeLimits.length;
/*      */   }
/*      */ 
/*      */   public static Coding codingForIndex(int paramInt)
/*      */   {
/*  271 */     return paramInt < basicCodings.length ? basicCodings[paramInt] : null;
/*      */   }
/*      */   public static int indexOf(Coding paramCoding) {
/*  274 */     Integer localInteger = (Integer)basicCodingIndexes.get(paramCoding);
/*  275 */     if (localInteger == null) return 0;
/*  276 */     return localInteger.intValue();
/*      */   }
/*      */   public static Coding[] getBasicCodings() {
/*  279 */     return (Coding[])basicCodings.clone();
/*      */   }
/*      */ 
/*      */   protected CodingMethod getBandHeader(int paramInt, Coding paramCoding)
/*      */   {
/*  287 */     CodingMethod[] arrayOfCodingMethod = { null };
/*      */ 
/*  289 */     this.bandHeaderBytes[(--this.bandHeaderBytePos)] = ((byte)paramInt);
/*  290 */     this.bandHeaderBytePos0 = this.bandHeaderBytePos;
/*      */ 
/*  292 */     this.bandHeaderBytePos = parseMetaCoding(this.bandHeaderBytes, this.bandHeaderBytePos, paramCoding, arrayOfCodingMethod);
/*      */ 
/*  296 */     return arrayOfCodingMethod[0];
/*      */   }
/*      */ 
/*      */   public static int parseMetaCoding(byte[] paramArrayOfByte, int paramInt, Coding paramCoding, CodingMethod[] paramArrayOfCodingMethod) {
/*  300 */     if ((paramArrayOfByte[paramInt] & 0xFF) == 0) {
/*  301 */       paramArrayOfCodingMethod[0] = paramCoding;
/*  302 */       return paramInt + 1;
/*      */     }
/*      */ 
/*  305 */     int i = Coding.parseMetaCoding(paramArrayOfByte, paramInt, paramCoding, paramArrayOfCodingMethod);
/*  306 */     if (i > paramInt) return i;
/*  307 */     i = PopulationCoding.parseMetaCoding(paramArrayOfByte, paramInt, paramCoding, paramArrayOfCodingMethod);
/*  308 */     if (i > paramInt) return i;
/*  309 */     i = AdaptiveCoding.parseMetaCoding(paramArrayOfByte, paramInt, paramCoding, paramArrayOfCodingMethod);
/*  310 */     if (i > paramInt) return i;
/*  311 */     throw new RuntimeException("Bad meta-coding op " + (paramArrayOfByte[paramInt] & 0xFF));
/*      */   }
/*      */ 
/*      */   static boolean phaseIsRead(int paramInt)
/*      */   {
/*  331 */     return paramInt % 2 == 0;
/*      */   }
/*      */   static int phaseCmp(int paramInt1, int paramInt2) {
/*  334 */     assert ((paramInt1 % 2 == paramInt2 % 2) || (paramInt1 % 8 == 0) || (paramInt2 % 8 == 0));
/*  335 */     return paramInt1 - paramInt2;
/*      */   }
/*      */ 
/*      */   static int getIntTotal(int[] paramArrayOfInt)
/*      */   {
/*  971 */     int i = 0;
/*  972 */     for (int j = 0; j < paramArrayOfInt.length; j++) {
/*  973 */       i += paramArrayOfInt[j];
/*      */     }
/*  975 */     return i;
/*      */   }
/*      */ 
/*      */   int encodeRef(ConstantPool.Entry paramEntry, ConstantPool.Index paramIndex)
/*      */   {
/* 1057 */     if (paramIndex == null)
/* 1058 */       throw new RuntimeException("null index for " + paramEntry.stringValue());
/* 1059 */     int i = paramIndex.indexOf(paramEntry);
/* 1060 */     if (this.verbose > 2)
/* 1061 */       Utils.log.fine("putRef " + i + " => " + paramEntry);
/* 1062 */     return i;
/*      */   }
/*      */ 
/*      */   ConstantPool.Entry decodeRef(int paramInt, ConstantPool.Index paramIndex) {
/* 1066 */     if ((paramInt < 0) || (paramInt >= paramIndex.size()))
/* 1067 */       Utils.log.warning("decoding bad ref " + paramInt + " in " + paramIndex);
/* 1068 */     ConstantPool.Entry localEntry = paramIndex.getEntry(paramInt);
/* 1069 */     if (this.verbose > 2)
/* 1070 */       Utils.log.fine("getRef " + paramInt + " => " + localEntry);
/* 1071 */     return localEntry;
/*      */   }
/*      */ 
/*      */   protected CodingChooser getCodingChooser()
/*      */   {
/* 1076 */     if (this.codingChooser == null) {
/* 1077 */       this.codingChooser = new CodingChooser(this.effort, basicCodings);
/* 1078 */       if ((this.codingChooser.stress != null) && ((this instanceof PackageWriter)))
/*      */       {
/* 1082 */         ArrayList localArrayList = ((PackageWriter)this).pkg.classes;
/* 1083 */         if (!localArrayList.isEmpty()) {
/* 1084 */           Package.Class localClass = (Package.Class)localArrayList.get(0);
/* 1085 */           this.codingChooser.addStressSeed(localClass.getName().hashCode());
/*      */         }
/*      */       }
/*      */     }
/* 1089 */     return this.codingChooser;
/*      */   }
/*      */ 
/*      */   public CodingMethod chooseCoding(int[] paramArrayOfInt1, int paramInt1, int paramInt2, Coding paramCoding, String paramString, int[] paramArrayOfInt2)
/*      */   {
/* 1095 */     assert (this.optVaryCodings);
/* 1096 */     if (this.effort <= 1) {
/* 1097 */       return paramCoding;
/*      */     }
/* 1099 */     CodingChooser localCodingChooser = getCodingChooser();
/* 1100 */     if ((this.verbose > 1) || (localCodingChooser.verbose > 1)) {
/* 1101 */       Utils.log.fine("--- chooseCoding " + paramString);
/*      */     }
/* 1103 */     return localCodingChooser.choose(paramArrayOfInt1, paramInt1, paramInt2, paramCoding, paramArrayOfInt2);
/*      */   }
/*      */ 
/*      */   protected static int decodeEscapeValue(int paramInt, Coding paramCoding)
/*      */   {
/* 1132 */     if ((paramCoding.B() == 1) || (paramCoding.L() == 0))
/* 1133 */       return -1;
/*      */     int i;
/* 1134 */     if (paramCoding.S() != 0) {
/* 1135 */       if ((-256 <= paramInt) && (paramInt <= -1) && (paramCoding.min() <= -256)) {
/* 1136 */         i = -1 - paramInt;
/* 1137 */         assert ((i >= 0) && (i < 256));
/* 1138 */         return i;
/*      */       }
/*      */     } else {
/* 1141 */       i = paramCoding.L();
/* 1142 */       if ((i <= paramInt) && (paramInt <= i + 255) && (paramCoding.max() >= i + 255)) {
/* 1143 */         int j = paramInt - i;
/* 1144 */         assert ((j >= 0) && (j < 256));
/* 1145 */         return j;
/*      */       }
/*      */     }
/* 1148 */     return -1;
/*      */   }
/*      */ 
/*      */   protected static int encodeEscapeValue(int paramInt, Coding paramCoding) {
/* 1152 */     assert ((paramInt >= 0) && (paramInt < 256));
/* 1153 */     assert ((paramCoding.B() > 1) && (paramCoding.L() > 0));
/*      */     int i;
/* 1155 */     if (paramCoding.S() != 0) {
/* 1156 */       assert (paramCoding.min() <= -256);
/* 1157 */       i = -1 - paramInt;
/*      */     } else {
/* 1159 */       int j = paramCoding.L();
/* 1160 */       assert (paramCoding.max() >= j + 255);
/* 1161 */       i = paramInt + j;
/*      */     }
/*      */ 
/* 1164 */     assert (decodeEscapeValue(i, paramCoding) == paramInt) : (paramCoding + " XB=" + paramInt + " X=" + i);
/* 1165 */     return i;
/*      */   }
/*      */ 
/*      */   void writeAllBandsTo(OutputStream paramOutputStream)
/*      */     throws IOException
/*      */   {
/* 1357 */     this.outputCounter = new ByteCounter(paramOutputStream);
/* 1358 */     paramOutputStream = this.outputCounter;
/* 1359 */     this.all_bands.writeTo(paramOutputStream);
/* 1360 */     if (this.verbose > 0) {
/* 1361 */       long l = this.outputCounter.getCount();
/* 1362 */       Utils.log.info("Wrote total of " + l + " bytes.");
/* 1363 */       assert (l == this.archiveSize0 + this.archiveSize1);
/*      */     }
/* 1365 */     this.outputCounter = null;
/*      */   }
/*      */ 
/*      */   static IntBand getAttrBand(MultiBand paramMultiBand, int paramInt)
/*      */   {
/* 1396 */     IntBand localIntBand = (IntBand)paramMultiBand.get(paramInt);
/* 1397 */     switch (paramInt) {
/*      */     case 0:
/* 1399 */       if ((!$assertionsDisabled) && (!localIntBand.name().endsWith("_flags_hi"))) throw new AssertionError(); break;
/*      */     case 1:
/* 1401 */       if ((!$assertionsDisabled) && (!localIntBand.name().endsWith("_flags_lo"))) throw new AssertionError(); break;
/*      */     case 2:
/* 1403 */       if ((!$assertionsDisabled) && (!localIntBand.name().endsWith("_attr_count"))) throw new AssertionError(); break;
/*      */     case 3:
/* 1405 */       if ((!$assertionsDisabled) && (!localIntBand.name().endsWith("_attr_indexes"))) throw new AssertionError(); break;
/*      */     case 4:
/* 1407 */       if ((!$assertionsDisabled) && (!localIntBand.name().endsWith("_attr_calls"))) throw new AssertionError(); break;
/*      */     default:
/* 1409 */       if (!$assertionsDisabled) throw new AssertionError(); break;
/*      */     }
/* 1411 */     return localIntBand;
/*      */   }
/*      */ 
/*      */   protected void setBandIndexes()
/*      */   {
/* 1623 */     for (Object[] arrayOfObject : this.needPredefIndex) {
/* 1624 */       CPRefBand localCPRefBand = (CPRefBand)arrayOfObject[0];
/* 1625 */       Byte localByte = (Byte)arrayOfObject[1];
/* 1626 */       localCPRefBand.setIndex(getCPIndex(localByte.byteValue()));
/*      */     }
/* 1628 */     this.needPredefIndex = null;
/*      */ 
/* 1630 */     if (this.verbose > 3)
/* 1631 */       printCDecl(this.all_bands);
/*      */   }
/*      */ 
/*      */   protected void setBandIndex(CPRefBand paramCPRefBand, byte paramByte)
/*      */   {
/* 1636 */     Object[] arrayOfObject = { paramCPRefBand, Byte.valueOf(paramByte) };
/* 1637 */     if (paramByte == 20)
/*      */     {
/* 1639 */       this.allKQBands.add(paramCPRefBand);
/* 1640 */     } else if (this.needPredefIndex != null) {
/* 1641 */       this.needPredefIndex.add(arrayOfObject);
/*      */     }
/*      */     else
/* 1644 */       paramCPRefBand.setIndex(getCPIndex(paramByte));
/*      */   }
/*      */ 
/*      */   protected void setConstantValueIndex(Package.Class.Field paramField)
/*      */   {
/* 1649 */     ConstantPool.Index localIndex = null;
/* 1650 */     if (paramField != null) {
/* 1651 */       byte b = paramField.getLiteralTag();
/* 1652 */       localIndex = getCPIndex(b);
/* 1653 */       if (this.verbose > 2)
/* 1654 */         Utils.log.fine("setConstantValueIndex " + paramField + " " + ConstantPool.tagName(b) + " => " + localIndex);
/* 1655 */       assert (localIndex != null);
/*      */     }
/*      */ 
/* 1658 */     for (CPRefBand localCPRefBand : this.allKQBands)
/* 1659 */       localCPRefBand.setIndex(localIndex);
/*      */   }
/*      */ 
/*      */   private void adjustToMajver()
/*      */   {
/* 1861 */     if (getPackageMajver() < 160) {
/* 1862 */       if (this.verbose > 0) Utils.log.fine("Legacy package version");
/*      */ 
/* 1864 */       undefineAttribute(0, 3);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void initAttrIndexLimit() {
/* 1869 */     for (int i = 0; i < 4; i++) {
/* 1870 */       assert (this.attrIndexLimit[i] == 0);
/* 1871 */       this.attrIndexLimit[i] = (haveFlagsHi(i) ? 63 : 32);
/* 1872 */       List localList = (List)this.attrDefs.get(i);
/* 1873 */       assert (localList.size() == 32);
/* 1874 */       int j = this.attrIndexLimit[i] - localList.size();
/* 1875 */       localList.addAll(Collections.nCopies(j, (Attribute.Layout)null));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean haveFlagsHi(int paramInt) {
/* 1880 */     int i = 1 << 9 + paramInt;
/* 1881 */     switch (paramInt) {
/*      */     case 0:
/* 1883 */       if ((!$assertionsDisabled) && (i != 512)) throw new AssertionError(); break;
/*      */     case 1:
/* 1885 */       if ((!$assertionsDisabled) && (i != 1024)) throw new AssertionError(); break;
/*      */     case 2:
/* 1887 */       if ((!$assertionsDisabled) && (i != 2048)) throw new AssertionError(); break;
/*      */     case 3:
/* 1889 */       if ((!$assertionsDisabled) && (i != 4096)) throw new AssertionError(); break;
/*      */     default:
/* 1891 */       if (!$assertionsDisabled) throw new AssertionError(); break;
/*      */     }
/* 1893 */     return testBit(this.archiveOptions, i);
/*      */   }
/*      */ 
/*      */   protected List getPredefinedAttrs(int paramInt) {
/* 1897 */     assert (this.attrIndexLimit[paramInt] != 0);
/* 1898 */     ArrayList localArrayList = new ArrayList(this.attrIndexLimit[paramInt]);
/*      */ 
/* 1900 */     for (int i = 0; i < this.attrIndexLimit[paramInt]; i++)
/* 1901 */       if (!testBit(this.attrDefSeen[paramInt], 1L << i)) {
/* 1902 */         Attribute.Layout localLayout = (Attribute.Layout)((List)this.attrDefs.get(paramInt)).get(i);
/* 1903 */         if (localLayout != null) {
/* 1904 */           assert (isPredefinedAttr(paramInt, i));
/* 1905 */           localArrayList.add(localLayout);
/*      */         }
/*      */       }
/* 1907 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   protected boolean isPredefinedAttr(int paramInt1, int paramInt2) {
/* 1911 */     assert (this.attrIndexLimit[paramInt1] != 0);
/*      */ 
/* 1913 */     if (paramInt2 >= this.attrIndexLimit[paramInt1]) return false;
/*      */ 
/* 1915 */     if (testBit(this.attrDefSeen[paramInt1], 1L << paramInt2)) return false;
/* 1916 */     return ((List)this.attrDefs.get(paramInt1)).get(paramInt2) != null;
/*      */   }
/*      */ 
/*      */   protected void adjustSpecialAttrMasks()
/*      */   {
/* 1921 */     this.attrClassFileVersionMask = ((int)(this.attrClassFileVersionMask & (this.attrDefSeen[0] ^ 0xFFFFFFFF)));
/*      */ 
/* 1923 */     for (int i = 0; i < 4; i++)
/*      */     {
/*      */       int tmp33_32 = i;
/*      */       int[] tmp33_29 = this.attrOverflowMask; tmp33_29[tmp33_32] = ((int)(tmp33_29[tmp33_32] & (this.attrDefSeen[i] ^ 0xFFFFFFFF)));
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Attribute makeClassFileVersionAttr(int paramInt1, int paramInt2) {
/* 1929 */     byte[] arrayOfByte = { (byte)(paramInt1 >> 8), (byte)paramInt1, (byte)(paramInt2 >> 8), (byte)paramInt2 };
/*      */ 
/* 1933 */     return this.attrClassFileVersion.addContent(arrayOfByte);
/*      */   }
/*      */ 
/*      */   protected short[] parseClassFileVersionAttr(Attribute paramAttribute) {
/* 1937 */     assert (paramAttribute.layout() == this.attrClassFileVersion);
/* 1938 */     assert (paramAttribute.size() == 4);
/* 1939 */     byte[] arrayOfByte = paramAttribute.bytes();
/* 1940 */     int i = (arrayOfByte[0] & 0xFF) << 8 | arrayOfByte[1] & 0xFF;
/* 1941 */     int j = (arrayOfByte[2] & 0xFF) << 8 | arrayOfByte[3] & 0xFF;
/* 1942 */     return new short[] { (short)i, (short)j };
/*      */   }
/*      */ 
/*      */   private boolean assertBandOKForElems(Band[] paramArrayOfBand, Attribute.Layout.Element[] paramArrayOfElement) {
/* 1946 */     for (int i = 0; i < paramArrayOfElement.length; i++) {
/* 1947 */       assert (assertBandOKForElem(paramArrayOfBand, paramArrayOfElement[i]));
/*      */     }
/* 1949 */     return true;
/*      */   }
/*      */   private boolean assertBandOKForElem(Band[] paramArrayOfBand, Attribute.Layout.Element paramElement) {
/* 1952 */     Band localBand = null;
/* 1953 */     if (paramElement.bandIndex != -1)
/* 1954 */       localBand = paramArrayOfBand[paramElement.bandIndex];
/* 1955 */     Coding localCoding = UNSIGNED5;
/* 1956 */     int i = 1;
/* 1957 */     switch (paramElement.kind) {
/*      */     case 1:
/* 1959 */       if (paramElement.flagTest((byte)1))
/* 1960 */         localCoding = SIGNED5;
/* 1961 */       else if (paramElement.len == 1)
/* 1962 */         localCoding = BYTE1; break;
/*      */     case 2:
/* 1966 */       if (!paramElement.flagTest((byte)2))
/* 1967 */         localCoding = BCI5;
/*      */       else {
/* 1969 */         localCoding = BRANCH5;
/*      */       }
/* 1971 */       break;
/*      */     case 3:
/* 1973 */       localCoding = BRANCH5;
/* 1974 */       break;
/*      */     case 4:
/* 1976 */       if (paramElement.len == 1) localCoding = BYTE1; break;
/*      */     case 5:
/* 1979 */       if (paramElement.len == 1) localCoding = BYTE1;
/* 1980 */       assertBandOKForElems(paramArrayOfBand, paramElement.body);
/* 1981 */       break;
/*      */     case 7:
/* 1983 */       if (paramElement.flagTest((byte)1))
/* 1984 */         localCoding = SIGNED5;
/* 1985 */       else if (paramElement.len == 1) {
/* 1986 */         localCoding = BYTE1;
/*      */       }
/* 1988 */       assertBandOKForElems(paramArrayOfBand, paramElement.body);
/* 1989 */       break;
/*      */     case 8:
/* 1991 */       assert (localBand == null);
/* 1992 */       assertBandOKForElems(paramArrayOfBand, paramElement.body);
/* 1993 */       return true;
/*      */     case 9:
/* 1995 */       assert (localBand == null);
/* 1996 */       return true;
/*      */     case 10:
/* 1998 */       assert (localBand == null);
/* 1999 */       assertBandOKForElems(paramArrayOfBand, paramElement.body);
/* 2000 */       return true;
/*      */     case 6:
/* 2002 */       i = 0;
/* 2003 */       assert ((localBand instanceof CPRefBand));
/* 2004 */       if ((!$assertionsDisabled) && (((CPRefBand)localBand).nullOK != paramElement.flagTest((byte)4))) throw new AssertionError(); break;
/*      */     default:
/* 2006 */       if (!$assertionsDisabled) throw new AssertionError();
/*      */       break;
/*      */     }
/* 2009 */     assert (localBand.regularCoding == localCoding) : (paramElement + " // " + localBand);
/* 2010 */     if ((i != 0) && 
/* 2011 */       (!$assertionsDisabled) && (!(localBand instanceof IntBand))) throw new AssertionError();
/* 2012 */     return true;
/*      */   }
/*      */ 
/*      */   private Attribute.Layout predefineAttribute(int paramInt1, int paramInt2, Band[] paramArrayOfBand, String paramString1, String paramString2)
/*      */   {
/* 2019 */     Attribute.Layout localLayout = Attribute.find(paramInt2, paramString1, paramString2).layout();
/*      */ 
/* 2021 */     if (paramInt1 >= 0) {
/* 2022 */       setAttributeLayoutIndex(localLayout, paramInt1);
/*      */     }
/* 2024 */     if (paramArrayOfBand == null) {
/* 2025 */       paramArrayOfBand = new Band[0];
/*      */     }
/* 2027 */     assert (this.attrBandTable.get(localLayout) == null);
/* 2028 */     this.attrBandTable.put(localLayout, paramArrayOfBand);
/*      */ 
/* 2030 */     assert (localLayout.bandCount == paramArrayOfBand.length) : (localLayout + " // " + Arrays.asList(paramArrayOfBand));
/*      */ 
/* 2032 */     assert (assertBandOKForElems(paramArrayOfBand, localLayout.elems));
/* 2033 */     return localLayout;
/*      */   }
/*      */ 
/*      */   private Attribute.Layout predefineAttribute(int paramInt, String paramString, MultiBand paramMultiBand, Attribute paramAttribute)
/*      */   {
/* 2042 */     Attribute.Layout localLayout = paramAttribute.layout();
/* 2043 */     int i = localLayout.ctype();
/* 2044 */     return predefineAttribute(paramInt, i, makeNewAttributeBands(paramString, localLayout, paramMultiBand), localLayout.name(), localLayout.layout());
/*      */   }
/*      */ 
/*      */   private void undefineAttribute(int paramInt1, int paramInt2)
/*      */   {
/* 2052 */     if (this.verbose > 1) {
/* 2053 */       System.out.println("Removing predefined " + Constants.ATTR_CONTEXT_NAME[paramInt2] + " attribute on bit " + paramInt1);
/*      */     }
/*      */ 
/* 2056 */     List localList = (List)this.attrDefs.get(paramInt2);
/* 2057 */     Attribute.Layout localLayout = (Attribute.Layout)localList.get(paramInt1);
/* 2058 */     assert (localLayout != null);
/* 2059 */     localList.set(paramInt1, null);
/* 2060 */     this.attrIndexTable.put(localLayout, null);
/*      */ 
/* 2062 */     assert (paramInt1 < 64);
/* 2063 */     this.attrDefSeen[paramInt2] &= (1L << paramInt1 ^ 0xFFFFFFFF);
/* 2064 */     this.attrFlagMask[paramInt2] &= (1L << paramInt1 ^ 0xFFFFFFFF);
/* 2065 */     Band[] arrayOfBand = (Band[])this.attrBandTable.get(localLayout);
/* 2066 */     for (int i = 0; i < arrayOfBand.length; i++)
/* 2067 */       arrayOfBand[i].doneWithUnusedBand();
/*      */   }
/*      */ 
/*      */   void makeNewAttributeBands()
/*      */   {
/* 2083 */     adjustSpecialAttrMasks();
/*      */ 
/* 2085 */     for (int i = 0; i < 4; i++) {
/* 2086 */       String str1 = Constants.ATTR_CONTEXT_NAME[i];
/* 2087 */       MultiBand localMultiBand = this.attrBands[i];
/* 2088 */       long l = this.attrDefSeen[i];
/*      */ 
/* 2090 */       assert ((l & (this.attrFlagMask[i] ^ 0xFFFFFFFF)) == 0L);
/* 2091 */       for (int j = 0; j < ((List)this.attrDefs.get(i)).size(); j++) {
/* 2092 */         Attribute.Layout localLayout = (Attribute.Layout)((List)this.attrDefs.get(i)).get(j);
/* 2093 */         if ((localLayout != null) && 
/* 2094 */           (localLayout.bandCount != 0))
/* 2095 */           if ((j < this.attrIndexLimit[i]) && (!testBit(l, 1L << j)))
/*      */           {
/* 2097 */             if ((!$assertionsDisabled) && (this.attrBandTable.get(localLayout) == null)) throw new AssertionError(); 
/*      */           }
/*      */           else
/*      */           {
/* 2100 */             int k = localMultiBand.size();
/* 2101 */             String str2 = str1 + "_" + localLayout.name() + "_";
/* 2102 */             if (this.verbose > 1)
/* 2103 */               Utils.log.fine("Making new bands for " + localLayout);
/* 2104 */             Band[] arrayOfBand1 = makeNewAttributeBands(str2, localLayout, localMultiBand);
/*      */ 
/* 2106 */             assert (arrayOfBand1.length == localLayout.bandCount);
/* 2107 */             Band[] arrayOfBand2 = (Band[])this.attrBandTable.put(localLayout, arrayOfBand1);
/* 2108 */             if (arrayOfBand2 != null)
/*      */             {
/* 2110 */               for (int m = 0; m < arrayOfBand2.length; m++)
/* 2111 */                 arrayOfBand2[m].doneWithUnusedBand();
/*      */             }
/*      */           }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private Band[] makeNewAttributeBands(String paramString, Attribute.Layout paramLayout, MultiBand paramMultiBand)
/*      */   {
/* 2121 */     int i = paramMultiBand.size();
/* 2122 */     makeNewAttributeBands(paramString, paramLayout.elems, paramMultiBand);
/* 2123 */     int j = paramMultiBand.size() - i;
/* 2124 */     Band[] arrayOfBand = new Band[j];
/* 2125 */     for (int k = 0; k < j; k++) {
/* 2126 */       arrayOfBand[k] = paramMultiBand.get(i + k);
/*      */     }
/* 2128 */     return arrayOfBand;
/*      */   }
/*      */ 
/*      */   private void makeNewAttributeBands(String paramString, Attribute.Layout.Element[] paramArrayOfElement, MultiBand paramMultiBand)
/*      */   {
/* 2134 */     for (int i = 0; i < paramArrayOfElement.length; i++) {
/* 2135 */       Attribute.Layout.Element localElement = paramArrayOfElement[i];
/* 2136 */       String str = paramString + paramMultiBand.size() + "_" + localElement.layout;
/*      */       int j;
/* 2139 */       if ((j = str.indexOf('[')) > 0)
/* 2140 */         str = str.substring(0, j);
/* 2141 */       if ((j = str.indexOf('(')) > 0)
/* 2142 */         str = str.substring(0, j);
/* 2143 */       if (str.endsWith("H"))
/* 2144 */         str = str.substring(0, str.length() - 1);
/*      */       Object localObject;
/* 2147 */       switch (localElement.kind) {
/*      */       case 1:
/* 2149 */         localObject = newElemBand(localElement, str, paramMultiBand);
/* 2150 */         break;
/*      */       case 2:
/* 2152 */         if (!localElement.flagTest((byte)2))
/*      */         {
/* 2154 */           localObject = paramMultiBand.newIntBand(str, BCI5);
/*      */         }
/*      */         else {
/* 2157 */           localObject = paramMultiBand.newIntBand(str, BRANCH5);
/*      */         }
/*      */ 
/* 2160 */         break;
/*      */       case 3:
/* 2163 */         localObject = paramMultiBand.newIntBand(str, BRANCH5);
/*      */ 
/* 2165 */         break;
/*      */       case 4:
/* 2167 */         assert (!localElement.flagTest((byte)1));
/* 2168 */         localObject = newElemBand(localElement, str, paramMultiBand);
/* 2169 */         break;
/*      */       case 5:
/* 2171 */         assert (!localElement.flagTest((byte)1));
/* 2172 */         localObject = newElemBand(localElement, str, paramMultiBand);
/* 2173 */         makeNewAttributeBands(paramString, localElement.body, paramMultiBand);
/* 2174 */         break;
/*      */       case 7:
/* 2176 */         localObject = newElemBand(localElement, str, paramMultiBand);
/* 2177 */         makeNewAttributeBands(paramString, localElement.body, paramMultiBand);
/* 2178 */         break;
/*      */       case 8:
/* 2180 */         if (localElement.flagTest((byte)8))
/*      */           continue;
/* 2182 */         makeNewAttributeBands(paramString, localElement.body, paramMultiBand); break;
/*      */       case 6:
/* 2186 */         byte b = localElement.refKind;
/* 2187 */         boolean bool = localElement.flagTest((byte)4);
/* 2188 */         localObject = paramMultiBand.newCPRefBand(str, UNSIGNED5, b, bool);
/*      */ 
/* 2190 */         break;
/*      */       case 9:
/* 2192 */         break;
/*      */       case 10:
/* 2194 */         makeNewAttributeBands(paramString, localElement.body, paramMultiBand);
/* 2195 */         break;
/*      */       default:
/* 2196 */         if ($assertionsDisabled) continue; throw new AssertionError();
/*      */       }
/* 2198 */       if (this.verbose > 1)
/* 2199 */         Utils.log.fine("New attribute band " + localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Band newElemBand(Attribute.Layout.Element paramElement, String paramString, MultiBand paramMultiBand)
/*      */   {
/* 2205 */     if (paramElement.flagTest((byte)1))
/* 2206 */       return paramMultiBand.newIntBand(paramString, SIGNED5);
/* 2207 */     if (paramElement.len == 1) {
/* 2208 */       return paramMultiBand.newIntBand(paramString, BYTE1);
/*      */     }
/* 2210 */     return paramMultiBand.newIntBand(paramString, UNSIGNED5);
/*      */   }
/*      */ 
/*      */   protected int setAttributeLayoutIndex(Attribute.Layout paramLayout, int paramInt)
/*      */   {
/* 2215 */     int i = paramLayout.ctype;
/* 2216 */     assert ((-1 <= paramInt) && (paramInt < this.attrIndexLimit[i]));
/* 2217 */     List localList = (List)this.attrDefs.get(i);
/* 2218 */     if (paramInt == -1)
/*      */     {
/* 2220 */       paramInt = localList.size();
/* 2221 */       localList.add(paramLayout);
/* 2222 */       if (this.verbose > 0)
/* 2223 */         Utils.log.info("Adding new attribute at " + paramLayout + ": " + paramInt);
/* 2224 */       this.attrIndexTable.put(paramLayout, Integer.valueOf(paramInt));
/* 2225 */       return paramInt;
/*      */     }
/*      */ 
/* 2229 */     if (testBit(this.attrDefSeen[i], 1L << paramInt)) {
/* 2230 */       throw new RuntimeException("Multiple explicit definition at " + paramInt + ": " + paramLayout);
/*      */     }
/* 2232 */     this.attrDefSeen[i] |= 1L << paramInt;
/*      */ 
/* 2235 */     assert ((0 <= paramInt) && (paramInt < this.attrIndexLimit[i]));
/* 2236 */     if (this.verbose > (this.attrClassFileVersionMask == 0 ? 2 : 0)) {
/* 2237 */       Utils.log.fine("Fixing new attribute at " + paramInt + ": " + paramLayout + (localList.get(paramInt) == null ? "" : new StringBuilder().append("; replacing ").append(localList.get(paramInt)).toString()));
/*      */     }
/*      */ 
/* 2241 */     this.attrFlagMask[i] |= 1L << paramInt;
/*      */ 
/* 2243 */     this.attrIndexTable.put(localList.get(paramInt), null);
/* 2244 */     localList.set(paramInt, paramLayout);
/* 2245 */     this.attrIndexTable.put(paramLayout, Integer.valueOf(paramInt));
/* 2246 */     return paramInt;
/*      */   }
/*      */ 
/*      */   static int shortCodeHeader(Code paramCode)
/*      */   {
/* 2259 */     int i = paramCode.max_stack;
/* 2260 */     int j = paramCode.max_locals;
/* 2261 */     int k = paramCode.handler_class.length;
/* 2262 */     if (k >= shortCodeLimits.length) return 0;
/* 2263 */     int m = paramCode.getMethod().getArgumentSize();
/* 2264 */     assert (j >= m);
/* 2265 */     if (j < m) return 0;
/* 2266 */     int n = j - m;
/* 2267 */     int i1 = shortCodeLimits[k][0];
/* 2268 */     int i2 = shortCodeLimits[k][1];
/* 2269 */     if ((i >= i1) || (n >= i2)) return 0;
/* 2270 */     int i3 = shortCodeHeader_h_base(k);
/* 2271 */     i3 += i + i1 * n;
/* 2272 */     if (i3 > 255) return 0;
/* 2273 */     assert (shortCodeHeader_max_stack(i3) == i);
/* 2274 */     assert (shortCodeHeader_max_na_locals(i3) == n);
/* 2275 */     assert (shortCodeHeader_handler_count(i3) == k);
/* 2276 */     return i3;
/*      */   }
/*      */ 
/*      */   static int shortCodeHeader_handler_count(int paramInt)
/*      */   {
/* 2281 */     assert ((paramInt > 0) && (paramInt <= 255));
/* 2282 */     for (int i = 0; ; i++)
/* 2283 */       if (paramInt < shortCodeHeader_h_base(i + 1))
/* 2284 */         return i;
/*      */   }
/*      */ 
/*      */   static int shortCodeHeader_max_stack(int paramInt) {
/* 2288 */     int i = shortCodeHeader_handler_count(paramInt);
/* 2289 */     int j = shortCodeLimits[i][0];
/* 2290 */     return (paramInt - shortCodeHeader_h_base(i)) % j;
/*      */   }
/*      */   static int shortCodeHeader_max_na_locals(int paramInt) {
/* 2293 */     int i = shortCodeHeader_handler_count(paramInt);
/* 2294 */     int j = shortCodeLimits[i][0];
/* 2295 */     return (paramInt - shortCodeHeader_h_base(i)) / j;
/*      */   }
/*      */ 
/*      */   private static int shortCodeHeader_h_base(int paramInt) {
/* 2299 */     assert (paramInt <= shortCodeLimits.length);
/* 2300 */     int i = 1;
/* 2301 */     for (int j = 0; j < paramInt; j++) {
/* 2302 */       int k = shortCodeLimits[j][0];
/* 2303 */       int m = shortCodeLimits[j][1];
/* 2304 */       i += k * m;
/*      */     }
/* 2306 */     return i;
/*      */   }
/*      */ 
/*      */   protected void putLabel(IntBand paramIntBand, Code paramCode, int paramInt1, int paramInt2)
/*      */   {
/* 2311 */     paramIntBand.putInt(paramCode.encodeBCI(paramInt2) - paramCode.encodeBCI(paramInt1));
/*      */   }
/*      */   protected int getLabel(IntBand paramIntBand, Code paramCode, int paramInt) {
/* 2314 */     return paramCode.decodeBCI(paramIntBand.getInt() + paramCode.encodeBCI(paramInt));
/*      */   }
/*      */ 
/*      */   protected CPRefBand getCPRefOpBand(int paramInt) {
/* 2318 */     switch (Instruction.getCPRefOpTag(paramInt)) {
/*      */     case 7:
/* 2320 */       return this.bc_classref;
/*      */     case 9:
/* 2322 */       return this.bc_fieldref;
/*      */     case 10:
/* 2324 */       return this.bc_methodref;
/*      */     case 11:
/* 2326 */       return this.bc_imethodref;
/*      */     case 20:
/* 2328 */       switch (paramInt) { case 234:
/*      */       case 237:
/* 2330 */         return this.bc_intref;
/*      */       case 235:
/*      */       case 238:
/* 2332 */         return this.bc_floatref;
/*      */       case 20:
/* 2334 */         return this.bc_longref;
/*      */       case 239:
/* 2336 */         return this.bc_doubleref;
/*      */       case 18:
/*      */       case 19:
/* 2338 */         return this.bc_stringref;
/*      */       case 233:
/*      */       case 236:
/* 2340 */         return this.bc_classref; } break;
/*      */     case 8:
/*      */     case 12:
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/* 2344 */     case 19: } if (!$assertionsDisabled) throw new AssertionError();
/* 2345 */     return null;
/*      */   }
/*      */ 
/*      */   protected CPRefBand selfOpRefBand(int paramInt) {
/* 2349 */     assert (Instruction.isSelfLinkerOp(paramInt));
/* 2350 */     int i = paramInt - 202;
/* 2351 */     int j = i >= 14 ? 1 : 0;
/* 2352 */     if (j != 0) i -= 14;
/* 2353 */     int k = i >= 7 ? 1 : 0;
/* 2354 */     if (k != 0) i -= 7;
/* 2355 */     int m = 178 + i;
/* 2356 */     boolean bool = Instruction.isFieldOp(m);
/* 2357 */     if (j == 0) {
/* 2358 */       return bool ? this.bc_thisfield : this.bc_thismethod;
/*      */     }
/* 2360 */     return bool ? this.bc_superfield : this.bc_supermethod;
/*      */   }
/*      */ 
/*      */   static OutputStream getDumpStream(Band paramBand, String paramString)
/*      */     throws IOException
/*      */   {
/* 2368 */     return getDumpStream(paramBand.name, paramBand.seqForDebug, paramString, paramBand);
/*      */   }
/*      */   static OutputStream getDumpStream(ConstantPool.Index paramIndex, String paramString) throws IOException {
/* 2371 */     if (paramIndex.size() == 0) return new ByteArrayOutputStream();
/* 2372 */     int i = ConstantPool.TAG_ORDER[paramIndex.cpMap[0].tag];
/* 2373 */     return getDumpStream(paramIndex.debugName, i, paramString, paramIndex);
/*      */   }
/*      */   static OutputStream getDumpStream(String paramString1, int paramInt, String paramString2, Object paramObject) throws IOException {
/* 2376 */     if (dumpDir == null) {
/* 2377 */       dumpDir = File.createTempFile("BD_", "", new File("."));
/* 2378 */       dumpDir.delete();
/* 2379 */       if (dumpDir.mkdir())
/* 2380 */         Utils.log.info("Dumping bands to " + dumpDir);
/*      */     }
/* 2382 */     paramString1 = paramString1.replace('(', ' ').replace(')', ' ');
/* 2383 */     paramString1 = paramString1.replace('/', ' ');
/* 2384 */     paramString1 = paramString1.replace('*', ' ');
/* 2385 */     paramString1 = paramString1.trim().replace(' ', '_');
/* 2386 */     paramString1 = (10000 + paramInt + "_" + paramString1).substring(1);
/* 2387 */     File localFile = new File(dumpDir, paramString1 + paramString2);
/* 2388 */     Utils.log.info("Dumping " + paramObject + " to " + localFile);
/* 2389 */     return new BufferedOutputStream(new FileOutputStream(localFile));
/*      */   }
/*      */ 
/*      */   static boolean assertCanChangeLength(Band paramBand)
/*      */   {
/* 2394 */     switch (paramBand.phase) {
/*      */     case 1:
/*      */     case 4:
/* 2397 */       return true;
/*      */     }
/* 2399 */     return false;
/*      */   }
/*      */ 
/*      */   static boolean assertPhase(Band paramBand, int paramInt)
/*      */   {
/* 2404 */     if (paramBand.phase() != paramInt) {
/* 2405 */       Utils.log.warning("phase expected " + paramInt + " was " + paramBand.phase() + " in " + paramBand);
/* 2406 */       return false;
/*      */     }
/* 2408 */     return true;
/*      */   }
/*      */ 
/*      */   static int verbose()
/*      */   {
/* 2414 */     return Utils.currentPropMap().getInteger("com.sun.java.util.jar.pack.verbose");
/*      */   }
/*      */ 
/*      */   static boolean assertPhaseChangeOK(Band paramBand, int paramInt1, int paramInt2)
/*      */   {
/* 2420 */     switch (paramInt1 * 10 + paramInt2)
/*      */     {
/*      */     case 1:
/* 2424 */       assert (!paramBand.isReader());
/* 2425 */       assert (paramBand.capacity() >= 0);
/* 2426 */       assert (paramBand.length() == 0);
/* 2427 */       return true;
/*      */     case 13:
/*      */     case 33:
/* 2430 */       assert (paramBand.length() == 0);
/* 2431 */       return true;
/*      */     case 15:
/*      */     case 35:
/* 2435 */       return true;
/*      */     case 58:
/* 2438 */       return true;
/*      */     case 2:
/* 2442 */       assert (paramBand.isReader());
/* 2443 */       assert (paramBand.capacity() < 0);
/* 2444 */       return true;
/*      */     case 24:
/* 2447 */       assert (Math.max(0, paramBand.capacity()) >= paramBand.valuesExpected());
/* 2448 */       assert (paramBand.length() <= 0);
/* 2449 */       return true;
/*      */     case 46:
/* 2452 */       assert (paramBand.valuesRemainingForDebug() == paramBand.length());
/* 2453 */       return true;
/*      */     case 68:
/* 2456 */       assert (assertDoneDisbursing(paramBand));
/* 2457 */       return true;
/*      */     }
/* 2459 */     if (paramInt1 == paramInt2)
/* 2460 */       Utils.log.warning("Already in phase " + paramInt1);
/*      */     else
/* 2462 */       Utils.log.warning("Unexpected phase " + paramInt1 + " -> " + paramInt2);
/* 2463 */     return false;
/*      */   }
/*      */ 
/*      */   private static boolean assertDoneDisbursing(Band paramBand) {
/* 2467 */     if (paramBand.phase != 6) {
/* 2468 */       Utils.log.warning("assertDoneDisbursing: still in phase " + paramBand.phase + ": " + paramBand);
/* 2469 */       if (verbose() <= 1) return false;
/*      */     }
/* 2471 */     int i = paramBand.valuesRemainingForDebug();
/* 2472 */     if (i > 0) {
/* 2473 */       Utils.log.warning("assertDoneDisbursing: " + i + " values left in " + paramBand);
/* 2474 */       if (verbose() <= 1) return false;
/*      */     }
/* 2476 */     if ((paramBand instanceof MultiBand)) {
/* 2477 */       MultiBand localMultiBand = (MultiBand)paramBand;
/* 2478 */       for (int j = 0; j < localMultiBand.bandCount; j++) {
/* 2479 */         Band localBand = localMultiBand.bands[j];
/* 2480 */         if (localBand.phase != 8) {
/* 2481 */           Utils.log.warning("assertDoneDisbursing: sub-band still in phase " + localBand.phase + ": " + localBand);
/* 2482 */           if (verbose() <= 1) return false;
/*      */         }
/*      */       }
/*      */     }
/* 2486 */     return true;
/*      */   }
/*      */ 
/*      */   private static void printCDecl(Band paramBand) {
/* 2490 */     if ((paramBand instanceof MultiBand)) {
/* 2491 */       localObject1 = (MultiBand)paramBand;
/* 2492 */       for (int i = 0; i < ((MultiBand)localObject1).bandCount; i++) {
/* 2493 */         printCDecl(localObject1.bands[i]);
/*      */       }
/* 2495 */       return;
/*      */     }
/* 2497 */     Object localObject1 = "NULL";
/* 2498 */     if ((paramBand instanceof CPRefBand)) {
/* 2499 */       localObject2 = ((CPRefBand)paramBand).index;
/* 2500 */       if (localObject2 != null) localObject1 = "INDEX(" + ((ConstantPool.Index)localObject2).debugName + ")";
/*      */     }
/* 2502 */     Object localObject2 = { BYTE1, CHAR3, BCI5, BRANCH5, UNSIGNED5, UDELTA5, SIGNED5, DELTA5, MDELTA5 };
/*      */ 
/* 2504 */     String[] arrayOfString = { "BYTE1", "CHAR3", "BCI5", "BRANCH5", "UNSIGNED5", "UDELTA5", "SIGNED5", "DELTA5", "MDELTA5" };
/*      */ 
/* 2506 */     Coding localCoding = paramBand.regularCoding;
/* 2507 */     int j = Arrays.asList((Object[])localObject2).indexOf(localCoding);
/*      */     String str;
/* 2509 */     if (j >= 0)
/* 2510 */       str = arrayOfString[j];
/*      */     else
/* 2512 */       str = "CODING" + localCoding.keyString();
/* 2513 */     System.out.println("  BAND_INIT(\"" + paramBand.name() + "\"" + ", " + str + ", " + (String)localObject1 + "),");
/*      */   }
/*      */ 
/*      */   boolean notePrevForAssert(Band paramBand1, Band paramBand2)
/*      */   {
/* 2521 */     if (this.prevForAssertMap == null)
/* 2522 */       this.prevForAssertMap = new HashMap();
/* 2523 */     this.prevForAssertMap.put(paramBand1, paramBand2);
/* 2524 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean assertReadyToReadFrom(Band paramBand, InputStream paramInputStream) throws IOException
/*      */   {
/* 2529 */     Band localBand = (Band)this.prevForAssertMap.get(paramBand);
/*      */ 
/* 2531 */     if ((localBand != null) && (phaseCmp(localBand.phase(), 6) < 0)) {
/* 2532 */       Utils.log.warning("Previous band not done reading.");
/* 2533 */       Utils.log.info("    Previous band: " + localBand);
/* 2534 */       Utils.log.info("        Next band: " + paramBand);
/* 2535 */       Thread.dumpStack();
/* 2536 */       assert (this.verbose > 0);
/*      */     }
/* 2538 */     String str1 = paramBand.name;
/* 2539 */     if ((this.optDebugBands) && (!str1.startsWith("(")))
/*      */     {
/* 2541 */       StringBuilder localStringBuilder1 = new StringBuilder();
/*      */       int i;
/* 2543 */       while ((i = paramInputStream.read()) > 0)
/* 2544 */         localStringBuilder1.append((char)i);
/* 2545 */       String str2 = localStringBuilder1.toString();
/* 2546 */       if (!str2.equals(str1)) {
/* 2547 */         StringBuilder localStringBuilder2 = new StringBuilder();
/* 2548 */         localStringBuilder2.append("Expected " + str1 + " but read: ");
/* 2549 */         str2 = str2 + (char)i;
/* 2550 */         while (str2.length() < 10) {
/* 2551 */           str2 = str2 + (char)paramInputStream.read();
/*      */         }
/* 2553 */         for (int j = 0; j < str2.length(); j++) {
/* 2554 */           localStringBuilder2.append(str2.charAt(j));
/*      */         }
/* 2556 */         Utils.log.warning(localStringBuilder2.toString());
/* 2557 */         return false;
/*      */       }
/*      */     }
/* 2560 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean assertValidCPRefs(CPRefBand paramCPRefBand)
/*      */   {
/* 2565 */     if (paramCPRefBand.index == null) return true;
/* 2566 */     int i = paramCPRefBand.index.size() + 1;
/* 2567 */     for (int j = 0; j < paramCPRefBand.length(); j++) {
/* 2568 */       int k = paramCPRefBand.valueAtForDebug(j);
/* 2569 */       if ((k < 0) || (k >= i)) {
/* 2570 */         Utils.log.warning("CP ref out of range [" + j + "] = " + k + " in " + paramCPRefBand);
/*      */ 
/* 2572 */         return false;
/*      */       }
/*      */     }
/* 2575 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean assertReadyToWriteTo(Band paramBand, OutputStream paramOutputStream) throws IOException
/*      */   {
/* 2580 */     Band localBand = (Band)this.prevForAssertMap.get(paramBand);
/*      */ 
/* 2582 */     if ((localBand != null) && (phaseCmp(localBand.phase(), 8) < 0)) {
/* 2583 */       Utils.log.warning("Previous band not done writing.");
/* 2584 */       Utils.log.info("    Previous band: " + localBand);
/* 2585 */       Utils.log.info("        Next band: " + paramBand);
/* 2586 */       Thread.dumpStack();
/* 2587 */       assert (this.verbose > 0);
/*      */     }
/* 2589 */     String str = paramBand.name;
/* 2590 */     if ((this.optDebugBands) && (!str.startsWith("(")))
/*      */     {
/* 2592 */       for (int i = 0; i < str.length(); i++) {
/* 2593 */         paramOutputStream.write((byte)str.charAt(i));
/*      */       }
/* 2595 */       paramOutputStream.write(0);
/*      */     }
/* 2597 */     return true;
/*      */   }
/*      */ 
/*      */   protected static boolean testBit(int paramInt1, int paramInt2) {
/* 2601 */     return (paramInt1 & paramInt2) != 0;
/*      */   }
/*      */   protected static int setBit(int paramInt1, int paramInt2, boolean paramBoolean) {
/* 2604 */     return paramBoolean ? paramInt1 | paramInt2 : paramInt1 & (paramInt2 ^ 0xFFFFFFFF);
/*      */   }
/*      */   protected static boolean testBit(long paramLong1, long paramLong2) {
/* 2607 */     return (paramLong1 & paramLong2) != 0L;
/*      */   }
/*      */   protected static long setBit(long paramLong1, long paramLong2, boolean paramBoolean) {
/* 2610 */     return paramBoolean ? paramLong1 | paramLong2 : paramLong1 & (paramLong2 ^ 0xFFFFFFFF);
/*      */   }
/*      */ 
/*      */   static void printArrayTo(PrintStream paramPrintStream, int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*      */   {
/* 2615 */     int i = paramInt2 - paramInt1;
/* 2616 */     for (int j = 0; j < i; j++) {
/* 2617 */       if (j % 10 == 0)
/* 2618 */         paramPrintStream.println();
/*      */       else
/* 2620 */         paramPrintStream.print(" ");
/* 2621 */       paramPrintStream.print(paramArrayOfInt[(paramInt1 + j)]);
/*      */     }
/* 2623 */     paramPrintStream.println();
/*      */   }
/*      */ 
/*      */   static void printArrayTo(PrintStream paramPrintStream, ConstantPool.Entry[] paramArrayOfEntry, int paramInt1, int paramInt2) {
/* 2627 */     StringBuffer localStringBuffer = new StringBuffer();
/* 2628 */     int i = paramInt2 - paramInt1;
/* 2629 */     for (int j = 0; j < i; j++) {
/* 2630 */       String str = paramArrayOfEntry[(paramInt1 + j)].stringValue();
/* 2631 */       localStringBuffer.setLength(0);
/* 2632 */       for (int k = 0; k < str.length(); k++) {
/* 2633 */         char c = str.charAt(k);
/* 2634 */         if ((c >= ' ') && (c <= '~') && (c != '\\'))
/* 2635 */           localStringBuffer.append(c);
/* 2636 */         else if (c == '\n')
/* 2637 */           localStringBuffer.append("\\n");
/* 2638 */         else if (c == '\t')
/* 2639 */           localStringBuffer.append("\\t");
/* 2640 */         else if (c == '\r')
/* 2641 */           localStringBuffer.append("\\r");
/*      */         else {
/* 2643 */           localStringBuffer.append("\\x" + Integer.toHexString(c));
/*      */         }
/*      */       }
/* 2646 */       paramPrintStream.println(localStringBuffer);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static Object[] realloc(Object[] paramArrayOfObject, int paramInt)
/*      */   {
/* 2653 */     Class localClass = paramArrayOfObject.getClass().getComponentType();
/* 2654 */     Object[] arrayOfObject = (Object[])Array.newInstance(localClass, paramInt);
/* 2655 */     System.arraycopy(paramArrayOfObject, 0, arrayOfObject, 0, Math.min(paramArrayOfObject.length, paramInt));
/* 2656 */     return arrayOfObject;
/*      */   }
/*      */   protected static Object[] realloc(Object[] paramArrayOfObject) {
/* 2659 */     return realloc(paramArrayOfObject, Math.max(10, paramArrayOfObject.length * 2));
/*      */   }
/*      */ 
/*      */   protected static int[] realloc(int[] paramArrayOfInt, int paramInt) {
/* 2663 */     if (paramInt == 0) return Constants.noInts;
/* 2664 */     if (paramArrayOfInt == null) return new int[paramInt];
/* 2665 */     int[] arrayOfInt = new int[paramInt];
/* 2666 */     System.arraycopy(paramArrayOfInt, 0, arrayOfInt, 0, Math.min(paramArrayOfInt.length, paramInt));
/* 2667 */     return arrayOfInt;
/*      */   }
/*      */   protected static int[] realloc(int[] paramArrayOfInt) {
/* 2670 */     return realloc(paramArrayOfInt, Math.max(10, paramArrayOfInt.length * 2));
/*      */   }
/*      */ 
/*      */   protected static byte[] realloc(byte[] paramArrayOfByte, int paramInt) {
/* 2674 */     if (paramInt == 0) return Constants.noBytes;
/* 2675 */     if (paramArrayOfByte == null) return new byte[paramInt];
/* 2676 */     byte[] arrayOfByte = new byte[paramInt];
/* 2677 */     System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, Math.min(paramArrayOfByte.length, paramInt));
/* 2678 */     return arrayOfByte;
/*      */   }
/*      */   protected static byte[] realloc(byte[] paramArrayOfByte) {
/* 2681 */     return realloc(paramArrayOfByte, Math.max(10, paramArrayOfByte.length * 2));
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  102 */     BYTE1 = Coding.of(1, 256);
/*      */ 
/*  104 */     CHAR3 = Coding.of(3, 128);
/*      */ 
/*  108 */     BCI5 = Coding.of(5, 4);
/*  109 */     BRANCH5 = Coding.of(5, 4, 2);
/*      */ 
/*  111 */     UNSIGNED5 = Coding.of(5, 64);
/*  112 */     UDELTA5 = UNSIGNED5.getDeltaCoding();
/*      */ 
/*  116 */     SIGNED5 = Coding.of(5, 64, 1);
/*  117 */     DELTA5 = SIGNED5.getDeltaCoding();
/*      */ 
/*  120 */     MDELTA5 = Coding.of(5, 64, 2).getDeltaCoding();
/*      */ 
/*  122 */     basicCodings = new Coding[] { null, Coding.of(1, 256, 0), Coding.of(1, 256, 1), Coding.of(1, 256, 0).getDeltaCoding(), Coding.of(1, 256, 1).getDeltaCoding(), Coding.of(2, 256, 0), Coding.of(2, 256, 1), Coding.of(2, 256, 0).getDeltaCoding(), Coding.of(2, 256, 1).getDeltaCoding(), Coding.of(3, 256, 0), Coding.of(3, 256, 1), Coding.of(3, 256, 0).getDeltaCoding(), Coding.of(3, 256, 1).getDeltaCoding(), Coding.of(4, 256, 0), Coding.of(4, 256, 1), Coding.of(4, 256, 0).getDeltaCoding(), Coding.of(4, 256, 1).getDeltaCoding(), Coding.of(5, 4, 0), Coding.of(5, 4, 1), Coding.of(5, 4, 2), Coding.of(5, 16, 0), Coding.of(5, 16, 1), Coding.of(5, 16, 2), Coding.of(5, 32, 0), Coding.of(5, 32, 1), Coding.of(5, 32, 2), Coding.of(5, 64, 0), Coding.of(5, 64, 1), Coding.of(5, 64, 2), Coding.of(5, 128, 0), Coding.of(5, 128, 1), Coding.of(5, 128, 2), Coding.of(5, 4, 0).getDeltaCoding(), Coding.of(5, 4, 1).getDeltaCoding(), Coding.of(5, 4, 2).getDeltaCoding(), Coding.of(5, 16, 0).getDeltaCoding(), Coding.of(5, 16, 1).getDeltaCoding(), Coding.of(5, 16, 2).getDeltaCoding(), Coding.of(5, 32, 0).getDeltaCoding(), Coding.of(5, 32, 1).getDeltaCoding(), Coding.of(5, 32, 2).getDeltaCoding(), Coding.of(5, 64, 0).getDeltaCoding(), Coding.of(5, 64, 1).getDeltaCoding(), Coding.of(5, 64, 2).getDeltaCoding(), Coding.of(5, 128, 0).getDeltaCoding(), Coding.of(5, 128, 1).getDeltaCoding(), Coding.of(5, 128, 2).getDeltaCoding(), Coding.of(2, 192, 0), Coding.of(2, 224, 0), Coding.of(2, 240, 0), Coding.of(2, 248, 0), Coding.of(2, 252, 0), Coding.of(2, 8, 0).getDeltaCoding(), Coding.of(2, 8, 1).getDeltaCoding(), Coding.of(2, 16, 0).getDeltaCoding(), Coding.of(2, 16, 1).getDeltaCoding(), Coding.of(2, 32, 0).getDeltaCoding(), Coding.of(2, 32, 1).getDeltaCoding(), Coding.of(2, 64, 0).getDeltaCoding(), Coding.of(2, 64, 1).getDeltaCoding(), Coding.of(2, 128, 0).getDeltaCoding(), Coding.of(2, 128, 1).getDeltaCoding(), Coding.of(2, 192, 0).getDeltaCoding(), Coding.of(2, 192, 1).getDeltaCoding(), Coding.of(2, 224, 0).getDeltaCoding(), Coding.of(2, 224, 1).getDeltaCoding(), Coding.of(2, 240, 0).getDeltaCoding(), Coding.of(2, 240, 1).getDeltaCoding(), Coding.of(2, 248, 0).getDeltaCoding(), Coding.of(2, 248, 1).getDeltaCoding(), Coding.of(3, 192, 0), Coding.of(3, 224, 0), Coding.of(3, 240, 0), Coding.of(3, 248, 0), Coding.of(3, 252, 0), Coding.of(3, 8, 0).getDeltaCoding(), Coding.of(3, 8, 1).getDeltaCoding(), Coding.of(3, 16, 0).getDeltaCoding(), Coding.of(3, 16, 1).getDeltaCoding(), Coding.of(3, 32, 0).getDeltaCoding(), Coding.of(3, 32, 1).getDeltaCoding(), Coding.of(3, 64, 0).getDeltaCoding(), Coding.of(3, 64, 1).getDeltaCoding(), Coding.of(3, 128, 0).getDeltaCoding(), Coding.of(3, 128, 1).getDeltaCoding(), Coding.of(3, 192, 0).getDeltaCoding(), Coding.of(3, 192, 1).getDeltaCoding(), Coding.of(3, 224, 0).getDeltaCoding(), Coding.of(3, 224, 1).getDeltaCoding(), Coding.of(3, 240, 0).getDeltaCoding(), Coding.of(3, 240, 1).getDeltaCoding(), Coding.of(3, 248, 0).getDeltaCoding(), Coding.of(3, 248, 1).getDeltaCoding(), Coding.of(4, 192, 0), Coding.of(4, 224, 0), Coding.of(4, 240, 0), Coding.of(4, 248, 0), Coding.of(4, 252, 0), Coding.of(4, 8, 0).getDeltaCoding(), Coding.of(4, 8, 1).getDeltaCoding(), Coding.of(4, 16, 0).getDeltaCoding(), Coding.of(4, 16, 1).getDeltaCoding(), Coding.of(4, 32, 0).getDeltaCoding(), Coding.of(4, 32, 1).getDeltaCoding(), Coding.of(4, 64, 0).getDeltaCoding(), Coding.of(4, 64, 1).getDeltaCoding(), Coding.of(4, 128, 0).getDeltaCoding(), Coding.of(4, 128, 1).getDeltaCoding(), Coding.of(4, 192, 0).getDeltaCoding(), Coding.of(4, 192, 1).getDeltaCoding(), Coding.of(4, 224, 0).getDeltaCoding(), Coding.of(4, 224, 1).getDeltaCoding(), Coding.of(4, 240, 0).getDeltaCoding(), Coding.of(4, 240, 1).getDeltaCoding(), Coding.of(4, 248, 0).getDeltaCoding(), Coding.of(4, 248, 1).getDeltaCoding(), null };
/*      */ 
/*  257 */     assert (basicCodings[0] == null);
/*  258 */     assert (basicCodings[1] != null);
/*  259 */     assert (basicCodings[115] != null);
/*  260 */     HashMap localHashMap = new HashMap();
/*      */     Coding localCoding;
/*  261 */     for (int j = 0; j < basicCodings.length; j++) {
/*  262 */       localCoding = basicCodings[j];
/*  263 */       if (localCoding != null) {
/*  264 */         assert (j >= 1);
/*  265 */         assert (j <= 115);
/*  266 */         localHashMap.put(localCoding, Integer.valueOf(j));
/*      */       }
/*      */     }
/*  268 */     basicCodingIndexes = localHashMap;
/*      */ 
/* 1106 */     defaultMetaCoding = new byte[] { 0 };
/* 1107 */     noMetaCoding = new byte[0];
/*      */ 
/* 1169 */     int i = 0;
/* 1170 */     assert ((i = 1) != 0);
/* 1171 */     if (i != 0)
/* 1172 */       for (j = 0; j < basicCodings.length; j++) {
/* 1173 */         localCoding = basicCodings[j];
/* 1174 */         if ((localCoding != null) && 
/* 1175 */           (localCoding.B() != 1) && 
/* 1176 */           (localCoding.L() != 0))
/* 1177 */           for (int k = 0; k <= 255; k++)
/*      */           {
/* 1179 */             encodeEscapeValue(k, localCoding);
/*      */           }
/*      */       }
/*      */   }
/*      */ 
/*      */   abstract class Band
/*      */   {
/*  360 */     private int phase = 0;
/*      */     private final String name;
/*      */     private int valuesExpected;
/*  365 */     protected long outputSize = -1L;
/*      */     public final Coding regularCoding;
/*      */     public final int seqForDebug;
/*      */     public int elementCountForDebug;
/*  500 */     protected int lengthForDebug = -1;
/*      */ 
/*      */     protected Band(String paramCoding, Coding arg3)
/*      */     {
/*  374 */       this.name = paramCoding;
/*      */       Object localObject;
/*  375 */       this.regularCoding = localObject;
/*  376 */       this.seqForDebug = (++BandStructure.nextSeqForDebug);
/*  377 */       if (BandStructure.this.verbose > 2)
/*  378 */         Utils.log.fine("Band " + this.seqForDebug + " is " + paramCoding);
/*      */     }
/*      */ 
/*      */     public Band init()
/*      */     {
/*  386 */       if (BandStructure.this.isReader)
/*  387 */         readyToExpect();
/*      */       else
/*  389 */         readyToCollect();
/*  390 */       return this;
/*      */     }
/*      */ 
/*      */     boolean isReader() {
/*  394 */       return BandStructure.this.isReader; } 
/*  395 */     int phase() { return this.phase; } 
/*  396 */     String name() { return this.name; }
/*      */ 
/*      */ 
/*      */     public abstract int capacity();
/*      */ 
/*      */     protected abstract void setCapacity(int paramInt);
/*      */ 
/*      */     public abstract int length();
/*      */ 
/*      */     protected abstract int valuesRemainingForDebug();
/*      */ 
/*      */     public final int valuesExpected()
/*      */     {
/*  410 */       return this.valuesExpected;
/*      */     }
/*      */ 
/*      */     public final void writeTo(OutputStream paramOutputStream) throws IOException
/*      */     {
/*  415 */       assert (BandStructure.this.assertReadyToWriteTo(this, paramOutputStream));
/*  416 */       setPhase(5);
/*      */ 
/*  418 */       writeDataTo(paramOutputStream);
/*  419 */       doneWriting();
/*      */     }
/*      */ 
/*      */     abstract void chooseBandCodings() throws IOException;
/*      */ 
/*      */     public final long outputSize() {
/*  425 */       if (this.outputSize >= 0L) {
/*  426 */         long l = this.outputSize;
/*  427 */         assert (l == computeOutputSize());
/*  428 */         return l;
/*      */       }
/*  430 */       return computeOutputSize();
/*      */     }
/*      */ 
/*      */     protected abstract long computeOutputSize();
/*      */ 
/*      */     protected abstract void writeDataTo(OutputStream paramOutputStream) throws IOException;
/*      */ 
/*      */     void expectLength(int paramInt)
/*      */     {
/*  439 */       assert (BandStructure.assertPhase(this, 2));
/*  440 */       assert (this.valuesExpected == 0);
/*  441 */       assert (paramInt >= 0);
/*  442 */       this.valuesExpected = paramInt;
/*      */     }
/*      */ 
/*      */     void expectMoreLength(int paramInt) {
/*  446 */       assert (BandStructure.assertPhase(this, 2));
/*  447 */       this.valuesExpected += paramInt;
/*      */     }
/*      */ 
/*      */     private void readyToCollect()
/*      */     {
/*  454 */       setCapacity(1);
/*  455 */       setPhase(1);
/*      */     }
/*      */     protected void doneWriting() {
/*  458 */       assert (BandStructure.assertPhase(this, 5));
/*  459 */       setPhase(8);
/*      */     }
/*      */     private void readyToExpect() {
/*  462 */       setPhase(2);
/*      */     }
/*      */ 
/*      */     public final void readFrom(InputStream paramInputStream) throws IOException {
/*  466 */       assert (BandStructure.this.assertReadyToReadFrom(this, paramInputStream));
/*  467 */       setCapacity(valuesExpected());
/*  468 */       setPhase(4);
/*      */ 
/*  470 */       readDataFrom(paramInputStream);
/*  471 */       readyToDisburse();
/*      */     }
/*      */     protected abstract void readDataFrom(InputStream paramInputStream) throws IOException;
/*      */ 
/*  475 */     protected void readyToDisburse() { if (BandStructure.this.verbose > 1) Utils.log.fine("readyToDisburse " + this);
/*  476 */       setPhase(6); }
/*      */ 
/*      */     public void doneDisbursing() {
/*  479 */       assert (BandStructure.assertPhase(this, 6));
/*  480 */       setPhase(8);
/*      */     }
/*      */     public final void doneWithUnusedBand() {
/*  483 */       if (BandStructure.this.isReader) {
/*  484 */         assert (BandStructure.assertPhase(this, 2));
/*  485 */         assert (valuesExpected() == 0);
/*      */ 
/*  487 */         setPhase(4);
/*  488 */         setPhase(6);
/*  489 */         setPhase(8);
/*      */       } else {
/*  491 */         setPhase(3);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void setPhase(int paramInt) {
/*  496 */       assert (BandStructure.assertPhaseChangeOK(this, this.phase, paramInt));
/*  497 */       this.phase = paramInt;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  502 */       int i = this.lengthForDebug != -1 ? this.lengthForDebug : length();
/*  503 */       String str = this.name;
/*  504 */       if (i != 0)
/*  505 */         str = str + "[" + i + "]";
/*  506 */       if (this.elementCountForDebug != 0)
/*  507 */         str = str + "(" + this.elementCountForDebug + ")";
/*  508 */       return str;
/*      */     }
/*      */   }
/*      */ 
/*      */   class ByteBand extends BandStructure.Band
/*      */   {
/*      */     private ByteArrayOutputStream bytes;
/*      */     private ByteArrayOutputStream bytesForDump;
/*      */     private InputStream in;
/*      */ 
/*      */     public ByteBand(String arg2)
/*      */     {
/*  787 */       super(str, BandStructure.BYTE1);
/*      */     }
/*      */ 
/*      */     public int capacity() {
/*  791 */       return this.bytes == null ? -1 : 2147483647;
/*      */     }
/*      */     protected void setCapacity(int paramInt) {
/*  794 */       assert (this.bytes == null);
/*  795 */       this.bytes = new ByteArrayOutputStream(paramInt);
/*      */     }
/*      */     public void destroy() {
/*  798 */       this.lengthForDebug = length();
/*  799 */       this.bytes = null;
/*      */     }
/*      */ 
/*      */     public int length() {
/*  803 */       return this.bytes == null ? -1 : this.bytes.size();
/*      */     }
/*      */     public void reset() {
/*  806 */       this.bytes.reset();
/*      */     }
/*      */     protected int valuesRemainingForDebug() {
/*  809 */       return this.bytes == null ? -1 : ((ByteArrayInputStream)this.in).available();
/*      */     }
/*      */ 
/*      */     protected void chooseBandCodings() throws IOException
/*      */     {
/*  814 */       assert (BandStructure.decodeEscapeValue(this.regularCoding.min(), this.regularCoding) < 0);
/*  815 */       assert (BandStructure.decodeEscapeValue(this.regularCoding.max(), this.regularCoding) < 0);
/*      */     }
/*      */ 
/*      */     protected long computeOutputSize()
/*      */     {
/*  820 */       return this.bytes.size();
/*      */     }
/*      */ 
/*      */     public void writeDataTo(OutputStream paramOutputStream) throws IOException {
/*  824 */       if (length() == 0) return;
/*  825 */       this.bytes.writeTo(paramOutputStream);
/*  826 */       if (BandStructure.this.optDumpBands) dumpBand();
/*  827 */       destroy();
/*      */     }
/*      */ 
/*      */     private void dumpBand() throws IOException {
/*  831 */       assert (BandStructure.this.optDumpBands);
/*  832 */       OutputStream localOutputStream = BandStructure.getDumpStream(this, ".bnd"); Object localObject1 = null;
/*      */       try { if (this.bytesForDump != null)
/*  834 */           this.bytesForDump.writeTo(localOutputStream);
/*      */         else
/*  836 */           this.bytes.writeTo(localOutputStream);
/*      */       }
/*      */       catch (Throwable localThrowable2)
/*      */       {
/*  832 */         localObject1 = localThrowable2; throw localThrowable2;
/*      */       }
/*      */       finally
/*      */       {
/*  837 */         if (localOutputStream != null) if (localObject1 != null) try { localOutputStream.close(); } catch (Throwable localThrowable3) { localObject1.addSuppressed(localThrowable3); } else localOutputStream.close();  
/*      */       }
/*      */     }
/*      */ 
/*      */     public void readDataFrom(InputStream paramInputStream) throws IOException {
/*  841 */       int i = valuesExpected();
/*  842 */       if (i == 0) return;
/*  843 */       if (BandStructure.this.verbose > 1) {
/*  844 */         this.lengthForDebug = i;
/*  845 */         Utils.log.fine("Reading band " + this);
/*  846 */         this.lengthForDebug = -1;
/*      */       }
/*  848 */       byte[] arrayOfByte = new byte[Math.min(i, 16384)];
/*  849 */       while (i > 0) {
/*  850 */         int j = paramInputStream.read(arrayOfByte, 0, Math.min(i, arrayOfByte.length));
/*  851 */         if (j < 0) throw new EOFException();
/*  852 */         this.bytes.write(arrayOfByte, 0, j);
/*  853 */         i -= j;
/*      */       }
/*  855 */       if (BandStructure.this.optDumpBands) dumpBand(); 
/*      */     }
/*      */ 
/*      */     public void readyToDisburse()
/*      */     {
/*  859 */       this.in = new ByteArrayInputStream(this.bytes.toByteArray());
/*  860 */       super.readyToDisburse();
/*      */     }
/*      */ 
/*      */     public void doneDisbursing() {
/*  864 */       super.doneDisbursing();
/*  865 */       if ((BandStructure.this.optDumpBands) && (this.bytesForDump != null) && (this.bytesForDump.size() > 0)) {
/*      */         try
/*      */         {
/*  868 */           dumpBand();
/*      */         } catch (IOException localIOException) {
/*  870 */           throw new RuntimeException(localIOException);
/*      */         }
/*      */       }
/*  873 */       this.in = null;
/*  874 */       this.bytes = null;
/*  875 */       this.bytesForDump = null;
/*      */     }
/*      */ 
/*      */     public void setInputStreamFrom(InputStream paramInputStream) throws IOException
/*      */     {
/*  880 */       assert (this.bytes == null);
/*  881 */       assert (BandStructure.this.assertReadyToReadFrom(this, paramInputStream));
/*  882 */       setPhase(4);
/*  883 */       this.in = paramInputStream;
/*  884 */       if (BandStructure.this.optDumpBands)
/*      */       {
/*  886 */         this.bytesForDump = new ByteArrayOutputStream();
/*  887 */         this.in = new FilterInputStream(paramInputStream) {
/*      */           public int read() throws IOException {
/*  889 */             int i = this.in.read();
/*  890 */             if (i >= 0) BandStructure.ByteBand.this.bytesForDump.write(i);
/*  891 */             return i;
/*      */           }
/*      */           public int read(byte[] paramAnonymousArrayOfByte, int paramAnonymousInt1, int paramAnonymousInt2) throws IOException {
/*  894 */             int i = this.in.read(paramAnonymousArrayOfByte, paramAnonymousInt1, paramAnonymousInt2);
/*  895 */             if (i >= 0) BandStructure.ByteBand.this.bytesForDump.write(paramAnonymousArrayOfByte, paramAnonymousInt1, i);
/*  896 */             return i;
/*      */           }
/*      */         };
/*      */       }
/*  900 */       super.readyToDisburse();
/*      */     }
/*      */ 
/*      */     public OutputStream collectorStream() {
/*  904 */       assert (phase() == 1);
/*  905 */       assert (this.bytes != null);
/*  906 */       return this.bytes;
/*      */     }
/*      */ 
/*      */     public InputStream getInputStream() {
/*  910 */       assert (phase() == 6);
/*  911 */       assert (this.in != null);
/*  912 */       return this.in;
/*      */     }
/*      */     public int getByte() throws IOException {
/*  915 */       int i = getInputStream().read();
/*  916 */       if (i < 0) throw new EOFException();
/*  917 */       return i;
/*      */     }
/*      */     public void putByte(int paramInt) throws IOException {
/*  920 */       assert (paramInt == (paramInt & 0xFF));
/*  921 */       collectorStream().write(paramInt);
/*      */     }
/*      */     public String toString() {
/*  924 */       return "byte " + super.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ByteCounter extends FilterOutputStream
/*      */   {
/*      */     private long count;
/*      */ 
/*      */     public ByteCounter(OutputStream paramOutputStream)
/*      */     {
/* 1335 */       super();
/*      */     }
/*      */     public long getCount() {
/* 1338 */       return this.count; } 
/* 1339 */     public void setCount(long paramLong) { this.count = paramLong; }
/*      */ 
/*      */     public void write(int paramInt) throws IOException {
/* 1342 */       this.count += 1L;
/* 1343 */       if (this.out != null) this.out.write(paramInt); 
/*      */     }
/*      */ 
/* 1346 */     public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException { this.count += paramInt2;
/* 1347 */       if (this.out != null) this.out.write(paramArrayOfByte, paramInt1, paramInt2);  } 
/*      */     public String toString()
/*      */     {
/* 1350 */       return String.valueOf(getCount());
/*      */     }
/*      */   }
/*      */ 
/*      */   class CPRefBand extends BandStructure.ValueBand
/*      */   {
/*      */     ConstantPool.Index index;
/*      */     boolean nullOK;
/*      */ 
/*      */     public CPRefBand(String paramCoding, Coding paramByte, byte paramBoolean, boolean arg5)
/*      */     {
/*  983 */       super(paramCoding, paramByte);
/*      */       boolean bool;
/*  984 */       this.nullOK = bool;
/*  985 */       if (paramBoolean)
/*  986 */         BandStructure.this.setBandIndex(this, paramBoolean); 
/*      */     }
/*      */ 
/*  989 */     public CPRefBand(String paramCoding, Coding paramByte, byte arg4) { this(paramCoding, paramByte, b, false); }
/*      */ 
/*      */     public CPRefBand(String paramCoding, Coding paramObject, Object arg4) {
/*  992 */       this(paramCoding, paramObject, (byte)0, false);
/*      */     }
/*      */ 
/*      */     public void setIndex(ConstantPool.Index paramIndex) {
/*  996 */       this.index = paramIndex;
/*      */     }
/*      */ 
/*      */     protected void readDataFrom(InputStream paramInputStream) throws IOException {
/* 1000 */       super.readDataFrom(paramInputStream);
/* 1001 */       assert (BandStructure.this.assertValidCPRefs(this));
/*      */     }
/*      */ 
/*      */     public void putRef(ConstantPool.Entry paramEntry)
/*      */     {
/* 1006 */       addValue(encodeRefOrNull(paramEntry, this.index));
/*      */     }
/*      */     public void putRef(ConstantPool.Entry paramEntry, ConstantPool.Index paramIndex) {
/* 1009 */       assert (this.index == null);
/* 1010 */       addValue(encodeRefOrNull(paramEntry, paramIndex));
/*      */     }
/*      */     public void putRef(ConstantPool.Entry paramEntry, byte paramByte) {
/* 1013 */       putRef(paramEntry, BandStructure.this.getCPIndex(paramByte));
/*      */     }
/*      */ 
/*      */     public ConstantPool.Entry getRef() {
/* 1017 */       if (this.index == null) Utils.log.warning("No index for " + this);
/* 1018 */       assert (this.index != null);
/* 1019 */       return decodeRefOrNull(getValue(), this.index);
/*      */     }
/*      */     public ConstantPool.Entry getRef(ConstantPool.Index paramIndex) {
/* 1022 */       assert (this.index == null);
/* 1023 */       return decodeRefOrNull(getValue(), paramIndex);
/*      */     }
/*      */     public ConstantPool.Entry getRef(byte paramByte) {
/* 1026 */       return getRef(BandStructure.this.getCPIndex(paramByte));
/*      */     }
/*      */ 
/*      */     private int encodeRefOrNull(ConstantPool.Entry paramEntry, ConstantPool.Index paramIndex)
/*      */     {
/*      */       int i;
/* 1031 */       if (paramEntry == null)
/* 1032 */         i = -1;
/*      */       else {
/* 1034 */         i = BandStructure.this.encodeRef(paramEntry, paramIndex);
/*      */       }
/*      */ 
/* 1037 */       return (this.nullOK ? 1 : 0) + i;
/*      */     }
/*      */ 
/*      */     private ConstantPool.Entry decodeRefOrNull(int paramInt, ConstantPool.Index paramIndex) {
/* 1041 */       int i = paramInt - (this.nullOK ? 1 : 0);
/* 1042 */       if (i == -1) {
/* 1043 */         return null;
/*      */       }
/* 1045 */       return BandStructure.this.decodeRef(i, paramIndex);
/*      */     }
/*      */   }
/*      */ 
/*      */   class IntBand extends BandStructure.ValueBand
/*      */   {
/*      */     public IntBand(String paramCoding, Coding arg3)
/*      */     {
/*  931 */       super(paramCoding, localCoding);
/*      */     }
/*      */ 
/*      */     public void putInt(int paramInt) {
/*  935 */       assert (phase() == 1);
/*  936 */       addValue(paramInt);
/*      */     }
/*      */ 
/*      */     public int getInt() {
/*  940 */       return getValue();
/*      */     }
/*      */ 
/*      */     public int getIntTotal() {
/*  944 */       assert (phase() == 6);
/*      */ 
/*  946 */       assert (valuesRemainingForDebug() == length());
/*  947 */       int i = 0;
/*  948 */       for (int j = length(); j > 0; j--) {
/*  949 */         i += getInt();
/*      */       }
/*  951 */       resetForSecondPass();
/*  952 */       return i;
/*      */     }
/*      */ 
/*      */     public int getIntCount(int paramInt) {
/*  956 */       assert (phase() == 6);
/*      */ 
/*  958 */       assert (valuesRemainingForDebug() == length());
/*  959 */       int i = 0;
/*  960 */       for (int j = length(); j > 0; j--) {
/*  961 */         if (getInt() == paramInt) {
/*  962 */           i++;
/*      */         }
/*      */       }
/*  965 */       resetForSecondPass();
/*  966 */       return i;
/*      */     }
/*      */   }
/*      */ 
/*      */   class MultiBand extends BandStructure.Band
/*      */   {
/* 1202 */     BandStructure.Band[] bands = new BandStructure.Band[10];
/* 1203 */     int bandCount = 0;
/*      */ 
/* 1264 */     private int cap = -1;
/*      */ 
/*      */     MultiBand(String paramCoding, Coding arg3)
/*      */     {
/* 1187 */       super(paramCoding, localCoding);
/*      */     }
/*      */ 
/*      */     public BandStructure.Band init() {
/* 1191 */       super.init();
/*      */ 
/* 1193 */       setCapacity(0);
/* 1194 */       if (phase() == 2)
/*      */       {
/* 1196 */         setPhase(4);
/* 1197 */         setPhase(6);
/*      */       }
/* 1199 */       return this;
/*      */     }
/*      */ 
/*      */     int size()
/*      */     {
/* 1206 */       return this.bandCount;
/*      */     }
/*      */     BandStructure.Band get(int paramInt) {
/* 1209 */       assert (paramInt < this.bandCount);
/* 1210 */       return this.bands[paramInt];
/*      */     }
/*      */     BandStructure.Band[] toArray() {
/* 1213 */       return (BandStructure.Band[])BandStructure.realloc(this.bands, this.bandCount);
/*      */     }
/*      */ 
/*      */     void add(BandStructure.Band paramBand) {
/* 1217 */       assert ((this.bandCount == 0) || (BandStructure.this.notePrevForAssert(paramBand, this.bands[(this.bandCount - 1)])));
/* 1218 */       if (this.bandCount == this.bands.length) {
/* 1219 */         this.bands = ((BandStructure.Band[])BandStructure.realloc(this.bands));
/*      */       }
/* 1221 */       this.bands[(this.bandCount++)] = paramBand;
/*      */     }
/*      */ 
/*      */     BandStructure.ByteBand newByteBand(String paramString) {
/* 1225 */       BandStructure.ByteBand localByteBand = new BandStructure.ByteBand(BandStructure.this, paramString);
/* 1226 */       localByteBand.init(); add(localByteBand);
/* 1227 */       return localByteBand;
/*      */     }
/*      */     BandStructure.IntBand newIntBand(String paramString) {
/* 1230 */       BandStructure.IntBand localIntBand = new BandStructure.IntBand(BandStructure.this, paramString, this.regularCoding);
/* 1231 */       localIntBand.init(); add(localIntBand);
/* 1232 */       return localIntBand;
/*      */     }
/*      */     BandStructure.IntBand newIntBand(String paramString, Coding paramCoding) {
/* 1235 */       BandStructure.IntBand localIntBand = new BandStructure.IntBand(BandStructure.this, paramString, paramCoding);
/* 1236 */       localIntBand.init(); add(localIntBand);
/* 1237 */       return localIntBand;
/*      */     }
/*      */     MultiBand newMultiBand(String paramString, Coding paramCoding) {
/* 1240 */       MultiBand localMultiBand = new MultiBand(BandStructure.this, paramString, paramCoding);
/* 1241 */       localMultiBand.init(); add(localMultiBand);
/* 1242 */       return localMultiBand;
/*      */     }
/*      */     BandStructure.CPRefBand newCPRefBand(String paramString, byte paramByte) {
/* 1245 */       BandStructure.CPRefBand localCPRefBand = new BandStructure.CPRefBand(BandStructure.this, paramString, this.regularCoding, paramByte);
/* 1246 */       localCPRefBand.init(); add(localCPRefBand);
/* 1247 */       return localCPRefBand;
/*      */     }
/*      */ 
/*      */     BandStructure.CPRefBand newCPRefBand(String paramString, Coding paramCoding, byte paramByte) {
/* 1251 */       BandStructure.CPRefBand localCPRefBand = new BandStructure.CPRefBand(BandStructure.this, paramString, paramCoding, paramByte);
/* 1252 */       localCPRefBand.init(); add(localCPRefBand);
/* 1253 */       return localCPRefBand;
/*      */     }
/*      */ 
/*      */     BandStructure.CPRefBand newCPRefBand(String paramString, Coding paramCoding, byte paramByte, boolean paramBoolean) {
/* 1257 */       BandStructure.CPRefBand localCPRefBand = new BandStructure.CPRefBand(BandStructure.this, paramString, paramCoding, paramByte, paramBoolean);
/* 1258 */       localCPRefBand.init(); add(localCPRefBand);
/* 1259 */       return localCPRefBand;
/*      */     }
/*      */     int bandCount() {
/* 1262 */       return this.bandCount;
/*      */     }
/*      */     public int capacity() {
/* 1265 */       return this.cap; } 
/* 1266 */     public void setCapacity(int paramInt) { this.cap = paramInt; } 
/*      */     public int length() {
/* 1268 */       return 0; } 
/* 1269 */     public int valuesRemainingForDebug() { return 0; }
/*      */ 
/*      */     protected void chooseBandCodings() throws IOException
/*      */     {
/* 1273 */       for (int i = 0; i < this.bandCount; i++) {
/* 1274 */         BandStructure.Band localBand = this.bands[i];
/* 1275 */         localBand.chooseBandCodings();
/*      */       }
/*      */     }
/*      */ 
/*      */     protected long computeOutputSize()
/*      */     {
/* 1281 */       long l1 = 0L;
/* 1282 */       for (int i = 0; i < this.bandCount; i++) {
/* 1283 */         BandStructure.Band localBand = this.bands[i];
/* 1284 */         long l2 = localBand.outputSize();
/* 1285 */         assert (l2 >= 0L) : localBand;
/* 1286 */         l1 += l2;
/*      */       }
/*      */ 
/* 1289 */       return l1;
/*      */     }
/*      */ 
/*      */     protected void writeDataTo(OutputStream paramOutputStream) throws IOException {
/* 1293 */       long l1 = 0L;
/* 1294 */       if (BandStructure.this.outputCounter != null) l1 = BandStructure.this.outputCounter.getCount();
/* 1295 */       for (int i = 0; i < this.bandCount; i++) {
/* 1296 */         BandStructure.Band localBand = this.bands[i];
/* 1297 */         localBand.writeTo(paramOutputStream);
/* 1298 */         if (BandStructure.this.outputCounter != null) {
/* 1299 */           long l2 = BandStructure.this.outputCounter.getCount();
/* 1300 */           long l3 = l2 - l1;
/* 1301 */           l1 = l2;
/* 1302 */           if (((BandStructure.this.verbose > 0) && (l3 > 0L)) || (BandStructure.this.verbose > 1))
/* 1303 */             Utils.log.info("  ...wrote " + l3 + " bytes from " + localBand);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     protected void readDataFrom(InputStream paramInputStream) throws IOException
/*      */     {
/* 1310 */       if (!$assertionsDisabled) throw new AssertionError();
/* 1311 */       for (int i = 0; i < this.bandCount; i++) {
/* 1312 */         BandStructure.Band localBand = this.bands[i];
/* 1313 */         localBand.readFrom(paramInputStream);
/* 1314 */         if (((BandStructure.this.verbose > 0) && (localBand.length() > 0)) || (BandStructure.this.verbose > 1))
/* 1315 */           Utils.log.info("  ...read " + localBand);
/*      */       }
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1321 */       return "{" + bandCount() + " bands: " + super.toString() + "}";
/*      */     }
/*      */   }
/*      */ 
/*      */   class ValueBand extends BandStructure.Band
/*      */   {
/*      */     private int[] values;
/*      */     private int length;
/*      */     private int valuesDisbursed;
/*      */     private CodingMethod bandCoding;
/*      */     private byte[] metaCoding;
/*      */ 
/*      */     protected ValueBand(String paramCoding, Coding arg3)
/*      */     {
/*  521 */       super(paramCoding, localCoding);
/*      */     }
/*      */ 
/*      */     public int capacity() {
/*  525 */       return this.values == null ? -1 : this.values.length;
/*      */     }
/*      */ 
/*      */     protected void setCapacity(int paramInt)
/*      */     {
/*  530 */       assert (this.length <= paramInt);
/*  531 */       if (paramInt == -1) { this.values = null; return; }
/*  532 */       this.values = BandStructure.realloc(this.values, paramInt);
/*      */     }
/*      */ 
/*      */     public int length() {
/*  536 */       return this.length;
/*      */     }
/*      */     protected int valuesRemainingForDebug() {
/*  539 */       return this.length - this.valuesDisbursed;
/*      */     }
/*      */     protected int valueAtForDebug(int paramInt) {
/*  542 */       return this.values[paramInt];
/*      */     }
/*      */ 
/*      */     void patchValue(int paramInt1, int paramInt2)
/*      */     {
/*  547 */       assert (this == BandStructure.this.archive_header_S);
/*  548 */       assert ((paramInt1 == 0) || (paramInt1 == 1));
/*  549 */       assert (paramInt1 < this.length);
/*  550 */       this.values[paramInt1] = paramInt2;
/*  551 */       this.outputSize = -1L;
/*      */     }
/*      */ 
/*      */     protected void initializeValues(int[] paramArrayOfInt) {
/*  555 */       assert (BandStructure.assertCanChangeLength(this));
/*  556 */       assert (this.length == 0);
/*  557 */       this.values = paramArrayOfInt;
/*  558 */       this.length = paramArrayOfInt.length;
/*      */     }
/*      */ 
/*      */     protected void addValue(int paramInt)
/*      */     {
/*  563 */       assert (BandStructure.assertCanChangeLength(this));
/*  564 */       if (this.length == this.values.length)
/*  565 */         setCapacity(this.length < 1000 ? this.length * 10 : this.length * 2);
/*  566 */       this.values[(this.length++)] = paramInt;
/*      */     }
/*      */ 
/*      */     private boolean canVaryCoding() {
/*  570 */       if (!BandStructure.this.optVaryCodings) return false;
/*  571 */       if (this.length == 0) return false;
/*      */ 
/*  573 */       if (this == BandStructure.this.archive_header_0) return false;
/*  574 */       if (this == BandStructure.this.archive_header_S) return false;
/*  575 */       if (this == BandStructure.this.archive_header_1) return false;
/*      */ 
/*  579 */       return (this.regularCoding.min() <= -256) || (this.regularCoding.max() >= 256);
/*      */     }
/*      */ 
/*      */     private boolean shouldVaryCoding() {
/*  583 */       assert (canVaryCoding());
/*  584 */       if ((BandStructure.this.effort < 9) && (this.length < 100))
/*  585 */         return false;
/*  586 */       return true;
/*      */     }
/*      */ 
/*      */     protected void chooseBandCodings() throws IOException {
/*  590 */       boolean bool = canVaryCoding();
/*      */       Object localObject;
/*  591 */       if ((!bool) || (!shouldVaryCoding())) {
/*  592 */         if (this.regularCoding.canRepresent(this.values, 0, this.length)) {
/*  593 */           this.bandCoding = this.regularCoding;
/*      */         } else {
/*  595 */           assert (bool);
/*  596 */           if (BandStructure.this.verbose > 1)
/*  597 */             Utils.log.fine("regular coding fails in band " + name());
/*  598 */           this.bandCoding = BandStructure.UNSIGNED5;
/*      */         }
/*  600 */         this.outputSize = -1L;
/*      */       } else {
/*  602 */         localObject = new int[] { 0, 0 };
/*  603 */         this.bandCoding = BandStructure.this.chooseCoding(this.values, 0, this.length, this.regularCoding, name(), (int[])localObject);
/*      */ 
/*  606 */         this.outputSize = localObject[0];
/*  607 */         if (this.outputSize == 0L) {
/*  608 */           this.outputSize = -1L;
/*      */         }
/*      */       }
/*      */ 
/*  612 */       if (this.bandCoding != this.regularCoding) {
/*  613 */         this.metaCoding = this.bandCoding.getMetaCoding(this.regularCoding);
/*  614 */         if (BandStructure.this.verbose > 1)
/*  615 */           Utils.log.fine("alternate coding " + this + " " + this.bandCoding);
/*      */       }
/*  617 */       else if ((bool) && (BandStructure.decodeEscapeValue(this.values[0], this.regularCoding) >= 0))
/*      */       {
/*  620 */         this.metaCoding = BandStructure.defaultMetaCoding;
/*      */       }
/*      */       else {
/*  623 */         this.metaCoding = BandStructure.noMetaCoding;
/*      */       }
/*  625 */       if ((this.metaCoding.length > 0) && ((BandStructure.this.verbose > 2) || ((BandStructure.this.verbose > 1) && (this.metaCoding.length > 1))))
/*      */       {
/*  627 */         localObject = new StringBuffer();
/*  628 */         for (int j = 0; j < this.metaCoding.length; j++) {
/*  629 */           if (j == 1) ((StringBuffer)localObject).append(" /");
/*  630 */           ((StringBuffer)localObject).append(" ").append(this.metaCoding[j] & 0xFF);
/*      */         }
/*  632 */         Utils.log.fine("   meta-coding " + localObject);
/*      */       }
/*      */ 
/*  639 */       assert ((this.outputSize < 0L) || (!(this.bandCoding instanceof Coding)) || (this.outputSize == ((Coding)this.bandCoding).getLength(this.values, 0, this.length))) : (this.bandCoding + " : " + this.outputSize + " != " + ((Coding)this.bandCoding).getLength(this.values, 0, this.length) + " ?= " + BandStructure.this.getCodingChooser().computeByteSize(this.bandCoding, this.values, 0, this.length));
/*      */ 
/*  646 */       if (this.metaCoding.length > 0)
/*      */       {
/*  650 */         if (this.outputSize >= 0L) {
/*  651 */           this.outputSize += computeEscapeSize();
/*      */         }
/*  653 */         for (int i = 1; i < this.metaCoding.length; i++)
/*  654 */           BandStructure.this.band_headers.putByte(this.metaCoding[i] & 0xFF);
/*      */       }
/*      */     }
/*      */ 
/*      */     protected long computeOutputSize()
/*      */     {
/*  660 */       this.outputSize = BandStructure.this.getCodingChooser().computeByteSize(this.bandCoding, this.values, 0, this.length);
/*      */ 
/*  662 */       assert (this.outputSize < 2147483647L);
/*  663 */       this.outputSize += computeEscapeSize();
/*  664 */       return this.outputSize;
/*      */     }
/*      */ 
/*      */     protected int computeEscapeSize() {
/*  668 */       if (this.metaCoding.length == 0) return 0;
/*  669 */       int i = this.metaCoding[0] & 0xFF;
/*  670 */       int j = BandStructure.encodeEscapeValue(i, this.regularCoding);
/*  671 */       return this.regularCoding.setD(0).getLength(j);
/*      */     }
/*      */ 
/*      */     protected void writeDataTo(OutputStream paramOutputStream) throws IOException {
/*  675 */       if (this.length == 0) return;
/*  676 */       long l = 0L;
/*  677 */       if (paramOutputStream == BandStructure.this.outputCounter) {
/*  678 */         l = BandStructure.this.outputCounter.getCount();
/*      */       }
/*  680 */       if (this.metaCoding.length > 0) {
/*  681 */         int i = this.metaCoding[0] & 0xFF;
/*      */ 
/*  685 */         int j = BandStructure.encodeEscapeValue(i, this.regularCoding);
/*      */ 
/*  687 */         this.regularCoding.setD(0).writeTo(paramOutputStream, j);
/*      */       }
/*  689 */       this.bandCoding.writeArrayTo(paramOutputStream, this.values, 0, this.length);
/*  690 */       if (paramOutputStream == BandStructure.this.outputCounter)
/*      */       {
/*  692 */         assert (this.outputSize == BandStructure.this.outputCounter.getCount() - l) : (this.outputSize + " != " + BandStructure.this.outputCounter.getCount() + "-" + l);
/*      */       }
/*  694 */       if (BandStructure.this.optDumpBands) dumpBand(); 
/*      */     }
/*      */ 
/*      */     protected void readDataFrom(InputStream paramInputStream) throws IOException {
/*  698 */       this.length = valuesExpected();
/*  699 */       if (this.length == 0) return;
/*  700 */       if (BandStructure.this.verbose > 1)
/*  701 */         Utils.log.fine("Reading band " + this);
/*  702 */       if (!canVaryCoding()) {
/*  703 */         this.bandCoding = this.regularCoding;
/*  704 */         this.metaCoding = BandStructure.noMetaCoding;
/*      */       } else {
/*  706 */         assert (paramInputStream.markSupported());
/*  707 */         paramInputStream.mark(5);
/*  708 */         int i = this.regularCoding.setD(0).readFrom(paramInputStream);
/*  709 */         int j = BandStructure.decodeEscapeValue(i, this.regularCoding);
/*  710 */         if (j < 0)
/*      */         {
/*  712 */           paramInputStream.reset();
/*  713 */           j = 0;
/*  714 */           this.bandCoding = this.regularCoding;
/*  715 */           this.metaCoding = BandStructure.noMetaCoding;
/*  716 */         } else if (j == 0) {
/*  717 */           this.bandCoding = this.regularCoding;
/*  718 */           this.metaCoding = BandStructure.defaultMetaCoding;
/*      */         } else {
/*  720 */           if (BandStructure.this.verbose > 2)
/*  721 */             Utils.log.fine("found X=" + i + " => XB=" + j);
/*  722 */           this.bandCoding = BandStructure.this.getBandHeader(j, this.regularCoding);
/*      */ 
/*  724 */           int k = BandStructure.this.bandHeaderBytePos0;
/*  725 */           int m = BandStructure.this.bandHeaderBytePos;
/*  726 */           this.metaCoding = new byte[m - k];
/*  727 */           System.arraycopy(BandStructure.this.bandHeaderBytes, k, this.metaCoding, 0, this.metaCoding.length);
/*      */         }
/*      */       }
/*      */ 
/*  731 */       if ((this.bandCoding != this.regularCoding) && 
/*  732 */         (BandStructure.this.verbose > 1)) {
/*  733 */         Utils.log.fine(name() + ": irregular coding " + this.bandCoding);
/*      */       }
/*  735 */       this.bandCoding.readArrayFrom(paramInputStream, this.values, 0, this.length);
/*  736 */       if (BandStructure.this.optDumpBands) dumpBand(); 
/*      */     }
/*      */ 
/*      */     public void doneDisbursing()
/*      */     {
/*  740 */       super.doneDisbursing();
/*  741 */       this.values = null;
/*      */     }
/*      */ 
/*      */     private void dumpBand() throws IOException {
/*  745 */       assert (BandStructure.this.optDumpBands);
/*  746 */       Object localObject1 = new PrintStream(BandStructure.getDumpStream(this, ".txt")); Object localObject2 = null;
/*      */       try { String str = this.bandCoding == this.regularCoding ? "" : " irregular";
/*  748 */         ((PrintStream)localObject1).print("# length=" + this.length + " size=" + outputSize() + str + " coding=" + this.bandCoding);
/*      */ 
/*  751 */         if (this.metaCoding != BandStructure.noMetaCoding) {
/*  752 */           StringBuffer localStringBuffer = new StringBuffer();
/*  753 */           for (int i = 0; i < this.metaCoding.length; i++) {
/*  754 */             if (i == 1) localStringBuffer.append(" /");
/*  755 */             localStringBuffer.append(" ").append(this.metaCoding[i] & 0xFF);
/*      */           }
/*  757 */           ((PrintStream)localObject1).print(" //header: " + localStringBuffer);
/*      */         }
/*  759 */         BandStructure.printArrayTo((PrintStream)localObject1, this.values, 0, this.length);
/*      */       }
/*      */       catch (Throwable localThrowable2)
/*      */       {
/*  746 */         localObject2 = localThrowable2; throw localThrowable2;
/*      */       }
/*      */       finally
/*      */       {
/*  760 */         if (localObject1 != null) if (localObject2 != null) try { ((PrintStream)localObject1).close(); } catch (Throwable localThrowable5) { localObject2.addSuppressed(localThrowable5); } else ((PrintStream)localObject1).close(); 
/*      */       }
/*  761 */       localObject1 = BandStructure.getDumpStream(this, ".bnd"); localObject2 = null;
/*      */       try { this.bandCoding.writeArrayTo((OutputStream)localObject1, this.values, 0, this.length); }
/*      */       catch (Throwable localThrowable4)
/*      */       {
/*  761 */         localObject2 = localThrowable4; throw localThrowable4;
/*      */       } finally {
/*  763 */         if (localObject1 != null) if (localObject2 != null) try { ((OutputStream)localObject1).close(); } catch (Throwable localThrowable6) { localObject2.addSuppressed(localThrowable6); } else ((OutputStream)localObject1).close(); 
/*      */       }
/*      */     }
/*      */ 
/*      */     protected int getValue()
/*      */     {
/*  768 */       assert (phase() == 6);
/*  769 */       assert (this.valuesDisbursed < this.length);
/*  770 */       return this.values[(this.valuesDisbursed++)];
/*      */     }
/*      */ 
/*      */     public void resetForSecondPass()
/*      */     {
/*  775 */       assert (phase() == 6);
/*  776 */       assert (this.valuesDisbursed == length());
/*  777 */       this.valuesDisbursed = 0;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.BandStructure
 * JD-Core Version:    0.6.2
 */