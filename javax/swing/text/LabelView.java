/*     */ package javax.swing.text;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Toolkit;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ 
/*     */ public class LabelView extends GlyphView
/*     */   implements TabableView
/*     */ {
/*     */   private Font font;
/*     */   private Color fg;
/*     */   private Color bg;
/*     */   private boolean underline;
/*     */   private boolean strike;
/*     */   private boolean superscript;
/*     */   private boolean subscript;
/*     */ 
/*     */   public LabelView(Element paramElement)
/*     */   {
/*  46 */     super(paramElement);
/*     */   }
/*     */ 
/*     */   final void sync()
/*     */   {
/*  55 */     if (this.font == null)
/*  56 */       setPropertiesFromAttributes();
/*     */   }
/*     */ 
/*     */   protected void setUnderline(boolean paramBoolean)
/*     */   {
/*  71 */     this.underline = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected void setStrikeThrough(boolean paramBoolean)
/*     */   {
/*  86 */     this.strike = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected void setSuperscript(boolean paramBoolean)
/*     */   {
/* 102 */     this.superscript = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected void setSubscript(boolean paramBoolean)
/*     */   {
/* 117 */     this.subscript = paramBoolean;
/*     */   }
/*     */ 
/*     */   protected void setBackground(Color paramColor)
/*     */   {
/* 133 */     this.bg = paramColor;
/*     */   }
/*     */ 
/*     */   protected void setPropertiesFromAttributes()
/*     */   {
/* 140 */     AttributeSet localAttributeSet = getAttributes();
/* 141 */     if (localAttributeSet != null) {
/* 142 */       Document localDocument = getDocument();
/* 143 */       if ((localDocument instanceof StyledDocument)) {
/* 144 */         StyledDocument localStyledDocument = (StyledDocument)localDocument;
/* 145 */         this.font = localStyledDocument.getFont(localAttributeSet);
/* 146 */         this.fg = localStyledDocument.getForeground(localAttributeSet);
/* 147 */         if (localAttributeSet.isDefined(StyleConstants.Background))
/* 148 */           this.bg = localStyledDocument.getBackground(localAttributeSet);
/*     */         else {
/* 150 */           this.bg = null;
/*     */         }
/* 152 */         setUnderline(StyleConstants.isUnderline(localAttributeSet));
/* 153 */         setStrikeThrough(StyleConstants.isStrikeThrough(localAttributeSet));
/* 154 */         setSuperscript(StyleConstants.isSuperscript(localAttributeSet));
/* 155 */         setSubscript(StyleConstants.isSubscript(localAttributeSet));
/*     */       } else {
/* 157 */         throw new StateInvariantError("LabelView needs StyledDocument");
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected FontMetrics getFontMetrics()
/*     */   {
/* 169 */     sync();
/* 170 */     Container localContainer = getContainer();
/* 171 */     return localContainer != null ? localContainer.getFontMetrics(this.font) : Toolkit.getDefaultToolkit().getFontMetrics(this.font);
/*     */   }
/*     */ 
/*     */   public Color getBackground()
/*     */   {
/* 184 */     sync();
/* 185 */     return this.bg;
/*     */   }
/*     */ 
/*     */   public Color getForeground()
/*     */   {
/* 197 */     sync();
/* 198 */     return this.fg;
/*     */   }
/*     */ 
/*     */   public Font getFont()
/*     */   {
/* 208 */     sync();
/* 209 */     return this.font;
/*     */   }
/*     */ 
/*     */   public boolean isUnderline()
/*     */   {
/* 228 */     sync();
/* 229 */     return this.underline;
/*     */   }
/*     */ 
/*     */   public boolean isStrikeThrough()
/*     */   {
/* 249 */     sync();
/* 250 */     return this.strike;
/*     */   }
/*     */ 
/*     */   public boolean isSubscript()
/*     */   {
/* 268 */     sync();
/* 269 */     return this.subscript;
/*     */   }
/*     */ 
/*     */   public boolean isSuperscript()
/*     */   {
/* 286 */     sync();
/* 287 */     return this.superscript;
/*     */   }
/*     */ 
/*     */   public void changedUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/* 302 */     this.font = null;
/* 303 */     super.changedUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.LabelView
 * JD-Core Version:    0.6.2
 */