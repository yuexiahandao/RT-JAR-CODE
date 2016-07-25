/*      */ package com.sun.org.apache.xerces.internal.util;
/*      */ 
/*      */ import java.util.Hashtable;
/*      */ 
/*      */ public class EncodingMap
/*      */ {
/*  480 */   protected static final Hashtable fIANA2JavaMap = new Hashtable();
/*      */ 
/*  483 */   protected static final Hashtable fJava2IANAMap = new Hashtable();
/*      */ 
/*      */   /** @deprecated */
/*      */   public static void putIANA2JavaMapping(String ianaEncoding, String javaEncoding)
/*      */   {
/*  968 */     fIANA2JavaMap.put(ianaEncoding, javaEncoding);
/*      */   }
/*      */ 
/*      */   public static String getIANA2JavaMapping(String ianaEncoding)
/*      */   {
/*  977 */     return (String)fIANA2JavaMap.get(ianaEncoding);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public static String removeIANA2JavaMapping(String ianaEncoding)
/*      */   {
/*  990 */     return (String)fIANA2JavaMap.remove(ianaEncoding);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public static void putJava2IANAMapping(String javaEncoding, String ianaEncoding)
/*      */   {
/* 1005 */     fJava2IANAMap.put(javaEncoding, ianaEncoding);
/*      */   }
/*      */ 
/*      */   public static String getJava2IANAMapping(String javaEncoding)
/*      */   {
/* 1014 */     return (String)fJava2IANAMap.get(javaEncoding);
/*      */   }
/*      */ 
/*      */   /** @deprecated */
/*      */   public static String removeJava2IANAMapping(String javaEncoding)
/*      */   {
/* 1027 */     return (String)fJava2IANAMap.remove(javaEncoding);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  492 */     fIANA2JavaMap.put("BIG5", "Big5");
/*  493 */     fIANA2JavaMap.put("CSBIG5", "Big5");
/*  494 */     fIANA2JavaMap.put("CP037", "CP037");
/*  495 */     fIANA2JavaMap.put("IBM037", "CP037");
/*  496 */     fIANA2JavaMap.put("CSIBM037", "CP037");
/*  497 */     fIANA2JavaMap.put("EBCDIC-CP-US", "CP037");
/*  498 */     fIANA2JavaMap.put("EBCDIC-CP-CA", "CP037");
/*  499 */     fIANA2JavaMap.put("EBCDIC-CP-NL", "CP037");
/*  500 */     fIANA2JavaMap.put("EBCDIC-CP-WT", "CP037");
/*  501 */     fIANA2JavaMap.put("IBM273", "CP273");
/*  502 */     fIANA2JavaMap.put("CP273", "CP273");
/*  503 */     fIANA2JavaMap.put("CSIBM273", "CP273");
/*  504 */     fIANA2JavaMap.put("IBM277", "CP277");
/*  505 */     fIANA2JavaMap.put("CP277", "CP277");
/*  506 */     fIANA2JavaMap.put("CSIBM277", "CP277");
/*  507 */     fIANA2JavaMap.put("EBCDIC-CP-DK", "CP277");
/*  508 */     fIANA2JavaMap.put("EBCDIC-CP-NO", "CP277");
/*  509 */     fIANA2JavaMap.put("IBM278", "CP278");
/*  510 */     fIANA2JavaMap.put("CP278", "CP278");
/*  511 */     fIANA2JavaMap.put("CSIBM278", "CP278");
/*  512 */     fIANA2JavaMap.put("EBCDIC-CP-FI", "CP278");
/*  513 */     fIANA2JavaMap.put("EBCDIC-CP-SE", "CP278");
/*  514 */     fIANA2JavaMap.put("IBM280", "CP280");
/*  515 */     fIANA2JavaMap.put("CP280", "CP280");
/*  516 */     fIANA2JavaMap.put("CSIBM280", "CP280");
/*  517 */     fIANA2JavaMap.put("EBCDIC-CP-IT", "CP280");
/*  518 */     fIANA2JavaMap.put("IBM284", "CP284");
/*  519 */     fIANA2JavaMap.put("CP284", "CP284");
/*  520 */     fIANA2JavaMap.put("CSIBM284", "CP284");
/*  521 */     fIANA2JavaMap.put("EBCDIC-CP-ES", "CP284");
/*  522 */     fIANA2JavaMap.put("EBCDIC-CP-GB", "CP285");
/*  523 */     fIANA2JavaMap.put("IBM285", "CP285");
/*  524 */     fIANA2JavaMap.put("CP285", "CP285");
/*  525 */     fIANA2JavaMap.put("CSIBM285", "CP285");
/*  526 */     fIANA2JavaMap.put("EBCDIC-JP-KANA", "CP290");
/*  527 */     fIANA2JavaMap.put("IBM290", "CP290");
/*  528 */     fIANA2JavaMap.put("CP290", "CP290");
/*  529 */     fIANA2JavaMap.put("CSIBM290", "CP290");
/*  530 */     fIANA2JavaMap.put("EBCDIC-CP-FR", "CP297");
/*  531 */     fIANA2JavaMap.put("IBM297", "CP297");
/*  532 */     fIANA2JavaMap.put("CP297", "CP297");
/*  533 */     fIANA2JavaMap.put("CSIBM297", "CP297");
/*  534 */     fIANA2JavaMap.put("EBCDIC-CP-AR1", "CP420");
/*  535 */     fIANA2JavaMap.put("IBM420", "CP420");
/*  536 */     fIANA2JavaMap.put("CP420", "CP420");
/*  537 */     fIANA2JavaMap.put("CSIBM420", "CP420");
/*  538 */     fIANA2JavaMap.put("EBCDIC-CP-HE", "CP424");
/*  539 */     fIANA2JavaMap.put("IBM424", "CP424");
/*  540 */     fIANA2JavaMap.put("CP424", "CP424");
/*  541 */     fIANA2JavaMap.put("CSIBM424", "CP424");
/*  542 */     fIANA2JavaMap.put("IBM437", "CP437");
/*  543 */     fIANA2JavaMap.put("437", "CP437");
/*  544 */     fIANA2JavaMap.put("CP437", "CP437");
/*  545 */     fIANA2JavaMap.put("CSPC8CODEPAGE437", "CP437");
/*  546 */     fIANA2JavaMap.put("EBCDIC-CP-CH", "CP500");
/*  547 */     fIANA2JavaMap.put("IBM500", "CP500");
/*  548 */     fIANA2JavaMap.put("CP500", "CP500");
/*  549 */     fIANA2JavaMap.put("CSIBM500", "CP500");
/*  550 */     fIANA2JavaMap.put("EBCDIC-CP-CH", "CP500");
/*  551 */     fIANA2JavaMap.put("EBCDIC-CP-BE", "CP500");
/*  552 */     fIANA2JavaMap.put("IBM775", "CP775");
/*  553 */     fIANA2JavaMap.put("CP775", "CP775");
/*  554 */     fIANA2JavaMap.put("CSPC775BALTIC", "CP775");
/*  555 */     fIANA2JavaMap.put("IBM850", "CP850");
/*  556 */     fIANA2JavaMap.put("850", "CP850");
/*  557 */     fIANA2JavaMap.put("CP850", "CP850");
/*  558 */     fIANA2JavaMap.put("CSPC850MULTILINGUAL", "CP850");
/*  559 */     fIANA2JavaMap.put("IBM852", "CP852");
/*  560 */     fIANA2JavaMap.put("852", "CP852");
/*  561 */     fIANA2JavaMap.put("CP852", "CP852");
/*  562 */     fIANA2JavaMap.put("CSPCP852", "CP852");
/*  563 */     fIANA2JavaMap.put("IBM855", "CP855");
/*  564 */     fIANA2JavaMap.put("855", "CP855");
/*  565 */     fIANA2JavaMap.put("CP855", "CP855");
/*  566 */     fIANA2JavaMap.put("CSIBM855", "CP855");
/*  567 */     fIANA2JavaMap.put("IBM857", "CP857");
/*  568 */     fIANA2JavaMap.put("857", "CP857");
/*  569 */     fIANA2JavaMap.put("CP857", "CP857");
/*  570 */     fIANA2JavaMap.put("CSIBM857", "CP857");
/*  571 */     fIANA2JavaMap.put("IBM00858", "CP858");
/*  572 */     fIANA2JavaMap.put("CP00858", "CP858");
/*  573 */     fIANA2JavaMap.put("CCSID00858", "CP858");
/*  574 */     fIANA2JavaMap.put("IBM860", "CP860");
/*  575 */     fIANA2JavaMap.put("860", "CP860");
/*  576 */     fIANA2JavaMap.put("CP860", "CP860");
/*  577 */     fIANA2JavaMap.put("CSIBM860", "CP860");
/*  578 */     fIANA2JavaMap.put("IBM861", "CP861");
/*  579 */     fIANA2JavaMap.put("861", "CP861");
/*  580 */     fIANA2JavaMap.put("CP861", "CP861");
/*  581 */     fIANA2JavaMap.put("CP-IS", "CP861");
/*  582 */     fIANA2JavaMap.put("CSIBM861", "CP861");
/*  583 */     fIANA2JavaMap.put("IBM862", "CP862");
/*  584 */     fIANA2JavaMap.put("862", "CP862");
/*  585 */     fIANA2JavaMap.put("CP862", "CP862");
/*  586 */     fIANA2JavaMap.put("CSPC862LATINHEBREW", "CP862");
/*  587 */     fIANA2JavaMap.put("IBM863", "CP863");
/*  588 */     fIANA2JavaMap.put("863", "CP863");
/*  589 */     fIANA2JavaMap.put("CP863", "CP863");
/*  590 */     fIANA2JavaMap.put("CSIBM863", "CP863");
/*  591 */     fIANA2JavaMap.put("IBM864", "CP864");
/*  592 */     fIANA2JavaMap.put("CP864", "CP864");
/*  593 */     fIANA2JavaMap.put("CSIBM864", "CP864");
/*  594 */     fIANA2JavaMap.put("IBM865", "CP865");
/*  595 */     fIANA2JavaMap.put("865", "CP865");
/*  596 */     fIANA2JavaMap.put("CP865", "CP865");
/*  597 */     fIANA2JavaMap.put("CSIBM865", "CP865");
/*  598 */     fIANA2JavaMap.put("IBM866", "CP866");
/*  599 */     fIANA2JavaMap.put("866", "CP866");
/*  600 */     fIANA2JavaMap.put("CP866", "CP866");
/*  601 */     fIANA2JavaMap.put("CSIBM866", "CP866");
/*  602 */     fIANA2JavaMap.put("IBM868", "CP868");
/*  603 */     fIANA2JavaMap.put("CP868", "CP868");
/*  604 */     fIANA2JavaMap.put("CSIBM868", "CP868");
/*  605 */     fIANA2JavaMap.put("CP-AR", "CP868");
/*  606 */     fIANA2JavaMap.put("IBM869", "CP869");
/*  607 */     fIANA2JavaMap.put("CP869", "CP869");
/*  608 */     fIANA2JavaMap.put("CSIBM869", "CP869");
/*  609 */     fIANA2JavaMap.put("CP-GR", "CP869");
/*  610 */     fIANA2JavaMap.put("IBM870", "CP870");
/*  611 */     fIANA2JavaMap.put("CP870", "CP870");
/*  612 */     fIANA2JavaMap.put("CSIBM870", "CP870");
/*  613 */     fIANA2JavaMap.put("EBCDIC-CP-ROECE", "CP870");
/*  614 */     fIANA2JavaMap.put("EBCDIC-CP-YU", "CP870");
/*  615 */     fIANA2JavaMap.put("IBM871", "CP871");
/*  616 */     fIANA2JavaMap.put("CP871", "CP871");
/*  617 */     fIANA2JavaMap.put("CSIBM871", "CP871");
/*  618 */     fIANA2JavaMap.put("EBCDIC-CP-IS", "CP871");
/*  619 */     fIANA2JavaMap.put("IBM918", "CP918");
/*  620 */     fIANA2JavaMap.put("CP918", "CP918");
/*  621 */     fIANA2JavaMap.put("CSIBM918", "CP918");
/*  622 */     fIANA2JavaMap.put("EBCDIC-CP-AR2", "CP918");
/*  623 */     fIANA2JavaMap.put("IBM00924", "CP924");
/*  624 */     fIANA2JavaMap.put("CP00924", "CP924");
/*  625 */     fIANA2JavaMap.put("CCSID00924", "CP924");
/*      */ 
/*  627 */     fIANA2JavaMap.put("EBCDIC-LATIN9--EURO", "CP924");
/*  628 */     fIANA2JavaMap.put("IBM1026", "CP1026");
/*  629 */     fIANA2JavaMap.put("CP1026", "CP1026");
/*  630 */     fIANA2JavaMap.put("CSIBM1026", "CP1026");
/*  631 */     fIANA2JavaMap.put("IBM01140", "Cp1140");
/*  632 */     fIANA2JavaMap.put("CP01140", "Cp1140");
/*  633 */     fIANA2JavaMap.put("CCSID01140", "Cp1140");
/*  634 */     fIANA2JavaMap.put("IBM01141", "Cp1141");
/*  635 */     fIANA2JavaMap.put("CP01141", "Cp1141");
/*  636 */     fIANA2JavaMap.put("CCSID01141", "Cp1141");
/*  637 */     fIANA2JavaMap.put("IBM01142", "Cp1142");
/*  638 */     fIANA2JavaMap.put("CP01142", "Cp1142");
/*  639 */     fIANA2JavaMap.put("CCSID01142", "Cp1142");
/*  640 */     fIANA2JavaMap.put("IBM01143", "Cp1143");
/*  641 */     fIANA2JavaMap.put("CP01143", "Cp1143");
/*  642 */     fIANA2JavaMap.put("CCSID01143", "Cp1143");
/*  643 */     fIANA2JavaMap.put("IBM01144", "Cp1144");
/*  644 */     fIANA2JavaMap.put("CP01144", "Cp1144");
/*  645 */     fIANA2JavaMap.put("CCSID01144", "Cp1144");
/*  646 */     fIANA2JavaMap.put("IBM01145", "Cp1145");
/*  647 */     fIANA2JavaMap.put("CP01145", "Cp1145");
/*  648 */     fIANA2JavaMap.put("CCSID01145", "Cp1145");
/*  649 */     fIANA2JavaMap.put("IBM01146", "Cp1146");
/*  650 */     fIANA2JavaMap.put("CP01146", "Cp1146");
/*  651 */     fIANA2JavaMap.put("CCSID01146", "Cp1146");
/*  652 */     fIANA2JavaMap.put("IBM01147", "Cp1147");
/*  653 */     fIANA2JavaMap.put("CP01147", "Cp1147");
/*  654 */     fIANA2JavaMap.put("CCSID01147", "Cp1147");
/*  655 */     fIANA2JavaMap.put("IBM01148", "Cp1148");
/*  656 */     fIANA2JavaMap.put("CP01148", "Cp1148");
/*  657 */     fIANA2JavaMap.put("CCSID01148", "Cp1148");
/*  658 */     fIANA2JavaMap.put("IBM01149", "Cp1149");
/*  659 */     fIANA2JavaMap.put("CP01149", "Cp1149");
/*  660 */     fIANA2JavaMap.put("CCSID01149", "Cp1149");
/*  661 */     fIANA2JavaMap.put("EUC-JP", "EUCJIS");
/*  662 */     fIANA2JavaMap.put("CSEUCPKDFMTJAPANESE", "EUCJIS");
/*  663 */     fIANA2JavaMap.put("EXTENDED_UNIX_CODE_PACKED_FORMAT_FOR_JAPANESE", "EUCJIS");
/*  664 */     fIANA2JavaMap.put("EUC-KR", "KSC5601");
/*  665 */     fIANA2JavaMap.put("CSEUCKR", "KSC5601");
/*  666 */     fIANA2JavaMap.put("KS_C_5601-1987", "KS_C_5601-1987");
/*  667 */     fIANA2JavaMap.put("ISO-IR-149", "KS_C_5601-1987");
/*  668 */     fIANA2JavaMap.put("KS_C_5601-1989", "KS_C_5601-1987");
/*  669 */     fIANA2JavaMap.put("KSC_5601", "KS_C_5601-1987");
/*  670 */     fIANA2JavaMap.put("KOREAN", "KS_C_5601-1987");
/*  671 */     fIANA2JavaMap.put("CSKSC56011987", "KS_C_5601-1987");
/*  672 */     fIANA2JavaMap.put("GB2312", "GB2312");
/*  673 */     fIANA2JavaMap.put("CSGB2312", "GB2312");
/*  674 */     fIANA2JavaMap.put("ISO-2022-JP", "JIS");
/*  675 */     fIANA2JavaMap.put("CSISO2022JP", "JIS");
/*  676 */     fIANA2JavaMap.put("ISO-2022-KR", "ISO2022KR");
/*  677 */     fIANA2JavaMap.put("CSISO2022KR", "ISO2022KR");
/*  678 */     fIANA2JavaMap.put("ISO-2022-CN", "ISO2022CN");
/*      */ 
/*  680 */     fIANA2JavaMap.put("X0201", "JIS0201");
/*  681 */     fIANA2JavaMap.put("CSISO13JISC6220JP", "JIS0201");
/*  682 */     fIANA2JavaMap.put("X0208", "JIS0208");
/*  683 */     fIANA2JavaMap.put("ISO-IR-87", "JIS0208");
/*  684 */     fIANA2JavaMap.put("X0208dbiJIS_X0208-1983", "JIS0208");
/*  685 */     fIANA2JavaMap.put("CSISO87JISX0208", "JIS0208");
/*  686 */     fIANA2JavaMap.put("X0212", "JIS0212");
/*  687 */     fIANA2JavaMap.put("ISO-IR-159", "JIS0212");
/*  688 */     fIANA2JavaMap.put("CSISO159JISX02121990", "JIS0212");
/*  689 */     fIANA2JavaMap.put("GB18030", "GB18030");
/*  690 */     fIANA2JavaMap.put("GBK", "GBK");
/*  691 */     fIANA2JavaMap.put("CP936", "GBK");
/*  692 */     fIANA2JavaMap.put("MS936", "GBK");
/*  693 */     fIANA2JavaMap.put("WINDOWS-936", "GBK");
/*  694 */     fIANA2JavaMap.put("SHIFT_JIS", "SJIS");
/*  695 */     fIANA2JavaMap.put("CSSHIFTJIS", "SJIS");
/*  696 */     fIANA2JavaMap.put("MS_KANJI", "SJIS");
/*  697 */     fIANA2JavaMap.put("WINDOWS-31J", "MS932");
/*  698 */     fIANA2JavaMap.put("CSWINDOWS31J", "MS932");
/*      */ 
/*  701 */     fIANA2JavaMap.put("WINDOWS-1250", "Cp1250");
/*  702 */     fIANA2JavaMap.put("WINDOWS-1251", "Cp1251");
/*  703 */     fIANA2JavaMap.put("WINDOWS-1252", "Cp1252");
/*  704 */     fIANA2JavaMap.put("WINDOWS-1253", "Cp1253");
/*  705 */     fIANA2JavaMap.put("WINDOWS-1254", "Cp1254");
/*  706 */     fIANA2JavaMap.put("WINDOWS-1255", "Cp1255");
/*  707 */     fIANA2JavaMap.put("WINDOWS-1256", "Cp1256");
/*  708 */     fIANA2JavaMap.put("WINDOWS-1257", "Cp1257");
/*  709 */     fIANA2JavaMap.put("WINDOWS-1258", "Cp1258");
/*  710 */     fIANA2JavaMap.put("TIS-620", "TIS620");
/*      */ 
/*  712 */     fIANA2JavaMap.put("ISO-8859-1", "ISO8859_1");
/*  713 */     fIANA2JavaMap.put("ISO-IR-100", "ISO8859_1");
/*  714 */     fIANA2JavaMap.put("ISO_8859-1", "ISO8859_1");
/*  715 */     fIANA2JavaMap.put("LATIN1", "ISO8859_1");
/*  716 */     fIANA2JavaMap.put("CSISOLATIN1", "ISO8859_1");
/*  717 */     fIANA2JavaMap.put("L1", "ISO8859_1");
/*  718 */     fIANA2JavaMap.put("IBM819", "ISO8859_1");
/*  719 */     fIANA2JavaMap.put("CP819", "ISO8859_1");
/*      */ 
/*  721 */     fIANA2JavaMap.put("ISO-8859-2", "ISO8859_2");
/*  722 */     fIANA2JavaMap.put("ISO-IR-101", "ISO8859_2");
/*  723 */     fIANA2JavaMap.put("ISO_8859-2", "ISO8859_2");
/*  724 */     fIANA2JavaMap.put("LATIN2", "ISO8859_2");
/*  725 */     fIANA2JavaMap.put("CSISOLATIN2", "ISO8859_2");
/*  726 */     fIANA2JavaMap.put("L2", "ISO8859_2");
/*      */ 
/*  728 */     fIANA2JavaMap.put("ISO-8859-3", "ISO8859_3");
/*  729 */     fIANA2JavaMap.put("ISO-IR-109", "ISO8859_3");
/*  730 */     fIANA2JavaMap.put("ISO_8859-3", "ISO8859_3");
/*  731 */     fIANA2JavaMap.put("LATIN3", "ISO8859_3");
/*  732 */     fIANA2JavaMap.put("CSISOLATIN3", "ISO8859_3");
/*  733 */     fIANA2JavaMap.put("L3", "ISO8859_3");
/*      */ 
/*  735 */     fIANA2JavaMap.put("ISO-8859-4", "ISO8859_4");
/*  736 */     fIANA2JavaMap.put("ISO-IR-110", "ISO8859_4");
/*  737 */     fIANA2JavaMap.put("ISO_8859-4", "ISO8859_4");
/*  738 */     fIANA2JavaMap.put("LATIN4", "ISO8859_4");
/*  739 */     fIANA2JavaMap.put("CSISOLATIN4", "ISO8859_4");
/*  740 */     fIANA2JavaMap.put("L4", "ISO8859_4");
/*      */ 
/*  742 */     fIANA2JavaMap.put("ISO-8859-5", "ISO8859_5");
/*  743 */     fIANA2JavaMap.put("ISO-IR-144", "ISO8859_5");
/*  744 */     fIANA2JavaMap.put("ISO_8859-5", "ISO8859_5");
/*  745 */     fIANA2JavaMap.put("CYRILLIC", "ISO8859_5");
/*  746 */     fIANA2JavaMap.put("CSISOLATINCYRILLIC", "ISO8859_5");
/*      */ 
/*  748 */     fIANA2JavaMap.put("ISO-8859-6", "ISO8859_6");
/*  749 */     fIANA2JavaMap.put("ISO-IR-127", "ISO8859_6");
/*  750 */     fIANA2JavaMap.put("ISO_8859-6", "ISO8859_6");
/*  751 */     fIANA2JavaMap.put("ECMA-114", "ISO8859_6");
/*  752 */     fIANA2JavaMap.put("ASMO-708", "ISO8859_6");
/*  753 */     fIANA2JavaMap.put("ARABIC", "ISO8859_6");
/*  754 */     fIANA2JavaMap.put("CSISOLATINARABIC", "ISO8859_6");
/*      */ 
/*  756 */     fIANA2JavaMap.put("ISO-8859-7", "ISO8859_7");
/*  757 */     fIANA2JavaMap.put("ISO-IR-126", "ISO8859_7");
/*  758 */     fIANA2JavaMap.put("ISO_8859-7", "ISO8859_7");
/*  759 */     fIANA2JavaMap.put("ELOT_928", "ISO8859_7");
/*  760 */     fIANA2JavaMap.put("ECMA-118", "ISO8859_7");
/*  761 */     fIANA2JavaMap.put("GREEK", "ISO8859_7");
/*  762 */     fIANA2JavaMap.put("CSISOLATINGREEK", "ISO8859_7");
/*  763 */     fIANA2JavaMap.put("GREEK8", "ISO8859_7");
/*      */ 
/*  765 */     fIANA2JavaMap.put("ISO-8859-8", "ISO8859_8");
/*  766 */     fIANA2JavaMap.put("ISO-8859-8-I", "ISO8859_8");
/*  767 */     fIANA2JavaMap.put("ISO-IR-138", "ISO8859_8");
/*  768 */     fIANA2JavaMap.put("ISO_8859-8", "ISO8859_8");
/*  769 */     fIANA2JavaMap.put("HEBREW", "ISO8859_8");
/*  770 */     fIANA2JavaMap.put("CSISOLATINHEBREW", "ISO8859_8");
/*      */ 
/*  772 */     fIANA2JavaMap.put("ISO-8859-9", "ISO8859_9");
/*  773 */     fIANA2JavaMap.put("ISO-IR-148", "ISO8859_9");
/*  774 */     fIANA2JavaMap.put("ISO_8859-9", "ISO8859_9");
/*  775 */     fIANA2JavaMap.put("LATIN5", "ISO8859_9");
/*  776 */     fIANA2JavaMap.put("CSISOLATIN5", "ISO8859_9");
/*  777 */     fIANA2JavaMap.put("L5", "ISO8859_9");
/*      */ 
/*  779 */     fIANA2JavaMap.put("ISO-8859-13", "ISO8859_13");
/*      */ 
/*  781 */     fIANA2JavaMap.put("ISO-8859-15", "ISO8859_15_FDIS");
/*  782 */     fIANA2JavaMap.put("ISO_8859-15", "ISO8859_15_FDIS");
/*  783 */     fIANA2JavaMap.put("LATIN-9", "ISO8859_15_FDIS");
/*      */ 
/*  785 */     fIANA2JavaMap.put("KOI8-R", "KOI8_R");
/*  786 */     fIANA2JavaMap.put("CSKOI8R", "KOI8_R");
/*  787 */     fIANA2JavaMap.put("US-ASCII", "ASCII");
/*  788 */     fIANA2JavaMap.put("ISO-IR-6", "ASCII");
/*  789 */     fIANA2JavaMap.put("ANSI_X3.4-1968", "ASCII");
/*  790 */     fIANA2JavaMap.put("ANSI_X3.4-1986", "ASCII");
/*  791 */     fIANA2JavaMap.put("ISO_646.IRV:1991", "ASCII");
/*  792 */     fIANA2JavaMap.put("ASCII", "ASCII");
/*  793 */     fIANA2JavaMap.put("CSASCII", "ASCII");
/*  794 */     fIANA2JavaMap.put("ISO646-US", "ASCII");
/*  795 */     fIANA2JavaMap.put("US", "ASCII");
/*  796 */     fIANA2JavaMap.put("IBM367", "ASCII");
/*  797 */     fIANA2JavaMap.put("CP367", "ASCII");
/*  798 */     fIANA2JavaMap.put("UTF-8", "UTF8");
/*  799 */     fIANA2JavaMap.put("UTF-16", "UTF-16");
/*  800 */     fIANA2JavaMap.put("UTF-16BE", "UnicodeBig");
/*  801 */     fIANA2JavaMap.put("UTF-16LE", "UnicodeLittle");
/*      */ 
/*  806 */     fIANA2JavaMap.put("IBM-1047", "Cp1047");
/*  807 */     fIANA2JavaMap.put("IBM1047", "Cp1047");
/*  808 */     fIANA2JavaMap.put("CP1047", "Cp1047");
/*      */ 
/*  812 */     fIANA2JavaMap.put("IBM-37", "CP037");
/*  813 */     fIANA2JavaMap.put("IBM-273", "CP273");
/*  814 */     fIANA2JavaMap.put("IBM-277", "CP277");
/*  815 */     fIANA2JavaMap.put("IBM-278", "CP278");
/*  816 */     fIANA2JavaMap.put("IBM-280", "CP280");
/*  817 */     fIANA2JavaMap.put("IBM-284", "CP284");
/*  818 */     fIANA2JavaMap.put("IBM-285", "CP285");
/*  819 */     fIANA2JavaMap.put("IBM-290", "CP290");
/*  820 */     fIANA2JavaMap.put("IBM-297", "CP297");
/*  821 */     fIANA2JavaMap.put("IBM-420", "CP420");
/*  822 */     fIANA2JavaMap.put("IBM-424", "CP424");
/*  823 */     fIANA2JavaMap.put("IBM-437", "CP437");
/*  824 */     fIANA2JavaMap.put("IBM-500", "CP500");
/*  825 */     fIANA2JavaMap.put("IBM-775", "CP775");
/*  826 */     fIANA2JavaMap.put("IBM-850", "CP850");
/*  827 */     fIANA2JavaMap.put("IBM-852", "CP852");
/*  828 */     fIANA2JavaMap.put("IBM-855", "CP855");
/*  829 */     fIANA2JavaMap.put("IBM-857", "CP857");
/*  830 */     fIANA2JavaMap.put("IBM-858", "CP858");
/*  831 */     fIANA2JavaMap.put("IBM-860", "CP860");
/*  832 */     fIANA2JavaMap.put("IBM-861", "CP861");
/*  833 */     fIANA2JavaMap.put("IBM-862", "CP862");
/*  834 */     fIANA2JavaMap.put("IBM-863", "CP863");
/*  835 */     fIANA2JavaMap.put("IBM-864", "CP864");
/*  836 */     fIANA2JavaMap.put("IBM-865", "CP865");
/*  837 */     fIANA2JavaMap.put("IBM-866", "CP866");
/*  838 */     fIANA2JavaMap.put("IBM-868", "CP868");
/*  839 */     fIANA2JavaMap.put("IBM-869", "CP869");
/*  840 */     fIANA2JavaMap.put("IBM-870", "CP870");
/*  841 */     fIANA2JavaMap.put("IBM-871", "CP871");
/*  842 */     fIANA2JavaMap.put("IBM-918", "CP918");
/*  843 */     fIANA2JavaMap.put("IBM-924", "CP924");
/*  844 */     fIANA2JavaMap.put("IBM-1026", "CP1026");
/*  845 */     fIANA2JavaMap.put("IBM-1140", "Cp1140");
/*  846 */     fIANA2JavaMap.put("IBM-1141", "Cp1141");
/*  847 */     fIANA2JavaMap.put("IBM-1142", "Cp1142");
/*  848 */     fIANA2JavaMap.put("IBM-1143", "Cp1143");
/*  849 */     fIANA2JavaMap.put("IBM-1144", "Cp1144");
/*  850 */     fIANA2JavaMap.put("IBM-1145", "Cp1145");
/*  851 */     fIANA2JavaMap.put("IBM-1146", "Cp1146");
/*  852 */     fIANA2JavaMap.put("IBM-1147", "Cp1147");
/*  853 */     fIANA2JavaMap.put("IBM-1148", "Cp1148");
/*  854 */     fIANA2JavaMap.put("IBM-1149", "Cp1149");
/*  855 */     fIANA2JavaMap.put("IBM-819", "ISO8859_1");
/*  856 */     fIANA2JavaMap.put("IBM-367", "ASCII");
/*      */ 
/*  864 */     fJava2IANAMap.put("ISO8859_1", "ISO-8859-1");
/*  865 */     fJava2IANAMap.put("ISO8859_2", "ISO-8859-2");
/*  866 */     fJava2IANAMap.put("ISO8859_3", "ISO-8859-3");
/*  867 */     fJava2IANAMap.put("ISO8859_4", "ISO-8859-4");
/*  868 */     fJava2IANAMap.put("ISO8859_5", "ISO-8859-5");
/*  869 */     fJava2IANAMap.put("ISO8859_6", "ISO-8859-6");
/*  870 */     fJava2IANAMap.put("ISO8859_7", "ISO-8859-7");
/*  871 */     fJava2IANAMap.put("ISO8859_8", "ISO-8859-8");
/*  872 */     fJava2IANAMap.put("ISO8859_9", "ISO-8859-9");
/*  873 */     fJava2IANAMap.put("ISO8859_13", "ISO-8859-13");
/*  874 */     fJava2IANAMap.put("ISO8859_15", "ISO-8859-15");
/*  875 */     fJava2IANAMap.put("ISO8859_15_FDIS", "ISO-8859-15");
/*  876 */     fJava2IANAMap.put("Big5", "BIG5");
/*  877 */     fJava2IANAMap.put("CP037", "EBCDIC-CP-US");
/*  878 */     fJava2IANAMap.put("CP273", "IBM273");
/*  879 */     fJava2IANAMap.put("CP277", "EBCDIC-CP-DK");
/*  880 */     fJava2IANAMap.put("CP278", "EBCDIC-CP-FI");
/*  881 */     fJava2IANAMap.put("CP280", "EBCDIC-CP-IT");
/*  882 */     fJava2IANAMap.put("CP284", "EBCDIC-CP-ES");
/*  883 */     fJava2IANAMap.put("CP285", "EBCDIC-CP-GB");
/*  884 */     fJava2IANAMap.put("CP290", "EBCDIC-JP-KANA");
/*  885 */     fJava2IANAMap.put("CP297", "EBCDIC-CP-FR");
/*  886 */     fJava2IANAMap.put("CP420", "EBCDIC-CP-AR1");
/*  887 */     fJava2IANAMap.put("CP424", "EBCDIC-CP-HE");
/*  888 */     fJava2IANAMap.put("CP437", "IBM437");
/*  889 */     fJava2IANAMap.put("CP500", "EBCDIC-CP-CH");
/*  890 */     fJava2IANAMap.put("CP775", "IBM775");
/*  891 */     fJava2IANAMap.put("CP850", "IBM850");
/*  892 */     fJava2IANAMap.put("CP852", "IBM852");
/*  893 */     fJava2IANAMap.put("CP855", "IBM855");
/*  894 */     fJava2IANAMap.put("CP857", "IBM857");
/*  895 */     fJava2IANAMap.put("CP858", "IBM00858");
/*  896 */     fJava2IANAMap.put("CP860", "IBM860");
/*  897 */     fJava2IANAMap.put("CP861", "IBM861");
/*  898 */     fJava2IANAMap.put("CP862", "IBM862");
/*  899 */     fJava2IANAMap.put("CP863", "IBM863");
/*  900 */     fJava2IANAMap.put("CP864", "IBM864");
/*  901 */     fJava2IANAMap.put("CP865", "IBM865");
/*  902 */     fJava2IANAMap.put("CP866", "IBM866");
/*  903 */     fJava2IANAMap.put("CP868", "IBM868");
/*  904 */     fJava2IANAMap.put("CP869", "IBM869");
/*  905 */     fJava2IANAMap.put("CP870", "EBCDIC-CP-ROECE");
/*  906 */     fJava2IANAMap.put("CP871", "EBCDIC-CP-IS");
/*  907 */     fJava2IANAMap.put("CP918", "EBCDIC-CP-AR2");
/*  908 */     fJava2IANAMap.put("CP924", "IBM00924");
/*  909 */     fJava2IANAMap.put("CP1026", "IBM1026");
/*  910 */     fJava2IANAMap.put("Cp01140", "IBM01140");
/*  911 */     fJava2IANAMap.put("Cp01141", "IBM01141");
/*  912 */     fJava2IANAMap.put("Cp01142", "IBM01142");
/*  913 */     fJava2IANAMap.put("Cp01143", "IBM01143");
/*  914 */     fJava2IANAMap.put("Cp01144", "IBM01144");
/*  915 */     fJava2IANAMap.put("Cp01145", "IBM01145");
/*  916 */     fJava2IANAMap.put("Cp01146", "IBM01146");
/*  917 */     fJava2IANAMap.put("Cp01147", "IBM01147");
/*  918 */     fJava2IANAMap.put("Cp01148", "IBM01148");
/*  919 */     fJava2IANAMap.put("Cp01149", "IBM01149");
/*  920 */     fJava2IANAMap.put("EUCJIS", "EUC-JP");
/*  921 */     fJava2IANAMap.put("KS_C_5601-1987", "KS_C_5601-1987");
/*  922 */     fJava2IANAMap.put("GB2312", "GB2312");
/*  923 */     fJava2IANAMap.put("ISO2022KR", "ISO-2022-KR");
/*  924 */     fJava2IANAMap.put("ISO2022CN", "ISO-2022-CN");
/*  925 */     fJava2IANAMap.put("JIS", "ISO-2022-JP");
/*  926 */     fJava2IANAMap.put("KOI8_R", "KOI8-R");
/*  927 */     fJava2IANAMap.put("KSC5601", "EUC-KR");
/*  928 */     fJava2IANAMap.put("GB18030", "GB18030");
/*  929 */     fJava2IANAMap.put("GBK", "GBK");
/*  930 */     fJava2IANAMap.put("SJIS", "SHIFT_JIS");
/*  931 */     fJava2IANAMap.put("MS932", "WINDOWS-31J");
/*  932 */     fJava2IANAMap.put("UTF8", "UTF-8");
/*  933 */     fJava2IANAMap.put("Unicode", "UTF-16");
/*  934 */     fJava2IANAMap.put("UnicodeBig", "UTF-16BE");
/*  935 */     fJava2IANAMap.put("UnicodeLittle", "UTF-16LE");
/*  936 */     fJava2IANAMap.put("JIS0201", "X0201");
/*  937 */     fJava2IANAMap.put("JIS0208", "X0208");
/*  938 */     fJava2IANAMap.put("JIS0212", "ISO-IR-159");
/*      */ 
/*  941 */     fJava2IANAMap.put("CP1047", "IBM1047");
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.util.EncodingMap
 * JD-Core Version:    0.6.2
 */