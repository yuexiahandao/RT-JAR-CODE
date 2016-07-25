/*      */ package java.io;
/*      */ 
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Proxy;
/*      */ import java.security.AccessControlContext;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.Arrays;
/*      */ import java.util.HashMap;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import sun.misc.VM;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ 
/*      */ public class ObjectInputStream extends InputStream
/*      */   implements ObjectInput, ObjectStreamConstants
/*      */ {
/*      */   private static final int NULL_HANDLE = -1;
/*  213 */   private static final Object unsharedMarker = new Object();
/*      */ 
/*  216 */   private static final HashMap<String, Class<?>> primClasses = new HashMap(8, 1.0F);
/*      */   private final BlockDataInputStream bin;
/*      */   private final ValidationList vlist;
/*      */   private int depth;
/*      */   private boolean closed;
/*      */   private final HandleTable handles;
/*  252 */   private int passHandle = -1;
/*      */ 
/*  254 */   private boolean defaultDataEnd = false;
/*      */   private byte[] primVals;
/*      */   private final boolean enableOverride;
/*      */   private boolean enableResolve;
/*      */   private SerialCallbackContext curContext;
/*      */ 
/*      */   public ObjectInputStream(InputStream paramInputStream)
/*      */     throws IOException
/*      */   {
/*  294 */     verifySubclass();
/*  295 */     this.bin = new BlockDataInputStream(paramInputStream);
/*  296 */     this.handles = new HandleTable(10);
/*  297 */     this.vlist = new ValidationList();
/*  298 */     this.enableOverride = false;
/*  299 */     readStreamHeader();
/*  300 */     this.bin.setBlockDataMode(true);
/*      */   }
/*      */ 
/*      */   protected ObjectInputStream()
/*      */     throws IOException, SecurityException
/*      */   {
/*  320 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  321 */     if (localSecurityManager != null) {
/*  322 */       localSecurityManager.checkPermission(SUBCLASS_IMPLEMENTATION_PERMISSION);
/*      */     }
/*  324 */     this.bin = null;
/*  325 */     this.handles = null;
/*  326 */     this.vlist = null;
/*  327 */     this.enableOverride = true;
/*      */   }
/*      */ 
/*      */   public final Object readObject()
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  363 */     if (this.enableOverride) {
/*  364 */       return readObjectOverride();
/*      */     }
/*      */ 
/*  368 */     int i = this.passHandle;
/*      */     try {
/*  370 */       Object localObject1 = readObject0(false);
/*  371 */       this.handles.markDependency(i, this.passHandle);
/*  372 */       ClassNotFoundException localClassNotFoundException = this.handles.lookupException(this.passHandle);
/*  373 */       if (localClassNotFoundException != null) {
/*  374 */         throw localClassNotFoundException;
/*      */       }
/*  376 */       if (this.depth == 0) {
/*  377 */         this.vlist.doCallbacks();
/*      */       }
/*  379 */       return localObject1;
/*      */     } finally {
/*  381 */       this.passHandle = i;
/*  382 */       if ((this.closed) && (this.depth == 0))
/*  383 */         clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Object readObjectOverride()
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  408 */     return null;
/*      */   }
/*      */ 
/*      */   public Object readUnshared()
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  458 */     int i = this.passHandle;
/*      */     try {
/*  460 */       Object localObject1 = readObject0(true);
/*  461 */       this.handles.markDependency(i, this.passHandle);
/*  462 */       ClassNotFoundException localClassNotFoundException = this.handles.lookupException(this.passHandle);
/*  463 */       if (localClassNotFoundException != null) {
/*  464 */         throw localClassNotFoundException;
/*      */       }
/*  466 */       if (this.depth == 0) {
/*  467 */         this.vlist.doCallbacks();
/*      */       }
/*  469 */       return localObject1;
/*      */     } finally {
/*  471 */       this.passHandle = i;
/*  472 */       if ((this.closed) && (this.depth == 0))
/*  473 */         clear();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void defaultReadObject()
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  493 */     SerialCallbackContext localSerialCallbackContext = this.curContext;
/*  494 */     if (localSerialCallbackContext == null) {
/*  495 */       throw new NotActiveException("not in call to readObject");
/*      */     }
/*  497 */     Object localObject = localSerialCallbackContext.getObj();
/*  498 */     ObjectStreamClass localObjectStreamClass = localSerialCallbackContext.getDesc();
/*  499 */     this.bin.setBlockDataMode(false);
/*  500 */     defaultReadFields(localObject, localObjectStreamClass);
/*  501 */     this.bin.setBlockDataMode(true);
/*  502 */     if (!localObjectStreamClass.hasWriteObjectData())
/*      */     {
/*  508 */       this.defaultDataEnd = true;
/*      */     }
/*  510 */     ClassNotFoundException localClassNotFoundException = this.handles.lookupException(this.passHandle);
/*  511 */     if (localClassNotFoundException != null)
/*  512 */       throw localClassNotFoundException;
/*      */   }
/*      */ 
/*      */   public GetField readFields()
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  532 */     SerialCallbackContext localSerialCallbackContext = this.curContext;
/*  533 */     if (localSerialCallbackContext == null) {
/*  534 */       throw new NotActiveException("not in call to readObject");
/*      */     }
/*  536 */     Object localObject = localSerialCallbackContext.getObj();
/*  537 */     ObjectStreamClass localObjectStreamClass = localSerialCallbackContext.getDesc();
/*  538 */     this.bin.setBlockDataMode(false);
/*  539 */     GetFieldImpl localGetFieldImpl = new GetFieldImpl(localObjectStreamClass);
/*  540 */     localGetFieldImpl.readFields();
/*  541 */     this.bin.setBlockDataMode(true);
/*  542 */     if (!localObjectStreamClass.hasWriteObjectData())
/*      */     {
/*  548 */       this.defaultDataEnd = true;
/*      */     }
/*      */ 
/*  551 */     return localGetFieldImpl;
/*      */   }
/*      */ 
/*      */   public void registerValidation(ObjectInputValidation paramObjectInputValidation, int paramInt)
/*      */     throws NotActiveException, InvalidObjectException
/*      */   {
/*  573 */     if (this.depth == 0) {
/*  574 */       throw new NotActiveException("stream inactive");
/*      */     }
/*  576 */     this.vlist.register(paramObjectInputValidation, paramInt);
/*      */   }
/*      */ 
/*      */   protected Class<?> resolveClass(ObjectStreamClass paramObjectStreamClass)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  623 */     String str = paramObjectStreamClass.getName();
/*      */     try {
/*  625 */       return Class.forName(str, false, latestUserDefinedLoader());
/*      */     } catch (ClassNotFoundException localClassNotFoundException) {
/*  627 */       Class localClass = (Class)primClasses.get(str);
/*  628 */       if (localClass != null) {
/*  629 */         return localClass;
/*      */       }
/*  631 */       throw localClassNotFoundException;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Class<?> resolveProxyClass(String[] paramArrayOfString)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  690 */     ClassLoader localClassLoader1 = latestUserDefinedLoader();
/*  691 */     ClassLoader localClassLoader2 = null;
/*  692 */     int i = 0;
/*      */ 
/*  695 */     Class[] arrayOfClass = new Class[paramArrayOfString.length];
/*  696 */     for (int j = 0; j < paramArrayOfString.length; j++) {
/*  697 */       Class localClass = Class.forName(paramArrayOfString[j], false, localClassLoader1);
/*  698 */       if ((localClass.getModifiers() & 0x1) == 0) {
/*  699 */         if (i != 0) {
/*  700 */           if (localClassLoader2 != localClass.getClassLoader())
/*  701 */             throw new IllegalAccessError("conflicting non-public interface class loaders");
/*      */         }
/*      */         else
/*      */         {
/*  705 */           localClassLoader2 = localClass.getClassLoader();
/*  706 */           i = 1;
/*      */         }
/*      */       }
/*  709 */       arrayOfClass[j] = localClass;
/*      */     }
/*      */     try {
/*  712 */       return Proxy.getProxyClass(i != 0 ? localClassLoader2 : localClassLoader1, arrayOfClass);
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException)
/*      */     {
/*  716 */       throw new ClassNotFoundException(null, localIllegalArgumentException);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected Object resolveObject(Object paramObject)
/*      */     throws IOException
/*      */   {
/*  748 */     return paramObject;
/*      */   }
/*      */ 
/*      */   protected boolean enableResolveObject(boolean paramBoolean)
/*      */     throws SecurityException
/*      */   {
/*  775 */     if (paramBoolean == this.enableResolve) {
/*  776 */       return paramBoolean;
/*      */     }
/*  778 */     if (paramBoolean) {
/*  779 */       SecurityManager localSecurityManager = System.getSecurityManager();
/*  780 */       if (localSecurityManager != null) {
/*  781 */         localSecurityManager.checkPermission(SUBSTITUTION_PERMISSION);
/*      */       }
/*      */     }
/*  784 */     this.enableResolve = paramBoolean;
/*  785 */     return !this.enableResolve;
/*      */   }
/*      */ 
/*      */   protected void readStreamHeader()
/*      */     throws IOException, StreamCorruptedException
/*      */   {
/*  801 */     short s1 = this.bin.readShort();
/*  802 */     short s2 = this.bin.readShort();
/*  803 */     if ((s1 != -21267) || (s2 != 5))
/*  804 */       throw new StreamCorruptedException(String.format("invalid stream header: %04X%04X", new Object[] { Short.valueOf(s1), Short.valueOf(s2) }));
/*      */   }
/*      */ 
/*      */   protected ObjectStreamClass readClassDescriptor()
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  829 */     ObjectStreamClass localObjectStreamClass = new ObjectStreamClass();
/*  830 */     localObjectStreamClass.readNonProxy(this);
/*  831 */     return localObjectStreamClass;
/*      */   }
/*      */ 
/*      */   public int read()
/*      */     throws IOException
/*      */   {
/*  841 */     return this.bin.read();
/*      */   }
/*      */ 
/*      */   public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/*  858 */     if (paramArrayOfByte == null) {
/*  859 */       throw new NullPointerException();
/*      */     }
/*  861 */     int i = paramInt1 + paramInt2;
/*  862 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (i > paramArrayOfByte.length) || (i < 0)) {
/*  863 */       throw new IndexOutOfBoundsException();
/*      */     }
/*  865 */     return this.bin.read(paramArrayOfByte, paramInt1, paramInt2, false);
/*      */   }
/*      */ 
/*      */   public int available()
/*      */     throws IOException
/*      */   {
/*  876 */     return this.bin.available();
/*      */   }
/*      */ 
/*      */   public void close()
/*      */     throws IOException
/*      */   {
/*  890 */     this.closed = true;
/*  891 */     if (this.depth == 0) {
/*  892 */       clear();
/*      */     }
/*  894 */     this.bin.close();
/*      */   }
/*      */ 
/*      */   public boolean readBoolean()
/*      */     throws IOException
/*      */   {
/*  905 */     return this.bin.readBoolean();
/*      */   }
/*      */ 
/*      */   public byte readByte()
/*      */     throws IOException
/*      */   {
/*  916 */     return this.bin.readByte();
/*      */   }
/*      */ 
/*      */   public int readUnsignedByte()
/*      */     throws IOException
/*      */   {
/*  927 */     return this.bin.readUnsignedByte();
/*      */   }
/*      */ 
/*      */   public char readChar()
/*      */     throws IOException
/*      */   {
/*  938 */     return this.bin.readChar();
/*      */   }
/*      */ 
/*      */   public short readShort()
/*      */     throws IOException
/*      */   {
/*  949 */     return this.bin.readShort();
/*      */   }
/*      */ 
/*      */   public int readUnsignedShort()
/*      */     throws IOException
/*      */   {
/*  960 */     return this.bin.readUnsignedShort();
/*      */   }
/*      */ 
/*      */   public int readInt()
/*      */     throws IOException
/*      */   {
/*  971 */     return this.bin.readInt();
/*      */   }
/*      */ 
/*      */   public long readLong()
/*      */     throws IOException
/*      */   {
/*  982 */     return this.bin.readLong();
/*      */   }
/*      */ 
/*      */   public float readFloat()
/*      */     throws IOException
/*      */   {
/*  993 */     return this.bin.readFloat();
/*      */   }
/*      */ 
/*      */   public double readDouble()
/*      */     throws IOException
/*      */   {
/* 1004 */     return this.bin.readDouble();
/*      */   }
/*      */ 
/*      */   public void readFully(byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/* 1015 */     this.bin.readFully(paramArrayOfByte, 0, paramArrayOfByte.length, false);
/*      */   }
/*      */ 
/*      */   public void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/* 1028 */     int i = paramInt1 + paramInt2;
/* 1029 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (i > paramArrayOfByte.length) || (i < 0)) {
/* 1030 */       throw new IndexOutOfBoundsException();
/*      */     }
/* 1032 */     this.bin.readFully(paramArrayOfByte, paramInt1, paramInt2, false);
/*      */   }
/*      */ 
/*      */   public int skipBytes(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1043 */     return this.bin.skipBytes(paramInt);
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public String readLine()
/*      */     throws IOException
/*      */   {
/* 1057 */     return this.bin.readLine();
/*      */   }
/*      */ 
/*      */   public String readUTF()
/*      */     throws IOException
/*      */   {
/* 1072 */     return this.bin.readUTF();
/*      */   }
/*      */ 
/*      */   private void verifySubclass()
/*      */   {
/* 1235 */     Class localClass = getClass();
/* 1236 */     if (localClass == ObjectInputStream.class) {
/* 1237 */       return;
/*      */     }
/* 1239 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1240 */     if (localSecurityManager == null) {
/* 1241 */       return;
/*      */     }
/* 1243 */     ObjectStreamClass.processQueue(Caches.subclassAuditsQueue, Caches.subclassAudits);
/* 1244 */     ObjectStreamClass.WeakClassKey localWeakClassKey = new ObjectStreamClass.WeakClassKey(localClass, Caches.subclassAuditsQueue);
/* 1245 */     Boolean localBoolean = (Boolean)Caches.subclassAudits.get(localWeakClassKey);
/* 1246 */     if (localBoolean == null) {
/* 1247 */       localBoolean = Boolean.valueOf(auditSubclass(localClass));
/* 1248 */       Caches.subclassAudits.putIfAbsent(localWeakClassKey, localBoolean);
/*      */     }
/* 1250 */     if (localBoolean.booleanValue()) {
/* 1251 */       return;
/*      */     }
/* 1253 */     localSecurityManager.checkPermission(SUBCLASS_IMPLEMENTATION_PERMISSION);
/*      */   }
/*      */ 
/*      */   private static boolean auditSubclass(Class<?> paramClass)
/*      */   {
/* 1262 */     Boolean localBoolean = (Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Boolean run() {
/* 1265 */         for (Class localClass = this.val$subcl; 
/* 1266 */           localClass != ObjectInputStream.class; 
/* 1267 */           localClass = localClass.getSuperclass())
/*      */           try
/*      */           {
/* 1270 */             localClass.getDeclaredMethod("readUnshared", (Class[])null);
/*      */ 
/* 1272 */             return Boolean.FALSE;
/*      */           }
/*      */           catch (NoSuchMethodException localNoSuchMethodException1) {
/*      */             try {
/* 1276 */               localClass.getDeclaredMethod("readFields", (Class[])null);
/* 1277 */               return Boolean.FALSE;
/*      */             } catch (NoSuchMethodException localNoSuchMethodException2) {
/*      */             }
/*      */           }
/* 1281 */         return Boolean.TRUE;
/*      */       }
/*      */     });
/* 1285 */     return localBoolean.booleanValue();
/*      */   }
/*      */ 
/*      */   private void clear()
/*      */   {
/* 1292 */     this.handles.clear();
/* 1293 */     this.vlist.clear();
/*      */   }
/*      */ 
/*      */   private Object readObject0(boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1300 */     boolean bool = this.bin.getBlockDataMode();
/*      */     int i;
/* 1301 */     if (bool) {
/* 1302 */       i = this.bin.currentBlockRemaining();
/* 1303 */       if (i > 0)
/* 1304 */         throw new OptionalDataException(i);
/* 1305 */       if (this.defaultDataEnd)
/*      */       {
/* 1312 */         throw new OptionalDataException(true);
/*      */       }
/* 1314 */       this.bin.setBlockDataMode(false);
/*      */     }
/*      */ 
/* 1318 */     while ((i = this.bin.peekByte()) == 121) {
/* 1319 */       this.bin.readByte();
/* 1320 */       handleReset();
/*      */     }
/*      */ 
/* 1323 */     this.depth += 1;
/*      */     try
/*      */     {
/*      */       Object localObject1;
/* 1325 */       switch (i) {
/*      */       case 112:
/* 1327 */         return readNull();
/*      */       case 113:
/* 1330 */         return readHandle(paramBoolean);
/*      */       case 118:
/* 1333 */         return readClass(paramBoolean);
/*      */       case 114:
/*      */       case 125:
/* 1337 */         return readClassDesc(paramBoolean);
/*      */       case 116:
/*      */       case 124:
/* 1341 */         return checkResolve(readString(paramBoolean));
/*      */       case 117:
/* 1344 */         return checkResolve(readArray(paramBoolean));
/*      */       case 126:
/* 1347 */         return checkResolve(readEnum(paramBoolean));
/*      */       case 115:
/* 1350 */         return checkResolve(readOrdinaryObject(paramBoolean));
/*      */       case 123:
/* 1353 */         localObject1 = readFatalException();
/* 1354 */         throw new WriteAbortedException("writing aborted", (Exception)localObject1);
/*      */       case 119:
/*      */       case 122:
/* 1358 */         if (bool) {
/* 1359 */           this.bin.setBlockDataMode(true);
/* 1360 */           this.bin.peek();
/* 1361 */           throw new OptionalDataException(this.bin.currentBlockRemaining());
/*      */         }
/*      */ 
/* 1364 */         throw new StreamCorruptedException("unexpected block data");
/*      */       case 120:
/* 1369 */         if (bool) {
/* 1370 */           throw new OptionalDataException(true);
/*      */         }
/* 1372 */         throw new StreamCorruptedException("unexpected end of block data");
/*      */       case 121:
/*      */       }
/*      */ 
/* 1377 */       throw new StreamCorruptedException(String.format("invalid type code: %02X", new Object[] { Byte.valueOf(i) }));
/*      */     }
/*      */     finally
/*      */     {
/* 1381 */       this.depth -= 1;
/* 1382 */       this.bin.setBlockDataMode(bool);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Object checkResolve(Object paramObject)
/*      */     throws IOException
/*      */   {
/* 1395 */     if ((!this.enableResolve) || (this.handles.lookupException(this.passHandle) != null)) {
/* 1396 */       return paramObject;
/*      */     }
/* 1398 */     Object localObject = resolveObject(paramObject);
/* 1399 */     if (localObject != paramObject) {
/* 1400 */       this.handles.setObject(this.passHandle, localObject);
/*      */     }
/* 1402 */     return localObject;
/*      */   }
/*      */ 
/*      */   String readTypeString()
/*      */     throws IOException
/*      */   {
/* 1410 */     int i = this.passHandle;
/*      */     try {
/* 1412 */       byte b = this.bin.peekByte();
/*      */       String str;
/* 1413 */       switch (b) {
/*      */       case 112:
/* 1415 */         return (String)readNull();
/*      */       case 113:
/* 1418 */         return (String)readHandle(false);
/*      */       case 116:
/*      */       case 124:
/* 1422 */         return readString(false);
/*      */       }
/*      */ 
/* 1425 */       throw new StreamCorruptedException(String.format("invalid type code: %02X", new Object[] { Byte.valueOf(b) }));
/*      */     }
/*      */     finally
/*      */     {
/* 1429 */       this.passHandle = i;
/*      */     }
/*      */   }
/*      */ 
/*      */   private Object readNull()
/*      */     throws IOException
/*      */   {
/* 1437 */     if (this.bin.readByte() != 112) {
/* 1438 */       throw new InternalError();
/*      */     }
/* 1440 */     this.passHandle = -1;
/* 1441 */     return null;
/*      */   }
/*      */ 
/*      */   private Object readHandle(boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1449 */     if (this.bin.readByte() != 113) {
/* 1450 */       throw new InternalError();
/*      */     }
/* 1452 */     this.passHandle = (this.bin.readInt() - 8257536);
/* 1453 */     if ((this.passHandle < 0) || (this.passHandle >= this.handles.size())) {
/* 1454 */       throw new StreamCorruptedException(String.format("invalid handle value: %08X", new Object[] { Integer.valueOf(this.passHandle + 8257536) }));
/*      */     }
/*      */ 
/* 1458 */     if (paramBoolean)
/*      */     {
/* 1460 */       throw new InvalidObjectException("cannot read back reference as unshared");
/*      */     }
/*      */ 
/* 1464 */     Object localObject = this.handles.lookupObject(this.passHandle);
/* 1465 */     if (localObject == unsharedMarker)
/*      */     {
/* 1467 */       throw new InvalidObjectException("cannot read back reference to unshared object");
/*      */     }
/*      */ 
/* 1470 */     return localObject;
/*      */   }
/*      */ 
/*      */   private Class readClass(boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1480 */     if (this.bin.readByte() != 118) {
/* 1481 */       throw new InternalError();
/*      */     }
/* 1483 */     ObjectStreamClass localObjectStreamClass = readClassDesc(false);
/* 1484 */     Class localClass = localObjectStreamClass.forClass();
/* 1485 */     this.passHandle = this.handles.assign(paramBoolean ? unsharedMarker : localClass);
/*      */ 
/* 1487 */     ClassNotFoundException localClassNotFoundException = localObjectStreamClass.getResolveException();
/* 1488 */     if (localClassNotFoundException != null) {
/* 1489 */       this.handles.markException(this.passHandle, localClassNotFoundException);
/*      */     }
/*      */ 
/* 1492 */     this.handles.finish(this.passHandle);
/* 1493 */     return localClass;
/*      */   }
/*      */ 
/*      */   private ObjectStreamClass readClassDesc(boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1505 */     byte b = this.bin.peekByte();
/* 1506 */     switch (b) {
/*      */     case 112:
/* 1508 */       return (ObjectStreamClass)readNull();
/*      */     case 113:
/* 1511 */       return (ObjectStreamClass)readHandle(paramBoolean);
/*      */     case 125:
/* 1514 */       return readProxyDesc(paramBoolean);
/*      */     case 114:
/* 1517 */       return readNonProxyDesc(paramBoolean);
/*      */     }
/*      */ 
/* 1520 */     throw new StreamCorruptedException(String.format("invalid type code: %02X", new Object[] { Byte.valueOf(b) }));
/*      */   }
/*      */ 
/*      */   private boolean isCustomSubclass()
/*      */   {
/* 1527 */     return getClass().getClassLoader() != ObjectInputStream.class.getClassLoader();
/*      */   }
/*      */ 
/*      */   private ObjectStreamClass readProxyDesc(boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1540 */     if (this.bin.readByte() != 125) {
/* 1541 */       throw new InternalError();
/*      */     }
/*      */ 
/* 1544 */     ObjectStreamClass localObjectStreamClass = new ObjectStreamClass();
/* 1545 */     int i = this.handles.assign(paramBoolean ? unsharedMarker : localObjectStreamClass);
/* 1546 */     this.passHandle = -1;
/*      */ 
/* 1548 */     int j = this.bin.readInt();
/* 1549 */     String[] arrayOfString = new String[j];
/* 1550 */     for (int k = 0; k < j; k++) {
/* 1551 */       arrayOfString[k] = this.bin.readUTF();
/*      */     }
/*      */ 
/* 1554 */     Class localClass = null;
/* 1555 */     Object localObject = null;
/* 1556 */     this.bin.setBlockDataMode(true);
/*      */     try {
/* 1558 */       if ((localClass = resolveProxyClass(arrayOfString)) == null) {
/* 1559 */         localObject = new ClassNotFoundException("null class"); } else {
/* 1560 */         if (!Proxy.isProxyClass(localClass)) {
/* 1561 */           throw new InvalidClassException("Not a proxy");
/*      */         }
/*      */ 
/* 1566 */         ReflectUtil.checkProxyPackageAccess(getClass().getClassLoader(), localClass.getInterfaces());
/*      */       }
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException)
/*      */     {
/* 1571 */       localObject = localClassNotFoundException;
/*      */     }
/* 1573 */     skipCustomData();
/*      */ 
/* 1575 */     localObjectStreamClass.initProxy(localClass, (ClassNotFoundException)localObject, readClassDesc(false));
/*      */ 
/* 1577 */     this.handles.finish(i);
/* 1578 */     this.passHandle = i;
/* 1579 */     return localObjectStreamClass;
/*      */   }
/*      */ 
/*      */   private ObjectStreamClass readNonProxyDesc(boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1591 */     if (this.bin.readByte() != 114) {
/* 1592 */       throw new InternalError();
/*      */     }
/*      */ 
/* 1595 */     ObjectStreamClass localObjectStreamClass1 = new ObjectStreamClass();
/* 1596 */     int i = this.handles.assign(paramBoolean ? unsharedMarker : localObjectStreamClass1);
/* 1597 */     this.passHandle = -1;
/*      */ 
/* 1599 */     ObjectStreamClass localObjectStreamClass2 = null;
/*      */     try {
/* 1601 */       localObjectStreamClass2 = readClassDescriptor();
/*      */     } catch (ClassNotFoundException localClassNotFoundException1) {
/* 1603 */       throw ((IOException)new InvalidClassException("failed to read class descriptor").initCause(localClassNotFoundException1));
/*      */     }
/*      */ 
/* 1607 */     Class localClass = null;
/* 1608 */     Object localObject = null;
/* 1609 */     this.bin.setBlockDataMode(true);
/* 1610 */     boolean bool = isCustomSubclass();
/*      */     try {
/* 1612 */       if ((localClass = resolveClass(localObjectStreamClass2)) == null)
/* 1613 */         localObject = new ClassNotFoundException("null class");
/* 1614 */       else if (bool)
/* 1615 */         ReflectUtil.checkPackageAccess(localClass);
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException2) {
/* 1618 */       localObject = localClassNotFoundException2;
/*      */     }
/* 1620 */     skipCustomData();
/*      */ 
/* 1622 */     localObjectStreamClass1.initNonProxy(localObjectStreamClass2, localClass, (ClassNotFoundException)localObject, readClassDesc(false));
/*      */ 
/* 1624 */     this.handles.finish(i);
/* 1625 */     this.passHandle = i;
/* 1626 */     return localObjectStreamClass1;
/*      */   }
/*      */ 
/*      */   private String readString(boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1635 */     byte b = this.bin.readByte();
/*      */     String str;
/* 1636 */     switch (b) {
/*      */     case 116:
/* 1638 */       str = this.bin.readUTF();
/* 1639 */       break;
/*      */     case 124:
/* 1642 */       str = this.bin.readLongUTF();
/* 1643 */       break;
/*      */     default:
/* 1646 */       throw new StreamCorruptedException(String.format("invalid type code: %02X", new Object[] { Byte.valueOf(b) }));
/*      */     }
/*      */ 
/* 1649 */     this.passHandle = this.handles.assign(paramBoolean ? unsharedMarker : str);
/* 1650 */     this.handles.finish(this.passHandle);
/* 1651 */     return str;
/*      */   }
/*      */ 
/*      */   private Object readArray(boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1659 */     if (this.bin.readByte() != 117) {
/* 1660 */       throw new InternalError();
/*      */     }
/*      */ 
/* 1663 */     ObjectStreamClass localObjectStreamClass = readClassDesc(false);
/* 1664 */     int i = this.bin.readInt();
/*      */ 
/* 1666 */     Object localObject = null;
/* 1667 */     Class localClass2 = null;
/*      */     Class localClass1;
/* 1668 */     if ((localClass1 = localObjectStreamClass.forClass()) != null) {
/* 1669 */       localClass2 = localClass1.getComponentType();
/* 1670 */       localObject = Array.newInstance(localClass2, i);
/*      */     }
/*      */ 
/* 1673 */     int j = this.handles.assign(paramBoolean ? unsharedMarker : localObject);
/* 1674 */     ClassNotFoundException localClassNotFoundException = localObjectStreamClass.getResolveException();
/* 1675 */     if (localClassNotFoundException != null) {
/* 1676 */       this.handles.markException(j, localClassNotFoundException);
/*      */     }
/*      */ 
/* 1679 */     if (localClass2 == null) {
/* 1680 */       for (int k = 0; k < i; k++)
/* 1681 */         readObject0(false);
/*      */     }
/* 1683 */     else if (localClass2.isPrimitive()) {
/* 1684 */       if (localClass2 == Integer.TYPE)
/* 1685 */         this.bin.readInts((int[])localObject, 0, i);
/* 1686 */       else if (localClass2 == Byte.TYPE)
/* 1687 */         this.bin.readFully((byte[])localObject, 0, i, true);
/* 1688 */       else if (localClass2 == Long.TYPE)
/* 1689 */         this.bin.readLongs((long[])localObject, 0, i);
/* 1690 */       else if (localClass2 == Float.TYPE)
/* 1691 */         this.bin.readFloats((float[])localObject, 0, i);
/* 1692 */       else if (localClass2 == Double.TYPE)
/* 1693 */         this.bin.readDoubles((double[])localObject, 0, i);
/* 1694 */       else if (localClass2 == Short.TYPE)
/* 1695 */         this.bin.readShorts((short[])localObject, 0, i);
/* 1696 */       else if (localClass2 == Character.TYPE)
/* 1697 */         this.bin.readChars((char[])localObject, 0, i);
/* 1698 */       else if (localClass2 == Boolean.TYPE)
/* 1699 */         this.bin.readBooleans((boolean[])localObject, 0, i);
/*      */       else
/* 1701 */         throw new InternalError();
/*      */     }
/*      */     else {
/* 1704 */       Object[] arrayOfObject = (Object[])localObject;
/* 1705 */       for (int m = 0; m < i; m++) {
/* 1706 */         arrayOfObject[m] = readObject0(false);
/* 1707 */         this.handles.markDependency(j, this.passHandle);
/*      */       }
/*      */     }
/*      */ 
/* 1711 */     this.handles.finish(j);
/* 1712 */     this.passHandle = j;
/* 1713 */     return localObject;
/*      */   }
/*      */ 
/*      */   private Enum readEnum(boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1721 */     if (this.bin.readByte() != 126) {
/* 1722 */       throw new InternalError();
/*      */     }
/*      */ 
/* 1725 */     ObjectStreamClass localObjectStreamClass = readClassDesc(false);
/* 1726 */     if (!localObjectStreamClass.isEnum()) {
/* 1727 */       throw new InvalidClassException("non-enum class: " + localObjectStreamClass);
/*      */     }
/*      */ 
/* 1730 */     int i = this.handles.assign(paramBoolean ? unsharedMarker : null);
/* 1731 */     ClassNotFoundException localClassNotFoundException = localObjectStreamClass.getResolveException();
/* 1732 */     if (localClassNotFoundException != null) {
/* 1733 */       this.handles.markException(i, localClassNotFoundException);
/*      */     }
/*      */ 
/* 1736 */     String str = readString(false);
/* 1737 */     Enum localEnum = null;
/* 1738 */     Class localClass = localObjectStreamClass.forClass();
/* 1739 */     if (localClass != null) {
/*      */       try {
/* 1741 */         localEnum = Enum.valueOf(localClass, str);
/*      */       } catch (IllegalArgumentException localIllegalArgumentException) {
/* 1743 */         throw ((IOException)new InvalidObjectException("enum constant " + str + " does not exist in " + localClass).initCause(localIllegalArgumentException));
/*      */       }
/*      */ 
/* 1747 */       if (!paramBoolean) {
/* 1748 */         this.handles.setObject(i, localEnum);
/*      */       }
/*      */     }
/*      */ 
/* 1752 */     this.handles.finish(i);
/* 1753 */     this.passHandle = i;
/* 1754 */     return localEnum;
/*      */   }
/*      */ 
/*      */   private Object readOrdinaryObject(boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1767 */     if (this.bin.readByte() != 115) {
/* 1768 */       throw new InternalError();
/*      */     }
/*      */ 
/* 1771 */     ObjectStreamClass localObjectStreamClass = readClassDesc(false);
/* 1772 */     localObjectStreamClass.checkDeserialize();
/*      */ 
/* 1774 */     Class localClass = localObjectStreamClass.forClass();
/* 1775 */     if ((localClass == String.class) || (localClass == Class.class) || (localClass == ObjectStreamClass.class))
/*      */     {
/* 1777 */       throw new InvalidClassException("invalid class descriptor");
/*      */     }
/*      */     Object localObject1;
/*      */     try
/*      */     {
/* 1782 */       localObject1 = localObjectStreamClass.isInstantiable() ? localObjectStreamClass.newInstance() : null;
/*      */     } catch (Exception localException) {
/* 1784 */       throw ((IOException)new InvalidClassException(localObjectStreamClass.forClass().getName(), "unable to create instance").initCause(localException));
/*      */     }
/*      */ 
/* 1789 */     this.passHandle = this.handles.assign(paramBoolean ? unsharedMarker : localObject1);
/* 1790 */     ClassNotFoundException localClassNotFoundException = localObjectStreamClass.getResolveException();
/* 1791 */     if (localClassNotFoundException != null) {
/* 1792 */       this.handles.markException(this.passHandle, localClassNotFoundException);
/*      */     }
/*      */ 
/* 1795 */     if (localObjectStreamClass.isExternalizable())
/* 1796 */       readExternalData((Externalizable)localObject1, localObjectStreamClass);
/*      */     else {
/* 1798 */       readSerialData(localObject1, localObjectStreamClass);
/*      */     }
/*      */ 
/* 1801 */     this.handles.finish(this.passHandle);
/*      */ 
/* 1803 */     if ((localObject1 != null) && (this.handles.lookupException(this.passHandle) == null) && (localObjectStreamClass.hasReadResolveMethod()))
/*      */     {
/* 1807 */       Object localObject2 = localObjectStreamClass.invokeReadResolve(localObject1);
/* 1808 */       if ((paramBoolean) && (localObject2.getClass().isArray())) {
/* 1809 */         localObject2 = cloneArray(localObject2);
/*      */       }
/* 1811 */       if (localObject2 != localObject1) {
/* 1812 */         this.handles.setObject(this.passHandle, localObject1 = localObject2);
/*      */       }
/*      */     }
/*      */ 
/* 1816 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private void readExternalData(Externalizable paramExternalizable, ObjectStreamClass paramObjectStreamClass)
/*      */     throws IOException
/*      */   {
/* 1828 */     SerialCallbackContext localSerialCallbackContext = this.curContext;
/* 1829 */     this.curContext = null;
/*      */     try {
/* 1831 */       boolean bool = paramObjectStreamClass.hasBlockExternalData();
/* 1832 */       if (bool) {
/* 1833 */         this.bin.setBlockDataMode(true);
/*      */       }
/* 1835 */       if (paramExternalizable != null) {
/*      */         try {
/* 1837 */           paramExternalizable.readExternal(this);
/*      */         }
/*      */         catch (ClassNotFoundException localClassNotFoundException)
/*      */         {
/* 1846 */           this.handles.markException(this.passHandle, localClassNotFoundException);
/*      */         }
/*      */       }
/* 1849 */       if (bool)
/* 1850 */         skipCustomData();
/*      */     }
/*      */     finally {
/* 1853 */       this.curContext = localSerialCallbackContext;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void readSerialData(Object paramObject, ObjectStreamClass paramObjectStreamClass)
/*      */     throws IOException
/*      */   {
/* 1878 */     ObjectStreamClass.ClassDataSlot[] arrayOfClassDataSlot = paramObjectStreamClass.getClassDataLayout();
/* 1879 */     for (int i = 0; i < arrayOfClassDataSlot.length; i++) {
/* 1880 */       ObjectStreamClass localObjectStreamClass = arrayOfClassDataSlot[i].desc;
/*      */ 
/* 1882 */       if (arrayOfClassDataSlot[i].hasData) {
/* 1883 */         if ((paramObject != null) && (localObjectStreamClass.hasReadObjectMethod()) && (this.handles.lookupException(this.passHandle) == null))
/*      */         {
/* 1887 */           SerialCallbackContext localSerialCallbackContext = this.curContext;
/*      */           try
/*      */           {
/* 1890 */             this.curContext = new SerialCallbackContext(paramObject, localObjectStreamClass);
/*      */ 
/* 1892 */             this.bin.setBlockDataMode(true);
/* 1893 */             localObjectStreamClass.invokeReadObject(paramObject, this);
/*      */           }
/*      */           catch (ClassNotFoundException localClassNotFoundException)
/*      */           {
/* 1902 */             this.handles.markException(this.passHandle, localClassNotFoundException);
/*      */           } finally {
/* 1904 */             this.curContext.setUsed();
/* 1905 */             this.curContext = localSerialCallbackContext;
/*      */           }
/*      */ 
/* 1913 */           this.defaultDataEnd = false;
/*      */         } else {
/* 1915 */           defaultReadFields(paramObject, localObjectStreamClass);
/*      */         }
/* 1917 */         if (localObjectStreamClass.hasWriteObjectData())
/* 1918 */           skipCustomData();
/*      */         else {
/* 1920 */           this.bin.setBlockDataMode(false);
/*      */         }
/*      */       }
/* 1923 */       else if ((paramObject != null) && (localObjectStreamClass.hasReadObjectNoDataMethod()) && (this.handles.lookupException(this.passHandle) == null))
/*      */       {
/* 1927 */         localObjectStreamClass.invokeReadObjectNoData(paramObject);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void skipCustomData()
/*      */     throws IOException
/*      */   {
/* 1938 */     int i = this.passHandle;
/*      */     while (true) {
/* 1940 */       if (this.bin.getBlockDataMode()) {
/* 1941 */         this.bin.skipBlockData();
/* 1942 */         this.bin.setBlockDataMode(false);
/*      */       }
/* 1944 */       switch (this.bin.peekByte()) {
/*      */       case 119:
/*      */       case 122:
/* 1947 */         this.bin.setBlockDataMode(true);
/* 1948 */         break;
/*      */       case 120:
/* 1951 */         this.bin.readByte();
/* 1952 */         this.passHandle = i;
/* 1953 */         return;
/*      */       case 121:
/*      */       default:
/* 1956 */         readObject0(false);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void defaultReadFields(Object paramObject, ObjectStreamClass paramObjectStreamClass)
/*      */     throws IOException
/*      */   {
/* 1970 */     Class localClass = paramObjectStreamClass.forClass();
/* 1971 */     if ((localClass != null) && (paramObject != null) && (!localClass.isInstance(paramObject))) {
/* 1972 */       throw new ClassCastException();
/*      */     }
/*      */ 
/* 1975 */     int i = paramObjectStreamClass.getPrimDataSize();
/* 1976 */     if ((this.primVals == null) || (this.primVals.length < i)) {
/* 1977 */       this.primVals = new byte[i];
/*      */     }
/* 1979 */     this.bin.readFully(this.primVals, 0, i, false);
/* 1980 */     if (paramObject != null) {
/* 1981 */       paramObjectStreamClass.setPrimFieldValues(paramObject, this.primVals);
/*      */     }
/*      */ 
/* 1984 */     int j = this.passHandle;
/* 1985 */     ObjectStreamField[] arrayOfObjectStreamField = paramObjectStreamClass.getFields(false);
/* 1986 */     Object[] arrayOfObject = new Object[paramObjectStreamClass.getNumObjFields()];
/* 1987 */     int k = arrayOfObjectStreamField.length - arrayOfObject.length;
/* 1988 */     for (int m = 0; m < arrayOfObject.length; m++) {
/* 1989 */       ObjectStreamField localObjectStreamField = arrayOfObjectStreamField[(k + m)];
/* 1990 */       arrayOfObject[m] = readObject0(localObjectStreamField.isUnshared());
/* 1991 */       if (localObjectStreamField.getField() != null) {
/* 1992 */         this.handles.markDependency(j, this.passHandle);
/*      */       }
/*      */     }
/* 1995 */     if (paramObject != null) {
/* 1996 */       paramObjectStreamClass.setObjFieldValues(paramObject, arrayOfObject);
/*      */     }
/* 1998 */     this.passHandle = j;
/*      */   }
/*      */ 
/*      */   private IOException readFatalException()
/*      */     throws IOException
/*      */   {
/* 2007 */     if (this.bin.readByte() != 123) {
/* 2008 */       throw new InternalError();
/*      */     }
/* 2010 */     clear();
/* 2011 */     return (IOException)readObject0(false);
/*      */   }
/*      */ 
/*      */   private void handleReset()
/*      */     throws StreamCorruptedException
/*      */   {
/* 2020 */     if (this.depth > 0) {
/* 2021 */       throw new StreamCorruptedException("unexpected reset; recursion depth: " + this.depth);
/*      */     }
/*      */ 
/* 2024 */     clear();
/*      */   }
/*      */ 
/*      */   private static native void bytesToFloats(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3);
/*      */ 
/*      */   private static native void bytesToDoubles(byte[] paramArrayOfByte, int paramInt1, double[] paramArrayOfDouble, int paramInt2, int paramInt3);
/*      */ 
/*      */   private static ClassLoader latestUserDefinedLoader()
/*      */   {
/* 2055 */     return VM.latestUserDefinedLoader();
/*      */   }
/*      */ 
/*      */   private static Object cloneArray(Object paramObject)
/*      */   {
/* 3512 */     if ((paramObject instanceof Object[]))
/* 3513 */       return ((Object[])paramObject).clone();
/* 3514 */     if ((paramObject instanceof boolean[]))
/* 3515 */       return ((boolean[])paramObject).clone();
/* 3516 */     if ((paramObject instanceof byte[]))
/* 3517 */       return ((byte[])paramObject).clone();
/* 3518 */     if ((paramObject instanceof char[]))
/* 3519 */       return ((char[])paramObject).clone();
/* 3520 */     if ((paramObject instanceof double[]))
/* 3521 */       return ((double[])paramObject).clone();
/* 3522 */     if ((paramObject instanceof float[]))
/* 3523 */       return ((float[])paramObject).clone();
/* 3524 */     if ((paramObject instanceof int[]))
/* 3525 */       return ((int[])paramObject).clone();
/* 3526 */     if ((paramObject instanceof long[]))
/* 3527 */       return ((long[])paramObject).clone();
/* 3528 */     if ((paramObject instanceof short[])) {
/* 3529 */       return ((short[])paramObject).clone();
/*      */     }
/* 3531 */     throw new AssertionError();
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  219 */     primClasses.put("boolean", Boolean.TYPE);
/*  220 */     primClasses.put("byte", Byte.TYPE);
/*  221 */     primClasses.put("char", Character.TYPE);
/*  222 */     primClasses.put("short", Short.TYPE);
/*  223 */     primClasses.put("int", Integer.TYPE);
/*  224 */     primClasses.put("long", Long.TYPE);
/*  225 */     primClasses.put("float", Float.TYPE);
/*  226 */     primClasses.put("double", Double.TYPE);
/*  227 */     primClasses.put("void", Void.TYPE);
/*      */   }
/*      */ 
/*      */   private class BlockDataInputStream extends InputStream
/*      */     implements DataInput
/*      */   {
/*      */     private static final int MAX_BLOCK_SIZE = 1024;
/*      */     private static final int MAX_HEADER_SIZE = 5;
/*      */     private static final int CHAR_BUF_SIZE = 256;
/*      */     private static final int HEADER_BLOCKED = -2;
/* 2374 */     private final byte[] buf = new byte[1024];
/*      */ 
/* 2376 */     private final byte[] hbuf = new byte[5];
/*      */ 
/* 2378 */     private final char[] cbuf = new char[256];
/*      */ 
/* 2381 */     private boolean blkmode = false;
/*      */ 
/* 2385 */     private int pos = 0;
/*      */ 
/* 2387 */     private int end = -1;
/*      */ 
/* 2389 */     private int unread = 0;
/*      */     private final ObjectInputStream.PeekInputStream in;
/*      */     private final DataInputStream din;
/*      */ 
/*      */     BlockDataInputStream(InputStream arg2)
/*      */     {
/*      */       InputStream localInputStream;
/* 2401 */       this.in = new ObjectInputStream.PeekInputStream(localInputStream);
/* 2402 */       this.din = new DataInputStream(this);
/*      */     }
/*      */ 
/*      */     boolean setBlockDataMode(boolean paramBoolean)
/*      */       throws IOException
/*      */     {
/* 2413 */       if (this.blkmode == paramBoolean) {
/* 2414 */         return this.blkmode;
/*      */       }
/* 2416 */       if (paramBoolean) {
/* 2417 */         this.pos = 0;
/* 2418 */         this.end = 0;
/* 2419 */         this.unread = 0;
/* 2420 */       } else if (this.pos < this.end) {
/* 2421 */         throw new IllegalStateException("unread block data");
/*      */       }
/* 2423 */       this.blkmode = paramBoolean;
/* 2424 */       return !this.blkmode;
/*      */     }
/*      */ 
/*      */     boolean getBlockDataMode()
/*      */     {
/* 2432 */       return this.blkmode;
/*      */     }
/*      */ 
/*      */     void skipBlockData()
/*      */       throws IOException
/*      */     {
/* 2441 */       if (!this.blkmode) {
/* 2442 */         throw new IllegalStateException("not in block data mode");
/*      */       }
/* 2444 */       while (this.end >= 0)
/* 2445 */         refill();
/*      */     }
/*      */ 
/*      */     private int readBlockHeader(boolean paramBoolean)
/*      */       throws IOException
/*      */     {
/* 2457 */       if (ObjectInputStream.this.defaultDataEnd)
/*      */       {
/* 2464 */         return -1;
/*      */       }
/*      */       try {
/*      */         while (true) {
/* 2468 */           int i = paramBoolean ? 2147483647 : this.in.available();
/* 2469 */           if (i == 0) {
/* 2470 */             return -2;
/*      */           }
/*      */ 
/* 2473 */           int j = this.in.peek();
/* 2474 */           switch (j) {
/*      */           case 119:
/* 2476 */             if (i < 2) {
/* 2477 */               return -2;
/*      */             }
/* 2479 */             this.in.readFully(this.hbuf, 0, 2);
/* 2480 */             return this.hbuf[1] & 0xFF;
/*      */           case 122:
/* 2483 */             if (i < 5) {
/* 2484 */               return -2;
/*      */             }
/* 2486 */             this.in.readFully(this.hbuf, 0, 5);
/* 2487 */             int k = Bits.getInt(this.hbuf, 1);
/* 2488 */             if (k < 0) {
/* 2489 */               throw new StreamCorruptedException("illegal block data header length: " + k);
/*      */             }
/*      */ 
/* 2493 */             return k;
/*      */           case 121:
/* 2502 */             this.in.read();
/* 2503 */             ObjectInputStream.this.handleReset();
/* 2504 */             break;
/*      */           case 120:
/*      */           default:
/* 2507 */             if ((j >= 0) && ((j < 112) || (j > 126))) {
/* 2508 */               throw new StreamCorruptedException(String.format("invalid type code: %02X", new Object[] { Integer.valueOf(j) }));
/*      */             }
/*      */ 
/* 2512 */             return -1;
/*      */           }
/*      */         }
/*      */       } catch (EOFException localEOFException) {  }
/*      */ 
/* 2516 */       throw new StreamCorruptedException("unexpected EOF while reading block data header");
/*      */     }
/*      */ 
/*      */     private void refill()
/*      */       throws IOException
/*      */     {
/*      */       try
/*      */       {
/*      */         do
/*      */         {
/* 2531 */           this.pos = 0;
/*      */           int i;
/* 2532 */           if (this.unread > 0) {
/* 2533 */             i = this.in.read(this.buf, 0, Math.min(this.unread, 1024));
/*      */ 
/* 2535 */             if (i >= 0) {
/* 2536 */               this.end = i;
/* 2537 */               this.unread -= i;
/*      */             } else {
/* 2539 */               throw new StreamCorruptedException("unexpected EOF in middle of data block");
/*      */             }
/*      */           }
/*      */           else {
/* 2543 */             i = readBlockHeader(true);
/* 2544 */             if (i >= 0) {
/* 2545 */               this.end = 0;
/* 2546 */               this.unread = i;
/*      */             } else {
/* 2548 */               this.end = -1;
/* 2549 */               this.unread = 0;
/*      */             }
/*      */           }
/*      */         }
/* 2552 */         while (this.pos == this.end);
/*      */       } catch (IOException localIOException) {
/* 2554 */         this.pos = 0;
/* 2555 */         this.end = -1;
/* 2556 */         this.unread = 0;
/* 2557 */         throw localIOException;
/*      */       }
/*      */     }
/*      */ 
/*      */     int currentBlockRemaining()
/*      */     {
/* 2567 */       if (this.blkmode) {
/* 2568 */         return this.end >= 0 ? this.end - this.pos + this.unread : 0;
/*      */       }
/* 2570 */       throw new IllegalStateException();
/*      */     }
/*      */ 
/*      */     int peek()
/*      */       throws IOException
/*      */     {
/* 2580 */       if (this.blkmode) {
/* 2581 */         if (this.pos == this.end) {
/* 2582 */           refill();
/*      */         }
/* 2584 */         return this.end >= 0 ? this.buf[this.pos] & 0xFF : -1;
/*      */       }
/* 2586 */       return this.in.peek();
/*      */     }
/*      */ 
/*      */     byte peekByte()
/*      */       throws IOException
/*      */     {
/* 2596 */       int i = peek();
/* 2597 */       if (i < 0) {
/* 2598 */         throw new EOFException();
/*      */       }
/* 2600 */       return (byte)i;
/*      */     }
/*      */ 
/*      */     public int read()
/*      */       throws IOException
/*      */     {
/* 2613 */       if (this.blkmode) {
/* 2614 */         if (this.pos == this.end) {
/* 2615 */           refill();
/*      */         }
/* 2617 */         return this.end >= 0 ? this.buf[(this.pos++)] & 0xFF : -1;
/*      */       }
/* 2619 */       return this.in.read();
/*      */     }
/*      */ 
/*      */     public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*      */     {
/* 2624 */       return read(paramArrayOfByte, paramInt1, paramInt2, false);
/*      */     }
/*      */ 
/*      */     public long skip(long paramLong) throws IOException {
/* 2628 */       long l = paramLong;
/* 2629 */       while (l > 0L)
/*      */       {
/*      */         int i;
/* 2630 */         if (this.blkmode) {
/* 2631 */           if (this.pos == this.end) {
/* 2632 */             refill();
/*      */           }
/* 2634 */           if (this.end < 0) {
/*      */             break;
/*      */           }
/* 2637 */           i = (int)Math.min(l, this.end - this.pos);
/* 2638 */           l -= i;
/* 2639 */           this.pos += i;
/*      */         } else {
/* 2641 */           i = (int)Math.min(l, 1024L);
/* 2642 */           if ((i = this.in.read(this.buf, 0, i)) < 0) {
/*      */             break;
/*      */           }
/* 2645 */           l -= i;
/*      */         }
/*      */       }
/* 2648 */       return paramLong - l;
/*      */     }
/*      */ 
/*      */     public int available() throws IOException {
/* 2652 */       if (this.blkmode) {
/* 2653 */         if ((this.pos == this.end) && (this.unread == 0))
/*      */         {
/* 2655 */           while ((i = readBlockHeader(false)) == 0);
/* 2656 */           switch (i) {
/*      */           case -2:
/* 2658 */             break;
/*      */           case -1:
/* 2661 */             this.pos = 0;
/* 2662 */             this.end = -1;
/* 2663 */             break;
/*      */           default:
/* 2666 */             this.pos = 0;
/* 2667 */             this.end = 0;
/* 2668 */             this.unread = i;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 2673 */         int i = this.unread > 0 ? Math.min(this.in.available(), this.unread) : 0;
/*      */ 
/* 2675 */         return this.end >= 0 ? this.end - this.pos + i : 0;
/*      */       }
/* 2677 */       return this.in.available();
/*      */     }
/*      */ 
/*      */     public void close() throws IOException
/*      */     {
/* 2682 */       if (this.blkmode) {
/* 2683 */         this.pos = 0;
/* 2684 */         this.end = -1;
/* 2685 */         this.unread = 0;
/*      */       }
/* 2687 */       this.in.close();
/*      */     }
/*      */ 
/*      */     int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
/*      */       throws IOException
/*      */     {
/* 2698 */       if (paramInt2 == 0)
/* 2699 */         return 0;
/*      */       int i;
/* 2700 */       if (this.blkmode) {
/* 2701 */         if (this.pos == this.end) {
/* 2702 */           refill();
/*      */         }
/* 2704 */         if (this.end < 0) {
/* 2705 */           return -1;
/*      */         }
/* 2707 */         i = Math.min(paramInt2, this.end - this.pos);
/* 2708 */         System.arraycopy(this.buf, this.pos, paramArrayOfByte, paramInt1, i);
/* 2709 */         this.pos += i;
/* 2710 */         return i;
/* 2711 */       }if (paramBoolean) {
/* 2712 */         i = this.in.read(this.buf, 0, Math.min(paramInt2, 1024));
/* 2713 */         if (i > 0) {
/* 2714 */           System.arraycopy(this.buf, 0, paramArrayOfByte, paramInt1, i);
/*      */         }
/* 2716 */         return i;
/*      */       }
/* 2718 */       return this.in.read(paramArrayOfByte, paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     public void readFully(byte[] paramArrayOfByte)
/*      */       throws IOException
/*      */     {
/* 2731 */       readFully(paramArrayOfByte, 0, paramArrayOfByte.length, false);
/*      */     }
/*      */ 
/*      */     public void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 2735 */       readFully(paramArrayOfByte, paramInt1, paramInt2, false);
/*      */     }
/*      */ 
/*      */     public void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
/*      */       throws IOException
/*      */     {
/* 2741 */       while (paramInt2 > 0) {
/* 2742 */         int i = read(paramArrayOfByte, paramInt1, paramInt2, paramBoolean);
/* 2743 */         if (i < 0) {
/* 2744 */           throw new EOFException();
/*      */         }
/* 2746 */         paramInt1 += i;
/* 2747 */         paramInt2 -= i;
/*      */       }
/*      */     }
/*      */ 
/*      */     public int skipBytes(int paramInt) throws IOException {
/* 2752 */       return this.din.skipBytes(paramInt);
/*      */     }
/*      */ 
/*      */     public boolean readBoolean() throws IOException {
/* 2756 */       int i = read();
/* 2757 */       if (i < 0) {
/* 2758 */         throw new EOFException();
/*      */       }
/* 2760 */       return i != 0;
/*      */     }
/*      */ 
/*      */     public byte readByte() throws IOException {
/* 2764 */       int i = read();
/* 2765 */       if (i < 0) {
/* 2766 */         throw new EOFException();
/*      */       }
/* 2768 */       return (byte)i;
/*      */     }
/*      */ 
/*      */     public int readUnsignedByte() throws IOException {
/* 2772 */       int i = read();
/* 2773 */       if (i < 0) {
/* 2774 */         throw new EOFException();
/*      */       }
/* 2776 */       return i;
/*      */     }
/*      */ 
/*      */     public char readChar() throws IOException {
/* 2780 */       if (!this.blkmode) {
/* 2781 */         this.pos = 0;
/* 2782 */         this.in.readFully(this.buf, 0, 2);
/* 2783 */       } else if (this.end - this.pos < 2) {
/* 2784 */         return this.din.readChar();
/*      */       }
/* 2786 */       char c = Bits.getChar(this.buf, this.pos);
/* 2787 */       this.pos += 2;
/* 2788 */       return c;
/*      */     }
/*      */ 
/*      */     public short readShort() throws IOException {
/* 2792 */       if (!this.blkmode) {
/* 2793 */         this.pos = 0;
/* 2794 */         this.in.readFully(this.buf, 0, 2);
/* 2795 */       } else if (this.end - this.pos < 2) {
/* 2796 */         return this.din.readShort();
/*      */       }
/* 2798 */       short s = Bits.getShort(this.buf, this.pos);
/* 2799 */       this.pos += 2;
/* 2800 */       return s;
/*      */     }
/*      */ 
/*      */     public int readUnsignedShort() throws IOException {
/* 2804 */       if (!this.blkmode) {
/* 2805 */         this.pos = 0;
/* 2806 */         this.in.readFully(this.buf, 0, 2);
/* 2807 */       } else if (this.end - this.pos < 2) {
/* 2808 */         return this.din.readUnsignedShort();
/*      */       }
/* 2810 */       int i = Bits.getShort(this.buf, this.pos) & 0xFFFF;
/* 2811 */       this.pos += 2;
/* 2812 */       return i;
/*      */     }
/*      */ 
/*      */     public int readInt() throws IOException {
/* 2816 */       if (!this.blkmode) {
/* 2817 */         this.pos = 0;
/* 2818 */         this.in.readFully(this.buf, 0, 4);
/* 2819 */       } else if (this.end - this.pos < 4) {
/* 2820 */         return this.din.readInt();
/*      */       }
/* 2822 */       int i = Bits.getInt(this.buf, this.pos);
/* 2823 */       this.pos += 4;
/* 2824 */       return i;
/*      */     }
/*      */ 
/*      */     public float readFloat() throws IOException {
/* 2828 */       if (!this.blkmode) {
/* 2829 */         this.pos = 0;
/* 2830 */         this.in.readFully(this.buf, 0, 4);
/* 2831 */       } else if (this.end - this.pos < 4) {
/* 2832 */         return this.din.readFloat();
/*      */       }
/* 2834 */       float f = Bits.getFloat(this.buf, this.pos);
/* 2835 */       this.pos += 4;
/* 2836 */       return f;
/*      */     }
/*      */ 
/*      */     public long readLong() throws IOException {
/* 2840 */       if (!this.blkmode) {
/* 2841 */         this.pos = 0;
/* 2842 */         this.in.readFully(this.buf, 0, 8);
/* 2843 */       } else if (this.end - this.pos < 8) {
/* 2844 */         return this.din.readLong();
/*      */       }
/* 2846 */       long l = Bits.getLong(this.buf, this.pos);
/* 2847 */       this.pos += 8;
/* 2848 */       return l;
/*      */     }
/*      */ 
/*      */     public double readDouble() throws IOException {
/* 2852 */       if (!this.blkmode) {
/* 2853 */         this.pos = 0;
/* 2854 */         this.in.readFully(this.buf, 0, 8);
/* 2855 */       } else if (this.end - this.pos < 8) {
/* 2856 */         return this.din.readDouble();
/*      */       }
/* 2858 */       double d = Bits.getDouble(this.buf, this.pos);
/* 2859 */       this.pos += 8;
/* 2860 */       return d;
/*      */     }
/*      */ 
/*      */     public String readUTF() throws IOException {
/* 2864 */       return readUTFBody(readUnsignedShort());
/*      */     }
/*      */ 
/*      */     public String readLine() throws IOException {
/* 2868 */       return this.din.readLine();
/*      */     }
/*      */ 
/*      */     void readBooleans(boolean[] paramArrayOfBoolean, int paramInt1, int paramInt2)
/*      */       throws IOException
/*      */     {
/* 2880 */       int j = paramInt1 + paramInt2;
/* 2881 */       while (paramInt1 < j)
/*      */       {
/*      */         int i;
/* 2882 */         if (!this.blkmode) {
/* 2883 */           int k = Math.min(j - paramInt1, 1024);
/* 2884 */           this.in.readFully(this.buf, 0, k);
/* 2885 */           i = paramInt1 + k;
/* 2886 */           this.pos = 0; } else {
/* 2887 */           if (this.end - this.pos < 1) {
/* 2888 */             paramArrayOfBoolean[(paramInt1++)] = this.din.readBoolean();
/* 2889 */             continue;
/*      */           }
/* 2891 */           i = Math.min(j, paramInt1 + this.end - this.pos);
/*      */         }
/*      */ 
/* 2894 */         while (paramInt1 < i)
/* 2895 */           paramArrayOfBoolean[(paramInt1++)] = Bits.getBoolean(this.buf, this.pos++);
/*      */       }
/*      */     }
/*      */ 
/*      */     void readChars(char[] paramArrayOfChar, int paramInt1, int paramInt2) throws IOException
/*      */     {
/* 2901 */       int j = paramInt1 + paramInt2;
/* 2902 */       while (paramInt1 < j)
/*      */       {
/*      */         int i;
/* 2903 */         if (!this.blkmode) {
/* 2904 */           int k = Math.min(j - paramInt1, 512);
/* 2905 */           this.in.readFully(this.buf, 0, k << 1);
/* 2906 */           i = paramInt1 + k;
/* 2907 */           this.pos = 0; } else {
/* 2908 */           if (this.end - this.pos < 2) {
/* 2909 */             paramArrayOfChar[(paramInt1++)] = this.din.readChar();
/* 2910 */             continue;
/*      */           }
/* 2912 */           i = Math.min(j, paramInt1 + (this.end - this.pos >> 1));
/*      */         }
/*      */ 
/* 2915 */         while (paramInt1 < i) {
/* 2916 */           paramArrayOfChar[(paramInt1++)] = Bits.getChar(this.buf, this.pos);
/* 2917 */           this.pos += 2;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     void readShorts(short[] paramArrayOfShort, int paramInt1, int paramInt2) throws IOException {
/* 2923 */       int j = paramInt1 + paramInt2;
/* 2924 */       while (paramInt1 < j)
/*      */       {
/*      */         int i;
/* 2925 */         if (!this.blkmode) {
/* 2926 */           int k = Math.min(j - paramInt1, 512);
/* 2927 */           this.in.readFully(this.buf, 0, k << 1);
/* 2928 */           i = paramInt1 + k;
/* 2929 */           this.pos = 0; } else {
/* 2930 */           if (this.end - this.pos < 2) {
/* 2931 */             paramArrayOfShort[(paramInt1++)] = this.din.readShort();
/* 2932 */             continue;
/*      */           }
/* 2934 */           i = Math.min(j, paramInt1 + (this.end - this.pos >> 1));
/*      */         }
/*      */ 
/* 2937 */         while (paramInt1 < i) {
/* 2938 */           paramArrayOfShort[(paramInt1++)] = Bits.getShort(this.buf, this.pos);
/* 2939 */           this.pos += 2;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     void readInts(int[] paramArrayOfInt, int paramInt1, int paramInt2) throws IOException {
/* 2945 */       int j = paramInt1 + paramInt2;
/* 2946 */       while (paramInt1 < j)
/*      */       {
/*      */         int i;
/* 2947 */         if (!this.blkmode) {
/* 2948 */           int k = Math.min(j - paramInt1, 256);
/* 2949 */           this.in.readFully(this.buf, 0, k << 2);
/* 2950 */           i = paramInt1 + k;
/* 2951 */           this.pos = 0; } else {
/* 2952 */           if (this.end - this.pos < 4) {
/* 2953 */             paramArrayOfInt[(paramInt1++)] = this.din.readInt();
/* 2954 */             continue;
/*      */           }
/* 2956 */           i = Math.min(j, paramInt1 + (this.end - this.pos >> 2));
/*      */         }
/*      */ 
/* 2959 */         while (paramInt1 < i) {
/* 2960 */           paramArrayOfInt[(paramInt1++)] = Bits.getInt(this.buf, this.pos);
/* 2961 */           this.pos += 4;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     void readFloats(float[] paramArrayOfFloat, int paramInt1, int paramInt2) throws IOException {
/* 2967 */       int j = paramInt1 + paramInt2;
/* 2968 */       while (paramInt1 < j)
/*      */       {
/*      */         int i;
/* 2969 */         if (!this.blkmode) {
/* 2970 */           i = Math.min(j - paramInt1, 256);
/* 2971 */           this.in.readFully(this.buf, 0, i << 2);
/* 2972 */           this.pos = 0; } else {
/* 2973 */           if (this.end - this.pos < 4) {
/* 2974 */             paramArrayOfFloat[(paramInt1++)] = this.din.readFloat();
/* 2975 */             continue;
/*      */           }
/* 2977 */           i = Math.min(j - paramInt1, this.end - this.pos >> 2);
/*      */         }
/*      */ 
/* 2980 */         ObjectInputStream.bytesToFloats(this.buf, this.pos, paramArrayOfFloat, paramInt1, i);
/* 2981 */         paramInt1 += i;
/* 2982 */         this.pos += (i << 2);
/*      */       }
/*      */     }
/*      */ 
/*      */     void readLongs(long[] paramArrayOfLong, int paramInt1, int paramInt2) throws IOException {
/* 2987 */       int j = paramInt1 + paramInt2;
/* 2988 */       while (paramInt1 < j)
/*      */       {
/*      */         int i;
/* 2989 */         if (!this.blkmode) {
/* 2990 */           int k = Math.min(j - paramInt1, 128);
/* 2991 */           this.in.readFully(this.buf, 0, k << 3);
/* 2992 */           i = paramInt1 + k;
/* 2993 */           this.pos = 0; } else {
/* 2994 */           if (this.end - this.pos < 8) {
/* 2995 */             paramArrayOfLong[(paramInt1++)] = this.din.readLong();
/* 2996 */             continue;
/*      */           }
/* 2998 */           i = Math.min(j, paramInt1 + (this.end - this.pos >> 3));
/*      */         }
/*      */ 
/* 3001 */         while (paramInt1 < i) {
/* 3002 */           paramArrayOfLong[(paramInt1++)] = Bits.getLong(this.buf, this.pos);
/* 3003 */           this.pos += 8;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     void readDoubles(double[] paramArrayOfDouble, int paramInt1, int paramInt2) throws IOException {
/* 3009 */       int j = paramInt1 + paramInt2;
/* 3010 */       while (paramInt1 < j)
/*      */       {
/*      */         int i;
/* 3011 */         if (!this.blkmode) {
/* 3012 */           i = Math.min(j - paramInt1, 128);
/* 3013 */           this.in.readFully(this.buf, 0, i << 3);
/* 3014 */           this.pos = 0; } else {
/* 3015 */           if (this.end - this.pos < 8) {
/* 3016 */             paramArrayOfDouble[(paramInt1++)] = this.din.readDouble();
/* 3017 */             continue;
/*      */           }
/* 3019 */           i = Math.min(j - paramInt1, this.end - this.pos >> 3);
/*      */         }
/*      */ 
/* 3022 */         ObjectInputStream.bytesToDoubles(this.buf, this.pos, paramArrayOfDouble, paramInt1, i);
/* 3023 */         paramInt1 += i;
/* 3024 */         this.pos += (i << 3);
/*      */       }
/*      */     }
/*      */ 
/*      */     String readLongUTF()
/*      */       throws IOException
/*      */     {
/* 3034 */       return readUTFBody(readLong());
/*      */     }
/*      */ 
/*      */     private String readUTFBody(long paramLong)
/*      */       throws IOException
/*      */     {
/* 3043 */       StringBuilder localStringBuilder = new StringBuilder();
/* 3044 */       if (!this.blkmode) {
/* 3045 */         this.end = (this.pos = 0);
/*      */       }
/*      */ 
/* 3048 */       while (paramLong > 0L) {
/* 3049 */         int i = this.end - this.pos;
/* 3050 */         if ((i >= 3) || (i == paramLong)) {
/* 3051 */           paramLong -= readUTFSpan(localStringBuilder, paramLong);
/*      */         }
/* 3053 */         else if (this.blkmode)
/*      */         {
/* 3055 */           paramLong -= readUTFChar(localStringBuilder, paramLong);
/*      */         }
/*      */         else {
/* 3058 */           if (i > 0) {
/* 3059 */             System.arraycopy(this.buf, this.pos, this.buf, 0, i);
/*      */           }
/* 3061 */           this.pos = 0;
/* 3062 */           this.end = ((int)Math.min(1024L, paramLong));
/* 3063 */           this.in.readFully(this.buf, i, this.end - i);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 3068 */       return localStringBuilder.toString();
/*      */     }
/*      */ 
/*      */     private long readUTFSpan(StringBuilder paramStringBuilder, long paramLong)
/*      */       throws IOException
/*      */     {
/* 3080 */       int i = 0;
/* 3081 */       int j = this.pos;
/* 3082 */       int k = Math.min(this.end - this.pos, 256);
/*      */ 
/* 3084 */       int m = this.pos + (paramLong > k ? k - 2 : (int)paramLong);
/* 3085 */       int n = 0;
/*      */       try
/*      */       {
/* 3088 */         while (this.pos < m)
/*      */         {
/* 3090 */           int i1 = this.buf[(this.pos++)] & 0xFF;
/*      */           int i2;
/* 3091 */           switch (i1 >> 4) {
/*      */           case 0:
/*      */           case 1:
/*      */           case 2:
/*      */           case 3:
/*      */           case 4:
/*      */           case 5:
/*      */           case 6:
/*      */           case 7:
/* 3100 */             this.cbuf[(i++)] = ((char)i1);
/* 3101 */             break;
/*      */           case 12:
/*      */           case 13:
/* 3105 */             i2 = this.buf[(this.pos++)];
/* 3106 */             if ((i2 & 0xC0) != 128) {
/* 3107 */               throw new UTFDataFormatException();
/*      */             }
/* 3109 */             this.cbuf[(i++)] = ((char)((i1 & 0x1F) << 6 | (i2 & 0x3F) << 0));
/*      */ 
/* 3111 */             break;
/*      */           case 14:
/* 3114 */             int i3 = this.buf[(this.pos + 1)];
/* 3115 */             i2 = this.buf[(this.pos + 0)];
/* 3116 */             this.pos += 2;
/* 3117 */             if (((i2 & 0xC0) != 128) || ((i3 & 0xC0) != 128)) {
/* 3118 */               throw new UTFDataFormatException();
/*      */             }
/* 3120 */             this.cbuf[(i++)] = ((char)((i1 & 0xF) << 12 | (i2 & 0x3F) << 6 | (i3 & 0x3F) << 0));
/*      */ 
/* 3123 */             break;
/*      */           case 8:
/*      */           case 9:
/*      */           case 10:
/*      */           case 11:
/*      */           default:
/* 3126 */             throw new UTFDataFormatException();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/* 3132 */         if ((n != 0) || (this.pos - j > paramLong))
/*      */         {
/* 3138 */           this.pos = (j + (int)paramLong);
/*      */           throw new UTFDataFormatException();
/*      */         }
/*      */       }
/*      */       catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
/*      */       {
/* 3130 */         n = 1;
/*      */ 
/* 3132 */         if ((n != 0) || (this.pos - j > paramLong))
/*      */         {
/* 3138 */           this.pos = (j + (int)paramLong);
/*      */           throw new UTFDataFormatException();
/*      */         }
/*      */       }
/*      */       finally
/*      */       {
/* 3132 */         if ((n != 0) || (this.pos - j > paramLong))
/*      */         {
/* 3138 */           this.pos = (j + (int)paramLong);
/* 3139 */           throw new UTFDataFormatException();
/*      */         }
/*      */       }
/*      */ 
/* 3143 */       paramStringBuilder.append(this.cbuf, 0, i);
/* 3144 */       return this.pos - j;
/*      */     }
/*      */ 
/*      */     private int readUTFChar(StringBuilder paramStringBuilder, long paramLong)
/*      */       throws IOException
/*      */     {
/* 3158 */       int i = readByte() & 0xFF;
/*      */       int j;
/* 3159 */       switch (i >> 4) {
/*      */       case 0:
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/* 3168 */         paramStringBuilder.append((char)i);
/* 3169 */         return 1;
/*      */       case 12:
/*      */       case 13:
/* 3173 */         if (paramLong < 2L) {
/* 3174 */           throw new UTFDataFormatException();
/*      */         }
/* 3176 */         j = readByte();
/* 3177 */         if ((j & 0xC0) != 128) {
/* 3178 */           throw new UTFDataFormatException();
/*      */         }
/* 3180 */         paramStringBuilder.append((char)((i & 0x1F) << 6 | (j & 0x3F) << 0));
/*      */ 
/* 3182 */         return 2;
/*      */       case 14:
/* 3185 */         if (paramLong < 3L) {
/* 3186 */           if (paramLong == 2L) {
/* 3187 */             readByte();
/*      */           }
/* 3189 */           throw new UTFDataFormatException();
/*      */         }
/* 3191 */         j = readByte();
/* 3192 */         int k = readByte();
/* 3193 */         if (((j & 0xC0) != 128) || ((k & 0xC0) != 128)) {
/* 3194 */           throw new UTFDataFormatException();
/*      */         }
/* 3196 */         paramStringBuilder.append((char)((i & 0xF) << 12 | (j & 0x3F) << 6 | (k & 0x3F) << 0));
/*      */ 
/* 3199 */         return 3;
/*      */       case 8:
/*      */       case 9:
/*      */       case 10:
/* 3202 */       case 11: } throw new UTFDataFormatException();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Caches
/*      */   {
/*  232 */     static final ConcurrentMap<ObjectStreamClass.WeakClassKey, Boolean> subclassAudits = new ConcurrentHashMap();
/*      */ 
/*  236 */     static final ReferenceQueue<Class<?>> subclassAuditsQueue = new ReferenceQueue();
/*      */   }
/*      */ 
/*      */   public static abstract class GetField
/*      */   {
/*      */     public abstract ObjectStreamClass getObjectStreamClass();
/*      */ 
/*      */     public abstract boolean defaulted(String paramString)
/*      */       throws IOException;
/*      */ 
/*      */     public abstract boolean get(String paramString, boolean paramBoolean)
/*      */       throws IOException;
/*      */ 
/*      */     public abstract byte get(String paramString, byte paramByte)
/*      */       throws IOException;
/*      */ 
/*      */     public abstract char get(String paramString, char paramChar)
/*      */       throws IOException;
/*      */ 
/*      */     public abstract short get(String paramString, short paramShort)
/*      */       throws IOException;
/*      */ 
/*      */     public abstract int get(String paramString, int paramInt)
/*      */       throws IOException;
/*      */ 
/*      */     public abstract long get(String paramString, long paramLong)
/*      */       throws IOException;
/*      */ 
/*      */     public abstract float get(String paramString, float paramFloat)
/*      */       throws IOException;
/*      */ 
/*      */     public abstract double get(String paramString, double paramDouble)
/*      */       throws IOException;
/*      */ 
/*      */     public abstract Object get(String paramString, Object paramObject)
/*      */       throws IOException;
/*      */   }
/*      */ 
/*      */   private class GetFieldImpl extends ObjectInputStream.GetField
/*      */   {
/*      */     private final ObjectStreamClass desc;
/*      */     private final byte[] primVals;
/*      */     private final Object[] objVals;
/*      */     private final int[] objHandles;
/*      */ 
/*      */     GetFieldImpl(ObjectStreamClass arg2)
/*      */     {
/*      */       Object localObject;
/* 2077 */       this.desc = localObject;
/* 2078 */       this.primVals = new byte[localObject.getPrimDataSize()];
/* 2079 */       this.objVals = new Object[localObject.getNumObjFields()];
/* 2080 */       this.objHandles = new int[this.objVals.length];
/*      */     }
/*      */ 
/*      */     public ObjectStreamClass getObjectStreamClass() {
/* 2084 */       return this.desc;
/*      */     }
/*      */ 
/*      */     public boolean defaulted(String paramString) throws IOException {
/* 2088 */       return getFieldOffset(paramString, null) < 0;
/*      */     }
/*      */ 
/*      */     public boolean get(String paramString, boolean paramBoolean) throws IOException {
/* 2092 */       int i = getFieldOffset(paramString, Boolean.TYPE);
/* 2093 */       return i >= 0 ? Bits.getBoolean(this.primVals, i) : paramBoolean;
/*      */     }
/*      */ 
/*      */     public byte get(String paramString, byte paramByte) throws IOException {
/* 2097 */       int i = getFieldOffset(paramString, Byte.TYPE);
/* 2098 */       return i >= 0 ? this.primVals[i] : paramByte;
/*      */     }
/*      */ 
/*      */     public char get(String paramString, char paramChar) throws IOException {
/* 2102 */       int i = getFieldOffset(paramString, Character.TYPE);
/* 2103 */       return i >= 0 ? Bits.getChar(this.primVals, i) : paramChar;
/*      */     }
/*      */ 
/*      */     public short get(String paramString, short paramShort) throws IOException {
/* 2107 */       int i = getFieldOffset(paramString, Short.TYPE);
/* 2108 */       return i >= 0 ? Bits.getShort(this.primVals, i) : paramShort;
/*      */     }
/*      */ 
/*      */     public int get(String paramString, int paramInt) throws IOException {
/* 2112 */       int i = getFieldOffset(paramString, Integer.TYPE);
/* 2113 */       return i >= 0 ? Bits.getInt(this.primVals, i) : paramInt;
/*      */     }
/*      */ 
/*      */     public float get(String paramString, float paramFloat) throws IOException {
/* 2117 */       int i = getFieldOffset(paramString, Float.TYPE);
/* 2118 */       return i >= 0 ? Bits.getFloat(this.primVals, i) : paramFloat;
/*      */     }
/*      */ 
/*      */     public long get(String paramString, long paramLong) throws IOException {
/* 2122 */       int i = getFieldOffset(paramString, Long.TYPE);
/* 2123 */       return i >= 0 ? Bits.getLong(this.primVals, i) : paramLong;
/*      */     }
/*      */ 
/*      */     public double get(String paramString, double paramDouble) throws IOException {
/* 2127 */       int i = getFieldOffset(paramString, Double.TYPE);
/* 2128 */       return i >= 0 ? Bits.getDouble(this.primVals, i) : paramDouble;
/*      */     }
/*      */ 
/*      */     public Object get(String paramString, Object paramObject) throws IOException {
/* 2132 */       int i = getFieldOffset(paramString, Object.class);
/* 2133 */       if (i >= 0) {
/* 2134 */         int j = this.objHandles[i];
/* 2135 */         ObjectInputStream.this.handles.markDependency(ObjectInputStream.this.passHandle, j);
/* 2136 */         return ObjectInputStream.this.handles.lookupException(j) == null ? this.objVals[i] : null;
/*      */       }
/*      */ 
/* 2139 */       return paramObject;
/*      */     }
/*      */ 
/*      */     void readFields()
/*      */       throws IOException
/*      */     {
/* 2147 */       ObjectInputStream.this.bin.readFully(this.primVals, 0, this.primVals.length, false);
/*      */ 
/* 2149 */       int i = ObjectInputStream.this.passHandle;
/* 2150 */       ObjectStreamField[] arrayOfObjectStreamField = this.desc.getFields(false);
/* 2151 */       int j = arrayOfObjectStreamField.length - this.objVals.length;
/* 2152 */       for (int k = 0; k < this.objVals.length; k++) {
/* 2153 */         this.objVals[k] = ObjectInputStream.this.readObject0(arrayOfObjectStreamField[(j + k)].isUnshared());
/*      */ 
/* 2155 */         this.objHandles[k] = ObjectInputStream.this.passHandle;
/*      */       }
/* 2157 */       ObjectInputStream.this.passHandle = i;
/*      */     }
/*      */ 
/*      */     private int getFieldOffset(String paramString, Class paramClass)
/*      */     {
/* 2170 */       ObjectStreamField localObjectStreamField = this.desc.getField(paramString, paramClass);
/* 2171 */       if (localObjectStreamField != null)
/* 2172 */         return localObjectStreamField.getOffset();
/* 2173 */       if (this.desc.getLocalDesc().getField(paramString, paramClass) != null) {
/* 2174 */         return -1;
/*      */       }
/* 2176 */       throw new IllegalArgumentException("no such field " + paramString + " with type " + paramClass);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class HandleTable
/*      */   {
/*      */     private static final byte STATUS_OK = 1;
/*      */     private static final byte STATUS_UNKNOWN = 2;
/*      */     private static final byte STATUS_EXCEPTION = 3;
/*      */     byte[] status;
/*      */     Object[] entries;
/*      */     HandleList[] deps;
/* 3250 */     int lowDep = -1;
/*      */ 
/* 3252 */     int size = 0;
/*      */ 
/*      */     HandleTable(int paramInt)
/*      */     {
/* 3258 */       this.status = new byte[paramInt];
/* 3259 */       this.entries = new Object[paramInt];
/* 3260 */       this.deps = new HandleList[paramInt];
/*      */     }
/*      */ 
/*      */     int assign(Object paramObject)
/*      */     {
/* 3270 */       if (this.size >= this.entries.length) {
/* 3271 */         grow();
/*      */       }
/* 3273 */       this.status[this.size] = 2;
/* 3274 */       this.entries[this.size] = paramObject;
/* 3275 */       return this.size++;
/*      */     }
/*      */ 
/*      */     void markDependency(int paramInt1, int paramInt2)
/*      */     {
/* 3285 */       if ((paramInt1 == -1) || (paramInt2 == -1)) {
/* 3286 */         return;
/*      */       }
/* 3288 */       switch (this.status[paramInt1])
/*      */       {
/*      */       case 2:
/* 3291 */         switch (this.status[paramInt2])
/*      */         {
/*      */         case 1:
/* 3294 */           break;
/*      */         case 3:
/* 3298 */           markException(paramInt1, (ClassNotFoundException)this.entries[paramInt2]);
/*      */ 
/* 3300 */           break;
/*      */         case 2:
/* 3304 */           if (this.deps[paramInt2] == null) {
/* 3305 */             this.deps[paramInt2] = new HandleList();
/*      */           }
/* 3307 */           this.deps[paramInt2].add(paramInt1);
/*      */ 
/* 3310 */           if ((this.lowDep < 0) || (this.lowDep > paramInt2))
/* 3311 */             this.lowDep = paramInt2; break;
/*      */         default:
/* 3316 */           throw new InternalError();
/*      */         }
/*      */ 
/*      */         break;
/*      */       case 3:
/* 3321 */         break;
/*      */       default:
/* 3324 */         throw new InternalError();
/*      */       }
/*      */     }
/*      */ 
/*      */     void markException(int paramInt, ClassNotFoundException paramClassNotFoundException)
/*      */     {
/* 3335 */       switch (this.status[paramInt]) {
/*      */       case 2:
/* 3337 */         this.status[paramInt] = 3;
/* 3338 */         this.entries[paramInt] = paramClassNotFoundException;
/*      */ 
/* 3341 */         HandleList localHandleList = this.deps[paramInt];
/* 3342 */         if (localHandleList != null) {
/* 3343 */           int i = localHandleList.size();
/* 3344 */           for (int j = 0; j < i; j++) {
/* 3345 */             markException(localHandleList.get(j), paramClassNotFoundException);
/*      */           }
/* 3347 */           this.deps[paramInt] = null;
/* 3348 */         }break;
/*      */       case 3:
/* 3352 */         break;
/*      */       default:
/* 3355 */         throw new InternalError();
/*      */       }
/*      */     }
/*      */ 
/*      */     void finish(int paramInt)
/*      */     {
/*      */       int i;
/* 3366 */       if (this.lowDep < 0)
/*      */       {
/* 3368 */         i = paramInt + 1;
/* 3369 */       } else if (this.lowDep >= paramInt)
/*      */       {
/* 3371 */         i = this.size;
/* 3372 */         this.lowDep = -1;
/*      */       }
/*      */       else {
/* 3375 */         return;
/*      */       }
/*      */ 
/* 3379 */       for (int j = paramInt; j < i; j++)
/* 3380 */         switch (this.status[j]) {
/*      */         case 2:
/* 3382 */           this.status[j] = 1;
/* 3383 */           this.deps[j] = null;
/* 3384 */           break;
/*      */         case 1:
/*      */         case 3:
/* 3388 */           break;
/*      */         default:
/* 3391 */           throw new InternalError();
/*      */         }
/*      */     }
/*      */ 
/*      */     void setObject(int paramInt, Object paramObject)
/*      */     {
/* 3403 */       switch (this.status[paramInt]) {
/*      */       case 1:
/*      */       case 2:
/* 3406 */         this.entries[paramInt] = paramObject;
/* 3407 */         break;
/*      */       case 3:
/* 3410 */         break;
/*      */       default:
/* 3413 */         throw new InternalError();
/*      */       }
/*      */     }
/*      */ 
/*      */     Object lookupObject(int paramInt)
/*      */     {
/* 3423 */       return (paramInt != -1) && (this.status[paramInt] != 3) ? this.entries[paramInt] : null;
/*      */     }
/*      */ 
/*      */     ClassNotFoundException lookupException(int paramInt)
/*      */     {
/* 3434 */       return (paramInt != -1) && (this.status[paramInt] == 3) ? (ClassNotFoundException)this.entries[paramInt] : null;
/*      */     }
/*      */ 
/*      */     void clear()
/*      */     {
/* 3443 */       Arrays.fill(this.status, 0, this.size, (byte)0);
/* 3444 */       Arrays.fill(this.entries, 0, this.size, null);
/* 3445 */       Arrays.fill(this.deps, 0, this.size, null);
/* 3446 */       this.lowDep = -1;
/* 3447 */       this.size = 0;
/*      */     }
/*      */ 
/*      */     int size()
/*      */     {
/* 3454 */       return this.size;
/*      */     }
/*      */ 
/*      */     private void grow()
/*      */     {
/* 3461 */       int i = (this.entries.length << 1) + 1;
/*      */ 
/* 3463 */       byte[] arrayOfByte = new byte[i];
/* 3464 */       Object[] arrayOfObject = new Object[i];
/* 3465 */       HandleList[] arrayOfHandleList = new HandleList[i];
/*      */ 
/* 3467 */       System.arraycopy(this.status, 0, arrayOfByte, 0, this.size);
/* 3468 */       System.arraycopy(this.entries, 0, arrayOfObject, 0, this.size);
/* 3469 */       System.arraycopy(this.deps, 0, arrayOfHandleList, 0, this.size);
/*      */ 
/* 3471 */       this.status = arrayOfByte;
/* 3472 */       this.entries = arrayOfObject;
/* 3473 */       this.deps = arrayOfHandleList;
/*      */     }
/*      */ 
/*      */     private static class HandleList
/*      */     {
/* 3480 */       private int[] list = new int[4];
/* 3481 */       private int size = 0;
/*      */ 
/*      */       public void add(int paramInt)
/*      */       {
/* 3487 */         if (this.size >= this.list.length) {
/* 3488 */           int[] arrayOfInt = new int[this.list.length << 1];
/* 3489 */           System.arraycopy(this.list, 0, arrayOfInt, 0, this.list.length);
/* 3490 */           this.list = arrayOfInt;
/*      */         }
/* 3492 */         this.list[(this.size++)] = paramInt;
/*      */       }
/*      */ 
/*      */       public int get(int paramInt) {
/* 3496 */         if (paramInt >= this.size) {
/* 3497 */           throw new ArrayIndexOutOfBoundsException();
/*      */         }
/* 3499 */         return this.list[paramInt];
/*      */       }
/*      */ 
/*      */       public int size() {
/* 3503 */         return this.size;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class PeekInputStream extends InputStream
/*      */   {
/*      */     private final InputStream in;
/* 2279 */     private int peekb = -1;
/*      */ 
/*      */     PeekInputStream(InputStream paramInputStream)
/*      */     {
/* 2285 */       this.in = paramInputStream;
/*      */     }
/*      */ 
/*      */     int peek()
/*      */       throws IOException
/*      */     {
/* 2293 */       return this.peekb = this.in.read();
/*      */     }
/*      */ 
/*      */     public int read() throws IOException {
/* 2297 */       if (this.peekb >= 0) {
/* 2298 */         int i = this.peekb;
/* 2299 */         this.peekb = -1;
/* 2300 */         return i;
/*      */       }
/* 2302 */       return this.in.read();
/*      */     }
/*      */ 
/*      */     public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*      */     {
/* 2307 */       if (paramInt2 == 0)
/* 2308 */         return 0;
/* 2309 */       if (this.peekb < 0) {
/* 2310 */         return this.in.read(paramArrayOfByte, paramInt1, paramInt2);
/*      */       }
/* 2312 */       paramArrayOfByte[(paramInt1++)] = ((byte)this.peekb);
/* 2313 */       paramInt2--;
/* 2314 */       this.peekb = -1;
/* 2315 */       int i = this.in.read(paramArrayOfByte, paramInt1, paramInt2);
/* 2316 */       return i >= 0 ? i + 1 : 1;
/*      */     }
/*      */ 
/*      */     void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*      */     {
/* 2321 */       int i = 0;
/* 2322 */       while (i < paramInt2) {
/* 2323 */         int j = read(paramArrayOfByte, paramInt1 + i, paramInt2 - i);
/* 2324 */         if (j < 0) {
/* 2325 */           throw new EOFException();
/*      */         }
/* 2327 */         i += j;
/*      */       }
/*      */     }
/*      */ 
/*      */     public long skip(long paramLong) throws IOException {
/* 2332 */       if (paramLong <= 0L) {
/* 2333 */         return 0L;
/*      */       }
/* 2335 */       int i = 0;
/* 2336 */       if (this.peekb >= 0) {
/* 2337 */         this.peekb = -1;
/* 2338 */         i++;
/* 2339 */         paramLong -= 1L;
/*      */       }
/* 2341 */       return i + skip(paramLong);
/*      */     }
/*      */ 
/*      */     public int available() throws IOException {
/* 2345 */       return this.in.available() + (this.peekb >= 0 ? 1 : 0);
/*      */     }
/*      */ 
/*      */     public void close() throws IOException {
/* 2349 */       this.in.close();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ValidationList
/*      */   {
/*      */     private Callback list;
/*      */ 
/*      */     void register(ObjectInputValidation paramObjectInputValidation, int paramInt)
/*      */       throws InvalidObjectException
/*      */     {
/* 2220 */       if (paramObjectInputValidation == null) {
/* 2221 */         throw new InvalidObjectException("null callback");
/*      */       }
/*      */ 
/* 2224 */       Object localObject = null; Callback localCallback = this.list;
/* 2225 */       while ((localCallback != null) && (paramInt < localCallback.priority)) {
/* 2226 */         localObject = localCallback;
/* 2227 */         localCallback = localCallback.next;
/*      */       }
/* 2229 */       AccessControlContext localAccessControlContext = AccessController.getContext();
/* 2230 */       if (localObject != null)
/* 2231 */         localObject.next = new Callback(paramObjectInputValidation, paramInt, localCallback, localAccessControlContext);
/*      */       else
/* 2233 */         this.list = new Callback(paramObjectInputValidation, paramInt, this.list, localAccessControlContext);
/*      */     }
/*      */ 
/*      */     void doCallbacks()
/*      */       throws InvalidObjectException
/*      */     {
/*      */       try
/*      */       {
/* 2246 */         while (this.list != null) {
/* 2247 */           AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */           {
/*      */             public Void run() throws InvalidObjectException
/*      */             {
/* 2251 */               ObjectInputStream.ValidationList.this.list.obj.validateObject();
/* 2252 */               return null;
/*      */             }
/*      */           }
/*      */           , this.list.acc);
/*      */ 
/* 2255 */           this.list = this.list.next;
/*      */         }
/*      */       } catch (PrivilegedActionException localPrivilegedActionException) {
/* 2258 */         this.list = null;
/* 2259 */         throw ((InvalidObjectException)localPrivilegedActionException.getException());
/*      */       }
/*      */     }
/*      */ 
/*      */     public void clear()
/*      */     {
/* 2267 */       this.list = null;
/*      */     }
/*      */ 
/*      */     private static class Callback
/*      */     {
/*      */       final ObjectInputValidation obj;
/*      */       final int priority;
/*      */       Callback next;
/*      */       final AccessControlContext acc;
/*      */ 
/*      */       Callback(ObjectInputValidation paramObjectInputValidation, int paramInt, Callback paramCallback, AccessControlContext paramAccessControlContext)
/*      */       {
/* 2197 */         this.obj = paramObjectInputValidation;
/* 2198 */         this.priority = paramInt;
/* 2199 */         this.next = paramCallback;
/* 2200 */         this.acc = paramAccessControlContext;
/*      */       }
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.ObjectInputStream
 * JD-Core Version:    0.6.2
 */