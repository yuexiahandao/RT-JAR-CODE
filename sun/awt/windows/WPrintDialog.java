/*    */ package sun.awt.windows;
/*    */ 
/*    */ import java.awt.Container;
/*    */ import java.awt.Dialog;
/*    */ import java.awt.Frame;
/*    */ import java.awt.PrintJob;
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.peer.ComponentPeer;
/*    */ import java.awt.print.PrinterJob;
/*    */ 
/*    */ public class WPrintDialog extends Dialog
/*    */ {
/*    */   protected PrintJob job;
/*    */   protected PrinterJob pjob;
/* 72 */   private boolean retval = false;
/*    */ 
/*    */   public WPrintDialog(Frame paramFrame, PrinterJob paramPrinterJob)
/*    */   {
/* 42 */     super(paramFrame, true);
/* 43 */     this.pjob = paramPrinterJob;
/* 44 */     setLayout(null);
/*    */   }
/*    */ 
/*    */   public WPrintDialog(Dialog paramDialog, PrinterJob paramPrinterJob) {
/* 48 */     super(paramDialog, "", true);
/* 49 */     this.pjob = paramPrinterJob;
/* 50 */     setLayout(null);
/*    */   }
/*    */ 
/*    */   protected native void setPeer(ComponentPeer paramComponentPeer);
/*    */ 
/*    */   public void addNotify()
/*    */   {
/* 57 */     synchronized (getTreeLock()) {
/* 58 */       Container localContainer = getParent();
/* 59 */       if ((localContainer != null) && (localContainer.getPeer() == null)) {
/* 60 */         localContainer.addNotify();
/*    */       }
/*    */ 
/* 63 */       if (getPeer() == null) {
/* 64 */         WPrintDialogPeer localWPrintDialogPeer = ((WToolkit)Toolkit.getDefaultToolkit()).createWPrintDialog(this);
/*    */ 
/* 66 */         setPeer(localWPrintDialogPeer);
/*    */       }
/* 68 */       super.addNotify();
/*    */     }
/*    */   }
/*    */ 
/*    */   public void setRetVal(boolean paramBoolean)
/*    */   {
/* 75 */     this.retval = paramBoolean;
/*    */   }
/*    */ 
/*    */   public boolean getRetVal() {
/* 79 */     return this.retval;
/*    */   }
/*    */ 
/*    */   private static native void initIDs();
/*    */ 
/*    */   static
/*    */   {
/* 35 */     initIDs();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WPrintDialog
 * JD-Core Version:    0.6.2
 */