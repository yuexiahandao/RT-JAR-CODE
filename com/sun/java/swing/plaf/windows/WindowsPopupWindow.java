/*    */ package com.sun.java.swing.plaf.windows;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Window;
/*    */ import javax.swing.JWindow;
/*    */ 
/*    */ class WindowsPopupWindow extends JWindow
/*    */ {
/*    */   static final int UNDEFINED_WINDOW_TYPE = 0;
/*    */   static final int TOOLTIP_WINDOW_TYPE = 1;
/*    */   static final int MENU_WINDOW_TYPE = 2;
/*    */   static final int SUBMENU_WINDOW_TYPE = 3;
/*    */   static final int POPUPMENU_WINDOW_TYPE = 4;
/*    */   static final int COMBOBOX_POPUP_WINDOW_TYPE = 5;
/*    */   private int windowType;
/*    */ 
/*    */   WindowsPopupWindow(Window paramWindow)
/*    */   {
/* 64 */     super(paramWindow);
/* 65 */     setFocusableWindowState(false);
/*    */   }
/*    */ 
/*    */   void setWindowType(int paramInt) {
/* 69 */     this.windowType = paramInt;
/*    */   }
/*    */ 
/*    */   int getWindowType() {
/* 73 */     return this.windowType;
/*    */   }
/*    */ 
/*    */   public void update(Graphics paramGraphics) {
/* 77 */     paint(paramGraphics);
/*    */   }
/*    */ 
/*    */   public void hide() {
/* 81 */     super.hide();
/*    */ 
/* 88 */     removeNotify();
/*    */   }
/*    */ 
/*    */   public void show() {
/* 92 */     super.show();
/* 93 */     pack();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.java.swing.plaf.windows.WindowsPopupWindow
 * JD-Core Version:    0.6.2
 */