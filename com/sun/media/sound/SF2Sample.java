/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import javax.sound.midi.Soundbank;
/*     */ import javax.sound.midi.SoundbankResource;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ 
/*     */ public final class SF2Sample extends SoundbankResource
/*     */ {
/*  41 */   String name = "";
/*  42 */   long startLoop = 0L;
/*  43 */   long endLoop = 0L;
/*  44 */   long sampleRate = 44100L;
/*  45 */   int originalPitch = 60;
/*  46 */   byte pitchCorrection = 0;
/*  47 */   int sampleLink = 0;
/*  48 */   int sampleType = 0;
/*     */   ModelByteBuffer data;
/*     */   ModelByteBuffer data24;
/*     */ 
/*     */   public SF2Sample(Soundbank paramSoundbank)
/*     */   {
/*  53 */     super(paramSoundbank, null, AudioInputStream.class);
/*     */   }
/*     */ 
/*     */   public SF2Sample() {
/*  57 */     super(null, null, AudioInputStream.class);
/*     */   }
/*     */ 
/*     */   public Object getData()
/*     */   {
/*  62 */     AudioFormat localAudioFormat = getFormat();
/*     */ 
/*  98 */     InputStream localInputStream = this.data.getInputStream();
/*  99 */     if (localInputStream == null)
/* 100 */       return null;
/* 101 */     return new AudioInputStream(localInputStream, localAudioFormat, this.data.capacity());
/*     */   }
/*     */ 
/*     */   public ModelByteBuffer getDataBuffer() {
/* 105 */     return this.data;
/*     */   }
/*     */ 
/*     */   public ModelByteBuffer getData24Buffer() {
/* 109 */     return this.data24;
/*     */   }
/*     */ 
/*     */   public AudioFormat getFormat() {
/* 113 */     return new AudioFormat((float)this.sampleRate, 16, 1, true, false);
/*     */   }
/*     */ 
/*     */   public void setData(ModelByteBuffer paramModelByteBuffer) {
/* 117 */     this.data = paramModelByteBuffer;
/*     */   }
/*     */ 
/*     */   public void setData(byte[] paramArrayOfByte) {
/* 121 */     this.data = new ModelByteBuffer(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public void setData(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 125 */     this.data = new ModelByteBuffer(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void setData24(ModelByteBuffer paramModelByteBuffer) {
/* 129 */     this.data24 = paramModelByteBuffer;
/*     */   }
/*     */ 
/*     */   public void setData24(byte[] paramArrayOfByte) {
/* 133 */     this.data24 = new ModelByteBuffer(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public void setData24(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 137 */     this.data24 = new ModelByteBuffer(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 150 */     return this.name;
/*     */   }
/*     */ 
/*     */   public void setName(String paramString) {
/* 154 */     this.name = paramString;
/*     */   }
/*     */ 
/*     */   public long getEndLoop() {
/* 158 */     return this.endLoop;
/*     */   }
/*     */ 
/*     */   public void setEndLoop(long paramLong) {
/* 162 */     this.endLoop = paramLong;
/*     */   }
/*     */ 
/*     */   public int getOriginalPitch() {
/* 166 */     return this.originalPitch;
/*     */   }
/*     */ 
/*     */   public void setOriginalPitch(int paramInt) {
/* 170 */     this.originalPitch = paramInt;
/*     */   }
/*     */ 
/*     */   public byte getPitchCorrection() {
/* 174 */     return this.pitchCorrection;
/*     */   }
/*     */ 
/*     */   public void setPitchCorrection(byte paramByte) {
/* 178 */     this.pitchCorrection = paramByte;
/*     */   }
/*     */ 
/*     */   public int getSampleLink() {
/* 182 */     return this.sampleLink;
/*     */   }
/*     */ 
/*     */   public void setSampleLink(int paramInt) {
/* 186 */     this.sampleLink = paramInt;
/*     */   }
/*     */ 
/*     */   public long getSampleRate() {
/* 190 */     return this.sampleRate;
/*     */   }
/*     */ 
/*     */   public void setSampleRate(long paramLong) {
/* 194 */     this.sampleRate = paramLong;
/*     */   }
/*     */ 
/*     */   public int getSampleType() {
/* 198 */     return this.sampleType;
/*     */   }
/*     */ 
/*     */   public void setSampleType(int paramInt) {
/* 202 */     this.sampleType = paramInt;
/*     */   }
/*     */ 
/*     */   public long getStartLoop() {
/* 206 */     return this.startLoop;
/*     */   }
/*     */ 
/*     */   public void setStartLoop(long paramLong) {
/* 210 */     this.startLoop = paramLong;
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 214 */     return "Sample: " + this.name;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SF2Sample
 * JD-Core Version:    0.6.2
 */