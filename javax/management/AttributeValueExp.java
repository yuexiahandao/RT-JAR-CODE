/*     */ package javax.management;
/*     */ 
/*     */ public class AttributeValueExp
/*     */   implements ValueExp
/*     */ {
/*     */   private static final long serialVersionUID = -7768025046539163385L;
/*     */   private String attr;
/*     */ 
/*     */   @Deprecated
/*     */   public AttributeValueExp()
/*     */   {
/*     */   }
/*     */ 
/*     */   public AttributeValueExp(String paramString)
/*     */   {
/*  71 */     this.attr = paramString;
/*     */   }
/*     */ 
/*     */   public String getAttributeName()
/*     */   {
/*  80 */     return this.attr;
/*     */   }
/*     */ 
/*     */   public ValueExp apply(ObjectName paramObjectName)
/*     */     throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException
/*     */   {
/* 105 */     Object localObject = getAttribute(paramObjectName);
/*     */ 
/* 107 */     if ((localObject instanceof Number))
/* 108 */       return new NumericValueExp((Number)localObject);
/* 109 */     if ((localObject instanceof String))
/* 110 */       return new StringValueExp((String)localObject);
/* 111 */     if ((localObject instanceof Boolean)) {
/* 112 */       return new BooleanValueExp((Boolean)localObject);
/*     */     }
/* 114 */     throw new BadAttributeValueExpException(localObject);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 123 */     return this.attr;
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setMBeanServer(MBeanServer paramMBeanServer)
/*     */   {
/*     */   }
/*     */ 
/*     */   protected Object getAttribute(ObjectName paramObjectName)
/*     */   {
/*     */     try
/*     */     {
/* 161 */       MBeanServer localMBeanServer = QueryEval.getMBeanServer();
/*     */ 
/* 163 */       return localMBeanServer.getAttribute(paramObjectName, this.attr); } catch (Exception localException) {
/*     */     }
/* 165 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.AttributeValueExp
 * JD-Core Version:    0.6.2
 */