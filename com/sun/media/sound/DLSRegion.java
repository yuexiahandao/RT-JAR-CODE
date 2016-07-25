/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public final class DLSRegion
/*     */ {
/*     */   public static final int OPTION_SELFNONEXCLUSIVE = 1;
/*  42 */   List<DLSModulator> modulators = new ArrayList();
/*     */   int keyfrom;
/*     */   int keyto;
/*     */   int velfrom;
/*     */   int velto;
/*     */   int options;
/*     */   int exclusiveClass;
/*     */   int fusoptions;
/*     */   int phasegroup;
/*     */   long channel;
/*  52 */   DLSSample sample = null;
/*     */   DLSSampleOptions sampleoptions;
/*     */ 
/*     */   public List<DLSModulator> getModulators()
/*     */   {
/*  56 */     return this.modulators;
/*     */   }
/*     */ 
/*     */   public long getChannel() {
/*  60 */     return this.channel;
/*     */   }
/*     */ 
/*     */   public void setChannel(long paramLong) {
/*  64 */     this.channel = paramLong;
/*     */   }
/*     */ 
/*     */   public int getExclusiveClass() {
/*  68 */     return this.exclusiveClass;
/*     */   }
/*     */ 
/*     */   public void setExclusiveClass(int paramInt) {
/*  72 */     this.exclusiveClass = paramInt;
/*     */   }
/*     */ 
/*     */   public int getFusoptions() {
/*  76 */     return this.fusoptions;
/*     */   }
/*     */ 
/*     */   public void setFusoptions(int paramInt) {
/*  80 */     this.fusoptions = paramInt;
/*     */   }
/*     */ 
/*     */   public int getKeyfrom() {
/*  84 */     return this.keyfrom;
/*     */   }
/*     */ 
/*     */   public void setKeyfrom(int paramInt) {
/*  88 */     this.keyfrom = paramInt;
/*     */   }
/*     */ 
/*     */   public int getKeyto() {
/*  92 */     return this.keyto;
/*     */   }
/*     */ 
/*     */   public void setKeyto(int paramInt) {
/*  96 */     this.keyto = paramInt;
/*     */   }
/*     */ 
/*     */   public int getOptions() {
/* 100 */     return this.options;
/*     */   }
/*     */ 
/*     */   public void setOptions(int paramInt) {
/* 104 */     this.options = paramInt;
/*     */   }
/*     */ 
/*     */   public int getPhasegroup() {
/* 108 */     return this.phasegroup;
/*     */   }
/*     */ 
/*     */   public void setPhasegroup(int paramInt) {
/* 112 */     this.phasegroup = paramInt;
/*     */   }
/*     */ 
/*     */   public DLSSample getSample() {
/* 116 */     return this.sample;
/*     */   }
/*     */ 
/*     */   public void setSample(DLSSample paramDLSSample) {
/* 120 */     this.sample = paramDLSSample;
/*     */   }
/*     */ 
/*     */   public int getVelfrom() {
/* 124 */     return this.velfrom;
/*     */   }
/*     */ 
/*     */   public void setVelfrom(int paramInt) {
/* 128 */     this.velfrom = paramInt;
/*     */   }
/*     */ 
/*     */   public int getVelto() {
/* 132 */     return this.velto;
/*     */   }
/*     */ 
/*     */   public void setVelto(int paramInt) {
/* 136 */     this.velto = paramInt;
/*     */   }
/*     */ 
/*     */   public void setModulators(List<DLSModulator> paramList) {
/* 140 */     this.modulators = paramList;
/*     */   }
/*     */ 
/*     */   public DLSSampleOptions getSampleoptions() {
/* 144 */     return this.sampleoptions;
/*     */   }
/*     */ 
/*     */   public void setSampleoptions(DLSSampleOptions paramDLSSampleOptions) {
/* 148 */     this.sampleoptions = paramDLSSampleOptions;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.DLSRegion
 * JD-Core Version:    0.6.2
 */