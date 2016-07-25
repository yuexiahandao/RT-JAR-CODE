/*    */ package sun.security.krb5.internal.crypto;
/*    */ 
/*    */ public class Crc32CksumType extends CksumType
/*    */ {
/*    */   public int confounderSize()
/*    */   {
/* 43 */     return 0;
/*    */   }
/*    */ 
/*    */   public int cksumType() {
/* 47 */     return 1;
/*    */   }
/*    */ 
/*    */   public boolean isSafe() {
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */   public int cksumSize() {
/* 55 */     return 4;
/*    */   }
/*    */ 
/*    */   public int keyType() {
/* 59 */     return 0;
/*    */   }
/*    */ 
/*    */   public int keySize() {
/* 63 */     return 0;
/*    */   }
/*    */ 
/*    */   public byte[] calculateChecksum(byte[] paramArrayOfByte, int paramInt) {
/* 67 */     return crc32.byte2crc32sum_bytes(paramArrayOfByte, paramInt);
/*    */   }
/*    */ 
/*    */   public byte[] calculateKeyedChecksum(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, int paramInt2)
/*    */   {
/* 72 */     return null;
/*    */   }
/*    */ 
/*    */   public boolean verifyKeyedChecksum(byte[] paramArrayOfByte1, int paramInt1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3, int paramInt2)
/*    */   {
/* 77 */     return false;
/*    */   }
/*    */ 
/*    */   public static byte[] int2quad(long paramLong) {
/* 81 */     byte[] arrayOfByte = new byte[4];
/* 82 */     for (int i = 0; i < 4; i++) {
/* 83 */       arrayOfByte[i] = ((byte)(int)(paramLong >>> i * 8 & 0xFF));
/*    */     }
/* 85 */     return arrayOfByte;
/*    */   }
/*    */ 
/*    */   public static long bytes2long(byte[] paramArrayOfByte) {
/* 89 */     long l = 0L;
/*    */ 
/* 91 */     l |= (paramArrayOfByte[0] & 0xFF) << 24;
/* 92 */     l |= (paramArrayOfByte[1] & 0xFF) << 16;
/* 93 */     l |= (paramArrayOfByte[2] & 0xFF) << 8;
/* 94 */     l |= paramArrayOfByte[3] & 0xFF;
/* 95 */     return l;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.crypto.Crc32CksumType
 * JD-Core Version:    0.6.2
 */