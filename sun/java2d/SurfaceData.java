/*      */ package sun.java2d;
/*      */ 
/*      */ import java.awt.AWTPermission;
/*      */ import java.awt.Color;
/*      */ import java.awt.GraphicsConfiguration;
/*      */ import java.awt.Image;
/*      */ import java.awt.Paint;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Transparency;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.awt.image.IndexColorModel;
/*      */ import java.awt.image.Raster;
/*      */ import java.security.Permission;
/*      */ import sun.awt.image.SurfaceManager;
/*      */ import sun.java2d.loops.CompositeType;
/*      */ import sun.java2d.loops.DrawGlyphList;
/*      */ import sun.java2d.loops.DrawGlyphListAA;
/*      */ import sun.java2d.loops.DrawGlyphListLCD;
/*      */ import sun.java2d.loops.DrawLine;
/*      */ import sun.java2d.loops.DrawParallelogram;
/*      */ import sun.java2d.loops.DrawPath;
/*      */ import sun.java2d.loops.DrawPolygons;
/*      */ import sun.java2d.loops.DrawRect;
/*      */ import sun.java2d.loops.FillParallelogram;
/*      */ import sun.java2d.loops.FillPath;
/*      */ import sun.java2d.loops.FillRect;
/*      */ import sun.java2d.loops.FillSpans;
/*      */ import sun.java2d.loops.FontInfo;
/*      */ import sun.java2d.loops.MaskFill;
/*      */ import sun.java2d.loops.RenderCache;
/*      */ import sun.java2d.loops.RenderLoops;
/*      */ import sun.java2d.loops.SurfaceType;
/*      */ import sun.java2d.pipe.AAShapePipe;
/*      */ import sun.java2d.pipe.AATextRenderer;
/*      */ import sun.java2d.pipe.AlphaColorPipe;
/*      */ import sun.java2d.pipe.AlphaPaintPipe;
/*      */ import sun.java2d.pipe.CompositePipe;
/*      */ import sun.java2d.pipe.DrawImage;
/*      */ import sun.java2d.pipe.DrawImagePipe;
/*      */ import sun.java2d.pipe.GeneralCompositePipe;
/*      */ import sun.java2d.pipe.LCDTextRenderer;
/*      */ import sun.java2d.pipe.LoopBasedPipe;
/*      */ import sun.java2d.pipe.LoopPipe;
/*      */ import sun.java2d.pipe.OutlineTextRenderer;
/*      */ import sun.java2d.pipe.ParallelogramPipe;
/*      */ import sun.java2d.pipe.PixelDrawPipe;
/*      */ import sun.java2d.pipe.PixelFillPipe;
/*      */ import sun.java2d.pipe.PixelToParallelogramConverter;
/*      */ import sun.java2d.pipe.PixelToShapeConverter;
/*      */ import sun.java2d.pipe.ShapeDrawPipe;
/*      */ import sun.java2d.pipe.SolidTextRenderer;
/*      */ import sun.java2d.pipe.SpanClipRenderer;
/*      */ import sun.java2d.pipe.SpanShapeRenderer;
/*      */ import sun.java2d.pipe.SpanShapeRenderer.Composite;
/*      */ import sun.java2d.pipe.TextPipe;
/*      */ import sun.java2d.pipe.TextRenderer;
/*      */ 
/*      */ public abstract class SurfaceData
/*      */   implements Transparency, DisposerTarget, StateTrackable, Surface
/*      */ {
/*      */   private long pData;
/*      */   private boolean valid;
/*      */   private boolean surfaceLost;
/*      */   private SurfaceType surfaceType;
/*      */   private ColorModel colorModel;
/*  110 */   private Object disposerReferent = new Object();
/*      */   private Object blitProxyKey;
/*      */   private StateTrackableDelegate stateDelegate;
/*  448 */   protected static final LoopPipe colorPrimitives = new LoopPipe();
/*      */ 
/*  450 */   public static final TextPipe outlineTextRenderer = new OutlineTextRenderer();
/*  451 */   public static final TextPipe solidTextRenderer = new SolidTextRenderer();
/*  452 */   public static final TextPipe aaTextRenderer = new AATextRenderer();
/*  453 */   public static final TextPipe lcdTextRenderer = new LCDTextRenderer();
/*      */ 
/*  455 */   protected static final AlphaColorPipe colorPipe = new AlphaColorPipe();
/*      */ 
/*  457 */   protected static final PixelToShapeConverter colorViaShape = new PixelToShapeLoopConverter(colorPrimitives);
/*  458 */   protected static final PixelToParallelogramConverter colorViaPgram = new PixelToPgramLoopConverter(colorPrimitives, colorPrimitives, 1.0D, 0.25D, true);
/*      */ 
/*  461 */   protected static final TextPipe colorText = new TextRenderer(colorPipe);
/*  462 */   protected static final CompositePipe clipColorPipe = new SpanClipRenderer(colorPipe);
/*  463 */   protected static final TextPipe clipColorText = new TextRenderer(clipColorPipe);
/*  464 */   protected static final AAShapePipe AAColorShape = new AAShapePipe(colorPipe);
/*  465 */   protected static final PixelToParallelogramConverter AAColorViaShape = makeConverter(AAColorShape);
/*  466 */   protected static final PixelToParallelogramConverter AAColorViaPgram = makeConverter(AAColorShape, colorPipe);
/*  467 */   protected static final AAShapePipe AAClipColorShape = new AAShapePipe(clipColorPipe);
/*  468 */   protected static final PixelToParallelogramConverter AAClipColorViaShape = makeConverter(AAClipColorShape);
/*      */ 
/*  470 */   protected static final CompositePipe paintPipe = new AlphaPaintPipe();
/*  471 */   protected static final SpanShapeRenderer paintShape = new SpanShapeRenderer.Composite(paintPipe);
/*  472 */   protected static final PixelToShapeConverter paintViaShape = new PixelToShapeConverter(paintShape);
/*  473 */   protected static final TextPipe paintText = new TextRenderer(paintPipe);
/*  474 */   protected static final CompositePipe clipPaintPipe = new SpanClipRenderer(paintPipe);
/*  475 */   protected static final TextPipe clipPaintText = new TextRenderer(clipPaintPipe);
/*  476 */   protected static final AAShapePipe AAPaintShape = new AAShapePipe(paintPipe);
/*  477 */   protected static final PixelToParallelogramConverter AAPaintViaShape = makeConverter(AAPaintShape);
/*  478 */   protected static final AAShapePipe AAClipPaintShape = new AAShapePipe(clipPaintPipe);
/*  479 */   protected static final PixelToParallelogramConverter AAClipPaintViaShape = makeConverter(AAClipPaintShape);
/*      */ 
/*  481 */   protected static final CompositePipe compPipe = new GeneralCompositePipe();
/*  482 */   protected static final SpanShapeRenderer compShape = new SpanShapeRenderer.Composite(compPipe);
/*  483 */   protected static final PixelToShapeConverter compViaShape = new PixelToShapeConverter(compShape);
/*  484 */   protected static final TextPipe compText = new TextRenderer(compPipe);
/*  485 */   protected static final CompositePipe clipCompPipe = new SpanClipRenderer(compPipe);
/*  486 */   protected static final TextPipe clipCompText = new TextRenderer(clipCompPipe);
/*  487 */   protected static final AAShapePipe AACompShape = new AAShapePipe(compPipe);
/*  488 */   protected static final PixelToParallelogramConverter AACompViaShape = makeConverter(AACompShape);
/*  489 */   protected static final AAShapePipe AAClipCompShape = new AAShapePipe(clipCompPipe);
/*  490 */   protected static final PixelToParallelogramConverter AAClipCompViaShape = makeConverter(AAClipCompShape);
/*      */ 
/*  492 */   protected static final DrawImagePipe imagepipe = new DrawImage();
/*      */   static final int LOOP_UNKNOWN = 0;
/*      */   static final int LOOP_FOUND = 1;
/*      */   static final int LOOP_NOTFOUND = 2;
/*      */   int haveLCDLoop;
/*      */   int havePgramXORLoop;
/*      */   int havePgramSolidLoop;
/*  845 */   private static RenderCache loopcache = new RenderCache(30);
/*      */   static Permission compPermission;
/*      */ 
/*      */   private static native void initIDs();
/*      */ 
/*      */   protected SurfaceData(SurfaceType paramSurfaceType, ColorModel paramColorModel)
/*      */   {
/*  122 */     this(StateTrackable.State.STABLE, paramSurfaceType, paramColorModel);
/*      */   }
/*      */ 
/*      */   protected SurfaceData(StateTrackable.State paramState, SurfaceType paramSurfaceType, ColorModel paramColorModel) {
/*  126 */     this(StateTrackableDelegate.createInstance(paramState), paramSurfaceType, paramColorModel);
/*      */   }
/*      */ 
/*      */   protected SurfaceData(StateTrackableDelegate paramStateTrackableDelegate, SurfaceType paramSurfaceType, ColorModel paramColorModel)
/*      */   {
/*  132 */     this.stateDelegate = paramStateTrackableDelegate;
/*  133 */     this.colorModel = paramColorModel;
/*  134 */     this.surfaceType = paramSurfaceType;
/*  135 */     this.valid = true;
/*      */   }
/*      */ 
/*      */   protected SurfaceData(StateTrackable.State paramState) {
/*  139 */     this.stateDelegate = StateTrackableDelegate.createInstance(paramState);
/*  140 */     this.valid = true;
/*      */   }
/*      */ 
/*      */   protected void setBlitProxyKey(Object paramObject)
/*      */   {
/*  175 */     if (SurfaceDataProxy.isCachingAllowed())
/*  176 */       this.blitProxyKey = paramObject;
/*      */   }
/*      */ 
/*      */   public SurfaceData getSourceSurfaceData(Image paramImage, int paramInt, CompositeType paramCompositeType, Color paramColor)
/*      */   {
/*  218 */     SurfaceManager localSurfaceManager = SurfaceManager.getManager(paramImage);
/*  219 */     SurfaceData localSurfaceData = localSurfaceManager.getPrimarySurfaceData();
/*  220 */     if ((paramImage.getAccelerationPriority() > 0.0F) && (this.blitProxyKey != null))
/*      */     {
/*  223 */       SurfaceDataProxy localSurfaceDataProxy = (SurfaceDataProxy)localSurfaceManager.getCacheData(this.blitProxyKey);
/*      */ 
/*  225 */       if ((localSurfaceDataProxy == null) || (!localSurfaceDataProxy.isValid())) {
/*  226 */         if (localSurfaceData.getState() == StateTrackable.State.UNTRACKABLE)
/*  227 */           localSurfaceDataProxy = SurfaceDataProxy.UNCACHED;
/*      */         else {
/*  229 */           localSurfaceDataProxy = makeProxyFor(localSurfaceData);
/*      */         }
/*  231 */         localSurfaceManager.setCacheData(this.blitProxyKey, localSurfaceDataProxy);
/*      */       }
/*  233 */       localSurfaceData = localSurfaceDataProxy.replaceData(localSurfaceData, paramInt, paramCompositeType, paramColor);
/*      */     }
/*  235 */     return localSurfaceData;
/*      */   }
/*      */ 
/*      */   public SurfaceDataProxy makeProxyFor(SurfaceData paramSurfaceData)
/*      */   {
/*  264 */     return SurfaceDataProxy.UNCACHED;
/*      */   }
/*      */ 
/*      */   public static SurfaceData getPrimarySurfaceData(Image paramImage)
/*      */   {
/*  273 */     SurfaceManager localSurfaceManager = SurfaceManager.getManager(paramImage);
/*  274 */     return localSurfaceManager.getPrimarySurfaceData();
/*      */   }
/*      */ 
/*      */   public static SurfaceData restoreContents(Image paramImage)
/*      */   {
/*  282 */     SurfaceManager localSurfaceManager = SurfaceManager.getManager(paramImage);
/*  283 */     return localSurfaceManager.restoreContents();
/*      */   }
/*      */ 
/*      */   public StateTrackable.State getState() {
/*  287 */     return this.stateDelegate.getState();
/*      */   }
/*      */ 
/*      */   public StateTracker getStateTracker() {
/*  291 */     return this.stateDelegate.getStateTracker();
/*      */   }
/*      */ 
/*      */   public final void markDirty()
/*      */   {
/*  298 */     this.stateDelegate.markDirty();
/*      */   }
/*      */ 
/*      */   public void setSurfaceLost(boolean paramBoolean)
/*      */   {
/*  307 */     this.surfaceLost = paramBoolean;
/*  308 */     this.stateDelegate.markDirty();
/*      */   }
/*      */ 
/*      */   public boolean isSurfaceLost() {
/*  312 */     return this.surfaceLost;
/*      */   }
/*      */ 
/*      */   public final boolean isValid()
/*      */   {
/*  319 */     return this.valid;
/*      */   }
/*      */ 
/*      */   public Object getDisposerReferent() {
/*  323 */     return this.disposerReferent;
/*      */   }
/*      */ 
/*      */   public long getNativeOps() {
/*  327 */     return this.pData;
/*      */   }
/*      */ 
/*      */   public void invalidate()
/*      */   {
/*  336 */     this.valid = false;
/*  337 */     this.stateDelegate.markDirty();
/*      */   }
/*      */ 
/*      */   public abstract SurfaceData getReplacement();
/*      */ 
/*      */   private static PixelToParallelogramConverter makeConverter(AAShapePipe paramAAShapePipe, ParallelogramPipe paramParallelogramPipe)
/*      */   {
/*  435 */     return new PixelToParallelogramConverter(paramAAShapePipe, paramParallelogramPipe, 0.125D, 0.499D, false);
/*      */   }
/*      */ 
/*      */   private static PixelToParallelogramConverter makeConverter(AAShapePipe paramAAShapePipe)
/*      */   {
/*  444 */     return makeConverter(paramAAShapePipe, paramAAShapePipe);
/*      */   }
/*      */ 
/*      */   public boolean canRenderLCDText(SunGraphics2D paramSunGraphics2D)
/*      */   {
/*  505 */     if ((paramSunGraphics2D.compositeState <= 0) && (paramSunGraphics2D.paintState <= 1) && (paramSunGraphics2D.clipState <= 1) && (paramSunGraphics2D.surfaceData.getTransparency() == 1))
/*      */     {
/*  510 */       if (this.haveLCDLoop == 0) {
/*  511 */         DrawGlyphListLCD localDrawGlyphListLCD = DrawGlyphListLCD.locate(SurfaceType.AnyColor, CompositeType.SrcNoEa, getSurfaceType());
/*      */ 
/*  515 */         this.haveLCDLoop = (localDrawGlyphListLCD != null ? 1 : 2);
/*      */       }
/*  517 */       return this.haveLCDLoop == 1;
/*      */     }
/*  519 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean canRenderParallelograms(SunGraphics2D paramSunGraphics2D) {
/*  523 */     if (paramSunGraphics2D.paintState <= 1)
/*      */     {
/*      */       FillParallelogram localFillParallelogram;
/*  524 */       if (paramSunGraphics2D.compositeState == 2) {
/*  525 */         if (this.havePgramXORLoop == 0) {
/*  526 */           localFillParallelogram = FillParallelogram.locate(SurfaceType.AnyColor, CompositeType.Xor, getSurfaceType());
/*      */ 
/*  530 */           this.havePgramXORLoop = (localFillParallelogram != null ? 1 : 2);
/*      */         }
/*      */ 
/*  533 */         return this.havePgramXORLoop == 1;
/*  534 */       }if ((paramSunGraphics2D.compositeState <= 0) && (paramSunGraphics2D.antialiasHint != 2) && (paramSunGraphics2D.clipState != 2))
/*      */       {
/*  538 */         if (this.havePgramSolidLoop == 0) {
/*  539 */           localFillParallelogram = FillParallelogram.locate(SurfaceType.AnyColor, CompositeType.SrcNoEa, getSurfaceType());
/*      */ 
/*  543 */           this.havePgramSolidLoop = (localFillParallelogram != null ? 1 : 2);
/*      */         }
/*      */ 
/*  546 */         return this.havePgramSolidLoop == 1;
/*      */       }
/*      */     }
/*  549 */     return false;
/*      */   }
/*      */ 
/*      */   public void validatePipe(SunGraphics2D paramSunGraphics2D) {
/*  553 */     paramSunGraphics2D.imagepipe = imagepipe;
/*      */     Object localObject;
/*  554 */     if (paramSunGraphics2D.compositeState == 2) {
/*  555 */       if (paramSunGraphics2D.paintState > 1) {
/*  556 */         paramSunGraphics2D.drawpipe = paintViaShape;
/*  557 */         paramSunGraphics2D.fillpipe = paintViaShape;
/*  558 */         paramSunGraphics2D.shapepipe = paintShape;
/*      */ 
/*  565 */         paramSunGraphics2D.textpipe = outlineTextRenderer;
/*      */       }
/*      */       else {
/*  568 */         if (canRenderParallelograms(paramSunGraphics2D)) {
/*  569 */           localObject = colorViaPgram;
/*      */ 
/*  574 */           paramSunGraphics2D.shapepipe = colorViaPgram;
/*      */         } else {
/*  576 */           localObject = colorViaShape;
/*  577 */           paramSunGraphics2D.shapepipe = colorPrimitives;
/*      */         }
/*  579 */         if (paramSunGraphics2D.clipState == 2) {
/*  580 */           paramSunGraphics2D.drawpipe = ((PixelDrawPipe)localObject);
/*  581 */           paramSunGraphics2D.fillpipe = ((PixelFillPipe)localObject);
/*      */ 
/*  590 */           paramSunGraphics2D.textpipe = outlineTextRenderer;
/*      */         } else {
/*  592 */           if (paramSunGraphics2D.transformState >= 3) {
/*  593 */             paramSunGraphics2D.drawpipe = ((PixelDrawPipe)localObject);
/*  594 */             paramSunGraphics2D.fillpipe = ((PixelFillPipe)localObject);
/*      */           } else {
/*  596 */             if (paramSunGraphics2D.strokeState != 0)
/*  597 */               paramSunGraphics2D.drawpipe = ((PixelDrawPipe)localObject);
/*      */             else {
/*  599 */               paramSunGraphics2D.drawpipe = colorPrimitives;
/*      */             }
/*  601 */             paramSunGraphics2D.fillpipe = colorPrimitives;
/*      */           }
/*  603 */           paramSunGraphics2D.textpipe = solidTextRenderer;
/*      */         }
/*      */       }
/*      */     }
/*  607 */     else if (paramSunGraphics2D.compositeState == 3) {
/*  608 */       if (paramSunGraphics2D.antialiasHint == 2) {
/*  609 */         if (paramSunGraphics2D.clipState == 2) {
/*  610 */           paramSunGraphics2D.drawpipe = AAClipCompViaShape;
/*  611 */           paramSunGraphics2D.fillpipe = AAClipCompViaShape;
/*  612 */           paramSunGraphics2D.shapepipe = AAClipCompViaShape;
/*  613 */           paramSunGraphics2D.textpipe = clipCompText;
/*      */         } else {
/*  615 */           paramSunGraphics2D.drawpipe = AACompViaShape;
/*  616 */           paramSunGraphics2D.fillpipe = AACompViaShape;
/*  617 */           paramSunGraphics2D.shapepipe = AACompViaShape;
/*  618 */           paramSunGraphics2D.textpipe = compText;
/*      */         }
/*      */       } else {
/*  621 */         paramSunGraphics2D.drawpipe = compViaShape;
/*  622 */         paramSunGraphics2D.fillpipe = compViaShape;
/*  623 */         paramSunGraphics2D.shapepipe = compShape;
/*  624 */         if (paramSunGraphics2D.clipState == 2)
/*  625 */           paramSunGraphics2D.textpipe = clipCompText;
/*      */         else
/*  627 */           paramSunGraphics2D.textpipe = compText;
/*      */       }
/*      */     }
/*  630 */     else if (paramSunGraphics2D.antialiasHint == 2) {
/*  631 */       paramSunGraphics2D.alphafill = getMaskFill(paramSunGraphics2D);
/*      */ 
/*  633 */       if (paramSunGraphics2D.alphafill != null) {
/*  634 */         if (paramSunGraphics2D.clipState == 2) {
/*  635 */           paramSunGraphics2D.drawpipe = AAClipColorViaShape;
/*  636 */           paramSunGraphics2D.fillpipe = AAClipColorViaShape;
/*  637 */           paramSunGraphics2D.shapepipe = AAClipColorViaShape;
/*  638 */           paramSunGraphics2D.textpipe = clipColorText;
/*      */         } else {
/*  640 */           localObject = paramSunGraphics2D.alphafill.canDoParallelograms() ? AAColorViaPgram : AAColorViaShape;
/*      */ 
/*  644 */           paramSunGraphics2D.drawpipe = ((PixelDrawPipe)localObject);
/*  645 */           paramSunGraphics2D.fillpipe = ((PixelFillPipe)localObject);
/*  646 */           paramSunGraphics2D.shapepipe = ((ShapeDrawPipe)localObject);
/*  647 */           if ((paramSunGraphics2D.paintState > 1) || (paramSunGraphics2D.compositeState > 0))
/*      */           {
/*  650 */             paramSunGraphics2D.textpipe = colorText;
/*      */           }
/*  652 */           else paramSunGraphics2D.textpipe = getTextPipe(paramSunGraphics2D, true);
/*      */         }
/*      */ 
/*      */       }
/*  656 */       else if (paramSunGraphics2D.clipState == 2) {
/*  657 */         paramSunGraphics2D.drawpipe = AAClipPaintViaShape;
/*  658 */         paramSunGraphics2D.fillpipe = AAClipPaintViaShape;
/*  659 */         paramSunGraphics2D.shapepipe = AAClipPaintViaShape;
/*  660 */         paramSunGraphics2D.textpipe = clipPaintText;
/*      */       } else {
/*  662 */         paramSunGraphics2D.drawpipe = AAPaintViaShape;
/*  663 */         paramSunGraphics2D.fillpipe = AAPaintViaShape;
/*  664 */         paramSunGraphics2D.shapepipe = AAPaintViaShape;
/*  665 */         paramSunGraphics2D.textpipe = paintText;
/*      */       }
/*      */     }
/*  668 */     else if ((paramSunGraphics2D.paintState > 1) || (paramSunGraphics2D.compositeState > 0) || (paramSunGraphics2D.clipState == 2))
/*      */     {
/*  672 */       paramSunGraphics2D.drawpipe = paintViaShape;
/*  673 */       paramSunGraphics2D.fillpipe = paintViaShape;
/*  674 */       paramSunGraphics2D.shapepipe = paintShape;
/*  675 */       paramSunGraphics2D.alphafill = getMaskFill(paramSunGraphics2D);
/*      */ 
/*  677 */       if (paramSunGraphics2D.alphafill != null) {
/*  678 */         if (paramSunGraphics2D.clipState == 2)
/*  679 */           paramSunGraphics2D.textpipe = clipColorText;
/*      */         else {
/*  681 */           paramSunGraphics2D.textpipe = colorText;
/*      */         }
/*      */       }
/*  684 */       else if (paramSunGraphics2D.clipState == 2)
/*  685 */         paramSunGraphics2D.textpipe = clipPaintText;
/*      */       else {
/*  687 */         paramSunGraphics2D.textpipe = paintText;
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  692 */       if (canRenderParallelograms(paramSunGraphics2D)) {
/*  693 */         localObject = colorViaPgram;
/*      */ 
/*  698 */         paramSunGraphics2D.shapepipe = colorViaPgram;
/*      */       } else {
/*  700 */         localObject = colorViaShape;
/*  701 */         paramSunGraphics2D.shapepipe = colorPrimitives;
/*      */       }
/*  703 */       if (paramSunGraphics2D.transformState >= 3) {
/*  704 */         paramSunGraphics2D.drawpipe = ((PixelDrawPipe)localObject);
/*  705 */         paramSunGraphics2D.fillpipe = ((PixelFillPipe)localObject);
/*      */       } else {
/*  707 */         if (paramSunGraphics2D.strokeState != 0)
/*  708 */           paramSunGraphics2D.drawpipe = ((PixelDrawPipe)localObject);
/*      */         else {
/*  710 */           paramSunGraphics2D.drawpipe = colorPrimitives;
/*      */         }
/*  712 */         paramSunGraphics2D.fillpipe = colorPrimitives;
/*      */       }
/*      */ 
/*  715 */       paramSunGraphics2D.textpipe = getTextPipe(paramSunGraphics2D, false);
/*      */     }
/*      */ 
/*  720 */     if (((paramSunGraphics2D.textpipe instanceof LoopBasedPipe)) || ((paramSunGraphics2D.shapepipe instanceof LoopBasedPipe)) || ((paramSunGraphics2D.fillpipe instanceof LoopBasedPipe)) || ((paramSunGraphics2D.drawpipe instanceof LoopBasedPipe)) || ((paramSunGraphics2D.imagepipe instanceof LoopBasedPipe)))
/*      */     {
/*  726 */       paramSunGraphics2D.loops = getRenderLoops(paramSunGraphics2D);
/*      */     }
/*      */   }
/*      */ 
/*      */   private TextPipe getTextPipe(SunGraphics2D paramSunGraphics2D, boolean paramBoolean)
/*      */   {
/*  740 */     switch (paramSunGraphics2D.textAntialiasHint) {
/*      */     case 0:
/*  742 */       if (paramBoolean) {
/*  743 */         return aaTextRenderer;
/*      */       }
/*  745 */       return solidTextRenderer;
/*      */     case 1:
/*  748 */       return solidTextRenderer;
/*      */     case 2:
/*  751 */       return aaTextRenderer;
/*      */     }
/*      */ 
/*  754 */     switch (paramSunGraphics2D.getFontInfo().aaHint)
/*      */     {
/*      */     case 4:
/*      */     case 6:
/*  758 */       return lcdTextRenderer;
/*      */     case 2:
/*  761 */       return aaTextRenderer;
/*      */     case 1:
/*  764 */       return solidTextRenderer;
/*      */     case 3:
/*      */     case 5:
/*      */     }
/*      */ 
/*  773 */     if (paramBoolean) {
/*  774 */       return aaTextRenderer;
/*      */     }
/*  776 */     return solidTextRenderer;
/*      */   }
/*      */ 
/*      */   private static SurfaceType getPaintSurfaceType(SunGraphics2D paramSunGraphics2D)
/*      */   {
/*  783 */     switch (paramSunGraphics2D.paintState) {
/*      */     case 0:
/*  785 */       return SurfaceType.OpaqueColor;
/*      */     case 1:
/*  787 */       return SurfaceType.AnyColor;
/*      */     case 2:
/*  789 */       if (paramSunGraphics2D.paint.getTransparency() == 1) {
/*  790 */         return SurfaceType.OpaqueGradientPaint;
/*      */       }
/*  792 */       return SurfaceType.GradientPaint;
/*      */     case 3:
/*  795 */       if (paramSunGraphics2D.paint.getTransparency() == 1) {
/*  796 */         return SurfaceType.OpaqueLinearGradientPaint;
/*      */       }
/*  798 */       return SurfaceType.LinearGradientPaint;
/*      */     case 4:
/*  801 */       if (paramSunGraphics2D.paint.getTransparency() == 1) {
/*  802 */         return SurfaceType.OpaqueRadialGradientPaint;
/*      */       }
/*  804 */       return SurfaceType.RadialGradientPaint;
/*      */     case 5:
/*  807 */       if (paramSunGraphics2D.paint.getTransparency() == 1) {
/*  808 */         return SurfaceType.OpaqueTexturePaint;
/*      */       }
/*  810 */       return SurfaceType.TexturePaint;
/*      */     case 6:
/*      */     }
/*      */ 
/*  814 */     return SurfaceType.AnyPaint;
/*      */   }
/*      */ 
/*      */   private static CompositeType getFillCompositeType(SunGraphics2D paramSunGraphics2D)
/*      */   {
/*  819 */     CompositeType localCompositeType = paramSunGraphics2D.imageComp;
/*  820 */     if (paramSunGraphics2D.compositeState == 0) {
/*  821 */       if (localCompositeType == CompositeType.SrcOverNoEa)
/*  822 */         localCompositeType = CompositeType.OpaqueSrcOverNoEa;
/*      */       else {
/*  824 */         localCompositeType = CompositeType.SrcNoEa;
/*      */       }
/*      */     }
/*  827 */     return localCompositeType;
/*      */   }
/*      */ 
/*      */   protected MaskFill getMaskFill(SunGraphics2D paramSunGraphics2D)
/*      */   {
/*  839 */     SurfaceType localSurfaceType1 = getPaintSurfaceType(paramSunGraphics2D);
/*  840 */     CompositeType localCompositeType = getFillCompositeType(paramSunGraphics2D);
/*  841 */     SurfaceType localSurfaceType2 = getSurfaceType();
/*  842 */     return MaskFill.getFromCache(localSurfaceType1, localCompositeType, localSurfaceType2);
/*      */   }
/*      */ 
/*      */   public RenderLoops getRenderLoops(SunGraphics2D paramSunGraphics2D)
/*      */   {
/*  853 */     SurfaceType localSurfaceType1 = getPaintSurfaceType(paramSunGraphics2D);
/*  854 */     CompositeType localCompositeType = getFillCompositeType(paramSunGraphics2D);
/*  855 */     SurfaceType localSurfaceType2 = paramSunGraphics2D.getSurfaceData().getSurfaceType();
/*      */ 
/*  857 */     Object localObject = loopcache.get(localSurfaceType1, localCompositeType, localSurfaceType2);
/*  858 */     if (localObject != null) {
/*  859 */       return (RenderLoops)localObject;
/*      */     }
/*      */ 
/*  862 */     RenderLoops localRenderLoops = makeRenderLoops(localSurfaceType1, localCompositeType, localSurfaceType2);
/*  863 */     loopcache.put(localSurfaceType1, localCompositeType, localSurfaceType2, localRenderLoops);
/*  864 */     return localRenderLoops;
/*      */   }
/*      */ 
/*      */   public static RenderLoops makeRenderLoops(SurfaceType paramSurfaceType1, CompositeType paramCompositeType, SurfaceType paramSurfaceType2)
/*      */   {
/*  877 */     RenderLoops localRenderLoops = new RenderLoops();
/*  878 */     localRenderLoops.drawLineLoop = DrawLine.locate(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  879 */     localRenderLoops.fillRectLoop = FillRect.locate(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  880 */     localRenderLoops.drawRectLoop = DrawRect.locate(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  881 */     localRenderLoops.drawPolygonsLoop = DrawPolygons.locate(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  882 */     localRenderLoops.drawPathLoop = DrawPath.locate(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  883 */     localRenderLoops.fillPathLoop = FillPath.locate(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  884 */     localRenderLoops.fillSpansLoop = FillSpans.locate(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  885 */     localRenderLoops.fillParallelogramLoop = FillParallelogram.locate(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  886 */     localRenderLoops.drawParallelogramLoop = DrawParallelogram.locate(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  887 */     localRenderLoops.drawGlyphListLoop = DrawGlyphList.locate(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  888 */     localRenderLoops.drawGlyphListAALoop = DrawGlyphListAA.locate(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*  889 */     localRenderLoops.drawGlyphListLCDLoop = DrawGlyphListLCD.locate(paramSurfaceType1, paramCompositeType, paramSurfaceType2);
/*      */ 
/*  900 */     return localRenderLoops;
/*      */   }
/*      */ 
/*      */   public abstract GraphicsConfiguration getDeviceConfiguration();
/*      */ 
/*      */   public final SurfaceType getSurfaceType()
/*      */   {
/*  914 */     return this.surfaceType;
/*      */   }
/*      */ 
/*      */   public final ColorModel getColorModel()
/*      */   {
/*  921 */     return this.colorModel;
/*      */   }
/*      */ 
/*      */   public int getTransparency()
/*      */   {
/*  930 */     return getColorModel().getTransparency();
/*      */   }
/*      */ 
/*      */   public abstract Raster getRaster(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*      */ 
/*      */   public boolean useTightBBoxes()
/*      */   {
/*  960 */     return true;
/*      */   }
/*      */ 
/*      */   public int pixelFor(int paramInt)
/*      */   {
/*  968 */     return this.surfaceType.pixelFor(paramInt, this.colorModel);
/*      */   }
/*      */ 
/*      */   public int pixelFor(Color paramColor)
/*      */   {
/*  986 */     return pixelFor(paramColor.getRGB());
/*      */   }
/*      */ 
/*      */   public int rgbFor(int paramInt)
/*      */   {
/*  994 */     return this.surfaceType.rgbFor(paramInt, this.colorModel);
/*      */   }
/*      */ 
/*      */   public abstract Rectangle getBounds();
/*      */ 
/*      */   protected void checkCustomComposite()
/*      */   {
/* 1010 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1011 */     if (localSecurityManager != null) {
/* 1012 */       if (compPermission == null) {
/* 1013 */         compPermission = new AWTPermission("readDisplayPixels");
/*      */       }
/*      */ 
/* 1016 */       localSecurityManager.checkPermission(compPermission);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static native boolean isOpaqueGray(IndexColorModel paramIndexColorModel);
/*      */ 
/*      */   public static boolean isNull(SurfaceData paramSurfaceData)
/*      */   {
/* 1032 */     if ((paramSurfaceData == null) || (paramSurfaceData == NullSurfaceData.theInstance)) {
/* 1033 */       return true;
/*      */     }
/* 1035 */     return false;
/*      */   }
/*      */ 
/*      */   public boolean copyArea(SunGraphics2D paramSunGraphics2D, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/* 1046 */     return false;
/*      */   }
/*      */ 
/*      */   public void flush()
/*      */   {
/*      */   }
/*      */ 
/*      */   public abstract Object getDestination();
/*      */ 
/*      */   public int getDefaultScale()
/*      */   {
/* 1068 */     return 1;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  118 */     initIDs();
/*      */   }
/*      */ 
/*      */   static class PixelToPgramLoopConverter extends PixelToParallelogramConverter
/*      */     implements LoopBasedPipe
/*      */   {
/*      */     public PixelToPgramLoopConverter(ShapeDrawPipe paramShapeDrawPipe, ParallelogramPipe paramParallelogramPipe, double paramDouble1, double paramDouble2, boolean paramBoolean)
/*      */     {
/*  427 */       super(paramParallelogramPipe, paramDouble1, paramDouble2, paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   static class PixelToShapeLoopConverter extends PixelToShapeConverter
/*      */     implements LoopBasedPipe
/*      */   {
/*      */     public PixelToShapeLoopConverter(ShapeDrawPipe paramShapeDrawPipe)
/*      */     {
/*  412 */       super();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.SurfaceData
 * JD-Core Version:    0.6.2
 */