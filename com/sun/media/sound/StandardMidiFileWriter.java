/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PipedInputStream;
/*     */ import java.io.PipedOutputStream;
/*     */ import java.io.SequenceInputStream;
/*     */ import javax.sound.midi.InvalidMidiDataException;
/*     */ import javax.sound.midi.MetaMessage;
/*     */ import javax.sound.midi.MidiEvent;
/*     */ import javax.sound.midi.MidiMessage;
/*     */ import javax.sound.midi.Sequence;
/*     */ import javax.sound.midi.ShortMessage;
/*     */ import javax.sound.midi.SysexMessage;
/*     */ import javax.sound.midi.Track;
/*     */ import javax.sound.midi.spi.MidiFileWriter;
/*     */ 
/*     */ public final class StandardMidiFileWriter extends MidiFileWriter
/*     */ {
/*     */   private static final int MThd_MAGIC = 1297377380;
/*     */   private static final int MTrk_MAGIC = 1297379947;
/*     */   private static final int ONE_BYTE = 1;
/*     */   private static final int TWO_BYTE = 2;
/*     */   private static final int SYSEX = 3;
/*     */   private static final int META = 4;
/*     */   private static final int ERROR = 5;
/*     */   private static final int IGNORE = 6;
/*     */   private static final int MIDI_TYPE_0 = 0;
/*     */   private static final int MIDI_TYPE_1 = 1;
/*     */   private static final int bufferSize = 16384;
/*     */   private DataOutputStream tddos;
/*  79 */   private static final int[] types = { 0, 1 };
/*     */   private static final long mask = 127L;
/*     */ 
/*     */   public int[] getMidiFileTypes()
/*     */   {
/*  89 */     int[] arrayOfInt = new int[types.length];
/*  90 */     System.arraycopy(types, 0, arrayOfInt, 0, types.length);
/*  91 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public int[] getMidiFileTypes(Sequence paramSequence)
/*     */   {
/* 104 */     Track[] arrayOfTrack = paramSequence.getTracks();
/*     */     int[] arrayOfInt;
/* 106 */     if (arrayOfTrack.length == 1) {
/* 107 */       arrayOfInt = new int[2];
/* 108 */       arrayOfInt[0] = 0;
/* 109 */       arrayOfInt[1] = 1;
/*     */     } else {
/* 111 */       arrayOfInt = new int[1];
/* 112 */       arrayOfInt[0] = 1;
/*     */     }
/*     */ 
/* 115 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public boolean isFileTypeSupported(int paramInt) {
/* 119 */     for (int i = 0; i < types.length; i++) {
/* 120 */       if (paramInt == types[i]) {
/* 121 */         return true;
/*     */       }
/*     */     }
/* 124 */     return false;
/*     */   }
/*     */ 
/*     */   public int write(Sequence paramSequence, int paramInt, OutputStream paramOutputStream) throws IOException {
/* 128 */     byte[] arrayOfByte = null;
/*     */ 
/* 130 */     int i = 0;
/* 131 */     long l = 0L;
/*     */ 
/* 133 */     if (!isFileTypeSupported(paramInt, paramSequence)) {
/* 134 */       throw new IllegalArgumentException("Could not write MIDI file");
/*     */     }
/*     */ 
/* 137 */     InputStream localInputStream = getFileStream(paramInt, paramSequence);
/* 138 */     if (localInputStream == null) {
/* 139 */       throw new IllegalArgumentException("Could not write MIDI file");
/*     */     }
/* 141 */     arrayOfByte = new byte[16384];
/*     */ 
/* 143 */     while ((i = localInputStream.read(arrayOfByte)) >= 0) {
/* 144 */       paramOutputStream.write(arrayOfByte, 0, i);
/* 145 */       l += i;
/*     */     }
/*     */ 
/* 148 */     return (int)l;
/*     */   }
/*     */ 
/*     */   public int write(Sequence paramSequence, int paramInt, File paramFile) throws IOException {
/* 152 */     FileOutputStream localFileOutputStream = new FileOutputStream(paramFile);
/* 153 */     int i = write(paramSequence, paramInt, localFileOutputStream);
/* 154 */     localFileOutputStream.close();
/* 155 */     return i;
/*     */   }
/*     */ 
/*     */   private InputStream getFileStream(int paramInt, Sequence paramSequence)
/*     */     throws IOException
/*     */   {
/* 162 */     Track[] arrayOfTrack = paramSequence.getTracks();
/* 163 */     int i = 0;
/* 164 */     int j = 14;
/* 165 */     int k = 0;
/*     */ 
/* 169 */     PipedOutputStream localPipedOutputStream = null;
/* 170 */     DataOutputStream localDataOutputStream = null;
/* 171 */     PipedInputStream localPipedInputStream = null;
/*     */ 
/* 173 */     InputStream[] arrayOfInputStream = null;
/* 174 */     Object localObject = null;
/* 175 */     SequenceInputStream localSequenceInputStream = null;
/*     */ 
/* 178 */     if (paramInt == 0) {
/* 179 */       if (arrayOfTrack.length != 1)
/* 180 */         return null;
/*     */     }
/* 182 */     else if (paramInt == 1) {
/* 183 */       if (arrayOfTrack.length < 1) {
/* 184 */         return null;
/*     */       }
/*     */     }
/* 187 */     else if (arrayOfTrack.length == 1)
/* 188 */       paramInt = 0;
/* 189 */     else if (arrayOfTrack.length > 1)
/* 190 */       paramInt = 1;
/*     */     else {
/* 192 */       return null;
/*     */     }
/*     */ 
/* 200 */     arrayOfInputStream = new InputStream[arrayOfTrack.length];
/* 201 */     int n = 0;
/* 202 */     for (int i1 = 0; i1 < arrayOfTrack.length; i1++) {
/*     */       try {
/* 204 */         arrayOfInputStream[n] = writeTrack(arrayOfTrack[i1], paramInt);
/* 205 */         n++;
/*     */       }
/*     */       catch (InvalidMidiDataException localInvalidMidiDataException)
/*     */       {
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 213 */     if (n == 1) {
/* 214 */       localObject = arrayOfInputStream[0];
/* 215 */     } else if (n > 1) {
/* 216 */       localObject = arrayOfInputStream[0];
/* 217 */       for (i1 = 1; i1 < arrayOfTrack.length; i1++)
/*     */       {
/* 220 */         if (arrayOfInputStream[i1] != null)
/* 221 */           localObject = new SequenceInputStream((InputStream)localObject, arrayOfInputStream[i1]);
/*     */       }
/*     */     }
/*     */     else {
/* 225 */       throw new IllegalArgumentException("invalid MIDI data in sequence");
/*     */     }
/*     */ 
/* 229 */     localPipedOutputStream = new PipedOutputStream();
/* 230 */     localDataOutputStream = new DataOutputStream(localPipedOutputStream);
/* 231 */     localPipedInputStream = new PipedInputStream(localPipedOutputStream);
/*     */ 
/* 234 */     localDataOutputStream.writeInt(1297377380);
/*     */ 
/* 237 */     localDataOutputStream.writeInt(j - 8);
/*     */ 
/* 240 */     if (paramInt == 0) {
/* 241 */       localDataOutputStream.writeShort(0);
/*     */     }
/*     */     else {
/* 244 */       localDataOutputStream.writeShort(1);
/*     */     }
/*     */ 
/* 248 */     localDataOutputStream.writeShort((short)n);
/*     */ 
/* 251 */     float f = paramSequence.getDivisionType();
/*     */     int m;
/* 252 */     if (f == 0.0F) {
/* 253 */       m = paramSequence.getResolution();
/* 254 */     } else if (f == 24.0F) {
/* 255 */       m = -6144;
/* 256 */       m += (paramSequence.getResolution() & 0xFF);
/* 257 */     } else if (f == 25.0F) {
/* 258 */       m = -6400;
/* 259 */       m += (paramSequence.getResolution() & 0xFF);
/* 260 */     } else if (f == 29.969999F) {
/* 261 */       m = -7424;
/* 262 */       m += (paramSequence.getResolution() & 0xFF);
/* 263 */     } else if (f == 30.0F) {
/* 264 */       m = -7680;
/* 265 */       m += (paramSequence.getResolution() & 0xFF);
/*     */     }
/*     */     else {
/* 268 */       return null;
/*     */     }
/* 270 */     localDataOutputStream.writeShort(m);
/*     */ 
/* 273 */     localSequenceInputStream = new SequenceInputStream(localPipedInputStream, (InputStream)localObject);
/* 274 */     localDataOutputStream.close();
/*     */ 
/* 276 */     k = i + j;
/* 277 */     return localSequenceInputStream;
/*     */   }
/*     */ 
/*     */   private int getType(int paramInt)
/*     */   {
/* 285 */     if ((paramInt & 0xF0) == 240) {
/* 286 */       switch (paramInt) {
/*     */       case 240:
/*     */       case 247:
/* 289 */         return 3;
/*     */       case 255:
/* 291 */         return 4;
/*     */       }
/* 293 */       return 6;
/*     */     }
/*     */ 
/* 296 */     switch (paramInt & 0xF0) {
/*     */     case 128:
/*     */     case 144:
/*     */     case 160:
/*     */     case 176:
/*     */     case 224:
/* 302 */       return 2;
/*     */     case 192:
/*     */     case 208:
/* 305 */       return 1;
/*     */     }
/* 307 */     return 5;
/*     */   }
/*     */ 
/*     */   private int writeVarInt(long paramLong)
/*     */     throws IOException
/*     */   {
/* 313 */     int i = 1;
/* 314 */     int j = 63;
/*     */ 
/* 316 */     while ((j > 0) && ((paramLong & 127L << j) == 0L)) j -= 7;
/*     */ 
/* 318 */     while (j > 0) {
/* 319 */       this.tddos.writeByte((int)((paramLong & 127L << j) >> j | 0x80));
/* 320 */       j -= 7;
/* 321 */       i++;
/*     */     }
/* 323 */     this.tddos.writeByte((int)(paramLong & 0x7F));
/* 324 */     return i;
/*     */   }
/*     */ 
/*     */   private InputStream writeTrack(Track paramTrack, int paramInt) throws IOException, InvalidMidiDataException {
/* 328 */     int i = 0;
/* 329 */     int j = 0;
/* 330 */     int k = paramTrack.size();
/* 331 */     PipedOutputStream localPipedOutputStream = new PipedOutputStream();
/* 332 */     DataOutputStream localDataOutputStream = new DataOutputStream(localPipedOutputStream);
/* 333 */     PipedInputStream localPipedInputStream = new PipedInputStream(localPipedOutputStream);
/*     */ 
/* 335 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 336 */     this.tddos = new DataOutputStream(localByteArrayOutputStream);
/* 337 */     ByteArrayInputStream localByteArrayInputStream = null;
/*     */ 
/* 339 */     SequenceInputStream localSequenceInputStream = null;
/*     */ 
/* 341 */     long l1 = 0L;
/* 342 */     long l2 = 0L;
/* 343 */     long l3 = 0L;
/* 344 */     int m = -1;
/*     */ 
/* 349 */     for (int n = 0; n < k; n++) {
/* 350 */       MidiEvent localMidiEvent = paramTrack.get(n);
/*     */ 
/* 357 */       byte[] arrayOfByte = null;
/* 358 */       ShortMessage localShortMessage = null;
/* 359 */       MetaMessage localMetaMessage = null;
/* 360 */       SysexMessage localSysexMessage = null;
/*     */ 
/* 364 */       l3 = localMidiEvent.getTick();
/* 365 */       l2 = localMidiEvent.getTick() - l1;
/* 366 */       l1 = localMidiEvent.getTick();
/*     */ 
/* 369 */       int i1 = localMidiEvent.getMessage().getStatus();
/* 370 */       int i2 = getType(i1);
/*     */       int i3;
/*     */       int i5;
/* 372 */       switch (i2) {
/*     */       case 1:
/* 374 */         localShortMessage = (ShortMessage)localMidiEvent.getMessage();
/* 375 */         i3 = localShortMessage.getData1();
/* 376 */         i += writeVarInt(l2);
/*     */ 
/* 378 */         if (i1 != m) {
/* 379 */           m = i1;
/* 380 */           this.tddos.writeByte(i1); i++;
/*     */         }
/* 382 */         this.tddos.writeByte(i3); i++;
/* 383 */         break;
/*     */       case 2:
/* 386 */         localShortMessage = (ShortMessage)localMidiEvent.getMessage();
/* 387 */         i3 = localShortMessage.getData1();
/* 388 */         int i4 = localShortMessage.getData2();
/*     */ 
/* 390 */         i += writeVarInt(l2);
/* 391 */         if (i1 != m) {
/* 392 */           m = i1;
/* 393 */           this.tddos.writeByte(i1); i++;
/*     */         }
/* 395 */         this.tddos.writeByte(i3); i++;
/* 396 */         this.tddos.writeByte(i4); i++;
/* 397 */         break;
/*     */       case 3:
/* 400 */         localSysexMessage = (SysexMessage)localMidiEvent.getMessage();
/* 401 */         i5 = localSysexMessage.getLength();
/* 402 */         arrayOfByte = localSysexMessage.getMessage();
/* 403 */         i += writeVarInt(l2);
/*     */ 
/* 406 */         m = i1;
/* 407 */         this.tddos.writeByte(arrayOfByte[0]); i++;
/*     */ 
/* 413 */         i += writeVarInt(arrayOfByte.length - 1);
/*     */ 
/* 417 */         this.tddos.write(arrayOfByte, 1, arrayOfByte.length - 1);
/* 418 */         i += arrayOfByte.length - 1;
/* 419 */         break;
/*     */       case 4:
/* 422 */         localMetaMessage = (MetaMessage)localMidiEvent.getMessage();
/* 423 */         i5 = localMetaMessage.getLength();
/* 424 */         arrayOfByte = localMetaMessage.getMessage();
/* 425 */         i += writeVarInt(l2);
/*     */ 
/* 433 */         m = i1;
/* 434 */         this.tddos.write(arrayOfByte, 0, arrayOfByte.length);
/* 435 */         i += arrayOfByte.length;
/* 436 */         break;
/*     */       case 6:
/* 440 */         break;
/*     */       case 5:
/* 444 */         break;
/*     */       default:
/* 447 */         throw new InvalidMidiDataException("internal file writer error");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 455 */     localDataOutputStream.writeInt(1297379947);
/* 456 */     localDataOutputStream.writeInt(i);
/* 457 */     i += 8;
/*     */ 
/* 460 */     localByteArrayInputStream = new ByteArrayInputStream(localByteArrayOutputStream.toByteArray());
/* 461 */     localSequenceInputStream = new SequenceInputStream(localPipedInputStream, localByteArrayInputStream);
/* 462 */     localDataOutputStream.close();
/* 463 */     this.tddos.close();
/*     */ 
/* 465 */     return localSequenceInputStream;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.StandardMidiFileWriter
 * JD-Core Version:    0.6.2
 */