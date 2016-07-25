/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Shape;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.LabelView;
/*     */ import javax.swing.text.View;
/*     */ import javax.swing.text.ViewFactory;
/*     */ 
/*     */ public class InlineView extends LabelView
/*     */ {
/*     */   private boolean nowrap;
/*     */   private AttributeSet attr;
/*     */ 
/*     */   public InlineView(Element paramElement)
/*     */   {
/*  46 */     super(paramElement);
/*  47 */     StyleSheet localStyleSheet = getStyleSheet();
/*  48 */     this.attr = localStyleSheet.getViewAttributes(this);
/*     */   }
/*     */ 
/*     */   public void insertUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/*  64 */     super.insertUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/*     */   }
/*     */ 
/*     */   public void removeUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/*  80 */     super.removeUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/*     */   }
/*     */ 
/*     */   public void changedUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/*  93 */     super.changedUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/*  94 */     StyleSheet localStyleSheet = getStyleSheet();
/*  95 */     this.attr = localStyleSheet.getViewAttributes(this);
/*  96 */     preferenceChanged(null, true, true);
/*     */   }
/*     */ 
/*     */   public AttributeSet getAttributes()
/*     */   {
/* 105 */     return this.attr;
/*     */   }
/*     */ 
/*     */   public int getBreakWeight(int paramInt, float paramFloat1, float paramFloat2)
/*     */   {
/* 147 */     if (this.nowrap) {
/* 148 */       return 0;
/*     */     }
/* 150 */     return super.getBreakWeight(paramInt, paramFloat1, paramFloat2);
/*     */   }
/*     */ 
/*     */   public View breakView(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2)
/*     */   {
/* 179 */     return super.breakView(paramInt1, paramInt2, paramFloat1, paramFloat2);
/*     */   }
/*     */ 
/*     */   protected void setPropertiesFromAttributes()
/*     */   {
/* 187 */     super.setPropertiesFromAttributes();
/* 188 */     AttributeSet localAttributeSet = getAttributes();
/* 189 */     Object localObject1 = localAttributeSet.getAttribute(CSS.Attribute.TEXT_DECORATION);
/* 190 */     boolean bool1 = localObject1.toString().indexOf("underline") >= 0;
/*     */ 
/* 192 */     setUnderline(bool1);
/* 193 */     boolean bool2 = localObject1.toString().indexOf("line-through") >= 0;
/*     */ 
/* 195 */     setStrikeThrough(bool2);
/* 196 */     Object localObject2 = localAttributeSet.getAttribute(CSS.Attribute.VERTICAL_ALIGN);
/* 197 */     bool2 = localObject2.toString().indexOf("sup") >= 0;
/* 198 */     setSuperscript(bool2);
/* 199 */     bool2 = localObject2.toString().indexOf("sub") >= 0;
/* 200 */     setSubscript(bool2);
/*     */ 
/* 202 */     Object localObject3 = localAttributeSet.getAttribute(CSS.Attribute.WHITE_SPACE);
/* 203 */     if ((localObject3 != null) && (localObject3.equals("nowrap")))
/* 204 */       this.nowrap = true;
/*     */     else {
/* 206 */       this.nowrap = false;
/*     */     }
/*     */ 
/* 209 */     HTMLDocument localHTMLDocument = (HTMLDocument)getDocument();
/*     */ 
/* 211 */     Color localColor = localHTMLDocument.getBackground(localAttributeSet);
/* 212 */     if (localColor != null)
/* 213 */       setBackground(localColor);
/*     */   }
/*     */ 
/*     */   protected StyleSheet getStyleSheet()
/*     */   {
/* 219 */     HTMLDocument localHTMLDocument = (HTMLDocument)getDocument();
/* 220 */     return localHTMLDocument.getStyleSheet();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.InlineView
 * JD-Core Version:    0.6.2
 */