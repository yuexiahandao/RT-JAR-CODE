/*     */ package java.awt;
/*     */ 
/*     */ import java.awt.font.FontRenderContext;
/*     */ import java.awt.font.LineMetrics;
/*     */ import java.awt.geom.Rectangle2D;
/*     */ import java.io.Serializable;
/*     */ import java.text.CharacterIterator;
/*     */ 
/*     */ public abstract class FontMetrics
/*     */   implements Serializable
/*     */ {
/* 110 */   private static final FontRenderContext DEFAULT_FRC = new FontRenderContext(null, false, false);
/*     */   protected Font font;
/*     */   private static final long serialVersionUID = 1681126225205050147L;
/*     */ 
/*     */   protected FontMetrics(Font paramFont)
/*     */   {
/* 135 */     this.font = paramFont;
/*     */   }
/*     */ 
/*     */   public Font getFont()
/*     */   {
/* 145 */     return this.font;
/*     */   }
/*     */ 
/*     */   public FontRenderContext getFontRenderContext()
/*     */   {
/* 161 */     return DEFAULT_FRC;
/*     */   }
/*     */ 
/*     */   public int getLeading()
/*     */   {
/* 177 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getAscent()
/*     */   {
/* 190 */     return this.font.getSize();
/*     */   }
/*     */ 
/*     */   public int getDescent()
/*     */   {
/* 205 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/* 222 */     return getLeading() + getAscent() + getDescent();
/*     */   }
/*     */ 
/*     */   public int getMaxAscent()
/*     */   {
/* 234 */     return getAscent();
/*     */   }
/*     */ 
/*     */   public int getMaxDescent()
/*     */   {
/* 246 */     return getDescent();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public int getMaxDecent()
/*     */   {
/* 259 */     return getMaxDescent();
/*     */   }
/*     */ 
/*     */   public int getMaxAdvance()
/*     */   {
/* 273 */     return -1;
/*     */   }
/*     */ 
/*     */   public int charWidth(int paramInt)
/*     */   {
/* 298 */     if (!Character.isValidCodePoint(paramInt)) {
/* 299 */       paramInt = 65535;
/*     */     }
/*     */ 
/* 302 */     if (paramInt < 256) {
/* 303 */       return getWidths()[paramInt];
/*     */     }
/* 305 */     char[] arrayOfChar = new char[2];
/* 306 */     int i = Character.toChars(paramInt, arrayOfChar, 0);
/* 307 */     return charsWidth(arrayOfChar, 0, i);
/*     */   }
/*     */ 
/*     */   public int charWidth(char paramChar)
/*     */   {
/* 332 */     if (paramChar < 'Ā') {
/* 333 */       return getWidths()[paramChar];
/*     */     }
/* 335 */     char[] arrayOfChar = { paramChar };
/* 336 */     return charsWidth(arrayOfChar, 0, 1);
/*     */   }
/*     */ 
/*     */   public int stringWidth(String paramString)
/*     */   {
/* 357 */     int i = paramString.length();
/* 358 */     char[] arrayOfChar = new char[i];
/* 359 */     paramString.getChars(0, i, arrayOfChar, 0);
/* 360 */     return charsWidth(arrayOfChar, 0, i);
/*     */   }
/*     */ 
/*     */   public int charsWidth(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 387 */     return stringWidth(new String(paramArrayOfChar, paramInt1, paramInt2));
/*     */   }
/*     */ 
/*     */   public int bytesWidth(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 413 */     return stringWidth(new String(paramArrayOfByte, 0, paramInt1, paramInt2));
/*     */   }
/*     */ 
/*     */   public int[] getWidths()
/*     */   {
/* 428 */     int[] arrayOfInt = new int[256];
/*     */     int j;
/* 429 */     for (int i = 0; i < 256; j = (char)(i + 1)) {
/* 430 */       arrayOfInt[i] = charWidth(i);
/*     */     }
/* 432 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public boolean hasUniformLineMetrics()
/*     */   {
/* 448 */     return this.font.hasUniformLineMetrics();
/*     */   }
/*     */ 
/*     */   public LineMetrics getLineMetrics(String paramString, Graphics paramGraphics)
/*     */   {
/* 461 */     return this.font.getLineMetrics(paramString, myFRC(paramGraphics));
/*     */   }
/*     */ 
/*     */   public LineMetrics getLineMetrics(String paramString, int paramInt1, int paramInt2, Graphics paramGraphics)
/*     */   {
/* 478 */     return this.font.getLineMetrics(paramString, paramInt1, paramInt2, myFRC(paramGraphics));
/*     */   }
/*     */ 
/*     */   public LineMetrics getLineMetrics(char[] paramArrayOfChar, int paramInt1, int paramInt2, Graphics paramGraphics)
/*     */   {
/* 495 */     return this.font.getLineMetrics(paramArrayOfChar, paramInt1, paramInt2, myFRC(paramGraphics));
/*     */   }
/*     */ 
/*     */   public LineMetrics getLineMetrics(CharacterIterator paramCharacterIterator, int paramInt1, int paramInt2, Graphics paramGraphics)
/*     */   {
/* 514 */     return this.font.getLineMetrics(paramCharacterIterator, paramInt1, paramInt2, myFRC(paramGraphics));
/*     */   }
/*     */ 
/*     */   public Rectangle2D getStringBounds(String paramString, Graphics paramGraphics)
/*     */   {
/* 531 */     return this.font.getStringBounds(paramString, myFRC(paramGraphics));
/*     */   }
/*     */ 
/*     */   public Rectangle2D getStringBounds(String paramString, int paramInt1, int paramInt2, Graphics paramGraphics)
/*     */   {
/* 552 */     return this.font.getStringBounds(paramString, paramInt1, paramInt2, myFRC(paramGraphics));
/*     */   }
/*     */ 
/*     */   public Rectangle2D getStringBounds(char[] paramArrayOfChar, int paramInt1, int paramInt2, Graphics paramGraphics)
/*     */   {
/* 577 */     return this.font.getStringBounds(paramArrayOfChar, paramInt1, paramInt2, myFRC(paramGraphics));
/*     */   }
/*     */ 
/*     */   public Rectangle2D getStringBounds(CharacterIterator paramCharacterIterator, int paramInt1, int paramInt2, Graphics paramGraphics)
/*     */   {
/* 599 */     return this.font.getStringBounds(paramCharacterIterator, paramInt1, paramInt2, myFRC(paramGraphics));
/*     */   }
/*     */ 
/*     */   public Rectangle2D getMaxCharBounds(Graphics paramGraphics)
/*     */   {
/* 612 */     return this.font.getMaxCharBounds(myFRC(paramGraphics));
/*     */   }
/*     */ 
/*     */   private FontRenderContext myFRC(Graphics paramGraphics) {
/* 616 */     if ((paramGraphics instanceof Graphics2D)) {
/* 617 */       return ((Graphics2D)paramGraphics).getFontRenderContext();
/*     */     }
/* 619 */     return DEFAULT_FRC;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 631 */     return getClass().getName() + "[font=" + getFont() + "ascent=" + getAscent() + ", descent=" + getDescent() + ", height=" + getHeight() + "]";
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   static
/*     */   {
/* 103 */     Toolkit.loadLibraries();
/* 104 */     if (!GraphicsEnvironment.isHeadless())
/* 105 */       initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.FontMetrics
 * JD-Core Version:    0.6.2
 */