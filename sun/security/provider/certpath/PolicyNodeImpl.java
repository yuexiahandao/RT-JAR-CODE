/*     */ package sun.security.provider.certpath;
/*     */ 
/*     */ import java.security.cert.PolicyNode;
/*     */ import java.security.cert.PolicyQualifierInfo;
/*     */ import java.util.Collections;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.Set;
/*     */ 
/*     */ final class PolicyNodeImpl
/*     */   implements PolicyNode
/*     */ {
/*     */   private static final String ANY_POLICY = "2.5.29.32.0";
/*     */   private PolicyNodeImpl mParent;
/*     */   private HashSet<PolicyNodeImpl> mChildren;
/*     */   private String mValidPolicy;
/*     */   private HashSet<PolicyQualifierInfo> mQualifierSet;
/*     */   private boolean mCriticalityIndicator;
/*     */   private HashSet<String> mExpectedPolicySet;
/*     */   private boolean mOriginalExpectedPolicySet;
/*     */   private int mDepth;
/*  69 */   private boolean isImmutable = false;
/*     */ 
/*     */   PolicyNodeImpl(PolicyNodeImpl paramPolicyNodeImpl, String paramString, Set<PolicyQualifierInfo> paramSet, boolean paramBoolean1, Set<String> paramSet1, boolean paramBoolean2)
/*     */   {
/*  93 */     this.mParent = paramPolicyNodeImpl;
/*  94 */     this.mChildren = new HashSet();
/*     */ 
/*  96 */     if (paramString != null)
/*  97 */       this.mValidPolicy = paramString;
/*     */     else {
/*  99 */       this.mValidPolicy = "";
/*     */     }
/* 101 */     if (paramSet != null)
/* 102 */       this.mQualifierSet = new HashSet(paramSet);
/*     */     else {
/* 104 */       this.mQualifierSet = new HashSet();
/*     */     }
/* 106 */     this.mCriticalityIndicator = paramBoolean1;
/*     */ 
/* 108 */     if (paramSet1 != null)
/* 109 */       this.mExpectedPolicySet = new HashSet(paramSet1);
/*     */     else {
/* 111 */       this.mExpectedPolicySet = new HashSet();
/*     */     }
/* 113 */     this.mOriginalExpectedPolicySet = (!paramBoolean2);
/*     */ 
/* 116 */     if (this.mParent != null) {
/* 117 */       this.mDepth = (this.mParent.getDepth() + 1);
/* 118 */       this.mParent.addChild(this);
/*     */     } else {
/* 120 */       this.mDepth = 0;
/*     */     }
/*     */   }
/*     */ 
/*     */   PolicyNodeImpl(PolicyNodeImpl paramPolicyNodeImpl1, PolicyNodeImpl paramPolicyNodeImpl2)
/*     */   {
/* 133 */     this(paramPolicyNodeImpl1, paramPolicyNodeImpl2.mValidPolicy, paramPolicyNodeImpl2.mQualifierSet, paramPolicyNodeImpl2.mCriticalityIndicator, paramPolicyNodeImpl2.mExpectedPolicySet, false);
/*     */   }
/*     */ 
/*     */   public PolicyNode getParent()
/*     */   {
/* 138 */     return this.mParent;
/*     */   }
/*     */ 
/*     */   public Iterator<PolicyNodeImpl> getChildren() {
/* 142 */     return Collections.unmodifiableSet(this.mChildren).iterator();
/*     */   }
/*     */ 
/*     */   public int getDepth() {
/* 146 */     return this.mDepth;
/*     */   }
/*     */ 
/*     */   public String getValidPolicy() {
/* 150 */     return this.mValidPolicy;
/*     */   }
/*     */ 
/*     */   public Set<PolicyQualifierInfo> getPolicyQualifiers() {
/* 154 */     return Collections.unmodifiableSet(this.mQualifierSet);
/*     */   }
/*     */ 
/*     */   public Set<String> getExpectedPolicies() {
/* 158 */     return Collections.unmodifiableSet(this.mExpectedPolicySet);
/*     */   }
/*     */ 
/*     */   public boolean isCritical() {
/* 162 */     return this.mCriticalityIndicator;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 173 */     StringBuffer localStringBuffer = new StringBuffer(asString());
/*     */ 
/* 175 */     Iterator localIterator = getChildren();
/* 176 */     while (localIterator.hasNext()) {
/* 177 */       localStringBuffer.append(localIterator.next());
/*     */     }
/* 179 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   boolean isImmutable()
/*     */   {
/* 185 */     return this.isImmutable;
/*     */   }
/*     */ 
/*     */   void setImmutable()
/*     */   {
/* 193 */     if (this.isImmutable)
/* 194 */       return;
/* 195 */     for (PolicyNodeImpl localPolicyNodeImpl : this.mChildren) {
/* 196 */       localPolicyNodeImpl.setImmutable();
/*     */     }
/* 198 */     this.isImmutable = true;
/*     */   }
/*     */ 
/*     */   private void addChild(PolicyNodeImpl paramPolicyNodeImpl)
/*     */   {
/* 208 */     if (this.isImmutable) {
/* 209 */       throw new IllegalStateException("PolicyNode is immutable");
/*     */     }
/* 211 */     this.mChildren.add(paramPolicyNodeImpl);
/*     */   }
/*     */ 
/*     */   void addExpectedPolicy(String paramString)
/*     */   {
/* 223 */     if (this.isImmutable) {
/* 224 */       throw new IllegalStateException("PolicyNode is immutable");
/*     */     }
/* 226 */     if (this.mOriginalExpectedPolicySet) {
/* 227 */       this.mExpectedPolicySet.clear();
/* 228 */       this.mOriginalExpectedPolicySet = false;
/*     */     }
/* 230 */     this.mExpectedPolicySet.add(paramString);
/*     */   }
/*     */ 
/*     */   void prune(int paramInt)
/*     */   {
/* 239 */     if (this.isImmutable) {
/* 240 */       throw new IllegalStateException("PolicyNode is immutable");
/*     */     }
/*     */ 
/* 243 */     if (this.mChildren.size() == 0) {
/* 244 */       return;
/*     */     }
/* 246 */     Iterator localIterator = this.mChildren.iterator();
/* 247 */     while (localIterator.hasNext()) {
/* 248 */       PolicyNodeImpl localPolicyNodeImpl = (PolicyNodeImpl)localIterator.next();
/* 249 */       localPolicyNodeImpl.prune(paramInt);
/*     */ 
/* 252 */       if ((localPolicyNodeImpl.mChildren.size() == 0) && (paramInt > this.mDepth + 1))
/* 253 */         localIterator.remove();
/*     */     }
/*     */   }
/*     */ 
/*     */   void deleteChild(PolicyNode paramPolicyNode)
/*     */   {
/* 263 */     if (this.isImmutable) {
/* 264 */       throw new IllegalStateException("PolicyNode is immutable");
/*     */     }
/* 266 */     this.mChildren.remove(paramPolicyNode);
/*     */   }
/*     */ 
/*     */   PolicyNodeImpl copyTree()
/*     */   {
/* 276 */     return copyTree(null);
/*     */   }
/*     */ 
/*     */   private PolicyNodeImpl copyTree(PolicyNodeImpl paramPolicyNodeImpl) {
/* 280 */     PolicyNodeImpl localPolicyNodeImpl1 = new PolicyNodeImpl(paramPolicyNodeImpl, this);
/*     */ 
/* 282 */     for (PolicyNodeImpl localPolicyNodeImpl2 : this.mChildren) {
/* 283 */       localPolicyNodeImpl2.copyTree(localPolicyNodeImpl1);
/*     */     }
/*     */ 
/* 286 */     return localPolicyNodeImpl1;
/*     */   }
/*     */ 
/*     */   Set<PolicyNodeImpl> getPolicyNodes(int paramInt)
/*     */   {
/* 296 */     HashSet localHashSet = new HashSet();
/* 297 */     getPolicyNodes(paramInt, localHashSet);
/* 298 */     return localHashSet;
/*     */   }
/*     */ 
/*     */   private void getPolicyNodes(int paramInt, Set<PolicyNodeImpl> paramSet)
/*     */   {
/* 307 */     if (this.mDepth == paramInt)
/* 308 */       paramSet.add(this);
/*     */     else
/* 310 */       for (PolicyNodeImpl localPolicyNodeImpl : this.mChildren)
/* 311 */         localPolicyNodeImpl.getPolicyNodes(paramInt, paramSet);
/*     */   }
/*     */ 
/*     */   Set<PolicyNodeImpl> getPolicyNodesExpected(int paramInt, String paramString, boolean paramBoolean)
/*     */   {
/* 330 */     if (paramString.equals("2.5.29.32.0")) {
/* 331 */       return getPolicyNodes(paramInt);
/*     */     }
/* 333 */     return getPolicyNodesExpectedHelper(paramInt, paramString, paramBoolean);
/*     */   }
/*     */ 
/*     */   private Set<PolicyNodeImpl> getPolicyNodesExpectedHelper(int paramInt, String paramString, boolean paramBoolean)
/*     */   {
/* 340 */     HashSet localHashSet = new HashSet();
/*     */ 
/* 342 */     if (this.mDepth < paramInt) {
/* 343 */       for (PolicyNodeImpl localPolicyNodeImpl : this.mChildren) {
/* 344 */         localHashSet.addAll(localPolicyNodeImpl.getPolicyNodesExpectedHelper(paramInt, paramString, paramBoolean));
/*     */       }
/*     */ 
/*     */     }
/* 349 */     else if (paramBoolean) {
/* 350 */       if (this.mExpectedPolicySet.contains("2.5.29.32.0"))
/* 351 */         localHashSet.add(this);
/*     */     }
/* 353 */     else if (this.mExpectedPolicySet.contains(paramString)) {
/* 354 */       localHashSet.add(this);
/*     */     }
/*     */ 
/* 358 */     return localHashSet;
/*     */   }
/*     */ 
/*     */   Set<PolicyNodeImpl> getPolicyNodesValid(int paramInt, String paramString)
/*     */   {
/* 370 */     HashSet localHashSet = new HashSet();
/*     */ 
/* 372 */     if (this.mDepth < paramInt) {
/* 373 */       for (PolicyNodeImpl localPolicyNodeImpl : this.mChildren) {
/* 374 */         localHashSet.addAll(localPolicyNodeImpl.getPolicyNodesValid(paramInt, paramString));
/*     */       }
/*     */     }
/* 377 */     else if (this.mValidPolicy.equals(paramString)) {
/* 378 */       localHashSet.add(this);
/*     */     }
/*     */ 
/* 381 */     return localHashSet;
/*     */   }
/*     */ 
/*     */   private static String policyToString(String paramString) {
/* 385 */     if (paramString.equals("2.5.29.32.0")) {
/* 386 */       return "anyPolicy";
/*     */     }
/* 388 */     return paramString;
/*     */   }
/*     */ 
/*     */   String asString()
/*     */   {
/* 396 */     if (this.mParent == null) {
/* 397 */       return "anyPolicy  ROOT\n";
/*     */     }
/* 399 */     StringBuffer localStringBuffer = new StringBuffer();
/* 400 */     int i = 0; for (int j = getDepth(); i < j; i++) {
/* 401 */       localStringBuffer.append("  ");
/*     */     }
/* 403 */     localStringBuffer.append(policyToString(getValidPolicy()));
/* 404 */     localStringBuffer.append("  CRIT: ");
/* 405 */     localStringBuffer.append(isCritical());
/* 406 */     localStringBuffer.append("  EP: ");
/* 407 */     for (String str : getExpectedPolicies()) {
/* 408 */       localStringBuffer.append(policyToString(str));
/* 409 */       localStringBuffer.append(" ");
/*     */     }
/* 411 */     localStringBuffer.append(" (");
/* 412 */     localStringBuffer.append(getDepth());
/* 413 */     localStringBuffer.append(")\n");
/* 414 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.provider.certpath.PolicyNodeImpl
 * JD-Core Version:    0.6.2
 */