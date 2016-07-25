/*     */ package com.sun.xml.internal.bind.v2.model.impl;
/*     */ 
/*     */ import java.text.MessageFormat;
/*     */ import java.util.ResourceBundle;
/*     */ 
/*     */  enum Messages
/*     */ {
/*  36 */   ID_MUST_BE_STRING, 
/*     */ 
/*  38 */   MUTUALLY_EXCLUSIVE_ANNOTATIONS, 
/*  39 */   DUPLICATE_ANNOTATIONS, 
/*  40 */   NO_DEFAULT_CONSTRUCTOR, 
/*  41 */   CANT_HANDLE_INTERFACE, 
/*  42 */   CANT_HANDLE_INNER_CLASS, 
/*  43 */   ANNOTATION_ON_WRONG_METHOD, 
/*  44 */   GETTER_SETTER_INCOMPATIBLE_TYPE, 
/*  45 */   DUPLICATE_ENTRY_IN_PROP_ORDER, 
/*  46 */   DUPLICATE_PROPERTIES, 
/*     */ 
/*  48 */   XML_ELEMENT_MAPPING_ON_NON_IXMLELEMENT_METHOD, 
/*  49 */   SCOPE_IS_NOT_COMPLEXTYPE, 
/*  50 */   CONFLICTING_XML_ELEMENT_MAPPING, 
/*     */ 
/*  52 */   REFERENCE_TO_NON_ELEMENT, 
/*     */ 
/*  54 */   NON_EXISTENT_ELEMENT_MAPPING, 
/*     */ 
/*  56 */   TWO_ATTRIBUTE_WILDCARDS, 
/*  57 */   SUPER_CLASS_HAS_WILDCARD, 
/*  58 */   INVALID_ATTRIBUTE_WILDCARD_TYPE, 
/*  59 */   PROPERTY_MISSING_FROM_ORDER, 
/*  60 */   PROPERTY_ORDER_CONTAINS_UNUSED_ENTRY, 
/*     */ 
/*  62 */   INVALID_XML_ENUM_VALUE, 
/*  63 */   NO_IMAGE_WRITER, 
/*     */ 
/*  65 */   ILLEGAL_MIME_TYPE, 
/*  66 */   ILLEGAL_ANNOTATION, 
/*     */ 
/*  68 */   MULTIPLE_VALUE_PROPERTY, 
/*  69 */   ELEMENT_AND_VALUE_PROPERTY, 
/*  70 */   CONFLICTING_XML_TYPE_MAPPING, 
/*  71 */   XMLVALUE_IN_DERIVED_TYPE, 
/*  72 */   SIMPLE_TYPE_IS_REQUIRED, 
/*  73 */   PROPERTY_COLLISION, 
/*  74 */   INVALID_IDREF, 
/*  75 */   INVALID_XML_ELEMENT_REF, 
/*  76 */   NO_XML_ELEMENT_DECL, 
/*  77 */   XML_ELEMENT_WRAPPER_ON_NON_COLLECTION, 
/*     */ 
/*  79 */   ANNOTATION_NOT_ALLOWED, 
/*  80 */   XMLLIST_NEEDS_SIMPLETYPE, 
/*  81 */   XMLLIST_ON_SINGLE_PROPERTY, 
/*  82 */   NO_FACTORY_METHOD, 
/*  83 */   FACTORY_CLASS_NEEDS_FACTORY_METHOD, 
/*     */ 
/*  85 */   INCOMPATIBLE_API_VERSION, 
/*  86 */   INCOMPATIBLE_API_VERSION_MUSTANG, 
/*  87 */   RUNNING_WITH_1_0_RUNTIME, 
/*     */ 
/*  89 */   MISSING_JAXB_PROPERTIES, 
/*  90 */   TRANSIENT_FIELD_NOT_BINDABLE, 
/*  91 */   THERE_MUST_BE_VALUE_IN_XMLVALUE, 
/*  92 */   UNMATCHABLE_ADAPTER, 
/*  93 */   ANONYMOUS_ARRAY_ITEM, 
/*     */ 
/*  95 */   ACCESSORFACTORY_INSTANTIATION_EXCEPTION, 
/*  96 */   ACCESSORFACTORY_ACCESS_EXCEPTION, 
/*  97 */   CUSTOM_ACCESSORFACTORY_PROPERTY_ERROR, 
/*  98 */   CUSTOM_ACCESSORFACTORY_FIELD_ERROR, 
/*  99 */   XMLGREGORIANCALENDAR_INVALID, 
/* 100 */   XMLGREGORIANCALENDAR_SEC, 
/* 101 */   XMLGREGORIANCALENDAR_MIN, 
/* 102 */   XMLGREGORIANCALENDAR_HR, 
/* 103 */   XMLGREGORIANCALENDAR_DAY, 
/* 104 */   XMLGREGORIANCALENDAR_MONTH, 
/* 105 */   XMLGREGORIANCALENDAR_YEAR, 
/* 106 */   XMLGREGORIANCALENDAR_TIMEZONE;
/*     */ 
/* 109 */   private static final ResourceBundle rb = ResourceBundle.getBundle(Messages.class.getName());
/*     */ 
/*     */   public String toString()
/*     */   {
/* 113 */     return format(new Object[0]);
/*     */   }
/*     */ 
/*     */   public String format(Object[] args) {
/* 117 */     return MessageFormat.format(rb.getString(name()), args);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.model.impl.Messages
 * JD-Core Version:    0.6.2
 */