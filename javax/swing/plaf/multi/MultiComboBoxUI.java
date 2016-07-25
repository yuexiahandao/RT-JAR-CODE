/*     */ package javax.swing.plaf.multi;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.util.Vector;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.swing.JComboBox;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.plaf.ComboBoxUI;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ 
/*     */ public class MultiComboBoxUI extends ComboBoxUI
/*     */ {
/*  51 */   protected Vector uis = new Vector();
/*     */ 
/*     */   public ComponentUI[] getUIs()
/*     */   {
/*  63 */     return MultiLookAndFeel.uisToArray(this.uis);
/*     */   }
/*     */ 
/*     */   public boolean isFocusTraversable(JComboBox paramJComboBox)
/*     */   {
/*  77 */     boolean bool = ((ComboBoxUI)this.uis.elementAt(0)).isFocusTraversable(paramJComboBox);
/*     */ 
/*  79 */     for (int i = 1; i < this.uis.size(); i++) {
/*  80 */       ((ComboBoxUI)this.uis.elementAt(i)).isFocusTraversable(paramJComboBox);
/*     */     }
/*  82 */     return bool;
/*     */   }
/*     */ 
/*     */   public void setPopupVisible(JComboBox paramJComboBox, boolean paramBoolean)
/*     */   {
/*  89 */     for (int i = 0; i < this.uis.size(); i++)
/*  90 */       ((ComboBoxUI)this.uis.elementAt(i)).setPopupVisible(paramJComboBox, paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean isPopupVisible(JComboBox paramJComboBox)
/*     */   {
/* 101 */     boolean bool = ((ComboBoxUI)this.uis.elementAt(0)).isPopupVisible(paramJComboBox);
/*     */ 
/* 103 */     for (int i = 1; i < this.uis.size(); i++) {
/* 104 */       ((ComboBoxUI)this.uis.elementAt(i)).isPopupVisible(paramJComboBox);
/*     */     }
/* 106 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean contains(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 120 */     boolean bool = ((ComponentUI)this.uis.elementAt(0)).contains(paramJComponent, paramInt1, paramInt2);
/*     */ 
/* 122 */     for (int i = 1; i < this.uis.size(); i++) {
/* 123 */       ((ComponentUI)this.uis.elementAt(i)).contains(paramJComponent, paramInt1, paramInt2);
/*     */     }
/* 125 */     return bool;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 132 */     for (int i = 0; i < this.uis.size(); i++)
/* 133 */       ((ComponentUI)this.uis.elementAt(i)).update(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 143 */     MultiComboBoxUI localMultiComboBoxUI = new MultiComboBoxUI();
/* 144 */     return MultiLookAndFeel.createUIs(localMultiComboBoxUI, ((MultiComboBoxUI)localMultiComboBoxUI).uis, paramJComponent);
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/* 153 */     for (int i = 0; i < this.uis.size(); i++)
/* 154 */       ((ComponentUI)this.uis.elementAt(i)).installUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/* 162 */     for (int i = 0; i < this.uis.size(); i++)
/* 163 */       ((ComponentUI)this.uis.elementAt(i)).uninstallUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 171 */     for (int i = 0; i < this.uis.size(); i++)
/* 172 */       ((ComponentUI)this.uis.elementAt(i)).paint(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 183 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getPreferredSize(paramJComponent);
/*     */ 
/* 185 */     for (int i = 1; i < this.uis.size(); i++) {
/* 186 */       ((ComponentUI)this.uis.elementAt(i)).getPreferredSize(paramJComponent);
/*     */     }
/* 188 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 198 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getMinimumSize(paramJComponent);
/*     */ 
/* 200 */     for (int i = 1; i < this.uis.size(); i++) {
/* 201 */       ((ComponentUI)this.uis.elementAt(i)).getMinimumSize(paramJComponent);
/*     */     }
/* 203 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent)
/*     */   {
/* 213 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getMaximumSize(paramJComponent);
/*     */ 
/* 215 */     for (int i = 1; i < this.uis.size(); i++) {
/* 216 */       ((ComponentUI)this.uis.elementAt(i)).getMaximumSize(paramJComponent);
/*     */     }
/* 218 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public int getAccessibleChildrenCount(JComponent paramJComponent)
/*     */   {
/* 228 */     int i = ((ComponentUI)this.uis.elementAt(0)).getAccessibleChildrenCount(paramJComponent);
/*     */ 
/* 230 */     for (int j = 1; j < this.uis.size(); j++) {
/* 231 */       ((ComponentUI)this.uis.elementAt(j)).getAccessibleChildrenCount(paramJComponent);
/*     */     }
/* 233 */     return i;
/*     */   }
/*     */ 
/*     */   public Accessible getAccessibleChild(JComponent paramJComponent, int paramInt)
/*     */   {
/* 243 */     Accessible localAccessible = ((ComponentUI)this.uis.elementAt(0)).getAccessibleChild(paramJComponent, paramInt);
/*     */ 
/* 245 */     for (int i = 1; i < this.uis.size(); i++) {
/* 246 */       ((ComponentUI)this.uis.elementAt(i)).getAccessibleChild(paramJComponent, paramInt);
/*     */     }
/* 248 */     return localAccessible;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.multi.MultiComboBoxUI
 * JD-Core Version:    0.6.2
 */