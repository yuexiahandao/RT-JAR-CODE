/*    */ package javax.management;
/*    */ 
/*    */ public class MBeanRegistrationException extends MBeanException
/*    */ {
/*    */   private static final long serialVersionUID = 4482382455277067805L;
/*    */ 
/*    */   public MBeanRegistrationException(Exception paramException)
/*    */   {
/* 47 */     super(paramException);
/*    */   }
/*    */ 
/*    */   public MBeanRegistrationException(Exception paramException, String paramString)
/*    */   {
/* 59 */     super(paramException, paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.MBeanRegistrationException
 * JD-Core Version:    0.6.2
 */