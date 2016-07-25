/*    */ package java.security.spec;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ public class RSAPublicKeySpec
/*    */   implements KeySpec
/*    */ {
/*    */   private BigInteger modulus;
/*    */   private BigInteger publicExponent;
/*    */ 
/*    */   public RSAPublicKeySpec(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
/*    */   {
/* 56 */     this.modulus = paramBigInteger1;
/* 57 */     this.publicExponent = paramBigInteger2;
/*    */   }
/*    */ 
/*    */   public BigInteger getModulus()
/*    */   {
/* 66 */     return this.modulus;
/*    */   }
/*    */ 
/*    */   public BigInteger getPublicExponent()
/*    */   {
/* 75 */     return this.publicExponent;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.spec.RSAPublicKeySpec
 * JD-Core Version:    0.6.2
 */