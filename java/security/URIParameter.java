/*    */ package java.security;
/*    */ 
/*    */ import java.net.URI;
/*    */ import javax.security.auth.login.Configuration.Parameters;
/*    */ 
/*    */ public class URIParameter
/*    */   implements Policy.Parameters, Configuration.Parameters
/*    */ {
/*    */   private URI uri;
/*    */ 
/*    */   public URIParameter(URI paramURI)
/*    */   {
/* 49 */     if (paramURI == null) {
/* 50 */       throw new NullPointerException("invalid null URI");
/*    */     }
/* 52 */     this.uri = paramURI;
/*    */   }
/*    */ 
/*    */   public URI getURI()
/*    */   {
/* 61 */     return this.uri;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.security.URIParameter
 * JD-Core Version:    0.6.2
 */