/*      */ package java.lang.reflect;
/*      */ 
/*      */ import java.lang.annotation.Annotation;
/*      */ import java.util.Map;
/*      */ import sun.misc.JavaLangAccess;
/*      */ import sun.misc.SharedSecrets;
/*      */ import sun.reflect.CallerSensitive;
/*      */ import sun.reflect.FieldAccessor;
/*      */ import sun.reflect.Reflection;
/*      */ import sun.reflect.ReflectionFactory;
/*      */ import sun.reflect.annotation.AnnotationParser;
/*      */ import sun.reflect.generics.factory.CoreReflectionFactory;
/*      */ import sun.reflect.generics.factory.GenericsFactory;
/*      */ import sun.reflect.generics.repository.FieldRepository;
/*      */ import sun.reflect.generics.scope.ClassScope;
/*      */ 
/*      */ public final class Field extends AccessibleObject
/*      */   implements Member
/*      */ {
/*      */   private Class<?> clazz;
/*      */   private int slot;
/*      */   private String name;
/*      */   private Class<?> type;
/*      */   private int modifiers;
/*      */   private transient String signature;
/*      */   private transient FieldRepository genericInfo;
/*      */   private byte[] annotations;
/*      */   private FieldAccessor fieldAccessor;
/*      */   private FieldAccessor overrideFieldAccessor;
/*      */   private Field root;
/*      */   private transient Map<Class<? extends Annotation>, Annotation> declaredAnnotations;
/*      */ 
/*      */   private String getGenericSignature()
/*      */   {
/*   85 */     return this.signature;
/*      */   }
/*      */ 
/*      */   private GenericsFactory getFactory() {
/*   89 */     Class localClass = getDeclaringClass();
/*      */ 
/*   91 */     return CoreReflectionFactory.make(localClass, ClassScope.make(localClass));
/*      */   }
/*      */ 
/*      */   private FieldRepository getGenericInfo()
/*      */   {
/*   97 */     if (this.genericInfo == null)
/*      */     {
/*   99 */       this.genericInfo = FieldRepository.make(getGenericSignature(), getFactory());
/*      */     }
/*      */ 
/*  102 */     return this.genericInfo;
/*      */   }
/*      */ 
/*      */   Field(Class<?> paramClass1, String paramString1, Class<?> paramClass2, int paramInt1, int paramInt2, String paramString2, byte[] paramArrayOfByte)
/*      */   {
/*  119 */     this.clazz = paramClass1;
/*  120 */     this.name = paramString1;
/*  121 */     this.type = paramClass2;
/*  122 */     this.modifiers = paramInt1;
/*  123 */     this.slot = paramInt2;
/*  124 */     this.signature = paramString2;
/*  125 */     this.annotations = paramArrayOfByte;
/*      */   }
/*      */ 
/*      */   Field copy()
/*      */   {
/*  141 */     Field localField = new Field(this.clazz, this.name, this.type, this.modifiers, this.slot, this.signature, this.annotations);
/*  142 */     localField.root = this;
/*      */ 
/*  144 */     localField.fieldAccessor = this.fieldAccessor;
/*  145 */     localField.overrideFieldAccessor = this.overrideFieldAccessor;
/*  146 */     return localField;
/*      */   }
/*      */ 
/*      */   public Class<?> getDeclaringClass()
/*      */   {
/*  154 */     return this.clazz;
/*      */   }
/*      */ 
/*      */   public String getName()
/*      */   {
/*  161 */     return this.name;
/*      */   }
/*      */ 
/*      */   public int getModifiers()
/*      */   {
/*  172 */     return this.modifiers;
/*      */   }
/*      */ 
/*      */   public boolean isEnumConstant()
/*      */   {
/*  184 */     return (getModifiers() & 0x4000) != 0;
/*      */   }
/*      */ 
/*      */   public boolean isSynthetic()
/*      */   {
/*  196 */     return Modifier.isSynthetic(getModifiers());
/*      */   }
/*      */ 
/*      */   public Class<?> getType()
/*      */   {
/*  208 */     return this.type;
/*      */   }
/*      */ 
/*      */   public Type getGenericType()
/*      */   {
/*  236 */     if (getGenericSignature() != null) {
/*  237 */       return getGenericInfo().getGenericType();
/*      */     }
/*  239 */     return getType();
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  250 */     if ((paramObject != null) && ((paramObject instanceof Field))) {
/*  251 */       Field localField = (Field)paramObject;
/*  252 */       return (getDeclaringClass() == localField.getDeclaringClass()) && (getName() == localField.getName()) && (getType() == localField.getType());
/*      */     }
/*      */ 
/*  256 */     return false;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/*  265 */     return getDeclaringClass().getName().hashCode() ^ getName().hashCode();
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  287 */     int i = getModifiers();
/*  288 */     return (i == 0 ? "" : new StringBuilder().append(Modifier.toString(i)).append(" ").toString()) + getTypeName(getType()) + " " + getTypeName(getDeclaringClass()) + "." + getName();
/*      */   }
/*      */ 
/*      */   public String toGenericString()
/*      */   {
/*  314 */     int i = getModifiers();
/*  315 */     Type localType = getGenericType();
/*  316 */     return (i == 0 ? "" : new StringBuilder().append(Modifier.toString(i)).append(" ").toString()) + ((localType instanceof Class) ? getTypeName((Class)localType) : localType.toString()) + " " + getTypeName(getDeclaringClass()) + "." + getName();
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public Object get(Object paramObject)
/*      */     throws IllegalArgumentException, IllegalAccessException
/*      */   {
/*  374 */     if ((!this.override) && 
/*  375 */       (!Reflection.quickCheckMemberAccess(this.clazz, this.modifiers))) {
/*  376 */       checkAccess(Reflection.getCallerClass(), this.clazz, paramObject, this.modifiers);
/*      */     }
/*      */ 
/*  379 */     return getFieldAccessor(paramObject).get(paramObject);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public boolean getBoolean(Object paramObject)
/*      */     throws IllegalArgumentException, IllegalAccessException
/*      */   {
/*  408 */     if ((!this.override) && 
/*  409 */       (!Reflection.quickCheckMemberAccess(this.clazz, this.modifiers))) {
/*  410 */       checkAccess(Reflection.getCallerClass(), this.clazz, paramObject, this.modifiers);
/*      */     }
/*      */ 
/*  413 */     return getFieldAccessor(paramObject).getBoolean(paramObject);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public byte getByte(Object paramObject)
/*      */     throws IllegalArgumentException, IllegalAccessException
/*      */   {
/*  442 */     if ((!this.override) && 
/*  443 */       (!Reflection.quickCheckMemberAccess(this.clazz, this.modifiers))) {
/*  444 */       checkAccess(Reflection.getCallerClass(), this.clazz, paramObject, this.modifiers);
/*      */     }
/*      */ 
/*  447 */     return getFieldAccessor(paramObject).getByte(paramObject);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public char getChar(Object paramObject)
/*      */     throws IllegalArgumentException, IllegalAccessException
/*      */   {
/*  478 */     if ((!this.override) && 
/*  479 */       (!Reflection.quickCheckMemberAccess(this.clazz, this.modifiers))) {
/*  480 */       checkAccess(Reflection.getCallerClass(), this.clazz, paramObject, this.modifiers);
/*      */     }
/*      */ 
/*  483 */     return getFieldAccessor(paramObject).getChar(paramObject);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public short getShort(Object paramObject)
/*      */     throws IllegalArgumentException, IllegalAccessException
/*      */   {
/*  514 */     if ((!this.override) && 
/*  515 */       (!Reflection.quickCheckMemberAccess(this.clazz, this.modifiers))) {
/*  516 */       checkAccess(Reflection.getCallerClass(), this.clazz, paramObject, this.modifiers);
/*      */     }
/*      */ 
/*  519 */     return getFieldAccessor(paramObject).getShort(paramObject);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public int getInt(Object paramObject)
/*      */     throws IllegalArgumentException, IllegalAccessException
/*      */   {
/*  550 */     if ((!this.override) && 
/*  551 */       (!Reflection.quickCheckMemberAccess(this.clazz, this.modifiers))) {
/*  552 */       checkAccess(Reflection.getCallerClass(), this.clazz, paramObject, this.modifiers);
/*      */     }
/*      */ 
/*  555 */     return getFieldAccessor(paramObject).getInt(paramObject);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public long getLong(Object paramObject)
/*      */     throws IllegalArgumentException, IllegalAccessException
/*      */   {
/*  586 */     if ((!this.override) && 
/*  587 */       (!Reflection.quickCheckMemberAccess(this.clazz, this.modifiers))) {
/*  588 */       checkAccess(Reflection.getCallerClass(), this.clazz, paramObject, this.modifiers);
/*      */     }
/*      */ 
/*  591 */     return getFieldAccessor(paramObject).getLong(paramObject);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public float getFloat(Object paramObject)
/*      */     throws IllegalArgumentException, IllegalAccessException
/*      */   {
/*  622 */     if ((!this.override) && 
/*  623 */       (!Reflection.quickCheckMemberAccess(this.clazz, this.modifiers))) {
/*  624 */       checkAccess(Reflection.getCallerClass(), this.clazz, paramObject, this.modifiers);
/*      */     }
/*      */ 
/*  627 */     return getFieldAccessor(paramObject).getFloat(paramObject);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public double getDouble(Object paramObject)
/*      */     throws IllegalArgumentException, IllegalAccessException
/*      */   {
/*  658 */     if ((!this.override) && 
/*  659 */       (!Reflection.quickCheckMemberAccess(this.clazz, this.modifiers))) {
/*  660 */       checkAccess(Reflection.getCallerClass(), this.clazz, paramObject, this.modifiers);
/*      */     }
/*      */ 
/*  663 */     return getFieldAccessor(paramObject).getDouble(paramObject);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public void set(Object paramObject1, Object paramObject2)
/*      */     throws IllegalArgumentException, IllegalAccessException
/*      */   {
/*  736 */     if ((!this.override) && 
/*  737 */       (!Reflection.quickCheckMemberAccess(this.clazz, this.modifiers))) {
/*  738 */       checkAccess(Reflection.getCallerClass(), this.clazz, paramObject1, this.modifiers);
/*      */     }
/*      */ 
/*  741 */     getFieldAccessor(paramObject1).set(paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public void setBoolean(Object paramObject, boolean paramBoolean)
/*      */     throws IllegalArgumentException, IllegalAccessException
/*      */   {
/*  772 */     if ((!this.override) && 
/*  773 */       (!Reflection.quickCheckMemberAccess(this.clazz, this.modifiers))) {
/*  774 */       checkAccess(Reflection.getCallerClass(), this.clazz, paramObject, this.modifiers);
/*      */     }
/*      */ 
/*  777 */     getFieldAccessor(paramObject).setBoolean(paramObject, paramBoolean);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public void setByte(Object paramObject, byte paramByte)
/*      */     throws IllegalArgumentException, IllegalAccessException
/*      */   {
/*  808 */     if ((!this.override) && 
/*  809 */       (!Reflection.quickCheckMemberAccess(this.clazz, this.modifiers))) {
/*  810 */       checkAccess(Reflection.getCallerClass(), this.clazz, paramObject, this.modifiers);
/*      */     }
/*      */ 
/*  813 */     getFieldAccessor(paramObject).setByte(paramObject, paramByte);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public void setChar(Object paramObject, char paramChar)
/*      */     throws IllegalArgumentException, IllegalAccessException
/*      */   {
/*  844 */     if ((!this.override) && 
/*  845 */       (!Reflection.quickCheckMemberAccess(this.clazz, this.modifiers))) {
/*  846 */       checkAccess(Reflection.getCallerClass(), this.clazz, paramObject, this.modifiers);
/*      */     }
/*      */ 
/*  849 */     getFieldAccessor(paramObject).setChar(paramObject, paramChar);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public void setShort(Object paramObject, short paramShort)
/*      */     throws IllegalArgumentException, IllegalAccessException
/*      */   {
/*  880 */     if ((!this.override) && 
/*  881 */       (!Reflection.quickCheckMemberAccess(this.clazz, this.modifiers))) {
/*  882 */       checkAccess(Reflection.getCallerClass(), this.clazz, paramObject, this.modifiers);
/*      */     }
/*      */ 
/*  885 */     getFieldAccessor(paramObject).setShort(paramObject, paramShort);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public void setInt(Object paramObject, int paramInt)
/*      */     throws IllegalArgumentException, IllegalAccessException
/*      */   {
/*  916 */     if ((!this.override) && 
/*  917 */       (!Reflection.quickCheckMemberAccess(this.clazz, this.modifiers))) {
/*  918 */       checkAccess(Reflection.getCallerClass(), this.clazz, paramObject, this.modifiers);
/*      */     }
/*      */ 
/*  921 */     getFieldAccessor(paramObject).setInt(paramObject, paramInt);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public void setLong(Object paramObject, long paramLong)
/*      */     throws IllegalArgumentException, IllegalAccessException
/*      */   {
/*  952 */     if ((!this.override) && 
/*  953 */       (!Reflection.quickCheckMemberAccess(this.clazz, this.modifiers))) {
/*  954 */       checkAccess(Reflection.getCallerClass(), this.clazz, paramObject, this.modifiers);
/*      */     }
/*      */ 
/*  957 */     getFieldAccessor(paramObject).setLong(paramObject, paramLong);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public void setFloat(Object paramObject, float paramFloat)
/*      */     throws IllegalArgumentException, IllegalAccessException
/*      */   {
/*  988 */     if ((!this.override) && 
/*  989 */       (!Reflection.quickCheckMemberAccess(this.clazz, this.modifiers))) {
/*  990 */       checkAccess(Reflection.getCallerClass(), this.clazz, paramObject, this.modifiers);
/*      */     }
/*      */ 
/*  993 */     getFieldAccessor(paramObject).setFloat(paramObject, paramFloat);
/*      */   }
/*      */ 
/*      */   @CallerSensitive
/*      */   public void setDouble(Object paramObject, double paramDouble)
/*      */     throws IllegalArgumentException, IllegalAccessException
/*      */   {
/* 1024 */     if ((!this.override) && 
/* 1025 */       (!Reflection.quickCheckMemberAccess(this.clazz, this.modifiers))) {
/* 1026 */       checkAccess(Reflection.getCallerClass(), this.clazz, paramObject, this.modifiers);
/*      */     }
/*      */ 
/* 1029 */     getFieldAccessor(paramObject).setDouble(paramObject, paramDouble);
/*      */   }
/*      */ 
/*      */   private FieldAccessor getFieldAccessor(Object paramObject)
/*      */     throws IllegalAccessException
/*      */   {
/* 1036 */     boolean bool = this.override;
/* 1037 */     FieldAccessor localFieldAccessor = bool ? this.overrideFieldAccessor : this.fieldAccessor;
/* 1038 */     return localFieldAccessor != null ? localFieldAccessor : acquireFieldAccessor(bool);
/*      */   }
/*      */ 
/*      */   private FieldAccessor acquireFieldAccessor(boolean paramBoolean)
/*      */   {
/* 1048 */     FieldAccessor localFieldAccessor = null;
/* 1049 */     if (this.root != null) localFieldAccessor = this.root.getFieldAccessor(paramBoolean);
/* 1050 */     if (localFieldAccessor != null) {
/* 1051 */       if (paramBoolean)
/* 1052 */         this.overrideFieldAccessor = localFieldAccessor;
/*      */       else
/* 1054 */         this.fieldAccessor = localFieldAccessor;
/*      */     }
/*      */     else {
/* 1057 */       localFieldAccessor = reflectionFactory.newFieldAccessor(this, paramBoolean);
/* 1058 */       setFieldAccessor(localFieldAccessor, paramBoolean);
/*      */     }
/*      */ 
/* 1061 */     return localFieldAccessor;
/*      */   }
/*      */ 
/*      */   private FieldAccessor getFieldAccessor(boolean paramBoolean)
/*      */   {
/* 1067 */     return paramBoolean ? this.overrideFieldAccessor : this.fieldAccessor;
/*      */   }
/*      */ 
/*      */   private void setFieldAccessor(FieldAccessor paramFieldAccessor, boolean paramBoolean)
/*      */   {
/* 1073 */     if (paramBoolean)
/* 1074 */       this.overrideFieldAccessor = paramFieldAccessor;
/*      */     else {
/* 1076 */       this.fieldAccessor = paramFieldAccessor;
/*      */     }
/* 1078 */     if (this.root != null)
/* 1079 */       this.root.setFieldAccessor(paramFieldAccessor, paramBoolean);
/*      */   }
/*      */ 
/*      */   static String getTypeName(Class<?> paramClass)
/*      */   {
/* 1087 */     if (paramClass.isArray())
/*      */       try {
/* 1089 */         Object localObject = paramClass;
/* 1090 */         int i = 0;
/* 1091 */         while (((Class)localObject).isArray()) {
/* 1092 */           i++;
/* 1093 */           localObject = ((Class)localObject).getComponentType();
/*      */         }
/* 1095 */         StringBuffer localStringBuffer = new StringBuffer();
/* 1096 */         localStringBuffer.append(((Class)localObject).getName());
/* 1097 */         for (int j = 0; j < i; j++) {
/* 1098 */           localStringBuffer.append("[]");
/*      */         }
/* 1100 */         return localStringBuffer.toString();
/*      */       } catch (Throwable localThrowable) {
/*      */       }
/* 1103 */     return paramClass.getName();
/*      */   }
/*      */ 
/*      */   public <T extends Annotation> T getAnnotation(Class<T> paramClass)
/*      */   {
/* 1111 */     if (paramClass == null) {
/* 1112 */       throw new NullPointerException();
/*      */     }
/* 1114 */     return (Annotation)declaredAnnotations().get(paramClass);
/*      */   }
/*      */ 
/*      */   public Annotation[] getDeclaredAnnotations()
/*      */   {
/* 1121 */     return AnnotationParser.toArray(declaredAnnotations());
/*      */   }
/*      */ 
/*      */   private synchronized Map<Class<? extends Annotation>, Annotation> declaredAnnotations()
/*      */   {
/* 1127 */     if (this.declaredAnnotations == null) {
/* 1128 */       this.declaredAnnotations = AnnotationParser.parseAnnotations(this.annotations, SharedSecrets.getJavaLangAccess().getConstantPool(getDeclaringClass()), getDeclaringClass());
/*      */     }
/*      */ 
/* 1133 */     return this.declaredAnnotations;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.lang.reflect.Field
 * JD-Core Version:    0.6.2
 */