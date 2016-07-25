/*    */ package sun.swing;
/*    */ 
/*    */ import java.awt.Point;
/*    */ import javax.swing.TransferHandler.DropLocation;
/*    */ import javax.swing.text.JTextComponent;
/*    */ import sun.misc.Unsafe;
/*    */ 
/*    */ public final class SwingAccessor
/*    */ {
/* 43 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*    */   private static JTextComponentAccessor jtextComponentAccessor;
/*    */ 
/*    */   public static void setJTextComponentAccessor(JTextComponentAccessor paramJTextComponentAccessor)
/*    */   {
/* 83 */     jtextComponentAccessor = paramJTextComponentAccessor;
/*    */   }
/*    */ 
/*    */   public static JTextComponentAccessor getJTextComponentAccessor()
/*    */   {
/* 90 */     if (jtextComponentAccessor == null) {
/* 91 */       unsafe.ensureClassInitialized(JTextComponent.class);
/*    */     }
/*    */ 
/* 94 */     return jtextComponentAccessor;
/*    */   }
/*    */ 
/*    */   public static abstract interface JTextComponentAccessor
/*    */   {
/*    */     public abstract TransferHandler.DropLocation dropLocationForPoint(JTextComponent paramJTextComponent, Point paramPoint);
/*    */ 
/*    */     public abstract Object setDropLocation(JTextComponent paramJTextComponent, TransferHandler.DropLocation paramDropLocation, Object paramObject, boolean paramBoolean);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.swing.SwingAccessor
 * JD-Core Version:    0.6.2
 */