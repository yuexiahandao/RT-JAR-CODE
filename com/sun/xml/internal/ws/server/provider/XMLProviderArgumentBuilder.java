/*     */ package com.sun.xml.internal.ws.server.provider;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Messages;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.encoding.xml.XMLMessage;
/*     */ import com.sun.xml.internal.ws.encoding.xml.XMLMessage.MessageDataSource;
/*     */ import com.sun.xml.internal.ws.resources.ServerMessages;
/*     */ import javax.activation.DataSource;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.ws.Service.Mode;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.http.HTTPException;
/*     */ 
/*     */ abstract class XMLProviderArgumentBuilder<T> extends ProviderArgumentsBuilder<T>
/*     */ {
/*     */   protected Packet getResponse(Packet request, Exception e, WSDLPort port, WSBinding binding)
/*     */   {
/*  51 */     Packet response = super.getResponse(request, e, port, binding);
/*  52 */     if (((e instanceof HTTPException)) && 
/*  53 */       (response.supports("javax.xml.ws.http.response.code"))) {
/*  54 */       response.put("javax.xml.ws.http.response.code", Integer.valueOf(((HTTPException)e).getStatusCode()));
/*     */     }
/*     */ 
/*  57 */     return response;
/*     */   }
/*     */ 
/*     */   static XMLProviderArgumentBuilder createBuilder(ProviderEndpointModel model, WSBinding binding) {
/*  61 */     if (model.mode == Service.Mode.PAYLOAD) {
/*  62 */       return new PayloadSource(null);
/*     */     }
/*  64 */     if (model.datatype == Source.class)
/*  65 */       return new PayloadSource(null);
/*  66 */     if (model.datatype == DataSource.class)
/*  67 */       return new DataSourceParameter(binding);
/*  68 */     throw new WebServiceException(ServerMessages.PROVIDER_INVALID_PARAMETER_TYPE(model.implClass, model.datatype));
/*     */   }
/*     */ 
/*     */   private static final class DataSourceParameter extends XMLProviderArgumentBuilder<DataSource>
/*     */   {
/*     */     private final WSBinding binding;
/*     */ 
/*     */     DataSourceParameter(WSBinding binding)
/*     */     {
/*  90 */       this.binding = binding;
/*     */     }
/*     */     public DataSource getParameter(Packet packet) {
/*  93 */       Message msg = packet.getMessage();
/*  94 */       return (msg instanceof XMLMessage.MessageDataSource) ? ((XMLMessage.MessageDataSource)msg).getDataSource() : XMLMessage.getDataSource(msg, this.binding);
/*     */     }
/*     */ 
/*     */     public Message getResponseMessage(DataSource ds)
/*     */     {
/* 100 */       return XMLMessage.create(ds, this.binding);
/*     */     }
/*     */ 
/*     */     protected Message getResponseMessage(Exception e) {
/* 104 */       return XMLMessage.create(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class PayloadSource extends XMLProviderArgumentBuilder<Source>
/*     */   {
/*     */     public Source getParameter(Packet packet)
/*     */     {
/*  74 */       return packet.getMessage().readPayloadAsSource();
/*     */     }
/*     */ 
/*     */     public Message getResponseMessage(Source source) {
/*  78 */       return Messages.createUsingPayload(source, SOAPVersion.SOAP_11);
/*     */     }
/*     */ 
/*     */     protected Message getResponseMessage(Exception e) {
/*  82 */       return XMLMessage.create(e);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.provider.XMLProviderArgumentBuilder
 * JD-Core Version:    0.6.2
 */