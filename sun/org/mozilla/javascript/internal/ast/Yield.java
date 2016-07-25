/*     */ package sun.org.mozilla.javascript.internal.ast;
/*     */ 
/*     */ public class Yield extends AstNode
/*     */ {
/*     */   private AstNode value;
/*     */ 
/*     */   public Yield()
/*     */   {
/*  55 */     this.type = 72;
/*     */   }
/*     */ 
/*     */   public Yield(int paramInt)
/*     */   {
/*  62 */     super(paramInt);
/*     */ 
/*  55 */     this.type = 72;
/*     */   }
/*     */ 
/*     */   public Yield(int paramInt1, int paramInt2)
/*     */   {
/*  66 */     super(paramInt1, paramInt2);
/*     */ 
/*  55 */     this.type = 72;
/*     */   }
/*     */ 
/*     */   public Yield(int paramInt1, int paramInt2, AstNode paramAstNode)
/*     */   {
/*  70 */     super(paramInt1, paramInt2);
/*     */ 
/*  55 */     this.type = 72;
/*     */ 
/*  71 */     setValue(paramAstNode);
/*     */   }
/*     */ 
/*     */   public AstNode getValue()
/*     */   {
/*  78 */     return this.value;
/*     */   }
/*     */ 
/*     */   public void setValue(AstNode paramAstNode)
/*     */   {
/*  86 */     this.value = paramAstNode;
/*  87 */     if (paramAstNode != null)
/*  88 */       paramAstNode.setParent(this);
/*     */   }
/*     */ 
/*     */   public String toSource(int paramInt)
/*     */   {
/*  93 */     return "yield " + this.value.toSource(0);
/*     */   }
/*     */ 
/*     */   public void visit(NodeVisitor paramNodeVisitor)
/*     */   {
/* 103 */     if ((paramNodeVisitor.visit(this)) && (this.value != null))
/* 104 */       this.value.visit(paramNodeVisitor);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.Yield
 * JD-Core Version:    0.6.2
 */