/*    */ package sun.awt.windows;
/*    */ 
/*    */ import java.awt.Container;
/*    */ import java.awt.Dialog;
/*    */ import java.awt.Frame;
/*    */ import java.awt.Toolkit;
/*    */ import java.awt.print.PageFormat;
/*    */ import java.awt.print.Printable;
/*    */ import java.awt.print.PrinterJob;
/*    */ 
/*    */ public class WPageDialog extends WPrintDialog
/*    */ {
/*    */   PageFormat page;
/*    */   Printable painter;
/*    */ 
/*    */   WPageDialog(Frame paramFrame, PrinterJob paramPrinterJob, PageFormat paramPageFormat, Printable paramPrintable)
/*    */   {
/* 46 */     super(paramFrame, paramPrinterJob);
/* 47 */     this.page = paramPageFormat;
/* 48 */     this.painter = paramPrintable;
/*    */   }
/*    */ 
/*    */   WPageDialog(Dialog paramDialog, PrinterJob paramPrinterJob, PageFormat paramPageFormat, Printable paramPrintable)
/*    */   {
/* 53 */     super(paramDialog, paramPrinterJob);
/* 54 */     this.page = paramPageFormat;
/* 55 */     this.painter = paramPrintable;
/*    */   }
/*    */ 
/*    */   public void addNotify() {
/* 59 */     synchronized (getTreeLock()) {
/* 60 */       Container localContainer = getParent();
/* 61 */       if ((localContainer != null) && (localContainer.getPeer() == null)) {
/* 62 */         localContainer.addNotify();
/*    */       }
/*    */ 
/* 65 */       if (getPeer() == null) {
/* 66 */         WPageDialogPeer localWPageDialogPeer = ((WToolkit)Toolkit.getDefaultToolkit()).createWPageDialog(this);
/*    */ 
/* 68 */         setPeer(localWPageDialogPeer);
/*    */       }
/* 70 */       super.addNotify();
/*    */     }
/*    */   }
/*    */ 
/*    */   private static native void initIDs();
/*    */ 
/*    */   static
/*    */   {
/* 39 */     initIDs();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WPageDialog
 * JD-Core Version:    0.6.2
 */