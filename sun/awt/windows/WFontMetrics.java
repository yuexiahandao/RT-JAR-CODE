/*     */ package sun.awt.windows;
/*     */ 
/*     */ import java.awt.Font;
/*     */ import java.awt.FontMetrics;
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ class WFontMetrics extends FontMetrics
/*     */ {
/*     */   int[] widths;
/*     */   int ascent;
/*     */   int descent;
/*     */   int leading;
/*     */   int height;
/*     */   int maxAscent;
/*     */   int maxDescent;
/*     */   int maxHeight;
/*     */   int maxAdvance;
/* 190 */   static Hashtable table = new Hashtable();
/*     */ 
/*     */   public WFontMetrics(Font paramFont)
/*     */   {
/* 113 */     super(paramFont);
/* 114 */     init();
/*     */   }
/*     */ 
/*     */   public int getLeading()
/*     */   {
/* 121 */     return this.leading;
/*     */   }
/*     */ 
/*     */   public int getAscent()
/*     */   {
/* 128 */     return this.ascent;
/*     */   }
/*     */ 
/*     */   public int getDescent()
/*     */   {
/* 135 */     return this.descent;
/*     */   }
/*     */ 
/*     */   public int getHeight()
/*     */   {
/* 142 */     return this.height;
/*     */   }
/*     */ 
/*     */   public int getMaxAscent()
/*     */   {
/* 149 */     return this.maxAscent;
/*     */   }
/*     */ 
/*     */   public int getMaxDescent()
/*     */   {
/* 156 */     return this.maxDescent;
/*     */   }
/*     */ 
/*     */   public int getMaxAdvance()
/*     */   {
/* 163 */     return this.maxAdvance;
/*     */   }
/*     */ 
/*     */   public native int stringWidth(String paramString);
/*     */ 
/*     */   public native int charsWidth(char[] paramArrayOfChar, int paramInt1, int paramInt2);
/*     */ 
/*     */   public native int bytesWidth(byte[] paramArrayOfByte, int paramInt1, int paramInt2);
/*     */ 
/*     */   public int[] getWidths()
/*     */   {
/* 185 */     return this.widths;
/*     */   }
/*     */ 
/*     */   native void init();
/*     */ 
/*     */   static FontMetrics getFontMetrics(Font paramFont)
/*     */   {
/* 193 */     Object localObject = (FontMetrics)table.get(paramFont);
/* 194 */     if (localObject == null) {
/* 195 */       table.put(paramFont, localObject = new WFontMetrics(paramFont));
/*     */     }
/* 197 */     return localObject;
/*     */   }
/*     */ 
/*     */   private static native void initIDs();
/*     */ 
/*     */   static
/*     */   {
/*  39 */     initIDs();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WFontMetrics
 * JD-Core Version:    0.6.2
 */