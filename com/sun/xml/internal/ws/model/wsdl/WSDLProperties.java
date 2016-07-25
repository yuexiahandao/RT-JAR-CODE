/*    */ package com.sun.xml.internal.ws.model.wsdl;
/*    */ 
/*    */ import com.sun.istack.internal.NotNull;
/*    */ import com.sun.xml.internal.ws.api.PropertySet;
/*    */ import com.sun.xml.internal.ws.api.PropertySet.Property;
/*    */ import com.sun.xml.internal.ws.api.PropertySet.PropertyMap;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLService;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ public final class WSDLProperties extends PropertySet
/*    */ {
/* 45 */   private static final PropertySet.PropertyMap model = parse(WSDLProperties.class);
/*    */ 
/*    */   @NotNull
/*    */   private final WSDLPort port;
/*    */ 
/* 51 */   public WSDLProperties(@NotNull WSDLPort port) { this.port = port; }
/*    */ 
/*    */   @PropertySet.Property({"javax.xml.ws.wsdl.service"})
/*    */   public QName getWSDLService()
/*    */   {
/* 56 */     return this.port.getOwner().getName();
/*    */   }
/*    */ 
/*    */   @PropertySet.Property({"javax.xml.ws.wsdl.port"})
/*    */   public QName getWSDLPort() {
/* 61 */     return this.port.getName();
/*    */   }
/*    */ 
/*    */   @PropertySet.Property({"javax.xml.ws.wsdl.interface"})
/*    */   public QName getWSDLPortType() {
/* 66 */     return this.port.getBinding().getPortTypeName();
/*    */   }
/*    */ 
/*    */   protected PropertySet.PropertyMap getPropertyMap()
/*    */   {
/* 71 */     return model;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.model.wsdl.WSDLProperties
 * JD-Core Version:    0.6.2
 */