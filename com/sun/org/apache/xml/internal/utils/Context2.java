/*     */ package com.sun.org.apache.xml.internal.utils;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ 
/*     */ final class Context2
/*     */ {
/* 452 */   private static final Enumeration EMPTY_ENUMERATION = new Vector().elements();
/*     */   Hashtable prefixTable;
/*     */   Hashtable uriTable;
/*     */   Hashtable elementNameTable;
/*     */   Hashtable attributeNameTable;
/* 463 */   String defaultNS = null;
/*     */ 
/* 469 */   private Vector declarations = null;
/* 470 */   private boolean tablesDirty = false;
/* 471 */   private Context2 parent = null;
/* 472 */   private Context2 child = null;
/*     */ 
/*     */   Context2(Context2 parent)
/*     */   {
/* 479 */     if (parent == null)
/*     */     {
/* 481 */       this.prefixTable = new Hashtable();
/* 482 */       this.uriTable = new Hashtable();
/* 483 */       this.elementNameTable = null;
/* 484 */       this.attributeNameTable = null;
/*     */     }
/*     */     else {
/* 487 */       setParent(parent);
/*     */     }
/*     */   }
/*     */ 
/*     */   Context2 getChild()
/*     */   {
/* 497 */     return this.child;
/*     */   }
/*     */ 
/*     */   Context2 getParent()
/*     */   {
/* 506 */     return this.parent;
/*     */   }
/*     */ 
/*     */   void setParent(Context2 parent)
/*     */   {
/* 518 */     this.parent = parent;
/* 519 */     parent.child = this;
/* 520 */     this.declarations = null;
/* 521 */     this.prefixTable = parent.prefixTable;
/* 522 */     this.uriTable = parent.uriTable;
/* 523 */     this.elementNameTable = parent.elementNameTable;
/* 524 */     this.attributeNameTable = parent.attributeNameTable;
/* 525 */     this.defaultNS = parent.defaultNS;
/* 526 */     this.tablesDirty = false;
/*     */   }
/*     */ 
/*     */   void declarePrefix(String prefix, String uri)
/*     */   {
/* 540 */     if (!this.tablesDirty) {
/* 541 */       copyTables();
/*     */     }
/* 543 */     if (this.declarations == null) {
/* 544 */       this.declarations = new Vector();
/*     */     }
/*     */ 
/* 547 */     prefix = prefix.intern();
/* 548 */     uri = uri.intern();
/* 549 */     if ("".equals(prefix)) {
/* 550 */       if ("".equals(uri))
/* 551 */         this.defaultNS = null;
/*     */       else
/* 553 */         this.defaultNS = uri;
/*     */     }
/*     */     else {
/* 556 */       this.prefixTable.put(prefix, uri);
/* 557 */       this.uriTable.put(uri, prefix);
/*     */     }
/* 559 */     this.declarations.addElement(prefix);
/*     */   }
/*     */ 
/*     */   String[] processName(String qName, boolean isAttribute)
/*     */   {
/*     */     Hashtable table;
/*     */     Hashtable table;
/* 580 */     if (isAttribute) {
/* 581 */       if (this.elementNameTable == null)
/* 582 */         this.elementNameTable = new Hashtable();
/* 583 */       table = this.elementNameTable;
/*     */     } else {
/* 585 */       if (this.attributeNameTable == null)
/* 586 */         this.attributeNameTable = new Hashtable();
/* 587 */       table = this.attributeNameTable;
/*     */     }
/*     */ 
/* 593 */     String[] name = (String[])table.get(qName);
/* 594 */     if (name != null) {
/* 595 */       return name;
/*     */     }
/*     */ 
/* 600 */     name = new String[3];
/* 601 */     int index = qName.indexOf(':');
/*     */ 
/* 605 */     if (index == -1) {
/* 606 */       if ((isAttribute) || (this.defaultNS == null))
/* 607 */         name[0] = "";
/*     */       else {
/* 609 */         name[0] = this.defaultNS;
/*     */       }
/* 611 */       name[1] = qName.intern();
/* 612 */       name[2] = name[1];
/*     */     }
/*     */     else
/*     */     {
/* 617 */       String prefix = qName.substring(0, index);
/* 618 */       String local = qName.substring(index + 1);
/*     */       String uri;
/*     */       String uri;
/* 620 */       if ("".equals(prefix))
/* 621 */         uri = this.defaultNS;
/*     */       else {
/* 623 */         uri = (String)this.prefixTable.get(prefix);
/*     */       }
/* 625 */       if (uri == null) {
/* 626 */         return null;
/*     */       }
/* 628 */       name[0] = uri;
/* 629 */       name[1] = local.intern();
/* 630 */       name[2] = qName.intern();
/*     */     }
/*     */ 
/* 634 */     table.put(name[2], name);
/* 635 */     this.tablesDirty = true;
/* 636 */     return name;
/*     */   }
/*     */ 
/*     */   String getURI(String prefix)
/*     */   {
/* 650 */     if ("".equals(prefix))
/* 651 */       return this.defaultNS;
/* 652 */     if (this.prefixTable == null) {
/* 653 */       return null;
/*     */     }
/* 655 */     return (String)this.prefixTable.get(prefix);
/*     */   }
/*     */ 
/*     */   String getPrefix(String uri)
/*     */   {
/* 672 */     if (this.uriTable == null) {
/* 673 */       return null;
/*     */     }
/* 675 */     return (String)this.uriTable.get(uri);
/*     */   }
/*     */ 
/*     */   Enumeration getDeclaredPrefixes()
/*     */   {
/* 688 */     if (this.declarations == null) {
/* 689 */       return EMPTY_ENUMERATION;
/*     */     }
/* 691 */     return this.declarations.elements();
/*     */   }
/*     */ 
/*     */   Enumeration getPrefixes()
/*     */   {
/* 707 */     if (this.prefixTable == null) {
/* 708 */       return EMPTY_ENUMERATION;
/*     */     }
/* 710 */     return this.prefixTable.keys();
/*     */   }
/*     */ 
/*     */   private void copyTables()
/*     */   {
/* 733 */     this.prefixTable = ((Hashtable)this.prefixTable.clone());
/* 734 */     this.uriTable = ((Hashtable)this.uriTable.clone());
/*     */ 
/* 741 */     if (this.elementNameTable != null)
/* 742 */       this.elementNameTable = new Hashtable();
/* 743 */     if (this.attributeNameTable != null)
/* 744 */       this.attributeNameTable = new Hashtable();
/* 745 */     this.tablesDirty = true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.utils.Context2
 * JD-Core Version:    0.6.2
 */