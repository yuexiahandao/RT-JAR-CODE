/*     */ package com.sun.xml.internal.stream.buffer.stax;
/*     */ 
/*     */ import com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx;
/*     */ import com.sun.xml.internal.org.jvnet.staxex.NamespaceContextEx.Binding;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public final class NamespaceContexHelper
/*     */   implements NamespaceContextEx
/*     */ {
/*  63 */   private static int DEFAULT_SIZE = 8;
/*     */ 
/*  66 */   private String[] prefixes = new String[DEFAULT_SIZE];
/*     */ 
/*  68 */   private String[] namespaceURIs = new String[DEFAULT_SIZE];
/*     */   private int namespacePosition;
/*  73 */   private int[] contexts = new int[DEFAULT_SIZE];
/*     */   private int contextPosition;
/*     */   private int currentContext;
/*     */ 
/*     */   public NamespaceContexHelper()
/*     */   {
/*  86 */     this.prefixes[0] = "xml";
/*  87 */     this.namespaceURIs[0] = "http://www.w3.org/XML/1998/namespace";
/*  88 */     this.prefixes[1] = "xmlns";
/*  89 */     this.namespaceURIs[1] = "http://www.w3.org/2000/xmlns/";
/*     */ 
/*  91 */     this.currentContext = (this.namespacePosition = 2);
/*     */   }
/*     */ 
/*     */   public String getNamespaceURI(String prefix)
/*     */   {
/*  98 */     if (prefix == null) throw new IllegalArgumentException();
/*     */ 
/* 100 */     prefix = prefix.intern();
/*     */ 
/* 102 */     for (int i = this.namespacePosition - 1; i >= 0; i--) {
/* 103 */       String declaredPrefix = this.prefixes[i];
/* 104 */       if (declaredPrefix == prefix) {
/* 105 */         return this.namespaceURIs[i];
/*     */       }
/*     */     }
/*     */ 
/* 109 */     return "";
/*     */   }
/*     */ 
/*     */   public String getPrefix(String namespaceURI) {
/* 113 */     if (namespaceURI == null) throw new IllegalArgumentException();
/*     */ 
/* 115 */     for (int i = this.namespacePosition - 1; i >= 0; i--) {
/* 116 */       String declaredNamespaceURI = this.namespaceURIs[i];
/* 117 */       if ((declaredNamespaceURI == namespaceURI) || (declaredNamespaceURI.equals(namespaceURI))) {
/* 118 */         String declaredPrefix = this.prefixes[i];
/*     */ 
/* 121 */         for (i++; i < this.namespacePosition; i++) {
/* 122 */           if (declaredPrefix == this.prefixes[i])
/* 123 */             return null;
/*     */         }
/* 125 */         return declaredPrefix;
/*     */       }
/*     */     }
/*     */ 
/* 129 */     return null;
/*     */   }
/*     */ 
/*     */   public Iterator getPrefixes(String namespaceURI) {
/* 133 */     if (namespaceURI == null) throw new IllegalArgumentException();
/*     */ 
/* 135 */     List l = new ArrayList();
/*     */ 
/* 137 */     label106: for (int i = this.namespacePosition - 1; i >= 0; i--) {
/* 138 */       String declaredNamespaceURI = this.namespaceURIs[i];
/* 139 */       if ((declaredNamespaceURI == namespaceURI) || (declaredNamespaceURI.equals(namespaceURI))) {
/* 140 */         String declaredPrefix = this.prefixes[i];
/*     */ 
/* 143 */         for (int j = i + 1; j < this.namespacePosition; j++) {
/* 144 */           if (declaredPrefix == this.prefixes[j])
/*     */             break label106;
/*     */         }
/* 147 */         l.add(declaredPrefix);
/*     */       }
/*     */     }
/*     */ 
/* 151 */     return l.iterator();
/*     */   }
/*     */ 
/*     */   public Iterator<NamespaceContextEx.Binding> iterator()
/*     */   {
/* 157 */     if (this.namespacePosition == 2) {
/* 158 */       return Collections.EMPTY_LIST.iterator();
/*     */     }
/* 160 */     List namespaces = new ArrayList(this.namespacePosition);
/*     */ 
/* 163 */     for (int i = this.namespacePosition - 1; i >= 2; i--) {
/* 164 */       String declaredPrefix = this.prefixes[i];
/*     */ 
/* 167 */       for (int j = i + 1; (j < this.namespacePosition) && 
/* 168 */         (declaredPrefix != this.prefixes[j]); j++)
/*     */       {
/* 171 */         namespaces.add(new NamespaceBindingImpl(i));
/*     */       }
/*     */     }
/*     */ 
/* 175 */     return namespaces.iterator();
/*     */   }
/*     */ 
/*     */   public void declareDefaultNamespace(String namespaceURI)
/*     */   {
/* 200 */     declareNamespace("", namespaceURI);
/*     */   }
/*     */ 
/*     */   public void declareNamespace(String prefix, String namespaceURI)
/*     */   {
/* 222 */     if (prefix == null) throw new IllegalArgumentException();
/*     */ 
/* 224 */     prefix = prefix.intern();
/*     */ 
/* 226 */     if ((prefix == "xml") || (prefix == "xmlns")) {
/* 227 */       return;
/*     */     }
/*     */ 
/* 230 */     if (namespaceURI != null) {
/* 231 */       namespaceURI = namespaceURI.intern();
/*     */     }
/* 233 */     if (this.namespacePosition == this.namespaceURIs.length) {
/* 234 */       resizeNamespaces();
/*     */     }
/*     */ 
/* 237 */     this.prefixes[this.namespacePosition] = prefix;
/* 238 */     this.namespaceURIs[(this.namespacePosition++)] = namespaceURI;
/*     */   }
/*     */ 
/*     */   private void resizeNamespaces() {
/* 242 */     int newLength = this.namespaceURIs.length * 3 / 2 + 1;
/*     */ 
/* 244 */     String[] newPrefixes = new String[newLength];
/* 245 */     System.arraycopy(this.prefixes, 0, newPrefixes, 0, this.prefixes.length);
/* 246 */     this.prefixes = newPrefixes;
/*     */ 
/* 248 */     String[] newNamespaceURIs = new String[newLength];
/* 249 */     System.arraycopy(this.namespaceURIs, 0, newNamespaceURIs, 0, this.namespaceURIs.length);
/* 250 */     this.namespaceURIs = newNamespaceURIs;
/*     */   }
/*     */ 
/*     */   public void pushContext()
/*     */   {
/* 257 */     if (this.contextPosition == this.contexts.length)
/* 258 */       resizeContexts();
/*     */     int tmp36_33 = this.namespacePosition; this.currentContext = tmp36_33; this.contexts[(this.contextPosition++)] = tmp36_33;
/*     */   }
/*     */ 
/*     */   private void resizeContexts() {
/* 264 */     int[] newContexts = new int[this.contexts.length * 3 / 2 + 1];
/* 265 */     System.arraycopy(this.contexts, 0, newContexts, 0, this.contexts.length);
/* 266 */     this.contexts = newContexts;
/*     */   }
/*     */ 
/*     */   public void popContext()
/*     */   {
/* 276 */     if (this.contextPosition > 0)
/* 277 */       this.namespacePosition = (this.currentContext = this.contexts[(--this.contextPosition)]);
/*     */   }
/*     */ 
/*     */   public void resetContexts()
/*     */   {
/* 287 */     this.currentContext = (this.namespacePosition = 2);
/*     */   }
/*     */ 
/*     */   private final class NamespaceBindingImpl
/*     */     implements NamespaceContextEx.Binding
/*     */   {
/*     */     int index;
/*     */ 
/*     */     NamespaceBindingImpl(int index)
/*     */     {
/* 182 */       this.index = index;
/*     */     }
/*     */ 
/*     */     public String getPrefix() {
/* 186 */       return NamespaceContexHelper.this.prefixes[this.index];
/*     */     }
/*     */ 
/*     */     public String getNamespaceURI() {
/* 190 */       return NamespaceContexHelper.this.namespaceURIs[this.index];
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.stream.buffer.stax.NamespaceContexHelper
 * JD-Core Version:    0.6.2
 */