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
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLBoundPortTypeImpl;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLPortImpl;
/*     */ import com.sun.xml.internal.ws.streaming.XMLStreamReaderUtil;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import org.xml.sax.Locator;
/*     */ import org.xml.sax.helpers.LocatorImpl;
/*     */ 
/*     */ final class WSDLParserExtensionFacade extends WSDLParserExtension
/*     */ {
/*     */   private final WSDLParserExtension[] extensions;
/*     */ 
/*     */   WSDLParserExtensionFacade(WSDLParserExtension[] extensions)
/*     */   {
/*  59 */     assert (extensions != null);
/*  60 */     this.extensions = extensions;
/*     */   }
/*     */ 
/*     */   public void start(WSDLParserExtensionContext context) {
/*  64 */     for (WSDLParserExtension e : this.extensions)
/*  65 */       e.start(context);
/*     */   }
/*     */ 
/*     */   public boolean serviceElements(WSDLService service, XMLStreamReader reader)
/*     */   {
/*  70 */     for (WSDLParserExtension e : this.extensions) {
/*  71 */       if (e.serviceElements(service, reader))
/*  72 */         return true;
/*     */     }
/*  74 */     XMLStreamReaderUtil.skipElement(reader);
/*  75 */     return true;
/*     */   }
/*     */ 
/*     */   public void serviceAttributes(WSDLService service, XMLStreamReader reader) {
/*  79 */     for (WSDLParserExtension e : this.extensions)
/*  80 */       e.serviceAttributes(service, reader);
/*     */   }
/*     */ 
/*     */   public boolean portElements(WSDLPort port, XMLStreamReader reader) {
/*  84 */     for (WSDLParserExtension e : this.extensions) {
/*  85 */       if (e.portElements(port, reader)) {
/*  86 */         return true;
/*     */       }
/*     */     }
/*     */ 
/*  90 */     if (isRequiredExtension(reader)) {
/*  91 */       ((WSDLPortImpl)port).addNotUnderstoodExtension(reader.getName(), getLocator(reader));
/*     */     }
/*  93 */     XMLStreamReaderUtil.skipElement(reader);
/*  94 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationInput(WSDLOperation op, XMLStreamReader reader) {
/*  98 */     for (WSDLParserExtension e : this.extensions) {
/*  99 */       e.portTypeOperationInput(op, reader);
/*     */     }
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationOutput(WSDLOperation op, XMLStreamReader reader) {
/* 105 */     for (WSDLParserExtension e : this.extensions) {
/* 106 */       e.portTypeOperationOutput(op, reader);
/*     */     }
/* 108 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationFault(WSDLOperation op, XMLStreamReader reader) {
/* 112 */     for (WSDLParserExtension e : this.extensions) {
/* 113 */       e.portTypeOperationFault(op, reader);
/*     */     }
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */   public void portAttributes(WSDLPort port, XMLStreamReader reader) {
/* 119 */     for (WSDLParserExtension e : this.extensions)
/* 120 */       e.portAttributes(port, reader);
/*     */   }
/*     */ 
/*     */   public boolean definitionsElements(XMLStreamReader reader) {
/* 124 */     for (WSDLParserExtension e : this.extensions) {
/* 125 */       if (e.definitionsElements(reader)) {
/* 126 */         return true;
/*     */       }
/*     */     }
/* 129 */     XMLStreamReaderUtil.skipElement(reader);
/* 130 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean bindingElements(WSDLBoundPortType binding, XMLStreamReader reader) {
/* 134 */     for (WSDLParserExtension e : this.extensions) {
/* 135 */       if (e.bindingElements(binding, reader)) {
/* 136 */         return true;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 141 */     if (isRequiredExtension(reader)) {
/* 142 */       ((WSDLBoundPortTypeImpl)binding).addNotUnderstoodExtension(reader.getName(), getLocator(reader));
/*     */     }
/*     */ 
/* 145 */     XMLStreamReaderUtil.skipElement(reader);
/* 146 */     return true;
/*     */   }
/*     */ 
/*     */   public void bindingAttributes(WSDLBoundPortType binding, XMLStreamReader reader) {
/* 150 */     for (WSDLParserExtension e : this.extensions)
/* 151 */       e.bindingAttributes(binding, reader);
/*     */   }
/*     */ 
/*     */   public boolean portTypeElements(WSDLPortType portType, XMLStreamReader reader)
/*     */   {
/* 156 */     for (WSDLParserExtension e : this.extensions) {
/* 157 */       if (e.portTypeElements(portType, reader)) {
/* 158 */         return true;
/*     */       }
/*     */     }
/* 161 */     XMLStreamReaderUtil.skipElement(reader);
/* 162 */     return true;
/*     */   }
/*     */ 
/*     */   public void portTypeAttributes(WSDLPortType portType, XMLStreamReader reader) {
/* 166 */     for (WSDLParserExtension e : this.extensions)
/* 167 */       e.portTypeAttributes(portType, reader);
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationElements(WSDLOperation operation, XMLStreamReader reader)
/*     */   {
/* 172 */     for (WSDLParserExtension e : this.extensions) {
/* 173 */       if (e.portTypeOperationElements(operation, reader)) {
/* 174 */         return true;
/*     */       }
/*     */     }
/* 177 */     XMLStreamReaderUtil.skipElement(reader);
/* 178 */     return true;
/*     */   }
/*     */ 
/*     */   public void portTypeOperationAttributes(WSDLOperation operation, XMLStreamReader reader) {
/* 182 */     for (WSDLParserExtension e : this.extensions)
/* 183 */       e.portTypeOperationAttributes(operation, reader);
/*     */   }
/*     */ 
/*     */   public boolean bindingOperationElements(WSDLBoundOperation operation, XMLStreamReader reader)
/*     */   {
/* 188 */     for (WSDLParserExtension e : this.extensions) {
/* 189 */       if (e.bindingOperationElements(operation, reader)) {
/* 190 */         return true;
/*     */       }
/*     */     }
/* 193 */     XMLStreamReaderUtil.skipElement(reader);
/* 194 */     return true;
/*     */   }
/*     */ 
/*     */   public void bindingOperationAttributes(WSDLBoundOperation operation, XMLStreamReader reader) {
/* 198 */     for (WSDLParserExtension e : this.extensions)
/* 199 */       e.bindingOperationAttributes(operation, reader);
/*     */   }
/*     */ 
/*     */   public boolean messageElements(WSDLMessage msg, XMLStreamReader reader)
/*     */   {
/* 204 */     for (WSDLParserExtension e : this.extensions) {
/* 205 */       if (e.messageElements(msg, reader)) {
/* 206 */         return true;
/*     */       }
/*     */     }
/* 209 */     XMLStreamReaderUtil.skipElement(reader);
/* 210 */     return true;
/*     */   }
/*     */ 
/*     */   public void messageAttributes(WSDLMessage msg, XMLStreamReader reader) {
/* 214 */     for (WSDLParserExtension e : this.extensions)
/* 215 */       e.messageAttributes(msg, reader);
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationInputElements(WSDLInput input, XMLStreamReader reader)
/*     */   {
/* 220 */     for (WSDLParserExtension e : this.extensions) {
/* 221 */       if (e.portTypeOperationInputElements(input, reader)) {
/* 222 */         return true;
/*     */       }
/*     */     }
/* 225 */     XMLStreamReaderUtil.skipElement(reader);
/* 226 */     return true;
/*     */   }
/*     */ 
/*     */   public void portTypeOperationInputAttributes(WSDLInput input, XMLStreamReader reader) {
/* 230 */     for (WSDLParserExtension e : this.extensions)
/* 231 */       e.portTypeOperationInputAttributes(input, reader);
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationOutputElements(WSDLOutput output, XMLStreamReader reader)
/*     */   {
/* 236 */     for (WSDLParserExtension e : this.extensions) {
/* 237 */       if (e.portTypeOperationOutputElements(output, reader)) {
/* 238 */         return true;
/*     */       }
/*     */     }
/* 241 */     XMLStreamReaderUtil.skipElement(reader);
/* 242 */     return true;
/*     */   }
/*     */ 
/*     */   public void portTypeOperationOutputAttributes(WSDLOutput output, XMLStreamReader reader) {
/* 246 */     for (WSDLParserExtension e : this.extensions)
/* 247 */       e.portTypeOperationOutputAttributes(output, reader);
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationFaultElements(WSDLFault fault, XMLStreamReader reader)
/*     */   {
/* 252 */     for (WSDLParserExtension e : this.extensions) {
/* 253 */       if (e.portTypeOperationFaultElements(fault, reader)) {
/* 254 */         return true;
/*     */       }
/*     */     }
/* 257 */     XMLStreamReaderUtil.skipElement(reader);
/* 258 */     return true;
/*     */   }
/*     */ 
/*     */   public void portTypeOperationFaultAttributes(WSDLFault fault, XMLStreamReader reader) {
/* 262 */     for (WSDLParserExtension e : this.extensions)
/* 263 */       e.portTypeOperationFaultAttributes(fault, reader);
/*     */   }
/*     */ 
/*     */   public boolean bindingOperationInputElements(WSDLBoundOperation operation, XMLStreamReader reader)
/*     */   {
/* 268 */     for (WSDLParserExtension e : this.extensions) {
/* 269 */       if (e.bindingOperationInputElements(operation, reader)) {
/* 270 */         return true;
/*     */       }
/*     */     }
/* 273 */     XMLStreamReaderUtil.skipElement(reader);
/* 274 */     return true;
/*     */   }
/*     */ 
/*     */   public void bindingOperationInputAttributes(WSDLBoundOperation operation, XMLStreamReader reader) {
/* 278 */     for (WSDLParserExtension e : this.extensions)
/* 279 */       e.bindingOperationInputAttributes(operation, reader);
/*     */   }
/*     */ 
/*     */   public boolean bindingOperationOutputElements(WSDLBoundOperation operation, XMLStreamReader reader)
/*     */   {
/* 284 */     for (WSDLParserExtension e : this.extensions) {
/* 285 */       if (e.bindingOperationOutputElements(operation, reader)) {
/* 286 */         return true;
/*     */       }
/*     */     }
/* 289 */     XMLStreamReaderUtil.skipElement(reader);
/* 290 */     return true;
/*     */   }
/*     */ 
/*     */   public void bindingOperationOutputAttributes(WSDLBoundOperation operation, XMLStreamReader reader) {
/* 294 */     for (WSDLParserExtension e : this.extensions)
/* 295 */       e.bindingOperationOutputAttributes(operation, reader);
/*     */   }
/*     */ 
/*     */   public boolean bindingOperationFaultElements(WSDLBoundFault fault, XMLStreamReader reader)
/*     */   {
/* 300 */     for (WSDLParserExtension e : this.extensions) {
/* 301 */       if (e.bindingOperationFaultElements(fault, reader)) {
/* 302 */         return true;
/*     */       }
/*     */     }
/* 305 */     XMLStreamReaderUtil.skipElement(reader);
/* 306 */     return true;
/*     */   }
/*     */ 
/*     */   public void bindingOperationFaultAttributes(WSDLBoundFault fault, XMLStreamReader reader) {
/* 310 */     for (WSDLParserExtension e : this.extensions)
/* 311 */       e.bindingOperationFaultAttributes(fault, reader);
/*     */   }
/*     */ 
/*     */   public void finished(WSDLParserExtensionContext context)
/*     */   {
/* 316 */     for (WSDLParserExtension e : this.extensions)
/* 317 */       e.finished(context);
/*     */   }
/*     */ 
/*     */   public void postFinished(WSDLParserExtensionContext context)
/*     */   {
/* 322 */     for (WSDLParserExtension e : this.extensions)
/* 323 */       e.postFinished(context);
/*     */   }
/*     */ 
/*     */   private boolean isRequiredExtension(XMLStreamReader reader)
/*     */   {
/* 333 */     String required = reader.getAttributeValue("http://schemas.xmlsoap.org/wsdl/", "required");
/* 334 */     if (required != null)
/* 335 */       return Boolean.parseBoolean(required);
/* 336 */     return false;
/*     */   }
/*     */ 
/*     */   private Locator getLocator(XMLStreamReader reader) {
/* 340 */     Location location = reader.getLocation();
/* 341 */     LocatorImpl loc = new LocatorImpl();
/* 342 */     loc.setSystemId(location.getSystemId());
/* 343 */     loc.setLineNumber(location.getLineNumber());
/* 344 */     return loc;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.parser.WSDLParserExtensionFacade
 * JD-Core Version:    0.6.2
 */