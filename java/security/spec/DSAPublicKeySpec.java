/*     */ package java.security.spec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ public class DSAPublicKeySpec
/*     */   implements KeySpec
/*     */ {
/*     */   private BigInteger y;
/*     */   private BigInteger p;
/*     */   private BigInteger q;
/*     */   private BigInteger g;
/*     */ 
/*     */   public DSAPublicKeySpec(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
/*     */   {
/*  65 */     this.y = paramBigInteger1;
/*  66 */     this.p = paramBigInteger2;
/*  67 */     this.q = paramBigInteger3;
/*  68 */     this.g = paramBigInteger4;
/*     */   }
/*     */ 
/*     */   public BigInteger getY()
/*     */   {
/*  77 */     return this.y;
/*     */   }
/*     */ 
/*     */   public BigInteger getP()
/*     */   {
/*  86 */     return this.p;
/*     */   }
/*     */ 
/*     */   public BigInteger getQ()
/*     */   {
/*  95 */     return this.q;
/*     */   }
/*     */ 
/*     */   public BigInteger getG()
/*     */   {
/* 104 */     return this.g;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.spec.DSAPublicKeySpec
 * JD-Core Version:    0.6.2
 */