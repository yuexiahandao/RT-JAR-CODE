/*      */ package javax.management.modelmbean;
/*      */ 
/*      */ import com.sun.jmx.defaults.JmxProperties;
/*      */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectInputStream.GetField;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.ObjectOutputStream.PutField;
/*      */ import java.io.ObjectStreamField;
/*      */ import java.security.AccessController;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.management.Descriptor;
/*      */ import javax.management.MBeanAttributeInfo;
/*      */ import javax.management.MBeanConstructorInfo;
/*      */ import javax.management.MBeanException;
/*      */ import javax.management.MBeanInfo;
/*      */ import javax.management.MBeanNotificationInfo;
/*      */ import javax.management.MBeanOperationInfo;
/*      */ import javax.management.RuntimeOperationsException;
/*      */ 
/*      */ public class ModelMBeanInfoSupport extends MBeanInfo
/*      */   implements ModelMBeanInfo
/*      */ {
/*      */   private static final long oldSerialVersionUID = -3944083498453227709L;
/*      */   private static final long newSerialVersionUID = -1935722590756516193L;
/*  100 */   private static final ObjectStreamField[] oldSerialPersistentFields = { new ObjectStreamField("modelMBeanDescriptor", Descriptor.class), new ObjectStreamField("mmbAttributes", [Ljavax.management.MBeanAttributeInfo.class), new ObjectStreamField("mmbConstructors", [Ljavax.management.MBeanConstructorInfo.class), new ObjectStreamField("mmbNotifications", [Ljavax.management.MBeanNotificationInfo.class), new ObjectStreamField("mmbOperations", [Ljavax.management.MBeanOperationInfo.class), new ObjectStreamField("currClass", String.class) };
/*      */ 
/*  111 */   private static final ObjectStreamField[] newSerialPersistentFields = { new ObjectStreamField("modelMBeanDescriptor", Descriptor.class), new ObjectStreamField("modelMBeanAttributes", [Ljavax.management.MBeanAttributeInfo.class), new ObjectStreamField("modelMBeanConstructors", [Ljavax.management.MBeanConstructorInfo.class), new ObjectStreamField("modelMBeanNotifications", [Ljavax.management.MBeanNotificationInfo.class), new ObjectStreamField("modelMBeanOperations", [Ljavax.management.MBeanOperationInfo.class) };
/*      */   private static final long serialVersionUID;
/*      */   private static final ObjectStreamField[] serialPersistentFields;
/*  139 */   private static boolean compat = false;
/*      */ 
/*  162 */   private Descriptor modelMBeanDescriptor = null;
/*      */   private MBeanAttributeInfo[] modelMBeanAttributes;
/*      */   private MBeanConstructorInfo[] modelMBeanConstructors;
/*      */   private MBeanNotificationInfo[] modelMBeanNotifications;
/*      */   private MBeanOperationInfo[] modelMBeanOperations;
/*      */   private static final String ATTR = "attribute";
/*      */   private static final String OPER = "operation";
/*      */   private static final String NOTF = "notification";
/*      */   private static final String CONS = "constructor";
/*      */   private static final String MMB = "mbean";
/*      */   private static final String ALL = "all";
/*      */   private static final String currClass = "ModelMBeanInfoSupport";
/*  343 */   private static final ModelMBeanAttributeInfo[] NO_ATTRIBUTES = new ModelMBeanAttributeInfo[0];
/*      */ 
/*  345 */   private static final ModelMBeanConstructorInfo[] NO_CONSTRUCTORS = new ModelMBeanConstructorInfo[0];
/*      */ 
/*  347 */   private static final ModelMBeanNotificationInfo[] NO_NOTIFICATIONS = new ModelMBeanNotificationInfo[0];
/*      */ 
/*  349 */   private static final ModelMBeanOperationInfo[] NO_OPERATIONS = new ModelMBeanOperationInfo[0];
/*      */ 
/*      */   public ModelMBeanInfoSupport(ModelMBeanInfo paramModelMBeanInfo)
/*      */   {
/*  217 */     super(paramModelMBeanInfo.getClassName(), paramModelMBeanInfo.getDescription(), paramModelMBeanInfo.getAttributes(), paramModelMBeanInfo.getConstructors(), paramModelMBeanInfo.getOperations(), paramModelMBeanInfo.getNotifications());
/*      */ 
/*  224 */     this.modelMBeanAttributes = paramModelMBeanInfo.getAttributes();
/*  225 */     this.modelMBeanConstructors = paramModelMBeanInfo.getConstructors();
/*  226 */     this.modelMBeanOperations = paramModelMBeanInfo.getOperations();
/*  227 */     this.modelMBeanNotifications = paramModelMBeanInfo.getNotifications();
/*      */     try
/*      */     {
/*  230 */       Descriptor localDescriptor = paramModelMBeanInfo.getMBeanDescriptor();
/*  231 */       this.modelMBeanDescriptor = validDescriptor(localDescriptor);
/*      */     } catch (MBeanException localMBeanException) {
/*  233 */       this.modelMBeanDescriptor = validDescriptor(null);
/*  234 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  235 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "ModelMBeanInfo(ModelMBeanInfo)", "Could not get a valid modelMBeanDescriptor, setting a default Descriptor");
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  243 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER))
/*  244 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "ModelMBeanInfo(ModelMBeanInfo)", "Exit");
/*      */   }
/*      */ 
/*      */   public ModelMBeanInfoSupport(String paramString1, String paramString2, ModelMBeanAttributeInfo[] paramArrayOfModelMBeanAttributeInfo, ModelMBeanConstructorInfo[] paramArrayOfModelMBeanConstructorInfo, ModelMBeanOperationInfo[] paramArrayOfModelMBeanOperationInfo, ModelMBeanNotificationInfo[] paramArrayOfModelMBeanNotificationInfo)
/*      */   {
/*  274 */     this(paramString1, paramString2, paramArrayOfModelMBeanAttributeInfo, paramArrayOfModelMBeanConstructorInfo, paramArrayOfModelMBeanOperationInfo, paramArrayOfModelMBeanNotificationInfo, null);
/*      */   }
/*      */ 
/*      */   public ModelMBeanInfoSupport(String paramString1, String paramString2, ModelMBeanAttributeInfo[] paramArrayOfModelMBeanAttributeInfo, ModelMBeanConstructorInfo[] paramArrayOfModelMBeanConstructorInfo, ModelMBeanOperationInfo[] paramArrayOfModelMBeanOperationInfo, ModelMBeanNotificationInfo[] paramArrayOfModelMBeanNotificationInfo, Descriptor paramDescriptor)
/*      */   {
/*  316 */     super(paramString1, paramString2, paramArrayOfModelMBeanAttributeInfo != null ? paramArrayOfModelMBeanAttributeInfo : NO_ATTRIBUTES, paramArrayOfModelMBeanConstructorInfo != null ? paramArrayOfModelMBeanConstructorInfo : NO_CONSTRUCTORS, paramArrayOfModelMBeanOperationInfo != null ? paramArrayOfModelMBeanOperationInfo : NO_OPERATIONS, paramArrayOfModelMBeanNotificationInfo != null ? paramArrayOfModelMBeanNotificationInfo : NO_NOTIFICATIONS);
/*      */ 
/*  328 */     this.modelMBeanAttributes = paramArrayOfModelMBeanAttributeInfo;
/*  329 */     this.modelMBeanConstructors = paramArrayOfModelMBeanConstructorInfo;
/*  330 */     this.modelMBeanOperations = paramArrayOfModelMBeanOperationInfo;
/*  331 */     this.modelMBeanNotifications = paramArrayOfModelMBeanNotificationInfo;
/*  332 */     this.modelMBeanDescriptor = validDescriptor(paramDescriptor);
/*  333 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER))
/*  334 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "ModelMBeanInfoSupport(String,String,ModelMBeanAttributeInfo[],ModelMBeanConstructorInfo[],ModelMBeanOperationInfo[],ModelMBeanNotificationInfo[],Descriptor)", "Exit");
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*  364 */     return new ModelMBeanInfoSupport(this);
/*      */   }
/*      */ 
/*      */   public Descriptor[] getDescriptors(String paramString)
/*      */     throws MBeanException, RuntimeOperationsException
/*      */   {
/*  370 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  371 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getDescriptors(String)", "Entry");
/*      */     }
/*      */ 
/*  376 */     if ((paramString == null) || (paramString.equals("")))
/*  377 */       paramString = "all";
/*      */     Descriptor[] arrayOfDescriptor;
/*  384 */     if (paramString.equalsIgnoreCase("mbean")) {
/*  385 */       arrayOfDescriptor = new Descriptor[] { this.modelMBeanDescriptor };
/*      */     }
/*      */     else
/*      */     {
/*      */       Object localObject;
/*      */       int i;
/*      */       int j;
/*  386 */       if (paramString.equalsIgnoreCase("attribute")) {
/*  387 */         localObject = this.modelMBeanAttributes;
/*  388 */         i = 0;
/*  389 */         if (localObject != null) i = localObject.length;
/*      */ 
/*  391 */         arrayOfDescriptor = new Descriptor[i];
/*  392 */         for (j = 0; j < i; j++) {
/*  393 */           arrayOfDescriptor[j] = ((ModelMBeanAttributeInfo)localObject[j]).getDescriptor();
/*      */         }
/*      */       }
/*  396 */       else if (paramString.equalsIgnoreCase("operation")) {
/*  397 */         localObject = this.modelMBeanOperations;
/*  398 */         i = 0;
/*  399 */         if (localObject != null) i = localObject.length;
/*      */ 
/*  401 */         arrayOfDescriptor = new Descriptor[i];
/*  402 */         for (j = 0; j < i; j++) {
/*  403 */           arrayOfDescriptor[j] = ((ModelMBeanOperationInfo)localObject[j]).getDescriptor();
/*      */         }
/*      */       }
/*  406 */       else if (paramString.equalsIgnoreCase("constructor")) {
/*  407 */         localObject = this.modelMBeanConstructors;
/*  408 */         i = 0;
/*  409 */         if (localObject != null) i = localObject.length;
/*      */ 
/*  411 */         arrayOfDescriptor = new Descriptor[i];
/*  412 */         for (j = 0; j < i; j++) {
/*  413 */           arrayOfDescriptor[j] = ((ModelMBeanConstructorInfo)localObject[j]).getDescriptor();
/*      */         }
/*      */       }
/*  416 */       else if (paramString.equalsIgnoreCase("notification")) {
/*  417 */         localObject = this.modelMBeanNotifications;
/*  418 */         i = 0;
/*  419 */         if (localObject != null) i = localObject.length;
/*      */ 
/*  421 */         arrayOfDescriptor = new Descriptor[i];
/*  422 */         for (j = 0; j < i; j++) {
/*  423 */           arrayOfDescriptor[j] = ((ModelMBeanNotificationInfo)localObject[j]).getDescriptor();
/*      */         }
/*      */       }
/*  426 */       else if (paramString.equalsIgnoreCase("all"))
/*      */       {
/*  428 */         localObject = this.modelMBeanAttributes;
/*  429 */         i = 0;
/*  430 */         if (localObject != null) i = localObject.length;
/*      */ 
/*  432 */         MBeanOperationInfo[] arrayOfMBeanOperationInfo = this.modelMBeanOperations;
/*  433 */         int k = 0;
/*  434 */         if (arrayOfMBeanOperationInfo != null) k = arrayOfMBeanOperationInfo.length;
/*      */ 
/*  436 */         MBeanConstructorInfo[] arrayOfMBeanConstructorInfo = this.modelMBeanConstructors;
/*  437 */         int m = 0;
/*  438 */         if (arrayOfMBeanConstructorInfo != null) m = arrayOfMBeanConstructorInfo.length;
/*      */ 
/*  440 */         MBeanNotificationInfo[] arrayOfMBeanNotificationInfo = this.modelMBeanNotifications;
/*  441 */         int n = 0;
/*  442 */         if (arrayOfMBeanNotificationInfo != null) n = arrayOfMBeanNotificationInfo.length;
/*      */ 
/*  444 */         int i1 = i + m + k + n + 1;
/*  445 */         arrayOfDescriptor = new Descriptor[i1];
/*      */ 
/*  447 */         arrayOfDescriptor[(i1 - 1)] = this.modelMBeanDescriptor;
/*      */ 
/*  449 */         int i2 = 0;
/*  450 */         for (int i3 = 0; i3 < i; i3++) {
/*  451 */           arrayOfDescriptor[i2] = ((ModelMBeanAttributeInfo)localObject[i3]).getDescriptor();
/*      */ 
/*  453 */           i2++;
/*      */         }
/*  455 */         for (i3 = 0; i3 < m; i3++) {
/*  456 */           arrayOfDescriptor[i2] = ((ModelMBeanConstructorInfo)arrayOfMBeanConstructorInfo[i3]).getDescriptor();
/*      */ 
/*  458 */           i2++;
/*      */         }
/*  460 */         for (i3 = 0; i3 < k; i3++) {
/*  461 */           arrayOfDescriptor[i2] = ((ModelMBeanOperationInfo)arrayOfMBeanOperationInfo[i3]).getDescriptor();
/*      */ 
/*  463 */           i2++;
/*      */         }
/*  465 */         for (i3 = 0; i3 < n; i3++) {
/*  466 */           arrayOfDescriptor[i2] = ((ModelMBeanNotificationInfo)arrayOfMBeanNotificationInfo[i3]).getDescriptor();
/*      */ 
/*  468 */           i2++;
/*      */         }
/*      */       } else {
/*  471 */         localObject = new IllegalArgumentException("Descriptor Type is invalid");
/*      */ 
/*  475 */         throw new RuntimeOperationsException((RuntimeException)localObject, "Exception occurred trying to find the descriptors of the MBean");
/*      */       }
/*      */     }
/*  477 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  478 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getDescriptors(String)", "Exit");
/*      */     }
/*      */ 
/*  483 */     return arrayOfDescriptor;
/*      */   }
/*      */ 
/*      */   public void setDescriptors(Descriptor[] paramArrayOfDescriptor)
/*      */     throws MBeanException, RuntimeOperationsException
/*      */   {
/*  489 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  490 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "setDescriptors(Descriptor[])", "Entry");
/*      */     }
/*      */ 
/*  494 */     if (paramArrayOfDescriptor == null)
/*      */     {
/*  496 */       throw new RuntimeOperationsException(new IllegalArgumentException("Descriptor list is invalid"), "Exception occurred trying to set the descriptors of the MBeanInfo");
/*      */     }
/*      */ 
/*  501 */     if (paramArrayOfDescriptor.length == 0) {
/*  502 */       return;
/*      */     }
/*  504 */     for (int i = 0; i < paramArrayOfDescriptor.length; i++) {
/*  505 */       setDescriptor(paramArrayOfDescriptor[i], null);
/*      */     }
/*  507 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER))
/*  508 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "setDescriptors(Descriptor[])", "Exit");
/*      */   }
/*      */ 
/*      */   public Descriptor getDescriptor(String paramString)
/*      */     throws MBeanException, RuntimeOperationsException
/*      */   {
/*  533 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  534 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getDescriptor(String)", "Entry");
/*      */     }
/*      */ 
/*  538 */     return getDescriptor(paramString, null);
/*      */   }
/*      */ 
/*      */   public Descriptor getDescriptor(String paramString1, String paramString2)
/*      */     throws MBeanException, RuntimeOperationsException
/*      */   {
/*  545 */     if (paramString1 == null)
/*      */     {
/*  547 */       throw new RuntimeOperationsException(new IllegalArgumentException("Descriptor is invalid"), "Exception occurred trying to set the descriptors of the MBeanInfo");
/*      */     }
/*      */ 
/*  553 */     if ("mbean".equalsIgnoreCase(paramString2))
/*  554 */       return (Descriptor)this.modelMBeanDescriptor.clone();
/*      */     Object localObject;
/*  564 */     if (("attribute".equalsIgnoreCase(paramString2)) || (paramString2 == null)) {
/*  565 */       localObject = getAttribute(paramString1);
/*  566 */       if (localObject != null)
/*  567 */         return ((ModelMBeanAttributeInfo)localObject).getDescriptor();
/*  568 */       if (paramString2 != null)
/*  569 */         return null;
/*      */     }
/*  571 */     if (("operation".equalsIgnoreCase(paramString2)) || (paramString2 == null)) {
/*  572 */       localObject = getOperation(paramString1);
/*  573 */       if (localObject != null)
/*  574 */         return ((ModelMBeanOperationInfo)localObject).getDescriptor();
/*  575 */       if (paramString2 != null)
/*  576 */         return null;
/*      */     }
/*  578 */     if (("constructor".equalsIgnoreCase(paramString2)) || (paramString2 == null)) {
/*  579 */       localObject = getConstructor(paramString1);
/*      */ 
/*  581 */       if (localObject != null)
/*  582 */         return ((ModelMBeanConstructorInfo)localObject).getDescriptor();
/*  583 */       if (paramString2 != null)
/*  584 */         return null;
/*      */     }
/*  586 */     if (("notification".equalsIgnoreCase(paramString2)) || (paramString2 == null)) {
/*  587 */       localObject = getNotification(paramString1);
/*      */ 
/*  589 */       if (localObject != null)
/*  590 */         return ((ModelMBeanNotificationInfo)localObject).getDescriptor();
/*  591 */       if (paramString2 != null)
/*  592 */         return null;
/*      */     }
/*  594 */     if (paramString2 == null)
/*  595 */       return null;
/*  596 */     throw new RuntimeOperationsException(new IllegalArgumentException("Descriptor Type is invalid"), "Exception occurred trying to find the descriptors of the MBean");
/*      */   }
/*      */ 
/*      */   public void setDescriptor(Descriptor paramDescriptor, String paramString)
/*      */     throws MBeanException, RuntimeOperationsException
/*      */   {
/*  609 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  610 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "setDescriptor(Descriptor,String)", "Entry");
/*      */     }
/*      */ 
/*  615 */     if (paramDescriptor == null) {
/*  616 */       paramDescriptor = new DescriptorSupport();
/*      */     }
/*      */ 
/*  619 */     if ((paramString == null) || (paramString.equals(""))) {
/*  620 */       paramString = (String)paramDescriptor.getFieldValue("descriptorType");
/*      */ 
/*  623 */       if (paramString == null) {
/*  624 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "setDescriptor(Descriptor,String)", "descriptorType null in both String parameter and Descriptor, defaulting to mbean");
/*      */ 
/*  628 */         paramString = "mbean";
/*      */       }
/*      */     }
/*      */ 
/*  632 */     String str = (String)paramDescriptor.getFieldValue("name");
/*      */ 
/*  634 */     if (str == null) {
/*  635 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "setDescriptor(Descriptor,String)", "descriptor name null, defaulting to " + getClassName());
/*      */ 
/*  639 */       str = getClassName();
/*      */     }
/*  641 */     int i = 0;
/*      */     Object localObject1;
/*  642 */     if (paramString.equalsIgnoreCase("mbean")) {
/*  643 */       setMBeanDescriptor(paramDescriptor);
/*  644 */       i = 1;
/*      */     }
/*      */     else
/*      */     {
/*      */       int j;
/*      */       int k;
/*      */       Object localObject2;
/*  645 */       if (paramString.equalsIgnoreCase("attribute")) {
/*  646 */         localObject1 = this.modelMBeanAttributes;
/*  647 */         j = 0;
/*  648 */         if (localObject1 != null) j = localObject1.length;
/*      */ 
/*  650 */         for (k = 0; k < j; k++) {
/*  651 */           if (str.equals(localObject1[k].getName())) {
/*  652 */             i = 1;
/*  653 */             localObject2 = (ModelMBeanAttributeInfo)localObject1[k];
/*      */ 
/*  655 */             ((ModelMBeanAttributeInfo)localObject2).setDescriptor(paramDescriptor);
/*  656 */             if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  657 */               StringBuilder localStringBuilder = new StringBuilder().append("Setting descriptor to ").append(paramDescriptor).append("\t\n local: AttributeInfo descriptor is ").append(((ModelMBeanAttributeInfo)localObject2).getDescriptor()).append("\t\n modelMBeanInfo: AttributeInfo descriptor is ").append(getDescriptor(str, "attribute"));
/*      */ 
/*  663 */               JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "setDescriptor(Descriptor,String)", localStringBuilder.toString());
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*  670 */       else if (paramString.equalsIgnoreCase("operation")) {
/*  671 */         localObject1 = this.modelMBeanOperations;
/*  672 */         j = 0;
/*  673 */         if (localObject1 != null) j = localObject1.length;
/*      */ 
/*  675 */         for (k = 0; k < j; k++)
/*  676 */           if (str.equals(localObject1[k].getName())) {
/*  677 */             i = 1;
/*  678 */             localObject2 = (ModelMBeanOperationInfo)localObject1[k];
/*      */ 
/*  680 */             ((ModelMBeanOperationInfo)localObject2).setDescriptor(paramDescriptor);
/*      */           }
/*      */       }
/*  683 */       else if (paramString.equalsIgnoreCase("constructor")) {
/*  684 */         localObject1 = this.modelMBeanConstructors;
/*  685 */         j = 0;
/*  686 */         if (localObject1 != null) j = localObject1.length;
/*      */ 
/*  688 */         for (k = 0; k < j; k++)
/*  689 */           if (str.equals(localObject1[k].getName())) {
/*  690 */             i = 1;
/*  691 */             localObject2 = (ModelMBeanConstructorInfo)localObject1[k];
/*      */ 
/*  693 */             ((ModelMBeanConstructorInfo)localObject2).setDescriptor(paramDescriptor);
/*      */           }
/*      */       }
/*  696 */       else if (paramString.equalsIgnoreCase("notification")) {
/*  697 */         localObject1 = this.modelMBeanNotifications;
/*  698 */         j = 0;
/*  699 */         if (localObject1 != null) j = localObject1.length;
/*      */ 
/*  701 */         for (k = 0; k < j; k++)
/*  702 */           if (str.equals(localObject1[k].getName())) {
/*  703 */             i = 1;
/*  704 */             localObject2 = (ModelMBeanNotificationInfo)localObject1[k];
/*      */ 
/*  706 */             ((ModelMBeanNotificationInfo)localObject2).setDescriptor(paramDescriptor);
/*      */           }
/*      */       }
/*      */       else {
/*  710 */         localObject1 = new IllegalArgumentException("Invalid descriptor type: " + paramString);
/*      */ 
/*  713 */         throw new RuntimeOperationsException((RuntimeException)localObject1, "Exception occurred trying to set the descriptors of the MBean");
/*      */       }
/*      */     }
/*  716 */     if (i == 0) {
/*  717 */       localObject1 = new IllegalArgumentException("Descriptor name is invalid: type=" + paramString + "; name=" + str);
/*      */ 
/*  721 */       throw new RuntimeOperationsException((RuntimeException)localObject1, "Exception occurred trying to set the descriptors of the MBean");
/*      */     }
/*  723 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER))
/*  724 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "setDescriptor(Descriptor,String)", "Exit");
/*      */   }
/*      */ 
/*      */   public ModelMBeanAttributeInfo getAttribute(String paramString)
/*      */     throws MBeanException, RuntimeOperationsException
/*      */   {
/*  734 */     ModelMBeanAttributeInfo localModelMBeanAttributeInfo = null;
/*  735 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  736 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getAttribute(String)", "Entry");
/*      */     }
/*      */ 
/*  740 */     if (paramString == null) {
/*  741 */       throw new RuntimeOperationsException(new IllegalArgumentException("Attribute Name is null"), "Exception occurred trying to get the ModelMBeanAttributeInfo of the MBean");
/*      */     }
/*      */ 
/*  746 */     MBeanAttributeInfo[] arrayOfMBeanAttributeInfo = this.modelMBeanAttributes;
/*  747 */     int i = 0;
/*  748 */     if (arrayOfMBeanAttributeInfo != null) i = arrayOfMBeanAttributeInfo.length;
/*      */ 
/*  750 */     for (int j = 0; (j < i) && (localModelMBeanAttributeInfo == null); j++) {
/*  751 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  752 */         StringBuilder localStringBuilder = new StringBuilder().append("\t\n this.getAttributes() MBeanAttributeInfo Array ").append(j).append(":").append(((ModelMBeanAttributeInfo)arrayOfMBeanAttributeInfo[j]).getDescriptor()).append("\t\n this.modelMBeanAttributes MBeanAttributeInfo Array ").append(j).append(":").append(((ModelMBeanAttributeInfo)this.modelMBeanAttributes[j]).getDescriptor());
/*      */ 
/*  759 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getAttribute(String)", localStringBuilder.toString());
/*      */       }
/*      */ 
/*  763 */       if (paramString.equals(arrayOfMBeanAttributeInfo[j].getName())) {
/*  764 */         localModelMBeanAttributeInfo = (ModelMBeanAttributeInfo)arrayOfMBeanAttributeInfo[j].clone();
/*      */       }
/*      */     }
/*  767 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  768 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getAttribute(String)", "Exit");
/*      */     }
/*      */ 
/*  773 */     return localModelMBeanAttributeInfo;
/*      */   }
/*      */ 
/*      */   public ModelMBeanOperationInfo getOperation(String paramString)
/*      */     throws MBeanException, RuntimeOperationsException
/*      */   {
/*  780 */     ModelMBeanOperationInfo localModelMBeanOperationInfo = null;
/*  781 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  782 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getOperation(String)", "Entry");
/*      */     }
/*      */ 
/*  786 */     if (paramString == null) {
/*  787 */       throw new RuntimeOperationsException(new IllegalArgumentException("inName is null"), "Exception occurred trying to get the ModelMBeanOperationInfo of the MBean");
/*      */     }
/*      */ 
/*  792 */     MBeanOperationInfo[] arrayOfMBeanOperationInfo = this.modelMBeanOperations;
/*  793 */     int i = 0;
/*  794 */     if (arrayOfMBeanOperationInfo != null) i = arrayOfMBeanOperationInfo.length;
/*      */ 
/*  796 */     for (int j = 0; (j < i) && (localModelMBeanOperationInfo == null); j++) {
/*  797 */       if (paramString.equals(arrayOfMBeanOperationInfo[j].getName())) {
/*  798 */         localModelMBeanOperationInfo = (ModelMBeanOperationInfo)arrayOfMBeanOperationInfo[j].clone();
/*      */       }
/*      */     }
/*  801 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  802 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getOperation(String)", "Exit");
/*      */     }
/*      */ 
/*  807 */     return localModelMBeanOperationInfo;
/*      */   }
/*      */ 
/*      */   public ModelMBeanConstructorInfo getConstructor(String paramString)
/*      */     throws MBeanException, RuntimeOperationsException
/*      */   {
/*  826 */     ModelMBeanConstructorInfo localModelMBeanConstructorInfo = null;
/*  827 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  828 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getConstructor(String)", "Entry");
/*      */     }
/*      */ 
/*  832 */     if (paramString == null) {
/*  833 */       throw new RuntimeOperationsException(new IllegalArgumentException("Constructor name is null"), "Exception occurred trying to get the ModelMBeanConstructorInfo of the MBean");
/*      */     }
/*      */ 
/*  838 */     MBeanConstructorInfo[] arrayOfMBeanConstructorInfo = this.modelMBeanConstructors;
/*  839 */     int i = 0;
/*  840 */     if (arrayOfMBeanConstructorInfo != null) i = arrayOfMBeanConstructorInfo.length;
/*      */ 
/*  842 */     for (int j = 0; (j < i) && (localModelMBeanConstructorInfo == null); j++) {
/*  843 */       if (paramString.equals(arrayOfMBeanConstructorInfo[j].getName())) {
/*  844 */         localModelMBeanConstructorInfo = (ModelMBeanConstructorInfo)arrayOfMBeanConstructorInfo[j].clone();
/*      */       }
/*      */     }
/*  847 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  848 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getConstructor(String)", "Exit");
/*      */     }
/*      */ 
/*  853 */     return localModelMBeanConstructorInfo;
/*      */   }
/*      */ 
/*      */   public ModelMBeanNotificationInfo getNotification(String paramString)
/*      */     throws MBeanException, RuntimeOperationsException
/*      */   {
/*  859 */     ModelMBeanNotificationInfo localModelMBeanNotificationInfo = null;
/*  860 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  861 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getNotification(String)", "Entry");
/*      */     }
/*      */ 
/*  865 */     if (paramString == null) {
/*  866 */       throw new RuntimeOperationsException(new IllegalArgumentException("Notification name is null"), "Exception occurred trying to get the ModelMBeanNotificationInfo of the MBean");
/*      */     }
/*      */ 
/*  871 */     MBeanNotificationInfo[] arrayOfMBeanNotificationInfo = this.modelMBeanNotifications;
/*  872 */     int i = 0;
/*  873 */     if (arrayOfMBeanNotificationInfo != null) i = arrayOfMBeanNotificationInfo.length;
/*      */ 
/*  875 */     for (int j = 0; (j < i) && (localModelMBeanNotificationInfo == null); j++) {
/*  876 */       if (paramString.equals(arrayOfMBeanNotificationInfo[j].getName())) {
/*  877 */         localModelMBeanNotificationInfo = (ModelMBeanNotificationInfo)arrayOfMBeanNotificationInfo[j].clone();
/*      */       }
/*      */     }
/*  880 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  881 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getNotification(String)", "Exit");
/*      */     }
/*      */ 
/*  886 */     return localModelMBeanNotificationInfo;
/*      */   }
/*      */ 
/*      */   public Descriptor getDescriptor()
/*      */   {
/*  896 */     return getMBeanDescriptorNoException();
/*      */   }
/*      */ 
/*      */   public Descriptor getMBeanDescriptor() throws MBeanException {
/*  900 */     return getMBeanDescriptorNoException();
/*      */   }
/*      */ 
/*      */   private Descriptor getMBeanDescriptorNoException() {
/*  904 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  905 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getMBeanDescriptorNoException()", "Entry");
/*      */     }
/*      */ 
/*  910 */     if (this.modelMBeanDescriptor == null) {
/*  911 */       this.modelMBeanDescriptor = validDescriptor(null);
/*      */     }
/*  913 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  914 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "getMBeanDescriptorNoException()", "Exit, returning: " + this.modelMBeanDescriptor);
/*      */     }
/*      */ 
/*  919 */     return (Descriptor)this.modelMBeanDescriptor.clone();
/*      */   }
/*      */ 
/*      */   public void setMBeanDescriptor(Descriptor paramDescriptor) throws MBeanException, RuntimeOperationsException
/*      */   {
/*  924 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINER)) {
/*  925 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINER, ModelMBeanInfoSupport.class.getName(), "setMBeanDescriptor(Descriptor)", "Entry");
/*      */     }
/*      */ 
/*  929 */     this.modelMBeanDescriptor = validDescriptor(paramDescriptor);
/*      */   }
/*      */ 
/*      */   private Descriptor validDescriptor(Descriptor paramDescriptor)
/*      */     throws RuntimeOperationsException
/*      */   {
/*  948 */     int i = paramDescriptor == null ? 1 : 0;
/*      */     Object localObject;
/*  949 */     if (i != 0) {
/*  950 */       localObject = new DescriptorSupport();
/*  951 */       JmxProperties.MODELMBEAN_LOGGER.finer("Null Descriptor, creating new.");
/*      */     } else {
/*  953 */       localObject = (Descriptor)paramDescriptor.clone();
/*      */     }
/*      */ 
/*  957 */     if ((i != 0) && (((Descriptor)localObject).getFieldValue("name") == null)) {
/*  958 */       ((Descriptor)localObject).setField("name", getClassName());
/*  959 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor name to " + getClassName());
/*      */     }
/*  961 */     if ((i != 0) && (((Descriptor)localObject).getFieldValue("descriptorType") == null)) {
/*  962 */       ((Descriptor)localObject).setField("descriptorType", "mbean");
/*  963 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting descriptorType to \"mbean\"");
/*      */     }
/*  965 */     if (((Descriptor)localObject).getFieldValue("displayName") == null) {
/*  966 */       ((Descriptor)localObject).setField("displayName", getClassName());
/*  967 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor displayName to " + getClassName());
/*      */     }
/*  969 */     if (((Descriptor)localObject).getFieldValue("persistPolicy") == null) {
/*  970 */       ((Descriptor)localObject).setField("persistPolicy", "never");
/*  971 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor persistPolicy to \"never\"");
/*      */     }
/*  973 */     if (((Descriptor)localObject).getFieldValue("log") == null) {
/*  974 */       ((Descriptor)localObject).setField("log", "F");
/*  975 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor \"log\" field to \"F\"");
/*      */     }
/*  977 */     if (((Descriptor)localObject).getFieldValue("visibility") == null) {
/*  978 */       ((Descriptor)localObject).setField("visibility", "1");
/*  979 */       JmxProperties.MODELMBEAN_LOGGER.finer("Defaulting Descriptor visibility to 1");
/*      */     }
/*      */ 
/*  983 */     if (!((Descriptor)localObject).isValid()) {
/*  984 */       throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The isValid() method of the Descriptor object itself returned false,one or more required fields are invalid. Descriptor:" + localObject.toString());
/*      */     }
/*      */ 
/*  989 */     if (!((String)((Descriptor)localObject).getFieldValue("descriptorType")).equalsIgnoreCase("mbean")) {
/*  990 */       throw new RuntimeOperationsException(new IllegalArgumentException("Invalid Descriptor argument"), "The Descriptor \"descriptorType\" field does not match the object described.  Expected: mbean , was: " + ((Descriptor)localObject).getFieldValue("descriptorType"));
/*      */     }
/*      */ 
/*  995 */     return localObject;
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1006 */     if (compat)
/*      */     {
/* 1009 */       ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 1010 */       this.modelMBeanDescriptor = ((Descriptor)localGetField.get("modelMBeanDescriptor", null));
/*      */ 
/* 1012 */       if (localGetField.defaulted("modelMBeanDescriptor")) {
/* 1013 */         throw new NullPointerException("modelMBeanDescriptor");
/*      */       }
/* 1015 */       this.modelMBeanAttributes = ((MBeanAttributeInfo[])localGetField.get("mmbAttributes", null));
/*      */ 
/* 1017 */       if (localGetField.defaulted("mmbAttributes")) {
/* 1018 */         throw new NullPointerException("mmbAttributes");
/*      */       }
/* 1020 */       this.modelMBeanConstructors = ((MBeanConstructorInfo[])localGetField.get("mmbConstructors", null));
/*      */ 
/* 1022 */       if (localGetField.defaulted("mmbConstructors")) {
/* 1023 */         throw new NullPointerException("mmbConstructors");
/*      */       }
/* 1025 */       this.modelMBeanNotifications = ((MBeanNotificationInfo[])localGetField.get("mmbNotifications", null));
/*      */ 
/* 1027 */       if (localGetField.defaulted("mmbNotifications")) {
/* 1028 */         throw new NullPointerException("mmbNotifications");
/*      */       }
/* 1030 */       this.modelMBeanOperations = ((MBeanOperationInfo[])localGetField.get("mmbOperations", null));
/*      */ 
/* 1032 */       if (localGetField.defaulted("mmbOperations")) {
/* 1033 */         throw new NullPointerException("mmbOperations");
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1038 */       paramObjectInputStream.defaultReadObject();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1048 */     if (compat)
/*      */     {
/* 1051 */       ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 1052 */       localPutField.put("modelMBeanDescriptor", this.modelMBeanDescriptor);
/* 1053 */       localPutField.put("mmbAttributes", this.modelMBeanAttributes);
/* 1054 */       localPutField.put("mmbConstructors", this.modelMBeanConstructors);
/* 1055 */       localPutField.put("mmbNotifications", this.modelMBeanNotifications);
/* 1056 */       localPutField.put("mmbOperations", this.modelMBeanOperations);
/* 1057 */       localPutField.put("currClass", "ModelMBeanInfoSupport");
/* 1058 */       paramObjectOutputStream.writeFields();
/*      */     }
/*      */     else
/*      */     {
/* 1062 */       paramObjectOutputStream.defaultWriteObject();
/*      */     }
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/*  142 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.serial.form");
/*  143 */       String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/*  144 */       compat = (str != null) && (str.equals("1.0"));
/*      */     }
/*      */     catch (Exception localException) {
/*      */     }
/*  148 */     if (compat) {
/*  149 */       serialPersistentFields = oldSerialPersistentFields;
/*  150 */       serialVersionUID = -3944083498453227709L;
/*      */     } else {
/*  152 */       serialPersistentFields = newSerialPersistentFields;
/*  153 */       serialVersionUID = -1935722590756516193L;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.modelmbean.ModelMBeanInfoSupport
 * JD-Core Version:    0.6.2
 */