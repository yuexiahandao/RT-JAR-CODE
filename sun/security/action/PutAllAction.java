/*    */ package sun.security.action;
/*    */ 
/*    */ import java.security.PrivilegedAction;
/*    */ import java.security.Provider;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class PutAllAction
/*    */   implements PrivilegedAction<Void>
/*    */ {
/*    */   private final Provider provider;
/*    */   private final Map map;
/*    */ 
/*    */   public PutAllAction(Provider paramProvider, Map paramMap)
/*    */   {
/* 47 */     this.provider = paramProvider;
/* 48 */     this.map = paramMap;
/*    */   }
/*    */ 
/*    */   public Void run() {
/* 52 */     this.provider.putAll(this.map);
/* 53 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.action.PutAllAction
 * JD-Core Version:    0.6.2
 */