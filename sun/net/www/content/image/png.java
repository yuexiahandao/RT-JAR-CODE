/*    */ package sun.net.www.content.image;
/*    */ 
/*    */ import java.awt.Image;
/*    */ import java.awt.Toolkit;
/*    */ import java.io.IOException;
/*    */ import java.net.ContentHandler;
/*    */ import java.net.URLConnection;
/*    */ import sun.awt.image.URLImageSource;
/*    */ 
/*    */ public class png extends ContentHandler
/*    */ {
/*    */   public Object getContent(URLConnection paramURLConnection)
/*    */     throws IOException
/*    */   {
/* 39 */     return new URLImageSource(paramURLConnection);
/*    */   }
/*    */ 
/*    */   public Object getContent(URLConnection paramURLConnection, Class[] paramArrayOfClass) throws IOException {
/* 43 */     for (int i = 0; i < paramArrayOfClass.length; i++) {
/* 44 */       if (paramArrayOfClass[i].isAssignableFrom(URLImageSource.class)) {
/* 45 */         return new URLImageSource(paramURLConnection);
/*    */       }
/* 47 */       if (paramArrayOfClass[i].isAssignableFrom(Image.class)) {
/* 48 */         Toolkit localToolkit = Toolkit.getDefaultToolkit();
/* 49 */         return localToolkit.createImage(new URLImageSource(paramURLConnection));
/*    */       }
/*    */     }
/* 52 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.content.image.png
 * JD-Core Version:    0.6.2
 */