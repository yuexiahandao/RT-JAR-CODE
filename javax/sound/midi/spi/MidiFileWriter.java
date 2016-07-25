/*     */ package javax.sound.midi.spi;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import javax.sound.midi.Sequence;
/*     */ 
/*     */ public abstract class MidiFileWriter
/*     */ {
/*     */   public abstract int[] getMidiFileTypes();
/*     */ 
/*     */   public abstract int[] getMidiFileTypes(Sequence paramSequence);
/*     */ 
/*     */   public boolean isFileTypeSupported(int paramInt)
/*     */   {
/*  75 */     int[] arrayOfInt = getMidiFileTypes();
/*  76 */     for (int i = 0; i < arrayOfInt.length; i++) {
/*  77 */       if (paramInt == arrayOfInt[i]) {
/*  78 */         return true;
/*     */       }
/*     */     }
/*  81 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isFileTypeSupported(int paramInt, Sequence paramSequence)
/*     */   {
/*  95 */     int[] arrayOfInt = getMidiFileTypes(paramSequence);
/*  96 */     for (int i = 0; i < arrayOfInt.length; i++) {
/*  97 */       if (paramInt == arrayOfInt[i]) {
/*  98 */         return true;
/*     */       }
/*     */     }
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */   public abstract int write(Sequence paramSequence, int paramInt, OutputStream paramOutputStream)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract int write(Sequence paramSequence, int paramInt, File paramFile)
/*     */     throws IOException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.midi.spi.MidiFileWriter
 * JD-Core Version:    0.6.2
 */