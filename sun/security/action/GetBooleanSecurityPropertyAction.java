/*    */ package sun.security.action;
/*    */ 
/*    */ import java.security.PrivilegedAction;
/*    */ import java.security.Security;
/*    */ 
/*    */ public class GetBooleanSecurityPropertyAction
/*    */   implements PrivilegedAction<Boolean>
/*    */ {
/*    */   private String theProp;
/*    */ 
/*    */   public GetBooleanSecurityPropertyAction(String paramString)
/*    */   {
/* 57 */     this.theProp = paramString;
/*    */   }
/*    */ 
/*    */   public Boolean run()
/*    */   {
/* 67 */     boolean bool = false;
/*    */     try {
/* 69 */       String str = Security.getProperty(this.theProp);
/* 70 */       bool = (str != null) && (str.equalsIgnoreCase("true")); } catch (NullPointerException localNullPointerException) {
/*    */     }
/* 72 */     return Boolean.valueOf(bool);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.action.GetBooleanSecurityPropertyAction
 * JD-Core Version:    0.6.2
 */