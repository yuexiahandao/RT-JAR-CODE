/*    */ package sun.org.mozilla.javascript.internal.ast;
/*    */ 
/*    */ public class ArrayComprehensionLoop extends ForInLoop
/*    */ {
/*    */   public ArrayComprehensionLoop()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ArrayComprehensionLoop(int paramInt)
/*    */   {
/* 55 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   public ArrayComprehensionLoop(int paramInt1, int paramInt2) {
/* 59 */     super(paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   public AstNode getBody()
/*    */   {
/* 67 */     return null;
/*    */   }
/*    */ 
/*    */   public void setBody(AstNode paramAstNode)
/*    */   {
/* 76 */     throw new UnsupportedOperationException("this node type has no body");
/*    */   }
/*    */ 
/*    */   public String toSource(int paramInt)
/*    */   {
/* 81 */     return makeIndent(paramInt) + " for (" + this.iterator.toSource(0) + " in " + this.iteratedObject.toSource(0) + ")";
/*    */   }
/*    */ 
/*    */   public void visit(NodeVisitor paramNodeVisitor)
/*    */   {
/* 95 */     if (paramNodeVisitor.visit(this)) {
/* 96 */       this.iterator.visit(paramNodeVisitor);
/* 97 */       this.iteratedObject.visit(paramNodeVisitor);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.ArrayComprehensionLoop
 * JD-Core Version:    0.6.2
 */