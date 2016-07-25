/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public class Decompiler
/*     */ {
/*     */   public static final int ONLY_BODY_FLAG = 1;
/*     */   public static final int TO_SOURCE_FLAG = 2;
/*     */   public static final int INITIAL_INDENT_PROP = 1;
/*     */   public static final int INDENT_GAP_PROP = 2;
/*     */   public static final int CASE_GAP_PROP = 3;
/*     */   private static final int FUNCTION_END = 163;
/* 915 */   private char[] sourceBuffer = new char[''];
/*     */   private int sourceTop;
/*     */   private static final boolean printSource = false;
/*     */ 
/*     */   String getEncodedSource()
/*     */   {
/* 113 */     return sourceToString(0);
/*     */   }
/*     */ 
/*     */   int getCurrentOffset()
/*     */   {
/* 118 */     return this.sourceTop;
/*     */   }
/*     */ 
/*     */   int markFunctionStart(int paramInt)
/*     */   {
/* 123 */     int i = getCurrentOffset();
/* 124 */     addToken(109);
/* 125 */     append((char)paramInt);
/* 126 */     return i;
/*     */   }
/*     */ 
/*     */   int markFunctionEnd(int paramInt)
/*     */   {
/* 131 */     int i = getCurrentOffset();
/* 132 */     append('£');
/* 133 */     return i;
/*     */   }
/*     */ 
/*     */   void addToken(int paramInt)
/*     */   {
/* 138 */     if ((0 > paramInt) || (paramInt > 162)) {
/* 139 */       throw new IllegalArgumentException();
/*     */     }
/* 141 */     append((char)paramInt);
/*     */   }
/*     */ 
/*     */   void addEOL(int paramInt)
/*     */   {
/* 146 */     if ((0 > paramInt) || (paramInt > 162)) {
/* 147 */       throw new IllegalArgumentException();
/*     */     }
/* 149 */     append((char)paramInt);
/* 150 */     append('\001');
/*     */   }
/*     */ 
/*     */   void addName(String paramString)
/*     */   {
/* 155 */     addToken(39);
/* 156 */     appendString(paramString);
/*     */   }
/*     */ 
/*     */   void addString(String paramString)
/*     */   {
/* 161 */     addToken(41);
/* 162 */     appendString(paramString);
/*     */   }
/*     */ 
/*     */   void addRegexp(String paramString1, String paramString2)
/*     */   {
/* 167 */     addToken(48);
/* 168 */     appendString('/' + paramString1 + '/' + paramString2);
/*     */   }
/*     */ 
/*     */   void addNumber(double paramDouble)
/*     */   {
/* 173 */     addToken(40);
/*     */ 
/* 192 */     long l = ()paramDouble;
/* 193 */     if (l != paramDouble)
/*     */     {
/* 196 */       l = Double.doubleToLongBits(paramDouble);
/* 197 */       append('D');
/* 198 */       append((char)(int)(l >> 48));
/* 199 */       append((char)(int)(l >> 32));
/* 200 */       append((char)(int)(l >> 16));
/* 201 */       append((char)(int)l);
/*     */     }
/*     */     else
/*     */     {
/* 206 */       if (l < 0L) Kit.codeBug();
/*     */ 
/* 210 */       if (l <= 65535L) {
/* 211 */         append('S');
/* 212 */         append((char)(int)l);
/*     */       }
/*     */       else {
/* 215 */         append('J');
/* 216 */         append((char)(int)(l >> 48));
/* 217 */         append((char)(int)(l >> 32));
/* 218 */         append((char)(int)(l >> 16));
/* 219 */         append((char)(int)l);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void appendString(String paramString)
/*     */   {
/* 226 */     int i = paramString.length();
/* 227 */     int j = 1;
/* 228 */     if (i >= 32768) {
/* 229 */       j = 2;
/*     */     }
/* 231 */     int k = this.sourceTop + j + i;
/* 232 */     if (k > this.sourceBuffer.length) {
/* 233 */       increaseSourceCapacity(k);
/*     */     }
/* 235 */     if (i >= 32768)
/*     */     {
/* 238 */       this.sourceBuffer[this.sourceTop] = ((char)(0x8000 | i >>> 16));
/* 239 */       this.sourceTop += 1;
/*     */     }
/* 241 */     this.sourceBuffer[this.sourceTop] = ((char)i);
/* 242 */     this.sourceTop += 1;
/* 243 */     paramString.getChars(0, i, this.sourceBuffer, this.sourceTop);
/* 244 */     this.sourceTop = k;
/*     */   }
/*     */ 
/*     */   private void append(char paramChar)
/*     */   {
/* 249 */     if (this.sourceTop == this.sourceBuffer.length) {
/* 250 */       increaseSourceCapacity(this.sourceTop + 1);
/*     */     }
/* 252 */     this.sourceBuffer[this.sourceTop] = paramChar;
/* 253 */     this.sourceTop += 1;
/*     */   }
/*     */ 
/*     */   private void increaseSourceCapacity(int paramInt)
/*     */   {
/* 259 */     if (paramInt <= this.sourceBuffer.length) Kit.codeBug();
/* 260 */     int i = this.sourceBuffer.length * 2;
/* 261 */     if (i < paramInt) {
/* 262 */       i = paramInt;
/*     */     }
/* 264 */     char[] arrayOfChar = new char[i];
/* 265 */     System.arraycopy(this.sourceBuffer, 0, arrayOfChar, 0, this.sourceTop);
/* 266 */     this.sourceBuffer = arrayOfChar;
/*     */   }
/*     */ 
/*     */   private String sourceToString(int paramInt)
/*     */   {
/* 271 */     if ((paramInt < 0) || (this.sourceTop < paramInt)) Kit.codeBug();
/* 272 */     return new String(this.sourceBuffer, paramInt, this.sourceTop - paramInt);
/*     */   }
/*     */ 
/*     */   public static String decompile(String paramString, int paramInt, UintMap paramUintMap)
/*     */   {
/* 294 */     int i = paramString.length();
/* 295 */     if (i == 0) return "";
/*     */ 
/* 297 */     int j = paramUintMap.getInt(1, 0);
/* 298 */     if (j < 0) throw new IllegalArgumentException();
/* 299 */     int k = paramUintMap.getInt(2, 4);
/* 300 */     if (k < 0) throw new IllegalArgumentException();
/* 301 */     int m = paramUintMap.getInt(3, 2);
/* 302 */     if (m < 0) throw new IllegalArgumentException();
/*     */ 
/* 304 */     StringBuffer localStringBuffer = new StringBuffer();
/* 305 */     int n = 0 != (paramInt & 0x1) ? 1 : 0;
/* 306 */     int i1 = 0 != (paramInt & 0x2) ? 1 : 0;
/*     */ 
/* 335 */     int i2 = 0;
/* 336 */     int i3 = 0;
/* 337 */     int i4 = 0;
/*     */     int i5;
/* 339 */     if (paramString.charAt(i4) == '') {
/* 340 */       i4++;
/* 341 */       i5 = -1;
/*     */     } else {
/* 343 */       i5 = paramString.charAt(i4 + 1);
/*     */     }
/*     */     int i6;
/* 346 */     if (i1 == 0)
/*     */     {
/* 348 */       localStringBuffer.append('\n');
/* 349 */       for (i6 = 0; i6 < j; i6++)
/* 350 */         localStringBuffer.append(' ');
/*     */     }
/* 352 */     else if (i5 == 2) {
/* 353 */       localStringBuffer.append('(');
/*     */     }
/*     */ 
/* 357 */     while (i4 < i) {
/* 358 */       switch (paramString.charAt(i4)) {
/*     */       case '':
/*     */       case '':
/* 361 */         localStringBuffer.append(paramString.charAt(i4) == '' ? "get " : "set ");
/* 362 */         i4++;
/* 363 */         i4 = printSourceString(paramString, i4 + 1, false, localStringBuffer);
/*     */ 
/* 365 */         i4++;
/* 366 */         break;
/*     */       case '\'':
/*     */       case '0':
/* 370 */         i4 = printSourceString(paramString, i4 + 1, false, localStringBuffer);
/* 371 */         break;
/*     */       case ')':
/* 374 */         i4 = printSourceString(paramString, i4 + 1, true, localStringBuffer);
/* 375 */         break;
/*     */       case '(':
/* 378 */         i4 = printSourceNumber(paramString, i4 + 1, localStringBuffer);
/* 379 */         break;
/*     */       case '-':
/* 382 */         localStringBuffer.append("true");
/* 383 */         break;
/*     */       case ',':
/* 386 */         localStringBuffer.append("false");
/* 387 */         break;
/*     */       case '*':
/* 390 */         localStringBuffer.append("null");
/* 391 */         break;
/*     */       case '+':
/* 394 */         localStringBuffer.append("this");
/* 395 */         break;
/*     */       case 'm':
/* 398 */         i4++;
/* 399 */         localStringBuffer.append("function ");
/* 400 */         break;
/*     */       case '£':
/* 404 */         break;
/*     */       case 'Y':
/* 407 */         localStringBuffer.append(", ");
/* 408 */         break;
/*     */       case 'U':
/* 411 */         i2++;
/* 412 */         if (1 == getNext(paramString, i, i4))
/* 413 */           j += k;
/* 414 */         localStringBuffer.append('{');
/* 415 */         break;
/*     */       case 'V':
/* 418 */         i2--;
/*     */ 
/* 423 */         if ((n == 0) || (i2 != 0))
/*     */         {
/* 426 */           localStringBuffer.append('}');
/* 427 */           switch (getNext(paramString, i, i4)) {
/*     */           case 1:
/*     */           case 163:
/* 430 */             j -= k;
/* 431 */             break;
/*     */           case 113:
/*     */           case 117:
/* 434 */             j -= k;
/* 435 */             localStringBuffer.append(' ');
/*     */           }
/*     */         }
/* 438 */         break;
/*     */       case 'W':
/* 441 */         localStringBuffer.append('(');
/* 442 */         break;
/*     */       case 'X':
/* 445 */         localStringBuffer.append(')');
/* 446 */         if (85 == getNext(paramString, i, i4))
/* 447 */           localStringBuffer.append(' '); break;
/*     */       case 'S':
/* 451 */         localStringBuffer.append('[');
/* 452 */         break;
/*     */       case 'T':
/* 455 */         localStringBuffer.append(']');
/* 456 */         break;
/*     */       case '\001':
/* 459 */         if (i1 == 0) {
/* 460 */           i6 = 1;
/* 461 */           if (i3 == 0) {
/* 462 */             i3 = 1;
/* 463 */             if (n != 0)
/*     */             {
/* 467 */               localStringBuffer.setLength(0);
/* 468 */               j -= k;
/* 469 */               i6 = 0;
/*     */             }
/*     */           }
/* 472 */           if (i6 != 0) {
/* 473 */             localStringBuffer.append('\n');
/*     */           }
/*     */ 
/* 480 */           if (i4 + 1 < i) {
/* 481 */             int i7 = 0;
/* 482 */             int i8 = paramString.charAt(i4 + 1);
/* 483 */             if ((i8 == 115) || (i8 == 116))
/*     */             {
/* 486 */               i7 = k - m;
/* 487 */             } else if (i8 == 86) {
/* 488 */               i7 = k;
/*     */             }
/* 494 */             else if (i8 == 39) { int i9 = getSourceStringEnd(paramString, i4 + 2);
/* 496 */               if (paramString.charAt(i9) != 'g'); }
/* 497 */             for (i7 = k; 
/* 500 */               i7 < j; i7++)
/* 501 */               localStringBuffer.append(' '); 
/*     */           }
/*     */         }
/* 502 */         break;
/*     */       case 'l':
/* 506 */         localStringBuffer.append('.');
/* 507 */         break;
/*     */       case '\036':
/* 510 */         localStringBuffer.append("new ");
/* 511 */         break;
/*     */       case '\037':
/* 514 */         localStringBuffer.append("delete ");
/* 515 */         break;
/*     */       case 'p':
/* 518 */         localStringBuffer.append("if ");
/* 519 */         break;
/*     */       case 'q':
/* 522 */         localStringBuffer.append("else ");
/* 523 */         break;
/*     */       case 'w':
/* 526 */         localStringBuffer.append("for ");
/* 527 */         break;
/*     */       case '4':
/* 530 */         localStringBuffer.append(" in ");
/* 531 */         break;
/*     */       case '{':
/* 534 */         localStringBuffer.append("with ");
/* 535 */         break;
/*     */       case 'u':
/* 538 */         localStringBuffer.append("while ");
/* 539 */         break;
/*     */       case 'v':
/* 542 */         localStringBuffer.append("do ");
/* 543 */         break;
/*     */       case 'Q':
/* 546 */         localStringBuffer.append("try ");
/* 547 */         break;
/*     */       case '|':
/* 550 */         localStringBuffer.append("catch ");
/* 551 */         break;
/*     */       case '}':
/* 554 */         localStringBuffer.append("finally ");
/* 555 */         break;
/*     */       case '2':
/* 558 */         localStringBuffer.append("throw ");
/* 559 */         break;
/*     */       case 'r':
/* 562 */         localStringBuffer.append("switch ");
/* 563 */         break;
/*     */       case 'x':
/* 566 */         localStringBuffer.append("break");
/* 567 */         if (39 == getNext(paramString, i, i4))
/* 568 */           localStringBuffer.append(' '); break;
/*     */       case 'y':
/* 572 */         localStringBuffer.append("continue");
/* 573 */         if (39 == getNext(paramString, i, i4))
/* 574 */           localStringBuffer.append(' '); break;
/*     */       case 's':
/* 578 */         localStringBuffer.append("case ");
/* 579 */         break;
/*     */       case 't':
/* 582 */         localStringBuffer.append("default");
/* 583 */         break;
/*     */       case '\004':
/* 586 */         localStringBuffer.append("return");
/* 587 */         if (82 != getNext(paramString, i, i4))
/* 588 */           localStringBuffer.append(' '); break;
/*     */       case 'z':
/* 592 */         localStringBuffer.append("var ");
/* 593 */         break;
/*     */       case '':
/* 596 */         localStringBuffer.append("let ");
/* 597 */         break;
/*     */       case 'R':
/* 600 */         localStringBuffer.append(';');
/* 601 */         if (1 != getNext(paramString, i, i4))
/*     */         {
/* 603 */           localStringBuffer.append(' '); } break;
/*     */       case 'Z':
/* 608 */         localStringBuffer.append(" = ");
/* 609 */         break;
/*     */       case 'a':
/* 612 */         localStringBuffer.append(" += ");
/* 613 */         break;
/*     */       case 'b':
/* 616 */         localStringBuffer.append(" -= ");
/* 617 */         break;
/*     */       case 'c':
/* 620 */         localStringBuffer.append(" *= ");
/* 621 */         break;
/*     */       case 'd':
/* 624 */         localStringBuffer.append(" /= ");
/* 625 */         break;
/*     */       case 'e':
/* 628 */         localStringBuffer.append(" %= ");
/* 629 */         break;
/*     */       case '[':
/* 632 */         localStringBuffer.append(" |= ");
/* 633 */         break;
/*     */       case '\\':
/* 636 */         localStringBuffer.append(" ^= ");
/* 637 */         break;
/*     */       case ']':
/* 640 */         localStringBuffer.append(" &= ");
/* 641 */         break;
/*     */       case '^':
/* 644 */         localStringBuffer.append(" <<= ");
/* 645 */         break;
/*     */       case '_':
/* 648 */         localStringBuffer.append(" >>= ");
/* 649 */         break;
/*     */       case '`':
/* 652 */         localStringBuffer.append(" >>>= ");
/* 653 */         break;
/*     */       case 'f':
/* 656 */         localStringBuffer.append(" ? ");
/* 657 */         break;
/*     */       case 'B':
/* 665 */         localStringBuffer.append(':');
/* 666 */         break;
/*     */       case 'g':
/* 669 */         if (1 == getNext(paramString, i, i4))
/*     */         {
/* 671 */           localStringBuffer.append(':');
/*     */         }
/*     */         else
/* 674 */           localStringBuffer.append(" : ");
/* 675 */         break;
/*     */       case 'h':
/* 678 */         localStringBuffer.append(" || ");
/* 679 */         break;
/*     */       case 'i':
/* 682 */         localStringBuffer.append(" && ");
/* 683 */         break;
/*     */       case '\t':
/* 686 */         localStringBuffer.append(" | ");
/* 687 */         break;
/*     */       case '\n':
/* 690 */         localStringBuffer.append(" ^ ");
/* 691 */         break;
/*     */       case '\013':
/* 694 */         localStringBuffer.append(" & ");
/* 695 */         break;
/*     */       case '.':
/* 698 */         localStringBuffer.append(" === ");
/* 699 */         break;
/*     */       case '/':
/* 702 */         localStringBuffer.append(" !== ");
/* 703 */         break;
/*     */       case '\f':
/* 706 */         localStringBuffer.append(" == ");
/* 707 */         break;
/*     */       case '\r':
/* 710 */         localStringBuffer.append(" != ");
/* 711 */         break;
/*     */       case '\017':
/* 714 */         localStringBuffer.append(" <= ");
/* 715 */         break;
/*     */       case '\016':
/* 718 */         localStringBuffer.append(" < ");
/* 719 */         break;
/*     */       case '\021':
/* 722 */         localStringBuffer.append(" >= ");
/* 723 */         break;
/*     */       case '\020':
/* 726 */         localStringBuffer.append(" > ");
/* 727 */         break;
/*     */       case '5':
/* 730 */         localStringBuffer.append(" instanceof ");
/* 731 */         break;
/*     */       case '\022':
/* 734 */         localStringBuffer.append(" << ");
/* 735 */         break;
/*     */       case '\023':
/* 738 */         localStringBuffer.append(" >> ");
/* 739 */         break;
/*     */       case '\024':
/* 742 */         localStringBuffer.append(" >>> ");
/* 743 */         break;
/*     */       case ' ':
/* 746 */         localStringBuffer.append("typeof ");
/* 747 */         break;
/*     */       case '~':
/* 750 */         localStringBuffer.append("void ");
/* 751 */         break;
/*     */       case '':
/* 754 */         localStringBuffer.append("const ");
/* 755 */         break;
/*     */       case 'H':
/* 758 */         localStringBuffer.append("yield ");
/* 759 */         break;
/*     */       case '\032':
/* 762 */         localStringBuffer.append('!');
/* 763 */         break;
/*     */       case '\033':
/* 766 */         localStringBuffer.append('~');
/* 767 */         break;
/*     */       case '\034':
/* 770 */         localStringBuffer.append('+');
/* 771 */         break;
/*     */       case '\035':
/* 774 */         localStringBuffer.append('-');
/* 775 */         break;
/*     */       case 'j':
/* 778 */         localStringBuffer.append("++");
/* 779 */         break;
/*     */       case 'k':
/* 782 */         localStringBuffer.append("--");
/* 783 */         break;
/*     */       case '\025':
/* 786 */         localStringBuffer.append(" + ");
/* 787 */         break;
/*     */       case '\026':
/* 790 */         localStringBuffer.append(" - ");
/* 791 */         break;
/*     */       case '\027':
/* 794 */         localStringBuffer.append(" * ");
/* 795 */         break;
/*     */       case '\030':
/* 798 */         localStringBuffer.append(" / ");
/* 799 */         break;
/*     */       case '\031':
/* 802 */         localStringBuffer.append(" % ");
/* 803 */         break;
/*     */       case '':
/* 806 */         localStringBuffer.append("::");
/* 807 */         break;
/*     */       case '':
/* 810 */         localStringBuffer.append("..");
/* 811 */         break;
/*     */       case '':
/* 814 */         localStringBuffer.append(".(");
/* 815 */         break;
/*     */       case '':
/* 818 */         localStringBuffer.append('@');
/* 819 */         break;
/*     */       case ' ':
/* 822 */         localStringBuffer.append("debugger;\n");
/* 823 */         break;
/*     */       case '\002':
/*     */       case '\003':
/*     */       case '\005':
/*     */       case '\006':
/*     */       case '\007':
/*     */       case '\b':
/*     */       case '!':
/*     */       case '"':
/*     */       case '#':
/*     */       case '$':
/*     */       case '%':
/*     */       case '&':
/*     */       case '1':
/*     */       case '3':
/*     */       case '6':
/*     */       case '7':
/*     */       case '8':
/*     */       case '9':
/*     */       case ':':
/*     */       case ';':
/*     */       case '<':
/*     */       case '=':
/*     */       case '>':
/*     */       case '?':
/*     */       case '@':
/*     */       case 'A':
/*     */       case 'C':
/*     */       case 'D':
/*     */       case 'E':
/*     */       case 'F':
/*     */       case 'G':
/*     */       case 'I':
/*     */       case 'J':
/*     */       case 'K':
/*     */       case 'L':
/*     */       case 'M':
/*     */       case 'N':
/*     */       case 'O':
/*     */       case 'P':
/*     */       case 'n':
/*     */       case 'o':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '':
/*     */       case '¡':
/*     */       case '¢':
/*     */       default:
/* 827 */         throw new RuntimeException("Token: " + Token.name(paramString.charAt(i4)));
/*     */ 
/* 830 */         i4++;
/*     */       }
/*     */     }
/* 833 */     if (i1 == 0)
/*     */     {
/* 835 */       if (n == 0)
/* 836 */         localStringBuffer.append('\n');
/*     */     }
/* 838 */     else if (i5 == 2) {
/* 839 */       localStringBuffer.append(')');
/*     */     }
/*     */ 
/* 843 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static int getNext(String paramString, int paramInt1, int paramInt2)
/*     */   {
/* 848 */     return paramInt2 + 1 < paramInt1 ? paramString.charAt(paramInt2 + 1) : 0;
/*     */   }
/*     */ 
/*     */   private static int getSourceStringEnd(String paramString, int paramInt)
/*     */   {
/* 853 */     return printSourceString(paramString, paramInt, false, null);
/*     */   }
/*     */ 
/*     */   private static int printSourceString(String paramString, int paramInt, boolean paramBoolean, StringBuffer paramStringBuffer)
/*     */   {
/* 860 */     int i = paramString.charAt(paramInt);
/* 861 */     paramInt++;
/* 862 */     if ((0x8000 & i) != 0) {
/* 863 */       i = (0x7FFF & i) << 16 | paramString.charAt(paramInt);
/* 864 */       paramInt++;
/*     */     }
/* 866 */     if (paramStringBuffer != null) {
/* 867 */       String str = paramString.substring(paramInt, paramInt + i);
/* 868 */       if (!paramBoolean) {
/* 869 */         paramStringBuffer.append(str);
/*     */       } else {
/* 871 */         paramStringBuffer.append('"');
/* 872 */         paramStringBuffer.append(ScriptRuntime.escapeString(str));
/* 873 */         paramStringBuffer.append('"');
/*     */       }
/*     */     }
/* 876 */     return paramInt + i;
/*     */   }
/*     */ 
/*     */   private static int printSourceNumber(String paramString, int paramInt, StringBuffer paramStringBuffer)
/*     */   {
/* 882 */     double d = 0.0D;
/* 883 */     int i = paramString.charAt(paramInt);
/* 884 */     paramInt++;
/* 885 */     if (i == 83) {
/* 886 */       if (paramStringBuffer != null) {
/* 887 */         int j = paramString.charAt(paramInt);
/* 888 */         d = j;
/*     */       }
/* 890 */       paramInt++;
/* 891 */     } else if ((i == 74) || (i == 68)) {
/* 892 */       if (paramStringBuffer != null)
/*     */       {
/* 894 */         long l = paramString.charAt(paramInt) << 48;
/* 895 */         l |= paramString.charAt(paramInt + 1) << 32;
/* 896 */         l |= paramString.charAt(paramInt + 2) << 16;
/* 897 */         l |= paramString.charAt(paramInt + 3);
/* 898 */         if (i == 74)
/* 899 */           d = l;
/*     */         else {
/* 901 */           d = Double.longBitsToDouble(l);
/*     */         }
/*     */       }
/* 904 */       paramInt += 4;
/*     */     }
/*     */     else {
/* 907 */       throw new RuntimeException();
/*     */     }
/* 909 */     if (paramStringBuffer != null) {
/* 910 */       paramStringBuffer.append(ScriptRuntime.numberToString(d, 10));
/*     */     }
/* 912 */     return paramInt;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.Decompiler
 * JD-Core Version:    0.6.2
 */