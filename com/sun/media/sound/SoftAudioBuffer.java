/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ 
/*     */ public final class SoftAudioBuffer
/*     */ {
/*     */   private int size;
/*     */   private float[] buffer;
/*  40 */   private boolean empty = true;
/*     */   private AudioFormat format;
/*     */   private AudioFloatConverter converter;
/*     */   private byte[] converter_buffer;
/*     */ 
/*     */   public SoftAudioBuffer(int paramInt, AudioFormat paramAudioFormat)
/*     */   {
/*  46 */     this.size = paramInt;
/*  47 */     this.format = paramAudioFormat;
/*  48 */     this.converter = AudioFloatConverter.getConverter(paramAudioFormat);
/*     */   }
/*     */ 
/*     */   public void swap(SoftAudioBuffer paramSoftAudioBuffer)
/*     */   {
/*  53 */     int i = this.size;
/*  54 */     float[] arrayOfFloat = this.buffer;
/*  55 */     boolean bool = this.empty;
/*  56 */     AudioFormat localAudioFormat = this.format;
/*  57 */     AudioFloatConverter localAudioFloatConverter = this.converter;
/*  58 */     byte[] arrayOfByte = this.converter_buffer;
/*     */ 
/*  60 */     this.size = paramSoftAudioBuffer.size;
/*  61 */     this.buffer = paramSoftAudioBuffer.buffer;
/*  62 */     this.empty = paramSoftAudioBuffer.empty;
/*  63 */     this.format = paramSoftAudioBuffer.format;
/*  64 */     this.converter = paramSoftAudioBuffer.converter;
/*  65 */     this.converter_buffer = paramSoftAudioBuffer.converter_buffer;
/*     */ 
/*  67 */     paramSoftAudioBuffer.size = i;
/*  68 */     paramSoftAudioBuffer.buffer = arrayOfFloat;
/*  69 */     paramSoftAudioBuffer.empty = bool;
/*  70 */     paramSoftAudioBuffer.format = localAudioFormat;
/*  71 */     paramSoftAudioBuffer.converter = localAudioFloatConverter;
/*  72 */     paramSoftAudioBuffer.converter_buffer = arrayOfByte;
/*     */   }
/*     */ 
/*     */   public AudioFormat getFormat() {
/*  76 */     return this.format;
/*     */   }
/*     */ 
/*     */   public int getSize() {
/*  80 */     return this.size;
/*     */   }
/*     */ 
/*     */   public void clear() {
/*  84 */     if (!this.empty) {
/*  85 */       Arrays.fill(this.buffer, 0.0F);
/*  86 */       this.empty = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isSilent() {
/*  91 */     return this.empty;
/*     */   }
/*     */ 
/*     */   public float[] array() {
/*  95 */     this.empty = false;
/*  96 */     if (this.buffer == null)
/*  97 */       this.buffer = new float[this.size];
/*  98 */     return this.buffer;
/*     */   }
/*     */ 
/*     */   public void get(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 103 */     int i = this.format.getFrameSize() / this.format.getChannels();
/* 104 */     int j = this.size * i;
/* 105 */     if ((this.converter_buffer == null) || (this.converter_buffer.length < j)) {
/* 106 */       this.converter_buffer = new byte[j];
/*     */     }
/* 108 */     if (this.format.getChannels() == 1) {
/* 109 */       this.converter.toByteArray(array(), this.size, paramArrayOfByte);
/*     */     } else {
/* 111 */       this.converter.toByteArray(array(), this.size, this.converter_buffer);
/* 112 */       if (paramInt >= this.format.getChannels())
/* 113 */         return;
/* 114 */       int k = this.format.getChannels() * i;
/* 115 */       int m = i;
/* 116 */       for (int n = 0; n < i; n++) {
/* 117 */         int i1 = n;
/* 118 */         int i2 = paramInt * i + n;
/* 119 */         for (int i3 = 0; i3 < this.size; i3++) {
/* 120 */           paramArrayOfByte[i2] = this.converter_buffer[i1];
/* 121 */           i2 += k;
/* 122 */           i1 += m;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftAudioBuffer
 * JD-Core Version:    0.6.2
 */