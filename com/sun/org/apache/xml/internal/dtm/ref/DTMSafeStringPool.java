/*    */ package com.sun.org.apache.xml.internal.dtm.ref;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ 
/*    */ public class DTMSafeStringPool extends DTMStringPool
/*    */ {
/*    */   public synchronized void removeAllElements()
/*    */   {
/* 39 */     super.removeAllElements();
/*    */   }
/*    */ 
/*    */   public synchronized String indexToString(int i)
/*    */     throws ArrayIndexOutOfBoundsException
/*    */   {
/* 49 */     return super.indexToString(i);
/*    */   }
/*    */ 
/*    */   public synchronized int stringToIndex(String s)
/*    */   {
/* 55 */     return super.stringToIndex(s);
/*    */   }
/*    */ 
/*    */   public static void _main(String[] args)
/*    */   {
/* 64 */     String[] word = { "Zero", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Nineteen", "Twenty", "Twenty-One", "Twenty-Two", "Twenty-Three", "Twenty-Four", "Twenty-Five", "Twenty-Six", "Twenty-Seven", "Twenty-Eight", "Twenty-Nine", "Thirty", "Thirty-One", "Thirty-Two", "Thirty-Three", "Thirty-Four", "Thirty-Five", "Thirty-Six", "Thirty-Seven", "Thirty-Eight", "Thirty-Nine" };
/*    */ 
/* 75 */     DTMStringPool pool = new DTMSafeStringPool();
/*    */ 
/* 77 */     System.out.println("If no complaints are printed below, we passed initial test.");
/*    */ 
/* 79 */     for (int pass = 0; pass <= 1; pass++)
/*    */     {
/* 83 */       for (int i = 0; i < word.length; i++)
/*    */       {
/* 85 */         int j = pool.stringToIndex(word[i]);
/* 86 */         if (j != i) {
/* 87 */           System.out.println("\tMismatch populating pool: assigned " + j + " for create " + i);
/*    */         }
/*    */       }
/*    */ 
/* 91 */       for (i = 0; i < word.length; i++)
/*    */       {
/* 93 */         int j = pool.stringToIndex(word[i]);
/* 94 */         if (j != i) {
/* 95 */           System.out.println("\tMismatch in stringToIndex: returned " + j + " for lookup " + i);
/*    */         }
/*    */       }
/*    */ 
/* 99 */       for (i = 0; i < word.length; i++)
/*    */       {
/* 101 */         String w = pool.indexToString(i);
/* 102 */         if (!word[i].equals(w)) {
/* 103 */           System.out.println("\tMismatch in indexToString: returned" + w + " for lookup " + i);
/*    */         }
/*    */       }
/*    */ 
/* 107 */       pool.removeAllElements();
/*    */ 
/* 109 */       System.out.println("\nPass " + pass + " complete\n");
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.ref.DTMSafeStringPool
 * JD-Core Version:    0.6.2
 */