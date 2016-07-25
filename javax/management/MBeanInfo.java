/*     */ package javax.management;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.io.StreamCorruptedException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Arrays;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.WeakHashMap;
/*     */ 
/*     */ public class MBeanInfo
/*     */   implements Cloneable, Serializable, DescriptorRead
/*     */ {
/*     */   static final long serialVersionUID = -6451021435135161911L;
/*     */   private transient Descriptor descriptor;
/*     */   private final String description;
/*     */   private final String className;
/*     */   private final MBeanAttributeInfo[] attributes;
/*     */   private final MBeanOperationInfo[] operations;
/*     */   private final MBeanConstructorInfo[] constructors;
/*     */   private final MBeanNotificationInfo[] notifications;
/*     */   private transient int hashCode;
/*     */   private final transient boolean arrayGettersSafe;
/* 533 */   private static final Map<Class<?>, Boolean> arrayGettersSafeMap = new WeakHashMap();
/*     */ 
/*     */   public MBeanInfo(String paramString1, String paramString2, MBeanAttributeInfo[] paramArrayOfMBeanAttributeInfo, MBeanConstructorInfo[] paramArrayOfMBeanConstructorInfo, MBeanOperationInfo[] paramArrayOfMBeanOperationInfo, MBeanNotificationInfo[] paramArrayOfMBeanNotificationInfo)
/*     */     throws IllegalArgumentException
/*     */   {
/* 193 */     this(paramString1, paramString2, paramArrayOfMBeanAttributeInfo, paramArrayOfMBeanConstructorInfo, paramArrayOfMBeanOperationInfo, paramArrayOfMBeanNotificationInfo, null);
/*     */   }
/*     */ 
/*     */   public MBeanInfo(String paramString1, String paramString2, MBeanAttributeInfo[] paramArrayOfMBeanAttributeInfo, MBeanConstructorInfo[] paramArrayOfMBeanConstructorInfo, MBeanOperationInfo[] paramArrayOfMBeanOperationInfo, MBeanNotificationInfo[] paramArrayOfMBeanNotificationInfo, Descriptor paramDescriptor)
/*     */     throws IllegalArgumentException
/*     */   {
/* 233 */     this.className = paramString1;
/*     */ 
/* 235 */     this.description = paramString2;
/*     */ 
/* 237 */     if (paramArrayOfMBeanAttributeInfo == null)
/* 238 */       paramArrayOfMBeanAttributeInfo = MBeanAttributeInfo.NO_ATTRIBUTES;
/* 239 */     this.attributes = paramArrayOfMBeanAttributeInfo;
/*     */ 
/* 241 */     if (paramArrayOfMBeanOperationInfo == null)
/* 242 */       paramArrayOfMBeanOperationInfo = MBeanOperationInfo.NO_OPERATIONS;
/* 243 */     this.operations = paramArrayOfMBeanOperationInfo;
/*     */ 
/* 245 */     if (paramArrayOfMBeanConstructorInfo == null)
/* 246 */       paramArrayOfMBeanConstructorInfo = MBeanConstructorInfo.NO_CONSTRUCTORS;
/* 247 */     this.constructors = paramArrayOfMBeanConstructorInfo;
/*     */ 
/* 249 */     if (paramArrayOfMBeanNotificationInfo == null)
/* 250 */       paramArrayOfMBeanNotificationInfo = MBeanNotificationInfo.NO_NOTIFICATIONS;
/* 251 */     this.notifications = paramArrayOfMBeanNotificationInfo;
/*     */ 
/* 253 */     if (paramDescriptor == null)
/* 254 */       paramDescriptor = ImmutableDescriptor.EMPTY_DESCRIPTOR;
/* 255 */     this.descriptor = paramDescriptor;
/*     */ 
/* 257 */     this.arrayGettersSafe = arrayGettersSafe(getClass(), MBeanInfo.class);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 274 */       return super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 277 */     return null;
/*     */   }
/*     */ 
/*     */   public String getClassName()
/*     */   {
/* 289 */     return this.className;
/*     */   }
/*     */ 
/*     */   public String getDescription()
/*     */   {
/* 298 */     return this.description;
/*     */   }
/*     */ 
/*     */   public MBeanAttributeInfo[] getAttributes()
/*     */   {
/* 313 */     MBeanAttributeInfo[] arrayOfMBeanAttributeInfo = nonNullAttributes();
/* 314 */     if (arrayOfMBeanAttributeInfo.length == 0) {
/* 315 */       return arrayOfMBeanAttributeInfo;
/*     */     }
/* 317 */     return (MBeanAttributeInfo[])arrayOfMBeanAttributeInfo.clone();
/*     */   }
/*     */ 
/*     */   private MBeanAttributeInfo[] fastGetAttributes() {
/* 321 */     if (this.arrayGettersSafe) {
/* 322 */       return nonNullAttributes();
/*     */     }
/* 324 */     return getAttributes();
/*     */   }
/*     */ 
/*     */   private MBeanAttributeInfo[] nonNullAttributes()
/*     */   {
/* 339 */     return this.attributes == null ? MBeanAttributeInfo.NO_ATTRIBUTES : this.attributes;
/*     */   }
/*     */ 
/*     */   public MBeanOperationInfo[] getOperations()
/*     */   {
/* 355 */     MBeanOperationInfo[] arrayOfMBeanOperationInfo = nonNullOperations();
/* 356 */     if (arrayOfMBeanOperationInfo.length == 0) {
/* 357 */       return arrayOfMBeanOperationInfo;
/*     */     }
/* 359 */     return (MBeanOperationInfo[])arrayOfMBeanOperationInfo.clone();
/*     */   }
/*     */ 
/*     */   private MBeanOperationInfo[] fastGetOperations() {
/* 363 */     if (this.arrayGettersSafe) {
/* 364 */       return nonNullOperations();
/*     */     }
/* 366 */     return getOperations();
/*     */   }
/*     */ 
/*     */   private MBeanOperationInfo[] nonNullOperations() {
/* 370 */     return this.operations == null ? MBeanOperationInfo.NO_OPERATIONS : this.operations;
/*     */   }
/*     */ 
/*     */   public MBeanConstructorInfo[] getConstructors()
/*     */   {
/* 394 */     MBeanConstructorInfo[] arrayOfMBeanConstructorInfo = nonNullConstructors();
/* 395 */     if (arrayOfMBeanConstructorInfo.length == 0) {
/* 396 */       return arrayOfMBeanConstructorInfo;
/*     */     }
/* 398 */     return (MBeanConstructorInfo[])arrayOfMBeanConstructorInfo.clone();
/*     */   }
/*     */ 
/*     */   private MBeanConstructorInfo[] fastGetConstructors() {
/* 402 */     if (this.arrayGettersSafe) {
/* 403 */       return nonNullConstructors();
/*     */     }
/* 405 */     return getConstructors();
/*     */   }
/*     */ 
/*     */   private MBeanConstructorInfo[] nonNullConstructors() {
/* 409 */     return this.constructors == null ? MBeanConstructorInfo.NO_CONSTRUCTORS : this.constructors;
/*     */   }
/*     */ 
/*     */   public MBeanNotificationInfo[] getNotifications()
/*     */   {
/* 425 */     MBeanNotificationInfo[] arrayOfMBeanNotificationInfo = nonNullNotifications();
/* 426 */     if (arrayOfMBeanNotificationInfo.length == 0) {
/* 427 */       return arrayOfMBeanNotificationInfo;
/*     */     }
/* 429 */     return (MBeanNotificationInfo[])arrayOfMBeanNotificationInfo.clone();
/*     */   }
/*     */ 
/*     */   private MBeanNotificationInfo[] fastGetNotifications() {
/* 433 */     if (this.arrayGettersSafe) {
/* 434 */       return nonNullNotifications();
/*     */     }
/* 436 */     return getNotifications();
/*     */   }
/*     */ 
/*     */   private MBeanNotificationInfo[] nonNullNotifications() {
/* 440 */     return this.notifications == null ? MBeanNotificationInfo.NO_NOTIFICATIONS : this.notifications;
/*     */   }
/*     */ 
/*     */   public Descriptor getDescriptor()
/*     */   {
/* 453 */     return (Descriptor)ImmutableDescriptor.nonNullDescriptor(this.descriptor).clone();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 458 */     return getClass().getName() + "[" + "description=" + getDescription() + ", " + "attributes=" + Arrays.asList(fastGetAttributes()) + ", " + "constructors=" + Arrays.asList(fastGetConstructors()) + ", " + "operations=" + Arrays.asList(fastGetOperations()) + ", " + "notifications=" + Arrays.asList(fastGetNotifications()) + ", " + "descriptor=" + getDescriptor() + "]";
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 490 */     if (paramObject == this)
/* 491 */       return true;
/* 492 */     if (!(paramObject instanceof MBeanInfo))
/* 493 */       return false;
/* 494 */     MBeanInfo localMBeanInfo = (MBeanInfo)paramObject;
/* 495 */     if ((!isEqual(getClassName(), localMBeanInfo.getClassName())) || (!isEqual(getDescription(), localMBeanInfo.getDescription())) || (!getDescriptor().equals(localMBeanInfo.getDescriptor())))
/*     */     {
/* 498 */       return false;
/*     */     }
/*     */ 
/* 501 */     return (Arrays.equals(localMBeanInfo.fastGetAttributes(), fastGetAttributes())) && (Arrays.equals(localMBeanInfo.fastGetOperations(), fastGetOperations())) && (Arrays.equals(localMBeanInfo.fastGetConstructors(), fastGetConstructors())) && (Arrays.equals(localMBeanInfo.fastGetNotifications(), fastGetNotifications()));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 516 */     if (this.hashCode != 0) {
/* 517 */       return this.hashCode;
/*     */     }
/* 519 */     this.hashCode = (Objects.hash(new Object[] { getClassName(), getDescriptor() }) ^ Arrays.hashCode(fastGetAttributes()) ^ Arrays.hashCode(fastGetOperations()) ^ Arrays.hashCode(fastGetConstructors()) ^ Arrays.hashCode(fastGetNotifications()));
/*     */ 
/* 525 */     return this.hashCode;
/*     */   }
/*     */ 
/*     */   static boolean arrayGettersSafe(Class<?> paramClass1, Class<?> paramClass2)
/*     */   {
/* 547 */     if (paramClass1 == paramClass2)
/* 548 */       return true;
/* 549 */     synchronized (arrayGettersSafeMap) {
/* 550 */       Boolean localBoolean = (Boolean)arrayGettersSafeMap.get(paramClass1);
/* 551 */       if (localBoolean == null) {
/*     */         try {
/* 553 */           ArrayGettersSafeAction localArrayGettersSafeAction = new ArrayGettersSafeAction(paramClass1, paramClass2);
/*     */ 
/* 555 */           localBoolean = (Boolean)AccessController.doPrivileged(localArrayGettersSafeAction);
/*     */         }
/*     */         catch (Exception localException) {
/* 558 */           localBoolean = Boolean.valueOf(false);
/*     */         }
/* 560 */         arrayGettersSafeMap.put(paramClass1, localBoolean);
/*     */       }
/* 562 */       return localBoolean.booleanValue();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isEqual(String paramString1, String paramString2)
/*     */   {
/*     */     boolean bool;
/* 609 */     if (paramString1 == null)
/* 610 */       bool = paramString2 == null;
/*     */     else {
/* 612 */       bool = paramString1.equals(paramString2);
/*     */     }
/*     */ 
/* 615 */     return bool;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 644 */     paramObjectOutputStream.defaultWriteObject();
/*     */ 
/* 646 */     if (this.descriptor.getClass() == ImmutableDescriptor.class) {
/* 647 */       paramObjectOutputStream.write(1);
/*     */ 
/* 649 */       String[] arrayOfString = this.descriptor.getFieldNames();
/*     */ 
/* 651 */       paramObjectOutputStream.writeObject(arrayOfString);
/* 652 */       paramObjectOutputStream.writeObject(this.descriptor.getFieldValues(arrayOfString));
/*     */     } else {
/* 654 */       paramObjectOutputStream.write(0);
/*     */ 
/* 656 */       paramObjectOutputStream.writeObject(this.descriptor);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 693 */     paramObjectInputStream.defaultReadObject();
/*     */ 
/* 695 */     switch (paramObjectInputStream.read()) {
/*     */     case 1:
/* 697 */       String[] arrayOfString = (String[])paramObjectInputStream.readObject();
/*     */ 
/* 699 */       if (arrayOfString.length == 0) {
/* 700 */         this.descriptor = ImmutableDescriptor.EMPTY_DESCRIPTOR;
/*     */       } else {
/* 702 */         Object[] arrayOfObject = (Object[])paramObjectInputStream.readObject();
/* 703 */         this.descriptor = new ImmutableDescriptor(arrayOfString, arrayOfObject);
/*     */       }
/*     */ 
/* 706 */       break;
/*     */     case 0:
/* 708 */       this.descriptor = ((Descriptor)paramObjectInputStream.readObject());
/*     */ 
/* 710 */       if (this.descriptor == null)
/* 711 */         this.descriptor = ImmutableDescriptor.EMPTY_DESCRIPTOR; break;
/*     */     case -1:
/* 716 */       this.descriptor = ImmutableDescriptor.EMPTY_DESCRIPTOR;
/*     */ 
/* 718 */       break;
/*     */     default:
/* 720 */       throw new StreamCorruptedException("Got unexpected byte.");
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ArrayGettersSafeAction
/*     */     implements PrivilegedAction<Boolean>
/*     */   {
/*     */     private final Class<?> subclass;
/*     */     private final Class<?> immutableClass;
/*     */ 
/*     */     ArrayGettersSafeAction(Class<?> paramClass1, Class<?> paramClass2)
/*     */     {
/* 580 */       this.subclass = paramClass1;
/* 581 */       this.immutableClass = paramClass2;
/*     */     }
/*     */ 
/*     */     public Boolean run() {
/* 585 */       Method[] arrayOfMethod = this.immutableClass.getMethods();
/* 586 */       for (int i = 0; i < arrayOfMethod.length; i++) {
/* 587 */         Method localMethod1 = arrayOfMethod[i];
/* 588 */         String str = localMethod1.getName();
/* 589 */         if ((str.startsWith("get")) && (localMethod1.getParameterTypes().length == 0) && (localMethod1.getReturnType().isArray()))
/*     */         {
/*     */           try
/*     */           {
/* 593 */             Method localMethod2 = this.subclass.getMethod(str, new Class[0]);
/*     */ 
/* 595 */             if (!localMethod2.equals(localMethod1))
/* 596 */               return Boolean.valueOf(false);
/*     */           } catch (NoSuchMethodException localNoSuchMethodException) {
/* 598 */             return Boolean.valueOf(false);
/*     */           }
/*     */         }
/*     */       }
/* 602 */       return Boolean.valueOf(true);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.MBeanInfo
 * JD-Core Version:    0.6.2
 */