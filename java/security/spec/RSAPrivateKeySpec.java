/*    */ package java.security.spec;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ public class RSAPrivateKeySpec
/*    */   implements KeySpec
/*    */ {
/*    */   private BigInteger modulus;
/*    */   private BigInteger privateExponent;
/*    */ 
/*    */   public RSAPrivateKeySpec(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
/*    */   {
/* 56 */     this.modulus = paramBigInteger1;
/* 57 */     this.privateExponent = paramBigInteger2;
/*    */   }
/*    */ 
/*    */   public BigInteger getModulus()
/*    */   {
/* 66 */     return this.modulus;
/*    */   }
/*    */ 
/*    */   public BigInteger getPrivateExponent()
/*    */   {
/* 75 */     return this.privateExponent;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.spec.RSAPrivateKeySpec
 * JD-Core Version:    0.6.2
 */