/*      */ package java.io;
/*      */ 
/*      */ import java.lang.ref.ReferenceQueue;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.List;
/*      */ import java.util.concurrent.ConcurrentHashMap;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ import sun.security.action.GetBooleanAction;
/*      */ 
/*      */ public class ObjectOutputStream extends OutputStream
/*      */   implements ObjectOutput, ObjectStreamConstants
/*      */ {
/*      */   private final BlockDataOutputStream bout;
/*      */   private final HandleTable handles;
/*      */   private final ReplaceTable subs;
/*  183 */   private int protocol = 2;
/*      */   private int depth;
/*      */   private byte[] primVals;
/*      */   private final boolean enableOverride;
/*      */   private boolean enableReplace;
/*      */   private SerialCallbackContext curContext;
/*      */   private PutFieldImpl curPut;
/*      */   private final DebugTraceInfoStack debugInfoStack;
/*  212 */   private static final boolean extendedDebugInfo = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.io.serialization.extendedDebugInfo"))).booleanValue();
/*      */ 
/*      */   public ObjectOutputStream(OutputStream paramOutputStream)
/*      */     throws IOException
/*      */   {
/*  241 */     verifySubclass();
/*  242 */     this.bout = new BlockDataOutputStream(paramOutputStream);
/*  243 */     this.handles = new HandleTable(10, 3.0F);
/*  244 */     this.subs = new ReplaceTable(10, 3.0F);
/*  245 */     this.enableOverride = false;
/*  246 */     writeStreamHeader();
/*  247 */     this.bout.setBlockDataMode(true);
/*  248 */     if (extendedDebugInfo)
/*  249 */       this.debugInfoStack = new DebugTraceInfoStack();
/*      */     else
/*  251 */       this.debugInfoStack = null;
/*      */   }
/*      */ 
/*      */   protected ObjectOutputStream()
/*      */     throws IOException, SecurityException
/*      */   {
/*  272 */     SecurityManager localSecurityManager = System.getSecurityManager();
/*  273 */     if (localSecurityManager != null) {
/*  274 */       localSecurityManager.checkPermission(SUBCLASS_IMPLEMENTATION_PERMISSION);
/*      */     }
/*  276 */     this.bout = null;
/*  277 */     this.handles = null;
/*  278 */     this.subs = null;
/*  279 */     this.enableOverride = true;
/*  280 */     this.debugInfoStack = null;
/*      */   }
/*      */ 
/*      */   public void useProtocolVersion(int paramInt)
/*      */     throws IOException
/*      */   {
/*  304 */     if (this.handles.size() != 0)
/*      */     {
/*  306 */       throw new IllegalStateException("stream non-empty");
/*      */     }
/*  308 */     switch (paramInt) {
/*      */     case 1:
/*      */     case 2:
/*  311 */       this.protocol = paramInt;
/*  312 */       break;
/*      */     default:
/*  315 */       throw new IllegalArgumentException("unknown version: " + paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void writeObject(Object paramObject)
/*      */     throws IOException
/*      */   {
/*  342 */     if (this.enableOverride) {
/*  343 */       writeObjectOverride(paramObject);
/*  344 */       return;
/*      */     }
/*      */     try {
/*  347 */       writeObject0(paramObject, false);
/*      */     } catch (IOException localIOException) {
/*  349 */       if (this.depth == 0) {
/*  350 */         writeFatalException(localIOException);
/*      */       }
/*  352 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void writeObjectOverride(Object paramObject)
/*      */     throws IOException
/*      */   {
/*      */   }
/*      */ 
/*      */   public void writeUnshared(Object paramObject)
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*  414 */       writeObject0(paramObject, true);
/*      */     } catch (IOException localIOException) {
/*  416 */       if (this.depth == 0) {
/*  417 */         writeFatalException(localIOException);
/*      */       }
/*  419 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void defaultWriteObject()
/*      */     throws IOException
/*      */   {
/*  433 */     SerialCallbackContext localSerialCallbackContext = this.curContext;
/*  434 */     if (localSerialCallbackContext == null) {
/*  435 */       throw new NotActiveException("not in call to writeObject");
/*      */     }
/*  437 */     Object localObject = localSerialCallbackContext.getObj();
/*  438 */     ObjectStreamClass localObjectStreamClass = localSerialCallbackContext.getDesc();
/*  439 */     this.bout.setBlockDataMode(false);
/*  440 */     defaultWriteFields(localObject, localObjectStreamClass);
/*  441 */     this.bout.setBlockDataMode(true);
/*      */   }
/*      */ 
/*      */   public PutField putFields()
/*      */     throws IOException
/*      */   {
/*  455 */     if (this.curPut == null) {
/*  456 */       SerialCallbackContext localSerialCallbackContext = this.curContext;
/*  457 */       if (localSerialCallbackContext == null) {
/*  458 */         throw new NotActiveException("not in call to writeObject");
/*      */       }
/*  460 */       Object localObject = localSerialCallbackContext.getObj();
/*  461 */       ObjectStreamClass localObjectStreamClass = localSerialCallbackContext.getDesc();
/*  462 */       this.curPut = new PutFieldImpl(localObjectStreamClass);
/*      */     }
/*  464 */     return this.curPut;
/*      */   }
/*      */ 
/*      */   public void writeFields()
/*      */     throws IOException
/*      */   {
/*  477 */     if (this.curPut == null) {
/*  478 */       throw new NotActiveException("no current PutField object");
/*      */     }
/*  480 */     this.bout.setBlockDataMode(false);
/*  481 */     this.curPut.writeFields();
/*  482 */     this.bout.setBlockDataMode(true);
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */     throws IOException
/*      */   {
/*  496 */     if (this.depth != 0) {
/*  497 */       throw new IOException("stream active");
/*      */     }
/*  499 */     this.bout.setBlockDataMode(false);
/*  500 */     this.bout.writeByte(121);
/*  501 */     clear();
/*  502 */     this.bout.setBlockDataMode(true);
/*      */   }
/*      */ 
/*      */   protected void annotateClass(Class<?> paramClass)
/*      */     throws IOException
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void annotateProxyClass(Class<?> paramClass)
/*      */     throws IOException
/*      */   {
/*      */   }
/*      */ 
/*      */   protected Object replaceObject(Object paramObject)
/*      */     throws IOException
/*      */   {
/*  587 */     return paramObject;
/*      */   }
/*      */ 
/*      */   protected boolean enableReplaceObject(boolean paramBoolean)
/*      */     throws SecurityException
/*      */   {
/*  613 */     if (paramBoolean == this.enableReplace) {
/*  614 */       return paramBoolean;
/*      */     }
/*  616 */     if (paramBoolean) {
/*  617 */       SecurityManager localSecurityManager = System.getSecurityManager();
/*  618 */       if (localSecurityManager != null) {
/*  619 */         localSecurityManager.checkPermission(SUBSTITUTION_PERMISSION);
/*      */       }
/*      */     }
/*  622 */     this.enableReplace = paramBoolean;
/*  623 */     return !this.enableReplace;
/*      */   }
/*      */ 
/*      */   protected void writeStreamHeader()
/*      */     throws IOException
/*      */   {
/*  635 */     this.bout.writeShort(-21267);
/*  636 */     this.bout.writeShort(5);
/*      */   }
/*      */ 
/*      */   protected void writeClassDescriptor(ObjectStreamClass paramObjectStreamClass)
/*      */     throws IOException
/*      */   {
/*  667 */     paramObjectStreamClass.writeNonProxy(this);
/*      */   }
/*      */ 
/*      */   public void write(int paramInt)
/*      */     throws IOException
/*      */   {
/*  678 */     this.bout.write(paramInt);
/*      */   }
/*      */ 
/*      */   public void write(byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/*  689 */     this.bout.write(paramArrayOfByte, 0, paramArrayOfByte.length, false);
/*      */   }
/*      */ 
/*      */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */     throws IOException
/*      */   {
/*  701 */     if (paramArrayOfByte == null) {
/*  702 */       throw new NullPointerException();
/*      */     }
/*  704 */     int i = paramInt1 + paramInt2;
/*  705 */     if ((paramInt1 < 0) || (paramInt2 < 0) || (i > paramArrayOfByte.length) || (i < 0)) {
/*  706 */       throw new IndexOutOfBoundsException();
/*      */     }
/*  708 */     this.bout.write(paramArrayOfByte, paramInt1, paramInt2, false);
/*      */   }
/*      */ 
/*      */   public void flush()
/*      */     throws IOException
/*      */   {
/*  718 */     this.bout.flush();
/*      */   }
/*      */ 
/*      */   protected void drain()
/*      */     throws IOException
/*      */   {
/*  729 */     this.bout.drain();
/*      */   }
/*      */ 
/*      */   public void close()
/*      */     throws IOException
/*      */   {
/*  739 */     flush();
/*  740 */     clear();
/*  741 */     this.bout.close();
/*      */   }
/*      */ 
/*      */   public void writeBoolean(boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*  752 */     this.bout.writeBoolean(paramBoolean);
/*      */   }
/*      */ 
/*      */   public void writeByte(int paramInt)
/*      */     throws IOException
/*      */   {
/*  763 */     this.bout.writeByte(paramInt);
/*      */   }
/*      */ 
/*      */   public void writeShort(int paramInt)
/*      */     throws IOException
/*      */   {
/*  774 */     this.bout.writeShort(paramInt);
/*      */   }
/*      */ 
/*      */   public void writeChar(int paramInt)
/*      */     throws IOException
/*      */   {
/*  785 */     this.bout.writeChar(paramInt);
/*      */   }
/*      */ 
/*      */   public void writeInt(int paramInt)
/*      */     throws IOException
/*      */   {
/*  796 */     this.bout.writeInt(paramInt);
/*      */   }
/*      */ 
/*      */   public void writeLong(long paramLong)
/*      */     throws IOException
/*      */   {
/*  807 */     this.bout.writeLong(paramLong);
/*      */   }
/*      */ 
/*      */   public void writeFloat(float paramFloat)
/*      */     throws IOException
/*      */   {
/*  818 */     this.bout.writeFloat(paramFloat);
/*      */   }
/*      */ 
/*      */   public void writeDouble(double paramDouble)
/*      */     throws IOException
/*      */   {
/*  829 */     this.bout.writeDouble(paramDouble);
/*      */   }
/*      */ 
/*      */   public void writeBytes(String paramString)
/*      */     throws IOException
/*      */   {
/*  840 */     this.bout.writeBytes(paramString);
/*      */   }
/*      */ 
/*      */   public void writeChars(String paramString)
/*      */     throws IOException
/*      */   {
/*  851 */     this.bout.writeChars(paramString);
/*      */   }
/*      */ 
/*      */   public void writeUTF(String paramString)
/*      */     throws IOException
/*      */   {
/*  868 */     this.bout.writeUTF(paramString);
/*      */   }
/*      */ 
/*      */   int getProtocolVersion()
/*      */   {
/* 1015 */     return this.protocol;
/*      */   }
/*      */ 
/*      */   void writeTypeString(String paramString)
/*      */     throws IOException
/*      */   {
/* 1024 */     if (paramString == null) {
/* 1025 */       writeNull();
/*      */     }
/*      */     else
/*      */     {
/*      */       int i;
/* 1026 */       if ((i = this.handles.lookup(paramString)) != -1)
/* 1027 */         writeHandle(i);
/*      */       else
/* 1029 */         writeString(paramString, false);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void verifySubclass()
/*      */   {
/* 1040 */     Class localClass = getClass();
/* 1041 */     if (localClass == ObjectOutputStream.class) {
/* 1042 */       return;
/*      */     }
/* 1044 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 1045 */     if (localSecurityManager == null) {
/* 1046 */       return;
/*      */     }
/* 1048 */     ObjectStreamClass.processQueue(Caches.subclassAuditsQueue, Caches.subclassAudits);
/* 1049 */     ObjectStreamClass.WeakClassKey localWeakClassKey = new ObjectStreamClass.WeakClassKey(localClass, Caches.subclassAuditsQueue);
/* 1050 */     Boolean localBoolean = (Boolean)Caches.subclassAudits.get(localWeakClassKey);
/* 1051 */     if (localBoolean == null) {
/* 1052 */       localBoolean = Boolean.valueOf(auditSubclass(localClass));
/* 1053 */       Caches.subclassAudits.putIfAbsent(localWeakClassKey, localBoolean);
/*      */     }
/* 1055 */     if (localBoolean.booleanValue()) {
/* 1056 */       return;
/*      */     }
/* 1058 */     localSecurityManager.checkPermission(SUBCLASS_IMPLEMENTATION_PERMISSION);
/*      */   }
/*      */ 
/*      */   private static boolean auditSubclass(Class paramClass)
/*      */   {
/* 1067 */     Boolean localBoolean = (Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Boolean run() {
/* 1070 */         for (Class localClass = this.val$subcl; 
/* 1071 */           localClass != ObjectOutputStream.class; 
/* 1072 */           localClass = localClass.getSuperclass())
/*      */           try
/*      */           {
/* 1075 */             localClass.getDeclaredMethod("writeUnshared", new Class[] { Object.class });
/*      */ 
/* 1077 */             return Boolean.FALSE;
/*      */           }
/*      */           catch (NoSuchMethodException localNoSuchMethodException1) {
/*      */             try {
/* 1081 */               localClass.getDeclaredMethod("putFields", (Class[])null);
/* 1082 */               return Boolean.FALSE;
/*      */             } catch (NoSuchMethodException localNoSuchMethodException2) {
/*      */             }
/*      */           }
/* 1086 */         return Boolean.TRUE;
/*      */       }
/*      */     });
/* 1090 */     return localBoolean.booleanValue();
/*      */   }
/*      */ 
/*      */   private void clear()
/*      */   {
/* 1097 */     this.subs.clear();
/* 1098 */     this.handles.clear();
/*      */   }
/*      */ 
/*      */   private void writeObject0(Object paramObject, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1107 */     boolean bool = this.bout.setBlockDataMode(false);
/* 1108 */     this.depth += 1;
/*      */     try
/*      */     {
/* 1112 */       if ((paramObject = this.subs.lookup(paramObject)) == null) {
/* 1113 */         writeNull();
/*      */       }
/*      */       else
/*      */       {
/*      */         int i;
/* 1115 */         if ((!paramBoolean) && ((i = this.handles.lookup(paramObject)) != -1)) {
/* 1116 */           writeHandle(i);
/*      */         }
/* 1118 */         else if ((paramObject instanceof Class)) {
/* 1119 */           writeClass((Class)paramObject, paramBoolean);
/*      */         }
/* 1121 */         else if ((paramObject instanceof ObjectStreamClass)) {
/* 1122 */           writeClassDesc((ObjectStreamClass)paramObject, paramBoolean);
/*      */         }
/*      */         else {
/* 1127 */           Object localObject1 = paramObject;
/* 1128 */           Object localObject2 = paramObject.getClass();
/*      */           ObjectStreamClass localObjectStreamClass;
/*      */           Object localObject3;
/*      */           while (true) {
/* 1133 */             localObjectStreamClass = ObjectStreamClass.lookup((Class)localObject2, true);
/* 1134 */             if ((!localObjectStreamClass.hasWriteReplaceMethod()) || ((paramObject = localObjectStreamClass.invokeWriteReplace(paramObject)) == null) || ((localObject3 = paramObject.getClass()) == localObject2))
/*      */             {
/*      */               break;
/*      */             }
/*      */ 
/* 1140 */             localObject2 = localObject3;
/*      */           }
/* 1142 */           if (this.enableReplace) {
/* 1143 */             localObject3 = replaceObject(paramObject);
/* 1144 */             if ((localObject3 != paramObject) && (localObject3 != null)) {
/* 1145 */               localObject2 = localObject3.getClass();
/* 1146 */               localObjectStreamClass = ObjectStreamClass.lookup((Class)localObject2, true);
/*      */             }
/* 1148 */             paramObject = localObject3;
/*      */           }
/*      */ 
/* 1152 */           if (paramObject != localObject1) {
/* 1153 */             this.subs.assign(localObject1, paramObject);
/* 1154 */             if (paramObject == null) { writeNull();
/*      */               return; }
/* 1157 */             if ((!paramBoolean) && ((i = this.handles.lookup(paramObject)) != -1)) { writeHandle(i);
/*      */               return; }
/* 1160 */             if ((paramObject instanceof Class)) { writeClass((Class)paramObject, paramBoolean);
/*      */               return; }
/* 1163 */             if ((paramObject instanceof ObjectStreamClass))
/*      */             {
/* 1164 */               writeClassDesc((ObjectStreamClass)paramObject, paramBoolean);
/*      */               return;
/*      */             }
/*      */           }
/* 1170 */           if ((paramObject instanceof String)) {
/* 1171 */             writeString((String)paramObject, paramBoolean);
/* 1172 */           } else if (((Class)localObject2).isArray()) {
/* 1173 */             writeArray(paramObject, localObjectStreamClass, paramBoolean);
/* 1174 */           } else if ((paramObject instanceof Enum)) {
/* 1175 */             writeEnum((Enum)paramObject, localObjectStreamClass, paramBoolean);
/* 1176 */           } else if ((paramObject instanceof Serializable)) {
/* 1177 */             writeOrdinaryObject(paramObject, localObjectStreamClass, paramBoolean);
/*      */           } else {
/* 1179 */             if (extendedDebugInfo) {
/* 1180 */               throw new NotSerializableException(((Class)localObject2).getName() + "\n" + this.debugInfoStack.toString());
/*      */             }
/*      */ 
/* 1183 */             throw new NotSerializableException(((Class)localObject2).getName());
/*      */           }
/*      */         }
/*      */       }
/*      */     } finally { this.depth -= 1;
/* 1188 */       this.bout.setBlockDataMode(bool);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeNull()
/*      */     throws IOException
/*      */   {
/* 1196 */     this.bout.writeByte(112);
/*      */   }
/*      */ 
/*      */   private void writeHandle(int paramInt)
/*      */     throws IOException
/*      */   {
/* 1203 */     this.bout.writeByte(113);
/* 1204 */     this.bout.writeInt(8257536 + paramInt);
/*      */   }
/*      */ 
/*      */   private void writeClass(Class paramClass, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1211 */     this.bout.writeByte(118);
/* 1212 */     writeClassDesc(ObjectStreamClass.lookup(paramClass, true), false);
/* 1213 */     this.handles.assign(paramBoolean ? null : paramClass);
/*      */   }
/*      */ 
/*      */   private void writeClassDesc(ObjectStreamClass paramObjectStreamClass, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1223 */     if (paramObjectStreamClass == null) {
/* 1224 */       writeNull();
/*      */     }
/*      */     else
/*      */     {
/*      */       int i;
/* 1225 */       if ((!paramBoolean) && ((i = this.handles.lookup(paramObjectStreamClass)) != -1))
/* 1226 */         writeHandle(i);
/* 1227 */       else if (paramObjectStreamClass.isProxy())
/* 1228 */         writeProxyDesc(paramObjectStreamClass, paramBoolean);
/*      */       else
/* 1230 */         writeNonProxyDesc(paramObjectStreamClass, paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   private boolean isCustomSubclass()
/*      */   {
/* 1236 */     return getClass().getClassLoader() != ObjectOutputStream.class.getClassLoader();
/*      */   }
/*      */ 
/*      */   private void writeProxyDesc(ObjectStreamClass paramObjectStreamClass, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1246 */     this.bout.writeByte(125);
/* 1247 */     this.handles.assign(paramBoolean ? null : paramObjectStreamClass);
/*      */ 
/* 1249 */     Class localClass = paramObjectStreamClass.forClass();
/* 1250 */     Class[] arrayOfClass = localClass.getInterfaces();
/* 1251 */     this.bout.writeInt(arrayOfClass.length);
/* 1252 */     for (int i = 0; i < arrayOfClass.length; i++) {
/* 1253 */       this.bout.writeUTF(arrayOfClass[i].getName());
/*      */     }
/*      */ 
/* 1256 */     this.bout.setBlockDataMode(true);
/* 1257 */     if ((localClass != null) && (isCustomSubclass())) {
/* 1258 */       ReflectUtil.checkPackageAccess(localClass);
/*      */     }
/* 1260 */     annotateProxyClass(localClass);
/* 1261 */     this.bout.setBlockDataMode(false);
/* 1262 */     this.bout.writeByte(120);
/*      */ 
/* 1264 */     writeClassDesc(paramObjectStreamClass.getSuperDesc(), false);
/*      */   }
/*      */ 
/*      */   private void writeNonProxyDesc(ObjectStreamClass paramObjectStreamClass, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1274 */     this.bout.writeByte(114);
/* 1275 */     this.handles.assign(paramBoolean ? null : paramObjectStreamClass);
/*      */ 
/* 1277 */     if (this.protocol == 1)
/*      */     {
/* 1279 */       paramObjectStreamClass.writeNonProxy(this);
/*      */     }
/* 1281 */     else writeClassDescriptor(paramObjectStreamClass);
/*      */ 
/* 1284 */     Class localClass = paramObjectStreamClass.forClass();
/* 1285 */     this.bout.setBlockDataMode(true);
/* 1286 */     if ((localClass != null) && (isCustomSubclass())) {
/* 1287 */       ReflectUtil.checkPackageAccess(localClass);
/*      */     }
/* 1289 */     annotateClass(localClass);
/* 1290 */     this.bout.setBlockDataMode(false);
/* 1291 */     this.bout.writeByte(120);
/*      */ 
/* 1293 */     writeClassDesc(paramObjectStreamClass.getSuperDesc(), false);
/*      */   }
/*      */ 
/*      */   private void writeString(String paramString, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1301 */     this.handles.assign(paramBoolean ? null : paramString);
/* 1302 */     long l = this.bout.getUTFLength(paramString);
/* 1303 */     if (l <= 65535L) {
/* 1304 */       this.bout.writeByte(116);
/* 1305 */       this.bout.writeUTF(paramString, l);
/*      */     } else {
/* 1307 */       this.bout.writeByte(124);
/* 1308 */       this.bout.writeLongUTF(paramString, l);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeArray(Object paramObject, ObjectStreamClass paramObjectStreamClass, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1320 */     this.bout.writeByte(117);
/* 1321 */     writeClassDesc(paramObjectStreamClass, false);
/* 1322 */     this.handles.assign(paramBoolean ? null : paramObject);
/*      */ 
/* 1324 */     Class localClass = paramObjectStreamClass.forClass().getComponentType();
/*      */     Object localObject1;
/* 1325 */     if (localClass.isPrimitive()) {
/* 1326 */       if (localClass == Integer.TYPE) {
/* 1327 */         localObject1 = (int[])paramObject;
/* 1328 */         this.bout.writeInt(localObject1.length);
/* 1329 */         this.bout.writeInts((int[])localObject1, 0, localObject1.length);
/* 1330 */       } else if (localClass == Byte.TYPE) {
/* 1331 */         localObject1 = (byte[])paramObject;
/* 1332 */         this.bout.writeInt(localObject1.length);
/* 1333 */         this.bout.write((byte[])localObject1, 0, localObject1.length, true);
/* 1334 */       } else if (localClass == Long.TYPE) {
/* 1335 */         localObject1 = (long[])paramObject;
/* 1336 */         this.bout.writeInt(localObject1.length);
/* 1337 */         this.bout.writeLongs((long[])localObject1, 0, localObject1.length);
/* 1338 */       } else if (localClass == Float.TYPE) {
/* 1339 */         localObject1 = (float[])paramObject;
/* 1340 */         this.bout.writeInt(localObject1.length);
/* 1341 */         this.bout.writeFloats((float[])localObject1, 0, localObject1.length);
/* 1342 */       } else if (localClass == Double.TYPE) {
/* 1343 */         localObject1 = (double[])paramObject;
/* 1344 */         this.bout.writeInt(localObject1.length);
/* 1345 */         this.bout.writeDoubles((double[])localObject1, 0, localObject1.length);
/* 1346 */       } else if (localClass == Short.TYPE) {
/* 1347 */         localObject1 = (short[])paramObject;
/* 1348 */         this.bout.writeInt(localObject1.length);
/* 1349 */         this.bout.writeShorts((short[])localObject1, 0, localObject1.length);
/* 1350 */       } else if (localClass == Character.TYPE) {
/* 1351 */         localObject1 = (char[])paramObject;
/* 1352 */         this.bout.writeInt(localObject1.length);
/* 1353 */         this.bout.writeChars((char[])localObject1, 0, localObject1.length);
/* 1354 */       } else if (localClass == Boolean.TYPE) {
/* 1355 */         localObject1 = (boolean[])paramObject;
/* 1356 */         this.bout.writeInt(localObject1.length);
/* 1357 */         this.bout.writeBooleans((boolean[])localObject1, 0, localObject1.length);
/*      */       } else {
/* 1359 */         throw new InternalError();
/*      */       }
/*      */     } else {
/* 1362 */       localObject1 = (Object[])paramObject;
/* 1363 */       int i = localObject1.length;
/* 1364 */       this.bout.writeInt(i);
/* 1365 */       if (extendedDebugInfo) {
/* 1366 */         this.debugInfoStack.push("array (class \"" + paramObject.getClass().getName() + "\", size: " + i + ")");
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/* 1371 */         for (int j = 0; j < i; j++) {
/* 1372 */           if (extendedDebugInfo) {
/* 1373 */             this.debugInfoStack.push("element of array (index: " + j + ")");
/*      */           }
/*      */           try
/*      */           {
/* 1377 */             writeObject0(localObject1[j], false);
/*      */           }
/*      */           finally
/*      */           {
/*      */           }
/*      */         }
/*      */       }
/*      */       finally {
/* 1385 */         if (extendedDebugInfo)
/* 1386 */           this.debugInfoStack.pop();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeEnum(Enum paramEnum, ObjectStreamClass paramObjectStreamClass, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1400 */     this.bout.writeByte(126);
/* 1401 */     ObjectStreamClass localObjectStreamClass = paramObjectStreamClass.getSuperDesc();
/* 1402 */     writeClassDesc(localObjectStreamClass.forClass() == Enum.class ? paramObjectStreamClass : localObjectStreamClass, false);
/* 1403 */     this.handles.assign(paramBoolean ? null : paramEnum);
/* 1404 */     writeString(paramEnum.name(), false);
/*      */   }
/*      */ 
/*      */   private void writeOrdinaryObject(Object paramObject, ObjectStreamClass paramObjectStreamClass, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/* 1417 */     if (extendedDebugInfo) {
/* 1418 */       this.debugInfoStack.push((this.depth == 1 ? "root " : "") + "object (class \"" + paramObject.getClass().getName() + "\", " + paramObject.toString() + ")");
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1423 */       paramObjectStreamClass.checkSerialize();
/*      */ 
/* 1425 */       this.bout.writeByte(115);
/* 1426 */       writeClassDesc(paramObjectStreamClass, false);
/* 1427 */       this.handles.assign(paramBoolean ? null : paramObject);
/* 1428 */       if ((paramObjectStreamClass.isExternalizable()) && (!paramObjectStreamClass.isProxy()))
/* 1429 */         writeExternalData((Externalizable)paramObject);
/*      */       else
/* 1431 */         writeSerialData(paramObject, paramObjectStreamClass);
/*      */     }
/*      */     finally {
/* 1434 */       if (extendedDebugInfo)
/* 1435 */         this.debugInfoStack.pop();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeExternalData(Externalizable paramExternalizable)
/*      */     throws IOException
/*      */   {
/* 1445 */     PutFieldImpl localPutFieldImpl = this.curPut;
/* 1446 */     this.curPut = null;
/*      */ 
/* 1448 */     if (extendedDebugInfo) {
/* 1449 */       this.debugInfoStack.push("writeExternal data");
/*      */     }
/* 1451 */     SerialCallbackContext localSerialCallbackContext = this.curContext;
/*      */     try {
/* 1453 */       this.curContext = null;
/* 1454 */       if (this.protocol == 1) {
/* 1455 */         paramExternalizable.writeExternal(this);
/*      */       } else {
/* 1457 */         this.bout.setBlockDataMode(true);
/* 1458 */         paramExternalizable.writeExternal(this);
/* 1459 */         this.bout.setBlockDataMode(false);
/* 1460 */         this.bout.writeByte(120);
/*      */       }
/*      */     } finally {
/* 1463 */       this.curContext = localSerialCallbackContext;
/* 1464 */       if (extendedDebugInfo) {
/* 1465 */         this.debugInfoStack.pop();
/*      */       }
/*      */     }
/*      */ 
/* 1469 */     this.curPut = localPutFieldImpl;
/*      */   }
/*      */ 
/*      */   private void writeSerialData(Object paramObject, ObjectStreamClass paramObjectStreamClass)
/*      */     throws IOException
/*      */   {
/* 1479 */     ObjectStreamClass.ClassDataSlot[] arrayOfClassDataSlot = paramObjectStreamClass.getClassDataLayout();
/* 1480 */     for (int i = 0; i < arrayOfClassDataSlot.length; i++) {
/* 1481 */       ObjectStreamClass localObjectStreamClass = arrayOfClassDataSlot[i].desc;
/* 1482 */       if (localObjectStreamClass.hasWriteObjectMethod()) {
/* 1483 */         PutFieldImpl localPutFieldImpl = this.curPut;
/* 1484 */         this.curPut = null;
/* 1485 */         SerialCallbackContext localSerialCallbackContext = this.curContext;
/*      */ 
/* 1487 */         if (extendedDebugInfo) {
/* 1488 */           this.debugInfoStack.push("custom writeObject data (class \"" + localObjectStreamClass.getName() + "\")");
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/* 1493 */           this.curContext = new SerialCallbackContext(paramObject, localObjectStreamClass);
/* 1494 */           this.bout.setBlockDataMode(true);
/* 1495 */           localObjectStreamClass.invokeWriteObject(paramObject, this);
/* 1496 */           this.bout.setBlockDataMode(false);
/* 1497 */           this.bout.writeByte(120);
/*      */         } finally {
/* 1499 */           this.curContext.setUsed();
/* 1500 */           this.curContext = localSerialCallbackContext;
/* 1501 */           if (extendedDebugInfo) {
/* 1502 */             this.debugInfoStack.pop();
/*      */           }
/*      */         }
/*      */ 
/* 1506 */         this.curPut = localPutFieldImpl;
/*      */       } else {
/* 1508 */         defaultWriteFields(paramObject, localObjectStreamClass);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void defaultWriteFields(Object paramObject, ObjectStreamClass paramObjectStreamClass)
/*      */     throws IOException
/*      */   {
/* 1521 */     Class localClass = paramObjectStreamClass.forClass();
/* 1522 */     if ((localClass != null) && (paramObject != null) && (!localClass.isInstance(paramObject))) {
/* 1523 */       throw new ClassCastException();
/*      */     }
/*      */ 
/* 1526 */     paramObjectStreamClass.checkDefaultSerialize();
/*      */ 
/* 1528 */     int i = paramObjectStreamClass.getPrimDataSize();
/* 1529 */     if ((this.primVals == null) || (this.primVals.length < i)) {
/* 1530 */       this.primVals = new byte[i];
/*      */     }
/* 1532 */     paramObjectStreamClass.getPrimFieldValues(paramObject, this.primVals);
/* 1533 */     this.bout.write(this.primVals, 0, i, false);
/*      */ 
/* 1535 */     ObjectStreamField[] arrayOfObjectStreamField = paramObjectStreamClass.getFields(false);
/* 1536 */     Object[] arrayOfObject = new Object[paramObjectStreamClass.getNumObjFields()];
/* 1537 */     int j = arrayOfObjectStreamField.length - arrayOfObject.length;
/* 1538 */     paramObjectStreamClass.getObjFieldValues(paramObject, arrayOfObject);
/* 1539 */     for (int k = 0; k < arrayOfObject.length; k++) {
/* 1540 */       if (extendedDebugInfo) {
/* 1541 */         this.debugInfoStack.push("field (class \"" + paramObjectStreamClass.getName() + "\", name: \"" + arrayOfObjectStreamField[(j + k)].getName() + "\", type: \"" + arrayOfObjectStreamField[(j + k)].getType() + "\")");
/*      */       }
/*      */ 
/*      */       try
/*      */       {
/* 1547 */         writeObject0(arrayOfObject[k], arrayOfObjectStreamField[(j + k)].isUnshared());
/*      */       }
/*      */       finally {
/* 1550 */         if (extendedDebugInfo)
/* 1551 */           this.debugInfoStack.pop();
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeFatalException(IOException paramIOException)
/*      */     throws IOException
/*      */   {
/* 1572 */     clear();
/* 1573 */     boolean bool = this.bout.setBlockDataMode(false);
/*      */     try {
/* 1575 */       this.bout.writeByte(123);
/* 1576 */       writeObject0(paramIOException, false);
/* 1577 */       clear();
/*      */     } finally {
/* 1579 */       this.bout.setBlockDataMode(bool);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static native void floatsToBytes(float[] paramArrayOfFloat, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3);
/*      */ 
/*      */   private static native void doublesToBytes(double[] paramArrayOfDouble, int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3);
/*      */ 
/*      */   private static class BlockDataOutputStream extends OutputStream
/*      */     implements DataOutput
/*      */   {
/*      */     private static final int MAX_BLOCK_SIZE = 1024;
/*      */     private static final int MAX_HEADER_SIZE = 5;
/*      */     private static final int CHAR_BUF_SIZE = 256;
/* 1749 */     private final byte[] buf = new byte[1024];
/*      */ 
/* 1751 */     private final byte[] hbuf = new byte[5];
/*      */ 
/* 1753 */     private final char[] cbuf = new char[256];
/*      */ 
/* 1756 */     private boolean blkmode = false;
/*      */ 
/* 1758 */     private int pos = 0;
/*      */     private final OutputStream out;
/*      */     private final DataOutputStream dout;
/*      */ 
/*      */     BlockDataOutputStream(OutputStream paramOutputStream)
/*      */     {
/* 1770 */       this.out = paramOutputStream;
/* 1771 */       this.dout = new DataOutputStream(this);
/*      */     }
/*      */ 
/*      */     boolean setBlockDataMode(boolean paramBoolean)
/*      */       throws IOException
/*      */     {
/* 1782 */       if (this.blkmode == paramBoolean) {
/* 1783 */         return this.blkmode;
/*      */       }
/* 1785 */       drain();
/* 1786 */       this.blkmode = paramBoolean;
/* 1787 */       return !this.blkmode;
/*      */     }
/*      */ 
/*      */     boolean getBlockDataMode()
/*      */     {
/* 1795 */       return this.blkmode;
/*      */     }
/*      */ 
/*      */     public void write(int paramInt)
/*      */       throws IOException
/*      */     {
/* 1806 */       if (this.pos >= 1024) {
/* 1807 */         drain();
/*      */       }
/* 1809 */       this.buf[(this.pos++)] = ((byte)paramInt);
/*      */     }
/*      */ 
/*      */     public void write(byte[] paramArrayOfByte) throws IOException {
/* 1813 */       write(paramArrayOfByte, 0, paramArrayOfByte.length, false);
/*      */     }
/*      */ 
/*      */     public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 1817 */       write(paramArrayOfByte, paramInt1, paramInt2, false);
/*      */     }
/*      */ 
/*      */     public void flush() throws IOException {
/* 1821 */       drain();
/* 1822 */       this.out.flush();
/*      */     }
/*      */ 
/*      */     public void close() throws IOException {
/* 1826 */       flush();
/* 1827 */       this.out.close();
/*      */     }
/*      */ 
/*      */     void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2, boolean paramBoolean)
/*      */       throws IOException
/*      */     {
/* 1839 */       if ((!paramBoolean) && (!this.blkmode)) {
/* 1840 */         drain();
/* 1841 */         this.out.write(paramArrayOfByte, paramInt1, paramInt2);
/* 1842 */         return;
/*      */       }
/*      */ 
/* 1845 */       while (paramInt2 > 0) {
/* 1846 */         if (this.pos >= 1024) {
/* 1847 */           drain();
/*      */         }
/* 1849 */         if ((paramInt2 >= 1024) && (!paramBoolean) && (this.pos == 0))
/*      */         {
/* 1851 */           writeBlockHeader(1024);
/* 1852 */           this.out.write(paramArrayOfByte, paramInt1, 1024);
/* 1853 */           paramInt1 += 1024;
/* 1854 */           paramInt2 -= 1024;
/*      */         } else {
/* 1856 */           int i = Math.min(paramInt2, 1024 - this.pos);
/* 1857 */           System.arraycopy(paramArrayOfByte, paramInt1, this.buf, this.pos, i);
/* 1858 */           this.pos += i;
/* 1859 */           paramInt1 += i;
/* 1860 */           paramInt2 -= i;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     void drain()
/*      */       throws IOException
/*      */     {
/* 1870 */       if (this.pos == 0) {
/* 1871 */         return;
/*      */       }
/* 1873 */       if (this.blkmode) {
/* 1874 */         writeBlockHeader(this.pos);
/*      */       }
/* 1876 */       this.out.write(this.buf, 0, this.pos);
/* 1877 */       this.pos = 0;
/*      */     }
/*      */ 
/*      */     private void writeBlockHeader(int paramInt)
/*      */       throws IOException
/*      */     {
/* 1886 */       if (paramInt <= 255) {
/* 1887 */         this.hbuf[0] = 119;
/* 1888 */         this.hbuf[1] = ((byte)paramInt);
/* 1889 */         this.out.write(this.hbuf, 0, 2);
/*      */       } else {
/* 1891 */         this.hbuf[0] = 122;
/* 1892 */         Bits.putInt(this.hbuf, 1, paramInt);
/* 1893 */         this.out.write(this.hbuf, 0, 5);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void writeBoolean(boolean paramBoolean)
/*      */       throws IOException
/*      */     {
/* 1906 */       if (this.pos >= 1024) {
/* 1907 */         drain();
/*      */       }
/* 1909 */       Bits.putBoolean(this.buf, this.pos++, paramBoolean);
/*      */     }
/*      */ 
/*      */     public void writeByte(int paramInt) throws IOException {
/* 1913 */       if (this.pos >= 1024) {
/* 1914 */         drain();
/*      */       }
/* 1916 */       this.buf[(this.pos++)] = ((byte)paramInt);
/*      */     }
/*      */ 
/*      */     public void writeChar(int paramInt) throws IOException {
/* 1920 */       if (this.pos + 2 <= 1024) {
/* 1921 */         Bits.putChar(this.buf, this.pos, (char)paramInt);
/* 1922 */         this.pos += 2;
/*      */       } else {
/* 1924 */         this.dout.writeChar(paramInt);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void writeShort(int paramInt) throws IOException {
/* 1929 */       if (this.pos + 2 <= 1024) {
/* 1930 */         Bits.putShort(this.buf, this.pos, (short)paramInt);
/* 1931 */         this.pos += 2;
/*      */       } else {
/* 1933 */         this.dout.writeShort(paramInt);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void writeInt(int paramInt) throws IOException {
/* 1938 */       if (this.pos + 4 <= 1024) {
/* 1939 */         Bits.putInt(this.buf, this.pos, paramInt);
/* 1940 */         this.pos += 4;
/*      */       } else {
/* 1942 */         this.dout.writeInt(paramInt);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void writeFloat(float paramFloat) throws IOException {
/* 1947 */       if (this.pos + 4 <= 1024) {
/* 1948 */         Bits.putFloat(this.buf, this.pos, paramFloat);
/* 1949 */         this.pos += 4;
/*      */       } else {
/* 1951 */         this.dout.writeFloat(paramFloat);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void writeLong(long paramLong) throws IOException {
/* 1956 */       if (this.pos + 8 <= 1024) {
/* 1957 */         Bits.putLong(this.buf, this.pos, paramLong);
/* 1958 */         this.pos += 8;
/*      */       } else {
/* 1960 */         this.dout.writeLong(paramLong);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void writeDouble(double paramDouble) throws IOException {
/* 1965 */       if (this.pos + 8 <= 1024) {
/* 1966 */         Bits.putDouble(this.buf, this.pos, paramDouble);
/* 1967 */         this.pos += 8;
/*      */       } else {
/* 1969 */         this.dout.writeDouble(paramDouble);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void writeBytes(String paramString) throws IOException {
/* 1974 */       int i = paramString.length();
/* 1975 */       int j = 0;
/* 1976 */       int k = 0;
/* 1977 */       for (int m = 0; m < i; ) {
/* 1978 */         if (j >= k) {
/* 1979 */           j = 0;
/* 1980 */           k = Math.min(i - m, 256);
/* 1981 */           paramString.getChars(m, m + k, this.cbuf, 0);
/*      */         }
/* 1983 */         if (this.pos >= 1024) {
/* 1984 */           drain();
/*      */         }
/* 1986 */         int n = Math.min(k - j, 1024 - this.pos);
/* 1987 */         int i1 = this.pos + n;
/* 1988 */         while (this.pos < i1) {
/* 1989 */           this.buf[(this.pos++)] = ((byte)this.cbuf[(j++)]);
/*      */         }
/* 1991 */         m += n;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void writeChars(String paramString) throws IOException {
/* 1996 */       int i = paramString.length();
/* 1997 */       for (int j = 0; j < i; ) {
/* 1998 */         int k = Math.min(i - j, 256);
/* 1999 */         paramString.getChars(j, j + k, this.cbuf, 0);
/* 2000 */         writeChars(this.cbuf, 0, k);
/* 2001 */         j += k;
/*      */       }
/*      */     }
/*      */ 
/*      */     public void writeUTF(String paramString) throws IOException {
/* 2006 */       writeUTF(paramString, getUTFLength(paramString));
/*      */     }
/*      */ 
/*      */     void writeBooleans(boolean[] paramArrayOfBoolean, int paramInt1, int paramInt2)
/*      */       throws IOException
/*      */     {
/* 2019 */       int i = paramInt1 + paramInt2;
/* 2020 */       while (paramInt1 < i) {
/* 2021 */         if (this.pos >= 1024) {
/* 2022 */           drain();
/*      */         }
/* 2024 */         int j = Math.min(i, paramInt1 + (1024 - this.pos));
/* 2025 */         while (paramInt1 < j)
/* 2026 */           Bits.putBoolean(this.buf, this.pos++, paramArrayOfBoolean[(paramInt1++)]);
/*      */       }
/*      */     }
/*      */ 
/*      */     void writeChars(char[] paramArrayOfChar, int paramInt1, int paramInt2) throws IOException
/*      */     {
/* 2032 */       int i = 1022;
/* 2033 */       int j = paramInt1 + paramInt2;
/* 2034 */       while (paramInt1 < j)
/* 2035 */         if (this.pos <= i) {
/* 2036 */           int k = 1024 - this.pos >> 1;
/* 2037 */           int m = Math.min(j, paramInt1 + k);
/* 2038 */           while (paramInt1 < m) {
/* 2039 */             Bits.putChar(this.buf, this.pos, paramArrayOfChar[(paramInt1++)]);
/* 2040 */             this.pos += 2;
/*      */           }
/*      */         } else {
/* 2043 */           this.dout.writeChar(paramArrayOfChar[(paramInt1++)]);
/*      */         }
/*      */     }
/*      */ 
/*      */     void writeShorts(short[] paramArrayOfShort, int paramInt1, int paramInt2) throws IOException
/*      */     {
/* 2049 */       int i = 1022;
/* 2050 */       int j = paramInt1 + paramInt2;
/* 2051 */       while (paramInt1 < j)
/* 2052 */         if (this.pos <= i) {
/* 2053 */           int k = 1024 - this.pos >> 1;
/* 2054 */           int m = Math.min(j, paramInt1 + k);
/* 2055 */           while (paramInt1 < m) {
/* 2056 */             Bits.putShort(this.buf, this.pos, paramArrayOfShort[(paramInt1++)]);
/* 2057 */             this.pos += 2;
/*      */           }
/*      */         } else {
/* 2060 */           this.dout.writeShort(paramArrayOfShort[(paramInt1++)]);
/*      */         }
/*      */     }
/*      */ 
/*      */     void writeInts(int[] paramArrayOfInt, int paramInt1, int paramInt2) throws IOException
/*      */     {
/* 2066 */       int i = 1020;
/* 2067 */       int j = paramInt1 + paramInt2;
/* 2068 */       while (paramInt1 < j)
/* 2069 */         if (this.pos <= i) {
/* 2070 */           int k = 1024 - this.pos >> 2;
/* 2071 */           int m = Math.min(j, paramInt1 + k);
/* 2072 */           while (paramInt1 < m) {
/* 2073 */             Bits.putInt(this.buf, this.pos, paramArrayOfInt[(paramInt1++)]);
/* 2074 */             this.pos += 4;
/*      */           }
/*      */         } else {
/* 2077 */           this.dout.writeInt(paramArrayOfInt[(paramInt1++)]);
/*      */         }
/*      */     }
/*      */ 
/*      */     void writeFloats(float[] paramArrayOfFloat, int paramInt1, int paramInt2) throws IOException
/*      */     {
/* 2083 */       int i = 1020;
/* 2084 */       int j = paramInt1 + paramInt2;
/* 2085 */       while (paramInt1 < j)
/* 2086 */         if (this.pos <= i) {
/* 2087 */           int k = 1024 - this.pos >> 2;
/* 2088 */           int m = Math.min(j - paramInt1, k);
/* 2089 */           ObjectOutputStream.floatsToBytes(paramArrayOfFloat, paramInt1, this.buf, this.pos, m);
/* 2090 */           paramInt1 += m;
/* 2091 */           this.pos += (m << 2);
/*      */         } else {
/* 2093 */           this.dout.writeFloat(paramArrayOfFloat[(paramInt1++)]);
/*      */         }
/*      */     }
/*      */ 
/*      */     void writeLongs(long[] paramArrayOfLong, int paramInt1, int paramInt2) throws IOException
/*      */     {
/* 2099 */       int i = 1016;
/* 2100 */       int j = paramInt1 + paramInt2;
/* 2101 */       while (paramInt1 < j)
/* 2102 */         if (this.pos <= i) {
/* 2103 */           int k = 1024 - this.pos >> 3;
/* 2104 */           int m = Math.min(j, paramInt1 + k);
/* 2105 */           while (paramInt1 < m) {
/* 2106 */             Bits.putLong(this.buf, this.pos, paramArrayOfLong[(paramInt1++)]);
/* 2107 */             this.pos += 8;
/*      */           }
/*      */         } else {
/* 2110 */           this.dout.writeLong(paramArrayOfLong[(paramInt1++)]);
/*      */         }
/*      */     }
/*      */ 
/*      */     void writeDoubles(double[] paramArrayOfDouble, int paramInt1, int paramInt2) throws IOException
/*      */     {
/* 2116 */       int i = 1016;
/* 2117 */       int j = paramInt1 + paramInt2;
/* 2118 */       while (paramInt1 < j)
/* 2119 */         if (this.pos <= i) {
/* 2120 */           int k = 1024 - this.pos >> 3;
/* 2121 */           int m = Math.min(j - paramInt1, k);
/* 2122 */           ObjectOutputStream.doublesToBytes(paramArrayOfDouble, paramInt1, this.buf, this.pos, m);
/* 2123 */           paramInt1 += m;
/* 2124 */           this.pos += (m << 3);
/*      */         } else {
/* 2126 */           this.dout.writeDouble(paramArrayOfDouble[(paramInt1++)]);
/*      */         }
/*      */     }
/*      */ 
/*      */     long getUTFLength(String paramString)
/*      */     {
/* 2135 */       int i = paramString.length();
/* 2136 */       long l = 0L;
/* 2137 */       for (int j = 0; j < i; ) {
/* 2138 */         int k = Math.min(i - j, 256);
/* 2139 */         paramString.getChars(j, j + k, this.cbuf, 0);
/* 2140 */         for (int m = 0; m < k; m++) {
/* 2141 */           int n = this.cbuf[m];
/* 2142 */           if ((n >= 1) && (n <= 127))
/* 2143 */             l += 1L;
/* 2144 */           else if (n > 2047)
/* 2145 */             l += 3L;
/*      */           else {
/* 2147 */             l += 2L;
/*      */           }
/*      */         }
/* 2150 */         j += k;
/*      */       }
/* 2152 */       return l;
/*      */     }
/*      */ 
/*      */     void writeUTF(String paramString, long paramLong)
/*      */       throws IOException
/*      */     {
/* 2162 */       if (paramLong > 65535L) {
/* 2163 */         throw new UTFDataFormatException();
/*      */       }
/* 2165 */       writeShort((int)paramLong);
/* 2166 */       if (paramLong == paramString.length())
/* 2167 */         writeBytes(paramString);
/*      */       else
/* 2169 */         writeUTFBody(paramString);
/*      */     }
/*      */ 
/*      */     void writeLongUTF(String paramString)
/*      */       throws IOException
/*      */     {
/* 2179 */       writeLongUTF(paramString, getUTFLength(paramString));
/*      */     }
/*      */ 
/*      */     void writeLongUTF(String paramString, long paramLong)
/*      */       throws IOException
/*      */     {
/* 2187 */       writeLong(paramLong);
/* 2188 */       if (paramLong == paramString.length())
/* 2189 */         writeBytes(paramString);
/*      */       else
/* 2191 */         writeUTFBody(paramString);
/*      */     }
/*      */ 
/*      */     private void writeUTFBody(String paramString)
/*      */       throws IOException
/*      */     {
/* 2200 */       int i = 1021;
/* 2201 */       int j = paramString.length();
/* 2202 */       for (int k = 0; k < j; ) {
/* 2203 */         int m = Math.min(j - k, 256);
/* 2204 */         paramString.getChars(k, k + m, this.cbuf, 0);
/* 2205 */         for (int n = 0; n < m; n++) {
/* 2206 */           int i1 = this.cbuf[n];
/* 2207 */           if (this.pos <= i) {
/* 2208 */             if ((i1 <= 127) && (i1 != 0)) {
/* 2209 */               this.buf[(this.pos++)] = ((byte)i1);
/* 2210 */             } else if (i1 > 2047) {
/* 2211 */               this.buf[(this.pos + 2)] = ((byte)(0x80 | i1 >> 0 & 0x3F));
/* 2212 */               this.buf[(this.pos + 1)] = ((byte)(0x80 | i1 >> 6 & 0x3F));
/* 2213 */               this.buf[(this.pos + 0)] = ((byte)(0xE0 | i1 >> 12 & 0xF));
/* 2214 */               this.pos += 3;
/*      */             } else {
/* 2216 */               this.buf[(this.pos + 1)] = ((byte)(0x80 | i1 >> 0 & 0x3F));
/* 2217 */               this.buf[(this.pos + 0)] = ((byte)(0xC0 | i1 >> 6 & 0x1F));
/* 2218 */               this.pos += 2;
/*      */             }
/*      */           }
/* 2221 */           else if ((i1 <= 127) && (i1 != 0)) {
/* 2222 */             write(i1);
/* 2223 */           } else if (i1 > 2047) {
/* 2224 */             write(0xE0 | i1 >> 12 & 0xF);
/* 2225 */             write(0x80 | i1 >> 6 & 0x3F);
/* 2226 */             write(0x80 | i1 >> 0 & 0x3F);
/*      */           } else {
/* 2228 */             write(0xC0 | i1 >> 6 & 0x1F);
/* 2229 */             write(0x80 | i1 >> 0 & 0x3F);
/*      */           }
/*      */         }
/*      */ 
/* 2233 */         k += m;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Caches
/*      */   {
/*  168 */     static final ConcurrentMap<ObjectStreamClass.WeakClassKey, Boolean> subclassAudits = new ConcurrentHashMap();
/*      */ 
/*  172 */     static final ReferenceQueue<Class<?>> subclassAuditsQueue = new ReferenceQueue();
/*      */   }
/*      */ 
/*      */   private static class DebugTraceInfoStack
/*      */   {
/*      */     private final List<String> stack;
/*      */ 
/*      */     DebugTraceInfoStack()
/*      */     {
/* 2435 */       this.stack = new ArrayList();
/*      */     }
/*      */ 
/*      */     void clear()
/*      */     {
/* 2442 */       this.stack.clear();
/*      */     }
/*      */ 
/*      */     void pop()
/*      */     {
/* 2449 */       this.stack.remove(this.stack.size() - 1);
/*      */     }
/*      */ 
/*      */     void push(String paramString)
/*      */     {
/* 2456 */       this.stack.add("\t- " + paramString);
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 2463 */       StringBuilder localStringBuilder = new StringBuilder();
/* 2464 */       if (!this.stack.isEmpty()) {
/* 2465 */         for (int i = this.stack.size(); i > 0; i--) {
/* 2466 */           localStringBuilder.append((String)this.stack.get(i - 1) + (i != 1 ? "\n" : ""));
/*      */         }
/*      */       }
/* 2469 */       return localStringBuilder.toString();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class HandleTable
/*      */   {
/*      */     private int size;
/*      */     private int threshold;
/*      */     private final float loadFactor;
/*      */     private int[] spine;
/*      */     private int[] next;
/*      */     private Object[] objs;
/*      */ 
/*      */     HandleTable(int paramInt, float paramFloat)
/*      */     {
/* 2261 */       this.loadFactor = paramFloat;
/* 2262 */       this.spine = new int[paramInt];
/* 2263 */       this.next = new int[paramInt];
/* 2264 */       this.objs = new Object[paramInt];
/* 2265 */       this.threshold = ((int)(paramInt * paramFloat));
/* 2266 */       clear();
/*      */     }
/*      */ 
/*      */     int assign(Object paramObject)
/*      */     {
/* 2274 */       if (this.size >= this.next.length) {
/* 2275 */         growEntries();
/*      */       }
/* 2277 */       if (this.size >= this.threshold) {
/* 2278 */         growSpine();
/*      */       }
/* 2280 */       insert(paramObject, this.size);
/* 2281 */       return this.size++;
/*      */     }
/*      */ 
/*      */     int lookup(Object paramObject)
/*      */     {
/* 2289 */       if (this.size == 0) {
/* 2290 */         return -1;
/*      */       }
/* 2292 */       int i = hash(paramObject) % this.spine.length;
/* 2293 */       for (int j = this.spine[i]; j >= 0; j = this.next[j]) {
/* 2294 */         if (this.objs[j] == paramObject) {
/* 2295 */           return j;
/*      */         }
/*      */       }
/* 2298 */       return -1;
/*      */     }
/*      */ 
/*      */     void clear()
/*      */     {
/* 2305 */       Arrays.fill(this.spine, -1);
/* 2306 */       Arrays.fill(this.objs, 0, this.size, null);
/* 2307 */       this.size = 0;
/*      */     }
/*      */ 
/*      */     int size()
/*      */     {
/* 2314 */       return this.size;
/*      */     }
/*      */ 
/*      */     private void insert(Object paramObject, int paramInt)
/*      */     {
/* 2322 */       int i = hash(paramObject) % this.spine.length;
/* 2323 */       this.objs[paramInt] = paramObject;
/* 2324 */       this.next[paramInt] = this.spine[i];
/* 2325 */       this.spine[i] = paramInt;
/*      */     }
/*      */ 
/*      */     private void growSpine()
/*      */     {
/* 2333 */       this.spine = new int[(this.spine.length << 1) + 1];
/* 2334 */       this.threshold = ((int)(this.spine.length * this.loadFactor));
/* 2335 */       Arrays.fill(this.spine, -1);
/* 2336 */       for (int i = 0; i < this.size; i++)
/* 2337 */         insert(this.objs[i], i);
/*      */     }
/*      */ 
/*      */     private void growEntries()
/*      */     {
/* 2345 */       int i = (this.next.length << 1) + 1;
/* 2346 */       int[] arrayOfInt = new int[i];
/* 2347 */       System.arraycopy(this.next, 0, arrayOfInt, 0, this.size);
/* 2348 */       this.next = arrayOfInt;
/*      */ 
/* 2350 */       Object[] arrayOfObject = new Object[i];
/* 2351 */       System.arraycopy(this.objs, 0, arrayOfObject, 0, this.size);
/* 2352 */       this.objs = arrayOfObject;
/*      */     }
/*      */ 
/*      */     private int hash(Object paramObject)
/*      */     {
/* 2359 */       return System.identityHashCode(paramObject) & 0x7FFFFFFF;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static abstract class PutField
/*      */   {
/*      */     public abstract void put(String paramString, boolean paramBoolean);
/*      */ 
/*      */     public abstract void put(String paramString, byte paramByte);
/*      */ 
/*      */     public abstract void put(String paramString, char paramChar);
/*      */ 
/*      */     public abstract void put(String paramString, short paramShort);
/*      */ 
/*      */     public abstract void put(String paramString, int paramInt);
/*      */ 
/*      */     public abstract void put(String paramString, long paramLong);
/*      */ 
/*      */     public abstract void put(String paramString, float paramFloat);
/*      */ 
/*      */     public abstract void put(String paramString, double paramDouble);
/*      */ 
/*      */     public abstract void put(String paramString, Object paramObject);
/*      */ 
/*      */     @Deprecated
/*      */     public abstract void write(ObjectOutput paramObjectOutput)
/*      */       throws IOException;
/*      */   }
/*      */ 
/*      */   private class PutFieldImpl extends ObjectOutputStream.PutField
/*      */   {
/*      */     private final ObjectStreamClass desc;
/*      */     private final byte[] primVals;
/*      */     private final Object[] objVals;
/*      */ 
/*      */     PutFieldImpl(ObjectStreamClass arg2)
/*      */     {
/*      */       Object localObject;
/* 1616 */       this.desc = localObject;
/* 1617 */       this.primVals = new byte[localObject.getPrimDataSize()];
/* 1618 */       this.objVals = new Object[localObject.getNumObjFields()];
/*      */     }
/*      */ 
/*      */     public void put(String paramString, boolean paramBoolean) {
/* 1622 */       Bits.putBoolean(this.primVals, getFieldOffset(paramString, Boolean.TYPE), paramBoolean);
/*      */     }
/*      */ 
/*      */     public void put(String paramString, byte paramByte) {
/* 1626 */       this.primVals[getFieldOffset(paramString, Byte.TYPE)] = paramByte;
/*      */     }
/*      */ 
/*      */     public void put(String paramString, char paramChar) {
/* 1630 */       Bits.putChar(this.primVals, getFieldOffset(paramString, Character.TYPE), paramChar);
/*      */     }
/*      */ 
/*      */     public void put(String paramString, short paramShort) {
/* 1634 */       Bits.putShort(this.primVals, getFieldOffset(paramString, Short.TYPE), paramShort);
/*      */     }
/*      */ 
/*      */     public void put(String paramString, int paramInt) {
/* 1638 */       Bits.putInt(this.primVals, getFieldOffset(paramString, Integer.TYPE), paramInt);
/*      */     }
/*      */ 
/*      */     public void put(String paramString, float paramFloat) {
/* 1642 */       Bits.putFloat(this.primVals, getFieldOffset(paramString, Float.TYPE), paramFloat);
/*      */     }
/*      */ 
/*      */     public void put(String paramString, long paramLong) {
/* 1646 */       Bits.putLong(this.primVals, getFieldOffset(paramString, Long.TYPE), paramLong);
/*      */     }
/*      */ 
/*      */     public void put(String paramString, double paramDouble) {
/* 1650 */       Bits.putDouble(this.primVals, getFieldOffset(paramString, Double.TYPE), paramDouble);
/*      */     }
/*      */ 
/*      */     public void put(String paramString, Object paramObject) {
/* 1654 */       this.objVals[getFieldOffset(paramString, Object.class)] = paramObject;
/*      */     }
/*      */ 
/*      */     public void write(ObjectOutput paramObjectOutput)
/*      */       throws IOException
/*      */     {
/* 1674 */       if (ObjectOutputStream.this != paramObjectOutput) {
/* 1675 */         throw new IllegalArgumentException("wrong stream");
/*      */       }
/* 1677 */       paramObjectOutput.write(this.primVals, 0, this.primVals.length);
/*      */ 
/* 1679 */       ObjectStreamField[] arrayOfObjectStreamField = this.desc.getFields(false);
/* 1680 */       int i = arrayOfObjectStreamField.length - this.objVals.length;
/*      */ 
/* 1682 */       for (int j = 0; j < this.objVals.length; j++) {
/* 1683 */         if (arrayOfObjectStreamField[(i + j)].isUnshared()) {
/* 1684 */           throw new IOException("cannot write unshared object");
/*      */         }
/* 1686 */         paramObjectOutput.writeObject(this.objVals[j]);
/*      */       }
/*      */     }
/*      */ 
/*      */     void writeFields()
/*      */       throws IOException
/*      */     {
/* 1694 */       ObjectOutputStream.this.bout.write(this.primVals, 0, this.primVals.length, false);
/*      */ 
/* 1696 */       ObjectStreamField[] arrayOfObjectStreamField = this.desc.getFields(false);
/* 1697 */       int i = arrayOfObjectStreamField.length - this.objVals.length;
/* 1698 */       for (int j = 0; j < this.objVals.length; j++) {
/* 1699 */         if (ObjectOutputStream.extendedDebugInfo) {
/* 1700 */           ObjectOutputStream.this.debugInfoStack.push("field (class \"" + this.desc.getName() + "\", name: \"" + arrayOfObjectStreamField[(i + j)].getName() + "\", type: \"" + arrayOfObjectStreamField[(i + j)].getType() + "\")");
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/* 1706 */           ObjectOutputStream.this.writeObject0(this.objVals[j], arrayOfObjectStreamField[(i + j)].isUnshared());
/*      */         }
/*      */         finally {
/* 1709 */           if (ObjectOutputStream.extendedDebugInfo)
/* 1710 */             ObjectOutputStream.this.debugInfoStack.pop();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private int getFieldOffset(String paramString, Class paramClass)
/*      */     {
/* 1723 */       ObjectStreamField localObjectStreamField = this.desc.getField(paramString, paramClass);
/* 1724 */       if (localObjectStreamField == null) {
/* 1725 */         throw new IllegalArgumentException("no such field " + paramString + " with type " + paramClass);
/*      */       }
/*      */ 
/* 1728 */       return localObjectStreamField.getOffset();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ReplaceTable
/*      */   {
/*      */     private final ObjectOutputStream.HandleTable htab;
/*      */     private Object[] reps;
/*      */ 
/*      */     ReplaceTable(int paramInt, float paramFloat)
/*      */     {
/* 2378 */       this.htab = new ObjectOutputStream.HandleTable(paramInt, paramFloat);
/* 2379 */       this.reps = new Object[paramInt];
/*      */     }
/*      */ 
/*      */     void assign(Object paramObject1, Object paramObject2)
/*      */     {
/* 2386 */       int i = this.htab.assign(paramObject1);
/* 2387 */       while (i >= this.reps.length) {
/* 2388 */         grow();
/*      */       }
/* 2390 */       this.reps[i] = paramObject2;
/*      */     }
/*      */ 
/*      */     Object lookup(Object paramObject)
/*      */     {
/* 2398 */       int i = this.htab.lookup(paramObject);
/* 2399 */       return i >= 0 ? this.reps[i] : paramObject;
/*      */     }
/*      */ 
/*      */     void clear()
/*      */     {
/* 2406 */       Arrays.fill(this.reps, 0, this.htab.size(), null);
/* 2407 */       this.htab.clear();
/*      */     }
/*      */ 
/*      */     int size()
/*      */     {
/* 2414 */       return this.htab.size();
/*      */     }
/*      */ 
/*      */     private void grow()
/*      */     {
/* 2421 */       Object[] arrayOfObject = new Object[(this.reps.length << 1) + 1];
/* 2422 */       System.arraycopy(this.reps, 0, arrayOfObject, 0, this.reps.length);
/* 2423 */       this.reps = arrayOfObject;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.ObjectOutputStream
 * JD-Core Version:    0.6.2
 */