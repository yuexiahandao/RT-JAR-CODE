/*      */ package java.awt;
/*      */ 
/*      */ import java.awt.image.ImageObserver;
/*      */ import java.text.AttributedCharacterIterator;
/*      */ 
/*      */ public abstract class Graphics
/*      */ {
/*      */   public abstract Graphics create();
/*      */ 
/*      */   public Graphics create(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  164 */     Graphics localGraphics = create();
/*  165 */     if (localGraphics == null) return null;
/*  166 */     localGraphics.translate(paramInt1, paramInt2);
/*  167 */     localGraphics.clipRect(0, 0, paramInt3, paramInt4);
/*  168 */     return localGraphics;
/*      */   }
/*      */ 
/*      */   public abstract void translate(int paramInt1, int paramInt2);
/*      */ 
/*      */   public abstract Color getColor();
/*      */ 
/*      */   public abstract void setColor(Color paramColor);
/*      */ 
/*      */   public abstract void setPaintMode();
/*      */ 
/*      */   public abstract void setXORMode(Color paramColor);
/*      */ 
/*      */   public abstract Font getFont();
/*      */ 
/*      */   public abstract void setFont(Font paramFont);
/*      */ 
/*      */   public FontMetrics getFontMetrics()
/*      */   {
/*  257 */     return getFontMetrics(getFont());
/*      */   }
/*      */ 
/*      */   public abstract FontMetrics getFontMetrics(Font paramFont);
/*      */ 
/*      */   public abstract Rectangle getClipBounds();
/*      */ 
/*      */   public abstract void clipRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*      */ 
/*      */   public abstract void setClip(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*      */ 
/*      */   public abstract Shape getClip();
/*      */ 
/*      */   public abstract void setClip(Shape paramShape);
/*      */ 
/*      */   public abstract void copyArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
/*      */ 
/*      */   public abstract void drawLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*      */ 
/*      */   public abstract void fillRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*      */ 
/*      */   public void drawRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  435 */     if ((paramInt3 < 0) || (paramInt4 < 0)) {
/*  436 */       return;
/*      */     }
/*      */ 
/*  439 */     if ((paramInt4 == 0) || (paramInt3 == 0)) {
/*  440 */       drawLine(paramInt1, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4);
/*      */     } else {
/*  442 */       drawLine(paramInt1, paramInt2, paramInt1 + paramInt3 - 1, paramInt2);
/*  443 */       drawLine(paramInt1 + paramInt3, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4 - 1);
/*  444 */       drawLine(paramInt1 + paramInt3, paramInt2 + paramInt4, paramInt1 + 1, paramInt2 + paramInt4);
/*  445 */       drawLine(paramInt1, paramInt2 + paramInt4, paramInt1, paramInt2 + 1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public abstract void clearRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*      */ 
/*      */   public abstract void drawRoundRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
/*      */ 
/*      */   public abstract void fillRoundRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
/*      */ 
/*      */   public void draw3DRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
/*      */   {
/*  529 */     Color localColor1 = getColor();
/*  530 */     Color localColor2 = localColor1.brighter();
/*  531 */     Color localColor3 = localColor1.darker();
/*      */ 
/*  533 */     setColor(paramBoolean ? localColor2 : localColor3);
/*  534 */     drawLine(paramInt1, paramInt2, paramInt1, paramInt2 + paramInt4);
/*  535 */     drawLine(paramInt1 + 1, paramInt2, paramInt1 + paramInt3 - 1, paramInt2);
/*  536 */     setColor(paramBoolean ? localColor3 : localColor2);
/*  537 */     drawLine(paramInt1 + 1, paramInt2 + paramInt4, paramInt1 + paramInt3, paramInt2 + paramInt4);
/*  538 */     drawLine(paramInt1 + paramInt3, paramInt2, paramInt1 + paramInt3, paramInt2 + paramInt4 - 1);
/*  539 */     setColor(localColor1);
/*      */   }
/*      */ 
/*      */   public void fill3DRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
/*      */   {
/*  559 */     Color localColor1 = getColor();
/*  560 */     Color localColor2 = localColor1.brighter();
/*  561 */     Color localColor3 = localColor1.darker();
/*      */ 
/*  563 */     if (!paramBoolean) {
/*  564 */       setColor(localColor3);
/*      */     }
/*  566 */     fillRect(paramInt1 + 1, paramInt2 + 1, paramInt3 - 2, paramInt4 - 2);
/*  567 */     setColor(paramBoolean ? localColor2 : localColor3);
/*  568 */     drawLine(paramInt1, paramInt2, paramInt1, paramInt2 + paramInt4 - 1);
/*  569 */     drawLine(paramInt1 + 1, paramInt2, paramInt1 + paramInt3 - 2, paramInt2);
/*  570 */     setColor(paramBoolean ? localColor3 : localColor2);
/*  571 */     drawLine(paramInt1 + 1, paramInt2 + paramInt4 - 1, paramInt1 + paramInt3 - 1, paramInt2 + paramInt4 - 1);
/*  572 */     drawLine(paramInt1 + paramInt3 - 1, paramInt2, paramInt1 + paramInt3 - 1, paramInt2 + paramInt4 - 2);
/*  573 */     setColor(localColor1);
/*      */   }
/*      */ 
/*      */   public abstract void drawOval(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*      */ 
/*      */   public abstract void fillOval(int paramInt1, int paramInt2, int paramInt3, int paramInt4);
/*      */ 
/*      */   public abstract void drawArc(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
/*      */ 
/*      */   public abstract void fillArc(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6);
/*      */ 
/*      */   public abstract void drawPolyline(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt);
/*      */ 
/*      */   public abstract void drawPolygon(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt);
/*      */ 
/*      */   public void drawPolygon(Polygon paramPolygon)
/*      */   {
/*  732 */     drawPolygon(paramPolygon.xpoints, paramPolygon.ypoints, paramPolygon.npoints);
/*      */   }
/*      */ 
/*      */   public abstract void fillPolygon(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt);
/*      */ 
/*      */   public void fillPolygon(Polygon paramPolygon)
/*      */   {
/*  768 */     fillPolygon(paramPolygon.xpoints, paramPolygon.ypoints, paramPolygon.npoints);
/*      */   }
/*      */ 
/*      */   public abstract void drawString(String paramString, int paramInt1, int paramInt2);
/*      */ 
/*      */   public abstract void drawString(AttributedCharacterIterator paramAttributedCharacterIterator, int paramInt1, int paramInt2);
/*      */ 
/*      */   public void drawChars(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  822 */     drawString(new String(paramArrayOfChar, paramInt1, paramInt2), paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void drawBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  847 */     drawString(new String(paramArrayOfByte, 0, paramInt1, paramInt2), paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public abstract boolean drawImage(Image paramImage, int paramInt1, int paramInt2, ImageObserver paramImageObserver);
/*      */ 
/*      */   public abstract boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, ImageObserver paramImageObserver);
/*      */ 
/*      */   public abstract boolean drawImage(Image paramImage, int paramInt1, int paramInt2, Color paramColor, ImageObserver paramImageObserver);
/*      */ 
/*      */   public abstract boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor, ImageObserver paramImageObserver);
/*      */ 
/*      */   public abstract boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, ImageObserver paramImageObserver);
/*      */ 
/*      */   public abstract boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Color paramColor, ImageObserver paramImageObserver);
/*      */ 
/*      */   public abstract void dispose();
/*      */ 
/*      */   public void finalize()
/*      */   {
/* 1164 */     dispose();
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1173 */     return getClass().getName() + "[font=" + getFont() + ",color=" + getColor() + "]";
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Rectangle getClipRect()
/*      */   {
/* 1185 */     return getClipBounds();
/*      */   }
/*      */ 
/*      */   public boolean hitClip(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1218 */     Rectangle localRectangle = getClipBounds();
/* 1219 */     if (localRectangle == null) {
/* 1220 */       return true;
/*      */     }
/* 1222 */     return localRectangle.intersects(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public Rectangle getClipBounds(Rectangle paramRectangle)
/*      */   {
/* 1245 */     Rectangle localRectangle = getClipBounds();
/* 1246 */     if (localRectangle != null) {
/* 1247 */       paramRectangle.x = localRectangle.x;
/* 1248 */       paramRectangle.y = localRectangle.y;
/* 1249 */       paramRectangle.width = localRectangle.width;
/* 1250 */       paramRectangle.height = localRectangle.height;
/* 1251 */     } else if (paramRectangle == null) {
/* 1252 */       throw new NullPointerException("null rectangle parameter");
/*      */     }
/* 1254 */     return paramRectangle;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.Graphics
 * JD-Core Version:    0.6.2
 */