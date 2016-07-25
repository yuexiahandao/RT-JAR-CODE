/*     */ package java.awt.font;
/*     */ 
/*     */ public final class TextHitInfo
/*     */ {
/*     */   private int charIndex;
/*     */   private boolean isLeadingEdge;
/*     */ 
/*     */   private TextHitInfo(int paramInt, boolean paramBoolean)
/*     */   {
/*  99 */     this.charIndex = paramInt;
/* 100 */     this.isLeadingEdge = paramBoolean;
/*     */   }
/*     */ 
/*     */   public int getCharIndex()
/*     */   {
/* 108 */     return this.charIndex;
/*     */   }
/*     */ 
/*     */   public boolean isLeadingEdge()
/*     */   {
/* 118 */     return this.isLeadingEdge;
/*     */   }
/*     */ 
/*     */   public int getInsertionIndex()
/*     */   {
/* 128 */     return this.isLeadingEdge ? this.charIndex : this.charIndex + 1;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 137 */     return this.charIndex;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 148 */     return ((paramObject instanceof TextHitInfo)) && (equals((TextHitInfo)paramObject));
/*     */   }
/*     */ 
/*     */   public boolean equals(TextHitInfo paramTextHitInfo)
/*     */   {
/* 162 */     return (paramTextHitInfo != null) && (this.charIndex == paramTextHitInfo.charIndex) && (this.isLeadingEdge == paramTextHitInfo.isLeadingEdge);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 173 */     return "TextHitInfo[" + this.charIndex + (this.isLeadingEdge ? "L" : "T") + "]";
/*     */   }
/*     */ 
/*     */   public static TextHitInfo leading(int paramInt)
/*     */   {
/* 184 */     return new TextHitInfo(paramInt, true);
/*     */   }
/*     */ 
/*     */   public static TextHitInfo trailing(int paramInt)
/*     */   {
/* 195 */     return new TextHitInfo(paramInt, false);
/*     */   }
/*     */ 
/*     */   public static TextHitInfo beforeOffset(int paramInt)
/*     */   {
/* 206 */     return new TextHitInfo(paramInt - 1, false);
/*     */   }
/*     */ 
/*     */   public static TextHitInfo afterOffset(int paramInt)
/*     */   {
/* 217 */     return new TextHitInfo(paramInt, true);
/*     */   }
/*     */ 
/*     */   public TextHitInfo getOtherHit()
/*     */   {
/* 227 */     if (this.isLeadingEdge) {
/* 228 */       return trailing(this.charIndex - 1);
/*     */     }
/* 230 */     return leading(this.charIndex + 1);
/*     */   }
/*     */ 
/*     */   public TextHitInfo getOffsetHit(int paramInt)
/*     */   {
/* 245 */     return new TextHitInfo(this.charIndex + paramInt, this.isLeadingEdge);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.font.TextHitInfo
 * JD-Core Version:    0.6.2
 */