/*      */ package sun.misc;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.DataOutputStream;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.OutputStream;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Method;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.ListIterator;
/*      */ import java.util.Map;
/*      */ import sun.security.action.GetBooleanAction;
/*      */ 
/*      */ public class ProxyGenerator
/*      */ {
/*      */   private static final int CLASSFILE_MAJOR_VERSION = 49;
/*      */   private static final int CLASSFILE_MINOR_VERSION = 0;
/*      */   private static final int CONSTANT_UTF8 = 1;
/*      */   private static final int CONSTANT_UNICODE = 2;
/*      */   private static final int CONSTANT_INTEGER = 3;
/*      */   private static final int CONSTANT_FLOAT = 4;
/*      */   private static final int CONSTANT_LONG = 5;
/*      */   private static final int CONSTANT_DOUBLE = 6;
/*      */   private static final int CONSTANT_CLASS = 7;
/*      */   private static final int CONSTANT_STRING = 8;
/*      */   private static final int CONSTANT_FIELD = 9;
/*      */   private static final int CONSTANT_METHOD = 10;
/*      */   private static final int CONSTANT_INTERFACEMETHOD = 11;
/*      */   private static final int CONSTANT_NAMEANDTYPE = 12;
/*      */   private static final int ACC_PUBLIC = 1;
/*      */   private static final int ACC_PRIVATE = 2;
/*      */   private static final int ACC_STATIC = 8;
/*      */   private static final int ACC_FINAL = 16;
/*      */   private static final int ACC_SUPER = 32;
/*      */   private static final int opc_aconst_null = 1;
/*      */   private static final int opc_iconst_0 = 3;
/*      */   private static final int opc_bipush = 16;
/*      */   private static final int opc_sipush = 17;
/*      */   private static final int opc_ldc = 18;
/*      */   private static final int opc_ldc_w = 19;
/*      */   private static final int opc_iload = 21;
/*      */   private static final int opc_lload = 22;
/*      */   private static final int opc_fload = 23;
/*      */   private static final int opc_dload = 24;
/*      */   private static final int opc_aload = 25;
/*      */   private static final int opc_iload_0 = 26;
/*      */   private static final int opc_lload_0 = 30;
/*      */   private static final int opc_fload_0 = 34;
/*      */   private static final int opc_dload_0 = 38;
/*      */   private static final int opc_aload_0 = 42;
/*      */   private static final int opc_astore = 58;
/*      */   private static final int opc_astore_0 = 75;
/*      */   private static final int opc_aastore = 83;
/*      */   private static final int opc_pop = 87;
/*      */   private static final int opc_dup = 89;
/*      */   private static final int opc_ireturn = 172;
/*      */   private static final int opc_lreturn = 173;
/*      */   private static final int opc_freturn = 174;
/*      */   private static final int opc_dreturn = 175;
/*      */   private static final int opc_areturn = 176;
/*      */   private static final int opc_return = 177;
/*      */   private static final int opc_getstatic = 178;
/*      */   private static final int opc_putstatic = 179;
/*      */   private static final int opc_getfield = 180;
/*      */   private static final int opc_invokevirtual = 182;
/*      */   private static final int opc_invokespecial = 183;
/*      */   private static final int opc_invokestatic = 184;
/*      */   private static final int opc_invokeinterface = 185;
/*      */   private static final int opc_new = 187;
/*      */   private static final int opc_anewarray = 189;
/*      */   private static final int opc_athrow = 191;
/*      */   private static final int opc_checkcast = 192;
/*      */   private static final int opc_wide = 196;
/*      */   private static final String superclassName = "java/lang/reflect/Proxy";
/*      */   private static final String handlerFieldName = "h";
/*      */   private static final boolean saveGeneratedFiles;
/*      */   private static Method hashCodeMethod;
/*      */   private static Method equalsMethod;
/*      */   private static Method toStringMethod;
/*      */   private String className;
/*      */   private Class[] interfaces;
/*  368 */   private ConstantPool cp = new ConstantPool(null);
/*      */ 
/*  371 */   private List<FieldInfo> fields = new ArrayList();
/*      */ 
/*  374 */   private List<MethodInfo> methods = new ArrayList();
/*      */ 
/*  380 */   private Map<String, List<ProxyMethod>> proxyMethods = new HashMap();
/*      */ 
/*  384 */   private int proxyMethodCount = 0;
/*      */ 
/*      */   public static byte[] generateProxyClass(String paramString, Class[] paramArrayOfClass)
/*      */   {
/*  322 */     ProxyGenerator localProxyGenerator = new ProxyGenerator(paramString, paramArrayOfClass);
/*  323 */     final byte[] arrayOfByte = localProxyGenerator.generateClassFile();
/*      */ 
/*  325 */     if (saveGeneratedFiles) {
/*  326 */       AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public Void run() {
/*      */           try {
/*  330 */             FileOutputStream localFileOutputStream = new FileOutputStream(ProxyGenerator.dotToSlash(this.val$name) + ".class");
/*      */ 
/*  332 */             localFileOutputStream.write(arrayOfByte);
/*  333 */             localFileOutputStream.close();
/*  334 */             return null;
/*      */           } catch (IOException localIOException) {
/*  336 */             throw new InternalError("I/O exception saving generated file: " + localIOException);
/*      */           }
/*      */         }
/*      */ 
/*      */       });
/*      */     }
/*      */ 
/*  343 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   private ProxyGenerator(String paramString, Class[] paramArrayOfClass)
/*      */   {
/*  394 */     this.className = paramString;
/*  395 */     this.interfaces = paramArrayOfClass;
/*      */   }
/*      */ 
/*      */   private byte[] generateClassFile()
/*      */   {
/*  416 */     addProxyMethod(hashCodeMethod, Object.class);
/*  417 */     addProxyMethod(equalsMethod, Object.class);
/*  418 */     addProxyMethod(toStringMethod, Object.class);
/*      */ 
/*  425 */     for (int i = 0; i < this.interfaces.length; i++) {
/*  426 */       localObject1 = this.interfaces[i].getMethods();
/*  427 */       for (int k = 0; k < localObject1.length; k++) {
/*  428 */         addProxyMethod(localObject1[k], this.interfaces[i]);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  436 */     for (Iterator localIterator1 = this.proxyMethods.values().iterator(); localIterator1.hasNext(); ) { localObject1 = (List)localIterator1.next();
/*  437 */       checkReturnTypes((List)localObject1);
/*      */     }
/*      */ 
/*      */     Object localObject2;
/*      */     try
/*      */     {
/*  445 */       this.methods.add(generateConstructor());
/*      */ 
/*  447 */       for (localIterator1 = this.proxyMethods.values().iterator(); localIterator1.hasNext(); ) { localObject1 = (List)localIterator1.next();
/*  448 */         for (localIterator2 = ((List)localObject1).iterator(); localIterator2.hasNext(); ) { localObject2 = (ProxyMethod)localIterator2.next();
/*      */ 
/*  451 */           this.fields.add(new FieldInfo(((ProxyMethod)localObject2).methodFieldName, "Ljava/lang/reflect/Method;", 10));
/*      */ 
/*  456 */           this.methods.add(((ProxyMethod)localObject2).generateMethod());
/*      */         }
/*      */       }
/*      */       Iterator localIterator2;
/*  460 */       this.methods.add(generateStaticInitializer());
/*      */     }
/*      */     catch (IOException localIOException1) {
/*  463 */       throw new InternalError("unexpected I/O Exception");
/*      */     }
/*      */ 
/*  466 */     if (this.methods.size() > 65535) {
/*  467 */       throw new IllegalArgumentException("method limit exceeded");
/*      */     }
/*  469 */     if (this.fields.size() > 65535) {
/*  470 */       throw new IllegalArgumentException("field limit exceeded");
/*      */     }
/*      */ 
/*  481 */     this.cp.getClass(dotToSlash(this.className));
/*  482 */     this.cp.getClass("java/lang/reflect/Proxy");
/*  483 */     for (int j = 0; j < this.interfaces.length; j++) {
/*  484 */       this.cp.getClass(dotToSlash(this.interfaces[j].getName()));
/*      */     }
/*      */ 
/*  491 */     this.cp.setReadOnly();
/*      */ 
/*  493 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/*  494 */     Object localObject1 = new DataOutputStream(localByteArrayOutputStream);
/*      */     try
/*      */     {
/*  502 */       ((DataOutputStream)localObject1).writeInt(-889275714);
/*      */ 
/*  504 */       ((DataOutputStream)localObject1).writeShort(0);
/*      */ 
/*  506 */       ((DataOutputStream)localObject1).writeShort(49);
/*      */ 
/*  508 */       this.cp.write((OutputStream)localObject1);
/*      */ 
/*  511 */       ((DataOutputStream)localObject1).writeShort(49);
/*      */ 
/*  513 */       ((DataOutputStream)localObject1).writeShort(this.cp.getClass(dotToSlash(this.className)));
/*      */ 
/*  515 */       ((DataOutputStream)localObject1).writeShort(this.cp.getClass("java/lang/reflect/Proxy"));
/*      */ 
/*  518 */       ((DataOutputStream)localObject1).writeShort(this.interfaces.length);
/*      */ 
/*  520 */       for (int m = 0; m < this.interfaces.length; m++) {
/*  521 */         ((DataOutputStream)localObject1).writeShort(this.cp.getClass(dotToSlash(this.interfaces[m].getName())));
/*      */       }
/*      */ 
/*  526 */       ((DataOutputStream)localObject1).writeShort(this.fields.size());
/*      */ 
/*  528 */       for (Iterator localIterator3 = this.fields.iterator(); localIterator3.hasNext(); ) { localObject2 = (FieldInfo)localIterator3.next();
/*  529 */         ((FieldInfo)localObject2).write((DataOutputStream)localObject1);
/*      */       }
/*      */ 
/*  533 */       ((DataOutputStream)localObject1).writeShort(this.methods.size());
/*      */ 
/*  535 */       for (localIterator3 = this.methods.iterator(); localIterator3.hasNext(); ) { localObject2 = (MethodInfo)localIterator3.next();
/*  536 */         ((MethodInfo)localObject2).write((DataOutputStream)localObject1);
/*      */       }
/*      */ 
/*  540 */       ((DataOutputStream)localObject1).writeShort(0);
/*      */     }
/*      */     catch (IOException localIOException2) {
/*  543 */       throw new InternalError("unexpected I/O Exception");
/*      */     }
/*      */ 
/*  546 */     return localByteArrayOutputStream.toByteArray();
/*      */   }
/*      */ 
/*      */   private void addProxyMethod(Method paramMethod, Class paramClass)
/*      */   {
/*  563 */     String str1 = paramMethod.getName();
/*  564 */     Class[] arrayOfClass1 = paramMethod.getParameterTypes();
/*  565 */     Class localClass = paramMethod.getReturnType();
/*  566 */     Class[] arrayOfClass2 = paramMethod.getExceptionTypes();
/*      */ 
/*  568 */     String str2 = str1 + getParameterDescriptors(arrayOfClass1);
/*  569 */     Object localObject = (List)this.proxyMethods.get(str2);
/*  570 */     if (localObject != null) {
/*  571 */       for (ProxyMethod localProxyMethod : (List)localObject)
/*  572 */         if (localClass == localProxyMethod.returnType)
/*      */         {
/*  579 */           ArrayList localArrayList = new ArrayList();
/*  580 */           collectCompatibleTypes(arrayOfClass2, localProxyMethod.exceptionTypes, localArrayList);
/*      */ 
/*  582 */           collectCompatibleTypes(localProxyMethod.exceptionTypes, arrayOfClass2, localArrayList);
/*      */ 
/*  584 */           localProxyMethod.exceptionTypes = new Class[localArrayList.size()];
/*  585 */           localProxyMethod.exceptionTypes = ((Class[])localArrayList.toArray(localProxyMethod.exceptionTypes));
/*      */ 
/*  587 */           return;
/*      */         }
/*      */     }
/*      */     else {
/*  591 */       localObject = new ArrayList(3);
/*  592 */       this.proxyMethods.put(str2, localObject);
/*      */     }
/*  594 */     ((List)localObject).add(new ProxyMethod(str1, arrayOfClass1, localClass, arrayOfClass2, paramClass, null));
/*      */   }
/*      */ 
/*      */   private static void checkReturnTypes(List<ProxyMethod> paramList)
/*      */   {
/*  613 */     if (paramList.size() < 2) {
/*  614 */       return;
/*      */     }
/*      */ 
/*  621 */     LinkedList localLinkedList = new LinkedList();
/*      */ 
/*  624 */     for (Object localObject = paramList.iterator(); ((Iterator)localObject).hasNext(); ) { ProxyMethod localProxyMethod = (ProxyMethod)((Iterator)localObject).next();
/*  625 */       Class localClass1 = localProxyMethod.returnType;
/*  626 */       if (localClass1.isPrimitive()) {
/*  627 */         throw new IllegalArgumentException("methods with same signature " + getFriendlyMethodSignature(localProxyMethod.methodName, localProxyMethod.parameterTypes) + " but incompatible return types: " + localClass1.getName() + " and others");
/*      */       }
/*      */ 
/*  634 */       int i = 0;
/*      */ 
/*  640 */       ListIterator localListIterator = localLinkedList.listIterator();
/*      */       while (true) { if (!localListIterator.hasNext()) break label214;
/*  642 */         Class localClass2 = (Class)localListIterator.next();
/*      */ 
/*  648 */         if (localClass1.isAssignableFrom(localClass2)) {
/*  649 */           if (($assertionsDisabled) || (i == 0)) break; throw new AssertionError();
/*      */         }
/*      */ 
/*  659 */         if (localClass2.isAssignableFrom(localClass1))
/*      */         {
/*  661 */           if (i == 0) {
/*  662 */             localListIterator.set(localClass1);
/*  663 */             i = 1;
/*      */           } else {
/*  665 */             localListIterator.remove();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  675 */       if (i == 0) {
/*  676 */         localLinkedList.add(localClass1);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  684 */     label214: if (localLinkedList.size() > 1) {
/*  685 */       localObject = (ProxyMethod)paramList.get(0);
/*  686 */       throw new IllegalArgumentException("methods with same signature " + getFriendlyMethodSignature(((ProxyMethod)localObject).methodName, ((ProxyMethod)localObject).parameterTypes) + " but incompatible return types: " + localLinkedList);
/*      */     }
/*      */   }
/*      */ 
/*      */   private MethodInfo generateConstructor()
/*      */     throws IOException
/*      */   {
/* 1141 */     MethodInfo localMethodInfo = new MethodInfo("<init>", "(Ljava/lang/reflect/InvocationHandler;)V", 1);
/*      */ 
/* 1145 */     DataOutputStream localDataOutputStream = new DataOutputStream(localMethodInfo.code);
/*      */ 
/* 1147 */     code_aload(0, localDataOutputStream);
/*      */ 
/* 1149 */     code_aload(1, localDataOutputStream);
/*      */ 
/* 1151 */     localDataOutputStream.writeByte(183);
/* 1152 */     localDataOutputStream.writeShort(this.cp.getMethodRef("java/lang/reflect/Proxy", "<init>", "(Ljava/lang/reflect/InvocationHandler;)V"));
/*      */ 
/* 1156 */     localDataOutputStream.writeByte(177);
/*      */ 
/* 1158 */     localMethodInfo.maxStack = 10;
/* 1159 */     localMethodInfo.maxLocals = 2;
/* 1160 */     localMethodInfo.declaredExceptions = new short[0];
/*      */ 
/* 1162 */     return localMethodInfo;
/*      */   }
/*      */ 
/*      */   private MethodInfo generateStaticInitializer()
/*      */     throws IOException
/*      */   {
/* 1169 */     MethodInfo localMethodInfo = new MethodInfo("<clinit>", "()V", 8);
/*      */ 
/* 1172 */     int i = 1;
/* 1173 */     short s2 = 0;
/*      */ 
/* 1175 */     DataOutputStream localDataOutputStream = new DataOutputStream(localMethodInfo.code);
/*      */ 
/* 1177 */     for (List localList : this.proxyMethods.values()) {
/* 1178 */       for (ProxyMethod localProxyMethod : localList) {
/* 1179 */         localProxyMethod.codeFieldInitialization(localDataOutputStream);
/*      */       }
/*      */     }
/*      */ 
/* 1183 */     localDataOutputStream.writeByte(177);
/*      */ 
/* 1185 */     short s3 = s1 = (short)localMethodInfo.code.size();
/*      */ 
/* 1187 */     localMethodInfo.exceptionTable.add(new ExceptionTableEntry(s2, s3, s1, this.cp.getClass("java/lang/NoSuchMethodException")));
/*      */ 
/* 1191 */     code_astore(i, localDataOutputStream);
/*      */ 
/* 1193 */     localDataOutputStream.writeByte(187);
/* 1194 */     localDataOutputStream.writeShort(this.cp.getClass("java/lang/NoSuchMethodError"));
/*      */ 
/* 1196 */     localDataOutputStream.writeByte(89);
/*      */ 
/* 1198 */     code_aload(i, localDataOutputStream);
/*      */ 
/* 1200 */     localDataOutputStream.writeByte(182);
/* 1201 */     localDataOutputStream.writeShort(this.cp.getMethodRef("java/lang/Throwable", "getMessage", "()Ljava/lang/String;"));
/*      */ 
/* 1204 */     localDataOutputStream.writeByte(183);
/* 1205 */     localDataOutputStream.writeShort(this.cp.getMethodRef("java/lang/NoSuchMethodError", "<init>", "(Ljava/lang/String;)V"));
/*      */ 
/* 1208 */     localDataOutputStream.writeByte(191);
/*      */ 
/* 1210 */     short s1 = (short)localMethodInfo.code.size();
/*      */ 
/* 1212 */     localMethodInfo.exceptionTable.add(new ExceptionTableEntry(s2, s3, s1, this.cp.getClass("java/lang/ClassNotFoundException")));
/*      */ 
/* 1216 */     code_astore(i, localDataOutputStream);
/*      */ 
/* 1218 */     localDataOutputStream.writeByte(187);
/* 1219 */     localDataOutputStream.writeShort(this.cp.getClass("java/lang/NoClassDefFoundError"));
/*      */ 
/* 1221 */     localDataOutputStream.writeByte(89);
/*      */ 
/* 1223 */     code_aload(i, localDataOutputStream);
/*      */ 
/* 1225 */     localDataOutputStream.writeByte(182);
/* 1226 */     localDataOutputStream.writeShort(this.cp.getMethodRef("java/lang/Throwable", "getMessage", "()Ljava/lang/String;"));
/*      */ 
/* 1229 */     localDataOutputStream.writeByte(183);
/* 1230 */     localDataOutputStream.writeShort(this.cp.getMethodRef("java/lang/NoClassDefFoundError", "<init>", "(Ljava/lang/String;)V"));
/*      */ 
/* 1234 */     localDataOutputStream.writeByte(191);
/*      */ 
/* 1236 */     if (localMethodInfo.code.size() > 65535) {
/* 1237 */       throw new IllegalArgumentException("code size limit exceeded");
/*      */     }
/*      */ 
/* 1240 */     localMethodInfo.maxStack = 10;
/* 1241 */     localMethodInfo.maxLocals = ((short)(i + 1));
/* 1242 */     localMethodInfo.declaredExceptions = new short[0];
/*      */ 
/* 1244 */     return localMethodInfo;
/*      */   }
/*      */ 
/*      */   private void code_iload(int paramInt, DataOutputStream paramDataOutputStream)
/*      */     throws IOException
/*      */   {
/* 1261 */     codeLocalLoadStore(paramInt, 21, 26, paramDataOutputStream);
/*      */   }
/*      */ 
/*      */   private void code_lload(int paramInt, DataOutputStream paramDataOutputStream)
/*      */     throws IOException
/*      */   {
/* 1267 */     codeLocalLoadStore(paramInt, 22, 30, paramDataOutputStream);
/*      */   }
/*      */ 
/*      */   private void code_fload(int paramInt, DataOutputStream paramDataOutputStream)
/*      */     throws IOException
/*      */   {
/* 1273 */     codeLocalLoadStore(paramInt, 23, 34, paramDataOutputStream);
/*      */   }
/*      */ 
/*      */   private void code_dload(int paramInt, DataOutputStream paramDataOutputStream)
/*      */     throws IOException
/*      */   {
/* 1279 */     codeLocalLoadStore(paramInt, 24, 38, paramDataOutputStream);
/*      */   }
/*      */ 
/*      */   private void code_aload(int paramInt, DataOutputStream paramDataOutputStream)
/*      */     throws IOException
/*      */   {
/* 1285 */     codeLocalLoadStore(paramInt, 25, 42, paramDataOutputStream);
/*      */   }
/*      */ 
/*      */   private void code_astore(int paramInt, DataOutputStream paramDataOutputStream)
/*      */     throws IOException
/*      */   {
/* 1315 */     codeLocalLoadStore(paramInt, 58, 75, paramDataOutputStream);
/*      */   }
/*      */ 
/*      */   private void codeLocalLoadStore(int paramInt1, int paramInt2, int paramInt3, DataOutputStream paramDataOutputStream)
/*      */     throws IOException
/*      */   {
/* 1331 */     assert ((paramInt1 >= 0) && (paramInt1 <= 65535));
/* 1332 */     if (paramInt1 <= 3) {
/* 1333 */       paramDataOutputStream.writeByte(paramInt3 + paramInt1);
/* 1334 */     } else if (paramInt1 <= 255) {
/* 1335 */       paramDataOutputStream.writeByte(paramInt2);
/* 1336 */       paramDataOutputStream.writeByte(paramInt1 & 0xFF);
/*      */     }
/*      */     else
/*      */     {
/* 1342 */       paramDataOutputStream.writeByte(196);
/* 1343 */       paramDataOutputStream.writeByte(paramInt2);
/* 1344 */       paramDataOutputStream.writeShort(paramInt1 & 0xFFFF);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void code_ldc(int paramInt, DataOutputStream paramDataOutputStream)
/*      */     throws IOException
/*      */   {
/* 1356 */     assert ((paramInt >= 0) && (paramInt <= 65535));
/* 1357 */     if (paramInt <= 255) {
/* 1358 */       paramDataOutputStream.writeByte(18);
/* 1359 */       paramDataOutputStream.writeByte(paramInt & 0xFF);
/*      */     } else {
/* 1361 */       paramDataOutputStream.writeByte(19);
/* 1362 */       paramDataOutputStream.writeShort(paramInt & 0xFFFF);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void code_ipush(int paramInt, DataOutputStream paramDataOutputStream)
/*      */     throws IOException
/*      */   {
/* 1375 */     if ((paramInt >= -1) && (paramInt <= 5)) {
/* 1376 */       paramDataOutputStream.writeByte(3 + paramInt);
/* 1377 */     } else if ((paramInt >= -128) && (paramInt <= 127)) {
/* 1378 */       paramDataOutputStream.writeByte(16);
/* 1379 */       paramDataOutputStream.writeByte(paramInt & 0xFF);
/* 1380 */     } else if ((paramInt >= -32768) && (paramInt <= 32767)) {
/* 1381 */       paramDataOutputStream.writeByte(17);
/* 1382 */       paramDataOutputStream.writeShort(paramInt & 0xFFFF);
/*      */     } else {
/* 1384 */       throw new AssertionError();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void codeClassForName(Class paramClass, DataOutputStream paramDataOutputStream)
/*      */     throws IOException
/*      */   {
/* 1397 */     code_ldc(this.cp.getString(paramClass.getName()), paramDataOutputStream);
/*      */ 
/* 1399 */     paramDataOutputStream.writeByte(184);
/* 1400 */     paramDataOutputStream.writeShort(this.cp.getMethodRef("java/lang/Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;"));
/*      */   }
/*      */ 
/*      */   private static String dotToSlash(String paramString)
/*      */   {
/* 1418 */     return paramString.replace('.', '/');
/*      */   }
/*      */ 
/*      */   private static String getMethodDescriptor(Class[] paramArrayOfClass, Class paramClass)
/*      */   {
/* 1428 */     return getParameterDescriptors(paramArrayOfClass) + (paramClass == Void.TYPE ? "V" : getFieldType(paramClass));
/*      */   }
/*      */ 
/*      */   private static String getParameterDescriptors(Class[] paramArrayOfClass)
/*      */   {
/* 1440 */     StringBuilder localStringBuilder = new StringBuilder("(");
/* 1441 */     for (int i = 0; i < paramArrayOfClass.length; i++) {
/* 1442 */       localStringBuilder.append(getFieldType(paramArrayOfClass[i]));
/*      */     }
/* 1444 */     localStringBuilder.append(')');
/* 1445 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private static String getFieldType(Class paramClass)
/*      */   {
/* 1454 */     if (paramClass.isPrimitive())
/* 1455 */       return PrimitiveTypeInfo.get(paramClass).baseTypeString;
/* 1456 */     if (paramClass.isArray())
/*      */     {
/* 1464 */       return paramClass.getName().replace('.', '/');
/*      */     }
/* 1466 */     return "L" + dotToSlash(paramClass.getName()) + ";";
/*      */   }
/*      */ 
/*      */   private static String getFriendlyMethodSignature(String paramString, Class[] paramArrayOfClass)
/*      */   {
/* 1477 */     StringBuilder localStringBuilder = new StringBuilder(paramString);
/* 1478 */     localStringBuilder.append('(');
/* 1479 */     for (int i = 0; i < paramArrayOfClass.length; i++) {
/* 1480 */       if (i > 0) {
/* 1481 */         localStringBuilder.append(',');
/*      */       }
/* 1483 */       Class localClass = paramArrayOfClass[i];
/* 1484 */       int j = 0;
/* 1485 */       while (localClass.isArray()) {
/* 1486 */         localClass = localClass.getComponentType();
/* 1487 */         j++;
/*      */       }
/* 1489 */       localStringBuilder.append(localClass.getName());
/* 1490 */       while (j-- > 0) {
/* 1491 */         localStringBuilder.append("[]");
/*      */       }
/*      */     }
/* 1494 */     localStringBuilder.append(')');
/* 1495 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private static int getWordsPerType(Class paramClass)
/*      */   {
/* 1508 */     if ((paramClass == Long.TYPE) || (paramClass == Double.TYPE)) {
/* 1509 */       return 2;
/*      */     }
/* 1511 */     return 1;
/*      */   }
/*      */ 
/*      */   private static void collectCompatibleTypes(Class<?>[] paramArrayOfClass1, Class<?>[] paramArrayOfClass2, List<Class<?>> paramList)
/*      */   {
/* 1528 */     for (int i = 0; i < paramArrayOfClass1.length; i++)
/* 1529 */       if (!paramList.contains(paramArrayOfClass1[i]))
/* 1530 */         for (int j = 0; j < paramArrayOfClass2.length; j++)
/* 1531 */           if (paramArrayOfClass2[j].isAssignableFrom(paramArrayOfClass1[i])) {
/* 1532 */             paramList.add(paramArrayOfClass1[i]);
/* 1533 */             break;
/*      */           }
/*      */   }
/*      */ 
/*      */   private static List<Class<?>> computeUniqueCatchList(Class<?>[] paramArrayOfClass)
/*      */   {
/* 1562 */     ArrayList localArrayList = new ArrayList();
/*      */ 
/* 1565 */     localArrayList.add(Error.class);
/* 1566 */     localArrayList.add(RuntimeException.class);
/*      */ 
/* 1569 */     label146: for (int i = 0; i < paramArrayOfClass.length; i++) {
/* 1570 */       Class<?> localClass = paramArrayOfClass[i];
/* 1571 */       if (localClass.isAssignableFrom(Throwable.class))
/*      */       {
/* 1577 */         localArrayList.clear();
/* 1578 */         break;
/* 1579 */       }if (Throwable.class.isAssignableFrom(localClass))
/*      */       {
/* 1589 */         for (int j = 0; j < localArrayList.size(); ) {
/* 1590 */           Class localClass1 = (Class)localArrayList.get(j);
/* 1591 */           if (localClass1.isAssignableFrom(localClass))
/*      */           {
/*      */             break label146;
/*      */           }
/*      */ 
/* 1597 */           if (localClass.isAssignableFrom(localClass1))
/*      */           {
/* 1602 */             localArrayList.remove(j);
/*      */           }
/* 1604 */           else j++;
/*      */ 
/*      */         }
/*      */ 
/* 1608 */         localArrayList.add(localClass);
/*      */       }
/*      */     }
/* 1610 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  311 */     saveGeneratedFiles = ((Boolean)AccessController.doPrivileged(new GetBooleanAction("sun.misc.ProxyGenerator.saveGeneratedFiles"))).booleanValue();
/*      */     try
/*      */     {
/*  352 */       hashCodeMethod = Object.class.getMethod("hashCode", new Class[0]);
/*  353 */       equalsMethod = Object.class.getMethod("equals", new Class[] { Object.class });
/*      */ 
/*  355 */       toStringMethod = Object.class.getMethod("toString", new Class[0]);
/*      */     } catch (NoSuchMethodException localNoSuchMethodException) {
/*  357 */       throw new NoSuchMethodError(localNoSuchMethodException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ConstantPool
/*      */   {
/*      */     private List<Entry> pool;
/*      */     private Map<Object, Short> map;
/*      */     private boolean readOnly;
/*      */ 
/*      */     private ConstantPool()
/*      */     {
/* 1697 */       this.pool = new ArrayList(32);
/*      */ 
/* 1705 */       this.map = new HashMap(16);
/*      */ 
/* 1708 */       this.readOnly = false;
/*      */     }
/*      */ 
/*      */     public short getUtf8(String paramString)
/*      */     {
/* 1714 */       if (paramString == null) {
/* 1715 */         throw new NullPointerException();
/*      */       }
/* 1717 */       return getValue(paramString);
/*      */     }
/*      */ 
/*      */     public short getInteger(int paramInt)
/*      */     {
/* 1724 */       return getValue(new Integer(paramInt));
/*      */     }
/*      */ 
/*      */     public short getFloat(float paramFloat)
/*      */     {
/* 1731 */       return getValue(new Float(paramFloat));
/*      */     }
/*      */ 
/*      */     public short getClass(String paramString)
/*      */     {
/* 1738 */       short s = getUtf8(paramString);
/* 1739 */       return getIndirect(new IndirectEntry(7, s));
/*      */     }
/*      */ 
/*      */     public short getString(String paramString)
/*      */     {
/* 1747 */       short s = getUtf8(paramString);
/* 1748 */       return getIndirect(new IndirectEntry(8, s));
/*      */     }
/*      */ 
/*      */     public short getFieldRef(String paramString1, String paramString2, String paramString3)
/*      */     {
/* 1758 */       short s1 = getClass(paramString1);
/* 1759 */       short s2 = getNameAndType(paramString2, paramString3);
/* 1760 */       return getIndirect(new IndirectEntry(9, s1, s2));
/*      */     }
/*      */ 
/*      */     public short getMethodRef(String paramString1, String paramString2, String paramString3)
/*      */     {
/* 1770 */       short s1 = getClass(paramString1);
/* 1771 */       short s2 = getNameAndType(paramString2, paramString3);
/* 1772 */       return getIndirect(new IndirectEntry(10, s1, s2));
/*      */     }
/*      */ 
/*      */     public short getInterfaceMethodRef(String paramString1, String paramString2, String paramString3)
/*      */     {
/* 1782 */       short s1 = getClass(paramString1);
/* 1783 */       short s2 = getNameAndType(paramString2, paramString3);
/* 1784 */       return getIndirect(new IndirectEntry(11, s1, s2));
/*      */     }
/*      */ 
/*      */     public short getNameAndType(String paramString1, String paramString2)
/*      */     {
/* 1792 */       short s1 = getUtf8(paramString1);
/* 1793 */       short s2 = getUtf8(paramString2);
/* 1794 */       return getIndirect(new IndirectEntry(12, s1, s2));
/*      */     }
/*      */ 
/*      */     public void setReadOnly()
/*      */     {
/* 1806 */       this.readOnly = true;
/*      */     }
/*      */ 
/*      */     public void write(OutputStream paramOutputStream)
/*      */       throws IOException
/*      */     {
/* 1818 */       DataOutputStream localDataOutputStream = new DataOutputStream(paramOutputStream);
/*      */ 
/* 1821 */       localDataOutputStream.writeShort(this.pool.size() + 1);
/*      */ 
/* 1823 */       for (Entry localEntry : this.pool)
/* 1824 */         localEntry.write(localDataOutputStream);
/*      */     }
/*      */ 
/*      */     private short addEntry(Entry paramEntry)
/*      */     {
/* 1832 */       this.pool.add(paramEntry);
/*      */ 
/* 1838 */       if (this.pool.size() >= 65535) {
/* 1839 */         throw new IllegalArgumentException("constant pool size limit exceeded");
/*      */       }
/*      */ 
/* 1842 */       return (short)this.pool.size();
/*      */     }
/*      */ 
/*      */     private short getValue(Object paramObject)
/*      */     {
/* 1857 */       Short localShort = (Short)this.map.get(paramObject);
/* 1858 */       if (localShort != null) {
/* 1859 */         return localShort.shortValue();
/*      */       }
/* 1861 */       if (this.readOnly) {
/* 1862 */         throw new InternalError("late constant pool addition: " + paramObject);
/*      */       }
/*      */ 
/* 1865 */       short s = addEntry(new ValueEntry(paramObject));
/* 1866 */       this.map.put(paramObject, new Short(s));
/* 1867 */       return s;
/*      */     }
/*      */ 
/*      */     private short getIndirect(IndirectEntry paramIndirectEntry)
/*      */     {
/* 1876 */       Short localShort = (Short)this.map.get(paramIndirectEntry);
/* 1877 */       if (localShort != null) {
/* 1878 */         return localShort.shortValue();
/*      */       }
/* 1880 */       if (this.readOnly) {
/* 1881 */         throw new InternalError("late constant pool addition");
/*      */       }
/* 1883 */       short s = addEntry(paramIndirectEntry);
/* 1884 */       this.map.put(paramIndirectEntry, new Short(s));
/* 1885 */       return s;
/*      */     }
/*      */ 
/*      */     private static abstract class Entry
/*      */     {
/*      */       public abstract void write(DataOutputStream paramDataOutputStream)
/*      */         throws IOException;
/*      */     }
/*      */ 
/*      */     private static class IndirectEntry extends ProxyGenerator.ConstantPool.Entry
/*      */     {
/*      */       private int tag;
/*      */       private short index0;
/*      */       private short index1;
/*      */ 
/*      */       public IndirectEntry(int paramInt, short paramShort)
/*      */       {
/* 1960 */         super();
/* 1961 */         this.tag = paramInt;
/* 1962 */         this.index0 = paramShort;
/* 1963 */         this.index1 = 0;
/*      */       }
/*      */ 
/*      */       public IndirectEntry(int paramInt, short paramShort1, short paramShort2)
/*      */       {
/* 1970 */         super();
/* 1971 */         this.tag = paramInt;
/* 1972 */         this.index0 = paramShort1;
/* 1973 */         this.index1 = paramShort2;
/*      */       }
/*      */ 
/*      */       public void write(DataOutputStream paramDataOutputStream) throws IOException {
/* 1977 */         paramDataOutputStream.writeByte(this.tag);
/* 1978 */         paramDataOutputStream.writeShort(this.index0);
/*      */ 
/* 1983 */         if ((this.tag == 9) || (this.tag == 10) || (this.tag == 11) || (this.tag == 12))
/*      */         {
/* 1988 */           paramDataOutputStream.writeShort(this.index1);
/*      */         }
/*      */       }
/*      */ 
/*      */       public int hashCode() {
/* 1993 */         return this.tag + this.index0 + this.index1;
/*      */       }
/*      */ 
/*      */       public boolean equals(Object paramObject) {
/* 1997 */         if ((paramObject instanceof IndirectEntry)) {
/* 1998 */           IndirectEntry localIndirectEntry = (IndirectEntry)paramObject;
/* 1999 */           if ((this.tag == localIndirectEntry.tag) && (this.index0 == localIndirectEntry.index0) && (this.index1 == localIndirectEntry.index1))
/*      */           {
/* 2002 */             return true;
/*      */           }
/*      */         }
/* 2005 */         return false;
/*      */       }
/*      */     }
/*      */ 
/*      */     private static class ValueEntry extends ProxyGenerator.ConstantPool.Entry
/*      */     {
/*      */       private Object value;
/*      */ 
/*      */       public ValueEntry(Object paramObject)
/*      */       {
/* 1910 */         super();
/* 1911 */         this.value = paramObject;
/*      */       }
/*      */ 
/*      */       public void write(DataOutputStream paramDataOutputStream) throws IOException {
/* 1915 */         if ((this.value instanceof String)) {
/* 1916 */           paramDataOutputStream.writeByte(1);
/* 1917 */           paramDataOutputStream.writeUTF((String)this.value);
/* 1918 */         } else if ((this.value instanceof Integer)) {
/* 1919 */           paramDataOutputStream.writeByte(3);
/* 1920 */           paramDataOutputStream.writeInt(((Integer)this.value).intValue());
/* 1921 */         } else if ((this.value instanceof Float)) {
/* 1922 */           paramDataOutputStream.writeByte(4);
/* 1923 */           paramDataOutputStream.writeFloat(((Float)this.value).floatValue());
/* 1924 */         } else if ((this.value instanceof Long)) {
/* 1925 */           paramDataOutputStream.writeByte(5);
/* 1926 */           paramDataOutputStream.writeLong(((Long)this.value).longValue());
/* 1927 */         } else if ((this.value instanceof Double)) {
/* 1928 */           paramDataOutputStream.writeDouble(6.0D);
/* 1929 */           paramDataOutputStream.writeDouble(((Double)this.value).doubleValue());
/*      */         } else {
/* 1931 */           throw new InternalError("bogus value entry: " + this.value);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class ExceptionTableEntry
/*      */   {
/*      */     public short startPc;
/*      */     public short endPc;
/*      */     public short handlerPc;
/*      */     public short catchType;
/*      */ 
/*      */     public ExceptionTableEntry(short paramShort1, short paramShort2, short paramShort3, short paramShort4)
/*      */     {
/*  746 */       this.startPc = paramShort1;
/*  747 */       this.endPc = paramShort2;
/*  748 */       this.handlerPc = paramShort3;
/*  749 */       this.catchType = paramShort4;
/*      */     }
/*      */   }
/*      */ 
/*      */   private class FieldInfo
/*      */   {
/*      */     public int accessFlags;
/*      */     public String name;
/*      */     public String descriptor;
/*      */ 
/*      */     public FieldInfo(String paramString1, String paramInt, int arg4)
/*      */     {
/*  704 */       this.name = paramString1;
/*  705 */       this.descriptor = paramInt;
/*      */       int i;
/*  706 */       this.accessFlags = i;
/*      */ 
/*  712 */       ProxyGenerator.this.cp.getUtf8(paramString1);
/*  713 */       ProxyGenerator.this.cp.getUtf8(paramInt);
/*      */     }
/*      */ 
/*      */     public void write(DataOutputStream paramDataOutputStream)
/*      */       throws IOException
/*      */     {
/*  722 */       paramDataOutputStream.writeShort(this.accessFlags);
/*      */ 
/*  724 */       paramDataOutputStream.writeShort(ProxyGenerator.this.cp.getUtf8(this.name));
/*      */ 
/*  726 */       paramDataOutputStream.writeShort(ProxyGenerator.this.cp.getUtf8(this.descriptor));
/*      */ 
/*  728 */       paramDataOutputStream.writeShort(0);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class MethodInfo
/*      */   {
/*      */     public int accessFlags;
/*      */     public String name;
/*      */     public String descriptor;
/*      */     public short maxStack;
/*      */     public short maxLocals;
/*  764 */     public ByteArrayOutputStream code = new ByteArrayOutputStream();
/*  765 */     public List<ProxyGenerator.ExceptionTableEntry> exceptionTable = new ArrayList();
/*      */     public short[] declaredExceptions;
/*      */ 
/*      */     public MethodInfo(String paramString1, String paramInt, int arg4)
/*      */     {
/*  770 */       this.name = paramString1;
/*  771 */       this.descriptor = paramInt;
/*      */       int i;
/*  772 */       this.accessFlags = i;
/*      */ 
/*  778 */       ProxyGenerator.this.cp.getUtf8(paramString1);
/*  779 */       ProxyGenerator.this.cp.getUtf8(paramInt);
/*  780 */       ProxyGenerator.this.cp.getUtf8("Code");
/*  781 */       ProxyGenerator.this.cp.getUtf8("Exceptions");
/*      */     }
/*      */ 
/*      */     public void write(DataOutputStream paramDataOutputStream)
/*      */       throws IOException
/*      */     {
/*  790 */       paramDataOutputStream.writeShort(this.accessFlags);
/*      */ 
/*  792 */       paramDataOutputStream.writeShort(ProxyGenerator.this.cp.getUtf8(this.name));
/*      */ 
/*  794 */       paramDataOutputStream.writeShort(ProxyGenerator.this.cp.getUtf8(this.descriptor));
/*      */ 
/*  796 */       paramDataOutputStream.writeShort(2);
/*      */ 
/*  801 */       paramDataOutputStream.writeShort(ProxyGenerator.this.cp.getUtf8("Code"));
/*      */ 
/*  803 */       paramDataOutputStream.writeInt(12 + this.code.size() + 8 * this.exceptionTable.size());
/*      */ 
/*  805 */       paramDataOutputStream.writeShort(this.maxStack);
/*      */ 
/*  807 */       paramDataOutputStream.writeShort(this.maxLocals);
/*      */ 
/*  809 */       paramDataOutputStream.writeInt(this.code.size());
/*      */ 
/*  811 */       this.code.writeTo(paramDataOutputStream);
/*      */ 
/*  813 */       paramDataOutputStream.writeShort(this.exceptionTable.size());
/*  814 */       for (ProxyGenerator.ExceptionTableEntry localExceptionTableEntry : this.exceptionTable)
/*      */       {
/*  816 */         paramDataOutputStream.writeShort(localExceptionTableEntry.startPc);
/*      */ 
/*  818 */         paramDataOutputStream.writeShort(localExceptionTableEntry.endPc);
/*      */ 
/*  820 */         paramDataOutputStream.writeShort(localExceptionTableEntry.handlerPc);
/*      */ 
/*  822 */         paramDataOutputStream.writeShort(localExceptionTableEntry.catchType);
/*      */       }
/*      */ 
/*  825 */       paramDataOutputStream.writeShort(0);
/*      */ 
/*  830 */       paramDataOutputStream.writeShort(ProxyGenerator.this.cp.getUtf8("Exceptions"));
/*      */ 
/*  832 */       paramDataOutputStream.writeInt(2 + 2 * this.declaredExceptions.length);
/*      */ 
/*  834 */       paramDataOutputStream.writeShort(this.declaredExceptions.length);
/*      */ 
/*  836 */       for (int i = 0; i < this.declaredExceptions.length; i++)
/*  837 */         paramDataOutputStream.writeShort(this.declaredExceptions[i]);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class PrimitiveTypeInfo
/*      */   {
/*      */     public String baseTypeString;
/*      */     public String wrapperClassName;
/*      */     public String wrapperValueOfDesc;
/*      */     public String unwrapMethodName;
/*      */     public String unwrapMethodDesc;
/*      */     private static Map<Class, PrimitiveTypeInfo> table;
/*      */ 
/*      */     private static void add(Class paramClass1, Class paramClass2)
/*      */     {
/* 1649 */       table.put(paramClass1, new PrimitiveTypeInfo(paramClass1, paramClass2));
/*      */     }
/*      */ 
/*      */     private PrimitiveTypeInfo(Class paramClass1, Class paramClass2)
/*      */     {
/* 1654 */       assert (paramClass1.isPrimitive());
/*      */ 
/* 1656 */       this.baseTypeString = Array.newInstance(paramClass1, 0).getClass().getName().substring(1);
/*      */ 
/* 1659 */       this.wrapperClassName = ProxyGenerator.dotToSlash(paramClass2.getName());
/* 1660 */       this.wrapperValueOfDesc = ("(" + this.baseTypeString + ")L" + this.wrapperClassName + ";");
/*      */ 
/* 1662 */       this.unwrapMethodName = (paramClass1.getName() + "Value");
/* 1663 */       this.unwrapMethodDesc = ("()" + this.baseTypeString);
/*      */     }
/*      */ 
/*      */     public static PrimitiveTypeInfo get(Class paramClass) {
/* 1667 */       return (PrimitiveTypeInfo)table.get(paramClass);
/*      */     }
/*      */ 
/*      */     static
/*      */     {
/* 1635 */       table = new HashMap();
/*      */ 
/* 1638 */       add(Byte.TYPE, Byte.class);
/* 1639 */       add(Character.TYPE, Character.class);
/* 1640 */       add(Double.TYPE, Double.class);
/* 1641 */       add(Float.TYPE, Float.class);
/* 1642 */       add(Integer.TYPE, Integer.class);
/* 1643 */       add(Long.TYPE, Long.class);
/* 1644 */       add(Short.TYPE, Short.class);
/* 1645 */       add(Boolean.TYPE, Boolean.class);
/*      */     }
/*      */   }
/*      */ 
/*      */   private class ProxyMethod
/*      */   {
/*      */     public String methodName;
/*      */     public Class[] parameterTypes;
/*      */     public Class returnType;
/*      */     public Class[] exceptionTypes;
/*      */     public Class fromClass;
/*      */     public String methodFieldName;
/*      */ 
/*      */     private ProxyMethod(String paramArrayOfClass1, Class[] paramClass1, Class paramArrayOfClass2, Class[] paramClass2, Class arg6)
/*      */     {
/*  861 */       this.methodName = paramArrayOfClass1;
/*  862 */       this.parameterTypes = paramClass1;
/*  863 */       this.returnType = paramArrayOfClass2;
/*  864 */       this.exceptionTypes = paramClass2;
/*      */       Object localObject;
/*  865 */       this.fromClass = localObject;
/*  866 */       this.methodFieldName = ("m" + ProxyGenerator.access$508(ProxyGenerator.this));
/*      */     }
/*      */ 
/*      */     private ProxyGenerator.MethodInfo generateMethod()
/*      */       throws IOException
/*      */     {
/*  874 */       String str = ProxyGenerator.getMethodDescriptor(this.parameterTypes, this.returnType);
/*  875 */       ProxyGenerator.MethodInfo localMethodInfo = new ProxyGenerator.MethodInfo(ProxyGenerator.this, this.methodName, str, 17);
/*      */ 
/*  878 */       int[] arrayOfInt = new int[this.parameterTypes.length];
/*  879 */       int i = 1;
/*  880 */       for (int j = 0; j < arrayOfInt.length; j++) {
/*  881 */         arrayOfInt[j] = i;
/*  882 */         i += ProxyGenerator.getWordsPerType(this.parameterTypes[j]);
/*      */       }
/*  884 */       j = i;
/*  885 */       short s2 = 0;
/*      */ 
/*  887 */       DataOutputStream localDataOutputStream = new DataOutputStream(localMethodInfo.code);
/*      */ 
/*  889 */       ProxyGenerator.this.code_aload(0, localDataOutputStream);
/*      */ 
/*  891 */       localDataOutputStream.writeByte(180);
/*  892 */       localDataOutputStream.writeShort(ProxyGenerator.this.cp.getFieldRef("java/lang/reflect/Proxy", "h", "Ljava/lang/reflect/InvocationHandler;"));
/*      */ 
/*  896 */       ProxyGenerator.this.code_aload(0, localDataOutputStream);
/*      */ 
/*  898 */       localDataOutputStream.writeByte(178);
/*  899 */       localDataOutputStream.writeShort(ProxyGenerator.this.cp.getFieldRef(ProxyGenerator.dotToSlash(ProxyGenerator.this.className), this.methodFieldName, "Ljava/lang/reflect/Method;"));
/*      */ 
/*  903 */       if (this.parameterTypes.length > 0)
/*      */       {
/*  905 */         ProxyGenerator.this.code_ipush(this.parameterTypes.length, localDataOutputStream);
/*      */ 
/*  907 */         localDataOutputStream.writeByte(189);
/*  908 */         localDataOutputStream.writeShort(ProxyGenerator.this.cp.getClass("java/lang/Object"));
/*      */ 
/*  910 */         for (int k = 0; k < this.parameterTypes.length; k++)
/*      */         {
/*  912 */           localDataOutputStream.writeByte(89);
/*      */ 
/*  914 */           ProxyGenerator.this.code_ipush(k, localDataOutputStream);
/*      */ 
/*  916 */           codeWrapArgument(this.parameterTypes[k], arrayOfInt[k], localDataOutputStream);
/*      */ 
/*  918 */           localDataOutputStream.writeByte(83);
/*      */         }
/*      */       }
/*      */       else {
/*  922 */         localDataOutputStream.writeByte(1);
/*      */       }
/*      */ 
/*  925 */       localDataOutputStream.writeByte(185);
/*  926 */       localDataOutputStream.writeShort(ProxyGenerator.this.cp.getInterfaceMethodRef("java/lang/reflect/InvocationHandler", "invoke", "(Ljava/lang/Object;Ljava/lang/reflect/Method;[Ljava/lang/Object;)Ljava/lang/Object;"));
/*      */ 
/*  931 */       localDataOutputStream.writeByte(4);
/*  932 */       localDataOutputStream.writeByte(0);
/*      */ 
/*  934 */       if (this.returnType == Void.TYPE)
/*      */       {
/*  936 */         localDataOutputStream.writeByte(87);
/*      */ 
/*  938 */         localDataOutputStream.writeByte(177);
/*      */       }
/*      */       else
/*      */       {
/*  942 */         codeUnwrapReturnValue(this.returnType, localDataOutputStream);
/*      */       }
/*      */       short s1;
/*  945 */       short s3 = s1 = (short)localMethodInfo.code.size();
/*      */ 
/*  947 */       List localList = ProxyGenerator.computeUniqueCatchList(this.exceptionTypes);
/*  948 */       if (localList.size() > 0)
/*      */       {
/*  950 */         for (Class localClass : localList) {
/*  951 */           localMethodInfo.exceptionTable.add(new ProxyGenerator.ExceptionTableEntry(s2, s3, s1, ProxyGenerator.this.cp.getClass(ProxyGenerator.dotToSlash(localClass.getName()))));
/*      */         }
/*      */ 
/*  956 */         localDataOutputStream.writeByte(191);
/*      */ 
/*  958 */         s1 = (short)localMethodInfo.code.size();
/*      */ 
/*  960 */         localMethodInfo.exceptionTable.add(new ProxyGenerator.ExceptionTableEntry(s2, s3, s1, ProxyGenerator.this.cp.getClass("java/lang/Throwable")));
/*      */ 
/*  963 */         ProxyGenerator.this.code_astore(j, localDataOutputStream);
/*      */ 
/*  965 */         localDataOutputStream.writeByte(187);
/*  966 */         localDataOutputStream.writeShort(ProxyGenerator.this.cp.getClass("java/lang/reflect/UndeclaredThrowableException"));
/*      */ 
/*  969 */         localDataOutputStream.writeByte(89);
/*      */ 
/*  971 */         ProxyGenerator.this.code_aload(j, localDataOutputStream);
/*      */ 
/*  973 */         localDataOutputStream.writeByte(183);
/*      */ 
/*  975 */         localDataOutputStream.writeShort(ProxyGenerator.this.cp.getMethodRef("java/lang/reflect/UndeclaredThrowableException", "<init>", "(Ljava/lang/Throwable;)V"));
/*      */ 
/*  979 */         localDataOutputStream.writeByte(191);
/*      */       }
/*      */ 
/*  982 */       if (localMethodInfo.code.size() > 65535) {
/*  983 */         throw new IllegalArgumentException("code size limit exceeded");
/*      */       }
/*      */ 
/*  986 */       localMethodInfo.maxStack = 10;
/*  987 */       localMethodInfo.maxLocals = ((short)(j + 1));
/*  988 */       localMethodInfo.declaredExceptions = new short[this.exceptionTypes.length];
/*  989 */       for (int m = 0; m < this.exceptionTypes.length; m++) {
/*  990 */         localMethodInfo.declaredExceptions[m] = ProxyGenerator.this.cp.getClass(ProxyGenerator.dotToSlash(this.exceptionTypes[m].getName()));
/*      */       }
/*      */ 
/*  994 */       return localMethodInfo;
/*      */     }
/*      */ 
/*      */     private void codeWrapArgument(Class paramClass, int paramInt, DataOutputStream paramDataOutputStream)
/*      */       throws IOException
/*      */     {
/* 1008 */       if (paramClass.isPrimitive()) {
/* 1009 */         ProxyGenerator.PrimitiveTypeInfo localPrimitiveTypeInfo = ProxyGenerator.PrimitiveTypeInfo.get(paramClass);
/*      */ 
/* 1011 */         if ((paramClass == Integer.TYPE) || (paramClass == Boolean.TYPE) || (paramClass == Byte.TYPE) || (paramClass == Character.TYPE) || (paramClass == Short.TYPE))
/*      */         {
/* 1017 */           ProxyGenerator.this.code_iload(paramInt, paramDataOutputStream);
/* 1018 */         } else if (paramClass == Long.TYPE)
/* 1019 */           ProxyGenerator.this.code_lload(paramInt, paramDataOutputStream);
/* 1020 */         else if (paramClass == Float.TYPE)
/* 1021 */           ProxyGenerator.this.code_fload(paramInt, paramDataOutputStream);
/* 1022 */         else if (paramClass == Double.TYPE)
/* 1023 */           ProxyGenerator.this.code_dload(paramInt, paramDataOutputStream);
/*      */         else {
/* 1025 */           throw new AssertionError();
/*      */         }
/*      */ 
/* 1028 */         paramDataOutputStream.writeByte(184);
/* 1029 */         paramDataOutputStream.writeShort(ProxyGenerator.this.cp.getMethodRef(localPrimitiveTypeInfo.wrapperClassName, "valueOf", localPrimitiveTypeInfo.wrapperValueOfDesc));
/*      */       }
/*      */       else
/*      */       {
/* 1035 */         ProxyGenerator.this.code_aload(paramInt, paramDataOutputStream);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void codeUnwrapReturnValue(Class paramClass, DataOutputStream paramDataOutputStream)
/*      */       throws IOException
/*      */     {
/* 1048 */       if (paramClass.isPrimitive()) {
/* 1049 */         ProxyGenerator.PrimitiveTypeInfo localPrimitiveTypeInfo = ProxyGenerator.PrimitiveTypeInfo.get(paramClass);
/*      */ 
/* 1051 */         paramDataOutputStream.writeByte(192);
/* 1052 */         paramDataOutputStream.writeShort(ProxyGenerator.this.cp.getClass(localPrimitiveTypeInfo.wrapperClassName));
/*      */ 
/* 1054 */         paramDataOutputStream.writeByte(182);
/* 1055 */         paramDataOutputStream.writeShort(ProxyGenerator.this.cp.getMethodRef(localPrimitiveTypeInfo.wrapperClassName, localPrimitiveTypeInfo.unwrapMethodName, localPrimitiveTypeInfo.unwrapMethodDesc));
/*      */ 
/* 1059 */         if ((paramClass == Integer.TYPE) || (paramClass == Boolean.TYPE) || (paramClass == Byte.TYPE) || (paramClass == Character.TYPE) || (paramClass == Short.TYPE))
/*      */         {
/* 1065 */           paramDataOutputStream.writeByte(172);
/* 1066 */         } else if (paramClass == Long.TYPE)
/* 1067 */           paramDataOutputStream.writeByte(173);
/* 1068 */         else if (paramClass == Float.TYPE)
/* 1069 */           paramDataOutputStream.writeByte(174);
/* 1070 */         else if (paramClass == Double.TYPE)
/* 1071 */           paramDataOutputStream.writeByte(175);
/*      */         else {
/* 1073 */           throw new AssertionError();
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/* 1078 */         paramDataOutputStream.writeByte(192);
/* 1079 */         paramDataOutputStream.writeShort(ProxyGenerator.this.cp.getClass(ProxyGenerator.dotToSlash(paramClass.getName())));
/*      */ 
/* 1081 */         paramDataOutputStream.writeByte(176);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void codeFieldInitialization(DataOutputStream paramDataOutputStream)
/*      */       throws IOException
/*      */     {
/* 1093 */       ProxyGenerator.this.codeClassForName(this.fromClass, paramDataOutputStream);
/*      */ 
/* 1095 */       ProxyGenerator.this.code_ldc(ProxyGenerator.this.cp.getString(this.methodName), paramDataOutputStream);
/*      */ 
/* 1097 */       ProxyGenerator.this.code_ipush(this.parameterTypes.length, paramDataOutputStream);
/*      */ 
/* 1099 */       paramDataOutputStream.writeByte(189);
/* 1100 */       paramDataOutputStream.writeShort(ProxyGenerator.this.cp.getClass("java/lang/Class"));
/*      */ 
/* 1102 */       for (int i = 0; i < this.parameterTypes.length; i++)
/*      */       {
/* 1104 */         paramDataOutputStream.writeByte(89);
/*      */ 
/* 1106 */         ProxyGenerator.this.code_ipush(i, paramDataOutputStream);
/*      */ 
/* 1108 */         if (this.parameterTypes[i].isPrimitive()) {
/* 1109 */           ProxyGenerator.PrimitiveTypeInfo localPrimitiveTypeInfo = ProxyGenerator.PrimitiveTypeInfo.get(this.parameterTypes[i]);
/*      */ 
/* 1112 */           paramDataOutputStream.writeByte(178);
/* 1113 */           paramDataOutputStream.writeShort(ProxyGenerator.this.cp.getFieldRef(localPrimitiveTypeInfo.wrapperClassName, "TYPE", "Ljava/lang/Class;"));
/*      */         }
/*      */         else
/*      */         {
/* 1117 */           ProxyGenerator.this.codeClassForName(this.parameterTypes[i], paramDataOutputStream);
/*      */         }
/*      */ 
/* 1120 */         paramDataOutputStream.writeByte(83);
/*      */       }
/*      */ 
/* 1123 */       paramDataOutputStream.writeByte(182);
/* 1124 */       paramDataOutputStream.writeShort(ProxyGenerator.this.cp.getMethodRef("java/lang/Class", "getMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;"));
/*      */ 
/* 1130 */       paramDataOutputStream.writeByte(179);
/* 1131 */       paramDataOutputStream.writeShort(ProxyGenerator.this.cp.getFieldRef(ProxyGenerator.dotToSlash(ProxyGenerator.this.className), this.methodFieldName, "Ljava/lang/reflect/Method;"));
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.misc.ProxyGenerator
 * JD-Core Version:    0.6.2
 */