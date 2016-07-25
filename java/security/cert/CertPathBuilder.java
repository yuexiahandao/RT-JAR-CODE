/*     */ package java.security.cert;
/*     */ 
/*     */ import java.security.AccessController;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.NoSuchProviderException;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.Provider;
/*     */ import java.security.Security;
/*     */ import sun.security.jca.GetInstance;
/*     */ import sun.security.jca.GetInstance.Instance;
/*     */ import sun.security.util.Debug;
/*     */ 
/*     */ public class CertPathBuilder
/*     */ {
/*     */   private static final String CPB_TYPE = "certpathbuilder.type";
/*  99 */   private static final Debug debug = Debug.getInstance("certpath");
/*     */   private CertPathBuilderSpi builderSpi;
/*     */   private Provider provider;
/*     */   private String algorithm;
/*     */ 
/*     */   protected CertPathBuilder(CertPathBuilderSpi paramCertPathBuilderSpi, Provider paramProvider, String paramString)
/*     */   {
/* 115 */     this.builderSpi = paramCertPathBuilderSpi;
/* 116 */     this.provider = paramProvider;
/* 117 */     this.algorithm = paramString;
/*     */   }
/*     */ 
/*     */   public static CertPathBuilder getInstance(String paramString)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 150 */     GetInstance.Instance localInstance = GetInstance.getInstance("CertPathBuilder", CertPathBuilderSpi.class, paramString);
/*     */ 
/* 152 */     return new CertPathBuilder((CertPathBuilderSpi)localInstance.impl, localInstance.provider, paramString);
/*     */   }
/*     */ 
/*     */   public static CertPathBuilder getInstance(String paramString1, String paramString2)
/*     */     throws NoSuchAlgorithmException, NoSuchProviderException
/*     */   {
/* 193 */     GetInstance.Instance localInstance = GetInstance.getInstance("CertPathBuilder", CertPathBuilderSpi.class, paramString1, paramString2);
/*     */ 
/* 195 */     return new CertPathBuilder((CertPathBuilderSpi)localInstance.impl, localInstance.provider, paramString1);
/*     */   }
/*     */ 
/*     */   public static CertPathBuilder getInstance(String paramString, Provider paramProvider)
/*     */     throws NoSuchAlgorithmException
/*     */   {
/* 230 */     GetInstance.Instance localInstance = GetInstance.getInstance("CertPathBuilder", CertPathBuilderSpi.class, paramString, paramProvider);
/*     */ 
/* 232 */     return new CertPathBuilder((CertPathBuilderSpi)localInstance.impl, localInstance.provider, paramString);
/*     */   }
/*     */ 
/*     */   public final Provider getProvider()
/*     */   {
/* 242 */     return this.provider;
/*     */   }
/*     */ 
/*     */   public final String getAlgorithm()
/*     */   {
/* 251 */     return this.algorithm;
/*     */   }
/*     */ 
/*     */   public final CertPathBuilderResult build(CertPathParameters paramCertPathParameters)
/*     */     throws CertPathBuilderException, InvalidAlgorithmParameterException
/*     */   {
/* 268 */     return this.builderSpi.engineBuild(paramCertPathParameters);
/*     */   }
/*     */ 
/*     */   public static final String getDefaultType()
/*     */   {
/* 294 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public String run() {
/* 296 */         return Security.getProperty("certpathbuilder.type");
/*     */       }
/*     */     });
/* 299 */     if (str == null) {
/* 300 */       str = "PKIX";
/*     */     }
/* 302 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.cert.CertPathBuilder
 * JD-Core Version:    0.6.2
 */