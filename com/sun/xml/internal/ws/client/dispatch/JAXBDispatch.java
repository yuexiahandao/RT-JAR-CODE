/*     */ package com.sun.xml.internal.ws.client.dispatch;
/*     */ 
/*     */ import com.sun.xml.internal.bind.api.JAXBRIContext;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*     */ import com.sun.xml.internal.ws.api.client.WSPortInfo;
/*     */ import com.sun.xml.internal.ws.api.message.Header;
/*     */ import com.sun.xml.internal.ws.api.message.Headers;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Messages;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*     */ import com.sun.xml.internal.ws.binding.BindingImpl;
/*     */ import com.sun.xml.internal.ws.client.WSServiceDelegate;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import javax.xml.bind.Marshaller;
/*     */ import javax.xml.bind.Unmarshaller;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.transform.Source;
/*     */ import javax.xml.ws.Service.Mode;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public class JAXBDispatch extends DispatchImpl<Object>
/*     */ {
/*     */   private final JAXBContext jaxbcontext;
/*     */ 
/*     */   @Deprecated
/*     */   public JAXBDispatch(QName port, JAXBContext jc, Service.Mode mode, WSServiceDelegate service, Tube pipe, BindingImpl binding, WSEndpointReference epr)
/*     */   {
/*  66 */     super(port, mode, service, pipe, binding, epr);
/*  67 */     this.jaxbcontext = jc;
/*     */   }
/*     */ 
/*     */   public JAXBDispatch(WSPortInfo portInfo, JAXBContext jc, Service.Mode mode, BindingImpl binding, WSEndpointReference epr) {
/*  71 */     super(portInfo, mode, binding, epr);
/*  72 */     this.jaxbcontext = jc;
/*     */   }
/*     */ 
/*     */   Object toReturnValue(Packet response) {
/*     */     try {
/*  77 */       Unmarshaller unmarshaller = this.jaxbcontext.createUnmarshaller();
/*  78 */       Message msg = response.getMessage();
/*  79 */       switch (1.$SwitchMap$javax$xml$ws$Service$Mode[this.mode.ordinal()]) {
/*     */       case 1:
/*  81 */         return msg.readPayloadAsJAXB(unmarshaller);
/*     */       case 2:
/*  83 */         Source result = msg.readEnvelopeAsSource();
/*  84 */         return unmarshaller.unmarshal(result);
/*     */       }
/*  86 */       throw new WebServiceException("Unrecognized dispatch mode");
/*     */     }
/*     */     catch (JAXBException e) {
/*  89 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   Packet createPacket(Object msg)
/*     */   {
/*  95 */     assert (this.jaxbcontext != null);
/*     */     try
/*     */     {
/*  98 */       Marshaller marshaller = this.jaxbcontext.createMarshaller();
/*  99 */       marshaller.setProperty("jaxb.fragment", Boolean.TRUE);
/*     */ 
/* 101 */       Message message = msg == null ? Messages.createEmpty(this.soapVersion) : Messages.create(marshaller, msg, this.soapVersion);
/* 102 */       return new Packet(message);
/*     */     } catch (JAXBException e) {
/* 104 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setOutboundHeaders(Object[] headers) {
/* 109 */     if (headers == null)
/* 110 */       throw new IllegalArgumentException();
/* 111 */     Header[] hl = new Header[headers.length];
/* 112 */     for (int i = 0; i < hl.length; i++) {
/* 113 */       if (headers[i] == null) {
/* 114 */         throw new IllegalArgumentException();
/*     */       }
/* 116 */       hl[i] = Headers.create((JAXBRIContext)this.jaxbcontext, headers[i]);
/*     */     }
/* 118 */     super.setOutboundHeaders(hl);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.dispatch.JAXBDispatch
 * JD-Core Version:    0.6.2
 */