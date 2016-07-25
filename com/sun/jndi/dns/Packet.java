/*     */ package com.sun.jndi.dns;
/*     */ 
/*     */ class Packet
/*     */ {
/*     */   byte[] buf;
/*     */ 
/*     */   Packet(int paramInt)
/*     */   {
/* 701 */     this.buf = new byte[paramInt];
/*     */   }
/*     */ 
/*     */   Packet(byte[] paramArrayOfByte, int paramInt) {
/* 705 */     this.buf = new byte[paramInt];
/* 706 */     System.arraycopy(paramArrayOfByte, 0, this.buf, 0, paramInt);
/*     */   }
/*     */ 
/*     */   void putInt(int paramInt1, int paramInt2) {
/* 710 */     this.buf[(paramInt2 + 0)] = ((byte)(paramInt1 >> 24));
/* 711 */     this.buf[(paramInt2 + 1)] = ((byte)(paramInt1 >> 16));
/* 712 */     this.buf[(paramInt2 + 2)] = ((byte)(paramInt1 >> 8));
/* 713 */     this.buf[(paramInt2 + 3)] = ((byte)paramInt1);
/*     */   }
/*     */ 
/*     */   void putShort(int paramInt1, int paramInt2) {
/* 717 */     this.buf[(paramInt2 + 0)] = ((byte)(paramInt1 >> 8));
/* 718 */     this.buf[(paramInt2 + 1)] = ((byte)paramInt1);
/*     */   }
/*     */ 
/*     */   void putByte(int paramInt1, int paramInt2) {
/* 722 */     this.buf[paramInt2] = ((byte)paramInt1);
/*     */   }
/*     */ 
/*     */   void putBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3) {
/* 726 */     System.arraycopy(paramArrayOfByte, paramInt1, this.buf, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   int length() {
/* 730 */     return this.buf.length;
/*     */   }
/*     */ 
/*     */   byte[] getData() {
/* 734 */     return this.buf;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.dns.Packet
 * JD-Core Version:    0.6.2
 */