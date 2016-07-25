/*     */ package java.awt.color;
/*     */ 
/*     */ import sun.java2d.cmm.CMSManager;
/*     */ import sun.java2d.cmm.ColorTransform;
/*     */ import sun.java2d.cmm.PCMM;
/*     */ 
/*     */ public class ICC_ColorSpace extends ColorSpace
/*     */ {
/*     */   static final long serialVersionUID = 3455889114070431483L;
/*     */   private ICC_Profile thisProfile;
/*     */   private float[] minVal;
/*     */   private float[] maxVal;
/*     */   private float[] diffMinMax;
/*     */   private float[] invDiffMinMax;
/*  96 */   private boolean needScaleInit = true;
/*     */   private transient ColorTransform this2srgb;
/*     */   private transient ColorTransform srgb2this;
/*     */   private transient ColorTransform this2xyz;
/*     */   private transient ColorTransform xyz2this;
/*     */ 
/*     */   public ICC_ColorSpace(ICC_Profile paramICC_Profile)
/*     */   {
/* 112 */     super(paramICC_Profile.getColorSpaceType(), paramICC_Profile.getNumComponents());
/*     */ 
/* 114 */     int i = paramICC_Profile.getProfileClass();
/*     */ 
/* 117 */     if ((i != 0) && (i != 1) && (i != 2) && (i != 4) && (i != 6) && (i != 5))
/*     */     {
/* 123 */       throw new IllegalArgumentException("Invalid profile type");
/*     */     }
/*     */ 
/* 126 */     this.thisProfile = paramICC_Profile;
/* 127 */     setMinMax();
/*     */   }
/*     */ 
/*     */   public ICC_Profile getProfile()
/*     */   {
/* 135 */     return this.thisProfile;
/*     */   }
/*     */ 
/*     */   public float[] toRGB(float[] paramArrayOfFloat)
/*     */   {
/* 161 */     if (this.this2srgb == null) {
/* 162 */       ColorTransform[] arrayOfColorTransform = new ColorTransform[2];
/* 163 */       localObject = (ICC_ColorSpace)ColorSpace.getInstance(1000);
/*     */ 
/* 165 */       PCMM localPCMM = CMSManager.getModule();
/* 166 */       arrayOfColorTransform[0] = localPCMM.createTransform(this.thisProfile, -1, 1);
/*     */ 
/* 168 */       arrayOfColorTransform[1] = localPCMM.createTransform(((ICC_ColorSpace)localObject).getProfile(), -1, 2);
/*     */ 
/* 170 */       this.this2srgb = localPCMM.createTransform(arrayOfColorTransform);
/* 171 */       if (this.needScaleInit) {
/* 172 */         setComponentScaling();
/*     */       }
/*     */     }
/*     */ 
/* 176 */     int i = getNumComponents();
/* 177 */     Object localObject = new short[i];
/* 178 */     for (int j = 0; j < i; j++) {
/* 179 */       localObject[j] = ((short)(int)((paramArrayOfFloat[j] - this.minVal[j]) * this.invDiffMinMax[j] + 0.5F));
/*     */     }
/*     */ 
/* 182 */     localObject = this.this2srgb.colorConvert((short[])localObject, null);
/* 183 */     float[] arrayOfFloat = new float[3];
/* 184 */     for (int k = 0; k < 3; k++) {
/* 185 */       arrayOfFloat[k] = ((localObject[k] & 0xFFFF) / 65535.0F);
/*     */     }
/* 187 */     return arrayOfFloat;
/*     */   }
/*     */ 
/*     */   public float[] fromRGB(float[] paramArrayOfFloat)
/*     */   {
/* 213 */     if (this.srgb2this == null) {
/* 214 */       localObject1 = new ColorTransform[2];
/* 215 */       ICC_ColorSpace localICC_ColorSpace = (ICC_ColorSpace)ColorSpace.getInstance(1000);
/*     */ 
/* 217 */       localObject2 = CMSManager.getModule();
/* 218 */       localObject1[0] = ((PCMM)localObject2).createTransform(localICC_ColorSpace.getProfile(), -1, 1);
/*     */ 
/* 220 */       localObject1[1] = ((PCMM)localObject2).createTransform(this.thisProfile, -1, 2);
/*     */ 
/* 222 */       this.srgb2this = ((PCMM)localObject2).createTransform((ColorTransform[])localObject1);
/* 223 */       if (this.needScaleInit) {
/* 224 */         setComponentScaling();
/*     */       }
/*     */     }
/*     */ 
/* 228 */     Object localObject1 = new short[3];
/* 229 */     for (int i = 0; i < 3; i++) {
/* 230 */       localObject1[i] = ((short)(int)(paramArrayOfFloat[i] * 65535.0F + 0.5F));
/*     */     }
/* 232 */     localObject1 = this.srgb2this.colorConvert((short[])localObject1, null);
/* 233 */     i = getNumComponents();
/* 234 */     Object localObject2 = new float[i];
/* 235 */     for (int j = 0; j < i; j++) {
/* 236 */       localObject2[j] = ((localObject1[j] & 0xFFFF) / 65535.0F * this.diffMinMax[j] + this.minVal[j]);
/*     */     }
/*     */ 
/* 239 */     return localObject2;
/*     */   }
/*     */ 
/*     */   public float[] toCIEXYZ(float[] paramArrayOfFloat)
/*     */   {
/* 348 */     if (this.this2xyz == null) {
/* 349 */       ColorTransform[] arrayOfColorTransform = new ColorTransform[2];
/* 350 */       localObject = (ICC_ColorSpace)ColorSpace.getInstance(1001);
/*     */ 
/* 352 */       PCMM localPCMM = CMSManager.getModule();
/*     */       try {
/* 354 */         arrayOfColorTransform[0] = localPCMM.createTransform(this.thisProfile, 1, 1);
/*     */       }
/*     */       catch (CMMException localCMMException)
/*     */       {
/* 358 */         arrayOfColorTransform[0] = localPCMM.createTransform(this.thisProfile, -1, 1);
/*     */       }
/*     */ 
/* 361 */       arrayOfColorTransform[1] = localPCMM.createTransform(((ICC_ColorSpace)localObject).getProfile(), -1, 2);
/*     */ 
/* 363 */       this.this2xyz = localPCMM.createTransform(arrayOfColorTransform);
/* 364 */       if (this.needScaleInit) {
/* 365 */         setComponentScaling();
/*     */       }
/*     */     }
/*     */ 
/* 369 */     int i = getNumComponents();
/* 370 */     Object localObject = new short[i];
/* 371 */     for (int j = 0; j < i; j++) {
/* 372 */       localObject[j] = ((short)(int)((paramArrayOfFloat[j] - this.minVal[j]) * this.invDiffMinMax[j] + 0.5F));
/*     */     }
/*     */ 
/* 375 */     localObject = this.this2xyz.colorConvert((short[])localObject, null);
/* 376 */     float f = 1.99997F;
/*     */ 
/* 378 */     float[] arrayOfFloat = new float[3];
/* 379 */     for (int k = 0; k < 3; k++) {
/* 380 */       arrayOfFloat[k] = ((localObject[k] & 0xFFFF) / 65535.0F * f);
/*     */     }
/* 382 */     return arrayOfFloat;
/*     */   }
/*     */ 
/*     */   public float[] fromCIEXYZ(float[] paramArrayOfFloat)
/*     */   {
/* 493 */     if (this.xyz2this == null) {
/* 494 */       localObject = new ColorTransform[2];
/* 495 */       ICC_ColorSpace localICC_ColorSpace = (ICC_ColorSpace)ColorSpace.getInstance(1001);
/*     */ 
/* 497 */       PCMM localPCMM = CMSManager.getModule();
/* 498 */       localObject[0] = localPCMM.createTransform(localICC_ColorSpace.getProfile(), -1, 1);
/*     */       try
/*     */       {
/* 501 */         localObject[1] = localPCMM.createTransform(this.thisProfile, 1, 2);
/*     */       }
/*     */       catch (CMMException localCMMException)
/*     */       {
/* 505 */         localObject[1] = CMSManager.getModule().createTransform(this.thisProfile, -1, 2);
/*     */       }
/*     */ 
/* 508 */       this.xyz2this = localPCMM.createTransform((ColorTransform[])localObject);
/* 509 */       if (this.needScaleInit) {
/* 510 */         setComponentScaling();
/*     */       }
/*     */     }
/*     */ 
/* 514 */     Object localObject = new short[3];
/* 515 */     float f1 = 1.99997F;
/* 516 */     float f2 = 65535.0F / f1;
/*     */ 
/* 518 */     for (int i = 0; i < 3; i++) {
/* 519 */       localObject[i] = ((short)(int)(paramArrayOfFloat[i] * f2 + 0.5F));
/*     */     }
/* 521 */     localObject = this.xyz2this.colorConvert((short[])localObject, null);
/* 522 */     i = getNumComponents();
/* 523 */     float[] arrayOfFloat = new float[i];
/* 524 */     for (int j = 0; j < i; j++) {
/* 525 */       arrayOfFloat[j] = ((localObject[j] & 0xFFFF) / 65535.0F * this.diffMinMax[j] + this.minVal[j]);
/*     */     }
/*     */ 
/* 528 */     return arrayOfFloat;
/*     */   }
/*     */ 
/*     */   public float getMinValue(int paramInt)
/*     */   {
/* 548 */     if ((paramInt < 0) || (paramInt > getNumComponents() - 1)) {
/* 549 */       throw new IllegalArgumentException("Component index out of range: + component");
/*     */     }
/*     */ 
/* 552 */     return this.minVal[paramInt];
/*     */   }
/*     */ 
/*     */   public float getMaxValue(int paramInt)
/*     */   {
/* 573 */     if ((paramInt < 0) || (paramInt > getNumComponents() - 1)) {
/* 574 */       throw new IllegalArgumentException("Component index out of range: + component");
/*     */     }
/*     */ 
/* 577 */     return this.maxVal[paramInt];
/*     */   }
/*     */ 
/*     */   private void setMinMax() {
/* 581 */     int i = getNumComponents();
/* 582 */     int j = getType();
/* 583 */     this.minVal = new float[i];
/* 584 */     this.maxVal = new float[i];
/* 585 */     if (j == 1) {
/* 586 */       this.minVal[0] = 0.0F;
/* 587 */       this.maxVal[0] = 100.0F;
/* 588 */       this.minVal[1] = -128.0F;
/* 589 */       this.maxVal[1] = 127.0F;
/* 590 */       this.minVal[2] = -128.0F;
/* 591 */       this.maxVal[2] = 127.0F;
/* 592 */     } else if (j == 0)
/*     */     {
/*     */       float tmp101_100 = (this.minVal[2] = 0.0F); this.minVal[1] = tmp101_100; this.minVal[0] = tmp101_100;
/*     */       float tmp123_122 = (this.maxVal[2] = 1.99997F); this.maxVal[1] = tmp123_122; this.maxVal[0] = tmp123_122;
/*     */     } else {
/* 596 */       for (int k = 0; k < i; k++) {
/* 597 */         this.minVal[k] = 0.0F;
/* 598 */         this.maxVal[k] = 1.0F;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setComponentScaling() {
/* 604 */     int i = getNumComponents();
/* 605 */     this.diffMinMax = new float[i];
/* 606 */     this.invDiffMinMax = new float[i];
/* 607 */     for (int j = 0; j < i; j++) {
/* 608 */       this.minVal[j] = getMinValue(j);
/* 609 */       this.maxVal[j] = getMaxValue(j);
/* 610 */       this.diffMinMax[j] = (this.maxVal[j] - this.minVal[j]);
/* 611 */       this.invDiffMinMax[j] = (65535.0F / this.diffMinMax[j]);
/*     */     }
/* 613 */     this.needScaleInit = false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.color.ICC_ColorSpace
 * JD-Core Version:    0.6.2
 */