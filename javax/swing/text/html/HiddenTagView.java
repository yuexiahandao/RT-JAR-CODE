/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.awt.BorderLayout;
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Toolkit;
/*     */ import java.io.Serializable;
/*     */ import javax.swing.JPanel;
/*     */ import javax.swing.JTextField;
/*     */ import javax.swing.SwingUtilities;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.event.DocumentListener;
/*     */ import javax.swing.text.AbstractDocument;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.SimpleAttributeSet;
/*     */ import javax.swing.text.StyleConstants;
/*     */ import javax.swing.text.StyledDocument;
/*     */ import javax.swing.text.ViewFactory;
/*     */ 
/*     */ class HiddenTagView extends EditableView
/*     */   implements DocumentListener
/*     */ {
/*     */   float yAlign;
/*     */   boolean isSettingAttributes;
/*     */   static final int circleR = 3;
/*     */   static final int circleD = 6;
/*     */   static final int tagSize = 6;
/*     */   static final int padding = 3;
/* 294 */   static final Color UnknownTagBorderColor = Color.black;
/* 295 */   static final Border StartBorder = new StartTagBorder();
/* 296 */   static final Border EndBorder = new EndTagBorder();
/*     */ 
/*     */   HiddenTagView(Element paramElement)
/*     */   {
/*  48 */     super(paramElement);
/*  49 */     this.yAlign = 1.0F;
/*     */   }
/*     */ 
/*     */   protected Component createComponent() {
/*  53 */     JTextField localJTextField = new JTextField(getElement().getName());
/*  54 */     Document localDocument = getDocument();
/*     */     Font localFont;
/*  56 */     if ((localDocument instanceof StyledDocument)) {
/*  57 */       localFont = ((StyledDocument)localDocument).getFont(getAttributes());
/*  58 */       localJTextField.setFont(localFont);
/*     */     }
/*     */     else {
/*  61 */       localFont = localJTextField.getFont();
/*     */     }
/*  63 */     localJTextField.getDocument().addDocumentListener(this);
/*  64 */     updateYAlign(localFont);
/*     */ 
/*  68 */     JPanel localJPanel = new JPanel(new BorderLayout());
/*  69 */     localJPanel.setBackground(null);
/*  70 */     if (isEndTag()) {
/*  71 */       localJPanel.setBorder(EndBorder);
/*     */     }
/*     */     else {
/*  74 */       localJPanel.setBorder(StartBorder);
/*     */     }
/*  76 */     localJPanel.add(localJTextField);
/*  77 */     return localJPanel;
/*     */   }
/*     */ 
/*     */   public float getAlignment(int paramInt) {
/*  81 */     if (paramInt == 1) {
/*  82 */       return this.yAlign;
/*     */     }
/*  84 */     return 0.5F;
/*     */   }
/*     */ 
/*     */   public float getMinimumSpan(int paramInt) {
/*  88 */     if ((paramInt == 0) && (isVisible()))
/*     */     {
/*  90 */       return Math.max(30.0F, super.getPreferredSpan(paramInt));
/*     */     }
/*  92 */     return super.getMinimumSpan(paramInt);
/*     */   }
/*     */ 
/*     */   public float getPreferredSpan(int paramInt) {
/*  96 */     if ((paramInt == 0) && (isVisible())) {
/*  97 */       return Math.max(30.0F, super.getPreferredSpan(paramInt));
/*     */     }
/*  99 */     return super.getPreferredSpan(paramInt);
/*     */   }
/*     */ 
/*     */   public float getMaximumSpan(int paramInt) {
/* 103 */     if ((paramInt == 0) && (isVisible()))
/*     */     {
/* 105 */       return Math.max(30.0F, super.getMaximumSpan(paramInt));
/*     */     }
/* 107 */     return super.getMaximumSpan(paramInt);
/*     */   }
/*     */ 
/*     */   public void insertUpdate(DocumentEvent paramDocumentEvent)
/*     */   {
/* 112 */     updateModelFromText();
/*     */   }
/*     */ 
/*     */   public void removeUpdate(DocumentEvent paramDocumentEvent) {
/* 116 */     updateModelFromText();
/*     */   }
/*     */ 
/*     */   public void changedUpdate(DocumentEvent paramDocumentEvent) {
/* 120 */     updateModelFromText();
/*     */   }
/*     */ 
/*     */   public void changedUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/* 125 */     if (!this.isSettingAttributes)
/* 126 */       setTextFromModel();
/*     */   }
/*     */ 
/*     */   void updateYAlign(Font paramFont)
/*     */   {
/* 133 */     Container localContainer = getContainer();
/* 134 */     FontMetrics localFontMetrics = localContainer != null ? localContainer.getFontMetrics(paramFont) : Toolkit.getDefaultToolkit().getFontMetrics(paramFont);
/*     */ 
/* 136 */     float f1 = localFontMetrics.getHeight();
/* 137 */     float f2 = localFontMetrics.getDescent();
/* 138 */     this.yAlign = (f1 > 0.0F ? (f1 - f2) / f1 : 0.0F);
/*     */   }
/*     */ 
/*     */   void resetBorder() {
/* 142 */     Component localComponent = getComponent();
/*     */ 
/* 144 */     if (localComponent != null)
/* 145 */       if (isEndTag()) {
/* 146 */         ((JPanel)localComponent).setBorder(EndBorder);
/*     */       }
/*     */       else
/* 149 */         ((JPanel)localComponent).setBorder(StartBorder);
/*     */   }
/*     */ 
/*     */   void setTextFromModel()
/*     */   {
/* 163 */     if (SwingUtilities.isEventDispatchThread()) {
/* 164 */       _setTextFromModel();
/*     */     }
/*     */     else
/* 167 */       SwingUtilities.invokeLater(new Runnable() {
/*     */         public void run() {
/* 169 */           HiddenTagView.this._setTextFromModel();
/*     */         }
/*     */       });
/*     */   }
/*     */ 
/*     */   void _setTextFromModel()
/*     */   {
/* 180 */     Document localDocument = getDocument();
/*     */     try {
/* 182 */       this.isSettingAttributes = true;
/* 183 */       if ((localDocument instanceof AbstractDocument)) {
/* 184 */         ((AbstractDocument)localDocument).readLock();
/*     */       }
/* 186 */       JTextComponent localJTextComponent = getTextComponent();
/* 187 */       if (localJTextComponent != null) {
/* 188 */         localJTextComponent.setText(getRepresentedText());
/* 189 */         resetBorder();
/* 190 */         Container localContainer = getContainer();
/* 191 */         if (localContainer != null) {
/* 192 */           preferenceChanged(this, true, true);
/* 193 */           localContainer.repaint();
/*     */         }
/*     */       }
/*     */     }
/*     */     finally {
/* 198 */       this.isSettingAttributes = false;
/* 199 */       if ((localDocument instanceof AbstractDocument))
/* 200 */         ((AbstractDocument)localDocument).readUnlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   void updateModelFromText()
/*     */   {
/* 214 */     if (!this.isSettingAttributes)
/* 215 */       if (SwingUtilities.isEventDispatchThread()) {
/* 216 */         _updateModelFromText();
/*     */       }
/*     */       else
/* 219 */         SwingUtilities.invokeLater(new Runnable() {
/*     */           public void run() {
/* 221 */             HiddenTagView.this._updateModelFromText();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   void _updateModelFromText()
/*     */   {
/* 233 */     Document localDocument = getDocument();
/* 234 */     Object localObject1 = getElement().getAttributes().getAttribute(StyleConstants.NameAttribute);
/*     */ 
/* 236 */     if (((localObject1 instanceof HTML.UnknownTag)) && ((localDocument instanceof StyledDocument)))
/*     */     {
/* 238 */       SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
/* 239 */       JTextComponent localJTextComponent = getTextComponent();
/* 240 */       if (localJTextComponent != null) {
/* 241 */         String str = localJTextComponent.getText();
/* 242 */         this.isSettingAttributes = true;
/*     */         try {
/* 244 */           localSimpleAttributeSet.addAttribute(StyleConstants.NameAttribute, new HTML.UnknownTag(str));
/*     */ 
/* 246 */           ((StyledDocument)localDocument).setCharacterAttributes(getStartOffset(), getEndOffset() - getStartOffset(), localSimpleAttributeSet, false);
/*     */         }
/*     */         finally
/*     */         {
/* 251 */           this.isSettingAttributes = false;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   JTextComponent getTextComponent() {
/* 258 */     Component localComponent = getComponent();
/*     */ 
/* 260 */     return localComponent == null ? null : (JTextComponent)((Container)localComponent).getComponent(0);
/*     */   }
/*     */ 
/*     */   String getRepresentedText()
/*     */   {
/* 265 */     String str = getElement().getName();
/* 266 */     return str == null ? "" : str;
/*     */   }
/*     */ 
/*     */   boolean isEndTag() {
/* 270 */     AttributeSet localAttributeSet = getElement().getAttributes();
/* 271 */     if (localAttributeSet != null) {
/* 272 */       Object localObject = localAttributeSet.getAttribute(HTML.Attribute.ENDTAG);
/* 273 */       if ((localObject != null) && ((localObject instanceof String)) && (((String)localObject).equals("true")))
/*     */       {
/* 275 */         return true;
/*     */       }
/*     */     }
/* 278 */     return false;
/*     */   }
/*     */ 
/*     */   static class EndTagBorder
/*     */     implements Border, Serializable
/*     */   {
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 333 */       paramGraphics.setColor(HiddenTagView.UnknownTagBorderColor);
/* 334 */       paramInt1 += 3;
/* 335 */       paramInt3 -= 6;
/* 336 */       paramGraphics.drawLine(paramInt1 + paramInt3 - 1, paramInt2 + 3, paramInt1 + paramInt3 - 1, paramInt2 + paramInt4 - 3);
/*     */ 
/* 338 */       paramGraphics.drawArc(paramInt1 + paramInt3 - 6 - 1, paramInt2 + paramInt4 - 6 - 1, 6, 6, 270, 90);
/*     */ 
/* 340 */       paramGraphics.drawArc(paramInt1 + paramInt3 - 6 - 1, paramInt2, 6, 6, 0, 90);
/* 341 */       paramGraphics.drawLine(paramInt1 + 6, paramInt2, paramInt1 + paramInt3 - 3, paramInt2);
/* 342 */       paramGraphics.drawLine(paramInt1 + 6, paramInt2 + paramInt4 - 1, paramInt1 + paramInt3 - 3, paramInt2 + paramInt4 - 1);
/*     */ 
/* 345 */       paramGraphics.drawLine(paramInt1 + 6, paramInt2, paramInt1, paramInt2 + paramInt4 / 2);
/*     */ 
/* 347 */       paramGraphics.drawLine(paramInt1 + 6, paramInt2 + paramInt4, paramInt1, paramInt2 + paramInt4 / 2);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent)
/*     */     {
/* 352 */       return new Insets(2, 11, 2, 5);
/*     */     }
/*     */ 
/*     */     public boolean isBorderOpaque() {
/* 356 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */   static class StartTagBorder
/*     */     implements Border, Serializable
/*     */   {
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 302 */       paramGraphics.setColor(HiddenTagView.UnknownTagBorderColor);
/* 303 */       paramInt1 += 3;
/* 304 */       paramInt3 -= 6;
/* 305 */       paramGraphics.drawLine(paramInt1, paramInt2 + 3, paramInt1, paramInt2 + paramInt4 - 3);
/*     */ 
/* 307 */       paramGraphics.drawArc(paramInt1, paramInt2 + paramInt4 - 6 - 1, 6, 6, 180, 90);
/*     */ 
/* 309 */       paramGraphics.drawArc(paramInt1, paramInt2, 6, 6, 90, 90);
/* 310 */       paramGraphics.drawLine(paramInt1 + 3, paramInt2, paramInt1 + paramInt3 - 6, paramInt2);
/* 311 */       paramGraphics.drawLine(paramInt1 + 3, paramInt2 + paramInt4 - 1, paramInt1 + paramInt3 - 6, paramInt2 + paramInt4 - 1);
/*     */ 
/* 314 */       paramGraphics.drawLine(paramInt1 + paramInt3 - 6, paramInt2, paramInt1 + paramInt3 - 1, paramInt2 + paramInt4 / 2);
/*     */ 
/* 316 */       paramGraphics.drawLine(paramInt1 + paramInt3 - 6, paramInt2 + paramInt4, paramInt1 + paramInt3 - 1, paramInt2 + paramInt4 / 2);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent)
/*     */     {
/* 321 */       return new Insets(2, 5, 2, 11);
/*     */     }
/*     */ 
/*     */     public boolean isBorderOpaque() {
/* 325 */       return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.HiddenTagView
 * JD-Core Version:    0.6.2
 */