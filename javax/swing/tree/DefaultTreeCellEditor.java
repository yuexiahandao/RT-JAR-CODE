/*     */ package javax.swing.tree;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ActionEvent;
/*     */ import java.awt.event.ActionListener;
/*     */ import java.awt.event.MouseEvent;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.EventObject;
/*     */ import java.util.Vector;
/*     */ import javax.swing.DefaultCellEditor;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.JTree;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.Timer;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.event.CellEditorListener;
/*     */ import javax.swing.event.TreeSelectionEvent;
/*     */ import javax.swing.event.TreeSelectionListener;
/*     */ import javax.swing.plaf.FontUIResource;
/*     */ 
/*     */ public class DefaultTreeCellEditor
/*     */   implements ActionListener, TreeCellEditor, TreeSelectionListener
/*     */ {
/*     */   protected TreeCellEditor realEditor;
/*     */   protected DefaultTreeCellRenderer renderer;
/*     */   protected Container editingContainer;
/*     */   protected transient Component editingComponent;
/*     */   protected boolean canEdit;
/*     */   protected transient int offset;
/*     */   protected transient JTree tree;
/*     */   protected transient TreePath lastPath;
/*     */   protected transient Timer timer;
/*     */   protected transient int lastRow;
/*     */   protected Color borderSelectionColor;
/*     */   protected transient Icon editingIcon;
/*     */   protected Font font;
/*     */ 
/*     */   public DefaultTreeCellEditor(JTree paramJTree, DefaultTreeCellRenderer paramDefaultTreeCellRenderer)
/*     */   {
/* 131 */     this(paramJTree, paramDefaultTreeCellRenderer, null);
/*     */   }
/*     */ 
/*     */   public DefaultTreeCellEditor(JTree paramJTree, DefaultTreeCellRenderer paramDefaultTreeCellRenderer, TreeCellEditor paramTreeCellEditor)
/*     */   {
/* 146 */     this.renderer = paramDefaultTreeCellRenderer;
/* 147 */     this.realEditor = paramTreeCellEditor;
/* 148 */     if (this.realEditor == null)
/* 149 */       this.realEditor = createTreeCellEditor();
/* 150 */     this.editingContainer = createContainer();
/* 151 */     setTree(paramJTree);
/* 152 */     setBorderSelectionColor(UIManager.getColor("Tree.editorBorderSelectionColor"));
/*     */   }
/*     */ 
/*     */   public void setBorderSelectionColor(Color paramColor)
/*     */   {
/* 161 */     this.borderSelectionColor = paramColor;
/*     */   }
/*     */ 
/*     */   public Color getBorderSelectionColor()
/*     */   {
/* 169 */     return this.borderSelectionColor;
/*     */   }
/*     */ 
/*     */   public void setFont(Font paramFont)
/*     */   {
/* 184 */     this.font = paramFont;
/*     */   }
/*     */ 
/*     */   public Font getFont()
/*     */   {
/* 194 */     return this.font;
/*     */   }
/*     */ 
/*     */   public Component getTreeCellEditorComponent(JTree paramJTree, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt)
/*     */   {
/* 208 */     setTree(paramJTree);
/* 209 */     this.lastRow = paramInt;
/* 210 */     determineOffset(paramJTree, paramObject, paramBoolean1, paramBoolean2, paramBoolean3, paramInt);
/*     */ 
/* 212 */     if (this.editingComponent != null) {
/* 213 */       this.editingContainer.remove(this.editingComponent);
/*     */     }
/* 215 */     this.editingComponent = this.realEditor.getTreeCellEditorComponent(paramJTree, paramObject, paramBoolean1, paramBoolean2, paramBoolean3, paramInt);
/*     */ 
/* 221 */     TreePath localTreePath = paramJTree.getPathForRow(paramInt);
/*     */ 
/* 223 */     this.canEdit = ((this.lastPath != null) && (localTreePath != null) && (this.lastPath.equals(localTreePath)));
/*     */ 
/* 226 */     Font localFont = getFont();
/*     */ 
/* 228 */     if (localFont == null) {
/* 229 */       if (this.renderer != null)
/* 230 */         localFont = this.renderer.getFont();
/* 231 */       if (localFont == null)
/* 232 */         localFont = paramJTree.getFont();
/*     */     }
/* 234 */     this.editingContainer.setFont(localFont);
/* 235 */     prepareForEditing();
/* 236 */     return this.editingContainer;
/*     */   }
/*     */ 
/*     */   public Object getCellEditorValue()
/*     */   {
/* 244 */     return this.realEditor.getCellEditorValue();
/*     */   }
/*     */ 
/*     */   public boolean isCellEditable(EventObject paramEventObject)
/*     */   {
/* 253 */     boolean bool1 = false;
/* 254 */     int i = 0;
/*     */ 
/* 256 */     if ((paramEventObject != null) && 
/* 257 */       ((paramEventObject.getSource() instanceof JTree))) {
/* 258 */       setTree((JTree)paramEventObject.getSource());
/* 259 */       if ((paramEventObject instanceof MouseEvent)) {
/* 260 */         TreePath localTreePath = this.tree.getPathForLocation(((MouseEvent)paramEventObject).getX(), ((MouseEvent)paramEventObject).getY());
/*     */ 
/* 263 */         i = (this.lastPath != null) && (localTreePath != null) && (this.lastPath.equals(localTreePath)) ? 1 : 0;
/*     */ 
/* 265 */         if (localTreePath != null) {
/* 266 */           this.lastRow = this.tree.getRowForPath(localTreePath);
/* 267 */           Object localObject = localTreePath.getLastPathComponent();
/* 268 */           boolean bool2 = this.tree.isRowSelected(this.lastRow);
/* 269 */           boolean bool3 = this.tree.isExpanded(localTreePath);
/* 270 */           TreeModel localTreeModel = this.tree.getModel();
/* 271 */           boolean bool4 = localTreeModel.isLeaf(localObject);
/* 272 */           determineOffset(this.tree, localObject, bool2, bool3, bool4, this.lastRow);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 278 */     if (!this.realEditor.isCellEditable(paramEventObject))
/* 279 */       return false;
/* 280 */     if (canEditImmediately(paramEventObject))
/* 281 */       bool1 = true;
/* 282 */     else if ((i != 0) && (shouldStartEditingTimer(paramEventObject))) {
/* 283 */       startEditingTimer();
/*     */     }
/* 285 */     else if ((this.timer != null) && (this.timer.isRunning()))
/* 286 */       this.timer.stop();
/* 287 */     if (bool1)
/* 288 */       prepareForEditing();
/* 289 */     return bool1;
/*     */   }
/*     */ 
/*     */   public boolean shouldSelectCell(EventObject paramEventObject)
/*     */   {
/* 296 */     return this.realEditor.shouldSelectCell(paramEventObject);
/*     */   }
/*     */ 
/*     */   public boolean stopCellEditing()
/*     */   {
/* 305 */     if (this.realEditor.stopCellEditing()) {
/* 306 */       cleanupAfterEditing();
/* 307 */       return true;
/*     */     }
/* 309 */     return false;
/*     */   }
/*     */ 
/*     */   public void cancelCellEditing()
/*     */   {
/* 317 */     this.realEditor.cancelCellEditing();
/* 318 */     cleanupAfterEditing();
/*     */   }
/*     */ 
/*     */   public void addCellEditorListener(CellEditorListener paramCellEditorListener)
/*     */   {
/* 326 */     this.realEditor.addCellEditorListener(paramCellEditorListener);
/*     */   }
/*     */ 
/*     */   public void removeCellEditorListener(CellEditorListener paramCellEditorListener)
/*     */   {
/* 334 */     this.realEditor.removeCellEditorListener(paramCellEditorListener);
/*     */   }
/*     */ 
/*     */   public CellEditorListener[] getCellEditorListeners()
/*     */   {
/* 346 */     return ((DefaultCellEditor)this.realEditor).getCellEditorListeners();
/*     */   }
/*     */ 
/*     */   public void valueChanged(TreeSelectionEvent paramTreeSelectionEvent)
/*     */   {
/* 357 */     if (this.tree != null) {
/* 358 */       if (this.tree.getSelectionCount() == 1)
/* 359 */         this.lastPath = this.tree.getSelectionPath();
/*     */       else
/* 361 */         this.lastPath = null;
/*     */     }
/* 363 */     if (this.timer != null)
/* 364 */       this.timer.stop();
/*     */   }
/*     */ 
/*     */   public void actionPerformed(ActionEvent paramActionEvent)
/*     */   {
/* 377 */     if ((this.tree != null) && (this.lastPath != null))
/* 378 */       this.tree.startEditingAtPath(this.lastPath);
/*     */   }
/*     */ 
/*     */   protected void setTree(JTree paramJTree)
/*     */   {
/* 392 */     if (this.tree != paramJTree) {
/* 393 */       if (this.tree != null)
/* 394 */         this.tree.removeTreeSelectionListener(this);
/* 395 */       this.tree = paramJTree;
/* 396 */       if (this.tree != null)
/* 397 */         this.tree.addTreeSelectionListener(this);
/* 398 */       if (this.timer != null)
/* 399 */         this.timer.stop();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected boolean shouldStartEditingTimer(EventObject paramEventObject)
/*     */   {
/* 410 */     if (((paramEventObject instanceof MouseEvent)) && (SwingUtilities.isLeftMouseButton((MouseEvent)paramEventObject)))
/*     */     {
/* 412 */       MouseEvent localMouseEvent = (MouseEvent)paramEventObject;
/*     */ 
/* 414 */       return (localMouseEvent.getClickCount() == 1) && (inHitRegion(localMouseEvent.getX(), localMouseEvent.getY()));
/*     */     }
/*     */ 
/* 417 */     return false;
/*     */   }
/*     */ 
/*     */   protected void startEditingTimer()
/*     */   {
/* 424 */     if (this.timer == null) {
/* 425 */       this.timer = new Timer(1200, this);
/* 426 */       this.timer.setRepeats(false);
/*     */     }
/* 428 */     this.timer.start();
/*     */   }
/*     */ 
/*     */   protected boolean canEditImmediately(EventObject paramEventObject)
/*     */   {
/* 438 */     if (((paramEventObject instanceof MouseEvent)) && (SwingUtilities.isLeftMouseButton((MouseEvent)paramEventObject)))
/*     */     {
/* 440 */       MouseEvent localMouseEvent = (MouseEvent)paramEventObject;
/*     */ 
/* 442 */       return (localMouseEvent.getClickCount() > 2) && (inHitRegion(localMouseEvent.getX(), localMouseEvent.getY()));
/*     */     }
/*     */ 
/* 445 */     return paramEventObject == null;
/*     */   }
/*     */ 
/*     */   protected boolean inHitRegion(int paramInt1, int paramInt2)
/*     */   {
/* 460 */     if ((this.lastRow != -1) && (this.tree != null)) {
/* 461 */       Rectangle localRectangle = this.tree.getRowBounds(this.lastRow);
/* 462 */       ComponentOrientation localComponentOrientation = this.tree.getComponentOrientation();
/*     */ 
/* 464 */       if (localComponentOrientation.isLeftToRight()) {
/* 465 */         if ((localRectangle != null) && (paramInt1 <= localRectangle.x + this.offset) && (this.offset < localRectangle.width - 5))
/*     */         {
/* 467 */           return false;
/*     */         }
/* 469 */       } else if ((localRectangle != null) && ((paramInt1 >= localRectangle.x + localRectangle.width - this.offset + 5) || (paramInt1 <= localRectangle.x + 5)) && (this.offset < localRectangle.width - 5))
/*     */       {
/* 473 */         return false;
/*     */       }
/*     */     }
/* 476 */     return true;
/*     */   }
/*     */ 
/*     */   protected void determineOffset(JTree paramJTree, Object paramObject, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt)
/*     */   {
/* 482 */     if (this.renderer != null) {
/* 483 */       if (paramBoolean3)
/* 484 */         this.editingIcon = this.renderer.getLeafIcon();
/* 485 */       else if (paramBoolean2)
/* 486 */         this.editingIcon = this.renderer.getOpenIcon();
/*     */       else
/* 488 */         this.editingIcon = this.renderer.getClosedIcon();
/* 489 */       if (this.editingIcon != null) {
/* 490 */         this.offset = (this.renderer.getIconTextGap() + this.editingIcon.getIconWidth());
/*     */       }
/*     */       else
/* 493 */         this.offset = this.renderer.getIconTextGap();
/*     */     }
/*     */     else {
/* 496 */       this.editingIcon = null;
/* 497 */       this.offset = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void prepareForEditing()
/*     */   {
/* 507 */     if (this.editingComponent != null)
/* 508 */       this.editingContainer.add(this.editingComponent);
/*     */   }
/*     */ 
/*     */   protected Container createContainer()
/*     */   {
/* 517 */     return new EditorContainer();
/*     */   }
/*     */ 
/*     */   protected TreeCellEditor createTreeCellEditor()
/*     */   {
/* 527 */     Border localBorder = UIManager.getBorder("Tree.editorBorder");
/* 528 */     DefaultCellEditor local1 = new DefaultCellEditor(new DefaultTextField(localBorder))
/*     */     {
/*     */       public boolean shouldSelectCell(EventObject paramAnonymousEventObject) {
/* 531 */         boolean bool = super.shouldSelectCell(paramAnonymousEventObject);
/* 532 */         return bool;
/*     */       }
/*     */     };
/* 537 */     local1.setClickCountToStart(1);
/* 538 */     return local1;
/*     */   }
/*     */ 
/*     */   private void cleanupAfterEditing()
/*     */   {
/* 546 */     if (this.editingComponent != null) {
/* 547 */       this.editingContainer.remove(this.editingComponent);
/*     */     }
/* 549 */     this.editingComponent = null;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream) throws IOException
/*     */   {
/* 554 */     Vector localVector = new Vector();
/*     */ 
/* 556 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 558 */     if ((this.realEditor != null) && ((this.realEditor instanceof Serializable))) {
/* 559 */       localVector.addElement("realEditor");
/* 560 */       localVector.addElement(this.realEditor);
/*     */     }
/* 562 */     paramObjectOutputStream.writeObject(localVector);
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream) throws IOException, ClassNotFoundException
/*     */   {
/* 567 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 569 */     Vector localVector = (Vector)paramObjectInputStream.readObject();
/* 570 */     int i = 0;
/* 571 */     int j = localVector.size();
/*     */ 
/* 573 */     if ((i < j) && (localVector.elementAt(i).equals("realEditor")))
/*     */     {
/* 575 */       this.realEditor = ((TreeCellEditor)localVector.elementAt(++i));
/* 576 */       i++;
/*     */     }
/*     */   }
/*     */ 
/*     */   public class DefaultTextField extends JTextField
/*     */   {
/*     */     protected Border border;
/*     */ 
/*     */     public DefaultTextField(Border arg2)
/*     */     {
/*     */       Border localBorder;
/* 600 */       setBorder(localBorder);
/*     */     }
/*     */ 
/*     */     public void setBorder(Border paramBorder)
/*     */     {
/* 617 */       super.setBorder(paramBorder);
/* 618 */       this.border = paramBorder;
/*     */     }
/*     */ 
/*     */     public Border getBorder()
/*     */     {
/* 626 */       return this.border;
/*     */     }
/*     */ 
/*     */     public Font getFont()
/*     */     {
/* 631 */       Font localFont = super.getFont();
/*     */ 
/* 635 */       if ((localFont instanceof FontUIResource)) {
/* 636 */         Container localContainer = getParent();
/*     */ 
/* 638 */         if ((localContainer != null) && (localContainer.getFont() != null))
/* 639 */           localFont = localContainer.getFont();
/*     */       }
/* 641 */       return localFont;
/*     */     }
/*     */ 
/*     */     public Dimension getPreferredSize()
/*     */     {
/* 652 */       Dimension localDimension1 = super.getPreferredSize();
/*     */ 
/* 655 */       if ((DefaultTreeCellEditor.this.renderer != null) && (DefaultTreeCellEditor.this.getFont() == null))
/*     */       {
/* 657 */         Dimension localDimension2 = DefaultTreeCellEditor.this.renderer.getPreferredSize();
/*     */ 
/* 659 */         localDimension1.height = localDimension2.height;
/*     */       }
/* 661 */       return localDimension1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public class EditorContainer extends Container
/*     */   {
/*     */     public EditorContainer()
/*     */     {
/* 674 */       setLayout(null);
/*     */     }
/*     */ 
/*     */     public void EditorContainer()
/*     */     {
/* 680 */       setLayout(null);
/*     */     }
/*     */ 
/*     */     public void paint(Graphics paramGraphics)
/*     */     {
/* 688 */       int i = getWidth();
/* 689 */       int j = getHeight();
/*     */ 
/* 692 */       if (DefaultTreeCellEditor.this.editingIcon != null) {
/* 693 */         int k = calculateIconY(DefaultTreeCellEditor.this.editingIcon);
/*     */ 
/* 695 */         if (getComponentOrientation().isLeftToRight())
/* 696 */           DefaultTreeCellEditor.this.editingIcon.paintIcon(this, paramGraphics, 0, k);
/*     */         else {
/* 698 */           DefaultTreeCellEditor.this.editingIcon.paintIcon(this, paramGraphics, i - DefaultTreeCellEditor.this.editingIcon.getIconWidth(), k);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 705 */       Color localColor = DefaultTreeCellEditor.this.getBorderSelectionColor();
/* 706 */       if (localColor != null) {
/* 707 */         paramGraphics.setColor(localColor);
/* 708 */         paramGraphics.drawRect(0, 0, i - 1, j - 1);
/*     */       }
/* 710 */       super.paint(paramGraphics);
/*     */     }
/*     */ 
/*     */     public void doLayout()
/*     */     {
/* 719 */       if (DefaultTreeCellEditor.this.editingComponent != null) {
/* 720 */         int i = getWidth();
/* 721 */         int j = getHeight();
/* 722 */         if (getComponentOrientation().isLeftToRight()) {
/* 723 */           DefaultTreeCellEditor.this.editingComponent.setBounds(DefaultTreeCellEditor.this.offset, 0, i - DefaultTreeCellEditor.this.offset, j);
/*     */         }
/*     */         else
/* 726 */           DefaultTreeCellEditor.this.editingComponent.setBounds(0, 0, i - DefaultTreeCellEditor.this.offset, j);
/*     */       }
/*     */     }
/*     */ 
/*     */     private int calculateIconY(Icon paramIcon)
/*     */     {
/* 739 */       int i = paramIcon.getIconHeight();
/* 740 */       int j = DefaultTreeCellEditor.this.editingComponent.getFontMetrics(DefaultTreeCellEditor.this.editingComponent.getFont()).getHeight();
/*     */ 
/* 742 */       int k = i / 2 - j / 2;
/* 743 */       int m = Math.min(0, k);
/* 744 */       int n = Math.max(i, k + j) - m;
/*     */ 
/* 746 */       return getHeight() / 2 - (m + n / 2);
/*     */     }
/*     */ 
/*     */     public Dimension getPreferredSize()
/*     */     {
/* 759 */       if (DefaultTreeCellEditor.this.editingComponent != null) {
/* 760 */         Dimension localDimension = DefaultTreeCellEditor.this.editingComponent.getPreferredSize();
/*     */ 
/* 762 */         localDimension.width += DefaultTreeCellEditor.this.offset + 5;
/*     */ 
/* 764 */         Object localObject = DefaultTreeCellEditor.this.renderer != null ? DefaultTreeCellEditor.this.renderer.getPreferredSize() : null;
/*     */ 
/* 767 */         if (localObject != null)
/* 768 */           localDimension.height = Math.max(localDimension.height, localObject.height);
/* 769 */         if (DefaultTreeCellEditor.this.editingIcon != null) {
/* 770 */           localDimension.height = Math.max(localDimension.height, DefaultTreeCellEditor.this.editingIcon.getIconHeight());
/*     */         }
/*     */ 
/* 774 */         localDimension.width = Math.max(localDimension.width, 100);
/* 775 */         return localDimension;
/*     */       }
/* 777 */       return new Dimension(0, 0);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.tree.DefaultTreeCellEditor
 * JD-Core Version:    0.6.2
 */