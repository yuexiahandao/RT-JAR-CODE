/*    */ package javax.swing;
/*    */ 
/*    */ import java.awt.Component;
/*    */ import java.awt.Container;
/*    */ import java.awt.Rectangle;
/*    */ import java.awt.event.PaintEvent;
/*    */ import java.security.AccessController;
/*    */ import sun.awt.AppContext;
/*    */ import sun.awt.PaintEventDispatcher;
/*    */ import sun.awt.SunToolkit;
/*    */ import sun.awt.event.IgnorePaintEvent;
/*    */ import sun.security.action.GetBooleanAction;
/*    */ import sun.security.action.GetPropertyAction;
/*    */ 
/*    */ class SwingPaintEventDispatcher extends PaintEventDispatcher
/*    */ {
/* 49 */   private static final boolean SHOW_FROM_DOUBLE_BUFFER = "true".equals(AccessController.doPrivileged(new GetPropertyAction("swing.showFromDoubleBuffer", "true")));
/*    */ 
/* 51 */   private static final boolean ERASE_BACKGROUND = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("swing.nativeErase"))).booleanValue();
/*    */ 
/*    */   public PaintEvent createPaintEvent(Component paramComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*    */   {
/*    */     AppContext localAppContext;
/*    */     RepaintManager localRepaintManager;
/* 57 */     if ((paramComponent instanceof RootPaneContainer)) {
/* 58 */       localAppContext = SunToolkit.targetToAppContext(paramComponent);
/* 59 */       localRepaintManager = RepaintManager.currentManager(localAppContext);
/* 60 */       if ((!SHOW_FROM_DOUBLE_BUFFER) || (!localRepaintManager.show((Container)paramComponent, paramInt1, paramInt2, paramInt3, paramInt4)))
/*    */       {
/* 62 */         localRepaintManager.nativeAddDirtyRegion(localAppContext, (Container)paramComponent, paramInt1, paramInt2, paramInt3, paramInt4);
/*    */       }
/*    */ 
/* 67 */       return new IgnorePaintEvent(paramComponent, 800, new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4));
/*    */     }
/*    */ 
/* 70 */     if ((paramComponent instanceof SwingHeavyWeight)) {
/* 71 */       localAppContext = SunToolkit.targetToAppContext(paramComponent);
/* 72 */       localRepaintManager = RepaintManager.currentManager(localAppContext);
/* 73 */       localRepaintManager.nativeAddDirtyRegion(localAppContext, (Container)paramComponent, paramInt1, paramInt2, paramInt3, paramInt4);
/*    */ 
/* 75 */       return new IgnorePaintEvent(paramComponent, 800, new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4));
/*    */     }
/*    */ 
/* 78 */     return super.createPaintEvent(paramComponent, paramInt1, paramInt2, paramInt3, paramInt4);
/*    */   }
/*    */ 
/*    */   public boolean shouldDoNativeBackgroundErase(Component paramComponent) {
/* 82 */     return (ERASE_BACKGROUND) || (!(paramComponent instanceof RootPaneContainer));
/*    */   }
/*    */ 
/*    */   public boolean queueSurfaceDataReplacing(Component paramComponent, Runnable paramRunnable) {
/* 86 */     if ((paramComponent instanceof RootPaneContainer)) {
/* 87 */       AppContext localAppContext = SunToolkit.targetToAppContext(paramComponent);
/* 88 */       RepaintManager.currentManager(localAppContext).nativeQueueSurfaceDataRunnable(localAppContext, paramComponent, paramRunnable);
/*    */ 
/* 90 */       return true;
/*    */     }
/* 92 */     return super.queueSurfaceDataReplacing(paramComponent, paramRunnable);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.SwingPaintEventDispatcher
 * JD-Core Version:    0.6.2
 */