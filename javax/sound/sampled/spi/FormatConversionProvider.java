/*     */ package javax.sound.sampled.spi;
/*     */ 
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioFormat.Encoding;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ 
/*     */ public abstract class FormatConversionProvider
/*     */ {
/*     */   public abstract AudioFormat.Encoding[] getSourceEncodings();
/*     */ 
/*     */   public abstract AudioFormat.Encoding[] getTargetEncodings();
/*     */ 
/*     */   public boolean isSourceEncodingSupported(AudioFormat.Encoding paramEncoding)
/*     */   {
/*  84 */     AudioFormat.Encoding[] arrayOfEncoding = getSourceEncodings();
/*     */ 
/*  86 */     for (int i = 0; i < arrayOfEncoding.length; i++) {
/*  87 */       if (paramEncoding.equals(arrayOfEncoding[i])) {
/*  88 */         return true;
/*     */       }
/*     */     }
/*  91 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isTargetEncodingSupported(AudioFormat.Encoding paramEncoding)
/*     */   {
/* 103 */     AudioFormat.Encoding[] arrayOfEncoding = getTargetEncodings();
/*     */ 
/* 105 */     for (int i = 0; i < arrayOfEncoding.length; i++) {
/* 106 */       if (paramEncoding.equals(arrayOfEncoding[i])) {
/* 107 */         return true;
/*     */       }
/*     */     }
/* 110 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract AudioFormat.Encoding[] getTargetEncodings(AudioFormat paramAudioFormat);
/*     */ 
/*     */   public boolean isConversionSupported(AudioFormat.Encoding paramEncoding, AudioFormat paramAudioFormat)
/*     */   {
/* 133 */     AudioFormat.Encoding[] arrayOfEncoding = getTargetEncodings(paramAudioFormat);
/*     */ 
/* 135 */     for (int i = 0; i < arrayOfEncoding.length; i++) {
/* 136 */       if (paramEncoding.equals(arrayOfEncoding[i])) {
/* 137 */         return true;
/*     */       }
/*     */     }
/* 140 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract AudioFormat[] getTargetFormats(AudioFormat.Encoding paramEncoding, AudioFormat paramAudioFormat);
/*     */ 
/*     */   public boolean isConversionSupported(AudioFormat paramAudioFormat1, AudioFormat paramAudioFormat2)
/*     */   {
/* 163 */     AudioFormat[] arrayOfAudioFormat = getTargetFormats(paramAudioFormat1.getEncoding(), paramAudioFormat2);
/*     */ 
/* 165 */     for (int i = 0; i < arrayOfAudioFormat.length; i++) {
/* 166 */       if (paramAudioFormat1.matches(arrayOfAudioFormat[i])) {
/* 167 */         return true;
/*     */       }
/*     */     }
/* 170 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract AudioInputStream getAudioInputStream(AudioFormat.Encoding paramEncoding, AudioInputStream paramAudioInputStream);
/*     */ 
/*     */   public abstract AudioInputStream getAudioInputStream(AudioFormat paramAudioFormat, AudioInputStream paramAudioInputStream);
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.spi.FormatConversionProvider
 * JD-Core Version:    0.6.2
 */