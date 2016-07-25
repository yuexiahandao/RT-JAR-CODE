/*     */ package javax.swing.plaf.synth;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Dimension;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.GridBagConstraints;
/*     */ import java.awt.GridBagLayout;
/*     */ import java.awt.Insets;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import javax.swing.Box;
/*     */ import javax.swing.JComponent;
/*     */ import javax.swing.JOptionPane;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JSeparator;
/*     */ import javax.swing.plaf.ComponentUI;
/*     */ import javax.swing.plaf.basic.BasicOptionPaneUI;
/*     */ import sun.swing.DefaultLookup;
/*     */ 
/*     */ public class SynthOptionPaneUI extends BasicOptionPaneUI
/*     */   implements PropertyChangeListener, SynthUI
/*     */ {
/*     */   private SynthStyle style;
/*     */ 
/*     */   public static ComponentUI createUI(JComponent paramJComponent)
/*     */   {
/*  55 */     return new SynthOptionPaneUI();
/*     */   }
/*     */ 
/*     */   protected void installDefaults()
/*     */   {
/*  63 */     updateStyle(this.optionPane);
/*     */   }
/*     */ 
/*     */   protected void installListeners()
/*     */   {
/*  71 */     super.installListeners();
/*  72 */     this.optionPane.addPropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   private void updateStyle(JComponent paramJComponent) {
/*  76 */     SynthContext localSynthContext = getContext(paramJComponent, 1);
/*  77 */     SynthStyle localSynthStyle = this.style;
/*     */ 
/*  79 */     this.style = SynthLookAndFeel.updateStyle(localSynthContext, this);
/*  80 */     if (this.style != localSynthStyle) {
/*  81 */       this.minimumSize = ((Dimension)this.style.get(localSynthContext, "OptionPane.minimumSize"));
/*     */ 
/*  83 */       if (this.minimumSize == null) {
/*  84 */         this.minimumSize = new Dimension(262, 90);
/*     */       }
/*  86 */       if (localSynthStyle != null) {
/*  87 */         uninstallKeyboardActions();
/*  88 */         installKeyboardActions();
/*     */       }
/*     */     }
/*  91 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void uninstallDefaults()
/*     */   {
/*  99 */     SynthContext localSynthContext = getContext(this.optionPane, 1);
/*     */ 
/* 101 */     this.style.uninstallDefaults(localSynthContext);
/* 102 */     localSynthContext.dispose();
/* 103 */     this.style = null;
/*     */   }
/*     */ 
/*     */   protected void uninstallListeners()
/*     */   {
/* 111 */     super.uninstallListeners();
/* 112 */     this.optionPane.removePropertyChangeListener(this);
/*     */   }
/*     */ 
/*     */   protected void installComponents()
/*     */   {
/* 120 */     this.optionPane.add(createMessageArea());
/*     */ 
/* 122 */     Container localContainer = createSeparator();
/* 123 */     if (localContainer != null) {
/* 124 */       this.optionPane.add(localContainer);
/* 125 */       SynthContext localSynthContext = getContext(this.optionPane, 1);
/* 126 */       this.optionPane.add(Box.createVerticalStrut(localSynthContext.getStyle().getInt(localSynthContext, "OptionPane.separatorPadding", 6)));
/*     */ 
/* 128 */       localSynthContext.dispose();
/*     */     }
/* 130 */     this.optionPane.add(createButtonArea());
/* 131 */     this.optionPane.applyComponentOrientation(this.optionPane.getComponentOrientation());
/*     */   }
/*     */ 
/*     */   public SynthContext getContext(JComponent paramJComponent)
/*     */   {
/* 139 */     return getContext(paramJComponent, getComponentState(paramJComponent));
/*     */   }
/*     */ 
/*     */   private SynthContext getContext(JComponent paramJComponent, int paramInt) {
/* 143 */     return SynthContext.getContext(SynthContext.class, paramJComponent, SynthLookAndFeel.getRegion(paramJComponent), this.style, paramInt);
/*     */   }
/*     */ 
/*     */   private int getComponentState(JComponent paramJComponent)
/*     */   {
/* 148 */     return SynthLookAndFeel.getComponentState(paramJComponent);
/*     */   }
/*     */ 
/*     */   public void update(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 165 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 167 */     SynthLookAndFeel.update(localSynthContext, paramGraphics);
/* 168 */     localSynthContext.getPainter().paintOptionPaneBackground(localSynthContext, paramGraphics, 0, 0, paramJComponent.getWidth(), paramJComponent.getHeight());
/*     */ 
/* 170 */     paint(localSynthContext, paramGraphics);
/* 171 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, JComponent paramJComponent)
/*     */   {
/* 185 */     SynthContext localSynthContext = getContext(paramJComponent);
/*     */ 
/* 187 */     paint(localSynthContext, paramGraphics);
/* 188 */     localSynthContext.dispose();
/*     */   }
/*     */ 
/*     */   protected void paint(SynthContext paramSynthContext, Graphics paramGraphics)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void paintBorder(SynthContext paramSynthContext, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 207 */     paramSynthContext.getPainter().paintOptionPaneBorder(paramSynthContext, paramGraphics, paramInt1, paramInt2, paramInt3, paramInt4);
/*     */   }
/*     */ 
/*     */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*     */   {
/* 215 */     if (SynthLookAndFeel.shouldUpdateStyle(paramPropertyChangeEvent))
/* 216 */       updateStyle((JOptionPane)paramPropertyChangeEvent.getSource());
/*     */   }
/*     */ 
/*     */   protected boolean getSizeButtonsToSameWidth()
/*     */   {
/* 225 */     return DefaultLookup.getBoolean(this.optionPane, this, "OptionPane.sameSizeButtons", true);
/*     */   }
/*     */ 
/*     */   protected Container createMessageArea()
/*     */   {
/* 236 */     JPanel localJPanel1 = new JPanel();
/* 237 */     localJPanel1.setName("OptionPane.messageArea");
/* 238 */     localJPanel1.setLayout(new BorderLayout());
/*     */ 
/* 241 */     JPanel localJPanel2 = new JPanel(new GridBagLayout());
/* 242 */     JPanel localJPanel3 = new JPanel(new BorderLayout());
/*     */ 
/* 244 */     localJPanel2.setName("OptionPane.body");
/* 245 */     localJPanel3.setName("OptionPane.realBody");
/*     */ 
/* 247 */     if (getIcon() != null) {
/* 248 */       localObject = new JPanel();
/* 249 */       ((JPanel)localObject).setName("OptionPane.separator");
/* 250 */       ((JPanel)localObject).setPreferredSize(new Dimension(15, 1));
/* 251 */       localJPanel3.add((Component)localObject, "Before");
/*     */     }
/* 253 */     localJPanel3.add(localJPanel2, "Center");
/*     */ 
/* 255 */     Object localObject = new GridBagConstraints();
/* 256 */     ((GridBagConstraints)localObject).gridx = (((GridBagConstraints)localObject).gridy = 0);
/* 257 */     ((GridBagConstraints)localObject).gridwidth = 0;
/* 258 */     ((GridBagConstraints)localObject).gridheight = 1;
/*     */ 
/* 260 */     SynthContext localSynthContext = getContext(this.optionPane, 1);
/* 261 */     ((GridBagConstraints)localObject).anchor = localSynthContext.getStyle().getInt(localSynthContext, "OptionPane.messageAnchor", 10);
/*     */ 
/* 263 */     localSynthContext.dispose();
/*     */ 
/* 265 */     ((GridBagConstraints)localObject).insets = new Insets(0, 0, 3, 0);
/*     */ 
/* 267 */     addMessageComponents(localJPanel2, (GridBagConstraints)localObject, getMessage(), getMaxCharactersPerLineCount(), false);
/*     */ 
/* 269 */     localJPanel1.add(localJPanel3, "Center");
/*     */ 
/* 271 */     addIcon(localJPanel1);
/* 272 */     return localJPanel1;
/*     */   }
/*     */ 
/*     */   protected Container createSeparator()
/*     */   {
/* 280 */     JSeparator localJSeparator = new JSeparator(0);
/*     */ 
/* 282 */     localJSeparator.setName("OptionPane.separator");
/* 283 */     return localJSeparator;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthOptionPaneUI
 * JD-Core Version:    0.6.2
 */