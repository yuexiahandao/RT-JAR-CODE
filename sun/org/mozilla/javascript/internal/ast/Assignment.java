/*    */ package sun.org.mozilla.javascript.internal.ast;
/*    */ 
/*    */ public class Assignment extends InfixExpression
/*    */ {
/*    */   public Assignment()
/*    */   {
/*    */   }
/*    */ 
/*    */   public Assignment(int paramInt)
/*    */   {
/* 51 */     super(paramInt);
/*    */   }
/*    */ 
/*    */   public Assignment(int paramInt1, int paramInt2) {
/* 55 */     super(paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   public Assignment(int paramInt1, int paramInt2, AstNode paramAstNode1, AstNode paramAstNode2) {
/* 59 */     super(paramInt1, paramInt2, paramAstNode1, paramAstNode2);
/*    */   }
/*    */ 
/*    */   public Assignment(AstNode paramAstNode1, AstNode paramAstNode2) {
/* 63 */     super(paramAstNode1, paramAstNode2);
/*    */   }
/*    */ 
/*    */   public Assignment(int paramInt1, AstNode paramAstNode1, AstNode paramAstNode2, int paramInt2)
/*    */   {
/* 68 */     super(paramInt1, paramAstNode1, paramAstNode2, paramInt2);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.ast.Assignment
 * JD-Core Version:    0.6.2
 */