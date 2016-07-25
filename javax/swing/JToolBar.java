/*     */ package javax.swing;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.LayoutManager2;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import javax.accessibility.Accessible;
/*     */ import javax.accessibility.AccessibleContext;
/*     */ import javax.accessibility.AccessibleRole;
/*     */ import javax.accessibility.AccessibleStateSet;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.ToolBarUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ 
/*     */ public class JToolBar extends JComponent
/*     */   implements SwingConstants, Accessible
/*     */ {
/*     */   private static final String uiClassID = "ToolBarUI";
/*  97 */   private boolean paintBorder = true;
/*  98 */   private Insets margin = null;
/*  99 */   private boolean floatable = true;
/* 100 */   private int orientation = 0;
/*     */ 
/*     */   public JToolBar()
/*     */   {
/* 107 */     this(0);
/*     */   }
/*     */ 
/*     */   public JToolBar(int paramInt)
/*     */   {
/* 119 */     this(null, paramInt);
/*     */   }
/*     */ 
/*     */   public JToolBar(String paramString)
/*     */   {
/* 131 */     this(paramString, 0);
/*     */   }
/*     */ 
/*     */   public JToolBar(String paramString, int paramInt)
/*     */   {
/* 149 */     setName(paramString);
/* 150 */     checkOrientation(paramInt);
/*     */ 
/* 152 */     this.orientation = paramInt;
/* 153 */     DefaultToolBarLayout localDefaultToolBarLayout = new DefaultToolBarLayout(paramInt);
/* 154 */     setLayout(localDefaultToolBarLayout);
/*     */ 
/* 156 */     addPropertyChangeListener(localDefaultToolBarLayout);
/*     */ 
/* 158 */     updateUI();
/*     */   }
/*     */ 
/*     */   public ToolBarUI getUI()
/*     */   {
/* 166 */     return (ToolBarUI)this.ui;
/*     */   }
/*     */ 
/*     */   public void setUI(ToolBarUI paramToolBarUI)
/*     */   {
/* 181 */     super.setUI(paramToolBarUI);
/*     */   }
/*     */ 
/*     */   public void updateUI()
/*     */   {
/* 192 */     setUI((ToolBarUI)UIManager.getUI(this));
/*     */ 
/* 196 */     if (getLayout() == null) {
/* 197 */       setLayout(new DefaultToolBarLayout(getOrientation()));
/*     */     }
/* 199 */     invalidate();
/*     */   }
/*     */ 
/*     */   public String getUIClassID()
/*     */   {
/* 212 */     return "ToolBarUI";
/*     */   }
/*     */ 
/*     */   public int getComponentIndex(Component paramComponent)
/*     */   {
/* 225 */     int i = getComponentCount();
/* 226 */     Component[] arrayOfComponent = getComponents();
/* 227 */     for (int j = 0; j < i; j++) {
/* 228 */       Component localComponent = arrayOfComponent[j];
/* 229 */       if (localComponent == paramComponent)
/* 230 */         return j;
/*     */     }
/* 232 */     return -1;
/*     */   }
/*     */ 
/*     */   public Component getComponentAtIndex(int paramInt)
/*     */   {
/* 244 */     int i = getComponentCount();
/* 245 */     if ((paramInt >= 0) && (paramInt < i)) {
/* 246 */       Component[] arrayOfComponent = getComponents();
/* 247 */       return arrayOfComponent[paramInt];
/*     */     }
/* 249 */     return null;
/*     */   }
/*     */ 
/*     */   public void setMargin(Insets paramInsets)
/*     */   {
/* 272 */     Insets localInsets = this.margin;
/* 273 */     this.margin = paramInsets;
/* 274 */     firePropertyChange("margin", localInsets, paramInsets);
/* 275 */     revalidate();
/* 276 */     repaint();
/*     */   }
/*     */ 
/*     */   public Insets getMargin()
/*     */   {
/* 288 */     if (this.margin == null) {
/* 289 */       return new Insets(0, 0, 0, 0);
/*     */     }
/* 291 */     return this.margin;
/*     */   }
/*     */ 
/*     */   public boolean isBorderPainted()
/*     */   {
/* 303 */     return this.paintBorder;
/*     */   }
/*     */ 
/*     */   public void setBorderPainted(boolean paramBoolean)
/*     */   {
/* 323 */     if (this.paintBorder != paramBoolean)
/*     */     {
/* 325 */       boolean bool = this.paintBorder;
/* 326 */       this.paintBorder = paramBoolean;
/* 327 */       firePropertyChange("borderPainted", bool, paramBoolean);
/* 328 */       revalidate();
/* 329 */       repaint();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void paintBorder(Graphics paramGraphics)
/*     */   {
/* 344 */     if (isBorderPainted())
/*     */     {
/* 346 */       super.paintBorder(paramGraphics);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isFloatable()
/*     */   {
/* 359 */     return this.floatable;
/*     */   }
/*     */ 
/*     */   public void setFloatable(boolean paramBoolean)
/*     */   {
/* 382 */     if (this.floatable != paramBoolean)
/*     */     {
/* 384 */       boolean bool = this.floatable;
/* 385 */       this.floatable = paramBoolean;
/*     */ 
/* 387 */       firePropertyChange("floatable", bool, paramBoolean);
/* 388 */       revalidate();
/* 389 */       repaint();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getOrientation()
/*     */   {
/* 403 */     return this.orientation;
/*     */   }
/*     */ 
/*     */   public void setOrientation(int paramInt)
/*     */   {
/* 426 */     checkOrientation(paramInt);
/*     */ 
/* 428 */     if (this.orientation != paramInt)
/*     */     {
/* 430 */       int i = this.orientation;
/* 431 */       this.orientation = paramInt;
/*     */ 
/* 433 */       firePropertyChange("orientation", i, paramInt);
/* 434 */       revalidate();
/* 435 */       repaint();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setRollover(boolean paramBoolean)
/*     */   {
/* 457 */     putClientProperty("JToolBar.isRollover", paramBoolean ? Boolean.TRUE : Boolean.FALSE);
/*     */   }
/*     */ 
/*     */   public boolean isRollover()
/*     */   {
/* 469 */     Boolean localBoolean = (Boolean)getClientProperty("JToolBar.isRollover");
/* 470 */     if (localBoolean != null) {
/* 471 */       return localBoolean.booleanValue();
/*     */     }
/* 473 */     return false;
/*     */   }
/*     */ 
/*     */   private void checkOrientation(int paramInt)
/*     */   {
/* 478 */     switch (paramInt)
/*     */     {
/*     */     case 0:
/*     */     case 1:
/* 482 */       break;
/*     */     default:
/* 484 */       throw new IllegalArgumentException("orientation must be one of: VERTICAL, HORIZONTAL");
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addSeparator()
/*     */   {
/* 494 */     addSeparator(null);
/*     */   }
/*     */ 
/*     */   public void addSeparator(Dimension paramDimension)
/*     */   {
/* 505 */     Separator localSeparator = new Separator(paramDimension);
/* 506 */     add(localSeparator);
/*     */   }
/*     */ 
/*     */   public JButton add(Action paramAction)
/*     */   {
/* 516 */     JButton localJButton = createActionComponent(paramAction);
/* 517 */     localJButton.setAction(paramAction);
/* 518 */     add(localJButton);
/* 519 */     return localJButton;
/*     */   }
/*     */ 
/*     */   protected JButton createActionComponent(Action paramAction)
/*     */   {
/* 533 */     JButton local1 = new JButton() {
/*     */       protected PropertyChangeListener createActionPropertyChangeListener(Action paramAnonymousAction) {
/* 535 */         PropertyChangeListener localPropertyChangeListener = JToolBar.this.createActionChangeListener(this);
/* 536 */         if (localPropertyChangeListener == null) {
/* 537 */           localPropertyChangeListener = super.createActionPropertyChangeListener(paramAnonymousAction);
/*     */         }
/* 539 */         return localPropertyChangeListener;
/*     */       }
/*     */     };
/* 542 */     if ((paramAction != null) && ((paramAction.getValue("SmallIcon") != null) || (paramAction.getValue("SwingLargeIconKey") != null)))
/*     */     {
/* 544 */       local1.setHideActionText(true);
/*     */     }
/* 546 */     local1.setHorizontalTextPosition(0);
/* 547 */     local1.setVerticalTextPosition(3);
/* 548 */     return local1;
/*     */   }
/*     */ 
/*     */   protected PropertyChangeListener createActionChangeListener(JButton paramJButton)
/*     */   {
/* 560 */     return null;
/*     */   }
/*     */ 
/*     */   protected void addImpl(Component paramComponent, Object paramObject, int paramInt)
/*     */   {
/* 573 */     if ((paramComponent instanceof Separator)) {
/* 574 */       if (getOrientation() == 1)
/* 575 */         ((Separator)paramComponent).setOrientation(0);
/*     */       else {
/* 577 */         ((Separator)paramComponent).setOrientation(1);
/*     */       }
/*     */     }
/* 580 */     super.addImpl(paramComponent, paramObject, paramInt);
/* 581 */     if ((paramComponent instanceof JButton))
/* 582 */       ((JButton)paramComponent).setDefaultCapable(false);
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 706 */     paramObjectOutputStream.defaultWriteObject();
/* 707 */     if (getUIClassID().equals("ToolBarUI")) {
/* 708 */       byte b = JComponent.getWriteObjCounter(this);
/* 709 */       b = (byte)(b - 1); JComponent.setWriteObjCounter(this, b);
/* 710 */       if ((b == 0) && (this.ui != null))
/* 711 */         this.ui.installUI(this);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected String paramString()
/*     */   {
/* 728 */     String str1 = this.paintBorder ? "true" : "false";
/*     */ 
/* 730 */     String str2 = this.margin != null ? this.margin.toString() : "";
/*     */ 
/* 732 */     String str3 = this.floatable ? "true" : "false";
/*     */ 
/* 734 */     String str4 = this.orientation == 0 ? "HORIZONTAL" : "VERTICAL";
/*     */ 
/* 737 */     return super.paramString() + ",floatable=" + str3 + ",margin=" + str2 + ",orientation=" + str4 + ",paintBorder=" + str1;
/*     */   }
/*     */ 
/*     */   public void setLayout(LayoutManager paramLayoutManager)
/*     */   {
/* 814 */     LayoutManager localLayoutManager = getLayout();
/* 815 */     if ((localLayoutManager instanceof PropertyChangeListener)) {
/* 816 */       removePropertyChangeListener((PropertyChangeListener)localLayoutManager);
/*     */     }
/* 818 */     super.setLayout(paramLayoutManager);
/*     */   }
/*     */ 
/*     */   public AccessibleContext getAccessibleContext()
/*     */   {
/* 835 */     if (this.accessibleContext == null) {
/* 836 */       this.accessibleContext = new AccessibleJToolBar();
/*     */     }
/* 838 */     return this.accessibleContext;
/*     */   }
/*     */ 
/*     */   protected class AccessibleJToolBar extends JComponent.AccessibleJComponent
/*     */   {
/*     */     protected AccessibleJToolBar()
/*     */     {
/* 846 */       super();
/*     */     }
/*     */ 
/*     */     public AccessibleStateSet getAccessibleStateSet()
/*     */     {
/* 856 */       AccessibleStateSet localAccessibleStateSet = super.getAccessibleStateSet();
/*     */ 
/* 859 */       return localAccessibleStateSet;
/*     */     }
/*     */ 
/*     */     public AccessibleRole getAccessibleRole()
/*     */     {
/* 868 */       return AccessibleRole.TOOL_BAR;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class DefaultToolBarLayout
/*     */     implements LayoutManager2, Serializable, PropertyChangeListener, UIResource
/*     */   {
/*     */     BoxLayout lm;
/*     */ 
/*     */     DefaultToolBarLayout(int arg2)
/*     */     {
/*     */       int i;
/* 751 */       if (i == 1)
/* 752 */         this.lm = new BoxLayout(JToolBar.this, 3);
/*     */       else
/* 754 */         this.lm = new BoxLayout(JToolBar.this, 2);
/*     */     }
/*     */ 
/*     */     public void addLayoutComponent(String paramString, Component paramComponent)
/*     */     {
/* 759 */       this.lm.addLayoutComponent(paramString, paramComponent);
/*     */     }
/*     */ 
/*     */     public void addLayoutComponent(Component paramComponent, Object paramObject) {
/* 763 */       this.lm.addLayoutComponent(paramComponent, paramObject);
/*     */     }
/*     */ 
/*     */     public void removeLayoutComponent(Component paramComponent) {
/* 767 */       this.lm.removeLayoutComponent(paramComponent);
/*     */     }
/*     */ 
/*     */     public Dimension preferredLayoutSize(Container paramContainer) {
/* 771 */       return this.lm.preferredLayoutSize(paramContainer);
/*     */     }
/*     */ 
/*     */     public Dimension minimumLayoutSize(Container paramContainer) {
/* 775 */       return this.lm.minimumLayoutSize(paramContainer);
/*     */     }
/*     */ 
/*     */     public Dimension maximumLayoutSize(Container paramContainer) {
/* 779 */       return this.lm.maximumLayoutSize(paramContainer);
/*     */     }
/*     */ 
/*     */     public void layoutContainer(Container paramContainer) {
/* 783 */       this.lm.layoutContainer(paramContainer);
/*     */     }
/*     */ 
/*     */     public float getLayoutAlignmentX(Container paramContainer) {
/* 787 */       return this.lm.getLayoutAlignmentX(paramContainer);
/*     */     }
/*     */ 
/*     */     public float getLayoutAlignmentY(Container paramContainer) {
/* 791 */       return this.lm.getLayoutAlignmentY(paramContainer);
/*     */     }
/*     */ 
/*     */     public void invalidateLayout(Container paramContainer) {
/* 795 */       this.lm.invalidateLayout(paramContainer);
/*     */     }
/*     */ 
/*     */     public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 799 */       String str = paramPropertyChangeEvent.getPropertyName();
/* 800 */       if (str.equals("orientation")) {
/* 801 */         int i = ((Integer)paramPropertyChangeEvent.getNewValue()).intValue();
/*     */ 
/* 803 */         if (i == 1)
/* 804 */           this.lm = new BoxLayout(JToolBar.this, 3);
/*     */         else
/* 806 */           this.lm = new BoxLayout(JToolBar.this, 2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static class Separator extends JSeparator
/*     */   {
/*     */     private Dimension separatorSize;
/*     */ 
/*     */     public Separator()
/*     */     {
/* 601 */       this(null);
/*     */     }
/*     */ 
/*     */     public Separator(Dimension paramDimension)
/*     */     {
/* 611 */       super();
/* 612 */       setSeparatorSize(paramDimension);
/*     */     }
/*     */ 
/*     */     public String getUIClassID()
/*     */     {
/* 624 */       return "ToolBarSeparatorUI";
/*     */     }
/*     */ 
/*     */     public void setSeparatorSize(Dimension paramDimension)
/*     */     {
/* 634 */       if (paramDimension != null)
/* 635 */         this.separatorSize = paramDimension;
/*     */       else {
/* 637 */         super.updateUI();
/*     */       }
/* 639 */       invalidate();
/*     */     }
/*     */ 
/*     */     public Dimension getSeparatorSize()
/*     */     {
/* 650 */       return this.separatorSize;
/*     */     }
/*     */ 
/*     */     public Dimension getMinimumSize()
/*     */     {
/* 661 */       if (this.separatorSize != null) {
/* 662 */         return this.separatorSize.getSize();
/*     */       }
/* 664 */       return super.getMinimumSize();
/*     */     }
/*     */ 
/*     */     public Dimension getMaximumSize()
/*     */     {
/* 676 */       if (this.separatorSize != null) {
/* 677 */         return this.separatorSize.getSize();
/*     */       }
/* 679 */       return super.getMaximumSize();
/*     */     }
/*     */ 
/*     */     public Dimension getPreferredSize()
/*     */     {
/* 691 */       if (this.separatorSize != null) {
/* 692 */         return this.separatorSize.getSize();
/*     */       }
/* 694 */       return super.getPreferredSize();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.JToolBar
 * JD-Core Version:    0.6.2
 */