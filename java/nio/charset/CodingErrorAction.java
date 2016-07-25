/*    */ package java.nio.charset;
/*    */ 
/*    */ public class CodingErrorAction
/*    */ {
/*    */   private String name;
/* 55 */   public static final CodingErrorAction IGNORE = new CodingErrorAction("IGNORE");
/*    */ 
/* 63 */   public static final CodingErrorAction REPLACE = new CodingErrorAction("REPLACE");
/*    */ 
/* 72 */   public static final CodingErrorAction REPORT = new CodingErrorAction("REPORT");
/*    */ 
/*    */   private CodingErrorAction(String paramString)
/*    */   {
/* 48 */     this.name = paramString;
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 81 */     return this.name;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.charset.CodingErrorAction
 * JD-Core Version:    0.6.2
 */