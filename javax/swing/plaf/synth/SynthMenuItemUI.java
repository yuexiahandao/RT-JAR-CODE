/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.LookAndFeel;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicMenuItemUI;
/*     */ import sun.swing.MenuItemLayoutHelper;
/*     */ 
/*     */ public class SynthMenuItemUI extends BasicMenuItemUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*     */   private SynthStyle accStyle;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  58 */     return new SynthMenuItemUI();
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/*  66 */     super.uninstallUI(paramJComponent);
/*     */ 
/*  68 */     JComponent localJComponent = MenuItemLayoutHelper.getMenuItemParent((JMenuItem)paramJComponent);
/*  69 */     if (localJComponent != null)
/*  70 */       localJComponent.putClientProperty(SynthMenuItemLayoutHelper.MAX_ACC_OR_ARROW_WIDTH, null);
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  80 */     updateStyle(this.menuItem);
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/*  88 */     super.installListeners();
/*  89 */     this.menuItem.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JMenuItem paramJMenuItem) {
/*  93 */     SynthContext localSynthContext = getContext(paramJMenuItem, 1);
/*  94 */     SynthStyle localSynthStyle = this.style;
/*     */ 
/*  96 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*  97 */     if (localSynthStyle != this.style) {
/*  98 */       localObject1 = getPropertyPrefix();
/*     */ 
/* 100 */       Object localObject2 = this.style.get(localSynthContext, (String)localObject1 + ".textIconGap");
/* 101 */       if (localObject2 != null) {
/* 102 */         LookAndFeel.installProperty(paramJMenuItem, "iconTextGap", localObject2);
/*     */       }
/* 104 */       this.defaultTextIconGap = paramJMenuItem.getIconTextGap();
/*     */ 
/* 106 */       if ((this.menuItem.getMargin() == null) || ((this.menuItem.getMargin() instanceof UIResource)))
/*     */       {
/* 108 */         Insets localInsets = (Insets)this.style.get(localSynthContext, (String)localObject1 + ".margin");
/*     */ 
/* 110 */         if (localInsets == null)
/*     */         {
/* 112 */           localInsets = SynthLookAndFeel.EMPTY_UIRESOURCE_INSETS;
/*     */         }
/* 114 */         this.menuItem.setMargin(localInsets);
/*     */       }
/* 116 */       this.acceleratorDelimiter = this.style.getString(localSynthContext, (String)localObject1 + ".acceleratorDelimiter", "+");
/*     */ 
/* 119 */       this.arrowIcon = this.style.getIcon(localSynthContext, (String)localObject1 + ".arrowIcon");
/*     */ 
/* 121 */       this.checkIcon = this.style.getIcon(localSynthContext, (String)localObject1 + ".checkIcon");
/* 122 */       if (localSynthStyle != null) {
/* 123 */         uninstallKeyboardActions();
/* 124 */         installKeyboardActions();
/*     */       }
/*     */     }
/* 127 */     localSynthContext.dispose();
/*     */ 
/* 129 */     Object localObject1 = getContext(paramJMenuItem, Region.MENU_ITEM_ACCELERATOR, 1);
/*     */ 
/* 132 */     this.accStyle = SynthLookAndFeel.updateStyle((SynthContext)localObject1, this);
/* 133 */     ((SynthContext)localObject1).dispose();
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 141 */     SynthContext localSynthContext1 = getContext(this.menuItem, 1);
/* 142 */     this.style.uninstallDefaults(localSynthContext1);
/* 143 */     localSynthContext1.dispose();
/* 144 */     this.style = null;
/*     */ 
/* 146 */     SynthContext localSynthContext2 = getContext(this.menuItem, Region.MENU_ITEM_ACCELERATOR, 1);
/*     */ 
/* 148 */     this.accStyle.uninstallDefaults(localSynthContext2);
/* 149 */     localSynthContext2.dispose();
/* 150 */     this.accStyle = null;
/*     */ 
/* 152 */     super.uninstallDefaults();
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 160 */     super.uninstallListeners();
/* 161 */     this.menuItem.removePropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 169 */     return getContext(paramJComponent, getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 173 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   SynthContext getContext(JComponent paramJComponent, Region paramRegion)
/*     */   {
/* 178 */     return getContext(paramJComponent, paramRegion, getComponentState(paramJComponent, paramRegion));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, Region paramRegion, int paramInt) {
/* 182 */     return SynthContext.getContext(SynthContext.class, paramJComponent, paramRegion, this.accStyle, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent)
/*     */   {
/*     */     int i;
/* 189 */     if (!paramJComponent.isEnabled()) {
/* 190 */       i = 8;
/*     */     }
/* 192 */     else if (this.menuItem.isArmed()) {
/* 193 */       i = 2;
/*     */     }
/*     */     else {
/* 196 */       i = SynthLookAndFeel.getComponentState(paramJComponent);
/*     */     }
/* 198 */     if (this.menuItem.isSelected()) {
/* 199 */       i |= 512;
/*     */     }
/* 201 */     return i;
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent, Region paramRegion) {
/* 205 */     return getComponentState(paramJComponent);
/*     */   }
/*     */ 
/*     */   protected Dimension getPreferredMenuItemSize(JComponent paramJComponent, Icon paramIcon1, Icon paramIcon2, int paramInt)
/*     */   {
/* 216 */     SynthContext localSynthContext1 = getContext(paramJComponent);
/* 217 */     SynthContext localSynthContext2 = getContext(paramJComponent, Region.MENU_ITEM_ACCELERATOR);
/* 218 */     Dimension localDimension = SynthGraphicsUtils.getPreferredMenuItemSize(localSynthContext1, localSynthContext2, paramJComponent, paramIcon1, paramIcon2, paramInt, this.acceleratorDelimiter, MenuItemLayoutHelper.useCheckAndArrow(this.menuItem), getPropertyPrefix());
/*     */ 
/* 223 */     localSynthContext1.dispose();
/* 224 */     localSynthContext2.dispose();
/* 225 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 243 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 245 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 246 */     paintBackground(localSynthContext, paramGraphics, paramJComponent);
/* 247 */     paint(localSynthContext, paramGraphics);
/* 248 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 262 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 264 */     paint(localSynthContext, paramGraphics);
/* 265 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/* 276 */     SynthContext localSynthContext = getContext(this.menuItem, Region.MENU_ITEM_ACCELERATOR);
/*     */ 
/* 280 */     String str = getPropertyPrefix();
/* 281 */     Icon localIcon1 = this.style.getIcon(paramSynthContext, str + ".checkIcon");
/* 282 */     Icon localIcon2 = this.style.getIcon(paramSynthContext, str + ".arrowIcon");
/* 283 */     SynthGraphicsUtils.paint(paramSynthContext, localSynthContext, paramGraphics, localIcon1, localIcon2, this.acceleratorDelimiter, this.defaultTextIconGap, getPropertyPrefix());
/*     */ 
/* 285 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   void paintBackground(SynthContext paramSynthContext, Graphics paramGraphics, JComponent paramJComponent) {
/* 289 */     SynthGraphicsUtils.paintBackground(paramSynthContext, paramGraphics, paramJComponent);
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 298 */     paramSynthContext.getPainter().paintMenuItemBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 306 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/* 307 */       updateStyle((JMenuItem)paramPropertyChangeEvent.getSource());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthMenuItemUI
 * JD-Core Version:    0.6.2
 */