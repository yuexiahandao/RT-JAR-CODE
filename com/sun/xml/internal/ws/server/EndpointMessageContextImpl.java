/*     */ package com.sun.xml.internal.ws.server;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.message.Attachment;
/*     */ import com.sun.xml.internal.ws.api.message.AttachmentSet;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import java.util.AbstractMap;
/*     */ import java.util.AbstractSet;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.xml.ws.handler.MessageContext;
/*     */ import javax.xml.ws.handler.MessageContext.Scope;
/*     */ 
/*     */ public final class EndpointMessageContextImpl extends AbstractMap<String, Object>
/*     */   implements MessageContext
/*     */ {
/*     */   private Set<Map.Entry<String, Object>> entrySet;
/*     */   private final Packet packet;
/*     */ 
/*     */   public EndpointMessageContextImpl(Packet packet)
/*     */   {
/*  68 */     this.packet = packet;
/*     */   }
/*     */ 
/*     */   public Object get(Object key)
/*     */   {
/*  73 */     if (this.packet.supports(key)) {
/*  74 */       return this.packet.get(key);
/*     */     }
/*  76 */     if (this.packet.getHandlerScopePropertyNames(true).contains(key)) {
/*  77 */       return null;
/*     */     }
/*  79 */     Object value = this.packet.invocationProperties.get(key);
/*     */ 
/*  82 */     if ((key.equals("javax.xml.ws.binding.attachments.outbound")) || (key.equals("javax.xml.ws.binding.attachments.inbound")))
/*     */     {
/*  84 */       Map atts = (Map)value;
/*  85 */       if (atts == null)
/*  86 */         atts = new HashMap();
/*  87 */       AttachmentSet attSet = this.packet.getMessage().getAttachments();
/*  88 */       for (Attachment att : attSet) {
/*  89 */         atts.put(att.getContentId(), att.asDataHandler());
/*     */       }
/*  91 */       return atts;
/*     */     }
/*  93 */     return value;
/*     */   }
/*     */ 
/*     */   public Object put(String key, Object value)
/*     */   {
/*  98 */     if (this.packet.supports(key)) {
/*  99 */       return this.packet.put(key, value);
/*     */     }
/* 101 */     Object old = this.packet.invocationProperties.get(key);
/* 102 */     if (old != null) {
/* 103 */       if (this.packet.getHandlerScopePropertyNames(true).contains(key)) {
/* 104 */         throw new IllegalArgumentException("Cannot overwrite property in HANDLER scope");
/*     */       }
/*     */ 
/* 107 */       this.packet.invocationProperties.put(key, value);
/* 108 */       return old;
/*     */     }
/*     */ 
/* 111 */     this.packet.invocationProperties.put(key, value);
/* 112 */     return null;
/*     */   }
/*     */ 
/*     */   public Object remove(Object key)
/*     */   {
/* 117 */     if (this.packet.supports(key)) {
/* 118 */       return this.packet.remove(key);
/*     */     }
/* 120 */     Object old = this.packet.invocationProperties.get(key);
/* 121 */     if (old != null) {
/* 122 */       if (this.packet.getHandlerScopePropertyNames(true).contains(key)) {
/* 123 */         throw new IllegalArgumentException("Cannot remove property in HANDLER scope");
/*     */       }
/*     */ 
/* 126 */       this.packet.invocationProperties.remove(key);
/* 127 */       return old;
/*     */     }
/*     */ 
/* 130 */     return null;
/*     */   }
/*     */ 
/*     */   public Set<Map.Entry<String, Object>> entrySet() {
/* 134 */     if (this.entrySet == null) {
/* 135 */       this.entrySet = new EntrySet(null);
/*     */     }
/* 137 */     return this.entrySet;
/*     */   }
/*     */ 
/*     */   public void setScope(String name, MessageContext.Scope scope) {
/* 141 */     throw new UnsupportedOperationException("All the properties in this context are in APPLICATION scope. Cannot do setScope().");
/*     */   }
/*     */ 
/*     */   public MessageContext.Scope getScope(String name)
/*     */   {
/* 146 */     throw new UnsupportedOperationException("All the properties in this context are in APPLICATION scope. Cannot do getScope().");
/*     */   }
/*     */ 
/*     */   private Map<String, Object> createBackupMap()
/*     */   {
/* 181 */     Map backupMap = new HashMap();
/* 182 */     backupMap.putAll(this.packet.createMapView());
/* 183 */     Set handlerProps = this.packet.getHandlerScopePropertyNames(true);
/* 184 */     for (Map.Entry e : this.packet.invocationProperties.entrySet()) {
/* 185 */       if (!handlerProps.contains(e.getKey())) {
/* 186 */         backupMap.put(e.getKey(), e.getValue());
/*     */       }
/*     */     }
/* 189 */     return backupMap;
/*     */   }
/*     */ 
/*     */   private class EntrySet extends AbstractSet<Map.Entry<String, Object>>
/*     */   {
/*     */     private EntrySet()
/*     */     {
/*     */     }
/*     */ 
/*     */     public Iterator<Map.Entry<String, Object>> iterator()
/*     */     {
/* 153 */       final Iterator it = EndpointMessageContextImpl.this.createBackupMap().entrySet().iterator();
/*     */ 
/* 155 */       return new Iterator() {
/*     */         Map.Entry<String, Object> cur;
/*     */ 
/*     */         public boolean hasNext() {
/* 159 */           return it.hasNext();
/*     */         }
/*     */ 
/*     */         public Map.Entry<String, Object> next() {
/* 163 */           this.cur = ((Map.Entry)it.next());
/* 164 */           return this.cur;
/*     */         }
/*     */ 
/*     */         public void remove() {
/* 168 */           it.remove();
/* 169 */           EndpointMessageContextImpl.this.remove(this.cur.getKey());
/*     */         }
/*     */       };
/*     */     }
/*     */ 
/*     */     public int size() {
/* 175 */       return EndpointMessageContextImpl.this.createBackupMap().size();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.EndpointMessageContextImpl
 * JD-Core Version:    0.6.2
 */