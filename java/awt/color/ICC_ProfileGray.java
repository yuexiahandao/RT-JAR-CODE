/*     */ package java.awt.color;
/*     */ 
/*     */ import sun.java2d.cmm.ProfileDeferralInfo;
/*     */ 
/*     */ public class ICC_ProfileGray extends ICC_Profile
/*     */ {
/*     */   static final long serialVersionUID = -1124721290732002649L;
/*     */ 
/*     */   ICC_ProfileGray(long paramLong)
/*     */   {
/*  80 */     super(paramLong);
/*     */   }
/*     */ 
/*     */   ICC_ProfileGray(ProfileDeferralInfo paramProfileDeferralInfo)
/*     */   {
/*  87 */     super(paramProfileDeferralInfo);
/*     */   }
/*     */ 
/*     */   public float[] getMediaWhitePoint()
/*     */   {
/*  98 */     return super.getMediaWhitePoint();
/*     */   }
/*     */ 
/*     */   public float getGamma()
/*     */   {
/* 121 */     float f = super.getGamma(1800688195);
/* 122 */     return f;
/*     */   }
/*     */ 
/*     */   public short[] getTRC()
/*     */   {
/* 146 */     short[] arrayOfShort = super.getTRC(1800688195);
/* 147 */     return arrayOfShort;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.color.ICC_ProfileGray
 * JD-Core Version:    0.6.2
 */