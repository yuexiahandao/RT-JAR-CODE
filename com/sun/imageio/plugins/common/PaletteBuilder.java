/*     */ package com.sun.imageio.plugins.common;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.ColorModel;
/*     */ import java.awt.image.IndexColorModel;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.RenderedImage;
/*     */ import java.awt.image.WritableRaster;
/*     */ import javax.imageio.ImageTypeSpecifier;
/*     */ 
/*     */ public class PaletteBuilder
/*     */ {
/*     */   protected static final int MAXLEVEL = 8;
/*     */   protected RenderedImage src;
/*     */   protected ColorModel srcColorModel;
/*     */   protected Raster srcRaster;
/*     */   protected int requiredSize;
/*     */   protected ColorNode root;
/*     */   protected int numNodes;
/*     */   protected int maxNodes;
/*     */   protected int currLevel;
/*     */   protected int currSize;
/*     */   protected ColorNode[] reduceList;
/*     */   protected ColorNode[] palette;
/*     */   protected int transparency;
/*     */   protected ColorNode transColor;
/*     */ 
/*     */   public static RenderedImage createIndexedImage(RenderedImage paramRenderedImage)
/*     */   {
/*  93 */     PaletteBuilder localPaletteBuilder = new PaletteBuilder(paramRenderedImage);
/*  94 */     localPaletteBuilder.buildPalette();
/*  95 */     return localPaletteBuilder.getIndexedImage();
/*     */   }
/*     */ 
/*     */   public static IndexColorModel createIndexColorModel(RenderedImage paramRenderedImage)
/*     */   {
/* 116 */     PaletteBuilder localPaletteBuilder = new PaletteBuilder(paramRenderedImage);
/* 117 */     localPaletteBuilder.buildPalette();
/* 118 */     return localPaletteBuilder.getIndexColorModel();
/*     */   }
/*     */ 
/*     */   public static boolean canCreatePalette(ImageTypeSpecifier paramImageTypeSpecifier)
/*     */   {
/* 135 */     if (paramImageTypeSpecifier == null) {
/* 136 */       throw new IllegalArgumentException("type == null");
/*     */     }
/* 138 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean canCreatePalette(RenderedImage paramRenderedImage)
/*     */   {
/* 155 */     if (paramRenderedImage == null) {
/* 156 */       throw new IllegalArgumentException("image == null");
/*     */     }
/* 158 */     ImageTypeSpecifier localImageTypeSpecifier = new ImageTypeSpecifier(paramRenderedImage);
/* 159 */     return canCreatePalette(localImageTypeSpecifier);
/*     */   }
/*     */ 
/*     */   protected RenderedImage getIndexedImage() {
/* 163 */     IndexColorModel localIndexColorModel = getIndexColorModel();
/*     */ 
/* 165 */     BufferedImage localBufferedImage = new BufferedImage(this.src.getWidth(), this.src.getHeight(), 13, localIndexColorModel);
/*     */ 
/* 169 */     WritableRaster localWritableRaster = localBufferedImage.getRaster();
/* 170 */     for (int i = 0; i < localBufferedImage.getHeight(); i++) {
/* 171 */       for (int j = 0; j < localBufferedImage.getWidth(); j++) {
/* 172 */         Color localColor = getSrcColor(j, i);
/* 173 */         localWritableRaster.setSample(j, i, 0, findColorIndex(this.root, localColor));
/*     */       }
/*     */     }
/*     */ 
/* 177 */     return localBufferedImage;
/*     */   }
/*     */ 
/*     */   protected PaletteBuilder(RenderedImage paramRenderedImage)
/*     */   {
/* 182 */     this(paramRenderedImage, 256);
/*     */   }
/*     */ 
/*     */   protected PaletteBuilder(RenderedImage paramRenderedImage, int paramInt) {
/* 186 */     this.src = paramRenderedImage;
/* 187 */     this.srcColorModel = paramRenderedImage.getColorModel();
/* 188 */     this.srcRaster = paramRenderedImage.getData();
/*     */ 
/* 190 */     this.transparency = this.srcColorModel.getTransparency();
/*     */ 
/* 193 */     this.requiredSize = paramInt;
/*     */   }
/*     */ 
/*     */   private Color getSrcColor(int paramInt1, int paramInt2) {
/* 197 */     int i = this.srcColorModel.getRGB(this.srcRaster.getDataElements(paramInt1, paramInt2, null));
/* 198 */     return new Color(i, this.transparency != 1);
/*     */   }
/*     */ 
/*     */   protected int findColorIndex(ColorNode paramColorNode, Color paramColor) {
/* 202 */     if ((this.transparency != 1) && (paramColor.getAlpha() != 255))
/*     */     {
/* 205 */       return 0;
/*     */     }
/*     */ 
/* 208 */     if (paramColorNode.isLeaf) {
/* 209 */       return paramColorNode.paletteIndex;
/*     */     }
/* 211 */     int i = getBranchIndex(paramColor, paramColorNode.level);
/*     */ 
/* 213 */     return findColorIndex(paramColorNode.children[i], paramColor);
/*     */   }
/*     */ 
/*     */   protected void buildPalette()
/*     */   {
/* 218 */     this.reduceList = new ColorNode[9];
/* 219 */     for (int i = 0; i < this.reduceList.length; i++) {
/* 220 */       this.reduceList[i] = null;
/*     */     }
/*     */ 
/* 223 */     this.numNodes = 0;
/* 224 */     this.maxNodes = 0;
/* 225 */     this.root = null;
/* 226 */     this.currSize = 0;
/* 227 */     this.currLevel = 8;
/*     */ 
/* 234 */     i = this.src.getWidth();
/* 235 */     int j = this.src.getHeight();
/* 236 */     for (int k = 0; k < j; k++)
/* 237 */       for (int m = 0; m < i; m++)
/*     */       {
/* 239 */         Color localColor = getSrcColor(i - m - 1, j - k - 1);
/*     */ 
/* 244 */         if ((this.transparency != 1) && (localColor.getAlpha() != 255))
/*     */         {
/* 247 */           if (this.transColor == null) {
/* 248 */             this.requiredSize -= 1;
/*     */ 
/* 250 */             this.transColor = new ColorNode();
/* 251 */             this.transColor.isLeaf = true;
/*     */           }
/* 253 */           this.transColor = insertNode(this.transColor, localColor, 0);
/*     */         } else {
/* 255 */           this.root = insertNode(this.root, localColor, 0);
/*     */         }
/* 257 */         if (this.currSize > this.requiredSize)
/* 258 */           reduceTree();
/*     */       }
/*     */   }
/*     */ 
/*     */   protected ColorNode insertNode(ColorNode paramColorNode, Color paramColor, int paramInt)
/*     */   {
/* 266 */     if (paramColorNode == null) {
/* 267 */       paramColorNode = new ColorNode();
/* 268 */       this.numNodes += 1;
/* 269 */       if (this.numNodes > this.maxNodes) {
/* 270 */         this.maxNodes = this.numNodes;
/*     */       }
/* 272 */       paramColorNode.level = paramInt;
/* 273 */       paramColorNode.isLeaf = (paramInt > 8);
/* 274 */       if (paramColorNode.isLeaf) {
/* 275 */         this.currSize += 1;
/*     */       }
/*     */     }
/* 278 */     paramColorNode.colorCount += 1;
/* 279 */     paramColorNode.red += paramColor.getRed();
/* 280 */     paramColorNode.green += paramColor.getGreen();
/* 281 */     paramColorNode.blue += paramColor.getBlue();
/*     */ 
/* 283 */     if (!paramColorNode.isLeaf) {
/* 284 */       int i = getBranchIndex(paramColor, paramInt);
/* 285 */       if (paramColorNode.children[i] == null) {
/* 286 */         paramColorNode.childCount += 1;
/* 287 */         if (paramColorNode.childCount == 2) {
/* 288 */           paramColorNode.nextReducible = this.reduceList[paramInt];
/* 289 */           this.reduceList[paramInt] = paramColorNode;
/*     */         }
/*     */       }
/* 292 */       paramColorNode.children[i] = insertNode(paramColorNode.children[i], paramColor, paramInt + 1);
/*     */     }
/*     */ 
/* 295 */     return paramColorNode;
/*     */   }
/*     */ 
/*     */   protected IndexColorModel getIndexColorModel() {
/* 299 */     int i = this.currSize;
/* 300 */     if (this.transColor != null) {
/* 301 */       i++;
/*     */     }
/*     */ 
/* 304 */     byte[] arrayOfByte1 = new byte[i];
/* 305 */     byte[] arrayOfByte2 = new byte[i];
/* 306 */     byte[] arrayOfByte3 = new byte[i];
/*     */ 
/* 308 */     int j = 0;
/* 309 */     this.palette = new ColorNode[i];
/* 310 */     if (this.transColor != null) {
/* 311 */       j++;
/*     */     }
/*     */ 
/* 314 */     if (this.root != null) {
/* 315 */       findPaletteEntry(this.root, j, arrayOfByte1, arrayOfByte2, arrayOfByte3);
/*     */     }
/*     */ 
/* 318 */     IndexColorModel localIndexColorModel = null;
/* 319 */     if (this.transColor != null)
/* 320 */       localIndexColorModel = new IndexColorModel(8, i, arrayOfByte1, arrayOfByte2, arrayOfByte3, 0);
/*     */     else {
/* 322 */       localIndexColorModel = new IndexColorModel(8, this.currSize, arrayOfByte1, arrayOfByte2, arrayOfByte3);
/*     */     }
/* 324 */     return localIndexColorModel;
/*     */   }
/*     */ 
/*     */   protected int findPaletteEntry(ColorNode paramColorNode, int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, byte[] paramArrayOfByte3)
/*     */   {
/* 330 */     if (paramColorNode.isLeaf) {
/* 331 */       paramArrayOfByte1[paramInt] = ((byte)(int)(paramColorNode.red / paramColorNode.colorCount));
/* 332 */       paramArrayOfByte2[paramInt] = ((byte)(int)(paramColorNode.green / paramColorNode.colorCount));
/* 333 */       paramArrayOfByte3[paramInt] = ((byte)(int)(paramColorNode.blue / paramColorNode.colorCount));
/* 334 */       paramColorNode.paletteIndex = paramInt;
/*     */ 
/* 336 */       this.palette[paramInt] = paramColorNode;
/*     */ 
/* 338 */       paramInt++;
/*     */     } else {
/* 340 */       for (int i = 0; i < 8; i++) {
/* 341 */         if (paramColorNode.children[i] != null) {
/* 342 */           paramInt = findPaletteEntry(paramColorNode.children[i], paramInt, paramArrayOfByte1, paramArrayOfByte2, paramArrayOfByte3);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 347 */     return paramInt;
/*     */   }
/*     */ 
/*     */   protected int getBranchIndex(Color paramColor, int paramInt) {
/* 351 */     if ((paramInt > 8) || (paramInt < 0)) {
/* 352 */       throw new IllegalArgumentException("Invalid octree node depth: " + paramInt);
/*     */     }
/*     */ 
/* 356 */     int i = 8 - paramInt;
/* 357 */     int j = 0x1 & (0xFF & paramColor.getRed()) >> i;
/* 358 */     int k = 0x1 & (0xFF & paramColor.getGreen()) >> i;
/* 359 */     int m = 0x1 & (0xFF & paramColor.getBlue()) >> i;
/* 360 */     int n = j << 2 | k << 1 | m;
/* 361 */     return n;
/*     */   }
/*     */ 
/*     */   protected void reduceTree() {
/* 365 */     int i = this.reduceList.length - 1;
/* 366 */     while ((this.reduceList[i] == null) && (i >= 0)) {
/* 367 */       i--;
/*     */     }
/*     */ 
/* 370 */     Object localObject1 = this.reduceList[i];
/* 371 */     if (localObject1 == null)
/*     */     {
/* 373 */       return;
/*     */     }
/*     */ 
/* 377 */     Object localObject2 = localObject1;
/* 378 */     int j = ((ColorNode)localObject2).colorCount;
/*     */ 
/* 380 */     int k = 1;
/* 381 */     while (((ColorNode)localObject2).nextReducible != null) {
/* 382 */       if (j > ((ColorNode)localObject2).nextReducible.colorCount) {
/* 383 */         localObject1 = localObject2;
/* 384 */         j = ((ColorNode)localObject2).colorCount;
/*     */       }
/* 386 */       localObject2 = ((ColorNode)localObject2).nextReducible;
/* 387 */       k++;
/*     */     }
/*     */ 
/* 392 */     if (localObject1 == this.reduceList[i]) {
/* 393 */       this.reduceList[i] = ((ColorNode)localObject1).nextReducible;
/*     */     } else {
/* 395 */       localObject2 = ((ColorNode)localObject1).nextReducible;
/* 396 */       ((ColorNode)localObject1).nextReducible = ((ColorNode)localObject2).nextReducible;
/* 397 */       localObject1 = localObject2;
/*     */     }
/*     */ 
/* 400 */     if (((ColorNode)localObject1).isLeaf) {
/* 401 */       return;
/*     */     }
/*     */ 
/* 405 */     int m = ((ColorNode)localObject1).getLeafChildCount();
/* 406 */     ((ColorNode)localObject1).isLeaf = true;
/* 407 */     this.currSize -= m - 1;
/* 408 */     int n = ((ColorNode)localObject1).level;
/* 409 */     for (int i1 = 0; i1 < 8; i1++) {
/* 410 */       ((ColorNode)localObject1).children[i1] = freeTree(localObject1.children[i1]);
/*     */     }
/* 412 */     ((ColorNode)localObject1).childCount = 0;
/*     */   }
/*     */ 
/*     */   protected ColorNode freeTree(ColorNode paramColorNode) {
/* 416 */     if (paramColorNode == null) {
/* 417 */       return null;
/*     */     }
/* 419 */     for (int i = 0; i < 8; i++) {
/* 420 */       paramColorNode.children[i] = freeTree(paramColorNode.children[i]);
/*     */     }
/*     */ 
/* 423 */     this.numNodes -= 1;
/* 424 */     return null;
/*     */   }
/*     */   protected class ColorNode { public boolean isLeaf;
/*     */     public int childCount;
/*     */     ColorNode[] children;
/*     */     public int colorCount;
/*     */     public long red;
/*     */     public long blue;
/*     */     public long green;
/*     */     public int paletteIndex;
/*     */     public int level;
/*     */     ColorNode nextReducible;
/*     */ 
/* 446 */     public ColorNode() { this.isLeaf = false;
/* 447 */       this.level = 0;
/* 448 */       this.childCount = 0;
/* 449 */       this.children = new ColorNode[8];
/* 450 */       for (int i = 0; i < 8; i++) {
/* 451 */         this.children[i] = null;
/*     */       }
/*     */ 
/* 454 */       this.colorCount = 0;
/* 455 */       this.red = (this.green = this.blue = 0L);
/*     */ 
/* 457 */       this.paletteIndex = 0; }
/*     */ 
/*     */     public int getLeafChildCount()
/*     */     {
/* 461 */       if (this.isLeaf) {
/* 462 */         return 0;
/*     */       }
/* 464 */       int i = 0;
/* 465 */       for (int j = 0; j < this.children.length; j++) {
/* 466 */         if (this.children[j] != null) {
/* 467 */           if (this.children[j].isLeaf)
/* 468 */             i++;
/*     */           else {
/* 470 */             i += this.children[j].getLeafChildCount();
/*     */           }
/*     */         }
/*     */       }
/* 474 */       return i;
/*     */     }
/*     */ 
/*     */     public int getRGB() {
/* 478 */       int i = (int)this.red / this.colorCount;
/* 479 */       int j = (int)this.green / this.colorCount;
/* 480 */       int k = (int)this.blue / this.colorCount;
/*     */ 
/* 482 */       int m = 0xFF000000 | (0xFF & i) << 16 | (0xFF & j) << 8 | 0xFF & k;
/* 483 */       return m;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.common.PaletteBuilder
 * JD-Core Version:    0.6.2
 */