/*     */ package com.sun.xml.internal.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.internal.bind.WhiteSpaceProcessor;
/*     */ import com.sun.xml.internal.bind.util.Which;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.ClassLocatable;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ErrorHandler;
/*     */ import com.sun.xml.internal.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.internal.bind.v2.model.core.Ref;
/*     */ import com.sun.xml.internal.bind.v2.model.core.RegistryInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.TypeInfoSet;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.annotation.XmlRegistry;
/*     */ import javax.xml.bind.annotation.XmlSchema;
/*     */ import javax.xml.bind.annotation.XmlSeeAlso;
/*     */ import javax.xml.bind.annotation.XmlTransient;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public class ModelBuilder<T, C, F, M>
/*     */ {
/*     */   final TypeInfoSetImpl<T, C, F, M> typeInfoSet;
/*     */   public final AnnotationReader<T, C, F, M> reader;
/*     */   public final Navigator<T, C, F, M> nav;
/*  83 */   private final Map<QName, TypeInfo> typeNames = new HashMap();
/*     */   public final String defaultNsUri;
/* 104 */   final Map<String, RegistryInfoImpl<T, C, F, M>> registries = new HashMap();
/*     */   private final Map<C, C> subclassReplacements;
/*     */   private ErrorHandler errorHandler;
/*     */   private boolean hadError;
/*     */   public boolean hasSwaRef;
/* 121 */   private final ErrorHandler proxyErrorHandler = new ErrorHandler() {
/*     */     public void error(IllegalAnnotationException e) {
/* 123 */       ModelBuilder.this.reportError(e);
/*     */     }
/* 121 */   };
/*     */   private boolean linked;
/*     */ 
/*     */   public ModelBuilder(AnnotationReader<T, C, F, M> reader, Navigator<T, C, F, M> navigator, Map<C, C> subclassReplacements, String defaultNamespaceRemap)
/*     */   {
/* 134 */     this.reader = reader;
/* 135 */     this.nav = navigator;
/* 136 */     this.subclassReplacements = subclassReplacements;
/* 137 */     if (defaultNamespaceRemap == null)
/* 138 */       defaultNamespaceRemap = "";
/* 139 */     this.defaultNsUri = defaultNamespaceRemap;
/* 140 */     reader.setErrorHandler(this.proxyErrorHandler);
/* 141 */     this.typeInfoSet = createTypeInfoSet();
/*     */   }
/*     */ 
/*     */   protected TypeInfoSetImpl<T, C, F, M> createTypeInfoSet()
/*     */   {
/* 186 */     return new TypeInfoSetImpl(this.nav, this.reader, BuiltinLeafInfoImpl.createLeaves(this.nav));
/*     */   }
/*     */ 
/*     */   public NonElement<T, C> getClassInfo(C clazz, Locatable upstream)
/*     */   {
/* 198 */     return getClassInfo(clazz, false, upstream);
/*     */   }
/*     */ 
/*     */   public NonElement<T, C> getClassInfo(C clazz, boolean searchForSuperClass, Locatable upstream)
/*     */   {
/* 207 */     assert (clazz != null);
/* 208 */     NonElement r = this.typeInfoSet.getClassInfo(clazz);
/* 209 */     if (r != null) {
/* 210 */       return r;
/*     */     }
/* 212 */     if (this.nav.isEnum(clazz)) {
/* 213 */       EnumLeafInfoImpl li = createEnumLeafInfo(clazz, upstream);
/* 214 */       this.typeInfoSet.add(li);
/* 215 */       r = li;
/* 216 */       addTypeName(r);
/*     */     } else {
/* 218 */       boolean isReplaced = this.subclassReplacements.containsKey(clazz);
/* 219 */       if ((isReplaced) && (!searchForSuperClass))
/*     */       {
/* 221 */         r = getClassInfo(this.subclassReplacements.get(clazz), upstream);
/*     */       }
/* 223 */       else if ((this.reader.hasClassAnnotation(clazz, XmlTransient.class)) || (isReplaced))
/*     */       {
/* 225 */         r = getClassInfo(this.nav.getSuperClass(clazz), searchForSuperClass, new ClassLocatable(upstream, clazz, this.nav));
/*     */       }
/*     */       else {
/* 228 */         ClassInfoImpl ci = createClassInfo(clazz, upstream);
/* 229 */         this.typeInfoSet.add(ci);
/*     */ 
/* 232 */         for (PropertyInfo p : ci.getProperties()) {
/* 233 */           if (p.kind() == PropertyKind.REFERENCE)
/*     */           {
/* 235 */             String pkg = this.nav.getPackageName(ci.getClazz());
/* 236 */             if (!this.registries.containsKey(pkg))
/*     */             {
/* 238 */               Object c = this.nav.loadObjectFactory(clazz, pkg);
/* 239 */               if (c != null) {
/* 240 */                 addRegistry(c, (Locatable)p);
/*     */               }
/*     */             }
/*     */           }
/* 244 */           for (Iterator i$ = p.ref().iterator(); i$.hasNext(); t = (TypeInfo)i$.next());
/*     */         }
/*     */         TypeInfo t;
/* 247 */         ci.getBaseClass();
/*     */ 
/* 249 */         r = ci;
/* 250 */         addTypeName(r);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 256 */     XmlSeeAlso sa = (XmlSeeAlso)this.reader.getClassAnnotation(XmlSeeAlso.class, clazz, upstream);
/* 257 */     if (sa != null) {
/* 258 */       for (Object t : this.reader.getClassArrayValue(sa, "value")) {
/* 259 */         getTypeInfo(t, (Locatable)sa);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 264 */     return r;
/*     */   }
/*     */ 
/*     */   private void addTypeName(NonElement<T, C> r)
/*     */   {
/* 271 */     QName t = r.getTypeName();
/* 272 */     if (t == null) return;
/*     */ 
/* 274 */     TypeInfo old = (TypeInfo)this.typeNames.put(t, r);
/* 275 */     if (old != null)
/*     */     {
/* 277 */       reportError(new IllegalAnnotationException(Messages.CONFLICTING_XML_TYPE_MAPPING.format(new Object[] { r.getTypeName() }), old, r));
/*     */     }
/*     */   }
/*     */ 
/*     */   public NonElement<T, C> getTypeInfo(T t, Locatable upstream)
/*     */   {
/* 291 */     NonElement r = this.typeInfoSet.getTypeInfo(t);
/* 292 */     if (r != null) return r;
/*     */ 
/* 294 */     if (this.nav.isArray(t)) {
/* 295 */       ArrayInfoImpl ai = createArrayInfo(upstream, t);
/*     */ 
/* 297 */       addTypeName(ai);
/* 298 */       this.typeInfoSet.add(ai);
/* 299 */       return ai;
/*     */     }
/*     */ 
/* 302 */     Object c = this.nav.asDecl(t);
/* 303 */     assert (c != null) : (t.toString() + " must be a leaf, but we failed to recognize it.");
/* 304 */     return getClassInfo(c, upstream);
/*     */   }
/*     */ 
/*     */   public NonElement<T, C> getTypeInfo(Ref<T, C> ref)
/*     */   {
/* 312 */     assert (!ref.valueList);
/* 313 */     Object c = this.nav.asDecl(ref.type);
/* 314 */     if ((c != null) && (this.reader.getClassAnnotation(XmlRegistry.class, c, null) != null)) {
/* 315 */       if (!this.registries.containsKey(this.nav.getPackageName(c)))
/* 316 */         addRegistry(c, null);
/* 317 */       return null;
/*     */     }
/* 319 */     return getTypeInfo(ref.type, null);
/*     */   }
/*     */ 
/*     */   protected EnumLeafInfoImpl<T, C, F, M> createEnumLeafInfo(C clazz, Locatable upstream)
/*     */   {
/* 324 */     return new EnumLeafInfoImpl(this, upstream, clazz, this.nav.use(clazz));
/*     */   }
/*     */ 
/*     */   protected ClassInfoImpl<T, C, F, M> createClassInfo(C clazz, Locatable upstream) {
/* 328 */     return new ClassInfoImpl(this, upstream, clazz);
/*     */   }
/*     */ 
/*     */   protected ElementInfoImpl<T, C, F, M> createElementInfo(RegistryInfoImpl<T, C, F, M> registryInfo, M m) throws IllegalAnnotationException
/*     */   {
/* 333 */     return new ElementInfoImpl(this, registryInfo, m);
/*     */   }
/*     */ 
/*     */   protected ArrayInfoImpl<T, C, F, M> createArrayInfo(Locatable upstream, T arrayType) {
/* 337 */     return new ArrayInfoImpl(this, upstream, arrayType);
/*     */   }
/*     */ 
/*     */   public RegistryInfo<T, C> addRegistry(C registryClass, Locatable upstream)
/*     */   {
/* 346 */     return new RegistryInfoImpl(this, upstream, registryClass);
/*     */   }
/*     */ 
/*     */   public RegistryInfo<T, C> getRegistry(String packageName)
/*     */   {
/* 358 */     return (RegistryInfo)this.registries.get(packageName);
/*     */   }
/*     */ 
/*     */   public TypeInfoSet<T, C, F, M> link()
/*     */   {
/* 376 */     assert (!this.linked);
/* 377 */     this.linked = true;
/*     */ 
/* 379 */     for (ElementInfoImpl ei : this.typeInfoSet.getAllElements()) {
/* 380 */       ei.link();
/*     */     }
/* 382 */     for (ClassInfoImpl ci : this.typeInfoSet.beans().values()) {
/* 383 */       ci.link();
/*     */     }
/* 385 */     for (EnumLeafInfoImpl li : this.typeInfoSet.enums().values()) {
/* 386 */       li.link();
/*     */     }
/* 388 */     if (this.hadError) {
/* 389 */       return null;
/*     */     }
/* 391 */     return this.typeInfoSet;
/*     */   }
/*     */ 
/*     */   public void setErrorHandler(ErrorHandler errorHandler)
/*     */   {
/* 407 */     this.errorHandler = errorHandler;
/*     */   }
/*     */ 
/*     */   public final void reportError(IllegalAnnotationException e) {
/* 411 */     this.hadError = true;
/* 412 */     if (this.errorHandler != null)
/* 413 */       this.errorHandler.error(e);
/*     */   }
/*     */ 
/*     */   public boolean isReplaced(C sc) {
/* 417 */     return this.subclassReplacements.containsKey(sc);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/* 150 */       XmlSchema s = null;
/* 151 */       s.location();
/*     */     }
/*     */     catch (NullPointerException e)
/*     */     {
/*     */     }
/*     */     catch (NoSuchMethodError e)
/*     */     {
/*     */       Messages res;
/*     */       Messages res;
/* 157 */       if (XmlSchema.class.getClassLoader() == null)
/* 158 */         res = Messages.INCOMPATIBLE_API_VERSION_MUSTANG;
/*     */       else {
/* 160 */         res = Messages.INCOMPATIBLE_API_VERSION;
/*     */       }
/* 162 */       throw new LinkageError(res.format(new Object[] { Which.which(XmlSchema.class), Which.which(ModelBuilder.class) }));
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 175 */       WhiteSpaceProcessor.isWhiteSpace("xyz");
/*     */     }
/*     */     catch (NoSuchMethodError e) {
/* 178 */       throw new LinkageError(Messages.RUNNING_WITH_1_0_RUNTIME.format(new Object[] { Which.which(WhiteSpaceProcessor.class), Which.which(ModelBuilder.class) }));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.ModelBuilder
 * JD-Core Version:    0.6.2
 */