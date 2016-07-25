/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.HttpURLConnection;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.security.AccessController;
/*     */ import java.security.cert.CRLReason;
/*     */ import java.security.cert.CertPathValidatorException;
/*     */ import java.security.cert.CertificateException;
/*     */ import java.security.cert.Extension;
/*     */ import java.security.cert.X509Certificate;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import sun.security.action.GetIntegerAction;
/*     */ import sun.security.util.Debug;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ import sun.security.x509.AccessDescription;
/*     */ import sun.security.x509.AuthorityInfoAccessExtension;
/*     */ import sun.security.x509.GeneralName;
/*     */ import sun.security.x509.URIName;
/*     */ import sun.security.x509.X509CertImpl;
/*     */ 
/*     */ public final class OCSP
/*     */ {
/*  66 */   private static final Debug debug = Debug.getInstance("certpath");
/*     */   private static final int DEFAULT_CONNECT_TIMEOUT = 15000;
/*  75 */   private static final int CONNECT_TIMEOUT = initializeTimeout();
/*     */ 
/*     */   private static int initializeTimeout()
/*     */   {
/*  83 */     Integer localInteger = (Integer)AccessController.doPrivileged(new GetIntegerAction("com.sun.security.ocsp.timeout"));
/*     */ 
/*  85 */     if ((localInteger == null) || (localInteger.intValue() < 0)) {
/*  86 */       return 15000;
/*     */     }
/*     */ 
/*  90 */     return localInteger.intValue() * 1000;
/*     */   }
/*     */ 
/*     */   public static RevocationStatus check(X509Certificate paramX509Certificate1, X509Certificate paramX509Certificate2)
/*     */     throws IOException, CertPathValidatorException
/*     */   {
/* 112 */     CertId localCertId = null;
/* 113 */     URI localURI = null;
/*     */     try {
/* 115 */       X509CertImpl localX509CertImpl = X509CertImpl.toImpl(paramX509Certificate1);
/* 116 */       localURI = getResponderURI(localX509CertImpl);
/* 117 */       if (localURI == null) {
/* 118 */         throw new CertPathValidatorException("No OCSP Responder URI in certificate");
/*     */       }
/*     */ 
/* 121 */       localCertId = new CertId(paramX509Certificate2, localX509CertImpl.getSerialNumberObject());
/*     */     } catch (CertificateException localCertificateException) {
/* 123 */       throw new CertPathValidatorException("Exception while encoding OCSPRequest", localCertificateException);
/*     */     }
/*     */     catch (IOException localIOException) {
/* 126 */       throw new CertPathValidatorException("Exception while encoding OCSPRequest", localIOException);
/*     */     }
/*     */ 
/* 129 */     OCSPResponse localOCSPResponse = check(Collections.singletonList(localCertId), localURI, Collections.singletonList(paramX509Certificate2), null);
/*     */ 
/* 131 */     return localOCSPResponse.getSingleResponse(localCertId);
/*     */   }
/*     */ 
/*     */   public static RevocationStatus check(X509Certificate paramX509Certificate1, X509Certificate paramX509Certificate2, URI paramURI, X509Certificate paramX509Certificate3, Date paramDate)
/*     */     throws IOException, CertPathValidatorException
/*     */   {
/* 154 */     return check(paramX509Certificate1, paramX509Certificate2, paramURI, Collections.singletonList(paramX509Certificate3), paramDate);
/*     */   }
/*     */ 
/*     */   public static RevocationStatus check(X509Certificate paramX509Certificate1, X509Certificate paramX509Certificate2, URI paramURI, List<X509Certificate> paramList, Date paramDate)
/*     */     throws IOException, CertPathValidatorException
/*     */   {
/* 178 */     CertId localCertId = null;
/*     */     try {
/* 180 */       X509CertImpl localX509CertImpl = X509CertImpl.toImpl(paramX509Certificate1);
/* 181 */       localCertId = new CertId(paramX509Certificate2, localX509CertImpl.getSerialNumberObject());
/*     */     } catch (CertificateException localCertificateException) {
/* 183 */       throw new CertPathValidatorException("Exception while encoding OCSPRequest", localCertificateException);
/*     */     }
/*     */     catch (IOException localIOException) {
/* 186 */       throw new CertPathValidatorException("Exception while encoding OCSPRequest", localIOException);
/*     */     }
/*     */ 
/* 189 */     OCSPResponse localOCSPResponse = check(Collections.singletonList(localCertId), paramURI, paramList, paramDate);
/*     */ 
/* 191 */     return localOCSPResponse.getSingleResponse(localCertId);
/*     */   }
/*     */ 
/*     */   static OCSPResponse check(List<CertId> paramList, URI paramURI, List<X509Certificate> paramList1, Date paramDate)
/*     */     throws IOException, CertPathValidatorException
/*     */   {
/* 212 */     byte[] arrayOfByte1 = null;
/*     */     try {
/* 214 */       OCSPRequest localOCSPRequest = new OCSPRequest(paramList);
/* 215 */       arrayOfByte1 = localOCSPRequest.encodeBytes();
/*     */     } catch (IOException localIOException1) {
/* 217 */       throw new CertPathValidatorException("Exception while encoding OCSPRequest", localIOException1);
/*     */     }
/*     */ 
/* 221 */     InputStream localInputStream = null;
/* 222 */     OutputStream localOutputStream = null;
/* 223 */     byte[] arrayOfByte2 = null;
/*     */     try {
/* 225 */       URL localURL = paramURI.toURL();
/* 226 */       if (debug != null) {
/* 227 */         debug.println("connecting to OCSP service at: " + localURL);
/*     */       }
/* 229 */       HttpURLConnection localHttpURLConnection = (HttpURLConnection)localURL.openConnection();
/* 230 */       localHttpURLConnection.setConnectTimeout(CONNECT_TIMEOUT);
/* 231 */       localHttpURLConnection.setReadTimeout(CONNECT_TIMEOUT);
/* 232 */       localHttpURLConnection.setDoOutput(true);
/* 233 */       localHttpURLConnection.setDoInput(true);
/* 234 */       localHttpURLConnection.setRequestMethod("POST");
/* 235 */       localHttpURLConnection.setRequestProperty("Content-type", "application/ocsp-request");
/*     */ 
/* 237 */       localHttpURLConnection.setRequestProperty("Content-length", String.valueOf(arrayOfByte1.length));
/*     */ 
/* 239 */       localOutputStream = localHttpURLConnection.getOutputStream();
/* 240 */       localOutputStream.write(arrayOfByte1);
/* 241 */       localOutputStream.flush();
/*     */ 
/* 243 */       if ((debug != null) && (localHttpURLConnection.getResponseCode() != 200))
/*     */       {
/* 245 */         debug.println("Received HTTP error: " + localHttpURLConnection.getResponseCode() + " - " + localHttpURLConnection.getResponseMessage());
/*     */       }
/*     */ 
/* 248 */       localInputStream = localHttpURLConnection.getInputStream();
/* 249 */       int i = localHttpURLConnection.getContentLength();
/* 250 */       if (i == -1) {
/* 251 */         i = 2147483647;
/*     */       }
/* 253 */       arrayOfByte2 = new byte[i > 2048 ? 2048 : i];
/* 254 */       int j = 0;
/* 255 */       while (j < i) {
/* 256 */         int k = localInputStream.read(arrayOfByte2, j, arrayOfByte2.length - j);
/* 257 */         if (k < 0) {
/*     */           break;
/*     */         }
/* 260 */         j += k;
/* 261 */         if ((j >= arrayOfByte2.length) && (j < i)) {
/* 262 */           arrayOfByte2 = Arrays.copyOf(arrayOfByte2, j * 2);
/*     */         }
/*     */       }
/* 265 */       arrayOfByte2 = Arrays.copyOf(arrayOfByte2, j);
/*     */     } finally {
/* 267 */       if (localInputStream != null) {
/*     */         try {
/* 269 */           localInputStream.close();
/*     */         } catch (IOException localIOException5) {
/* 271 */           throw localIOException5;
/*     */         }
/*     */       }
/* 274 */       if (localOutputStream != null) {
/*     */         try {
/* 276 */           localOutputStream.close();
/*     */         } catch (IOException localIOException6) {
/* 278 */           throw localIOException6;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 283 */     OCSPResponse localOCSPResponse = null;
/*     */     try {
/* 285 */       localOCSPResponse = new OCSPResponse(arrayOfByte2, paramDate, paramList1);
/*     */     }
/*     */     catch (IOException localIOException4) {
/* 288 */       throw new CertPathValidatorException(localIOException4);
/*     */     }
/* 290 */     if (localOCSPResponse.getResponseStatus() != OCSPResponse.ResponseStatus.SUCCESSFUL) {
/* 291 */       throw new CertPathValidatorException("OCSP response error: " + localOCSPResponse.getResponseStatus());
/*     */     }
/*     */ 
/* 297 */     for (CertId localCertId : paramList) {
/* 298 */       OCSPResponse.SingleResponse localSingleResponse = localOCSPResponse.getSingleResponse(localCertId);
/* 299 */       if (localSingleResponse == null) {
/* 300 */         if (debug != null) {
/* 301 */           debug.println("No response found for CertId: " + localCertId);
/*     */         }
/* 303 */         throw new CertPathValidatorException("OCSP response does not include a response for a certificate supplied in the OCSP request");
/*     */       }
/*     */ 
/* 307 */       if (debug != null) {
/* 308 */         debug.println("Status of certificate (with serial number " + localCertId.getSerialNumber() + ") is: " + localSingleResponse.getCertStatus());
/*     */       }
/*     */     }
/*     */ 
/* 312 */     return localOCSPResponse;
/*     */   }
/*     */ 
/*     */   public static URI getResponderURI(X509Certificate paramX509Certificate)
/*     */   {
/*     */     try
/*     */     {
/* 325 */       return getResponderURI(X509CertImpl.toImpl(paramX509Certificate));
/*     */     } catch (CertificateException localCertificateException) {
/*     */     }
/* 328 */     return null;
/*     */   }
/*     */ 
/*     */   static URI getResponderURI(X509CertImpl paramX509CertImpl)
/*     */   {
/* 335 */     AuthorityInfoAccessExtension localAuthorityInfoAccessExtension = paramX509CertImpl.getAuthorityInfoAccessExtension();
/*     */ 
/* 337 */     if (localAuthorityInfoAccessExtension == null) {
/* 338 */       return null;
/*     */     }
/*     */ 
/* 341 */     List localList = localAuthorityInfoAccessExtension.getAccessDescriptions();
/* 342 */     for (AccessDescription localAccessDescription : localList) {
/* 343 */       if (localAccessDescription.getAccessMethod().equals(AccessDescription.Ad_OCSP_Id))
/*     */       {
/* 346 */         GeneralName localGeneralName = localAccessDescription.getAccessLocation();
/* 347 */         if (localGeneralName.getType() == 6) {
/* 348 */           URIName localURIName = (URIName)localGeneralName.getName();
/* 349 */           return localURIName.getURI();
/*     */         }
/*     */       }
/*     */     }
/* 353 */     return null; } 
/*     */   public static abstract interface RevocationStatus { public abstract CertStatus getCertStatus();
/*     */ 
/*     */     public abstract Date getRevocationTime();
/*     */ 
/*     */     public abstract CRLReason getRevocationReason();
/*     */ 
/*     */     public abstract Map<String, Extension> getSingleExtensions();
/*     */ 
/* 360 */     public static enum CertStatus { GOOD, REVOKED, UNKNOWN; }
/*     */ 
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.OCSP
 * JD-Core Version:    0.6.2
 */