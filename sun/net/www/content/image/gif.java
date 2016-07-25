/*    */ package sun.net.www.content.image;
/*    */ 
/*    */ import java.awt.Image;
/*    */ import java.awt.Toolkit;
/*    */ import java.io.IOException;
/*    */ import java.net.ContentHandler;
/*    */ import java.net.URLConnection;
/*    */ import sun.awt.image.URLImageSource;
/*    */ 
/*    */ public class gif extends ContentHandler
/*    */ {
/*    */   public Object getContent(URLConnection paramURLConnection)
/*    */     throws IOException
/*    */   {
/* 40 */     return new URLImageSource(paramURLConnection);
/*    */   }
/*    */ 
/*    */   public Object getContent(URLConnection paramURLConnection, Class[] paramArrayOfClass) throws IOException {
/* 44 */     for (int i = 0; i < paramArrayOfClass.length; i++) {
/* 45 */       if (paramArrayOfClass[i].isAssignableFrom(URLImageSource.class)) {
/* 46 */         return new URLImageSource(paramURLConnection);
/*    */       }
/* 48 */       if (paramArrayOfClass[i].isAssignableFrom(Image.class)) {
/* 49 */         Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 50 */         return localToolkit.createImage(new URLImageSource(paramURLConnection));
/*    */       }
/*    */     }
/* 53 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.content.image.gif
 * JD-Core Version:    0.6.2
 */