/*     */ package javax.swing.plaf.multi;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Point;
/*     */ import java.awt.Rectangle;
/*     */ import java.util.Vector;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.TextUI;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.EditorKit;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.Position.Bias;
/*     */ import javax.swing.text.View;
/*     */ 
/*     */ public class MultiTextUI extends TextUI
/*     */ {
/*  58 */   protected Vector uis = new Vector();
/*     */ 
/*     */   public ComponentUI[] getUIs()
/*     */   {
/*  70 */     return MultiLookAndFeel.uisToArray(this.uis);
/*     */   }
/*     */ 
/*     */   public String getToolTipText(JTextComponent paramJTextComponent, Point paramPoint)
/*     */   {
/*  85 */     String str = ((TextUI)this.uis.elementAt(0)).getToolTipText(paramJTextComponent, paramPoint);
/*     */ 
/*  87 */     for (int i = 1; i < this.uis.size(); i++) {
/*  88 */       ((TextUI)this.uis.elementAt(i)).getToolTipText(paramJTextComponent, paramPoint);
/*     */     }
/*  90 */     return str;
/*     */   }
/*     */ 
/*     */   public Rectangle modelToView(JTextComponent paramJTextComponent, int paramInt)
/*     */     throws BadLocationException
/*     */   {
/* 101 */     Rectangle localRectangle = ((TextUI)this.uis.elementAt(0)).modelToView(paramJTextComponent, paramInt);
/*     */ 
/* 103 */     for (int i = 1; i < this.uis.size(); i++) {
/* 104 */       ((TextUI)this.uis.elementAt(i)).modelToView(paramJTextComponent, paramInt);
/*     */     }
/* 106 */     return localRectangle;
/*     */   }
/*     */ 
/*     */   public Rectangle modelToView(JTextComponent paramJTextComponent, int paramInt, Position.Bias paramBias)
/*     */     throws BadLocationException
/*     */   {
/* 117 */     Rectangle localRectangle = ((TextUI)this.uis.elementAt(0)).modelToView(paramJTextComponent, paramInt, paramBias);
/*     */ 
/* 119 */     for (int i = 1; i < this.uis.size(); i++) {
/* 120 */       ((TextUI)this.uis.elementAt(i)).modelToView(paramJTextComponent, paramInt, paramBias);
/*     */     }
/* 122 */     return localRectangle;
/*     */   }
/*     */ 
/*     */   public int viewToModel(JTextComponent paramJTextComponent, Point paramPoint)
/*     */   {
/* 132 */     int i = ((TextUI)this.uis.elementAt(0)).viewToModel(paramJTextComponent, paramPoint);
/*     */ 
/* 134 */     for (int j = 1; j < this.uis.size(); j++) {
/* 135 */       ((TextUI)this.uis.elementAt(j)).viewToModel(paramJTextComponent, paramPoint);
/*     */     }
/* 137 */     return i;
/*     */   }
/*     */ 
/*     */   public int viewToModel(JTextComponent paramJTextComponent, Point paramPoint, Position.Bias[] paramArrayOfBias)
/*     */   {
/* 147 */     int i = ((TextUI)this.uis.elementAt(0)).viewToModel(paramJTextComponent, paramPoint, paramArrayOfBias);
/*     */ 
/* 149 */     for (int j = 1; j < this.uis.size(); j++) {
/* 150 */       ((TextUI)this.uis.elementAt(j)).viewToModel(paramJTextComponent, paramPoint, paramArrayOfBias);
/*     */     }
/* 152 */     return i;
/*     */   }
/*     */ 
/*     */   public int getNextVisualPositionFrom(JTextComponent paramJTextComponent, int paramInt1, Position.Bias paramBias, int paramInt2, Position.Bias[] paramArrayOfBias)
/*     */     throws BadLocationException
/*     */   {
/* 163 */     int i = ((TextUI)this.uis.elementAt(0)).getNextVisualPositionFrom(paramJTextComponent, paramInt1, paramBias, paramInt2, paramArrayOfBias);
/*     */ 
/* 165 */     for (int j = 1; j < this.uis.size(); j++) {
/* 166 */       ((TextUI)this.uis.elementAt(j)).getNextVisualPositionFrom(paramJTextComponent, paramInt1, paramBias, paramInt2, paramArrayOfBias);
/*     */     }
/* 168 */     return i;
/*     */   }
/*     */ 
/*     */   public void damageRange(JTextComponent paramJTextComponent, int paramInt1, int paramInt2)
/*     */   {
/* 175 */     for (int i = 0; i < this.uis.size(); i++)
/* 176 */       ((TextUI)this.uis.elementAt(i)).damageRange(paramJTextComponent, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void damageRange(JTextComponent paramJTextComponent, int paramInt1, int paramInt2, Position.Bias paramBias1, Position.Bias paramBias2)
/*     */   {
/* 184 */     for (int i = 0; i < this.uis.size(); i++)
/* 185 */       ((TextUI)this.uis.elementAt(i)).damageRange(paramJTextComponent, paramInt1, paramInt2, paramBias1, paramBias2);
/*     */   }
/*     */ 
/*     */   public EditorKit getEditorKit(JTextComponent paramJTextComponent)
/*     */   {
/* 196 */     EditorKit localEditorKit = ((TextUI)this.uis.elementAt(0)).getEditorKit(paramJTextComponent);
/*     */ 
/* 198 */     for (int i = 1; i < this.uis.size(); i++) {
/* 199 */       ((TextUI)this.uis.elementAt(i)).getEditorKit(paramJTextComponent);
/*     */     }
/* 201 */     return localEditorKit;
/*     */   }
/*     */ 
/*     */   public View getRootView(JTextComponent paramJTextComponent)
/*     */   {
/* 211 */     View localView = ((TextUI)this.uis.elementAt(0)).getRootView(paramJTextComponent);
/*     */ 
/* 213 */     for (int i = 1; i < this.uis.size(); i++) {
/* 214 */       ((TextUI)this.uis.elementAt(i)).getRootView(paramJTextComponent);
/*     */     }
/* 216 */     return localView;
/*     */   }
/*     */ 
/*     */   public boolean contains(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 230 */     boolean bool = ((ComponentUI)this.uis.elementAt(0)).contains(paramJComponent, paramInt1, paramInt2);
/*     */ 
/* 232 */     for (int i = 1; i < this.uis.size(); i++) {
/* 233 */       ((ComponentUI)this.uis.elementAt(i)).contains(paramJComponent, paramInt1, paramInt2);
/*     */     }
/* 235 */     return bool;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 242 */     for (int i = 0; i < this.uis.size(); i++)
/* 243 */       ((ComponentUI)this.uis.elementAt(i)).update(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 253 */     MultiTextUI localMultiTextUI = new MultiTextUI();
/* 254 */     return MultiLookAndFeel.createUIs(localMultiTextUI, ((MultiTextUI)localMultiTextUI).uis, paramJComponent);
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/* 263 */     for (int i = 0; i < this.uis.size(); i++)
/* 264 */       ((ComponentUI)this.uis.elementAt(i)).installUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/* 272 */     for (int i = 0; i < this.uis.size(); i++)
/* 273 */       ((ComponentUI)this.uis.elementAt(i)).uninstallUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 281 */     for (int i = 0; i < this.uis.size(); i++)
/* 282 */       ((ComponentUI)this.uis.elementAt(i)).paint(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 293 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getPreferredSize(paramJComponent);
/*     */ 
/* 295 */     for (int i = 1; i < this.uis.size(); i++) {
/* 296 */       ((ComponentUI)this.uis.elementAt(i)).getPreferredSize(paramJComponent);
/*     */     }
/* 298 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 308 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getMinimumSize(paramJComponent);
/*     */ 
/* 310 */     for (int i = 1; i < this.uis.size(); i++) {
/* 311 */       ((ComponentUI)this.uis.elementAt(i)).getMinimumSize(paramJComponent);
/*     */     }
/* 313 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent)
/*     */   {
/* 323 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getMaximumSize(paramJComponent);
/*     */ 
/* 325 */     for (int i = 1; i < this.uis.size(); i++) {
/* 326 */       ((ComponentUI)this.uis.elementAt(i)).getMaximumSize(paramJComponent);
/*     */     }
/* 328 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public int getAccessibleChildrenCount(JComponent paramJComponent)
/*     */   {
/* 338 */     int i = ((ComponentUI)this.uis.elementAt(0)).getAccessibleChildrenCount(paramJComponent);
/*     */ 
/* 340 */     for (int j = 1; j < this.uis.size(); j++) {
/* 341 */       ((ComponentUI)this.uis.elementAt(j)).getAccessibleChildrenCount(paramJComponent);
/*     */     }
/* 343 */     return i;
/*     */   }
/*     */ 
/*     */   public Accessible getAccessibleChild(JComponent paramJComponent, int paramInt)
/*     */   {
/* 353 */     Accessible localAccessible = ((ComponentUI)this.uis.elementAt(0)).getAccessibleChild(paramJComponent, paramInt);
/*     */ 
/* 355 */     for (int i = 1; i < this.uis.size(); i++) {
/* 356 */       ((ComponentUI)this.uis.elementAt(i)).getAccessibleChild(paramJComponent, paramInt);
/*     */     }
/* 358 */     return localAccessible;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.multi.MultiTextUI
 * JD-Core Version:    0.6.2
 */