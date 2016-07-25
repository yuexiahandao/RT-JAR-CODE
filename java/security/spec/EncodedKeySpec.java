/*    */ package java.security.spec;
/*    */ 
/*    */ public abstract class EncodedKeySpec
/*    */   implements KeySpec
/*    */ {
/*    */   private byte[] encodedKey;
/*    */ 
/*    */   public EncodedKeySpec(byte[] paramArrayOfByte)
/*    */   {
/* 56 */     this.encodedKey = ((byte[])paramArrayOfByte.clone());
/*    */   }
/*    */ 
/*    */   public byte[] getEncoded()
/*    */   {
/* 66 */     return (byte[])this.encodedKey.clone();
/*    */   }
/*    */ 
/*    */   public abstract String getFormat();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.spec.EncodedKeySpec
 * JD-Core Version:    0.6.2
 */