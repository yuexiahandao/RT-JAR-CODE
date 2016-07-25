/*     */ package javax.management;
/*     */ 
/*     */ public class StringValueExp
/*     */   implements ValueExp
/*     */ {
/*     */   private static final long serialVersionUID = -3256390509806284044L;
/*     */   private String val;
/*     */ 
/*     */   public StringValueExp()
/*     */   {
/*     */   }
/*     */ 
/*     */   public StringValueExp(String paramString)
/*     */   {
/*  59 */     this.val = paramString;
/*     */   }
/*     */ 
/*     */   public String getValue()
/*     */   {
/*  69 */     return this.val;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  76 */     return "'" + this.val.replace("'", "''") + "'";
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setMBeanServer(MBeanServer paramMBeanServer)
/*     */   {
/*     */   }
/*     */ 
/*     */   public ValueExp apply(ObjectName paramObjectName)
/*     */     throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException
/*     */   {
/* 105 */     return this;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.StringValueExp
 * JD-Core Version:    0.6.2
 */