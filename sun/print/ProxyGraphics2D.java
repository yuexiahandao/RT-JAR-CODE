/*      */ package sun.print;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Composite;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Graphics2D;
/*      */ import java.awt.GraphicsConfiguration;
/*      */ import java.awt.Image;
/*      */ import java.awt.Paint;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.RenderingHints;
/*      */ import java.awt.RenderingHints.Key;
/*      */ import java.awt.Shape;
/*      */ import java.awt.Stroke;
/*      */ import java.awt.font.FontRenderContext;
/*      */ import java.awt.font.GlyphVector;
/*      */ import java.awt.geom.AffineTransform;
/*      */ import java.awt.geom.NoninvertibleTransformException;
/*      */ import java.awt.image.BufferedImage;
/*      */ import java.awt.image.BufferedImageOp;
/*      */ import java.awt.image.ImageObserver;
/*      */ import java.awt.image.RenderedImage;
/*      */ import java.awt.image.renderable.RenderContext;
/*      */ import java.awt.image.renderable.RenderableImage;
/*      */ import java.awt.print.PrinterGraphics;
/*      */ import java.awt.print.PrinterJob;
/*      */ import java.text.AttributedCharacterIterator;
/*      */ import java.util.Map;
/*      */ 
/*      */ public class ProxyGraphics2D extends Graphics2D
/*      */   implements PrinterGraphics
/*      */ {
/*      */   Graphics2D mGraphics;
/*      */   PrinterJob mPrinterJob;
/*      */ 
/*      */   public ProxyGraphics2D(Graphics2D paramGraphics2D, PrinterJob paramPrinterJob)
/*      */   {
/*   80 */     this.mGraphics = paramGraphics2D;
/*   81 */     this.mPrinterJob = paramPrinterJob;
/*      */   }
/*      */ 
/*      */   public Graphics2D getDelegate()
/*      */   {
/*   89 */     return this.mGraphics;
/*      */   }
/*      */ 
/*      */   public void setDelegate(Graphics2D paramGraphics2D)
/*      */   {
/*   97 */     this.mGraphics = paramGraphics2D;
/*      */   }
/*      */ 
/*      */   public PrinterJob getPrinterJob() {
/*  101 */     return this.mPrinterJob;
/*      */   }
/*      */ 
/*      */   public GraphicsConfiguration getDeviceConfiguration()
/*      */   {
/*  108 */     return ((RasterPrinterJob)this.mPrinterJob).getPrinterGraphicsConfig();
/*      */   }
/*      */ 
/*      */   public Graphics create()
/*      */   {
/*  121 */     return new ProxyGraphics2D((Graphics2D)this.mGraphics.create(), this.mPrinterJob);
/*      */   }
/*      */ 
/*      */   public void translate(int paramInt1, int paramInt2)
/*      */   {
/*  138 */     this.mGraphics.translate(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void translate(double paramDouble1, double paramDouble2)
/*      */   {
/*  153 */     this.mGraphics.translate(paramDouble1, paramDouble2);
/*      */   }
/*      */ 
/*      */   public void rotate(double paramDouble)
/*      */   {
/*  171 */     this.mGraphics.rotate(paramDouble);
/*      */   }
/*      */ 
/*      */   public void rotate(double paramDouble1, double paramDouble2, double paramDouble3)
/*      */   {
/*  190 */     this.mGraphics.rotate(paramDouble1, paramDouble2, paramDouble3);
/*      */   }
/*      */ 
/*      */   public void scale(double paramDouble1, double paramDouble2)
/*      */   {
/*  205 */     this.mGraphics.scale(paramDouble1, paramDouble2);
/*      */   }
/*      */ 
/*      */   public void shear(double paramDouble1, double paramDouble2)
/*      */   {
/*  224 */     this.mGraphics.shear(paramDouble1, paramDouble2);
/*      */   }
/*      */ 
/*      */   public Color getColor()
/*      */   {
/*  235 */     return this.mGraphics.getColor();
/*      */   }
/*      */ 
/*      */   public void setColor(Color paramColor)
/*      */   {
/*  248 */     this.mGraphics.setColor(paramColor);
/*      */   }
/*      */ 
/*      */   public void setPaintMode()
/*      */   {
/*  260 */     this.mGraphics.setPaintMode();
/*      */   }
/*      */ 
/*      */   public void setXORMode(Color paramColor)
/*      */   {
/*  280 */     this.mGraphics.setXORMode(paramColor);
/*      */   }
/*      */ 
/*      */   public Font getFont()
/*      */   {
/*  291 */     return this.mGraphics.getFont();
/*      */   }
/*      */ 
/*      */   public void setFont(Font paramFont)
/*      */   {
/*  306 */     this.mGraphics.setFont(paramFont);
/*      */   }
/*      */ 
/*      */   public FontMetrics getFontMetrics(Font paramFont)
/*      */   {
/*  319 */     return this.mGraphics.getFontMetrics(paramFont);
/*      */   }
/*      */ 
/*      */   public FontRenderContext getFontRenderContext()
/*      */   {
/*  327 */     return this.mGraphics.getFontRenderContext();
/*      */   }
/*      */ 
/*      */   public Rectangle getClipBounds()
/*      */   {
/*  342 */     return this.mGraphics.getClipBounds();
/*      */   }
/*      */ 
/*      */   public void clipRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  361 */     this.mGraphics.clipRect(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void setClip(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  378 */     this.mGraphics.setClip(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public Shape getClip()
/*      */   {
/*  392 */     return this.mGraphics.getClip();
/*      */   }
/*      */ 
/*      */   public void setClip(Shape paramShape)
/*      */   {
/*  410 */     this.mGraphics.setClip(paramShape);
/*      */   }
/*      */ 
/*      */   public void copyArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  436 */     this.mGraphics.copyArea(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */   }
/*      */ 
/*      */   public void drawLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  450 */     this.mGraphics.drawLine(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void fillRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  475 */     this.mGraphics.fillRect(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void clearRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  499 */     this.mGraphics.clearRect(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void drawRoundRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  521 */     this.mGraphics.drawRoundRect(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */   }
/*      */ 
/*      */   public void fillRoundRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  543 */     this.mGraphics.fillRoundRect(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */   }
/*      */ 
/*      */   public void drawOval(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  565 */     this.mGraphics.drawOval(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void fillOval(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  581 */     this.mGraphics.fillOval(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void drawArc(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  616 */     this.mGraphics.drawArc(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */   }
/*      */ 
/*      */   public void fillArc(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  650 */     this.mGraphics.fillArc(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */   }
/*      */ 
/*      */   public void drawPolyline(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*      */   {
/*  667 */     this.mGraphics.drawPolyline(paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*      */   }
/*      */ 
/*      */   public void drawPolygon(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*      */   {
/*  692 */     this.mGraphics.drawPolygon(paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*      */   }
/*      */ 
/*      */   public void fillPolygon(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*      */   {
/*  718 */     this.mGraphics.fillPolygon(paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*      */   }
/*      */ 
/*      */   public void drawString(String paramString, int paramInt1, int paramInt2)
/*      */   {
/*  734 */     this.mGraphics.drawString(paramString, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void drawString(AttributedCharacterIterator paramAttributedCharacterIterator, int paramInt1, int paramInt2)
/*      */   {
/*  759 */     this.mGraphics.drawString(paramAttributedCharacterIterator, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void drawString(AttributedCharacterIterator paramAttributedCharacterIterator, float paramFloat1, float paramFloat2)
/*      */   {
/*  784 */     this.mGraphics.drawString(paramAttributedCharacterIterator, paramFloat1, paramFloat2);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, ImageObserver paramImageObserver)
/*      */   {
/*  815 */     return this.mGraphics.drawImage(paramImage, paramInt1, paramInt2, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, ImageObserver paramImageObserver)
/*      */   {
/*  856 */     return this.mGraphics.drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/*  894 */     if (paramImage == null)
/*  895 */       return true;
/*      */     boolean bool;
/*  900 */     if (needToCopyBgColorImage(paramImage)) {
/*  901 */       BufferedImage localBufferedImage = getBufferedImageCopy(paramImage, paramColor);
/*  902 */       bool = this.mGraphics.drawImage(localBufferedImage, paramInt1, paramInt2, null);
/*      */     } else {
/*  904 */       bool = this.mGraphics.drawImage(paramImage, paramInt1, paramInt2, paramColor, paramImageObserver);
/*      */     }
/*      */ 
/*  907 */     return bool;
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/*  954 */     if (paramImage == null)
/*  955 */       return true;
/*      */     boolean bool;
/*  960 */     if (needToCopyBgColorImage(paramImage)) {
/*  961 */       BufferedImage localBufferedImage = getBufferedImageCopy(paramImage, paramColor);
/*  962 */       bool = this.mGraphics.drawImage(localBufferedImage, paramInt1, paramInt2, paramInt3, paramInt4, null);
/*      */     } else {
/*  964 */       bool = this.mGraphics.drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramColor, paramImageObserver);
/*      */     }
/*      */ 
/*  968 */     return bool;
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, ImageObserver paramImageObserver)
/*      */   {
/* 1022 */     return this.mGraphics.drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/* 1086 */     if (paramImage == null)
/* 1087 */       return true;
/*      */     boolean bool;
/* 1091 */     if (needToCopyBgColorImage(paramImage)) {
/* 1092 */       BufferedImage localBufferedImage = getBufferedImageCopy(paramImage, paramColor);
/* 1093 */       bool = this.mGraphics.drawImage(localBufferedImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt6, paramInt6, paramInt7, paramInt8, null);
/*      */     }
/*      */     else
/*      */     {
/* 1098 */       bool = this.mGraphics.drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt6, paramInt6, paramInt7, paramInt8, paramColor, paramImageObserver);
/*      */     }
/*      */ 
/* 1105 */     return bool;
/*      */   }
/*      */ 
/*      */   private boolean needToCopyBgColorImage(Image paramImage)
/*      */   {
/* 1121 */     AffineTransform localAffineTransform = getTransform();
/*      */ 
/* 1123 */     return (localAffineTransform.getType() & 0x30) != 0;
/*      */   }
/*      */ 
/*      */   private BufferedImage getBufferedImageCopy(Image paramImage, Color paramColor)
/*      */   {
/* 1141 */     BufferedImage localBufferedImage = null;
/*      */ 
/* 1143 */     int i = paramImage.getWidth(null);
/* 1144 */     int j = paramImage.getHeight(null);
/*      */ 
/* 1146 */     if ((i > 0) && (j > 0))
/*      */     {
/*      */       int k;
/* 1154 */       if ((paramImage instanceof BufferedImage)) {
/* 1155 */         localObject = (BufferedImage)paramImage;
/* 1156 */         k = ((BufferedImage)localObject).getType();
/*      */       } else {
/* 1158 */         k = 2;
/*      */       }
/*      */ 
/* 1161 */       localBufferedImage = new BufferedImage(i, j, k);
/*      */ 
/* 1168 */       Object localObject = localBufferedImage.createGraphics();
/* 1169 */       ((Graphics)localObject).drawImage(paramImage, 0, 0, paramColor, null);
/* 1170 */       ((Graphics)localObject).dispose();
/*      */     }
/*      */     else
/*      */     {
/* 1176 */       localBufferedImage = null;
/*      */     }
/*      */ 
/* 1179 */     return localBufferedImage;
/*      */   }
/*      */ 
/*      */   public void drawRenderedImage(RenderedImage paramRenderedImage, AffineTransform paramAffineTransform)
/*      */   {
/* 1202 */     this.mGraphics.drawRenderedImage(paramRenderedImage, paramAffineTransform);
/*      */   }
/*      */ 
/*      */   public void drawRenderableImage(RenderableImage paramRenderableImage, AffineTransform paramAffineTransform)
/*      */   {
/* 1210 */     if (paramRenderableImage == null) {
/* 1211 */       return; } 
/*      */ AffineTransform localAffineTransform1 = getTransform();
/* 1215 */     AffineTransform localAffineTransform2 = new AffineTransform(paramAffineTransform);
/* 1216 */     localAffineTransform2.concatenate(localAffineTransform1);
/*      */ 
/* 1219 */     RenderContext localRenderContext = new RenderContext(localAffineTransform2);
/*      */     AffineTransform localAffineTransform3;
/*      */     try { localAffineTransform3 = localAffineTransform1.createInverse();
/*      */     } catch (NoninvertibleTransformException localNoninvertibleTransformException) {
/* 1224 */       localRenderContext = new RenderContext(localAffineTransform1);
/* 1225 */       localAffineTransform3 = new AffineTransform();
/*      */     }
/*      */ 
/* 1228 */     RenderedImage localRenderedImage = paramRenderableImage.createRendering(localRenderContext);
/* 1229 */     drawRenderedImage(localRenderedImage, localAffineTransform3);
/*      */   }
/*      */ 
/*      */   public void dispose()
/*      */   {
/* 1261 */     this.mGraphics.dispose();
/*      */   }
/*      */ 
/*      */   public void finalize()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void draw(Shape paramShape)
/*      */   {
/* 1288 */     this.mGraphics.draw(paramShape);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, AffineTransform paramAffineTransform, ImageObserver paramImageObserver)
/*      */   {
/* 1315 */     return this.mGraphics.drawImage(paramImage, paramAffineTransform, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public void drawImage(BufferedImage paramBufferedImage, BufferedImageOp paramBufferedImageOp, int paramInt1, int paramInt2)
/*      */   {
/* 1340 */     this.mGraphics.drawImage(paramBufferedImage, paramBufferedImageOp, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void drawString(String paramString, float paramFloat1, float paramFloat2)
/*      */   {
/* 1362 */     this.mGraphics.drawString(paramString, paramFloat1, paramFloat2);
/*      */   }
/*      */ 
/*      */   public void drawGlyphVector(GlyphVector paramGlyphVector, float paramFloat1, float paramFloat2)
/*      */   {
/* 1383 */     this.mGraphics.drawGlyphVector(paramGlyphVector, paramFloat1, paramFloat2);
/*      */   }
/*      */ 
/*      */   public void fill(Shape paramShape)
/*      */   {
/* 1399 */     this.mGraphics.fill(paramShape);
/*      */   }
/*      */ 
/*      */   public boolean hit(Rectangle paramRectangle, Shape paramShape, boolean paramBoolean)
/*      */   {
/* 1424 */     return this.mGraphics.hit(paramRectangle, paramShape, paramBoolean);
/*      */   }
/*      */ 
/*      */   public void setComposite(Composite paramComposite)
/*      */   {
/* 1438 */     this.mGraphics.setComposite(paramComposite);
/*      */   }
/*      */ 
/*      */   public void setPaint(Paint paramPaint)
/*      */   {
/* 1451 */     this.mGraphics.setPaint(paramPaint);
/*      */   }
/*      */ 
/*      */   public void setStroke(Stroke paramStroke)
/*      */   {
/* 1461 */     this.mGraphics.setStroke(paramStroke);
/*      */   }
/*      */ 
/*      */   public void setRenderingHint(RenderingHints.Key paramKey, Object paramObject)
/*      */   {
/* 1474 */     this.mGraphics.setRenderingHint(paramKey, paramObject);
/*      */   }
/*      */ 
/*      */   public Object getRenderingHint(RenderingHints.Key paramKey)
/*      */   {
/* 1484 */     return this.mGraphics.getRenderingHint(paramKey);
/*      */   }
/*      */ 
/*      */   public void setRenderingHints(Map<?, ?> paramMap)
/*      */   {
/* 1495 */     this.mGraphics.setRenderingHints(paramMap);
/*      */   }
/*      */ 
/*      */   public void addRenderingHints(Map<?, ?> paramMap)
/*      */   {
/* 1506 */     this.mGraphics.addRenderingHints(paramMap);
/*      */   }
/*      */ 
/*      */   public RenderingHints getRenderingHints()
/*      */   {
/* 1516 */     return this.mGraphics.getRenderingHints();
/*      */   }
/*      */ 
/*      */   public void transform(AffineTransform paramAffineTransform)
/*      */   {
/* 1538 */     this.mGraphics.transform(paramAffineTransform);
/*      */   }
/*      */ 
/*      */   public void setTransform(AffineTransform paramAffineTransform)
/*      */   {
/* 1549 */     this.mGraphics.setTransform(paramAffineTransform);
/*      */   }
/*      */ 
/*      */   public AffineTransform getTransform()
/*      */   {
/* 1558 */     return this.mGraphics.getTransform();
/*      */   }
/*      */ 
/*      */   public Paint getPaint()
/*      */   {
/* 1567 */     return this.mGraphics.getPaint();
/*      */   }
/*      */ 
/*      */   public Composite getComposite()
/*      */   {
/* 1575 */     return this.mGraphics.getComposite();
/*      */   }
/*      */ 
/*      */   public void setBackground(Color paramColor)
/*      */   {
/* 1591 */     this.mGraphics.setBackground(paramColor);
/*      */   }
/*      */ 
/*      */   public Color getBackground()
/*      */   {
/* 1599 */     return this.mGraphics.getBackground();
/*      */   }
/*      */ 
/*      */   public Stroke getStroke()
/*      */   {
/* 1607 */     return this.mGraphics.getStroke();
/*      */   }
/*      */ 
/*      */   public void clip(Shape paramShape)
/*      */   {
/* 1620 */     this.mGraphics.clip(paramShape);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.ProxyGraphics2D
 * JD-Core Version:    0.6.2
 */