/*     */ package java.awt.color;
/*     */ 
/*     */ import sun.java2d.cmm.ProfileDeferralInfo;
/*     */ 
/*     */ public class ICC_ProfileRGB extends ICC_Profile
/*     */ {
/*     */   static final long serialVersionUID = 8505067385152579334L;
/*     */   public static final int REDCOMPONENT = 0;
/*     */   public static final int GREENCOMPONENT = 1;
/*     */   public static final int BLUECOMPONENT = 2;
/*     */ 
/*     */   ICC_ProfileRGB(long paramLong)
/*     */   {
/* 118 */     super(paramLong);
/*     */   }
/*     */ 
/*     */   ICC_ProfileRGB(ProfileDeferralInfo paramProfileDeferralInfo)
/*     */   {
/* 128 */     super(paramProfileDeferralInfo);
/*     */   }
/*     */ 
/*     */   public float[] getMediaWhitePoint()
/*     */   {
/* 140 */     return super.getMediaWhitePoint();
/*     */   }
/*     */ 
/*     */   public float[][] getMatrix()
/*     */   {
/* 158 */     float[][] arrayOfFloat = new float[3][3];
/*     */ 
/* 161 */     float[] arrayOfFloat1 = getXYZTag(1918392666);
/* 162 */     arrayOfFloat[0][0] = arrayOfFloat1[0];
/* 163 */     arrayOfFloat[1][0] = arrayOfFloat1[1];
/* 164 */     arrayOfFloat[2][0] = arrayOfFloat1[2];
/* 165 */     arrayOfFloat1 = getXYZTag(1733843290);
/* 166 */     arrayOfFloat[0][1] = arrayOfFloat1[0];
/* 167 */     arrayOfFloat[1][1] = arrayOfFloat1[1];
/* 168 */     arrayOfFloat[2][1] = arrayOfFloat1[2];
/* 169 */     arrayOfFloat1 = getXYZTag(1649957210);
/* 170 */     arrayOfFloat[0][2] = arrayOfFloat1[0];
/* 171 */     arrayOfFloat[1][2] = arrayOfFloat1[1];
/* 172 */     arrayOfFloat[2][2] = arrayOfFloat1[2];
/* 173 */     return arrayOfFloat;
/*     */   }
/*     */ 
/*     */   public float getGamma(int paramInt)
/*     */   {
/*     */     int i;
/* 204 */     switch (paramInt) {
/*     */     case 0:
/* 206 */       i = 1918128707;
/* 207 */       break;
/*     */     case 1:
/* 210 */       i = 1733579331;
/* 211 */       break;
/*     */     case 2:
/* 214 */       i = 1649693251;
/* 215 */       break;
/*     */     default:
/* 218 */       throw new IllegalArgumentException("Must be Red, Green, or Blue");
/*     */     }
/*     */ 
/* 221 */     float f = super.getGamma(i);
/*     */ 
/* 223 */     return f;
/*     */   }
/*     */ 
/*     */   public short[] getTRC(int paramInt)
/*     */   {
/*     */     int i;
/* 260 */     switch (paramInt) {
/*     */     case 0:
/* 262 */       i = 1918128707;
/* 263 */       break;
/*     */     case 1:
/* 266 */       i = 1733579331;
/* 267 */       break;
/*     */     case 2:
/* 270 */       i = 1649693251;
/* 271 */       break;
/*     */     default:
/* 274 */       throw new IllegalArgumentException("Must be Red, Green, or Blue");
/*     */     }
/*     */ 
/* 277 */     short[] arrayOfShort = super.getTRC(i);
/*     */ 
/* 279 */     return arrayOfShort;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.color.ICC_ProfileRGB
 * JD-Core Version:    0.6.2
 */