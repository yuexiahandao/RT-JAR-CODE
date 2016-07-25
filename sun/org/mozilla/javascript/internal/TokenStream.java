/*      */ package sun.org.mozilla.javascript.internal;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.Reader;
/*      */ 
/*      */ class TokenStream
/*      */ {
/*      */   private static final int EOF_CHAR = -1;
/*      */   private static final char BYTE_ORDER_MARK = '﻿';
/*      */   private boolean dirtyLine;
/*      */   String regExpFlags;
/* 1572 */   private String string = "";
/*      */   private double number;
/*      */   private boolean isOctal;
/*      */   private int quoteChar;
/* 1579 */   private char[] stringBuffer = new char[''];
/*      */   private int stringBufferTop;
/* 1581 */   private ObjToIntMap allStrings = new ObjToIntMap(50);
/*      */ 
/* 1584 */   private final int[] ungetBuffer = new int[3];
/*      */   private int ungetCursor;
/* 1587 */   private boolean hitEOF = false;
/*      */ 
/* 1589 */   private int lineStart = 0;
/* 1590 */   private int lineEndChar = -1;
/*      */   int lineno;
/*      */   private String sourceString;
/*      */   private Reader sourceReader;
/*      */   private char[] sourceBuffer;
/*      */   private int sourceEnd;
/*      */   int sourceCursor;
/*      */   int cursor;
/*      */   int tokenBeg;
/*      */   int tokenEnd;
/*      */   Token.CommentType commentType;
/*      */   private boolean xmlIsAttribute;
/*      */   private boolean xmlIsTagContent;
/*      */   private int xmlOpenTagsCount;
/*      */   private Parser parser;
/* 1621 */   private String commentPrefix = "";
/* 1622 */   private int commentCursor = -1;
/*      */ 
/*      */   TokenStream(Parser paramParser, Reader paramReader, String paramString, int paramInt)
/*      */   {
/*   77 */     this.parser = paramParser;
/*   78 */     this.lineno = paramInt;
/*   79 */     if (paramReader != null) {
/*   80 */       if (paramString != null) Kit.codeBug();
/*   81 */       this.sourceReader = paramReader;
/*   82 */       this.sourceBuffer = new char[512];
/*   83 */       this.sourceEnd = 0;
/*      */     } else {
/*   85 */       if (paramString == null) Kit.codeBug();
/*   86 */       this.sourceString = paramString;
/*   87 */       this.sourceEnd = paramString.length();
/*      */     }
/*   89 */     this.sourceCursor = (this.cursor = 0);
/*      */   }
/*      */ 
/*      */   String tokenToString(int paramInt)
/*      */   {
/*  113 */     return "";
/*      */   }
/*      */ 
/*      */   static boolean isKeyword(String paramString)
/*      */   {
/*  118 */     return 0 != stringToKeyword(paramString);
/*      */   }
/*      */ 
/*      */   private static int stringToKeyword(String paramString)
/*      */   {
/*  191 */     String str1 = paramString;
/*      */ 
/*  193 */     int i = 0; String str2 = null;
/*      */     int j;
/*  194 */     switch (str1.length()) { case 2:
/*  195 */       j = str1.charAt(1);
/*  196 */       if (j == 102) { if (str1.charAt(0) == 'i') { i = 112; break label1797; }
/*  197 */       } else if (j == 110) { if (str1.charAt(0) == 'i') { i = 52; break label1797; }
/*  198 */       } else if ((j == 111) && (str1.charAt(0) == 'd')) i = 118; break;
/*      */     case 3:
/*  200 */       switch (str1.charAt(0)) { case 'f':
/*  201 */         if ((str1.charAt(2) == 'r') && (str1.charAt(1) == 'o')) i = 119; break;
/*      */       case 'i':
/*  202 */         if ((str1.charAt(2) == 't') && (str1.charAt(1) == 'n')) i = 127; break;
/*      */       case 'l':
/*  203 */         if ((str1.charAt(2) == 't') && (str1.charAt(1) == 'e')) i = 153; break;
/*      */       case 'n':
/*  204 */         if ((str1.charAt(2) == 'w') && (str1.charAt(1) == 'e')) i = 30; break;
/*      */       case 't':
/*  205 */         if ((str1.charAt(2) == 'y') && (str1.charAt(1) == 'r')) i = 81; break;
/*      */       case 'v':
/*  206 */         if ((str1.charAt(2) == 'r') && (str1.charAt(1) == 'a')) { i = 122; break label1797; } break;
/*      */       case 'g':
/*      */       case 'h':
/*      */       case 'j':
/*      */       case 'k':
/*      */       case 'm':
/*      */       case 'o':
/*      */       case 'p':
/*      */       case 'q':
/*      */       case 'r':
/*      */       case 's':
/*  207 */       case 'u': } break;
/*      */     case 4:
/*  208 */       switch (str1.charAt(0)) { case 'b':
/*  209 */         str2 = "byte"; i = 127; break;
/*      */       case 'c':
/*  210 */         j = str1.charAt(3);
/*  211 */         if (j == 101) { if ((str1.charAt(2) == 's') && (str1.charAt(1) == 'a')) { i = 115; break label1797; }
/*  212 */         } else if ((j == 114) && (str1.charAt(2) == 'a') && (str1.charAt(1) == 'h')) i = 127; break;
/*      */       case 'e':
/*  214 */         j = str1.charAt(3);
/*  215 */         if (j == 101) { if ((str1.charAt(2) == 's') && (str1.charAt(1) == 'l')) { i = 113; break label1797; }
/*  216 */         } else if ((j == 109) && (str1.charAt(2) == 'u') && (str1.charAt(1) == 'n')) i = 127; break;
/*      */       case 'g':
/*  218 */         str2 = "goto"; i = 127; break;
/*      */       case 'l':
/*  219 */         str2 = "long"; i = 127; break;
/*      */       case 'n':
/*  220 */         str2 = "null"; i = 42; break;
/*      */       case 't':
/*  221 */         j = str1.charAt(3);
/*  222 */         if (j == 101) { if ((str1.charAt(2) == 'u') && (str1.charAt(1) == 'r')) { i = 45; break label1797; }
/*  223 */         } else if ((j == 115) && (str1.charAt(2) == 'i') && (str1.charAt(1) == 'h')) i = 43; break;
/*      */       case 'v':
/*  225 */         str2 = "void"; i = 126; break;
/*      */       case 'w':
/*  226 */         str2 = "with"; i = 123;
/*      */       case 'd':
/*      */       case 'f':
/*      */       case 'h':
/*      */       case 'i':
/*      */       case 'j':
/*      */       case 'k':
/*      */       case 'm':
/*      */       case 'o':
/*      */       case 'p':
/*      */       case 'q':
/*      */       case 'r':
/*      */       case 's':
/*  227 */       case 'u': } break;
/*      */     case 5:
/*  228 */       switch (str1.charAt(2)) { case 'a':
/*  229 */         str2 = "class"; i = 127; break;
/*      */       case 'e':
/*  230 */         j = str1.charAt(0);
/*  231 */         if (j == 98) { str2 = "break"; i = 120;
/*  232 */         } else if (j == 121) { str2 = "yield"; i = 72; } break;
/*      */       case 'i':
/*  234 */         str2 = "while"; i = 117; break;
/*      */       case 'l':
/*  235 */         str2 = "false"; i = 44; break;
/*      */       case 'n':
/*  236 */         j = str1.charAt(0);
/*  237 */         if (j == 99) { str2 = "const"; i = 154;
/*  238 */         } else if (j == 102) { str2 = "final"; i = 127; } break;
/*      */       case 'o':
/*  240 */         j = str1.charAt(0);
/*  241 */         if (j == 102) { str2 = "float"; i = 127;
/*  242 */         } else if (j == 115) { str2 = "short"; i = 127; } break;
/*      */       case 'p':
/*  244 */         str2 = "super"; i = 127; break;
/*      */       case 'r':
/*  245 */         str2 = "throw"; i = 50; break;
/*      */       case 't':
/*  246 */         str2 = "catch"; i = 124;
/*      */       case 'b':
/*      */       case 'c':
/*      */       case 'd':
/*      */       case 'f':
/*      */       case 'g':
/*      */       case 'h':
/*      */       case 'j':
/*      */       case 'k':
/*      */       case 'm':
/*      */       case 'q':
/*  247 */       case 's': } break;
/*      */     case 6:
/*  248 */       switch (str1.charAt(1)) { case 'a':
/*  249 */         str2 = "native"; i = 127; break;
/*      */       case 'e':
/*  250 */         j = str1.charAt(0);
/*  251 */         if (j == 100) { str2 = "delete"; i = 31;
/*  252 */         } else if (j == 114) { str2 = "return"; i = 4; } break;
/*      */       case 'h':
/*  254 */         str2 = "throws"; i = 127; break;
/*      */       case 'm':
/*  255 */         str2 = "import"; i = 127; break;
/*      */       case 'o':
/*  256 */         str2 = "double"; i = 127; break;
/*      */       case 't':
/*  257 */         str2 = "static"; i = 127; break;
/*      */       case 'u':
/*  258 */         str2 = "public"; i = 127; break;
/*      */       case 'w':
/*  259 */         str2 = "switch"; i = 114; break;
/*      */       case 'x':
/*  260 */         str2 = "export"; i = 127; break;
/*      */       case 'y':
/*  261 */         str2 = "typeof"; i = 32;
/*      */       case 'b':
/*      */       case 'c':
/*      */       case 'd':
/*      */       case 'f':
/*      */       case 'g':
/*      */       case 'i':
/*      */       case 'j':
/*      */       case 'k':
/*      */       case 'l':
/*      */       case 'n':
/*      */       case 'p':
/*      */       case 'q':
/*      */       case 'r':
/*      */       case 's':
/*  262 */       case 'v': } break;
/*      */     case 7:
/*  263 */       switch (str1.charAt(1)) { case 'a':
/*  264 */         str2 = "package"; i = 127; break;
/*      */       case 'e':
/*  265 */         str2 = "default"; i = 116; break;
/*      */       case 'i':
/*  266 */         str2 = "finally"; i = 125; break;
/*      */       case 'o':
/*  267 */         str2 = "boolean"; i = 127; break;
/*      */       case 'r':
/*  268 */         str2 = "private"; i = 127; break;
/*      */       case 'x':
/*  269 */         str2 = "extends"; i = 127; }
/*  270 */       break;
/*      */     case 8:
/*  271 */       switch (str1.charAt(0)) { case 'a':
/*  272 */         str2 = "abstract"; i = 127; break;
/*      */       case 'c':
/*  273 */         str2 = "continue"; i = 121; break;
/*      */       case 'd':
/*  274 */         str2 = "debugger"; i = 160; break;
/*      */       case 'f':
/*  275 */         str2 = "function"; i = 109; break;
/*      */       case 'v':
/*  276 */         str2 = "volatile"; i = 127; }
/*  277 */       break;
/*      */     case 9:
/*  278 */       j = str1.charAt(0);
/*  279 */       if (j == 105) { str2 = "interface"; i = 127;
/*  280 */       } else if (j == 112) { str2 = "protected"; i = 127;
/*  281 */       } else if (j == 116) { str2 = "transient"; i = 127; } break;
/*      */     case 10:
/*  283 */       j = str1.charAt(1);
/*  284 */       if (j == 109) { str2 = "implements"; i = 127;
/*  285 */       } else if (j == 110) { str2 = "instanceof"; i = 53; } break;
/*      */     case 12:
/*  287 */       str2 = "synchronized"; i = 127; break;
/*      */     case 11: }
/*  289 */     if ((str2 != null) && (str2 != str1) && (!str2.equals(str1))) i = 0;
/*      */ 
/*  293 */     label1797: if (i == 0) return 0;
/*  294 */     return i & 0xFF;
/*      */   }
/*      */   final String getSourceString() {
/*  297 */     return this.sourceString;
/*      */   }
/*  299 */   final int getLineno() { return this.lineno; } 
/*      */   final String getString() {
/*  301 */     return this.string;
/*      */   }
/*      */   final char getQuoteChar() {
/*  304 */     return (char)this.quoteChar;
/*      */   }
/*      */   final double getNumber() {
/*  307 */     return this.number; } 
/*  308 */   final boolean isNumberOctal() { return this.isOctal; } 
/*      */   final boolean eof() {
/*  310 */     return this.hitEOF;
/*      */   }
/*      */ 
/*      */   final int getToken()
/*      */     throws IOException
/*      */   {
/*      */     int i;
/*      */     do
/*      */     {
/*  320 */       i = getChar();
/*  321 */       if (i == -1) {
/*  322 */         this.tokenBeg = (this.cursor - 1);
/*  323 */         this.tokenEnd = this.cursor;
/*  324 */         return 0;
/*  325 */       }if (i == 10) {
/*  326 */         this.dirtyLine = false;
/*  327 */         this.tokenBeg = (this.cursor - 1);
/*  328 */         this.tokenEnd = this.cursor;
/*  329 */         return 1; } 
/*  330 */     }while (isJSSpace(i));
/*  331 */     if (i != 45) {
/*  332 */       this.dirtyLine = true;
/*      */     }
/*      */ 
/*  339 */     this.tokenBeg = (this.cursor - 1);
/*  340 */     this.tokenEnd = this.cursor;
/*      */ 
/*  342 */     if (i == 64) return 147;
/*      */ 
/*  347 */     int j = 0;
/*      */     boolean bool;
/*  348 */     if (i == 92) {
/*  349 */       i = getChar();
/*  350 */       if (i == 117) {
/*  351 */         bool = true;
/*  352 */         j = 1;
/*  353 */         this.stringBufferTop = 0;
/*      */       } else {
/*  355 */         bool = false;
/*  356 */         ungetChar(i);
/*  357 */         i = 92;
/*      */       }
/*      */     } else {
/*  360 */       bool = Character.isJavaIdentifierStart((char)i);
/*  361 */       if (bool) {
/*  362 */         this.stringBufferTop = 0;
/*  363 */         addToString(i);
/*      */       }
/*      */     }
/*  368 */     int k;
/*  367 */     if (bool) { k = j;
/*      */       int i2;
/*      */       while (true) if (j != 0)
/*      */         {
/*  377 */           int n = 0;
/*  378 */           for (i2 = 0; i2 != 4; i2++) {
/*  379 */             i = getChar();
/*  380 */             n = Kit.xDigitToInt(i, n);
/*      */ 
/*  382 */             if (n < 0) break;
/*      */           }
/*  384 */           if (n < 0) {
/*  385 */             this.parser.addError("msg.invalid.escape");
/*  386 */             return -1;
/*      */           }
/*  388 */           addToString(n);
/*  389 */           j = 0;
/*      */         } else {
/*  391 */           i = getChar();
/*  392 */           if (i == 92) {
/*  393 */             i = getChar();
/*  394 */             if (i == 117) {
/*  395 */               j = 1;
/*  396 */               k = 1;
/*      */             } else {
/*  398 */               this.parser.addError("msg.illegal.character");
/*  399 */               return -1;
/*      */             }
/*      */           } else {
/*  402 */             if ((i == -1) || (i == 65279) || (!Character.isJavaIdentifierPart((char)i)))
/*      */             {
/*      */               break;
/*      */             }
/*      */ 
/*  407 */             addToString(i);
/*      */           }
/*      */         }
/*      */ 
/*  411 */       ungetChar(i);
/*      */ 
/*  413 */       String str2 = getStringFromBuffer();
/*  414 */       if (k == 0)
/*      */       {
/*  419 */         i2 = stringToKeyword(str2);
/*  420 */         if (i2 != 0) {
/*  421 */           if (((i2 == 153) || (i2 == 72)) && (this.parser.compilerEnv.getLanguageVersion() < 170))
/*      */           {
/*  426 */             this.string = (i2 == 153 ? "let" : "yield");
/*  427 */             i2 = 39;
/*      */           }
/*  429 */           if (i2 != 127)
/*  430 */             return i2;
/*  431 */           if (!this.parser.compilerEnv.isReservedKeywordAsIdentifier())
/*      */           {
/*  434 */             return i2;
/*      */           }
/*      */         }
/*      */       }
/*  438 */       this.string = ((String)this.allStrings.intern(str2));
/*  439 */       return 39;
/*      */     }
/*      */     int i1;
/*  443 */     if ((isDigit(i)) || ((i == 46) && (isDigit(peekChar())))) {
/*  444 */       this.isOctal = false;
/*  445 */       this.stringBufferTop = 0;
/*  446 */       k = 10;
/*      */ 
/*  448 */       if (i == 48) {
/*  449 */         i = getChar();
/*  450 */         if ((i == 120) || (i == 88)) {
/*  451 */           k = 16;
/*  452 */           i = getChar();
/*  453 */         } else if (isDigit(i)) {
/*  454 */           k = 8;
/*  455 */           this.isOctal = true;
/*      */         } else {
/*  457 */           addToString(48);
/*      */         }
/*      */       }
/*      */ 
/*  461 */       if (k == 16) {
/*  462 */         while (0 <= Kit.xDigitToInt(i, 0)) {
/*  463 */           addToString(i);
/*  464 */           i = getChar();
/*      */         }
/*      */       }
/*  467 */       while ((48 <= i) && (i <= 57))
/*      */       {
/*  474 */         if ((k == 8) && (i >= 56)) {
/*  475 */           this.parser.addWarning("msg.bad.octal.literal", i == 56 ? "8" : "9");
/*      */ 
/*  477 */           k = 10;
/*      */         }
/*  479 */         addToString(i);
/*  480 */         i = getChar();
/*      */       }
/*      */ 
/*  484 */       i1 = 1;
/*      */ 
/*  486 */       if ((k == 10) && ((i == 46) || (i == 101) || (i == 69))) {
/*  487 */         i1 = 0;
/*  488 */         if (i == 46) {
/*      */           do {
/*  490 */             addToString(i);
/*  491 */             i = getChar();
/*  492 */           }while (isDigit(i));
/*      */         }
/*  494 */         if ((i == 101) || (i == 69)) {
/*  495 */           addToString(i);
/*  496 */           i = getChar();
/*  497 */           if ((i == 43) || (i == 45)) {
/*  498 */             addToString(i);
/*  499 */             i = getChar();
/*      */           }
/*  501 */           if (!isDigit(i)) {
/*  502 */             this.parser.addError("msg.missing.exponent");
/*  503 */             return -1;
/*      */           }
/*      */           do {
/*  506 */             addToString(i);
/*  507 */             i = getChar();
/*  508 */           }while (isDigit(i));
/*      */         }
/*      */       }
/*  511 */       ungetChar(i);
/*  512 */       String str3 = getStringFromBuffer();
/*  513 */       this.string = str3;
/*      */       double d;
/*  516 */       if ((k == 10) && (i1 == 0))
/*      */         try
/*      */         {
/*  519 */           d = Double.valueOf(str3).doubleValue();
/*      */         }
/*      */         catch (NumberFormatException localNumberFormatException) {
/*  522 */           this.parser.addError("msg.caught.nfe");
/*  523 */           return -1;
/*      */         }
/*      */       else {
/*  526 */         d = ScriptRuntime.stringToNumber(str3, 0, k);
/*      */       }
/*      */ 
/*  529 */       this.number = d;
/*  530 */       return 40;
/*      */     }
/*      */ 
/*  534 */     if ((i == 34) || (i == 39))
/*      */     {
/*  540 */       this.quoteChar = i;
/*  541 */       this.stringBufferTop = 0;
/*      */ 
/*  543 */       i = getChar();
/*  544 */       while (i != this.quoteChar) {
/*  545 */         if ((i == 10) || (i == -1)) {
/*  546 */           ungetChar(i);
/*  547 */           this.tokenEnd = this.cursor;
/*  548 */           this.parser.addError("msg.unterminated.string.lit");
/*  549 */           return -1;
/*      */         }
/*      */ 
/*  552 */         if (i == 92)
/*      */         {
/*  556 */           i = getChar();
/*      */           int i3;
/*  557 */           switch (i) { case 98:
/*  558 */             i = 8; break;
/*      */           case 102:
/*  559 */             i = 12; break;
/*      */           case 110:
/*  560 */             i = 10; break;
/*      */           case 114:
/*  561 */             i = 13; break;
/*      */           case 116:
/*  562 */             i = 9; break;
/*      */           case 118:
/*  566 */             i = 11; break;
/*      */           case 117:
/*  572 */             i1 = this.stringBufferTop;
/*  573 */             addToString(117);
/*  574 */             k = 0;
/*  575 */             for (i3 = 0; ; i3++) { if (i3 == 4) break label1104;
/*  576 */               i = getChar();
/*  577 */               k = Kit.xDigitToInt(i, k);
/*  578 */               if (k < 0) {
/*      */                 break;
/*      */               }
/*  581 */               addToString(i);
/*      */             }
/*      */ 
/*  585 */             this.stringBufferTop = i1;
/*  586 */             i = k;
/*  587 */             break;
/*      */           case 120:
/*  591 */             i = getChar();
/*  592 */             k = Kit.xDigitToInt(i, 0);
/*  593 */             if (k < 0) {
/*  594 */               addToString(120);
/*  595 */               continue;
/*      */             }
/*  597 */             i3 = i;
/*  598 */             i = getChar();
/*  599 */             k = Kit.xDigitToInt(i, k);
/*  600 */             if (k < 0) {
/*  601 */               addToString(120);
/*  602 */               addToString(i3);
/*  603 */               continue;
/*      */             }
/*      */ 
/*  606 */             i = k;
/*      */ 
/*  609 */             break;
/*      */           case 10:
/*  614 */             i = getChar();
/*  615 */             break;
/*      */           default:
/*  618 */             label1104: if ((48 <= i) && (i < 56)) {
/*  619 */               i3 = i - 48;
/*  620 */               i = getChar();
/*  621 */               if ((48 <= i) && (i < 56)) {
/*  622 */                 i3 = 8 * i3 + i - 48;
/*  623 */                 i = getChar();
/*  624 */                 if ((48 <= i) && (i < 56) && (i3 <= 31))
/*      */                 {
/*  627 */                   i3 = 8 * i3 + i - 48;
/*  628 */                   i = getChar();
/*      */                 }
/*      */               }
/*  631 */               ungetChar(i);
/*  632 */               i = i3;
/*      */             }
/*      */             break; }
/*      */         } else {
/*  636 */           addToString(i);
/*  637 */           i = getChar();
/*      */         }
/*      */       }
/*  640 */       String str1 = getStringFromBuffer();
/*  641 */       this.string = ((String)this.allStrings.intern(str1));
/*  642 */       return 41;
/*      */     }
/*      */ 
/*  645 */     switch (i) { case 59:
/*  646 */       return 82;
/*      */     case 91:
/*  647 */       return 83;
/*      */     case 93:
/*  648 */       return 84;
/*      */     case 123:
/*  649 */       return 85;
/*      */     case 125:
/*  650 */       return 86;
/*      */     case 40:
/*  651 */       return 87;
/*      */     case 41:
/*  652 */       return 88;
/*      */     case 44:
/*  653 */       return 89;
/*      */     case 63:
/*  654 */       return 102;
/*      */     case 58:
/*  656 */       if (matchChar(58)) {
/*  657 */         return 144;
/*      */       }
/*  659 */       return 103;
/*      */     case 46:
/*  662 */       if (matchChar(46))
/*  663 */         return 143;
/*  664 */       if (matchChar(40)) {
/*  665 */         return 146;
/*      */       }
/*  667 */       return 108;
/*      */     case 124:
/*  671 */       if (matchChar(124))
/*  672 */         return 104;
/*  673 */       if (matchChar(61)) {
/*  674 */         return 91;
/*      */       }
/*  676 */       return 9;
/*      */     case 94:
/*  680 */       if (matchChar(61)) {
/*  681 */         return 92;
/*      */       }
/*  683 */       return 10;
/*      */     case 38:
/*  687 */       if (matchChar(38))
/*  688 */         return 105;
/*  689 */       if (matchChar(61)) {
/*  690 */         return 93;
/*      */       }
/*  692 */       return 11;
/*      */     case 61:
/*  696 */       if (matchChar(61)) {
/*  697 */         if (matchChar(61)) {
/*  698 */           return 46;
/*      */         }
/*  700 */         return 12;
/*      */       }
/*      */ 
/*  703 */       return 90;
/*      */     case 33:
/*  707 */       if (matchChar(61)) {
/*  708 */         if (matchChar(61)) {
/*  709 */           return 47;
/*      */         }
/*  711 */         return 13;
/*      */       }
/*      */ 
/*  714 */       return 26;
/*      */     case 60:
/*  719 */       if (matchChar(33)) {
/*  720 */         if (matchChar(45)) {
/*  721 */           if (matchChar(45)) {
/*  722 */             this.tokenBeg = (this.cursor - 4);
/*  723 */             skipLine();
/*  724 */             this.commentType = Token.CommentType.HTML;
/*  725 */             return 161;
/*      */           }
/*  727 */           ungetCharIgnoreLineEnd(45);
/*      */         }
/*  729 */         ungetCharIgnoreLineEnd(33);
/*      */       }
/*  731 */       if (matchChar(60)) {
/*  732 */         if (matchChar(61)) {
/*  733 */           return 94;
/*      */         }
/*  735 */         return 18;
/*      */       }
/*      */ 
/*  738 */       if (matchChar(61)) {
/*  739 */         return 15;
/*      */       }
/*  741 */       return 14;
/*      */     case 62:
/*  746 */       if (matchChar(62)) {
/*  747 */         if (matchChar(62)) {
/*  748 */           if (matchChar(61)) {
/*  749 */             return 96;
/*      */           }
/*  751 */           return 20;
/*      */         }
/*      */ 
/*  754 */         if (matchChar(61)) {
/*  755 */           return 95;
/*      */         }
/*  757 */         return 19;
/*      */       }
/*      */ 
/*  761 */       if (matchChar(61)) {
/*  762 */         return 17;
/*      */       }
/*  764 */       return 16;
/*      */     case 42:
/*  769 */       if (matchChar(61)) {
/*  770 */         return 99;
/*      */       }
/*  772 */       return 23;
/*      */     case 47:
/*  776 */       markCommentStart();
/*      */ 
/*  778 */       if (matchChar(47)) {
/*  779 */         this.tokenBeg = (this.cursor - 2);
/*  780 */         skipLine();
/*  781 */         this.commentType = Token.CommentType.LINE;
/*  782 */         return 161;
/*      */       }
/*      */ 
/*  785 */       if (matchChar(42)) {
/*  786 */         int m = 0;
/*  787 */         this.tokenBeg = (this.cursor - 2);
/*  788 */         if (matchChar(42)) {
/*  789 */           m = 1;
/*  790 */           this.commentType = Token.CommentType.JSDOC;
/*      */         } else {
/*  792 */           this.commentType = Token.CommentType.BLOCK_COMMENT;
/*      */         }
/*      */         while (true) {
/*  795 */           i = getChar();
/*  796 */           if (i == -1) {
/*  797 */             this.tokenEnd = (this.cursor - 1);
/*  798 */             this.parser.addError("msg.unterminated.comment");
/*  799 */             return 161;
/*  800 */           }if (i == 42) {
/*  801 */             m = 1;
/*  802 */           } else if (i == 47) {
/*  803 */             if (m != 0) {
/*  804 */               this.tokenEnd = this.cursor;
/*  805 */               return 161;
/*      */             }
/*      */           } else {
/*  808 */             m = 0;
/*  809 */             this.tokenEnd = this.cursor;
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  814 */       if (matchChar(61)) {
/*  815 */         return 100;
/*      */       }
/*  817 */       return 24;
/*      */     case 37:
/*  821 */       if (matchChar(61)) {
/*  822 */         return 101;
/*      */       }
/*  824 */       return 25;
/*      */     case 126:
/*  828 */       return 27;
/*      */     case 43:
/*  831 */       if (matchChar(61))
/*  832 */         return 97;
/*  833 */       if (matchChar(43)) {
/*  834 */         return 106;
/*      */       }
/*  836 */       return 21;
/*      */     case 45:
/*  840 */       if (matchChar(61)) {
/*  841 */         i = 98;
/*  842 */       } else if (matchChar(45)) {
/*  843 */         if (!this.dirtyLine)
/*      */         {
/*  846 */           if (matchChar(62)) {
/*  847 */             markCommentStart("--");
/*  848 */             skipLine();
/*  849 */             this.commentType = Token.CommentType.HTML;
/*  850 */             return 161;
/*      */           }
/*      */         }
/*  853 */         i = 107;
/*      */       } else {
/*  855 */         i = 22;
/*      */       }
/*  857 */       this.dirtyLine = true;
/*  858 */       return i;
/*      */     case 34:
/*      */     case 35:
/*      */     case 36:
/*      */     case 39:
/*      */     case 48:
/*      */     case 49:
/*      */     case 50:
/*      */     case 51:
/*      */     case 52:
/*      */     case 53:
/*      */     case 54:
/*      */     case 55:
/*      */     case 56:
/*      */     case 57:
/*      */     case 64:
/*      */     case 65:
/*      */     case 66:
/*      */     case 67:
/*      */     case 68:
/*      */     case 69:
/*      */     case 70:
/*      */     case 71:
/*      */     case 72:
/*      */     case 73:
/*      */     case 74:
/*      */     case 75:
/*      */     case 76:
/*      */     case 77:
/*      */     case 78:
/*      */     case 79:
/*      */     case 80:
/*      */     case 81:
/*      */     case 82:
/*      */     case 83:
/*      */     case 84:
/*      */     case 85:
/*      */     case 86:
/*      */     case 87:
/*      */     case 88:
/*      */     case 89:
/*      */     case 90:
/*      */     case 92:
/*      */     case 95:
/*      */     case 96:
/*      */     case 97:
/*      */     case 98:
/*      */     case 99:
/*      */     case 100:
/*      */     case 101:
/*      */     case 102:
/*      */     case 103:
/*      */     case 104:
/*      */     case 105:
/*      */     case 106:
/*      */     case 107:
/*      */     case 108:
/*      */     case 109:
/*      */     case 110:
/*      */     case 111:
/*      */     case 112:
/*      */     case 113:
/*      */     case 114:
/*      */     case 115:
/*      */     case 116:
/*      */     case 117:
/*      */     case 118:
/*      */     case 119:
/*      */     case 120:
/*      */     case 121:
/*  861 */     case 122: } this.parser.addError("msg.illegal.character");
/*  862 */     return -1;
/*      */   }
/*      */ 
/*      */   private static boolean isAlpha(int paramInt)
/*      */   {
/*  870 */     if (paramInt <= 90) {
/*  871 */       return 65 <= paramInt;
/*      */     }
/*  873 */     return (97 <= paramInt) && (paramInt <= 122);
/*      */   }
/*      */ 
/*      */   static boolean isDigit(int paramInt)
/*      */   {
/*  879 */     return (48 <= paramInt) && (paramInt <= 57);
/*      */   }
/*      */ 
/*      */   static boolean isJSSpace(int paramInt)
/*      */   {
/*  888 */     if (paramInt <= 127) {
/*  889 */       return (paramInt == 32) || (paramInt == 9) || (paramInt == 12) || (paramInt == 11);
/*      */     }
/*  891 */     return (paramInt == 160) || (paramInt == 65279) || (Character.getType((char)paramInt) == 12);
/*      */   }
/*      */ 
/*      */   private static boolean isJSFormatChar(int paramInt)
/*      */   {
/*  898 */     return (paramInt > 127) && (Character.getType((char)paramInt) == 16);
/*      */   }
/*      */ 
/*      */   void readRegExp(int paramInt)
/*      */     throws IOException
/*      */   {
/*  907 */     int i = this.tokenBeg;
/*  908 */     this.stringBufferTop = 0;
/*  909 */     if (paramInt == 100)
/*      */     {
/*  911 */       addToString(61);
/*      */     }
/*  913 */     else if (paramInt != 24) Kit.codeBug();
/*      */ 
/*  916 */     int j = 0;
/*      */     int k;
/*  918 */     while (((k = getChar()) != 47) || (j != 0)) {
/*  919 */       if ((k == 10) || (k == -1)) {
/*  920 */         ungetChar(k);
/*  921 */         this.tokenEnd = (this.cursor - 1);
/*  922 */         this.string = new String(this.stringBuffer, 0, this.stringBufferTop);
/*  923 */         this.parser.reportError("msg.unterminated.re.lit");
/*  924 */         return;
/*      */       }
/*  926 */       if (k == 92) {
/*  927 */         addToString(k);
/*  928 */         k = getChar();
/*  929 */       } else if (k == 91) {
/*  930 */         j = 1;
/*  931 */       } else if (k == 93) {
/*  932 */         j = 0;
/*      */       }
/*  934 */       addToString(k);
/*      */     }
/*  936 */     int m = this.stringBufferTop;
/*      */     while (true)
/*      */     {
/*  939 */       if (matchChar(103)) {
/*  940 */         addToString(103);
/*  941 */       } else if (matchChar(105)) {
/*  942 */         addToString(105);
/*  943 */       } else if (matchChar(109)) {
/*  944 */         addToString(109); } else {
/*  945 */         if (!matchChar(121)) break;
/*  946 */         addToString(121);
/*      */       }
/*      */     }
/*      */ 
/*  950 */     this.tokenEnd = (i + this.stringBufferTop + 2);
/*      */ 
/*  952 */     if (isAlpha(peekChar())) {
/*  953 */       this.parser.reportError("msg.invalid.re.flag");
/*      */     }
/*      */ 
/*  956 */     this.string = new String(this.stringBuffer, 0, m);
/*  957 */     this.regExpFlags = new String(this.stringBuffer, m, this.stringBufferTop - m);
/*      */   }
/*      */ 
/*      */   String readAndClearRegExpFlags()
/*      */   {
/*  962 */     String str = this.regExpFlags;
/*  963 */     this.regExpFlags = null;
/*  964 */     return str;
/*      */   }
/*      */ 
/*      */   boolean isXMLAttribute()
/*      */   {
/*  969 */     return this.xmlIsAttribute;
/*      */   }
/*      */ 
/*      */   int getFirstXMLToken() throws IOException
/*      */   {
/*  974 */     this.xmlOpenTagsCount = 0;
/*  975 */     this.xmlIsAttribute = false;
/*  976 */     this.xmlIsTagContent = false;
/*  977 */     if (!canUngetChar())
/*  978 */       return -1;
/*  979 */     ungetChar(60);
/*  980 */     return getNextXMLToken();
/*      */   }
/*      */ 
/*      */   int getNextXMLToken() throws IOException
/*      */   {
/*  985 */     this.tokenBeg = this.cursor;
/*  986 */     this.stringBufferTop = 0;
/*      */ 
/*  988 */     for (int i = getChar(); i != -1; i = getChar()) {
/*  989 */       if (this.xmlIsTagContent) {
/*  990 */         switch (i) {
/*      */         case 62:
/*  992 */           addToString(i);
/*  993 */           this.xmlIsTagContent = false;
/*  994 */           this.xmlIsAttribute = false;
/*  995 */           break;
/*      */         case 47:
/*  997 */           addToString(i);
/*  998 */           if (peekChar() == 62) {
/*  999 */             i = getChar();
/* 1000 */             addToString(i);
/* 1001 */             this.xmlIsTagContent = false;
/* 1002 */             this.xmlOpenTagsCount -= 1; } break;
/*      */         case 123:
/* 1006 */           ungetChar(i);
/* 1007 */           this.string = getStringFromBuffer();
/* 1008 */           return 145;
/*      */         case 34:
/*      */         case 39:
/* 1011 */           addToString(i);
/* 1012 */           if (!readQuotedString(i)) return -1;
/*      */           break;
/*      */         case 61:
/* 1015 */           addToString(i);
/* 1016 */           this.xmlIsAttribute = true;
/* 1017 */           break;
/*      */         case 9:
/*      */         case 10:
/*      */         case 13:
/*      */         case 32:
/* 1022 */           addToString(i);
/* 1023 */           break;
/*      */         default:
/* 1025 */           addToString(i);
/* 1026 */           this.xmlIsAttribute = false;
/*      */         }
/*      */ 
/* 1030 */         if ((!this.xmlIsTagContent) && (this.xmlOpenTagsCount == 0)) {
/* 1031 */           this.string = getStringFromBuffer();
/* 1032 */           return 148;
/*      */         }
/*      */       } else {
/* 1035 */         switch (i) {
/*      */         case 60:
/* 1037 */           addToString(i);
/* 1038 */           i = peekChar();
/* 1039 */           switch (i) {
/*      */           case 33:
/* 1041 */             i = getChar();
/* 1042 */             addToString(i);
/* 1043 */             i = peekChar();
/* 1044 */             switch (i) {
/*      */             case 45:
/* 1046 */               i = getChar();
/* 1047 */               addToString(i);
/* 1048 */               i = getChar();
/* 1049 */               if (i == 45) {
/* 1050 */                 addToString(i);
/* 1051 */                 if (!readXmlComment()) return -1; 
/*      */               }
/*      */               else
/*      */               {
/* 1054 */                 this.stringBufferTop = 0;
/* 1055 */                 this.string = null;
/* 1056 */                 this.parser.addError("msg.XML.bad.form");
/* 1057 */                 return -1;
/*      */               }
/*      */               break;
/*      */             case 91:
/* 1061 */               i = getChar();
/* 1062 */               addToString(i);
/* 1063 */               if ((getChar() == 67) && (getChar() == 68) && (getChar() == 65) && (getChar() == 84) && (getChar() == 65) && (getChar() == 91))
/*      */               {
/* 1070 */                 addToString(67);
/* 1071 */                 addToString(68);
/* 1072 */                 addToString(65);
/* 1073 */                 addToString(84);
/* 1074 */                 addToString(65);
/* 1075 */                 addToString(91);
/* 1076 */                 if (!readCDATA()) return -1;
/*      */               }
/*      */               else
/*      */               {
/* 1080 */                 this.stringBufferTop = 0;
/* 1081 */                 this.string = null;
/* 1082 */                 this.parser.addError("msg.XML.bad.form");
/* 1083 */                 return -1;
/*      */               }
/*      */               break;
/*      */             default:
/* 1087 */               if (!readEntity()) return -1;
/*      */               break;
/*      */             }
/*      */             break;
/*      */           case 63:
/* 1092 */             i = getChar();
/* 1093 */             addToString(i);
/* 1094 */             if (!readPI()) return -1;
/*      */ 
/*      */             break;
/*      */           case 47:
/* 1098 */             i = getChar();
/* 1099 */             addToString(i);
/* 1100 */             if (this.xmlOpenTagsCount == 0)
/*      */             {
/* 1102 */               this.stringBufferTop = 0;
/* 1103 */               this.string = null;
/* 1104 */               this.parser.addError("msg.XML.bad.form");
/* 1105 */               return -1;
/*      */             }
/* 1107 */             this.xmlIsTagContent = true;
/* 1108 */             this.xmlOpenTagsCount -= 1;
/* 1109 */             break;
/*      */           default:
/* 1112 */             this.xmlIsTagContent = true;
/* 1113 */             this.xmlOpenTagsCount += 1;
/* 1114 */           }break;
/*      */         case 123:
/* 1118 */           ungetChar(i);
/* 1119 */           this.string = getStringFromBuffer();
/* 1120 */           return 145;
/*      */         default:
/* 1122 */           addToString(i);
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1128 */     this.tokenEnd = this.cursor;
/* 1129 */     this.stringBufferTop = 0;
/* 1130 */     this.string = null;
/* 1131 */     this.parser.addError("msg.XML.bad.form");
/* 1132 */     return -1;
/*      */   }
/*      */ 
/*      */   private boolean readQuotedString(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1140 */     for (int i = getChar(); i != -1; i = getChar()) {
/* 1141 */       addToString(i);
/* 1142 */       if (i == paramInt) return true;
/*      */     }
/*      */ 
/* 1145 */     this.stringBufferTop = 0;
/* 1146 */     this.string = null;
/* 1147 */     this.parser.addError("msg.XML.bad.form");
/* 1148 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean readXmlComment()
/*      */     throws IOException
/*      */   {
/* 1156 */     for (int i = getChar(); i != -1; ) {
/* 1157 */       addToString(i);
/* 1158 */       if ((i == 45) && (peekChar() == 45)) {
/* 1159 */         i = getChar();
/* 1160 */         addToString(i);
/* 1161 */         if (peekChar() == 62) {
/* 1162 */           i = getChar();
/* 1163 */           addToString(i);
/* 1164 */           return true;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1169 */         i = getChar();
/*      */       }
/*      */     }
/* 1172 */     this.stringBufferTop = 0;
/* 1173 */     this.string = null;
/* 1174 */     this.parser.addError("msg.XML.bad.form");
/* 1175 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean readCDATA()
/*      */     throws IOException
/*      */   {
/* 1183 */     for (int i = getChar(); i != -1; ) {
/* 1184 */       addToString(i);
/* 1185 */       if ((i == 93) && (peekChar() == 93)) {
/* 1186 */         i = getChar();
/* 1187 */         addToString(i);
/* 1188 */         if (peekChar() == 62) {
/* 1189 */           i = getChar();
/* 1190 */           addToString(i);
/* 1191 */           return true;
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1196 */         i = getChar();
/*      */       }
/*      */     }
/* 1199 */     this.stringBufferTop = 0;
/* 1200 */     this.string = null;
/* 1201 */     this.parser.addError("msg.XML.bad.form");
/* 1202 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean readEntity()
/*      */     throws IOException
/*      */   {
/* 1210 */     int i = 1;
/* 1211 */     for (int j = getChar(); j != -1; j = getChar()) {
/* 1212 */       addToString(j);
/* 1213 */       switch (j) {
/*      */       case 60:
/* 1215 */         i++;
/* 1216 */         break;
/*      */       case 62:
/* 1218 */         i--;
/* 1219 */         if (i == 0) return true;
/*      */         break;
/*      */       }
/*      */     }
/*      */ 
/* 1224 */     this.stringBufferTop = 0;
/* 1225 */     this.string = null;
/* 1226 */     this.parser.addError("msg.XML.bad.form");
/* 1227 */     return false;
/*      */   }
/*      */ 
/*      */   private boolean readPI()
/*      */     throws IOException
/*      */   {
/* 1235 */     for (int i = getChar(); i != -1; i = getChar()) {
/* 1236 */       addToString(i);
/* 1237 */       if ((i == 63) && (peekChar() == 62)) {
/* 1238 */         i = getChar();
/* 1239 */         addToString(i);
/* 1240 */         return true;
/*      */       }
/*      */     }
/*      */ 
/* 1244 */     this.stringBufferTop = 0;
/* 1245 */     this.string = null;
/* 1246 */     this.parser.addError("msg.XML.bad.form");
/* 1247 */     return false;
/*      */   }
/*      */ 
/*      */   private String getStringFromBuffer()
/*      */   {
/* 1252 */     this.tokenEnd = this.cursor;
/* 1253 */     return new String(this.stringBuffer, 0, this.stringBufferTop);
/*      */   }
/*      */ 
/*      */   private void addToString(int paramInt)
/*      */   {
/* 1258 */     int i = this.stringBufferTop;
/* 1259 */     if (i == this.stringBuffer.length) {
/* 1260 */       char[] arrayOfChar = new char[this.stringBuffer.length * 2];
/* 1261 */       System.arraycopy(this.stringBuffer, 0, arrayOfChar, 0, i);
/* 1262 */       this.stringBuffer = arrayOfChar;
/*      */     }
/* 1264 */     this.stringBuffer[i] = ((char)paramInt);
/* 1265 */     this.stringBufferTop = (i + 1);
/*      */   }
/*      */ 
/*      */   private boolean canUngetChar() {
/* 1269 */     return (this.ungetCursor == 0) || (this.ungetBuffer[(this.ungetCursor - 1)] != 10);
/*      */   }
/*      */ 
/*      */   private void ungetChar(int paramInt)
/*      */   {
/* 1275 */     if ((this.ungetCursor != 0) && (this.ungetBuffer[(this.ungetCursor - 1)] == 10))
/* 1276 */       Kit.codeBug();
/* 1277 */     this.ungetBuffer[(this.ungetCursor++)] = paramInt;
/* 1278 */     this.cursor -= 1;
/*      */   }
/*      */ 
/*      */   private boolean matchChar(int paramInt) throws IOException
/*      */   {
/* 1283 */     int i = getCharIgnoreLineEnd();
/* 1284 */     if (i == paramInt) {
/* 1285 */       this.tokenEnd = this.cursor;
/* 1286 */       return true;
/*      */     }
/* 1288 */     ungetCharIgnoreLineEnd(i);
/* 1289 */     return false;
/*      */   }
/*      */ 
/*      */   private int peekChar()
/*      */     throws IOException
/*      */   {
/* 1295 */     int i = getChar();
/* 1296 */     ungetChar(i);
/* 1297 */     return i;
/*      */   }
/*      */ 
/*      */   private int getChar() throws IOException
/*      */   {
/* 1302 */     if (this.ungetCursor != 0) {
/* 1303 */       this.cursor += 1;
/* 1304 */       return this.ungetBuffer[(--this.ungetCursor)];
/*      */     }int i;
/*      */     label199: 
/*      */     do {
/*      */       while (true) {
/* 1309 */         if (this.sourceString != null) {
/* 1310 */           if (this.sourceCursor == this.sourceEnd) {
/* 1311 */             this.hitEOF = true;
/* 1312 */             return -1;
/*      */           }
/* 1314 */           this.cursor += 1;
/* 1315 */           i = this.sourceString.charAt(this.sourceCursor++);
/*      */         } else {
/* 1317 */           if ((this.sourceCursor == this.sourceEnd) && 
/* 1318 */             (!fillSourceBuffer())) {
/* 1319 */             this.hitEOF = true;
/* 1320 */             return -1;
/*      */           }
/*      */ 
/* 1323 */           this.cursor += 1;
/* 1324 */           i = this.sourceBuffer[(this.sourceCursor++)];
/*      */         }
/*      */ 
/* 1327 */         if (this.lineEndChar < 0) break label199;
/* 1328 */         if ((this.lineEndChar != 13) || (i != 10)) break;
/* 1329 */         this.lineEndChar = 10;
/*      */       }
/*      */ 
/* 1332 */       this.lineEndChar = -1;
/* 1333 */       this.lineStart = (this.sourceCursor - 1);
/* 1334 */       this.lineno += 1;
/*      */ 
/* 1337 */       if (i <= 127) {
/* 1338 */         if ((i != 10) && (i != 13)) break;
/* 1339 */         this.lineEndChar = i;
/* 1340 */         i = 10; break;
/*      */       }
/*      */ 
/* 1343 */       if (i == 65279) return i; 
/*      */     }
/* 1344 */     while (isJSFormatChar(i));
/*      */ 
/* 1347 */     if (ScriptRuntime.isJSLineTerminator(i)) {
/* 1348 */       this.lineEndChar = i;
/* 1349 */       i = 10;
/*      */     }
/*      */ 
/* 1352 */     return i;
/*      */   }
/*      */ 
/*      */   private int getCharIgnoreLineEnd()
/*      */     throws IOException
/*      */   {
/* 1358 */     if (this.ungetCursor != 0) {
/* 1359 */       this.cursor += 1;
/* 1360 */       return this.ungetBuffer[(--this.ungetCursor)];
/*      */     }
/*      */     int i;
/*      */     do
/*      */     {
/* 1365 */       if (this.sourceString != null) {
/* 1366 */         if (this.sourceCursor == this.sourceEnd) {
/* 1367 */           this.hitEOF = true;
/* 1368 */           return -1;
/*      */         }
/* 1370 */         this.cursor += 1;
/* 1371 */         i = this.sourceString.charAt(this.sourceCursor++);
/*      */       } else {
/* 1373 */         if ((this.sourceCursor == this.sourceEnd) && 
/* 1374 */           (!fillSourceBuffer())) {
/* 1375 */           this.hitEOF = true;
/* 1376 */           return -1;
/*      */         }
/*      */ 
/* 1379 */         this.cursor += 1;
/* 1380 */         i = this.sourceBuffer[(this.sourceCursor++)];
/*      */       }
/*      */ 
/* 1383 */       if (i <= 127) {
/* 1384 */         if ((i != 10) && (i != 13)) break;
/* 1385 */         this.lineEndChar = i;
/* 1386 */         i = 10; break;
/*      */       }
/*      */ 
/* 1389 */       if (i == 65279) return i; 
/*      */     }
/* 1390 */     while (isJSFormatChar(i));
/*      */ 
/* 1393 */     if (ScriptRuntime.isJSLineTerminator(i)) {
/* 1394 */       this.lineEndChar = i;
/* 1395 */       i = 10;
/*      */     }
/*      */ 
/* 1398 */     return i;
/*      */   }
/*      */ 
/*      */   private void ungetCharIgnoreLineEnd(int paramInt)
/*      */   {
/* 1404 */     this.ungetBuffer[(this.ungetCursor++)] = paramInt;
/* 1405 */     this.cursor -= 1;
/*      */   }
/*      */ 
/*      */   private void skipLine()
/*      */     throws IOException
/*      */   {
/*      */     int i;
/* 1412 */     while (((i = getChar()) != -1) && (i != 10));
/* 1413 */     ungetChar(i);
/* 1414 */     this.tokenEnd = this.cursor;
/*      */   }
/*      */ 
/*      */   final int getOffset()
/*      */   {
/* 1422 */     int i = this.sourceCursor - this.lineStart;
/* 1423 */     if (this.lineEndChar >= 0) i--;
/* 1424 */     return i;
/*      */   }
/*      */ 
/*      */   final String getLine()
/*      */   {
/*      */     int j;
/* 1429 */     if (this.sourceString != null)
/*      */     {
/* 1431 */       i = this.sourceCursor;
/* 1432 */       if (this.lineEndChar >= 0)
/* 1433 */         i--;
/*      */       else {
/* 1435 */         for (; i != this.sourceEnd; i++) {
/* 1436 */           j = this.sourceString.charAt(i);
/* 1437 */           if (ScriptRuntime.isJSLineTerminator(j)) {
/*      */             break;
/*      */           }
/*      */         }
/*      */       }
/* 1442 */       return this.sourceString.substring(this.lineStart, i);
/*      */     }
/*      */ 
/* 1445 */     int i = this.sourceCursor - this.lineStart;
/* 1446 */     if (this.lineEndChar >= 0) {
/* 1447 */       i--;
/*      */     }
/*      */     else {
/* 1450 */       for (; ; i++) {
/* 1451 */         j = this.lineStart + i;
/* 1452 */         if (j == this.sourceEnd) {
/*      */           try {
/* 1454 */             if (!fillSourceBuffer()) break; 
/*      */           }
/*      */           catch (IOException localIOException)
/*      */           {
/* 1457 */             break;
/*      */           }
/*      */ 
/* 1461 */           j = this.lineStart + i;
/*      */         }
/* 1463 */         int k = this.sourceBuffer[j];
/* 1464 */         if (ScriptRuntime.isJSLineTerminator(k)) {
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/* 1469 */     return new String(this.sourceBuffer, this.lineStart, i);
/*      */   }
/*      */ 
/*      */   private boolean fillSourceBuffer()
/*      */     throws IOException
/*      */   {
/* 1475 */     if (this.sourceString != null) Kit.codeBug();
/* 1476 */     if (this.sourceEnd == this.sourceBuffer.length) {
/* 1477 */       if ((this.lineStart != 0) && (!isMarkingComment())) {
/* 1478 */         System.arraycopy(this.sourceBuffer, this.lineStart, this.sourceBuffer, 0, this.sourceEnd - this.lineStart);
/*      */ 
/* 1480 */         this.sourceEnd -= this.lineStart;
/* 1481 */         this.sourceCursor -= this.lineStart;
/* 1482 */         this.lineStart = 0;
/*      */       } else {
/* 1484 */         char[] arrayOfChar = new char[this.sourceBuffer.length * 2];
/* 1485 */         System.arraycopy(this.sourceBuffer, 0, arrayOfChar, 0, this.sourceEnd);
/* 1486 */         this.sourceBuffer = arrayOfChar;
/*      */       }
/*      */     }
/* 1489 */     int i = this.sourceReader.read(this.sourceBuffer, this.sourceEnd, this.sourceBuffer.length - this.sourceEnd);
/*      */ 
/* 1491 */     if (i < 0) {
/* 1492 */       return false;
/*      */     }
/* 1494 */     this.sourceEnd += i;
/* 1495 */     return true;
/*      */   }
/*      */ 
/*      */   public int getCursor()
/*      */   {
/* 1502 */     return this.cursor;
/*      */   }
/*      */ 
/*      */   public int getTokenBeg()
/*      */   {
/* 1509 */     return this.tokenBeg;
/*      */   }
/*      */ 
/*      */   public int getTokenEnd()
/*      */   {
/* 1516 */     return this.tokenEnd;
/*      */   }
/*      */ 
/*      */   public int getTokenLength()
/*      */   {
/* 1523 */     return this.tokenEnd - this.tokenBeg;
/*      */   }
/*      */ 
/*      */   public Token.CommentType getCommentType()
/*      */   {
/* 1531 */     return this.commentType;
/*      */   }
/*      */ 
/*      */   private void markCommentStart() {
/* 1535 */     markCommentStart("");
/*      */   }
/*      */ 
/*      */   private void markCommentStart(String paramString) {
/* 1539 */     if ((this.parser.compilerEnv.isRecordingComments()) && (this.sourceReader != null)) {
/* 1540 */       this.commentPrefix = paramString;
/* 1541 */       this.commentCursor = (this.sourceCursor - 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean isMarkingComment() {
/* 1546 */     return this.commentCursor != -1;
/*      */   }
/*      */ 
/*      */   final String getAndResetCurrentComment() {
/* 1550 */     if (this.sourceString != null) {
/* 1551 */       if (isMarkingComment()) Kit.codeBug();
/* 1552 */       return this.sourceString.substring(this.tokenBeg, this.tokenEnd);
/*      */     }
/* 1554 */     if (!isMarkingComment()) Kit.codeBug();
/* 1555 */     StringBuilder localStringBuilder = new StringBuilder(this.commentPrefix);
/* 1556 */     localStringBuilder.append(this.sourceBuffer, this.commentCursor, getTokenLength() - this.commentPrefix.length());
/*      */ 
/* 1558 */     this.commentCursor = -1;
/* 1559 */     return localStringBuilder.toString();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.TokenStream
 * JD-Core Version:    0.6.2
 */