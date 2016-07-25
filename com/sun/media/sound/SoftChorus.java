/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public final class SoftChorus
/*     */   implements SoftAudioProcessor
/*     */ {
/* 174 */   private boolean mix = true;
/*     */   private SoftAudioBuffer inputA;
/*     */   private SoftAudioBuffer left;
/*     */   private SoftAudioBuffer right;
/*     */   private SoftAudioBuffer reverb;
/*     */   private LFODelay vdelay1L;
/*     */   private LFODelay vdelay1R;
/* 181 */   private float rgain = 0.0F;
/* 182 */   private boolean dirty = true;
/*     */   private double dirty_vdelay1L_rate;
/*     */   private double dirty_vdelay1R_rate;
/*     */   private double dirty_vdelay1L_depth;
/*     */   private double dirty_vdelay1R_depth;
/*     */   private float dirty_vdelay1L_feedback;
/*     */   private float dirty_vdelay1R_feedback;
/*     */   private float dirty_vdelay1L_reverbsendgain;
/*     */   private float dirty_vdelay1R_reverbsendgain;
/*     */   private float controlrate;
/* 287 */   double silentcounter = 1000.0D;
/*     */ 
/*     */   public void init(float paramFloat1, float paramFloat2)
/*     */   {
/* 194 */     this.controlrate = paramFloat2;
/* 195 */     this.vdelay1L = new LFODelay(paramFloat1, paramFloat2);
/* 196 */     this.vdelay1R = new LFODelay(paramFloat1, paramFloat2);
/* 197 */     this.vdelay1L.setGain(1.0F);
/* 198 */     this.vdelay1R.setGain(1.0F);
/* 199 */     this.vdelay1L.setPhase(1.570796326794897D);
/* 200 */     this.vdelay1R.setPhase(0.0D);
/*     */ 
/* 202 */     globalParameterControlChange(new int[] { 130 }, 0L, 2L);
/*     */   }
/*     */ 
/*     */   public void globalParameterControlChange(int[] paramArrayOfInt, long paramLong1, long paramLong2)
/*     */   {
/* 207 */     if ((paramArrayOfInt.length == 1) && 
/* 208 */       (paramArrayOfInt[0] == 130)) {
/* 209 */       if (paramLong1 == 0L) {
/* 210 */         switch ((int)paramLong2) {
/*     */         case 0:
/* 212 */           globalParameterControlChange(paramArrayOfInt, 3L, 0L);
/* 213 */           globalParameterControlChange(paramArrayOfInt, 1L, 3L);
/* 214 */           globalParameterControlChange(paramArrayOfInt, 2L, 5L);
/* 215 */           globalParameterControlChange(paramArrayOfInt, 4L, 0L);
/* 216 */           break;
/*     */         case 1:
/* 218 */           globalParameterControlChange(paramArrayOfInt, 3L, 5L);
/* 219 */           globalParameterControlChange(paramArrayOfInt, 1L, 9L);
/* 220 */           globalParameterControlChange(paramArrayOfInt, 2L, 19L);
/* 221 */           globalParameterControlChange(paramArrayOfInt, 4L, 0L);
/* 222 */           break;
/*     */         case 2:
/* 224 */           globalParameterControlChange(paramArrayOfInt, 3L, 8L);
/* 225 */           globalParameterControlChange(paramArrayOfInt, 1L, 3L);
/* 226 */           globalParameterControlChange(paramArrayOfInt, 2L, 19L);
/* 227 */           globalParameterControlChange(paramArrayOfInt, 4L, 0L);
/* 228 */           break;
/*     */         case 3:
/* 230 */           globalParameterControlChange(paramArrayOfInt, 3L, 16L);
/* 231 */           globalParameterControlChange(paramArrayOfInt, 1L, 9L);
/* 232 */           globalParameterControlChange(paramArrayOfInt, 2L, 16L);
/* 233 */           globalParameterControlChange(paramArrayOfInt, 4L, 0L);
/* 234 */           break;
/*     */         case 4:
/* 236 */           globalParameterControlChange(paramArrayOfInt, 3L, 64L);
/* 237 */           globalParameterControlChange(paramArrayOfInt, 1L, 2L);
/* 238 */           globalParameterControlChange(paramArrayOfInt, 2L, 24L);
/* 239 */           globalParameterControlChange(paramArrayOfInt, 4L, 0L);
/* 240 */           break;
/*     */         case 5:
/* 242 */           globalParameterControlChange(paramArrayOfInt, 3L, 112L);
/* 243 */           globalParameterControlChange(paramArrayOfInt, 1L, 1L);
/* 244 */           globalParameterControlChange(paramArrayOfInt, 2L, 5L);
/* 245 */           globalParameterControlChange(paramArrayOfInt, 4L, 0L);
/* 246 */           break;
/*     */         default:
/* 248 */           break;
/*     */         }
/* 250 */       } else if (paramLong1 == 1L) {
/* 251 */         this.dirty_vdelay1L_rate = (paramLong2 * 0.122D);
/* 252 */         this.dirty_vdelay1R_rate = (paramLong2 * 0.122D);
/* 253 */         this.dirty = true;
/* 254 */       } else if (paramLong1 == 2L) {
/* 255 */         this.dirty_vdelay1L_depth = ((paramLong2 + 1L) / 3200.0D);
/* 256 */         this.dirty_vdelay1R_depth = ((paramLong2 + 1L) / 3200.0D);
/* 257 */         this.dirty = true;
/* 258 */       } else if (paramLong1 == 3L) {
/* 259 */         this.dirty_vdelay1L_feedback = ((float)paramLong2 * 0.00763F);
/* 260 */         this.dirty_vdelay1R_feedback = ((float)paramLong2 * 0.00763F);
/* 261 */         this.dirty = true;
/*     */       }
/* 263 */       if (paramLong1 == 4L) {
/* 264 */         this.rgain = ((float)paramLong2 * 0.00787F);
/* 265 */         this.dirty_vdelay1L_reverbsendgain = ((float)paramLong2 * 0.00787F);
/* 266 */         this.dirty_vdelay1R_reverbsendgain = ((float)paramLong2 * 0.00787F);
/* 267 */         this.dirty = true;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void processControlLogic()
/*     */   {
/* 275 */     if (this.dirty) {
/* 276 */       this.dirty = false;
/* 277 */       this.vdelay1L.setRate(this.dirty_vdelay1L_rate);
/* 278 */       this.vdelay1R.setRate(this.dirty_vdelay1R_rate);
/* 279 */       this.vdelay1L.setDepth(this.dirty_vdelay1L_depth);
/* 280 */       this.vdelay1R.setDepth(this.dirty_vdelay1R_depth);
/* 281 */       this.vdelay1L.setFeedBack(this.dirty_vdelay1L_feedback);
/* 282 */       this.vdelay1R.setFeedBack(this.dirty_vdelay1R_feedback);
/* 283 */       this.vdelay1L.setReverbSendGain(this.dirty_vdelay1L_reverbsendgain);
/* 284 */       this.vdelay1R.setReverbSendGain(this.dirty_vdelay1R_reverbsendgain);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void processAudio()
/*     */   {
/* 291 */     if (this.inputA.isSilent()) {
/* 292 */       this.silentcounter += 1.0F / this.controlrate;
/*     */ 
/* 294 */       if (this.silentcounter > 1.0D)
/* 295 */         if (!this.mix) {
/* 296 */           this.left.clear();
/* 297 */           this.right.clear();
/*     */         }
/*     */     }
/*     */     else
/*     */     {
/* 302 */       this.silentcounter = 0.0D;
/*     */     }
/* 304 */     float[] arrayOfFloat1 = this.inputA.array();
/* 305 */     float[] arrayOfFloat2 = this.left.array();
/* 306 */     float[] arrayOfFloat3 = this.right == null ? null : this.right.array();
/* 307 */     float[] arrayOfFloat4 = this.rgain != 0.0F ? this.reverb.array() : null;
/*     */ 
/* 309 */     if (this.mix) {
/* 310 */       this.vdelay1L.processMix(arrayOfFloat1, arrayOfFloat2, arrayOfFloat4);
/* 311 */       if (arrayOfFloat3 != null)
/* 312 */         this.vdelay1R.processMix(arrayOfFloat1, arrayOfFloat3, arrayOfFloat4);
/*     */     } else {
/* 314 */       this.vdelay1L.processReplace(arrayOfFloat1, arrayOfFloat2, arrayOfFloat4);
/* 315 */       if (arrayOfFloat3 != null)
/* 316 */         this.vdelay1R.processReplace(arrayOfFloat1, arrayOfFloat3, arrayOfFloat4);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setInput(int paramInt, SoftAudioBuffer paramSoftAudioBuffer) {
/* 321 */     if (paramInt == 0)
/* 322 */       this.inputA = paramSoftAudioBuffer;
/*     */   }
/*     */ 
/*     */   public void setMixMode(boolean paramBoolean) {
/* 326 */     this.mix = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void setOutput(int paramInt, SoftAudioBuffer paramSoftAudioBuffer) {
/* 330 */     if (paramInt == 0)
/* 331 */       this.left = paramSoftAudioBuffer;
/* 332 */     if (paramInt == 1)
/* 333 */       this.right = paramSoftAudioBuffer;
/* 334 */     if (paramInt == 2)
/* 335 */       this.reverb = paramSoftAudioBuffer;
/*     */   }
/*     */ 
/*     */   private static class LFODelay
/*     */   {
/* 118 */     private double phase = 1.0D;
/* 119 */     private double phase_step = 0.0D;
/* 120 */     private double depth = 0.0D;
/*     */     private SoftChorus.VariableDelay vdelay;
/*     */     private final double samplerate;
/*     */     private final double controlrate;
/*     */ 
/*     */     LFODelay(double paramDouble1, double paramDouble2)
/*     */     {
/* 126 */       this.samplerate = paramDouble1;
/* 127 */       this.controlrate = paramDouble2;
/*     */ 
/* 129 */       this.vdelay = new SoftChorus.VariableDelay((int)((this.depth + 10.0D) * 2.0D));
/*     */     }
/*     */ 
/*     */     public void setDepth(double paramDouble)
/*     */     {
/* 134 */       this.depth = (paramDouble * this.samplerate);
/* 135 */       this.vdelay = new SoftChorus.VariableDelay((int)((this.depth + 10.0D) * 2.0D));
/*     */     }
/*     */ 
/*     */     public void setRate(double paramDouble) {
/* 139 */       double d = 6.283185307179586D * (paramDouble / this.controlrate);
/* 140 */       this.phase_step = d;
/*     */     }
/*     */ 
/*     */     public void setPhase(double paramDouble) {
/* 144 */       this.phase = paramDouble;
/*     */     }
/*     */ 
/*     */     public void setFeedBack(float paramFloat) {
/* 148 */       this.vdelay.setFeedBack(paramFloat);
/*     */     }
/*     */ 
/*     */     public void setGain(float paramFloat) {
/* 152 */       this.vdelay.setGain(paramFloat);
/*     */     }
/*     */ 
/*     */     public void setReverbSendGain(float paramFloat) {
/* 156 */       this.vdelay.setReverbSendGain(paramFloat);
/*     */     }
/*     */ 
/*     */     public void processMix(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3) {
/* 160 */       this.phase += this.phase_step;
/* 161 */       while (this.phase > 6.283185307179586D) this.phase -= 6.283185307179586D;
/* 162 */       this.vdelay.setDelay((float)(this.depth * 0.5D * (Math.cos(this.phase) + 2.0D)));
/* 163 */       this.vdelay.processMix(paramArrayOfFloat1, paramArrayOfFloat2, paramArrayOfFloat3);
/*     */     }
/*     */ 
/*     */     public void processReplace(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3) {
/* 167 */       this.phase += this.phase_step;
/* 168 */       while (this.phase > 6.283185307179586D) this.phase -= 6.283185307179586D;
/* 169 */       this.vdelay.setDelay((float)(this.depth * 0.5D * (Math.cos(this.phase) + 2.0D)));
/* 170 */       this.vdelay.processReplace(paramArrayOfFloat1, paramArrayOfFloat2, paramArrayOfFloat3);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class VariableDelay
/*     */   {
/*     */     private final float[] delaybuffer;
/*  40 */     private int rovepos = 0;
/*  41 */     private float gain = 1.0F;
/*  42 */     private float rgain = 0.0F;
/*  43 */     private float delay = 0.0F;
/*  44 */     private float lastdelay = 0.0F;
/*  45 */     private float feedback = 0.0F;
/*     */ 
/*     */     VariableDelay(int paramInt) {
/*  48 */       this.delaybuffer = new float[paramInt];
/*     */     }
/*     */ 
/*     */     public void setDelay(float paramFloat) {
/*  52 */       this.delay = paramFloat;
/*     */     }
/*     */ 
/*     */     public void setFeedBack(float paramFloat) {
/*  56 */       this.feedback = paramFloat;
/*     */     }
/*     */ 
/*     */     public void setGain(float paramFloat) {
/*  60 */       this.gain = paramFloat;
/*     */     }
/*     */ 
/*     */     public void setReverbSendGain(float paramFloat) {
/*  64 */       this.rgain = paramFloat;
/*     */     }
/*     */ 
/*     */     public void processMix(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3) {
/*  68 */       float f1 = this.gain;
/*  69 */       float f2 = this.delay;
/*  70 */       float f3 = this.feedback;
/*     */ 
/*  72 */       float[] arrayOfFloat = this.delaybuffer;
/*  73 */       int i = paramArrayOfFloat1.length;
/*  74 */       float f4 = (f2 - this.lastdelay) / i;
/*  75 */       int j = arrayOfFloat.length;
/*  76 */       int k = this.rovepos;
/*     */       int m;
/*     */       float f5;
/*     */       int n;
/*     */       float f6;
/*     */       float f7;
/*     */       float f8;
/*     */       float f9;
/*  78 */       if (paramArrayOfFloat3 == null)
/*  79 */         for (m = 0; m < i; m++) {
/*  80 */           f5 = k - (this.lastdelay + 2.0F) + j;
/*  81 */           n = (int)f5;
/*  82 */           f6 = f5 - n;
/*  83 */           f7 = arrayOfFloat[(n % j)];
/*  84 */           f8 = arrayOfFloat[((n + 1) % j)];
/*  85 */           f9 = f7 * (1.0F - f6) + f8 * f6;
/*  86 */           paramArrayOfFloat2[m] += f9 * f1;
/*  87 */           paramArrayOfFloat1[m] += f9 * f3;
/*  88 */           k = (k + 1) % j;
/*  89 */           this.lastdelay += f4;
/*     */         }
/*     */       else
/*  92 */         for (m = 0; m < i; m++) {
/*  93 */           f5 = k - (this.lastdelay + 2.0F) + j;
/*  94 */           n = (int)f5;
/*  95 */           f6 = f5 - n;
/*  96 */           f7 = arrayOfFloat[(n % j)];
/*  97 */           f8 = arrayOfFloat[((n + 1) % j)];
/*  98 */           f9 = f7 * (1.0F - f6) + f8 * f6;
/*  99 */           paramArrayOfFloat2[m] += f9 * f1;
/* 100 */           paramArrayOfFloat3[m] += f9 * this.rgain;
/* 101 */           paramArrayOfFloat1[m] += f9 * f3;
/* 102 */           k = (k + 1) % j;
/* 103 */           this.lastdelay += f4;
/*     */         }
/* 105 */       this.rovepos = k;
/* 106 */       this.lastdelay = f2;
/*     */     }
/*     */ 
/*     */     public void processReplace(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float[] paramArrayOfFloat3) {
/* 110 */       Arrays.fill(paramArrayOfFloat2, 0.0F);
/* 111 */       Arrays.fill(paramArrayOfFloat3, 0.0F);
/* 112 */       processMix(paramArrayOfFloat1, paramArrayOfFloat2, paramArrayOfFloat3);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftChorus
 * JD-Core Version:    0.6.2
 */