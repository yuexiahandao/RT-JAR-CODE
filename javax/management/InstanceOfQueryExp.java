/*     */ package javax.management;
/*     */ 
/*     */ class InstanceOfQueryExp extends QueryEval
/*     */   implements QueryExp
/*     */ {
/*     */   private static final long serialVersionUID = -1081892073854801359L;
/*     */   private StringValueExp classNameValue;
/*     */ 
/*     */   public InstanceOfQueryExp(StringValueExp paramStringValueExp)
/*     */   {
/*  59 */     if (paramStringValueExp == null) {
/*  60 */       throw new IllegalArgumentException("Null class name.");
/*     */     }
/*     */ 
/*  63 */     this.classNameValue = paramStringValueExp;
/*     */   }
/*     */ 
/*     */   public StringValueExp getClassNameValue()
/*     */   {
/*  72 */     return this.classNameValue;
/*     */   }
/*     */ 
/*     */   public boolean apply(ObjectName paramObjectName)
/*     */     throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException
/*     */   {
/*     */     StringValueExp localStringValueExp;
/*     */     try
/*     */     {
/*  95 */       localStringValueExp = (StringValueExp)this.classNameValue.apply(paramObjectName);
/*     */     }
/*     */     catch (ClassCastException localClassCastException)
/*     */     {
/*  99 */       BadStringOperationException localBadStringOperationException = new BadStringOperationException(localClassCastException.toString());
/*     */ 
/* 101 */       localBadStringOperationException.initCause(localClassCastException);
/* 102 */       throw localBadStringOperationException;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 107 */       return getMBeanServer().isInstanceOf(paramObjectName, localStringValueExp.getValue()); } catch (InstanceNotFoundException localInstanceNotFoundException) {
/*     */     }
/* 109 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 118 */     return "InstanceOf " + this.classNameValue.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.InstanceOfQueryExp
 * JD-Core Version:    0.6.2
 */