/*    */ package com.sun.xml.internal.ws.wsdl.parser;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*    */ import com.sun.xml.internal.ws.model.wsdl.WSDLBoundPortTypeImpl;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ public class W3CAddressingMetadataWSDLParserExtension extends W3CAddressingWSDLParserExtension
/*    */ {
/* 43 */   String METADATA_WSDL_EXTN_NS = "http://www.w3.org/2007/05/addressing/metadata";
/* 44 */   QName METADATA_WSDL_ACTION_TAG = new QName(this.METADATA_WSDL_EXTN_NS, "Action", "wsam");
/*    */ 
/*    */   public boolean bindingElements(WSDLBoundPortType binding, XMLStreamReader reader) {
/* 47 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean portElements(WSDLPort port, XMLStreamReader reader)
/*    */   {
/* 52 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean bindingOperationElements(WSDLBoundOperation operation, XMLStreamReader reader)
/*    */   {
/* 57 */     return false;
/*    */   }
/*    */ 
/*    */   protected void patchAnonymousDefault(WSDLBoundPortTypeImpl binding)
/*    */   {
/*    */   }
/*    */ 
/*    */   protected String getNamespaceURI()
/*    */   {
/* 66 */     return this.METADATA_WSDL_EXTN_NS;
/*    */   }
/*    */ 
/*    */   protected QName getWsdlActionTag()
/*    */   {
/* 71 */     return this.METADATA_WSDL_ACTION_TAG;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.parser.W3CAddressingMetadataWSDLParserExtension
 * JD-Core Version:    0.6.2
 */