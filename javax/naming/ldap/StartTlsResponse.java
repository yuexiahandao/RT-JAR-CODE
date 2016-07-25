/*     */ package javax.naming.ldap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import javax.net.ssl.HostnameVerifier;
/*     */ import javax.net.ssl.SSLSession;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ 
/*     */ public abstract class StartTlsResponse
/*     */   implements ExtendedResponse
/*     */ {
/*     */   public static final String OID = "1.3.6.1.4.1.1466.20037";
/*     */   private static final long serialVersionUID = 8372842182579276418L;
/*     */ 
/*     */   public String getID()
/*     */   {
/* 106 */     return "1.3.6.1.4.1.1466.20037";
/*     */   }
/*     */ 
/*     */   public byte[] getEncodedValue()
/*     */   {
/* 117 */     return null;
/*     */   }
/*     */ 
/*     */   public abstract void setEnabledCipherSuites(String[] paramArrayOfString);
/*     */ 
/*     */   public abstract void setHostnameVerifier(HostnameVerifier paramHostnameVerifier);
/*     */ 
/*     */   public abstract SSLSession negotiate()
/*     */     throws IOException;
/*     */ 
/*     */   public abstract SSLSession negotiate(SSLSocketFactory paramSSLSocketFactory)
/*     */     throws IOException;
/*     */ 
/*     */   public abstract void close()
/*     */     throws IOException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.ldap.StartTlsResponse
 * JD-Core Version:    0.6.2
 */