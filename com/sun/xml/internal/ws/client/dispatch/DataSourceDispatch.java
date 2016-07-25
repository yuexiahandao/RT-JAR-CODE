/*    */ package com.sun.xml.internal.ws.client.dispatch;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.addressing.WSEndpointReference;
/*    */ import com.sun.xml.internal.ws.api.client.WSPortInfo;
/*    */ import com.sun.xml.internal.ws.api.message.Message;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.pipe.Tube;
/*    */ import com.sun.xml.internal.ws.binding.BindingImpl;
/*    */ import com.sun.xml.internal.ws.client.WSServiceDelegate;
/*    */ import com.sun.xml.internal.ws.encoding.xml.XMLMessage;
/*    */ import com.sun.xml.internal.ws.encoding.xml.XMLMessage.MessageDataSource;
/*    */ import javax.activation.DataSource;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.ws.Service.Mode;
/*    */ import javax.xml.ws.WebServiceException;
/*    */ 
/*    */ public class DataSourceDispatch extends DispatchImpl<DataSource>
/*    */ {
/*    */   @Deprecated
/*    */   public DataSourceDispatch(QName port, Service.Mode mode, WSServiceDelegate service, Tube pipe, BindingImpl binding, WSEndpointReference epr)
/*    */   {
/* 53 */     super(port, mode, service, pipe, binding, epr);
/*    */   }
/*    */ 
/*    */   public DataSourceDispatch(WSPortInfo portInfo, Service.Mode mode, BindingImpl binding, WSEndpointReference epr) {
/* 57 */     super(portInfo, mode, binding, epr);
/*    */   }
/*    */ 
/*    */   Packet createPacket(DataSource arg)
/*    */   {
/* 62 */     switch (1.$SwitchMap$javax$xml$ws$Service$Mode[this.mode.ordinal()]) {
/*    */     case 1:
/* 64 */       throw new IllegalArgumentException("DataSource use is not allowed in Service.Mode.PAYLOAD\n");
/*    */     case 2:
/* 66 */       return new Packet(XMLMessage.create(arg, this.binding));
/*    */     }
/* 68 */     throw new WebServiceException("Unrecognized message mode");
/*    */   }
/*    */ 
/*    */   DataSource toReturnValue(Packet response)
/*    */   {
/* 73 */     Message message = response.getMessage();
/* 74 */     return (message instanceof XMLMessage.MessageDataSource) ? ((XMLMessage.MessageDataSource)message).getDataSource() : XMLMessage.getDataSource(message, this.binding);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.dispatch.DataSourceDispatch
 * JD-Core Version:    0.6.2
 */