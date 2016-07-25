/*     */ package javax.swing.plaf.metal;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicTreeUI;
/*     */ import javax.swing.tree.TreePath;
/*     */ 
/*     */ public class MetalTreeUI extends BasicTreeUI
/*     */ {
/*     */   private static Color lineColor;
/*     */   private static final String LINE_STYLE = "JTree.lineStyle";
/*     */   private static final String LEG_LINE_STYLE_STRING = "Angled";
/*     */   private static final String HORIZ_STYLE_STRING = "Horizontal";
/*     */   private static final String NO_STYLE_STRING = "None";
/*     */   private static final int LEG_LINE_STYLE = 2;
/*     */   private static final int HORIZ_LINE_STYLE = 1;
/*     */   private static final int NO_LINE_STYLE = 0;
/*  93 */   private int lineStyle = 2;
/*  94 */   private PropertyChangeListener lineStyleListener = new LineListener();
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  98 */     return new MetalTreeUI();
/*     */   }
/*     */ 
/*     */   protected int getHorizontalLegBuffer()
/*     */   {
/* 108 */     return 3;
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent) {
/* 112 */     super.installUI(paramJComponent);
/* 113 */     lineColor = UIManager.getColor("Tree.line");
/*     */ 
/* 115 */     Object localObject = paramJComponent.getClientProperty("JTree.lineStyle");
/* 116 */     decodeLineStyle(localObject);
/* 117 */     paramJComponent.addPropertyChangeListener(this.lineStyleListener);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/* 122 */     paramJComponent.removePropertyChangeListener(this.lineStyleListener);
/* 123 */     super.uninstallUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void decodeLineStyle(Object paramObject)
/*     */   {
/* 131 */     if ((paramObject == null) || (paramObject.equals("Angled")))
/*     */     {
/* 133 */       this.lineStyle = 2;
/*     */     }
/* 135 */     else if (paramObject.equals("None"))
/* 136 */       this.lineStyle = 0;
/* 137 */     else if (paramObject.equals("Horizontal"))
/* 138 */       this.lineStyle = 1;
/*     */   }
/*     */ 
/*     */   protected boolean isLocationInExpandControl(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 146 */     if ((this.tree != null) && (!isLeaf(paramInt1)))
/*     */     {
/*     */       int i;
/* 149 */       if (getExpandedIcon() != null)
/* 150 */         i = getExpandedIcon().getIconWidth() + 6;
/*     */       else {
/* 152 */         i = 8;
/*     */       }
/* 154 */       Insets localInsets = this.tree.getInsets();
/* 155 */       int j = localInsets != null ? localInsets.left : 0;
/*     */ 
/* 158 */       j += (paramInt2 + this.depthOffset - 1) * this.totalChildIndent + getLeftChildIndent() - i / 2;
/*     */ 
/* 161 */       int k = j + i;
/*     */ 
/* 163 */       return (paramInt3 >= j) && (paramInt3 <= k);
/*     */     }
/* 165 */     return false;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent) {
/* 169 */     super.paint(paramGraphics, paramJComponent);
/*     */ 
/* 173 */     if ((this.lineStyle == 1) && (!this.largeModel))
/* 174 */       paintHorizontalSeparators(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   protected void paintHorizontalSeparators(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 179 */     paramGraphics.setColor(lineColor);
/*     */ 
/* 181 */     Rectangle localRectangle1 = paramGraphics.getClipBounds();
/*     */ 
/* 183 */     int i = getRowForPath(this.tree, getClosestPathForLocation(this.tree, 0, localRectangle1.y));
/*     */ 
/* 185 */     int j = getRowForPath(this.tree, getClosestPathForLocation(this.tree, 0, localRectangle1.y + localRectangle1.height - 1));
/*     */ 
/* 188 */     if ((i <= -1) || (j <= -1)) {
/* 189 */       return;
/*     */     }
/*     */ 
/* 192 */     for (int k = i; k <= j; k++) {
/* 193 */       TreePath localTreePath = getPathForRow(this.tree, k);
/*     */ 
/* 195 */       if ((localTreePath != null) && (localTreePath.getPathCount() == 2)) {
/* 196 */         Rectangle localRectangle2 = getPathBounds(this.tree, getPathForRow(this.tree, k));
/*     */ 
/* 200 */         if (localRectangle2 != null)
/* 201 */           paramGraphics.drawLine(localRectangle1.x, localRectangle2.y, localRectangle1.x + localRectangle1.width, localRectangle2.y);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintVerticalPartOfLeg(Graphics paramGraphics, Rectangle paramRectangle, Insets paramInsets, TreePath paramTreePath)
/*     */   {
/* 210 */     if (this.lineStyle == 2)
/* 211 */       super.paintVerticalPartOfLeg(paramGraphics, paramRectangle, paramInsets, paramTreePath);
/*     */   }
/*     */ 
/*     */   protected void paintHorizontalPartOfLeg(Graphics paramGraphics, Rectangle paramRectangle1, Insets paramInsets, Rectangle paramRectangle2, TreePath paramTreePath, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*     */   {
/* 221 */     if (this.lineStyle == 2)
/* 222 */       super.paintHorizontalPartOfLeg(paramGraphics, paramRectangle1, paramInsets, paramRectangle2, paramTreePath, paramInt, paramBoolean1, paramBoolean2, paramBoolean3);
/*     */   }
/*     */ 
/*     */   class LineListener implements PropertyChangeListener
/*     */   {
/*     */     LineListener() {
/*     */     }
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 231 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 232 */       if (str.equals("JTree.lineStyle"))
/* 233 */         MetalTreeUI.this.decodeLineStyle(paramPropertyChangeEvent.getNewValue());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.metal.MetalTreeUI
 * JD-Core Version:    0.6.2
 */