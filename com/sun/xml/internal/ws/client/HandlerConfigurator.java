/*     */ package com.sun.xml.internal.ws.client;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.client.WSPortInfo;
/*     */ import com.sun.xml.internal.ws.binding.BindingImpl;
/*     */ import com.sun.xml.internal.ws.handler.HandlerChainsModel;
/*     */ import com.sun.xml.internal.ws.util.HandlerAnnotationInfo;
/*     */ import com.sun.xml.internal.ws.util.HandlerAnnotationProcessor;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.ws.handler.Handler;
/*     */ import javax.xml.ws.handler.HandlerResolver;
/*     */ import javax.xml.ws.handler.PortInfo;
/*     */ import javax.xml.ws.soap.SOAPBinding;
/*     */ 
/*     */ abstract class HandlerConfigurator
/*     */ {
/*     */   abstract void configureHandlers(@NotNull WSPortInfo paramWSPortInfo, @NotNull BindingImpl paramBindingImpl);
/*     */ 
/*     */   abstract HandlerResolver getResolver();
/*     */ 
/*     */   static final class AnnotationConfigurator extends HandlerConfigurator
/*     */   {
/*     */     private final HandlerChainsModel handlerModel;
/* 116 */     private final Map<WSPortInfo, HandlerAnnotationInfo> chainMap = new HashMap();
/* 117 */     private static final Logger logger = Logger.getLogger("com.sun.xml.internal.ws.handler");
/*     */ 
/*     */     AnnotationConfigurator(WSServiceDelegate delegate)
/*     */     {
/* 121 */       this.handlerModel = HandlerAnnotationProcessor.buildHandlerChainsModel(delegate.getServiceClass());
/* 122 */       assert (this.handlerModel != null);
/*     */     }
/*     */ 
/*     */     void configureHandlers(WSPortInfo port, BindingImpl binding)
/*     */     {
/* 128 */       HandlerAnnotationInfo chain = (HandlerAnnotationInfo)this.chainMap.get(port);
/*     */ 
/* 130 */       if (chain == null) {
/* 131 */         logGetChain(port);
/*     */ 
/* 133 */         chain = this.handlerModel.getHandlersForPortInfo(port);
/* 134 */         this.chainMap.put(port, chain);
/*     */       }
/*     */ 
/* 137 */       if ((binding instanceof SOAPBinding)) {
/* 138 */         ((SOAPBinding)binding).setRoles(chain.getRoles());
/*     */       }
/*     */ 
/* 141 */       logSetChain(port, chain);
/* 142 */       binding.setHandlerChain(chain.getHandlers());
/*     */     }
/*     */ 
/*     */     HandlerResolver getResolver() {
/* 146 */       return new HandlerResolver() {
/*     */         public List<Handler> getHandlerChain(PortInfo portInfo) {
/* 148 */           return new ArrayList(HandlerConfigurator.AnnotationConfigurator.this.handlerModel.getHandlersForPortInfo(portInfo).getHandlers());
/*     */         }
/*     */       };
/*     */     }
/*     */ 
/*     */     private void logSetChain(WSPortInfo info, HandlerAnnotationInfo chain)
/*     */     {
/* 155 */       logger.finer("Setting chain of length " + chain.getHandlers().size() + " for port info");
/*     */ 
/* 157 */       logPortInfo(info, Level.FINER);
/*     */     }
/*     */ 
/*     */     private void logGetChain(WSPortInfo info)
/*     */     {
/* 162 */       logger.fine("No handler chain found for port info:");
/* 163 */       logPortInfo(info, Level.FINE);
/* 164 */       logger.fine("Existing handler chains:");
/* 165 */       if (this.chainMap.isEmpty())
/* 166 */         logger.fine("none");
/*     */       else
/* 168 */         for (WSPortInfo key : this.chainMap.keySet()) {
/* 169 */           logger.fine(((HandlerAnnotationInfo)this.chainMap.get(key)).getHandlers().size() + " handlers for port info ");
/*     */ 
/* 171 */           logPortInfo(key, Level.FINE);
/*     */         }
/*     */     }
/*     */ 
/*     */     private void logPortInfo(WSPortInfo info, Level level)
/*     */     {
/* 177 */       logger.log(level, "binding: " + info.getBindingID() + "\nservice: " + info.getServiceName() + "\nport: " + info.getPortName());
/*     */     }
/*     */   }
/*     */ 
/*     */   static final class HandlerResolverImpl extends HandlerConfigurator
/*     */   {
/*     */ 
/*     */     @Nullable
/*     */     private final HandlerResolver resolver;
/*     */ 
/*     */     public HandlerResolverImpl(HandlerResolver resolver)
/*     */     {
/*  81 */       this.resolver = resolver;
/*     */     }
/*     */ 
/*     */     void configureHandlers(@NotNull WSPortInfo port, @NotNull BindingImpl binding) {
/*  85 */       if (this.resolver != null)
/*  86 */         binding.setHandlerChain(this.resolver.getHandlerChain(port));
/*     */     }
/*     */ 
/*     */     HandlerResolver getResolver()
/*     */     {
/*  91 */       return this.resolver;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.HandlerConfigurator
 * JD-Core Version:    0.6.2
 */