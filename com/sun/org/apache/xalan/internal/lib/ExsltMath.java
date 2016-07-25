/*     */ package com.sun.org.apache.xalan.internal.lib;
/*     */ 
/*     */ import com.sun.org.apache.xpath.internal.NodeSet;
/*     */ import org.w3c.dom.Node;
/*     */ import org.w3c.dom.NodeList;
/*     */ 
/*     */ public class ExsltMath extends ExsltBase
/*     */ {
/*  47 */   private static String PI = "3.1415926535897932384626433832795028841971693993751";
/*  48 */   private static String E = "2.71828182845904523536028747135266249775724709369996";
/*  49 */   private static String SQRRT2 = "1.41421356237309504880168872420969807856967187537694";
/*  50 */   private static String LN2 = "0.69314718055994530941723212145817656807550013436025";
/*  51 */   private static String LN10 = "2.302585092994046";
/*  52 */   private static String LOG2E = "1.4426950408889633";
/*  53 */   private static String SQRT1_2 = "0.7071067811865476";
/*     */ 
/*     */   public static double max(NodeList nl)
/*     */   {
/*  73 */     if ((nl == null) || (nl.getLength() == 0)) {
/*  74 */       return (0.0D / 0.0D);
/*     */     }
/*  76 */     double m = -1.797693134862316E+308D;
/*  77 */     for (int i = 0; i < nl.getLength(); i++)
/*     */     {
/*  79 */       Node n = nl.item(i);
/*  80 */       double d = toNumber(n);
/*  81 */       if (Double.isNaN(d))
/*  82 */         return (0.0D / 0.0D);
/*  83 */       if (d > m) {
/*  84 */         m = d;
/*     */       }
/*     */     }
/*  87 */     return m;
/*     */   }
/*     */ 
/*     */   public static double min(NodeList nl)
/*     */   {
/* 108 */     if ((nl == null) || (nl.getLength() == 0)) {
/* 109 */       return (0.0D / 0.0D);
/*     */     }
/* 111 */     double m = 1.7976931348623157E+308D;
/* 112 */     for (int i = 0; i < nl.getLength(); i++)
/*     */     {
/* 114 */       Node n = nl.item(i);
/* 115 */       double d = toNumber(n);
/* 116 */       if (Double.isNaN(d))
/* 117 */         return (0.0D / 0.0D);
/* 118 */       if (d < m) {
/* 119 */         m = d;
/*     */       }
/*     */     }
/* 122 */     return m;
/*     */   }
/*     */ 
/*     */   public static NodeList highest(NodeList nl)
/*     */   {
/* 144 */     double maxValue = max(nl);
/*     */ 
/* 146 */     NodeSet highNodes = new NodeSet();
/* 147 */     highNodes.setShouldCacheNodes(true);
/*     */ 
/* 149 */     if (Double.isNaN(maxValue)) {
/* 150 */       return highNodes;
/*     */     }
/* 152 */     for (int i = 0; i < nl.getLength(); i++)
/*     */     {
/* 154 */       Node n = nl.item(i);
/* 155 */       double d = toNumber(n);
/* 156 */       if (d == maxValue)
/* 157 */         highNodes.addElement(n);
/*     */     }
/* 159 */     return highNodes;
/*     */   }
/*     */ 
/*     */   public static NodeList lowest(NodeList nl)
/*     */   {
/* 181 */     double minValue = min(nl);
/*     */ 
/* 183 */     NodeSet lowNodes = new NodeSet();
/* 184 */     lowNodes.setShouldCacheNodes(true);
/*     */ 
/* 186 */     if (Double.isNaN(minValue)) {
/* 187 */       return lowNodes;
/*     */     }
/* 189 */     for (int i = 0; i < nl.getLength(); i++)
/*     */     {
/* 191 */       Node n = nl.item(i);
/* 192 */       double d = toNumber(n);
/* 193 */       if (d == minValue)
/* 194 */         lowNodes.addElement(n);
/*     */     }
/* 196 */     return lowNodes;
/*     */   }
/*     */ 
/*     */   public static double abs(double num)
/*     */   {
/* 207 */     return Math.abs(num);
/*     */   }
/*     */ 
/*     */   public static double acos(double num)
/*     */   {
/* 218 */     return Math.acos(num);
/*     */   }
/*     */ 
/*     */   public static double asin(double num)
/*     */   {
/* 229 */     return Math.asin(num);
/*     */   }
/*     */ 
/*     */   public static double atan(double num)
/*     */   {
/* 240 */     return Math.atan(num);
/*     */   }
/*     */ 
/*     */   public static double atan2(double num1, double num2)
/*     */   {
/* 252 */     return Math.atan2(num1, num2);
/*     */   }
/*     */ 
/*     */   public static double cos(double num)
/*     */   {
/* 263 */     return Math.cos(num);
/*     */   }
/*     */ 
/*     */   public static double exp(double num)
/*     */   {
/* 274 */     return Math.exp(num);
/*     */   }
/*     */ 
/*     */   public static double log(double num)
/*     */   {
/* 285 */     return Math.log(num);
/*     */   }
/*     */ 
/*     */   public static double power(double num1, double num2)
/*     */   {
/* 297 */     return Math.pow(num1, num2);
/*     */   }
/*     */ 
/*     */   public static double random()
/*     */   {
/* 307 */     return Math.random();
/*     */   }
/*     */ 
/*     */   public static double sin(double num)
/*     */   {
/* 318 */     return Math.sin(num);
/*     */   }
/*     */ 
/*     */   public static double sqrt(double num)
/*     */   {
/* 329 */     return Math.sqrt(num);
/*     */   }
/*     */ 
/*     */   public static double tan(double num)
/*     */   {
/* 340 */     return Math.tan(num);
/*     */   }
/*     */ 
/*     */   public static double constant(String name, double precision)
/*     */   {
/* 361 */     String value = null;
/* 362 */     if (name.equals("PI"))
/* 363 */       value = PI;
/* 364 */     else if (name.equals("E"))
/* 365 */       value = E;
/* 366 */     else if (name.equals("SQRRT2"))
/* 367 */       value = SQRRT2;
/* 368 */     else if (name.equals("LN2"))
/* 369 */       value = LN2;
/* 370 */     else if (name.equals("LN10"))
/* 371 */       value = LN10;
/* 372 */     else if (name.equals("LOG2E"))
/* 373 */       value = LOG2E;
/* 374 */     else if (name.equals("SQRT1_2")) {
/* 375 */       value = SQRT1_2;
/*     */     }
/* 377 */     if (value != null)
/*     */     {
/* 379 */       int bits = new Double(precision).intValue();
/*     */ 
/* 381 */       if (bits <= value.length()) {
/* 382 */         value = value.substring(0, bits);
/*     */       }
/* 384 */       return Double.parseDouble(value);
/*     */     }
/*     */ 
/* 387 */     return (0.0D / 0.0D);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.lib.ExsltMath
 * JD-Core Version:    0.6.2
 */