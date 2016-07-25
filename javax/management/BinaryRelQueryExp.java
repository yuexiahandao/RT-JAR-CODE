/*     */ package javax.management;
/*     */ 
/*     */ class BinaryRelQueryExp extends QueryEval
/*     */   implements QueryExp
/*     */ {
/*     */   private static final long serialVersionUID = -5690656271650491000L;
/*     */   private int relOp;
/*     */   private ValueExp exp1;
/*     */   private ValueExp exp2;
/*     */ 
/*     */   public BinaryRelQueryExp()
/*     */   {
/*     */   }
/*     */ 
/*     */   public BinaryRelQueryExp(int paramInt, ValueExp paramValueExp1, ValueExp paramValueExp2)
/*     */   {
/*  68 */     this.relOp = paramInt;
/*  69 */     this.exp1 = paramValueExp1;
/*  70 */     this.exp2 = paramValueExp2;
/*     */   }
/*     */ 
/*     */   public int getOperator()
/*     */   {
/*  78 */     return this.relOp;
/*     */   }
/*     */ 
/*     */   public ValueExp getLeftValue()
/*     */   {
/*  85 */     return this.exp1;
/*     */   }
/*     */ 
/*     */   public ValueExp getRightValue()
/*     */   {
/*  92 */     return this.exp2;
/*     */   }
/*     */ 
/*     */   public boolean apply(ObjectName paramObjectName)
/*     */     throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException
/*     */   {
/* 109 */     ValueExp localValueExp1 = this.exp1.apply(paramObjectName);
/* 110 */     ValueExp localValueExp2 = this.exp2.apply(paramObjectName);
/* 111 */     boolean bool1 = localValueExp1 instanceof NumericValueExp;
/* 112 */     boolean bool2 = localValueExp1 instanceof BooleanValueExp;
/* 113 */     if (bool1) {
/* 114 */       if (((NumericValueExp)localValueExp1).isLong()) {
/* 115 */         long l1 = ((NumericValueExp)localValueExp1).longValue();
/* 116 */         long l2 = ((NumericValueExp)localValueExp2).longValue();
/*     */ 
/* 118 */         switch (this.relOp) {
/*     */         case 0:
/* 120 */           return l1 > l2;
/*     */         case 1:
/* 122 */           return l1 < l2;
/*     */         case 2:
/* 124 */           return l1 >= l2;
/*     */         case 3:
/* 126 */           return l1 <= l2;
/*     */         case 4:
/* 128 */           return l1 == l2;
/*     */         }
/*     */       } else {
/* 131 */         double d1 = ((NumericValueExp)localValueExp1).doubleValue();
/* 132 */         double d2 = ((NumericValueExp)localValueExp2).doubleValue();
/*     */ 
/* 134 */         switch (this.relOp) {
/*     */         case 0:
/* 136 */           return d1 > d2;
/*     */         case 1:
/* 138 */           return d1 < d2;
/*     */         case 2:
/* 140 */           return d1 >= d2;
/*     */         case 3:
/* 142 */           return d1 <= d2;
/*     */         case 4:
/* 144 */           return d1 == d2;
/*     */         }
/*     */       }
/*     */     }
/* 148 */     else if (bool2)
/*     */     {
/* 150 */       boolean bool3 = ((BooleanValueExp)localValueExp1).getValue().booleanValue();
/* 151 */       boolean bool4 = ((BooleanValueExp)localValueExp2).getValue().booleanValue();
/*     */ 
/* 153 */       switch (this.relOp) {
/*     */       case 0:
/* 155 */         return (bool3) && (!bool4);
/*     */       case 1:
/* 157 */         return (!bool3) && (bool4);
/*     */       case 2:
/* 159 */         return (bool3) || (!bool4);
/*     */       case 3:
/* 161 */         return (!bool3) || (bool4);
/*     */       case 4:
/* 163 */         return bool3 == bool4;
/*     */       }
/*     */     }
/*     */     else {
/* 167 */       String str1 = ((StringValueExp)localValueExp1).getValue();
/* 168 */       String str2 = ((StringValueExp)localValueExp2).getValue();
/*     */ 
/* 170 */       switch (this.relOp) {
/*     */       case 0:
/* 172 */         return str1.compareTo(str2) > 0;
/*     */       case 1:
/* 174 */         return str1.compareTo(str2) < 0;
/*     */       case 2:
/* 176 */         return str1.compareTo(str2) >= 0;
/*     */       case 3:
/* 178 */         return str1.compareTo(str2) <= 0;
/*     */       case 4:
/* 180 */         return str1.compareTo(str2) == 0;
/*     */       }
/*     */     }
/*     */ 
/* 184 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 192 */     return "(" + this.exp1 + ") " + relOpString() + " (" + this.exp2 + ")";
/*     */   }
/*     */ 
/*     */   private String relOpString() {
/* 196 */     switch (this.relOp) {
/*     */     case 0:
/* 198 */       return ">";
/*     */     case 1:
/* 200 */       return "<";
/*     */     case 2:
/* 202 */       return ">=";
/*     */     case 3:
/* 204 */       return "<=";
/*     */     case 4:
/* 206 */       return "=";
/*     */     }
/*     */ 
/* 209 */     return "=";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.BinaryRelQueryExp
 * JD-Core Version:    0.6.2
 */