/*    */ package com.sun.imageio.plugins.png;
/*    */ 
/*    */ class CRC
/*    */ {
/* 56 */   private static int[] crcTable = new int[256];
/* 57 */   private int crc = -1;
/*    */ 
/*    */   public void reset()
/*    */   {
/* 78 */     this.crc = -1;
/*    */   }
/*    */ 
/*    */   public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 82 */     for (int i = 0; i < paramInt2; i++)
/* 83 */       this.crc = (crcTable[((this.crc ^ paramArrayOfByte[(paramInt1 + i)]) & 0xFF)] ^ this.crc >>> 8);
/*    */   }
/*    */ 
/*    */   public void update(int paramInt)
/*    */   {
/* 88 */     this.crc = (crcTable[((this.crc ^ paramInt) & 0xFF)] ^ this.crc >>> 8);
/*    */   }
/*    */ 
/*    */   public int getValue() {
/* 92 */     return this.crc ^ 0xFFFFFFFF;
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 61 */     for (int i = 0; i < 256; i++) {
/* 62 */       int j = i;
/* 63 */       for (int k = 0; k < 8; k++) {
/* 64 */         if ((j & 0x1) == 1)
/* 65 */           j = 0xEDB88320 ^ j >>> 1;
/*    */         else {
/* 67 */           j >>>= 1;
/*    */         }
/*    */ 
/* 70 */         crcTable[i] = j;
/*    */       }
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.png.CRC
 * JD-Core Version:    0.6.2
 */