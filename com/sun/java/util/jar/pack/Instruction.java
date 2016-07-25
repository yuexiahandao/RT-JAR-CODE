/*     */ package com.sun.java.util.jar.pack;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ class Instruction
/*     */ {
/*     */   protected byte[] bytes;
/*     */   protected int pc;
/*     */   protected int bc;
/*     */   protected int w;
/*     */   protected int length;
/*     */   protected boolean special;
/*     */   private static final byte[][] BC_LENGTH;
/*     */   private static final byte[][] BC_INDEX;
/*     */   private static final byte[][] BC_TAG;
/*     */   private static final byte[][] BC_BRANCH;
/*     */   private static final byte[][] BC_SLOT;
/*     */   private static final byte[][] BC_CON;
/*     */   private static final String[] BC_NAME;
/*     */   private static final String[][] BC_FORMAT;
/* 599 */   private static int BW = 4;
/*     */ 
/*     */   protected Instruction(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/*  47 */     reset(paramArrayOfByte, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */   private void reset(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  50 */     this.bytes = paramArrayOfByte;
/*  51 */     this.pc = paramInt1;
/*  52 */     this.bc = paramInt2;
/*  53 */     this.w = paramInt3;
/*  54 */     this.length = paramInt4;
/*     */   }
/*     */ 
/*     */   public int getBC() {
/*  58 */     return this.bc;
/*     */   }
/*     */   public boolean isWide() {
/*  61 */     return this.w != 0;
/*     */   }
/*     */   public byte[] getBytes() {
/*  64 */     return this.bytes;
/*     */   }
/*     */   public int getPC() {
/*  67 */     return this.pc;
/*     */   }
/*     */   public int getLength() {
/*  70 */     return this.length;
/*     */   }
/*     */   public int getNextPC() {
/*  73 */     return this.pc + this.length;
/*     */   }
/*     */ 
/*     */   public Instruction next() {
/*  77 */     int i = this.pc + this.length;
/*  78 */     if (i == this.bytes.length) {
/*  79 */       return null;
/*     */     }
/*  81 */     return at(this.bytes, i, this);
/*     */   }
/*     */ 
/*     */   public boolean isNonstandard() {
/*  85 */     return isNonstandard(this.bc);
/*     */   }
/*     */ 
/*     */   public void setNonstandardLength(int paramInt) {
/*  89 */     assert (isNonstandard());
/*  90 */     this.length = paramInt;
/*     */   }
/*     */ 
/*     */   public Instruction forceNextPC(int paramInt)
/*     */   {
/*  96 */     int i = paramInt - this.pc;
/*  97 */     return new Instruction(this.bytes, this.pc, -1, -1, i);
/*     */   }
/*     */ 
/*     */   public static Instruction at(byte[] paramArrayOfByte, int paramInt) {
/* 101 */     return at(paramArrayOfByte, paramInt, null);
/*     */   }
/*     */ 
/*     */   public static Instruction at(byte[] paramArrayOfByte, int paramInt, Instruction paramInstruction) {
/* 105 */     int i = getByte(paramArrayOfByte, paramInt);
/* 106 */     int j = -1;
/* 107 */     int k = 0;
/* 108 */     int m = BC_LENGTH[k][i];
/* 109 */     if (m == 0)
/*     */     {
/* 111 */       switch (i) {
/*     */       case 196:
/* 113 */         i = getByte(paramArrayOfByte, paramInt + 1);
/* 114 */         k = 1;
/* 115 */         m = BC_LENGTH[k][i];
/* 116 */         if (m == 0)
/*     */         {
/* 118 */           m = 1; } break;
/*     */       case 170:
/* 122 */         return new TableSwitch(paramArrayOfByte, paramInt);
/*     */       case 171:
/* 124 */         return new LookupSwitch(paramArrayOfByte, paramInt);
/*     */       default:
/* 127 */         m = 1;
/*     */       }
/*     */     }
/*     */ 
/* 131 */     assert (m > 0);
/* 132 */     assert (paramInt + m <= paramArrayOfByte.length);
/*     */ 
/* 134 */     if ((paramInstruction != null) && (!paramInstruction.special)) {
/* 135 */       paramInstruction.reset(paramArrayOfByte, paramInt, i, k, m);
/* 136 */       return paramInstruction;
/*     */     }
/* 138 */     return new Instruction(paramArrayOfByte, paramInt, i, k, m);
/*     */   }
/*     */ 
/*     */   public byte getCPTag()
/*     */   {
/* 143 */     return BC_TAG[this.w][this.bc];
/*     */   }
/*     */ 
/*     */   public int getCPIndex()
/*     */   {
/* 148 */     int i = BC_INDEX[this.w][this.bc];
/* 149 */     if (i == 0) return -1;
/* 150 */     assert (this.w == 0);
/* 151 */     if (this.length == 2) {
/* 152 */       return getByte(this.bytes, this.pc + i);
/*     */     }
/* 154 */     return getShort(this.bytes, this.pc + i);
/*     */   }
/*     */ 
/*     */   public void setCPIndex(int paramInt) {
/* 158 */     int i = BC_INDEX[this.w][this.bc];
/* 159 */     assert (i != 0);
/* 160 */     if (this.length == 2)
/* 161 */       setByte(this.bytes, this.pc + i, paramInt);
/*     */     else
/* 163 */       setShort(this.bytes, this.pc + i, paramInt);
/* 164 */     assert (getCPIndex() == paramInt);
/*     */   }
/*     */ 
/*     */   public ConstantPool.Entry getCPRef(ConstantPool.Entry[] paramArrayOfEntry) {
/* 168 */     int i = getCPIndex();
/* 169 */     return i < 0 ? null : paramArrayOfEntry[i];
/*     */   }
/*     */ 
/*     */   public int getLocalSlot()
/*     */   {
/* 174 */     int i = BC_SLOT[this.w][this.bc];
/* 175 */     if (i == 0) return -1;
/* 176 */     if (this.w == 0) {
/* 177 */       return getByte(this.bytes, this.pc + i);
/*     */     }
/* 179 */     return getShort(this.bytes, this.pc + i);
/*     */   }
/*     */ 
/*     */   public int getBranchLabel()
/*     */   {
/* 184 */     int i = BC_BRANCH[this.w][this.bc];
/* 185 */     if (i == 0) return -1;
/* 186 */     assert (this.w == 0);
/* 187 */     assert ((this.length == 3) || (this.length == 5));
/*     */     int j;
/* 189 */     if (this.length == 3)
/* 190 */       j = (short)getShort(this.bytes, this.pc + i);
/*     */     else
/* 192 */       j = getInt(this.bytes, this.pc + i);
/* 193 */     assert (j + this.pc >= 0);
/* 194 */     assert (j + this.pc <= this.bytes.length);
/* 195 */     return j + this.pc;
/*     */   }
/*     */ 
/*     */   public void setBranchLabel(int paramInt) {
/* 199 */     int i = BC_BRANCH[this.w][this.bc];
/* 200 */     assert (i != 0);
/* 201 */     if (this.length == 3)
/* 202 */       setShort(this.bytes, this.pc + i, paramInt - this.pc);
/*     */     else
/* 204 */       setInt(this.bytes, this.pc + i, paramInt - this.pc);
/* 205 */     assert (paramInt == getBranchLabel());
/*     */   }
/*     */ 
/*     */   public int getConstant()
/*     */   {
/* 211 */     int i = BC_CON[this.w][this.bc];
/* 212 */     if (i == 0) return 0;
/* 213 */     switch (this.length - i) { case 1:
/* 214 */       return (byte)getByte(this.bytes, this.pc + i);
/*     */     case 2:
/* 215 */       return (short)getShort(this.bytes, this.pc + i);
/*     */     }
/* 217 */     if (!$assertionsDisabled) throw new AssertionError();
/* 218 */     return 0;
/*     */   }
/*     */ 
/*     */   public void setConstant(int paramInt) {
/* 222 */     int i = BC_CON[this.w][this.bc];
/* 223 */     assert (i != 0);
/* 224 */     switch (this.length - i) { case 1:
/* 225 */       setByte(this.bytes, this.pc + i, paramInt); break;
/*     */     case 2:
/* 226 */       setShort(this.bytes, this.pc + i, paramInt);
/*     */     }
/* 228 */     assert (paramInt == getConstant());
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 322 */     return (paramObject != null) && (paramObject.getClass() == Instruction.class) && (equals((Instruction)paramObject));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 327 */     int i = 3;
/* 328 */     i = 11 * i + Arrays.hashCode(this.bytes);
/* 329 */     i = 11 * i + this.pc;
/* 330 */     i = 11 * i + this.bc;
/* 331 */     i = 11 * i + this.w;
/* 332 */     i = 11 * i + this.length;
/* 333 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean equals(Instruction paramInstruction) {
/* 337 */     if (this.pc != paramInstruction.pc) return false;
/* 338 */     if (this.bc != paramInstruction.bc) return false;
/* 339 */     if (this.w != paramInstruction.w) return false;
/* 340 */     if (this.length != paramInstruction.length) return false;
/* 341 */     for (int i = 1; i < this.length; i++) {
/* 342 */       if (this.bytes[(this.pc + i)] != paramInstruction.bytes[(paramInstruction.pc + i)])
/* 343 */         return false;
/*     */     }
/* 345 */     return true;
/*     */   }
/*     */ 
/*     */   static String labstr(int paramInt) {
/* 349 */     if ((paramInt >= 0) && (paramInt < 100000))
/* 350 */       return (100000 + paramInt + "").substring(1);
/* 351 */     return paramInt + "";
/*     */   }
/*     */   public String toString() {
/* 354 */     return toString(null);
/*     */   }
/*     */   public String toString(ConstantPool.Entry[] paramArrayOfEntry) {
/* 357 */     String str1 = labstr(this.pc) + ": ";
/* 358 */     if (this.bc >= 202) {
/* 359 */       str1 = str1 + Integer.toHexString(this.bc);
/* 360 */       return str1;
/*     */     }
/* 362 */     if (this.w == 1) str1 = str1 + "wide ";
/* 363 */     String str2 = this.bc < BC_NAME.length ? BC_NAME[this.bc] : null;
/* 364 */     if (str2 == null) {
/* 365 */       return str1 + "opcode#" + this.bc;
/*     */     }
/* 367 */     str1 = str1 + str2;
/* 368 */     int i = getCPTag();
/* 369 */     if (i != 0) str1 = str1 + " " + ConstantPool.tagName(i) + ":";
/* 370 */     int j = getCPIndex();
/* 371 */     if (j >= 0) str1 = str1 + (paramArrayOfEntry == null ? "" + j : new StringBuilder().append("=").append(paramArrayOfEntry[j].stringValue()).toString());
/* 372 */     int k = getLocalSlot();
/* 373 */     if (k >= 0) str1 = str1 + " Local:" + k;
/* 374 */     int m = getBranchLabel();
/* 375 */     if (m >= 0) str1 = str1 + " To:" + labstr(m);
/* 376 */     int n = getConstant();
/* 377 */     if (n != 0) str1 = str1 + " Con:" + n;
/* 378 */     return str1;
/*     */   }
/*     */ 
/*     */   public int getIntAt(int paramInt)
/*     */   {
/* 387 */     return getInt(this.bytes, this.pc + paramInt);
/*     */   }
/*     */   public int getShortAt(int paramInt) {
/* 390 */     return getShort(this.bytes, this.pc + paramInt);
/*     */   }
/*     */   public int getByteAt(int paramInt) {
/* 393 */     return getByte(this.bytes, this.pc + paramInt);
/*     */   }
/*     */ 
/*     */   public static int getInt(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 398 */     return (getShort(paramArrayOfByte, paramInt + 0) << 16) + (getShort(paramArrayOfByte, paramInt + 2) << 0);
/*     */   }
/*     */   public static int getShort(byte[] paramArrayOfByte, int paramInt) {
/* 401 */     return (getByte(paramArrayOfByte, paramInt + 0) << 8) + (getByte(paramArrayOfByte, paramInt + 1) << 0);
/*     */   }
/*     */   public static int getByte(byte[] paramArrayOfByte, int paramInt) {
/* 404 */     return paramArrayOfByte[paramInt] & 0xFF;
/*     */   }
/*     */ 
/*     */   public static void setInt(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 409 */     setShort(paramArrayOfByte, paramInt1 + 0, paramInt2 >> 16);
/* 410 */     setShort(paramArrayOfByte, paramInt1 + 2, paramInt2 >> 0);
/*     */   }
/*     */   public static void setShort(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 413 */     setByte(paramArrayOfByte, paramInt1 + 0, paramInt2 >> 8);
/* 414 */     setByte(paramArrayOfByte, paramInt1 + 1, paramInt2 >> 0);
/*     */   }
/*     */   public static void setByte(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 417 */     paramArrayOfByte[paramInt1] = ((byte)paramInt2);
/*     */   }
/*     */ 
/*     */   public static boolean isNonstandard(int paramInt)
/*     */   {
/* 424 */     return BC_LENGTH[0][paramInt] < 0;
/*     */   }
/*     */ 
/*     */   public static int opLength(int paramInt) {
/* 428 */     int i = BC_LENGTH[0][paramInt];
/* 429 */     assert (i > 0);
/* 430 */     return i;
/*     */   }
/*     */   public static int opWideLength(int paramInt) {
/* 433 */     int i = BC_LENGTH[1][paramInt];
/* 434 */     assert (i > 0);
/* 435 */     return i;
/*     */   }
/*     */ 
/*     */   public static boolean isLocalSlotOp(int paramInt) {
/* 439 */     return (paramInt < BC_SLOT[0].length) && (BC_SLOT[0][paramInt] > 0);
/*     */   }
/*     */ 
/*     */   public static boolean isBranchOp(int paramInt) {
/* 443 */     return (paramInt < BC_BRANCH[0].length) && (BC_BRANCH[0][paramInt] > 0);
/*     */   }
/*     */ 
/*     */   public static boolean isCPRefOp(int paramInt) {
/* 447 */     if ((paramInt < BC_INDEX[0].length) && (BC_INDEX[0][paramInt] > 0)) return true;
/* 448 */     if ((paramInt >= 233) && (paramInt < 240)) return true;
/* 449 */     return false;
/*     */   }
/*     */ 
/*     */   public static byte getCPRefOpTag(int paramInt) {
/* 453 */     if ((paramInt < BC_INDEX[0].length) && (BC_INDEX[0][paramInt] > 0)) return BC_TAG[0][paramInt];
/* 454 */     if ((paramInt >= 233) && (paramInt < 240)) return 20;
/* 455 */     return 0;
/*     */   }
/*     */ 
/*     */   public static boolean isFieldOp(int paramInt) {
/* 459 */     return (paramInt >= 178) && (paramInt <= 181);
/*     */   }
/*     */ 
/*     */   public static boolean isInvokeInitOp(int paramInt) {
/* 463 */     return (paramInt >= 230) && (paramInt < 233);
/*     */   }
/*     */ 
/*     */   public static boolean isSelfLinkerOp(int paramInt) {
/* 467 */     return (paramInt >= 202) && (paramInt < 230);
/*     */   }
/*     */ 
/*     */   public static String byteName(int paramInt)
/*     */   {
/*     */     String str;
/* 557 */     if ((paramInt < BC_NAME.length) && (BC_NAME[paramInt] != null)) {
/* 558 */       str = BC_NAME[paramInt];
/*     */     }
/*     */     else
/*     */     {
/*     */       int i;
/* 559 */       if (isSelfLinkerOp(paramInt)) {
/* 560 */         i = paramInt - 202;
/* 561 */         int j = i >= 14 ? 1 : 0;
/* 562 */         if (j != 0) i -= 14;
/* 563 */         int k = i >= 7 ? 1 : 0;
/* 564 */         if (k != 0) i -= 7;
/* 565 */         int m = 178 + i;
/* 566 */         assert ((m >= 178) && (m <= 184));
/* 567 */         str = BC_NAME[m];
/* 568 */         str = str + (j != 0 ? "_super" : "_this");
/* 569 */         if (k != 0) str = "aload_0&" + str;
/* 570 */         str = "*" + str;
/* 571 */       } else if (isInvokeInitOp(paramInt)) {
/* 572 */         i = paramInt - 230;
/* 573 */         switch (i) {
/*     */         case 0:
/* 575 */           str = "*invokespecial_init_this"; break;
/*     */         case 1:
/* 577 */           str = "*invokespecial_init_super"; break;
/*     */         default:
/* 579 */           assert (i == 2);
/* 580 */           str = "*invokespecial_init_new";
/*     */         }
/*     */       } else {
/* 583 */         switch (paramInt) { case 234:
/* 584 */           str = "*ildc"; break;
/*     */         case 235:
/* 585 */           str = "*fldc"; break;
/*     */         case 237:
/* 586 */           str = "*ildc_w"; break;
/*     */         case 238:
/* 587 */           str = "*fldc_w"; break;
/*     */         case 239:
/* 588 */           str = "*dldc2_w"; break;
/*     */         case 233:
/* 589 */           str = "*cldc"; break;
/*     */         case 236:
/* 590 */           str = "*cldc_w"; break;
/*     */         case 254:
/* 591 */           str = "*byte_escape"; break;
/*     */         case 253:
/* 592 */           str = "*ref_escape"; break;
/*     */         case 255:
/* 593 */           str = "*end"; break;
/*     */         case 240:
/*     */         case 241:
/*     */         case 242:
/*     */         case 243:
/*     */         case 244:
/*     */         case 245:
/*     */         case 246:
/*     */         case 247:
/*     */         case 248:
/*     */         case 249:
/*     */         case 250:
/*     */         case 251:
/*     */         case 252:
/*     */         default:
/* 594 */           str = "*bc#" + paramInt; }
/*     */       }
/*     */     }
/* 597 */     return str;
/*     */   }
/*     */ 
/*     */   private static void def(String paramString, int paramInt) {
/* 601 */     def(paramString, paramInt, paramInt);
/*     */   }
/*     */   private static void def(String paramString, int paramInt1, int paramInt2) {
/* 604 */     String[] arrayOfString = { paramString, null };
/* 605 */     if (paramString.indexOf('w') > 0) {
/* 606 */       arrayOfString[1] = paramString.substring(paramString.indexOf(119));
/* 607 */       arrayOfString[0] = paramString.substring(0, paramString.indexOf(119));
/*     */     }
/* 609 */     for (int i = 0; i <= 1; i++) {
/* 610 */       paramString = arrayOfString[i];
/* 611 */       if (paramString != null) {
/* 612 */         int j = paramString.length();
/* 613 */         int k = Math.max(0, paramString.indexOf('k'));
/* 614 */         int m = 0;
/* 615 */         int n = Math.max(0, paramString.indexOf('o'));
/* 616 */         int i1 = Math.max(0, paramString.indexOf('l'));
/* 617 */         int i2 = Math.max(0, paramString.indexOf('x'));
/* 618 */         if ((k > 0) && (k + 1 < j)) {
/* 619 */           switch (paramString.charAt(k + 1)) { case 'c':
/* 620 */             m = 7; break;
/*     */           case 'k':
/* 621 */             m = 20; break;
/*     */           case 'f':
/* 622 */             m = 9; break;
/*     */           case 'm':
/* 623 */             m = 10; break;
/*     */           case 'i':
/* 624 */             m = 11;
/*     */           case 'd':
/*     */           case 'e':
/*     */           case 'g':
/*     */           case 'h':
/*     */           case 'j':
/* 626 */           case 'l': } if ((!$assertionsDisabled) && (m == 0)) throw new AssertionError(); 
/*     */         }
/* 627 */         else if ((k > 0) && (j == 2)) {
/* 628 */           assert (paramInt1 == 18);
/* 629 */           m = 20;
/*     */         }
/* 631 */         for (int i3 = paramInt1; i3 <= paramInt2; i3++) {
/* 632 */           BC_FORMAT[i][i3] = paramString;
/* 633 */           assert (BC_LENGTH[i][i3] == -1);
/* 634 */           BC_LENGTH[i][i3] = ((byte)j);
/* 635 */           BC_INDEX[i][i3] = ((byte)k);
/* 636 */           BC_TAG[i][i3] = ((byte)m);
/* 637 */           assert ((k != 0) || (m == 0));
/* 638 */           BC_BRANCH[i][i3] = ((byte)n);
/* 639 */           BC_SLOT[i][i3] = ((byte)i1);
/* 640 */           assert ((n == 0) || (i1 == 0));
/* 641 */           assert ((n == 0) || (k == 0));
/* 642 */           assert ((i1 == 0) || (k == 0));
/* 643 */           BC_CON[i][i3] = ((byte)i2);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/* 649 */   public static void opcodeChecker(byte[] paramArrayOfByte) throws Instruction.FormatException { Instruction localInstruction = at(paramArrayOfByte, 0);
/* 650 */     while (localInstruction != null) {
/* 651 */       int i = localInstruction.getBC();
/* 652 */       if ((i == 186) || (i < 0) || (i > 201)) {
/* 653 */         String str = "illegal opcode: " + i + " " + localInstruction;
/* 654 */         throw new FormatException(str);
/*     */       }
/* 656 */       localInstruction = localInstruction.next();
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 472 */     BC_LENGTH = new byte[2][256];
/* 473 */     BC_INDEX = new byte[2][256];
/* 474 */     BC_TAG = new byte[2][256];
/* 475 */     BC_BRANCH = new byte[2][256];
/* 476 */     BC_SLOT = new byte[2][256];
/* 477 */     BC_CON = new byte[2][256];
/* 478 */     BC_NAME = new String[256];
/* 479 */     BC_FORMAT = new String[2]['Ê'];
/*     */ 
/* 481 */     for (int i = 0; i < 202; i++) {
/* 482 */       BC_LENGTH[0][i] = -1;
/* 483 */       BC_LENGTH[1][i] = -1;
/*     */     }
/* 485 */     def("b", 0, 15);
/* 486 */     def("bx", 16);
/* 487 */     def("bxx", 17);
/* 488 */     def("bk", 18);
/* 489 */     def("bkk", 19, 20);
/* 490 */     def("blwbll", 21, 25);
/* 491 */     def("b", 26, 53);
/* 492 */     def("blwbll", 54, 58);
/* 493 */     def("b", 59, 131);
/* 494 */     def("blxwbllxx", 132);
/* 495 */     def("b", 133, 152);
/* 496 */     def("boo", 153, 168);
/* 497 */     def("blwbll", 169);
/* 498 */     def("", 170, 171);
/* 499 */     def("b", 172, 177);
/* 500 */     def("bkf", 178, 181);
/* 501 */     def("bkm", 182, 184);
/* 502 */     def("bkixx", 185);
/* 503 */     def("", 186);
/* 504 */     def("bkc", 187);
/* 505 */     def("bx", 188);
/* 506 */     def("bkc", 189);
/* 507 */     def("b", 190, 191);
/* 508 */     def("bkc", 192, 193);
/* 509 */     def("b", 194, 195);
/* 510 */     def("", 196);
/* 511 */     def("bkcx", 197);
/* 512 */     def("boo", 198, 199);
/* 513 */     def("boooo", 200, 201);
/* 514 */     for (i = 0; i < 202; i++)
/*     */     {
/* 517 */       if (BC_LENGTH[0][i] == -1) {
/* 518 */         if ((!$assertionsDisabled) && (i != 186)) throw new AssertionError();
/*     */ 
/*     */       }
/* 523 */       else if (BC_LENGTH[1][i] == -1) {
/* 524 */         BC_LENGTH[1][i] = ((byte)(1 + BC_LENGTH[0][i]));
/*     */       }
/*     */     }
/* 527 */     String str = "nop aconst_null iconst_m1 iconst_0 iconst_1 iconst_2 iconst_3 iconst_4 iconst_5 lconst_0 lconst_1 fconst_0 fconst_1 fconst_2 dconst_0 dconst_1 bipush sipush ldc ldc_w ldc2_w iload lload fload dload aload iload_0 iload_1 iload_2 iload_3 lload_0 lload_1 lload_2 lload_3 fload_0 fload_1 fload_2 fload_3 dload_0 dload_1 dload_2 dload_3 aload_0 aload_1 aload_2 aload_3 iaload laload faload daload aaload baload caload saload istore lstore fstore dstore astore istore_0 istore_1 istore_2 istore_3 lstore_0 lstore_1 lstore_2 lstore_3 fstore_0 fstore_1 fstore_2 fstore_3 dstore_0 dstore_1 dstore_2 dstore_3 astore_0 astore_1 astore_2 astore_3 iastore lastore fastore dastore aastore bastore castore sastore pop pop2 dup dup_x1 dup_x2 dup2 dup2_x1 dup2_x2 swap iadd ladd fadd dadd isub lsub fsub dsub imul lmul fmul dmul idiv ldiv fdiv ddiv irem lrem frem drem ineg lneg fneg dneg ishl lshl ishr lshr iushr lushr iand land ior lor ixor lxor iinc i2l i2f i2d l2i l2f l2d f2i f2l f2d d2i d2l d2f i2b i2c i2s lcmp fcmpl fcmpg dcmpl dcmpg ifeq ifne iflt ifge ifgt ifle if_icmpeq if_icmpne if_icmplt if_icmpge if_icmpgt if_icmple if_acmpeq if_acmpne goto jsr ret tableswitch lookupswitch ireturn lreturn freturn dreturn areturn return getstatic putstatic getfield putfield invokevirtual invokespecial invokestatic invokeinterface xxxunusedxxx new newarray anewarray arraylength athrow checkcast instanceof monitorenter monitorexit wide multianewarray ifnull ifnonnull goto_w jsr_w ";
/*     */ 
/* 549 */     for (int j = 0; str.length() > 0; j++) {
/* 550 */       int k = str.indexOf(' ');
/* 551 */       BC_NAME[j] = str.substring(0, k);
/* 552 */       str = str.substring(k + 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class FormatException extends IOException
/*     */   {
/*     */     FormatException(String paramString)
/*     */     {
/* 661 */       super();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class LookupSwitch extends Instruction.Switch
/*     */   {
/*     */     public int getCaseCount()
/*     */     {
/* 301 */       return intAt(1); } 
/* 302 */     public int getCaseValue(int paramInt) { return intAt(2 + paramInt * 2 + 0); } 
/* 303 */     public int getCaseLabel(int paramInt) { return intAt(2 + paramInt * 2 + 1) + this.pc; }
/*     */ 
/*     */     public void setCaseCount(int paramInt) {
/* 306 */       setIntAt(1, paramInt);
/* 307 */       this.length = getLength(paramInt);
/*     */     }
/* 309 */     public void setCaseValue(int paramInt1, int paramInt2) { setIntAt(2 + paramInt1 * 2 + 0, paramInt2); } 
/* 310 */     public void setCaseLabel(int paramInt1, int paramInt2) { setIntAt(2 + paramInt1 * 2 + 1, paramInt2 - this.pc); }
/*     */ 
/*     */     LookupSwitch(byte[] paramArrayOfByte, int paramInt) {
/* 313 */       super(paramInt, 171);
/*     */     }
/*     */     protected int getLength(int paramInt) {
/* 316 */       return this.apc - this.pc + (2 + paramInt * 2) * 4;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract class Switch extends Instruction
/*     */   {
/*     */     protected int apc;
/*     */ 
/*     */     public abstract int getCaseCount();
/*     */ 
/*     */     public abstract int getCaseValue(int paramInt);
/*     */ 
/*     */     public abstract int getCaseLabel(int paramInt);
/*     */ 
/*     */     public abstract void setCaseCount(int paramInt);
/*     */ 
/*     */     public abstract void setCaseValue(int paramInt1, int paramInt2);
/*     */ 
/*     */     public abstract void setCaseLabel(int paramInt1, int paramInt2);
/*     */ 
/*     */     protected abstract int getLength(int paramInt);
/*     */ 
/*     */     public int getDefaultLabel()
/*     */     {
/* 241 */       return intAt(0) + this.pc; } 
/* 242 */     public void setDefaultLabel(int paramInt) { setIntAt(0, paramInt - this.pc); }
/*     */ 
/*     */     protected int intAt(int paramInt) {
/* 245 */       return getInt(this.bytes, this.apc + paramInt * 4); } 
/* 246 */     protected void setIntAt(int paramInt1, int paramInt2) { setInt(this.bytes, this.apc + paramInt1 * 4, paramInt2); } 
/*     */     protected Switch(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 248 */       super(paramInt1, paramInt2, 0, 0);
/* 249 */       this.apc = alignPC(paramInt1 + 1);
/* 250 */       this.special = true;
/* 251 */       this.length = getLength(getCaseCount());
/*     */     }
/* 253 */     public int getAlignedPC() { return this.apc; } 
/*     */     public String toString() {
/* 255 */       String str = super.toString();
/* 256 */       str = str + " Default:" + labstr(getDefaultLabel());
/* 257 */       int i = getCaseCount();
/* 258 */       for (int j = 0; j < i; j++) {
/* 259 */         str = str + "\n\tCase " + getCaseValue(j) + ":" + labstr(getCaseLabel(j));
/*     */       }
/* 261 */       return str;
/*     */     }
/*     */     public static int alignPC(int paramInt) {
/* 264 */       while (paramInt % 4 != 0) paramInt++;
/* 265 */       return paramInt;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class TableSwitch extends Instruction.Switch {
/*     */     public int getLowCase() {
/* 271 */       return intAt(1); } 
/* 272 */     public int getHighCase() { return intAt(2); } 
/* 273 */     public int getCaseCount() { return intAt(2) - intAt(1) + 1; } 
/* 274 */     public int getCaseValue(int paramInt) { return getLowCase() + paramInt; } 
/* 275 */     public int getCaseLabel(int paramInt) { return intAt(3 + paramInt) + this.pc; } 
/*     */     public void setLowCase(int paramInt) {
/* 277 */       setIntAt(1, paramInt); } 
/* 278 */     public void setHighCase(int paramInt) { setIntAt(2, paramInt); } 
/* 279 */     public void setCaseLabel(int paramInt1, int paramInt2) { setIntAt(3 + paramInt1, paramInt2 - this.pc); } 
/*     */     public void setCaseCount(int paramInt) {
/* 281 */       setHighCase(getLowCase() + paramInt - 1);
/* 282 */       this.length = getLength(paramInt);
/*     */     }
/*     */     public void setCaseValue(int paramInt1, int paramInt2) {
/* 285 */       if (paramInt1 != 0) throw new UnsupportedOperationException();
/* 286 */       int i = getCaseCount();
/* 287 */       setLowCase(paramInt2);
/* 288 */       setCaseCount(i);
/*     */     }
/*     */ 
/*     */     TableSwitch(byte[] paramArrayOfByte, int paramInt) {
/* 292 */       super(paramInt, 170);
/*     */     }
/*     */     protected int getLength(int paramInt) {
/* 295 */       return this.apc - this.pc + (3 + paramInt) * 4;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.util.jar.pack.Instruction
 * JD-Core Version:    0.6.2
 */