/*    */ package javax.net.ssl;
/*    */ 
/*    */ import java.util.EventObject;
/*    */ 
/*    */ public class SSLSessionBindingEvent extends EventObject
/*    */ {
/*    */   private static final long serialVersionUID = 3989172637106345L;
/*    */   private String name;
/*    */ 
/*    */   public SSLSessionBindingEvent(SSLSession paramSSLSession, String paramString)
/*    */   {
/* 68 */     super(paramSSLSession);
/* 69 */     this.name = paramString;
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 80 */     return this.name;
/*    */   }
/*    */ 
/*    */   public SSLSession getSession()
/*    */   {
/* 91 */     return (SSLSession)getSource();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.SSLSessionBindingEvent
 * JD-Core Version:    0.6.2
 */