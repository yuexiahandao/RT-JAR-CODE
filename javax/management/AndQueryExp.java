/*     */ package javax.management;
/*     */ 
/*     */ class AndQueryExp extends QueryEval
/*     */   implements QueryExp
/*     */ {
/*     */   private static final long serialVersionUID = -1081892073854801359L;
/*     */   private QueryExp exp1;
/*     */   private QueryExp exp2;
/*     */ 
/*     */   public AndQueryExp()
/*     */   {
/*     */   }
/*     */ 
/*     */   public AndQueryExp(QueryExp paramQueryExp1, QueryExp paramQueryExp2)
/*     */   {
/*  62 */     this.exp1 = paramQueryExp1;
/*  63 */     this.exp2 = paramQueryExp2;
/*     */   }
/*     */ 
/*     */   public QueryExp getLeftExp()
/*     */   {
/*  71 */     return this.exp1;
/*     */   }
/*     */ 
/*     */   public QueryExp getRightExp()
/*     */   {
/*  78 */     return this.exp2;
/*     */   }
/*     */ 
/*     */   public boolean apply(ObjectName paramObjectName)
/*     */     throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException
/*     */   {
/*  97 */     return (this.exp1.apply(paramObjectName)) && (this.exp2.apply(paramObjectName));
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 105 */     return "(" + this.exp1 + ") and (" + this.exp2 + ")";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.AndQueryExp
 * JD-Core Version:    0.6.2
 */