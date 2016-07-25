/*     */ package javax.swing.plaf.multi;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.util.Vector;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTabbedPane;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.TabbedPaneUI;
/*     */ 
/*     */ public class MultiTabbedPaneUI extends TabbedPaneUI
/*     */ {
/*  52 */   protected Vector uis = new Vector();
/*     */ 
/*     */   public ComponentUI[] getUIs()
/*     */   {
/*  64 */     return MultiLookAndFeel.uisToArray(this.uis);
/*     */   }
/*     */ 
/*     */   public int tabForCoordinate(JTabbedPane paramJTabbedPane, int paramInt1, int paramInt2)
/*     */   {
/*  78 */     int i = ((TabbedPaneUI)this.uis.elementAt(0)).tabForCoordinate(paramJTabbedPane, paramInt1, paramInt2);
/*     */ 
/*  80 */     for (int j = 1; j < this.uis.size(); j++) {
/*  81 */       ((TabbedPaneUI)this.uis.elementAt(j)).tabForCoordinate(paramJTabbedPane, paramInt1, paramInt2);
/*     */     }
/*  83 */     return i;
/*     */   }
/*     */ 
/*     */   public Rectangle getTabBounds(JTabbedPane paramJTabbedPane, int paramInt)
/*     */   {
/*  93 */     Rectangle localRectangle = ((TabbedPaneUI)this.uis.elementAt(0)).getTabBounds(paramJTabbedPane, paramInt);
/*     */ 
/*  95 */     for (int i = 1; i < this.uis.size(); i++) {
/*  96 */       ((TabbedPaneUI)this.uis.elementAt(i)).getTabBounds(paramJTabbedPane, paramInt);
/*     */     }
/*  98 */     return localRectangle;
/*     */   }
/*     */ 
/*     */   public int getTabRunCount(JTabbedPane paramJTabbedPane)
/*     */   {
/* 108 */     int i = ((TabbedPaneUI)this.uis.elementAt(0)).getTabRunCount(paramJTabbedPane);
/*     */ 
/* 110 */     for (int j = 1; j < this.uis.size(); j++) {
/* 111 */       ((TabbedPaneUI)this.uis.elementAt(j)).getTabRunCount(paramJTabbedPane);
/*     */     }
/* 113 */     return i;
/*     */   }
/*     */ 
/*     */   public boolean contains(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 127 */     boolean bool = ((ComponentUI)this.uis.elementAt(0)).contains(paramJComponent, paramInt1, paramInt2);
/*     */ 
/* 129 */     for (int i = 1; i < this.uis.size(); i++) {
/* 130 */       ((ComponentUI)this.uis.elementAt(i)).contains(paramJComponent, paramInt1, paramInt2);
/*     */     }
/* 132 */     return bool;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 139 */     for (int i = 0; i < this.uis.size(); i++)
/* 140 */       ((ComponentUI)this.uis.elementAt(i)).update(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 150 */     MultiTabbedPaneUI localMultiTabbedPaneUI = new MultiTabbedPaneUI();
/* 151 */     return MultiLookAndFeel.createUIs(localMultiTabbedPaneUI, ((MultiTabbedPaneUI)localMultiTabbedPaneUI).uis, paramJComponent);
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/* 160 */     for (int i = 0; i < this.uis.size(); i++)
/* 161 */       ((ComponentUI)this.uis.elementAt(i)).installUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/* 169 */     for (int i = 0; i < this.uis.size(); i++)
/* 170 */       ((ComponentUI)this.uis.elementAt(i)).uninstallUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 178 */     for (int i = 0; i < this.uis.size(); i++)
/* 179 */       ((ComponentUI)this.uis.elementAt(i)).paint(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 190 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getPreferredSize(paramJComponent);
/*     */ 
/* 192 */     for (int i = 1; i < this.uis.size(); i++) {
/* 193 */       ((ComponentUI)this.uis.elementAt(i)).getPreferredSize(paramJComponent);
/*     */     }
/* 195 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 205 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getMinimumSize(paramJComponent);
/*     */ 
/* 207 */     for (int i = 1; i < this.uis.size(); i++) {
/* 208 */       ((ComponentUI)this.uis.elementAt(i)).getMinimumSize(paramJComponent);
/*     */     }
/* 210 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent)
/*     */   {
/* 220 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getMaximumSize(paramJComponent);
/*     */ 
/* 222 */     for (int i = 1; i < this.uis.size(); i++) {
/* 223 */       ((ComponentUI)this.uis.elementAt(i)).getMaximumSize(paramJComponent);
/*     */     }
/* 225 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public int getAccessibleChildrenCount(JComponent paramJComponent)
/*     */   {
/* 235 */     int i = ((ComponentUI)this.uis.elementAt(0)).getAccessibleChildrenCount(paramJComponent);
/*     */ 
/* 237 */     for (int j = 1; j < this.uis.size(); j++) {
/* 238 */       ((ComponentUI)this.uis.elementAt(j)).getAccessibleChildrenCount(paramJComponent);
/*     */     }
/* 240 */     return i;
/*     */   }
/*     */ 
/*     */   public Accessible getAccessibleChild(JComponent paramJComponent, int paramInt)
/*     */   {
/* 250 */     Accessible localAccessible = ((ComponentUI)this.uis.elementAt(0)).getAccessibleChild(paramJComponent, paramInt);
/*     */ 
/* 252 */     for (int i = 1; i < this.uis.size(); i++) {
/* 253 */       ((ComponentUI)this.uis.elementAt(i)).getAccessibleChild(paramJComponent, paramInt);
/*     */     }
/* 255 */     return localAccessible;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.multi.MultiTabbedPaneUI
 * JD-Core Version:    0.6.2
 */