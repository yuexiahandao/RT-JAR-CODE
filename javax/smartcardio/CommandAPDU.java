/*     */ package javax.smartcardio;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public final class CommandAPDU
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 398698301286670877L;
/*     */   private static final int MAX_APDU_SIZE = 65544;
/*     */   private byte[] apdu;
/*     */   private transient int nc;
/*     */   private transient int ne;
/*     */   private transient int dataOffset;
/*     */ 
/*     */   public CommandAPDU(byte[] paramArrayOfByte)
/*     */   {
/*  97 */     this.apdu = ((byte[])paramArrayOfByte.clone());
/*  98 */     parse();
/*     */   }
/*     */ 
/*     */   public CommandAPDU(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 121 */     checkArrayBounds(paramArrayOfByte, paramInt1, paramInt2);
/* 122 */     this.apdu = new byte[paramInt2];
/* 123 */     System.arraycopy(paramArrayOfByte, paramInt1, this.apdu, 0, paramInt2);
/* 124 */     parse();
/*     */   }
/*     */ 
/*     */   private void checkArrayBounds(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 128 */     if ((paramInt1 < 0) || (paramInt2 < 0)) {
/* 129 */       throw new IllegalArgumentException("Offset and length must not be negative");
/*     */     }
/*     */ 
/* 132 */     if (paramArrayOfByte == null) {
/* 133 */       if ((paramInt1 != 0) && (paramInt2 != 0)) {
/* 134 */         throw new IllegalArgumentException("offset and length must be 0 if array is null");
/*     */       }
/*     */ 
/*     */     }
/* 138 */     else if (paramInt1 > paramArrayOfByte.length - paramInt2)
/* 139 */       throw new IllegalArgumentException("Offset plus length exceed array size");
/*     */   }
/*     */ 
/*     */   public CommandAPDU(ByteBuffer paramByteBuffer)
/*     */   {
/* 162 */     this.apdu = new byte[paramByteBuffer.remaining()];
/* 163 */     paramByteBuffer.get(this.apdu);
/* 164 */     parse();
/*     */   }
/*     */ 
/*     */   public CommandAPDU(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 177 */     this(paramInt1, paramInt2, paramInt3, paramInt4, null, 0, 0, 0);
/*     */   }
/*     */ 
/*     */   public CommandAPDU(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5)
/*     */   {
/* 196 */     this(paramInt1, paramInt2, paramInt3, paramInt4, null, 0, 0, paramInt5);
/*     */   }
/*     */ 
/*     */   public CommandAPDU(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte)
/*     */   {
/* 217 */     this(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, 0, arrayLength(paramArrayOfByte), 0);
/*     */   }
/*     */ 
/*     */   public CommandAPDU(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/* 245 */     this(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, paramInt5, paramInt6, 0);
/*     */   }
/*     */ 
/*     */   public CommandAPDU(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5)
/*     */   {
/* 269 */     this(paramInt1, paramInt2, paramInt3, paramInt4, paramArrayOfByte, 0, arrayLength(paramArrayOfByte), paramInt5);
/*     */   }
/*     */ 
/*     */   private static int arrayLength(byte[] paramArrayOfByte) {
/* 273 */     return paramArrayOfByte != null ? paramArrayOfByte.length : 0;
/*     */   }
/*     */ 
/*     */   private void parse()
/*     */   {
/* 291 */     if (this.apdu.length < 4) {
/* 292 */       throw new IllegalArgumentException("apdu must be at least 4 bytes long");
/*     */     }
/* 294 */     if (this.apdu.length == 4)
/*     */     {
/* 296 */       return;
/*     */     }
/* 298 */     int i = this.apdu[4] & 0xFF;
/* 299 */     if (this.apdu.length == 5)
/*     */     {
/* 301 */       this.ne = (i == 0 ? 256 : i);
/* 302 */       return;
/*     */     }
/* 304 */     if (i != 0) {
/* 305 */       if (this.apdu.length == 5 + i)
/*     */       {
/* 307 */         this.nc = i;
/* 308 */         this.dataOffset = 5;
/* 309 */         return;
/* 310 */       }if (this.apdu.length == 6 + i)
/*     */       {
/* 312 */         this.nc = i;
/* 313 */         this.dataOffset = 5;
/* 314 */         j = this.apdu[(this.apdu.length - 1)] & 0xFF;
/* 315 */         this.ne = (j == 0 ? 256 : j);
/* 316 */         return;
/*     */       }
/* 318 */       throw new IllegalArgumentException("Invalid APDU: length=" + this.apdu.length + ", b1=" + i);
/*     */     }
/*     */ 
/* 322 */     if (this.apdu.length < 7) {
/* 323 */       throw new IllegalArgumentException("Invalid APDU: length=" + this.apdu.length + ", b1=" + i);
/*     */     }
/*     */ 
/* 326 */     int j = (this.apdu[5] & 0xFF) << 8 | this.apdu[6] & 0xFF;
/* 327 */     if (this.apdu.length == 7)
/*     */     {
/* 329 */       this.ne = (j == 0 ? 65536 : j);
/* 330 */       return;
/*     */     }
/* 332 */     if (j == 0) {
/* 333 */       throw new IllegalArgumentException("Invalid APDU: length=" + this.apdu.length + ", b1=" + i + ", b2||b3=" + j);
/*     */     }
/*     */ 
/* 336 */     if (this.apdu.length == 7 + j)
/*     */     {
/* 338 */       this.nc = j;
/* 339 */       this.dataOffset = 7;
/* 340 */       return;
/* 341 */     }if (this.apdu.length == 9 + j)
/*     */     {
/* 343 */       this.nc = j;
/* 344 */       this.dataOffset = 7;
/* 345 */       int k = this.apdu.length - 2;
/* 346 */       int m = (this.apdu[k] & 0xFF) << 8 | this.apdu[(k + 1)] & 0xFF;
/* 347 */       this.ne = (m == 0 ? 65536 : m);
/*     */     } else {
/* 349 */       throw new IllegalArgumentException("Invalid APDU: length=" + this.apdu.length + ", b1=" + i + ", b2||b3=" + j);
/*     */     }
/*     */   }
/*     */ 
/*     */   public CommandAPDU(int paramInt1, int paramInt2, int paramInt3, int paramInt4, byte[] paramArrayOfByte, int paramInt5, int paramInt6, int paramInt7)
/*     */   {
/* 383 */     checkArrayBounds(paramArrayOfByte, paramInt5, paramInt6);
/* 384 */     if (paramInt6 > 65535) {
/* 385 */       throw new IllegalArgumentException("dataLength is too large");
/*     */     }
/* 387 */     if (paramInt7 < 0) {
/* 388 */       throw new IllegalArgumentException("ne must not be negative");
/*     */     }
/* 390 */     if (paramInt7 > 65536) {
/* 391 */       throw new IllegalArgumentException("ne is too large");
/*     */     }
/* 393 */     this.ne = paramInt7;
/* 394 */     this.nc = paramInt6;
/*     */     int i;
/* 395 */     if (paramInt6 == 0) {
/* 396 */       if (paramInt7 == 0)
/*     */       {
/* 398 */         this.apdu = new byte[4];
/* 399 */         setHeader(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */       }
/* 402 */       else if (paramInt7 <= 256)
/*     */       {
/* 405 */         i = paramInt7 != 256 ? (byte)paramInt7 : 0;
/* 406 */         this.apdu = new byte[5];
/* 407 */         setHeader(paramInt1, paramInt2, paramInt3, paramInt4);
/* 408 */         this.apdu[4] = i;
/*     */       }
/*     */       else
/*     */       {
/*     */         int j;
/* 413 */         if (paramInt7 == 65536) {
/* 414 */           i = 0;
/* 415 */           j = 0;
/*     */         } else {
/* 417 */           i = (byte)(paramInt7 >> 8);
/* 418 */           j = (byte)paramInt7;
/*     */         }
/* 420 */         this.apdu = new byte[7];
/* 421 */         setHeader(paramInt1, paramInt2, paramInt3, paramInt4);
/* 422 */         this.apdu[5] = i;
/* 423 */         this.apdu[6] = j;
/*     */       }
/*     */ 
/*     */     }
/* 427 */     else if (paramInt7 == 0)
/*     */     {
/* 429 */       if (paramInt6 <= 255)
/*     */       {
/* 431 */         this.apdu = new byte[5 + paramInt6];
/* 432 */         setHeader(paramInt1, paramInt2, paramInt3, paramInt4);
/* 433 */         this.apdu[4] = ((byte)paramInt6);
/* 434 */         this.dataOffset = 5;
/* 435 */         System.arraycopy(paramArrayOfByte, paramInt5, this.apdu, 5, paramInt6);
/*     */       }
/*     */       else {
/* 438 */         this.apdu = new byte[7 + paramInt6];
/* 439 */         setHeader(paramInt1, paramInt2, paramInt3, paramInt4);
/* 440 */         this.apdu[4] = 0;
/* 441 */         this.apdu[5] = ((byte)(paramInt6 >> 8));
/* 442 */         this.apdu[6] = ((byte)paramInt6);
/* 443 */         this.dataOffset = 7;
/* 444 */         System.arraycopy(paramArrayOfByte, paramInt5, this.apdu, 7, paramInt6);
/*     */       }
/*     */ 
/*     */     }
/* 448 */     else if ((paramInt6 <= 255) && (paramInt7 <= 256))
/*     */     {
/* 450 */       this.apdu = new byte[6 + paramInt6];
/* 451 */       setHeader(paramInt1, paramInt2, paramInt3, paramInt4);
/* 452 */       this.apdu[4] = ((byte)paramInt6);
/* 453 */       this.dataOffset = 5;
/* 454 */       System.arraycopy(paramArrayOfByte, paramInt5, this.apdu, 5, paramInt6);
/* 455 */       this.apdu[(this.apdu.length - 1)] = (paramInt7 != 256 ? (byte)paramInt7 : 0);
/*     */     }
/*     */     else {
/* 458 */       this.apdu = new byte[9 + paramInt6];
/* 459 */       setHeader(paramInt1, paramInt2, paramInt3, paramInt4);
/* 460 */       this.apdu[4] = 0;
/* 461 */       this.apdu[5] = ((byte)(paramInt6 >> 8));
/* 462 */       this.apdu[6] = ((byte)paramInt6);
/* 463 */       this.dataOffset = 7;
/* 464 */       System.arraycopy(paramArrayOfByte, paramInt5, this.apdu, 7, paramInt6);
/* 465 */       if (paramInt7 != 65536) {
/* 466 */         i = this.apdu.length - 2;
/* 467 */         this.apdu[i] = ((byte)(paramInt7 >> 8));
/* 468 */         this.apdu[(i + 1)] = ((byte)paramInt7);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setHeader(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 476 */     this.apdu[0] = ((byte)paramInt1);
/* 477 */     this.apdu[1] = ((byte)paramInt2);
/* 478 */     this.apdu[2] = ((byte)paramInt3);
/* 479 */     this.apdu[3] = ((byte)paramInt4);
/*     */   }
/*     */ 
/*     */   public int getCLA()
/*     */   {
/* 488 */     return this.apdu[0] & 0xFF;
/*     */   }
/*     */ 
/*     */   public int getINS()
/*     */   {
/* 497 */     return this.apdu[1] & 0xFF;
/*     */   }
/*     */ 
/*     */   public int getP1()
/*     */   {
/* 506 */     return this.apdu[2] & 0xFF;
/*     */   }
/*     */ 
/*     */   public int getP2()
/*     */   {
/* 515 */     return this.apdu[3] & 0xFF;
/*     */   }
/*     */ 
/*     */   public int getNc()
/*     */   {
/* 527 */     return this.nc;
/*     */   }
/*     */ 
/*     */   public byte[] getData()
/*     */   {
/* 538 */     byte[] arrayOfByte = new byte[this.nc];
/* 539 */     System.arraycopy(this.apdu, this.dataOffset, arrayOfByte, 0, this.nc);
/* 540 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public int getNe()
/*     */   {
/* 550 */     return this.ne;
/*     */   }
/*     */ 
/*     */   public byte[] getBytes()
/*     */   {
/* 559 */     return (byte[])this.apdu.clone();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 568 */     return "CommmandAPDU: " + this.apdu.length + " bytes, nc=" + this.nc + ", ne=" + this.ne;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 580 */     if (this == paramObject) {
/* 581 */       return true;
/*     */     }
/* 583 */     if (!(paramObject instanceof CommandAPDU)) {
/* 584 */       return false;
/*     */     }
/* 586 */     CommandAPDU localCommandAPDU = (CommandAPDU)paramObject;
/* 587 */     return Arrays.equals(this.apdu, localCommandAPDU.apdu);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 596 */     return Arrays.hashCode(this.apdu);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/* 601 */     this.apdu = ((byte[])paramObjectInputStream.readUnshared());
/*     */ 
/* 603 */     parse();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.smartcardio.CommandAPDU
 * JD-Core Version:    0.6.2
 */