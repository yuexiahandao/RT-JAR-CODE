/*     */ package com.sun.corba.se.impl.io;
/*     */ 
/*     */ import com.sun.corba.se.impl.util.RepositoryId;
/*     */ import com.sun.corba.se.impl.util.RepositoryIdCache;
/*     */ import com.sun.org.omg.CORBA.AttributeDescription;
/*     */ import com.sun.org.omg.CORBA.Initializer;
/*     */ import com.sun.org.omg.CORBA.OperationDescription;
/*     */ import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescription;
/*     */ import com.sun.org.omg.CORBA._IDLTypeStub;
/*     */ import com.sun.org.omg.SendingContext.CodeBase;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.rmi.Remote;
/*     */ import java.util.Iterator;
/*     */ import java.util.Stack;
/*     */ import javax.rmi.CORBA.ValueHandler;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.TCKind;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.ValueMember;
/*     */ import sun.corba.JavaCorbaAccess;
/*     */ import sun.corba.SharedSecrets;
/*     */ 
/*     */ public class ValueUtility
/*     */ {
/*     */   public static final short PRIVATE_MEMBER = 0;
/*     */   public static final short PUBLIC_MEMBER = 1;
/*  60 */   private static final String[] primitiveConstants = { null, null, "S", "I", "S", "I", "F", "D", "Z", "C", "B", null, null, null, null, null, null, null, null, null, null, null, null, "J", "J", "D", "C", null, null, null, null, null, null };
/*     */ 
/*     */   public static String getSignature(ValueMember paramValueMember)
/*     */     throws ClassNotFoundException
/*     */   {
/* 120 */     if ((paramValueMember.type.kind().value() == 30) || (paramValueMember.type.kind().value() == 29) || (paramValueMember.type.kind().value() == 14))
/*     */     {
/* 123 */       Class localClass = RepositoryId.cache.getId(paramValueMember.id).getClassFromType();
/* 124 */       return ObjectStreamClass.getSignature(localClass);
/*     */     }
/*     */ 
/* 128 */     return primitiveConstants[paramValueMember.type.kind().value()];
/*     */   }
/*     */ 
/*     */   public static FullValueDescription translate(ORB paramORB, ObjectStreamClass paramObjectStreamClass, ValueHandler paramValueHandler)
/*     */   {
/* 136 */     FullValueDescription localFullValueDescription = new FullValueDescription();
/* 137 */     Class localClass1 = paramObjectStreamClass.forClass();
/*     */ 
/* 139 */     ValueHandlerImpl localValueHandlerImpl = (ValueHandlerImpl)paramValueHandler;
/* 140 */     String str = localValueHandlerImpl.createForAnyType(localClass1);
/*     */ 
/* 143 */     localFullValueDescription.name = localValueHandlerImpl.getUnqualifiedName(str);
/* 144 */     if (localFullValueDescription.name == null) {
/* 145 */       localFullValueDescription.name = "";
/*     */     }
/*     */ 
/* 148 */     localFullValueDescription.id = localValueHandlerImpl.getRMIRepositoryID(localClass1);
/* 149 */     if (localFullValueDescription.id == null) {
/* 150 */       localFullValueDescription.id = "";
/*     */     }
/*     */ 
/* 153 */     localFullValueDescription.is_abstract = ObjectStreamClassCorbaExt.isAbstractInterface(localClass1);
/*     */ 
/* 156 */     localFullValueDescription.is_custom = ((paramObjectStreamClass.hasWriteObject()) || (paramObjectStreamClass.isExternalizable()));
/*     */ 
/* 159 */     localFullValueDescription.defined_in = localValueHandlerImpl.getDefinedInId(str);
/* 160 */     if (localFullValueDescription.defined_in == null) {
/* 161 */       localFullValueDescription.defined_in = "";
/*     */     }
/*     */ 
/* 164 */     localFullValueDescription.version = localValueHandlerImpl.getSerialVersionUID(str);
/* 165 */     if (localFullValueDescription.version == null) {
/* 166 */       localFullValueDescription.version = "";
/*     */     }
/*     */ 
/* 169 */     localFullValueDescription.operations = new OperationDescription[0];
/*     */ 
/* 172 */     localFullValueDescription.attributes = new AttributeDescription[0];
/*     */ 
/* 176 */     IdentityKeyValueStack localIdentityKeyValueStack = new IdentityKeyValueStack(null);
/*     */ 
/* 178 */     localFullValueDescription.members = translateMembers(paramORB, paramObjectStreamClass, paramValueHandler, localIdentityKeyValueStack);
/*     */ 
/* 181 */     localFullValueDescription.initializers = new Initializer[0];
/*     */ 
/* 183 */     Class[] arrayOfClass = paramObjectStreamClass.forClass().getInterfaces();
/* 184 */     int i = 0;
/*     */ 
/* 187 */     localFullValueDescription.supported_interfaces = new String[arrayOfClass.length];
/* 188 */     for (int j = 0; j < arrayOfClass.length; 
/* 189 */       j++) {
/* 190 */       localFullValueDescription.supported_interfaces[j] = localValueHandlerImpl.createForAnyType(arrayOfClass[j]);
/*     */ 
/* 193 */       if ((!Remote.class.isAssignableFrom(arrayOfClass[j])) || (!Modifier.isPublic(arrayOfClass[j].getModifiers())))
/*     */       {
/* 195 */         i++;
/*     */       }
/*     */     }
/*     */ 
/* 199 */     localFullValueDescription.abstract_base_values = new String[i];
/* 200 */     for (j = 0; j < arrayOfClass.length; 
/* 201 */       j++) {
/* 202 */       if ((!Remote.class.isAssignableFrom(arrayOfClass[j])) || (!Modifier.isPublic(arrayOfClass[j].getModifiers())))
/*     */       {
/* 204 */         localFullValueDescription.abstract_base_values[j] = localValueHandlerImpl.createForAnyType(arrayOfClass[j]);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 209 */     localFullValueDescription.is_truncatable = false;
/*     */ 
/* 212 */     Class localClass2 = paramObjectStreamClass.forClass().getSuperclass();
/* 213 */     if (Serializable.class.isAssignableFrom(localClass2))
/* 214 */       localFullValueDescription.base_value = localValueHandlerImpl.getRMIRepositoryID(localClass2);
/*     */     else {
/* 216 */       localFullValueDescription.base_value = "";
/*     */     }
/*     */ 
/* 220 */     localFullValueDescription.type = paramORB.get_primitive_tc(TCKind.tk_value);
/*     */ 
/* 222 */     return localFullValueDescription;
/*     */   }
/*     */ 
/*     */   private static ValueMember[] translateMembers(ORB paramORB, ObjectStreamClass paramObjectStreamClass, ValueHandler paramValueHandler, IdentityKeyValueStack paramIdentityKeyValueStack)
/*     */   {
/* 231 */     ValueHandlerImpl localValueHandlerImpl = (ValueHandlerImpl)paramValueHandler;
/* 232 */     ObjectStreamField[] arrayOfObjectStreamField = paramObjectStreamClass.getFields();
/* 233 */     int i = arrayOfObjectStreamField.length;
/* 234 */     ValueMember[] arrayOfValueMember = new ValueMember[i];
/*     */ 
/* 237 */     for (int j = 0; j < i; j++) {
/* 238 */       String str = localValueHandlerImpl.getRMIRepositoryID(arrayOfObjectStreamField[j].getClazz());
/* 239 */       arrayOfValueMember[j] = new ValueMember();
/* 240 */       arrayOfValueMember[j].name = arrayOfObjectStreamField[j].getName();
/* 241 */       arrayOfValueMember[j].id = str;
/* 242 */       arrayOfValueMember[j].defined_in = localValueHandlerImpl.getDefinedInId(str);
/* 243 */       arrayOfValueMember[j].version = "1.0";
/* 244 */       arrayOfValueMember[j].type_def = new _IDLTypeStub();
/*     */ 
/* 246 */       if (arrayOfObjectStreamField[j].getField() == null)
/*     */       {
/* 252 */         arrayOfValueMember[j].access = 0;
/*     */       } else {
/* 254 */         int k = arrayOfObjectStreamField[j].getField().getModifiers();
/* 255 */         if (Modifier.isPublic(k))
/* 256 */           arrayOfValueMember[j].access = 1;
/*     */         else {
/* 258 */           arrayOfValueMember[j].access = 0;
/*     */         }
/*     */       }
/* 261 */       switch (arrayOfObjectStreamField[j].getTypeCode()) {
/*     */       case 'B':
/* 263 */         arrayOfValueMember[j].type = paramORB.get_primitive_tc(TCKind.tk_octet);
/* 264 */         break;
/*     */       case 'C':
/* 266 */         arrayOfValueMember[j].type = paramORB.get_primitive_tc(localValueHandlerImpl.getJavaCharTCKind());
/*     */ 
/* 268 */         break;
/*     */       case 'F':
/* 270 */         arrayOfValueMember[j].type = paramORB.get_primitive_tc(TCKind.tk_float);
/* 271 */         break;
/*     */       case 'D':
/* 273 */         arrayOfValueMember[j].type = paramORB.get_primitive_tc(TCKind.tk_double);
/* 274 */         break;
/*     */       case 'I':
/* 276 */         arrayOfValueMember[j].type = paramORB.get_primitive_tc(TCKind.tk_long);
/* 277 */         break;
/*     */       case 'J':
/* 279 */         arrayOfValueMember[j].type = paramORB.get_primitive_tc(TCKind.tk_longlong);
/* 280 */         break;
/*     */       case 'S':
/* 282 */         arrayOfValueMember[j].type = paramORB.get_primitive_tc(TCKind.tk_short);
/* 283 */         break;
/*     */       case 'Z':
/* 285 */         arrayOfValueMember[j].type = paramORB.get_primitive_tc(TCKind.tk_boolean);
/* 286 */         break;
/*     */       case 'E':
/*     */       case 'G':
/*     */       case 'H':
/*     */       case 'K':
/*     */       case 'L':
/*     */       case 'M':
/*     */       case 'N':
/*     */       case 'O':
/*     */       case 'P':
/*     */       case 'Q':
/*     */       case 'R':
/*     */       case 'T':
/*     */       case 'U':
/*     */       case 'V':
/*     */       case 'W':
/*     */       case 'X':
/*     */       case 'Y':
/*     */       default:
/* 292 */         arrayOfValueMember[j].type = createTypeCodeForClassInternal(paramORB, arrayOfObjectStreamField[j].getClazz(), localValueHandlerImpl, paramIdentityKeyValueStack);
/*     */ 
/* 294 */         arrayOfValueMember[j].id = localValueHandlerImpl.createForAnyType(arrayOfObjectStreamField[j].getType());
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 300 */     return arrayOfValueMember;
/*     */   }
/*     */ 
/*     */   private static boolean exists(String paramString, String[] paramArrayOfString) {
/* 304 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 305 */       if (paramString.equals(paramArrayOfString[i]))
/* 306 */         return true;
/*     */     }
/* 308 */     return false;
/*     */   }
/*     */ 
/*     */   public static boolean isAssignableFrom(String paramString, FullValueDescription paramFullValueDescription, CodeBase paramCodeBase)
/*     */   {
/* 314 */     if (exists(paramString, paramFullValueDescription.supported_interfaces)) {
/* 315 */       return true;
/*     */     }
/* 317 */     if (paramString.equals(paramFullValueDescription.id)) {
/* 318 */       return true;
/*     */     }
/* 320 */     if ((paramFullValueDescription.base_value != null) && (!paramFullValueDescription.base_value.equals("")))
/*     */     {
/* 322 */       FullValueDescription localFullValueDescription = paramCodeBase.meta(paramFullValueDescription.base_value);
/*     */ 
/* 324 */       return isAssignableFrom(paramString, localFullValueDescription, paramCodeBase);
/*     */     }
/*     */ 
/* 327 */     return false;
/*     */   }
/*     */ 
/*     */   public static TypeCode createTypeCodeForClass(ORB paramORB, Class paramClass, ValueHandler paramValueHandler)
/*     */   {
/* 333 */     IdentityKeyValueStack localIdentityKeyValueStack = new IdentityKeyValueStack(null);
/*     */ 
/* 335 */     TypeCode localTypeCode = createTypeCodeForClassInternal(paramORB, paramClass, paramValueHandler, localIdentityKeyValueStack);
/* 336 */     return localTypeCode;
/*     */   }
/*     */ 
/*     */   private static TypeCode createTypeCodeForClassInternal(ORB paramORB, Class paramClass, ValueHandler paramValueHandler, IdentityKeyValueStack paramIdentityKeyValueStack)
/*     */   {
/* 345 */     TypeCode localTypeCode = null;
/* 346 */     String str = (String)paramIdentityKeyValueStack.get(paramClass);
/* 347 */     if (str != null) {
/* 348 */       return paramORB.create_recursive_tc(str);
/*     */     }
/* 350 */     str = paramValueHandler.getRMIRepositoryID(paramClass);
/* 351 */     if (str == null) str = "";
/*     */ 
/* 354 */     paramIdentityKeyValueStack.push(paramClass, str);
/* 355 */     localTypeCode = createTypeCodeInternal(paramORB, paramClass, paramValueHandler, str, paramIdentityKeyValueStack);
/* 356 */     paramIdentityKeyValueStack.pop();
/* 357 */     return localTypeCode;
/*     */   }
/*     */ 
/*     */   private static TypeCode createTypeCodeInternal(ORB paramORB, Class paramClass, ValueHandler paramValueHandler, String paramString, IdentityKeyValueStack paramIdentityKeyValueStack)
/*     */   {
/* 408 */     if (paramClass.isArray())
/*     */     {
/* 410 */       localObject = paramClass.getComponentType();
/*     */       TypeCode localTypeCode1;
/* 412 */       if (((Class)localObject).isPrimitive()) {
/* 413 */         localTypeCode1 = getPrimitiveTypeCodeForClass(paramORB, (Class)localObject, paramValueHandler);
/*     */       }
/*     */       else
/*     */       {
/* 418 */         localTypeCode1 = createTypeCodeForClassInternal(paramORB, (Class)localObject, paramValueHandler, paramIdentityKeyValueStack);
/*     */       }
/*     */ 
/* 421 */       localTypeCode2 = paramORB.create_sequence_tc(0, localTypeCode1);
/* 422 */       return paramORB.create_value_box_tc(paramString, "Sequence", localTypeCode2);
/* 423 */     }if (paramClass == String.class)
/*     */     {
/* 425 */       localObject = paramORB.create_string_tc(0);
/* 426 */       return paramORB.create_value_box_tc(paramString, "StringValue", (TypeCode)localObject);
/* 427 */     }if (Remote.class.isAssignableFrom(paramClass))
/* 428 */       return paramORB.get_primitive_tc(TCKind.tk_objref);
/* 429 */     if (org.omg.CORBA.Object.class.isAssignableFrom(paramClass)) {
/* 430 */       return paramORB.get_primitive_tc(TCKind.tk_objref);
/*     */     }
/*     */ 
/* 435 */     java.lang.Object localObject = ObjectStreamClass.lookup(paramClass);
/*     */ 
/* 437 */     if (localObject == null) {
/* 438 */       return paramORB.create_value_box_tc(paramString, "Value", paramORB.get_primitive_tc(TCKind.tk_value));
/*     */     }
/*     */ 
/* 443 */     short s = ((ObjectStreamClass)localObject).isCustomMarshaled() ? 1 : 0;
/*     */ 
/* 446 */     TypeCode localTypeCode2 = null;
/* 447 */     Class localClass = paramClass.getSuperclass();
/* 448 */     if ((localClass != null) && (Serializable.class.isAssignableFrom(localClass))) {
/* 449 */       localTypeCode2 = createTypeCodeForClassInternal(paramORB, localClass, paramValueHandler, paramIdentityKeyValueStack);
/*     */     }
/*     */ 
/* 453 */     ValueMember[] arrayOfValueMember = translateMembers(paramORB, (ObjectStreamClass)localObject, paramValueHandler, paramIdentityKeyValueStack);
/*     */ 
/* 455 */     return paramORB.create_value_tc(paramString, paramClass.getName(), s, localTypeCode2, arrayOfValueMember);
/*     */   }
/*     */ 
/*     */   public static TypeCode getPrimitiveTypeCodeForClass(ORB paramORB, Class paramClass, ValueHandler paramValueHandler)
/*     */   {
/* 462 */     if (paramClass == Integer.TYPE)
/* 463 */       return paramORB.get_primitive_tc(TCKind.tk_long);
/* 464 */     if (paramClass == Byte.TYPE)
/* 465 */       return paramORB.get_primitive_tc(TCKind.tk_octet);
/* 466 */     if (paramClass == Long.TYPE)
/* 467 */       return paramORB.get_primitive_tc(TCKind.tk_longlong);
/* 468 */     if (paramClass == Float.TYPE)
/* 469 */       return paramORB.get_primitive_tc(TCKind.tk_float);
/* 470 */     if (paramClass == Double.TYPE)
/* 471 */       return paramORB.get_primitive_tc(TCKind.tk_double);
/* 472 */     if (paramClass == Short.TYPE)
/* 473 */       return paramORB.get_primitive_tc(TCKind.tk_short);
/* 474 */     if (paramClass == Character.TYPE)
/* 475 */       return paramORB.get_primitive_tc(((ValueHandlerImpl)paramValueHandler).getJavaCharTCKind());
/* 476 */     if (paramClass == Boolean.TYPE) {
/* 477 */       return paramORB.get_primitive_tc(TCKind.tk_boolean);
/*     */     }
/*     */ 
/* 480 */     return paramORB.get_primitive_tc(TCKind.tk_any);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  97 */     SharedSecrets.setJavaCorbaAccess(new JavaCorbaAccess() {
/*     */       public ValueHandlerImpl newValueHandlerImpl() {
/*  99 */         return ValueHandlerImpl.getInstance();
/*     */       }
/*     */       public Class<?> loadClass(String paramAnonymousString) throws ClassNotFoundException {
/* 102 */         if (Thread.currentThread().getContextClassLoader() != null) {
/* 103 */           return Thread.currentThread().getContextClassLoader().loadClass(paramAnonymousString);
/*     */         }
/*     */ 
/* 106 */         return ClassLoader.getSystemClassLoader().loadClass(paramAnonymousString);
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static class IdentityKeyValueStack
/*     */   {
/* 375 */     Stack pairs = null;
/*     */ 
/*     */     java.lang.Object get(java.lang.Object paramObject) {
/* 378 */       if (this.pairs == null) {
/* 379 */         return null;
/*     */       }
/* 381 */       for (Iterator localIterator = this.pairs.iterator(); localIterator.hasNext(); ) {
/* 382 */         KeyValuePair localKeyValuePair = (KeyValuePair)localIterator.next();
/* 383 */         if (localKeyValuePair.key == paramObject) {
/* 384 */           return localKeyValuePair.value;
/*     */         }
/*     */       }
/* 387 */       return null;
/*     */     }
/*     */ 
/*     */     void push(java.lang.Object paramObject1, java.lang.Object paramObject2) {
/* 391 */       if (this.pairs == null) {
/* 392 */         this.pairs = new Stack();
/*     */       }
/* 394 */       this.pairs.push(new KeyValuePair(paramObject1, paramObject2));
/*     */     }
/*     */ 
/*     */     void pop() {
/* 398 */       this.pairs.pop();
/*     */     }
/*     */ 
/*     */     private static class KeyValuePair
/*     */     {
/*     */       java.lang.Object key;
/*     */       java.lang.Object value;
/*     */ 
/*     */       KeyValuePair(java.lang.Object paramObject1, java.lang.Object paramObject2)
/*     */       {
/* 367 */         this.key = paramObject1;
/* 368 */         this.value = paramObject2;
/*     */       }
/*     */       boolean equals(KeyValuePair paramKeyValuePair) {
/* 371 */         return paramKeyValuePair.key == this.key;
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.io.ValueUtility
 * JD-Core Version:    0.6.2
 */