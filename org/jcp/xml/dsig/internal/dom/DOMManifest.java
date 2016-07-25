/*     */ package org.jcp.xml.dsig.internal.dom;
/*     */ 
/*     */ import java.security.Provider;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import javax.xml.crypto.MarshalException;
/*     */ import javax.xml.crypto.XMLCryptoContext;
/*     */ import javax.xml.crypto.dom.DOMCryptoContext;
/*     */ import javax.xml.crypto.dsig.Manifest;
/*     */ import javax.xml.crypto.dsig.Reference;
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.Element;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public final class DOMManifest extends DOMStructure
/*     */   implements Manifest
/*     */ {
/*     */   private final List references;
/*     */   private final String id;
/*     */ 
/*     */   public DOMManifest(List paramList, String paramString)
/*     */   {
/*  64 */     if (paramList == null) {
/*  65 */       throw new NullPointerException("references cannot be null");
/*     */     }
/*  67 */     ArrayList localArrayList = new ArrayList(paramList);
/*  68 */     if (localArrayList.isEmpty()) {
/*  69 */       throw new IllegalArgumentException("list of references must contain at least one entry");
/*     */     }
/*     */ 
/*  72 */     int i = 0; for (int j = localArrayList.size(); i < j; i++) {
/*  73 */       if (!(localArrayList.get(i) instanceof Reference)) {
/*  74 */         throw new ClassCastException("references[" + i + "] is not a valid type");
/*     */       }
/*     */     }
/*     */ 
/*  78 */     this.references = Collections.unmodifiableList(localArrayList);
/*  79 */     this.id = paramString;
/*     */   }
/*     */ 
/*     */   public DOMManifest(Element paramElement, XMLCryptoContext paramXMLCryptoContext, Provider paramProvider)
/*     */     throws MarshalException
/*     */   {
/*  89 */     Attr localAttr = paramElement.getAttributeNodeNS(null, "Id");
/*  90 */     if (localAttr != null) {
/*  91 */       this.id = localAttr.getValue();
/*  92 */       paramElement.setIdAttributeNode(localAttr, true);
/*     */     } else {
/*  94 */       this.id = null;
/*     */     }
/*     */ 
/*  97 */     boolean bool = Utils.secureValidation(paramXMLCryptoContext);
/*  98 */     Element localElement = DOMUtils.getFirstChildElement(paramElement);
/*  99 */     ArrayList localArrayList = new ArrayList();
/* 100 */     int i = 0;
/* 101 */     while (localElement != null) {
/* 102 */       localArrayList.add(new DOMReference(localElement, paramXMLCryptoContext, paramProvider));
/* 103 */       localElement = DOMUtils.getNextSiblingElement(localElement);
/*     */ 
/* 105 */       i++;
/* 106 */       if ((bool) && (i > 30)) {
/* 107 */         String str = "A maxiumum of 30 references per Manifest are allowed with secure validation";
/*     */ 
/* 111 */         throw new MarshalException(str);
/*     */       }
/*     */     }
/* 114 */     this.references = Collections.unmodifiableList(localArrayList);
/*     */   }
/*     */ 
/*     */   public String getId() {
/* 118 */     return this.id;
/*     */   }
/*     */ 
/*     */   public List getReferences() {
/* 122 */     return this.references;
/*     */   }
/*     */ 
/*     */   public void marshal(Node paramNode, String paramString, DOMCryptoContext paramDOMCryptoContext) throws MarshalException
/*     */   {
/* 127 */     Document localDocument = DOMUtils.getOwnerDocument(paramNode);
/*     */ 
/* 129 */     Element localElement = DOMUtils.createElement(localDocument, "Manifest", "http://www.w3.org/2000/09/xmldsig#", paramString);
/*     */ 
/* 132 */     DOMUtils.setAttributeID(localElement, "Id", this.id);
/*     */ 
/* 135 */     int i = 0; for (int j = this.references.size(); i < j; i++) {
/* 136 */       DOMReference localDOMReference = (DOMReference)this.references.get(i);
/* 137 */       localDOMReference.marshal(localElement, paramString, paramDOMCryptoContext);
/*     */     }
/* 139 */     paramNode.appendChild(localElement);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 143 */     if (this == paramObject) {
/* 144 */       return true;
/*     */     }
/*     */ 
/* 147 */     if (!(paramObject instanceof Manifest)) {
/* 148 */       return false;
/*     */     }
/* 150 */     Manifest localManifest = (Manifest)paramObject;
/*     */ 
/* 152 */     boolean bool = this.id == null ? false : localManifest.getId() == null ? true : this.id.equals(localManifest.getId());
/*     */ 
/* 155 */     return (bool) && (this.references.equals(localManifest.getReferences()));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.jcp.xml.dsig.internal.dom.DOMManifest
 * JD-Core Version:    0.6.2
 */