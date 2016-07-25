/*      */ package java.io;
/*      */ 
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.ref.SoftReference;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Member;
/*      */ import java.lang.reflect.Method;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.security.AccessController;
/*      */ import java.security.MessageDigest;
/*      */ import java.security.NoSuchAlgorithmException;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashSet;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import sun.misc.Unsafe;
/*      */ import sun.reflect.CallerSensitive;
/*      */ import sun.reflect.Reflection;
/*      */ import sun.reflect.ReflectionFactory;
/*      */ import sun.reflect.ReflectionFactory.GetReflectionFactoryAction;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ 
/*      */ public class ObjectStreamClass
/*      */   implements Serializable
/*      */ {
/*   75 */   public static final ObjectStreamField[] NO_FIELDS = new ObjectStreamField[0];
/*      */   private static final long serialVersionUID = -6120832682080437368L;
/*   79 */   private static final ObjectStreamField[] serialPersistentFields = NO_FIELDS;
/*      */ 
/*   83 */   private static final ReflectionFactory reflFactory = (ReflectionFactory)AccessController.doPrivileged(new ReflectionFactory.GetReflectionFactoryAction());
/*      */   private Class<?> cl;
/*      */   private String name;
/*      */   private volatile Long suid;
/*      */   private boolean isProxy;
/*      */   private boolean isEnum;
/*      */   private boolean serializable;
/*      */   private boolean externalizable;
/*      */   private boolean hasWriteObjectData;
/*  127 */   private boolean hasBlockExternalData = true;
/*      */   private ClassNotFoundException resolveEx;
/*      */   private ExceptionInfo deserializeEx;
/*      */   private ExceptionInfo serializeEx;
/*      */   private ExceptionInfo defaultSerializeEx;
/*      */   private ObjectStreamField[] fields;
/*      */   private int primDataSize;
/*      */   private int numObjFields;
/*      */   private FieldReflector fieldRefl;
/*      */   private volatile ClassDataSlot[] dataLayout;
/*      */   private Constructor cons;
/*      */   private Method writeObjectMethod;
/*      */   private Method readObjectMethod;
/*      */   private Method readObjectNoDataMethod;
/*      */   private Method writeReplaceMethod;
/*      */   private Method readResolveMethod;
/*      */   private ObjectStreamClass localDesc;
/*      */   private ObjectStreamClass superDesc;
/*      */ 
/*      */   private static native void initNative();
/*      */ 
/*      */   public static ObjectStreamClass lookup(Class<?> paramClass)
/*      */   {
/*  210 */     return lookup(paramClass, false);
/*      */   }
/*      */ 
/*      */   public static ObjectStreamClass lookupAny(Class<?> paramClass)
/*      */   {
/*  222 */     return lookup(paramClass, true);
/*      */   }
/*      */ 
/*      */   public String getName()
/*      */   {
/*  233 */     return this.name;
/*      */   }
/*      */ 
/*      */   public long getSerialVersionUID()
/*      */   {
/*  246 */     if (this.suid == null) {
/*  247 */       this.suid = ((Long)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Long run() {
/*  250 */           return Long.valueOf(ObjectStreamClass.computeDefaultSUID(ObjectStreamClass.this.cl));
/*      */         }
/*      */       }));
/*      */     }
/*      */ 
/*  255 */     return this.suid.longValue();
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public Class<?> forClass()
/*      */   {
/*  266 */     if (this.cl == null) {
/*  267 */       return null;
/*      */     }
/*  269 */     if (System.getSecurityManager() != null) {
/*  270 */       Class localClass = Reflection.getCallerClass();
/*  271 */       if (ReflectUtil.needsPackageAccessCheck(localClass.getClassLoader(), this.cl.getClassLoader())) {
/*  272 */         ReflectUtil.checkPackageAccess(this.cl);
/*      */       }
/*      */     }
/*  275 */     return this.cl;
/*      */   }
/*      */ 
/*      */   public ObjectStreamField[] getFields()
/*      */   {
/*  287 */     return getFields(true);
/*      */   }
/*      */ 
/*      */   public ObjectStreamField getField(String paramString)
/*      */   {
/*  298 */     return getField(paramString, null);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  305 */     return this.name + ": static final long serialVersionUID = " + getSerialVersionUID() + "L;";
/*      */   }
/*      */ 
/*      */   static ObjectStreamClass lookup(Class<?> paramClass, boolean paramBoolean)
/*      */   {
/*  318 */     if ((!paramBoolean) && (!Serializable.class.isAssignableFrom(paramClass))) {
/*  319 */       return null;
/*      */     }
/*  321 */     processQueue(Caches.localDescsQueue, Caches.localDescs);
/*  322 */     WeakClassKey localWeakClassKey = new WeakClassKey(paramClass, Caches.localDescsQueue);
/*  323 */     Reference localReference = (Reference)Caches.localDescs.get(localWeakClassKey);
/*  324 */     Object localObject1 = null;
/*  325 */     if (localReference != null) {
/*  326 */       localObject1 = localReference.get();
/*      */     }
/*  328 */     Object localObject2 = null;
/*  329 */     if (localObject1 == null) {
/*  330 */       EntryFuture localEntryFuture = new EntryFuture(null);
/*  331 */       SoftReference localSoftReference = new SoftReference(localEntryFuture);
/*      */       do {
/*  333 */         if (localReference != null) {
/*  334 */           Caches.localDescs.remove(localWeakClassKey, localReference);
/*      */         }
/*  336 */         localReference = (Reference)Caches.localDescs.putIfAbsent(localWeakClassKey, localSoftReference);
/*  337 */         if (localReference != null)
/*  338 */           localObject1 = localReference.get();
/*      */       }
/*  340 */       while ((localReference != null) && (localObject1 == null));
/*  341 */       if (localObject1 == null) {
/*  342 */         localObject2 = localEntryFuture;
/*      */       }
/*      */     }
/*      */ 
/*  346 */     if ((localObject1 instanceof ObjectStreamClass)) {
/*  347 */       return (ObjectStreamClass)localObject1;
/*      */     }
/*  349 */     if ((localObject1 instanceof EntryFuture)) {
/*  350 */       localObject2 = (EntryFuture)localObject1;
/*  351 */       if (((EntryFuture)localObject2).getOwner() == Thread.currentThread())
/*      */       {
/*  358 */         localObject1 = null;
/*      */       }
/*  360 */       else localObject1 = ((EntryFuture)localObject2).get();
/*      */     }
/*      */ 
/*  363 */     if (localObject1 == null) {
/*      */       try {
/*  365 */         localObject1 = new ObjectStreamClass(paramClass);
/*      */       } catch (Throwable localThrowable) {
/*  367 */         localObject1 = localThrowable;
/*      */       }
/*  369 */       if (((EntryFuture)localObject2).set(localObject1)) {
/*  370 */         Caches.localDescs.put(localWeakClassKey, new SoftReference(localObject1));
/*      */       }
/*      */       else {
/*  373 */         localObject1 = ((EntryFuture)localObject2).get();
/*      */       }
/*      */     }
/*      */ 
/*  377 */     if ((localObject1 instanceof ObjectStreamClass))
/*  378 */       return (ObjectStreamClass)localObject1;
/*  379 */     if ((localObject1 instanceof RuntimeException))
/*  380 */       throw ((RuntimeException)localObject1);
/*  381 */     if ((localObject1 instanceof Error)) {
/*  382 */       throw ((Error)localObject1);
/*      */     }
/*  384 */     throw new InternalError("unexpected entry: " + localObject1);
/*      */   }
/*      */ 
/*      */   private ObjectStreamClass(final Class<?> paramClass)
/*      */   {
/*  456 */     this.cl = paramClass;
/*  457 */     this.name = paramClass.getName();
/*  458 */     this.isProxy = Proxy.isProxyClass(paramClass);
/*  459 */     this.isEnum = Enum.class.isAssignableFrom(paramClass);
/*  460 */     this.serializable = Serializable.class.isAssignableFrom(paramClass);
/*  461 */     this.externalizable = Externalizable.class.isAssignableFrom(paramClass);
/*      */ 
/*  463 */     Class localClass = paramClass.getSuperclass();
/*  464 */     this.superDesc = (localClass != null ? lookup(localClass, false) : null);
/*  465 */     this.localDesc = this;
/*      */ 
/*  467 */     if (this.serializable) {
/*  468 */       AccessController.doPrivileged(new PrivilegedAction() {
/*      */         public Void run() {
/*  470 */           if (ObjectStreamClass.this.isEnum) {
/*  471 */             ObjectStreamClass.this.suid = Long.valueOf(0L);
/*  472 */             ObjectStreamClass.this.fields = ObjectStreamClass.NO_FIELDS;
/*  473 */             return null;
/*      */           }
/*  475 */           if (paramClass.isArray()) {
/*  476 */             ObjectStreamClass.this.fields = ObjectStreamClass.NO_FIELDS;
/*  477 */             return null;
/*      */           }
/*      */ 
/*  480 */           ObjectStreamClass.this.suid = ObjectStreamClass.getDeclaredSUID(paramClass);
/*      */           try {
/*  482 */             ObjectStreamClass.this.fields = ObjectStreamClass.getSerialFields(paramClass);
/*  483 */             ObjectStreamClass.this.computeFieldOffsets();
/*      */           } catch (InvalidClassException localInvalidClassException) {
/*  485 */             ObjectStreamClass.this.serializeEx = ObjectStreamClass.access$1102(ObjectStreamClass.this, new ObjectStreamClass.ExceptionInfo(localInvalidClassException.classname, localInvalidClassException.getMessage()));
/*      */ 
/*  487 */             ObjectStreamClass.this.fields = ObjectStreamClass.NO_FIELDS;
/*      */           }
/*      */ 
/*  490 */           if (ObjectStreamClass.this.externalizable) {
/*  491 */             ObjectStreamClass.this.cons = ObjectStreamClass.getExternalizableConstructor(paramClass);
/*      */           } else {
/*  493 */             ObjectStreamClass.this.cons = ObjectStreamClass.getSerializableConstructor(paramClass);
/*  494 */             ObjectStreamClass.this.writeObjectMethod = ObjectStreamClass.getPrivateMethod(paramClass, "writeObject", new Class[] { ObjectOutputStream.class }, Void.TYPE);
/*      */ 
/*  497 */             ObjectStreamClass.this.readObjectMethod = ObjectStreamClass.getPrivateMethod(paramClass, "readObject", new Class[] { ObjectInputStream.class }, Void.TYPE);
/*      */ 
/*  500 */             ObjectStreamClass.this.readObjectNoDataMethod = ObjectStreamClass.getPrivateMethod(paramClass, "readObjectNoData", null, Void.TYPE);
/*      */ 
/*  502 */             ObjectStreamClass.this.hasWriteObjectData = (ObjectStreamClass.this.writeObjectMethod != null);
/*      */           }
/*  504 */           ObjectStreamClass.this.writeReplaceMethod = ObjectStreamClass.getInheritableMethod(paramClass, "writeReplace", null, Object.class);
/*      */ 
/*  506 */           ObjectStreamClass.this.readResolveMethod = ObjectStreamClass.getInheritableMethod(paramClass, "readResolve", null, Object.class);
/*      */ 
/*  508 */           return null;
/*      */         } } );
/*      */     }
/*      */     else {
/*  512 */       this.suid = Long.valueOf(0L);
/*  513 */       this.fields = NO_FIELDS;
/*      */     }
/*      */     try
/*      */     {
/*  517 */       this.fieldRefl = getReflector(this.fields, this);
/*      */     }
/*      */     catch (InvalidClassException localInvalidClassException) {
/*  520 */       throw new InternalError();
/*      */     }
/*      */ 
/*  523 */     if (this.deserializeEx == null) {
/*  524 */       if (this.isEnum)
/*  525 */         this.deserializeEx = new ExceptionInfo(this.name, "enum type");
/*  526 */       else if (this.cons == null) {
/*  527 */         this.deserializeEx = new ExceptionInfo(this.name, "no valid constructor");
/*      */       }
/*      */     }
/*  530 */     for (int i = 0; i < this.fields.length; i++)
/*  531 */       if (this.fields[i].getField() == null)
/*  532 */         this.defaultSerializeEx = new ExceptionInfo(this.name, "unmatched serializable field(s) declared");
/*      */   }
/*      */ 
/*      */   ObjectStreamClass()
/*      */   {
/*      */   }
/*      */ 
/*      */   void initProxy(Class<?> paramClass, ClassNotFoundException paramClassNotFoundException, ObjectStreamClass paramObjectStreamClass)
/*      */     throws InvalidClassException
/*      */   {
/*  553 */     this.cl = paramClass;
/*  554 */     this.resolveEx = paramClassNotFoundException;
/*  555 */     this.superDesc = paramObjectStreamClass;
/*  556 */     this.isProxy = true;
/*  557 */     this.serializable = true;
/*  558 */     this.suid = Long.valueOf(0L);
/*  559 */     this.fields = NO_FIELDS;
/*      */ 
/*  561 */     if (paramClass != null) {
/*  562 */       this.localDesc = lookup(paramClass, true);
/*  563 */       if (!this.localDesc.isProxy) {
/*  564 */         throw new InvalidClassException("cannot bind proxy descriptor to a non-proxy class");
/*      */       }
/*      */ 
/*  567 */       this.name = this.localDesc.name;
/*  568 */       this.externalizable = this.localDesc.externalizable;
/*  569 */       this.cons = this.localDesc.cons;
/*  570 */       this.writeReplaceMethod = this.localDesc.writeReplaceMethod;
/*  571 */       this.readResolveMethod = this.localDesc.readResolveMethod;
/*  572 */       this.deserializeEx = this.localDesc.deserializeEx;
/*      */     }
/*  574 */     this.fieldRefl = getReflector(this.fields, this.localDesc);
/*      */   }
/*      */ 
/*      */   void initNonProxy(ObjectStreamClass paramObjectStreamClass1, Class<?> paramClass, ClassNotFoundException paramClassNotFoundException, ObjectStreamClass paramObjectStreamClass2)
/*      */     throws InvalidClassException
/*      */   {
/*  586 */     this.cl = paramClass;
/*  587 */     this.resolveEx = paramClassNotFoundException;
/*  588 */     this.superDesc = paramObjectStreamClass2;
/*  589 */     this.name = paramObjectStreamClass1.name;
/*  590 */     this.suid = Long.valueOf(paramObjectStreamClass1.getSerialVersionUID());
/*  591 */     this.isProxy = false;
/*  592 */     this.isEnum = paramObjectStreamClass1.isEnum;
/*  593 */     this.serializable = paramObjectStreamClass1.serializable;
/*  594 */     this.externalizable = paramObjectStreamClass1.externalizable;
/*  595 */     this.hasBlockExternalData = paramObjectStreamClass1.hasBlockExternalData;
/*  596 */     this.hasWriteObjectData = paramObjectStreamClass1.hasWriteObjectData;
/*  597 */     this.fields = paramObjectStreamClass1.fields;
/*  598 */     this.primDataSize = paramObjectStreamClass1.primDataSize;
/*  599 */     this.numObjFields = paramObjectStreamClass1.numObjFields;
/*      */ 
/*  601 */     if (paramClass != null) {
/*  602 */       this.localDesc = lookup(paramClass, true);
/*  603 */       if (this.localDesc.isProxy) {
/*  604 */         throw new InvalidClassException("cannot bind non-proxy descriptor to a proxy class");
/*      */       }
/*      */ 
/*  607 */       if (this.isEnum != this.localDesc.isEnum) {
/*  608 */         throw new InvalidClassException(this.isEnum ? "cannot bind enum descriptor to a non-enum class" : "cannot bind non-enum descriptor to an enum class");
/*      */       }
/*      */ 
/*  613 */       if ((this.serializable == this.localDesc.serializable) && (!paramClass.isArray()) && (this.suid.longValue() != this.localDesc.getSerialVersionUID()))
/*      */       {
/*  617 */         throw new InvalidClassException(this.localDesc.name, "local class incompatible: stream classdesc serialVersionUID = " + this.suid + ", local class serialVersionUID = " + this.localDesc.getSerialVersionUID());
/*      */       }
/*      */ 
/*  624 */       if (!classNamesEqual(this.name, this.localDesc.name)) {
/*  625 */         throw new InvalidClassException(this.localDesc.name, "local class name incompatible with stream class name \"" + this.name + "\"");
/*      */       }
/*      */ 
/*  630 */       if (!this.isEnum) {
/*  631 */         if ((this.serializable == this.localDesc.serializable) && (this.externalizable != this.localDesc.externalizable))
/*      */         {
/*  634 */           throw new InvalidClassException(this.localDesc.name, "Serializable incompatible with Externalizable");
/*      */         }
/*      */ 
/*  638 */         if ((this.serializable != this.localDesc.serializable) || (this.externalizable != this.localDesc.externalizable) || ((!this.serializable) && (!this.externalizable)))
/*      */         {
/*  642 */           this.deserializeEx = new ExceptionInfo(this.localDesc.name, "class invalid for deserialization");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  647 */       this.cons = this.localDesc.cons;
/*  648 */       this.writeObjectMethod = this.localDesc.writeObjectMethod;
/*  649 */       this.readObjectMethod = this.localDesc.readObjectMethod;
/*  650 */       this.readObjectNoDataMethod = this.localDesc.readObjectNoDataMethod;
/*  651 */       this.writeReplaceMethod = this.localDesc.writeReplaceMethod;
/*  652 */       this.readResolveMethod = this.localDesc.readResolveMethod;
/*  653 */       if (this.deserializeEx == null) {
/*  654 */         this.deserializeEx = this.localDesc.deserializeEx;
/*      */       }
/*      */     }
/*  657 */     this.fieldRefl = getReflector(this.fields, this.localDesc);
/*      */ 
/*  659 */     this.fields = this.fieldRefl.getFields();
/*      */   }
/*      */ 
/*      */   void readNonProxy(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  671 */     this.name = paramObjectInputStream.readUTF();
/*  672 */     this.suid = Long.valueOf(paramObjectInputStream.readLong());
/*  673 */     this.isProxy = false;
/*      */ 
/*  675 */     int i = paramObjectInputStream.readByte();
/*  676 */     this.hasWriteObjectData = ((i & 0x1) != 0);
/*      */ 
/*  678 */     this.hasBlockExternalData = ((i & 0x8) != 0);
/*      */ 
/*  680 */     this.externalizable = ((i & 0x4) != 0);
/*      */ 
/*  682 */     int j = (i & 0x2) != 0 ? 1 : 0;
/*      */ 
/*  684 */     if ((this.externalizable) && (j != 0)) {
/*  685 */       throw new InvalidClassException(this.name, "serializable and externalizable flags conflict");
/*      */     }
/*      */ 
/*  688 */     this.serializable = ((this.externalizable) || (j != 0));
/*  689 */     this.isEnum = ((i & 0x10) != 0);
/*  690 */     if ((this.isEnum) && (this.suid.longValue() != 0L)) {
/*  691 */       throw new InvalidClassException(this.name, "enum descriptor has non-zero serialVersionUID: " + this.suid);
/*      */     }
/*      */ 
/*  695 */     int k = paramObjectInputStream.readShort();
/*  696 */     if ((this.isEnum) && (k != 0)) {
/*  697 */       throw new InvalidClassException(this.name, "enum descriptor has non-zero field count: " + k);
/*      */     }
/*      */ 
/*  700 */     this.fields = (k > 0 ? new ObjectStreamField[k] : NO_FIELDS);
/*      */ 
/*  702 */     for (int m = 0; m < k; m++) {
/*  703 */       int n = (char)paramObjectInputStream.readByte();
/*  704 */       String str1 = paramObjectInputStream.readUTF();
/*  705 */       String str2 = (n == 76) || (n == 91) ? paramObjectInputStream.readTypeString() : new String(new char[] { n });
/*      */       try
/*      */       {
/*  708 */         this.fields[m] = new ObjectStreamField(str1, str2, false);
/*      */       } catch (RuntimeException localRuntimeException) {
/*  710 */         throw ((IOException)new InvalidClassException(this.name, "invalid descriptor for field " + str1).initCause(localRuntimeException));
/*      */       }
/*      */     }
/*      */ 
/*  714 */     computeFieldOffsets();
/*      */   }
/*      */ 
/*      */   void writeNonProxy(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/*  721 */     paramObjectOutputStream.writeUTF(this.name);
/*  722 */     paramObjectOutputStream.writeLong(getSerialVersionUID());
/*      */ 
/*  724 */     int i = 0;
/*  725 */     if (this.externalizable) {
/*  726 */       i = (byte)(i | 0x4);
/*  727 */       j = paramObjectOutputStream.getProtocolVersion();
/*  728 */       if (j != 1)
/*  729 */         i = (byte)(i | 0x8);
/*      */     }
/*  731 */     else if (this.serializable) {
/*  732 */       i = (byte)(i | 0x2);
/*      */     }
/*  734 */     if (this.hasWriteObjectData) {
/*  735 */       i = (byte)(i | 0x1);
/*      */     }
/*  737 */     if (this.isEnum) {
/*  738 */       i = (byte)(i | 0x10);
/*      */     }
/*  740 */     paramObjectOutputStream.writeByte(i);
/*      */ 
/*  742 */     paramObjectOutputStream.writeShort(this.fields.length);
/*  743 */     for (int j = 0; j < this.fields.length; j++) {
/*  744 */       ObjectStreamField localObjectStreamField = this.fields[j];
/*  745 */       paramObjectOutputStream.writeByte(localObjectStreamField.getTypeCode());
/*  746 */       paramObjectOutputStream.writeUTF(localObjectStreamField.getName());
/*  747 */       if (!localObjectStreamField.isPrimitive())
/*  748 */         paramObjectOutputStream.writeTypeString(localObjectStreamField.getTypeString());
/*      */     }
/*      */   }
/*      */ 
/*      */   ClassNotFoundException getResolveException()
/*      */   {
/*  758 */     return this.resolveEx;
/*      */   }
/*      */ 
/*      */   void checkDeserialize()
/*      */     throws InvalidClassException
/*      */   {
/*  767 */     if (this.deserializeEx != null)
/*  768 */       throw this.deserializeEx.newInvalidClassException();
/*      */   }
/*      */ 
/*      */   void checkSerialize()
/*      */     throws InvalidClassException
/*      */   {
/*  778 */     if (this.serializeEx != null)
/*  779 */       throw this.serializeEx.newInvalidClassException();
/*      */   }
/*      */ 
/*      */   void checkDefaultSerialize()
/*      */     throws InvalidClassException
/*      */   {
/*  791 */     if (this.defaultSerializeEx != null)
/*  792 */       throw this.defaultSerializeEx.newInvalidClassException();
/*      */   }
/*      */ 
/*      */   ObjectStreamClass getSuperDesc()
/*      */   {
/*  802 */     return this.superDesc;
/*      */   }
/*      */ 
/*      */   ObjectStreamClass getLocalDesc()
/*      */   {
/*  812 */     return this.localDesc;
/*      */   }
/*      */ 
/*      */   ObjectStreamField[] getFields(boolean paramBoolean)
/*      */   {
/*  822 */     return paramBoolean ? (ObjectStreamField[])this.fields.clone() : this.fields;
/*      */   }
/*      */ 
/*      */   ObjectStreamField getField(String paramString, Class<?> paramClass)
/*      */   {
/*  832 */     for (int i = 0; i < this.fields.length; i++) {
/*  833 */       ObjectStreamField localObjectStreamField = this.fields[i];
/*  834 */       if (localObjectStreamField.getName().equals(paramString)) {
/*  835 */         if ((paramClass == null) || ((paramClass == Object.class) && (!localObjectStreamField.isPrimitive())))
/*      */         {
/*  838 */           return localObjectStreamField;
/*      */         }
/*  840 */         Class localClass = localObjectStreamField.getType();
/*  841 */         if ((localClass != null) && (paramClass.isAssignableFrom(localClass))) {
/*  842 */           return localObjectStreamField;
/*      */         }
/*      */       }
/*      */     }
/*  846 */     return null;
/*      */   }
/*      */ 
/*      */   boolean isProxy()
/*      */   {
/*  854 */     return this.isProxy;
/*      */   }
/*      */ 
/*      */   boolean isEnum()
/*      */   {
/*  862 */     return this.isEnum;
/*      */   }
/*      */ 
/*      */   boolean isExternalizable()
/*      */   {
/*  870 */     return this.externalizable;
/*      */   }
/*      */ 
/*      */   boolean isSerializable()
/*      */   {
/*  878 */     return this.serializable;
/*      */   }
/*      */ 
/*      */   boolean hasBlockExternalData()
/*      */   {
/*  886 */     return this.hasBlockExternalData;
/*      */   }
/*      */ 
/*      */   boolean hasWriteObjectData()
/*      */   {
/*  895 */     return this.hasWriteObjectData;
/*      */   }
/*      */ 
/*      */   boolean isInstantiable()
/*      */   {
/*  906 */     return this.cons != null;
/*      */   }
/*      */ 
/*      */   boolean hasWriteObjectMethod()
/*      */   {
/*  915 */     return this.writeObjectMethod != null;
/*      */   }
/*      */ 
/*      */   boolean hasReadObjectMethod()
/*      */   {
/*  924 */     return this.readObjectMethod != null;
/*      */   }
/*      */ 
/*      */   boolean hasReadObjectNoDataMethod()
/*      */   {
/*  933 */     return this.readObjectNoDataMethod != null;
/*      */   }
/*      */ 
/*      */   boolean hasWriteReplaceMethod()
/*      */   {
/*  941 */     return this.writeReplaceMethod != null;
/*      */   }
/*      */ 
/*      */   boolean hasReadResolveMethod()
/*      */   {
/*  949 */     return this.readResolveMethod != null;
/*      */   }
/*      */ 
/*      */   Object newInstance()
/*      */     throws InstantiationException, InvocationTargetException, UnsupportedOperationException
/*      */   {
/*  965 */     if (this.cons != null) {
/*      */       try {
/*  967 */         return this.cons.newInstance(new Object[0]);
/*      */       }
/*      */       catch (IllegalAccessException localIllegalAccessException) {
/*  970 */         throw new InternalError();
/*      */       }
/*      */     }
/*  973 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   void invokeWriteObject(Object paramObject, ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException, UnsupportedOperationException
/*      */   {
/*  986 */     if (this.writeObjectMethod != null)
/*      */       try {
/*  988 */         this.writeObjectMethod.invoke(paramObject, new Object[] { paramObjectOutputStream });
/*      */       } catch (InvocationTargetException localInvocationTargetException) {
/*  990 */         Throwable localThrowable = localInvocationTargetException.getTargetException();
/*  991 */         if ((localThrowable instanceof IOException)) {
/*  992 */           throw ((IOException)localThrowable);
/*      */         }
/*  994 */         throwMiscException(localThrowable);
/*      */       }
/*      */       catch (IllegalAccessException localIllegalAccessException)
/*      */       {
/*  998 */         throw new InternalError();
/*      */       }
/*      */     else
/* 1001 */       throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   void invokeReadObject(Object paramObject, ObjectInputStream paramObjectInputStream)
/*      */     throws ClassNotFoundException, IOException, UnsupportedOperationException
/*      */   {
/* 1015 */     if (this.readObjectMethod != null)
/*      */       try {
/* 1017 */         this.readObjectMethod.invoke(paramObject, new Object[] { paramObjectInputStream });
/*      */       } catch (InvocationTargetException localInvocationTargetException) {
/* 1019 */         Throwable localThrowable = localInvocationTargetException.getTargetException();
/* 1020 */         if ((localThrowable instanceof ClassNotFoundException))
/* 1021 */           throw ((ClassNotFoundException)localThrowable);
/* 1022 */         if ((localThrowable instanceof IOException)) {
/* 1023 */           throw ((IOException)localThrowable);
/*      */         }
/* 1025 */         throwMiscException(localThrowable);
/*      */       }
/*      */       catch (IllegalAccessException localIllegalAccessException)
/*      */       {
/* 1029 */         throw new InternalError();
/*      */       }
/*      */     else
/* 1032 */       throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   void invokeReadObjectNoData(Object paramObject)
/*      */     throws IOException, UnsupportedOperationException
/*      */   {
/* 1045 */     if (this.readObjectNoDataMethod != null)
/*      */       try {
/* 1047 */         this.readObjectNoDataMethod.invoke(paramObject, (Object[])null);
/*      */       } catch (InvocationTargetException localInvocationTargetException) {
/* 1049 */         Throwable localThrowable = localInvocationTargetException.getTargetException();
/* 1050 */         if ((localThrowable instanceof ObjectStreamException)) {
/* 1051 */           throw ((ObjectStreamException)localThrowable);
/*      */         }
/* 1053 */         throwMiscException(localThrowable);
/*      */       }
/*      */       catch (IllegalAccessException localIllegalAccessException)
/*      */       {
/* 1057 */         throw new InternalError();
/*      */       }
/*      */     else
/* 1060 */       throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   Object invokeWriteReplace(Object paramObject)
/*      */     throws IOException, UnsupportedOperationException
/*      */   {
/* 1073 */     if (this.writeReplaceMethod != null) {
/*      */       try {
/* 1075 */         return this.writeReplaceMethod.invoke(paramObject, (Object[])null);
/*      */       } catch (InvocationTargetException localInvocationTargetException) {
/* 1077 */         Throwable localThrowable = localInvocationTargetException.getTargetException();
/* 1078 */         if ((localThrowable instanceof ObjectStreamException)) {
/* 1079 */           throw ((ObjectStreamException)localThrowable);
/*      */         }
/* 1081 */         throwMiscException(localThrowable);
/* 1082 */         throw new InternalError();
/*      */       }
/*      */       catch (IllegalAccessException localIllegalAccessException)
/*      */       {
/* 1086 */         throw new InternalError();
/*      */       }
/*      */     }
/* 1089 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   Object invokeReadResolve(Object paramObject)
/*      */     throws IOException, UnsupportedOperationException
/*      */   {
/* 1102 */     if (this.readResolveMethod != null) {
/*      */       try {
/* 1104 */         return this.readResolveMethod.invoke(paramObject, (Object[])null);
/*      */       } catch (InvocationTargetException localInvocationTargetException) {
/* 1106 */         Throwable localThrowable = localInvocationTargetException.getTargetException();
/* 1107 */         if ((localThrowable instanceof ObjectStreamException)) {
/* 1108 */           throw ((ObjectStreamException)localThrowable);
/*      */         }
/* 1110 */         throwMiscException(localThrowable);
/* 1111 */         throw new InternalError();
/*      */       }
/*      */       catch (IllegalAccessException localIllegalAccessException)
/*      */       {
/* 1115 */         throw new InternalError();
/*      */       }
/*      */     }
/* 1118 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   ClassDataSlot[] getClassDataLayout()
/*      */     throws InvalidClassException
/*      */   {
/* 1150 */     if (this.dataLayout == null) {
/* 1151 */       this.dataLayout = getClassDataLayout0();
/*      */     }
/* 1153 */     return this.dataLayout;
/*      */   }
/*      */ 
/*      */   private ClassDataSlot[] getClassDataLayout0()
/*      */     throws InvalidClassException
/*      */   {
/* 1159 */     ArrayList localArrayList = new ArrayList();
/* 1160 */     Class localClass1 = this.cl; Class localClass2 = this.cl;
/*      */ 
/* 1163 */     while ((localClass2 != null) && (Serializable.class.isAssignableFrom(localClass2))) {
/* 1164 */       localClass2 = localClass2.getSuperclass();
/*      */     }
/*      */ 
/* 1167 */     HashSet localHashSet = new HashSet(3);
/*      */ 
/* 1169 */     for (Object localObject1 = this; localObject1 != null; localObject1 = ((ObjectStreamClass)localObject1).superDesc) {
/* 1170 */       if (localHashSet.contains(((ObjectStreamClass)localObject1).name)) {
/* 1171 */         throw new InvalidClassException("Circular reference.");
/*      */       }
/* 1173 */       localHashSet.add(((ObjectStreamClass)localObject1).name);
/*      */ 
/* 1177 */       String str = ((ObjectStreamClass)localObject1).cl != null ? ((ObjectStreamClass)localObject1).cl.getName() : ((ObjectStreamClass)localObject1).name;
/* 1178 */       Object localObject2 = null;
/* 1179 */       for (Class localClass3 = localClass1; localClass3 != localClass2; localClass3 = localClass3.getSuperclass()) {
/* 1180 */         if (str.equals(localClass3.getName())) {
/* 1181 */           localObject2 = localClass3;
/* 1182 */           break;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1187 */       if (localObject2 != null) {
/* 1188 */         for (localClass3 = localClass1; localClass3 != localObject2; localClass3 = localClass3.getSuperclass()) {
/* 1189 */           localArrayList.add(new ClassDataSlot(lookup(localClass3, true), false));
/*      */         }
/*      */ 
/* 1192 */         localClass1 = localObject2.getSuperclass();
/*      */       }
/*      */ 
/* 1196 */       localArrayList.add(new ClassDataSlot(((ObjectStreamClass)localObject1).getVariantFor(localObject2), true));
/*      */     }
/*      */ 
/* 1200 */     for (localObject1 = localClass1; localObject1 != localClass2; localObject1 = ((Class)localObject1).getSuperclass()) {
/* 1201 */       localArrayList.add(new ClassDataSlot(lookup((Class)localObject1, true), false));
/*      */     }
/*      */ 
/* 1206 */     Collections.reverse(localArrayList);
/* 1207 */     return (ClassDataSlot[])localArrayList.toArray(new ClassDataSlot[localArrayList.size()]);
/*      */   }
/*      */ 
/*      */   int getPrimDataSize()
/*      */   {
/* 1215 */     return this.primDataSize;
/*      */   }
/*      */ 
/*      */   int getNumObjFields()
/*      */   {
/* 1223 */     return this.numObjFields;
/*      */   }
/*      */ 
/*      */   void getPrimFieldValues(Object paramObject, byte[] paramArrayOfByte)
/*      */   {
/* 1233 */     this.fieldRefl.getPrimFieldValues(paramObject, paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   void setPrimFieldValues(Object paramObject, byte[] paramArrayOfByte)
/*      */   {
/* 1243 */     this.fieldRefl.setPrimFieldValues(paramObject, paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   void getObjFieldValues(Object paramObject, Object[] paramArrayOfObject)
/*      */   {
/* 1252 */     this.fieldRefl.getObjFieldValues(paramObject, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   void setObjFieldValues(Object paramObject, Object[] paramArrayOfObject)
/*      */   {
/* 1261 */     this.fieldRefl.setObjFieldValues(paramObject, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   private void computeFieldOffsets()
/*      */     throws InvalidClassException
/*      */   {
/* 1270 */     this.primDataSize = 0;
/* 1271 */     this.numObjFields = 0;
/* 1272 */     int i = -1;
/*      */ 
/* 1274 */     for (int j = 0; j < this.fields.length; j++) {
/* 1275 */       ObjectStreamField localObjectStreamField = this.fields[j];
/* 1276 */       switch (localObjectStreamField.getTypeCode()) {
/*      */       case 'B':
/*      */       case 'Z':
/* 1279 */         localObjectStreamField.setOffset(this.primDataSize++);
/* 1280 */         break;
/*      */       case 'C':
/*      */       case 'S':
/* 1284 */         localObjectStreamField.setOffset(this.primDataSize);
/* 1285 */         this.primDataSize += 2;
/* 1286 */         break;
/*      */       case 'F':
/*      */       case 'I':
/* 1290 */         localObjectStreamField.setOffset(this.primDataSize);
/* 1291 */         this.primDataSize += 4;
/* 1292 */         break;
/*      */       case 'D':
/*      */       case 'J':
/* 1296 */         localObjectStreamField.setOffset(this.primDataSize);
/* 1297 */         this.primDataSize += 8;
/* 1298 */         break;
/*      */       case 'L':
/*      */       case '[':
/* 1302 */         localObjectStreamField.setOffset(this.numObjFields++);
/* 1303 */         if (i == -1)
/* 1304 */           i = j; break;
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
/*      */       case 'Y':
/*      */       default:
/* 1309 */         throw new InternalError();
/*      */       }
/*      */     }
/* 1312 */     if ((i != -1) && (i + this.numObjFields != this.fields.length))
/*      */     {
/* 1315 */       throw new InvalidClassException(this.name, "illegal field order");
/*      */     }
/*      */   }
/*      */ 
/*      */   private ObjectStreamClass getVariantFor(Class<?> paramClass)
/*      */     throws InvalidClassException
/*      */   {
/* 1327 */     if (this.cl == paramClass) {
/* 1328 */       return this;
/*      */     }
/* 1330 */     ObjectStreamClass localObjectStreamClass = new ObjectStreamClass();
/* 1331 */     if (this.isProxy)
/* 1332 */       localObjectStreamClass.initProxy(paramClass, null, this.superDesc);
/*      */     else {
/* 1334 */       localObjectStreamClass.initNonProxy(this, paramClass, null, this.superDesc);
/*      */     }
/* 1336 */     return localObjectStreamClass;
/*      */   }
/*      */ 
/*      */   private static Constructor getExternalizableConstructor(Class<?> paramClass)
/*      */   {
/*      */     try
/*      */     {
/* 1346 */       Constructor localConstructor = paramClass.getDeclaredConstructor((Class[])null);
/* 1347 */       localConstructor.setAccessible(true);
/* 1348 */       return (localConstructor.getModifiers() & 0x1) != 0 ? localConstructor : null;
/*      */     } catch (NoSuchMethodException localNoSuchMethodException) {
/*      */     }
/* 1351 */     return null;
/*      */   }
/*      */ 
/*      */   private static Constructor getSerializableConstructor(Class<?> paramClass)
/*      */   {
/* 1361 */     Object localObject = paramClass;
/* 1362 */     while (Serializable.class.isAssignableFrom((Class)localObject)) {
/* 1363 */       if ((localObject = ((Class)localObject).getSuperclass()) == null)
/* 1364 */         return null;
/*      */     }
/*      */     try
/*      */     {
/* 1368 */       Constructor localConstructor = ((Class)localObject).getDeclaredConstructor((Class[])null);
/* 1369 */       int i = localConstructor.getModifiers();
/* 1370 */       if (((i & 0x2) != 0) || (((i & 0x5) == 0) && (!packageEquals(paramClass, (Class)localObject))))
/*      */       {
/* 1374 */         return null;
/*      */       }
/* 1376 */       localConstructor = reflFactory.newConstructorForSerialization(paramClass, localConstructor);
/* 1377 */       localConstructor.setAccessible(true);
/* 1378 */       return localConstructor; } catch (NoSuchMethodException localNoSuchMethodException) {
/*      */     }
/* 1380 */     return null;
/*      */   }
/*      */ 
/*      */   private static Method getInheritableMethod(Class<?> paramClass1, String paramString, Class<?>[] paramArrayOfClass, Class<?> paramClass2)
/*      */   {
/* 1394 */     Method localMethod = null;
/* 1395 */     Object localObject = paramClass1;
/* 1396 */     while (localObject != null) {
/*      */       try {
/* 1398 */         localMethod = ((Class)localObject).getDeclaredMethod(paramString, paramArrayOfClass);
/*      */       }
/*      */       catch (NoSuchMethodException localNoSuchMethodException) {
/* 1401 */         localObject = ((Class)localObject).getSuperclass();
/*      */       }
/*      */     }
/*      */ 
/* 1405 */     if ((localMethod == null) || (localMethod.getReturnType() != paramClass2)) {
/* 1406 */       return null;
/*      */     }
/* 1408 */     localMethod.setAccessible(true);
/* 1409 */     int i = localMethod.getModifiers();
/* 1410 */     if ((i & 0x408) != 0)
/* 1411 */       return null;
/* 1412 */     if ((i & 0x5) != 0)
/* 1413 */       return localMethod;
/* 1414 */     if ((i & 0x2) != 0) {
/* 1415 */       return paramClass1 == localObject ? localMethod : null;
/*      */     }
/* 1417 */     return packageEquals(paramClass1, (Class)localObject) ? localMethod : null;
/*      */   }
/*      */ 
/*      */   private static Method getPrivateMethod(Class<?> paramClass1, String paramString, Class<?>[] paramArrayOfClass, Class<?> paramClass2)
/*      */   {
/*      */     try
/*      */     {
/* 1431 */       Method localMethod = paramClass1.getDeclaredMethod(paramString, paramArrayOfClass);
/* 1432 */       localMethod.setAccessible(true);
/* 1433 */       int i = localMethod.getModifiers();
/* 1434 */       return (localMethod.getReturnType() == paramClass2) && ((i & 0x8) == 0) && ((i & 0x2) != 0) ? localMethod : null;
/*      */     }
/*      */     catch (NoSuchMethodException localNoSuchMethodException) {
/*      */     }
/* 1438 */     return null;
/*      */   }
/*      */ 
/*      */   private static boolean packageEquals(Class<?> paramClass1, Class<?> paramClass2)
/*      */   {
/* 1447 */     return (paramClass1.getClassLoader() == paramClass2.getClassLoader()) && (getPackageName(paramClass1).equals(getPackageName(paramClass2)));
/*      */   }
/*      */ 
/*      */   private static String getPackageName(Class<?> paramClass)
/*      */   {
/* 1455 */     String str = paramClass.getName();
/* 1456 */     int i = str.lastIndexOf('[');
/* 1457 */     if (i >= 0) {
/* 1458 */       str = str.substring(i + 2);
/*      */     }
/* 1460 */     i = str.lastIndexOf('.');
/* 1461 */     return i >= 0 ? str.substring(0, i) : "";
/*      */   }
/*      */ 
/*      */   private static boolean classNamesEqual(String paramString1, String paramString2)
/*      */   {
/* 1469 */     paramString1 = paramString1.substring(paramString1.lastIndexOf('.') + 1);
/* 1470 */     paramString2 = paramString2.substring(paramString2.lastIndexOf('.') + 1);
/* 1471 */     return paramString1.equals(paramString2);
/*      */   }
/*      */ 
/*      */   private static String getClassSignature(Class<?> paramClass)
/*      */   {
/* 1478 */     StringBuilder localStringBuilder = new StringBuilder();
/* 1479 */     while (paramClass.isArray()) {
/* 1480 */       localStringBuilder.append('[');
/* 1481 */       paramClass = paramClass.getComponentType();
/*      */     }
/* 1483 */     if (paramClass.isPrimitive()) {
/* 1484 */       if (paramClass == Integer.TYPE)
/* 1485 */         localStringBuilder.append('I');
/* 1486 */       else if (paramClass == Byte.TYPE)
/* 1487 */         localStringBuilder.append('B');
/* 1488 */       else if (paramClass == Long.TYPE)
/* 1489 */         localStringBuilder.append('J');
/* 1490 */       else if (paramClass == Float.TYPE)
/* 1491 */         localStringBuilder.append('F');
/* 1492 */       else if (paramClass == Double.TYPE)
/* 1493 */         localStringBuilder.append('D');
/* 1494 */       else if (paramClass == Short.TYPE)
/* 1495 */         localStringBuilder.append('S');
/* 1496 */       else if (paramClass == Character.TYPE)
/* 1497 */         localStringBuilder.append('C');
/* 1498 */       else if (paramClass == Boolean.TYPE)
/* 1499 */         localStringBuilder.append('Z');
/* 1500 */       else if (paramClass == Void.TYPE)
/* 1501 */         localStringBuilder.append('V');
/*      */       else
/* 1503 */         throw new InternalError();
/*      */     }
/*      */     else {
/* 1506 */       localStringBuilder.append('L' + paramClass.getName().replace('.', '/') + ';');
/*      */     }
/* 1508 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private static String getMethodSignature(Class<?>[] paramArrayOfClass, Class<?> paramClass)
/*      */   {
/* 1517 */     StringBuilder localStringBuilder = new StringBuilder();
/* 1518 */     localStringBuilder.append('(');
/* 1519 */     for (int i = 0; i < paramArrayOfClass.length; i++) {
/* 1520 */       localStringBuilder.append(getClassSignature(paramArrayOfClass[i]));
/*      */     }
/* 1522 */     localStringBuilder.append(')');
/* 1523 */     localStringBuilder.append(getClassSignature(paramClass));
/* 1524 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private static void throwMiscException(Throwable paramThrowable)
/*      */     throws IOException
/*      */   {
/* 1533 */     if ((paramThrowable instanceof RuntimeException))
/* 1534 */       throw ((RuntimeException)paramThrowable);
/* 1535 */     if ((paramThrowable instanceof Error)) {
/* 1536 */       throw ((Error)paramThrowable);
/*      */     }
/* 1538 */     IOException localIOException = new IOException("unexpected exception type");
/* 1539 */     localIOException.initCause(paramThrowable);
/* 1540 */     throw localIOException;
/*      */   }
/*      */ 
/*      */   private static ObjectStreamField[] getSerialFields(Class<?> paramClass)
/*      */     throws InvalidClassException
/*      */   {
/*      */     ObjectStreamField[] arrayOfObjectStreamField;
/* 1555 */     if ((Serializable.class.isAssignableFrom(paramClass)) && (!Externalizable.class.isAssignableFrom(paramClass)) && (!Proxy.isProxyClass(paramClass)) && (!paramClass.isInterface()))
/*      */     {
/* 1560 */       if ((arrayOfObjectStreamField = getDeclaredSerialFields(paramClass)) == null) {
/* 1561 */         arrayOfObjectStreamField = getDefaultSerialFields(paramClass);
/*      */       }
/* 1563 */       Arrays.sort(arrayOfObjectStreamField);
/*      */     } else {
/* 1565 */       arrayOfObjectStreamField = NO_FIELDS;
/*      */     }
/* 1567 */     return arrayOfObjectStreamField;
/*      */   }
/*      */ 
/*      */   private static ObjectStreamField[] getDeclaredSerialFields(Class<?> paramClass)
/*      */     throws InvalidClassException
/*      */   {
/* 1584 */     ObjectStreamField[] arrayOfObjectStreamField1 = null;
/*      */     try {
/* 1586 */       Field localField1 = paramClass.getDeclaredField("serialPersistentFields");
/* 1587 */       int i = 26;
/* 1588 */       if ((localField1.getModifiers() & i) == i) {
/* 1589 */         localField1.setAccessible(true);
/* 1590 */         arrayOfObjectStreamField1 = (ObjectStreamField[])localField1.get(null);
/*      */       }
/*      */     } catch (Exception localException) {
/*      */     }
/* 1594 */     if (arrayOfObjectStreamField1 == null)
/* 1595 */       return null;
/* 1596 */     if (arrayOfObjectStreamField1.length == 0) {
/* 1597 */       return NO_FIELDS;
/*      */     }
/*      */ 
/* 1600 */     ObjectStreamField[] arrayOfObjectStreamField2 = new ObjectStreamField[arrayOfObjectStreamField1.length];
/*      */ 
/* 1602 */     HashSet localHashSet = new HashSet(arrayOfObjectStreamField1.length);
/*      */ 
/* 1604 */     for (int j = 0; j < arrayOfObjectStreamField1.length; j++) {
/* 1605 */       ObjectStreamField localObjectStreamField = arrayOfObjectStreamField1[j];
/*      */ 
/* 1607 */       String str = localObjectStreamField.getName();
/* 1608 */       if (localHashSet.contains(str)) {
/* 1609 */         throw new InvalidClassException("multiple serializable fields named " + str);
/*      */       }
/*      */ 
/* 1612 */       localHashSet.add(str);
/*      */       try
/*      */       {
/* 1615 */         Field localField2 = paramClass.getDeclaredField(str);
/* 1616 */         if ((localField2.getType() == localObjectStreamField.getType()) && ((localField2.getModifiers() & 0x8) == 0))
/*      */         {
/* 1619 */           arrayOfObjectStreamField2[j] = new ObjectStreamField(localField2, localObjectStreamField.isUnshared(), true);
/*      */         }
/*      */       }
/*      */       catch (NoSuchFieldException localNoSuchFieldException) {
/*      */       }
/* 1624 */       if (arrayOfObjectStreamField2[j] == null) {
/* 1625 */         arrayOfObjectStreamField2[j] = new ObjectStreamField(str, localObjectStreamField.getType(), localObjectStreamField.isUnshared());
/*      */       }
/*      */     }
/*      */ 
/* 1629 */     return arrayOfObjectStreamField2;
/*      */   }
/*      */ 
/*      */   private static ObjectStreamField[] getDefaultSerialFields(Class<?> paramClass)
/*      */   {
/* 1639 */     Field[] arrayOfField = paramClass.getDeclaredFields();
/* 1640 */     ArrayList localArrayList = new ArrayList();
/* 1641 */     int i = 136;
/*      */ 
/* 1643 */     for (int j = 0; j < arrayOfField.length; j++) {
/* 1644 */       if ((arrayOfField[j].getModifiers() & i) == 0) {
/* 1645 */         localArrayList.add(new ObjectStreamField(arrayOfField[j], false, true));
/*      */       }
/*      */     }
/* 1648 */     j = localArrayList.size();
/* 1649 */     return j == 0 ? NO_FIELDS : (ObjectStreamField[])localArrayList.toArray(new ObjectStreamField[j]);
/*      */   }
/*      */ 
/*      */   private static Long getDeclaredSUID(Class<?> paramClass)
/*      */   {
/*      */     try
/*      */     {
/* 1659 */       Field localField = paramClass.getDeclaredField("serialVersionUID");
/* 1660 */       int i = 24;
/* 1661 */       if ((localField.getModifiers() & i) == i) {
/* 1662 */         localField.setAccessible(true);
/* 1663 */         return Long.valueOf(localField.getLong(null));
/*      */       }
/*      */     } catch (Exception localException) {
/*      */     }
/* 1667 */     return null;
/*      */   }
/*      */ 
/*      */   private static long computeDefaultSUID(Class<?> paramClass)
/*      */   {
/* 1674 */     if ((!Serializable.class.isAssignableFrom(paramClass)) || (Proxy.isProxyClass(paramClass)))
/*      */     {
/* 1676 */       return 0L;
/*      */     }
/*      */     try
/*      */     {
/* 1680 */       ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/* 1681 */       DataOutputStream localDataOutputStream = new DataOutputStream(localByteArrayOutputStream);
/*      */ 
/* 1683 */       localDataOutputStream.writeUTF(paramClass.getName());
/*      */ 
/* 1685 */       int i = paramClass.getModifiers() & 0x611;
/*      */ 
/* 1693 */       Method[] arrayOfMethod = paramClass.getDeclaredMethods();
/* 1694 */       if ((i & 0x200) != 0) {
/* 1695 */         i = arrayOfMethod.length > 0 ? i | 0x400 : i & 0xFFFFFBFF;
/*      */       }
/*      */ 
/* 1699 */       localDataOutputStream.writeInt(i);
/*      */ 
/* 1701 */       if (!paramClass.isArray())
/*      */       {
/* 1707 */         localObject1 = paramClass.getInterfaces();
/* 1708 */         localObject2 = new String[localObject1.length];
/* 1709 */         for (j = 0; j < localObject1.length; j++) {
/* 1710 */           localObject2[j] = localObject1[j].getName();
/*      */         }
/* 1712 */         Arrays.sort((Object[])localObject2);
/* 1713 */         for (j = 0; j < localObject2.length; j++) {
/* 1714 */           localDataOutputStream.writeUTF(localObject2[j]);
/*      */         }
/*      */       }
/*      */ 
/* 1718 */       Object localObject1 = paramClass.getDeclaredFields();
/* 1719 */       Object localObject2 = new MemberSignature[localObject1.length];
/* 1720 */       for (int j = 0; j < localObject1.length; j++) {
/* 1721 */         localObject2[j] = new MemberSignature(localObject1[j]);
/*      */       }
/* 1723 */       Arrays.sort((Object[])localObject2, new Comparator() {
/*      */         public int compare(ObjectStreamClass.MemberSignature paramAnonymousMemberSignature1, ObjectStreamClass.MemberSignature paramAnonymousMemberSignature2) {
/* 1725 */           return paramAnonymousMemberSignature1.name.compareTo(paramAnonymousMemberSignature2.name);
/*      */         }
/*      */       });
/* 1728 */       for (j = 0; j < localObject2.length; j++) {
/* 1729 */         arrayOfMemberSignature1 = localObject2[j];
/* 1730 */         k = arrayOfMemberSignature1.member.getModifiers() & 0xDF;
/*      */ 
/* 1734 */         if (((k & 0x2) == 0) || ((k & 0x88) == 0))
/*      */         {
/* 1737 */           localDataOutputStream.writeUTF(arrayOfMemberSignature1.name);
/* 1738 */           localDataOutputStream.writeInt(k);
/* 1739 */           localDataOutputStream.writeUTF(arrayOfMemberSignature1.signature);
/*      */         }
/*      */       }
/*      */ 
/* 1743 */       if (hasStaticInitializer(paramClass)) {
/* 1744 */         localDataOutputStream.writeUTF("<clinit>");
/* 1745 */         localDataOutputStream.writeInt(8);
/* 1746 */         localDataOutputStream.writeUTF("()V");
/*      */       }
/*      */ 
/* 1749 */       Constructor[] arrayOfConstructor = paramClass.getDeclaredConstructors();
/* 1750 */       MemberSignature[] arrayOfMemberSignature1 = new MemberSignature[arrayOfConstructor.length];
/* 1751 */       for (int k = 0; k < arrayOfConstructor.length; k++) {
/* 1752 */         arrayOfMemberSignature1[k] = new MemberSignature(arrayOfConstructor[k]);
/*      */       }
/* 1754 */       Arrays.sort(arrayOfMemberSignature1, new Comparator() {
/*      */         public int compare(ObjectStreamClass.MemberSignature paramAnonymousMemberSignature1, ObjectStreamClass.MemberSignature paramAnonymousMemberSignature2) {
/* 1756 */           return paramAnonymousMemberSignature1.signature.compareTo(paramAnonymousMemberSignature2.signature);
/*      */         }
/*      */       });
/* 1759 */       for (k = 0; k < arrayOfMemberSignature1.length; k++) {
/* 1760 */         MemberSignature localMemberSignature = arrayOfMemberSignature1[k];
/* 1761 */         int n = localMemberSignature.member.getModifiers() & 0xD3F;
/*      */ 
/* 1766 */         if ((n & 0x2) == 0) {
/* 1767 */           localDataOutputStream.writeUTF("<init>");
/* 1768 */           localDataOutputStream.writeInt(n);
/* 1769 */           localDataOutputStream.writeUTF(localMemberSignature.signature.replace('/', '.'));
/*      */         }
/*      */       }
/*      */ 
/* 1773 */       MemberSignature[] arrayOfMemberSignature2 = new MemberSignature[arrayOfMethod.length];
/* 1774 */       for (int m = 0; m < arrayOfMethod.length; m++) {
/* 1775 */         arrayOfMemberSignature2[m] = new MemberSignature(arrayOfMethod[m]);
/*      */       }
/* 1777 */       Arrays.sort(arrayOfMemberSignature2, new Comparator() {
/*      */         public int compare(ObjectStreamClass.MemberSignature paramAnonymousMemberSignature1, ObjectStreamClass.MemberSignature paramAnonymousMemberSignature2) {
/* 1779 */           int i = paramAnonymousMemberSignature1.name.compareTo(paramAnonymousMemberSignature2.name);
/* 1780 */           if (i == 0) {
/* 1781 */             i = paramAnonymousMemberSignature1.signature.compareTo(paramAnonymousMemberSignature2.signature);
/*      */           }
/* 1783 */           return i;
/*      */         }
/*      */       });
/* 1786 */       for (m = 0; m < arrayOfMemberSignature2.length; m++) {
/* 1787 */         localObject3 = arrayOfMemberSignature2[m];
/* 1788 */         int i1 = ((MemberSignature)localObject3).member.getModifiers() & 0xD3F;
/*      */ 
/* 1793 */         if ((i1 & 0x2) == 0) {
/* 1794 */           localDataOutputStream.writeUTF(((MemberSignature)localObject3).name);
/* 1795 */           localDataOutputStream.writeInt(i1);
/* 1796 */           localDataOutputStream.writeUTF(((MemberSignature)localObject3).signature.replace('/', '.'));
/*      */         }
/*      */       }
/*      */ 
/* 1800 */       localDataOutputStream.flush();
/*      */ 
/* 1802 */       MessageDigest localMessageDigest = MessageDigest.getInstance("SHA");
/* 1803 */       Object localObject3 = localMessageDigest.digest(localByteArrayOutputStream.toByteArray());
/* 1804 */       long l = 0L;
/* 1805 */       for (int i2 = Math.min(localObject3.length, 8) - 1; i2 >= 0; i2--) {
/* 1806 */         l = l << 8 | localObject3[i2] & 0xFF;
/*      */       }
/* 1808 */       return l;
/*      */     } catch (IOException localIOException) {
/* 1810 */       throw new InternalError();
/*      */     } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
/* 1812 */       throw new SecurityException(localNoSuchAlgorithmException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native boolean hasStaticInitializer(Class<?> paramClass);
/*      */ 
/*      */   private static FieldReflector getReflector(ObjectStreamField[] paramArrayOfObjectStreamField, ObjectStreamClass paramObjectStreamClass)
/*      */     throws InvalidClassException
/*      */   {
/* 2115 */     Class localClass = (paramObjectStreamClass != null) && (paramArrayOfObjectStreamField.length > 0) ? paramObjectStreamClass.cl : null;
/*      */ 
/* 2117 */     processQueue(Caches.reflectorsQueue, Caches.reflectors);
/* 2118 */     FieldReflectorKey localFieldReflectorKey = new FieldReflectorKey(localClass, paramArrayOfObjectStreamField, Caches.reflectorsQueue);
/*      */ 
/* 2120 */     Reference localReference = (Reference)Caches.reflectors.get(localFieldReflectorKey);
/* 2121 */     Object localObject1 = null;
/* 2122 */     if (localReference != null) {
/* 2123 */       localObject1 = localReference.get();
/*      */     }
/* 2125 */     Object localObject2 = null;
/* 2126 */     if (localObject1 == null) {
/* 2127 */       EntryFuture localEntryFuture = new EntryFuture(null);
/* 2128 */       SoftReference localSoftReference = new SoftReference(localEntryFuture);
/*      */       do {
/* 2130 */         if (localReference != null) {
/* 2131 */           Caches.reflectors.remove(localFieldReflectorKey, localReference);
/*      */         }
/* 2133 */         localReference = (Reference)Caches.reflectors.putIfAbsent(localFieldReflectorKey, localSoftReference);
/* 2134 */         if (localReference != null)
/* 2135 */           localObject1 = localReference.get();
/*      */       }
/* 2137 */       while ((localReference != null) && (localObject1 == null));
/* 2138 */       if (localObject1 == null) {
/* 2139 */         localObject2 = localEntryFuture;
/*      */       }
/*      */     }
/*      */ 
/* 2143 */     if ((localObject1 instanceof FieldReflector))
/* 2144 */       return (FieldReflector)localObject1;
/* 2145 */     if ((localObject1 instanceof EntryFuture)) {
/* 2146 */       localObject1 = ((EntryFuture)localObject1).get();
/* 2147 */     } else if (localObject1 == null) {
/*      */       try {
/* 2149 */         localObject1 = new FieldReflector(matchFields(paramArrayOfObjectStreamField, paramObjectStreamClass));
/*      */       } catch (Throwable localThrowable) {
/* 2151 */         localObject1 = localThrowable;
/*      */       }
/* 2153 */       localObject2.set(localObject1);
/* 2154 */       Caches.reflectors.put(localFieldReflectorKey, new SoftReference(localObject1));
/*      */     }
/*      */ 
/* 2157 */     if ((localObject1 instanceof FieldReflector))
/* 2158 */       return (FieldReflector)localObject1;
/* 2159 */     if ((localObject1 instanceof InvalidClassException))
/* 2160 */       throw ((InvalidClassException)localObject1);
/* 2161 */     if ((localObject1 instanceof RuntimeException))
/* 2162 */       throw ((RuntimeException)localObject1);
/* 2163 */     if ((localObject1 instanceof Error)) {
/* 2164 */       throw ((Error)localObject1);
/*      */     }
/* 2166 */     throw new InternalError("unexpected entry: " + localObject1);
/*      */   }
/*      */ 
/*      */   private static ObjectStreamField[] matchFields(ObjectStreamField[] paramArrayOfObjectStreamField, ObjectStreamClass paramObjectStreamClass)
/*      */     throws InvalidClassException
/*      */   {
/* 2231 */     ObjectStreamField[] arrayOfObjectStreamField1 = paramObjectStreamClass != null ? paramObjectStreamClass.fields : NO_FIELDS;
/*      */ 
/* 2245 */     ObjectStreamField[] arrayOfObjectStreamField2 = new ObjectStreamField[paramArrayOfObjectStreamField.length];
/* 2246 */     for (int i = 0; i < paramArrayOfObjectStreamField.length; i++) {
/* 2247 */       ObjectStreamField localObjectStreamField1 = paramArrayOfObjectStreamField[i]; ObjectStreamField localObjectStreamField2 = null;
/* 2248 */       for (int j = 0; j < arrayOfObjectStreamField1.length; j++) {
/* 2249 */         ObjectStreamField localObjectStreamField3 = arrayOfObjectStreamField1[j];
/* 2250 */         if (localObjectStreamField1.getName().equals(localObjectStreamField3.getName())) {
/* 2251 */           if (((localObjectStreamField1.isPrimitive()) || (localObjectStreamField3.isPrimitive())) && (localObjectStreamField1.getTypeCode() != localObjectStreamField3.getTypeCode()))
/*      */           {
/* 2254 */             throw new InvalidClassException(paramObjectStreamClass.name, "incompatible types for field " + localObjectStreamField1.getName());
/*      */           }
/*      */ 
/* 2257 */           if (localObjectStreamField3.getField() != null) {
/* 2258 */             localObjectStreamField2 = new ObjectStreamField(localObjectStreamField3.getField(), localObjectStreamField3.isUnshared(), false);
/*      */           }
/*      */           else {
/* 2261 */             localObjectStreamField2 = new ObjectStreamField(localObjectStreamField3.getName(), localObjectStreamField3.getSignature(), localObjectStreamField3.isUnshared());
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/* 2266 */       if (localObjectStreamField2 == null) {
/* 2267 */         localObjectStreamField2 = new ObjectStreamField(localObjectStreamField1.getName(), localObjectStreamField1.getSignature(), false);
/*      */       }
/*      */ 
/* 2270 */       localObjectStreamField2.setOffset(localObjectStreamField1.getOffset());
/* 2271 */       arrayOfObjectStreamField2[i] = localObjectStreamField2;
/*      */     }
/* 2273 */     return arrayOfObjectStreamField2;
/*      */   }
/*      */ 
/*      */   static void processQueue(ReferenceQueue<Class<?>> paramReferenceQueue, ConcurrentMap<? extends WeakReference<Class<?>>, ?> paramConcurrentMap)
/*      */   {
/*      */     Reference localReference;
/* 2285 */     while ((localReference = paramReferenceQueue.poll()) != null)
/* 2286 */       paramConcurrentMap.remove(localReference);
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  197 */     initNative();
/*      */   }
/*      */ 
/*      */   private static class Caches
/*      */   {
/*   89 */     static final ConcurrentMap<ObjectStreamClass.WeakClassKey, Reference<?>> localDescs = new ConcurrentHashMap();
/*      */ 
/*   93 */     static final ConcurrentMap<ObjectStreamClass.FieldReflectorKey, Reference<?>> reflectors = new ConcurrentHashMap();
/*      */ 
/*   97 */     private static final ReferenceQueue<Class<?>> localDescsQueue = new ReferenceQueue();
/*      */ 
/*  100 */     private static final ReferenceQueue<Class<?>> reflectorsQueue = new ReferenceQueue();
/*      */   }
/*      */ 
/*      */   static class ClassDataSlot
/*      */   {
/*      */     final ObjectStreamClass desc;
/*      */     final boolean hasData;
/*      */ 
/*      */     ClassDataSlot(ObjectStreamClass paramObjectStreamClass, boolean paramBoolean)
/*      */     {
/* 1136 */       this.desc = paramObjectStreamClass;
/* 1137 */       this.hasData = paramBoolean;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class EntryFuture
/*      */   {
/*  398 */     private static final Object unset = new Object();
/*  399 */     private final Thread owner = Thread.currentThread();
/*  400 */     private Object entry = unset;
/*      */ 
/*      */     synchronized boolean set(Object paramObject)
/*      */     {
/*  410 */       if (this.entry != unset) {
/*  411 */         return false;
/*      */       }
/*  413 */       this.entry = paramObject;
/*  414 */       notifyAll();
/*  415 */       return true;
/*      */     }
/*      */ 
/*      */     synchronized Object get()
/*      */     {
/*  423 */       int i = 0;
/*  424 */       while (this.entry == unset) {
/*      */         try {
/*  426 */           wait();
/*      */         } catch (InterruptedException localInterruptedException) {
/*  428 */           i = 1;
/*      */         }
/*      */       }
/*  431 */       if (i != 0) {
/*  432 */         AccessController.doPrivileged(new PrivilegedAction()
/*      */         {
/*      */           public Void run() {
/*  435 */             Thread.currentThread().interrupt();
/*  436 */             return null;
/*      */           }
/*      */         });
/*      */       }
/*      */ 
/*  441 */       return this.entry;
/*      */     }
/*      */ 
/*      */     Thread getOwner()
/*      */     {
/*  448 */       return this.owner;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ExceptionInfo
/*      */   {
/*      */     private final String className;
/*      */     private final String message;
/*      */ 
/*      */     ExceptionInfo(String paramString1, String paramString2)
/*      */     {
/*  140 */       this.className = paramString1;
/*  141 */       this.message = paramString2;
/*      */     }
/*      */ 
/*      */     InvalidClassException newInvalidClassException()
/*      */     {
/*  150 */       return new InvalidClassException(this.className, this.message);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class FieldReflector
/*      */   {
/* 1860 */     private static final Unsafe unsafe = Unsafe.getUnsafe();
/*      */     private final ObjectStreamField[] fields;
/*      */     private final int numPrimFields;
/*      */     private final long[] readKeys;
/*      */     private final long[] writeKeys;
/*      */     private final int[] offsets;
/*      */     private final char[] typeCodes;
/*      */     private final Class<?>[] types;
/*      */ 
/*      */     FieldReflector(ObjectStreamField[] paramArrayOfObjectStreamField)
/*      */     {
/* 1885 */       this.fields = paramArrayOfObjectStreamField;
/* 1886 */       int i = paramArrayOfObjectStreamField.length;
/* 1887 */       this.readKeys = new long[i];
/* 1888 */       this.writeKeys = new long[i];
/* 1889 */       this.offsets = new int[i];
/* 1890 */       this.typeCodes = new char[i];
/* 1891 */       ArrayList localArrayList = new ArrayList();
/* 1892 */       HashSet localHashSet = new HashSet();
/*      */ 
/* 1895 */       for (int j = 0; j < i; j++) {
/* 1896 */         ObjectStreamField localObjectStreamField = paramArrayOfObjectStreamField[j];
/* 1897 */         Field localField = localObjectStreamField.getField();
/* 1898 */         long l = localField != null ? unsafe.objectFieldOffset(localField) : -1L;
/*      */ 
/* 1900 */         this.readKeys[j] = l;
/* 1901 */         this.writeKeys[j] = (localHashSet.add(Long.valueOf(l)) ? l : -1L);
/*      */ 
/* 1903 */         this.offsets[j] = localObjectStreamField.getOffset();
/* 1904 */         this.typeCodes[j] = localObjectStreamField.getTypeCode();
/* 1905 */         if (!localObjectStreamField.isPrimitive()) {
/* 1906 */           localArrayList.add(localField != null ? localField.getType() : null);
/*      */         }
/*      */       }
/*      */ 
/* 1910 */       this.types = ((Class[])localArrayList.toArray(new Class[localArrayList.size()]));
/* 1911 */       this.numPrimFields = (i - this.types.length);
/*      */     }
/*      */ 
/*      */     ObjectStreamField[] getFields()
/*      */     {
/* 1921 */       return this.fields;
/*      */     }
/*      */ 
/*      */     void getPrimFieldValues(Object paramObject, byte[] paramArrayOfByte)
/*      */     {
/* 1930 */       if (paramObject == null) {
/* 1931 */         throw new NullPointerException();
/*      */       }
/*      */ 
/* 1937 */       for (int i = 0; i < this.numPrimFields; i++) {
/* 1938 */         long l = this.readKeys[i];
/* 1939 */         int j = this.offsets[i];
/* 1940 */         switch (this.typeCodes[i]) {
/*      */         case 'Z':
/* 1942 */           Bits.putBoolean(paramArrayOfByte, j, unsafe.getBoolean(paramObject, l));
/* 1943 */           break;
/*      */         case 'B':
/* 1946 */           paramArrayOfByte[j] = unsafe.getByte(paramObject, l);
/* 1947 */           break;
/*      */         case 'C':
/* 1950 */           Bits.putChar(paramArrayOfByte, j, unsafe.getChar(paramObject, l));
/* 1951 */           break;
/*      */         case 'S':
/* 1954 */           Bits.putShort(paramArrayOfByte, j, unsafe.getShort(paramObject, l));
/* 1955 */           break;
/*      */         case 'I':
/* 1958 */           Bits.putInt(paramArrayOfByte, j, unsafe.getInt(paramObject, l));
/* 1959 */           break;
/*      */         case 'F':
/* 1962 */           Bits.putFloat(paramArrayOfByte, j, unsafe.getFloat(paramObject, l));
/* 1963 */           break;
/*      */         case 'J':
/* 1966 */           Bits.putLong(paramArrayOfByte, j, unsafe.getLong(paramObject, l));
/* 1967 */           break;
/*      */         case 'D':
/* 1970 */           Bits.putDouble(paramArrayOfByte, j, unsafe.getDouble(paramObject, l));
/* 1971 */           break;
/*      */         case 'E':
/*      */         case 'G':
/*      */         case 'H':
/*      */         case 'K':
/*      */         case 'L':
/*      */         case 'M':
/*      */         case 'N':
/*      */         case 'O':
/*      */         case 'P':
/*      */         case 'Q':
/*      */         case 'R':
/*      */         case 'T':
/*      */         case 'U':
/*      */         case 'V':
/*      */         case 'W':
/*      */         case 'X':
/*      */         case 'Y':
/*      */         default:
/* 1974 */           throw new InternalError();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     void setPrimFieldValues(Object paramObject, byte[] paramArrayOfByte)
/*      */     {
/* 1985 */       if (paramObject == null) {
/* 1986 */         throw new NullPointerException();
/*      */       }
/* 1988 */       for (int i = 0; i < this.numPrimFields; i++) {
/* 1989 */         long l = this.writeKeys[i];
/* 1990 */         if (l != -1L)
/*      */         {
/* 1993 */           int j = this.offsets[i];
/* 1994 */           switch (this.typeCodes[i]) {
/*      */           case 'Z':
/* 1996 */             unsafe.putBoolean(paramObject, l, Bits.getBoolean(paramArrayOfByte, j));
/* 1997 */             break;
/*      */           case 'B':
/* 2000 */             unsafe.putByte(paramObject, l, paramArrayOfByte[j]);
/* 2001 */             break;
/*      */           case 'C':
/* 2004 */             unsafe.putChar(paramObject, l, Bits.getChar(paramArrayOfByte, j));
/* 2005 */             break;
/*      */           case 'S':
/* 2008 */             unsafe.putShort(paramObject, l, Bits.getShort(paramArrayOfByte, j));
/* 2009 */             break;
/*      */           case 'I':
/* 2012 */             unsafe.putInt(paramObject, l, Bits.getInt(paramArrayOfByte, j));
/* 2013 */             break;
/*      */           case 'F':
/* 2016 */             unsafe.putFloat(paramObject, l, Bits.getFloat(paramArrayOfByte, j));
/* 2017 */             break;
/*      */           case 'J':
/* 2020 */             unsafe.putLong(paramObject, l, Bits.getLong(paramArrayOfByte, j));
/* 2021 */             break;
/*      */           case 'D':
/* 2024 */             unsafe.putDouble(paramObject, l, Bits.getDouble(paramArrayOfByte, j));
/* 2025 */             break;
/*      */           case 'E':
/*      */           case 'G':
/*      */           case 'H':
/*      */           case 'K':
/*      */           case 'L':
/*      */           case 'M':
/*      */           case 'N':
/*      */           case 'O':
/*      */           case 'P':
/*      */           case 'Q':
/*      */           case 'R':
/*      */           case 'T':
/*      */           case 'U':
/*      */           case 'V':
/*      */           case 'W':
/*      */           case 'X':
/*      */           case 'Y':
/*      */           default:
/* 2028 */             throw new InternalError();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     void getObjFieldValues(Object paramObject, Object[] paramArrayOfObject)
/*      */     {
/* 2039 */       if (paramObject == null) {
/* 2040 */         throw new NullPointerException();
/*      */       }
/*      */ 
/* 2046 */       for (int i = this.numPrimFields; i < this.fields.length; i++)
/* 2047 */         switch (this.typeCodes[i]) {
/*      */         case 'L':
/*      */         case '[':
/* 2050 */           paramArrayOfObject[this.offsets[i]] = unsafe.getObject(paramObject, this.readKeys[i]);
/* 2051 */           break;
/*      */         default:
/* 2054 */           throw new InternalError();
/*      */         }
/*      */     }
/*      */ 
/*      */     void setObjFieldValues(Object paramObject, Object[] paramArrayOfObject)
/*      */     {
/* 2067 */       if (paramObject == null) {
/* 2068 */         throw new NullPointerException();
/*      */       }
/* 2070 */       for (int i = this.numPrimFields; i < this.fields.length; i++) {
/* 2071 */         long l = this.writeKeys[i];
/* 2072 */         if (l != -1L)
/*      */         {
/* 2075 */           switch (this.typeCodes[i]) {
/*      */           case 'L':
/*      */           case '[':
/* 2078 */             Object localObject = paramArrayOfObject[this.offsets[i]];
/* 2079 */             if ((localObject != null) && (!this.types[(i - this.numPrimFields)].isInstance(localObject)))
/*      */             {
/* 2082 */               Field localField = this.fields[i].getField();
/* 2083 */               throw new ClassCastException("cannot assign instance of " + localObject.getClass().getName() + " to field " + localField.getDeclaringClass().getName() + "." + localField.getName() + " of type " + localField.getType().getName() + " in instance of " + paramObject.getClass().getName());
/*      */             }
/*      */ 
/* 2091 */             unsafe.putObject(paramObject, l, localObject);
/* 2092 */             break;
/*      */           default:
/* 2095 */             throw new InternalError();
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class FieldReflectorKey extends WeakReference<Class<?>>
/*      */   {
/*      */     private final String sigs;
/*      */     private final int hash;
/*      */     private final boolean nullClass;
/*      */ 
/*      */     FieldReflectorKey(Class<?> paramClass, ObjectStreamField[] paramArrayOfObjectStreamField, ReferenceQueue<Class<?>> paramReferenceQueue)
/*      */     {
/* 2183 */       super(paramReferenceQueue);
/* 2184 */       this.nullClass = (paramClass == null);
/* 2185 */       StringBuilder localStringBuilder = new StringBuilder();
/* 2186 */       for (int i = 0; i < paramArrayOfObjectStreamField.length; i++) {
/* 2187 */         ObjectStreamField localObjectStreamField = paramArrayOfObjectStreamField[i];
/* 2188 */         localStringBuilder.append(localObjectStreamField.getName()).append(localObjectStreamField.getSignature());
/*      */       }
/* 2190 */       this.sigs = localStringBuilder.toString();
/* 2191 */       this.hash = (System.identityHashCode(paramClass) + this.sigs.hashCode());
/*      */     }
/*      */ 
/*      */     public int hashCode() {
/* 2195 */       return this.hash;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject) {
/* 2199 */       if (paramObject == this) {
/* 2200 */         return true;
/*      */       }
/*      */ 
/* 2203 */       if ((paramObject instanceof FieldReflectorKey)) {
/* 2204 */         FieldReflectorKey localFieldReflectorKey = (FieldReflectorKey)paramObject;
/*      */         Class localClass;
/* 2206 */         return (this.nullClass ? localFieldReflectorKey.nullClass : ((localClass = (Class)get()) != null) && (localClass == localFieldReflectorKey.get())) && (this.sigs.equals(localFieldReflectorKey.sigs));
/*      */       }
/*      */ 
/* 2211 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class MemberSignature
/*      */   {
/*      */     public final Member member;
/*      */     public final String name;
/*      */     public final String signature;
/*      */ 
/*      */     public MemberSignature(Field paramField)
/*      */     {
/* 1833 */       this.member = paramField;
/* 1834 */       this.name = paramField.getName();
/* 1835 */       this.signature = ObjectStreamClass.getClassSignature(paramField.getType());
/*      */     }
/*      */ 
/*      */     public MemberSignature(Constructor paramConstructor) {
/* 1839 */       this.member = paramConstructor;
/* 1840 */       this.name = paramConstructor.getName();
/* 1841 */       this.signature = ObjectStreamClass.getMethodSignature(paramConstructor.getParameterTypes(), Void.TYPE);
/*      */     }
/*      */ 
/*      */     public MemberSignature(Method paramMethod)
/*      */     {
/* 1846 */       this.member = paramMethod;
/* 1847 */       this.name = paramMethod.getName();
/* 1848 */       this.signature = ObjectStreamClass.getMethodSignature(paramMethod.getParameterTypes(), paramMethod.getReturnType());
/*      */     }
/*      */   }
/*      */ 
/*      */   static class WeakClassKey extends WeakReference<Class<?>>
/*      */   {
/*      */     private final int hash;
/*      */ 
/*      */     WeakClassKey(Class<?> paramClass, ReferenceQueue<Class<?>> paramReferenceQueue)
/*      */     {
/* 2306 */       super(paramReferenceQueue);
/* 2307 */       this.hash = System.identityHashCode(paramClass);
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 2314 */       return this.hash;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/* 2324 */       if (paramObject == this) {
/* 2325 */         return true;
/*      */       }
/*      */ 
/* 2328 */       if ((paramObject instanceof WeakClassKey)) {
/* 2329 */         Object localObject = get();
/* 2330 */         return (localObject != null) && (localObject == ((WeakClassKey)paramObject).get());
/*      */       }
/*      */ 
/* 2333 */       return false;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.ObjectStreamClass
 * JD-Core Version:    0.6.2
 */