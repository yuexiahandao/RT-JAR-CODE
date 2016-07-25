/*     */ package com.sun.xml.internal.ws.policy.jaxws;
/*     */ 
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundFault;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLBoundPortType;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLFault;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLInput;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLMessage;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLModel;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLObject;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLOperation;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPort;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLPortType;
/*     */ import com.sun.xml.internal.ws.api.model.wsdl.WSDLService;
/*     */ import com.sun.xml.internal.ws.api.policy.PolicyResolver;
/*     */ import com.sun.xml.internal.ws.api.policy.PolicyResolver.ClientContext;
/*     */ import com.sun.xml.internal.ws.api.policy.PolicyResolver.ServerContext;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtension;
/*     */ import com.sun.xml.internal.ws.api.wsdl.parser.WSDLParserExtensionContext;
/*     */ import com.sun.xml.internal.ws.model.wsdl.WSDLModelImpl;
/*     */ import com.sun.xml.internal.ws.policy.PolicyException;
/*     */ import com.sun.xml.internal.ws.policy.PolicyMap;
/*     */ import com.sun.xml.internal.ws.policy.PolicyMapMutator;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.IO;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModel;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModelContext;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.XmlToken;
/*     */ import com.sun.xml.internal.ws.resources.PolicyMessages;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.net.URI;
/*     */ import java.net.URISyntaxException;
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ import javax.xml.stream.Location;
/*     */ import javax.xml.stream.XMLInputFactory;
/*     */ import javax.xml.stream.XMLStreamException;
/*     */ import javax.xml.stream.XMLStreamReader;
/*     */ import javax.xml.ws.WebServiceException;
/*     */ 
/*     */ public final class PolicyWSDLParserExtension extends WSDLParserExtension
/*     */ {
/*  96 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(PolicyWSDLParserExtension.class);
/*     */ 
/*  99 */   private static final StringBuffer AnonymnousPolicyIdPrefix = new StringBuffer("#__anonymousPolicy__ID");
/*     */   private int anonymousPoliciesCount;
/* 104 */   private final SafePolicyReader policyReader = new SafePolicyReader();
/*     */ 
/* 107 */   private SafePolicyReader.PolicyRecord expandQueueHead = null;
/*     */ 
/* 110 */   private Map<String, SafePolicyReader.PolicyRecord> policyRecordsPassedBy = null;
/*     */ 
/* 112 */   private Map<String, PolicySourceModel> anonymousPolicyModels = null;
/*     */ 
/* 115 */   private List<String> unresolvedUris = null;
/*     */ 
/* 118 */   private final LinkedList<String> urisNeeded = new LinkedList();
/* 119 */   private final Map<String, PolicySourceModel> modelsNeeded = new HashMap();
/*     */ 
/* 122 */   private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4ServiceMap = null;
/* 123 */   private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4PortMap = null;
/* 124 */   private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4PortTypeMap = null;
/* 125 */   private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4BindingMap = null;
/* 126 */   private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4BoundOperationMap = null;
/* 127 */   private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4OperationMap = null;
/* 128 */   private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4MessageMap = null;
/* 129 */   private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4InputMap = null;
/* 130 */   private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4OutputMap = null;
/* 131 */   private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4FaultMap = null;
/* 132 */   private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4BindingInputOpMap = null;
/* 133 */   private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4BindingOutputOpMap = null;
/* 134 */   private Map<WSDLObject, Collection<PolicyRecordHandler>> handlers4BindingFaultOpMap = null;
/*     */ 
/* 136 */   private PolicyMapBuilder policyBuilder = new PolicyMapBuilder();
/*     */ 
/*     */   private boolean isPolicyProcessed(String policyUri) {
/* 139 */     return this.modelsNeeded.containsKey(policyUri);
/*     */   }
/*     */ 
/*     */   private void addNewPolicyNeeded(String policyUri, PolicySourceModel policyModel) {
/* 143 */     if (!this.modelsNeeded.containsKey(policyUri)) {
/* 144 */       this.modelsNeeded.put(policyUri, policyModel);
/* 145 */       this.urisNeeded.addFirst(policyUri);
/*     */     }
/*     */   }
/*     */ 
/*     */   private Map<String, PolicySourceModel> getPolicyModels() {
/* 150 */     return this.modelsNeeded;
/*     */   }
/*     */ 
/*     */   private Map<String, SafePolicyReader.PolicyRecord> getPolicyRecordsPassedBy() {
/* 154 */     if (null == this.policyRecordsPassedBy) {
/* 155 */       this.policyRecordsPassedBy = new HashMap();
/*     */     }
/* 157 */     return this.policyRecordsPassedBy;
/*     */   }
/*     */ 
/*     */   private Map<String, PolicySourceModel> getAnonymousPolicyModels() {
/* 161 */     if (null == this.anonymousPolicyModels) {
/* 162 */       this.anonymousPolicyModels = new HashMap();
/*     */     }
/* 164 */     return this.anonymousPolicyModels;
/*     */   }
/*     */ 
/*     */   private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4ServiceMap() {
/* 168 */     if (null == this.handlers4ServiceMap) {
/* 169 */       this.handlers4ServiceMap = new HashMap();
/*     */     }
/* 171 */     return this.handlers4ServiceMap;
/*     */   }
/*     */ 
/*     */   private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4PortMap() {
/* 175 */     if (null == this.handlers4PortMap) {
/* 176 */       this.handlers4PortMap = new HashMap();
/*     */     }
/* 178 */     return this.handlers4PortMap;
/*     */   }
/*     */ 
/*     */   private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4PortTypeMap() {
/* 182 */     if (null == this.handlers4PortTypeMap) {
/* 183 */       this.handlers4PortTypeMap = new HashMap();
/*     */     }
/* 185 */     return this.handlers4PortTypeMap;
/*     */   }
/*     */ 
/*     */   private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4BindingMap() {
/* 189 */     if (null == this.handlers4BindingMap) {
/* 190 */       this.handlers4BindingMap = new HashMap();
/*     */     }
/* 192 */     return this.handlers4BindingMap;
/*     */   }
/*     */ 
/*     */   private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4OperationMap() {
/* 196 */     if (null == this.handlers4OperationMap) {
/* 197 */       this.handlers4OperationMap = new HashMap();
/*     */     }
/* 199 */     return this.handlers4OperationMap;
/*     */   }
/*     */ 
/*     */   private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4BoundOperationMap() {
/* 203 */     if (null == this.handlers4BoundOperationMap) {
/* 204 */       this.handlers4BoundOperationMap = new HashMap();
/*     */     }
/* 206 */     return this.handlers4BoundOperationMap;
/*     */   }
/*     */ 
/*     */   private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4MessageMap() {
/* 210 */     if (null == this.handlers4MessageMap) {
/* 211 */       this.handlers4MessageMap = new HashMap();
/*     */     }
/* 213 */     return this.handlers4MessageMap;
/*     */   }
/*     */ 
/*     */   private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4InputMap() {
/* 217 */     if (null == this.handlers4InputMap) {
/* 218 */       this.handlers4InputMap = new HashMap();
/*     */     }
/* 220 */     return this.handlers4InputMap;
/*     */   }
/*     */ 
/*     */   private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4OutputMap() {
/* 224 */     if (null == this.handlers4OutputMap) {
/* 225 */       this.handlers4OutputMap = new HashMap();
/*     */     }
/* 227 */     return this.handlers4OutputMap;
/*     */   }
/*     */ 
/*     */   private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4FaultMap() {
/* 231 */     if (null == this.handlers4FaultMap) {
/* 232 */       this.handlers4FaultMap = new HashMap();
/*     */     }
/* 234 */     return this.handlers4FaultMap;
/*     */   }
/*     */ 
/*     */   private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4BindingInputOpMap() {
/* 238 */     if (null == this.handlers4BindingInputOpMap) {
/* 239 */       this.handlers4BindingInputOpMap = new HashMap();
/*     */     }
/* 241 */     return this.handlers4BindingInputOpMap;
/*     */   }
/*     */ 
/*     */   private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4BindingOutputOpMap() {
/* 245 */     if (null == this.handlers4BindingOutputOpMap) {
/* 246 */       this.handlers4BindingOutputOpMap = new HashMap();
/*     */     }
/* 248 */     return this.handlers4BindingOutputOpMap;
/*     */   }
/*     */ 
/*     */   private Map<WSDLObject, Collection<PolicyRecordHandler>> getHandlers4BindingFaultOpMap() {
/* 252 */     if (null == this.handlers4BindingFaultOpMap) {
/* 253 */       this.handlers4BindingFaultOpMap = new HashMap();
/*     */     }
/* 255 */     return this.handlers4BindingFaultOpMap;
/*     */   }
/*     */ 
/*     */   private List<String> getUnresolvedUris(boolean emptyListNeeded) {
/* 259 */     if ((null == this.unresolvedUris) || (emptyListNeeded)) {
/* 260 */       this.unresolvedUris = new LinkedList();
/*     */     }
/* 262 */     return this.unresolvedUris;
/*     */   }
/*     */ 
/*     */   private void policyRecToExpandQueue(SafePolicyReader.PolicyRecord policyRec)
/*     */   {
/* 268 */     if (null == this.expandQueueHead)
/* 269 */       this.expandQueueHead = policyRec;
/*     */     else
/* 271 */       this.expandQueueHead = this.expandQueueHead.insert(policyRec);
/*     */   }
/*     */ 
/*     */   private PolicyRecordHandler readSinglePolicy(SafePolicyReader.PolicyRecord policyRec, boolean inner)
/*     */   {
/* 284 */     PolicyRecordHandler handler = null;
/* 285 */     String policyId = policyRec.policyModel.getPolicyId();
/* 286 */     if (policyId == null) {
/* 287 */       policyId = policyRec.policyModel.getPolicyName();
/*     */     }
/* 289 */     if (policyId != null) {
/* 290 */       handler = new PolicyRecordHandler(HandlerType.PolicyUri, policyRec.getUri());
/* 291 */       getPolicyRecordsPassedBy().put(policyRec.getUri(), policyRec);
/* 292 */       policyRecToExpandQueue(policyRec);
/* 293 */     } else if (inner) {
/* 294 */       String anonymousId = this.anonymousPoliciesCount++;
/* 295 */       handler = new PolicyRecordHandler(HandlerType.AnonymousPolicyId, anonymousId);
/* 296 */       getAnonymousPolicyModels().put(anonymousId, policyRec.policyModel);
/* 297 */       if (null != policyRec.unresolvedURIs) {
/* 298 */         getUnresolvedUris(false).addAll(policyRec.unresolvedURIs);
/*     */       }
/*     */     }
/* 301 */     return handler;
/*     */   }
/*     */ 
/*     */   private void addHandlerToMap(Map<WSDLObject, Collection<PolicyRecordHandler>> map, WSDLObject key, PolicyRecordHandler handler)
/*     */   {
/* 307 */     if (map.containsKey(key)) {
/* 308 */       ((Collection)map.get(key)).add(handler);
/*     */     } else {
/* 310 */       Collection newSet = new LinkedList();
/* 311 */       newSet.add(handler);
/* 312 */       map.put(key, newSet);
/*     */     }
/*     */   }
/*     */ 
/*     */   private String getBaseUrl(String policyUri) {
/* 317 */     if (null == policyUri) {
/* 318 */       return null;
/*     */     }
/*     */ 
/* 321 */     int fragmentIdx = policyUri.indexOf('#');
/* 322 */     return fragmentIdx == -1 ? policyUri : policyUri.substring(0, fragmentIdx);
/*     */   }
/*     */ 
/*     */   private void processReferenceUri(String policyUri, WSDLObject element, XMLStreamReader reader, Map<WSDLObject, Collection<PolicyRecordHandler>> map)
/*     */   {
/* 333 */     if ((null == policyUri) || (policyUri.length() == 0)) {
/* 334 */       return;
/*     */     }
/* 336 */     if ('#' != policyUri.charAt(0)) {
/* 337 */       getUnresolvedUris(false).add(policyUri);
/*     */     }
/*     */ 
/* 340 */     addHandlerToMap(map, element, new PolicyRecordHandler(HandlerType.PolicyUri, SafePolicyReader.relativeToAbsoluteUrl(policyUri, reader.getLocation().getSystemId())));
/*     */   }
/*     */ 
/*     */   private boolean processSubelement(WSDLObject element, XMLStreamReader reader, Map<WSDLObject, Collection<PolicyRecordHandler>> map)
/*     */   {
/* 348 */     if (NamespaceVersion.resolveAsToken(reader.getName()) == XmlToken.PolicyReference) {
/* 349 */       processReferenceUri(this.policyReader.readPolicyReferenceElement(reader), element, reader, map);
/* 350 */       return true;
/* 351 */     }if (NamespaceVersion.resolveAsToken(reader.getName()) == XmlToken.Policy) {
/* 352 */       PolicyRecordHandler handler = readSinglePolicy(this.policyReader.readPolicyElement(reader, null == reader.getLocation().getSystemId() ? "" : reader.getLocation().getSystemId()), true);
/*     */ 
/* 359 */       if (null != handler) {
/* 360 */         addHandlerToMap(map, element, handler);
/*     */       }
/* 362 */       return true;
/*     */     }
/* 364 */     return false;
/*     */   }
/*     */ 
/*     */   private void processAttributes(WSDLObject element, XMLStreamReader reader, Map<WSDLObject, Collection<PolicyRecordHandler>> map) {
/* 368 */     String[] uriArray = getPolicyURIsFromAttr(reader);
/* 369 */     if (null != uriArray)
/* 370 */       for (String policyUri : uriArray)
/* 371 */         processReferenceUri(policyUri, element, reader, map);
/*     */   }
/*     */ 
/*     */   public boolean portElements(WSDLPort port, XMLStreamReader reader)
/*     */   {
/* 378 */     LOGGER.entering();
/* 379 */     boolean result = processSubelement(port, reader, getHandlers4PortMap());
/* 380 */     LOGGER.exiting();
/* 381 */     return result;
/*     */   }
/*     */ 
/*     */   public void portAttributes(WSDLPort port, XMLStreamReader reader)
/*     */   {
/* 386 */     LOGGER.entering();
/* 387 */     processAttributes(port, reader, getHandlers4PortMap());
/* 388 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public boolean serviceElements(WSDLService service, XMLStreamReader reader)
/*     */   {
/* 393 */     LOGGER.entering();
/* 394 */     boolean result = processSubelement(service, reader, getHandlers4ServiceMap());
/* 395 */     LOGGER.exiting();
/* 396 */     return result;
/*     */   }
/*     */ 
/*     */   public void serviceAttributes(WSDLService service, XMLStreamReader reader)
/*     */   {
/* 401 */     LOGGER.entering();
/* 402 */     processAttributes(service, reader, getHandlers4ServiceMap());
/* 403 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public boolean definitionsElements(XMLStreamReader reader)
/*     */   {
/* 409 */     LOGGER.entering();
/* 410 */     if (NamespaceVersion.resolveAsToken(reader.getName()) == XmlToken.Policy) {
/* 411 */       readSinglePolicy(this.policyReader.readPolicyElement(reader, null == reader.getLocation().getSystemId() ? "" : reader.getLocation().getSystemId()), false);
/*     */ 
/* 417 */       LOGGER.exiting();
/* 418 */       return true;
/*     */     }
/* 420 */     LOGGER.exiting();
/* 421 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean bindingElements(WSDLBoundPortType binding, XMLStreamReader reader)
/*     */   {
/* 426 */     LOGGER.entering();
/* 427 */     boolean result = processSubelement(binding, reader, getHandlers4BindingMap());
/* 428 */     LOGGER.exiting();
/* 429 */     return result;
/*     */   }
/*     */ 
/*     */   public void bindingAttributes(WSDLBoundPortType binding, XMLStreamReader reader)
/*     */   {
/* 434 */     LOGGER.entering();
/* 435 */     processAttributes(binding, reader, getHandlers4BindingMap());
/* 436 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public boolean portTypeElements(WSDLPortType portType, XMLStreamReader reader)
/*     */   {
/* 441 */     LOGGER.entering();
/* 442 */     boolean result = processSubelement(portType, reader, getHandlers4PortTypeMap());
/* 443 */     LOGGER.exiting();
/* 444 */     return result;
/*     */   }
/*     */ 
/*     */   public void portTypeAttributes(WSDLPortType portType, XMLStreamReader reader)
/*     */   {
/* 449 */     LOGGER.entering();
/* 450 */     processAttributes(portType, reader, getHandlers4PortTypeMap());
/* 451 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationElements(WSDLOperation operation, XMLStreamReader reader)
/*     */   {
/* 456 */     LOGGER.entering();
/* 457 */     boolean result = processSubelement(operation, reader, getHandlers4OperationMap());
/* 458 */     LOGGER.exiting();
/* 459 */     return result;
/*     */   }
/*     */ 
/*     */   public void portTypeOperationAttributes(WSDLOperation operation, XMLStreamReader reader)
/*     */   {
/* 464 */     LOGGER.entering();
/* 465 */     processAttributes(operation, reader, getHandlers4OperationMap());
/* 466 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public boolean bindingOperationElements(WSDLBoundOperation boundOperation, XMLStreamReader reader)
/*     */   {
/* 471 */     LOGGER.entering();
/* 472 */     boolean result = processSubelement(boundOperation, reader, getHandlers4BoundOperationMap());
/* 473 */     LOGGER.exiting();
/* 474 */     return result;
/*     */   }
/*     */ 
/*     */   public void bindingOperationAttributes(WSDLBoundOperation boundOperation, XMLStreamReader reader)
/*     */   {
/* 479 */     LOGGER.entering();
/* 480 */     processAttributes(boundOperation, reader, getHandlers4BoundOperationMap());
/* 481 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public boolean messageElements(WSDLMessage msg, XMLStreamReader reader)
/*     */   {
/* 486 */     LOGGER.entering();
/* 487 */     boolean result = processSubelement(msg, reader, getHandlers4MessageMap());
/* 488 */     LOGGER.exiting();
/* 489 */     return result;
/*     */   }
/*     */ 
/*     */   public void messageAttributes(WSDLMessage msg, XMLStreamReader reader)
/*     */   {
/* 494 */     LOGGER.entering();
/* 495 */     processAttributes(msg, reader, getHandlers4MessageMap());
/* 496 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationInputElements(WSDLInput input, XMLStreamReader reader)
/*     */   {
/* 501 */     LOGGER.entering();
/* 502 */     boolean result = processSubelement(input, reader, getHandlers4InputMap());
/* 503 */     LOGGER.exiting();
/* 504 */     return result;
/*     */   }
/*     */ 
/*     */   public void portTypeOperationInputAttributes(WSDLInput input, XMLStreamReader reader)
/*     */   {
/* 509 */     LOGGER.entering();
/* 510 */     processAttributes(input, reader, getHandlers4InputMap());
/* 511 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationOutputElements(WSDLOutput output, XMLStreamReader reader)
/*     */   {
/* 517 */     LOGGER.entering();
/* 518 */     boolean result = processSubelement(output, reader, getHandlers4OutputMap());
/* 519 */     LOGGER.exiting();
/* 520 */     return result;
/*     */   }
/*     */ 
/*     */   public void portTypeOperationOutputAttributes(WSDLOutput output, XMLStreamReader reader)
/*     */   {
/* 525 */     LOGGER.entering();
/* 526 */     processAttributes(output, reader, getHandlers4OutputMap());
/* 527 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public boolean portTypeOperationFaultElements(WSDLFault fault, XMLStreamReader reader)
/*     */   {
/* 533 */     LOGGER.entering();
/* 534 */     boolean result = processSubelement(fault, reader, getHandlers4FaultMap());
/* 535 */     LOGGER.exiting();
/* 536 */     return result;
/*     */   }
/*     */ 
/*     */   public void portTypeOperationFaultAttributes(WSDLFault fault, XMLStreamReader reader)
/*     */   {
/* 541 */     LOGGER.entering();
/* 542 */     processAttributes(fault, reader, getHandlers4FaultMap());
/* 543 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public boolean bindingOperationInputElements(WSDLBoundOperation operation, XMLStreamReader reader)
/*     */   {
/* 548 */     LOGGER.entering();
/* 549 */     boolean result = processSubelement(operation, reader, getHandlers4BindingInputOpMap());
/* 550 */     LOGGER.exiting();
/* 551 */     return result;
/*     */   }
/*     */ 
/*     */   public void bindingOperationInputAttributes(WSDLBoundOperation operation, XMLStreamReader reader)
/*     */   {
/* 556 */     LOGGER.entering();
/* 557 */     processAttributes(operation, reader, getHandlers4BindingInputOpMap());
/* 558 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public boolean bindingOperationOutputElements(WSDLBoundOperation operation, XMLStreamReader reader)
/*     */   {
/* 564 */     LOGGER.entering();
/* 565 */     boolean result = processSubelement(operation, reader, getHandlers4BindingOutputOpMap());
/* 566 */     LOGGER.exiting();
/* 567 */     return result;
/*     */   }
/*     */ 
/*     */   public void bindingOperationOutputAttributes(WSDLBoundOperation operation, XMLStreamReader reader)
/*     */   {
/* 572 */     LOGGER.entering();
/* 573 */     processAttributes(operation, reader, getHandlers4BindingOutputOpMap());
/* 574 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public boolean bindingOperationFaultElements(WSDLBoundFault fault, XMLStreamReader reader)
/*     */   {
/* 579 */     LOGGER.entering();
/* 580 */     boolean result = processSubelement(fault, reader, getHandlers4BindingFaultOpMap());
/* 581 */     LOGGER.exiting(Boolean.valueOf(result));
/* 582 */     return result;
/*     */   }
/*     */ 
/*     */   public void bindingOperationFaultAttributes(WSDLBoundFault fault, XMLStreamReader reader)
/*     */   {
/* 587 */     LOGGER.entering();
/* 588 */     processAttributes(fault, reader, getHandlers4BindingFaultOpMap());
/* 589 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   private PolicyMapBuilder getPolicyMapBuilder()
/*     */   {
/* 594 */     if (null == this.policyBuilder) {
/* 595 */       this.policyBuilder = new PolicyMapBuilder();
/*     */     }
/* 597 */     return this.policyBuilder;
/*     */   }
/*     */ 
/*     */   private Collection<String> getPolicyURIs(Collection<PolicyRecordHandler> handlers, PolicySourceModelContext modelContext) throws PolicyException
/*     */   {
/* 602 */     Collection result = new ArrayList(handlers.size());
/*     */ 
/* 604 */     for (PolicyRecordHandler handler : handlers) {
/* 605 */       String policyUri = handler.handler;
/* 606 */       if (HandlerType.AnonymousPolicyId == handler.type) {
/* 607 */         PolicySourceModel policyModel = (PolicySourceModel)getAnonymousPolicyModels().get(policyUri);
/* 608 */         policyModel.expand(modelContext);
/* 609 */         while (getPolicyModels().containsKey(policyUri)) {
/* 610 */           policyUri = this.anonymousPoliciesCount++;
/*     */         }
/* 612 */         getPolicyModels().put(policyUri, policyModel);
/*     */       }
/* 614 */       result.add(policyUri);
/*     */     }
/* 616 */     return result;
/*     */   }
/*     */ 
/*     */   private boolean readExternalFile(String fileUrl) {
/* 620 */     InputStream ios = null;
/* 621 */     XMLStreamReader reader = null;
/*     */     try {
/* 623 */       URL xmlURL = new URL(fileUrl);
/* 624 */       ios = xmlURL.openStream();
/* 625 */       reader = XMLInputFactory.newInstance().createXMLStreamReader(ios);
/* 626 */       while (reader.hasNext()) {
/* 627 */         if ((reader.isStartElement()) && (NamespaceVersion.resolveAsToken(reader.getName()) == XmlToken.Policy)) {
/* 628 */           readSinglePolicy(this.policyReader.readPolicyElement(reader, fileUrl), false);
/*     */         }
/* 630 */         reader.next();
/*     */       }
/* 632 */       return true;
/*     */     } catch (IOException ioe) {
/* 634 */       return false;
/*     */     }
/*     */     catch (XMLStreamException xmlse)
/*     */     {
/*     */       boolean bool;
/* 636 */       return false;
/*     */     } finally {
/* 638 */       PolicyUtils.IO.closeResource(reader);
/* 639 */       PolicyUtils.IO.closeResource(ios);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void finished(WSDLParserExtensionContext context)
/*     */   {
/* 645 */     LOGGER.entering(new Object[] { context });
/*     */ 
/* 647 */     if (null != this.expandQueueHead) {
/* 648 */       List externalUris = getUnresolvedUris(false);
/* 649 */       getUnresolvedUris(true);
/* 650 */       LinkedList baseUnresolvedUris = new LinkedList();
/* 651 */       for (SafePolicyReader.PolicyRecord currentRec = this.expandQueueHead; null != currentRec; currentRec = currentRec.next) {
/* 652 */         baseUnresolvedUris.addFirst(currentRec.getUri());
/*     */       }
/* 654 */       getUnresolvedUris(false).addAll(baseUnresolvedUris);
/* 655 */       this.expandQueueHead = null;
/* 656 */       getUnresolvedUris(false).addAll(externalUris);
/*     */     }
/*     */ 
/* 659 */     while (!getUnresolvedUris(false).isEmpty()) {
/* 660 */       List urisToBeSolvedList = getUnresolvedUris(false);
/* 661 */       getUnresolvedUris(true);
/* 662 */       for (String currentUri : urisToBeSolvedList) {
/* 663 */         if (!isPolicyProcessed(currentUri)) {
/* 664 */           SafePolicyReader.PolicyRecord prefetchedRecord = (SafePolicyReader.PolicyRecord)getPolicyRecordsPassedBy().get(currentUri);
/* 665 */           if (null == prefetchedRecord) {
/* 666 */             if (this.policyReader.getUrlsRead().contains(getBaseUrl(currentUri))) {
/* 667 */               LOGGER.logSevereException(new PolicyException(PolicyMessages.WSP_1014_CAN_NOT_FIND_POLICY(currentUri)));
/*     */             }
/* 669 */             else if (readExternalFile(getBaseUrl(currentUri)))
/* 670 */               getUnresolvedUris(false).add(currentUri);
/*     */           }
/*     */           else
/*     */           {
/* 674 */             if (null != prefetchedRecord.unresolvedURIs) {
/* 675 */               getUnresolvedUris(false).addAll(prefetchedRecord.unresolvedURIs);
/*     */             }
/* 677 */             addNewPolicyNeeded(currentUri, prefetchedRecord.policyModel);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 682 */     PolicySourceModelContext modelContext = PolicySourceModelContext.createContext();
/* 683 */     for (String policyUri : this.urisNeeded) {
/* 684 */       PolicySourceModel sourceModel = (PolicySourceModel)this.modelsNeeded.get(policyUri);
/*     */       try {
/* 686 */         sourceModel.expand(modelContext);
/* 687 */         modelContext.addModel(new URI(policyUri), sourceModel);
/*     */       } catch (URISyntaxException e) {
/* 689 */         LOGGER.logSevereException(e);
/*     */       } catch (PolicyException e) {
/* 691 */         LOGGER.logSevereException(e);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 701 */       HashSet messageSet = new HashSet();
/* 702 */       for (Iterator i$ = context.getWSDLModel().getServices().values().iterator(); i$.hasNext(); ) { service = (WSDLService)i$.next();
/* 703 */         if (getHandlers4ServiceMap().containsKey(service)) {
/* 704 */           getPolicyMapBuilder().registerHandler(new BuilderHandlerServiceScope(getPolicyURIs((Collection)getHandlers4ServiceMap().get(service), modelContext), getPolicyModels(), service, service.getName()));
/*     */         }
/*     */ 
/* 712 */         for (i$ = service.getPorts().iterator(); i$.hasNext(); ) { port = (WSDLPort)i$.next();
/* 713 */           if (getHandlers4PortMap().containsKey(port)) {
/* 714 */             getPolicyMapBuilder().registerHandler(new BuilderHandlerEndpointScope(getPolicyURIs((Collection)getHandlers4PortMap().get(port), modelContext), getPolicyModels(), port, port.getOwner().getName(), port.getName()));
/*     */           }
/*     */ 
/* 722 */           if (null != port.getBinding())
/*     */           {
/* 724 */             if (getHandlers4BindingMap().containsKey(port.getBinding()))
/*     */             {
/* 726 */               getPolicyMapBuilder().registerHandler(new BuilderHandlerEndpointScope(getPolicyURIs((Collection)getHandlers4BindingMap().get(port.getBinding()), modelContext), getPolicyModels(), port.getBinding(), service.getName(), port.getName()));
/*     */             }
/*     */ 
/* 735 */             if (getHandlers4PortTypeMap().containsKey(port.getBinding().getPortType()))
/*     */             {
/* 737 */               getPolicyMapBuilder().registerHandler(new BuilderHandlerEndpointScope(getPolicyURIs((Collection)getHandlers4PortTypeMap().get(port.getBinding().getPortType()), modelContext), getPolicyModels(), port.getBinding().getPortType(), service.getName(), port.getName()));
/*     */             }
/*     */ 
/* 748 */             for (i$ = port.getBinding().getBindingOperations().iterator(); i$.hasNext(); ) { boundOperation = (WSDLBoundOperation)i$.next();
/*     */ 
/* 750 */               WSDLOperation operation = boundOperation.getOperation();
/* 751 */               operationName = new QName(boundOperation.getBoundPortType().getName().getNamespaceURI(), boundOperation.getName().getLocalPart());
/*     */ 
/* 753 */               if (getHandlers4BoundOperationMap().containsKey(boundOperation))
/*     */               {
/* 755 */                 getPolicyMapBuilder().registerHandler(new BuilderHandlerOperationScope(getPolicyURIs((Collection)getHandlers4BoundOperationMap().get(boundOperation), modelContext), getPolicyModels(), boundOperation, service.getName(), port.getName(), operationName));
/*     */               }
/*     */ 
/* 765 */               if (getHandlers4OperationMap().containsKey(operation))
/*     */               {
/* 767 */                 getPolicyMapBuilder().registerHandler(new BuilderHandlerOperationScope(getPolicyURIs((Collection)getHandlers4OperationMap().get(operation), modelContext), getPolicyModels(), operation, service.getName(), port.getName(), operationName));
/*     */               }
/*     */ 
/* 779 */               WSDLInput input = operation.getInput();
/* 780 */               if (null != input) {
/* 781 */                 WSDLMessage inputMsg = input.getMessage();
/* 782 */                 if ((inputMsg != null) && (getHandlers4MessageMap().containsKey(inputMsg))) {
/* 783 */                   messageSet.add(new BuilderHandlerMessageScope(getPolicyURIs((Collection)getHandlers4MessageMap().get(inputMsg), modelContext), getPolicyModels(), inputMsg, BuilderHandlerMessageScope.Scope.InputMessageScope, service.getName(), port.getName(), operationName, null));
/*     */                 }
/*     */ 
/*     */               }
/*     */ 
/* 796 */               if (getHandlers4BindingInputOpMap().containsKey(boundOperation))
/*     */               {
/* 798 */                 getPolicyMapBuilder().registerHandler(new BuilderHandlerMessageScope(getPolicyURIs((Collection)getHandlers4BindingInputOpMap().get(boundOperation), modelContext), getPolicyModels(), boundOperation, BuilderHandlerMessageScope.Scope.InputMessageScope, service.getName(), port.getName(), operationName, null));
/*     */               }
/*     */ 
/* 810 */               if ((null != input) && (getHandlers4InputMap().containsKey(input)))
/*     */               {
/* 812 */                 getPolicyMapBuilder().registerHandler(new BuilderHandlerMessageScope(getPolicyURIs((Collection)getHandlers4InputMap().get(input), modelContext), getPolicyModels(), input, BuilderHandlerMessageScope.Scope.InputMessageScope, service.getName(), port.getName(), operationName, null));
/*     */               }
/*     */ 
/* 826 */               WSDLOutput output = operation.getOutput();
/* 827 */               if (null != output) {
/* 828 */                 WSDLMessage outputMsg = output.getMessage();
/* 829 */                 if ((outputMsg != null) && (getHandlers4MessageMap().containsKey(outputMsg))) {
/* 830 */                   messageSet.add(new BuilderHandlerMessageScope(getPolicyURIs((Collection)getHandlers4MessageMap().get(outputMsg), modelContext), getPolicyModels(), outputMsg, BuilderHandlerMessageScope.Scope.OutputMessageScope, service.getName(), port.getName(), operationName, null));
/*     */                 }
/*     */ 
/*     */               }
/*     */ 
/* 843 */               if (getHandlers4BindingOutputOpMap().containsKey(boundOperation))
/*     */               {
/* 845 */                 getPolicyMapBuilder().registerHandler(new BuilderHandlerMessageScope(getPolicyURIs((Collection)getHandlers4BindingOutputOpMap().get(boundOperation), modelContext), getPolicyModels(), boundOperation, BuilderHandlerMessageScope.Scope.OutputMessageScope, service.getName(), port.getName(), operationName, null));
/*     */               }
/*     */ 
/* 857 */               if ((null != output) && (getHandlers4OutputMap().containsKey(output)))
/*     */               {
/* 859 */                 getPolicyMapBuilder().registerHandler(new BuilderHandlerMessageScope(getPolicyURIs((Collection)getHandlers4OutputMap().get(output), modelContext), getPolicyModels(), output, BuilderHandlerMessageScope.Scope.OutputMessageScope, service.getName(), port.getName(), operationName, null));
/*     */               }
/*     */ 
/* 873 */               for (WSDLBoundFault boundFault : boundOperation.getFaults()) {
/* 874 */                 WSDLFault fault = boundFault.getFault();
/* 875 */                 WSDLMessage faultMessage = fault.getMessage();
/* 876 */                 QName faultName = new QName(boundOperation.getBoundPortType().getName().getNamespaceURI(), boundFault.getName());
/*     */ 
/* 878 */                 if ((faultMessage != null) && (getHandlers4MessageMap().containsKey(faultMessage))) {
/* 879 */                   messageSet.add(new BuilderHandlerMessageScope(getPolicyURIs((Collection)getHandlers4MessageMap().get(faultMessage), modelContext), getPolicyModels(), new WSDLBoundFaultContainer(boundFault, boundOperation), BuilderHandlerMessageScope.Scope.FaultMessageScope, service.getName(), port.getName(), operationName, faultName));
/*     */                 }
/*     */ 
/* 891 */                 if (getHandlers4FaultMap().containsKey(fault)) {
/* 892 */                   messageSet.add(new BuilderHandlerMessageScope(getPolicyURIs((Collection)getHandlers4FaultMap().get(fault), modelContext), getPolicyModels(), new WSDLBoundFaultContainer(boundFault, boundOperation), BuilderHandlerMessageScope.Scope.FaultMessageScope, service.getName(), port.getName(), operationName, faultName));
/*     */                 }
/*     */ 
/* 904 */                 if (getHandlers4BindingFaultOpMap().containsKey(boundFault))
/* 905 */                   messageSet.add(new BuilderHandlerMessageScope(getPolicyURIs((Collection)getHandlers4BindingFaultOpMap().get(boundFault), modelContext), getPolicyModels(), new WSDLBoundFaultContainer(boundFault, boundOperation), BuilderHandlerMessageScope.Scope.FaultMessageScope, service.getName(), port.getName(), operationName, faultName));
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       WSDLService service;
/*     */       Iterator i$;
/*     */       WSDLPort port;
/*     */       Iterator i$;
/*     */       WSDLBoundOperation boundOperation;
/*     */       QName operationName;
/* 925 */       for (BuilderHandlerMessageScope scopeHandler : messageSet)
/* 926 */         getPolicyMapBuilder().registerHandler(scopeHandler);
/*     */     }
/*     */     catch (PolicyException e) {
/* 929 */       LOGGER.logSevereException(e);
/*     */     }
/*     */ 
/* 933 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public void postFinished(WSDLParserExtensionContext context)
/*     */   {
/* 941 */     WSDLModel wsdlModel = context.getWSDLModel();
/*     */     PolicyMap effectiveMap;
/*     */     try
/*     */     {
/*     */       PolicyMap effectiveMap;
/* 944 */       if (context.isClientSide())
/* 945 */         effectiveMap = context.getPolicyResolver().resolve(new PolicyResolver.ClientContext(this.policyBuilder.getPolicyMap(new PolicyMapMutator[0]), context.getContainer()));
/*     */       else
/* 947 */         effectiveMap = context.getPolicyResolver().resolve(new PolicyResolver.ServerContext(this.policyBuilder.getPolicyMap(new PolicyMapMutator[0]), context.getContainer(), null, new PolicyMapMutator[0]));
/* 948 */       ((WSDLModelImpl)wsdlModel).setPolicyMap(effectiveMap);
/*     */     } catch (PolicyException e) {
/* 950 */       LOGGER.logSevereException(e);
/* 951 */       throw ((WebServiceException)LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1007_POLICY_EXCEPTION_WHILE_FINISHING_PARSING_WSDL(), e)));
/*     */     }
/*     */     try {
/* 954 */       PolicyUtil.configureModel(wsdlModel, effectiveMap);
/*     */     } catch (PolicyException e) {
/* 956 */       LOGGER.logSevereException(e);
/* 957 */       throw ((WebServiceException)LOGGER.logSevereException(new WebServiceException(PolicyMessages.WSP_1012_FAILED_CONFIGURE_WSDL_MODEL(), e)));
/*     */     }
/* 959 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   private String[] getPolicyURIsFromAttr(XMLStreamReader reader)
/*     */   {
/* 971 */     StringBuilder policyUriBuffer = new StringBuilder();
/* 972 */     for (NamespaceVersion version : NamespaceVersion.values()) {
/* 973 */       String value = reader.getAttributeValue(version.toString(), XmlToken.PolicyUris.toString());
/* 974 */       if (value != null) {
/* 975 */         policyUriBuffer.append(value).append(" ");
/*     */       }
/*     */     }
/* 978 */     return policyUriBuffer.length() > 0 ? policyUriBuffer.toString().split("[\\n ]+") : null;
/*     */   }
/*     */ 
/*     */   static enum HandlerType
/*     */   {
/*  75 */     PolicyUri, AnonymousPolicyId;
/*     */   }
/*     */   static final class PolicyRecordHandler {
/*     */     String handler;
/*     */     PolicyWSDLParserExtension.HandlerType type;
/*     */ 
/*     */     PolicyRecordHandler(PolicyWSDLParserExtension.HandlerType type, String handler) {
/*  83 */       this.type = type;
/*  84 */       this.handler = handler;
/*     */     }
/*     */ 
/*     */     PolicyWSDLParserExtension.HandlerType getType() {
/*  88 */       return this.type;
/*     */     }
/*     */ 
/*     */     String getHandler() {
/*  92 */       return this.handler;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.jaxws.PolicyWSDLParserExtension
 * JD-Core Version:    0.6.2
 */