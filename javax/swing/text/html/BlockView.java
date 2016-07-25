/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import javax.swing.SizeRequirements;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.BoxView;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.View;
/*     */ import javax.swing.text.ViewFactory;
/*     */ 
/*     */ public class BlockView extends BoxView
/*     */ {
/*     */   private AttributeSet attr;
/*     */   private StyleSheet.BoxPainter painter;
/*     */   private CSS.LengthValue cssWidth;
/*     */   private CSS.LengthValue cssHeight;
/*     */ 
/*     */   public BlockView(Element paramElement, int paramInt)
/*     */   {
/*  51 */     super(paramElement, paramInt);
/*     */   }
/*     */ 
/*     */   public void setParent(View paramView)
/*     */   {
/*  72 */     super.setParent(paramView);
/*  73 */     if (paramView != null)
/*  74 */       setPropertiesFromAttributes();
/*     */   }
/*     */ 
/*     */   protected SizeRequirements calculateMajorAxisRequirements(int paramInt, SizeRequirements paramSizeRequirements)
/*     */   {
/*  86 */     if (paramSizeRequirements == null) {
/*  87 */       paramSizeRequirements = new SizeRequirements();
/*     */     }
/*  89 */     if (!spanSetFromAttributes(paramInt, paramSizeRequirements, this.cssWidth, this.cssHeight)) {
/*  90 */       paramSizeRequirements = super.calculateMajorAxisRequirements(paramInt, paramSizeRequirements);
/*     */     }
/*     */     else
/*     */     {
/*  95 */       SizeRequirements localSizeRequirements = super.calculateMajorAxisRequirements(paramInt, null);
/*     */ 
/*  97 */       int i = paramInt == 0 ? getLeftInset() + getRightInset() : getTopInset() + getBottomInset();
/*     */ 
/*  99 */       paramSizeRequirements.minimum -= i;
/* 100 */       paramSizeRequirements.preferred -= i;
/* 101 */       paramSizeRequirements.maximum -= i;
/* 102 */       constrainSize(paramInt, paramSizeRequirements, localSizeRequirements);
/*     */     }
/* 104 */     return paramSizeRequirements;
/*     */   }
/*     */ 
/*     */   protected SizeRequirements calculateMinorAxisRequirements(int paramInt, SizeRequirements paramSizeRequirements)
/*     */   {
/* 116 */     if (paramSizeRequirements == null)
/* 117 */       paramSizeRequirements = new SizeRequirements();
/*     */     Object localObject;
/* 120 */     if (!spanSetFromAttributes(paramInt, paramSizeRequirements, this.cssWidth, this.cssHeight))
/*     */     {
/* 146 */       paramSizeRequirements = super.calculateMinorAxisRequirements(paramInt, paramSizeRequirements);
/*     */     }
/*     */     else
/*     */     {
/* 151 */       localObject = super.calculateMinorAxisRequirements(paramInt, null);
/*     */ 
/* 153 */       int i = paramInt == 0 ? getLeftInset() + getRightInset() : getTopInset() + getBottomInset();
/*     */ 
/* 155 */       paramSizeRequirements.minimum -= i;
/* 156 */       paramSizeRequirements.preferred -= i;
/* 157 */       paramSizeRequirements.maximum -= i;
/* 158 */       constrainSize(paramInt, paramSizeRequirements, (SizeRequirements)localObject);
/*     */     }
/*     */ 
/* 166 */     if (paramInt == 0) {
/* 167 */       localObject = getAttributes().getAttribute(CSS.Attribute.TEXT_ALIGN);
/* 168 */       if (localObject != null) {
/* 169 */         String str = localObject.toString();
/* 170 */         if (str.equals("center"))
/* 171 */           paramSizeRequirements.alignment = 0.5F;
/* 172 */         else if (str.equals("right"))
/* 173 */           paramSizeRequirements.alignment = 1.0F;
/*     */         else {
/* 175 */           paramSizeRequirements.alignment = 0.0F;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 180 */     return paramSizeRequirements;
/*     */   }
/*     */ 
/*     */   boolean isPercentage(int paramInt, AttributeSet paramAttributeSet) {
/* 184 */     if (paramInt == 0) {
/* 185 */       if (this.cssWidth != null) {
/* 186 */         return this.cssWidth.isPercentage();
/*     */       }
/*     */     }
/* 189 */     else if (this.cssHeight != null) {
/* 190 */       return this.cssHeight.isPercentage();
/*     */     }
/*     */ 
/* 193 */     return false;
/*     */   }
/*     */ 
/*     */   static boolean spanSetFromAttributes(int paramInt, SizeRequirements paramSizeRequirements, CSS.LengthValue paramLengthValue1, CSS.LengthValue paramLengthValue2)
/*     */   {
/* 205 */     if (paramInt == 0) {
/* 206 */       if ((paramLengthValue1 != null) && (!paramLengthValue1.isPercentage())) {
/* 207 */         paramSizeRequirements.minimum = (paramSizeRequirements.preferred = paramSizeRequirements.maximum = (int)paramLengthValue1.getValue());
/* 208 */         return true;
/*     */       }
/*     */     }
/* 211 */     else if ((paramLengthValue2 != null) && (!paramLengthValue2.isPercentage())) {
/* 212 */       paramSizeRequirements.minimum = (paramSizeRequirements.preferred = paramSizeRequirements.maximum = (int)paramLengthValue2.getValue());
/* 213 */       return true;
/*     */     }
/*     */ 
/* 216 */     return false;
/*     */   }
/*     */ 
/*     */   protected void layoutMinorAxis(int paramInt1, int paramInt2, int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*     */   {
/* 236 */     int i = getViewCount();
/* 237 */     CSS.Attribute localAttribute = paramInt2 == 0 ? CSS.Attribute.WIDTH : CSS.Attribute.HEIGHT;
/* 238 */     for (int j = 0; j < i; j++) {
/* 239 */       View localView = getView(j);
/* 240 */       int k = (int)localView.getMinimumSpan(paramInt2);
/*     */ 
/* 244 */       AttributeSet localAttributeSet = localView.getAttributes();
/* 245 */       CSS.LengthValue localLengthValue = (CSS.LengthValue)localAttributeSet.getAttribute(localAttribute);
/*     */       int m;
/* 246 */       if ((localLengthValue != null) && (localLengthValue.isPercentage()))
/*     */       {
/* 248 */         k = Math.max((int)localLengthValue.getValue(paramInt1), k);
/* 249 */         m = k;
/*     */       } else {
/* 251 */         m = (int)localView.getMaximumSpan(paramInt2);
/*     */       }
/*     */ 
/* 255 */       if (m < paramInt1)
/*     */       {
/* 257 */         float f = localView.getAlignment(paramInt2);
/* 258 */         paramArrayOfInt1[j] = ((int)((paramInt1 - m) * f));
/* 259 */         paramArrayOfInt2[j] = m;
/*     */       }
/*     */       else {
/* 262 */         paramArrayOfInt1[j] = 0;
/* 263 */         paramArrayOfInt2[j] = Math.max(k, paramInt1);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, Shape paramShape)
/*     */   {
/* 280 */     Rectangle localRectangle = (Rectangle)paramShape;
/* 281 */     this.painter.paint(paramGraphics, localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height, this);
/* 282 */     super.paint(paramGraphics, localRectangle);
/*     */   }
/*     */ 
/*     */   public AttributeSet getAttributes()
/*     */   {
/* 291 */     if (this.attr == null) {
/* 292 */       StyleSheet localStyleSheet = getStyleSheet();
/* 293 */       this.attr = localStyleSheet.getViewAttributes(this);
/*     */     }
/* 295 */     return this.attr;
/*     */   }
/*     */ 
/*     */   public int getResizeWeight(int paramInt)
/*     */   {
/* 306 */     switch (paramInt) {
/*     */     case 0:
/* 308 */       return 1;
/*     */     case 1:
/* 310 */       return 0;
/*     */     }
/* 312 */     throw new IllegalArgumentException("Invalid axis: " + paramInt);
/*     */   }
/*     */ 
/*     */   public float getAlignment(int paramInt)
/*     */   {
/* 323 */     switch (paramInt) {
/*     */     case 0:
/* 325 */       return 0.0F;
/*     */     case 1:
/* 327 */       if (getViewCount() == 0) {
/* 328 */         return 0.0F;
/*     */       }
/* 330 */       float f1 = getPreferredSpan(1);
/* 331 */       View localView = getView(0);
/* 332 */       float f2 = localView.getPreferredSpan(1);
/* 333 */       float f3 = (int)f1 != 0 ? f2 * localView.getAlignment(1) / f1 : 0.0F;
/* 334 */       return f3;
/*     */     }
/* 336 */     throw new IllegalArgumentException("Invalid axis: " + paramInt);
/*     */   }
/*     */ 
/*     */   public void changedUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory)
/*     */   {
/* 341 */     super.changedUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/* 342 */     int i = paramDocumentEvent.getOffset();
/* 343 */     if ((i <= getStartOffset()) && (i + paramDocumentEvent.getLength() >= getEndOffset()))
/*     */     {
/* 345 */       setPropertiesFromAttributes();
/*     */     }
/*     */   }
/*     */ 
/*     */   public float getPreferredSpan(int paramInt)
/*     */   {
/* 362 */     return super.getPreferredSpan(paramInt);
/*     */   }
/*     */ 
/*     */   public float getMinimumSpan(int paramInt)
/*     */   {
/* 378 */     return super.getMinimumSpan(paramInt);
/*     */   }
/*     */ 
/*     */   public float getMaximumSpan(int paramInt)
/*     */   {
/* 394 */     return super.getMaximumSpan(paramInt);
/*     */   }
/*     */ 
/*     */   protected void setPropertiesFromAttributes()
/*     */   {
/* 403 */     StyleSheet localStyleSheet = getStyleSheet();
/* 404 */     this.attr = localStyleSheet.getViewAttributes(this);
/*     */ 
/* 407 */     this.painter = localStyleSheet.getBoxPainter(this.attr);
/* 408 */     if (this.attr != null) {
/* 409 */       setInsets((short)(int)this.painter.getInset(1, this), (short)(int)this.painter.getInset(2, this), (short)(int)this.painter.getInset(3, this), (short)(int)this.painter.getInset(4, this));
/*     */     }
/*     */ 
/* 416 */     this.cssWidth = ((CSS.LengthValue)this.attr.getAttribute(CSS.Attribute.WIDTH));
/* 417 */     this.cssHeight = ((CSS.LengthValue)this.attr.getAttribute(CSS.Attribute.HEIGHT));
/*     */   }
/*     */ 
/*     */   protected StyleSheet getStyleSheet() {
/* 421 */     HTMLDocument localHTMLDocument = (HTMLDocument)getDocument();
/* 422 */     return localHTMLDocument.getStyleSheet();
/*     */   }
/*     */ 
/*     */   private void constrainSize(int paramInt, SizeRequirements paramSizeRequirements1, SizeRequirements paramSizeRequirements2)
/*     */   {
/* 431 */     if (paramSizeRequirements2.minimum > paramSizeRequirements1.minimum) {
/* 432 */       paramSizeRequirements1.minimum = (paramSizeRequirements1.preferred = paramSizeRequirements2.minimum);
/* 433 */       paramSizeRequirements1.maximum = Math.max(paramSizeRequirements1.maximum, paramSizeRequirements2.maximum);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.BlockView
 * JD-Core Version:    0.6.2
 */