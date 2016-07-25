/*     */ package javax.management;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*     */ import java.security.AccessController;
/*     */ 
/*     */ class ClassAttributeValueExp extends AttributeValueExp
/*     */ {
/*     */   private static final long oldSerialVersionUID = -2212731951078526753L;
/*     */   private static final long newSerialVersionUID = -1081892073854801359L;
/*     */   private static final long serialVersionUID;
/*     */   private String attr;
/*     */ 
/*     */   public ClassAttributeValueExp()
/*     */   {
/*  86 */     super("Class");
/*  87 */     this.attr = "Class";
/*     */   }
/*     */ 
/*     */   public ValueExp apply(ObjectName paramObjectName)
/*     */     throws BadStringOperationException, BadBinaryOpValueExpException, BadAttributeValueExpException, InvalidApplicationException
/*     */   {
/* 106 */     Object localObject = getValue(paramObjectName);
/* 107 */     if ((localObject instanceof String)) {
/* 108 */       return new StringValueExp((String)localObject);
/*     */     }
/* 110 */     throw new BadAttributeValueExpException(localObject);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 118 */     return this.attr;
/*     */   }
/*     */ 
/*     */   protected Object getValue(ObjectName paramObjectName)
/*     */   {
/*     */     try
/*     */     {
/* 125 */       MBeanServer localMBeanServer = QueryEval.getMBeanServer();
/* 126 */       return localMBeanServer.getObjectInstance(paramObjectName).getClassName(); } catch (Exception localException) {
/*     */     }
/* 128 */     return null;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  59 */     int i = 0;
/*     */     try {
/*  61 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.serial.form");
/*  62 */       String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/*  63 */       i = (str != null) && (str.equals("1.0")) ? 1 : 0;
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/*  67 */     if (i != 0)
/*  68 */       serialVersionUID = -2212731951078526753L;
/*     */     else
/*  70 */       serialVersionUID = -1081892073854801359L;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.ClassAttributeValueExp
 * JD-Core Version:    0.6.2
 */