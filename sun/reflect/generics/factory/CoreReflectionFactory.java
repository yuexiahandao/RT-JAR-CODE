/*     */ package sun.reflect.generics.factory;
/*     */ 
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.GenericDeclaration;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import sun.reflect.generics.reflectiveObjects.GenericArrayTypeImpl;
/*     */ import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;
/*     */ import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;
/*     */ import sun.reflect.generics.reflectiveObjects.WildcardTypeImpl;
/*     */ import sun.reflect.generics.scope.Scope;
/*     */ import sun.reflect.generics.tree.FieldTypeSignature;
/*     */ 
/*     */ public class CoreReflectionFactory
/*     */   implements GenericsFactory
/*     */ {
/*     */   private GenericDeclaration decl;
/*     */   private Scope scope;
/*     */ 
/*     */   private CoreReflectionFactory(GenericDeclaration paramGenericDeclaration, Scope paramScope)
/*     */   {
/*  52 */     this.decl = paramGenericDeclaration;
/*  53 */     this.scope = paramScope;
/*     */   }
/*     */   private GenericDeclaration getDecl() {
/*  56 */     return this.decl;
/*     */   }
/*  58 */   private Scope getScope() { return this.scope; }
/*     */ 
/*     */   private ClassLoader getDeclsLoader()
/*     */   {
/*  62 */     if ((this.decl instanceof Class)) return ((Class)this.decl).getClassLoader();
/*  63 */     if ((this.decl instanceof Method)) {
/*  64 */       return ((Method)this.decl).getDeclaringClass().getClassLoader();
/*     */     }
/*  66 */     assert ((this.decl instanceof Constructor)) : "Constructor expected";
/*  67 */     return ((Constructor)this.decl).getDeclaringClass().getClassLoader();
/*     */   }
/*     */ 
/*     */   public static CoreReflectionFactory make(GenericDeclaration paramGenericDeclaration, Scope paramScope)
/*     */   {
/*  89 */     return new CoreReflectionFactory(paramGenericDeclaration, paramScope);
/*     */   }
/*     */ 
/*     */   public TypeVariable<?> makeTypeVariable(String paramString, FieldTypeSignature[] paramArrayOfFieldTypeSignature)
/*     */   {
/*  94 */     return TypeVariableImpl.make(getDecl(), paramString, paramArrayOfFieldTypeSignature, this);
/*     */   }
/*     */ 
/*     */   public WildcardType makeWildcard(FieldTypeSignature[] paramArrayOfFieldTypeSignature1, FieldTypeSignature[] paramArrayOfFieldTypeSignature2)
/*     */   {
/*  99 */     return WildcardTypeImpl.make(paramArrayOfFieldTypeSignature1, paramArrayOfFieldTypeSignature2, this);
/*     */   }
/*     */ 
/*     */   public ParameterizedType makeParameterizedType(Type paramType1, Type[] paramArrayOfType, Type paramType2)
/*     */   {
/* 105 */     return ParameterizedTypeImpl.make((Class)paramType1, paramArrayOfType, paramType2);
/*     */   }
/*     */ 
/*     */   public TypeVariable<?> findTypeVariable(String paramString)
/*     */   {
/* 110 */     return getScope().lookup(paramString);
/*     */   }
/*     */   public Type makeNamedType(String paramString) {
/*     */     try {
/* 114 */       return Class.forName(paramString, false, getDeclsLoader());
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {
/* 117 */       throw new TypeNotPresentException(paramString, localClassNotFoundException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Type makeArrayType(Type paramType) {
/* 122 */     if ((paramType instanceof Class)) {
/* 123 */       return Array.newInstance((Class)paramType, 0).getClass();
/*     */     }
/* 125 */     return GenericArrayTypeImpl.make(paramType);
/*     */   }
/*     */   public Type makeByte() {
/* 128 */     return Byte.TYPE; } 
/* 129 */   public Type makeBool() { return Boolean.TYPE; } 
/* 130 */   public Type makeShort() { return Short.TYPE; } 
/* 131 */   public Type makeChar() { return Character.TYPE; } 
/* 132 */   public Type makeInt() { return Integer.TYPE; } 
/* 133 */   public Type makeLong() { return Long.TYPE; } 
/* 134 */   public Type makeFloat() { return Float.TYPE; } 
/* 135 */   public Type makeDouble() { return Double.TYPE; } 
/*     */   public Type makeVoid() {
/* 137 */     return Void.TYPE;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.factory.CoreReflectionFactory
 * JD-Core Version:    0.6.2
 */