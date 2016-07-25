/*    */ package javax.management;
/*    */ 
/*    */ class NotQueryExp extends QueryEval
/*    */   implements QueryExp
/*    */ {
/*    */   private static final long serialVersionUID = 5269643775896723397L;
/*    */   private QueryExp exp;
/*    */ 
/*    */   public NotQueryExp()
/*    */   {
/*    */   }
/*    */ 
/*    */   public NotQueryExp(QueryExp paramQueryExp)
/*    */   {
/* 58 */     this.exp = paramQueryExp;
/*    */   }
/*    */ 
/*    */   public QueryExp getNegatedExp()
/*    */   {
/* 66 */     return this.exp;
/*    */   }
/*    */ 
/*    */   public boolean apply(ObjectName paramObjectName)
/*    */     throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException
/*    */   {
/* 83 */     return !this.exp.apply(paramObjectName);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 91 */     return "not (" + this.exp + ")";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.NotQueryExp
 * JD-Core Version:    0.6.2
 */