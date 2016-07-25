/*    */ package java.awt;
/*    */ 
/*    */ public abstract class PrintJob
/*    */ {
/*    */   public abstract Graphics getGraphics();
/*    */ 
/*    */   public abstract Dimension getPageDimension();
/*    */ 
/*    */   public abstract int getPageResolution();
/*    */ 
/*    */   public abstract boolean lastPageFirst();
/*    */ 
/*    */   public abstract void end();
/*    */ 
/*    */   public void finalize()
/*    */   {
/* 77 */     end();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.PrintJob
 * JD-Core Version:    0.6.2
 */