/*    */ package sun.awt.windows;
/*    */ 
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Image;
/*    */ import java.awt.Point;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.awt.image.DataBuffer;
/*    */ import java.awt.image.DataBufferInt;
/*    */ import java.awt.image.Raster;
/*    */ import java.awt.image.WritableRaster;
/*    */ import sun.awt.CustomCursor;
/*    */ import sun.awt.image.ImageRepresentation;
/*    */ import sun.awt.image.IntegerComponentRaster;
/*    */ import sun.awt.image.ToolkitImage;
/*    */ 
/*    */ public class WCustomCursor extends CustomCursor
/*    */ {
/*    */   public WCustomCursor(Image paramImage, Point paramPoint, String paramString)
/*    */     throws IndexOutOfBoundsException
/*    */   {
/* 45 */     super(paramImage, paramPoint, paramString);
/*    */   }
/*    */ 
/*    */   protected void createNativeCursor(Image paramImage, int[] paramArrayOfInt, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
/*    */   {
/* 50 */     BufferedImage localBufferedImage = new BufferedImage(paramInt1, paramInt2, 1);
/*    */ 
/* 52 */     Graphics localGraphics = localBufferedImage.getGraphics();
/*    */     try {
/* 54 */       if ((paramImage instanceof ToolkitImage)) {
/* 55 */         localObject1 = ((ToolkitImage)paramImage).getImageRep();
/* 56 */         ((ImageRepresentation)localObject1).reconstruct(32);
/*    */       }
/* 58 */       localGraphics.drawImage(paramImage, 0, 0, paramInt1, paramInt2, null);
/*    */     } finally {
/* 60 */       localGraphics.dispose();
/*    */     }
/* 62 */     Object localObject1 = localBufferedImage.getRaster();
/* 63 */     DataBuffer localDataBuffer = ((Raster)localObject1).getDataBuffer();
/*    */ 
/* 65 */     int[] arrayOfInt = ((DataBufferInt)localDataBuffer).getData();
/*    */ 
/* 67 */     byte[] arrayOfByte = new byte[paramInt1 * paramInt2 / 8];
/* 68 */     int i = paramArrayOfInt.length;
/* 69 */     for (int j = 0; j < i; j++) {
/* 70 */       int k = j / 8;
/* 71 */       int m = 1 << 7 - j % 8;
/* 72 */       if ((paramArrayOfInt[j] & 0xFF000000) == 0)
/*    */       {
/*    */         int tmp156_154 = k;
/*    */         byte[] tmp156_152 = arrayOfByte; tmp156_152[tmp156_154] = ((byte)(tmp156_152[tmp156_154] | m));
/*    */       }
/*    */ 
/*    */     }
/*    */ 
/* 79 */     j = ((Raster)localObject1).getWidth();
/* 80 */     if ((localObject1 instanceof IntegerComponentRaster)) {
/* 81 */       j = ((IntegerComponentRaster)localObject1).getScanlineStride();
/*    */     }
/* 83 */     createCursorIndirect(((DataBufferInt)localBufferedImage.getRaster().getDataBuffer()).getData(), arrayOfByte, j, ((Raster)localObject1).getWidth(), ((Raster)localObject1).getHeight(), paramInt3, paramInt4);
/*    */   }
/*    */ 
/*    */   private native void createCursorIndirect(int[] paramArrayOfInt, byte[] paramArrayOfByte, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5);
/*    */ 
/*    */   static native int getCursorWidth();
/*    */ 
/*    */   static native int getCursorHeight();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WCustomCursor
 * JD-Core Version:    0.6.2
 */