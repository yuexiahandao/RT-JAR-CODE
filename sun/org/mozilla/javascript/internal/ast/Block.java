/*    */ package sun.org.mozilla.javascript.internal.ast;
/*    */ 
/*    */ import sun.org.mozilla.javascript.internal.Node;
/*    */ 
/*    */ public class Block extends AstNode
/*    */ {
/*    */   public Block()
/*    */   {
/* 56 */     this.type = 129;
/*    */   }
/*    */ 
/*    */   public Block(int paramInt)
/*    */   {
/* 63 */     super(paramInt);
/*    */ 
/* 56 */     this.type = 129;
/*    */   }
/*    */ 
/*    */   public Block(int paramInt1, int paramInt2)
/*    */   {
/* 67 */     super(paramInt1, paramInt2);
/*    */ 
/* 56 */     this.type = 129;
/*    */   }
/*    */ 
/*    */   public void addStatement(AstNode paramAstNode)
/*    */   {
/* 74 */     addChild(paramAstNode);
/*    */   }
/*    */ 
/*    */   public String toSource(int paramInt)
/*    */   {
/* 79 */     StringBuilder localStringBuilder = new StringBuilder();
/* 80 */     localStringBuilder.append(makeIndent(paramInt));
/* 81 */     localStringBuilder.append("{\n");
/* 82 */     for (Node localNode : this) {
/* 83 */       localStringBuilder.append(((AstNode)localNode).toSource(paramInt + 1));
/*    */     }
/* 85 */     localStringBuilder.append(makeIndent(paramInt));
/* 86 */     localStringBuilder.append("}\n");
/* 87 */     return localStringBuilder.toString();
/*    */   }
/*    */ 
/*    */   public void visit(NodeVisitor paramNodeVisitor)
/*    */   {
/* 92 */     if (paramNodeVisitor.visit(this))
/* 93 */       for (Node localNode : this)
/* 94 */         ((AstNode)localNode).visit(paramNodeVisitor);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.Block
 * JD-Core Version:    0.6.2
 */