/*      */ package com.sun.imageio.plugins.gif;
/*      */ 
/*      */ import java.util.Locale;
/*      */ import javax.imageio.ImageWriteParam;
/*      */ 
/*      */ class GIFImageWriteParam extends ImageWriteParam
/*      */ {
/*      */   GIFImageWriteParam(Locale paramLocale)
/*      */   {
/* 1304 */     super(paramLocale);
/* 1305 */     this.canWriteCompressed = true;
/* 1306 */     this.canWriteProgressive = true;
/* 1307 */     this.compressionTypes = new String[] { "LZW", "lzw" };
/* 1308 */     this.compressionType = this.compressionTypes[0];
/*      */   }
/*      */ 
/*      */   public void setCompressionMode(int paramInt) {
/* 1312 */     if (paramInt == 0) {
/* 1313 */       throw new UnsupportedOperationException("MODE_DISABLED is not supported.");
/*      */     }
/* 1315 */     super.setCompressionMode(paramInt);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.gif.GIFImageWriteParam
 * JD-Core Version:    0.6.2
 */