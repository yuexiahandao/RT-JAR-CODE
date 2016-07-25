/*    */ package java.security.spec;
/*    */ 
/*    */ public class ECPublicKeySpec
/*    */   implements KeySpec
/*    */ {
/*    */   private ECPoint w;
/*    */   private ECParameterSpec params;
/*    */ 
/*    */   public ECPublicKeySpec(ECPoint paramECPoint, ECParameterSpec paramECParameterSpec)
/*    */   {
/* 56 */     if (paramECPoint == null) {
/* 57 */       throw new NullPointerException("w is null");
/*    */     }
/* 59 */     if (paramECParameterSpec == null) {
/* 60 */       throw new NullPointerException("params is null");
/*    */     }
/* 62 */     if (paramECPoint == ECPoint.POINT_INFINITY) {
/* 63 */       throw new IllegalArgumentException("w is ECPoint.POINT_INFINITY");
/*    */     }
/* 65 */     this.w = paramECPoint;
/* 66 */     this.params = paramECParameterSpec;
/*    */   }
/*    */ 
/*    */   public ECPoint getW()
/*    */   {
/* 74 */     return this.w;
/*    */   }
/*    */ 
/*    */   public ECParameterSpec getParams()
/*    */   {
/* 83 */     return this.params;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.spec.ECPublicKeySpec
 * JD-Core Version:    0.6.2
 */