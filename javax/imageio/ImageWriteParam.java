/*      */ package javax.imageio;
/*      */ 
/*      */ import java.awt.Dimension;
/*      */ import java.util.Locale;
/*      */ 
/*      */ public class ImageWriteParam extends IIOParam
/*      */ {
/*      */   public static final int MODE_DISABLED = 0;
/*      */   public static final int MODE_DEFAULT = 1;
/*      */   public static final int MODE_EXPLICIT = 2;
/*      */   public static final int MODE_COPY_FROM_METADATA = 3;
/*      */   private static final int MAX_MODE = 3;
/*  192 */   protected boolean canWriteTiles = false;
/*      */ 
/*  208 */   protected int tilingMode = 3;
/*      */ 
/*  221 */   protected Dimension[] preferredTileSizes = null;
/*      */ 
/*  230 */   protected boolean tilingSet = false;
/*      */ 
/*  238 */   protected int tileWidth = 0;
/*      */ 
/*  247 */   protected int tileHeight = 0;
/*      */ 
/*  259 */   protected boolean canOffsetTiles = false;
/*      */ 
/*  269 */   protected int tileGridXOffset = 0;
/*      */ 
/*  279 */   protected int tileGridYOffset = 0;
/*      */ 
/*  291 */   protected boolean canWriteProgressive = false;
/*      */ 
/*  309 */   protected int progressiveMode = 3;
/*      */ 
/*  319 */   protected boolean canWriteCompressed = false;
/*      */ 
/*  336 */   protected int compressionMode = 3;
/*      */ 
/*  346 */   protected String[] compressionTypes = null;
/*      */ 
/*  355 */   protected String compressionType = null;
/*      */ 
/*  364 */   protected float compressionQuality = 1.0F;
/*      */ 
/*  372 */   protected Locale locale = null;
/*      */ 
/*      */   protected ImageWriteParam()
/*      */   {
/*      */   }
/*      */ 
/*      */   public ImageWriteParam(Locale paramLocale)
/*      */   {
/*  389 */     this.locale = paramLocale;
/*      */   }
/*      */ 
/*      */   private static Dimension[] clonePreferredTileSizes(Dimension[] paramArrayOfDimension)
/*      */   {
/*  394 */     if (paramArrayOfDimension == null) {
/*  395 */       return null;
/*      */     }
/*  397 */     Dimension[] arrayOfDimension = new Dimension[paramArrayOfDimension.length];
/*  398 */     for (int i = 0; i < paramArrayOfDimension.length; i++) {
/*  399 */       arrayOfDimension[i] = new Dimension(paramArrayOfDimension[i]);
/*      */     }
/*  401 */     return arrayOfDimension;
/*      */   }
/*      */ 
/*      */   public Locale getLocale()
/*      */   {
/*  412 */     return this.locale;
/*      */   }
/*      */ 
/*      */   public boolean canWriteTiles()
/*      */   {
/*  427 */     return this.canWriteTiles;
/*      */   }
/*      */ 
/*      */   public boolean canOffsetTiles()
/*      */   {
/*  446 */     return this.canOffsetTiles;
/*      */   }
/*      */ 
/*      */   public void setTilingMode(int paramInt)
/*      */   {
/*  487 */     if (!canWriteTiles()) {
/*  488 */       throw new UnsupportedOperationException("Tiling not supported!");
/*      */     }
/*  490 */     if ((paramInt < 0) || (paramInt > 3)) {
/*  491 */       throw new IllegalArgumentException("Illegal value for mode!");
/*      */     }
/*  493 */     this.tilingMode = paramInt;
/*  494 */     if (paramInt == 2)
/*  495 */       unsetTiling();
/*      */   }
/*      */ 
/*      */   public int getTilingMode()
/*      */   {
/*  511 */     if (!canWriteTiles()) {
/*  512 */       throw new UnsupportedOperationException("Tiling not supported");
/*      */     }
/*  514 */     return this.tilingMode;
/*      */   }
/*      */ 
/*      */   public Dimension[] getPreferredTileSizes()
/*      */   {
/*  541 */     if (!canWriteTiles()) {
/*  542 */       throw new UnsupportedOperationException("Tiling not supported");
/*      */     }
/*  544 */     return clonePreferredTileSizes(this.preferredTileSizes);
/*      */   }
/*      */ 
/*      */   public void setTiling(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*      */   {
/*  586 */     if (!canWriteTiles()) {
/*  587 */       throw new UnsupportedOperationException("Tiling not supported!");
/*      */     }
/*  589 */     if (getTilingMode() != 2) {
/*  590 */       throw new IllegalStateException("Tiling mode not MODE_EXPLICIT!");
/*      */     }
/*  592 */     if ((paramInt1 <= 0) || (paramInt2 <= 0)) {
/*  593 */       throw new IllegalArgumentException("tile dimensions are non-positive!");
/*      */     }
/*      */ 
/*  596 */     int i = (paramInt3 != 0) || (paramInt4 != 0) ? 1 : 0;
/*  597 */     if ((!canOffsetTiles()) && (i != 0)) {
/*  598 */       throw new UnsupportedOperationException("Can't offset tiles!");
/*      */     }
/*  600 */     if (this.preferredTileSizes != null) {
/*  601 */       int j = 1;
/*  602 */       for (int k = 0; k < this.preferredTileSizes.length; k += 2) {
/*  603 */         Dimension localDimension1 = this.preferredTileSizes[k];
/*  604 */         Dimension localDimension2 = this.preferredTileSizes[(k + 1)];
/*  605 */         if ((paramInt1 < localDimension1.width) || (paramInt1 > localDimension2.width) || (paramInt2 < localDimension1.height) || (paramInt2 > localDimension2.height))
/*      */         {
/*  609 */           j = 0;
/*  610 */           break;
/*      */         }
/*      */       }
/*  613 */       if (j == 0) {
/*  614 */         throw new IllegalArgumentException("Illegal tile size!");
/*      */       }
/*      */     }
/*      */ 
/*  618 */     this.tilingSet = true;
/*  619 */     this.tileWidth = paramInt1;
/*  620 */     this.tileHeight = paramInt2;
/*  621 */     this.tileGridXOffset = paramInt3;
/*  622 */     this.tileGridYOffset = paramInt4;
/*      */   }
/*      */ 
/*      */   public void unsetTiling()
/*      */   {
/*  642 */     if (!canWriteTiles()) {
/*  643 */       throw new UnsupportedOperationException("Tiling not supported!");
/*      */     }
/*  645 */     if (getTilingMode() != 2) {
/*  646 */       throw new IllegalStateException("Tiling mode not MODE_EXPLICIT!");
/*      */     }
/*  648 */     this.tilingSet = false;
/*  649 */     this.tileWidth = 0;
/*  650 */     this.tileHeight = 0;
/*  651 */     this.tileGridXOffset = 0;
/*  652 */     this.tileGridYOffset = 0;
/*      */   }
/*      */ 
/*      */   public int getTileWidth()
/*      */   {
/*  673 */     if (!canWriteTiles()) {
/*  674 */       throw new UnsupportedOperationException("Tiling not supported!");
/*      */     }
/*  676 */     if (getTilingMode() != 2) {
/*  677 */       throw new IllegalStateException("Tiling mode not MODE_EXPLICIT!");
/*      */     }
/*  679 */     if (!this.tilingSet) {
/*  680 */       throw new IllegalStateException("Tiling parameters not set!");
/*      */     }
/*  682 */     return this.tileWidth;
/*      */   }
/*      */ 
/*      */   public int getTileHeight()
/*      */   {
/*  703 */     if (!canWriteTiles()) {
/*  704 */       throw new UnsupportedOperationException("Tiling not supported!");
/*      */     }
/*  706 */     if (getTilingMode() != 2) {
/*  707 */       throw new IllegalStateException("Tiling mode not MODE_EXPLICIT!");
/*      */     }
/*  709 */     if (!this.tilingSet) {
/*  710 */       throw new IllegalStateException("Tiling parameters not set!");
/*      */     }
/*  712 */     return this.tileHeight;
/*      */   }
/*      */ 
/*      */   public int getTileGridXOffset()
/*      */   {
/*  733 */     if (!canWriteTiles()) {
/*  734 */       throw new UnsupportedOperationException("Tiling not supported!");
/*      */     }
/*  736 */     if (getTilingMode() != 2) {
/*  737 */       throw new IllegalStateException("Tiling mode not MODE_EXPLICIT!");
/*      */     }
/*  739 */     if (!this.tilingSet) {
/*  740 */       throw new IllegalStateException("Tiling parameters not set!");
/*      */     }
/*  742 */     return this.tileGridXOffset;
/*      */   }
/*      */ 
/*      */   public int getTileGridYOffset()
/*      */   {
/*  763 */     if (!canWriteTiles()) {
/*  764 */       throw new UnsupportedOperationException("Tiling not supported!");
/*      */     }
/*  766 */     if (getTilingMode() != 2) {
/*  767 */       throw new IllegalStateException("Tiling mode not MODE_EXPLICIT!");
/*      */     }
/*  769 */     if (!this.tilingSet) {
/*  770 */       throw new IllegalStateException("Tiling parameters not set!");
/*      */     }
/*  772 */     return this.tileGridYOffset;
/*      */   }
/*      */ 
/*      */   public boolean canWriteProgressive()
/*      */   {
/*  786 */     return this.canWriteProgressive;
/*      */   }
/*      */ 
/*      */   public void setProgressiveMode(int paramInt)
/*      */   {
/*  830 */     if (!canWriteProgressive()) {
/*  831 */       throw new UnsupportedOperationException("Progressive output not supported");
/*      */     }
/*      */ 
/*  834 */     if ((paramInt < 0) || (paramInt > 3)) {
/*  835 */       throw new IllegalArgumentException("Illegal value for mode!");
/*      */     }
/*  837 */     if (paramInt == 2) {
/*  838 */       throw new IllegalArgumentException("MODE_EXPLICIT not supported for progressive output");
/*      */     }
/*      */ 
/*  841 */     this.progressiveMode = paramInt;
/*      */   }
/*      */ 
/*      */   public int getProgressiveMode()
/*      */   {
/*  856 */     if (!canWriteProgressive()) {
/*  857 */       throw new UnsupportedOperationException("Progressive output not supported");
/*      */     }
/*      */ 
/*  860 */     return this.progressiveMode;
/*      */   }
/*      */ 
/*      */   public boolean canWriteCompressed()
/*      */   {
/*  869 */     return this.canWriteCompressed;
/*      */   }
/*      */ 
/*      */   public void setCompressionMode(int paramInt)
/*      */   {
/*  914 */     if (!canWriteCompressed()) {
/*  915 */       throw new UnsupportedOperationException("Compression not supported.");
/*      */     }
/*      */ 
/*  918 */     if ((paramInt < 0) || (paramInt > 3)) {
/*  919 */       throw new IllegalArgumentException("Illegal value for mode!");
/*      */     }
/*  921 */     this.compressionMode = paramInt;
/*  922 */     if (paramInt == 2)
/*  923 */       unsetCompression();
/*      */   }
/*      */ 
/*      */   public int getCompressionMode()
/*      */   {
/*  939 */     if (!canWriteCompressed()) {
/*  940 */       throw new UnsupportedOperationException("Compression not supported.");
/*      */     }
/*      */ 
/*  943 */     return this.compressionMode;
/*      */   }
/*      */ 
/*      */   public String[] getCompressionTypes()
/*      */   {
/*  973 */     if (!canWriteCompressed()) {
/*  974 */       throw new UnsupportedOperationException("Compression not supported");
/*      */     }
/*      */ 
/*  977 */     if (this.compressionTypes == null) {
/*  978 */       return null;
/*      */     }
/*  980 */     return (String[])this.compressionTypes.clone();
/*      */   }
/*      */ 
/*      */   public void setCompressionType(String paramString)
/*      */   {
/* 1017 */     if (!canWriteCompressed()) {
/* 1018 */       throw new UnsupportedOperationException("Compression not supported");
/*      */     }
/*      */ 
/* 1021 */     if (getCompressionMode() != 2) {
/* 1022 */       throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
/*      */     }
/*      */ 
/* 1025 */     String[] arrayOfString = getCompressionTypes();
/* 1026 */     if (arrayOfString == null) {
/* 1027 */       throw new UnsupportedOperationException("No settable compression types");
/*      */     }
/*      */ 
/* 1030 */     if (paramString != null) {
/* 1031 */       int i = 0;
/* 1032 */       if (arrayOfString != null) {
/* 1033 */         for (int j = 0; j < arrayOfString.length; j++) {
/* 1034 */           if (paramString.equals(arrayOfString[j])) {
/* 1035 */             i = 1;
/* 1036 */             break;
/*      */           }
/*      */         }
/*      */       }
/* 1040 */       if (i == 0) {
/* 1041 */         throw new IllegalArgumentException("Unknown compression type!");
/*      */       }
/*      */     }
/* 1044 */     this.compressionType = paramString;
/*      */   }
/*      */ 
/*      */   public String getCompressionType()
/*      */   {
/* 1071 */     if (!canWriteCompressed()) {
/* 1072 */       throw new UnsupportedOperationException("Compression not supported.");
/*      */     }
/*      */ 
/* 1075 */     if (getCompressionMode() != 2) {
/* 1076 */       throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
/*      */     }
/*      */ 
/* 1079 */     return this.compressionType;
/*      */   }
/*      */ 
/*      */   public void unsetCompression()
/*      */   {
/* 1099 */     if (!canWriteCompressed()) {
/* 1100 */       throw new UnsupportedOperationException("Compression not supported");
/*      */     }
/*      */ 
/* 1103 */     if (getCompressionMode() != 2) {
/* 1104 */       throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
/*      */     }
/*      */ 
/* 1107 */     this.compressionType = null;
/* 1108 */     this.compressionQuality = 1.0F;
/*      */   }
/*      */ 
/*      */   public String getLocalizedCompressionTypeName()
/*      */   {
/* 1133 */     if (!canWriteCompressed()) {
/* 1134 */       throw new UnsupportedOperationException("Compression not supported.");
/*      */     }
/*      */ 
/* 1137 */     if (getCompressionMode() != 2) {
/* 1138 */       throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
/*      */     }
/*      */ 
/* 1141 */     if (getCompressionType() == null) {
/* 1142 */       throw new IllegalStateException("No compression type set!");
/*      */     }
/* 1144 */     return getCompressionType();
/*      */   }
/*      */ 
/*      */   public boolean isCompressionLossless()
/*      */   {
/* 1175 */     if (!canWriteCompressed()) {
/* 1176 */       throw new UnsupportedOperationException("Compression not supported");
/*      */     }
/*      */ 
/* 1179 */     if (getCompressionMode() != 2) {
/* 1180 */       throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
/*      */     }
/*      */ 
/* 1183 */     if ((getCompressionTypes() != null) && (getCompressionType() == null))
/*      */     {
/* 1185 */       throw new IllegalStateException("No compression type set!");
/*      */     }
/* 1187 */     return true;
/*      */   }
/*      */ 
/*      */   public void setCompressionQuality(float paramFloat)
/*      */   {
/* 1235 */     if (!canWriteCompressed()) {
/* 1236 */       throw new UnsupportedOperationException("Compression not supported");
/*      */     }
/*      */ 
/* 1239 */     if (getCompressionMode() != 2) {
/* 1240 */       throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
/*      */     }
/*      */ 
/* 1243 */     if ((getCompressionTypes() != null) && (getCompressionType() == null)) {
/* 1244 */       throw new IllegalStateException("No compression type set!");
/*      */     }
/* 1246 */     if ((paramFloat < 0.0F) || (paramFloat > 1.0F)) {
/* 1247 */       throw new IllegalArgumentException("Quality out-of-bounds!");
/*      */     }
/* 1249 */     this.compressionQuality = paramFloat;
/*      */   }
/*      */ 
/*      */   public float getCompressionQuality()
/*      */   {
/* 1279 */     if (!canWriteCompressed()) {
/* 1280 */       throw new UnsupportedOperationException("Compression not supported.");
/*      */     }
/*      */ 
/* 1283 */     if (getCompressionMode() != 2) {
/* 1284 */       throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
/*      */     }
/*      */ 
/* 1287 */     if ((getCompressionTypes() != null) && (getCompressionType() == null))
/*      */     {
/* 1289 */       throw new IllegalStateException("No compression type set!");
/*      */     }
/* 1291 */     return this.compressionQuality;
/*      */   }
/*      */ 
/*      */   public float getBitRate(float paramFloat)
/*      */   {
/* 1332 */     if (!canWriteCompressed()) {
/* 1333 */       throw new UnsupportedOperationException("Compression not supported.");
/*      */     }
/*      */ 
/* 1336 */     if (getCompressionMode() != 2) {
/* 1337 */       throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
/*      */     }
/*      */ 
/* 1340 */     if ((getCompressionTypes() != null) && (getCompressionType() == null))
/*      */     {
/* 1342 */       throw new IllegalStateException("No compression type set!");
/*      */     }
/* 1344 */     if ((paramFloat < 0.0F) || (paramFloat > 1.0F)) {
/* 1345 */       throw new IllegalArgumentException("Quality out-of-bounds!");
/*      */     }
/* 1347 */     return -1.0F;
/*      */   }
/*      */ 
/*      */   public String[] getCompressionQualityDescriptions()
/*      */   {
/* 1404 */     if (!canWriteCompressed()) {
/* 1405 */       throw new UnsupportedOperationException("Compression not supported.");
/*      */     }
/*      */ 
/* 1408 */     if (getCompressionMode() != 2) {
/* 1409 */       throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
/*      */     }
/*      */ 
/* 1412 */     if ((getCompressionTypes() != null) && (getCompressionType() == null))
/*      */     {
/* 1414 */       throw new IllegalStateException("No compression type set!");
/*      */     }
/* 1416 */     return null;
/*      */   }
/*      */ 
/*      */   public float[] getCompressionQualityValues()
/*      */   {
/* 1457 */     if (!canWriteCompressed()) {
/* 1458 */       throw new UnsupportedOperationException("Compression not supported.");
/*      */     }
/*      */ 
/* 1461 */     if (getCompressionMode() != 2) {
/* 1462 */       throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
/*      */     }
/*      */ 
/* 1465 */     if ((getCompressionTypes() != null) && (getCompressionType() == null))
/*      */     {
/* 1467 */       throw new IllegalStateException("No compression type set!");
/*      */     }
/* 1469 */     return null;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.ImageWriteParam
 * JD-Core Version:    0.6.2
 */