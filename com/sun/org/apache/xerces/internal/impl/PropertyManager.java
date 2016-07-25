/*     */ package com.sun.org.apache.xerces.internal.impl;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityManager.State;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager;
/*     */ import com.sun.org.apache.xerces.internal.utils.XMLSecurityPropertyManager.State;
/*     */ import com.sun.xml.internal.stream.StaxEntityResolverWrapper;
/*     */ import java.util.HashMap;
/*     */ import javax.xml.stream.XMLResolver;
/*     */ 
/*     */ public class PropertyManager
/*     */ {
/*     */   public static final String STAX_NOTATIONS = "javax.xml.stream.notations";
/*     */   public static final String STAX_ENTITIES = "javax.xml.stream.entities";
/*     */   private static final String STRING_INTERNING = "http://xml.org/sax/features/string-interning";
/*     */   private static final String SECURITY_MANAGER = "http://apache.org/xml/properties/security-manager";
/*     */   private static final String XML_SECURITY_PROPERTY_MANAGER = "http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager";
/*  61 */   HashMap supportedProps = new HashMap();
/*     */   private XMLSecurityManager fSecurityManager;
/*     */   private XMLSecurityPropertyManager fSecurityPropertyMgr;
/*     */   public static final int CONTEXT_READER = 1;
/*     */   public static final int CONTEXT_WRITER = 2;
/*     */ 
/*     */   public PropertyManager(int context)
/*     */   {
/*  71 */     switch (context) {
/*     */     case 1:
/*  73 */       initConfigurableReaderProperties();
/*  74 */       break;
/*     */     case 2:
/*  77 */       initWriterProps();
/*     */     }
/*     */   }
/*     */ 
/*     */   public PropertyManager(PropertyManager propertyManager)
/*     */   {
/*  88 */     HashMap properties = propertyManager.getProperties();
/*  89 */     this.supportedProps.putAll(properties);
/*  90 */     this.fSecurityManager = ((XMLSecurityManager)getProperty("http://apache.org/xml/properties/security-manager"));
/*  91 */     this.fSecurityPropertyMgr = ((XMLSecurityPropertyManager)getProperty("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager"));
/*     */   }
/*     */ 
/*     */   private HashMap getProperties() {
/*  95 */     return this.supportedProps;
/*     */   }
/*     */ 
/*     */   private void initConfigurableReaderProperties()
/*     */   {
/* 107 */     this.supportedProps.put("javax.xml.stream.isNamespaceAware", Boolean.TRUE);
/* 108 */     this.supportedProps.put("javax.xml.stream.isValidating", Boolean.FALSE);
/* 109 */     this.supportedProps.put("javax.xml.stream.isReplacingEntityReferences", Boolean.TRUE);
/* 110 */     this.supportedProps.put("javax.xml.stream.isSupportingExternalEntities", Boolean.TRUE);
/* 111 */     this.supportedProps.put("javax.xml.stream.isCoalescing", Boolean.FALSE);
/* 112 */     this.supportedProps.put("javax.xml.stream.supportDTD", Boolean.TRUE);
/* 113 */     this.supportedProps.put("javax.xml.stream.reporter", null);
/* 114 */     this.supportedProps.put("javax.xml.stream.resolver", null);
/* 115 */     this.supportedProps.put("javax.xml.stream.allocator", null);
/* 116 */     this.supportedProps.put("javax.xml.stream.notations", null);
/*     */ 
/* 120 */     this.supportedProps.put("http://xml.org/sax/features/string-interning", new Boolean(true));
/*     */ 
/* 122 */     this.supportedProps.put("http://apache.org/xml/features/allow-java-encodings", new Boolean(true));
/*     */ 
/* 124 */     this.supportedProps.put("add-namespacedecl-as-attrbiute", Boolean.FALSE);
/* 125 */     this.supportedProps.put("http://java.sun.com/xml/stream/properties/reader-in-defined-state", new Boolean(true));
/* 126 */     this.supportedProps.put("reuse-instance", new Boolean(true));
/* 127 */     this.supportedProps.put("http://java.sun.com/xml/stream/properties/report-cdata-event", new Boolean(false));
/* 128 */     this.supportedProps.put("http://java.sun.com/xml/stream/properties/ignore-external-dtd", Boolean.FALSE);
/* 129 */     this.supportedProps.put("http://apache.org/xml/features/validation/warn-on-duplicate-attdef", new Boolean(false));
/* 130 */     this.supportedProps.put("http://apache.org/xml/features/warn-on-duplicate-entitydef", new Boolean(false));
/* 131 */     this.supportedProps.put("http://apache.org/xml/features/validation/warn-on-undeclared-elemdef", new Boolean(false));
/*     */ 
/* 133 */     this.fSecurityManager = new XMLSecurityManager(true);
/* 134 */     this.supportedProps.put("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
/* 135 */     this.fSecurityPropertyMgr = new XMLSecurityPropertyManager();
/* 136 */     this.supportedProps.put("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
/*     */   }
/*     */ 
/*     */   private void initWriterProps() {
/* 140 */     this.supportedProps.put("javax.xml.stream.isRepairingNamespaces", Boolean.FALSE);
/*     */ 
/* 142 */     this.supportedProps.put("escapeCharacters", Boolean.TRUE);
/* 143 */     this.supportedProps.put("reuse-instance", new Boolean(true));
/*     */   }
/*     */ 
/*     */   public boolean containsProperty(String property)
/*     */   {
/* 152 */     return (this.supportedProps.containsKey(property)) || ((this.fSecurityManager != null) && (this.fSecurityManager.getIndex(property) > -1)) || ((this.fSecurityPropertyMgr != null) && (this.fSecurityPropertyMgr.getIndex(property) > -1));
/*     */   }
/*     */ 
/*     */   public Object getProperty(String property)
/*     */   {
/* 158 */     return this.supportedProps.get(property);
/*     */   }
/*     */ 
/*     */   public void setProperty(String property, Object value) {
/* 162 */     String equivalentProperty = null;
/* 163 */     if ((property == "javax.xml.stream.isNamespaceAware") || (property.equals("javax.xml.stream.isNamespaceAware"))) {
/* 164 */       equivalentProperty = "http://apache.org/xml/features/namespaces";
/*     */     }
/* 166 */     else if ((property == "javax.xml.stream.isValidating") || (property.equals("javax.xml.stream.isValidating"))) {
/* 167 */       if (((value instanceof Boolean)) && (((Boolean)value).booleanValue())) {
/* 168 */         throw new IllegalArgumentException("true value of isValidating not supported");
/*     */       }
/*     */     }
/* 171 */     else if ((property == "http://xml.org/sax/features/string-interning") || (property.equals("http://xml.org/sax/features/string-interning"))) {
/* 172 */       if (((value instanceof Boolean)) && (!((Boolean)value).booleanValue())) {
/* 173 */         throw new IllegalArgumentException("false value of http://xml.org/sax/features/string-interningfeature is not supported");
/*     */       }
/*     */     }
/* 176 */     else if ((property == "javax.xml.stream.resolver") || (property.equals("javax.xml.stream.resolver")))
/*     */     {
/* 178 */       this.supportedProps.put("http://apache.org/xml/properties/internal/stax-entity-resolver", new StaxEntityResolverWrapper((XMLResolver)value));
/*     */     }
/*     */ 
/* 185 */     if (property.equals("http://apache.org/xml/properties/security-manager")) {
/* 186 */       this.fSecurityManager = XMLSecurityManager.convert(value, this.fSecurityManager);
/* 187 */       this.supportedProps.put("http://apache.org/xml/properties/security-manager", this.fSecurityManager);
/* 188 */       return;
/*     */     }
/* 190 */     if (property.equals("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager")) {
/* 191 */       if (value == null)
/* 192 */         this.fSecurityPropertyMgr = new XMLSecurityPropertyManager();
/*     */       else {
/* 194 */         this.fSecurityPropertyMgr = ((XMLSecurityPropertyManager)value);
/*     */       }
/* 196 */       this.supportedProps.put("http://www.oracle.com/xml/jaxp/properties/xmlSecurityPropertyManager", this.fSecurityPropertyMgr);
/* 197 */       return;
/*     */     }
/*     */ 
/* 201 */     if ((this.fSecurityManager == null) || (!this.fSecurityManager.setLimit(property, XMLSecurityManager.State.APIPROPERTY, value)))
/*     */     {
/* 204 */       if ((this.fSecurityPropertyMgr == null) || (!this.fSecurityPropertyMgr.setValue(property, XMLSecurityPropertyManager.State.APIPROPERTY, value)))
/*     */       {
/* 207 */         this.supportedProps.put(property, value);
/*     */       }
/*     */     }
/*     */ 
/* 211 */     if (equivalentProperty != null)
/* 212 */       this.supportedProps.put(equivalentProperty, value);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 217 */     return this.supportedProps.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.PropertyManager
 * JD-Core Version:    0.6.2
 */