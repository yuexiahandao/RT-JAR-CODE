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
/*    */ public final class SF2SoundbankReader extends SoundbankReader
/*    */ {
/*    */   public Soundbank getSoundbank(URL paramURL)
/*    */     throws InvalidMidiDataException, IOException
/*    */   {
/*    */     try
/*    */     {
/* 46 */       return new SF2Soundbank(paramURL);
/*    */     } catch (RIFFInvalidFormatException localRIFFInvalidFormatException) {
/* 48 */       return null; } catch (IOException localIOException) {
/*    */     }
/* 50 */     return null;
/*    */   }
/*    */ 
/*    */   public Soundbank getSoundbank(InputStream paramInputStream) throws InvalidMidiDataException, IOException
/*    */   {
/*    */     try
/*    */     {
/* 57 */       paramInputStream.mark(512);
/* 58 */       return new SF2Soundbank(paramInputStream);
/*    */     } catch (RIFFInvalidFormatException localRIFFInvalidFormatException) {
/* 60 */       paramInputStream.reset();
/* 61 */     }return null;
/*    */   }
/*    */ 
/*    */   public Soundbank getSoundbank(File paramFile) throws InvalidMidiDataException, IOException
/*    */   {
/*    */     try
/*    */     {
/* 68 */       return new SF2Soundbank(paramFile); } catch (RIFFInvalidFormatException localRIFFInvalidFormatException) {
/*    */     }
/* 70 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SF2SoundbankReader
 * JD-Core Version:    0.6.2
 */