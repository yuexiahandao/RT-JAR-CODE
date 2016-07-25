/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.event.ComponentEvent;
/*     */ import java.awt.event.ComponentListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.Icon;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JDesktopPane;
/*     */ import javax.swing.JInternalFrame;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameTitlePane;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameUI;
/*     */ import javax.swing.plaf.basic.BasicInternalFrameUI.ComponentHandler;
/*     */ 
/*     */ public class SynthInternalFrameUI extends BasicInternalFrameUI
/*     */   implements SynthUI, PropertyChangeListener
/*     */ {
/*     */   private SynthStyle style;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  56 */     return new SynthInternalFrameUI((JInternalFrame)paramJComponent);
/*     */   }
/*     */ 
/*     */   protected SynthInternalFrameUI(JInternalFrame paramJInternalFrame) {
/*  60 */     super(paramJInternalFrame);
/*     */   }
/*     */ 
/*     */   public void installDefaults()
/*     */   {
/*  68 */     this.frame.setLayout(this.internalFrameLayout = createLayoutManager());
/*  69 */     updateStyle(this.frame);
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/*  77 */     super.installListeners();
/*  78 */     this.frame.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void uninstallComponents()
/*     */   {
/*  86 */     if ((this.frame.getComponentPopupMenu() instanceof UIResource)) {
/*  87 */       this.frame.setComponentPopupMenu(null);
/*     */     }
/*  89 */     super.uninstallComponents();
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/*  97 */     this.frame.removePropertyChangeListener(this);
/*  98 */     super.uninstallListeners();
/*     */   }
/*     */ 
/*     */   private void updateStyle(JComponent paramJComponent) {
/* 102 */     SynthContext localSynthContext = getContext(paramJComponent, 1);
/* 103 */     SynthStyle localSynthStyle = this.style;
/*     */ 
/* 105 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/* 106 */     if (this.style != localSynthStyle) {
/* 107 */       Icon localIcon = this.frame.getFrameIcon();
/* 108 */       if ((localIcon == null) || ((localIcon instanceof UIResource))) {
/* 109 */         this.frame.setFrameIcon(localSynthContext.getStyle().getIcon(localSynthContext, "InternalFrame.icon"));
/*     */       }
/*     */ 
/* 112 */       if (localSynthStyle != null) {
/* 113 */         uninstallKeyboardActions();
/* 114 */         installKeyboardActions();
/*     */       }
/*     */     }
/* 117 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 125 */     SynthContext localSynthContext = getContext(this.frame, 1);
/* 126 */     this.style.uninstallDefaults(localSynthContext);
/* 127 */     localSynthContext.dispose();
/* 128 */     this.style = null;
/* 129 */     if (this.frame.getLayout() == this.internalFrameLayout)
/* 130 */       this.frame.setLayout(null);
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 140 */     return getContext(paramJComponent, getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 144 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent)
/*     */   {
/* 149 */     return SynthLookAndFeel.getComponentState(paramJComponent);
/*     */   }
/*     */ 
/*     */   protected JComponent createNorthPane(JInternalFrame paramJInternalFrame)
/*     */   {
/* 157 */     this.titlePane = new SynthInternalFrameTitlePane(paramJInternalFrame);
/* 158 */     this.titlePane.setName("InternalFrame.northPane");
/* 159 */     return this.titlePane;
/*     */   }
/*     */ 
/*     */   protected ComponentListener createComponentListener()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: ldc 3
/*     */     //   2: invokestatic 208	javax/swing/UIManager:getBoolean	(Ljava/lang/Object;)Z
/*     */     //   5: ifeq +12 -> 17
/*     */     //   8: new 104	javax/swing/plaf/synth/SynthInternalFrameUI$1
/*     */     //   11: dup
/*     */     //   12: aload_0
/*     */     //   13: invokespecial 229	javax/swing/plaf/synth/SynthInternalFrameUI$1:<init>	(Ljavax/swing/plaf/synth/SynthInternalFrameUI;)V
/*     */     //   16: areturn
/*     */     //   17: aload_0
/*     */     //   18: invokespecial 213	javax/swing/plaf/basic/BasicInternalFrameUI:createComponentListener	()Ljava/awt/event/ComponentListener;
/*     */     //   21: areturn
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 210 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 212 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 213 */     localSynthContext.getPainter().paintInternalFrameBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 215 */     paint(localSynthContext, paramGraphics);
/* 216 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 230 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 232 */     paint(localSynthContext, paramGraphics);
/* 233 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 252 */     paramSynthContext.getPainter().paintInternalFrameBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 261 */     SynthStyle localSynthStyle = this.style;
/* 262 */     JInternalFrame localJInternalFrame = (JInternalFrame)paramPropertyChangeEvent.getSource();
/* 263 */     String str = paramPropertyChangeEvent.getPropertyName();
/*     */ 
/* 265 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent)) {
/* 266 */       updateStyle(localJInternalFrame);
/*     */     }
/*     */ 
/* 269 */     if ((this.style == localSynthStyle) && ((str == "maximum") || (str == "selected")))
/*     */     {
/* 273 */       SynthContext localSynthContext = getContext(localJInternalFrame, 1);
/* 274 */       this.style.uninstallDefaults(localSynthContext);
/* 275 */       this.style.installDefaults(localSynthContext, this);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthInternalFrameUI
 * JD-Core Version:    0.6.2
 */