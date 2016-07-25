/*     */ package java.text;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ 
/*     */ class CharacterIteratorFieldDelegate
/*     */   implements Format.FieldDelegate
/*     */ {
/*     */   private ArrayList attributedStrings;
/*     */   private int size;
/*     */ 
/*     */   CharacterIteratorFieldDelegate()
/*     */   {
/*  53 */     this.attributedStrings = new ArrayList();
/*     */   }
/*     */ 
/*     */   public void formatted(Format.Field paramField, Object paramObject, int paramInt1, int paramInt2, StringBuffer paramStringBuffer)
/*     */   {
/*  58 */     if (paramInt1 != paramInt2)
/*     */     {
/*     */       int i;
/*  59 */       if (paramInt1 < this.size)
/*     */       {
/*  61 */         i = this.size;
/*  62 */         int j = this.attributedStrings.size() - 1;
/*     */ 
/*  64 */         while (paramInt1 < i) {
/*  65 */           AttributedString localAttributedString2 = (AttributedString)this.attributedStrings.get(j--);
/*     */ 
/*  67 */           int k = i - localAttributedString2.length();
/*  68 */           int m = Math.max(0, paramInt1 - k);
/*     */ 
/*  70 */           localAttributedString2.addAttribute(paramField, paramObject, m, Math.min(paramInt2 - paramInt1, localAttributedString2.length() - m) + m);
/*     */ 
/*  73 */           i = k;
/*     */         }
/*     */       }
/*  76 */       if (this.size < paramInt1)
/*     */       {
/*  78 */         this.attributedStrings.add(new AttributedString(paramStringBuffer.substring(this.size, paramInt1)));
/*     */ 
/*  80 */         this.size = paramInt1;
/*     */       }
/*  82 */       if (this.size < paramInt2)
/*     */       {
/*  84 */         i = Math.max(paramInt1, this.size);
/*  85 */         AttributedString localAttributedString1 = new AttributedString(paramStringBuffer.substring(i, paramInt2));
/*     */ 
/*  88 */         localAttributedString1.addAttribute(paramField, paramObject);
/*  89 */         this.attributedStrings.add(localAttributedString1);
/*  90 */         this.size = paramInt2;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void formatted(int paramInt1, Format.Field paramField, Object paramObject, int paramInt2, int paramInt3, StringBuffer paramStringBuffer)
/*     */   {
/*  97 */     formatted(paramField, paramObject, paramInt2, paramInt3, paramStringBuffer);
/*     */   }
/*     */ 
/*     */   public AttributedCharacterIterator getIterator(String paramString)
/*     */   {
/* 109 */     if (paramString.length() > this.size) {
/* 110 */       this.attributedStrings.add(new AttributedString(paramString.substring(this.size)));
/*     */ 
/* 112 */       this.size = paramString.length();
/*     */     }
/* 114 */     int i = this.attributedStrings.size();
/* 115 */     AttributedCharacterIterator[] arrayOfAttributedCharacterIterator = new AttributedCharacterIterator[i];
/*     */ 
/* 118 */     for (int j = 0; j < i; j++) {
/* 119 */       arrayOfAttributedCharacterIterator[j] = ((AttributedString)this.attributedStrings.get(j)).getIterator();
/*     */     }
/*     */ 
/* 122 */     return new AttributedString(arrayOfAttributedCharacterIterator).getIterator();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.CharacterIteratorFieldDelegate
 * JD-Core Version:    0.6.2
 */