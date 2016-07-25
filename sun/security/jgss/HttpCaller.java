/*    */ package sun.security.jgss;
/*    */ 
/*    */ import sun.net.www.protocol.http.HttpCallerInfo;
/*    */ 
/*    */ public class HttpCaller extends GSSCaller
/*    */ {
/*    */   private final HttpCallerInfo hci;
/*    */ 
/*    */   public HttpCaller(HttpCallerInfo paramHttpCallerInfo)
/*    */   {
/* 38 */     this.hci = paramHttpCallerInfo;
/*    */   }
/*    */ 
/*    */   public HttpCallerInfo info() {
/* 42 */     return this.hci;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.HttpCaller
 * JD-Core Version:    0.6.2
 */