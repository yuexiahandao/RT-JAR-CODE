/*     */ package java.security.spec;
/*     */ 
/*     */ public class MGF1ParameterSpec
/*     */   implements AlgorithmParameterSpec
/*     */ {
/*  63 */   public static final MGF1ParameterSpec SHA1 = new MGF1ParameterSpec("SHA-1");
/*     */ 
/*  68 */   public static final MGF1ParameterSpec SHA256 = new MGF1ParameterSpec("SHA-256");
/*     */ 
/*  73 */   public static final MGF1ParameterSpec SHA384 = new MGF1ParameterSpec("SHA-384");
/*     */ 
/*  78 */   public static final MGF1ParameterSpec SHA512 = new MGF1ParameterSpec("SHA-512");
/*     */   private String mdName;
/*     */ 
/*     */   public MGF1ParameterSpec(String paramString)
/*     */   {
/*  92 */     if (paramString == null) {
/*  93 */       throw new NullPointerException("digest algorithm is null");
/*     */     }
/*  95 */     this.mdName = paramString;
/*     */   }
/*     */ 
/*     */   public String getDigestAlgorithm()
/*     */   {
/* 105 */     return this.mdName;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.spec.MGF1ParameterSpec
 * JD-Core Version:    0.6.2
 */