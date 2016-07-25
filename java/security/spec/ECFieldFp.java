/*    */ package java.security.spec;
/*    */ 
/*    */ import java.math.BigInteger;
/*    */ 
/*    */ public class ECFieldFp
/*    */   implements ECField
/*    */ {
/*    */   private BigInteger p;
/*    */ 
/*    */   public ECFieldFp(BigInteger paramBigInteger)
/*    */   {
/* 53 */     if (paramBigInteger.signum() != 1) {
/* 54 */       throw new IllegalArgumentException("p is not positive");
/*    */     }
/* 56 */     this.p = paramBigInteger;
/*    */   }
/*    */ 
/*    */   public int getFieldSize()
/*    */   {
/* 65 */     return this.p.bitLength();
/*    */   }
/*    */ 
/*    */   public BigInteger getP()
/*    */   {
/* 73 */     return this.p;
/*    */   }
/*    */ 
/*    */   public boolean equals(Object paramObject)
/*    */   {
/* 84 */     if (this == paramObject) return true;
/* 85 */     if ((paramObject instanceof ECFieldFp)) {
/* 86 */       return this.p.equals(((ECFieldFp)paramObject).p);
/*    */     }
/* 88 */     return false;
/*    */   }
/*    */ 
/*    */   public int hashCode()
/*    */   {
/* 96 */     return this.p.hashCode();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.spec.ECFieldFp
 * JD-Core Version:    0.6.2
 */