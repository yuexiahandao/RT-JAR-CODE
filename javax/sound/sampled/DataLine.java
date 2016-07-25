/*     */ package javax.sound.sampled;
/*     */ 
/*     */ public abstract interface DataLine extends Line
/*     */ {
/*     */   public abstract void drain();
/*     */ 
/*     */   public abstract void flush();
/*     */ 
/*     */   public abstract void start();
/*     */ 
/*     */   public abstract void stop();
/*     */ 
/*     */   public abstract boolean isRunning();
/*     */ 
/*     */   public abstract boolean isActive();
/*     */ 
/*     */   public abstract AudioFormat getFormat();
/*     */ 
/*     */   public abstract int getBufferSize();
/*     */ 
/*     */   public abstract int available();
/*     */ 
/*     */   public abstract int getFramePosition();
/*     */ 
/*     */   public abstract long getLongFramePosition();
/*     */ 
/*     */   public abstract long getMicrosecondPosition();
/*     */ 
/*     */   public abstract float getLevel();
/*     */ 
/*     */   public static class Info extends Line.Info
/*     */   {
/*     */     private AudioFormat[] formats;
/*     */     private int minBufferSize;
/*     */     private int maxBufferSize;
/*     */ 
/*     */     public Info(Class<?> paramClass, AudioFormat[] paramArrayOfAudioFormat, int paramInt1, int paramInt2)
/*     */     {
/* 302 */       super();
/*     */ 
/* 304 */       if (paramArrayOfAudioFormat == null)
/* 305 */         this.formats = new AudioFormat[0];
/*     */       else {
/* 307 */         this.formats = paramArrayOfAudioFormat;
/*     */       }
/*     */ 
/* 310 */       this.minBufferSize = paramInt1;
/* 311 */       this.maxBufferSize = paramInt2;
/*     */     }
/*     */ 
/*     */     public Info(Class<?> paramClass, AudioFormat paramAudioFormat, int paramInt)
/*     */     {
/* 327 */       super();
/*     */ 
/* 329 */       if (paramAudioFormat == null) {
/* 330 */         this.formats = new AudioFormat[0];
/*     */       } else {
/* 332 */         AudioFormat[] arrayOfAudioFormat = { paramAudioFormat };
/* 333 */         this.formats = arrayOfAudioFormat;
/*     */       }
/*     */ 
/* 336 */       this.minBufferSize = paramInt;
/* 337 */       this.maxBufferSize = paramInt;
/*     */     }
/*     */ 
/*     */     public Info(Class<?> paramClass, AudioFormat paramAudioFormat)
/*     */     {
/* 351 */       this(paramClass, paramAudioFormat, -1);
/*     */     }
/*     */ 
/*     */     public AudioFormat[] getFormats()
/*     */     {
/* 377 */       AudioFormat[] arrayOfAudioFormat = new AudioFormat[this.formats.length];
/* 378 */       System.arraycopy(this.formats, 0, arrayOfAudioFormat, 0, this.formats.length);
/* 379 */       return arrayOfAudioFormat;
/*     */     }
/*     */ 
/*     */     public boolean isFormatSupported(AudioFormat paramAudioFormat)
/*     */     {
/* 394 */       for (int i = 0; i < this.formats.length; i++) {
/* 395 */         if (paramAudioFormat.matches(this.formats[i])) {
/* 396 */           return true;
/*     */         }
/*     */       }
/*     */ 
/* 400 */       return false;
/*     */     }
/*     */ 
/*     */     public int getMinBufferSize()
/*     */     {
/* 408 */       return this.minBufferSize;
/*     */     }
/*     */ 
/*     */     public int getMaxBufferSize()
/*     */     {
/* 417 */       return this.maxBufferSize;
/*     */     }
/*     */ 
/*     */     public boolean matches(Line.Info paramInfo)
/*     */     {
/* 433 */       if (!super.matches(paramInfo)) {
/* 434 */         return false;
/*     */       }
/*     */ 
/* 437 */       Info localInfo = (Info)paramInfo;
/*     */ 
/* 442 */       if ((getMaxBufferSize() >= 0) && (localInfo.getMaxBufferSize() >= 0) && 
/* 443 */         (getMaxBufferSize() > localInfo.getMaxBufferSize())) {
/* 444 */         return false;
/*     */       }
/*     */ 
/* 448 */       if ((getMinBufferSize() >= 0) && (localInfo.getMinBufferSize() >= 0) && 
/* 449 */         (getMinBufferSize() < localInfo.getMinBufferSize())) {
/* 450 */         return false;
/*     */       }
/*     */ 
/* 454 */       AudioFormat[] arrayOfAudioFormat = getFormats();
/*     */ 
/* 456 */       if (arrayOfAudioFormat != null)
/*     */       {
/* 458 */         for (int i = 0; i < arrayOfAudioFormat.length; i++) {
/* 459 */           if ((arrayOfAudioFormat[i] != null) && 
/* 460 */             (!localInfo.isFormatSupported(arrayOfAudioFormat[i]))) {
/* 461 */             return false;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 467 */       return true;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 476 */       StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 478 */       if ((this.formats.length == 1) && (this.formats[0] != null))
/* 479 */         localStringBuffer.append(" supporting format " + this.formats[0]);
/* 480 */       else if (getFormats().length > 1) {
/* 481 */         localStringBuffer.append(" supporting " + getFormats().length + " audio formats");
/*     */       }
/*     */ 
/* 484 */       if ((this.minBufferSize != -1) && (this.maxBufferSize != -1))
/* 485 */         localStringBuffer.append(", and buffers of " + this.minBufferSize + " to " + this.maxBufferSize + " bytes");
/* 486 */       else if ((this.minBufferSize != -1) && (this.minBufferSize > 0))
/* 487 */         localStringBuffer.append(", and buffers of at least " + this.minBufferSize + " bytes");
/* 488 */       else if (this.maxBufferSize != -1) {
/* 489 */         localStringBuffer.append(", and buffers of up to " + this.minBufferSize + " bytes");
/*     */       }
/*     */ 
/* 492 */       return new String(super.toString() + localStringBuffer);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.DataLine
 * JD-Core Version:    0.6.2
 */