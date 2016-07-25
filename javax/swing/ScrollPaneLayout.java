/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.awt.Dimension;
/*      */ import java.awt.Insets;
/*      */ import java.awt.LayoutManager;
/*      */ import java.awt.Rectangle;
/*      */ import java.io.Serializable;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.plaf.UIResource;
/*      */ 
/*      */ public class ScrollPaneLayout
/*      */   implements LayoutManager, ScrollPaneConstants, Serializable
/*      */ {
/*      */   protected JViewport viewport;
/*      */   protected JScrollBar vsb;
/*      */   protected JScrollBar hsb;
/*      */   protected JViewport rowHead;
/*      */   protected JViewport colHead;
/*      */   protected Component lowerLeft;
/*      */   protected Component lowerRight;
/*      */   protected Component upperLeft;
/*      */   protected Component upperRight;
/*      */   protected int vsbPolicy;
/*      */   protected int hsbPolicy;
/*      */ 
/*      */   public ScrollPaneLayout()
/*      */   {
/*  142 */     this.vsbPolicy = 20;
/*      */ 
/*  153 */     this.hsbPolicy = 30;
/*      */   }
/*      */ 
/*      */   public void syncWithScrollPane(JScrollPane paramJScrollPane)
/*      */   {
/*  172 */     this.viewport = paramJScrollPane.getViewport();
/*  173 */     this.vsb = paramJScrollPane.getVerticalScrollBar();
/*  174 */     this.hsb = paramJScrollPane.getHorizontalScrollBar();
/*  175 */     this.rowHead = paramJScrollPane.getRowHeader();
/*  176 */     this.colHead = paramJScrollPane.getColumnHeader();
/*  177 */     this.lowerLeft = paramJScrollPane.getCorner("LOWER_LEFT_CORNER");
/*  178 */     this.lowerRight = paramJScrollPane.getCorner("LOWER_RIGHT_CORNER");
/*  179 */     this.upperLeft = paramJScrollPane.getCorner("UPPER_LEFT_CORNER");
/*  180 */     this.upperRight = paramJScrollPane.getCorner("UPPER_RIGHT_CORNER");
/*  181 */     this.vsbPolicy = paramJScrollPane.getVerticalScrollBarPolicy();
/*  182 */     this.hsbPolicy = paramJScrollPane.getHorizontalScrollBarPolicy();
/*      */   }
/*      */ 
/*      */   protected Component addSingletonComponent(Component paramComponent1, Component paramComponent2)
/*      */   {
/*  201 */     if ((paramComponent1 != null) && (paramComponent1 != paramComponent2)) {
/*  202 */       paramComponent1.getParent().remove(paramComponent1);
/*      */     }
/*  204 */     return paramComponent2;
/*      */   }
/*      */ 
/*      */   public void addLayoutComponent(String paramString, Component paramComponent)
/*      */   {
/*  229 */     if (paramString.equals("VIEWPORT")) {
/*  230 */       this.viewport = ((JViewport)addSingletonComponent(this.viewport, paramComponent));
/*      */     }
/*  232 */     else if (paramString.equals("VERTICAL_SCROLLBAR")) {
/*  233 */       this.vsb = ((JScrollBar)addSingletonComponent(this.vsb, paramComponent));
/*      */     }
/*  235 */     else if (paramString.equals("HORIZONTAL_SCROLLBAR")) {
/*  236 */       this.hsb = ((JScrollBar)addSingletonComponent(this.hsb, paramComponent));
/*      */     }
/*  238 */     else if (paramString.equals("ROW_HEADER")) {
/*  239 */       this.rowHead = ((JViewport)addSingletonComponent(this.rowHead, paramComponent));
/*      */     }
/*  241 */     else if (paramString.equals("COLUMN_HEADER")) {
/*  242 */       this.colHead = ((JViewport)addSingletonComponent(this.colHead, paramComponent));
/*      */     }
/*  244 */     else if (paramString.equals("LOWER_LEFT_CORNER")) {
/*  245 */       this.lowerLeft = addSingletonComponent(this.lowerLeft, paramComponent);
/*      */     }
/*  247 */     else if (paramString.equals("LOWER_RIGHT_CORNER")) {
/*  248 */       this.lowerRight = addSingletonComponent(this.lowerRight, paramComponent);
/*      */     }
/*  250 */     else if (paramString.equals("UPPER_LEFT_CORNER")) {
/*  251 */       this.upperLeft = addSingletonComponent(this.upperLeft, paramComponent);
/*      */     }
/*  253 */     else if (paramString.equals("UPPER_RIGHT_CORNER")) {
/*  254 */       this.upperRight = addSingletonComponent(this.upperRight, paramComponent);
/*      */     }
/*      */     else
/*  257 */       throw new IllegalArgumentException("invalid layout key " + paramString);
/*      */   }
/*      */ 
/*      */   public void removeLayoutComponent(Component paramComponent)
/*      */   {
/*  269 */     if (paramComponent == this.viewport) {
/*  270 */       this.viewport = null;
/*      */     }
/*  272 */     else if (paramComponent == this.vsb) {
/*  273 */       this.vsb = null;
/*      */     }
/*  275 */     else if (paramComponent == this.hsb) {
/*  276 */       this.hsb = null;
/*      */     }
/*  278 */     else if (paramComponent == this.rowHead) {
/*  279 */       this.rowHead = null;
/*      */     }
/*  281 */     else if (paramComponent == this.colHead) {
/*  282 */       this.colHead = null;
/*      */     }
/*  284 */     else if (paramComponent == this.lowerLeft) {
/*  285 */       this.lowerLeft = null;
/*      */     }
/*  287 */     else if (paramComponent == this.lowerRight) {
/*  288 */       this.lowerRight = null;
/*      */     }
/*  290 */     else if (paramComponent == this.upperLeft) {
/*  291 */       this.upperLeft = null;
/*      */     }
/*  293 */     else if (paramComponent == this.upperRight)
/*  294 */       this.upperRight = null;
/*      */   }
/*      */ 
/*      */   public int getVerticalScrollBarPolicy()
/*      */   {
/*  306 */     return this.vsbPolicy;
/*      */   }
/*      */ 
/*      */   public void setVerticalScrollBarPolicy(int paramInt)
/*      */   {
/*  327 */     switch (paramInt) {
/*      */     case 20:
/*      */     case 21:
/*      */     case 22:
/*  331 */       this.vsbPolicy = paramInt;
/*  332 */       break;
/*      */     default:
/*  334 */       throw new IllegalArgumentException("invalid verticalScrollBarPolicy");
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getHorizontalScrollBarPolicy()
/*      */   {
/*  346 */     return this.hsbPolicy;
/*      */   }
/*      */ 
/*      */   public void setHorizontalScrollBarPolicy(int paramInt)
/*      */   {
/*  365 */     switch (paramInt) {
/*      */     case 30:
/*      */     case 31:
/*      */     case 32:
/*  369 */       this.hsbPolicy = paramInt;
/*  370 */       break;
/*      */     default:
/*  372 */       throw new IllegalArgumentException("invalid horizontalScrollBarPolicy");
/*      */     }
/*      */   }
/*      */ 
/*      */   public JViewport getViewport()
/*      */   {
/*  384 */     return this.viewport;
/*      */   }
/*      */ 
/*      */   public JScrollBar getHorizontalScrollBar()
/*      */   {
/*  394 */     return this.hsb;
/*      */   }
/*      */ 
/*      */   public JScrollBar getVerticalScrollBar()
/*      */   {
/*  403 */     return this.vsb;
/*      */   }
/*      */ 
/*      */   public JViewport getRowHeader()
/*      */   {
/*  413 */     return this.rowHead;
/*      */   }
/*      */ 
/*      */   public JViewport getColumnHeader()
/*      */   {
/*  423 */     return this.colHead;
/*      */   }
/*      */ 
/*      */   public Component getCorner(String paramString)
/*      */   {
/*  436 */     if (paramString.equals("LOWER_LEFT_CORNER")) {
/*  437 */       return this.lowerLeft;
/*      */     }
/*  439 */     if (paramString.equals("LOWER_RIGHT_CORNER")) {
/*  440 */       return this.lowerRight;
/*      */     }
/*  442 */     if (paramString.equals("UPPER_LEFT_CORNER")) {
/*  443 */       return this.upperLeft;
/*      */     }
/*  445 */     if (paramString.equals("UPPER_RIGHT_CORNER")) {
/*  446 */       return this.upperRight;
/*      */     }
/*      */ 
/*  449 */     return null;
/*      */   }
/*      */ 
/*      */   public Dimension preferredLayoutSize(Container paramContainer)
/*      */   {
/*  474 */     JScrollPane localJScrollPane = (JScrollPane)paramContainer;
/*  475 */     this.vsbPolicy = localJScrollPane.getVerticalScrollBarPolicy();
/*  476 */     this.hsbPolicy = localJScrollPane.getHorizontalScrollBarPolicy();
/*      */ 
/*  478 */     Insets localInsets1 = paramContainer.getInsets();
/*  479 */     int i = localInsets1.left + localInsets1.right;
/*  480 */     int j = localInsets1.top + localInsets1.bottom;
/*      */ 
/*  487 */     Dimension localDimension1 = null;
/*  488 */     Dimension localDimension2 = null;
/*  489 */     Component localComponent = null;
/*      */ 
/*  491 */     if (this.viewport != null) {
/*  492 */       localDimension1 = this.viewport.getPreferredSize();
/*  493 */       localComponent = this.viewport.getView();
/*  494 */       if (localComponent != null)
/*  495 */         localDimension2 = localComponent.getPreferredSize();
/*      */       else {
/*  497 */         localDimension2 = new Dimension(0, 0);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  504 */     if (localDimension1 != null) {
/*  505 */       i += localDimension1.width;
/*  506 */       j += localDimension1.height;
/*      */     }
/*      */ 
/*  512 */     Border localBorder = localJScrollPane.getViewportBorder();
/*  513 */     if (localBorder != null) {
/*  514 */       Insets localInsets2 = localBorder.getBorderInsets(paramContainer);
/*  515 */       i += localInsets2.left + localInsets2.right;
/*  516 */       j += localInsets2.top + localInsets2.bottom;
/*      */     }
/*      */ 
/*  523 */     if ((this.rowHead != null) && (this.rowHead.isVisible())) {
/*  524 */       i += this.rowHead.getPreferredSize().width;
/*      */     }
/*      */ 
/*  527 */     if ((this.colHead != null) && (this.colHead.isVisible()))
/*  528 */       j += this.colHead.getPreferredSize().height;
/*      */     int k;
/*  548 */     if ((this.vsb != null) && (this.vsbPolicy != 21)) {
/*  549 */       if (this.vsbPolicy == 22) {
/*  550 */         i += this.vsb.getPreferredSize().width;
/*      */       }
/*  552 */       else if ((localDimension2 != null) && (localDimension1 != null)) {
/*  553 */         k = 1;
/*  554 */         if ((localComponent instanceof Scrollable)) {
/*  555 */           k = !((Scrollable)localComponent).getScrollableTracksViewportHeight() ? 1 : 0;
/*      */         }
/*  557 */         if ((k != 0) && (localDimension2.height > localDimension1.height)) {
/*  558 */           i += this.vsb.getPreferredSize().width;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  563 */     if ((this.hsb != null) && (this.hsbPolicy != 31)) {
/*  564 */       if (this.hsbPolicy == 32) {
/*  565 */         j += this.hsb.getPreferredSize().height;
/*      */       }
/*  567 */       else if ((localDimension2 != null) && (localDimension1 != null)) {
/*  568 */         k = 1;
/*  569 */         if ((localComponent instanceof Scrollable)) {
/*  570 */           k = !((Scrollable)localComponent).getScrollableTracksViewportWidth() ? 1 : 0;
/*      */         }
/*  572 */         if ((k != 0) && (localDimension2.width > localDimension1.width)) {
/*  573 */           j += this.hsb.getPreferredSize().height;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  578 */     return new Dimension(i, j);
/*      */   }
/*      */ 
/*      */   public Dimension minimumLayoutSize(Container paramContainer)
/*      */   {
/*  597 */     JScrollPane localJScrollPane = (JScrollPane)paramContainer;
/*  598 */     this.vsbPolicy = localJScrollPane.getVerticalScrollBarPolicy();
/*  599 */     this.hsbPolicy = localJScrollPane.getHorizontalScrollBarPolicy();
/*      */ 
/*  601 */     Insets localInsets = paramContainer.getInsets();
/*  602 */     int i = localInsets.left + localInsets.right;
/*  603 */     int j = localInsets.top + localInsets.bottom;
/*      */ 
/*  608 */     if (this.viewport != null) {
/*  609 */       localObject1 = this.viewport.getMinimumSize();
/*  610 */       i += ((Dimension)localObject1).width;
/*  611 */       j += ((Dimension)localObject1).height;
/*      */     }
/*      */ 
/*  617 */     Object localObject1 = localJScrollPane.getViewportBorder();
/*      */     Object localObject2;
/*  618 */     if (localObject1 != null) {
/*  619 */       localObject2 = ((Border)localObject1).getBorderInsets(paramContainer);
/*  620 */       i += ((Insets)localObject2).left + ((Insets)localObject2).right;
/*  621 */       j += ((Insets)localObject2).top + ((Insets)localObject2).bottom;
/*      */     }
/*      */ 
/*  628 */     if ((this.rowHead != null) && (this.rowHead.isVisible())) {
/*  629 */       localObject2 = this.rowHead.getMinimumSize();
/*  630 */       i += ((Dimension)localObject2).width;
/*  631 */       j = Math.max(j, ((Dimension)localObject2).height);
/*      */     }
/*      */ 
/*  634 */     if ((this.colHead != null) && (this.colHead.isVisible())) {
/*  635 */       localObject2 = this.colHead.getMinimumSize();
/*  636 */       i = Math.max(i, ((Dimension)localObject2).width);
/*  637 */       j += ((Dimension)localObject2).height;
/*      */     }
/*      */ 
/*  644 */     if ((this.vsb != null) && (this.vsbPolicy != 21)) {
/*  645 */       localObject2 = this.vsb.getMinimumSize();
/*  646 */       i += ((Dimension)localObject2).width;
/*  647 */       j = Math.max(j, ((Dimension)localObject2).height);
/*      */     }
/*      */ 
/*  650 */     if ((this.hsb != null) && (this.hsbPolicy != 31)) {
/*  651 */       localObject2 = this.hsb.getMinimumSize();
/*  652 */       i = Math.max(i, ((Dimension)localObject2).width);
/*  653 */       j += ((Dimension)localObject2).height;
/*      */     }
/*      */ 
/*  656 */     return new Dimension(i, j);
/*      */   }
/*      */ 
/*      */   public void layoutContainer(Container paramContainer)
/*      */   {
/*  698 */     JScrollPane localJScrollPane = (JScrollPane)paramContainer;
/*  699 */     this.vsbPolicy = localJScrollPane.getVerticalScrollBarPolicy();
/*  700 */     this.hsbPolicy = localJScrollPane.getHorizontalScrollBarPolicy();
/*      */ 
/*  702 */     Rectangle localRectangle1 = localJScrollPane.getBounds();
/*  703 */     localRectangle1.x = (localRectangle1.y = 0);
/*      */ 
/*  705 */     Insets localInsets1 = paramContainer.getInsets();
/*  706 */     localRectangle1.x = localInsets1.left;
/*  707 */     localRectangle1.y = localInsets1.top;
/*  708 */     localRectangle1.width -= localInsets1.left + localInsets1.right;
/*  709 */     localRectangle1.height -= localInsets1.top + localInsets1.bottom;
/*      */ 
/*  713 */     boolean bool1 = SwingUtilities.isLeftToRight(localJScrollPane);
/*      */ 
/*  720 */     Rectangle localRectangle2 = new Rectangle(0, localRectangle1.y, 0, 0);
/*      */ 
/*  722 */     if ((this.colHead != null) && (this.colHead.isVisible())) {
/*  723 */       int i = Math.min(localRectangle1.height, this.colHead.getPreferredSize().height);
/*      */ 
/*  725 */       localRectangle2.height = i;
/*  726 */       localRectangle1.y += i;
/*  727 */       localRectangle1.height -= i;
/*      */     }
/*      */ 
/*  735 */     Rectangle localRectangle3 = new Rectangle(0, 0, 0, 0);
/*      */ 
/*  737 */     if ((this.rowHead != null) && (this.rowHead.isVisible())) {
/*  738 */       int j = Math.min(localRectangle1.width, this.rowHead.getPreferredSize().width);
/*      */ 
/*  740 */       localRectangle3.width = j;
/*  741 */       localRectangle1.width -= j;
/*  742 */       if (bool1) {
/*  743 */         localRectangle3.x = localRectangle1.x;
/*  744 */         localRectangle1.x += j;
/*      */       } else {
/*  746 */         localRectangle1.x += localRectangle1.width;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  754 */     Border localBorder = localJScrollPane.getViewportBorder();
/*      */     Insets localInsets2;
/*  756 */     if (localBorder != null) {
/*  757 */       localInsets2 = localBorder.getBorderInsets(paramContainer);
/*  758 */       localRectangle1.x += localInsets2.left;
/*  759 */       localRectangle1.y += localInsets2.top;
/*  760 */       localRectangle1.width -= localInsets2.left + localInsets2.right;
/*  761 */       localRectangle1.height -= localInsets2.top + localInsets2.bottom;
/*      */     }
/*      */     else {
/*  764 */       localInsets2 = new Insets(0, 0, 0, 0);
/*      */     }
/*      */ 
/*  789 */     Object localObject = this.viewport != null ? this.viewport.getView() : null;
/*  790 */     Dimension localDimension1 = localObject != null ? localObject.getPreferredSize() : new Dimension(0, 0);
/*      */ 
/*  794 */     Dimension localDimension2 = this.viewport != null ? this.viewport.toViewCoordinates(localRectangle1.getSize()) : new Dimension(0, 0);
/*      */ 
/*  798 */     boolean bool2 = false;
/*  799 */     boolean bool3 = false;
/*  800 */     int k = (localRectangle1.width < 0) || (localRectangle1.height < 0) ? 1 : 0;
/*      */     Scrollable localScrollable;
/*  805 */     if ((k == 0) && ((localObject instanceof Scrollable))) {
/*  806 */       localScrollable = (Scrollable)localObject;
/*  807 */       bool2 = localScrollable.getScrollableTracksViewportWidth();
/*  808 */       bool3 = localScrollable.getScrollableTracksViewportHeight();
/*      */     }
/*      */     else {
/*  811 */       localScrollable = null;
/*      */     }
/*      */ 
/*  819 */     Rectangle localRectangle4 = new Rectangle(0, localRectangle1.y - localInsets2.top, 0, 0);
/*      */     boolean bool4;
/*  822 */     if (k != 0) {
/*  823 */       bool4 = false;
/*      */     }
/*  825 */     else if (this.vsbPolicy == 22) {
/*  826 */       bool4 = true;
/*      */     }
/*  828 */     else if (this.vsbPolicy == 21) {
/*  829 */       bool4 = false;
/*      */     }
/*      */     else {
/*  832 */       bool4 = (!bool3) && (localDimension1.height > localDimension2.height);
/*      */     }
/*      */ 
/*  836 */     if ((this.vsb != null) && (bool4)) {
/*  837 */       adjustForVSB(true, localRectangle1, localRectangle4, localInsets2, bool1);
/*  838 */       localDimension2 = this.viewport.toViewCoordinates(localRectangle1.getSize());
/*      */     }
/*      */ 
/*  846 */     Rectangle localRectangle5 = new Rectangle(localRectangle1.x - localInsets2.left, 0, 0, 0);
/*      */     boolean bool5;
/*  848 */     if (k != 0) {
/*  849 */       bool5 = false;
/*      */     }
/*  851 */     else if (this.hsbPolicy == 32) {
/*  852 */       bool5 = true;
/*      */     }
/*  854 */     else if (this.hsbPolicy == 31) {
/*  855 */       bool5 = false;
/*      */     }
/*      */     else {
/*  858 */       bool5 = (!bool2) && (localDimension1.width > localDimension2.width);
/*      */     }
/*      */ 
/*  861 */     if ((this.hsb != null) && (bool5)) {
/*  862 */       adjustForHSB(true, localRectangle1, localRectangle5, localInsets2);
/*      */ 
/*  870 */       if ((this.vsb != null) && (!bool4) && (this.vsbPolicy != 21))
/*      */       {
/*  873 */         localDimension2 = this.viewport.toViewCoordinates(localRectangle1.getSize());
/*  874 */         bool4 = localDimension1.height > localDimension2.height;
/*      */ 
/*  876 */         if (bool4) {
/*  877 */           adjustForVSB(true, localRectangle1, localRectangle4, localInsets2, bool1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  889 */     if (this.viewport != null) {
/*  890 */       this.viewport.setBounds(localRectangle1);
/*      */ 
/*  892 */       if (localScrollable != null) {
/*  893 */         localDimension2 = this.viewport.toViewCoordinates(localRectangle1.getSize());
/*      */ 
/*  895 */         boolean bool6 = bool5;
/*  896 */         boolean bool7 = bool4;
/*  897 */         bool2 = localScrollable.getScrollableTracksViewportWidth();
/*      */ 
/*  899 */         bool3 = localScrollable.getScrollableTracksViewportHeight();
/*      */         boolean bool8;
/*  901 */         if ((this.vsb != null) && (this.vsbPolicy == 20)) {
/*  902 */           bool8 = (!bool3) && (localDimension1.height > localDimension2.height);
/*      */ 
/*  904 */           if (bool8 != bool4) {
/*  905 */             bool4 = bool8;
/*  906 */             adjustForVSB(bool4, localRectangle1, localRectangle4, localInsets2, bool1);
/*      */ 
/*  908 */             localDimension2 = this.viewport.toViewCoordinates(localRectangle1.getSize());
/*      */           }
/*      */         }
/*      */ 
/*  912 */         if ((this.hsb != null) && (this.hsbPolicy == 30)) {
/*  913 */           bool8 = (!bool2) && (localDimension1.width > localDimension2.width);
/*      */ 
/*  915 */           if (bool8 != bool5) {
/*  916 */             bool5 = bool8;
/*  917 */             adjustForHSB(bool5, localRectangle1, localRectangle5, localInsets2);
/*  918 */             if ((this.vsb != null) && (!bool4) && (this.vsbPolicy != 21))
/*      */             {
/*  921 */               localDimension2 = this.viewport.toViewCoordinates(localRectangle1.getSize());
/*      */ 
/*  923 */               bool4 = localDimension1.height > localDimension2.height;
/*      */ 
/*  926 */               if (bool4) {
/*  927 */                 adjustForVSB(true, localRectangle1, localRectangle4, localInsets2, bool1);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/*  933 */         if ((bool6 != bool5) || (bool7 != bool4))
/*      */         {
/*  935 */           this.viewport.setBounds(localRectangle1);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  947 */     localRectangle4.height = (localRectangle1.height + localInsets2.top + localInsets2.bottom);
/*  948 */     localRectangle5.width = (localRectangle1.width + localInsets2.left + localInsets2.right);
/*  949 */     localRectangle3.height = (localRectangle1.height + localInsets2.top + localInsets2.bottom);
/*  950 */     localRectangle1.y -= localInsets2.top;
/*  951 */     localRectangle2.width = (localRectangle1.width + localInsets2.left + localInsets2.right);
/*  952 */     localRectangle1.x -= localInsets2.left;
/*      */ 
/*  958 */     if (this.rowHead != null) {
/*  959 */       this.rowHead.setBounds(localRectangle3);
/*      */     }
/*      */ 
/*  962 */     if (this.colHead != null) {
/*  963 */       this.colHead.setBounds(localRectangle2);
/*      */     }
/*      */ 
/*  966 */     if (this.vsb != null) {
/*  967 */       if (bool4) {
/*  968 */         if ((this.colHead != null) && (UIManager.getBoolean("ScrollPane.fillUpperCorner")))
/*      */         {
/*  971 */           if (((bool1) && (this.upperRight == null)) || ((!bool1) && (this.upperLeft == null)))
/*      */           {
/*  979 */             localRectangle4.y = localRectangle2.y;
/*  980 */             localRectangle4.height += localRectangle2.height;
/*      */           }
/*      */         }
/*  983 */         this.vsb.setVisible(true);
/*  984 */         this.vsb.setBounds(localRectangle4);
/*      */       }
/*      */       else {
/*  987 */         this.vsb.setVisible(false);
/*      */       }
/*      */     }
/*      */ 
/*  991 */     if (this.hsb != null) {
/*  992 */       if (bool5) {
/*  993 */         if ((this.rowHead != null) && (UIManager.getBoolean("ScrollPane.fillLowerCorner")))
/*      */         {
/*  996 */           if (((bool1) && (this.lowerLeft == null)) || ((!bool1) && (this.lowerRight == null)))
/*      */           {
/* 1004 */             if (bool1) {
/* 1005 */               localRectangle5.x = localRectangle3.x;
/*      */             }
/* 1007 */             localRectangle5.width += localRectangle3.width;
/*      */           }
/*      */         }
/* 1010 */         this.hsb.setVisible(true);
/* 1011 */         this.hsb.setBounds(localRectangle5);
/*      */       }
/*      */       else {
/* 1014 */         this.hsb.setVisible(false);
/*      */       }
/*      */     }
/*      */ 
/* 1018 */     if (this.lowerLeft != null) {
/* 1019 */       this.lowerLeft.setBounds(bool1 ? localRectangle3.x : localRectangle4.x, localRectangle5.y, bool1 ? localRectangle3.width : localRectangle4.width, localRectangle5.height);
/*      */     }
/*      */ 
/* 1025 */     if (this.lowerRight != null) {
/* 1026 */       this.lowerRight.setBounds(bool1 ? localRectangle4.x : localRectangle3.x, localRectangle5.y, bool1 ? localRectangle4.width : localRectangle3.width, localRectangle5.height);
/*      */     }
/*      */ 
/* 1032 */     if (this.upperLeft != null) {
/* 1033 */       this.upperLeft.setBounds(bool1 ? localRectangle3.x : localRectangle4.x, localRectangle2.y, bool1 ? localRectangle3.width : localRectangle4.width, localRectangle2.height);
/*      */     }
/*      */ 
/* 1039 */     if (this.upperRight != null)
/* 1040 */       this.upperRight.setBounds(bool1 ? localRectangle4.x : localRectangle3.x, localRectangle2.y, bool1 ? localRectangle4.width : localRectangle3.width, localRectangle2.height);
/*      */   }
/*      */ 
/*      */   private void adjustForVSB(boolean paramBoolean1, Rectangle paramRectangle1, Rectangle paramRectangle2, Insets paramInsets, boolean paramBoolean2)
/*      */   {
/* 1058 */     int i = paramRectangle2.width;
/* 1059 */     if (paramBoolean1) {
/* 1060 */       int j = Math.max(0, Math.min(this.vsb.getPreferredSize().width, paramRectangle1.width));
/*      */ 
/* 1063 */       paramRectangle1.width -= j;
/* 1064 */       paramRectangle2.width = j;
/*      */ 
/* 1066 */       if (paramBoolean2) {
/* 1067 */         paramRectangle2.x = (paramRectangle1.x + paramRectangle1.width + paramInsets.right);
/*      */       } else {
/* 1069 */         paramRectangle1.x -= paramInsets.left;
/* 1070 */         paramRectangle1.x += j;
/*      */       }
/*      */     }
/*      */     else {
/* 1074 */       paramRectangle1.width += i;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void adjustForHSB(boolean paramBoolean, Rectangle paramRectangle1, Rectangle paramRectangle2, Insets paramInsets)
/*      */   {
/* 1088 */     int i = paramRectangle2.height;
/* 1089 */     if (paramBoolean) {
/* 1090 */       int j = Math.max(0, Math.min(paramRectangle1.height, this.hsb.getPreferredSize().height));
/*      */ 
/* 1093 */       paramRectangle1.height -= j;
/* 1094 */       paramRectangle2.y = (paramRectangle1.y + paramRectangle1.height + paramInsets.bottom);
/* 1095 */       paramRectangle2.height = j;
/*      */     }
/*      */     else {
/* 1098 */       paramRectangle1.height += i;
/*      */     }
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Rectangle getViewportBorderBounds(JScrollPane paramJScrollPane)
/*      */   {
/* 1114 */     return paramJScrollPane.getViewportBorderBounds();
/*      */   }
/*      */ 
/*      */   public static class UIResource extends ScrollPaneLayout
/*      */     implements UIResource
/*      */   {
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.ScrollPaneLayout
 * JD-Core Version:    0.6.2
 */