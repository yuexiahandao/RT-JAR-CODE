/*    */ package javax.xml.bind;
/*    */ 
/*    */ import java.security.PrivilegedAction;
/*    */ 
/*    */ final class GetPropertyAction
/*    */   implements PrivilegedAction<String>
/*    */ {
/*    */   private final String propertyName;
/*    */ 
/*    */   public GetPropertyAction(String propertyName)
/*    */   {
/* 38 */     this.propertyName = propertyName;
/*    */   }
/*    */ 
/*    */   public String run() {
/* 42 */     return System.getProperty(this.propertyName);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.bind.GetPropertyAction
 * JD-Core Version:    0.6.2
 */