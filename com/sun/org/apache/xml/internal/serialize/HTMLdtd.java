/*     */ package com.sun.org.apache.xml.internal.serialize;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.dom.DOMMessageFormatter;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public final class HTMLdtd
/*     */ {
/*     */   public static final String HTMLPublicId = "-//W3C//DTD HTML 4.01//EN";
/*     */   public static final String HTMLSystemId = "http://www.w3.org/TR/html4/strict.dtd";
/*     */   public static final String XHTMLPublicId = "-//W3C//DTD XHTML 1.0 Strict//EN";
/*     */   public static final String XHTMLSystemId = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
/*     */   private static Hashtable _byChar;
/*     */   private static Hashtable _byName;
/*     */   private static Hashtable _boolAttrs;
/* 475 */   private static Hashtable _elemDefs = new Hashtable();
/*     */   private static final String ENTITIES_RESOURCE = "HTMLEntities.res";
/*     */   private static final int ONLY_OPENING = 1;
/*     */   private static final int ELEM_CONTENT = 2;
/*     */   private static final int PRESERVE = 4;
/*     */   private static final int OPT_CLOSING = 8;
/*     */   private static final int EMPTY = 17;
/*     */   private static final int ALLOWED_HEAD = 32;
/*     */   private static final int CLOSE_P = 64;
/*     */   private static final int CLOSE_DD_DT = 128;
/*     */   private static final int CLOSE_SELF = 256;
/*     */   private static final int CLOSE_TABLE = 512;
/*     */   private static final int CLOSE_TH_TD = 16384;
/*     */ 
/*     */   public static boolean isEmptyTag(String tagName)
/*     */   {
/* 181 */     return isElement(tagName, 17);
/*     */   }
/*     */ 
/*     */   public static boolean isElementContent(String tagName)
/*     */   {
/* 195 */     return isElement(tagName, 2);
/*     */   }
/*     */ 
/*     */   public static boolean isPreserveSpace(String tagName)
/*     */   {
/* 209 */     return isElement(tagName, 4);
/*     */   }
/*     */ 
/*     */   public static boolean isOptionalClosing(String tagName)
/*     */   {
/* 223 */     return isElement(tagName, 8);
/*     */   }
/*     */ 
/*     */   public static boolean isOnlyOpening(String tagName)
/*     */   {
/* 236 */     return isElement(tagName, 1);
/*     */   }
/*     */ 
/*     */   public static boolean isClosing(String tagName, String openTag)
/*     */   {
/* 253 */     if (openTag.equalsIgnoreCase("HEAD")) {
/* 254 */       return !isElement(tagName, 32);
/*     */     }
/* 256 */     if (openTag.equalsIgnoreCase("P")) {
/* 257 */       return isElement(tagName, 64);
/*     */     }
/* 259 */     if ((openTag.equalsIgnoreCase("DT")) || (openTag.equalsIgnoreCase("DD"))) {
/* 260 */       return isElement(tagName, 128);
/*     */     }
/* 262 */     if ((openTag.equalsIgnoreCase("LI")) || (openTag.equalsIgnoreCase("OPTION"))) {
/* 263 */       return isElement(tagName, 256);
/*     */     }
/* 265 */     if ((openTag.equalsIgnoreCase("THEAD")) || (openTag.equalsIgnoreCase("TFOOT")) || (openTag.equalsIgnoreCase("TBODY")) || (openTag.equalsIgnoreCase("TR")) || (openTag.equalsIgnoreCase("COLGROUP")))
/*     */     {
/* 268 */       return isElement(tagName, 512);
/*     */     }
/* 270 */     if ((openTag.equalsIgnoreCase("TH")) || (openTag.equalsIgnoreCase("TD")))
/* 271 */       return isElement(tagName, 16384);
/* 272 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isURI(String tagName, String attrName)
/*     */   {
/* 287 */     return (attrName.equalsIgnoreCase("href")) || (attrName.equalsIgnoreCase("src"));
/*     */   }
/*     */ 
/*     */   public static boolean isBoolean(String tagName, String attrName)
/*     */   {
/* 303 */     String[] attrNames = (String[])_boolAttrs.get(tagName.toUpperCase(Locale.ENGLISH));
/* 304 */     if (attrNames == null)
/* 305 */       return false;
/* 306 */     for (int i = 0; i < attrNames.length; i++)
/* 307 */       if (attrNames[i].equalsIgnoreCase(attrName))
/* 308 */         return true;
/* 309 */     return false;
/*     */   }
/*     */ 
/*     */   public static int charFromName(String name)
/*     */   {
/* 325 */     initialize();
/* 326 */     Object value = _byName.get(name);
/* 327 */     if ((value != null) && ((value instanceof Integer))) {
/* 328 */       return ((Integer)value).intValue();
/*     */     }
/* 330 */     return -1;
/*     */   }
/*     */ 
/*     */   public static String fromChar(int value)
/*     */   {
/* 344 */     if (value > 65535) {
/* 345 */       return null;
/*     */     }
/*     */ 
/* 349 */     initialize();
/* 350 */     String name = (String)_byChar.get(new Integer(value));
/* 351 */     return name;
/*     */   }
/*     */ 
/*     */   private static void initialize()
/*     */   {
/* 363 */     InputStream is = null;
/* 364 */     BufferedReader reader = null;
/*     */ 
/* 372 */     if (_byName != null)
/* 373 */       return;
/*     */     try {
/* 375 */       _byName = new Hashtable();
/* 376 */       _byChar = new Hashtable();
/* 377 */       is = HTMLdtd.class.getResourceAsStream("HTMLEntities.res");
/* 378 */       if (is == null) {
/* 379 */         throw new RuntimeException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "ResourceNotFound", new Object[] { "HTMLEntities.res" }));
/*     */       }
/*     */ 
/* 384 */       reader = new BufferedReader(new InputStreamReader(is, "ASCII"));
/* 385 */       String line = reader.readLine();
/* 386 */       while (line != null)
/* 387 */         if ((line.length() == 0) || (line.charAt(0) == '#')) {
/* 388 */           line = reader.readLine();
/*     */         }
/*     */         else {
/* 391 */           int index = line.indexOf(' ');
/* 392 */           if (index > 1) {
/* 393 */             String name = line.substring(0, index);
/* 394 */             index++;
/* 395 */             if (index < line.length()) {
/* 396 */               String value = line.substring(index);
/* 397 */               index = value.indexOf(' ');
/* 398 */               if (index > 0)
/* 399 */                 value = value.substring(0, index);
/* 400 */               int code = Integer.parseInt(value);
/* 401 */               defineEntity(name, (char)code);
/*     */             }
/*     */           }
/* 404 */           line = reader.readLine();
/*     */         }
/* 406 */       is.close();
/*     */     } catch (Exception except) {
/* 408 */       throw new RuntimeException(DOMMessageFormatter.formatMessage("http://apache.org/xml/serializer", "ResourceNotLoaded", new Object[] { "HTMLEntities.res", except.toString() }));
/*     */     }
/*     */     finally
/*     */     {
/* 413 */       if (is != null)
/*     */         try {
/* 415 */           is.close();
/*     */         }
/*     */         catch (Exception except)
/*     */         {
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void defineEntity(String name, char value)
/*     */   {
/* 436 */     if (_byName.get(name) == null) {
/* 437 */       _byName.put(name, new Integer(value));
/* 438 */       _byChar.put(new Integer(value), name);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void defineElement(String name, int flags)
/*     */   {
/* 445 */     _elemDefs.put(name, new Integer(flags));
/*     */   }
/*     */ 
/*     */   private static void defineBoolean(String tagName, String attrName)
/*     */   {
/* 451 */     defineBoolean(tagName, new String[] { attrName });
/*     */   }
/*     */ 
/*     */   private static void defineBoolean(String tagName, String[] attrNames)
/*     */   {
/* 457 */     _boolAttrs.put(tagName, attrNames);
/*     */   }
/*     */ 
/*     */   private static boolean isElement(String name, int flag)
/*     */   {
/* 465 */     Integer flags = (Integer)_elemDefs.get(name.toUpperCase(Locale.ENGLISH));
/* 466 */     if (flags == null) {
/* 467 */       return false;
/*     */     }
/* 469 */     return (flags.intValue() & flag) == flag;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 476 */     defineElement("ADDRESS", 64);
/* 477 */     defineElement("AREA", 17);
/* 478 */     defineElement("BASE", 49);
/* 479 */     defineElement("BASEFONT", 17);
/* 480 */     defineElement("BLOCKQUOTE", 64);
/* 481 */     defineElement("BODY", 8);
/* 482 */     defineElement("BR", 17);
/* 483 */     defineElement("COL", 17);
/* 484 */     defineElement("COLGROUP", 522);
/* 485 */     defineElement("DD", 137);
/* 486 */     defineElement("DIV", 64);
/* 487 */     defineElement("DL", 66);
/* 488 */     defineElement("DT", 137);
/* 489 */     defineElement("FIELDSET", 64);
/* 490 */     defineElement("FORM", 64);
/* 491 */     defineElement("FRAME", 25);
/* 492 */     defineElement("H1", 64);
/* 493 */     defineElement("H2", 64);
/* 494 */     defineElement("H3", 64);
/* 495 */     defineElement("H4", 64);
/* 496 */     defineElement("H5", 64);
/* 497 */     defineElement("H6", 64);
/* 498 */     defineElement("HEAD", 10);
/* 499 */     defineElement("HR", 81);
/* 500 */     defineElement("HTML", 10);
/* 501 */     defineElement("IMG", 17);
/* 502 */     defineElement("INPUT", 17);
/* 503 */     defineElement("ISINDEX", 49);
/* 504 */     defineElement("LI", 265);
/* 505 */     defineElement("LINK", 49);
/* 506 */     defineElement("MAP", 32);
/* 507 */     defineElement("META", 49);
/* 508 */     defineElement("OL", 66);
/* 509 */     defineElement("OPTGROUP", 2);
/* 510 */     defineElement("OPTION", 265);
/* 511 */     defineElement("P", 328);
/* 512 */     defineElement("PARAM", 17);
/* 513 */     defineElement("PRE", 68);
/* 514 */     defineElement("SCRIPT", 36);
/* 515 */     defineElement("NOSCRIPT", 36);
/* 516 */     defineElement("SELECT", 2);
/* 517 */     defineElement("STYLE", 36);
/* 518 */     defineElement("TABLE", 66);
/* 519 */     defineElement("TBODY", 522);
/* 520 */     defineElement("TD", 16392);
/* 521 */     defineElement("TEXTAREA", 4);
/* 522 */     defineElement("TFOOT", 522);
/* 523 */     defineElement("TH", 16392);
/* 524 */     defineElement("THEAD", 522);
/* 525 */     defineElement("TITLE", 32);
/* 526 */     defineElement("TR", 522);
/* 527 */     defineElement("UL", 66);
/*     */ 
/* 529 */     _boolAttrs = new Hashtable();
/* 530 */     defineBoolean("AREA", "href");
/* 531 */     defineBoolean("BUTTON", "disabled");
/* 532 */     defineBoolean("DIR", "compact");
/* 533 */     defineBoolean("DL", "compact");
/* 534 */     defineBoolean("FRAME", "noresize");
/* 535 */     defineBoolean("HR", "noshade");
/* 536 */     defineBoolean("IMAGE", "ismap");
/* 537 */     defineBoolean("INPUT", new String[] { "defaultchecked", "checked", "readonly", "disabled" });
/* 538 */     defineBoolean("LINK", "link");
/* 539 */     defineBoolean("MENU", "compact");
/* 540 */     defineBoolean("OBJECT", "declare");
/* 541 */     defineBoolean("OL", "compact");
/* 542 */     defineBoolean("OPTGROUP", "disabled");
/* 543 */     defineBoolean("OPTION", new String[] { "default-selected", "selected", "disabled" });
/* 544 */     defineBoolean("SCRIPT", "defer");
/* 545 */     defineBoolean("SELECT", new String[] { "multiple", "disabled" });
/* 546 */     defineBoolean("STYLE", "disabled");
/* 547 */     defineBoolean("TD", "nowrap");
/* 548 */     defineBoolean("TH", "nowrap");
/* 549 */     defineBoolean("TEXTAREA", new String[] { "disabled", "readonly" });
/* 550 */     defineBoolean("UL", "compact");
/*     */ 
/* 552 */     initialize();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serialize.HTMLdtd
 * JD-Core Version:    0.6.2
 */