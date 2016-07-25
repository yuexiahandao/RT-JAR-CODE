/*    */ package java.rmi.activation;
/*    */ 
/*    */ import java.rmi.RemoteException;
/*    */ 
/*    */ public class ActivateFailedException extends RemoteException
/*    */ {
/*    */   private static final long serialVersionUID = 4863550261346652506L;
/*    */ 
/*    */   public ActivateFailedException(String paramString)
/*    */   {
/* 48 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public ActivateFailedException(String paramString, Exception paramException)
/*    */   {
/* 60 */     super(paramString, paramException);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.activation.ActivateFailedException
 * JD-Core Version:    0.6.2
 */