/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import com.sun.jndi.toolkit.ctx.Continuation;
/*     */ import java.io.IOException;
/*     */ import java.util.Vector;
/*     */ import javax.naming.Binding;
/*     */ import javax.naming.CompositeName;
/*     */ import javax.naming.InterruptedNamingException;
/*     */ import javax.naming.Name;
/*     */ import javax.naming.NamingEnumeration;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.SearchResult;
/*     */ import javax.naming.event.EventContext;
/*     */ import javax.naming.event.NamingEvent;
/*     */ import javax.naming.event.NamingExceptionEvent;
/*     */ import javax.naming.event.NamingListener;
/*     */ import javax.naming.ldap.Control;
/*     */ import javax.naming.ldap.HasControls;
/*     */ import javax.naming.ldap.LdapName;
/*     */ 
/*     */ final class NamingEventNotifier
/*     */   implements Runnable
/*     */ {
/*     */   private static final boolean debug = false;
/*     */   private Vector namingListeners;
/*     */   private Thread worker;
/*     */   private LdapCtx context;
/*     */   private EventContext eventSrc;
/*     */   private EventSupport support;
/*     */   private NamingEnumeration results;
/*     */   NotifierArgs info;
/*     */ 
/*     */   NamingEventNotifier(EventSupport paramEventSupport, LdapCtx paramLdapCtx, NotifierArgs paramNotifierArgs, NamingListener paramNamingListener)
/*     */     throws NamingException
/*     */   {
/*  65 */     this.info = paramNotifierArgs;
/*  66 */     this.support = paramEventSupport;
/*     */     PersistentSearchControl localPersistentSearchControl;
/*     */     try
/*     */     {
/*  70 */       localPersistentSearchControl = new PersistentSearchControl(paramNotifierArgs.mask, true, true, true);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*  76 */       NamingException localNamingException = new NamingException("Problem creating persistent search control");
/*     */ 
/*  78 */       localNamingException.setRootCause(localIOException);
/*  79 */       throw localNamingException;
/*     */     }
/*     */ 
/*  83 */     this.context = ((LdapCtx)paramLdapCtx.newInstance(new Control[] { localPersistentSearchControl }));
/*  84 */     this.eventSrc = paramLdapCtx;
/*     */ 
/*  86 */     this.namingListeners = new Vector();
/*  87 */     this.namingListeners.addElement(paramNamingListener);
/*     */ 
/*  89 */     this.worker = Obj.helper.createThread(this);
/*  90 */     this.worker.setDaemon(true);
/*  91 */     this.worker.start();
/*     */   }
/*     */ 
/*     */   void addNamingListener(NamingListener paramNamingListener)
/*     */   {
/*  96 */     this.namingListeners.addElement(paramNamingListener);
/*     */   }
/*     */ 
/*     */   void removeNamingListener(NamingListener paramNamingListener)
/*     */   {
/* 101 */     this.namingListeners.removeElement(paramNamingListener);
/*     */   }
/*     */ 
/*     */   boolean hasNamingListeners()
/*     */   {
/* 106 */     return this.namingListeners.size() > 0;
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/*     */     try
/*     */     {
/* 116 */       Continuation localContinuation = new Continuation();
/* 117 */       localContinuation.setError(this, this.info.name);
/* 118 */       Name localName = (this.info.name == null) || (this.info.name.equals("")) ? new CompositeName() : new CompositeName().add(this.info.name);
/*     */ 
/* 121 */       this.results = this.context.searchAux(localName, this.info.filter, this.info.controls, true, false, localContinuation);
/*     */ 
/* 127 */       ((LdapSearchEnumeration)this.results).setStartName(this.context.currentParsedDN);
/*     */ 
/* 134 */       while (this.results.hasMore()) {
/* 135 */         SearchResult localSearchResult = (SearchResult)this.results.next();
/* 136 */         Object localObject1 = (localSearchResult instanceof HasControls) ? ((HasControls)localSearchResult).getControls() : null;
/*     */ 
/* 145 */         if (localObject1 != null) {
/* 146 */           int i = 0; if (i < localObject1.length)
/*     */           {
/* 149 */             if ((localObject1[i] instanceof EntryChangeResponseControl)) {
/* 150 */               EntryChangeResponseControl localEntryChangeResponseControl = (EntryChangeResponseControl)localObject1[i];
/* 151 */               long l = localEntryChangeResponseControl.getChangeNumber();
/* 152 */               switch (localEntryChangeResponseControl.getChangeType()) {
/*     */               case 1:
/* 154 */                 fireObjectAdded(localSearchResult, l);
/* 155 */                 break;
/*     */               case 2:
/* 157 */                 fireObjectRemoved(localSearchResult, l);
/* 158 */                 break;
/*     */               case 4:
/* 160 */                 fireObjectChanged(localSearchResult, l);
/* 161 */                 break;
/*     */               case 8:
/* 163 */                 fireObjectRenamed(localSearchResult, localEntryChangeResponseControl.getPreviousDN(), l);
/*     */               case 3:
/*     */               case 5:
/*     */               case 6:
/*     */               case 7:
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     } catch (InterruptedNamingException localInterruptedNamingException) {
/*     */     }
/*     */     catch (NamingException localNamingException) {
/* 176 */       fireNamingException(localNamingException);
/*     */ 
/* 179 */       this.support.removeDeadNotifier(this.info);
/*     */     } finally {
/* 181 */       cleanup();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void cleanup()
/*     */   {
/*     */     try
/*     */     {
/* 190 */       if (this.results != null)
/*     */       {
/* 192 */         this.results.close();
/* 193 */         this.results = null;
/*     */       }
/* 195 */       if (this.context != null)
/*     */       {
/* 197 */         this.context.close();
/* 198 */         this.context = null;
/*     */       }
/*     */     }
/*     */     catch (NamingException localNamingException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   void stop()
/*     */   {
/* 209 */     if (this.worker != null) {
/* 210 */       this.worker.interrupt();
/* 211 */       this.worker = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void fireObjectAdded(Binding paramBinding, long paramLong)
/*     */   {
/* 219 */     if ((this.namingListeners == null) || (this.namingListeners.size() == 0)) {
/* 220 */       return;
/*     */     }
/* 222 */     NamingEvent localNamingEvent = new NamingEvent(this.eventSrc, 0, paramBinding, null, new Long(paramLong));
/*     */ 
/* 224 */     this.support.queueEvent(localNamingEvent, this.namingListeners);
/*     */   }
/*     */ 
/*     */   private void fireObjectRemoved(Binding paramBinding, long paramLong)
/*     */   {
/* 231 */     if ((this.namingListeners == null) || (this.namingListeners.size() == 0)) {
/* 232 */       return;
/*     */     }
/* 234 */     NamingEvent localNamingEvent = new NamingEvent(this.eventSrc, 1, null, paramBinding, new Long(paramLong));
/*     */ 
/* 236 */     this.support.queueEvent(localNamingEvent, this.namingListeners);
/*     */   }
/*     */ 
/*     */   private void fireObjectChanged(Binding paramBinding, long paramLong)
/*     */   {
/* 243 */     if ((this.namingListeners == null) || (this.namingListeners.size() == 0)) {
/* 244 */       return;
/*     */     }
/*     */ 
/* 247 */     Binding localBinding = new Binding(paramBinding.getName(), null, paramBinding.isRelative());
/*     */ 
/* 249 */     NamingEvent localNamingEvent = new NamingEvent(this.eventSrc, 3, paramBinding, localBinding, new Long(paramLong));
/*     */ 
/* 251 */     this.support.queueEvent(localNamingEvent, this.namingListeners);
/*     */   }
/*     */ 
/*     */   private void fireObjectRenamed(Binding paramBinding, String paramString, long paramLong)
/*     */   {
/* 258 */     if ((this.namingListeners == null) || (this.namingListeners.size() == 0)) {
/* 259 */       return;
/*     */     }
/* 261 */     Binding localBinding = null;
/*     */     try {
/* 263 */       LdapName localLdapName = new LdapName(paramString);
/* 264 */       if (localLdapName.startsWith(this.context.currentParsedDN)) {
/* 265 */         String str = localLdapName.getSuffix(this.context.currentParsedDN.size()).toString();
/* 266 */         localBinding = new Binding(str, null);
/*     */       }
/*     */     } catch (NamingException localNamingException) {
/*     */     }
/* 270 */     if (localBinding == null) {
/* 271 */       localBinding = new Binding(paramString, null, false);
/*     */     }
/*     */ 
/* 274 */     NamingEvent localNamingEvent = new NamingEvent(this.eventSrc, 2, paramBinding, localBinding, new Long(paramLong));
/*     */ 
/* 276 */     this.support.queueEvent(localNamingEvent, this.namingListeners);
/*     */   }
/*     */ 
/*     */   private void fireNamingException(NamingException paramNamingException) {
/* 280 */     if ((this.namingListeners == null) || (this.namingListeners.size() == 0)) {
/* 281 */       return;
/*     */     }
/* 283 */     NamingExceptionEvent localNamingExceptionEvent = new NamingExceptionEvent(this.eventSrc, paramNamingException);
/* 284 */     this.support.queueEvent(localNamingExceptionEvent, this.namingListeners);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.NamingEventNotifier
 * JD-Core Version:    0.6.2
 */