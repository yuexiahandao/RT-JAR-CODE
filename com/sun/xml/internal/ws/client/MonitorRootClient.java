/*    */ package com.sun.xml.internal.ws.client;
/*    */ 
/*    */ import com.sun.org.glassfish.gmbal.AMXMetadata;
/*    */ import com.sun.org.glassfish.gmbal.Description;
/*    */ import com.sun.org.glassfish.gmbal.ManagedAttribute;
/*    */ import com.sun.org.glassfish.gmbal.ManagedObject;
/*    */ import com.sun.xml.internal.ws.api.server.Container;
/*    */ import com.sun.xml.internal.ws.model.wsdl.WSDLServiceImpl;
/*    */ import com.sun.xml.internal.ws.server.MonitorBase;
/*    */ import java.net.URL;
/*    */ import java.util.Map;
/*    */ import javax.xml.namespace.QName;
/*    */ 
/*    */ @ManagedObject
/*    */ @Description("Metro Web Service client")
/*    */ @AMXMetadata(type="WSClient")
/*    */ public final class MonitorRootClient extends MonitorBase
/*    */ {
/*    */   private final Stub stub;
/*    */ 
/*    */   MonitorRootClient(Stub stub)
/*    */   {
/* 57 */     this.stub = stub;
/*    */   }
/*    */ 
/*    */   @ManagedAttribute
/*    */   private Container getContainer()
/*    */   {
/* 70 */     return this.stub.owner.getContainer();
/*    */   }
/* 73 */   @ManagedAttribute
/*    */   private Map<QName, PortInfo> qnameToPortInfoMap() { return this.stub.owner.getQNameToPortInfoMap(); } 
/*    */   @ManagedAttribute
/*    */   private QName serviceName() {
/* 76 */     return this.stub.owner.getServiceName();
/*    */   }
/* 79 */   @ManagedAttribute
/*    */   private Class serviceClass() { return this.stub.owner.getServiceClass(); } 
/*    */   @ManagedAttribute
/*    */   private URL wsdlDocumentLocation() {
/* 82 */     return this.stub.owner.getWSDLDocumentLocation();
/*    */   }
/* 85 */   @ManagedAttribute
/*    */   private WSDLServiceImpl wsdlService() { return this.stub.owner.getWsdlService(); }
/*    */ 
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.client.MonitorRootClient
 * JD-Core Version:    0.6.2
 */