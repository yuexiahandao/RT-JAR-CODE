/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import javax.sound.midi.InvalidMidiDataException;
/*     */ import javax.sound.midi.MetaMessage;
/*     */ import javax.sound.midi.MidiEvent;
/*     */ import javax.sound.midi.MidiMessage;
/*     */ import javax.sound.midi.SysexMessage;
/*     */ import javax.sound.midi.Track;
/*     */ 
/*     */ final class SMFParser
/*     */ {
/*     */   private static final int MTrk_MAGIC = 1297379947;
/*     */   private static final boolean STRICT_PARSER = false;
/*     */   private static final boolean DEBUG = false;
/*     */   int tracks;
/*     */   DataInputStream stream;
/* 246 */   private int trackLength = 0;
/* 247 */   private byte[] trackData = null;
/* 248 */   private int pos = 0;
/*     */ 
/*     */   private int readUnsigned()
/*     */     throws IOException
/*     */   {
/* 254 */     return this.trackData[(this.pos++)] & 0xFF;
/*     */   }
/*     */ 
/*     */   private void read(byte[] paramArrayOfByte) throws IOException {
/* 258 */     System.arraycopy(this.trackData, this.pos, paramArrayOfByte, 0, paramArrayOfByte.length);
/* 259 */     this.pos += paramArrayOfByte.length;
/*     */   }
/*     */ 
/*     */   private long readVarInt() throws IOException {
/* 263 */     long l = 0L;
/* 264 */     int i = 0;
/*     */     do {
/* 266 */       i = this.trackData[(this.pos++)] & 0xFF;
/* 267 */       l = (l << 7) + (i & 0x7F);
/* 268 */     }while ((i & 0x80) != 0);
/* 269 */     return l;
/*     */   }
/*     */ 
/*     */   private int readIntFromStream() throws IOException {
/*     */     try {
/* 274 */       return this.stream.readInt(); } catch (EOFException localEOFException) {
/*     */     }
/* 276 */     throw new EOFException("invalid MIDI file");
/*     */   }
/*     */ 
/*     */   boolean nextTrack() throws IOException, InvalidMidiDataException
/*     */   {
/* 282 */     this.trackLength = 0;
/*     */     int i;
/*     */     do {
/* 285 */       if (this.stream.skipBytes(this.trackLength) != this.trackLength)
/*     */       {
/* 287 */         return false;
/*     */       }
/*     */ 
/* 291 */       i = readIntFromStream();
/* 292 */       this.trackLength = readIntFromStream();
/* 293 */     }while (i != 1297379947);
/*     */ 
/* 295 */     if (this.trackLength < 0) {
/* 296 */       return false;
/*     */     }
/*     */ 
/* 300 */     this.trackData = new byte[this.trackLength];
/*     */     try
/*     */     {
/* 303 */       this.stream.readFully(this.trackData);
/*     */     }
/*     */     catch (EOFException localEOFException) {
/* 306 */       return false;
/*     */     }
/*     */ 
/* 310 */     this.pos = 0;
/* 311 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean trackFinished() {
/* 315 */     return this.pos >= this.trackLength;
/*     */   }
/*     */ 
/*     */   void readTrack(Track paramTrack) throws IOException, InvalidMidiDataException
/*     */   {
/*     */     try {
/* 321 */       long l = 0L;
/*     */ 
/* 326 */       int i = 0;
/* 327 */       int j = 0;
/*     */ 
/* 329 */       while ((!trackFinished()) && (j == 0))
/*     */       {
/* 332 */         int k = -1;
/* 333 */         int m = 0;
/*     */ 
/* 338 */         l += readVarInt();
/*     */ 
/* 341 */         int n = readUnsigned();
/*     */ 
/* 343 */         if (n >= 128)
/* 344 */           i = n;
/*     */         else
/* 346 */           k = n;
/*     */         Object localObject;
/* 349 */         switch (i & 0xF0)
/*     */         {
/*     */         case 128:
/*     */         case 144:
/*     */         case 160:
/*     */         case 176:
/*     */         case 224:
/* 356 */           if (k == -1) {
/* 357 */             k = readUnsigned();
/*     */           }
/* 359 */           m = readUnsigned();
/* 360 */           localObject = new FastShortMessage(i | k << 8 | m << 16);
/* 361 */           break;
/*     */         case 192:
/*     */         case 208:
/* 365 */           if (k == -1) {
/* 366 */             k = readUnsigned();
/*     */           }
/* 368 */           localObject = new FastShortMessage(i | k << 8);
/* 369 */           break;
/*     */         case 240:
/* 372 */           switch (i)
/*     */           {
/*     */           case 240:
/*     */           case 247:
/* 376 */             int i1 = (int)readVarInt();
/* 377 */             byte[] arrayOfByte1 = new byte[i1];
/* 378 */             read(arrayOfByte1);
/*     */ 
/* 380 */             SysexMessage localSysexMessage = new SysexMessage();
/* 381 */             localSysexMessage.setMessage(i, arrayOfByte1, i1);
/* 382 */             localObject = localSysexMessage;
/* 383 */             break;
/*     */           case 255:
/* 387 */             int i2 = readUnsigned();
/* 388 */             int i3 = (int)readVarInt();
/*     */ 
/* 390 */             byte[] arrayOfByte2 = new byte[i3];
/* 391 */             read(arrayOfByte2);
/*     */ 
/* 393 */             MetaMessage localMetaMessage = new MetaMessage();
/* 394 */             localMetaMessage.setMessage(i2, arrayOfByte2, i3);
/* 395 */             localObject = localMetaMessage;
/* 396 */             if (i2 == 47)
/*     */             {
/* 398 */               j = 1; } break;
/*     */           default:
/* 402 */             throw new InvalidMidiDataException("Invalid status byte: " + i);
/*     */           }
/* 404 */           break;
/*     */         default:
/* 406 */           throw new InvalidMidiDataException("Invalid status byte: " + i);
/*     */         }
/* 408 */         paramTrack.add(new MidiEvent((MidiMessage)localObject, l));
/*     */       }
/*     */     }
/*     */     catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
/*     */     {
/* 413 */       throw new EOFException("invalid MIDI file");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SMFParser
 * JD-Core Version:    0.6.2
 */