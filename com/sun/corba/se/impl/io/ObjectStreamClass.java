/*      */ package com.sun.corba.se.impl.io;
/*      */ 
/*      */ import com.sun.corba.se.impl.util.RepositoryId;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.Externalizable;
/*      */ import java.io.IOException;
/*      */ import java.io.InvalidClassException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Member;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Modifier;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.security.AccessController;
/*      */ import java.security.DigestOutputStream;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.Arrays;
/*      */ import java.util.Comparator;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import org.omg.CORBA.ValueMember;
/*      */ import sun.corba.Bridge;
/*      */ 
/*      */ public class ObjectStreamClass
/*      */   implements Serializable
/*      */ {
/*      */   private static final boolean DEBUG_SVUID = false;
/*      */   public static final long kDefaultUID = -1L;
/*   85 */   private static Object[] noArgsList = new Object[0];
/*   86 */   private static Class<?>[] noTypesList = new Class[0];
/*      */   private boolean isEnum;
/*   91 */   private static final Bridge bridge = (Bridge)AccessController.doPrivileged(new PrivilegedAction()
/*      */   {
/*      */     public Bridge run()
/*      */     {
/*   95 */       return Bridge.get();
/*      */     }
/*      */   });
/*      */ 
/*  433 */   private static final PersistentFieldsValue persistentFieldsValue = new PersistentFieldsValue();
/*      */   public static final int CLASS_MASK = 1553;
/*      */   public static final int FIELD_MASK = 223;
/*      */   public static final int METHOD_MASK = 3391;
/* 1404 */   private static ObjectStreamClassEntry[] descriptorFor = new ObjectStreamClassEntry[61];
/*      */   private String name;
/*      */   private ObjectStreamClass superclass;
/*      */   private boolean serializable;
/*      */   private boolean externalizable;
/*      */   private ObjectStreamField[] fields;
/*      */   private Class<?> ofClass;
/*      */   boolean forProxyClass;
/* 1505 */   private long suid = -1L;
/* 1506 */   private String suidStr = null;
/*      */ 
/* 1511 */   private long actualSuid = -1L;
/* 1512 */   private String actualSuidStr = null;
/*      */   int primBytes;
/*      */   int objFields;
/* 1527 */   private boolean initialized = false;
/*      */ 
/* 1530 */   private Object lock = new Object();
/*      */   private boolean hasExternalizableBlockData;
/*      */   Method writeObjectMethod;
/*      */   Method readObjectMethod;
/*      */   private transient Method writeReplaceObjectMethod;
/*      */   private transient Method readResolveObjectMethod;
/*      */   private Constructor cons;
/* 1553 */   private String rmiiiopOptionalDataRepId = null;
/*      */   private ObjectStreamClass localClassDesc;
/* 1561 */   private static Method hasStaticInitializerMethod = null;
/*      */   private static final long serialVersionUID = -6120832682080437368L;
/* 1608 */   public static final ObjectStreamField[] NO_FIELDS = new ObjectStreamField[0];
/*      */ 
/* 1633 */   private static Comparator compareClassByName = new CompareClassByName(null);
/*      */ 
/* 1647 */   private static final Comparator compareObjStrFieldsByName = new CompareObjStrFieldsByName(null);
/*      */ 
/* 1662 */   private static Comparator compareMemberByName = new CompareMemberByName(null);
/*      */ 
/*      */   static final ObjectStreamClass lookup(Class<?> paramClass)
/*      */   {
/*  106 */     ObjectStreamClass localObjectStreamClass = lookupInternal(paramClass);
/*  107 */     if ((localObjectStreamClass.isSerializable()) || (localObjectStreamClass.isExternalizable()))
/*  108 */       return localObjectStreamClass;
/*  109 */     return null;
/*      */   }
/*      */ 
/*      */   static ObjectStreamClass lookupInternal(Class<?> paramClass)
/*      */   {
/*  121 */     ObjectStreamClass localObjectStreamClass1 = null;
/*  122 */     synchronized (descriptorFor)
/*      */     {
/*  124 */       localObjectStreamClass1 = findDescriptorFor(paramClass);
/*  125 */       if (localObjectStreamClass1 == null)
/*      */       {
/*  127 */         boolean bool1 = Serializable.class.isAssignableFrom(paramClass);
/*      */ 
/*  132 */         ObjectStreamClass localObjectStreamClass2 = null;
/*  133 */         if (bool1) {
/*  134 */           Class localClass = paramClass.getSuperclass();
/*  135 */           if (localClass != null) {
/*  136 */             localObjectStreamClass2 = lookup(localClass);
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  143 */         boolean bool2 = false;
/*  144 */         if (bool1) {
/*  145 */           bool2 = ((localObjectStreamClass2 != null) && (localObjectStreamClass2.isExternalizable())) || (Externalizable.class.isAssignableFrom(paramClass));
/*      */ 
/*  148 */           if (bool2) {
/*  149 */             bool1 = false;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  156 */         localObjectStreamClass1 = new ObjectStreamClass(paramClass, localObjectStreamClass2, bool1, bool2);
/*      */       }
/*      */ 
/*  173 */       localObjectStreamClass1.init();
/*      */     }
/*  175 */     return localObjectStreamClass1;
/*      */   }
/*      */ 
/*      */   public final String getName()
/*      */   {
/*  182 */     return this.name;
/*      */   }
/*      */ 
/*      */   public static final long getSerialVersionUID(Class<?> paramClass)
/*      */   {
/*  192 */     ObjectStreamClass localObjectStreamClass = lookup(paramClass);
/*  193 */     if (localObjectStreamClass != null)
/*      */     {
/*  195 */       return localObjectStreamClass.getSerialVersionUID();
/*      */     }
/*  197 */     return 0L;
/*      */   }
/*      */ 
/*      */   public final long getSerialVersionUID()
/*      */   {
/*  207 */     return this.suid;
/*      */   }
/*      */ 
/*      */   public final String getSerialVersionUIDStr()
/*      */   {
/*  217 */     if (this.suidStr == null)
/*  218 */       this.suidStr = Long.toHexString(this.suid).toUpperCase();
/*  219 */     return this.suidStr;
/*      */   }
/*      */ 
/*      */   public static final long getActualSerialVersionUID(Class<?> paramClass)
/*      */   {
/*  227 */     ObjectStreamClass localObjectStreamClass = lookup(paramClass);
/*  228 */     if (localObjectStreamClass != null)
/*      */     {
/*  230 */       return localObjectStreamClass.getActualSerialVersionUID();
/*      */     }
/*  232 */     return 0L;
/*      */   }
/*      */ 
/*      */   public final long getActualSerialVersionUID()
/*      */   {
/*  239 */     return this.actualSuid;
/*      */   }
/*      */ 
/*      */   public final String getActualSerialVersionUIDStr()
/*      */   {
/*  246 */     if (this.actualSuidStr == null)
/*  247 */       this.actualSuidStr = Long.toHexString(this.actualSuid).toUpperCase();
/*  248 */     return this.actualSuidStr;
/*      */   }
/*      */ 
/*      */   public final Class<?> forClass()
/*      */   {
/*  256 */     return this.ofClass;
/*      */   }
/*      */ 
/*      */   public ObjectStreamField[] getFields()
/*      */   {
/*  268 */     if (this.fields.length > 0) {
/*  269 */       ObjectStreamField[] arrayOfObjectStreamField = new ObjectStreamField[this.fields.length];
/*  270 */       System.arraycopy(this.fields, 0, arrayOfObjectStreamField, 0, this.fields.length);
/*  271 */       return arrayOfObjectStreamField;
/*      */     }
/*  273 */     return this.fields;
/*      */   }
/*      */ 
/*      */   public boolean hasField(ValueMember paramValueMember)
/*      */   {
/*      */     try
/*      */     {
/*  280 */       for (int i = 0; i < this.fields.length; i++) {
/*  281 */         if ((this.fields[i].getName().equals(paramValueMember.name)) && 
/*  282 */           (this.fields[i].getSignature().equals(ValueUtility.getSignature(paramValueMember))))
/*      */         {
/*  284 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*      */ 
/*  292 */     return false;
/*      */   }
/*      */ 
/*      */   final ObjectStreamField[] getFieldsNoCopy()
/*      */   {
/*  297 */     return this.fields;
/*      */   }
/*      */ 
/*      */   public final ObjectStreamField getField(String paramString)
/*      */   {
/*  308 */     for (int i = this.fields.length - 1; i >= 0; i--) {
/*  309 */       if (paramString.equals(this.fields[i].getName())) {
/*  310 */         return this.fields[i];
/*      */       }
/*      */     }
/*  313 */     return null;
/*      */   }
/*      */ 
/*      */   public Serializable writeReplace(Serializable paramSerializable) {
/*  317 */     if (this.writeReplaceObjectMethod != null) {
/*      */       try {
/*  319 */         return (Serializable)this.writeReplaceObjectMethod.invoke(paramSerializable, noArgsList);
/*      */       } catch (Throwable localThrowable) {
/*  321 */         throw new RuntimeException(localThrowable);
/*      */       }
/*      */     }
/*  324 */     return paramSerializable;
/*      */   }
/*      */ 
/*      */   public Object readResolve(Object paramObject) {
/*  328 */     if (this.readResolveObjectMethod != null) {
/*      */       try {
/*  330 */         return this.readResolveObjectMethod.invoke(paramObject, noArgsList);
/*      */       } catch (Throwable localThrowable) {
/*  332 */         throw new RuntimeException(localThrowable);
/*      */       }
/*      */     }
/*  335 */     return paramObject;
/*      */   }
/*      */ 
/*      */   public final String toString()
/*      */   {
/*  342 */     StringBuffer localStringBuffer = new StringBuffer();
/*      */ 
/*  344 */     localStringBuffer.append(this.name);
/*  345 */     localStringBuffer.append(": static final long serialVersionUID = ");
/*  346 */     localStringBuffer.append(Long.toString(this.suid));
/*  347 */     localStringBuffer.append("L;");
/*  348 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private ObjectStreamClass(Class<?> paramClass, ObjectStreamClass paramObjectStreamClass, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  358 */     this.ofClass = paramClass;
/*      */ 
/*  360 */     if (Proxy.isProxyClass(paramClass)) {
/*  361 */       this.forProxyClass = true;
/*      */     }
/*      */ 
/*  364 */     this.name = paramClass.getName();
/*  365 */     this.isEnum = Enum.class.isAssignableFrom(paramClass);
/*  366 */     this.superclass = paramObjectStreamClass;
/*  367 */     this.serializable = paramBoolean1;
/*  368 */     if (!this.forProxyClass)
/*      */     {
/*  370 */       this.externalizable = paramBoolean2;
/*      */     }
/*      */ 
/*  378 */     insertDescriptorFor(this);
/*      */   }
/*      */ 
/*      */   private void init()
/*      */   {
/*  446 */     synchronized (this.lock)
/*      */     {
/*  449 */       if (this.initialized) {
/*  450 */         return;
/*      */       }
/*  452 */       final Class localClass = this.ofClass;
/*      */ 
/*  454 */       if ((!this.serializable) || (this.externalizable) || (this.forProxyClass) || (this.name.equals("java.lang.String")))
/*      */       {
/*  458 */         this.fields = NO_FIELDS;
/*  459 */       } else if (this.serializable)
/*      */       {
/*  462 */         AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Object run()
/*      */           {
/*  468 */             ObjectStreamClass.this.fields = ObjectStreamClass.persistentFieldsValue.get(localClass);
/*      */ 
/*  470 */             if (ObjectStreamClass.this.fields == null)
/*      */             {
/*  479 */               Field[] arrayOfField = localClass.getDeclaredFields();
/*      */ 
/*  481 */               int j = 0;
/*  482 */               ObjectStreamField[] arrayOfObjectStreamField = new ObjectStreamField[arrayOfField.length];
/*      */ 
/*  484 */               for (int k = 0; k < arrayOfField.length; k++) {
/*  485 */                 Field localField2 = arrayOfField[k];
/*  486 */                 int m = localField2.getModifiers();
/*  487 */                 if ((!Modifier.isStatic(m)) && (!Modifier.isTransient(m)))
/*      */                 {
/*  489 */                   localField2.setAccessible(true);
/*  490 */                   arrayOfObjectStreamField[(j++)] = new ObjectStreamField(localField2);
/*      */                 }
/*      */               }
/*      */ 
/*  494 */               ObjectStreamClass.this.fields = new ObjectStreamField[j];
/*  495 */               System.arraycopy(arrayOfObjectStreamField, 0, ObjectStreamClass.this.fields, 0, j);
/*      */             }
/*      */             else
/*      */             {
/*  501 */               for (int i = ObjectStreamClass.this.fields.length - 1; i >= 0; i--)
/*      */                 try {
/*  503 */                   Field localField1 = localClass.getDeclaredField(ObjectStreamClass.this.fields[i].getName());
/*  504 */                   if (ObjectStreamClass.this.fields[i].getType() == localField1.getType()) {
/*  505 */                     localField1.setAccessible(true);
/*  506 */                     ObjectStreamClass.this.fields[i].setField(localField1);
/*      */                   }
/*      */                 }
/*      */                 catch (NoSuchFieldException localNoSuchFieldException)
/*      */                 {
/*      */                 }
/*      */             }
/*  513 */             return null;
/*      */           }
/*      */         });
/*  517 */         if (this.fields.length > 1) {
/*  518 */           Arrays.sort(this.fields);
/*      */         }
/*      */ 
/*  521 */         computeFieldInfo();
/*      */       }
/*      */ 
/*  530 */       if ((isNonSerializable()) || (this.isEnum)) {
/*  531 */         this.suid = 0L;
/*      */       }
/*      */       else {
/*  534 */         AccessController.doPrivileged(new PrivilegedAction() {
/*      */           public Object run() {
/*  536 */             if (ObjectStreamClass.this.forProxyClass)
/*      */             {
/*  538 */               ObjectStreamClass.this.suid = 0L;
/*      */             }
/*      */             else try {
/*  541 */                 Field localField = localClass.getDeclaredField("serialVersionUID");
/*  542 */                 int i = localField.getModifiers();
/*      */ 
/*  544 */                 if ((Modifier.isStatic(i)) && (Modifier.isFinal(i))) {
/*  545 */                   localField.setAccessible(true);
/*  546 */                   ObjectStreamClass.this.suid = localField.getLong(localClass);
/*      */                 }
/*      */                 else
/*      */                 {
/*  550 */                   ObjectStreamClass.this.suid = ObjectStreamClass._computeSerialVersionUID(localClass);
/*      */                 }
/*      */               }
/*      */               catch (NoSuchFieldException localNoSuchFieldException)
/*      */               {
/*  555 */                 ObjectStreamClass.this.suid = ObjectStreamClass._computeSerialVersionUID(localClass);
/*      */               }
/*      */               catch (IllegalAccessException localIllegalAccessException)
/*      */               {
/*  559 */                 ObjectStreamClass.this.suid = ObjectStreamClass._computeSerialVersionUID(localClass);
/*      */               }
/*      */ 
/*      */ 
/*  563 */             ObjectStreamClass.this.writeReplaceObjectMethod = ObjectStreamClass.getInheritableMethod(localClass, "writeReplace", ObjectStreamClass.noTypesList, Object.class);
/*      */ 
/*  566 */             ObjectStreamClass.this.readResolveObjectMethod = ObjectStreamClass.getInheritableMethod(localClass, "readResolve", ObjectStreamClass.noTypesList, Object.class);
/*      */ 
/*  569 */             if (ObjectStreamClass.this.externalizable)
/*  570 */               ObjectStreamClass.this.cons = ObjectStreamClass.getExternalizableConstructor(localClass);
/*      */             else {
/*  572 */               ObjectStreamClass.this.cons = ObjectStreamClass.getSerializableConstructor(localClass);
/*      */             }
/*  574 */             if ((ObjectStreamClass.this.serializable) && (!ObjectStreamClass.this.forProxyClass))
/*      */             {
/*  579 */               ObjectStreamClass.this.writeObjectMethod = ObjectStreamClass.getPrivateMethod(localClass, "writeObject", new Class[] { ObjectOutputStream.class }, Void.TYPE);
/*      */ 
/*  581 */               ObjectStreamClass.this.readObjectMethod = ObjectStreamClass.getPrivateMethod(localClass, "readObject", new Class[] { ObjectInputStream.class }, Void.TYPE);
/*      */             }
/*      */ 
/*  584 */             return null;
/*      */           }
/*      */ 
/*      */         });
/*      */       }
/*      */ 
/*  590 */       this.actualSuid = computeStructuralUID(this, localClass);
/*      */ 
/*  595 */       if (hasWriteObject()) {
/*  596 */         this.rmiiiopOptionalDataRepId = computeRMIIIOPOptionalDataRepId();
/*      */       }
/*      */ 
/*  599 */       this.initialized = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Method getPrivateMethod(Class<?> paramClass1, String paramString, Class<?>[] paramArrayOfClass, Class<?> paramClass2)
/*      */   {
/*      */     try
/*      */     {
/*  613 */       Method localMethod = paramClass1.getDeclaredMethod(paramString, paramArrayOfClass);
/*  614 */       localMethod.setAccessible(true);
/*  615 */       int i = localMethod.getModifiers();
/*  616 */       return (localMethod.getReturnType() == paramClass2) && ((i & 0x8) == 0) && ((i & 0x2) != 0) ? localMethod : null;
/*      */     }
/*      */     catch (NoSuchMethodException localNoSuchMethodException) {
/*      */     }
/*  620 */     return null;
/*      */   }
/*      */ 
/*      */   private String computeRMIIIOPOptionalDataRepId()
/*      */   {
/*  636 */     StringBuffer localStringBuffer = new StringBuffer("RMI:org.omg.custom.");
/*  637 */     localStringBuffer.append(RepositoryId.convertToISOLatin1(getName()));
/*  638 */     localStringBuffer.append(':');
/*  639 */     localStringBuffer.append(getActualSerialVersionUIDStr());
/*  640 */     localStringBuffer.append(':');
/*  641 */     localStringBuffer.append(getSerialVersionUIDStr());
/*      */ 
/*  643 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   public final String getRMIIIOPOptionalDataRepId()
/*      */   {
/*  650 */     return this.rmiiiopOptionalDataRepId;
/*      */   }
/*      */ 
/*      */   ObjectStreamClass(String paramString, long paramLong)
/*      */   {
/*  660 */     this.name = paramString;
/*  661 */     this.suid = paramLong;
/*  662 */     this.superclass = null;
/*      */   }
/*      */ 
/*      */   final void setClass(Class<?> paramClass)
/*      */     throws InvalidClassException
/*      */   {
/*  673 */     if (paramClass == null) {
/*  674 */       this.localClassDesc = null;
/*  675 */       this.ofClass = null;
/*  676 */       computeFieldInfo();
/*  677 */       return;
/*      */     }
/*      */ 
/*  680 */     this.localClassDesc = lookupInternal(paramClass);
/*  681 */     if (this.localClassDesc == null)
/*      */     {
/*  683 */       throw new InvalidClassException(paramClass.getName(), "Local class not compatible");
/*      */     }
/*  685 */     if (this.suid != this.localClassDesc.suid)
/*      */     {
/*  692 */       int i = (isNonSerializable()) || (this.localClassDesc.isNonSerializable()) ? 1 : 0;
/*      */ 
/*  704 */       int j = (paramClass.isArray()) && (!paramClass.getName().equals(this.name)) ? 1 : 0;
/*      */ 
/*  706 */       if ((j == 0) && (i == 0))
/*      */       {
/*  708 */         throw new InvalidClassException(paramClass.getName(), "Local class not compatible: stream classdesc serialVersionUID=" + this.suid + " local class serialVersionUID=" + this.localClassDesc.suid);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  716 */     if (!compareClassNames(this.name, paramClass.getName(), '.'))
/*      */     {
/*  718 */       throw new InvalidClassException(paramClass.getName(), "Incompatible local class name. Expected class name compatible with " + this.name);
/*      */     }
/*      */ 
/*  735 */     if ((this.serializable != this.localClassDesc.serializable) || (this.externalizable != this.localClassDesc.externalizable) || ((!this.serializable) && (!this.externalizable)))
/*      */     {
/*  740 */       throw new InvalidClassException(paramClass.getName(), "Serialization incompatible with Externalization");
/*      */     }
/*      */ 
/*  757 */     ObjectStreamField[] arrayOfObjectStreamField1 = (ObjectStreamField[])this.localClassDesc.fields;
/*      */ 
/*  759 */     ObjectStreamField[] arrayOfObjectStreamField2 = (ObjectStreamField[])this.fields;
/*      */ 
/*  762 */     int k = 0;
/*      */ 
/*  764 */     for (int m = 0; m < arrayOfObjectStreamField2.length; m++)
/*      */     {
/*  766 */       for (int n = k; n < arrayOfObjectStreamField1.length; n++) {
/*  767 */         if (arrayOfObjectStreamField2[m].getName().equals(arrayOfObjectStreamField1[n].getName()))
/*      */         {
/*  769 */           if ((arrayOfObjectStreamField2[m].isPrimitive()) && (!arrayOfObjectStreamField2[m].typeEquals(arrayOfObjectStreamField1[n])))
/*      */           {
/*  772 */             throw new InvalidClassException(paramClass.getName(), "The type of field " + arrayOfObjectStreamField2[m].getName() + " of class " + this.name + " is incompatible.");
/*      */           }
/*      */ 
/*  780 */           k = n;
/*      */ 
/*  782 */           arrayOfObjectStreamField2[m].setField(arrayOfObjectStreamField1[k].getField());
/*      */ 
/*  784 */           break;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  790 */     computeFieldInfo();
/*      */ 
/*  793 */     this.ofClass = paramClass;
/*      */ 
/*  798 */     this.readObjectMethod = this.localClassDesc.readObjectMethod;
/*  799 */     this.readResolveObjectMethod = this.localClassDesc.readResolveObjectMethod;
/*      */   }
/*      */ 
/*      */   static boolean compareClassNames(String paramString1, String paramString2, char paramChar)
/*      */   {
/*  815 */     int i = paramString1.lastIndexOf(paramChar);
/*  816 */     if (i < 0) {
/*  817 */       i = 0;
/*      */     }
/*  819 */     int j = paramString2.lastIndexOf(paramChar);
/*  820 */     if (j < 0) {
/*  821 */       j = 0;
/*      */     }
/*  823 */     return paramString1.regionMatches(false, i, paramString2, j, paramString1.length() - i);
/*      */   }
/*      */ 
/*      */   final boolean typeEquals(ObjectStreamClass paramObjectStreamClass)
/*      */   {
/*  833 */     return (this.suid == paramObjectStreamClass.suid) && (compareClassNames(this.name, paramObjectStreamClass.name, '.'));
/*      */   }
/*      */ 
/*      */   final void setSuperclass(ObjectStreamClass paramObjectStreamClass)
/*      */   {
/*  841 */     this.superclass = paramObjectStreamClass;
/*      */   }
/*      */ 
/*      */   final ObjectStreamClass getSuperclass()
/*      */   {
/*  848 */     return this.superclass;
/*      */   }
/*      */ 
/*      */   final boolean hasReadObject()
/*      */   {
/*  855 */     return this.readObjectMethod != null;
/*      */   }
/*      */ 
/*      */   final boolean hasWriteObject()
/*      */   {
/*  862 */     return this.writeObjectMethod != null;
/*      */   }
/*      */ 
/*      */   final boolean isCustomMarshaled()
/*      */   {
/*  872 */     return (hasWriteObject()) || (isExternalizable()) || ((this.superclass != null) && (this.superclass.isCustomMarshaled()));
/*      */   }
/*      */ 
/*      */   boolean hasExternalizableBlockDataMode()
/*      */   {
/*  903 */     return this.hasExternalizableBlockData;
/*      */   }
/*      */ 
/*      */   Object newInstance()
/*      */     throws InstantiationException, InvocationTargetException, UnsupportedOperationException
/*      */   {
/*  919 */     if (this.cons != null) {
/*      */       try {
/*  921 */         return this.cons.newInstance(new Object[0]);
/*      */       }
/*      */       catch (IllegalAccessException localIllegalAccessException) {
/*  924 */         InternalError localInternalError = new InternalError();
/*  925 */         localInternalError.initCause(localIllegalAccessException);
/*  926 */         throw localInternalError;
/*      */       }
/*      */     }
/*  929 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   private static Constructor getExternalizableConstructor(Class<?> paramClass)
/*      */   {
/*      */     try
/*      */     {
/*  940 */       Constructor localConstructor = paramClass.getDeclaredConstructor(new Class[0]);
/*  941 */       localConstructor.setAccessible(true);
/*  942 */       return (localConstructor.getModifiers() & 0x1) != 0 ? localConstructor : null;
/*      */     } catch (NoSuchMethodException localNoSuchMethodException) {
/*      */     }
/*  945 */     return null;
/*      */   }
/*      */ 
/*      */   private static Constructor getSerializableConstructor(Class<?> paramClass)
/*      */   {
/*  955 */     Object localObject = paramClass;
/*  956 */     while (Serializable.class.isAssignableFrom((Class)localObject)) {
/*  957 */       if ((localObject = ((Class)localObject).getSuperclass()) == null)
/*  958 */         return null;
/*      */     }
/*      */     try
/*      */     {
/*  962 */       Constructor localConstructor = ((Class)localObject).getDeclaredConstructor(new Class[0]);
/*  963 */       int i = localConstructor.getModifiers();
/*  964 */       if (((i & 0x2) != 0) || (((i & 0x5) == 0) && (!packageEquals(paramClass, (Class)localObject))))
/*      */       {
/*  968 */         return null;
/*      */       }
/*  970 */       localConstructor = bridge.newConstructorForSerialization(paramClass, localConstructor);
/*  971 */       localConstructor.setAccessible(true);
/*  972 */       return localConstructor; } catch (NoSuchMethodException localNoSuchMethodException) {
/*      */     }
/*  974 */     return null;
/*      */   }
/*      */ 
/*      */   final ObjectStreamClass localClassDescriptor()
/*      */   {
/*  982 */     return this.localClassDesc;
/*      */   }
/*      */ 
/*      */   boolean isSerializable()
/*      */   {
/*  989 */     return this.serializable;
/*      */   }
/*      */ 
/*      */   boolean isExternalizable()
/*      */   {
/*  996 */     return this.externalizable;
/*      */   }
/*      */ 
/*      */   boolean isNonSerializable() {
/* 1000 */     return (!this.externalizable) && (!this.serializable);
/*      */   }
/*      */ 
/*      */   private void computeFieldInfo()
/*      */   {
/* 1009 */     this.primBytes = 0;
/* 1010 */     this.objFields = 0;
/*      */ 
/* 1012 */     for (int i = 0; i < this.fields.length; i++)
/* 1013 */       switch (this.fields[i].getTypeCode()) {
/*      */       case 'B':
/*      */       case 'Z':
/* 1016 */         this.primBytes += 1;
/* 1017 */         break;
/*      */       case 'C':
/*      */       case 'S':
/* 1020 */         this.primBytes += 2;
/* 1021 */         break;
/*      */       case 'F':
/*      */       case 'I':
/* 1025 */         this.primBytes += 4;
/* 1026 */         break;
/*      */       case 'D':
/*      */       case 'J':
/* 1029 */         this.primBytes += 8;
/* 1030 */         break;
/*      */       case 'L':
/*      */       case '[':
/* 1034 */         this.objFields += 1;
/*      */       case 'E':
/*      */       case 'G':
/*      */       case 'H':
/*      */       case 'K':
/*      */       case 'M':
/*      */       case 'N':
/*      */       case 'O':
/*      */       case 'P':
/*      */       case 'Q':
/*      */       case 'R':
/*      */       case 'T':
/*      */       case 'U':
/*      */       case 'V':
/*      */       case 'W':
/*      */       case 'X':
/*      */       case 'Y': }   } 
/* 1042 */   private static void msg(String paramString) { System.out.println(paramString); }
/*      */ 
/*      */ 
/*      */   private static long _computeSerialVersionUID(Class<?> paramClass)
/*      */   {
/* 1070 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(512);
/*      */ 
/* 1072 */     long l = 0L;
/*      */     try {
/* 1074 */       MessageDigest localMessageDigest = MessageDigest.getInstance("SHA");
/* 1075 */       localObject1 = new DigestOutputStream(localByteArrayOutputStream, localMessageDigest);
/* 1076 */       DataOutputStream localDataOutputStream = new DataOutputStream((OutputStream)localObject1);
/*      */ 
/* 1080 */       localDataOutputStream.writeUTF(paramClass.getName());
/*      */ 
/* 1082 */       int i = paramClass.getModifiers();
/* 1083 */       i &= 1553;
/*      */ 
/* 1093 */       Method[] arrayOfMethod = paramClass.getDeclaredMethods();
/* 1094 */       if ((i & 0x200) != 0) {
/* 1095 */         i &= -1025;
/* 1096 */         if (arrayOfMethod.length > 0) {
/* 1097 */           i |= 1024;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1102 */       i &= 1553;
/*      */ 
/* 1106 */       localDataOutputStream.writeInt(i);
/*      */ 
/* 1113 */       if (!paramClass.isArray())
/*      */       {
/* 1121 */         localObject2 = paramClass.getInterfaces();
/* 1122 */         Arrays.sort((Object[])localObject2, compareClassByName);
/*      */ 
/* 1124 */         for (j = 0; j < localObject2.length; j++)
/*      */         {
/* 1127 */           localDataOutputStream.writeUTF(localObject2[j].getName());
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1132 */       Object localObject2 = paramClass.getDeclaredFields();
/* 1133 */       Arrays.sort((Object[])localObject2, compareMemberByName);
/*      */ 
/* 1135 */       for (int j = 0; j < localObject2.length; j++) {
/* 1136 */         Object localObject3 = localObject2[j];
/*      */ 
/* 1141 */         int m = localObject3.getModifiers();
/* 1142 */         if ((!Modifier.isPrivate(m)) || ((!Modifier.isTransient(m)) && (!Modifier.isStatic(m))))
/*      */         {
/* 1148 */           localDataOutputStream.writeUTF(localObject3.getName());
/*      */ 
/* 1151 */           m &= 223;
/*      */ 
/* 1155 */           localDataOutputStream.writeInt(m);
/*      */ 
/* 1159 */           localDataOutputStream.writeUTF(getSignature(localObject3.getType()));
/*      */         }
/*      */       }
/* 1162 */       if (hasStaticInitializer(paramClass))
/*      */       {
/* 1165 */         localDataOutputStream.writeUTF("<clinit>");
/*      */ 
/* 1169 */         localDataOutputStream.writeInt(8);
/*      */ 
/* 1173 */         localDataOutputStream.writeUTF("()V");
/*      */       }
/*      */ 
/* 1182 */       MethodSignature[] arrayOfMethodSignature1 = MethodSignature.removePrivateAndSort(paramClass.getDeclaredConstructors());
/*      */       Object localObject4;
/*      */       String str;
/*      */       int i2;
/* 1184 */       for (int k = 0; k < arrayOfMethodSignature1.length; k++) {
/* 1185 */         MethodSignature localMethodSignature = arrayOfMethodSignature1[k];
/* 1186 */         localObject4 = "<init>";
/* 1187 */         str = localMethodSignature.signature;
/* 1188 */         str = str.replace('/', '.');
/*      */ 
/* 1191 */         localDataOutputStream.writeUTF((String)localObject4);
/*      */ 
/* 1194 */         i2 = localMethodSignature.member.getModifiers() & 0xD3F;
/*      */ 
/* 1198 */         localDataOutputStream.writeInt(i2);
/*      */ 
/* 1202 */         localDataOutputStream.writeUTF(str);
/*      */       }
/*      */ 
/* 1208 */       MethodSignature[] arrayOfMethodSignature2 = MethodSignature.removePrivateAndSort(arrayOfMethod);
/*      */ 
/* 1210 */       for (int n = 0; n < arrayOfMethodSignature2.length; n++) {
/* 1211 */         localObject4 = arrayOfMethodSignature2[n];
/* 1212 */         str = ((MethodSignature)localObject4).signature;
/* 1213 */         str = str.replace('/', '.');
/*      */ 
/* 1217 */         localDataOutputStream.writeUTF(((MethodSignature)localObject4).member.getName());
/*      */ 
/* 1220 */         i2 = ((MethodSignature)localObject4).member.getModifiers() & 0xD3F;
/*      */ 
/* 1224 */         localDataOutputStream.writeInt(i2);
/*      */ 
/* 1228 */         localDataOutputStream.writeUTF(str);
/*      */       }
/*      */ 
/* 1234 */       localDataOutputStream.flush();
/* 1235 */       byte[] arrayOfByte = localMessageDigest.digest();
/* 1236 */       for (int i1 = 0; i1 < Math.min(8, arrayOfByte.length); i1++)
/* 1237 */         l += ((arrayOfByte[i1] & 0xFF) << i1 * 8);
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/* 1241 */       l = -1L;
/*      */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 1243 */       Object localObject1 = new SecurityException();
/* 1244 */       ((SecurityException)localObject1).initCause(localNoSuchAlgorithmException);
/* 1245 */       throw ((Throwable)localObject1);
/*      */     }
/*      */ 
/* 1248 */     return l;
/*      */   }
/*      */ 
/*      */   private static long computeStructuralUID(ObjectStreamClass paramObjectStreamClass, Class<?> paramClass) {
/* 1252 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(512);
/*      */ 
/* 1254 */     long l = 0L;
/*      */     try
/*      */     {
/* 1257 */       if ((!Serializable.class.isAssignableFrom(paramClass)) || (paramClass.isInterface()))
/*      */       {
/* 1259 */         return 0L;
/*      */       }
/*      */ 
/* 1262 */       if (Externalizable.class.isAssignableFrom(paramClass)) {
/* 1263 */         return 1L;
/*      */       }
/*      */ 
/* 1266 */       MessageDigest localMessageDigest = MessageDigest.getInstance("SHA");
/* 1267 */       localObject = new DigestOutputStream(localByteArrayOutputStream, localMessageDigest);
/* 1268 */       DataOutputStream localDataOutputStream = new DataOutputStream((OutputStream)localObject);
/*      */ 
/* 1271 */       Class localClass = paramClass.getSuperclass();
/* 1272 */       if (localClass != null)
/*      */       {
/* 1279 */         localDataOutputStream.writeLong(computeStructuralUID(lookup(localClass), localClass));
/*      */       }
/*      */ 
/* 1282 */       if (paramObjectStreamClass.hasWriteObject())
/* 1283 */         localDataOutputStream.writeInt(2);
/*      */       else {
/* 1285 */         localDataOutputStream.writeInt(1);
/*      */       }
/*      */ 
/* 1290 */       ObjectStreamField[] arrayOfObjectStreamField = paramObjectStreamClass.getFields();
/* 1291 */       if (arrayOfObjectStreamField.length > 1) {
/* 1292 */         Arrays.sort(arrayOfObjectStreamField, compareObjStrFieldsByName);
/*      */       }
/*      */ 
/* 1297 */       for (int i = 0; i < arrayOfObjectStreamField.length; i++) {
/* 1298 */         localDataOutputStream.writeUTF(arrayOfObjectStreamField[i].getName());
/* 1299 */         localDataOutputStream.writeUTF(arrayOfObjectStreamField[i].getSignature());
/*      */       }
/*      */ 
/* 1305 */       localDataOutputStream.flush();
/* 1306 */       byte[] arrayOfByte = localMessageDigest.digest();
/*      */ 
/* 1310 */       for (int j = 0; j < Math.min(8, arrayOfByte.length); j++)
/* 1311 */         l += ((arrayOfByte[j] & 0xFF) << j * 8);
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/* 1315 */       l = -1L;
/*      */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 1317 */       Object localObject = new SecurityException();
/* 1318 */       ((SecurityException)localObject).initCause(localNoSuchAlgorithmException);
/* 1319 */       throw ((Throwable)localObject);
/*      */     }
/* 1321 */     return l;
/*      */   }
/*      */ 
/*      */   static String getSignature(Class<?> paramClass)
/*      */   {
/* 1328 */     String str = null;
/* 1329 */     if (paramClass.isArray()) {
/* 1330 */       Object localObject = paramClass;
/* 1331 */       int i = 0;
/* 1332 */       while (((Class)localObject).isArray()) {
/* 1333 */         i++;
/* 1334 */         localObject = ((Class)localObject).getComponentType();
/*      */       }
/* 1336 */       StringBuffer localStringBuffer = new StringBuffer();
/* 1337 */       for (int j = 0; j < i; j++) {
/* 1338 */         localStringBuffer.append("[");
/*      */       }
/* 1340 */       localStringBuffer.append(getSignature((Class)localObject));
/* 1341 */       str = localStringBuffer.toString();
/* 1342 */     } else if (paramClass.isPrimitive()) {
/* 1343 */       if (paramClass == Integer.TYPE)
/* 1344 */         str = "I";
/* 1345 */       else if (paramClass == Byte.TYPE)
/* 1346 */         str = "B";
/* 1347 */       else if (paramClass == Long.TYPE)
/* 1348 */         str = "J";
/* 1349 */       else if (paramClass == Float.TYPE)
/* 1350 */         str = "F";
/* 1351 */       else if (paramClass == Double.TYPE)
/* 1352 */         str = "D";
/* 1353 */       else if (paramClass == Short.TYPE)
/* 1354 */         str = "S";
/* 1355 */       else if (paramClass == Character.TYPE)
/* 1356 */         str = "C";
/* 1357 */       else if (paramClass == Boolean.TYPE)
/* 1358 */         str = "Z";
/* 1359 */       else if (paramClass == Void.TYPE)
/* 1360 */         str = "V";
/*      */     }
/*      */     else {
/* 1363 */       str = "L" + paramClass.getName().replace('.', '/') + ";";
/*      */     }
/* 1365 */     return str;
/*      */   }
/*      */ 
/*      */   static String getSignature(Method paramMethod)
/*      */   {
/* 1372 */     StringBuffer localStringBuffer = new StringBuffer();
/*      */ 
/* 1374 */     localStringBuffer.append("(");
/*      */ 
/* 1376 */     Class[] arrayOfClass = paramMethod.getParameterTypes();
/* 1377 */     for (int i = 0; i < arrayOfClass.length; i++) {
/* 1378 */       localStringBuffer.append(getSignature(arrayOfClass[i]));
/*      */     }
/* 1380 */     localStringBuffer.append(")");
/* 1381 */     localStringBuffer.append(getSignature(paramMethod.getReturnType()));
/* 1382 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   static String getSignature(Constructor paramConstructor)
/*      */   {
/* 1389 */     StringBuffer localStringBuffer = new StringBuffer();
/*      */ 
/* 1391 */     localStringBuffer.append("(");
/*      */ 
/* 1393 */     Class[] arrayOfClass = paramConstructor.getParameterTypes();
/* 1394 */     for (int i = 0; i < arrayOfClass.length; i++) {
/* 1395 */       localStringBuffer.append(getSignature(arrayOfClass[i]));
/*      */     }
/* 1397 */     localStringBuffer.append(")V");
/* 1398 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private static ObjectStreamClass findDescriptorFor(Class<?> paramClass)
/*      */   {
/* 1415 */     int i = paramClass.hashCode();
/* 1416 */     int j = (i & 0x7FFFFFFF) % descriptorFor.length;
/*      */     ObjectStreamClassEntry localObjectStreamClassEntry1;
/* 1421 */     while (((localObjectStreamClassEntry1 = descriptorFor[j]) != null) && (localObjectStreamClassEntry1.get() == null)) {
/* 1422 */       descriptorFor[j] = localObjectStreamClassEntry1.next;
/*      */     }
/*      */ 
/* 1428 */     ObjectStreamClassEntry localObjectStreamClassEntry2 = localObjectStreamClassEntry1;
/* 1429 */     while (localObjectStreamClassEntry1 != null) {
/* 1430 */       ObjectStreamClass localObjectStreamClass = (ObjectStreamClass)localObjectStreamClassEntry1.get();
/* 1431 */       if (localObjectStreamClass == null)
/*      */       {
/* 1433 */         localObjectStreamClassEntry2.next = localObjectStreamClassEntry1.next;
/*      */       } else {
/* 1435 */         if (localObjectStreamClass.ofClass == paramClass)
/* 1436 */           return localObjectStreamClass;
/* 1437 */         localObjectStreamClassEntry2 = localObjectStreamClassEntry1;
/*      */       }
/* 1439 */       localObjectStreamClassEntry1 = localObjectStreamClassEntry1.next;
/*      */     }
/* 1441 */     return null;
/*      */   }
/*      */ 
/*      */   private static void insertDescriptorFor(ObjectStreamClass paramObjectStreamClass)
/*      */   {
/* 1449 */     if (findDescriptorFor(paramObjectStreamClass.ofClass) != null) {
/* 1450 */       return;
/*      */     }
/*      */ 
/* 1453 */     int i = paramObjectStreamClass.ofClass.hashCode();
/* 1454 */     int j = (i & 0x7FFFFFFF) % descriptorFor.length;
/* 1455 */     ObjectStreamClassEntry localObjectStreamClassEntry = new ObjectStreamClassEntry(paramObjectStreamClass);
/* 1456 */     localObjectStreamClassEntry.next = descriptorFor[j];
/* 1457 */     descriptorFor[j] = localObjectStreamClassEntry;
/*      */   }
/*      */ 
/*      */   private static Field[] getDeclaredFields(Class<?> paramClass) {
/* 1461 */     return (Field[])AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public Object run() {
/* 1463 */         return this.val$clz.getDeclaredFields();
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private static boolean hasStaticInitializer(Class<?> paramClass)
/*      */   {
/*      */     Object localObject;
/* 1567 */     if (hasStaticInitializerMethod == null) {
/* 1568 */       localObject = null;
/*      */       try
/*      */       {
/* 1571 */         if (localObject == null) {
/* 1572 */           localObject = java.io.ObjectStreamClass.class;
/*      */         }
/* 1574 */         hasStaticInitializerMethod = ((Class)localObject).getDeclaredMethod("hasStaticInitializer", new Class[] { Class.class });
/*      */       }
/*      */       catch (NoSuchMethodException localNoSuchMethodException)
/*      */       {
/*      */       }
/*      */ 
/* 1580 */       if (hasStaticInitializerMethod == null)
/*      */       {
/* 1582 */         throw new InternalError("Can't find hasStaticInitializer method on " + ((Class)localObject).getName());
/*      */       }
/*      */ 
/* 1585 */       hasStaticInitializerMethod.setAccessible(true);
/*      */     }
/*      */     try
/*      */     {
/* 1589 */       localObject = (Boolean)hasStaticInitializerMethod.invoke(null, new Object[] { paramClass });
/*      */ 
/* 1591 */       return ((Boolean)localObject).booleanValue();
/*      */     }
/*      */     catch (Exception localException) {
/* 1594 */       InternalError localInternalError = new InternalError("Error invoking hasStaticInitializer");
/* 1595 */       localInternalError.initCause(localException);
/* 1596 */       throw localInternalError;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static Method getInheritableMethod(Class<?> paramClass1, String paramString, Class<?>[] paramArrayOfClass, Class<?> paramClass2)
/*      */   {
/* 1757 */     Method localMethod = null;
/* 1758 */     Object localObject = paramClass1;
/* 1759 */     while (localObject != null) {
/*      */       try {
/* 1761 */         localMethod = ((Class)localObject).getDeclaredMethod(paramString, paramArrayOfClass);
/*      */       }
/*      */       catch (NoSuchMethodException localNoSuchMethodException) {
/* 1764 */         localObject = ((Class)localObject).getSuperclass();
/*      */       }
/*      */     }
/*      */ 
/* 1768 */     if ((localMethod == null) || (localMethod.getReturnType() != paramClass2)) {
/* 1769 */       return null;
/*      */     }
/* 1771 */     localMethod.setAccessible(true);
/* 1772 */     int i = localMethod.getModifiers();
/* 1773 */     if ((i & 0x408) != 0)
/* 1774 */       return null;
/* 1775 */     if ((i & 0x5) != 0)
/* 1776 */       return localMethod;
/* 1777 */     if ((i & 0x2) != 0) {
/* 1778 */       return paramClass1 == localObject ? localMethod : null;
/*      */     }
/* 1780 */     return packageEquals(paramClass1, (Class)localObject) ? localMethod : null;
/*      */   }
/*      */ 
/*      */   private static boolean packageEquals(Class<?> paramClass1, Class<?> paramClass2)
/*      */   {
/* 1791 */     Package localPackage1 = paramClass1.getPackage(); Package localPackage2 = paramClass2.getPackage();
/* 1792 */     return (localPackage1 == localPackage2) || ((localPackage1 != null) && (localPackage1.equals(localPackage2)));
/*      */   }
/*      */ 
/*      */   private static class CompareClassByName
/*      */     implements Comparator
/*      */   {
/*      */     public int compare(Object paramObject1, Object paramObject2)
/*      */     {
/* 1638 */       Class localClass1 = (Class)paramObject1;
/* 1639 */       Class localClass2 = (Class)paramObject2;
/* 1640 */       return localClass1.getName().compareTo(localClass2.getName());
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class CompareMemberByName
/*      */     implements Comparator
/*      */   {
/*      */     public int compare(Object paramObject1, Object paramObject2)
/*      */     {
/* 1667 */       String str1 = ((Member)paramObject1).getName();
/* 1668 */       String str2 = ((Member)paramObject2).getName();
/*      */ 
/* 1670 */       if ((paramObject1 instanceof Method)) {
/* 1671 */         str1 = str1 + ObjectStreamClass.getSignature((Method)paramObject1);
/* 1672 */         str2 = str2 + ObjectStreamClass.getSignature((Method)paramObject2);
/* 1673 */       } else if ((paramObject1 instanceof Constructor)) {
/* 1674 */         str1 = str1 + ObjectStreamClass.getSignature((Constructor)paramObject1);
/* 1675 */         str2 = str2 + ObjectStreamClass.getSignature((Constructor)paramObject2);
/*      */       }
/* 1677 */       return str1.compareTo(str2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class CompareObjStrFieldsByName
/*      */     implements Comparator
/*      */   {
/*      */     public int compare(Object paramObject1, Object paramObject2)
/*      */     {
/* 1652 */       ObjectStreamField localObjectStreamField1 = (ObjectStreamField)paramObject1;
/* 1653 */       ObjectStreamField localObjectStreamField2 = (ObjectStreamField)paramObject2;
/*      */ 
/* 1655 */       return localObjectStreamField1.getName().compareTo(localObjectStreamField2.getName());
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class MethodSignature
/*      */     implements Comparator
/*      */   {
/*      */     Member member;
/*      */     String signature;
/*      */ 
/*      */     static MethodSignature[] removePrivateAndSort(Member[] paramArrayOfMember)
/*      */     {
/* 1692 */       int i = 0;
/* 1693 */       for (int j = 0; j < paramArrayOfMember.length; j++) {
/* 1694 */         if (!Modifier.isPrivate(paramArrayOfMember[j].getModifiers())) {
/* 1695 */           i++;
/*      */         }
/*      */       }
/* 1698 */       MethodSignature[] arrayOfMethodSignature = new MethodSignature[i];
/* 1699 */       int k = 0;
/* 1700 */       for (int m = 0; m < paramArrayOfMember.length; m++) {
/* 1701 */         if (!Modifier.isPrivate(paramArrayOfMember[m].getModifiers())) {
/* 1702 */           arrayOfMethodSignature[k] = new MethodSignature(paramArrayOfMember[m]);
/* 1703 */           k++;
/*      */         }
/*      */       }
/* 1706 */       if (k > 0)
/* 1707 */         Arrays.sort(arrayOfMethodSignature, arrayOfMethodSignature[0]);
/* 1708 */       return arrayOfMethodSignature;
/*      */     }
/*      */ 
/*      */     public int compare(Object paramObject1, Object paramObject2)
/*      */     {
/* 1715 */       if (paramObject1 == paramObject2) {
/* 1716 */         return 0;
/*      */       }
/* 1718 */       MethodSignature localMethodSignature1 = (MethodSignature)paramObject1;
/* 1719 */       MethodSignature localMethodSignature2 = (MethodSignature)paramObject2;
/*      */       int i;
/* 1722 */       if (isConstructor()) {
/* 1723 */         i = localMethodSignature1.signature.compareTo(localMethodSignature2.signature);
/*      */       } else {
/* 1725 */         i = localMethodSignature1.member.getName().compareTo(localMethodSignature2.member.getName());
/* 1726 */         if (i == 0)
/* 1727 */           i = localMethodSignature1.signature.compareTo(localMethodSignature2.signature);
/*      */       }
/* 1729 */       return i;
/*      */     }
/*      */ 
/*      */     private final boolean isConstructor() {
/* 1733 */       return this.member instanceof Constructor;
/*      */     }
/*      */     private MethodSignature(Member paramMember) {
/* 1736 */       this.member = paramMember;
/* 1737 */       if (isConstructor())
/* 1738 */         this.signature = ObjectStreamClass.getSignature((Constructor)paramMember);
/*      */       else
/* 1740 */         this.signature = ObjectStreamClass.getSignature((Method)paramMember);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ObjectStreamClassEntry
/*      */   {
/*      */     ObjectStreamClassEntry next;
/*      */     private ObjectStreamClass c;
/*      */ 
/*      */     ObjectStreamClassEntry(ObjectStreamClass paramObjectStreamClass)
/*      */     {
/* 1619 */       this.c = paramObjectStreamClass;
/*      */     }
/*      */ 
/*      */     public Object get()
/*      */     {
/* 1625 */       return this.c;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class PersistentFieldsValue
/*      */   {
/*  388 */     private final ConcurrentMap map = new ConcurrentHashMap();
/*  389 */     private static final Object NULL_VALUE = PersistentFieldsValue.class.getName() + ".NULL_VALUE";
/*      */ 
/*      */     ObjectStreamField[] get(Class<?> paramClass)
/*      */     {
/*  395 */       Object localObject = this.map.get(paramClass);
/*  396 */       if (localObject == null) {
/*  397 */         localObject = computeValue(paramClass);
/*  398 */         this.map.putIfAbsent(paramClass, localObject);
/*      */       }
/*  400 */       return localObject == NULL_VALUE ? null : (ObjectStreamField[])localObject;
/*      */     }
/*      */ 
/*      */     private static Object computeValue(Class<?> paramClass) {
/*      */       try {
/*  405 */         Field localField = paramClass.getDeclaredField("serialPersistentFields");
/*  406 */         int i = localField.getModifiers();
/*  407 */         if ((Modifier.isPrivate(i)) && (Modifier.isStatic(i)) && (Modifier.isFinal(i)))
/*      */         {
/*  409 */           localField.setAccessible(true);
/*  410 */           java.io.ObjectStreamField[] arrayOfObjectStreamField = (java.io.ObjectStreamField[])localField.get(paramClass);
/*      */ 
/*  412 */           return translateFields(arrayOfObjectStreamField);
/*      */         }
/*      */       } catch (NoSuchFieldException localNoSuchFieldException) {
/*      */       } catch (IllegalAccessException localIllegalAccessException) {
/*      */       } catch (IllegalArgumentException localIllegalArgumentException) {
/*      */       } catch (ClassCastException localClassCastException) {  }
/*      */ 
/*  418 */       return NULL_VALUE;
/*      */     }
/*      */ 
/*      */     private static ObjectStreamField[] translateFields(java.io.ObjectStreamField[] paramArrayOfObjectStreamField)
/*      */     {
/*  423 */       ObjectStreamField[] arrayOfObjectStreamField = new ObjectStreamField[paramArrayOfObjectStreamField.length];
/*      */ 
/*  425 */       for (int i = 0; i < paramArrayOfObjectStreamField.length; i++) {
/*  426 */         arrayOfObjectStreamField[i] = new ObjectStreamField(paramArrayOfObjectStreamField[i].getName(), paramArrayOfObjectStreamField[i].getType());
/*      */       }
/*      */ 
/*  429 */       return arrayOfObjectStreamField;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.io.ObjectStreamClass
 * JD-Core Version:    0.6.2
 */