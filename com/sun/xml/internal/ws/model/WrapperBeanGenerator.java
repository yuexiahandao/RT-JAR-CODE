/*     */ package com.sun.xml.internal.ws.model;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.RuntimeInlineAnnotationReader;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.ws.org.objectweb.asm.AnnotationVisitor;
/*     */ import com.sun.xml.internal.ws.org.objectweb.asm.ClassWriter;
/*     */ import com.sun.xml.internal.ws.org.objectweb.asm.FieldVisitor;
/*     */ import com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.GenericArrayType;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.TypeVariable;
/*     */ import java.lang.reflect.WildcardType;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.bind.annotation.XmlAttachmentRef;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlList;
/*     */ import javax.xml.bind.annotation.XmlMimeType;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.Holder;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public class WrapperBeanGenerator
/*     */ {
/*  58 */   private static final Logger LOGGER = Logger.getLogger(WrapperBeanGenerator.class.getName());
/*     */ 
/*  60 */   private static final FieldFactory FIELD_FACTORY = new FieldFactory(null);
/*     */ 
/*  62 */   private static final AbstractWrapperBeanGenerator RUNTIME_GENERATOR = new RuntimeWrapperBeanGenerator(new RuntimeInlineAnnotationReader(), Utils.REFLECTION_NAVIGATOR, FIELD_FACTORY);
/*     */ 
/*     */   private static byte[] createBeanImage(String className, String rootName, String rootNS, String typeName, String typeNS, Collection<Field> fields)
/*     */     throws Exception
/*     */   {
/* 105 */     ClassWriter cw = new ClassWriter(0);
/*     */ 
/* 108 */     cw.visit(49, 33, replaceDotWithSlash(className), null, "java/lang/Object", null);
/*     */ 
/* 110 */     AnnotationVisitor root = cw.visitAnnotation("Ljavax/xml/bind/annotation/XmlRootElement;", true);
/* 111 */     root.visit("name", rootName);
/* 112 */     root.visit("namespace", rootNS);
/* 113 */     root.visitEnd();
/*     */ 
/* 115 */     AnnotationVisitor type = cw.visitAnnotation("Ljavax/xml/bind/annotation/XmlType;", true);
/* 116 */     type.visit("name", typeName);
/* 117 */     type.visit("namespace", typeNS);
/* 118 */     if (fields.size() > 1) {
/* 119 */       AnnotationVisitor propVisitor = type.visitArray("propOrder");
/* 120 */       for (Field field : fields) {
/* 121 */         propVisitor.visit("propOrder", field.fieldName);
/*     */       }
/* 123 */       propVisitor.visitEnd();
/*     */     }
/* 125 */     type.visitEnd();
/*     */ 
/* 127 */     for (Field field : fields) {
/* 128 */       FieldVisitor fv = cw.visitField(1, field.fieldName, field.asmType.getDescriptor(), field.getSignature(), null);
/*     */ 
/* 130 */       for (Annotation ann : field.jaxbAnnotations) {
/* 131 */         if ((ann instanceof XmlMimeType)) {
/* 132 */           AnnotationVisitor mime = fv.visitAnnotation("Ljavax/xml/bind/annotation/XmlMimeType;", true);
/* 133 */           mime.visit("value", ((XmlMimeType)ann).value());
/* 134 */           mime.visitEnd();
/* 135 */         } else if ((ann instanceof XmlJavaTypeAdapter)) {
/* 136 */           AnnotationVisitor ada = fv.visitAnnotation("Ljavax/xml/bind/annotation/adapters/XmlJavaTypeAdapter;", true);
/* 137 */           ada.visit("value", getASMType(((XmlJavaTypeAdapter)ann).value()));
/*     */ 
/* 140 */           ada.visitEnd();
/* 141 */         } else if ((ann instanceof XmlAttachmentRef)) {
/* 142 */           AnnotationVisitor att = fv.visitAnnotation("Ljavax/xml/bind/annotation/XmlAttachmentRef;", true);
/* 143 */           att.visitEnd();
/* 144 */         } else if ((ann instanceof XmlList)) {
/* 145 */           AnnotationVisitor list = fv.visitAnnotation("Ljavax/xml/bind/annotation/XmlList;", true);
/* 146 */           list.visitEnd();
/* 147 */         } else if ((ann instanceof XmlElement)) {
/* 148 */           AnnotationVisitor elem = fv.visitAnnotation("Ljavax/xml/bind/annotation/XmlElement;", true);
/* 149 */           XmlElement xmlElem = (XmlElement)ann;
/* 150 */           elem.visit("name", xmlElem.name());
/* 151 */           elem.visit("namespace", xmlElem.namespace());
/* 152 */           if (xmlElem.nillable()) {
/* 153 */             elem.visit("nillable", Boolean.valueOf(true));
/*     */           }
/* 155 */           if (xmlElem.required()) {
/* 156 */             elem.visit("required", Boolean.valueOf(true));
/*     */           }
/* 158 */           elem.visitEnd();
/*     */         } else {
/* 160 */           throw new WebServiceException("Unknown JAXB annotation " + ann);
/*     */         }
/*     */       }
/*     */ 
/* 164 */       fv.visitEnd();
/*     */     }
/*     */ 
/* 167 */     MethodVisitor mv = cw.visitMethod(1, "<init>", "()V", null, null);
/* 168 */     mv.visitCode();
/* 169 */     mv.visitVarInsn(25, 0);
/* 170 */     mv.visitMethodInsn(183, "java/lang/Object", "<init>", "()V");
/* 171 */     mv.visitInsn(177);
/* 172 */     mv.visitMaxs(1, 1);
/* 173 */     mv.visitEnd();
/*     */ 
/* 175 */     cw.visitEnd();
/*     */ 
/* 177 */     if (LOGGER.isLoggable(Level.FINE))
/*     */     {
/* 179 */       StringBuilder sb = new StringBuilder();
/* 180 */       sb.append("\n");
/* 181 */       sb.append("@XmlRootElement(name=").append(rootName).append(", namespace=").append(rootNS).append(")");
/*     */ 
/* 185 */       sb.append("\n");
/* 186 */       sb.append("@XmlType(name=").append(typeName).append(", namespace=").append(typeNS);
/*     */ 
/* 188 */       if (fields.size() > 1) {
/* 189 */         sb.append(", propOrder={");
/* 190 */         for (Field field : fields) {
/* 191 */           sb.append(" ");
/* 192 */           sb.append(field.fieldName);
/*     */         }
/* 194 */         sb.append(" }");
/*     */       }
/* 196 */       sb.append(")");
/*     */ 
/* 199 */       sb.append("\n");
/* 200 */       sb.append("public class ").append(className).append(" {");
/*     */ 
/* 203 */       for (Field field : fields) {
/* 204 */         sb.append("\n");
/*     */ 
/* 207 */         for (Annotation ann : field.jaxbAnnotations) {
/* 208 */           sb.append("\n    ");
/*     */ 
/* 210 */           if ((ann instanceof XmlMimeType)) {
/* 211 */             sb.append("@XmlMimeType(value=").append(((XmlMimeType)ann).value()).append(")");
/* 212 */           } else if ((ann instanceof XmlJavaTypeAdapter)) {
/* 213 */             sb.append("@XmlJavaTypeAdapter(value=").append(getASMType(((XmlJavaTypeAdapter)ann).value())).append(")");
/* 214 */           } else if ((ann instanceof XmlAttachmentRef)) {
/* 215 */             sb.append("@XmlAttachmentRef");
/* 216 */           } else if ((ann instanceof XmlList)) {
/* 217 */             sb.append("@XmlList");
/* 218 */           } else if ((ann instanceof XmlElement)) {
/* 219 */             XmlElement xmlElem = (XmlElement)ann;
/* 220 */             sb.append("\n    ");
/* 221 */             sb.append("@XmlElement(name=").append(xmlElem.name()).append(", namespace=").append(xmlElem.namespace());
/*     */ 
/* 223 */             if (xmlElem.nillable()) {
/* 224 */               sb.append(", nillable=true");
/*     */             }
/* 226 */             if (xmlElem.required()) {
/* 227 */               sb.append(", required=true");
/*     */             }
/* 229 */             sb.append(")");
/*     */           } else {
/* 231 */             throw new WebServiceException("Unknown JAXB annotation " + ann);
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 236 */         sb.append("\n    ");
/* 237 */         sb.append("public ");
/* 238 */         if (field.getSignature() == null)
/* 239 */           sb.append(field.asmType.getDescriptor());
/*     */         else {
/* 241 */           sb.append(field.getSignature());
/*     */         }
/* 243 */         sb.append(" ");
/* 244 */         sb.append(field.fieldName);
/*     */       }
/*     */ 
/* 247 */       sb.append("\n\n}");
/* 248 */       LOGGER.fine(sb.toString());
/*     */     }
/*     */ 
/* 251 */     return cw.toByteArray();
/*     */   }
/*     */ 
/*     */   private static String replaceDotWithSlash(String name) {
/* 255 */     return name.replace('.', '/');
/*     */   }
/*     */ 
/*     */   static Class createRequestWrapperBean(String className, Method method, QName reqElemName, ClassLoader cl)
/*     */   {
/* 260 */     LOGGER.fine("Request Wrapper Class : " + className);
/*     */ 
/* 262 */     List requestMembers = RUNTIME_GENERATOR.collectRequestBeanMembers(method);
/*     */     byte[] image;
/*     */     try {
/* 267 */       image = createBeanImage(className, reqElemName.getLocalPart(), reqElemName.getNamespaceURI(), reqElemName.getLocalPart(), reqElemName.getNamespaceURI(), requestMembers);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 271 */       throw new WebServiceException(e);
/*     */     }
/*     */ 
/* 274 */     return Injector.inject(cl, className, image);
/*     */   }
/*     */ 
/*     */   static Class createResponseWrapperBean(String className, Method method, QName resElemName, ClassLoader cl) {
/* 279 */     LOGGER.fine("Response Wrapper Class : " + className);
/*     */ 
/* 281 */     List responseMembers = RUNTIME_GENERATOR.collectResponseBeanMembers(method);
/*     */     byte[] image;
/*     */     try {
/* 285 */       image = createBeanImage(className, resElemName.getLocalPart(), resElemName.getNamespaceURI(), resElemName.getLocalPart(), resElemName.getNamespaceURI(), responseMembers);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 289 */       throw new WebServiceException(e);
/*     */     }
/*     */ 
/* 292 */     return Injector.inject(cl, className, image);
/*     */   }
/*     */ 
/*     */   private static com.sun.xml.internal.ws.org.objectweb.asm.Type getASMType(java.lang.reflect.Type t)
/*     */   {
/* 297 */     assert (t != null);
/*     */ 
/* 299 */     if ((t instanceof Class)) {
/* 300 */       return com.sun.xml.internal.ws.org.objectweb.asm.Type.getType((Class)t);
/*     */     }
/*     */ 
/* 303 */     if ((t instanceof ParameterizedType)) {
/* 304 */       ParameterizedType pt = (ParameterizedType)t;
/* 305 */       if ((pt.getRawType() instanceof Class)) {
/* 306 */         return com.sun.xml.internal.ws.org.objectweb.asm.Type.getType((Class)pt.getRawType());
/*     */       }
/*     */     }
/* 309 */     if ((t instanceof GenericArrayType)) {
/* 310 */       return com.sun.xml.internal.ws.org.objectweb.asm.Type.getType(FieldSignature.vms(t));
/*     */     }
/*     */ 
/* 313 */     if ((t instanceof WildcardType)) {
/* 314 */       return com.sun.xml.internal.ws.org.objectweb.asm.Type.getType(FieldSignature.vms(t));
/*     */     }
/*     */ 
/* 317 */     if ((t instanceof TypeVariable)) {
/* 318 */       TypeVariable tv = (TypeVariable)t;
/* 319 */       if ((tv.getBounds()[0] instanceof Class)) {
/* 320 */         return com.sun.xml.internal.ws.org.objectweb.asm.Type.getType((Class)tv.getBounds()[0]);
/*     */       }
/*     */     }
/*     */ 
/* 324 */     throw new IllegalArgumentException("Not creating ASM Type for type = " + t);
/*     */   }
/*     */ 
/*     */   static Class createExceptionBean(String className, Class exception, String typeNS, String elemName, String elemNS, ClassLoader cl)
/*     */   {
/* 330 */     Collection fields = RUNTIME_GENERATOR.collectExceptionBeanMembers(exception);
/*     */     byte[] image;
/*     */     try
/*     */     {
/* 334 */       image = createBeanImage(className, elemName, elemNS, exception.getSimpleName(), typeNS, fields);
/*     */     }
/*     */     catch (Exception e)
/*     */     {
/* 338 */       throw new WebServiceException(e);
/*     */     }
/*     */ 
/* 341 */     return Injector.inject(cl, className, image);
/*     */   }
/*     */   private static class Field implements Comparable<Field> {
/*     */     private final java.lang.reflect.Type reflectType;
/*     */     private final com.sun.xml.internal.ws.org.objectweb.asm.Type asmType;
/*     */     private final String fieldName;
/*     */     private final List<Annotation> jaxbAnnotations;
/*     */ 
/* 352 */     Field(String paramName, java.lang.reflect.Type paramType, com.sun.xml.internal.ws.org.objectweb.asm.Type asmType, List<Annotation> jaxbAnnotations) { this.reflectType = paramType;
/* 353 */       this.asmType = asmType;
/* 354 */       this.fieldName = paramName;
/* 355 */       this.jaxbAnnotations = jaxbAnnotations; }
/*     */ 
/*     */     String getSignature()
/*     */     {
/* 359 */       if ((this.reflectType instanceof Class)) {
/* 360 */         return null;
/*     */       }
/* 362 */       if ((this.reflectType instanceof TypeVariable)) {
/* 363 */         return null;
/*     */       }
/* 365 */       return FieldSignature.vms(this.reflectType);
/*     */     }
/*     */ 
/*     */     public int compareTo(Field o) {
/* 369 */       return this.fieldName.compareTo(o.fieldName);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class FieldFactory
/*     */     implements AbstractWrapperBeanGenerator.BeanMemberFactory<java.lang.reflect.Type, WrapperBeanGenerator.Field>
/*     */   {
/*     */     public WrapperBeanGenerator.Field createWrapperBeanMember(java.lang.reflect.Type paramType, String paramName, List<Annotation> jaxb)
/*     */     {
/*  95 */       return new WrapperBeanGenerator.Field(paramName, paramType, WrapperBeanGenerator.getASMType(paramType), jaxb);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class RuntimeWrapperBeanGenerator extends AbstractWrapperBeanGenerator<java.lang.reflect.Type, Class, Method, WrapperBeanGenerator.Field>
/*     */   {
/*     */     protected RuntimeWrapperBeanGenerator(AnnotationReader<java.lang.reflect.Type, Class, ?, Method> annReader, Navigator<java.lang.reflect.Type, Class, ?, Method> nav, AbstractWrapperBeanGenerator.BeanMemberFactory<java.lang.reflect.Type, WrapperBeanGenerator.Field> beanMemberFactory)
/*     */     {
/*  69 */       super(nav, beanMemberFactory);
/*     */     }
/*     */ 
/*     */     protected java.lang.reflect.Type getSafeType(java.lang.reflect.Type type) {
/*  73 */       return type;
/*     */     }
/*     */ 
/*     */     protected java.lang.reflect.Type getHolderValueType(java.lang.reflect.Type paramType) {
/*  77 */       if ((paramType instanceof ParameterizedType)) {
/*  78 */         ParameterizedType p = (ParameterizedType)paramType;
/*  79 */         if (p.getRawType().equals(Holder.class)) {
/*  80 */           return p.getActualTypeArguments()[0];
/*     */         }
/*     */       }
/*  83 */       return null;
/*     */     }
/*     */ 
/*     */     protected boolean isVoidType(java.lang.reflect.Type type) {
/*  87 */       return type == Void.TYPE;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.WrapperBeanGenerator
 * JD-Core Version:    0.6.2
 */