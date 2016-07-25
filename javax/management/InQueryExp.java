/*     */ package javax.management;
/*     */ 
/*     */ class InQueryExp extends QueryEval
/*     */   implements QueryExp
/*     */ {
/*     */   private static final long serialVersionUID = -5801329450358952434L;
/*     */   private ValueExp val;
/*     */   private ValueExp[] valueList;
/*     */ 
/*     */   public InQueryExp()
/*     */   {
/*     */   }
/*     */ 
/*     */   public InQueryExp(ValueExp paramValueExp, ValueExp[] paramArrayOfValueExp)
/*     */   {
/*  63 */     this.val = paramValueExp;
/*  64 */     this.valueList = paramArrayOfValueExp;
/*     */   }
/*     */ 
/*     */   public ValueExp getCheckedValue()
/*     */   {
/*  72 */     return this.val;
/*     */   }
/*     */ 
/*     */   public ValueExp[] getExplicitValues()
/*     */   {
/*  79 */     return this.valueList;
/*     */   }
/*     */ 
/*     */   public boolean apply(ObjectName paramObjectName)
/*     */     throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException
/*     */   {
/*  97 */     if (this.valueList != null) {
/*  98 */       ValueExp localValueExp1 = this.val.apply(paramObjectName);
/*  99 */       boolean bool = localValueExp1 instanceof NumericValueExp;
/*     */ 
/* 101 */       for (ValueExp localValueExp2 : this.valueList) {
/* 102 */         localValueExp2 = localValueExp2.apply(paramObjectName);
/* 103 */         if (bool) {
/* 104 */           if (((NumericValueExp)localValueExp2).doubleValue() == ((NumericValueExp)localValueExp1).doubleValue())
/*     */           {
/* 106 */             return true;
/*     */           }
/*     */         }
/* 109 */         else if (((StringValueExp)localValueExp2).getValue().equals(((StringValueExp)localValueExp1).getValue()))
/*     */         {
/* 111 */           return true;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 116 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 123 */     return this.val + " in (" + generateValueList() + ")";
/*     */   }
/*     */ 
/*     */   private String generateValueList()
/*     */   {
/* 128 */     if ((this.valueList == null) || (this.valueList.length == 0)) {
/* 129 */       return "";
/*     */     }
/*     */ 
/* 132 */     StringBuilder localStringBuilder = new StringBuilder(this.valueList[0].toString());
/*     */ 
/* 135 */     for (int i = 1; i < this.valueList.length; i++) {
/* 136 */       localStringBuilder.append(", ");
/* 137 */       localStringBuilder.append(this.valueList[i]);
/*     */     }
/*     */ 
/* 140 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.InQueryExp
 * JD-Core Version:    0.6.2
 */