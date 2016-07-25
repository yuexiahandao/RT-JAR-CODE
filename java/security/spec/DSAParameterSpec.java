/*    */ package java.security.spec;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ import java.security.interfaces.DSAParams;
/*    */ 
/*    */ public class DSAParameterSpec
/*    */   implements AlgorithmParameterSpec, DSAParams
/*    */ {
/*    */   BigInteger p;
/*    */   BigInteger q;
/*    */   BigInteger g;
/*    */ 
/*    */   public DSAParameterSpec(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3)
/*    */   {
/* 58 */     this.p = paramBigInteger1;
/* 59 */     this.q = paramBigInteger2;
/* 60 */     this.g = paramBigInteger3;
/*    */   }
/*    */ 
/*    */   public BigInteger getP()
/*    */   {
/* 69 */     return this.p;
/*    */   }
/*    */ 
/*    */   public BigInteger getQ()
/*    */   {
/* 78 */     return this.q;
/*    */   }
/*    */ 
/*    */   public BigInteger getG()
/*    */   {
/* 87 */     return this.g;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.spec.DSAParameterSpec
 * JD-Core Version:    0.6.2
 */