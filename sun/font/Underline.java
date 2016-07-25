/*     */ package sun.font;
/*     */ 
/*     */ import java.awt.BasicStroke;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Shape;
/*     */ import java.awt.Stroke;
/*     */ import java.awt.font.TextAttribute;
/*     */ import java.awt.geom.GeneralPath;
/*     */ import java.awt.geom.Line2D.Float;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ abstract class Underline
/*     */ {
/*     */   private static final float DEFAULT_THICKNESS = 1.0F;
/*     */   private static final boolean USE_THICKNESS = true;
/*     */   private static final boolean IGNORE_THICKNESS = false;
/* 262 */   private static final ConcurrentHashMap<Object, Underline> UNDERLINES = new ConcurrentHashMap(6);
/*     */ 
/* 286 */   private static final Underline[] UNDERLINE_LIST = arrayOfUnderline;
/*     */ 
/*     */   abstract void drawUnderline(Graphics2D paramGraphics2D, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
/*     */ 
/*     */   abstract float getLowerDrawLimit(float paramFloat);
/*     */ 
/*     */   abstract Shape getUnderlineShape(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4);
/*     */ 
/*     */   static Underline getUnderline(Object paramObject)
/*     */   {
/* 298 */     if (paramObject == null) {
/* 299 */       return null;
/*     */     }
/*     */ 
/* 302 */     return (Underline)UNDERLINES.get(paramObject);
/*     */   }
/*     */ 
/*     */   static Underline getUnderline(int paramInt) {
/* 306 */     return paramInt < 0 ? null : UNDERLINE_LIST[paramInt];
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 266 */     Underline[] arrayOfUnderline = new Underline[6];
/*     */ 
/* 268 */     arrayOfUnderline[0] = new StandardUnderline(0.0F, 1.0F, null, true);
/* 269 */     UNDERLINES.put(TextAttribute.UNDERLINE_ON, arrayOfUnderline[0]);
/*     */ 
/* 271 */     arrayOfUnderline[1] = new StandardUnderline(1.0F, 1.0F, null, false);
/* 272 */     UNDERLINES.put(TextAttribute.UNDERLINE_LOW_ONE_PIXEL, arrayOfUnderline[1]);
/*     */ 
/* 274 */     arrayOfUnderline[2] = new StandardUnderline(1.0F, 2.0F, null, false);
/* 275 */     UNDERLINES.put(TextAttribute.UNDERLINE_LOW_TWO_PIXEL, arrayOfUnderline[2]);
/*     */ 
/* 277 */     arrayOfUnderline[3] = new StandardUnderline(1.0F, 1.0F, new float[] { 1.0F, 1.0F }, false);
/* 278 */     UNDERLINES.put(TextAttribute.UNDERLINE_LOW_DOTTED, arrayOfUnderline[3]);
/*     */ 
/* 280 */     arrayOfUnderline[4] = new IMGrayUnderline();
/* 281 */     UNDERLINES.put(TextAttribute.UNDERLINE_LOW_GRAY, arrayOfUnderline[4]);
/*     */ 
/* 283 */     arrayOfUnderline[5] = new StandardUnderline(1.0F, 1.0F, new float[] { 4.0F, 4.0F }, false);
/* 284 */     UNDERLINES.put(TextAttribute.UNDERLINE_LOW_DASHED, arrayOfUnderline[5]);
/*     */   }
/*     */ 
/*     */   private static class IMGrayUnderline extends Underline
/*     */   {
/*     */     private BasicStroke stroke;
/*     */ 
/*     */     IMGrayUnderline()
/*     */     {
/* 202 */       this.stroke = new BasicStroke(1.0F, 0, 0, 10.0F, new float[] { 1.0F, 1.0F }, 0.0F);
/*     */     }
/*     */ 
/*     */     void drawUnderline(Graphics2D paramGraphics2D, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
/*     */     {
/* 216 */       Stroke localStroke = paramGraphics2D.getStroke();
/* 217 */       paramGraphics2D.setStroke(this.stroke);
/*     */ 
/* 219 */       Line2D.Float localFloat = new Line2D.Float(paramFloat2, paramFloat4, paramFloat3, paramFloat4);
/* 220 */       paramGraphics2D.draw(localFloat);
/*     */ 
/* 222 */       localFloat.y1 += 1.0F;
/* 223 */       localFloat.y2 += 1.0F;
/* 224 */       localFloat.x1 += 1.0F;
/*     */ 
/* 226 */       paramGraphics2D.draw(localFloat);
/*     */ 
/* 228 */       paramGraphics2D.setStroke(localStroke);
/*     */     }
/*     */ 
/*     */     float getLowerDrawLimit(float paramFloat)
/*     */     {
/* 233 */       return 2.0F;
/*     */     }
/*     */ 
/*     */     Shape getUnderlineShape(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
/*     */     {
/* 241 */       GeneralPath localGeneralPath = new GeneralPath();
/*     */ 
/* 243 */       Line2D.Float localFloat = new Line2D.Float(paramFloat2, paramFloat4, paramFloat3, paramFloat4);
/* 244 */       localGeneralPath.append(this.stroke.createStrokedShape(localFloat), false);
/*     */ 
/* 246 */       localFloat.y1 += 1.0F;
/* 247 */       localFloat.y2 += 1.0F;
/* 248 */       localFloat.x1 += 1.0F;
/*     */ 
/* 250 */       localGeneralPath.append(this.stroke.createStrokedShape(localFloat), false);
/*     */ 
/* 252 */       return localGeneralPath;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class StandardUnderline extends Underline
/*     */   {
/*     */     private float shift;
/*     */     private float thicknessMultiplier;
/*     */     private float[] dashPattern;
/*     */     private boolean useThickness;
/*     */     private BasicStroke cachedStroke;
/*     */ 
/*     */     StandardUnderline(float paramFloat1, float paramFloat2, float[] paramArrayOfFloat, boolean paramBoolean)
/*     */     {
/* 119 */       this.shift = paramFloat1;
/* 120 */       this.thicknessMultiplier = paramFloat2;
/* 121 */       this.dashPattern = paramArrayOfFloat;
/* 122 */       this.useThickness = paramBoolean;
/* 123 */       this.cachedStroke = null;
/*     */     }
/*     */ 
/*     */     private BasicStroke createStroke(float paramFloat)
/*     */     {
/* 128 */       if (this.dashPattern == null) {
/* 129 */         return new BasicStroke(paramFloat, 0, 0);
/*     */       }
/*     */ 
/* 134 */       return new BasicStroke(paramFloat, 0, 0, 10.0F, this.dashPattern, 0.0F);
/*     */     }
/*     */ 
/*     */     private float getLineThickness(float paramFloat)
/*     */     {
/* 145 */       if (this.useThickness) {
/* 146 */         return paramFloat * this.thicknessMultiplier;
/*     */       }
/*     */ 
/* 149 */       return 1.0F * this.thicknessMultiplier;
/*     */     }
/*     */ 
/*     */     private Stroke getStroke(float paramFloat)
/*     */     {
/* 155 */       float f = getLineThickness(paramFloat);
/* 156 */       BasicStroke localBasicStroke = this.cachedStroke;
/* 157 */       if ((localBasicStroke == null) || (localBasicStroke.getLineWidth() != f))
/*     */       {
/* 160 */         localBasicStroke = createStroke(f);
/* 161 */         this.cachedStroke = localBasicStroke;
/*     */       }
/*     */ 
/* 164 */       return localBasicStroke;
/*     */     }
/*     */ 
/*     */     void drawUnderline(Graphics2D paramGraphics2D, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
/*     */     {
/* 174 */       Stroke localStroke = paramGraphics2D.getStroke();
/* 175 */       paramGraphics2D.setStroke(getStroke(paramFloat1));
/* 176 */       paramGraphics2D.draw(new Line2D.Float(paramFloat2, paramFloat4 + this.shift, paramFloat3, paramFloat4 + this.shift));
/* 177 */       paramGraphics2D.setStroke(localStroke);
/*     */     }
/*     */ 
/*     */     float getLowerDrawLimit(float paramFloat)
/*     */     {
/* 182 */       return this.shift + getLineThickness(paramFloat);
/*     */     }
/*     */ 
/*     */     Shape getUnderlineShape(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
/*     */     {
/* 190 */       Stroke localStroke = getStroke(paramFloat1);
/* 191 */       Line2D.Float localFloat = new Line2D.Float(paramFloat2, paramFloat4 + this.shift, paramFloat3, paramFloat4 + this.shift);
/* 192 */       return localStroke.createStrokedShape(localFloat);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.font.Underline
 * JD-Core Version:    0.6.2
 */