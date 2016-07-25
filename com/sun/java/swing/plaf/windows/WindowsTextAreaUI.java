/*    */ package com.sun.java.swing.plaf.windows;
/*    */ 
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicTextAreaUI;
/*    */ import javax.swing.text.Caret;
/*    */ 
/*    */ public class WindowsTextAreaUI extends BasicTextAreaUI
/*    */ {
/*    */   protected Caret createCaret()
/*    */   {
/* 55 */     return new WindowsTextUI.WindowsCaret();
/*    */   }
/*    */ 
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 65 */     return new WindowsTextAreaUI();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsTextAreaUI
 * JD-Core Version:    0.6.2
 */