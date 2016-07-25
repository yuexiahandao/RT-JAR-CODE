/*     */ package com.sun.xml.internal.ws.policy.sourcemodel;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.AssertionSet;
/*     */ import com.sun.xml.internal.ws.policy.Policy;
/*     */ import com.sun.xml.internal.ws.policy.PolicyAssertion;
/*     */ import com.sun.xml.internal.ws.policy.PolicyException;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Collections;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.ServiceProvider;
/*     */ import com.sun.xml.internal.ws.policy.spi.AssertionCreationException;
/*     */ import com.sun.xml.internal.ws.policy.spi.PolicyAssertionCreator;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Queue;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public class PolicyModelTranslator
/*     */ {
/* 129 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(PolicyModelTranslator.class);
/*     */ 
/* 131 */   private static final PolicyAssertionCreator defaultCreator = new DefaultPolicyAssertionCreator();
/*     */   private final Map<String, PolicyAssertionCreator> assertionCreators;
/*     */ 
/*     */   private PolicyModelTranslator()
/*     */     throws PolicyException
/*     */   {
/* 137 */     this(null);
/*     */   }
/*     */ 
/*     */   protected PolicyModelTranslator(Collection<PolicyAssertionCreator> creators) throws PolicyException {
/* 141 */     LOGGER.entering(new Object[] { creators });
/*     */ 
/* 143 */     Collection allCreators = new LinkedList();
/* 144 */     PolicyAssertionCreator[] discoveredCreators = (PolicyAssertionCreator[])PolicyUtils.ServiceProvider.load(PolicyAssertionCreator.class);
/* 145 */     for (PolicyAssertionCreator creator : discoveredCreators) {
/* 146 */       allCreators.add(creator);
/*     */     }
/* 148 */     if (creators != null) {
/* 149 */       for (PolicyAssertionCreator creator : creators) {
/* 150 */         allCreators.add(creator);
/*     */       }
/*     */     }
/*     */ 
/* 154 */     Map pacMap = new HashMap();
/* 155 */     for (PolicyAssertionCreator creator : allCreators) {
/* 156 */       String[] supportedURIs = creator.getSupportedDomainNamespaceURIs();
/* 157 */       String creatorClassName = creator.getClass().getName();
/*     */ 
/* 159 */       if ((supportedURIs == null) || (supportedURIs.length == 0)) {
/* 160 */         LOGGER.warning(LocalizationMessages.WSP_0077_ASSERTION_CREATOR_DOES_NOT_SUPPORT_ANY_URI(creatorClassName));
/*     */       }
/*     */       else
/*     */       {
/* 164 */         for (String supportedURI : supportedURIs) {
/* 165 */           LOGGER.config(LocalizationMessages.WSP_0078_ASSERTION_CREATOR_DISCOVERED(creatorClassName, supportedURI));
/* 166 */           if ((supportedURI == null) || (supportedURI.length() == 0)) {
/* 167 */             throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0070_ERROR_REGISTERING_ASSERTION_CREATOR(creatorClassName))));
/*     */           }
/*     */ 
/* 171 */           PolicyAssertionCreator oldCreator = (PolicyAssertionCreator)pacMap.put(supportedURI, creator);
/* 172 */           if (oldCreator != null) {
/* 173 */             throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0071_ERROR_MULTIPLE_ASSERTION_CREATORS_FOR_NAMESPACE(supportedURI, oldCreator.getClass().getName(), creator.getClass().getName()))));
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 180 */     this.assertionCreators = Collections.unmodifiableMap(pacMap);
/* 181 */     LOGGER.exiting();
/*     */   }
/*     */ 
/*     */   public static PolicyModelTranslator getTranslator()
/*     */     throws PolicyException
/*     */   {
/* 194 */     return new PolicyModelTranslator();
/*     */   }
/*     */ 
/*     */   public Policy translate(PolicySourceModel model)
/*     */     throws PolicyException
/*     */   {
/* 206 */     LOGGER.entering(new Object[] { model });
/*     */ 
/* 208 */     if (model == null) {
/* 209 */       throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0043_POLICY_MODEL_TRANSLATION_ERROR_INPUT_PARAM_NULL())));
/*     */     }
/*     */     PolicySourceModel localPolicyModelCopy;
/*     */     try
/*     */     {
/* 214 */       localPolicyModelCopy = model.clone();
/*     */     } catch (CloneNotSupportedException e) {
/* 216 */       throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0016_UNABLE_TO_CLONE_POLICY_SOURCE_MODEL(), e)));
/*     */     }
/*     */ 
/* 219 */     String policyId = localPolicyModelCopy.getPolicyId();
/* 220 */     String policyName = localPolicyModelCopy.getPolicyName();
/*     */ 
/* 222 */     Collection alternatives = createPolicyAlternatives(localPolicyModelCopy);
/* 223 */     LOGGER.finest(LocalizationMessages.WSP_0052_NUMBER_OF_ALTERNATIVE_COMBINATIONS_CREATED(Integer.valueOf(alternatives.size())));
/*     */ 
/* 225 */     Policy policy = null;
/* 226 */     if (alternatives.size() == 0) {
/* 227 */       policy = Policy.createNullPolicy(model.getNamespaceVersion(), policyName, policyId);
/* 228 */       LOGGER.finest(LocalizationMessages.WSP_0055_NO_ALTERNATIVE_COMBINATIONS_CREATED());
/* 229 */     } else if ((alternatives.size() == 1) && (((AssertionSet)alternatives.iterator().next()).isEmpty())) {
/* 230 */       policy = Policy.createEmptyPolicy(model.getNamespaceVersion(), policyName, policyId);
/* 231 */       LOGGER.finest(LocalizationMessages.WSP_0026_SINGLE_EMPTY_ALTERNATIVE_COMBINATION_CREATED());
/*     */     } else {
/* 233 */       policy = Policy.createPolicy(model.getNamespaceVersion(), policyName, policyId, alternatives);
/* 234 */       LOGGER.finest(LocalizationMessages.WSP_0057_N_ALTERNATIVE_COMBINATIONS_M_POLICY_ALTERNATIVES_CREATED(Integer.valueOf(alternatives.size()), Integer.valueOf(policy.getNumberOfAssertionSets())));
/*     */     }
/*     */ 
/* 237 */     LOGGER.exiting(policy);
/* 238 */     return policy;
/*     */   }
/*     */ 
/*     */   private Collection<AssertionSet> createPolicyAlternatives(PolicySourceModel model)
/*     */     throws PolicyException
/*     */   {
/* 248 */     ContentDecomposition decomposition = new ContentDecomposition(null);
/*     */ 
/* 251 */     Queue policyQueue = new LinkedList();
/* 252 */     Queue contentQueue = new LinkedList();
/*     */ 
/* 254 */     RawPolicy rootPolicy = new RawPolicy(model.getRootNode(), new LinkedList());
/* 255 */     RawPolicy processedPolicy = rootPolicy;
/*     */     do {
/* 257 */       Collection processedContent = processedPolicy.originalContent;
/*     */       do {
/* 259 */         decompose(processedContent, decomposition);
/* 260 */         if (decomposition.exactlyOneContents.isEmpty()) {
/* 261 */           RawAlternative alternative = new RawAlternative(decomposition.assertions);
/* 262 */           processedPolicy.alternatives.add(alternative);
/* 263 */           if (!alternative.allNestedPolicies.isEmpty())
/* 264 */             policyQueue.addAll(alternative.allNestedPolicies);
/*     */         }
/*     */         else {
/* 267 */           Collection combinations = PolicyUtils.Collections.combine(decomposition.assertions, decomposition.exactlyOneContents, false);
/* 268 */           if ((combinations != null) && (!combinations.isEmpty()))
/*     */           {
/* 270 */             contentQueue.addAll(combinations);
/*     */           }
/*     */         }
/*     */       }
/* 273 */       while ((processedContent = (Collection)contentQueue.poll()) != null);
/* 274 */     }while ((processedPolicy = (RawPolicy)policyQueue.poll()) != null);
/*     */ 
/* 277 */     Collection assertionSets = new LinkedList();
/* 278 */     for (RawAlternative rootAlternative : rootPolicy.alternatives) {
/* 279 */       Collection normalizedAlternatives = normalizeRawAlternative(rootAlternative);
/* 280 */       assertionSets.addAll(normalizedAlternatives);
/*     */     }
/*     */ 
/* 283 */     return assertionSets;
/*     */   }
/*     */ 
/*     */   private void decompose(Collection<ModelNode> content, ContentDecomposition decomposition)
/*     */     throws PolicyException
/*     */   {
/* 295 */     decomposition.reset();
/*     */ 
/* 297 */     Queue allContentQueue = new LinkedList(content);
/*     */     ModelNode node;
/* 299 */     while ((node = (ModelNode)allContentQueue.poll()) != null)
/*     */     {
/* 301 */       switch (1.$SwitchMap$com$sun$xml$internal$ws$policy$sourcemodel$ModelNode$Type[node.getType().ordinal()]) {
/*     */       case 2:
/*     */       case 4:
/* 304 */         allContentQueue.addAll(node.getChildren());
/* 305 */         break;
/*     */       case 3:
/* 307 */         allContentQueue.addAll(getReferencedModelRootNode(node).getChildren());
/* 308 */         break;
/*     */       case 5:
/* 310 */         decomposition.exactlyOneContents.add(expandsExactlyOneContent(node.getChildren()));
/* 311 */         break;
/*     */       case 6:
/* 313 */         decomposition.assertions.add(node);
/* 314 */         break;
/*     */       default:
/* 316 */         throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0007_UNEXPECTED_MODEL_NODE_TYPE_FOUND(node.getType()))));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static ModelNode getReferencedModelRootNode(ModelNode policyReferenceNode) throws PolicyException {
/* 322 */     PolicySourceModel referencedModel = policyReferenceNode.getReferencedModel();
/* 323 */     if (referencedModel == null) {
/* 324 */       PolicyReferenceData refData = policyReferenceNode.getPolicyReferenceData();
/* 325 */       if (refData == null) {
/* 326 */         throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0041_POLICY_REFERENCE_NODE_FOUND_WITH_NO_POLICY_REFERENCE_IN_IT())));
/*     */       }
/* 328 */       throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0010_UNEXPANDED_POLICY_REFERENCE_NODE_FOUND_REFERENCING(refData.getReferencedModelUri()))));
/*     */     }
/*     */ 
/* 331 */     return referencedModel.getRootNode();
/*     */   }
/*     */ 
/*     */   private Collection<ModelNode> expandsExactlyOneContent(Collection<ModelNode> content)
/*     */     throws PolicyException
/*     */   {
/* 339 */     Collection result = new LinkedList();
/*     */ 
/* 341 */     Queue eoContentQueue = new LinkedList(content);
/*     */     ModelNode node;
/* 343 */     while ((node = (ModelNode)eoContentQueue.poll()) != null)
/*     */     {
/* 345 */       switch (1.$SwitchMap$com$sun$xml$internal$ws$policy$sourcemodel$ModelNode$Type[node.getType().ordinal()]) {
/*     */       case 2:
/*     */       case 4:
/*     */       case 6:
/* 349 */         result.add(node);
/* 350 */         break;
/*     */       case 3:
/* 352 */         result.add(getReferencedModelRootNode(node));
/* 353 */         break;
/*     */       case 5:
/* 355 */         eoContentQueue.addAll(node.getChildren());
/* 356 */         break;
/*     */       default:
/* 358 */         throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0001_UNSUPPORTED_MODEL_NODE_TYPE(node.getType()))));
/*     */       }
/*     */     }
/*     */ 
/* 362 */     return result;
/*     */   }
/*     */ 
/*     */   private List<AssertionSet> normalizeRawAlternative(RawAlternative alternative) throws AssertionCreationException, PolicyException {
/* 366 */     List normalizedContentBase = new LinkedList();
/* 367 */     Collection normalizedContentOptions = new LinkedList();
/* 368 */     if (!alternative.nestedAssertions.isEmpty()) {
/* 369 */       Queue nestedAssertionsQueue = new LinkedList(alternative.nestedAssertions);
/*     */       RawAssertion rawAssertion;
/* 371 */       while ((rawAssertion = (RawAssertion)nestedAssertionsQueue.poll()) != null) {
/* 372 */         List normalized = normalizeRawAssertion(rawAssertion);
/*     */ 
/* 375 */         if (normalized.size() == 1)
/* 376 */           normalizedContentBase.addAll(normalized);
/*     */         else {
/* 378 */           normalizedContentOptions.add(normalized);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 383 */     List options = new LinkedList();
/* 384 */     if (normalizedContentOptions.isEmpty())
/*     */     {
/* 386 */       options.add(AssertionSet.createAssertionSet(normalizedContentBase));
/*     */     }
/*     */     else {
/* 389 */       Collection contentCombinations = PolicyUtils.Collections.combine(normalizedContentBase, normalizedContentOptions, true);
/* 390 */       for (Collection contentOption : contentCombinations) {
/* 391 */         options.add(AssertionSet.createAssertionSet(contentOption));
/*     */       }
/*     */     }
/* 394 */     return options;
/*     */   }
/*     */ 
/*     */   private List<PolicyAssertion> normalizeRawAssertion(RawAssertion assertion)
/*     */     throws AssertionCreationException, PolicyException
/*     */   {
/*     */     List parameters;
/*     */     List parameters;
/* 399 */     if (assertion.parameters.isEmpty()) {
/* 400 */       parameters = null;
/*     */     } else {
/* 402 */       parameters = new ArrayList(assertion.parameters.size());
/* 403 */       for (ModelNode parameterNode : assertion.parameters) {
/* 404 */         parameters.add(createPolicyAssertionParameter(parameterNode));
/*     */       }
/*     */     }
/*     */ 
/* 408 */     List nestedAlternatives = new LinkedList();
/* 409 */     if ((assertion.nestedAlternatives != null) && (!assertion.nestedAlternatives.isEmpty())) {
/* 410 */       Queue nestedAlternativeQueue = new LinkedList(assertion.nestedAlternatives);
/*     */       RawAlternative rawAlternative;
/* 412 */       while ((rawAlternative = (RawAlternative)nestedAlternativeQueue.poll()) != null) {
/* 413 */         nestedAlternatives.addAll(normalizeRawAlternative(rawAlternative));
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 419 */     List assertionOptions = new LinkedList();
/* 420 */     boolean nestedAlternativesAvailable = !nestedAlternatives.isEmpty();
/* 421 */     if (nestedAlternativesAvailable) {
/* 422 */       for (AssertionSet nestedAlternative : nestedAlternatives)
/* 423 */         assertionOptions.add(createPolicyAssertion(assertion.originalNode.getNodeData(), parameters, nestedAlternative));
/*     */     }
/*     */     else {
/* 426 */       assertionOptions.add(createPolicyAssertion(assertion.originalNode.getNodeData(), parameters, null));
/*     */     }
/* 428 */     return assertionOptions;
/*     */   }
/*     */ 
/*     */   private PolicyAssertion createPolicyAssertionParameter(ModelNode parameterNode) throws AssertionCreationException, PolicyException {
/* 432 */     if (parameterNode.getType() != ModelNode.Type.ASSERTION_PARAMETER_NODE) {
/* 433 */       throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0065_INCONSISTENCY_IN_POLICY_SOURCE_MODEL(parameterNode.getType()))));
/*     */     }
/*     */ 
/* 436 */     List childParameters = null;
/* 437 */     if (parameterNode.hasChildren()) {
/* 438 */       childParameters = new ArrayList(parameterNode.childrenSize());
/* 439 */       for (ModelNode childParameterNode : parameterNode) {
/* 440 */         childParameters.add(createPolicyAssertionParameter(childParameterNode));
/*     */       }
/*     */     }
/*     */ 
/* 444 */     return createPolicyAssertion(parameterNode.getNodeData(), childParameters, null);
/*     */   }
/*     */ 
/*     */   private PolicyAssertion createPolicyAssertion(AssertionData data, Collection<PolicyAssertion> assertionParameters, AssertionSet nestedAlternative) throws AssertionCreationException {
/* 448 */     String assertionNamespace = data.getName().getNamespaceURI();
/* 449 */     PolicyAssertionCreator domainSpecificPAC = (PolicyAssertionCreator)this.assertionCreators.get(assertionNamespace);
/*     */ 
/* 452 */     if (domainSpecificPAC == null) {
/* 453 */       return defaultCreator.createAssertion(data, assertionParameters, nestedAlternative, null);
/*     */     }
/* 455 */     return domainSpecificPAC.createAssertion(data, assertionParameters, nestedAlternative, defaultCreator);
/*     */   }
/*     */ 
/*     */   private static final class ContentDecomposition
/*     */   {
/*  58 */     final List<Collection<ModelNode>> exactlyOneContents = new LinkedList();
/*  59 */     final List<ModelNode> assertions = new LinkedList();
/*     */ 
/*     */     void reset() {
/*  62 */       this.exactlyOneContents.clear();
/*  63 */       this.assertions.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class RawAlternative
/*     */   {
/*  79 */     private static final PolicyLogger LOGGER = PolicyLogger.getLogger(RawAlternative.class);
/*     */ 
/*  81 */     final List<PolicyModelTranslator.RawPolicy> allNestedPolicies = new LinkedList();
/*     */     final Collection<PolicyModelTranslator.RawAssertion> nestedAssertions;
/*     */ 
/*     */     RawAlternative(Collection<ModelNode> assertionNodes)
/*     */       throws PolicyException
/*     */     {
/*  85 */       this.nestedAssertions = new LinkedList();
/*  86 */       for (ModelNode node : assertionNodes) {
/*  87 */         assertion = new PolicyModelTranslator.RawAssertion(node, new LinkedList());
/*  88 */         this.nestedAssertions.add(assertion);
/*     */ 
/*  90 */         for (ModelNode assertionNodeChild : assertion.originalNode.getChildren())
/*  91 */           switch (PolicyModelTranslator.1.$SwitchMap$com$sun$xml$internal$ws$policy$sourcemodel$ModelNode$Type[assertionNodeChild.getType().ordinal()]) {
/*     */           case 1:
/*  93 */             assertion.parameters.add(assertionNodeChild);
/*  94 */             break;
/*     */           case 2:
/*     */           case 3:
/*  97 */             if (assertion.nestedAlternatives == null) {
/*  98 */               assertion.nestedAlternatives = new LinkedList();
/*     */               PolicyModelTranslator.RawPolicy nestedPolicy;
/*     */               PolicyModelTranslator.RawPolicy nestedPolicy;
/* 100 */               if (assertionNodeChild.getType() == ModelNode.Type.POLICY)
/* 101 */                 nestedPolicy = new PolicyModelTranslator.RawPolicy(assertionNodeChild, assertion.nestedAlternatives);
/*     */               else {
/* 103 */                 nestedPolicy = new PolicyModelTranslator.RawPolicy(PolicyModelTranslator.getReferencedModelRootNode(assertionNodeChild), assertion.nestedAlternatives);
/*     */               }
/* 105 */               this.allNestedPolicies.add(nestedPolicy);
/*     */             } else {
/* 107 */               throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0006_UNEXPECTED_MULTIPLE_POLICY_NODES())));
/*     */             }
/*     */             break;
/*     */           default:
/* 111 */             throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0008_UNEXPECTED_CHILD_MODEL_TYPE(assertionNodeChild.getType()))));
/*     */           }
/*     */       }
/*     */       PolicyModelTranslator.RawAssertion assertion;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class RawAssertion
/*     */   {
/*     */     ModelNode originalNode;
/*  69 */     Collection<PolicyModelTranslator.RawAlternative> nestedAlternatives = null;
/*     */     final Collection<ModelNode> parameters;
/*     */ 
/*     */     RawAssertion(ModelNode originalNode, Collection<ModelNode> parameters)
/*     */     {
/*  73 */       this.parameters = parameters;
/*  74 */       this.originalNode = originalNode;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class RawPolicy
/*     */   {
/*     */     final Collection<ModelNode> originalContent;
/*     */     final Collection<PolicyModelTranslator.RawAlternative> alternatives;
/*     */ 
/*     */     RawPolicy(ModelNode policyNode, Collection<PolicyModelTranslator.RawAlternative> alternatives)
/*     */     {
/* 124 */       this.originalContent = policyNode.getChildren();
/* 125 */       this.alternatives = alternatives;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelTranslator
 * JD-Core Version:    0.6.2
 */