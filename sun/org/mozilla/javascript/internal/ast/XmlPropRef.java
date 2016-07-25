/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class XmlPropRef extends XmlRef
/*     */ {
/*     */   private Name propName;
/*     */ 
/*     */   public XmlPropRef()
/*     */   {
/*  65 */     this.type = 79;
/*     */   }
/*     */ 
/*     */   public XmlPropRef(int paramInt)
/*     */   {
/*  72 */     super(paramInt);
/*     */ 
/*  65 */     this.type = 79;
/*     */   }
/*     */ 
/*     */   public XmlPropRef(int paramInt1, int paramInt2)
/*     */   {
/*  76 */     super(paramInt1, paramInt2);
/*     */ 
/*  65 */     this.type = 79;
/*     */   }
/*     */ 
/*     */   public Name getPropName()
/*     */   {
/*  83 */     return this.propName;
/*     */   }
/*     */ 
/*     */   public void setPropName(Name paramName)
/*     */   {
/*  91 */     assertNotNull(paramName);
/*  92 */     this.propName = paramName;
/*  93 */     paramName.setParent(this);
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/*  98 */     StringBuilder localStringBuilder = new StringBuilder();
/*  99 */     localStringBuilder.append(makeIndent(paramInt));
/* 100 */     if (isAttributeAccess()) {
/* 101 */       localStringBuilder.append("@");
/*     */     }
/* 103 */     if (this.namespace != null) {
/* 104 */       localStringBuilder.append(this.namespace.toSource(0));
/* 105 */       localStringBuilder.append("::");
/*     */     }
/* 107 */     localStringBuilder.append(this.propName.toSource(0));
/* 108 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 116 */     if (paramNodeVisitor.visit(this)) {
/* 117 */       if (this.namespace != null) {
/* 118 */         this.namespace.visit(paramNodeVisitor);
/*     */       }
/* 120 */       this.propName.visit(paramNodeVisitor);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.XmlPropRef
 * JD-Core Version:    0.6.2
 */