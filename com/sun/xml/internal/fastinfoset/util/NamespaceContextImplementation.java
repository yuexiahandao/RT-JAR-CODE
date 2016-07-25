/*     */ package com.sun.xml.internal.fastinfoset.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import javax.xml.namespace.NamespaceContext;
/*     */ 
/*     */ public final class NamespaceContextImplementation
/*     */   implements NamespaceContext
/*     */ {
/*  40 */   private static int DEFAULT_SIZE = 8;
/*     */ 
/*  42 */   private String[] prefixes = new String[DEFAULT_SIZE];
/*  43 */   private String[] namespaceURIs = new String[DEFAULT_SIZE];
/*     */   private int namespacePosition;
/*  46 */   private int[] contexts = new int[DEFAULT_SIZE];
/*     */   private int contextPosition;
/*     */   private int currentContext;
/*     */ 
/*     */   public NamespaceContextImplementation()
/*     */   {
/*  52 */     this.prefixes[0] = "xml";
/*  53 */     this.namespaceURIs[0] = "http://www.w3.org/XML/1998/namespace";
/*  54 */     this.prefixes[1] = "xmlns";
/*  55 */     this.namespaceURIs[1] = "http://www.w3.org/2000/xmlns/";
/*     */ 
/*  57 */     this.currentContext = (this.namespacePosition = 2);
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI(String prefix)
/*     */   {
/*  62 */     if (prefix == null) throw new IllegalArgumentException();
/*     */ 
/*  66 */     for (int i = this.namespacePosition - 1; i >= 0; i--) {
/*  67 */       String declaredPrefix = this.prefixes[i];
/*  68 */       if (declaredPrefix.equals(prefix)) {
/*  69 */         return this.namespaceURIs[i];
/*     */       }
/*     */     }
/*     */ 
/*  73 */     return "";
/*     */   }
/*     */ 
/*     */   public String getPrefix(String namespaceURI) {
/*  77 */     if (namespaceURI == null) throw new IllegalArgumentException();
/*     */ 
/*  81 */     for (int i = this.namespacePosition - 1; i >= 0; i--) {
/*  82 */       String declaredNamespaceURI = this.namespaceURIs[i];
/*  83 */       if (declaredNamespaceURI.equals(namespaceURI)) {
/*  84 */         String declaredPrefix = this.prefixes[i];
/*     */ 
/*  87 */         boolean isOutOfScope = false;
/*  88 */         for (int j = i + 1; j < this.namespacePosition; j++) {
/*  89 */           if (declaredPrefix.equals(this.prefixes[j])) {
/*  90 */             isOutOfScope = true;
/*  91 */             break;
/*     */           }
/*     */         }
/*  94 */         if (!isOutOfScope) {
/*  95 */           return declaredPrefix;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */   public String getNonDefaultPrefix(String namespaceURI) {
/* 104 */     if (namespaceURI == null) throw new IllegalArgumentException();
/*     */ 
/* 108 */     for (int i = this.namespacePosition - 1; i >= 0; i--) {
/* 109 */       String declaredNamespaceURI = this.namespaceURIs[i];
/* 110 */       if ((declaredNamespaceURI.equals(namespaceURI)) && (this.prefixes[i].length() > 0))
/*     */       {
/* 112 */         String declaredPrefix = this.prefixes[i];
/*     */ 
/* 115 */         for (i++; i < this.namespacePosition; i++) {
/* 116 */           if (declaredPrefix.equals(this.prefixes[i]))
/* 117 */             return null;
/*     */         }
/* 119 */         return declaredPrefix;
/*     */       }
/*     */     }
/*     */ 
/* 123 */     return null;
/*     */   }
/*     */ 
/*     */   public Iterator getPrefixes(String namespaceURI) {
/* 127 */     if (namespaceURI == null) throw new IllegalArgumentException();
/*     */ 
/* 131 */     List l = new ArrayList();
/*     */ 
/* 133 */     label103: for (int i = this.namespacePosition - 1; i >= 0; i--) {
/* 134 */       String declaredNamespaceURI = this.namespaceURIs[i];
/* 135 */       if (declaredNamespaceURI.equals(namespaceURI)) {
/* 136 */         String declaredPrefix = this.prefixes[i];
/*     */ 
/* 139 */         for (int j = i + 1; j < this.namespacePosition; j++) {
/* 140 */           if (declaredPrefix.equals(this.prefixes[j]))
/*     */             break label103;
/*     */         }
/* 143 */         l.add(declaredPrefix);
/*     */       }
/*     */     }
/*     */ 
/* 147 */     return l.iterator();
/*     */   }
/*     */ 
/*     */   public String getPrefix(int index)
/*     */   {
/* 152 */     return this.prefixes[index];
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI(int index) {
/* 156 */     return this.namespaceURIs[index];
/*     */   }
/*     */ 
/*     */   public int getCurrentContextStartIndex() {
/* 160 */     return this.currentContext;
/*     */   }
/*     */ 
/*     */   public int getCurrentContextEndIndex() {
/* 164 */     return this.namespacePosition;
/*     */   }
/*     */ 
/*     */   public boolean isCurrentContextEmpty() {
/* 168 */     return this.currentContext == this.namespacePosition;
/*     */   }
/*     */ 
/*     */   public void declarePrefix(String prefix, String namespaceURI) {
/* 172 */     prefix = prefix.intern();
/* 173 */     namespaceURI = namespaceURI.intern();
/*     */ 
/* 176 */     if ((prefix == "xml") || (prefix == "xmlns")) {
/* 177 */       return;
/*     */     }
/*     */ 
/* 180 */     for (int i = this.currentContext; i < this.namespacePosition; i++) {
/* 181 */       String declaredPrefix = this.prefixes[i];
/* 182 */       if (declaredPrefix == prefix) {
/* 183 */         this.prefixes[i] = prefix;
/* 184 */         this.namespaceURIs[i] = namespaceURI;
/* 185 */         return;
/*     */       }
/*     */     }
/*     */ 
/* 189 */     if (this.namespacePosition == this.namespaceURIs.length) {
/* 190 */       resizeNamespaces();
/*     */     }
/*     */ 
/* 193 */     this.prefixes[this.namespacePosition] = prefix;
/* 194 */     this.namespaceURIs[(this.namespacePosition++)] = namespaceURI;
/*     */   }
/*     */ 
/*     */   private void resizeNamespaces() {
/* 198 */     int newLength = this.namespaceURIs.length * 3 / 2 + 1;
/*     */ 
/* 200 */     String[] newPrefixes = new String[newLength];
/* 201 */     System.arraycopy(this.prefixes, 0, newPrefixes, 0, this.prefixes.length);
/* 202 */     this.prefixes = newPrefixes;
/*     */ 
/* 204 */     String[] newNamespaceURIs = new String[newLength];
/* 205 */     System.arraycopy(this.namespaceURIs, 0, newNamespaceURIs, 0, this.namespaceURIs.length);
/* 206 */     this.namespaceURIs = newNamespaceURIs;
/*     */   }
/*     */ 
/*     */   public void pushContext() {
/* 210 */     if (this.contextPosition == this.contexts.length)
/* 211 */       resizeContexts();
/*     */     int tmp36_33 = this.namespacePosition; this.currentContext = tmp36_33; this.contexts[(this.contextPosition++)] = tmp36_33;
/*     */   }
/*     */ 
/*     */   private void resizeContexts() {
/* 217 */     int[] newContexts = new int[this.contexts.length * 3 / 2 + 1];
/* 218 */     System.arraycopy(this.contexts, 0, newContexts, 0, this.contexts.length);
/* 219 */     this.contexts = newContexts;
/*     */   }
/*     */ 
/*     */   public void popContext() {
/* 223 */     if (this.contextPosition > 0)
/* 224 */       this.namespacePosition = (this.currentContext = this.contexts[(--this.contextPosition)]);
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 229 */     this.currentContext = (this.namespacePosition = 2);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.fastinfoset.util.NamespaceContextImplementation
 * JD-Core Version:    0.6.2
 */