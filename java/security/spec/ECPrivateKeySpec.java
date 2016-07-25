/*    */ package java.security.spec;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ public class ECPrivateKeySpec
/*    */   implements KeySpec
/*    */ {
/*    */   private BigInteger s;
/*    */   private ECParameterSpec params;
/*    */ 
/*    */   public ECPrivateKeySpec(BigInteger paramBigInteger, ECParameterSpec paramECParameterSpec)
/*    */   {
/* 55 */     if (paramBigInteger == null) {
/* 56 */       throw new NullPointerException("s is null");
/*    */     }
/* 58 */     if (paramECParameterSpec == null) {
/* 59 */       throw new NullPointerException("params is null");
/*    */     }
/* 61 */     this.s = paramBigInteger;
/* 62 */     this.params = paramECParameterSpec;
/*    */   }
/*    */ 
/*    */   public BigInteger getS()
/*    */   {
/* 70 */     return this.s;
/*    */   }
/*    */ 
/*    */   public ECParameterSpec getParams()
/*    */   {
/* 79 */     return this.params;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.spec.ECPrivateKeySpec
 * JD-Core Version:    0.6.2
 */