/*     */ package com.sun.media.sound;
/*     */ 
/*     */ public final class SoftLowFrequencyOscillator
/*     */   implements SoftProcess
/*     */ {
/*  34 */   private final int max_count = 10;
/*  35 */   private int used_count = 0;
/*  36 */   private final double[][] out = new double[10][1];
/*  37 */   private final double[][] delay = new double[10][1];
/*  38 */   private final double[][] delay2 = new double[10][1];
/*  39 */   private final double[][] freq = new double[10][1];
/*  40 */   private final int[] delay_counter = new int[10];
/*  41 */   private final double[] sin_phase = new double[10];
/*  42 */   private final double[] sin_stepfreq = new double[10];
/*  43 */   private final double[] sin_step = new double[10];
/*  44 */   private double control_time = 0.0D;
/*  45 */   private double sin_factor = 0.0D;
/*     */   private static final double PI2 = 6.283185307179586D;
/*     */ 
/*     */   public SoftLowFrequencyOscillator()
/*     */   {
/*  50 */     for (int i = 0; i < this.sin_stepfreq.length; i++)
/*  51 */       this.sin_stepfreq[i] = (-1.0D / 0.0D);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/*  56 */     for (int i = 0; i < this.used_count; i++) {
/*  57 */       this.out[i][0] = 0.0D;
/*  58 */       this.delay[i][0] = 0.0D;
/*  59 */       this.delay2[i][0] = 0.0D;
/*  60 */       this.freq[i][0] = 0.0D;
/*  61 */       this.delay_counter[i] = 0;
/*  62 */       this.sin_phase[i] = 0.0D;
/*     */ 
/*  64 */       this.sin_stepfreq[i] = (-1.0D / 0.0D);
/*  65 */       this.sin_step[i] = 0.0D;
/*     */     }
/*  67 */     this.used_count = 0;
/*     */   }
/*     */ 
/*     */   public void init(SoftSynthesizer paramSoftSynthesizer) {
/*  71 */     this.control_time = (1.0D / paramSoftSynthesizer.getControlRate());
/*  72 */     this.sin_factor = (this.control_time * 2.0D * 3.141592653589793D);
/*  73 */     for (int i = 0; i < this.used_count; i++) {
/*  74 */       this.delay_counter[i] = ((int)(Math.pow(2.0D, this.delay[i][0] / 1200.0D) / this.control_time));
/*     */ 
/*  76 */       this.delay_counter[i] += (int)(this.delay2[i][0] / (this.control_time * 1000.0D));
/*     */     }
/*  78 */     processControlLogic();
/*     */   }
/*     */ 
/*     */   public void processControlLogic() {
/*  82 */     for (int i = 0; i < this.used_count; i++)
/*  83 */       if (this.delay_counter[i] > 0) {
/*  84 */         this.delay_counter[i] -= 1;
/*  85 */         this.out[i][0] = 0.5D;
/*     */       } else {
/*  87 */         double d1 = this.freq[i][0];
/*     */ 
/*  89 */         if (this.sin_stepfreq[i] != d1) {
/*  90 */           this.sin_stepfreq[i] = d1;
/*  91 */           d2 = 440.0D * Math.exp((d1 - 6900.0D) * (Math.log(2.0D) / 1200.0D));
/*     */ 
/*  93 */           this.sin_step[i] = (d2 * this.sin_factor);
/*     */         }
/*     */ 
/* 106 */         double d2 = this.sin_phase[i];
/* 107 */         d2 += this.sin_step[i];
/* 108 */         while (d2 > 6.283185307179586D)
/* 109 */           d2 -= 6.283185307179586D;
/* 110 */         this.out[i][0] = (0.5D + Math.sin(d2) * 0.5D);
/* 111 */         this.sin_phase[i] = d2;
/*     */       }
/*     */   }
/*     */ 
/*     */   public double[] get(int paramInt, String paramString)
/*     */   {
/* 118 */     if (paramInt >= this.used_count)
/* 119 */       this.used_count = (paramInt + 1);
/* 120 */     if (paramString == null)
/* 121 */       return this.out[paramInt];
/* 122 */     if (paramString.equals("delay"))
/* 123 */       return this.delay[paramInt];
/* 124 */     if (paramString.equals("delay2"))
/* 125 */       return this.delay2[paramInt];
/* 126 */     if (paramString.equals("freq"))
/* 127 */       return this.freq[paramInt];
/* 128 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftLowFrequencyOscillator
 * JD-Core Version:    0.6.2
 */