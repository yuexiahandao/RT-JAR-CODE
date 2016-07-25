/*     */ package com.sun.media.sound;
/*     */ 
/*     */ public final class SoftEnvelopeGenerator
/*     */   implements SoftProcess
/*     */ {
/*     */   public static final int EG_OFF = 0;
/*     */   public static final int EG_DELAY = 1;
/*     */   public static final int EG_ATTACK = 2;
/*     */   public static final int EG_HOLD = 3;
/*     */   public static final int EG_DECAY = 4;
/*     */   public static final int EG_SUSTAIN = 5;
/*     */   public static final int EG_RELEASE = 6;
/*     */   public static final int EG_SHUTDOWN = 7;
/*     */   public static final int EG_END = 8;
/*  43 */   int max_count = 10;
/*  44 */   int used_count = 0;
/*  45 */   private final int[] stage = new int[this.max_count];
/*  46 */   private final int[] stage_ix = new int[this.max_count];
/*  47 */   private final double[] stage_v = new double[this.max_count];
/*  48 */   private final int[] stage_count = new int[this.max_count];
/*  49 */   private final double[][] on = new double[this.max_count][1];
/*  50 */   private final double[][] active = new double[this.max_count][1];
/*  51 */   private final double[][] out = new double[this.max_count][1];
/*  52 */   private final double[][] delay = new double[this.max_count][1];
/*  53 */   private final double[][] attack = new double[this.max_count][1];
/*  54 */   private final double[][] hold = new double[this.max_count][1];
/*  55 */   private final double[][] decay = new double[this.max_count][1];
/*  56 */   private final double[][] sustain = new double[this.max_count][1];
/*  57 */   private final double[][] release = new double[this.max_count][1];
/*  58 */   private final double[][] shutdown = new double[this.max_count][1];
/*  59 */   private final double[][] release2 = new double[this.max_count][1];
/*  60 */   private final double[][] attack2 = new double[this.max_count][1];
/*  61 */   private final double[][] decay2 = new double[this.max_count][1];
/*  62 */   private double control_time = 0.0D;
/*     */ 
/*     */   public void reset() {
/*  65 */     for (int i = 0; i < this.used_count; i++) {
/*  66 */       this.stage[i] = 0;
/*  67 */       this.on[i][0] = 0.0D;
/*  68 */       this.out[i][0] = 0.0D;
/*  69 */       this.delay[i][0] = 0.0D;
/*  70 */       this.attack[i][0] = 0.0D;
/*  71 */       this.hold[i][0] = 0.0D;
/*  72 */       this.decay[i][0] = 0.0D;
/*  73 */       this.sustain[i][0] = 0.0D;
/*  74 */       this.release[i][0] = 0.0D;
/*  75 */       this.shutdown[i][0] = 0.0D;
/*  76 */       this.attack2[i][0] = 0.0D;
/*  77 */       this.decay2[i][0] = 0.0D;
/*  78 */       this.release2[i][0] = 0.0D;
/*     */     }
/*  80 */     this.used_count = 0;
/*     */   }
/*     */ 
/*     */   public void init(SoftSynthesizer paramSoftSynthesizer) {
/*  84 */     this.control_time = (1.0D / paramSoftSynthesizer.getControlRate());
/*  85 */     processControlLogic();
/*     */   }
/*     */ 
/*     */   public double[] get(int paramInt, String paramString) {
/*  89 */     if (paramInt >= this.used_count)
/*  90 */       this.used_count = (paramInt + 1);
/*  91 */     if (paramString == null)
/*  92 */       return this.out[paramInt];
/*  93 */     if (paramString.equals("on"))
/*  94 */       return this.on[paramInt];
/*  95 */     if (paramString.equals("active"))
/*  96 */       return this.active[paramInt];
/*  97 */     if (paramString.equals("delay"))
/*  98 */       return this.delay[paramInt];
/*  99 */     if (paramString.equals("attack"))
/* 100 */       return this.attack[paramInt];
/* 101 */     if (paramString.equals("hold"))
/* 102 */       return this.hold[paramInt];
/* 103 */     if (paramString.equals("decay"))
/* 104 */       return this.decay[paramInt];
/* 105 */     if (paramString.equals("sustain"))
/* 106 */       return this.sustain[paramInt];
/* 107 */     if (paramString.equals("release"))
/* 108 */       return this.release[paramInt];
/* 109 */     if (paramString.equals("shutdown"))
/* 110 */       return this.shutdown[paramInt];
/* 111 */     if (paramString.equals("attack2"))
/* 112 */       return this.attack2[paramInt];
/* 113 */     if (paramString.equals("decay2"))
/* 114 */       return this.decay2[paramInt];
/* 115 */     if (paramString.equals("release2")) {
/* 116 */       return this.release2[paramInt];
/*     */     }
/* 118 */     return null;
/*     */   }
/*     */ 
/*     */   public void processControlLogic() {
/* 122 */     for (int i = 0; i < this.used_count; i++)
/*     */     {
/* 124 */       if (this.stage[i] != 8)
/*     */       {
/*     */         double d1;
/* 127 */         if ((this.stage[i] > 0) && (this.stage[i] < 6) && 
/* 128 */           (this.on[i][0] < 0.5D))
/* 129 */           if (this.on[i][0] < -0.5D) {
/* 130 */             this.stage_count[i] = ((int)(Math.pow(2.0D, this.shutdown[i][0] / 1200.0D) / this.control_time));
/*     */ 
/* 132 */             if (this.stage_count[i] < 0)
/* 133 */               this.stage_count[i] = 0;
/* 134 */             this.stage_v[i] = this.out[i][0];
/* 135 */             this.stage_ix[i] = 0;
/* 136 */             this.stage[i] = 7;
/*     */           } else {
/* 138 */             if ((this.release2[i][0] < 1.0E-006D) && (this.release[i][0] < 0.0D) && (Double.isInfinite(this.release[i][0])))
/*     */             {
/* 140 */               this.out[i][0] = 0.0D;
/* 141 */               this.active[i][0] = 0.0D;
/* 142 */               this.stage[i] = 8;
/* 143 */               continue;
/*     */             }
/*     */ 
/* 146 */             this.stage_count[i] = ((int)(Math.pow(2.0D, this.release[i][0] / 1200.0D) / this.control_time));
/*     */ 
/* 148 */             this.stage_count[i] += (int)(this.release2[i][0] / (this.control_time * 1000.0D));
/*     */ 
/* 150 */             if (this.stage_count[i] < 0) {
/* 151 */               this.stage_count[i] = 0;
/*     */             }
/* 153 */             this.stage_ix[i] = 0;
/*     */ 
/* 155 */             d1 = 1.0D - this.out[i][0];
/* 156 */             this.stage_ix[i] = ((int)(this.stage_count[i] * d1));
/*     */ 
/* 158 */             this.stage[i] = 6;
/*     */           }
/*     */         double d2;
/* 163 */         switch (this.stage[i]) {
/*     */         case 0:
/* 165 */           this.active[i][0] = 1.0D;
/* 166 */           if (this.on[i][0] >= 0.5D)
/*     */           {
/* 168 */             this.stage[i] = 1;
/* 169 */             this.stage_ix[i] = ((int)(Math.pow(2.0D, this.delay[i][0] / 1200.0D) / this.control_time));
/*     */ 
/* 171 */             if (this.stage_ix[i] < 0)
/* 172 */               this.stage_ix[i] = 0;  } break;
/*     */         case 1:
/* 174 */           if (this.stage_ix[i] == 0) {
/* 175 */             d1 = this.attack[i][0];
/* 176 */             d2 = this.attack2[i][0];
/*     */ 
/* 178 */             if ((d2 < 1.0E-006D) && (d1 < 0.0D) && (Double.isInfinite(d1)))
/*     */             {
/* 180 */               this.out[i][0] = 1.0D;
/* 181 */               this.stage[i] = 3;
/* 182 */               this.stage_count[i] = ((int)(Math.pow(2.0D, this.hold[i][0] / 1200.0D) / this.control_time));
/*     */ 
/* 184 */               this.stage_ix[i] = 0;
/*     */             } else {
/* 186 */               this.stage[i] = 2;
/* 187 */               this.stage_count[i] = ((int)(Math.pow(2.0D, d1 / 1200.0D) / this.control_time));
/*     */ 
/* 189 */               this.stage_count[i] += (int)(d2 / (this.control_time * 1000.0D));
/* 190 */               if (this.stage_count[i] < 0)
/* 191 */                 this.stage_count[i] = 0;
/* 192 */               this.stage_ix[i] = 0;
/*     */             }
/*     */           } else {
/* 195 */             this.stage_ix[i] -= 1;
/* 196 */           }break;
/*     */         case 2:
/* 198 */           this.stage_ix[i] += 1;
/* 199 */           if (this.stage_ix[i] >= this.stage_count[i]) {
/* 200 */             this.out[i][0] = 1.0D;
/* 201 */             this.stage[i] = 3;
/*     */           }
/*     */           else {
/* 204 */             d1 = this.stage_ix[i] / this.stage_count[i];
/* 205 */             d1 = 1.0D + 0.4166666666666667D / Math.log(10.0D) * Math.log(d1);
/* 206 */             if (d1 < 0.0D)
/* 207 */               d1 = 0.0D;
/* 208 */             else if (d1 > 1.0D)
/* 209 */               d1 = 1.0D;
/* 210 */             this.out[i][0] = d1;
/*     */           }
/* 212 */           break;
/*     */         case 3:
/* 214 */           this.stage_ix[i] += 1;
/* 215 */           if (this.stage_ix[i] >= this.stage_count[i]) {
/* 216 */             this.stage[i] = 4;
/* 217 */             this.stage_count[i] = ((int)(Math.pow(2.0D, this.decay[i][0] / 1200.0D) / this.control_time));
/*     */ 
/* 219 */             this.stage_count[i] += (int)(this.decay2[i][0] / (this.control_time * 1000.0D));
/* 220 */             if (this.stage_count[i] < 0)
/* 221 */               this.stage_count[i] = 0;
/* 222 */             this.stage_ix[i] = 0; } break;
/*     */         case 4:
/* 226 */           this.stage_ix[i] += 1;
/* 227 */           d1 = this.sustain[i][0] * 0.001D;
/* 228 */           if (this.stage_ix[i] >= this.stage_count[i]) {
/* 229 */             this.out[i][0] = d1;
/* 230 */             this.stage[i] = 5;
/* 231 */             if (d1 < 0.001D) {
/* 232 */               this.out[i][0] = 0.0D;
/* 233 */               this.active[i][0] = 0.0D;
/* 234 */               this.stage[i] = 8;
/*     */             }
/*     */           } else {
/* 237 */             d2 = this.stage_ix[i] / this.stage_count[i];
/* 238 */             this.out[i][0] = (1.0D - d2 + d1 * d2);
/*     */           }
/* 240 */           break;
/*     */         case 5:
/* 242 */           break;
/*     */         case 6:
/* 244 */           this.stage_ix[i] += 1;
/* 245 */           if (this.stage_ix[i] >= this.stage_count[i]) {
/* 246 */             this.out[i][0] = 0.0D;
/* 247 */             this.active[i][0] = 0.0D;
/* 248 */             this.stage[i] = 8;
/*     */           } else {
/* 250 */             d2 = this.stage_ix[i] / this.stage_count[i];
/* 251 */             this.out[i][0] = (1.0D - d2);
/*     */ 
/* 253 */             if (this.on[i][0] < -0.5D) {
/* 254 */               this.stage_count[i] = ((int)(Math.pow(2.0D, this.shutdown[i][0] / 1200.0D) / this.control_time));
/*     */ 
/* 256 */               if (this.stage_count[i] < 0)
/* 257 */                 this.stage_count[i] = 0;
/* 258 */               this.stage_v[i] = this.out[i][0];
/* 259 */               this.stage_ix[i] = 0;
/* 260 */               this.stage[i] = 7;
/*     */             }
/*     */ 
/* 264 */             if (this.on[i][0] > 0.5D) {
/* 265 */               d1 = this.sustain[i][0] * 0.001D;
/* 266 */               if (this.out[i][0] > d1) {
/* 267 */                 this.stage[i] = 4;
/* 268 */                 this.stage_count[i] = ((int)(Math.pow(2.0D, this.decay[i][0] / 1200.0D) / this.control_time));
/*     */ 
/* 270 */                 this.stage_count[i] += (int)(this.decay2[i][0] / (this.control_time * 1000.0D));
/*     */ 
/* 272 */                 if (this.stage_count[i] < 0)
/* 273 */                   this.stage_count[i] = 0;
/* 274 */                 d2 = (this.out[i][0] - 1.0D) / (d1 - 1.0D);
/* 275 */                 this.stage_ix[i] = ((int)(this.stage_count[i] * d2));
/*     */               }
/*     */             }
/*     */           }
/*     */ 
/* 280 */           break;
/*     */         case 7:
/* 282 */           this.stage_ix[i] += 1;
/* 283 */           if (this.stage_ix[i] >= this.stage_count[i]) {
/* 284 */             this.out[i][0] = 0.0D;
/* 285 */             this.active[i][0] = 0.0D;
/* 286 */             this.stage[i] = 8;
/*     */           } else {
/* 288 */             d2 = this.stage_ix[i] / this.stage_count[i];
/* 289 */             this.out[i][0] = ((1.0D - d2) * this.stage_v[i]);
/*     */           }
/*     */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftEnvelopeGenerator
 * JD-Core Version:    0.6.2
 */