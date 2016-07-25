/*     */ package javax.management.modelmbean;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.management.Descriptor;
/*     */ import javax.management.DescriptorAccess;
/*     */ import javax.management.MBeanOperationInfo;
/*     */ import javax.management.MBeanParameterInfo;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ 
/*     */ public class ModelMBeanOperationInfo extends MBeanOperationInfo
/*     */   implements DescriptorAccess
/*     */ {
/*     */   private static final long oldSerialVersionUID = 9087646304346171239L;
/*     */   private static final long newSerialVersionUID = 6532732096650090465L;
/* 125 */   private static final ObjectStreamField[] oldSerialPersistentFields = { new ObjectStreamField("operationDescriptor", Descriptor.class), new ObjectStreamField("currClass", String.class) };
/*     */ 
/* 132 */   private static final ObjectStreamField[] newSerialPersistentFields = { new ObjectStreamField("operationDescriptor", Descriptor.class) };
/*     */   private static final long serialVersionUID;
/*     */   private static final ObjectStreamField[] serialPersistentFields;
/* 144 */   private static boolean compat = false;
/*     */ 
/* 167 */   private Descriptor operationDescriptor = validDescriptor(null);
/*     */   private static final String currClass = "ModelMBeanOperationInfo";
/*     */ 
/*     */   public ModelMBeanOperationInfo(String paramString, Method paramMethod)
/*     */   {
/* 186 */     super(paramString, paramMethod);
/*     */ 
/* 188 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 189 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanOperationInfo.class.getName(), "ModelMBeanOperationInfo(String,Method)", "Entry");
/*     */     }
/*     */ 
/* 194 */     this.operationDescriptor = validDescriptor(null);
/*     */   }
/*     */ 
/*     */   public ModelMBeanOperationInfo(String paramString, Method paramMethod, Descriptor paramDescriptor)
/*     */   {
/* 230 */     super(paramString, paramMethod);
/* 231 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 232 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanOperationInfo.class.getName(), "ModelMBeanOperationInfo(String,Method,Descriptor)", "Entry");
/*     */     }
/*     */ 
/* 237 */     this.operationDescriptor = validDescriptor(paramDescriptor);
/*     */   }
/*     */ 
/*     */   public ModelMBeanOperationInfo(String paramString1, String paramString2, MBeanParameterInfo[] paramArrayOfMBeanParameterInfo, String paramString3, int paramInt)
/*     */   {
/* 259 */     super(paramString1, paramString2, paramArrayOfMBeanParameterInfo, paramString3, paramInt);
/*     */ 
/* 261 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 262 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanOperationInfo.class.getName(), "ModelMBeanOperationInfo(String,String,MBeanParameterInfo[],String,int)", "Entry");
/*     */     }
/*     */ 
/* 268 */     this.operationDescriptor = validDescriptor(null);
/*     */   }
/*     */ 
/*     */   public ModelMBeanOperationInfo(String paramString1, String paramString2, MBeanParameterInfo[] paramArrayOfMBeanParameterInfo, String paramString3, int paramInt, Descriptor paramDescriptor)
/*     */   {
/* 304 */     super(paramString1, paramString2, paramArrayOfMBeanParameterInfo, paramString3, paramInt);
/* 305 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 306 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanOperationInfo.class.getName(), "ModelMBeanOperationInfo(String,String,MBeanParameterInfo[],String,int,Descriptor)", "Entry");
/*     */     }
/*     */ 
/* 312 */     this.operationDescriptor = validDescriptor(paramDescriptor);
/*     */   }
/*     */ 
/*     */   public ModelMBeanOperationInfo(ModelMBeanOperationInfo paramModelMBeanOperationInfo)
/*     */   {
/* 325 */     super(paramModelMBeanOperationInfo.getName(), paramModelMBeanOperationInfo.getDescription(), paramModelMBeanOperationInfo.getSignature(), paramModelMBeanOperationInfo.getReturnType(), paramModelMBeanOperationInfo.getImpact());
/*     */ 
/* 330 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 331 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanOperationInfo.class.getName(), "ModelMBeanOperationInfo(ModelMBeanOperationInfo)", "Entry");
/*     */     }
/*     */ 
/* 336 */     Descriptor localDescriptor = paramModelMBeanOperationInfo.getDescriptor();
/* 337 */     this.operationDescriptor = validDescriptor(localDescriptor);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 348 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 349 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanOperationInfo.class.getName(), "clone()", "Entry");
/*     */     }
/*     */ 
/* 353 */     return new ModelMBeanOperationInfo(this);
/*     */   }
/*     */ 
/*     */   public Descriptor getDescriptor()
/*     */   {
/* 368 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 369 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanOperationInfo.class.getName(), "getDescriptor()", "Entry");
/*     */     }
/*     */ 
/* 373 */     if (this.operationDescriptor == null) {
/* 374 */       this.operationDescriptor = validDescriptor(null);
/*     */     }
/*     */ 
/* 377 */     return (Descriptor)this.operationDescriptor.clone();
/*     */   }
/*     */ 
/*     */   public void setDescriptor(Descriptor paramDescriptor)
/*     */   {
/* 399 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 400 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanOperationInfo.class.getName(), "setDescriptor(Descriptor)", "Entry");
/*     */     }
/*     */ 
/* 404 */     this.operationDescriptor = validDescriptor(paramDescriptor);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 413 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 414 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanOperationInfo.class.getName(), "toString()", "Entry");
/*     */     }
/*     */ 
/* 418 */     String str = "ModelMBeanOperationInfo: " + getName() + " ; Description: " + getDescription() + " ; Descriptor: " + getDescriptor() + " ; ReturnType: " + getReturnType() + " ; Signature: ";
/*     */ 
/* 424 */     MBeanParameterInfo[] arrayOfMBeanParameterInfo = getSignature();
/* 425 */     for (int i = 0; i < arrayOfMBeanParameterInfo.length; i++)
/*     */     {
/* 427 */       str = str.concat(arrayOfMBeanParameterInfo[i].getType() + ", ");
/*     */     }
/* 429 */     return str;
/*     */   }
/*     */ 
/*     */   private Descriptor validDescriptor(Descriptor paramDescriptor)
/*     */     throws RuntimeOperationsException
/*     */   {
/* 449 */     int i = paramDescriptor == null ? 1 : 0;
/*     */     Object localObject1;
/* 450 */     if (i != 0) {
/* 451 */       localObject1 = new DescriptorSupport();
/* 452 */       JmxProperties.MODELMBEAN_LOGGER.finer("Null Descriptor, creating new.");
/*     */     } else {
/* 454 */       localObject1 = (Descriptor)paramDescriptor.clone();
/*     */     }
/*     */ 
/* 458 */     if ((i != 0) && (((Descriptor)localObject1).getFieldValue("name") == null)) {
/* 459 */       ((Descriptor)localObject1).setField("name", getName());
/* 460 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor name to " + getName());
/*     */     }
/* 462 */     if ((i != 0) && (((Descriptor)localObject1).getFieldValue("descriptorType") == null)) {
/* 463 */       ((Descriptor)localObject1).setField("descriptorType", "operation");
/* 464 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting descriptorType to \"operation\"");
/*     */     }
/* 466 */     if (((Descriptor)localObject1).getFieldValue("displayName") == null) {
/* 467 */       ((Descriptor)localObject1).setField("displayName", getName());
/* 468 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor displayName to " + getName());
/*     */     }
/* 470 */     if (((Descriptor)localObject1).getFieldValue("role") == null) {
/* 471 */       ((Descriptor)localObject1).setField("role", "operation");
/* 472 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor role field to \"operation\"");
/*     */     }
/*     */ 
/* 476 */     if (!((Descriptor)localObject1).isValid()) {
/* 477 */       throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The isValid() method of the Descriptor object itself returned false,one or more required fields are invalid. Descriptor:" + localObject1.toString());
/*     */     }
/*     */ 
/* 481 */     if (!getName().equalsIgnoreCase((String)((Descriptor)localObject1).getFieldValue("name"))) {
/* 482 */       throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor \"name\" field does not match the object described.  Expected: " + getName() + " , was: " + ((Descriptor)localObject1).getFieldValue("name"));
/*     */     }
/*     */ 
/* 486 */     if (!"operation".equalsIgnoreCase((String)((Descriptor)localObject1).getFieldValue("descriptorType"))) {
/* 487 */       throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor \"descriptorType\" field does not match the object described.  Expected: \"operation\" , was: " + ((Descriptor)localObject1).getFieldValue("descriptorType"));
/*     */     }
/*     */ 
/* 491 */     String str = (String)((Descriptor)localObject1).getFieldValue("role");
/* 492 */     if ((!str.equalsIgnoreCase("operation")) && (!str.equalsIgnoreCase("setter")) && (!str.equalsIgnoreCase("getter")))
/*     */     {
/* 495 */       throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor \"role\" field does not match the object described.  Expected: \"operation\", \"setter\", or \"getter\" , was: " + ((Descriptor)localObject1).getFieldValue("role"));
/*     */     }
/*     */ 
/* 499 */     Object localObject2 = ((Descriptor)localObject1).getFieldValue("targetType");
/* 500 */     if ((localObject2 != null) && 
/* 501 */       (!(localObject2 instanceof String))) {
/* 502 */       throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor field \"targetValue\" is invalid class.  Expected: java.lang.String,  was: " + localObject2.getClass().getName());
/*     */     }
/*     */ 
/* 507 */     return localObject1;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 516 */     paramObjectInputStream.defaultReadObject();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 525 */     if (compat)
/*     */     {
/* 529 */       ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 530 */       localPutField.put("operationDescriptor", this.operationDescriptor);
/* 531 */       localPutField.put("currClass", "ModelMBeanOperationInfo");
/* 532 */       paramObjectOutputStream.writeFields();
/*     */     }
/*     */     else
/*     */     {
/* 538 */       paramObjectOutputStream.defaultWriteObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 147 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.serial.form");
/* 148 */       String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/* 149 */       compat = (str != null) && (str.equals("1.0"));
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 153 */     if (compat) {
/* 154 */       serialPersistentFields = oldSerialPersistentFields;
/* 155 */       serialVersionUID = 9087646304346171239L;
/*     */     } else {
/* 157 */       serialPersistentFields = newSerialPersistentFields;
/* 158 */       serialVersionUID = 6532732096650090465L;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.modelmbean.ModelMBeanOperationInfo
 * JD-Core Version:    0.6.2
 */