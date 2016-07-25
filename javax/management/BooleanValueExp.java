/*    */ package javax.management;
/*    */ 
/*    */ class BooleanValueExp extends QueryEval
/*    */   implements ValueExp
/*    */ {
/*    */   private static final long serialVersionUID = 7754922052666594581L;
/* 44 */   private boolean val = false;
/*    */ 
/*    */   BooleanValueExp(boolean paramBoolean)
/*    */   {
/* 49 */     this.val = paramBoolean;
/*    */   }
/*    */ 
/*    */   BooleanValueExp(Boolean paramBoolean)
/*    */   {
/* 54 */     this.val = paramBoolean.booleanValue();
/*    */   }
/*    */ 
/*    */   public Boolean getValue()
/*    */   {
/* 60 */     return Boolean.valueOf(this.val);
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 67 */     return String.valueOf(this.val);
/*    */   }
/*    */ 
/*    */   public ValueExp apply(ObjectName paramObjectName)
/*    */     throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException
/*    */   {
/* 84 */     return this;
/*    */   }
/*    */ 
/*    */   @Deprecated
/*    */   public void setMBeanServer(MBeanServer paramMBeanServer) {
/* 89 */     super.setMBeanServer(paramMBeanServer);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.BooleanValueExp
 * JD-Core Version:    0.6.2
 */