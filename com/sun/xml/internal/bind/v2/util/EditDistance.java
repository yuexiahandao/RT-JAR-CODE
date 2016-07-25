/*     */ package com.sun.xml.internal.bind.v2.util;
/*     */ 
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ 
/*     */ public class EditDistance
/*     */ {
/*     */   private int[] cost;
/*     */   private int[] back;
/*     */   private final String a;
/*     */   private final String b;
/*     */ 
/*     */   public static int editDistance(String a, String b)
/*     */   {
/*  50 */     return new EditDistance(a, b).calc();
/*     */   }
/*     */ 
/*     */   public static String findNearest(String key, String[] group)
/*     */   {
/*  60 */     return findNearest(key, Arrays.asList(group));
/*     */   }
/*     */ 
/*     */   public static String findNearest(String key, Collection<String> group)
/*     */   {
/*  70 */     int c = 2147483647;
/*  71 */     String r = null;
/*     */ 
/*  73 */     for (String s : group) {
/*  74 */       int ed = editDistance(key, s);
/*  75 */       if (c > ed) {
/*  76 */         c = ed;
/*  77 */         r = s;
/*     */       }
/*     */     }
/*  80 */     return r;
/*     */   }
/*     */ 
/*     */   private EditDistance(String a, String b)
/*     */   {
/*  92 */     this.a = a;
/*  93 */     this.b = b;
/*  94 */     this.cost = new int[a.length() + 1];
/*  95 */     this.back = new int[a.length() + 1];
/*     */ 
/*  97 */     for (int i = 0; i <= a.length(); i++)
/*  98 */       this.cost[i] = i;
/*     */   }
/*     */ 
/*     */   private void flip()
/*     */   {
/* 105 */     int[] t = this.cost;
/* 106 */     this.cost = this.back;
/* 107 */     this.back = t;
/*     */   }
/*     */ 
/*     */   private int min(int a, int b, int c) {
/* 111 */     return Math.min(a, Math.min(b, c));
/*     */   }
/*     */ 
/*     */   private int calc() {
/* 115 */     for (int j = 0; j < this.b.length(); j++) {
/* 116 */       flip();
/* 117 */       this.cost[0] = (j + 1);
/* 118 */       for (int i = 0; i < this.a.length(); i++) {
/* 119 */         int match = this.a.charAt(i) == this.b.charAt(j) ? 0 : 1;
/* 120 */         this.cost[(i + 1)] = min(this.back[i] + match, this.cost[i] + 1, this.back[(i + 1)] + 1);
/*     */       }
/*     */     }
/* 123 */     return this.cost[this.a.length()];
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.util.EditDistance
 * JD-Core Version:    0.6.2
 */