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
/*     */ import javax.xml.crypto.dsig.keyinfo.KeyInfo;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public final class DOMKeyInfo extends DOMStructure
/*     */   implements KeyInfo
/*     */ {
/*     */   private final String id;
/*     */   private final List keyInfoTypes;
/*     */ 
/*     */   public DOMKeyInfo(List paramList, String paramString)
/*     */   {
/*  66 */     if (paramList == null) {
/*  67 */       throw new NullPointerException("content cannot be null");
/*     */     }
/*  69 */     ArrayList localArrayList = new ArrayList(paramList);
/*  70 */     if (localArrayList.isEmpty()) {
/*  71 */       throw new IllegalArgumentException("content cannot be empty");
/*     */     }
/*  73 */     int i = 0; for (int j = localArrayList.size(); i < j; i++) {
/*  74 */       if (!(localArrayList.get(i) instanceof XMLStructure)) {
/*  75 */         throw new ClassCastException("content[" + i + "] is not a valid KeyInfo type");
/*     */       }
/*     */     }
/*     */ 
/*  79 */     this.keyInfoTypes = Collections.unmodifiableList(localArrayList);
/*  80 */     this.id = paramString;
/*     */   }
/*     */ 
/*     */   public DOMKeyInfo(Element paramElement, XMLCryptoContext paramXMLCryptoContext, Provider paramProvider)
/*     */     throws MarshalException
/*     */   {
/*  91 */     Attr localAttr = paramElement.getAttributeNodeNS(null, "Id");
/*  92 */     if (localAttr != null) {
/*  93 */       this.id = localAttr.getValue();
/*  94 */       paramElement.setIdAttributeNode(localAttr, true);
/*     */     } else {
/*  96 */       this.id = null;
/*     */     }
/*     */ 
/* 100 */     NodeList localNodeList = paramElement.getChildNodes();
/* 101 */     int i = localNodeList.getLength();
/* 102 */     if (i < 1) {
/* 103 */       throw new MarshalException("KeyInfo must contain at least one type");
/*     */     }
/*     */ 
/* 106 */     ArrayList localArrayList = new ArrayList(i);
/* 107 */     for (int j = 0; j < i; j++) {
/* 108 */       Node localNode = localNodeList.item(j);
/*     */ 
/* 110 */       if (localNode.getNodeType() == 1)
/*     */       {
/* 113 */         Element localElement = (Element)localNode;
/* 114 */         String str = localElement.getLocalName();
/* 115 */         if (str.equals("X509Data"))
/* 116 */           localArrayList.add(new DOMX509Data(localElement));
/* 117 */         else if (str.equals("KeyName"))
/* 118 */           localArrayList.add(new DOMKeyName(localElement));
/* 119 */         else if (str.equals("KeyValue"))
/* 120 */           localArrayList.add(new DOMKeyValue(localElement));
/* 121 */         else if (str.equals("RetrievalMethod")) {
/* 122 */           localArrayList.add(new DOMRetrievalMethod(localElement, paramXMLCryptoContext, paramProvider));
/*     */         }
/* 124 */         else if (str.equals("PGPData"))
/* 125 */           localArrayList.add(new DOMPGPData(localElement));
/*     */         else
/* 127 */           localArrayList.add(new javax.xml.crypto.dom.DOMStructure(localElement));
/*     */       }
/*     */     }
/* 130 */     this.keyInfoTypes = Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   public String getId() {
/* 134 */     return this.id;
/*     */   }
/*     */ 
/*     */   public List getContent() {
/* 138 */     return this.keyInfoTypes;
/*     */   }
/*     */ 
/*     */   public void marshal(XMLStructure paramXMLStructure, XMLCryptoContext paramXMLCryptoContext) throws MarshalException
/*     */   {
/* 143 */     if (paramXMLStructure == null) {
/* 144 */       throw new NullPointerException("parent is null");
/*     */     }
/*     */ 
/* 147 */     Node localNode = ((javax.xml.crypto.dom.DOMStructure)paramXMLStructure).getNode();
/* 148 */     String str = DOMUtils.getSignaturePrefix(paramXMLCryptoContext);
/* 149 */     Element localElement = DOMUtils.createElement(DOMUtils.getOwnerDocument(localNode), "KeyInfo", "http://www.w3.org/2000/09/xmldsig#", str);
/*     */ 
/* 152 */     if ((str == null) || (str.length() == 0)) {
/* 153 */       localElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns", "http://www.w3.org/2000/09/xmldsig#");
/*     */     }
/*     */     else {
/* 156 */       localElement.setAttributeNS("http://www.w3.org/2000/xmlns/", "xmlns:" + str, "http://www.w3.org/2000/09/xmldsig#");
/*     */     }
/*     */ 
/* 160 */     marshal(localNode, localElement, null, str, (DOMCryptoContext)paramXMLCryptoContext);
/*     */   }
/*     */ 
/*     */   public void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext) throws MarshalException
/*     */   {
/* 165 */     marshal(paramNode, null, paramString, paramDOMCryptoContext);
/*     */   }
/*     */ 
/*     */   public void marshal(Node paramNode1, Node paramNode2, String paramString, DOMCryptoContext paramDOMCryptoContext) throws MarshalException
/*     */   {
/* 170 */     Document localDocument = DOMUtils.getOwnerDocument(paramNode1);
/*     */ 
/* 172 */     Element localElement = DOMUtils.createElement(localDocument, "KeyInfo", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 174 */     marshal(paramNode1, localElement, paramNode2, paramString, paramDOMCryptoContext);
/*     */   }
/*     */ 
/*     */   private void marshal(Node paramNode1, Element paramElement, Node paramNode2, String paramString, DOMCryptoContext paramDOMCryptoContext)
/*     */     throws MarshalException
/*     */   {
/* 180 */     int i = 0; for (int j = this.keyInfoTypes.size(); i < j; i++) {
/* 181 */       XMLStructure localXMLStructure = (XMLStructure)this.keyInfoTypes.get(i);
/* 182 */       if ((localXMLStructure instanceof DOMStructure))
/* 183 */         ((DOMStructure)localXMLStructure).marshal(paramElement, paramString, paramDOMCryptoContext);
/*     */       else {
/* 185 */         DOMUtils.appendChild(paramElement, ((javax.xml.crypto.dom.DOMStructure)localXMLStructure).getNode());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 191 */     DOMUtils.setAttributeID(paramElement, "Id", this.id);
/*     */ 
/* 193 */     paramNode1.insertBefore(paramElement, paramNode2);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 197 */     if (this == paramObject) {
/* 198 */       return true;
/*     */     }
/*     */ 
/* 201 */     if (!(paramObject instanceof KeyInfo)) {
/* 202 */       return false;
/*     */     }
/* 204 */     KeyInfo localKeyInfo = (KeyInfo)paramObject;
/*     */ 
/* 206 */     boolean bool = this.id == null ? false : localKeyInfo.getId() == null ? true : this.id.equals(localKeyInfo.getId());
/*     */ 
/* 209 */     return (this.keyInfoTypes.equals(localKeyInfo.getContent())) && (bool);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMKeyInfo
 * JD-Core Version:    0.6.2
 */