/*     */ package javax.swing.plaf.multi;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.util.Vector;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JList;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.ListUI;
/*     */ 
/*     */ public class MultiListUI extends ListUI
/*     */ {
/*  53 */   protected Vector uis = new Vector();
/*     */ 
/*     */   public ComponentUI[] getUIs()
/*     */   {
/*  65 */     return MultiLookAndFeel.uisToArray(this.uis);
/*     */   }
/*     */ 
/*     */   public int locationToIndex(JList paramJList, Point paramPoint)
/*     */   {
/*  79 */     int i = ((ListUI)this.uis.elementAt(0)).locationToIndex(paramJList, paramPoint);
/*     */ 
/*  81 */     for (int j = 1; j < this.uis.size(); j++) {
/*  82 */       ((ListUI)this.uis.elementAt(j)).locationToIndex(paramJList, paramPoint);
/*     */     }
/*  84 */     return i;
/*     */   }
/*     */ 
/*     */   public Point indexToLocation(JList paramJList, int paramInt)
/*     */   {
/*  94 */     Point localPoint = ((ListUI)this.uis.elementAt(0)).indexToLocation(paramJList, paramInt);
/*     */ 
/*  96 */     for (int i = 1; i < this.uis.size(); i++) {
/*  97 */       ((ListUI)this.uis.elementAt(i)).indexToLocation(paramJList, paramInt);
/*     */     }
/*  99 */     return localPoint;
/*     */   }
/*     */ 
/*     */   public Rectangle getCellBounds(JList paramJList, int paramInt1, int paramInt2)
/*     */   {
/* 109 */     Rectangle localRectangle = ((ListUI)this.uis.elementAt(0)).getCellBounds(paramJList, paramInt1, paramInt2);
/*     */ 
/* 111 */     for (int i = 1; i < this.uis.size(); i++) {
/* 112 */       ((ListUI)this.uis.elementAt(i)).getCellBounds(paramJList, paramInt1, paramInt2);
/*     */     }
/* 114 */     return localRectangle;
/*     */   }
/*     */ 
/*     */   public boolean contains(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 128 */     boolean bool = ((ComponentUI)this.uis.elementAt(0)).contains(paramJComponent, paramInt1, paramInt2);
/*     */ 
/* 130 */     for (int i = 1; i < this.uis.size(); i++) {
/* 131 */       ((ComponentUI)this.uis.elementAt(i)).contains(paramJComponent, paramInt1, paramInt2);
/*     */     }
/* 133 */     return bool;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 140 */     for (int i = 0; i < this.uis.size(); i++)
/* 141 */       ((ComponentUI)this.uis.elementAt(i)).update(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 151 */     MultiListUI localMultiListUI = new MultiListUI();
/* 152 */     return MultiLookAndFeel.createUIs(localMultiListUI, ((MultiListUI)localMultiListUI).uis, paramJComponent);
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/* 161 */     for (int i = 0; i < this.uis.size(); i++)
/* 162 */       ((ComponentUI)this.uis.elementAt(i)).installUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/* 170 */     for (int i = 0; i < this.uis.size(); i++)
/* 171 */       ((ComponentUI)this.uis.elementAt(i)).uninstallUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 179 */     for (int i = 0; i < this.uis.size(); i++)
/* 180 */       ((ComponentUI)this.uis.elementAt(i)).paint(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 191 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getPreferredSize(paramJComponent);
/*     */ 
/* 193 */     for (int i = 1; i < this.uis.size(); i++) {
/* 194 */       ((ComponentUI)this.uis.elementAt(i)).getPreferredSize(paramJComponent);
/*     */     }
/* 196 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 206 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getMinimumSize(paramJComponent);
/*     */ 
/* 208 */     for (int i = 1; i < this.uis.size(); i++) {
/* 209 */       ((ComponentUI)this.uis.elementAt(i)).getMinimumSize(paramJComponent);
/*     */     }
/* 211 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent)
/*     */   {
/* 221 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getMaximumSize(paramJComponent);
/*     */ 
/* 223 */     for (int i = 1; i < this.uis.size(); i++) {
/* 224 */       ((ComponentUI)this.uis.elementAt(i)).getMaximumSize(paramJComponent);
/*     */     }
/* 226 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public int getAccessibleChildrenCount(JComponent paramJComponent)
/*     */   {
/* 236 */     int i = ((ComponentUI)this.uis.elementAt(0)).getAccessibleChildrenCount(paramJComponent);
/*     */ 
/* 238 */     for (int j = 1; j < this.uis.size(); j++) {
/* 239 */       ((ComponentUI)this.uis.elementAt(j)).getAccessibleChildrenCount(paramJComponent);
/*     */     }
/* 241 */     return i;
/*     */   }
/*     */ 
/*     */   public Accessible getAccessibleChild(JComponent paramJComponent, int paramInt)
/*     */   {
/* 251 */     Accessible localAccessible = ((ComponentUI)this.uis.elementAt(0)).getAccessibleChild(paramJComponent, paramInt);
/*     */ 
/* 253 */     for (int i = 1; i < this.uis.size(); i++) {
/* 254 */       ((ComponentUI)this.uis.elementAt(i)).getAccessibleChild(paramJComponent, paramInt);
/*     */     }
/* 256 */     return localAccessible;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.multi.MultiListUI
 * JD-Core Version:    0.6.2
 */