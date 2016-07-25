/*    */ package sun.awt.windows;
/*    */ 
/*    */ public class WPageDialogPeer extends WPrintDialogPeer
/*    */ {
/*    */   WPageDialogPeer(WPageDialog paramWPageDialog)
/*    */   {
/* 31 */     super(paramWPageDialog);
/*    */   }
/*    */ 
/*    */   private native boolean _show();
/*    */ 
/*    */   public void show()
/*    */   {
/* 41 */     new Thread(new Runnable()
/*    */     {
/*    */       public void run()
/*    */       {
/*    */         try {
/* 46 */           ((WPrintDialog)WPageDialogPeer.this.target).setRetVal(WPageDialogPeer.this._show());
/*    */         }
/*    */         catch (Exception localException)
/*    */         {
/*    */         }
/*    */ 
/* 52 */         ((WPrintDialog)WPageDialogPeer.this.target).hide();
/*    */       }
/*    */     }).start();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WPageDialogPeer
 * JD-Core Version:    0.6.2
 */