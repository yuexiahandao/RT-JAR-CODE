/*     */ package com.sun.xml.internal.ws.wsdl.writer;
/*     */ 
/*     */ import com.sun.istack.internal.NotNull;
/*     */ import com.sun.xml.internal.txw2.TypedXmlWriter;
/*     */ import com.sun.xml.internal.ws.api.model.CheckedException;
/*     */ import com.sun.xml.internal.ws.api.model.JavaMethod;
/*     */ import com.sun.xml.internal.ws.api.wsdl.writer.WSDLGenExtnContext;
/*     */ import com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension;
/*     */ 
/*     */ final class WSDLGeneratorExtensionFacade extends WSDLGeneratorExtension
/*     */ {
/*     */   private final WSDLGeneratorExtension[] extensions;
/*     */ 
/*     */   WSDLGeneratorExtensionFacade(WSDLGeneratorExtension[] extensions)
/*     */   {
/*  50 */     assert (extensions != null);
/*  51 */     this.extensions = extensions;
/*     */   }
/*     */ 
/*     */   public void start(WSDLGenExtnContext ctxt) {
/*  55 */     for (WSDLGeneratorExtension e : this.extensions)
/*  56 */       e.start(ctxt);
/*     */   }
/*     */ 
/*     */   public void end(@NotNull WSDLGenExtnContext ctxt) {
/*  60 */     for (WSDLGeneratorExtension e : this.extensions)
/*  61 */       e.end(ctxt);
/*     */   }
/*     */ 
/*     */   public void addDefinitionsExtension(TypedXmlWriter definitions) {
/*  65 */     for (WSDLGeneratorExtension e : this.extensions)
/*  66 */       e.addDefinitionsExtension(definitions);
/*     */   }
/*     */ 
/*     */   public void addServiceExtension(TypedXmlWriter service) {
/*  70 */     for (WSDLGeneratorExtension e : this.extensions)
/*  71 */       e.addServiceExtension(service);
/*     */   }
/*     */ 
/*     */   public void addPortExtension(TypedXmlWriter port) {
/*  75 */     for (WSDLGeneratorExtension e : this.extensions)
/*  76 */       e.addPortExtension(port);
/*     */   }
/*     */ 
/*     */   public void addPortTypeExtension(TypedXmlWriter portType) {
/*  80 */     for (WSDLGeneratorExtension e : this.extensions)
/*  81 */       e.addPortTypeExtension(portType);
/*     */   }
/*     */ 
/*     */   public void addBindingExtension(TypedXmlWriter binding) {
/*  85 */     for (WSDLGeneratorExtension e : this.extensions)
/*  86 */       e.addBindingExtension(binding);
/*     */   }
/*     */ 
/*     */   public void addOperationExtension(TypedXmlWriter operation, JavaMethod method) {
/*  90 */     for (WSDLGeneratorExtension e : this.extensions)
/*  91 */       e.addOperationExtension(operation, method);
/*     */   }
/*     */ 
/*     */   public void addBindingOperationExtension(TypedXmlWriter operation, JavaMethod method) {
/*  95 */     for (WSDLGeneratorExtension e : this.extensions)
/*  96 */       e.addBindingOperationExtension(operation, method);
/*     */   }
/*     */ 
/*     */   public void addInputMessageExtension(TypedXmlWriter message, JavaMethod method) {
/* 100 */     for (WSDLGeneratorExtension e : this.extensions)
/* 101 */       e.addInputMessageExtension(message, method);
/*     */   }
/*     */ 
/*     */   public void addOutputMessageExtension(TypedXmlWriter message, JavaMethod method) {
/* 105 */     for (WSDLGeneratorExtension e : this.extensions)
/* 106 */       e.addOutputMessageExtension(message, method);
/*     */   }
/*     */ 
/*     */   public void addOperationInputExtension(TypedXmlWriter input, JavaMethod method) {
/* 110 */     for (WSDLGeneratorExtension e : this.extensions)
/* 111 */       e.addOperationInputExtension(input, method);
/*     */   }
/*     */ 
/*     */   public void addOperationOutputExtension(TypedXmlWriter output, JavaMethod method) {
/* 115 */     for (WSDLGeneratorExtension e : this.extensions)
/* 116 */       e.addOperationOutputExtension(output, method);
/*     */   }
/*     */ 
/*     */   public void addBindingOperationInputExtension(TypedXmlWriter input, JavaMethod method) {
/* 120 */     for (WSDLGeneratorExtension e : this.extensions)
/* 121 */       e.addBindingOperationInputExtension(input, method);
/*     */   }
/*     */ 
/*     */   public void addBindingOperationOutputExtension(TypedXmlWriter output, JavaMethod method) {
/* 125 */     for (WSDLGeneratorExtension e : this.extensions)
/* 126 */       e.addBindingOperationOutputExtension(output, method);
/*     */   }
/*     */ 
/*     */   public void addBindingOperationFaultExtension(TypedXmlWriter fault, JavaMethod method, CheckedException ce) {
/* 130 */     for (WSDLGeneratorExtension e : this.extensions)
/* 131 */       e.addBindingOperationFaultExtension(fault, method, ce);
/*     */   }
/*     */ 
/*     */   public void addFaultMessageExtension(TypedXmlWriter message, JavaMethod method, CheckedException ce) {
/* 135 */     for (WSDLGeneratorExtension e : this.extensions)
/* 136 */       e.addFaultMessageExtension(message, method, ce);
/*     */   }
/*     */ 
/*     */   public void addOperationFaultExtension(TypedXmlWriter fault, JavaMethod method, CheckedException ce) {
/* 140 */     for (WSDLGeneratorExtension e : this.extensions)
/* 141 */       e.addOperationFaultExtension(fault, method, ce);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.writer.WSDLGeneratorExtensionFacade
 * JD-Core Version:    0.6.2
 */