/*     */ package sun.audio;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.FilterInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import javax.sound.midi.InvalidMidiDataException;
/*     */ import javax.sound.midi.MidiFileFormat;
/*     */ import javax.sound.midi.MidiSystem;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ import javax.sound.sampled.AudioSystem;
/*     */ import javax.sound.sampled.UnsupportedAudioFileException;
/*     */ 
/*     */ public final class AudioStream extends FilterInputStream
/*     */ {
/*  45 */   AudioInputStream ais = null;
/*  46 */   AudioFormat format = null;
/*  47 */   MidiFileFormat midiformat = null;
/*  48 */   InputStream stream = null;
/*     */ 
/*     */   public AudioStream(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  59 */     super(paramInputStream);
/*     */ 
/*  61 */     this.stream = paramInputStream;
/*     */ 
/*  63 */     if (!paramInputStream.markSupported())
/*     */     {
/*  65 */       this.stream = new BufferedInputStream(paramInputStream, 1024);
/*     */     }
/*     */     try
/*     */     {
/*  69 */       this.ais = AudioSystem.getAudioInputStream(this.stream);
/*  70 */       this.format = this.ais.getFormat();
/*  71 */       this.in = this.ais;
/*     */     }
/*     */     catch (UnsupportedAudioFileException localUnsupportedAudioFileException)
/*     */     {
/*     */       try
/*     */       {
/*  77 */         this.midiformat = MidiSystem.getMidiFileFormat(this.stream);
/*     */       }
/*     */       catch (InvalidMidiDataException localInvalidMidiDataException) {
/*  80 */         throw new IOException("could not create audio stream from input stream");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public AudioData getData()
/*     */     throws IOException
/*     */   {
/* 101 */     int i = getLength();
/*     */ 
/* 104 */     if (i < 1048576) {
/* 105 */       byte[] arrayOfByte = new byte[i];
/*     */       try {
/* 107 */         this.ais.read(arrayOfByte, 0, i);
/*     */       } catch (IOException localIOException) {
/* 109 */         throw new IOException("Could not create AudioData Object");
/*     */       }
/* 111 */       return new AudioData(this.format, arrayOfByte);
/*     */     }
/*     */ 
/* 125 */     throw new IOException("could not create AudioData object");
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 131 */     if ((this.ais != null) && (this.format != null)) {
/* 132 */       return (int)(this.ais.getFrameLength() * this.ais.getFormat().getFrameSize());
/*     */     }
/*     */ 
/* 135 */     if (this.midiformat != null) {
/* 136 */       return this.midiformat.getByteLength();
/*     */     }
/*     */ 
/* 139 */     return -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.audio.AudioStream
 * JD-Core Version:    0.6.2
 */