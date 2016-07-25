/*     */ package com.sun.xml.internal.ws.util;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.EmptyStackException;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class NamespaceSupport
/*     */ {
/*     */   public static final String XMLNS = "http://www.w3.org/XML/1998/namespace";
/* 106 */   private static final Iterable<String> EMPTY_ENUMERATION = new ArrayList();
/*     */   private Context[] contexts;
/*     */   private Context currentContext;
/*     */   private int contextPos;
/*     */ 
/*     */   public NamespaceSupport()
/*     */   {
/* 117 */     reset();
/*     */   }
/*     */ 
/*     */   public NamespaceSupport(NamespaceSupport that)
/*     */   {
/* 122 */     this.contexts = new Context[that.contexts.length];
/* 123 */     this.currentContext = null;
/* 124 */     this.contextPos = that.contextPos;
/*     */ 
/* 126 */     Context currentParent = null;
/*     */ 
/* 128 */     for (int i = 0; i < that.contexts.length; i++) {
/* 129 */       Context thatContext = that.contexts[i];
/*     */ 
/* 131 */       if (thatContext == null) {
/* 132 */         this.contexts[i] = null;
/*     */       }
/*     */       else
/*     */       {
/* 136 */         Context thisContext = new Context(thatContext, currentParent);
/* 137 */         this.contexts[i] = thisContext;
/* 138 */         if (that.currentContext == thatContext) {
/* 139 */           this.currentContext = thisContext;
/*     */         }
/*     */ 
/* 142 */         currentParent = thisContext;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 157 */     this.contexts = new Context[32];
/* 158 */     this.contextPos = 0;
/*     */     void tmp30_27 = new Context(); this.currentContext = tmp30_27; this.contexts[this.contextPos] = tmp30_27;
/* 160 */     this.currentContext.declarePrefix("xml", "http://www.w3.org/XML/1998/namespace");
/*     */   }
/*     */ 
/*     */   public void pushContext()
/*     */   {
/* 178 */     int max = this.contexts.length;
/* 179 */     this.contextPos += 1;
/*     */ 
/* 182 */     if (this.contextPos >= max) {
/* 183 */       Context[] newContexts = new Context[max * 2];
/* 184 */       System.arraycopy(this.contexts, 0, newContexts, 0, max);
/* 185 */       this.contexts = newContexts;
/*     */     }
/*     */ 
/* 189 */     this.currentContext = this.contexts[this.contextPos];
/* 190 */     if (this.currentContext == null)
/*     */     {
/*     */       void tmp83_80 = new Context(); this.currentContext = tmp83_80; this.contexts[this.contextPos] = tmp83_80;
/*     */     }
/*     */ 
/* 195 */     if (this.contextPos > 0)
/* 196 */       this.currentContext.setParent(this.contexts[(this.contextPos - 1)]);
/*     */   }
/*     */ 
/*     */   public void popContext()
/*     */   {
/* 214 */     this.contextPos -= 1;
/* 215 */     if (this.contextPos < 0) {
/* 216 */       throw new EmptyStackException();
/*     */     }
/* 218 */     this.currentContext = this.contexts[this.contextPos];
/*     */   }
/*     */ 
/*     */   public void slideContextUp()
/*     */   {
/* 226 */     this.contextPos -= 1;
/* 227 */     this.currentContext = this.contexts[this.contextPos];
/*     */   }
/*     */ 
/*     */   public void slideContextDown()
/*     */   {
/* 235 */     this.contextPos += 1;
/*     */ 
/* 237 */     if (this.contexts[this.contextPos] == null)
/*     */     {
/* 239 */       this.contexts[this.contextPos] = this.contexts[(this.contextPos - 1)];
/*     */     }
/*     */ 
/* 242 */     this.currentContext = this.contexts[this.contextPos];
/*     */   }
/*     */ 
/*     */   public boolean declarePrefix(String prefix, String uri)
/*     */   {
/* 279 */     if (((prefix.equals("xml")) && (!uri.equals("http://www.w3.org/XML/1998/namespace"))) || (prefix.equals("xmlns")))
/*     */     {
/* 281 */       return false;
/*     */     }
/* 283 */     this.currentContext.declarePrefix(prefix, uri);
/* 284 */     return true;
/*     */   }
/*     */ 
/*     */   public String[] processName(String qName, String[] parts, boolean isAttribute)
/*     */   {
/* 330 */     String[] myParts = this.currentContext.processName(qName, isAttribute);
/* 331 */     if (myParts == null) {
/* 332 */       return null;
/*     */     }
/* 334 */     parts[0] = myParts[0];
/* 335 */     parts[1] = myParts[1];
/* 336 */     parts[2] = myParts[2];
/* 337 */     return parts;
/*     */   }
/*     */ 
/*     */   public String getURI(String prefix)
/*     */   {
/* 354 */     return this.currentContext.getURI(prefix);
/*     */   }
/*     */ 
/*     */   public Iterable<String> getPrefixes()
/*     */   {
/* 371 */     return this.currentContext.getPrefixes();
/*     */   }
/*     */ 
/*     */   public String getPrefix(String uri)
/*     */   {
/* 394 */     return this.currentContext.getPrefix(uri);
/*     */   }
/*     */ 
/*     */   public Iterator getPrefixes(String uri)
/*     */   {
/* 419 */     List prefixes = new ArrayList();
/* 420 */     for (String prefix : getPrefixes()) {
/* 421 */       if (uri.equals(getURI(prefix))) {
/* 422 */         prefixes.add(prefix);
/*     */       }
/*     */     }
/* 425 */     return prefixes.iterator();
/*     */   }
/*     */ 
/*     */   public Iterable<String> getDeclaredPrefixes()
/*     */   {
/* 441 */     return this.currentContext.getDeclaredPrefixes();
/*     */   }
/*     */ 
/*     */   static final class Context
/*     */   {
/*     */     HashMap prefixTable;
/*     */     HashMap uriTable;
/*     */     HashMap elementNameTable;
/*     */     HashMap attributeNameTable;
/* 735 */     String defaultNS = null;
/*     */ 
/* 742 */     private ArrayList declarations = null;
/* 743 */     private boolean tablesDirty = false;
/* 744 */     private Context parent = null;
/*     */ 
/*     */     Context()
/*     */     {
/* 469 */       copyTables();
/*     */     }
/*     */ 
/*     */     Context(Context that, Context newParent) {
/* 473 */       if (that == null) {
/* 474 */         copyTables();
/* 475 */         return;
/*     */       }
/*     */ 
/* 478 */       if ((newParent != null) && (!that.tablesDirty)) {
/* 479 */         this.prefixTable = (that.prefixTable == that.parent.prefixTable ? newParent.prefixTable : (HashMap)that.prefixTable.clone());
/*     */ 
/* 484 */         this.uriTable = (that.uriTable == that.parent.uriTable ? newParent.uriTable : (HashMap)that.uriTable.clone());
/*     */ 
/* 488 */         this.elementNameTable = (that.elementNameTable == that.parent.elementNameTable ? newParent.elementNameTable : (HashMap)that.elementNameTable.clone());
/*     */ 
/* 492 */         this.attributeNameTable = (that.attributeNameTable == that.parent.attributeNameTable ? newParent.attributeNameTable : (HashMap)that.attributeNameTable.clone());
/*     */ 
/* 496 */         this.defaultNS = (that.defaultNS == that.parent.defaultNS ? newParent.defaultNS : that.defaultNS);
/*     */       }
/*     */       else
/*     */       {
/* 501 */         this.prefixTable = ((HashMap)that.prefixTable.clone());
/* 502 */         this.uriTable = ((HashMap)that.uriTable.clone());
/* 503 */         this.elementNameTable = ((HashMap)that.elementNameTable.clone());
/* 504 */         this.attributeNameTable = ((HashMap)that.attributeNameTable.clone());
/* 505 */         this.defaultNS = that.defaultNS;
/*     */       }
/*     */ 
/* 508 */       this.tablesDirty = that.tablesDirty;
/* 509 */       this.parent = newParent;
/* 510 */       this.declarations = (that.declarations == null ? null : (ArrayList)that.declarations.clone());
/*     */     }
/*     */ 
/*     */     void setParent(Context parent)
/*     */     {
/* 522 */       this.parent = parent;
/* 523 */       this.declarations = null;
/* 524 */       this.prefixTable = parent.prefixTable;
/* 525 */       this.uriTable = parent.uriTable;
/* 526 */       this.elementNameTable = parent.elementNameTable;
/* 527 */       this.attributeNameTable = parent.attributeNameTable;
/* 528 */       this.defaultNS = parent.defaultNS;
/* 529 */       this.tablesDirty = false;
/*     */     }
/*     */ 
/*     */     void declarePrefix(String prefix, String uri)
/*     */     {
/* 541 */       if (!this.tablesDirty) {
/* 542 */         copyTables();
/*     */       }
/* 544 */       if (this.declarations == null) {
/* 545 */         this.declarations = new ArrayList();
/*     */       }
/*     */ 
/* 548 */       prefix = prefix.intern();
/* 549 */       uri = uri.intern();
/* 550 */       if ("".equals(prefix)) {
/* 551 */         if ("".equals(uri))
/* 552 */           this.defaultNS = null;
/*     */         else
/* 554 */           this.defaultNS = uri;
/*     */       }
/*     */       else {
/* 557 */         this.prefixTable.put(prefix, uri);
/* 558 */         this.uriTable.put(uri, prefix);
/*     */       }
/* 560 */       this.declarations.add(prefix);
/*     */     }
/*     */ 
/*     */     String[] processName(String qName, boolean isAttribute)
/*     */     {
/*     */       Map table;
/*     */       Map table;
/* 579 */       if (isAttribute)
/* 580 */         table = this.elementNameTable;
/*     */       else {
/* 582 */         table = this.attributeNameTable;
/*     */       }
/*     */ 
/* 588 */       String[] name = (String[])table.get(qName);
/* 589 */       if (name != null) {
/* 590 */         return name;
/*     */       }
/*     */ 
/* 595 */       name = new String[3];
/* 596 */       int index = qName.indexOf(':');
/*     */ 
/* 599 */       if (index == -1) {
/* 600 */         if ((isAttribute) || (this.defaultNS == null))
/* 601 */           name[0] = "";
/*     */         else {
/* 603 */           name[0] = this.defaultNS;
/*     */         }
/* 605 */         name[1] = qName.intern();
/* 606 */         name[2] = name[1];
/*     */       }
/*     */       else
/*     */       {
/* 611 */         String prefix = qName.substring(0, index);
/* 612 */         String local = qName.substring(index + 1);
/*     */         String uri;
/*     */         String uri;
/* 614 */         if ("".equals(prefix))
/* 615 */           uri = this.defaultNS;
/*     */         else {
/* 617 */           uri = (String)this.prefixTable.get(prefix);
/*     */         }
/* 619 */         if (uri == null) {
/* 620 */           return null;
/*     */         }
/* 622 */         name[0] = uri;
/* 623 */         name[1] = local.intern();
/* 624 */         name[2] = qName.intern();
/*     */       }
/*     */ 
/* 628 */       table.put(name[2], name);
/* 629 */       this.tablesDirty = true;
/* 630 */       return name;
/*     */     }
/*     */ 
/*     */     String getURI(String prefix)
/*     */     {
/* 642 */       if ("".equals(prefix))
/* 643 */         return this.defaultNS;
/* 644 */       if (this.prefixTable == null) {
/* 645 */         return null;
/*     */       }
/* 647 */       return (String)this.prefixTable.get(prefix);
/*     */     }
/*     */ 
/*     */     String getPrefix(String uri)
/*     */     {
/* 662 */       if (this.uriTable == null) {
/* 663 */         return null;
/*     */       }
/* 665 */       return (String)this.uriTable.get(uri);
/*     */     }
/*     */ 
/*     */     Iterable<String> getDeclaredPrefixes()
/*     */     {
/* 676 */       if (this.declarations == null) {
/* 677 */         return NamespaceSupport.EMPTY_ENUMERATION;
/*     */       }
/* 679 */       return this.declarations;
/*     */     }
/*     */ 
/*     */     Iterable<String> getPrefixes()
/*     */     {
/* 693 */       if (this.prefixTable == null) {
/* 694 */         return NamespaceSupport.EMPTY_ENUMERATION;
/*     */       }
/* 696 */       return this.prefixTable.keySet();
/*     */     }
/*     */ 
/*     */     private void copyTables()
/*     */     {
/* 711 */       if (this.prefixTable != null)
/* 712 */         this.prefixTable = ((HashMap)this.prefixTable.clone());
/*     */       else {
/* 714 */         this.prefixTable = new HashMap();
/*     */       }
/* 716 */       if (this.uriTable != null)
/* 717 */         this.uriTable = ((HashMap)this.uriTable.clone());
/*     */       else {
/* 719 */         this.uriTable = new HashMap();
/*     */       }
/* 721 */       this.elementNameTable = new HashMap();
/* 722 */       this.attributeNameTable = new HashMap();
/* 723 */       this.tablesDirty = true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.ws.util.NamespaceSupport
 * JD-Core Version:    0.6.2
 */