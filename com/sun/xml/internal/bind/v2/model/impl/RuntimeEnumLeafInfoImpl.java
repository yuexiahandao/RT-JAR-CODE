/*     */ package com.sun.xml.internal.bind.v2.model.impl;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.FieldLocatable;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.Locatable;
/*     */ import com.sun.xml.internal.bind.v2.model.core.NonElement;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeEnumLeafInfo;
/*     */ import com.sun.xml.internal.bind.v2.model.runtime.RuntimeNonElement;
/*     */ import com.sun.xml.internal.bind.v2.runtime.IllegalAnnotationException;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Name;
/*     */ import com.sun.xml.internal.bind.v2.runtime.Transducer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.EnumMap;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ final class RuntimeEnumLeafInfoImpl<T extends Enum<T>, B> extends EnumLeafInfoImpl<Type, Class, Field, Method>
/*     */   implements RuntimeEnumLeafInfo, Transducer<T>
/*     */ {
/*     */   private final Transducer<B> baseXducer;
/*  67 */   private final Map<B, T> parseMap = new HashMap();
/*     */   private final Map<T, B> printMap;
/*     */ 
/*     */   public Transducer<T> getTransducer()
/*     */   {
/*  58 */     return this;
/*     */   }
/*     */ 
/*     */   RuntimeEnumLeafInfoImpl(RuntimeModelBuilder builder, Locatable upstream, Class<T> enumType)
/*     */   {
/*  71 */     super(builder, upstream, enumType, enumType);
/*  72 */     this.printMap = new EnumMap(enumType);
/*     */ 
/*  74 */     this.baseXducer = ((RuntimeNonElement)this.baseType).getTransducer();
/*     */   }
/*     */ 
/*     */   public RuntimeEnumConstantImpl createEnumConstant(String name, String literal, Field constant, EnumConstantImpl<Type, Class, Field, Method> last)
/*     */   {
/*     */     Enum t;
/*     */     try {
/*     */       try {
/*  82 */         constant.setAccessible(true);
/*     */       }
/*     */       catch (SecurityException e)
/*     */       {
/*     */       }
/*     */ 
/*  88 */       t = (Enum)constant.get(null);
/*     */     }
/*     */     catch (IllegalAccessException e) {
/*  91 */       throw new IllegalAccessError(e.getMessage());
/*     */     }
/*     */ 
/*  94 */     Object b = null;
/*     */     try {
/*  96 */       b = this.baseXducer.parse(literal);
/*     */     } catch (Exception e) {
/*  98 */       this.builder.reportError(new IllegalAnnotationException(Messages.INVALID_XML_ENUM_VALUE.format(new Object[] { literal, ((Type)this.baseType.getType()).toString() }), e, new FieldLocatable(this, constant, nav())));
/*     */     }
/*     */ 
/* 103 */     this.parseMap.put(b, t);
/* 104 */     this.printMap.put(t, b);
/*     */ 
/* 106 */     return new RuntimeEnumConstantImpl(this, name, literal, last);
/*     */   }
/*     */ 
/*     */   public QName[] getTypeNames() {
/* 110 */     return new QName[] { getTypeName() };
/*     */   }
/*     */ 
/*     */   public boolean isDefault() {
/* 114 */     return false;
/*     */   }
/*     */ 
/*     */   public Class getClazz()
/*     */   {
/* 119 */     return (Class)this.clazz;
/*     */   }
/*     */ 
/*     */   public boolean useNamespace() {
/* 123 */     return this.baseXducer.useNamespace();
/*     */   }
/*     */ 
/*     */   public void declareNamespace(T t, XMLSerializer w) throws AccessorException {
/* 127 */     this.baseXducer.declareNamespace(this.printMap.get(t), w);
/*     */   }
/*     */ 
/*     */   public CharSequence print(T t) throws AccessorException {
/* 131 */     return this.baseXducer.print(this.printMap.get(t));
/*     */   }
/*     */ 
/*     */   public T parse(CharSequence lexical)
/*     */     throws AccessorException, SAXException
/*     */   {
/* 137 */     Object b = this.baseXducer.parse(lexical);
/*     */ 
/* 139 */     if (this.tokenStringType) {
/* 140 */       b = ((String)b).trim();
/*     */     }
/*     */ 
/* 143 */     return (Enum)this.parseMap.get(b);
/*     */   }
/*     */ 
/*     */   public void writeText(XMLSerializer w, T t, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/* 147 */     this.baseXducer.writeText(w, this.printMap.get(t), fieldName);
/*     */   }
/*     */ 
/*     */   public void writeLeafElement(XMLSerializer w, Name tagName, T o, String fieldName) throws IOException, SAXException, XMLStreamException, AccessorException {
/* 151 */     this.baseXducer.writeLeafElement(w, tagName, this.printMap.get(o), fieldName);
/*     */   }
/*     */ 
/*     */   public QName getTypeName(T instance) {
/* 155 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.RuntimeEnumLeafInfoImpl
 * JD-Core Version:    0.6.2
 */