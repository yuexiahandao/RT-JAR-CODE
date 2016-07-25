/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.FlowLayout;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.ComponentListener;
/*     */ import java.awt.event.ContainerEvent;
/*     */ import java.awt.event.ContainerListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyVetoException;
/*     */ import javax.swing.DefaultDesktopManager;
/*     */ import javax.swing.DesktopManager;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JDesktopPane;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.JInternalFrame.JDesktopIcon;
/*     */ import javax.swing.JLayeredPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.BevelBorder;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicDesktopPaneUI;
/*     */ 
/*     */ public class SynthDesktopPaneUI extends BasicDesktopPaneUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*     */   private TaskBar taskBar;
/*     */   private DesktopManager oldDesktopManager;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  57 */     return new SynthDesktopPaneUI();
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/*  65 */     super.installListeners();
/*  66 */     this.desktop.addPropertyChangeListener(this);
/*  67 */     if (this.taskBar != null)
/*     */     {
/*  69 */       this.desktop.addComponentListener(this.taskBar);
/*     */ 
/*  71 */       this.desktop.addContainerListener(this.taskBar);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  80 */     updateStyle(this.desktop);
/*     */ 
/*  82 */     if (UIManager.getBoolean("InternalFrame.useTaskBar")) {
/*  83 */       this.taskBar = new TaskBar();
/*     */ 
/*  85 */       for (Component localComponent : this.desktop.getComponents())
/*     */       {
/*     */         JInternalFrame.JDesktopIcon localJDesktopIcon;
/*  88 */         if ((localComponent instanceof JInternalFrame.JDesktopIcon)) {
/*  89 */           localJDesktopIcon = (JInternalFrame.JDesktopIcon)localComponent; } else {
/*  90 */           if (!(localComponent instanceof JInternalFrame)) continue;
/*  91 */           localJDesktopIcon = ((JInternalFrame)localComponent).getDesktopIcon();
/*     */         }
/*     */ 
/*  96 */         if (localJDesktopIcon.getParent() == this.desktop) {
/*  97 */           this.desktop.remove(localJDesktopIcon);
/*     */         }
/*  99 */         if (localJDesktopIcon.getParent() != this.taskBar) {
/* 100 */           this.taskBar.add(localJDesktopIcon);
/* 101 */           localJDesktopIcon.getInternalFrame().addComponentListener(this.taskBar);
/*     */         }
/*     */       }
/*     */ 
/* 105 */       this.taskBar.setBackground(this.desktop.getBackground());
/* 106 */       this.desktop.add(this.taskBar, Integer.valueOf(JLayeredPane.PALETTE_LAYER.intValue() + 1));
/*     */ 
/* 108 */       if (this.desktop.isShowing())
/* 109 */         this.taskBar.adjustSize();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void updateStyle(JDesktopPane paramJDesktopPane)
/*     */   {
/* 115 */     SynthStyle localSynthStyle = this.style;
/* 116 */     SynthContext localSynthContext = getContext(paramJDesktopPane, 1);
/* 117 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/* 118 */     if (localSynthStyle != null) {
/* 119 */       uninstallKeyboardActions();
/* 120 */       installKeyboardActions();
/*     */     }
/* 122 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 130 */     if (this.taskBar != null) {
/* 131 */       this.desktop.removeComponentListener(this.taskBar);
/* 132 */       this.desktop.removeContainerListener(this.taskBar);
/*     */     }
/* 134 */     this.desktop.removePropertyChangeListener(this);
/* 135 */     super.uninstallListeners();
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 143 */     SynthContext localSynthContext = getContext(this.desktop, 1);
/*     */ 
/* 145 */     this.style.uninstallDefaults(localSynthContext);
/* 146 */     localSynthContext.dispose();
/* 147 */     this.style = null;
/*     */ 
/* 149 */     if (this.taskBar != null) {
/* 150 */       for (Component localComponent : this.taskBar.getComponents()) {
/* 151 */         JInternalFrame.JDesktopIcon localJDesktopIcon = (JInternalFrame.JDesktopIcon)localComponent;
/*     */ 
/* 153 */         this.taskBar.remove(localJDesktopIcon);
/* 154 */         localJDesktopIcon.setPreferredSize(null);
/* 155 */         JInternalFrame localJInternalFrame = localJDesktopIcon.getInternalFrame();
/* 156 */         if (localJInternalFrame.isIcon()) {
/* 157 */           this.desktop.add(localJDesktopIcon);
/*     */         }
/* 159 */         localJInternalFrame.removeComponentListener(this.taskBar);
/*     */       }
/* 161 */       this.desktop.remove(this.taskBar);
/* 162 */       this.taskBar = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void installDesktopManager()
/*     */   {
/* 171 */     if (UIManager.getBoolean("InternalFrame.useTaskBar")) {
/* 172 */       this.desktopManager = (this.oldDesktopManager = this.desktop.getDesktopManager());
/* 173 */       if (!(this.desktopManager instanceof SynthDesktopManager)) {
/* 174 */         this.desktopManager = new SynthDesktopManager();
/* 175 */         this.desktop.setDesktopManager(this.desktopManager);
/*     */       }
/*     */     } else {
/* 178 */       super.installDesktopManager();
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void uninstallDesktopManager()
/*     */   {
/* 187 */     if ((this.oldDesktopManager != null) && (!(this.oldDesktopManager instanceof UIResource))) {
/* 188 */       this.desktopManager = this.desktop.getDesktopManager();
/* 189 */       if ((this.desktopManager == null) || ((this.desktopManager instanceof UIResource))) {
/* 190 */         this.desktop.setDesktopManager(this.oldDesktopManager);
/*     */       }
/*     */     }
/* 193 */     this.oldDesktopManager = null;
/* 194 */     super.uninstallDesktopManager();
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 429 */     return getContext(paramJComponent, getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 433 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent)
/*     */   {
/* 438 */     return SynthLookAndFeel.getComponentState(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 455 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 457 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 458 */     localSynthContext.getPainter().paintDesktopPaneBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 460 */     paint(localSynthContext, paramGraphics);
/* 461 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 475 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 477 */     paint(localSynthContext, paramGraphics);
/* 478 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 497 */     paramSynthContext.getPainter().paintDesktopPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 505 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent)) {
/* 506 */       updateStyle((JDesktopPane)paramPropertyChangeEvent.getSource());
/*     */     }
/* 508 */     if ((paramPropertyChangeEvent.getPropertyName() == "ancestor") && (this.taskBar != null))
/* 509 */       this.taskBar.adjustSize();
/*     */   }
/*     */ 
/*     */   class SynthDesktopManager extends DefaultDesktopManager
/*     */     implements UIResource
/*     */   {
/*     */     SynthDesktopManager()
/*     */     {
/*     */     }
/*     */ 
/*     */     public void maximizeFrame(JInternalFrame paramJInternalFrame)
/*     */     {
/* 328 */       if (paramJInternalFrame.isIcon()) {
/*     */         try {
/* 330 */           paramJInternalFrame.setIcon(false);
/*     */         } catch (PropertyVetoException localPropertyVetoException1) {
/*     */         }
/*     */       } else {
/* 334 */         paramJInternalFrame.setNormalBounds(paramJInternalFrame.getBounds());
/* 335 */         Container localContainer = paramJInternalFrame.getParent();
/* 336 */         setBoundsForFrame(paramJInternalFrame, 0, 0, localContainer.getWidth(), localContainer.getHeight() - SynthDesktopPaneUI.this.taskBar.getHeight());
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 342 */         paramJInternalFrame.setSelected(true);
/*     */       }
/*     */       catch (PropertyVetoException localPropertyVetoException2) {
/*     */       }
/*     */     }
/*     */ 
/*     */     public void iconifyFrame(JInternalFrame paramJInternalFrame) {
/* 349 */       Container localContainer = paramJInternalFrame.getParent();
/* 350 */       JDesktopPane localJDesktopPane = paramJInternalFrame.getDesktopPane();
/* 351 */       boolean bool = paramJInternalFrame.isSelected();
/*     */ 
/* 353 */       if (localContainer == null) {
/* 354 */         return;
/*     */       }
/*     */ 
/* 357 */       JInternalFrame.JDesktopIcon localJDesktopIcon = paramJInternalFrame.getDesktopIcon();
/*     */ 
/* 359 */       if (!paramJInternalFrame.isMaximum()) {
/* 360 */         paramJInternalFrame.setNormalBounds(paramJInternalFrame.getBounds());
/*     */       }
/* 362 */       localContainer.remove(paramJInternalFrame);
/* 363 */       localContainer.repaint(paramJInternalFrame.getX(), paramJInternalFrame.getY(), paramJInternalFrame.getWidth(), paramJInternalFrame.getHeight());
/*     */       try {
/* 365 */         paramJInternalFrame.setSelected(false);
/*     */       }
/*     */       catch (PropertyVetoException localPropertyVetoException1)
/*     */       {
/*     */       }
/* 370 */       if (bool)
/* 371 */         for (Component localComponent : localContainer.getComponents())
/* 372 */           if ((localComponent instanceof JInternalFrame)) {
/*     */             try {
/* 374 */               ((JInternalFrame)localComponent).setSelected(true);
/*     */             } catch (PropertyVetoException localPropertyVetoException2) {
/*     */             }
/* 377 */             ((JInternalFrame)localComponent).moveToFront();
/* 378 */             return;
/*     */           }
/*     */     }
/*     */ 
/*     */     public void deiconifyFrame(JInternalFrame paramJInternalFrame)
/*     */     {
/* 386 */       JInternalFrame.JDesktopIcon localJDesktopIcon = paramJInternalFrame.getDesktopIcon();
/* 387 */       Container localContainer = localJDesktopIcon.getParent();
/* 388 */       if (localContainer != null) {
/* 389 */         localContainer = localContainer.getParent();
/* 390 */         if (localContainer != null) {
/* 391 */           localContainer.add(paramJInternalFrame);
/* 392 */           if (paramJInternalFrame.isMaximum()) {
/* 393 */             int i = localContainer.getWidth();
/* 394 */             int j = localContainer.getHeight() - SynthDesktopPaneUI.this.taskBar.getHeight();
/* 395 */             if ((paramJInternalFrame.getWidth() != i) || (paramJInternalFrame.getHeight() != j)) {
/* 396 */               setBoundsForFrame(paramJInternalFrame, 0, 0, i, j);
/*     */             }
/*     */           }
/* 399 */           if (paramJInternalFrame.isSelected())
/* 400 */             paramJInternalFrame.moveToFront();
/*     */           else
/*     */             try {
/* 403 */               paramJInternalFrame.setSelected(true);
/*     */             }
/*     */             catch (PropertyVetoException localPropertyVetoException) {
/*     */             }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     protected void removeIconFor(JInternalFrame paramJInternalFrame) {
/* 412 */       super.removeIconFor(paramJInternalFrame);
/* 413 */       SynthDesktopPaneUI.this.taskBar.validate();
/*     */     }
/*     */ 
/*     */     public void setBoundsForFrame(JComponent paramJComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 417 */       super.setBoundsForFrame(paramJComponent, paramInt1, paramInt2, paramInt3, paramInt4);
/* 418 */       if ((SynthDesktopPaneUI.this.taskBar != null) && (paramInt2 >= SynthDesktopPaneUI.this.taskBar.getY()))
/* 419 */         paramJComponent.setLocation(paramJComponent.getX(), SynthDesktopPaneUI.this.taskBar.getY() - paramJComponent.getInsets().top);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class TaskBar extends JPanel
/*     */     implements ComponentListener, ContainerListener
/*     */   {
/*     */     TaskBar()
/*     */     {
/* 199 */       setOpaque(true);
/* 200 */       setLayout(new FlowLayout(0, 0, 0)
/*     */       {
/*     */         public void layoutContainer(Container paramAnonymousContainer) {
/* 203 */           Component[] arrayOfComponent1 = paramAnonymousContainer.getComponents();
/* 204 */           int i = arrayOfComponent1.length;
/* 205 */           if (i > 0)
/*     */           {
/* 207 */             int j = 0;
/*     */             Component[] arrayOfComponent2;
/* 208 */             for (arrayOfComponent2 : arrayOfComponent1) {
/* 209 */               arrayOfComponent2.setPreferredSize(null);
/* 210 */               Dimension localDimension1 = arrayOfComponent2.getPreferredSize();
/* 211 */               if (localDimension1.width > j) {
/* 212 */                 j = localDimension1.width;
/*     */               }
/*     */             }
/*     */ 
/* 216 */             ??? = paramAnonymousContainer.getInsets();
/* 217 */             ??? = paramAnonymousContainer.getWidth() - ((Insets)???).left - ((Insets)???).right;
/* 218 */             ??? = Math.min(j, Math.max(10, ??? / i));
/* 219 */             for (Component localComponent : arrayOfComponent1) {
/* 220 */               Dimension localDimension2 = localComponent.getPreferredSize();
/* 221 */               localComponent.setPreferredSize(new Dimension(???, localDimension2.height));
/*     */             }
/*     */           }
/* 224 */           super.layoutContainer(paramAnonymousContainer);
/*     */         }
/*     */       });
/* 229 */       setBorder(new BevelBorder(0)
/*     */       {
/*     */         protected void paintRaisedBevel(Component paramAnonymousComponent, Graphics paramAnonymousGraphics, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3, int paramAnonymousInt4) {
/* 232 */           Color localColor = paramAnonymousGraphics.getColor();
/* 233 */           paramAnonymousGraphics.translate(paramAnonymousInt1, paramAnonymousInt2);
/* 234 */           paramAnonymousGraphics.setColor(getHighlightOuterColor(paramAnonymousComponent));
/* 235 */           paramAnonymousGraphics.drawLine(0, 0, 0, paramAnonymousInt4 - 2);
/* 236 */           paramAnonymousGraphics.drawLine(1, 0, paramAnonymousInt3 - 2, 0);
/* 237 */           paramAnonymousGraphics.setColor(getShadowOuterColor(paramAnonymousComponent));
/* 238 */           paramAnonymousGraphics.drawLine(0, paramAnonymousInt4 - 1, paramAnonymousInt3 - 1, paramAnonymousInt4 - 1);
/* 239 */           paramAnonymousGraphics.drawLine(paramAnonymousInt3 - 1, 0, paramAnonymousInt3 - 1, paramAnonymousInt4 - 2);
/* 240 */           paramAnonymousGraphics.translate(-paramAnonymousInt1, -paramAnonymousInt2);
/* 241 */           paramAnonymousGraphics.setColor(localColor);
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/*     */     void adjustSize() {
/* 247 */       JDesktopPane localJDesktopPane = (JDesktopPane)getParent();
/* 248 */       if (localJDesktopPane != null) {
/* 249 */         int i = getPreferredSize().height;
/* 250 */         Insets localInsets = getInsets();
/* 251 */         if (i == localInsets.top + localInsets.bottom) {
/* 252 */           if (getHeight() <= i)
/*     */           {
/* 254 */             i += 21;
/*     */           }
/*     */           else {
/* 257 */             i = getHeight();
/*     */           }
/*     */         }
/* 260 */         setBounds(0, localJDesktopPane.getHeight() - i, localJDesktopPane.getWidth(), i);
/* 261 */         revalidate();
/* 262 */         repaint();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void componentResized(ComponentEvent paramComponentEvent)
/*     */     {
/* 269 */       if ((paramComponentEvent.getSource() instanceof JDesktopPane))
/* 270 */         adjustSize();
/*     */     }
/*     */ 
/*     */     public void componentMoved(ComponentEvent paramComponentEvent) {
/*     */     }
/*     */ 
/*     */     public void componentShown(ComponentEvent paramComponentEvent) {
/* 277 */       if ((paramComponentEvent.getSource() instanceof JInternalFrame))
/* 278 */         adjustSize();
/*     */     }
/*     */ 
/*     */     public void componentHidden(ComponentEvent paramComponentEvent)
/*     */     {
/* 283 */       if ((paramComponentEvent.getSource() instanceof JInternalFrame)) {
/* 284 */         ((JInternalFrame)paramComponentEvent.getSource()).getDesktopIcon().setVisible(false);
/* 285 */         revalidate();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void componentAdded(ContainerEvent paramContainerEvent)
/*     */     {
/* 292 */       if ((paramContainerEvent.getChild() instanceof JInternalFrame)) {
/* 293 */         JDesktopPane localJDesktopPane = (JDesktopPane)paramContainerEvent.getSource();
/* 294 */         JInternalFrame localJInternalFrame = (JInternalFrame)paramContainerEvent.getChild();
/* 295 */         JInternalFrame.JDesktopIcon localJDesktopIcon = localJInternalFrame.getDesktopIcon();
/* 296 */         for (Component localComponent : getComponents()) {
/* 297 */           if (localComponent == localJDesktopIcon)
/*     */           {
/* 299 */             return;
/*     */           }
/*     */         }
/* 302 */         add(localJDesktopIcon);
/* 303 */         localJInternalFrame.addComponentListener(this);
/* 304 */         if (getComponentCount() == 1)
/* 305 */           adjustSize();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void componentRemoved(ContainerEvent paramContainerEvent)
/*     */     {
/* 311 */       if ((paramContainerEvent.getChild() instanceof JInternalFrame)) {
/* 312 */         JInternalFrame localJInternalFrame = (JInternalFrame)paramContainerEvent.getChild();
/* 313 */         if (!localJInternalFrame.isIcon())
/*     */         {
/* 315 */           remove(localJInternalFrame.getDesktopIcon());
/* 316 */           localJInternalFrame.removeComponentListener(this);
/* 317 */           revalidate();
/* 318 */           repaint();
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthDesktopPaneUI
 * JD-Core Version:    0.6.2
 */