/*     */ package com.sun.xml.internal.fastinfoset.stax;
/*     */ 
/*     */ import com.sun.xml.internal.fastinfoset.CommonResourceBundle;
/*     */ import java.util.HashMap;
/*     */ 
/*     */ public class StAXManager
/*     */ {
/*     */   protected static final String STAX_NOTATIONS = "javax.xml.stream.notations";
/*     */   protected static final String STAX_ENTITIES = "javax.xml.stream.entities";
/*  39 */   HashMap features = new HashMap();
/*     */   public static final int CONTEXT_READER = 1;
/*     */   public static final int CONTEXT_WRITER = 2;
/*     */ 
/*     */   public StAXManager()
/*     */   {
/*     */   }
/*     */ 
/*     */   public StAXManager(int context)
/*     */   {
/*  50 */     switch (context) {
/*     */     case 1:
/*  52 */       initConfigurableReaderProperties();
/*  53 */       break;
/*     */     case 2:
/*  56 */       initWriterProps();
/*     */     }
/*     */   }
/*     */ 
/*     */   public StAXManager(StAXManager manager)
/*     */   {
/*  64 */     HashMap properties = manager.getProperties();
/*  65 */     this.features.putAll(properties);
/*     */   }
/*     */ 
/*     */   private HashMap getProperties() {
/*  69 */     return this.features;
/*     */   }
/*     */ 
/*     */   private void initConfigurableReaderProperties()
/*     */   {
/*  74 */     this.features.put("javax.xml.stream.isNamespaceAware", Boolean.TRUE);
/*  75 */     this.features.put("javax.xml.stream.isValidating", Boolean.FALSE);
/*  76 */     this.features.put("javax.xml.stream.isReplacingEntityReferences", Boolean.TRUE);
/*  77 */     this.features.put("javax.xml.stream.isSupportingExternalEntities", Boolean.TRUE);
/*  78 */     this.features.put("javax.xml.stream.isCoalescing", Boolean.FALSE);
/*  79 */     this.features.put("javax.xml.stream.supportDTD", Boolean.FALSE);
/*  80 */     this.features.put("javax.xml.stream.reporter", null);
/*  81 */     this.features.put("javax.xml.stream.resolver", null);
/*  82 */     this.features.put("javax.xml.stream.allocator", null);
/*  83 */     this.features.put("javax.xml.stream.notations", null);
/*     */   }
/*     */ 
/*     */   private void initWriterProps() {
/*  87 */     this.features.put("javax.xml.stream.isRepairingNamespaces", Boolean.FALSE);
/*     */   }
/*     */ 
/*     */   public boolean containsProperty(String property)
/*     */   {
/*  96 */     return this.features.containsKey(property);
/*     */   }
/*     */ 
/*     */   public Object getProperty(String name) {
/* 100 */     checkProperty(name);
/* 101 */     return this.features.get(name);
/*     */   }
/*     */ 
/*     */   public void setProperty(String name, Object value) {
/* 105 */     checkProperty(name);
/* 106 */     if ((name.equals("javax.xml.stream.isValidating")) && (Boolean.TRUE.equals(value)))
/*     */     {
/* 108 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.validationNotSupported") + CommonResourceBundle.getInstance().getString("support_validation"));
/*     */     }
/* 110 */     if ((name.equals("javax.xml.stream.isSupportingExternalEntities")) && (Boolean.TRUE.equals(value)))
/*     */     {
/* 112 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.externalEntities") + CommonResourceBundle.getInstance().getString("resolve_external_entities_"));
/*     */     }
/*     */ 
/* 115 */     this.features.put(name, value);
/*     */   }
/*     */ 
/*     */   public void checkProperty(String name)
/*     */   {
/* 120 */     if (!this.features.containsKey(name))
/* 121 */       throw new IllegalArgumentException(CommonResourceBundle.getInstance().getString("message.propertyNotSupported", new Object[] { name }));
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 125 */     return this.features.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.stax.StAXManager
 * JD-Core Version:    0.6.2
 */