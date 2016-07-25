/*     */ package com.sun.org.apache.xerces.internal.impl.xs;
/*     */ 
/*     */ import com.sun.org.apache.xerces.internal.impl.xs.util.XSGrammarPool;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.Grammar;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XMLGrammarDescription;
/*     */ import com.sun.org.apache.xerces.internal.xni.grammars.XSGrammar;
/*     */ import com.sun.org.apache.xerces.internal.xni.parser.XMLInputSource;
/*     */ import com.sun.org.apache.xerces.internal.xs.LSInputList;
/*     */ import com.sun.org.apache.xerces.internal.xs.StringList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSLoader;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSModel;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSNamedMap;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSObjectList;
/*     */ import com.sun.org.apache.xerces.internal.xs.XSTypeDefinition;
/*     */ import org.w3c.dom.DOMConfiguration;
/*     */ import org.w3c.dom.DOMException;
/*     */ import org.w3c.dom.DOMStringList;
/*     */ import org.w3c.dom.ls.LSInput;
/*     */ 
/*     */ public final class XSLoaderImpl
/*     */   implements XSLoader, DOMConfiguration
/*     */ {
/*  56 */   private final XSGrammarPool fGrammarPool = new XSGrammarMerger();
/*     */ 
/*  59 */   private final XMLSchemaLoader fSchemaLoader = new XMLSchemaLoader();
/*     */ 
/*     */   public XSLoaderImpl()
/*     */   {
/*  65 */     this.fSchemaLoader.setProperty("http://apache.org/xml/properties/internal/grammar-pool", this.fGrammarPool);
/*     */   }
/*     */ 
/*     */   public DOMConfiguration getConfig()
/*     */   {
/*  93 */     return this;
/*     */   }
/*     */ 
/*     */   public XSModel loadURIList(StringList uriList)
/*     */   {
/* 104 */     int length = uriList.getLength();
/*     */     try {
/* 106 */       this.fGrammarPool.clear();
/* 107 */       for (int i = 0; i < length; i++) {
/* 108 */         this.fSchemaLoader.loadGrammar(new XMLInputSource(null, uriList.item(i), null));
/*     */       }
/* 110 */       return this.fGrammarPool.toXSModel();
/*     */     }
/*     */     catch (Exception e) {
/* 113 */       this.fSchemaLoader.reportDOMFatalError(e);
/* 114 */     }return null;
/*     */   }
/*     */ 
/*     */   public XSModel loadInputList(LSInputList is)
/*     */   {
/* 126 */     int length = is.getLength();
/*     */     try {
/* 128 */       this.fGrammarPool.clear();
/* 129 */       for (int i = 0; i < length; i++) {
/* 130 */         this.fSchemaLoader.loadGrammar(this.fSchemaLoader.dom2xmlInputSource(is.item(i)));
/*     */       }
/* 132 */       return this.fGrammarPool.toXSModel();
/*     */     }
/*     */     catch (Exception e) {
/* 135 */       this.fSchemaLoader.reportDOMFatalError(e);
/* 136 */     }return null;
/*     */   }
/*     */ 
/*     */   public XSModel loadURI(String uri)
/*     */   {
/*     */     try
/*     */     {
/* 149 */       this.fGrammarPool.clear();
/* 150 */       return ((XSGrammar)this.fSchemaLoader.loadGrammar(new XMLInputSource(null, uri, null))).toXSModel();
/*     */     }
/*     */     catch (Exception e) {
/* 153 */       this.fSchemaLoader.reportDOMFatalError(e);
/* 154 */     }return null;
/*     */   }
/*     */ 
/*     */   public XSModel load(LSInput is)
/*     */   {
/*     */     try
/*     */     {
/* 167 */       this.fGrammarPool.clear();
/* 168 */       return ((XSGrammar)this.fSchemaLoader.loadGrammar(this.fSchemaLoader.dom2xmlInputSource(is))).toXSModel();
/*     */     }
/*     */     catch (Exception e) {
/* 171 */       this.fSchemaLoader.reportDOMFatalError(e);
/* 172 */     }return null;
/*     */   }
/*     */ 
/*     */   public void setParameter(String name, Object value)
/*     */     throws DOMException
/*     */   {
/* 180 */     this.fSchemaLoader.setParameter(name, value);
/*     */   }
/*     */ 
/*     */   public Object getParameter(String name)
/*     */     throws DOMException
/*     */   {
/* 187 */     return this.fSchemaLoader.getParameter(name);
/*     */   }
/*     */ 
/*     */   public boolean canSetParameter(String name, Object value)
/*     */   {
/* 194 */     return this.fSchemaLoader.canSetParameter(name, value);
/*     */   }
/*     */ 
/*     */   public DOMStringList getParameterNames()
/*     */   {
/* 201 */     return this.fSchemaLoader.getParameterNames();
/*     */   }
/*     */ 
/*     */   private static final class XSGrammarMerger extends XSGrammarPool
/*     */   {
/*     */     public void putGrammar(Grammar grammar)
/*     */     {
/* 214 */       SchemaGrammar cachedGrammar = toSchemaGrammar(super.getGrammar(grammar.getGrammarDescription()));
/*     */ 
/* 216 */       if (cachedGrammar != null) {
/* 217 */         SchemaGrammar newGrammar = toSchemaGrammar(grammar);
/* 218 */         if (newGrammar != null)
/* 219 */           mergeSchemaGrammars(cachedGrammar, newGrammar);
/*     */       }
/*     */       else
/*     */       {
/* 223 */         super.putGrammar(grammar);
/*     */       }
/*     */     }
/*     */ 
/*     */     private SchemaGrammar toSchemaGrammar(Grammar grammar) {
/* 228 */       return (grammar instanceof SchemaGrammar) ? (SchemaGrammar)grammar : null;
/*     */     }
/*     */ 
/*     */     private void mergeSchemaGrammars(SchemaGrammar cachedGrammar, SchemaGrammar newGrammar)
/*     */     {
/* 234 */       XSNamedMap map = newGrammar.getComponents((short)2);
/* 235 */       int length = map.getLength();
/* 236 */       for (int i = 0; i < length; i++) {
/* 237 */         XSElementDecl decl = (XSElementDecl)map.item(i);
/* 238 */         if (cachedGrammar.getGlobalElementDecl(decl.getName()) == null) {
/* 239 */           cachedGrammar.addGlobalElementDecl(decl);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 244 */       map = newGrammar.getComponents((short)1);
/* 245 */       length = map.getLength();
/* 246 */       for (int i = 0; i < length; i++) {
/* 247 */         XSAttributeDecl decl = (XSAttributeDecl)map.item(i);
/* 248 */         if (cachedGrammar.getGlobalAttributeDecl(decl.getName()) == null) {
/* 249 */           cachedGrammar.addGlobalAttributeDecl(decl);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 254 */       map = newGrammar.getComponents((short)3);
/* 255 */       length = map.getLength();
/* 256 */       for (int i = 0; i < length; i++) {
/* 257 */         XSTypeDefinition decl = (XSTypeDefinition)map.item(i);
/* 258 */         if (cachedGrammar.getGlobalTypeDecl(decl.getName()) == null) {
/* 259 */           cachedGrammar.addGlobalTypeDecl(decl);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 264 */       map = newGrammar.getComponents((short)5);
/* 265 */       length = map.getLength();
/* 266 */       for (int i = 0; i < length; i++) {
/* 267 */         XSAttributeGroupDecl decl = (XSAttributeGroupDecl)map.item(i);
/* 268 */         if (cachedGrammar.getGlobalAttributeGroupDecl(decl.getName()) == null) {
/* 269 */           cachedGrammar.addGlobalAttributeGroupDecl(decl);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 274 */       map = newGrammar.getComponents((short)7);
/* 275 */       length = map.getLength();
/* 276 */       for (int i = 0; i < length; i++) {
/* 277 */         XSGroupDecl decl = (XSGroupDecl)map.item(i);
/* 278 */         if (cachedGrammar.getGlobalGroupDecl(decl.getName()) == null) {
/* 279 */           cachedGrammar.addGlobalGroupDecl(decl);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 284 */       map = newGrammar.getComponents((short)11);
/* 285 */       length = map.getLength();
/* 286 */       for (int i = 0; i < length; i++) {
/* 287 */         XSNotationDecl decl = (XSNotationDecl)map.item(i);
/* 288 */         if (cachedGrammar.getGlobalNotationDecl(decl.getName()) == null) {
/* 289 */           cachedGrammar.addGlobalNotationDecl(decl);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 298 */       XSObjectList annotations = newGrammar.getAnnotations();
/* 299 */       length = annotations.getLength();
/* 300 */       for (int i = 0; i < length; i++)
/* 301 */         cachedGrammar.addAnnotation((XSAnnotationImpl)annotations.item(i));
/*     */     }
/*     */ 
/*     */     public boolean containsGrammar(XMLGrammarDescription desc)
/*     */     {
/* 307 */       return false;
/*     */     }
/*     */ 
/*     */     public Grammar getGrammar(XMLGrammarDescription desc) {
/* 311 */       return null;
/*     */     }
/*     */ 
/*     */     public Grammar retrieveGrammar(XMLGrammarDescription desc) {
/* 315 */       return null;
/*     */     }
/*     */ 
/*     */     public Grammar[] retrieveInitialGrammarSet(String grammarType) {
/* 319 */       return new Grammar[0];
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xerces.internal.impl.xs.XSLoaderImpl
 * JD-Core Version:    0.6.2
 */