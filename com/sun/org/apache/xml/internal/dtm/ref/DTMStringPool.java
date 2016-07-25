/*     */ package com.sun.org.apache.xml.internal.dtm.ref;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.utils.IntVector;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class DTMStringPool
/*     */ {
/*     */   Vector m_intToString;
/*     */   static final int HASHPRIME = 101;
/*  63 */   int[] m_hashStart = new int[101];
/*     */   IntVector m_hashChain;
/*     */   public static final int NULL = -1;
/*     */ 
/*     */   public DTMStringPool(int chainSize)
/*     */   {
/*  74 */     this.m_intToString = new Vector();
/*  75 */     this.m_hashChain = new IntVector(chainSize);
/*  76 */     removeAllElements();
/*     */ 
/*  79 */     stringToIndex("");
/*     */   }
/*     */ 
/*     */   public DTMStringPool()
/*     */   {
/*  84 */     this(512);
/*     */   }
/*     */ 
/*     */   public void removeAllElements()
/*     */   {
/*  89 */     this.m_intToString.removeAllElements();
/*  90 */     for (int i = 0; i < 101; i++)
/*  91 */       this.m_hashStart[i] = -1;
/*  92 */     this.m_hashChain.removeAllElements();
/*     */   }
/*     */ 
/*     */   public String indexToString(int i)
/*     */     throws ArrayIndexOutOfBoundsException
/*     */   {
/* 102 */     if (i == -1) return null;
/* 103 */     return (String)this.m_intToString.elementAt(i);
/*     */   }
/*     */ 
/*     */   public int stringToIndex(String s)
/*     */   {
/* 109 */     if (s == null) return -1;
/*     */ 
/* 111 */     int hashslot = s.hashCode() % 101;
/* 112 */     if (hashslot < 0) hashslot = -hashslot;
/*     */ 
/* 115 */     int hashlast = this.m_hashStart[hashslot];
/* 116 */     int hashcandidate = hashlast;
/* 117 */     while (hashcandidate != -1)
/*     */     {
/* 119 */       if (this.m_intToString.elementAt(hashcandidate).equals(s)) {
/* 120 */         return hashcandidate;
/*     */       }
/* 122 */       hashlast = hashcandidate;
/* 123 */       hashcandidate = this.m_hashChain.elementAt(hashcandidate);
/*     */     }
/*     */ 
/* 127 */     int newIndex = this.m_intToString.size();
/* 128 */     this.m_intToString.addElement(s);
/*     */ 
/* 130 */     this.m_hashChain.addElement(-1);
/* 131 */     if (hashlast == -1)
/* 132 */       this.m_hashStart[hashslot] = newIndex;
/*     */     else {
/* 134 */       this.m_hashChain.setElementAt(newIndex, hashlast);
/*     */     }
/* 136 */     return newIndex;
/*     */   }
/*     */ 
/*     */   public static void _main(String[] args)
/*     */   {
/* 145 */     String[] word = { "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen", "Twenty", "Twenty-One", "Twenty-Two", "Twenty-Three", "Twenty-Four", "Twenty-Five", "Twenty-Six", "Twenty-Seven", "Twenty-Eight", "Twenty-Nine", "Thirty", "Thirty-One", "Thirty-Two", "Thirty-Three", "Thirty-Four", "Thirty-Five", "Thirty-Six", "Thirty-Seven", "Thirty-Eight", "Thirty-Nine" };
/*     */ 
/* 156 */     DTMStringPool pool = new DTMStringPool();
/*     */ 
/* 158 */     System.out.println("If no complaints are printed below, we passed initial test.");
/*     */ 
/* 160 */     for (int pass = 0; pass <= 1; pass++)
/*     */     {
/* 164 */       for (int i = 0; i < word.length; i++)
/*     */       {
/* 166 */         int j = pool.stringToIndex(word[i]);
/* 167 */         if (j != i) {
/* 168 */           System.out.println("\tMismatch populating pool: assigned " + j + " for create " + i);
/*     */         }
/*     */       }
/*     */ 
/* 172 */       for (i = 0; i < word.length; i++)
/*     */       {
/* 174 */         int j = pool.stringToIndex(word[i]);
/* 175 */         if (j != i) {
/* 176 */           System.out.println("\tMismatch in stringToIndex: returned " + j + " for lookup " + i);
/*     */         }
/*     */       }
/*     */ 
/* 180 */       for (i = 0; i < word.length; i++)
/*     */       {
/* 182 */         String w = pool.indexToString(i);
/* 183 */         if (!word[i].equals(w)) {
/* 184 */           System.out.println("\tMismatch in indexToString: returned" + w + " for lookup " + i);
/*     */         }
/*     */       }
/*     */ 
/* 188 */       pool.removeAllElements();
/*     */ 
/* 190 */       System.out.println("\nPass " + pass + " complete\n");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.DTMStringPool
 * JD-Core Version:    0.6.2
 */