/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.DocumentType;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.UserDataHandler;
/*     */ 
/*     */ public class DocumentTypeImpl extends ParentNode
/*     */   implements DocumentType
/*     */ {
/*     */   static final long serialVersionUID = 7751299192316526485L;
/*     */   protected String name;
/*     */   protected NamedNodeMapImpl entities;
/*     */   protected NamedNodeMapImpl notations;
/*     */   protected NamedNodeMapImpl elements;
/*     */   protected String publicID;
/*     */   protected String systemID;
/*     */   protected String internalSubset;
/*  96 */   private int doctypeNumber = 0;
/*     */ 
/* 101 */   private Hashtable userData = null;
/*     */ 
/*     */   public DocumentTypeImpl(CoreDocumentImpl ownerDocument, String name) {
/* 104 */     super(ownerDocument);
/*     */ 
/* 106 */     this.name = name;
/*     */ 
/* 108 */     this.entities = new NamedNodeMapImpl(this);
/* 109 */     this.notations = new NamedNodeMapImpl(this);
/*     */ 
/* 112 */     this.elements = new NamedNodeMapImpl(this);
/*     */   }
/*     */ 
/*     */   public DocumentTypeImpl(CoreDocumentImpl ownerDocument, String qualifiedName, String publicID, String systemID)
/*     */   {
/* 120 */     this(ownerDocument, qualifiedName);
/* 121 */     this.publicID = publicID;
/* 122 */     this.systemID = systemID;
/*     */   }
/*     */ 
/*     */   public String getPublicId()
/*     */   {
/* 137 */     if (needsSyncData()) {
/* 138 */       synchronizeData();
/*     */     }
/* 140 */     return this.publicID;
/*     */   }
/*     */ 
/*     */   public String getSystemId()
/*     */   {
/* 149 */     if (needsSyncData()) {
/* 150 */       synchronizeData();
/*     */     }
/* 152 */     return this.systemID;
/*     */   }
/*     */ 
/*     */   public void setInternalSubset(String internalSubset)
/*     */   {
/* 161 */     if (needsSyncData()) {
/* 162 */       synchronizeData();
/*     */     }
/* 164 */     this.internalSubset = internalSubset;
/*     */   }
/*     */ 
/*     */   public String getInternalSubset()
/*     */   {
/* 174 */     if (needsSyncData()) {
/* 175 */       synchronizeData();
/*     */     }
/* 177 */     return this.internalSubset;
/*     */   }
/*     */ 
/*     */   public short getNodeType()
/*     */   {
/* 189 */     return 10;
/*     */   }
/*     */ 
/*     */   public String getNodeName()
/*     */   {
/* 196 */     if (needsSyncData()) {
/* 197 */       synchronizeData();
/*     */     }
/* 199 */     return this.name;
/*     */   }
/*     */ 
/*     */   public Node cloneNode(boolean deep)
/*     */   {
/* 205 */     DocumentTypeImpl newnode = (DocumentTypeImpl)super.cloneNode(deep);
/*     */ 
/* 207 */     newnode.entities = this.entities.cloneMap(newnode);
/* 208 */     newnode.notations = this.notations.cloneMap(newnode);
/* 209 */     newnode.elements = this.elements.cloneMap(newnode);
/*     */ 
/* 211 */     return newnode;
/*     */   }
/*     */ 
/*     */   public String getTextContent()
/*     */     throws DOMException
/*     */   {
/* 220 */     return null;
/*     */   }
/*     */ 
/*     */   public void setTextContent(String textContent)
/*     */     throws DOMException
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean isEqualNode(Node arg)
/*     */   {
/* 238 */     if (!super.isEqualNode(arg)) {
/* 239 */       return false;
/*     */     }
/*     */ 
/* 242 */     if (needsSyncData()) {
/* 243 */       synchronizeData();
/*     */     }
/* 245 */     DocumentTypeImpl argDocType = (DocumentTypeImpl)arg;
/*     */ 
/* 249 */     if (((getPublicId() == null) && (argDocType.getPublicId() != null)) || ((getPublicId() != null) && (argDocType.getPublicId() == null)) || ((getSystemId() == null) && (argDocType.getSystemId() != null)) || ((getSystemId() != null) && (argDocType.getSystemId() == null)) || ((getInternalSubset() == null) && (argDocType.getInternalSubset() != null)) || ((getInternalSubset() != null) && (argDocType.getInternalSubset() == null)))
/*     */     {
/* 257 */       return false;
/*     */     }
/*     */ 
/* 260 */     if ((getPublicId() != null) && 
/* 261 */       (!getPublicId().equals(argDocType.getPublicId()))) {
/* 262 */       return false;
/*     */     }
/*     */ 
/* 266 */     if ((getSystemId() != null) && 
/* 267 */       (!getSystemId().equals(argDocType.getSystemId()))) {
/* 268 */       return false;
/*     */     }
/*     */ 
/* 272 */     if ((getInternalSubset() != null) && 
/* 273 */       (!getInternalSubset().equals(argDocType.getInternalSubset()))) {
/* 274 */       return false;
/*     */     }
/*     */ 
/* 279 */     NamedNodeMapImpl argEntities = argDocType.entities;
/*     */ 
/* 281 */     if (((this.entities == null) && (argEntities != null)) || ((this.entities != null) && (argEntities == null)))
/*     */     {
/* 283 */       return false;
/*     */     }
/* 285 */     if ((this.entities != null) && (argEntities != null)) {
/* 286 */       if (this.entities.getLength() != argEntities.getLength()) {
/* 287 */         return false;
/*     */       }
/* 289 */       for (int index = 0; this.entities.item(index) != null; index++) {
/* 290 */         Node entNode1 = this.entities.item(index);
/* 291 */         Node entNode2 = argEntities.getNamedItem(entNode1.getNodeName());
/*     */ 
/* 294 */         if (!((NodeImpl)entNode1).isEqualNode((NodeImpl)entNode2)) {
/* 295 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 299 */     NamedNodeMapImpl argNotations = argDocType.notations;
/*     */ 
/* 301 */     if (((this.notations == null) && (argNotations != null)) || ((this.notations != null) && (argNotations == null)))
/*     */     {
/* 303 */       return false;
/*     */     }
/* 305 */     if ((this.notations != null) && (argNotations != null)) {
/* 306 */       if (this.notations.getLength() != argNotations.getLength()) {
/* 307 */         return false;
/*     */       }
/* 309 */       for (int index = 0; this.notations.item(index) != null; index++) {
/* 310 */         Node noteNode1 = this.notations.item(index);
/* 311 */         Node noteNode2 = argNotations.getNamedItem(noteNode1.getNodeName());
/*     */ 
/* 314 */         if (!((NodeImpl)noteNode1).isEqualNode((NodeImpl)noteNode2)) {
/* 315 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 319 */     return true;
/*     */   }
/*     */ 
/*     */   void setOwnerDocument(CoreDocumentImpl doc)
/*     */   {
/* 328 */     super.setOwnerDocument(doc);
/* 329 */     this.entities.setOwnerDocument(doc);
/* 330 */     this.notations.setOwnerDocument(doc);
/* 331 */     this.elements.setOwnerDocument(doc);
/*     */   }
/*     */ 
/*     */   protected int getNodeNumber()
/*     */   {
/* 340 */     if (getOwnerDocument() != null) {
/* 341 */       return super.getNodeNumber();
/*     */     }
/*     */ 
/* 345 */     if (this.doctypeNumber == 0)
/*     */     {
/* 347 */       CoreDOMImplementationImpl cd = (CoreDOMImplementationImpl)CoreDOMImplementationImpl.getDOMImplementation();
/* 348 */       this.doctypeNumber = cd.assignDocTypeNumber();
/*     */     }
/* 350 */     return this.doctypeNumber;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 363 */     if (needsSyncData()) {
/* 364 */       synchronizeData();
/*     */     }
/* 366 */     return this.name;
/*     */   }
/*     */ 
/*     */   public NamedNodeMap getEntities()
/*     */   {
/* 393 */     if (needsSyncChildren()) {
/* 394 */       synchronizeChildren();
/*     */     }
/* 396 */     return this.entities;
/*     */   }
/*     */ 
/*     */   public NamedNodeMap getNotations()
/*     */   {
/* 405 */     if (needsSyncChildren()) {
/* 406 */       synchronizeChildren();
/*     */     }
/* 408 */     return this.notations;
/*     */   }
/*     */ 
/*     */   public void setReadOnly(boolean readOnly, boolean deep)
/*     */   {
/* 422 */     if (needsSyncChildren()) {
/* 423 */       synchronizeChildren();
/*     */     }
/* 425 */     super.setReadOnly(readOnly, deep);
/*     */ 
/* 428 */     this.elements.setReadOnly(readOnly, true);
/* 429 */     this.entities.setReadOnly(readOnly, true);
/* 430 */     this.notations.setReadOnly(readOnly, true);
/*     */   }
/*     */ 
/*     */   public NamedNodeMap getElements()
/*     */   {
/* 439 */     if (needsSyncChildren()) {
/* 440 */       synchronizeChildren();
/*     */     }
/* 442 */     return this.elements;
/*     */   }
/*     */ 
/*     */   public Object setUserData(String key, Object data, UserDataHandler handler)
/*     */   {
/* 447 */     if (this.userData == null)
/* 448 */       this.userData = new Hashtable();
/* 449 */     if (data == null) {
/* 450 */       if (this.userData != null) {
/* 451 */         Object o = this.userData.remove(key);
/* 452 */         if (o != null) {
/* 453 */           ParentNode.UserDataRecord r = (ParentNode.UserDataRecord)o;
/* 454 */           return r.fData;
/*     */         }
/*     */       }
/* 457 */       return null;
/*     */     }
/*     */ 
/* 460 */     Object o = this.userData.put(key, new ParentNode.UserDataRecord(this, data, handler));
/* 461 */     if (o != null) {
/* 462 */       ParentNode.UserDataRecord r = (ParentNode.UserDataRecord)o;
/* 463 */       return r.fData;
/*     */     }
/*     */ 
/* 466 */     return null;
/*     */   }
/*     */ 
/*     */   public Object getUserData(String key) {
/* 470 */     if (this.userData == null) {
/* 471 */       return null;
/*     */     }
/* 473 */     Object o = this.userData.get(key);
/* 474 */     if (o != null) {
/* 475 */       ParentNode.UserDataRecord r = (ParentNode.UserDataRecord)o;
/* 476 */       return r.fData;
/*     */     }
/* 478 */     return null;
/*     */   }
/*     */ 
/*     */   protected Hashtable getUserDataRecord() {
/* 482 */     return this.userData;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.DocumentTypeImpl
 * JD-Core Version:    0.6.2
 */