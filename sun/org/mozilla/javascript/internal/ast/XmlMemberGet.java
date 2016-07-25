/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class XmlMemberGet extends InfixExpression
/*     */ {
/*     */   public XmlMemberGet()
/*     */   {
/*  54 */     this.type = 143;
/*     */   }
/*     */ 
/*     */   public XmlMemberGet(int paramInt)
/*     */   {
/*  61 */     super(paramInt);
/*     */ 
/*  54 */     this.type = 143;
/*     */   }
/*     */ 
/*     */   public XmlMemberGet(int paramInt1, int paramInt2)
/*     */   {
/*  65 */     super(paramInt1, paramInt2);
/*     */ 
/*  54 */     this.type = 143;
/*     */   }
/*     */ 
/*     */   public XmlMemberGet(int paramInt1, int paramInt2, AstNode paramAstNode, XmlRef paramXmlRef)
/*     */   {
/*  69 */     super(paramInt1, paramInt2, paramAstNode, paramXmlRef);
/*     */ 
/*  54 */     this.type = 143;
/*     */   }
/*     */ 
/*     */   public XmlMemberGet(AstNode paramAstNode, XmlRef paramXmlRef)
/*     */   {
/*  77 */     super(paramAstNode, paramXmlRef);
/*     */ 
/*  54 */     this.type = 143;
/*     */   }
/*     */ 
/*     */   public XmlMemberGet(AstNode paramAstNode, XmlRef paramXmlRef, int paramInt)
/*     */   {
/*  81 */     super(143, paramAstNode, paramXmlRef, paramInt);
/*     */ 
/*  54 */     this.type = 143;
/*     */   }
/*     */ 
/*     */   public AstNode getTarget()
/*     */   {
/*  89 */     return getLeft();
/*     */   }
/*     */ 
/*     */   public void setTarget(AstNode paramAstNode)
/*     */   {
/*  97 */     setLeft(paramAstNode);
/*     */   }
/*     */ 
/*     */   public XmlRef getMemberRef()
/*     */   {
/* 105 */     return (XmlRef)getRight();
/*     */   }
/*     */ 
/*     */   public void setProperty(XmlRef paramXmlRef)
/*     */   {
/* 114 */     setRight(paramXmlRef);
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 119 */     StringBuilder localStringBuilder = new StringBuilder();
/* 120 */     localStringBuilder.append(makeIndent(paramInt));
/* 121 */     localStringBuilder.append(getLeft().toSource(0));
/* 122 */     localStringBuilder.append(operatorToString(getType()));
/* 123 */     localStringBuilder.append(getRight().toSource(0));
/* 124 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.XmlMemberGet
 * JD-Core Version:    0.6.2
 */