/*     */ package com.sun.xml.internal.bind.api;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Collection;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public final class TypeReference
/*     */ {
/*     */   public final QName tagName;
/*     */   public final Type type;
/*     */   public final Annotation[] annotations;
/*     */ 
/*     */   public TypeReference(QName tagName, Type type, Annotation[] annotations)
/*     */   {
/*  67 */     if ((tagName == null) || (type == null) || (annotations == null)) {
/*  68 */       String nullArgs = "";
/*     */ 
/*  70 */       if (tagName == null) nullArgs = "tagName";
/*  71 */       if (type == null) nullArgs = nullArgs + (nullArgs.length() > 0 ? ", type" : "type");
/*  72 */       if (annotations == null) nullArgs = nullArgs + (nullArgs.length() > 0 ? ", annotations" : "annotations");
/*     */ 
/*  74 */       Messages.ARGUMENT_CANT_BE_NULL.format(new Object[] { nullArgs });
/*     */ 
/*  76 */       throw new IllegalArgumentException(Messages.ARGUMENT_CANT_BE_NULL.format(new Object[] { nullArgs }));
/*     */     }
/*     */ 
/*  79 */     this.tagName = new QName(tagName.getNamespaceURI().intern(), tagName.getLocalPart().intern(), tagName.getPrefix());
/*  80 */     this.type = type;
/*  81 */     this.annotations = annotations;
/*     */   }
/*     */ 
/*     */   public <A extends Annotation> A get(Class<A> annotationType)
/*     */   {
/*  89 */     for (Annotation a : this.annotations) {
/*  90 */       if (a.annotationType() == annotationType)
/*  91 */         return (Annotation)annotationType.cast(a);
/*     */     }
/*  93 */     return null;
/*     */   }
/*     */ 
/*     */   public TypeReference toItemType()
/*     */   {
/* 105 */     Type base = (Type)Utils.REFLECTION_NAVIGATOR.getBaseClass(this.type, Collection.class);
/* 106 */     if (base == null) {
/* 107 */       return this;
/*     */     }
/* 109 */     return new TypeReference(this.tagName, (Type)Utils.REFLECTION_NAVIGATOR.getTypeArgument(base, 0), new Annotation[0]);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.api.TypeReference
 * JD-Core Version:    0.6.2
 */