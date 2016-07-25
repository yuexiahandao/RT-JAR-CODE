/*     */ package sun.reflect;
/*     */ 
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Modifier;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class UnsafeFieldAccessorFactory
/*     */ {
/*     */   static FieldAccessor newFieldAccessor(Field paramField, boolean paramBoolean)
/*     */   {
/*  33 */     Class localClass = paramField.getType();
/*  34 */     boolean bool1 = Modifier.isStatic(paramField.getModifiers());
/*  35 */     boolean bool2 = Modifier.isFinal(paramField.getModifiers());
/*  36 */     boolean bool3 = Modifier.isVolatile(paramField.getModifiers());
/*  37 */     int i = (bool2) || (bool3) ? 1 : 0;
/*  38 */     boolean bool4 = (bool2) && ((bool1) || (!paramBoolean));
/*  39 */     if (bool1)
/*     */     {
/*  43 */       UnsafeFieldAccessorImpl.unsafe.ensureClassInitialized(paramField.getDeclaringClass());
/*     */ 
/*  45 */       if (i == 0) {
/*  46 */         if (localClass == Boolean.TYPE)
/*  47 */           return new UnsafeStaticBooleanFieldAccessorImpl(paramField);
/*  48 */         if (localClass == Byte.TYPE)
/*  49 */           return new UnsafeStaticByteFieldAccessorImpl(paramField);
/*  50 */         if (localClass == Short.TYPE)
/*  51 */           return new UnsafeStaticShortFieldAccessorImpl(paramField);
/*  52 */         if (localClass == Character.TYPE)
/*  53 */           return new UnsafeStaticCharacterFieldAccessorImpl(paramField);
/*  54 */         if (localClass == Integer.TYPE)
/*  55 */           return new UnsafeStaticIntegerFieldAccessorImpl(paramField);
/*  56 */         if (localClass == Long.TYPE)
/*  57 */           return new UnsafeStaticLongFieldAccessorImpl(paramField);
/*  58 */         if (localClass == Float.TYPE)
/*  59 */           return new UnsafeStaticFloatFieldAccessorImpl(paramField);
/*  60 */         if (localClass == Double.TYPE) {
/*  61 */           return new UnsafeStaticDoubleFieldAccessorImpl(paramField);
/*     */         }
/*  63 */         return new UnsafeStaticObjectFieldAccessorImpl(paramField);
/*     */       }
/*     */ 
/*  66 */       if (localClass == Boolean.TYPE)
/*  67 */         return new UnsafeQualifiedStaticBooleanFieldAccessorImpl(paramField, bool4);
/*  68 */       if (localClass == Byte.TYPE)
/*  69 */         return new UnsafeQualifiedStaticByteFieldAccessorImpl(paramField, bool4);
/*  70 */       if (localClass == Short.TYPE)
/*  71 */         return new UnsafeQualifiedStaticShortFieldAccessorImpl(paramField, bool4);
/*  72 */       if (localClass == Character.TYPE)
/*  73 */         return new UnsafeQualifiedStaticCharacterFieldAccessorImpl(paramField, bool4);
/*  74 */       if (localClass == Integer.TYPE)
/*  75 */         return new UnsafeQualifiedStaticIntegerFieldAccessorImpl(paramField, bool4);
/*  76 */       if (localClass == Long.TYPE)
/*  77 */         return new UnsafeQualifiedStaticLongFieldAccessorImpl(paramField, bool4);
/*  78 */       if (localClass == Float.TYPE)
/*  79 */         return new UnsafeQualifiedStaticFloatFieldAccessorImpl(paramField, bool4);
/*  80 */       if (localClass == Double.TYPE) {
/*  81 */         return new UnsafeQualifiedStaticDoubleFieldAccessorImpl(paramField, bool4);
/*     */       }
/*  83 */       return new UnsafeQualifiedStaticObjectFieldAccessorImpl(paramField, bool4);
/*     */     }
/*     */ 
/*  87 */     if (i == 0) {
/*  88 */       if (localClass == Boolean.TYPE)
/*  89 */         return new UnsafeBooleanFieldAccessorImpl(paramField);
/*  90 */       if (localClass == Byte.TYPE)
/*  91 */         return new UnsafeByteFieldAccessorImpl(paramField);
/*  92 */       if (localClass == Short.TYPE)
/*  93 */         return new UnsafeShortFieldAccessorImpl(paramField);
/*  94 */       if (localClass == Character.TYPE)
/*  95 */         return new UnsafeCharacterFieldAccessorImpl(paramField);
/*  96 */       if (localClass == Integer.TYPE)
/*  97 */         return new UnsafeIntegerFieldAccessorImpl(paramField);
/*  98 */       if (localClass == Long.TYPE)
/*  99 */         return new UnsafeLongFieldAccessorImpl(paramField);
/* 100 */       if (localClass == Float.TYPE)
/* 101 */         return new UnsafeFloatFieldAccessorImpl(paramField);
/* 102 */       if (localClass == Double.TYPE) {
/* 103 */         return new UnsafeDoubleFieldAccessorImpl(paramField);
/*     */       }
/* 105 */       return new UnsafeObjectFieldAccessorImpl(paramField);
/*     */     }
/*     */ 
/* 108 */     if (localClass == Boolean.TYPE)
/* 109 */       return new UnsafeQualifiedBooleanFieldAccessorImpl(paramField, bool4);
/* 110 */     if (localClass == Byte.TYPE)
/* 111 */       return new UnsafeQualifiedByteFieldAccessorImpl(paramField, bool4);
/* 112 */     if (localClass == Short.TYPE)
/* 113 */       return new UnsafeQualifiedShortFieldAccessorImpl(paramField, bool4);
/* 114 */     if (localClass == Character.TYPE)
/* 115 */       return new UnsafeQualifiedCharacterFieldAccessorImpl(paramField, bool4);
/* 116 */     if (localClass == Integer.TYPE)
/* 117 */       return new UnsafeQualifiedIntegerFieldAccessorImpl(paramField, bool4);
/* 118 */     if (localClass == Long.TYPE)
/* 119 */       return new UnsafeQualifiedLongFieldAccessorImpl(paramField, bool4);
/* 120 */     if (localClass == Float.TYPE)
/* 121 */       return new UnsafeQualifiedFloatFieldAccessorImpl(paramField, bool4);
/* 122 */     if (localClass == Double.TYPE) {
/* 123 */       return new UnsafeQualifiedDoubleFieldAccessorImpl(paramField, bool4);
/*     */     }
/* 125 */     return new UnsafeQualifiedObjectFieldAccessorImpl(paramField, bool4);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.UnsafeFieldAccessorFactory
 * JD-Core Version:    0.6.2
 */