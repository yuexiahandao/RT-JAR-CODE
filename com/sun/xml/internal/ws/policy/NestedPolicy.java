/*     */ package com.sun.xml.internal.ws.policy;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public final class NestedPolicy extends Policy
/*     */ {
/*     */   private static final String NESTED_POLICY_TOSTRING_NAME = "nested policy";
/*     */ 
/*     */   private NestedPolicy(AssertionSet set)
/*     */   {
/*  40 */     super("nested policy", Arrays.asList(new AssertionSet[] { set }));
/*     */   }
/*     */ 
/*     */   private NestedPolicy(String name, String policyId, AssertionSet set) {
/*  44 */     super("nested policy", name, policyId, Arrays.asList(new AssertionSet[] { set }));
/*     */   }
/*     */ 
/*     */   static NestedPolicy createNestedPolicy(AssertionSet set) {
/*  48 */     return new NestedPolicy(set);
/*     */   }
/*     */ 
/*     */   static NestedPolicy createNestedPolicy(String name, String policyId, AssertionSet set) {
/*  52 */     return new NestedPolicy(name, policyId, set);
/*     */   }
/*     */ 
/*     */   public AssertionSet getAssertionSet()
/*     */   {
/*  63 */     Iterator iterator = iterator();
/*  64 */     if (iterator.hasNext()) {
/*  65 */       return (AssertionSet)iterator.next();
/*     */     }
/*  67 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object obj)
/*     */   {
/*  76 */     if (this == obj) {
/*  77 */       return true;
/*     */     }
/*     */ 
/*  80 */     if (!(obj instanceof NestedPolicy)) {
/*  81 */       return false;
/*     */     }
/*     */ 
/*  84 */     NestedPolicy that = (NestedPolicy)obj;
/*     */ 
/*  86 */     return super.equals(that);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  91 */     return super.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  99 */     return toString(0, new StringBuffer()).toString();
/*     */   }
/*     */ 
/*     */   StringBuffer toString(int indentLevel, StringBuffer buffer)
/*     */   {
/* 111 */     return super.toString(indentLevel, buffer);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.policy.NestedPolicy
 * JD-Core Version:    0.6.2
 */