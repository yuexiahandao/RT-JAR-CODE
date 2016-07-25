/*     */ package com.sun.jmx.mbeanserver;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock.ReadLock;
/*     */ import java.util.concurrent.locks.ReentrantReadWriteLock.WriteLock;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.management.DynamicMBean;
/*     */ import javax.management.InstanceAlreadyExistsException;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.QueryExp;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ 
/*     */ public class Repository
/*     */ {
/*     */   private final Map<String, Map<String, NamedObject>> domainTb;
/*  89 */   private volatile int nbElements = 0;
/*     */   private final String domain;
/*     */   private final ReentrantReadWriteLock lock;
/*     */ 
/*     */   private void addAllMatching(Map<String, NamedObject> paramMap, Set<NamedObject> paramSet, ObjectNamePattern paramObjectNamePattern)
/*     */   {
/* 229 */     synchronized (paramMap) {
/* 230 */       for (NamedObject localNamedObject : paramMap.values()) {
/* 231 */         ObjectName localObjectName = localNamedObject.getName();
/*     */ 
/* 233 */         if (paramObjectNamePattern.matchKeys(localObjectName)) paramSet.add(localNamedObject);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addNewDomMoi(DynamicMBean paramDynamicMBean, String paramString, ObjectName paramObjectName, RegistrationContext paramRegistrationContext)
/*     */   {
/* 242 */     HashMap localHashMap = new HashMap();
/*     */ 
/* 244 */     String str = paramObjectName.getCanonicalKeyPropertyListString();
/* 245 */     addMoiToTb(paramDynamicMBean, paramObjectName, str, localHashMap, paramRegistrationContext);
/* 246 */     this.domainTb.put(paramString, localHashMap);
/* 247 */     this.nbElements += 1;
/*     */   }
/*     */ 
/*     */   private void registering(RegistrationContext paramRegistrationContext) {
/* 251 */     if (paramRegistrationContext == null) return; try
/*     */     {
/* 253 */       paramRegistrationContext.registering();
/*     */     } catch (RuntimeOperationsException localRuntimeOperationsException) {
/* 255 */       throw localRuntimeOperationsException;
/*     */     } catch (RuntimeException localRuntimeException) {
/* 257 */       throw new RuntimeOperationsException(localRuntimeException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void unregistering(RegistrationContext paramRegistrationContext, ObjectName paramObjectName) {
/* 262 */     if (paramRegistrationContext == null) return; try
/*     */     {
/* 264 */       paramRegistrationContext.unregistered();
/*     */     }
/*     */     catch (Exception localException) {
/* 267 */       JmxProperties.MBEANSERVER_LOGGER.log(Level.FINE, "Unexpected exception while unregistering " + paramObjectName, localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void addMoiToTb(DynamicMBean paramDynamicMBean, ObjectName paramObjectName, String paramString, Map<String, NamedObject> paramMap, RegistrationContext paramRegistrationContext)
/*     */   {
/* 278 */     registering(paramRegistrationContext);
/* 279 */     paramMap.put(paramString, new NamedObject(paramObjectName, paramDynamicMBean));
/*     */   }
/*     */ 
/*     */   private NamedObject retrieveNamedObject(ObjectName paramObjectName)
/*     */   {
/* 289 */     if (paramObjectName.isPattern()) return null;
/*     */ 
/* 292 */     String str = paramObjectName.getDomain().intern();
/*     */ 
/* 295 */     if (str.length() == 0) {
/* 296 */       str = this.domain;
/*     */     }
/*     */ 
/* 299 */     Map localMap = (Map)this.domainTb.get(str);
/* 300 */     if (localMap == null) {
/* 301 */       return null;
/*     */     }
/*     */ 
/* 304 */     return (NamedObject)localMap.get(paramObjectName.getCanonicalKeyPropertyListString());
/*     */   }
/*     */ 
/*     */   public Repository(String paramString)
/*     */   {
/* 319 */     this(paramString, true);
/*     */   }
/*     */ 
/*     */   public Repository(String paramString, boolean paramBoolean)
/*     */   {
/* 326 */     this.lock = new ReentrantReadWriteLock(paramBoolean);
/*     */ 
/* 328 */     this.domainTb = new HashMap(5);
/*     */ 
/* 330 */     if ((paramString != null) && (paramString.length() != 0))
/* 331 */       this.domain = paramString.intern();
/*     */     else {
/* 333 */       this.domain = "DefaultDomain";
/*     */     }
/*     */ 
/* 336 */     this.domainTb.put(this.domain, new HashMap());
/*     */   }
/*     */ 
/*     */   public String[] getDomains()
/*     */   {
/* 346 */     this.lock.readLock().lock();
/*     */     ArrayList localArrayList;
/*     */     try
/*     */     {
/* 350 */       localArrayList = new ArrayList(this.domainTb.size());
/*     */ 
/* 352 */       for (Map.Entry localEntry : this.domainTb.entrySet())
/*     */       {
/* 356 */         Map localMap = (Map)localEntry.getValue();
/* 357 */         if ((localMap != null) && (localMap.size() != 0))
/* 358 */           localArrayList.add(localEntry.getKey());
/*     */       }
/*     */     } finally {
/* 361 */       this.lock.readLock().unlock();
/*     */     }
/*     */ 
/* 365 */     return (String[])localArrayList.toArray(new String[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   public void addMBean(DynamicMBean paramDynamicMBean, ObjectName paramObjectName, RegistrationContext paramRegistrationContext)
/*     */     throws InstanceAlreadyExistsException
/*     */   {
/* 388 */     if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/* 389 */       JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, Repository.class.getName(), "addMBean", "name = " + paramObjectName);
/*     */     }
/*     */ 
/* 394 */     String str1 = paramObjectName.getDomain().intern();
/* 395 */     int i = 0;
/*     */ 
/* 398 */     if (str1.length() == 0) {
/* 399 */       paramObjectName = Util.newObjectName(this.domain + paramObjectName.toString());
/*     */     }
/*     */ 
/* 402 */     if (str1 == this.domain) {
/* 403 */       i = 1;
/* 404 */       str1 = this.domain;
/*     */     } else {
/* 406 */       i = 0;
/*     */     }
/*     */ 
/* 410 */     if (paramObjectName.isPattern()) {
/* 411 */       throw new RuntimeOperationsException(new IllegalArgumentException("Repository: cannot add mbean for pattern name " + paramObjectName.toString()));
/*     */     }
/*     */ 
/* 416 */     this.lock.writeLock().lock();
/*     */     try
/*     */     {
/* 419 */       if ((i == 0) && (str1.equals("JMImplementation")) && (this.domainTb.containsKey("JMImplementation")))
/*     */       {
/* 422 */         throw new RuntimeOperationsException(new IllegalArgumentException("Repository: domain name cannot be JMImplementation"));
/*     */       }
/*     */ 
/* 428 */       Map localMap = (Map)this.domainTb.get(str1);
/* 429 */       if (localMap == null) {
/* 430 */         addNewDomMoi(paramDynamicMBean, str1, paramObjectName, paramRegistrationContext);
/*     */       }
/*     */       else
/*     */       {
/* 434 */         String str2 = paramObjectName.getCanonicalKeyPropertyListString();
/* 435 */         NamedObject localNamedObject = (NamedObject)localMap.get(str2);
/* 436 */         if (localNamedObject != null) {
/* 437 */           throw new InstanceAlreadyExistsException(paramObjectName.toString());
/*     */         }
/* 439 */         this.nbElements += 1;
/* 440 */         addMoiToTb(paramDynamicMBean, paramObjectName, str2, localMap, paramRegistrationContext);
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 445 */       this.lock.writeLock().unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean contains(ObjectName paramObjectName)
/*     */   {
/* 459 */     if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/* 460 */       JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, Repository.class.getName(), "contains", " name = " + paramObjectName);
/*     */     }
/*     */ 
/* 463 */     this.lock.readLock().lock();
/*     */     try {
/* 465 */       return retrieveNamedObject(paramObjectName) != null;
/*     */     } finally {
/* 467 */       this.lock.readLock().unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public DynamicMBean retrieve(ObjectName paramObjectName)
/*     */   {
/* 481 */     if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/* 482 */       JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, Repository.class.getName(), "retrieve", "name = " + paramObjectName);
/*     */     }
/*     */ 
/* 487 */     this.lock.readLock().lock();
/*     */     try {
/* 489 */       NamedObject localNamedObject = retrieveNamedObject(paramObjectName);
/*     */       DynamicMBean localDynamicMBean;
/* 490 */       if (localNamedObject == null) return null;
/* 491 */       return localNamedObject.getObject();
/*     */     } finally {
/* 493 */       this.lock.readLock().unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Set<NamedObject> query(ObjectName paramObjectName, QueryExp paramQueryExp)
/*     */   {
/* 513 */     HashSet localHashSet = new HashSet();
/*     */     ObjectName localObjectName;
/* 522 */     if ((paramObjectName == null) || (paramObjectName.getCanonicalName().length() == 0) || (paramObjectName.equals(ObjectName.WILDCARD)))
/*     */     {
/* 525 */       localObjectName = ObjectName.WILDCARD;
/*     */     } else localObjectName = paramObjectName;
/*     */ 
/* 528 */     this.lock.readLock().lock();
/*     */     try
/*     */     {
/*     */       Object localObject2;
/* 532 */       if (!localObjectName.isPattern()) {
/* 533 */         localObject1 = retrieveNamedObject(localObjectName);
/* 534 */         if (localObject1 != null) localHashSet.add(localObject1);
/* 535 */         return localHashSet;
/*     */       }
/*     */ 
/* 539 */       if (localObjectName == ObjectName.WILDCARD) {
/* 540 */         for (localObject1 = this.domainTb.values().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (Map)((Iterator)localObject1).next();
/* 541 */           localHashSet.addAll(((Map)localObject2).values());
/*     */         }
/* 543 */         return localHashSet;
/*     */       }
/*     */ 
/* 546 */       Object localObject1 = localObjectName.getCanonicalKeyPropertyListString();
/*     */ 
/* 548 */       int i = ((String)localObject1).length() == 0 ? 1 : 0;
/*     */ 
/* 550 */       ObjectNamePattern localObjectNamePattern = i != 0 ? null : new ObjectNamePattern(localObjectName);
/*     */ 
/* 554 */       if (localObjectName.getDomain().length() == 0) {
/* 555 */         localObject3 = (Map)this.domainTb.get(this.domain);
/* 556 */         if (i != 0)
/* 557 */           localHashSet.addAll(((Map)localObject3).values());
/*     */         else
/* 559 */           addAllMatching((Map)localObject3, localHashSet, localObjectNamePattern);
/* 560 */         return localHashSet;
/*     */       }
/*     */ 
/* 563 */       if (!localObjectName.isDomainPattern()) {
/* 564 */         localObject3 = (Map)this.domainTb.get(localObjectName.getDomain());
/* 565 */         if (localObject3 == null) return Collections.emptySet();
/* 566 */         if (i != 0)
/* 567 */           localHashSet.addAll(((Map)localObject3).values());
/*     */         else
/* 569 */           addAllMatching((Map)localObject3, localHashSet, localObjectNamePattern);
/* 570 */         return localHashSet;
/*     */       }
/*     */ 
/* 574 */       Object localObject3 = localObjectName.getDomain();
/* 575 */       for (Object localObject4 = this.domainTb.keySet().iterator(); ((Iterator)localObject4).hasNext(); ) { String str = (String)((Iterator)localObject4).next();
/* 576 */         if (Util.wildmatch(str, (String)localObject3)) {
/* 577 */           Map localMap = (Map)this.domainTb.get(str);
/* 578 */           if (i != 0)
/* 579 */             localHashSet.addAll(localMap.values());
/*     */           else
/* 581 */             addAllMatching(localMap, localHashSet, localObjectNamePattern);
/*     */         }
/*     */       }
/* 584 */       return localHashSet;
/*     */     } finally {
/* 586 */       this.lock.readLock().unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void remove(ObjectName paramObjectName, RegistrationContext paramRegistrationContext)
/*     */     throws InstanceNotFoundException
/*     */   {
/* 612 */     if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINER)) {
/* 613 */       JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINER, Repository.class.getName(), "remove", "name = " + paramObjectName);
/*     */     }
/*     */ 
/* 618 */     String str = paramObjectName.getDomain().intern();
/*     */ 
/* 621 */     if (str.length() == 0) str = this.domain;
/*     */ 
/* 623 */     this.lock.writeLock().lock();
/*     */     try
/*     */     {
/* 626 */       Map localMap = (Map)this.domainTb.get(str);
/* 627 */       if (localMap == null) {
/* 628 */         throw new InstanceNotFoundException(paramObjectName.toString());
/*     */       }
/*     */ 
/* 632 */       if (localMap.remove(paramObjectName.getCanonicalKeyPropertyListString()) == null) {
/* 633 */         throw new InstanceNotFoundException(paramObjectName.toString());
/*     */       }
/*     */ 
/* 637 */       this.nbElements -= 1;
/*     */ 
/* 640 */       if (localMap.isEmpty()) {
/* 641 */         this.domainTb.remove(str);
/*     */ 
/* 647 */         if (str == this.domain) {
/* 648 */           this.domainTb.put(this.domain, new HashMap());
/*     */         }
/*     */       }
/* 651 */       unregistering(paramRegistrationContext, paramObjectName);
/*     */     }
/*     */     finally {
/* 654 */       this.lock.writeLock().unlock();
/*     */     }
/*     */   }
/*     */ 
/*     */   public Integer getCount()
/*     */   {
/* 664 */     return Integer.valueOf(this.nbElements);
/*     */   }
/*     */ 
/*     */   public String getDefaultDomain()
/*     */   {
/* 674 */     return this.domain;
/*     */   }
/*     */ 
/*     */   private static final class ObjectNamePattern
/*     */   {
/*     */     private final String[] keys;
/*     */     private final String[] values;
/*     */     private final String properties;
/*     */     private final boolean isPropertyListPattern;
/*     */     private final boolean isPropertyValuePattern;
/*     */     public final ObjectName pattern;
/*     */ 
/*     */     public ObjectNamePattern(ObjectName paramObjectName)
/*     */     {
/* 128 */       this(paramObjectName.isPropertyListPattern(), paramObjectName.isPropertyValuePattern(), paramObjectName.getCanonicalKeyPropertyListString(), paramObjectName.getKeyPropertyList(), paramObjectName);
/*     */     }
/*     */ 
/*     */     ObjectNamePattern(boolean paramBoolean1, boolean paramBoolean2, String paramString, Map<String, String> paramMap, ObjectName paramObjectName)
/*     */     {
/* 149 */       this.isPropertyListPattern = paramBoolean1;
/* 150 */       this.isPropertyValuePattern = paramBoolean2;
/* 151 */       this.properties = paramString;
/* 152 */       int i = paramMap.size();
/* 153 */       this.keys = new String[i];
/* 154 */       this.values = new String[i];
/* 155 */       int j = 0;
/* 156 */       for (Map.Entry localEntry : paramMap.entrySet()) {
/* 157 */         this.keys[j] = ((String)localEntry.getKey());
/* 158 */         this.values[j] = ((String)localEntry.getValue());
/* 159 */         j++;
/*     */       }
/* 161 */       this.pattern = paramObjectName;
/*     */     }
/*     */ 
/*     */     public boolean matchKeys(ObjectName paramObjectName)
/*     */     {
/* 177 */       if ((this.isPropertyValuePattern) && (!this.isPropertyListPattern) && (paramObjectName.getKeyPropertyList().size() != this.keys.length))
/*     */       {
/* 180 */         return false;
/*     */       }
/*     */ 
/* 185 */       if ((this.isPropertyValuePattern) || (this.isPropertyListPattern)) {
/* 186 */         for (int i = this.keys.length - 1; i >= 0; i--)
/*     */         {
/* 190 */           str2 = paramObjectName.getKeyProperty(this.keys[i]);
/*     */ 
/* 193 */           if (str2 == null) return false;
/*     */ 
/* 196 */           if ((this.isPropertyValuePattern) && (this.pattern.isPropertyValuePattern(this.keys[i])))
/*     */           {
/* 201 */             if (!Util.wildmatch(str2, this.values[i]))
/*     */             {
/* 204 */               return false;
/*     */             }
/* 206 */           } else if (!str2.equals(this.values[i]))
/* 207 */             return false;
/*     */         }
/* 209 */         return true;
/*     */       }
/*     */ 
/* 214 */       String str1 = paramObjectName.getCanonicalKeyPropertyListString();
/* 215 */       String str2 = this.properties;
/* 216 */       return str1.equals(str2);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static abstract interface RegistrationContext
/*     */   {
/*     */     public abstract void registering();
/*     */ 
/*     */     public abstract void unregistered();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.Repository
 * JD-Core Version:    0.6.2
 */