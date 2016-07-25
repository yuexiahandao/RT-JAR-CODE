/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.InvalidKeyException;
/*     */ import java.security.Key;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.SignatureException;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.crypto.Mac;
/*     */ import javax.crypto.SecretKey;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.dsig.XMLSignContext;
/*     */ import javax.xml.crypto.dsig.XMLSignatureException;
/*     */ import javax.xml.crypto.dsig.XMLValidateContext;
/*     */ import javax.xml.crypto.dsig.spec.HMACParameterSpec;
/*     */ import javax.xml.crypto.dsig.spec.SignatureMethodParameterSpec;
/*     */ import org.jcp.xml.dsig.internal.MacOutputStream;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public abstract class DOMHMACSignatureMethod extends DOMSignatureMethod
/*     */ {
/*  57 */   private static Logger log = Logger.getLogger("org.jcp.xml.dsig.internal.dom");
/*     */   private Mac hmac;
/*     */   private int outputLength;
/*     */   private boolean outputLengthSet;
/*     */ 
/*     */   DOMHMACSignatureMethod(AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/*  71 */     super(paramAlgorithmParameterSpec);
/*     */   }
/*     */ 
/*     */   DOMHMACSignatureMethod(Element paramElement)
/*     */     throws MarshalException
/*     */   {
/*  80 */     super(paramElement);
/*     */   }
/*     */ 
/*     */   void checkParams(SignatureMethodParameterSpec paramSignatureMethodParameterSpec) throws InvalidAlgorithmParameterException
/*     */   {
/*  85 */     if (paramSignatureMethodParameterSpec != null) {
/*  86 */       if (!(paramSignatureMethodParameterSpec instanceof HMACParameterSpec)) {
/*  87 */         throw new InvalidAlgorithmParameterException("params must be of type HMACParameterSpec");
/*     */       }
/*     */ 
/*  90 */       this.outputLength = ((HMACParameterSpec)paramSignatureMethodParameterSpec).getOutputLength();
/*  91 */       this.outputLengthSet = true;
/*  92 */       if (log.isLoggable(Level.FINE)) {
/*  93 */         log.log(Level.FINE, "Setting outputLength from HMACParameterSpec to: " + this.outputLength);
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*  98 */       this.outputLength = -1;
/*     */     }
/*     */   }
/*     */ 
/*     */   SignatureMethodParameterSpec unmarshalParams(Element paramElement) throws MarshalException
/*     */   {
/* 104 */     this.outputLength = new Integer(paramElement.getFirstChild().getNodeValue()).intValue();
/*     */ 
/* 106 */     this.outputLengthSet = true;
/* 107 */     if (log.isLoggable(Level.FINE)) {
/* 108 */       log.log(Level.FINE, "unmarshalled outputLength: " + this.outputLength);
/*     */     }
/* 110 */     return new HMACParameterSpec(this.outputLength);
/*     */   }
/*     */ 
/*     */   void marshalParams(Element paramElement, String paramString)
/*     */     throws MarshalException
/*     */   {
/* 116 */     Document localDocument = DOMUtils.getOwnerDocument(paramElement);
/* 117 */     Element localElement = DOMUtils.createElement(localDocument, "HMACOutputLength", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 119 */     localElement.appendChild(localDocument.createTextNode(String.valueOf(this.outputLength)));
/*     */ 
/* 122 */     paramElement.appendChild(localElement);
/*     */   }
/*     */ 
/*     */   boolean verify(Key paramKey, DOMSignedInfo paramDOMSignedInfo, byte[] paramArrayOfByte, XMLValidateContext paramXMLValidateContext)
/*     */     throws InvalidKeyException, SignatureException, XMLSignatureException
/*     */   {
/* 128 */     if ((paramKey == null) || (paramDOMSignedInfo == null) || (paramArrayOfByte == null)) {
/* 129 */       throw new NullPointerException();
/*     */     }
/* 131 */     if (!(paramKey instanceof SecretKey)) {
/* 132 */       throw new InvalidKeyException("key must be SecretKey");
/*     */     }
/* 134 */     if (this.hmac == null) {
/*     */       try {
/* 136 */         this.hmac = Mac.getInstance(getSignatureAlgorithm());
/*     */       } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 138 */         throw new XMLSignatureException(localNoSuchAlgorithmException);
/*     */       }
/*     */     }
/* 141 */     if ((this.outputLengthSet) && (this.outputLength < getDigestLength())) {
/* 142 */       throw new XMLSignatureException("HMACOutputLength must not be less than " + getDigestLength());
/*     */     }
/*     */ 
/* 145 */     this.hmac.init((SecretKey)paramKey);
/* 146 */     paramDOMSignedInfo.canonicalize(paramXMLValidateContext, new MacOutputStream(this.hmac));
/* 147 */     byte[] arrayOfByte = this.hmac.doFinal();
/*     */ 
/* 149 */     return MessageDigest.isEqual(paramArrayOfByte, arrayOfByte);
/*     */   }
/*     */ 
/*     */   byte[] sign(Key paramKey, DOMSignedInfo paramDOMSignedInfo, XMLSignContext paramXMLSignContext) throws InvalidKeyException, XMLSignatureException
/*     */   {
/* 154 */     if ((paramKey == null) || (paramDOMSignedInfo == null)) {
/* 155 */       throw new NullPointerException();
/*     */     }
/* 157 */     if (!(paramKey instanceof SecretKey)) {
/* 158 */       throw new InvalidKeyException("key must be SecretKey");
/*     */     }
/* 160 */     if (this.hmac == null) {
/*     */       try {
/* 162 */         this.hmac = Mac.getInstance(getSignatureAlgorithm());
/*     */       } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 164 */         throw new XMLSignatureException(localNoSuchAlgorithmException);
/*     */       }
/*     */     }
/* 167 */     if ((this.outputLengthSet) && (this.outputLength < getDigestLength())) {
/* 168 */       throw new XMLSignatureException("HMACOutputLength must not be less than " + getDigestLength());
/*     */     }
/*     */ 
/* 171 */     this.hmac.init((SecretKey)paramKey);
/* 172 */     paramDOMSignedInfo.canonicalize(paramXMLSignContext, new MacOutputStream(this.hmac));
/* 173 */     return this.hmac.doFinal();
/*     */   }
/*     */ 
/*     */   boolean paramsEqual(AlgorithmParameterSpec paramAlgorithmParameterSpec) {
/* 177 */     if (getParameterSpec() == paramAlgorithmParameterSpec) {
/* 178 */       return true;
/*     */     }
/* 180 */     if (!(paramAlgorithmParameterSpec instanceof HMACParameterSpec)) {
/* 181 */       return false;
/*     */     }
/* 183 */     HMACParameterSpec localHMACParameterSpec = (HMACParameterSpec)paramAlgorithmParameterSpec;
/*     */ 
/* 185 */     return this.outputLength == localHMACParameterSpec.getOutputLength();
/*     */   }
/*     */ 
/*     */   abstract int getDigestLength();
/*     */ 
/*     */   static final class SHA1 extends DOMHMACSignatureMethod
/*     */   {
/*     */     SHA1(AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */       throws InvalidAlgorithmParameterException
/*     */     {
/* 196 */       super();
/*     */     }
/*     */     SHA1(Element paramElement) throws MarshalException {
/* 199 */       super();
/*     */     }
/*     */     public String getAlgorithm() {
/* 202 */       return "http://www.w3.org/2000/09/xmldsig#hmac-sha1";
/*     */     }
/*     */     String getSignatureAlgorithm() {
/* 205 */       return "HmacSHA1";
/*     */     }
/*     */     int getDigestLength() {
/* 208 */       return 160;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class SHA256 extends DOMHMACSignatureMethod
/*     */   {
/*     */     SHA256(AlgorithmParameterSpec paramAlgorithmParameterSpec) throws InvalidAlgorithmParameterException {
/* 215 */       super();
/*     */     }
/*     */     SHA256(Element paramElement) throws MarshalException {
/* 218 */       super();
/*     */     }
/*     */     public String getAlgorithm() {
/* 221 */       return "http://www.w3.org/2001/04/xmldsig-more#hmac-sha256";
/*     */     }
/*     */     String getSignatureAlgorithm() {
/* 224 */       return "HmacSHA256";
/*     */     }
/*     */     int getDigestLength() {
/* 227 */       return 256;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class SHA384 extends DOMHMACSignatureMethod
/*     */   {
/*     */     SHA384(AlgorithmParameterSpec paramAlgorithmParameterSpec) throws InvalidAlgorithmParameterException {
/* 234 */       super();
/*     */     }
/*     */     SHA384(Element paramElement) throws MarshalException {
/* 237 */       super();
/*     */     }
/*     */     public String getAlgorithm() {
/* 240 */       return "http://www.w3.org/2001/04/xmldsig-more#hmac-sha384";
/*     */     }
/*     */     String getSignatureAlgorithm() {
/* 243 */       return "HmacSHA384";
/*     */     }
/*     */     int getDigestLength() {
/* 246 */       return 384;
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class SHA512 extends DOMHMACSignatureMethod
/*     */   {
/*     */     SHA512(AlgorithmParameterSpec paramAlgorithmParameterSpec) throws InvalidAlgorithmParameterException {
/* 253 */       super();
/*     */     }
/*     */     SHA512(Element paramElement) throws MarshalException {
/* 256 */       super();
/*     */     }
/*     */     public String getAlgorithm() {
/* 259 */       return "http://www.w3.org/2001/04/xmldsig-more#hmac-sha512";
/*     */     }
/*     */     String getSignatureAlgorithm() {
/* 262 */       return "HmacSHA512";
/*     */     }
/*     */     int getDigestLength() {
/* 265 */       return 512;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMHMACSignatureMethod
 * JD-Core Version:    0.6.2
 */