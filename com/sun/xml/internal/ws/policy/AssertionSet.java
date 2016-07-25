/*     */ package com.sun.xml.internal.ws.policy;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Comparison;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public final class AssertionSet
/*     */   implements Iterable<PolicyAssertion>, Comparable<AssertionSet>
/*     */ {
/*  48 */   private static final AssertionSet EMPTY_ASSERTION_SET = new AssertionSet(Collections.unmodifiableList(new LinkedList()));
/*     */ 
/*  60 */   private static final Comparator<PolicyAssertion> ASSERTION_COMPARATOR = new Comparator() {
/*     */     public int compare(PolicyAssertion pa1, PolicyAssertion pa2) {
/*  62 */       if (pa1.equals(pa2)) {
/*  63 */         return 0;
/*     */       }
/*     */ 
/*  68 */       int result = PolicyUtils.Comparison.QNAME_COMPARATOR.compare(pa1.getName(), pa2.getName());
/*  69 */       if (result != 0) {
/*  70 */         return result;
/*     */       }
/*     */ 
/*  73 */       result = PolicyUtils.Comparison.compareNullableStrings(pa1.getValue(), pa2.getValue());
/*  74 */       if (result != 0) {
/*  75 */         return result;
/*     */       }
/*     */ 
/*  78 */       result = PolicyUtils.Comparison.compareBoolean(pa1.hasNestedAssertions(), pa2.hasNestedAssertions());
/*  79 */       if (result != 0) {
/*  80 */         return result;
/*     */       }
/*     */ 
/*  83 */       result = PolicyUtils.Comparison.compareBoolean(pa1.hasNestedPolicy(), pa2.hasNestedPolicy());
/*  84 */       if (result != 0) {
/*  85 */         return result;
/*     */       }
/*     */ 
/*  88 */       return Math.round(Math.signum(pa1.hashCode() - pa2.hashCode()));
/*     */     }
/*  60 */   };
/*     */   private final List<PolicyAssertion> assertions;
/*  93 */   private final Set<QName> vocabulary = new TreeSet(PolicyUtils.Comparison.QNAME_COMPARATOR);
/*  94 */   private final Collection<QName> immutableVocabulary = Collections.unmodifiableCollection(this.vocabulary);
/*     */ 
/*     */   private AssertionSet(List<PolicyAssertion> list) {
/*  97 */     assert (list != null) : LocalizationMessages.WSP_0037_PRIVATE_CONSTRUCTOR_DOES_NOT_TAKE_NULL();
/*  98 */     this.assertions = list;
/*     */   }
/*     */ 
/*     */   private AssertionSet(Collection<AssertionSet> alternatives) {
/* 102 */     this.assertions = new LinkedList();
/* 103 */     for (AssertionSet alternative : alternatives)
/* 104 */       addAll(alternative.assertions);
/*     */   }
/*     */ 
/*     */   private boolean add(PolicyAssertion assertion)
/*     */   {
/* 109 */     if (assertion == null) {
/* 110 */       return false;
/*     */     }
/*     */ 
/* 113 */     if (this.assertions.contains(assertion)) {
/* 114 */       return false;
/*     */     }
/* 116 */     this.assertions.add(assertion);
/* 117 */     this.vocabulary.add(assertion.getName());
/* 118 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean addAll(Collection<? extends PolicyAssertion> assertions)
/*     */   {
/* 123 */     boolean result = true;
/*     */ 
/* 125 */     if (assertions != null) {
/* 126 */       for (PolicyAssertion assertion : assertions) {
/* 127 */         result &= add(assertion);
/*     */       }
/*     */     }
/*     */ 
/* 131 */     return result;
/*     */   }
/*     */ 
/*     */   Collection<PolicyAssertion> getAssertions()
/*     */   {
/* 140 */     return this.assertions;
/*     */   }
/*     */ 
/*     */   Collection<QName> getVocabulary()
/*     */   {
/* 150 */     return this.immutableVocabulary;
/*     */   }
/*     */ 
/*     */   boolean isCompatibleWith(AssertionSet alternative, PolicyIntersector.CompatibilityMode mode)
/*     */   {
/* 161 */     boolean result = (mode == PolicyIntersector.CompatibilityMode.LAX) || (this.vocabulary.equals(alternative.vocabulary));
/*     */ 
/* 163 */     result = (result) && (areAssertionsCompatible(alternative, mode));
/* 164 */     result = (result) && (alternative.areAssertionsCompatible(this, mode));
/*     */ 
/* 166 */     return result;
/*     */   }
/*     */ 
/*     */   private boolean areAssertionsCompatible(AssertionSet alternative, PolicyIntersector.CompatibilityMode mode) {
/* 170 */     for (PolicyAssertion thisAssertion : this.assertions) {
/* 171 */       if ((mode == PolicyIntersector.CompatibilityMode.STRICT) || (!thisAssertion.isIgnorable())) {
/* 172 */         Iterator i$ = alternative.assertions.iterator();
/*     */         while (true) { if (!i$.hasNext()) break label95; PolicyAssertion thatAssertion = (PolicyAssertion)i$.next();
/* 173 */           if (thisAssertion.isCompatibleWith(thatAssertion, mode)) {
/*     */             break;
/*     */           }
/*     */         }
/* 177 */         return false;
/*     */       }
/*     */     }
/* 180 */     label95: return true;
/*     */   }
/*     */ 
/*     */   public static AssertionSet createMergedAssertionSet(Collection<AssertionSet> alternatives)
/*     */   {
/* 195 */     if ((alternatives == null) || (alternatives.isEmpty())) {
/* 196 */       return EMPTY_ASSERTION_SET;
/*     */     }
/*     */ 
/* 199 */     AssertionSet result = new AssertionSet(alternatives);
/* 200 */     Collections.sort(result.assertions, ASSERTION_COMPARATOR);
/*     */ 
/* 202 */     return result;
/*     */   }
/*     */ 
/*     */   public static AssertionSet createAssertionSet(Collection<? extends PolicyAssertion> assertions)
/*     */   {
/* 212 */     if ((assertions == null) || (assertions.isEmpty())) {
/* 213 */       return EMPTY_ASSERTION_SET;
/*     */     }
/*     */ 
/* 216 */     AssertionSet result = new AssertionSet(new LinkedList());
/* 217 */     result.addAll(assertions);
/* 218 */     Collections.sort(result.assertions, ASSERTION_COMPARATOR);
/*     */ 
/* 220 */     return result;
/*     */   }
/*     */ 
/*     */   public static AssertionSet emptyAssertionSet() {
/* 224 */     return EMPTY_ASSERTION_SET;
/*     */   }
/*     */ 
/*     */   public Iterator<PolicyAssertion> iterator()
/*     */   {
/* 232 */     return this.assertions.iterator();
/*     */   }
/*     */ 
/*     */   public Collection<PolicyAssertion> get(QName name)
/*     */   {
/* 244 */     List matched = new LinkedList();
/*     */ 
/* 246 */     if (this.vocabulary.contains(name))
/*     */     {
/* 248 */       for (PolicyAssertion assertion : this.assertions) {
/* 249 */         if (assertion.getName().equals(name)) {
/* 250 */           matched.add(assertion);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 255 */     return matched;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 264 */     return this.assertions.isEmpty();
/*     */   }
/*     */ 
/*     */   public boolean contains(QName assertionName)
/*     */   {
/* 274 */     return this.vocabulary.contains(assertionName);
/*     */   }
/*     */ 
/*     */   public int compareTo(AssertionSet that)
/*     */   {
/* 282 */     if (equals(that)) {
/* 283 */       return 0;
/*     */     }
/*     */ 
/* 287 */     Iterator vIterator1 = getVocabulary().iterator();
/* 288 */     Iterator vIterator2 = that.getVocabulary().iterator();
/* 289 */     while (vIterator1.hasNext()) {
/* 290 */       QName entry1 = (QName)vIterator1.next();
/* 291 */       if (vIterator2.hasNext()) {
/* 292 */         QName entry2 = (QName)vIterator2.next();
/* 293 */         int result = PolicyUtils.Comparison.QNAME_COMPARATOR.compare(entry1, entry2);
/* 294 */         if (result != 0)
/* 295 */           return result;
/*     */       }
/*     */       else {
/* 298 */         return 1;
/*     */       }
/*     */     }
/*     */ 
/* 302 */     if (vIterator2.hasNext()) {
/* 303 */       return -1;
/*     */     }
/*     */ 
/* 307 */     Iterator pIterator1 = getAssertions().iterator();
/* 308 */     Iterator pIterator2 = that.getAssertions().iterator();
/* 309 */     while (pIterator1.hasNext()) {
/* 310 */       PolicyAssertion pa1 = (PolicyAssertion)pIterator1.next();
/* 311 */       if (pIterator2.hasNext()) {
/* 312 */         PolicyAssertion pa2 = (PolicyAssertion)pIterator2.next();
/* 313 */         int result = ASSERTION_COMPARATOR.compare(pa1, pa2);
/* 314 */         if (result != 0)
/* 315 */           return result;
/*     */       }
/*     */       else {
/* 318 */         return 1;
/*     */       }
/*     */     }
/*     */ 
/* 322 */     if (pIterator2.hasNext()) {
/* 323 */       return -1;
/*     */     }
/*     */ 
/* 329 */     return 1;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 336 */     if (this == obj) {
/* 337 */       return true;
/*     */     }
/*     */ 
/* 340 */     if (!(obj instanceof AssertionSet)) {
/* 341 */       return false;
/*     */     }
/*     */ 
/* 344 */     AssertionSet that = (AssertionSet)obj;
/* 345 */     boolean result = true;
/*     */ 
/* 347 */     result = (result) && (this.vocabulary.equals(that.vocabulary));
/* 348 */     result = (result) && (this.assertions.size() == that.assertions.size()) && (this.assertions.containsAll(that.assertions));
/*     */ 
/* 350 */     return result;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 357 */     int result = 17;
/*     */ 
/* 359 */     result = 37 * result + this.vocabulary.hashCode();
/* 360 */     result = 37 * result + this.assertions.hashCode();
/*     */ 
/* 362 */     return result;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 369 */     return toString(0, new StringBuffer()).toString();
/*     */   }
/*     */ 
/*     */   StringBuffer toString(int indentLevel, StringBuffer buffer)
/*     */   {
/* 380 */     String indent = PolicyUtils.Text.createIndent(indentLevel);
/* 381 */     String innerIndent = PolicyUtils.Text.createIndent(indentLevel + 1);
/*     */ 
/* 383 */     buffer.append(indent).append("assertion set {").append(PolicyUtils.Text.NEW_LINE);
/*     */ 
/* 385 */     if (this.assertions.isEmpty())
/* 386 */       buffer.append(innerIndent).append("no assertions").append(PolicyUtils.Text.NEW_LINE);
/*     */     else {
/* 388 */       for (PolicyAssertion assertion : this.assertions) {
/* 389 */         assertion.toString(indentLevel + 1, buffer).append(PolicyUtils.Text.NEW_LINE);
/*     */       }
/*     */     }
/*     */ 
/* 393 */     buffer.append(indent).append('}');
/*     */ 
/* 395 */     return buffer;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.AssertionSet
 * JD-Core Version:    0.6.2
 */