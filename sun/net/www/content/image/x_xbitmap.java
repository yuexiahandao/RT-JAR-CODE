/*    */ package sun.net.www.content.image;
/*    */ 
/*    */ import java.awt.Image;
/*    */ import java.awt.Toolkit;
/*    */ import java.io.IOException;
/*    */ import java.net.ContentHandler;
/*    */ import java.net.URLConnection;
/*    */ import sun.awt.image.URLImageSource;
/*    */ 
/*    */ public class x_xbitmap extends ContentHandler
/*    */ {
/*    */   public Object getContent(URLConnection paramURLConnection)
/*    */     throws IOException
/*    */   {
/* 35 */     return new URLImageSource(paramURLConnection);
/*    */   }
/*    */ 
/*    */   public Object getContent(URLConnection paramURLConnection, Class[] paramArrayOfClass) throws IOException {
/* 39 */     for (int i = 0; i < paramArrayOfClass.length; i++) {
/* 40 */       if (paramArrayOfClass[i].isAssignableFrom(URLImageSource.class)) {
/* 41 */         return new URLImageSource(paramURLConnection);
/*    */       }
/* 43 */       if (paramArrayOfClass[i].isAssignableFrom(Image.class)) {
/* 44 */         Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 45 */         return localToolkit.createImage(new URLImageSource(paramURLConnection));
/*    */       }
/*    */     }
/* 48 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.content.image.x_xbitmap
 * JD-Core Version:    0.6.2
 */