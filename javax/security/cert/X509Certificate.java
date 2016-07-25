/*     */ package javax.security.cert;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.math.BigInteger;
/*     */ import java.security.AccessController;
/*     */ import java.security.Principal;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.Security;
/*     */ import java.util.Date;
/*     */ 
/*     */ public abstract class X509Certificate extends Certificate
/*     */ {
/*     */   private static final String X509_PROVIDER = "cert.provider.x509v1";
/* 145 */   private static String X509Provider = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public String run() {
/* 148 */       return Security.getProperty("cert.provider.x509v1");
/*     */     }
/*     */   });
/*     */ 
/*     */   public static final X509Certificate getInstance(InputStream paramInputStream)
/*     */     throws CertificateException
/*     */   {
/* 179 */     return getInst(paramInputStream);
/*     */   }
/*     */ 
/*     */   public static final X509Certificate getInstance(byte[] paramArrayOfByte)
/*     */     throws CertificateException
/*     */   {
/* 205 */     return getInst(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   private static final X509Certificate getInst(Object paramObject)
/*     */     throws CertificateException
/*     */   {
/* 215 */     String str = X509Provider;
/* 216 */     if ((str == null) || (str.length() == 0))
/*     */     {
/* 219 */       str = "com.sun.security.cert.internal.x509.X509V1CertImpl";
/*     */     }
/*     */     try {
/* 222 */       Class[] arrayOfClass = null;
/* 223 */       if ((paramObject instanceof InputStream))
/* 224 */         arrayOfClass = new Class[] { InputStream.class };
/* 225 */       else if ((paramObject instanceof byte[]))
/* 226 */         arrayOfClass = new Class[] { paramObject.getClass() };
/*     */       else
/* 228 */         throw new CertificateException("Unsupported argument type");
/* 229 */       Class localClass = Class.forName(str);
/*     */ 
/* 232 */       Constructor localConstructor = localClass.getConstructor(arrayOfClass);
/*     */ 
/* 235 */       Object localObject = localConstructor.newInstance(new Object[] { paramObject });
/* 236 */       return (X509Certificate)localObject;
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {
/* 239 */       throw new CertificateException("Could not find class: " + localClassNotFoundException);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 241 */       throw new CertificateException("Could not access class: " + localIllegalAccessException);
/*     */     } catch (InstantiationException localInstantiationException) {
/* 243 */       throw new CertificateException("Problems instantiating: " + localInstantiationException);
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 245 */       throw new CertificateException("InvocationTargetException: " + localInvocationTargetException.getTargetException());
/*     */     }
/*     */     catch (NoSuchMethodException localNoSuchMethodException) {
/* 248 */       throw new CertificateException("Could not find class method: " + localNoSuchMethodException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public abstract void checkValidity()
/*     */     throws CertificateExpiredException, CertificateNotYetValidException;
/*     */ 
/*     */   public abstract void checkValidity(Date paramDate)
/*     */     throws CertificateExpiredException, CertificateNotYetValidException;
/*     */ 
/*     */   public abstract int getVersion();
/*     */ 
/*     */   public abstract BigInteger getSerialNumber();
/*     */ 
/*     */   public abstract Principal getIssuerDN();
/*     */ 
/*     */   public abstract Principal getSubjectDN();
/*     */ 
/*     */   public abstract Date getNotBefore();
/*     */ 
/*     */   public abstract Date getNotAfter();
/*     */ 
/*     */   public abstract String getSigAlgName();
/*     */ 
/*     */   public abstract String getSigAlgOID();
/*     */ 
/*     */   public abstract byte[] getSigAlgParams();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.cert.X509Certificate
 * JD-Core Version:    0.6.2
 */