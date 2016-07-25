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
/*     */ import javax.management.IntrospectionException;
/*     */ import javax.management.MBeanAttributeInfo;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ 
/*     */ public class ModelMBeanAttributeInfo extends MBeanAttributeInfo
/*     */   implements DescriptorAccess
/*     */ {
/*     */   private static final long oldSerialVersionUID = 7098036920755973145L;
/*     */   private static final long newSerialVersionUID = 6181543027787327345L;
/* 132 */   private static final ObjectStreamField[] oldSerialPersistentFields = { new ObjectStreamField("attrDescriptor", Descriptor.class), new ObjectStreamField("currClass", String.class) };
/*     */ 
/* 139 */   private static final ObjectStreamField[] newSerialPersistentFields = { new ObjectStreamField("attrDescriptor", Descriptor.class) };
/*     */   private static final long serialVersionUID;
/*     */   private static final ObjectStreamField[] serialPersistentFields;
/* 151 */   private static boolean compat = false;
/*     */ 
/* 175 */   private Descriptor attrDescriptor = validDescriptor(null);
/*     */   private static final String currClass = "ModelMBeanAttributeInfo";
/*     */ 
/*     */   public ModelMBeanAttributeInfo(String paramString1, String paramString2, Method paramMethod1, Method paramMethod2)
/*     */     throws IntrospectionException
/*     */   {
/* 202 */     super(paramString1, paramString2, paramMethod1, paramMethod2);
/*     */ 
/* 204 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 205 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanAttributeInfo.class.getName(), "ModelMBeanAttributeInfo(String,String,Method,Method)", "Entry", paramString1);
/*     */     }
/*     */ 
/* 212 */     this.attrDescriptor = validDescriptor(null);
/*     */   }
/*     */ 
/*     */   public ModelMBeanAttributeInfo(String paramString1, String paramString2, Method paramMethod1, Method paramMethod2, Descriptor paramDescriptor)
/*     */     throws IntrospectionException
/*     */   {
/* 252 */     super(paramString1, paramString2, paramMethod1, paramMethod2);
/*     */ 
/* 254 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 255 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanAttributeInfo.class.getName(), "ModelMBeanAttributeInfo(String,String,Method,Method,Descriptor)", "Entry", paramString1);
/*     */     }
/*     */ 
/* 261 */     this.attrDescriptor = validDescriptor(paramDescriptor);
/*     */   }
/*     */ 
/*     */   public ModelMBeanAttributeInfo(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3)
/*     */   {
/* 283 */     super(paramString1, paramString2, paramString3, paramBoolean1, paramBoolean2, paramBoolean3);
/*     */ 
/* 285 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 286 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanAttributeInfo.class.getName(), "ModelMBeanAttributeInfo(String,String,String,boolean,boolean,boolean)", "Entry", paramString1);
/*     */     }
/*     */ 
/* 292 */     this.attrDescriptor = validDescriptor(null);
/*     */   }
/*     */ 
/*     */   public ModelMBeanAttributeInfo(String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, Descriptor paramDescriptor)
/*     */   {
/* 323 */     super(paramString1, paramString2, paramString3, paramBoolean1, paramBoolean2, paramBoolean3);
/* 324 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 325 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanAttributeInfo.class.getName(), "ModelMBeanAttributeInfo(String,String,String,boolean,boolean,boolean,Descriptor)", "Entry", paramString1);
/*     */     }
/*     */ 
/* 331 */     this.attrDescriptor = validDescriptor(paramDescriptor);
/*     */   }
/*     */ 
/*     */   public ModelMBeanAttributeInfo(ModelMBeanAttributeInfo paramModelMBeanAttributeInfo)
/*     */   {
/* 344 */     super(paramModelMBeanAttributeInfo.getName(), paramModelMBeanAttributeInfo.getType(), paramModelMBeanAttributeInfo.getDescription(), paramModelMBeanAttributeInfo.isReadable(), paramModelMBeanAttributeInfo.isWritable(), paramModelMBeanAttributeInfo.isIs());
/*     */ 
/* 350 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 351 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanAttributeInfo.class.getName(), "ModelMBeanAttributeInfo(ModelMBeanAttributeInfo)", "Entry");
/*     */     }
/*     */ 
/* 356 */     Descriptor localDescriptor = paramModelMBeanAttributeInfo.getDescriptor();
/* 357 */     this.attrDescriptor = validDescriptor(localDescriptor);
/*     */   }
/*     */ 
/*     */   public Descriptor getDescriptor()
/*     */   {
/* 371 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 372 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanAttributeInfo.class.getName(), "getDescriptor()", "Entry");
/*     */     }
/*     */ 
/* 376 */     if (this.attrDescriptor == null) {
/* 377 */       this.attrDescriptor = validDescriptor(null);
/*     */     }
/* 379 */     return (Descriptor)this.attrDescriptor.clone();
/*     */   }
/*     */ 
/*     */   public void setDescriptor(Descriptor paramDescriptor)
/*     */   {
/* 400 */     this.attrDescriptor = validDescriptor(paramDescriptor);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 414 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 415 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanAttributeInfo.class.getName(), "clone()", "Entry");
/*     */     }
/*     */ 
/* 419 */     return new ModelMBeanAttributeInfo(this);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 429 */     return "ModelMBeanAttributeInfo: " + getName() + " ; Description: " + getDescription() + " ; Types: " + getType() + " ; isReadable: " + isReadable() + " ; isWritable: " + isWritable() + " ; Descriptor: " + getDescriptor();
/*     */   }
/*     */ 
/*     */   private Descriptor validDescriptor(Descriptor paramDescriptor)
/*     */     throws RuntimeOperationsException
/*     */   {
/* 454 */     int i = paramDescriptor == null ? 1 : 0;
/*     */     Object localObject;
/* 455 */     if (i != 0) {
/* 456 */       localObject = new DescriptorSupport();
/* 457 */       JmxProperties.MODELMBEAN_LOGGER.finer("Null Descriptor, creating new.");
/*     */     } else {
/* 459 */       localObject = (Descriptor)paramDescriptor.clone();
/*     */     }
/*     */ 
/* 463 */     if ((i != 0) && (((Descriptor)localObject).getFieldValue("name") == null)) {
/* 464 */       ((Descriptor)localObject).setField("name", getName());
/* 465 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor name to " + getName());
/*     */     }
/* 467 */     if ((i != 0) && (((Descriptor)localObject).getFieldValue("descriptorType") == null)) {
/* 468 */       ((Descriptor)localObject).setField("descriptorType", "attribute");
/* 469 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting descriptorType to \"attribute\"");
/*     */     }
/* 471 */     if (((Descriptor)localObject).getFieldValue("displayName") == null) {
/* 472 */       ((Descriptor)localObject).setField("displayName", getName());
/* 473 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor displayName to " + getName());
/*     */     }
/*     */ 
/* 477 */     if (!((Descriptor)localObject).isValid()) {
/* 478 */       throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The isValid() method of the Descriptor object itself returned false,one or more required fields are invalid. Descriptor:" + localObject.toString());
/*     */     }
/*     */ 
/* 482 */     if (!getName().equalsIgnoreCase((String)((Descriptor)localObject).getFieldValue("name"))) {
/* 483 */       throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor \"name\" field does not match the object described.  Expected: " + getName() + " , was: " + ((Descriptor)localObject).getFieldValue("name"));
/*     */     }
/*     */ 
/* 488 */     if (!"attribute".equalsIgnoreCase((String)((Descriptor)localObject).getFieldValue("descriptorType"))) {
/* 489 */       throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor \"descriptorType\" field does not match the object described.  Expected: \"attribute\" , was: " + ((Descriptor)localObject).getFieldValue("descriptorType"));
/*     */     }
/*     */ 
/* 494 */     return localObject;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 504 */     paramObjectInputStream.defaultReadObject();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 513 */     if (compat)
/*     */     {
/* 517 */       ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 518 */       localPutField.put("attrDescriptor", this.attrDescriptor);
/* 519 */       localPutField.put("currClass", "ModelMBeanAttributeInfo");
/* 520 */       paramObjectOutputStream.writeFields();
/*     */     }
/*     */     else
/*     */     {
/* 526 */       paramObjectOutputStream.defaultWriteObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 154 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.serial.form");
/* 155 */       String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/* 156 */       compat = (str != null) && (str.equals("1.0"));
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 160 */     if (compat) {
/* 161 */       serialPersistentFields = oldSerialPersistentFields;
/* 162 */       serialVersionUID = 7098036920755973145L;
/*     */     } else {
/* 164 */       serialPersistentFields = newSerialPersistentFields;
/* 165 */       serialVersionUID = 6181543027787327345L;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.modelmbean.ModelMBeanAttributeInfo
 * JD-Core Version:    0.6.2
 */