/*    */ package sun.dc.path;
/*    */ 
/*    */ public class PathError extends RuntimeException
/*    */ {
/*    */   public static final String UNEX_beginPath = "beginPath: unexpected";
/*    */   public static final String UNEX_beginSubpath = "beginSubpath: unexpected";
/*    */   public static final String UNEX_appendLine = "appendLine: unexpected";
/*    */   public static final String UNEX_appendQuadratic = "appendQuadratic: unexpected";
/*    */   public static final String UNEX_appendCubic = "appendCubic: unexpected";
/*    */   public static final String UNEX_closedSubpath = "closedSubpath: unexpected";
/*    */   public static final String UNEX_endPath = "endPath: unexpected";
/*    */   public static final String UNEX_useProxy = "useProxy: unexpected";
/*    */   public static final String UNEX_getBox = "getBox: unexpected";
/*    */   public static final String UNEX_sendTo = "sendTo: unexpected";
/*    */   public static final String BAD_boxdest = "getBox: invalid box destination array";
/*    */   public static final String BAD_pathconsumer = "sendTo: invalid path consumer";
/*    */   public static final String INTERRUPTED = "";
/*    */   public static final String DUMMY = "";
/*    */ 
/*    */   public PathError()
/*    */   {
/*    */   }
/*    */ 
/*    */   public PathError(String paramString)
/*    */   {
/* 45 */     super(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.dc.path.PathError
 * JD-Core Version:    0.6.2
 */