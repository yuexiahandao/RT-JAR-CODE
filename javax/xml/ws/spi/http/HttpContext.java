/*    */ package javax.xml.ws.spi.http;
/*    */ 
/*    */ import java.util.Set;
/*    */ 
/*    */ public abstract class HttpContext
/*    */ {
/*    */   protected HttpHandler handler;
/*    */ 
/*    */   public void setHandler(HttpHandler handler)
/*    */   {
/* 55 */     this.handler = handler;
/*    */   }
/*    */ 
/*    */   public abstract String getPath();
/*    */ 
/*    */   public abstract Object getAttribute(String paramString);
/*    */ 
/*    */   public abstract Set<String> getAttributeNames();
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.ws.spi.http.HttpContext
 * JD-Core Version:    0.6.2
 */