/*     */ package javax.swing.plaf.multi;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.util.Vector;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.TreeUI;
/*     */ import javax.swing.tree.TreePath;
/*     */ 
/*     */ public class MultiTreeUI extends TreeUI
/*     */ {
/*  53 */   protected Vector uis = new Vector();
/*     */ 
/*     */   public ComponentUI[] getUIs()
/*     */   {
/*  65 */     return MultiLookAndFeel.uisToArray(this.uis);
/*     */   }
/*     */ 
/*     */   public Rectangle getPathBounds(JTree paramJTree, TreePath paramTreePath)
/*     */   {
/*  79 */     Rectangle localRectangle = ((TreeUI)this.uis.elementAt(0)).getPathBounds(paramJTree, paramTreePath);
/*     */ 
/*  81 */     for (int i = 1; i < this.uis.size(); i++) {
/*  82 */       ((TreeUI)this.uis.elementAt(i)).getPathBounds(paramJTree, paramTreePath);
/*     */     }
/*  84 */     return localRectangle;
/*     */   }
/*     */ 
/*     */   public TreePath getPathForRow(JTree paramJTree, int paramInt)
/*     */   {
/*  94 */     TreePath localTreePath = ((TreeUI)this.uis.elementAt(0)).getPathForRow(paramJTree, paramInt);
/*     */ 
/*  96 */     for (int i = 1; i < this.uis.size(); i++) {
/*  97 */       ((TreeUI)this.uis.elementAt(i)).getPathForRow(paramJTree, paramInt);
/*     */     }
/*  99 */     return localTreePath;
/*     */   }
/*     */ 
/*     */   public int getRowForPath(JTree paramJTree, TreePath paramTreePath)
/*     */   {
/* 109 */     int i = ((TreeUI)this.uis.elementAt(0)).getRowForPath(paramJTree, paramTreePath);
/*     */ 
/* 111 */     for (int j = 1; j < this.uis.size(); j++) {
/* 112 */       ((TreeUI)this.uis.elementAt(j)).getRowForPath(paramJTree, paramTreePath);
/*     */     }
/* 114 */     return i;
/*     */   }
/*     */ 
/*     */   public int getRowCount(JTree paramJTree)
/*     */   {
/* 124 */     int i = ((TreeUI)this.uis.elementAt(0)).getRowCount(paramJTree);
/*     */ 
/* 126 */     for (int j = 1; j < this.uis.size(); j++) {
/* 127 */       ((TreeUI)this.uis.elementAt(j)).getRowCount(paramJTree);
/*     */     }
/* 129 */     return i;
/*     */   }
/*     */ 
/*     */   public TreePath getClosestPathForLocation(JTree paramJTree, int paramInt1, int paramInt2)
/*     */   {
/* 139 */     TreePath localTreePath = ((TreeUI)this.uis.elementAt(0)).getClosestPathForLocation(paramJTree, paramInt1, paramInt2);
/*     */ 
/* 141 */     for (int i = 1; i < this.uis.size(); i++) {
/* 142 */       ((TreeUI)this.uis.elementAt(i)).getClosestPathForLocation(paramJTree, paramInt1, paramInt2);
/*     */     }
/* 144 */     return localTreePath;
/*     */   }
/*     */ 
/*     */   public boolean isEditing(JTree paramJTree)
/*     */   {
/* 154 */     boolean bool = ((TreeUI)this.uis.elementAt(0)).isEditing(paramJTree);
/*     */ 
/* 156 */     for (int i = 1; i < this.uis.size(); i++) {
/* 157 */       ((TreeUI)this.uis.elementAt(i)).isEditing(paramJTree);
/*     */     }
/* 159 */     return bool;
/*     */   }
/*     */ 
/*     */   public boolean stopEditing(JTree paramJTree)
/*     */   {
/* 169 */     boolean bool = ((TreeUI)this.uis.elementAt(0)).stopEditing(paramJTree);
/*     */ 
/* 171 */     for (int i = 1; i < this.uis.size(); i++) {
/* 172 */       ((TreeUI)this.uis.elementAt(i)).stopEditing(paramJTree);
/*     */     }
/* 174 */     return bool;
/*     */   }
/*     */ 
/*     */   public void cancelEditing(JTree paramJTree)
/*     */   {
/* 181 */     for (int i = 0; i < this.uis.size(); i++)
/* 182 */       ((TreeUI)this.uis.elementAt(i)).cancelEditing(paramJTree);
/*     */   }
/*     */ 
/*     */   public void startEditingAtPath(JTree paramJTree, TreePath paramTreePath)
/*     */   {
/* 190 */     for (int i = 0; i < this.uis.size(); i++)
/* 191 */       ((TreeUI)this.uis.elementAt(i)).startEditingAtPath(paramJTree, paramTreePath);
/*     */   }
/*     */ 
/*     */   public TreePath getEditingPath(JTree paramJTree)
/*     */   {
/* 202 */     TreePath localTreePath = ((TreeUI)this.uis.elementAt(0)).getEditingPath(paramJTree);
/*     */ 
/* 204 */     for (int i = 1; i < this.uis.size(); i++) {
/* 205 */       ((TreeUI)this.uis.elementAt(i)).getEditingPath(paramJTree);
/*     */     }
/* 207 */     return localTreePath;
/*     */   }
/*     */ 
/*     */   public boolean contains(JComponent paramJComponent, int paramInt1, int paramInt2)
/*     */   {
/* 221 */     boolean bool = ((ComponentUI)this.uis.elementAt(0)).contains(paramJComponent, paramInt1, paramInt2);
/*     */ 
/* 223 */     for (int i = 1; i < this.uis.size(); i++) {
/* 224 */       ((ComponentUI)this.uis.elementAt(i)).contains(paramJComponent, paramInt1, paramInt2);
/*     */     }
/* 226 */     return bool;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 233 */     for (int i = 0; i < this.uis.size(); i++)
/* 234 */       ((ComponentUI)this.uis.elementAt(i)).update(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/* 244 */     MultiTreeUI localMultiTreeUI = new MultiTreeUI();
/* 245 */     return MultiLookAndFeel.createUIs(localMultiTreeUI, ((MultiTreeUI)localMultiTreeUI).uis, paramJComponent);
/*     */   }
/*     */ 
/*     */   public void installUI(JComponent paramJComponent)
/*     */   {
/* 254 */     for (int i = 0; i < this.uis.size(); i++)
/* 255 */       ((ComponentUI)this.uis.elementAt(i)).installUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/* 263 */     for (int i = 0; i < this.uis.size(); i++)
/* 264 */       ((ComponentUI)this.uis.elementAt(i)).uninstallUI(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 272 */     for (int i = 0; i < this.uis.size(); i++)
/* 273 */       ((ComponentUI)this.uis.elementAt(i)).paint(paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public Dimension getPreferredSize(JComponent paramJComponent)
/*     */   {
/* 284 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getPreferredSize(paramJComponent);
/*     */ 
/* 286 */     for (int i = 1; i < this.uis.size(); i++) {
/* 287 */       ((ComponentUI)this.uis.elementAt(i)).getPreferredSize(paramJComponent);
/*     */     }
/* 289 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMinimumSize(JComponent paramJComponent)
/*     */   {
/* 299 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getMinimumSize(paramJComponent);
/*     */ 
/* 301 */     for (int i = 1; i < this.uis.size(); i++) {
/* 302 */       ((ComponentUI)this.uis.elementAt(i)).getMinimumSize(paramJComponent);
/*     */     }
/* 304 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public Dimension getMaximumSize(JComponent paramJComponent)
/*     */   {
/* 314 */     Dimension localDimension = ((ComponentUI)this.uis.elementAt(0)).getMaximumSize(paramJComponent);
/*     */ 
/* 316 */     for (int i = 1; i < this.uis.size(); i++) {
/* 317 */       ((ComponentUI)this.uis.elementAt(i)).getMaximumSize(paramJComponent);
/*     */     }
/* 319 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public int getAccessibleChildrenCount(JComponent paramJComponent)
/*     */   {
/* 329 */     int i = ((ComponentUI)this.uis.elementAt(0)).getAccessibleChildrenCount(paramJComponent);
/*     */ 
/* 331 */     for (int j = 1; j < this.uis.size(); j++) {
/* 332 */       ((ComponentUI)this.uis.elementAt(j)).getAccessibleChildrenCount(paramJComponent);
/*     */     }
/* 334 */     return i;
/*     */   }
/*     */ 
/*     */   public Accessible getAccessibleChild(JComponent paramJComponent, int paramInt)
/*     */   {
/* 344 */     Accessible localAccessible = ((ComponentUI)this.uis.elementAt(0)).getAccessibleChild(paramJComponent, paramInt);
/*     */ 
/* 346 */     for (int i = 1; i < this.uis.size(); i++) {
/* 347 */       ((ComponentUI)this.uis.elementAt(i)).getAccessibleChild(paramJComponent, paramInt);
/*     */     }
/* 349 */     return localAccessible;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.multi.MultiTreeUI
 * JD-Core Version:    0.6.2
 */