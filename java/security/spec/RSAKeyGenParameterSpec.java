/*    */ package java.security.spec;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ public class RSAKeyGenParameterSpec
/*    */   implements AlgorithmParameterSpec
/*    */ {
/*    */   private int keysize;
/*    */   private BigInteger publicExponent;
/* 50 */   public static final BigInteger F0 = BigInteger.valueOf(3L);
/*    */ 
/* 55 */   public static final BigInteger F4 = BigInteger.valueOf(65537L);
/*    */ 
/*    */   public RSAKeyGenParameterSpec(int paramInt, BigInteger paramBigInteger)
/*    */   {
/* 65 */     this.keysize = paramInt;
/* 66 */     this.publicExponent = paramBigInteger;
/*    */   }
/*    */ 
/*    */   public int getKeysize()
/*    */   {
/* 75 */     return this.keysize;
/*    */   }
/*    */ 
/*    */   public BigInteger getPublicExponent()
/*    */   {
/* 84 */     return this.publicExponent;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.spec.RSAKeyGenParameterSpec
 * JD-Core Version:    0.6.2
 */