/*     */ package java.text;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import sun.text.ComposedCharIter;
/*     */ import sun.text.IntHashtable;
/*     */ import sun.text.UCompactIntArray;
/*     */ import sun.text.normalizer.NormalizerImpl;
/*     */ 
/*     */ final class RBTableBuilder
/*     */ {
/*     */   static final int CHARINDEX = 1879048192;
/*     */   private static final int IGNORABLEMASK = 65535;
/*     */   private static final int PRIMARYORDERINCREMENT = 65536;
/*     */   private static final int SECONDARYORDERINCREMENT = 256;
/*     */   private static final int TERTIARYORDERINCREMENT = 1;
/*     */   private static final int INITIALTABLESIZE = 20;
/*     */   private static final int MAXKEYSIZE = 5;
/* 600 */   private RBCollationTables.BuildAPI tables = null;
/* 601 */   private MergeCollation mPattern = null;
/* 602 */   private boolean isOverIgnore = false;
/* 603 */   private char[] keyBuf = new char[5];
/* 604 */   private IntHashtable contractFlags = new IntHashtable(100);
/*     */ 
/* 609 */   private boolean frenchSec = false;
/* 610 */   private boolean seAsianSwapping = false;
/*     */ 
/* 612 */   private UCompactIntArray mapping = null;
/* 613 */   private Vector contractTable = null;
/* 614 */   private Vector expandTable = null;
/*     */ 
/* 616 */   private short maxSecOrder = 0;
/* 617 */   private short maxTerOrder = 0;
/*     */ 
/*     */   public RBTableBuilder(RBCollationTables.BuildAPI paramBuildAPI)
/*     */   {
/*  66 */     this.tables = paramBuildAPI;
/*     */   }
/*     */ 
/*     */   public void build(String paramString, int paramInt)
/*     */     throws ParseException
/*     */   {
/*  80 */     int i = 1;
/*  81 */     int j = 0;
/*     */ 
/*  84 */     if (paramString.length() == 0) {
/*  85 */       throw new ParseException("Build rules empty.", 0);
/*     */     }
/*     */ 
/*  88 */     this.mapping = new UCompactIntArray(-1);
/*     */ 
/* 107 */     paramString = NormalizerImpl.canonicalDecomposeWithSingleQuotation(paramString);
/*     */ 
/* 117 */     this.mPattern = new MergeCollation(paramString);
/*     */ 
/* 119 */     int k = 0;
/*     */ 
/* 122 */     for (j = 0; j < this.mPattern.getCount(); j++)
/*     */     {
/* 124 */       PatternEntry localPatternEntry = this.mPattern.getItemAt(j);
/* 125 */       if (localPatternEntry != null) {
/* 126 */         String str2 = localPatternEntry.getChars();
/* 127 */         if (str2.length() > 1) {
/* 128 */           switch (str2.charAt(str2.length() - 1)) {
/*     */           case '@':
/* 130 */             this.frenchSec = true;
/* 131 */             str2 = str2.substring(0, str2.length() - 1);
/* 132 */             break;
/*     */           case '!':
/* 134 */             this.seAsianSwapping = true;
/* 135 */             str2 = str2.substring(0, str2.length() - 1);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 140 */         k = increment(localPatternEntry.getStrength(), k);
/* 141 */         String str1 = localPatternEntry.getExtension();
/*     */ 
/* 143 */         if (str1.length() != 0) {
/* 144 */           addExpandOrder(str2, str1, k);
/*     */         }
/*     */         else
/*     */         {
/*     */           char c;
/* 145 */           if (str2.length() > 1) {
/* 146 */             c = str2.charAt(0);
/* 147 */             if ((Character.isHighSurrogate(c)) && (str2.length() == 2))
/* 148 */               addOrder(Character.toCodePoint(c, str2.charAt(1)), k);
/*     */             else
/* 150 */               addContractOrder(str2, k);
/*     */           }
/*     */           else {
/* 153 */             c = str2.charAt(0);
/* 154 */             addOrder(c, k);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 158 */     addComposedChars();
/*     */ 
/* 160 */     commit();
/* 161 */     this.mapping.compact();
/*     */ 
/* 171 */     this.tables.fillInTables(this.frenchSec, this.seAsianSwapping, this.mapping, this.contractTable, this.expandTable, this.contractFlags, this.maxSecOrder, this.maxTerOrder);
/*     */   }
/*     */ 
/*     */   private void addComposedChars()
/*     */     throws ParseException
/*     */   {
/* 180 */     ComposedCharIter localComposedCharIter = new ComposedCharIter();
/*     */     int i;
/* 182 */     while ((i = localComposedCharIter.next()) != -1)
/* 183 */       if (getCharOrder(i) == -1)
/*     */       {
/* 200 */         String str = localComposedCharIter.decomposition();
/*     */         int j;
/* 209 */         if (str.length() == 1) {
/* 210 */           j = getCharOrder(str.charAt(0));
/* 211 */           if (j != -1)
/* 212 */             addOrder(i, j);
/*     */         }
/*     */         else
/*     */         {
/*     */           int m;
/* 215 */           if (str.length() == 2) {
/* 216 */             j = str.charAt(0);
/* 217 */             if (Character.isHighSurrogate(j)) {
/* 218 */               m = getCharOrder(str.codePointAt(0));
/* 219 */               if (m == -1) continue;
/* 220 */               addOrder(i, m);
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 225 */             int k = getContractOrder(str);
/* 226 */             if (k != -1) {
/* 227 */               addOrder(i, k);
/*     */             }
/*     */             else
/*     */             {
/* 235 */               m = 1;
/* 236 */               for (int n = 0; n < str.length(); n++) {
/* 237 */                 if (getCharOrder(str.charAt(n)) == -1) {
/* 238 */                   m = 0;
/* 239 */                   break;
/*     */                 }
/*     */               }
/* 242 */               if (m != 0)
/* 243 */                 addExpandOrder(i, str, -1);
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   private final void commit()
/*     */   {
/* 264 */     if (this.expandTable != null)
/* 265 */       for (int i = 0; i < this.expandTable.size(); i++) {
/* 266 */         int[] arrayOfInt = (int[])this.expandTable.elementAt(i);
/* 267 */         for (int j = 0; j < arrayOfInt.length; j++) {
/* 268 */           int k = arrayOfInt[j];
/* 269 */           if ((k < 2113929216) && (k > 1879048192))
/*     */           {
/* 271 */             int m = k - 1879048192;
/*     */ 
/* 274 */             int n = getCharOrder(m);
/*     */ 
/* 276 */             if (n == -1)
/*     */             {
/* 278 */               arrayOfInt[j] = (0xFFFF & m);
/*     */             }
/*     */             else
/* 281 */               arrayOfInt[j] = n;
/*     */           }
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   private final int increment(int paramInt1, int paramInt2)
/*     */   {
/* 293 */     switch (paramInt1)
/*     */     {
/*     */     case 0:
/* 297 */       paramInt2 += 65536;
/* 298 */       paramInt2 &= -65536;
/* 299 */       this.isOverIgnore = true;
/* 300 */       break;
/*     */     case 1:
/* 303 */       paramInt2 += 256;
/* 304 */       paramInt2 &= -256;
/*     */ 
/* 306 */       if (!this.isOverIgnore)
/* 307 */         this.maxSecOrder = ((short)(this.maxSecOrder + 1)); break;
/*     */     case 2:
/* 311 */       paramInt2++;
/*     */ 
/* 313 */       if (!this.isOverIgnore)
/* 314 */         this.maxTerOrder = ((short)(this.maxTerOrder + 1));
/*     */       break;
/*     */     }
/* 317 */     return paramInt2;
/*     */   }
/*     */ 
/*     */   private final void addOrder(int paramInt1, int paramInt2)
/*     */   {
/* 326 */     int i = this.mapping.elementAt(paramInt1);
/*     */ 
/* 328 */     if (i >= 2130706432)
/*     */     {
/* 332 */       int j = 1;
/* 333 */       if (Character.isSupplementaryCodePoint(paramInt1))
/* 334 */         j = Character.toChars(paramInt1, this.keyBuf, 0);
/*     */       else {
/* 336 */         this.keyBuf[0] = ((char)paramInt1);
/*     */       }
/* 338 */       addContractOrder(new String(this.keyBuf, 0, j), paramInt2);
/*     */     }
/*     */     else
/*     */     {
/* 342 */       this.mapping.setElementAt(paramInt1, paramInt2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void addContractOrder(String paramString, int paramInt) {
/* 347 */     addContractOrder(paramString, paramInt, true);
/*     */   }
/*     */ 
/*     */   private final void addContractOrder(String paramString, int paramInt, boolean paramBoolean)
/*     */   {
/* 356 */     if (this.contractTable == null) {
/* 357 */       this.contractTable = new Vector(20);
/*     */     }
/*     */ 
/* 361 */     int i = paramString.codePointAt(0);
/*     */ 
/* 368 */     int j = this.mapping.elementAt(i);
/* 369 */     Vector localVector = getContractValuesImpl(j - 2130706432);
/*     */ 
/* 371 */     if (localVector == null)
/*     */     {
/* 373 */       k = 2130706432 + this.contractTable.size();
/* 374 */       localVector = new Vector(20);
/* 375 */       this.contractTable.addElement(localVector);
/*     */ 
/* 379 */       localVector.addElement(new EntryPair(paramString.substring(0, Character.charCount(i)), j));
/* 380 */       this.mapping.setElementAt(i, k);
/*     */     }
/*     */ 
/* 384 */     int k = RBCollationTables.getEntry(localVector, paramString, paramBoolean);
/*     */     EntryPair localEntryPair;
/* 385 */     if (k != -1) {
/* 386 */       localEntryPair = (EntryPair)localVector.elementAt(k);
/* 387 */       localEntryPair.value = paramInt;
/*     */     } else {
/* 389 */       localEntryPair = (EntryPair)localVector.lastElement();
/*     */ 
/* 396 */       if (paramString.length() > localEntryPair.entryName.length())
/* 397 */         localVector.addElement(new EntryPair(paramString, paramInt, paramBoolean));
/*     */       else {
/* 399 */         localVector.insertElementAt(new EntryPair(paramString, paramInt, paramBoolean), localVector.size() - 1);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 407 */     if ((paramBoolean) && (paramString.length() > 1)) {
/* 408 */       addContractFlags(paramString);
/* 409 */       addContractOrder(new StringBuffer(paramString).reverse().toString(), paramInt, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   private int getContractOrder(String paramString)
/*     */   {
/* 421 */     int i = -1;
/* 422 */     if (this.contractTable != null) {
/* 423 */       int j = paramString.codePointAt(0);
/*     */ 
/* 429 */       Vector localVector = getContractValues(j);
/* 430 */       if (localVector != null) {
/* 431 */         int k = RBCollationTables.getEntry(localVector, paramString, true);
/* 432 */         if (k != -1) {
/* 433 */           EntryPair localEntryPair = (EntryPair)localVector.elementAt(k);
/* 434 */           i = localEntryPair.value;
/*     */         }
/*     */       }
/*     */     }
/* 438 */     return i;
/*     */   }
/*     */ 
/*     */   private final int getCharOrder(int paramInt) {
/* 442 */     int i = this.mapping.elementAt(paramInt);
/*     */ 
/* 444 */     if (i >= 2130706432) {
/* 445 */       Vector localVector = getContractValuesImpl(i - 2130706432);
/* 446 */       EntryPair localEntryPair = (EntryPair)localVector.firstElement();
/* 447 */       i = localEntryPair.value;
/*     */     }
/* 449 */     return i;
/*     */   }
/*     */ 
/*     */   private Vector getContractValues(int paramInt)
/*     */   {
/* 459 */     int i = this.mapping.elementAt(paramInt);
/* 460 */     return getContractValuesImpl(i - 2130706432);
/*     */   }
/*     */ 
/*     */   private Vector getContractValuesImpl(int paramInt)
/*     */   {
/* 465 */     if (paramInt >= 0)
/*     */     {
/* 467 */       return (Vector)this.contractTable.elementAt(paramInt);
/*     */     }
/*     */ 
/* 471 */     return null;
/*     */   }
/*     */ 
/*     */   private final void addExpandOrder(String paramString1, String paramString2, int paramInt)
/*     */     throws ParseException
/*     */   {
/* 483 */     int i = addExpansion(paramInt, paramString2);
/*     */ 
/* 486 */     if (paramString1.length() > 1) {
/* 487 */       char c1 = paramString1.charAt(0);
/* 488 */       if ((Character.isHighSurrogate(c1)) && (paramString1.length() == 2)) {
/* 489 */         char c2 = paramString1.charAt(1);
/* 490 */         if (Character.isLowSurrogate(c2))
/*     */         {
/* 492 */           addOrder(Character.toCodePoint(c1, c2), i);
/*     */         }
/*     */       } else {
/* 495 */         addContractOrder(paramString1, i);
/*     */       }
/*     */     } else {
/* 498 */       addOrder(paramString1.charAt(0), i);
/*     */     }
/*     */   }
/*     */ 
/*     */   private final void addExpandOrder(int paramInt1, String paramString, int paramInt2)
/*     */     throws ParseException
/*     */   {
/* 505 */     int i = addExpansion(paramInt2, paramString);
/* 506 */     addOrder(paramInt1, i);
/*     */   }
/*     */ 
/*     */   private int addExpansion(int paramInt, String paramString)
/*     */   {
/* 515 */     if (this.expandTable == null) {
/* 516 */       this.expandTable = new Vector(20);
/*     */     }
/*     */ 
/* 520 */     int i = paramInt == -1 ? 0 : 1;
/*     */ 
/* 522 */     Object localObject = new int[paramString.length() + i];
/* 523 */     if (i == 1) {
/* 524 */       localObject[0] = paramInt;
/*     */     }
/*     */ 
/* 527 */     int j = i;
/* 528 */     for (int k = 0; k < paramString.length(); k++) {
/* 529 */       int n = paramString.charAt(k);
/*     */       int i1;
/* 532 */       if (Character.isHighSurrogate(n)) {
/* 533 */         k++;
/*     */         char c;
/* 533 */         if ((k == paramString.length()) || (!Character.isLowSurrogate(c = paramString.charAt(k))))
/*     */         {
/*     */           break;
/*     */         }
/*     */ 
/* 539 */         i1 = Character.toCodePoint(n, c);
/*     */       }
/*     */       else {
/* 542 */         i1 = n;
/*     */       }
/*     */ 
/* 545 */       int i2 = getCharOrder(i1);
/*     */ 
/* 547 */       if (i2 != -1) {
/* 548 */         localObject[(j++)] = i2;
/*     */       }
/*     */       else {
/* 551 */         localObject[(j++)] = (1879048192 + i1);
/*     */       }
/*     */     }
/* 554 */     if (j < localObject.length)
/*     */     {
/* 557 */       int[] arrayOfInt = new int[j];
/*     */       while (true) { j--; if (j < 0) break;
/* 559 */         arrayOfInt[j] = localObject[j];
/*     */       }
/* 561 */       localObject = arrayOfInt;
/*     */     }
/*     */ 
/* 564 */     int m = 2113929216 + this.expandTable.size();
/* 565 */     this.expandTable.addElement(localObject);
/*     */ 
/* 567 */     return m;
/*     */   }
/*     */ 
/*     */   private void addContractFlags(String paramString)
/*     */   {
/* 573 */     int i = paramString.length();
/* 574 */     for (int j = 0; j < i; j++) {
/* 575 */       char c1 = paramString.charAt(j);
/* 576 */       char c2 = Character.isHighSurrogate(c1) ? Character.toCodePoint(c1, paramString.charAt(++j)) : c1;
/*     */ 
/* 579 */       this.contractFlags.put(c2, 1);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.RBTableBuilder
 * JD-Core Version:    0.6.2
 */