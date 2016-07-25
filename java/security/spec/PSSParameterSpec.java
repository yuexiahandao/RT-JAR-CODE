/*     */ package java.security.spec;
/*     */ 
/*     */ public class PSSParameterSpec
/*     */   implements AlgorithmParameterSpec
/*     */ {
/*  80 */   private String mdName = "SHA-1";
/*  81 */   private String mgfName = "MGF1";
/*  82 */   private AlgorithmParameterSpec mgfSpec = MGF1ParameterSpec.SHA1;
/*  83 */   private int saltLen = 20;
/*  84 */   private int trailerField = 1;
/*     */ 
/*  90 */   public static final PSSParameterSpec DEFAULT = new PSSParameterSpec();
/*     */ 
/*     */   private PSSParameterSpec()
/*     */   {
/*     */   }
/*     */ 
/*     */   public PSSParameterSpec(String paramString1, String paramString2, AlgorithmParameterSpec paramAlgorithmParameterSpec, int paramInt1, int paramInt2)
/*     */   {
/* 122 */     if (paramString1 == null) {
/* 123 */       throw new NullPointerException("digest algorithm is null");
/*     */     }
/* 125 */     if (paramString2 == null) {
/* 126 */       throw new NullPointerException("mask generation function algorithm is null");
/*     */     }
/*     */ 
/* 129 */     if (paramInt1 < 0) {
/* 130 */       throw new IllegalArgumentException("negative saltLen value: " + paramInt1);
/*     */     }
/*     */ 
/* 133 */     if (paramInt2 < 0) {
/* 134 */       throw new IllegalArgumentException("negative trailerField: " + paramInt2);
/*     */     }
/*     */ 
/* 137 */     this.mdName = paramString1;
/* 138 */     this.mgfName = paramString2;
/* 139 */     this.mgfSpec = paramAlgorithmParameterSpec;
/* 140 */     this.saltLen = paramInt1;
/* 141 */     this.trailerField = paramInt2;
/*     */   }
/*     */ 
/*     */   public PSSParameterSpec(int paramInt)
/*     */   {
/* 155 */     if (paramInt < 0) {
/* 156 */       throw new IllegalArgumentException("negative saltLen value: " + paramInt);
/*     */     }
/*     */ 
/* 159 */     this.saltLen = paramInt;
/*     */   }
/*     */ 
/*     */   public String getDigestAlgorithm()
/*     */   {
/* 169 */     return this.mdName;
/*     */   }
/*     */ 
/*     */   public String getMGFAlgorithm()
/*     */   {
/* 180 */     return this.mgfName;
/*     */   }
/*     */ 
/*     */   public AlgorithmParameterSpec getMGFParameters()
/*     */   {
/* 190 */     return this.mgfSpec;
/*     */   }
/*     */ 
/*     */   public int getSaltLength()
/*     */   {
/* 199 */     return this.saltLen;
/*     */   }
/*     */ 
/*     */   public int getTrailerField()
/*     */   {
/* 209 */     return this.trailerField;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.spec.PSSParameterSpec
 * JD-Core Version:    0.6.2
 */