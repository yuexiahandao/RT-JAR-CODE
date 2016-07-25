/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import java.security.Provider;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.XMLCryptoContext;
/*     */ import javax.xml.crypto.XMLStructure;
/*     */ import javax.xml.crypto.dom.DOMCryptoContext;
/*     */ import javax.xml.crypto.dsig.XMLObject;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public final class DOMXMLObject extends DOMStructure
/*     */   implements XMLObject
/*     */ {
/*     */   private final String id;
/*     */   private final String mimeType;
/*     */   private final String encoding;
/*     */   private final List content;
/*     */ 
/*     */   public DOMXMLObject(List paramList, String paramString1, String paramString2, String paramString3)
/*     */   {
/*  68 */     if ((paramList == null) || (paramList.isEmpty())) {
/*  69 */       this.content = Collections.EMPTY_LIST;
/*     */     } else {
/*  71 */       ArrayList localArrayList = new ArrayList(paramList);
/*  72 */       int i = 0; for (int j = localArrayList.size(); i < j; i++) {
/*  73 */         if (!(localArrayList.get(i) instanceof XMLStructure)) {
/*  74 */           throw new ClassCastException("content[" + i + "] is not a valid type");
/*     */         }
/*     */       }
/*     */ 
/*  78 */       this.content = Collections.unmodifiableList(localArrayList);
/*     */     }
/*  80 */     this.id = paramString1;
/*  81 */     this.mimeType = paramString2;
/*  82 */     this.encoding = paramString3;
/*     */   }
/*     */ 
/*     */   public DOMXMLObject(Element paramElement, XMLCryptoContext paramXMLCryptoContext, Provider paramProvider)
/*     */     throws MarshalException
/*     */   {
/*  94 */     this.encoding = DOMUtils.getAttributeValue(paramElement, "Encoding");
/*     */ 
/*  96 */     Attr localAttr = paramElement.getAttributeNodeNS(null, "Id");
/*  97 */     if (localAttr != null) {
/*  98 */       this.id = localAttr.getValue();
/*  99 */       paramElement.setIdAttributeNode(localAttr, true);
/*     */     } else {
/* 101 */       this.id = null;
/*     */     }
/* 103 */     this.mimeType = DOMUtils.getAttributeValue(paramElement, "MimeType");
/*     */ 
/* 105 */     NodeList localNodeList = paramElement.getChildNodes();
/* 106 */     int i = localNodeList.getLength();
/* 107 */     ArrayList localArrayList = new ArrayList(i);
/* 108 */     for (int j = 0; j < i; j++) {
/* 109 */       Node localNode = localNodeList.item(j);
/* 110 */       if (localNode.getNodeType() == 1) {
/* 111 */         Element localElement = (Element)localNode;
/* 112 */         String str = localElement.getLocalName();
/* 113 */         if (str.equals("Manifest")) {
/* 114 */           localArrayList.add(new DOMManifest(localElement, paramXMLCryptoContext, paramProvider));
/* 115 */           continue;
/* 116 */         }if (str.equals("SignatureProperties")) {
/* 117 */           localArrayList.add(new DOMSignatureProperties(localElement));
/* 118 */           continue;
/* 119 */         }if (str.equals("X509Data")) {
/* 120 */           localArrayList.add(new DOMX509Data(localElement));
/* 121 */           continue;
/*     */         }
/*     */       }
/*     */ 
/* 125 */       localArrayList.add(new javax.xml.crypto.dom.DOMStructure(localNode));
/*     */     }
/* 127 */     if (localArrayList.isEmpty())
/* 128 */       this.content = Collections.EMPTY_LIST;
/*     */     else
/* 130 */       this.content = Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   public List getContent()
/*     */   {
/* 135 */     return this.content;
/*     */   }
/*     */ 
/*     */   public String getId() {
/* 139 */     return this.id;
/*     */   }
/*     */ 
/*     */   public String getMimeType() {
/* 143 */     return this.mimeType;
/*     */   }
/*     */ 
/*     */   public String getEncoding() {
/* 147 */     return this.encoding;
/*     */   }
/*     */ 
/*     */   public void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext) throws MarshalException
/*     */   {
/* 152 */     Document localDocument = DOMUtils.getOwnerDocument(paramNode);
/*     */ 
/* 154 */     Element localElement = DOMUtils.createElement(localDocument, "Object", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 158 */     DOMUtils.setAttributeID(localElement, "Id", this.id);
/* 159 */     DOMUtils.setAttribute(localElement, "MimeType", this.mimeType);
/* 160 */     DOMUtils.setAttribute(localElement, "Encoding", this.encoding);
/*     */ 
/* 163 */     int i = 0; for (int j = this.content.size(); i < j; i++) {
/* 164 */       XMLStructure localXMLStructure = (XMLStructure)this.content.get(i);
/* 165 */       if ((localXMLStructure instanceof DOMStructure)) {
/* 166 */         ((DOMStructure)localXMLStructure).marshal(localElement, paramString, paramDOMCryptoContext);
/*     */       } else {
/* 168 */         javax.xml.crypto.dom.DOMStructure localDOMStructure = (javax.xml.crypto.dom.DOMStructure)localXMLStructure;
/*     */ 
/* 170 */         DOMUtils.appendChild(localElement, localDOMStructure.getNode());
/*     */       }
/*     */     }
/*     */ 
/* 174 */     paramNode.appendChild(localElement);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 178 */     if (this == paramObject) {
/* 179 */       return true;
/*     */     }
/*     */ 
/* 182 */     if (!(paramObject instanceof XMLObject)) {
/* 183 */       return false;
/*     */     }
/* 185 */     XMLObject localXMLObject = (XMLObject)paramObject;
/*     */ 
/* 187 */     boolean bool1 = this.id == null ? false : localXMLObject.getId() == null ? true : this.id.equals(localXMLObject.getId());
/*     */ 
/* 189 */     boolean bool2 = this.encoding == null ? false : localXMLObject.getEncoding() == null ? true : this.encoding.equals(localXMLObject.getEncoding());
/*     */ 
/* 191 */     boolean bool3 = this.mimeType == null ? false : localXMLObject.getMimeType() == null ? true : this.mimeType.equals(localXMLObject.getMimeType());
/*     */ 
/* 194 */     return (bool1) && (bool2) && (bool3) && (equalsContent(localXMLObject.getContent()));
/*     */   }
/*     */ 
/*     */   private boolean equalsContent(List paramList)
/*     */   {
/* 199 */     if (this.content.size() != paramList.size()) {
/* 200 */       return false;
/*     */     }
/* 202 */     int i = 0; for (int j = paramList.size(); i < j; i++) {
/* 203 */       XMLStructure localXMLStructure1 = (XMLStructure)paramList.get(i);
/* 204 */       XMLStructure localXMLStructure2 = (XMLStructure)this.content.get(i);
/* 205 */       if ((localXMLStructure1 instanceof javax.xml.crypto.dom.DOMStructure)) {
/* 206 */         if (!(localXMLStructure2 instanceof javax.xml.crypto.dom.DOMStructure)) {
/* 207 */           return false;
/*     */         }
/* 209 */         Node localNode1 = ((javax.xml.crypto.dom.DOMStructure)localXMLStructure1).getNode();
/*     */ 
/* 211 */         Node localNode2 = ((javax.xml.crypto.dom.DOMStructure)localXMLStructure2).getNode();
/*     */ 
/* 213 */         if (!DOMUtils.nodesEqual(localNode2, localNode1)) {
/* 214 */           return false;
/*     */         }
/*     */       }
/* 217 */       else if (!localXMLStructure2.equals(localXMLStructure1)) {
/* 218 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 223 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMXMLObject
 * JD-Core Version:    0.6.2
 */