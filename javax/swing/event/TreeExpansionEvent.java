/*    */ package javax.swing.event;
/*    */ 
/*    */ import java.util.EventObject;
/*    */ import javax.swing.tree.TreePath;
/*    */ 
/*    */ public class TreeExpansionEvent extends EventObject
/*    */ {
/*    */   protected TreePath path;
/*    */ 
/*    */   public TreeExpansionEvent(Object paramObject, TreePath paramTreePath)
/*    */   {
/* 67 */     super(paramObject);
/* 68 */     this.path = paramTreePath;
/*    */   }
/*    */ 
/*    */   public TreePath getPath()
/*    */   {
/* 74 */     return this.path;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.event.TreeExpansionEvent
 * JD-Core Version:    0.6.2
 */