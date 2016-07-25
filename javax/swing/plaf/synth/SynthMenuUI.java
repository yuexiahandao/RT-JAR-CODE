/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JMenu;
/*     */ import javax.swing.JMenuItem;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicMenuUI;
/*     */ import sun.swing.MenuItemLayoutHelper;
/*     */ 
/*     */ public class SynthMenuUI extends BasicMenuUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*     */   private SynthStyle accStyle;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  55 */     return new SynthMenuUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  63 */     updateStyle(this.menuItem);
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/*  71 */     super.installListeners();
/*  72 */     this.menuItem.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JMenuItem paramJMenuItem) {
/*  76 */     SynthStyle localSynthStyle = this.style;
/*  77 */     SynthContext localSynthContext = getContext(paramJMenuItem, 1);
/*     */ 
/*  79 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*  80 */     if (localSynthStyle != this.style) {
/*  81 */       localObject = getPropertyPrefix();
/*  82 */       this.defaultTextIconGap = this.style.getInt(localSynthContext, (String)localObject + ".textIconGap", 4);
/*     */ 
/*  84 */       if ((this.menuItem.getMargin() == null) || ((this.menuItem.getMargin() instanceof UIResource)))
/*     */       {
/*  86 */         Insets localInsets = (Insets)this.style.get(localSynthContext, (String)localObject + ".margin");
/*     */ 
/*  88 */         if (localInsets == null)
/*     */         {
/*  90 */           localInsets = SynthLookAndFeel.EMPTY_UIRESOURCE_INSETS;
/*     */         }
/*  92 */         this.menuItem.setMargin(localInsets);
/*     */       }
/*  94 */       this.acceleratorDelimiter = this.style.getString(localSynthContext, (String)localObject + ".acceleratorDelimiter", "+");
/*     */ 
/*  97 */       if (MenuItemLayoutHelper.useCheckAndArrow(this.menuItem)) {
/*  98 */         this.checkIcon = this.style.getIcon(localSynthContext, (String)localObject + ".checkIcon");
/*  99 */         this.arrowIcon = this.style.getIcon(localSynthContext, (String)localObject + ".arrowIcon");
/*     */       }
/*     */       else {
/* 102 */         this.checkIcon = null;
/* 103 */         this.arrowIcon = null;
/*     */       }
/*     */ 
/* 106 */       ((JMenu)this.menuItem).setDelay(this.style.getInt(localSynthContext, (String)localObject + ".delay", 200));
/*     */ 
/* 108 */       if (localSynthStyle != null) {
/* 109 */         uninstallKeyboardActions();
/* 110 */         installKeyboardActions();
/*     */       }
/*     */     }
/* 113 */     localSynthContext.dispose();
/*     */ 
/* 115 */     Object localObject = getContext(paramJMenuItem, Region.MENU_ITEM_ACCELERATOR, 1);
/*     */ 
/* 118 */     this.accStyle = SynthLookAndFeel.updateStyle((SynthContext)localObject, this);
/* 119 */     ((SynthContext)localObject).dispose();
/*     */   }
/*     */ 
/*     */   public void uninstallUI(JComponent paramJComponent)
/*     */   {
/* 127 */     super.uninstallUI(paramJComponent);
/*     */ 
/* 129 */     JComponent localJComponent = MenuItemLayoutHelper.getMenuItemParent((JMenuItem)paramJComponent);
/* 130 */     if (localJComponent != null)
/* 131 */       localJComponent.putClientProperty(SynthMenuItemLayoutHelper.MAX_ACC_OR_ARROW_WIDTH, null);
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
/* 173 */     Region localRegion = SynthLookAndFeel.getRegion(paramJComponent);
/* 174 */     return SynthContext.getContext(SynthContext.class, paramJComponent, localRegion, this.style, paramInt);
/*     */   }
/*     */ 
/*     */   SynthContext getContext(JComponent paramJComponent, Region paramRegion)
/*     */   {
/* 179 */     return getContext(paramJComponent, paramRegion, getComponentState(paramJComponent, paramRegion));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, Region paramRegion, int paramInt) {
/* 183 */     return SynthContext.getContext(SynthContext.class, paramJComponent, paramRegion, this.accStyle, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent)
/*     */   {
/* 190 */     if (!paramJComponent.isEnabled())
/* 191 */       return 8;
/*     */     int i;
/* 193 */     if (this.menuItem.isArmed()) {
/* 194 */       i = 2;
/*     */     }
/*     */     else {
/* 197 */       i = SynthLookAndFeel.getComponentState(paramJComponent);
/*     */     }
/* 199 */     if (this.menuItem.isSelected()) {
/* 200 */       i |= 512;
/*     */     }
/* 202 */     return i;
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent, Region paramRegion) {
/* 206 */     return getComponentState(paramJComponent);
/*     */   }
/*     */ 
/*     */   protected Dimension getPreferredMenuItemSize(JComponent paramJComponent, Icon paramIcon1, Icon paramIcon2, int paramInt)
/*     */   {
/* 217 */     SynthContext localSynthContext1 = getContext(paramJComponent);
/* 218 */     SynthContext localSynthContext2 = getContext(paramJComponent, Region.MENU_ITEM_ACCELERATOR);
/* 219 */     Dimension localDimension = SynthGraphicsUtils.getPreferredMenuItemSize(localSynthContext1, localSynthContext2, paramJComponent, paramIcon1, paramIcon2, paramInt, this.acceleratorDelimiter, MenuItemLayoutHelper.useCheckAndArrow(this.menuItem), getPropertyPrefix());
/*     */ 
/* 224 */     localSynthContext1.dispose();
/* 225 */     localSynthContext2.dispose();
/* 226 */     return localDimension;
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 243 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 245 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 246 */     localSynthContext.getPainter().paintMenuBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 248 */     paint(localSynthContext, paramGraphics);
/* 249 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 263 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 265 */     paint(localSynthContext, paramGraphics);
/* 266 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/* 277 */     SynthContext localSynthContext = getContext(this.menuItem, Region.MENU_ITEM_ACCELERATOR);
/*     */ 
/* 280 */     String str = getPropertyPrefix();
/* 281 */     Icon localIcon1 = this.style.getIcon(paramSynthContext, str + ".checkIcon");
/* 282 */     Icon localIcon2 = this.style.getIcon(paramSynthContext, str + ".arrowIcon");
/* 283 */     SynthGraphicsUtils.paint(paramSynthContext, localSynthContext, paramGraphics, localIcon1, localIcon2, this.acceleratorDelimiter, this.defaultTextIconGap, getPropertyPrefix());
/*     */ 
/* 285 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 294 */     paramSynthContext.getPainter().paintMenuBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 302 */     if ((SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent)) || ((paramPropertyChangeEvent.getPropertyName().equals("ancestor")) && (UIManager.getBoolean("Menu.useMenuBarForTopLevelMenus"))))
/*     */     {
/* 304 */       updateStyle((JMenu)paramPropertyChangeEvent.getSource());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthMenuUI
 * JD-Core Version:    0.6.2
 */