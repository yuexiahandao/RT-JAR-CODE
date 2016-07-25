/*     */ package javax.management.modelmbean;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.security.AccessController;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.management.Descriptor;
/*     */ import javax.management.DescriptorAccess;
/*     */ import javax.management.MBeanConstructorInfo;
/*     */ import javax.management.MBeanParameterInfo;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ 
/*     */ public class ModelMBeanConstructorInfo extends MBeanConstructorInfo
/*     */   implements DescriptorAccess
/*     */ {
/*     */   private static final long oldSerialVersionUID = -4440125391095574518L;
/*     */   private static final long newSerialVersionUID = 3862947819818064362L;
/* 106 */   private static final ObjectStreamField[] oldSerialPersistentFields = { new ObjectStreamField("consDescriptor", Descriptor.class), new ObjectStreamField("currClass", String.class) };
/*     */ 
/* 113 */   private static final ObjectStreamField[] newSerialPersistentFields = { new ObjectStreamField("consDescriptor", Descriptor.class) };
/*     */   private static final long serialVersionUID;
/*     */   private static final ObjectStreamField[] serialPersistentFields;
/* 124 */   private static boolean compat = false;
/*     */ 
/* 147 */   private Descriptor consDescriptor = validDescriptor(null);
/*     */   private static final String currClass = "ModelMBeanConstructorInfo";
/*     */ 
/*     */   public ModelMBeanConstructorInfo(String paramString, Constructor<?> paramConstructor)
/*     */   {
/* 166 */     super(paramString, paramConstructor);
/* 167 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 168 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanConstructorInfo.class.getName(), "ModelMBeanConstructorInfo(String,Constructor)", "Entry");
/*     */     }
/*     */ 
/* 173 */     this.consDescriptor = validDescriptor(null);
/*     */   }
/*     */ 
/*     */   public ModelMBeanConstructorInfo(String paramString, Constructor<?> paramConstructor, Descriptor paramDescriptor)
/*     */   {
/* 210 */     super(paramString, paramConstructor);
/*     */ 
/* 212 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 213 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanConstructorInfo.class.getName(), "ModelMBeanConstructorInfo(String,Constructor,Descriptor)", "Entry");
/*     */     }
/*     */ 
/* 218 */     this.consDescriptor = validDescriptor(paramDescriptor);
/*     */   }
/*     */ 
/*     */   public ModelMBeanConstructorInfo(String paramString1, String paramString2, MBeanParameterInfo[] paramArrayOfMBeanParameterInfo)
/*     */   {
/* 233 */     super(paramString1, paramString2, paramArrayOfMBeanParameterInfo);
/*     */ 
/* 235 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 236 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanConstructorInfo.class.getName(), "ModelMBeanConstructorInfo(String,String,MBeanParameterInfo[])", "Entry");
/*     */     }
/*     */ 
/* 241 */     this.consDescriptor = validDescriptor(null);
/*     */   }
/*     */ 
/*     */   public ModelMBeanConstructorInfo(String paramString1, String paramString2, MBeanParameterInfo[] paramArrayOfMBeanParameterInfo, Descriptor paramDescriptor)
/*     */   {
/* 267 */     super(paramString1, paramString2, paramArrayOfMBeanParameterInfo);
/* 268 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 269 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanConstructorInfo.class.getName(), "ModelMBeanConstructorInfo(String,String,MBeanParameterInfo[],Descriptor)", "Entry");
/*     */     }
/*     */ 
/* 275 */     this.consDescriptor = validDescriptor(paramDescriptor);
/*     */   }
/*     */ 
/*     */   ModelMBeanConstructorInfo(ModelMBeanConstructorInfo paramModelMBeanConstructorInfo)
/*     */   {
/* 286 */     super(paramModelMBeanConstructorInfo.getName(), paramModelMBeanConstructorInfo.getDescription(), paramModelMBeanConstructorInfo.getSignature());
/* 287 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 288 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanConstructorInfo.class.getName(), "ModelMBeanConstructorInfo(ModelMBeanConstructorInfo)", "Entry");
/*     */     }
/*     */ 
/* 293 */     this.consDescriptor = validDescriptor(this.consDescriptor);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 303 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 304 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanConstructorInfo.class.getName(), "clone()", "Entry");
/*     */     }
/*     */ 
/* 308 */     return new ModelMBeanConstructorInfo(this);
/*     */   }
/*     */ 
/*     */   public Descriptor getDescriptor()
/*     */   {
/* 324 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 325 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanConstructorInfo.class.getName(), "getDescriptor()", "Entry");
/*     */     }
/*     */ 
/* 329 */     if (this.consDescriptor == null) {
/* 330 */       this.consDescriptor = validDescriptor(null);
/*     */     }
/* 332 */     return (Descriptor)this.consDescriptor.clone();
/*     */   }
/*     */ 
/*     */   public void setDescriptor(Descriptor paramDescriptor)
/*     */   {
/* 359 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 360 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanConstructorInfo.class.getName(), "setDescriptor()", "Entry");
/*     */     }
/*     */ 
/* 364 */     this.consDescriptor = validDescriptor(paramDescriptor);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 373 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 374 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanConstructorInfo.class.getName(), "toString()", "Entry");
/*     */     }
/*     */ 
/* 378 */     String str = "ModelMBeanConstructorInfo: " + getName() + " ; Description: " + getDescription() + " ; Descriptor: " + getDescriptor() + " ; Signature: ";
/*     */ 
/* 383 */     MBeanParameterInfo[] arrayOfMBeanParameterInfo = getSignature();
/* 384 */     for (int i = 0; i < arrayOfMBeanParameterInfo.length; i++)
/*     */     {
/* 386 */       str = str.concat(arrayOfMBeanParameterInfo[i].getType() + ", ");
/*     */     }
/* 388 */     return str;
/*     */   }
/*     */ 
/*     */   private Descriptor validDescriptor(Descriptor paramDescriptor)
/*     */     throws RuntimeOperationsException
/*     */   {
/* 408 */     int i = paramDescriptor == null ? 1 : 0;
/*     */     Object localObject;
/* 409 */     if (i != 0) {
/* 410 */       localObject = new DescriptorSupport();
/* 411 */       JmxProperties.MODELMBEAN_LOGGER.finer("Null Descriptor, creating new.");
/*     */     } else {
/* 413 */       localObject = (Descriptor)paramDescriptor.clone();
/*     */     }
/*     */ 
/* 417 */     if ((i != 0) && (((Descriptor)localObject).getFieldValue("name") == null)) {
/* 418 */       ((Descriptor)localObject).setField("name", getName());
/* 419 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor name to " + getName());
/*     */     }
/* 421 */     if ((i != 0) && (((Descriptor)localObject).getFieldValue("descriptorType") == null)) {
/* 422 */       ((Descriptor)localObject).setField("descriptorType", "operation");
/* 423 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting descriptorType to \"operation\"");
/*     */     }
/* 425 */     if (((Descriptor)localObject).getFieldValue("displayName") == null) {
/* 426 */       ((Descriptor)localObject).setField("displayName", getName());
/* 427 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor displayName to " + getName());
/*     */     }
/* 429 */     if (((Descriptor)localObject).getFieldValue("role") == null) {
/* 430 */       ((Descriptor)localObject).setField("role", "constructor");
/* 431 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor role field to \"constructor\"");
/*     */     }
/*     */ 
/* 435 */     if (!((Descriptor)localObject).isValid()) {
/* 436 */       throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The isValid() method of the Descriptor object itself returned false,one or more required fields are invalid. Descriptor:" + localObject.toString());
/*     */     }
/*     */ 
/* 440 */     if (!getName().equalsIgnoreCase((String)((Descriptor)localObject).getFieldValue("name"))) {
/* 441 */       throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor \"name\" field does not match the object described.  Expected: " + getName() + " , was: " + ((Descriptor)localObject).getFieldValue("name"));
/*     */     }
/*     */ 
/* 445 */     if (!"operation".equalsIgnoreCase((String)((Descriptor)localObject).getFieldValue("descriptorType"))) {
/* 446 */       throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor \"descriptorType\" field does not match the object described.  Expected: \"operation\" , was: " + ((Descriptor)localObject).getFieldValue("descriptorType"));
/*     */     }
/*     */ 
/* 450 */     if (!((String)((Descriptor)localObject).getFieldValue("role")).equalsIgnoreCase("constructor")) {
/* 451 */       throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor \"role\" field does not match the object described.  Expected: \"constructor\" , was: " + ((Descriptor)localObject).getFieldValue("role"));
/*     */     }
/*     */ 
/* 456 */     return localObject;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 465 */     paramObjectInputStream.defaultReadObject();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 474 */     if (compat)
/*     */     {
/* 478 */       ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 479 */       localPutField.put("consDescriptor", this.consDescriptor);
/* 480 */       localPutField.put("currClass", "ModelMBeanConstructorInfo");
/* 481 */       paramObjectOutputStream.writeFields();
/*     */     }
/*     */     else
/*     */     {
/* 487 */       paramObjectOutputStream.defaultWriteObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 127 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.serial.form");
/* 128 */       String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/* 129 */       compat = (str != null) && (str.equals("1.0"));
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 133 */     if (compat) {
/* 134 */       serialPersistentFields = oldSerialPersistentFields;
/* 135 */       serialVersionUID = -4440125391095574518L;
/*     */     } else {
/* 137 */       serialPersistentFields = newSerialPersistentFields;
/* 138 */       serialVersionUID = 3862947819818064362L;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.modelmbean.ModelMBeanConstructorInfo
 * JD-Core Version:    0.6.2
 */