/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.Hashtable;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.StyleConstants;
/*     */ import javax.swing.text.StyleContext;
/*     */ 
/*     */ public class HTML
/*     */ {
/* 539 */   private static final Hashtable<String, Tag> tagHashtable = new Hashtable(73);
/*     */ 
/* 542 */   private static final Hashtable<Object, Tag> scMapping = new Hashtable(8);
/*     */   public static final String NULL_ATTRIBUTE_VALUE = "#DEFAULT";
/*     */   private static final Hashtable<String, Attribute> attHashtable;
/*     */ 
/*     */   public static Tag[] getAllTags()
/*     */   {
/* 575 */     Tag[] arrayOfTag = new Tag[Tag.allTags.length];
/* 576 */     System.arraycopy(Tag.allTags, 0, arrayOfTag, 0, Tag.allTags.length);
/* 577 */     return arrayOfTag;
/*     */   }
/*     */ 
/*     */   public static Tag getTag(String paramString)
/*     */   {
/* 601 */     Tag localTag = (Tag)tagHashtable.get(paramString);
/* 602 */     return localTag == null ? null : localTag;
/*     */   }
/*     */ 
/*     */   static Tag getTagForStyleConstantsKey(StyleConstants paramStyleConstants)
/*     */   {
/* 616 */     return (Tag)scMapping.get(paramStyleConstants);
/*     */   }
/*     */ 
/*     */   public static int getIntegerAttributeValue(AttributeSet paramAttributeSet, Attribute paramAttribute, int paramInt)
/*     */   {
/* 631 */     int i = paramInt;
/* 632 */     String str = (String)paramAttributeSet.getAttribute(paramAttribute);
/* 633 */     if (str != null) {
/*     */       try {
/* 635 */         i = Integer.valueOf(str).intValue();
/*     */       } catch (NumberFormatException localNumberFormatException) {
/* 637 */         i = paramInt;
/*     */       }
/*     */     }
/* 640 */     return i;
/*     */   }
/*     */ 
/*     */   public static Attribute[] getAllAttributeKeys()
/*     */   {
/* 663 */     Attribute[] arrayOfAttribute = new Attribute[Attribute.allAttributes.length];
/* 664 */     System.arraycopy(Attribute.allAttributes, 0, arrayOfAttribute, 0, Attribute.allAttributes.length);
/*     */ 
/* 666 */     return arrayOfAttribute;
/*     */   }
/*     */ 
/*     */   public static Attribute getAttributeKey(String paramString)
/*     */   {
/* 690 */     Attribute localAttribute = (Attribute)attHashtable.get(paramString);
/* 691 */     if (localAttribute == null) {
/* 692 */       return null;
/*     */     }
/* 694 */     return localAttribute;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 546 */     for (int i = 0; i < Tag.allTags.length; i++) {
/* 547 */       tagHashtable.put(Tag.allTags[i].toString(), Tag.allTags[i]);
/* 548 */       StyleContext.registerStaticAttributeKey(Tag.allTags[i]);
/*     */     }
/* 550 */     StyleContext.registerStaticAttributeKey(Tag.IMPLIED);
/* 551 */     StyleContext.registerStaticAttributeKey(Tag.CONTENT);
/* 552 */     StyleContext.registerStaticAttributeKey(Tag.COMMENT);
/* 553 */     for (i = 0; i < Attribute.allAttributes.length; i++) {
/* 554 */       StyleContext.registerStaticAttributeKey(Attribute.allAttributes[i]);
/*     */     }
/*     */ 
/* 557 */     StyleContext.registerStaticAttributeKey("#DEFAULT");
/* 558 */     scMapping.put(StyleConstants.Bold, Tag.B);
/* 559 */     scMapping.put(StyleConstants.Italic, Tag.I);
/* 560 */     scMapping.put(StyleConstants.Underline, Tag.U);
/* 561 */     scMapping.put(StyleConstants.StrikeThrough, Tag.STRIKE);
/* 562 */     scMapping.put(StyleConstants.Superscript, Tag.SUP);
/* 563 */     scMapping.put(StyleConstants.Subscript, Tag.SUB);
/* 564 */     scMapping.put(StyleConstants.FontFamily, Tag.FONT);
/* 565 */     scMapping.put(StyleConstants.FontSize, Tag.FONT);
/*     */ 
/* 649 */     attHashtable = new Hashtable(77);
/*     */ 
/* 653 */     for (i = 0; i < Attribute.allAttributes.length; i++)
/* 654 */       attHashtable.put(Attribute.allAttributes[i].toString(), Attribute.allAttributes[i]);
/*     */   }
/*     */ 
/*     */   public static final class Attribute
/*     */   {
/*     */     private String name;
/* 367 */     public static final Attribute SIZE = new Attribute("size");
/* 368 */     public static final Attribute COLOR = new Attribute("color");
/* 369 */     public static final Attribute CLEAR = new Attribute("clear");
/* 370 */     public static final Attribute BACKGROUND = new Attribute("background");
/* 371 */     public static final Attribute BGCOLOR = new Attribute("bgcolor");
/* 372 */     public static final Attribute TEXT = new Attribute("text");
/* 373 */     public static final Attribute LINK = new Attribute("link");
/* 374 */     public static final Attribute VLINK = new Attribute("vlink");
/* 375 */     public static final Attribute ALINK = new Attribute("alink");
/* 376 */     public static final Attribute WIDTH = new Attribute("width");
/* 377 */     public static final Attribute HEIGHT = new Attribute("height");
/* 378 */     public static final Attribute ALIGN = new Attribute("align");
/* 379 */     public static final Attribute NAME = new Attribute("name");
/* 380 */     public static final Attribute HREF = new Attribute("href");
/* 381 */     public static final Attribute REL = new Attribute("rel");
/* 382 */     public static final Attribute REV = new Attribute("rev");
/* 383 */     public static final Attribute TITLE = new Attribute("title");
/* 384 */     public static final Attribute TARGET = new Attribute("target");
/* 385 */     public static final Attribute SHAPE = new Attribute("shape");
/* 386 */     public static final Attribute COORDS = new Attribute("coords");
/* 387 */     public static final Attribute ISMAP = new Attribute("ismap");
/* 388 */     public static final Attribute NOHREF = new Attribute("nohref");
/* 389 */     public static final Attribute ALT = new Attribute("alt");
/* 390 */     public static final Attribute ID = new Attribute("id");
/* 391 */     public static final Attribute SRC = new Attribute("src");
/* 392 */     public static final Attribute HSPACE = new Attribute("hspace");
/* 393 */     public static final Attribute VSPACE = new Attribute("vspace");
/* 394 */     public static final Attribute USEMAP = new Attribute("usemap");
/* 395 */     public static final Attribute LOWSRC = new Attribute("lowsrc");
/* 396 */     public static final Attribute CODEBASE = new Attribute("codebase");
/* 397 */     public static final Attribute CODE = new Attribute("code");
/* 398 */     public static final Attribute ARCHIVE = new Attribute("archive");
/* 399 */     public static final Attribute VALUE = new Attribute("value");
/* 400 */     public static final Attribute VALUETYPE = new Attribute("valuetype");
/* 401 */     public static final Attribute TYPE = new Attribute("type");
/* 402 */     public static final Attribute CLASS = new Attribute("class");
/* 403 */     public static final Attribute STYLE = new Attribute("style");
/* 404 */     public static final Attribute LANG = new Attribute("lang");
/* 405 */     public static final Attribute FACE = new Attribute("face");
/* 406 */     public static final Attribute DIR = new Attribute("dir");
/* 407 */     public static final Attribute DECLARE = new Attribute("declare");
/* 408 */     public static final Attribute CLASSID = new Attribute("classid");
/* 409 */     public static final Attribute DATA = new Attribute("data");
/* 410 */     public static final Attribute CODETYPE = new Attribute("codetype");
/* 411 */     public static final Attribute STANDBY = new Attribute("standby");
/* 412 */     public static final Attribute BORDER = new Attribute("border");
/* 413 */     public static final Attribute SHAPES = new Attribute("shapes");
/* 414 */     public static final Attribute NOSHADE = new Attribute("noshade");
/* 415 */     public static final Attribute COMPACT = new Attribute("compact");
/* 416 */     public static final Attribute START = new Attribute("start");
/* 417 */     public static final Attribute ACTION = new Attribute("action");
/* 418 */     public static final Attribute METHOD = new Attribute("method");
/* 419 */     public static final Attribute ENCTYPE = new Attribute("enctype");
/* 420 */     public static final Attribute CHECKED = new Attribute("checked");
/* 421 */     public static final Attribute MAXLENGTH = new Attribute("maxlength");
/* 422 */     public static final Attribute MULTIPLE = new Attribute("multiple");
/* 423 */     public static final Attribute SELECTED = new Attribute("selected");
/* 424 */     public static final Attribute ROWS = new Attribute("rows");
/* 425 */     public static final Attribute COLS = new Attribute("cols");
/* 426 */     public static final Attribute DUMMY = new Attribute("dummy");
/* 427 */     public static final Attribute CELLSPACING = new Attribute("cellspacing");
/* 428 */     public static final Attribute CELLPADDING = new Attribute("cellpadding");
/* 429 */     public static final Attribute VALIGN = new Attribute("valign");
/* 430 */     public static final Attribute HALIGN = new Attribute("halign");
/* 431 */     public static final Attribute NOWRAP = new Attribute("nowrap");
/* 432 */     public static final Attribute ROWSPAN = new Attribute("rowspan");
/* 433 */     public static final Attribute COLSPAN = new Attribute("colspan");
/* 434 */     public static final Attribute PROMPT = new Attribute("prompt");
/* 435 */     public static final Attribute HTTPEQUIV = new Attribute("http-equiv");
/* 436 */     public static final Attribute CONTENT = new Attribute("content");
/* 437 */     public static final Attribute LANGUAGE = new Attribute("language");
/* 438 */     public static final Attribute VERSION = new Attribute("version");
/* 439 */     public static final Attribute N = new Attribute("n");
/* 440 */     public static final Attribute FRAMEBORDER = new Attribute("frameborder");
/* 441 */     public static final Attribute MARGINWIDTH = new Attribute("marginwidth");
/* 442 */     public static final Attribute MARGINHEIGHT = new Attribute("marginheight");
/* 443 */     public static final Attribute SCROLLING = new Attribute("scrolling");
/* 444 */     public static final Attribute NORESIZE = new Attribute("noresize");
/* 445 */     public static final Attribute ENDTAG = new Attribute("endtag");
/* 446 */     public static final Attribute COMMENT = new Attribute("comment");
/* 447 */     static final Attribute MEDIA = new Attribute("media");
/*     */ 
/* 449 */     static final Attribute[] allAttributes = { FACE, COMMENT, SIZE, COLOR, CLEAR, BACKGROUND, BGCOLOR, TEXT, LINK, VLINK, ALINK, WIDTH, HEIGHT, ALIGN, NAME, HREF, REL, REV, TITLE, TARGET, SHAPE, COORDS, ISMAP, NOHREF, ALT, ID, SRC, HSPACE, VSPACE, USEMAP, LOWSRC, CODEBASE, CODE, ARCHIVE, VALUE, VALUETYPE, TYPE, CLASS, STYLE, LANG, DIR, DECLARE, CLASSID, DATA, CODETYPE, STANDBY, BORDER, SHAPES, NOSHADE, COMPACT, START, ACTION, METHOD, ENCTYPE, CHECKED, MAXLENGTH, MULTIPLE, SELECTED, ROWS, COLS, DUMMY, CELLSPACING, CELLPADDING, VALIGN, HALIGN, NOWRAP, ROWSPAN, COLSPAN, PROMPT, HTTPEQUIV, CONTENT, LANGUAGE, VERSION, N, FRAMEBORDER, MARGINWIDTH, MARGINHEIGHT, SCROLLING, NORESIZE, MEDIA, ENDTAG };
/*     */ 
/*     */     Attribute(String paramString)
/*     */     {
/* 354 */       this.name = paramString;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 362 */       return this.name;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Tag
/*     */   {
/*     */     boolean blockTag;
/*     */     boolean breakTag;
/*     */     String name;
/*     */     boolean unknown;
/* 159 */     public static final Tag A = new Tag("a");
/* 160 */     public static final Tag ADDRESS = new Tag("address");
/* 161 */     public static final Tag APPLET = new Tag("applet");
/* 162 */     public static final Tag AREA = new Tag("area");
/* 163 */     public static final Tag B = new Tag("b");
/* 164 */     public static final Tag BASE = new Tag("base");
/* 165 */     public static final Tag BASEFONT = new Tag("basefont");
/* 166 */     public static final Tag BIG = new Tag("big");
/* 167 */     public static final Tag BLOCKQUOTE = new Tag("blockquote", true, true);
/* 168 */     public static final Tag BODY = new Tag("body", true, true);
/* 169 */     public static final Tag BR = new Tag("br", true, false);
/* 170 */     public static final Tag CAPTION = new Tag("caption");
/* 171 */     public static final Tag CENTER = new Tag("center", true, false);
/* 172 */     public static final Tag CITE = new Tag("cite");
/* 173 */     public static final Tag CODE = new Tag("code");
/* 174 */     public static final Tag DD = new Tag("dd", true, true);
/* 175 */     public static final Tag DFN = new Tag("dfn");
/* 176 */     public static final Tag DIR = new Tag("dir", true, true);
/* 177 */     public static final Tag DIV = new Tag("div", true, true);
/* 178 */     public static final Tag DL = new Tag("dl", true, true);
/* 179 */     public static final Tag DT = new Tag("dt", true, true);
/* 180 */     public static final Tag EM = new Tag("em");
/* 181 */     public static final Tag FONT = new Tag("font");
/* 182 */     public static final Tag FORM = new Tag("form", true, false);
/* 183 */     public static final Tag FRAME = new Tag("frame");
/* 184 */     public static final Tag FRAMESET = new Tag("frameset");
/* 185 */     public static final Tag H1 = new Tag("h1", true, true);
/* 186 */     public static final Tag H2 = new Tag("h2", true, true);
/* 187 */     public static final Tag H3 = new Tag("h3", true, true);
/* 188 */     public static final Tag H4 = new Tag("h4", true, true);
/* 189 */     public static final Tag H5 = new Tag("h5", true, true);
/* 190 */     public static final Tag H6 = new Tag("h6", true, true);
/* 191 */     public static final Tag HEAD = new Tag("head", true, true);
/* 192 */     public static final Tag HR = new Tag("hr", true, false);
/* 193 */     public static final Tag HTML = new Tag("html", true, false);
/* 194 */     public static final Tag I = new Tag("i");
/* 195 */     public static final Tag IMG = new Tag("img");
/* 196 */     public static final Tag INPUT = new Tag("input");
/* 197 */     public static final Tag ISINDEX = new Tag("isindex", true, false);
/* 198 */     public static final Tag KBD = new Tag("kbd");
/* 199 */     public static final Tag LI = new Tag("li", true, true);
/* 200 */     public static final Tag LINK = new Tag("link");
/* 201 */     public static final Tag MAP = new Tag("map");
/* 202 */     public static final Tag MENU = new Tag("menu", true, true);
/* 203 */     public static final Tag META = new Tag("meta");
/* 204 */     static final Tag NOBR = new Tag("nobr");
/* 205 */     public static final Tag NOFRAMES = new Tag("noframes", true, true);
/* 206 */     public static final Tag OBJECT = new Tag("object");
/* 207 */     public static final Tag OL = new Tag("ol", true, true);
/* 208 */     public static final Tag OPTION = new Tag("option");
/* 209 */     public static final Tag P = new Tag("p", true, true);
/* 210 */     public static final Tag PARAM = new Tag("param");
/* 211 */     public static final Tag PRE = new Tag("pre", true, true);
/* 212 */     public static final Tag SAMP = new Tag("samp");
/* 213 */     public static final Tag SCRIPT = new Tag("script");
/* 214 */     public static final Tag SELECT = new Tag("select");
/* 215 */     public static final Tag SMALL = new Tag("small");
/* 216 */     public static final Tag SPAN = new Tag("span");
/* 217 */     public static final Tag STRIKE = new Tag("strike");
/* 218 */     public static final Tag S = new Tag("s");
/* 219 */     public static final Tag STRONG = new Tag("strong");
/* 220 */     public static final Tag STYLE = new Tag("style");
/* 221 */     public static final Tag SUB = new Tag("sub");
/* 222 */     public static final Tag SUP = new Tag("sup");
/* 223 */     public static final Tag TABLE = new Tag("table", false, true);
/* 224 */     public static final Tag TD = new Tag("td", true, true);
/* 225 */     public static final Tag TEXTAREA = new Tag("textarea");
/* 226 */     public static final Tag TH = new Tag("th", true, true);
/* 227 */     public static final Tag TITLE = new Tag("title", true, true);
/* 228 */     public static final Tag TR = new Tag("tr", false, true);
/* 229 */     public static final Tag TT = new Tag("tt");
/* 230 */     public static final Tag U = new Tag("u");
/* 231 */     public static final Tag UL = new Tag("ul", true, true);
/* 232 */     public static final Tag VAR = new Tag("var");
/*     */ 
/* 244 */     public static final Tag IMPLIED = new Tag("p-implied");
/*     */ 
/* 254 */     public static final Tag CONTENT = new Tag("content");
/*     */ 
/* 264 */     public static final Tag COMMENT = new Tag("comment");
/*     */ 
/* 266 */     static final Tag[] allTags = { A, ADDRESS, APPLET, AREA, B, BASE, BASEFONT, BIG, BLOCKQUOTE, BODY, BR, CAPTION, CENTER, CITE, CODE, DD, DFN, DIR, DIV, DL, DT, EM, FONT, FORM, FRAME, FRAMESET, H1, H2, H3, H4, H5, H6, HEAD, HR, HTML, I, IMG, INPUT, ISINDEX, KBD, LI, LINK, MAP, MENU, META, NOBR, NOFRAMES, OBJECT, OL, OPTION, P, PARAM, PRE, SAMP, SCRIPT, SELECT, SMALL, SPAN, STRIKE, S, STRONG, STYLE, SUB, SUP, TABLE, TD, TEXTAREA, TH, TITLE, TR, TT, U, UL, VAR };
/*     */ 
/*     */     public Tag()
/*     */     {
/*     */     }
/*     */ 
/*     */     protected Tag(String paramString)
/*     */     {
/*  63 */       this(paramString, false, false);
/*     */     }
/*     */ 
/*     */     protected Tag(String paramString, boolean paramBoolean1, boolean paramBoolean2)
/*     */     {
/*  78 */       this.name = paramString;
/*  79 */       this.breakTag = paramBoolean1;
/*  80 */       this.blockTag = paramBoolean2;
/*     */     }
/*     */ 
/*     */     public boolean isBlock()
/*     */     {
/*  92 */       return this.blockTag;
/*     */     }
/*     */ 
/*     */     public boolean breaksFlow()
/*     */     {
/* 105 */       return this.breakTag;
/*     */     }
/*     */ 
/*     */     public boolean isPreformatted()
/*     */     {
/* 117 */       return (this == PRE) || (this == TEXTAREA);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 127 */       return this.name;
/*     */     }
/*     */ 
/*     */     boolean isParagraph()
/*     */     {
/* 139 */       return (this == P) || (this == IMPLIED) || (this == DT) || (this == H1) || (this == H2) || (this == H3) || (this == H4) || (this == H5) || (this == H6);
/*     */     }
/*     */ 
/*     */     static
/*     */     {
/* 280 */       HTML.getTag("html");
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class UnknownTag extends HTML.Tag
/*     */     implements Serializable
/*     */   {
/*     */     public UnknownTag(String paramString)
/*     */     {
/* 294 */       super();
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 302 */       return toString().hashCode();
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 316 */       if ((paramObject instanceof UnknownTag)) {
/* 317 */         return toString().equals(paramObject.toString());
/*     */       }
/* 319 */       return false;
/*     */     }
/*     */ 
/*     */     private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*     */     {
/* 324 */       paramObjectOutputStream.defaultWriteObject();
/* 325 */       paramObjectOutputStream.writeBoolean(this.blockTag);
/* 326 */       paramObjectOutputStream.writeBoolean(this.breakTag);
/* 327 */       paramObjectOutputStream.writeBoolean(this.unknown);
/* 328 */       paramObjectOutputStream.writeObject(this.name);
/*     */     }
/*     */ 
/*     */     private void readObject(ObjectInputStream paramObjectInputStream) throws ClassNotFoundException, IOException
/*     */     {
/* 333 */       paramObjectInputStream.defaultReadObject();
/* 334 */       this.blockTag = paramObjectInputStream.readBoolean();
/* 335 */       this.breakTag = paramObjectInputStream.readBoolean();
/* 336 */       this.unknown = paramObjectInputStream.readBoolean();
/* 337 */       this.name = ((String)paramObjectInputStream.readObject());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.HTML
 * JD-Core Version:    0.6.2
 */