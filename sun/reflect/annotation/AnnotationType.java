/*     */ package sun.reflect.annotation;
/*     */ 
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.annotation.Inherited;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.reflect.Method;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import sun.misc.JavaLangAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ 
/*     */ public class AnnotationType
/*     */ {
/*     */   private final Map<String, Class<?>> memberTypes;
/*     */   private final Map<String, Object> memberDefaults;
/*     */   private final Map<String, Method> members;
/*     */   private final RetentionPolicy retention;
/*     */   private final boolean inherited;
/*     */ 
/*     */   public static AnnotationType getInstance(Class<? extends Annotation> paramClass)
/*     */   {
/*  82 */     JavaLangAccess localJavaLangAccess = SharedSecrets.getJavaLangAccess();
/*  83 */     AnnotationType localAnnotationType = localJavaLangAccess.getAnnotationType(paramClass);
/*  84 */     if (localAnnotationType == null) {
/*  85 */       localAnnotationType = new AnnotationType(paramClass);
/*     */ 
/*  87 */       if (!localJavaLangAccess.casAnnotationType(paramClass, null, localAnnotationType))
/*     */       {
/*  89 */         localAnnotationType = localJavaLangAccess.getAnnotationType(paramClass);
/*  90 */         assert (localAnnotationType != null);
/*     */       }
/*     */     }
/*     */ 
/*  94 */     return localAnnotationType;
/*     */   }
/*     */ 
/*     */   private AnnotationType(final Class<? extends Annotation> paramClass)
/*     */   {
/* 105 */     if (!paramClass.isAnnotation()) {
/* 106 */       throw new IllegalArgumentException("Not an annotation type");
/*     */     }
/* 108 */     Method[] arrayOfMethod = (Method[])AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public Method[] run()
/*     */       {
/* 112 */         return paramClass.getDeclaredMethods();
/*     */       }
/*     */     });
/* 116 */     this.memberTypes = new HashMap(arrayOfMethod.length + 1, 1.0F);
/* 117 */     this.memberDefaults = new HashMap(0);
/* 118 */     this.members = new HashMap(arrayOfMethod.length + 1, 1.0F);
/*     */ 
/* 120 */     for (Object localObject2 : arrayOfMethod) {
/* 121 */       if (localObject2.getParameterTypes().length != 0)
/* 122 */         throw new IllegalArgumentException(localObject2 + " has params");
/* 123 */       String str = localObject2.getName();
/* 124 */       Class localClass = localObject2.getReturnType();
/* 125 */       this.memberTypes.put(str, invocationHandlerReturnType(localClass));
/* 126 */       this.members.put(str, localObject2);
/*     */ 
/* 128 */       Object localObject3 = localObject2.getDefaultValue();
/* 129 */       if (localObject3 != null) {
/* 130 */         this.memberDefaults.put(str, localObject3);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 135 */     if ((paramClass != Retention.class) && (paramClass != Inherited.class))
/*     */     {
/* 137 */       ??? = SharedSecrets.getJavaLangAccess();
/* 138 */       Map localMap = AnnotationParser.parseSelectAnnotations(((JavaLangAccess)???).getRawClassAnnotations(paramClass), ((JavaLangAccess)???).getConstantPool(paramClass), paramClass, new Class[] { Retention.class, Inherited.class });
/*     */ 
/* 145 */       Retention localRetention = (Retention)localMap.get(Retention.class);
/* 146 */       this.retention = (localRetention == null ? RetentionPolicy.CLASS : localRetention.value());
/* 147 */       this.inherited = localMap.containsKey(Inherited.class);
/*     */     }
/*     */     else {
/* 150 */       this.retention = RetentionPolicy.RUNTIME;
/* 151 */       this.inherited = false;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Class<?> invocationHandlerReturnType(Class<?> paramClass)
/*     */   {
/* 163 */     if (paramClass == Byte.TYPE)
/* 164 */       return Byte.class;
/* 165 */     if (paramClass == Character.TYPE)
/* 166 */       return Character.class;
/* 167 */     if (paramClass == Double.TYPE)
/* 168 */       return Double.class;
/* 169 */     if (paramClass == Float.TYPE)
/* 170 */       return Float.class;
/* 171 */     if (paramClass == Integer.TYPE)
/* 172 */       return Integer.class;
/* 173 */     if (paramClass == Long.TYPE)
/* 174 */       return Long.class;
/* 175 */     if (paramClass == Short.TYPE)
/* 176 */       return Short.class;
/* 177 */     if (paramClass == Boolean.TYPE) {
/* 178 */       return Boolean.class;
/*     */     }
/*     */ 
/* 181 */     return paramClass;
/*     */   }
/*     */ 
/*     */   public Map<String, Class<?>> memberTypes()
/*     */   {
/* 189 */     return this.memberTypes;
/*     */   }
/*     */ 
/*     */   public Map<String, Method> members()
/*     */   {
/* 197 */     return this.members;
/*     */   }
/*     */ 
/*     */   public Map<String, Object> memberDefaults()
/*     */   {
/* 205 */     return this.memberDefaults;
/*     */   }
/*     */ 
/*     */   public RetentionPolicy retention()
/*     */   {
/* 212 */     return this.retention;
/*     */   }
/*     */ 
/*     */   public boolean isInherited()
/*     */   {
/* 219 */     return this.inherited;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 226 */     return "Annotation Type:\n   Member types: " + this.memberTypes + "\n" + "   Member defaults: " + this.memberDefaults + "\n" + "   Retention policy: " + this.retention + "\n" + "   Inherited: " + this.inherited;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.annotation.AnnotationType
 * JD-Core Version:    0.6.2
 */