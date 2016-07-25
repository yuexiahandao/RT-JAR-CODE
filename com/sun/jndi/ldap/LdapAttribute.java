/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NameNotFoundException;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.Attribute;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.BasicAttribute;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.directory.InitialDirContext;
/*     */ 
/*     */ final class LdapAttribute extends BasicAttribute
/*     */ {
/*     */   static final long serialVersionUID = -4288716561020779584L;
/*  47 */   private transient DirContext baseCtx = null;
/*  48 */   private Name rdn = new CompositeName();
/*     */   private String baseCtxURL;
/*     */   private Hashtable baseCtxEnv;
/*     */ 
/*     */   public Object clone()
/*     */   {
/*  56 */     LdapAttribute localLdapAttribute = new LdapAttribute(this.attrID, this.baseCtx, this.rdn);
/*  57 */     localLdapAttribute.values = ((Vector)this.values.clone());
/*  58 */     return localLdapAttribute;
/*     */   }
/*     */ 
/*     */   public boolean add(Object paramObject)
/*     */   {
/*  71 */     this.values.addElement(paramObject);
/*  72 */     return true;
/*     */   }
/*     */ 
/*     */   LdapAttribute(String paramString)
/*     */   {
/*  81 */     super(paramString);
/*     */   }
/*     */ 
/*     */   private LdapAttribute(String paramString, DirContext paramDirContext, Name paramName)
/*     */   {
/*  92 */     super(paramString);
/*  93 */     this.baseCtx = paramDirContext;
/*  94 */     this.rdn = paramName;
/*     */   }
/*     */ 
/*     */   void setParent(DirContext paramDirContext, Name paramName)
/*     */   {
/* 102 */     this.baseCtx = paramDirContext;
/* 103 */     this.rdn = paramName;
/*     */   }
/*     */ 
/*     */   private DirContext getBaseCtx()
/*     */     throws NamingException
/*     */   {
/* 113 */     if (this.baseCtx == null) {
/* 114 */       if (this.baseCtxEnv == null) {
/* 115 */         this.baseCtxEnv = new Hashtable(3);
/*     */       }
/* 117 */       this.baseCtxEnv.put("java.naming.factory.initial", "com.sun.jndi.ldap.LdapCtxFactory");
/*     */ 
/* 119 */       this.baseCtxEnv.put("java.naming.provider.url", this.baseCtxURL);
/* 120 */       this.baseCtx = new InitialDirContext(this.baseCtxEnv);
/*     */     }
/* 122 */     return this.baseCtx;
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 136 */     setBaseCtxInfo();
/*     */ 
/* 139 */     paramObjectOutputStream.defaultWriteObject();
/*     */   }
/*     */ 
/*     */   private void setBaseCtxInfo()
/*     */   {
/* 148 */     Hashtable localHashtable1 = null;
/* 149 */     Hashtable localHashtable2 = null;
/*     */ 
/* 151 */     if (this.baseCtx != null) {
/* 152 */       localHashtable1 = ((LdapCtx)this.baseCtx).envprops;
/* 153 */       this.baseCtxURL = ((LdapCtx)this.baseCtx).getURL();
/*     */     }
/*     */ 
/* 156 */     if ((localHashtable1 != null) && (localHashtable1.size() > 0))
/*     */     {
/* 159 */       Enumeration localEnumeration = localHashtable1.keys();
/* 160 */       while (localEnumeration.hasMoreElements()) {
/* 161 */         String str = (String)localEnumeration.nextElement();
/* 162 */         if (str.indexOf("security") != -1)
/*     */         {
/* 167 */           if (localHashtable2 == null) {
/* 168 */             localHashtable2 = (Hashtable)localHashtable1.clone();
/*     */           }
/* 170 */           localHashtable2.remove(str);
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 176 */     this.baseCtxEnv = (localHashtable2 == null ? localHashtable1 : localHashtable2);
/*     */   }
/*     */ 
/*     */   public DirContext getAttributeSyntaxDefinition()
/*     */     throws NamingException
/*     */   {
/* 185 */     DirContext localDirContext1 = getBaseCtx().getSchema(this.rdn);
/* 186 */     DirContext localDirContext2 = (DirContext)localDirContext1.lookup("AttributeDefinition/" + getID());
/*     */ 
/* 189 */     Attribute localAttribute = localDirContext2.getAttributes("").get("SYNTAX");
/*     */ 
/* 191 */     if ((localAttribute == null) || (localAttribute.size() == 0)) {
/* 192 */       throw new NameNotFoundException(getID() + "does not have a syntax associated with it");
/*     */     }
/*     */ 
/* 196 */     String str = (String)localAttribute.get();
/*     */ 
/* 199 */     return (DirContext)localDirContext1.lookup("SyntaxDefinition/" + str);
/*     */   }
/*     */ 
/*     */   public DirContext getAttributeDefinition()
/*     */     throws NamingException
/*     */   {
/* 209 */     DirContext localDirContext = getBaseCtx().getSchema(this.rdn);
/*     */ 
/* 211 */     return (DirContext)localDirContext.lookup("AttributeDefinition/" + getID());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.LdapAttribute
 * JD-Core Version:    0.6.2
 */