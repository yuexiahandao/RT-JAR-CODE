/*      */ package com.sun.jndi.dns;
/*      */ 
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.NoSuchElementException;
/*      */ import javax.naming.Binding;
/*      */ import javax.naming.CompositeName;
/*      */ import javax.naming.Name;
/*      */ import javax.naming.NamingException;
/*      */ import javax.naming.spi.DirectoryManager;
/*      */ 
/*      */ class BindingEnumeration extends NameClassPairEnumeration
/*      */ {
/*      */   BindingEnumeration(DnsContext paramDnsContext, Hashtable paramHashtable)
/*      */   {
/* 1030 */     super(paramDnsContext, paramHashtable);
/*      */   }
/*      */ 
/*      */   public Object next()
/*      */     throws NamingException
/*      */   {
/* 1039 */     if (!hasMore()) {
/* 1040 */       throw new NoSuchElementException(); } NameNode localNameNode = (NameNode)this.nodes.nextElement();
/*      */ 
/* 1044 */     String str1 = localNameNode.getLabel();
/* 1045 */     Name localName1 = new DnsName().add(str1);
/* 1046 */     String str2 = localName1.toString();
/* 1047 */     Name localName2 = new CompositeName().add(str2);
/* 1048 */     String str3 = localName2.toString();
/*      */ 
/* 1050 */     DnsName localDnsName = this.ctx.fullyQualify(localName1);
/*      */ 
/* 1053 */     DnsContext localDnsContext = new DnsContext(this.ctx, localDnsName);
/*      */     Object localObject2;
/*      */     try { Object localObject1 = DirectoryManager.getObjectInstance(localDnsContext, localName2, this.ctx, localDnsContext.environment, null);
/*      */ 
/* 1058 */       localObject2 = new Binding(str3, localObject1);
/* 1059 */       ((Binding)localObject2).setNameInNamespace(this.ctx.fullyQualify(localName2).toString());
/* 1060 */       return localObject2;
/*      */     } catch (Exception localException) {
/* 1062 */       localObject2 = new NamingException("Problem generating object using object factory");
/*      */ 
/* 1064 */       ((NamingException)localObject2).setRootCause(localException);
/* 1065 */     }throw ((Throwable)localObject2);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.dns.BindingEnumeration
 * JD-Core Version:    0.6.2
 */