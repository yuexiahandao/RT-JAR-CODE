/*     */ package com.sun.imageio.plugins.png;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import javax.imageio.ImageWriteParam;
/*     */ 
/*     */ class PNGImageWriteParam extends ImageWriteParam
/*     */ {
/*     */   public PNGImageWriteParam(Locale paramLocale)
/*     */   {
/* 271 */     this.canWriteProgressive = true;
/* 272 */     this.locale = paramLocale;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.png.PNGImageWriteParam
 * JD-Core Version:    0.6.2
 */