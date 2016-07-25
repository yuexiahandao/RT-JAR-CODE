/*     */ package com.sun.xml.internal.txw2;
/*     */ 
/*     */ class StartTag extends Content
/*     */   implements NamespaceResolver
/*     */ {
/*     */   private String uri;
/*     */   private final String localName;
/*     */   private Attribute firstAtt;
/*     */   private Attribute lastAtt;
/*     */   private ContainerElement owner;
/*     */   private NamespaceDecl firstNs;
/*     */   private NamespaceDecl lastNs;
/*     */   final Document document;
/*     */ 
/*     */   public StartTag(ContainerElement owner, String uri, String localName)
/*     */   {
/*  76 */     this(owner.document, uri, localName);
/*  77 */     this.owner = owner;
/*     */   }
/*     */ 
/*     */   public StartTag(Document document, String uri, String localName) {
/*  81 */     assert (uri != null);
/*  82 */     assert (localName != null);
/*     */ 
/*  84 */     this.uri = uri;
/*  85 */     this.localName = localName;
/*  86 */     this.document = document;
/*     */ 
/*  91 */     addNamespaceDecl(uri, null, false);
/*     */   }
/*     */ 
/*     */   public void addAttribute(String nsUri, String localName, Object arg) {
/*  95 */     checkWritable();
/*     */ 
/*  99 */     for (Attribute a = this.firstAtt; (a != null) && 
/* 100 */       (!a.hasName(nsUri, localName)); a = a.next);
/* 106 */     if (a == null) {
/* 107 */       a = new Attribute(nsUri, localName);
/* 108 */       if (this.lastAtt == null) {
/* 109 */         assert (this.firstAtt == null);
/* 110 */         this.firstAtt = (this.lastAtt = a);
/*     */       } else {
/* 112 */         assert (this.firstAtt != null);
/* 113 */         this.lastAtt.next = a;
/* 114 */         this.lastAtt = a;
/*     */       }
/* 116 */       if (nsUri.length() > 0) {
/* 117 */         addNamespaceDecl(nsUri, null, true);
/*     */       }
/*     */     }
/* 120 */     this.document.writeValue(arg, this, a.value);
/*     */   }
/*     */ 
/*     */   public NamespaceDecl addNamespaceDecl(String uri, String prefix, boolean requirePrefix)
/*     */   {
/* 143 */     checkWritable();
/*     */ 
/* 145 */     if (uri == null)
/* 146 */       throw new IllegalArgumentException();
/* 147 */     if (uri.length() == 0) {
/* 148 */       if (requirePrefix)
/* 149 */         throw new IllegalArgumentException("The empty namespace cannot have a non-empty prefix");
/* 150 */       if ((prefix != null) && (prefix.length() > 0))
/* 151 */         throw new IllegalArgumentException("The empty namespace can be only bound to the empty prefix");
/* 152 */       prefix = "";
/*     */     }
/*     */ 
/* 156 */     for (NamespaceDecl n = this.firstNs; n != null; n = n.next) {
/* 157 */       if (uri.equals(n.uri)) {
/* 158 */         if (prefix == null)
/*     */         {
/* 160 */           n.requirePrefix |= requirePrefix;
/* 161 */           return n;
/*     */         }
/* 163 */         if (n.prefix == null)
/*     */         {
/* 165 */           n.prefix = prefix;
/* 166 */           n.requirePrefix |= requirePrefix;
/* 167 */           return n;
/*     */         }
/* 169 */         if (prefix.equals(n.prefix))
/*     */         {
/* 171 */           n.requirePrefix |= requirePrefix;
/* 172 */           return n;
/*     */         }
/*     */       }
/* 175 */       if ((prefix != null) && (n.prefix != null) && (n.prefix.equals(prefix))) {
/* 176 */         throw new IllegalArgumentException("Prefix '" + prefix + "' is already bound to '" + n.uri + '\'');
/*     */       }
/*     */     }
/*     */ 
/* 180 */     NamespaceDecl ns = new NamespaceDecl(this.document.assignNewId(), uri, prefix, requirePrefix);
/* 181 */     if (this.lastNs == null) {
/* 182 */       assert (this.firstNs == null);
/* 183 */       this.firstNs = (this.lastNs = ns);
/*     */     } else {
/* 185 */       assert (this.firstNs != null);
/* 186 */       this.lastNs.next = ns;
/* 187 */       this.lastNs = ns;
/*     */     }
/* 189 */     return ns;
/*     */   }
/*     */ 
/*     */   private void checkWritable()
/*     */   {
/* 196 */     if (isWritten())
/* 197 */       throw new IllegalStateException("The start tag of " + this.localName + " has already been written. " + "If you need out of order writing, see the TypedXmlWriter.block method");
/*     */   }
/*     */ 
/*     */   boolean isWritten()
/*     */   {
/* 206 */     return this.uri == null;
/*     */   }
/*     */ 
/*     */   boolean isReadyToCommit()
/*     */   {
/* 214 */     if ((this.owner != null) && (this.owner.isBlocked())) {
/* 215 */       return false;
/*     */     }
/* 217 */     for (Content c = getNext(); c != null; c = c.getNext()) {
/* 218 */       if (c.concludesPendingStartTag())
/* 219 */         return true;
/*     */     }
/* 221 */     return false;
/*     */   }
/*     */ 
/*     */   public void written() {
/* 225 */     this.firstAtt = (this.lastAtt = null);
/* 226 */     this.uri = null;
/* 227 */     if (this.owner != null) {
/* 228 */       assert (this.owner.startTag == this);
/* 229 */       this.owner.startTag = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   boolean concludesPendingStartTag() {
/* 234 */     return true;
/*     */   }
/*     */ 
/*     */   void accept(ContentVisitor visitor) {
/* 238 */     visitor.onStartTag(this.uri, this.localName, this.firstAtt, this.firstNs);
/*     */   }
/*     */ 
/*     */   public String getPrefix(String nsUri) {
/* 242 */     NamespaceDecl ns = addNamespaceDecl(nsUri, null, false);
/* 243 */     if (ns.prefix != null)
/*     */     {
/* 245 */       return ns.prefix;
/* 246 */     }return ns.dummyPrefix;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.StartTag
 * JD-Core Version:    0.6.2
 */