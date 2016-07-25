/*    */ package javax.imageio.spi;
/*    */ 
/*    */ import javax.imageio.ImageTranscoder;
/*    */ 
/*    */ public abstract class ImageTranscoderSpi extends IIOServiceProvider
/*    */ {
/*    */   protected ImageTranscoderSpi()
/*    */   {
/*    */   }
/*    */ 
/*    */   public ImageTranscoderSpi(String paramString1, String paramString2)
/*    */   {
/* 59 */     super(paramString1, paramString2);
/*    */   }
/*    */ 
/*    */   public abstract String getReaderServiceProviderName();
/*    */ 
/*    */   public abstract String getWriterServiceProviderName();
/*    */ 
/*    */   public abstract ImageTranscoder createTranscoderInstance();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.spi.ImageTranscoderSpi
 * JD-Core Version:    0.6.2
 */