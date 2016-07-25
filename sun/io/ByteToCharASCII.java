/*    */ package sun.io;
/*    */ 
/*    */ public class ByteToCharASCII extends ByteToCharConverter
/*    */ {
/*    */   public String getCharacterEncoding()
/*    */   {
/* 38 */     return "ASCII";
/*    */   }
/*    */ 
/*    */   public int flush(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*    */   {
/* 43 */     this.byteOff = (this.charOff = 0);
/* 44 */     return 0;
/*    */   }
/*    */ 
/*    */   public int convert(byte[] paramArrayOfByte, int paramInt1, int paramInt2, char[] paramArrayOfChar, int paramInt3, int paramInt4)
/*    */     throws ConversionBufferFullException, UnknownCharacterException
/*    */   {
/* 56 */     this.charOff = paramInt3;
/* 57 */     this.byteOff = paramInt1;
/*    */ 
/* 60 */     while (this.byteOff < paramInt2)
/*    */     {
/* 63 */       if (this.charOff >= paramInt4) {
/* 64 */         throw new ConversionBufferFullException();
/*    */       }
/*    */ 
/* 67 */       int i = paramArrayOfByte[(this.byteOff++)];
/*    */ 
/* 69 */       if (i >= 0) {
/* 70 */         paramArrayOfChar[(this.charOff++)] = ((char)i);
/*    */       }
/* 72 */       else if (this.subMode) {
/* 73 */         paramArrayOfChar[(this.charOff++)] = 65533;
/*    */       } else {
/* 75 */         this.badInputLength = 1;
/* 76 */         throw new UnknownCharacterException();
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 82 */     return this.charOff - paramInt3;
/*    */   }
/*    */ 
/*    */   public void reset()
/*    */   {
/* 89 */     this.byteOff = (this.charOff = 0);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.io.ByteToCharASCII
 * JD-Core Version:    0.6.2
 */