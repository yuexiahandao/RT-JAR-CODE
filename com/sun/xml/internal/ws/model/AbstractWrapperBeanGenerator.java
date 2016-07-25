/*     */ package com.sun.xml.internal.ws.model;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.bind.api.JAXBRIContext;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.AnnotationReader;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.ws.util.StringUtils;
/*     */ import java.lang.annotation.Annotation;
/*     */ import java.lang.reflect.InvocationHandler;
/*     */ import java.lang.reflect.Method;
/*     */ import java.lang.reflect.Proxy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import java.util.logging.Logger;
/*     */ import javax.jws.WebParam;
/*     */ import javax.jws.WebParam.Mode;
/*     */ import javax.jws.WebResult;
/*     */ import javax.xml.bind.annotation.XmlAttachmentRef;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlList;
/*     */ import javax.xml.bind.annotation.XmlMimeType;
/*     */ import javax.xml.bind.annotation.XmlType;
/*     */ import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public abstract class AbstractWrapperBeanGenerator<T, C, M, A extends Comparable>
/*     */ {
/*  58 */   private static final Logger LOGGER = Logger.getLogger(AbstractWrapperBeanGenerator.class.getName());
/*     */   private static final String RETURN = "return";
/*     */   private static final String EMTPY_NAMESPACE_ID = "";
/*  63 */   private static final Class[] jaxbAnns = { XmlAttachmentRef.class, XmlMimeType.class, XmlJavaTypeAdapter.class, XmlList.class, XmlElement.class };
/*     */ 
/*  68 */   private static final Set<String> skipProperties = new HashSet();
/*     */   private final AnnotationReader<T, C, ?, M> annReader;
/*     */   private final Navigator<T, C, ?, M> nav;
/*     */   private final BeanMemberFactory<T, A> factory;
/*     */   private static final Map<String, String> reservedWords;
/*     */ 
/*     */   protected AbstractWrapperBeanGenerator(AnnotationReader<T, C, ?, M> annReader, Navigator<T, C, ?, M> nav, BeanMemberFactory<T, A> factory)
/*     */   {
/*  83 */     this.annReader = annReader;
/*  84 */     this.nav = nav;
/*  85 */     this.factory = factory;
/*     */   }
/*     */ 
/*     */   private List<Annotation> collectJAXBAnnotations(M method)
/*     */   {
/*  94 */     List jaxbAnnotation = new ArrayList();
/*  95 */     for (Class jaxbClass : jaxbAnns) {
/*  96 */       Annotation ann = this.annReader.getMethodAnnotation(jaxbClass, method, null);
/*  97 */       if (ann != null) {
/*  98 */         jaxbAnnotation.add(ann);
/*     */       }
/*     */     }
/* 101 */     return jaxbAnnotation;
/*     */   }
/*     */ 
/*     */   private List<Annotation> collectJAXBAnnotations(M method, int paramIndex)
/*     */   {
/* 106 */     List jaxbAnnotation = new ArrayList();
/* 107 */     for (Class jaxbClass : jaxbAnns) {
/* 108 */       Annotation ann = this.annReader.getMethodParameterAnnotation(jaxbClass, method, paramIndex, null);
/* 109 */       if (ann != null) {
/* 110 */         jaxbAnnotation.add(ann);
/*     */       }
/*     */     }
/* 113 */     return jaxbAnnotation;
/*     */   }
/*     */ 
/*     */   protected abstract T getSafeType(T paramT);
/*     */ 
/*     */   protected abstract T getHolderValueType(T paramT);
/*     */ 
/*     */   protected abstract boolean isVoidType(T paramT);
/*     */ 
/*     */   public List<A> collectRequestBeanMembers(M method)
/*     */   {
/* 138 */     List requestMembers = new ArrayList();
/* 139 */     int paramIndex = -1;
/*     */ 
/* 141 */     for (Object param : this.nav.getMethodParameters(method)) {
/* 142 */       paramIndex++;
/* 143 */       WebParam webParam = (WebParam)this.annReader.getMethodParameterAnnotation(WebParam.class, method, paramIndex, null);
/* 144 */       if ((webParam == null) || ((!webParam.header()) && (!webParam.mode().equals(WebParam.Mode.OUT))))
/*     */       {
/* 147 */         Object holderType = getHolderValueType(param);
/* 148 */         if ((holderType == null) || (webParam == null) || (!webParam.mode().equals(WebParam.Mode.IN)))
/*     */         {
/* 153 */           Object paramType = holderType != null ? holderType : getSafeType(param);
/* 154 */           String paramName = "arg" + paramIndex;
/*     */ 
/* 156 */           String paramNamespace = (webParam != null) && (webParam.targetNamespace().length() > 0) ? webParam.targetNamespace() : "";
/*     */ 
/* 160 */           List jaxbAnnotation = collectJAXBAnnotations(method, paramIndex);
/*     */ 
/* 163 */           processXmlElement(jaxbAnnotation, paramName, paramNamespace, paramType);
/* 164 */           Comparable member = (Comparable)this.factory.createWrapperBeanMember(paramType, getPropertyName(paramName), jaxbAnnotation);
/*     */ 
/* 166 */           requestMembers.add(member);
/*     */         }
/*     */       }
/*     */     }
/* 168 */     return requestMembers;
/*     */   }
/*     */ 
/*     */   public List<A> collectResponseBeanMembers(M method)
/*     */   {
/* 182 */     List responseMembers = new ArrayList();
/*     */ 
/* 185 */     String responseElementName = "return";
/* 186 */     String responseNamespace = "";
/* 187 */     boolean isResultHeader = false;
/* 188 */     WebResult webResult = (WebResult)this.annReader.getMethodAnnotation(WebResult.class, method, null);
/* 189 */     if (webResult != null) {
/* 190 */       if (webResult.name().length() > 0) {
/* 191 */         responseElementName = webResult.name();
/*     */       }
/* 193 */       if (webResult.targetNamespace().length() > 0) {
/* 194 */         responseNamespace = webResult.targetNamespace();
/*     */       }
/* 196 */       isResultHeader = webResult.header();
/*     */     }
/* 198 */     Object returnType = getSafeType(this.nav.getReturnType(method));
/* 199 */     if ((!isVoidType(returnType)) && (!isResultHeader)) {
/* 200 */       List jaxbRespAnnotations = collectJAXBAnnotations(method);
/* 201 */       processXmlElement(jaxbRespAnnotations, responseElementName, responseNamespace, returnType);
/* 202 */       responseMembers.add(this.factory.createWrapperBeanMember(returnType, getPropertyName(responseElementName), jaxbRespAnnotations));
/*     */     }
/*     */ 
/* 206 */     int paramIndex = -1;
/* 207 */     for (Object param : this.nav.getMethodParameters(method)) {
/* 208 */       paramIndex++;
/*     */ 
/* 210 */       Object paramType = getHolderValueType(param);
/* 211 */       WebParam webParam = (WebParam)this.annReader.getMethodParameterAnnotation(WebParam.class, method, paramIndex, null);
/* 212 */       if ((paramType != null) && ((webParam == null) || (!webParam.header())))
/*     */       {
/* 216 */         String paramName = "arg" + paramIndex;
/*     */ 
/* 218 */         String paramNamespace = (webParam != null) && (webParam.targetNamespace().length() > 0) ? webParam.targetNamespace() : "";
/*     */ 
/* 220 */         List jaxbAnnotation = collectJAXBAnnotations(method, paramIndex);
/* 221 */         processXmlElement(jaxbAnnotation, paramName, paramNamespace, paramType);
/* 222 */         Comparable member = (Comparable)this.factory.createWrapperBeanMember(paramType, getPropertyName(paramName), jaxbAnnotation);
/*     */ 
/* 224 */         responseMembers.add(member);
/*     */       }
/*     */     }
/* 227 */     return responseMembers;
/*     */   }
/*     */ 
/*     */   private void processXmlElement(List<Annotation> jaxb, String elemName, String elemNS, T type) {
/* 231 */     XmlElement elemAnn = null;
/* 232 */     for (Annotation a : jaxb) {
/* 233 */       if (a.annotationType() == XmlElement.class) {
/* 234 */         elemAnn = (XmlElement)a;
/* 235 */         jaxb.remove(a);
/* 236 */         break;
/*     */       }
/*     */     }
/* 239 */     String name = (elemAnn != null) && (!elemAnn.name().equals("##default")) ? elemAnn.name() : elemName;
/*     */ 
/* 242 */     String ns = (elemAnn != null) && (!elemAnn.namespace().equals("##default")) ? elemAnn.namespace() : elemNS;
/*     */ 
/* 245 */     boolean nillable = (this.nav.isArray(type)) || ((elemAnn != null) && (elemAnn.nillable()));
/*     */ 
/* 248 */     boolean required = (elemAnn != null) && (elemAnn.required());
/* 249 */     XmlElementHandler handler = new XmlElementHandler(name, ns, nillable, required);
/* 250 */     XmlElement elem = (XmlElement)Proxy.newProxyInstance(getClass().getClassLoader(), new Class[] { XmlElement.class }, handler);
/* 251 */     jaxb.add(elem);
/*     */   }
/*     */ 
/*     */   public Collection<A> collectExceptionBeanMembers(C exception)
/*     */   {
/* 301 */     TreeMap fields = new TreeMap();
/* 302 */     getExceptionProperties(exception, fields);
/*     */ 
/* 305 */     XmlType xmlType = (XmlType)this.annReader.getClassAnnotation(XmlType.class, exception, null);
/* 306 */     if (xmlType != null) {
/* 307 */       String[] propOrder = xmlType.propOrder();
/*     */ 
/* 309 */       if ((propOrder.length > 0) && (propOrder[0].length() != 0)) {
/* 310 */         List list = new ArrayList();
/* 311 */         for (String prop : propOrder) {
/* 312 */           Comparable a = (Comparable)fields.get(prop);
/* 313 */           if (a != null)
/* 314 */             list.add(a);
/*     */           else {
/* 316 */             throw new WebServiceException("Exception " + exception + " has @XmlType and its propOrder contains unknown property " + prop);
/*     */           }
/*     */         }
/*     */ 
/* 320 */         return list;
/*     */       }
/*     */     }
/*     */ 
/* 324 */     return fields.values();
/*     */   }
/*     */ 
/*     */   private void getExceptionProperties(C exception, TreeMap<String, A> fields)
/*     */   {
/* 329 */     Object sc = this.nav.getSuperClass(exception);
/* 330 */     if (sc != null) {
/* 331 */       getExceptionProperties(sc, fields);
/*     */     }
/* 333 */     Collection methods = this.nav.getDeclaredMethods(exception);
/*     */ 
/* 335 */     for (Iterator i$ = methods.iterator(); i$.hasNext(); ) { Object method = i$.next();
/*     */ 
/* 339 */       if ((this.nav.isPublicMethod(method)) && ((!this.nav.isStaticMethod(method)) || (!this.nav.isFinalMethod(method))) && 
/* 344 */         (this.nav.isPublicMethod(method)))
/*     */       {
/* 348 */         String name = this.nav.getMethodName(method);
/*     */ 
/* 350 */         if (((name.startsWith("get")) || (name.startsWith("is"))) && (!skipProperties.contains(name)) && (!name.equals("get")) && (!name.equals("is")))
/*     */         {
/* 356 */           Object returnType = getSafeType(this.nav.getReturnType(method));
/* 357 */           if (this.nav.getMethodParameters(method).length == 0) {
/* 358 */             String fieldName = name.startsWith("get") ? StringUtils.decapitalize(name.substring(3)) : StringUtils.decapitalize(name.substring(2));
/*     */ 
/* 361 */             fields.put(fieldName, this.factory.createWrapperBeanMember(returnType, fieldName, Collections.emptyList()));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static String getPropertyName(String name)
/*     */   {
/* 373 */     String propertyName = JAXBRIContext.mangleNameToVariableName(name);
/*     */ 
/* 376 */     return getJavaReservedVarialbeName(propertyName);
/*     */   }
/*     */ 
/*     */   @NotNull
/*     */   private static String getJavaReservedVarialbeName(@NotNull String name)
/*     */   {
/* 385 */     String reservedName = (String)reservedWords.get(name);
/* 386 */     return reservedName == null ? name : reservedName;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  70 */     skipProperties.add("getCause");
/*  71 */     skipProperties.add("getLocalizedMessage");
/*  72 */     skipProperties.add("getClass");
/*  73 */     skipProperties.add("getStackTrace");
/*  74 */     skipProperties.add("getSuppressed");
/*     */ 
/* 392 */     reservedWords = new HashMap();
/* 393 */     reservedWords.put("abstract", "_abstract");
/* 394 */     reservedWords.put("assert", "_assert");
/* 395 */     reservedWords.put("boolean", "_boolean");
/* 396 */     reservedWords.put("break", "_break");
/* 397 */     reservedWords.put("byte", "_byte");
/* 398 */     reservedWords.put("case", "_case");
/* 399 */     reservedWords.put("catch", "_catch");
/* 400 */     reservedWords.put("char", "_char");
/* 401 */     reservedWords.put("class", "_class");
/* 402 */     reservedWords.put("const", "_const");
/* 403 */     reservedWords.put("continue", "_continue");
/* 404 */     reservedWords.put("default", "_default");
/* 405 */     reservedWords.put("do", "_do");
/* 406 */     reservedWords.put("double", "_double");
/* 407 */     reservedWords.put("else", "_else");
/* 408 */     reservedWords.put("extends", "_extends");
/* 409 */     reservedWords.put("false", "_false");
/* 410 */     reservedWords.put("final", "_final");
/* 411 */     reservedWords.put("finally", "_finally");
/* 412 */     reservedWords.put("float", "_float");
/* 413 */     reservedWords.put("for", "_for");
/* 414 */     reservedWords.put("goto", "_goto");
/* 415 */     reservedWords.put("if", "_if");
/* 416 */     reservedWords.put("implements", "_implements");
/* 417 */     reservedWords.put("import", "_import");
/* 418 */     reservedWords.put("instanceof", "_instanceof");
/* 419 */     reservedWords.put("int", "_int");
/* 420 */     reservedWords.put("interface", "_interface");
/* 421 */     reservedWords.put("long", "_long");
/* 422 */     reservedWords.put("native", "_native");
/* 423 */     reservedWords.put("new", "_new");
/* 424 */     reservedWords.put("null", "_null");
/* 425 */     reservedWords.put("package", "_package");
/* 426 */     reservedWords.put("private", "_private");
/* 427 */     reservedWords.put("protected", "_protected");
/* 428 */     reservedWords.put("public", "_public");
/* 429 */     reservedWords.put("return", "_return");
/* 430 */     reservedWords.put("short", "_short");
/* 431 */     reservedWords.put("static", "_static");
/* 432 */     reservedWords.put("strictfp", "_strictfp");
/* 433 */     reservedWords.put("super", "_super");
/* 434 */     reservedWords.put("switch", "_switch");
/* 435 */     reservedWords.put("synchronized", "_synchronized");
/* 436 */     reservedWords.put("this", "_this");
/* 437 */     reservedWords.put("throw", "_throw");
/* 438 */     reservedWords.put("throws", "_throws");
/* 439 */     reservedWords.put("transient", "_transient");
/* 440 */     reservedWords.put("true", "_true");
/* 441 */     reservedWords.put("try", "_try");
/* 442 */     reservedWords.put("void", "_void");
/* 443 */     reservedWords.put("volatile", "_volatile");
/* 444 */     reservedWords.put("while", "_while");
/* 445 */     reservedWords.put("enum", "_enum");
/*     */   }
/*     */ 
/*     */   public static abstract interface BeanMemberFactory<T, A>
/*     */   {
/*     */     public abstract A createWrapperBeanMember(T paramT, String paramString, List<Annotation> paramList);
/*     */   }
/*     */ 
/*     */   private static class XmlElementHandler
/*     */     implements InvocationHandler
/*     */   {
/*     */     private String name;
/*     */     private String namespace;
/*     */     private boolean nillable;
/*     */     private boolean required;
/*     */ 
/*     */     XmlElementHandler(String name, String namespace, boolean nillable, boolean required)
/*     */     {
/* 263 */       this.name = name;
/* 264 */       this.namespace = namespace;
/* 265 */       this.nillable = nillable;
/* 266 */       this.required = required;
/*     */     }
/*     */ 
/*     */     public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
/* 270 */       String methodName = method.getName();
/* 271 */       if (methodName.equals("name"))
/* 272 */         return this.name;
/* 273 */       if (methodName.equals("namespace"))
/* 274 */         return this.namespace;
/* 275 */       if (methodName.equals("nillable"))
/* 276 */         return Boolean.valueOf(this.nillable);
/* 277 */       if (methodName.equals("required")) {
/* 278 */         return Boolean.valueOf(this.required);
/*     */       }
/* 280 */       throw new WebServiceException("Not handling " + methodName);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.AbstractWrapperBeanGenerator
 * JD-Core Version:    0.6.2
 */