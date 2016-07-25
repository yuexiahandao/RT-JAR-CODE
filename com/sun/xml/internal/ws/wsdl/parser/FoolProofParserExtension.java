/*     */ package com.sun.xml.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundFault;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLFault;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLInput;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLMessage;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPortType;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLService;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ 
/*     */ final class FoolProofParserExtension extends DelegatingParserExtension
/*     */ {
/*     */   public FoolProofParserExtension(WSDLParserExtension core)
/*     */   {
/*  52 */     super(core);
/*     */   }
/*     */ 
/*     */   private QName pre(XMLStreamReader xsr) {
/*  56 */     return xsr.getName();
/*     */   }
/*     */ 
/*     */   private boolean post(QName tagName, XMLStreamReader xsr, boolean result) {
/*  60 */     if (!tagName.equals(xsr.getName()))
/*  61 */       return foundFool();
/*  62 */     if (result) {
/*  63 */       if (xsr.getEventType() != 2)
/*  64 */         foundFool();
/*     */     }
/*  66 */     else if (xsr.getEventType() != 1) {
/*  67 */       foundFool();
/*     */     }
/*  69 */     return result;
/*     */   }
/*     */ 
/*     */   private boolean foundFool() {
/*  73 */     throw new AssertionError("XMLStreamReader is placed at the wrong place after invoking " + this.core);
/*     */   }
/*     */ 
/*     */   public boolean serviceElements(WSDLService service, XMLStreamReader reader) {
/*  77 */     return post(pre(reader), reader, super.serviceElements(service, reader));
/*     */   }
/*     */ 
/*     */   public boolean portElements(WSDLPort port, XMLStreamReader reader) {
/*  81 */     return post(pre(reader), reader, super.portElements(port, reader));
/*     */   }
/*     */ 
/*     */   public boolean definitionsElements(XMLStreamReader reader) {
/*  85 */     return post(pre(reader), reader, super.definitionsElements(reader));
/*     */   }
/*     */ 
/*     */   public boolean bindingElements(WSDLBoundPortType binding, XMLStreamReader reader) {
/*  89 */     return post(pre(reader), reader, super.bindingElements(binding, reader));
/*     */   }
/*     */ 
/*     */   public boolean portTypeElements(WSDLPortType portType, XMLStreamReader reader) {
/*  93 */     return post(pre(reader), reader, super.portTypeElements(portType, reader));
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationElements(WSDLOperation operation, XMLStreamReader reader) {
/*  97 */     return post(pre(reader), reader, super.portTypeOperationElements(operation, reader));
/*     */   }
/*     */ 
/*     */   public boolean bindingOperationElements(WSDLBoundOperation operation, XMLStreamReader reader) {
/* 101 */     return post(pre(reader), reader, super.bindingOperationElements(operation, reader));
/*     */   }
/*     */ 
/*     */   public boolean messageElements(WSDLMessage msg, XMLStreamReader reader) {
/* 105 */     return post(pre(reader), reader, super.messageElements(msg, reader));
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationInputElements(WSDLInput input, XMLStreamReader reader) {
/* 109 */     return post(pre(reader), reader, super.portTypeOperationInputElements(input, reader));
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationOutputElements(WSDLOutput output, XMLStreamReader reader) {
/* 113 */     return post(pre(reader), reader, super.portTypeOperationOutputElements(output, reader));
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationFaultElements(WSDLFault fault, XMLStreamReader reader) {
/* 117 */     return post(pre(reader), reader, super.portTypeOperationFaultElements(fault, reader));
/*     */   }
/*     */ 
/*     */   public boolean bindingOperationInputElements(WSDLBoundOperation operation, XMLStreamReader reader) {
/* 121 */     return super.bindingOperationInputElements(operation, reader);
/*     */   }
/*     */ 
/*     */   public boolean bindingOperationOutputElements(WSDLBoundOperation operation, XMLStreamReader reader) {
/* 125 */     return post(pre(reader), reader, super.bindingOperationOutputElements(operation, reader));
/*     */   }
/*     */ 
/*     */   public boolean bindingOperationFaultElements(WSDLBoundFault fault, XMLStreamReader reader) {
/* 129 */     return post(pre(reader), reader, super.bindingOperationFaultElements(fault, reader));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.parser.FoolProofParserExtension
 * JD-Core Version:    0.6.2
 */