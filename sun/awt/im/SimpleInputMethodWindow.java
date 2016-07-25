/*    */ package sun.awt.im;
/*    */ 
/*    */ import java.awt.Frame;
/*    */ 
/*    */ public class SimpleInputMethodWindow extends Frame
/*    */   implements InputMethodWindow
/*    */ {
/* 40 */   InputContext inputContext = null;
/*    */ 
/*    */   public SimpleInputMethodWindow(String paramString, InputContext paramInputContext)
/*    */   {
/* 46 */     super(paramString);
/* 47 */     if (paramInputContext != null) {
/* 48 */       this.inputContext = paramInputContext;
/*    */     }
/* 50 */     setFocusableWindowState(false);
/*    */   }
/*    */ 
/*    */   public void setInputContext(InputContext paramInputContext) {
/* 54 */     this.inputContext = paramInputContext;
/*    */   }
/*    */ 
/*    */   public java.awt.im.InputContext getInputContext() {
/* 58 */     if (this.inputContext != null) {
/* 59 */       return this.inputContext;
/*    */     }
/* 61 */     return super.getInputContext();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.im.SimpleInputMethodWindow
 * JD-Core Version:    0.6.2
 */