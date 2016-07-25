/*    */ package com.sun.xml.internal.ws.wsdl.parser;
/*    */ 
/*    */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLFeaturedObject;
/*    */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*    */ import com.sun.xml.internal.ws.developer.MemberSubmissionAddressingFeature;
/*    */ import com.sun.xml.internal.ws.model.wsdl.WSDLBoundPortTypeImpl;
/*    */ import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
/*    */ import javax.xml.namespace.QName;
/*    */ import javax.xml.stream.XMLStreamReader;
/*    */ 
/*    */ public class MemberSubmissionAddressingWSDLParserExtension extends W3CAddressingWSDLParserExtension
/*    */ {
/*    */   public boolean bindingElements(WSDLBoundPortType binding, XMLStreamReader reader)
/*    */   {
/* 50 */     return addressibleElement(reader, binding);
/*    */   }
/*    */ 
/*    */   public boolean portElements(WSDLPort port, XMLStreamReader reader)
/*    */   {
/* 55 */     return addressibleElement(reader, port);
/*    */   }
/*    */ 
/*    */   private boolean addressibleElement(XMLStreamReader reader, WSDLFeaturedObject binding) {
/* 59 */     QName ua = reader.getName();
/* 60 */     if (ua.equals(AddressingVersion.MEMBER.wsdlExtensionTag)) {
/* 61 */       String required = reader.getAttributeValue("http://schemas.xmlsoap.org/wsdl/", "required");
/* 62 */       binding.addFeature(new MemberSubmissionAddressingFeature(Boolean.parseBoolean(required)));
/* 63 */       XMLStreamReaderUtil.skipElement(reader);
/* 64 */       return true;
/*    */     }
/*    */ 
/* 67 */     return false;
/*    */   }
/*    */ 
/*    */   public boolean bindingOperationElements(WSDLBoundOperation operation, XMLStreamReader reader)
/*    */   {
/* 72 */     return false;
/*    */   }
/*    */ 
/*    */   protected void patchAnonymousDefault(WSDLBoundPortTypeImpl binding)
/*    */   {
/*    */   }
/*    */ 
/*    */   protected String getNamespaceURI()
/*    */   {
/* 81 */     return AddressingVersion.MEMBER.wsdlNsUri;
/*    */   }
/*    */ 
/*    */   protected QName getWsdlActionTag()
/*    */   {
/* 86 */     return AddressingVersion.MEMBER.wsdlActionTag;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.parser.MemberSubmissionAddressingWSDLParserExtension
 * JD-Core Version:    0.6.2
 */