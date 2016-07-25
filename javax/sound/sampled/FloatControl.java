/*     */ package javax.sound.sampled;
/*     */ 
/*     */ public abstract class FloatControl extends Control
/*     */ {
/*     */   private float minimum;
/*     */   private float maximum;
/*     */   private float precision;
/*     */   private int updatePeriod;
/*     */   private final String units;
/*     */   private final String minLabel;
/*     */   private final String maxLabel;
/*     */   private final String midLabel;
/*     */   private float value;
/*     */ 
/*     */   protected FloatControl(Type paramType, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt, float paramFloat4, String paramString1, String paramString2, String paramString3, String paramString4)
/*     */   {
/* 143 */     super(paramType);
/*     */ 
/* 145 */     if (paramFloat1 > paramFloat2) {
/* 146 */       throw new IllegalArgumentException("Minimum value " + paramFloat1 + " exceeds maximum value " + paramFloat2 + ".");
/*     */     }
/*     */ 
/* 149 */     if (paramFloat4 < paramFloat1) {
/* 150 */       throw new IllegalArgumentException("Initial value " + paramFloat4 + " smaller than allowable minimum value " + paramFloat1 + ".");
/*     */     }
/*     */ 
/* 153 */     if (paramFloat4 > paramFloat2) {
/* 154 */       throw new IllegalArgumentException("Initial value " + paramFloat4 + " exceeds allowable maximum value " + paramFloat2 + ".");
/*     */     }
/*     */ 
/* 159 */     this.minimum = paramFloat1;
/* 160 */     this.maximum = paramFloat2;
/*     */ 
/* 162 */     this.precision = paramFloat3;
/* 163 */     this.updatePeriod = paramInt;
/* 164 */     this.value = paramFloat4;
/*     */ 
/* 166 */     this.units = paramString1;
/* 167 */     this.minLabel = (paramString2 == null ? "" : paramString2);
/* 168 */     this.midLabel = (paramString3 == null ? "" : paramString3);
/* 169 */     this.maxLabel = (paramString4 == null ? "" : paramString4);
/*     */   }
/*     */ 
/*     */   protected FloatControl(Type paramType, float paramFloat1, float paramFloat2, float paramFloat3, int paramInt, float paramFloat4, String paramString)
/*     */   {
/* 195 */     this(paramType, paramFloat1, paramFloat2, paramFloat3, paramInt, paramFloat4, paramString, "", "", "");
/*     */   }
/*     */ 
/*     */   public void setValue(float paramFloat)
/*     */   {
/* 217 */     if (paramFloat > this.maximum) {
/* 218 */       throw new IllegalArgumentException("Requested value " + paramFloat + " exceeds allowable maximum value " + this.maximum + ".");
/*     */     }
/*     */ 
/* 221 */     if (paramFloat < this.minimum) {
/* 222 */       throw new IllegalArgumentException("Requested value " + paramFloat + " smaller than allowable minimum value " + this.minimum + ".");
/*     */     }
/*     */ 
/* 225 */     this.value = paramFloat;
/*     */   }
/*     */ 
/*     */   public float getValue()
/*     */   {
/* 234 */     return this.value;
/*     */   }
/*     */ 
/*     */   public float getMaximum()
/*     */   {
/* 243 */     return this.maximum;
/*     */   }
/*     */ 
/*     */   public float getMinimum()
/*     */   {
/* 252 */     return this.minimum;
/*     */   }
/*     */ 
/*     */   public String getUnits()
/*     */   {
/* 262 */     return this.units;
/*     */   }
/*     */ 
/*     */   public String getMinLabel()
/*     */   {
/* 271 */     return this.minLabel;
/*     */   }
/*     */ 
/*     */   public String getMidLabel()
/*     */   {
/* 280 */     return this.midLabel;
/*     */   }
/*     */ 
/*     */   public String getMaxLabel()
/*     */   {
/* 289 */     return this.maxLabel;
/*     */   }
/*     */ 
/*     */   public float getPrecision()
/*     */   {
/* 301 */     return this.precision;
/*     */   }
/*     */ 
/*     */   public int getUpdatePeriod()
/*     */   {
/* 316 */     return this.updatePeriod;
/*     */   }
/*     */ 
/*     */   public void shift(float paramFloat1, float paramFloat2, int paramInt)
/*     */   {
/* 339 */     if (paramFloat1 < this.minimum) {
/* 340 */       throw new IllegalArgumentException("Requested value " + paramFloat1 + " smaller than allowable minimum value " + this.minimum + ".");
/*     */     }
/*     */ 
/* 343 */     if (paramFloat1 > this.maximum) {
/* 344 */       throw new IllegalArgumentException("Requested value " + paramFloat1 + " exceeds allowable maximum value " + this.maximum + ".");
/*     */     }
/*     */ 
/* 347 */     setValue(paramFloat2);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 359 */     return new String(getType() + " with current value: " + getValue() + " " + this.units + " (range: " + this.minimum + " - " + this.maximum + ")");
/*     */   }
/*     */ 
/*     */   public static class Type extends Control.Type
/*     */   {
/* 419 */     public static final Type MASTER_GAIN = new Type("Master Gain");
/*     */ 
/* 427 */     public static final Type AUX_SEND = new Type("AUX Send");
/*     */ 
/* 435 */     public static final Type AUX_RETURN = new Type("AUX Return");
/*     */ 
/* 446 */     public static final Type REVERB_SEND = new Type("Reverb Send");
/*     */ 
/* 456 */     public static final Type REVERB_RETURN = new Type("Reverb Return");
/*     */ 
/* 467 */     public static final Type VOLUME = new Type("Volume");
/*     */ 
/* 481 */     public static final Type PAN = new Type("Pan");
/*     */ 
/* 493 */     public static final Type BALANCE = new Type("Balance");
/*     */ 
/* 514 */     public static final Type SAMPLE_RATE = new Type("Sample Rate");
/*     */ 
/*     */     protected Type(String paramString)
/*     */     {
/* 524 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.FloatControl
 * JD-Core Version:    0.6.2
 */