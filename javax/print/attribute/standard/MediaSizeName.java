/*     */ package javax.print.attribute.standard;
/*     */ 
/*     */ import javax.print.attribute.EnumSyntax;
/*     */ 
/*     */ public class MediaSizeName extends Media
/*     */ {
/*     */   private static final long serialVersionUID = 2778798329756942747L;
/*  54 */   public static final MediaSizeName ISO_A0 = new MediaSizeName(0);
/*     */ 
/*  58 */   public static final MediaSizeName ISO_A1 = new MediaSizeName(1);
/*     */ 
/*  62 */   public static final MediaSizeName ISO_A2 = new MediaSizeName(2);
/*     */ 
/*  66 */   public static final MediaSizeName ISO_A3 = new MediaSizeName(3);
/*     */ 
/*  70 */   public static final MediaSizeName ISO_A4 = new MediaSizeName(4);
/*     */ 
/*  74 */   public static final MediaSizeName ISO_A5 = new MediaSizeName(5);
/*     */ 
/*  78 */   public static final MediaSizeName ISO_A6 = new MediaSizeName(6);
/*     */ 
/*  82 */   public static final MediaSizeName ISO_A7 = new MediaSizeName(7);
/*     */ 
/*  86 */   public static final MediaSizeName ISO_A8 = new MediaSizeName(8);
/*     */ 
/*  90 */   public static final MediaSizeName ISO_A9 = new MediaSizeName(9);
/*     */ 
/*  94 */   public static final MediaSizeName ISO_A10 = new MediaSizeName(10);
/*     */ 
/*  99 */   public static final MediaSizeName ISO_B0 = new MediaSizeName(11);
/*     */ 
/* 103 */   public static final MediaSizeName ISO_B1 = new MediaSizeName(12);
/*     */ 
/* 107 */   public static final MediaSizeName ISO_B2 = new MediaSizeName(13);
/*     */ 
/* 111 */   public static final MediaSizeName ISO_B3 = new MediaSizeName(14);
/*     */ 
/* 115 */   public static final MediaSizeName ISO_B4 = new MediaSizeName(15);
/*     */ 
/* 119 */   public static final MediaSizeName ISO_B5 = new MediaSizeName(16);
/*     */ 
/* 123 */   public static final MediaSizeName ISO_B6 = new MediaSizeName(17);
/*     */ 
/* 127 */   public static final MediaSizeName ISO_B7 = new MediaSizeName(18);
/*     */ 
/* 131 */   public static final MediaSizeName ISO_B8 = new MediaSizeName(19);
/*     */ 
/* 135 */   public static final MediaSizeName ISO_B9 = new MediaSizeName(20);
/*     */ 
/* 139 */   public static final MediaSizeName ISO_B10 = new MediaSizeName(21);
/*     */ 
/* 144 */   public static final MediaSizeName JIS_B0 = new MediaSizeName(22);
/*     */ 
/* 148 */   public static final MediaSizeName JIS_B1 = new MediaSizeName(23);
/*     */ 
/* 152 */   public static final MediaSizeName JIS_B2 = new MediaSizeName(24);
/*     */ 
/* 156 */   public static final MediaSizeName JIS_B3 = new MediaSizeName(25);
/*     */ 
/* 160 */   public static final MediaSizeName JIS_B4 = new MediaSizeName(26);
/*     */ 
/* 164 */   public static final MediaSizeName JIS_B5 = new MediaSizeName(27);
/*     */ 
/* 168 */   public static final MediaSizeName JIS_B6 = new MediaSizeName(28);
/*     */ 
/* 172 */   public static final MediaSizeName JIS_B7 = new MediaSizeName(29);
/*     */ 
/* 176 */   public static final MediaSizeName JIS_B8 = new MediaSizeName(30);
/*     */ 
/* 180 */   public static final MediaSizeName JIS_B9 = new MediaSizeName(31);
/*     */ 
/* 184 */   public static final MediaSizeName JIS_B10 = new MediaSizeName(32);
/*     */ 
/* 189 */   public static final MediaSizeName ISO_C0 = new MediaSizeName(33);
/*     */ 
/* 193 */   public static final MediaSizeName ISO_C1 = new MediaSizeName(34);
/*     */ 
/* 197 */   public static final MediaSizeName ISO_C2 = new MediaSizeName(35);
/*     */ 
/* 201 */   public static final MediaSizeName ISO_C3 = new MediaSizeName(36);
/*     */ 
/* 205 */   public static final MediaSizeName ISO_C4 = new MediaSizeName(37);
/*     */ 
/* 209 */   public static final MediaSizeName ISO_C5 = new MediaSizeName(38);
/*     */ 
/* 213 */   public static final MediaSizeName ISO_C6 = new MediaSizeName(39);
/*     */ 
/* 217 */   public static final MediaSizeName NA_LETTER = new MediaSizeName(40);
/*     */ 
/* 222 */   public static final MediaSizeName NA_LEGAL = new MediaSizeName(41);
/*     */ 
/* 227 */   public static final MediaSizeName EXECUTIVE = new MediaSizeName(42);
/*     */ 
/* 232 */   public static final MediaSizeName LEDGER = new MediaSizeName(43);
/*     */ 
/* 237 */   public static final MediaSizeName TABLOID = new MediaSizeName(44);
/*     */ 
/* 242 */   public static final MediaSizeName INVOICE = new MediaSizeName(45);
/*     */ 
/* 247 */   public static final MediaSizeName FOLIO = new MediaSizeName(46);
/*     */ 
/* 252 */   public static final MediaSizeName QUARTO = new MediaSizeName(47);
/*     */ 
/* 258 */   public static final MediaSizeName JAPANESE_POSTCARD = new MediaSizeName(48);
/*     */ 
/* 263 */   public static final MediaSizeName JAPANESE_DOUBLE_POSTCARD = new MediaSizeName(49);
/*     */ 
/* 268 */   public static final MediaSizeName A = new MediaSizeName(50);
/*     */ 
/* 273 */   public static final MediaSizeName B = new MediaSizeName(51);
/*     */ 
/* 278 */   public static final MediaSizeName C = new MediaSizeName(52);
/*     */ 
/* 283 */   public static final MediaSizeName D = new MediaSizeName(53);
/*     */ 
/* 288 */   public static final MediaSizeName E = new MediaSizeName(54);
/*     */ 
/* 294 */   public static final MediaSizeName ISO_DESIGNATED_LONG = new MediaSizeName(55);
/*     */ 
/* 300 */   public static final MediaSizeName ITALY_ENVELOPE = new MediaSizeName(56);
/*     */ 
/* 306 */   public static final MediaSizeName MONARCH_ENVELOPE = new MediaSizeName(57);
/*     */ 
/* 311 */   public static final MediaSizeName PERSONAL_ENVELOPE = new MediaSizeName(58);
/*     */ 
/* 316 */   public static final MediaSizeName NA_NUMBER_9_ENVELOPE = new MediaSizeName(59);
/*     */ 
/* 321 */   public static final MediaSizeName NA_NUMBER_10_ENVELOPE = new MediaSizeName(60);
/*     */ 
/* 326 */   public static final MediaSizeName NA_NUMBER_11_ENVELOPE = new MediaSizeName(61);
/*     */ 
/* 331 */   public static final MediaSizeName NA_NUMBER_12_ENVELOPE = new MediaSizeName(62);
/*     */ 
/* 336 */   public static final MediaSizeName NA_NUMBER_14_ENVELOPE = new MediaSizeName(63);
/*     */ 
/* 341 */   public static final MediaSizeName NA_6X9_ENVELOPE = new MediaSizeName(64);
/*     */ 
/* 346 */   public static final MediaSizeName NA_7X9_ENVELOPE = new MediaSizeName(65);
/*     */ 
/* 351 */   public static final MediaSizeName NA_9X11_ENVELOPE = new MediaSizeName(66);
/*     */ 
/* 356 */   public static final MediaSizeName NA_9X12_ENVELOPE = new MediaSizeName(67);
/*     */ 
/* 362 */   public static final MediaSizeName NA_10X13_ENVELOPE = new MediaSizeName(68);
/*     */ 
/* 367 */   public static final MediaSizeName NA_10X14_ENVELOPE = new MediaSizeName(69);
/*     */ 
/* 372 */   public static final MediaSizeName NA_10X15_ENVELOPE = new MediaSizeName(70);
/*     */ 
/* 378 */   public static final MediaSizeName NA_5X7 = new MediaSizeName(71);
/*     */ 
/* 384 */   public static final MediaSizeName NA_8X10 = new MediaSizeName(72);
/*     */ 
/* 396 */   private static final String[] myStringTable = { "iso-a0", "iso-a1", "iso-a2", "iso-a3", "iso-a4", "iso-a5", "iso-a6", "iso-a7", "iso-a8", "iso-a9", "iso-a10", "iso-b0", "iso-b1", "iso-b2", "iso-b3", "iso-b4", "iso-b5", "iso-b6", "iso-b7", "iso-b8", "iso-b9", "iso-b10", "jis-b0", "jis-b1", "jis-b2", "jis-b3", "jis-b4", "jis-b5", "jis-b6", "jis-b7", "jis-b8", "jis-b9", "jis-b10", "iso-c0", "iso-c1", "iso-c2", "iso-c3", "iso-c4", "iso-c5", "iso-c6", "na-letter", "na-legal", "executive", "ledger", "tabloid", "invoice", "folio", "quarto", "japanese-postcard", "oufuko-postcard", "a", "b", "c", "d", "e", "iso-designated-long", "italian-envelope", "monarch-envelope", "personal-envelope", "na-number-9-envelope", "na-number-10-envelope", "na-number-11-envelope", "na-number-12-envelope", "na-number-14-envelope", "na-6x9-envelope", "na-7x9-envelope", "na-9x11-envelope", "na-9x12-envelope", "na-10x13-envelope", "na-10x14-envelope", "na-10x15-envelope", "na-5x7", "na-8x10" };
/*     */ 
/* 472 */   private static final MediaSizeName[] myEnumValueTable = { ISO_A0, ISO_A1, ISO_A2, ISO_A3, ISO_A4, ISO_A5, ISO_A6, ISO_A7, ISO_A8, ISO_A9, ISO_A10, ISO_B0, ISO_B1, ISO_B2, ISO_B3, ISO_B4, ISO_B5, ISO_B6, ISO_B7, ISO_B8, ISO_B9, ISO_B10, JIS_B0, JIS_B1, JIS_B2, JIS_B3, JIS_B4, JIS_B5, JIS_B6, JIS_B7, JIS_B8, JIS_B9, JIS_B10, ISO_C0, ISO_C1, ISO_C2, ISO_C3, ISO_C4, ISO_C5, ISO_C6, NA_LETTER, NA_LEGAL, EXECUTIVE, LEDGER, TABLOID, INVOICE, FOLIO, QUARTO, JAPANESE_POSTCARD, JAPANESE_DOUBLE_POSTCARD, A, B, C, D, E, ISO_DESIGNATED_LONG, ITALY_ENVELOPE, MONARCH_ENVELOPE, PERSONAL_ENVELOPE, NA_NUMBER_9_ENVELOPE, NA_NUMBER_10_ENVELOPE, NA_NUMBER_11_ENVELOPE, NA_NUMBER_12_ENVELOPE, NA_NUMBER_14_ENVELOPE, NA_6X9_ENVELOPE, NA_7X9_ENVELOPE, NA_9X11_ENVELOPE, NA_9X12_ENVELOPE, NA_10X13_ENVELOPE, NA_10X14_ENVELOPE, NA_10X15_ENVELOPE, NA_5X7, NA_8X10 };
/*     */ 
/*     */   protected MediaSizeName(int paramInt)
/*     */   {
/* 393 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   protected String[] getStringTable()
/*     */   {
/* 554 */     return (String[])myStringTable.clone();
/*     */   }
/*     */ 
/*     */   protected EnumSyntax[] getEnumValueTable()
/*     */   {
/* 561 */     return (EnumSyntax[])myEnumValueTable.clone();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.standard.MediaSizeName
 * JD-Core Version:    0.6.2
 */