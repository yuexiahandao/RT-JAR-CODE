/*     */ package com.sun.org.apache.xml.internal.security.c14n.helper;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.util.Comparator;
/*     */ import org.w3c.dom.Attr;
/*     */ 
/*     */ public class AttrCompare
/*     */   implements Comparator, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7113259629930576230L;
/*     */   private static final int ATTR0_BEFORE_ATTR1 = -1;
/*     */   private static final int ATTR1_BEFORE_ATTR0 = 1;
/*     */   private static final String XMLNS = "http://www.w3.org/2000/xmlns/";
/*     */ 
/*     */   public int compare(Object paramObject1, Object paramObject2)
/*     */   {
/*  73 */     Attr localAttr1 = (Attr)paramObject1;
/*  74 */     Attr localAttr2 = (Attr)paramObject2;
/*  75 */     String str1 = localAttr1.getNamespaceURI();
/*  76 */     String str2 = localAttr2.getNamespaceURI();
/*     */ 
/*  78 */     int i = "http://www.w3.org/2000/xmlns/" == str1 ? 1 : 0;
/*  79 */     int j = "http://www.w3.org/2000/xmlns/" == str2 ? 1 : 0;
/*     */     String str3;
/*     */     String str4;
/*  81 */     if (i != 0) {
/*  82 */       if (j != 0)
/*     */       {
/*  84 */         str3 = localAttr1.getLocalName();
/*  85 */         str4 = localAttr2.getLocalName();
/*     */ 
/*  87 */         if (str3.equals("xmlns")) {
/*  88 */           str3 = "";
/*     */         }
/*     */ 
/*  91 */         if (str4.equals("xmlns")) {
/*  92 */           str4 = "";
/*     */         }
/*     */ 
/*  95 */         return str3.compareTo(str4);
/*     */       }
/*     */ 
/*  98 */       return -1;
/*     */     }
/*     */ 
/* 101 */     if (j != 0)
/*     */     {
/* 103 */       return 1;
/*     */     }
/*     */ 
/* 107 */     if (str1 == null) {
/* 108 */       if (str2 == null) {
/* 109 */         str3 = localAttr1.getName();
/* 110 */         str4 = localAttr2.getName();
/* 111 */         return str3.compareTo(str4);
/*     */       }
/* 113 */       return -1;
/*     */     }
/*     */ 
/* 116 */     if (str2 == null) {
/* 117 */       return 1;
/*     */     }
/*     */ 
/* 120 */     int k = str1.compareTo(str2);
/* 121 */     if (k != 0) {
/* 122 */       return k;
/*     */     }
/*     */ 
/* 125 */     return localAttr1.getLocalName().compareTo(localAttr2.getLocalName());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.security.c14n.helper.AttrCompare
 * JD-Core Version:    0.6.2
 */