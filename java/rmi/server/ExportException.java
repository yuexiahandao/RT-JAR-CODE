/*    */ package java.rmi.server;
/*    */ 
/*    */ import java.rmi.RemoteException;
/*    */ 
/*    */ public class ExportException extends RemoteException
/*    */ {
/*    */   private static final long serialVersionUID = -9155485338494060170L;
/*    */ 
/*    */   public ExportException(String paramString)
/*    */   {
/* 53 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public ExportException(String paramString, Exception paramException)
/*    */   {
/* 65 */     super(paramString, paramException);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.server.ExportException
 * JD-Core Version:    0.6.2
 */