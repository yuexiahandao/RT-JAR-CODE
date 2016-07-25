/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.utils.Base64;
/*     */ import com.sun.org.apache.xml.internal.security.utils.UnsyncBufferedOutputStream;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.security.Provider;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.crypto.Data;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.XMLCryptoContext;
/*     */ import javax.xml.crypto.dom.DOMCryptoContext;
/*     */ import javax.xml.crypto.dsig.CanonicalizationMethod;
/*     */ import javax.xml.crypto.dsig.Reference;
/*     */ import javax.xml.crypto.dsig.SignatureMethod;
/*     */ import javax.xml.crypto.dsig.SignedInfo;
/*     */ import javax.xml.crypto.dsig.TransformException;
/*     */ import javax.xml.crypto.dsig.XMLSignatureException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public final class DOMSignedInfo extends DOMStructure
/*     */   implements SignedInfo
/*     */ {
/*     */   public static final int MAXIMUM_REFERENCE_COUNT = 30;
/*  65 */   private static Logger log = Logger.getLogger("org.jcp.xml.dsig.internal.dom");
/*     */   private static final String ALGO_ID_SIGNATURE_NOT_RECOMMENDED_RSA_MD5 = "http://www.w3.org/2001/04/xmldsig-more#rsa-md5";
/*     */   private static final String ALGO_ID_MAC_HMAC_NOT_RECOMMENDED_MD5 = "http://www.w3.org/2001/04/xmldsig-more#hmac-md5";
/*     */   private List references;
/*     */   private CanonicalizationMethod canonicalizationMethod;
/*     */   private SignatureMethod signatureMethod;
/*     */   private String id;
/*     */   private Document ownerDoc;
/*     */   private Element localSiElem;
/*     */   private InputStream canonData;
/*     */ 
/*     */   public DOMSignedInfo(CanonicalizationMethod paramCanonicalizationMethod, SignatureMethod paramSignatureMethod, List paramList)
/*     */   {
/*  99 */     if ((paramCanonicalizationMethod == null) || (paramSignatureMethod == null) || (paramList == null)) {
/* 100 */       throw new NullPointerException();
/*     */     }
/* 102 */     this.canonicalizationMethod = paramCanonicalizationMethod;
/* 103 */     this.signatureMethod = paramSignatureMethod;
/* 104 */     this.references = Collections.unmodifiableList(new ArrayList(paramList));
/*     */ 
/* 106 */     if (this.references.isEmpty()) {
/* 107 */       throw new IllegalArgumentException("list of references must contain at least one entry");
/*     */     }
/*     */ 
/* 110 */     int i = 0; for (int j = this.references.size(); i < j; i++) {
/* 111 */       Object localObject = this.references.get(i);
/* 112 */       if (!(localObject instanceof Reference))
/* 113 */         throw new ClassCastException("list of references contains an illegal type");
/*     */     }
/*     */   }
/*     */ 
/*     */   public DOMSignedInfo(CanonicalizationMethod paramCanonicalizationMethod, SignatureMethod paramSignatureMethod, List paramList, String paramString)
/*     */   {
/* 136 */     this(paramCanonicalizationMethod, paramSignatureMethod, paramList);
/* 137 */     this.id = paramString;
/*     */   }
/*     */ 
/*     */   public DOMSignedInfo(Element paramElement, XMLCryptoContext paramXMLCryptoContext, Provider paramProvider)
/*     */     throws MarshalException
/*     */   {
/* 147 */     this.localSiElem = paramElement;
/* 148 */     this.ownerDoc = paramElement.getOwnerDocument();
/*     */ 
/* 151 */     this.id = DOMUtils.getAttributeValue(paramElement, "Id");
/*     */ 
/* 154 */     Element localElement1 = DOMUtils.getFirstChildElement(paramElement);
/* 155 */     this.canonicalizationMethod = new DOMCanonicalizationMethod(localElement1, paramXMLCryptoContext, paramProvider);
/*     */ 
/* 159 */     Element localElement2 = DOMUtils.getNextSiblingElement(localElement1);
/* 160 */     this.signatureMethod = DOMSignatureMethod.unmarshal(localElement2);
/*     */ 
/* 162 */     boolean bool = Utils.secureValidation(paramXMLCryptoContext);
/* 163 */     String str1 = this.signatureMethod.getAlgorithm();
/* 164 */     if ((bool) && (("http://www.w3.org/2001/04/xmldsig-more#hmac-md5".equals(str1)) || ("http://www.w3.org/2001/04/xmldsig-more#rsa-md5".equals(str1))))
/*     */     {
/* 167 */       throw new MarshalException("It is forbidden to use algorithm " + this.signatureMethod + " when secure validation is enabled");
/*     */     }
/*     */ 
/* 173 */     ArrayList localArrayList = new ArrayList(5);
/* 174 */     Element localElement3 = DOMUtils.getNextSiblingElement(localElement2);
/* 175 */     int i = 0;
/* 176 */     while (localElement3 != null) {
/* 177 */       localArrayList.add(new DOMReference(localElement3, paramXMLCryptoContext, paramProvider));
/* 178 */       localElement3 = DOMUtils.getNextSiblingElement(localElement3);
/*     */ 
/* 180 */       i++;
/* 181 */       if ((bool) && (i > 30)) {
/* 182 */         String str2 = "A maxiumum of 30 references per SignedInfo are allowed with secure validation";
/*     */ 
/* 185 */         throw new MarshalException(str2);
/*     */       }
/*     */     }
/* 188 */     this.references = Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   public CanonicalizationMethod getCanonicalizationMethod() {
/* 192 */     return this.canonicalizationMethod;
/*     */   }
/*     */ 
/*     */   public SignatureMethod getSignatureMethod() {
/* 196 */     return this.signatureMethod;
/*     */   }
/*     */ 
/*     */   public String getId() {
/* 200 */     return this.id;
/*     */   }
/*     */ 
/*     */   public List getReferences() {
/* 204 */     return this.references;
/*     */   }
/*     */ 
/*     */   public InputStream getCanonicalizedData() {
/* 208 */     return this.canonData;
/*     */   }
/*     */ 
/*     */   public void canonicalize(XMLCryptoContext paramXMLCryptoContext, ByteArrayOutputStream paramByteArrayOutputStream)
/*     */     throws XMLSignatureException
/*     */   {
/* 214 */     if (paramXMLCryptoContext == null) {
/* 215 */       throw new NullPointerException("context cannot be null");
/*     */     }
/*     */ 
/* 218 */     UnsyncBufferedOutputStream localUnsyncBufferedOutputStream = new UnsyncBufferedOutputStream(paramByteArrayOutputStream);
/*     */     try {
/* 220 */       localUnsyncBufferedOutputStream.close();
/*     */     }
/*     */     catch (IOException localIOException1)
/*     */     {
/*     */     }
/* 225 */     DOMSubTreeData localDOMSubTreeData = new DOMSubTreeData(this.localSiElem, true);
/*     */     try
/*     */     {
/* 228 */       Data localData = ((DOMCanonicalizationMethod)this.canonicalizationMethod).canonicalize(localDOMSubTreeData, paramXMLCryptoContext, localUnsyncBufferedOutputStream);
/*     */     }
/*     */     catch (TransformException localTransformException) {
/* 231 */       throw new XMLSignatureException(localTransformException);
/*     */     }
/*     */ 
/* 234 */     byte[] arrayOfByte = paramByteArrayOutputStream.toByteArray();
/*     */ 
/* 237 */     if (log.isLoggable(Level.FINE)) {
/* 238 */       InputStreamReader localInputStreamReader = new InputStreamReader(new ByteArrayInputStream(arrayOfByte));
/*     */ 
/* 240 */       char[] arrayOfChar = new char[arrayOfByte.length];
/*     */       try {
/* 242 */         localInputStreamReader.read(arrayOfChar);
/* 243 */         log.log(Level.FINE, "Canonicalized SignedInfo:\n" + new String(arrayOfChar));
/*     */       }
/*     */       catch (IOException localIOException2) {
/* 246 */         log.log(Level.FINE, "IOException reading SignedInfo bytes");
/*     */       }
/* 248 */       log.log(Level.FINE, "Data to be signed/verified:" + Base64.encode(arrayOfByte));
/*     */     }
/*     */ 
/* 252 */     this.canonData = new ByteArrayInputStream(arrayOfByte);
/*     */   }
/*     */ 
/*     */   public void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext) throws MarshalException
/*     */   {
/* 257 */     this.ownerDoc = DOMUtils.getOwnerDocument(paramNode);
/*     */ 
/* 259 */     Element localElement = DOMUtils.createElement(this.ownerDoc, "SignedInfo", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 263 */     DOMCanonicalizationMethod localDOMCanonicalizationMethod = (DOMCanonicalizationMethod)this.canonicalizationMethod;
/*     */ 
/* 265 */     localDOMCanonicalizationMethod.marshal(localElement, paramString, paramDOMCryptoContext);
/*     */ 
/* 268 */     ((DOMSignatureMethod)this.signatureMethod).marshal(localElement, paramString, paramDOMCryptoContext);
/*     */ 
/* 272 */     int i = 0; for (int j = this.references.size(); i < j; i++) {
/* 273 */       DOMReference localDOMReference = (DOMReference)this.references.get(i);
/* 274 */       localDOMReference.marshal(localElement, paramString, paramDOMCryptoContext);
/*     */     }
/*     */ 
/* 278 */     DOMUtils.setAttributeID(localElement, "Id", this.id);
/*     */ 
/* 280 */     paramNode.appendChild(localElement);
/* 281 */     this.localSiElem = localElement;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 285 */     if (this == paramObject) {
/* 286 */       return true;
/*     */     }
/*     */ 
/* 289 */     if (!(paramObject instanceof SignedInfo)) {
/* 290 */       return false;
/*     */     }
/* 292 */     SignedInfo localSignedInfo = (SignedInfo)paramObject;
/*     */ 
/* 294 */     boolean bool = this.id == null ? false : localSignedInfo.getId() == null ? true : this.id.equals(localSignedInfo.getId());
/*     */ 
/* 297 */     return (this.canonicalizationMethod.equals(localSignedInfo.getCanonicalizationMethod())) && (this.signatureMethod.equals(localSignedInfo.getSignatureMethod())) && (this.references.equals(localSignedInfo.getReferences())) && (bool);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMSignedInfo
 * JD-Core Version:    0.6.2
 */