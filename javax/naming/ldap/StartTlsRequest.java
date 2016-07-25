/*     */ package javax.naming.ldap;
/*     */ 
/*     */ import com.sun.naming.internal.VersionHelper;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Iterator;
/*     */ import java.util.ServiceLoader;
/*     */ import javax.naming.ConfigurationException;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ public class StartTlsRequest
/*     */   implements ExtendedRequest
/*     */ {
/*     */   public static final String OID = "1.3.6.1.4.1.1466.20037";
/*     */   private static final long serialVersionUID = 4441679576360753397L;
/*     */ 
/*     */   public String getID()
/*     */   {
/* 107 */     return "1.3.6.1.4.1.1466.20037";
/*     */   }
/*     */ 
/*     */   public byte[] getEncodedValue()
/*     */   {
/* 118 */     return null;
/*     */   }
/*     */ 
/*     */   public ExtendedResponse createExtendedResponse(String paramString, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws NamingException
/*     */   {
/* 175 */     if ((paramString != null) && (!paramString.equals("1.3.6.1.4.1.1466.20037"))) {
/* 176 */       throw new ConfigurationException("Start TLS received the following response instead of 1.3.6.1.4.1.1466.20037: " + paramString);
/*     */     }
/*     */ 
/* 181 */     StartTlsResponse localStartTlsResponse = null;
/*     */ 
/* 183 */     ServiceLoader localServiceLoader = ServiceLoader.load(StartTlsResponse.class, getContextClassLoader());
/*     */ 
/* 185 */     Iterator localIterator = localServiceLoader.iterator();
/*     */ 
/* 187 */     while ((localStartTlsResponse == null) && (privilegedHasNext(localIterator))) {
/* 188 */       localStartTlsResponse = (StartTlsResponse)localIterator.next();
/*     */     }
/* 190 */     if (localStartTlsResponse != null)
/* 191 */       return localStartTlsResponse;
/*     */     try
/*     */     {
/* 194 */       VersionHelper localVersionHelper = VersionHelper.getVersionHelper();
/* 195 */       Class localClass = localVersionHelper.loadClass("com.sun.jndi.ldap.ext.StartTlsResponseImpl");
/*     */ 
/* 198 */       localStartTlsResponse = (StartTlsResponse)localClass.newInstance();
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException) {
/* 201 */       throw wrapException(localIllegalAccessException);
/*     */     }
/*     */     catch (InstantiationException localInstantiationException) {
/* 204 */       throw wrapException(localInstantiationException);
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {
/* 207 */       throw wrapException(localClassNotFoundException);
/*     */     }
/*     */ 
/* 210 */     return localStartTlsResponse;
/*     */   }
/*     */ 
/*     */   private ConfigurationException wrapException(Exception paramException)
/*     */   {
/* 218 */     ConfigurationException localConfigurationException = new ConfigurationException("Cannot load implementation of javax.naming.ldap.StartTlsResponse");
/*     */ 
/* 221 */     localConfigurationException.setRootCause(paramException);
/* 222 */     return localConfigurationException;
/*     */   }
/*     */ 
/*     */   private final ClassLoader getContextClassLoader()
/*     */   {
/* 229 */     return (ClassLoader)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 232 */         return Thread.currentThread().getContextClassLoader();
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static final boolean privilegedHasNext(Iterator paramIterator)
/*     */   {
/* 239 */     Boolean localBoolean = (Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Object run() {
/* 242 */         return Boolean.valueOf(this.val$iter.hasNext());
/*     */       }
/*     */     });
/* 245 */     return localBoolean.booleanValue();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.ldap.StartTlsRequest
 * JD-Core Version:    0.6.2
 */