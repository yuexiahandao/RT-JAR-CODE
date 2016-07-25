/*     */ package javax.swing.plaf.multi;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.util.Vector;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.MenuItemUI;
/*     */ 
/*     */ public class MultiMenuItemUI extends MenuItemUI
/*     */ {
/*  51 */   protected Vector uis = new Vector();
/*     */ 
/*     */   public ComponentUI[] getUIs()
/*     */   {
/*  63 */     return MultiLookAndFeel.uisToArray(this.uis);
/*     */   }
/*     */ 
/*     */   public boolean contains(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/*  85 */     boolean bool = ((ComponentUI)this.uis.elementAt(0)).contains(paramJComponent, paramInt1, paramInt2);
/*     */ 
/*  87 */     for (int i = 1; i < this.uis.size(); i++) {
/*  88 */       ((ComponentUI)this.uis.elementAt(i)).contains(paramJComponent, paramInt1, paramInt2);
/*     */     }
/*  90 */     return bool;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/*  97 */     for (int i = 0; i < this.uis.size(); i++)
/*  98 */       ((ComponentUI)this.uis.elementAt(i)).update(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 108 */     MultiMenuItemUI localMultiMenuItemUI = new MultiMenuItemUI();
/* 109 */     return MultiLookAndFeel.createUIs(localMultiMenuItemUI, ((MultiMenuItemUI)localMultiMenuItemUI).uis, paramJComponent);
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/* 118 */     for (int i = 0; i < this.uis.size(); i++)
/* 119 */       ((ComponentUI)this.uis.elementAt(i)).installUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/* 127 */     for (int i = 0; i < this.uis.size(); i++)
/* 128 */       ((ComponentUI)this.uis.elementAt(i)).uninstallUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 136 */     for (int i = 0; i < this.uis.size(); i++)
/* 137 */       ((ComponentUI)this.uis.elementAt(i)).paint(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 148 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getPreferredSize(paramJComponent);
/*     */ 
/* 150 */     for (int i = 1; i < this.uis.size(); i++) {
/* 151 */       ((ComponentUI)this.uis.elementAt(i)).getPreferredSize(paramJComponent);
/*     */     }
/* 153 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 163 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getMinimumSize(paramJComponent);
/*     */ 
/* 165 */     for (int i = 1; i < this.uis.size(); i++) {
/* 166 */       ((ComponentUI)this.uis.elementAt(i)).getMinimumSize(paramJComponent);
/*     */     }
/* 168 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent)
/*     */   {
/* 178 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getMaximumSize(paramJComponent);
/*     */ 
/* 180 */     for (int i = 1; i < this.uis.size(); i++) {
/* 181 */       ((ComponentUI)this.uis.elementAt(i)).getMaximumSize(paramJComponent);
/*     */     }
/* 183 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public int getAccessibleChildrenCount(JComponent paramJComponent)
/*     */   {
/* 193 */     int i = ((ComponentUI)this.uis.elementAt(0)).getAccessibleChildrenCount(paramJComponent);
/*     */ 
/* 195 */     for (int j = 1; j < this.uis.size(); j++) {
/* 196 */       ((ComponentUI)this.uis.elementAt(j)).getAccessibleChildrenCount(paramJComponent);
/*     */     }
/* 198 */     return i;
/*     */   }
/*     */ 
/*     */   public Accessible getAccessibleChild(JComponent paramJComponent, int paramInt)
/*     */   {
/* 208 */     Accessible localAccessible = ((ComponentUI)this.uis.elementAt(0)).getAccessibleChild(paramJComponent, paramInt);
/*     */ 
/* 210 */     for (int i = 1; i < this.uis.size(); i++) {
/* 211 */       ((ComponentUI)this.uis.elementAt(i)).getAccessibleChild(paramJComponent, paramInt);
/*     */     }
/* 213 */     return localAccessible;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.multi.MultiMenuItemUI
 * JD-Core Version:    0.6.2
 */