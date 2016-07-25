/*     */ package com.sun.xml.internal.ws.policy;
/*     */ 
/*     */ import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Comparison;
/*     */ import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.Text;
/*     */ import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.TreeSet;
/*     */ import javax.xml.namespace.QName;
/*     */ 
/*     */ public class Policy
/*     */   implements Iterable<AssertionSet>
/*     */ {
/*     */   private static final String POLICY_TOSTRING_NAME = "policy";
/*  57 */   private static final List<AssertionSet> NULL_POLICY_ASSERTION_SETS = Collections.unmodifiableList(new LinkedList());
/*     */ 
/*  64 */   private static final List<AssertionSet> EMPTY_POLICY_ASSERTION_SETS = Collections.unmodifiableList(new LinkedList(Arrays.asList(new AssertionSet[] { AssertionSet.emptyAssertionSet() })));
/*     */ 
/*  70 */   private static final Set<QName> EMPTY_VOCABULARY = Collections.unmodifiableSet(new TreeSet(PolicyUtils.Comparison.QNAME_COMPARATOR));
/*     */ 
/*  76 */   private static final Policy ANONYMOUS_NULL_POLICY = new Policy(null, null, NULL_POLICY_ASSERTION_SETS, EMPTY_VOCABULARY);
/*     */ 
/*  82 */   private static final Policy ANONYMOUS_EMPTY_POLICY = new Policy(null, null, EMPTY_POLICY_ASSERTION_SETS, EMPTY_VOCABULARY);
/*     */   private String policyId;
/*     */   private String name;
/*     */   private NamespaceVersion nsVersion;
/*     */   private final List<AssertionSet> assertionSets;
/*     */   private final Set<QName> vocabulary;
/*     */   private final Collection<QName> immutableVocabulary;
/*     */   private final String toStringName;
/*     */ 
/*     */   public static Policy createNullPolicy()
/*     */   {
/* 127 */     return ANONYMOUS_NULL_POLICY;
/*     */   }
/*     */ 
/*     */   public static Policy createEmptyPolicy()
/*     */   {
/* 138 */     return ANONYMOUS_EMPTY_POLICY;
/*     */   }
/*     */ 
/*     */   public static Policy createNullPolicy(String name, String policyId)
/*     */   {
/* 150 */     if ((name == null) && (policyId == null)) {
/* 151 */       return ANONYMOUS_NULL_POLICY;
/*     */     }
/* 153 */     return new Policy(name, policyId, NULL_POLICY_ASSERTION_SETS, EMPTY_VOCABULARY);
/*     */   }
/*     */ 
/*     */   public static Policy createNullPolicy(NamespaceVersion nsVersion, String name, String policyId)
/*     */   {
/* 167 */     if (((nsVersion == null) || (nsVersion == NamespaceVersion.getLatestVersion())) && (name == null) && (policyId == null)) {
/* 168 */       return ANONYMOUS_NULL_POLICY;
/*     */     }
/* 170 */     return new Policy(nsVersion, name, policyId, NULL_POLICY_ASSERTION_SETS, EMPTY_VOCABULARY);
/*     */   }
/*     */ 
/*     */   public static Policy createEmptyPolicy(String name, String policyId)
/*     */   {
/* 185 */     if ((name == null) && (policyId == null)) {
/* 186 */       return ANONYMOUS_EMPTY_POLICY;
/*     */     }
/* 188 */     return new Policy(name, policyId, EMPTY_POLICY_ASSERTION_SETS, EMPTY_VOCABULARY);
/*     */   }
/*     */ 
/*     */   public static Policy createEmptyPolicy(NamespaceVersion nsVersion, String name, String policyId)
/*     */   {
/* 204 */     if (((nsVersion == null) || (nsVersion == NamespaceVersion.getLatestVersion())) && (name == null) && (policyId == null)) {
/* 205 */       return ANONYMOUS_EMPTY_POLICY;
/*     */     }
/* 207 */     return new Policy(nsVersion, name, policyId, EMPTY_POLICY_ASSERTION_SETS, EMPTY_VOCABULARY);
/*     */   }
/*     */ 
/*     */   public static Policy createPolicy(Collection<AssertionSet> sets)
/*     */   {
/* 224 */     if ((sets == null) || (sets.isEmpty())) {
/* 225 */       return createNullPolicy();
/*     */     }
/* 227 */     return new Policy("policy", sets);
/*     */   }
/*     */ 
/*     */   public static Policy createPolicy(String name, String policyId, Collection<AssertionSet> sets)
/*     */   {
/* 246 */     if ((sets == null) || (sets.isEmpty())) {
/* 247 */       return createNullPolicy(name, policyId);
/*     */     }
/* 249 */     return new Policy("policy", name, policyId, sets);
/*     */   }
/*     */ 
/*     */   public static Policy createPolicy(NamespaceVersion nsVersion, String name, String policyId, Collection<AssertionSet> sets)
/*     */   {
/* 269 */     if ((sets == null) || (sets.isEmpty())) {
/* 270 */       return createNullPolicy(nsVersion, name, policyId);
/*     */     }
/* 272 */     return new Policy(nsVersion, "policy", name, policyId, sets);
/*     */   }
/*     */ 
/*     */   private Policy(String name, String policyId, List<AssertionSet> assertionSets, Set<QName> vocabulary)
/*     */   {
/* 289 */     this.nsVersion = NamespaceVersion.getLatestVersion();
/* 290 */     this.toStringName = "policy";
/* 291 */     this.name = name;
/* 292 */     this.policyId = policyId;
/* 293 */     this.assertionSets = assertionSets;
/* 294 */     this.vocabulary = vocabulary;
/* 295 */     this.immutableVocabulary = Collections.unmodifiableCollection(this.vocabulary);
/*     */   }
/*     */ 
/*     */   Policy(String toStringName, Collection<AssertionSet> sets)
/*     */   {
/* 310 */     this.nsVersion = NamespaceVersion.getLatestVersion();
/* 311 */     this.toStringName = toStringName;
/*     */ 
/* 313 */     if ((sets == null) || (sets.isEmpty())) {
/* 314 */       this.assertionSets = NULL_POLICY_ASSERTION_SETS;
/* 315 */       this.vocabulary = EMPTY_VOCABULARY;
/* 316 */       this.immutableVocabulary = EMPTY_VOCABULARY;
/*     */     } else {
/* 318 */       this.assertionSets = new LinkedList();
/* 319 */       this.vocabulary = new TreeSet(PolicyUtils.Comparison.QNAME_COMPARATOR);
/* 320 */       this.immutableVocabulary = Collections.unmodifiableCollection(this.vocabulary);
/*     */ 
/* 322 */       addAll(sets);
/*     */     }
/*     */   }
/*     */ 
/*     */   Policy(String toStringName, String name, String policyId, Collection<AssertionSet> sets)
/*     */   {
/* 340 */     this(toStringName, sets);
/* 341 */     this.name = name;
/* 342 */     this.policyId = policyId;
/*     */   }
/*     */ 
/*     */   private Policy(NamespaceVersion nsVersion, String name, String policyId, List<AssertionSet> assertionSets, Set<QName> vocabulary)
/*     */   {
/* 359 */     this.nsVersion = nsVersion;
/* 360 */     this.toStringName = "policy";
/* 361 */     this.name = name;
/* 362 */     this.policyId = policyId;
/* 363 */     this.assertionSets = assertionSets;
/* 364 */     this.vocabulary = vocabulary;
/* 365 */     this.immutableVocabulary = Collections.unmodifiableCollection(this.vocabulary);
/*     */   }
/*     */ 
/*     */   Policy(NamespaceVersion nsVersion, String toStringName, Collection<AssertionSet> sets)
/*     */   {
/* 381 */     this.nsVersion = nsVersion;
/* 382 */     this.toStringName = toStringName;
/*     */ 
/* 384 */     if ((sets == null) || (sets.isEmpty())) {
/* 385 */       this.assertionSets = NULL_POLICY_ASSERTION_SETS;
/* 386 */       this.vocabulary = EMPTY_VOCABULARY;
/* 387 */       this.immutableVocabulary = EMPTY_VOCABULARY;
/*     */     } else {
/* 389 */       this.assertionSets = new LinkedList();
/* 390 */       this.vocabulary = new TreeSet(PolicyUtils.Comparison.QNAME_COMPARATOR);
/* 391 */       this.immutableVocabulary = Collections.unmodifiableCollection(this.vocabulary);
/*     */ 
/* 393 */       addAll(sets);
/*     */     }
/*     */   }
/*     */ 
/*     */   Policy(NamespaceVersion nsVersion, String toStringName, String name, String policyId, Collection<AssertionSet> sets)
/*     */   {
/* 412 */     this(nsVersion, toStringName, sets);
/* 413 */     this.name = name;
/* 414 */     this.policyId = policyId;
/*     */   }
/*     */ 
/*     */   private boolean add(AssertionSet set)
/*     */   {
/* 426 */     if (set == null) {
/* 427 */       return false;
/*     */     }
/*     */ 
/* 430 */     if (this.assertionSets.contains(set)) {
/* 431 */       return false;
/*     */     }
/* 433 */     this.assertionSets.add(set);
/* 434 */     this.vocabulary.addAll(set.getVocabulary());
/* 435 */     return true;
/*     */   }
/*     */ 
/*     */   private boolean addAll(Collection<AssertionSet> sets)
/*     */   {
/* 450 */     assert ((sets != null) && (!sets.isEmpty())) : LocalizationMessages.WSP_0036_PRIVATE_METHOD_DOES_NOT_ACCEPT_NULL_OR_EMPTY_COLLECTION();
/*     */ 
/* 452 */     boolean result = true;
/* 453 */     for (AssertionSet set : sets) {
/* 454 */       result &= add(set);
/*     */     }
/* 456 */     Collections.sort(this.assertionSets);
/*     */ 
/* 458 */     return result;
/*     */   }
/*     */ 
/*     */   Collection<AssertionSet> getContent() {
/* 462 */     return this.assertionSets;
/*     */   }
/*     */ 
/*     */   public String getId()
/*     */   {
/* 471 */     return this.policyId;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 480 */     return this.name;
/*     */   }
/*     */ 
/*     */   public NamespaceVersion getNamespaceVersion() {
/* 484 */     return this.nsVersion;
/*     */   }
/*     */ 
/*     */   public String getIdOrName()
/*     */   {
/* 496 */     if (this.policyId != null) {
/* 497 */       return this.policyId;
/*     */     }
/* 499 */     return this.name;
/*     */   }
/*     */ 
/*     */   public int getNumberOfAssertionSets()
/*     */   {
/* 508 */     return this.assertionSets.size();
/*     */   }
/*     */ 
/*     */   public Iterator<AssertionSet> iterator()
/*     */   {
/* 518 */     return this.assertionSets.iterator();
/*     */   }
/*     */ 
/*     */   public boolean isNull()
/*     */   {
/* 527 */     return this.assertionSets.size() == 0;
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 536 */     return (this.assertionSets.size() == 1) && (((AssertionSet)this.assertionSets.get(0)).isEmpty());
/*     */   }
/*     */ 
/*     */   public boolean contains(String namespaceUri)
/*     */   {
/* 546 */     for (QName entry : this.vocabulary) {
/* 547 */       if (entry.getNamespaceURI().equals(namespaceUri)) {
/* 548 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 552 */     return false;
/*     */   }
/*     */ 
/*     */   public Collection<QName> getVocabulary()
/*     */   {
/* 562 */     return this.immutableVocabulary;
/*     */   }
/*     */ 
/*     */   public boolean contains(QName assertionName)
/*     */   {
/* 574 */     return this.vocabulary.contains(assertionName);
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/* 582 */     if (this == obj) {
/* 583 */       return true;
/*     */     }
/*     */ 
/* 586 */     if (!(obj instanceof Policy)) {
/* 587 */       return false;
/*     */     }
/*     */ 
/* 590 */     Policy that = (Policy)obj;
/*     */ 
/* 592 */     boolean result = true;
/*     */ 
/* 594 */     result = (result) && (this.vocabulary.equals(that.vocabulary));
/* 595 */     result = (result) && (this.assertionSets.size() == that.assertionSets.size()) && (this.assertionSets.containsAll(that.assertionSets));
/*     */ 
/* 597 */     return result;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 605 */     int result = 17;
/*     */ 
/* 607 */     result = 37 * result + this.vocabulary.hashCode();
/* 608 */     result = 37 * result + this.assertionSets.hashCode();
/*     */ 
/* 610 */     return result;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 618 */     return toString(0, new StringBuffer()).toString();
/*     */   }
/*     */ 
/*     */   StringBuffer toString(int indentLevel, StringBuffer buffer)
/*     */   {
/* 629 */     String indent = PolicyUtils.Text.createIndent(indentLevel);
/* 630 */     String innerIndent = PolicyUtils.Text.createIndent(indentLevel + 1);
/* 631 */     String innerDoubleIndent = PolicyUtils.Text.createIndent(indentLevel + 2);
/*     */ 
/* 633 */     buffer.append(indent).append(this.toStringName).append(" {").append(PolicyUtils.Text.NEW_LINE);
/* 634 */     buffer.append(innerIndent).append("namespace version = '").append(this.nsVersion.name()).append('\'').append(PolicyUtils.Text.NEW_LINE);
/* 635 */     buffer.append(innerIndent).append("id = '").append(this.policyId).append('\'').append(PolicyUtils.Text.NEW_LINE);
/* 636 */     buffer.append(innerIndent).append("name = '").append(this.name).append('\'').append(PolicyUtils.Text.NEW_LINE);
/*     */ 
/* 638 */     buffer.append(innerIndent).append("vocabulary {").append(PolicyUtils.Text.NEW_LINE);
/*     */     int index;
/* 639 */     if (this.vocabulary.isEmpty()) {
/* 640 */       buffer.append(innerDoubleIndent).append("no entries").append(PolicyUtils.Text.NEW_LINE);
/*     */     } else {
/* 642 */       index = 1;
/* 643 */       for (QName entry : this.vocabulary) {
/* 644 */         buffer.append(innerDoubleIndent).append(index++).append(". entry = '").append(entry.getNamespaceURI()).append(':').append(entry.getLocalPart()).append('\'').append(PolicyUtils.Text.NEW_LINE);
/*     */       }
/*     */     }
/* 647 */     buffer.append(innerIndent).append('}').append(PolicyUtils.Text.NEW_LINE);
/*     */ 
/* 649 */     if (this.assertionSets.isEmpty())
/* 650 */       buffer.append(innerIndent).append("no assertion sets").append(PolicyUtils.Text.NEW_LINE);
/*     */     else {
/* 652 */       for (AssertionSet set : this.assertionSets) {
/* 653 */         set.toString(indentLevel + 1, buffer).append(PolicyUtils.Text.NEW_LINE);
/*     */       }
/*     */     }
/*     */ 
/* 657 */     buffer.append(indent).append('}');
/*     */ 
/* 659 */     return buffer;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.Policy
 * JD-Core Version:    0.6.2
 */