/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Container;
/*     */ import java.awt.Font;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Insets;
/*     */ import javax.swing.JTextArea;
/*     */ import javax.swing.border.Border;
/*     */ import javax.swing.border.LineBorder;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.SimpleAttributeSet;
/*     */ import javax.swing.text.StyledDocument;
/*     */ 
/*     */ class CommentView extends HiddenTagView
/*     */ {
/* 114 */   static final Border CBorder = new CommentBorder();
/*     */   static final int commentPadding = 3;
/*     */   static final int commentPaddingD = 9;
/*     */ 
/*     */   CommentView(Element paramElement)
/*     */   {
/*  48 */     super(paramElement);
/*     */   }
/*     */ 
/*     */   protected Component createComponent() {
/*  52 */     Container localContainer = getContainer();
/*  53 */     if ((localContainer != null) && (!((JTextComponent)localContainer).isEditable())) {
/*  54 */       return null;
/*     */     }
/*  56 */     JTextArea localJTextArea = new JTextArea(getRepresentedText());
/*  57 */     Document localDocument = getDocument();
/*     */     Font localFont;
/*  59 */     if ((localDocument instanceof StyledDocument)) {
/*  60 */       localFont = ((StyledDocument)localDocument).getFont(getAttributes());
/*  61 */       localJTextArea.setFont(localFont);
/*     */     }
/*     */     else {
/*  64 */       localFont = localJTextArea.getFont();
/*     */     }
/*  66 */     updateYAlign(localFont);
/*  67 */     localJTextArea.setBorder(CBorder);
/*  68 */     localJTextArea.getDocument().addDocumentListener(this);
/*  69 */     localJTextArea.setFocusable(isVisible());
/*  70 */     return localJTextArea;
/*     */   }
/*     */ 
/*     */   void resetBorder()
/*     */   {
/*     */   }
/*     */ 
/*     */   void _updateModelFromText()
/*     */   {
/*  81 */     JTextComponent localJTextComponent = getTextComponent();
/*  82 */     Document localDocument = getDocument();
/*  83 */     if ((localJTextComponent != null) && (localDocument != null)) {
/*  84 */       String str = localJTextComponent.getText();
/*  85 */       SimpleAttributeSet localSimpleAttributeSet = new SimpleAttributeSet();
/*  86 */       this.isSettingAttributes = true;
/*     */       try {
/*  88 */         localSimpleAttributeSet.addAttribute(HTML.Attribute.COMMENT, str);
/*  89 */         ((StyledDocument)localDocument).setCharacterAttributes(getStartOffset(), getEndOffset() - getStartOffset(), localSimpleAttributeSet, false);
/*     */       }
/*     */       finally
/*     */       {
/*  94 */         this.isSettingAttributes = false;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   JTextComponent getTextComponent() {
/* 100 */     return (JTextComponent)getComponent();
/*     */   }
/*     */ 
/*     */   String getRepresentedText() {
/* 104 */     AttributeSet localAttributeSet = getElement().getAttributes();
/* 105 */     if (localAttributeSet != null) {
/* 106 */       Object localObject = localAttributeSet.getAttribute(HTML.Attribute.COMMENT);
/* 107 */       if ((localObject instanceof String)) {
/* 108 */         return (String)localObject;
/*     */       }
/*     */     }
/* 111 */     return "";
/*     */   }
/*     */ 
/*     */   static class CommentBorder extends LineBorder
/*     */   {
/*     */     CommentBorder()
/*     */     {
/* 120 */       super(1);
/*     */     }
/*     */ 
/*     */     public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */     {
/* 125 */       super.paintBorder(paramComponent, paramGraphics, paramInt1 + 3, paramInt2, paramInt3 - 9, paramInt4);
/*     */     }
/*     */ 
/*     */     public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */     {
/* 130 */       Insets localInsets = super.getBorderInsets(paramComponent, paramInsets);
/*     */ 
/* 132 */       localInsets.left += 3;
/* 133 */       localInsets.right += 3;
/* 134 */       return localInsets;
/*     */     }
/*     */ 
/*     */     public boolean isBorderOpaque() {
/* 138 */       return false;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.CommentView
 * JD-Core Version:    0.6.2
 */