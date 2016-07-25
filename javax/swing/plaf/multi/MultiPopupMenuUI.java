/*     */ package javax.swing.plaf.multi;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.util.Vector;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JPopupMenu;
/*     */ import javax.swing.Popup;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.PopupMenuUI;
/*     */ 
/*     */ public class MultiPopupMenuUI extends PopupMenuUI
/*     */ {
/*  53 */   protected Vector uis = new Vector();
/*     */ 
/*     */   public ComponentUI[] getUIs()
/*     */   {
/*  65 */     return MultiLookAndFeel.uisToArray(this.uis);
/*     */   }
/*     */ 
/*     */   public boolean isPopupTrigger(MouseEvent paramMouseEvent)
/*     */   {
/*  80 */     boolean bool = ((PopupMenuUI)this.uis.elementAt(0)).isPopupTrigger(paramMouseEvent);
/*     */ 
/*  82 */     for (int i = 1; i < this.uis.size(); i++) {
/*  83 */       ((PopupMenuUI)this.uis.elementAt(i)).isPopupTrigger(paramMouseEvent);
/*     */     }
/*  85 */     return bool;
/*     */   }
/*     */ 
/*     */   public Popup getPopup(JPopupMenu paramJPopupMenu, int paramInt1, int paramInt2)
/*     */   {
/*  96 */     Popup localPopup = ((PopupMenuUI)this.uis.elementAt(0)).getPopup(paramJPopupMenu, paramInt1, paramInt2);
/*     */ 
/*  98 */     for (int i = 1; i < this.uis.size(); i++) {
/*  99 */       ((PopupMenuUI)this.uis.elementAt(i)).getPopup(paramJPopupMenu, paramInt1, paramInt2);
/*     */     }
/* 101 */     return localPopup;
/*     */   }
/*     */ 
/*     */   public boolean contains(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 115 */     boolean bool = ((ComponentUI)this.uis.elementAt(0)).contains(paramJComponent, paramInt1, paramInt2);
/*     */ 
/* 117 */     for (int i = 1; i < this.uis.size(); i++) {
/* 118 */       ((ComponentUI)this.uis.elementAt(i)).contains(paramJComponent, paramInt1, paramInt2);
/*     */     }
/* 120 */     return bool;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 127 */     for (int i = 0; i < this.uis.size(); i++)
/* 128 */       ((ComponentUI)this.uis.elementAt(i)).update(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 138 */     MultiPopupMenuUI localMultiPopupMenuUI = new MultiPopupMenuUI();
/* 139 */     return MultiLookAndFeel.createUIs(localMultiPopupMenuUI, ((MultiPopupMenuUI)localMultiPopupMenuUI).uis, paramJComponent);
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/* 148 */     for (int i = 0; i < this.uis.size(); i++)
/* 149 */       ((ComponentUI)this.uis.elementAt(i)).installUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/* 157 */     for (int i = 0; i < this.uis.size(); i++)
/* 158 */       ((ComponentUI)this.uis.elementAt(i)).uninstallUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 166 */     for (int i = 0; i < this.uis.size(); i++)
/* 167 */       ((ComponentUI)this.uis.elementAt(i)).paint(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 178 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getPreferredSize(paramJComponent);
/*     */ 
/* 180 */     for (int i = 1; i < this.uis.size(); i++) {
/* 181 */       ((ComponentUI)this.uis.elementAt(i)).getPreferredSize(paramJComponent);
/*     */     }
/* 183 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 193 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getMinimumSize(paramJComponent);
/*     */ 
/* 195 */     for (int i = 1; i < this.uis.size(); i++) {
/* 196 */       ((ComponentUI)this.uis.elementAt(i)).getMinimumSize(paramJComponent);
/*     */     }
/* 198 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent)
/*     */   {
/* 208 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getMaximumSize(paramJComponent);
/*     */ 
/* 210 */     for (int i = 1; i < this.uis.size(); i++) {
/* 211 */       ((ComponentUI)this.uis.elementAt(i)).getMaximumSize(paramJComponent);
/*     */     }
/* 213 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public int getAccessibleChildrenCount(JComponent paramJComponent)
/*     */   {
/* 223 */     int i = ((ComponentUI)this.uis.elementAt(0)).getAccessibleChildrenCount(paramJComponent);
/*     */ 
/* 225 */     for (int j = 1; j < this.uis.size(); j++) {
/* 226 */       ((ComponentUI)this.uis.elementAt(j)).getAccessibleChildrenCount(paramJComponent);
/*     */     }
/* 228 */     return i;
/*     */   }
/*     */ 
/*     */   public Accessible getAccessibleChild(JComponent paramJComponent, int paramInt)
/*     */   {
/* 238 */     Accessible localAccessible = ((ComponentUI)this.uis.elementAt(0)).getAccessibleChild(paramJComponent, paramInt);
/*     */ 
/* 240 */     for (int i = 1; i < this.uis.size(); i++) {
/* 241 */       ((ComponentUI)this.uis.elementAt(i)).getAccessibleChild(paramJComponent, paramInt);
/*     */     }
/* 243 */     return localAccessible;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.multi.MultiPopupMenuUI
 * JD-Core Version:    0.6.2
 */