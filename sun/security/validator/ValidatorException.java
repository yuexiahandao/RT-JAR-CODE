/*     */ package sun.security.validator;
/*     */ 
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.X509Certificate;
/*     */ 
/*     */ public class ValidatorException extends CertificateException
/*     */ {
/*     */   private static final long serialVersionUID = -2836879718282292155L;
/*  40 */   public static final Object T_NO_TRUST_ANCHOR = "No trusted certificate found";
/*     */ 
/*  43 */   public static final Object T_EE_EXTENSIONS = "End entity certificate extension check failed";
/*     */ 
/*  46 */   public static final Object T_CA_EXTENSIONS = "CA certificate extension check failed";
/*     */ 
/*  49 */   public static final Object T_CERT_EXPIRED = "Certificate expired";
/*     */ 
/*  52 */   public static final Object T_SIGNATURE_ERROR = "Certificate signature validation failed";
/*     */ 
/*  55 */   public static final Object T_NAME_CHAINING = "Certificate chaining error";
/*     */ 
/*  58 */   public static final Object T_ALGORITHM_DISABLED = "Certificate signature algorithm disabled";
/*     */ 
/*  61 */   public static final Object T_UNTRUSTED_CERT = "Untrusted certificate";
/*     */   private Object type;
/*     */   private X509Certificate cert;
/*     */ 
/*     */   public ValidatorException(String paramString)
/*     */   {
/*  68 */     super(paramString);
/*     */   }
/*     */ 
/*     */   public ValidatorException(String paramString, Throwable paramThrowable) {
/*  72 */     super(paramString);
/*  73 */     initCause(paramThrowable);
/*     */   }
/*     */ 
/*     */   public ValidatorException(Object paramObject) {
/*  77 */     this(paramObject, null);
/*     */   }
/*     */ 
/*     */   public ValidatorException(Object paramObject, X509Certificate paramX509Certificate) {
/*  81 */     super((String)paramObject);
/*  82 */     this.type = paramObject;
/*  83 */     this.cert = paramX509Certificate;
/*     */   }
/*     */ 
/*     */   public ValidatorException(Object paramObject, X509Certificate paramX509Certificate, Throwable paramThrowable)
/*     */   {
/*  88 */     this(paramObject, paramX509Certificate);
/*  89 */     initCause(paramThrowable);
/*     */   }
/*     */ 
/*     */   public ValidatorException(String paramString, Object paramObject, X509Certificate paramX509Certificate) {
/*  93 */     super(paramString);
/*  94 */     this.type = paramObject;
/*  95 */     this.cert = paramX509Certificate;
/*     */   }
/*     */ 
/*     */   public ValidatorException(String paramString, Object paramObject, X509Certificate paramX509Certificate, Throwable paramThrowable)
/*     */   {
/* 100 */     this(paramString, paramObject, paramX509Certificate);
/* 101 */     initCause(paramThrowable);
/*     */   }
/*     */ 
/*     */   public Object getErrorType()
/*     */   {
/* 109 */     return this.type;
/*     */   }
/*     */ 
/*     */   public X509Certificate getErrorCertificate()
/*     */   {
/* 116 */     return this.cert;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.validator.ValidatorException
 * JD-Core Version:    0.6.2
 */