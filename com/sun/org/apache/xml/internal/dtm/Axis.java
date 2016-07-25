/*     */ package com.sun.org.apache.xml.internal.dtm;
/*     */ 
/*     */ public final class Axis
/*     */ {
/*     */   public static final int ANCESTOR = 0;
/*     */   public static final int ANCESTORORSELF = 1;
/*     */   public static final int ATTRIBUTE = 2;
/*     */   public static final int CHILD = 3;
/*     */   public static final int DESCENDANT = 4;
/*     */   public static final int DESCENDANTORSELF = 5;
/*     */   public static final int FOLLOWING = 6;
/*     */   public static final int FOLLOWINGSIBLING = 7;
/*     */   public static final int NAMESPACEDECLS = 8;
/*     */   public static final int NAMESPACE = 9;
/*     */   public static final int PARENT = 10;
/*     */   public static final int PRECEDING = 11;
/*     */   public static final int PRECEDINGSIBLING = 12;
/*     */   public static final int SELF = 13;
/*     */   public static final int ALLFROMNODE = 14;
/*     */   public static final int PRECEDINGANDANCESTOR = 15;
/*     */   public static final int ALL = 16;
/*     */   public static final int DESCENDANTSFROMROOT = 17;
/*     */   public static final int DESCENDANTSORSELFFROMROOT = 18;
/*     */   public static final int ROOT = 19;
/*     */   public static final int FILTEREDLIST = 20;
/* 167 */   private static final boolean[] isReverse = { true, true, false, false, false, false, false, false, false, false, false, true, true, false };
/*     */ 
/* 185 */   private static final String[] names = { "ancestor", "ancestor-or-self", "attribute", "child", "descendant", "descendant-or-self", "following", "following-sibling", "namespace-decls", "namespace", "parent", "preceding", "preceding-sibling", "self", "all-from-node", "preceding-and-ancestor", "all", "descendants-from-root", "descendants-or-self-from-root", "root", "filtered-list" };
/*     */ 
/*     */   public static boolean isReverse(int axis)
/*     */   {
/* 211 */     return isReverse[axis];
/*     */   }
/*     */ 
/*     */   public static String getNames(int index) {
/* 215 */     return names[index];
/*     */   }
/*     */ 
/*     */   public static int getNamesLength() {
/* 219 */     return names.length;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.Axis
 * JD-Core Version:    0.6.2
 */