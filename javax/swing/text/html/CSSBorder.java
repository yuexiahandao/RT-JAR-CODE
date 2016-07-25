/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.Graphics2D;
/*     */ import java.awt.Insets;
/*     */ import java.awt.Polygon;
/*     */ import java.awt.Rectangle;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.swing.border.AbstractBorder;
/*     */ import javax.swing.text.AttributeSet;
/*     */ 
/*     */ class CSSBorder extends AbstractBorder
/*     */ {
/*     */   static final int COLOR = 0;
/*     */   static final int STYLE = 1;
/*     */   static final int WIDTH = 2;
/*     */   static final int TOP = 0;
/*     */   static final int RIGHT = 1;
/*     */   static final int BOTTOM = 2;
/*     */   static final int LEFT = 3;
/*  62 */   static final CSS.Attribute[][] ATTRIBUTES = { { CSS.Attribute.BORDER_TOP_COLOR, CSS.Attribute.BORDER_RIGHT_COLOR, CSS.Attribute.BORDER_BOTTOM_COLOR, CSS.Attribute.BORDER_LEFT_COLOR }, { CSS.Attribute.BORDER_TOP_STYLE, CSS.Attribute.BORDER_RIGHT_STYLE, CSS.Attribute.BORDER_BOTTOM_STYLE, CSS.Attribute.BORDER_LEFT_STYLE }, { CSS.Attribute.BORDER_TOP_WIDTH, CSS.Attribute.BORDER_RIGHT_WIDTH, CSS.Attribute.BORDER_BOTTOM_WIDTH, CSS.Attribute.BORDER_LEFT_WIDTH } };
/*     */ 
/*  72 */   static final CSS.CssValue[] PARSERS = { new CSS.ColorValue(), new CSS.BorderStyle(), new CSS.BorderWidthValue(null, 0) };
/*     */ 
/*  77 */   static final Object[] DEFAULTS = { CSS.Attribute.BORDER_COLOR, PARSERS[1].parseCssValue(CSS.Attribute.BORDER_STYLE.getDefaultValue()), PARSERS[2].parseCssValue(CSS.Attribute.BORDER_WIDTH.getDefaultValue()) };
/*     */   final AttributeSet attrs;
/* 420 */   static Map<CSS.Value, BorderPainter> borderPainters = new HashMap();
/*     */ 
/*     */   CSSBorder(AttributeSet paramAttributeSet)
/*     */   {
/*  90 */     this.attrs = paramAttributeSet;
/*     */   }
/*     */ 
/*     */   private Color getBorderColor(int paramInt)
/*     */   {
/*  97 */     Object localObject = this.attrs.getAttribute(ATTRIBUTES[0][paramInt]);
/*     */     CSS.ColorValue localColorValue;
/*  99 */     if ((localObject instanceof CSS.ColorValue)) {
/* 100 */       localColorValue = (CSS.ColorValue)localObject;
/*     */     }
/*     */     else
/*     */     {
/* 104 */       localColorValue = (CSS.ColorValue)this.attrs.getAttribute(CSS.Attribute.COLOR);
/* 105 */       if (localColorValue == null) {
/* 106 */         localColorValue = (CSS.ColorValue)PARSERS[0].parseCssValue(CSS.Attribute.COLOR.getDefaultValue());
/*     */       }
/*     */     }
/*     */ 
/* 110 */     return localColorValue.getValue();
/*     */   }
/*     */ 
/*     */   private int getBorderWidth(int paramInt)
/*     */   {
/* 117 */     int i = 0;
/* 118 */     CSS.BorderStyle localBorderStyle = (CSS.BorderStyle)this.attrs.getAttribute(ATTRIBUTES[1][paramInt]);
/*     */ 
/* 120 */     if ((localBorderStyle != null) && (localBorderStyle.getValue() != CSS.Value.NONE))
/*     */     {
/* 123 */       CSS.LengthValue localLengthValue = (CSS.LengthValue)this.attrs.getAttribute(ATTRIBUTES[2][paramInt]);
/*     */ 
/* 125 */       if (localLengthValue == null) {
/* 126 */         localLengthValue = (CSS.LengthValue)DEFAULTS[2];
/*     */       }
/* 128 */       i = (int)localLengthValue.getValue(true);
/*     */     }
/* 130 */     return i;
/*     */   }
/*     */ 
/*     */   private int[] getWidths()
/*     */   {
/* 137 */     int[] arrayOfInt = new int[4];
/* 138 */     for (int i = 0; i < arrayOfInt.length; i++) {
/* 139 */       arrayOfInt[i] = getBorderWidth(i);
/*     */     }
/* 141 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   private CSS.Value getBorderStyle(int paramInt)
/*     */   {
/* 148 */     CSS.BorderStyle localBorderStyle = (CSS.BorderStyle)this.attrs.getAttribute(ATTRIBUTES[1][paramInt]);
/*     */ 
/* 150 */     if (localBorderStyle == null) {
/* 151 */       localBorderStyle = (CSS.BorderStyle)DEFAULTS[1];
/*     */     }
/* 153 */     return localBorderStyle.getValue();
/*     */   }
/*     */ 
/*     */   private Polygon getBorderShape(int paramInt)
/*     */   {
/* 161 */     Polygon localPolygon = null;
/* 162 */     int[] arrayOfInt = getWidths();
/* 163 */     if (arrayOfInt[paramInt] != 0) {
/* 164 */       localPolygon = new Polygon(new int[4], new int[4], 0);
/* 165 */       localPolygon.addPoint(0, 0);
/* 166 */       localPolygon.addPoint(-arrayOfInt[((paramInt + 3) % 4)], -arrayOfInt[paramInt]);
/* 167 */       localPolygon.addPoint(arrayOfInt[((paramInt + 1) % 4)], -arrayOfInt[paramInt]);
/* 168 */       localPolygon.addPoint(0, 0);
/*     */     }
/* 170 */     return localPolygon;
/*     */   }
/*     */ 
/*     */   private BorderPainter getBorderPainter(int paramInt)
/*     */   {
/* 177 */     CSS.Value localValue = getBorderStyle(paramInt);
/* 178 */     return (BorderPainter)borderPainters.get(localValue);
/*     */   }
/*     */ 
/*     */   static Color getAdjustedColor(Color paramColor, double paramDouble)
/*     */   {
/* 188 */     double d1 = 1.0D - Math.min(Math.abs(paramDouble), 1.0D);
/* 189 */     double d2 = paramDouble > 0.0D ? 255.0D * (1.0D - d1) : 0.0D;
/* 190 */     return new Color((int)(paramColor.getRed() * d1 + d2), (int)(paramColor.getGreen() * d1 + d2), (int)(paramColor.getBlue() * d1 + d2));
/*     */   }
/*     */ 
/*     */   public Insets getBorderInsets(Component paramComponent, Insets paramInsets)
/*     */   {
/* 199 */     int[] arrayOfInt = getWidths();
/* 200 */     paramInsets.set(arrayOfInt[0], arrayOfInt[3], arrayOfInt[2], arrayOfInt[1]);
/* 201 */     return paramInsets;
/*     */   }
/*     */ 
/*     */   public void paintBorder(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*     */   {
/* 206 */     if (!(paramGraphics instanceof Graphics2D)) {
/* 207 */       return;
/*     */     }
/*     */ 
/* 210 */     Graphics2D localGraphics2D = (Graphics2D)paramGraphics.create();
/*     */ 
/* 212 */     int[] arrayOfInt = getWidths();
/*     */ 
/* 215 */     int i = paramInt1 + arrayOfInt[3];
/* 216 */     int j = paramInt2 + arrayOfInt[0];
/* 217 */     int k = paramInt3 - (arrayOfInt[1] + arrayOfInt[3]);
/* 218 */     int m = paramInt4 - (arrayOfInt[0] + arrayOfInt[2]);
/*     */ 
/* 221 */     int[][] arrayOfInt1 = { { i, j }, { i + k, j }, { i + k, j + m }, { i, j + m } };
/*     */ 
/* 229 */     for (int n = 0; n < 4; n++) {
/* 230 */       CSS.Value localValue = getBorderStyle(n);
/* 231 */       Polygon localPolygon = getBorderShape(n);
/* 232 */       if ((localValue != CSS.Value.NONE) && (localPolygon != null)) {
/* 233 */         int i1 = n % 2 == 0 ? k : m;
/*     */ 
/* 236 */         localPolygon.xpoints[2] += i1;
/* 237 */         localPolygon.xpoints[3] += i1;
/* 238 */         Color localColor = getBorderColor(n);
/* 239 */         BorderPainter localBorderPainter = getBorderPainter(n);
/*     */ 
/* 241 */         double d = n * 3.141592653589793D / 2.0D;
/* 242 */         localGraphics2D.setClip(paramGraphics.getClip());
/* 243 */         localGraphics2D.translate(arrayOfInt1[n][0], arrayOfInt1[n][1]);
/* 244 */         localGraphics2D.rotate(d);
/* 245 */         localGraphics2D.clip(localPolygon);
/* 246 */         localBorderPainter.paint(localPolygon, localGraphics2D, localColor, n);
/* 247 */         localGraphics2D.rotate(-d);
/* 248 */         localGraphics2D.translate(-arrayOfInt1[n][0], -arrayOfInt1[n][1]);
/*     */       }
/*     */     }
/* 251 */     localGraphics2D.dispose();
/*     */   }
/*     */ 
/*     */   static void registerBorderPainter(CSS.Value paramValue, BorderPainter paramBorderPainter)
/*     */   {
/* 416 */     borderPainters.put(paramValue, paramBorderPainter);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 425 */     registerBorderPainter(CSS.Value.NONE, new NullPainter());
/* 426 */     registerBorderPainter(CSS.Value.HIDDEN, new NullPainter());
/* 427 */     registerBorderPainter(CSS.Value.SOLID, new SolidPainter());
/* 428 */     registerBorderPainter(CSS.Value.DOUBLE, new DoublePainter());
/* 429 */     registerBorderPainter(CSS.Value.DOTTED, new DottedDashedPainter(1));
/* 430 */     registerBorderPainter(CSS.Value.DASHED, new DottedDashedPainter(3));
/* 431 */     registerBorderPainter(CSS.Value.GROOVE, new GrooveRidgePainter(CSS.Value.GROOVE));
/* 432 */     registerBorderPainter(CSS.Value.RIDGE, new GrooveRidgePainter(CSS.Value.RIDGE));
/* 433 */     registerBorderPainter(CSS.Value.INSET, new InsetOutsetPainter(CSS.Value.INSET));
/* 434 */     registerBorderPainter(CSS.Value.OUTSET, new InsetOutsetPainter(CSS.Value.OUTSET));
/*     */   }
/*     */ 
/*     */   static abstract interface BorderPainter
/*     */   {
/*     */     public abstract void paint(Polygon paramPolygon, Graphics paramGraphics, Color paramColor, int paramInt);
/*     */   }
/*     */ 
/*     */   static class DottedDashedPainter extends CSSBorder.StrokePainter
/*     */   {
/*     */     final int factor;
/*     */ 
/*     */     DottedDashedPainter(int paramInt)
/*     */     {
/* 342 */       this.factor = paramInt;
/*     */     }
/*     */ 
/*     */     public void paint(Polygon paramPolygon, Graphics paramGraphics, Color paramColor, int paramInt) {
/* 346 */       Rectangle localRectangle = paramPolygon.getBounds();
/* 347 */       int i = localRectangle.height * this.factor;
/* 348 */       int[] arrayOfInt = { i, i };
/* 349 */       Color[] arrayOfColor = { paramColor, null };
/* 350 */       paintStrokes(localRectangle, paramGraphics, 0, arrayOfInt, arrayOfColor);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class DoublePainter extends CSSBorder.StrokePainter
/*     */   {
/*     */     public void paint(Polygon paramPolygon, Graphics paramGraphics, Color paramColor, int paramInt)
/*     */     {
/* 327 */       Rectangle localRectangle = paramPolygon.getBounds();
/* 328 */       int i = Math.max(localRectangle.height / 3, 1);
/* 329 */       int[] arrayOfInt = { i, i };
/* 330 */       Color[] arrayOfColor = { paramColor, null };
/* 331 */       paintStrokes(localRectangle, paramGraphics, 1, arrayOfInt, arrayOfColor);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class GrooveRidgePainter extends CSSBorder.ShadowLightPainter
/*     */   {
/*     */     final CSS.Value type;
/*     */ 
/*     */     GrooveRidgePainter(CSS.Value paramValue)
/*     */     {
/* 380 */       this.type = paramValue;
/*     */     }
/*     */ 
/*     */     public void paint(Polygon paramPolygon, Graphics paramGraphics, Color paramColor, int paramInt) {
/* 384 */       Rectangle localRectangle = paramPolygon.getBounds();
/* 385 */       int i = Math.max(localRectangle.height / 2, 1);
/* 386 */       int[] arrayOfInt = { i, i };
/* 387 */       Color[] arrayOfColor = { getLightColor(paramColor), ((paramInt + 1) % 4 < 2 ? 1 : 0) == (this.type == CSS.Value.GROOVE ? 1 : 0) ? new Color[] { getShadowColor(paramColor), getLightColor(paramColor) } : getShadowColor(paramColor) };
/*     */ 
/* 391 */       paintStrokes(localRectangle, paramGraphics, 1, arrayOfInt, arrayOfColor);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class InsetOutsetPainter extends CSSBorder.ShadowLightPainter
/*     */   {
/*     */     CSS.Value type;
/*     */ 
/*     */     InsetOutsetPainter(CSS.Value paramValue)
/*     */     {
/* 402 */       this.type = paramValue;
/*     */     }
/*     */ 
/*     */     public void paint(Polygon paramPolygon, Graphics paramGraphics, Color paramColor, int paramInt) {
/* 406 */       paramGraphics.setColor(((paramInt + 1) % 4 < 2 ? 1 : 0) == (this.type == CSS.Value.INSET ? 1 : 0) ? getShadowColor(paramColor) : getLightColor(paramColor));
/*     */ 
/* 408 */       paramGraphics.fillPolygon(paramPolygon);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class NullPainter
/*     */     implements CSSBorder.BorderPainter
/*     */   {
/*     */     public void paint(Polygon paramPolygon, Graphics paramGraphics, Color paramColor, int paramInt)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract class ShadowLightPainter extends CSSBorder.StrokePainter
/*     */   {
/*     */     static Color getShadowColor(Color paramColor)
/*     */     {
/* 362 */       return CSSBorder.getAdjustedColor(paramColor, -0.3D);
/*     */     }
/*     */ 
/*     */     static Color getLightColor(Color paramColor)
/*     */     {
/* 369 */       return CSSBorder.getAdjustedColor(paramColor, 0.7D);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class SolidPainter
/*     */     implements CSSBorder.BorderPainter
/*     */   {
/*     */     public void paint(Polygon paramPolygon, Graphics paramGraphics, Color paramColor, int paramInt)
/*     */     {
/* 283 */       paramGraphics.setColor(paramColor);
/* 284 */       paramGraphics.fillPolygon(paramPolygon);
/*     */     }
/*     */   }
/*     */ 
/*     */   static abstract class StrokePainter
/*     */     implements CSSBorder.BorderPainter
/*     */   {
/*     */     void paintStrokes(Rectangle paramRectangle, Graphics paramGraphics, int paramInt, int[] paramArrayOfInt, Color[] paramArrayOfColor)
/*     */     {
/* 298 */       int i = paramInt == 0 ? 1 : 0;
/* 299 */       int j = 0;
/* 300 */       int k = i != 0 ? paramRectangle.width : paramRectangle.height;
/*     */ 
/* 302 */       for (; j < k; 
/* 302 */         goto 42) { int m = 0; if ((m < paramArrayOfInt.length) && 
/* 303 */           (j < k))
/*     */         {
/* 306 */           int n = paramArrayOfInt[m];
/* 307 */           Color localColor = paramArrayOfColor[m];
/* 308 */           if (localColor != null) {
/* 309 */             int i1 = paramRectangle.x + (i != 0 ? j : 0);
/* 310 */             int i2 = paramRectangle.y + (i != 0 ? 0 : j);
/* 311 */             int i3 = i != 0 ? n : paramRectangle.width;
/* 312 */             int i4 = i != 0 ? paramRectangle.height : n;
/* 313 */             paramGraphics.setColor(localColor);
/* 314 */             paramGraphics.fillRect(i1, i2, i3, i4);
/*     */           }
/* 316 */           j += n;
/*     */ 
/* 302 */           m++;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.CSSBorder
 * JD-Core Version:    0.6.2
 */