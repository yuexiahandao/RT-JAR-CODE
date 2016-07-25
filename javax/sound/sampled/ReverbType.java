/*     */ package javax.sound.sampled;
/*     */ 
/*     */ public class ReverbType
/*     */ {
/*     */   private String name;
/*     */   private int earlyReflectionDelay;
/*     */   private float earlyReflectionIntensity;
/*     */   private int lateReflectionDelay;
/*     */   private float lateReflectionIntensity;
/*     */   private int decayTime;
/*     */ 
/*     */   protected ReverbType(String paramString, int paramInt1, float paramFloat1, int paramInt2, float paramFloat2, int paramInt3)
/*     */   {
/* 187 */     this.name = paramString;
/* 188 */     this.earlyReflectionDelay = paramInt1;
/* 189 */     this.earlyReflectionIntensity = paramFloat1;
/* 190 */     this.lateReflectionDelay = paramInt2;
/* 191 */     this.lateReflectionIntensity = paramFloat2;
/* 192 */     this.decayTime = paramInt3;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 202 */     return this.name;
/*     */   }
/*     */ 
/*     */   public final int getEarlyReflectionDelay()
/*     */   {
/* 213 */     return this.earlyReflectionDelay;
/*     */   }
/*     */ 
/*     */   public final float getEarlyReflectionIntensity()
/*     */   {
/* 224 */     return this.earlyReflectionIntensity;
/*     */   }
/*     */ 
/*     */   public final int getLateReflectionDelay()
/*     */   {
/* 235 */     return this.lateReflectionDelay;
/*     */   }
/*     */ 
/*     */   public final float getLateReflectionIntensity()
/*     */   {
/* 246 */     return this.lateReflectionIntensity;
/*     */   }
/*     */ 
/*     */   public final int getDecayTime()
/*     */   {
/* 257 */     return this.decayTime;
/*     */   }
/*     */ 
/*     */   public final boolean equals(Object paramObject)
/*     */   {
/* 269 */     return super.equals(paramObject);
/*     */   }
/*     */ 
/*     */   public final int hashCode()
/*     */   {
/* 277 */     return super.hashCode();
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 292 */     return this.name + ", early reflection delay " + this.earlyReflectionDelay + " ns, early reflection intensity " + this.earlyReflectionIntensity + " dB, late deflection delay " + this.lateReflectionDelay + " ns, late reflection intensity " + this.lateReflectionIntensity + " dB, decay time " + this.decayTime;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.ReverbType
 * JD-Core Version:    0.6.2
 */