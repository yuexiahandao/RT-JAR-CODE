/*     */ package com.sun.xml.internal.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.MethodLocatable;
/*     */ import com.sun.xml.internal.bind.v2.model.core.RegistryInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Location;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.bind.annotation.XmlElementDecl;
/*     */ 
/*     */ final class RegistryInfoImpl<T, C, F, M>
/*     */   implements Locatable, RegistryInfo<T, C>
/*     */ {
/*     */   final C registryClass;
/*     */   private final Locatable upstream;
/*     */   private final Navigator<T, C, F, M> nav;
/*  59 */   private final Set<TypeInfo<T, C>> references = new LinkedHashSet();
/*     */ 
/*     */   RegistryInfoImpl(ModelBuilder<T, C, F, M> builder, Locatable upstream, C registryClass)
/*     */   {
/*  65 */     this.nav = builder.nav;
/*  66 */     this.registryClass = registryClass;
/*  67 */     this.upstream = upstream;
/*  68 */     builder.registries.put(getPackageName(), this);
/*     */ 
/*  70 */     if (this.nav.getDeclaredField(registryClass, "_useJAXBProperties") != null)
/*     */     {
/*  73 */       builder.reportError(new IllegalAnnotationException(Messages.MISSING_JAXB_PROPERTIES.format(new Object[] { getPackageName() }), this));
/*     */ 
/*  78 */       return;
/*     */     }
/*     */ 
/*  81 */     for (Iterator i$ = this.nav.getDeclaredMethods(registryClass).iterator(); i$.hasNext(); ) { Object m = i$.next();
/*  82 */       XmlElementDecl em = (XmlElementDecl)builder.reader.getMethodAnnotation(XmlElementDecl.class, m, this);
/*     */ 
/*  85 */       if (em == null) {
/*  86 */         if (this.nav.getMethodName(m).startsWith("create"))
/*     */         {
/*  88 */           this.references.add(builder.getTypeInfo(this.nav.getReturnType(m), new MethodLocatable(this, m, this.nav)));
/*     */         }
/*     */ 
/*     */       }
/*     */       else
/*     */       {
/*     */         ElementInfoImpl ei;
/*     */         try
/*     */         {
/*  98 */           ei = builder.createElementInfo(this, m);
/*     */         } catch (IllegalAnnotationException e) {
/* 100 */           builder.reportError(e);
/* 101 */         }continue;
/*     */ 
/* 106 */         builder.typeInfoSet.add(ei, builder);
/* 107 */         this.references.add(ei);
/*     */       } }
/*     */   }
/*     */ 
/*     */   public Locatable getUpstream() {
/* 112 */     return this.upstream;
/*     */   }
/*     */ 
/*     */   public Location getLocation() {
/* 116 */     return this.nav.getClassLocation(this.registryClass);
/*     */   }
/*     */ 
/*     */   public Set<TypeInfo<T, C>> getReferences() {
/* 120 */     return this.references;
/*     */   }
/*     */ 
/*     */   public String getPackageName()
/*     */   {
/* 127 */     return this.nav.getPackageName(this.registryClass);
/*     */   }
/*     */ 
/*     */   public C getClazz() {
/* 131 */     return this.registryClass;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.RegistryInfoImpl
 * JD-Core Version:    0.6.2
 */