/*     */ package java.text;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import sun.text.CollatorUtilities;
/*     */ import sun.text.normalizer.NormalizerBase;
/*     */ import sun.text.normalizer.NormalizerBase.Mode;
/*     */ 
/*     */ public final class CollationElementIterator
/*     */ {
/*     */   public static final int NULLORDER = -1;
/*     */   static final int UNMAPPEDCHARVALUE = 2147418112;
/* 770 */   private NormalizerBase text = null;
/* 771 */   private int[] buffer = null;
/* 772 */   private int expIndex = 0;
/* 773 */   private StringBuffer key = new StringBuffer(5);
/* 774 */   private int swapOrder = 0;
/*     */   private RBCollationTables ordering;
/*     */   private RuleBasedCollator owner;
/*     */ 
/*     */   CollationElementIterator(String paramString, RuleBasedCollator paramRuleBasedCollator)
/*     */   {
/* 125 */     this.owner = paramRuleBasedCollator;
/* 126 */     this.ordering = paramRuleBasedCollator.getTables();
/* 127 */     if (paramString.length() != 0) {
/* 128 */       NormalizerBase.Mode localMode = CollatorUtilities.toNormalizerMode(paramRuleBasedCollator.getDecomposition());
/*     */ 
/* 130 */       this.text = new NormalizerBase(paramString, localMode);
/*     */     }
/*     */   }
/*     */ 
/*     */   CollationElementIterator(CharacterIterator paramCharacterIterator, RuleBasedCollator paramRuleBasedCollator)
/*     */   {
/* 143 */     this.owner = paramRuleBasedCollator;
/* 144 */     this.ordering = paramRuleBasedCollator.getTables();
/* 145 */     NormalizerBase.Mode localMode = CollatorUtilities.toNormalizerMode(paramRuleBasedCollator.getDecomposition());
/*     */ 
/* 147 */     this.text = new NormalizerBase(paramCharacterIterator, localMode);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 156 */     if (this.text != null) {
/* 157 */       this.text.reset();
/* 158 */       NormalizerBase.Mode localMode = CollatorUtilities.toNormalizerMode(this.owner.getDecomposition());
/*     */ 
/* 160 */       this.text.setMode(localMode);
/*     */     }
/* 162 */     this.buffer = null;
/* 163 */     this.expIndex = 0;
/* 164 */     this.swapOrder = 0;
/*     */   }
/*     */ 
/*     */   public int next()
/*     */   {
/* 183 */     if (this.text == null) {
/* 184 */       return -1;
/*     */     }
/* 186 */     NormalizerBase.Mode localMode1 = this.text.getMode();
/*     */ 
/* 188 */     NormalizerBase.Mode localMode2 = CollatorUtilities.toNormalizerMode(this.owner.getDecomposition());
/*     */ 
/* 190 */     if (localMode1 != localMode2) {
/* 191 */       this.text.setMode(localMode2);
/*     */     }
/*     */ 
/* 197 */     if (this.buffer != null) {
/* 198 */       if (this.expIndex < this.buffer.length) {
/* 199 */         return strengthOrder(this.buffer[(this.expIndex++)]);
/*     */       }
/* 201 */       this.buffer = null;
/* 202 */       this.expIndex = 0;
/*     */     }
/* 204 */     else if (this.swapOrder != 0) {
/* 205 */       if (Character.isSupplementaryCodePoint(this.swapOrder)) {
/* 206 */         char[] arrayOfChar = Character.toChars(this.swapOrder);
/* 207 */         this.swapOrder = arrayOfChar[1];
/* 208 */         return arrayOfChar[0] << '\020';
/*     */       }
/* 210 */       i = this.swapOrder << 16;
/* 211 */       this.swapOrder = 0;
/* 212 */       return i;
/*     */     }
/* 214 */     int i = this.text.next();
/*     */ 
/* 217 */     if (i == -1) {
/* 218 */       return -1;
/*     */     }
/*     */ 
/* 221 */     int j = this.ordering.getUnicodeOrder(i);
/* 222 */     if (j == -1) {
/* 223 */       this.swapOrder = i;
/* 224 */       return 2147418112;
/*     */     }
/* 226 */     if (j >= 2130706432) {
/* 227 */       j = nextContractChar(i);
/*     */     }
/* 229 */     if (j >= 2113929216) {
/* 230 */       this.buffer = this.ordering.getExpandValueList(j);
/* 231 */       this.expIndex = 0;
/* 232 */       j = this.buffer[(this.expIndex++)];
/*     */     }
/*     */ 
/* 235 */     if (this.ordering.isSEAsianSwapping())
/*     */     {
/*     */       int k;
/* 237 */       if (isThaiPreVowel(i)) {
/* 238 */         k = this.text.next();
/* 239 */         if (isThaiBaseConsonant(k)) {
/* 240 */           this.buffer = makeReorderedBuffer(k, j, this.buffer, true);
/* 241 */           j = this.buffer[0];
/* 242 */           this.expIndex = 1;
/* 243 */         } else if (k != -1) {
/* 244 */           this.text.previous();
/*     */         }
/*     */       }
/* 247 */       if (isLaoPreVowel(i)) {
/* 248 */         k = this.text.next();
/* 249 */         if (isLaoBaseConsonant(k)) {
/* 250 */           this.buffer = makeReorderedBuffer(k, j, this.buffer, true);
/* 251 */           j = this.buffer[0];
/* 252 */           this.expIndex = 1;
/* 253 */         } else if (k != -1) {
/* 254 */           this.text.previous();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 259 */     return strengthOrder(j);
/*     */   }
/*     */ 
/*     */   public int previous()
/*     */   {
/* 279 */     if (this.text == null) {
/* 280 */       return -1;
/*     */     }
/* 282 */     NormalizerBase.Mode localMode1 = this.text.getMode();
/*     */ 
/* 284 */     NormalizerBase.Mode localMode2 = CollatorUtilities.toNormalizerMode(this.owner.getDecomposition());
/*     */ 
/* 286 */     if (localMode1 != localMode2) {
/* 287 */       this.text.setMode(localMode2);
/*     */     }
/* 289 */     if (this.buffer != null) {
/* 290 */       if (this.expIndex > 0) {
/* 291 */         return strengthOrder(this.buffer[(--this.expIndex)]);
/*     */       }
/* 293 */       this.buffer = null;
/* 294 */       this.expIndex = 0;
/*     */     }
/* 296 */     else if (this.swapOrder != 0) {
/* 297 */       if (Character.isSupplementaryCodePoint(this.swapOrder)) {
/* 298 */         char[] arrayOfChar = Character.toChars(this.swapOrder);
/* 299 */         this.swapOrder = arrayOfChar[1];
/* 300 */         return arrayOfChar[0] << '\020';
/*     */       }
/* 302 */       i = this.swapOrder << 16;
/* 303 */       this.swapOrder = 0;
/* 304 */       return i;
/*     */     }
/* 306 */     int i = this.text.previous();
/* 307 */     if (i == -1) {
/* 308 */       return -1;
/*     */     }
/*     */ 
/* 311 */     int j = this.ordering.getUnicodeOrder(i);
/*     */ 
/* 313 */     if (j == -1) {
/* 314 */       this.swapOrder = 2147418112;
/* 315 */       return i;
/* 316 */     }if (j >= 2130706432) {
/* 317 */       j = prevContractChar(i);
/*     */     }
/* 319 */     if (j >= 2113929216) {
/* 320 */       this.buffer = this.ordering.getExpandValueList(j);
/* 321 */       this.expIndex = this.buffer.length;
/* 322 */       j = this.buffer[(--this.expIndex)];
/*     */     }
/*     */ 
/* 325 */     if (this.ordering.isSEAsianSwapping())
/*     */     {
/*     */       int k;
/* 327 */       if (isThaiBaseConsonant(i)) {
/* 328 */         k = this.text.previous();
/* 329 */         if (isThaiPreVowel(k)) {
/* 330 */           this.buffer = makeReorderedBuffer(k, j, this.buffer, false);
/* 331 */           this.expIndex = (this.buffer.length - 1);
/* 332 */           j = this.buffer[this.expIndex];
/*     */         } else {
/* 334 */           this.text.next();
/*     */         }
/*     */       }
/* 337 */       if (isLaoBaseConsonant(i)) {
/* 338 */         k = this.text.previous();
/* 339 */         if (isLaoPreVowel(k)) {
/* 340 */           this.buffer = makeReorderedBuffer(k, j, this.buffer, false);
/* 341 */           this.expIndex = (this.buffer.length - 1);
/* 342 */           j = this.buffer[this.expIndex];
/*     */         } else {
/* 344 */           this.text.next();
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 349 */     return strengthOrder(j);
/*     */   }
/*     */ 
/*     */   public static final int primaryOrder(int paramInt)
/*     */   {
/* 359 */     paramInt &= -65536;
/* 360 */     return paramInt >>> 16;
/*     */   }
/*     */ 
/*     */   public static final short secondaryOrder(int paramInt)
/*     */   {
/* 369 */     paramInt &= 65280;
/* 370 */     return (short)(paramInt >> 8);
/*     */   }
/*     */ 
/*     */   public static final short tertiaryOrder(int paramInt)
/*     */   {
/* 379 */     return (short)(paramInt &= 255);
/*     */   }
/*     */ 
/*     */   final int strengthOrder(int paramInt)
/*     */   {
/* 389 */     int i = this.owner.getStrength();
/* 390 */     if (i == 0)
/*     */     {
/* 392 */       paramInt &= -65536;
/* 393 */     } else if (i == 1)
/*     */     {
/* 395 */       paramInt &= -256;
/*     */     }
/* 397 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public void setOffset(int paramInt)
/*     */   {
/* 417 */     if (this.text != null) {
/* 418 */       if ((paramInt < this.text.getBeginIndex()) || (paramInt >= this.text.getEndIndex()))
/*     */       {
/* 420 */         this.text.setIndexOnly(paramInt);
/*     */       } else {
/* 422 */         int i = this.text.setIndex(paramInt);
/*     */ 
/* 427 */         if (this.ordering.usedInContractSeq(i))
/*     */         {
/* 430 */           while (this.ordering.usedInContractSeq(i)) {
/* 431 */             i = this.text.previous();
/*     */           }
/*     */ 
/* 437 */           int j = this.text.getIndex();
/* 438 */           while (this.text.getIndex() <= paramInt) {
/* 439 */             j = this.text.getIndex();
/* 440 */             next();
/*     */           }
/* 442 */           this.text.setIndexOnly(j);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 450 */     this.buffer = null;
/* 451 */     this.expIndex = 0;
/* 452 */     this.swapOrder = 0;
/*     */   }
/*     */ 
/*     */   public int getOffset()
/*     */   {
/* 471 */     return this.text != null ? this.text.getIndex() : 0;
/*     */   }
/*     */ 
/*     */   public int getMaxExpansion(int paramInt)
/*     */   {
/* 485 */     return this.ordering.getMaxExpansion(paramInt);
/*     */   }
/*     */ 
/*     */   public void setText(String paramString)
/*     */   {
/* 496 */     this.buffer = null;
/* 497 */     this.swapOrder = 0;
/* 498 */     this.expIndex = 0;
/* 499 */     NormalizerBase.Mode localMode = CollatorUtilities.toNormalizerMode(this.owner.getDecomposition());
/*     */ 
/* 501 */     if (this.text == null) {
/* 502 */       this.text = new NormalizerBase(paramString, localMode);
/*     */     } else {
/* 504 */       this.text.setMode(localMode);
/* 505 */       this.text.setText(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setText(CharacterIterator paramCharacterIterator)
/*     */   {
/* 517 */     this.buffer = null;
/* 518 */     this.swapOrder = 0;
/* 519 */     this.expIndex = 0;
/* 520 */     NormalizerBase.Mode localMode = CollatorUtilities.toNormalizerMode(this.owner.getDecomposition());
/*     */ 
/* 522 */     if (this.text == null) {
/* 523 */       this.text = new NormalizerBase(paramCharacterIterator, localMode);
/*     */     } else {
/* 525 */       this.text.setMode(localMode);
/* 526 */       this.text.setText(paramCharacterIterator);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final boolean isThaiPreVowel(int paramInt)
/*     */   {
/* 539 */     return (paramInt >= 3648) && (paramInt <= 3652);
/*     */   }
/*     */ 
/*     */   private static final boolean isThaiBaseConsonant(int paramInt)
/*     */   {
/* 546 */     return (paramInt >= 3585) && (paramInt <= 3630);
/*     */   }
/*     */ 
/*     */   private static final boolean isLaoPreVowel(int paramInt)
/*     */   {
/* 554 */     return (paramInt >= 3776) && (paramInt <= 3780);
/*     */   }
/*     */ 
/*     */   private static final boolean isLaoBaseConsonant(int paramInt)
/*     */   {
/* 561 */     return (paramInt >= 3713) && (paramInt <= 3758);
/*     */   }
/*     */ 
/*     */   private int[] makeReorderedBuffer(int paramInt1, int paramInt2, int[] paramArrayOfInt, boolean paramBoolean)
/*     */   {
/* 581 */     int i = this.ordering.getUnicodeOrder(paramInt1);
/* 582 */     if (i >= 2130706432) {
/* 583 */       i = paramBoolean ? nextContractChar(paramInt1) : prevContractChar(paramInt1);
/*     */     }
/*     */ 
/* 586 */     int[] arrayOfInt2 = null;
/* 587 */     if (i >= 2113929216)
/* 588 */       arrayOfInt2 = this.ordering.getExpandValueList(i);
/*     */     int j;
/* 591 */     if (!paramBoolean) {
/* 592 */       j = i;
/* 593 */       i = paramInt2;
/* 594 */       paramInt2 = j;
/* 595 */       int[] arrayOfInt3 = arrayOfInt2;
/* 596 */       arrayOfInt2 = paramArrayOfInt;
/* 597 */       paramArrayOfInt = arrayOfInt3;
/*     */     }
/*     */     int[] arrayOfInt1;
/* 600 */     if ((arrayOfInt2 == null) && (paramArrayOfInt == null)) {
/* 601 */       arrayOfInt1 = new int[2];
/* 602 */       arrayOfInt1[0] = i;
/* 603 */       arrayOfInt1[1] = paramInt2;
/*     */     }
/*     */     else {
/* 606 */       j = arrayOfInt2 == null ? 1 : arrayOfInt2.length;
/* 607 */       int k = paramArrayOfInt == null ? 1 : paramArrayOfInt.length;
/* 608 */       arrayOfInt1 = new int[j + k];
/*     */ 
/* 610 */       if (arrayOfInt2 == null) {
/* 611 */         arrayOfInt1[0] = i;
/*     */       }
/*     */       else {
/* 614 */         System.arraycopy(arrayOfInt2, 0, arrayOfInt1, 0, j);
/*     */       }
/*     */ 
/* 617 */       if (paramArrayOfInt == null) {
/* 618 */         arrayOfInt1[j] = paramInt2;
/*     */       }
/*     */       else {
/* 621 */         System.arraycopy(paramArrayOfInt, 0, arrayOfInt1, j, k);
/*     */       }
/*     */     }
/*     */ 
/* 625 */     return arrayOfInt1;
/*     */   }
/*     */ 
/*     */   static final boolean isIgnorable(int paramInt)
/*     */   {
/* 634 */     return primaryOrder(paramInt) == 0;
/*     */   }
/*     */ 
/*     */   private int nextContractChar(int paramInt)
/*     */   {
/* 648 */     Vector localVector = this.ordering.getContractValues(paramInt);
/* 649 */     EntryPair localEntryPair = (EntryPair)localVector.firstElement();
/* 650 */     int i = localEntryPair.value;
/*     */ 
/* 655 */     localEntryPair = (EntryPair)localVector.lastElement();
/* 656 */     int j = localEntryPair.entryName.length();
/*     */ 
/* 660 */     NormalizerBase localNormalizerBase = (NormalizerBase)this.text.clone();
/*     */ 
/* 665 */     localNormalizerBase.previous();
/* 666 */     this.key.setLength(0);
/* 667 */     int k = localNormalizerBase.next();
/* 668 */     while ((j > 0) && (k != -1)) {
/* 669 */       if (Character.isSupplementaryCodePoint(k)) {
/* 670 */         this.key.append(Character.toChars(k));
/* 671 */         j -= 2;
/*     */       } else {
/* 673 */         this.key.append((char)k);
/* 674 */         j--;
/*     */       }
/* 676 */       k = localNormalizerBase.next();
/*     */     }
/* 678 */     String str = this.key.toString();
/*     */ 
/* 685 */     j = 1;
/* 686 */     for (int m = localVector.size() - 1; m > 0; m--) {
/* 687 */       localEntryPair = (EntryPair)localVector.elementAt(m);
/* 688 */       if (localEntryPair.fwd)
/*     */       {
/* 691 */         if ((str.startsWith(localEntryPair.entryName)) && (localEntryPair.entryName.length() > j))
/*     */         {
/* 693 */           j = localEntryPair.entryName.length();
/* 694 */           i = localEntryPair.value;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 702 */     while (j > 1) {
/* 703 */       k = this.text.next();
/* 704 */       j -= Character.charCount(k);
/*     */     }
/* 706 */     return i;
/*     */   }
/*     */ 
/*     */   private int prevContractChar(int paramInt)
/*     */   {
/* 724 */     Vector localVector = this.ordering.getContractValues(paramInt);
/* 725 */     EntryPair localEntryPair = (EntryPair)localVector.firstElement();
/* 726 */     int i = localEntryPair.value;
/*     */ 
/* 728 */     localEntryPair = (EntryPair)localVector.lastElement();
/* 729 */     int j = localEntryPair.entryName.length();
/*     */ 
/* 731 */     NormalizerBase localNormalizerBase = (NormalizerBase)this.text.clone();
/*     */ 
/* 733 */     localNormalizerBase.next();
/* 734 */     this.key.setLength(0);
/* 735 */     int k = localNormalizerBase.previous();
/* 736 */     while ((j > 0) && (k != -1)) {
/* 737 */       if (Character.isSupplementaryCodePoint(k)) {
/* 738 */         this.key.append(Character.toChars(k));
/* 739 */         j -= 2;
/*     */       } else {
/* 741 */         this.key.append((char)k);
/* 742 */         j--;
/*     */       }
/* 744 */       k = localNormalizerBase.previous();
/*     */     }
/* 746 */     String str = this.key.toString();
/*     */ 
/* 748 */     j = 1;
/* 749 */     for (int m = localVector.size() - 1; m > 0; m--) {
/* 750 */       localEntryPair = (EntryPair)localVector.elementAt(m);
/* 751 */       if (!localEntryPair.fwd)
/*     */       {
/* 754 */         if ((str.startsWith(localEntryPair.entryName)) && (localEntryPair.entryName.length() > j))
/*     */         {
/* 756 */           j = localEntryPair.entryName.length();
/* 757 */           i = localEntryPair.value;
/*     */         }
/*     */       }
/*     */     }
/* 761 */     while (j > 1) {
/* 762 */       k = this.text.previous();
/* 763 */       j -= Character.charCount(k);
/*     */     }
/* 765 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.CollationElementIterator
 * JD-Core Version:    0.6.2
 */