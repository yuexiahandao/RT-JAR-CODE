/*    */ package java.io;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.LinkedHashSet;
/*    */ import java.util.List;
/*    */ import sun.misc.JavaLangAccess;
/*    */ import sun.misc.SharedSecrets;
/*    */ 
/*    */ class DeleteOnExitHook
/*    */ {
/* 37 */   private static LinkedHashSet<String> files = new LinkedHashSet();
/*    */ 
/*    */   static synchronized void add(String paramString)
/*    */   {
/* 58 */     if (files == null)
/*    */     {
/* 60 */       throw new IllegalStateException("Shutdown in progress");
/*    */     }
/*    */ 
/* 63 */     files.add(paramString);
/*    */   }
/*    */ 
/*    */   static void runHooks()
/*    */   {
/*    */     LinkedHashSet localLinkedHashSet;
/* 69 */     synchronized (DeleteOnExitHook.class) {
/* 70 */       localLinkedHashSet = files;
/* 71 */       files = null;
/*    */     }
/*    */ 
/* 74 */     ??? = new ArrayList(localLinkedHashSet);
/*    */ 
/* 78 */     Collections.reverse((List)???);
/* 79 */     for (String str : (ArrayList)???)
/* 80 */       new File(str).delete();
/*    */   }
/*    */ 
/*    */   static
/*    */   {
/* 44 */     SharedSecrets.getJavaLangAccess().registerShutdownHook(2, true, new Runnable()
/*    */     {
/*    */       public void run()
/*    */       {
/* 49 */         DeleteOnExitHook.runHooks();
/*    */       }
/*    */     });
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.DeleteOnExitHook
 * JD-Core Version:    0.6.2
 */