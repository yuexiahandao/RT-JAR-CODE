/*     */ package com.sun.xml.internal.ws.addressing;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.ws.api.PropertySet;
/*     */ import com.sun.xml.internal.ws.api.PropertySet.Property;
/*     */ import com.sun.xml.internal.ws.api.PropertySet.PropertyMap;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*     */ import com.sun.xml.internal.ws.api.message.Header;
/*     */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*     */ import com.sun.xml.internal.ws.api.message.Message;
/*     */ import com.sun.xml.internal.ws.api.message.Packet;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ 
/*     */ public class WsaPropertyBag extends PropertySet
/*     */ {
/*     */ 
/*     */   @NotNull
/*     */   private final AddressingVersion addressingVersion;
/*     */ 
/*     */   @NotNull
/*     */   private final SOAPVersion soapVersion;
/*     */ 
/*     */   @NotNull
/*     */   private final Packet packet;
/* 125 */   private static final PropertySet.PropertyMap model = parse(WsaPropertyBag.class);
/*     */ 
/*     */   WsaPropertyBag(AddressingVersion addressingVersion, SOAPVersion soapVersion, Packet packet)
/*     */   {
/*  59 */     this.addressingVersion = addressingVersion;
/*  60 */     this.soapVersion = soapVersion;
/*  61 */     this.packet = packet;
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"com.sun.xml.internal.ws.api.addressing.to"})
/*     */   public String getTo()
/*     */     throws XMLStreamException
/*     */   {
/*  72 */     Header h = this.packet.getMessage().getHeaders().get(this.addressingVersion.toTag, false);
/*  73 */     if (h == null) return null;
/*  74 */     return h.getStringContent();
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"com.sun.xml.internal.ws.api.addressing.from"})
/*     */   public WSEndpointReference getFrom()
/*     */     throws XMLStreamException
/*     */   {
/*  85 */     return getEPR(this.addressingVersion.fromTag);
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"com.sun.xml.internal.ws.api.addressing.action"})
/*     */   public String getAction()
/*     */   {
/*  96 */     Header h = this.packet.getMessage().getHeaders().get(this.addressingVersion.actionTag, false);
/*  97 */     if (h == null) return null;
/*  98 */     return h.getStringContent();
/*     */   }
/*     */ 
/*     */   @PropertySet.Property({"com.sun.xml.internal.ws.api.addressing.messageId", "com.sun.xml.internal.ws.addressing.request.messageID"})
/*     */   public String getMessageID()
/*     */   {
/* 110 */     return this.packet.getMessage().getHeaders().getMessageID(this.addressingVersion, this.soapVersion);
/*     */   }
/*     */ 
/*     */   private WSEndpointReference getEPR(QName tag) throws XMLStreamException {
/* 114 */     Header h = this.packet.getMessage().getHeaders().get(tag, false);
/* 115 */     if (h == null) return null;
/* 116 */     return h.readAsEPR(this.addressingVersion);
/*     */   }
/*     */ 
/*     */   protected PropertySet.PropertyMap getPropertyMap() {
/* 120 */     return model;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.WsaPropertyBag
 * JD-Core Version:    0.6.2
 */