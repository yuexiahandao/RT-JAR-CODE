/*     */ package javax.rmi.ssl;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.net.Socket;
/*     */ import java.rmi.server.RMIClientSocketFactory;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.net.SocketFactory;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ import javax.net.ssl.SSLSocketFactory;
/*     */ 
/*     */ public class SslRMIClientSocketFactory
/*     */   implements RMIClientSocketFactory, Serializable
/*     */ {
/* 203 */   private static SocketFactory defaultSocketFactory = null;
/*     */   private static final long serialVersionUID = -8310631444933958385L;
/*     */ 
/*     */   public Socket createSocket(String paramString, int paramInt)
/*     */     throws IOException
/*     */   {
/* 117 */     SocketFactory localSocketFactory = getDefaultClientSocketFactory();
/*     */ 
/* 120 */     SSLSocket localSSLSocket = (SSLSocket)localSocketFactory.createSocket(paramString, paramInt);
/*     */ 
/* 124 */     String str = System.getProperty("javax.rmi.ssl.client.enabledCipherSuites");
/*     */ 
/* 126 */     if (str != null) {
/* 127 */       localObject = new StringTokenizer(str, ",");
/* 128 */       int i = ((StringTokenizer)localObject).countTokens();
/* 129 */       String[] arrayOfString1 = new String[i];
/* 130 */       for (int k = 0; k < i; k++)
/* 131 */         arrayOfString1[k] = ((StringTokenizer)localObject).nextToken();
/*     */       try
/*     */       {
/* 134 */         localSSLSocket.setEnabledCipherSuites(arrayOfString1);
/*     */       } catch (IllegalArgumentException localIllegalArgumentException1) {
/* 136 */         throw ((IOException)new IOException(localIllegalArgumentException1.getMessage()).initCause(localIllegalArgumentException1));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 142 */     Object localObject = System.getProperty("javax.rmi.ssl.client.enabledProtocols");
/*     */ 
/* 144 */     if (localObject != null) {
/* 145 */       StringTokenizer localStringTokenizer = new StringTokenizer((String)localObject, ",");
/* 146 */       int j = localStringTokenizer.countTokens();
/* 147 */       String[] arrayOfString2 = new String[j];
/* 148 */       for (int m = 0; m < j; m++)
/* 149 */         arrayOfString2[m] = localStringTokenizer.nextToken();
/*     */       try
/*     */       {
/* 152 */         localSSLSocket.setEnabledProtocols(arrayOfString2);
/*     */       } catch (IllegalArgumentException localIllegalArgumentException2) {
/* 154 */         throw ((IOException)new IOException(localIllegalArgumentException2.getMessage()).initCause(localIllegalArgumentException2));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 160 */     return localSSLSocket;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 176 */     if (paramObject == null) return false;
/* 177 */     if (paramObject == this) return true;
/* 178 */     return getClass().equals(paramObject.getClass());
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 189 */     return getClass().hashCode();
/*     */   }
/*     */ 
/*     */   private static synchronized SocketFactory getDefaultClientSocketFactory()
/*     */   {
/* 206 */     if (defaultSocketFactory == null)
/* 207 */       defaultSocketFactory = SSLSocketFactory.getDefault();
/* 208 */     return defaultSocketFactory;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.rmi.ssl.SslRMIClientSocketFactory
 * JD-Core Version:    0.6.2
 */