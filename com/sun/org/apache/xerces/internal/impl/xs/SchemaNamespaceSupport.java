/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.NamespaceSupport;
/*     */ 
/*     */ public class SchemaNamespaceSupport extends NamespaceSupport
/*     */ {
/*     */   public SchemaNamespaceSupport()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SchemaNamespaceSupport(SchemaNamespaceSupport nSupport)
/*     */   {
/*  44 */     this.fNamespaceSize = nSupport.fNamespaceSize;
/*  45 */     if (this.fNamespace.length < this.fNamespaceSize)
/*  46 */       this.fNamespace = new String[this.fNamespaceSize];
/*  47 */     System.arraycopy(nSupport.fNamespace, 0, this.fNamespace, 0, this.fNamespaceSize);
/*  48 */     this.fCurrentContext = nSupport.fCurrentContext;
/*  49 */     if (this.fContext.length <= this.fCurrentContext)
/*  50 */       this.fContext = new int[this.fCurrentContext + 1];
/*  51 */     System.arraycopy(nSupport.fContext, 0, this.fContext, 0, this.fCurrentContext + 1);
/*     */   }
/*     */ 
/*     */   public void setEffectiveContext(String[] namespaceDecls)
/*     */   {
/*  66 */     if ((namespaceDecls == null) || (namespaceDecls.length == 0)) return;
/*  67 */     pushContext();
/*  68 */     int newSize = this.fNamespaceSize + namespaceDecls.length;
/*  69 */     if (this.fNamespace.length < newSize)
/*     */     {
/*  71 */       String[] tempNSArray = new String[newSize];
/*  72 */       System.arraycopy(this.fNamespace, 0, tempNSArray, 0, this.fNamespace.length);
/*  73 */       this.fNamespace = tempNSArray;
/*     */     }
/*  75 */     System.arraycopy(namespaceDecls, 0, this.fNamespace, this.fNamespaceSize, namespaceDecls.length);
/*     */ 
/*  77 */     this.fNamespaceSize = newSize;
/*     */   }
/*     */ 
/*     */   public String[] getEffectiveLocalContext()
/*     */   {
/*  91 */     String[] returnVal = null;
/*  92 */     if (this.fCurrentContext >= 3) {
/*  93 */       int bottomLocalContext = this.fContext[3];
/*  94 */       int copyCount = this.fNamespaceSize - bottomLocalContext;
/*  95 */       if (copyCount > 0) {
/*  96 */         returnVal = new String[copyCount];
/*  97 */         System.arraycopy(this.fNamespace, bottomLocalContext, returnVal, 0, copyCount);
/*     */       }
/*     */     }
/*     */ 
/* 101 */     return returnVal;
/*     */   }
/*     */ 
/*     */   public void makeGlobal()
/*     */   {
/* 107 */     if (this.fCurrentContext >= 3) {
/* 108 */       this.fCurrentContext = 3;
/* 109 */       this.fNamespaceSize = this.fContext[3];
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.SchemaNamespaceSupport
 * JD-Core Version:    0.6.2
 */