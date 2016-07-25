/*    */ package java.rmi.server;
/*    */ 
/*    */ import java.rmi.RemoteException;
/*    */ 
/*    */ @Deprecated
/*    */ public class SkeletonNotFoundException extends RemoteException
/*    */ {
/*    */   private static final long serialVersionUID = -7860299673822761231L;
/*    */ 
/*    */   public SkeletonNotFoundException(String paramString)
/*    */   {
/* 54 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public SkeletonNotFoundException(String paramString, Exception paramException)
/*    */   {
/* 66 */     super(paramString, paramException);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.server.SkeletonNotFoundException
 * JD-Core Version:    0.6.2
 */