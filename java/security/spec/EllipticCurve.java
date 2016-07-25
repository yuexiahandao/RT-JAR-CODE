/*     */ package java.security.spec;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ public class EllipticCurve
/*     */ {
/*     */   private final ECField field;
/*     */   private final BigInteger a;
/*     */   private final BigInteger b;
/*     */   private final byte[] seed;
/*     */ 
/*     */   private static void checkValidity(ECField paramECField, BigInteger paramBigInteger, String paramString)
/*     */   {
/*  54 */     if ((paramECField instanceof ECFieldFp)) {
/*  55 */       BigInteger localBigInteger = ((ECFieldFp)paramECField).getP();
/*  56 */       if (localBigInteger.compareTo(paramBigInteger) != 1)
/*  57 */         throw new IllegalArgumentException(paramString + " is too large");
/*  58 */       if (paramBigInteger.signum() < 0)
/*  59 */         throw new IllegalArgumentException(paramString + " is negative");
/*     */     }
/*  61 */     else if ((paramECField instanceof ECFieldF2m)) {
/*  62 */       int i = ((ECFieldF2m)paramECField).getM();
/*  63 */       if (paramBigInteger.bitLength() > i)
/*  64 */         throw new IllegalArgumentException(paramString + " is too large");
/*     */     }
/*     */   }
/*     */ 
/*     */   public EllipticCurve(ECField paramECField, BigInteger paramBigInteger1, BigInteger paramBigInteger2)
/*     */   {
/*  83 */     this(paramECField, paramBigInteger1, paramBigInteger2, null);
/*     */   }
/*     */ 
/*     */   public EllipticCurve(ECField paramECField, BigInteger paramBigInteger1, BigInteger paramBigInteger2, byte[] paramArrayOfByte)
/*     */   {
/* 103 */     if (paramECField == null) {
/* 104 */       throw new NullPointerException("field is null");
/*     */     }
/* 106 */     if (paramBigInteger1 == null) {
/* 107 */       throw new NullPointerException("first coefficient is null");
/*     */     }
/* 109 */     if (paramBigInteger2 == null) {
/* 110 */       throw new NullPointerException("second coefficient is null");
/*     */     }
/* 112 */     checkValidity(paramECField, paramBigInteger1, "first coefficient");
/* 113 */     checkValidity(paramECField, paramBigInteger2, "second coefficient");
/* 114 */     this.field = paramECField;
/* 115 */     this.a = paramBigInteger1;
/* 116 */     this.b = paramBigInteger2;
/* 117 */     if (paramArrayOfByte != null)
/* 118 */       this.seed = ((byte[])paramArrayOfByte.clone());
/*     */     else
/* 120 */       this.seed = null;
/*     */   }
/*     */ 
/*     */   public ECField getField()
/*     */   {
/* 131 */     return this.field;
/*     */   }
/*     */ 
/*     */   public BigInteger getA()
/*     */   {
/* 140 */     return this.a;
/*     */   }
/*     */ 
/*     */   public BigInteger getB()
/*     */   {
/* 149 */     return this.b;
/*     */   }
/*     */ 
/*     */   public byte[] getSeed()
/*     */   {
/* 159 */     if (this.seed == null) return null;
/* 160 */     return (byte[])this.seed.clone();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 171 */     if (this == paramObject) return true;
/* 172 */     if ((paramObject instanceof EllipticCurve)) {
/* 173 */       EllipticCurve localEllipticCurve = (EllipticCurve)paramObject;
/* 174 */       if ((this.field.equals(localEllipticCurve.field)) && (this.a.equals(localEllipticCurve.a)) && (this.b.equals(localEllipticCurve.b)))
/*     */       {
/* 177 */         return true;
/*     */       }
/*     */     }
/* 180 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 192 */     return this.field.hashCode() << 6 + (this.a.hashCode() << 4) + (this.b.hashCode() << 2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.spec.EllipticCurve
 * JD-Core Version:    0.6.2
 */