/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import java.security.KeyException;
/*     */ import java.security.KeyFactory;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ import java.security.PublicKey;
/*     */ import java.security.interfaces.DSAParams;
/*     */ import java.security.interfaces.DSAPublicKey;
/*     */ import java.security.interfaces.RSAPublicKey;
/*     */ import java.security.spec.DSAPublicKeySpec;
/*     */ import java.security.spec.InvalidKeySpecException;
/*     */ import java.security.spec.KeySpec;
/*     */ import java.security.spec.RSAPublicKeySpec;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.dom.DOMCryptoContext;
/*     */ import javax.xml.crypto.dsig.keyinfo.KeyValue;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public final class DOMKeyValue extends DOMStructure
/*     */   implements KeyValue
/*     */ {
/*     */   private KeyFactory rsakf;
/*     */   private KeyFactory dsakf;
/*     */   private PublicKey publicKey;
/*     */   private javax.xml.crypto.dom.DOMStructure externalPublicKey;
/*     */   private DOMCryptoBinary p;
/*     */   private DOMCryptoBinary q;
/*     */   private DOMCryptoBinary g;
/*     */   private DOMCryptoBinary y;
/*     */   private DOMCryptoBinary j;
/*     */   private DOMCryptoBinary seed;
/*     */   private DOMCryptoBinary pgen;
/*     */   private DOMCryptoBinary modulus;
/*     */   private DOMCryptoBinary exponent;
/*     */ 
/*     */   public DOMKeyValue(PublicKey paramPublicKey)
/*     */     throws KeyException
/*     */   {
/*  67 */     if (paramPublicKey == null) {
/*  68 */       throw new NullPointerException("key cannot be null");
/*     */     }
/*  70 */     this.publicKey = paramPublicKey;
/*     */     Object localObject;
/*  71 */     if ((paramPublicKey instanceof DSAPublicKey)) {
/*  72 */       localObject = (DSAPublicKey)paramPublicKey;
/*  73 */       DSAParams localDSAParams = ((DSAPublicKey)localObject).getParams();
/*  74 */       this.p = new DOMCryptoBinary(localDSAParams.getP());
/*  75 */       this.q = new DOMCryptoBinary(localDSAParams.getQ());
/*  76 */       this.g = new DOMCryptoBinary(localDSAParams.getG());
/*  77 */       this.y = new DOMCryptoBinary(((DSAPublicKey)localObject).getY());
/*  78 */     } else if ((paramPublicKey instanceof RSAPublicKey)) {
/*  79 */       localObject = (RSAPublicKey)paramPublicKey;
/*  80 */       this.exponent = new DOMCryptoBinary(((RSAPublicKey)localObject).getPublicExponent());
/*  81 */       this.modulus = new DOMCryptoBinary(((RSAPublicKey)localObject).getModulus());
/*     */     } else {
/*  83 */       throw new KeyException("unsupported key algorithm: " + paramPublicKey.getAlgorithm());
/*     */     }
/*     */   }
/*     */ 
/*     */   public DOMKeyValue(Element paramElement)
/*     */     throws MarshalException
/*     */   {
/*  94 */     Element localElement = DOMUtils.getFirstChildElement(paramElement);
/*  95 */     if (localElement.getLocalName().equals("DSAKeyValue")) {
/*  96 */       this.publicKey = unmarshalDSAKeyValue(localElement);
/*  97 */     } else if (localElement.getLocalName().equals("RSAKeyValue")) {
/*  98 */       this.publicKey = unmarshalRSAKeyValue(localElement);
/*     */     } else {
/* 100 */       this.publicKey = null;
/* 101 */       this.externalPublicKey = new javax.xml.crypto.dom.DOMStructure(localElement);
/*     */     }
/*     */   }
/*     */ 
/*     */   public PublicKey getPublicKey() throws KeyException {
/* 106 */     if (this.publicKey == null) {
/* 107 */       throw new KeyException("can't convert KeyValue to PublicKey");
/*     */     }
/* 109 */     return this.publicKey;
/*     */   }
/*     */ 
/*     */   public void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext)
/*     */     throws MarshalException
/*     */   {
/* 115 */     Document localDocument = DOMUtils.getOwnerDocument(paramNode);
/*     */ 
/* 118 */     Element localElement = DOMUtils.createElement(localDocument, "KeyValue", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 120 */     marshalPublicKey(localElement, localDocument, paramString, paramDOMCryptoContext);
/*     */ 
/* 122 */     paramNode.appendChild(localElement);
/*     */   }
/*     */ 
/*     */   private void marshalPublicKey(Node paramNode, Document paramDocument, String paramString, DOMCryptoContext paramDOMCryptoContext) throws MarshalException
/*     */   {
/* 127 */     if (this.publicKey != null) {
/* 128 */       if ((this.publicKey instanceof DSAPublicKey))
/*     */       {
/* 130 */         marshalDSAPublicKey(paramNode, paramDocument, paramString, paramDOMCryptoContext);
/* 131 */       } else if ((this.publicKey instanceof RSAPublicKey))
/*     */       {
/* 133 */         marshalRSAPublicKey(paramNode, paramDocument, paramString, paramDOMCryptoContext);
/*     */       }
/* 135 */       else throw new MarshalException(this.publicKey.getAlgorithm() + " public key algorithm not supported");
/*     */     }
/*     */     else
/*     */     {
/* 139 */       paramNode.appendChild(this.externalPublicKey.getNode());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void marshalDSAPublicKey(Node paramNode, Document paramDocument, String paramString, DOMCryptoContext paramDOMCryptoContext) throws MarshalException
/*     */   {
/* 145 */     Element localElement1 = DOMUtils.createElement(paramDocument, "DSAKeyValue", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 148 */     Element localElement2 = DOMUtils.createElement(paramDocument, "P", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 150 */     Element localElement3 = DOMUtils.createElement(paramDocument, "Q", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 152 */     Element localElement4 = DOMUtils.createElement(paramDocument, "G", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 154 */     Element localElement5 = DOMUtils.createElement(paramDocument, "Y", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 156 */     this.p.marshal(localElement2, paramString, paramDOMCryptoContext);
/* 157 */     this.q.marshal(localElement3, paramString, paramDOMCryptoContext);
/* 158 */     this.g.marshal(localElement4, paramString, paramDOMCryptoContext);
/* 159 */     this.y.marshal(localElement5, paramString, paramDOMCryptoContext);
/* 160 */     localElement1.appendChild(localElement2);
/* 161 */     localElement1.appendChild(localElement3);
/* 162 */     localElement1.appendChild(localElement4);
/* 163 */     localElement1.appendChild(localElement5);
/* 164 */     paramNode.appendChild(localElement1);
/*     */   }
/*     */ 
/*     */   private void marshalRSAPublicKey(Node paramNode, Document paramDocument, String paramString, DOMCryptoContext paramDOMCryptoContext) throws MarshalException
/*     */   {
/* 169 */     Element localElement1 = DOMUtils.createElement(paramDocument, "RSAKeyValue", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 171 */     Element localElement2 = DOMUtils.createElement(paramDocument, "Modulus", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 173 */     Element localElement3 = DOMUtils.createElement(paramDocument, "Exponent", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 175 */     this.modulus.marshal(localElement2, paramString, paramDOMCryptoContext);
/* 176 */     this.exponent.marshal(localElement3, paramString, paramDOMCryptoContext);
/* 177 */     localElement1.appendChild(localElement2);
/* 178 */     localElement1.appendChild(localElement3);
/* 179 */     paramNode.appendChild(localElement1);
/*     */   }
/*     */ 
/*     */   private DSAPublicKey unmarshalDSAKeyValue(Element paramElement) throws MarshalException
/*     */   {
/* 184 */     if (this.dsakf == null) {
/*     */       try {
/* 186 */         this.dsakf = KeyFactory.getInstance("DSA");
/*     */       } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 188 */         throw new RuntimeException("unable to create DSA KeyFactory: " + localNoSuchAlgorithmException.getMessage());
/*     */       }
/*     */     }
/*     */ 
/* 192 */     Element localElement = DOMUtils.getFirstChildElement(paramElement);
/*     */ 
/* 194 */     if (localElement.getLocalName().equals("P")) {
/* 195 */       this.p = new DOMCryptoBinary(localElement.getFirstChild());
/* 196 */       localElement = DOMUtils.getNextSiblingElement(localElement);
/* 197 */       this.q = new DOMCryptoBinary(localElement.getFirstChild());
/* 198 */       localElement = DOMUtils.getNextSiblingElement(localElement);
/*     */     }
/* 200 */     if (localElement.getLocalName().equals("G")) {
/* 201 */       this.g = new DOMCryptoBinary(localElement.getFirstChild());
/* 202 */       localElement = DOMUtils.getNextSiblingElement(localElement);
/*     */     }
/* 204 */     this.y = new DOMCryptoBinary(localElement.getFirstChild());
/* 205 */     localElement = DOMUtils.getNextSiblingElement(localElement);
/* 206 */     if ((localElement != null) && (localElement.getLocalName().equals("J"))) {
/* 207 */       this.j = new DOMCryptoBinary(localElement.getFirstChild());
/* 208 */       localElement = DOMUtils.getNextSiblingElement(localElement);
/*     */     }
/* 210 */     if (localElement != null) {
/* 211 */       this.seed = new DOMCryptoBinary(localElement.getFirstChild());
/* 212 */       localElement = DOMUtils.getNextSiblingElement(localElement);
/* 213 */       this.pgen = new DOMCryptoBinary(localElement.getFirstChild());
/*     */     }
/*     */ 
/* 216 */     DSAPublicKeySpec localDSAPublicKeySpec = new DSAPublicKeySpec(this.y.getBigNum(), this.p.getBigNum(), this.q.getBigNum(), this.g.getBigNum());
/*     */ 
/* 218 */     return (DSAPublicKey)generatePublicKey(this.dsakf, localDSAPublicKeySpec);
/*     */   }
/*     */ 
/*     */   private RSAPublicKey unmarshalRSAKeyValue(Element paramElement) throws MarshalException
/*     */   {
/* 223 */     if (this.rsakf == null) {
/*     */       try {
/* 225 */         this.rsakf = KeyFactory.getInstance("RSA");
/*     */       } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 227 */         throw new RuntimeException("unable to create RSA KeyFactory: " + localNoSuchAlgorithmException.getMessage());
/*     */       }
/*     */     }
/*     */ 
/* 231 */     Element localElement1 = DOMUtils.getFirstChildElement(paramElement);
/* 232 */     this.modulus = new DOMCryptoBinary(localElement1.getFirstChild());
/* 233 */     Element localElement2 = DOMUtils.getNextSiblingElement(localElement1);
/* 234 */     this.exponent = new DOMCryptoBinary(localElement2.getFirstChild());
/* 235 */     RSAPublicKeySpec localRSAPublicKeySpec = new RSAPublicKeySpec(this.modulus.getBigNum(), this.exponent.getBigNum());
/*     */ 
/* 237 */     return (RSAPublicKey)generatePublicKey(this.rsakf, localRSAPublicKeySpec);
/*     */   }
/*     */ 
/*     */   private PublicKey generatePublicKey(KeyFactory paramKeyFactory, KeySpec paramKeySpec) {
/*     */     try {
/* 242 */       return paramKeyFactory.generatePublic(paramKeySpec);
/*     */     } catch (InvalidKeySpecException localInvalidKeySpecException) {
/*     */     }
/* 245 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 250 */     if (this == paramObject) {
/* 251 */       return true;
/*     */     }
/* 253 */     if (!(paramObject instanceof KeyValue))
/* 254 */       return false;
/*     */     try
/*     */     {
/* 257 */       KeyValue localKeyValue = (KeyValue)paramObject;
/* 258 */       if (this.publicKey == null) {
/* 259 */         if (localKeyValue.getPublicKey() != null)
/* 260 */           return false;
/*     */       }
/* 262 */       else if (!this.publicKey.equals(localKeyValue.getPublicKey()))
/* 263 */         return false;
/*     */     }
/*     */     catch (KeyException localKeyException)
/*     */     {
/* 267 */       return false;
/*     */     }
/*     */ 
/* 270 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMKeyValue
 * JD-Core Version:    0.6.2
 */