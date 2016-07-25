/*     */ package com.sun.xml.internal.bind.v2;
/*     */ 
/*     */ import com.sun.istack.internal.FinalArrayList;
/*     */ import com.sun.xml.internal.bind.Util;
/*     */ import com.sun.xml.internal.bind.api.JAXBRIContext;
/*     */ import com.sun.xml.internal.bind.api.TypeReference;
/*     */ import com.sun.xml.internal.bind.v2.model.annotation.RuntimeAnnotationReader;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl.JAXBContextBuilder;
/*     */ import com.sun.xml.internal.bind.v2.util.TypeCast;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ 
/*     */ public class ContextFactory
/*     */ {
/*     */   public static final String USE_JAXB_PROPERTIES = "_useJAXBProperties";
/*     */ 
/*     */   public static JAXBContext createContext(Class[] classes, Map<String, Object> properties)
/*     */     throws JAXBException
/*     */   {
/*  68 */     if (properties == null)
/*  69 */       properties = Collections.emptyMap();
/*     */     else {
/*  71 */       properties = new HashMap(properties);
/*     */     }
/*  73 */     String defaultNsUri = (String)getPropertyValue(properties, "com.sun.xml.internal.bind.defaultNamespaceRemap", String.class);
/*     */ 
/*  75 */     Boolean c14nSupport = (Boolean)getPropertyValue(properties, "com.sun.xml.internal.bind.c14n", Boolean.class);
/*  76 */     if (c14nSupport == null) {
/*  77 */       c14nSupport = Boolean.valueOf(false);
/*     */     }
/*  79 */     Boolean allNillable = (Boolean)getPropertyValue(properties, "com.sun.xml.internal.bind.treatEverythingNillable", Boolean.class);
/*  80 */     if (allNillable == null) {
/*  81 */       allNillable = Boolean.valueOf(false);
/*     */     }
/*  83 */     Boolean retainPropertyInfo = (Boolean)getPropertyValue(properties, "retainReferenceToInfo", Boolean.class);
/*  84 */     if (retainPropertyInfo == null) {
/*  85 */       retainPropertyInfo = Boolean.valueOf(false);
/*     */     }
/*  87 */     Boolean supressAccessorWarnings = (Boolean)getPropertyValue(properties, "supressAccessorWarnings", Boolean.class);
/*  88 */     if (supressAccessorWarnings == null) {
/*  89 */       supressAccessorWarnings = Boolean.valueOf(false);
/*     */     }
/*  91 */     Boolean improvedXsiTypeHandling = (Boolean)getPropertyValue(properties, "com.sun.xml.internal.bind.improvedXsiTypeHandling", Boolean.class);
/*  92 */     if (improvedXsiTypeHandling == null) {
/*  93 */       improvedXsiTypeHandling = Boolean.valueOf(true);
/*     */     }
/*  95 */     Boolean xmlAccessorFactorySupport = (Boolean)getPropertyValue(properties, "com.sun.xml.internal.bind.XmlAccessorFactory", Boolean.class);
/*     */ 
/*  97 */     if (xmlAccessorFactorySupport == null) {
/*  98 */       xmlAccessorFactorySupport = Boolean.valueOf(false);
/*  99 */       Util.getClassLogger().log(Level.FINE, "Property com.sun.xml.internal.bind.XmlAccessorFactoryis not active.  Using JAXB's implementation");
/*     */     }
/*     */ 
/* 104 */     RuntimeAnnotationReader ar = (RuntimeAnnotationReader)getPropertyValue(properties, JAXBRIContext.ANNOTATION_READER, RuntimeAnnotationReader.class);
/*     */     Map subclassReplacements;
/*     */     try
/*     */     {
/* 108 */       subclassReplacements = TypeCast.checkedCast((Map)getPropertyValue(properties, "com.sun.xml.internal.bind.subclassReplacements", Map.class), Class.class, Class.class);
/*     */     }
/*     */     catch (ClassCastException e) {
/* 111 */       throw new JAXBException(Messages.INVALID_TYPE_IN_MAP.format(new Object[0]), e);
/*     */     }
/*     */ 
/* 114 */     if (!properties.isEmpty()) {
/* 115 */       throw new JAXBException(Messages.UNSUPPORTED_PROPERTY.format(new Object[] { properties.keySet().iterator().next() }));
/*     */     }
/*     */ 
/* 118 */     JAXBContextImpl.JAXBContextBuilder builder = new JAXBContextImpl.JAXBContextBuilder();
/* 119 */     builder.setClasses(classes);
/* 120 */     builder.setTypeRefs(Collections.emptyList());
/* 121 */     builder.setSubclassReplacements(subclassReplacements);
/* 122 */     builder.setDefaultNsUri(defaultNsUri);
/* 123 */     builder.setC14NSupport(c14nSupport.booleanValue());
/* 124 */     builder.setAnnotationReader(ar);
/* 125 */     builder.setXmlAccessorFactorySupport(xmlAccessorFactorySupport.booleanValue());
/* 126 */     builder.setAllNillable(allNillable.booleanValue());
/* 127 */     builder.setRetainPropertyInfo(retainPropertyInfo.booleanValue());
/* 128 */     builder.setSupressAccessorWarnings(supressAccessorWarnings.booleanValue());
/* 129 */     builder.setImprovedXsiTypeHandling(improvedXsiTypeHandling.booleanValue());
/* 130 */     return builder.build();
/*     */   }
/*     */ 
/*     */   private static <T> T getPropertyValue(Map<String, Object> properties, String keyName, Class<T> type)
/*     */     throws JAXBException
/*     */   {
/* 137 */     Object o = properties.get(keyName);
/* 138 */     if (o == null) return null;
/*     */ 
/* 140 */     properties.remove(keyName);
/* 141 */     if (!type.isInstance(o)) {
/* 142 */       throw new JAXBException(Messages.INVALID_PROPERTY_VALUE.format(new Object[] { keyName, o }));
/*     */     }
/* 144 */     return type.cast(o);
/*     */   }
/*     */ 
/*     */   public static JAXBRIContext createContext(Class[] classes, Collection<TypeReference> typeRefs, Map<Class, Class> subclassReplacements, String defaultNsUri, boolean c14nSupport, RuntimeAnnotationReader ar, boolean xmlAccessorFactorySupport, boolean allNillable, boolean retainPropertyInfo)
/*     */     throws JAXBException
/*     */   {
/* 152 */     return createContext(classes, typeRefs, subclassReplacements, defaultNsUri, c14nSupport, ar, xmlAccessorFactorySupport, allNillable, retainPropertyInfo, false);
/*     */   }
/*     */ 
/*     */   public static JAXBRIContext createContext(Class[] classes, Collection<TypeReference> typeRefs, Map<Class, Class> subclassReplacements, String defaultNsUri, boolean c14nSupport, RuntimeAnnotationReader ar, boolean xmlAccessorFactorySupport, boolean allNillable, boolean retainPropertyInfo, boolean improvedXsiTypeHandling)
/*     */     throws JAXBException
/*     */   {
/* 162 */     JAXBContextImpl.JAXBContextBuilder builder = new JAXBContextImpl.JAXBContextBuilder();
/* 163 */     builder.setClasses(classes);
/* 164 */     builder.setTypeRefs(typeRefs);
/* 165 */     builder.setSubclassReplacements(subclassReplacements);
/* 166 */     builder.setDefaultNsUri(defaultNsUri);
/* 167 */     builder.setC14NSupport(c14nSupport);
/* 168 */     builder.setAnnotationReader(ar);
/* 169 */     builder.setXmlAccessorFactorySupport(xmlAccessorFactorySupport);
/* 170 */     builder.setAllNillable(allNillable);
/* 171 */     builder.setRetainPropertyInfo(retainPropertyInfo);
/* 172 */     builder.setImprovedXsiTypeHandling(improvedXsiTypeHandling);
/* 173 */     return builder.build();
/*     */   }
/*     */ 
/*     */   public static JAXBContext createContext(String contextPath, ClassLoader classLoader, Map<String, Object> properties)
/*     */     throws JAXBException
/*     */   {
/* 181 */     FinalArrayList classes = new FinalArrayList();
/* 182 */     StringTokenizer tokens = new StringTokenizer(contextPath, ":");
/*     */ 
/* 189 */     while (tokens.hasMoreTokens())
/*     */     {
/*     */       boolean foundJaxbIndex;
/* 190 */       boolean foundObjectFactory = foundJaxbIndex = 0;
/* 191 */       String pkg = tokens.nextToken();
/*     */       try
/*     */       {
/* 196 */         Class o = classLoader.loadClass(pkg + ".ObjectFactory");
/* 197 */         classes.add(o);
/* 198 */         foundObjectFactory = true;
/*     */       }
/*     */       catch (ClassNotFoundException e)
/*     */       {
/*     */       }
/*     */       List indexedClasses;
/*     */       try {
/* 205 */         indexedClasses = loadIndexedClasses(pkg, classLoader);
/*     */       }
/*     */       catch (IOException e) {
/* 208 */         throw new JAXBException(e);
/*     */       }
/* 210 */       if (indexedClasses != null) {
/* 211 */         classes.addAll(indexedClasses);
/* 212 */         foundJaxbIndex = true;
/*     */       }
/*     */ 
/* 215 */       if ((!foundObjectFactory) && (!foundJaxbIndex)) {
/* 216 */         throw new JAXBException(Messages.BROKEN_CONTEXTPATH.format(new Object[] { pkg }));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 221 */     return createContext((Class[])classes.toArray(new Class[classes.size()]), properties);
/*     */   }
/*     */ 
/*     */   private static List<Class> loadIndexedClasses(String pkg, ClassLoader classLoader)
/*     */     throws IOException, JAXBException
/*     */   {
/* 234 */     String resource = pkg.replace('.', '/') + "/jaxb.index";
/* 235 */     InputStream resourceAsStream = classLoader.getResourceAsStream(resource);
/*     */ 
/* 237 */     if (resourceAsStream == null) {
/* 238 */       return null;
/*     */     }
/*     */ 
/* 241 */     BufferedReader in = new BufferedReader(new InputStreamReader(resourceAsStream, "UTF-8"));
/*     */     try
/*     */     {
/* 244 */       FinalArrayList classes = new FinalArrayList();
/* 245 */       String className = in.readLine();
/* 246 */       while (className != null) {
/* 247 */         className = className.trim();
/* 248 */         if ((className.startsWith("#")) || (className.length() == 0)) {
/* 249 */           className = in.readLine();
/*     */         }
/*     */         else
/*     */         {
/* 253 */           if (className.endsWith(".class")) {
/* 254 */             throw new JAXBException(Messages.ILLEGAL_ENTRY.format(new Object[] { className }));
/*     */           }
/*     */           try
/*     */           {
/* 258 */             classes.add(classLoader.loadClass(pkg + '.' + className));
/*     */           } catch (ClassNotFoundException e) {
/* 260 */             throw new JAXBException(Messages.ERROR_LOADING_CLASS.format(new Object[] { className, resource }), e);
/*     */           }
/*     */ 
/* 263 */           className = in.readLine();
/*     */         }
/*     */       }
/* 265 */       return classes;
/*     */     } finally {
/* 267 */       in.close();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.ContextFactory
 * JD-Core Version:    0.6.2
 */