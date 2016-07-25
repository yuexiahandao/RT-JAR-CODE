/*      */ package com.sun.org.apache.xerces.internal.impl.xpath.regex;
/*      */ 
/*      */ import java.io.Serializable;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ 
/*      */ class Token
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = 8484976002585487481L;
/*      */   static final boolean COUNTTOKENS = true;
/*   38 */   static int tokens = 0;
/*      */   static final int CHAR = 0;
/*      */   static final int DOT = 11;
/*      */   static final int CONCAT = 1;
/*      */   static final int UNION = 2;
/*      */   static final int CLOSURE = 3;
/*      */   static final int RANGE = 4;
/*      */   static final int NRANGE = 5;
/*      */   static final int PAREN = 6;
/*      */   static final int EMPTY = 7;
/*      */   static final int ANCHOR = 8;
/*      */   static final int NONGREEDYCLOSURE = 9;
/*      */   static final int STRING = 10;
/*      */   static final int BACKREFERENCE = 12;
/*      */   static final int LOOKAHEAD = 20;
/*      */   static final int NEGATIVELOOKAHEAD = 21;
/*      */   static final int LOOKBEHIND = 22;
/*      */   static final int NEGATIVELOOKBEHIND = 23;
/*      */   static final int INDEPENDENT = 24;
/*      */   static final int MODIFIERGROUP = 25;
/*      */   static final int CONDITION = 26;
/*      */   static final int UTF16_MAX = 1114111;
/*      */   final int type;
/*      */   static Token token_dot;
/*      */   static Token token_0to9;
/*      */   static Token token_wordchars;
/*  113 */   static Token token_not_0to9 = complementRanges(token_0to9);
/*  114 */   static Token token_not_wordchars = complementRanges(token_wordchars);
/*      */   static Token token_spaces;
/*  115 */   static Token token_not_spaces = complementRanges(token_spaces);
/*      */ 
/*   84 */   static Token token_empty = new Token(7);
/*      */ 
/*   86 */   static Token token_linebeginning = createAnchor(94);
/*   87 */   static Token token_linebeginning2 = createAnchor(64);
/*   88 */   static Token token_lineend = createAnchor(36);
/*   89 */   static Token token_stringbeginning = createAnchor(65);
/*   90 */   static Token token_stringend = createAnchor(122);
/*   91 */   static Token token_stringend2 = createAnchor(90);
/*   92 */   static Token token_wordedge = createAnchor(98);
/*   93 */   static Token token_not_wordedge = createAnchor(66);
/*   94 */   static Token token_wordbeginning = createAnchor(60);
/*   95 */   static Token token_wordend = createAnchor(62);
/*      */   static final int FC_CONTINUE = 0;
/*      */   static final int FC_TERMINAL = 1;
/*      */   static final int FC_ANY = 2;
/*  586 */   private static final Hashtable categories = new Hashtable();
/*  587 */   private static final Hashtable categories2 = new Hashtable();
/*  588 */   private static final String[] categoryNames = { "Cn", "Lu", "Ll", "Lt", "Lm", "Lo", "Mn", "Me", "Mc", "Nd", "Nl", "No", "Zs", "Zl", "Zp", "Cc", "Cf", null, "Co", "Cs", "Pd", "Ps", "Pe", "Pc", "Po", "Sm", "Sc", "Sk", "So", "Pi", "Pf", "L", "M", "N", "Z", "C", "P", "S" };
/*      */   static final int CHAR_INIT_QUOTE = 29;
/*      */   static final int CHAR_FINAL_QUOTE = 30;
/*      */   static final int CHAR_LETTER = 31;
/*      */   static final int CHAR_MARK = 32;
/*      */   static final int CHAR_NUMBER = 33;
/*      */   static final int CHAR_SEPARATOR = 34;
/*      */   static final int CHAR_OTHER = 35;
/*      */   static final int CHAR_PUNCTUATION = 36;
/*      */   static final int CHAR_SYMBOL = 37;
/*  608 */   private static final String[] blockNames = { "Basic Latin", "Latin-1 Supplement", "Latin Extended-A", "Latin Extended-B", "IPA Extensions", "Spacing Modifier Letters", "Combining Diacritical Marks", "Greek", "Cyrillic", "Armenian", "Hebrew", "Arabic", "Syriac", "Thaana", "Devanagari", "Bengali", "Gurmukhi", "Gujarati", "Oriya", "Tamil", "Telugu", "Kannada", "Malayalam", "Sinhala", "Thai", "Lao", "Tibetan", "Myanmar", "Georgian", "Hangul Jamo", "Ethiopic", "Cherokee", "Unified Canadian Aboriginal Syllabics", "Ogham", "Runic", "Khmer", "Mongolian", "Latin Extended Additional", "Greek Extended", "General Punctuation", "Superscripts and Subscripts", "Currency Symbols", "Combining Marks for Symbols", "Letterlike Symbols", "Number Forms", "Arrows", "Mathematical Operators", "Miscellaneous Technical", "Control Pictures", "Optical Character Recognition", "Enclosed Alphanumerics", "Box Drawing", "Block Elements", "Geometric Shapes", "Miscellaneous Symbols", "Dingbats", "Braille Patterns", "CJK Radicals Supplement", "Kangxi Radicals", "Ideographic Description Characters", "CJK Symbols and Punctuation", "Hiragana", "Katakana", "Bopomofo", "Hangul Compatibility Jamo", "Kanbun", "Bopomofo Extended", "Enclosed CJK Letters and Months", "CJK Compatibility", "CJK Unified Ideographs Extension A", "CJK Unified Ideographs", "Yi Syllables", "Yi Radicals", "Hangul Syllables", "Private Use", "CJK Compatibility Ideographs", "Alphabetic Presentation Forms", "Arabic Presentation Forms-A", "Combining Half Marks", "CJK Compatibility Forms", "Small Form Variants", "Arabic Presentation Forms-B", "Specials", "Halfwidth and Fullwidth Forms", "Old Italic", "Gothic", "Deseret", "Byzantine Musical Symbols", "Musical Symbols", "Mathematical Alphanumeric Symbols", "CJK Unified Ideographs Extension B", "CJK Compatibility Ideographs Supplement", "Tags" };
/*      */   static final String blockRanges = "";
/*  722 */   static final int[] nonBMPBlockRanges = { 66304, 66351, 66352, 66383, 66560, 66639, 118784, 119039, 119040, 119295, 119808, 120831, 131072, 173782, 194560, 195103, 917504, 917631 };
/*      */   private static final int NONBMP_BLOCK_START = 84;
/*  975 */   static Hashtable nonxs = null;
/*      */   static final String viramaString = "्্੍્୍்్್്ฺ྄";
/* 1020 */   private static Token token_grapheme = null;
/*      */ 
/* 1059 */   private static Token token_ccs = null;
/*      */ 
/*      */   static ParenToken createLook(int type, Token child)
/*      */   {
/*  119 */     tokens += 1;
/*  120 */     return new ParenToken(type, child, 0);
/*      */   }
/*      */   static ParenToken createParen(Token child, int pnumber) {
/*  123 */     tokens += 1;
/*  124 */     return new ParenToken(6, child, pnumber);
/*      */   }
/*      */   static ClosureToken createClosure(Token tok) {
/*  127 */     tokens += 1;
/*  128 */     return new ClosureToken(3, tok);
/*      */   }
/*      */   static ClosureToken createNGClosure(Token tok) {
/*  131 */     tokens += 1;
/*  132 */     return new ClosureToken(9, tok);
/*      */   }
/*      */   static ConcatToken createConcat(Token tok1, Token tok2) {
/*  135 */     tokens += 1;
/*  136 */     return new ConcatToken(tok1, tok2);
/*      */   }
/*      */   static UnionToken createConcat() {
/*  139 */     tokens += 1;
/*  140 */     return new UnionToken(1);
/*      */   }
/*      */   static UnionToken createUnion() {
/*  143 */     tokens += 1;
/*  144 */     return new UnionToken(2);
/*      */   }
/*      */   static Token createEmpty() {
/*  147 */     return token_empty;
/*      */   }
/*      */   static RangeToken createRange() {
/*  150 */     tokens += 1;
/*  151 */     return new RangeToken(4);
/*      */   }
/*      */   static RangeToken createNRange() {
/*  154 */     tokens += 1;
/*  155 */     return new RangeToken(5);
/*      */   }
/*      */   static CharToken createChar(int ch) {
/*  158 */     tokens += 1;
/*  159 */     return new CharToken(0, ch);
/*      */   }
/*      */   private static CharToken createAnchor(int ch) {
/*  162 */     tokens += 1;
/*  163 */     return new CharToken(8, ch);
/*      */   }
/*      */   static StringToken createBackReference(int refno) {
/*  166 */     tokens += 1;
/*  167 */     return new StringToken(12, null, refno);
/*      */   }
/*      */   static StringToken createString(String str) {
/*  170 */     tokens += 1;
/*  171 */     return new StringToken(10, str, 0);
/*      */   }
/*      */   static ModifierToken createModifierGroup(Token child, int add, int mask) {
/*  174 */     tokens += 1;
/*  175 */     return new ModifierToken(child, add, mask);
/*      */   }
/*      */ 
/*      */   static ConditionToken createCondition(int refno, Token condition, Token yespat, Token nopat) {
/*  179 */     tokens += 1;
/*  180 */     return new ConditionToken(refno, condition, yespat, nopat);
/*      */   }
/*      */ 
/*      */   protected Token(int type) {
/*  184 */     this.type = type;
/*      */   }
/*      */ 
/*      */   int size()
/*      */   {
/*  191 */     return 0;
/*      */   }
/*      */   Token getChild(int index) {
/*  194 */     return null;
/*      */   }
/*      */   void addChild(Token tok) {
/*  197 */     throw new RuntimeException("Not supported.");
/*      */   }
/*      */ 
/*      */   protected void addRange(int start, int end)
/*      */   {
/*  202 */     throw new RuntimeException("Not supported.");
/*      */   }
/*      */   protected void sortRanges() {
/*  205 */     throw new RuntimeException("Not supported.");
/*      */   }
/*      */   protected void compactRanges() {
/*  208 */     throw new RuntimeException("Not supported.");
/*      */   }
/*      */   protected void mergeRanges(Token tok) {
/*  211 */     throw new RuntimeException("Not supported.");
/*      */   }
/*      */   protected void subtractRanges(Token tok) {
/*  214 */     throw new RuntimeException("Not supported.");
/*      */   }
/*      */   protected void intersectRanges(Token tok) {
/*  217 */     throw new RuntimeException("Not supported.");
/*      */   }
/*      */   static Token complementRanges(Token tok) {
/*  220 */     return RangeToken.complementRanges(tok);
/*      */   }
/*      */   void setMin(int min) {
/*      */   }
/*      */ 
/*      */   void setMax(int max) {
/*      */   }
/*      */ 
/*      */   int getMin() {
/*  229 */     return -1;
/*      */   }
/*      */   int getMax() {
/*  232 */     return -1;
/*      */   }
/*      */   int getReferenceNumber() {
/*  235 */     return 0;
/*      */   }
/*      */   String getString() {
/*  238 */     return null;
/*      */   }
/*      */ 
/*      */   int getParenNumber() {
/*  242 */     return 0;
/*      */   }
/*      */   int getChar() {
/*  245 */     return -1;
/*      */   }
/*      */ 
/*      */   public String toString() {
/*  249 */     return toString(0);
/*      */   }
/*      */   public String toString(int options) {
/*  252 */     return this.type == 11 ? "." : "";
/*      */   }
/*      */ 
/*      */   final int getMinLength()
/*      */   {
/*  259 */     switch (this.type) {
/*      */     case 1:
/*  261 */       int sum = 0;
/*  262 */       for (int i = 0; i < size(); i++)
/*  263 */         sum += getChild(i).getMinLength();
/*  264 */       return sum;
/*      */     case 2:
/*      */     case 26:
/*  268 */       if (size() == 0)
/*  269 */         return 0;
/*  270 */       int ret = getChild(0).getMinLength();
/*  271 */       for (int i = 1; i < size(); i++) {
/*  272 */         int min = getChild(i).getMinLength();
/*  273 */         if (min < ret) ret = min;
/*      */       }
/*  275 */       return ret;
/*      */     case 3:
/*      */     case 9:
/*  279 */       if (getMin() >= 0)
/*  280 */         return getMin() * getChild(0).getMinLength();
/*  281 */       return 0;
/*      */     case 7:
/*      */     case 8:
/*  285 */       return 0;
/*      */     case 0:
/*      */     case 4:
/*      */     case 5:
/*      */     case 11:
/*  291 */       return 1;
/*      */     case 6:
/*      */     case 24:
/*      */     case 25:
/*  296 */       return getChild(0).getMinLength();
/*      */     case 12:
/*  299 */       return 0;
/*      */     case 10:
/*  302 */       return getString().length();
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*      */     case 23:
/*  308 */       return 0;
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*  311 */     case 19: } throw new RuntimeException("Token#getMinLength(): Invalid Type: " + this.type);
/*      */   }
/*      */ 
/*      */   final int getMaxLength()
/*      */   {
/*  316 */     switch (this.type) {
/*      */     case 1:
/*  318 */       int sum = 0;
/*  319 */       for (int i = 0; i < size(); i++) {
/*  320 */         int d = getChild(i).getMaxLength();
/*  321 */         if (d < 0) return -1;
/*  322 */         sum += d;
/*      */       }
/*  324 */       return sum;
/*      */     case 2:
/*      */     case 26:
/*  328 */       if (size() == 0)
/*  329 */         return 0;
/*  330 */       int ret = getChild(0).getMaxLength();
/*  331 */       for (int i = 1; (ret >= 0) && (i < size()); i++) {
/*  332 */         int max = getChild(i).getMaxLength();
/*  333 */         if (max < 0) {
/*  334 */           ret = -1;
/*  335 */           break;
/*      */         }
/*  337 */         if (max > ret) ret = max;
/*      */       }
/*  339 */       return ret;
/*      */     case 3:
/*      */     case 9:
/*  343 */       if (getMax() >= 0)
/*      */       {
/*  346 */         return getMax() * getChild(0).getMaxLength();
/*  347 */       }return -1;
/*      */     case 7:
/*      */     case 8:
/*  351 */       return 0;
/*      */     case 0:
/*  354 */       return 1;
/*      */     case 4:
/*      */     case 5:
/*      */     case 11:
/*  358 */       return 2;
/*      */     case 6:
/*      */     case 24:
/*      */     case 25:
/*  363 */       return getChild(0).getMaxLength();
/*      */     case 12:
/*  366 */       return -1;
/*      */     case 10:
/*  369 */       return getString().length();
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*      */     case 23:
/*  375 */       return 0;
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*  378 */     case 19: } throw new RuntimeException("Token#getMaxLength(): Invalid Type: " + this.type);
/*      */   }
/*      */ 
/*      */   private static final boolean isSet(int options, int flag)
/*      */   {
/*  386 */     return (options & flag) == flag;
/*      */   }
/*      */   final int analyzeFirstCharacter(RangeToken result, int options) {
/*  389 */     switch (this.type) {
/*      */     case 1:
/*  391 */       int ret = 0;
/*  392 */       for (int i = 0; (i < size()) && 
/*  393 */         ((ret = getChild(i).analyzeFirstCharacter(result, options)) == 0); i++);
/*  395 */       return ret;
/*      */     case 2:
/*  398 */       if (size() == 0) {
/*  399 */         return 0;
/*      */       }
/*      */ 
/*  405 */       int ret2 = 0;
/*  406 */       boolean hasEmpty = false;
/*  407 */       for (int i = 0; i < size(); i++) {
/*  408 */         ret2 = getChild(i).analyzeFirstCharacter(result, options);
/*  409 */         if (ret2 == 2)
/*      */           break;
/*  411 */         if (ret2 == 0)
/*  412 */           hasEmpty = true;
/*      */       }
/*  414 */       return hasEmpty ? 0 : ret2;
/*      */     case 26:
/*  417 */       int ret3 = getChild(0).analyzeFirstCharacter(result, options);
/*  418 */       if (size() == 1) return 0;
/*  419 */       if (ret3 == 2) return ret3;
/*  420 */       int ret4 = getChild(1).analyzeFirstCharacter(result, options);
/*  421 */       if (ret4 == 2) return ret4;
/*  422 */       return (ret3 == 0) || (ret4 == 0) ? 0 : 1;
/*      */     case 3:
/*      */     case 9:
/*  426 */       getChild(0).analyzeFirstCharacter(result, options);
/*  427 */       return 0;
/*      */     case 7:
/*      */     case 8:
/*  431 */       return 0;
/*      */     case 0:
/*  434 */       int ch = getChar();
/*  435 */       result.addRange(ch, ch);
/*  436 */       if ((ch < 65536) && (isSet(options, 2))) {
/*  437 */         ch = Character.toUpperCase((char)ch);
/*  438 */         result.addRange(ch, ch);
/*  439 */         ch = Character.toLowerCase((char)ch);
/*  440 */         result.addRange(ch, ch);
/*      */       }
/*  442 */       return 1;
/*      */     case 11:
/*  445 */       return 2;
/*      */     case 4:
/*  448 */       result.mergeRanges(this);
/*  449 */       return 1;
/*      */     case 5:
/*  452 */       result.mergeRanges(complementRanges(this));
/*  453 */       return 1;
/*      */     case 6:
/*      */     case 24:
/*  457 */       return getChild(0).analyzeFirstCharacter(result, options);
/*      */     case 25:
/*  460 */       options |= ((ModifierToken)this).getOptions();
/*  461 */       options &= (((ModifierToken)this).getOptionsMask() ^ 0xFFFFFFFF);
/*  462 */       return getChild(0).analyzeFirstCharacter(result, options);
/*      */     case 12:
/*  465 */       result.addRange(0, 1114111);
/*  466 */       return 2;
/*      */     case 10:
/*  469 */       int cha = getString().charAt(0);
/*      */       int ch2;
/*  471 */       if ((REUtil.isHighSurrogate(cha)) && (getString().length() >= 2) && (REUtil.isLowSurrogate(ch2 = getString().charAt(1))))
/*      */       {
/*  474 */         cha = REUtil.composeFromSurrogates(cha, ch2);
/*  475 */       }result.addRange(cha, cha);
/*  476 */       if ((cha < 65536) && (isSet(options, 2))) {
/*  477 */         cha = Character.toUpperCase((char)cha);
/*  478 */         result.addRange(cha, cha);
/*  479 */         cha = Character.toLowerCase((char)cha);
/*  480 */         result.addRange(cha, cha);
/*      */       }
/*  482 */       return 1;
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*      */     case 23:
/*  488 */       return 0;
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*  491 */     case 19: } throw new RuntimeException("Token#analyzeHeadCharacter(): Invalid Type: " + this.type);
/*      */   }
/*      */ 
/*      */   private final boolean isShorterThan(Token tok)
/*      */   {
/*  496 */     if (tok == null) return false;
/*  508 */     int mylength;
/*  508 */     if (this.type == 10) mylength = getString().length(); else
/*  509 */       throw new RuntimeException("Internal Error: Illegal type: " + this.type);
/*  511 */     int mylength;
/*      */     int otherlength;
/*  511 */     if (tok.type == 10) otherlength = tok.getString().length(); else
/*  512 */       throw new RuntimeException("Internal Error: Illegal type: " + tok.type);
/*      */     int otherlength;
/*  513 */     return mylength < otherlength;
/*      */   }
/*      */ 
/*      */   final void findFixedString(FixedStringContainer container, int options)
/*      */   {
/*  524 */     switch (this.type) {
/*      */     case 1:
/*  526 */       Token prevToken = null;
/*  527 */       int prevOptions = 0;
/*  528 */       for (int i = 0; i < size(); i++) {
/*  529 */         getChild(i).findFixedString(container, options);
/*  530 */         if ((prevToken == null) || (prevToken.isShorterThan(container.token))) {
/*  531 */           prevToken = container.token;
/*  532 */           prevOptions = container.options;
/*      */         }
/*      */       }
/*  535 */       container.token = prevToken;
/*  536 */       container.options = prevOptions;
/*  537 */       return;
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 11:
/*      */     case 12:
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*      */     case 23:
/*      */     case 26:
/*  553 */       container.token = null;
/*  554 */       return;
/*      */     case 0:
/*  557 */       container.token = null;
/*  558 */       return;
/*      */     case 10:
/*  561 */       container.token = this;
/*  562 */       container.options = options;
/*  563 */       return;
/*      */     case 6:
/*      */     case 24:
/*  567 */       getChild(0).findFixedString(container, options);
/*  568 */       return;
/*      */     case 25:
/*  571 */       options |= ((ModifierToken)this).getOptions();
/*  572 */       options &= (((ModifierToken)this).getOptionsMask() ^ 0xFFFFFFFF);
/*  573 */       getChild(0).findFixedString(container, options);
/*  574 */       return;
/*      */     case 13:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 18:
/*  577 */     case 19: } throw new RuntimeException("Token#findFixedString(): Invalid Type: " + this.type);
/*      */   }
/*      */ 
/*      */   boolean match(int ch)
/*      */   {
/*  582 */     throw new RuntimeException("NFAArrow#match(): Internal error: " + this.type);
/*      */   }
/*      */ 
/*      */   protected static RangeToken getRange(String name, boolean positive)
/*      */   {
/*  736 */     if (categories.size() == 0) {
/*  737 */       synchronized (categories) {
/*  738 */         Token[] ranges = new Token[categoryNames.length];
/*  739 */         for (int i = 0; i < ranges.length; i++) {
/*  740 */           ranges[i] = createRange();
/*      */         }
/*      */ 
/*  743 */         for (int i = 0; i < 65536; i++) {
/*  744 */           int type = Character.getType((char)i);
/*  745 */           if ((type == 21) || (type == 22))
/*      */           {
/*  748 */             if ((i == 171) || (i == 8216) || (i == 8219) || (i == 8220) || (i == 8223) || (i == 8249))
/*      */             {
/*  750 */               type = 29;
/*      */             }
/*      */ 
/*  753 */             if ((i == 187) || (i == 8217) || (i == 8221) || (i == 8250)) {
/*  754 */               type = 30;
/*      */             }
/*      */           }
/*  757 */           ranges[type].addRange(i, i);
/*  758 */           switch (type) {
/*      */           case 1:
/*      */           case 2:
/*      */           case 3:
/*      */           case 4:
/*      */           case 5:
/*  764 */             type = 31;
/*  765 */             break;
/*      */           case 6:
/*      */           case 7:
/*      */           case 8:
/*  769 */             type = 32;
/*  770 */             break;
/*      */           case 9:
/*      */           case 10:
/*      */           case 11:
/*  774 */             type = 33;
/*  775 */             break;
/*      */           case 12:
/*      */           case 13:
/*      */           case 14:
/*  779 */             type = 34;
/*  780 */             break;
/*      */           case 0:
/*      */           case 15:
/*      */           case 16:
/*      */           case 18:
/*      */           case 19:
/*  786 */             type = 35;
/*  787 */             break;
/*      */           case 20:
/*      */           case 21:
/*      */           case 22:
/*      */           case 23:
/*      */           case 24:
/*      */           case 29:
/*      */           case 30:
/*  795 */             type = 36;
/*  796 */             break;
/*      */           case 25:
/*      */           case 26:
/*      */           case 27:
/*      */           case 28:
/*  801 */             type = 37;
/*  802 */             break;
/*      */           case 17:
/*      */           default:
/*  804 */             throw new RuntimeException("org.apache.xerces.utils.regex.Token#getRange(): Unknown Unicode category: " + type);
/*      */           }
/*  806 */           ranges[type].addRange(i, i);
/*      */         }
/*  808 */         ranges[0].addRange(65536, 1114111);
/*      */ 
/*  810 */         for (int i = 0; i < ranges.length; i++) {
/*  811 */           if (categoryNames[i] != null) {
/*  812 */             if (i == 0) {
/*  813 */               ranges[i].addRange(65536, 1114111);
/*      */             }
/*  815 */             categories.put(categoryNames[i], ranges[i]);
/*  816 */             categories2.put(categoryNames[i], complementRanges(ranges[i]));
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  823 */         StringBuffer buffer = new StringBuffer(50);
/*  824 */         for (int i = 0; i < blockNames.length; i++) {
/*  825 */           Token r1 = createRange();
/*      */ 
/*  827 */           if (i < 84) {
/*  828 */             int location = i * 2;
/*  829 */             int rstart = "".charAt(location);
/*  830 */             int rend = "".charAt(location + 1);
/*      */ 
/*  834 */             r1.addRange(rstart, rend);
/*      */           } else {
/*  836 */             int location = (i - 84) * 2;
/*  837 */             r1.addRange(nonBMPBlockRanges[location], nonBMPBlockRanges[(location + 1)]);
/*      */           }
/*      */ 
/*  840 */           String n = blockNames[i];
/*  841 */           if (n.equals("Specials"))
/*  842 */             r1.addRange(65520, 65533);
/*  843 */           if (n.equals("Private Use")) {
/*  844 */             r1.addRange(983040, 1048573);
/*  845 */             r1.addRange(1048576, 1114109);
/*      */           }
/*  847 */           categories.put(n, r1);
/*  848 */           categories2.put(n, complementRanges(r1));
/*  849 */           buffer.setLength(0);
/*  850 */           buffer.append("Is");
/*  851 */           if (n.indexOf(' ') >= 0) {
/*  852 */             for (int ci = 0; ci < n.length(); ci++)
/*  853 */               if (n.charAt(ci) != ' ') buffer.append(n.charAt(ci));
/*      */           }
/*      */           else {
/*  856 */             buffer.append(n);
/*      */           }
/*  858 */           setAlias(buffer.toString(), n, true);
/*      */         }
/*      */ 
/*  862 */         setAlias("ASSIGNED", "Cn", false);
/*  863 */         setAlias("UNASSIGNED", "Cn", true);
/*  864 */         Token all = createRange();
/*  865 */         all.addRange(0, 1114111);
/*  866 */         categories.put("ALL", all);
/*  867 */         categories2.put("ALL", complementRanges(all));
/*  868 */         registerNonXS("ASSIGNED");
/*  869 */         registerNonXS("UNASSIGNED");
/*  870 */         registerNonXS("ALL");
/*      */ 
/*  872 */         Token isalpha = createRange();
/*  873 */         isalpha.mergeRanges(ranges[1]);
/*  874 */         isalpha.mergeRanges(ranges[2]);
/*  875 */         isalpha.mergeRanges(ranges[5]);
/*  876 */         categories.put("IsAlpha", isalpha);
/*  877 */         categories2.put("IsAlpha", complementRanges(isalpha));
/*  878 */         registerNonXS("IsAlpha");
/*      */ 
/*  880 */         Token isalnum = createRange();
/*  881 */         isalnum.mergeRanges(isalpha);
/*  882 */         isalnum.mergeRanges(ranges[9]);
/*  883 */         categories.put("IsAlnum", isalnum);
/*  884 */         categories2.put("IsAlnum", complementRanges(isalnum));
/*  885 */         registerNonXS("IsAlnum");
/*      */ 
/*  887 */         Token isspace = createRange();
/*  888 */         isspace.mergeRanges(token_spaces);
/*  889 */         isspace.mergeRanges(ranges[34]);
/*  890 */         categories.put("IsSpace", isspace);
/*  891 */         categories2.put("IsSpace", complementRanges(isspace));
/*  892 */         registerNonXS("IsSpace");
/*      */ 
/*  894 */         Token isword = createRange();
/*  895 */         isword.mergeRanges(isalnum);
/*  896 */         isword.addRange(95, 95);
/*  897 */         categories.put("IsWord", isword);
/*  898 */         categories2.put("IsWord", complementRanges(isword));
/*  899 */         registerNonXS("IsWord");
/*      */ 
/*  901 */         Token isascii = createRange();
/*  902 */         isascii.addRange(0, 127);
/*  903 */         categories.put("IsASCII", isascii);
/*  904 */         categories2.put("IsASCII", complementRanges(isascii));
/*  905 */         registerNonXS("IsASCII");
/*      */ 
/*  907 */         Token isnotgraph = createRange();
/*  908 */         isnotgraph.mergeRanges(ranges[35]);
/*  909 */         isnotgraph.addRange(32, 32);
/*  910 */         categories.put("IsGraph", complementRanges(isnotgraph));
/*  911 */         categories2.put("IsGraph", isnotgraph);
/*  912 */         registerNonXS("IsGraph");
/*      */ 
/*  914 */         Token isxdigit = createRange();
/*  915 */         isxdigit.addRange(48, 57);
/*  916 */         isxdigit.addRange(65, 70);
/*  917 */         isxdigit.addRange(97, 102);
/*  918 */         categories.put("IsXDigit", complementRanges(isxdigit));
/*  919 */         categories2.put("IsXDigit", isxdigit);
/*  920 */         registerNonXS("IsXDigit");
/*      */ 
/*  922 */         setAlias("IsDigit", "Nd", true);
/*  923 */         setAlias("IsUpper", "Lu", true);
/*  924 */         setAlias("IsLower", "Ll", true);
/*  925 */         setAlias("IsCntrl", "C", true);
/*  926 */         setAlias("IsPrint", "C", false);
/*  927 */         setAlias("IsPunct", "P", true);
/*  928 */         registerNonXS("IsDigit");
/*  929 */         registerNonXS("IsUpper");
/*  930 */         registerNonXS("IsLower");
/*  931 */         registerNonXS("IsCntrl");
/*  932 */         registerNonXS("IsPrint");
/*  933 */         registerNonXS("IsPunct");
/*      */ 
/*  935 */         setAlias("alpha", "IsAlpha", true);
/*  936 */         setAlias("alnum", "IsAlnum", true);
/*  937 */         setAlias("ascii", "IsASCII", true);
/*  938 */         setAlias("cntrl", "IsCntrl", true);
/*  939 */         setAlias("digit", "IsDigit", true);
/*  940 */         setAlias("graph", "IsGraph", true);
/*  941 */         setAlias("lower", "IsLower", true);
/*  942 */         setAlias("print", "IsPrint", true);
/*  943 */         setAlias("punct", "IsPunct", true);
/*  944 */         setAlias("space", "IsSpace", true);
/*  945 */         setAlias("upper", "IsUpper", true);
/*  946 */         setAlias("word", "IsWord", true);
/*  947 */         setAlias("xdigit", "IsXDigit", true);
/*  948 */         registerNonXS("alpha");
/*  949 */         registerNonXS("alnum");
/*  950 */         registerNonXS("ascii");
/*  951 */         registerNonXS("cntrl");
/*  952 */         registerNonXS("digit");
/*  953 */         registerNonXS("graph");
/*  954 */         registerNonXS("lower");
/*  955 */         registerNonXS("print");
/*  956 */         registerNonXS("punct");
/*  957 */         registerNonXS("space");
/*  958 */         registerNonXS("upper");
/*  959 */         registerNonXS("word");
/*  960 */         registerNonXS("xdigit");
/*      */       }
/*      */     }
/*  963 */     RangeToken tok = positive ? (RangeToken)categories.get(name) : (RangeToken)categories2.get(name);
/*      */ 
/*  966 */     return tok;
/*      */   }
/*      */   protected static RangeToken getRange(String name, boolean positive, boolean xs) {
/*  969 */     RangeToken range = getRange(name, positive);
/*  970 */     if ((xs) && (range != null) && (isRegisterNonXS(name)))
/*  971 */       range = null;
/*  972 */     return range;
/*      */   }
/*      */ 
/*      */   protected static void registerNonXS(String name)
/*      */   {
/*  981 */     if (nonxs == null)
/*  982 */       nonxs = new Hashtable();
/*  983 */     nonxs.put(name, name);
/*      */   }
/*      */   protected static boolean isRegisterNonXS(String name) {
/*  986 */     if (nonxs == null) {
/*  987 */       return false;
/*      */     }
/*      */ 
/*  990 */     return nonxs.containsKey(name);
/*      */   }
/*      */ 
/*      */   private static void setAlias(String newName, String name, boolean positive) {
/*  994 */     Token t1 = (Token)categories.get(name);
/*  995 */     Token t2 = (Token)categories2.get(name);
/*  996 */     if (positive) {
/*  997 */       categories.put(newName, t1);
/*  998 */       categories2.put(newName, t2);
/*      */     } else {
/* 1000 */       categories2.put(newName, t1);
/* 1001 */       categories.put(newName, t2);
/*      */     }
/*      */   }
/*      */ 
/*      */   static synchronized Token getGraphemePattern()
/*      */   {
/* 1022 */     if (token_grapheme != null) {
/* 1023 */       return token_grapheme;
/*      */     }
/* 1025 */     Token base_char = createRange();
/* 1026 */     base_char.mergeRanges(getRange("ASSIGNED", true));
/* 1027 */     base_char.subtractRanges(getRange("M", true));
/* 1028 */     base_char.subtractRanges(getRange("C", true));
/*      */ 
/* 1030 */     Token virama = createRange();
/* 1031 */     for (int i = 0; i < "्্੍્୍்్್്ฺ྄".length(); i++) {
/* 1032 */       virama.addRange(i, i);
/*      */     }
/*      */ 
/* 1035 */     Token combiner_wo_virama = createRange();
/* 1036 */     combiner_wo_virama.mergeRanges(getRange("M", true));
/* 1037 */     combiner_wo_virama.addRange(4448, 4607);
/* 1038 */     combiner_wo_virama.addRange(65438, 65439);
/*      */ 
/* 1040 */     Token left = createUnion();
/* 1041 */     left.addChild(base_char);
/* 1042 */     left.addChild(token_empty);
/*      */ 
/* 1044 */     Token foo = createUnion();
/* 1045 */     foo.addChild(createConcat(virama, getRange("L", true)));
/* 1046 */     foo.addChild(combiner_wo_virama);
/*      */ 
/* 1048 */     foo = createClosure(foo);
/*      */ 
/* 1050 */     foo = createConcat(left, foo);
/*      */ 
/* 1052 */     token_grapheme = foo;
/* 1053 */     return token_grapheme;
/*      */   }
/*      */ 
/*      */   static synchronized Token getCombiningCharacterSequence()
/*      */   {
/* 1061 */     if (token_ccs != null) {
/* 1062 */       return token_ccs;
/*      */     }
/* 1064 */     Token foo = createClosure(getRange("M", true));
/* 1065 */     foo = createConcat(getRange("M", false), foo);
/* 1066 */     token_ccs = foo;
/* 1067 */     return token_ccs;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   97 */     token_dot = new Token(11);
/*      */ 
/*   99 */     token_0to9 = createRange();
/*  100 */     token_0to9.addRange(48, 57);
/*  101 */     token_wordchars = createRange();
/*  102 */     token_wordchars.addRange(48, 57);
/*  103 */     token_wordchars.addRange(65, 90);
/*  104 */     token_wordchars.addRange(95, 95);
/*  105 */     token_wordchars.addRange(97, 122);
/*  106 */     token_spaces = createRange();
/*  107 */     token_spaces.addRange(9, 9);
/*  108 */     token_spaces.addRange(10, 10);
/*  109 */     token_spaces.addRange(12, 12);
/*  110 */     token_spaces.addRange(13, 13);
/*  111 */     token_spaces.addRange(32, 32);
/*      */   }
/*      */ 
/*      */   static class CharToken extends Token
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -4394272816279496989L;
/*      */     final int chardata;
/*      */ 
/*      */     CharToken(int type, int ch)
/*      */     {
/* 1149 */       super();
/* 1150 */       this.chardata = ch;
/*      */     }
/*      */ 
/*      */     int getChar() {
/* 1154 */       return this.chardata;
/*      */     }
/*      */ 
/*      */     public String toString(int options)
/*      */     {
/*      */       String ret;
/*      */       String ret;
/* 1159 */       switch (this.type)
/*      */       {
/*      */       case 0:
/*      */         String ret;
/* 1161 */         switch (this.chardata) { case 40:
/*      */         case 41:
/*      */         case 42:
/*      */         case 43:
/*      */         case 46:
/*      */         case 63:
/*      */         case 91:
/*      */         case 92:
/*      */         case 123:
/*      */         case 124:
/* 1165 */           ret = "\\" + (char)this.chardata;
/* 1166 */           break;
/*      */         case 12:
/* 1167 */           ret = "\\f"; break;
/*      */         case 10:
/* 1168 */           ret = "\\n"; break;
/*      */         case 13:
/* 1169 */           ret = "\\r"; break;
/*      */         case 9:
/* 1170 */           ret = "\\t"; break;
/*      */         case 27:
/* 1171 */           ret = "\\e"; break;
/*      */         default:
/* 1174 */           if (this.chardata >= 65536) {
/* 1175 */             String pre = "0" + Integer.toHexString(this.chardata);
/* 1176 */             ret = "\\v" + pre.substring(pre.length() - 6, pre.length());
/*      */           } else {
/* 1178 */             ret = "" + (char)this.chardata;
/*      */           }break; }
/* 1180 */         break;
/*      */       case 8:
/* 1183 */         if ((this == Token.token_linebeginning) || (this == Token.token_lineend))
/* 1184 */           ret = "" + (char)this.chardata;
/*      */         else
/* 1186 */           ret = "\\" + (char)this.chardata;
/* 1187 */         break;
/*      */       default:
/* 1190 */         ret = null;
/*      */       }
/* 1192 */       return ret;
/*      */     }
/*      */ 
/*      */     boolean match(int ch) {
/* 1196 */       if (this.type == 0) {
/* 1197 */         return ch == this.chardata;
/*      */       }
/* 1199 */       throw new RuntimeException("NFAArrow#match(): Internal error: " + this.type);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ClosureToken extends Token implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 1308971930673997452L;
/*      */     int min;
/*      */     int max;
/*      */     final Token child;
/*      */ 
/*      */     ClosureToken(int type, Token tok)
/*      */     {
/* 1215 */       super();
/* 1216 */       this.child = tok;
/* 1217 */       setMin(-1);
/* 1218 */       setMax(-1);
/*      */     }
/*      */ 
/*      */     int size() {
/* 1222 */       return 1;
/*      */     }
/*      */     Token getChild(int index) {
/* 1225 */       return this.child;
/*      */     }
/*      */ 
/*      */     final void setMin(int min) {
/* 1229 */       this.min = min;
/*      */     }
/*      */     final void setMax(int max) {
/* 1232 */       this.max = max;
/*      */     }
/*      */     final int getMin() {
/* 1235 */       return this.min;
/*      */     }
/*      */     final int getMax() {
/* 1238 */       return this.max;
/*      */     }
/*      */ 
/*      */     public String toString(int options)
/*      */     {
/* 1243 */       if (this.type == 3)
/*      */       {
/*      */         String ret;
/* 1244 */         if ((getMin() < 0) && (getMax() < 0)) {
/* 1245 */           ret = this.child.toString(options) + "*";
/*      */         }
/*      */         else
/*      */         {
/*      */           String ret;
/* 1246 */           if (getMin() == getMax()) {
/* 1247 */             ret = this.child.toString(options) + "{" + getMin() + "}";
/*      */           }
/*      */           else
/*      */           {
/*      */             String ret;
/* 1248 */             if ((getMin() >= 0) && (getMax() >= 0)) {
/* 1249 */               ret = this.child.toString(options) + "{" + getMin() + "," + getMax() + "}";
/*      */             }
/*      */             else
/*      */             {
/*      */               String ret;
/* 1250 */               if ((getMin() >= 0) && (getMax() < 0))
/* 1251 */                 ret = this.child.toString(options) + "{" + getMin() + ",}";
/*      */               else
/* 1253 */                 throw new RuntimeException("Token#toString(): CLOSURE " + getMin() + ", " + getMax());
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*      */         String ret;
/* 1256 */         if ((getMin() < 0) && (getMax() < 0)) {
/* 1257 */           ret = this.child.toString(options) + "*?";
/*      */         }
/*      */         else
/*      */         {
/*      */           String ret;
/* 1258 */           if (getMin() == getMax()) {
/* 1259 */             ret = this.child.toString(options) + "{" + getMin() + "}?";
/*      */           }
/*      */           else
/*      */           {
/*      */             String ret;
/* 1260 */             if ((getMin() >= 0) && (getMax() >= 0)) {
/* 1261 */               ret = this.child.toString(options) + "{" + getMin() + "," + getMax() + "}?";
/*      */             }
/*      */             else
/*      */             {
/*      */               String ret;
/* 1262 */               if ((getMin() >= 0) && (getMax() < 0))
/* 1263 */                 ret = this.child.toString(options) + "{" + getMin() + ",}?";
/*      */               else
/* 1265 */                 throw new RuntimeException("Token#toString(): NONGREEDYCLOSURE " + getMin() + ", " + getMax());
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */       String ret;
/* 1268 */       return ret;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ConcatToken extends Token
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 8717321425541346381L;
/*      */     final Token child;
/*      */     final Token child2;
/*      */ 
/*      */     ConcatToken(Token t1, Token t2)
/*      */     {
/* 1115 */       super();
/* 1116 */       this.child = t1;
/* 1117 */       this.child2 = t2;
/*      */     }
/*      */ 
/*      */     int size() {
/* 1121 */       return 2;
/*      */     }
/*      */     Token getChild(int index) {
/* 1124 */       return index == 0 ? this.child : this.child2;
/*      */     }
/*      */ 
/*      */     public String toString(int options)
/*      */     {
/*      */       String ret;
/*      */       String ret;
/* 1129 */       if ((this.child2.type == 3) && (this.child2.getChild(0) == this.child)) {
/* 1130 */         ret = this.child.toString(options) + "+";
/*      */       }
/*      */       else
/*      */       {
/*      */         String ret;
/* 1131 */         if ((this.child2.type == 9) && (this.child2.getChild(0) == this.child))
/* 1132 */           ret = this.child.toString(options) + "+?";
/*      */         else
/* 1134 */           ret = this.child.toString(options) + this.child2.toString(options); 
/*      */       }
/* 1135 */       return ret;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ConditionToken extends Token
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 4353765277910594411L;
/*      */     final int refNumber;
/*      */     final Token condition;
/*      */     final Token yes;
/*      */     final Token no;
/*      */ 
/*      */     ConditionToken(int refno, Token cond, Token yespat, Token nopat)
/*      */     {
/* 1342 */       super();
/* 1343 */       this.refNumber = refno;
/* 1344 */       this.condition = cond;
/* 1345 */       this.yes = yespat;
/* 1346 */       this.no = nopat;
/*      */     }
/*      */     int size() {
/* 1349 */       return this.no == null ? 1 : 2;
/*      */     }
/*      */     Token getChild(int index) {
/* 1352 */       if (index == 0) return this.yes;
/* 1353 */       if (index == 1) return this.no;
/* 1354 */       throw new RuntimeException("Internal Error: " + index);
/*      */     }
/*      */ 
/*      */     public String toString(int options)
/*      */     {
/*      */       String ret;
/*      */       String ret;
/* 1359 */       if (this.refNumber > 0) {
/* 1360 */         ret = "(?(" + this.refNumber + ")";
/*      */       }
/*      */       else
/*      */       {
/*      */         String ret;
/* 1361 */         if (this.condition.type == 8)
/* 1362 */           ret = "(?(" + this.condition + ")";
/*      */         else {
/* 1364 */           ret = "(?" + this.condition;
/*      */         }
/*      */       }
/* 1367 */       if (this.no == null)
/* 1368 */         ret = ret + this.yes + ")";
/*      */       else {
/* 1370 */         ret = ret + this.yes + "|" + this.no + ")";
/*      */       }
/* 1372 */       return ret;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class FixedStringContainer
/*      */   {
/*  517 */     Token token = null;
/*  518 */     int options = 0;
/*      */   }
/*      */ 
/*      */   static class ModifierToken extends Token
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -9114536559696480356L;
/*      */     final Token child;
/*      */     final int add;
/*      */     final int mask;
/*      */ 
/*      */     ModifierToken(Token tok, int add, int mask)
/*      */     {
/* 1388 */       super();
/* 1389 */       this.child = tok;
/* 1390 */       this.add = add;
/* 1391 */       this.mask = mask;
/*      */     }
/*      */ 
/*      */     int size() {
/* 1395 */       return 1;
/*      */     }
/*      */     Token getChild(int index) {
/* 1398 */       return this.child;
/*      */     }
/*      */ 
/*      */     int getOptions() {
/* 1402 */       return this.add;
/*      */     }
/*      */     int getOptionsMask() {
/* 1405 */       return this.mask;
/*      */     }
/*      */ 
/*      */     public String toString(int options) {
/* 1409 */       return "(?" + (this.add == 0 ? "" : REUtil.createOptionString(this.add)) + (this.mask == 0 ? "" : REUtil.createOptionString(this.mask)) + ":" + this.child.toString(options) + ")";
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ParenToken extends Token
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -5938014719827987704L;
/*      */     final Token child;
/*      */     final int parennumber;
/*      */ 
/*      */     ParenToken(int type, Token tok, int paren)
/*      */     {
/* 1283 */       super();
/* 1284 */       this.child = tok;
/* 1285 */       this.parennumber = paren;
/*      */     }
/*      */ 
/*      */     int size() {
/* 1289 */       return 1;
/*      */     }
/*      */     Token getChild(int index) {
/* 1292 */       return this.child;
/*      */     }
/*      */ 
/*      */     int getParenNumber() {
/* 1296 */       return this.parennumber;
/*      */     }
/*      */ 
/*      */     public String toString(int options) {
/* 1300 */       String ret = null;
/* 1301 */       switch (this.type) {
/*      */       case 6:
/* 1303 */         if (this.parennumber == 0)
/* 1304 */           ret = "(?:" + this.child.toString(options) + ")";
/*      */         else {
/* 1306 */           ret = "(" + this.child.toString(options) + ")";
/*      */         }
/* 1308 */         break;
/*      */       case 20:
/* 1311 */         ret = "(?=" + this.child.toString(options) + ")";
/* 1312 */         break;
/*      */       case 21:
/* 1314 */         ret = "(?!" + this.child.toString(options) + ")";
/* 1315 */         break;
/*      */       case 22:
/* 1317 */         ret = "(?<=" + this.child.toString(options) + ")";
/* 1318 */         break;
/*      */       case 23:
/* 1320 */         ret = "(?<!" + this.child.toString(options) + ")";
/* 1321 */         break;
/*      */       case 24:
/* 1323 */         ret = "(?>" + this.child.toString(options) + ")";
/*      */       case 7:
/*      */       case 8:
/*      */       case 9:
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 17:
/*      */       case 18:
/* 1326 */       case 19: } return ret;
/*      */     }
/*      */   }
/*      */ 
/*      */   static class StringToken extends Token
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -4614366944218504172L;
/*      */     String string;
/*      */     final int refNumber;
/*      */ 
/*      */     StringToken(int type, String str, int n)
/*      */     {
/* 1084 */       super();
/* 1085 */       this.string = str;
/* 1086 */       this.refNumber = n;
/*      */     }
/*      */ 
/*      */     int getReferenceNumber() {
/* 1090 */       return this.refNumber;
/*      */     }
/*      */     String getString() {
/* 1093 */       return this.string;
/*      */     }
/*      */ 
/*      */     public String toString(int options) {
/* 1097 */       if (this.type == 12) {
/* 1098 */         return "\\" + this.refNumber;
/*      */       }
/* 1100 */       return REUtil.quoteMeta(this.string);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class UnionToken extends Token
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -2568843945989489861L;
/*      */     Vector children;
/*      */ 
/*      */     UnionToken(int type)
/*      */     {
/* 1429 */       super();
/*      */     }
/*      */ 
/*      */     void addChild(Token tok) {
/* 1433 */       if (tok == null) return;
/* 1434 */       if (this.children == null) this.children = new Vector();
/* 1435 */       if (this.type == 2) {
/* 1436 */         this.children.addElement(tok);
/* 1437 */         return;
/*      */       }
/*      */ 
/* 1440 */       if (tok.type == 1) {
/* 1441 */         for (int i = 0; i < tok.size(); i++)
/* 1442 */           addChild(tok.getChild(i));
/* 1443 */         return;
/*      */       }
/* 1445 */       int size = this.children.size();
/* 1446 */       if (size == 0) {
/* 1447 */         this.children.addElement(tok);
/* 1448 */         return;
/*      */       }
/* 1450 */       Token previous = (Token)this.children.elementAt(size - 1);
/* 1451 */       if (((previous.type != 0) && (previous.type != 10)) || ((tok.type != 0) && (tok.type != 10)))
/*      */       {
/* 1453 */         this.children.addElement(tok);
/* 1454 */         return;
/*      */       }
/*      */ 
/* 1460 */       int nextMaxLength = tok.type == 0 ? 2 : tok.getString().length();
/*      */       StringBuffer buffer;
/* 1461 */       if (previous.type == 0) {
/* 1462 */         StringBuffer buffer = new StringBuffer(2 + nextMaxLength);
/* 1463 */         int ch = previous.getChar();
/* 1464 */         if (ch >= 65536)
/* 1465 */           buffer.append(REUtil.decomposeToSurrogates(ch));
/*      */         else
/* 1467 */           buffer.append((char)ch);
/* 1468 */         previous = Token.createString(null);
/* 1469 */         this.children.setElementAt(previous, size - 1);
/*      */       } else {
/* 1471 */         buffer = new StringBuffer(previous.getString().length() + nextMaxLength);
/* 1472 */         buffer.append(previous.getString());
/*      */       }
/*      */ 
/* 1475 */       if (tok.type == 0) {
/* 1476 */         int ch = tok.getChar();
/* 1477 */         if (ch >= 65536)
/* 1478 */           buffer.append(REUtil.decomposeToSurrogates(ch));
/*      */         else
/* 1480 */           buffer.append((char)ch);
/*      */       } else {
/* 1482 */         buffer.append(tok.getString());
/*      */       }
/*      */ 
/* 1485 */       ((Token.StringToken)previous).string = new String(buffer);
/*      */     }
/*      */ 
/*      */     int size() {
/* 1489 */       return this.children == null ? 0 : this.children.size();
/*      */     }
/*      */     Token getChild(int index) {
/* 1492 */       return (Token)this.children.elementAt(index);
/*      */     }
/*      */ 
/*      */     public String toString(int options)
/*      */     {
/* 1497 */       if (this.type == 1)
/*      */       {
/*      */         String ret;
/*      */         String ret;
/* 1498 */         if (this.children.size() == 2) {
/* 1499 */           Token ch = getChild(0);
/* 1500 */           Token ch2 = getChild(1);
/*      */           String ret;
/* 1501 */           if ((ch2.type == 3) && (ch2.getChild(0) == ch)) {
/* 1502 */             ret = ch.toString(options) + "+";
/*      */           }
/*      */           else
/*      */           {
/*      */             String ret;
/* 1503 */             if ((ch2.type == 9) && (ch2.getChild(0) == ch))
/* 1504 */               ret = ch.toString(options) + "+?";
/*      */             else
/* 1506 */               ret = ch.toString(options) + ch2.toString(options); 
/*      */           }
/*      */         } else { StringBuffer sb = new StringBuffer();
/* 1509 */           for (int i = 0; i < this.children.size(); i++) {
/* 1510 */             sb.append(((Token)this.children.elementAt(i)).toString(options));
/*      */           }
/* 1512 */           ret = new String(sb);
/*      */         }
/* 1514 */         return ret;
/*      */       }
/*      */       String ret;
/*      */       String ret;
/* 1516 */       if ((this.children.size() == 2) && (getChild(1).type == 7)) {
/* 1517 */         ret = getChild(0).toString(options) + "?";
/*      */       }
/*      */       else
/*      */       {
/*      */         String ret;
/* 1518 */         if ((this.children.size() == 2) && (getChild(0).type == 7))
/*      */         {
/* 1520 */           ret = getChild(1).toString(options) + "??";
/*      */         } else {
/* 1522 */           StringBuffer sb = new StringBuffer();
/* 1523 */           sb.append(((Token)this.children.elementAt(0)).toString(options));
/* 1524 */           for (int i = 1; i < this.children.size(); i++) {
/* 1525 */             sb.append('|');
/* 1526 */             sb.append(((Token)this.children.elementAt(i)).toString(options));
/*      */           }
/* 1528 */           ret = new String(sb);
/*      */         }
/*      */       }
/* 1530 */       return ret;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xpath.regex.Token
 * JD-Core Version:    0.6.2
 */