/*    */ package javax.naming;
/*    */ 
/*    */ public class StringRefAddr extends RefAddr
/*    */ {
/*    */   private String contents;
/*    */   private static final long serialVersionUID = -8913762495138505527L;
/*    */ 
/*    */   public StringRefAddr(String paramString1, String paramString2)
/*    */   {
/* 69 */     super(paramString1);
/* 70 */     this.contents = paramString2;
/*    */   }
/*    */ 
/*    */   public Object getContent()
/*    */   {
/* 79 */     return this.contents;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.StringRefAddr
 * JD-Core Version:    0.6.2
 */