/*      */ package sun.java2d;
/*      */ 
/*      */ import java.awt.AlphaComposite;
/*      */ import java.awt.BasicStroke;
/*      */ import java.awt.Color;
/*      */ import java.awt.Composite;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.GradientPaint;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.GraphicsConfiguration;
/*      */ import java.awt.Image;
/*      */ import java.awt.LinearGradientPaint;
/*      */ import java.awt.Paint;
/*      */ import java.awt.RadialGradientPaint;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.RenderingHints;
/*      */ import java.awt.RenderingHints.Key;
/*      */ import java.awt.Shape;
/*      */ import java.awt.Stroke;
/*      */ import java.awt.TexturePaint;
/*      */ import java.awt.font.FontRenderContext;
/*      */ import java.awt.font.GlyphVector;
/*      */ import java.awt.font.TextLayout;
/*      */ import java.awt.geom.AffineTransform;
/*      */ import java.awt.geom.Area;
/*      */ import java.awt.geom.GeneralPath;
/*      */ import java.awt.geom.NoninvertibleTransformException;
/*      */ import java.awt.geom.PathIterator;
/*      */ import java.awt.geom.Rectangle2D;
/*      */ import java.awt.geom.Rectangle2D.Double;
/*      */ import java.awt.geom.Rectangle2D.Float;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.BufferedImageOp;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.ImageObserver;
/*      */ import java.awt.image.Raster;
/*      */ import java.awt.image.RenderedImage;
/*      */ import java.awt.image.WritableRaster;
/*      */ import java.awt.image.renderable.RenderContext;
/*      */ import java.awt.image.renderable.RenderableImage;
/*      */ import java.text.AttributedCharacterIterator;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import sun.awt.ConstrainableGraphics;
/*      */ import sun.awt.SunHints;
/*      */ import sun.awt.SunHints.Key;
/*      */ import sun.awt.SunHints.Value;
/*      */ import sun.awt.image.SurfaceManager;
/*      */ import sun.font.Font2D;
/*      */ import sun.font.FontDesignMetrics;
/*      */ import sun.font.FontUtilities;
/*      */ import sun.java2d.loops.Blit;
/*      */ import sun.java2d.loops.CompositeType;
/*      */ import sun.java2d.loops.FontInfo;
/*      */ import sun.java2d.loops.MaskFill;
/*      */ import sun.java2d.loops.RenderLoops;
/*      */ import sun.java2d.loops.SurfaceType;
/*      */ import sun.java2d.loops.XORComposite;
/*      */ import sun.java2d.pipe.DrawImagePipe;
/*      */ import sun.java2d.pipe.LoopPipe;
/*      */ import sun.java2d.pipe.PixelDrawPipe;
/*      */ import sun.java2d.pipe.PixelFillPipe;
/*      */ import sun.java2d.pipe.Region;
/*      */ import sun.java2d.pipe.RenderingEngine;
/*      */ import sun.java2d.pipe.ShapeDrawPipe;
/*      */ import sun.java2d.pipe.ShapeSpanIterator;
/*      */ import sun.java2d.pipe.TextPipe;
/*      */ import sun.java2d.pipe.ValidatePipe;
/*      */ import sun.misc.PerformanceLogger;
/*      */ 
/*      */ public final class SunGraphics2D extends Graphics2D
/*      */   implements ConstrainableGraphics, Cloneable, DestSurfaceProvider
/*      */ {
/*      */   public static final int PAINT_CUSTOM = 6;
/*      */   public static final int PAINT_TEXTURE = 5;
/*      */   public static final int PAINT_RAD_GRADIENT = 4;
/*      */   public static final int PAINT_LIN_GRADIENT = 3;
/*      */   public static final int PAINT_GRADIENT = 2;
/*      */   public static final int PAINT_ALPHACOLOR = 1;
/*      */   public static final int PAINT_OPAQUECOLOR = 0;
/*      */   public static final int COMP_CUSTOM = 3;
/*      */   public static final int COMP_XOR = 2;
/*      */   public static final int COMP_ALPHA = 1;
/*      */   public static final int COMP_ISCOPY = 0;
/*      */   public static final int STROKE_CUSTOM = 3;
/*      */   public static final int STROKE_WIDE = 2;
/*      */   public static final int STROKE_THINDASHED = 1;
/*      */   public static final int STROKE_THIN = 0;
/*      */   public static final int TRANSFORM_GENERIC = 4;
/*      */   public static final int TRANSFORM_TRANSLATESCALE = 3;
/*      */   public static final int TRANSFORM_ANY_TRANSLATE = 2;
/*      */   public static final int TRANSFORM_INT_TRANSLATE = 1;
/*      */   public static final int TRANSFORM_ISIDENT = 0;
/*      */   public static final int CLIP_SHAPE = 2;
/*      */   public static final int CLIP_RECTANGULAR = 1;
/*      */   public static final int CLIP_DEVICE = 0;
/*      */   public int eargb;
/*      */   public int pixel;
/*      */   public SurfaceData surfaceData;
/*      */   public PixelDrawPipe drawpipe;
/*      */   public PixelFillPipe fillpipe;
/*      */   public DrawImagePipe imagepipe;
/*      */   public ShapeDrawPipe shapepipe;
/*      */   public TextPipe textpipe;
/*      */   public MaskFill alphafill;
/*      */   public RenderLoops loops;
/*      */   public CompositeType imageComp;
/*      */   public int paintState;
/*      */   public int compositeState;
/*      */   public int strokeState;
/*      */   public int transformState;
/*      */   public int clipState;
/*      */   public Color foregroundColor;
/*      */   public Color backgroundColor;
/*      */   public AffineTransform transform;
/*      */   public int transX;
/*      */   public int transY;
/*  178 */   protected static final Stroke defaultStroke = new BasicStroke();
/*  179 */   protected static final Composite defaultComposite = AlphaComposite.SrcOver;
/*  180 */   private static final Font defaultFont = new Font("Dialog", 0, 12);
/*      */   public Paint paint;
/*      */   public Stroke stroke;
/*      */   public Composite composite;
/*      */   protected Font font;
/*      */   protected FontMetrics fontMetrics;
/*      */   public int renderHint;
/*      */   public int antialiasHint;
/*      */   public int textAntialiasHint;
/*      */   protected int fractionalMetricsHint;
/*      */   public int lcdTextContrast;
/*  196 */   private static int lcdTextContrastDefaultValue = 140;
/*      */   private int interpolationHint;
/*      */   public int strokeHint;
/*      */   public int interpolationType;
/*      */   public RenderingHints hints;
/*      */   public Region constrainClip;
/*      */   public int constrainX;
/*      */   public int constrainY;
/*      */   public Region clipRegion;
/*      */   public Shape usrClip;
/*      */   protected Region devClip;
/*      */   private final int devScale;
/*      */   private boolean validFontInfo;
/*      */   private FontInfo fontInfo;
/*      */   private FontInfo glyphVectorFontInfo;
/*      */   private FontRenderContext glyphVectorFRC;
/*      */   private static final int slowTextTransformMask = 120;
/*      */   protected static ValidatePipe invalidpipe;
/*      */   private static final double[] IDENT_MATRIX;
/*      */   private static final AffineTransform IDENT_ATX;
/*      */   private static final int MINALLOCATED = 8;
/*      */   private static final int TEXTARRSIZE = 17;
/*      */   private static double[][] textTxArr;
/*      */   private static AffineTransform[] textAtArr;
/*      */   static final int NON_UNIFORM_SCALE_MASK = 36;
/* 1001 */   public static final double MinPenSizeAA = RenderingEngine.getInstance().getMinimumAAPenSize();
/*      */ 
/* 1003 */   public static final double MinPenSizeAASquared = MinPenSizeAA * MinPenSizeAA;
/*      */   public static final double MinPenSizeSquared = 1.000000001D;
/*      */   static final int NON_RECTILINEAR_TRANSFORM_MASK = 48;
/*      */   Blit lastCAblit;
/*      */   Composite lastCAcomp;
/*      */   private FontRenderContext cachedFRC;
/*      */ 
/*      */   public SunGraphics2D(SurfaceData paramSurfaceData, Color paramColor1, Color paramColor2, Font paramFont)
/*      */   {
/*  234 */     this.surfaceData = paramSurfaceData;
/*  235 */     this.foregroundColor = paramColor1;
/*  236 */     this.backgroundColor = paramColor2;
/*      */ 
/*  238 */     this.transform = new AffineTransform();
/*  239 */     this.stroke = defaultStroke;
/*  240 */     this.composite = defaultComposite;
/*  241 */     this.paint = this.foregroundColor;
/*      */ 
/*  243 */     this.imageComp = CompositeType.SrcOverNoEa;
/*      */ 
/*  245 */     this.renderHint = 0;
/*  246 */     this.antialiasHint = 1;
/*  247 */     this.textAntialiasHint = 0;
/*  248 */     this.fractionalMetricsHint = 1;
/*  249 */     this.lcdTextContrast = lcdTextContrastDefaultValue;
/*  250 */     this.interpolationHint = -1;
/*  251 */     this.strokeHint = 0;
/*      */ 
/*  253 */     this.interpolationType = 1;
/*      */ 
/*  255 */     validateColor();
/*      */ 
/*  257 */     this.devScale = paramSurfaceData.getDefaultScale();
/*  258 */     if (this.devScale != 1) {
/*  259 */       this.transform.setToScale(this.devScale, this.devScale);
/*  260 */       invalidateTransform();
/*      */     }
/*      */ 
/*  263 */     this.font = paramFont;
/*  264 */     if (this.font == null) {
/*  265 */       this.font = defaultFont;
/*      */     }
/*      */ 
/*  268 */     setDevClip(paramSurfaceData.getBounds());
/*  269 */     invalidatePipe();
/*      */   }
/*      */ 
/*      */   protected Object clone() {
/*      */     try {
/*  274 */       SunGraphics2D localSunGraphics2D = (SunGraphics2D)super.clone();
/*  275 */       localSunGraphics2D.transform = new AffineTransform(this.transform);
/*  276 */       if (this.hints != null) {
/*  277 */         localSunGraphics2D.hints = ((RenderingHints)this.hints.clone());
/*      */       }
/*      */ 
/*  285 */       if (this.fontInfo != null) {
/*  286 */         if (this.validFontInfo)
/*  287 */           localSunGraphics2D.fontInfo = ((FontInfo)this.fontInfo.clone());
/*      */         else {
/*  289 */           localSunGraphics2D.fontInfo = null;
/*      */         }
/*      */       }
/*  292 */       if (this.glyphVectorFontInfo != null) {
/*  293 */         localSunGraphics2D.glyphVectorFontInfo = ((FontInfo)this.glyphVectorFontInfo.clone());
/*      */ 
/*  295 */         localSunGraphics2D.glyphVectorFRC = this.glyphVectorFRC;
/*      */       }
/*      */ 
/*  298 */       return localSunGraphics2D;
/*      */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*      */     }
/*  301 */     return null;
/*      */   }
/*      */ 
/*      */   public Graphics create()
/*      */   {
/*  308 */     return (Graphics)clone();
/*      */   }
/*      */ 
/*      */   public void setDevClip(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*  312 */     Region localRegion = this.constrainClip;
/*  313 */     if (localRegion == null)
/*  314 */       this.devClip = Region.getInstanceXYWH(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     else {
/*  316 */       this.devClip = localRegion.getIntersectionXYWH(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*  318 */     validateCompClip();
/*      */   }
/*      */ 
/*      */   public void setDevClip(Rectangle paramRectangle) {
/*  322 */     setDevClip(paramRectangle.x, paramRectangle.y, paramRectangle.width, paramRectangle.height);
/*      */   }
/*      */ 
/*      */   public void constrain(int paramInt1, int paramInt2, int paramInt3, int paramInt4, Region paramRegion)
/*      */   {
/*  329 */     if ((paramInt1 | paramInt2) != 0) {
/*  330 */       translate(paramInt1, paramInt2);
/*      */     }
/*  332 */     if (this.transformState > 3) {
/*  333 */       clipRect(0, 0, paramInt3, paramInt4);
/*  334 */       return;
/*      */     }
/*      */ 
/*  337 */     double d1 = this.transform.getScaleX();
/*  338 */     double d2 = this.transform.getScaleY();
/*  339 */     paramInt1 = this.constrainX = (int)this.transform.getTranslateX();
/*  340 */     paramInt2 = this.constrainY = (int)this.transform.getTranslateY();
/*  341 */     paramInt3 = Region.dimAdd(paramInt1, Region.clipScale(paramInt3, d1));
/*  342 */     paramInt4 = Region.dimAdd(paramInt2, Region.clipScale(paramInt4, d2));
/*      */ 
/*  344 */     Region localRegion = this.constrainClip;
/*  345 */     if (localRegion == null)
/*  346 */       localRegion = Region.getInstanceXYXY(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     else {
/*  348 */       localRegion = localRegion.getIntersectionXYXY(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     }
/*  350 */     if (paramRegion != null) {
/*  351 */       paramRegion = paramRegion.getScaledRegion(d1, d2);
/*  352 */       paramRegion = paramRegion.getTranslatedRegion(paramInt1, paramInt2);
/*  353 */       localRegion = localRegion.getIntersection(paramRegion);
/*      */     }
/*      */ 
/*  356 */     if (localRegion == this.constrainClip)
/*      */     {
/*  358 */       return;
/*      */     }
/*      */ 
/*  361 */     this.constrainClip = localRegion;
/*  362 */     if (!this.devClip.isInsideQuickCheck(localRegion)) {
/*  363 */       this.devClip = this.devClip.getIntersection(localRegion);
/*  364 */       validateCompClip();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void constrain(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  382 */     constrain(paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */   }
/*      */ 
/*      */   protected void invalidatePipe()
/*      */   {
/*  391 */     this.drawpipe = invalidpipe;
/*  392 */     this.fillpipe = invalidpipe;
/*  393 */     this.shapepipe = invalidpipe;
/*  394 */     this.textpipe = invalidpipe;
/*  395 */     this.imagepipe = invalidpipe;
/*  396 */     this.loops = null;
/*      */   }
/*      */ 
/*      */   public void validatePipe()
/*      */   {
/*  407 */     if (!this.surfaceData.isValid()) {
/*  408 */       throw new InvalidPipeException("attempt to validate Pipe with invalid SurfaceData");
/*      */     }
/*      */ 
/*  411 */     this.surfaceData.validatePipe(this);
/*      */   }
/*      */ 
/*      */   Shape intersectShapes(Shape paramShape1, Shape paramShape2, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  422 */     if (((paramShape1 instanceof Rectangle)) && ((paramShape2 instanceof Rectangle))) {
/*  423 */       return ((Rectangle)paramShape1).intersection((Rectangle)paramShape2);
/*      */     }
/*  425 */     if ((paramShape1 instanceof Rectangle2D))
/*  426 */       return intersectRectShape((Rectangle2D)paramShape1, paramShape2, paramBoolean1, paramBoolean2);
/*  427 */     if ((paramShape2 instanceof Rectangle2D)) {
/*  428 */       return intersectRectShape((Rectangle2D)paramShape2, paramShape1, paramBoolean2, paramBoolean1);
/*      */     }
/*  430 */     return intersectByArea(paramShape1, paramShape2, paramBoolean1, paramBoolean2);
/*      */   }
/*      */ 
/*      */   Shape intersectRectShape(Rectangle2D paramRectangle2D, Shape paramShape, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  442 */     if ((paramShape instanceof Rectangle2D)) {
/*  443 */       Rectangle2D localRectangle2D = (Rectangle2D)paramShape;
/*      */       Object localObject;
/*  445 */       if (!paramBoolean1)
/*  446 */         localObject = paramRectangle2D;
/*  447 */       else if (!paramBoolean2)
/*  448 */         localObject = localRectangle2D;
/*      */       else {
/*  450 */         localObject = new Rectangle2D.Float();
/*      */       }
/*  452 */       double d1 = Math.max(paramRectangle2D.getX(), localRectangle2D.getX());
/*  453 */       double d2 = Math.min(paramRectangle2D.getX() + paramRectangle2D.getWidth(), localRectangle2D.getX() + localRectangle2D.getWidth());
/*      */ 
/*  455 */       double d3 = Math.max(paramRectangle2D.getY(), localRectangle2D.getY());
/*  456 */       double d4 = Math.min(paramRectangle2D.getY() + paramRectangle2D.getHeight(), localRectangle2D.getY() + localRectangle2D.getHeight());
/*      */ 
/*  459 */       if ((d2 - d1 < 0.0D) || (d4 - d3 < 0.0D))
/*      */       {
/*  461 */         ((Rectangle2D)localObject).setFrameFromDiagonal(0.0D, 0.0D, 0.0D, 0.0D);
/*      */       }
/*  463 */       else ((Rectangle2D)localObject).setFrameFromDiagonal(d1, d3, d2, d4);
/*  464 */       return localObject;
/*      */     }
/*  466 */     if (paramRectangle2D.contains(paramShape.getBounds2D())) {
/*  467 */       if (paramBoolean2) {
/*  468 */         paramShape = cloneShape(paramShape);
/*      */       }
/*  470 */       return paramShape;
/*      */     }
/*  472 */     return intersectByArea(paramRectangle2D, paramShape, paramBoolean1, paramBoolean2);
/*      */   }
/*      */ 
/*      */   protected static Shape cloneShape(Shape paramShape) {
/*  476 */     return new GeneralPath(paramShape);
/*      */   }
/*      */ 
/*      */   Shape intersectByArea(Shape paramShape1, Shape paramShape2, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*      */     Area localArea1;
/*  493 */     if ((!paramBoolean1) && ((paramShape1 instanceof Area))) {
/*  494 */       localArea1 = (Area)paramShape1;
/*  495 */     } else if ((!paramBoolean2) && ((paramShape2 instanceof Area))) {
/*  496 */       localArea1 = (Area)paramShape2;
/*  497 */       paramShape2 = paramShape1;
/*      */     } else {
/*  499 */       localArea1 = new Area(paramShape1);
/*      */     }
/*      */     Area localArea2;
/*  502 */     if ((paramShape2 instanceof Area))
/*  503 */       localArea2 = (Area)paramShape2;
/*      */     else {
/*  505 */       localArea2 = new Area(paramShape2);
/*      */     }
/*      */ 
/*  508 */     localArea1.intersect(localArea2);
/*  509 */     if (localArea1.isRectangular()) {
/*  510 */       return localArea1.getBounds();
/*      */     }
/*      */ 
/*  513 */     return localArea1;
/*      */   }
/*      */ 
/*      */   public Region getCompClip()
/*      */   {
/*  521 */     if (!this.surfaceData.isValid())
/*      */     {
/*  523 */       revalidateAll();
/*      */     }
/*      */ 
/*  526 */     return this.clipRegion;
/*      */   }
/*      */ 
/*      */   public Font getFont() {
/*  530 */     if (this.font == null) {
/*  531 */       this.font = defaultFont;
/*      */     }
/*  533 */     return this.font;
/*      */   }
/*      */ 
/*      */   public FontInfo checkFontInfo(FontInfo paramFontInfo, Font paramFont, FontRenderContext paramFontRenderContext)
/*      */   {
/*  560 */     if (paramFontInfo == null) {
/*  561 */       paramFontInfo = new FontInfo();
/*      */     }
/*      */ 
/*  564 */     float f = paramFont.getSize2D();
/*      */ 
/*  566 */     AffineTransform localAffineTransform2 = null;
/*      */     int i;
/*      */     AffineTransform localAffineTransform1;
/*      */     double d3;
/*  567 */     if (paramFont.isTransformed()) {
/*  568 */       localAffineTransform2 = paramFont.getTransform();
/*  569 */       localAffineTransform2.scale(f, f);
/*  570 */       i = localAffineTransform2.getType();
/*  571 */       paramFontInfo.originX = ((float)localAffineTransform2.getTranslateX());
/*  572 */       paramFontInfo.originY = ((float)localAffineTransform2.getTranslateY());
/*  573 */       localAffineTransform2.translate(-paramFontInfo.originX, -paramFontInfo.originY);
/*  574 */       if (this.transformState >= 3) {
/*  575 */         this.transform.getMatrix(paramFontInfo.devTx = new double[4]);
/*  576 */         localAffineTransform1 = new AffineTransform(paramFontInfo.devTx);
/*  577 */         localAffineTransform2.preConcatenate(localAffineTransform1);
/*      */       } else {
/*  579 */         paramFontInfo.devTx = IDENT_MATRIX;
/*  580 */         localAffineTransform1 = IDENT_ATX;
/*      */       }
/*  582 */       localAffineTransform2.getMatrix(paramFontInfo.glyphTx = new double[4]);
/*  583 */       double d1 = localAffineTransform2.getShearX();
/*  584 */       d3 = localAffineTransform2.getScaleY();
/*  585 */       if (d1 != 0.0D) {
/*  586 */         d3 = Math.sqrt(d1 * d1 + d3 * d3);
/*      */       }
/*  588 */       paramFontInfo.pixelHeight = ((int)(Math.abs(d3) + 0.5D));
/*      */     } else {
/*  590 */       i = 0;
/*  591 */       paramFontInfo.originX = (paramFontInfo.originY = 0.0F);
/*  592 */       if (this.transformState >= 3) {
/*  593 */         this.transform.getMatrix(paramFontInfo.devTx = new double[4]);
/*  594 */         localAffineTransform1 = new AffineTransform(paramFontInfo.devTx);
/*  595 */         paramFontInfo.glyphTx = new double[4];
/*  596 */         for (int j = 0; j < 4; j++) {
/*  597 */           paramFontInfo.glyphTx[j] = (paramFontInfo.devTx[j] * f);
/*      */         }
/*  599 */         localAffineTransform2 = new AffineTransform(paramFontInfo.glyphTx);
/*  600 */         double d2 = this.transform.getShearX();
/*  601 */         d3 = this.transform.getScaleY();
/*  602 */         if (d2 != 0.0D) {
/*  603 */           d3 = Math.sqrt(d2 * d2 + d3 * d3);
/*      */         }
/*  605 */         paramFontInfo.pixelHeight = ((int)(Math.abs(d3 * f) + 0.5D));
/*      */       }
/*      */       else
/*      */       {
/*  615 */         k = (int)f;
/*  616 */         if ((f == k) && (k >= 8) && (k < 17))
/*      */         {
/*  618 */           paramFontInfo.glyphTx = textTxArr[k];
/*  619 */           localAffineTransform2 = textAtArr[k];
/*  620 */           paramFontInfo.pixelHeight = k;
/*      */         } else {
/*  622 */           paramFontInfo.pixelHeight = ((int)(f + 0.5D));
/*      */         }
/*  624 */         if (localAffineTransform2 == null) {
/*  625 */           paramFontInfo.glyphTx = new double[] { f, 0.0D, 0.0D, f };
/*  626 */           localAffineTransform2 = new AffineTransform(paramFontInfo.glyphTx);
/*      */         }
/*      */ 
/*  629 */         paramFontInfo.devTx = IDENT_MATRIX;
/*  630 */         localAffineTransform1 = IDENT_ATX;
/*      */       }
/*      */     }
/*      */ 
/*  634 */     paramFontInfo.font2D = FontUtilities.getFont2D(paramFont);
/*      */ 
/*  636 */     int k = this.fractionalMetricsHint;
/*  637 */     if (k == 0) {
/*  638 */       k = 1;
/*      */     }
/*  640 */     paramFontInfo.lcdSubPixPos = false;
/*      */     int m;
/*  665 */     if (paramFontRenderContext == null)
/*  666 */       m = this.textAntialiasHint;
/*      */     else {
/*  668 */       m = ((SunHints.Value)paramFontRenderContext.getAntiAliasingHint()).getIndex();
/*      */     }
/*  670 */     if (m == 0) {
/*  671 */       if (this.antialiasHint == 2)
/*  672 */         m = 2;
/*      */       else {
/*  674 */         m = 1;
/*      */       }
/*      */ 
/*      */     }
/*  683 */     else if (m == 3) {
/*  684 */       if (paramFontInfo.font2D.useAAForPtSize(paramFontInfo.pixelHeight))
/*  685 */         m = 2;
/*      */       else
/*  687 */         m = 1;
/*      */     }
/*  689 */     else if (m >= 4)
/*      */     {
/*  705 */       if (!this.surfaceData.canRenderLCDText(this))
/*      */       {
/*  711 */         m = 2;
/*      */       } else {
/*  713 */         paramFontInfo.lcdRGBOrder = true;
/*      */ 
/*  721 */         if (m == 5) {
/*  722 */           m = 4;
/*  723 */           paramFontInfo.lcdRGBOrder = false;
/*  724 */         } else if (m == 7)
/*      */         {
/*  726 */           m = 6;
/*  727 */           paramFontInfo.lcdRGBOrder = false;
/*      */         }
/*      */ 
/*  732 */         paramFontInfo.lcdSubPixPos = ((k == 2) && (m == 4));
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  738 */     paramFontInfo.aaHint = m;
/*  739 */     paramFontInfo.fontStrike = paramFontInfo.font2D.getStrike(paramFont, localAffineTransform1, localAffineTransform2, m, k);
/*      */ 
/*  741 */     return paramFontInfo;
/*      */   }
/*      */ 
/*      */   public static boolean isRotated(double[] paramArrayOfDouble) {
/*  745 */     if ((paramArrayOfDouble[0] == paramArrayOfDouble[3]) && (paramArrayOfDouble[1] == 0.0D) && (paramArrayOfDouble[2] == 0.0D) && (paramArrayOfDouble[0] > 0.0D))
/*      */     {
/*  750 */       return false;
/*      */     }
/*      */ 
/*  753 */     return true;
/*      */   }
/*      */ 
/*      */   public void setFont(Font paramFont)
/*      */   {
/*  762 */     if ((paramFont != null) && (paramFont != this.font))
/*      */     {
/*  777 */       if ((this.textAntialiasHint == 3) && (this.textpipe != invalidpipe)) if ((this.transformState <= 2) && (!paramFont.isTransformed()) && (this.fontInfo != null))
/*      */         {
/*  777 */           if ((this.fontInfo.aaHint == 2) == FontUtilities.getFont2D(paramFont).useAAForPtSize(paramFont.getSize()));
/*      */         }
/*      */         else
/*      */         {
/*  785 */           this.textpipe = invalidpipe;
/*      */         }
/*  787 */       this.font = paramFont;
/*  788 */       this.fontMetrics = null;
/*  789 */       this.validFontInfo = false;
/*      */     }
/*      */   }
/*      */ 
/*      */   public FontInfo getFontInfo() {
/*  794 */     if (!this.validFontInfo) {
/*  795 */       this.fontInfo = checkFontInfo(this.fontInfo, this.font, null);
/*  796 */       this.validFontInfo = true;
/*      */     }
/*  798 */     return this.fontInfo;
/*      */   }
/*      */ 
/*      */   public FontInfo getGVFontInfo(Font paramFont, FontRenderContext paramFontRenderContext)
/*      */   {
/*  803 */     if ((this.glyphVectorFontInfo != null) && (this.glyphVectorFontInfo.font == paramFont) && (this.glyphVectorFRC == paramFontRenderContext))
/*      */     {
/*  806 */       return this.glyphVectorFontInfo;
/*      */     }
/*  808 */     this.glyphVectorFRC = paramFontRenderContext;
/*  809 */     return this.glyphVectorFontInfo = checkFontInfo(this.glyphVectorFontInfo, paramFont, paramFontRenderContext);
/*      */   }
/*      */ 
/*      */   public FontMetrics getFontMetrics()
/*      */   {
/*  815 */     if (this.fontMetrics != null) {
/*  816 */       return this.fontMetrics;
/*      */     }
/*      */ 
/*  819 */     return this.fontMetrics = FontDesignMetrics.getMetrics(this.font, getFontRenderContext());
/*      */   }
/*      */ 
/*      */   public FontMetrics getFontMetrics(Font paramFont)
/*      */   {
/*  824 */     if ((this.fontMetrics != null) && (paramFont == this.font)) {
/*  825 */       return this.fontMetrics;
/*      */     }
/*  827 */     FontDesignMetrics localFontDesignMetrics = FontDesignMetrics.getMetrics(paramFont, getFontRenderContext());
/*      */ 
/*  830 */     if (this.font == paramFont) {
/*  831 */       this.fontMetrics = localFontDesignMetrics;
/*      */     }
/*  833 */     return localFontDesignMetrics;
/*      */   }
/*      */ 
/*      */   public boolean hit(Rectangle paramRectangle, Shape paramShape, boolean paramBoolean)
/*      */   {
/*  854 */     if (paramBoolean) {
/*  855 */       paramShape = this.stroke.createStrokedShape(paramShape);
/*      */     }
/*      */ 
/*  858 */     paramShape = transformShape(paramShape);
/*  859 */     if ((this.constrainX | this.constrainY) != 0) {
/*  860 */       paramRectangle = new Rectangle(paramRectangle);
/*  861 */       paramRectangle.translate(this.constrainX, this.constrainY);
/*      */     }
/*      */ 
/*  864 */     return paramShape.intersects(paramRectangle);
/*      */   }
/*      */ 
/*      */   public ColorModel getDeviceColorModel()
/*      */   {
/*  871 */     return this.surfaceData.getColorModel();
/*      */   }
/*      */ 
/*      */   public GraphicsConfiguration getDeviceConfiguration()
/*      */   {
/*  878 */     return this.surfaceData.getDeviceConfiguration();
/*      */   }
/*      */ 
/*      */   public final SurfaceData getSurfaceData()
/*      */   {
/*  886 */     return this.surfaceData;
/*      */   }
/*      */ 
/*      */   public void setComposite(Composite paramComposite)
/*      */   {
/*  900 */     if (this.composite == paramComposite)
/*      */       return;
/*      */     CompositeType localCompositeType;
/*      */     int i;
/*  905 */     if ((paramComposite instanceof AlphaComposite)) {
/*  906 */       AlphaComposite localAlphaComposite = (AlphaComposite)paramComposite;
/*  907 */       localCompositeType = CompositeType.forAlphaComposite(localAlphaComposite);
/*  908 */       if (localCompositeType == CompositeType.SrcOverNoEa) {
/*  909 */         if ((this.paintState == 0) || ((this.paintState > 1) && (this.paint.getTransparency() == 1)))
/*      */         {
/*  913 */           i = 0;
/*      */         }
/*  915 */         else i = 1;
/*      */       }
/*  917 */       else if ((localCompositeType == CompositeType.SrcNoEa) || (localCompositeType == CompositeType.Src) || (localCompositeType == CompositeType.Clear))
/*      */       {
/*  921 */         i = 0;
/*  922 */       } else if ((this.surfaceData.getTransparency() == 1) && (localCompositeType == CompositeType.SrcIn))
/*      */       {
/*  925 */         i = 0;
/*      */       }
/*  927 */       else i = 1;
/*      */     }
/*  929 */     else if ((paramComposite instanceof XORComposite)) {
/*  930 */       i = 2;
/*  931 */       localCompositeType = CompositeType.Xor; } else {
/*  932 */       if (paramComposite == null) {
/*  933 */         throw new IllegalArgumentException("null Composite");
/*      */       }
/*  935 */       this.surfaceData.checkCustomComposite();
/*  936 */       i = 3;
/*  937 */       localCompositeType = CompositeType.General;
/*      */     }
/*  939 */     if ((this.compositeState != i) || (this.imageComp != localCompositeType))
/*      */     {
/*  942 */       this.compositeState = i;
/*  943 */       this.imageComp = localCompositeType;
/*  944 */       invalidatePipe();
/*  945 */       this.validFontInfo = false;
/*      */     }
/*  947 */     this.composite = paramComposite;
/*  948 */     if (this.paintState <= 1)
/*  949 */       validateColor();
/*      */   }
/*      */ 
/*      */   public void setPaint(Paint paramPaint)
/*      */   {
/*  962 */     if ((paramPaint instanceof Color)) {
/*  963 */       setColor((Color)paramPaint);
/*  964 */       return;
/*      */     }
/*  966 */     if ((paramPaint == null) || (this.paint == paramPaint)) {
/*  967 */       return;
/*      */     }
/*  969 */     this.paint = paramPaint;
/*  970 */     if (this.imageComp == CompositeType.SrcOverNoEa)
/*      */     {
/*  972 */       if (paramPaint.getTransparency() == 1) {
/*  973 */         if (this.compositeState != 0) {
/*  974 */           this.compositeState = 0;
/*      */         }
/*      */       }
/*  977 */       else if (this.compositeState == 0) {
/*  978 */         this.compositeState = 1;
/*      */       }
/*      */     }
/*      */ 
/*  982 */     Class localClass = paramPaint.getClass();
/*  983 */     if (localClass == GradientPaint.class)
/*  984 */       this.paintState = 2;
/*  985 */     else if (localClass == LinearGradientPaint.class)
/*  986 */       this.paintState = 3;
/*  987 */     else if (localClass == RadialGradientPaint.class)
/*  988 */       this.paintState = 4;
/*  989 */     else if (localClass == TexturePaint.class)
/*  990 */       this.paintState = 5;
/*      */     else {
/*  992 */       this.paintState = 6;
/*      */     }
/*  994 */     this.validFontInfo = false;
/*  995 */     invalidatePipe();
/*      */   }
/*      */ 
/*      */   private void validateBasicStroke(BasicStroke paramBasicStroke)
/*      */   {
/* 1013 */     int i = this.antialiasHint == 2 ? 1 : 0;
/* 1014 */     if (this.transformState < 3) {
/* 1015 */       if (i != 0) {
/* 1016 */         if (paramBasicStroke.getLineWidth() <= MinPenSizeAA) {
/* 1017 */           if (paramBasicStroke.getDashArray() == null)
/* 1018 */             this.strokeState = 0;
/*      */           else
/* 1020 */             this.strokeState = 1;
/*      */         }
/*      */         else {
/* 1023 */           this.strokeState = 2;
/*      */         }
/*      */       }
/* 1026 */       else if (paramBasicStroke == defaultStroke)
/* 1027 */         this.strokeState = 0;
/* 1028 */       else if (paramBasicStroke.getLineWidth() <= 1.0F) {
/* 1029 */         if (paramBasicStroke.getDashArray() == null)
/* 1030 */           this.strokeState = 0;
/*      */         else
/* 1032 */           this.strokeState = 1;
/*      */       }
/*      */       else
/* 1035 */         this.strokeState = 2;
/*      */     }
/*      */     else
/*      */     {
/*      */       double d1;
/* 1040 */       if ((this.transform.getType() & 0x24) == 0)
/*      */       {
/* 1042 */         d1 = Math.abs(this.transform.getDeterminant());
/*      */       }
/*      */       else {
/* 1045 */         double d2 = this.transform.getScaleX();
/* 1046 */         double d3 = this.transform.getShearX();
/* 1047 */         double d4 = this.transform.getShearY();
/* 1048 */         double d5 = this.transform.getScaleY();
/*      */ 
/* 1064 */         double d6 = d2 * d2 + d4 * d4;
/* 1065 */         double d7 = 2.0D * (d2 * d3 + d4 * d5);
/* 1066 */         double d8 = d3 * d3 + d5 * d5;
/*      */ 
/* 1090 */         double d9 = Math.sqrt(d7 * d7 + (d6 - d8) * (d6 - d8));
/*      */ 
/* 1093 */         d1 = (d6 + d8 + d9) / 2.0D;
/*      */       }
/* 1095 */       if (paramBasicStroke != defaultStroke) {
/* 1096 */         d1 *= paramBasicStroke.getLineWidth() * paramBasicStroke.getLineWidth();
/*      */       }
/* 1098 */       if (d1 <= (i != 0 ? MinPenSizeAASquared : 1.000000001D))
/*      */       {
/* 1101 */         if (paramBasicStroke.getDashArray() == null)
/* 1102 */           this.strokeState = 0;
/*      */         else
/* 1104 */           this.strokeState = 1;
/*      */       }
/*      */       else
/* 1107 */         this.strokeState = 2;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setStroke(Stroke paramStroke)
/*      */   {
/* 1119 */     if (paramStroke == null) {
/* 1120 */       throw new IllegalArgumentException("null Stroke");
/*      */     }
/* 1122 */     int i = this.strokeState;
/* 1123 */     this.stroke = paramStroke;
/* 1124 */     if ((paramStroke instanceof BasicStroke))
/* 1125 */       validateBasicStroke((BasicStroke)paramStroke);
/*      */     else {
/* 1127 */       this.strokeState = 3;
/*      */     }
/* 1129 */     if (this.strokeState != i)
/* 1130 */       invalidatePipe();
/*      */   }
/*      */ 
/*      */   public void setRenderingHint(RenderingHints.Key paramKey, Object paramObject)
/*      */   {
/* 1151 */     if (!paramKey.isCompatibleValue(paramObject)) {
/* 1152 */       throw new IllegalArgumentException(paramObject + " is not compatible with " + paramKey);
/*      */     }
/*      */ 
/* 1155 */     if ((paramKey instanceof SunHints.Key))
/*      */     {
/* 1157 */       int j = 0;
/* 1158 */       int k = 1;
/* 1159 */       SunHints.Key localKey = (SunHints.Key)paramKey;
/*      */       int m;
/* 1161 */       if (localKey == SunHints.KEY_TEXT_ANTIALIAS_LCD_CONTRAST)
/* 1162 */         m = ((Integer)paramObject).intValue();
/*      */       else
/* 1164 */         m = ((SunHints.Value)paramObject).getIndex();
/*      */       int i;
/* 1166 */       switch (localKey.getIndex()) {
/*      */       case 0:
/* 1168 */         i = this.renderHint != m ? 1 : 0;
/* 1169 */         if (i != 0) {
/* 1170 */           this.renderHint = m;
/* 1171 */           if (this.interpolationHint == -1)
/* 1172 */             this.interpolationType = (m == 2 ? 2 : 1);  } break;
/*      */       case 1:
/* 1180 */         i = this.antialiasHint != m ? 1 : 0;
/* 1181 */         this.antialiasHint = m;
/* 1182 */         if (i != 0) {
/* 1183 */           j = this.textAntialiasHint == 0 ? 1 : 0;
/*      */ 
/* 1186 */           if (this.strokeState != 3)
/* 1187 */             validateBasicStroke((BasicStroke)this.stroke);  } break;
/*      */       case 2:
/* 1192 */         i = this.textAntialiasHint != m ? 1 : 0;
/* 1193 */         j = i;
/* 1194 */         this.textAntialiasHint = m;
/* 1195 */         break;
/*      */       case 3:
/* 1197 */         i = this.fractionalMetricsHint != m ? 1 : 0;
/* 1198 */         j = i;
/* 1199 */         this.fractionalMetricsHint = m;
/* 1200 */         break;
/*      */       case 100:
/* 1202 */         i = 0;
/*      */ 
/* 1204 */         this.lcdTextContrast = m;
/* 1205 */         break;
/*      */       case 5:
/* 1207 */         this.interpolationHint = m;
/* 1208 */         switch (m) {
/*      */         case 2:
/* 1210 */           m = 3;
/* 1211 */           break;
/*      */         case 1:
/* 1213 */           m = 2;
/* 1214 */           break;
/*      */         case 0:
/*      */         default:
/* 1217 */           m = 1;
/*      */         }
/*      */ 
/* 1220 */         i = this.interpolationType != m ? 1 : 0;
/* 1221 */         this.interpolationType = m;
/* 1222 */         break;
/*      */       case 8:
/* 1224 */         i = this.strokeHint != m ? 1 : 0;
/* 1225 */         this.strokeHint = m;
/* 1226 */         break;
/*      */       default:
/* 1228 */         k = 0;
/* 1229 */         i = 0;
/*      */       }
/*      */ 
/* 1232 */       if (k != 0) {
/* 1233 */         if (i != 0) {
/* 1234 */           invalidatePipe();
/* 1235 */           if (j != 0) {
/* 1236 */             this.fontMetrics = null;
/* 1237 */             this.cachedFRC = null;
/* 1238 */             this.validFontInfo = false;
/* 1239 */             this.glyphVectorFontInfo = null;
/*      */           }
/*      */         }
/* 1242 */         if (this.hints != null) {
/* 1243 */           this.hints.put(paramKey, paramObject);
/*      */         }
/* 1245 */         return;
/*      */       }
/*      */     }
/*      */ 
/* 1249 */     if (this.hints == null) {
/* 1250 */       this.hints = makeHints(null);
/*      */     }
/* 1252 */     this.hints.put(paramKey, paramObject);
/*      */   }
/*      */ 
/*      */   public Object getRenderingHint(RenderingHints.Key paramKey)
/*      */   {
/* 1265 */     if (this.hints != null) {
/* 1266 */       return this.hints.get(paramKey);
/*      */     }
/* 1268 */     if (!(paramKey instanceof SunHints.Key)) {
/* 1269 */       return null;
/*      */     }
/* 1271 */     int i = ((SunHints.Key)paramKey).getIndex();
/* 1272 */     switch (i) {
/*      */     case 0:
/* 1274 */       return SunHints.Value.get(0, this.renderHint);
/*      */     case 1:
/* 1277 */       return SunHints.Value.get(1, this.antialiasHint);
/*      */     case 2:
/* 1280 */       return SunHints.Value.get(2, this.textAntialiasHint);
/*      */     case 3:
/* 1283 */       return SunHints.Value.get(3, this.fractionalMetricsHint);
/*      */     case 100:
/* 1286 */       return new Integer(this.lcdTextContrast);
/*      */     case 5:
/* 1288 */       switch (this.interpolationHint) {
/*      */       case 0:
/* 1290 */         return SunHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
/*      */       case 1:
/* 1292 */         return SunHints.VALUE_INTERPOLATION_BILINEAR;
/*      */       case 2:
/* 1294 */         return SunHints.VALUE_INTERPOLATION_BICUBIC;
/*      */       }
/* 1296 */       return null;
/*      */     case 8:
/* 1298 */       return SunHints.Value.get(8, this.strokeHint);
/*      */     }
/*      */ 
/* 1301 */     return null;
/*      */   }
/*      */ 
/*      */   public void setRenderingHints(Map<?, ?> paramMap)
/*      */   {
/* 1312 */     this.hints = null;
/* 1313 */     this.renderHint = 0;
/* 1314 */     this.antialiasHint = 1;
/* 1315 */     this.textAntialiasHint = 0;
/* 1316 */     this.fractionalMetricsHint = 1;
/* 1317 */     this.lcdTextContrast = lcdTextContrastDefaultValue;
/* 1318 */     this.interpolationHint = -1;
/* 1319 */     this.interpolationType = 1;
/* 1320 */     int i = 0;
/* 1321 */     Iterator localIterator = paramMap.keySet().iterator();
/* 1322 */     while (localIterator.hasNext()) {
/* 1323 */       Object localObject = localIterator.next();
/* 1324 */       if ((localObject == SunHints.KEY_RENDERING) || (localObject == SunHints.KEY_ANTIALIASING) || (localObject == SunHints.KEY_TEXT_ANTIALIASING) || (localObject == SunHints.KEY_FRACTIONALMETRICS) || (localObject == SunHints.KEY_TEXT_ANTIALIAS_LCD_CONTRAST) || (localObject == SunHints.KEY_STROKE_CONTROL) || (localObject == SunHints.KEY_INTERPOLATION))
/*      */       {
/* 1332 */         setRenderingHint((RenderingHints.Key)localObject, paramMap.get(localObject));
/*      */       }
/* 1334 */       else i = 1;
/*      */     }
/*      */ 
/* 1337 */     if (i != 0) {
/* 1338 */       this.hints = makeHints(paramMap);
/*      */     }
/* 1340 */     invalidatePipe();
/*      */   }
/*      */ 
/*      */   public void addRenderingHints(Map<?, ?> paramMap)
/*      */   {
/* 1351 */     int i = 0;
/* 1352 */     Iterator localIterator = paramMap.keySet().iterator();
/* 1353 */     while (localIterator.hasNext()) {
/* 1354 */       Object localObject = localIterator.next();
/* 1355 */       if ((localObject == SunHints.KEY_RENDERING) || (localObject == SunHints.KEY_ANTIALIASING) || (localObject == SunHints.KEY_TEXT_ANTIALIASING) || (localObject == SunHints.KEY_FRACTIONALMETRICS) || (localObject == SunHints.KEY_TEXT_ANTIALIAS_LCD_CONTRAST) || (localObject == SunHints.KEY_STROKE_CONTROL) || (localObject == SunHints.KEY_INTERPOLATION))
/*      */       {
/* 1363 */         setRenderingHint((RenderingHints.Key)localObject, paramMap.get(localObject));
/*      */       }
/* 1365 */       else i = 1;
/*      */     }
/*      */ 
/* 1368 */     if (i != 0)
/* 1369 */       if (this.hints == null)
/* 1370 */         this.hints = makeHints(paramMap);
/*      */       else
/* 1372 */         this.hints.putAll(paramMap);
/*      */   }
/*      */ 
/*      */   public RenderingHints getRenderingHints()
/*      */   {
/* 1384 */     if (this.hints == null) {
/* 1385 */       return makeHints(null);
/*      */     }
/* 1387 */     return (RenderingHints)this.hints.clone();
/*      */   }
/*      */ 
/*      */   RenderingHints makeHints(Map paramMap)
/*      */   {
/* 1392 */     RenderingHints localRenderingHints = new RenderingHints(paramMap);
/* 1393 */     localRenderingHints.put(SunHints.KEY_RENDERING, SunHints.Value.get(0, this.renderHint));
/*      */ 
/* 1396 */     localRenderingHints.put(SunHints.KEY_ANTIALIASING, SunHints.Value.get(1, this.antialiasHint));
/*      */ 
/* 1399 */     localRenderingHints.put(SunHints.KEY_TEXT_ANTIALIASING, SunHints.Value.get(2, this.textAntialiasHint));
/*      */ 
/* 1402 */     localRenderingHints.put(SunHints.KEY_FRACTIONALMETRICS, SunHints.Value.get(3, this.fractionalMetricsHint));
/*      */ 
/* 1405 */     localRenderingHints.put(SunHints.KEY_TEXT_ANTIALIAS_LCD_CONTRAST, Integer.valueOf(this.lcdTextContrast));
/*      */     Object localObject;
/* 1408 */     switch (this.interpolationHint) {
/*      */     case 0:
/* 1410 */       localObject = SunHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
/* 1411 */       break;
/*      */     case 1:
/* 1413 */       localObject = SunHints.VALUE_INTERPOLATION_BILINEAR;
/* 1414 */       break;
/*      */     case 2:
/* 1416 */       localObject = SunHints.VALUE_INTERPOLATION_BICUBIC;
/* 1417 */       break;
/*      */     default:
/* 1419 */       localObject = null;
/*      */     }
/*      */ 
/* 1422 */     if (localObject != null) {
/* 1423 */       localRenderingHints.put(SunHints.KEY_INTERPOLATION, localObject);
/*      */     }
/* 1425 */     localRenderingHints.put(SunHints.KEY_STROKE_CONTROL, SunHints.Value.get(8, this.strokeHint));
/*      */ 
/* 1428 */     return localRenderingHints;
/*      */   }
/*      */ 
/*      */   public void translate(double paramDouble1, double paramDouble2)
/*      */   {
/* 1443 */     this.transform.translate(paramDouble1, paramDouble2);
/* 1444 */     invalidateTransform();
/*      */   }
/*      */ 
/*      */   public void rotate(double paramDouble)
/*      */   {
/* 1462 */     this.transform.rotate(paramDouble);
/* 1463 */     invalidateTransform();
/*      */   }
/*      */ 
/*      */   public void rotate(double paramDouble1, double paramDouble2, double paramDouble3)
/*      */   {
/* 1482 */     this.transform.rotate(paramDouble1, paramDouble2, paramDouble3);
/* 1483 */     invalidateTransform();
/*      */   }
/*      */ 
/*      */   public void scale(double paramDouble1, double paramDouble2)
/*      */   {
/* 1498 */     this.transform.scale(paramDouble1, paramDouble2);
/* 1499 */     invalidateTransform();
/*      */   }
/*      */ 
/*      */   public void shear(double paramDouble1, double paramDouble2)
/*      */   {
/* 1518 */     this.transform.shear(paramDouble1, paramDouble2);
/* 1519 */     invalidateTransform();
/*      */   }
/*      */ 
/*      */   public void transform(AffineTransform paramAffineTransform)
/*      */   {
/* 1540 */     this.transform.concatenate(paramAffineTransform);
/* 1541 */     invalidateTransform();
/*      */   }
/*      */ 
/*      */   public void translate(int paramInt1, int paramInt2)
/*      */   {
/* 1548 */     this.transform.translate(paramInt1, paramInt2);
/* 1549 */     if (this.transformState <= 1) {
/* 1550 */       this.transX += paramInt1;
/* 1551 */       this.transY += paramInt2;
/* 1552 */       this.transformState = ((this.transX | this.transY) == 0 ? 0 : 1);
/*      */     }
/*      */     else {
/* 1555 */       invalidateTransform();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void setTransform(AffineTransform paramAffineTransform)
/*      */   {
/* 1568 */     if (((this.constrainX | this.constrainY) == 0) && (this.devScale == 1)) {
/* 1569 */       this.transform.setTransform(paramAffineTransform);
/*      */     } else {
/* 1571 */       this.transform.setTransform(this.devScale, 0.0D, 0.0D, this.devScale, this.constrainX, this.constrainY);
/*      */ 
/* 1573 */       this.transform.concatenate(paramAffineTransform);
/*      */     }
/* 1575 */     invalidateTransform();
/*      */   }
/*      */ 
/*      */   protected void invalidateTransform() {
/* 1579 */     int i = this.transform.getType();
/* 1580 */     int j = this.transformState;
/* 1581 */     if (i == 0) {
/* 1582 */       this.transformState = 0;
/* 1583 */       this.transX = (this.transY = 0);
/* 1584 */     } else if (i == 1) {
/* 1585 */       double d1 = this.transform.getTranslateX();
/* 1586 */       double d2 = this.transform.getTranslateY();
/* 1587 */       this.transX = ((int)Math.floor(d1 + 0.5D));
/* 1588 */       this.transY = ((int)Math.floor(d2 + 0.5D));
/* 1589 */       if ((d1 == this.transX) && (d2 == this.transY))
/* 1590 */         this.transformState = 1;
/*      */       else
/* 1592 */         this.transformState = 2;
/*      */     }
/* 1594 */     else if ((i & 0x78) == 0)
/*      */     {
/* 1598 */       this.transformState = 3;
/* 1599 */       this.transX = (this.transY = 0);
/*      */     } else {
/* 1601 */       this.transformState = 4;
/* 1602 */       this.transX = (this.transY = 0);
/*      */     }
/*      */ 
/* 1605 */     if ((this.transformState >= 3) || (j >= 3))
/*      */     {
/* 1611 */       this.cachedFRC = null;
/* 1612 */       this.validFontInfo = false;
/* 1613 */       this.fontMetrics = null;
/* 1614 */       this.glyphVectorFontInfo = null;
/*      */ 
/* 1616 */       if (this.transformState != j) {
/* 1617 */         invalidatePipe();
/*      */       }
/*      */     }
/* 1620 */     if (this.strokeState != 3)
/* 1621 */       validateBasicStroke((BasicStroke)this.stroke);
/*      */   }
/*      */ 
/*      */   public AffineTransform getTransform()
/*      */   {
/* 1632 */     if (((this.constrainX | this.constrainY) == 0) && (this.devScale == 1)) {
/* 1633 */       return new AffineTransform(this.transform);
/*      */     }
/* 1635 */     double d = 1.0D / this.devScale;
/* 1636 */     AffineTransform localAffineTransform = new AffineTransform(d, 0.0D, 0.0D, d, -this.constrainX * d, -this.constrainY * d);
/*      */ 
/* 1639 */     localAffineTransform.concatenate(this.transform);
/* 1640 */     return localAffineTransform;
/*      */   }
/*      */ 
/*      */   public AffineTransform cloneTransform()
/*      */   {
/* 1648 */     return new AffineTransform(this.transform);
/*      */   }
/*      */ 
/*      */   public Paint getPaint()
/*      */   {
/* 1657 */     return this.paint;
/*      */   }
/*      */ 
/*      */   public Composite getComposite()
/*      */   {
/* 1665 */     return this.composite;
/*      */   }
/*      */ 
/*      */   public Color getColor() {
/* 1669 */     return this.foregroundColor;
/*      */   }
/*      */ 
/*      */   final void validateColor()
/*      */   {
/*      */     int i;
/* 1694 */     if (this.imageComp == CompositeType.Clear) {
/* 1695 */       i = 0;
/*      */     } else {
/* 1697 */       i = this.foregroundColor.getRGB();
/* 1698 */       if ((this.compositeState <= 1) && (this.imageComp != CompositeType.SrcNoEa) && (this.imageComp != CompositeType.SrcOverNoEa))
/*      */       {
/* 1702 */         AlphaComposite localAlphaComposite = (AlphaComposite)this.composite;
/* 1703 */         int j = Math.round(localAlphaComposite.getAlpha() * (i >>> 24));
/* 1704 */         i = i & 0xFFFFFF | j << 24;
/*      */       }
/*      */     }
/* 1707 */     this.eargb = i;
/* 1708 */     this.pixel = this.surfaceData.pixelFor(i);
/*      */   }
/*      */ 
/*      */   public void setColor(Color paramColor) {
/* 1712 */     if ((paramColor == null) || (paramColor == this.paint)) {
/* 1713 */       return;
/*      */     }
/* 1715 */     this.paint = (this.foregroundColor = paramColor);
/* 1716 */     validateColor();
/* 1717 */     if (this.eargb >> 24 == -1) {
/* 1718 */       if (this.paintState == 0) {
/* 1719 */         return;
/*      */       }
/* 1721 */       this.paintState = 0;
/* 1722 */       if (this.imageComp == CompositeType.SrcOverNoEa)
/*      */       {
/* 1724 */         this.compositeState = 0;
/*      */       }
/*      */     } else {
/* 1727 */       if (this.paintState == 1) {
/* 1728 */         return;
/*      */       }
/* 1730 */       this.paintState = 1;
/* 1731 */       if (this.imageComp == CompositeType.SrcOverNoEa)
/*      */       {
/* 1733 */         this.compositeState = 1;
/*      */       }
/*      */     }
/* 1736 */     this.validFontInfo = false;
/* 1737 */     invalidatePipe();
/*      */   }
/*      */ 
/*      */   public void setBackground(Color paramColor)
/*      */   {
/* 1753 */     this.backgroundColor = paramColor;
/*      */   }
/*      */ 
/*      */   public Color getBackground()
/*      */   {
/* 1761 */     return this.backgroundColor;
/*      */   }
/*      */ 
/*      */   public Stroke getStroke()
/*      */   {
/* 1769 */     return this.stroke;
/*      */   }
/*      */ 
/*      */   public Rectangle getClipBounds() {
/* 1773 */     if (this.clipState == 0) {
/* 1774 */       return null;
/*      */     }
/* 1776 */     return getClipBounds(new Rectangle());
/*      */   }
/*      */ 
/*      */   public Rectangle getClipBounds(Rectangle paramRectangle) {
/* 1780 */     if (this.clipState != 0) {
/* 1781 */       if (this.transformState <= 1) {
/* 1782 */         if ((this.usrClip instanceof Rectangle))
/* 1783 */           paramRectangle.setBounds((Rectangle)this.usrClip);
/*      */         else {
/* 1785 */           paramRectangle.setFrame(this.usrClip.getBounds2D());
/*      */         }
/* 1787 */         paramRectangle.translate(-this.transX, -this.transY);
/*      */       } else {
/* 1789 */         paramRectangle.setFrame(getClip().getBounds2D());
/*      */       }
/* 1791 */     } else if (paramRectangle == null) {
/* 1792 */       throw new NullPointerException("null rectangle parameter");
/*      */     }
/* 1794 */     return paramRectangle;
/*      */   }
/*      */ 
/*      */   public boolean hitClip(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 1798 */     if ((paramInt3 <= 0) || (paramInt4 <= 0)) {
/* 1799 */       return false;
/*      */     }
/* 1801 */     if (this.transformState > 1)
/*      */     {
/* 1819 */       double[] arrayOfDouble = { paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2, paramInt1, paramInt2 + paramInt4, paramInt1 + paramInt3, paramInt2 + paramInt4 };
/*      */ 
/* 1825 */       this.transform.transform(arrayOfDouble, 0, arrayOfDouble, 0, 4);
/* 1826 */       paramInt1 = (int)Math.floor(Math.min(Math.min(arrayOfDouble[0], arrayOfDouble[2]), Math.min(arrayOfDouble[4], arrayOfDouble[6])));
/*      */ 
/* 1828 */       paramInt2 = (int)Math.floor(Math.min(Math.min(arrayOfDouble[1], arrayOfDouble[3]), Math.min(arrayOfDouble[5], arrayOfDouble[7])));
/*      */ 
/* 1830 */       paramInt3 = (int)Math.ceil(Math.max(Math.max(arrayOfDouble[0], arrayOfDouble[2]), Math.max(arrayOfDouble[4], arrayOfDouble[6])));
/*      */ 
/* 1832 */       paramInt4 = (int)Math.ceil(Math.max(Math.max(arrayOfDouble[1], arrayOfDouble[3]), Math.max(arrayOfDouble[5], arrayOfDouble[7])));
/*      */     }
/*      */     else {
/* 1835 */       paramInt1 += this.transX;
/* 1836 */       paramInt2 += this.transY;
/* 1837 */       paramInt3 += paramInt1;
/* 1838 */       paramInt4 += paramInt2;
/*      */     }
/*      */     try
/*      */     {
/* 1842 */       if (!getCompClip().intersectsQuickCheckXYXY(paramInt1, paramInt2, paramInt3, paramInt4))
/* 1843 */         return false;
/*      */     }
/*      */     catch (InvalidPipeException localInvalidPipeException) {
/* 1846 */       return false;
/*      */     }
/*      */ 
/* 1853 */     return true;
/*      */   }
/*      */ 
/*      */   protected void validateCompClip() {
/* 1857 */     int i = this.clipState;
/* 1858 */     if (this.usrClip == null) {
/* 1859 */       this.clipState = 0;
/* 1860 */       this.clipRegion = this.devClip;
/* 1861 */     } else if ((this.usrClip instanceof Rectangle2D)) {
/* 1862 */       this.clipState = 1;
/* 1863 */       if ((this.usrClip instanceof Rectangle))
/* 1864 */         this.clipRegion = this.devClip.getIntersection((Rectangle)this.usrClip);
/*      */       else
/* 1866 */         this.clipRegion = this.devClip.getIntersection(this.usrClip.getBounds());
/*      */     }
/*      */     else {
/* 1869 */       PathIterator localPathIterator = this.usrClip.getPathIterator(null);
/* 1870 */       int[] arrayOfInt = new int[4];
/* 1871 */       ShapeSpanIterator localShapeSpanIterator = LoopPipe.getFillSSI(this);
/*      */       try {
/* 1873 */         localShapeSpanIterator.setOutputArea(this.devClip);
/* 1874 */         localShapeSpanIterator.appendPath(localPathIterator);
/* 1875 */         localShapeSpanIterator.getPathBox(arrayOfInt);
/* 1876 */         Region localRegion = Region.getInstance(arrayOfInt);
/* 1877 */         localRegion.appendSpans(localShapeSpanIterator);
/* 1878 */         this.clipRegion = localRegion;
/* 1879 */         this.clipState = (localRegion.isRectangular() ? 1 : 2);
/*      */       }
/*      */       finally {
/* 1882 */         localShapeSpanIterator.dispose();
/*      */       }
/*      */     }
/* 1885 */     if ((i != this.clipState) && ((this.clipState == 2) || (i == 2)))
/*      */     {
/* 1888 */       this.validFontInfo = false;
/* 1889 */       invalidatePipe();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Shape transformShape(Shape paramShape)
/*      */   {
/* 1898 */     if (paramShape == null) {
/* 1899 */       return null;
/*      */     }
/* 1901 */     if (this.transformState > 1) {
/* 1902 */       return transformShape(this.transform, paramShape);
/*      */     }
/* 1904 */     return transformShape(this.transX, this.transY, paramShape);
/*      */   }
/*      */ 
/*      */   public Shape untransformShape(Shape paramShape)
/*      */   {
/* 1909 */     if (paramShape == null) {
/* 1910 */       return null;
/*      */     }
/* 1912 */     if (this.transformState > 1) {
/*      */       try {
/* 1914 */         return transformShape(this.transform.createInverse(), paramShape);
/*      */       } catch (NoninvertibleTransformException localNoninvertibleTransformException) {
/* 1916 */         return null;
/*      */       }
/*      */     }
/* 1919 */     return transformShape(-this.transX, -this.transY, paramShape);
/*      */   }
/*      */ 
/*      */   protected static Shape transformShape(int paramInt1, int paramInt2, Shape paramShape)
/*      */   {
/* 1924 */     if (paramShape == null) {
/* 1925 */       return null;
/*      */     }
/*      */ 
/* 1928 */     if ((paramShape instanceof Rectangle)) {
/* 1929 */       localObject = paramShape.getBounds();
/* 1930 */       ((Rectangle)localObject).translate(paramInt1, paramInt2);
/* 1931 */       return localObject;
/*      */     }
/* 1933 */     if ((paramShape instanceof Rectangle2D)) {
/* 1934 */       localObject = (Rectangle2D)paramShape;
/* 1935 */       return new Rectangle2D.Double(((Rectangle2D)localObject).getX() + paramInt1, ((Rectangle2D)localObject).getY() + paramInt2, ((Rectangle2D)localObject).getWidth(), ((Rectangle2D)localObject).getHeight());
/*      */     }
/*      */ 
/* 1941 */     if ((paramInt1 == 0) && (paramInt2 == 0)) {
/* 1942 */       return cloneShape(paramShape);
/*      */     }
/*      */ 
/* 1945 */     Object localObject = AffineTransform.getTranslateInstance(paramInt1, paramInt2);
/* 1946 */     return ((AffineTransform)localObject).createTransformedShape(paramShape);
/*      */   }
/*      */ 
/*      */   protected static Shape transformShape(AffineTransform paramAffineTransform, Shape paramShape) {
/* 1950 */     if (paramShape == null) {
/* 1951 */       return null;
/*      */     }
/*      */ 
/* 1954 */     if (((paramShape instanceof Rectangle2D)) && ((paramAffineTransform.getType() & 0x30) == 0))
/*      */     {
/* 1957 */       Rectangle2D localRectangle2D = (Rectangle2D)paramShape;
/* 1958 */       double[] arrayOfDouble = new double[4];
/* 1959 */       arrayOfDouble[0] = localRectangle2D.getX();
/* 1960 */       arrayOfDouble[1] = localRectangle2D.getY();
/* 1961 */       arrayOfDouble[2] = (arrayOfDouble[0] + localRectangle2D.getWidth());
/* 1962 */       arrayOfDouble[3] = (arrayOfDouble[1] + localRectangle2D.getHeight());
/* 1963 */       paramAffineTransform.transform(arrayOfDouble, 0, arrayOfDouble, 0, 2);
/* 1964 */       fixRectangleOrientation(arrayOfDouble, localRectangle2D);
/* 1965 */       return new Rectangle2D.Double(arrayOfDouble[0], arrayOfDouble[1], arrayOfDouble[2] - arrayOfDouble[0], arrayOfDouble[3] - arrayOfDouble[1]);
/*      */     }
/*      */ 
/* 1970 */     if (paramAffineTransform.isIdentity()) {
/* 1971 */       return cloneShape(paramShape);
/*      */     }
/*      */ 
/* 1974 */     return paramAffineTransform.createTransformedShape(paramShape);
/*      */   }
/*      */ 
/*      */   private static void fixRectangleOrientation(double[] paramArrayOfDouble, Rectangle2D paramRectangle2D)
/*      */   {
/*      */     double d;
/* 1981 */     if ((paramRectangle2D.getWidth() > 0.0D ? 1 : 0) != (paramArrayOfDouble[2] - paramArrayOfDouble[0] > 0.0D ? 1 : 0)) {
/* 1982 */       d = paramArrayOfDouble[0];
/* 1983 */       paramArrayOfDouble[0] = paramArrayOfDouble[2];
/* 1984 */       paramArrayOfDouble[2] = d;
/*      */     }
/* 1986 */     if ((paramRectangle2D.getHeight() > 0.0D ? 1 : 0) != (paramArrayOfDouble[3] - paramArrayOfDouble[1] > 0.0D ? 1 : 0)) {
/* 1987 */       d = paramArrayOfDouble[1];
/* 1988 */       paramArrayOfDouble[1] = paramArrayOfDouble[3];
/* 1989 */       paramArrayOfDouble[3] = d;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void clipRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 1994 */     clip(new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4));
/*      */   }
/*      */ 
/*      */   public void setClip(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 1998 */     setClip(new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4));
/*      */   }
/*      */ 
/*      */   public Shape getClip() {
/* 2002 */     return untransformShape(this.usrClip);
/*      */   }
/*      */ 
/*      */   public void setClip(Shape paramShape) {
/* 2006 */     this.usrClip = transformShape(paramShape);
/* 2007 */     validateCompClip();
/*      */   }
/*      */ 
/*      */   public void clip(Shape paramShape)
/*      */   {
/* 2019 */     paramShape = transformShape(paramShape);
/* 2020 */     if (this.usrClip != null) {
/* 2021 */       paramShape = intersectShapes(this.usrClip, paramShape, true, true);
/*      */     }
/* 2023 */     this.usrClip = paramShape;
/* 2024 */     validateCompClip();
/*      */   }
/*      */ 
/*      */   public void setPaintMode() {
/* 2028 */     setComposite(AlphaComposite.SrcOver);
/*      */   }
/*      */ 
/*      */   public void setXORMode(Color paramColor) {
/* 2032 */     if (paramColor == null) {
/* 2033 */       throw new IllegalArgumentException("null XORColor");
/*      */     }
/* 2035 */     setComposite(new XORComposite(paramColor, this.surfaceData));
/*      */   }
/*      */ 
/*      */   public void copyArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*      */     try
/*      */     {
/* 2043 */       doCopyArea(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 2046 */         revalidateAll();
/* 2047 */         doCopyArea(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 2054 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void doCopyArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
/* 2059 */     if ((paramInt3 <= 0) || (paramInt4 <= 0)) {
/* 2060 */       return;
/*      */     }
/* 2062 */     SurfaceData localSurfaceData = this.surfaceData;
/* 2063 */     if (localSurfaceData.copyArea(this, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6)) {
/* 2064 */       return;
/*      */     }
/* 2066 */     if (this.transformState >= 3) {
/* 2067 */       throw new InternalError("transformed copyArea not implemented yet");
/*      */     }
/*      */ 
/* 2072 */     Region localRegion = getCompClip();
/*      */ 
/* 2074 */     Composite localComposite = this.composite;
/* 2075 */     if (this.lastCAcomp != localComposite) {
/* 2076 */       localObject = localSurfaceData.getSurfaceType();
/* 2077 */       CompositeType localCompositeType = this.imageComp;
/* 2078 */       if ((CompositeType.SrcOverNoEa.equals(localCompositeType)) && (localSurfaceData.getTransparency() == 1))
/*      */       {
/* 2081 */         localCompositeType = CompositeType.SrcNoEa;
/*      */       }
/* 2083 */       this.lastCAblit = Blit.locate((SurfaceType)localObject, localCompositeType, (SurfaceType)localObject);
/* 2084 */       this.lastCAcomp = localComposite;
/*      */     }
/*      */ 
/* 2087 */     paramInt1 += this.transX;
/* 2088 */     paramInt2 += this.transY;
/*      */ 
/* 2090 */     Object localObject = this.lastCAblit;
/*      */     int i;
/*      */     int j;
/* 2091 */     if ((paramInt6 == 0) && (paramInt5 > 0) && (paramInt5 < paramInt3)) {
/* 2092 */       while (paramInt3 > 0) {
/* 2093 */         i = Math.min(paramInt3, paramInt5);
/* 2094 */         paramInt3 -= i;
/* 2095 */         j = paramInt1 + paramInt3;
/* 2096 */         ((Blit)localObject).Blit(localSurfaceData, localSurfaceData, localComposite, localRegion, j, paramInt2, j + paramInt5, paramInt2 + paramInt6, i, paramInt4);
/*      */       }
/*      */ 
/* 2099 */       return;
/*      */     }
/* 2101 */     if ((paramInt6 > 0) && (paramInt6 < paramInt4) && (paramInt5 > -paramInt3) && (paramInt5 < paramInt3)) {
/* 2102 */       while (paramInt4 > 0) {
/* 2103 */         i = Math.min(paramInt4, paramInt6);
/* 2104 */         paramInt4 -= i;
/* 2105 */         j = paramInt2 + paramInt4;
/* 2106 */         ((Blit)localObject).Blit(localSurfaceData, localSurfaceData, localComposite, localRegion, paramInt1, j, paramInt1 + paramInt5, j + paramInt6, paramInt3, i);
/*      */       }
/*      */ 
/* 2109 */       return;
/*      */     }
/* 2111 */     ((Blit)localObject).Blit(localSurfaceData, localSurfaceData, localComposite, localRegion, paramInt1, paramInt2, paramInt1 + paramInt5, paramInt2 + paramInt6, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void drawLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*      */     try
/*      */     {
/* 2175 */       this.drawpipe.drawLine(this, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 2178 */         revalidateAll();
/* 2179 */         this.drawpipe.drawLine(this, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 2186 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void drawRoundRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
/*      */     try {
/* 2192 */       this.drawpipe.drawRoundRect(this, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 2195 */         revalidateAll();
/* 2196 */         this.drawpipe.drawRoundRect(this, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 2203 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void fillRoundRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6) {
/*      */     try {
/* 2209 */       this.fillpipe.fillRoundRect(this, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 2212 */         revalidateAll();
/* 2213 */         this.fillpipe.fillRoundRect(this, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 2220 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void drawOval(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*      */     try {
/* 2226 */       this.drawpipe.drawOval(this, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 2229 */         revalidateAll();
/* 2230 */         this.drawpipe.drawOval(this, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 2237 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void fillOval(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*      */     try {
/* 2243 */       this.fillpipe.fillOval(this, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 2246 */         revalidateAll();
/* 2247 */         this.fillpipe.fillOval(this, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 2254 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void drawArc(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*      */     try {
/* 2261 */       this.drawpipe.drawArc(this, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 2264 */         revalidateAll();
/* 2265 */         this.drawpipe.drawArc(this, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 2272 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void fillArc(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*      */     try {
/* 2279 */       this.fillpipe.fillArc(this, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 2282 */         revalidateAll();
/* 2283 */         this.fillpipe.fillArc(this, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 2290 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void drawPolyline(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt) {
/*      */     try {
/* 2296 */       this.drawpipe.drawPolyline(this, paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 2299 */         revalidateAll();
/* 2300 */         this.drawpipe.drawPolyline(this, paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 2307 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void drawPolygon(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt) {
/*      */     try {
/* 2313 */       this.drawpipe.drawPolygon(this, paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 2316 */         revalidateAll();
/* 2317 */         this.drawpipe.drawPolygon(this, paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 2324 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void fillPolygon(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt) {
/*      */     try {
/* 2330 */       this.fillpipe.fillPolygon(this, paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 2333 */         revalidateAll();
/* 2334 */         this.fillpipe.fillPolygon(this, paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 2341 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void drawRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*      */     try {
/* 2347 */       this.drawpipe.drawRect(this, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 2350 */         revalidateAll();
/* 2351 */         this.drawpipe.drawRect(this, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 2358 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void fillRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/*      */     try {
/* 2364 */       this.fillpipe.fillRect(this, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 2367 */         revalidateAll();
/* 2368 */         this.fillpipe.fillRect(this, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 2375 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void revalidateAll()
/*      */   {
/*      */     try
/*      */     {
/* 2386 */       this.surfaceData = this.surfaceData.getReplacement();
/* 2387 */       if (this.surfaceData == null) {
/* 2388 */         this.surfaceData = NullSurfaceData.theInstance;
/*      */       }
/*      */ 
/* 2391 */       invalidatePipe();
/*      */ 
/* 2394 */       setDevClip(this.surfaceData.getBounds());
/*      */ 
/* 2396 */       if (this.paintState <= 1) {
/* 2397 */         validateColor();
/*      */       }
/* 2399 */       if ((this.composite instanceof XORComposite)) {
/* 2400 */         Color localColor = ((XORComposite)this.composite).getXorColor();
/* 2401 */         setComposite(new XORComposite(localColor, this.surfaceData));
/*      */       }
/* 2403 */       validatePipe();
/*      */     }
/*      */     finally
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public void clearRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2413 */     Composite localComposite = this.composite;
/* 2414 */     Paint localPaint = this.paint;
/* 2415 */     setComposite(AlphaComposite.Src);
/* 2416 */     setColor(getBackground());
/* 2417 */     fillRect(paramInt1, paramInt2, paramInt3, paramInt4);
/* 2418 */     setPaint(localPaint);
/* 2419 */     setComposite(localComposite);
/*      */   }
/*      */ 
/*      */   public void draw(Shape paramShape)
/*      */   {
/*      */     try
/*      */     {
/* 2438 */       this.shapepipe.draw(this, paramShape);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 2441 */         revalidateAll();
/* 2442 */         this.shapepipe.draw(this, paramShape);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 2449 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void fill(Shape paramShape)
/*      */   {
/*      */     try
/*      */     {
/* 2468 */       this.shapepipe.fill(this, paramShape);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 2471 */         revalidateAll();
/* 2472 */         this.shapepipe.fill(this, paramShape);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 2479 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean isIntegerTranslation(AffineTransform paramAffineTransform)
/*      */   {
/* 2488 */     if (paramAffineTransform.isIdentity()) {
/* 2489 */       return true;
/*      */     }
/* 2491 */     if (paramAffineTransform.getType() == 1) {
/* 2492 */       double d1 = paramAffineTransform.getTranslateX();
/* 2493 */       double d2 = paramAffineTransform.getTranslateY();
/* 2494 */       return (d1 == (int)d1) && (d2 == (int)d2);
/*      */     }
/* 2496 */     return false;
/*      */   }
/*      */ 
/*      */   private static int getTileIndex(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 2504 */     paramInt1 -= paramInt2;
/* 2505 */     if (paramInt1 < 0) {
/* 2506 */       paramInt1 += 1 - paramInt3;
/*      */     }
/* 2508 */     return paramInt1 / paramInt3;
/*      */   }
/*      */ 
/*      */   private static Rectangle getImageRegion(RenderedImage paramRenderedImage, Region paramRegion, AffineTransform paramAffineTransform1, AffineTransform paramAffineTransform2, int paramInt1, int paramInt2)
/*      */   {
/* 2523 */     Rectangle localRectangle1 = new Rectangle(paramRenderedImage.getMinX(), paramRenderedImage.getMinY(), paramRenderedImage.getWidth(), paramRenderedImage.getHeight());
/*      */ 
/* 2527 */     Rectangle localRectangle2 = null;
/*      */     try {
/* 2529 */       double[] arrayOfDouble = new double[8];
/*      */       double tmp53_52 = paramRegion.getLoX(); arrayOfDouble[2] = tmp53_52; arrayOfDouble[0] = tmp53_52;
/*      */       double tmp68_67 = paramRegion.getHiX(); arrayOfDouble[6] = tmp68_67; arrayOfDouble[4] = tmp68_67;
/*      */       double tmp82_81 = paramRegion.getLoY(); arrayOfDouble[5] = tmp82_81; arrayOfDouble[1] = tmp82_81;
/*      */       double tmp97_96 = paramRegion.getHiY(); arrayOfDouble[7] = tmp97_96; arrayOfDouble[3] = tmp97_96;
/*      */ 
/* 2536 */       paramAffineTransform1.inverseTransform(arrayOfDouble, 0, arrayOfDouble, 0, 4);
/* 2537 */       paramAffineTransform2.inverseTransform(arrayOfDouble, 0, arrayOfDouble, 0, 4);
/*      */       double d2;
/* 2541 */       double d1 = d2 = arrayOfDouble[0];
/*      */       double d4;
/* 2542 */       double d3 = d4 = arrayOfDouble[1];
/*      */ 
/* 2544 */       for (int i = 2; i < 8; ) {
/* 2545 */         double d5 = arrayOfDouble[(i++)];
/* 2546 */         if (d5 < d1)
/* 2547 */           d1 = d5;
/* 2548 */         else if (d5 > d2) {
/* 2549 */           d2 = d5;
/*      */         }
/* 2551 */         d5 = arrayOfDouble[(i++)];
/* 2552 */         if (d5 < d3)
/* 2553 */           d3 = d5;
/* 2554 */         else if (d5 > d4) {
/* 2555 */           d4 = d5;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2561 */       i = (int)d1 - paramInt1;
/* 2562 */       int j = (int)(d2 - d1 + 2 * paramInt1);
/* 2563 */       int k = (int)d3 - paramInt2;
/* 2564 */       int m = (int)(d4 - d3 + 2 * paramInt2);
/*      */ 
/* 2566 */       Rectangle localRectangle3 = new Rectangle(i, k, j, m);
/* 2567 */       localRectangle2 = localRectangle3.intersection(localRectangle1);
/*      */     }
/*      */     catch (NoninvertibleTransformException localNoninvertibleTransformException) {
/* 2570 */       localRectangle2 = localRectangle1;
/*      */     }
/*      */ 
/* 2573 */     return localRectangle2;
/*      */   }
/*      */ 
/*      */   public void drawRenderedImage(RenderedImage paramRenderedImage, AffineTransform paramAffineTransform)
/*      */   {
/* 2597 */     if (paramRenderedImage == null) {
/* 2598 */       return;
/*      */     }
/*      */ 
/* 2602 */     if ((paramRenderedImage instanceof BufferedImage)) {
/* 2603 */       BufferedImage localBufferedImage1 = (BufferedImage)paramRenderedImage;
/* 2604 */       drawImage(localBufferedImage1, paramAffineTransform, null);
/* 2605 */       return;
/*      */     }
/*      */ 
/* 2611 */     int i = (this.transformState <= 1) && (isIntegerTranslation(paramAffineTransform)) ? 1 : 0;
/*      */ 
/* 2616 */     int j = i != 0 ? 0 : 3;
/*      */     Region localRegion;
/*      */     try
/*      */     {
/* 2620 */       localRegion = getCompClip();
/*      */     } catch (InvalidPipeException localInvalidPipeException) {
/* 2622 */       return;
/*      */     }
/*      */ 
/* 2627 */     Rectangle localRectangle = getImageRegion(paramRenderedImage, localRegion, this.transform, paramAffineTransform, j, j);
/*      */ 
/* 2632 */     if ((localRectangle.width <= 0) || (localRectangle.height <= 0)) {
/* 2633 */       return;
/*      */     }
/*      */ 
/* 2641 */     if (i != 0)
/*      */     {
/* 2648 */       drawTranslatedRenderedImage(paramRenderedImage, localRectangle, (int)paramAffineTransform.getTranslateX(), (int)paramAffineTransform.getTranslateY());
/*      */ 
/* 2651 */       return;
/*      */     }
/*      */ 
/* 2655 */     Raster localRaster = paramRenderedImage.getData(localRectangle);
/*      */ 
/* 2660 */     WritableRaster localWritableRaster = Raster.createWritableRaster(localRaster.getSampleModel(), localRaster.getDataBuffer(), null);
/*      */ 
/* 2670 */     int k = localRaster.getMinX();
/* 2671 */     int m = localRaster.getMinY();
/* 2672 */     int n = localRaster.getWidth();
/* 2673 */     int i1 = localRaster.getHeight();
/* 2674 */     int i2 = k - localRaster.getSampleModelTranslateX();
/* 2675 */     int i3 = m - localRaster.getSampleModelTranslateY();
/* 2676 */     if ((i2 != 0) || (i3 != 0) || (n != localWritableRaster.getWidth()) || (i1 != localWritableRaster.getHeight()))
/*      */     {
/* 2678 */       localWritableRaster = localWritableRaster.createWritableChild(i2, i3, n, i1, 0, 0, null);
/*      */     }
/*      */ 
/* 2691 */     AffineTransform localAffineTransform = (AffineTransform)paramAffineTransform.clone();
/* 2692 */     localAffineTransform.translate(k, m);
/*      */ 
/* 2694 */     ColorModel localColorModel = paramRenderedImage.getColorModel();
/* 2695 */     BufferedImage localBufferedImage2 = new BufferedImage(localColorModel, localWritableRaster, localColorModel.isAlphaPremultiplied(), null);
/*      */ 
/* 2699 */     drawImage(localBufferedImage2, localAffineTransform, null);
/*      */   }
/*      */ 
/*      */   private boolean clipTo(Rectangle paramRectangle1, Rectangle paramRectangle2)
/*      */   {
/* 2708 */     int i = Math.max(paramRectangle1.x, paramRectangle2.x);
/* 2709 */     int j = Math.min(paramRectangle1.x + paramRectangle1.width, paramRectangle2.x + paramRectangle2.width);
/* 2710 */     int k = Math.max(paramRectangle1.y, paramRectangle2.y);
/* 2711 */     int m = Math.min(paramRectangle1.y + paramRectangle1.height, paramRectangle2.y + paramRectangle2.height);
/* 2712 */     if ((j - i < 0) || (m - k < 0)) {
/* 2713 */       paramRectangle1.width = -1;
/* 2714 */       paramRectangle1.height = -1;
/* 2715 */       return false;
/*      */     }
/* 2717 */     paramRectangle1.x = i;
/* 2718 */     paramRectangle1.y = k;
/* 2719 */     paramRectangle1.width = (j - i);
/* 2720 */     paramRectangle1.height = (m - k);
/* 2721 */     return true;
/*      */   }
/*      */ 
/*      */   private void drawTranslatedRenderedImage(RenderedImage paramRenderedImage, Rectangle paramRectangle, int paramInt1, int paramInt2)
/*      */   {
/* 2735 */     int i = paramRenderedImage.getTileGridXOffset();
/* 2736 */     int j = paramRenderedImage.getTileGridYOffset();
/* 2737 */     int k = paramRenderedImage.getTileWidth();
/* 2738 */     int m = paramRenderedImage.getTileHeight();
/*      */ 
/* 2741 */     int n = getTileIndex(paramRectangle.x, i, k);
/*      */ 
/* 2743 */     int i1 = getTileIndex(paramRectangle.y, j, m);
/*      */ 
/* 2745 */     int i2 = getTileIndex(paramRectangle.x + paramRectangle.width - 1, i, k);
/*      */ 
/* 2748 */     int i3 = getTileIndex(paramRectangle.y + paramRectangle.height - 1, j, m);
/*      */ 
/* 2753 */     ColorModel localColorModel = paramRenderedImage.getColorModel();
/*      */ 
/* 2756 */     Rectangle localRectangle = new Rectangle();
/*      */ 
/* 2758 */     for (int i4 = i1; i4 <= i3; i4++)
/* 2759 */       for (int i5 = n; i5 <= i2; i5++)
/*      */       {
/* 2761 */         Raster localRaster = paramRenderedImage.getTile(i5, i4);
/*      */ 
/* 2764 */         localRectangle.x = (i5 * k + i);
/* 2765 */         localRectangle.y = (i4 * m + j);
/* 2766 */         localRectangle.width = k;
/* 2767 */         localRectangle.height = m;
/*      */ 
/* 2772 */         clipTo(localRectangle, paramRectangle);
/*      */ 
/* 2775 */         WritableRaster localWritableRaster = null;
/* 2776 */         if ((localRaster instanceof WritableRaster)) {
/* 2777 */           localWritableRaster = (WritableRaster)localRaster;
/*      */         }
/*      */         else
/*      */         {
/* 2781 */           localWritableRaster = Raster.createWritableRaster(localRaster.getSampleModel(), localRaster.getDataBuffer(), null);
/*      */         }
/*      */ 
/* 2789 */         localWritableRaster = localWritableRaster.createWritableChild(localRectangle.x, localRectangle.y, localRectangle.width, localRectangle.height, 0, 0, null);
/*      */ 
/* 2796 */         BufferedImage localBufferedImage = new BufferedImage(localColorModel, localWritableRaster, localColorModel.isAlphaPremultiplied(), null);
/*      */ 
/* 2807 */         copyImage(localBufferedImage, localRectangle.x + paramInt1, localRectangle.y + paramInt2, 0, 0, localRectangle.width, localRectangle.height, null, null);
/*      */       }
/*      */   }
/*      */ 
/*      */   public void drawRenderableImage(RenderableImage paramRenderableImage, AffineTransform paramAffineTransform)
/*      */   {
/* 2817 */     if (paramRenderableImage == null) {
/* 2818 */       return; } 
/*      */ AffineTransform localAffineTransform1 = this.transform;
/* 2822 */     AffineTransform localAffineTransform2 = new AffineTransform(paramAffineTransform);
/* 2823 */     localAffineTransform2.concatenate(localAffineTransform1);
/*      */ 
/* 2826 */     RenderContext localRenderContext = new RenderContext(localAffineTransform2);
/*      */     AffineTransform localAffineTransform3;
/*      */     try { localAffineTransform3 = localAffineTransform1.createInverse();
/*      */     } catch (NoninvertibleTransformException localNoninvertibleTransformException) {
/* 2831 */       localRenderContext = new RenderContext(localAffineTransform1);
/* 2832 */       localAffineTransform3 = new AffineTransform();
/*      */     }
/*      */ 
/* 2835 */     RenderedImage localRenderedImage = paramRenderableImage.createRendering(localRenderContext);
/* 2836 */     drawRenderedImage(localRenderedImage, localAffineTransform3);
/*      */   }
/*      */ 
/*      */   protected Rectangle transformBounds(Rectangle paramRectangle, AffineTransform paramAffineTransform)
/*      */   {
/* 2846 */     if (paramAffineTransform.isIdentity()) {
/* 2847 */       return paramRectangle;
/*      */     }
/*      */ 
/* 2850 */     Shape localShape = transformShape(paramAffineTransform, paramRectangle);
/* 2851 */     return localShape.getBounds();
/*      */   }
/*      */ 
/*      */   public void drawString(String paramString, int paramInt1, int paramInt2)
/*      */   {
/* 2856 */     if (paramString == null) {
/* 2857 */       throw new NullPointerException("String is null");
/*      */     }
/*      */ 
/* 2860 */     if (this.font.hasLayoutAttributes()) {
/* 2861 */       if (paramString.length() == 0) {
/* 2862 */         return;
/*      */       }
/* 2864 */       new TextLayout(paramString, this.font, getFontRenderContext()).draw(this, paramInt1, paramInt2);
/* 2865 */       return;
/*      */     }
/*      */     try
/*      */     {
/* 2869 */       this.textpipe.drawString(this, paramString, paramInt1, paramInt2);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 2872 */         revalidateAll();
/* 2873 */         this.textpipe.drawString(this, paramString, paramInt1, paramInt2);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 2880 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void drawString(String paramString, float paramFloat1, float paramFloat2) {
/* 2885 */     if (paramString == null) {
/* 2886 */       throw new NullPointerException("String is null");
/*      */     }
/*      */ 
/* 2889 */     if (this.font.hasLayoutAttributes()) {
/* 2890 */       if (paramString.length() == 0) {
/* 2891 */         return;
/*      */       }
/* 2893 */       new TextLayout(paramString, this.font, getFontRenderContext()).draw(this, paramFloat1, paramFloat2);
/* 2894 */       return;
/*      */     }
/*      */     try
/*      */     {
/* 2898 */       this.textpipe.drawString(this, paramString, paramFloat1, paramFloat2);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 2901 */         revalidateAll();
/* 2902 */         this.textpipe.drawString(this, paramString, paramFloat1, paramFloat2);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 2909 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void drawString(AttributedCharacterIterator paramAttributedCharacterIterator, int paramInt1, int paramInt2)
/*      */   {
/* 2915 */     if (paramAttributedCharacterIterator == null) {
/* 2916 */       throw new NullPointerException("AttributedCharacterIterator is null");
/*      */     }
/* 2918 */     if (paramAttributedCharacterIterator.getBeginIndex() == paramAttributedCharacterIterator.getEndIndex()) {
/* 2919 */       return;
/*      */     }
/* 2921 */     TextLayout localTextLayout = new TextLayout(paramAttributedCharacterIterator, getFontRenderContext());
/* 2922 */     localTextLayout.draw(this, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void drawString(AttributedCharacterIterator paramAttributedCharacterIterator, float paramFloat1, float paramFloat2)
/*      */   {
/* 2927 */     if (paramAttributedCharacterIterator == null) {
/* 2928 */       throw new NullPointerException("AttributedCharacterIterator is null");
/*      */     }
/* 2930 */     if (paramAttributedCharacterIterator.getBeginIndex() == paramAttributedCharacterIterator.getEndIndex()) {
/* 2931 */       return;
/*      */     }
/* 2933 */     TextLayout localTextLayout = new TextLayout(paramAttributedCharacterIterator, getFontRenderContext());
/* 2934 */     localTextLayout.draw(this, paramFloat1, paramFloat2);
/*      */   }
/*      */ 
/*      */   public void drawGlyphVector(GlyphVector paramGlyphVector, float paramFloat1, float paramFloat2)
/*      */   {
/* 2939 */     if (paramGlyphVector == null) {
/* 2940 */       throw new NullPointerException("GlyphVector is null");
/*      */     }
/*      */     try
/*      */     {
/* 2944 */       this.textpipe.drawGlyphVector(this, paramGlyphVector, paramFloat1, paramFloat2);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 2947 */         revalidateAll();
/* 2948 */         this.textpipe.drawGlyphVector(this, paramGlyphVector, paramFloat1, paramFloat2);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 2955 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void drawChars(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 2961 */     if (paramArrayOfChar == null) {
/* 2962 */       throw new NullPointerException("char data is null");
/*      */     }
/* 2964 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfChar.length)) {
/* 2965 */       throw new ArrayIndexOutOfBoundsException("bad offset/length");
/*      */     }
/* 2967 */     if (this.font.hasLayoutAttributes()) {
/* 2968 */       if (paramArrayOfChar.length == 0) {
/* 2969 */         return;
/*      */       }
/* 2971 */       new TextLayout(new String(paramArrayOfChar, paramInt1, paramInt2), this.font, getFontRenderContext()).draw(this, paramInt3, paramInt4);
/*      */ 
/* 2973 */       return;
/*      */     }
/*      */     try
/*      */     {
/* 2977 */       this.textpipe.drawChars(this, paramArrayOfChar, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 2980 */         revalidateAll();
/* 2981 */         this.textpipe.drawChars(this, paramArrayOfChar, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 2988 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void drawBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
/* 2993 */     if (paramArrayOfByte == null) {
/* 2994 */       throw new NullPointerException("byte data is null");
/*      */     }
/* 2996 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (paramInt1 + paramInt2 > paramArrayOfByte.length)) {
/* 2997 */       throw new ArrayIndexOutOfBoundsException("bad offset/length");
/*      */     }
/*      */ 
/* 3000 */     char[] arrayOfChar = new char[paramInt2];
/* 3001 */     for (int i = paramInt2; i-- > 0; ) {
/* 3002 */       arrayOfChar[i] = ((char)(paramArrayOfByte[(i + paramInt1)] & 0xFF));
/*      */     }
/* 3004 */     if (this.font.hasLayoutAttributes()) {
/* 3005 */       if (paramArrayOfByte.length == 0) {
/* 3006 */         return;
/*      */       }
/* 3008 */       new TextLayout(new String(arrayOfChar), this.font, getFontRenderContext()).draw(this, paramInt3, paramInt4);
/*      */ 
/* 3010 */       return;
/*      */     }
/*      */     try
/*      */     {
/* 3014 */       this.textpipe.drawChars(this, arrayOfChar, 0, paramInt2, paramInt3, paramInt4);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 3017 */         revalidateAll();
/* 3018 */         this.textpipe.drawChars(this, arrayOfChar, 0, paramInt2, paramInt3, paramInt4);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 3025 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static boolean isHiDPIImage(Image paramImage)
/*      */   {
/* 3031 */     return SurfaceManager.getImageScale(paramImage) != 1;
/*      */   }
/*      */ 
/*      */   private boolean drawHiDPIImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/* 3037 */     int i = SurfaceManager.getImageScale(paramImage);
/* 3038 */     paramInt5 = Region.clipScale(paramInt5, i);
/* 3039 */     paramInt7 = Region.clipScale(paramInt7, i);
/* 3040 */     paramInt6 = Region.clipScale(paramInt6, i);
/* 3041 */     paramInt8 = Region.clipScale(paramInt8, i);
/*      */     try {
/* 3043 */       return this.imagepipe.scaleImage(this, paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramColor, paramImageObserver);
/*      */     }
/*      */     catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 3047 */         revalidateAll();
/* 3048 */         return this.imagepipe.scaleImage(this, paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramColor, paramImageObserver);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/* 3054 */         return false;
/*      */       }
/*      */     } finally {
/* 3057 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, ImageObserver paramImageObserver)
/*      */   {
/* 3067 */     return drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, null, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean copyImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/*      */     try
/*      */     {
/* 3082 */       return this.imagepipe.copyImage(this, paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramColor, paramImageObserver);
/*      */     }
/*      */     catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 3086 */         revalidateAll();
/* 3087 */         return this.imagepipe.copyImage(this, paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramColor, paramImageObserver);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/* 3093 */         return false;
/*      */       }
/*      */     } finally {
/* 3096 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/* 3107 */     if (paramImage == null) {
/* 3108 */       return true;
/*      */     }
/*      */ 
/* 3111 */     if ((paramInt3 == 0) || (paramInt4 == 0)) {
/* 3112 */       return true;
/*      */     }
/*      */ 
/* 3115 */     int i = paramImage.getWidth(null);
/* 3116 */     int j = paramImage.getHeight(null);
/* 3117 */     if (isHiDPIImage(paramImage)) {
/* 3118 */       return drawHiDPIImage(paramImage, paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4, 0, 0, i, j, paramColor, paramImageObserver);
/*      */     }
/*      */ 
/* 3122 */     if ((paramInt3 == i) && (paramInt4 == j)) {
/* 3123 */       return copyImage(paramImage, paramInt1, paramInt2, 0, 0, paramInt3, paramInt4, paramColor, paramImageObserver);
/*      */     }
/*      */     try
/*      */     {
/* 3127 */       return this.imagepipe.scaleImage(this, paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramColor, paramImageObserver);
/*      */     }
/*      */     catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 3131 */         revalidateAll();
/* 3132 */         return this.imagepipe.scaleImage(this, paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramColor, paramImageObserver);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/* 3138 */         return false;
/*      */       }
/*      */     } finally {
/* 3141 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, ImageObserver paramImageObserver)
/*      */   {
/* 3149 */     return drawImage(paramImage, paramInt1, paramInt2, null, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/* 3159 */     if (paramImage == null) {
/* 3160 */       return true;
/*      */     }
/*      */ 
/* 3163 */     if (isHiDPIImage(paramImage)) {
/* 3164 */       int i = paramImage.getWidth(null);
/* 3165 */       int j = paramImage.getHeight(null);
/* 3166 */       return drawHiDPIImage(paramImage, paramInt1, paramInt2, paramInt1 + i, paramInt2 + j, 0, 0, i, j, paramColor, paramImageObserver);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 3171 */       return this.imagepipe.copyImage(this, paramImage, paramInt1, paramInt2, paramColor, paramImageObserver);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 3174 */         revalidateAll();
/* 3175 */         return this.imagepipe.copyImage(this, paramImage, paramInt1, paramInt2, paramColor, paramImageObserver);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/* 3180 */         return false;
/*      */       }
/*      */     } finally {
/* 3183 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, ImageObserver paramImageObserver)
/*      */   {
/* 3195 */     return drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, null, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/* 3208 */     if (paramImage == null) {
/* 3209 */       return true;
/*      */     }
/*      */ 
/* 3212 */     if ((paramInt1 == paramInt3) || (paramInt2 == paramInt4) || (paramInt5 == paramInt7) || (paramInt6 == paramInt8))
/*      */     {
/* 3215 */       return true;
/*      */     }
/*      */ 
/* 3218 */     if (isHiDPIImage(paramImage))
/* 3219 */       return drawHiDPIImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramColor, paramImageObserver);
/*      */     int k;
/* 3223 */     if ((paramInt7 - paramInt5 == paramInt3 - paramInt1) && (paramInt8 - paramInt6 == paramInt4 - paramInt2))
/*      */     {
/*      */       int n;
/*      */       int i;
/* 3228 */       if (paramInt7 > paramInt5) {
/* 3229 */         n = paramInt7 - paramInt5;
/* 3230 */         i = paramInt5;
/* 3231 */         k = paramInt1;
/*      */       } else {
/* 3233 */         n = paramInt5 - paramInt7;
/* 3234 */         i = paramInt7;
/* 3235 */         k = paramInt3;
/*      */       }
/*      */       int i1;
/*      */       int j;
/*      */       int m;
/* 3237 */       if (paramInt8 > paramInt6) {
/* 3238 */         i1 = paramInt8 - paramInt6;
/* 3239 */         j = paramInt6;
/* 3240 */         m = paramInt2;
/*      */       } else {
/* 3242 */         i1 = paramInt6 - paramInt8;
/* 3243 */         j = paramInt8;
/* 3244 */         m = paramInt4;
/*      */       }
/* 3246 */       return copyImage(paramImage, k, m, i, j, n, i1, paramColor, paramImageObserver);
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 3251 */       return this.imagepipe.scaleImage(this, paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramColor, paramImageObserver);
/*      */     }
/*      */     catch (InvalidPipeException localInvalidPipeException1)
/*      */     {
/*      */       try {
/* 3256 */         revalidateAll();
/* 3257 */         return this.imagepipe.scaleImage(this, paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramColor, paramImageObserver);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/* 3264 */         return 0;
/*      */       }
/*      */     } finally {
/* 3267 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, AffineTransform paramAffineTransform, ImageObserver paramImageObserver)
/*      */   {
/* 3293 */     if (paramImage == null) {
/* 3294 */       return true;
/*      */     }
/*      */ 
/* 3297 */     if ((paramAffineTransform == null) || (paramAffineTransform.isIdentity())) {
/* 3298 */       return drawImage(paramImage, 0, 0, null, paramImageObserver);
/*      */     }
/*      */ 
/* 3301 */     if (isHiDPIImage(paramImage)) {
/* 3302 */       int i = paramImage.getWidth(null);
/* 3303 */       int j = paramImage.getHeight(null);
/* 3304 */       AffineTransform localAffineTransform = new AffineTransform(this.transform);
/* 3305 */       transform(paramAffineTransform);
/* 3306 */       boolean bool4 = drawHiDPIImage(paramImage, 0, 0, i, j, 0, 0, i, j, null, paramImageObserver);
/*      */ 
/* 3308 */       this.transform.setTransform(localAffineTransform);
/* 3309 */       invalidateTransform();
/* 3310 */       return bool4;
/*      */     }
/*      */     try
/*      */     {
/* 3314 */       return this.imagepipe.transformImage(this, paramImage, paramAffineTransform, paramImageObserver);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 3317 */         revalidateAll();
/* 3318 */         return this.imagepipe.transformImage(this, paramImage, paramAffineTransform, paramImageObserver);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/* 3323 */         return false;
/*      */       }
/*      */     } finally {
/* 3326 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void drawImage(BufferedImage paramBufferedImage, BufferedImageOp paramBufferedImageOp, int paramInt1, int paramInt2)
/*      */   {
/* 3335 */     if (paramBufferedImage == null) {
/* 3336 */       return;
/*      */     }
/*      */     try
/*      */     {
/* 3340 */       this.imagepipe.transformImage(this, paramBufferedImage, paramBufferedImageOp, paramInt1, paramInt2);
/*      */     } catch (InvalidPipeException localInvalidPipeException1) {
/*      */       try {
/* 3343 */         revalidateAll();
/* 3344 */         this.imagepipe.transformImage(this, paramBufferedImage, paramBufferedImageOp, paramInt1, paramInt2);
/*      */       }
/*      */       catch (InvalidPipeException localInvalidPipeException2)
/*      */       {
/*      */       }
/*      */     }
/*      */     finally {
/* 3351 */       this.surfaceData.markDirty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public FontRenderContext getFontRenderContext()
/*      */   {
/* 3360 */     if (this.cachedFRC == null) {
/* 3361 */       int i = this.textAntialiasHint;
/* 3362 */       if ((i == 0) && (this.antialiasHint == 2))
/*      */       {
/* 3364 */         i = 2;
/*      */       }
/*      */ 
/* 3367 */       AffineTransform localAffineTransform = null;
/* 3368 */       if (this.transformState >= 3) {
/* 3369 */         if ((this.transform.getTranslateX() == 0.0D) && (this.transform.getTranslateY() == 0.0D))
/*      */         {
/* 3371 */           localAffineTransform = this.transform;
/*      */         }
/* 3373 */         else localAffineTransform = new AffineTransform(this.transform.getScaleX(), this.transform.getShearY(), this.transform.getShearX(), this.transform.getScaleY(), 0.0D, 0.0D);
/*      */ 
/*      */       }
/*      */ 
/* 3380 */       this.cachedFRC = new FontRenderContext(localAffineTransform, SunHints.Value.get(2, i), SunHints.Value.get(3, this.fractionalMetricsHint));
/*      */     }
/*      */ 
/* 3385 */     return this.cachedFRC;
/*      */   }
/*      */ 
/*      */   public void dispose()
/*      */   {
/* 3399 */     this.surfaceData = NullSurfaceData.theInstance;
/* 3400 */     invalidatePipe();
/*      */   }
/*      */ 
/*      */   public void finalize()
/*      */   {
/*      */   }
/*      */ 
/*      */   public Object getDestination()
/*      */   {
/* 3424 */     return this.surfaceData.getDestination();
/*      */   }
/*      */ 
/*      */   public Surface getDestSurface()
/*      */   {
/* 3434 */     return this.surfaceData;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  228 */     if (PerformanceLogger.loggingEnabled()) {
/*  229 */       PerformanceLogger.setTime("SunGraphics2D static initialization");
/*      */     }
/*      */ 
/*  385 */     invalidpipe = new ValidatePipe();
/*      */ 
/*  536 */     IDENT_MATRIX = new double[] { 1.0D, 0.0D, 0.0D, 1.0D };
/*  537 */     IDENT_ATX = new AffineTransform();
/*      */ 
/*  542 */     textTxArr = new double[17][];
/*  543 */     textAtArr = new AffineTransform[17];
/*      */ 
/*  547 */     for (int i = 8; i < 17; i++) {
/*  548 */       textTxArr[i] = { i, 0.0D, 0.0D, i };
/*  549 */       textAtArr[i] = new AffineTransform(textTxArr[i]);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.SunGraphics2D
 * JD-Core Version:    0.6.2
 */