/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URL;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.sound.sampled.AudioFileFormat;
/*     */ import javax.sound.sampled.AudioFileFormat.Type;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ import javax.sound.sampled.AudioFormat.Encoding;
/*     */ import javax.sound.sampled.AudioInputStream;
/*     */ import javax.sound.sampled.UnsupportedAudioFileException;
/*     */ import javax.sound.sampled.spi.AudioFileReader;
/*     */ 
/*     */ public final class WaveExtensibleFileReader extends AudioFileReader
/*     */ {
/* 143 */   private static final String[] channelnames = { "FL", "FR", "FC", "LF", "BL", "BR", "FLC", "FLR", "BC", "SL", "SR", "TC", "TFL", "TFC", "TFR", "TBL", "TBC", "TBR" };
/*     */ 
/* 149 */   private static final String[] allchannelnames = { "w1", "w2", "w3", "w4", "w5", "w6", "w7", "w8", "w9", "w10", "w11", "w12", "w13", "w14", "w15", "w16", "w17", "w18", "w19", "w20", "w21", "w22", "w23", "w24", "w25", "w26", "w27", "w28", "w29", "w30", "w31", "w32", "w33", "w34", "w35", "w36", "w37", "w38", "w39", "w40", "w41", "w42", "w43", "w44", "w45", "w46", "w47", "w48", "w49", "w50", "w51", "w52", "w53", "w54", "w55", "w56", "w57", "w58", "w59", "w60", "w61", "w62", "w63", "w64" };
/*     */ 
/* 158 */   private static final GUID SUBTYPE_PCM = new GUID(1L, 0, 16, 128, 0, 0, 170, 0, 56, 155, 113);
/*     */ 
/* 161 */   private static final GUID SUBTYPE_IEEE_FLOAT = new GUID(3L, 0, 16, 128, 0, 0, 170, 0, 56, 155, 113);
/*     */ 
/*     */   private String decodeChannelMask(long paramLong)
/*     */   {
/* 165 */     StringBuffer localStringBuffer = new StringBuffer();
/* 166 */     long l = 1L;
/* 167 */     for (int i = 0; i < allchannelnames.length; i++) {
/* 168 */       if ((paramLong & l) != 0L) {
/* 169 */         if (i < channelnames.length)
/* 170 */           localStringBuffer.append(channelnames[i] + " ");
/*     */         else {
/* 172 */           localStringBuffer.append(allchannelnames[i] + " ");
/*     */         }
/*     */       }
/* 175 */       l *= 2L;
/*     */     }
/* 177 */     if (localStringBuffer.length() == 0)
/* 178 */       return null;
/* 179 */     return localStringBuffer.substring(0, localStringBuffer.length() - 1);
/*     */   }
/*     */ 
/*     */   public AudioFileFormat getAudioFileFormat(InputStream paramInputStream) throws UnsupportedAudioFileException, IOException
/*     */   {
/* 186 */     paramInputStream.mark(200);
/*     */     AudioFileFormat localAudioFileFormat;
/*     */     try
/*     */     {
/* 189 */       localAudioFileFormat = internal_getAudioFileFormat(paramInputStream);
/*     */     } finally {
/* 191 */       paramInputStream.reset();
/*     */     }
/* 193 */     return localAudioFileFormat;
/*     */   }
/*     */ 
/*     */   private AudioFileFormat internal_getAudioFileFormat(InputStream paramInputStream)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 199 */     RIFFReader localRIFFReader = new RIFFReader(paramInputStream);
/* 200 */     if (!localRIFFReader.getFormat().equals("RIFF"))
/* 201 */       throw new UnsupportedAudioFileException();
/* 202 */     if (!localRIFFReader.getType().equals("WAVE")) {
/* 203 */       throw new UnsupportedAudioFileException();
/*     */     }
/* 205 */     int i = 0;
/* 206 */     int j = 0;
/*     */ 
/* 208 */     int k = 1;
/* 209 */     long l1 = 1L;
/*     */ 
/* 211 */     int m = 1;
/* 212 */     int n = 1;
/* 213 */     int i1 = 1;
/* 214 */     long l2 = 0L;
/* 215 */     GUID localGUID = null;
/*     */ 
/* 217 */     while (localRIFFReader.hasNextChunk()) {
/* 218 */       localObject = localRIFFReader.nextChunk();
/*     */ 
/* 220 */       if (((RIFFReader)localObject).getFormat().equals("fmt ")) {
/* 221 */         i = 1;
/*     */ 
/* 223 */         int i2 = ((RIFFReader)localObject).readUnsignedShort();
/* 224 */         if (i2 != 65534) {
/* 225 */           throw new UnsupportedAudioFileException();
/*     */         }
/* 227 */         k = ((RIFFReader)localObject).readUnsignedShort();
/* 228 */         l1 = ((RIFFReader)localObject).readUnsignedInt();
/* 229 */         ((RIFFReader)localObject).readUnsignedInt();
/* 230 */         m = ((RIFFReader)localObject).readUnsignedShort();
/* 231 */         n = ((RIFFReader)localObject).readUnsignedShort();
/* 232 */         int i3 = ((RIFFReader)localObject).readUnsignedShort();
/* 233 */         if (i3 != 22)
/* 234 */           throw new UnsupportedAudioFileException();
/* 235 */         i1 = ((RIFFReader)localObject).readUnsignedShort();
/* 236 */         if (i1 > n)
/* 237 */           throw new UnsupportedAudioFileException();
/* 238 */         l2 = ((RIFFReader)localObject).readUnsignedInt();
/* 239 */         localGUID = GUID.read((RIFFReader)localObject);
/*     */       }
/*     */ 
/* 242 */       if (((RIFFReader)localObject).getFormat().equals("data")) {
/* 243 */         j = 1;
/* 244 */         break;
/*     */       }
/*     */     }
/*     */ 
/* 248 */     if (i == 0)
/* 249 */       throw new UnsupportedAudioFileException();
/* 250 */     if (j == 0) {
/* 251 */       throw new UnsupportedAudioFileException();
/*     */     }
/* 253 */     Object localObject = new HashMap();
/* 254 */     String str = decodeChannelMask(l2);
/* 255 */     if (str != null)
/* 256 */       ((Map)localObject).put("channelOrder", str);
/* 257 */     if (l2 != 0L) {
/* 258 */       ((Map)localObject).put("channelMask", Long.valueOf(l2));
/*     */     }
/*     */ 
/* 261 */     ((Map)localObject).put("validBitsPerSample", Integer.valueOf(i1));
/*     */ 
/* 263 */     AudioFormat localAudioFormat = null;
/* 264 */     if (localGUID.equals(SUBTYPE_PCM)) {
/* 265 */       if (n == 8) {
/* 266 */         localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_UNSIGNED, (float)l1, n, k, m, (float)l1, false, (Map)localObject);
/*     */       }
/*     */       else
/*     */       {
/* 270 */         localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, (float)l1, n, k, m, (float)l1, false, (Map)localObject);
/*     */       }
/*     */     }
/* 273 */     else if (localGUID.equals(SUBTYPE_IEEE_FLOAT)) {
/* 274 */       localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_FLOAT, (float)l1, n, k, m, (float)l1, false, (Map)localObject);
/*     */     }
/*     */     else {
/* 277 */       throw new UnsupportedAudioFileException();
/*     */     }
/* 279 */     AudioFileFormat localAudioFileFormat = new AudioFileFormat(AudioFileFormat.Type.WAVE, localAudioFormat, -1);
/*     */ 
/* 282 */     return localAudioFileFormat;
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(InputStream paramInputStream)
/*     */     throws UnsupportedAudioFileException, IOException
/*     */   {
/* 288 */     AudioFileFormat localAudioFileFormat = getAudioFileFormat(paramInputStream);
/* 289 */     RIFFReader localRIFFReader1 = new RIFFReader(paramInputStream);
/* 290 */     if (!localRIFFReader1.getFormat().equals("RIFF"))
/* 291 */       throw new UnsupportedAudioFileException();
/* 292 */     if (!localRIFFReader1.getType().equals("WAVE"))
/* 293 */       throw new UnsupportedAudioFileException();
/* 294 */     while (localRIFFReader1.hasNextChunk()) {
/* 295 */       RIFFReader localRIFFReader2 = localRIFFReader1.nextChunk();
/* 296 */       if (localRIFFReader2.getFormat().equals("data")) {
/* 297 */         return new AudioInputStream(localRIFFReader2, localAudioFileFormat.getFormat(), localRIFFReader2.getSize());
/*     */       }
/*     */     }
/*     */ 
/* 301 */     throw new UnsupportedAudioFileException();
/*     */   }
/*     */ 
/*     */   public AudioFileFormat getAudioFileFormat(URL paramURL) throws UnsupportedAudioFileException, IOException {
/* 306 */     InputStream localInputStream = paramURL.openStream();
/*     */     AudioFileFormat localAudioFileFormat;
/*     */     try {
/* 309 */       localAudioFileFormat = getAudioFileFormat(new BufferedInputStream(localInputStream));
/*     */     } finally {
/* 311 */       localInputStream.close();
/*     */     }
/* 313 */     return localAudioFileFormat;
/*     */   }
/*     */ 
/*     */   public AudioFileFormat getAudioFileFormat(File paramFile) throws UnsupportedAudioFileException, IOException {
/* 318 */     FileInputStream localFileInputStream = new FileInputStream(paramFile);
/*     */     AudioFileFormat localAudioFileFormat;
/*     */     try {
/* 321 */       localAudioFileFormat = getAudioFileFormat(new BufferedInputStream(localFileInputStream));
/*     */     } finally {
/* 323 */       localFileInputStream.close();
/*     */     }
/* 325 */     return localAudioFileFormat;
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(URL paramURL) throws UnsupportedAudioFileException, IOException
/*     */   {
/* 330 */     return getAudioInputStream(new BufferedInputStream(paramURL.openStream()));
/*     */   }
/*     */ 
/*     */   public AudioInputStream getAudioInputStream(File paramFile) throws UnsupportedAudioFileException, IOException
/*     */   {
/* 335 */     return getAudioInputStream(new BufferedInputStream(new FileInputStream(paramFile)));
/*     */   }
/*     */ 
/*     */   private static class GUID
/*     */   {
/*     */     long i1;
/*     */     int s1;
/*     */     int s2;
/*     */     int x1;
/*     */     int x2;
/*     */     int x3;
/*     */     int x4;
/*     */     int x5;
/*     */     int x6;
/*     */     int x7;
/*     */     int x8;
/*     */ 
/*     */     private GUID()
/*     */     {
/*     */     }
/*     */ 
/*     */     GUID(long paramLong, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, int paramInt9, int paramInt10)
/*     */     {
/*  79 */       this.i1 = paramLong;
/*  80 */       this.s1 = paramInt1;
/*  81 */       this.s2 = paramInt2;
/*  82 */       this.x1 = paramInt3;
/*  83 */       this.x2 = paramInt4;
/*  84 */       this.x3 = paramInt5;
/*  85 */       this.x4 = paramInt6;
/*  86 */       this.x5 = paramInt7;
/*  87 */       this.x6 = paramInt8;
/*  88 */       this.x7 = paramInt9;
/*  89 */       this.x8 = paramInt10;
/*     */     }
/*     */ 
/*     */     public static GUID read(RIFFReader paramRIFFReader) throws IOException {
/*  93 */       GUID localGUID = new GUID();
/*  94 */       localGUID.i1 = paramRIFFReader.readUnsignedInt();
/*  95 */       localGUID.s1 = paramRIFFReader.readUnsignedShort();
/*  96 */       localGUID.s2 = paramRIFFReader.readUnsignedShort();
/*  97 */       localGUID.x1 = paramRIFFReader.readUnsignedByte();
/*  98 */       localGUID.x2 = paramRIFFReader.readUnsignedByte();
/*  99 */       localGUID.x3 = paramRIFFReader.readUnsignedByte();
/* 100 */       localGUID.x4 = paramRIFFReader.readUnsignedByte();
/* 101 */       localGUID.x5 = paramRIFFReader.readUnsignedByte();
/* 102 */       localGUID.x6 = paramRIFFReader.readUnsignedByte();
/* 103 */       localGUID.x7 = paramRIFFReader.readUnsignedByte();
/* 104 */       localGUID.x8 = paramRIFFReader.readUnsignedByte();
/* 105 */       return localGUID;
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/* 109 */       return (int)this.i1;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/* 113 */       if (!(paramObject instanceof GUID))
/* 114 */         return false;
/* 115 */       GUID localGUID = (GUID)paramObject;
/* 116 */       if (this.i1 != localGUID.i1)
/* 117 */         return false;
/* 118 */       if (this.s1 != localGUID.s1)
/* 119 */         return false;
/* 120 */       if (this.s2 != localGUID.s2)
/* 121 */         return false;
/* 122 */       if (this.x1 != localGUID.x1)
/* 123 */         return false;
/* 124 */       if (this.x2 != localGUID.x2)
/* 125 */         return false;
/* 126 */       if (this.x3 != localGUID.x3)
/* 127 */         return false;
/* 128 */       if (this.x4 != localGUID.x4)
/* 129 */         return false;
/* 130 */       if (this.x5 != localGUID.x5)
/* 131 */         return false;
/* 132 */       if (this.x6 != localGUID.x6)
/* 133 */         return false;
/* 134 */       if (this.x7 != localGUID.x7)
/* 135 */         return false;
/* 136 */       if (this.x8 != localGUID.x8)
/* 137 */         return false;
/* 138 */       return true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.WaveExtensibleFileReader
 * JD-Core Version:    0.6.2
 */