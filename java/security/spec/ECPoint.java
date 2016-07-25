/*     */ package java.security.spec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ public class ECPoint
/*     */ {
/*     */   private final BigInteger x;
/*     */   private final BigInteger y;
/*  47 */   public static final ECPoint POINT_INFINITY = new ECPoint();
/*     */ 
/*     */   private ECPoint()
/*     */   {
/*  51 */     this.x = null;
/*  52 */     this.y = null;
/*     */   }
/*     */ 
/*     */   public ECPoint(BigInteger paramBigInteger1, BigInteger paramBigInteger2)
/*     */   {
/*  64 */     if ((paramBigInteger1 == null) || (paramBigInteger2 == null)) {
/*  65 */       throw new NullPointerException("affine coordinate x or y is null");
/*     */     }
/*  67 */     this.x = paramBigInteger1;
/*  68 */     this.y = paramBigInteger2;
/*     */   }
/*     */ 
/*     */   public BigInteger getAffineX()
/*     */   {
/*  77 */     return this.x;
/*     */   }
/*     */ 
/*     */   public BigInteger getAffineY()
/*     */   {
/*  86 */     return this.y;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  97 */     if (this == paramObject) return true;
/*  98 */     if (this == POINT_INFINITY) return false;
/*  99 */     if ((paramObject instanceof ECPoint)) {
/* 100 */       return (this.x.equals(((ECPoint)paramObject).x)) && (this.y.equals(((ECPoint)paramObject).y));
/*     */     }
/*     */ 
/* 103 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 111 */     if (this == POINT_INFINITY) return 0;
/* 112 */     return this.x.hashCode() << 5 + this.y.hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.spec.ECPoint
 * JD-Core Version:    0.6.2
 */