/*     */ package javax.swing.plaf.nimbus;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.UIResource;
/*     */ 
/*     */ class DerivedColor extends Color
/*     */ {
/*     */   private final String uiDefaultParentName;
/*     */   private final float hOffset;
/*     */   private final float sOffset;
/*     */   private final float bOffset;
/*     */   private final int aOffset;
/*     */   private int argbValue;
/*     */ 
/*     */   DerivedColor(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt)
/*     */   {
/*  48 */     super(0);
/*  49 */     this.uiDefaultParentName = paramString;
/*  50 */     this.hOffset = paramFloat1;
/*  51 */     this.sOffset = paramFloat2;
/*  52 */     this.bOffset = paramFloat3;
/*  53 */     this.aOffset = paramInt;
/*     */   }
/*     */ 
/*     */   public String getUiDefaultParentName() {
/*  57 */     return this.uiDefaultParentName;
/*     */   }
/*     */ 
/*     */   public float getHueOffset() {
/*  61 */     return this.hOffset;
/*     */   }
/*     */ 
/*     */   public float getSaturationOffset() {
/*  65 */     return this.sOffset;
/*     */   }
/*     */ 
/*     */   public float getBrightnessOffset() {
/*  69 */     return this.bOffset;
/*     */   }
/*     */ 
/*     */   public int getAlphaOffset() {
/*  73 */     return this.aOffset;
/*     */   }
/*     */ 
/*     */   public void rederiveColor()
/*     */   {
/*  80 */     Color localColor = UIManager.getColor(this.uiDefaultParentName);
/*     */     float[] arrayOfFloat;
/*     */     int i;
/*  81 */     if (localColor != null) {
/*  82 */       arrayOfFloat = Color.RGBtoHSB(localColor.getRed(), localColor.getGreen(), localColor.getBlue(), null);
/*     */ 
/*  84 */       arrayOfFloat[0] = clamp(arrayOfFloat[0] + this.hOffset);
/*  85 */       arrayOfFloat[1] = clamp(arrayOfFloat[1] + this.sOffset);
/*  86 */       arrayOfFloat[2] = clamp(arrayOfFloat[2] + this.bOffset);
/*  87 */       i = clamp(localColor.getAlpha() + this.aOffset);
/*  88 */       this.argbValue = (Color.HSBtoRGB(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2]) & 0xFFFFFF | i << 24);
/*     */     } else {
/*  90 */       arrayOfFloat = new float[3];
/*  91 */       arrayOfFloat[0] = clamp(this.hOffset);
/*  92 */       arrayOfFloat[1] = clamp(this.sOffset);
/*  93 */       arrayOfFloat[2] = clamp(this.bOffset);
/*  94 */       i = clamp(this.aOffset);
/*  95 */       this.argbValue = (Color.HSBtoRGB(arrayOfFloat[0], arrayOfFloat[1], arrayOfFloat[2]) & 0xFFFFFF | i << 24);
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getRGB()
/*     */   {
/* 111 */     return this.argbValue;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 116 */     if (this == paramObject) return true;
/* 117 */     if (!(paramObject instanceof DerivedColor)) return false;
/* 118 */     DerivedColor localDerivedColor = (DerivedColor)paramObject;
/* 119 */     if (this.aOffset != localDerivedColor.aOffset) return false;
/* 120 */     if (Float.compare(localDerivedColor.bOffset, this.bOffset) != 0) return false;
/* 121 */     if (Float.compare(localDerivedColor.hOffset, this.hOffset) != 0) return false;
/* 122 */     if (Float.compare(localDerivedColor.sOffset, this.sOffset) != 0) return false;
/* 123 */     if (!this.uiDefaultParentName.equals(localDerivedColor.uiDefaultParentName)) return false;
/* 124 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 129 */     int i = this.uiDefaultParentName.hashCode();
/* 130 */     i = 31 * i + this.hOffset != 0.0F ? Float.floatToIntBits(this.hOffset) : 0;
/*     */ 
/* 132 */     i = 31 * i + this.sOffset != 0.0F ? Float.floatToIntBits(this.sOffset) : 0;
/*     */ 
/* 134 */     i = 31 * i + this.bOffset != 0.0F ? Float.floatToIntBits(this.bOffset) : 0;
/*     */ 
/* 136 */     i = 31 * i + this.aOffset;
/* 137 */     return i;
/*     */   }
/*     */ 
/*     */   private float clamp(float paramFloat) {
/* 141 */     if (paramFloat < 0.0F)
/* 142 */       paramFloat = 0.0F;
/* 143 */     else if (paramFloat > 1.0F) {
/* 144 */       paramFloat = 1.0F;
/*     */     }
/* 146 */     return paramFloat;
/*     */   }
/*     */ 
/*     */   private int clamp(int paramInt) {
/* 150 */     if (paramInt < 0)
/* 151 */       paramInt = 0;
/* 152 */     else if (paramInt > 255) {
/* 153 */       paramInt = 255;
/*     */     }
/* 155 */     return paramInt;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 168 */     Color localColor = UIManager.getColor(this.uiDefaultParentName);
/* 169 */     String str = "DerivedColor(color=" + getRed() + "," + getGreen() + "," + getBlue() + " parent=" + this.uiDefaultParentName + " offsets=" + getHueOffset() + "," + getSaturationOffset() + "," + getBrightnessOffset() + "," + getAlphaOffset();
/*     */ 
/* 173 */     return str + " pColor=" + localColor.getRed() + "," + localColor.getGreen() + "," + localColor.getBlue();
/*     */   }
/*     */ 
/*     */   static class UIResource extends DerivedColor implements UIResource
/*     */   {
/*     */     UIResource(String paramString, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt) {
/* 179 */       super(paramFloat1, paramFloat2, paramFloat3, paramInt);
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 184 */       return ((paramObject instanceof UIResource)) && (super.equals(paramObject));
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 189 */       return super.hashCode() + 7;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.DerivedColor
 * JD-Core Version:    0.6.2
 */