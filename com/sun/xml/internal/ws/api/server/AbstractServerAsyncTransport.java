/*     */ package com.sun.xml.internal.ws.api.server;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.istack.internal.Nullable;
/*     */ import com.sun.xml.internal.ws.api.PropertySet;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.pipe.Codec;
/*     */ import com.sun.xml.internal.ws.util.Pool;
/*     */ import java.io.IOException;
/*     */ 
/*     */ public abstract class AbstractServerAsyncTransport<T>
/*     */ {
/*     */   private final WSEndpoint endpoint;
/*     */   private final CodecPool codecPool;
/*     */ 
/*     */   public AbstractServerAsyncTransport(WSEndpoint endpoint)
/*     */   {
/*  57 */     this.endpoint = endpoint;
/*  58 */     this.codecPool = new CodecPool(endpoint);
/*     */   }
/*     */ 
/*     */   protected Packet decodePacket(T connection, @NotNull Codec codec)
/*     */     throws IOException
/*     */   {
/*  70 */     Packet packet = new Packet();
/*  71 */     packet.acceptableMimeTypes = getAcceptableMimeTypes(connection);
/*  72 */     packet.addSatellite(getPropertySet(connection));
/*  73 */     packet.transportBackChannel = getTransportBackChannel(connection);
/*  74 */     return packet;
/*     */   }
/*     */ 
/*     */   protected abstract void encodePacket(T paramT, @NotNull Packet paramPacket, @NotNull Codec paramCodec)
/*     */     throws IOException;
/*     */ 
/*     */   @Nullable
/*     */   protected abstract String getAcceptableMimeTypes(T paramT);
/*     */ 
/*     */   @Nullable
/*     */   protected abstract TransportBackChannel getTransportBackChannel(T paramT);
/*     */ 
/*     */   @NotNull
/*     */   protected abstract PropertySet getPropertySet(T paramT);
/*     */ 
/*     */   @NotNull
/*     */   protected abstract WebServiceContextDelegate getWebServiceContextDelegate(T paramT);
/*     */ 
/*     */   protected void handle(final T connection)
/*     */     throws IOException
/*     */   {
/* 131 */     final Codec codec = (Codec)this.codecPool.take();
/* 132 */     Packet request = decodePacket(connection, codec);
/* 133 */     if (!request.getMessage().isFault())
/* 134 */       this.endpoint.schedule(request, new WSEndpoint.CompletionCallback() {
/*     */         public void onCompletion(@NotNull Packet response) {
/*     */           try {
/* 137 */             AbstractServerAsyncTransport.this.encodePacket(connection, response, codec);
/*     */           } catch (IOException ioe) {
/* 139 */             ioe.printStackTrace();
/*     */           }
/* 141 */           AbstractServerAsyncTransport.this.codecPool.recycle(codec);
/*     */         }
/*     */       });
/*     */   }
/*     */ 
/*     */   private static final class CodecPool extends Pool<Codec>
/*     */   {
/*     */     WSEndpoint endpoint;
/*     */ 
/*     */     CodecPool(WSEndpoint endpoint) {
/* 151 */       this.endpoint = endpoint;
/*     */     }
/*     */ 
/*     */     protected Codec create() {
/* 155 */       return this.endpoint.createCodec();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.api.server.AbstractServerAsyncTransport
 * JD-Core Version:    0.6.2
 */