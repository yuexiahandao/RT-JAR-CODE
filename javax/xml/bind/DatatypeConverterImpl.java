/*      */ package javax.xml.bind;
/*      */ 
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Calendar;
/*      */ import java.util.Collections;
/*      */ import java.util.Date;
/*      */ import java.util.GregorianCalendar;
/*      */ import java.util.Map;
/*      */ import java.util.TimeZone;
/*      */ import java.util.WeakHashMap;
/*      */ import javax.xml.datatype.DatatypeConfigurationException;
/*      */ import javax.xml.datatype.DatatypeFactory;
/*      */ import javax.xml.datatype.XMLGregorianCalendar;
/*      */ import javax.xml.namespace.NamespaceContext;
/*      */ import javax.xml.namespace.QName;
/*      */ 
/*      */ final class DatatypeConverterImpl
/*      */   implements DatatypeConverterInterface
/*      */ {
/*   64 */   public static final DatatypeConverterInterface theInstance = new DatatypeConverterImpl();
/*      */ 
/*  476 */   private static final char[] hexCode = "0123456789ABCDEF".toCharArray();
/*      */ 
/*  611 */   private static final byte[] decodeMap = initDecodeMap();
/*      */   private static final byte PADDING = 127;
/*  738 */   private static final char[] encodeMap = initEncodeMap();
/*      */ 
/*  891 */   private static final Map<ClassLoader, DatatypeFactory> DF_CACHE = Collections.synchronizedMap(new WeakHashMap());
/*      */ 
/*      */   public String parseString(String lexicalXSDString)
/*      */   {
/*   70 */     return lexicalXSDString;
/*      */   }
/*      */ 
/*      */   public BigInteger parseInteger(String lexicalXSDInteger) {
/*   74 */     return _parseInteger(lexicalXSDInteger);
/*      */   }
/*      */ 
/*      */   public static BigInteger _parseInteger(CharSequence s) {
/*   78 */     return new BigInteger(removeOptionalPlus(WhiteSpaceProcessor.trim(s)).toString());
/*      */   }
/*      */ 
/*      */   public String printInteger(BigInteger val) {
/*   82 */     return _printInteger(val);
/*      */   }
/*      */ 
/*      */   public static String _printInteger(BigInteger val) {
/*   86 */     return val.toString();
/*      */   }
/*      */ 
/*      */   public int parseInt(String s) {
/*   90 */     return _parseInt(s);
/*      */   }
/*      */ 
/*      */   public static int _parseInt(CharSequence s)
/*      */   {
/*  104 */     int len = s.length();
/*  105 */     int sign = 1;
/*      */ 
/*  107 */     int r = 0;
/*      */ 
/*  109 */     for (int i = 0; i < len; i++) {
/*  110 */       char ch = s.charAt(i);
/*  111 */       if (!WhiteSpaceProcessor.isWhiteSpace(ch))
/*      */       {
/*  113 */         if (('0' <= ch) && (ch <= '9'))
/*  114 */           r = r * 10 + (ch - '0');
/*  115 */         else if (ch == '-')
/*  116 */           sign = -1;
/*  117 */         else if (ch != '+')
/*      */         {
/*  120 */           throw new NumberFormatException("Not a number: " + s);
/*      */         }
/*      */       }
/*      */     }
/*  124 */     return r * sign;
/*      */   }
/*      */ 
/*      */   public long parseLong(String lexicalXSLong) {
/*  128 */     return _parseLong(lexicalXSLong);
/*      */   }
/*      */ 
/*      */   public static long _parseLong(CharSequence s) {
/*  132 */     return Long.valueOf(removeOptionalPlus(WhiteSpaceProcessor.trim(s)).toString()).longValue();
/*      */   }
/*      */ 
/*      */   public short parseShort(String lexicalXSDShort) {
/*  136 */     return _parseShort(lexicalXSDShort);
/*      */   }
/*      */ 
/*      */   public static short _parseShort(CharSequence s) {
/*  140 */     return (short)_parseInt(s);
/*      */   }
/*      */ 
/*      */   public String printShort(short val) {
/*  144 */     return _printShort(val);
/*      */   }
/*      */ 
/*      */   public static String _printShort(short val) {
/*  148 */     return String.valueOf(val);
/*      */   }
/*      */ 
/*      */   public BigDecimal parseDecimal(String content) {
/*  152 */     return _parseDecimal(content);
/*      */   }
/*      */ 
/*      */   public static BigDecimal _parseDecimal(CharSequence content) {
/*  156 */     content = WhiteSpaceProcessor.trim(content);
/*      */ 
/*  158 */     if (content.length() <= 0) {
/*  159 */       return null;
/*      */     }
/*      */ 
/*  162 */     return new BigDecimal(content.toString());
/*      */   }
/*      */ 
/*      */   public float parseFloat(String lexicalXSDFloat)
/*      */   {
/*  178 */     return _parseFloat(lexicalXSDFloat);
/*      */   }
/*      */ 
/*      */   public static float _parseFloat(CharSequence _val) {
/*  182 */     String s = WhiteSpaceProcessor.trim(_val).toString();
/*      */ 
/*  198 */     if (s.equals("NaN")) {
/*  199 */       return (0.0F / 0.0F);
/*      */     }
/*  201 */     if (s.equals("INF")) {
/*  202 */       return (1.0F / 1.0F);
/*      */     }
/*  204 */     if (s.equals("-INF")) {
/*  205 */       return (1.0F / -1.0F);
/*      */     }
/*      */ 
/*  208 */     if ((s.length() == 0) || (!isDigitOrPeriodOrSign(s.charAt(0))) || (!isDigitOrPeriodOrSign(s.charAt(s.length() - 1))))
/*      */     {
/*  211 */       throw new NumberFormatException();
/*      */     }
/*      */ 
/*  215 */     return Float.parseFloat(s);
/*      */   }
/*      */ 
/*      */   public String printFloat(float v) {
/*  219 */     return _printFloat(v);
/*      */   }
/*      */ 
/*      */   public static String _printFloat(float v) {
/*  223 */     if (Float.isNaN(v)) {
/*  224 */       return "NaN";
/*      */     }
/*  226 */     if (v == (1.0F / 1.0F)) {
/*  227 */       return "INF";
/*      */     }
/*  229 */     if (v == (1.0F / -1.0F)) {
/*  230 */       return "-INF";
/*      */     }
/*  232 */     return String.valueOf(v);
/*      */   }
/*      */ 
/*      */   public double parseDouble(String lexicalXSDDouble) {
/*  236 */     return _parseDouble(lexicalXSDDouble);
/*      */   }
/*      */ 
/*      */   public static double _parseDouble(CharSequence _val) {
/*  240 */     String val = WhiteSpaceProcessor.trim(_val).toString();
/*      */ 
/*  242 */     if (val.equals("NaN")) {
/*  243 */       return (0.0D / 0.0D);
/*      */     }
/*  245 */     if (val.equals("INF")) {
/*  246 */       return (1.0D / 0.0D);
/*      */     }
/*  248 */     if (val.equals("-INF")) {
/*  249 */       return (-1.0D / 0.0D);
/*      */     }
/*      */ 
/*  252 */     if ((val.length() == 0) || (!isDigitOrPeriodOrSign(val.charAt(0))) || (!isDigitOrPeriodOrSign(val.charAt(val.length() - 1))))
/*      */     {
/*  255 */       throw new NumberFormatException(val);
/*      */     }
/*      */ 
/*  260 */     return Double.parseDouble(val);
/*      */   }
/*      */ 
/*      */   public boolean parseBoolean(String lexicalXSDBoolean) {
/*  264 */     return _parseBoolean(lexicalXSDBoolean).booleanValue();
/*      */   }
/*      */ 
/*      */   public static Boolean _parseBoolean(CharSequence literal) {
/*  268 */     if (literal == null) {
/*  269 */       return null;
/*      */     }
/*      */ 
/*  272 */     int i = 0;
/*  273 */     int len = literal.length();
/*      */ 
/*  275 */     boolean value = false;
/*      */ 
/*  277 */     if (literal.length() <= 0) {
/*  278 */       return null;
/*      */     }
/*      */     char ch;
/*      */     do
/*  282 */       ch = literal.charAt(i++);
/*  283 */     while ((WhiteSpaceProcessor.isWhiteSpace(ch)) && (i < len));
/*      */ 
/*  285 */     int strIndex = 0;
/*      */ 
/*  287 */     switch (ch) {
/*      */     case '1':
/*  289 */       value = true;
/*  290 */       break;
/*      */     case '0':
/*  292 */       value = false;
/*  293 */       break;
/*      */     case 't':
/*  295 */       String strTrue = "rue";
/*      */       do
/*  297 */         ch = literal.charAt(i++);
/*  298 */       while ((strTrue.charAt(strIndex++) == ch) && (i < len) && (strIndex < 3));
/*      */ 
/*  300 */       if (strIndex == 3)
/*  301 */         value = true;
/*      */       else {
/*  303 */         return Boolean.valueOf(false);
/*      */       }
/*      */ 
/*      */       break;
/*      */     case 'f':
/*  309 */       String strFalse = "alse";
/*      */       do
/*  311 */         ch = literal.charAt(i++);
/*  312 */       while ((strFalse.charAt(strIndex++) == ch) && (i < len) && (strIndex < 4));
/*      */ 
/*  315 */       if (strIndex == 4)
/*  316 */         value = false;
/*      */       else {
/*  318 */         return Boolean.valueOf(false);
/*      */       }
/*      */ 
/*      */       break;
/*      */     }
/*      */ 
/*  325 */     if (i < len) {
/*      */       do
/*  327 */         ch = literal.charAt(i++);
/*  328 */       while ((WhiteSpaceProcessor.isWhiteSpace(ch)) && (i < len));
/*      */     }
/*      */ 
/*  331 */     if (i == len) {
/*  332 */       return Boolean.valueOf(value);
/*      */     }
/*  334 */     return null;
/*      */   }
/*      */ 
/*      */   public String printBoolean(boolean val)
/*      */   {
/*  340 */     return val ? "true" : "false";
/*      */   }
/*      */ 
/*      */   public static String _printBoolean(boolean val) {
/*  344 */     return val ? "true" : "false";
/*      */   }
/*      */ 
/*      */   public byte parseByte(String lexicalXSDByte) {
/*  348 */     return _parseByte(lexicalXSDByte);
/*      */   }
/*      */ 
/*      */   public static byte _parseByte(CharSequence literal) {
/*  352 */     return (byte)_parseInt(literal);
/*      */   }
/*      */ 
/*      */   public String printByte(byte val) {
/*  356 */     return _printByte(val);
/*      */   }
/*      */ 
/*      */   public static String _printByte(byte val) {
/*  360 */     return String.valueOf(val);
/*      */   }
/*      */ 
/*      */   public QName parseQName(String lexicalXSDQName, NamespaceContext nsc) {
/*  364 */     return _parseQName(lexicalXSDQName, nsc);
/*      */   }
/*      */ 
/*      */   public static QName _parseQName(CharSequence text, NamespaceContext nsc)
/*      */   {
/*  371 */     int length = text.length();
/*      */ 
/*  374 */     int start = 0;
/*  375 */     while ((start < length) && (WhiteSpaceProcessor.isWhiteSpace(text.charAt(start)))) {
/*  376 */       start++;
/*      */     }
/*      */ 
/*  379 */     int end = length;
/*  380 */     while ((end > start) && (WhiteSpaceProcessor.isWhiteSpace(text.charAt(end - 1)))) {
/*  381 */       end--;
/*      */     }
/*      */ 
/*  384 */     if (end == start) {
/*  385 */       throw new IllegalArgumentException("input is empty");
/*      */     }
/*      */ 
/*  394 */     int idx = start + 1;
/*  395 */     while ((idx < end) && (text.charAt(idx) != ':'))
/*  396 */       idx++;
/*      */     String prefix;
/*      */     String prefix;
/*      */     String localPart;
/*      */     String uri;
/*  399 */     if (idx == end) {
/*  400 */       String uri = nsc.getNamespaceURI("");
/*  401 */       String localPart = text.subSequence(start, end).toString();
/*  402 */       prefix = "";
/*      */     }
/*      */     else {
/*  405 */       prefix = text.subSequence(start, idx).toString();
/*  406 */       localPart = text.subSequence(idx + 1, end).toString();
/*  407 */       uri = nsc.getNamespaceURI(prefix);
/*      */ 
/*  410 */       if ((uri == null) || (uri.length() == 0))
/*      */       {
/*  413 */         throw new IllegalArgumentException("prefix " + prefix + " is not bound to a namespace");
/*      */       }
/*      */     }
/*      */ 
/*  417 */     return new QName(uri, localPart, prefix);
/*      */   }
/*      */ 
/*      */   public Calendar parseDateTime(String lexicalXSDDateTime) {
/*  421 */     return _parseDateTime(lexicalXSDDateTime);
/*      */   }
/*      */ 
/*      */   public static GregorianCalendar _parseDateTime(CharSequence s) {
/*  425 */     String val = WhiteSpaceProcessor.trim(s).toString();
/*  426 */     return getDatatypeFactory().newXMLGregorianCalendar(val).toGregorianCalendar();
/*      */   }
/*      */ 
/*      */   public String printDateTime(Calendar val) {
/*  430 */     return _printDateTime(val);
/*      */   }
/*      */ 
/*      */   public static String _printDateTime(Calendar val) {
/*  434 */     return CalendarFormatter.doFormat("%Y-%M-%DT%h:%m:%s%z", val);
/*      */   }
/*      */ 
/*      */   public byte[] parseBase64Binary(String lexicalXSDBase64Binary) {
/*  438 */     return _parseBase64Binary(lexicalXSDBase64Binary);
/*      */   }
/*      */ 
/*      */   public byte[] parseHexBinary(String s) {
/*  442 */     int len = s.length();
/*      */ 
/*  445 */     if (len % 2 != 0) {
/*  446 */       throw new IllegalArgumentException("hexBinary needs to be even-length: " + s);
/*      */     }
/*      */ 
/*  449 */     byte[] out = new byte[len / 2];
/*      */ 
/*  451 */     for (int i = 0; i < len; i += 2) {
/*  452 */       int h = hexToBin(s.charAt(i));
/*  453 */       int l = hexToBin(s.charAt(i + 1));
/*  454 */       if ((h == -1) || (l == -1)) {
/*  455 */         throw new IllegalArgumentException("contains illegal character for hexBinary: " + s);
/*      */       }
/*      */ 
/*  458 */       out[(i / 2)] = ((byte)(h * 16 + l));
/*      */     }
/*      */ 
/*  461 */     return out;
/*      */   }
/*      */ 
/*      */   private static int hexToBin(char ch) {
/*  465 */     if (('0' <= ch) && (ch <= '9')) {
/*  466 */       return ch - '0';
/*      */     }
/*  468 */     if (('A' <= ch) && (ch <= 'F')) {
/*  469 */       return ch - 'A' + 10;
/*      */     }
/*  471 */     if (('a' <= ch) && (ch <= 'f')) {
/*  472 */       return ch - 'a' + 10;
/*      */     }
/*  474 */     return -1;
/*      */   }
/*      */ 
/*      */   public String printHexBinary(byte[] data)
/*      */   {
/*  479 */     StringBuilder r = new StringBuilder(data.length * 2);
/*  480 */     for (byte b : data) {
/*  481 */       r.append(hexCode[(b >> 4 & 0xF)]);
/*  482 */       r.append(hexCode[(b & 0xF)]);
/*      */     }
/*  484 */     return r.toString();
/*      */   }
/*      */ 
/*      */   public long parseUnsignedInt(String lexicalXSDUnsignedInt) {
/*  488 */     return _parseLong(lexicalXSDUnsignedInt);
/*      */   }
/*      */ 
/*      */   public String printUnsignedInt(long val) {
/*  492 */     return _printLong(val);
/*      */   }
/*      */ 
/*      */   public int parseUnsignedShort(String lexicalXSDUnsignedShort) {
/*  496 */     return _parseInt(lexicalXSDUnsignedShort);
/*      */   }
/*      */ 
/*      */   public Calendar parseTime(String lexicalXSDTime) {
/*  500 */     return getDatatypeFactory().newXMLGregorianCalendar(lexicalXSDTime).toGregorianCalendar();
/*      */   }
/*      */ 
/*      */   public String printTime(Calendar val) {
/*  504 */     return CalendarFormatter.doFormat("%h:%m:%s%z", val);
/*      */   }
/*      */ 
/*      */   public Calendar parseDate(String lexicalXSDDate) {
/*  508 */     return getDatatypeFactory().newXMLGregorianCalendar(lexicalXSDDate).toGregorianCalendar();
/*      */   }
/*      */ 
/*      */   public String printDate(Calendar val) {
/*  512 */     return _printDate(val);
/*      */   }
/*      */ 
/*      */   public static String _printDate(Calendar val) {
/*  516 */     return CalendarFormatter.doFormat("%Y-%M-%D" + "%z", val);
/*      */   }
/*      */ 
/*      */   public String parseAnySimpleType(String lexicalXSDAnySimpleType) {
/*  520 */     return lexicalXSDAnySimpleType;
/*      */   }
/*      */ 
/*      */   public String printString(String val)
/*      */   {
/*  526 */     return val;
/*      */   }
/*      */ 
/*      */   public String printInt(int val) {
/*  530 */     return _printInt(val);
/*      */   }
/*      */ 
/*      */   public static String _printInt(int val) {
/*  534 */     return String.valueOf(val);
/*      */   }
/*      */ 
/*      */   public String printLong(long val) {
/*  538 */     return _printLong(val);
/*      */   }
/*      */ 
/*      */   public static String _printLong(long val) {
/*  542 */     return String.valueOf(val);
/*      */   }
/*      */ 
/*      */   public String printDecimal(BigDecimal val) {
/*  546 */     return _printDecimal(val);
/*      */   }
/*      */ 
/*      */   public static String _printDecimal(BigDecimal val) {
/*  550 */     return val.toPlainString();
/*      */   }
/*      */ 
/*      */   public String printDouble(double v) {
/*  554 */     return _printDouble(v);
/*      */   }
/*      */ 
/*      */   public static String _printDouble(double v) {
/*  558 */     if (Double.isNaN(v)) {
/*  559 */       return "NaN";
/*      */     }
/*  561 */     if (v == (1.0D / 0.0D)) {
/*  562 */       return "INF";
/*      */     }
/*  564 */     if (v == (-1.0D / 0.0D)) {
/*  565 */       return "-INF";
/*      */     }
/*  567 */     return String.valueOf(v);
/*      */   }
/*      */ 
/*      */   public String printQName(QName val, NamespaceContext nsc) {
/*  571 */     return _printQName(val, nsc);
/*      */   }
/*      */ 
/*      */   public static String _printQName(QName val, NamespaceContext nsc)
/*      */   {
/*  577 */     String prefix = nsc.getPrefix(val.getNamespaceURI());
/*  578 */     String localPart = val.getLocalPart();
/*      */     String qname;
/*      */     String qname;
/*  580 */     if ((prefix == null) || (prefix.length() == 0))
/*  581 */       qname = localPart;
/*      */     else {
/*  583 */       qname = prefix + ':' + localPart;
/*      */     }
/*      */ 
/*  586 */     return qname;
/*      */   }
/*      */ 
/*      */   public String printBase64Binary(byte[] val) {
/*  590 */     return _printBase64Binary(val);
/*      */   }
/*      */ 
/*      */   public String printUnsignedShort(int val) {
/*  594 */     return String.valueOf(val);
/*      */   }
/*      */ 
/*      */   public String printAnySimpleType(String val) {
/*  598 */     return val;
/*      */   }
/*      */ 
/*      */   public static String installHook(String s)
/*      */   {
/*  607 */     DatatypeConverter.setDatatypeConverter(theInstance);
/*  608 */     return s;
/*      */   }
/*      */ 
/*      */   private static byte[] initDecodeMap()
/*      */   {
/*  615 */     byte[] map = new byte[''];
/*      */ 
/*  617 */     for (int i = 0; i < 128; i++) {
/*  618 */       map[i] = -1;
/*      */     }
/*      */ 
/*  621 */     for (i = 65; i <= 90; i++) {
/*  622 */       map[i] = ((byte)(i - 65));
/*      */     }
/*  624 */     for (i = 97; i <= 122; i++) {
/*  625 */       map[i] = ((byte)(i - 97 + 26));
/*      */     }
/*  627 */     for (i = 48; i <= 57; i++) {
/*  628 */       map[i] = ((byte)(i - 48 + 52));
/*      */     }
/*  630 */     map[43] = 62;
/*  631 */     map[47] = 63;
/*  632 */     map[61] = 127;
/*      */ 
/*  634 */     return map;
/*      */   }
/*      */ 
/*      */   private static int guessLength(String text)
/*      */   {
/*  658 */     int len = text.length();
/*      */ 
/*  661 */     for (int j = len - 1; 
/*  662 */       j >= 0; j--) {
/*  663 */       byte code = decodeMap[text.charAt(j)];
/*  664 */       if (code != 127)
/*      */       {
/*  667 */         if (code != -1)
/*      */           break;
/*  669 */         return text.length() / 4 * 3;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  674 */     j++;
/*  675 */     int padSize = len - j;
/*  676 */     if (padSize > 2)
/*      */     {
/*  678 */       return text.length() / 4 * 3;
/*      */     }
/*      */ 
/*  683 */     return text.length() / 4 * 3 - padSize;
/*      */   }
/*      */ 
/*      */   public static byte[] _parseBase64Binary(String text)
/*      */   {
/*  696 */     int buflen = guessLength(text);
/*  697 */     byte[] out = new byte[buflen];
/*  698 */     int o = 0;
/*      */ 
/*  700 */     int len = text.length();
/*      */ 
/*  703 */     byte[] quadruplet = new byte[4];
/*  704 */     int q = 0;
/*      */ 
/*  707 */     for (int i = 0; i < len; i++) {
/*  708 */       char ch = text.charAt(i);
/*  709 */       byte v = decodeMap[ch];
/*      */ 
/*  711 */       if (v != -1) {
/*  712 */         quadruplet[(q++)] = v;
/*      */       }
/*      */ 
/*  715 */       if (q == 4)
/*      */       {
/*  717 */         out[(o++)] = ((byte)(quadruplet[0] << 2 | quadruplet[1] >> 4));
/*  718 */         if (quadruplet[2] != 127) {
/*  719 */           out[(o++)] = ((byte)(quadruplet[1] << 4 | quadruplet[2] >> 2));
/*      */         }
/*  721 */         if (quadruplet[3] != 127) {
/*  722 */           out[(o++)] = ((byte)(quadruplet[2] << 6 | quadruplet[3]));
/*      */         }
/*  724 */         q = 0;
/*      */       }
/*      */     }
/*      */ 
/*  728 */     if (buflen == o)
/*      */     {
/*  730 */       return out;
/*      */     }
/*      */ 
/*  734 */     byte[] nb = new byte[o];
/*  735 */     System.arraycopy(out, 0, nb, 0, o);
/*  736 */     return nb;
/*      */   }
/*      */ 
/*      */   private static char[] initEncodeMap()
/*      */   {
/*  741 */     char[] map = new char[64];
/*      */ 
/*  743 */     for (int i = 0; i < 26; i++) {
/*  744 */       map[i] = ((char)(65 + i));
/*      */     }
/*  746 */     for (i = 26; i < 52; i++) {
/*  747 */       map[i] = ((char)(97 + (i - 26)));
/*      */     }
/*  749 */     for (i = 52; i < 62; i++) {
/*  750 */       map[i] = ((char)(48 + (i - 52)));
/*      */     }
/*  752 */     map[62] = '+';
/*  753 */     map[63] = '/';
/*      */ 
/*  755 */     return map;
/*      */   }
/*      */ 
/*      */   public static char encode(int i) {
/*  759 */     return encodeMap[(i & 0x3F)];
/*      */   }
/*      */ 
/*      */   public static byte encodeByte(int i) {
/*  763 */     return (byte)encodeMap[(i & 0x3F)];
/*      */   }
/*      */ 
/*      */   public static String _printBase64Binary(byte[] input) {
/*  767 */     return _printBase64Binary(input, 0, input.length);
/*      */   }
/*      */ 
/*      */   public static String _printBase64Binary(byte[] input, int offset, int len) {
/*  771 */     char[] buf = new char[(len + 2) / 3 * 4];
/*  772 */     int ptr = _printBase64Binary(input, offset, len, buf, 0);
/*  773 */     assert (ptr == buf.length);
/*  774 */     return new String(buf);
/*      */   }
/*      */ 
/*      */   public static int _printBase64Binary(byte[] input, int offset, int len, char[] buf, int ptr)
/*      */   {
/*  788 */     int remaining = len;
/*      */ 
/*  790 */     for (int i = offset; remaining >= 3; i += 3) {
/*  791 */       buf[(ptr++)] = encode(input[i] >> 2);
/*  792 */       buf[(ptr++)] = encode((input[i] & 0x3) << 4 | input[(i + 1)] >> 4 & 0xF);
/*      */ 
/*  795 */       buf[(ptr++)] = encode((input[(i + 1)] & 0xF) << 2 | input[(i + 2)] >> 6 & 0x3);
/*      */ 
/*  798 */       buf[(ptr++)] = encode(input[(i + 2)] & 0x3F);
/*      */ 
/*  790 */       remaining -= 3;
/*      */     }
/*      */ 
/*  801 */     if (remaining == 1) {
/*  802 */       buf[(ptr++)] = encode(input[i] >> 2);
/*  803 */       buf[(ptr++)] = encode((input[i] & 0x3) << 4);
/*  804 */       buf[(ptr++)] = '=';
/*  805 */       buf[(ptr++)] = '=';
/*      */     }
/*      */ 
/*  808 */     if (remaining == 2) {
/*  809 */       buf[(ptr++)] = encode(input[i] >> 2);
/*  810 */       buf[(ptr++)] = encode((input[i] & 0x3) << 4 | input[(i + 1)] >> 4 & 0xF);
/*      */ 
/*  812 */       buf[(ptr++)] = encode((input[(i + 1)] & 0xF) << 2);
/*  813 */       buf[(ptr++)] = '=';
/*      */     }
/*  815 */     return ptr;
/*      */   }
/*      */ 
/*      */   public static int _printBase64Binary(byte[] input, int offset, int len, byte[] out, int ptr)
/*      */   {
/*  829 */     byte[] buf = out;
/*  830 */     int remaining = len;
/*      */ 
/*  832 */     for (int i = offset; remaining >= 3; i += 3) {
/*  833 */       buf[(ptr++)] = encodeByte(input[i] >> 2);
/*  834 */       buf[(ptr++)] = encodeByte((input[i] & 0x3) << 4 | input[(i + 1)] >> 4 & 0xF);
/*      */ 
/*  837 */       buf[(ptr++)] = encodeByte((input[(i + 1)] & 0xF) << 2 | input[(i + 2)] >> 6 & 0x3);
/*      */ 
/*  840 */       buf[(ptr++)] = encodeByte(input[(i + 2)] & 0x3F);
/*      */ 
/*  832 */       remaining -= 3;
/*      */     }
/*      */ 
/*  843 */     if (remaining == 1) {
/*  844 */       buf[(ptr++)] = encodeByte(input[i] >> 2);
/*  845 */       buf[(ptr++)] = encodeByte((input[i] & 0x3) << 4);
/*  846 */       buf[(ptr++)] = 61;
/*  847 */       buf[(ptr++)] = 61;
/*      */     }
/*      */ 
/*  850 */     if (remaining == 2) {
/*  851 */       buf[(ptr++)] = encodeByte(input[i] >> 2);
/*  852 */       buf[(ptr++)] = encodeByte((input[i] & 0x3) << 4 | input[(i + 1)] >> 4 & 0xF);
/*      */ 
/*  855 */       buf[(ptr++)] = encodeByte((input[(i + 1)] & 0xF) << 2);
/*  856 */       buf[(ptr++)] = 61;
/*      */     }
/*      */ 
/*  859 */     return ptr;
/*      */   }
/*      */ 
/*      */   private static CharSequence removeOptionalPlus(CharSequence s) {
/*  863 */     int len = s.length();
/*      */ 
/*  865 */     if ((len <= 1) || (s.charAt(0) != '+')) {
/*  866 */       return s;
/*      */     }
/*      */ 
/*  869 */     s = s.subSequence(1, len);
/*  870 */     char ch = s.charAt(0);
/*  871 */     if (('0' <= ch) && (ch <= '9')) {
/*  872 */       return s;
/*      */     }
/*  874 */     if ('.' == ch) {
/*  875 */       return s;
/*      */     }
/*      */ 
/*  878 */     throw new NumberFormatException();
/*      */   }
/*      */ 
/*      */   private static boolean isDigitOrPeriodOrSign(char ch) {
/*  882 */     if (('0' <= ch) && (ch <= '9')) {
/*  883 */       return true;
/*      */     }
/*  885 */     if ((ch == '+') || (ch == '-') || (ch == '.')) {
/*  886 */       return true;
/*      */     }
/*  888 */     return false;
/*      */   }
/*      */ 
/*      */   public static DatatypeFactory getDatatypeFactory()
/*      */   {
/*  894 */     ClassLoader tccl = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public ClassLoader run() {
/*  896 */         return Thread.currentThread().getContextClassLoader();
/*      */       }
/*      */     });
/*  899 */     DatatypeFactory df = (DatatypeFactory)DF_CACHE.get(tccl);
/*  900 */     if (df == null) {
/*  901 */       synchronized (DatatypeConverterImpl.class) {
/*  902 */         df = (DatatypeFactory)DF_CACHE.get(tccl);
/*  903 */         if (df == null) {
/*      */           try {
/*  905 */             df = DatatypeFactory.newInstance();
/*      */           } catch (DatatypeConfigurationException e) {
/*  907 */             throw new Error(Messages.format("FAILED_TO_INITIALE_DATATYPE_FACTORY"), e);
/*      */           }
/*  909 */           DF_CACHE.put(tccl, df);
/*      */         }
/*      */       }
/*      */     }
/*  913 */     return df;
/*      */   }
/*      */ 
/*      */   private static final class CalendarFormatter
/*      */   {
/*      */     public static String doFormat(String format, Calendar cal) throws IllegalArgumentException {
/*  919 */       int fidx = 0;
/*  920 */       int flen = format.length();
/*  921 */       StringBuilder buf = new StringBuilder();
/*      */ 
/*  923 */       while (fidx < flen) {
/*  924 */         char fch = format.charAt(fidx++);
/*      */ 
/*  926 */         if (fch != '%') {
/*  927 */           buf.append(fch);
/*      */         }
/*      */         else
/*      */         {
/*  932 */           switch (format.charAt(fidx++)) {
/*      */           case 'Y':
/*  934 */             formatYear(cal, buf);
/*  935 */             break;
/*      */           case 'M':
/*  938 */             formatMonth(cal, buf);
/*  939 */             break;
/*      */           case 'D':
/*  942 */             formatDays(cal, buf);
/*  943 */             break;
/*      */           case 'h':
/*  946 */             formatHours(cal, buf);
/*  947 */             break;
/*      */           case 'm':
/*  950 */             formatMinutes(cal, buf);
/*  951 */             break;
/*      */           case 's':
/*  954 */             formatSeconds(cal, buf);
/*  955 */             break;
/*      */           case 'z':
/*  958 */             formatTimeZone(cal, buf);
/*  959 */             break;
/*      */           default:
/*  963 */             throw new InternalError();
/*      */           }
/*      */         }
/*      */       }
/*  967 */       return buf.toString();
/*      */     }
/*      */ 
/*      */     private static void formatYear(Calendar cal, StringBuilder buf) {
/*  971 */       int year = cal.get(1);
/*      */       String s;
/*      */       String s;
/*  974 */       if (year <= 0)
/*      */       {
/*  976 */         s = Integer.toString(1 - year);
/*      */       }
/*      */       else {
/*  979 */         s = Integer.toString(year);
/*      */       }
/*      */ 
/*  982 */       while (s.length() < 4) {
/*  983 */         s = '0' + s;
/*      */       }
/*  985 */       if (year <= 0) {
/*  986 */         s = '-' + s;
/*      */       }
/*      */ 
/*  989 */       buf.append(s);
/*      */     }
/*      */ 
/*      */     private static void formatMonth(Calendar cal, StringBuilder buf) {
/*  993 */       formatTwoDigits(cal.get(2) + 1, buf);
/*      */     }
/*      */ 
/*      */     private static void formatDays(Calendar cal, StringBuilder buf) {
/*  997 */       formatTwoDigits(cal.get(5), buf);
/*      */     }
/*      */ 
/*      */     private static void formatHours(Calendar cal, StringBuilder buf) {
/* 1001 */       formatTwoDigits(cal.get(11), buf);
/*      */     }
/*      */ 
/*      */     private static void formatMinutes(Calendar cal, StringBuilder buf) {
/* 1005 */       formatTwoDigits(cal.get(12), buf);
/*      */     }
/*      */ 
/*      */     private static void formatSeconds(Calendar cal, StringBuilder buf) {
/* 1009 */       formatTwoDigits(cal.get(13), buf);
/* 1010 */       if (cal.isSet(14)) {
/* 1011 */         int n = cal.get(14);
/* 1012 */         if (n != 0) {
/* 1013 */           String ms = Integer.toString(n);
/* 1014 */           while (ms.length() < 3) {
/* 1015 */             ms = '0' + ms;
/*      */           }
/* 1017 */           buf.append('.');
/* 1018 */           buf.append(ms);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private static void formatTimeZone(Calendar cal, StringBuilder buf)
/*      */     {
/* 1025 */       TimeZone tz = cal.getTimeZone();
/*      */ 
/* 1027 */       if (tz == null) {
/* 1028 */         return;
/*      */       }
/*      */ 
/* 1032 */       int offset = tz.getOffset(cal.getTime().getTime());
/*      */ 
/* 1034 */       if (offset == 0) {
/* 1035 */         buf.append('Z');
/* 1036 */         return;
/*      */       }
/*      */ 
/* 1039 */       if (offset >= 0) {
/* 1040 */         buf.append('+');
/*      */       } else {
/* 1042 */         buf.append('-');
/* 1043 */         offset *= -1;
/*      */       }
/*      */ 
/* 1046 */       offset /= 60000;
/*      */ 
/* 1048 */       formatTwoDigits(offset / 60, buf);
/* 1049 */       buf.append(':');
/* 1050 */       formatTwoDigits(offset % 60, buf);
/*      */     }
/*      */ 
/*      */     private static void formatTwoDigits(int n, StringBuilder buf)
/*      */     {
/* 1056 */       if (n < 10) {
/* 1057 */         buf.append('0');
/*      */       }
/* 1059 */       buf.append(n);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.DatatypeConverterImpl
 * JD-Core Version:    0.6.2
 */