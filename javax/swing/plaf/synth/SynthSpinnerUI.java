/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.Component;
/*     */ import java.awt.ComponentOrientation;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.LayoutManager;
/*     */ import java.awt.event.FocusEvent;
/*     */ import java.awt.event.FocusListener;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.JButton;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JFormattedTextField;
/*     */ import javax.swing.JSpinner;
/*     */ import javax.swing.JSpinner.DefaultEditor;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.SpinnerUI;
/*     */ import javax.swing.plaf.UIResource;
/*     */ import javax.swing.plaf.basic.BasicSpinnerUI;
/*     */ 
/*     */ public class SynthSpinnerUI extends BasicSpinnerUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*     */   private EditorFocusHandler editorFocusHandler;
/*     */ 
/*     */   public SynthSpinnerUI()
/*     */   {
/*  54 */     this.editorFocusHandler = new EditorFocusHandler(null);
/*     */   }
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  64 */     return new SynthSpinnerUI();
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/*  72 */     super.installListeners();
/*  73 */     this.spinner.addPropertyChangeListener(this);
/*  74 */     JComponent localJComponent = this.spinner.getEditor();
/*  75 */     if ((localJComponent instanceof JSpinner.DefaultEditor)) {
/*  76 */       JFormattedTextField localJFormattedTextField = ((JSpinner.DefaultEditor)localJComponent).getTextField();
/*  77 */       if (localJFormattedTextField != null)
/*  78 */         localJFormattedTextField.addFocusListener(this.editorFocusHandler);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/*  88 */     super.uninstallListeners();
/*  89 */     this.spinner.removePropertyChangeListener(this);
/*  90 */     JComponent localJComponent = this.spinner.getEditor();
/*  91 */     if ((localJComponent instanceof JSpinner.DefaultEditor)) {
/*  92 */       JFormattedTextField localJFormattedTextField = ((JSpinner.DefaultEditor)localJComponent).getTextField();
/*  93 */       if (localJFormattedTextField != null)
/*  94 */         localJFormattedTextField.removeFocusListener(this.editorFocusHandler);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/* 114 */     LayoutManager localLayoutManager = this.spinner.getLayout();
/*     */ 
/* 116 */     if ((localLayoutManager == null) || ((localLayoutManager instanceof UIResource))) {
/* 117 */       this.spinner.setLayout(createLayout());
/*     */     }
/* 119 */     updateStyle(this.spinner);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JSpinner paramJSpinner)
/*     */   {
/* 124 */     SynthContext localSynthContext = getContext(paramJSpinner, 1);
/* 125 */     SynthStyle localSynthStyle = this.style;
/* 126 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/* 127 */     if ((this.style != localSynthStyle) && 
/* 128 */       (localSynthStyle != null))
/*     */     {
/* 131 */       installKeyboardActions();
/*     */     }
/*     */ 
/* 134 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/* 147 */     if ((this.spinner.getLayout() instanceof UIResource)) {
/* 148 */       this.spinner.setLayout(null);
/*     */     }
/*     */ 
/* 151 */     SynthContext localSynthContext = getContext(this.spinner, 1);
/*     */ 
/* 153 */     this.style.uninstallDefaults(localSynthContext);
/* 154 */     localSynthContext.dispose();
/* 155 */     this.style = null;
/*     */   }
/*     */ 
/*     */   protected LayoutManager createLayout()
/*     */   {
/* 163 */     return new SpinnerLayout(null);
/*     */   }
/*     */ 
/*     */   protected Component createPreviousButton()
/*     */   {
/* 172 */     SynthArrowButton localSynthArrowButton = new SynthArrowButton(5);
/* 173 */     localSynthArrowButton.setName("Spinner.previousButton");
/* 174 */     installPreviousButtonListeners(localSynthArrowButton);
/* 175 */     return localSynthArrowButton;
/*     */   }
/*     */ 
/*     */   protected Component createNextButton()
/*     */   {
/* 184 */     SynthArrowButton localSynthArrowButton = new SynthArrowButton(1);
/* 185 */     localSynthArrowButton.setName("Spinner.nextButton");
/* 186 */     installNextButtonListeners(localSynthArrowButton);
/* 187 */     return localSynthArrowButton;
/*     */   }
/*     */ 
/*     */   protected JComponent createEditor()
/*     */   {
/* 216 */     JComponent localJComponent = this.spinner.getEditor();
/* 217 */     localJComponent.setName("Spinner.editor");
/* 218 */     updateEditorAlignment(localJComponent);
/* 219 */     return localJComponent;
/*     */   }
/*     */ 
/*     */   protected void replaceEditor(JComponent paramJComponent1, JComponent paramJComponent2)
/*     */   {
/* 240 */     this.spinner.remove(paramJComponent1);
/* 241 */     this.spinner.add(paramJComponent2, "Editor");
/*     */     JFormattedTextField localJFormattedTextField;
/* 242 */     if ((paramJComponent1 instanceof JSpinner.DefaultEditor)) {
/* 243 */       localJFormattedTextField = ((JSpinner.DefaultEditor)paramJComponent1).getTextField();
/* 244 */       if (localJFormattedTextField != null) {
/* 245 */         localJFormattedTextField.removeFocusListener(this.editorFocusHandler);
/*     */       }
/*     */     }
/* 248 */     if ((paramJComponent2 instanceof JSpinner.DefaultEditor)) {
/* 249 */       localJFormattedTextField = ((JSpinner.DefaultEditor)paramJComponent2).getTextField();
/* 250 */       if (localJFormattedTextField != null)
/* 251 */         localJFormattedTextField.addFocusListener(this.editorFocusHandler);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void updateEditorAlignment(JComponent paramJComponent)
/*     */   {
/* 257 */     if ((paramJComponent instanceof JSpinner.DefaultEditor)) {
/* 258 */       SynthContext localSynthContext = getContext(this.spinner);
/* 259 */       Integer localInteger = (Integer)localSynthContext.getStyle().get(localSynthContext, "Spinner.editorAlignment");
/*     */ 
/* 261 */       JFormattedTextField localJFormattedTextField = ((JSpinner.DefaultEditor)paramJComponent).getTextField();
/* 262 */       if (localInteger != null) {
/* 263 */         localJFormattedTextField.setHorizontalAlignment(localInteger.intValue());
/*     */       }
/*     */ 
/* 267 */       localJFormattedTextField.putClientProperty("JComponent.sizeVariant", this.spinner.getClientProperty("JComponent.sizeVariant"));
/*     */     }
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 277 */     return getContext(paramJComponent, SynthLookAndFeel.getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 281 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 299 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 301 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 302 */     localSynthContext.getPainter().paintSpinnerBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 304 */     paint(localSynthContext, paramGraphics);
/* 305 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 320 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 322 */     paint(localSynthContext, paramGraphics);
/* 323 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 342 */     paramSynthContext.getPainter().paintSpinnerBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 446 */     JSpinner localJSpinner = (JSpinner)paramPropertyChangeEvent.getSource();
/* 447 */     SpinnerUI localSpinnerUI = localJSpinner.getUI();
/*     */ 
/* 449 */     if ((localSpinnerUI instanceof SynthSpinnerUI)) {
/* 450 */       SynthSpinnerUI localSynthSpinnerUI = (SynthSpinnerUI)localSpinnerUI;
/*     */ 
/* 452 */       if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/* 453 */         localSynthSpinnerUI.updateStyle(localJSpinner);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class EditorFocusHandler implements FocusListener {
/*     */     private EditorFocusHandler() {
/*     */     }
/*     */ 
/*     */     public void focusGained(FocusEvent paramFocusEvent) {
/* 462 */       SynthSpinnerUI.this.spinner.repaint();
/*     */     }
/*     */ 
/*     */     public void focusLost(FocusEvent paramFocusEvent)
/*     */     {
/* 467 */       SynthSpinnerUI.this.spinner.repaint();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SpinnerLayout
/*     */     implements LayoutManager, UIResource
/*     */   {
/* 352 */     private Component nextButton = null;
/* 353 */     private Component previousButton = null;
/* 354 */     private Component editor = null;
/*     */ 
/*     */     public void addLayoutComponent(String paramString, Component paramComponent) {
/* 357 */       if ("Next".equals(paramString)) {
/* 358 */         this.nextButton = paramComponent;
/*     */       }
/* 360 */       else if ("Previous".equals(paramString)) {
/* 361 */         this.previousButton = paramComponent;
/*     */       }
/* 363 */       else if ("Editor".equals(paramString))
/* 364 */         this.editor = paramComponent;
/*     */     }
/*     */ 
/*     */     public void removeLayoutComponent(Component paramComponent)
/*     */     {
/* 369 */       if (paramComponent == this.nextButton) {
/* 370 */         this.nextButton = null;
/*     */       }
/* 372 */       else if (paramComponent == this.previousButton) {
/* 373 */         this.previousButton = null;
/*     */       }
/* 375 */       else if (paramComponent == this.editor)
/* 376 */         this.editor = null;
/*     */     }
/*     */ 
/*     */     private Dimension preferredSize(Component paramComponent)
/*     */     {
/* 381 */       return paramComponent == null ? new Dimension(0, 0) : paramComponent.getPreferredSize();
/*     */     }
/*     */ 
/*     */     public Dimension preferredLayoutSize(Container paramContainer) {
/* 385 */       Dimension localDimension1 = preferredSize(this.nextButton);
/* 386 */       Dimension localDimension2 = preferredSize(this.previousButton);
/* 387 */       Dimension localDimension3 = preferredSize(this.editor);
/*     */ 
/* 391 */       localDimension3.height = ((localDimension3.height + 1) / 2 * 2);
/*     */ 
/* 393 */       Dimension localDimension4 = new Dimension(localDimension3.width, localDimension3.height);
/* 394 */       localDimension4.width += Math.max(localDimension1.width, localDimension2.width);
/* 395 */       Insets localInsets = paramContainer.getInsets();
/* 396 */       localDimension4.width += localInsets.left + localInsets.right;
/* 397 */       localDimension4.height += localInsets.top + localInsets.bottom;
/* 398 */       return localDimension4;
/*     */     }
/*     */ 
/*     */     public Dimension minimumLayoutSize(Container paramContainer) {
/* 402 */       return preferredLayoutSize(paramContainer);
/*     */     }
/*     */ 
/*     */     private void setBounds(Component paramComponent, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 406 */       if (paramComponent != null)
/* 407 */         paramComponent.setBounds(paramInt1, paramInt2, paramInt3, paramInt4);
/*     */     }
/*     */ 
/*     */     public void layoutContainer(Container paramContainer)
/*     */     {
/* 412 */       Insets localInsets = paramContainer.getInsets();
/* 413 */       int i = paramContainer.getWidth() - (localInsets.left + localInsets.right);
/* 414 */       int j = paramContainer.getHeight() - (localInsets.top + localInsets.bottom);
/* 415 */       Dimension localDimension1 = preferredSize(this.nextButton);
/* 416 */       Dimension localDimension2 = preferredSize(this.previousButton);
/* 417 */       int k = j / 2;
/* 418 */       int m = j - k;
/* 419 */       int n = Math.max(localDimension1.width, localDimension2.width);
/* 420 */       int i1 = i - n;
/*     */       int i2;
/*     */       int i3;
/* 425 */       if (paramContainer.getComponentOrientation().isLeftToRight()) {
/* 426 */         i2 = localInsets.left;
/* 427 */         i3 = i2 + i1;
/*     */       }
/*     */       else {
/* 430 */         i3 = localInsets.left;
/* 431 */         i2 = i3 + n;
/*     */       }
/*     */ 
/* 434 */       int i4 = localInsets.top + k;
/* 435 */       setBounds(this.editor, i2, localInsets.top, i1, j);
/* 436 */       setBounds(this.nextButton, i3, localInsets.top, n, k);
/* 437 */       setBounds(this.previousButton, i3, i4, n, m);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthSpinnerUI
 * JD-Core Version:    0.6.2
 */