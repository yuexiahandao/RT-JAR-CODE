/*    */ package sun.awt.im;
/*    */ 
/*    */ import javax.swing.JFrame;
/*    */ import javax.swing.JRootPane;
/*    */ 
/*    */ public class InputMethodJFrame extends JFrame
/*    */   implements InputMethodWindow
/*    */ {
/* 41 */   InputContext inputContext = null;
/*    */ 
/*    */   public InputMethodJFrame(String paramString, InputContext paramInputContext)
/*    */   {
/* 47 */     super(paramString);
/*    */ 
/* 49 */     if (JFrame.isDefaultLookAndFeelDecorated())
/*    */     {
/* 51 */       setUndecorated(true);
/* 52 */       getRootPane().setWindowDecorationStyle(0);
/*    */     }
/* 54 */     if (paramInputContext != null) {
/* 55 */       this.inputContext = paramInputContext;
/*    */     }
/* 57 */     setFocusableWindowState(false);
/*    */   }
/*    */ 
/*    */   public void setInputContext(InputContext paramInputContext) {
/* 61 */     this.inputContext = paramInputContext;
/*    */   }
/*    */ 
/*    */   public java.awt.im.InputContext getInputContext() {
/* 65 */     if (this.inputContext != null) {
/* 66 */       return this.inputContext;
/*    */     }
/* 68 */     return super.getInputContext();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.im.InputMethodJFrame
 * JD-Core Version:    0.6.2
 */