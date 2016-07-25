/*    */ package sun.rmi.server;
/*    */ 
/*    */ import java.io.ObjectOutput;
/*    */ import java.rmi.server.RMIClientSocketFactory;
/*    */ import java.rmi.server.RMIServerSocketFactory;
/*    */ import java.rmi.server.RemoteRef;
/*    */ import sun.rmi.transport.LiveRef;
/*    */ 
/*    */ public class UnicastServerRef2 extends UnicastServerRef
/*    */ {
/*    */   private static final long serialVersionUID = -2289703812660767614L;
/*    */ 
/*    */   public UnicastServerRef2()
/*    */   {
/*    */   }
/*    */ 
/*    */   public UnicastServerRef2(LiveRef paramLiveRef)
/*    */   {
/* 58 */     super(paramLiveRef);
/*    */   }
/*    */ 
/*    */   public UnicastServerRef2(int paramInt, RMIClientSocketFactory paramRMIClientSocketFactory, RMIServerSocketFactory paramRMIServerSocketFactory)
/*    */   {
/* 69 */     super(new LiveRef(paramInt, paramRMIClientSocketFactory, paramRMIServerSocketFactory));
/*    */   }
/*    */ 
/*    */   public String getRefClass(ObjectOutput paramObjectOutput)
/*    */   {
/* 77 */     return "UnicastServerRef2";
/*    */   }
/*    */ 
/*    */   protected RemoteRef getClientRef()
/*    */   {
/* 87 */     return new UnicastRef2(this.ref);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.server.UnicastServerRef2
 * JD-Core Version:    0.6.2
 */