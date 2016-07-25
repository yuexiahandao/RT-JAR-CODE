/*     */ package com.sun.org.apache.xalan.internal.utils;
/*     */ 
/*     */ public final class XMLSecurityManager
/*     */ {
/*     */   private final int[] values;
/*     */   private State[] states;
/*     */   private boolean[] isSet;
/* 163 */   private int indexEntityCountInfo = 10000;
/* 164 */   private String printEntityCountInfo = "";
/*     */ 
/*     */   public XMLSecurityManager()
/*     */   {
/* 171 */     this(false);
/*     */   }
/*     */ 
/*     */   public XMLSecurityManager(boolean secureProcessing)
/*     */   {
/* 180 */     this.values = new int[Limit.values().length];
/* 181 */     this.states = new State[Limit.values().length];
/* 182 */     this.isSet = new boolean[Limit.values().length];
/* 183 */     for (Limit limit : Limit.values()) {
/* 184 */       if (secureProcessing) {
/* 185 */         this.values[limit.ordinal()] = limit.secureValue();
/* 186 */         this.states[limit.ordinal()] = State.FSP;
/*     */       } else {
/* 188 */         this.values[limit.ordinal()] = limit.defaultValue();
/* 189 */         this.states[limit.ordinal()] = State.DEFAULT;
/*     */       }
/*     */     }
/*     */ 
/* 193 */     readSystemProperties();
/*     */   }
/*     */ 
/*     */   public void setSecureProcessing(boolean secure)
/*     */   {
/* 200 */     for (Limit limit : Limit.values())
/* 201 */       if (secure)
/* 202 */         setLimit(limit.ordinal(), State.FSP, limit.secureValue());
/*     */       else
/* 204 */         setLimit(limit.ordinal(), State.FSP, limit.defaultValue());
/*     */   }
/*     */ 
/*     */   public boolean setLimit(String propertyName, State state, Object value)
/*     */   {
/* 218 */     int index = getIndex(propertyName);
/* 219 */     if (index > -1) {
/* 220 */       setLimit(index, state, value);
/* 221 */       return true;
/*     */     }
/* 223 */     return false;
/*     */   }
/*     */ 
/*     */   public void setLimit(Limit limit, State state, int value)
/*     */   {
/* 234 */     setLimit(limit.ordinal(), state, value);
/*     */   }
/*     */ 
/*     */   public void setLimit(int index, State state, Object value)
/*     */   {
/* 245 */     if (index == this.indexEntityCountInfo)
/*     */     {
/* 247 */       this.printEntityCountInfo = ((String)value);
/*     */     } else {
/* 249 */       int temp = 0;
/*     */       try {
/* 251 */         temp = Integer.parseInt((String)value);
/* 252 */         if (temp < 0)
/* 253 */           temp = 0;
/*     */       } catch (NumberFormatException e) {
/*     */       }
/* 256 */       setLimit(index, state, temp);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setLimit(int index, State state, int value)
/*     */   {
/* 267 */     if (index == this.indexEntityCountInfo)
/*     */     {
/* 269 */       this.printEntityCountInfo = "yes";
/*     */     }
/* 272 */     else if (state.compareTo(this.states[index]) >= 0) {
/* 273 */       this.values[index] = value;
/* 274 */       this.states[index] = state;
/* 275 */       this.isSet[index] = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getLimitAsString(String propertyName)
/*     */   {
/* 289 */     int index = getIndex(propertyName);
/* 290 */     if (index > -1) {
/* 291 */       return getLimitValueByIndex(index);
/*     */     }
/*     */ 
/* 294 */     return null;
/*     */   }
/*     */ 
/*     */   public String getLimitValueAsString(Limit limit)
/*     */   {
/* 304 */     return Integer.toString(this.values[limit.ordinal()]);
/*     */   }
/*     */ 
/*     */   public int getLimit(Limit limit)
/*     */   {
/* 314 */     return this.values[limit.ordinal()];
/*     */   }
/*     */ 
/*     */   public int getLimitByIndex(int index)
/*     */   {
/* 324 */     return this.values[index];
/*     */   }
/*     */ 
/*     */   public String getLimitValueByIndex(int index)
/*     */   {
/* 333 */     if (index == this.indexEntityCountInfo) {
/* 334 */       return this.printEntityCountInfo;
/*     */     }
/*     */ 
/* 337 */     return Integer.toString(this.values[index]);
/*     */   }
/*     */ 
/*     */   public State getState(Limit limit)
/*     */   {
/* 346 */     return this.states[limit.ordinal()];
/*     */   }
/*     */ 
/*     */   public String getStateLiteral(Limit limit)
/*     */   {
/* 356 */     return this.states[limit.ordinal()].literal();
/*     */   }
/*     */ 
/*     */   public int getIndex(String propertyName)
/*     */   {
/* 366 */     for (Limit limit : Limit.values()) {
/* 367 */       if (limit.equalsAPIPropertyName(propertyName))
/*     */       {
/* 369 */         return limit.ordinal();
/*     */       }
/*     */     }
/*     */ 
/* 373 */     if (propertyName.equals("http://www.oracle.com/xml/jaxp/properties/getEntityCountInfo")) {
/* 374 */       return this.indexEntityCountInfo;
/*     */     }
/* 376 */     return -1;
/*     */   }
/*     */ 
/*     */   public boolean isSet(int index)
/*     */   {
/* 385 */     return this.isSet[index];
/*     */   }
/*     */ 
/*     */   public boolean printEntityCountInfo() {
/* 389 */     return this.printEntityCountInfo.equals("yes");
/*     */   }
/*     */ 
/*     */   private void readSystemProperties()
/*     */   {
/* 396 */     for (Limit limit : Limit.values())
/* 397 */       if (!getSystemProperty(limit, limit.systemProperty()))
/*     */       {
/* 399 */         for (NameMap nameMap : NameMap.values()) {
/* 400 */           String oldName = nameMap.getOldName(limit.systemProperty());
/* 401 */           if (oldName != null)
/* 402 */             getSystemProperty(limit, oldName);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   private boolean getSystemProperty(Limit limit, String sysPropertyName)
/*     */   {
/*     */     try
/*     */     {
/* 418 */       String value = SecuritySupport.getSystemProperty(sysPropertyName);
/* 419 */       if ((value != null) && (!value.equals(""))) {
/* 420 */         this.values[limit.ordinal()] = Integer.parseInt(value);
/* 421 */         this.states[limit.ordinal()] = State.SYSTEMPROPERTY;
/* 422 */         return true;
/*     */       }
/*     */ 
/* 425 */       value = SecuritySupport.readJAXPProperty(sysPropertyName);
/* 426 */       if ((value != null) && (!value.equals(""))) {
/* 427 */         this.values[limit.ordinal()] = Integer.parseInt(value);
/* 428 */         this.states[limit.ordinal()] = State.JAXPDOTPROPERTIES;
/* 429 */         return true;
/*     */       }
/*     */     }
/*     */     catch (NumberFormatException e) {
/* 433 */       throw new NumberFormatException("Invalid setting for system property: " + limit.systemProperty());
/*     */     }
/* 435 */     return false;
/*     */   }
/*     */ 
/*     */   public static enum Limit
/*     */   {
/*  68 */     ENTITY_EXPANSION_LIMIT("http://www.oracle.com/xml/jaxp/properties/entityExpansionLimit", "jdk.xml.entityExpansionLimit", 0, 64000), 
/*     */ 
/*  70 */     MAX_OCCUR_NODE_LIMIT("http://www.oracle.com/xml/jaxp/properties/maxOccurLimit", "jdk.xml.maxOccurLimit", 0, 5000), 
/*     */ 
/*  72 */     ELEMENT_ATTRIBUTE_LIMIT("http://www.oracle.com/xml/jaxp/properties/elementAttributeLimit", "jdk.xml.elementAttributeLimit", 0, 10000), 
/*     */ 
/*  74 */     TOTAL_ENTITY_SIZE_LIMIT("http://www.oracle.com/xml/jaxp/properties/totalEntitySizeLimit", "jdk.xml.totalEntitySizeLimit", 0, 50000000), 
/*     */ 
/*  76 */     GENEAL_ENTITY_SIZE_LIMIT("http://www.oracle.com/xml/jaxp/properties/maxGeneralEntitySizeLimit", "jdk.xml.maxGeneralEntitySizeLimit", 0, 0), 
/*     */ 
/*  78 */     PARAMETER_ENTITY_SIZE_LIMIT("http://www.oracle.com/xml/jaxp/properties/maxParameterEntitySizeLimit", "jdk.xml.maxParameterEntitySizeLimit", 0, 1000000), 
/*     */ 
/*  80 */     MAX_ELEMENT_DEPTH_LIMIT("http://www.oracle.com/xml/jaxp/properties/maxElementDepth", "jdk.xml.maxElementDepth", 0, 0);
/*     */ 
/*     */     final String apiProperty;
/*     */     final String systemProperty;
/*     */     final int defaultValue;
/*     */     final int secureValue;
/*     */ 
/*  89 */     private Limit(String apiProperty, String systemProperty, int value, int secureValue) { this.apiProperty = apiProperty;
/*  90 */       this.systemProperty = systemProperty;
/*  91 */       this.defaultValue = value;
/*  92 */       this.secureValue = secureValue; }
/*     */ 
/*     */     public boolean equalsAPIPropertyName(String propertyName)
/*     */     {
/*  96 */       return propertyName == null ? false : this.apiProperty.equals(propertyName);
/*     */     }
/*     */ 
/*     */     public boolean equalsSystemPropertyName(String propertyName) {
/* 100 */       return propertyName == null ? false : this.systemProperty.equals(propertyName);
/*     */     }
/*     */ 
/*     */     public String apiProperty() {
/* 104 */       return this.apiProperty;
/*     */     }
/*     */ 
/*     */     String systemProperty() {
/* 108 */       return this.systemProperty;
/*     */     }
/*     */ 
/*     */     int defaultValue() {
/* 112 */       return this.defaultValue;
/*     */     }
/*     */ 
/*     */     int secureValue() {
/* 116 */       return this.secureValue;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum NameMap
/*     */   {
/* 125 */     ENTITY_EXPANSION_LIMIT("jdk.xml.entityExpansionLimit", "entityExpansionLimit"), 
/*     */ 
/* 127 */     MAX_OCCUR_NODE_LIMIT("jdk.xml.maxOccurLimit", "maxOccurLimit"), 
/*     */ 
/* 129 */     ELEMENT_ATTRIBUTE_LIMIT("jdk.xml.elementAttributeLimit", "elementAttributeLimit");
/*     */ 
/*     */     final String newName;
/*     */     final String oldName;
/*     */ 
/* 135 */     private NameMap(String newName, String oldName) { this.newName = newName;
/* 136 */       this.oldName = oldName; }
/*     */ 
/*     */     String getOldName(String newName)
/*     */     {
/* 140 */       if (newName.equals(this.newName)) {
/* 141 */         return this.oldName;
/*     */       }
/* 143 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum State
/*     */   {
/*  49 */     DEFAULT("default"), FSP("FEATURE_SECURE_PROCESSING"), 
/*  50 */     JAXPDOTPROPERTIES("jaxp.properties"), SYSTEMPROPERTY("system property"), 
/*  51 */     APIPROPERTY("property");
/*     */ 
/*     */     final String literal;
/*     */ 
/*  55 */     private State(String literal) { this.literal = literal; }
/*     */ 
/*     */     String literal()
/*     */     {
/*  59 */       return this.literal;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.utils.XMLSecurityManager
 * JD-Core Version:    0.6.2
 */