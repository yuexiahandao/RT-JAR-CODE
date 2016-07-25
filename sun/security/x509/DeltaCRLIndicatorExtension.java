/*     */ package sun.security.x509;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.math.BigInteger;
/*     */ import sun.security.util.DerOutputStream;
/*     */ 
/*     */ public class DeltaCRLIndicatorExtension extends CRLNumberExtension
/*     */ {
/*     */   public static final String NAME = "DeltaCRLIndicator";
/*     */   private static final String LABEL = "Base CRL Number";
/*     */ 
/*     */   public DeltaCRLIndicatorExtension(int paramInt)
/*     */     throws IOException
/*     */   {
/*  78 */     super(PKIXExtensions.DeltaCRLIndicator_Id, true, BigInteger.valueOf(paramInt), "DeltaCRLIndicator", "Base CRL Number");
/*     */   }
/*     */ 
/*     */   public DeltaCRLIndicatorExtension(BigInteger paramBigInteger)
/*     */     throws IOException
/*     */   {
/*  89 */     super(PKIXExtensions.DeltaCRLIndicator_Id, true, paramBigInteger, "DeltaCRLIndicator", "Base CRL Number");
/*     */   }
/*     */ 
/*     */   public DeltaCRLIndicatorExtension(Boolean paramBoolean, Object paramObject)
/*     */     throws IOException
/*     */   {
/* 102 */     super(PKIXExtensions.DeltaCRLIndicator_Id, Boolean.valueOf(paramBoolean.booleanValue()), paramObject, "DeltaCRLIndicator", "Base CRL Number");
/*     */   }
/*     */ 
/*     */   public void encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 113 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/* 114 */     super.encode(paramOutputStream, PKIXExtensions.DeltaCRLIndicator_Id, true);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.x509.DeltaCRLIndicatorExtension
 * JD-Core Version:    0.6.2
 */