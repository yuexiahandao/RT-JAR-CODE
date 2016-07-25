/*    */ package sun.misc;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FilenameFilter;
/*    */ 
/*    */ public class JarFilter
/*    */   implements FilenameFilter
/*    */ {
/*    */   public boolean accept(File paramFile, String paramString)
/*    */   {
/* 42 */     String str = paramString.toLowerCase();
/* 43 */     return (str.endsWith(".jar")) || (str.endsWith(".zip"));
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.JarFilter
 * JD-Core Version:    0.6.2
 */