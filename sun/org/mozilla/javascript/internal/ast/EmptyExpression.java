/*    */ package sun.org.mozilla.javascript.internal.ast;
/*    */ 
/*    */ public class EmptyExpression extends AstNode
/*    */ {
/*    */   public EmptyExpression()
/*    */   {
/* 51 */     this.type = 128;
/*    */   }
/*    */ 
/*    */   public EmptyExpression(int paramInt)
/*    */   {
/* 58 */     super(paramInt);
/*    */ 
/* 51 */     this.type = 128;
/*    */   }
/*    */ 
/*    */   public EmptyExpression(int paramInt1, int paramInt2)
/*    */   {
/* 62 */     super(paramInt1, paramInt2);
/*    */ 
/* 51 */     this.type = 128;
/*    */   }
/*    */ 
/*    */   public String toSource(int paramInt)
/*    */   {
/* 67 */     return makeIndent(paramInt);
/*    */   }
/*    */ 
/*    */   public void visit(NodeVisitor paramNodeVisitor)
/*    */   {
/* 75 */     paramNodeVisitor.visit(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.EmptyExpression
 * JD-Core Version:    0.6.2
 */