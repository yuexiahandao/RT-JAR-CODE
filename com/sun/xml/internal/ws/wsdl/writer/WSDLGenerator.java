/*      */ package com.sun.xml.internal.ws.wsdl.writer;
/*      */ 
/*      */ import com.sun.xml.internal.bind.api.Bridge;
/*      */ import com.sun.xml.internal.bind.api.JAXBRIContext;
/*      */ import com.sun.xml.internal.bind.api.TypeReference;
/*      */ import com.sun.xml.internal.bind.v2.schemagen.Util;
/*      */ import com.sun.xml.internal.txw2.TXW;
/*      */ import com.sun.xml.internal.txw2.TypedXmlWriter;
/*      */ import com.sun.xml.internal.txw2.output.ResultFactory;
/*      */ import com.sun.xml.internal.txw2.output.TXWResult;
/*      */ import com.sun.xml.internal.txw2.output.XmlSerializer;
/*      */ import com.sun.xml.internal.ws.api.BindingID;
/*      */ import com.sun.xml.internal.ws.api.SOAPVersion;
/*      */ import com.sun.xml.internal.ws.api.WSBinding;
/*      */ import com.sun.xml.internal.ws.api.model.MEP;
/*      */ import com.sun.xml.internal.ws.api.model.ParameterBinding;
/*      */ import com.sun.xml.internal.ws.api.server.Container;
/*      */ import com.sun.xml.internal.ws.api.wsdl.writer.WSDLGenExtnContext;
/*      */ import com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension;
/*      */ import com.sun.xml.internal.ws.model.AbstractSEIModelImpl;
/*      */ import com.sun.xml.internal.ws.model.CheckedExceptionImpl;
/*      */ import com.sun.xml.internal.ws.model.JavaMethodImpl;
/*      */ import com.sun.xml.internal.ws.model.ParameterImpl;
/*      */ import com.sun.xml.internal.ws.model.WrapperParameter;
/*      */ import com.sun.xml.internal.ws.model.soap.SOAPBindingImpl;
/*      */ import com.sun.xml.internal.ws.policy.jaxws.PolicyWSDLGeneratorExtension;
/*      */ import com.sun.xml.internal.ws.util.RuntimeVersion;
/*      */ import com.sun.xml.internal.ws.wsdl.writer.document.Binding;
/*      */ import com.sun.xml.internal.ws.wsdl.writer.document.BindingOperationType;
/*      */ import com.sun.xml.internal.ws.wsdl.writer.document.Definitions;
/*      */ import com.sun.xml.internal.ws.wsdl.writer.document.Fault;
/*      */ import com.sun.xml.internal.ws.wsdl.writer.document.FaultType;
/*      */ import com.sun.xml.internal.ws.wsdl.writer.document.Message;
/*      */ import com.sun.xml.internal.ws.wsdl.writer.document.Operation;
/*      */ import com.sun.xml.internal.ws.wsdl.writer.document.ParamType;
/*      */ import com.sun.xml.internal.ws.wsdl.writer.document.Part;
/*      */ import com.sun.xml.internal.ws.wsdl.writer.document.Port;
/*      */ import com.sun.xml.internal.ws.wsdl.writer.document.PortType;
/*      */ import com.sun.xml.internal.ws.wsdl.writer.document.Service;
/*      */ import com.sun.xml.internal.ws.wsdl.writer.document.Types;
/*      */ import com.sun.xml.internal.ws.wsdl.writer.document.xsd.Schema;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import javax.jws.soap.SOAPBinding.Style;
/*      */ import javax.jws.soap.SOAPBinding.Use;
/*      */ import javax.xml.bind.SchemaOutputResolver;
/*      */ import javax.xml.namespace.QName;
/*      */ import javax.xml.transform.Result;
/*      */ import javax.xml.ws.Holder;
/*      */ import javax.xml.ws.WebServiceException;
/*      */ 
/*      */ public class WSDLGenerator
/*      */ {
/*      */   private JAXWSOutputSchemaResolver resolver;
/*   99 */   private WSDLResolver wsdlResolver = null;
/*      */   private AbstractSEIModelImpl model;
/*      */   private Definitions serviceDefinitions;
/*      */   private Definitions portDefinitions;
/*      */   private Types types;
/*      */   private static final String DOT_WSDL = ".wsdl";
/*      */   private static final String RESPONSE = "Response";
/*      */   private static final String PARAMETERS = "parameters";
/*      */   private static final String RESULT = "parameters";
/*      */   private static final String UNWRAPPABLE_RESULT = "result";
/*      */   private static final String WSDL_NAMESPACE = "http://schemas.xmlsoap.org/wsdl/";
/*      */   private static final String XSD_NAMESPACE = "http://www.w3.org/2001/XMLSchema";
/*      */   private static final String XSD_PREFIX = "xsd";
/*      */   private static final String SOAP11_NAMESPACE = "http://schemas.xmlsoap.org/wsdl/soap/";
/*      */   private static final String SOAP12_NAMESPACE = "http://schemas.xmlsoap.org/wsdl/soap12/";
/*      */   private static final String SOAP_PREFIX = "soap";
/*      */   private static final String SOAP12_PREFIX = "soap12";
/*      */   private static final String TNS_PREFIX = "tns";
/*      */   private static final String DOCUMENT = "document";
/*      */   private static final String RPC = "rpc";
/*      */   private static final String LITERAL = "literal";
/*      */   private static final String REPLACE_WITH_ACTUAL_URL = "REPLACE_WITH_ACTUAL_URL";
/*  176 */   private Set<QName> processedExceptions = new HashSet();
/*      */   private WSBinding binding;
/*      */   private String wsdlLocation;
/*      */   private String portWSDLID;
/*      */   private String schemaPrefix;
/*      */   private WSDLGeneratorExtension extension;
/*      */   List<WSDLGeneratorExtension> extensionHandlers;
/*  184 */   private String endpointAddress = "REPLACE_WITH_ACTUAL_URL";
/*      */   private Container container;
/*      */   private final Class implType;
/*      */   private boolean inlineSchemas;
/*      */ 
/*      */   public WSDLGenerator(AbstractSEIModelImpl model, WSDLResolver wsdlResolver, WSBinding binding, Container container, Class implType, boolean inlineSchemas, WSDLGeneratorExtension[] extensions)
/*      */   {
/*  200 */     this.model = model;
/*  201 */     this.resolver = new JAXWSOutputSchemaResolver();
/*  202 */     this.wsdlResolver = wsdlResolver;
/*  203 */     this.binding = binding;
/*  204 */     this.container = container;
/*  205 */     this.implType = implType;
/*  206 */     this.extensionHandlers = new ArrayList();
/*  207 */     this.inlineSchemas = inlineSchemas;
/*      */ 
/*  210 */     register(new W3CAddressingWSDLGeneratorExtension());
/*  211 */     register(new W3CAddressingMetadataWSDLGeneratorExtension());
/*  212 */     register(new PolicyWSDLGeneratorExtension());
/*  213 */     for (WSDLGeneratorExtension w : extensions) {
/*  214 */       register(w);
/*      */     }
/*  216 */     this.extension = new WSDLGeneratorExtensionFacade((WSDLGeneratorExtension[])this.extensionHandlers.toArray(new WSDLGeneratorExtension[0]));
/*      */   }
/*      */ 
/*      */   public void setEndpointAddress(String address)
/*      */   {
/*  226 */     this.endpointAddress = address;
/*      */   }
/*      */ 
/*      */   public void doGeneration()
/*      */   {
/*  234 */     XmlSerializer portWriter = null;
/*  235 */     String fileName = JAXBRIContext.mangleNameToClassName(this.model.getServiceQName().getLocalPart());
/*  236 */     Result result = this.wsdlResolver.getWSDL(fileName + ".wsdl");
/*  237 */     this.wsdlLocation = result.getSystemId();
/*  238 */     XmlSerializer serviceWriter = new CommentFilter(ResultFactory.createSerializer(result));
/*  239 */     if (this.model.getServiceQName().getNamespaceURI().equals(this.model.getTargetNamespace())) {
/*  240 */       portWriter = serviceWriter;
/*  241 */       this.schemaPrefix = (fileName + "_");
/*      */     } else {
/*  243 */       String wsdlName = JAXBRIContext.mangleNameToClassName(this.model.getPortTypeName().getLocalPart());
/*  244 */       if (wsdlName.equals(fileName))
/*  245 */         wsdlName = wsdlName + "PortType";
/*  246 */       Holder absWSDLName = new Holder();
/*  247 */       absWSDLName.value = (wsdlName + ".wsdl");
/*  248 */       result = this.wsdlResolver.getAbstractWSDL(absWSDLName);
/*      */ 
/*  250 */       if (result != null) {
/*  251 */         this.portWSDLID = result.getSystemId();
/*  252 */         if (this.portWSDLID.equals(this.wsdlLocation))
/*  253 */           portWriter = serviceWriter;
/*      */         else
/*  255 */           portWriter = new CommentFilter(ResultFactory.createSerializer(result));
/*      */       }
/*      */       else {
/*  258 */         this.portWSDLID = ((String)absWSDLName.value);
/*      */       }
/*  260 */       this.schemaPrefix = new File(this.portWSDLID).getName();
/*  261 */       int idx = this.schemaPrefix.lastIndexOf('.');
/*  262 */       if (idx > 0)
/*  263 */         this.schemaPrefix = this.schemaPrefix.substring(0, idx);
/*  264 */       this.schemaPrefix = (JAXBRIContext.mangleNameToClassName(this.schemaPrefix) + "_");
/*      */     }
/*  266 */     generateDocument(serviceWriter, portWriter);
/*      */   }
/*      */ 
/*      */   private void generateDocument(XmlSerializer serviceStream, XmlSerializer portStream)
/*      */   {
/*  332 */     this.serviceDefinitions = ((Definitions)TXW.create(Definitions.class, serviceStream));
/*  333 */     this.serviceDefinitions._namespace("http://schemas.xmlsoap.org/wsdl/", "");
/*  334 */     this.serviceDefinitions._namespace("http://www.w3.org/2001/XMLSchema", "xsd");
/*  335 */     this.serviceDefinitions.targetNamespace(this.model.getServiceQName().getNamespaceURI());
/*  336 */     this.serviceDefinitions._namespace(this.model.getServiceQName().getNamespaceURI(), "tns");
/*  337 */     if (this.binding.getSOAPVersion() == SOAPVersion.SOAP_12)
/*  338 */       this.serviceDefinitions._namespace("http://schemas.xmlsoap.org/wsdl/soap12/", "soap12");
/*      */     else
/*  340 */       this.serviceDefinitions._namespace("http://schemas.xmlsoap.org/wsdl/soap/", "soap");
/*  341 */     this.serviceDefinitions.name(this.model.getServiceQName().getLocalPart());
/*  342 */     WSDLGenExtnContext serviceCtx = new WSDLGenExtnContext(this.serviceDefinitions, this.model, this.binding, this.container, this.implType);
/*  343 */     this.extension.start(serviceCtx);
/*  344 */     if ((serviceStream != portStream) && (portStream != null))
/*      */     {
/*  346 */       this.portDefinitions = ((Definitions)TXW.create(Definitions.class, portStream));
/*  347 */       this.portDefinitions._namespace("http://schemas.xmlsoap.org/wsdl/", "");
/*  348 */       this.portDefinitions._namespace("http://www.w3.org/2001/XMLSchema", "xsd");
/*  349 */       if (this.model.getTargetNamespace() != null) {
/*  350 */         this.portDefinitions.targetNamespace(this.model.getTargetNamespace());
/*  351 */         this.portDefinitions._namespace(this.model.getTargetNamespace(), "tns");
/*      */       }
/*      */ 
/*  354 */       String schemaLoc = relativize(this.portWSDLID, this.wsdlLocation);
/*  355 */       com.sun.xml.internal.ws.wsdl.writer.document.Import _import = this.serviceDefinitions._import().namespace(this.model.getTargetNamespace());
/*  356 */       _import.location(schemaLoc);
/*  357 */     } else if (portStream != null)
/*      */     {
/*  359 */       this.portDefinitions = this.serviceDefinitions;
/*      */     }
/*      */     else {
/*  362 */       String schemaLoc = relativize(this.portWSDLID, this.wsdlLocation);
/*  363 */       com.sun.xml.internal.ws.wsdl.writer.document.Import _import = this.serviceDefinitions._import().namespace(this.model.getTargetNamespace());
/*  364 */       _import.location(schemaLoc);
/*      */     }
/*  366 */     this.extension.addDefinitionsExtension(this.serviceDefinitions);
/*      */ 
/*  368 */     if (this.portDefinitions != null) {
/*  369 */       generateTypes();
/*  370 */       generateMessages();
/*  371 */       generatePortType();
/*      */     }
/*  373 */     generateBinding();
/*  374 */     generateService();
/*      */ 
/*  376 */     this.extension.end(serviceCtx);
/*  377 */     this.serviceDefinitions.commit();
/*  378 */     if ((this.portDefinitions != null) && (this.portDefinitions != this.serviceDefinitions))
/*  379 */       this.portDefinitions.commit();
/*      */   }
/*      */ 
/*      */   protected void generateTypes()
/*      */   {
/*  387 */     this.types = this.portDefinitions.types();
/*  388 */     if (this.model.getJAXBContext() != null)
/*      */       try {
/*  390 */         this.model.getJAXBContext().generateSchema(this.resolver);
/*      */       }
/*      */       catch (IOException e) {
/*  393 */         e.printStackTrace();
/*  394 */         throw new WebServiceException(e.getMessage());
/*      */       }
/*      */   }
/*      */ 
/*      */   protected void generateMessages()
/*      */   {
/*  403 */     for (JavaMethodImpl method : this.model.getJavaMethods())
/*  404 */       generateSOAPMessages(method, method.getBinding());
/*      */   }
/*      */ 
/*      */   protected void generateSOAPMessages(JavaMethodImpl method, com.sun.xml.internal.ws.api.model.soap.SOAPBinding binding)
/*      */   {
/*  414 */     boolean isDoclit = binding.isDocLit();
/*      */ 
/*  416 */     Message message = this.portDefinitions.message().name(method.getRequestMessageName());
/*  417 */     this.extension.addInputMessageExtension(message, method);
/*      */ 
/*  419 */     JAXBRIContext jaxbContext = this.model.getJAXBContext();
/*  420 */     boolean unwrappable = true;
/*  421 */     for (ParameterImpl param : method.getRequestParameters()) {
/*  422 */       if (isDoclit) {
/*  423 */         if (isHeaderParameter(param)) {
/*  424 */           unwrappable = false;
/*      */         }
/*  426 */         Part part = message.part().name(param.getPartName());
/*  427 */         part.element(param.getName());
/*      */       }
/*  429 */       else if (param.isWrapperStyle()) {
/*  430 */         for (ParameterImpl childParam : ((WrapperParameter)param).getWrapperChildren()) {
/*  431 */           Part part = message.part().name(childParam.getPartName());
/*  432 */           part.type(jaxbContext.getTypeName(childParam.getBridge().getTypeReference()));
/*      */         }
/*      */       } else {
/*  435 */         Part part = message.part().name(param.getPartName());
/*  436 */         part.element(param.getName());
/*      */       }
/*      */     }
/*      */ 
/*  440 */     if (method.getMEP() != MEP.ONE_WAY)
/*      */     {
/*  442 */       message = this.portDefinitions.message().name(method.getResponseMessageName());
/*  443 */       this.extension.addOutputMessageExtension(message, method);
/*  444 */       if (unwrappable) {
/*  445 */         for (ParameterImpl param : method.getResponseParameters()) {
/*  446 */           if (isHeaderParameter(param)) {
/*  447 */             unwrappable = false;
/*      */           }
/*      */         }
/*      */       }
/*  451 */       for (ParameterImpl param : method.getResponseParameters()) {
/*  452 */         if (isDoclit) {
/*  453 */           Part part = message.part().name(param.getPartName());
/*  454 */           part.element(param.getName());
/*      */         }
/*  457 */         else if (param.isWrapperStyle()) {
/*  458 */           for (ParameterImpl childParam : ((WrapperParameter)param).getWrapperChildren()) {
/*  459 */             Part part = message.part().name(childParam.getPartName());
/*  460 */             part.type(jaxbContext.getTypeName(childParam.getBridge().getTypeReference()));
/*      */           }
/*      */         } else {
/*  463 */           Part part = message.part().name(param.getPartName());
/*  464 */           part.element(param.getName());
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  469 */     for (CheckedExceptionImpl exception : method.getCheckedExceptions()) {
/*  470 */       QName tagName = exception.getDetailType().tagName;
/*  471 */       String messageName = exception.getMessageName();
/*  472 */       QName messageQName = new QName(this.model.getTargetNamespace(), messageName);
/*  473 */       if (!this.processedExceptions.contains(messageQName))
/*      */       {
/*  475 */         message = this.portDefinitions.message().name(messageName);
/*      */ 
/*  477 */         this.extension.addFaultMessageExtension(message, method, exception);
/*  478 */         Part part = message.part().name("fault");
/*  479 */         part.element(tagName);
/*  480 */         this.processedExceptions.add(messageQName);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void generatePortType()
/*      */   {
/*  489 */     PortType portType = this.portDefinitions.portType().name(this.model.getPortTypeName().getLocalPart());
/*  490 */     this.extension.addPortTypeExtension(portType);
/*  491 */     for (Iterator i$ = this.model.getJavaMethods().iterator(); i$.hasNext(); ) { method = (JavaMethodImpl)i$.next();
/*  492 */       operation = portType.operation().name(method.getOperationName());
/*  493 */       generateParameterOrder(operation, method);
/*  494 */       this.extension.addOperationExtension(operation, method);
/*  495 */       switch (1.$SwitchMap$com$sun$xml$internal$ws$api$model$MEP[method.getMEP().ordinal()])
/*      */       {
/*      */       case 1:
/*  498 */         generateInputMessage(operation, method);
/*      */ 
/*  500 */         generateOutputMessage(operation, method);
/*  501 */         break;
/*      */       case 2:
/*  503 */         generateInputMessage(operation, method);
/*      */       }
/*      */ 
/*  507 */       for (CheckedExceptionImpl exception : method.getCheckedExceptions()) {
/*  508 */         QName messageName = new QName(this.model.getTargetNamespace(), exception.getMessageName());
/*  509 */         FaultType paramType = operation.fault().message(messageName).name(exception.getMessageName());
/*  510 */         this.extension.addOperationFaultExtension(paramType, method, exception);
/*      */       }
/*      */     }
/*      */     JavaMethodImpl method;
/*      */     Operation operation;
/*      */   }
/*      */ 
/*      */   protected boolean isWrapperStyle(JavaMethodImpl method)
/*      */   {
/*  521 */     if (method.getRequestParameters().size() > 0) {
/*  522 */       ParameterImpl param = (ParameterImpl)method.getRequestParameters().iterator().next();
/*  523 */       return param.isWrapperStyle();
/*      */     }
/*  525 */     return false;
/*      */   }
/*      */ 
/*      */   protected boolean isRpcLit(JavaMethodImpl method)
/*      */   {
/*  534 */     return method.getBinding().getStyle() == SOAPBinding.Style.RPC;
/*      */   }
/*      */ 
/*      */   protected void generateParameterOrder(Operation operation, JavaMethodImpl method)
/*      */   {
/*  543 */     if (method.getMEP() == MEP.ONE_WAY)
/*  544 */       return;
/*  545 */     if (isRpcLit(method))
/*  546 */       generateRpcParameterOrder(operation, method);
/*      */     else
/*  548 */       generateDocumentParameterOrder(operation, method);
/*      */   }
/*      */ 
/*      */   protected void generateRpcParameterOrder(Operation operation, JavaMethodImpl method)
/*      */   {
/*  558 */     StringBuffer paramOrder = new StringBuffer();
/*  559 */     Set partNames = new HashSet();
/*  560 */     List sortedParams = sortMethodParameters(method);
/*  561 */     int i = 0;
/*  562 */     for (ParameterImpl parameter : sortedParams) {
/*  563 */       if (parameter.getIndex() >= 0) {
/*  564 */         String partName = parameter.getPartName();
/*  565 */         if (!partNames.contains(partName)) {
/*  566 */           if (i++ > 0)
/*  567 */             paramOrder.append(' ');
/*  568 */           paramOrder.append(partName);
/*  569 */           partNames.add(partName);
/*      */         }
/*      */       }
/*      */     }
/*  573 */     if (i > 1)
/*  574 */       operation.parameterOrder(paramOrder.toString());
/*      */   }
/*      */ 
/*      */   protected void generateDocumentParameterOrder(Operation operation, JavaMethodImpl method)
/*      */   {
/*  586 */     StringBuffer paramOrder = new StringBuffer();
/*  587 */     Set partNames = new HashSet();
/*  588 */     List sortedParams = sortMethodParameters(method);
/*  589 */     boolean isWrapperStyle = isWrapperStyle(method);
/*  590 */     int i = 0;
/*  591 */     for (ParameterImpl parameter : sortedParams)
/*      */     {
/*  593 */       if (parameter.getIndex() >= 0)
/*      */       {
/*  598 */         String partName = parameter.getPartName();
/*      */ 
/*  614 */         if (!partNames.contains(partName)) {
/*  615 */           if (i++ > 0)
/*  616 */             paramOrder.append(' ');
/*  617 */           paramOrder.append(partName);
/*  618 */           partNames.add(partName);
/*      */         }
/*      */       }
/*      */     }
/*  621 */     if (i > 1)
/*  622 */       operation.parameterOrder(paramOrder.toString());
/*      */   }
/*      */ 
/*      */   protected List<ParameterImpl> sortMethodParameters(JavaMethodImpl method)
/*      */   {
/*  632 */     Set paramSet = new HashSet();
/*  633 */     List sortedParams = new ArrayList();
/*  634 */     if (isRpcLit(method)) {
/*  635 */       for (ParameterImpl param : method.getRequestParameters()) {
/*  636 */         if ((param instanceof WrapperParameter))
/*  637 */           paramSet.addAll(((WrapperParameter)param).getWrapperChildren());
/*      */         else {
/*  639 */           paramSet.add(param);
/*      */         }
/*      */       }
/*  642 */       for (ParameterImpl param : method.getResponseParameters())
/*  643 */         if ((param instanceof WrapperParameter))
/*  644 */           paramSet.addAll(((WrapperParameter)param).getWrapperChildren());
/*      */         else
/*  646 */           paramSet.add(param);
/*      */     }
/*      */     else
/*      */     {
/*  650 */       paramSet.addAll(method.getRequestParameters());
/*  651 */       paramSet.addAll(method.getResponseParameters());
/*      */     }
/*  653 */     Iterator params = paramSet.iterator();
/*  654 */     if (paramSet.size() == 0)
/*  655 */       return sortedParams;
/*  656 */     ParameterImpl param = (ParameterImpl)params.next();
/*  657 */     sortedParams.add(param);
/*      */ 
/*  660 */     for (int i = 1; i < paramSet.size(); i++) {
/*  661 */       param = (ParameterImpl)params.next();
/*  662 */       for (int pos = 0; pos < i; pos++) {
/*  663 */         ParameterImpl sortedParam = (ParameterImpl)sortedParams.get(pos);
/*  664 */         if ((param.getIndex() == sortedParam.getIndex()) && ((param instanceof WrapperParameter))) {
/*      */           break;
/*      */         }
/*  667 */         if (param.getIndex() < sortedParam.getIndex()) {
/*      */           break;
/*      */         }
/*      */       }
/*  671 */       sortedParams.add(pos, param);
/*      */     }
/*  673 */     return sortedParams;
/*      */   }
/*      */ 
/*      */   protected boolean isBodyParameter(ParameterImpl parameter)
/*      */   {
/*  682 */     ParameterBinding paramBinding = parameter.getBinding();
/*  683 */     return paramBinding.isBody();
/*      */   }
/*      */ 
/*      */   protected boolean isHeaderParameter(ParameterImpl parameter) {
/*  687 */     ParameterBinding paramBinding = parameter.getBinding();
/*  688 */     return paramBinding.isHeader();
/*      */   }
/*      */ 
/*      */   protected boolean isAttachmentParameter(ParameterImpl parameter) {
/*  692 */     ParameterBinding paramBinding = parameter.getBinding();
/*  693 */     return paramBinding.isAttachment();
/*      */   }
/*      */ 
/*      */   protected void generateBinding()
/*      */   {
/*  701 */     Binding binding = this.serviceDefinitions.binding().name(this.model.getBoundPortTypeName().getLocalPart());
/*  702 */     this.extension.addBindingExtension(binding);
/*  703 */     binding.type(this.model.getPortTypeName());
/*  704 */     boolean first = true;
/*  705 */     for (JavaMethodImpl method : this.model.getJavaMethods()) {
/*  706 */       if (first) {
/*  707 */         com.sun.xml.internal.ws.api.model.soap.SOAPBinding sBinding = method.getBinding();
/*  708 */         SOAPVersion soapVersion = sBinding.getSOAPVersion();
/*  709 */         if (soapVersion == SOAPVersion.SOAP_12) {
/*  710 */           com.sun.xml.internal.ws.wsdl.writer.document.soap12.SOAPBinding soapBinding = binding.soap12Binding();
/*  711 */           soapBinding.transport(this.binding.getBindingId().getTransport());
/*  712 */           if (sBinding.getStyle().equals(SOAPBinding.Style.DOCUMENT))
/*  713 */             soapBinding.style("document");
/*      */           else
/*  715 */             soapBinding.style("rpc");
/*      */         } else {
/*  717 */           com.sun.xml.internal.ws.wsdl.writer.document.soap.SOAPBinding soapBinding = binding.soapBinding();
/*  718 */           soapBinding.transport(this.binding.getBindingId().getTransport());
/*  719 */           if (sBinding.getStyle().equals(SOAPBinding.Style.DOCUMENT))
/*  720 */             soapBinding.style("document");
/*      */           else
/*  722 */             soapBinding.style("rpc");
/*      */         }
/*  724 */         first = false;
/*      */       }
/*  726 */       if (this.binding.getBindingId().getSOAPVersion() == SOAPVersion.SOAP_12)
/*  727 */         generateSOAP12BindingOperation(method, binding);
/*      */       else
/*  729 */         generateBindingOperation(method, binding);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void generateBindingOperation(JavaMethodImpl method, Binding binding) {
/*  734 */     BindingOperationType operation = binding.operation().name(method.getOperationName());
/*  735 */     this.extension.addBindingOperationExtension(operation, method);
/*  736 */     String targetNamespace = this.model.getTargetNamespace();
/*  737 */     QName requestMessage = new QName(targetNamespace, method.getOperationName());
/*  738 */     List bodyParams = new ArrayList();
/*  739 */     List headerParams = new ArrayList();
/*  740 */     splitParameters(bodyParams, headerParams, method.getRequestParameters());
/*  741 */     com.sun.xml.internal.ws.api.model.soap.SOAPBinding soapBinding = method.getBinding();
/*  742 */     operation.soapOperation().soapAction(soapBinding.getSOAPAction());
/*      */ 
/*  745 */     TypedXmlWriter input = operation.input();
/*  746 */     this.extension.addBindingOperationInputExtension(input, method);
/*  747 */     com.sun.xml.internal.ws.wsdl.writer.document.soap.BodyType body = (com.sun.xml.internal.ws.wsdl.writer.document.soap.BodyType)input._element(com.sun.xml.internal.ws.wsdl.writer.document.soap.Body.class);
/*  748 */     boolean isRpc = soapBinding.getStyle().equals(SOAPBinding.Style.RPC);
/*  749 */     if (soapBinding.getUse() == SOAPBinding.Use.LITERAL) {
/*  750 */       body.use("literal");
/*  751 */       if (headerParams.size() > 0) {
/*  752 */         if (bodyParams.size() > 0) {
/*  753 */           ParameterImpl param = (ParameterImpl)bodyParams.iterator().next();
/*  754 */           if (isRpc) {
/*  755 */             StringBuffer parts = new StringBuffer();
/*  756 */             int i = 0;
/*  757 */             for (ParameterImpl parameter : ((WrapperParameter)param).getWrapperChildren()) {
/*  758 */               if (i++ > 0)
/*  759 */                 parts.append(' ');
/*  760 */               parts.append(parameter.getPartName());
/*      */             }
/*  762 */             body.parts(parts.toString());
/*      */           } else {
/*  764 */             body.parts(param.getPartName());
/*      */           }
/*      */         } else {
/*  767 */           body.parts("");
/*      */         }
/*  769 */         generateSOAPHeaders(input, headerParams, requestMessage);
/*      */       }
/*  771 */       if (isRpc)
/*  772 */         body.namespace(((ParameterImpl)method.getRequestParameters().iterator().next()).getName().getNamespaceURI());
/*      */     }
/*      */     else
/*      */     {
/*  776 */       throw new WebServiceException("encoded use is not supported");
/*      */     }
/*      */ 
/*  779 */     if (method.getMEP() != MEP.ONE_WAY) {
/*  780 */       boolean unwrappable = headerParams.size() == 0;
/*      */ 
/*  782 */       bodyParams.clear();
/*  783 */       headerParams.clear();
/*  784 */       splitParameters(bodyParams, headerParams, method.getResponseParameters());
/*  785 */       unwrappable = unwrappable ? false : headerParams.size() == 0 ? true : unwrappable;
/*  786 */       TypedXmlWriter output = operation.output();
/*  787 */       this.extension.addBindingOperationOutputExtension(output, method);
/*  788 */       body = (com.sun.xml.internal.ws.wsdl.writer.document.soap.BodyType)output._element(com.sun.xml.internal.ws.wsdl.writer.document.soap.Body.class);
/*  789 */       body.use("literal");
/*  790 */       if (headerParams.size() > 0) {
/*  791 */         String parts = "";
/*  792 */         if (bodyParams.size() > 0) {
/*  793 */           ParameterImpl param = bodyParams.iterator().hasNext() ? (ParameterImpl)bodyParams.iterator().next() : null;
/*  794 */           if (param != null)
/*      */           {
/*      */             int i;
/*  795 */             if (isRpc) {
/*  796 */               i = 0;
/*  797 */               for (ParameterImpl parameter : ((WrapperParameter)param).getWrapperChildren()) {
/*  798 */                 if (i++ > 0)
/*  799 */                   parts = parts + " ";
/*  800 */                 parts = parts + parameter.getPartName();
/*      */               }
/*      */             } else {
/*  803 */               parts = param.getPartName();
/*      */             }
/*      */           }
/*      */         }
/*  807 */         body.parts(parts);
/*  808 */         QName responseMessage = new QName(targetNamespace, method.getResponseMessageName());
/*  809 */         generateSOAPHeaders(output, headerParams, responseMessage);
/*      */       }
/*  811 */       if (isRpc) {
/*  812 */         body.namespace(((ParameterImpl)method.getRequestParameters().iterator().next()).getName().getNamespaceURI());
/*      */       }
/*      */     }
/*  815 */     for (CheckedExceptionImpl exception : method.getCheckedExceptions()) {
/*  816 */       Fault fault = operation.fault().name(exception.getMessageName());
/*  817 */       this.extension.addBindingOperationFaultExtension(fault, method, exception);
/*  818 */       com.sun.xml.internal.ws.wsdl.writer.document.soap.SOAPFault soapFault = ((com.sun.xml.internal.ws.wsdl.writer.document.soap.SOAPFault)fault._element(com.sun.xml.internal.ws.wsdl.writer.document.soap.SOAPFault.class)).name(exception.getMessageName());
/*  819 */       soapFault.use("literal");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void generateSOAP12BindingOperation(JavaMethodImpl method, Binding binding) {
/*  824 */     BindingOperationType operation = binding.operation().name(method.getOperationName());
/*  825 */     this.extension.addBindingOperationExtension(operation, method);
/*  826 */     String targetNamespace = this.model.getTargetNamespace();
/*  827 */     QName requestMessage = new QName(targetNamespace, method.getOperationName());
/*  828 */     ArrayList bodyParams = new ArrayList();
/*  829 */     ArrayList headerParams = new ArrayList();
/*  830 */     splitParameters(bodyParams, headerParams, method.getRequestParameters());
/*  831 */     com.sun.xml.internal.ws.api.model.soap.SOAPBinding soapBinding = method.getBinding();
/*  832 */     operation.soap12Operation().soapAction(soapBinding.getSOAPAction());
/*      */ 
/*  835 */     TypedXmlWriter input = operation.input();
/*  836 */     this.extension.addBindingOperationInputExtension(input, method);
/*  837 */     com.sun.xml.internal.ws.wsdl.writer.document.soap12.BodyType body = (com.sun.xml.internal.ws.wsdl.writer.document.soap12.BodyType)input._element(com.sun.xml.internal.ws.wsdl.writer.document.soap12.Body.class);
/*  838 */     boolean isRpc = soapBinding.getStyle().equals(SOAPBinding.Style.RPC);
/*  839 */     if (soapBinding.getUse().equals(SOAPBinding.Use.LITERAL)) {
/*  840 */       body.use("literal");
/*  841 */       if (headerParams.size() > 0) {
/*  842 */         if (bodyParams.size() > 0) {
/*  843 */           ParameterImpl param = (ParameterImpl)bodyParams.iterator().next();
/*  844 */           if (isRpc) {
/*  845 */             StringBuffer parts = new StringBuffer();
/*  846 */             int i = 0;
/*  847 */             for (ParameterImpl parameter : ((WrapperParameter)param).getWrapperChildren()) {
/*  848 */               if (i++ > 0)
/*  849 */                 parts.append(' ');
/*  850 */               parts.append(parameter.getPartName());
/*      */             }
/*  852 */             body.parts(parts.toString());
/*      */           } else {
/*  854 */             body.parts(param.getPartName());
/*      */           }
/*      */         } else {
/*  857 */           body.parts("");
/*      */         }
/*  859 */         generateSOAP12Headers(input, headerParams, requestMessage);
/*      */       }
/*  861 */       if (isRpc)
/*  862 */         body.namespace(((ParameterImpl)method.getRequestParameters().iterator().next()).getName().getNamespaceURI());
/*      */     }
/*      */     else
/*      */     {
/*  866 */       throw new WebServiceException("encoded use is not supported");
/*      */     }
/*      */ 
/*  869 */     if (method.getMEP() != MEP.ONE_WAY)
/*      */     {
/*  871 */       boolean unwrappable = headerParams.size() == 0;
/*  872 */       bodyParams.clear();
/*  873 */       headerParams.clear();
/*  874 */       splitParameters(bodyParams, headerParams, method.getResponseParameters());
/*  875 */       unwrappable = unwrappable ? false : headerParams.size() == 0 ? true : unwrappable;
/*  876 */       TypedXmlWriter output = operation.output();
/*  877 */       this.extension.addBindingOperationOutputExtension(output, method);
/*  878 */       body = (com.sun.xml.internal.ws.wsdl.writer.document.soap12.BodyType)output._element(com.sun.xml.internal.ws.wsdl.writer.document.soap12.Body.class);
/*  879 */       body.use("literal");
/*  880 */       if (headerParams.size() > 0) {
/*  881 */         if (bodyParams.size() > 0) {
/*  882 */           ParameterImpl param = (ParameterImpl)bodyParams.iterator().next();
/*  883 */           if (isRpc) {
/*  884 */             String parts = "";
/*  885 */             int i = 0;
/*  886 */             for (ParameterImpl parameter : ((WrapperParameter)param).getWrapperChildren()) {
/*  887 */               if (i++ > 0)
/*  888 */                 parts = parts + " ";
/*  889 */               parts = parts + parameter.getPartName();
/*      */             }
/*  891 */             body.parts(parts);
/*      */           } else {
/*  893 */             body.parts(param.getPartName());
/*      */           }
/*      */         } else {
/*  896 */           body.parts("");
/*      */         }
/*  898 */         QName responseMessage = new QName(targetNamespace, method.getResponseMessageName());
/*  899 */         generateSOAP12Headers(output, headerParams, responseMessage);
/*      */       }
/*  901 */       if (isRpc) {
/*  902 */         body.namespace(((ParameterImpl)method.getRequestParameters().iterator().next()).getName().getNamespaceURI());
/*      */       }
/*      */     }
/*  905 */     for (CheckedExceptionImpl exception : method.getCheckedExceptions()) {
/*  906 */       Fault fault = operation.fault().name(exception.getMessageName());
/*  907 */       this.extension.addBindingOperationFaultExtension(fault, method, exception);
/*  908 */       com.sun.xml.internal.ws.wsdl.writer.document.soap12.SOAPFault soapFault = ((com.sun.xml.internal.ws.wsdl.writer.document.soap12.SOAPFault)fault._element(com.sun.xml.internal.ws.wsdl.writer.document.soap12.SOAPFault.class)).name(exception.getMessageName());
/*  909 */       soapFault.use("literal");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void splitParameters(List<ParameterImpl> bodyParams, List<ParameterImpl> headerParams, List<ParameterImpl> params) {
/*  914 */     for (ParameterImpl parameter : params)
/*  915 */       if (isBodyParameter(parameter))
/*  916 */         bodyParams.add(parameter);
/*      */       else
/*  918 */         headerParams.add(parameter);
/*      */   }
/*      */ 
/*      */   protected void generateSOAPHeaders(TypedXmlWriter writer, List<ParameterImpl> parameters, QName message)
/*      */   {
/*  925 */     for (ParameterImpl headerParam : parameters) {
/*  926 */       com.sun.xml.internal.ws.wsdl.writer.document.soap.Header header = (com.sun.xml.internal.ws.wsdl.writer.document.soap.Header)writer._element(com.sun.xml.internal.ws.wsdl.writer.document.soap.Header.class);
/*  927 */       header.message(message);
/*  928 */       header.part(headerParam.getPartName());
/*  929 */       header.use("literal");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void generateSOAP12Headers(TypedXmlWriter writer, List<ParameterImpl> parameters, QName message)
/*      */   {
/*  935 */     for (ParameterImpl headerParam : parameters) {
/*  936 */       com.sun.xml.internal.ws.wsdl.writer.document.soap12.Header header = (com.sun.xml.internal.ws.wsdl.writer.document.soap12.Header)writer._element(com.sun.xml.internal.ws.wsdl.writer.document.soap12.Header.class);
/*  937 */       header.message(message);
/*      */ 
/*  940 */       header.part(headerParam.getPartName());
/*  941 */       header.use("literal");
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void generateService()
/*      */   {
/*  949 */     QName portQName = this.model.getPortName();
/*  950 */     QName serviceQName = this.model.getServiceQName();
/*  951 */     Service service = this.serviceDefinitions.service().name(serviceQName.getLocalPart());
/*  952 */     this.extension.addServiceExtension(service);
/*  953 */     Port port = service.port().name(portQName.getLocalPart());
/*  954 */     port.binding(this.model.getBoundPortTypeName());
/*  955 */     this.extension.addPortExtension(port);
/*  956 */     if (this.model.getJavaMethods().size() == 0) {
/*  957 */       return;
/*      */     }
/*  959 */     if (this.binding.getBindingId().getSOAPVersion() == SOAPVersion.SOAP_12) {
/*  960 */       com.sun.xml.internal.ws.wsdl.writer.document.soap12.SOAPAddress address = (com.sun.xml.internal.ws.wsdl.writer.document.soap12.SOAPAddress)port._element(com.sun.xml.internal.ws.wsdl.writer.document.soap12.SOAPAddress.class);
/*  961 */       address.location(this.endpointAddress);
/*      */     } else {
/*  963 */       com.sun.xml.internal.ws.wsdl.writer.document.soap.SOAPAddress address = (com.sun.xml.internal.ws.wsdl.writer.document.soap.SOAPAddress)port._element(com.sun.xml.internal.ws.wsdl.writer.document.soap.SOAPAddress.class);
/*  964 */       address.location(this.endpointAddress);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void generateInputMessage(Operation operation, JavaMethodImpl method) {
/*  969 */     ParamType paramType = operation.input();
/*  970 */     this.extension.addOperationInputExtension(paramType, method);
/*      */ 
/*  972 */     paramType.message(new QName(this.model.getTargetNamespace(), method.getRequestMessageName()));
/*      */   }
/*      */ 
/*      */   protected void generateOutputMessage(Operation operation, JavaMethodImpl method) {
/*  976 */     ParamType paramType = operation.output();
/*  977 */     this.extension.addOperationOutputExtension(paramType, method);
/*      */ 
/*  979 */     paramType.message(new QName(this.model.getTargetNamespace(), method.getResponseMessageName()));
/*      */   }
/*      */ 
/*      */   public Result createOutputFile(String namespaceUri, String suggestedFileName)
/*      */     throws IOException
/*      */   {
/*  992 */     if (namespaceUri.equals("")) {
/*  993 */       return null;
/*      */     }
/*  995 */     com.sun.xml.internal.ws.wsdl.writer.document.xsd.Import _import = this.types.schema()._import().namespace(namespaceUri);
/*      */ 
/*  997 */     Holder fileNameHolder = new Holder();
/*  998 */     fileNameHolder.value = (this.schemaPrefix + suggestedFileName);
/*  999 */     Result result = this.wsdlResolver.getSchemaOutput(namespaceUri, fileNameHolder);
/*      */     String schemaLoc;
/*      */     String schemaLoc;
/* 1003 */     if (result == null)
/* 1004 */       schemaLoc = (String)fileNameHolder.value;
/*      */     else {
/* 1006 */       schemaLoc = relativize(result.getSystemId(), this.wsdlLocation);
/*      */     }
/* 1008 */     _import.schemaLocation(schemaLoc);
/* 1009 */     return result;
/*      */   }
/*      */ 
/*      */   private Result createInlineSchema(String namespaceUri, String suggestedFileName) throws IOException
/*      */   {
/* 1014 */     if (namespaceUri.equals("")) {
/* 1015 */       return null;
/*      */     }
/*      */ 
/* 1027 */     Result result = new TXWResult(this.types);
/* 1028 */     result.setSystemId("");
/*      */ 
/* 1030 */     return result;
/*      */   }
/*      */ 
/*      */   protected static String relativize(String uri, String baseUri)
/*      */   {
/*      */     try
/*      */     {
/* 1054 */       assert (uri != null);
/*      */ 
/* 1056 */       if (baseUri == null) return uri;
/*      */ 
/* 1058 */       URI theUri = new URI(Util.escapeURI(uri));
/* 1059 */       URI theBaseUri = new URI(Util.escapeURI(baseUri));
/*      */ 
/* 1061 */       if ((theUri.isOpaque()) || (theBaseUri.isOpaque())) {
/* 1062 */         return uri;
/*      */       }
/* 1064 */       if ((!Util.equalsIgnoreCase(theUri.getScheme(), theBaseUri.getScheme())) || (!Util.equal(theUri.getAuthority(), theBaseUri.getAuthority())))
/*      */       {
/* 1066 */         return uri;
/*      */       }
/* 1068 */       String uriPath = theUri.getPath();
/* 1069 */       String basePath = theBaseUri.getPath();
/*      */ 
/* 1072 */       if (!basePath.endsWith("/")) {
/* 1073 */         basePath = Util.normalizeUriPath(basePath);
/*      */       }
/*      */ 
/* 1076 */       if (uriPath.equals(basePath)) {
/* 1077 */         return ".";
/*      */       }
/* 1079 */       String relPath = calculateRelativePath(uriPath, basePath);
/*      */ 
/* 1081 */       if (relPath == null)
/* 1082 */         return uri;
/* 1083 */       StringBuffer relUri = new StringBuffer();
/* 1084 */       relUri.append(relPath);
/* 1085 */       if (theUri.getQuery() != null)
/* 1086 */         relUri.append('?').append(theUri.getQuery());
/* 1087 */       if (theUri.getFragment() != null) {
/* 1088 */         relUri.append('#').append(theUri.getFragment());
/*      */       }
/* 1090 */       return relUri.toString(); } catch (URISyntaxException e) {
/*      */     }
/* 1092 */     throw new InternalError("Error escaping one of these uris:\n\t" + uri + "\n\t" + baseUri);
/*      */   }
/*      */ 
/*      */   private static String calculateRelativePath(String uri, String base)
/*      */   {
/* 1097 */     if (base == null) {
/* 1098 */       return null;
/*      */     }
/* 1100 */     if (uri.startsWith(base)) {
/* 1101 */       return uri.substring(base.length());
/*      */     }
/* 1103 */     return "../" + calculateRelativePath(uri, Util.getParentUriPath(base));
/*      */   }
/*      */ 
/*      */   private void register(WSDLGeneratorExtension h)
/*      */   {
/* 1129 */     this.extensionHandlers.add(h);
/*      */   }
/*      */ 
/*      */   private static class CommentFilter
/*      */     implements XmlSerializer
/*      */   {
/*      */     final XmlSerializer serializer;
/*  276 */     private static final String VERSION_COMMENT = " Generated by JAX-WS RI at http://jax-ws.dev.java.net. RI's version is " + RuntimeVersion.VERSION + ". ";
/*      */ 
/*      */     CommentFilter(XmlSerializer serializer)
/*      */     {
/*  280 */       this.serializer = serializer;
/*      */     }
/*      */ 
/*      */     public void startDocument() {
/*  284 */       this.serializer.startDocument();
/*  285 */       comment(new StringBuilder(VERSION_COMMENT));
/*  286 */       text(new StringBuilder("\n"));
/*      */     }
/*      */ 
/*      */     public void beginStartTag(String uri, String localName, String prefix) {
/*  290 */       this.serializer.beginStartTag(uri, localName, prefix);
/*      */     }
/*      */ 
/*      */     public void writeAttribute(String uri, String localName, String prefix, StringBuilder value) {
/*  294 */       this.serializer.writeAttribute(uri, localName, prefix, value);
/*      */     }
/*      */ 
/*      */     public void writeXmlns(String prefix, String uri) {
/*  298 */       this.serializer.writeXmlns(prefix, uri);
/*      */     }
/*      */ 
/*      */     public void endStartTag(String uri, String localName, String prefix) {
/*  302 */       this.serializer.endStartTag(uri, localName, prefix);
/*      */     }
/*      */ 
/*      */     public void endTag() {
/*  306 */       this.serializer.endTag();
/*      */     }
/*      */ 
/*      */     public void text(StringBuilder text) {
/*  310 */       this.serializer.text(text);
/*      */     }
/*      */ 
/*      */     public void cdata(StringBuilder text) {
/*  314 */       this.serializer.cdata(text);
/*      */     }
/*      */ 
/*      */     public void comment(StringBuilder comment) {
/*  318 */       this.serializer.comment(comment);
/*      */     }
/*      */ 
/*      */     public void endDocument() {
/*  322 */       this.serializer.endDocument();
/*      */     }
/*      */ 
/*      */     public void flush() {
/*  326 */       this.serializer.flush();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class JAXWSOutputSchemaResolver extends SchemaOutputResolver
/*      */   {
/*      */     protected JAXWSOutputSchemaResolver()
/*      */     {
/*      */     }
/*      */ 
/*      */     public Result createOutput(String namespaceUri, String suggestedFileName)
/*      */       throws IOException
/*      */     {
/* 1122 */       return WSDLGenerator.this.inlineSchemas ? WSDLGenerator.this.createInlineSchema(namespaceUri, suggestedFileName) : WSDLGenerator.this.createOutputFile(namespaceUri, suggestedFileName);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.wsdl.writer.WSDLGenerator
 * JD-Core Version:    0.6.2
 */