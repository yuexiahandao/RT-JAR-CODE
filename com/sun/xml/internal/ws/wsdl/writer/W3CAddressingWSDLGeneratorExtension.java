/*     */ package com.sun.xml.internal.ws.wsdl.writer;
/*     */ 
/*     */ import com.sun.xml.internal.txw2.TypedXmlWriter;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.model.CheckedException;
/*     */ import com.sun.xml.internal.ws.api.model.JavaMethod;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
/*     */ import com.sun.xml.internal.ws.api.model.soap.SOAPBinding;
/*     */ import com.sun.xml.internal.ws.api.wsdl.writer.WSDLGenExtnContext;
/*     */ import com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.util.logging.Logger;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.Action;
/*     */ import javax.xml.ws.FaultAction;
/*     */ import javax.xml.ws.soap.AddressingFeature;
/*     */ 
/*     */ public class W3CAddressingWSDLGeneratorExtension extends WSDLGeneratorExtension
/*     */ {
/*     */   private boolean enabled;
/*  49 */   private boolean required = false;
/*     */ 
/* 154 */   private static final Logger LOGGER = Logger.getLogger(W3CAddressingWSDLGeneratorExtension.class.getName());
/*     */ 
/*     */   public void start(WSDLGenExtnContext ctxt)
/*     */   {
/*  53 */     WSBinding binding = ctxt.getBinding();
/*  54 */     TypedXmlWriter root = ctxt.getRoot();
/*  55 */     this.enabled = binding.isFeatureEnabled(AddressingFeature.class);
/*  56 */     if (!this.enabled)
/*  57 */       return;
/*  58 */     AddressingFeature ftr = (AddressingFeature)binding.getFeature(AddressingFeature.class);
/*  59 */     this.required = ftr.isRequired();
/*  60 */     root._namespace(AddressingVersion.W3C.wsdlNsUri, AddressingVersion.W3C.getWsdlPrefix());
/*     */   }
/*     */ 
/*     */   public void addOperationInputExtension(TypedXmlWriter input, JavaMethod method)
/*     */   {
/*  65 */     if (!this.enabled) {
/*  66 */       return;
/*     */     }
/*  68 */     Action a = (Action)method.getSEIMethod().getAnnotation(Action.class);
/*  69 */     if ((a != null) && (!a.input().equals(""))) {
/*  70 */       addAttribute(input, a.input());
/*     */     }
/*  72 */     else if (method.getBinding().getSOAPAction().equals(""))
/*     */     {
/*  74 */       String defaultAction = getDefaultAction(method);
/*  75 */       addAttribute(input, defaultAction);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected static final String getDefaultAction(JavaMethod method)
/*     */   {
/*  81 */     String tns = method.getOwner().getTargetNamespace();
/*  82 */     String delim = "/";
/*     */     try
/*     */     {
/*  85 */       URI uri = new URI(tns);
/*  86 */       if (uri.getScheme().equalsIgnoreCase("urn"))
/*  87 */         delim = ":";
/*     */     } catch (URISyntaxException e) {
/*  89 */       LOGGER.warning("TargetNamespace of WebService is not a valid URI");
/*     */     }
/*  91 */     if (tns.endsWith(delim)) {
/*  92 */       tns = tns.substring(0, tns.length() - 1);
/*     */     }
/*     */ 
/*  96 */     String name = method.getOperationName() + "Request";
/*     */ 
/*  98 */     return tns + delim + method.getOwner().getPortTypeName().getLocalPart() + delim + name;
/*     */   }
/*     */ 
/*     */   public void addOperationOutputExtension(TypedXmlWriter output, JavaMethod method)
/*     */   {
/* 105 */     if (!this.enabled) {
/* 106 */       return;
/*     */     }
/* 108 */     Action a = (Action)method.getSEIMethod().getAnnotation(Action.class);
/* 109 */     if ((a != null) && (!a.output().equals("")))
/* 110 */       addAttribute(output, a.output());
/*     */   }
/*     */ 
/*     */   public void addOperationFaultExtension(TypedXmlWriter fault, JavaMethod method, CheckedException ce)
/*     */   {
/* 116 */     if (!this.enabled) {
/* 117 */       return;
/*     */     }
/* 119 */     Action a = (Action)method.getSEIMethod().getAnnotation(Action.class);
/* 120 */     Class[] exs = method.getSEIMethod().getExceptionTypes();
/*     */ 
/* 122 */     if (exs == null) {
/* 123 */       return;
/*     */     }
/* 125 */     if ((a != null) && (a.fault() != null))
/* 126 */       for (FaultAction fa : a.fault())
/* 127 */         if (fa.className().getName().equals(ce.getExceptionClass().getName())) {
/* 128 */           if (fa.value().equals("")) {
/* 129 */             return;
/*     */           }
/* 131 */           addAttribute(fault, fa.value());
/* 132 */           return;
/*     */         }
/*     */   }
/*     */ 
/*     */   private void addAttribute(TypedXmlWriter writer, String attrValue)
/*     */   {
/* 139 */     writer._attribute(AddressingVersion.W3C.wsdlActionTag, attrValue);
/*     */   }
/*     */ 
/*     */   public void addBindingExtension(TypedXmlWriter binding)
/*     */   {
/* 144 */     if (!this.enabled)
/* 145 */       return;
/* 146 */     UsingAddressing ua = (UsingAddressing)binding._element(AddressingVersion.W3C.wsdlExtensionTag, UsingAddressing.class);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.writer.W3CAddressingWSDLGeneratorExtension
 * JD-Core Version:    0.6.2
 */