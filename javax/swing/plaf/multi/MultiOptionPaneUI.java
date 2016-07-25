/*     */ package javax.swing.plaf.multi;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.util.Vector;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.OptionPaneUI;
/*     */ 
/*     */ public class MultiOptionPaneUI extends OptionPaneUI
/*     */ {
/*  51 */   protected Vector uis = new Vector();
/*     */ 
/*     */   public ComponentUI[] getUIs()
/*     */   {
/*  63 */     return MultiLookAndFeel.uisToArray(this.uis);
/*     */   }
/*     */ 
/*     */   public void selectInitialValue(JOptionPane paramJOptionPane)
/*     */   {
/*  74 */     for (int i = 0; i < this.uis.size(); i++)
/*  75 */       ((OptionPaneUI)this.uis.elementAt(i)).selectInitialValue(paramJOptionPane);
/*     */   }
/*     */ 
/*     */   public boolean containsCustomComponents(JOptionPane paramJOptionPane)
/*     */   {
/*  86 */     boolean bool = ((OptionPaneUI)this.uis.elementAt(0)).containsCustomComponents(paramJOptionPane);
/*     */ 
/*  88 */     for (int i = 1; i < this.uis.size(); i++) {
/*  89 */       ((OptionPaneUI)this.uis.elementAt(i)).containsCustomComponents(paramJOptionPane);
/*     */     }
/*  91 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean contains(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 105 */     boolean bool = ((ComponentUI)this.uis.elementAt(0)).contains(paramJComponent, paramInt1, paramInt2);
/*     */ 
/* 107 */     for (int i = 1; i < this.uis.size(); i++) {
/* 108 */       ((ComponentUI)this.uis.elementAt(i)).contains(paramJComponent, paramInt1, paramInt2);
/*     */     }
/* 110 */     return bool;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 117 */     for (int i = 0; i < this.uis.size(); i++)
/* 118 */       ((ComponentUI)this.uis.elementAt(i)).update(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 128 */     MultiOptionPaneUI localMultiOptionPaneUI = new MultiOptionPaneUI();
/* 129 */     return MultiLookAndFeel.createUIs(localMultiOptionPaneUI, ((MultiOptionPaneUI)localMultiOptionPaneUI).uis, paramJComponent);
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/* 138 */     for (int i = 0; i < this.uis.size(); i++)
/* 139 */       ((ComponentUI)this.uis.elementAt(i)).installUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/* 147 */     for (int i = 0; i < this.uis.size(); i++)
/* 148 */       ((ComponentUI)this.uis.elementAt(i)).uninstallUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 156 */     for (int i = 0; i < this.uis.size(); i++)
/* 157 */       ((ComponentUI)this.uis.elementAt(i)).paint(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 168 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getPreferredSize(paramJComponent);
/*     */ 
/* 170 */     for (int i = 1; i < this.uis.size(); i++) {
/* 171 */       ((ComponentUI)this.uis.elementAt(i)).getPreferredSize(paramJComponent);
/*     */     }
/* 173 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 183 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getMinimumSize(paramJComponent);
/*     */ 
/* 185 */     for (int i = 1; i < this.uis.size(); i++) {
/* 186 */       ((ComponentUI)this.uis.elementAt(i)).getMinimumSize(paramJComponent);
/*     */     }
/* 188 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent)
/*     */   {
/* 198 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getMaximumSize(paramJComponent);
/*     */ 
/* 200 */     for (int i = 1; i < this.uis.size(); i++) {
/* 201 */       ((ComponentUI)this.uis.elementAt(i)).getMaximumSize(paramJComponent);
/*     */     }
/* 203 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public int getAccessibleChildrenCount(JComponent paramJComponent)
/*     */   {
/* 213 */     int i = ((ComponentUI)this.uis.elementAt(0)).getAccessibleChildrenCount(paramJComponent);
/*     */ 
/* 215 */     for (int j = 1; j < this.uis.size(); j++) {
/* 216 */       ((ComponentUI)this.uis.elementAt(j)).getAccessibleChildrenCount(paramJComponent);
/*     */     }
/* 218 */     return i;
/*     */   }
/*     */ 
/*     */   public Accessible getAccessibleChild(JComponent paramJComponent, int paramInt)
/*     */   {
/* 228 */     Accessible localAccessible = ((ComponentUI)this.uis.elementAt(0)).getAccessibleChild(paramJComponent, paramInt);
/*     */ 
/* 230 */     for (int i = 1; i < this.uis.size(); i++) {
/* 231 */       ((ComponentUI)this.uis.elementAt(i)).getAccessibleChild(paramJComponent, paramInt);
/*     */     }
/* 233 */     return localAccessible;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.multi.MultiOptionPaneUI
 * JD-Core Version:    0.6.2
 */