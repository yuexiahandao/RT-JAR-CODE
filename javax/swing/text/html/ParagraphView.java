/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import javax.swing.SizeRequirements;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.Document;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.JTextComponent;
/*     */ import javax.swing.text.View;
/*     */ 
/*     */ public class ParagraphView extends javax.swing.text.ParagraphView
/*     */ {
/*     */   private AttributeSet attr;
/*     */   private StyleSheet.BoxPainter painter;
/*     */   private CSS.LengthValue cssWidth;
/*     */   private CSS.LengthValue cssHeight;
/*     */ 
/*     */   public ParagraphView(Element paramElement)
/*     */   {
/*  54 */     super(paramElement);
/*     */   }
/*     */ 
/*     */   public void setParent(View paramView)
/*     */   {
/*  75 */     super.setParent(paramView);
/*  76 */     if (paramView != null)
/*  77 */       setPropertiesFromAttributes();
/*     */   }
/*     */ 
/*     */   public AttributeSet getAttributes()
/*     */   {
/*  87 */     if (this.attr == null) {
/*  88 */       StyleSheet localStyleSheet = getStyleSheet();
/*  89 */       this.attr = localStyleSheet.getViewAttributes(this);
/*     */     }
/*  91 */     return this.attr;
/*     */   }
/*     */ 
/*     */   protected void setPropertiesFromAttributes()
/*     */   {
/* 100 */     StyleSheet localStyleSheet = getStyleSheet();
/* 101 */     this.attr = localStyleSheet.getViewAttributes(this);
/* 102 */     this.painter = localStyleSheet.getBoxPainter(this.attr);
/* 103 */     if (this.attr != null) {
/* 104 */       super.setPropertiesFromAttributes();
/* 105 */       setInsets((short)(int)this.painter.getInset(1, this), (short)(int)this.painter.getInset(2, this), (short)(int)this.painter.getInset(3, this), (short)(int)this.painter.getInset(4, this));
/*     */ 
/* 109 */       Object localObject = this.attr.getAttribute(CSS.Attribute.TEXT_ALIGN);
/* 110 */       if (localObject != null)
/*     */       {
/* 112 */         String str = localObject.toString();
/* 113 */         if (str.equals("left"))
/* 114 */           setJustification(0);
/* 115 */         else if (str.equals("center"))
/* 116 */           setJustification(1);
/* 117 */         else if (str.equals("right"))
/* 118 */           setJustification(2);
/* 119 */         else if (str.equals("justify")) {
/* 120 */           setJustification(3);
/*     */         }
/*     */       }
/*     */ 
/* 124 */       this.cssWidth = ((CSS.LengthValue)this.attr.getAttribute(CSS.Attribute.WIDTH));
/*     */ 
/* 126 */       this.cssHeight = ((CSS.LengthValue)this.attr.getAttribute(CSS.Attribute.HEIGHT));
/*     */     }
/*     */   }
/*     */ 
/*     */   protected StyleSheet getStyleSheet()
/*     */   {
/* 132 */     HTMLDocument localHTMLDocument = (HTMLDocument)getDocument();
/* 133 */     return localHTMLDocument.getStyleSheet();
/*     */   }
/*     */ 
/*     */   protected SizeRequirements calculateMinorAxisRequirements(int paramInt, SizeRequirements paramSizeRequirements)
/*     */   {
/* 157 */     paramSizeRequirements = super.calculateMinorAxisRequirements(paramInt, paramSizeRequirements);
/*     */ 
/* 159 */     if (BlockView.spanSetFromAttributes(paramInt, paramSizeRequirements, this.cssWidth, this.cssHeight))
/*     */     {
/* 162 */       int i = paramInt == 0 ? getLeftInset() + getRightInset() : getTopInset() + getBottomInset();
/*     */ 
/* 164 */       paramSizeRequirements.minimum -= i;
/* 165 */       paramSizeRequirements.preferred -= i;
/* 166 */       paramSizeRequirements.maximum -= i;
/*     */     }
/* 168 */     return paramSizeRequirements;
/*     */   }
/*     */ 
/*     */   public boolean isVisible()
/*     */   {
/* 184 */     int i = getLayoutViewCount() - 1;
/*     */     Object localObject;
/* 185 */     for (int j = 0; j < i; j++) {
/* 186 */       localObject = getLayoutView(j);
/* 187 */       if (((View)localObject).isVisible()) {
/* 188 */         return true;
/*     */       }
/*     */     }
/* 191 */     if (i > 0) {
/* 192 */       View localView = getLayoutView(i);
/* 193 */       if (localView.getEndOffset() - localView.getStartOffset() == 1) {
/* 194 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 199 */     if (getStartOffset() == getDocument().getLength()) {
/* 200 */       boolean bool = false;
/* 201 */       localObject = getContainer();
/* 202 */       if ((localObject instanceof JTextComponent)) {
/* 203 */         bool = ((JTextComponent)localObject).isEditable();
/*     */       }
/* 205 */       if (!bool) {
/* 206 */         return false;
/*     */       }
/*     */     }
/* 209 */     return true;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, Shape paramShape)
/*     */   {
/* 222 */     if (paramShape == null)
/*     */       return;
/*     */     Rectangle localRectangle;
/* 227 */     if ((paramShape instanceof Rectangle))
/* 228 */       localRectangle = (Rectangle)paramShape;
/*     */     else {
/* 230 */       localRectangle = paramShape.getBounds();
/*     */     }
/* 232 */     this.painter.paint(paramGraphics, localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height, this);
/* 233 */     super.paint(paramGraphics, paramShape);
/*     */   }
/*     */ 
/*     */   public float getPreferredSpan(int paramInt)
/*     */   {
/* 250 */     if (!isVisible()) {
/* 251 */       return 0.0F;
/*     */     }
/* 253 */     return super.getPreferredSpan(paramInt);
/*     */   }
/*     */ 
/*     */   public float getMinimumSpan(int paramInt)
/*     */   {
/* 267 */     if (!isVisible()) {
/* 268 */       return 0.0F;
/*     */     }
/* 270 */     return super.getMinimumSpan(paramInt);
/*     */   }
/*     */ 
/*     */   public float getMaximumSpan(int paramInt)
/*     */   {
/* 284 */     if (!isVisible()) {
/* 285 */       return 0.0F;
/*     */     }
/* 287 */     return super.getMaximumSpan(paramInt);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.ParagraphView
 * JD-Core Version:    0.6.2
 */