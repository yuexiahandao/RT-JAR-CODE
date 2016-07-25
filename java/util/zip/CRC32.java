/*    */ package java.util.zip;
/*    */ 
/*    */ public class CRC32
/*    */   implements Checksum
/*    */ {
/*    */   private int crc;
/*    */ 
/*    */   public void update(int paramInt)
/*    */   {
/* 52 */     this.crc = update(this.crc, paramInt);
/*    */   }
/*    */ 
/*    */   public void update(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*    */   {
/* 59 */     if (paramArrayOfByte == null) {
/* 60 */       throw new NullPointerException();
/*    */     }
/* 62 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 > paramArrayOfByte.length - paramInt2)) {
/* 63 */       throw new ArrayIndexOutOfBoundsException();
/*    */     }
/* 65 */     this.crc = updateBytes(this.crc, paramArrayOfByte, paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   public void update(byte[] paramArrayOfByte)
/*    */   {
/* 74 */     this.crc = updateBytes(this.crc, paramArrayOfByte, 0, paramArrayOfByte.length);
/*    */   }
/*    */ 
/*    */   public void reset()
/*    */   {
/* 81 */     this.crc = 0;
/*    */   }
/*    */ 
/*    */   public long getValue()
/*    */   {
/* 88 */     return this.crc & 0xFFFFFFFF;
/*    */   }
/*    */ 
/*    */   private static native int update(int paramInt1, int paramInt2);
/*    */ 
/*    */   private static native int updateBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.zip.CRC32
 * JD-Core Version:    0.6.2
 */