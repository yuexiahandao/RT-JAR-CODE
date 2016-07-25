/*     */ package javax.print.attribute;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public abstract class ResolutionSyntax
/*     */   implements Serializable, Cloneable
/*     */ {
/*     */   private static final long serialVersionUID = 2706743076526672017L;
/*     */   private int crossFeedResolution;
/*     */   private int feedResolution;
/*     */   public static final int DPI = 100;
/*     */   public static final int DPCM = 254;
/*     */ 
/*     */   public ResolutionSyntax(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 133 */     if (paramInt1 < 1) {
/* 134 */       throw new IllegalArgumentException("crossFeedResolution is < 1");
/*     */     }
/* 136 */     if (paramInt2 < 1) {
/* 137 */       throw new IllegalArgumentException("feedResolution is < 1");
/*     */     }
/* 139 */     if (paramInt3 < 1) {
/* 140 */       throw new IllegalArgumentException("units is < 1");
/*     */     }
/*     */ 
/* 143 */     this.crossFeedResolution = (paramInt1 * paramInt3);
/* 144 */     this.feedResolution = (paramInt2 * paramInt3);
/*     */   }
/*     */ 
/*     */   private static int convertFromDphi(int paramInt1, int paramInt2)
/*     */   {
/* 163 */     if (paramInt2 < 1) {
/* 164 */       throw new IllegalArgumentException(": units is < 1");
/*     */     }
/* 166 */     int i = paramInt2 / 2;
/* 167 */     return (paramInt1 + i) / paramInt2;
/*     */   }
/*     */ 
/*     */   public int[] getResolution(int paramInt)
/*     */   {
/* 185 */     return new int[] { getCrossFeedResolution(paramInt), getFeedResolution(paramInt) };
/*     */   }
/*     */ 
/*     */   public int getCrossFeedResolution(int paramInt)
/*     */   {
/* 204 */     return convertFromDphi(this.crossFeedResolution, paramInt);
/*     */   }
/*     */ 
/*     */   public int getFeedResolution(int paramInt)
/*     */   {
/* 221 */     return convertFromDphi(this.feedResolution, paramInt);
/*     */   }
/*     */ 
/*     */   public String toString(int paramInt, String paramString)
/*     */   {
/* 244 */     StringBuffer localStringBuffer = new StringBuffer();
/* 245 */     localStringBuffer.append(getCrossFeedResolution(paramInt));
/* 246 */     localStringBuffer.append('x');
/* 247 */     localStringBuffer.append(getFeedResolution(paramInt));
/* 248 */     if (paramString != null) {
/* 249 */       localStringBuffer.append(' ');
/* 250 */       localStringBuffer.append(paramString);
/*     */     }
/* 252 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public boolean lessThanOrEquals(ResolutionSyntax paramResolutionSyntax)
/*     */   {
/* 278 */     return (this.crossFeedResolution <= paramResolutionSyntax.crossFeedResolution) && (this.feedResolution <= paramResolutionSyntax.feedResolution);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 306 */     return (paramObject != null) && ((paramObject instanceof ResolutionSyntax)) && (this.crossFeedResolution == ((ResolutionSyntax)paramObject).crossFeedResolution) && (this.feedResolution == ((ResolutionSyntax)paramObject).feedResolution);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 318 */     return this.crossFeedResolution & 0xFFFF | (this.feedResolution & 0xFFFF) << 16;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 329 */     StringBuffer localStringBuffer = new StringBuffer();
/* 330 */     localStringBuffer.append(this.crossFeedResolution);
/* 331 */     localStringBuffer.append('x');
/* 332 */     localStringBuffer.append(this.feedResolution);
/* 333 */     localStringBuffer.append(" dphi");
/* 334 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   protected int getCrossFeedResolutionDphi()
/*     */   {
/* 345 */     return this.crossFeedResolution;
/*     */   }
/*     */ 
/*     */   protected int getFeedResolutionDphi()
/*     */   {
/* 355 */     return this.feedResolution;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.attribute.ResolutionSyntax
 * JD-Core Version:    0.6.2
 */