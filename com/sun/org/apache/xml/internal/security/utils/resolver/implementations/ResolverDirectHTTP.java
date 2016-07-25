/*     */ package com.sun.org.apache.xml.internal.security.utils.resolver.implementations;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import com.sun.org.apache.xml.internal.security.utils.Base64;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverException;
/*     */ import com.sun.org.apache.xml.internal.security.utils.resolver.ResourceResolverSpi;
/*     */ import com.sun.org.apache.xml.internal.utils.URI;
/*     */ import com.sun.org.apache.xml.internal.utils.URI.MalformedURIException;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import org.w3c.dom.Attr;
/*     */ 
/*     */ public class ResolverDirectHTTP extends ResourceResolverSpi
/*     */ {
/*  64 */   static Logger log = Logger.getLogger(ResolverDirectHTTP.class.getName());
/*     */ 
/*  69 */   private static final String[] properties = { "http.proxy.host", "http.proxy.port", "http.proxy.username", "http.proxy.password", "http.basic.username", "http.basic.password" };
/*     */   private static final int HttpProxyHost = 0;
/*     */   private static final int HttpProxyPort = 1;
/*     */   private static final int HttpProxyUser = 2;
/*     */   private static final int HttpProxyPass = 3;
/*     */   private static final int HttpBasicUser = 4;
/*     */   private static final int HttpBasicPass = 5;
/*     */ 
/*     */   public boolean engineIsThreadSafe()
/*     */   {
/*  95 */     return true;
/*     */   }
/*     */ 
/*     */   public XMLSignatureInput engineResolve(Attr paramAttr, String paramString)
/*     */     throws ResourceResolverException
/*     */   {
/*     */     try
/*     */     {
/* 111 */       int i = 0;
/* 112 */       String str1 = engineGetProperty(properties[0]);
/*     */ 
/* 115 */       String str2 = engineGetProperty(properties[1]);
/*     */ 
/* 119 */       if ((str1 != null) && (str2 != null)) {
/* 120 */         i = 1;
/*     */       }
/*     */ 
/* 123 */       String str3 = null;
/* 124 */       String str4 = null;
/* 125 */       String str5 = null;
/*     */ 
/* 127 */       if (i != 0) {
/* 128 */         if (log.isLoggable(Level.FINE)) {
/* 129 */           log.log(Level.FINE, "Use of HTTP proxy enabled: " + str1 + ":" + str2);
/*     */         }
/*     */ 
/* 132 */         str3 = System.getProperty("http.proxySet");
/* 133 */         str4 = System.getProperty("http.proxyHost");
/* 134 */         str5 = System.getProperty("http.proxyPort");
/* 135 */         System.setProperty("http.proxySet", "true");
/* 136 */         System.setProperty("http.proxyHost", str1);
/* 137 */         System.setProperty("http.proxyPort", str2);
/*     */       }
/*     */ 
/* 140 */       int j = (str3 != null) && (str4 != null) && (str5 != null) ? 1 : 0;
/*     */ 
/* 145 */       URI localURI1 = getNewURI(paramAttr.getNodeValue(), paramString);
/*     */ 
/* 148 */       URI localURI2 = new URI(localURI1);
/*     */ 
/* 150 */       localURI2.setFragment(null);
/*     */ 
/* 152 */       URL localURL = new URL(localURI2.toString());
/* 153 */       URLConnection localURLConnection = localURL.openConnection();
/*     */ 
/* 158 */       String str6 = engineGetProperty(properties[2]);
/*     */ 
/* 161 */       Object localObject1 = engineGetProperty(properties[3]);
/*     */ 
/* 165 */       if ((str6 != null) && (localObject1 != null)) {
/* 166 */         localObject2 = str6 + ":" + (String)localObject1;
/* 167 */         localObject3 = Base64.encode(((String)localObject2).getBytes());
/*     */ 
/* 170 */         localURLConnection.setRequestProperty("Proxy-Authorization", (String)localObject3);
/*     */       }
/*     */ 
/* 178 */       str6 = localURLConnection.getHeaderField("WWW-Authenticate");
/*     */ 
/* 180 */       if (str6 != null)
/*     */       {
/* 183 */         if (str6.startsWith("Basic")) {
/* 184 */           localObject1 = engineGetProperty(properties[4]);
/*     */ 
/* 187 */           localObject2 = engineGetProperty(properties[5]);
/*     */ 
/* 191 */           if ((localObject1 != null) && (localObject2 != null)) {
/* 192 */             localURLConnection = localURL.openConnection();
/*     */ 
/* 194 */             localObject3 = (String)localObject1 + ":" + (String)localObject2;
/* 195 */             String str7 = Base64.encode(((String)localObject3).getBytes());
/*     */ 
/* 199 */             localURLConnection.setRequestProperty("Authorization", "Basic " + str7);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 207 */       str6 = localURLConnection.getHeaderField("Content-Type");
/* 208 */       localObject1 = localURLConnection.getInputStream();
/* 209 */       Object localObject2 = new ByteArrayOutputStream();
/* 210 */       Object localObject3 = new byte[4096];
/* 211 */       int k = 0;
/* 212 */       int m = 0;
/*     */ 
/* 214 */       while ((k = ((InputStream)localObject1).read((byte[])localObject3)) >= 0) {
/* 215 */         ((ByteArrayOutputStream)localObject2).write((byte[])localObject3, 0, k);
/*     */ 
/* 217 */         m += k;
/*     */       }
/*     */ 
/* 220 */       log.log(Level.FINE, "Fetched " + m + " bytes from URI " + localURI1.toString());
/*     */ 
/* 223 */       XMLSignatureInput localXMLSignatureInput = new XMLSignatureInput(((ByteArrayOutputStream)localObject2).toByteArray());
/*     */ 
/* 226 */       localXMLSignatureInput.setSourceURI(localURI1.toString());
/* 227 */       localXMLSignatureInput.setMIMEType(str6);
/*     */ 
/* 230 */       if ((i != 0) && (j != 0)) {
/* 231 */         System.setProperty("http.proxySet", str3);
/* 232 */         System.setProperty("http.proxyHost", str4);
/* 233 */         System.setProperty("http.proxyPort", str5);
/*     */       }
/*     */ 
/* 236 */       return localXMLSignatureInput;
/*     */     } catch (MalformedURLException localMalformedURLException) {
/* 238 */       throw new ResourceResolverException("generic.EmptyMessage", localMalformedURLException, paramAttr, paramString);
/*     */     }
/*     */     catch (IOException localIOException) {
/* 241 */       throw new ResourceResolverException("generic.EmptyMessage", localIOException, paramAttr, paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean engineCanResolve(Attr paramAttr, String paramString)
/*     */   {
/* 254 */     if (paramAttr == null) {
/* 255 */       log.log(Level.FINE, "quick fail, uri == null");
/*     */ 
/* 257 */       return false;
/*     */     }
/*     */ 
/* 260 */     String str = paramAttr.getNodeValue();
/*     */ 
/* 262 */     if ((str.equals("")) || (str.charAt(0) == '#')) {
/* 263 */       log.log(Level.FINE, "quick fail for empty URIs and local ones");
/*     */ 
/* 265 */       return false;
/*     */     }
/*     */ 
/* 268 */     if (log.isLoggable(Level.FINE)) {
/* 269 */       log.log(Level.FINE, "I was asked whether I can resolve " + str);
/*     */     }
/*     */ 
/* 272 */     if ((str.startsWith("http:")) || ((paramString != null) && (paramString.startsWith("http:"))))
/*     */     {
/* 274 */       if (log.isLoggable(Level.FINE)) {
/* 275 */         log.log(Level.FINE, "I state that I can resolve " + str);
/*     */       }
/*     */ 
/* 278 */       return true;
/*     */     }
/*     */ 
/* 281 */     if (log.isLoggable(Level.FINE)) {
/* 282 */       log.log(Level.FINE, "I state that I can't resolve " + str);
/*     */     }
/*     */ 
/* 285 */     return false;
/*     */   }
/*     */ 
/*     */   public String[] engineGetPropertyKeys()
/*     */   {
/* 292 */     return (String[])properties.clone();
/*     */   }
/*     */ 
/*     */   private URI getNewURI(String paramString1, String paramString2)
/*     */     throws URI.MalformedURIException
/*     */   {
/* 298 */     if ((paramString2 == null) || ("".equals(paramString2))) {
/* 299 */       return new URI(paramString1);
/*     */     }
/* 301 */     return new URI(new URI(paramString2), paramString1);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.utils.resolver.implementations.ResolverDirectHTTP
 * JD-Core Version:    0.6.2
 */