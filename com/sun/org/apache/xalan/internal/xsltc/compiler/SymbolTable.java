/*     */ package com.sun.org.apache.xalan.internal.xsltc.compiler;
/*     */ 
/*     */ import com.sun.org.apache.xalan.internal.xsltc.compiler.util.MethodType;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Stack;
/*     */ import java.util.StringTokenizer;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class SymbolTable
/*     */ {
/*  41 */   private final Hashtable _stylesheets = new Hashtable();
/*  42 */   private final Hashtable _primops = new Hashtable();
/*     */ 
/*  45 */   private Hashtable _variables = null;
/*  46 */   private Hashtable _templates = null;
/*  47 */   private Hashtable _attributeSets = null;
/*  48 */   private Hashtable _aliases = null;
/*  49 */   private Hashtable _excludedURI = null;
/*  50 */   private Stack _excludedURIStack = null;
/*  51 */   private Hashtable _decimalFormats = null;
/*  52 */   private Hashtable _keys = null;
/*     */ 
/* 160 */   private int _nsCounter = 0;
/*     */ 
/* 169 */   private SyntaxTreeNode _current = null;
/*     */ 
/*     */   public DecimalFormatting getDecimalFormatting(QName name)
/*     */   {
/*  55 */     if (this._decimalFormats == null) return null;
/*  56 */     return (DecimalFormatting)this._decimalFormats.get(name);
/*     */   }
/*     */ 
/*     */   public void addDecimalFormatting(QName name, DecimalFormatting symbols) {
/*  60 */     if (this._decimalFormats == null) this._decimalFormats = new Hashtable();
/*  61 */     this._decimalFormats.put(name, symbols);
/*     */   }
/*     */ 
/*     */   public Key getKey(QName name) {
/*  65 */     if (this._keys == null) return null;
/*  66 */     return (Key)this._keys.get(name);
/*     */   }
/*     */ 
/*     */   public void addKey(QName name, Key key) {
/*  70 */     if (this._keys == null) this._keys = new Hashtable();
/*  71 */     this._keys.put(name, key);
/*     */   }
/*     */ 
/*     */   public Stylesheet addStylesheet(QName name, Stylesheet node) {
/*  75 */     return (Stylesheet)this._stylesheets.put(name, node);
/*     */   }
/*     */ 
/*     */   public Stylesheet lookupStylesheet(QName name) {
/*  79 */     return (Stylesheet)this._stylesheets.get(name);
/*     */   }
/*     */ 
/*     */   public Template addTemplate(Template template) {
/*  83 */     QName name = template.getName();
/*  84 */     if (this._templates == null) this._templates = new Hashtable();
/*  85 */     return (Template)this._templates.put(name, template);
/*     */   }
/*     */ 
/*     */   public Template lookupTemplate(QName name) {
/*  89 */     if (this._templates == null) return null;
/*  90 */     return (Template)this._templates.get(name);
/*     */   }
/*     */ 
/*     */   public Variable addVariable(Variable variable) {
/*  94 */     if (this._variables == null) this._variables = new Hashtable();
/*  95 */     String name = variable.getName().getStringRep();
/*  96 */     return (Variable)this._variables.put(name, variable);
/*     */   }
/*     */ 
/*     */   public Param addParam(Param parameter) {
/* 100 */     if (this._variables == null) this._variables = new Hashtable();
/* 101 */     String name = parameter.getName().getStringRep();
/* 102 */     return (Param)this._variables.put(name, parameter);
/*     */   }
/*     */ 
/*     */   public Variable lookupVariable(QName qname) {
/* 106 */     if (this._variables == null) return null;
/* 107 */     String name = qname.getStringRep();
/* 108 */     Object obj = this._variables.get(name);
/* 109 */     return (obj instanceof Variable) ? (Variable)obj : null;
/*     */   }
/*     */ 
/*     */   public Param lookupParam(QName qname) {
/* 113 */     if (this._variables == null) return null;
/* 114 */     String name = qname.getStringRep();
/* 115 */     Object obj = this._variables.get(name);
/* 116 */     return (obj instanceof Param) ? (Param)obj : null;
/*     */   }
/*     */ 
/*     */   public SyntaxTreeNode lookupName(QName qname) {
/* 120 */     if (this._variables == null) return null;
/* 121 */     String name = qname.getStringRep();
/* 122 */     return (SyntaxTreeNode)this._variables.get(name);
/*     */   }
/*     */ 
/*     */   public AttributeSet addAttributeSet(AttributeSet atts) {
/* 126 */     if (this._attributeSets == null) this._attributeSets = new Hashtable();
/* 127 */     return (AttributeSet)this._attributeSets.put(atts.getName(), atts);
/*     */   }
/*     */ 
/*     */   public AttributeSet lookupAttributeSet(QName name) {
/* 131 */     if (this._attributeSets == null) return null;
/* 132 */     return (AttributeSet)this._attributeSets.get(name);
/*     */   }
/*     */ 
/*     */   public void addPrimop(String name, MethodType mtype)
/*     */   {
/* 141 */     Vector methods = (Vector)this._primops.get(name);
/* 142 */     if (methods == null) {
/* 143 */       this._primops.put(name, methods = new Vector());
/*     */     }
/* 145 */     methods.addElement(mtype);
/*     */   }
/*     */ 
/*     */   public Vector lookupPrimop(String name)
/*     */   {
/* 153 */     return (Vector)this._primops.get(name);
/*     */   }
/*     */ 
/*     */   public String generateNamespacePrefix()
/*     */   {
/* 163 */     return "ns" + this._nsCounter++;
/*     */   }
/*     */ 
/*     */   public void setCurrentNode(SyntaxTreeNode node)
/*     */   {
/* 172 */     this._current = node;
/*     */   }
/*     */ 
/*     */   public String lookupNamespace(String prefix) {
/* 176 */     if (this._current == null) return "";
/* 177 */     return this._current.lookupNamespace(prefix);
/*     */   }
/*     */ 
/*     */   public void addPrefixAlias(String prefix, String alias)
/*     */   {
/* 184 */     if (this._aliases == null) this._aliases = new Hashtable();
/* 185 */     this._aliases.put(prefix, alias);
/*     */   }
/*     */ 
/*     */   public String lookupPrefixAlias(String prefix)
/*     */   {
/* 192 */     if (this._aliases == null) return null;
/* 193 */     return (String)this._aliases.get(prefix);
/*     */   }
/*     */ 
/*     */   public void excludeURI(String uri)
/*     */   {
/* 202 */     if (uri == null) return;
/*     */ 
/* 205 */     if (this._excludedURI == null) this._excludedURI = new Hashtable();
/*     */ 
/* 208 */     Integer refcnt = (Integer)this._excludedURI.get(uri);
/* 209 */     if (refcnt == null)
/* 210 */       refcnt = new Integer(1);
/*     */     else
/* 212 */       refcnt = new Integer(refcnt.intValue() + 1);
/* 213 */     this._excludedURI.put(uri, refcnt);
/*     */   }
/*     */ 
/*     */   public void excludeNamespaces(String prefixes)
/*     */   {
/* 221 */     if (prefixes != null) {
/* 222 */       StringTokenizer tokens = new StringTokenizer(prefixes);
/* 223 */       while (tokens.hasMoreTokens()) {
/* 224 */         String prefix = tokens.nextToken();
/*     */         String uri;
/*     */         String uri;
/* 226 */         if (prefix.equals("#default"))
/* 227 */           uri = lookupNamespace("");
/*     */         else
/* 229 */           uri = lookupNamespace(prefix);
/* 230 */         if (uri != null) excludeURI(uri);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isExcludedNamespace(String uri)
/*     */   {
/* 239 */     if ((uri != null) && (this._excludedURI != null)) {
/* 240 */       Integer refcnt = (Integer)this._excludedURI.get(uri);
/* 241 */       return (refcnt != null) && (refcnt.intValue() > 0);
/*     */     }
/* 243 */     return false;
/*     */   }
/*     */ 
/*     */   public void unExcludeNamespaces(String prefixes)
/*     */   {
/* 250 */     if (this._excludedURI == null) return;
/* 251 */     if (prefixes != null) {
/* 252 */       StringTokenizer tokens = new StringTokenizer(prefixes);
/* 253 */       while (tokens.hasMoreTokens()) {
/* 254 */         String prefix = tokens.nextToken();
/*     */         String uri;
/*     */         String uri;
/* 256 */         if (prefix.equals("#default"))
/* 257 */           uri = lookupNamespace("");
/*     */         else
/* 259 */           uri = lookupNamespace(prefix);
/* 260 */         Integer refcnt = (Integer)this._excludedURI.get(uri);
/* 261 */         if (refcnt != null)
/* 262 */           this._excludedURI.put(uri, new Integer(refcnt.intValue() - 1));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void pushExcludedNamespacesContext()
/*     */   {
/* 274 */     if (this._excludedURIStack == null) {
/* 275 */       this._excludedURIStack = new Stack();
/*     */     }
/* 277 */     this._excludedURIStack.push(this._excludedURI);
/* 278 */     this._excludedURI = null;
/*     */   }
/*     */ 
/*     */   public void popExcludedNamespacesContext()
/*     */   {
/* 289 */     this._excludedURI = ((Hashtable)this._excludedURIStack.pop());
/* 290 */     if (this._excludedURIStack.isEmpty())
/* 291 */       this._excludedURIStack = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xalan.internal.xsltc.compiler.SymbolTable
 * JD-Core Version:    0.6.2
 */