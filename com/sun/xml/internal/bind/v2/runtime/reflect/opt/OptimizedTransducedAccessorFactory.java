/*     */ package com.sun.xml.internal.bind.v2.runtime.reflect.opt;
/*     */ 
/*     */ import com.sun.xml.internal.bind.Util;
/*     */ import com.sun.xml.internal.bind.v2.bytecode.ClassTailor;
/*     */ import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeClassInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.FieldReflection;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.Accessor.GetterSetterReflection;
/*     */ import com.sun.xml.internal.bind.v2.runtime.reflect.TransducedAccessor;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Modifier;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public abstract class OptimizedTransducedAccessorFactory
/*     */ {
/*  56 */   private static final Logger logger = Util.getClassLogger();
/*     */   private static final String fieldTemplateName;
/*     */   private static final String methodTemplateName;
/*     */   private static final Map<Class, String> suffixMap;
/*     */ 
/*     */   public static final TransducedAccessor get(RuntimePropertyInfo prop)
/*     */   {
/*  76 */     Accessor acc = prop.getAccessor();
/*     */ 
/*  79 */     Class opt = null;
/*     */ 
/*  81 */     TypeInfo parent = prop.parent();
/*  82 */     if (!(parent instanceof RuntimeClassInfo)) {
/*  83 */       return null;
/*     */     }
/*  85 */     Class dc = (Class)((RuntimeClassInfo)parent).getClazz();
/*  86 */     String newClassName = ClassTailor.toVMClassName(dc) + "_JaxbXducedAccessor_" + prop.getName();
/*     */ 
/*  89 */     if ((acc instanceof Accessor.FieldReflection))
/*     */     {
/*  91 */       Accessor.FieldReflection racc = (Accessor.FieldReflection)acc;
/*  92 */       Field field = racc.f;
/*     */ 
/*  94 */       int mods = field.getModifiers();
/*  95 */       if ((Modifier.isPrivate(mods)) || (Modifier.isFinal(mods)))
/*     */       {
/*  98 */         return null;
/*     */       }
/* 100 */       Class t = field.getType();
/* 101 */       if (t.isPrimitive()) {
/* 102 */         opt = AccessorInjector.prepare(dc, fieldTemplateName + (String)suffixMap.get(t), newClassName, new String[] { ClassTailor.toVMClassName(Bean.class), ClassTailor.toVMClassName(dc), "f_" + t.getName(), field.getName() });
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 111 */     if (acc.getClass() == Accessor.GetterSetterReflection.class) {
/* 112 */       Accessor.GetterSetterReflection gacc = (Accessor.GetterSetterReflection)acc;
/*     */ 
/* 114 */       if ((gacc.getter == null) || (gacc.setter == null)) {
/* 115 */         return null;
/*     */       }
/* 117 */       Class t = gacc.getter.getReturnType();
/*     */ 
/* 119 */       if ((Modifier.isPrivate(gacc.getter.getModifiers())) || (Modifier.isPrivate(gacc.setter.getModifiers())))
/*     */       {
/* 122 */         return null;
/*     */       }
/*     */ 
/* 125 */       if (t.isPrimitive()) {
/* 126 */         opt = AccessorInjector.prepare(dc, methodTemplateName + (String)suffixMap.get(t), newClassName, new String[] { ClassTailor.toVMClassName(Bean.class), ClassTailor.toVMClassName(dc), "get_" + t.getName(), gacc.getter.getName(), "set_" + t.getName(), gacc.setter.getName() });
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 137 */     if (opt == null) {
/* 138 */       return null;
/*     */     }
/* 140 */     logger.log(Level.FINE, "Using optimized TransducedAccessor for " + prop.displayName());
/*     */     try
/*     */     {
/* 144 */       return (TransducedAccessor)opt.newInstance();
/*     */     } catch (InstantiationException e) {
/* 146 */       logger.log(Level.INFO, "failed to load an optimized TransducedAccessor", e);
/*     */     } catch (IllegalAccessException e) {
/* 148 */       logger.log(Level.INFO, "failed to load an optimized TransducedAccessor", e);
/*     */     } catch (SecurityException e) {
/* 150 */       logger.log(Level.INFO, "failed to load an optimized TransducedAccessor", e);
/*     */     }
/* 152 */     return null;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  62 */     String s = TransducedAccessor_field_Byte.class.getName();
/*  63 */     fieldTemplateName = s.substring(0, s.length() - "Byte".length()).replace('.', '/');
/*     */ 
/*  65 */     s = TransducedAccessor_method_Byte.class.getName();
/*  66 */     methodTemplateName = s.substring(0, s.length() - "Byte".length()).replace('.', '/');
/*     */ 
/* 155 */     suffixMap = new HashMap();
/*     */ 
/* 158 */     suffixMap.put(Byte.TYPE, "Byte");
/* 159 */     suffixMap.put(Short.TYPE, "Short");
/* 160 */     suffixMap.put(Integer.TYPE, "Integer");
/* 161 */     suffixMap.put(Long.TYPE, "Long");
/* 162 */     suffixMap.put(Boolean.TYPE, "Boolean");
/* 163 */     suffixMap.put(Float.TYPE, "Float");
/* 164 */     suffixMap.put(Double.TYPE, "Double");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.opt.OptimizedTransducedAccessorFactory
 * JD-Core Version:    0.6.2
 */