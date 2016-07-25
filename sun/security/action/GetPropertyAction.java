/*    */ package sun.security.action;
/*    */ 
/*    */ import java.security.PrivilegedAction;
/*    */ 
/*    */ public class GetPropertyAction
/*    */   implements PrivilegedAction<String>
/*    */ {
/*    */   private String theProp;
/*    */   private String defaultVal;
/*    */ 
/*    */   public GetPropertyAction(String paramString)
/*    */   {
/* 61 */     this.theProp = paramString;
/*    */   }
/*    */ 
/*    */   public GetPropertyAction(String paramString1, String paramString2)
/*    */   {
/* 72 */     this.theProp = paramString1;
/* 73 */     this.defaultVal = paramString2;
/*    */   }
/*    */ 
/*    */   public String run()
/*    */   {
/* 84 */     String str = System.getProperty(this.theProp);
/* 85 */     return str == null ? this.defaultVal : str;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.action.GetPropertyAction
 * JD-Core Version:    0.6.2
 */