/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import javax.naming.directory.SearchControls;
/*     */ import javax.naming.event.NamespaceChangeListener;
/*     */ import javax.naming.event.NamingListener;
/*     */ import javax.naming.event.ObjectChangeListener;
/*     */ 
/*     */ final class NotifierArgs
/*     */ {
/*     */   static final int ADDED_MASK = 1;
/*     */   static final int REMOVED_MASK = 2;
/*     */   static final int CHANGED_MASK = 4;
/*     */   static final int RENAMED_MASK = 8;
/*     */   String name;
/*     */   String filter;
/*     */   SearchControls controls;
/*     */   int mask;
/* 125 */   private int sum = -1;
/*     */ 
/*     */   NotifierArgs(String paramString, int paramInt, NamingListener paramNamingListener)
/*     */   {
/*  56 */     this(paramString, "(objectclass=*)", null, paramNamingListener);
/*     */ 
/*  59 */     if (paramInt != 1) {
/*  60 */       this.controls = new SearchControls();
/*  61 */       this.controls.setSearchScope(paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   NotifierArgs(String paramString1, String paramString2, SearchControls paramSearchControls, NamingListener paramNamingListener)
/*     */   {
/*  68 */     this.name = paramString1;
/*  69 */     this.filter = paramString2;
/*  70 */     this.controls = paramSearchControls;
/*     */ 
/*  72 */     if ((paramNamingListener instanceof NamespaceChangeListener)) {
/*  73 */       this.mask |= 11;
/*     */     }
/*  75 */     if ((paramNamingListener instanceof ObjectChangeListener))
/*  76 */       this.mask |= 4;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/*  82 */     if ((paramObject instanceof NotifierArgs)) {
/*  83 */       NotifierArgs localNotifierArgs = (NotifierArgs)paramObject;
/*  84 */       return (this.mask == localNotifierArgs.mask) && (this.name.equals(localNotifierArgs.name)) && (this.filter.equals(localNotifierArgs.filter)) && (checkControls(localNotifierArgs.controls));
/*     */     }
/*     */ 
/*  88 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean checkControls(SearchControls paramSearchControls) {
/*  92 */     if ((this.controls == null) || (paramSearchControls == null)) {
/*  93 */       return paramSearchControls == this.controls;
/*     */     }
/*     */ 
/*  97 */     return (this.controls.getSearchScope() == paramSearchControls.getSearchScope()) && (this.controls.getTimeLimit() == paramSearchControls.getTimeLimit()) && (this.controls.getDerefLinkFlag() == paramSearchControls.getDerefLinkFlag()) && (this.controls.getReturningObjFlag() == paramSearchControls.getReturningObjFlag()) && (this.controls.getCountLimit() == paramSearchControls.getCountLimit()) && (checkStringArrays(this.controls.getReturningAttributes(), paramSearchControls.getReturningAttributes()));
/*     */   }
/*     */ 
/*     */   private static boolean checkStringArrays(String[] paramArrayOfString1, String[] paramArrayOfString2)
/*     */   {
/* 107 */     if ((paramArrayOfString1 == null) || (paramArrayOfString2 == null)) {
/* 108 */       return paramArrayOfString1 == paramArrayOfString2;
/*     */     }
/*     */ 
/* 112 */     if (paramArrayOfString1.length != paramArrayOfString2.length) {
/* 113 */       return false;
/*     */     }
/*     */ 
/* 116 */     for (int i = 0; i < paramArrayOfString1.length; i++) {
/* 117 */       if (!paramArrayOfString1[i].equals(paramArrayOfString2[i])) {
/* 118 */         return false;
/*     */       }
/*     */     }
/* 121 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 127 */     if (this.sum == -1)
/* 128 */       this.sum = (this.mask + this.name.hashCode() + this.filter.hashCode() + controlsCode());
/* 129 */     return this.sum;
/*     */   }
/*     */ 
/*     */   private int controlsCode()
/*     */   {
/* 134 */     if (this.controls == null) return 0;
/*     */ 
/* 136 */     int i = this.controls.getTimeLimit() + (int)this.controls.getCountLimit() + (this.controls.getDerefLinkFlag() ? 1 : 0) + (this.controls.getReturningObjFlag() ? 1 : 0);
/*     */ 
/* 140 */     String[] arrayOfString = this.controls.getReturningAttributes();
/* 141 */     if (arrayOfString != null) {
/* 142 */       for (int j = 0; j < arrayOfString.length; j++) {
/* 143 */         i += arrayOfString[j].hashCode();
/*     */       }
/*     */     }
/*     */ 
/* 147 */     return i;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.NotifierArgs
 * JD-Core Version:    0.6.2
 */