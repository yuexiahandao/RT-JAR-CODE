/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Container;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import javax.swing.event.DocumentEvent;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.BadLocationException;
/*     */ import javax.swing.text.Element;
/*     */ import javax.swing.text.Position.Bias;
/*     */ import javax.swing.text.StyleConstants;
/*     */ import javax.swing.text.View;
/*     */ import javax.swing.text.ViewFactory;
/*     */ 
/*     */ class HRuleView extends View
/*     */ {
/*     */   private float topMargin;
/*     */   private float bottomMargin;
/*     */   private float leftMargin;
/*     */   private float rightMargin;
/* 311 */   private int alignment = 1;
/* 312 */   private String noshade = null;
/* 313 */   private int size = 0;
/*     */   private CSS.LengthValue widthValue;
/*     */   private static final int SPACE_ABOVE = 3;
/*     */   private static final int SPACE_BELOW = 3;
/*     */   private AttributeSet attr;
/*     */ 
/*     */   public HRuleView(Element paramElement)
/*     */   {
/*  48 */     super(paramElement);
/*  49 */     setPropertiesFromAttributes();
/*     */   }
/*     */ 
/*     */   protected void setPropertiesFromAttributes()
/*     */   {
/*  56 */     StyleSheet localStyleSheet = ((HTMLDocument)getDocument()).getStyleSheet();
/*  57 */     AttributeSet localAttributeSet = getElement().getAttributes();
/*  58 */     this.attr = localStyleSheet.getViewAttributes(this);
/*     */ 
/*  60 */     this.alignment = 1;
/*  61 */     this.size = 0;
/*  62 */     this.noshade = null;
/*  63 */     this.widthValue = null;
/*     */ 
/*  65 */     if (this.attr != null)
/*     */     {
/*  69 */       if (this.attr.getAttribute(StyleConstants.Alignment) != null) {
/*  70 */         this.alignment = StyleConstants.getAlignment(this.attr);
/*     */       }
/*     */ 
/*  73 */       this.noshade = ((String)localAttributeSet.getAttribute(HTML.Attribute.NOSHADE));
/*  74 */       Object localObject = localAttributeSet.getAttribute(HTML.Attribute.SIZE);
/*  75 */       if ((localObject != null) && ((localObject instanceof String))) {
/*     */         try {
/*  77 */           this.size = Integer.parseInt((String)localObject);
/*     */         } catch (NumberFormatException localNumberFormatException) {
/*  79 */           this.size = 1;
/*     */         }
/*     */       }
/*  82 */       localObject = this.attr.getAttribute(CSS.Attribute.WIDTH);
/*  83 */       if ((localObject != null) && ((localObject instanceof CSS.LengthValue))) {
/*  84 */         this.widthValue = ((CSS.LengthValue)localObject);
/*     */       }
/*  86 */       this.topMargin = getLength(CSS.Attribute.MARGIN_TOP, this.attr);
/*  87 */       this.bottomMargin = getLength(CSS.Attribute.MARGIN_BOTTOM, this.attr);
/*  88 */       this.leftMargin = getLength(CSS.Attribute.MARGIN_LEFT, this.attr);
/*  89 */       this.rightMargin = getLength(CSS.Attribute.MARGIN_RIGHT, this.attr);
/*     */     }
/*     */     else {
/*  92 */       this.topMargin = (this.bottomMargin = this.leftMargin = this.rightMargin = 0.0F);
/*     */     }
/*  94 */     this.size = Math.max(2, this.size);
/*     */   }
/*     */ 
/*     */   private float getLength(CSS.Attribute paramAttribute, AttributeSet paramAttributeSet)
/*     */   {
/* 100 */     CSS.LengthValue localLengthValue = (CSS.LengthValue)paramAttributeSet.getAttribute(paramAttribute);
/* 101 */     float f = localLengthValue != null ? localLengthValue.getValue() : 0.0F;
/* 102 */     return f;
/*     */   }
/*     */ 
/*     */   public void paint(Graphics paramGraphics, Shape paramShape)
/*     */   {
/* 115 */     Rectangle localRectangle = (paramShape instanceof Rectangle) ? (Rectangle)paramShape : paramShape.getBounds();
/*     */ 
/* 117 */     int i = 0;
/* 118 */     int j = localRectangle.y + 3 + (int)this.topMargin;
/* 119 */     int k = localRectangle.width - (int)(this.leftMargin + this.rightMargin);
/* 120 */     if (this.widthValue != null) {
/* 121 */       k = (int)this.widthValue.getValue(k);
/*     */     }
/* 123 */     int m = localRectangle.height - (6 + (int)this.topMargin + (int)this.bottomMargin);
/*     */ 
/* 125 */     if (this.size > 0) {
/* 126 */       m = this.size;
/*     */     }
/*     */ 
/* 129 */     switch (this.alignment) {
/*     */     case 1:
/* 131 */       i = localRectangle.x + localRectangle.width / 2 - k / 2;
/* 132 */       break;
/*     */     case 2:
/* 134 */       i = localRectangle.x + localRectangle.width - k - (int)this.rightMargin;
/* 135 */       break;
/*     */     case 0:
/*     */     default:
/* 138 */       i = localRectangle.x + (int)this.leftMargin;
/*     */     }
/*     */ 
/* 143 */     if (this.noshade != null) {
/* 144 */       paramGraphics.setColor(Color.black);
/* 145 */       paramGraphics.fillRect(i, j, k, m);
/*     */     }
/*     */     else {
/* 148 */       Color localColor1 = getContainer().getBackground();
/*     */       Color localColor3;
/*     */       Color localColor2;
/* 150 */       if ((localColor1 == null) || (localColor1.equals(Color.white))) {
/* 151 */         localColor3 = Color.darkGray;
/* 152 */         localColor2 = Color.lightGray;
/*     */       }
/*     */       else {
/* 155 */         localColor3 = Color.darkGray;
/* 156 */         localColor2 = Color.white;
/*     */       }
/* 158 */       paramGraphics.setColor(localColor2);
/* 159 */       paramGraphics.drawLine(i + k - 1, j, i + k - 1, j + m - 1);
/* 160 */       paramGraphics.drawLine(i, j + m - 1, i + k - 1, j + m - 1);
/* 161 */       paramGraphics.setColor(localColor3);
/* 162 */       paramGraphics.drawLine(i, j, i + k - 1, j);
/* 163 */       paramGraphics.drawLine(i, j, i, j + m - 1);
/*     */     }
/*     */   }
/*     */ 
/*     */   public float getPreferredSpan(int paramInt)
/*     */   {
/* 178 */     switch (paramInt) {
/*     */     case 0:
/* 180 */       return 1.0F;
/*     */     case 1:
/* 182 */       if (this.size > 0) {
/* 183 */         return this.size + 3 + 3 + this.topMargin + this.bottomMargin;
/*     */       }
/*     */ 
/* 186 */       if (this.noshade != null) {
/* 187 */         return 8.0F + this.topMargin + this.bottomMargin;
/*     */       }
/*     */ 
/* 190 */       return 6.0F + this.topMargin + this.bottomMargin;
/*     */     }
/*     */ 
/* 194 */     throw new IllegalArgumentException("Invalid axis: " + paramInt);
/*     */   }
/*     */ 
/*     */   public int getResizeWeight(int paramInt)
/*     */   {
/* 206 */     if (paramInt == 0)
/* 207 */       return 1;
/* 208 */     if (paramInt == 1) {
/* 209 */       return 0;
/*     */     }
/* 211 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getBreakWeight(int paramInt, float paramFloat1, float paramFloat2)
/*     */   {
/* 231 */     if (paramInt == 0) {
/* 232 */       return 3000;
/*     */     }
/* 234 */     return 0;
/*     */   }
/*     */ 
/*     */   public View breakView(int paramInt1, int paramInt2, float paramFloat1, float paramFloat2) {
/* 238 */     return null;
/*     */   }
/*     */ 
/*     */   public Shape modelToView(int paramInt, Shape paramShape, Position.Bias paramBias)
/*     */     throws BadLocationException
/*     */   {
/* 253 */     int i = getStartOffset();
/* 254 */     int j = getEndOffset();
/* 255 */     if ((paramInt >= i) && (paramInt <= j)) {
/* 256 */       Rectangle localRectangle = paramShape.getBounds();
/* 257 */       if (paramInt == j) {
/* 258 */         localRectangle.x += localRectangle.width;
/*     */       }
/* 260 */       localRectangle.width = 0;
/* 261 */       return localRectangle;
/*     */     }
/* 263 */     return null;
/*     */   }
/*     */ 
/*     */   public int viewToModel(float paramFloat1, float paramFloat2, Shape paramShape, Position.Bias[] paramArrayOfBias)
/*     */   {
/* 278 */     Rectangle localRectangle = (Rectangle)paramShape;
/* 279 */     if (paramFloat1 < localRectangle.x + localRectangle.width / 2) {
/* 280 */       paramArrayOfBias[0] = Position.Bias.Forward;
/* 281 */       return getStartOffset();
/*     */     }
/* 283 */     paramArrayOfBias[0] = Position.Bias.Backward;
/* 284 */     return getEndOffset();
/*     */   }
/*     */ 
/*     */   public AttributeSet getAttributes()
/*     */   {
/* 293 */     return this.attr;
/*     */   }
/*     */ 
/*     */   public void changedUpdate(DocumentEvent paramDocumentEvent, Shape paramShape, ViewFactory paramViewFactory) {
/* 297 */     super.changedUpdate(paramDocumentEvent, paramShape, paramViewFactory);
/* 298 */     int i = paramDocumentEvent.getOffset();
/* 299 */     if ((i <= getStartOffset()) && (i + paramDocumentEvent.getLength() >= getEndOffset()))
/*     */     {
/* 301 */       setPropertiesFromAttributes();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.HRuleView
 * JD-Core Version:    0.6.2
 */