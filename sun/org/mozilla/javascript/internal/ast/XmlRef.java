/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public abstract class XmlRef extends AstNode
/*     */ {
/*     */   protected Name namespace;
/*  69 */   protected int atPos = -1;
/*  70 */   protected int colonPos = -1;
/*     */ 
/*     */   public XmlRef() {
/*     */   }
/*     */ 
/*     */   public XmlRef(int paramInt) {
/*  76 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public XmlRef(int paramInt1, int paramInt2) {
/*  80 */     super(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public Name getNamespace()
/*     */   {
/*  87 */     return this.namespace;
/*     */   }
/*     */ 
/*     */   public void setNamespace(Name paramName)
/*     */   {
/*  95 */     this.namespace = paramName;
/*  96 */     if (paramName != null)
/*  97 */       paramName.setParent(this);
/*     */   }
/*     */ 
/*     */   public boolean isAttributeAccess()
/*     */   {
/* 104 */     return this.atPos >= 0;
/*     */   }
/*     */ 
/*     */   public int getAtPos()
/*     */   {
/* 112 */     return this.atPos;
/*     */   }
/*     */ 
/*     */   public void setAtPos(int paramInt)
/*     */   {
/* 119 */     this.atPos = paramInt;
/*     */   }
/*     */ 
/*     */   public int getColonPos()
/*     */   {
/* 127 */     return this.colonPos;
/*     */   }
/*     */ 
/*     */   public void setColonPos(int paramInt)
/*     */   {
/* 134 */     this.colonPos = paramInt;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.XmlRef
 * JD-Core Version:    0.6.2
 */