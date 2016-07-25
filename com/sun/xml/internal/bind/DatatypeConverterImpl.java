/*     */ package com.sun.xml.internal.bind;
/*     */ 
/*     */ import java.math.BigDecimal;
/*     */ import java.math.BigInteger;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.GregorianCalendar;
/*     */ import java.util.Map;
/*     */ import java.util.TimeZone;
/*     */ import java.util.WeakHashMap;
/*     */ import javax.xml.datatype.DatatypeConfigurationException;
/*     */ import javax.xml.datatype.DatatypeFactory;
/*     */ import javax.xml.datatype.XMLGregorianCalendar;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamWriter;
/*     */ 
/*     */ /** @deprecated */
/*     */ public final class DatatypeConverterImpl
/*     */ {
/* 412 */   private static final byte[] decodeMap = initDecodeMap();
/*     */   private static final byte PADDING = 127;
/* 539 */   private static final char[] encodeMap = initEncodeMap();
/*     */ 
/* 727 */   private static final Map<ClassLoader, DatatypeFactory> DF_CACHE = Collections.synchronizedMap(new WeakHashMap());
/*     */ 
/*     */   public static BigInteger _parseInteger(CharSequence s)
/*     */   {
/*  72 */     return new BigInteger(removeOptionalPlus(WhiteSpaceProcessor.trim(s)).toString());
/*     */   }
/*     */ 
/*     */   public static String _printInteger(BigInteger val) {
/*  76 */     return val.toString();
/*     */   }
/*     */ 
/*     */   public static int _parseInt(CharSequence s)
/*     */   {
/*  90 */     int len = s.length();
/*  91 */     int sign = 1;
/*     */ 
/*  93 */     int r = 0;
/*     */ 
/*  95 */     for (int i = 0; i < len; i++) {
/*  96 */       char ch = s.charAt(i);
/*  97 */       if (!WhiteSpaceProcessor.isWhiteSpace(ch))
/*     */       {
/*  99 */         if (('0' <= ch) && (ch <= '9'))
/* 100 */           r = r * 10 + (ch - '0');
/* 101 */         else if (ch == '-')
/* 102 */           sign = -1;
/* 103 */         else if (ch != '+')
/*     */         {
/* 106 */           throw new NumberFormatException("Not a number: " + s);
/*     */         }
/*     */       }
/*     */     }
/* 110 */     return r * sign;
/*     */   }
/*     */ 
/*     */   public static long _parseLong(CharSequence s) {
/* 114 */     return Long.valueOf(removeOptionalPlus(WhiteSpaceProcessor.trim(s)).toString()).longValue();
/*     */   }
/*     */ 
/*     */   public static short _parseShort(CharSequence s) {
/* 118 */     return (short)_parseInt(s);
/*     */   }
/*     */ 
/*     */   public static String _printShort(short val) {
/* 122 */     return String.valueOf(val);
/*     */   }
/*     */ 
/*     */   public static BigDecimal _parseDecimal(CharSequence content) {
/* 126 */     content = WhiteSpaceProcessor.trim(content);
/*     */ 
/* 128 */     if (content.length() <= 0) {
/* 129 */       return null;
/*     */     }
/*     */ 
/* 132 */     return new BigDecimal(content.toString());
/*     */   }
/*     */ 
/*     */   public static float _parseFloat(CharSequence _val)
/*     */   {
/* 148 */     String s = WhiteSpaceProcessor.trim(_val).toString();
/*     */ 
/* 164 */     if (s.equals("NaN")) {
/* 165 */       return (0.0F / 0.0F);
/*     */     }
/* 167 */     if (s.equals("INF")) {
/* 168 */       return (1.0F / 1.0F);
/*     */     }
/* 170 */     if (s.equals("-INF")) {
/* 171 */       return (1.0F / -1.0F);
/*     */     }
/*     */ 
/* 174 */     if ((s.length() == 0) || (!isDigitOrPeriodOrSign(s.charAt(0))) || (!isDigitOrPeriodOrSign(s.charAt(s.length() - 1))))
/*     */     {
/* 177 */       throw new NumberFormatException();
/*     */     }
/*     */ 
/* 181 */     return Float.parseFloat(s);
/*     */   }
/*     */ 
/*     */   public static String _printFloat(float v) {
/* 185 */     if (Float.isNaN(v)) {
/* 186 */       return "NaN";
/*     */     }
/* 188 */     if (v == (1.0F / 1.0F)) {
/* 189 */       return "INF";
/*     */     }
/* 191 */     if (v == (1.0F / -1.0F)) {
/* 192 */       return "-INF";
/*     */     }
/* 194 */     return String.valueOf(v);
/*     */   }
/*     */ 
/*     */   public static double _parseDouble(CharSequence _val) {
/* 198 */     String val = WhiteSpaceProcessor.trim(_val).toString();
/*     */ 
/* 200 */     if (val.equals("NaN")) {
/* 201 */       return (0.0D / 0.0D);
/*     */     }
/* 203 */     if (val.equals("INF")) {
/* 204 */       return (1.0D / 0.0D);
/*     */     }
/* 206 */     if (val.equals("-INF")) {
/* 207 */       return (-1.0D / 0.0D);
/*     */     }
/*     */ 
/* 210 */     if ((val.length() == 0) || (!isDigitOrPeriodOrSign(val.charAt(0))) || (!isDigitOrPeriodOrSign(val.charAt(val.length() - 1))))
/*     */     {
/* 213 */       throw new NumberFormatException(val);
/*     */     }
/*     */ 
/* 218 */     return Double.parseDouble(val);
/*     */   }
/*     */ 
/*     */   public static Boolean _parseBoolean(CharSequence literal) {
/* 222 */     if (literal == null) {
/* 223 */       return null;
/*     */     }
/*     */ 
/* 226 */     int i = 0;
/* 227 */     int len = literal.length();
/*     */ 
/* 229 */     boolean value = false;
/*     */ 
/* 231 */     if (literal.length() <= 0) {
/* 232 */       return null;
/*     */     }
/*     */     char ch;
/*     */     do
/* 236 */       ch = literal.charAt(i++);
/* 237 */     while ((WhiteSpaceProcessor.isWhiteSpace(ch)) && (i < len));
/*     */ 
/* 239 */     int strIndex = 0;
/*     */ 
/* 241 */     switch (ch) {
/*     */     case '1':
/* 243 */       value = true;
/* 244 */       break;
/*     */     case '0':
/* 246 */       value = false;
/* 247 */       break;
/*     */     case 't':
/* 249 */       String strTrue = "rue";
/*     */       do
/* 251 */         ch = literal.charAt(i++);
/* 252 */       while ((strTrue.charAt(strIndex++) == ch) && (i < len) && (strIndex < 3));
/*     */ 
/* 254 */       if (strIndex == 3)
/* 255 */         value = true;
/*     */       else {
/* 257 */         return Boolean.valueOf(false);
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 'f':
/* 263 */       String strFalse = "alse";
/*     */       do
/* 265 */         ch = literal.charAt(i++);
/* 266 */       while ((strFalse.charAt(strIndex++) == ch) && (i < len) && (strIndex < 4));
/*     */ 
/* 269 */       if (strIndex == 4)
/* 270 */         value = false;
/*     */       else {
/* 272 */         return Boolean.valueOf(false);
/*     */       }
/*     */ 
/*     */       break;
/*     */     }
/*     */ 
/* 279 */     if (i < len) {
/*     */       do
/* 281 */         ch = literal.charAt(i++);
/* 282 */       while ((WhiteSpaceProcessor.isWhiteSpace(ch)) && (i < len));
/*     */     }
/*     */ 
/* 285 */     if (i == len) {
/* 286 */       return Boolean.valueOf(value);
/*     */     }
/* 288 */     return null;
/*     */   }
/*     */ 
/*     */   public static String _printBoolean(boolean val)
/*     */   {
/* 294 */     return val ? "true" : "false";
/*     */   }
/*     */ 
/*     */   public static byte _parseByte(CharSequence literal) {
/* 298 */     return (byte)_parseInt(literal);
/*     */   }
/*     */ 
/*     */   public static String _printByte(byte val) {
/* 302 */     return String.valueOf(val);
/*     */   }
/*     */ 
/*     */   public static QName _parseQName(CharSequence text, NamespaceContext nsc)
/*     */   {
/* 309 */     int length = text.length();
/*     */ 
/* 312 */     int start = 0;
/* 313 */     while ((start < length) && (WhiteSpaceProcessor.isWhiteSpace(text.charAt(start)))) {
/* 314 */       start++;
/*     */     }
/*     */ 
/* 317 */     int end = length;
/* 318 */     while ((end > start) && (WhiteSpaceProcessor.isWhiteSpace(text.charAt(end - 1)))) {
/* 319 */       end--;
/*     */     }
/*     */ 
/* 322 */     if (end == start) {
/* 323 */       throw new IllegalArgumentException("input is empty");
/*     */     }
/*     */ 
/* 332 */     int idx = start + 1;
/* 333 */     while ((idx < end) && (text.charAt(idx) != ':'))
/* 334 */       idx++;
/*     */     String prefix;
/*     */     String prefix;
/*     */     String localPart;
/*     */     String uri;
/* 337 */     if (idx == end) {
/* 338 */       String uri = nsc.getNamespaceURI("");
/* 339 */       String localPart = text.subSequence(start, end).toString();
/* 340 */       prefix = "";
/*     */     }
/*     */     else {
/* 343 */       prefix = text.subSequence(start, idx).toString();
/* 344 */       localPart = text.subSequence(idx + 1, end).toString();
/* 345 */       uri = nsc.getNamespaceURI(prefix);
/*     */ 
/* 348 */       if ((uri == null) || (uri.length() == 0))
/*     */       {
/* 351 */         throw new IllegalArgumentException("prefix " + prefix + " is not bound to a namespace");
/*     */       }
/*     */     }
/*     */ 
/* 355 */     return new QName(uri, localPart, prefix);
/*     */   }
/*     */ 
/*     */   public static GregorianCalendar _parseDateTime(CharSequence s) {
/* 359 */     String val = WhiteSpaceProcessor.trim(s).toString();
/* 360 */     return getDatatypeFactory().newXMLGregorianCalendar(val).toGregorianCalendar();
/*     */   }
/*     */ 
/*     */   public static String _printDateTime(Calendar val) {
/* 364 */     return CalendarFormatter.doFormat("%Y-%M-%DT%h:%m:%s%z", val);
/*     */   }
/*     */ 
/*     */   public static String _printDate(Calendar val) {
/* 368 */     return CalendarFormatter.doFormat("%Y-%M-%D" + "%z", val);
/*     */   }
/*     */ 
/*     */   public static String _printInt(int val) {
/* 372 */     return String.valueOf(val);
/*     */   }
/*     */ 
/*     */   public static String _printLong(long val) {
/* 376 */     return String.valueOf(val);
/*     */   }
/*     */ 
/*     */   public static String _printDecimal(BigDecimal val) {
/* 380 */     return val.toPlainString();
/*     */   }
/*     */ 
/*     */   public static String _printDouble(double v) {
/* 384 */     if (Double.isNaN(v)) {
/* 385 */       return "NaN";
/*     */     }
/* 387 */     if (v == (1.0D / 0.0D)) {
/* 388 */       return "INF";
/*     */     }
/* 390 */     if (v == (-1.0D / 0.0D)) {
/* 391 */       return "-INF";
/*     */     }
/* 393 */     return String.valueOf(v);
/*     */   }
/*     */ 
/*     */   public static String _printQName(QName val, NamespaceContext nsc)
/*     */   {
/* 399 */     String prefix = nsc.getPrefix(val.getNamespaceURI());
/* 400 */     String localPart = val.getLocalPart();
/*     */     String qname;
/*     */     String qname;
/* 402 */     if ((prefix == null) || (prefix.length() == 0))
/* 403 */       qname = localPart;
/*     */     else {
/* 405 */       qname = prefix + ':' + localPart;
/*     */     }
/*     */ 
/* 408 */     return qname;
/*     */   }
/*     */ 
/*     */   private static byte[] initDecodeMap()
/*     */   {
/* 416 */     byte[] map = new byte[''];
/*     */ 
/* 418 */     for (int i = 0; i < 128; i++) {
/* 419 */       map[i] = -1;
/*     */     }
/*     */ 
/* 422 */     for (i = 65; i <= 90; i++) {
/* 423 */       map[i] = ((byte)(i - 65));
/*     */     }
/* 425 */     for (i = 97; i <= 122; i++) {
/* 426 */       map[i] = ((byte)(i - 97 + 26));
/*     */     }
/* 428 */     for (i = 48; i <= 57; i++) {
/* 429 */       map[i] = ((byte)(i - 48 + 52));
/*     */     }
/* 431 */     map[43] = 62;
/* 432 */     map[47] = 63;
/* 433 */     map[61] = 127;
/*     */ 
/* 435 */     return map;
/*     */   }
/*     */ 
/*     */   private static int guessLength(String text)
/*     */   {
/* 459 */     int len = text.length();
/*     */ 
/* 462 */     for (int j = len - 1; 
/* 463 */       j >= 0; j--) {
/* 464 */       byte code = decodeMap[text.charAt(j)];
/* 465 */       if (code != 127)
/*     */       {
/* 468 */         if (code != -1)
/*     */           break;
/* 470 */         return text.length() / 4 * 3;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 475 */     j++;
/* 476 */     int padSize = len - j;
/* 477 */     if (padSize > 2)
/*     */     {
/* 479 */       return text.length() / 4 * 3;
/*     */     }
/*     */ 
/* 484 */     return text.length() / 4 * 3 - padSize;
/*     */   }
/*     */ 
/*     */   public static byte[] _parseBase64Binary(String text)
/*     */   {
/* 497 */     int buflen = guessLength(text);
/* 498 */     byte[] out = new byte[buflen];
/* 499 */     int o = 0;
/*     */ 
/* 501 */     int len = text.length();
/*     */ 
/* 504 */     byte[] quadruplet = new byte[4];
/* 505 */     int q = 0;
/*     */ 
/* 508 */     for (int i = 0; i < len; i++) {
/* 509 */       char ch = text.charAt(i);
/* 510 */       byte v = decodeMap[ch];
/*     */ 
/* 512 */       if (v != -1) {
/* 513 */         quadruplet[(q++)] = v;
/*     */       }
/*     */ 
/* 516 */       if (q == 4)
/*     */       {
/* 518 */         out[(o++)] = ((byte)(quadruplet[0] << 2 | quadruplet[1] >> 4));
/* 519 */         if (quadruplet[2] != 127) {
/* 520 */           out[(o++)] = ((byte)(quadruplet[1] << 4 | quadruplet[2] >> 2));
/*     */         }
/* 522 */         if (quadruplet[3] != 127) {
/* 523 */           out[(o++)] = ((byte)(quadruplet[2] << 6 | quadruplet[3]));
/*     */         }
/* 525 */         q = 0;
/*     */       }
/*     */     }
/*     */ 
/* 529 */     if (buflen == o)
/*     */     {
/* 531 */       return out;
/*     */     }
/*     */ 
/* 535 */     byte[] nb = new byte[o];
/* 536 */     System.arraycopy(out, 0, nb, 0, o);
/* 537 */     return nb;
/*     */   }
/*     */ 
/*     */   private static char[] initEncodeMap()
/*     */   {
/* 542 */     char[] map = new char[64];
/*     */ 
/* 544 */     for (int i = 0; i < 26; i++) {
/* 545 */       map[i] = ((char)(65 + i));
/*     */     }
/* 547 */     for (i = 26; i < 52; i++) {
/* 548 */       map[i] = ((char)(97 + (i - 26)));
/*     */     }
/* 550 */     for (i = 52; i < 62; i++) {
/* 551 */       map[i] = ((char)(48 + (i - 52)));
/*     */     }
/* 553 */     map[62] = '+';
/* 554 */     map[63] = '/';
/*     */ 
/* 556 */     return map;
/*     */   }
/*     */ 
/*     */   public static char encode(int i) {
/* 560 */     return encodeMap[(i & 0x3F)];
/*     */   }
/*     */ 
/*     */   public static byte encodeByte(int i) {
/* 564 */     return (byte)encodeMap[(i & 0x3F)];
/*     */   }
/*     */ 
/*     */   public static String _printBase64Binary(byte[] input) {
/* 568 */     return _printBase64Binary(input, 0, input.length);
/*     */   }
/*     */ 
/*     */   public static String _printBase64Binary(byte[] input, int offset, int len) {
/* 572 */     char[] buf = new char[(len + 2) / 3 * 4];
/* 573 */     int ptr = _printBase64Binary(input, offset, len, buf, 0);
/* 574 */     assert (ptr == buf.length);
/* 575 */     return new String(buf);
/*     */   }
/*     */ 
/*     */   public static int _printBase64Binary(byte[] input, int offset, int len, char[] buf, int ptr)
/*     */   {
/* 589 */     int remaining = len;
/*     */ 
/* 591 */     for (int i = offset; remaining >= 3; i += 3) {
/* 592 */       buf[(ptr++)] = encode(input[i] >> 2);
/* 593 */       buf[(ptr++)] = encode((input[i] & 0x3) << 4 | input[(i + 1)] >> 4 & 0xF);
/*     */ 
/* 596 */       buf[(ptr++)] = encode((input[(i + 1)] & 0xF) << 2 | input[(i + 2)] >> 6 & 0x3);
/*     */ 
/* 599 */       buf[(ptr++)] = encode(input[(i + 2)] & 0x3F);
/*     */ 
/* 591 */       remaining -= 3;
/*     */     }
/*     */ 
/* 602 */     if (remaining == 1) {
/* 603 */       buf[(ptr++)] = encode(input[i] >> 2);
/* 604 */       buf[(ptr++)] = encode((input[i] & 0x3) << 4);
/* 605 */       buf[(ptr++)] = '=';
/* 606 */       buf[(ptr++)] = '=';
/*     */     }
/*     */ 
/* 609 */     if (remaining == 2) {
/* 610 */       buf[(ptr++)] = encode(input[i] >> 2);
/* 611 */       buf[(ptr++)] = encode((input[i] & 0x3) << 4 | input[(i + 1)] >> 4 & 0xF);
/*     */ 
/* 613 */       buf[(ptr++)] = encode((input[(i + 1)] & 0xF) << 2);
/* 614 */       buf[(ptr++)] = '=';
/*     */     }
/* 616 */     return ptr;
/*     */   }
/*     */ 
/*     */   public static void _printBase64Binary(byte[] input, int offset, int len, XMLStreamWriter output) throws XMLStreamException {
/* 620 */     int remaining = len;
/*     */ 
/* 622 */     char[] buf = new char[4];
/*     */ 
/* 624 */     for (int i = offset; remaining >= 3; i += 3) {
/* 625 */       buf[0] = encode(input[i] >> 2);
/* 626 */       buf[1] = encode((input[i] & 0x3) << 4 | input[(i + 1)] >> 4 & 0xF);
/*     */ 
/* 629 */       buf[2] = encode((input[(i + 1)] & 0xF) << 2 | input[(i + 2)] >> 6 & 0x3);
/*     */ 
/* 632 */       buf[3] = encode(input[(i + 2)] & 0x3F);
/* 633 */       output.writeCharacters(buf, 0, 4);
/*     */ 
/* 624 */       remaining -= 3;
/*     */     }
/*     */ 
/* 636 */     if (remaining == 1) {
/* 637 */       buf[0] = encode(input[i] >> 2);
/* 638 */       buf[1] = encode((input[i] & 0x3) << 4);
/* 639 */       buf[2] = '=';
/* 640 */       buf[3] = '=';
/* 641 */       output.writeCharacters(buf, 0, 4);
/*     */     }
/*     */ 
/* 644 */     if (remaining == 2) {
/* 645 */       buf[0] = encode(input[i] >> 2);
/* 646 */       buf[1] = encode((input[i] & 0x3) << 4 | input[(i + 1)] >> 4 & 0xF);
/*     */ 
/* 648 */       buf[2] = encode((input[(i + 1)] & 0xF) << 2);
/* 649 */       buf[3] = '=';
/* 650 */       output.writeCharacters(buf, 0, 4);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static int _printBase64Binary(byte[] input, int offset, int len, byte[] out, int ptr)
/*     */   {
/* 665 */     byte[] buf = out;
/* 666 */     int remaining = len;
/*     */ 
/* 668 */     for (int i = offset; remaining >= 3; i += 3) {
/* 669 */       buf[(ptr++)] = encodeByte(input[i] >> 2);
/* 670 */       buf[(ptr++)] = encodeByte((input[i] & 0x3) << 4 | input[(i + 1)] >> 4 & 0xF);
/*     */ 
/* 673 */       buf[(ptr++)] = encodeByte((input[(i + 1)] & 0xF) << 2 | input[(i + 2)] >> 6 & 0x3);
/*     */ 
/* 676 */       buf[(ptr++)] = encodeByte(input[(i + 2)] & 0x3F);
/*     */ 
/* 668 */       remaining -= 3;
/*     */     }
/*     */ 
/* 679 */     if (remaining == 1) {
/* 680 */       buf[(ptr++)] = encodeByte(input[i] >> 2);
/* 681 */       buf[(ptr++)] = encodeByte((input[i] & 0x3) << 4);
/* 682 */       buf[(ptr++)] = 61;
/* 683 */       buf[(ptr++)] = 61;
/*     */     }
/*     */ 
/* 686 */     if (remaining == 2) {
/* 687 */       buf[(ptr++)] = encodeByte(input[i] >> 2);
/* 688 */       buf[(ptr++)] = encodeByte((input[i] & 0x3) << 4 | input[(i + 1)] >> 4 & 0xF);
/*     */ 
/* 691 */       buf[(ptr++)] = encodeByte((input[(i + 1)] & 0xF) << 2);
/* 692 */       buf[(ptr++)] = 61;
/*     */     }
/*     */ 
/* 695 */     return ptr;
/*     */   }
/*     */ 
/*     */   private static CharSequence removeOptionalPlus(CharSequence s) {
/* 699 */     int len = s.length();
/*     */ 
/* 701 */     if ((len <= 1) || (s.charAt(0) != '+')) {
/* 702 */       return s;
/*     */     }
/*     */ 
/* 705 */     s = s.subSequence(1, len);
/* 706 */     char ch = s.charAt(0);
/* 707 */     if (('0' <= ch) && (ch <= '9')) {
/* 708 */       return s;
/*     */     }
/* 710 */     if ('.' == ch) {
/* 711 */       return s;
/*     */     }
/*     */ 
/* 714 */     throw new NumberFormatException();
/*     */   }
/*     */ 
/*     */   private static boolean isDigitOrPeriodOrSign(char ch) {
/* 718 */     if (('0' <= ch) && (ch <= '9')) {
/* 719 */       return true;
/*     */     }
/* 721 */     if ((ch == '+') || (ch == '-') || (ch == '.')) {
/* 722 */       return true;
/*     */     }
/* 724 */     return false;
/*     */   }
/*     */ 
/*     */   public static DatatypeFactory getDatatypeFactory()
/*     */   {
/* 730 */     ClassLoader tccl = (ClassLoader)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public ClassLoader run() {
/* 732 */         return Thread.currentThread().getContextClassLoader();
/*     */       }
/*     */     });
/* 735 */     DatatypeFactory df = (DatatypeFactory)DF_CACHE.get(tccl);
/* 736 */     if (df == null) {
/* 737 */       synchronized (DatatypeConverterImpl.class) {
/* 738 */         df = (DatatypeFactory)DF_CACHE.get(tccl);
/* 739 */         if (df == null) {
/*     */           try {
/* 741 */             df = DatatypeFactory.newInstance();
/*     */           } catch (DatatypeConfigurationException e) {
/* 743 */             throw new Error(Messages.FAILED_TO_INITIALE_DATATYPE_FACTORY.format(new Object[0]), e);
/*     */           }
/* 745 */           DF_CACHE.put(tccl, df);
/*     */         }
/*     */       }
/*     */     }
/* 749 */     return df;
/*     */   }
/*     */ 
/*     */   private static final class CalendarFormatter
/*     */   {
/*     */     public static String doFormat(String format, Calendar cal) throws IllegalArgumentException {
/* 755 */       int fidx = 0;
/* 756 */       int flen = format.length();
/* 757 */       StringBuilder buf = new StringBuilder();
/*     */ 
/* 759 */       while (fidx < flen) {
/* 760 */         char fch = format.charAt(fidx++);
/*     */ 
/* 762 */         if (fch != '%') {
/* 763 */           buf.append(fch);
/*     */         }
/*     */         else
/*     */         {
/* 768 */           switch (format.charAt(fidx++)) {
/*     */           case 'Y':
/* 770 */             formatYear(cal, buf);
/* 771 */             break;
/*     */           case 'M':
/* 774 */             formatMonth(cal, buf);
/* 775 */             break;
/*     */           case 'D':
/* 778 */             formatDays(cal, buf);
/* 779 */             break;
/*     */           case 'h':
/* 782 */             formatHours(cal, buf);
/* 783 */             break;
/*     */           case 'm':
/* 786 */             formatMinutes(cal, buf);
/* 787 */             break;
/*     */           case 's':
/* 790 */             formatSeconds(cal, buf);
/* 791 */             break;
/*     */           case 'z':
/* 794 */             formatTimeZone(cal, buf);
/* 795 */             break;
/*     */           default:
/* 799 */             throw new InternalError();
/*     */           }
/*     */         }
/*     */       }
/* 803 */       return buf.toString();
/*     */     }
/*     */ 
/*     */     private static void formatYear(Calendar cal, StringBuilder buf) {
/* 807 */       int year = cal.get(1);
/*     */       String s;
/*     */       String s;
/* 810 */       if (year <= 0)
/*     */       {
/* 812 */         s = Integer.toString(1 - year);
/*     */       }
/*     */       else {
/* 815 */         s = Integer.toString(year);
/*     */       }
/*     */ 
/* 818 */       while (s.length() < 4) {
/* 819 */         s = '0' + s;
/*     */       }
/* 821 */       if (year <= 0) {
/* 822 */         s = '-' + s;
/*     */       }
/*     */ 
/* 825 */       buf.append(s);
/*     */     }
/*     */ 
/*     */     private static void formatMonth(Calendar cal, StringBuilder buf) {
/* 829 */       formatTwoDigits(cal.get(2) + 1, buf);
/*     */     }
/*     */ 
/*     */     private static void formatDays(Calendar cal, StringBuilder buf) {
/* 833 */       formatTwoDigits(cal.get(5), buf);
/*     */     }
/*     */ 
/*     */     private static void formatHours(Calendar cal, StringBuilder buf) {
/* 837 */       formatTwoDigits(cal.get(11), buf);
/*     */     }
/*     */ 
/*     */     private static void formatMinutes(Calendar cal, StringBuilder buf) {
/* 841 */       formatTwoDigits(cal.get(12), buf);
/*     */     }
/*     */ 
/*     */     private static void formatSeconds(Calendar cal, StringBuilder buf) {
/* 845 */       formatTwoDigits(cal.get(13), buf);
/* 846 */       if (cal.isSet(14)) {
/* 847 */         int n = cal.get(14);
/* 848 */         if (n != 0) {
/* 849 */           String ms = Integer.toString(n);
/* 850 */           while (ms.length() < 3) {
/* 851 */             ms = '0' + ms;
/*     */           }
/* 853 */           buf.append('.');
/* 854 */           buf.append(ms);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     private static void formatTimeZone(Calendar cal, StringBuilder buf)
/*     */     {
/* 861 */       TimeZone tz = cal.getTimeZone();
/*     */ 
/* 863 */       if (tz == null) {
/* 864 */         return;
/*     */       }
/*     */ 
/* 868 */       int offset = tz.getOffset(cal.getTime().getTime());
/*     */ 
/* 870 */       if (offset == 0) {
/* 871 */         buf.append('Z');
/* 872 */         return;
/*     */       }
/*     */ 
/* 875 */       if (offset >= 0) {
/* 876 */         buf.append('+');
/*     */       } else {
/* 878 */         buf.append('-');
/* 879 */         offset *= -1;
/*     */       }
/*     */ 
/* 882 */       offset /= 60000;
/*     */ 
/* 884 */       formatTwoDigits(offset / 60, buf);
/* 885 */       buf.append(':');
/* 886 */       formatTwoDigits(offset % 60, buf);
/*     */     }
/*     */ 
/*     */     private static void formatTwoDigits(int n, StringBuilder buf)
/*     */     {
/* 892 */       if (n < 10) {
/* 893 */         buf.append('0');
/*     */       }
/* 895 */       buf.append(n);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.DatatypeConverterImpl
 * JD-Core Version:    0.6.2
 */