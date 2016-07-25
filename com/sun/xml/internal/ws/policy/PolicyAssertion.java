/*     */ package com.sun.xml.internal.ws.policy;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.AssertionData;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.ModelNode.Type;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public abstract class PolicyAssertion
/*     */ {
/*     */   private final AssertionData data;
/*     */   private AssertionSet parameters;
/*     */   private NestedPolicy nestedPolicy;
/*     */ 
/*     */   protected PolicyAssertion()
/*     */   {
/*  55 */     this.data = AssertionData.createAssertionData(null);
/*  56 */     this.parameters = AssertionSet.createAssertionSet(null);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected PolicyAssertion(AssertionData assertionData, Collection<? extends PolicyAssertion> assertionParameters, AssertionSet nestedAlternative)
/*     */   {
/*  73 */     this.data = assertionData;
/*  74 */     if (nestedAlternative != null) {
/*  75 */       this.nestedPolicy = NestedPolicy.createNestedPolicy(nestedAlternative);
/*     */     }
/*     */ 
/*  78 */     this.parameters = AssertionSet.createAssertionSet(assertionParameters);
/*     */   }
/*     */ 
/*     */   protected PolicyAssertion(AssertionData assertionData, Collection<? extends PolicyAssertion> assertionParameters)
/*     */   {
/*  88 */     if (assertionData == null)
/*  89 */       this.data = AssertionData.createAssertionData(null);
/*     */     else {
/*  91 */       this.data = assertionData;
/*     */     }
/*  93 */     this.parameters = AssertionSet.createAssertionSet(assertionParameters);
/*     */   }
/*     */ 
/*     */   public final QName getName()
/*     */   {
/* 102 */     return this.data.getName();
/*     */   }
/*     */ 
/*     */   public final String getValue()
/*     */   {
/* 111 */     return this.data.getValue();
/*     */   }
/*     */ 
/*     */   public boolean isOptional()
/*     */   {
/* 123 */     return this.data.isOptionalAttributeSet();
/*     */   }
/*     */ 
/*     */   public boolean isIgnorable()
/*     */   {
/* 135 */     return this.data.isIgnorableAttributeSet();
/*     */   }
/*     */ 
/*     */   public final boolean isPrivate()
/*     */   {
/* 144 */     return this.data.isPrivateAttributeSet();
/*     */   }
/*     */ 
/*     */   public final Set<Map.Entry<QName, String>> getAttributesSet()
/*     */   {
/* 158 */     return this.data.getAttributesSet();
/*     */   }
/*     */ 
/*     */   public final Map<QName, String> getAttributes()
/*     */   {
/* 171 */     return this.data.getAttributes();
/*     */   }
/*     */ 
/*     */   public final String getAttributeValue(QName name)
/*     */   {
/* 181 */     return this.data.getAttributeValue(name);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final boolean hasNestedAssertions()
/*     */   {
/* 194 */     return !this.parameters.isEmpty();
/*     */   }
/*     */ 
/*     */   public final boolean hasParameters()
/*     */   {
/* 203 */     return !this.parameters.isEmpty();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public final Iterator<PolicyAssertion> getNestedAssertionsIterator()
/*     */   {
/* 216 */     return this.parameters.iterator();
/*     */   }
/*     */ 
/*     */   public final Iterator<PolicyAssertion> getParametersIterator()
/*     */   {
/* 225 */     return this.parameters.iterator();
/*     */   }
/*     */ 
/*     */   boolean isParameter() {
/* 229 */     return this.data.getNodeType() == ModelNode.Type.ASSERTION_PARAMETER_NODE;
/*     */   }
/*     */ 
/*     */   public boolean hasNestedPolicy()
/*     */   {
/* 239 */     return getNestedPolicy() != null;
/*     */   }
/*     */ 
/*     */   public NestedPolicy getNestedPolicy()
/*     */   {
/* 249 */     return this.nestedPolicy;
/*     */   }
/*     */ 
/*     */   public <T extends PolicyAssertion> T getImplementation(Class<T> type)
/*     */   {
/* 261 */     if (type.isAssignableFrom(getClass())) {
/* 262 */       return (PolicyAssertion)type.cast(this);
/*     */     }
/*     */ 
/* 265 */     return null;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 274 */     return toString(0, new StringBuffer()).toString();
/*     */   }
/*     */ 
/*     */   protected StringBuffer toString(int indentLevel, StringBuffer buffer)
/*     */   {
/* 285 */     String indent = PolicyUtils.Text.createIndent(indentLevel);
/* 286 */     String innerIndent = PolicyUtils.Text.createIndent(indentLevel + 1);
/*     */ 
/* 288 */     buffer.append(indent).append("Assertion[").append(getClass().getName()).append("] {").append(PolicyUtils.Text.NEW_LINE);
/* 289 */     this.data.toString(indentLevel + 1, buffer);
/* 290 */     buffer.append(PolicyUtils.Text.NEW_LINE);
/*     */ 
/* 292 */     if (hasParameters()) {
/* 293 */       buffer.append(innerIndent).append("parameters {").append(PolicyUtils.Text.NEW_LINE);
/* 294 */       for (PolicyAssertion parameter : this.parameters) {
/* 295 */         parameter.toString(indentLevel + 2, buffer).append(PolicyUtils.Text.NEW_LINE);
/*     */       }
/* 297 */       buffer.append(innerIndent).append('}').append(PolicyUtils.Text.NEW_LINE);
/*     */     } else {
/* 299 */       buffer.append(innerIndent).append("no parameters").append(PolicyUtils.Text.NEW_LINE);
/*     */     }
/*     */ 
/* 302 */     if (hasNestedPolicy())
/* 303 */       getNestedPolicy().toString(indentLevel + 1, buffer).append(PolicyUtils.Text.NEW_LINE);
/*     */     else {
/* 305 */       buffer.append(innerIndent).append("no nested policy").append(PolicyUtils.Text.NEW_LINE);
/*     */     }
/*     */ 
/* 308 */     buffer.append(indent).append('}');
/*     */ 
/* 310 */     return buffer;
/*     */   }
/*     */ 
/*     */   boolean isCompatibleWith(PolicyAssertion assertion, PolicyIntersector.CompatibilityMode mode)
/*     */   {
/* 321 */     boolean result = (this.data.getName().equals(assertion.data.getName())) && (hasNestedPolicy() == assertion.hasNestedPolicy());
/*     */ 
/* 323 */     if ((result) && (hasNestedPolicy())) {
/* 324 */       result = getNestedPolicy().getAssertionSet().isCompatibleWith(assertion.getNestedPolicy().getAssertionSet(), mode);
/*     */     }
/*     */ 
/* 327 */     return result;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 335 */     if (this == obj) {
/* 336 */       return true;
/*     */     }
/*     */ 
/* 339 */     if (!(obj instanceof PolicyAssertion)) {
/* 340 */       return false;
/*     */     }
/*     */ 
/* 343 */     PolicyAssertion that = (PolicyAssertion)obj;
/* 344 */     boolean result = true;
/*     */ 
/* 346 */     result = (result) && (this.data.equals(that.data));
/* 347 */     result = (result) && (this.parameters.equals(that.parameters));
/* 348 */     result = (result) && (getNestedPolicy() == null ? that.getNestedPolicy() == null : getNestedPolicy().equals(that.getNestedPolicy()));
/*     */ 
/* 350 */     return result;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 358 */     int result = 17;
/*     */ 
/* 360 */     result = 37 * result + this.data.hashCode();
/* 361 */     result = 37 * result + (hasParameters() ? 17 : 0);
/* 362 */     result = 37 * result + (hasNestedPolicy() ? 17 : 0);
/*     */ 
/* 364 */     return result;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.PolicyAssertion
 * JD-Core Version:    0.6.2
 */