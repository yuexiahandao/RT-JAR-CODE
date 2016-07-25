/*     */ package java.security.spec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ public class DSAPrivateKeySpec
/*     */   implements KeySpec
/*     */ {
/*     */   private BigInteger x;
/*     */   private BigInteger p;
/*     */   private BigInteger q;
/*     */   private BigInteger g;
/*     */ 
/*     */   public DSAPrivateKeySpec(BigInteger paramBigInteger1, BigInteger paramBigInteger2, BigInteger paramBigInteger3, BigInteger paramBigInteger4)
/*     */   {
/*  65 */     this.x = paramBigInteger1;
/*  66 */     this.p = paramBigInteger2;
/*  67 */     this.q = paramBigInteger3;
/*  68 */     this.g = paramBigInteger4;
/*     */   }
/*     */ 
/*     */   public BigInteger getX()
/*     */   {
/*  77 */     return this.x;
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
 * Qualified Name:     java.security.spec.DSAPrivateKeySpec
 * JD-Core Version:    0.6.2
 */