/*    */ package com.sun.media.sound;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.URL;
/*    */ import javax.sound.midi.InvalidMidiDataException;
/*    */ import javax.sound.midi.Soundbank;
/*    */ import javax.sound.midi.spi.SoundbankReader;
/*    */ 
/*    */ public final class DLSSoundbankReader extends SoundbankReader
/*    */ {
/*    */   public Soundbank getSoundbank(URL paramURL)
/*    */     throws InvalidMidiDataException, IOException
/*    */   {
/*    */     try
/*    */     {
/* 47 */       return new DLSSoundbank(paramURL);
/*    */     } catch (RIFFInvalidFormatException localRIFFInvalidFormatException) {
/* 49 */       return null; } catch (IOException localIOException) {
/*    */     }
/* 51 */     return null;
/*    */   }
/*    */ 
/*    */   public Soundbank getSoundbank(InputStream paramInputStream) throws InvalidMidiDataException, IOException
/*    */   {
/*    */     try
/*    */     {
/* 58 */       paramInputStream.mark(512);
/* 59 */       return new DLSSoundbank(paramInputStream);
/*    */     } catch (RIFFInvalidFormatException localRIFFInvalidFormatException) {
/* 61 */       paramInputStream.reset();
/* 62 */     }return null;
/*    */   }
/*    */ 
/*    */   public Soundbank getSoundbank(File paramFile) throws InvalidMidiDataException, IOException
/*    */   {
/*    */     try
/*    */     {
/* 69 */       return new DLSSoundbank(paramFile); } catch (RIFFInvalidFormatException localRIFFInvalidFormatException) {
/*    */     }
/* 71 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.DLSSoundbankReader
 * JD-Core Version:    0.6.2
 */