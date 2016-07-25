/*     */ package javax.management;
/*     */ 
/*     */ class OrQueryExp extends QueryEval
/*     */   implements QueryExp
/*     */ {
/*     */   private static final long serialVersionUID = 2962973084421716523L;
/*     */   private QueryExp exp1;
/*     */   private QueryExp exp2;
/*     */ 
/*     */   public OrQueryExp()
/*     */   {
/*     */   }
/*     */ 
/*     */   public OrQueryExp(QueryExp paramQueryExp1, QueryExp paramQueryExp2)
/*     */   {
/*  63 */     this.exp1 = paramQueryExp1;
/*  64 */     this.exp2 = paramQueryExp2;
/*     */   }
/*     */ 
/*     */   public QueryExp getLeftExp()
/*     */   {
/*  72 */     return this.exp1;
/*     */   }
/*     */ 
/*     */   public QueryExp getRightExp()
/*     */   {
/*  79 */     return this.exp2;
/*     */   }
/*     */ 
/*     */   public boolean apply(ObjectName paramObjectName)
/*     */     throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException
/*     */   {
/*  97 */     return (this.exp1.apply(paramObjectName)) || (this.exp2.apply(paramObjectName));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 105 */     return "(" + this.exp1 + ") or (" + this.exp2 + ")";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.OrQueryExp
 * JD-Core Version:    0.6.2
 */