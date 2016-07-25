/*     */ package com.sun.xml.internal.bind.v2.runtime.reflect.opt;
/*     */ 
/*     */ import com.sun.xml.internal.bind.Util;
/*     */ import com.sun.xml.internal.bind.v2.bytecode.ClassTailor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.RuntimeUtil;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public abstract class OptimizedAccessorFactory
/*     */ {
/*  47 */   private static final Logger logger = Util.getClassLogger();
/*     */   private static final String fieldTemplateName;
/*  58 */   private static final String methodTemplateName = s.substring(0, s.length() - "Byte".length()).replace('.', '/');
/*     */ 
/*     */   public static final <B, V> Accessor<B, V> get(Method getter, Method setter)
/*     */   {
/*  69 */     if (getter.getParameterTypes().length != 0)
/*  70 */       return null;
/*  71 */     Class[] sparams = setter.getParameterTypes();
/*  72 */     if (sparams.length != 1)
/*  73 */       return null;
/*  74 */     if (sparams[0] != getter.getReturnType())
/*  75 */       return null;
/*  76 */     if (setter.getReturnType() != Void.TYPE)
/*  77 */       return null;
/*  78 */     if (getter.getDeclaringClass() != setter.getDeclaringClass())
/*  79 */       return null;
/*  80 */     if ((Modifier.isPrivate(getter.getModifiers())) || (Modifier.isPrivate(setter.getModifiers())))
/*     */     {
/*  82 */       return null;
/*     */     }
/*  84 */     Class t = sparams[0];
/*  85 */     String typeName = t.getName().replace('.', '_');
/*  86 */     if (t.isArray()) {
/*  87 */       typeName = "AOf_";
/*  88 */       String compName = t.getComponentType().getName().replace('.', '_');
/*  89 */       while (compName.startsWith("[L")) {
/*  90 */         compName = compName.substring(2);
/*  91 */         typeName = typeName + "AOf_";
/*     */       }
/*  93 */       typeName = typeName + compName;
/*     */     }
/*     */ 
/*  96 */     String newClassName = ClassTailor.toVMClassName(getter.getDeclaringClass()) + "$JaxbAccessorM_" + getter.getName() + '_' + setter.getName() + '_' + typeName;
/*     */     Class opt;
/*     */     Class opt;
/*  99 */     if (t.isPrimitive()) {
/* 100 */       opt = AccessorInjector.prepare(getter.getDeclaringClass(), methodTemplateName + ((Class)RuntimeUtil.primitiveToBox.get(t)).getSimpleName(), newClassName, new String[] { ClassTailor.toVMClassName(Bean.class), ClassTailor.toVMClassName(getter.getDeclaringClass()), "get_" + t.getName(), getter.getName(), "set_" + t.getName(), setter.getName() });
/*     */     }
/*     */     else
/*     */     {
/* 110 */       opt = AccessorInjector.prepare(getter.getDeclaringClass(), methodTemplateName + "Ref", newClassName, new String[] { ClassTailor.toVMClassName(Bean.class), ClassTailor.toVMClassName(getter.getDeclaringClass()), ClassTailor.toVMClassName(Ref.class), ClassTailor.toVMClassName(t), "()" + ClassTailor.toVMTypeName(Ref.class), "()" + ClassTailor.toVMTypeName(t), '(' + ClassTailor.toVMTypeName(Ref.class) + ")V", '(' + ClassTailor.toVMTypeName(t) + ")V", "get_ref", getter.getName(), "set_ref", setter.getName() });
/*     */     }
/*     */ 
/* 126 */     if (opt == null) {
/* 127 */       return null;
/*     */     }
/* 129 */     Accessor acc = instanciate(opt);
/* 130 */     if (acc != null)
/* 131 */       logger.log(Level.FINE, "Using optimized Accessor for " + getter + " and " + setter);
/* 132 */     return acc;
/*     */   }
/*     */ 
/*     */   public static final <B, V> Accessor<B, V> get(Field field)
/*     */   {
/* 143 */     int mods = field.getModifiers();
/* 144 */     if ((Modifier.isPrivate(mods)) || (Modifier.isFinal(mods)))
/*     */     {
/* 146 */       return null;
/*     */     }
/* 148 */     String newClassName = ClassTailor.toVMClassName(field.getDeclaringClass()) + "$JaxbAccessorF_" + field.getName();
/*     */     Class opt;
/*     */     Class opt;
/* 152 */     if (field.getType().isPrimitive()) {
/* 153 */       opt = AccessorInjector.prepare(field.getDeclaringClass(), fieldTemplateName + ((Class)RuntimeUtil.primitiveToBox.get(field.getType())).getSimpleName(), newClassName, new String[] { ClassTailor.toVMClassName(Bean.class), ClassTailor.toVMClassName(field.getDeclaringClass()), "f_" + field.getType().getName(), field.getName() });
/*     */     }
/*     */     else
/*     */     {
/* 161 */       opt = AccessorInjector.prepare(field.getDeclaringClass(), fieldTemplateName + "Ref", newClassName, new String[] { ClassTailor.toVMClassName(Bean.class), ClassTailor.toVMClassName(field.getDeclaringClass()), ClassTailor.toVMClassName(Ref.class), ClassTailor.toVMClassName(field.getType()), ClassTailor.toVMTypeName(Ref.class), ClassTailor.toVMTypeName(field.getType()), "f_ref", field.getName() });
/*     */     }
/*     */ 
/* 173 */     if (opt == null) {
/* 174 */       return null;
/*     */     }
/* 176 */     Accessor acc = instanciate(opt);
/* 177 */     if (acc != null)
/* 178 */       logger.log(Level.FINE, "Using optimized Accessor for " + field);
/* 179 */     return acc;
/*     */   }
/*     */ 
/*     */   private static <B, V> Accessor<B, V> instanciate(Class opt) {
/*     */     try {
/* 184 */       return (Accessor)opt.newInstance();
/*     */     } catch (InstantiationException e) {
/* 186 */       logger.log(Level.INFO, "failed to load an optimized Accessor", e);
/*     */     } catch (IllegalAccessException e) {
/* 188 */       logger.log(Level.INFO, "failed to load an optimized Accessor", e);
/*     */     } catch (SecurityException e) {
/* 190 */       logger.log(Level.INFO, "failed to load an optimized Accessor", e);
/*     */     }
/* 192 */     return null;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  54 */     String s = FieldAccessor_Byte.class.getName();
/*  55 */     fieldTemplateName = s.substring(0, s.length() - "Byte".length()).replace('.', '/');
/*     */ 
/*  57 */     s = MethodAccessor_Byte.class.getName();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.opt.OptimizedAccessorFactory
 * JD-Core Version:    0.6.2
 */