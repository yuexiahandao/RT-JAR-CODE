/*     */ package javax.management.modelmbean;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.ObjectOutputStream.PutField;
/*     */ import java.io.ObjectStreamField;
/*     */ import java.security.AccessController;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.management.Descriptor;
/*     */ import javax.management.DescriptorAccess;
/*     */ import javax.management.MBeanNotificationInfo;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ 
/*     */ public class ModelMBeanNotificationInfo extends MBeanNotificationInfo
/*     */   implements DescriptorAccess
/*     */ {
/*     */   private static final long oldSerialVersionUID = -5211564525059047097L;
/*     */   private static final long newSerialVersionUID = -7445681389570207141L;
/* 113 */   private static final ObjectStreamField[] oldSerialPersistentFields = { new ObjectStreamField("notificationDescriptor", Descriptor.class), new ObjectStreamField("currClass", String.class) };
/*     */ 
/* 120 */   private static final ObjectStreamField[] newSerialPersistentFields = { new ObjectStreamField("notificationDescriptor", Descriptor.class) };
/*     */   private static final long serialVersionUID;
/*     */   private static final ObjectStreamField[] serialPersistentFields;
/* 132 */   private static boolean compat = false;
/*     */   private Descriptor notificationDescriptor;
/*     */   private static final String currClass = "ModelMBeanNotificationInfo";
/*     */ 
/*     */   public ModelMBeanNotificationInfo(String[] paramArrayOfString, String paramString1, String paramString2)
/*     */   {
/* 173 */     this(paramArrayOfString, paramString1, paramString2, null);
/*     */   }
/*     */ 
/*     */   public ModelMBeanNotificationInfo(String[] paramArrayOfString, String paramString1, String paramString2, Descriptor paramDescriptor)
/*     */   {
/* 201 */     super(paramArrayOfString, paramString1, paramString2);
/* 202 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 203 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanNotificationInfo.class.getName(), "ModelMBeanNotificationInfo", "Entry");
/*     */     }
/*     */ 
/* 207 */     this.notificationDescriptor = validDescriptor(paramDescriptor);
/*     */   }
/*     */ 
/*     */   public ModelMBeanNotificationInfo(ModelMBeanNotificationInfo paramModelMBeanNotificationInfo)
/*     */   {
/* 218 */     this(paramModelMBeanNotificationInfo.getNotifTypes(), paramModelMBeanNotificationInfo.getName(), paramModelMBeanNotificationInfo.getDescription(), paramModelMBeanNotificationInfo.getDescriptor());
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 228 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 229 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanNotificationInfo.class.getName(), "clone()", "Entry");
/*     */     }
/*     */ 
/* 233 */     return new ModelMBeanNotificationInfo(this);
/*     */   }
/*     */ 
/*     */   public Descriptor getDescriptor()
/*     */   {
/* 246 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 247 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanNotificationInfo.class.getName(), "getDescriptor()", "Entry");
/*     */     }
/*     */ 
/* 252 */     if (this.notificationDescriptor == null)
/*     */     {
/* 254 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 255 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanNotificationInfo.class.getName(), "getDescriptor()", "Descriptor value is null, setting descriptor to default values");
/*     */       }
/*     */ 
/* 260 */       this.notificationDescriptor = validDescriptor(null);
/*     */     }
/*     */ 
/* 263 */     return (Descriptor)this.notificationDescriptor.clone();
/*     */   }
/*     */ 
/*     */   public void setDescriptor(Descriptor paramDescriptor)
/*     */   {
/* 284 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 285 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanNotificationInfo.class.getName(), "setDescriptor(Descriptor)", "Entry");
/*     */     }
/*     */ 
/* 289 */     this.notificationDescriptor = validDescriptor(paramDescriptor);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 299 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/* 300 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanNotificationInfo.class.getName(), "toString()", "Entry");
/*     */     }
/*     */ 
/* 305 */     StringBuilder localStringBuilder = new StringBuilder();
/*     */ 
/* 307 */     localStringBuilder.append("ModelMBeanNotificationInfo: ").append(getName());
/*     */ 
/* 310 */     localStringBuilder.append(" ; Description: ").append(getDescription());
/*     */ 
/* 313 */     localStringBuilder.append(" ; Descriptor: ").append(getDescriptor());
/*     */ 
/* 316 */     localStringBuilder.append(" ; Types: ");
/* 317 */     String[] arrayOfString = getNotifTypes();
/* 318 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 319 */       if (i > 0) localStringBuilder.append(", ");
/* 320 */       localStringBuilder.append(arrayOfString[i]);
/*     */     }
/* 322 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private Descriptor validDescriptor(Descriptor paramDescriptor)
/*     */     throws RuntimeOperationsException
/*     */   {
/* 342 */     int i = paramDescriptor == null ? 1 : 0;
/*     */     Object localObject;
/* 343 */     if (i != 0) {
/* 344 */       localObject = new DescriptorSupport();
/* 345 */       JmxProperties.MODELMBEAN_LOGGER.finer("Null Descriptor, creating new.");
/*     */     } else {
/* 347 */       localObject = (Descriptor)paramDescriptor.clone();
/*     */     }
/*     */ 
/* 351 */     if ((i != 0) && (((Descriptor)localObject).getFieldValue("name") == null)) {
/* 352 */       ((Descriptor)localObject).setField("name", getName());
/* 353 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor name to " + getName());
/*     */     }
/* 355 */     if ((i != 0) && (((Descriptor)localObject).getFieldValue("descriptorType") == null)) {
/* 356 */       ((Descriptor)localObject).setField("descriptorType", "notification");
/* 357 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting descriptorType to \"notification\"");
/*     */     }
/* 359 */     if (((Descriptor)localObject).getFieldValue("displayName") == null) {
/* 360 */       ((Descriptor)localObject).setField("displayName", getName());
/* 361 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor displayName to " + getName());
/*     */     }
/* 363 */     if (((Descriptor)localObject).getFieldValue("severity") == null) {
/* 364 */       ((Descriptor)localObject).setField("severity", "6");
/* 365 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor severity field to 6");
/*     */     }
/*     */ 
/* 369 */     if (!((Descriptor)localObject).isValid()) {
/* 370 */       throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The isValid() method of the Descriptor object itself returned false,one or more required fields are invalid. Descriptor:" + localObject.toString());
/*     */     }
/*     */ 
/* 374 */     if (!getName().equalsIgnoreCase((String)((Descriptor)localObject).getFieldValue("name"))) {
/* 375 */       throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor \"name\" field does not match the object described.  Expected: " + getName() + " , was: " + ((Descriptor)localObject).getFieldValue("name"));
/*     */     }
/*     */ 
/* 379 */     if (!"notification".equalsIgnoreCase((String)((Descriptor)localObject).getFieldValue("descriptorType"))) {
/* 380 */       throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor \"descriptorType\" field does not match the object described.  Expected: \"notification\" , was: " + ((Descriptor)localObject).getFieldValue("descriptorType"));
/*     */     }
/*     */ 
/* 385 */     return localObject;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 396 */     paramObjectInputStream.defaultReadObject();
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 406 */     if (compat)
/*     */     {
/* 409 */       ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 410 */       localPutField.put("notificationDescriptor", this.notificationDescriptor);
/* 411 */       localPutField.put("currClass", "ModelMBeanNotificationInfo");
/* 412 */       paramObjectOutputStream.writeFields();
/*     */     }
/*     */     else
/*     */     {
/* 416 */       paramObjectOutputStream.defaultWriteObject();
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 135 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.serial.form");
/* 136 */       String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/* 137 */       compat = (str != null) && (str.equals("1.0"));
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 141 */     if (compat) {
/* 142 */       serialPersistentFields = oldSerialPersistentFields;
/* 143 */       serialVersionUID = -5211564525059047097L;
/*     */     } else {
/* 145 */       serialPersistentFields = newSerialPersistentFields;
/* 146 */       serialVersionUID = -7445681389570207141L;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.modelmbean.ModelMBeanNotificationInfo
 * JD-Core Version:    0.6.2
 */