/*     */ package javax.management;
/*     */ 
/*     */ class QualifiedAttributeValueExp extends AttributeValueExp
/*     */ {
/*     */   private static final long serialVersionUID = 8832517277410933254L;
/*     */   private String className;
/*     */ 
/*     */   @Deprecated
/*     */   public QualifiedAttributeValueExp()
/*     */   {
/*     */   }
/*     */ 
/*     */   public QualifiedAttributeValueExp(String paramString1, String paramString2)
/*     */   {
/*  64 */     super(paramString2);
/*  65 */     this.className = paramString1;
/*     */   }
/*     */ 
/*     */   public String getAttrClassName()
/*     */   {
/*  73 */     return this.className;
/*     */   }
/*     */ 
/*     */   public ValueExp apply(ObjectName paramObjectName)
/*     */     throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException
/*     */   {
/*     */     try
/*     */     {
/*  92 */       MBeanServer localMBeanServer = QueryEval.getMBeanServer();
/*  93 */       String str = localMBeanServer.getObjectInstance(paramObjectName).getClassName();
/*     */ 
/*  95 */       if (str.equals(this.className)) {
/*  96 */         return super.apply(paramObjectName);
/*     */       }
/*  98 */       throw new InvalidApplicationException("Class name is " + str + ", should be " + this.className);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 102 */       throw new InvalidApplicationException("Qualified attribute: " + localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 115 */     if (this.className != null) {
/* 116 */       return this.className + "." + super.toString();
/*     */     }
/* 118 */     return super.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.QualifiedAttributeValueExp
 * JD-Core Version:    0.6.2
 */