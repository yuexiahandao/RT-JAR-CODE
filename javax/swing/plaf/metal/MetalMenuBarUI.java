/*    */ package javax.swing.plaf.metal;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import javax.swing.JComponent;
/*    */ import javax.swing.JMenuBar;
/*    */ import javax.swing.JToolBar;
/*    */ import javax.swing.UIManager;
/*    */ import javax.swing.plaf.ComponentUI;
/*    */ import javax.swing.plaf.UIResource;
/*    */ import javax.swing.plaf.basic.BasicMenuBarUI;
/*    */ 
/*    */ public class MetalMenuBarUI extends BasicMenuBarUI
/*    */ {
/*    */   public static ComponentUI createUI(JComponent paramJComponent)
/*    */   {
/* 51 */     if (paramJComponent == null) {
/* 52 */       throw new NullPointerException("Must pass in a non-null component");
/*    */     }
/* 54 */     return new MetalMenuBarUI();
/*    */   }
/*    */ 
/*    */   public void installUI(JComponent paramJComponent)
/*    */   {
/* 65 */     super.installUI(paramJComponent);
/* 66 */     MetalToolBarUI.register(paramJComponent);
/*    */   }
/*    */ 
/*    */   public void uninstallUI(JComponent paramJComponent)
/*    */   {
/* 77 */     super.uninstallUI(paramJComponent);
/* 78 */     MetalToolBarUI.unregister(paramJComponent);
/*    */   }
/*    */ 
/*    */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*    */   {
/* 94 */     boolean bool = paramJComponent.isOpaque();
/* 95 */     if (paramGraphics == null) {
/* 96 */       throw new NullPointerException("Graphics must be non-null");
/*    */     }
/* 98 */     if ((bool) && ((paramJComponent.getBackground() instanceof UIResource)) && (UIManager.get("MenuBar.gradient") != null))
/*    */     {
/* 100 */       if (MetalToolBarUI.doesMenuBarBorderToolBar((JMenuBar)paramJComponent)) {
/* 101 */         JToolBar localJToolBar = (JToolBar)MetalToolBarUI.findRegisteredComponentOfType(paramJComponent, JToolBar.class);
/*    */ 
/* 103 */         if ((localJToolBar.isOpaque()) && ((localJToolBar.getBackground() instanceof UIResource))) {
/* 104 */           MetalUtils.drawGradient(paramJComponent, paramGraphics, "MenuBar.gradient", 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight() + localJToolBar.getHeight(), true);
/*    */ 
/* 107 */           paint(paramGraphics, paramJComponent);
/* 108 */           return;
/*    */         }
/*    */       }
/* 111 */       MetalUtils.drawGradient(paramJComponent, paramGraphics, "MenuBar.gradient", 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight(), true);
/*    */ 
/* 113 */       paint(paramGraphics, paramJComponent);
/*    */     }
/*    */     else {
/* 116 */       super.update(paramGraphics, paramJComponent);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalMenuBarUI
 * JD-Core Version:    0.6.2
 */