/*     */ package com.sun.xml.internal.ws.client;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.EndpointAddress;
/*     */ import com.sun.xml.internal.ws.api.PropertySet;
/*     */ import com.sun.xml.internal.ws.api.PropertySet.Accessor;
/*     */ import com.sun.xml.internal.ws.api.PropertySet.Property;
/*     */ import com.sun.xml.internal.ws.api.PropertySet.PropertyMap;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ public final class RequestContext extends PropertySet
/*     */ {
/* 106 */   private static final Logger LOGGER = Logger.getLogger(RequestContext.class.getName());
/*     */ 
/* 114 */   private static ContentNegotiation defaultContentNegotiation = ContentNegotiation.obtainFromSystemProperty();
/*     */   private final Map<String, Object> others;
/*     */ 
/*     */   @NotNull
/*     */   private EndpointAddress endpointAddress;
/* 161 */   public ContentNegotiation contentNegotiation = defaultContentNegotiation;
/*     */   private String soapAction;
/*     */   private Boolean soapActionUse;
/* 237 */   private final MapView mapView = new MapView(null);
/*     */ 
/* 422 */   private static final PropertySet.PropertyMap propMap = parse(RequestContext.class);
/*     */ 
/*     */   /** @deprecated */
/*     */   @PropertySet.Property({"javax.xml.ws.service.endpoint.address"})
/*     */   public String getEndPointAddressString()
/*     */   {
/* 139 */     return this.endpointAddress.toString();
/*     */   }
/*     */ 
/*     */   public void setEndPointAddressString(String s) {
/* 143 */     if (s == null) {
/* 144 */       throw new IllegalArgumentException();
/*     */     }
/* 146 */     this.endpointAddress = EndpointAddress.create(s);
/*     */   }
/*     */ 
/*     */   public void setEndpointAddress(@NotNull EndpointAddress epa) {
/* 150 */     this.endpointAddress = epa;
/*     */   }
/*     */   @NotNull
/*     */   public EndpointAddress getEndpointAddress() {
/* 154 */     return this.endpointAddress;
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"com.sun.xml.internal.ws.client.ContentNegotiation"})
/*     */   public String getContentNegotiationString()
/*     */   {
/* 165 */     return this.contentNegotiation.toString();
/*     */   }
/*     */ 
/*     */   public void setContentNegotiationString(String s) {
/* 169 */     if (s == null)
/* 170 */       this.contentNegotiation = ContentNegotiation.none;
/*     */     else
/*     */       try {
/* 173 */         this.contentNegotiation = ContentNegotiation.valueOf(s);
/*     */       }
/*     */       catch (IllegalArgumentException e) {
/* 176 */         this.contentNegotiation = ContentNegotiation.none;
/*     */       }
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"javax.xml.ws.soap.http.soapaction.uri"})
/*     */   public String getSoapAction()
/*     */   {
/* 209 */     return this.soapAction;
/*     */   }
/*     */   public void setSoapAction(String sAction) {
/* 212 */     if (sAction == null) {
/* 213 */       throw new IllegalArgumentException("SOAPAction value cannot be null");
/*     */     }
/* 215 */     this.soapAction = sAction;
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"javax.xml.ws.soap.http.soapaction.use"})
/*     */   public Boolean getSoapActionUse()
/*     */   {
/* 228 */     return this.soapActionUse;
/*     */   }
/*     */   public void setSoapActionUse(Boolean sActionUse) {
/* 231 */     this.soapActionUse = sActionUse;
/*     */   }
/*     */ 
/*     */   RequestContext()
/*     */   {
/* 243 */     this.others = new HashMap();
/*     */   }
/*     */ 
/*     */   private RequestContext(RequestContext that)
/*     */   {
/* 250 */     this.others = new HashMap(that.others);
/* 251 */     this.endpointAddress = that.endpointAddress;
/* 252 */     this.soapAction = that.soapAction;
/* 253 */     this.contentNegotiation = that.contentNegotiation;
/*     */   }
/*     */ 
/*     */   public Object get(Object key)
/*     */   {
/* 261 */     if (super.supports(key)) {
/* 262 */       return super.get(key);
/*     */     }
/* 264 */     return this.others.get(key);
/*     */   }
/*     */ 
/*     */   public Object put(String key, Object value)
/*     */   {
/* 271 */     if (super.supports(key)) {
/* 272 */       return super.put(key, value);
/*     */     }
/* 274 */     return this.others.put(key, value);
/*     */   }
/*     */ 
/*     */   public Map<String, Object> getMapView()
/*     */   {
/* 284 */     return this.mapView;
/*     */   }
/*     */ 
/*     */   public void fill(Packet packet, boolean isAddressingEnabled)
/*     */   {
/* 291 */     if (this.mapView.fallbackMap == null) {
/* 292 */       if (this.endpointAddress != null)
/* 293 */         packet.endpointAddress = this.endpointAddress;
/* 294 */       packet.contentNegotiation = this.contentNegotiation;
/*     */ 
/* 301 */       if (((this.soapActionUse != null) && (this.soapActionUse.booleanValue())) || ((this.soapActionUse == null) && (isAddressingEnabled) && 
/* 302 */         (this.soapAction != null))) {
/* 303 */         packet.soapAction = this.soapAction;
/*     */       }
/*     */ 
/* 306 */       if ((!isAddressingEnabled) && ((this.soapActionUse == null) || (!this.soapActionUse.booleanValue())) && (this.soapAction != null)) {
/* 307 */         LOGGER.warning("BindingProvider.SOAPACTION_URI_PROPERTY is set in the RequestContext but is ineffective, Either set BindingProvider.SOAPACTION_USE_PROPERTY to true or enable AddressingFeature");
/*     */       }
/*     */ 
/* 310 */       if (!this.others.isEmpty()) {
/* 311 */         packet.invocationProperties.putAll(this.others);
/*     */ 
/* 313 */         packet.getHandlerScopePropertyNames(false).addAll(this.others.keySet());
/*     */       }
/*     */     } else {
/* 316 */       Set handlerScopePropertyNames = new HashSet();
/*     */ 
/* 318 */       for (Map.Entry entry : this.mapView.fallbackMap.entrySet()) {
/* 319 */         String key = (String)entry.getKey();
/* 320 */         if (packet.supports(key))
/* 321 */           packet.put(key, entry.getValue());
/*     */         else {
/* 323 */           packet.invocationProperties.put(key, entry.getValue());
/*     */         }
/*     */ 
/* 326 */         if (!super.supports(key)) {
/* 327 */           handlerScopePropertyNames.add(key);
/*     */         }
/*     */       }
/*     */ 
/* 331 */       if (!handlerScopePropertyNames.isEmpty())
/* 332 */         packet.getHandlerScopePropertyNames(false).addAll(handlerScopePropertyNames);
/*     */     }
/*     */   }
/*     */ 
/*     */   public RequestContext copy() {
/* 337 */     return new RequestContext(this);
/*     */   }
/*     */ 
/*     */   protected PropertySet.PropertyMap getPropertyMap()
/*     */   {
/* 419 */     return propMap;
/*     */   }
/*     */ 
/*     */   private final class MapView
/*     */     implements Map<String, Object>
/*     */   {
/*     */     private Map<String, Object> fallbackMap;
/*     */ 
/*     */     private MapView()
/*     */     {
/*     */     }
/*     */ 
/*     */     private Map<String, Object> fallback()
/*     */     {
/* 345 */       if (this.fallbackMap == null)
/*     */       {
/* 347 */         this.fallbackMap = new HashMap(RequestContext.this.others);
/*     */ 
/* 349 */         for (Map.Entry prop : RequestContext.propMap.entrySet()) {
/* 350 */           this.fallbackMap.put(prop.getKey(), ((PropertySet.Accessor)prop.getValue()).get(RequestContext.this));
/*     */         }
/*     */       }
/* 353 */       return this.fallbackMap;
/*     */     }
/*     */ 
/*     */     public int size() {
/* 357 */       return fallback().size();
/*     */     }
/*     */ 
/*     */     public boolean isEmpty() {
/* 361 */       return fallback().isEmpty();
/*     */     }
/*     */ 
/*     */     public boolean containsKey(Object key) {
/* 365 */       return fallback().containsKey(key);
/*     */     }
/*     */ 
/*     */     public boolean containsValue(Object value) {
/* 369 */       return fallback().containsValue(value);
/*     */     }
/*     */ 
/*     */     public Object get(Object key) {
/* 373 */       if (this.fallbackMap == null) {
/* 374 */         return RequestContext.this.get(key);
/*     */       }
/* 376 */       return fallback().get(key);
/*     */     }
/*     */ 
/*     */     public Object put(String key, Object value)
/*     */     {
/* 381 */       if (this.fallbackMap == null) {
/* 382 */         return RequestContext.this.put(key, value);
/*     */       }
/* 384 */       return fallback().put(key, value);
/*     */     }
/*     */ 
/*     */     public Object remove(Object key) {
/* 388 */       if (this.fallbackMap == null) {
/* 389 */         return RequestContext.this.remove(key);
/*     */       }
/* 391 */       return fallback().remove(key);
/*     */     }
/*     */ 
/*     */     public void putAll(Map<? extends String, ? extends Object> t)
/*     */     {
/* 396 */       for (Map.Entry e : t.entrySet())
/* 397 */         put((String)e.getKey(), e.getValue());
/*     */     }
/*     */ 
/*     */     public void clear()
/*     */     {
/* 402 */       fallback().clear();
/*     */     }
/*     */ 
/*     */     public Set<String> keySet() {
/* 406 */       return fallback().keySet();
/*     */     }
/*     */ 
/*     */     public Collection<Object> values() {
/* 410 */       return fallback().values();
/*     */     }
/*     */ 
/*     */     public Set<Map.Entry<String, Object>> entrySet() {
/* 414 */       return fallback().entrySet();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.RequestContext
 * JD-Core Version:    0.6.2
 */