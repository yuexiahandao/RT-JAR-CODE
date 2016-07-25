/*    */ package sun.org.mozilla.javascript.internal.ast;
/*    */ 
/*    */ public class ErrorNode extends AstNode
/*    */ {
/*    */   private String message;
/*    */ 
/*    */   public ErrorNode()
/*    */   {
/* 52 */     this.type = -1;
/*    */   }
/*    */ 
/*    */   public ErrorNode(int paramInt)
/*    */   {
/* 59 */     super(paramInt);
/*    */ 
/* 52 */     this.type = -1;
/*    */   }
/*    */ 
/*    */   public ErrorNode(int paramInt1, int paramInt2)
/*    */   {
/* 63 */     super(paramInt1, paramInt2);
/*    */ 
/* 52 */     this.type = -1;
/*    */   }
/*    */ 
/*    */   public String getMessage()
/*    */   {
/* 70 */     return this.message;
/*    */   }
/*    */ 
/*    */   public void setMessage(String paramString)
/*    */   {
/* 77 */     this.message = paramString;
/*    */   }
/*    */ 
/*    */   public String toSource(int paramInt)
/*    */   {
/* 82 */     return "";
/*    */   }
/*    */ 
/*    */   public void visit(NodeVisitor paramNodeVisitor)
/*    */   {
/* 91 */     paramNodeVisitor.visit(this);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.ErrorNode
 * JD-Core Version:    0.6.2
 */