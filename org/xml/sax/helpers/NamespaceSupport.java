/*     */ package org.xml.sax.helpers;
/*     */ 
/*     */ import java.util.EmptyStackException;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class NamespaceSupport
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
/* 834 */     String defaultNS = null;
/*     */ 
/* 842 */     private Vector declarations = null;
/* 843 */     private boolean declSeen = false;
/* 844 */     private Context parent = null;
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
/* 600 */       this.defaultNS = null;
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
/* 627 */         if ("".equals(uri))
/* 628 */           this.defaultNS = null;
/*     */         else
/* 630 */           this.defaultNS = uri;
/*     */       }
/*     */       else {
/* 633 */         this.prefixTable.put(prefix, uri);
/* 634 */         this.uriTable.put(uri, prefix);
/*     */       }
/* 636 */       this.declarations.addElement(prefix);
/*     */     }
/*     */ 
/*     */     String[] processName(String qName, boolean isAttribute)
/*     */     {
/*     */       Hashtable table;
/*     */       Hashtable table;
/* 657 */       if (isAttribute)
/* 658 */         table = this.attributeNameTable;
/*     */       else {
/* 660 */         table = this.elementNameTable;
/*     */       }
/*     */ 
/* 666 */       String[] name = (String[])table.get(qName);
/* 667 */       if (name != null) {
/* 668 */         return name;
/*     */       }
/*     */ 
/* 675 */       name = new String[3];
/* 676 */       name[2] = qName.intern();
/* 677 */       int index = qName.indexOf(':');
/*     */ 
/* 681 */       if (index == -1) {
/* 682 */         if (isAttribute) {
/* 683 */           if ((qName == "xmlns") && (NamespaceSupport.this.namespaceDeclUris))
/* 684 */             name[0] = "http://www.w3.org/xmlns/2000/";
/*     */           else
/* 686 */             name[0] = "";
/* 687 */         } else if (this.defaultNS == null)
/* 688 */           name[0] = "";
/*     */         else {
/* 690 */           name[0] = this.defaultNS;
/*     */         }
/* 692 */         name[1] = name[2];
/*     */       }
/*     */       else
/*     */       {
/* 697 */         String prefix = qName.substring(0, index);
/* 698 */         String local = qName.substring(index + 1);
/*     */         String uri;
/*     */         String uri;
/* 700 */         if ("".equals(prefix))
/* 701 */           uri = this.defaultNS;
/*     */         else {
/* 703 */           uri = (String)this.prefixTable.get(prefix);
/*     */         }
/* 705 */         if ((uri == null) || ((!isAttribute) && ("xmlns".equals(prefix))))
/*     */         {
/* 707 */           return null;
/*     */         }
/* 709 */         name[0] = uri;
/* 710 */         name[1] = local.intern();
/*     */       }
/*     */ 
/* 715 */       table.put(name[2], name);
/* 716 */       return name;
/*     */     }
/*     */ 
/*     */     String getURI(String prefix)
/*     */     {
/* 730 */       if ("".equals(prefix))
/* 731 */         return this.defaultNS;
/* 732 */       if (this.prefixTable == null) {
/* 733 */         return null;
/*     */       }
/* 735 */       return (String)this.prefixTable.get(prefix);
/*     */     }
/*     */ 
/*     */     String getPrefix(String uri)
/*     */     {
/* 752 */       if (this.uriTable == null) {
/* 753 */         return null;
/*     */       }
/* 755 */       return (String)this.uriTable.get(uri);
/*     */     }
/*     */ 
/*     */     Enumeration getDeclaredPrefixes()
/*     */     {
/* 768 */       if (this.declarations == null) {
/* 769 */         return NamespaceSupport.EMPTY_ENUMERATION;
/*     */       }
/* 771 */       return this.declarations.elements();
/*     */     }
/*     */ 
/*     */     Enumeration getPrefixes()
/*     */     {
/* 787 */       if (this.prefixTable == null) {
/* 788 */         return NamespaceSupport.EMPTY_ENUMERATION;
/*     */       }
/* 790 */       return this.prefixTable.keys();
/*     */     }
/*     */ 
/*     */     private void copyTables()
/*     */     {
/* 809 */       if (this.prefixTable != null)
/* 810 */         this.prefixTable = ((Hashtable)this.prefixTable.clone());
/*     */       else {
/* 812 */         this.prefixTable = new Hashtable();
/*     */       }
/* 814 */       if (this.uriTable != null)
/* 815 */         this.uriTable = ((Hashtable)this.uriTable.clone());
/*     */       else {
/* 817 */         this.uriTable = new Hashtable();
/*     */       }
/* 819 */       this.elementNameTable = new Hashtable();
/* 820 */       this.attributeNameTable = new Hashtable();
/* 821 */       this.declSeen = true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.xml.sax.helpers.NamespaceSupport
 * JD-Core Version:    0.6.2
 */