/*    */ package javax.management.remote;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class JMXProviderException extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = -3166703627550447198L;
/* 82 */   private Throwable cause = null;
/*    */ 
/*    */   public JMXProviderException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public JMXProviderException(String paramString)
/*    */   {
/* 58 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public JMXProviderException(String paramString, Throwable paramThrowable)
/*    */   {
/* 69 */     super(paramString);
/* 70 */     this.cause = paramThrowable;
/*    */   }
/*    */ 
/*    */   public Throwable getCause() {
/* 74 */     return this.cause;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.JMXProviderException
 * JD-Core Version:    0.6.2
 */