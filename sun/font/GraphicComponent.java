/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Rectangle;
/*     */ import java.awt.Shape;
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.font.GlyphJustificationInfo;
/*     */ import java.awt.font.GraphicAttribute;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.Rectangle2D.Float;
/*     */ 
/*     */ public final class GraphicComponent
/*     */   implements TextLineComponent, Decoration.Label
/*     */ {
/*     */   public static final float GRAPHIC_LEADING = 2.0F;
/*     */   private GraphicAttribute graphic;
/*     */   private int graphicCount;
/*     */   private int[] charsLtoV;
/*     */   private byte[] levels;
/*  58 */   private Rectangle2D visualBounds = null;
/*     */   private float graphicAdvance;
/*     */   private AffineTransform baseTx;
/*     */   private CoreMetrics cm;
/*     */   private Decoration decorator;
/*     */ 
/*     */   public GraphicComponent(GraphicAttribute paramGraphicAttribute, Decoration paramDecoration, int[] paramArrayOfInt, byte[] paramArrayOfByte, int paramInt1, int paramInt2, AffineTransform paramAffineTransform)
/*     */   {
/*  81 */     if (paramInt2 <= paramInt1) {
/*  82 */       throw new IllegalArgumentException("0 or negative length in GraphicComponent");
/*     */     }
/*  84 */     this.graphic = paramGraphicAttribute;
/*  85 */     this.graphicAdvance = paramGraphicAttribute.getAdvance();
/*  86 */     this.decorator = paramDecoration;
/*  87 */     this.cm = createCoreMetrics(paramGraphicAttribute);
/*  88 */     this.baseTx = paramAffineTransform;
/*     */ 
/*  90 */     initLocalOrdering(paramArrayOfInt, paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   private GraphicComponent(GraphicComponent paramGraphicComponent, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*  95 */     this.graphic = paramGraphicComponent.graphic;
/*  96 */     this.graphicAdvance = paramGraphicComponent.graphicAdvance;
/*  97 */     this.decorator = paramGraphicComponent.decorator;
/*  98 */     this.cm = paramGraphicComponent.cm;
/*  99 */     this.baseTx = paramGraphicComponent.baseTx;
/*     */ 
/* 101 */     int[] arrayOfInt = null;
/* 102 */     byte[] arrayOfByte = null;
/*     */ 
/* 104 */     if (paramInt3 == 2) {
/* 105 */       arrayOfInt = paramGraphicComponent.charsLtoV;
/* 106 */       arrayOfByte = paramGraphicComponent.levels;
/*     */     }
/* 108 */     else if ((paramInt3 == 0) || (paramInt3 == 1)) {
/* 109 */       paramInt2 -= paramInt1;
/* 110 */       paramInt1 = 0;
/* 111 */       if (paramInt3 == 1) {
/* 112 */         arrayOfInt = new int[paramInt2];
/* 113 */         arrayOfByte = new byte[paramInt2];
/* 114 */         for (int i = 0; i < paramInt2; i++) {
/* 115 */           arrayOfInt[i] = (paramInt2 - i - 1);
/* 116 */           arrayOfByte[i] = 1;
/*     */         }
/*     */       }
/*     */     }
/*     */     else {
/* 121 */       throw new IllegalArgumentException("Invalid direction flag");
/*     */     }
/*     */ 
/* 124 */     initLocalOrdering(arrayOfInt, arrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   private void initLocalOrdering(int[] paramArrayOfInt, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 135 */     this.graphicCount = (paramInt2 - paramInt1);
/*     */ 
/* 137 */     if ((paramArrayOfInt == null) || (paramArrayOfInt.length == this.graphicCount)) {
/* 138 */       this.charsLtoV = paramArrayOfInt;
/*     */     }
/*     */     else {
/* 141 */       this.charsLtoV = BidiUtils.createNormalizedMap(paramArrayOfInt, paramArrayOfByte, paramInt1, paramInt2);
/*     */     }
/*     */ 
/* 144 */     if ((paramArrayOfByte == null) || (paramArrayOfByte.length == this.graphicCount)) {
/* 145 */       this.levels = paramArrayOfByte;
/*     */     }
/*     */     else {
/* 148 */       this.levels = new byte[this.graphicCount];
/* 149 */       System.arraycopy(paramArrayOfByte, paramInt1, this.levels, 0, this.graphicCount);
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isSimple() {
/* 154 */     return false;
/*     */   }
/*     */ 
/*     */   public Rectangle getPixelBounds(FontRenderContext paramFontRenderContext, float paramFloat1, float paramFloat2) {
/* 158 */     throw new InternalError("do not call if isSimple returns false");
/*     */   }
/*     */ 
/*     */   public Rectangle2D handleGetVisualBounds()
/*     */   {
/* 163 */     Rectangle2D localRectangle2D = this.graphic.getBounds();
/*     */ 
/* 165 */     float f = (float)localRectangle2D.getWidth() + this.graphicAdvance * (this.graphicCount - 1);
/*     */ 
/* 168 */     return new Rectangle2D.Float((float)localRectangle2D.getX(), (float)localRectangle2D.getY(), f, (float)localRectangle2D.getHeight());
/*     */   }
/*     */ 
/*     */   public CoreMetrics getCoreMetrics()
/*     */   {
/* 175 */     return this.cm;
/*     */   }
/*     */ 
/*     */   public static CoreMetrics createCoreMetrics(GraphicAttribute paramGraphicAttribute) {
/* 179 */     return new CoreMetrics(paramGraphicAttribute.getAscent(), paramGraphicAttribute.getDescent(), 2.0F, paramGraphicAttribute.getAscent() + paramGraphicAttribute.getDescent() + 2.0F, paramGraphicAttribute.getAlignment(), new float[] { 0.0F, -paramGraphicAttribute.getAscent() / 2.0F, -paramGraphicAttribute.getAscent() }, -paramGraphicAttribute.getAscent() / 2.0F, paramGraphicAttribute.getAscent() / 12.0F, paramGraphicAttribute.getDescent() / 3.0F, paramGraphicAttribute.getAscent() / 12.0F, 0.0F, 0.0F);
/*     */   }
/*     */ 
/*     */   public float getItalicAngle()
/*     */   {
/* 195 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   public Rectangle2D getVisualBounds()
/*     */   {
/* 200 */     if (this.visualBounds == null) {
/* 201 */       this.visualBounds = this.decorator.getVisualBounds(this);
/*     */     }
/* 203 */     Rectangle2D.Float localFloat = new Rectangle2D.Float();
/* 204 */     localFloat.setRect(this.visualBounds);
/* 205 */     return localFloat;
/*     */   }
/*     */ 
/*     */   public Shape handleGetOutline(float paramFloat1, float paramFloat2) {
/* 209 */     double[] arrayOfDouble = { 1.0D, 0.0D, 0.0D, 1.0D, paramFloat1, paramFloat2 };
/*     */ 
/* 211 */     if (this.graphicCount == 1) {
/* 212 */       localObject = new AffineTransform(arrayOfDouble);
/* 213 */       return this.graphic.getOutline((AffineTransform)localObject);
/*     */     }
/*     */ 
/* 216 */     Object localObject = new GeneralPath();
/* 217 */     for (int i = 0; i < this.graphicCount; i++) {
/* 218 */       AffineTransform localAffineTransform = new AffineTransform(arrayOfDouble);
/* 219 */       ((GeneralPath)localObject).append(this.graphic.getOutline(localAffineTransform), false);
/* 220 */       arrayOfDouble[4] += this.graphicAdvance;
/*     */     }
/*     */ 
/* 223 */     return localObject;
/*     */   }
/*     */ 
/*     */   public AffineTransform getBaselineTransform() {
/* 227 */     return this.baseTx;
/*     */   }
/*     */ 
/*     */   public Shape getOutline(float paramFloat1, float paramFloat2)
/*     */   {
/* 232 */     return this.decorator.getOutline(this, paramFloat1, paramFloat2);
/*     */   }
/*     */ 
/*     */   public void handleDraw(Graphics2D paramGraphics2D, float paramFloat1, float paramFloat2)
/*     */   {
/* 237 */     for (int i = 0; i < this.graphicCount; i++)
/*     */     {
/* 239 */       this.graphic.draw(paramGraphics2D, paramFloat1, paramFloat2);
/* 240 */       paramFloat1 += this.graphicAdvance;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void draw(Graphics2D paramGraphics2D, float paramFloat1, float paramFloat2)
/*     */   {
/* 246 */     this.decorator.drawTextAndDecorations(this, paramGraphics2D, paramFloat1, paramFloat2);
/*     */   }
/*     */ 
/*     */   public Rectangle2D getCharVisualBounds(int paramInt)
/*     */   {
/* 251 */     return this.decorator.getCharVisualBounds(this, paramInt);
/*     */   }
/*     */ 
/*     */   public int getNumCharacters()
/*     */   {
/* 256 */     return this.graphicCount;
/*     */   }
/*     */ 
/*     */   public float getCharX(int paramInt)
/*     */   {
/* 261 */     int i = this.charsLtoV == null ? paramInt : this.charsLtoV[paramInt];
/* 262 */     return this.graphicAdvance * i;
/*     */   }
/*     */ 
/*     */   public float getCharY(int paramInt)
/*     */   {
/* 267 */     return 0.0F;
/*     */   }
/*     */ 
/*     */   public float getCharAdvance(int paramInt)
/*     */   {
/* 272 */     return this.graphicAdvance;
/*     */   }
/*     */ 
/*     */   public boolean caretAtOffsetIsValid(int paramInt)
/*     */   {
/* 277 */     return true;
/*     */   }
/*     */ 
/*     */   public Rectangle2D handleGetCharVisualBounds(int paramInt)
/*     */   {
/* 282 */     Rectangle2D localRectangle2D = this.graphic.getBounds();
/*     */ 
/* 285 */     Rectangle2D.Float localFloat = new Rectangle2D.Float();
/* 286 */     localFloat.setRect(localRectangle2D);
/* 287 */     localFloat.x += this.graphicAdvance * paramInt;
/*     */ 
/* 289 */     return localFloat;
/*     */   }
/*     */ 
/*     */   public int getLineBreakIndex(int paramInt, float paramFloat)
/*     */   {
/* 295 */     int i = (int)(paramFloat / this.graphicAdvance);
/* 296 */     if (i > this.graphicCount - paramInt) {
/* 297 */       i = this.graphicCount - paramInt;
/*     */     }
/* 299 */     return i;
/*     */   }
/*     */ 
/*     */   public float getAdvanceBetween(int paramInt1, int paramInt2)
/*     */   {
/* 305 */     return this.graphicAdvance * (paramInt2 - paramInt1);
/*     */   }
/*     */ 
/*     */   public Rectangle2D getLogicalBounds()
/*     */   {
/* 310 */     float f1 = 0.0F;
/* 311 */     float f2 = -this.cm.ascent;
/* 312 */     float f3 = this.graphicAdvance * this.graphicCount;
/* 313 */     float f4 = this.cm.descent - f2;
/*     */ 
/* 315 */     return new Rectangle2D.Float(f1, f2, f3, f4);
/*     */   }
/*     */ 
/*     */   public float getAdvance() {
/* 319 */     return this.graphicAdvance * this.graphicCount;
/*     */   }
/*     */ 
/*     */   public Rectangle2D getItalicBounds() {
/* 323 */     return getLogicalBounds();
/*     */   }
/*     */ 
/*     */   public TextLineComponent getSubset(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 328 */     if ((paramInt1 < 0) || (paramInt2 > this.graphicCount) || (paramInt1 >= paramInt2)) {
/* 329 */       throw new IllegalArgumentException("Invalid range.  start=" + paramInt1 + "; limit=" + paramInt2);
/*     */     }
/*     */ 
/* 333 */     if ((paramInt1 == 0) && (paramInt2 == this.graphicCount) && (paramInt3 == 2)) {
/* 334 */       return this;
/*     */     }
/*     */ 
/* 337 */     return new GraphicComponent(this, paramInt1, paramInt2, paramInt3);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 342 */     return "[graphic=" + this.graphic + ":count=" + getNumCharacters() + "]";
/*     */   }
/*     */ 
/*     */   public int getNumJustificationInfos()
/*     */   {
/* 349 */     return 0;
/*     */   }
/*     */ 
/*     */   public void getJustificationInfos(GlyphJustificationInfo[] paramArrayOfGlyphJustificationInfo, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/*     */   }
/*     */ 
/*     */   public TextLineComponent applyJustificationDeltas(float[] paramArrayOfFloat, int paramInt, boolean[] paramArrayOfBoolean)
/*     */   {
/* 374 */     return this;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.GraphicComponent
 * JD-Core Version:    0.6.2
 */