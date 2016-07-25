/*     */ package com.sun.xml.internal.ws.api.pipe;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.EndpointAddress;
/*     */ import com.sun.xml.internal.ws.api.pipe.helper.PipeAdapter;
/*     */ import com.sun.xml.internal.ws.transport.http.client.HttpTransportPipe;
/*     */ import com.sun.xml.internal.ws.util.ServiceFinder;
/*     */ import java.net.URI;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public abstract class TransportTubeFactory
/*     */ {
/* 136 */   private static final Logger logger = Logger.getLogger(TransportTubeFactory.class.getName());
/*     */ 
/*     */   public abstract Tube doCreate(@NotNull ClientTubeAssemblerContext paramClientTubeAssemblerContext);
/*     */ 
/*     */   public static Tube create(@Nullable ClassLoader classLoader, @NotNull ClientTubeAssemblerContext context)
/*     */   {
/* 106 */     for (TransportTubeFactory factory : ServiceFinder.find(TransportTubeFactory.class, classLoader)) {
/* 107 */       Tube tube = factory.doCreate(context);
/* 108 */       if (tube != null) {
/* 109 */         logger.fine(factory.getClass() + " successfully created " + tube);
/* 110 */         return tube;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 115 */     ClientPipeAssemblerContext ctxt = new ClientPipeAssemblerContext(context.getAddress(), context.getWsdlModel(), context.getService(), context.getBinding(), context.getContainer());
/*     */ 
/* 118 */     for (TransportPipeFactory factory : ServiceFinder.find(TransportPipeFactory.class, classLoader)) {
/* 119 */       Pipe pipe = factory.doCreate(ctxt);
/* 120 */       if (pipe != null) {
/* 121 */         logger.fine(factory.getClass() + " successfully created " + pipe);
/* 122 */         return PipeAdapter.adapt(pipe);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 127 */     String scheme = context.getAddress().getURI().getScheme();
/* 128 */     if ((scheme != null) && (
/* 129 */       (scheme.equalsIgnoreCase("http")) || (scheme.equalsIgnoreCase("https")))) {
/* 130 */       return new HttpTransportPipe(context.getCodec(), context.getBinding());
/*     */     }
/*     */ 
/* 133 */     throw new WebServiceException("Unsupported endpoint address: " + context.getAddress());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.pipe.TransportTubeFactory
 * JD-Core Version:    0.6.2
 */