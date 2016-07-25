/*     */ package com.sun.xml.internal.ws.server;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.org.glassfish.gmbal.AMXMetadata;
/*     */ import com.sun.org.glassfish.gmbal.Description;
/*     */ import com.sun.org.glassfish.gmbal.ManagedAttribute;
/*     */ import com.sun.org.glassfish.gmbal.ManagedObject;
/*     */ import com.sun.xml.internal.ws.api.BindingID;
/*     */ import com.sun.xml.internal.ws.api.EndpointAddress;
/*     */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.WSFeatureList;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.server.Container;
/*     */ import com.sun.xml.internal.ws.api.server.SDDocument;
/*     */ import com.sun.xml.internal.ws.api.server.ServiceDefinition;
/*     */ import com.sun.xml.internal.ws.api.server.WSEndpoint;
/*     */ import com.sun.xml.internal.ws.policy.PolicyMap;
/*     */ import com.sun.xml.internal.ws.transport.http.HttpAdapter;
/*     */ import com.sun.xml.internal.ws.util.RuntimeVersion;
/*     */ import com.sun.xml.internal.ws.util.Version;
/*     */ import java.net.URL;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ @ManagedObject
/*     */ @Description("Metro Web Service endpoint")
/*     */ @AMXMetadata(type="WSEndpoint")
/*     */ public final class MonitorRootService extends MonitorBase
/*     */ {
/*     */   private final WSEndpoint endpoint;
/*     */ 
/*     */   MonitorRootService(WSEndpoint endpoint)
/*     */   {
/*  98 */     this.endpoint = endpoint;
/*     */   }
/*     */ 
/*     */   @ManagedAttribute
/*     */   @Description("Policy associated with Endpoint")
/*     */   public String policy()
/*     */   {
/* 108 */     return this.endpoint.getPolicyMap() != null ? this.endpoint.getPolicyMap().toString() : null;
/*     */   }
/* 115 */   @ManagedAttribute
/*     */   @Description("Container")
/*     */   @NotNull
/*     */   public Container container() { return this.endpoint.getContainer(); } 
/*     */   @ManagedAttribute
/*     */   @Description("Port name")
/*     */   @NotNull
/*     */   public QName portName() {
/* 122 */     return this.endpoint.getPortName(); } 
/* 128 */   @ManagedAttribute
/*     */   @Description("Service name")
/*     */   @NotNull
/*     */   public QName serviceName() { return this.endpoint.getServiceName(); }
/*     */ 
/*     */ 
/*     */   @ManagedAttribute
/*     */   @Description("Binding SOAP Version")
/*     */   public String soapVersionHttpBindingId()
/*     */   {
/* 160 */     return this.endpoint.getBinding().getSOAPVersion().httpBindingId;
/*     */   }
/*     */   @ManagedAttribute
/*     */   @Description("Binding Addressing Version")
/*     */   public AddressingVersion addressingVersion() {
/* 166 */     return this.endpoint.getBinding().getAddressingVersion(); } 
/* 172 */   @ManagedAttribute
/*     */   @Description("Binding Identifier")
/*     */   @NotNull
/*     */   public BindingID bindingID() { return this.endpoint.getBinding().getBindingId(); } 
/* 178 */   @ManagedAttribute
/*     */   @Description("Binding features")
/*     */   @NotNull
/*     */   public WSFeatureList features() { return this.endpoint.getBinding().getFeatures(); }
/*     */ 
/*     */ 
/*     */   @ManagedAttribute
/*     */   @Description("WSDLPort bound port type")
/*     */   public QName wsdlPortTypeName()
/*     */   {
/* 188 */     return this.endpoint.getPort() != null ? this.endpoint.getPort().getBinding().getPortTypeName() : null;
/*     */   }
/*     */ 
/*     */   @ManagedAttribute
/*     */   @Description("Endpoint address")
/*     */   public EndpointAddress wsdlEndpointAddress() {
/* 195 */     return this.endpoint.getPort() != null ? this.endpoint.getPort().getAddress() : null;
/*     */   }
/*     */ 
/*     */   @ManagedAttribute
/*     */   @Description("Documents referenced")
/*     */   public Set<String> serviceDefinitionImports()
/*     */   {
/* 206 */     return this.endpoint.getServiceDefinition() != null ? this.endpoint.getServiceDefinition().getPrimary().getImports() : null;
/*     */   }
/*     */ 
/*     */   @ManagedAttribute
/*     */   @Description("System ID where document is taken from")
/*     */   public URL serviceDefinitionURL() {
/* 213 */     return this.endpoint.getServiceDefinition() != null ? this.endpoint.getServiceDefinition().getPrimary().getURL() : null;
/*     */   }
/*     */ 
/*     */   @ManagedAttribute
/*     */   @Description("SEI model WSDL location")
/*     */   public String seiModelWSDLLocation()
/*     */   {
/* 224 */     return this.endpoint.getSEIModel() != null ? this.endpoint.getSEIModel().getWSDLLocation() : null;
/*     */   }
/*     */ 
/*     */   @ManagedAttribute
/*     */   @Description("JAX-WS runtime version")
/*     */   public String jaxwsRuntimeVersion()
/*     */   {
/* 235 */     return RuntimeVersion.VERSION.toString();
/*     */   }
/*     */ 
/*     */   @ManagedAttribute
/*     */   @Description("If true: show what goes across HTTP transport")
/*     */   public boolean dumpHTTPMessages()
/*     */   {
/* 244 */     return HttpAdapter.dump;
/*     */   }
/* 249 */   @ManagedAttribute
/*     */   @Description("Show what goes across HTTP transport")
/*     */   public void dumpHTTPMessages(boolean x) { HttpAdapter.dump = x; }
/*     */ 
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.server.MonitorRootService
 * JD-Core Version:    0.6.2
 */