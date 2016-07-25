/*     */ package java.text;
/*     */ 
/*     */ public class ParsePosition
/*     */ {
/*  65 */   int index = 0;
/*  66 */   int errorIndex = -1;
/*     */ 
/*     */   public int getIndex()
/*     */   {
/*  74 */     return this.index;
/*     */   }
/*     */ 
/*     */   public void setIndex(int paramInt)
/*     */   {
/*  81 */     this.index = paramInt;
/*     */   }
/*     */ 
/*     */   public ParsePosition(int paramInt)
/*     */   {
/*  88 */     this.index = paramInt;
/*     */   }
/*     */ 
/*     */   public void setErrorIndex(int paramInt)
/*     */   {
/*  98 */     this.errorIndex = paramInt;
/*     */   }
/*     */ 
/*     */   public int getErrorIndex()
/*     */   {
/* 108 */     return this.errorIndex;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 115 */     if (paramObject == null) return false;
/* 116 */     if (!(paramObject instanceof ParsePosition))
/* 117 */       return false;
/* 118 */     ParsePosition localParsePosition = (ParsePosition)paramObject;
/* 119 */     return (this.index == localParsePosition.index) && (this.errorIndex == localParsePosition.errorIndex);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 127 */     return this.errorIndex << 16 | this.index;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 135 */     return getClass().getName() + "[index=" + this.index + ",errorIndex=" + this.errorIndex + ']';
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.text.ParsePosition
 * JD-Core Version:    0.6.2
 */