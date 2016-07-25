/*    */ package sun.misc;
/*    */ 
/*    */ import java.io.File;
/*    */ import sun.usagetracker.UsageTrackerClient;
/*    */ 
/*    */ public class PostVMInitHook
/*    */ {
/*    */   public static void run()
/*    */   {
/* 22 */     trackJavaUsage();
/*    */   }
/*    */ 
/*    */   private static void trackJavaUsage()
/*    */   {
/* 32 */     String str1 = System.getProperty("java.home") + File.separator + "lib" + File.separator + "management" + File.separator + "usagetracker.properties";
/*    */ 
/* 37 */     String str2 = System.getProperty("com.oracle.usagetracker.config.file", str1);
/*    */ 
/* 42 */     if (new File(str2).exists())
/*    */     {
/* 46 */       UsageTrackerClient localUsageTrackerClient = new UsageTrackerClient();
/* 47 */       localUsageTrackerClient.run("VM start", System.getProperty("sun.java.command"));
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.PostVMInitHook
 * JD-Core Version:    0.6.2
 */