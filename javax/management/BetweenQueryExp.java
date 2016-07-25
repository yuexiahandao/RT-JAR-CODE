/*     */ package javax.management;
/*     */ 
/*     */ class BetweenQueryExp extends QueryEval
/*     */   implements QueryExp
/*     */ {
/*     */   private static final long serialVersionUID = -2933597532866307444L;
/*     */   private ValueExp exp1;
/*     */   private ValueExp exp2;
/*     */   private ValueExp exp3;
/*     */ 
/*     */   public BetweenQueryExp()
/*     */   {
/*     */   }
/*     */ 
/*     */   public BetweenQueryExp(ValueExp paramValueExp1, ValueExp paramValueExp2, ValueExp paramValueExp3)
/*     */   {
/*  68 */     this.exp1 = paramValueExp1;
/*  69 */     this.exp2 = paramValueExp2;
/*  70 */     this.exp3 = paramValueExp3;
/*     */   }
/*     */ 
/*     */   public ValueExp getCheckedValue()
/*     */   {
/*  78 */     return this.exp1;
/*     */   }
/*     */ 
/*     */   public ValueExp getLowerBound()
/*     */   {
/*  85 */     return this.exp2;
/*     */   }
/*     */ 
/*     */   public ValueExp getUpperBound()
/*     */   {
/*  92 */     return this.exp3;
/*     */   }
/*     */ 
/*     */   public boolean apply(ObjectName paramObjectName)
/*     */     throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException
/*     */   {
/* 109 */     ValueExp localValueExp1 = this.exp1.apply(paramObjectName);
/* 110 */     ValueExp localValueExp2 = this.exp2.apply(paramObjectName);
/* 111 */     ValueExp localValueExp3 = this.exp3.apply(paramObjectName);
/* 112 */     boolean bool = localValueExp1 instanceof NumericValueExp;
/*     */ 
/* 114 */     if (bool) {
/* 115 */       if (((NumericValueExp)localValueExp1).isLong()) {
/* 116 */         long l1 = ((NumericValueExp)localValueExp1).longValue();
/* 117 */         long l2 = ((NumericValueExp)localValueExp2).longValue();
/* 118 */         long l3 = ((NumericValueExp)localValueExp3).longValue();
/* 119 */         return (l2 <= l1) && (l1 <= l3);
/*     */       }
/* 121 */       double d1 = ((NumericValueExp)localValueExp1).doubleValue();
/* 122 */       double d2 = ((NumericValueExp)localValueExp2).doubleValue();
/* 123 */       double d3 = ((NumericValueExp)localValueExp3).doubleValue();
/* 124 */       return (d2 <= d1) && (d1 <= d3);
/*     */     }
/*     */ 
/* 128 */     String str1 = ((StringValueExp)localValueExp1).getValue();
/* 129 */     String str2 = ((StringValueExp)localValueExp2).getValue();
/* 130 */     String str3 = ((StringValueExp)localValueExp3).getValue();
/* 131 */     return (str2.compareTo(str1) <= 0) && (str1.compareTo(str3) <= 0);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 140 */     return "(" + this.exp1 + ") between (" + this.exp2 + ") and (" + this.exp3 + ")";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.BetweenQueryExp
 * JD-Core Version:    0.6.2
 */