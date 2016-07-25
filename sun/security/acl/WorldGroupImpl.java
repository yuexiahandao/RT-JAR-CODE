/*    */ package sun.security.acl;
/*    */ 
/*    */ import java.security.Principal;
/*    */ 
/*    */ public class WorldGroupImpl extends GroupImpl
/*    */ {
/*    */   public WorldGroupImpl(String paramString)
/*    */   {
/* 37 */     super(paramString);
/*    */   }
/*    */ 
/*    */   public boolean isMember(Principal paramPrincipal)
/*    */   {
/* 46 */     return true;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.acl.WorldGroupImpl
 * JD-Core Version:    0.6.2
 */