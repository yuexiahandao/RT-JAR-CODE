/*    */ package java.security.spec;
/*    */ 
/*    */ public class PKCS8EncodedKeySpec extends EncodedKeySpec
/*    */ {
/*    */   public PKCS8EncodedKeySpec(byte[] paramArrayOfByte)
/*    */   {
/* 74 */     super(paramArrayOfByte);
/*    */   }
/*    */ 
/*    */   public byte[] getEncoded()
/*    */   {
/* 84 */     return super.getEncoded();
/*    */   }
/*    */ 
/*    */   public final String getFormat()
/*    */   {
/* 94 */     return "PKCS#8";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.spec.PKCS8EncodedKeySpec
 * JD-Core Version:    0.6.2
 */