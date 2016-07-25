/*     */ package java.text;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Stack;
/*     */ import java.util.Vector;
/*     */ 
/*     */ class DictionaryBasedBreakIterator extends RuleBasedBreakIterator
/*     */ {
/*     */   private BreakDictionary dictionary;
/*     */   private boolean[] categoryFlags;
/*     */   private int dictionaryCharCount;
/*     */   private int[] cachedBreakPositions;
/*     */   private int positionInCache;
/*     */ 
/*     */   public DictionaryBasedBreakIterator(String paramString1, String paramString2)
/*     */     throws IOException
/*     */   {
/* 119 */     super(paramString1);
/* 120 */     byte[] arrayOfByte = super.getAdditionalData();
/* 121 */     if (arrayOfByte != null) {
/* 122 */       prepareCategoryFlags(arrayOfByte);
/* 123 */       super.setAdditionalData(null);
/*     */     }
/* 125 */     this.dictionary = new BreakDictionary(paramString2);
/*     */   }
/*     */ 
/*     */   private void prepareCategoryFlags(byte[] paramArrayOfByte) {
/* 129 */     this.categoryFlags = new boolean[paramArrayOfByte.length];
/* 130 */     for (int i = 0; i < paramArrayOfByte.length; i++)
/* 131 */       this.categoryFlags[i] = (paramArrayOfByte[i] == 1 ? 1 : false);
/*     */   }
/*     */ 
/*     */   public void setText(CharacterIterator paramCharacterIterator)
/*     */   {
/* 136 */     super.setText(paramCharacterIterator);
/* 137 */     this.cachedBreakPositions = null;
/* 138 */     this.dictionaryCharCount = 0;
/* 139 */     this.positionInCache = 0;
/*     */   }
/*     */ 
/*     */   public int first()
/*     */   {
/* 148 */     this.cachedBreakPositions = null;
/* 149 */     this.dictionaryCharCount = 0;
/* 150 */     this.positionInCache = 0;
/* 151 */     return super.first();
/*     */   }
/*     */ 
/*     */   public int last()
/*     */   {
/* 160 */     this.cachedBreakPositions = null;
/* 161 */     this.dictionaryCharCount = 0;
/* 162 */     this.positionInCache = 0;
/* 163 */     return super.last();
/*     */   }
/*     */ 
/*     */   public int previous()
/*     */   {
/* 172 */     CharacterIterator localCharacterIterator = getText();
/*     */ 
/* 176 */     if ((this.cachedBreakPositions != null) && (this.positionInCache > 0)) {
/* 177 */       this.positionInCache -= 1;
/* 178 */       localCharacterIterator.setIndex(this.cachedBreakPositions[this.positionInCache]);
/* 179 */       return this.cachedBreakPositions[this.positionInCache];
/*     */     }
/*     */ 
/* 186 */     this.cachedBreakPositions = null;
/* 187 */     int i = super.previous();
/* 188 */     if (this.cachedBreakPositions != null) {
/* 189 */       this.positionInCache = (this.cachedBreakPositions.length - 2);
/*     */     }
/* 191 */     return i;
/*     */   }
/*     */ 
/*     */   public int preceding(int paramInt)
/*     */   {
/* 202 */     CharacterIterator localCharacterIterator = getText();
/* 203 */     checkOffset(paramInt, localCharacterIterator);
/*     */ 
/* 209 */     if ((this.cachedBreakPositions == null) || (paramInt <= this.cachedBreakPositions[0]) || (paramInt > this.cachedBreakPositions[(this.cachedBreakPositions.length - 1)]))
/*     */     {
/* 211 */       this.cachedBreakPositions = null;
/* 212 */       return super.preceding(paramInt);
/*     */     }
/*     */ 
/* 219 */     this.positionInCache = 0;
/*     */ 
/* 221 */     while ((this.positionInCache < this.cachedBreakPositions.length) && (paramInt > this.cachedBreakPositions[this.positionInCache])) {
/* 222 */       this.positionInCache += 1;
/*     */     }
/* 224 */     this.positionInCache -= 1;
/* 225 */     localCharacterIterator.setIndex(this.cachedBreakPositions[this.positionInCache]);
/* 226 */     return localCharacterIterator.getIndex();
/*     */   }
/*     */ 
/*     */   public int following(int paramInt)
/*     */   {
/* 237 */     CharacterIterator localCharacterIterator = getText();
/* 238 */     checkOffset(paramInt, localCharacterIterator);
/*     */ 
/* 244 */     if ((this.cachedBreakPositions == null) || (paramInt < this.cachedBreakPositions[0]) || (paramInt >= this.cachedBreakPositions[(this.cachedBreakPositions.length - 1)]))
/*     */     {
/* 246 */       this.cachedBreakPositions = null;
/* 247 */       return super.following(paramInt);
/*     */     }
/*     */ 
/* 254 */     this.positionInCache = 0;
/*     */ 
/* 256 */     while ((this.positionInCache < this.cachedBreakPositions.length) && (paramInt >= this.cachedBreakPositions[this.positionInCache])) {
/* 257 */       this.positionInCache += 1;
/*     */     }
/* 259 */     localCharacterIterator.setIndex(this.cachedBreakPositions[this.positionInCache]);
/* 260 */     return localCharacterIterator.getIndex();
/*     */   }
/*     */ 
/*     */   protected int handleNext()
/*     */   {
/* 268 */     CharacterIterator localCharacterIterator = getText();
/*     */ 
/* 273 */     if ((this.cachedBreakPositions == null) || (this.positionInCache == this.cachedBreakPositions.length - 1))
/*     */     {
/* 279 */       int i = localCharacterIterator.getIndex();
/* 280 */       this.dictionaryCharCount = 0;
/* 281 */       int j = super.handleNext();
/*     */ 
/* 286 */       if ((this.dictionaryCharCount > 1) && (j - i > 1)) {
/* 287 */         divideUpDictionaryRange(i, j);
/*     */       }
/*     */       else
/*     */       {
/* 293 */         this.cachedBreakPositions = null;
/* 294 */         return j;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 301 */     if (this.cachedBreakPositions != null) {
/* 302 */       this.positionInCache += 1;
/* 303 */       localCharacterIterator.setIndex(this.cachedBreakPositions[this.positionInCache]);
/* 304 */       return this.cachedBreakPositions[this.positionInCache];
/*     */     }
/* 306 */     return -9999;
/*     */   }
/*     */ 
/*     */   protected int lookupCategory(int paramInt)
/*     */   {
/* 318 */     int i = super.lookupCategory(paramInt);
/* 319 */     if ((i != -1) && (this.categoryFlags[i] != 0)) {
/* 320 */       this.dictionaryCharCount += 1;
/*     */     }
/* 322 */     return i;
/*     */   }
/*     */ 
/*     */   private void divideUpDictionaryRange(int paramInt1, int paramInt2)
/*     */   {
/* 334 */     CharacterIterator localCharacterIterator = getText();
/*     */ 
/* 340 */     localCharacterIterator.setIndex(paramInt1);
/* 341 */     int i = getCurrent();
/* 342 */     int j = lookupCategory(i);
/* 343 */     while ((j == -1) || (this.categoryFlags[j] == 0)) {
/* 344 */       i = getNext();
/* 345 */       j = lookupCategory(i);
/*     */     }
/*     */ 
/* 359 */     Object localObject1 = new Stack();
/* 360 */     Stack localStack1 = new Stack();
/* 361 */     Vector localVector = new Vector();
/*     */ 
/* 367 */     int k = 0;
/*     */ 
/* 376 */     int m = localCharacterIterator.getIndex();
/* 377 */     Stack localStack2 = null;
/*     */ 
/* 380 */     i = getCurrent();
/*     */     while (true)
/*     */     {
/* 386 */       if (this.dictionary.getNextState(k, 0) == -1) {
/* 387 */         localStack1.push(Integer.valueOf(localCharacterIterator.getIndex()));
/*     */       }
/*     */ 
/* 391 */       k = this.dictionary.getNextStateFromCharacter(k, i);
/*     */ 
/* 397 */       if (k == -1) {
/* 398 */         ((Stack)localObject1).push(Integer.valueOf(localCharacterIterator.getIndex()));
/* 399 */         break;
/*     */       }
/*     */ 
/* 406 */       if ((k == 0) || (localCharacterIterator.getIndex() >= paramInt2))
/*     */       {
/* 410 */         if (localCharacterIterator.getIndex() > m) {
/* 411 */           m = localCharacterIterator.getIndex();
/* 412 */           localStack2 = (Stack)((Stack)localObject1).clone();
/*     */         }
/*     */ 
/* 428 */         Object localObject2 = null;
/* 429 */         while ((!localStack1.isEmpty()) && (localVector.contains(localStack1.peek())))
/*     */         {
/* 431 */           localStack1.pop();
/*     */         }
/*     */ 
/* 439 */         if (localStack1.isEmpty()) {
/* 440 */           if (localStack2 != null) {
/* 441 */             localObject1 = localStack2;
/* 442 */             if (m >= paramInt2) break;
/* 443 */             localCharacterIterator.setIndex(m + 1);
/*     */           }
/*     */           else
/*     */           {
/* 450 */             if (((((Stack)localObject1).size() == 0) || (((Integer)((Stack)localObject1).peek()).intValue() != localCharacterIterator.getIndex())) && (localCharacterIterator.getIndex() != paramInt1))
/*     */             {
/* 453 */               ((Stack)localObject1).push(new Integer(localCharacterIterator.getIndex()));
/*     */             }
/* 455 */             getNext();
/* 456 */             ((Stack)localObject1).push(new Integer(localCharacterIterator.getIndex()));
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 466 */           Integer localInteger = (Integer)localStack1.pop();
/* 467 */           Object localObject3 = null;
/* 468 */           while ((!((Stack)localObject1).isEmpty()) && (localInteger.intValue() < ((Integer)((Stack)localObject1).peek()).intValue()))
/*     */           {
/* 470 */             localObject3 = ((Stack)localObject1).pop();
/* 471 */             localVector.addElement(localObject3);
/*     */           }
/* 473 */           ((Stack)localObject1).push(localInteger);
/* 474 */           localCharacterIterator.setIndex(((Integer)((Stack)localObject1).peek()).intValue());
/*     */         }
/*     */ 
/* 479 */         i = getCurrent();
/* 480 */         if (localCharacterIterator.getIndex() >= paramInt2)
/*     */         {
/*     */           break;
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/* 488 */         i = getNext();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 496 */     if (!((Stack)localObject1).isEmpty()) {
/* 497 */       ((Stack)localObject1).pop();
/*     */     }
/* 499 */     ((Stack)localObject1).push(Integer.valueOf(paramInt2));
/*     */ 
/* 506 */     this.cachedBreakPositions = new int[((Stack)localObject1).size() + 1];
/* 507 */     this.cachedBreakPositions[0] = paramInt1;
/*     */ 
/* 509 */     for (int n = 0; n < ((Stack)localObject1).size(); n++) {
/* 510 */       this.cachedBreakPositions[(n + 1)] = ((Integer)((Stack)localObject1).elementAt(n)).intValue();
/*     */     }
/* 512 */     this.positionInCache = 0;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.DictionaryBasedBreakIterator
 * JD-Core Version:    0.6.2
 */