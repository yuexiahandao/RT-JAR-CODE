/*      */ package java.beans.beancontext;
/*      */ 
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.TooManyListenersException;
/*      */ 
/*      */ public class BeanContextServicesSupport extends BeanContextSupport
/*      */   implements BeanContextServices
/*      */ {
/*      */   private static final long serialVersionUID = -8494482757288719206L;
/*      */   protected transient HashMap services;
/* 1225 */   protected transient int serializable = 0;
/*      */   protected transient BCSSProxyServiceProvider proxy;
/*      */   protected transient ArrayList bcsListeners;
/*      */ 
/*      */   public BeanContextServicesSupport(BeanContextServices paramBeanContextServices, Locale paramLocale, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*   78 */     super(paramBeanContextServices, paramLocale, paramBoolean1, paramBoolean2);
/*      */   }
/*      */ 
/*      */   public BeanContextServicesSupport(BeanContextServices paramBeanContextServices, Locale paramLocale, boolean paramBoolean)
/*      */   {
/*   90 */     this(paramBeanContextServices, paramLocale, paramBoolean, true);
/*      */   }
/*      */ 
/*      */   public BeanContextServicesSupport(BeanContextServices paramBeanContextServices, Locale paramLocale)
/*      */   {
/*  101 */     this(paramBeanContextServices, paramLocale, false, true);
/*      */   }
/*      */ 
/*      */   public BeanContextServicesSupport(BeanContextServices paramBeanContextServices)
/*      */   {
/*  111 */     this(paramBeanContextServices, null, false, true);
/*      */   }
/*      */ 
/*      */   public BeanContextServicesSupport()
/*      */   {
/*  119 */     this(null, null, false, true);
/*      */   }
/*      */ 
/*      */   public void initialize()
/*      */   {
/*  131 */     super.initialize();
/*      */ 
/*  133 */     this.services = new HashMap(this.serializable + 1);
/*  134 */     this.bcsListeners = new ArrayList(1);
/*      */   }
/*      */ 
/*      */   public BeanContextServices getBeanContextServicesPeer()
/*      */   {
/*  145 */     return (BeanContextServices)getBeanContextChildPeer();
/*      */   }
/*      */ 
/*      */   protected BeanContextSupport.BCSChild createBCSChild(Object paramObject1, Object paramObject2)
/*      */   {
/*  587 */     return new BCSSChild(paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   protected BCSSServiceProvider createBCSSServiceProvider(Class paramClass, BeanContextServiceProvider paramBeanContextServiceProvider)
/*      */   {
/*  624 */     return new BCSSServiceProvider(paramClass, paramBeanContextServiceProvider);
/*      */   }
/*      */ 
/*      */   public void addBeanContextServicesListener(BeanContextServicesListener paramBeanContextServicesListener)
/*      */   {
/*  636 */     if (paramBeanContextServicesListener == null) throw new NullPointerException("bcsl");
/*      */ 
/*  638 */     synchronized (this.bcsListeners) {
/*  639 */       if (this.bcsListeners.contains(paramBeanContextServicesListener)) {
/*  640 */         return;
/*      */       }
/*  642 */       this.bcsListeners.add(paramBeanContextServicesListener);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeBeanContextServicesListener(BeanContextServicesListener paramBeanContextServicesListener)
/*      */   {
/*  651 */     if (paramBeanContextServicesListener == null) throw new NullPointerException("bcsl");
/*      */ 
/*  653 */     synchronized (this.bcsListeners) {
/*  654 */       if (!this.bcsListeners.contains(paramBeanContextServicesListener)) {
/*  655 */         return;
/*      */       }
/*  657 */       this.bcsListeners.remove(paramBeanContextServicesListener);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean addService(Class paramClass, BeanContextServiceProvider paramBeanContextServiceProvider)
/*      */   {
/*  666 */     return addService(paramClass, paramBeanContextServiceProvider, true);
/*      */   }
/*      */ 
/*      */   protected boolean addService(Class paramClass, BeanContextServiceProvider paramBeanContextServiceProvider, boolean paramBoolean)
/*      */   {
/*  675 */     if (paramClass == null) throw new NullPointerException("serviceClass");
/*  676 */     if (paramBeanContextServiceProvider == null) throw new NullPointerException("bcsp");
/*      */ 
/*  678 */     synchronized (BeanContext.globalHierarchyLock) {
/*  679 */       if (this.services.containsKey(paramClass)) {
/*  680 */         return false;
/*      */       }
/*  682 */       this.services.put(paramClass, createBCSSServiceProvider(paramClass, paramBeanContextServiceProvider));
/*      */ 
/*  684 */       if ((paramBeanContextServiceProvider instanceof Serializable)) this.serializable += 1;
/*      */ 
/*  686 */       if (!paramBoolean) return true;
/*      */ 
/*  689 */       BeanContextServiceAvailableEvent localBeanContextServiceAvailableEvent = new BeanContextServiceAvailableEvent(getBeanContextServicesPeer(), paramClass);
/*      */ 
/*  691 */       fireServiceAdded(localBeanContextServiceAvailableEvent);
/*      */ 
/*  693 */       synchronized (this.children) {
/*  694 */         Iterator localIterator = this.children.keySet().iterator();
/*      */ 
/*  696 */         while (localIterator.hasNext()) {
/*  697 */           Object localObject1 = localIterator.next();
/*      */ 
/*  699 */           if ((localObject1 instanceof BeanContextServices)) {
/*  700 */             ((BeanContextServicesListener)localObject1).serviceAvailable(localBeanContextServiceAvailableEvent);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  705 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void revokeService(Class paramClass, BeanContextServiceProvider paramBeanContextServiceProvider, boolean paramBoolean)
/*      */   {
/*  716 */     if (paramClass == null) throw new NullPointerException("serviceClass");
/*  717 */     if (paramBeanContextServiceProvider == null) throw new NullPointerException("bcsp");
/*      */ 
/*  719 */     synchronized (BeanContext.globalHierarchyLock) {
/*  720 */       if (!this.services.containsKey(paramClass)) return;
/*      */ 
/*  722 */       BCSSServiceProvider localBCSSServiceProvider = (BCSSServiceProvider)this.services.get(paramClass);
/*      */ 
/*  724 */       if (!localBCSSServiceProvider.getServiceProvider().equals(paramBeanContextServiceProvider)) {
/*  725 */         throw new IllegalArgumentException("service provider mismatch");
/*      */       }
/*  727 */       this.services.remove(paramClass);
/*      */ 
/*  729 */       if ((paramBeanContextServiceProvider instanceof Serializable)) this.serializable -= 1;
/*      */ 
/*  731 */       Iterator localIterator = bcsChildren();
/*      */ 
/*  733 */       while (localIterator.hasNext()) {
/*  734 */         ((BCSSChild)localIterator.next()).revokeService(paramClass, false, paramBoolean);
/*      */       }
/*      */ 
/*  737 */       fireServiceRevoked(paramClass, paramBoolean);
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized boolean hasService(Class paramClass)
/*      */   {
/*  746 */     if (paramClass == null) throw new NullPointerException("serviceClass");
/*      */ 
/*  748 */     synchronized (BeanContext.globalHierarchyLock) {
/*  749 */       if (this.services.containsKey(paramClass)) return true;
/*      */ 
/*  751 */       BeanContextServices localBeanContextServices = null;
/*      */       try
/*      */       {
/*  754 */         localBeanContextServices = (BeanContextServices)getBeanContext();
/*      */       } catch (ClassCastException localClassCastException) {
/*  756 */         return false;
/*      */       }
/*      */ 
/*  759 */       return localBeanContextServices == null ? false : localBeanContextServices.hasService(paramClass);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Object getService(BeanContextChild paramBeanContextChild, Object paramObject1, Class paramClass, Object paramObject2, BeanContextServiceRevokedListener paramBeanContextServiceRevokedListener)
/*      */     throws TooManyListenersException
/*      */   {
/*  820 */     if (paramBeanContextChild == null) throw new NullPointerException("child");
/*  821 */     if (paramClass == null) throw new NullPointerException("serviceClass");
/*  822 */     if (paramObject1 == null) throw new NullPointerException("requestor");
/*  823 */     if (paramBeanContextServiceRevokedListener == null) throw new NullPointerException("bcsrl");
/*      */ 
/*  825 */     Object localObject1 = null;
/*      */ 
/*  827 */     BeanContextServices localBeanContextServices = getBeanContextServicesPeer();
/*      */ 
/*  829 */     synchronized (BeanContext.globalHierarchyLock)
/*      */     {
/*  830 */       BCSSChild localBCSSChild;
/*  830 */       synchronized (this.children) { localBCSSChild = (BCSSChild)this.children.get(paramBeanContextChild); }
/*      */ 
/*  832 */       if (localBCSSChild == null) throw new IllegalArgumentException("not a child of this context");
/*      */ 
/*  834 */       ??? = (BCSSServiceProvider)this.services.get(paramClass);
/*      */ 
/*  836 */       if (??? != null) {
/*  837 */         BeanContextServiceProvider localBeanContextServiceProvider = ((BCSSServiceProvider)???).getServiceProvider();
/*  838 */         localObject1 = localBeanContextServiceProvider.getService(localBeanContextServices, paramObject1, paramClass, paramObject2);
/*  839 */         if (localObject1 != null) {
/*      */           try {
/*  841 */             localBCSSChild.usingService(paramObject1, localObject1, paramClass, localBeanContextServiceProvider, false, paramBeanContextServiceRevokedListener);
/*      */           } catch (TooManyListenersException localTooManyListenersException2) {
/*  843 */             localBeanContextServiceProvider.releaseService(localBeanContextServices, paramObject1, localObject1);
/*  844 */             throw localTooManyListenersException2;
/*      */           } catch (UnsupportedOperationException localUnsupportedOperationException2) {
/*  846 */             localBeanContextServiceProvider.releaseService(localBeanContextServices, paramObject1, localObject1);
/*  847 */             throw localUnsupportedOperationException2;
/*      */           }
/*      */ 
/*  850 */           return localObject1;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  855 */       if (this.proxy != null)
/*      */       {
/*  859 */         localObject1 = this.proxy.getService(localBeanContextServices, paramObject1, paramClass, paramObject2);
/*      */ 
/*  861 */         if (localObject1 != null) {
/*      */           try {
/*  863 */             localBCSSChild.usingService(paramObject1, localObject1, paramClass, this.proxy, true, paramBeanContextServiceRevokedListener);
/*      */           } catch (TooManyListenersException localTooManyListenersException1) {
/*  865 */             this.proxy.releaseService(localBeanContextServices, paramObject1, localObject1);
/*  866 */             throw localTooManyListenersException1;
/*      */           } catch (UnsupportedOperationException localUnsupportedOperationException1) {
/*  868 */             this.proxy.releaseService(localBeanContextServices, paramObject1, localObject1);
/*  869 */             throw localUnsupportedOperationException1;
/*      */           }
/*      */ 
/*  872 */           return localObject1;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  877 */     return null;
/*      */   }
/*      */ 
/*      */   public void releaseService(BeanContextChild paramBeanContextChild, Object paramObject1, Object paramObject2)
/*      */   {
/*  885 */     if (paramBeanContextChild == null) throw new NullPointerException("child");
/*  886 */     if (paramObject1 == null) throw new NullPointerException("requestor");
/*  887 */     if (paramObject2 == null) throw new NullPointerException("service");
/*      */ 
/*  891 */     synchronized (BeanContext.globalHierarchyLock)
/*      */     {
/*  892 */       BCSSChild localBCSSChild;
/*  892 */       synchronized (this.children) { localBCSSChild = (BCSSChild)this.children.get(paramBeanContextChild); }
/*      */ 
/*  894 */       if (localBCSSChild != null)
/*  895 */         localBCSSChild.releaseService(paramObject1, paramObject2);
/*      */       else
/*  897 */         throw new IllegalArgumentException("child actual is not a child of this BeanContext");
/*      */     }
/*      */   }
/*      */ 
/*      */   public Iterator getCurrentServiceClasses()
/*      */   {
/*  906 */     return new BeanContextSupport.BCSIterator(this.services.keySet().iterator());
/*      */   }
/*      */ 
/*      */   public Iterator getCurrentServiceSelectors(Class paramClass)
/*      */   {
/*  916 */     BCSSServiceProvider localBCSSServiceProvider = (BCSSServiceProvider)this.services.get(paramClass);
/*      */ 
/*  918 */     return localBCSSServiceProvider != null ? new BeanContextSupport.BCSIterator(localBCSSServiceProvider.getServiceProvider().getCurrentServiceSelectors(getBeanContextServicesPeer(), paramClass)) : null;
/*      */   }
/*      */ 
/*      */   public void serviceAvailable(BeanContextServiceAvailableEvent paramBeanContextServiceAvailableEvent)
/*      */   {
/*  932 */     synchronized (BeanContext.globalHierarchyLock) {
/*  933 */       if (this.services.containsKey(paramBeanContextServiceAvailableEvent.getServiceClass())) return;
/*      */ 
/*  935 */       fireServiceAdded(paramBeanContextServiceAvailableEvent);
/*      */       Iterator localIterator;
/*  939 */       synchronized (this.children) {
/*  940 */         localIterator = this.children.keySet().iterator();
/*      */       }
/*      */ 
/*  943 */       while (localIterator.hasNext()) {
/*  944 */         ??? = localIterator.next();
/*      */ 
/*  946 */         if ((??? instanceof BeanContextServices))
/*  947 */           ((BeanContextServicesListener)???).serviceAvailable(paramBeanContextServiceAvailableEvent);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void serviceRevoked(BeanContextServiceRevokedEvent paramBeanContextServiceRevokedEvent)
/*      */   {
/*  964 */     synchronized (BeanContext.globalHierarchyLock) {
/*  965 */       if (this.services.containsKey(paramBeanContextServiceRevokedEvent.getServiceClass())) return;
/*      */ 
/*  967 */       fireServiceRevoked(paramBeanContextServiceRevokedEvent);
/*      */       Iterator localIterator;
/*  971 */       synchronized (this.children) {
/*  972 */         localIterator = this.children.keySet().iterator();
/*      */       }
/*      */ 
/*  975 */       while (localIterator.hasNext()) {
/*  976 */         ??? = localIterator.next();
/*      */ 
/*  978 */         if ((??? instanceof BeanContextServices))
/*  979 */           ((BeanContextServicesListener)???).serviceRevoked(paramBeanContextServiceRevokedEvent);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static final BeanContextServicesListener getChildBeanContextServicesListener(Object paramObject)
/*      */   {
/*      */     try
/*      */     {
/*  994 */       return (BeanContextServicesListener)paramObject; } catch (ClassCastException localClassCastException) {
/*      */     }
/*  996 */     return null;
/*      */   }
/*      */ 
/*      */   protected void childJustRemovedHook(Object paramObject, BeanContextSupport.BCSChild paramBCSChild)
/*      */   {
/* 1012 */     BCSSChild localBCSSChild = (BCSSChild)paramBCSChild;
/*      */ 
/* 1014 */     localBCSSChild.cleanupReferences();
/*      */   }
/*      */ 
/*      */   protected synchronized void releaseBeanContextResources()
/*      */   {
/* 1029 */     super.releaseBeanContextResources();
/*      */     Object[] arrayOfObject;
/* 1031 */     synchronized (this.children) {
/* 1032 */       if (this.children.isEmpty()) return;
/*      */ 
/* 1034 */       arrayOfObject = this.children.values().toArray();
/*      */     }
/*      */ 
/* 1038 */     for (int i = 0; i < arrayOfObject.length; i++) {
/* 1039 */       ((BCSSChild)arrayOfObject[i]).revokeAllDelegatedServicesNow();
/*      */     }
/*      */ 
/* 1042 */     this.proxy = null;
/*      */   }
/*      */ 
/*      */   protected synchronized void initializeBeanContextResources()
/*      */   {
/* 1053 */     super.initializeBeanContextResources();
/*      */ 
/* 1055 */     BeanContext localBeanContext = getBeanContext();
/*      */ 
/* 1057 */     if (localBeanContext == null) return;
/*      */     try
/*      */     {
/* 1060 */       BeanContextServices localBeanContextServices = (BeanContextServices)localBeanContext;
/*      */ 
/* 1062 */       this.proxy = new BCSSProxyServiceProvider(localBeanContextServices);
/*      */     }
/*      */     catch (ClassCastException localClassCastException)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void fireServiceAdded(Class paramClass)
/*      */   {
/* 1072 */     BeanContextServiceAvailableEvent localBeanContextServiceAvailableEvent = new BeanContextServiceAvailableEvent(getBeanContextServicesPeer(), paramClass);
/*      */ 
/* 1074 */     fireServiceAdded(localBeanContextServiceAvailableEvent);
/*      */   }
/*      */ 
/*      */   protected final void fireServiceAdded(BeanContextServiceAvailableEvent paramBeanContextServiceAvailableEvent)
/*      */   {
/* 1086 */     Object[] arrayOfObject;
/* 1086 */     synchronized (this.bcsListeners) { arrayOfObject = this.bcsListeners.toArray(); }
/*      */ 
/* 1088 */     for (int i = 0; i < arrayOfObject.length; i++)
/* 1089 */       ((BeanContextServicesListener)arrayOfObject[i]).serviceAvailable(paramBeanContextServiceAvailableEvent);
/*      */   }
/*      */ 
/*      */   protected final void fireServiceRevoked(BeanContextServiceRevokedEvent paramBeanContextServiceRevokedEvent)
/*      */   {
/* 1101 */     Object[] arrayOfObject;
/* 1101 */     synchronized (this.bcsListeners) { arrayOfObject = this.bcsListeners.toArray(); }
/*      */ 
/* 1103 */     for (int i = 0; i < arrayOfObject.length; i++)
/* 1104 */       ((BeanContextServiceRevokedListener)arrayOfObject[i]).serviceRevoked(paramBeanContextServiceRevokedEvent);
/*      */   }
/*      */ 
/*      */   protected final void fireServiceRevoked(Class paramClass, boolean paramBoolean)
/*      */   {
/* 1115 */     BeanContextServiceRevokedEvent localBeanContextServiceRevokedEvent = new BeanContextServiceRevokedEvent(getBeanContextServicesPeer(), paramClass, paramBoolean);
/*      */     Object[] arrayOfObject;
/* 1117 */     synchronized (this.bcsListeners) { arrayOfObject = this.bcsListeners.toArray(); }
/*      */ 
/* 1119 */     for (int i = 0; i < arrayOfObject.length; i++)
/* 1120 */       ((BeanContextServicesListener)arrayOfObject[i]).serviceRevoked(localBeanContextServiceRevokedEvent);
/*      */   }
/*      */ 
/*      */   protected synchronized void bcsPreSerializationHook(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1137 */     paramObjectOutputStream.writeInt(this.serializable);
/*      */ 
/* 1139 */     if (this.serializable <= 0) return;
/*      */ 
/* 1141 */     int i = 0;
/*      */ 
/* 1143 */     Iterator localIterator = this.services.entrySet().iterator();
/*      */ 
/* 1145 */     while ((localIterator.hasNext()) && (i < this.serializable)) {
/* 1146 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/* 1147 */       BCSSServiceProvider localBCSSServiceProvider = null;
/*      */       try
/*      */       {
/* 1150 */         localBCSSServiceProvider = (BCSSServiceProvider)localEntry.getValue(); } catch (ClassCastException localClassCastException) {
/*      */       }
/* 1152 */       continue;
/*      */ 
/* 1155 */       if ((localBCSSServiceProvider.getServiceProvider() instanceof Serializable)) {
/* 1156 */         paramObjectOutputStream.writeObject(localEntry.getKey());
/* 1157 */         paramObjectOutputStream.writeObject(localBCSSServiceProvider);
/* 1158 */         i++;
/*      */       }
/*      */     }
/*      */ 
/* 1162 */     if (i != this.serializable)
/* 1163 */       throw new IOException("wrote different number of service providers than expected");
/*      */   }
/*      */ 
/*      */   protected synchronized void bcsPreDeserializationHook(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1180 */     this.serializable = paramObjectInputStream.readInt();
/*      */ 
/* 1182 */     int i = this.serializable;
/*      */ 
/* 1184 */     while (i > 0) {
/* 1185 */       this.services.put(paramObjectInputStream.readObject(), paramObjectInputStream.readObject());
/* 1186 */       i--;
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1195 */     paramObjectOutputStream.defaultWriteObject();
/*      */ 
/* 1197 */     serialize(paramObjectOutputStream, this.bcsListeners);
/*      */   }
/*      */ 
/*      */   private synchronized void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1206 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 1208 */     deserialize(paramObjectInputStream, this.bcsListeners);
/*      */   }
/*      */ 
/*      */   protected class BCSSChild extends BeanContextSupport.BCSChild
/*      */   {
/*      */     private static final long serialVersionUID = -3263851306889194873L;
/*      */     private transient HashMap serviceClasses;
/*      */     private transient HashMap serviceRequestors;
/*      */ 
/*      */     BCSSChild(Object paramObject1, Object arg3)
/*      */     {
/*  321 */       super(paramObject1, localObject);
/*      */     }
/*      */ 
/*      */     synchronized void usingService(Object paramObject1, Object paramObject2, Class paramClass, BeanContextServiceProvider paramBeanContextServiceProvider, boolean paramBoolean, BeanContextServiceRevokedListener paramBeanContextServiceRevokedListener)
/*      */       throws TooManyListenersException, UnsupportedOperationException
/*      */     {
/*  329 */       BCSSCServiceClassRef localBCSSCServiceClassRef = null;
/*      */ 
/*  331 */       if (this.serviceClasses == null)
/*  332 */         this.serviceClasses = new HashMap(1);
/*      */       else {
/*  334 */         localBCSSCServiceClassRef = (BCSSCServiceClassRef)this.serviceClasses.get(paramClass);
/*      */       }
/*  336 */       if (localBCSSCServiceClassRef == null) {
/*  337 */         localBCSSCServiceClassRef = new BCSSCServiceClassRef(paramClass, paramBeanContextServiceProvider, paramBoolean);
/*  338 */         this.serviceClasses.put(paramClass, localBCSSCServiceClassRef);
/*      */       }
/*      */       else {
/*  341 */         localBCSSCServiceClassRef.verifyAndMaybeSetProvider(paramBeanContextServiceProvider, paramBoolean);
/*  342 */         localBCSSCServiceClassRef.verifyRequestor(paramObject1, paramBeanContextServiceRevokedListener);
/*      */       }
/*      */ 
/*  345 */       localBCSSCServiceClassRef.addRequestor(paramObject1, paramBeanContextServiceRevokedListener);
/*  346 */       localBCSSCServiceClassRef.addRef(paramBoolean);
/*      */ 
/*  350 */       BCSSCServiceRef localBCSSCServiceRef = null;
/*  351 */       Object localObject = null;
/*      */ 
/*  353 */       if (this.serviceRequestors == null)
/*  354 */         this.serviceRequestors = new HashMap(1);
/*      */       else {
/*  356 */         localObject = (Map)this.serviceRequestors.get(paramObject1);
/*      */       }
/*      */ 
/*  359 */       if (localObject == null) {
/*  360 */         localObject = new HashMap(1);
/*      */ 
/*  362 */         this.serviceRequestors.put(paramObject1, localObject);
/*      */       } else {
/*  364 */         localBCSSCServiceRef = (BCSSCServiceRef)((Map)localObject).get(paramObject2);
/*      */       }
/*  366 */       if (localBCSSCServiceRef == null) {
/*  367 */         localBCSSCServiceRef = new BCSSCServiceRef(localBCSSCServiceClassRef, paramBoolean);
/*      */ 
/*  369 */         ((Map)localObject).put(paramObject2, localBCSSCServiceRef);
/*      */       } else {
/*  371 */         localBCSSCServiceRef.addRef();
/*      */       }
/*      */     }
/*      */ 
/*      */     synchronized void releaseService(Object paramObject1, Object paramObject2)
/*      */     {
/*  378 */       if (this.serviceRequestors == null) return;
/*      */ 
/*  380 */       Map localMap = (Map)this.serviceRequestors.get(paramObject1);
/*      */ 
/*  382 */       if (localMap == null) return;
/*      */ 
/*  384 */       BCSSCServiceRef localBCSSCServiceRef = (BCSSCServiceRef)localMap.get(paramObject2);
/*      */ 
/*  386 */       if (localBCSSCServiceRef == null) return;
/*      */ 
/*  388 */       BCSSCServiceClassRef localBCSSCServiceClassRef = localBCSSCServiceRef.getServiceClassRef();
/*  389 */       boolean bool = localBCSSCServiceRef.isDelegated();
/*  390 */       BeanContextServiceProvider localBeanContextServiceProvider = bool ? localBCSSCServiceClassRef.getDelegateProvider() : localBCSSCServiceClassRef.getServiceProvider();
/*      */ 
/*  392 */       localBeanContextServiceProvider.releaseService(BeanContextServicesSupport.this.getBeanContextServicesPeer(), paramObject1, paramObject2);
/*      */ 
/*  394 */       localBCSSCServiceClassRef.releaseRef(bool);
/*  395 */       localBCSSCServiceClassRef.removeRequestor(paramObject1);
/*      */ 
/*  397 */       if (localBCSSCServiceRef.release() == 0)
/*      */       {
/*  399 */         localMap.remove(paramObject2);
/*      */ 
/*  401 */         if (localMap.isEmpty()) {
/*  402 */           this.serviceRequestors.remove(paramObject1);
/*  403 */           localBCSSCServiceClassRef.removeRequestor(paramObject1);
/*      */         }
/*      */ 
/*  406 */         if (this.serviceRequestors.isEmpty()) {
/*  407 */           this.serviceRequestors = null;
/*      */         }
/*      */ 
/*  410 */         if (localBCSSCServiceClassRef.isEmpty()) {
/*  411 */           this.serviceClasses.remove(localBCSSCServiceClassRef.getServiceClass());
/*      */         }
/*      */ 
/*  414 */         if (this.serviceClasses.isEmpty())
/*  415 */           this.serviceClasses = null;
/*      */       }
/*      */     }
/*      */ 
/*      */     synchronized void revokeService(Class paramClass, boolean paramBoolean1, boolean paramBoolean2)
/*      */     {
/*  422 */       if (this.serviceClasses == null) return;
/*      */ 
/*  424 */       BCSSCServiceClassRef localBCSSCServiceClassRef = (BCSSCServiceClassRef)this.serviceClasses.get(paramClass);
/*      */ 
/*  426 */       if (localBCSSCServiceClassRef == null) return;
/*      */ 
/*  428 */       Iterator localIterator1 = localBCSSCServiceClassRef.cloneOfEntries();
/*      */ 
/*  430 */       BeanContextServiceRevokedEvent localBeanContextServiceRevokedEvent = new BeanContextServiceRevokedEvent(BeanContextServicesSupport.this.getBeanContextServicesPeer(), paramClass, paramBoolean2);
/*  431 */       boolean bool = false;
/*      */ 
/*  433 */       while ((localIterator1.hasNext()) && (this.serviceRequestors != null)) {
/*  434 */         Map.Entry localEntry1 = (Map.Entry)localIterator1.next();
/*  435 */         BeanContextServiceRevokedListener localBeanContextServiceRevokedListener = (BeanContextServiceRevokedListener)localEntry1.getValue();
/*      */ 
/*  437 */         if (paramBoolean2) {
/*  438 */           Object localObject = localEntry1.getKey();
/*  439 */           Map localMap = (Map)this.serviceRequestors.get(localObject);
/*      */ 
/*  441 */           if (localMap != null) {
/*  442 */             Iterator localIterator2 = localMap.entrySet().iterator();
/*      */ 
/*  444 */             while (localIterator2.hasNext()) {
/*  445 */               Map.Entry localEntry2 = (Map.Entry)localIterator2.next();
/*      */ 
/*  447 */               BCSSCServiceRef localBCSSCServiceRef = (BCSSCServiceRef)localEntry2.getValue();
/*  448 */               if ((localBCSSCServiceRef.getServiceClassRef().equals(localBCSSCServiceClassRef)) && (paramBoolean1 == localBCSSCServiceRef.isDelegated())) {
/*  449 */                 localIterator2.remove();
/*      */               }
/*      */             }
/*      */ 
/*  453 */             if ((bool = localMap.isEmpty())) {
/*  454 */               this.serviceRequestors.remove(localObject);
/*      */             }
/*      */           }
/*      */ 
/*  458 */           if (bool) localBCSSCServiceClassRef.removeRequestor(localObject);
/*      */         }
/*      */ 
/*  461 */         localBeanContextServiceRevokedListener.serviceRevoked(localBeanContextServiceRevokedEvent);
/*      */       }
/*      */ 
/*  464 */       if ((paramBoolean2) && (this.serviceClasses != null)) {
/*  465 */         if (localBCSSCServiceClassRef.isEmpty()) {
/*  466 */           this.serviceClasses.remove(paramClass);
/*      */         }
/*  468 */         if (this.serviceClasses.isEmpty()) {
/*  469 */           this.serviceClasses = null;
/*      */         }
/*      */       }
/*  472 */       if ((this.serviceRequestors != null) && (this.serviceRequestors.isEmpty()))
/*  473 */         this.serviceRequestors = null;
/*      */     }
/*      */ 
/*      */     void cleanupReferences()
/*      */     {
/*  480 */       if (this.serviceRequestors == null) return;
/*      */ 
/*  482 */       Iterator localIterator1 = this.serviceRequestors.entrySet().iterator();
/*      */ 
/*  484 */       while (localIterator1.hasNext()) {
/*  485 */         Map.Entry localEntry1 = (Map.Entry)localIterator1.next();
/*  486 */         Object localObject1 = localEntry1.getKey();
/*  487 */         Iterator localIterator2 = ((Map)localEntry1.getValue()).entrySet().iterator();
/*      */ 
/*  489 */         localIterator1.remove();
/*      */ 
/*  491 */         while (localIterator2.hasNext()) {
/*  492 */           Map.Entry localEntry2 = (Map.Entry)localIterator2.next();
/*  493 */           Object localObject2 = localEntry2.getKey();
/*  494 */           BCSSCServiceRef localBCSSCServiceRef = (BCSSCServiceRef)localEntry2.getValue();
/*      */ 
/*  496 */           BCSSCServiceClassRef localBCSSCServiceClassRef = localBCSSCServiceRef.getServiceClassRef();
/*      */ 
/*  498 */           BeanContextServiceProvider localBeanContextServiceProvider = localBCSSCServiceRef.isDelegated() ? localBCSSCServiceClassRef.getDelegateProvider() : localBCSSCServiceClassRef.getServiceProvider();
/*      */ 
/*  500 */           localBCSSCServiceClassRef.removeRequestor(localObject1);
/*  501 */           localIterator2.remove();
/*      */ 
/*  503 */           while (localBCSSCServiceRef.release() >= 0) {
/*  504 */             localBeanContextServiceProvider.releaseService(BeanContextServicesSupport.this.getBeanContextServicesPeer(), localObject1, localObject2);
/*      */           }
/*      */         }
/*      */       }
/*      */ 
/*  509 */       this.serviceRequestors = null;
/*  510 */       this.serviceClasses = null;
/*      */     }
/*      */ 
/*      */     void revokeAllDelegatedServicesNow() {
/*  514 */       if (this.serviceClasses == null) return;
/*      */ 
/*  516 */       Iterator localIterator1 = new HashSet(this.serviceClasses.values()).iterator();
/*      */ 
/*  519 */       while (localIterator1.hasNext()) {
/*  520 */         BCSSCServiceClassRef localBCSSCServiceClassRef = (BCSSCServiceClassRef)localIterator1.next();
/*      */ 
/*  522 */         if (localBCSSCServiceClassRef.isDelegated())
/*      */         {
/*  524 */           Iterator localIterator2 = localBCSSCServiceClassRef.cloneOfEntries();
/*  525 */           BeanContextServiceRevokedEvent localBeanContextServiceRevokedEvent = new BeanContextServiceRevokedEvent(BeanContextServicesSupport.this.getBeanContextServicesPeer(), localBCSSCServiceClassRef.getServiceClass(), true);
/*  526 */           boolean bool = false;
/*      */ 
/*  528 */           while (localIterator2.hasNext()) {
/*  529 */             Map.Entry localEntry1 = (Map.Entry)localIterator2.next();
/*  530 */             BeanContextServiceRevokedListener localBeanContextServiceRevokedListener = (BeanContextServiceRevokedListener)localEntry1.getValue();
/*      */ 
/*  532 */             Object localObject = localEntry1.getKey();
/*  533 */             Map localMap = (Map)this.serviceRequestors.get(localObject);
/*      */ 
/*  535 */             if (localMap != null) {
/*  536 */               Iterator localIterator3 = localMap.entrySet().iterator();
/*      */ 
/*  538 */               while (localIterator3.hasNext()) {
/*  539 */                 Map.Entry localEntry2 = (Map.Entry)localIterator3.next();
/*      */ 
/*  541 */                 BCSSCServiceRef localBCSSCServiceRef = (BCSSCServiceRef)localEntry2.getValue();
/*  542 */                 if ((localBCSSCServiceRef.getServiceClassRef().equals(localBCSSCServiceClassRef)) && (localBCSSCServiceRef.isDelegated())) {
/*  543 */                   localIterator3.remove();
/*      */                 }
/*      */               }
/*      */ 
/*  547 */               if ((bool = localMap.isEmpty())) {
/*  548 */                 this.serviceRequestors.remove(localObject);
/*      */               }
/*      */             }
/*      */ 
/*  552 */             if (bool) localBCSSCServiceClassRef.removeRequestor(localObject);
/*      */ 
/*  554 */             localBeanContextServiceRevokedListener.serviceRevoked(localBeanContextServiceRevokedEvent);
/*      */ 
/*  556 */             if (localBCSSCServiceClassRef.isEmpty())
/*  557 */               this.serviceClasses.remove(localBCSSCServiceClassRef.getServiceClass());
/*      */           }
/*      */         }
/*      */       }
/*  561 */       if (this.serviceClasses.isEmpty()) this.serviceClasses = null;
/*      */ 
/*  563 */       if ((this.serviceRequestors != null) && (this.serviceRequestors.isEmpty()))
/*  564 */         this.serviceRequestors = null;
/*      */     }
/*      */ 
/*      */     class BCSSCServiceClassRef
/*      */     {
/*      */       Class serviceClass;
/*      */       BeanContextServiceProvider serviceProvider;
/*      */       int serviceRefs;
/*      */       BeanContextServiceProvider delegateProvider;
/*      */       int delegateRefs;
/*  292 */       HashMap requestors = new HashMap(1);
/*      */ 
/*      */       BCSSCServiceClassRef(Class paramBeanContextServiceProvider, BeanContextServiceProvider paramBoolean, boolean arg4)
/*      */       {
/*  175 */         this.serviceClass = paramBeanContextServiceProvider;
/*      */         int i;
/*  177 */         if (i != 0)
/*  178 */           this.delegateProvider = paramBoolean;
/*      */         else
/*  180 */           this.serviceProvider = paramBoolean;
/*      */       }
/*      */ 
/*      */       void addRequestor(Object paramObject, BeanContextServiceRevokedListener paramBeanContextServiceRevokedListener)
/*      */         throws TooManyListenersException
/*      */       {
/*  186 */         BeanContextServiceRevokedListener localBeanContextServiceRevokedListener = (BeanContextServiceRevokedListener)this.requestors.get(paramObject);
/*      */ 
/*  188 */         if ((localBeanContextServiceRevokedListener != null) && (!localBeanContextServiceRevokedListener.equals(paramBeanContextServiceRevokedListener))) {
/*  189 */           throw new TooManyListenersException();
/*      */         }
/*  191 */         this.requestors.put(paramObject, paramBeanContextServiceRevokedListener);
/*      */       }
/*      */ 
/*      */       void removeRequestor(Object paramObject)
/*      */       {
/*  197 */         this.requestors.remove(paramObject);
/*      */       }
/*      */ 
/*      */       void verifyRequestor(Object paramObject, BeanContextServiceRevokedListener paramBeanContextServiceRevokedListener)
/*      */         throws TooManyListenersException
/*      */       {
/*  203 */         BeanContextServiceRevokedListener localBeanContextServiceRevokedListener = (BeanContextServiceRevokedListener)this.requestors.get(paramObject);
/*      */ 
/*  205 */         if ((localBeanContextServiceRevokedListener != null) && (!localBeanContextServiceRevokedListener.equals(paramBeanContextServiceRevokedListener)))
/*  206 */           throw new TooManyListenersException();
/*      */       }
/*      */ 
/*      */       void verifyAndMaybeSetProvider(BeanContextServiceProvider paramBeanContextServiceProvider, boolean paramBoolean)
/*      */       {
/*      */         BeanContextServiceProvider localBeanContextServiceProvider;
/*  212 */         if (paramBoolean) {
/*  213 */           localBeanContextServiceProvider = this.delegateProvider;
/*      */ 
/*  215 */           if ((localBeanContextServiceProvider == null) || (paramBeanContextServiceProvider == null))
/*  216 */             this.delegateProvider = paramBeanContextServiceProvider;
/*      */         }
/*      */         else
/*      */         {
/*  220 */           localBeanContextServiceProvider = this.serviceProvider;
/*      */ 
/*  222 */           if ((localBeanContextServiceProvider == null) || (paramBeanContextServiceProvider == null)) {
/*  223 */             this.serviceProvider = paramBeanContextServiceProvider;
/*  224 */             return;
/*      */           }
/*      */         }
/*      */ 
/*  228 */         if (!localBeanContextServiceProvider.equals(paramBeanContextServiceProvider))
/*  229 */           throw new UnsupportedOperationException("existing service reference obtained from different BeanContextServiceProvider not supported");
/*      */       }
/*      */ 
/*      */       Iterator cloneOfEntries()
/*      */       {
/*  234 */         return ((HashMap)this.requestors.clone()).entrySet().iterator();
/*      */       }
/*      */       Iterator entries() {
/*  237 */         return this.requestors.entrySet().iterator();
/*      */       }
/*  239 */       boolean isEmpty() { return this.requestors.isEmpty(); } 
/*      */       Class getServiceClass() {
/*  241 */         return this.serviceClass;
/*      */       }
/*      */       BeanContextServiceProvider getServiceProvider() {
/*  244 */         return this.serviceProvider;
/*      */       }
/*      */ 
/*      */       BeanContextServiceProvider getDelegateProvider() {
/*  248 */         return this.delegateProvider;
/*      */       }
/*      */       boolean isDelegated() {
/*  251 */         return this.delegateProvider != null;
/*      */       }
/*      */       void addRef(boolean paramBoolean) {
/*  254 */         if (paramBoolean)
/*  255 */           this.delegateRefs += 1;
/*      */         else
/*  257 */           this.serviceRefs += 1;
/*      */       }
/*      */ 
/*      */       void releaseRef(boolean paramBoolean)
/*      */       {
/*  263 */         if (paramBoolean) {
/*  264 */           if (--this.delegateRefs == 0) {
/*  265 */             this.delegateProvider = null;
/*      */           }
/*      */         }
/*  268 */         else if (--this.serviceRefs <= 0)
/*  269 */           this.serviceProvider = null;
/*      */       }
/*      */ 
/*      */       int getRefs()
/*      */       {
/*  274 */         return this.serviceRefs + this.delegateRefs;
/*      */       }
/*  276 */       int getDelegateRefs() { return this.delegateRefs; } 
/*      */       int getServiceRefs() {
/*  278 */         return this.serviceRefs;
/*      */       }
/*      */     }
/*      */ 
/*      */     class BCSSCServiceRef
/*      */     {
/*      */       BeanContextServicesSupport.BCSSChild.BCSSCServiceClassRef serviceClassRef;
/*  317 */       int refCnt = 1;
/*  318 */       boolean delegated = false;
/*      */ 
/*      */       BCSSCServiceRef(BeanContextServicesSupport.BCSSChild.BCSSCServiceClassRef paramBoolean, boolean arg3)
/*      */       {
/*  301 */         this.serviceClassRef = paramBoolean;
/*      */         boolean bool;
/*  302 */         this.delegated = bool;
/*      */       }
/*      */       void addRef() {
/*  305 */         this.refCnt += 1; } 
/*  306 */       int release() { return --this.refCnt; } 
/*      */       BeanContextServicesSupport.BCSSChild.BCSSCServiceClassRef getServiceClassRef() {
/*  308 */         return this.serviceClassRef;
/*      */       }
/*  310 */       boolean isDelegated() { return this.delegated; }
/*      */ 
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class BCSSProxyServiceProvider
/*      */     implements BeanContextServiceProvider, BeanContextServiceRevokedListener
/*      */   {
/*      */     private BeanContextServices nestingCtxt;
/*      */ 
/*      */     BCSSProxyServiceProvider(BeanContextServices arg2)
/*      */     {
/*      */       Object localObject;
/*  775 */       this.nestingCtxt = localObject;
/*      */     }
/*      */ 
/*      */     public Object getService(BeanContextServices paramBeanContextServices, Object paramObject1, Class paramClass, Object paramObject2) {
/*  779 */       Object localObject = null;
/*      */       try
/*      */       {
/*  782 */         localObject = this.nestingCtxt.getService(paramBeanContextServices, paramObject1, paramClass, paramObject2, this);
/*      */       } catch (TooManyListenersException localTooManyListenersException) {
/*  784 */         return null;
/*      */       }
/*      */ 
/*  787 */       return localObject;
/*      */     }
/*      */ 
/*      */     public void releaseService(BeanContextServices paramBeanContextServices, Object paramObject1, Object paramObject2) {
/*  791 */       this.nestingCtxt.releaseService(paramBeanContextServices, paramObject1, paramObject2);
/*      */     }
/*      */ 
/*      */     public Iterator getCurrentServiceSelectors(BeanContextServices paramBeanContextServices, Class paramClass) {
/*  795 */       return this.nestingCtxt.getCurrentServiceSelectors(paramClass);
/*      */     }
/*      */ 
/*      */     public void serviceRevoked(BeanContextServiceRevokedEvent paramBeanContextServiceRevokedEvent) {
/*  799 */       Iterator localIterator = BeanContextServicesSupport.this.bcsChildren();
/*      */ 
/*  801 */       while (localIterator.hasNext())
/*  802 */         ((BeanContextServicesSupport.BCSSChild)localIterator.next()).revokeService(paramBeanContextServiceRevokedEvent.getServiceClass(), true, paramBeanContextServiceRevokedEvent.isCurrentServiceInvalidNow());
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static class BCSSServiceProvider
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = 861278251667444782L;
/*      */     protected BeanContextServiceProvider serviceProvider;
/*      */ 
/*      */     BCSSServiceProvider(Class paramClass, BeanContextServiceProvider paramBeanContextServiceProvider)
/*      */     {
/*  603 */       this.serviceProvider = paramBeanContextServiceProvider;
/*      */     }
/*      */ 
/*      */     protected BeanContextServiceProvider getServiceProvider() {
/*  607 */       return this.serviceProvider;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.beancontext.BeanContextServicesSupport
 * JD-Core Version:    0.6.2
 */