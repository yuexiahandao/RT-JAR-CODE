/*    */ package com.sun.xml.internal.ws.handler;
/*    */ 
/*    */ import com.sun.istack.internal.Nullable;
/*    */ import com.sun.xml.internal.ws.api.WSBinding;
/*    */ import com.sun.xml.internal.ws.api.handler.MessageHandlerContext;
/*    */ import com.sun.xml.internal.ws.api.message.Message;
/*    */ import com.sun.xml.internal.ws.api.message.Packet;
/*    */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class MessageHandlerContextImpl extends MessageUpdatableContext
/*    */   implements MessageHandlerContext
/*    */ {
/*    */ 
/*    */   @Nullable
/*    */   private SEIModel seiModel;
/*    */   private Set<String> roles;
/*    */   private WSBinding binding;
/*    */ 
/*    */   @Nullable
/*    */   private WSDLPort wsdlModel;
/*    */ 
/*    */   public MessageHandlerContextImpl(@Nullable SEIModel seiModel, WSBinding binding, @Nullable WSDLPort wsdlModel, Packet packet, Set<String> roles)
/*    */   {
/* 48 */     super(packet);
/* 49 */     this.seiModel = seiModel;
/* 50 */     this.binding = binding;
/* 51 */     this.wsdlModel = wsdlModel;
/* 52 */     this.roles = roles;
/*    */   }
/*    */   public Message getMessage() {
/* 55 */     return this.packet.getMessage();
/*    */   }
/*    */ 
/*    */   public void setMessage(Message message) {
/* 59 */     this.packet.setMessage(message);
/*    */   }
/*    */ 
/*    */   public Set<String> getRoles() {
/* 63 */     return this.roles;
/*    */   }
/*    */ 
/*    */   public WSBinding getWSBinding() {
/* 67 */     return this.binding;
/*    */   }
/*    */   @Nullable
/*    */   public SEIModel getSEIModel() {
/* 71 */     return this.seiModel;
/*    */   }
/*    */   @Nullable
/*    */   public WSDLPort getPort() {
/* 75 */     return this.wsdlModel;
/*    */   }
/*    */ 
/*    */   void updateMessage()
/*    */   {
/*    */   }
/*    */ 
/*    */   void setPacketMessage(Message newMessage) {
/* 83 */     setMessage(newMessage);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.handler.MessageHandlerContextImpl
 * JD-Core Version:    0.6.2
 */