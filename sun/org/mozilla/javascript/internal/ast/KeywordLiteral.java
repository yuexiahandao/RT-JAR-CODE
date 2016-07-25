/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class KeywordLiteral extends AstNode
/*     */ {
/*     */   public KeywordLiteral()
/*     */   {
/*     */   }
/*     */ 
/*     */   public KeywordLiteral(int paramInt)
/*     */   {
/*  59 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public KeywordLiteral(int paramInt1, int paramInt2) {
/*  63 */     super(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public KeywordLiteral(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*  71 */     super(paramInt1, paramInt2);
/*  72 */     setType(paramInt3);
/*     */   }
/*     */ 
/*     */   public KeywordLiteral setType(int paramInt)
/*     */   {
/*  81 */     if ((paramInt != 43) && (paramInt != 42) && (paramInt != 45) && (paramInt != 44) && (paramInt != 160))
/*     */     {
/*  86 */       throw new IllegalArgumentException("Invalid node type: " + paramInt);
/*     */     }
/*  88 */     this.type = paramInt;
/*  89 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean isBooleanLiteral()
/*     */   {
/*  97 */     return (this.type == 45) || (this.type == 44);
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 102 */     StringBuilder localStringBuilder = new StringBuilder();
/* 103 */     localStringBuilder.append(makeIndent(paramInt));
/* 104 */     switch (getType()) {
/*     */     case 43:
/* 106 */       localStringBuilder.append("this");
/* 107 */       break;
/*     */     case 42:
/* 109 */       localStringBuilder.append("null");
/* 110 */       break;
/*     */     case 45:
/* 112 */       localStringBuilder.append("true");
/* 113 */       break;
/*     */     case 44:
/* 115 */       localStringBuilder.append("false");
/* 116 */       break;
/*     */     case 160:
/* 118 */       localStringBuilder.append("debugger");
/* 119 */       break;
/*     */     default:
/* 121 */       throw new IllegalStateException("Invalid keyword literal type: " + getType());
/*     */     }
/*     */ 
/* 124 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 132 */     paramNodeVisitor.visit(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.KeywordLiteral
 * JD-Core Version:    0.6.2
 */