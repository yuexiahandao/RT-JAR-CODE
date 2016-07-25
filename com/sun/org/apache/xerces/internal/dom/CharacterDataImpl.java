/*     */ package com.sun.org.apache.xerces.internal.dom;
/*     */ 
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public abstract class CharacterDataImpl extends ChildNode
/*     */ {
/*     */   static final long serialVersionUID = 7931170150428474230L;
/*     */   protected String data;
/*  60 */   private static transient NodeList singletonNodeList = new NodeList() {
/*  61 */     public Node item(int index) { return null; } 
/*  62 */     public int getLength() { return 0; }
/*     */ 
/*  60 */   };
/*     */ 
/*     */   public CharacterDataImpl()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected CharacterDataImpl(CoreDocumentImpl ownerDocument, String data)
/*     */   {
/*  73 */     super(ownerDocument);
/*  74 */     this.data = data;
/*     */   }
/*     */ 
/*     */   public NodeList getChildNodes()
/*     */   {
/*  83 */     return singletonNodeList;
/*     */   }
/*     */ 
/*     */   public String getNodeValue()
/*     */   {
/*  90 */     if (needsSyncData()) {
/*  91 */       synchronizeData();
/*     */     }
/*  93 */     return this.data;
/*     */   }
/*     */ 
/*     */   protected void setNodeValueInternal(String value)
/*     */   {
/* 100 */     setNodeValueInternal(value, false);
/*     */   }
/*     */ 
/*     */   protected void setNodeValueInternal(String value, boolean replace)
/*     */   {
/* 112 */     CoreDocumentImpl ownerDocument = ownerDocument();
/*     */ 
/* 114 */     if ((ownerDocument.errorChecking) && (isReadOnly())) {
/* 115 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/* 116 */       throw new DOMException((short)7, msg);
/*     */     }
/*     */ 
/* 121 */     if (needsSyncData()) {
/* 122 */       synchronizeData();
/*     */     }
/*     */ 
/* 126 */     String oldvalue = this.data;
/*     */ 
/* 129 */     ownerDocument.modifyingCharacterData(this, replace);
/*     */ 
/* 131 */     this.data = value;
/*     */ 
/* 134 */     ownerDocument.modifiedCharacterData(this, oldvalue, value, replace);
/*     */   }
/*     */ 
/*     */   public void setNodeValue(String value)
/*     */   {
/* 143 */     setNodeValueInternal(value);
/*     */ 
/* 146 */     ownerDocument().replacedText(this);
/*     */   }
/*     */ 
/*     */   public String getData()
/*     */   {
/* 162 */     if (needsSyncData()) {
/* 163 */       synchronizeData();
/*     */     }
/* 165 */     return this.data;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 173 */     if (needsSyncData()) {
/* 174 */       synchronizeData();
/*     */     }
/* 176 */     return this.data.length();
/*     */   }
/*     */ 
/*     */   public void appendData(String data)
/*     */   {
/* 189 */     if (isReadOnly()) {
/* 190 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/* 191 */       throw new DOMException((short)7, msg);
/*     */     }
/* 193 */     if (data == null) {
/* 194 */       return;
/*     */     }
/* 196 */     if (needsSyncData()) {
/* 197 */       synchronizeData();
/*     */     }
/*     */ 
/* 200 */     setNodeValue(this.data + data);
/*     */   }
/*     */ 
/*     */   public void deleteData(int offset, int count)
/*     */     throws DOMException
/*     */   {
/* 219 */     internalDeleteData(offset, count, false);
/*     */   }
/*     */ 
/*     */   void internalDeleteData(int offset, int count, boolean replace)
/*     */     throws DOMException
/*     */   {
/* 231 */     CoreDocumentImpl ownerDocument = ownerDocument();
/* 232 */     if (ownerDocument.errorChecking) {
/* 233 */       if (isReadOnly()) {
/* 234 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/* 235 */         throw new DOMException((short)7, msg);
/*     */       }
/*     */ 
/* 238 */       if (count < 0) {
/* 239 */         String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", null);
/* 240 */         throw new DOMException((short)1, msg);
/*     */       }
/*     */     }
/*     */ 
/* 244 */     if (needsSyncData()) {
/* 245 */       synchronizeData();
/*     */     }
/* 247 */     int tailLength = Math.max(this.data.length() - count - offset, 0);
/*     */     try {
/* 249 */       String value = this.data.substring(0, offset) + (tailLength > 0 ? this.data.substring(offset + count, offset + count + tailLength) : "");
/*     */ 
/* 252 */       setNodeValueInternal(value, replace);
/*     */ 
/* 255 */       ownerDocument.deletedText(this, offset, count);
/*     */     }
/*     */     catch (StringIndexOutOfBoundsException e) {
/* 258 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", null);
/* 259 */       throw new DOMException((short)1, msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void insertData(int offset, String data)
/*     */     throws DOMException
/*     */   {
/* 276 */     internalInsertData(offset, data, false);
/*     */   }
/*     */ 
/*     */   void internalInsertData(int offset, String data, boolean replace)
/*     */     throws DOMException
/*     */   {
/* 290 */     CoreDocumentImpl ownerDocument = ownerDocument();
/*     */ 
/* 292 */     if ((ownerDocument.errorChecking) && (isReadOnly())) {
/* 293 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/* 294 */       throw new DOMException((short)7, msg);
/*     */     }
/*     */ 
/* 297 */     if (needsSyncData())
/* 298 */       synchronizeData();
/*     */     try
/*     */     {
/* 301 */       String value = new StringBuffer(this.data).insert(offset, data).toString();
/*     */ 
/* 305 */       setNodeValueInternal(value, replace);
/*     */ 
/* 308 */       ownerDocument.insertedText(this, offset, data.length());
/*     */     }
/*     */     catch (StringIndexOutOfBoundsException e) {
/* 311 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", null);
/* 312 */       throw new DOMException((short)1, msg);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void replaceData(int offset, int count, String data)
/*     */     throws DOMException
/*     */   {
/* 346 */     CoreDocumentImpl ownerDocument = ownerDocument();
/*     */ 
/* 354 */     if ((ownerDocument.errorChecking) && (isReadOnly())) {
/* 355 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "NO_MODIFICATION_ALLOWED_ERR", null);
/* 356 */       throw new DOMException((short)7, msg);
/*     */     }
/*     */ 
/* 359 */     if (needsSyncData()) {
/* 360 */       synchronizeData();
/*     */     }
/*     */ 
/* 364 */     ownerDocument.replacingData(this);
/*     */ 
/* 367 */     String oldvalue = this.data;
/*     */ 
/* 369 */     internalDeleteData(offset, count, true);
/* 370 */     internalInsertData(offset, data, true);
/*     */ 
/* 372 */     ownerDocument.replacedCharacterData(this, oldvalue, this.data);
/*     */   }
/*     */ 
/*     */   public void setData(String value)
/*     */     throws DOMException
/*     */   {
/* 383 */     setNodeValue(value);
/*     */   }
/*     */ 
/*     */   public String substringData(int offset, int count)
/*     */     throws DOMException
/*     */   {
/* 409 */     if (needsSyncData()) {
/* 410 */       synchronizeData();
/*     */     }
/*     */ 
/* 413 */     int length = this.data.length();
/* 414 */     if ((count < 0) || (offset < 0) || (offset > length - 1)) {
/* 415 */       String msg = DOMMessageFormatter.formatMessage("http://www.w3.org/dom/DOMTR", "INDEX_SIZE_ERR", null);
/* 416 */       throw new DOMException((short)1, msg);
/*     */     }
/*     */ 
/* 419 */     int tailIndex = Math.min(offset + count, length);
/*     */ 
/* 421 */     return this.data.substring(offset, tailIndex);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.dom.CharacterDataImpl
 * JD-Core Version:    0.6.2
 */