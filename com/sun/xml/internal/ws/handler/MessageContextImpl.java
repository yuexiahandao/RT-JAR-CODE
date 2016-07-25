/*     */ package com.sun.xml.internal.ws.handler;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.message.Attachment;
/*     */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.util.ReadOnlyPropertyException;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.xml.ws.handler.MessageContext;
/*     */ import javax.xml.ws.handler.MessageContext.Scope;
/*     */ 
/*     */ class MessageContextImpl
/*     */   implements MessageContext
/*     */ {
/*  46 */   private Map<String, Object> fallbackMap = null;
/*     */   private Set<String> handlerScopeProps;
/*     */   Packet packet;
/*     */ 
/*     */   void fallback()
/*     */   {
/*  52 */     if (this.fallbackMap == null) {
/*  53 */       this.fallbackMap = new HashMap();
/*  54 */       this.fallbackMap.putAll(this.packet.createMapView());
/*  55 */       this.fallbackMap.putAll(this.packet.invocationProperties);
/*     */     }
/*     */   }
/*     */ 
/*     */   public MessageContextImpl(Packet packet) {
/*  60 */     this.packet = packet;
/*  61 */     this.handlerScopeProps = packet.getHandlerScopePropertyNames(false);
/*     */   }
/*     */   protected void updatePacket() {
/*  64 */     throw new UnsupportedOperationException("wrong call");
/*     */   }
/*     */ 
/*     */   public void setScope(String name, MessageContext.Scope scope) {
/*  68 */     if (!containsKey(name))
/*  69 */       throw new IllegalArgumentException("Property " + name + " does not exist.");
/*  70 */     if (scope == MessageContext.Scope.APPLICATION)
/*  71 */       this.handlerScopeProps.remove(name);
/*     */     else
/*  73 */       this.handlerScopeProps.add(name);
/*     */   }
/*     */ 
/*     */   public MessageContext.Scope getScope(String name)
/*     */   {
/*  79 */     if (!containsKey(name))
/*  80 */       throw new IllegalArgumentException("Property " + name + " does not exist.");
/*  81 */     if (this.handlerScopeProps.contains(name)) {
/*  82 */       return MessageContext.Scope.HANDLER;
/*     */     }
/*  84 */     return MessageContext.Scope.APPLICATION;
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/*  89 */     fallback();
/*  90 */     return this.fallbackMap.size();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty() {
/*  94 */     fallback();
/*  95 */     return this.fallbackMap.isEmpty();
/*     */   }
/*     */ 
/*     */   public boolean containsKey(Object key) {
/*  99 */     if (this.fallbackMap == null) {
/* 100 */       if (this.packet.supports(key))
/* 101 */         return true;
/* 102 */       return this.packet.invocationProperties.containsKey(key);
/*     */     }
/* 104 */     fallback();
/* 105 */     return this.fallbackMap.containsKey(key);
/*     */   }
/*     */ 
/*     */   public boolean containsValue(Object value)
/*     */   {
/* 110 */     fallback();
/* 111 */     return this.fallbackMap.containsValue(value);
/*     */   }
/*     */ 
/*     */   public Object put(String key, Object value) {
/* 115 */     if (this.fallbackMap == null) {
/* 116 */       if (this.packet.supports(key)) {
/* 117 */         return this.packet.put(key, value);
/*     */       }
/* 119 */       if (!this.packet.invocationProperties.containsKey(key))
/*     */       {
/* 121 */         this.handlerScopeProps.add(key);
/*     */       }
/* 123 */       return this.packet.invocationProperties.put(key, value);
/*     */     }
/*     */ 
/* 126 */     fallback();
/* 127 */     if (!this.fallbackMap.containsKey(key))
/*     */     {
/* 129 */       this.handlerScopeProps.add(key);
/*     */     }
/* 131 */     return this.fallbackMap.put(key, value);
/*     */   }
/*     */ 
/*     */   public Object get(Object key) {
/* 135 */     if (key == null)
/* 136 */       return null;
/*     */     Object value;
/*     */     Object value;
/* 138 */     if (this.fallbackMap == null)
/*     */     {
/*     */       Object value;
/* 139 */       if (this.packet.supports(key))
/* 140 */         value = this.packet.get(key);
/*     */       else
/* 142 */         value = this.packet.invocationProperties.get(key);
/*     */     }
/*     */     else {
/* 145 */       fallback();
/* 146 */       value = this.fallbackMap.get(key);
/*     */     }
/*     */ 
/* 149 */     if ((key.equals("javax.xml.ws.binding.attachments.outbound")) || (key.equals("javax.xml.ws.binding.attachments.inbound")))
/*     */     {
/* 151 */       Map atts = (Map)value;
/* 152 */       if (atts == null)
/* 153 */         atts = new HashMap();
/* 154 */       AttachmentSet attSet = this.packet.getMessage().getAttachments();
/* 155 */       for (Attachment att : attSet) {
/* 156 */         atts.put(att.getContentId(), att.asDataHandler());
/*     */       }
/* 158 */       return atts;
/*     */     }
/* 160 */     return value;
/*     */   }
/*     */ 
/*     */   public void putAll(Map<? extends String, ? extends Object> t) {
/* 164 */     fallback();
/* 165 */     for (String key : t.keySet()) {
/* 166 */       if (!this.fallbackMap.containsKey(key))
/*     */       {
/* 168 */         this.handlerScopeProps.add(key);
/*     */       }
/*     */     }
/* 171 */     this.fallbackMap.putAll(t);
/*     */   }
/*     */ 
/*     */   public void clear() {
/* 175 */     fallback();
/* 176 */     this.fallbackMap.clear();
/*     */   }
/*     */   public Object remove(Object key) {
/* 179 */     fallback();
/* 180 */     this.handlerScopeProps.remove(key);
/* 181 */     return this.fallbackMap.remove(key);
/*     */   }
/*     */   public Set<String> keySet() {
/* 184 */     fallback();
/* 185 */     return this.fallbackMap.keySet();
/*     */   }
/*     */   public Set<Map.Entry<String, Object>> entrySet() {
/* 188 */     fallback();
/* 189 */     return this.fallbackMap.entrySet();
/*     */   }
/*     */   public Collection<Object> values() {
/* 192 */     fallback();
/* 193 */     return this.fallbackMap.values();
/*     */   }
/*     */ 
/*     */   void fill(Packet packet)
/*     */   {
/* 201 */     if (this.fallbackMap != null) {
/* 202 */       for (Map.Entry entry : this.fallbackMap.entrySet()) {
/* 203 */         String key = (String)entry.getKey();
/* 204 */         if (packet.supports(key))
/*     */           try {
/* 206 */             packet.put(key, entry.getValue());
/*     */           }
/*     */           catch (ReadOnlyPropertyException e) {
/*     */           }
/*     */         else {
/* 211 */           packet.invocationProperties.put(key, entry.getValue());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 216 */       packet.createMapView().keySet().retainAll(this.fallbackMap.keySet());
/* 217 */       packet.invocationProperties.keySet().retainAll(this.fallbackMap.keySet());
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.handler.MessageContextImpl
 * JD-Core Version:    0.6.2
 */