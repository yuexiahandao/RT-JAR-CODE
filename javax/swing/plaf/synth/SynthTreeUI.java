/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import javax.swing.CellRendererPane;
/*     */ import javax.swing.DefaultCellEditor;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.JTree.DropLocation;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicTreeUI;
/*     */ import javax.swing.tree.AbstractLayoutCache;
/*     */ import javax.swing.tree.DefaultTreeCellEditor;
/*     */ import javax.swing.tree.DefaultTreeCellRenderer;
/*     */ import javax.swing.tree.TreeCellEditor;
/*     */ import javax.swing.tree.TreeCellRenderer;
/*     */ import javax.swing.tree.TreeModel;
/*     */ import javax.swing.tree.TreePath;
/*     */ import javax.swing.tree.TreeSelectionModel;
/*     */ import sun.swing.plaf.synth.SynthIcon;
/*     */ 
/*     */ public class SynthTreeUI extends BasicTreeUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*     */   private SynthStyle cellStyle;
/*     */   private SynthContext paintContext;
/*     */   private boolean drawHorizontalLines;
/*     */   private boolean drawVerticalLines;
/*     */   private Object linesStyle;
/*     */   private int padding;
/*     */   private boolean useTreeColors;
/*     */   private Icon expandedIconWrapper;
/*     */ 
/*     */   public SynthTreeUI()
/*     */   {
/*  75 */     this.expandedIconWrapper = new ExpandedIconWrapper(null);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  84 */     return new SynthTreeUI();
/*     */   }
/*     */ 
/*     */   public Icon getExpandedIcon()
/*     */   {
/*  92 */     return this.expandedIconWrapper;
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/* 100 */     updateStyle(this.tree);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JTree paramJTree) {
/* 104 */     SynthContext localSynthContext = getContext(paramJTree, 1);
/* 105 */     SynthStyle localSynthStyle = this.style;
/*     */ 
/* 107 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/* 108 */     if (this.style != localSynthStyle)
/*     */     {
/* 111 */       setExpandedIcon(this.style.getIcon(localSynthContext, "Tree.expandedIcon"));
/* 112 */       setCollapsedIcon(this.style.getIcon(localSynthContext, "Tree.collapsedIcon"));
/*     */ 
/* 114 */       setLeftChildIndent(this.style.getInt(localSynthContext, "Tree.leftChildIndent", 0));
/*     */ 
/* 116 */       setRightChildIndent(this.style.getInt(localSynthContext, "Tree.rightChildIndent", 0));
/*     */ 
/* 119 */       this.drawHorizontalLines = this.style.getBoolean(localSynthContext, "Tree.drawHorizontalLines", true);
/*     */ 
/* 121 */       this.drawVerticalLines = this.style.getBoolean(localSynthContext, "Tree.drawVerticalLines", true);
/*     */ 
/* 123 */       this.linesStyle = this.style.get(localSynthContext, "Tree.linesStyle");
/*     */ 
/* 125 */       Object localObject = this.style.get(localSynthContext, "Tree.rowHeight");
/* 126 */       if (localObject != null) {
/* 127 */         LookAndFeel.installProperty(paramJTree, "rowHeight", localObject);
/*     */       }
/*     */ 
/* 130 */       localObject = this.style.get(localSynthContext, "Tree.scrollsOnExpand");
/* 131 */       LookAndFeel.installProperty(paramJTree, "scrollsOnExpand", localObject != null ? localObject : Boolean.TRUE);
/*     */ 
/* 134 */       this.padding = this.style.getInt(localSynthContext, "Tree.padding", 0);
/*     */ 
/* 136 */       this.largeModel = ((paramJTree.isLargeModel()) && (paramJTree.getRowHeight() > 0));
/*     */ 
/* 138 */       this.useTreeColors = this.style.getBoolean(localSynthContext, "Tree.rendererUseTreeColors", true);
/*     */ 
/* 141 */       Boolean localBoolean = Boolean.valueOf(this.style.getBoolean(localSynthContext, "Tree.showsRootHandles", Boolean.TRUE.booleanValue()));
/*     */ 
/* 143 */       LookAndFeel.installProperty(paramJTree, "showsRootHandles", localBoolean);
/*     */ 
/* 146 */       if (localSynthStyle != null) {
/* 147 */         uninstallKeyboardActions();
/* 148 */         installKeyboardActions();
/*     */       }
/*     */     }
/* 151 */     localSynthContext.dispose();
/*     */ 
/* 153 */     localSynthContext = getContext(paramJTree, Region.TREE_CELL, 1);
/* 154 */     this.cellStyle = SynthLookAndFeel.updateStyle(localSynthContext, this);
/* 155 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/* 163 */     super.installListeners();
/* 164 */     this.tree.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 172 */     return getContext(paramJComponent, SynthLookAndFeel.getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 176 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, Region paramRegion)
/*     */   {
/* 181 */     return getContext(paramJComponent, paramRegion, getComponentState(paramJComponent, paramRegion));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, Region paramRegion, int paramInt) {
/* 185 */     return SynthContext.getContext(SynthContext.class, paramJComponent, paramRegion, this.cellStyle, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent, Region paramRegion)
/*     */   {
/* 192 */     return 513;
/*     */   }
/*     */ 
/*     */   protected TreeCellEditor createDefaultCellEditor()
/*     */   {
/* 200 */     TreeCellRenderer localTreeCellRenderer = this.tree.getCellRenderer();
/*     */     SynthTreeCellEditor localSynthTreeCellEditor;
/* 203 */     if ((localTreeCellRenderer != null) && ((localTreeCellRenderer instanceof DefaultTreeCellRenderer))) {
/* 204 */       localSynthTreeCellEditor = new SynthTreeCellEditor(this.tree, (DefaultTreeCellRenderer)localTreeCellRenderer);
/*     */     }
/*     */     else
/*     */     {
/* 208 */       localSynthTreeCellEditor = new SynthTreeCellEditor(this.tree, null);
/*     */     }
/* 210 */     return localSynthTreeCellEditor;
/*     */   }
/*     */ 
/*     */   protected TreeCellRenderer createDefaultCellRenderer()
/*     */   {
/* 218 */     return new SynthTreeCellRenderer();
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 226 */     SynthContext localSynthContext = getContext(this.tree, 1);
/*     */ 
/* 228 */     this.style.uninstallDefaults(localSynthContext);
/* 229 */     localSynthContext.dispose();
/* 230 */     this.style = null;
/*     */ 
/* 232 */     localSynthContext = getContext(this.tree, Region.TREE_CELL, 1);
/* 233 */     this.cellStyle.uninstallDefaults(localSynthContext);
/* 234 */     localSynthContext.dispose();
/* 235 */     this.cellStyle = null;
/*     */ 
/* 238 */     if ((this.tree.getTransferHandler() instanceof UIResource))
/* 239 */       this.tree.setTransferHandler(null);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 248 */     super.uninstallListeners();
/* 249 */     this.tree.removePropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 266 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 268 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 269 */     localSynthContext.getPainter().paintTreeBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 271 */     paint(localSynthContext, paramGraphics);
/* 272 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 281 */     paramSynthContext.getPainter().paintTreeBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 295 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 297 */     paint(localSynthContext, paramGraphics);
/* 298 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/* 309 */     this.paintContext = paramSynthContext;
/*     */ 
/* 311 */     updateLeadSelectionRow();
/*     */ 
/* 313 */     Rectangle localRectangle1 = paramGraphics.getClipBounds();
/* 314 */     Insets localInsets = this.tree.getInsets();
/* 315 */     TreePath localTreePath1 = getClosestPathForLocation(this.tree, 0, localRectangle1.y);
/*     */ 
/* 317 */     Enumeration localEnumeration = this.treeState.getVisiblePathsFrom(localTreePath1);
/*     */ 
/* 319 */     int i = this.treeState.getRowForPath(localTreePath1);
/* 320 */     int j = localRectangle1.y + localRectangle1.height;
/* 321 */     TreeModel localTreeModel = this.tree.getModel();
/* 322 */     SynthContext localSynthContext = getContext(this.tree, Region.TREE_CELL);
/*     */ 
/* 324 */     this.drawingCache.clear();
/*     */ 
/* 326 */     setHashColor(paramSynthContext.getStyle().getColor(paramSynthContext, ColorType.FOREGROUND));
/*     */ 
/* 329 */     if (localEnumeration != null)
/*     */     {
/* 332 */       int k = 0;
/*     */ 
/* 336 */       Rectangle localRectangle2 = new Rectangle(0, 0, this.tree.getWidth(), 0);
/*     */ 
/* 339 */       TreeCellRenderer localTreeCellRenderer = this.tree.getCellRenderer();
/* 340 */       DefaultTreeCellRenderer localDefaultTreeCellRenderer = (localTreeCellRenderer instanceof DefaultTreeCellRenderer) ? (DefaultTreeCellRenderer)localTreeCellRenderer : null;
/*     */ 
/* 344 */       configureRenderer(localSynthContext);
/*     */       TreePath localTreePath2;
/*     */       boolean bool3;
/*     */       boolean bool2;
/*     */       boolean bool1;
/*     */       Rectangle localRectangle3;
/* 345 */       while ((k == 0) && (localEnumeration.hasMoreElements())) {
/* 346 */         localTreePath2 = (TreePath)localEnumeration.nextElement();
/* 347 */         if (localTreePath2 != null) {
/* 348 */           bool3 = localTreeModel.isLeaf(localTreePath2.getLastPathComponent());
/* 349 */           if (bool3) {
/* 350 */             bool1 = bool2 = 0;
/*     */           }
/*     */           else {
/* 353 */             bool1 = this.treeState.getExpandedState(localTreePath2);
/* 354 */             bool2 = this.tree.hasBeenExpanded(localTreePath2);
/*     */           }
/* 356 */           localRectangle3 = getPathBounds(this.tree, localTreePath2);
/* 357 */           localRectangle2.y = localRectangle3.y;
/* 358 */           localRectangle2.height = localRectangle3.height;
/* 359 */           paintRow(localTreeCellRenderer, localDefaultTreeCellRenderer, paramSynthContext, localSynthContext, paramGraphics, localRectangle1, localInsets, localRectangle3, localRectangle2, localTreePath2, i, bool1, bool2, bool3);
/*     */ 
/* 362 */           if (localRectangle3.y + localRectangle3.height >= j)
/* 363 */             k = 1;
/*     */         }
/*     */         else
/*     */         {
/* 367 */           k = 1;
/*     */         }
/* 369 */         i++;
/*     */       }
/*     */ 
/* 374 */       boolean bool4 = this.tree.isRootVisible();
/* 375 */       TreePath localTreePath3 = localTreePath1;
/* 376 */       localTreePath3 = localTreePath3.getParentPath();
/* 377 */       while (localTreePath3 != null) {
/* 378 */         paintVerticalPartOfLeg(paramGraphics, localRectangle1, localInsets, localTreePath3);
/* 379 */         this.drawingCache.put(localTreePath3, Boolean.TRUE);
/* 380 */         localTreePath3 = localTreePath3.getParentPath();
/*     */       }
/* 382 */       k = 0;
/* 383 */       localEnumeration = this.treeState.getVisiblePathsFrom(localTreePath1);
/* 384 */       while ((k == 0) && (localEnumeration.hasMoreElements())) {
/* 385 */         localTreePath2 = (TreePath)localEnumeration.nextElement();
/* 386 */         if (localTreePath2 != null) {
/* 387 */           bool3 = localTreeModel.isLeaf(localTreePath2.getLastPathComponent());
/* 388 */           if (bool3) {
/* 389 */             bool1 = bool2 = 0;
/*     */           }
/*     */           else {
/* 392 */             bool1 = this.treeState.getExpandedState(localTreePath2);
/* 393 */             bool2 = this.tree.hasBeenExpanded(localTreePath2);
/*     */           }
/* 395 */           localRectangle3 = getPathBounds(this.tree, localTreePath2);
/*     */ 
/* 397 */           localTreePath3 = localTreePath2.getParentPath();
/* 398 */           if (localTreePath3 != null) {
/* 399 */             if (this.drawingCache.get(localTreePath3) == null) {
/* 400 */               paintVerticalPartOfLeg(paramGraphics, localRectangle1, localInsets, localTreePath3);
/*     */ 
/* 402 */               this.drawingCache.put(localTreePath3, Boolean.TRUE);
/*     */             }
/* 404 */             paintHorizontalPartOfLeg(paramGraphics, localRectangle1, localInsets, localRectangle3, localTreePath2, i, bool1, bool2, bool3);
/*     */           }
/* 409 */           else if ((bool4) && (i == 0)) {
/* 410 */             paintHorizontalPartOfLeg(paramGraphics, localRectangle1, localInsets, localRectangle3, localTreePath2, i, bool1, bool2, bool3);
/*     */           }
/*     */ 
/* 415 */           if (shouldPaintExpandControl(localTreePath2, i, bool1, bool2, bool3))
/*     */           {
/* 417 */             paintExpandControl(paramGraphics, localRectangle1, localInsets, localRectangle3, localTreePath2, i, bool1, bool2, bool3);
/*     */           }
/*     */ 
/* 421 */           if (localRectangle3.y + localRectangle3.height >= j)
/* 422 */             k = 1;
/*     */         }
/*     */         else
/*     */         {
/* 426 */           k = 1;
/*     */         }
/* 428 */         i++;
/*     */       }
/*     */     }
/* 431 */     localSynthContext.dispose();
/*     */ 
/* 433 */     paintDropLine(paramGraphics);
/*     */ 
/* 436 */     this.rendererPane.removeAll();
/*     */ 
/* 438 */     this.paintContext = null;
/*     */   }
/*     */ 
/*     */   private void configureRenderer(SynthContext paramSynthContext) {
/* 442 */     TreeCellRenderer localTreeCellRenderer = this.tree.getCellRenderer();
/*     */ 
/* 444 */     if ((localTreeCellRenderer instanceof DefaultTreeCellRenderer)) {
/* 445 */       DefaultTreeCellRenderer localDefaultTreeCellRenderer = (DefaultTreeCellRenderer)localTreeCellRenderer;
/* 446 */       SynthStyle localSynthStyle = paramSynthContext.getStyle();
/*     */ 
/* 448 */       paramSynthContext.setComponentState(513);
/* 449 */       Color localColor = localDefaultTreeCellRenderer.getTextSelectionColor();
/* 450 */       if ((localColor == null) || ((localColor instanceof UIResource))) {
/* 451 */         localDefaultTreeCellRenderer.setTextSelectionColor(localSynthStyle.getColor(paramSynthContext, ColorType.TEXT_FOREGROUND));
/*     */       }
/*     */ 
/* 454 */       localColor = localDefaultTreeCellRenderer.getBackgroundSelectionColor();
/* 455 */       if ((localColor == null) || ((localColor instanceof UIResource))) {
/* 456 */         localDefaultTreeCellRenderer.setBackgroundSelectionColor(localSynthStyle.getColor(paramSynthContext, ColorType.TEXT_BACKGROUND));
/*     */       }
/*     */ 
/* 460 */       paramSynthContext.setComponentState(1);
/* 461 */       localColor = localDefaultTreeCellRenderer.getTextNonSelectionColor();
/* 462 */       if ((localColor == null) || ((localColor instanceof UIResource))) {
/* 463 */         localDefaultTreeCellRenderer.setTextNonSelectionColor(localSynthStyle.getColorForState(paramSynthContext, ColorType.TEXT_FOREGROUND));
/*     */       }
/*     */ 
/* 466 */       localColor = localDefaultTreeCellRenderer.getBackgroundNonSelectionColor();
/* 467 */       if ((localColor == null) || ((localColor instanceof UIResource)))
/* 468 */         localDefaultTreeCellRenderer.setBackgroundNonSelectionColor(localSynthStyle.getColorForState(paramSynthContext, ColorType.TEXT_BACKGROUND));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintHorizontalPartOfLeg(Graphics paramGraphics, Rectangle paramRectangle1, Insets paramInsets, Rectangle paramRectangle2, TreePath paramTreePath, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*     */   {
/* 484 */     if (this.drawHorizontalLines)
/* 485 */       super.paintHorizontalPartOfLeg(paramGraphics, paramRectangle1, paramInsets, paramRectangle2, paramTreePath, paramInt, paramBoolean1, paramBoolean2, paramBoolean3);
/*     */   }
/*     */ 
/*     */   protected void paintHorizontalLine(Graphics paramGraphics, JComponent paramJComponent, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 497 */     this.paintContext.getStyle().getGraphicsUtils(this.paintContext).drawLine(this.paintContext, "Tree.horizontalLine", paramGraphics, paramInt2, paramInt1, paramInt3, paramInt1, this.linesStyle);
/*     */   }
/*     */ 
/*     */   protected void paintVerticalPartOfLeg(Graphics paramGraphics, Rectangle paramRectangle, Insets paramInsets, TreePath paramTreePath)
/*     */   {
/* 508 */     if (this.drawVerticalLines)
/* 509 */       super.paintVerticalPartOfLeg(paramGraphics, paramRectangle, paramInsets, paramTreePath);
/*     */   }
/*     */ 
/*     */   protected void paintVerticalLine(Graphics paramGraphics, JComponent paramJComponent, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 519 */     this.paintContext.getStyle().getGraphicsUtils(this.paintContext).drawLine(this.paintContext, "Tree.verticalLine", paramGraphics, paramInt1, paramInt2, paramInt1, paramInt3, this.linesStyle);
/*     */   }
/*     */ 
/*     */   private void paintRow(TreeCellRenderer paramTreeCellRenderer, DefaultTreeCellRenderer paramDefaultTreeCellRenderer, SynthContext paramSynthContext1, SynthContext paramSynthContext2, Graphics paramGraphics, Rectangle paramRectangle1, Insets paramInsets, Rectangle paramRectangle2, Rectangle paramRectangle3, TreePath paramTreePath, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*     */   {
/* 530 */     boolean bool = this.tree.isRowSelected(paramInt);
/*     */ 
/* 532 */     JTree.DropLocation localDropLocation = this.tree.getDropLocation();
/* 533 */     int i = (localDropLocation != null) && (localDropLocation.getChildIndex() == -1) && (paramTreePath == localDropLocation.getPath()) ? 1 : 0;
/*     */ 
/* 537 */     int j = 1;
/* 538 */     if ((bool) || (i != 0)) {
/* 539 */       j |= 512;
/*     */     }
/*     */ 
/* 542 */     if ((this.tree.isFocusOwner()) && (paramInt == getLeadSelectionRow())) {
/* 543 */       j |= 256;
/*     */     }
/*     */ 
/* 546 */     paramSynthContext2.setComponentState(j);
/*     */ 
/* 548 */     if ((paramDefaultTreeCellRenderer != null) && ((paramDefaultTreeCellRenderer.getBorderSelectionColor() instanceof UIResource)))
/*     */     {
/* 550 */       paramDefaultTreeCellRenderer.setBorderSelectionColor(this.style.getColor(paramSynthContext2, ColorType.FOCUS));
/*     */     }
/*     */ 
/* 553 */     SynthLookAndFeel.updateSubregion(paramSynthContext2, paramGraphics, paramRectangle3);
/* 554 */     paramSynthContext2.getPainter().paintTreeCellBackground(paramSynthContext2, paramGraphics, paramRectangle3.x, paramRectangle3.y, paramRectangle3.width, paramRectangle3.height);
/*     */ 
/* 557 */     paramSynthContext2.getPainter().paintTreeCellBorder(paramSynthContext2, paramGraphics, paramRectangle3.x, paramRectangle3.y, paramRectangle3.width, paramRectangle3.height);
/*     */ 
/* 560 */     if ((this.editingComponent != null) && (this.editingRow == paramInt))
/*     */       return;
/*     */     int k;
/* 566 */     if (this.tree.hasFocus()) {
/* 567 */       k = getLeadSelectionRow();
/*     */     }
/*     */     else {
/* 570 */       k = -1;
/*     */     }
/*     */ 
/* 573 */     Component localComponent = paramTreeCellRenderer.getTreeCellRendererComponent(this.tree, paramTreePath.getLastPathComponent(), bool, paramBoolean1, paramBoolean3, paramInt, k == paramInt);
/*     */ 
/* 578 */     this.rendererPane.paintComponent(paramGraphics, localComponent, this.tree, paramRectangle2.x, paramRectangle2.y, paramRectangle2.width, paramRectangle2.height, true);
/*     */   }
/*     */ 
/*     */   private int findCenteredX(int paramInt1, int paramInt2)
/*     */   {
/* 583 */     return this.tree.getComponentOrientation().isLeftToRight() ? paramInt1 - (int)Math.ceil(paramInt2 / 2.0D) : paramInt1 - (int)Math.floor(paramInt2 / 2.0D);
/*     */   }
/*     */ 
/*     */   protected void paintExpandControl(Graphics paramGraphics, Rectangle paramRectangle1, Insets paramInsets, Rectangle paramRectangle2, TreePath paramTreePath, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*     */   {
/* 599 */     boolean bool = this.tree.getSelectionModel().isPathSelected(paramTreePath);
/* 600 */     int i = this.paintContext.getComponentState();
/* 601 */     if (bool) {
/* 602 */       this.paintContext.setComponentState(i | 0x200);
/*     */     }
/* 604 */     super.paintExpandControl(paramGraphics, paramRectangle1, paramInsets, paramRectangle2, paramTreePath, paramInt, paramBoolean1, paramBoolean2, paramBoolean3);
/*     */ 
/* 606 */     this.paintContext.setComponentState(i);
/*     */   }
/*     */ 
/*     */   protected void drawCentered(Component paramComponent, Graphics paramGraphics, Icon paramIcon, int paramInt1, int paramInt2)
/*     */   {
/* 615 */     int i = SynthIcon.getIconWidth(paramIcon, this.paintContext);
/* 616 */     int j = SynthIcon.getIconHeight(paramIcon, this.paintContext);
/*     */ 
/* 618 */     SynthIcon.paintIcon(paramIcon, this.paintContext, paramGraphics, findCenteredX(paramInt1, i), paramInt2 - j / 2, i, j);
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 628 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent)) {
/* 629 */       updateStyle((JTree)paramPropertyChangeEvent.getSource());
/*     */     }
/*     */ 
/* 632 */     if ("dropLocation" == paramPropertyChangeEvent.getPropertyName()) {
/* 633 */       JTree.DropLocation localDropLocation = (JTree.DropLocation)paramPropertyChangeEvent.getOldValue();
/* 634 */       repaintDropLocation(localDropLocation);
/* 635 */       repaintDropLocation(this.tree.getDropLocation());
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintDropLine(Graphics paramGraphics)
/*     */   {
/* 644 */     JTree.DropLocation localDropLocation = this.tree.getDropLocation();
/* 645 */     if (!isDropLine(localDropLocation)) {
/* 646 */       return;
/*     */     }
/*     */ 
/* 649 */     Color localColor = (Color)this.style.get(this.paintContext, "Tree.dropLineColor");
/* 650 */     if (localColor != null) {
/* 651 */       paramGraphics.setColor(localColor);
/* 652 */       Rectangle localRectangle = getDropLineRect(localDropLocation);
/* 653 */       paramGraphics.fillRect(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void repaintDropLocation(JTree.DropLocation paramDropLocation) {
/* 658 */     if (paramDropLocation == null)
/*     */       return;
/*     */     Rectangle localRectangle;
/* 664 */     if (isDropLine(paramDropLocation)) {
/* 665 */       localRectangle = getDropLineRect(paramDropLocation);
/*     */     } else {
/* 667 */       localRectangle = this.tree.getPathBounds(paramDropLocation.getPath());
/* 668 */       if (localRectangle != null) {
/* 669 */         localRectangle.x = 0;
/* 670 */         localRectangle.width = this.tree.getWidth();
/*     */       }
/*     */     }
/*     */ 
/* 674 */     if (localRectangle != null)
/* 675 */       this.tree.repaint(localRectangle);
/*     */   }
/*     */ 
/*     */   protected int getRowX(int paramInt1, int paramInt2)
/*     */   {
/* 684 */     return super.getRowX(paramInt1, paramInt2) + this.padding;
/*     */   }
/*     */ 
/*     */   private class ExpandedIconWrapper extends SynthIcon
/*     */   {
/*     */     private ExpandedIconWrapper()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void paintIcon(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 787 */       if (paramSynthContext == null) {
/* 788 */         paramSynthContext = SynthTreeUI.this.getContext(SynthTreeUI.this.tree);
/* 789 */         SynthIcon.paintIcon(SynthTreeUI.this.expandedIcon, paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/* 790 */         paramSynthContext.dispose();
/*     */       }
/*     */       else {
/* 793 */         SynthIcon.paintIcon(SynthTreeUI.this.expandedIcon, paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */       }
/*     */     }
/*     */ 
/*     */     public int getIconWidth(SynthContext paramSynthContext)
/*     */     {
/*     */       int i;
/* 799 */       if (paramSynthContext == null) {
/* 800 */         paramSynthContext = SynthTreeUI.this.getContext(SynthTreeUI.this.tree);
/* 801 */         i = SynthIcon.getIconWidth(SynthTreeUI.this.expandedIcon, paramSynthContext);
/* 802 */         paramSynthContext.dispose();
/*     */       }
/*     */       else {
/* 805 */         i = SynthIcon.getIconWidth(SynthTreeUI.this.expandedIcon, paramSynthContext);
/*     */       }
/* 807 */       return i;
/*     */     }
/*     */ 
/*     */     public int getIconHeight(SynthContext paramSynthContext)
/*     */     {
/*     */       int i;
/* 812 */       if (paramSynthContext == null) {
/* 813 */         paramSynthContext = SynthTreeUI.this.getContext(SynthTreeUI.this.tree);
/* 814 */         i = SynthIcon.getIconHeight(SynthTreeUI.this.expandedIcon, paramSynthContext);
/* 815 */         paramSynthContext.dispose();
/*     */       }
/*     */       else {
/* 818 */         i = SynthIcon.getIconHeight(SynthTreeUI.this.expandedIcon, paramSynthContext);
/*     */       }
/* 820 */       return i;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SynthTreeCellEditor extends DefaultTreeCellEditor
/*     */   {
/*     */     public SynthTreeCellEditor(JTree paramJTree, DefaultTreeCellRenderer paramDefaultTreeCellRenderer)
/*     */     {
/* 759 */       super(paramDefaultTreeCellRenderer);
/* 760 */       setBorderSelectionColor(null);
/*     */     }
/*     */ 
/*     */     protected TreeCellEditor createTreeCellEditor()
/*     */     {
/* 765 */       JTextField local1 = new JTextField()
/*     */       {
/*     */         public String getName() {
/* 768 */           return "Tree.cellEditor";
/*     */         }
/*     */       };
/* 771 */       DefaultCellEditor localDefaultCellEditor = new DefaultCellEditor(local1);
/*     */ 
/* 774 */       localDefaultCellEditor.setClickCountToStart(1);
/* 775 */       return localDefaultCellEditor;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class SynthTreeCellRenderer extends DefaultTreeCellRenderer
/*     */     implements UIResource
/*     */   {
/*     */     SynthTreeCellRenderer()
/*     */     {
/*     */     }
/*     */ 
/*     */     public String getName()
/*     */     {
/* 695 */       return "Tree.cellRenderer";
/*     */     }
/*     */ 
/*     */     public Component getTreeCellRendererComponent(JTree paramJTree, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt, boolean paramBoolean4)
/*     */     {
/* 704 */       if ((!SynthTreeUI.this.useTreeColors) && ((paramBoolean1) || (paramBoolean4))) {
/* 705 */         SynthLookAndFeel.setSelectedUI((SynthLabelUI)SynthLookAndFeel.getUIOfType(getUI(), SynthLabelUI.class), paramBoolean1, paramBoolean4, paramJTree.isEnabled(), false);
/*     */       }
/*     */       else
/*     */       {
/* 710 */         SynthLookAndFeel.resetSelectedUI();
/*     */       }
/* 712 */       return super.getTreeCellRendererComponent(paramJTree, paramObject, paramBoolean1, paramBoolean2, paramBoolean3, paramInt, paramBoolean4);
/*     */     }
/*     */ 
/*     */     public void paint(Graphics paramGraphics)
/*     */     {
/* 718 */       paintComponent(paramGraphics);
/* 719 */       if (this.hasFocus) {
/* 720 */         SynthContext localSynthContext = SynthTreeUI.this.getContext(SynthTreeUI.this.tree, Region.TREE_CELL);
/*     */ 
/* 722 */         if (localSynthContext.getStyle() == null) {
/* 723 */           if (!$assertionsDisabled) throw new AssertionError("SynthTreeCellRenderer is being used outside of UI that created it");
/*     */ 
/* 725 */           return;
/*     */         }
/* 727 */         int i = 0;
/* 728 */         Icon localIcon = getIcon();
/*     */ 
/* 730 */         if ((localIcon != null) && (getText() != null)) {
/* 731 */           i = localIcon.getIconWidth() + Math.max(0, getIconTextGap() - 1);
/*     */         }
/*     */ 
/* 734 */         if (this.selected) {
/* 735 */           localSynthContext.setComponentState(513);
/*     */         }
/*     */         else {
/* 738 */           localSynthContext.setComponentState(1);
/*     */         }
/* 740 */         if (getComponentOrientation().isLeftToRight()) {
/* 741 */           localSynthContext.getPainter().paintTreeCellFocus(localSynthContext, paramGraphics, i, 0, getWidth() - i, getHeight());
/*     */         }
/*     */         else
/*     */         {
/* 746 */           localSynthContext.getPainter().paintTreeCellFocus(localSynthContext, paramGraphics, 0, 0, getWidth() - i, getHeight());
/*     */         }
/*     */ 
/* 749 */         localSynthContext.dispose();
/*     */       }
/* 751 */       SynthLookAndFeel.resetSelectedUI();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthTreeUI
 * JD-Core Version:    0.6.2
 */