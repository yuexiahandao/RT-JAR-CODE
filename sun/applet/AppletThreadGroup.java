/*    */ package sun.applet;
/*    */ 
/*    */ public class AppletThreadGroup extends ThreadGroup
/*    */ {
/*    */   public AppletThreadGroup(String paramString)
/*    */   {
/* 43 */     this(Thread.currentThread().getThreadGroup(), paramString);
/*    */   }
/*    */ 
/*    */   public AppletThreadGroup(ThreadGroup paramThreadGroup, String paramString)
/*    */   {
/* 61 */     super(paramThreadGroup, paramString);
/* 62 */     setMaxPriority(4);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.applet.AppletThreadGroup
 * JD-Core Version:    0.6.2
 */