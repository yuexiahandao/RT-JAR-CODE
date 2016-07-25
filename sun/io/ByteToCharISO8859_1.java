/*    */ package sun.io;
/*    */ 
/*    */ public class ByteToCharISO8859_1 extends ByteToCharConverter
/*    */ {
/*    */   public String getCharacterEncoding()
/*    */   {
/* 38 */     return "ISO8859_1";
/*    */   }
/*    */ 
/*    */   public int flush(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*    */   {
/* 43 */     this.byteOff = (this.charOff = 0);
/* 44 */     return 0;
/*    */   }
/*    */ 
/*    */   public int convert(byte[] paramArrayOfByte, int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3, int paramInt4)
/*    */     throws ConversionBufferFullException
/*    */   {
/* 55 */     int i = paramInt1 + (paramInt4 - paramInt3);
/* 56 */     if (i >= paramInt2) {
/* 57 */       i = paramInt2;
/*    */     }
/* 59 */     int j = paramInt2 - paramInt1;
/*    */     try
/*    */     {
/* 64 */       while (paramInt1 < i)
/* 65 */         paramArrayOfChar[(paramInt3++)] = ((char)(0xFF & paramArrayOfByte[(paramInt1++)]));
/*    */     }
/*    */     finally {
/* 68 */       this.charOff = paramInt3;
/* 69 */       this.byteOff = paramInt1;
/*    */     }
/*    */ 
/* 73 */     if (i < paramInt2) {
/* 74 */       throw new ConversionBufferFullException();
/*    */     }
/*    */ 
/* 77 */     return j;
/*    */   }
/*    */ 
/*    */   public void reset()
/*    */   {
/* 84 */     this.byteOff = (this.charOff = 0);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.ByteToCharISO8859_1
 * JD-Core Version:    0.6.2
 */