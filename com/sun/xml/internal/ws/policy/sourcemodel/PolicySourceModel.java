/*     */ package com.sun.xml.internal.ws.policy.sourcemodel;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.PolicyException;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.ServiceProvider;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
/*     */ import com.sun.xml.internal.ws.policy.spi.PrefixMapper;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Queue;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public class PolicySourceModel
/*     */   implements Cloneable
/*     */ {
/*  56 */   private static final PolicyLogger LOGGER = PolicyLogger.getLogger(PolicySourceModel.class);
/*     */ 
/*  58 */   private static final Map<String, String> DEFAULT_NAMESPACE_TO_PREFIX = new HashMap();
/*     */ 
/*  77 */   private final Map<String, String> namespaceToPrefix = new HashMap(DEFAULT_NAMESPACE_TO_PREFIX);
/*     */   private ModelNode rootNode;
/*     */   private final String policyId;
/*     */   private final String policyName;
/*     */   private final NamespaceVersion nsVersion;
/*  84 */   private final List<ModelNode> references = new LinkedList();
/*  85 */   private boolean expanded = false;
/*     */ 
/*     */   public static PolicySourceModel createPolicySourceModel(NamespaceVersion nsVersion)
/*     */   {
/*  98 */     return new PolicySourceModel(nsVersion);
/*     */   }
/*     */ 
/*     */   public static PolicySourceModel createPolicySourceModel(NamespaceVersion nsVersion, String policyId, String policyName)
/*     */   {
/* 113 */     return new PolicySourceModel(nsVersion, policyId, policyName);
/*     */   }
/*     */ 
/*     */   private PolicySourceModel(NamespaceVersion nsVersion)
/*     */   {
/* 125 */     this(nsVersion, null, null);
/*     */   }
/*     */ 
/*     */   private PolicySourceModel(NamespaceVersion nsVersion, String policyId, String policyName)
/*     */   {
/* 137 */     this(nsVersion, policyId, policyName, null);
/*     */   }
/*     */ 
/*     */   protected PolicySourceModel(NamespaceVersion nsVersion, String policyId, String policyName, Collection<PrefixMapper> prefixMappers)
/*     */   {
/* 153 */     this.rootNode = ModelNode.createRootPolicyNode(this);
/* 154 */     this.nsVersion = nsVersion;
/* 155 */     this.policyId = policyId;
/* 156 */     this.policyName = policyName;
/* 157 */     if (prefixMappers != null)
/* 158 */       for (PrefixMapper prefixMapper : prefixMappers)
/* 159 */         this.namespaceToPrefix.putAll(prefixMapper.getPrefixMap());
/*     */   }
/*     */ 
/*     */   public ModelNode getRootNode()
/*     */   {
/* 170 */     return this.rootNode;
/*     */   }
/*     */ 
/*     */   public String getPolicyName()
/*     */   {
/* 179 */     return this.policyName;
/*     */   }
/*     */ 
/*     */   public String getPolicyId()
/*     */   {
/* 188 */     return this.policyId;
/*     */   }
/*     */ 
/*     */   public NamespaceVersion getNamespaceVersion()
/*     */   {
/* 197 */     return this.nsVersion;
/*     */   }
/*     */ 
/*     */   Map<String, String> getNamespaceToPrefixMapping()
/*     */     throws PolicyException
/*     */   {
/* 210 */     Map nsToPrefixMap = new HashMap();
/*     */ 
/* 212 */     Collection namespaces = getUsedNamespaces();
/* 213 */     for (String namespace : namespaces) {
/* 214 */       String prefix = getDefaultPrefix(namespace);
/* 215 */       if (prefix != null) {
/* 216 */         nsToPrefixMap.put(namespace, prefix);
/*     */       }
/*     */     }
/*     */ 
/* 220 */     return nsToPrefixMap;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 231 */     if (this == obj) {
/* 232 */       return true;
/*     */     }
/*     */ 
/* 235 */     if (!(obj instanceof PolicySourceModel)) {
/* 236 */       return false;
/*     */     }
/*     */ 
/* 239 */     boolean result = true;
/* 240 */     PolicySourceModel that = (PolicySourceModel)obj;
/*     */ 
/* 242 */     result = (result) && (this.policyId == null ? that.policyId == null : this.policyId.equals(that.policyId));
/* 243 */     result = (result) && (this.policyName == null ? that.policyName == null : this.policyName.equals(that.policyName));
/* 244 */     result = (result) && (this.rootNode.equals(that.rootNode));
/*     */ 
/* 246 */     return result;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 254 */     int result = 17;
/*     */ 
/* 256 */     result = 37 * result + (this.policyId == null ? 0 : this.policyId.hashCode());
/* 257 */     result = 37 * result + (this.policyName == null ? 0 : this.policyName.hashCode());
/* 258 */     result = 37 * result + this.rootNode.hashCode();
/*     */ 
/* 260 */     return result;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 271 */     String innerIndent = PolicyUtils.Text.createIndent(1);
/* 272 */     StringBuffer buffer = new StringBuffer(60);
/*     */ 
/* 274 */     buffer.append("Policy source model {").append(PolicyUtils.Text.NEW_LINE);
/* 275 */     buffer.append(innerIndent).append("policy id = '").append(this.policyId).append('\'').append(PolicyUtils.Text.NEW_LINE);
/* 276 */     buffer.append(innerIndent).append("policy name = '").append(this.policyName).append('\'').append(PolicyUtils.Text.NEW_LINE);
/* 277 */     this.rootNode.toString(1, buffer).append(PolicyUtils.Text.NEW_LINE).append('}');
/*     */ 
/* 279 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   protected PolicySourceModel clone() throws CloneNotSupportedException
/*     */   {
/* 284 */     PolicySourceModel clone = (PolicySourceModel)super.clone();
/*     */ 
/* 286 */     clone.rootNode = this.rootNode.clone();
/*     */     try {
/* 288 */       clone.rootNode.setParentModel(clone);
/*     */     } catch (IllegalAccessException e) {
/* 290 */       throw ((CloneNotSupportedException)LOGGER.logSevereException(new CloneNotSupportedException(LocalizationMessages.WSP_0013_UNABLE_TO_SET_PARENT_MODEL_ON_ROOT()), e));
/*     */     }
/*     */ 
/* 293 */     return clone;
/*     */   }
/*     */ 
/*     */   public boolean containsPolicyReferences()
/*     */   {
/* 305 */     return !this.references.isEmpty();
/*     */   }
/*     */ 
/*     */   private boolean isExpanded()
/*     */   {
/* 322 */     return (this.references.isEmpty()) || (this.expanded);
/*     */   }
/*     */ 
/*     */   public synchronized void expand(PolicySourceModelContext context)
/*     */     throws PolicyException
/*     */   {
/* 340 */     if (!isExpanded()) {
/* 341 */       for (ModelNode reference : this.references) {
/* 342 */         PolicyReferenceData refData = reference.getPolicyReferenceData();
/* 343 */         String digest = refData.getDigest();
/*     */         PolicySourceModel referencedModel;
/*     */         PolicySourceModel referencedModel;
/* 345 */         if (digest == null)
/* 346 */           referencedModel = context.retrieveModel(refData.getReferencedModelUri());
/*     */         else {
/* 348 */           referencedModel = context.retrieveModel(refData.getReferencedModelUri(), refData.getDigestAlgorithmUri(), digest);
/*     */         }
/*     */ 
/* 351 */         reference.setReferencedModel(referencedModel);
/*     */       }
/* 353 */       this.expanded = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   void addNewPolicyReference(ModelNode node)
/*     */   {
/* 366 */     if (node.getType() != ModelNode.Type.POLICY_REFERENCE) {
/* 367 */       throw new IllegalArgumentException(LocalizationMessages.WSP_0042_POLICY_REFERENCE_NODE_EXPECTED_INSTEAD_OF(node.getType()));
/*     */     }
/*     */ 
/* 370 */     this.references.add(node);
/*     */   }
/*     */ 
/*     */   private Collection<String> getUsedNamespaces()
/*     */     throws PolicyException
/*     */   {
/* 381 */     Set namespaces = new HashSet();
/* 382 */     namespaces.add(getNamespaceVersion().toString());
/*     */ 
/* 384 */     if (this.policyId != null) {
/* 385 */       namespaces.add("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd");
/*     */     }
/*     */ 
/* 388 */     Queue nodesToBeProcessed = new LinkedList();
/* 389 */     nodesToBeProcessed.add(this.rootNode);
/*     */     ModelNode processedNode;
/* 392 */     while ((processedNode = (ModelNode)nodesToBeProcessed.poll()) != null) {
/* 393 */       for (ModelNode child : processedNode.getChildren()) {
/* 394 */         if ((child.hasChildren()) && 
/* 395 */           (!nodesToBeProcessed.offer(child))) {
/* 396 */           throw ((PolicyException)LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0081_UNABLE_TO_INSERT_CHILD(nodesToBeProcessed, child))));
/*     */         }
/*     */ 
/* 400 */         if (child.isDomainSpecific()) {
/* 401 */           AssertionData nodeData = child.getNodeData();
/* 402 */           namespaces.add(nodeData.getName().getNamespaceURI());
/* 403 */           if (nodeData.isPrivateAttributeSet()) {
/* 404 */             namespaces.add("http://java.sun.com/xml/ns/wsit/policy");
/*     */           }
/*     */ 
/* 407 */           for (Map.Entry attribute : nodeData.getAttributesSet()) {
/* 408 */             namespaces.add(((QName)attribute.getKey()).getNamespaceURI());
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 414 */     return namespaces;
/*     */   }
/*     */ 
/*     */   private String getDefaultPrefix(String namespace)
/*     */   {
/* 426 */     return (String)this.namespaceToPrefix.get(namespace);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  60 */     PrefixMapper[] prefixMappers = (PrefixMapper[])PolicyUtils.ServiceProvider.load(PrefixMapper.class);
/*  61 */     if (prefixMappers != null) {
/*  62 */       for (PrefixMapper mapper : prefixMappers) {
/*  63 */         DEFAULT_NAMESPACE_TO_PREFIX.putAll(mapper.getPrefixMap());
/*     */       }
/*     */     }
/*     */ 
/*  67 */     for (NamespaceVersion version : NamespaceVersion.values()) {
/*  68 */       DEFAULT_NAMESPACE_TO_PREFIX.put(version.toString(), version.getDefaultNamespacePrefix());
/*     */     }
/*  70 */     DEFAULT_NAMESPACE_TO_PREFIX.put("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd", "wsu");
/*     */ 
/*  72 */     DEFAULT_NAMESPACE_TO_PREFIX.put("http://java.sun.com/xml/ns/wsit/policy", "sunwsp");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModel
 * JD-Core Version:    0.6.2
 */