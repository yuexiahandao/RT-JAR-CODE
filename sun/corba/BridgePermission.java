/*    */ package sun.corba;
/*    */ 
/*    */ import java.security.BasicPermission;
/*    */ 
/*    */ public final class BridgePermission extends BasicPermission
/*    */ {
/*    */   public BridgePermission(String paramString)
/*    */   {
/* 46 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public BridgePermission(String paramString1, String paramString2)
/*    */   {
/* 61 */     super(paramString1, paramString2);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.corba.BridgePermission
 * JD-Core Version:    0.6.2
 */