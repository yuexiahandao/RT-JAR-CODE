/*    */ package javax.net.ssl;
/*    */ 
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class SSLException extends IOException
/*    */ {
/*    */   private static final long serialVersionUID = 4511006460650708967L;
/*    */ 
/*    */   public SSLException(String paramString)
/*    */   {
/* 52 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public SSLException(String paramString, Throwable paramThrowable)
/*    */   {
/* 68 */     super(paramString);
/* 69 */     initCause(paramThrowable);
/*    */   }
/*    */ 
/*    */   public SSLException(Throwable paramThrowable)
/*    */   {
/* 85 */     super(paramThrowable == null ? null : paramThrowable.toString());
/* 86 */     initCause(paramThrowable);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.net.ssl.SSLException
 * JD-Core Version:    0.6.2
 */