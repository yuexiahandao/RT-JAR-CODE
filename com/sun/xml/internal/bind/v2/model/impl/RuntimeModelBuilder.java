/*     */ package com.sun.xml.internal.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.bind.WhiteSpaceProcessor;
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.RuntimeAnnotationReader;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElementRef;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimePropertyInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeTypeInfoSet;
/*     */ import com.sun.xml.internal.bind.v2.runtime.FilterTransducer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
/*     */ import com.sun.xml.internal.bind.v2.runtime.InlineBinaryTransducer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.MimeTypedTransducer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.SchemaTypeTransducer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Transducer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.Map;
/*     */ import javax.activation.MimeType;
/*     */ import javax.xml.namespace.QName;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public class RuntimeModelBuilder extends ModelBuilder<Type, Class, Field, Method>
/*     */ {
/*     */ 
/*     */   @Nullable
/*     */   public final JAXBContextImpl context;
/*     */ 
/*     */   public RuntimeModelBuilder(JAXBContextImpl context, RuntimeAnnotationReader annotationReader, Map<Class, Class> subclassReplacements, String defaultNamespaceRemap)
/*     */   {
/*  76 */     super(annotationReader, Utils.REFLECTION_NAVIGATOR, subclassReplacements, defaultNamespaceRemap);
/*  77 */     this.context = context;
/*     */   }
/*     */ 
/*     */   public RuntimeNonElement getClassInfo(Class clazz, Locatable upstream)
/*     */   {
/*  82 */     return (RuntimeNonElement)super.getClassInfo(clazz, upstream);
/*     */   }
/*     */ 
/*     */   public RuntimeNonElement getClassInfo(Class clazz, boolean searchForSuperClass, Locatable upstream)
/*     */   {
/*  87 */     return (RuntimeNonElement)super.getClassInfo(clazz, searchForSuperClass, upstream);
/*     */   }
/*     */ 
/*     */   protected RuntimeEnumLeafInfoImpl createEnumLeafInfo(Class clazz, Locatable upstream)
/*     */   {
/*  92 */     return new RuntimeEnumLeafInfoImpl(this, upstream, clazz);
/*     */   }
/*     */ 
/*     */   protected RuntimeClassInfoImpl createClassInfo(Class clazz, Locatable upstream)
/*     */   {
/*  97 */     return new RuntimeClassInfoImpl(this, upstream, clazz);
/*     */   }
/*     */ 
/*     */   public RuntimeElementInfoImpl createElementInfo(RegistryInfoImpl<Type, Class, Field, Method> registryInfo, Method method) throws IllegalAnnotationException
/*     */   {
/* 102 */     return new RuntimeElementInfoImpl(this, registryInfo, method);
/*     */   }
/*     */ 
/*     */   public RuntimeArrayInfoImpl createArrayInfo(Locatable upstream, Type arrayType)
/*     */   {
/* 107 */     return new RuntimeArrayInfoImpl(this, upstream, (Class)arrayType);
/*     */   }
/*     */ 
/*     */   protected RuntimeTypeInfoSetImpl createTypeInfoSet()
/*     */   {
/* 112 */     return new RuntimeTypeInfoSetImpl(this.reader);
/*     */   }
/*     */ 
/*     */   public RuntimeTypeInfoSet link()
/*     */   {
/* 117 */     return (RuntimeTypeInfoSet)super.link();
/*     */   }
/*     */ 
/*     */   public static Transducer createTransducer(RuntimeNonElementRef ref)
/*     */   {
/* 129 */     Transducer t = ref.getTarget().getTransducer();
/* 130 */     RuntimePropertyInfo src = ref.getSource();
/* 131 */     ID id = src.id();
/*     */ 
/* 133 */     if (id == ID.IDREF) {
/* 134 */       return RuntimeBuiltinLeafInfoImpl.STRING;
/*     */     }
/* 136 */     if (id == ID.ID) {
/* 137 */       t = new IDTransducerImpl(t);
/*     */     }
/* 139 */     MimeType emt = src.getExpectedMimeType();
/* 140 */     if (emt != null) {
/* 141 */       t = new MimeTypedTransducer(t, emt);
/*     */     }
/* 143 */     if (src.inlineBinaryData()) {
/* 144 */       t = new InlineBinaryTransducer(t);
/*     */     }
/* 146 */     if (src.getSchemaType() != null) {
/* 147 */       if (src.getSchemaType().equals(createXSSimpleType())) {
/* 148 */         return RuntimeBuiltinLeafInfoImpl.STRING;
/*     */       }
/* 150 */       t = new SchemaTypeTransducer(t, src.getSchemaType());
/*     */     }
/*     */ 
/* 153 */     return t;
/*     */   }
/*     */ 
/*     */   private static QName createXSSimpleType() {
/* 157 */     return new QName("http://www.w3.org/2001/XMLSchema", "anySimpleType");
/*     */   }
/*     */ 
/*     */   private static final class IDTransducerImpl<ValueT> extends FilterTransducer<ValueT>
/*     */   {
/*     */     public IDTransducerImpl(Transducer<ValueT> core)
/*     */     {
/* 168 */       super();
/*     */     }
/*     */ 
/*     */     public ValueT parse(CharSequence lexical) throws AccessorException, SAXException
/*     */     {
/* 173 */       String value = WhiteSpaceProcessor.trim(lexical).toString();
/* 174 */       UnmarshallingContext.getInstance().addToIdTable(value);
/* 175 */       return this.core.parse(value);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.RuntimeModelBuilder
 * JD-Core Version:    0.6.2
 */