/*      */ package com.sun.corba.se.impl.orbutil;
/*      */ 
/*      */ import com.sun.corba.se.impl.io.ObjectStreamClass;
/*      */ import com.sun.corba.se.impl.io.ValueUtility;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.Externalizable;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
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
/*      */ import java.util.Hashtable;
/*      */ import org.omg.CORBA.ValueMember;
/*      */ 
/*      */ public class ObjectStreamClass_1_3_1
/*      */   implements Serializable
/*      */ {
/*      */   public static final long kDefaultUID = -1L;
/*   91 */   private static Object[] noArgsList = new Object[0];
/*   92 */   private static Class<?>[] noTypesList = new Class[0];
/*      */   private static Hashtable translatedFields;
/*  991 */   private static ObjectStreamClassEntry[] descriptorFor = new ObjectStreamClassEntry[61];
/*      */   private String name;
/*      */   private ObjectStreamClass_1_3_1 superclass;
/*      */   private boolean serializable;
/*      */   private boolean externalizable;
/*      */   private ObjectStreamField[] fields;
/*      */   private Class<?> ofClass;
/*      */   boolean forProxyClass;
/* 1092 */   private long suid = -1L;
/* 1093 */   private String suidStr = null;
/*      */ 
/* 1098 */   private long actualSuid = -1L;
/* 1099 */   private String actualSuidStr = null;
/*      */   int primBytes;
/*      */   int objFields;
/* 1109 */   private Object lock = new Object();
/*      */   private boolean hasWriteObjectMethod;
/*      */   private boolean hasExternalizableBlockData;
/*      */   Method writeObjectMethod;
/*      */   Method readObjectMethod;
/*      */   private transient Method writeReplaceObjectMethod;
/*      */   private transient Method readResolveObjectMethod;
/*      */   private ObjectStreamClass_1_3_1 localClassDesc;
/*      */   private static final long serialVersionUID = -6120832682080437368L;
/* 1141 */   public static final ObjectStreamField[] NO_FIELDS = new ObjectStreamField[0];
/*      */ 
/* 1166 */   private static Comparator compareClassByName = new CompareClassByName(null);
/*      */ 
/* 1180 */   private static Comparator compareMemberByName = new CompareMemberByName(null);
/*      */ 
/*      */   static final ObjectStreamClass_1_3_1 lookup(Class<?> paramClass)
/*      */   {
/*  102 */     ObjectStreamClass_1_3_1 localObjectStreamClass_1_3_1 = lookupInternal(paramClass);
/*  103 */     if ((localObjectStreamClass_1_3_1.isSerializable()) || (localObjectStreamClass_1_3_1.isExternalizable()))
/*  104 */       return localObjectStreamClass_1_3_1;
/*  105 */     return null;
/*      */   }
/*      */ 
/*      */   static ObjectStreamClass_1_3_1 lookupInternal(Class<?> paramClass)
/*      */   {
/*  117 */     ObjectStreamClass_1_3_1 localObjectStreamClass_1_3_11 = null;
/*  118 */     synchronized (descriptorFor)
/*      */     {
/*  120 */       localObjectStreamClass_1_3_11 = findDescriptorFor(paramClass);
/*  121 */       if (localObjectStreamClass_1_3_11 != null) {
/*  122 */         return localObjectStreamClass_1_3_11;
/*      */       }
/*      */ 
/*  126 */       boolean bool1 = Serializable.class.isAssignableFrom(paramClass);
/*      */ 
/*  130 */       ObjectStreamClass_1_3_1 localObjectStreamClass_1_3_12 = null;
/*  131 */       if (bool1) {
/*  132 */         Class localClass = paramClass.getSuperclass();
/*  133 */         if (localClass != null) {
/*  134 */           localObjectStreamClass_1_3_12 = lookup(localClass);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  141 */       boolean bool2 = false;
/*  142 */       if (bool1) {
/*  143 */         bool2 = ((localObjectStreamClass_1_3_12 != null) && (localObjectStreamClass_1_3_12.isExternalizable())) || (Externalizable.class.isAssignableFrom(paramClass));
/*      */ 
/*  146 */         if (bool2) {
/*  147 */           bool1 = false;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  154 */       localObjectStreamClass_1_3_11 = new ObjectStreamClass_1_3_1(paramClass, localObjectStreamClass_1_3_12, bool1, bool2);
/*      */     }
/*      */ 
/*  157 */     localObjectStreamClass_1_3_11.init();
/*  158 */     return localObjectStreamClass_1_3_11;
/*      */   }
/*      */ 
/*      */   public final String getName()
/*      */   {
/*  165 */     return this.name;
/*      */   }
/*      */ 
/*      */   public static final long getSerialVersionUID(Class<?> paramClass)
/*      */   {
/*  175 */     ObjectStreamClass_1_3_1 localObjectStreamClass_1_3_1 = lookup(paramClass);
/*  176 */     if (localObjectStreamClass_1_3_1 != null)
/*      */     {
/*  178 */       return localObjectStreamClass_1_3_1.getSerialVersionUID();
/*      */     }
/*  180 */     return 0L;
/*      */   }
/*      */ 
/*      */   public final long getSerialVersionUID()
/*      */   {
/*  190 */     return this.suid;
/*      */   }
/*      */ 
/*      */   public final String getSerialVersionUIDStr()
/*      */   {
/*  200 */     if (this.suidStr == null)
/*  201 */       this.suidStr = Long.toHexString(this.suid).toUpperCase();
/*  202 */     return this.suidStr;
/*      */   }
/*      */ 
/*      */   public static final long getActualSerialVersionUID(Class<?> paramClass)
/*      */   {
/*  210 */     ObjectStreamClass_1_3_1 localObjectStreamClass_1_3_1 = lookup(paramClass);
/*  211 */     if (localObjectStreamClass_1_3_1 != null)
/*      */     {
/*  213 */       return localObjectStreamClass_1_3_1.getActualSerialVersionUID();
/*      */     }
/*  215 */     return 0L;
/*      */   }
/*      */ 
/*      */   public final long getActualSerialVersionUID()
/*      */   {
/*  222 */     return this.actualSuid;
/*      */   }
/*      */ 
/*      */   public final String getActualSerialVersionUIDStr()
/*      */   {
/*  229 */     if (this.actualSuidStr == null)
/*  230 */       this.actualSuidStr = Long.toHexString(this.actualSuid).toUpperCase();
/*  231 */     return this.actualSuidStr;
/*      */   }
/*      */ 
/*      */   public final Class<?> forClass()
/*      */   {
/*  239 */     return this.ofClass;
/*      */   }
/*      */ 
/*      */   public ObjectStreamField[] getFields()
/*      */   {
/*  251 */     if (this.fields.length > 0) {
/*  252 */       ObjectStreamField[] arrayOfObjectStreamField = new ObjectStreamField[this.fields.length];
/*  253 */       System.arraycopy(this.fields, 0, arrayOfObjectStreamField, 0, this.fields.length);
/*  254 */       return arrayOfObjectStreamField;
/*      */     }
/*  256 */     return this.fields;
/*      */   }
/*      */ 
/*      */   public boolean hasField(ValueMember paramValueMember)
/*      */   {
/*  262 */     for (int i = 0; i < this.fields.length; i++)
/*      */       try {
/*  264 */         if (this.fields[i].getName().equals(paramValueMember.name))
/*      */         {
/*  266 */           if (this.fields[i].getSignature().equals(ValueUtility.getSignature(paramValueMember)))
/*  267 */             return true;
/*      */         }
/*      */       }
/*      */       catch (Throwable localThrowable) {
/*      */       }
/*  272 */     return false;
/*      */   }
/*      */ 
/*      */   final ObjectStreamField[] getFieldsNoCopy()
/*      */   {
/*  277 */     return this.fields;
/*      */   }
/*      */ 
/*      */   public final ObjectStreamField getField(String paramString)
/*      */   {
/*  288 */     for (int i = this.fields.length - 1; i >= 0; i--) {
/*  289 */       if (paramString.equals(this.fields[i].getName())) {
/*  290 */         return this.fields[i];
/*      */       }
/*      */     }
/*  293 */     return null;
/*      */   }
/*      */ 
/*      */   public Serializable writeReplace(Serializable paramSerializable) {
/*  297 */     if (this.writeReplaceObjectMethod != null) {
/*      */       try {
/*  299 */         return (Serializable)this.writeReplaceObjectMethod.invoke(paramSerializable, noArgsList);
/*      */       }
/*      */       catch (Throwable localThrowable) {
/*  302 */         throw new RuntimeException(localThrowable.getMessage());
/*      */       }
/*      */     }
/*  305 */     return paramSerializable;
/*      */   }
/*      */ 
/*      */   public Object readResolve(Object paramObject) {
/*  309 */     if (this.readResolveObjectMethod != null) {
/*      */       try {
/*  311 */         return this.readResolveObjectMethod.invoke(paramObject, noArgsList);
/*      */       }
/*      */       catch (Throwable localThrowable) {
/*  314 */         throw new RuntimeException(localThrowable.getMessage());
/*      */       }
/*      */     }
/*  317 */     return paramObject;
/*      */   }
/*      */ 
/*      */   public final String toString()
/*      */   {
/*  324 */     StringBuffer localStringBuffer = new StringBuffer();
/*      */ 
/*  326 */     localStringBuffer.append(this.name);
/*  327 */     localStringBuffer.append(": static final long serialVersionUID = ");
/*  328 */     localStringBuffer.append(Long.toString(this.suid));
/*  329 */     localStringBuffer.append("L;");
/*  330 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private ObjectStreamClass_1_3_1(Class<?> paramClass, ObjectStreamClass_1_3_1 paramObjectStreamClass_1_3_1, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  340 */     this.ofClass = paramClass;
/*      */ 
/*  342 */     if (Proxy.isProxyClass(paramClass)) {
/*  343 */       this.forProxyClass = true;
/*      */     }
/*      */ 
/*  346 */     this.name = paramClass.getName();
/*  347 */     this.superclass = paramObjectStreamClass_1_3_1;
/*  348 */     this.serializable = paramBoolean1;
/*  349 */     if (!this.forProxyClass)
/*      */     {
/*  351 */       this.externalizable = paramBoolean2;
/*      */     }
/*      */ 
/*  359 */     insertDescriptorFor(this);
/*      */   }
/*      */ 
/*      */   private void init()
/*      */   {
/*  378 */     synchronized (this.lock)
/*      */     {
/*  380 */       final Class localClass = this.ofClass;
/*      */ 
/*  382 */       if (this.fields != null) {
/*  383 */         return;
/*      */       }
/*      */ 
/*  386 */       if ((!this.serializable) || (this.externalizable) || (this.forProxyClass) || (this.name.equals("java.lang.String")))
/*      */       {
/*  390 */         this.fields = NO_FIELDS;
/*  391 */       } else if (this.serializable)
/*      */       {
/*  395 */         AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Object run()
/*      */           {
/*      */             try
/*      */             {
/*  402 */               Field localField1 = localClass.getDeclaredField("serialPersistentFields");
/*      */ 
/*  405 */               localField1.setAccessible(true);
/*      */ 
/*  408 */               java.io.ObjectStreamField[] arrayOfObjectStreamField = (java.io.ObjectStreamField[])localField1.get(localClass);
/*      */ 
/*  410 */               int k = localField1.getModifiers();
/*  411 */               if ((Modifier.isPrivate(k)) && (Modifier.isStatic(k)) && (Modifier.isFinal(k)))
/*      */               {
/*  415 */                 ObjectStreamClass_1_3_1.this.fields = ((ObjectStreamField[])ObjectStreamClass_1_3_1.translateFields((Object[])localField1.get(localClass)));
/*      */               }
/*      */             } catch (NoSuchFieldException localNoSuchFieldException1) {
/*  418 */               ObjectStreamClass_1_3_1.this.fields = null;
/*      */             } catch (IllegalAccessException localIllegalAccessException) {
/*  420 */               ObjectStreamClass_1_3_1.this.fields = null;
/*      */             } catch (IllegalArgumentException localIllegalArgumentException) {
/*  422 */               ObjectStreamClass_1_3_1.this.fields = null;
/*      */             }
/*      */             catch (ClassCastException localClassCastException)
/*      */             {
/*  427 */               ObjectStreamClass_1_3_1.this.fields = null;
/*      */             }
/*      */ 
/*  431 */             if (ObjectStreamClass_1_3_1.this.fields == null)
/*      */             {
/*  440 */               Field[] arrayOfField = localClass.getDeclaredFields();
/*      */ 
/*  442 */               int j = 0;
/*  443 */               ObjectStreamField[] arrayOfObjectStreamField1 = new ObjectStreamField[arrayOfField.length];
/*      */ 
/*  445 */               for (int m = 0; m < arrayOfField.length; m++) {
/*  446 */                 int n = arrayOfField[m].getModifiers();
/*  447 */                 if ((!Modifier.isStatic(n)) && (!Modifier.isTransient(n)))
/*      */                 {
/*  449 */                   arrayOfObjectStreamField1[(j++)] = new ObjectStreamField(arrayOfField[m]);
/*      */                 }
/*      */               }
/*      */ 
/*  453 */               ObjectStreamClass_1_3_1.this.fields = new ObjectStreamField[j];
/*  454 */               System.arraycopy(arrayOfObjectStreamField1, 0, ObjectStreamClass_1_3_1.this.fields, 0, j);
/*      */             }
/*      */             else
/*      */             {
/*  460 */               for (int i = ObjectStreamClass_1_3_1.this.fields.length - 1; i >= 0; i--)
/*      */                 try {
/*  462 */                   Field localField2 = localClass.getDeclaredField(ObjectStreamClass_1_3_1.this.fields[i].getName());
/*  463 */                   if (ObjectStreamClass_1_3_1.this.fields[i].getType() == localField2.getType())
/*      */                   {
/*  465 */                     ObjectStreamClass_1_3_1.this.fields[i].setField(localField2);
/*      */                   }
/*      */                 }
/*      */                 catch (NoSuchFieldException localNoSuchFieldException2)
/*      */                 {
/*      */                 }
/*      */             }
/*  472 */             return null;
/*      */           }
/*      */         });
/*  476 */         if (this.fields.length > 1) {
/*  477 */           Arrays.sort(this.fields);
/*      */         }
/*      */ 
/*  480 */         computeFieldInfo();
/*      */       }
/*      */ 
/*  489 */       if (isNonSerializable()) {
/*  490 */         this.suid = 0L;
/*      */       }
/*      */       else {
/*  493 */         AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Object run()
/*      */           {
/*      */             int i;
/*  495 */             if (ObjectStreamClass_1_3_1.this.forProxyClass)
/*      */             {
/*  497 */               ObjectStreamClass_1_3_1.this.suid = 0L;
/*      */             }
/*      */             else try {
/*  500 */                 Field localField = localClass.getDeclaredField("serialVersionUID");
/*  501 */                 i = localField.getModifiers();
/*      */ 
/*  503 */                 if ((Modifier.isStatic(i)) && (Modifier.isFinal(i)))
/*      */                 {
/*  505 */                   localField.setAccessible(true);
/*  506 */                   ObjectStreamClass_1_3_1.this.suid = localField.getLong(localClass);
/*      */                 }
/*      */                 else
/*      */                 {
/*  512 */                   ObjectStreamClass_1_3_1.this.suid = ObjectStreamClass.getSerialVersionUID(localClass);
/*      */                 }
/*      */               }
/*      */               catch (NoSuchFieldException localNoSuchFieldException)
/*      */               {
/*  517 */                 ObjectStreamClass_1_3_1.this.suid = ObjectStreamClass.getSerialVersionUID(localClass);
/*      */               }
/*      */               catch (IllegalAccessException localIllegalAccessException)
/*      */               {
/*  521 */                 ObjectStreamClass_1_3_1.this.suid = ObjectStreamClass.getSerialVersionUID(localClass);
/*      */               }
/*      */ 
/*      */ 
/*      */             try
/*      */             {
/*  527 */               ObjectStreamClass_1_3_1.this.writeReplaceObjectMethod = localClass.getDeclaredMethod("writeReplace", ObjectStreamClass_1_3_1.noTypesList);
/*  528 */               if (Modifier.isStatic(ObjectStreamClass_1_3_1.this.writeReplaceObjectMethod.getModifiers()))
/*  529 */                 ObjectStreamClass_1_3_1.this.writeReplaceObjectMethod = null;
/*      */               else {
/*  531 */                 ObjectStreamClass_1_3_1.this.writeReplaceObjectMethod.setAccessible(true);
/*      */               }
/*      */             }
/*      */             catch (NoSuchMethodException localNoSuchMethodException1)
/*      */             {
/*      */             }
/*      */             try
/*      */             {
/*  539 */               ObjectStreamClass_1_3_1.this.readResolveObjectMethod = localClass.getDeclaredMethod("readResolve", ObjectStreamClass_1_3_1.noTypesList);
/*  540 */               if (Modifier.isStatic(ObjectStreamClass_1_3_1.this.readResolveObjectMethod.getModifiers()))
/*  541 */                 ObjectStreamClass_1_3_1.this.readResolveObjectMethod = null;
/*      */               else {
/*  543 */                 ObjectStreamClass_1_3_1.this.readResolveObjectMethod.setAccessible(true);
/*      */               }
/*      */ 
/*      */             }
/*      */             catch (NoSuchMethodException localNoSuchMethodException2)
/*      */             {
/*      */             }
/*      */ 
/*  555 */             if ((ObjectStreamClass_1_3_1.this.serializable) && (!ObjectStreamClass_1_3_1.this.forProxyClass))
/*      */             {
/*      */               try
/*      */               {
/*  562 */                 Class[] arrayOfClass1 = { ObjectOutputStream.class };
/*  563 */                 ObjectStreamClass_1_3_1.this.writeObjectMethod = localClass.getDeclaredMethod("writeObject", arrayOfClass1);
/*  564 */                 ObjectStreamClass_1_3_1.this.hasWriteObjectMethod = true;
/*  565 */                 i = ObjectStreamClass_1_3_1.this.writeObjectMethod.getModifiers();
/*      */ 
/*  568 */                 if ((!Modifier.isPrivate(i)) || (Modifier.isStatic(i)))
/*      */                 {
/*  570 */                   ObjectStreamClass_1_3_1.this.writeObjectMethod = null;
/*  571 */                   ObjectStreamClass_1_3_1.this.hasWriteObjectMethod = false;
/*      */                 }
/*      */ 
/*      */               }
/*      */               catch (NoSuchMethodException localNoSuchMethodException3)
/*      */               {
/*      */               }
/*      */ 
/*      */               try
/*      */               {
/*  582 */                 Class[] arrayOfClass2 = { ObjectInputStream.class };
/*  583 */                 ObjectStreamClass_1_3_1.this.readObjectMethod = localClass.getDeclaredMethod("readObject", arrayOfClass2);
/*  584 */                 i = ObjectStreamClass_1_3_1.this.readObjectMethod.getModifiers();
/*      */ 
/*  587 */                 if ((!Modifier.isPrivate(i)) || (Modifier.isStatic(i)))
/*      */                 {
/*  589 */                   ObjectStreamClass_1_3_1.this.readObjectMethod = null;
/*      */                 }
/*      */               }
/*      */               catch (NoSuchMethodException localNoSuchMethodException4)
/*      */               {
/*      */               }
/*      */             }
/*      */ 
/*  597 */             return null;
/*      */           }
/*      */         });
/*      */       }
/*      */ 
/*  602 */       this.actualSuid = computeStructuralUID(this, localClass);
/*      */     }
/*      */   }
/*      */ 
/*      */   ObjectStreamClass_1_3_1(String paramString, long paramLong)
/*      */   {
/*  614 */     this.name = paramString;
/*  615 */     this.suid = paramLong;
/*  616 */     this.superclass = null;
/*      */   }
/*      */ 
/*      */   private static Object[] translateFields(Object[] paramArrayOfObject) throws NoSuchFieldException
/*      */   {
/*      */     try {
/*  622 */       java.io.ObjectStreamField[] arrayOfObjectStreamField = (java.io.ObjectStreamField[])paramArrayOfObject;
/*  623 */       Object[] arrayOfObject1 = null;
/*      */ 
/*  625 */       if (translatedFields == null) {
/*  626 */         translatedFields = new Hashtable();
/*      */       }
/*  628 */       arrayOfObject1 = (Object[])translatedFields.get(arrayOfObjectStreamField);
/*      */ 
/*  630 */       if (arrayOfObject1 != null) {
/*  631 */         return arrayOfObject1;
/*      */       }
/*  633 */       ObjectStreamField localObjectStreamField = ObjectStreamField.class;
/*      */ 
/*  635 */       arrayOfObject1 = (Object[])Array.newInstance(localObjectStreamField, paramArrayOfObject.length);
/*  636 */       Object[] arrayOfObject2 = new Object[2];
/*  637 */       Class[] arrayOfClass = { String.class, Class.class };
/*  638 */       Constructor localConstructor = localObjectStreamField.getDeclaredConstructor(arrayOfClass);
/*  639 */       for (int i = arrayOfObjectStreamField.length - 1; i >= 0; i--) {
/*  640 */         arrayOfObject2[0] = arrayOfObjectStreamField[i].getName();
/*  641 */         arrayOfObject2[1] = arrayOfObjectStreamField[i].getType();
/*      */ 
/*  643 */         arrayOfObject1[i] = localConstructor.newInstance(arrayOfObject2);
/*      */       }
/*  645 */       translatedFields.put(arrayOfObjectStreamField, arrayOfObject1);
/*      */ 
/*  649 */       return (Object[])arrayOfObject1;
/*      */     } catch (Throwable localThrowable) {
/*      */     }
/*  652 */     throw new NoSuchFieldException();
/*      */   }
/*      */ 
/*      */   static boolean compareClassNames(String paramString1, String paramString2, char paramChar)
/*      */   {
/*  669 */     int i = paramString1.lastIndexOf(paramChar);
/*  670 */     if (i < 0) {
/*  671 */       i = 0;
/*      */     }
/*  673 */     int j = paramString2.lastIndexOf(paramChar);
/*  674 */     if (j < 0) {
/*  675 */       j = 0;
/*      */     }
/*  677 */     return paramString1.regionMatches(false, i, paramString2, j, paramString1.length() - i);
/*      */   }
/*      */ 
/*      */   final boolean typeEquals(ObjectStreamClass_1_3_1 paramObjectStreamClass_1_3_1)
/*      */   {
/*  687 */     return (this.suid == paramObjectStreamClass_1_3_1.suid) && (compareClassNames(this.name, paramObjectStreamClass_1_3_1.name, '.'));
/*      */   }
/*      */ 
/*      */   final void setSuperclass(ObjectStreamClass_1_3_1 paramObjectStreamClass_1_3_1)
/*      */   {
/*  695 */     this.superclass = paramObjectStreamClass_1_3_1;
/*      */   }
/*      */ 
/*      */   final ObjectStreamClass_1_3_1 getSuperclass()
/*      */   {
/*  702 */     return this.superclass;
/*      */   }
/*      */ 
/*      */   final boolean hasWriteObject()
/*      */   {
/*  709 */     return this.hasWriteObjectMethod;
/*      */   }
/*      */ 
/*      */   final boolean isCustomMarshaled() {
/*  713 */     return (hasWriteObject()) || (isExternalizable());
/*      */   }
/*      */ 
/*      */   boolean hasExternalizableBlockDataMode()
/*      */   {
/*  743 */     return this.hasExternalizableBlockData;
/*      */   }
/*      */ 
/*      */   final ObjectStreamClass_1_3_1 localClassDescriptor()
/*      */   {
/*  750 */     return this.localClassDesc;
/*      */   }
/*      */ 
/*      */   boolean isSerializable()
/*      */   {
/*  757 */     return this.serializable;
/*      */   }
/*      */ 
/*      */   boolean isExternalizable()
/*      */   {
/*  764 */     return this.externalizable;
/*      */   }
/*      */ 
/*      */   boolean isNonSerializable() {
/*  768 */     return (!this.externalizable) && (!this.serializable);
/*      */   }
/*      */ 
/*      */   private void computeFieldInfo()
/*      */   {
/*  777 */     this.primBytes = 0;
/*  778 */     this.objFields = 0;
/*      */ 
/*  780 */     for (int i = 0; i < this.fields.length; i++)
/*  781 */       switch (this.fields[i].getTypeCode()) {
/*      */       case 'B':
/*      */       case 'Z':
/*  784 */         this.primBytes += 1;
/*  785 */         break;
/*      */       case 'C':
/*      */       case 'S':
/*  788 */         this.primBytes += 2;
/*  789 */         break;
/*      */       case 'F':
/*      */       case 'I':
/*  793 */         this.primBytes += 4;
/*  794 */         break;
/*      */       case 'D':
/*      */       case 'J':
/*  797 */         this.primBytes += 8;
/*  798 */         break;
/*      */       case 'L':
/*      */       case '[':
/*  802 */         this.objFields += 1;
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
/*  809 */   private static long computeStructuralUID(ObjectStreamClass_1_3_1 paramObjectStreamClass_1_3_1, Class<?> paramClass) { ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(512);
/*      */ 
/*  811 */     long l = 0L;
/*      */     try
/*      */     {
/*  814 */       if ((!Serializable.class.isAssignableFrom(paramClass)) || (paramClass.isInterface()))
/*      */       {
/*  816 */         return 0L;
/*      */       }
/*      */ 
/*  819 */       if (Externalizable.class.isAssignableFrom(paramClass)) {
/*  820 */         return 1L;
/*      */       }
/*      */ 
/*  823 */       MessageDigest localMessageDigest = MessageDigest.getInstance("SHA");
/*  824 */       DigestOutputStream localDigestOutputStream = new DigestOutputStream(localByteArrayOutputStream, localMessageDigest);
/*  825 */       DataOutputStream localDataOutputStream = new DataOutputStream(localDigestOutputStream);
/*      */ 
/*  828 */       Class localClass = paramClass.getSuperclass();
/*  829 */       if (localClass != null)
/*      */       {
/*  836 */         localDataOutputStream.writeLong(computeStructuralUID(lookup(localClass), localClass));
/*      */       }
/*      */ 
/*  839 */       if (paramObjectStreamClass_1_3_1.hasWriteObject())
/*  840 */         localDataOutputStream.writeInt(2);
/*      */       else {
/*  842 */         localDataOutputStream.writeInt(1);
/*      */       }
/*      */ 
/*  847 */       ObjectStreamField[] arrayOfObjectStreamField = paramObjectStreamClass_1_3_1.getFields();
/*      */ 
/*  852 */       int i = 0;
/*  853 */       for (int j = 0; j < arrayOfObjectStreamField.length; j++) {
/*  854 */         if (arrayOfObjectStreamField[j].getField() != null)
/*  855 */           i++;
/*      */       }
/*  857 */       Field[] arrayOfField = new Field[i];
/*  858 */       int k = 0; for (int m = 0; k < arrayOfObjectStreamField.length; k++) {
/*  859 */         if (arrayOfObjectStreamField[k].getField() != null) {
/*  860 */           arrayOfField[(m++)] = arrayOfObjectStreamField[k].getField();
/*      */         }
/*      */       }
/*      */ 
/*  864 */       if (arrayOfField.length > 1) {
/*  865 */         Arrays.sort(arrayOfField, compareMemberByName);
/*      */       }
/*  867 */       for (k = 0; k < arrayOfField.length; k++) {
/*  868 */         Field localField = arrayOfField[k];
/*      */ 
/*  873 */         int i1 = localField.getModifiers();
/*      */ 
/*  887 */         localDataOutputStream.writeUTF(localField.getName());
/*  888 */         localDataOutputStream.writeUTF(getSignature(localField.getType()));
/*      */       }
/*      */ 
/*  894 */       localDataOutputStream.flush();
/*  895 */       byte[] arrayOfByte = localMessageDigest.digest();
/*      */ 
/*  899 */       for (int n = 0; n < Math.min(8, arrayOfByte.length); n++)
/*  900 */         l += ((arrayOfByte[n] & 0xFF) << n * 8);
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*  904 */       l = -1L;
/*      */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/*  906 */       throw new SecurityException(localNoSuchAlgorithmException.getMessage());
/*      */     }
/*  908 */     return l;
/*      */   }
/*      */ 
/*      */   static String getSignature(Class<?> paramClass)
/*      */   {
/*  915 */     String str = null;
/*  916 */     if (paramClass.isArray()) {
/*  917 */       Object localObject = paramClass;
/*  918 */       int i = 0;
/*  919 */       while (((Class)localObject).isArray()) {
/*  920 */         i++;
/*  921 */         localObject = ((Class)localObject).getComponentType();
/*      */       }
/*  923 */       StringBuffer localStringBuffer = new StringBuffer();
/*  924 */       for (int j = 0; j < i; j++) {
/*  925 */         localStringBuffer.append("[");
/*      */       }
/*  927 */       localStringBuffer.append(getSignature((Class)localObject));
/*  928 */       str = localStringBuffer.toString();
/*  929 */     } else if (paramClass.isPrimitive()) {
/*  930 */       if (paramClass == Integer.TYPE)
/*  931 */         str = "I";
/*  932 */       else if (paramClass == Byte.TYPE)
/*  933 */         str = "B";
/*  934 */       else if (paramClass == Long.TYPE)
/*  935 */         str = "J";
/*  936 */       else if (paramClass == Float.TYPE)
/*  937 */         str = "F";
/*  938 */       else if (paramClass == Double.TYPE)
/*  939 */         str = "D";
/*  940 */       else if (paramClass == Short.TYPE)
/*  941 */         str = "S";
/*  942 */       else if (paramClass == Character.TYPE)
/*  943 */         str = "C";
/*  944 */       else if (paramClass == Boolean.TYPE)
/*  945 */         str = "Z";
/*  946 */       else if (paramClass == Void.TYPE)
/*  947 */         str = "V";
/*      */     }
/*      */     else {
/*  950 */       str = "L" + paramClass.getName().replace('.', '/') + ";";
/*      */     }
/*  952 */     return str;
/*      */   }
/*      */ 
/*      */   static String getSignature(Method paramMethod)
/*      */   {
/*  959 */     StringBuffer localStringBuffer = new StringBuffer();
/*      */ 
/*  961 */     localStringBuffer.append("(");
/*      */ 
/*  963 */     Class[] arrayOfClass = paramMethod.getParameterTypes();
/*  964 */     for (int i = 0; i < arrayOfClass.length; i++) {
/*  965 */       localStringBuffer.append(getSignature(arrayOfClass[i]));
/*      */     }
/*  967 */     localStringBuffer.append(")");
/*  968 */     localStringBuffer.append(getSignature(paramMethod.getReturnType()));
/*  969 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   static String getSignature(Constructor paramConstructor)
/*      */   {
/*  976 */     StringBuffer localStringBuffer = new StringBuffer();
/*      */ 
/*  978 */     localStringBuffer.append("(");
/*      */ 
/*  980 */     Class[] arrayOfClass = paramConstructor.getParameterTypes();
/*  981 */     for (int i = 0; i < arrayOfClass.length; i++) {
/*  982 */       localStringBuffer.append(getSignature(arrayOfClass[i]));
/*      */     }
/*  984 */     localStringBuffer.append(")V");
/*  985 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private static ObjectStreamClass_1_3_1 findDescriptorFor(Class<?> paramClass)
/*      */   {
/* 1002 */     int i = paramClass.hashCode();
/* 1003 */     int j = (i & 0x7FFFFFFF) % descriptorFor.length;
/*      */     ObjectStreamClassEntry localObjectStreamClassEntry1;
/* 1008 */     while (((localObjectStreamClassEntry1 = descriptorFor[j]) != null) && (localObjectStreamClassEntry1.get() == null)) {
/* 1009 */       descriptorFor[j] = localObjectStreamClassEntry1.next;
/*      */     }
/*      */ 
/* 1015 */     ObjectStreamClassEntry localObjectStreamClassEntry2 = localObjectStreamClassEntry1;
/* 1016 */     while (localObjectStreamClassEntry1 != null) {
/* 1017 */       ObjectStreamClass_1_3_1 localObjectStreamClass_1_3_1 = (ObjectStreamClass_1_3_1)localObjectStreamClassEntry1.get();
/* 1018 */       if (localObjectStreamClass_1_3_1 == null)
/*      */       {
/* 1020 */         localObjectStreamClassEntry2.next = localObjectStreamClassEntry1.next;
/*      */       } else {
/* 1022 */         if (localObjectStreamClass_1_3_1.ofClass == paramClass)
/* 1023 */           return localObjectStreamClass_1_3_1;
/* 1024 */         localObjectStreamClassEntry2 = localObjectStreamClassEntry1;
/*      */       }
/* 1026 */       localObjectStreamClassEntry1 = localObjectStreamClassEntry1.next;
/*      */     }
/* 1028 */     return null;
/*      */   }
/*      */ 
/*      */   private static void insertDescriptorFor(ObjectStreamClass_1_3_1 paramObjectStreamClass_1_3_1)
/*      */   {
/* 1036 */     if (findDescriptorFor(paramObjectStreamClass_1_3_1.ofClass) != null) {
/* 1037 */       return;
/*      */     }
/*      */ 
/* 1040 */     int i = paramObjectStreamClass_1_3_1.ofClass.hashCode();
/* 1041 */     int j = (i & 0x7FFFFFFF) % descriptorFor.length;
/* 1042 */     ObjectStreamClassEntry localObjectStreamClassEntry = new ObjectStreamClassEntry(paramObjectStreamClass_1_3_1);
/* 1043 */     localObjectStreamClassEntry.next = descriptorFor[j];
/* 1044 */     descriptorFor[j] = localObjectStreamClassEntry;
/*      */   }
/*      */ 
/*      */   private static Field[] getDeclaredFields(Class paramClass) {
/* 1048 */     return (Field[])AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public Object run() {
/* 1050 */         return this.val$clz.getDeclaredFields();
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   private static class CompareClassByName
/*      */     implements Comparator
/*      */   {
/*      */     public int compare(Object paramObject1, Object paramObject2)
/*      */     {
/* 1171 */       Class localClass1 = (Class)paramObject1;
/* 1172 */       Class localClass2 = (Class)paramObject2;
/* 1173 */       return localClass1.getName().compareTo(localClass2.getName());
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class CompareMemberByName
/*      */     implements Comparator
/*      */   {
/*      */     public int compare(Object paramObject1, Object paramObject2)
/*      */     {
/* 1185 */       String str1 = ((Member)paramObject1).getName();
/* 1186 */       String str2 = ((Member)paramObject2).getName();
/*      */ 
/* 1188 */       if ((paramObject1 instanceof Method)) {
/* 1189 */         str1 = str1 + ObjectStreamClass_1_3_1.getSignature((Method)paramObject1);
/* 1190 */         str2 = str2 + ObjectStreamClass_1_3_1.getSignature((Method)paramObject2);
/* 1191 */       } else if ((paramObject1 instanceof Constructor)) {
/* 1192 */         str1 = str1 + ObjectStreamClass_1_3_1.getSignature((Constructor)paramObject1);
/* 1193 */         str2 = str2 + ObjectStreamClass_1_3_1.getSignature((Constructor)paramObject2);
/*      */       }
/* 1195 */       return str1.compareTo(str2);
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
/* 1210 */       int i = 0;
/* 1211 */       for (int j = 0; j < paramArrayOfMember.length; j++) {
/* 1212 */         if (!Modifier.isPrivate(paramArrayOfMember[j].getModifiers())) {
/* 1213 */           i++;
/*      */         }
/*      */       }
/* 1216 */       MethodSignature[] arrayOfMethodSignature = new MethodSignature[i];
/* 1217 */       int k = 0;
/* 1218 */       for (int m = 0; m < paramArrayOfMember.length; m++) {
/* 1219 */         if (!Modifier.isPrivate(paramArrayOfMember[m].getModifiers())) {
/* 1220 */           arrayOfMethodSignature[k] = new MethodSignature(paramArrayOfMember[m]);
/* 1221 */           k++;
/*      */         }
/*      */       }
/* 1224 */       if (k > 0)
/* 1225 */         Arrays.sort(arrayOfMethodSignature, arrayOfMethodSignature[0]);
/* 1226 */       return arrayOfMethodSignature;
/*      */     }
/*      */ 
/*      */     public int compare(Object paramObject1, Object paramObject2)
/*      */     {
/* 1233 */       if (paramObject1 == paramObject2) {
/* 1234 */         return 0;
/*      */       }
/* 1236 */       MethodSignature localMethodSignature1 = (MethodSignature)paramObject1;
/* 1237 */       MethodSignature localMethodSignature2 = (MethodSignature)paramObject2;
/*      */       int i;
/* 1240 */       if (isConstructor()) {
/* 1241 */         i = localMethodSignature1.signature.compareTo(localMethodSignature2.signature);
/*      */       } else {
/* 1243 */         i = localMethodSignature1.member.getName().compareTo(localMethodSignature2.member.getName());
/* 1244 */         if (i == 0)
/* 1245 */           i = localMethodSignature1.signature.compareTo(localMethodSignature2.signature);
/*      */       }
/* 1247 */       return i;
/*      */     }
/*      */ 
/*      */     private final boolean isConstructor() {
/* 1251 */       return this.member instanceof Constructor;
/*      */     }
/*      */     private MethodSignature(Member paramMember) {
/* 1254 */       this.member = paramMember;
/* 1255 */       if (isConstructor())
/* 1256 */         this.signature = ObjectStreamClass_1_3_1.getSignature((Constructor)paramMember);
/*      */       else
/* 1258 */         this.signature = ObjectStreamClass_1_3_1.getSignature((Method)paramMember);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ObjectStreamClassEntry
/*      */   {
/*      */     ObjectStreamClassEntry next;
/*      */     private ObjectStreamClass_1_3_1 c;
/*      */ 
/*      */     ObjectStreamClassEntry(ObjectStreamClass_1_3_1 paramObjectStreamClass_1_3_1)
/*      */     {
/* 1152 */       this.c = paramObjectStreamClass_1_3_1;
/*      */     }
/*      */ 
/*      */     public Object get()
/*      */     {
/* 1158 */       return this.c;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.ObjectStreamClass_1_3_1
 * JD-Core Version:    0.6.2
 */