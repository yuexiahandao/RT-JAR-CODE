/*     */ package com.sun.xml.internal.ws.policy.jaxws;
/*     */ 
/*     */ import com.sun.xml.internal.txw2.TypedXmlWriter;
/*     */ import com.sun.xml.internal.ws.addressing.policy.AddressingPolicyMapConfigurator;
/*     */ import com.sun.xml.internal.ws.api.WSBinding;
/*     */ import com.sun.xml.internal.ws.api.model.CheckedException;
/*     */ import com.sun.xml.internal.ws.api.model.JavaMethod;
/*     */ import com.sun.xml.internal.ws.api.model.SEIModel;
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
/*     */ import com.sun.xml.internal.ws.api.policy.ModelGenerator;
/*     */ import com.sun.xml.internal.ws.api.policy.PolicyResolver;
/*     */ import com.sun.xml.internal.ws.api.policy.PolicyResolver.ServerContext;
/*     */ import com.sun.xml.internal.ws.api.policy.PolicyResolverFactory;
/*     */ import com.sun.xml.internal.ws.api.wsdl.writer.WSDLGenExtnContext;
/*     */ import com.sun.xml.internal.ws.api.wsdl.writer.WSDLGeneratorExtension;
/*     */ import com.sun.xml.internal.ws.encoding.policy.MtomPolicyMapConfigurator;
/*     */ import com.sun.xml.internal.ws.policy.Policy;
/*     */ import com.sun.xml.internal.ws.policy.PolicyException;
/*     */ import com.sun.xml.internal.ws.policy.PolicyMap;
/*     */ import com.sun.xml.internal.ws.policy.PolicyMapExtender;
/*     */ import com.sun.xml.internal.ws.policy.PolicyMapUtil;
/*     */ import com.sun.xml.internal.ws.policy.PolicyMerger;
/*     */ import com.sun.xml.internal.ws.policy.PolicySubject;
/*     */ import com.sun.xml.internal.ws.policy.jaxws.spi.PolicyMapConfigurator;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelGenerator;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelMarshaller;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModel;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken;
/*     */ import com.sun.xml.internal.ws.policy.subject.WsdlBindingSubject;
/*     */ import com.sun.xml.internal.ws.policy.subject.WsdlBindingSubject.WsdlMessageType;
/*     */ import com.sun.xml.internal.ws.resources.PolicyMessages;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public class PolicyWSDLGeneratorExtension extends WSDLGeneratorExtension
/*     */ {
/*  96 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(PolicyWSDLGeneratorExtension.class);
/*     */   private PolicyMap policyMap;
/*     */   private SEIModel seiModel;
/*  99 */   private final Collection<PolicySubject> subjects = new LinkedList();
/* 100 */   private final PolicyModelMarshaller marshaller = PolicyModelMarshaller.getXmlMarshaller(true);
/* 101 */   private final PolicyMerger merger = PolicyMerger.getMerger();
/*     */ 
/*     */   public void start(WSDLGenExtnContext context)
/*     */   {
/* 105 */     LOGGER.entering();
/*     */     try {
/* 107 */       this.seiModel = context.getModel();
/*     */ 
/* 109 */       PolicyMapConfigurator[] policyMapConfigurators = loadConfigurators();
/* 110 */       PolicyMapExtender[] extenders = new PolicyMapExtender[policyMapConfigurators.length];
/* 111 */       for (int i = 0; i < policyMapConfigurators.length; i++) {
/* 112 */         extenders[i] = PolicyMapExtender.createPolicyMapExtender();
/*     */       }
/*     */ 
/* 115 */       this.policyMap = PolicyResolverFactory.create().resolve(new PolicyResolver.ServerContext(this.policyMap, context.getContainer(), context.getEndpointClass(), false, extenders));
/*     */ 
/* 118 */       if (this.policyMap == null) {
/* 119 */         LOGGER.fine(PolicyMessages.WSP_1019_CREATE_EMPTY_POLICY_MAP());
/* 120 */         this.policyMap = PolicyMap.createPolicyMap(Arrays.asList(extenders));
/*     */       }
/*     */ 
/* 123 */       WSBinding binding = context.getBinding();
/*     */       try {
/* 125 */         Collection policySubjects = new LinkedList();
/* 126 */         for (int i = 0; i < policyMapConfigurators.length; i++) {
/* 127 */           policySubjects.addAll(policyMapConfigurators[i].update(this.policyMap, this.seiModel, binding));
/* 128 */           extenders[i].disconnect();
/*     */         }
/* 130 */         PolicyMapUtil.insertPolicies(this.policyMap, policySubjects, this.seiModel.getServiceQName(), this.seiModel.getPortName());
/*     */       } catch (PolicyException e) {
/* 132 */         throw ((WebServiceException)LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1017_MAP_UPDATE_FAILED(), e)));
/*     */       }
/* 134 */       TypedXmlWriter root = context.getRoot();
/* 135 */       root._namespace(NamespaceVersion.v1_2.toString(), NamespaceVersion.v1_2.getDefaultNamespacePrefix());
/* 136 */       root._namespace(NamespaceVersion.v1_5.toString(), NamespaceVersion.v1_5.getDefaultNamespacePrefix());
/* 137 */       root._namespace("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "wsu");
/*     */     }
/*     */     finally {
/* 140 */       LOGGER.exiting();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addDefinitionsExtension(TypedXmlWriter definitions)
/*     */   {
/*     */     try {
/* 147 */       LOGGER.entering();
/* 148 */       if (this.policyMap == null) {
/* 149 */         LOGGER.fine(PolicyMessages.WSP_1009_NOT_MARSHALLING_ANY_POLICIES_POLICY_MAP_IS_NULL());
/*     */       } else {
/* 151 */         this.subjects.addAll(this.policyMap.getPolicySubjects());
/* 152 */         generator = ModelGenerator.getGenerator();
/* 153 */         policyIDsOrNamesWritten = new HashSet();
/* 154 */         for (PolicySubject subject : this.subjects)
/* 155 */           if (subject.getSubject() == null) {
/* 156 */             LOGGER.fine(PolicyMessages.WSP_1008_NOT_MARSHALLING_WSDL_SUBJ_NULL(subject));
/*     */           } else {
/*     */             Policy policy;
/*     */             try {
/* 160 */               policy = subject.getEffectivePolicy(this.merger);
/*     */             } catch (PolicyException e) {
/* 162 */               throw ((WebServiceException)LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1011_FAILED_TO_RETRIEVE_EFFECTIVE_POLICY_FOR_SUBJECT(subject.toString()), e)));
/*     */             }
/* 164 */             if ((null == policy.getIdOrName()) || (policyIDsOrNamesWritten.contains(policy.getIdOrName()))) {
/* 165 */               LOGGER.fine(PolicyMessages.WSP_1016_POLICY_ID_NULL_OR_DUPLICATE(policy));
/*     */             } else {
/*     */               try {
/* 168 */                 PolicySourceModel policyInfoset = generator.translate(policy);
/* 169 */                 this.marshaller.marshal(policyInfoset, definitions);
/*     */               } catch (PolicyException e) {
/* 171 */                 throw ((WebServiceException)LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1018_FAILED_TO_MARSHALL_POLICY(policy.getIdOrName()), e)));
/*     */               }
/* 173 */               policyIDsOrNamesWritten.add(policy.getIdOrName());
/*     */             }
/*     */           }
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/*     */       PolicyModelGenerator generator;
/*     */       Set policyIDsOrNamesWritten;
/* 179 */       LOGGER.exiting();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addServiceExtension(TypedXmlWriter service)
/*     */   {
/* 185 */     LOGGER.entering();
/* 186 */     String serviceName = null == this.seiModel ? null : this.seiModel.getServiceQName().getLocalPart();
/* 187 */     selectAndProcessSubject(service, WSDLService.class, ScopeType.SERVICE, serviceName);
/* 188 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public void addPortExtension(TypedXmlWriter port)
/*     */   {
/* 193 */     LOGGER.entering();
/* 194 */     String portName = null == this.seiModel ? null : this.seiModel.getPortName().getLocalPart();
/* 195 */     selectAndProcessSubject(port, WSDLPort.class, ScopeType.ENDPOINT, portName);
/* 196 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public void addPortTypeExtension(TypedXmlWriter portType)
/*     */   {
/* 201 */     LOGGER.entering();
/* 202 */     String portTypeName = null == this.seiModel ? null : this.seiModel.getPortTypeName().getLocalPart();
/* 203 */     selectAndProcessSubject(portType, WSDLPortType.class, ScopeType.ENDPOINT, portTypeName);
/* 204 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public void addBindingExtension(TypedXmlWriter binding)
/*     */   {
/* 209 */     LOGGER.entering();
/* 210 */     QName bindingName = null == this.seiModel ? null : this.seiModel.getBoundPortTypeName();
/* 211 */     selectAndProcessBindingSubject(binding, WSDLBoundPortType.class, ScopeType.ENDPOINT, bindingName);
/* 212 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public void addOperationExtension(TypedXmlWriter operation, JavaMethod method)
/*     */   {
/* 217 */     LOGGER.entering();
/* 218 */     selectAndProcessSubject(operation, WSDLOperation.class, ScopeType.OPERATION, (String)null);
/* 219 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public void addBindingOperationExtension(TypedXmlWriter operation, JavaMethod method)
/*     */   {
/* 224 */     LOGGER.entering();
/* 225 */     QName operationName = method == null ? null : new QName(method.getOwner().getTargetNamespace(), method.getOperationName());
/* 226 */     selectAndProcessBindingSubject(operation, WSDLBoundOperation.class, ScopeType.OPERATION, operationName);
/* 227 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public void addInputMessageExtension(TypedXmlWriter message, JavaMethod method)
/*     */   {
/* 232 */     LOGGER.entering();
/* 233 */     String messageName = null == method ? null : method.getRequestMessageName();
/* 234 */     selectAndProcessSubject(message, WSDLMessage.class, ScopeType.INPUT_MESSAGE, messageName);
/* 235 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public void addOutputMessageExtension(TypedXmlWriter message, JavaMethod method)
/*     */   {
/* 240 */     LOGGER.entering();
/* 241 */     String messageName = null == method ? null : method.getResponseMessageName();
/* 242 */     selectAndProcessSubject(message, WSDLMessage.class, ScopeType.OUTPUT_MESSAGE, messageName);
/* 243 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public void addFaultMessageExtension(TypedXmlWriter message, JavaMethod method, CheckedException exception)
/*     */   {
/* 248 */     LOGGER.entering();
/* 249 */     String messageName = null == exception ? null : exception.getMessageName();
/* 250 */     selectAndProcessSubject(message, WSDLMessage.class, ScopeType.FAULT_MESSAGE, messageName);
/* 251 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public void addOperationInputExtension(TypedXmlWriter input, JavaMethod method)
/*     */   {
/* 256 */     LOGGER.entering();
/* 257 */     String messageName = null == method ? null : method.getRequestMessageName();
/* 258 */     selectAndProcessSubject(input, WSDLInput.class, ScopeType.INPUT_MESSAGE, messageName);
/* 259 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public void addOperationOutputExtension(TypedXmlWriter output, JavaMethod method)
/*     */   {
/* 264 */     LOGGER.entering();
/* 265 */     String messageName = null == method ? null : method.getResponseMessageName();
/* 266 */     selectAndProcessSubject(output, WSDLOutput.class, ScopeType.OUTPUT_MESSAGE, messageName);
/* 267 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public void addOperationFaultExtension(TypedXmlWriter fault, JavaMethod method, CheckedException exception)
/*     */   {
/* 272 */     LOGGER.entering();
/* 273 */     String messageName = null == exception ? null : exception.getMessageName();
/* 274 */     selectAndProcessSubject(fault, WSDLFault.class, ScopeType.FAULT_MESSAGE, messageName);
/* 275 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public void addBindingOperationInputExtension(TypedXmlWriter input, JavaMethod method)
/*     */   {
/* 280 */     LOGGER.entering();
/* 281 */     QName operationName = new QName(method.getOwner().getTargetNamespace(), method.getOperationName());
/* 282 */     selectAndProcessBindingSubject(input, WSDLBoundOperation.class, ScopeType.INPUT_MESSAGE, operationName);
/* 283 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public void addBindingOperationOutputExtension(TypedXmlWriter output, JavaMethod method)
/*     */   {
/* 288 */     LOGGER.entering();
/* 289 */     QName operationName = new QName(method.getOwner().getTargetNamespace(), method.getOperationName());
/* 290 */     selectAndProcessBindingSubject(output, WSDLBoundOperation.class, ScopeType.OUTPUT_MESSAGE, operationName);
/* 291 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public void addBindingOperationFaultExtension(TypedXmlWriter writer, JavaMethod method, CheckedException exception)
/*     */   {
/* 296 */     LOGGER.entering(new Object[] { writer, method, exception });
/* 297 */     if (this.subjects != null) {
/* 298 */       for (PolicySubject subject : this.subjects) {
/* 299 */         if (this.policyMap.isFaultMessageSubject(subject)) {
/* 300 */           Object concreteSubject = subject.getSubject();
/* 301 */           if (concreteSubject != null) {
/* 302 */             String exceptionName = exception == null ? null : exception.getMessageName();
/* 303 */             if (exceptionName == null) {
/* 304 */               writePolicyOrReferenceIt(subject, writer);
/*     */             }
/* 306 */             if (WSDLBoundFaultContainer.class.isInstance(concreteSubject)) {
/* 307 */               WSDLBoundFaultContainer faultContainer = (WSDLBoundFaultContainer)concreteSubject;
/* 308 */               WSDLBoundFault fault = faultContainer.getBoundFault();
/* 309 */               WSDLBoundOperation operation = faultContainer.getBoundOperation();
/* 310 */               if ((exceptionName.equals(fault.getName())) && (operation.getName().getLocalPart().equals(method.getOperationName())))
/*     */               {
/* 312 */                 writePolicyOrReferenceIt(subject, writer);
/*     */               }
/*     */             }
/* 315 */             else if (WsdlBindingSubject.class.isInstance(concreteSubject)) {
/* 316 */               WsdlBindingSubject wsdlSubject = (WsdlBindingSubject)concreteSubject;
/* 317 */               if ((wsdlSubject.getMessageType() == WsdlBindingSubject.WsdlMessageType.FAULT) && (exception.getOwner().getTargetNamespace().equals(wsdlSubject.getName().getNamespaceURI())) && (exceptionName.equals(wsdlSubject.getName().getLocalPart())))
/*     */               {
/* 320 */                 writePolicyOrReferenceIt(subject, writer);
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 327 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   private void selectAndProcessSubject(TypedXmlWriter xmlWriter, Class clazz, ScopeType scopeType, QName bindingName)
/*     */   {
/* 340 */     LOGGER.entering(new Object[] { xmlWriter, clazz, scopeType, bindingName });
/* 341 */     if (bindingName == null) {
/* 342 */       selectAndProcessSubject(xmlWriter, clazz, scopeType, (String)null);
/*     */     } else {
/* 344 */       if (this.subjects != null) {
/* 345 */         for (PolicySubject subject : this.subjects) {
/* 346 */           if (bindingName.equals(subject.getSubject())) {
/* 347 */             writePolicyOrReferenceIt(subject, xmlWriter);
/*     */           }
/*     */         }
/*     */       }
/* 351 */       selectAndProcessSubject(xmlWriter, clazz, scopeType, bindingName.getLocalPart());
/*     */     }
/* 353 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   private void selectAndProcessBindingSubject(TypedXmlWriter xmlWriter, Class clazz, ScopeType scopeType, QName bindingName) {
/* 357 */     LOGGER.entering(new Object[] { xmlWriter, clazz, scopeType, bindingName });
/* 358 */     if ((this.subjects != null) && (bindingName != null)) {
/* 359 */       for (PolicySubject subject : this.subjects) {
/* 360 */         if ((subject.getSubject() instanceof WsdlBindingSubject)) {
/* 361 */           WsdlBindingSubject wsdlSubject = (WsdlBindingSubject)subject.getSubject();
/* 362 */           if (bindingName.equals(wsdlSubject.getName())) {
/* 363 */             writePolicyOrReferenceIt(subject, xmlWriter);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 368 */     selectAndProcessSubject(xmlWriter, clazz, scopeType, bindingName);
/* 369 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   private void selectAndProcessSubject(TypedXmlWriter xmlWriter, Class clazz, ScopeType scopeType, String wsdlName) {
/* 373 */     LOGGER.entering(new Object[] { xmlWriter, clazz, scopeType, wsdlName });
/* 374 */     if (this.subjects != null) {
/* 375 */       for (PolicySubject subject : this.subjects) {
/* 376 */         if (isCorrectType(this.policyMap, subject, scopeType)) {
/* 377 */           Object concreteSubject = subject.getSubject();
/* 378 */           if ((concreteSubject != null) && (clazz.isInstance(concreteSubject))) {
/* 379 */             if (null == wsdlName)
/* 380 */               writePolicyOrReferenceIt(subject, xmlWriter);
/*     */             else {
/*     */               try {
/* 383 */                 Method getNameMethod = clazz.getDeclaredMethod("getName", new Class[0]);
/* 384 */                 if (stringEqualsToStringOrQName(wsdlName, getNameMethod.invoke(concreteSubject, new Object[0])))
/* 385 */                   writePolicyOrReferenceIt(subject, xmlWriter);
/*     */               }
/*     */               catch (NoSuchMethodException e) {
/* 388 */                 throw ((WebServiceException)LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1003_UNABLE_TO_CHECK_ELEMENT_NAME(clazz.getName(), wsdlName), e)));
/*     */               } catch (IllegalAccessException e) {
/* 390 */                 throw ((WebServiceException)LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1003_UNABLE_TO_CHECK_ELEMENT_NAME(clazz.getName(), wsdlName), e)));
/*     */               } catch (InvocationTargetException e) {
/* 392 */                 throw ((WebServiceException)LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1003_UNABLE_TO_CHECK_ELEMENT_NAME(clazz.getName(), wsdlName), e)));
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 399 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   private static boolean isCorrectType(PolicyMap map, PolicySubject subject, ScopeType type) {
/* 403 */     switch (1.$SwitchMap$com$sun$xml$internal$ws$policy$jaxws$PolicyWSDLGeneratorExtension$ScopeType[type.ordinal()]) {
/*     */     case 1:
/* 405 */       return (!map.isInputMessageSubject(subject)) && (!map.isOutputMessageSubject(subject)) && (!map.isFaultMessageSubject(subject));
/*     */     case 2:
/* 407 */       return map.isInputMessageSubject(subject);
/*     */     case 3:
/* 409 */       return map.isOutputMessageSubject(subject);
/*     */     case 4:
/* 411 */       return map.isFaultMessageSubject(subject);
/*     */     }
/* 413 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean stringEqualsToStringOrQName(String first, Object second)
/*     */   {
/* 418 */     return (second instanceof QName) ? first.equals(((QName)second).getLocalPart()) : first.equals(second);
/*     */   }
/*     */ 
/*     */   private void writePolicyOrReferenceIt(PolicySubject subject, TypedXmlWriter writer)
/*     */   {
/*     */     Policy policy;
/*     */     try
/*     */     {
/* 433 */       policy = subject.getEffectivePolicy(this.merger);
/*     */     } catch (PolicyException e) {
/* 435 */       throw ((WebServiceException)LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1011_FAILED_TO_RETRIEVE_EFFECTIVE_POLICY_FOR_SUBJECT(subject.toString()), e)));
/*     */     }
/* 437 */     if (policy != null)
/* 438 */       if (null == policy.getIdOrName()) {
/* 439 */         PolicyModelGenerator generator = ModelGenerator.getGenerator();
/*     */         try {
/* 441 */           PolicySourceModel policyInfoset = generator.translate(policy);
/* 442 */           this.marshaller.marshal(policyInfoset, writer);
/*     */         } catch (PolicyException pe) {
/* 444 */           throw ((WebServiceException)LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1002_UNABLE_TO_MARSHALL_POLICY_OR_POLICY_REFERENCE(), pe)));
/*     */         }
/*     */       } else {
/* 447 */         TypedXmlWriter policyReference = writer._element(policy.getNamespaceVersion().asQName(XmlToken.PolicyReference), TypedXmlWriter.class);
/* 448 */         policyReference._attribute(XmlToken.Uri.toString(), '#' + policy.getIdOrName());
/*     */       }
/*     */   }
/*     */ 
/*     */   private PolicyMapConfigurator[] loadConfigurators()
/*     */   {
/* 454 */     Collection configurators = new LinkedList();
/*     */ 
/* 457 */     configurators.add(new AddressingPolicyMapConfigurator());
/* 458 */     configurators.add(new MtomPolicyMapConfigurator());
/*     */ 
/* 461 */     PolicyUtil.addServiceProviders(configurators, PolicyMapConfigurator.class);
/*     */ 
/* 463 */     return (PolicyMapConfigurator[])configurators.toArray(new PolicyMapConfigurator[configurators.size()]);
/*     */   }
/*     */ 
/*     */   static enum ScopeType
/*     */   {
/*  89 */     SERVICE, 
/*  90 */     ENDPOINT, 
/*  91 */     OPERATION, 
/*  92 */     INPUT_MESSAGE, 
/*  93 */     OUTPUT_MESSAGE, 
/*  94 */     FAULT_MESSAGE;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.jaxws.PolicyWSDLGeneratorExtension
 * JD-Core Version:    0.6.2
 */