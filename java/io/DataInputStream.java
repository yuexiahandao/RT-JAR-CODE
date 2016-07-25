/*     */ package java.io;
/*     */ 
/*     */ public class DataInputStream extends FilterInputStream
/*     */   implements DataInput
/*     */ {
/*  58 */   private byte[] bytearr = new byte[80];
/*  59 */   private char[] chararr = new char[80];
/*     */ 
/* 396 */   private byte[] readBuffer = new byte[8];
/*     */   private char[] lineBuffer;
/*     */ 
/*     */   public DataInputStream(InputStream paramInputStream)
/*     */   {
/*  52 */     super(paramInputStream);
/*     */   }
/*     */ 
/*     */   public final int read(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 100 */     return this.in.read(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public final int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 149 */     return this.in.read(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void readFully(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 169 */     readFully(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public final void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 191 */     if (paramInt2 < 0)
/* 192 */       throw new IndexOutOfBoundsException();
/* 193 */     int i = 0;
/* 194 */     while (i < paramInt2) {
/* 195 */       int j = this.in.read(paramArrayOfByte, paramInt1 + i, paramInt2 - i);
/* 196 */       if (j < 0)
/* 197 */         throw new EOFException();
/* 198 */       i += j;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int skipBytes(int paramInt)
/*     */     throws IOException
/*     */   {
/* 217 */     int i = 0;
/* 218 */     int j = 0;
/*     */ 
/* 220 */     while ((i < paramInt) && ((j = (int)this.in.skip(paramInt - i)) > 0)) {
/* 221 */       i += j;
/*     */     }
/*     */ 
/* 224 */     return i;
/*     */   }
/*     */ 
/*     */   public final boolean readBoolean()
/*     */     throws IOException
/*     */   {
/* 242 */     int i = this.in.read();
/* 243 */     if (i < 0)
/* 244 */       throw new EOFException();
/* 245 */     return i != 0;
/*     */   }
/*     */ 
/*     */   public final byte readByte()
/*     */     throws IOException
/*     */   {
/* 265 */     int i = this.in.read();
/* 266 */     if (i < 0)
/* 267 */       throw new EOFException();
/* 268 */     return (byte)i;
/*     */   }
/*     */ 
/*     */   public final int readUnsignedByte()
/*     */     throws IOException
/*     */   {
/* 288 */     int i = this.in.read();
/* 289 */     if (i < 0)
/* 290 */       throw new EOFException();
/* 291 */     return i;
/*     */   }
/*     */ 
/*     */   public final short readShort()
/*     */     throws IOException
/*     */   {
/* 312 */     int i = this.in.read();
/* 313 */     int j = this.in.read();
/* 314 */     if ((i | j) < 0)
/* 315 */       throw new EOFException();
/* 316 */     return (short)((i << 8) + (j << 0));
/*     */   }
/*     */ 
/*     */   public final int readUnsignedShort()
/*     */     throws IOException
/*     */   {
/* 337 */     int i = this.in.read();
/* 338 */     int j = this.in.read();
/* 339 */     if ((i | j) < 0)
/* 340 */       throw new EOFException();
/* 341 */     return (i << 8) + (j << 0);
/*     */   }
/*     */ 
/*     */   public final char readChar()
/*     */     throws IOException
/*     */   {
/* 362 */     int i = this.in.read();
/* 363 */     int j = this.in.read();
/* 364 */     if ((i | j) < 0)
/* 365 */       throw new EOFException();
/* 366 */     return (char)((i << 8) + (j << 0));
/*     */   }
/*     */ 
/*     */   public final int readInt()
/*     */     throws IOException
/*     */   {
/* 387 */     int i = this.in.read();
/* 388 */     int j = this.in.read();
/* 389 */     int k = this.in.read();
/* 390 */     int m = this.in.read();
/* 391 */     if ((i | j | k | m) < 0)
/* 392 */       throw new EOFException();
/* 393 */     return (i << 24) + (j << 16) + (k << 8) + (m << 0);
/*     */   }
/*     */ 
/*     */   public final long readLong()
/*     */     throws IOException
/*     */   {
/* 416 */     readFully(this.readBuffer, 0, 8);
/* 417 */     return (this.readBuffer[0] << 56) + ((this.readBuffer[1] & 0xFF) << 48) + ((this.readBuffer[2] & 0xFF) << 40) + ((this.readBuffer[3] & 0xFF) << 32) + ((this.readBuffer[4] & 0xFF) << 24) + ((this.readBuffer[5] & 0xFF) << 16) + ((this.readBuffer[6] & 0xFF) << 8) + ((this.readBuffer[7] & 0xFF) << 0);
/*     */   }
/*     */ 
/*     */   public final float readFloat()
/*     */     throws IOException
/*     */   {
/* 446 */     return Float.intBitsToFloat(readInt());
/*     */   }
/*     */ 
/*     */   public final double readDouble()
/*     */     throws IOException
/*     */   {
/* 468 */     return Double.longBitsToDouble(readLong());
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final String readLine()
/*     */     throws IOException
/*     */   {
/* 502 */     char[] arrayOfChar = this.lineBuffer;
/*     */ 
/* 504 */     if (arrayOfChar == null) {
/* 505 */       arrayOfChar = this.lineBuffer = new char[''];
/*     */     }
/*     */ 
/* 508 */     int i = arrayOfChar.length;
/* 509 */     int j = 0;
/*     */     int k;
/*     */     while (true) switch (k = this.in.read()) {
/*     */       case -1:
/*     */       case 10:
/* 516 */         break;
/*     */       case 13:
/* 519 */         int m = this.in.read();
/* 520 */         if ((m == 10) || (m == -1)) break;
/* 521 */         if (!(this.in instanceof PushbackInputStream)) {
/* 522 */           this.in = new PushbackInputStream(this.in);
/*     */         }
/* 524 */         ((PushbackInputStream)this.in).unread(m); break;
/*     */       default:
/* 529 */         i--; if (i < 0) {
/* 530 */           arrayOfChar = new char[j + 128];
/* 531 */           i = arrayOfChar.length - j - 1;
/* 532 */           System.arraycopy(this.lineBuffer, 0, arrayOfChar, 0, j);
/* 533 */           this.lineBuffer = arrayOfChar;
/*     */         }
/* 535 */         arrayOfChar[(j++)] = ((char)k);
/*     */       }
/*     */ 
/*     */ 
/* 539 */     if ((k == -1) && (j == 0)) {
/* 540 */       return null;
/*     */     }
/* 542 */     return String.copyValueOf(arrayOfChar, 0, j);
/*     */   }
/*     */ 
/*     */   public final String readUTF()
/*     */     throws IOException
/*     */   {
/* 564 */     return readUTF(this);
/*     */   }
/*     */ 
/*     */   public static final String readUTF(DataInput paramDataInput)
/*     */     throws IOException
/*     */   {
/* 589 */     int i = paramDataInput.readUnsignedShort();
/* 590 */     byte[] arrayOfByte = null;
/* 591 */     char[] arrayOfChar = null;
/* 592 */     if ((paramDataInput instanceof DataInputStream)) {
/* 593 */       DataInputStream localDataInputStream = (DataInputStream)paramDataInput;
/* 594 */       if (localDataInputStream.bytearr.length < i) {
/* 595 */         localDataInputStream.bytearr = new byte[i * 2];
/* 596 */         localDataInputStream.chararr = new char[i * 2];
/*     */       }
/* 598 */       arrayOfChar = localDataInputStream.chararr;
/* 599 */       arrayOfByte = localDataInputStream.bytearr;
/*     */     } else {
/* 601 */       arrayOfByte = new byte[i];
/* 602 */       arrayOfChar = new char[i];
/*     */     }
/*     */ 
/* 606 */     int n = 0;
/* 607 */     int i1 = 0;
/*     */ 
/* 609 */     paramDataInput.readFully(arrayOfByte, 0, i);
/*     */     int j;
/* 611 */     while (n < i) {
/* 612 */       j = arrayOfByte[n] & 0xFF;
/* 613 */       if (j > 127) break;
/* 614 */       n++;
/* 615 */       arrayOfChar[(i1++)] = ((char)j);
/*     */     }
/*     */ 
/* 618 */     while (n < i) {
/* 619 */       j = arrayOfByte[n] & 0xFF;
/*     */       int k;
/* 620 */       switch (j >> 4) { case 0:
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/* 623 */         n++;
/* 624 */         arrayOfChar[(i1++)] = ((char)j);
/* 625 */         break;
/*     */       case 12:
/*     */       case 13:
/* 628 */         n += 2;
/* 629 */         if (n > i) {
/* 630 */           throw new UTFDataFormatException("malformed input: partial character at end");
/*     */         }
/* 632 */         k = arrayOfByte[(n - 1)];
/* 633 */         if ((k & 0xC0) != 128) {
/* 634 */           throw new UTFDataFormatException("malformed input around byte " + n);
/*     */         }
/* 636 */         arrayOfChar[(i1++)] = ((char)((j & 0x1F) << 6 | k & 0x3F));
/*     */ 
/* 638 */         break;
/*     */       case 14:
/* 641 */         n += 3;
/* 642 */         if (n > i) {
/* 643 */           throw new UTFDataFormatException("malformed input: partial character at end");
/*     */         }
/* 645 */         k = arrayOfByte[(n - 2)];
/* 646 */         int m = arrayOfByte[(n - 1)];
/* 647 */         if (((k & 0xC0) != 128) || ((m & 0xC0) != 128)) {
/* 648 */           throw new UTFDataFormatException("malformed input around byte " + (n - 1));
/*     */         }
/* 650 */         arrayOfChar[(i1++)] = ((char)((j & 0xF) << 12 | (k & 0x3F) << 6 | (m & 0x3F) << 0));
/*     */ 
/* 653 */         break;
/*     */       case 8:
/*     */       case 9:
/*     */       case 10:
/*     */       case 11:
/*     */       default:
/* 656 */         throw new UTFDataFormatException("malformed input around byte " + n);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 661 */     return new String(arrayOfChar, 0, i1);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.DataInputStream
 * JD-Core Version:    0.6.2
 */