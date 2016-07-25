/*     */ package com.sun.xml.internal.txw2;
/*     */ 
/*     */ import java.util.EmptyStackException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class NamespaceSupport
/*     */ {
/*     */   public static final String XMLNS = "http://www.w3.org/XML/1998/namespace";
/*     */   public static final String NSDECL = "http://www.w3.org/xmlns/2000/";
/* 140 */   private static final Enumeration EMPTY_ENUMERATION = new Vector().elements();
/*     */   private Context[] contexts;
/*     */   private Context currentContext;
/*     */   private int contextPos;
/*     */   private boolean namespaceDeclUris;
/*     */ 
/*     */   public NamespaceSupport()
/*     */   {
/* 154 */     reset();
/*     */   }
/*     */ 
/*     */   public void reset()
/*     */   {
/* 177 */     this.contexts = new Context[32];
/* 178 */     this.namespaceDeclUris = false;
/* 179 */     this.contextPos = 0;
/*     */     void tmp36_33 = new Context(); this.currentContext = tmp36_33; this.contexts[this.contextPos] = tmp36_33;
/* 181 */     this.currentContext.declarePrefix("xml", "http://www.w3.org/XML/1998/namespace");
/*     */   }
/*     */ 
/*     */   public void pushContext()
/*     */   {
/* 223 */     int max = this.contexts.length;
/*     */ 
/* 225 */     this.contextPos += 1;
/*     */ 
/* 228 */     if (this.contextPos >= max) {
/* 229 */       Context[] newContexts = new Context[max * 2];
/* 230 */       System.arraycopy(this.contexts, 0, newContexts, 0, max);
/* 231 */       max *= 2;
/* 232 */       this.contexts = newContexts;
/*     */     }
/*     */ 
/* 236 */     this.currentContext = this.contexts[this.contextPos];
/* 237 */     if (this.currentContext == null)
/*     */     {
/*     */       void tmp88_85 = new Context(); this.currentContext = tmp88_85; this.contexts[this.contextPos] = tmp88_85;
/*     */     }
/*     */ 
/* 242 */     if (this.contextPos > 0)
/* 243 */       this.currentContext.setParent(this.contexts[(this.contextPos - 1)]);
/*     */   }
/*     */ 
/*     */   public void popContext()
/*     */   {
/* 263 */     this.contexts[this.contextPos].clear();
/* 264 */     this.contextPos -= 1;
/* 265 */     if (this.contextPos < 0) {
/* 266 */       throw new EmptyStackException();
/*     */     }
/* 268 */     this.currentContext = this.contexts[this.contextPos];
/*     */   }
/*     */ 
/*     */   public boolean declarePrefix(String prefix, String uri)
/*     */   {
/* 313 */     if ((prefix.equals("xml")) || (prefix.equals("xmlns"))) {
/* 314 */       return false;
/*     */     }
/* 316 */     this.currentContext.declarePrefix(prefix, uri);
/* 317 */     return true;
/*     */   }
/*     */ 
/*     */   public String[] processName(String qName, String[] parts, boolean isAttribute)
/*     */   {
/* 365 */     String[] myParts = this.currentContext.processName(qName, isAttribute);
/* 366 */     if (myParts == null) {
/* 367 */       return null;
/*     */     }
/* 369 */     parts[0] = myParts[0];
/* 370 */     parts[1] = myParts[1];
/* 371 */     parts[2] = myParts[2];
/* 372 */     return parts;
/*     */   }
/*     */ 
/*     */   public String getURI(String prefix)
/*     */   {
/* 391 */     return this.currentContext.getURI(prefix);
/*     */   }
/*     */ 
/*     */   public Enumeration getPrefixes()
/*     */   {
/* 411 */     return this.currentContext.getPrefixes();
/*     */   }
/*     */ 
/*     */   public String getPrefix(String uri)
/*     */   {
/* 436 */     return this.currentContext.getPrefix(uri);
/*     */   }
/*     */ 
/*     */   public Enumeration getPrefixes(String uri)
/*     */   {
/* 465 */     Vector prefixes = new Vector();
/* 466 */     Enumeration allPrefixes = getPrefixes();
/* 467 */     while (allPrefixes.hasMoreElements()) {
/* 468 */       String prefix = (String)allPrefixes.nextElement();
/* 469 */       if (uri.equals(getURI(prefix))) {
/* 470 */         prefixes.addElement(prefix);
/*     */       }
/*     */     }
/* 473 */     return prefixes.elements();
/*     */   }
/*     */ 
/*     */   public Enumeration getDeclaredPrefixes()
/*     */   {
/* 491 */     return this.currentContext.getDeclaredPrefixes();
/*     */   }
/*     */ 
/*     */   public void setNamespaceDeclUris(boolean value)
/*     */   {
/* 507 */     if (this.contextPos != 0)
/* 508 */       throw new IllegalStateException();
/* 509 */     if (value == this.namespaceDeclUris)
/* 510 */       return;
/* 511 */     this.namespaceDeclUris = value;
/* 512 */     if (value) {
/* 513 */       this.currentContext.declarePrefix("xmlns", "http://www.w3.org/xmlns/2000/");
/*     */     }
/*     */     else
/*     */     {
/*     */       void tmp64_61 = new Context(); this.currentContext = tmp64_61; this.contexts[this.contextPos] = tmp64_61;
/* 516 */       this.currentContext.declarePrefix("xml", "http://www.w3.org/XML/1998/namespace");
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean isNamespaceDeclUris()
/*     */   {
/* 527 */     return this.namespaceDeclUris;
/*     */   }
/*     */ 
/*     */   final class Context
/*     */   {
/*     */     Hashtable prefixTable;
/*     */     Hashtable uriTable;
/*     */     Hashtable elementNameTable;
/*     */     Hashtable attributeNameTable;
/* 831 */     String defaultNS = "";
/*     */ 
/* 839 */     private Vector declarations = null;
/* 840 */     private boolean declSeen = false;
/* 841 */     private Context parent = null;
/*     */ 
/*     */     Context()
/*     */     {
/* 564 */       copyTables();
/*     */     }
/*     */ 
/*     */     void setParent(Context parent)
/*     */     {
/* 577 */       this.parent = parent;
/* 578 */       this.declarations = null;
/* 579 */       this.prefixTable = parent.prefixTable;
/* 580 */       this.uriTable = parent.uriTable;
/* 581 */       this.elementNameTable = parent.elementNameTable;
/* 582 */       this.attributeNameTable = parent.attributeNameTable;
/* 583 */       this.defaultNS = parent.defaultNS;
/* 584 */       this.declSeen = false;
/*     */     }
/*     */ 
/*     */     void clear()
/*     */     {
/* 595 */       this.parent = null;
/* 596 */       this.prefixTable = null;
/* 597 */       this.uriTable = null;
/* 598 */       this.elementNameTable = null;
/* 599 */       this.attributeNameTable = null;
/* 600 */       this.defaultNS = "";
/*     */     }
/*     */ 
/*     */     void declarePrefix(String prefix, String uri)
/*     */     {
/* 617 */       if (!this.declSeen) {
/* 618 */         copyTables();
/*     */       }
/* 620 */       if (this.declarations == null) {
/* 621 */         this.declarations = new Vector();
/*     */       }
/*     */ 
/* 624 */       prefix = prefix.intern();
/* 625 */       uri = uri.intern();
/* 626 */       if ("".equals(prefix)) {
/* 627 */         this.defaultNS = uri;
/*     */       } else {
/* 629 */         this.prefixTable.put(prefix, uri);
/* 630 */         this.uriTable.put(uri, prefix);
/*     */       }
/* 632 */       this.declarations.addElement(prefix);
/*     */     }
/*     */ 
/*     */     String[] processName(String qName, boolean isAttribute)
/*     */     {
/*     */       Hashtable table;
/*     */       Hashtable table;
/* 653 */       if (isAttribute)
/* 654 */         table = this.attributeNameTable;
/*     */       else {
/* 656 */         table = this.elementNameTable;
/*     */       }
/*     */ 
/* 662 */       String[] name = (String[])table.get(qName);
/* 663 */       if (name != null) {
/* 664 */         return name;
/*     */       }
/*     */ 
/* 671 */       name = new String[3];
/* 672 */       name[2] = qName.intern();
/* 673 */       int index = qName.indexOf(':');
/*     */ 
/* 677 */       if (index == -1) {
/* 678 */         if (isAttribute) {
/* 679 */           if ((qName == "xmlns") && (NamespaceSupport.this.namespaceDeclUris))
/* 680 */             name[0] = "http://www.w3.org/xmlns/2000/";
/*     */           else
/* 682 */             name[0] = "";
/*     */         }
/* 684 */         else name[0] = this.defaultNS;
/*     */ 
/* 686 */         name[1] = name[2];
/*     */       }
/*     */       else
/*     */       {
/* 691 */         String prefix = qName.substring(0, index);
/* 692 */         String local = qName.substring(index + 1);
/*     */         String uri;
/*     */         String uri;
/* 694 */         if ("".equals(prefix))
/* 695 */           uri = this.defaultNS;
/*     */         else {
/* 697 */           uri = (String)this.prefixTable.get(prefix);
/*     */         }
/* 699 */         if ((uri == null) || ((!isAttribute) && ("xmlns".equals(prefix))))
/*     */         {
/* 701 */           return null;
/*     */         }
/* 703 */         name[0] = uri;
/* 704 */         name[1] = local.intern();
/*     */       }
/*     */ 
/* 709 */       table.put(name[2], name);
/* 710 */       return name;
/*     */     }
/*     */ 
/*     */     String getURI(String prefix)
/*     */     {
/* 724 */       if ("".equals(prefix))
/* 725 */         return this.defaultNS;
/* 726 */       if (this.prefixTable == null) {
/* 727 */         return null;
/*     */       }
/* 729 */       return (String)this.prefixTable.get(prefix);
/*     */     }
/*     */ 
/*     */     String getPrefix(String uri)
/*     */     {
/* 745 */       if (this.uriTable != null) {
/* 746 */         String uriPrefix = (String)this.uriTable.get(uri);
/* 747 */         if (uriPrefix == null) return null;
/* 748 */         String verifyNamespace = (String)this.prefixTable.get(uriPrefix);
/* 749 */         if (uri.equals(verifyNamespace)) {
/* 750 */           return uriPrefix;
/*     */         }
/*     */       }
/* 753 */       return null;
/*     */     }
/*     */ 
/*     */     Enumeration getDeclaredPrefixes()
/*     */     {
/* 765 */       if (this.declarations == null) {
/* 766 */         return NamespaceSupport.EMPTY_ENUMERATION;
/*     */       }
/* 768 */       return this.declarations.elements();
/*     */     }
/*     */ 
/*     */     Enumeration getPrefixes()
/*     */     {
/* 784 */       if (this.prefixTable == null) {
/* 785 */         return NamespaceSupport.EMPTY_ENUMERATION;
/*     */       }
/* 787 */       return this.prefixTable.keys();
/*     */     }
/*     */ 
/*     */     private void copyTables()
/*     */     {
/* 806 */       if (this.prefixTable != null)
/* 807 */         this.prefixTable = ((Hashtable)this.prefixTable.clone());
/*     */       else {
/* 809 */         this.prefixTable = new Hashtable();
/*     */       }
/* 811 */       if (this.uriTable != null)
/* 812 */         this.uriTable = ((Hashtable)this.uriTable.clone());
/*     */       else {
/* 814 */         this.uriTable = new Hashtable();
/*     */       }
/* 816 */       this.elementNameTable = new Hashtable();
/* 817 */       this.attributeNameTable = new Hashtable();
/* 818 */       this.declSeen = true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.txw2.NamespaceSupport
 * JD-Core Version:    0.6.2
 */