/*    */ package javax.swing.plaf.synth;
/*    */ 
/*    */ import java.awt.Container;
/*    */ import java.awt.Dimension;
/*    */ import javax.swing.JPopupMenu;
/*    */ import javax.swing.plaf.basic.DefaultMenuLayout;
/*    */ 
/*    */ class SynthMenuLayout extends DefaultMenuLayout
/*    */ {
/*    */   public SynthMenuLayout(Container paramContainer, int paramInt)
/*    */   {
/* 41 */     super(paramContainer, paramInt);
/*    */   }
/*    */ 
/*    */   public Dimension preferredLayoutSize(Container paramContainer) {
/* 45 */     if ((paramContainer instanceof JPopupMenu)) {
/* 46 */       JPopupMenu localJPopupMenu = (JPopupMenu)paramContainer;
/* 47 */       localJPopupMenu.putClientProperty(SynthMenuItemLayoutHelper.MAX_ACC_OR_ARROW_WIDTH, null);
/*    */     }
/*    */ 
/* 51 */     return super.preferredLayoutSize(paramContainer);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthMenuLayout
 * JD-Core Version:    0.6.2
 */