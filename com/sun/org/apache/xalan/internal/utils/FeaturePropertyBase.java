/*     */ package com.sun.org.apache.xalan.internal.utils;
/*     */ 
/*     */ public abstract class FeaturePropertyBase
/*     */ {
/*  50 */   String[] values = null;
/*     */ 
/*  54 */   State[] states = { State.DEFAULT, State.DEFAULT };
/*     */ 
/*     */   public void setValue(Enum property, State state, String value)
/*     */   {
/*  66 */     if (state.compareTo(this.states[property.ordinal()]) >= 0) {
/*  67 */       this.values[property.ordinal()] = value;
/*  68 */       this.states[property.ordinal()] = state;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setValue(int index, State state, String value)
/*     */   {
/*  80 */     if (state.compareTo(this.states[index]) >= 0) {
/*  81 */       this.values[index] = value;
/*  82 */       this.states[index] = state;
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean setValue(String propertyName, State state, Object value)
/*     */   {
/*  95 */     int index = getIndex(propertyName);
/*  96 */     if (index > -1) {
/*  97 */       setValue(index, state, (String)value);
/*  98 */       return true;
/*     */     }
/* 100 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean setValue(String propertyName, State state, boolean value)
/*     */   {
/* 112 */     int index = getIndex(propertyName);
/* 113 */     if (index > -1) {
/* 114 */       if (value)
/* 115 */         setValue(index, state, "true");
/*     */       else {
/* 117 */         setValue(index, state, "false");
/*     */       }
/* 119 */       return true;
/*     */     }
/* 121 */     return false;
/*     */   }
/*     */ 
/*     */   public String getValue(Enum property)
/*     */   {
/* 131 */     return this.values[property.ordinal()];
/*     */   }
/*     */ 
/*     */   public String getValue(String property)
/*     */   {
/* 141 */     int index = getIndex(property);
/* 142 */     if (index > -1) {
/* 143 */       return getValueByIndex(index);
/*     */     }
/* 145 */     return null;
/*     */   }
/*     */ 
/*     */   public String getValueAsString(String propertyName)
/*     */   {
/* 156 */     int index = getIndex(propertyName);
/* 157 */     if (index > -1) {
/* 158 */       return getValueByIndex(index);
/*     */     }
/*     */ 
/* 161 */     return null;
/*     */   }
/*     */ 
/*     */   public String getValueByIndex(int index)
/*     */   {
/* 170 */     return this.values[index];
/*     */   }
/*     */ 
/*     */   public abstract int getIndex(String paramString);
/*     */ 
/*     */   public <E extends Enum<E>> int getIndex(Class<E> property, String propertyName)
/*     */   {
/* 181 */     for (Enum enumItem : (Enum[])property.getEnumConstants()) {
/* 182 */       if (enumItem.toString().equals(propertyName))
/*     */       {
/* 184 */         return enumItem.ordinal();
/*     */       }
/*     */     }
/* 187 */     return -1;
/*     */   }
/*     */ 
/*     */   void getSystemProperty(Enum property, String systemProperty)
/*     */   {
/*     */     try
/*     */     {
/* 199 */       String value = SecuritySupport.getSystemProperty(systemProperty);
/* 200 */       if (value != null) {
/* 201 */         this.values[property.ordinal()] = value;
/* 202 */         this.states[property.ordinal()] = State.SYSTEMPROPERTY;
/* 203 */         return;
/*     */       }
/*     */ 
/* 206 */       value = SecuritySupport.readJAXPProperty(systemProperty);
/* 207 */       if (value != null) {
/* 208 */         this.values[property.ordinal()] = value;
/* 209 */         this.states[property.ordinal()] = State.JAXPDOTPROPERTIES;
/*     */       }
/*     */     }
/*     */     catch (NumberFormatException e)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public static enum State
/*     */   {
/*  43 */     DEFAULT, FSP, JAXPDOTPROPERTIES, SYSTEMPROPERTY, APIPROPERTY;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.utils.FeaturePropertyBase
 * JD-Core Version:    0.6.2
 */