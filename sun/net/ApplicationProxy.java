/*    */ package sun.net;
/*    */ 
/*    */ import java.net.Proxy;
/*    */ 
/*    */ public final class ApplicationProxy extends Proxy
/*    */ {
/*    */   private ApplicationProxy(Proxy paramProxy)
/*    */   {
/* 37 */     super(paramProxy.type(), paramProxy.address());
/*    */   }
/*    */ 
/*    */   public static ApplicationProxy create(Proxy paramProxy) {
/* 41 */     return new ApplicationProxy(paramProxy);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.ApplicationProxy
 * JD-Core Version:    0.6.2
 */