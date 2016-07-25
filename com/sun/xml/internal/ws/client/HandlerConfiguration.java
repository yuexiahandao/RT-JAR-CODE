/*     */ package com.sun.xml.internal.ws.client;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.handler.MessageHandler;
/*     */ import com.sun.xml.internal.ws.handler.HandlerException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.handler.Handler;
/*     */ import javax.xml.ws.handler.LogicalHandler;
/*     */ import javax.xml.ws.handler.soap.SOAPHandler;
/*     */ 
/*     */ public class HandlerConfiguration
/*     */ {
/*     */   private final Set<String> roles;
/*     */   private final List<Handler> handlerChain;
/*     */   private final List<LogicalHandler> logicalHandlers;
/*     */   private final List<SOAPHandler> soapHandlers;
/*     */   private final List<MessageHandler> messageHandlers;
/*     */   private Set<QName> handlerKnownHeaders;
/*     */ 
/*     */   public HandlerConfiguration(Set<String> roles, List<Handler> handlerChain)
/*     */   {
/*  64 */     this.roles = roles;
/*  65 */     this.handlerChain = handlerChain;
/*  66 */     this.logicalHandlers = new ArrayList();
/*  67 */     this.soapHandlers = new ArrayList();
/*  68 */     this.messageHandlers = new ArrayList();
/*  69 */     this.handlerKnownHeaders = new HashSet();
/*     */ 
/*  71 */     for (Handler handler : handlerChain)
/*  72 */       if ((handler instanceof LogicalHandler)) {
/*  73 */         this.logicalHandlers.add((LogicalHandler)handler);
/*  74 */       } else if ((handler instanceof SOAPHandler)) {
/*  75 */         this.soapHandlers.add((SOAPHandler)handler);
/*  76 */         Set headers = ((SOAPHandler)handler).getHeaders();
/*  77 */         if (headers != null)
/*  78 */           this.handlerKnownHeaders.addAll(headers);
/*     */       }
/*  80 */       else if ((handler instanceof MessageHandler)) {
/*  81 */         this.messageHandlers.add((MessageHandler)handler);
/*  82 */         Set headers = ((MessageHandler)handler).getHeaders();
/*  83 */         if (headers != null)
/*  84 */           this.handlerKnownHeaders.addAll(headers);
/*     */       }
/*     */       else {
/*  87 */         throw new HandlerException("handler.not.valid.type", new Object[] { handler.getClass() });
/*     */       }
/*     */   }
/*     */ 
/*     */   public HandlerConfiguration(Set<String> roles, HandlerConfiguration oldConfig)
/*     */   {
/*  99 */     this.roles = roles;
/* 100 */     this.handlerChain = oldConfig.handlerChain;
/* 101 */     this.logicalHandlers = oldConfig.logicalHandlers;
/* 102 */     this.soapHandlers = oldConfig.soapHandlers;
/* 103 */     this.messageHandlers = oldConfig.messageHandlers;
/* 104 */     this.handlerKnownHeaders = oldConfig.handlerKnownHeaders;
/*     */   }
/*     */ 
/*     */   public Set<String> getRoles() {
/* 108 */     return this.roles;
/*     */   }
/*     */ 
/*     */   public List<Handler> getHandlerChain()
/*     */   {
/* 116 */     if (this.handlerChain == null)
/* 117 */       return Collections.emptyList();
/* 118 */     return new ArrayList(this.handlerChain);
/*     */   }
/*     */ 
/*     */   public List<LogicalHandler> getLogicalHandlers()
/*     */   {
/* 123 */     return this.logicalHandlers;
/*     */   }
/*     */ 
/*     */   public List<SOAPHandler> getSoapHandlers() {
/* 127 */     return this.soapHandlers;
/*     */   }
/*     */ 
/*     */   public List<MessageHandler> getMessageHandlers() {
/* 131 */     return this.messageHandlers;
/*     */   }
/*     */ 
/*     */   public Set<QName> getHandlerKnownHeaders() {
/* 135 */     return this.handlerKnownHeaders;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.HandlerConfiguration
 * JD-Core Version:    0.6.2
 */