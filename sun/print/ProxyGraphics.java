/*      */ package sun.print;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Image;
/*      */ import java.awt.Polygon;
/*      */ import java.awt.Rectangle;
/*      */ import java.awt.Shape;
/*      */ import java.awt.image.ImageObserver;
/*      */ import java.text.AttributedCharacterIterator;
/*      */ 
/*      */ public class ProxyGraphics extends Graphics
/*      */ {
/*      */   private Graphics g;
/*      */ 
/*      */   public ProxyGraphics(Graphics paramGraphics)
/*      */   {
/*   58 */     this.g = paramGraphics;
/*      */   }
/*      */ 
/*      */   Graphics getGraphics() {
/*   62 */     return this.g;
/*      */   }
/*      */ 
/*      */   public Graphics create()
/*      */   {
/*   72 */     return new ProxyGraphics(this.g.create());
/*      */   }
/*      */ 
/*      */   public Graphics create(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  110 */     return new ProxyGraphics(this.g.create(paramInt1, paramInt2, paramInt3, paramInt4));
/*      */   }
/*      */ 
/*      */   public void translate(int paramInt1, int paramInt2)
/*      */   {
/*  125 */     this.g.translate(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public Color getColor()
/*      */   {
/*  135 */     return this.g.getColor();
/*      */   }
/*      */ 
/*      */   public void setColor(Color paramColor)
/*      */   {
/*  147 */     this.g.setColor(paramColor);
/*      */   }
/*      */ 
/*      */   public void setPaintMode()
/*      */   {
/*  158 */     this.g.setPaintMode();
/*      */   }
/*      */ 
/*      */   public void setXORMode(Color paramColor)
/*      */   {
/*  177 */     this.g.setXORMode(paramColor);
/*      */   }
/*      */ 
/*      */   public Font getFont()
/*      */   {
/*  187 */     return this.g.getFont();
/*      */   }
/*      */ 
/*      */   public void setFont(Font paramFont)
/*      */   {
/*  201 */     this.g.setFont(paramFont);
/*      */   }
/*      */ 
/*      */   public FontMetrics getFontMetrics()
/*      */   {
/*  213 */     return this.g.getFontMetrics();
/*      */   }
/*      */ 
/*      */   public FontMetrics getFontMetrics(Font paramFont)
/*      */   {
/*  225 */     return this.g.getFontMetrics(paramFont);
/*      */   }
/*      */ 
/*      */   public Rectangle getClipBounds()
/*      */   {
/*  247 */     return this.g.getClipBounds();
/*      */   }
/*      */ 
/*      */   public void clipRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  270 */     this.g.clipRect(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void setClip(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  288 */     this.g.setClip(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public Shape getClip()
/*      */   {
/*  308 */     return this.g.getClip();
/*      */   }
/*      */ 
/*      */   public void setClip(Shape paramShape)
/*      */   {
/*  328 */     this.g.setClip(paramShape);
/*      */   }
/*      */ 
/*      */   public void copyArea(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  352 */     this.g.copyArea(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */   }
/*      */ 
/*      */   public void drawLine(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  365 */     this.g.drawLine(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void fillRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  388 */     this.g.fillRect(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void drawRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  408 */     this.g.drawRect(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void clearRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  431 */     this.g.clearRect(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void drawRoundRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  452 */     this.g.drawRoundRect(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */   }
/*      */ 
/*      */   public void fillRoundRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  473 */     this.g.fillRoundRect(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */   }
/*      */ 
/*      */   public void draw3DRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
/*      */   {
/*  497 */     this.g.draw3DRect(paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean);
/*      */   }
/*      */ 
/*      */   public void fill3DRect(int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean)
/*      */   {
/*  517 */     this.g.fill3DRect(paramInt1, paramInt2, paramInt3, paramInt4, paramBoolean);
/*      */   }
/*      */ 
/*      */   public void drawOval(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  538 */     this.g.drawOval(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void fillOval(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  553 */     this.g.fillOval(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void drawArc(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  595 */     this.g.drawArc(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */   }
/*      */ 
/*      */   public void fillArc(int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6)
/*      */   {
/*  637 */     this.g.fillArc(paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6);
/*      */   }
/*      */ 
/*      */   public void drawPolyline(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*      */   {
/*  654 */     this.g.drawPolyline(paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*      */   }
/*      */ 
/*      */   public void drawPolygon(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*      */   {
/*  678 */     this.g.drawPolygon(paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*      */   }
/*      */ 
/*      */   public void drawPolygon(Polygon paramPolygon)
/*      */   {
/*  689 */     this.g.drawPolygon(paramPolygon);
/*      */   }
/*      */ 
/*      */   public void fillPolygon(int[] paramArrayOfInt1, int[] paramArrayOfInt2, int paramInt)
/*      */   {
/*  714 */     this.g.fillPolygon(paramArrayOfInt1, paramArrayOfInt2, paramInt);
/*      */   }
/*      */ 
/*      */   public void fillPolygon(Polygon paramPolygon)
/*      */   {
/*  727 */     this.g.fillPolygon(paramPolygon);
/*      */   }
/*      */ 
/*      */   public void drawString(String paramString, int paramInt1, int paramInt2)
/*      */   {
/*  742 */     this.g.drawString(paramString, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void drawString(AttributedCharacterIterator paramAttributedCharacterIterator, int paramInt1, int paramInt2)
/*      */   {
/*  759 */     this.g.drawString(paramAttributedCharacterIterator, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void drawChars(char[] paramArrayOfChar, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  776 */     this.g.drawChars(paramArrayOfChar, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public void drawBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  793 */     this.g.drawBytes(paramArrayOfByte, paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, ImageObserver paramImageObserver)
/*      */   {
/*  822 */     return this.g.drawImage(paramImage, paramInt1, paramInt2, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, ImageObserver paramImageObserver)
/*      */   {
/*  861 */     return this.g.drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/*  897 */     return this.g.drawImage(paramImage, paramInt1, paramInt2, paramColor, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/*  943 */     return this.g.drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramColor, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, ImageObserver paramImageObserver)
/*      */   {
/*  998 */     return this.g.drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public boolean drawImage(Image paramImage, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8, Color paramColor, ImageObserver paramImageObserver)
/*      */   {
/* 1062 */     return this.g.drawImage(paramImage, paramInt1, paramInt2, paramInt3, paramInt4, paramInt5, paramInt6, paramInt7, paramInt8, paramColor, paramImageObserver);
/*      */   }
/*      */ 
/*      */   public void dispose()
/*      */   {
/* 1096 */     this.g.dispose();
/*      */   }
/*      */ 
/*      */   public void finalize()
/*      */   {
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1111 */     return getClass().getName() + "[font=" + getFont() + ",color=" + getColor() + "]";
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Rectangle getClipRect()
/*      */   {
/* 1120 */     return this.g.getClipRect();
/*      */   }
/*      */ 
/*      */   public boolean hitClip(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/* 1135 */     return this.g.hitClip(paramInt1, paramInt2, paramInt3, paramInt4);
/*      */   }
/*      */ 
/*      */   public Rectangle getClipBounds(Rectangle paramRectangle)
/*      */   {
/* 1155 */     return this.g.getClipBounds(paramRectangle);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.ProxyGraphics
 * JD-Core Version:    0.6.2
 */