/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PrivateKey;
/*     */ import java.security.Provider;
/*     */ import java.security.PublicKey;
/*     */ import java.security.Signature;
/*     */ import java.security.SignatureException;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.dom.DOMCryptoContext;
/*     */ import javax.xml.crypto.dsig.SignatureMethod;
/*     */ import javax.xml.crypto.dsig.XMLSignContext;
/*     */ import javax.xml.crypto.dsig.XMLSignatureException;
/*     */ import javax.xml.crypto.dsig.XMLValidateContext;
/*     */ import javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec;
/*     */ import org.jcp.xml.dsig.internal.SignerOutputStream;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public abstract class DOMSignatureMethod extends DOMStructure
/*     */   implements SignatureMethod
/*     */ {
/*  53 */   private static Logger log = Logger.getLogger("org.jcp.xml.dsig.internal.dom");
/*     */   static final String RSA_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
/*     */   static final String RSA_SHA384 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha384";
/*     */   static final String RSA_SHA512 = "http://www.w3.org/2001/04/xmldsig-more#rsa-sha512";
/*     */   static final String HMAC_SHA256 = "http://www.w3.org/2001/04/xmldsig-more#hmac-sha256";
/*     */   static final String HMAC_SHA384 = "http://www.w3.org/2001/04/xmldsig-more#hmac-sha384";
/*     */   static final String HMAC_SHA512 = "http://www.w3.org/2001/04/xmldsig-more#hmac-sha512";
/*     */   private SignatureMethodParameterSpec params;
/*     */   private Signature signature;
/*     */ 
/*     */   DOMSignatureMethod(AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/*  82 */     if ((paramAlgorithmParameterSpec != null) && (!(paramAlgorithmParameterSpec instanceof SignatureMethodParameterSpec)))
/*     */     {
/*  84 */       throw new InvalidAlgorithmParameterException("params must be of type SignatureMethodParameterSpec");
/*     */     }
/*     */ 
/*  87 */     checkParams((SignatureMethodParameterSpec)paramAlgorithmParameterSpec);
/*  88 */     this.params = ((SignatureMethodParameterSpec)paramAlgorithmParameterSpec);
/*     */   }
/*     */ 
/*     */   DOMSignatureMethod(Element paramElement)
/*     */     throws MarshalException
/*     */   {
/*  99 */     Element localElement = DOMUtils.getFirstChildElement(paramElement);
/* 100 */     if (localElement != null)
/* 101 */       this.params = unmarshalParams(localElement);
/*     */     try
/*     */     {
/* 104 */       checkParams(this.params);
/*     */     } catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException) {
/* 106 */       throw new MarshalException(localInvalidAlgorithmParameterException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static SignatureMethod unmarshal(Element paramElement) throws MarshalException {
/* 111 */     String str = DOMUtils.getAttributeValue(paramElement, "Algorithm");
/* 112 */     if (str.equals("http://www.w3.org/2000/09/xmldsig#rsa-sha1"))
/* 113 */       return new SHA1withRSA(paramElement);
/* 114 */     if (str.equals("http://www.w3.org/2001/04/xmldsig-more#rsa-sha256"))
/* 115 */       return new SHA256withRSA(paramElement);
/* 116 */     if (str.equals("http://www.w3.org/2001/04/xmldsig-more#rsa-sha384"))
/* 117 */       return new SHA384withRSA(paramElement);
/* 118 */     if (str.equals("http://www.w3.org/2001/04/xmldsig-more#rsa-sha512"))
/* 119 */       return new SHA512withRSA(paramElement);
/* 120 */     if (str.equals("http://www.w3.org/2000/09/xmldsig#dsa-sha1"))
/* 121 */       return new SHA1withDSA(paramElement);
/* 122 */     if (str.equals("http://www.w3.org/2000/09/xmldsig#hmac-sha1"))
/* 123 */       return new DOMHMACSignatureMethod.SHA1(paramElement);
/* 124 */     if (str.equals("http://www.w3.org/2001/04/xmldsig-more#hmac-sha256"))
/* 125 */       return new DOMHMACSignatureMethod.SHA256(paramElement);
/* 126 */     if (str.equals("http://www.w3.org/2001/04/xmldsig-more#hmac-sha384"))
/* 127 */       return new DOMHMACSignatureMethod.SHA384(paramElement);
/* 128 */     if (str.equals("http://www.w3.org/2001/04/xmldsig-more#hmac-sha512")) {
/* 129 */       return new DOMHMACSignatureMethod.SHA512(paramElement);
/*     */     }
/* 131 */     throw new MarshalException("unsupported SignatureMethod algorithm: " + str);
/*     */   }
/*     */ 
/*     */   void checkParams(SignatureMethodParameterSpec paramSignatureMethodParameterSpec)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/* 148 */     if (paramSignatureMethodParameterSpec != null)
/* 149 */       throw new InvalidAlgorithmParameterException("no parameters should be specified for the " + getSignatureAlgorithm() + " SignatureMethod algorithm");
/*     */   }
/*     */ 
/*     */   public final AlgorithmParameterSpec getParameterSpec()
/*     */   {
/* 156 */     return this.params;
/*     */   }
/*     */ 
/*     */   SignatureMethodParameterSpec unmarshalParams(Element paramElement)
/*     */     throws MarshalException
/*     */   {
/* 171 */     throw new MarshalException("no parameters should be specified for the " + getSignatureAlgorithm() + " SignatureMethod algorithm");
/*     */   }
/*     */ 
/*     */   public void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext)
/*     */     throws MarshalException
/*     */   {
/* 182 */     Document localDocument = DOMUtils.getOwnerDocument(paramNode);
/*     */ 
/* 184 */     Element localElement = DOMUtils.createElement(localDocument, "SignatureMethod", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 186 */     DOMUtils.setAttribute(localElement, "Algorithm", getAlgorithm());
/*     */ 
/* 188 */     if (this.params != null) {
/* 189 */       marshalParams(localElement, paramString);
/*     */     }
/*     */ 
/* 192 */     paramNode.appendChild(localElement);
/*     */   }
/*     */ 
/*     */   boolean verify(Key paramKey, DOMSignedInfo paramDOMSignedInfo, byte[] paramArrayOfByte, XMLValidateContext paramXMLValidateContext)
/*     */     throws InvalidKeyException, SignatureException, XMLSignatureException
/*     */   {
/* 216 */     if ((paramKey == null) || (paramDOMSignedInfo == null) || (paramArrayOfByte == null)) {
/* 217 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 220 */     if (!(paramKey instanceof PublicKey)) {
/* 221 */       throw new InvalidKeyException("key must be PublicKey");
/*     */     }
/* 223 */     if (this.signature == null) {
/*     */       try {
/* 225 */         Provider localProvider = (Provider)paramXMLValidateContext.getProperty("org.jcp.xml.dsig.internal.dom.SignatureProvider");
/*     */ 
/* 227 */         this.signature = (localProvider == null ? Signature.getInstance(getSignatureAlgorithm()) : Signature.getInstance(getSignatureAlgorithm(), localProvider));
/*     */       }
/*     */       catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*     */       {
/* 231 */         throw new XMLSignatureException(localNoSuchAlgorithmException);
/*     */       }
/*     */     }
/* 234 */     this.signature.initVerify((PublicKey)paramKey);
/* 235 */     if (log.isLoggable(Level.FINE)) {
/* 236 */       log.log(Level.FINE, "Signature provider:" + this.signature.getProvider());
/* 237 */       log.log(Level.FINE, "verifying with key: " + paramKey);
/*     */     }
/* 239 */     paramDOMSignedInfo.canonicalize(paramXMLValidateContext, new SignerOutputStream(this.signature));
/*     */ 
/* 241 */     if (getAlgorithm().equals("http://www.w3.org/2000/09/xmldsig#dsa-sha1")) {
/*     */       try {
/* 243 */         return this.signature.verify(convertXMLDSIGtoASN1(paramArrayOfByte));
/*     */       } catch (IOException localIOException) {
/* 245 */         throw new XMLSignatureException(localIOException);
/*     */       }
/*     */     }
/* 248 */     return this.signature.verify(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   byte[] sign(Key paramKey, DOMSignedInfo paramDOMSignedInfo, XMLSignContext paramXMLSignContext)
/*     */     throws InvalidKeyException, XMLSignatureException
/*     */   {
/* 268 */     if ((paramKey == null) || (paramDOMSignedInfo == null)) {
/* 269 */       throw new NullPointerException();
/*     */     }
/*     */ 
/* 272 */     if (!(paramKey instanceof PrivateKey)) {
/* 273 */       throw new InvalidKeyException("key must be PrivateKey");
/*     */     }
/* 275 */     if (this.signature == null) {
/*     */       try {
/* 277 */         Provider localProvider = (Provider)paramXMLSignContext.getProperty("org.jcp.xml.dsig.internal.dom.SignatureProvider");
/*     */ 
/* 279 */         this.signature = (localProvider == null ? Signature.getInstance(getSignatureAlgorithm()) : Signature.getInstance(getSignatureAlgorithm(), localProvider));
/*     */       }
/*     */       catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
/*     */       {
/* 283 */         throw new XMLSignatureException(localNoSuchAlgorithmException);
/*     */       }
/*     */     }
/* 286 */     this.signature.initSign((PrivateKey)paramKey);
/* 287 */     if (log.isLoggable(Level.FINE)) {
/* 288 */       log.log(Level.FINE, "Signature provider:" + this.signature.getProvider());
/* 289 */       log.log(Level.FINE, "Signing with key: " + paramKey);
/*     */     }
/*     */ 
/* 292 */     paramDOMSignedInfo.canonicalize(paramXMLSignContext, new SignerOutputStream(this.signature));
/*     */     try
/*     */     {
/* 295 */       if (getAlgorithm().equals("http://www.w3.org/2000/09/xmldsig#dsa-sha1")) {
/* 296 */         return convertASN1toXMLDSIG(this.signature.sign());
/*     */       }
/* 298 */       return this.signature.sign();
/*     */     }
/*     */     catch (SignatureException localSignatureException) {
/* 301 */       throw new XMLSignatureException(localSignatureException);
/*     */     } catch (IOException localIOException) {
/* 303 */       throw new XMLSignatureException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   void marshalParams(Element paramElement, String paramString)
/*     */     throws MarshalException
/*     */   {
/* 319 */     throw new MarshalException("no parameters should be specified for the " + getSignatureAlgorithm() + " SignatureMethod algorithm");
/*     */   }
/*     */ 
/*     */   abstract String getSignatureAlgorithm();
/*     */ 
/*     */   boolean paramsEqual(AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */   {
/* 336 */     return getParameterSpec() == paramAlgorithmParameterSpec;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 340 */     if (this == paramObject) {
/* 341 */       return true;
/*     */     }
/*     */ 
/* 344 */     if (!(paramObject instanceof SignatureMethod)) {
/* 345 */       return false;
/*     */     }
/* 347 */     SignatureMethod localSignatureMethod = (SignatureMethod)paramObject;
/*     */ 
/* 349 */     return (getAlgorithm().equals(localSignatureMethod.getAlgorithm())) && (paramsEqual(localSignatureMethod.getParameterSpec()));
/*     */   }
/*     */ 
/*     */   private static byte[] convertASN1toXMLDSIG(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 367 */     int i = paramArrayOfByte[3];
/*     */ 
/* 370 */     for (int j = i; (j > 0) && (paramArrayOfByte[(4 + i - j)] == 0); j--);
/* 372 */     int k = paramArrayOfByte[(5 + i)];
/*     */ 
/* 375 */     int m = k;
/* 376 */     while ((m > 0) && (paramArrayOfByte[(6 + i + k - m)] == 0)) m--;
/*     */ 
/* 378 */     if ((paramArrayOfByte[0] != 48) || (paramArrayOfByte[1] != paramArrayOfByte.length - 2) || (paramArrayOfByte[2] != 2) || (j > 20) || (paramArrayOfByte[(4 + i)] != 2) || (m > 20))
/*     */     {
/* 381 */       throw new IOException("Invalid ASN.1 format of DSA signature");
/*     */     }
/* 383 */     byte[] arrayOfByte = new byte[40];
/*     */ 
/* 385 */     System.arraycopy(paramArrayOfByte, 4 + i - j, arrayOfByte, 20 - j, j);
/* 386 */     System.arraycopy(paramArrayOfByte, 6 + i + k - m, arrayOfByte, 40 - m, m);
/*     */ 
/* 389 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   private static byte[] convertXMLDSIGtoASN1(byte[] paramArrayOfByte)
/*     */     throws IOException
/*     */   {
/* 407 */     if (paramArrayOfByte.length != 40) {
/* 408 */       throw new IOException("Invalid XMLDSIG format of DSA signature");
/*     */     }
/*     */ 
/* 413 */     for (int i = 20; (i > 0) && (paramArrayOfByte[(20 - i)] == 0); i--);
/* 415 */     int j = i;
/*     */ 
/* 417 */     if (paramArrayOfByte[(20 - i)] < 0) {
/* 418 */       j++;
/*     */     }
/*     */ 
/* 423 */     for (int k = 20; (k > 0) && (paramArrayOfByte[(40 - k)] == 0); k--);
/* 425 */     int m = k;
/*     */ 
/* 427 */     if (paramArrayOfByte[(40 - k)] < 0) {
/* 428 */       m++;
/*     */     }
/*     */ 
/* 431 */     byte[] arrayOfByte = new byte[6 + j + m];
/*     */ 
/* 433 */     arrayOfByte[0] = 48;
/* 434 */     arrayOfByte[1] = ((byte)(4 + j + m));
/* 435 */     arrayOfByte[2] = 2;
/* 436 */     arrayOfByte[3] = ((byte)j);
/*     */ 
/* 438 */     System.arraycopy(paramArrayOfByte, 20 - i, arrayOfByte, 4 + j - i, i);
/*     */ 
/* 440 */     arrayOfByte[(4 + j)] = 2;
/* 441 */     arrayOfByte[(5 + j)] = ((byte)m);
/*     */ 
/* 443 */     System.arraycopy(paramArrayOfByte, 40 - k, arrayOfByte, 6 + j + m - k, k);
/*     */ 
/* 445 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   static final class SHA1withDSA extends DOMSignatureMethod
/*     */   {
/*     */     SHA1withDSA(AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */       throws InvalidAlgorithmParameterException
/*     */     {
/* 515 */       super();
/*     */     }
/*     */     SHA1withDSA(Element paramElement) throws MarshalException {
/* 518 */       super();
/*     */     }
/*     */     public String getAlgorithm() {
/* 521 */       return "http://www.w3.org/2000/09/xmldsig#dsa-sha1";
/*     */     }
/*     */     String getSignatureAlgorithm() {
/* 524 */       return "SHA1withDSA";
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class SHA1withRSA extends DOMSignatureMethod
/*     */   {
/*     */     SHA1withRSA(AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */       throws InvalidAlgorithmParameterException
/*     */     {
/* 451 */       super();
/*     */     }
/*     */     SHA1withRSA(Element paramElement) throws MarshalException {
/* 454 */       super();
/*     */     }
/*     */     public String getAlgorithm() {
/* 457 */       return "http://www.w3.org/2000/09/xmldsig#rsa-sha1";
/*     */     }
/*     */     String getSignatureAlgorithm() {
/* 460 */       return "SHA1withRSA";
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class SHA256withRSA extends DOMSignatureMethod
/*     */   {
/*     */     SHA256withRSA(AlgorithmParameterSpec paramAlgorithmParameterSpec) throws InvalidAlgorithmParameterException {
/* 467 */       super();
/*     */     }
/*     */     SHA256withRSA(Element paramElement) throws MarshalException {
/* 470 */       super();
/*     */     }
/*     */     public String getAlgorithm() {
/* 473 */       return "http://www.w3.org/2001/04/xmldsig-more#rsa-sha256";
/*     */     }
/*     */     String getSignatureAlgorithm() {
/* 476 */       return "SHA256withRSA";
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class SHA384withRSA extends DOMSignatureMethod
/*     */   {
/*     */     SHA384withRSA(AlgorithmParameterSpec paramAlgorithmParameterSpec) throws InvalidAlgorithmParameterException {
/* 483 */       super();
/*     */     }
/*     */     SHA384withRSA(Element paramElement) throws MarshalException {
/* 486 */       super();
/*     */     }
/*     */     public String getAlgorithm() {
/* 489 */       return "http://www.w3.org/2001/04/xmldsig-more#rsa-sha384";
/*     */     }
/*     */     String getSignatureAlgorithm() {
/* 492 */       return "SHA384withRSA";
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class SHA512withRSA extends DOMSignatureMethod
/*     */   {
/*     */     SHA512withRSA(AlgorithmParameterSpec paramAlgorithmParameterSpec) throws InvalidAlgorithmParameterException {
/* 499 */       super();
/*     */     }
/*     */     SHA512withRSA(Element paramElement) throws MarshalException {
/* 502 */       super();
/*     */     }
/*     */     public String getAlgorithm() {
/* 505 */       return "http://www.w3.org/2001/04/xmldsig-more#rsa-sha512";
/*     */     }
/*     */     String getSignatureAlgorithm() {
/* 508 */       return "SHA512withRSA";
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMSignatureMethod
 * JD-Core Version:    0.6.2
 */