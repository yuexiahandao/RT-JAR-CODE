/*    */ package sun.dc.path;
/*    */ 
/*    */ public class PathException extends Exception
/*    */ {
/*    */   public static final String BAD_PATH_endPath = "endPath: bad path";
/*    */   public static final String BAD_PATH_useProxy = "useProxy: bad path";
/*    */   public static final String DUMMY = "";
/*    */ 
/*    */   public PathException()
/*    */   {
/*    */   }
/*    */ 
/*    */   public PathException(String paramString)
/*    */   {
/* 34 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.dc.path.PathException
 * JD-Core Version:    0.6.2
 */