/*     */ package java.text;
/*     */ 
/*     */ public class FieldPosition
/*     */ {
/*  79 */   int field = 0;
/*     */ 
/*  85 */   int endIndex = 0;
/*     */ 
/*  91 */   int beginIndex = 0;
/*     */   private Format.Field attribute;
/*     */ 
/*     */   public FieldPosition(int paramInt)
/*     */   {
/* 109 */     this.field = paramInt;
/*     */   }
/*     */ 
/*     */   public FieldPosition(Format.Field paramField)
/*     */   {
/* 122 */     this(paramField, -1);
/*     */   }
/*     */ 
/*     */   public FieldPosition(Format.Field paramField, int paramInt)
/*     */   {
/* 142 */     this.attribute = paramField;
/* 143 */     this.field = paramInt;
/*     */   }
/*     */ 
/*     */   public Format.Field getFieldAttribute()
/*     */   {
/* 155 */     return this.attribute;
/*     */   }
/*     */ 
/*     */   public int getField()
/*     */   {
/* 162 */     return this.field;
/*     */   }
/*     */ 
/*     */   public int getBeginIndex()
/*     */   {
/* 169 */     return this.beginIndex;
/*     */   }
/*     */ 
/*     */   public int getEndIndex()
/*     */   {
/* 177 */     return this.endIndex;
/*     */   }
/*     */ 
/*     */   public void setBeginIndex(int paramInt)
/*     */   {
/* 185 */     this.beginIndex = paramInt;
/*     */   }
/*     */ 
/*     */   public void setEndIndex(int paramInt)
/*     */   {
/* 193 */     this.endIndex = paramInt;
/*     */   }
/*     */ 
/*     */   Format.FieldDelegate getFieldDelegate()
/*     */   {
/* 203 */     return new Delegate(null);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 211 */     if (paramObject == null) return false;
/* 212 */     if (!(paramObject instanceof FieldPosition))
/* 213 */       return false;
/* 214 */     FieldPosition localFieldPosition = (FieldPosition)paramObject;
/* 215 */     if (this.attribute == null) {
/* 216 */       if (localFieldPosition.attribute != null) {
/* 217 */         return false;
/*     */       }
/*     */     }
/* 220 */     else if (!this.attribute.equals(localFieldPosition.attribute)) {
/* 221 */       return false;
/*     */     }
/* 223 */     return (this.beginIndex == localFieldPosition.beginIndex) && (this.endIndex == localFieldPosition.endIndex) && (this.field == localFieldPosition.field);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 233 */     return this.field << 24 | this.beginIndex << 16 | this.endIndex;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 241 */     return getClass().getName() + "[field=" + this.field + ",attribute=" + this.attribute + ",beginIndex=" + this.beginIndex + ",endIndex=" + this.endIndex + ']';
/*     */   }
/*     */ 
/*     */   private boolean matchesField(Format.Field paramField)
/*     */   {
/* 253 */     if (this.attribute != null) {
/* 254 */       return this.attribute.equals(paramField);
/*     */     }
/* 256 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean matchesField(Format.Field paramField, int paramInt)
/*     */   {
/* 265 */     if (this.attribute != null) {
/* 266 */       return this.attribute.equals(paramField);
/*     */     }
/* 268 */     return paramInt == this.field;
/*     */   }
/*     */ 
/*     */   private class Delegate
/*     */     implements Format.FieldDelegate
/*     */   {
/*     */     private boolean encounteredField;
/*     */ 
/*     */     private Delegate()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void formatted(Format.Field paramField, Object paramObject, int paramInt1, int paramInt2, StringBuffer paramStringBuffer)
/*     */     {
/* 287 */       if ((!this.encounteredField) && (FieldPosition.this.matchesField(paramField))) {
/* 288 */         FieldPosition.this.setBeginIndex(paramInt1);
/* 289 */         FieldPosition.this.setEndIndex(paramInt2);
/* 290 */         this.encounteredField = (paramInt1 != paramInt2);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void formatted(int paramInt1, Format.Field paramField, Object paramObject, int paramInt2, int paramInt3, StringBuffer paramStringBuffer)
/*     */     {
/* 296 */       if ((!this.encounteredField) && (FieldPosition.this.matchesField(paramField, paramInt1))) {
/* 297 */         FieldPosition.this.setBeginIndex(paramInt2);
/* 298 */         FieldPosition.this.setEndIndex(paramInt3);
/* 299 */         this.encounteredField = (paramInt2 != paramInt3);
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.FieldPosition
 * JD-Core Version:    0.6.2
 */