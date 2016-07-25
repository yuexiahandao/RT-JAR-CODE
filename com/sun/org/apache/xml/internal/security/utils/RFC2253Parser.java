/*     */ package com.sun.org.apache.xml.internal.security.utils;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.io.StringReader;
/*     */ 
/*     */ public class RFC2253Parser
/*     */ {
/*  41 */   static boolean _TOXML = true;
/*     */ 
/* 549 */   static int counter = 0;
/*     */ 
/*     */   public static String rfc2253toXMLdsig(String paramString)
/*     */   {
/*  52 */     _TOXML = true;
/*     */ 
/*  55 */     String str = normalize(paramString);
/*     */ 
/*  57 */     return rfctoXML(str);
/*     */   }
/*     */ 
/*     */   public static String xmldsigtoRFC2253(String paramString)
/*     */   {
/*  68 */     _TOXML = false;
/*     */ 
/*  71 */     String str = normalize(paramString);
/*     */ 
/*  73 */     return xmltoRFC(str);
/*     */   }
/*     */ 
/*     */   public static String normalize(String paramString)
/*     */   {
/*  85 */     if ((paramString == null) || (paramString.equals(""))) {
/*  86 */       return "";
/*     */     }
/*     */     try
/*     */     {
/*  90 */       String str = semicolonToComma(paramString);
/*  91 */       StringBuffer localStringBuffer = new StringBuffer();
/*  92 */       int i = 0;
/*  93 */       int j = 0;
/*     */       int k;
/*  97 */       for (int m = 0; (k = str.indexOf(",", m)) >= 0; m = k + 1) {
/*  98 */         j += countQuotes(str, m, k);
/*     */ 
/* 100 */         if ((k > 0) && (str.charAt(k - 1) != '\\') && (j % 2 != 1)) {
/* 101 */           localStringBuffer.append(parseRDN(str.substring(i, k).trim()) + ",");
/*     */ 
/* 103 */           i = k + 1;
/* 104 */           j = 0;
/*     */         }
/*     */       }
/*     */ 
/* 108 */       localStringBuffer.append(parseRDN(trim(str.substring(i))));
/*     */ 
/* 110 */       return localStringBuffer.toString(); } catch (IOException localIOException) {
/*     */     }
/* 112 */     return paramString;
/*     */   }
/*     */ 
/*     */   static String parseRDN(String paramString)
/*     */     throws IOException
/*     */   {
/* 125 */     StringBuffer localStringBuffer = new StringBuffer();
/* 126 */     int i = 0;
/* 127 */     int j = 0;
/*     */     int k;
/* 130 */     for (int m = 0; (k = paramString.indexOf("+", m)) >= 0; m = k + 1) {
/* 131 */       j += countQuotes(paramString, m, k);
/*     */ 
/* 133 */       if ((k > 0) && (paramString.charAt(k - 1) != '\\') && (j % 2 != 1)) {
/* 134 */         localStringBuffer.append(parseATAV(trim(paramString.substring(i, k))) + "+");
/*     */ 
/* 136 */         i = k + 1;
/* 137 */         j = 0;
/*     */       }
/*     */     }
/*     */ 
/* 141 */     localStringBuffer.append(parseATAV(trim(paramString.substring(i))));
/*     */ 
/* 143 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   static String parseATAV(String paramString)
/*     */     throws IOException
/*     */   {
/* 155 */     int i = paramString.indexOf("=");
/*     */ 
/* 157 */     if ((i == -1) || ((i > 0) && (paramString.charAt(i - 1) == '\\'))) {
/* 158 */       return paramString;
/*     */     }
/* 160 */     String str1 = normalizeAT(paramString.substring(0, i));
/*     */ 
/* 162 */     String str2 = null;
/* 163 */     if ((str1.charAt(0) >= '0') && (str1.charAt(0) <= '9'))
/* 164 */       str2 = paramString.substring(i + 1);
/*     */     else {
/* 166 */       str2 = normalizeV(paramString.substring(i + 1));
/*     */     }
/*     */ 
/* 169 */     return str1 + "=" + str2;
/*     */   }
/*     */ 
/*     */   static String normalizeAT(String paramString)
/*     */   {
/* 181 */     String str = paramString.toUpperCase().trim();
/*     */ 
/* 183 */     if (str.startsWith("OID")) {
/* 184 */       str = str.substring(3);
/*     */     }
/*     */ 
/* 187 */     return str;
/*     */   }
/*     */ 
/*     */   static String normalizeV(String paramString)
/*     */     throws IOException
/*     */   {
/* 199 */     String str = trim(paramString);
/*     */ 
/* 201 */     if (str.startsWith("\"")) {
/* 202 */       StringBuffer localStringBuffer = new StringBuffer();
/* 203 */       StringReader localStringReader = new StringReader(str.substring(1, str.length() - 1));
/*     */ 
/* 205 */       int i = 0;
/*     */ 
/* 208 */       while ((i = localStringReader.read()) > -1) {
/* 209 */         char c = (char)i;
/*     */ 
/* 212 */         if ((c == ',') || (c == '=') || (c == '+') || (c == '<') || (c == '>') || (c == '#') || (c == ';'))
/*     */         {
/* 214 */           localStringBuffer.append('\\');
/*     */         }
/*     */ 
/* 217 */         localStringBuffer.append(c);
/*     */       }
/*     */ 
/* 220 */       str = trim(localStringBuffer.toString());
/*     */     }
/*     */ 
/* 223 */     if (_TOXML == true) {
/* 224 */       if (str.startsWith("#")) {
/* 225 */         str = '\\' + str;
/*     */       }
/*     */     }
/* 228 */     else if (str.startsWith("\\#")) {
/* 229 */       str = str.substring(1);
/*     */     }
/*     */ 
/* 233 */     return str;
/*     */   }
/*     */ 
/*     */   static String rfctoXML(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 245 */       String str = changeLess32toXML(paramString);
/*     */ 
/* 247 */       return changeWStoXML(str); } catch (Exception localException) {
/*     */     }
/* 249 */     return paramString;
/*     */   }
/*     */ 
/*     */   static String xmltoRFC(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 262 */       String str = changeLess32toRFC(paramString);
/*     */ 
/* 264 */       return changeWStoRFC(str); } catch (Exception localException) {
/*     */     }
/* 266 */     return paramString;
/*     */   }
/*     */ 
/*     */   static String changeLess32toRFC(String paramString)
/*     */     throws IOException
/*     */   {
/* 279 */     StringBuffer localStringBuffer = new StringBuffer();
/* 280 */     StringReader localStringReader = new StringReader(paramString);
/* 281 */     int i = 0;
/*     */ 
/* 284 */     while ((i = localStringReader.read()) > -1) {
/* 285 */       char c1 = (char)i;
/*     */ 
/* 287 */       if (c1 == '\\') {
/* 288 */         localStringBuffer.append(c1);
/*     */ 
/* 290 */         char c2 = (char)localStringReader.read();
/* 291 */         char c3 = (char)localStringReader.read();
/*     */ 
/* 294 */         if (((c2 >= '0') && (c2 <= '9')) || ((c2 >= 'A') && (c2 <= 'F')) || ((c2 >= 'a') && (c2 <= 'f') && (((c3 >= '0') && (c3 <= '9')) || ((c3 >= 'A') && (c3 <= 'F')) || ((c3 >= 'a') && (c3 <= 'f')))))
/*     */         {
/* 298 */           char c4 = (char)Byte.parseByte("" + c2 + c3, 16);
/*     */ 
/* 300 */           localStringBuffer.append(c4);
/*     */         } else {
/* 302 */           localStringBuffer.append(c2);
/* 303 */           localStringBuffer.append(c3);
/*     */         }
/*     */       } else {
/* 306 */         localStringBuffer.append(c1);
/*     */       }
/*     */     }
/*     */ 
/* 310 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   static String changeLess32toXML(String paramString)
/*     */     throws IOException
/*     */   {
/* 322 */     StringBuffer localStringBuffer = new StringBuffer();
/* 323 */     StringReader localStringReader = new StringReader(paramString);
/* 324 */     int i = 0;
/*     */ 
/* 326 */     while ((i = localStringReader.read()) > -1) {
/* 327 */       if (i < 32) {
/* 328 */         localStringBuffer.append('\\');
/* 329 */         localStringBuffer.append(Integer.toHexString(i));
/*     */       } else {
/* 331 */         localStringBuffer.append((char)i);
/*     */       }
/*     */     }
/*     */ 
/* 335 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   static String changeWStoXML(String paramString)
/*     */     throws IOException
/*     */   {
/* 347 */     StringBuffer localStringBuffer = new StringBuffer();
/* 348 */     StringReader localStringReader = new StringReader(paramString);
/* 349 */     int i = 0;
/*     */ 
/* 352 */     while ((i = localStringReader.read()) > -1) {
/* 353 */       char c1 = (char)i;
/*     */ 
/* 355 */       if (c1 == '\\') {
/* 356 */         char c2 = (char)localStringReader.read();
/*     */ 
/* 358 */         if (c2 == ' ') {
/* 359 */           localStringBuffer.append('\\');
/*     */ 
/* 361 */           String str = "20";
/*     */ 
/* 363 */           localStringBuffer.append(str);
/*     */         } else {
/* 365 */           localStringBuffer.append('\\');
/* 366 */           localStringBuffer.append(c2);
/*     */         }
/*     */       } else {
/* 369 */         localStringBuffer.append(c1);
/*     */       }
/*     */     }
/*     */ 
/* 373 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   static String changeWStoRFC(String paramString)
/*     */   {
/* 384 */     StringBuffer localStringBuffer = new StringBuffer();
/* 385 */     int i = 0;
/*     */     int j;
/* 388 */     for (int k = 0; (j = paramString.indexOf("\\20", k)) >= 0; k = j + 3) {
/* 389 */       localStringBuffer.append(trim(paramString.substring(i, j)) + "\\ ");
/*     */ 
/* 391 */       i = j + 3;
/*     */     }
/*     */ 
/* 394 */     localStringBuffer.append(paramString.substring(i));
/*     */ 
/* 396 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   static String semicolonToComma(String paramString)
/*     */   {
/* 406 */     return removeWSandReplace(paramString, ";", ",");
/*     */   }
/*     */ 
/*     */   static String removeWhiteSpace(String paramString1, String paramString2)
/*     */   {
/* 417 */     return removeWSandReplace(paramString1, paramString2, paramString2);
/*     */   }
/*     */ 
/*     */   static String removeWSandReplace(String paramString1, String paramString2, String paramString3)
/*     */   {
/* 430 */     StringBuffer localStringBuffer = new StringBuffer();
/* 431 */     int i = 0;
/* 432 */     int j = 0;
/*     */     int k;
/* 435 */     for (int m = 0; (k = paramString1.indexOf(paramString2, m)) >= 0; m = k + 1) {
/* 436 */       j += countQuotes(paramString1, m, k);
/*     */ 
/* 438 */       if ((k > 0) && (paramString1.charAt(k - 1) != '\\') && (j % 2 != 1)) {
/* 439 */         localStringBuffer.append(trim(paramString1.substring(i, k)) + paramString3);
/*     */ 
/* 441 */         i = k + 1;
/* 442 */         j = 0;
/*     */       }
/*     */     }
/*     */ 
/* 446 */     localStringBuffer.append(trim(paramString1.substring(i)));
/*     */ 
/* 448 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static int countQuotes(String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 461 */     int i = 0;
/*     */ 
/* 463 */     for (int j = paramInt1; j < paramInt2; j++) {
/* 464 */       if (paramString.charAt(j) == '"') {
/* 465 */         i++;
/*     */       }
/*     */     }
/*     */ 
/* 469 */     return i;
/*     */   }
/*     */ 
/*     */   static String trim(String paramString)
/*     */   {
/* 482 */     String str = paramString.trim();
/* 483 */     int i = paramString.indexOf(str) + str.length();
/*     */ 
/* 485 */     if ((paramString.length() > i) && (str.endsWith("\\")) && (!str.endsWith("\\\\")))
/*     */     {
/* 487 */       if (paramString.charAt(i) == ' ') {
/* 488 */         str = str + " ";
/*     */       }
/*     */     }
/*     */ 
/* 492 */     return str;
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */     throws Exception
/*     */   {
/* 503 */     testToXML("CN=\"Steve, Kille\",  O=Isode Limited, C=GB");
/* 504 */     testToXML("CN=Steve Kille    ,   O=Isode Limited,C=GB");
/* 505 */     testToXML("\\ OU=Sales+CN=J. Smith,O=Widget Inc.,C=US\\ \\ ");
/* 506 */     testToXML("CN=L. Eagle,O=Sue\\, Grabbit and Runn,C=GB");
/* 507 */     testToXML("CN=Before\\0DAfter,O=Test,C=GB");
/* 508 */     testToXML("CN=\"L. Eagle,O=Sue, = + < > # ;Grabbit and Runn\",C=GB");
/* 509 */     testToXML("1.3.6.1.4.1.1466.0=#04024869,O=Test,C=GB");
/*     */ 
/* 512 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 514 */     localStringBuffer.append('L');
/* 515 */     localStringBuffer.append('u');
/* 516 */     localStringBuffer.append(50317);
/* 517 */     localStringBuffer.append('i');
/* 518 */     localStringBuffer.append(50311);
/*     */ 
/* 520 */     String str = "SN=" + localStringBuffer.toString();
/*     */ 
/* 522 */     testToXML(str);
/*     */ 
/* 525 */     testToRFC("CN=\"Steve, Kille\",  O=Isode Limited, C=GB");
/* 526 */     testToRFC("CN=Steve Kille    ,   O=Isode Limited,C=GB");
/* 527 */     testToRFC("\\20OU=Sales+CN=J. Smith,O=Widget Inc.,C=US\\20\\20 ");
/* 528 */     testToRFC("CN=L. Eagle,O=Sue\\, Grabbit and Runn,C=GB");
/* 529 */     testToRFC("CN=Before\\12After,O=Test,C=GB");
/* 530 */     testToRFC("CN=\"L. Eagle,O=Sue, = + < > # ;Grabbit and Runn\",C=GB");
/* 531 */     testToRFC("1.3.6.1.4.1.1466.0=\\#04024869,O=Test,C=GB");
/*     */ 
/* 534 */     localStringBuffer = new StringBuffer();
/*     */ 
/* 536 */     localStringBuffer.append('L');
/* 537 */     localStringBuffer.append('u');
/* 538 */     localStringBuffer.append(50317);
/* 539 */     localStringBuffer.append('i');
/* 540 */     localStringBuffer.append(50311);
/*     */ 
/* 542 */     str = "SN=" + localStringBuffer.toString();
/*     */ 
/* 544 */     testToRFC(str);
/*     */   }
/*     */ 
/*     */   static void testToXML(String paramString)
/*     */   {
/* 558 */     System.out.println("start " + counter++ + ": " + paramString);
/* 559 */     System.out.println("         " + rfc2253toXMLdsig(paramString));
/* 560 */     System.out.println("");
/*     */   }
/*     */ 
/*     */   static void testToRFC(String paramString)
/*     */   {
/* 570 */     System.out.println("start " + counter++ + ": " + paramString);
/* 571 */     System.out.println("         " + xmldsigtoRFC2253(paramString));
/* 572 */     System.out.println("");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.RFC2253Parser
 * JD-Core Version:    0.6.2
 */