/*     */ package com.sun.jndi.toolkit.dir;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.naming.Binding;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.Context;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.Attributes;
/*     */ import javax.naming.directory.DirContext;
/*     */ import javax.naming.directory.SearchControls;
/*     */ import javax.naming.directory.SearchResult;
/*     */ import javax.naming.spi.DirectoryManager;
/*     */ 
/*     */ public final class LazySearchEnumerationImpl
/*     */   implements NamingEnumeration
/*     */ {
/*     */   private NamingEnumeration candidates;
/*  52 */   private SearchResult nextMatch = null;
/*     */   private SearchControls cons;
/*     */   private AttrFilter filter;
/*     */   private Context context;
/*     */   private Hashtable env;
/*  57 */   private boolean useFactory = true;
/*     */ 
/*     */   public LazySearchEnumerationImpl(NamingEnumeration paramNamingEnumeration, AttrFilter paramAttrFilter, SearchControls paramSearchControls) throws NamingException
/*     */   {
/*  61 */     this.candidates = paramNamingEnumeration;
/*  62 */     this.filter = paramAttrFilter;
/*     */ 
/*  64 */     if (paramSearchControls == null)
/*  65 */       this.cons = new SearchControls();
/*     */     else
/*  67 */       this.cons = paramSearchControls;
/*     */   }
/*     */ 
/*     */   public LazySearchEnumerationImpl(NamingEnumeration paramNamingEnumeration, AttrFilter paramAttrFilter, SearchControls paramSearchControls, Context paramContext, Hashtable paramHashtable, boolean paramBoolean)
/*     */     throws NamingException
/*     */   {
/*  75 */     this.candidates = paramNamingEnumeration;
/*  76 */     this.filter = paramAttrFilter;
/*  77 */     this.env = paramHashtable;
/*  78 */     this.context = paramContext;
/*  79 */     this.useFactory = paramBoolean;
/*     */ 
/*  81 */     if (paramSearchControls == null)
/*  82 */       this.cons = new SearchControls();
/*     */     else
/*  84 */       this.cons = paramSearchControls;
/*     */   }
/*     */ 
/*     */   public LazySearchEnumerationImpl(NamingEnumeration paramNamingEnumeration, AttrFilter paramAttrFilter, SearchControls paramSearchControls, Context paramContext, Hashtable paramHashtable)
/*     */     throws NamingException
/*     */   {
/*  92 */     this(paramNamingEnumeration, paramAttrFilter, paramSearchControls, paramContext, paramHashtable, true);
/*     */   }
/*     */ 
/*     */   public boolean hasMore() throws NamingException
/*     */   {
/*  97 */     return findNextMatch(false) != null;
/*     */   }
/*     */ 
/*     */   public boolean hasMoreElements() {
/*     */     try {
/* 102 */       return hasMore(); } catch (NamingException localNamingException) {
/*     */     }
/* 104 */     return false;
/*     */   }
/*     */ 
/*     */   public Object nextElement()
/*     */   {
/*     */     try {
/* 110 */       return findNextMatch(true);
/*     */     } catch (NamingException localNamingException) {
/* 112 */       throw new NoSuchElementException(localNamingException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object next() throws NamingException
/*     */   {
/* 118 */     return findNextMatch(true);
/*     */   }
/*     */ 
/*     */   public void close() throws NamingException {
/* 122 */     if (this.candidates != null)
/* 123 */       this.candidates.close();
/*     */   }
/*     */ 
/*     */   private SearchResult findNextMatch(boolean paramBoolean)
/*     */     throws NamingException
/*     */   {
/*     */     SearchResult localSearchResult;
/* 129 */     if (this.nextMatch != null) {
/* 130 */       localSearchResult = this.nextMatch;
/* 131 */       if (paramBoolean) {
/* 132 */         this.nextMatch = null;
/*     */       }
/* 134 */       return localSearchResult;
/*     */     }
/*     */ 
/* 140 */     while (this.candidates.hasMore()) {
/* 141 */       Binding localBinding = (Binding)this.candidates.next();
/* 142 */       Object localObject = localBinding.getObject();
/* 143 */       if ((localObject instanceof DirContext)) {
/* 144 */         Attributes localAttributes = ((DirContext)localObject).getAttributes("");
/* 145 */         if (this.filter.check(localAttributes)) {
/* 146 */           if (!this.cons.getReturningObjFlag())
/* 147 */             localObject = null;
/* 148 */           else if (this.useFactory)
/*     */           {
/*     */             try
/*     */             {
/* 153 */               Name localName = this.context != null ? new CompositeName(localBinding.getName()) : null;
/*     */ 
/* 155 */               localObject = DirectoryManager.getObjectInstance(localObject, localName, this.context, this.env, localAttributes);
/*     */             }
/*     */             catch (NamingException localNamingException1) {
/* 158 */               throw localNamingException1;
/*     */             } catch (Exception localException) {
/* 160 */               NamingException localNamingException2 = new NamingException("problem generating object using object factory");
/*     */ 
/* 162 */               localNamingException2.setRootCause(localException);
/* 163 */               throw localNamingException2;
/*     */             }
/*     */           }
/* 166 */           localSearchResult = new SearchResult(localBinding.getName(), localBinding.getClassName(), localObject, SearchFilter.selectAttributes(localAttributes, this.cons.getReturningAttributes()), true);
/*     */ 
/* 171 */           if (!paramBoolean)
/* 172 */             this.nextMatch = localSearchResult;
/* 173 */           return localSearchResult;
/*     */         }
/*     */       }
/*     */     }
/* 177 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.toolkit.dir.LazySearchEnumerationImpl
 * JD-Core Version:    0.6.2
 */