/*     */ package com.sun.xml.internal.ws.wsdl.parser;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation.ANONYMOUS;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLFault;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLFeaturedObject;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLInput;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLModel;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLService;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtensionContext;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLBoundOperationImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLBoundPortTypeImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLFaultImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLInputImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLOperationImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLOutputImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLPortImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLPortTypeImpl;
/*     */ import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ import javax.xml.ws.soap.AddressingFeature;
/*     */ 
/*     */ public class W3CAddressingWSDLParserExtension extends WSDLParserExtension
/*     */ {
/*     */   protected static final String COLON_DELIMITER = ":";
/*     */   protected static final String SLASH_DELIMITER = "/";
/*     */ 
/*     */   public boolean bindingElements(WSDLBoundPortType binding, XMLStreamReader reader)
/*     */   {
/*  51 */     return addressibleElement(reader, binding);
/*     */   }
/*     */ 
/*     */   public boolean portElements(WSDLPort port, XMLStreamReader reader)
/*     */   {
/*  56 */     return addressibleElement(reader, port);
/*     */   }
/*     */ 
/*     */   private boolean addressibleElement(XMLStreamReader reader, WSDLFeaturedObject binding) {
/*  60 */     QName ua = reader.getName();
/*  61 */     if (ua.equals(AddressingVersion.W3C.wsdlExtensionTag)) {
/*  62 */       String required = reader.getAttributeValue("http://schemas.xmlsoap.org/wsdl/", "required");
/*  63 */       binding.addFeature(new AddressingFeature(true, Boolean.parseBoolean(required)));
/*  64 */       XMLStreamReaderUtil.skipElement(reader);
/*  65 */       return true;
/*     */     }
/*     */ 
/*  68 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean bindingOperationElements(WSDLBoundOperation operation, XMLStreamReader reader)
/*     */   {
/*  73 */     WSDLBoundOperationImpl impl = (WSDLBoundOperationImpl)operation;
/*     */ 
/*  75 */     QName anon = reader.getName();
/*  76 */     if (anon.equals(AddressingVersion.W3C.wsdlAnonymousTag)) {
/*     */       try {
/*  78 */         String value = reader.getElementText();
/*  79 */         if ((value == null) || (value.trim().equals(""))) {
/*  80 */           throw new WebServiceException("Null values not permitted in wsaw:Anonymous.");
/*     */         }
/*     */ 
/*  83 */         if (value.equals("optional"))
/*  84 */           impl.setAnonymous(WSDLBoundOperation.ANONYMOUS.optional);
/*  85 */         else if (value.equals("required"))
/*  86 */           impl.setAnonymous(WSDLBoundOperation.ANONYMOUS.required);
/*  87 */         else if (value.equals("prohibited"))
/*  88 */           impl.setAnonymous(WSDLBoundOperation.ANONYMOUS.prohibited);
/*     */         else {
/*  90 */           throw new WebServiceException("wsaw:Anonymous value \"" + value + "\" not understood.");
/*     */         }
/*     */       }
/*     */       catch (XMLStreamException e)
/*     */       {
/*  95 */         throw new WebServiceException(e);
/*     */       }
/*     */ 
/*  98 */       return true;
/*     */     }
/*     */ 
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */   public void portTypeOperationInputAttributes(WSDLInput input, XMLStreamReader reader) {
/* 105 */     String action = ParserUtil.getAttribute(reader, getWsdlActionTag());
/* 106 */     if (action != null) {
/* 107 */       ((WSDLInputImpl)input).setAction(action);
/* 108 */       ((WSDLInputImpl)input).setDefaultAction(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void portTypeOperationOutputAttributes(WSDLOutput output, XMLStreamReader reader)
/*     */   {
/* 114 */     String action = ParserUtil.getAttribute(reader, getWsdlActionTag());
/* 115 */     if (action != null) {
/* 116 */       ((WSDLOutputImpl)output).setAction(action);
/* 117 */       ((WSDLOutputImpl)output).setDefaultAction(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void portTypeOperationFaultAttributes(WSDLFault fault, XMLStreamReader reader)
/*     */   {
/* 123 */     String action = ParserUtil.getAttribute(reader, getWsdlActionTag());
/* 124 */     if (action != null) {
/* 125 */       ((WSDLFaultImpl)fault).setAction(action);
/* 126 */       ((WSDLFaultImpl)fault).setDefaultAction(false);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void finished(WSDLParserExtensionContext context)
/*     */   {
/* 142 */     WSDLModel model = context.getWSDLModel();
/* 143 */     for (WSDLService service : model.getServices().values())
/* 144 */       for (WSDLPort wp : service.getPorts()) {
/* 145 */         WSDLPortImpl port = (WSDLPortImpl)wp;
/* 146 */         WSDLBoundPortTypeImpl binding = port.getBinding();
/*     */ 
/* 149 */         populateActions(binding);
/*     */ 
/* 152 */         patchAnonymousDefault(binding);
/*     */       }
/*     */   }
/*     */ 
/*     */   protected String getNamespaceURI()
/*     */   {
/* 158 */     return AddressingVersion.W3C.wsdlNsUri;
/*     */   }
/*     */ 
/*     */   protected QName getWsdlActionTag() {
/* 162 */     return AddressingVersion.W3C.wsdlActionTag;
/*     */   }
/*     */ 
/*     */   private void populateActions(WSDLBoundPortTypeImpl binding)
/*     */   {
/* 170 */     WSDLPortTypeImpl porttype = binding.getPortType();
/* 171 */     for (Iterator i$ = porttype.getOperations().iterator(); i$.hasNext(); ) { o = (WSDLOperationImpl)i$.next();
/*     */ 
/* 174 */       WSDLBoundOperationImpl wboi = binding.get(o.getName());
/*     */ 
/* 176 */       if (wboi == null)
/*     */       {
/* 178 */         o.getInput().setAction(defaultInputAction(o));
/*     */       }
/*     */       else {
/* 181 */         String soapAction = wboi.getSOAPAction();
/* 182 */         if ((o.getInput().getAction() == null) || (o.getInput().getAction().equals("")))
/*     */         {
/* 185 */           if ((soapAction != null) && (!soapAction.equals("")))
/*     */           {
/* 187 */             o.getInput().setAction(soapAction);
/*     */           }
/*     */           else {
/* 190 */             o.getInput().setAction(defaultInputAction(o));
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 195 */         if (o.getOutput() != null)
/*     */         {
/* 198 */           if ((o.getOutput().getAction() == null) || (o.getOutput().getAction().equals(""))) {
/* 199 */             o.getOutput().setAction(defaultOutputAction(o));
/*     */           }
/*     */ 
/* 202 */           if ((o.getFaults() != null) && (o.getFaults().iterator().hasNext()))
/*     */           {
/* 205 */             for (WSDLFault f : o.getFaults())
/* 206 */               if ((f.getAction() == null) || (f.getAction().equals("")))
/* 207 */                 ((WSDLFaultImpl)f).setAction(defaultFaultAction(f.getName(), o));
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */     WSDLOperationImpl o;
/*     */   }
/*     */ 
/*     */   protected void patchAnonymousDefault(WSDLBoundPortTypeImpl binding)
/*     */   {
/* 220 */     for (WSDLBoundOperationImpl wbo : binding.getBindingOperations())
/* 221 */       if (wbo.getAnonymous() == null)
/* 222 */         wbo.setAnonymous(WSDLBoundOperation.ANONYMOUS.optional);
/*     */   }
/*     */ 
/*     */   private String defaultInputAction(WSDLOperation o)
/*     */   {
/* 227 */     return buildAction(o.getInput().getName(), o, false);
/*     */   }
/*     */ 
/*     */   private String defaultOutputAction(WSDLOperation o) {
/* 231 */     return buildAction(o.getOutput().getName(), o, false);
/*     */   }
/*     */ 
/*     */   private String defaultFaultAction(String name, WSDLOperation o) {
/* 235 */     return buildAction(name, o, true);
/*     */   }
/*     */ 
/*     */   protected static final String buildAction(String name, WSDLOperation o, boolean isFault) {
/* 239 */     String tns = o.getName().getNamespaceURI();
/*     */ 
/* 241 */     String delim = "/";
/*     */ 
/* 244 */     if (!tns.startsWith("http")) {
/* 245 */       delim = ":";
/*     */     }
/* 247 */     if (tns.endsWith(delim)) {
/* 248 */       tns = tns.substring(0, tns.length() - 1);
/*     */     }
/* 250 */     if (o.getPortTypeName() == null) {
/* 251 */       throw new WebServiceException("\"" + o.getName() + "\" operation's owning portType name is null.");
/*     */     }
/* 253 */     return tns + delim + o.getPortTypeName().getLocalPart() + delim + (isFault ? o.getName().getLocalPart() + delim + "Fault" + delim : "") + name;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.parser.W3CAddressingWSDLParserExtension
 * JD-Core Version:    0.6.2
 */