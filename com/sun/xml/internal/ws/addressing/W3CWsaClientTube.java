/*    */ package com.sun.xml.internal.ws.addressing;
/*    */ 
/*    */ import com.sun.xml.internal.ws.addressing.model.MissingAddressingHeaderException;
/*    */ import com.sun.xml.internal.ws.api.WSBinding;
/*    */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*    */ import com.sun.xml.internal.ws.api.message.HeaderList;
/*    */ import com.sun.xml.internal.ws.api.message.Message;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*    */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*    */ import com.sun.xml.internal.ws.api.pipe.TubeCloner;
/*    */ 
/*    */ public class W3CWsaClientTube extends WsaClientTube
/*    */ {
/*    */   public W3CWsaClientTube(WSDLPort wsdlPort, WSBinding binding, Tube next)
/*    */   {
/* 40 */     super(wsdlPort, binding, next);
/*    */   }
/*    */ 
/*    */   public W3CWsaClientTube(WsaClientTube that, TubeCloner cloner) {
/* 44 */     super(that, cloner);
/*    */   }
/*    */ 
/*    */   public W3CWsaClientTube copy(TubeCloner cloner)
/*    */   {
/* 49 */     return new W3CWsaClientTube(this, cloner);
/*    */   }
/*    */ 
/*    */   protected void checkMandatoryHeaders(Packet packet, boolean foundAction, boolean foundTo, boolean foundReplyTo, boolean foundFaultTo, boolean foundMessageID, boolean foundRelatesTo)
/*    */   {
/* 55 */     super.checkMandatoryHeaders(packet, foundAction, foundTo, foundReplyTo, foundFaultTo, foundMessageID, foundRelatesTo);
/*    */ 
/* 60 */     if ((this.expectReply) && (packet.getMessage() != null) && (!foundRelatesTo)) {
/* 61 */       String action = packet.getMessage().getHeaders().getAction(this.addressingVersion, this.soapVersion);
/*    */ 
/* 64 */       if ((!packet.getMessage().isFault()) || (!action.equals(this.addressingVersion.getDefaultFaultAction())))
/* 65 */         throw new MissingAddressingHeaderException(this.addressingVersion.relatesToTag, packet);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.addressing.W3CWsaClientTube
 * JD-Core Version:    0.6.2
 */