/*     */ package java.text;
/*     */ 
/*     */ public class RuleBasedCollator extends Collator
/*     */ {
/*     */   static final int CHARINDEX = 1879048192;
/*     */   static final int EXPANDCHARINDEX = 2113929216;
/*     */   static final int CONTRACTCHARINDEX = 2130706432;
/*     */   static final int UNMAPPED = -1;
/*     */   private static final int COLLATIONKEYOFFSET = 1;
/* 754 */   private RBCollationTables tables = null;
/*     */ 
/* 758 */   private StringBuffer primResult = null;
/* 759 */   private StringBuffer secResult = null;
/* 760 */   private StringBuffer terResult = null;
/* 761 */   private CollationElementIterator sourceCursor = null;
/* 762 */   private CollationElementIterator targetCursor = null;
/*     */ 
/*     */   public RuleBasedCollator(String paramString)
/*     */     throws ParseException
/*     */   {
/* 281 */     this(paramString, 1);
/*     */   }
/*     */ 
/*     */   RuleBasedCollator(String paramString, int paramInt)
/*     */     throws ParseException
/*     */   {
/* 298 */     setStrength(2);
/* 299 */     setDecomposition(paramInt);
/* 300 */     this.tables = new RBCollationTables(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   private RuleBasedCollator(RuleBasedCollator paramRuleBasedCollator)
/*     */   {
/* 307 */     setStrength(paramRuleBasedCollator.getStrength());
/* 308 */     setDecomposition(paramRuleBasedCollator.getDecomposition());
/* 309 */     this.tables = paramRuleBasedCollator.tables;
/*     */   }
/*     */ 
/*     */   public String getRules()
/*     */   {
/* 319 */     return this.tables.getRules();
/*     */   }
/*     */ 
/*     */   public CollationElementIterator getCollationElementIterator(String paramString)
/*     */   {
/* 327 */     return new CollationElementIterator(paramString, this);
/*     */   }
/*     */ 
/*     */   public CollationElementIterator getCollationElementIterator(CharacterIterator paramCharacterIterator)
/*     */   {
/* 337 */     return new CollationElementIterator(paramCharacterIterator, this);
/*     */   }
/*     */ 
/*     */   public synchronized int compare(String paramString1, String paramString2)
/*     */   {
/* 350 */     if ((paramString1 == null) || (paramString2 == null)) {
/* 351 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 373 */     int i = 0;
/*     */ 
/* 375 */     if (this.sourceCursor == null)
/* 376 */       this.sourceCursor = getCollationElementIterator(paramString1);
/*     */     else {
/* 378 */       this.sourceCursor.setText(paramString1);
/*     */     }
/* 380 */     if (this.targetCursor == null)
/* 381 */       this.targetCursor = getCollationElementIterator(paramString2);
/*     */     else {
/* 383 */       this.targetCursor.setText(paramString2); } 
/*     */ int j = 0; int k = 0;
/*     */ 
/* 388 */     int m = getStrength() >= 1 ? 1 : 0;
/* 389 */     int n = m;
/* 390 */     int i1 = getStrength() >= 2 ? 1 : 0;
/*     */ 
/* 392 */     int i2 = 1; int i3 = 1;
/*     */     int i4;
/*     */     while (true) { if (i2 != 0) j = this.sourceCursor.next(); else i2 = 1;
/* 398 */       if (i3 != 0) k = this.targetCursor.next(); else i3 = 1;
/*     */ 
/* 401 */       if ((j == -1) || (k == -1))
/*     */       {
/*     */         break;
/*     */       }
/* 405 */       i4 = CollationElementIterator.primaryOrder(j);
/* 406 */       int i5 = CollationElementIterator.primaryOrder(k);
/*     */ 
/* 409 */       if (j == k) {
/* 410 */         if ((this.tables.isFrenchSec()) && (i4 != 0) && 
/* 411 */           (n == 0))
/*     */         {
/* 414 */           n = m;
/*     */ 
/* 417 */           i1 = 0;
/*     */         }
/*     */ 
/*     */       }
/* 424 */       else if (i4 != i5)
/*     */       {
/* 426 */         if (j == 0)
/*     */         {
/* 429 */           i3 = 0;
/*     */         }
/* 432 */         else if (k == 0) {
/* 433 */           i2 = 0;
/*     */         }
/* 440 */         else if (i4 == 0)
/*     */         {
/* 444 */           if (n != 0) {
/* 445 */             i = 1;
/* 446 */             n = 0;
/*     */           }
/*     */ 
/* 449 */           i3 = 0;
/*     */         }
/* 451 */         else if (i5 == 0)
/*     */         {
/* 454 */           if (n != 0) {
/* 455 */             i = -1;
/* 456 */             n = 0;
/*     */           }
/*     */ 
/* 459 */           i2 = 0;
/*     */         }
/*     */         else
/*     */         {
/* 464 */           if (i4 < i5) {
/* 465 */             return -1;
/*     */           }
/* 467 */           return 1;
/*     */         }
/*     */ 
/*     */       }
/* 475 */       else if (n != 0)
/*     */       {
/* 477 */         int i6 = CollationElementIterator.secondaryOrder(j);
/* 478 */         int i7 = CollationElementIterator.secondaryOrder(k);
/* 479 */         if (i6 != i7)
/*     */         {
/* 481 */           i = i6 < i7 ? -1 : 1;
/*     */ 
/* 483 */           n = 0;
/*     */         }
/* 487 */         else if (i1 != 0)
/*     */         {
/* 489 */           int i8 = CollationElementIterator.tertiaryOrder(j);
/* 490 */           int i9 = CollationElementIterator.tertiaryOrder(k);
/* 491 */           if (i8 != i9)
/*     */           {
/* 493 */             i = i8 < i9 ? -1 : 1;
/*     */ 
/* 495 */             i1 = 0;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 504 */     if (j != -1)
/*     */     {
/*     */       do
/*     */       {
/* 509 */         if (CollationElementIterator.primaryOrder(j) != 0)
/*     */         {
/* 512 */           return 1;
/*     */         }
/* 514 */         if (CollationElementIterator.secondaryOrder(j) != 0)
/*     */         {
/* 516 */           if (n != 0) {
/* 517 */             i = 1;
/* 518 */             n = 0;
/*     */           }
/*     */         }
/*     */       }
/* 521 */       while ((j = this.sourceCursor.next()) != -1);
/*     */     }
/* 523 */     else if (k != -1) {
/*     */       do
/*     */       {
/* 526 */         if (CollationElementIterator.primaryOrder(k) != 0)
/*     */         {
/* 529 */           return -1;
/* 530 */         }if (CollationElementIterator.secondaryOrder(k) != 0)
/*     */         {
/* 532 */           if (n != 0) {
/* 533 */             i = -1;
/* 534 */             n = 0;
/*     */           }
/*     */         }
/*     */       }
/* 537 */       while ((k = this.targetCursor.next()) != -1);
/*     */     }
/*     */ 
/* 542 */     if ((i == 0) && (getStrength() == 3)) {
/* 543 */       i4 = getDecomposition();
/*     */       Normalizer.Form localForm;
/* 545 */       if (i4 == 1)
/* 546 */         localForm = Normalizer.Form.NFD;
/* 547 */       else if (i4 == 2)
/* 548 */         localForm = Normalizer.Form.NFKD;
/*     */       else {
/* 550 */         return paramString1.compareTo(paramString2);
/*     */       }
/*     */ 
/* 553 */       String str1 = Normalizer.normalize(paramString1, localForm);
/* 554 */       String str2 = Normalizer.normalize(paramString2, localForm);
/* 555 */       return str1.compareTo(str2);
/*     */     }
/* 557 */     return i;
/*     */   }
/*     */ 
/*     */   public synchronized CollationKey getCollationKey(String paramString)
/*     */   {
/* 597 */     if (paramString == null) {
/* 598 */       return null;
/*     */     }
/* 600 */     if (this.primResult == null) {
/* 601 */       this.primResult = new StringBuffer();
/* 602 */       this.secResult = new StringBuffer();
/* 603 */       this.terResult = new StringBuffer();
/*     */     } else {
/* 605 */       this.primResult.setLength(0);
/* 606 */       this.secResult.setLength(0);
/* 607 */       this.terResult.setLength(0);
/*     */     }
/* 609 */     int i = 0;
/* 610 */     int j = getStrength() >= 1 ? 1 : 0;
/* 611 */     int k = getStrength() >= 2 ? 1 : 0;
/* 612 */     int m = -1;
/* 613 */     int n = -1;
/* 614 */     int i1 = 0;
/*     */ 
/* 616 */     if (this.sourceCursor == null)
/* 617 */       this.sourceCursor = getCollationElementIterator(paramString);
/*     */     else {
/* 619 */       this.sourceCursor.setText(paramString);
/*     */     }
/*     */ 
/* 623 */     while ((i = this.sourceCursor.next()) != -1)
/*     */     {
/* 626 */       m = CollationElementIterator.secondaryOrder(i);
/* 627 */       n = CollationElementIterator.tertiaryOrder(i);
/* 628 */       if (!CollationElementIterator.isIgnorable(i))
/*     */       {
/* 630 */         this.primResult.append((char)(CollationElementIterator.primaryOrder(i) + 1));
/*     */ 
/* 633 */         if (j != 0)
/*     */         {
/* 638 */           if ((this.tables.isFrenchSec()) && (i1 < this.secResult.length()))
/*     */           {
/* 644 */             RBCollationTables.reverse(this.secResult, i1, this.secResult.length());
/*     */           }
/*     */ 
/* 648 */           this.secResult.append((char)(m + 1));
/* 649 */           i1 = this.secResult.length();
/*     */         }
/* 651 */         if (k != 0) {
/* 652 */           this.terResult.append((char)(n + 1));
/*     */         }
/*     */       }
/*     */       else
/*     */       {
/* 657 */         if ((j != 0) && (m != 0)) {
/* 658 */           this.secResult.append((char)(m + this.tables.getMaxSecOrder() + 1));
/*     */         }
/* 660 */         if ((k != 0) && (n != 0)) {
/* 661 */           this.terResult.append((char)(n + this.tables.getMaxTerOrder() + 1));
/*     */         }
/*     */       }
/*     */     }
/* 665 */     if (this.tables.isFrenchSec())
/*     */     {
/* 667 */       if (i1 < this.secResult.length())
/*     */       {
/* 670 */         RBCollationTables.reverse(this.secResult, i1, this.secResult.length());
/*     */       }
/*     */ 
/* 673 */       RBCollationTables.reverse(this.secResult, 0, this.secResult.length());
/*     */     }
/* 675 */     this.primResult.append('\000');
/* 676 */     this.secResult.append('\000');
/* 677 */     this.secResult.append(this.terResult.toString());
/* 678 */     this.primResult.append(this.secResult.toString());
/*     */ 
/* 680 */     if (getStrength() == 3) {
/* 681 */       this.primResult.append('\000');
/* 682 */       int i2 = getDecomposition();
/* 683 */       if (i2 == 1)
/* 684 */         this.primResult.append(Normalizer.normalize(paramString, Normalizer.Form.NFD));
/* 685 */       else if (i2 == 2)
/* 686 */         this.primResult.append(Normalizer.normalize(paramString, Normalizer.Form.NFKD));
/*     */       else {
/* 688 */         this.primResult.append(paramString);
/*     */       }
/*     */     }
/* 691 */     return new RuleBasedCollationKey(paramString, this.primResult.toString());
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 701 */     if (getClass() == RuleBasedCollator.class) {
/* 702 */       return new RuleBasedCollator(this);
/*     */     }
/*     */ 
/* 705 */     RuleBasedCollator localRuleBasedCollator = (RuleBasedCollator)super.clone();
/* 706 */     localRuleBasedCollator.primResult = null;
/* 707 */     localRuleBasedCollator.secResult = null;
/* 708 */     localRuleBasedCollator.terResult = null;
/* 709 */     localRuleBasedCollator.sourceCursor = null;
/* 710 */     localRuleBasedCollator.targetCursor = null;
/* 711 */     return localRuleBasedCollator;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 722 */     if (paramObject == null) return false;
/* 723 */     if (!super.equals(paramObject)) return false;
/* 724 */     RuleBasedCollator localRuleBasedCollator = (RuleBasedCollator)paramObject;
/*     */ 
/* 726 */     return getRules().equals(localRuleBasedCollator.getRules());
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 733 */     return getRules().hashCode();
/*     */   }
/*     */ 
/*     */   RBCollationTables getTables()
/*     */   {
/* 740 */     return this.tables;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.RuleBasedCollator
 * JD-Core Version:    0.6.2
 */