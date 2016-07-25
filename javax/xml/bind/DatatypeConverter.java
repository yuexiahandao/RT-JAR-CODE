/*     */ package javax.xml.bind;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.util.Calendar;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public final class DatatypeConverter
/*     */ {
/*  97 */   private static DatatypeConverterInterface theConverter = null;
/*     */ 
/*  99 */   private static final JAXBPermission SET_DATATYPE_CONVERTER_PERMISSION = new JAXBPermission("setDatatypeConverter");
/*     */ 
/*     */   public static void setDatatypeConverter(DatatypeConverterInterface converter)
/*     */   {
/* 128 */     if (converter == null) {
/* 129 */       throw new IllegalArgumentException(Messages.format("DatatypeConverter.ConverterMustNotBeNull"));
/*     */     }
/* 131 */     if (theConverter == null) {
/* 132 */       SecurityManager sm = System.getSecurityManager();
/* 133 */       if (sm != null)
/* 134 */         sm.checkPermission(SET_DATATYPE_CONVERTER_PERMISSION);
/* 135 */       theConverter = converter;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static synchronized void initConverter() {
/* 140 */     theConverter = new DatatypeConverterImpl();
/*     */   }
/*     */ 
/*     */   public static String parseString(String lexicalXSDString)
/*     */   {
/* 153 */     if (theConverter == null) initConverter();
/* 154 */     return theConverter.parseString(lexicalXSDString);
/*     */   }
/*     */ 
/*     */   public static BigInteger parseInteger(String lexicalXSDInteger)
/*     */   {
/* 168 */     if (theConverter == null) initConverter();
/* 169 */     return theConverter.parseInteger(lexicalXSDInteger);
/*     */   }
/*     */ 
/*     */   public static int parseInt(String lexicalXSDInt)
/*     */   {
/* 183 */     if (theConverter == null) initConverter();
/* 184 */     return theConverter.parseInt(lexicalXSDInt);
/*     */   }
/*     */ 
/*     */   public static long parseLong(String lexicalXSDLong)
/*     */   {
/* 198 */     if (theConverter == null) initConverter();
/* 199 */     return theConverter.parseLong(lexicalXSDLong);
/*     */   }
/*     */ 
/*     */   public static short parseShort(String lexicalXSDShort)
/*     */   {
/* 213 */     if (theConverter == null) initConverter();
/* 214 */     return theConverter.parseShort(lexicalXSDShort);
/*     */   }
/*     */ 
/*     */   public static BigDecimal parseDecimal(String lexicalXSDDecimal)
/*     */   {
/* 228 */     if (theConverter == null) initConverter();
/* 229 */     return theConverter.parseDecimal(lexicalXSDDecimal);
/*     */   }
/*     */ 
/*     */   public static float parseFloat(String lexicalXSDFloat)
/*     */   {
/* 243 */     if (theConverter == null) initConverter();
/* 244 */     return theConverter.parseFloat(lexicalXSDFloat);
/*     */   }
/*     */ 
/*     */   public static double parseDouble(String lexicalXSDDouble)
/*     */   {
/* 258 */     if (theConverter == null) initConverter();
/* 259 */     return theConverter.parseDouble(lexicalXSDDouble);
/*     */   }
/*     */ 
/*     */   public static boolean parseBoolean(String lexicalXSDBoolean)
/*     */   {
/* 273 */     if (theConverter == null) initConverter();
/* 274 */     return theConverter.parseBoolean(lexicalXSDBoolean);
/*     */   }
/*     */ 
/*     */   public static byte parseByte(String lexicalXSDByte)
/*     */   {
/* 288 */     if (theConverter == null) initConverter();
/* 289 */     return theConverter.parseByte(lexicalXSDByte);
/*     */   }
/*     */ 
/*     */   public static QName parseQName(String lexicalXSDQName, NamespaceContext nsc)
/*     */   {
/* 311 */     if (theConverter == null) initConverter();
/* 312 */     return theConverter.parseQName(lexicalXSDQName, nsc);
/*     */   }
/*     */ 
/*     */   public static Calendar parseDateTime(String lexicalXSDDateTime)
/*     */   {
/* 326 */     if (theConverter == null) initConverter();
/* 327 */     return theConverter.parseDateTime(lexicalXSDDateTime);
/*     */   }
/*     */ 
/*     */   public static byte[] parseBase64Binary(String lexicalXSDBase64Binary)
/*     */   {
/* 341 */     if (theConverter == null) initConverter();
/* 342 */     return theConverter.parseBase64Binary(lexicalXSDBase64Binary);
/*     */   }
/*     */ 
/*     */   public static byte[] parseHexBinary(String lexicalXSDHexBinary)
/*     */   {
/* 356 */     if (theConverter == null) initConverter();
/* 357 */     return theConverter.parseHexBinary(lexicalXSDHexBinary);
/*     */   }
/*     */ 
/*     */   public static long parseUnsignedInt(String lexicalXSDUnsignedInt)
/*     */   {
/* 371 */     if (theConverter == null) initConverter();
/* 372 */     return theConverter.parseUnsignedInt(lexicalXSDUnsignedInt);
/*     */   }
/*     */ 
/*     */   public static int parseUnsignedShort(String lexicalXSDUnsignedShort)
/*     */   {
/* 386 */     if (theConverter == null) initConverter();
/* 387 */     return theConverter.parseUnsignedShort(lexicalXSDUnsignedShort);
/*     */   }
/*     */ 
/*     */   public static Calendar parseTime(String lexicalXSDTime)
/*     */   {
/* 401 */     if (theConverter == null) initConverter();
/* 402 */     return theConverter.parseTime(lexicalXSDTime);
/*     */   }
/*     */ 
/*     */   public static Calendar parseDate(String lexicalXSDDate)
/*     */   {
/* 415 */     if (theConverter == null) initConverter();
/* 416 */     return theConverter.parseDate(lexicalXSDDate);
/*     */   }
/*     */ 
/*     */   public static String parseAnySimpleType(String lexicalXSDAnySimpleType)
/*     */   {
/* 431 */     if (theConverter == null) initConverter();
/* 432 */     return theConverter.parseAnySimpleType(lexicalXSDAnySimpleType);
/*     */   }
/*     */ 
/*     */   public static String printString(String val)
/*     */   {
/* 446 */     if (theConverter == null) initConverter();
/* 447 */     return theConverter.printString(val);
/*     */   }
/*     */ 
/*     */   public static String printInteger(BigInteger val)
/*     */   {
/* 460 */     if (theConverter == null) initConverter();
/* 461 */     return theConverter.printInteger(val);
/*     */   }
/*     */ 
/*     */   public static String printInt(int val)
/*     */   {
/* 473 */     if (theConverter == null) initConverter();
/* 474 */     return theConverter.printInt(val);
/*     */   }
/*     */ 
/*     */   public static String printLong(long val)
/*     */   {
/* 486 */     if (theConverter == null) initConverter();
/* 487 */     return theConverter.printLong(val);
/*     */   }
/*     */ 
/*     */   public static String printShort(short val)
/*     */   {
/* 499 */     if (theConverter == null) initConverter();
/* 500 */     return theConverter.printShort(val);
/*     */   }
/*     */ 
/*     */   public static String printDecimal(BigDecimal val)
/*     */   {
/* 513 */     if (theConverter == null) initConverter();
/* 514 */     return theConverter.printDecimal(val);
/*     */   }
/*     */ 
/*     */   public static String printFloat(float val)
/*     */   {
/* 526 */     if (theConverter == null) initConverter();
/* 527 */     return theConverter.printFloat(val);
/*     */   }
/*     */ 
/*     */   public static String printDouble(double val)
/*     */   {
/* 539 */     if (theConverter == null) initConverter();
/* 540 */     return theConverter.printDouble(val);
/*     */   }
/*     */ 
/*     */   public static String printBoolean(boolean val)
/*     */   {
/* 552 */     if (theConverter == null) initConverter();
/* 553 */     return theConverter.printBoolean(val);
/*     */   }
/*     */ 
/*     */   public static String printByte(byte val)
/*     */   {
/* 565 */     if (theConverter == null) initConverter();
/* 566 */     return theConverter.printByte(val);
/*     */   }
/*     */ 
/*     */   public static String printQName(QName val, NamespaceContext nsc)
/*     */   {
/* 583 */     if (theConverter == null) initConverter();
/* 584 */     return theConverter.printQName(val, nsc);
/*     */   }
/*     */ 
/*     */   public static String printDateTime(Calendar val)
/*     */   {
/* 597 */     if (theConverter == null) initConverter();
/* 598 */     return theConverter.printDateTime(val);
/*     */   }
/*     */ 
/*     */   public static String printBase64Binary(byte[] val)
/*     */   {
/* 611 */     if (theConverter == null) initConverter();
/* 612 */     return theConverter.printBase64Binary(val);
/*     */   }
/*     */ 
/*     */   public static String printHexBinary(byte[] val)
/*     */   {
/* 625 */     if (theConverter == null) initConverter();
/* 626 */     return theConverter.printHexBinary(val);
/*     */   }
/*     */ 
/*     */   public static String printUnsignedInt(long val)
/*     */   {
/* 638 */     if (theConverter == null) initConverter();
/* 639 */     return theConverter.printUnsignedInt(val);
/*     */   }
/*     */ 
/*     */   public static String printUnsignedShort(int val)
/*     */   {
/* 651 */     if (theConverter == null) initConverter();
/* 652 */     return theConverter.printUnsignedShort(val);
/*     */   }
/*     */ 
/*     */   public static String printTime(Calendar val)
/*     */   {
/* 665 */     if (theConverter == null) initConverter();
/* 666 */     return theConverter.printTime(val);
/*     */   }
/*     */ 
/*     */   public static String printDate(Calendar val)
/*     */   {
/* 679 */     if (theConverter == null) initConverter();
/* 680 */     return theConverter.printDate(val);
/*     */   }
/*     */ 
/*     */   public static String printAnySimpleType(String val)
/*     */   {
/* 692 */     if (theConverter == null) initConverter();
/* 693 */     return theConverter.printAnySimpleType(val);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.DatatypeConverter
 * JD-Core Version:    0.6.2
 */