/*     */ package com.sun.xml.internal.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Location;
/*     */ import java.beans.Introspector;
/*     */ import java.lang.annotation.Annotation;
/*     */ 
/*     */ class GetterSetterPropertySeed<TypeT, ClassDeclT, FieldT, MethodT>
/*     */   implements PropertySeed<TypeT, ClassDeclT, FieldT, MethodT>
/*     */ {
/*     */   protected final MethodT getter;
/*     */   protected final MethodT setter;
/*     */   private ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> parent;
/*     */ 
/*     */   GetterSetterPropertySeed(ClassInfoImpl<TypeT, ClassDeclT, FieldT, MethodT> parent, MethodT getter, MethodT setter)
/*     */   {
/*  49 */     this.parent = parent;
/*  50 */     this.getter = getter;
/*  51 */     this.setter = setter;
/*     */ 
/*  53 */     if ((getter == null) && (setter == null))
/*  54 */       throw new IllegalArgumentException();
/*     */   }
/*     */ 
/*     */   public TypeT getRawType() {
/*  58 */     if (this.getter != null) {
/*  59 */       return this.parent.nav().getReturnType(this.getter);
/*     */     }
/*  61 */     return this.parent.nav().getMethodParameters(this.setter)[0];
/*     */   }
/*     */ 
/*     */   public <A extends Annotation> A readAnnotation(Class<A> annotation) {
/*  65 */     return this.parent.reader().getMethodAnnotation(annotation, this.getter, this.setter, this);
/*     */   }
/*     */ 
/*     */   public boolean hasAnnotation(Class<? extends Annotation> annotationType) {
/*  69 */     return this.parent.reader().hasMethodAnnotation(annotationType, getName(), this.getter, this.setter, this);
/*     */   }
/*     */ 
/*     */   public String getName() {
/*  73 */     if (this.getter != null) {
/*  74 */       return getName(this.getter);
/*     */     }
/*  76 */     return getName(this.setter);
/*     */   }
/*     */ 
/*     */   private String getName(MethodT m) {
/*  80 */     String seed = this.parent.nav().getMethodName(m);
/*  81 */     String lseed = seed.toLowerCase();
/*  82 */     if ((lseed.startsWith("get")) || (lseed.startsWith("set")))
/*  83 */       return camelize(seed.substring(3));
/*  84 */     if (lseed.startsWith("is"))
/*  85 */       return camelize(seed.substring(2));
/*  86 */     return seed;
/*     */   }
/*     */ 
/*     */   private static String camelize(String s)
/*     */   {
/*  91 */     return Introspector.decapitalize(s);
/*     */   }
/*     */ 
/*     */   public Locatable getUpstream()
/*     */   {
/*  98 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public Location getLocation() {
/* 102 */     if (this.getter != null) {
/* 103 */       return this.parent.nav().getMethodLocation(this.getter);
/*     */     }
/* 105 */     return this.parent.nav().getMethodLocation(this.setter);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.GetterSetterPropertySeed
 * JD-Core Version:    0.6.2
 */