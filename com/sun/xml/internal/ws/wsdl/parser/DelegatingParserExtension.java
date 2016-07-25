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
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtensionContext;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ 
/*     */ class DelegatingParserExtension extends WSDLParserExtension
/*     */ {
/*     */   protected final WSDLParserExtension core;
/*     */ 
/*     */   public DelegatingParserExtension(WSDLParserExtension core)
/*     */   {
/*  44 */     this.core = core;
/*     */   }
/*     */ 
/*     */   public void start(WSDLParserExtensionContext context) {
/*  48 */     this.core.start(context);
/*     */   }
/*     */ 
/*     */   public void serviceAttributes(WSDLService service, XMLStreamReader reader) {
/*  52 */     this.core.serviceAttributes(service, reader);
/*     */   }
/*     */ 
/*     */   public boolean serviceElements(WSDLService service, XMLStreamReader reader) {
/*  56 */     return this.core.serviceElements(service, reader);
/*     */   }
/*     */ 
/*     */   public void portAttributes(WSDLPort port, XMLStreamReader reader) {
/*  60 */     this.core.portAttributes(port, reader);
/*     */   }
/*     */ 
/*     */   public boolean portElements(WSDLPort port, XMLStreamReader reader) {
/*  64 */     return this.core.portElements(port, reader);
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationInput(WSDLOperation op, XMLStreamReader reader) {
/*  68 */     return this.core.portTypeOperationInput(op, reader);
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationOutput(WSDLOperation op, XMLStreamReader reader) {
/*  72 */     return this.core.portTypeOperationOutput(op, reader);
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationFault(WSDLOperation op, XMLStreamReader reader) {
/*  76 */     return this.core.portTypeOperationFault(op, reader);
/*     */   }
/*     */ 
/*     */   public boolean definitionsElements(XMLStreamReader reader) {
/*  80 */     return this.core.definitionsElements(reader);
/*     */   }
/*     */ 
/*     */   public boolean bindingElements(WSDLBoundPortType binding, XMLStreamReader reader) {
/*  84 */     return this.core.bindingElements(binding, reader);
/*     */   }
/*     */ 
/*     */   public void bindingAttributes(WSDLBoundPortType binding, XMLStreamReader reader) {
/*  88 */     this.core.bindingAttributes(binding, reader);
/*     */   }
/*     */ 
/*     */   public boolean portTypeElements(WSDLPortType portType, XMLStreamReader reader) {
/*  92 */     return this.core.portTypeElements(portType, reader);
/*     */   }
/*     */ 
/*     */   public void portTypeAttributes(WSDLPortType portType, XMLStreamReader reader) {
/*  96 */     this.core.portTypeAttributes(portType, reader);
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationElements(WSDLOperation operation, XMLStreamReader reader) {
/* 100 */     return this.core.portTypeOperationElements(operation, reader);
/*     */   }
/*     */ 
/*     */   public void portTypeOperationAttributes(WSDLOperation operation, XMLStreamReader reader) {
/* 104 */     this.core.portTypeOperationAttributes(operation, reader);
/*     */   }
/*     */ 
/*     */   public boolean bindingOperationElements(WSDLBoundOperation operation, XMLStreamReader reader) {
/* 108 */     return this.core.bindingOperationElements(operation, reader);
/*     */   }
/*     */ 
/*     */   public void bindingOperationAttributes(WSDLBoundOperation operation, XMLStreamReader reader) {
/* 112 */     this.core.bindingOperationAttributes(operation, reader);
/*     */   }
/*     */ 
/*     */   public boolean messageElements(WSDLMessage msg, XMLStreamReader reader) {
/* 116 */     return this.core.messageElements(msg, reader);
/*     */   }
/*     */ 
/*     */   public void messageAttributes(WSDLMessage msg, XMLStreamReader reader) {
/* 120 */     this.core.messageAttributes(msg, reader);
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationInputElements(WSDLInput input, XMLStreamReader reader) {
/* 124 */     return this.core.portTypeOperationInputElements(input, reader);
/*     */   }
/*     */ 
/*     */   public void portTypeOperationInputAttributes(WSDLInput input, XMLStreamReader reader) {
/* 128 */     this.core.portTypeOperationInputAttributes(input, reader);
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationOutputElements(WSDLOutput output, XMLStreamReader reader) {
/* 132 */     return this.core.portTypeOperationOutputElements(output, reader);
/*     */   }
/*     */ 
/*     */   public void portTypeOperationOutputAttributes(WSDLOutput output, XMLStreamReader reader) {
/* 136 */     this.core.portTypeOperationOutputAttributes(output, reader);
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationFaultElements(WSDLFault fault, XMLStreamReader reader) {
/* 140 */     return this.core.portTypeOperationFaultElements(fault, reader);
/*     */   }
/*     */ 
/*     */   public void portTypeOperationFaultAttributes(WSDLFault fault, XMLStreamReader reader) {
/* 144 */     this.core.portTypeOperationFaultAttributes(fault, reader);
/*     */   }
/*     */ 
/*     */   public boolean bindingOperationInputElements(WSDLBoundOperation operation, XMLStreamReader reader) {
/* 148 */     return this.core.bindingOperationInputElements(operation, reader);
/*     */   }
/*     */ 
/*     */   public void bindingOperationInputAttributes(WSDLBoundOperation operation, XMLStreamReader reader) {
/* 152 */     this.core.bindingOperationInputAttributes(operation, reader);
/*     */   }
/*     */ 
/*     */   public boolean bindingOperationOutputElements(WSDLBoundOperation operation, XMLStreamReader reader) {
/* 156 */     return this.core.bindingOperationOutputElements(operation, reader);
/*     */   }
/*     */ 
/*     */   public void bindingOperationOutputAttributes(WSDLBoundOperation operation, XMLStreamReader reader) {
/* 160 */     this.core.bindingOperationOutputAttributes(operation, reader);
/*     */   }
/*     */ 
/*     */   public boolean bindingOperationFaultElements(WSDLBoundFault fault, XMLStreamReader reader) {
/* 164 */     return this.core.bindingOperationFaultElements(fault, reader);
/*     */   }
/*     */ 
/*     */   public void bindingOperationFaultAttributes(WSDLBoundFault fault, XMLStreamReader reader) {
/* 168 */     this.core.bindingOperationFaultAttributes(fault, reader);
/*     */   }
/*     */ 
/*     */   public void finished(WSDLParserExtensionContext context) {
/* 172 */     this.core.finished(context);
/*     */   }
/*     */ 
/*     */   public void postFinished(WSDLParserExtensionContext context) {
/* 176 */     this.core.postFinished(context);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.parser.DelegatingParserExtension
 * JD-Core Version:    0.6.2
 */