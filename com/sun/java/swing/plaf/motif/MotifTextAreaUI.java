/*    */ package com.sun.java.swing.plaf.motif;
/*    */ 
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicTextAreaUI;
/*    */ import javax.swing.text.Caret;
/*    */ 
/*    */ public class MotifTextAreaUI extends BasicTextAreaUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 55 */     return new MotifTextAreaUI();
/*    */   }
/*    */ 
/*    */   protected Caret createCaret()
/*    */   {
/* 67 */     return MotifTextUI.createCaret();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifTextAreaUI
 * JD-Core Version:    0.6.2
 */