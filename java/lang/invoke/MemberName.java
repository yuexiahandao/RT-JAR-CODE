/*     */ package java.lang.invoke;
/*     */ 
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Member;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import sun.invoke.util.BytecodeDescriptor;
/*     */ import sun.invoke.util.VerifyAccess;
/*     */ 
/*     */ final class MemberName
/*     */   implements Member, Cloneable
/*     */ {
/*     */   private Class<?> clazz;
/*     */   private String name;
/*     */   private Object type;
/*     */   private int flags;
/*     */   private Object resolution;
/*     */   static final int BRIDGE = 64;
/*     */   static final int VARARGS = 128;
/*     */   static final int SYNTHETIC = 4096;
/*     */   static final int ANNOTATION = 8192;
/*     */   static final int ENUM = 16384;
/*     */   static final String CONSTRUCTOR_NAME = "<init>";
/*     */   static final int RECOGNIZED_MODIFIERS = 65535;
/*     */   static final int IS_METHOD = 65536;
/*     */   static final int IS_CONSTRUCTOR = 131072;
/*     */   static final int IS_FIELD = 262144;
/*     */   static final int IS_TYPE = 524288;
/*     */   static final int IS_CALLER_SENSITIVE = 1048576;
/*     */   static final int ALL_ACCESS = 7;
/*     */   static final int ALL_KINDS = 983040;
/*     */   static final int IS_INVOCABLE = 196608;
/*     */   static final int IS_FIELD_OR_METHOD = 327680;
/*     */   static final int SEARCH_ALL_SUPERS = 3145728;
/*     */ 
/*     */   public Class<?> getDeclaringClass()
/*     */   {
/*  85 */     return this.clazz;
/*     */   }
/*     */ 
/*     */   public ClassLoader getClassLoader()
/*     */   {
/*  90 */     return this.clazz.getClassLoader();
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  99 */     if (this.name == null) {
/* 100 */       expandFromVM();
/* 101 */       if (this.name == null) return null;
/*     */     }
/* 103 */     return this.name;
/*     */   }
/*     */ 
/*     */   public MethodType getMethodOrFieldType() {
/* 107 */     if (isInvocable())
/* 108 */       return getMethodType();
/* 109 */     if (isGetter())
/* 110 */       return MethodType.methodType(getFieldType());
/* 111 */     if (isSetter())
/* 112 */       return MethodType.methodType(Void.TYPE, getFieldType());
/* 113 */     throw new InternalError("not a method or field: " + this);
/*     */   }
/*     */ 
/*     */   public MethodType getMethodType()
/*     */   {
/* 120 */     if (this.type == null) {
/* 121 */       expandFromVM();
/* 122 */       if (this.type == null) return null;
/*     */     }
/* 124 */     if (!isInvocable())
/* 125 */       throw MethodHandleStatics.newIllegalArgumentException("not invocable, no method type");
/* 126 */     if ((this.type instanceof MethodType))
/* 127 */       return (MethodType)this.type;
/*     */     Object localObject1;
/*     */     Object localObject2;
/* 129 */     if ((this.type instanceof String)) {
/* 130 */       localObject1 = (String)this.type;
/* 131 */       localObject2 = MethodType.fromMethodDescriptorString((String)localObject1, getClassLoader());
/* 132 */       this.type = localObject2;
/* 133 */       return localObject2;
/*     */     }
/* 135 */     if ((this.type instanceof Object[])) {
/* 136 */       localObject1 = (Object[])this.type;
/* 137 */       localObject2 = (Class[])localObject1[1];
/* 138 */       Class localClass = (Class)localObject1[0];
/* 139 */       MethodType localMethodType = MethodType.methodType(localClass, (Class[])localObject2);
/* 140 */       this.type = localMethodType;
/* 141 */       return localMethodType;
/*     */     }
/* 143 */     throw new InternalError("bad method type " + this.type);
/*     */   }
/*     */ 
/*     */   public MethodType getInvocationType()
/*     */   {
/* 151 */     MethodType localMethodType = getMethodOrFieldType();
/* 152 */     if ((isConstructor()) && (getReferenceKind() == 8))
/* 153 */       return localMethodType.changeReturnType(this.clazz);
/* 154 */     if (!isStatic())
/* 155 */       return localMethodType.insertParameterTypes(0, new Class[] { this.clazz });
/* 156 */     return localMethodType;
/*     */   }
/*     */ 
/*     */   public Class<?>[] getParameterTypes()
/*     */   {
/* 161 */     return getMethodType().parameterArray();
/*     */   }
/*     */ 
/*     */   public Class<?> getReturnType()
/*     */   {
/* 166 */     return getMethodType().returnType();
/*     */   }
/*     */ 
/*     */   public Class<?> getFieldType()
/*     */   {
/* 174 */     if (this.type == null) {
/* 175 */       expandFromVM();
/* 176 */       if (this.type == null) return null;
/*     */     }
/* 178 */     if (isInvocable())
/* 179 */       throw MethodHandleStatics.newIllegalArgumentException("not a field or nested class, no simple type");
/* 180 */     if ((this.type instanceof Class)) {
/* 181 */       return (Class)this.type;
/*     */     }
/* 183 */     if ((this.type instanceof String)) {
/* 184 */       String str = (String)this.type;
/* 185 */       MethodType localMethodType = MethodType.fromMethodDescriptorString("()" + str, getClassLoader());
/* 186 */       Class localClass = localMethodType.returnType();
/* 187 */       this.type = localClass;
/* 188 */       return localClass;
/*     */     }
/* 190 */     throw new InternalError("bad field type " + this.type);
/*     */   }
/*     */ 
/*     */   public Object getType()
/*     */   {
/* 195 */     return isInvocable() ? getMethodType() : getFieldType();
/*     */   }
/*     */ 
/*     */   public String getSignature()
/*     */   {
/* 202 */     if (this.type == null) {
/* 203 */       expandFromVM();
/* 204 */       if (this.type == null) return null;
/*     */     }
/* 206 */     if ((this.type instanceof String))
/* 207 */       return (String)this.type;
/* 208 */     if (isInvocable()) {
/* 209 */       return BytecodeDescriptor.unparse(getMethodType());
/*     */     }
/* 211 */     return BytecodeDescriptor.unparse(getFieldType());
/*     */   }
/*     */ 
/*     */   public int getModifiers()
/*     */   {
/* 218 */     return this.flags & 0xFFFF;
/*     */   }
/*     */ 
/*     */   public byte getReferenceKind()
/*     */   {
/* 224 */     return (byte)(this.flags >>> 24 & 0xF);
/*     */   }
/*     */   private boolean referenceKindIsConsistent() {
/* 227 */     byte b = getReferenceKind();
/* 228 */     if (b == 0) return isType();
/* 229 */     if (isField()) {
/* 230 */       assert (staticIsConsistent());
/* 231 */       if ((!$assertionsDisabled) && (!MethodHandleNatives.refKindIsField(b))) throw new AssertionError(); 
/*     */     }
/* 232 */     else if (isConstructor()) {
/* 233 */       if ((!$assertionsDisabled) && (b != 8) && (b != 7)) throw new AssertionError(); 
/*     */     }
/* 234 */     else if (isMethod()) {
/* 235 */       assert (staticIsConsistent());
/* 236 */       assert (MethodHandleNatives.refKindIsMethod(b));
/* 237 */       if ((this.clazz.isInterface()) && 
/* 238 */         (!$assertionsDisabled) && (b != 9) && (
/* 238 */         (b != 5) || (!isObjectPublicMethod()))) throw new AssertionError();
/*     */ 
/*     */     }
/* 241 */     else if (!$assertionsDisabled) { throw new AssertionError(); }
/*     */ 
/* 243 */     return true;
/*     */   }
/*     */   private boolean isObjectPublicMethod() {
/* 246 */     if (this.clazz == Object.class) return true;
/* 247 */     MethodType localMethodType = getMethodType();
/* 248 */     if ((this.name.equals("toString")) && (localMethodType.returnType() == String.class) && (localMethodType.parameterCount() == 0))
/* 249 */       return true;
/* 250 */     if ((this.name.equals("hashCode")) && (localMethodType.returnType() == Integer.TYPE) && (localMethodType.parameterCount() == 0))
/* 251 */       return true;
/* 252 */     if ((this.name.equals("equals")) && (localMethodType.returnType() == Boolean.TYPE) && (localMethodType.parameterCount() == 1) && (localMethodType.parameterType(0) == Object.class))
/* 253 */       return true;
/* 254 */     return false;
/*     */   }
/*     */   boolean referenceKindIsConsistentWith(int paramInt) {
/* 257 */     int i = getReferenceKind();
/* 258 */     if (i == paramInt) return true;
/* 259 */     switch (paramInt)
/*     */     {
/*     */     case 9:
/* 263 */       assert ((i == 5) || (i == 7)) : this;
/* 264 */       return true;
/*     */     case 5:
/*     */     case 8:
/* 268 */       assert (i == 7) : this;
/* 269 */       return true;
/*     */     case 6:
/* 271 */     case 7: } if (!$assertionsDisabled) throw new AssertionError(this);
/* 272 */     return true;
/*     */   }
/*     */   private boolean staticIsConsistent() {
/* 275 */     byte b = getReferenceKind();
/* 276 */     return (MethodHandleNatives.refKindIsStatic(b) == isStatic()) || (getModifiers() == 0);
/*     */   }
/*     */   private boolean vminfoIsConsistent() {
/* 279 */     byte b = getReferenceKind();
/* 280 */     assert (isResolved());
/* 281 */     Object localObject1 = MethodHandleNatives.getMemberVMInfo(this);
/* 282 */     assert ((localObject1 instanceof Object[]));
/* 283 */     long l = ((Long)((Object[])(Object[])localObject1)[0]).longValue();
/* 284 */     Object localObject2 = ((Object[])(Object[])localObject1)[1];
/* 285 */     if (MethodHandleNatives.refKindIsField(b)) {
/* 286 */       assert (l >= 0L) : (l + ":" + this);
/* 287 */       if ((!$assertionsDisabled) && (!(localObject2 instanceof Class))) throw new AssertionError(); 
/*     */     }
/* 289 */     else { if (MethodHandleNatives.refKindDoesDispatch(b)) {
/* 290 */         if ((!$assertionsDisabled) && (l < 0L)) throw new AssertionError(l + ":" + this); 
/*     */       }
/*     */       else
/* 292 */         assert (l < 0L) : l;
/* 293 */       assert ((localObject2 instanceof MemberName)) : (localObject2 + " in " + this);
/*     */     }
/* 295 */     return true;
/*     */   }
/*     */ 
/*     */   private MemberName changeReferenceKind(byte paramByte1, byte paramByte2) {
/* 299 */     assert (getReferenceKind() == paramByte2);
/* 300 */     assert (MethodHandleNatives.refKindIsValid(paramByte1));
/* 301 */     this.flags += (paramByte1 - paramByte2 << 24);
/*     */ 
/* 306 */     return this;
/*     */   }
/*     */ 
/*     */   private boolean testFlags(int paramInt1, int paramInt2) {
/* 310 */     return (this.flags & paramInt1) == paramInt2;
/*     */   }
/*     */   private boolean testAllFlags(int paramInt) {
/* 313 */     return testFlags(paramInt, paramInt);
/*     */   }
/*     */   private boolean testAnyFlags(int paramInt) {
/* 316 */     return !testFlags(paramInt, 0);
/*     */   }
/*     */ 
/*     */   public boolean isMethodHandleInvoke()
/*     */   {
/* 323 */     if ((testFlags(280, 272)) && (this.clazz == MethodHandle.class))
/*     */     {
/* 325 */       return (this.name.equals("invoke")) || (this.name.equals("invokeExact"));
/*     */     }
/* 327 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isStatic()
/*     */   {
/* 332 */     return Modifier.isStatic(this.flags);
/*     */   }
/*     */ 
/*     */   public boolean isPublic() {
/* 336 */     return Modifier.isPublic(this.flags);
/*     */   }
/*     */ 
/*     */   public boolean isPrivate() {
/* 340 */     return Modifier.isPrivate(this.flags);
/*     */   }
/*     */ 
/*     */   public boolean isProtected() {
/* 344 */     return Modifier.isProtected(this.flags);
/*     */   }
/*     */ 
/*     */   public boolean isFinal() {
/* 348 */     return Modifier.isFinal(this.flags);
/*     */   }
/*     */ 
/*     */   public boolean canBeStaticallyBound() {
/* 352 */     return Modifier.isFinal(this.flags | this.clazz.getModifiers());
/*     */   }
/*     */ 
/*     */   public boolean isVolatile() {
/* 356 */     return Modifier.isVolatile(this.flags);
/*     */   }
/*     */ 
/*     */   public boolean isAbstract() {
/* 360 */     return Modifier.isAbstract(this.flags);
/*     */   }
/*     */ 
/*     */   public boolean isNative() {
/* 364 */     return Modifier.isNative(this.flags);
/*     */   }
/*     */ 
/*     */   public boolean isBridge()
/*     */   {
/* 376 */     return testAllFlags(65600);
/*     */   }
/*     */ 
/*     */   public boolean isVarargs() {
/* 380 */     return (testAllFlags(128)) && (isInvocable());
/*     */   }
/*     */ 
/*     */   public boolean isSynthetic() {
/* 384 */     return testAllFlags(4096);
/*     */   }
/*     */ 
/*     */   public boolean isInvocable()
/*     */   {
/* 408 */     return testAnyFlags(196608);
/*     */   }
/*     */ 
/*     */   public boolean isFieldOrMethod() {
/* 412 */     return testAnyFlags(327680);
/*     */   }
/*     */ 
/*     */   public boolean isMethod() {
/* 416 */     return testAllFlags(65536);
/*     */   }
/*     */ 
/*     */   public boolean isConstructor() {
/* 420 */     return testAllFlags(131072);
/*     */   }
/*     */ 
/*     */   public boolean isField() {
/* 424 */     return testAllFlags(262144);
/*     */   }
/*     */ 
/*     */   public boolean isType() {
/* 428 */     return testAllFlags(524288);
/*     */   }
/*     */ 
/*     */   public boolean isPackage() {
/* 432 */     return !testAnyFlags(7);
/*     */   }
/*     */ 
/*     */   public boolean isCallerSensitive() {
/* 436 */     return testAllFlags(1048576);
/*     */   }
/*     */ 
/*     */   public boolean isAccessibleFrom(Class<?> paramClass)
/*     */   {
/* 441 */     return VerifyAccess.isMemberAccessible(getDeclaringClass(), getDeclaringClass(), this.flags, paramClass, 15);
/*     */   }
/*     */ 
/*     */   private void init(Class<?> paramClass, String paramString, Object paramObject, int paramInt)
/*     */   {
/* 451 */     this.clazz = paramClass;
/* 452 */     this.name = paramString;
/* 453 */     this.type = paramObject;
/* 454 */     this.flags = paramInt;
/* 455 */     assert (testAnyFlags(983040));
/* 456 */     assert (this.resolution == null);
/*     */   }
/*     */ 
/*     */   private void expandFromVM()
/*     */   {
/* 461 */     if (!isResolved()) return;
/* 462 */     if ((this.type instanceof Object[]))
/* 463 */       this.type = null;
/* 464 */     MethodHandleNatives.expand(this);
/*     */   }
/*     */ 
/*     */   private static int flagsMods(int paramInt1, int paramInt2, byte paramByte)
/*     */   {
/* 469 */     assert ((paramInt1 & 0xFFFF) == 0);
/* 470 */     assert ((paramInt2 & 0xFFFF0000) == 0);
/* 471 */     assert ((paramByte & 0xFFFFFFF0) == 0);
/* 472 */     return paramInt1 | paramInt2 | paramByte << 24;
/*     */   }
/*     */ 
/*     */   public MemberName(Method paramMethod) {
/* 476 */     this(paramMethod, false);
/*     */   }
/*     */ 
/*     */   public MemberName(Method paramMethod, boolean paramBoolean) {
/* 480 */     paramMethod.getClass();
/*     */ 
/* 482 */     MethodHandleNatives.init(this, paramMethod);
/* 483 */     assert ((isResolved()) && (this.clazz != null));
/* 484 */     this.name = paramMethod.getName();
/* 485 */     if (this.type == null)
/* 486 */       this.type = new Object[] { paramMethod.getReturnType(), paramMethod.getParameterTypes() };
/* 487 */     if ((paramBoolean) && 
/* 488 */       (getReferenceKind() == 5))
/* 489 */       changeReferenceKind((byte)7, (byte)5);
/*     */   }
/*     */ 
/*     */   public MemberName asSpecial() {
/* 493 */     switch (getReferenceKind()) { case 7:
/* 494 */       return this;
/*     */     case 5:
/* 495 */       return clone().changeReferenceKind((byte)7, (byte)5);
/*     */     case 8:
/* 496 */       return clone().changeReferenceKind((byte)7, (byte)8);
/*     */     case 6: }
/* 498 */     throw new IllegalArgumentException(toString());
/*     */   }
/*     */   public MemberName asConstructor() {
/* 501 */     switch (getReferenceKind()) { case 7:
/* 502 */       return clone().changeReferenceKind((byte)8, (byte)7);
/*     */     case 8:
/* 503 */       return this;
/*     */     }
/* 505 */     throw new IllegalArgumentException(toString());
/*     */   }
/*     */ 
/*     */   public MemberName(Constructor<?> paramConstructor)
/*     */   {
/* 510 */     paramConstructor.getClass();
/*     */ 
/* 512 */     MethodHandleNatives.init(this, paramConstructor);
/* 513 */     assert ((isResolved()) && (this.clazz != null));
/* 514 */     this.name = "<init>";
/* 515 */     if (this.type == null)
/* 516 */       this.type = new Object[] { Void.TYPE, paramConstructor.getParameterTypes() };
/*     */   }
/*     */ 
/*     */   public MemberName(Field paramField)
/*     */   {
/* 521 */     this(paramField, false);
/*     */   }
/*     */ 
/*     */   public MemberName(Field paramField, boolean paramBoolean) {
/* 525 */     paramField.getClass();
/*     */ 
/* 527 */     MethodHandleNatives.init(this, paramField);
/* 528 */     assert ((isResolved()) && (this.clazz != null));
/* 529 */     this.name = paramField.getName();
/* 530 */     this.type = paramField.getType();
/*     */ 
/* 532 */     byte b = getReferenceKind();
/* 533 */     if (!$assertionsDisabled) if (b != (isStatic() ? 2 : 1)) throw new AssertionError();
/* 534 */     if (paramBoolean)
/* 535 */       changeReferenceKind((byte)(b + 2), b);
/*     */   }
/*     */ 
/*     */   public boolean isGetter() {
/* 539 */     return MethodHandleNatives.refKindIsGetter(getReferenceKind());
/*     */   }
/*     */   public boolean isSetter() {
/* 542 */     return MethodHandleNatives.refKindIsSetter(getReferenceKind());
/*     */   }
/*     */   public MemberName asSetter() {
/* 545 */     byte b1 = getReferenceKind();
/* 546 */     assert (MethodHandleNatives.refKindIsGetter(b1));
/*     */ 
/* 548 */     byte b2 = (byte)(b1 + 2);
/* 549 */     return clone().changeReferenceKind(b2, b1);
/*     */   }
/*     */ 
/*     */   public MemberName(Class<?> paramClass) {
/* 553 */     init(paramClass.getDeclaringClass(), paramClass.getSimpleName(), paramClass, flagsMods(524288, paramClass.getModifiers(), (byte)0));
/*     */ 
/* 555 */     initResolved(true);
/*     */   }
/*     */ 
/*     */   MemberName()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected MemberName clone() {
/*     */     try {
/* 564 */       return (MemberName)super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 566 */       throw MethodHandleStatics.newInternalError(localCloneNotSupportedException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public MemberName getDefinition()
/*     */   {
/* 574 */     if (!isResolved()) throw new IllegalStateException("must be resolved: " + this);
/* 575 */     if (isType()) return this;
/* 576 */     MemberName localMemberName = clone();
/* 577 */     localMemberName.clazz = null;
/* 578 */     localMemberName.type = null;
/* 579 */     localMemberName.name = null;
/* 580 */     localMemberName.resolution = localMemberName;
/* 581 */     localMemberName.expandFromVM();
/* 582 */     assert (localMemberName.getName().equals(getName()));
/* 583 */     return localMemberName;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 588 */     return Objects.hash(new Object[] { this.clazz, Integer.valueOf(this.flags), this.name, getType() });
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/* 592 */     return ((paramObject instanceof MemberName)) && (equals((MemberName)paramObject));
/*     */   }
/*     */ 
/*     */   public boolean equals(MemberName paramMemberName)
/*     */   {
/* 601 */     if (this == paramMemberName) return true;
/* 602 */     if (paramMemberName == null) return false;
/* 603 */     return (this.clazz == paramMemberName.clazz) && (this.flags == paramMemberName.flags) && (Objects.equals(this.name, paramMemberName.name)) && (Objects.equals(getType(), paramMemberName.getType()));
/*     */   }
/*     */ 
/*     */   public MemberName(Class<?> paramClass1, String paramString, Class<?> paramClass2, byte paramByte)
/*     */   {
/* 615 */     init(paramClass1, paramString, paramClass2, flagsMods(262144, 0, paramByte));
/* 616 */     initResolved(false);
/*     */   }
/*     */ 
/*     */   public MemberName(Class<?> paramClass1, String paramString, Class<?> paramClass2, Void paramVoid)
/*     */   {
/* 624 */     this(paramClass1, paramString, paramClass2, (byte)0);
/* 625 */     initResolved(false);
/*     */   }
/*     */ 
/*     */   public MemberName(Class<?> paramClass, String paramString, MethodType paramMethodType, byte paramByte)
/*     */   {
/* 635 */     int i = (paramString != null) && (paramString.equals("<init>")) ? 131072 : 65536;
/* 636 */     init(paramClass, paramString, paramMethodType, flagsMods(i, 0, paramByte));
/* 637 */     initResolved(false);
/*     */   }
/*     */ 
/*     */   public boolean hasReceiverTypeDispatch()
/*     */   {
/* 652 */     return MethodHandleNatives.refKindDoesDispatch(getReferenceKind());
/*     */   }
/*     */ 
/*     */   public boolean isResolved()
/*     */   {
/* 661 */     return this.resolution == null;
/*     */   }
/*     */ 
/*     */   private void initResolved(boolean paramBoolean) {
/* 665 */     assert (this.resolution == null);
/* 666 */     if (!paramBoolean)
/* 667 */       this.resolution = this;
/* 668 */     assert (isResolved() == paramBoolean);
/*     */   }
/*     */ 
/*     */   void checkForTypeAlias()
/*     */   {
/*     */     Object localObject;
/* 672 */     if (isInvocable())
/*     */     {
/* 674 */       if ((this.type instanceof MethodType))
/* 675 */         localObject = (MethodType)this.type;
/*     */       else
/* 677 */         this.type = (localObject = getMethodType());
/* 678 */       if (((MethodType)localObject).erase() == localObject) return;
/* 679 */       if (VerifyAccess.isTypeVisible((MethodType)localObject, this.clazz)) return;
/* 680 */       throw new LinkageError("bad method type alias: " + localObject + " not visible from " + this.clazz);
/*     */     }
/*     */ 
/* 683 */     if ((this.type instanceof Class))
/* 684 */       localObject = (Class)this.type;
/*     */     else
/* 686 */       this.type = (localObject = getFieldType());
/* 687 */     if (VerifyAccess.isTypeVisible((Class)localObject, this.clazz)) return;
/* 688 */     throw new LinkageError("bad field type alias: " + localObject + " not visible from " + this.clazz);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 703 */     if (isType()) {
/* 704 */       return this.type.toString();
/*     */     }
/* 706 */     StringBuilder localStringBuilder = new StringBuilder();
/* 707 */     if (getDeclaringClass() != null) {
/* 708 */       localStringBuilder.append(getName(this.clazz));
/* 709 */       localStringBuilder.append('.');
/*     */     }
/* 711 */     String str = getName();
/* 712 */     localStringBuilder.append(str == null ? "*" : str);
/* 713 */     Object localObject = getType();
/* 714 */     if (!isInvocable()) {
/* 715 */       localStringBuilder.append('/');
/* 716 */       localStringBuilder.append(localObject == null ? "*" : getName(localObject));
/*     */     } else {
/* 718 */       localStringBuilder.append(localObject == null ? "(*)*" : getName(localObject));
/*     */     }
/* 720 */     byte b = getReferenceKind();
/* 721 */     if (b != 0) {
/* 722 */       localStringBuilder.append('/');
/* 723 */       localStringBuilder.append(MethodHandleNatives.refKindName(b));
/*     */     }
/*     */ 
/* 726 */     return localStringBuilder.toString();
/*     */   }
/*     */   private static String getName(Object paramObject) {
/* 729 */     if ((paramObject instanceof Class))
/* 730 */       return ((Class)paramObject).getName();
/* 731 */     return String.valueOf(paramObject);
/*     */   }
/*     */ 
/*     */   public IllegalAccessException makeAccessException(String paramString, Object paramObject) {
/* 735 */     paramString = paramString + ": " + toString();
/* 736 */     if (paramObject != null) paramString = paramString + ", from " + paramObject;
/* 737 */     return new IllegalAccessException(paramString);
/*     */   }
/*     */   private String message() {
/* 740 */     if (isResolved())
/* 741 */       return "no access";
/* 742 */     if (isConstructor())
/* 743 */       return "no such constructor";
/* 744 */     if (isMethod()) {
/* 745 */       return "no such method";
/*     */     }
/* 747 */     return "no such field";
/*     */   }
/*     */   public ReflectiveOperationException makeAccessException() {
/* 750 */     String str = message() + ": " + toString();
/*     */     Object localObject;
/* 752 */     if ((isResolved()) || ((!(this.resolution instanceof NoSuchMethodError)) && (!(this.resolution instanceof NoSuchFieldError))))
/*     */     {
/* 754 */       localObject = new IllegalAccessException(str);
/* 755 */     } else if (isConstructor())
/* 756 */       localObject = new NoSuchMethodException(str);
/* 757 */     else if (isMethod())
/* 758 */       localObject = new NoSuchMethodException(str);
/*     */     else
/* 760 */       localObject = new NoSuchFieldException(str);
/* 761 */     if ((this.resolution instanceof Throwable))
/* 762 */       ((ReflectiveOperationException)localObject).initCause((Throwable)this.resolution);
/* 763 */     return localObject;
/*     */   }
/*     */ 
/*     */   static Factory getFactory()
/*     */   {
/* 768 */     return Factory.INSTANCE;
/*     */   }
/*     */ 
/*     */   static class Factory
/*     */   {
/* 775 */     static Factory INSTANCE = new Factory();
/*     */ 
/* 777 */     private static int ALLOWED_FLAGS = 983040;
/*     */ 
/*     */     List<MemberName> getMembers(Class<?> paramClass1, String paramString, Object paramObject, int paramInt, Class<?> paramClass2)
/*     */     {
/* 783 */       paramInt &= ALLOWED_FLAGS;
/* 784 */       String str = null;
/* 785 */       if (paramObject != null) {
/* 786 */         str = BytecodeDescriptor.unparse(paramObject);
/* 787 */         if (str.startsWith("("))
/* 788 */           paramInt &= -786433;
/*     */         else {
/* 790 */           paramInt &= -720897;
/*     */         }
/*     */       }
/* 793 */       int i = paramObject == null ? 4 : paramString == null ? 10 : 1;
/* 794 */       MemberName[] arrayOfMemberName = newMemberBuffer(i);
/* 795 */       int j = 0;
/* 796 */       ArrayList localArrayList1 = null;
/* 797 */       int k = 0;
/*     */       while (true) {
/* 799 */         k = MethodHandleNatives.getMembers(paramClass1, paramString, str, paramInt, paramClass2, j, arrayOfMemberName);
/*     */ 
/* 803 */         if (k <= arrayOfMemberName.length) {
/* 804 */           if (k < 0) k = 0;
/* 805 */           j += k;
/* 806 */           break;
/*     */         }
/*     */ 
/* 809 */         j += arrayOfMemberName.length;
/* 810 */         int m = k - arrayOfMemberName.length;
/* 811 */         if (localArrayList1 == null) localArrayList1 = new ArrayList(1);
/* 812 */         localArrayList1.add(arrayOfMemberName);
/* 813 */         int n = arrayOfMemberName.length;
/* 814 */         n = Math.max(n, m);
/* 815 */         n = Math.max(n, j / 4);
/* 816 */         arrayOfMemberName = newMemberBuffer(Math.min(8192, n));
/*     */       }
/* 818 */       ArrayList localArrayList2 = new ArrayList(j);
/*     */       Iterator localIterator;
/* 819 */       if (localArrayList1 != null)
/* 820 */         for (localIterator = localArrayList1.iterator(); localIterator.hasNext(); ) { localObject = (MemberName[])localIterator.next();
/* 821 */           Collections.addAll(localArrayList2, (Object[])localObject);
/*     */         }
/*     */       Object localObject;
/* 824 */       localArrayList2.addAll(Arrays.asList(arrayOfMemberName).subList(0, k));
/*     */ 
/* 828 */       if ((paramObject != null) && (paramObject != str)) {
/* 829 */         for (localIterator = localArrayList2.iterator(); localIterator.hasNext(); ) {
/* 830 */           localObject = (MemberName)localIterator.next();
/* 831 */           if (!paramObject.equals(((MemberName)localObject).getType()))
/* 832 */             localIterator.remove();
/*     */         }
/*     */       }
/* 835 */       return localArrayList2;
/*     */     }
/*     */ 
/*     */     private MemberName resolve(byte paramByte, MemberName paramMemberName, Class<?> paramClass)
/*     */     {
/* 844 */       MemberName localMemberName = paramMemberName.clone();
/* 845 */       assert (paramByte == localMemberName.getReferenceKind());
/*     */       try {
/* 847 */         localMemberName = MethodHandleNatives.resolve(localMemberName, paramClass);
/* 848 */         localMemberName.checkForTypeAlias();
/* 849 */         localMemberName.resolution = null;
/*     */       }
/*     */       catch (LinkageError localLinkageError) {
/* 852 */         assert (!localMemberName.isResolved());
/* 853 */         localMemberName.resolution = localLinkageError;
/* 854 */         return localMemberName;
/*     */       }
/* 856 */       assert (localMemberName.referenceKindIsConsistent());
/* 857 */       localMemberName.initResolved(true);
/* 858 */       assert (localMemberName.vminfoIsConsistent());
/* 859 */       return localMemberName;
/*     */     }
/*     */ 
/*     */     public <NoSuchMemberException extends ReflectiveOperationException> MemberName resolveOrFail(byte paramByte, MemberName paramMemberName, Class<?> paramClass, Class<NoSuchMemberException> paramClass1)
/*     */       throws IllegalAccessException, ReflectiveOperationException
/*     */     {
/* 872 */       MemberName localMemberName = resolve(paramByte, paramMemberName, paramClass);
/* 873 */       if (localMemberName.isResolved())
/* 874 */         return localMemberName;
/* 875 */       ReflectiveOperationException localReflectiveOperationException = localMemberName.makeAccessException();
/* 876 */       if ((localReflectiveOperationException instanceof IllegalAccessException)) throw ((IllegalAccessException)localReflectiveOperationException);
/* 877 */       throw ((ReflectiveOperationException)paramClass1.cast(localReflectiveOperationException));
/*     */     }
/*     */ 
/*     */     public MemberName resolveOrNull(byte paramByte, MemberName paramMemberName, Class<?> paramClass)
/*     */     {
/* 887 */       MemberName localMemberName = resolve(paramByte, paramMemberName, paramClass);
/* 888 */       if (localMemberName.isResolved())
/* 889 */         return localMemberName;
/* 890 */       return null;
/*     */     }
/*     */ 
/*     */     public List<MemberName> getMethods(Class<?> paramClass1, boolean paramBoolean, Class<?> paramClass2)
/*     */     {
/* 899 */       return getMethods(paramClass1, paramBoolean, null, null, paramClass2);
/*     */     }
/*     */ 
/*     */     public List<MemberName> getMethods(Class<?> paramClass1, boolean paramBoolean, String paramString, MethodType paramMethodType, Class<?> paramClass2)
/*     */     {
/* 909 */       int i = 0x10000 | (paramBoolean ? 3145728 : 0);
/* 910 */       return getMembers(paramClass1, paramString, paramMethodType, i, paramClass2);
/*     */     }
/*     */ 
/*     */     public List<MemberName> getConstructors(Class<?> paramClass1, Class<?> paramClass2)
/*     */     {
/* 917 */       return getMembers(paramClass1, null, null, 131072, paramClass2);
/*     */     }
/*     */ 
/*     */     public List<MemberName> getFields(Class<?> paramClass1, boolean paramBoolean, Class<?> paramClass2)
/*     */     {
/* 926 */       return getFields(paramClass1, paramBoolean, null, null, paramClass2);
/*     */     }
/*     */ 
/*     */     public List<MemberName> getFields(Class<?> paramClass1, boolean paramBoolean, String paramString, Class<?> paramClass2, Class<?> paramClass3)
/*     */     {
/* 936 */       int i = 0x40000 | (paramBoolean ? 3145728 : 0);
/* 937 */       return getMembers(paramClass1, paramString, paramClass2, i, paramClass3);
/*     */     }
/*     */ 
/*     */     public List<MemberName> getNestedTypes(Class<?> paramClass1, boolean paramBoolean, Class<?> paramClass2)
/*     */     {
/* 946 */       int i = 0x80000 | (paramBoolean ? 3145728 : 0);
/* 947 */       return getMembers(paramClass1, null, null, i, paramClass2);
/*     */     }
/*     */     private static MemberName[] newMemberBuffer(int paramInt) {
/* 950 */       MemberName[] arrayOfMemberName = new MemberName[paramInt];
/*     */ 
/* 952 */       for (int i = 0; i < paramInt; i++)
/* 953 */         arrayOfMemberName[i] = new MemberName();
/* 954 */       return arrayOfMemberName;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.invoke.MemberName
 * JD-Core Version:    0.6.2
 */