/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.URI;
/*     */ import com.sun.org.apache.xerces.internal.util.URI.MalformedURIException;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.DocumentType;
/*     */ import org.w3c.dom.EntityReference;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class EntityReferenceImpl extends ParentNode
/*     */   implements EntityReference
/*     */ {
/*     */   static final long serialVersionUID = -7381452955687102062L;
/*     */   protected String name;
/*     */   protected String baseURI;
/*     */ 
/*     */   public EntityReferenceImpl(CoreDocumentImpl ownerDoc, String name)
/*     */   {
/* 118 */     super(ownerDoc);
/* 119 */     this.name = name;
/* 120 */     isReadOnly(true);
/* 121 */     needsSyncChildren(true);
/*     */   }
/*     */ 
/*     */   public short getNodeType()
/*     */   {
/* 133 */     return 5;
/*     */   }
/*     */ 
/*     */   public String getNodeName()
/*     */   {
/* 140 */     if (needsSyncData()) {
/* 141 */       synchronizeData();
/*     */     }
/* 143 */     return this.name;
/*     */   }
/*     */ 
/*     */   public Node cloneNode(boolean deep)
/*     */   {
/* 148 */     EntityReferenceImpl er = (EntityReferenceImpl)super.cloneNode(deep);
/* 149 */     er.setReadOnly(true, deep);
/* 150 */     return er;
/*     */   }
/*     */ 
/*     */   public String getBaseURI()
/*     */   {
/* 162 */     if (needsSyncData()) {
/* 163 */       synchronizeData();
/*     */     }
/* 165 */     if (this.baseURI == null)
/*     */     {
/*     */       DocumentType doctype;
/*     */       NamedNodeMap entities;
/* 169 */       if ((null != (doctype = getOwnerDocument().getDoctype())) && (null != (entities = doctype.getEntities())))
/*     */       {
/* 172 */         EntityImpl entDef = (EntityImpl)entities.getNamedItem(getNodeName());
/* 173 */         if (entDef != null)
/* 174 */           return entDef.getBaseURI();
/*     */       }
/*     */     }
/* 177 */     else if ((this.baseURI != null) && (this.baseURI.length() != 0)) {
/*     */       try {
/* 179 */         return new URI(this.baseURI).toString();
/*     */       }
/*     */       catch (URI.MalformedURIException e)
/*     */       {
/* 183 */         return null;
/*     */       }
/*     */     }
/* 186 */     return this.baseURI;
/*     */   }
/*     */ 
/*     */   public void setBaseURI(String uri)
/*     */   {
/* 192 */     if (needsSyncData()) {
/* 193 */       synchronizeData();
/*     */     }
/* 195 */     this.baseURI = uri;
/*     */   }
/*     */ 
/*     */   protected String getEntityRefValue()
/*     */   {
/* 206 */     if (needsSyncChildren()) {
/* 207 */       synchronizeChildren();
/*     */     }
/*     */ 
/* 210 */     String value = "";
/* 211 */     if (this.firstChild != null) {
/* 212 */       if (this.firstChild.getNodeType() == 5) {
/* 213 */         value = ((EntityReferenceImpl)this.firstChild).getEntityRefValue();
/*     */       }
/* 215 */       else if (this.firstChild.getNodeType() == 3) {
/* 216 */         value = this.firstChild.getNodeValue();
/*     */       }
/*     */       else
/*     */       {
/* 220 */         return null;
/*     */       }
/*     */ 
/* 223 */       if (this.firstChild.nextSibling == null) {
/* 224 */         return value;
/*     */       }
/*     */ 
/* 227 */       StringBuffer buff = new StringBuffer(value);
/* 228 */       ChildNode next = this.firstChild.nextSibling;
/* 229 */       while (next != null)
/*     */       {
/* 231 */         if (next.getNodeType() == 5) {
/* 232 */           value = ((EntityReferenceImpl)next).getEntityRefValue();
/*     */         }
/* 234 */         else if (next.getNodeType() == 3) {
/* 235 */           value = next.getNodeValue();
/*     */         }
/*     */         else
/*     */         {
/* 239 */           return null;
/*     */         }
/* 241 */         buff.append(value);
/* 242 */         next = next.nextSibling;
/*     */       }
/*     */ 
/* 245 */       return buff.toString();
/*     */     }
/*     */ 
/* 248 */     return "";
/*     */   }
/*     */ 
/*     */   protected void synchronizeChildren()
/*     */   {
/* 259 */     needsSyncChildren(false);
/*     */     DocumentType doctype;
/*     */     NamedNodeMap entities;
/* 264 */     if ((null != (doctype = getOwnerDocument().getDoctype())) && (null != (entities = doctype.getEntities())))
/*     */     {
/* 267 */       EntityImpl entDef = (EntityImpl)entities.getNamedItem(getNodeName());
/*     */ 
/* 270 */       if (entDef == null) {
/* 271 */         return;
/*     */       }
/*     */ 
/* 274 */       isReadOnly(false);
/* 275 */       for (Node defkid = entDef.getFirstChild(); 
/* 276 */         defkid != null; 
/* 277 */         defkid = defkid.getNextSibling()) {
/* 278 */         Node newkid = defkid.cloneNode(true);
/* 279 */         insertBefore(newkid, null);
/*     */       }
/* 281 */       setReadOnly(true, true);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setReadOnly(boolean readOnly, boolean deep)
/*     */   {
/* 293 */     if (needsSyncData()) {
/* 294 */       synchronizeData();
/*     */     }
/* 296 */     if (deep)
/*     */     {
/* 298 */       if (needsSyncChildren()) {
/* 299 */         synchronizeChildren();
/*     */       }
/*     */ 
/* 302 */       for (ChildNode mykid = this.firstChild; 
/* 303 */         mykid != null; 
/* 304 */         mykid = mykid.nextSibling)
/*     */       {
/* 306 */         mykid.setReadOnly(readOnly, true);
/*     */       }
/*     */     }
/*     */ 
/* 310 */     isReadOnly(readOnly);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.EntityReferenceImpl
 * JD-Core Version:    0.6.2
 */