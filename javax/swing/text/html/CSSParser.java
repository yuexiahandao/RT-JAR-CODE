/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Reader;
/*     */ 
/*     */ class CSSParser
/*     */ {
/*     */   private static final int IDENTIFIER = 1;
/*     */   private static final int BRACKET_OPEN = 2;
/*     */   private static final int BRACKET_CLOSE = 3;
/*     */   private static final int BRACE_OPEN = 4;
/*     */   private static final int BRACE_CLOSE = 5;
/*     */   private static final int PAREN_OPEN = 6;
/*     */   private static final int PAREN_CLOSE = 7;
/*     */   private static final int END = -1;
/*  91 */   private static final char[] charMapping = { '\000', '\000', '[', ']', '{', '}', '(', ')', '\000' };
/*     */   private boolean didPushChar;
/*     */   private int pushedChar;
/*     */   private StringBuffer unitBuffer;
/*     */   private int[] unitStack;
/*     */   private int stackCount;
/*     */   private Reader reader;
/*     */   private boolean encounteredRuleSet;
/*     */   private CSSParserCallback callback;
/*     */   private char[] tokenBuffer;
/*     */   private int tokenBufferLength;
/*     */   private boolean readWS;
/*     */ 
/*     */   CSSParser()
/*     */   {
/* 137 */     this.unitStack = new int[2];
/* 138 */     this.tokenBuffer = new char[80];
/* 139 */     this.unitBuffer = new StringBuffer();
/*     */   }
/*     */ 
/*     */   void parse(Reader paramReader, CSSParserCallback paramCSSParserCallback, boolean paramBoolean) throws IOException
/*     */   {
/* 144 */     this.callback = paramCSSParserCallback;
/* 145 */     this.stackCount = (this.tokenBufferLength = 0);
/* 146 */     this.reader = paramReader;
/* 147 */     this.encounteredRuleSet = false;
/*     */     try {
/* 149 */       if (paramBoolean) {
/* 150 */         parseDeclarationBlock();
/*     */       }
/*     */       else
/* 153 */         while (getNextStatement());
/*     */     }
/*     */     finally {
/* 156 */       paramCSSParserCallback = null;
/* 157 */       paramReader = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean getNextStatement()
/*     */     throws IOException
/*     */   {
/* 166 */     this.unitBuffer.setLength(0);
/*     */ 
/* 168 */     int i = nextToken('\000');
/*     */ 
/* 170 */     switch (i) {
/*     */     case 1:
/* 172 */       if (this.tokenBufferLength > 0) {
/* 173 */         if (this.tokenBuffer[0] == '@') {
/* 174 */           parseAtRule();
/*     */         }
/*     */         else {
/* 177 */           this.encounteredRuleSet = true;
/* 178 */           parseRuleSet();
/*     */         }
/*     */       }
/* 181 */       return true;
/*     */     case 2:
/*     */     case 4:
/*     */     case 6:
/* 185 */       parseTillClosed(i);
/* 186 */       return true;
/*     */     case 3:
/*     */     case 5:
/*     */     case 7:
/* 192 */       throw new RuntimeException("Unexpected top level block close");
/*     */     case -1:
/* 195 */       return false;
/*     */     case 0:
/* 197 */     }return true;
/*     */   }
/*     */ 
/*     */   private void parseAtRule()
/*     */     throws IOException
/*     */   {
/* 205 */     int i = 0;
/* 206 */     int j = (this.tokenBufferLength == 7) && (this.tokenBuffer[0] == '@') && (this.tokenBuffer[1] == 'i') && (this.tokenBuffer[2] == 'm') && (this.tokenBuffer[3] == 'p') && (this.tokenBuffer[4] == 'o') && (this.tokenBuffer[5] == 'r') && (this.tokenBuffer[6] == 't') ? 1 : 0;
/*     */ 
/* 212 */     this.unitBuffer.setLength(0);
/* 213 */     while (i == 0) {
/* 214 */       int k = nextToken(';');
/*     */ 
/* 216 */       switch (k) {
/*     */       case 1:
/* 218 */         if ((this.tokenBufferLength > 0) && (this.tokenBuffer[(this.tokenBufferLength - 1)] == ';'))
/*     */         {
/* 220 */           this.tokenBufferLength -= 1;
/* 221 */           i = 1;
/*     */         }
/* 223 */         if (this.tokenBufferLength > 0) {
/* 224 */           if ((this.unitBuffer.length() > 0) && (this.readWS)) {
/* 225 */             this.unitBuffer.append(' ');
/*     */           }
/* 227 */           this.unitBuffer.append(this.tokenBuffer, 0, this.tokenBufferLength); } break;
/*     */       case 4:
/* 232 */         if ((this.unitBuffer.length() > 0) && (this.readWS)) {
/* 233 */           this.unitBuffer.append(' ');
/*     */         }
/* 235 */         this.unitBuffer.append(charMapping[k]);
/* 236 */         parseTillClosed(k);
/* 237 */         i = 1;
/*     */ 
/* 240 */         int m = readWS();
/* 241 */         if ((m != -1) && (m != 59)) {
/* 242 */           pushChar(m);
/*     */         }
/*     */ 
/* 245 */         break;
/*     */       case 2:
/*     */       case 6:
/* 248 */         this.unitBuffer.append(charMapping[k]);
/* 249 */         parseTillClosed(k);
/* 250 */         break;
/*     */       case 3:
/*     */       case 5:
/*     */       case 7:
/* 253 */         throw new RuntimeException("Unexpected close in @ rule");
/*     */       case -1:
/* 256 */         i = 1;
/*     */       case 0:
/*     */       }
/*     */     }
/* 260 */     if ((j != 0) && (!this.encounteredRuleSet))
/* 261 */       this.callback.handleImport(this.unitBuffer.toString());
/*     */   }
/*     */ 
/*     */   private void parseRuleSet()
/*     */     throws IOException
/*     */   {
/* 270 */     if (parseSelectors()) {
/* 271 */       this.callback.startRule();
/* 272 */       parseDeclarationBlock();
/* 273 */       this.callback.endRule();
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean parseSelectors()
/*     */     throws IOException
/*     */   {
/* 285 */     if (this.tokenBufferLength > 0) {
/* 286 */       this.callback.handleSelector(new String(this.tokenBuffer, 0, this.tokenBufferLength));
/*     */     }
/*     */ 
/* 290 */     this.unitBuffer.setLength(0);
/*     */     while (true)
/*     */     {
/*     */       int i;
/* 292 */       if ((i = nextToken('\000')) == 1) {
/* 293 */         if (this.tokenBufferLength > 0) {
/* 294 */           this.callback.handleSelector(new String(this.tokenBuffer, 0, this.tokenBufferLength));
/*     */         }
/*     */       }
/*     */       else
/* 298 */         switch (i) {
/*     */         case 4:
/* 300 */           return true;
/*     */         case 2:
/*     */         case 6:
/* 303 */           parseTillClosed(i);
/*     */ 
/* 306 */           this.unitBuffer.setLength(0);
/* 307 */           break;
/*     */         case 3:
/*     */         case 5:
/*     */         case 7:
/* 310 */           throw new RuntimeException("Unexpected block close in selector");
/*     */         case -1:
/* 314 */           return false;
/*     */         case 0:
/*     */         case 1:
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parseDeclarationBlock() throws IOException
/*     */   {
/*     */     while (true)
/*     */     {
/* 325 */       int i = parseDeclaration();
/* 326 */       switch (i) { case -1:
/*     */       case 5:
/* 328 */         return;
/*     */       case 3:
/*     */       case 7:
/* 332 */         throw new RuntimeException("Unexpected close in declaration block");
/*     */       case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 4:
/*     */       case 6:
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private int parseDeclaration()
/*     */     throws IOException
/*     */   {
/* 347 */     if ((i = parseIdentifiers(':', false)) != 1) {
/* 348 */       return i;
/*     */     }
/*     */ 
/* 351 */     for (int j = this.unitBuffer.length() - 1; j >= 0; j--) {
/* 352 */       this.unitBuffer.setCharAt(j, Character.toLowerCase(this.unitBuffer.charAt(j)));
/*     */     }
/*     */ 
/* 355 */     this.callback.handleProperty(this.unitBuffer.toString());
/*     */ 
/* 357 */     int i = parseIdentifiers(';', true);
/* 358 */     this.callback.handleValue(this.unitBuffer.toString());
/* 359 */     return i;
/*     */   }
/*     */ 
/*     */   private int parseIdentifiers(char paramChar, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 372 */     this.unitBuffer.setLength(0);
/*     */     while (true) {
/* 374 */       int i = nextToken(paramChar);
/*     */ 
/* 376 */       switch (i) {
/*     */       case 1:
/* 378 */         if (this.tokenBufferLength > 0) {
/* 379 */           if (this.tokenBuffer[(this.tokenBufferLength - 1)] == paramChar) {
/* 380 */             if (--this.tokenBufferLength > 0) {
/* 381 */               if ((this.readWS) && (this.unitBuffer.length() > 0)) {
/* 382 */                 this.unitBuffer.append(' ');
/*     */               }
/* 384 */               this.unitBuffer.append(this.tokenBuffer, 0, this.tokenBufferLength);
/*     */             }
/*     */ 
/* 387 */             return 1;
/*     */           }
/* 389 */           if ((this.readWS) && (this.unitBuffer.length() > 0)) {
/* 390 */             this.unitBuffer.append(' ');
/*     */           }
/* 392 */           this.unitBuffer.append(this.tokenBuffer, 0, this.tokenBufferLength); } break;
/*     */       case 2:
/*     */       case 4:
/*     */       case 6:
/* 399 */         int j = this.unitBuffer.length();
/* 400 */         if (paramBoolean) {
/* 401 */           this.unitBuffer.append(charMapping[i]);
/*     */         }
/* 403 */         parseTillClosed(i);
/* 404 */         if (!paramBoolean)
/* 405 */           this.unitBuffer.setLength(j); break;
/*     */       case -1:
/*     */       case 3:
/*     */       case 5:
/*     */       case 7:
/* 416 */         return i;
/*     */       case 0:
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void parseTillClosed(int paramInt)
/*     */     throws IOException
/*     */   {
/* 427 */     int j = 0;
/*     */ 
/* 429 */     startBlock(paramInt);
/* 430 */     while (j == 0) {
/* 431 */       int i = nextToken('\000');
/* 432 */       switch (i) {
/*     */       case 1:
/* 434 */         if ((this.unitBuffer.length() > 0) && (this.readWS)) {
/* 435 */           this.unitBuffer.append(' ');
/*     */         }
/* 437 */         if (this.tokenBufferLength > 0)
/* 438 */           this.unitBuffer.append(this.tokenBuffer, 0, this.tokenBufferLength); break;
/*     */       case 2:
/*     */       case 4:
/*     */       case 6:
/* 443 */         if ((this.unitBuffer.length() > 0) && (this.readWS)) {
/* 444 */           this.unitBuffer.append(' ');
/*     */         }
/* 446 */         this.unitBuffer.append(charMapping[i]);
/* 447 */         startBlock(i);
/* 448 */         break;
/*     */       case 3:
/*     */       case 5:
/*     */       case 7:
/* 451 */         if ((this.unitBuffer.length() > 0) && (this.readWS)) {
/* 452 */           this.unitBuffer.append(' ');
/*     */         }
/* 454 */         this.unitBuffer.append(charMapping[i]);
/* 455 */         endBlock(i);
/* 456 */         if (!inBlock())
/* 457 */           j = 1; break;
/*     */       case -1:
/* 463 */         throw new RuntimeException("Unclosed block");
/*     */       case 0:
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private int nextToken(char paramChar)
/*     */     throws IOException
/*     */   {
/* 472 */     this.readWS = false;
/*     */ 
/* 474 */     int i = readWS();
/*     */ 
/* 476 */     switch (i) {
/*     */     case 39:
/* 478 */       readTill('\'');
/* 479 */       if (this.tokenBufferLength > 0) {
/* 480 */         this.tokenBufferLength -= 1;
/*     */       }
/* 482 */       return 1;
/*     */     case 34:
/* 484 */       readTill('"');
/* 485 */       if (this.tokenBufferLength > 0) {
/* 486 */         this.tokenBufferLength -= 1;
/*     */       }
/* 488 */       return 1;
/*     */     case 91:
/* 490 */       return 2;
/*     */     case 93:
/* 492 */       return 3;
/*     */     case 123:
/* 494 */       return 4;
/*     */     case 125:
/* 496 */       return 5;
/*     */     case 40:
/* 498 */       return 6;
/*     */     case 41:
/* 500 */       return 7;
/*     */     case -1:
/* 502 */       return -1;
/*     */     }
/* 504 */     pushChar(i);
/* 505 */     getIdentifier(paramChar);
/* 506 */     return 1;
/*     */   }
/*     */ 
/*     */   private boolean getIdentifier(char paramChar)
/*     */     throws IOException
/*     */   {
/* 518 */     int i = 0;
/* 519 */     int j = 0;
/* 520 */     int k = 0;
/* 521 */     int m = 0;
/*     */ 
/* 523 */     int i1 = paramChar;
/*     */ 
/* 527 */     int i3 = 0;
/*     */ 
/* 529 */     this.tokenBufferLength = 0;
/* 530 */     while (j == 0) {
/* 531 */       int n = readChar();
/*     */       int i2;
/* 532 */       switch (n) {
/*     */       case 92:
/* 534 */         i2 = 1;
/* 535 */         break;
/*     */       case 48:
/*     */       case 49:
/*     */       case 50:
/*     */       case 51:
/*     */       case 52:
/*     */       case 53:
/*     */       case 54:
/*     */       case 55:
/*     */       case 56:
/*     */       case 57:
/* 539 */         i2 = 2;
/* 540 */         i3 = n - 48;
/* 541 */         break;
/*     */       case 97:
/*     */       case 98:
/*     */       case 99:
/*     */       case 100:
/*     */       case 101:
/*     */       case 102:
/* 544 */         i2 = 2;
/* 545 */         i3 = n - 97 + 10;
/* 546 */         break;
/*     */       case 65:
/*     */       case 66:
/*     */       case 67:
/*     */       case 68:
/*     */       case 69:
/*     */       case 70:
/* 549 */         i2 = 2;
/* 550 */         i3 = n - 65 + 10;
/* 551 */         break;
/*     */       case 9:
/*     */       case 10:
/*     */       case 13:
/*     */       case 32:
/*     */       case 34:
/*     */       case 39:
/*     */       case 40:
/*     */       case 41:
/*     */       case 91:
/*     */       case 93:
/*     */       case 123:
/*     */       case 125:
/* 556 */         i2 = 3;
/* 557 */         break;
/*     */       case 47:
/* 560 */         i2 = 4;
/* 561 */         break;
/*     */       case -1:
/* 565 */         j = 1;
/* 566 */         i2 = 0;
/* 567 */         break;
/*     */       case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 8:
/*     */       case 11:
/*     */       case 12:
/*     */       case 14:
/*     */       case 15:
/*     */       case 16:
/*     */       case 17:
/*     */       case 18:
/*     */       case 19:
/*     */       case 20:
/*     */       case 21:
/*     */       case 22:
/*     */       case 23:
/*     */       case 24:
/*     */       case 25:
/*     */       case 26:
/*     */       case 27:
/*     */       case 28:
/*     */       case 29:
/*     */       case 30:
/*     */       case 31:
/*     */       case 33:
/*     */       case 35:
/*     */       case 36:
/*     */       case 37:
/*     */       case 38:
/*     */       case 42:
/*     */       case 43:
/*     */       case 44:
/*     */       case 45:
/*     */       case 46:
/*     */       case 58:
/*     */       case 59:
/*     */       case 60:
/*     */       case 61:
/*     */       case 62:
/*     */       case 63:
/*     */       case 64:
/*     */       case 71:
/*     */       case 72:
/*     */       case 73:
/*     */       case 74:
/*     */       case 75:
/*     */       case 76:
/*     */       case 77:
/*     */       case 78:
/*     */       case 79:
/*     */       case 80:
/*     */       case 81:
/*     */       case 82:
/*     */       case 83:
/*     */       case 84:
/*     */       case 85:
/*     */       case 86:
/*     */       case 87:
/*     */       case 88:
/*     */       case 89:
/*     */       case 90:
/*     */       case 94:
/*     */       case 95:
/*     */       case 96:
/*     */       case 103:
/*     */       case 104:
/*     */       case 105:
/*     */       case 106:
/*     */       case 107:
/*     */       case 108:
/*     */       case 109:
/*     */       case 110:
/*     */       case 111:
/*     */       case 112:
/*     */       case 113:
/*     */       case 114:
/*     */       case 115:
/*     */       case 116:
/*     */       case 117:
/*     */       case 118:
/*     */       case 119:
/*     */       case 120:
/*     */       case 121:
/*     */       case 122:
/*     */       case 124:
/*     */       default:
/* 570 */         i2 = 0;
/*     */       }
/*     */ 
/* 573 */       if (i != 0) {
/* 574 */         if (i2 == 2)
/*     */         {
/* 576 */           m = m * 16 + i3;
/* 577 */           k++; if (k == 4) {
/* 578 */             i = 0;
/* 579 */             append((char)m);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 584 */           i = 0;
/* 585 */           if (k > 0) {
/* 586 */             append((char)m);
/*     */ 
/* 588 */             pushChar(n);
/*     */           }
/* 590 */           else if (j == 0) {
/* 591 */             append((char)n);
/*     */           }
/*     */         }
/*     */       }
/* 595 */       else if (j == 0) {
/* 596 */         if (i2 == 1) {
/* 597 */           i = 1;
/* 598 */           m = k = 0;
/*     */         }
/* 600 */         else if (i2 == 3) {
/* 601 */           j = 1;
/* 602 */           pushChar(n);
/*     */         }
/* 604 */         else if (i2 == 4)
/*     */         {
/* 606 */           n = readChar();
/* 607 */           if (n == 42) {
/* 608 */             j = 1;
/* 609 */             readComment();
/* 610 */             this.readWS = true;
/*     */           }
/*     */           else {
/* 613 */             append('/');
/* 614 */             if (n == -1) {
/* 615 */               j = 1;
/*     */             }
/*     */             else
/* 618 */               pushChar(n);
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 623 */           append((char)n);
/* 624 */           if (n == i1) {
/* 625 */             j = 1;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 630 */     return this.tokenBufferLength > 0;
/*     */   }
/*     */ 
/*     */   private void readTill(char paramChar)
/*     */     throws IOException
/*     */   {
/* 638 */     int i = 0;
/* 639 */     int j = 0;
/* 640 */     int k = 0;
/*     */ 
/* 642 */     int n = 0;
/* 643 */     int i1 = paramChar;
/*     */ 
/* 646 */     int i3 = 0;
/*     */ 
/* 648 */     this.tokenBufferLength = 0;
/* 649 */     while (n == 0) {
/* 650 */       int m = readChar();
/*     */       int i2;
/* 651 */       switch (m) {
/*     */       case 92:
/* 653 */         i2 = 1;
/* 654 */         break;
/*     */       case 48:
/*     */       case 49:
/*     */       case 50:
/*     */       case 51:
/*     */       case 52:
/*     */       case 53:
/*     */       case 54:
/*     */       case 55:
/*     */       case 56:
/*     */       case 57:
/* 658 */         i2 = 2;
/* 659 */         i3 = m - 48;
/* 660 */         break;
/*     */       case 97:
/*     */       case 98:
/*     */       case 99:
/*     */       case 100:
/*     */       case 101:
/*     */       case 102:
/* 663 */         i2 = 2;
/* 664 */         i3 = m - 97 + 10;
/* 665 */         break;
/*     */       case 65:
/*     */       case 66:
/*     */       case 67:
/*     */       case 68:
/*     */       case 69:
/*     */       case 70:
/* 668 */         i2 = 2;
/* 669 */         i3 = m - 65 + 10;
/* 670 */         break;
/*     */       case -1:
/* 674 */         throw new RuntimeException("Unclosed " + paramChar);
/*     */       case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       case 12:
/*     */       case 13:
/*     */       case 14:
/*     */       case 15:
/*     */       case 16:
/*     */       case 17:
/*     */       case 18:
/*     */       case 19:
/*     */       case 20:
/*     */       case 21:
/*     */       case 22:
/*     */       case 23:
/*     */       case 24:
/*     */       case 25:
/*     */       case 26:
/*     */       case 27:
/*     */       case 28:
/*     */       case 29:
/*     */       case 30:
/*     */       case 31:
/*     */       case 32:
/*     */       case 33:
/*     */       case 34:
/*     */       case 35:
/*     */       case 36:
/*     */       case 37:
/*     */       case 38:
/*     */       case 39:
/*     */       case 40:
/*     */       case 41:
/*     */       case 42:
/*     */       case 43:
/*     */       case 44:
/*     */       case 45:
/*     */       case 46:
/*     */       case 47:
/*     */       case 58:
/*     */       case 59:
/*     */       case 60:
/*     */       case 61:
/*     */       case 62:
/*     */       case 63:
/*     */       case 64:
/*     */       case 71:
/*     */       case 72:
/*     */       case 73:
/*     */       case 74:
/*     */       case 75:
/*     */       case 76:
/*     */       case 77:
/*     */       case 78:
/*     */       case 79:
/*     */       case 80:
/*     */       case 81:
/*     */       case 82:
/*     */       case 83:
/*     */       case 84:
/*     */       case 85:
/*     */       case 86:
/*     */       case 87:
/*     */       case 88:
/*     */       case 89:
/*     */       case 90:
/*     */       case 91:
/*     */       case 93:
/*     */       case 94:
/*     */       case 95:
/*     */       case 96:
/*     */       default:
/* 677 */         i2 = 0;
/*     */       }
/*     */ 
/* 680 */       if (i != 0) {
/* 681 */         if (i2 == 2)
/*     */         {
/* 683 */           k = k * 16 + i3;
/* 684 */           j++; if (j == 4) {
/* 685 */             i = 0;
/* 686 */             append((char)k);
/*     */           }
/*     */ 
/*     */         }
/* 691 */         else if (j > 0) {
/* 692 */           append((char)k);
/* 693 */           if (i2 == 1) {
/* 694 */             i = 1;
/* 695 */             k = j = 0;
/*     */           }
/*     */           else {
/* 698 */             if (m == i1) {
/* 699 */               n = 1;
/*     */             }
/* 701 */             append((char)m);
/* 702 */             i = 0;
/*     */           }
/*     */         }
/*     */         else {
/* 706 */           append((char)m);
/* 707 */           i = 0;
/*     */         }
/*     */ 
/*     */       }
/* 711 */       else if (i2 == 1) {
/* 712 */         i = 1;
/* 713 */         k = j = 0;
/*     */       }
/*     */       else {
/* 716 */         if (m == i1) {
/* 717 */           n = 1;
/*     */         }
/* 719 */         append((char)m);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void append(char paramChar) {
/* 725 */     if (this.tokenBufferLength == this.tokenBuffer.length) {
/* 726 */       char[] arrayOfChar = new char[this.tokenBuffer.length * 2];
/* 727 */       System.arraycopy(this.tokenBuffer, 0, arrayOfChar, 0, this.tokenBuffer.length);
/* 728 */       this.tokenBuffer = arrayOfChar;
/*     */     }
/* 730 */     this.tokenBuffer[(this.tokenBufferLength++)] = paramChar;
/*     */   }
/*     */ 
/*     */   private void readComment()
/*     */     throws IOException
/*     */   {
/*     */     while (true)
/*     */     {
/* 740 */       int i = readChar();
/* 741 */       switch (i) {
/*     */       case -1:
/* 743 */         throw new RuntimeException("Unclosed comment");
/*     */       case 42:
/* 745 */         i = readChar();
/* 746 */         if (i == 47) {
/* 747 */           return;
/*     */         }
/* 749 */         if (i == -1) {
/* 750 */           throw new RuntimeException("Unclosed comment");
/*     */         }
/*     */ 
/* 753 */         pushChar(i);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void startBlock(int paramInt)
/*     */   {
/* 766 */     if (this.stackCount == this.unitStack.length) {
/* 767 */       int[] arrayOfInt = new int[this.stackCount * 2];
/*     */ 
/* 769 */       System.arraycopy(this.unitStack, 0, arrayOfInt, 0, this.stackCount);
/* 770 */       this.unitStack = arrayOfInt;
/*     */     }
/* 772 */     this.unitStack[(this.stackCount++)] = paramInt;
/*     */   }
/*     */ 
/*     */   private void endBlock(int paramInt)
/*     */   {
/*     */     int i;
/* 781 */     switch (paramInt) {
/*     */     case 3:
/* 783 */       i = 2;
/* 784 */       break;
/*     */     case 5:
/* 786 */       i = 4;
/* 787 */       break;
/*     */     case 7:
/* 789 */       i = 6;
/* 790 */       break;
/*     */     case 4:
/*     */     case 6:
/*     */     default:
/* 793 */       i = -1;
/*     */     }
/*     */ 
/* 796 */     if ((this.stackCount > 0) && (this.unitStack[(this.stackCount - 1)] == i)) {
/* 797 */       this.stackCount -= 1;
/*     */     }
/*     */     else
/*     */     {
/* 801 */       throw new RuntimeException("Unmatched block");
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean inBlock()
/*     */   {
/* 809 */     return this.stackCount > 0;
/*     */   }
/*     */ 
/*     */   private int readWS()
/*     */     throws IOException
/*     */   {
/*     */     int i;
/* 817 */     while (((i = readChar()) != -1) && (Character.isWhitespace((char)i)))
/*     */     {
/* 819 */       this.readWS = true;
/*     */     }
/* 821 */     return i;
/*     */   }
/*     */ 
/*     */   private int readChar()
/*     */     throws IOException
/*     */   {
/* 828 */     if (this.didPushChar) {
/* 829 */       this.didPushChar = false;
/* 830 */       return this.pushedChar;
/*     */     }
/* 832 */     return this.reader.read();
/*     */   }
/*     */ 
/*     */   private void pushChar(int paramInt)
/*     */   {
/* 847 */     if (this.didPushChar)
/*     */     {
/* 849 */       throw new RuntimeException("Can not handle look ahead of more than one character");
/*     */     }
/* 851 */     this.didPushChar = true;
/* 852 */     this.pushedChar = paramInt;
/*     */   }
/*     */ 
/*     */   static abstract interface CSSParserCallback
/*     */   {
/*     */     public abstract void handleImport(String paramString);
/*     */ 
/*     */     public abstract void handleSelector(String paramString);
/*     */ 
/*     */     public abstract void startRule();
/*     */ 
/*     */     public abstract void handleProperty(String paramString);
/*     */ 
/*     */     public abstract void handleValue(String paramString);
/*     */ 
/*     */     public abstract void endRule();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.CSSParser
 * JD-Core Version:    0.6.2
 */