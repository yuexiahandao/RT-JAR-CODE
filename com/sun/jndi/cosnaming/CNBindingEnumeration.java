/*     */ package com.sun.jndi.cosnaming;
/*     */ 
/*     */ import java.util.Hashtable;
/*     */ import java.util.NoSuchElementException;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.spi.NamingManager;
/*     */ import org.omg.CosNaming.BindingIterator;
/*     */ import org.omg.CosNaming.BindingIteratorHolder;
/*     */ import org.omg.CosNaming.BindingListHolder;
/*     */ import org.omg.CosNaming.NameComponent;
/*     */ import org.omg.CosNaming.NamingContext;
/*     */ 
/*     */ final class CNBindingEnumeration
/*     */   implements NamingEnumeration
/*     */ {
/*     */   private static final int DEFAULT_BATCHSIZE = 100;
/*     */   private BindingListHolder _bindingList;
/*     */   private BindingIterator _bindingIter;
/*     */   private int counter;
/*  53 */   private int batchsize = 100;
/*     */   private CNCtx _ctx;
/*     */   private Hashtable _env;
/*  56 */   private boolean more = false;
/*  57 */   private boolean isLookedUpCtx = false;
/*     */ 
/*     */   CNBindingEnumeration(CNCtx paramCNCtx, boolean paramBoolean, Hashtable paramHashtable)
/*     */   {
/*  65 */     String str = paramHashtable != null ? (String)paramHashtable.get("java.naming.batchsize") : null;
/*     */ 
/*  67 */     if (str != null) {
/*     */       try {
/*  69 */         this.batchsize = Integer.parseInt(str);
/*     */       } catch (NumberFormatException localNumberFormatException) {
/*  71 */         throw new IllegalArgumentException("Batch size not numeric: " + str);
/*     */       }
/*     */     }
/*  74 */     this._ctx = paramCNCtx;
/*  75 */     this._ctx.incEnumCount();
/*  76 */     this.isLookedUpCtx = paramBoolean;
/*  77 */     this._env = paramHashtable;
/*  78 */     this._bindingList = new BindingListHolder();
/*  79 */     BindingIteratorHolder localBindingIteratorHolder = new BindingIteratorHolder();
/*     */ 
/*  83 */     this._ctx._nc.list(0, this._bindingList, localBindingIteratorHolder);
/*     */ 
/*  85 */     this._bindingIter = localBindingIteratorHolder.value;
/*     */ 
/*  88 */     if (this._bindingIter != null)
/*  89 */       this.more = this._bindingIter.next_n(this.batchsize, this._bindingList);
/*     */     else {
/*  91 */       this.more = false;
/*     */     }
/*  93 */     this.counter = 0;
/*     */   }
/*     */ 
/*     */   public Object next()
/*     */     throws NamingException
/*     */   {
/* 102 */     if ((this.more) && (this.counter >= this._bindingList.value.length)) {
/* 103 */       getMore();
/*     */     }
/* 105 */     if ((this.more) && (this.counter < this._bindingList.value.length)) {
/* 106 */       org.omg.CosNaming.Binding localBinding = this._bindingList.value[this.counter];
/* 107 */       this.counter += 1;
/* 108 */       return mapBinding(localBinding);
/*     */     }
/* 110 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */   public boolean hasMore()
/*     */     throws NamingException
/*     */   {
/* 124 */     return (this.counter < this._bindingList.value.length) || (getMore());
/*     */   }
/*     */ 
/*     */   public boolean hasMoreElements()
/*     */   {
/*     */     try
/*     */     {
/* 135 */       return hasMore(); } catch (NamingException localNamingException) {
/*     */     }
/* 137 */     return false;
/*     */   }
/*     */ 
/*     */   public Object nextElement()
/*     */   {
/*     */     try
/*     */     {
/* 149 */       return next(); } catch (NamingException localNamingException) {
/*     */     }
/* 151 */     throw new NoSuchElementException();
/*     */   }
/*     */ 
/*     */   public void close() throws NamingException
/*     */   {
/* 156 */     this.more = false;
/* 157 */     if (this._bindingIter != null) {
/* 158 */       this._bindingIter.destroy();
/* 159 */       this._bindingIter = null;
/*     */     }
/* 161 */     if (this._ctx != null) {
/* 162 */       this._ctx.decEnumCount();
/*     */ 
/* 168 */       if (this.isLookedUpCtx) {
/* 169 */         this._ctx.close();
/*     */       }
/* 171 */       this._ctx = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void finalize() {
/*     */     try {
/* 177 */       close();
/*     */     }
/*     */     catch (NamingException localNamingException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean getMore() throws NamingException
/*     */   {
/*     */     try
/*     */     {
/* 188 */       this.more = this._bindingIter.next_n(this.batchsize, this._bindingList);
/* 189 */       this.counter = 0;
/*     */     } catch (Exception localException) {
/* 191 */       this.more = false;
/* 192 */       NamingException localNamingException = new NamingException("Problem getting binding list");
/*     */ 
/* 194 */       localNamingException.setRootCause(localException);
/* 195 */       throw localNamingException;
/*     */     }
/* 197 */     return this.more;
/*     */   }
/*     */ 
/*     */   private javax.naming.Binding mapBinding(org.omg.CosNaming.Binding paramBinding)
/*     */     throws NamingException
/*     */   {
/* 211 */     Object localObject1 = this._ctx.callResolve(paramBinding.binding_name);
/*     */ 
/* 213 */     Name localName = CNNameParser.cosNameToName(paramBinding.binding_name);
/*     */     try
/*     */     {
/* 216 */       localObject1 = NamingManager.getObjectInstance(localObject1, localName, this._ctx, this._env);
/*     */     } catch (NamingException localNamingException) {
/* 218 */       throw localNamingException;
/*     */     } catch (Exception localException) {
/* 220 */       localObject2 = new NamingException("problem generating object using object factory");
/*     */ 
/* 222 */       ((NamingException)localObject2).setRootCause(localException);
/* 223 */       throw ((Throwable)localObject2);
/*     */     }
/*     */ 
/* 228 */     String str1 = localName.toString();
/* 229 */     Object localObject2 = new javax.naming.Binding(str1, localObject1);
/*     */ 
/* 231 */     NameComponent[] arrayOfNameComponent = this._ctx.makeFullName(paramBinding.binding_name);
/* 232 */     String str2 = CNNameParser.cosNameToInsString(arrayOfNameComponent);
/* 233 */     ((javax.naming.Binding)localObject2).setNameInNamespace(str2);
/* 234 */     return localObject2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.cosnaming.CNBindingEnumeration
 * JD-Core Version:    0.6.2
 */