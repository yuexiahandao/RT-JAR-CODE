/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler.util;
/*     */ 
/*     */ import com.sun.org.apache.bcel.internal.generic.Type;
/*     */ import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
/*     */ import com.sun.org.apache.xml.internal.utils.XML11Char;
/*     */ import java.io.PrintStream;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public final class Util
/*     */ {
/*  42 */   private static char filesep = temp.charAt(0);
/*     */ 
/*     */   public static String noExtName(String name)
/*     */   {
/*  46 */     int index = name.lastIndexOf('.');
/*  47 */     return name.substring(0, index >= 0 ? index : name.length());
/*     */   }
/*     */ 
/*     */   public static String baseName(String name)
/*     */   {
/*  55 */     int index = name.lastIndexOf('\\');
/*  56 */     if (index < 0) {
/*  57 */       index = name.lastIndexOf('/');
/*     */     }
/*     */ 
/*  60 */     if (index >= 0) {
/*  61 */       return name.substring(index + 1);
/*     */     }
/*  63 */     int lastColonIndex = name.lastIndexOf(':');
/*  64 */     if (lastColonIndex > 0) {
/*  65 */       return name.substring(lastColonIndex + 1);
/*     */     }
/*  67 */     return name;
/*     */   }
/*     */ 
/*     */   public static String pathName(String name)
/*     */   {
/*  76 */     int index = name.lastIndexOf('/');
/*  77 */     if (index < 0) {
/*  78 */       index = name.lastIndexOf('\\');
/*     */     }
/*  80 */     return name.substring(0, index + 1);
/*     */   }
/*     */ 
/*     */   public static String toJavaName(String name)
/*     */   {
/*  87 */     if (name.length() > 0) {
/*  88 */       StringBuffer result = new StringBuffer();
/*     */ 
/*  90 */       char ch = name.charAt(0);
/*  91 */       result.append(Character.isJavaIdentifierStart(ch) ? ch : '_');
/*     */ 
/*  93 */       int n = name.length();
/*  94 */       for (int i = 1; i < n; i++) {
/*  95 */         ch = name.charAt(i);
/*  96 */         result.append(Character.isJavaIdentifierPart(ch) ? ch : '_');
/*     */       }
/*  98 */       return result.toString();
/*     */     }
/* 100 */     return name;
/*     */   }
/*     */ 
/*     */   public static Type getJCRefType(String signature) {
/* 104 */     return Type.getType(signature);
/*     */   }
/*     */ 
/*     */   public static String internalName(String cname) {
/* 108 */     return cname.replace('.', filesep);
/*     */   }
/*     */ 
/*     */   public static void println(String s) {
/* 112 */     System.out.println(s);
/*     */   }
/*     */ 
/*     */   public static void println(char ch) {
/* 116 */     System.out.println(ch);
/*     */   }
/*     */ 
/*     */   public static void TRACE1() {
/* 120 */     System.out.println("TRACE1");
/*     */   }
/*     */ 
/*     */   public static void TRACE2() {
/* 124 */     System.out.println("TRACE2");
/*     */   }
/*     */ 
/*     */   public static void TRACE3() {
/* 128 */     System.out.println("TRACE3");
/*     */   }
/*     */ 
/*     */   public static String replace(String base, char ch, String str)
/*     */   {
/* 135 */     return base.indexOf(ch) < 0 ? base : replace(base, String.valueOf(ch), new String[] { str });
/*     */   }
/*     */ 
/*     */   public static String replace(String base, String delim, String[] str)
/*     */   {
/* 140 */     int len = base.length();
/* 141 */     StringBuffer result = new StringBuffer();
/*     */ 
/* 143 */     for (int i = 0; i < len; i++) {
/* 144 */       char ch = base.charAt(i);
/* 145 */       int k = delim.indexOf(ch);
/*     */ 
/* 147 */       if (k >= 0) {
/* 148 */         result.append(str[k]);
/*     */       }
/*     */       else {
/* 151 */         result.append(ch);
/*     */       }
/*     */     }
/* 154 */     return result.toString();
/*     */   }
/*     */ 
/*     */   public static String escape(String input)
/*     */   {
/* 161 */     return replace(input, ".-/:", new String[] { "$dot$", "$dash$", "$slash$", "$colon$" });
/*     */   }
/*     */ 
/*     */   public static String getLocalName(String qname)
/*     */   {
/* 166 */     int index = qname.lastIndexOf(":");
/* 167 */     return index > 0 ? qname.substring(index + 1) : qname;
/*     */   }
/*     */ 
/*     */   public static String getPrefix(String qname) {
/* 171 */     int index = qname.lastIndexOf(":");
/* 172 */     return index > 0 ? qname.substring(0, index) : "";
/*     */   }
/*     */ 
/*     */   public static boolean isLiteral(String str)
/*     */   {
/* 180 */     int length = str.length();
/* 181 */     for (int i = 0; i < length - 1; i++) {
/* 182 */       if ((str.charAt(i) == '{') && (str.charAt(i + 1) != '{')) {
/* 183 */         return false;
/*     */       }
/*     */     }
/* 186 */     return true;
/*     */   }
/*     */ 
/*     */   public static boolean isValidQNames(String str)
/*     */   {
/* 193 */     if ((str != null) && (!str.equals(""))) {
/* 194 */       StringTokenizer tokens = new StringTokenizer(str);
/* 195 */       while (tokens.hasMoreTokens()) {
/* 196 */         if (!XML11Char.isXML11ValidQName(tokens.nextToken())) {
/* 197 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 201 */     return true;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  41 */     String temp = SecuritySupport.getSystemProperty("file.separator", "/");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.util.Util
 * JD-Core Version:    0.6.2
 */