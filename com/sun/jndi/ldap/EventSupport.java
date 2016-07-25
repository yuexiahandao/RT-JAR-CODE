/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.EventObject;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Vector;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.directory.SearchControls;
/*     */ import javax.naming.event.NamespaceChangeListener;
/*     */ import javax.naming.event.NamingExceptionEvent;
/*     */ import javax.naming.event.NamingListener;
/*     */ import javax.naming.event.ObjectChangeListener;
/*     */ import javax.naming.ldap.UnsolicitedNotification;
/*     */ import javax.naming.ldap.UnsolicitedNotificationEvent;
/*     */ import javax.naming.ldap.UnsolicitedNotificationListener;
/*     */ 
/*     */ final class EventSupport
/*     */ {
/*     */   private static final boolean debug = false;
/*     */   private LdapCtx ctx;
/* 123 */   private Hashtable notifiers = new Hashtable(11);
/*     */ 
/* 128 */   private Vector unsolicited = null;
/*     */   private EventQueue eventQueue;
/*     */ 
/*     */   EventSupport(LdapCtx paramLdapCtx)
/*     */   {
/* 138 */     this.ctx = paramLdapCtx;
/*     */   }
/*     */ 
/*     */   synchronized void addNamingListener(String paramString, int paramInt, NamingListener paramNamingListener)
/*     */     throws NamingException
/*     */   {
/* 154 */     if (((paramNamingListener instanceof ObjectChangeListener)) || ((paramNamingListener instanceof NamespaceChangeListener)))
/*     */     {
/* 156 */       NotifierArgs localNotifierArgs = new NotifierArgs(paramString, paramInt, paramNamingListener);
/*     */ 
/* 158 */       NamingEventNotifier localNamingEventNotifier = (NamingEventNotifier)this.notifiers.get(localNotifierArgs);
/*     */ 
/* 160 */       if (localNamingEventNotifier == null) {
/* 161 */         localNamingEventNotifier = new NamingEventNotifier(this, this.ctx, localNotifierArgs, paramNamingListener);
/* 162 */         this.notifiers.put(localNotifierArgs, localNamingEventNotifier);
/*     */       } else {
/* 164 */         localNamingEventNotifier.addNamingListener(paramNamingListener);
/*     */       }
/*     */     }
/* 167 */     if ((paramNamingListener instanceof UnsolicitedNotificationListener))
/*     */     {
/* 169 */       if (this.unsolicited == null) {
/* 170 */         this.unsolicited = new Vector(3);
/*     */       }
/*     */ 
/* 173 */       this.unsolicited.addElement(paramNamingListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void addNamingListener(String paramString1, String paramString2, SearchControls paramSearchControls, NamingListener paramNamingListener)
/*     */     throws NamingException
/*     */   {
/* 184 */     if (((paramNamingListener instanceof ObjectChangeListener)) || ((paramNamingListener instanceof NamespaceChangeListener)))
/*     */     {
/* 186 */       NotifierArgs localNotifierArgs = new NotifierArgs(paramString1, paramString2, paramSearchControls, paramNamingListener);
/*     */ 
/* 188 */       NamingEventNotifier localNamingEventNotifier = (NamingEventNotifier)this.notifiers.get(localNotifierArgs);
/*     */ 
/* 190 */       if (localNamingEventNotifier == null) {
/* 191 */         localNamingEventNotifier = new NamingEventNotifier(this, this.ctx, localNotifierArgs, paramNamingListener);
/* 192 */         this.notifiers.put(localNotifierArgs, localNamingEventNotifier);
/*     */       } else {
/* 194 */         localNamingEventNotifier.addNamingListener(paramNamingListener);
/*     */       }
/*     */     }
/* 197 */     if ((paramNamingListener instanceof UnsolicitedNotificationListener))
/*     */     {
/* 199 */       if (this.unsolicited == null) {
/* 200 */         this.unsolicited = new Vector(3);
/*     */       }
/* 202 */       this.unsolicited.addElement(paramNamingListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void removeNamingListener(NamingListener paramNamingListener)
/*     */   {
/* 210 */     Enumeration localEnumeration = this.notifiers.elements();
/*     */ 
/* 217 */     while (localEnumeration.hasMoreElements()) {
/* 218 */       NamingEventNotifier localNamingEventNotifier = (NamingEventNotifier)localEnumeration.nextElement();
/* 219 */       if (localNamingEventNotifier != null)
/*     */       {
/* 222 */         localNamingEventNotifier.removeNamingListener(paramNamingListener);
/* 223 */         if (!localNamingEventNotifier.hasNamingListeners())
/*     */         {
/* 226 */           localNamingEventNotifier.stop();
/* 227 */           this.notifiers.remove(localNamingEventNotifier.info);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 235 */     if (this.unsolicited != null)
/* 236 */       this.unsolicited.removeElement(paramNamingListener);
/*     */   }
/*     */ 
/*     */   synchronized boolean hasUnsolicited()
/*     */   {
/* 242 */     return (this.unsolicited != null) && (this.unsolicited.size() > 0);
/*     */   }
/*     */ 
/*     */   synchronized void removeDeadNotifier(NotifierArgs paramNotifierArgs)
/*     */   {
/* 254 */     this.notifiers.remove(paramNotifierArgs);
/*     */   }
/*     */ 
/*     */   synchronized void fireUnsolicited(Object paramObject)
/*     */   {
/* 267 */     if ((this.unsolicited == null) || (this.unsolicited.size() == 0))
/*     */       return;
/*     */     Object localObject;
/* 274 */     if ((paramObject instanceof UnsolicitedNotification))
/*     */     {
/* 278 */       localObject = new UnsolicitedNotificationEvent(this.ctx, (UnsolicitedNotification)paramObject);
/*     */ 
/* 280 */       queueEvent((EventObject)localObject, this.unsolicited);
/*     */     }
/* 282 */     else if ((paramObject instanceof NamingException))
/*     */     {
/* 286 */       localObject = new NamingExceptionEvent(this.ctx, (NamingException)paramObject);
/*     */ 
/* 288 */       queueEvent((EventObject)localObject, this.unsolicited);
/*     */ 
/* 296 */       this.unsolicited = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void cleanup()
/*     */   {
/* 307 */     if (this.notifiers != null) {
/* 308 */       for (Enumeration localEnumeration = this.notifiers.elements(); localEnumeration.hasMoreElements(); ) {
/* 309 */         ((NamingEventNotifier)localEnumeration.nextElement()).stop();
/*     */       }
/* 311 */       this.notifiers = null;
/*     */     }
/* 313 */     if (this.eventQueue != null) {
/* 314 */       this.eventQueue.stop();
/* 315 */       this.eventQueue = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void queueEvent(EventObject paramEventObject, Vector paramVector)
/*     */   {
/* 332 */     if (this.eventQueue == null) {
/* 333 */       this.eventQueue = new EventQueue();
/*     */     }
/*     */ 
/* 343 */     Vector localVector = (Vector)paramVector.clone();
/* 344 */     this.eventQueue.enqueue(paramEventObject, localVector);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.EventSupport
 * JD-Core Version:    0.6.2
 */