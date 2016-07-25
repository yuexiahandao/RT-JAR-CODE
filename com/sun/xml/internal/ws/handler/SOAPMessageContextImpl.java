/*     */ package com.sun.xml.internal.ws.handler;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.message.Header;
/*     */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import com.sun.xml.internal.ws.message.saaj.SAAJMessage;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.xml.bind.JAXBContext;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.soap.SOAPException;
/*     */ import javax.xml.soap.SOAPMessage;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.handler.soap.SOAPMessageContext;
/*     */ 
/*     */ class SOAPMessageContextImpl extends MessageUpdatableContext
/*     */   implements SOAPMessageContext
/*     */ {
/*     */   private Set<String> roles;
/*  57 */   private SOAPMessage soapMsg = null;
/*     */   private WSBinding binding;
/*     */ 
/*     */   public SOAPMessageContextImpl(WSBinding binding, Packet packet, Set<String> roles)
/*     */   {
/*  61 */     super(packet);
/*  62 */     this.binding = binding;
/*  63 */     this.roles = roles;
/*     */   }
/*     */ 
/*     */   public SOAPMessage getMessage() {
/*  67 */     if (this.soapMsg == null) {
/*     */       try {
/*  69 */         this.soapMsg = this.packet.getMessage().readAsSOAPMessage();
/*     */       } catch (SOAPException e) {
/*  71 */         throw new WebServiceException(e);
/*     */       }
/*     */     }
/*  74 */     return this.soapMsg;
/*     */   }
/*     */ 
/*     */   public void setMessage(SOAPMessage soapMsg) {
/*     */     try {
/*  79 */       this.soapMsg = soapMsg;
/*     */     } catch (Exception e) {
/*  81 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   void setPacketMessage(Message newMessage) {
/*  86 */     if (newMessage != null) {
/*  87 */       this.packet.setMessage(newMessage);
/*  88 */       this.soapMsg = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void updateMessage()
/*     */   {
/*  95 */     if (this.soapMsg != null) {
/*  96 */       this.packet.setMessage(new SAAJMessage(this.soapMsg));
/*  97 */       this.soapMsg = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object[] getHeaders(QName header, JAXBContext jaxbContext, boolean allRoles) {
/* 102 */     SOAPVersion soapVersion = this.binding.getSOAPVersion();
/*     */ 
/* 104 */     List beanList = new ArrayList();
/*     */     try {
/* 106 */       Iterator itr = this.packet.getMessage().getHeaders().getHeaders(header, false);
/* 107 */       if (allRoles) {
/* 108 */         while (itr.hasNext()) {
/* 109 */           beanList.add(((Header)itr.next()).readAsJAXB(jaxbContext.createUnmarshaller()));
/*     */         }
/*     */       }
/* 112 */       while (itr.hasNext()) {
/* 113 */         Header soapHeader = (Header)itr.next();
/*     */ 
/* 115 */         String role = soapHeader.getRole(soapVersion);
/* 116 */         if (getRoles().contains(role)) {
/* 117 */           beanList.add(soapHeader.readAsJAXB(jaxbContext.createUnmarshaller()));
/*     */         }
/*     */       }
/*     */ 
/* 121 */       return beanList.toArray();
/*     */     } catch (Exception e) {
/* 123 */       throw new WebServiceException(e);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Set<String> getRoles() {
/* 128 */     return this.roles;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.handler.SOAPMessageContextImpl
 * JD-Core Version:    0.6.2
 */