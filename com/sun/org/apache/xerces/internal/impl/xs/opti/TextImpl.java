/*     */ package com.sun.org.apache.xerces.internal.impl.xs.opti;
/*     */ 
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ public class TextImpl extends DefaultText
/*     */ {
/*  35 */   String fData = null;
/*  36 */   SchemaDOM fSchemaDOM = null;
/*     */   int fRow;
/*     */   int fCol;
/*     */ 
/*     */   public TextImpl(StringBuffer str, SchemaDOM sDOM, int row, int col)
/*     */   {
/*  41 */     this.fData = str.toString();
/*  42 */     this.fSchemaDOM = sDOM;
/*  43 */     this.fRow = row;
/*  44 */     this.fCol = col;
/*  45 */     this.rawname = (this.prefix = this.localpart = this.uri = null);
/*  46 */     this.nodeType = 3;
/*     */   }
/*     */ 
/*     */   public Node getParentNode()
/*     */   {
/*  54 */     return this.fSchemaDOM.relations[this.fRow][0];
/*     */   }
/*     */ 
/*     */   public Node getPreviousSibling() {
/*  58 */     if (this.fCol == 1) {
/*  59 */       return null;
/*     */     }
/*  61 */     return this.fSchemaDOM.relations[this.fRow][(this.fCol - 1)];
/*     */   }
/*     */ 
/*     */   public Node getNextSibling()
/*     */   {
/*  66 */     if (this.fCol == this.fSchemaDOM.relations[this.fRow].length - 1) {
/*  67 */       return null;
/*     */     }
/*  69 */     return this.fSchemaDOM.relations[this.fRow][(this.fCol + 1)];
/*     */   }
/*     */ 
/*     */   public String getData()
/*     */     throws DOMException
/*     */   {
/*  91 */     return this.fData;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 100 */     if (this.fData == null) return 0;
/* 101 */     return this.fData.length();
/*     */   }
/*     */ 
/*     */   public String substringData(int offset, int count)
/*     */     throws DOMException
/*     */   {
/* 122 */     if (this.fData == null) return null;
/* 123 */     if ((count < 0) || (offset < 0) || (offset > this.fData.length()))
/* 124 */       throw new DOMException((short)1, "parameter error");
/* 125 */     if (offset + count >= this.fData.length())
/* 126 */       return this.fData.substring(offset);
/* 127 */     return this.fData.substring(offset, offset + count);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.opti.TextImpl
 * JD-Core Version:    0.6.2
 */