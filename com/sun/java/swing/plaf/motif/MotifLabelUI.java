/*    */ package com.sun.java.swing.plaf.motif;
/*    */ 
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.basic.BasicLabelUI;
/*    */ import sun.awt.AppContext;
/*    */ 
/*    */ public class MotifLabelUI extends BasicLabelUI
/*    */ {
/* 49 */   private static final Object MOTIF_LABEL_UI_KEY = new Object();
/*    */ 
/*    */   public static ComponentUI createUI(JComponent paramJComponent) {
/* 52 */     AppContext localAppContext = AppContext.getAppContext();
/* 53 */     MotifLabelUI localMotifLabelUI = (MotifLabelUI)localAppContext.get(MOTIF_LABEL_UI_KEY);
/*    */ 
/* 55 */     if (localMotifLabelUI == null) {
/* 56 */       localMotifLabelUI = new MotifLabelUI();
/* 57 */       localAppContext.put(MOTIF_LABEL_UI_KEY, localMotifLabelUI);
/*    */     }
/* 59 */     return localMotifLabelUI;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.motif.MotifLabelUI
 * JD-Core Version:    0.6.2
 */