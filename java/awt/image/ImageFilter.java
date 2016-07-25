/*     */ package java.awt.image;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ 
/*     */ public class ImageFilter
/*     */   implements ImageConsumer, Cloneable
/*     */ {
/*     */   protected ImageConsumer consumer;
/*     */ 
/*     */   public ImageFilter getFilterInstance(ImageConsumer paramImageConsumer)
/*     */   {
/*  73 */     ImageFilter localImageFilter = (ImageFilter)clone();
/*  74 */     localImageFilter.consumer = paramImageConsumer;
/*  75 */     return localImageFilter;
/*     */   }
/*     */ 
/*     */   public void setDimensions(int paramInt1, int paramInt2)
/*     */   {
/*  90 */     this.consumer.setDimensions(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void setProperties(Hashtable<?, ?> paramHashtable)
/*     */   {
/* 107 */     Hashtable localHashtable = (Hashtable)paramHashtable.clone();
/* 108 */     Object localObject = localHashtable.get("filters");
/* 109 */     if (localObject == null)
/* 110 */       localHashtable.put("filters", toString());
/* 111 */     else if ((localObject instanceof String)) {
/* 112 */       localHashtable.put("filters", (String)localObject + toString());
/*     */     }
/* 114 */     this.consumer.setProperties(localHashtable);
/*     */   }
/*     */ 
/*     */   public void setColorModel(ColorModel paramColorModel)
/*     */   {
/* 129 */     this.consumer.setColorModel(paramColorModel);
/*     */   }
/*     */ 
/*     */   public void setHints(int paramInt)
/*     */   {
/* 144 */     this.consumer.setHints(paramInt);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, byte[] paramArrayOfByte, int paramInt5, int paramInt6)
/*     */   {
/* 161 */     this.consumer.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfByte, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void setPixels(int paramInt1, int paramInt2, int paramInt3, int paramInt4, ColorModel paramColorModel, int[] paramArrayOfInt, int paramInt5, int paramInt6)
/*     */   {
/* 178 */     this.consumer.setPixels(paramInt1, paramInt2, paramInt3, paramInt4, paramColorModel, paramArrayOfInt, paramInt5, paramInt6);
/*     */   }
/*     */ 
/*     */   public void imageComplete(int paramInt)
/*     */   {
/* 193 */     this.consumer.imageComplete(paramInt);
/*     */   }
/*     */ 
/*     */   public void resendTopDownLeftRight(ImageProducer paramImageProducer)
/*     */   {
/* 244 */     paramImageProducer.requestTopDownLeftRightResend(this);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 252 */       return super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 255 */     throw new InternalError();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.image.ImageFilter
 * JD-Core Version:    0.6.2
 */