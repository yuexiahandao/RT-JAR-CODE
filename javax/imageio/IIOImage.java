/*     */ package javax.imageio;
/*     */ 
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.awt.image.Raster;
/*     */ import java.awt.image.RenderedImage;
/*     */ import java.util.List;
/*     */ import javax.imageio.metadata.IIOMetadata;
/*     */ 
/*     */ public class IIOImage
/*     */ {
/*     */   protected RenderedImage image;
/*     */   protected Raster raster;
/*  77 */   protected List<? extends BufferedImage> thumbnails = null;
/*     */   protected IIOMetadata metadata;
/*     */ 
/*     */   public IIOImage(RenderedImage paramRenderedImage, List<? extends BufferedImage> paramList, IIOMetadata paramIIOMetadata)
/*     */   {
/* 108 */     if (paramRenderedImage == null) {
/* 109 */       throw new IllegalArgumentException("image == null!");
/*     */     }
/* 111 */     this.image = paramRenderedImage;
/* 112 */     this.raster = null;
/* 113 */     this.thumbnails = paramList;
/* 114 */     this.metadata = paramIIOMetadata;
/*     */   }
/*     */ 
/*     */   public IIOImage(Raster paramRaster, List<? extends BufferedImage> paramList, IIOMetadata paramIIOMetadata)
/*     */   {
/* 136 */     if (paramRaster == null) {
/* 137 */       throw new IllegalArgumentException("raster == null!");
/*     */     }
/* 139 */     this.raster = paramRaster;
/* 140 */     this.image = null;
/* 141 */     this.thumbnails = paramList;
/* 142 */     this.metadata = paramIIOMetadata;
/*     */   }
/*     */ 
/*     */   public RenderedImage getRenderedImage()
/*     */   {
/* 154 */     synchronized (this) {
/* 155 */       return this.image;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setRenderedImage(RenderedImage paramRenderedImage)
/*     */   {
/* 172 */     synchronized (this) {
/* 173 */       if (paramRenderedImage == null) {
/* 174 */         throw new IllegalArgumentException("image == null!");
/*     */       }
/* 176 */       this.image = paramRenderedImage;
/* 177 */       this.raster = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean hasRaster()
/*     */   {
/* 189 */     synchronized (this) {
/* 190 */       return this.raster != null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Raster getRaster()
/*     */   {
/* 204 */     synchronized (this) {
/* 205 */       return this.raster;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setRaster(Raster paramRaster)
/*     */   {
/* 222 */     synchronized (this) {
/* 223 */       if (paramRaster == null) {
/* 224 */         throw new IllegalArgumentException("raster == null!");
/*     */       }
/* 226 */       this.raster = paramRaster;
/* 227 */       this.image = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getNumThumbnails()
/*     */   {
/* 238 */     return this.thumbnails == null ? 0 : this.thumbnails.size();
/*     */   }
/*     */ 
/*     */   public BufferedImage getThumbnail(int paramInt)
/*     */   {
/* 258 */     if (this.thumbnails == null) {
/* 259 */       throw new IndexOutOfBoundsException("No thumbnails available!");
/*     */     }
/* 261 */     return (BufferedImage)this.thumbnails.get(paramInt);
/*     */   }
/*     */ 
/*     */   public List<? extends BufferedImage> getThumbnails()
/*     */   {
/* 276 */     return this.thumbnails;
/*     */   }
/*     */ 
/*     */   public void setThumbnails(List<? extends BufferedImage> paramList)
/*     */   {
/* 295 */     this.thumbnails = paramList;
/*     */   }
/*     */ 
/*     */   public IIOMetadata getMetadata()
/*     */   {
/* 307 */     return this.metadata;
/*     */   }
/*     */ 
/*     */   public void setMetadata(IIOMetadata paramIIOMetadata)
/*     */   {
/* 320 */     this.metadata = paramIIOMetadata;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.IIOImage
 * JD-Core Version:    0.6.2
 */