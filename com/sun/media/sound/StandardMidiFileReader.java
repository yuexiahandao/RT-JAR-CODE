/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import javax.sound.midi.InvalidMidiDataException;
/*     */ import javax.sound.midi.MidiFileFormat;
/*     */ import javax.sound.midi.Sequence;
/*     */ import javax.sound.midi.spi.MidiFileReader;
/*     */ 
/*     */ public final class StandardMidiFileReader extends MidiFileReader
/*     */ {
/*     */   private static final int MThd_MAGIC = 1297377380;
/*     */   private static final int bisBufferSize = 1024;
/*     */ 
/*     */   public MidiFileFormat getMidiFileFormat(InputStream paramInputStream)
/*     */     throws InvalidMidiDataException, IOException
/*     */   {
/*  64 */     return getMidiFileFormatFromStream(paramInputStream, -1, null);
/*     */   }
/*     */ 
/*     */   private MidiFileFormat getMidiFileFormatFromStream(InputStream paramInputStream, int paramInt, SMFParser paramSMFParser) throws InvalidMidiDataException, IOException
/*     */   {
/*  69 */     int i = 16;
/*  70 */     int j = -1;
/*     */     DataInputStream localDataInputStream;
/*  73 */     if ((paramInputStream instanceof DataInputStream))
/*  74 */       localDataInputStream = (DataInputStream)paramInputStream;
/*     */     else {
/*  76 */       localDataInputStream = new DataInputStream(paramInputStream);
/*     */     }
/*  78 */     if (paramSMFParser == null)
/*  79 */       localDataInputStream.mark(i);
/*     */     else {
/*  81 */       paramSMFParser.stream = localDataInputStream;
/*     */     }
/*     */     int k;
/*     */     float f;
/*     */     int n;
/*     */     try
/*     */     {
/*  90 */       int i1 = localDataInputStream.readInt();
/*  91 */       if (i1 != 1297377380)
/*     */       {
/*  93 */         throw new InvalidMidiDataException("not a valid MIDI file");
/*     */       }
/*     */ 
/*  97 */       int i2 = localDataInputStream.readInt() - 6;
/*  98 */       k = localDataInputStream.readShort();
/*  99 */       int m = localDataInputStream.readShort();
/* 100 */       int i3 = localDataInputStream.readShort();
/*     */ 
/* 103 */       if (i3 > 0)
/*     */       {
/* 105 */         f = 0.0F;
/* 106 */         n = i3;
/*     */       }
/*     */       else {
/* 109 */         int i4 = -1 * (i3 >> 8);
/* 110 */         switch (i4) {
/*     */         case 24:
/* 112 */           f = 24.0F;
/* 113 */           break;
/*     */         case 25:
/* 115 */           f = 25.0F;
/* 116 */           break;
/*     */         case 29:
/* 118 */           f = 29.969999F;
/* 119 */           break;
/*     */         case 30:
/* 121 */           f = 30.0F;
/* 122 */           break;
/*     */         case 26:
/*     */         case 27:
/*     */         case 28:
/*     */         default:
/* 124 */           throw new InvalidMidiDataException("Unknown frame code: " + i4);
/*     */         }
/*     */ 
/* 127 */         n = i3 & 0xFF;
/*     */       }
/* 129 */       if (paramSMFParser != null)
/*     */       {
/* 131 */         localDataInputStream.skip(i2);
/* 132 */         paramSMFParser.tracks = m;
/*     */       }
/*     */     }
/*     */     finally {
/* 136 */       if (paramSMFParser == null) {
/* 137 */         localDataInputStream.reset();
/*     */       }
/*     */     }
/* 140 */     MidiFileFormat localMidiFileFormat = new MidiFileFormat(k, f, n, paramInt, j);
/* 141 */     return localMidiFileFormat;
/*     */   }
/*     */ 
/*     */   public MidiFileFormat getMidiFileFormat(URL paramURL) throws InvalidMidiDataException, IOException
/*     */   {
/* 146 */     InputStream localInputStream = paramURL.openStream();
/* 147 */     BufferedInputStream localBufferedInputStream = new BufferedInputStream(localInputStream, 1024);
/* 148 */     MidiFileFormat localMidiFileFormat = null;
/*     */     try {
/* 150 */       localMidiFileFormat = getMidiFileFormat(localBufferedInputStream);
/*     */     } finally {
/* 152 */       localBufferedInputStream.close();
/*     */     }
/* 154 */     return localMidiFileFormat;
/*     */   }
/*     */ 
/*     */   public MidiFileFormat getMidiFileFormat(File paramFile) throws InvalidMidiDataException, IOException
/*     */   {
/* 159 */     FileInputStream localFileInputStream = new FileInputStream(paramFile);
/* 160 */     BufferedInputStream localBufferedInputStream = new BufferedInputStream(localFileInputStream, 1024);
/*     */ 
/* 163 */     long l = paramFile.length();
/* 164 */     if (l > 2147483647L) {
/* 165 */       l = -1L;
/*     */     }
/* 167 */     MidiFileFormat localMidiFileFormat = null;
/*     */     try {
/* 169 */       localMidiFileFormat = getMidiFileFormatFromStream(localBufferedInputStream, (int)l, null);
/*     */     } finally {
/* 171 */       localBufferedInputStream.close();
/*     */     }
/* 173 */     return localMidiFileFormat;
/*     */   }
/*     */ 
/*     */   public Sequence getSequence(InputStream paramInputStream) throws InvalidMidiDataException, IOException
/*     */   {
/* 178 */     SMFParser localSMFParser = new SMFParser();
/* 179 */     MidiFileFormat localMidiFileFormat = getMidiFileFormatFromStream(paramInputStream, -1, localSMFParser);
/*     */ 
/* 184 */     if ((localMidiFileFormat.getType() != 0) && (localMidiFileFormat.getType() != 1)) {
/* 185 */       throw new InvalidMidiDataException("Invalid or unsupported file type: " + localMidiFileFormat.getType());
/*     */     }
/*     */ 
/* 189 */     Sequence localSequence = new Sequence(localMidiFileFormat.getDivisionType(), localMidiFileFormat.getResolution());
/*     */ 
/* 192 */     for (int i = 0; (i < localSMFParser.tracks) && 
/* 193 */       (localSMFParser.nextTrack()); i++)
/*     */     {
/* 194 */       localSMFParser.readTrack(localSequence.createTrack());
/*     */     }
/*     */ 
/* 199 */     return localSequence;
/*     */   }
/*     */ 
/*     */   public Sequence getSequence(URL paramURL)
/*     */     throws InvalidMidiDataException, IOException
/*     */   {
/* 205 */     Object localObject1 = paramURL.openStream();
/* 206 */     localObject1 = new BufferedInputStream((InputStream)localObject1, 1024);
/* 207 */     Sequence localSequence = null;
/*     */     try {
/* 209 */       localSequence = getSequence((InputStream)localObject1);
/*     */     } finally {
/* 211 */       ((InputStream)localObject1).close();
/*     */     }
/* 213 */     return localSequence;
/*     */   }
/*     */ 
/*     */   public Sequence getSequence(File paramFile) throws InvalidMidiDataException, IOException
/*     */   {
/* 218 */     Object localObject1 = new FileInputStream(paramFile);
/* 219 */     localObject1 = new BufferedInputStream((InputStream)localObject1, 1024);
/* 220 */     Sequence localSequence = null;
/*     */     try {
/* 222 */       localSequence = getSequence((InputStream)localObject1);
/*     */     } finally {
/* 224 */       ((InputStream)localObject1).close();
/*     */     }
/* 226 */     return localSequence;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.StandardMidiFileReader
 * JD-Core Version:    0.6.2
 */