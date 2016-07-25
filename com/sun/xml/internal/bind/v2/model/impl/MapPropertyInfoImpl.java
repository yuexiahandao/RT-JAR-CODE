/*     */ package com.sun.xml.internal.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.model.core.MapPropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.internal.bind.v2.model.core.PropertyKind;
/*     */ import com.sun.xml.internal.bind.v2.model.core.TypeInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import javax.xml.bind.annotation.XmlElementWrapper;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ class MapPropertyInfoImpl<T, C, F, M> extends PropertyInfoImpl<T, C, F, M>
/*     */   implements MapPropertyInfo<T, C>
/*     */ {
/*     */   private final QName xmlName;
/*     */   private boolean nil;
/*     */   private final T keyType;
/*     */   private final T valueType;
/*     */   private NonElement<T, C> keyTypeInfo;
/*     */   private NonElement<T, C> valueTypeInfo;
/*     */ 
/*     */   public MapPropertyInfoImpl(ClassInfoImpl<T, C, F, M> ci, PropertySeed<T, C, F, M> seed)
/*     */   {
/*  56 */     super(ci, seed);
/*     */ 
/*  58 */     XmlElementWrapper xe = (XmlElementWrapper)seed.readAnnotation(XmlElementWrapper.class);
/*  59 */     this.xmlName = calcXmlName(xe);
/*  60 */     this.nil = ((xe != null) && (xe.nillable()));
/*     */ 
/*  62 */     Object raw = getRawType();
/*  63 */     Object bt = nav().getBaseClass(raw, nav().asDecl(Map.class));
/*  64 */     assert (bt != null);
/*     */ 
/*  66 */     if (nav().isParameterizedType(bt)) {
/*  67 */       this.keyType = nav().getTypeArgument(bt, 0);
/*  68 */       this.valueType = nav().getTypeArgument(bt, 1);
/*     */     } else {
/*  70 */       this.keyType = (this.valueType = nav().ref(Object.class));
/*     */     }
/*     */   }
/*     */ 
/*     */   public Collection<? extends TypeInfo<T, C>> ref() {
/*  75 */     return Arrays.asList(new NonElement[] { getKeyType(), getValueType() });
/*     */   }
/*     */ 
/*     */   public final PropertyKind kind() {
/*  79 */     return PropertyKind.MAP;
/*     */   }
/*     */ 
/*     */   public QName getXmlName() {
/*  83 */     return this.xmlName;
/*     */   }
/*     */ 
/*     */   public boolean isCollectionNillable() {
/*  87 */     return this.nil;
/*     */   }
/*     */ 
/*     */   public NonElement<T, C> getKeyType() {
/*  91 */     if (this.keyTypeInfo == null)
/*  92 */       this.keyTypeInfo = getTarget(this.keyType);
/*  93 */     return this.keyTypeInfo;
/*     */   }
/*     */ 
/*     */   public NonElement<T, C> getValueType() {
/*  97 */     if (this.valueTypeInfo == null)
/*  98 */       this.valueTypeInfo = getTarget(this.valueType);
/*  99 */     return this.valueTypeInfo;
/*     */   }
/*     */ 
/*     */   public NonElement<T, C> getTarget(T type) {
/* 103 */     assert (this.parent.builder != null) : "this method must be called during the build stage";
/* 104 */     return this.parent.builder.getTypeInfo(type, this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.MapPropertyInfoImpl
 * JD-Core Version:    0.6.2
 */