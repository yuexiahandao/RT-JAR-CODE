/*      */ package javax.swing;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Container;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Image;
/*      */ import java.awt.Point;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.Toolkit;
/*      */ import java.awt.image.FilteredImageSource;
/*      */ import java.awt.image.ImageObserver;
/*      */ import java.awt.image.ImageProducer;
/*      */ import java.io.PrintStream;
/*      */ import java.text.AttributedCharacterIterator;
/*      */ import java.util.Hashtable;
/*      */ 
/*      */ public class DebugGraphics extends Graphics
/*      */ {
/*      */   Graphics graphics;
/*      */   Image buffer;
/*      */   int debugOptions;
/*   53 */   int graphicsID = graphicsCount++;
/*      */   int xOffset;
/*      */   int yOffset;
/*   55 */   private static int graphicsCount = 0;
/*   56 */   private static ImageIcon imageLoadingIcon = new ImageIcon();
/*      */   public static final int LOG_OPTION = 1;
/*      */   public static final int FLASH_OPTION = 2;
/*      */   public static final int BUFFERED_OPTION = 4;
/*      */   public static final int NONE_OPTION = -1;
/* 1465 */   private static final Class debugGraphicsInfoKey = DebugGraphicsInfo.class;
/*      */ 
/*      */   public DebugGraphics()
/*      */   {
/*   77 */     this.buffer = null;
/*   78 */     this.xOffset = (this.yOffset = 0);
/*      */   }
/*      */ 
/*      */   public DebugGraphics(Graphics paramGraphics, JComponent paramJComponent)
/*      */   {
/*   89 */     this(paramGraphics);
/*   90 */     setDebugOptions(paramJComponent.shouldDebugGraphics());
/*      */   }
/*      */ 
/*      */   public DebugGraphics(Graphics paramGraphics)
/*      */   {
/*  100 */     this();
/*  101 */     this.graphics = paramGraphics;
/*      */   }
/*      */ 
/*      */   public Graphics create()
/*      */   {
/*  110 */     DebugGraphics localDebugGraphics = new DebugGraphics();
/*  111 */     localDebugGraphics.graphics = this.graphics.create();
/*  112 */     localDebugGraphics.debugOptions = this.debugOptions;
/*  113 */     localDebugGraphics.buffer = this.buffer;
/*      */ 
/*  115 */     return localDebugGraphics;
/*      */   }
/*      */ 
/*      */   public Graphics create(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  124 */     DebugGraphics localDebugGraphics = new DebugGraphics();
/*  125 */     localDebugGraphics.graphics = this.graphics.create(paramInt1, paramInt2, paramInt3, paramInt4);
/*  126 */     localDebugGraphics.debugOptions = this.debugOptions;
/*  127 */     localDebugGraphics.buffer = this.buffer;
/*  128 */     this.xOffset += paramInt1;
/*  129 */     this.yOffset += paramInt2;
/*      */ 
/*  131 */     return localDebugGraphics;
/*      */   }
/*      */ 
/*      */   public static void setFlashColor(Color paramColor)
/*      */   {
/*  143 */     info().flashColor = paramColor;
/*      */   }
/*      */ 
/*      */   public static Color flashColor()
/*      */   {
/*  151 */     return info().flashColor;
/*      */   }
/*      */ 
/*      */   public static void setFlashTime(int paramInt)
/*      */   {
/*  158 */     info().flashTime = paramInt;
/*      */   }
/*      */ 
/*      */   public static int flashTime()
/*      */   {
/*  166 */     return info().flashTime;
/*      */   }
/*      */ 
/*      */   public static void setFlashCount(int paramInt)
/*      */   {
/*  173 */     info().flashCount = paramInt;
/*      */   }
/*      */ 
/*      */   public static int flashCount()
/*      */   {
/*  180 */     return info().flashCount;
/*      */   }
/*      */ 
/*      */   public static void setLogStream(PrintStream paramPrintStream)
/*      */   {
/*  186 */     info().stream = paramPrintStream;
/*      */   }
/*      */ 
/*      */   public static PrintStream logStream()
/*      */   {
/*  193 */     return info().stream;
/*      */   }
/*      */ 
/*      */   public void setFont(Font paramFont)
/*      */   {
/*  199 */     if (debugLog()) {
/*  200 */       info().log(toShortString() + " Setting font: " + paramFont);
/*      */     }
/*  202 */     this.graphics.setFont(paramFont);
/*      */   }
/*      */ 
/*      */   public Font getFont()
/*      */   {
/*  209 */     return this.graphics.getFont();
/*      */   }
/*      */ 
/*      */   public void setColor(Color paramColor)
/*      */   {
/*  215 */     if (debugLog()) {
/*  216 */       info().log(toShortString() + " Setting color: " + paramColor);
/*      */     }
/*  218 */     this.graphics.setColor(paramColor);
/*      */   }
/*      */ 
/*      */   public Color getColor()
/*      */   {
/*  225 */     return this.graphics.getColor();
/*      */   }
/*      */ 
/*      */   public FontMetrics getFontMetrics()
/*      */   {
/*  237 */     return this.graphics.getFontMetrics();
/*      */   }
/*      */ 
/*      */   public FontMetrics getFontMetrics(Font paramFont)
/*      */   {
/*  244 */     return this.graphics.getFontMetrics(paramFont);
/*      */   }
/*      */ 
/*      */   public void translate(int paramInt1, int paramInt2)
/*      */   {
/*  251 */     if (debugLog()) {
/*  252 */       info().log(toShortString() + " Translating by: " + new Point(paramInt1, paramInt2));
/*      */     }
/*      */ 
/*  255 */     this.xOffset += paramInt1;
/*  256 */     this.yOffset += paramInt2;
/*  257 */     this.graphics.translate(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void setPaintMode()
/*      */   {
/*  264 */     if (debugLog()) {
/*  265 */       info().log(toShortString() + " Setting paint mode");
/*      */     }
/*  267 */     this.graphics.setPaintMode();
/*      */   }
/*      */ 
/*      */   public void setXORMode(Color paramColor)
/*      */   {
/*  274 */     if (debugLog()) {
/*  275 */       info().log(toShortString() + " Setting XOR mode: " + paramColor);
/*      */     }
/*  277 */     this.graphics.setXORMode(paramColor);
/*      */   }
/*      */ 
/*      */   public Rectangle getClipBounds()
/*      */   {
/*  284 */     return this.graphics.getClipBounds();
/*      */   }
/*      */ 
/*      */   public void clipRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  291 */     this.graphics.clipRect(paramInt1, paramInt2, paramInt3, paramInt4);
/*  292 */     if (debugLog())
/*  293 */       info().log(toShortString() + " Setting clipRect: " + new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4) + " New clipRect: " + this.graphics.getClip());
/*      */   }
/*      */ 
/*      */   public void setClip(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  303 */     this.graphics.setClip(paramInt1, paramInt2, paramInt3, paramInt4);
/*  304 */     if (debugLog())
/*  305 */       info().log(toShortString() + " Setting new clipRect: " + this.graphics.getClip());
/*      */   }
/*      */ 
/*      */   public Shape getClip()
/*      */   {
/*  314 */     return this.graphics.getClip();
/*      */   }
/*      */ 
/*      */   public void setClip(Shape paramShape)
/*      */   {
/*  321 */     this.graphics.setClip(paramShape);
/*  322 */     if (debugLog())
/*  323 */       info().log(toShortString() + " Setting new clipRect: " + this.graphics.getClip());
/*      */   }
/*      */ 
/*      */   public void drawRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  332 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/*  334 */     if (debugLog())
/*  335 */       info().log(toShortString() + " Drawing rect: " + new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4));
/*      */     Object localObject;
/*  340 */     if (isDrawingBuffer()) {
/*  341 */       if (debugBuffered()) {
/*  342 */         localObject = debugGraphics();
/*      */ 
/*  344 */         ((Graphics)localObject).drawRect(paramInt1, paramInt2, paramInt3, paramInt4);
/*  345 */         ((Graphics)localObject).dispose();
/*      */       }
/*  347 */     } else if (debugFlash()) {
/*  348 */       localObject = getColor();
/*  349 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/*      */ 
/*  351 */       for (int i = 0; i < j; i++) {
/*  352 */         this.graphics.setColor(i % 2 == 0 ? localDebugGraphicsInfo.flashColor : (Color)localObject);
/*  353 */         this.graphics.drawRect(paramInt1, paramInt2, paramInt3, paramInt4);
/*  354 */         Toolkit.getDefaultToolkit().sync();
/*  355 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*  357 */       this.graphics.setColor((Color)localObject);
/*      */     }
/*  359 */     this.graphics.drawRect(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void fillRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  366 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/*  368 */     if (debugLog())
/*  369 */       info().log(toShortString() + " Filling rect: " + new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4));
/*      */     Object localObject;
/*  374 */     if (isDrawingBuffer()) {
/*  375 */       if (debugBuffered()) {
/*  376 */         localObject = debugGraphics();
/*      */ 
/*  378 */         ((Graphics)localObject).fillRect(paramInt1, paramInt2, paramInt3, paramInt4);
/*  379 */         ((Graphics)localObject).dispose();
/*      */       }
/*  381 */     } else if (debugFlash()) {
/*  382 */       localObject = getColor();
/*  383 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/*      */ 
/*  385 */       for (int i = 0; i < j; i++) {
/*  386 */         this.graphics.setColor(i % 2 == 0 ? localDebugGraphicsInfo.flashColor : (Color)localObject);
/*  387 */         this.graphics.fillRect(paramInt1, paramInt2, paramInt3, paramInt4);
/*  388 */         Toolkit.getDefaultToolkit().sync();
/*  389 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*  391 */       this.graphics.setColor((Color)localObject);
/*      */     }
/*  393 */     this.graphics.fillRect(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void clearRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  400 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/*  402 */     if (debugLog())
/*  403 */       info().log(toShortString() + " Clearing rect: " + new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4));
/*      */     Object localObject;
/*  408 */     if (isDrawingBuffer()) {
/*  409 */       if (debugBuffered()) {
/*  410 */         localObject = debugGraphics();
/*      */ 
/*  412 */         ((Graphics)localObject).clearRect(paramInt1, paramInt2, paramInt3, paramInt4);
/*  413 */         ((Graphics)localObject).dispose();
/*      */       }
/*  415 */     } else if (debugFlash()) {
/*  416 */       localObject = getColor();
/*  417 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/*      */ 
/*  419 */       for (int i = 0; i < j; i++) {
/*  420 */         this.graphics.setColor(i % 2 == 0 ? localDebugGraphicsInfo.flashColor : (Color)localObject);
/*  421 */         this.graphics.clearRect(paramInt1, paramInt2, paramInt3, paramInt4);
/*  422 */         Toolkit.getDefaultToolkit().sync();
/*  423 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*  425 */       this.graphics.setColor((Color)localObject);
/*      */     }
/*  427 */     this.graphics.clearRect(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void drawRoundRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  435 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/*  437 */     if (debugLog())
/*  438 */       info().log(toShortString() + " Drawing round rect: " + new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4) + " arcWidth: " + paramInt5 + " archHeight: " + paramInt6);
/*      */     Object localObject;
/*  444 */     if (isDrawingBuffer()) {
/*  445 */       if (debugBuffered()) {
/*  446 */         localObject = debugGraphics();
/*      */ 
/*  448 */         ((Graphics)localObject).drawRoundRect(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */ 
/*  450 */         ((Graphics)localObject).dispose();
/*      */       }
/*  452 */     } else if (debugFlash()) {
/*  453 */       localObject = getColor();
/*  454 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/*      */ 
/*  456 */       for (int i = 0; i < j; i++) {
/*  457 */         this.graphics.setColor(i % 2 == 0 ? localDebugGraphicsInfo.flashColor : (Color)localObject);
/*  458 */         this.graphics.drawRoundRect(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */ 
/*  460 */         Toolkit.getDefaultToolkit().sync();
/*  461 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*  463 */       this.graphics.setColor((Color)localObject);
/*      */     }
/*  465 */     this.graphics.drawRoundRect(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */   }
/*      */ 
/*      */   public void fillRoundRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  473 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/*  475 */     if (debugLog())
/*  476 */       info().log(toShortString() + " Filling round rect: " + new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4) + " arcWidth: " + paramInt5 + " archHeight: " + paramInt6);
/*      */     Object localObject;
/*  482 */     if (isDrawingBuffer()) {
/*  483 */       if (debugBuffered()) {
/*  484 */         localObject = debugGraphics();
/*      */ 
/*  486 */         ((Graphics)localObject).fillRoundRect(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */ 
/*  488 */         ((Graphics)localObject).dispose();
/*      */       }
/*  490 */     } else if (debugFlash()) {
/*  491 */       localObject = getColor();
/*  492 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/*      */ 
/*  494 */       for (int i = 0; i < j; i++) {
/*  495 */         this.graphics.setColor(i % 2 == 0 ? localDebugGraphicsInfo.flashColor : (Color)localObject);
/*  496 */         this.graphics.fillRoundRect(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */ 
/*  498 */         Toolkit.getDefaultToolkit().sync();
/*  499 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*  501 */       this.graphics.setColor((Color)localObject);
/*      */     }
/*  503 */     this.graphics.fillRoundRect(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */   }
/*      */ 
/*      */   public void drawLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  510 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/*  512 */     if (debugLog())
/*  513 */       info().log(toShortString() + " Drawing line: from " + pointToString(paramInt1, paramInt2) + " to " + pointToString(paramInt3, paramInt4));
/*      */     Object localObject;
/*  518 */     if (isDrawingBuffer()) {
/*  519 */       if (debugBuffered()) {
/*  520 */         localObject = debugGraphics();
/*      */ 
/*  522 */         ((Graphics)localObject).drawLine(paramInt1, paramInt2, paramInt3, paramInt4);
/*  523 */         ((Graphics)localObject).dispose();
/*      */       }
/*  525 */     } else if (debugFlash()) {
/*  526 */       localObject = getColor();
/*  527 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/*      */ 
/*  529 */       for (int i = 0; i < j; i++) {
/*  530 */         this.graphics.setColor(i % 2 == 0 ? localDebugGraphicsInfo.flashColor : (Color)localObject);
/*  531 */         this.graphics.drawLine(paramInt1, paramInt2, paramInt3, paramInt4);
/*  532 */         Toolkit.getDefaultToolkit().sync();
/*  533 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*  535 */       this.graphics.setColor((Color)localObject);
/*      */     }
/*  537 */     this.graphics.drawLine(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void draw3DRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
/*      */   {
/*  545 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/*  547 */     if (debugLog())
/*  548 */       info().log(toShortString() + " Drawing 3D rect: " + new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4) + " Raised bezel: " + paramBoolean);
/*      */     Object localObject;
/*  553 */     if (isDrawingBuffer()) {
/*  554 */       if (debugBuffered()) {
/*  555 */         localObject = debugGraphics();
/*      */ 
/*  557 */         ((Graphics)localObject).draw3DRect(paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean);
/*  558 */         ((Graphics)localObject).dispose();
/*      */       }
/*  560 */     } else if (debugFlash()) {
/*  561 */       localObject = getColor();
/*  562 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/*      */ 
/*  564 */       for (int i = 0; i < j; i++) {
/*  565 */         this.graphics.setColor(i % 2 == 0 ? localDebugGraphicsInfo.flashColor : (Color)localObject);
/*  566 */         this.graphics.draw3DRect(paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean);
/*  567 */         Toolkit.getDefaultToolkit().sync();
/*  568 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*  570 */       this.graphics.setColor((Color)localObject);
/*      */     }
/*  572 */     this.graphics.draw3DRect(paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean);
/*      */   }
/*      */ 
/*      */   public void fill3DRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
/*      */   {
/*  580 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/*  582 */     if (debugLog())
/*  583 */       info().log(toShortString() + " Filling 3D rect: " + new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4) + " Raised bezel: " + paramBoolean);
/*      */     Object localObject;
/*  588 */     if (isDrawingBuffer()) {
/*  589 */       if (debugBuffered()) {
/*  590 */         localObject = debugGraphics();
/*      */ 
/*  592 */         ((Graphics)localObject).fill3DRect(paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean);
/*  593 */         ((Graphics)localObject).dispose();
/*      */       }
/*  595 */     } else if (debugFlash()) {
/*  596 */       localObject = getColor();
/*  597 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/*      */ 
/*  599 */       for (int i = 0; i < j; i++) {
/*  600 */         this.graphics.setColor(i % 2 == 0 ? localDebugGraphicsInfo.flashColor : (Color)localObject);
/*  601 */         this.graphics.fill3DRect(paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean);
/*  602 */         Toolkit.getDefaultToolkit().sync();
/*  603 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*  605 */       this.graphics.setColor((Color)localObject);
/*      */     }
/*  607 */     this.graphics.fill3DRect(paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean);
/*      */   }
/*      */ 
/*      */   public void drawOval(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  614 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/*  616 */     if (debugLog())
/*  617 */       info().log(toShortString() + " Drawing oval: " + new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4));
/*      */     Object localObject;
/*  621 */     if (isDrawingBuffer()) {
/*  622 */       if (debugBuffered()) {
/*  623 */         localObject = debugGraphics();
/*      */ 
/*  625 */         ((Graphics)localObject).drawOval(paramInt1, paramInt2, paramInt3, paramInt4);
/*  626 */         ((Graphics)localObject).dispose();
/*      */       }
/*  628 */     } else if (debugFlash()) {
/*  629 */       localObject = getColor();
/*  630 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/*      */ 
/*  632 */       for (int i = 0; i < j; i++) {
/*  633 */         this.graphics.setColor(i % 2 == 0 ? localDebugGraphicsInfo.flashColor : (Color)localObject);
/*  634 */         this.graphics.drawOval(paramInt1, paramInt2, paramInt3, paramInt4);
/*  635 */         Toolkit.getDefaultToolkit().sync();
/*  636 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*  638 */       this.graphics.setColor((Color)localObject);
/*      */     }
/*  640 */     this.graphics.drawOval(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void fillOval(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  647 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/*  649 */     if (debugLog())
/*  650 */       info().log(toShortString() + " Filling oval: " + new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4));
/*      */     Object localObject;
/*  654 */     if (isDrawingBuffer()) {
/*  655 */       if (debugBuffered()) {
/*  656 */         localObject = debugGraphics();
/*      */ 
/*  658 */         ((Graphics)localObject).fillOval(paramInt1, paramInt2, paramInt3, paramInt4);
/*  659 */         ((Graphics)localObject).dispose();
/*      */       }
/*  661 */     } else if (debugFlash()) {
/*  662 */       localObject = getColor();
/*  663 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/*      */ 
/*  665 */       for (int i = 0; i < j; i++) {
/*  666 */         this.graphics.setColor(i % 2 == 0 ? localDebugGraphicsInfo.flashColor : (Color)localObject);
/*  667 */         this.graphics.fillOval(paramInt1, paramInt2, paramInt3, paramInt4);
/*  668 */         Toolkit.getDefaultToolkit().sync();
/*  669 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*  671 */       this.graphics.setColor((Color)localObject);
/*      */     }
/*  673 */     this.graphics.fillOval(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void drawArc(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  681 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/*  683 */     if (debugLog())
/*  684 */       info().log(toShortString() + " Drawing arc: " + new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4) + " startAngle: " + paramInt5 + " arcAngle: " + paramInt6);
/*      */     Object localObject;
/*  690 */     if (isDrawingBuffer()) {
/*  691 */       if (debugBuffered()) {
/*  692 */         localObject = debugGraphics();
/*      */ 
/*  694 */         ((Graphics)localObject).drawArc(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */ 
/*  696 */         ((Graphics)localObject).dispose();
/*      */       }
/*  698 */     } else if (debugFlash()) {
/*  699 */       localObject = getColor();
/*  700 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/*      */ 
/*  702 */       for (int i = 0; i < j; i++) {
/*  703 */         this.graphics.setColor(i % 2 == 0 ? localDebugGraphicsInfo.flashColor : (Color)localObject);
/*  704 */         this.graphics.drawArc(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*  705 */         Toolkit.getDefaultToolkit().sync();
/*  706 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*  708 */       this.graphics.setColor((Color)localObject);
/*      */     }
/*  710 */     this.graphics.drawArc(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */   }
/*      */ 
/*      */   public void fillArc(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  718 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/*  720 */     if (debugLog())
/*  721 */       info().log(toShortString() + " Filling arc: " + new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4) + " startAngle: " + paramInt5 + " arcAngle: " + paramInt6);
/*      */     Object localObject;
/*  727 */     if (isDrawingBuffer()) {
/*  728 */       if (debugBuffered()) {
/*  729 */         localObject = debugGraphics();
/*      */ 
/*  731 */         ((Graphics)localObject).fillArc(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */ 
/*  733 */         ((Graphics)localObject).dispose();
/*      */       }
/*  735 */     } else if (debugFlash()) {
/*  736 */       localObject = getColor();
/*  737 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/*      */ 
/*  739 */       for (int i = 0; i < j; i++) {
/*  740 */         this.graphics.setColor(i % 2 == 0 ? localDebugGraphicsInfo.flashColor : (Color)localObject);
/*  741 */         this.graphics.fillArc(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*  742 */         Toolkit.getDefaultToolkit().sync();
/*  743 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*  745 */       this.graphics.setColor((Color)localObject);
/*      */     }
/*  747 */     this.graphics.fillArc(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */   }
/*      */ 
/*      */   public void drawPolyline(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*      */   {
/*  754 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/*  756 */     if (debugLog())
/*  757 */       info().log(toShortString() + " Drawing polyline: " + " nPoints: " + paramInt + " X's: " + paramArrayOfInt1 + " Y's: " + paramArrayOfInt2);
/*      */     Object localObject;
/*  763 */     if (isDrawingBuffer()) {
/*  764 */       if (debugBuffered()) {
/*  765 */         localObject = debugGraphics();
/*      */ 
/*  767 */         ((Graphics)localObject).drawPolyline(paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*  768 */         ((Graphics)localObject).dispose();
/*      */       }
/*  770 */     } else if (debugFlash()) {
/*  771 */       localObject = getColor();
/*  772 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/*      */ 
/*  774 */       for (int i = 0; i < j; i++) {
/*  775 */         this.graphics.setColor(i % 2 == 0 ? localDebugGraphicsInfo.flashColor : (Color)localObject);
/*  776 */         this.graphics.drawPolyline(paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*  777 */         Toolkit.getDefaultToolkit().sync();
/*  778 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*  780 */       this.graphics.setColor((Color)localObject);
/*      */     }
/*  782 */     this.graphics.drawPolyline(paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*      */   }
/*      */ 
/*      */   public void drawPolygon(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*      */   {
/*  789 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/*  791 */     if (debugLog())
/*  792 */       info().log(toShortString() + " Drawing polygon: " + " nPoints: " + paramInt + " X's: " + paramArrayOfInt1 + " Y's: " + paramArrayOfInt2);
/*      */     Object localObject;
/*  798 */     if (isDrawingBuffer()) {
/*  799 */       if (debugBuffered()) {
/*  800 */         localObject = debugGraphics();
/*      */ 
/*  802 */         ((Graphics)localObject).drawPolygon(paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*  803 */         ((Graphics)localObject).dispose();
/*      */       }
/*  805 */     } else if (debugFlash()) {
/*  806 */       localObject = getColor();
/*  807 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/*      */ 
/*  809 */       for (int i = 0; i < j; i++) {
/*  810 */         this.graphics.setColor(i % 2 == 0 ? localDebugGraphicsInfo.flashColor : (Color)localObject);
/*  811 */         this.graphics.drawPolygon(paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*  812 */         Toolkit.getDefaultToolkit().sync();
/*  813 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*  815 */       this.graphics.setColor((Color)localObject);
/*      */     }
/*  817 */     this.graphics.drawPolygon(paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*      */   }
/*      */ 
/*      */   public void fillPolygon(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*      */   {
/*  824 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/*  826 */     if (debugLog())
/*  827 */       info().log(toShortString() + " Filling polygon: " + " nPoints: " + paramInt + " X's: " + paramArrayOfInt1 + " Y's: " + paramArrayOfInt2);
/*      */     Object localObject;
/*  833 */     if (isDrawingBuffer()) {
/*  834 */       if (debugBuffered()) {
/*  835 */         localObject = debugGraphics();
/*      */ 
/*  837 */         ((Graphics)localObject).fillPolygon(paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*  838 */         ((Graphics)localObject).dispose();
/*      */       }
/*  840 */     } else if (debugFlash()) {
/*  841 */       localObject = getColor();
/*  842 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/*      */ 
/*  844 */       for (int i = 0; i < j; i++) {
/*  845 */         this.graphics.setColor(i % 2 == 0 ? localDebugGraphicsInfo.flashColor : (Color)localObject);
/*  846 */         this.graphics.fillPolygon(paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*  847 */         Toolkit.getDefaultToolkit().sync();
/*  848 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*  850 */       this.graphics.setColor((Color)localObject);
/*      */     }
/*  852 */     this.graphics.fillPolygon(paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*      */   }
/*      */ 
/*      */   public void drawString(String paramString, int paramInt1, int paramInt2)
/*      */   {
/*  859 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/*  861 */     if (debugLog())
/*  862 */       info().log(toShortString() + " Drawing string: \"" + paramString + "\" at: " + new Point(paramInt1, paramInt2));
/*      */     Object localObject;
/*  867 */     if (isDrawingBuffer()) {
/*  868 */       if (debugBuffered()) {
/*  869 */         localObject = debugGraphics();
/*      */ 
/*  871 */         ((Graphics)localObject).drawString(paramString, paramInt1, paramInt2);
/*  872 */         ((Graphics)localObject).dispose();
/*      */       }
/*  874 */     } else if (debugFlash()) {
/*  875 */       localObject = getColor();
/*  876 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/*      */ 
/*  878 */       for (int i = 0; i < j; i++) {
/*  879 */         this.graphics.setColor(i % 2 == 0 ? localDebugGraphicsInfo.flashColor : (Color)localObject);
/*      */ 
/*  881 */         this.graphics.drawString(paramString, paramInt1, paramInt2);
/*  882 */         Toolkit.getDefaultToolkit().sync();
/*  883 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*  885 */       this.graphics.setColor((Color)localObject);
/*      */     }
/*  887 */     this.graphics.drawString(paramString, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void drawString(AttributedCharacterIterator paramAttributedCharacterIterator, int paramInt1, int paramInt2)
/*      */   {
/*  894 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/*  896 */     if (debugLog())
/*  897 */       info().log(toShortString() + " Drawing text: \"" + paramAttributedCharacterIterator + "\" at: " + new Point(paramInt1, paramInt2));
/*      */     Object localObject;
/*  902 */     if (isDrawingBuffer()) {
/*  903 */       if (debugBuffered()) {
/*  904 */         localObject = debugGraphics();
/*      */ 
/*  906 */         ((Graphics)localObject).drawString(paramAttributedCharacterIterator, paramInt1, paramInt2);
/*  907 */         ((Graphics)localObject).dispose();
/*      */       }
/*  909 */     } else if (debugFlash()) {
/*  910 */       localObject = getColor();
/*  911 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/*      */ 
/*  913 */       for (int i = 0; i < j; i++) {
/*  914 */         this.graphics.setColor(i % 2 == 0 ? localDebugGraphicsInfo.flashColor : (Color)localObject);
/*      */ 
/*  916 */         this.graphics.drawString(paramAttributedCharacterIterator, paramInt1, paramInt2);
/*  917 */         Toolkit.getDefaultToolkit().sync();
/*  918 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*  920 */       this.graphics.setColor((Color)localObject);
/*      */     }
/*  922 */     this.graphics.drawString(paramAttributedCharacterIterator, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void drawBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  929 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/*  931 */     Font localFont = this.graphics.getFont();
/*      */ 
/*  933 */     if (debugLog())
/*  934 */       info().log(toShortString() + " Drawing bytes at: " + new Point(paramInt3, paramInt4));
/*      */     Object localObject;
/*  938 */     if (isDrawingBuffer()) {
/*  939 */       if (debugBuffered()) {
/*  940 */         localObject = debugGraphics();
/*      */ 
/*  942 */         ((Graphics)localObject).drawBytes(paramArrayOfByte, paramInt1, paramInt2, paramInt3, paramInt4);
/*  943 */         ((Graphics)localObject).dispose();
/*      */       }
/*  945 */     } else if (debugFlash()) {
/*  946 */       localObject = getColor();
/*  947 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/*      */ 
/*  949 */       for (int i = 0; i < j; i++) {
/*  950 */         this.graphics.setColor(i % 2 == 0 ? localDebugGraphicsInfo.flashColor : (Color)localObject);
/*      */ 
/*  952 */         this.graphics.drawBytes(paramArrayOfByte, paramInt1, paramInt2, paramInt3, paramInt4);
/*  953 */         Toolkit.getDefaultToolkit().sync();
/*  954 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*  956 */       this.graphics.setColor((Color)localObject);
/*      */     }
/*  958 */     this.graphics.drawBytes(paramArrayOfByte, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void drawChars(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  965 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/*  967 */     Font localFont = this.graphics.getFont();
/*      */ 
/*  969 */     if (debugLog())
/*  970 */       info().log(toShortString() + " Drawing chars at " + new Point(paramInt3, paramInt4));
/*      */     Object localObject;
/*  974 */     if (isDrawingBuffer()) {
/*  975 */       if (debugBuffered()) {
/*  976 */         localObject = debugGraphics();
/*      */ 
/*  978 */         ((Graphics)localObject).drawChars(paramArrayOfChar, paramInt1, paramInt2, paramInt3, paramInt4);
/*  979 */         ((Graphics)localObject).dispose();
/*      */       }
/*  981 */     } else if (debugFlash()) {
/*  982 */       localObject = getColor();
/*  983 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/*      */ 
/*  985 */       for (int i = 0; i < j; i++) {
/*  986 */         this.graphics.setColor(i % 2 == 0 ? localDebugGraphicsInfo.flashColor : (Color)localObject);
/*      */ 
/*  988 */         this.graphics.drawChars(paramArrayOfChar, paramInt1, paramInt2, paramInt3, paramInt4);
/*  989 */         Toolkit.getDefaultToolkit().sync();
/*  990 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*  992 */       this.graphics.setColor((Color)localObject);
/*      */     }
/*  994 */     this.graphics.drawChars(paramArrayOfChar, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, ImageObserver paramImageObserver)
/*      */   {
/* 1002 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/* 1004 */     if (debugLog()) {
/* 1005 */       localDebugGraphicsInfo.log(toShortString() + " Drawing image: " + paramImage + " at: " + new Point(paramInt1, paramInt2));
/*      */     }
/*      */ 
/* 1010 */     if (isDrawingBuffer()) {
/* 1011 */       if (debugBuffered()) {
/* 1012 */         Graphics localGraphics = debugGraphics();
/*      */ 
/* 1014 */         localGraphics.drawImage(paramImage, paramInt1, paramInt2, paramImageObserver);
/* 1015 */         localGraphics.dispose();
/*      */       }
/* 1017 */     } else if (debugFlash()) {
/* 1018 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/* 1019 */       ImageProducer localImageProducer = paramImage.getSource();
/* 1020 */       FilteredImageSource localFilteredImageSource = new FilteredImageSource(localImageProducer, new DebugGraphicsFilter(localDebugGraphicsInfo.flashColor));
/*      */ 
/* 1023 */       Image localImage1 = Toolkit.getDefaultToolkit().createImage(localFilteredImageSource);
/*      */ 
/* 1025 */       DebugGraphicsObserver localDebugGraphicsObserver = new DebugGraphicsObserver();
/*      */ 
/* 1029 */       for (int i = 0; i < j; i++) {
/* 1030 */         Image localImage2 = i % 2 == 0 ? localImage1 : paramImage;
/* 1031 */         loadImage(localImage2);
/* 1032 */         this.graphics.drawImage(localImage2, paramInt1, paramInt2, localDebugGraphicsObserver);
/*      */ 
/* 1034 */         Toolkit.getDefaultToolkit().sync();
/* 1035 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*      */     }
/* 1038 */     return this.graphics.drawImage(paramImage, paramInt1, paramInt2, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, ImageObserver paramImageObserver)
/*      */   {
/* 1046 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/* 1048 */     if (debugLog()) {
/* 1049 */       localDebugGraphicsInfo.log(toShortString() + " Drawing image: " + paramImage + " at: " + new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4));
/*      */     }
/*      */ 
/* 1054 */     if (isDrawingBuffer()) {
/* 1055 */       if (debugBuffered()) {
/* 1056 */         Graphics localGraphics = debugGraphics();
/*      */ 
/* 1058 */         localGraphics.drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramImageObserver);
/* 1059 */         localGraphics.dispose();
/*      */       }
/* 1061 */     } else if (debugFlash()) {
/* 1062 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/* 1063 */       ImageProducer localImageProducer = paramImage.getSource();
/* 1064 */       FilteredImageSource localFilteredImageSource = new FilteredImageSource(localImageProducer, new DebugGraphicsFilter(localDebugGraphicsInfo.flashColor));
/*      */ 
/* 1067 */       Image localImage1 = Toolkit.getDefaultToolkit().createImage(localFilteredImageSource);
/*      */ 
/* 1069 */       DebugGraphicsObserver localDebugGraphicsObserver = new DebugGraphicsObserver();
/*      */ 
/* 1073 */       for (int i = 0; i < j; i++) {
/* 1074 */         Image localImage2 = i % 2 == 0 ? localImage1 : paramImage;
/* 1075 */         loadImage(localImage2);
/* 1076 */         this.graphics.drawImage(localImage2, paramInt1, paramInt2, paramInt3, paramInt4, localDebugGraphicsObserver);
/*      */ 
/* 1078 */         Toolkit.getDefaultToolkit().sync();
/* 1079 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*      */     }
/* 1082 */     return this.graphics.drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/* 1091 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/* 1093 */     if (debugLog()) {
/* 1094 */       localDebugGraphicsInfo.log(toShortString() + " Drawing image: " + paramImage + " at: " + new Point(paramInt1, paramInt2) + ", bgcolor: " + paramColor);
/*      */     }
/*      */ 
/* 1100 */     if (isDrawingBuffer()) {
/* 1101 */       if (debugBuffered()) {
/* 1102 */         Graphics localGraphics = debugGraphics();
/*      */ 
/* 1104 */         localGraphics.drawImage(paramImage, paramInt1, paramInt2, paramColor, paramImageObserver);
/* 1105 */         localGraphics.dispose();
/*      */       }
/* 1107 */     } else if (debugFlash()) {
/* 1108 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/* 1109 */       ImageProducer localImageProducer = paramImage.getSource();
/* 1110 */       FilteredImageSource localFilteredImageSource = new FilteredImageSource(localImageProducer, new DebugGraphicsFilter(localDebugGraphicsInfo.flashColor));
/*      */ 
/* 1113 */       Image localImage1 = Toolkit.getDefaultToolkit().createImage(localFilteredImageSource);
/*      */ 
/* 1115 */       DebugGraphicsObserver localDebugGraphicsObserver = new DebugGraphicsObserver();
/*      */ 
/* 1119 */       for (int i = 0; i < j; i++) {
/* 1120 */         Image localImage2 = i % 2 == 0 ? localImage1 : paramImage;
/* 1121 */         loadImage(localImage2);
/* 1122 */         this.graphics.drawImage(localImage2, paramInt1, paramInt2, paramColor, localDebugGraphicsObserver);
/*      */ 
/* 1124 */         Toolkit.getDefaultToolkit().sync();
/* 1125 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*      */     }
/* 1128 */     return this.graphics.drawImage(paramImage, paramInt1, paramInt2, paramColor, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/* 1137 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/* 1139 */     if (debugLog()) {
/* 1140 */       localDebugGraphicsInfo.log(toShortString() + " Drawing image: " + paramImage + " at: " + new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4) + ", bgcolor: " + paramColor);
/*      */     }
/*      */ 
/* 1146 */     if (isDrawingBuffer()) {
/* 1147 */       if (debugBuffered()) {
/* 1148 */         Graphics localGraphics = debugGraphics();
/*      */ 
/* 1150 */         localGraphics.drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramColor, paramImageObserver);
/*      */ 
/* 1152 */         localGraphics.dispose();
/*      */       }
/* 1154 */     } else if (debugFlash()) {
/* 1155 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/* 1156 */       ImageProducer localImageProducer = paramImage.getSource();
/* 1157 */       FilteredImageSource localFilteredImageSource = new FilteredImageSource(localImageProducer, new DebugGraphicsFilter(localDebugGraphicsInfo.flashColor));
/*      */ 
/* 1160 */       Image localImage1 = Toolkit.getDefaultToolkit().createImage(localFilteredImageSource);
/*      */ 
/* 1162 */       DebugGraphicsObserver localDebugGraphicsObserver = new DebugGraphicsObserver();
/*      */ 
/* 1166 */       for (int i = 0; i < j; i++) {
/* 1167 */         Image localImage2 = i % 2 == 0 ? localImage1 : paramImage;
/* 1168 */         loadImage(localImage2);
/* 1169 */         this.graphics.drawImage(localImage2, paramInt1, paramInt2, paramInt3, paramInt4, paramColor, localDebugGraphicsObserver);
/*      */ 
/* 1171 */         Toolkit.getDefaultToolkit().sync();
/* 1172 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*      */     }
/* 1175 */     return this.graphics.drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramColor, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, ImageObserver paramImageObserver)
/*      */   {
/* 1185 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/* 1187 */     if (debugLog()) {
/* 1188 */       localDebugGraphicsInfo.log(toShortString() + " Drawing image: " + paramImage + " destination: " + new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4) + " source: " + new Rectangle(paramInt5, paramInt6, paramInt7, paramInt8));
/*      */     }
/*      */ 
/* 1194 */     if (isDrawingBuffer()) {
/* 1195 */       if (debugBuffered()) {
/* 1196 */         Graphics localGraphics = debugGraphics();
/*      */ 
/* 1198 */         localGraphics.drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramImageObserver);
/*      */ 
/* 1200 */         localGraphics.dispose();
/*      */       }
/* 1202 */     } else if (debugFlash()) {
/* 1203 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/* 1204 */       ImageProducer localImageProducer = paramImage.getSource();
/* 1205 */       FilteredImageSource localFilteredImageSource = new FilteredImageSource(localImageProducer, new DebugGraphicsFilter(localDebugGraphicsInfo.flashColor));
/*      */ 
/* 1208 */       Image localImage1 = Toolkit.getDefaultToolkit().createImage(localFilteredImageSource);
/*      */ 
/* 1210 */       DebugGraphicsObserver localDebugGraphicsObserver = new DebugGraphicsObserver();
/*      */ 
/* 1214 */       for (int i = 0; i < j; i++) {
/* 1215 */         Image localImage2 = i % 2 == 0 ? localImage1 : paramImage;
/* 1216 */         loadImage(localImage2);
/* 1217 */         this.graphics.drawImage(localImage2, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, localDebugGraphicsObserver);
/*      */ 
/* 1220 */         Toolkit.getDefaultToolkit().sync();
/* 1221 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*      */     }
/* 1224 */     return this.graphics.drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/* 1236 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/* 1238 */     if (debugLog()) {
/* 1239 */       localDebugGraphicsInfo.log(toShortString() + " Drawing image: " + paramImage + " destination: " + new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4) + " source: " + new Rectangle(paramInt5, paramInt6, paramInt7, paramInt8) + ", bgcolor: " + paramColor);
/*      */     }
/*      */ 
/* 1246 */     if (isDrawingBuffer()) {
/* 1247 */       if (debugBuffered()) {
/* 1248 */         Graphics localGraphics = debugGraphics();
/*      */ 
/* 1250 */         localGraphics.drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramColor, paramImageObserver);
/*      */ 
/* 1252 */         localGraphics.dispose();
/*      */       }
/* 1254 */     } else if (debugFlash()) {
/* 1255 */       int j = localDebugGraphicsInfo.flashCount * 2 - 1;
/* 1256 */       ImageProducer localImageProducer = paramImage.getSource();
/* 1257 */       FilteredImageSource localFilteredImageSource = new FilteredImageSource(localImageProducer, new DebugGraphicsFilter(localDebugGraphicsInfo.flashColor));
/*      */ 
/* 1260 */       Image localImage1 = Toolkit.getDefaultToolkit().createImage(localFilteredImageSource);
/*      */ 
/* 1262 */       DebugGraphicsObserver localDebugGraphicsObserver = new DebugGraphicsObserver();
/*      */ 
/* 1266 */       for (int i = 0; i < j; i++) {
/* 1267 */         Image localImage2 = i % 2 == 0 ? localImage1 : paramImage;
/* 1268 */         loadImage(localImage2);
/* 1269 */         this.graphics.drawImage(localImage2, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramColor, localDebugGraphicsObserver);
/*      */ 
/* 1272 */         Toolkit.getDefaultToolkit().sync();
/* 1273 */         sleep(localDebugGraphicsInfo.flashTime);
/*      */       }
/*      */     }
/* 1276 */     return this.graphics.drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramColor, paramImageObserver);
/*      */   }
/*      */ 
/*      */   static void loadImage(Image paramImage)
/*      */   {
/* 1281 */     imageLoadingIcon.loadImage(paramImage);
/*      */   }
/*      */ 
/*      */   public void copyArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/* 1290 */     if (debugLog()) {
/* 1291 */       info().log(toShortString() + " Copying area from: " + new Rectangle(paramInt1, paramInt2, paramInt3, paramInt4) + " to: " + new Point(paramInt5, paramInt6));
/*      */     }
/*      */ 
/* 1296 */     this.graphics.copyArea(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */   }
/*      */ 
/*      */   final void sleep(int paramInt) {
/*      */     try {
/* 1301 */       Thread.sleep(paramInt);
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public void dispose()
/*      */   {
/* 1310 */     this.graphics.dispose();
/* 1311 */     this.graphics = null;
/*      */   }
/*      */ 
/*      */   public boolean isDrawingBuffer()
/*      */   {
/* 1321 */     return this.buffer != null;
/*      */   }
/*      */ 
/*      */   String toShortString() {
/* 1325 */     return "Graphics" + (isDrawingBuffer() ? "<B>" : "") + "(" + this.graphicsID + "-" + this.debugOptions + ")";
/*      */   }
/*      */ 
/*      */   String pointToString(int paramInt1, int paramInt2) {
/* 1329 */     return "(" + paramInt1 + ", " + paramInt2 + ")";
/*      */   }
/*      */ 
/*      */   public void setDebugOptions(int paramInt)
/*      */   {
/* 1341 */     if (paramInt != 0)
/* 1342 */       if (paramInt == -1) {
/* 1343 */         if (this.debugOptions != 0) {
/* 1344 */           System.err.println(toShortString() + " Disabling debug");
/* 1345 */           this.debugOptions = 0;
/*      */         }
/*      */       }
/* 1348 */       else if (this.debugOptions != paramInt) {
/* 1349 */         this.debugOptions |= paramInt;
/* 1350 */         if (debugLog())
/* 1351 */           System.err.println(toShortString() + " Enabling debug");
/*      */       }
/*      */   }
/*      */ 
/*      */   public int getDebugOptions()
/*      */   {
/* 1362 */     return this.debugOptions;
/*      */   }
/*      */ 
/*      */   static void setDebugOptions(JComponent paramJComponent, int paramInt)
/*      */   {
/* 1369 */     info().setDebugOptions(paramJComponent, paramInt);
/*      */   }
/*      */ 
/*      */   static int getDebugOptions(JComponent paramJComponent)
/*      */   {
/* 1375 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/* 1376 */     if (localDebugGraphicsInfo == null) {
/* 1377 */       return 0;
/*      */     }
/* 1379 */     return localDebugGraphicsInfo.getDebugOptions(paramJComponent);
/*      */   }
/*      */ 
/*      */   static int shouldComponentDebug(JComponent paramJComponent)
/*      */   {
/* 1388 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/* 1389 */     if (localDebugGraphicsInfo == null) {
/* 1390 */       return 0;
/*      */     }
/* 1392 */     Object localObject = paramJComponent;
/* 1393 */     int i = 0;
/*      */ 
/* 1395 */     while ((localObject != null) && ((localObject instanceof JComponent))) {
/* 1396 */       i |= localDebugGraphicsInfo.getDebugOptions((JComponent)localObject);
/* 1397 */       localObject = ((Container)localObject).getParent();
/*      */     }
/*      */ 
/* 1400 */     return i;
/*      */   }
/*      */ 
/*      */   static int debugComponentCount()
/*      */   {
/* 1408 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/* 1409 */     if ((localDebugGraphicsInfo != null) && (localDebugGraphicsInfo.componentToDebug != null))
/*      */     {
/* 1411 */       return localDebugGraphicsInfo.componentToDebug.size();
/*      */     }
/* 1413 */     return 0;
/*      */   }
/*      */ 
/*      */   boolean debugLog()
/*      */   {
/* 1418 */     return (this.debugOptions & 0x1) == 1;
/*      */   }
/*      */ 
/*      */   boolean debugFlash() {
/* 1422 */     return (this.debugOptions & 0x2) == 2;
/*      */   }
/*      */ 
/*      */   boolean debugBuffered() {
/* 1426 */     return (this.debugOptions & 0x4) == 4;
/*      */   }
/*      */ 
/*      */   private Graphics debugGraphics()
/*      */   {
/* 1433 */     DebugGraphicsInfo localDebugGraphicsInfo = info();
/*      */ 
/* 1436 */     if (localDebugGraphicsInfo.debugFrame == null) {
/* 1437 */       localDebugGraphicsInfo.debugFrame = new JFrame();
/* 1438 */       localDebugGraphicsInfo.debugFrame.setSize(500, 500);
/*      */     }
/* 1440 */     JFrame localJFrame = localDebugGraphicsInfo.debugFrame;
/* 1441 */     localJFrame.show();
/* 1442 */     DebugGraphics localDebugGraphics = new DebugGraphics(localJFrame.getGraphics());
/* 1443 */     localDebugGraphics.setFont(getFont());
/* 1444 */     localDebugGraphics.setColor(getColor());
/* 1445 */     localDebugGraphics.translate(this.xOffset, this.yOffset);
/* 1446 */     localDebugGraphics.setClip(getClipBounds());
/* 1447 */     if (debugFlash()) {
/* 1448 */       localDebugGraphics.setDebugOptions(2);
/*      */     }
/* 1450 */     return localDebugGraphics;
/*      */   }
/*      */ 
/*      */   static DebugGraphicsInfo info()
/*      */   {
/* 1456 */     DebugGraphicsInfo localDebugGraphicsInfo = (DebugGraphicsInfo)SwingUtilities.appContextGet(debugGraphicsInfoKey);
/*      */ 
/* 1458 */     if (localDebugGraphicsInfo == null) {
/* 1459 */       localDebugGraphicsInfo = new DebugGraphicsInfo();
/* 1460 */       SwingUtilities.appContextPut(debugGraphicsInfoKey, localDebugGraphicsInfo);
/*      */     }
/*      */ 
/* 1463 */     return localDebugGraphicsInfo;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*   68 */     JComponent.DEBUG_GRAPHICS_LOADED = true;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.DebugGraphics
 * JD-Core Version:    0.6.2
 */