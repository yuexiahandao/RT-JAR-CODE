/*     */ package javax.sound.sampled.spi;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import javax.sound.sampled.AudioFileFormat.Type;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ 
/*     */ public abstract class AudioFileWriter
/*     */ {
/*     */   public abstract AudioFileFormat.Type[] getAudioFileTypes();
/*     */ 
/*     */   public boolean isFileTypeSupported(AudioFileFormat.Type paramType)
/*     */   {
/*  65 */     AudioFileFormat.Type[] arrayOfType = getAudioFileTypes();
/*     */ 
/*  67 */     for (int i = 0; i < arrayOfType.length; i++) {
/*  68 */       if (paramType.equals(arrayOfType[i])) {
/*  69 */         return true;
/*     */       }
/*     */     }
/*  72 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract AudioFileFormat.Type[] getAudioFileTypes(AudioInputStream paramAudioInputStream);
/*     */ 
/*     */   public boolean isFileTypeSupported(AudioFileFormat.Type paramType, AudioInputStream paramAudioInputStream)
/*     */   {
/*  97 */     AudioFileFormat.Type[] arrayOfType = getAudioFileTypes(paramAudioInputStream);
/*     */ 
/*  99 */     for (int i = 0; i < arrayOfType.length; i++) {
/* 100 */       if (paramType.equals(arrayOfType[i])) {
/* 101 */         return true;
/*     */       }
/*     */     }
/* 104 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract int write(AudioInputStream paramAudioInputStream, AudioFileFormat.Type paramType, OutputStream paramOutputStream)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract int write(AudioInputStream paramAudioInputStream, AudioFileFormat.Type paramType, File paramFile)
/*     */     throws IOException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.spi.AudioFileWriter
 * JD-Core Version:    0.6.2
 */