/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Paint;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import java.awt.geom.Area;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.Line2D.Float;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.awt.geom.Rectangle2D.Float;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class Decoration
/*     */ {
/*  86 */   private static final int VALUES_MASK = AttributeValues.getMask(new EAttribute[] { EAttribute.EFOREGROUND, EAttribute.EBACKGROUND, EAttribute.ESWAP_COLORS, EAttribute.ESTRIKETHROUGH, EAttribute.EUNDERLINE, EAttribute.EINPUT_METHOD_HIGHLIGHT, EAttribute.EINPUT_METHOD_UNDERLINE });
/*     */ 
/* 142 */   private static final Decoration PLAIN = new Decoration();
/*     */ 
/*     */   public static Decoration getPlainDecoration()
/*     */   {
/*  83 */     return PLAIN;
/*     */   }
/*     */ 
/*     */   public static Decoration getDecoration(AttributeValues paramAttributeValues)
/*     */   {
/*  92 */     if ((paramAttributeValues == null) || (!paramAttributeValues.anyDefined(VALUES_MASK))) {
/*  93 */       return PLAIN;
/*     */     }
/*     */ 
/*  96 */     paramAttributeValues = paramAttributeValues.applyIMHighlight();
/*     */ 
/*  98 */     return new DecorationImpl(paramAttributeValues.getForeground(), paramAttributeValues.getBackground(), paramAttributeValues.getSwapColors(), paramAttributeValues.getStrikethrough(), Underline.getUnderline(paramAttributeValues.getUnderline()), Underline.getUnderline(paramAttributeValues.getInputMethodUnderline()));
/*     */   }
/*     */ 
/*     */   public static Decoration getDecoration(Map paramMap)
/*     */   {
/* 111 */     if (paramMap == null) {
/* 112 */       return PLAIN;
/*     */     }
/* 114 */     return getDecoration(AttributeValues.fromMap(paramMap));
/*     */   }
/*     */ 
/*     */   public void drawTextAndDecorations(Label paramLabel, Graphics2D paramGraphics2D, float paramFloat1, float paramFloat2)
/*     */   {
/* 122 */     paramLabel.handleDraw(paramGraphics2D, paramFloat1, paramFloat2);
/*     */   }
/*     */ 
/*     */   public Rectangle2D getVisualBounds(Label paramLabel)
/*     */   {
/* 127 */     return paramLabel.handleGetVisualBounds();
/*     */   }
/*     */ 
/*     */   public Rectangle2D getCharVisualBounds(Label paramLabel, int paramInt)
/*     */   {
/* 132 */     return paramLabel.handleGetCharVisualBounds(paramInt);
/*     */   }
/*     */ 
/*     */   Shape getOutline(Label paramLabel, float paramFloat1, float paramFloat2)
/*     */   {
/* 139 */     return paramLabel.handleGetOutline(paramFloat1, paramFloat2);
/*     */   }
/*     */ 
/*     */   private static final class DecorationImpl extends Decoration
/*     */   {
/* 146 */     private Paint fgPaint = null;
/* 147 */     private Paint bgPaint = null;
/* 148 */     private boolean swapColors = false;
/* 149 */     private boolean strikethrough = false;
/* 150 */     private Underline stdUnderline = null;
/* 151 */     private Underline imUnderline = null;
/*     */ 
/*     */     DecorationImpl(Paint paramPaint1, Paint paramPaint2, boolean paramBoolean1, boolean paramBoolean2, Underline paramUnderline1, Underline paramUnderline2)
/*     */     {
/* 158 */       super();
/*     */ 
/* 160 */       this.fgPaint = paramPaint1;
/* 161 */       this.bgPaint = paramPaint2;
/*     */ 
/* 163 */       this.swapColors = paramBoolean1;
/* 164 */       this.strikethrough = paramBoolean2;
/*     */ 
/* 166 */       this.stdUnderline = paramUnderline1;
/* 167 */       this.imUnderline = paramUnderline2;
/*     */     }
/*     */ 
/*     */     private static boolean areEqual(Object paramObject1, Object paramObject2)
/*     */     {
/* 172 */       if (paramObject1 == null) {
/* 173 */         return paramObject2 == null;
/*     */       }
/*     */ 
/* 176 */       return paramObject1.equals(paramObject2);
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 182 */       if (paramObject == this) {
/* 183 */         return true;
/*     */       }
/* 185 */       if (paramObject == null) {
/* 186 */         return false;
/*     */       }
/*     */ 
/* 189 */       DecorationImpl localDecorationImpl = null;
/*     */       try {
/* 191 */         localDecorationImpl = (DecorationImpl)paramObject;
/*     */       }
/*     */       catch (ClassCastException localClassCastException) {
/* 194 */         return false;
/*     */       }
/*     */ 
/* 197 */       if ((this.swapColors != localDecorationImpl.swapColors) || (this.strikethrough != localDecorationImpl.strikethrough))
/*     */       {
/* 199 */         return false;
/*     */       }
/*     */ 
/* 202 */       if (!areEqual(this.stdUnderline, localDecorationImpl.stdUnderline)) {
/* 203 */         return false;
/*     */       }
/* 205 */       if (!areEqual(this.fgPaint, localDecorationImpl.fgPaint)) {
/* 206 */         return false;
/*     */       }
/* 208 */       if (!areEqual(this.bgPaint, localDecorationImpl.bgPaint)) {
/* 209 */         return false;
/*     */       }
/* 211 */       return areEqual(this.imUnderline, localDecorationImpl.imUnderline);
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 216 */       int i = 1;
/* 217 */       if (this.strikethrough) {
/* 218 */         i |= 2;
/*     */       }
/* 220 */       if (this.swapColors) {
/* 221 */         i |= 4;
/*     */       }
/* 223 */       if (this.stdUnderline != null) {
/* 224 */         i += this.stdUnderline.hashCode();
/*     */       }
/* 226 */       return i;
/*     */     }
/*     */ 
/*     */     private float getUnderlineMaxY(CoreMetrics paramCoreMetrics)
/*     */     {
/* 235 */       float f1 = 0.0F;
/*     */       float f2;
/* 236 */       if (this.stdUnderline != null)
/*     */       {
/* 238 */         f2 = paramCoreMetrics.underlineOffset;
/* 239 */         f2 += this.stdUnderline.getLowerDrawLimit(paramCoreMetrics.underlineThickness);
/* 240 */         f1 = Math.max(f1, f2);
/*     */       }
/*     */ 
/* 243 */       if (this.imUnderline != null)
/*     */       {
/* 245 */         f2 = paramCoreMetrics.underlineOffset;
/* 246 */         f2 += this.imUnderline.getLowerDrawLimit(paramCoreMetrics.underlineThickness);
/* 247 */         f1 = Math.max(f1, f2);
/*     */       }
/*     */ 
/* 250 */       return f1;
/*     */     }
/*     */ 
/*     */     private void drawTextAndEmbellishments(Decoration.Label paramLabel, Graphics2D paramGraphics2D, float paramFloat1, float paramFloat2)
/*     */     {
/* 258 */       paramLabel.handleDraw(paramGraphics2D, paramFloat1, paramFloat2);
/*     */ 
/* 260 */       if ((!this.strikethrough) && (this.stdUnderline == null) && (this.imUnderline == null)) {
/* 261 */         return;
/*     */       }
/*     */ 
/* 264 */       float f1 = paramFloat1;
/* 265 */       float f2 = f1 + (float)paramLabel.getLogicalBounds().getWidth();
/*     */ 
/* 267 */       CoreMetrics localCoreMetrics = paramLabel.getCoreMetrics();
/* 268 */       if (this.strikethrough) {
/* 269 */         Stroke localStroke = paramGraphics2D.getStroke();
/* 270 */         paramGraphics2D.setStroke(new BasicStroke(localCoreMetrics.strikethroughThickness, 0, 0));
/*     */ 
/* 273 */         f4 = paramFloat2 + localCoreMetrics.strikethroughOffset;
/* 274 */         paramGraphics2D.draw(new Line2D.Float(f1, f4, f2, f4));
/* 275 */         paramGraphics2D.setStroke(localStroke);
/*     */       }
/*     */ 
/* 278 */       float f3 = localCoreMetrics.underlineOffset;
/* 279 */       float f4 = localCoreMetrics.underlineThickness;
/*     */ 
/* 281 */       if (this.stdUnderline != null) {
/* 282 */         this.stdUnderline.drawUnderline(paramGraphics2D, f4, f1, f2, paramFloat2 + f3);
/*     */       }
/*     */ 
/* 285 */       if (this.imUnderline != null)
/* 286 */         this.imUnderline.drawUnderline(paramGraphics2D, f4, f1, f2, paramFloat2 + f3);
/*     */     }
/*     */ 
/*     */     public void drawTextAndDecorations(Decoration.Label paramLabel, Graphics2D paramGraphics2D, float paramFloat1, float paramFloat2)
/*     */     {
/* 295 */       if ((this.fgPaint == null) && (this.bgPaint == null) && (!this.swapColors)) {
/* 296 */         drawTextAndEmbellishments(paramLabel, paramGraphics2D, paramFloat1, paramFloat2);
/*     */       }
/*     */       else {
/* 299 */         Paint localPaint1 = paramGraphics2D.getPaint();
/*     */         Paint localPaint2;
/*     */         Object localObject2;
/*     */         Object localObject1;
/* 302 */         if (this.swapColors) {
/* 303 */           localPaint2 = this.fgPaint == null ? localPaint1 : this.fgPaint;
/* 304 */           if (this.bgPaint == null) {
/* 305 */             if ((localPaint2 instanceof Color)) {
/* 306 */               localObject2 = (Color)localPaint2;
/*     */ 
/* 308 */               int i = 33 * ((Color)localObject2).getRed() + 53 * ((Color)localObject2).getGreen() + 14 * ((Color)localObject2).getBlue();
/*     */ 
/* 311 */               localObject1 = i > 18500 ? Color.BLACK : Color.WHITE;
/*     */             } else {
/* 313 */               localObject1 = Color.WHITE;
/*     */             }
/*     */           }
/* 316 */           else localObject1 = this.bgPaint;
/*     */         }
/*     */         else
/*     */         {
/* 320 */           localObject1 = this.fgPaint == null ? localPaint1 : this.fgPaint;
/* 321 */           localPaint2 = this.bgPaint;
/*     */         }
/*     */ 
/* 324 */         if (localPaint2 != null)
/*     */         {
/* 326 */           localObject2 = paramLabel.getLogicalBounds();
/* 327 */           localObject2 = new Rectangle2D.Float(paramFloat1 + (float)((Rectangle2D)localObject2).getX(), paramFloat2 + (float)((Rectangle2D)localObject2).getY(), (float)((Rectangle2D)localObject2).getWidth(), (float)((Rectangle2D)localObject2).getHeight());
/*     */ 
/* 332 */           paramGraphics2D.setPaint(localPaint2);
/* 333 */           paramGraphics2D.fill((Shape)localObject2);
/*     */         }
/*     */ 
/* 336 */         paramGraphics2D.setPaint((Paint)localObject1);
/* 337 */         drawTextAndEmbellishments(paramLabel, paramGraphics2D, paramFloat1, paramFloat2);
/* 338 */         paramGraphics2D.setPaint(localPaint1);
/*     */       }
/*     */     }
/*     */ 
/*     */     public Rectangle2D getVisualBounds(Decoration.Label paramLabel)
/*     */     {
/* 344 */       Rectangle2D localRectangle2D1 = paramLabel.handleGetVisualBounds();
/*     */ 
/* 346 */       if ((this.swapColors) || (this.bgPaint != null) || (this.strikethrough) || (this.stdUnderline != null) || (this.imUnderline != null))
/*     */       {
/* 349 */         float f1 = 0.0F;
/* 350 */         Rectangle2D localRectangle2D2 = paramLabel.getLogicalBounds();
/*     */ 
/* 352 */         float f2 = 0.0F; float f3 = 0.0F;
/*     */ 
/* 354 */         if ((this.swapColors) || (this.bgPaint != null))
/*     */         {
/* 356 */           f2 = (float)localRectangle2D2.getY();
/* 357 */           f3 = f2 + (float)localRectangle2D2.getHeight();
/*     */         }
/*     */ 
/* 360 */         f3 = Math.max(f3, getUnderlineMaxY(paramLabel.getCoreMetrics()));
/*     */ 
/* 362 */         Rectangle2D.Float localFloat = new Rectangle2D.Float(f1, f2, (float)localRectangle2D2.getWidth(), f3 - f2);
/* 363 */         localRectangle2D1.add(localFloat);
/*     */       }
/*     */ 
/* 366 */       return localRectangle2D1;
/*     */     }
/*     */ 
/*     */     Shape getOutline(Decoration.Label paramLabel, float paramFloat1, float paramFloat2)
/*     */     {
/* 373 */       if ((!this.strikethrough) && (this.stdUnderline == null) && (this.imUnderline == null)) {
/* 374 */         return paramLabel.handleGetOutline(paramFloat1, paramFloat2);
/*     */       }
/*     */ 
/* 377 */       CoreMetrics localCoreMetrics = paramLabel.getCoreMetrics();
/*     */ 
/* 381 */       float f1 = localCoreMetrics.underlineThickness;
/* 382 */       float f2 = localCoreMetrics.underlineOffset;
/*     */ 
/* 384 */       Rectangle2D localRectangle2D = paramLabel.getLogicalBounds();
/* 385 */       float f3 = paramFloat1;
/* 386 */       float f4 = f3 + (float)localRectangle2D.getWidth();
/*     */ 
/* 388 */       Object localObject1 = null;
/*     */       Object localObject2;
/* 390 */       if (this.stdUnderline != null) {
/* 391 */         localObject2 = this.stdUnderline.getUnderlineShape(f1, f3, f4, paramFloat2 + f2);
/*     */ 
/* 393 */         localObject1 = new Area((Shape)localObject2);
/*     */       }
/*     */ 
/* 396 */       if (this.strikethrough) {
/* 397 */         localObject2 = new BasicStroke(localCoreMetrics.strikethroughThickness, 0, 0);
/*     */ 
/* 400 */         float f5 = paramFloat2 + localCoreMetrics.strikethroughOffset;
/* 401 */         Line2D.Float localFloat = new Line2D.Float(f3, f5, f4, f5);
/* 402 */         Area localArea2 = new Area(((Stroke)localObject2).createStrokedShape(localFloat));
/* 403 */         if (localObject1 == null)
/* 404 */           localObject1 = localArea2;
/*     */         else {
/* 406 */           ((Area)localObject1).add(localArea2);
/*     */         }
/*     */       }
/*     */ 
/* 410 */       if (this.imUnderline != null) {
/* 411 */         localObject2 = this.imUnderline.getUnderlineShape(f1, f3, f4, paramFloat2 + f2);
/*     */ 
/* 413 */         Area localArea1 = new Area((Shape)localObject2);
/* 414 */         if (localObject1 == null) {
/* 415 */           localObject1 = localArea1;
/*     */         }
/*     */         else {
/* 418 */           ((Area)localObject1).add(localArea1);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 423 */       ((Area)localObject1).add(new Area(paramLabel.handleGetOutline(paramFloat1, paramFloat2)));
/*     */ 
/* 425 */       return new GeneralPath((Shape)localObject1);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 430 */       StringBuffer localStringBuffer = new StringBuffer();
/* 431 */       localStringBuffer.append(super.toString());
/* 432 */       localStringBuffer.append("[");
/* 433 */       if (this.fgPaint != null) localStringBuffer.append("fgPaint: " + this.fgPaint);
/* 434 */       if (this.bgPaint != null) localStringBuffer.append(" bgPaint: " + this.bgPaint);
/* 435 */       if (this.swapColors) localStringBuffer.append(" swapColors: true");
/* 436 */       if (this.strikethrough) localStringBuffer.append(" strikethrough: true");
/* 437 */       if (this.stdUnderline != null) localStringBuffer.append(" stdUnderline: " + this.stdUnderline);
/* 438 */       if (this.imUnderline != null) localStringBuffer.append(" imUnderline: " + this.imUnderline);
/* 439 */       localStringBuffer.append("]");
/* 440 */       return localStringBuffer.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface Label
/*     */   {
/*     */     public abstract CoreMetrics getCoreMetrics();
/*     */ 
/*     */     public abstract Rectangle2D getLogicalBounds();
/*     */ 
/*     */     public abstract void handleDraw(Graphics2D paramGraphics2D, float paramFloat1, float paramFloat2);
/*     */ 
/*     */     public abstract Rectangle2D handleGetCharVisualBounds(int paramInt);
/*     */ 
/*     */     public abstract Rectangle2D handleGetVisualBounds();
/*     */ 
/*     */     public abstract Shape handleGetOutline(float paramFloat1, float paramFloat2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.Decoration
 * JD-Core Version:    0.6.2
 */