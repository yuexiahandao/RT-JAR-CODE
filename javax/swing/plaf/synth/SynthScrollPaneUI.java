/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.event.ContainerEvent;
/*     */ import java.awt.event.ContainerListener;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JScrollPane;
/*     */ import javax.swing.JViewport;
/*     */ import javax.swing.UIManager;
/*     */ import javax.swing.border.AbstractBorder;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicScrollPaneUI;
/*     */ import javax.swing.text.JTextComponent;
/*     */ 
/*     */ public class SynthScrollPaneUI extends BasicScrollPaneUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*     */   private boolean viewportViewHasFocus;
/*     */   private ViewportViewFocusHandler viewportViewFocusHandler;
/*     */ 
/*     */   public SynthScrollPaneUI()
/*     */   {
/*  53 */     this.viewportViewHasFocus = false;
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  63 */     return new SynthScrollPaneUI();
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/*  80 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/*  82 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/*  83 */     localSynthContext.getPainter().paintScrollPaneBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/*  85 */     paint(localSynthContext, paramGraphics);
/*  86 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 100 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 102 */     paint(localSynthContext, paramGraphics);
/* 103 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/* 114 */     Border localBorder = this.scrollpane.getViewportBorder();
/* 115 */     if (localBorder != null) {
/* 116 */       Rectangle localRectangle = this.scrollpane.getViewportBorderBounds();
/* 117 */       localBorder.paintBorder(this.scrollpane, paramGraphics, localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 127 */     paramSynthContext.getPainter().paintScrollPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   protected void installDefaults(JScrollPane paramJScrollPane)
/*     */   {
/* 135 */     updateStyle(paramJScrollPane);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JScrollPane paramJScrollPane) {
/* 139 */     SynthContext localSynthContext = getContext(paramJScrollPane, 1);
/* 140 */     SynthStyle localSynthStyle = this.style;
/*     */ 
/* 142 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/* 143 */     if (this.style != localSynthStyle) {
/* 144 */       Border localBorder = this.scrollpane.getViewportBorder();
/* 145 */       if ((localBorder == null) || ((localBorder instanceof UIResource))) {
/* 146 */         this.scrollpane.setViewportBorder(new ViewportBorder(localSynthContext));
/*     */       }
/* 148 */       if (localSynthStyle != null) {
/* 149 */         uninstallKeyboardActions(paramJScrollPane);
/* 150 */         installKeyboardActions(paramJScrollPane);
/*     */       }
/*     */     }
/* 153 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void installListeners(JScrollPane paramJScrollPane)
/*     */   {
/* 161 */     super.installListeners(paramJScrollPane);
/* 162 */     paramJScrollPane.addPropertyChangeListener(this);
/* 163 */     if (UIManager.getBoolean("ScrollPane.useChildTextComponentFocus")) {
/* 164 */       this.viewportViewFocusHandler = new ViewportViewFocusHandler(null);
/* 165 */       paramJScrollPane.getViewport().addContainerListener(this.viewportViewFocusHandler);
/* 166 */       Component localComponent = paramJScrollPane.getViewport().getView();
/* 167 */       if ((localComponent instanceof JTextComponent))
/* 168 */         localComponent.addFocusListener(this.viewportViewFocusHandler);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults(JScrollPane paramJScrollPane)
/*     */   {
/* 178 */     SynthContext localSynthContext = getContext(paramJScrollPane, 1);
/*     */ 
/* 180 */     this.style.uninstallDefaults(localSynthContext);
/* 181 */     localSynthContext.dispose();
/*     */ 
/* 183 */     if ((this.scrollpane.getViewportBorder() instanceof UIResource))
/* 184 */       this.scrollpane.setViewportBorder(null);
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners(JComponent paramJComponent)
/*     */   {
/* 193 */     super.uninstallListeners(paramJComponent);
/* 194 */     paramJComponent.removePropertyChangeListener(this);
/* 195 */     if (this.viewportViewFocusHandler != null) {
/* 196 */       JViewport localJViewport = ((JScrollPane)paramJComponent).getViewport();
/* 197 */       localJViewport.removeContainerListener(this.viewportViewFocusHandler);
/* 198 */       if (localJViewport.getView() != null) {
/* 199 */         localJViewport.getView().removeFocusListener(this.viewportViewFocusHandler);
/*     */       }
/* 201 */       this.viewportViewFocusHandler = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 210 */     return getContext(paramJComponent, getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 214 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent)
/*     */   {
/* 219 */     int i = SynthLookAndFeel.getComponentState(paramJComponent);
/* 220 */     if ((this.viewportViewFocusHandler != null) && (this.viewportViewHasFocus)) {
/* 221 */       i |= 256;
/*     */     }
/* 223 */     return i;
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent) {
/* 227 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/* 228 */       updateStyle(this.scrollpane);
/*     */   }
/*     */ 
/*     */   private class ViewportBorder extends AbstractBorder
/*     */     implements UIResource
/*     */   {
/*     */     private Insets insets;
/*     */ 
/*     */     ViewportBorder(SynthContext arg2)
/*     */     {
/*     */       SynthContext localSynthContext;
/* 238 */       this.insets = ((Insets)localSynthContext.getStyle().get(localSynthContext, "ScrollPane.viewportBorderInsets"));
/*     */ 
/* 240 */       if (this.insets == null)
/* 241 */         this.insets = SynthLookAndFeel.EMPTY_UIRESOURCE_INSETS;
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 248 */       JComponent localJComponent = (JComponent)paramComponent;
/* 249 */       SynthContext localSynthContext = SynthScrollPaneUI.this.getContext(localJComponent);
/* 250 */       SynthStyle localSynthStyle = localSynthContext.getStyle();
/* 251 */       if (localSynthStyle == null) {
/* 252 */         if (!$assertionsDisabled) throw new AssertionError("SynthBorder is being used outside after the  UI has been uninstalled");
/*     */ 
/* 254 */         return;
/*     */       }
/* 256 */       localSynthContext.getPainter().paintViewportBorder(localSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */ 
/* 258 */       localSynthContext.dispose();
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 263 */       if (paramInsets == null) {
/* 264 */         return new Insets(this.insets.top, this.insets.left, this.insets.bottom, this.insets.right);
/*     */       }
/*     */ 
/* 267 */       paramInsets.top = this.insets.top;
/* 268 */       paramInsets.bottom = this.insets.bottom;
/* 269 */       paramInsets.left = this.insets.left;
/* 270 */       paramInsets.right = this.insets.left;
/* 271 */       return paramInsets;
/*     */     }
/*     */ 
/*     */     public boolean isBorderOpaque()
/*     */     {
/* 276 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ViewportViewFocusHandler implements ContainerListener, FocusListener
/*     */   {
/*     */     private ViewportViewFocusHandler() {
/*     */     }
/*     */ 
/*     */     public void componentAdded(ContainerEvent paramContainerEvent) {
/* 286 */       if ((paramContainerEvent.getChild() instanceof JTextComponent)) {
/* 287 */         paramContainerEvent.getChild().addFocusListener(this);
/* 288 */         SynthScrollPaneUI.this.viewportViewHasFocus = paramContainerEvent.getChild().isFocusOwner();
/* 289 */         SynthScrollPaneUI.this.scrollpane.repaint();
/*     */       }
/*     */     }
/*     */ 
/*     */     public void componentRemoved(ContainerEvent paramContainerEvent) {
/* 294 */       if ((paramContainerEvent.getChild() instanceof JTextComponent))
/* 295 */         paramContainerEvent.getChild().removeFocusListener(this);
/*     */     }
/*     */ 
/*     */     public void focusGained(FocusEvent paramFocusEvent)
/*     */     {
/* 300 */       SynthScrollPaneUI.this.viewportViewHasFocus = true;
/* 301 */       SynthScrollPaneUI.this.scrollpane.repaint();
/*     */     }
/*     */ 
/*     */     public void focusLost(FocusEvent paramFocusEvent) {
/* 305 */       SynthScrollPaneUI.this.viewportViewHasFocus = false;
/* 306 */       SynthScrollPaneUI.this.scrollpane.repaint();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthScrollPaneUI
 * JD-Core Version:    0.6.2
 */