/*     */ package javax.swing.plaf.multi;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.util.Vector;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JSplitPane;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.SplitPaneUI;
/*     */ 
/*     */ public class MultiSplitPaneUI extends SplitPaneUI
/*     */ {
/*  51 */   protected Vector uis = new Vector();
/*     */ 
/*     */   public ComponentUI[] getUIs()
/*     */   {
/*  63 */     return MultiLookAndFeel.uisToArray(this.uis);
/*     */   }
/*     */ 
/*     */   public void resetToPreferredSizes(JSplitPane paramJSplitPane)
/*     */   {
/*  74 */     for (int i = 0; i < this.uis.size(); i++)
/*  75 */       ((SplitPaneUI)this.uis.elementAt(i)).resetToPreferredSizes(paramJSplitPane);
/*     */   }
/*     */ 
/*     */   public void setDividerLocation(JSplitPane paramJSplitPane, int paramInt)
/*     */   {
/*  83 */     for (int i = 0; i < this.uis.size(); i++)
/*  84 */       ((SplitPaneUI)this.uis.elementAt(i)).setDividerLocation(paramJSplitPane, paramInt);
/*     */   }
/*     */ 
/*     */   public int getDividerLocation(JSplitPane paramJSplitPane)
/*     */   {
/*  95 */     int i = ((SplitPaneUI)this.uis.elementAt(0)).getDividerLocation(paramJSplitPane);
/*     */ 
/*  97 */     for (int j = 1; j < this.uis.size(); j++) {
/*  98 */       ((SplitPaneUI)this.uis.elementAt(j)).getDividerLocation(paramJSplitPane);
/*     */     }
/* 100 */     return i;
/*     */   }
/*     */ 
/*     */   public int getMinimumDividerLocation(JSplitPane paramJSplitPane)
/*     */   {
/* 110 */     int i = ((SplitPaneUI)this.uis.elementAt(0)).getMinimumDividerLocation(paramJSplitPane);
/*     */ 
/* 112 */     for (int j = 1; j < this.uis.size(); j++) {
/* 113 */       ((SplitPaneUI)this.uis.elementAt(j)).getMinimumDividerLocation(paramJSplitPane);
/*     */     }
/* 115 */     return i;
/*     */   }
/*     */ 
/*     */   public int getMaximumDividerLocation(JSplitPane paramJSplitPane)
/*     */   {
/* 125 */     int i = ((SplitPaneUI)this.uis.elementAt(0)).getMaximumDividerLocation(paramJSplitPane);
/*     */ 
/* 127 */     for (int j = 1; j < this.uis.size(); j++) {
/* 128 */       ((SplitPaneUI)this.uis.elementAt(j)).getMaximumDividerLocation(paramJSplitPane);
/*     */     }
/* 130 */     return i;
/*     */   }
/*     */ 
/*     */   public void finishedPaintingChildren(JSplitPane paramJSplitPane, Graphics paramGraphics)
/*     */   {
/* 137 */     for (int i = 0; i < this.uis.size(); i++)
/* 138 */       ((SplitPaneUI)this.uis.elementAt(i)).finishedPaintingChildren(paramJSplitPane, paramGraphics);
/*     */   }
/*     */ 
/*     */   public boolean contains(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 153 */     boolean bool = ((ComponentUI)this.uis.elementAt(0)).contains(paramJComponent, paramInt1, paramInt2);
/*     */ 
/* 155 */     for (int i = 1; i < this.uis.size(); i++) {
/* 156 */       ((ComponentUI)this.uis.elementAt(i)).contains(paramJComponent, paramInt1, paramInt2);
/*     */     }
/* 158 */     return bool;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 165 */     for (int i = 0; i < this.uis.size(); i++)
/* 166 */       ((ComponentUI)this.uis.elementAt(i)).update(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 176 */     MultiSplitPaneUI localMultiSplitPaneUI = new MultiSplitPaneUI();
/* 177 */     return MultiLookAndFeel.createUIs(localMultiSplitPaneUI, ((MultiSplitPaneUI)localMultiSplitPaneUI).uis, paramJComponent);
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/* 186 */     for (int i = 0; i < this.uis.size(); i++)
/* 187 */       ((ComponentUI)this.uis.elementAt(i)).installUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/* 195 */     for (int i = 0; i < this.uis.size(); i++)
/* 196 */       ((ComponentUI)this.uis.elementAt(i)).uninstallUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 204 */     for (int i = 0; i < this.uis.size(); i++)
/* 205 */       ((ComponentUI)this.uis.elementAt(i)).paint(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 216 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getPreferredSize(paramJComponent);
/*     */ 
/* 218 */     for (int i = 1; i < this.uis.size(); i++) {
/* 219 */       ((ComponentUI)this.uis.elementAt(i)).getPreferredSize(paramJComponent);
/*     */     }
/* 221 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 231 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getMinimumSize(paramJComponent);
/*     */ 
/* 233 */     for (int i = 1; i < this.uis.size(); i++) {
/* 234 */       ((ComponentUI)this.uis.elementAt(i)).getMinimumSize(paramJComponent);
/*     */     }
/* 236 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent)
/*     */   {
/* 246 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getMaximumSize(paramJComponent);
/*     */ 
/* 248 */     for (int i = 1; i < this.uis.size(); i++) {
/* 249 */       ((ComponentUI)this.uis.elementAt(i)).getMaximumSize(paramJComponent);
/*     */     }
/* 251 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public int getAccessibleChildrenCount(JComponent paramJComponent)
/*     */   {
/* 261 */     int i = ((ComponentUI)this.uis.elementAt(0)).getAccessibleChildrenCount(paramJComponent);
/*     */ 
/* 263 */     for (int j = 1; j < this.uis.size(); j++) {
/* 264 */       ((ComponentUI)this.uis.elementAt(j)).getAccessibleChildrenCount(paramJComponent);
/*     */     }
/* 266 */     return i;
/*     */   }
/*     */ 
/*     */   public Accessible getAccessibleChild(JComponent paramJComponent, int paramInt)
/*     */   {
/* 276 */     Accessible localAccessible = ((ComponentUI)this.uis.elementAt(0)).getAccessibleChild(paramJComponent, paramInt);
/*     */ 
/* 278 */     for (int i = 1; i < this.uis.size(); i++) {
/* 279 */       ((ComponentUI)this.uis.elementAt(i)).getAccessibleChild(paramJComponent, paramInt);
/*     */     }
/* 281 */     return localAccessible;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.multi.MultiSplitPaneUI
 * JD-Core Version:    0.6.2
 */