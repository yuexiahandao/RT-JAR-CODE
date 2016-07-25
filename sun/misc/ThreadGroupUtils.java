/*    */ package sun.misc;
/*    */ 
/*    */ public final class ThreadGroupUtils
/*    */ {
/*    */   public static ThreadGroup getRootThreadGroup()
/*    */   {
/* 47 */     Object localObject = Thread.currentThread().getThreadGroup();
/* 48 */     ThreadGroup localThreadGroup = ((ThreadGroup)localObject).getParent();
/* 49 */     while (localThreadGroup != null) {
/* 50 */       localObject = localThreadGroup;
/* 51 */       localThreadGroup = ((ThreadGroup)localObject).getParent();
/*    */     }
/* 53 */     return localObject;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.ThreadGroupUtils
 * JD-Core Version:    0.6.2
 */