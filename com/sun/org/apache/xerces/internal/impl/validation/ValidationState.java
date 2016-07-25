/*     */ package com.sun.org.apache.xerces.internal.impl.validation;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.dv.ValidationContext;
/*     */ import com.sun.org.apache.xerces.internal.util.SymbolTable;
/*     */ import com.sun.org.apache.xerces.internal.xni.NamespaceContext;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public class ValidationState
/*     */   implements ValidationContext
/*     */ {
/*  44 */   private boolean fExtraChecking = true;
/*  45 */   private boolean fFacetChecking = true;
/*  46 */   private boolean fNormalize = true;
/*  47 */   private boolean fNamespaces = true;
/*     */ 
/*  49 */   private EntityState fEntityState = null;
/*  50 */   private NamespaceContext fNamespaceContext = null;
/*  51 */   private SymbolTable fSymbolTable = null;
/*  52 */   private Locale fLocale = null;
/*     */   private ArrayList<String> fIdList;
/*     */   private ArrayList<String> fIdRefList;
/*     */ 
/*     */   public void setExtraChecking(boolean newValue)
/*     */   {
/*  61 */     this.fExtraChecking = newValue;
/*     */   }
/*     */ 
/*     */   public void setFacetChecking(boolean newValue) {
/*  65 */     this.fFacetChecking = newValue;
/*     */   }
/*     */ 
/*     */   public void setNormalizationRequired(boolean newValue) {
/*  69 */     this.fNormalize = newValue;
/*     */   }
/*     */ 
/*     */   public void setUsingNamespaces(boolean newValue) {
/*  73 */     this.fNamespaces = newValue;
/*     */   }
/*     */ 
/*     */   public void setEntityState(EntityState state) {
/*  77 */     this.fEntityState = state;
/*     */   }
/*     */ 
/*     */   public void setNamespaceSupport(NamespaceContext namespace) {
/*  81 */     this.fNamespaceContext = namespace;
/*     */   }
/*     */ 
/*     */   public void setSymbolTable(SymbolTable sTable) {
/*  85 */     this.fSymbolTable = sTable;
/*     */   }
/*     */ 
/*     */   public String checkIDRefID()
/*     */   {
/*  93 */     if ((this.fIdList == null) && 
/*  94 */       (this.fIdRefList != null)) {
/*  95 */       return (String)this.fIdRefList.get(0);
/*     */     }
/*     */ 
/*  99 */     if (this.fIdRefList != null)
/*     */     {
/* 101 */       for (int i = 0; i < this.fIdRefList.size(); i++) {
/* 102 */         String key = (String)this.fIdRefList.get(i);
/* 103 */         if (!this.fIdList.contains(key)) {
/* 104 */           return key;
/*     */         }
/*     */       }
/*     */     }
/* 108 */     return null;
/*     */   }
/*     */ 
/*     */   public void reset() {
/* 112 */     this.fExtraChecking = true;
/* 113 */     this.fFacetChecking = true;
/* 114 */     this.fNamespaces = true;
/* 115 */     this.fIdList = null;
/* 116 */     this.fIdRefList = null;
/* 117 */     this.fEntityState = null;
/* 118 */     this.fNamespaceContext = null;
/* 119 */     this.fSymbolTable = null;
/*     */   }
/*     */ 
/*     */   public void resetIDTables()
/*     */   {
/* 129 */     this.fIdList = null;
/* 130 */     this.fIdRefList = null;
/*     */   }
/*     */ 
/*     */   public boolean needExtraChecking()
/*     */   {
/* 139 */     return this.fExtraChecking;
/*     */   }
/*     */ 
/*     */   public boolean needFacetChecking()
/*     */   {
/* 144 */     return this.fFacetChecking;
/*     */   }
/*     */ 
/*     */   public boolean needToNormalize() {
/* 148 */     return this.fNormalize;
/*     */   }
/*     */ 
/*     */   public boolean useNamespaces() {
/* 152 */     return this.fNamespaces;
/*     */   }
/*     */ 
/*     */   public boolean isEntityDeclared(String name)
/*     */   {
/* 157 */     if (this.fEntityState != null) {
/* 158 */       return this.fEntityState.isEntityDeclared(getSymbol(name));
/*     */     }
/* 160 */     return false;
/*     */   }
/*     */   public boolean isEntityUnparsed(String name) {
/* 163 */     if (this.fEntityState != null) {
/* 164 */       return this.fEntityState.isEntityUnparsed(getSymbol(name));
/*     */     }
/* 166 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isIdDeclared(String name)
/*     */   {
/* 171 */     if (this.fIdList == null) return false;
/* 172 */     return this.fIdList.contains(name);
/*     */   }
/*     */   public void addId(String name) {
/* 175 */     if (this.fIdList == null) this.fIdList = new ArrayList();
/* 176 */     this.fIdList.add(name);
/*     */   }
/*     */ 
/*     */   public void addIdRef(String name)
/*     */   {
/* 181 */     if (this.fIdRefList == null) this.fIdRefList = new ArrayList();
/* 182 */     this.fIdRefList.add(name);
/*     */   }
/*     */ 
/*     */   public String getSymbol(String symbol)
/*     */   {
/* 187 */     if (this.fSymbolTable != null) {
/* 188 */       return this.fSymbolTable.addSymbol(symbol);
/*     */     }
/*     */ 
/* 193 */     return symbol.intern();
/*     */   }
/*     */ 
/*     */   public String getURI(String prefix) {
/* 197 */     if (this.fNamespaceContext != null) {
/* 198 */       return this.fNamespaceContext.getURI(prefix);
/*     */     }
/* 200 */     return null;
/*     */   }
/*     */ 
/*     */   public void setLocale(Locale locale)
/*     */   {
/* 206 */     this.fLocale = locale;
/*     */   }
/*     */ 
/*     */   public Locale getLocale() {
/* 210 */     return this.fLocale;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.validation.ValidationState
 * JD-Core Version:    0.6.2
 */