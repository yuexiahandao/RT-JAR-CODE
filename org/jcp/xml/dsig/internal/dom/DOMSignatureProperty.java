/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.XMLStructure;
/*     */ import javax.xml.crypto.dom.DOMCryptoContext;
/*     */ import javax.xml.crypto.dsig.SignatureProperty;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public final class DOMSignatureProperty extends DOMStructure
/*     */   implements SignatureProperty
/*     */ {
/*     */   private final String id;
/*     */   private final String target;
/*     */   private final List content;
/*     */ 
/*     */   public DOMSignatureProperty(List paramList, String paramString1, String paramString2)
/*     */   {
/*  67 */     if (paramString1 == null)
/*  68 */       throw new NullPointerException("target cannot be null");
/*  69 */     if (paramList == null)
/*  70 */       throw new NullPointerException("content cannot be null");
/*  71 */     if (paramList.isEmpty()) {
/*  72 */       throw new IllegalArgumentException("content cannot be empty");
/*     */     }
/*  74 */     ArrayList localArrayList = new ArrayList(paramList);
/*  75 */     int i = 0; for (int j = localArrayList.size(); i < j; i++) {
/*  76 */       if (!(localArrayList.get(i) instanceof XMLStructure)) {
/*  77 */         throw new ClassCastException("content[" + i + "] is not a valid type");
/*     */       }
/*     */     }
/*     */ 
/*  81 */     this.content = Collections.unmodifiableList(localArrayList);
/*     */ 
/*  83 */     this.target = paramString1;
/*  84 */     this.id = paramString2;
/*     */   }
/*     */ 
/*     */   public DOMSignatureProperty(Element paramElement)
/*     */     throws MarshalException
/*     */   {
/*  94 */     this.target = DOMUtils.getAttributeValue(paramElement, "Target");
/*  95 */     if (this.target == null) {
/*  96 */       throw new MarshalException("target cannot be null");
/*     */     }
/*  98 */     Attr localAttr = paramElement.getAttributeNodeNS(null, "Id");
/*  99 */     if (localAttr != null) {
/* 100 */       this.id = localAttr.getValue();
/* 101 */       paramElement.setIdAttributeNode(localAttr, true);
/*     */     } else {
/* 103 */       this.id = null;
/*     */     }
/*     */ 
/* 106 */     NodeList localNodeList = paramElement.getChildNodes();
/* 107 */     int i = localNodeList.getLength();
/* 108 */     ArrayList localArrayList = new ArrayList(i);
/* 109 */     for (int j = 0; j < i; j++) {
/* 110 */       localArrayList.add(new javax.xml.crypto.dom.DOMStructure(localNodeList.item(j)));
/*     */     }
/* 112 */     if (localArrayList.isEmpty()) {
/* 113 */       throw new MarshalException("content cannot be empty");
/*     */     }
/* 115 */     this.content = Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   public List getContent()
/*     */   {
/* 120 */     return this.content;
/*     */   }
/*     */ 
/*     */   public String getId() {
/* 124 */     return this.id;
/*     */   }
/*     */ 
/*     */   public String getTarget() {
/* 128 */     return this.target;
/*     */   }
/*     */ 
/*     */   public void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext) throws MarshalException
/*     */   {
/* 133 */     Document localDocument = DOMUtils.getOwnerDocument(paramNode);
/*     */ 
/* 135 */     Element localElement = DOMUtils.createElement(localDocument, "SignatureProperty", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 139 */     DOMUtils.setAttributeID(localElement, "Id", this.id);
/* 140 */     DOMUtils.setAttribute(localElement, "Target", this.target);
/*     */ 
/* 143 */     int i = 0; for (int j = this.content.size(); i < j; i++) {
/* 144 */       javax.xml.crypto.dom.DOMStructure localDOMStructure = (javax.xml.crypto.dom.DOMStructure)this.content.get(i);
/*     */ 
/* 146 */       DOMUtils.appendChild(localElement, localDOMStructure.getNode());
/*     */     }
/*     */ 
/* 149 */     paramNode.appendChild(localElement);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 153 */     if (this == paramObject) {
/* 154 */       return true;
/*     */     }
/*     */ 
/* 157 */     if (!(paramObject instanceof SignatureProperty)) {
/* 158 */       return false;
/*     */     }
/* 160 */     SignatureProperty localSignatureProperty = (SignatureProperty)paramObject;
/*     */ 
/* 162 */     boolean bool = this.id == null ? false : localSignatureProperty.getId() == null ? true : this.id.equals(localSignatureProperty.getId());
/*     */ 
/* 165 */     return (equalsContent(localSignatureProperty.getContent())) && (this.target.equals(localSignatureProperty.getTarget())) && (bool);
/*     */   }
/*     */ 
/*     */   private boolean equalsContent(List paramList)
/*     */   {
/* 170 */     int i = paramList.size();
/* 171 */     if (this.content.size() != i) {
/* 172 */       return false;
/*     */     }
/* 174 */     for (int j = 0; j < i; j++) {
/* 175 */       XMLStructure localXMLStructure1 = (XMLStructure)paramList.get(j);
/* 176 */       XMLStructure localXMLStructure2 = (XMLStructure)this.content.get(j);
/* 177 */       if ((localXMLStructure1 instanceof javax.xml.crypto.dom.DOMStructure)) {
/* 178 */         if (!(localXMLStructure2 instanceof javax.xml.crypto.dom.DOMStructure)) {
/* 179 */           return false;
/*     */         }
/* 181 */         Node localNode1 = ((javax.xml.crypto.dom.DOMStructure)localXMLStructure1).getNode();
/*     */ 
/* 183 */         Node localNode2 = ((javax.xml.crypto.dom.DOMStructure)localXMLStructure2).getNode();
/*     */ 
/* 185 */         if (!DOMUtils.nodesEqual(localNode2, localNode1)) {
/* 186 */           return false;
/*     */         }
/*     */       }
/* 189 */       else if (!localXMLStructure2.equals(localXMLStructure1)) {
/* 190 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 195 */     return true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMSignatureProperty
 * JD-Core Version:    0.6.2
 */