/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.security.signature.XMLSignatureInput;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.security.Provider;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.crypto.Data;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.NodeSetData;
/*     */ import javax.xml.crypto.URIDereferencer;
/*     */ import javax.xml.crypto.URIReferenceException;
/*     */ import javax.xml.crypto.XMLCryptoContext;
/*     */ import javax.xml.crypto.XMLStructure;
/*     */ import javax.xml.crypto.dom.DOMCryptoContext;
/*     */ import javax.xml.crypto.dom.DOMURIReference;
/*     */ import javax.xml.crypto.dsig.Transform;
/*     */ import javax.xml.crypto.dsig.keyinfo.RetrievalMethod;
/*     */ import javax.xml.parsers.DocumentBuilder;
/*     */ import javax.xml.parsers.DocumentBuilderFactory;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public final class DOMRetrievalMethod extends DOMStructure
/*     */   implements RetrievalMethod, DOMURIReference
/*     */ {
/*     */   private final List transforms;
/*     */   private String uri;
/*     */   private String type;
/*     */   private Attr here;
/*     */ 
/*     */   public DOMRetrievalMethod(String paramString1, String paramString2, List paramList)
/*     */   {
/*  87 */     if (paramString1 == null) {
/*  88 */       throw new NullPointerException("uri cannot be null");
/*     */     }
/*  90 */     if ((paramList == null) || (paramList.isEmpty())) {
/*  91 */       this.transforms = Collections.EMPTY_LIST;
/*     */     } else {
/*  93 */       ArrayList localArrayList = new ArrayList(paramList);
/*  94 */       int i = 0; for (int j = localArrayList.size(); i < j; i++) {
/*  95 */         if (!(localArrayList.get(i) instanceof Transform)) {
/*  96 */           throw new ClassCastException("transforms[" + i + "] is not a valid type");
/*     */         }
/*     */       }
/*     */ 
/* 100 */       this.transforms = Collections.unmodifiableList(localArrayList);
/*     */     }
/* 102 */     this.uri = paramString1;
/* 103 */     if ((paramString1 != null) && (!paramString1.equals(""))) {
/*     */       try {
/* 105 */         new URI(paramString1);
/*     */       } catch (URISyntaxException localURISyntaxException) {
/* 107 */         throw new IllegalArgumentException(localURISyntaxException.getMessage());
/*     */       }
/*     */     }
/*     */ 
/* 111 */     this.type = paramString2;
/*     */   }
/*     */ 
/*     */   public DOMRetrievalMethod(Element paramElement, XMLCryptoContext paramXMLCryptoContext, Provider paramProvider)
/*     */     throws MarshalException
/*     */   {
/* 122 */     this.uri = DOMUtils.getAttributeValue(paramElement, "URI");
/* 123 */     this.type = DOMUtils.getAttributeValue(paramElement, "Type");
/*     */ 
/* 126 */     this.here = paramElement.getAttributeNodeNS(null, "URI");
/*     */ 
/* 128 */     boolean bool = Utils.secureValidation(paramXMLCryptoContext);
/*     */ 
/* 131 */     ArrayList localArrayList = new ArrayList();
/* 132 */     Element localElement1 = DOMUtils.getFirstChildElement(paramElement);
/*     */ 
/* 134 */     int i = 0;
/* 135 */     if (localElement1 != null) {
/* 136 */       Element localElement2 = DOMUtils.getFirstChildElement(localElement1);
/*     */ 
/* 138 */       while (localElement2 != null) {
/* 139 */         localArrayList.add(new DOMTransform(localElement2, paramXMLCryptoContext, paramProvider));
/*     */ 
/* 141 */         localElement2 = DOMUtils.getNextSiblingElement(localElement2);
/*     */ 
/* 143 */         i++;
/* 144 */         if ((bool) && (i > 5))
/*     */         {
/* 147 */           String str = "A maxiumum of 5 transforms per Reference are allowed with secure validation";
/*     */ 
/* 151 */           throw new MarshalException(str);
/*     */         }
/*     */       }
/*     */     }
/* 155 */     if (localArrayList.isEmpty())
/* 156 */       this.transforms = Collections.EMPTY_LIST;
/*     */     else
/* 158 */       this.transforms = Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   public String getURI()
/*     */   {
/* 163 */     return this.uri;
/*     */   }
/*     */ 
/*     */   public String getType() {
/* 167 */     return this.type;
/*     */   }
/*     */ 
/*     */   public List getTransforms() {
/* 171 */     return this.transforms;
/*     */   }
/*     */ 
/*     */   public void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext) throws MarshalException
/*     */   {
/* 176 */     Document localDocument = DOMUtils.getOwnerDocument(paramNode);
/*     */ 
/* 178 */     Element localElement1 = DOMUtils.createElement(localDocument, "RetrievalMethod", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 182 */     DOMUtils.setAttribute(localElement1, "URI", this.uri);
/* 183 */     DOMUtils.setAttribute(localElement1, "Type", this.type);
/*     */ 
/* 186 */     if (!this.transforms.isEmpty()) {
/* 187 */       Element localElement2 = DOMUtils.createElement(localDocument, "Transforms", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 189 */       localElement1.appendChild(localElement2);
/* 190 */       int i = 0; for (int j = this.transforms.size(); i < j; i++) {
/* 191 */         ((DOMTransform)this.transforms.get(i)).marshal(localElement2, paramString, paramDOMCryptoContext);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 196 */     paramNode.appendChild(localElement1);
/*     */ 
/* 199 */     this.here = localElement1.getAttributeNodeNS(null, "URI");
/*     */   }
/*     */ 
/*     */   public Node getHere() {
/* 203 */     return this.here;
/*     */   }
/*     */ 
/*     */   public Data dereference(XMLCryptoContext paramXMLCryptoContext)
/*     */     throws URIReferenceException
/*     */   {
/* 209 */     if (paramXMLCryptoContext == null) {
/* 210 */       throw new NullPointerException("context cannot be null");
/*     */     }
/*     */ 
/* 217 */     URIDereferencer localURIDereferencer = paramXMLCryptoContext.getURIDereferencer();
/* 218 */     if (localURIDereferencer == null) {
/* 219 */       localURIDereferencer = DOMURIDereferencer.INSTANCE;
/*     */     }
/*     */ 
/* 222 */     Data localData = localURIDereferencer.dereference(this, paramXMLCryptoContext);
/*     */     Object localObject;
/*     */     try {
/* 226 */       int i = 0; for (int j = this.transforms.size(); i < j; i++) {
/* 227 */         localObject = (Transform)this.transforms.get(i);
/* 228 */         localData = ((DOMTransform)localObject).transform(localData, paramXMLCryptoContext);
/*     */       }
/*     */     } catch (Exception localException) {
/* 231 */       throw new URIReferenceException(localException);
/*     */     }
/*     */ 
/* 235 */     if (((localData instanceof NodeSetData)) && (Utils.secureValidation(paramXMLCryptoContext))) {
/* 236 */       NodeSetData localNodeSetData = (NodeSetData)localData;
/* 237 */       Iterator localIterator = localNodeSetData.iterator();
/* 238 */       if (localIterator.hasNext()) {
/* 239 */         localObject = (Node)localIterator.next();
/* 240 */         if ("RetrievalMethod".equals(((Node)localObject).getLocalName())) {
/* 241 */           throw new URIReferenceException("It is forbidden to have one RetrievalMethod point to another when secure validation is enabled");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 248 */     return localData;
/*     */   }
/*     */ 
/*     */   public XMLStructure dereferenceAsXMLStructure(XMLCryptoContext paramXMLCryptoContext) throws URIReferenceException
/*     */   {
/*     */     try
/*     */     {
/* 255 */       ApacheData localApacheData = (ApacheData)dereference(paramXMLCryptoContext);
/* 256 */       DocumentBuilderFactory localDocumentBuilderFactory = DocumentBuilderFactory.newInstance();
/* 257 */       localDocumentBuilderFactory.setNamespaceAware(true);
/* 258 */       localDocumentBuilderFactory.setFeature("http://javax.xml.XMLConstants/feature/secure-processing", Boolean.TRUE.booleanValue());
/*     */ 
/* 260 */       DocumentBuilder localDocumentBuilder = localDocumentBuilderFactory.newDocumentBuilder();
/* 261 */       Document localDocument = localDocumentBuilder.parse(new ByteArrayInputStream(localApacheData.getXMLSignatureInput().getBytes()));
/*     */ 
/* 263 */       Element localElement = localDocument.getDocumentElement();
/* 264 */       if (localElement.getLocalName().equals("X509Data")) {
/* 265 */         return new DOMX509Data(localElement);
/*     */       }
/* 267 */       return null;
/*     */     }
/*     */     catch (Exception localException) {
/* 270 */       throw new URIReferenceException(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 275 */     if (this == paramObject) {
/* 276 */       return true;
/*     */     }
/* 278 */     if (!(paramObject instanceof DOMURIReference)) {
/* 279 */       return false;
/*     */     }
/* 281 */     RetrievalMethod localRetrievalMethod = (DOMURIReference)paramObject;
/*     */ 
/* 283 */     boolean bool = this.type == null ? false : localRetrievalMethod.getType() == null ? true : this.type.equals(localRetrievalMethod.getType());
/*     */ 
/* 286 */     return (this.uri.equals(localRetrievalMethod.getURI())) && (this.transforms.equals(localRetrievalMethod.getTransforms())) && (bool);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMRetrievalMethod
 * JD-Core Version:    0.6.2
 */