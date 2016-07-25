/*     */ package com.sun.xml.internal.ws.policy;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public final class PolicyMap
/*     */   implements Iterable<Policy>
/*     */ {
/*  53 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(PolicyMap.class);
/*     */ 
/*  55 */   private static final PolicyMapKeyHandler serviceKeyHandler = new PolicyMapKeyHandler() {
/*     */     public boolean areEqual(PolicyMapKey key1, PolicyMapKey key2) {
/*  57 */       return key1.getService().equals(key2.getService());
/*     */     }
/*     */ 
/*     */     public int generateHashCode(PolicyMapKey key) {
/*  61 */       int result = 17;
/*     */ 
/*  63 */       result = 37 * result + key.getService().hashCode();
/*     */ 
/*  65 */       return result;
/*     */     }
/*  55 */   };
/*     */ 
/*  69 */   private static final PolicyMapKeyHandler endpointKeyHandler = new PolicyMapKeyHandler() {
/*     */     public boolean areEqual(PolicyMapKey key1, PolicyMapKey key2) {
/*  71 */       boolean retVal = true;
/*     */ 
/*  73 */       retVal = (retVal) && (key1.getService().equals(key2.getService()));
/*  74 */       retVal = (retVal) && (key1.getPort() == null ? key2.getPort() == null : key1.getPort().equals(key2.getPort()));
/*     */ 
/*  76 */       return retVal;
/*     */     }
/*     */ 
/*     */     public int generateHashCode(PolicyMapKey key) {
/*  80 */       int result = 17;
/*     */ 
/*  82 */       result = 37 * result + key.getService().hashCode();
/*  83 */       result = 37 * result + (key.getPort() == null ? 0 : key.getPort().hashCode());
/*     */ 
/*  85 */       return result;
/*     */     }
/*  69 */   };
/*     */ 
/*  89 */   private static final PolicyMapKeyHandler operationAndInputOutputMessageKeyHandler = new PolicyMapKeyHandler()
/*     */   {
/*     */     public boolean areEqual(PolicyMapKey key1, PolicyMapKey key2)
/*     */     {
/*  93 */       boolean retVal = true;
/*     */ 
/*  95 */       retVal = (retVal) && (key1.getService().equals(key2.getService()));
/*  96 */       retVal = (retVal) && (key1.getPort() == null ? key2.getPort() == null : key1.getPort().equals(key2.getPort()));
/*  97 */       retVal = (retVal) && (key1.getOperation() == null ? key2.getOperation() == null : key1.getOperation().equals(key2.getOperation()));
/*     */ 
/*  99 */       return retVal;
/*     */     }
/*     */ 
/*     */     public int generateHashCode(PolicyMapKey key) {
/* 103 */       int result = 17;
/*     */ 
/* 105 */       result = 37 * result + key.getService().hashCode();
/* 106 */       result = 37 * result + (key.getPort() == null ? 0 : key.getPort().hashCode());
/* 107 */       result = 37 * result + (key.getOperation() == null ? 0 : key.getOperation().hashCode());
/*     */ 
/* 109 */       return result;
/*     */     }
/*  89 */   };
/*     */ 
/* 113 */   private static final PolicyMapKeyHandler faultMessageHandler = new PolicyMapKeyHandler() {
/*     */     public boolean areEqual(PolicyMapKey key1, PolicyMapKey key2) {
/* 115 */       boolean retVal = true;
/*     */ 
/* 117 */       retVal = (retVal) && (key1.getService().equals(key2.getService()));
/* 118 */       retVal = (retVal) && (key1.getPort() == null ? key2.getPort() == null : key1.getPort().equals(key2.getPort()));
/* 119 */       retVal = (retVal) && (key1.getOperation() == null ? key2.getOperation() == null : key1.getOperation().equals(key2.getOperation()));
/* 120 */       retVal = (retVal) && (key1.getFaultMessage() == null ? key2.getFaultMessage() == null : key1.getFaultMessage().equals(key2.getFaultMessage()));
/*     */ 
/* 122 */       return retVal;
/*     */     }
/*     */ 
/*     */     public int generateHashCode(PolicyMapKey key) {
/* 126 */       int result = 17;
/*     */ 
/* 128 */       result = 37 * result + key.getService().hashCode();
/* 129 */       result = 37 * result + (key.getPort() == null ? 0 : key.getPort().hashCode());
/* 130 */       result = 37 * result + (key.getOperation() == null ? 0 : key.getOperation().hashCode());
/* 131 */       result = 37 * result + (key.getFaultMessage() == null ? 0 : key.getFaultMessage().hashCode());
/*     */ 
/* 133 */       return result;
/*     */     }
/* 113 */   };
/*     */ 
/* 243 */   private static final PolicyMerger merger = PolicyMerger.getMerger();
/*     */ 
/* 245 */   private final ScopeMap serviceMap = new ScopeMap(merger, serviceKeyHandler);
/* 246 */   private final ScopeMap endpointMap = new ScopeMap(merger, endpointKeyHandler);
/* 247 */   private final ScopeMap operationMap = new ScopeMap(merger, operationAndInputOutputMessageKeyHandler);
/* 248 */   private final ScopeMap inputMessageMap = new ScopeMap(merger, operationAndInputOutputMessageKeyHandler);
/* 249 */   private final ScopeMap outputMessageMap = new ScopeMap(merger, operationAndInputOutputMessageKeyHandler);
/* 250 */   private final ScopeMap faultMessageMap = new ScopeMap(merger, faultMessageHandler);
/*     */ 
/*     */   public static PolicyMap createPolicyMap(Collection<? extends PolicyMapMutator> mutators)
/*     */   {
/* 266 */     PolicyMap result = new PolicyMap();
/*     */ 
/* 268 */     if ((mutators != null) && (!mutators.isEmpty())) {
/* 269 */       for (PolicyMapMutator mutator : mutators) {
/* 270 */         mutator.connect(result);
/*     */       }
/*     */     }
/*     */ 
/* 274 */     return result;
/*     */   }
/*     */ 
/*     */   public Policy getServiceEffectivePolicy(PolicyMapKey key) throws PolicyException {
/* 278 */     return this.serviceMap.getEffectivePolicy(key);
/*     */   }
/*     */ 
/*     */   public Policy getEndpointEffectivePolicy(PolicyMapKey key) throws PolicyException {
/* 282 */     return this.endpointMap.getEffectivePolicy(key);
/*     */   }
/*     */ 
/*     */   public Policy getOperationEffectivePolicy(PolicyMapKey key) throws PolicyException {
/* 286 */     return this.operationMap.getEffectivePolicy(key);
/*     */   }
/*     */ 
/*     */   public Policy getInputMessageEffectivePolicy(PolicyMapKey key) throws PolicyException {
/* 290 */     return this.inputMessageMap.getEffectivePolicy(key);
/*     */   }
/*     */ 
/*     */   public Policy getOutputMessageEffectivePolicy(PolicyMapKey key) throws PolicyException {
/* 294 */     return this.outputMessageMap.getEffectivePolicy(key);
/*     */   }
/*     */ 
/*     */   public Policy getFaultMessageEffectivePolicy(PolicyMapKey key) throws PolicyException {
/* 298 */     return this.faultMessageMap.getEffectivePolicy(key);
/*     */   }
/*     */ 
/*     */   public Collection<PolicyMapKey> getAllServiceScopeKeys()
/*     */   {
/* 307 */     return this.serviceMap.getAllKeys();
/*     */   }
/*     */ 
/*     */   public Collection<PolicyMapKey> getAllEndpointScopeKeys()
/*     */   {
/* 316 */     return this.endpointMap.getAllKeys();
/*     */   }
/*     */ 
/*     */   public Collection<PolicyMapKey> getAllOperationScopeKeys()
/*     */   {
/* 325 */     return this.operationMap.getAllKeys();
/*     */   }
/*     */ 
/*     */   public Collection<PolicyMapKey> getAllInputMessageScopeKeys()
/*     */   {
/* 334 */     return this.inputMessageMap.getAllKeys();
/*     */   }
/*     */ 
/*     */   public Collection<PolicyMapKey> getAllOutputMessageScopeKeys()
/*     */   {
/* 343 */     return this.outputMessageMap.getAllKeys();
/*     */   }
/*     */ 
/*     */   public Collection<PolicyMapKey> getAllFaultMessageScopeKeys()
/*     */   {
/* 352 */     return this.faultMessageMap.getAllKeys();
/*     */   }
/*     */ 
/*     */   void putSubject(ScopeType scopeType, PolicyMapKey key, PolicySubject subject)
/*     */   {
/* 365 */     switch (6.$SwitchMap$com$sun$xml$internal$ws$policy$PolicyMap$ScopeType[scopeType.ordinal()]) {
/*     */     case 1:
/* 367 */       this.serviceMap.putSubject(key, subject);
/* 368 */       break;
/*     */     case 2:
/* 370 */       this.endpointMap.putSubject(key, subject);
/* 371 */       break;
/*     */     case 3:
/* 373 */       this.operationMap.putSubject(key, subject);
/* 374 */       break;
/*     */     case 4:
/* 376 */       this.inputMessageMap.putSubject(key, subject);
/* 377 */       break;
/*     */     case 5:
/* 379 */       this.outputMessageMap.putSubject(key, subject);
/* 380 */       break;
/*     */     case 6:
/* 382 */       this.faultMessageMap.putSubject(key, subject);
/* 383 */       break;
/*     */     default:
/* 385 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0002_UNRECOGNIZED_SCOPE_TYPE(scopeType))));
/*     */     }
/*     */   }
/*     */ 
/*     */   void setNewEffectivePolicyForScope(ScopeType scopeType, PolicyMapKey key, Policy newEffectivePolicy)
/*     */     throws IllegalArgumentException
/*     */   {
/* 402 */     if ((scopeType == null) || (key == null) || (newEffectivePolicy == null)) {
/* 403 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0062_INPUT_PARAMS_MUST_NOT_BE_NULL())));
/*     */     }
/*     */ 
/* 406 */     switch (6.$SwitchMap$com$sun$xml$internal$ws$policy$PolicyMap$ScopeType[scopeType.ordinal()]) {
/*     */     case 1:
/* 408 */       this.serviceMap.setNewEffectivePolicy(key, newEffectivePolicy);
/* 409 */       break;
/*     */     case 2:
/* 411 */       this.endpointMap.setNewEffectivePolicy(key, newEffectivePolicy);
/* 412 */       break;
/*     */     case 3:
/* 414 */       this.operationMap.setNewEffectivePolicy(key, newEffectivePolicy);
/* 415 */       break;
/*     */     case 4:
/* 417 */       this.inputMessageMap.setNewEffectivePolicy(key, newEffectivePolicy);
/* 418 */       break;
/*     */     case 5:
/* 420 */       this.outputMessageMap.setNewEffectivePolicy(key, newEffectivePolicy);
/* 421 */       break;
/*     */     case 6:
/* 423 */       this.faultMessageMap.setNewEffectivePolicy(key, newEffectivePolicy);
/* 424 */       break;
/*     */     default:
/* 426 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0002_UNRECOGNIZED_SCOPE_TYPE(scopeType))));
/*     */     }
/*     */   }
/*     */ 
/*     */   public Collection<PolicySubject> getPolicySubjects()
/*     */   {
/* 436 */     List subjects = new LinkedList();
/* 437 */     addSubjects(subjects, this.serviceMap);
/* 438 */     addSubjects(subjects, this.endpointMap);
/* 439 */     addSubjects(subjects, this.operationMap);
/* 440 */     addSubjects(subjects, this.inputMessageMap);
/* 441 */     addSubjects(subjects, this.outputMessageMap);
/* 442 */     addSubjects(subjects, this.faultMessageMap);
/* 443 */     return subjects;
/*     */   }
/*     */ 
/*     */   public boolean isInputMessageSubject(PolicySubject subject)
/*     */   {
/* 450 */     for (PolicyScope scope : this.inputMessageMap.getStoredScopes()) {
/* 451 */       if (scope.getPolicySubjects().contains(subject)) {
/* 452 */         return true;
/*     */       }
/*     */     }
/* 455 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isOutputMessageSubject(PolicySubject subject)
/*     */   {
/* 462 */     for (PolicyScope scope : this.outputMessageMap.getStoredScopes()) {
/* 463 */       if (scope.getPolicySubjects().contains(subject)) {
/* 464 */         return true;
/*     */       }
/*     */     }
/* 467 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isFaultMessageSubject(PolicySubject subject)
/*     */   {
/* 475 */     for (PolicyScope scope : this.faultMessageMap.getStoredScopes()) {
/* 476 */       if (scope.getPolicySubjects().contains(subject)) {
/* 477 */         return true;
/*     */       }
/*     */     }
/* 480 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 492 */     return (this.serviceMap.isEmpty()) && (this.endpointMap.isEmpty()) && (this.operationMap.isEmpty()) && (this.inputMessageMap.isEmpty()) && (this.outputMessageMap.isEmpty()) && (this.faultMessageMap.isEmpty());
/*     */   }
/*     */ 
/*     */   private void addSubjects(Collection<PolicySubject> subjects, ScopeMap scopeMap)
/*     */   {
/* 505 */     for (PolicyScope scope : scopeMap.getStoredScopes()) {
/* 506 */       Collection scopedSubjects = scope.getPolicySubjects();
/* 507 */       subjects.addAll(scopedSubjects);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static PolicyMapKey createWsdlServiceScopeKey(QName service)
/*     */     throws IllegalArgumentException
/*     */   {
/* 519 */     if (service == null) {
/* 520 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0031_SERVICE_PARAM_MUST_NOT_BE_NULL())));
/*     */     }
/* 522 */     return new PolicyMapKey(service, null, null, serviceKeyHandler);
/*     */   }
/*     */ 
/*     */   public static PolicyMapKey createWsdlEndpointScopeKey(QName service, QName port)
/*     */     throws IllegalArgumentException
/*     */   {
/* 534 */     if ((service == null) || (port == null)) {
/* 535 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0033_SERVICE_AND_PORT_PARAM_MUST_NOT_BE_NULL(service, port))));
/*     */     }
/* 537 */     return new PolicyMapKey(service, port, null, endpointKeyHandler);
/*     */   }
/*     */ 
/*     */   public static PolicyMapKey createWsdlOperationScopeKey(QName service, QName port, QName operation)
/*     */     throws IllegalArgumentException
/*     */   {
/* 550 */     return createOperationOrInputOutputMessageKey(service, port, operation);
/*     */   }
/*     */ 
/*     */   public static PolicyMapKey createWsdlMessageScopeKey(QName service, QName port, QName operation)
/*     */     throws IllegalArgumentException
/*     */   {
/* 568 */     return createOperationOrInputOutputMessageKey(service, port, operation);
/*     */   }
/*     */ 
/*     */   public static PolicyMapKey createWsdlFaultMessageScopeKey(QName service, QName port, QName operation, QName fault)
/*     */     throws IllegalArgumentException
/*     */   {
/* 588 */     if ((service == null) || (port == null) || (operation == null) || (fault == null)) {
/* 589 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0030_SERVICE_PORT_OPERATION_FAULT_MSG_PARAM_MUST_NOT_BE_NULL(service, port, operation, fault))));
/*     */     }
/*     */ 
/* 592 */     return new PolicyMapKey(service, port, operation, fault, faultMessageHandler);
/*     */   }
/*     */ 
/*     */   private static PolicyMapKey createOperationOrInputOutputMessageKey(QName service, QName port, QName operation) {
/* 596 */     if ((service == null) || (port == null) || (operation == null)) {
/* 597 */       throw ((IllegalArgumentException)LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0029_SERVICE_PORT_OPERATION_PARAM_MUST_NOT_BE_NULL(service, port, operation))));
/*     */     }
/*     */ 
/* 600 */     return new PolicyMapKey(service, port, operation, operationAndInputOutputMessageKeyHandler);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 606 */     StringBuffer result = new StringBuffer();
/* 607 */     if (null != this.serviceMap) {
/* 608 */       result.append("\nServiceMap=").append(this.serviceMap);
/*     */     }
/* 610 */     if (null != this.endpointMap) {
/* 611 */       result.append("\nEndpointMap=").append(this.endpointMap);
/*     */     }
/* 613 */     if (null != this.operationMap) {
/* 614 */       result.append("\nOperationMap=").append(this.operationMap);
/*     */     }
/* 616 */     if (null != this.inputMessageMap) {
/* 617 */       result.append("\nInputMessageMap=").append(this.inputMessageMap);
/*     */     }
/* 619 */     if (null != this.outputMessageMap) {
/* 620 */       result.append("\nOutputMessageMap=").append(this.outputMessageMap);
/*     */     }
/* 622 */     if (null != this.faultMessageMap) {
/* 623 */       result.append("\nFaultMessageMap=").append(this.faultMessageMap);
/*     */     }
/* 625 */     return result.toString();
/*     */   }
/*     */ 
/*     */   public Iterator<Policy> iterator() {
/* 629 */     return new Iterator()
/*     */     {
/*     */       private final Iterator<Iterator<Policy>> mainIterator;
/*     */       private Iterator<Policy> currentScopeIterator;
/*     */ 
/*     */       public boolean hasNext()
/*     */       {
/* 647 */         while (!this.currentScopeIterator.hasNext()) {
/* 648 */           if (this.mainIterator.hasNext())
/* 649 */             this.currentScopeIterator = ((Iterator)this.mainIterator.next());
/*     */           else {
/* 651 */             return false;
/*     */           }
/*     */         }
/*     */ 
/* 655 */         return true;
/*     */       }
/*     */ 
/*     */       public Policy next() {
/* 659 */         if (hasNext()) {
/* 660 */           return (Policy)this.currentScopeIterator.next();
/*     */         }
/* 662 */         throw ((NoSuchElementException)PolicyMap.LOGGER.logSevereException(new NoSuchElementException(LocalizationMessages.WSP_0054_NO_MORE_ELEMS_IN_POLICY_MAP())));
/*     */       }
/*     */ 
/*     */       public void remove() {
/* 666 */         throw ((UnsupportedOperationException)PolicyMap.LOGGER.logSevereException(new UnsupportedOperationException(LocalizationMessages.WSP_0034_REMOVE_OPERATION_NOT_SUPPORTED())));
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   private static final class ScopeMap
/*     */     implements Iterable<Policy>
/*     */   {
/* 148 */     private final Map<PolicyMapKey, PolicyScope> internalMap = new HashMap();
/*     */     private final PolicyMapKeyHandler scopeKeyHandler;
/*     */     private final PolicyMerger merger;
/*     */ 
/*     */     ScopeMap(PolicyMerger merger, PolicyMapKeyHandler scopeKeyHandler)
/*     */     {
/* 153 */       this.merger = merger;
/* 154 */       this.scopeKeyHandler = scopeKeyHandler;
/*     */     }
/*     */ 
/*     */     Policy getEffectivePolicy(PolicyMapKey key) throws PolicyException {
/* 158 */       PolicyScope scope = (PolicyScope)this.internalMap.get(createLocalCopy(key));
/* 159 */       return scope == null ? null : scope.getEffectivePolicy(this.merger);
/*     */     }
/*     */ 
/*     */     void putSubject(PolicyMapKey key, PolicySubject subject) {
/* 163 */       PolicyMapKey localKey = createLocalCopy(key);
/* 164 */       PolicyScope scope = (PolicyScope)this.internalMap.get(localKey);
/* 165 */       if (scope == null) {
/* 166 */         List list = new LinkedList();
/* 167 */         list.add(subject);
/* 168 */         this.internalMap.put(localKey, new PolicyScope(list));
/*     */       } else {
/* 170 */         scope.attach(subject);
/*     */       }
/*     */     }
/*     */ 
/*     */     void setNewEffectivePolicy(PolicyMapKey key, Policy newEffectivePolicy)
/*     */     {
/* 177 */       PolicySubject subject = new PolicySubject(key, newEffectivePolicy);
/*     */ 
/* 179 */       PolicyMapKey localKey = createLocalCopy(key);
/* 180 */       PolicyScope scope = (PolicyScope)this.internalMap.get(localKey);
/* 181 */       if (scope == null) {
/* 182 */         List list = new LinkedList();
/* 183 */         list.add(subject);
/* 184 */         this.internalMap.put(localKey, new PolicyScope(list));
/*     */       } else {
/* 186 */         scope.dettachAllSubjects();
/* 187 */         scope.attach(subject);
/*     */       }
/*     */     }
/*     */ 
/*     */     Collection<PolicyScope> getStoredScopes() {
/* 192 */       return this.internalMap.values();
/*     */     }
/*     */ 
/*     */     Set<PolicyMapKey> getAllKeys() {
/* 196 */       return this.internalMap.keySet();
/*     */     }
/*     */ 
/*     */     private PolicyMapKey createLocalCopy(PolicyMapKey key) {
/* 200 */       if (key == null) {
/* 201 */         throw ((IllegalArgumentException)PolicyMap.LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0045_POLICY_MAP_KEY_MUST_NOT_BE_NULL())));
/*     */       }
/*     */ 
/* 204 */       PolicyMapKey localKeyCopy = new PolicyMapKey(key);
/* 205 */       localKeyCopy.setHandler(this.scopeKeyHandler);
/*     */ 
/* 207 */       return localKeyCopy;
/*     */     }
/*     */ 
/*     */     public Iterator<Policy> iterator() {
/* 211 */       return new Iterator() {
/* 212 */         private final Iterator<PolicyMapKey> keysIterator = PolicyMap.ScopeMap.this.internalMap.keySet().iterator();
/*     */ 
/*     */         public boolean hasNext() {
/* 215 */           return this.keysIterator.hasNext();
/*     */         }
/*     */ 
/*     */         public Policy next() {
/* 219 */           PolicyMapKey key = (PolicyMapKey)this.keysIterator.next();
/*     */           try {
/* 221 */             return PolicyMap.ScopeMap.this.getEffectivePolicy(key);
/*     */           } catch (PolicyException e) {
/* 223 */             throw ((IllegalStateException)PolicyMap.LOGGER.logSevereException(new IllegalStateException(LocalizationMessages.WSP_0069_EXCEPTION_WHILE_RETRIEVING_EFFECTIVE_POLICY_FOR_KEY(key), e)));
/*     */           }
/*     */         }
/*     */ 
/*     */         public void remove() {
/* 228 */           throw ((UnsupportedOperationException)PolicyMap.LOGGER.logSevereException(new UnsupportedOperationException(LocalizationMessages.WSP_0034_REMOVE_OPERATION_NOT_SUPPORTED())));
/*     */         }
/*     */       };
/*     */     }
/*     */ 
/*     */     public boolean isEmpty() {
/* 234 */       return this.internalMap.isEmpty();
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 239 */       return this.internalMap.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   static enum ScopeType
/*     */   {
/* 139 */     SERVICE, 
/* 140 */     ENDPOINT, 
/* 141 */     OPERATION, 
/* 142 */     INPUT_MESSAGE, 
/* 143 */     OUTPUT_MESSAGE, 
/* 144 */     FAULT_MESSAGE;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.PolicyMap
 * JD-Core Version:    0.6.2
 */