/*    */ package javax.swing.text.html;
/*    */ 
/*    */ import javax.swing.text.Element;
/*    */ 
/*    */ class BRView extends InlineView
/*    */ {
/*    */   public BRView(Element paramElement)
/*    */   {
/* 42 */     super(paramElement);
/*    */   }
/*    */ 
/*    */   public int getBreakWeight(int paramInt, float paramFloat1, float paramFloat2)
/*    */   {
/* 51 */     if (paramInt == 0) {
/* 52 */       return 3000;
/*    */     }
/* 54 */     return super.getBreakWeight(paramInt, paramFloat1, paramFloat2);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.BRView
 * JD-Core Version:    0.6.2
 */