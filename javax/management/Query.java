/*     */ package javax.management;
/*     */ 
/*     */ public class Query
/*     */ {
/*     */   public static final int GT = 0;
/*     */   public static final int LT = 1;
/*     */   public static final int GE = 2;
/*     */   public static final int LE = 3;
/*     */   public static final int EQ = 4;
/*     */   public static final int PLUS = 0;
/*     */   public static final int MINUS = 1;
/*     */   public static final int TIMES = 2;
/*     */   public static final int DIV = 3;
/*     */ 
/*     */   public static QueryExp and(QueryExp paramQueryExp1, QueryExp paramQueryExp2)
/*     */   {
/* 135 */     return new AndQueryExp(paramQueryExp1, paramQueryExp2);
/*     */   }
/*     */ 
/*     */   public static QueryExp or(QueryExp paramQueryExp1, QueryExp paramQueryExp2)
/*     */   {
/* 151 */     return new OrQueryExp(paramQueryExp1, paramQueryExp2);
/*     */   }
/*     */ 
/*     */   public static QueryExp gt(ValueExp paramValueExp1, ValueExp paramValueExp2)
/*     */   {
/* 169 */     return new BinaryRelQueryExp(0, paramValueExp1, paramValueExp2);
/*     */   }
/*     */ 
/*     */   public static QueryExp geq(ValueExp paramValueExp1, ValueExp paramValueExp2)
/*     */   {
/* 187 */     return new BinaryRelQueryExp(2, paramValueExp1, paramValueExp2);
/*     */   }
/*     */ 
/*     */   public static QueryExp leq(ValueExp paramValueExp1, ValueExp paramValueExp2)
/*     */   {
/* 205 */     return new BinaryRelQueryExp(3, paramValueExp1, paramValueExp2);
/*     */   }
/*     */ 
/*     */   public static QueryExp lt(ValueExp paramValueExp1, ValueExp paramValueExp2)
/*     */   {
/* 223 */     return new BinaryRelQueryExp(1, paramValueExp1, paramValueExp2);
/*     */   }
/*     */ 
/*     */   public static QueryExp eq(ValueExp paramValueExp1, ValueExp paramValueExp2)
/*     */   {
/* 241 */     return new BinaryRelQueryExp(4, paramValueExp1, paramValueExp2);
/*     */   }
/*     */ 
/*     */   public static QueryExp between(ValueExp paramValueExp1, ValueExp paramValueExp2, ValueExp paramValueExp3)
/*     */   {
/* 259 */     return new BetweenQueryExp(paramValueExp1, paramValueExp2, paramValueExp3);
/*     */   }
/*     */ 
/*     */   public static QueryExp match(AttributeValueExp paramAttributeValueExp, StringValueExp paramStringValueExp)
/*     */   {
/* 287 */     return new MatchQueryExp(paramAttributeValueExp, paramStringValueExp);
/*     */   }
/*     */ 
/*     */   public static AttributeValueExp attr(String paramString)
/*     */   {
/* 304 */     return new AttributeValueExp(paramString);
/*     */   }
/*     */ 
/*     */   public static AttributeValueExp attr(String paramString1, String paramString2)
/*     */   {
/* 327 */     return new QualifiedAttributeValueExp(paramString1, paramString2);
/*     */   }
/*     */ 
/*     */   public static AttributeValueExp classattr()
/*     */   {
/* 346 */     return new ClassAttributeValueExp();
/*     */   }
/*     */ 
/*     */   public static QueryExp not(QueryExp paramQueryExp)
/*     */   {
/* 360 */     return new NotQueryExp(paramQueryExp);
/*     */   }
/*     */ 
/*     */   public static QueryExp in(ValueExp paramValueExp, ValueExp[] paramArrayOfValueExp)
/*     */   {
/* 376 */     return new InQueryExp(paramValueExp, paramArrayOfValueExp);
/*     */   }
/*     */ 
/*     */   public static StringValueExp value(String paramString)
/*     */   {
/* 387 */     return new StringValueExp(paramString);
/*     */   }
/*     */ 
/*     */   public static ValueExp value(Number paramNumber)
/*     */   {
/* 403 */     return new NumericValueExp(paramNumber);
/*     */   }
/*     */ 
/*     */   public static ValueExp value(int paramInt)
/*     */   {
/* 419 */     return new NumericValueExp(Long.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   public static ValueExp value(long paramLong)
/*     */   {
/* 435 */     return new NumericValueExp(Long.valueOf(paramLong));
/*     */   }
/*     */ 
/*     */   public static ValueExp value(float paramFloat)
/*     */   {
/* 451 */     return new NumericValueExp(Double.valueOf(paramFloat));
/*     */   }
/*     */ 
/*     */   public static ValueExp value(double paramDouble)
/*     */   {
/* 467 */     return new NumericValueExp(Double.valueOf(paramDouble));
/*     */   }
/*     */ 
/*     */   public static ValueExp value(boolean paramBoolean)
/*     */   {
/* 483 */     return new BooleanValueExp(paramBoolean);
/*     */   }
/*     */ 
/*     */   public static ValueExp plus(ValueExp paramValueExp1, ValueExp paramValueExp2)
/*     */   {
/* 501 */     return new BinaryOpValueExp(0, paramValueExp1, paramValueExp2);
/*     */   }
/*     */ 
/*     */   public static ValueExp times(ValueExp paramValueExp1, ValueExp paramValueExp2)
/*     */   {
/* 519 */     return new BinaryOpValueExp(2, paramValueExp1, paramValueExp2);
/*     */   }
/*     */ 
/*     */   public static ValueExp minus(ValueExp paramValueExp1, ValueExp paramValueExp2)
/*     */   {
/* 537 */     return new BinaryOpValueExp(1, paramValueExp1, paramValueExp2);
/*     */   }
/*     */ 
/*     */   public static ValueExp div(ValueExp paramValueExp1, ValueExp paramValueExp2)
/*     */   {
/* 555 */     return new BinaryOpValueExp(3, paramValueExp1, paramValueExp2);
/*     */   }
/*     */ 
/*     */   public static QueryExp initialSubString(AttributeValueExp paramAttributeValueExp, StringValueExp paramStringValueExp)
/*     */   {
/* 574 */     return new MatchQueryExp(paramAttributeValueExp, new StringValueExp(escapeString(paramStringValueExp.getValue()) + "*"));
/*     */   }
/*     */ 
/*     */   public static QueryExp anySubString(AttributeValueExp paramAttributeValueExp, StringValueExp paramStringValueExp)
/*     */   {
/* 593 */     return new MatchQueryExp(paramAttributeValueExp, new StringValueExp("*" + escapeString(paramStringValueExp.getValue()) + "*"));
/*     */   }
/*     */ 
/*     */   public static QueryExp finalSubString(AttributeValueExp paramAttributeValueExp, StringValueExp paramStringValueExp)
/*     */   {
/* 613 */     return new MatchQueryExp(paramAttributeValueExp, new StringValueExp("*" + escapeString(paramStringValueExp.getValue())));
/*     */   }
/*     */ 
/*     */   public static QueryExp isInstanceOf(StringValueExp paramStringValueExp)
/*     */   {
/* 639 */     return new InstanceOfQueryExp(paramStringValueExp);
/*     */   }
/*     */ 
/*     */   private static String escapeString(String paramString)
/*     */   {
/* 647 */     if (paramString == null)
/* 648 */       return null;
/* 649 */     paramString = paramString.replace("\\", "\\\\");
/* 650 */     paramString = paramString.replace("*", "\\*");
/* 651 */     paramString = paramString.replace("?", "\\?");
/* 652 */     paramString = paramString.replace("[", "\\[");
/* 653 */     return paramString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.Query
 * JD-Core Version:    0.6.2
 */