/*     */ package com.sun.xml.internal.ws.client;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.message.Attachment;
/*     */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class ResponseContext extends AbstractMap<String, Object>
/*     */ {
/*     */   private final Packet packet;
/*     */   private Set<Map.Entry<String, Object>> entrySet;
/*     */ 
/*     */   public ResponseContext(Packet packet)
/*     */   {
/*  83 */     this.packet = packet;
/*     */   }
/*     */ 
/*     */   public boolean containsKey(Object key) {
/*  87 */     if (this.packet.supports(key)) {
/*  88 */       return this.packet.containsKey(key);
/*     */     }
/*  90 */     if (this.packet.invocationProperties.containsKey(key))
/*     */     {
/*  92 */       return !this.packet.getHandlerScopePropertyNames(true).contains(key);
/*     */     }
/*  94 */     return false;
/*     */   }
/*     */ 
/*     */   public Object get(Object key) {
/*  98 */     if (this.packet.supports(key)) {
/*  99 */       return this.packet.get(key);
/*     */     }
/* 101 */     if (this.packet.getHandlerScopePropertyNames(true).contains(key)) {
/* 102 */       return null;
/*     */     }
/* 104 */     Object value = this.packet.invocationProperties.get(key);
/*     */ 
/* 107 */     if (key.equals("javax.xml.ws.binding.attachments.inbound")) {
/* 108 */       Map atts = (Map)value;
/* 109 */       if (atts == null)
/* 110 */         atts = new HashMap();
/* 111 */       AttachmentSet attSet = this.packet.getMessage().getAttachments();
/* 112 */       for (Attachment att : attSet) {
/* 113 */         atts.put(att.getContentId(), att.asDataHandler());
/*     */       }
/* 115 */       return atts;
/*     */     }
/* 117 */     return value;
/*     */   }
/*     */ 
/*     */   public Object put(String key, Object value)
/*     */   {
/* 122 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Object remove(Object key)
/*     */   {
/* 127 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void putAll(Map<? extends String, ? extends Object> t)
/*     */   {
/* 132 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 137 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Set<Map.Entry<String, Object>> entrySet() {
/* 141 */     if (this.entrySet == null)
/*     */     {
/* 146 */       Map r = new HashMap();
/*     */ 
/* 149 */       r.putAll(this.packet.invocationProperties);
/*     */ 
/* 152 */       r.keySet().removeAll(this.packet.getHandlerScopePropertyNames(true));
/*     */ 
/* 155 */       r.putAll(this.packet.createMapView());
/*     */ 
/* 157 */       this.entrySet = Collections.unmodifiableSet(r.entrySet());
/*     */     }
/*     */ 
/* 160 */     return this.entrySet;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.ResponseContext
 * JD-Core Version:    0.6.2
 */