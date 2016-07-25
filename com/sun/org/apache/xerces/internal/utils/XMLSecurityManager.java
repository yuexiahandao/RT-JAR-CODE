/*     */ package com.sun.org.apache.xerces.internal.utils;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.util.SecurityManager;
/*     */ 
/*     */ public final class XMLSecurityManager
/*     */ {
/*     */   private static final int NO_LIMIT = 0;
/*     */   private final int[] values;
/*     */   private State[] states;
/*     */   boolean secureProcessing;
/*     */   private boolean[] isSet;
/* 155 */   private int indexEntityCountInfo = 10000;
/* 156 */   private String printEntityCountInfo = "";
/*     */ 
/*     */   public XMLSecurityManager()
/*     */   {
/* 163 */     this(false);
/*     */   }
/*     */ 
/*     */   public XMLSecurityManager(boolean secureProcessing)
/*     */   {
/* 172 */     this.values = new int[Limit.values().length];
/* 173 */     this.states = new State[Limit.values().length];
/* 174 */     this.isSet = new boolean[Limit.values().length];
/* 175 */     this.secureProcessing = secureProcessing;
/* 176 */     for (Limit limit : Limit.values()) {
/* 177 */       if (secureProcessing) {
/* 178 */         this.values[limit.ordinal()] = limit.secureValue;
/* 179 */         this.states[limit.ordinal()] = State.FSP;
/*     */       } else {
/* 181 */         this.values[limit.ordinal()] = limit.defaultValue();
/* 182 */         this.states[limit.ordinal()] = State.DEFAULT;
/*     */       }
/*     */     }
/*     */ 
/* 186 */     readSystemProperties();
/*     */   }
/*     */ 
/*     */   public void setSecureProcessing(boolean secure)
/*     */   {
/* 193 */     this.secureProcessing = secure;
/* 194 */     for (Limit limit : Limit.values())
/* 195 */       if (secure)
/* 196 */         setLimit(limit.ordinal(), State.FSP, limit.secureValue());
/*     */       else
/* 198 */         setLimit(limit.ordinal(), State.FSP, limit.defaultValue());
/*     */   }
/*     */ 
/*     */   public boolean isSecureProcessing()
/*     */   {
/* 208 */     return this.secureProcessing;
/*     */   }
/*     */ 
/*     */   public boolean setLimit(String propertyName, State state, Object value)
/*     */   {
/* 221 */     int index = getIndex(propertyName);
/* 222 */     if (index > -1) {
/* 223 */       setLimit(index, state, value);
/* 224 */       return true;
/*     */     }
/* 226 */     return false;
/*     */   }
/*     */ 
/*     */   public void setLimit(Limit limit, State state, int value)
/*     */   {
/* 237 */     setLimit(limit.ordinal(), state, value);
/*     */   }
/*     */ 
/*     */   public void setLimit(int index, State state, Object value)
/*     */   {
/* 248 */     if (index == this.indexEntityCountInfo) {
/* 249 */       this.printEntityCountInfo = ((String)value);
/*     */     }
/*     */     else
/*     */     {
/*     */       int temp;
/*     */       int temp;
/* 252 */       if (Integer.class.isAssignableFrom(value.getClass())) {
/* 253 */         temp = ((Integer)value).intValue();
/*     */       } else {
/* 255 */         temp = Integer.parseInt((String)value);
/* 256 */         if (temp < 0) {
/* 257 */           temp = 0;
/*     */         }
/*     */       }
/* 260 */       setLimit(index, state, temp);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setLimit(int index, State state, int value)
/*     */   {
/* 272 */     if (index == this.indexEntityCountInfo)
/*     */     {
/* 274 */       this.printEntityCountInfo = "yes";
/*     */     }
/* 277 */     else if (state.compareTo(this.states[index]) >= 0) {
/* 278 */       this.values[index] = value;
/* 279 */       this.states[index] = state;
/* 280 */       this.isSet[index] = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getLimitAsString(String propertyName)
/*     */   {
/* 293 */     int index = getIndex(propertyName);
/* 294 */     if (index > -1) {
/* 295 */       return getLimitValueByIndex(index);
/*     */     }
/*     */ 
/* 298 */     return null;
/*     */   }
/*     */ 
/*     */   public int getLimit(Limit limit)
/*     */   {
/* 307 */     return this.values[limit.ordinal()];
/*     */   }
/*     */ 
/*     */   public String getLimitValueAsString(Limit limit)
/*     */   {
/* 317 */     return Integer.toString(this.values[limit.ordinal()]);
/*     */   }
/*     */ 
/*     */   public String getLimitValueByIndex(int index)
/*     */   {
/* 327 */     if (index == this.indexEntityCountInfo) {
/* 328 */       return this.printEntityCountInfo;
/*     */     }
/*     */ 
/* 331 */     return Integer.toString(this.values[index]);
/*     */   }
/*     */ 
/*     */   public State getState(Limit limit)
/*     */   {
/* 341 */     return this.states[limit.ordinal()];
/*     */   }
/*     */ 
/*     */   public String getStateLiteral(Limit limit)
/*     */   {
/* 351 */     return this.states[limit.ordinal()].literal();
/*     */   }
/*     */ 
/*     */   public int getIndex(String propertyName)
/*     */   {
/* 361 */     for (Limit limit : Limit.values()) {
/* 362 */       if (limit.equalsAPIPropertyName(propertyName))
/*     */       {
/* 364 */         return limit.ordinal();
/*     */       }
/*     */     }
/*     */ 
/* 368 */     if (propertyName.equals("http://www.oracle.com/xml/jaxp/properties/getEntityCountInfo")) {
/* 369 */       return this.indexEntityCountInfo;
/*     */     }
/* 371 */     return -1;
/*     */   }
/*     */ 
/*     */   public boolean isNoLimit(int limit)
/*     */   {
/* 380 */     return limit == 0;
/*     */   }
/*     */ 
/*     */   public boolean isOverLimit(Limit limit, String entityName, int size, XMLLimitAnalyzer limitAnalyzer)
/*     */   {
/* 393 */     return isOverLimit(limit.ordinal(), entityName, size, limitAnalyzer);
/*     */   }
/*     */ 
/*     */   public boolean isOverLimit(int index, String entityName, int size, XMLLimitAnalyzer limitAnalyzer)
/*     */   {
/* 407 */     if (this.values[index] == 0) {
/* 408 */       return false;
/*     */     }
/* 410 */     if (size > this.values[index]) {
/* 411 */       limitAnalyzer.addValue(index, entityName, size);
/* 412 */       return true;
/*     */     }
/* 414 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isOverLimit(Limit limit, XMLLimitAnalyzer limitAnalyzer)
/*     */   {
/* 425 */     return isOverLimit(limit.ordinal(), limitAnalyzer);
/*     */   }
/*     */ 
/*     */   public boolean isOverLimit(int index, XMLLimitAnalyzer limitAnalyzer) {
/* 429 */     if (this.values[index] == 0) {
/* 430 */       return false;
/*     */     }
/*     */ 
/* 433 */     if ((index == Limit.ELEMENT_ATTRIBUTE_LIMIT.ordinal()) || (index == Limit.ENTITY_EXPANSION_LIMIT.ordinal()) || (index == Limit.TOTAL_ENTITY_SIZE_LIMIT.ordinal()) || (index == Limit.MAX_ELEMENT_DEPTH_LIMIT.ordinal()))
/*     */     {
/* 437 */       return limitAnalyzer.getTotalValue(index) > this.values[index];
/*     */     }
/* 439 */     return limitAnalyzer.getValue(index) > this.values[index];
/*     */   }
/*     */ 
/*     */   public void debugPrint(XMLLimitAnalyzer limitAnalyzer)
/*     */   {
/* 444 */     if (this.printEntityCountInfo.equals("yes"))
/* 445 */       limitAnalyzer.debugPrint(this);
/*     */   }
/*     */ 
/*     */   public boolean isSet(int index)
/*     */   {
/* 456 */     return this.isSet[index];
/*     */   }
/*     */ 
/*     */   public boolean printEntityCountInfo() {
/* 460 */     return this.printEntityCountInfo.equals("yes");
/*     */   }
/*     */ 
/*     */   private void readSystemProperties()
/*     */   {
/* 468 */     for (Limit limit : Limit.values())
/* 469 */       if (!getSystemProperty(limit, limit.systemProperty()))
/*     */       {
/* 471 */         for (NameMap nameMap : NameMap.values()) {
/* 472 */           String oldName = nameMap.getOldName(limit.systemProperty());
/* 473 */           if (oldName != null)
/* 474 */             getSystemProperty(limit, oldName);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   private boolean getSystemProperty(Limit limit, String sysPropertyName)
/*     */   {
/*     */     try
/*     */     {
/* 490 */       String value = SecuritySupport.getSystemProperty(sysPropertyName);
/* 491 */       if ((value != null) && (!value.equals(""))) {
/* 492 */         this.values[limit.ordinal()] = Integer.parseInt(value);
/* 493 */         this.states[limit.ordinal()] = State.SYSTEMPROPERTY;
/* 494 */         return true;
/*     */       }
/*     */ 
/* 497 */       value = SecuritySupport.readJAXPProperty(sysPropertyName);
/* 498 */       if ((value != null) && (!value.equals(""))) {
/* 499 */         this.values[limit.ordinal()] = Integer.parseInt(value);
/* 500 */         this.states[limit.ordinal()] = State.JAXPDOTPROPERTIES;
/* 501 */         return true;
/*     */       }
/*     */     }
/*     */     catch (NumberFormatException e) {
/* 505 */       throw new NumberFormatException("Invalid setting for system property: " + limit.systemProperty());
/*     */     }
/* 507 */     return false;
/*     */   }
/*     */ 
/*     */   public static XMLSecurityManager convert(Object value, XMLSecurityManager securityManager)
/*     */   {
/* 521 */     if (value == null) {
/* 522 */       if (securityManager == null) {
/* 523 */         securityManager = new XMLSecurityManager(true);
/*     */       }
/* 525 */       return securityManager;
/*     */     }
/* 527 */     if (XMLSecurityManager.class.isAssignableFrom(value.getClass())) {
/* 528 */       return (XMLSecurityManager)value;
/*     */     }
/* 530 */     if (securityManager == null) {
/* 531 */       securityManager = new XMLSecurityManager(true);
/*     */     }
/* 533 */     if (SecurityManager.class.isAssignableFrom(value.getClass())) {
/* 534 */       SecurityManager origSM = (SecurityManager)value;
/* 535 */       securityManager.setLimit(Limit.MAX_OCCUR_NODE_LIMIT, State.APIPROPERTY, origSM.getMaxOccurNodeLimit());
/* 536 */       securityManager.setLimit(Limit.ENTITY_EXPANSION_LIMIT, State.APIPROPERTY, origSM.getEntityExpansionLimit());
/* 537 */       securityManager.setLimit(Limit.ELEMENT_ATTRIBUTE_LIMIT, State.APIPROPERTY, origSM.getElementAttrLimit());
/*     */     }
/* 539 */     return securityManager;
/*     */   }
/*     */ 
/*     */   public static enum Limit
/*     */   {
/*  64 */     ENTITY_EXPANSION_LIMIT("http://www.oracle.com/xml/jaxp/properties/entityExpansionLimit", "jdk.xml.entityExpansionLimit", 0, 64000), 
/*  65 */     MAX_OCCUR_NODE_LIMIT("http://www.oracle.com/xml/jaxp/properties/maxOccurLimit", "jdk.xml.maxOccurLimit", 0, 5000), 
/*  66 */     ELEMENT_ATTRIBUTE_LIMIT("http://www.oracle.com/xml/jaxp/properties/elementAttributeLimit", "jdk.xml.elementAttributeLimit", 0, 10000), 
/*  67 */     TOTAL_ENTITY_SIZE_LIMIT("http://www.oracle.com/xml/jaxp/properties/totalEntitySizeLimit", "jdk.xml.totalEntitySizeLimit", 0, 50000000), 
/*  68 */     GENEAL_ENTITY_SIZE_LIMIT("http://www.oracle.com/xml/jaxp/properties/maxGeneralEntitySizeLimit", "jdk.xml.maxGeneralEntitySizeLimit", 0, 0), 
/*  69 */     PARAMETER_ENTITY_SIZE_LIMIT("http://www.oracle.com/xml/jaxp/properties/maxParameterEntitySizeLimit", "jdk.xml.maxParameterEntitySizeLimit", 0, 1000000), 
/*  70 */     MAX_ELEMENT_DEPTH_LIMIT("http://www.oracle.com/xml/jaxp/properties/maxElementDepth", "jdk.xml.maxElementDepth", 0, 0);
/*     */ 
/*     */     final String apiProperty;
/*     */     final String systemProperty;
/*     */     final int defaultValue;
/*     */     final int secureValue;
/*     */ 
/*  78 */     private Limit(String apiProperty, String systemProperty, int value, int secureValue) { this.apiProperty = apiProperty;
/*  79 */       this.systemProperty = systemProperty;
/*  80 */       this.defaultValue = value;
/*  81 */       this.secureValue = secureValue; }
/*     */ 
/*     */     public boolean equalsAPIPropertyName(String propertyName)
/*     */     {
/*  85 */       return propertyName == null ? false : this.apiProperty.equals(propertyName);
/*     */     }
/*     */ 
/*     */     public boolean equalsSystemPropertyName(String propertyName) {
/*  89 */       return propertyName == null ? false : this.systemProperty.equals(propertyName);
/*     */     }
/*     */ 
/*     */     public String apiProperty() {
/*  93 */       return this.apiProperty;
/*     */     }
/*     */ 
/*     */     String systemProperty() {
/*  97 */       return this.systemProperty;
/*     */     }
/*     */ 
/*     */     int defaultValue() {
/* 101 */       return this.defaultValue;
/*     */     }
/*     */ 
/*     */     int secureValue() {
/* 105 */       return this.secureValue;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum NameMap
/*     */   {
/* 114 */     ENTITY_EXPANSION_LIMIT("jdk.xml.entityExpansionLimit", "entityExpansionLimit"), 
/* 115 */     MAX_OCCUR_NODE_LIMIT("jdk.xml.maxOccurLimit", "maxOccurLimit"), 
/* 116 */     ELEMENT_ATTRIBUTE_LIMIT("jdk.xml.elementAttributeLimit", "elementAttributeLimit");
/*     */ 
/*     */     final String newName;
/*     */     final String oldName;
/*     */ 
/* 121 */     private NameMap(String newName, String oldName) { this.newName = newName;
/* 122 */       this.oldName = oldName; }
/*     */ 
/*     */     String getOldName(String newName)
/*     */     {
/* 126 */       if (newName.equals(this.newName)) {
/* 127 */         return this.oldName;
/*     */       }
/* 129 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum State
/*     */   {
/*  45 */     DEFAULT("default"), FSP("FEATURE_SECURE_PROCESSING"), 
/*  46 */     JAXPDOTPROPERTIES("jaxp.properties"), SYSTEMPROPERTY("system property"), 
/*  47 */     APIPROPERTY("property");
/*     */ 
/*     */     final String literal;
/*     */ 
/*  51 */     private State(String literal) { this.literal = literal; }
/*     */ 
/*     */     String literal()
/*     */     {
/*  55 */       return this.literal;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.utils.XMLSecurityManager
 * JD-Core Version:    0.6.2
 */