/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class ObjectProperty extends InfixExpression
/*     */ {
/*     */   public void setNodeType(int paramInt)
/*     */   {
/*  77 */     if ((paramInt != 103) && (paramInt != 151) && (paramInt != 152))
/*     */     {
/*  80 */       throw new IllegalArgumentException("invalid node type: " + paramInt);
/*     */     }
/*  82 */     setType(paramInt);
/*     */   }
/*     */ 
/*     */   public ObjectProperty()
/*     */   {
/*  68 */     this.type = 103;
/*     */   }
/*     */ 
/*     */   public ObjectProperty(int paramInt)
/*     */   {
/*  89 */     super(paramInt);
/*     */ 
/*  68 */     this.type = 103;
/*     */   }
/*     */ 
/*     */   public ObjectProperty(int paramInt1, int paramInt2)
/*     */   {
/*  93 */     super(paramInt1, paramInt2);
/*     */ 
/*  68 */     this.type = 103;
/*     */   }
/*     */ 
/*     */   public void setIsGetter()
/*     */   {
/* 100 */     this.type = 151;
/*     */   }
/*     */ 
/*     */   public boolean isGetter()
/*     */   {
/* 107 */     return this.type == 151;
/*     */   }
/*     */ 
/*     */   public void setIsSetter()
/*     */   {
/* 114 */     this.type = 152;
/*     */   }
/*     */ 
/*     */   public boolean isSetter()
/*     */   {
/* 121 */     return this.type == 152;
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 126 */     StringBuilder localStringBuilder = new StringBuilder();
/* 127 */     localStringBuilder.append(makeIndent(paramInt));
/* 128 */     if (isGetter())
/* 129 */       localStringBuilder.append("get ");
/* 130 */     else if (isSetter()) {
/* 131 */       localStringBuilder.append("set ");
/*     */     }
/* 133 */     localStringBuilder.append(this.left.toSource(0));
/* 134 */     if (this.type == 103) {
/* 135 */       localStringBuilder.append(": ");
/*     */     }
/* 137 */     localStringBuilder.append(this.right.toSource(0));
/* 138 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.ObjectProperty
 * JD-Core Version:    0.6.2
 */