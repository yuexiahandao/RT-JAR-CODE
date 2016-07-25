/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import java.security.InvalidAlgorithmParameterException;
/*     */ import java.security.spec.AlgorithmParameterSpec;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.dom.DOMCryptoContext;
/*     */ import javax.xml.crypto.dsig.DigestMethod;
/*     */ import javax.xml.crypto.dsig.spec.DigestMethodParameterSpec;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public abstract class DOMDigestMethod extends DOMStructure
/*     */   implements DigestMethod
/*     */ {
/*     */   static final String SHA384 = "http://www.w3.org/2001/04/xmldsig-more#sha384";
/*     */   private DigestMethodParameterSpec params;
/*     */ 
/*     */   DOMDigestMethod(AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/*  61 */     if ((paramAlgorithmParameterSpec != null) && (!(paramAlgorithmParameterSpec instanceof DigestMethodParameterSpec))) {
/*  62 */       throw new InvalidAlgorithmParameterException("params must be of type DigestMethodParameterSpec");
/*     */     }
/*     */ 
/*  65 */     checkParams((DigestMethodParameterSpec)paramAlgorithmParameterSpec);
/*  66 */     this.params = ((DigestMethodParameterSpec)paramAlgorithmParameterSpec);
/*     */   }
/*     */ 
/*     */   DOMDigestMethod(Element paramElement)
/*     */     throws MarshalException
/*     */   {
/*  77 */     Element localElement = DOMUtils.getFirstChildElement(paramElement);
/*  78 */     if (localElement != null)
/*  79 */       this.params = unmarshalParams(localElement);
/*     */     try
/*     */     {
/*  82 */       checkParams(this.params);
/*     */     } catch (InvalidAlgorithmParameterException localInvalidAlgorithmParameterException) {
/*  84 */       throw new MarshalException(localInvalidAlgorithmParameterException);
/*     */     }
/*     */   }
/*     */ 
/*     */   static DigestMethod unmarshal(Element paramElement) throws MarshalException {
/*  89 */     String str = DOMUtils.getAttributeValue(paramElement, "Algorithm");
/*  90 */     if (str.equals("http://www.w3.org/2000/09/xmldsig#sha1"))
/*  91 */       return new SHA1(paramElement);
/*  92 */     if (str.equals("http://www.w3.org/2001/04/xmlenc#sha256"))
/*  93 */       return new SHA256(paramElement);
/*  94 */     if (str.equals("http://www.w3.org/2001/04/xmldsig-more#sha384"))
/*  95 */       return new SHA384(paramElement);
/*  96 */     if (str.equals("http://www.w3.org/2001/04/xmlenc#sha512")) {
/*  97 */       return new SHA512(paramElement);
/*     */     }
/*  99 */     throw new MarshalException("unsupported DigestMethod algorithm: " + str);
/*     */   }
/*     */ 
/*     */   void checkParams(DigestMethodParameterSpec paramDigestMethodParameterSpec)
/*     */     throws InvalidAlgorithmParameterException
/*     */   {
/* 116 */     if (paramDigestMethodParameterSpec != null)
/* 117 */       throw new InvalidAlgorithmParameterException("no parameters should be specified for the " + getMessageDigestAlgorithm() + " DigestMethod algorithm");
/*     */   }
/*     */ 
/*     */   public final AlgorithmParameterSpec getParameterSpec()
/*     */   {
/* 124 */     return this.params;
/*     */   }
/*     */ 
/*     */   DigestMethodParameterSpec unmarshalParams(Element paramElement)
/*     */     throws MarshalException
/*     */   {
/* 139 */     throw new MarshalException("no parameters should be specified for the " + getMessageDigestAlgorithm() + " DigestMethod algorithm");
/*     */   }
/*     */ 
/*     */   public void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext)
/*     */     throws MarshalException
/*     */   {
/* 150 */     Document localDocument = DOMUtils.getOwnerDocument(paramNode);
/*     */ 
/* 152 */     Element localElement = DOMUtils.createElement(localDocument, "DigestMethod", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 154 */     DOMUtils.setAttribute(localElement, "Algorithm", getAlgorithm());
/*     */ 
/* 156 */     if (this.params != null) {
/* 157 */       marshalParams(localElement, paramString);
/*     */     }
/*     */ 
/* 160 */     paramNode.appendChild(localElement);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 164 */     if (this == paramObject) {
/* 165 */       return true;
/*     */     }
/*     */ 
/* 168 */     if (!(paramObject instanceof DigestMethod)) {
/* 169 */       return false;
/*     */     }
/* 171 */     DigestMethod localDigestMethod = (DigestMethod)paramObject;
/*     */ 
/* 173 */     boolean bool = this.params == null ? false : localDigestMethod.getParameterSpec() == null ? true : this.params.equals(localDigestMethod.getParameterSpec());
/*     */ 
/* 176 */     return (getAlgorithm().equals(localDigestMethod.getAlgorithm())) && (bool);
/*     */   }
/*     */ 
/*     */   void marshalParams(Element paramElement, String paramString)
/*     */     throws MarshalException
/*     */   {
/* 191 */     throw new MarshalException("no parameters should be specified for the " + getMessageDigestAlgorithm() + " DigestMethod algorithm");
/*     */   }
/*     */ 
/*     */   abstract String getMessageDigestAlgorithm();
/*     */ 
/*     */   static final class SHA1 extends DOMDigestMethod
/*     */   {
/*     */     SHA1(AlgorithmParameterSpec paramAlgorithmParameterSpec)
/*     */       throws InvalidAlgorithmParameterException
/*     */     {
/* 204 */       super();
/*     */     }
/*     */     SHA1(Element paramElement) throws MarshalException {
/* 207 */       super();
/*     */     }
/*     */     public String getAlgorithm() {
/* 210 */       return "http://www.w3.org/2000/09/xmldsig#sha1";
/*     */     }
/*     */     String getMessageDigestAlgorithm() {
/* 213 */       return "SHA-1";
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class SHA256 extends DOMDigestMethod
/*     */   {
/*     */     SHA256(AlgorithmParameterSpec paramAlgorithmParameterSpec) throws InvalidAlgorithmParameterException {
/* 220 */       super();
/*     */     }
/*     */     SHA256(Element paramElement) throws MarshalException {
/* 223 */       super();
/*     */     }
/*     */     public String getAlgorithm() {
/* 226 */       return "http://www.w3.org/2001/04/xmlenc#sha256";
/*     */     }
/*     */     String getMessageDigestAlgorithm() {
/* 229 */       return "SHA-256";
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class SHA384 extends DOMDigestMethod
/*     */   {
/*     */     SHA384(AlgorithmParameterSpec paramAlgorithmParameterSpec) throws InvalidAlgorithmParameterException {
/* 236 */       super();
/*     */     }
/*     */     SHA384(Element paramElement) throws MarshalException {
/* 239 */       super();
/*     */     }
/*     */     public String getAlgorithm() {
/* 242 */       return "http://www.w3.org/2001/04/xmldsig-more#sha384";
/*     */     }
/*     */     String getMessageDigestAlgorithm() {
/* 245 */       return "SHA-384";
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class SHA512 extends DOMDigestMethod
/*     */   {
/*     */     SHA512(AlgorithmParameterSpec paramAlgorithmParameterSpec) throws InvalidAlgorithmParameterException {
/* 252 */       super();
/*     */     }
/*     */     SHA512(Element paramElement) throws MarshalException {
/* 255 */       super();
/*     */     }
/*     */     public String getAlgorithm() {
/* 258 */       return "http://www.w3.org/2001/04/xmlenc#sha512";
/*     */     }
/*     */     String getMessageDigestAlgorithm() {
/* 261 */       return "SHA-512";
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMDigestMethod
 * JD-Core Version:    0.6.2
 */