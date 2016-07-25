/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.dom.DOMCryptoContext;
/*     */ import javax.xml.crypto.dsig.SignatureProperties;
/*     */ import javax.xml.crypto.dsig.SignatureProperty;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public final class DOMSignatureProperties extends DOMStructure
/*     */   implements SignatureProperties
/*     */ {
/*     */   private final String id;
/*     */   private final List properties;
/*     */ 
/*     */   public DOMSignatureProperties(List paramList, String paramString)
/*     */   {
/*  65 */     if (paramList == null)
/*  66 */       throw new NullPointerException("properties cannot be null");
/*  67 */     if (paramList.isEmpty()) {
/*  68 */       throw new IllegalArgumentException("properties cannot be empty");
/*     */     }
/*  70 */     ArrayList localArrayList = new ArrayList(paramList);
/*  71 */     int i = 0; for (int j = localArrayList.size(); i < j; i++) {
/*  72 */       if (!(localArrayList.get(i) instanceof SignatureProperty)) {
/*  73 */         throw new ClassCastException("properties[" + i + "] is not a valid type");
/*     */       }
/*     */     }
/*     */ 
/*  77 */     this.properties = Collections.unmodifiableList(localArrayList);
/*     */ 
/*  79 */     this.id = paramString;
/*     */   }
/*     */ 
/*     */   public DOMSignatureProperties(Element paramElement)
/*     */     throws MarshalException
/*     */   {
/*  90 */     Attr localAttr = paramElement.getAttributeNodeNS(null, "Id");
/*  91 */     if (localAttr != null) {
/*  92 */       this.id = localAttr.getValue();
/*  93 */       paramElement.setIdAttributeNode(localAttr, true);
/*     */     } else {
/*  95 */       this.id = null;
/*     */     }
/*     */ 
/*  98 */     NodeList localNodeList = paramElement.getChildNodes();
/*  99 */     int i = localNodeList.getLength();
/* 100 */     ArrayList localArrayList = new ArrayList(i);
/* 101 */     for (int j = 0; j < i; j++) {
/* 102 */       Node localNode = localNodeList.item(j);
/* 103 */       if (localNode.getNodeType() == 1) {
/* 104 */         localArrayList.add(new DOMSignatureProperty((Element)localNode));
/*     */       }
/*     */     }
/* 107 */     if (localArrayList.isEmpty()) {
/* 108 */       throw new MarshalException("properties cannot be empty");
/*     */     }
/* 110 */     this.properties = Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   public List getProperties()
/*     */   {
/* 115 */     return this.properties;
/*     */   }
/*     */ 
/*     */   public String getId() {
/* 119 */     return this.id;
/*     */   }
/*     */ 
/*     */   public void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext) throws MarshalException
/*     */   {
/* 124 */     Document localDocument = DOMUtils.getOwnerDocument(paramNode);
/*     */ 
/* 126 */     Element localElement = DOMUtils.createElement(localDocument, "SignatureProperties", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 130 */     DOMUtils.setAttributeID(localElement, "Id", this.id);
/*     */ 
/* 133 */     int i = 0; for (int j = this.properties.size(); i < j; i++) {
/* 134 */       DOMSignatureProperty localDOMSignatureProperty = (DOMSignatureProperty)this.properties.get(i);
/*     */ 
/* 136 */       localDOMSignatureProperty.marshal(localElement, paramString, paramDOMCryptoContext);
/*     */     }
/*     */ 
/* 139 */     paramNode.appendChild(localElement);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 143 */     if (this == paramObject) {
/* 144 */       return true;
/*     */     }
/*     */ 
/* 147 */     if (!(paramObject instanceof SignatureProperties)) {
/* 148 */       return false;
/*     */     }
/* 150 */     SignatureProperties localSignatureProperties = (SignatureProperties)paramObject;
/*     */ 
/* 152 */     boolean bool = this.id == null ? false : localSignatureProperties.getId() == null ? true : this.id.equals(localSignatureProperties.getId());
/*     */ 
/* 155 */     return (this.properties.equals(localSignatureProperties.getProperties())) && (bool);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMSignatureProperties
 * JD-Core Version:    0.6.2
 */