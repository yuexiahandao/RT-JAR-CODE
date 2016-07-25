/*    */ package javax.lang.model.element;
/*    */ 
/*    */ public enum NestingKind
/*    */ {
/* 85 */   TOP_LEVEL, 
/* 86 */   MEMBER, 
/* 87 */   LOCAL, 
/* 88 */   ANONYMOUS;
/*    */ 
/*    */   public boolean isNested()
/*    */   {
/* 97 */     return this != TOP_LEVEL;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.element.NestingKind
 * JD-Core Version:    0.6.2
 */