/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import java.text.CollationElementIterator;
/*     */ import java.text.Collator;
/*     */ import java.text.RuleBasedCollator;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public class StringComparable
/*     */   implements Comparable
/*     */ {
/*     */   public static final int UNKNOWN_CASE = -1;
/*     */   public static final int UPPER_CASE = 1;
/*     */   public static final int LOWER_CASE = 2;
/*     */   private String m_text;
/*     */   private Locale m_locale;
/*     */   private RuleBasedCollator m_collator;
/*     */   private String m_caseOrder;
/*  48 */   private int m_mask = -1;
/*     */ 
/*     */   public StringComparable(String text, Locale locale, Collator collator, String caseOrder) {
/*  51 */     this.m_text = text;
/*  52 */     this.m_locale = locale;
/*  53 */     this.m_collator = ((RuleBasedCollator)collator);
/*  54 */     this.m_caseOrder = caseOrder;
/*  55 */     this.m_mask = getMask(this.m_collator.getStrength());
/*     */   }
/*     */ 
/*     */   public static final Comparable getComparator(String text, Locale locale, Collator collator, String caseOrder) {
/*  59 */     if ((caseOrder == null) || (caseOrder.length() == 0)) {
/*  60 */       return ((RuleBasedCollator)collator).getCollationKey(text);
/*     */     }
/*  62 */     return new StringComparable(text, locale, collator, caseOrder);
/*     */   }
/*     */ 
/*     */   public final String toString() {
/*  66 */     return this.m_text;
/*     */   }
/*     */   public int compareTo(Object o) {
/*  69 */     String pattern = ((StringComparable)o).toString();
/*  70 */     if (this.m_text.equals(pattern)) {
/*  71 */       return 0;
/*     */     }
/*  73 */     int savedStrength = this.m_collator.getStrength();
/*  74 */     int comp = 0;
/*     */ 
/*  76 */     if ((savedStrength == 0) || (savedStrength == 1)) {
/*  77 */       comp = this.m_collator.compare(this.m_text, pattern);
/*     */     } else {
/*  79 */       this.m_collator.setStrength(1);
/*  80 */       comp = this.m_collator.compare(this.m_text, pattern);
/*  81 */       this.m_collator.setStrength(savedStrength);
/*     */     }
/*  83 */     if (comp != 0) {
/*  84 */       return comp;
/*     */     }
/*     */ 
/*  89 */     comp = getCaseDiff(this.m_text, pattern);
/*  90 */     if (comp != 0) {
/*  91 */       return comp;
/*     */     }
/*  93 */     return this.m_collator.compare(this.m_text, pattern);
/*     */   }
/*     */ 
/*     */   private final int getCaseDiff(String text, String pattern)
/*     */   {
/*  99 */     int savedStrength = this.m_collator.getStrength();
/* 100 */     int savedDecomposition = this.m_collator.getDecomposition();
/* 101 */     this.m_collator.setStrength(2);
/* 102 */     this.m_collator.setDecomposition(1);
/*     */ 
/* 104 */     int[] diff = getFirstCaseDiff(text, pattern, this.m_locale);
/* 105 */     this.m_collator.setStrength(savedStrength);
/* 106 */     this.m_collator.setDecomposition(savedDecomposition);
/* 107 */     if (diff != null) {
/* 108 */       if (this.m_caseOrder.equals("upper-first")) {
/* 109 */         if (diff[0] == 1) {
/* 110 */           return -1;
/*     */         }
/* 112 */         return 1;
/*     */       }
/*     */ 
/* 115 */       if (diff[0] == 2) {
/* 116 */         return -1;
/*     */       }
/* 118 */       return 1;
/*     */     }
/*     */ 
/* 122 */     return 0; } 
/* 131 */   private final int[] getFirstCaseDiff(String text, String pattern, Locale locale) { CollationElementIterator targIter = this.m_collator.getCollationElementIterator(text);
/* 132 */     CollationElementIterator patIter = this.m_collator.getCollationElementIterator(pattern);
/* 133 */     int startTarg = -1;
/* 134 */     int endTarg = -1;
/* 135 */     int startPatt = -1;
/* 136 */     int endPatt = -1;
/* 137 */     int done = getElement(-1);
/* 138 */     int patternElement = 0; int targetElement = 0;
/* 139 */     boolean getPattern = true; boolean getTarget = true;
/*     */     int[] diff;
/*     */     do { String subText;
/*     */       String subPatt;
/*     */       String subTextUp;
/*     */       String subPattUp;
/*     */       do { do while (true) { if (getPattern) {
/* 143 */               startPatt = patIter.getOffset();
/* 144 */               patternElement = getElement(patIter.next());
/* 145 */               endPatt = patIter.getOffset();
/*     */             }
/* 147 */             if (getTarget) {
/* 148 */               startTarg = targIter.getOffset();
/* 149 */               targetElement = getElement(targIter.next());
/* 150 */               endTarg = targIter.getOffset();
/*     */             }
/* 152 */             getTarget = getPattern = 1;
/* 153 */             if ((patternElement == done) || (targetElement == done))
/* 154 */               return null;
/* 155 */             if (targetElement == 0) {
/* 156 */               getPattern = false; } else {
/* 157 */               if (patternElement != 0) break;
/* 158 */               getTarget = false;
/*     */             } } while ((targetElement == patternElement) || 
/* 160 */           (startPatt >= endPatt) || (startTarg >= endTarg));
/* 161 */         subText = text.substring(startTarg, endTarg);
/* 162 */         subPatt = pattern.substring(startPatt, endPatt);
/* 163 */         subTextUp = subText.toUpperCase(locale);
/* 164 */         subPattUp = subPatt.toUpperCase(locale); }
/* 165 */       while (this.m_collator.compare(subTextUp, subPattUp) != 0);
/*     */ 
/* 169 */       diff = new int[] { -1, -1 };
/* 170 */       if (this.m_collator.compare(subText, subTextUp) == 0)
/* 171 */         diff[0] = 1;
/* 172 */       else if (this.m_collator.compare(subText, subText.toLowerCase(locale)) == 0) {
/* 173 */         diff[0] = 2;
/*     */       }
/* 175 */       if (this.m_collator.compare(subPatt, subPattUp) == 0)
/* 176 */         diff[1] = 1;
/* 177 */       else if (this.m_collator.compare(subPatt, subPatt.toLowerCase(locale)) == 0) {
/* 178 */         diff[1] = 2;
/*     */       }
/*     */     }
/* 181 */     while (((diff[0] != 1) || (diff[1] != 2)) && ((diff[0] != 2) || (diff[1] != 1)));
/*     */ 
/* 183 */     return diff;
/*     */   }
/*     */ 
/*     */   private static final int getMask(int strength)
/*     */   {
/* 199 */     switch (strength) {
/*     */     case 0:
/* 201 */       return -65536;
/*     */     case 1:
/* 203 */       return -256;
/*     */     }
/* 205 */     return -1;
/*     */   }
/*     */ 
/*     */   private final int getElement(int maxStrengthElement)
/*     */   {
/* 212 */     return maxStrengthElement & this.m_mask;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.StringComparable
 * JD-Core Version:    0.6.2
 */