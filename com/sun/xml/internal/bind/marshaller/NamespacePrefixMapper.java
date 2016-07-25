/*     */ package com.sun.xml.internal.bind.marshaller;
/*     */ 
/*     */ public abstract class NamespacePrefixMapper
/*     */ {
/*  59 */   private static final String[] EMPTY_STRING = new String[0];
/*     */ 
/*     */   public abstract String getPreferredPrefix(String paramString1, String paramString2, boolean paramBoolean);
/*     */ 
/*     */   public String[] getPreDeclaredNamespaceUris()
/*     */   {
/* 178 */     return EMPTY_STRING;
/*     */   }
/*     */ 
/*     */   public String[] getPreDeclaredNamespaceUris2()
/*     */   {
/* 203 */     return EMPTY_STRING;
/*     */   }
/*     */ 
/*     */   public String[] getContextualNamespaceDecls()
/*     */   {
/* 254 */     return EMPTY_STRING;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.marshaller.NamespacePrefixMapper
 * JD-Core Version:    0.6.2
 */