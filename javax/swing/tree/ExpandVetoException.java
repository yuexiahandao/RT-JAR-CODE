/*    */ package javax.swing.tree;
/*    */ 
/*    */ import javax.swing.event.TreeExpansionEvent;
/*    */ 
/*    */ public class ExpandVetoException extends Exception
/*    */ {
/*    */   protected TreeExpansionEvent event;
/*    */ 
/*    */   public ExpandVetoException(TreeExpansionEvent paramTreeExpansionEvent)
/*    */   {
/* 50 */     this(paramTreeExpansionEvent, null);
/*    */   }
/*    */ 
/*    */   public ExpandVetoException(TreeExpansionEvent paramTreeExpansionEvent, String paramString)
/*    */   {
/* 60 */     super(paramString);
/* 61 */     this.event = paramTreeExpansionEvent;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.tree.ExpandVetoException
 * JD-Core Version:    0.6.2
 */