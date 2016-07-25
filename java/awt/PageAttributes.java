/*      */ package java.awt;
/*      */ 
/*      */ import java.util.Locale;
/*      */ 
/*      */ public final class PageAttributes
/*      */   implements Cloneable
/*      */ {
/*      */   private ColorType color;
/*      */   private MediaType media;
/*      */   private OrientationRequestedType orientationRequested;
/*      */   private OriginType origin;
/*      */   private PrintQualityType printQuality;
/*      */   private int[] printerResolution;
/*      */ 
/*      */   public PageAttributes()
/*      */   {
/*  913 */     setColor(ColorType.MONOCHROME);
/*  914 */     setMediaToDefault();
/*  915 */     setOrientationRequestedToDefault();
/*  916 */     setOrigin(OriginType.PHYSICAL);
/*  917 */     setPrintQualityToDefault();
/*  918 */     setPrinterResolutionToDefault();
/*      */   }
/*      */ 
/*      */   public PageAttributes(PageAttributes paramPageAttributes)
/*      */   {
/*  928 */     set(paramPageAttributes);
/*      */   }
/*      */ 
/*      */   public PageAttributes(ColorType paramColorType, MediaType paramMediaType, OrientationRequestedType paramOrientationRequestedType, OriginType paramOriginType, PrintQualityType paramPrintQualityType, int[] paramArrayOfInt)
/*      */   {
/*  953 */     setColor(paramColorType);
/*  954 */     setMedia(paramMediaType);
/*  955 */     setOrientationRequested(paramOrientationRequestedType);
/*  956 */     setOrigin(paramOriginType);
/*  957 */     setPrintQuality(paramPrintQualityType);
/*  958 */     setPrinterResolution(paramArrayOfInt);
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*      */     try
/*      */     {
/*  969 */       return super.clone();
/*      */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */     }
/*  972 */     throw new InternalError();
/*      */   }
/*      */ 
/*      */   public void set(PageAttributes paramPageAttributes)
/*      */   {
/*  983 */     this.color = paramPageAttributes.color;
/*  984 */     this.media = paramPageAttributes.media;
/*  985 */     this.orientationRequested = paramPageAttributes.orientationRequested;
/*  986 */     this.origin = paramPageAttributes.origin;
/*  987 */     this.printQuality = paramPageAttributes.printQuality;
/*      */ 
/*  989 */     this.printerResolution = paramPageAttributes.printerResolution;
/*      */   }
/*      */ 
/*      */   public ColorType getColor()
/*      */   {
/* 1000 */     return this.color;
/*      */   }
/*      */ 
/*      */   public void setColor(ColorType paramColorType)
/*      */   {
/* 1012 */     if (paramColorType == null) {
/* 1013 */       throw new IllegalArgumentException("Invalid value for attribute color");
/*      */     }
/*      */ 
/* 1016 */     this.color = paramColorType;
/*      */   }
/*      */ 
/*      */   public MediaType getMedia()
/*      */   {
/* 1026 */     return this.media;
/*      */   }
/*      */ 
/*      */   public void setMedia(MediaType paramMediaType)
/*      */   {
/* 1043 */     if (paramMediaType == null) {
/* 1044 */       throw new IllegalArgumentException("Invalid value for attribute media");
/*      */     }
/*      */ 
/* 1047 */     this.media = paramMediaType;
/*      */   }
/*      */ 
/*      */   public void setMediaToDefault()
/*      */   {
/* 1057 */     String str = Locale.getDefault().getCountry();
/* 1058 */     if ((str != null) && ((str.equals(Locale.US.getCountry())) || (str.equals(Locale.CANADA.getCountry()))))
/*      */     {
/* 1061 */       setMedia(MediaType.NA_LETTER);
/*      */     }
/* 1063 */     else setMedia(MediaType.ISO_A4);
/*      */   }
/*      */ 
/*      */   public OrientationRequestedType getOrientationRequested()
/*      */   {
/* 1075 */     return this.orientationRequested;
/*      */   }
/*      */ 
/*      */   public void setOrientationRequested(OrientationRequestedType paramOrientationRequestedType)
/*      */   {
/* 1089 */     if (paramOrientationRequestedType == null) {
/* 1090 */       throw new IllegalArgumentException("Invalid value for attribute orientationRequested");
/*      */     }
/*      */ 
/* 1093 */     this.orientationRequested = paramOrientationRequestedType;
/*      */   }
/*      */ 
/*      */   public void setOrientationRequested(int paramInt)
/*      */   {
/* 1108 */     switch (paramInt) {
/*      */     case 3:
/* 1110 */       setOrientationRequested(OrientationRequestedType.PORTRAIT);
/* 1111 */       break;
/*      */     case 4:
/* 1113 */       setOrientationRequested(OrientationRequestedType.LANDSCAPE);
/* 1114 */       break;
/*      */     default:
/* 1117 */       setOrientationRequested(null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setOrientationRequestedToDefault()
/*      */   {
/* 1127 */     setOrientationRequested(OrientationRequestedType.PORTRAIT);
/*      */   }
/*      */ 
/*      */   public OriginType getOrigin()
/*      */   {
/* 1141 */     return this.origin;
/*      */   }
/*      */ 
/*      */   public void setOrigin(OriginType paramOriginType)
/*      */   {
/* 1155 */     if (paramOriginType == null) {
/* 1156 */       throw new IllegalArgumentException("Invalid value for attribute origin");
/*      */     }
/*      */ 
/* 1159 */     this.origin = paramOriginType;
/*      */   }
/*      */ 
/*      */   public PrintQualityType getPrintQuality()
/*      */   {
/* 1170 */     return this.printQuality;
/*      */   }
/*      */ 
/*      */   public void setPrintQuality(PrintQualityType paramPrintQualityType)
/*      */   {
/* 1183 */     if (paramPrintQualityType == null) {
/* 1184 */       throw new IllegalArgumentException("Invalid value for attribute printQuality");
/*      */     }
/*      */ 
/* 1187 */     this.printQuality = paramPrintQualityType;
/*      */   }
/*      */ 
/*      */   public void setPrintQuality(int paramInt)
/*      */   {
/* 1203 */     switch (paramInt) {
/*      */     case 3:
/* 1205 */       setPrintQuality(PrintQualityType.DRAFT);
/* 1206 */       break;
/*      */     case 4:
/* 1208 */       setPrintQuality(PrintQualityType.NORMAL);
/* 1209 */       break;
/*      */     case 5:
/* 1211 */       setPrintQuality(PrintQualityType.HIGH);
/* 1212 */       break;
/*      */     default:
/* 1215 */       setPrintQuality(null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setPrintQualityToDefault()
/*      */   {
/* 1225 */     setPrintQuality(PrintQualityType.NORMAL);
/*      */   }
/*      */ 
/*      */   public int[] getPrinterResolution()
/*      */   {
/* 1246 */     int[] arrayOfInt = new int[3];
/* 1247 */     arrayOfInt[0] = this.printerResolution[0];
/* 1248 */     arrayOfInt[1] = this.printerResolution[1];
/* 1249 */     arrayOfInt[2] = this.printerResolution[2];
/* 1250 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public void setPrinterResolution(int[] paramArrayOfInt)
/*      */   {
/* 1275 */     if ((paramArrayOfInt == null) || (paramArrayOfInt.length != 3) || (paramArrayOfInt[0] <= 0) || (paramArrayOfInt[1] <= 0) || ((paramArrayOfInt[2] != 3) && (paramArrayOfInt[2] != 4)))
/*      */     {
/* 1280 */       throw new IllegalArgumentException("Invalid value for attribute printerResolution");
/*      */     }
/*      */ 
/* 1286 */     int[] arrayOfInt = new int[3];
/* 1287 */     arrayOfInt[0] = paramArrayOfInt[0];
/* 1288 */     arrayOfInt[1] = paramArrayOfInt[1];
/* 1289 */     arrayOfInt[2] = paramArrayOfInt[2];
/* 1290 */     this.printerResolution = arrayOfInt;
/*      */   }
/*      */ 
/*      */   public void setPrinterResolution(int paramInt)
/*      */   {
/* 1305 */     setPrinterResolution(new int[] { paramInt, paramInt, 3 });
/*      */   }
/*      */ 
/*      */   public void setPrinterResolutionToDefault()
/*      */   {
/* 1315 */     setPrinterResolution(72);
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 1333 */     if (!(paramObject instanceof PageAttributes)) {
/* 1334 */       return false;
/*      */     }
/*      */ 
/* 1337 */     PageAttributes localPageAttributes = (PageAttributes)paramObject;
/*      */ 
/* 1339 */     return (this.color == localPageAttributes.color) && (this.media == localPageAttributes.media) && (this.orientationRequested == localPageAttributes.orientationRequested) && (this.origin == localPageAttributes.origin) && (this.printQuality == localPageAttributes.printQuality) && (this.printerResolution[0] == localPageAttributes.printerResolution[0]) && (this.printerResolution[1] == localPageAttributes.printerResolution[1]) && (this.printerResolution[2] == localPageAttributes.printerResolution[2]);
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1355 */     return this.color.hashCode() << 31 ^ this.media.hashCode() << 24 ^ this.orientationRequested.hashCode() << 23 ^ this.origin.hashCode() << 22 ^ this.printQuality.hashCode() << 20 ^ this.printerResolution[2] >> 2 << 19 ^ this.printerResolution[1] << 10 ^ this.printerResolution[0];
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1372 */     return "color=" + getColor() + ",media=" + getMedia() + ",orientation-requested=" + getOrientationRequested() + ",origin=" + getOrigin() + ",print-quality=" + getPrintQuality() + ",printer-resolution=[" + this.printerResolution[0] + "," + this.printerResolution[1] + "," + this.printerResolution[2] + "]";
/*      */   }
/*      */ 
/*      */   public static final class ColorType extends AttributeValue
/*      */   {
/*      */     private static final int I_COLOR = 0;
/*      */     private static final int I_MONOCHROME = 1;
/*   70 */     private static final String[] NAMES = { "color", "monochrome" };
/*      */ 
/*   77 */     public static final ColorType COLOR = new ColorType(0);
/*      */ 
/*   81 */     public static final ColorType MONOCHROME = new ColorType(1);
/*      */ 
/*      */     private ColorType(int paramInt) {
/*   84 */       super(NAMES);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class MediaType extends AttributeValue
/*      */   {
/*      */     private static final int I_ISO_4A0 = 0;
/*      */     private static final int I_ISO_2A0 = 1;
/*      */     private static final int I_ISO_A0 = 2;
/*      */     private static final int I_ISO_A1 = 3;
/*      */     private static final int I_ISO_A2 = 4;
/*      */     private static final int I_ISO_A3 = 5;
/*      */     private static final int I_ISO_A4 = 6;
/*      */     private static final int I_ISO_A5 = 7;
/*      */     private static final int I_ISO_A6 = 8;
/*      */     private static final int I_ISO_A7 = 9;
/*      */     private static final int I_ISO_A8 = 10;
/*      */     private static final int I_ISO_A9 = 11;
/*      */     private static final int I_ISO_A10 = 12;
/*      */     private static final int I_ISO_B0 = 13;
/*      */     private static final int I_ISO_B1 = 14;
/*      */     private static final int I_ISO_B2 = 15;
/*      */     private static final int I_ISO_B3 = 16;
/*      */     private static final int I_ISO_B4 = 17;
/*      */     private static final int I_ISO_B5 = 18;
/*      */     private static final int I_ISO_B6 = 19;
/*      */     private static final int I_ISO_B7 = 20;
/*      */     private static final int I_ISO_B8 = 21;
/*      */     private static final int I_ISO_B9 = 22;
/*      */     private static final int I_ISO_B10 = 23;
/*      */     private static final int I_JIS_B0 = 24;
/*      */     private static final int I_JIS_B1 = 25;
/*      */     private static final int I_JIS_B2 = 26;
/*      */     private static final int I_JIS_B3 = 27;
/*      */     private static final int I_JIS_B4 = 28;
/*      */     private static final int I_JIS_B5 = 29;
/*      */     private static final int I_JIS_B6 = 30;
/*      */     private static final int I_JIS_B7 = 31;
/*      */     private static final int I_JIS_B8 = 32;
/*      */     private static final int I_JIS_B9 = 33;
/*      */     private static final int I_JIS_B10 = 34;
/*      */     private static final int I_ISO_C0 = 35;
/*      */     private static final int I_ISO_C1 = 36;
/*      */     private static final int I_ISO_C2 = 37;
/*      */     private static final int I_ISO_C3 = 38;
/*      */     private static final int I_ISO_C4 = 39;
/*      */     private static final int I_ISO_C5 = 40;
/*      */     private static final int I_ISO_C6 = 41;
/*      */     private static final int I_ISO_C7 = 42;
/*      */     private static final int I_ISO_C8 = 43;
/*      */     private static final int I_ISO_C9 = 44;
/*      */     private static final int I_ISO_C10 = 45;
/*      */     private static final int I_ISO_DESIGNATED_LONG = 46;
/*      */     private static final int I_EXECUTIVE = 47;
/*      */     private static final int I_FOLIO = 48;
/*      */     private static final int I_INVOICE = 49;
/*      */     private static final int I_LEDGER = 50;
/*      */     private static final int I_NA_LETTER = 51;
/*      */     private static final int I_NA_LEGAL = 52;
/*      */     private static final int I_QUARTO = 53;
/*      */     private static final int I_A = 54;
/*      */     private static final int I_B = 55;
/*      */     private static final int I_C = 56;
/*      */     private static final int I_D = 57;
/*      */     private static final int I_E = 58;
/*      */     private static final int I_NA_10X15_ENVELOPE = 59;
/*      */     private static final int I_NA_10X14_ENVELOPE = 60;
/*      */     private static final int I_NA_10X13_ENVELOPE = 61;
/*      */     private static final int I_NA_9X12_ENVELOPE = 62;
/*      */     private static final int I_NA_9X11_ENVELOPE = 63;
/*      */     private static final int I_NA_7X9_ENVELOPE = 64;
/*      */     private static final int I_NA_6X9_ENVELOPE = 65;
/*      */     private static final int I_NA_NUMBER_9_ENVELOPE = 66;
/*      */     private static final int I_NA_NUMBER_10_ENVELOPE = 67;
/*      */     private static final int I_NA_NUMBER_11_ENVELOPE = 68;
/*      */     private static final int I_NA_NUMBER_12_ENVELOPE = 69;
/*      */     private static final int I_NA_NUMBER_14_ENVELOPE = 70;
/*      */     private static final int I_INVITE_ENVELOPE = 71;
/*      */     private static final int I_ITALY_ENVELOPE = 72;
/*      */     private static final int I_MONARCH_ENVELOPE = 73;
/*      */     private static final int I_PERSONAL_ENVELOPE = 74;
/*  170 */     private static final String[] NAMES = { "iso-4a0", "iso-2a0", "iso-a0", "iso-a1", "iso-a2", "iso-a3", "iso-a4", "iso-a5", "iso-a6", "iso-a7", "iso-a8", "iso-a9", "iso-a10", "iso-b0", "iso-b1", "iso-b2", "iso-b3", "iso-b4", "iso-b5", "iso-b6", "iso-b7", "iso-b8", "iso-b9", "iso-b10", "jis-b0", "jis-b1", "jis-b2", "jis-b3", "jis-b4", "jis-b5", "jis-b6", "jis-b7", "jis-b8", "jis-b9", "jis-b10", "iso-c0", "iso-c1", "iso-c2", "iso-c3", "iso-c4", "iso-c5", "iso-c6", "iso-c7", "iso-c8", "iso-c9", "iso-c10", "iso-designated-long", "executive", "folio", "invoice", "ledger", "na-letter", "na-legal", "quarto", "a", "b", "c", "d", "e", "na-10x15-envelope", "na-10x14-envelope", "na-10x13-envelope", "na-9x12-envelope", "na-9x11-envelope", "na-7x9-envelope", "na-6x9-envelope", "na-number-9-envelope", "na-number-10-envelope", "na-number-11-envelope", "na-number-12-envelope", "na-number-14-envelope", "invite-envelope", "italy-envelope", "monarch-envelope", "personal-envelope" };
/*      */ 
/*  192 */     public static final MediaType ISO_4A0 = new MediaType(0);
/*      */ 
/*  196 */     public static final MediaType ISO_2A0 = new MediaType(1);
/*      */ 
/*  200 */     public static final MediaType ISO_A0 = new MediaType(2);
/*      */ 
/*  204 */     public static final MediaType ISO_A1 = new MediaType(3);
/*      */ 
/*  208 */     public static final MediaType ISO_A2 = new MediaType(4);
/*      */ 
/*  212 */     public static final MediaType ISO_A3 = new MediaType(5);
/*      */ 
/*  216 */     public static final MediaType ISO_A4 = new MediaType(6);
/*      */ 
/*  220 */     public static final MediaType ISO_A5 = new MediaType(7);
/*      */ 
/*  224 */     public static final MediaType ISO_A6 = new MediaType(8);
/*      */ 
/*  228 */     public static final MediaType ISO_A7 = new MediaType(9);
/*      */ 
/*  232 */     public static final MediaType ISO_A8 = new MediaType(10);
/*      */ 
/*  236 */     public static final MediaType ISO_A9 = new MediaType(11);
/*      */ 
/*  240 */     public static final MediaType ISO_A10 = new MediaType(12);
/*      */ 
/*  244 */     public static final MediaType ISO_B0 = new MediaType(13);
/*      */ 
/*  248 */     public static final MediaType ISO_B1 = new MediaType(14);
/*      */ 
/*  252 */     public static final MediaType ISO_B2 = new MediaType(15);
/*      */ 
/*  256 */     public static final MediaType ISO_B3 = new MediaType(16);
/*      */ 
/*  260 */     public static final MediaType ISO_B4 = new MediaType(17);
/*      */ 
/*  264 */     public static final MediaType ISO_B5 = new MediaType(18);
/*      */ 
/*  268 */     public static final MediaType ISO_B6 = new MediaType(19);
/*      */ 
/*  272 */     public static final MediaType ISO_B7 = new MediaType(20);
/*      */ 
/*  276 */     public static final MediaType ISO_B8 = new MediaType(21);
/*      */ 
/*  280 */     public static final MediaType ISO_B9 = new MediaType(22);
/*      */ 
/*  284 */     public static final MediaType ISO_B10 = new MediaType(23);
/*      */ 
/*  288 */     public static final MediaType JIS_B0 = new MediaType(24);
/*      */ 
/*  292 */     public static final MediaType JIS_B1 = new MediaType(25);
/*      */ 
/*  296 */     public static final MediaType JIS_B2 = new MediaType(26);
/*      */ 
/*  300 */     public static final MediaType JIS_B3 = new MediaType(27);
/*      */ 
/*  304 */     public static final MediaType JIS_B4 = new MediaType(28);
/*      */ 
/*  308 */     public static final MediaType JIS_B5 = new MediaType(29);
/*      */ 
/*  312 */     public static final MediaType JIS_B6 = new MediaType(30);
/*      */ 
/*  316 */     public static final MediaType JIS_B7 = new MediaType(31);
/*      */ 
/*  320 */     public static final MediaType JIS_B8 = new MediaType(32);
/*      */ 
/*  324 */     public static final MediaType JIS_B9 = new MediaType(33);
/*      */ 
/*  328 */     public static final MediaType JIS_B10 = new MediaType(34);
/*      */ 
/*  332 */     public static final MediaType ISO_C0 = new MediaType(35);
/*      */ 
/*  336 */     public static final MediaType ISO_C1 = new MediaType(36);
/*      */ 
/*  340 */     public static final MediaType ISO_C2 = new MediaType(37);
/*      */ 
/*  344 */     public static final MediaType ISO_C3 = new MediaType(38);
/*      */ 
/*  348 */     public static final MediaType ISO_C4 = new MediaType(39);
/*      */ 
/*  352 */     public static final MediaType ISO_C5 = new MediaType(40);
/*      */ 
/*  356 */     public static final MediaType ISO_C6 = new MediaType(41);
/*      */ 
/*  360 */     public static final MediaType ISO_C7 = new MediaType(42);
/*      */ 
/*  364 */     public static final MediaType ISO_C8 = new MediaType(43);
/*      */ 
/*  368 */     public static final MediaType ISO_C9 = new MediaType(44);
/*      */ 
/*  372 */     public static final MediaType ISO_C10 = new MediaType(45);
/*      */ 
/*  376 */     public static final MediaType ISO_DESIGNATED_LONG = new MediaType(46);
/*      */ 
/*  381 */     public static final MediaType EXECUTIVE = new MediaType(47);
/*      */ 
/*  385 */     public static final MediaType FOLIO = new MediaType(48);
/*      */ 
/*  389 */     public static final MediaType INVOICE = new MediaType(49);
/*      */ 
/*  393 */     public static final MediaType LEDGER = new MediaType(50);
/*      */ 
/*  397 */     public static final MediaType NA_LETTER = new MediaType(51);
/*      */ 
/*  401 */     public static final MediaType NA_LEGAL = new MediaType(52);
/*      */ 
/*  405 */     public static final MediaType QUARTO = new MediaType(53);
/*      */ 
/*  409 */     public static final MediaType A = new MediaType(54);
/*      */ 
/*  413 */     public static final MediaType B = new MediaType(55);
/*      */ 
/*  417 */     public static final MediaType C = new MediaType(56);
/*      */ 
/*  421 */     public static final MediaType D = new MediaType(57);
/*      */ 
/*  425 */     public static final MediaType E = new MediaType(58);
/*      */ 
/*  429 */     public static final MediaType NA_10X15_ENVELOPE = new MediaType(59);
/*      */ 
/*  434 */     public static final MediaType NA_10X14_ENVELOPE = new MediaType(60);
/*      */ 
/*  439 */     public static final MediaType NA_10X13_ENVELOPE = new MediaType(61);
/*      */ 
/*  444 */     public static final MediaType NA_9X12_ENVELOPE = new MediaType(62);
/*      */ 
/*  449 */     public static final MediaType NA_9X11_ENVELOPE = new MediaType(63);
/*      */ 
/*  454 */     public static final MediaType NA_7X9_ENVELOPE = new MediaType(64);
/*      */ 
/*  459 */     public static final MediaType NA_6X9_ENVELOPE = new MediaType(65);
/*      */ 
/*  465 */     public static final MediaType NA_NUMBER_9_ENVELOPE = new MediaType(66);
/*      */ 
/*  471 */     public static final MediaType NA_NUMBER_10_ENVELOPE = new MediaType(67);
/*      */ 
/*  477 */     public static final MediaType NA_NUMBER_11_ENVELOPE = new MediaType(68);
/*      */ 
/*  483 */     public static final MediaType NA_NUMBER_12_ENVELOPE = new MediaType(69);
/*      */ 
/*  489 */     public static final MediaType NA_NUMBER_14_ENVELOPE = new MediaType(70);
/*      */ 
/*  494 */     public static final MediaType INVITE_ENVELOPE = new MediaType(71);
/*      */ 
/*  499 */     public static final MediaType ITALY_ENVELOPE = new MediaType(72);
/*      */ 
/*  504 */     public static final MediaType MONARCH_ENVELOPE = new MediaType(73);
/*      */ 
/*  509 */     public static final MediaType PERSONAL_ENVELOPE = new MediaType(74);
/*      */ 
/*  514 */     public static final MediaType A0 = ISO_A0;
/*      */ 
/*  518 */     public static final MediaType A1 = ISO_A1;
/*      */ 
/*  522 */     public static final MediaType A2 = ISO_A2;
/*      */ 
/*  526 */     public static final MediaType A3 = ISO_A3;
/*      */ 
/*  530 */     public static final MediaType A4 = ISO_A4;
/*      */ 
/*  534 */     public static final MediaType A5 = ISO_A5;
/*      */ 
/*  538 */     public static final MediaType A6 = ISO_A6;
/*      */ 
/*  542 */     public static final MediaType A7 = ISO_A7;
/*      */ 
/*  546 */     public static final MediaType A8 = ISO_A8;
/*      */ 
/*  550 */     public static final MediaType A9 = ISO_A9;
/*      */ 
/*  554 */     public static final MediaType A10 = ISO_A10;
/*      */ 
/*  558 */     public static final MediaType B0 = ISO_B0;
/*      */ 
/*  562 */     public static final MediaType B1 = ISO_B1;
/*      */ 
/*  566 */     public static final MediaType B2 = ISO_B2;
/*      */ 
/*  570 */     public static final MediaType B3 = ISO_B3;
/*      */ 
/*  574 */     public static final MediaType B4 = ISO_B4;
/*      */ 
/*  578 */     public static final MediaType ISO_B4_ENVELOPE = ISO_B4;
/*      */ 
/*  582 */     public static final MediaType B5 = ISO_B5;
/*      */ 
/*  586 */     public static final MediaType ISO_B5_ENVELOPE = ISO_B5;
/*      */ 
/*  590 */     public static final MediaType B6 = ISO_B6;
/*      */ 
/*  594 */     public static final MediaType B7 = ISO_B7;
/*      */ 
/*  598 */     public static final MediaType B8 = ISO_B8;
/*      */ 
/*  602 */     public static final MediaType B9 = ISO_B9;
/*      */ 
/*  606 */     public static final MediaType B10 = ISO_B10;
/*      */ 
/*  610 */     public static final MediaType C0 = ISO_C0;
/*      */ 
/*  614 */     public static final MediaType ISO_C0_ENVELOPE = ISO_C0;
/*      */ 
/*  618 */     public static final MediaType C1 = ISO_C1;
/*      */ 
/*  622 */     public static final MediaType ISO_C1_ENVELOPE = ISO_C1;
/*      */ 
/*  626 */     public static final MediaType C2 = ISO_C2;
/*      */ 
/*  630 */     public static final MediaType ISO_C2_ENVELOPE = ISO_C2;
/*      */ 
/*  634 */     public static final MediaType C3 = ISO_C3;
/*      */ 
/*  638 */     public static final MediaType ISO_C3_ENVELOPE = ISO_C3;
/*      */ 
/*  642 */     public static final MediaType C4 = ISO_C4;
/*      */ 
/*  646 */     public static final MediaType ISO_C4_ENVELOPE = ISO_C4;
/*      */ 
/*  650 */     public static final MediaType C5 = ISO_C5;
/*      */ 
/*  654 */     public static final MediaType ISO_C5_ENVELOPE = ISO_C5;
/*      */ 
/*  658 */     public static final MediaType C6 = ISO_C6;
/*      */ 
/*  662 */     public static final MediaType ISO_C6_ENVELOPE = ISO_C6;
/*      */ 
/*  666 */     public static final MediaType C7 = ISO_C7;
/*      */ 
/*  670 */     public static final MediaType ISO_C7_ENVELOPE = ISO_C7;
/*      */ 
/*  674 */     public static final MediaType C8 = ISO_C8;
/*      */ 
/*  678 */     public static final MediaType ISO_C8_ENVELOPE = ISO_C8;
/*      */ 
/*  682 */     public static final MediaType C9 = ISO_C9;
/*      */ 
/*  686 */     public static final MediaType ISO_C9_ENVELOPE = ISO_C9;
/*      */ 
/*  690 */     public static final MediaType C10 = ISO_C10;
/*      */ 
/*  694 */     public static final MediaType ISO_C10_ENVELOPE = ISO_C10;
/*      */ 
/*  698 */     public static final MediaType ISO_DESIGNATED_LONG_ENVELOPE = ISO_DESIGNATED_LONG;
/*      */ 
/*  703 */     public static final MediaType STATEMENT = INVOICE;
/*      */ 
/*  707 */     public static final MediaType TABLOID = LEDGER;
/*      */ 
/*  711 */     public static final MediaType LETTER = NA_LETTER;
/*      */ 
/*  715 */     public static final MediaType NOTE = NA_LETTER;
/*      */ 
/*  719 */     public static final MediaType LEGAL = NA_LEGAL;
/*      */ 
/*  723 */     public static final MediaType ENV_10X15 = NA_10X15_ENVELOPE;
/*      */ 
/*  727 */     public static final MediaType ENV_10X14 = NA_10X14_ENVELOPE;
/*      */ 
/*  731 */     public static final MediaType ENV_10X13 = NA_10X13_ENVELOPE;
/*      */ 
/*  735 */     public static final MediaType ENV_9X12 = NA_9X12_ENVELOPE;
/*      */ 
/*  739 */     public static final MediaType ENV_9X11 = NA_9X11_ENVELOPE;
/*      */ 
/*  743 */     public static final MediaType ENV_7X9 = NA_7X9_ENVELOPE;
/*      */ 
/*  747 */     public static final MediaType ENV_6X9 = NA_6X9_ENVELOPE;
/*      */ 
/*  751 */     public static final MediaType ENV_9 = NA_NUMBER_9_ENVELOPE;
/*      */ 
/*  755 */     public static final MediaType ENV_10 = NA_NUMBER_10_ENVELOPE;
/*      */ 
/*  759 */     public static final MediaType ENV_11 = NA_NUMBER_11_ENVELOPE;
/*      */ 
/*  763 */     public static final MediaType ENV_12 = NA_NUMBER_12_ENVELOPE;
/*      */ 
/*  767 */     public static final MediaType ENV_14 = NA_NUMBER_14_ENVELOPE;
/*      */ 
/*  771 */     public static final MediaType ENV_INVITE = INVITE_ENVELOPE;
/*      */ 
/*  775 */     public static final MediaType ENV_ITALY = ITALY_ENVELOPE;
/*      */ 
/*  779 */     public static final MediaType ENV_MONARCH = MONARCH_ENVELOPE;
/*      */ 
/*  783 */     public static final MediaType ENV_PERSONAL = PERSONAL_ENVELOPE;
/*      */ 
/*  787 */     public static final MediaType INVITE = INVITE_ENVELOPE;
/*      */ 
/*  791 */     public static final MediaType ITALY = ITALY_ENVELOPE;
/*      */ 
/*  795 */     public static final MediaType MONARCH = MONARCH_ENVELOPE;
/*      */ 
/*  799 */     public static final MediaType PERSONAL = PERSONAL_ENVELOPE;
/*      */ 
/*      */     private MediaType(int paramInt) {
/*  802 */       super(NAMES);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class OrientationRequestedType extends AttributeValue
/*      */   {
/*      */     private static final int I_PORTRAIT = 0;
/*      */     private static final int I_LANDSCAPE = 1;
/*  815 */     private static final String[] NAMES = { "portrait", "landscape" };
/*      */ 
/*  823 */     public static final OrientationRequestedType PORTRAIT = new OrientationRequestedType(0);
/*      */ 
/*  829 */     public static final OrientationRequestedType LANDSCAPE = new OrientationRequestedType(1);
/*      */ 
/*      */     private OrientationRequestedType(int paramInt)
/*      */     {
/*  833 */       super(NAMES);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class OriginType extends AttributeValue
/*      */   {
/*      */     private static final int I_PHYSICAL = 0;
/*      */     private static final int I_PRINTABLE = 1;
/*  845 */     private static final String[] NAMES = { "physical", "printable" };
/*      */ 
/*  852 */     public static final OriginType PHYSICAL = new OriginType(0);
/*      */ 
/*  856 */     public static final OriginType PRINTABLE = new OriginType(1);
/*      */ 
/*      */     private OriginType(int paramInt) {
/*  859 */       super(NAMES);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class PrintQualityType extends AttributeValue
/*      */   {
/*      */     private static final int I_HIGH = 0;
/*      */     private static final int I_NORMAL = 1;
/*      */     private static final int I_DRAFT = 2;
/*  873 */     private static final String[] NAMES = { "high", "normal", "draft" };
/*      */ 
/*  881 */     public static final PrintQualityType HIGH = new PrintQualityType(0);
/*      */ 
/*  887 */     public static final PrintQualityType NORMAL = new PrintQualityType(1);
/*      */ 
/*  893 */     public static final PrintQualityType DRAFT = new PrintQualityType(2);
/*      */ 
/*      */     private PrintQualityType(int paramInt)
/*      */     {
/*  897 */       super(NAMES);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.PageAttributes
 * JD-Core Version:    0.6.2
 */