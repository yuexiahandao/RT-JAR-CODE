/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import sun.org.mozilla.javascript.internal.Node;
/*     */ import sun.org.mozilla.javascript.internal.Token;
/*     */ 
/*     */ public class VariableDeclaration extends AstNode
/*     */ {
/*  61 */   private List<VariableInitializer> variables = new ArrayList();
/*     */ 
/*     */   public VariableDeclaration()
/*     */   {
/*  65 */     this.type = 122;
/*     */   }
/*     */ 
/*     */   public VariableDeclaration(int paramInt)
/*     */   {
/*  72 */     super(paramInt);
/*     */ 
/*  65 */     this.type = 122;
/*     */   }
/*     */ 
/*     */   public VariableDeclaration(int paramInt1, int paramInt2)
/*     */   {
/*  76 */     super(paramInt1, paramInt2);
/*     */ 
/*  65 */     this.type = 122;
/*     */   }
/*     */ 
/*     */   public List<VariableInitializer> getVariables()
/*     */   {
/*  83 */     return this.variables;
/*     */   }
/*     */ 
/*     */   public void setVariables(List<VariableInitializer> paramList)
/*     */   {
/*  91 */     assertNotNull(paramList);
/*  92 */     this.variables.clear();
/*  93 */     for (VariableInitializer localVariableInitializer : paramList)
/*  94 */       addVariable(localVariableInitializer);
/*     */   }
/*     */ 
/*     */   public void addVariable(VariableInitializer paramVariableInitializer)
/*     */   {
/* 104 */     assertNotNull(paramVariableInitializer);
/* 105 */     this.variables.add(paramVariableInitializer);
/* 106 */     paramVariableInitializer.setParent(this);
/*     */   }
/*     */ 
/*     */   public Node setType(int paramInt)
/*     */   {
/* 115 */     if ((paramInt != 122) && (paramInt != 154) && (paramInt != 153))
/*     */     {
/* 118 */       throw new IllegalArgumentException("invalid decl type: " + paramInt);
/* 119 */     }return super.setType(paramInt);
/*     */   }
/*     */ 
/*     */   public boolean isVar()
/*     */   {
/* 128 */     return this.type == 122;
/*     */   }
/*     */ 
/*     */   public boolean isConst()
/*     */   {
/* 135 */     return this.type == 154;
/*     */   }
/*     */ 
/*     */   public boolean isLet()
/*     */   {
/* 142 */     return this.type == 153;
/*     */   }
/*     */ 
/*     */   private String declTypeName() {
/* 146 */     return Token.typeToName(this.type).toLowerCase();
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/* 151 */     StringBuilder localStringBuilder = new StringBuilder();
/* 152 */     localStringBuilder.append(makeIndent(paramInt));
/* 153 */     localStringBuilder.append(declTypeName());
/* 154 */     localStringBuilder.append(" ");
/* 155 */     printList(this.variables, localStringBuilder);
/* 156 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 164 */     if (paramNodeVisitor.visit(this))
/* 165 */       for (AstNode localAstNode : this.variables)
/* 166 */         localAstNode.visit(paramNodeVisitor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.VariableDeclaration
 * JD-Core Version:    0.6.2
 */