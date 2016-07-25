/*     */ package com.sun.xml.internal.ws.handler;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import java.util.Collection;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.xml.ws.handler.MessageContext;
/*     */ import javax.xml.ws.handler.MessageContext.Scope;
/*     */ 
/*     */ public abstract class MessageUpdatableContext
/*     */   implements MessageContext
/*     */ {
/*     */   final Packet packet;
/*     */   private MessageContextImpl ctxt;
/*     */ 
/*     */   public MessageUpdatableContext(Packet packet)
/*     */   {
/*  46 */     this.ctxt = new MessageContextImpl(packet);
/*  47 */     this.packet = packet;
/*     */   }
/*     */ 
/*     */   private void fill(Packet packet)
/*     */   {
/*  54 */     this.ctxt.fill(packet);
/*     */   }
/*     */ 
/*     */   abstract void updateMessage();
/*     */ 
/*     */   Message getPacketMessage()
/*     */   {
/*  66 */     updateMessage();
/*  67 */     return this.packet.getMessage();
/*     */   }
/*     */ 
/*     */   abstract void setPacketMessage(Message paramMessage);
/*     */ 
/*     */   final void updatePacket()
/*     */   {
/*  81 */     updateMessage();
/*  82 */     fill(this.packet);
/*     */   }
/*     */ 
/*     */   MessageContextImpl getMessageContext() {
/*  86 */     return this.ctxt;
/*     */   }
/*     */ 
/*     */   public void setScope(String name, MessageContext.Scope scope) {
/*  90 */     this.ctxt.setScope(name, scope);
/*     */   }
/*     */ 
/*     */   public MessageContext.Scope getScope(String name) {
/*  94 */     return this.ctxt.getScope(name);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 100 */     this.ctxt.clear();
/*     */   }
/*     */ 
/*     */   public boolean containsKey(Object obj) {
/* 104 */     return this.ctxt.containsKey(obj);
/*     */   }
/*     */ 
/*     */   public boolean containsValue(Object obj) {
/* 108 */     return this.ctxt.containsValue(obj);
/*     */   }
/*     */ 
/*     */   public Set<Map.Entry<String, Object>> entrySet() {
/* 112 */     return this.ctxt.entrySet();
/*     */   }
/*     */ 
/*     */   public Object get(Object obj) {
/* 116 */     return this.ctxt.get(obj);
/*     */   }
/*     */ 
/*     */   public boolean isEmpty() {
/* 120 */     return this.ctxt.isEmpty();
/*     */   }
/*     */ 
/*     */   public Set<String> keySet() {
/* 124 */     return this.ctxt.keySet();
/*     */   }
/*     */ 
/*     */   public Object put(String str, Object obj) {
/* 128 */     return this.ctxt.put(str, obj);
/*     */   }
/*     */ 
/*     */   public void putAll(Map<? extends String, ? extends Object> map) {
/* 132 */     this.ctxt.putAll(map);
/*     */   }
/*     */ 
/*     */   public Object remove(Object obj) {
/* 136 */     return this.ctxt.remove(obj);
/*     */   }
/*     */ 
/*     */   public int size() {
/* 140 */     return this.ctxt.size();
/*     */   }
/*     */ 
/*     */   public Collection<Object> values() {
/* 144 */     return this.ctxt.values();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.handler.MessageUpdatableContext
 * JD-Core Version:    0.6.2
 */