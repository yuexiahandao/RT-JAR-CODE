/*    */ package sun.awt.image;
/*    */ 
/*    */ import java.awt.image.BufferedImage;
/*    */ import sun.java2d.SurfaceData;
/*    */ 
/*    */ public class BufImgSurfaceManager extends SurfaceManager
/*    */ {
/*    */   protected BufferedImage bImg;
/*    */   protected SurfaceData sdDefault;
/*    */ 
/*    */   public BufImgSurfaceManager(BufferedImage paramBufferedImage)
/*    */   {
/* 54 */     this.bImg = paramBufferedImage;
/* 55 */     this.sdDefault = BufImgSurfaceData.createData(paramBufferedImage);
/*    */   }
/*    */ 
/*    */   public SurfaceData getPrimarySurfaceData() {
/* 59 */     return this.sdDefault;
/*    */   }
/*    */ 
/*    */   public SurfaceData restoreContents()
/*    */   {
/* 67 */     return this.sdDefault;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.BufImgSurfaceManager
 * JD-Core Version:    0.6.2
 */