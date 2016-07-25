/*     */ package com.sun.org.apache.xerces.internal.impl.xs.opti;
/*     */ 
/*     */ import org.w3c.dom.Attr;
/*     */ import org.w3c.dom.Document;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class ElementImpl extends DefaultElement
/*     */ {
/*     */   SchemaDOM schemaDOM;
/*     */   Attr[] attrs;
/*     */   int row;
/*     */   int col;
/*     */   int parentRow;
/*     */   int line;
/*     */   int column;
/*     */   int charOffset;
/*     */   String fAnnotation;
/*     */   String fSyntheticAnnotation;
/*     */ 
/*     */   public ElementImpl(int line, int column, int offset)
/*     */   {
/*  51 */     this.row = -1;
/*  52 */     this.col = -1;
/*  53 */     this.parentRow = -1;
/*  54 */     this.nodeType = 1;
/*     */ 
/*  56 */     this.line = line;
/*  57 */     this.column = column;
/*  58 */     this.charOffset = offset;
/*     */   }
/*     */ 
/*     */   public ElementImpl(int line, int column) {
/*  62 */     this(line, column, -1);
/*     */   }
/*     */ 
/*     */   public ElementImpl(String prefix, String localpart, String rawname, String uri, int line, int column, int offset)
/*     */   {
/*  68 */     super(prefix, localpart, rawname, uri, (short)1);
/*  69 */     this.row = -1;
/*  70 */     this.col = -1;
/*  71 */     this.parentRow = -1;
/*     */ 
/*  73 */     this.line = line;
/*  74 */     this.column = column;
/*  75 */     this.charOffset = offset;
/*     */   }
/*     */ 
/*     */   public ElementImpl(String prefix, String localpart, String rawname, String uri, int line, int column)
/*     */   {
/*  80 */     this(prefix, localpart, rawname, uri, line, column, -1);
/*     */   }
/*     */ 
/*     */   public Document getOwnerDocument()
/*     */   {
/*  89 */     return this.schemaDOM;
/*     */   }
/*     */ 
/*     */   public Node getParentNode()
/*     */   {
/*  94 */     return this.schemaDOM.relations[this.row][0];
/*     */   }
/*     */ 
/*     */   public boolean hasChildNodes()
/*     */   {
/*  99 */     if (this.parentRow == -1) {
/* 100 */       return false;
/*     */     }
/*     */ 
/* 103 */     return true;
/*     */   }
/*     */ 
/*     */   public Node getFirstChild()
/*     */   {
/* 109 */     if (this.parentRow == -1) {
/* 110 */       return null;
/*     */     }
/* 112 */     return this.schemaDOM.relations[this.parentRow][1];
/*     */   }
/*     */ 
/*     */   public Node getLastChild()
/*     */   {
/* 117 */     if (this.parentRow == -1) {
/* 118 */       return null;
/*     */     }
/* 120 */     for (int i = 1; 
/* 121 */       i < this.schemaDOM.relations[this.parentRow].length; i++) {
/* 122 */       if (this.schemaDOM.relations[this.parentRow][i] == null) {
/* 123 */         return this.schemaDOM.relations[this.parentRow][(i - 1)];
/*     */       }
/*     */     }
/* 126 */     if (i == 1) {
/* 127 */       i++;
/*     */     }
/* 129 */     return this.schemaDOM.relations[this.parentRow][(i - 1)];
/*     */   }
/*     */ 
/*     */   public Node getPreviousSibling()
/*     */   {
/* 134 */     if (this.col == 1) {
/* 135 */       return null;
/*     */     }
/* 137 */     return this.schemaDOM.relations[this.row][(this.col - 1)];
/*     */   }
/*     */ 
/*     */   public Node getNextSibling()
/*     */   {
/* 142 */     if (this.col == this.schemaDOM.relations[this.row].length - 1) {
/* 143 */       return null;
/*     */     }
/* 145 */     return this.schemaDOM.relations[this.row][(this.col + 1)];
/*     */   }
/*     */ 
/*     */   public NamedNodeMap getAttributes()
/*     */   {
/* 150 */     return new NamedNodeMapImpl(this.attrs);
/*     */   }
/*     */ 
/*     */   public boolean hasAttributes()
/*     */   {
/* 155 */     return this.attrs.length != 0;
/*     */   }
/*     */ 
/*     */   public String getTagName()
/*     */   {
/* 165 */     return this.rawname;
/*     */   }
/*     */ 
/*     */   public String getAttribute(String name)
/*     */   {
/* 171 */     for (int i = 0; i < this.attrs.length; i++) {
/* 172 */       if (this.attrs[i].getName().equals(name)) {
/* 173 */         return this.attrs[i].getValue();
/*     */       }
/*     */     }
/* 176 */     return "";
/*     */   }
/*     */ 
/*     */   public Attr getAttributeNode(String name)
/*     */   {
/* 181 */     for (int i = 0; i < this.attrs.length; i++) {
/* 182 */       if (this.attrs[i].getName().equals(name)) {
/* 183 */         return this.attrs[i];
/*     */       }
/*     */     }
/* 186 */     return null;
/*     */   }
/*     */ 
/*     */   public String getAttributeNS(String namespaceURI, String localName)
/*     */   {
/* 191 */     for (int i = 0; i < this.attrs.length; i++) {
/* 192 */       if ((this.attrs[i].getLocalName().equals(localName)) && (nsEquals(this.attrs[i].getNamespaceURI(), namespaceURI))) {
/* 193 */         return this.attrs[i].getValue();
/*     */       }
/*     */     }
/* 196 */     return "";
/*     */   }
/*     */ 
/*     */   public Attr getAttributeNodeNS(String namespaceURI, String localName)
/*     */   {
/* 201 */     for (int i = 0; i < this.attrs.length; i++) {
/* 202 */       if ((this.attrs[i].getName().equals(localName)) && (nsEquals(this.attrs[i].getNamespaceURI(), namespaceURI))) {
/* 203 */         return this.attrs[i];
/*     */       }
/*     */     }
/* 206 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean hasAttribute(String name)
/*     */   {
/* 211 */     for (int i = 0; i < this.attrs.length; i++) {
/* 212 */       if (this.attrs[i].getName().equals(name)) {
/* 213 */         return true;
/*     */       }
/*     */     }
/* 216 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean hasAttributeNS(String namespaceURI, String localName)
/*     */   {
/* 221 */     for (int i = 0; i < this.attrs.length; i++) {
/* 222 */       if ((this.attrs[i].getName().equals(localName)) && (nsEquals(this.attrs[i].getNamespaceURI(), namespaceURI))) {
/* 223 */         return true;
/*     */       }
/*     */     }
/* 226 */     return false;
/*     */   }
/*     */ 
/*     */   public void setAttribute(String name, String value)
/*     */   {
/* 231 */     for (int i = 0; i < this.attrs.length; i++)
/* 232 */       if (this.attrs[i].getName().equals(name)) {
/* 233 */         this.attrs[i].setValue(value);
/* 234 */         return;
/*     */       }
/*     */   }
/*     */ 
/*     */   public int getLineNumber()
/*     */   {
/* 241 */     return this.line;
/*     */   }
/*     */ 
/*     */   public int getColumnNumber()
/*     */   {
/* 246 */     return this.column;
/*     */   }
/*     */ 
/*     */   public int getCharacterOffset()
/*     */   {
/* 251 */     return this.charOffset;
/*     */   }
/*     */ 
/*     */   public String getAnnotation() {
/* 255 */     return this.fAnnotation;
/*     */   }
/*     */ 
/*     */   public String getSyntheticAnnotation() {
/* 259 */     return this.fSyntheticAnnotation;
/*     */   }
/*     */ 
/*     */   private static boolean nsEquals(String nsURI_1, String nsURI_2)
/*     */   {
/* 266 */     if (nsURI_1 == null) {
/* 267 */       return nsURI_2 == null;
/*     */     }
/*     */ 
/* 270 */     return nsURI_1.equals(nsURI_2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.opti.ElementImpl
 * JD-Core Version:    0.6.2
 */