/*      */ package com.sun.jndi.dns;
/*      */ 
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.NoSuchElementException;
/*      */ import javax.naming.CompositeName;
/*      */ import javax.naming.Name;
/*      */ import javax.naming.NameClassPair;
/*      */ import javax.naming.NamingEnumeration;
/*      */ import javax.naming.NamingException;
/*      */ 
/*      */ class NameClassPairEnumeration
/*      */   implements NamingEnumeration
/*      */ {
/*      */   protected Enumeration nodes;
/*      */   protected DnsContext ctx;
/*      */ 
/*      */   NameClassPairEnumeration(DnsContext paramDnsContext, Hashtable paramHashtable)
/*      */   {
/*  968 */     this.ctx = paramDnsContext;
/*  969 */     this.nodes = (paramHashtable != null ? paramHashtable.elements() : null);
/*      */   }
/*      */ 
/*      */   public void close()
/*      */   {
/*  978 */     this.nodes = null;
/*  979 */     this.ctx = null;
/*      */   }
/*      */ 
/*      */   public boolean hasMore() {
/*  983 */     boolean bool = (this.nodes != null) && (this.nodes.hasMoreElements());
/*  984 */     if (!bool) {
/*  985 */       close();
/*      */     }
/*  987 */     return bool;
/*      */   }
/*      */ 
/*      */   public Object next() throws NamingException {
/*  991 */     if (!hasMore()) {
/*  992 */       throw new NoSuchElementException();
/*      */     }
/*  994 */     NameNode localNameNode = (NameNode)this.nodes.nextElement();
/*  995 */     String str1 = (localNameNode.isZoneCut()) || (localNameNode.getChildren() != null) ? "javax.naming.directory.DirContext" : "java.lang.Object";
/*      */ 
/* 1000 */     String str2 = localNameNode.getLabel();
/* 1001 */     Name localName1 = new DnsName().add(str2);
/* 1002 */     Name localName2 = new CompositeName().add(localName1.toString());
/*      */ 
/* 1004 */     NameClassPair localNameClassPair = new NameClassPair(localName2.toString(), str1);
/* 1005 */     localNameClassPair.setNameInNamespace(this.ctx.fullyQualify(localName2).toString());
/* 1006 */     return localNameClassPair;
/*      */   }
/*      */ 
/*      */   public boolean hasMoreElements() {
/* 1010 */     return hasMore();
/*      */   }
/*      */ 
/*      */   public Object nextElement() {
/*      */     try {
/* 1015 */       return next();
/*      */     } catch (NamingException localNamingException) {
/* 1017 */       throw new NoSuchElementException("javax.naming.NamingException was thrown: " + localNamingException.getMessage());
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.dns.NameClassPairEnumeration
 * JD-Core Version:    0.6.2
 */