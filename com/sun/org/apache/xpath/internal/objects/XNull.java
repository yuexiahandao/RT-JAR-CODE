/*     */ package com.sun.org.apache.xpath.internal.objects;
/*     */ 
/*     */ import com.sun.org.apache.xpath.internal.XPathContext;
/*     */ 
/*     */ public class XNull extends XNodeSet
/*     */ {
/*     */   static final long serialVersionUID = -6841683711458983005L;
/*     */ 
/*     */   public int getType()
/*     */   {
/*  52 */     return -1;
/*     */   }
/*     */ 
/*     */   public String getTypeString()
/*     */   {
/*  63 */     return "#CLASS_NULL";
/*     */   }
/*     */ 
/*     */   public double num()
/*     */   {
/*  74 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   public boolean bool()
/*     */   {
/*  84 */     return false;
/*     */   }
/*     */ 
/*     */   public String str()
/*     */   {
/*  94 */     return "";
/*     */   }
/*     */ 
/*     */   public int rtf(XPathContext support)
/*     */   {
/* 108 */     return -1;
/*     */   }
/*     */ 
/*     */   public boolean equals(XObject obj2)
/*     */   {
/* 130 */     return obj2.getType() == -1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.objects.XNull
 * JD-Core Version:    0.6.2
 */