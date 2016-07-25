/*    */ package sun.net.www.content.text;
/*    */ 
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import java.net.ContentHandler;
/*    */ import java.net.URLConnection;
/*    */ 
/*    */ public class plain extends ContentHandler
/*    */ {
/*    */   public Object getContent(URLConnection paramURLConnection)
/*    */   {
/*    */     try
/*    */     {
/* 42 */       InputStream localInputStream = paramURLConnection.getInputStream();
/* 43 */       return new PlainTextInputStream(paramURLConnection.getInputStream());
/*    */     } catch (IOException localIOException) {
/* 45 */       return "Error reading document:\n" + localIOException.toString();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.content.text.plain
 * JD-Core Version:    0.6.2
 */