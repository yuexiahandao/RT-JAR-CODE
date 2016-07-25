/*     */ package java.io;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class StreamTokenizer
/*     */ {
/*  68 */   private Reader reader = null;
/*  69 */   private InputStream input = null;
/*     */ 
/*  71 */   private char[] buf = new char[20];
/*     */ 
/*  80 */   private int peekc = 2147483647;
/*     */   private static final int NEED_CHAR = 2147483647;
/*     */   private static final int SKIP_LF = 2147483646;
/*     */   private boolean pushedBack;
/*     */   private boolean forceLower;
/*  88 */   private int LINENO = 1;
/*     */ 
/*  90 */   private boolean eolIsSignificantP = false;
/*  91 */   private boolean slashSlashCommentsP = false;
/*  92 */   private boolean slashStarCommentsP = false;
/*     */ 
/*  94 */   private byte[] ctype = new byte[256];
/*     */   private static final byte CT_WHITESPACE = 1;
/*     */   private static final byte CT_DIGIT = 2;
/*     */   private static final byte CT_ALPHA = 4;
/*     */   private static final byte CT_QUOTE = 8;
/*     */   private static final byte CT_COMMENT = 16;
/* 128 */   public int ttype = -4;
/*     */   public static final int TT_EOF = -1;
/*     */   public static final int TT_EOL = 10;
/*     */   public static final int TT_NUMBER = -2;
/*     */   public static final int TT_WORD = -3;
/*     */   private static final int TT_NOTHING = -4;
/*     */   public String sval;
/*     */   public double nval;
/*     */ 
/*     */   private StreamTokenizer()
/*     */   {
/* 189 */     wordChars(97, 122);
/* 190 */     wordChars(65, 90);
/* 191 */     wordChars(160, 255);
/* 192 */     whitespaceChars(0, 32);
/* 193 */     commentChar(47);
/* 194 */     quoteChar(34);
/* 195 */     quoteChar(39);
/* 196 */     parseNumbers();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public StreamTokenizer(InputStream paramInputStream)
/*     */   {
/* 232 */     this();
/* 233 */     if (paramInputStream == null) {
/* 234 */       throw new NullPointerException();
/*     */     }
/* 236 */     this.input = paramInputStream;
/*     */   }
/*     */ 
/*     */   public StreamTokenizer(Reader paramReader)
/*     */   {
/* 246 */     this();
/* 247 */     if (paramReader == null) {
/* 248 */       throw new NullPointerException();
/*     */     }
/* 250 */     this.reader = paramReader;
/*     */   }
/*     */ 
/*     */   public void resetSyntax()
/*     */   {
/* 261 */     int i = this.ctype.length;
/*     */     while (true) { i--; if (i < 0) break;
/* 262 */       this.ctype[i] = 0; }  } 
/*     */   public void wordChars(int paramInt1, int paramInt2) { // Byte code:
/*     */     //   0: iload_1
/*     */     //   1: ifge +5 -> 6
/*     */     //   4: iconst_0
/*     */     //   5: istore_1
/*     */     //   6: iload_2
/*     */     //   7: aload_0
/*     */     //   8: getfield 167	java/io/StreamTokenizer:ctype	[B
/*     */     //   11: arraylength
/*     */     //   12: if_icmplt +11 -> 23
/*     */     //   15: aload_0
/*     */     //   16: getfield 167	java/io/StreamTokenizer:ctype	[B
/*     */     //   19: arraylength
/*     */     //   20: iconst_1
/*     */     //   21: isub
/*     */     //   22: istore_2
/*     */     //   23: iload_1
/*     */     //   24: iload_2
/*     */     //   25: if_icmpgt +20 -> 45
/*     */     //   28: aload_0
/*     */     //   29: getfield 167	java/io/StreamTokenizer:ctype	[B
/*     */     //   32: iload_1
/*     */     //   33: iinc 1 1
/*     */     //   36: dup2
/*     */     //   37: baload
/*     */     //   38: iconst_4
/*     */     //   39: ior
/*     */     //   40: i2b
/*     */     //   41: bastore
/*     */     //   42: goto -19 -> 23
/*     */     //   45: return } 
/* 296 */   public void whitespaceChars(int paramInt1, int paramInt2) { if (paramInt1 < 0)
/* 297 */       paramInt1 = 0;
/* 298 */     if (paramInt2 >= this.ctype.length)
/* 299 */       paramInt2 = this.ctype.length - 1;
/* 300 */     while (paramInt1 <= paramInt2)
/* 301 */       this.ctype[(paramInt1++)] = 1;
/*     */   }
/*     */ 
/*     */   public void ordinaryChars(int paramInt1, int paramInt2)
/*     */   {
/* 316 */     if (paramInt1 < 0)
/* 317 */       paramInt1 = 0;
/* 318 */     if (paramInt2 >= this.ctype.length)
/* 319 */       paramInt2 = this.ctype.length - 1;
/* 320 */     while (paramInt1 <= paramInt2)
/* 321 */       this.ctype[(paramInt1++)] = 0;
/*     */   }
/*     */ 
/*     */   public void ordinaryChar(int paramInt)
/*     */   {
/* 342 */     if ((paramInt >= 0) && (paramInt < this.ctype.length))
/* 343 */       this.ctype[paramInt] = 0;
/*     */   }
/*     */ 
/*     */   public void commentChar(int paramInt)
/*     */   {
/* 356 */     if ((paramInt >= 0) && (paramInt < this.ctype.length))
/* 357 */       this.ctype[paramInt] = 16;
/*     */   }
/*     */ 
/*     */   public void quoteChar(int paramInt)
/*     */   {
/* 385 */     if ((paramInt >= 0) && (paramInt < this.ctype.length))
/* 386 */       this.ctype[paramInt] = 8;
/*     */   }
/*     */ 
/*     */   public void parseNumbers()
/*     */   {
/* 410 */     for (int i = 48; i <= 57; i++)
/*     */     {
/*     */       int tmp14_13 = i;
/*     */       byte[] tmp14_10 = this.ctype; tmp14_10[tmp14_13] = ((byte)(tmp14_10[tmp14_13] | 0x2));
/*     */     }
/*     */     byte[] tmp32_27 = this.ctype; tmp32_27[46] = ((byte)(tmp32_27[46] | 0x2));
/*     */     byte[] tmp44_39 = this.ctype; tmp44_39[45] = ((byte)(tmp44_39[45] | 0x2));
/*     */   }
/*     */ 
/*     */   public void eolIsSignificant(boolean paramBoolean)
/*     */   {
/* 440 */     this.eolIsSignificantP = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void slashStarComments(boolean paramBoolean)
/*     */   {
/* 456 */     this.slashStarCommentsP = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void slashSlashComments(boolean paramBoolean)
/*     */   {
/* 473 */     this.slashSlashCommentsP = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void lowerCaseMode(boolean paramBoolean)
/*     */   {
/* 494 */     this.forceLower = paramBoolean;
/*     */   }
/*     */ 
/*     */   private int read() throws IOException
/*     */   {
/* 499 */     if (this.reader != null)
/* 500 */       return this.reader.read();
/* 501 */     if (this.input != null) {
/* 502 */       return this.input.read();
/*     */     }
/* 504 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   public int nextToken()
/*     */     throws IOException
/*     */   {
/* 526 */     if (this.pushedBack) {
/* 527 */       this.pushedBack = false;
/* 528 */       return this.ttype;
/*     */     }
/* 530 */     byte[] arrayOfByte = this.ctype;
/* 531 */     this.sval = null;
/*     */ 
/* 533 */     int i = this.peekc;
/* 534 */     if (i < 0)
/* 535 */       i = 2147483647;
/* 536 */     if (i == 2147483646) {
/* 537 */       i = read();
/* 538 */       if (i < 0)
/* 539 */         return this.ttype = -1;
/* 540 */       if (i == 10)
/* 541 */         i = 2147483647;
/*     */     }
/* 543 */     if (i == 2147483647) {
/* 544 */       i = read();
/* 545 */       if (i < 0)
/* 546 */         return this.ttype = -1;
/*     */     }
/* 548 */     this.ttype = i;
/*     */ 
/* 553 */     this.peekc = 2147483647;
/*     */ 
/* 555 */     int j = i < 256 ? arrayOfByte[i] : 4;
/* 556 */     while ((j & 0x1) != 0) {
/* 557 */       if (i == 13) {
/* 558 */         this.LINENO += 1;
/* 559 */         if (this.eolIsSignificantP) {
/* 560 */           this.peekc = 2147483646;
/* 561 */           return this.ttype = 10;
/*     */         }
/* 563 */         i = read();
/* 564 */         if (i == 10)
/* 565 */           i = read();
/*     */       } else {
/* 567 */         if (i == 10) {
/* 568 */           this.LINENO += 1;
/* 569 */           if (this.eolIsSignificantP) {
/* 570 */             return this.ttype = 10;
/*     */           }
/*     */         }
/* 573 */         i = read();
/*     */       }
/* 575 */       if (i < 0)
/* 576 */         return this.ttype = -1;
/* 577 */       j = i < 256 ? arrayOfByte[i] : 4;
/*     */     }
/*     */     int k;
/*     */     int i1;
/* 580 */     if ((j & 0x2) != 0) {
/* 581 */       k = 0;
/* 582 */       if (i == 45) {
/* 583 */         i = read();
/* 584 */         if ((i != 46) && ((i < 48) || (i > 57))) {
/* 585 */           this.peekc = i;
/* 586 */           return this.ttype = 45;
/*     */         }
/* 588 */         k = 1;
/*     */       }
/* 590 */       double d1 = 0.0D;
/* 591 */       i1 = 0;
/* 592 */       int i2 = 0;
/*     */       while (true) {
/* 594 */         if ((i == 46) && (i2 == 0)) {
/* 595 */           i2 = 1; } else {
/* 596 */           if ((48 > i) || (i > 57)) break;
/* 597 */           d1 = d1 * 10.0D + (i - 48);
/* 598 */           i1 += i2;
/*     */         }
/*     */ 
/* 601 */         i = read();
/*     */       }
/* 603 */       this.peekc = i;
/* 604 */       if (i1 != 0) {
/* 605 */         double d2 = 10.0D;
/* 606 */         i1--;
/* 607 */         while (i1 > 0) {
/* 608 */           d2 *= 10.0D;
/* 609 */           i1--;
/*     */         }
/*     */ 
/* 612 */         d1 /= d2;
/*     */       }
/* 614 */       this.nval = (k != 0 ? -d1 : d1);
/* 615 */       return this.ttype = -2;
/*     */     }
/*     */ 
/* 618 */     if ((j & 0x4) != 0) {
/* 619 */       k = 0;
/*     */       do {
/* 621 */         if (k >= this.buf.length) {
/* 622 */           this.buf = Arrays.copyOf(this.buf, this.buf.length * 2);
/*     */         }
/* 624 */         this.buf[(k++)] = ((char)i);
/* 625 */         i = read();
/* 626 */         j = i < 256 ? arrayOfByte[i] : i < 0 ? 1 : 4;
/* 627 */       }while ((j & 0x6) != 0);
/* 628 */       this.peekc = i;
/* 629 */       this.sval = String.copyValueOf(this.buf, 0, k);
/* 630 */       if (this.forceLower)
/* 631 */         this.sval = this.sval.toLowerCase();
/* 632 */       return this.ttype = -3;
/*     */     }
/*     */ 
/* 635 */     if ((j & 0x8) != 0) {
/* 636 */       this.ttype = i;
/* 637 */       k = 0;
/*     */ 
/* 642 */       int m = read();
/* 643 */       while ((m >= 0) && (m != this.ttype) && (m != 10) && (m != 13)) {
/* 644 */         if (m == 92) {
/* 645 */           i = read();
/* 646 */           int n = i;
/* 647 */           if ((i >= 48) && (i <= 55)) {
/* 648 */             i -= 48;
/* 649 */             i1 = read();
/* 650 */             if ((48 <= i1) && (i1 <= 55)) {
/* 651 */               i = (i << 3) + (i1 - 48);
/* 652 */               i1 = read();
/* 653 */               if ((48 <= i1) && (i1 <= 55) && (n <= 51)) {
/* 654 */                 i = (i << 3) + (i1 - 48);
/* 655 */                 m = read();
/*     */               } else {
/* 657 */                 m = i1;
/*     */               }
/*     */             } else { m = i1; }
/*     */           } else {
/* 661 */             switch (i) {
/*     */             case 97:
/* 663 */               i = 7;
/* 664 */               break;
/*     */             case 98:
/* 666 */               i = 8;
/* 667 */               break;
/*     */             case 102:
/* 669 */               i = 12;
/* 670 */               break;
/*     */             case 110:
/* 672 */               i = 10;
/* 673 */               break;
/*     */             case 114:
/* 675 */               i = 13;
/* 676 */               break;
/*     */             case 116:
/* 678 */               i = 9;
/* 679 */               break;
/*     */             case 118:
/* 681 */               i = 11;
/*     */             case 99:
/*     */             case 100:
/*     */             case 101:
/*     */             case 103:
/*     */             case 104:
/*     */             case 105:
/*     */             case 106:
/*     */             case 107:
/*     */             case 108:
/*     */             case 109:
/*     */             case 111:
/*     */             case 112:
/*     */             case 113:
/*     */             case 115:
/* 684 */             case 117: } m = read();
/*     */           }
/*     */         } else {
/* 687 */           i = m;
/* 688 */           m = read();
/*     */         }
/* 690 */         if (k >= this.buf.length) {
/* 691 */           this.buf = Arrays.copyOf(this.buf, this.buf.length * 2);
/*     */         }
/* 693 */         this.buf[(k++)] = ((char)i);
/*     */       }
/*     */ 
/* 700 */       this.peekc = (m == this.ttype ? 2147483647 : m);
/*     */ 
/* 702 */       this.sval = String.copyValueOf(this.buf, 0, k);
/* 703 */       return this.ttype;
/*     */     }
/*     */ 
/* 706 */     if ((i == 47) && ((this.slashSlashCommentsP) || (this.slashStarCommentsP))) {
/* 707 */       i = read();
/* 708 */       if ((i == 42) && (this.slashStarCommentsP)) {
/* 709 */         k = 0;
/* 710 */         while (((i = read()) != 47) || (k != 42)) {
/* 711 */           if (i == 13) {
/* 712 */             this.LINENO += 1;
/* 713 */             i = read();
/* 714 */             if (i == 10) {
/* 715 */               i = read();
/*     */             }
/*     */           }
/* 718 */           else if (i == 10) {
/* 719 */             this.LINENO += 1;
/* 720 */             i = read();
/*     */           }
/*     */ 
/* 723 */           if (i < 0)
/* 724 */             return this.ttype = -1;
/* 725 */           k = i;
/*     */         }
/* 727 */         return nextToken();
/* 728 */       }if ((i == 47) && (this.slashSlashCommentsP)) {
/* 729 */         while (((i = read()) != 10) && (i != 13) && (i >= 0));
/* 730 */         this.peekc = i;
/* 731 */         return nextToken();
/*     */       }
/*     */ 
/* 734 */       if ((arrayOfByte[47] & 0x10) != 0) {
/* 735 */         while (((i = read()) != 10) && (i != 13) && (i >= 0));
/* 736 */         this.peekc = i;
/* 737 */         return nextToken();
/*     */       }
/* 739 */       this.peekc = i;
/* 740 */       return this.ttype = 47;
/*     */     }
/*     */ 
/* 745 */     if ((j & 0x10) != 0) {
/* 746 */       while (((i = read()) != 10) && (i != 13) && (i >= 0));
/* 747 */       this.peekc = i;
/* 748 */       return nextToken();
/*     */     }
/*     */ 
/* 751 */     return this.ttype = i;
/*     */   }
/*     */ 
/*     */   public void pushBack()
/*     */   {
/* 766 */     if (this.ttype != -4)
/* 767 */       this.pushedBack = true;
/*     */   }
/*     */ 
/*     */   public int lineno()
/*     */   {
/* 776 */     return this.LINENO;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*     */     String str;
/* 795 */     switch (this.ttype) {
/*     */     case -1:
/* 797 */       str = "EOF";
/* 798 */       break;
/*     */     case 10:
/* 800 */       str = "EOL";
/* 801 */       break;
/*     */     case -3:
/* 803 */       str = this.sval;
/* 804 */       break;
/*     */     case -2:
/* 806 */       str = "n=" + this.nval;
/* 807 */       break;
/*     */     case -4:
/* 809 */       str = "NOTHING";
/* 810 */       break;
/*     */     case 0:
/*     */     case 1:
/*     */     case 2:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/*     */     case 6:
/*     */     case 7:
/*     */     case 8:
/*     */     case 9:
/*     */     default:
/* 818 */       if ((this.ttype < 256) && ((this.ctype[this.ttype] & 0x8) != 0))
/*     */       {
/* 820 */         str = this.sval;
/*     */       }
/*     */       else
/*     */       {
/* 824 */         char[] arrayOfChar = new char[3];
/* 825 */         arrayOfChar[2] = 39; arrayOfChar[0] = 39;
/* 826 */         arrayOfChar[1] = ((char)this.ttype);
/* 827 */         str = new String(arrayOfChar);
/* 828 */       }break;
/*     */     }
/*     */ 
/* 831 */     return "Token[" + str + "], line " + this.LINENO;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.StreamTokenizer
 * JD-Core Version:    0.6.2
 */