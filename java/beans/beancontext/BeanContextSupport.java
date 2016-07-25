/*      */ package java.beans.beancontext;
/*      */ 
/*      */ import java.awt.Component;
/*      */ import java.awt.Container;
/*      */ import java.beans.Beans;
/*      */ import java.beans.PropertyChangeEvent;
/*      */ import java.beans.PropertyChangeListener;
/*      */ import java.beans.PropertyVetoException;
/*      */ import java.beans.VetoableChangeListener;
/*      */ import java.beans.Visibility;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.net.URL;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Locale;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ 
/*      */ public class BeanContextSupport extends BeanContextChildSupport
/*      */   implements BeanContext, Serializable, PropertyChangeListener, VetoableChangeListener
/*      */ {
/*      */   static final long serialVersionUID = -4879613978649577204L;
/*      */   protected transient HashMap children;
/* 1356 */   private int serializable = 0;
/*      */   protected transient ArrayList bcmListeners;
/*      */   protected Locale locale;
/*      */   protected boolean okToUseGui;
/*      */   protected boolean designTime;
/*      */   private transient PropertyChangeListener childPCL;
/*      */   private transient VetoableChangeListener childVCL;
/*      */   private transient boolean serializing;
/*      */ 
/*      */   public BeanContextSupport(BeanContext paramBeanContext, Locale paramLocale, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/*  104 */     super(paramBeanContext);
/*      */ 
/*  106 */     this.locale = (paramLocale != null ? paramLocale : Locale.getDefault());
/*  107 */     this.designTime = paramBoolean1;
/*  108 */     this.okToUseGui = paramBoolean2;
/*      */ 
/*  110 */     initialize();
/*      */   }
/*      */ 
/*      */   public BeanContextSupport(BeanContext paramBeanContext, Locale paramLocale, boolean paramBoolean)
/*      */   {
/*  129 */     this(paramBeanContext, paramLocale, paramBoolean, true);
/*      */   }
/*      */ 
/*      */   public BeanContextSupport(BeanContext paramBeanContext, Locale paramLocale)
/*      */   {
/*  149 */     this(paramBeanContext, paramLocale, false, true);
/*      */   }
/*      */ 
/*      */   public BeanContextSupport(BeanContext paramBeanContext)
/*      */   {
/*  161 */     this(paramBeanContext, null, false, true);
/*      */   }
/*      */ 
/*      */   public BeanContextSupport()
/*      */   {
/*  169 */     this(null, null, false, true);
/*      */   }
/*      */ 
/*      */   public BeanContext getBeanContextPeer()
/*      */   {
/*  177 */     return (VetoableChangeListener)getBeanContextChildPeer();
/*      */   }
/*      */ 
/*      */   public Object instantiateChild(String paramString)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  198 */     BeanContext localBeanContext = getBeanContextPeer();
/*      */ 
/*  200 */     return Beans.instantiate(localBeanContext.getClass().getClassLoader(), paramString, localBeanContext);
/*      */   }
/*      */ 
/*      */   public int size()
/*      */   {
/*  210 */     synchronized (this.children) {
/*  211 */       return this.children.size();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean isEmpty()
/*      */   {
/*  224 */     synchronized (this.children) {
/*  225 */       return this.children.isEmpty();
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean contains(Object paramObject)
/*      */   {
/*  236 */     synchronized (this.children) {
/*  237 */       return this.children.containsKey(paramObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean containsKey(Object paramObject)
/*      */   {
/*  248 */     synchronized (this.children) {
/*  249 */       return this.children.containsKey(paramObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public Iterator iterator()
/*      */   {
/*  259 */     synchronized (this.children) {
/*  260 */       return new BCSIterator(this.children.keySet().iterator());
/*      */     }
/*      */   }
/*      */ 
/*      */   public Object[] toArray()
/*      */   {
/*  269 */     synchronized (this.children) {
/*  270 */       return this.children.keySet().toArray();
/*      */     }
/*      */   }
/*      */ 
/*      */   public Object[] toArray(Object[] paramArrayOfObject)
/*      */   {
/*  283 */     synchronized (this.children) {
/*  284 */       return this.children.keySet().toArray(paramArrayOfObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected BCSChild createBCSChild(Object paramObject1, Object paramObject2)
/*      */   {
/*  360 */     return new BCSChild(paramObject1, paramObject2);
/*      */   }
/*      */ 
/*      */   public boolean add(Object paramObject)
/*      */   {
/*  381 */     if (paramObject == null) throw new IllegalArgumentException();
/*      */ 
/*  386 */     if (this.children.containsKey(paramObject)) return false;
/*      */ 
/*  388 */     synchronized (VetoableChangeListener.globalHierarchyLock) {
/*  389 */       if (this.children.containsKey(paramObject)) return false;
/*      */ 
/*  391 */       if (!validatePendingAdd(paramObject)) {
/*  392 */         throw new IllegalStateException();
/*      */       }
/*      */ 
/*  399 */       BeanContextChild localBeanContextChild1 = getChildBeanContextChild(paramObject);
/*  400 */       BeanContextChild localBeanContextChild2 = null;
/*      */ 
/*  402 */       synchronized (paramObject)
/*      */       {
/*  404 */         if ((paramObject instanceof BeanContextProxy)) {
/*  405 */           localBeanContextChild2 = ((BeanContextProxy)paramObject).getBeanContextProxy();
/*      */ 
/*  407 */           if (localBeanContextChild2 == null) throw new NullPointerException("BeanContextPeer.getBeanContextProxy()");
/*      */         }
/*      */ 
/*  410 */         BCSChild localBCSChild1 = createBCSChild(paramObject, localBeanContextChild2);
/*  411 */         BCSChild localBCSChild2 = null;
/*      */ 
/*  413 */         synchronized (this.children) {
/*  414 */           this.children.put(paramObject, localBCSChild1);
/*      */ 
/*  416 */           if (localBeanContextChild2 != null) this.children.put(localBeanContextChild2, localBCSChild2 = createBCSChild(localBeanContextChild2, paramObject));
/*      */         }
/*      */ 
/*  419 */         if (localBeanContextChild1 != null) synchronized (localBeanContextChild1) {
/*      */             try {
/*  421 */               localBeanContextChild1.setBeanContext(getBeanContextPeer());
/*      */             }
/*      */             catch (PropertyVetoException localPropertyVetoException) {
/*  424 */               synchronized (this.children) {
/*  425 */                 this.children.remove(paramObject);
/*      */ 
/*  427 */                 if (localBeanContextChild2 != null) this.children.remove(localBeanContextChild2);
/*      */               }
/*      */ 
/*  430 */               throw new IllegalStateException();
/*      */             }
/*      */ 
/*  433 */             localBeanContextChild1.addPropertyChangeListener("beanContext", this.childPCL);
/*  434 */             localBeanContextChild1.addVetoableChangeListener("beanContext", this.childVCL);
/*      */           }
/*      */ 
/*  437 */         ??? = getChildVisibility(paramObject);
/*      */ 
/*  439 */         if (??? != null) {
/*  440 */           if (this.okToUseGui)
/*  441 */             ((Visibility)???).okToUseGui();
/*      */           else {
/*  443 */             ((Visibility)???).dontUseGui();
/*      */           }
/*      */         }
/*  446 */         if (getChildSerializable(paramObject) != null) this.serializable += 1;
/*      */ 
/*  448 */         childJustAddedHook(paramObject, localBCSChild1);
/*      */ 
/*  450 */         if (localBeanContextChild2 != null) {
/*  451 */           ??? = getChildVisibility(localBeanContextChild2);
/*      */ 
/*  453 */           if (??? != null) {
/*  454 */             if (this.okToUseGui)
/*  455 */               ((Visibility)???).okToUseGui();
/*      */             else {
/*  457 */               ((Visibility)???).dontUseGui();
/*      */             }
/*      */           }
/*  460 */           if (getChildSerializable(localBeanContextChild2) != null) this.serializable += 1;
/*      */ 
/*  462 */           childJustAddedHook(localBeanContextChild2, localBCSChild2);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  470 */       fireChildrenAdded(new BeanContextMembershipEvent(getBeanContextPeer(), new Object[] { paramObject, localBeanContextChild2 == null ? new Object[] { paramObject } : localBeanContextChild2 }));
/*      */     }
/*      */ 
/*  474 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean remove(Object paramObject)
/*      */   {
/*  484 */     return remove(paramObject, true);
/*      */   }
/*      */ 
/*      */   protected boolean remove(Object paramObject, boolean paramBoolean)
/*      */   {
/*  498 */     if (paramObject == null) throw new IllegalArgumentException();
/*      */ 
/*  500 */     synchronized (VetoableChangeListener.globalHierarchyLock) {
/*  501 */       if (!containsKey(paramObject)) return false;
/*      */ 
/*  503 */       if (!validatePendingRemove(paramObject)) {
/*  504 */         throw new IllegalStateException();
/*      */       }
/*      */ 
/*  507 */       BCSChild localBCSChild1 = (BCSChild)this.children.get(paramObject);
/*  508 */       BCSChild localBCSChild2 = null;
/*  509 */       Object localObject1 = null;
/*      */ 
/*  514 */       synchronized (paramObject) {
/*  515 */         if (paramBoolean) {
/*  516 */           BeanContextChild localBeanContextChild = getChildBeanContextChild(paramObject);
/*  517 */           if (localBeanContextChild != null) synchronized (localBeanContextChild) {
/*  518 */               localBeanContextChild.removePropertyChangeListener("beanContext", this.childPCL);
/*  519 */               localBeanContextChild.removeVetoableChangeListener("beanContext", this.childVCL);
/*      */               try
/*      */               {
/*  522 */                 localBeanContextChild.setBeanContext(null);
/*      */               } catch (PropertyVetoException localPropertyVetoException) {
/*  524 */                 localBeanContextChild.addPropertyChangeListener("beanContext", this.childPCL);
/*  525 */                 localBeanContextChild.addVetoableChangeListener("beanContext", this.childVCL);
/*  526 */                 throw new IllegalStateException();
/*      */               }
/*      */             }
/*      */ 
/*      */         }
/*      */ 
/*  532 */         synchronized (this.children) {
/*  533 */           this.children.remove(paramObject);
/*      */ 
/*  535 */           if (localBCSChild1.isProxyPeer()) {
/*  536 */             localBCSChild2 = (BCSChild)this.children.get(localObject1 = localBCSChild1.getProxyPeer());
/*  537 */             this.children.remove(localObject1);
/*      */           }
/*      */         }
/*      */ 
/*  541 */         if (getChildSerializable(paramObject) != null) this.serializable -= 1;
/*      */ 
/*  543 */         childJustRemovedHook(paramObject, localBCSChild1);
/*      */ 
/*  545 */         if (localObject1 != null) {
/*  546 */           if (getChildSerializable(localObject1) != null) this.serializable -= 1;
/*      */ 
/*  548 */           childJustRemovedHook(localObject1, localBCSChild2);
/*      */         }
/*      */       }
/*      */ 
/*  552 */       fireChildrenRemoved(new BeanContextMembershipEvent(getBeanContextPeer(), new Object[] { paramObject, localObject1 == null ? new Object[] { paramObject } : localObject1 }));
/*      */     }
/*      */ 
/*  556 */     return true;
/*      */   }
/*      */ 
/*      */   public boolean containsAll(Collection paramCollection)
/*      */   {
/*  570 */     synchronized (this.children) {
/*  571 */       Iterator localIterator = paramCollection.iterator();
/*  572 */       while (localIterator.hasNext()) {
/*  573 */         if (!contains(localIterator.next()))
/*  574 */           return false;
/*      */       }
/*  576 */       return true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean addAll(Collection paramCollection)
/*      */   {
/*  586 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean removeAll(Collection paramCollection)
/*      */   {
/*  595 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public boolean retainAll(Collection paramCollection)
/*      */   {
/*  605 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void clear()
/*      */   {
/*  614 */     throw new UnsupportedOperationException();
/*      */   }
/*      */ 
/*      */   public void addBeanContextMembershipListener(BeanContextMembershipListener paramBeanContextMembershipListener)
/*      */   {
/*  625 */     if (paramBeanContextMembershipListener == null) throw new NullPointerException("listener");
/*      */ 
/*  627 */     synchronized (this.bcmListeners) {
/*  628 */       if (this.bcmListeners.contains(paramBeanContextMembershipListener)) {
/*  629 */         return;
/*      */       }
/*  631 */       this.bcmListeners.add(paramBeanContextMembershipListener);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void removeBeanContextMembershipListener(BeanContextMembershipListener paramBeanContextMembershipListener)
/*      */   {
/*  643 */     if (paramBeanContextMembershipListener == null) throw new NullPointerException("listener");
/*      */ 
/*  645 */     synchronized (this.bcmListeners) {
/*  646 */       if (!this.bcmListeners.contains(paramBeanContextMembershipListener)) {
/*  647 */         return;
/*      */       }
/*  649 */       this.bcmListeners.remove(paramBeanContextMembershipListener);
/*      */     }
/*      */   }
/*      */ 
/*      */   public InputStream getResourceAsStream(String paramString, BeanContextChild paramBeanContextChild)
/*      */   {
/*  662 */     if (paramString == null) throw new NullPointerException("name");
/*  663 */     if (paramBeanContextChild == null) throw new NullPointerException("bcc");
/*      */ 
/*  665 */     if (containsKey(paramBeanContextChild)) {
/*  666 */       ClassLoader localClassLoader = paramBeanContextChild.getClass().getClassLoader();
/*      */ 
/*  668 */       return localClassLoader != null ? localClassLoader.getResourceAsStream(paramString) : ClassLoader.getSystemResourceAsStream(paramString);
/*      */     }
/*  670 */     throw new IllegalArgumentException("Not a valid child");
/*      */   }
/*      */ 
/*      */   public URL getResource(String paramString, BeanContextChild paramBeanContextChild)
/*      */   {
/*  681 */     if (paramString == null) throw new NullPointerException("name");
/*  682 */     if (paramBeanContextChild == null) throw new NullPointerException("bcc");
/*      */ 
/*  684 */     if (containsKey(paramBeanContextChild)) {
/*  685 */       ClassLoader localClassLoader = paramBeanContextChild.getClass().getClassLoader();
/*      */ 
/*  687 */       return localClassLoader != null ? localClassLoader.getResource(paramString) : ClassLoader.getSystemResource(paramString);
/*      */     }
/*  689 */     throw new IllegalArgumentException("Not a valid child");
/*      */   }
/*      */ 
/*      */   public synchronized void setDesignTime(boolean paramBoolean)
/*      */   {
/*  697 */     if (this.designTime != paramBoolean) {
/*  698 */       this.designTime = paramBoolean;
/*      */ 
/*  700 */       firePropertyChange("designMode", Boolean.valueOf(!paramBoolean), Boolean.valueOf(paramBoolean));
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized boolean isDesignTime()
/*      */   {
/*  711 */     return this.designTime;
/*      */   }
/*      */ 
/*      */   public synchronized void setLocale(Locale paramLocale)
/*      */     throws PropertyVetoException
/*      */   {
/*  721 */     if ((this.locale != null) && (!this.locale.equals(paramLocale)) && (paramLocale != null)) {
/*  722 */       Locale localLocale = this.locale;
/*      */ 
/*  724 */       fireVetoableChange("locale", localLocale, paramLocale);
/*      */ 
/*  726 */       this.locale = paramLocale;
/*      */ 
/*  728 */       firePropertyChange("locale", localLocale, paramLocale);
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized Locale getLocale()
/*      */   {
/*  737 */     return this.locale;
/*      */   }
/*      */ 
/*      */   public synchronized boolean needsGui()
/*      */   {
/*  752 */     BeanContext localBeanContext = getBeanContextPeer();
/*      */ 
/*  754 */     if (localBeanContext != this) {
/*  755 */       if ((localBeanContext instanceof Visibility)) return localBeanContext.needsGui();
/*      */ 
/*  757 */       if (((localBeanContext instanceof Container)) || ((localBeanContext instanceof Component)))
/*  758 */         return true;
/*      */     }
/*      */     Iterator localIterator;
/*  761 */     synchronized (this.children) {
/*  762 */       for (localIterator = this.children.keySet().iterator(); localIterator.hasNext(); ) {
/*  763 */         Object localObject1 = localIterator.next();
/*      */         try
/*      */         {
/*  766 */           return ((Visibility)localObject1).needsGui();
/*      */         }
/*      */         catch (ClassCastException localClassCastException)
/*      */         {
/*  771 */           if (((localObject1 instanceof Container)) || ((localObject1 instanceof Component)))
/*  772 */             return true;
/*      */         }
/*      */       }
/*      */     }
/*  776 */     return false;
/*      */   }
/*      */ 
/*      */   public synchronized void dontUseGui()
/*      */   {
/*  784 */     if (this.okToUseGui) {
/*  785 */       this.okToUseGui = false;
/*      */       Iterator localIterator;
/*  788 */       synchronized (this.children) {
/*  789 */         for (localIterator = this.children.keySet().iterator(); localIterator.hasNext(); ) {
/*  790 */           Visibility localVisibility = getChildVisibility(localIterator.next());
/*      */ 
/*  792 */           if (localVisibility != null) localVisibility.dontUseGui();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized void okToUseGui()
/*      */   {
/*  803 */     if (!this.okToUseGui) {
/*  804 */       this.okToUseGui = true;
/*      */       Iterator localIterator;
/*  807 */       synchronized (this.children) {
/*  808 */         for (localIterator = this.children.keySet().iterator(); localIterator.hasNext(); ) {
/*  809 */           Visibility localVisibility = getChildVisibility(localIterator.next());
/*      */ 
/*  811 */           if (localVisibility != null) localVisibility.okToUseGui();
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean avoidingGui()
/*      */   {
/*  824 */     return (!this.okToUseGui) && (needsGui());
/*      */   }
/*      */ 
/*      */   public boolean isSerializing()
/*      */   {
/*  833 */     return this.serializing;
/*      */   }
/*      */ 
/*      */   protected Iterator bcsChildren()
/*      */   {
/*  840 */     synchronized (this.children) { return this.children.values().iterator(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   protected void bcsPreSerializationHook(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void bcsPreDeserializationHook(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void childDeserializedHook(Object paramObject, BCSChild paramBCSChild)
/*      */   {
/*  878 */     synchronized (this.children) {
/*  879 */       this.children.put(paramObject, paramBCSChild);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void serialize(ObjectOutputStream paramObjectOutputStream, Collection paramCollection)
/*      */     throws IOException
/*      */   {
/*  891 */     int i = 0;
/*  892 */     Object[] arrayOfObject = paramCollection.toArray();
/*      */ 
/*  894 */     for (int j = 0; j < arrayOfObject.length; j++) {
/*  895 */       if ((arrayOfObject[j] instanceof Serializable))
/*  896 */         i++;
/*      */       else {
/*  898 */         arrayOfObject[j] = null;
/*      */       }
/*      */     }
/*  901 */     paramObjectOutputStream.writeInt(i);
/*      */ 
/*  903 */     for (j = 0; i > 0; j++) {
/*  904 */       Object localObject = arrayOfObject[j];
/*      */ 
/*  906 */       if (localObject != null) {
/*  907 */         paramObjectOutputStream.writeObject(localObject);
/*  908 */         i--;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void deserialize(ObjectInputStream paramObjectInputStream, Collection paramCollection)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  919 */     int i = 0;
/*      */ 
/*  921 */     i = paramObjectInputStream.readInt();
/*      */ 
/*  923 */     while (i-- > 0)
/*  924 */       paramCollection.add(paramObjectInputStream.readObject());
/*      */   }
/*      */ 
/*      */   public final void writeChildren(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/*  936 */     if (this.serializable <= 0) return;
/*      */ 
/*  938 */     boolean bool = this.serializing;
/*      */ 
/*  940 */     this.serializing = true;
/*      */ 
/*  942 */     int i = 0;
/*      */ 
/*  944 */     synchronized (this.children) {
/*  945 */       Iterator localIterator = this.children.entrySet().iterator();
/*      */ 
/*  947 */       while ((localIterator.hasNext()) && (i < this.serializable)) {
/*  948 */         Map.Entry localEntry = (Map.Entry)localIterator.next();
/*      */ 
/*  950 */         if ((localEntry.getKey() instanceof Serializable)) {
/*      */           try {
/*  952 */             paramObjectOutputStream.writeObject(localEntry.getKey());
/*  953 */             paramObjectOutputStream.writeObject(localEntry.getValue());
/*      */           } catch (IOException localIOException) {
/*  955 */             this.serializing = bool;
/*  956 */             throw localIOException;
/*      */           }
/*  958 */           i++;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  963 */     this.serializing = bool;
/*      */ 
/*  965 */     if (i != this.serializable)
/*  966 */       throw new IOException("wrote different number of children than expected");
/*      */   }
/*      */ 
/*      */   private synchronized void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  986 */     this.serializing = true;
/*      */ 
/*  988 */     synchronized (VetoableChangeListener.globalHierarchyLock) {
/*      */       try {
/*  990 */         paramObjectOutputStream.defaultWriteObject();
/*      */ 
/*  992 */         bcsPreSerializationHook(paramObjectOutputStream);
/*      */ 
/*  994 */         if ((this.serializable > 0) && (equals(getBeanContextPeer()))) {
/*  995 */           writeChildren(paramObjectOutputStream);
/*      */         }
/*  997 */         serialize(paramObjectOutputStream, this.bcmListeners);
/*      */       } finally {
/*  999 */         this.serializing = false;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void readChildren(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1011 */     int i = this.serializable;
/*      */ 
/* 1013 */     while (i-- > 0) {
/* 1014 */       Object localObject1 = null;
/* 1015 */       BCSChild localBCSChild = null;
/*      */       try
/*      */       {
/* 1018 */         localObject1 = paramObjectInputStream.readObject();
/* 1019 */         localBCSChild = (BCSChild)paramObjectInputStream.readObject();
/*      */       } catch (IOException localIOException) {
/* 1021 */         continue; } catch (ClassNotFoundException localClassNotFoundException) {
/*      */       }
/* 1023 */       continue;
/*      */ 
/* 1027 */       synchronized (localObject1) {
/* 1028 */         BeanContextChild localBeanContextChild = null;
/*      */         try
/*      */         {
/* 1031 */           localBeanContextChild = (BeanContextChild)localObject1;
/*      */         }
/*      */         catch (ClassCastException localClassCastException)
/*      */         {
/*      */         }
/* 1036 */         if (localBeanContextChild != null) {
/*      */           try {
/* 1038 */             localBeanContextChild.setBeanContext(getBeanContextPeer());
/*      */ 
/* 1040 */             localBeanContextChild.addPropertyChangeListener("beanContext", this.childPCL);
/* 1041 */             localBeanContextChild.addVetoableChangeListener("beanContext", this.childVCL);
/*      */           }
/*      */           catch (PropertyVetoException localPropertyVetoException)
/*      */           {
/*      */           }
/*      */         }
/*      */         else
/* 1048 */           childDeserializedHook(localObject1, localBCSChild);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1061 */     synchronized (VetoableChangeListener.globalHierarchyLock) {
/* 1062 */       paramObjectInputStream.defaultReadObject();
/*      */ 
/* 1064 */       initialize();
/*      */ 
/* 1066 */       bcsPreDeserializationHook(paramObjectInputStream);
/*      */ 
/* 1068 */       if ((this.serializable > 0) && (equals(getBeanContextPeer()))) {
/* 1069 */         readChildren(paramObjectInputStream);
/*      */       }
/* 1071 */       deserialize(paramObjectInputStream, this.bcmListeners = new ArrayList(1));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void vetoableChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */     throws PropertyVetoException
/*      */   {
/* 1080 */     String str = paramPropertyChangeEvent.getPropertyName();
/* 1081 */     Object localObject1 = paramPropertyChangeEvent.getSource();
/*      */ 
/* 1083 */     synchronized (this.children) {
/* 1084 */       if (("beanContext".equals(str)) && (containsKey(localObject1)) && (!getBeanContextPeer().equals(paramPropertyChangeEvent.getNewValue())))
/*      */       {
/* 1088 */         if (!validatePendingRemove(localObject1))
/* 1089 */           throw new PropertyVetoException("current BeanContext vetoes setBeanContext()", paramPropertyChangeEvent);
/* 1090 */         ((BCSChild)this.children.get(localObject1)).setRemovePending(true);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void propertyChange(PropertyChangeEvent paramPropertyChangeEvent)
/*      */   {
/* 1100 */     String str = paramPropertyChangeEvent.getPropertyName();
/* 1101 */     Object localObject1 = paramPropertyChangeEvent.getSource();
/*      */ 
/* 1103 */     synchronized (this.children) {
/* 1104 */       if (("beanContext".equals(str)) && (containsKey(localObject1)) && (((BCSChild)this.children.get(localObject1)).isRemovePending()))
/*      */       {
/* 1107 */         BeanContext localBeanContext = getBeanContextPeer();
/*      */ 
/* 1109 */         if ((localBeanContext.equals(paramPropertyChangeEvent.getOldValue())) && (!localBeanContext.equals(paramPropertyChangeEvent.getNewValue())))
/* 1110 */           remove(localObject1, false);
/*      */         else
/* 1112 */           ((BCSChild)this.children.get(localObject1)).setRemovePending(false);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   protected boolean validatePendingAdd(Object paramObject)
/*      */   {
/* 1129 */     return true;
/*      */   }
/*      */ 
/*      */   protected boolean validatePendingRemove(Object paramObject)
/*      */   {
/* 1143 */     return true;
/*      */   }
/*      */ 
/*      */   protected void childJustAddedHook(Object paramObject, BCSChild paramBCSChild)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void childJustRemovedHook(Object paramObject, BCSChild paramBCSChild)
/*      */   {
/*      */   }
/*      */ 
/*      */   protected static final Visibility getChildVisibility(Object paramObject)
/*      */   {
/*      */     try
/*      */     {
/* 1171 */       return (Visibility)paramObject; } catch (ClassCastException localClassCastException) {
/*      */     }
/* 1173 */     return null;
/*      */   }
/*      */ 
/*      */   protected static final Serializable getChildSerializable(Object paramObject)
/*      */   {
/*      */     try
/*      */     {
/* 1184 */       return (Serializable)paramObject; } catch (ClassCastException localClassCastException) {
/*      */     }
/* 1186 */     return null;
/*      */   }
/*      */ 
/*      */   protected static final PropertyChangeListener getChildPropertyChangeListener(Object paramObject)
/*      */   {
/*      */     try
/*      */     {
/* 1198 */       return (PropertyChangeListener)paramObject; } catch (ClassCastException localClassCastException) {
/*      */     }
/* 1200 */     return null;
/*      */   }
/*      */ 
/*      */   protected static final VetoableChangeListener getChildVetoableChangeListener(Object paramObject)
/*      */   {
/*      */     try
/*      */     {
/* 1212 */       return (VetoableChangeListener)paramObject; } catch (ClassCastException localClassCastException) {
/*      */     }
/* 1214 */     return null;
/*      */   }
/*      */ 
/*      */   protected static final BeanContextMembershipListener getChildBeanContextMembershipListener(Object paramObject)
/*      */   {
/*      */     try
/*      */     {
/* 1226 */       return (BeanContextMembershipListener)paramObject; } catch (ClassCastException localClassCastException) {
/*      */     }
/* 1228 */     return null;
/*      */   }
/*      */ 
/*      */   protected static final BeanContextChild getChildBeanContextChild(Object paramObject)
/*      */   {
/*      */     try
/*      */     {
/* 1240 */       BeanContextChild localBeanContextChild = (BeanContextChild)paramObject;
/*      */ 
/* 1242 */       if (((paramObject instanceof BeanContextChild)) && ((paramObject instanceof BeanContextProxy))) {
/* 1243 */         throw new IllegalArgumentException("child cannot implement both BeanContextChild and BeanContextProxy");
/*      */       }
/* 1245 */       return localBeanContextChild;
/*      */     } catch (ClassCastException localClassCastException1) {
/*      */       try {
/* 1248 */         return ((BeanContextProxy)paramObject).getBeanContextProxy(); } catch (ClassCastException localClassCastException2) {  }
/*      */     }
/* 1250 */     return null;
/*      */   }
/*      */ 
/*      */   protected final void fireChildrenAdded(BeanContextMembershipEvent paramBeanContextMembershipEvent)
/*      */   {
/* 1262 */     Object[] arrayOfObject;
/* 1262 */     synchronized (this.bcmListeners) { arrayOfObject = this.bcmListeners.toArray(); }
/*      */ 
/* 1264 */     for (int i = 0; i < arrayOfObject.length; i++)
/* 1265 */       ((BeanContextMembershipListener)arrayOfObject[i]).childrenAdded(paramBeanContextMembershipEvent);
/*      */   }
/*      */ 
/*      */   protected final void fireChildrenRemoved(BeanContextMembershipEvent paramBeanContextMembershipEvent)
/*      */   {
/* 1275 */     Object[] arrayOfObject;
/* 1275 */     synchronized (this.bcmListeners) { arrayOfObject = this.bcmListeners.toArray(); }
/*      */ 
/* 1277 */     for (int i = 0; i < arrayOfObject.length; i++)
/* 1278 */       ((BeanContextMembershipListener)arrayOfObject[i]).childrenRemoved(paramBeanContextMembershipEvent);
/*      */   }
/*      */ 
/*      */   protected synchronized void initialize()
/*      */   {
/* 1293 */     this.children = new HashMap(this.serializable + 1);
/* 1294 */     this.bcmListeners = new ArrayList(1);
/*      */ 
/* 1296 */     this.childPCL = new PropertyChangeListener()
/*      */     {
/*      */       public void propertyChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent)
/*      */       {
/* 1306 */         BeanContextSupport.this.propertyChange(paramAnonymousPropertyChangeEvent);
/*      */       }
/*      */     };
/* 1310 */     this.childVCL = new VetoableChangeListener()
/*      */     {
/*      */       public void vetoableChange(PropertyChangeEvent paramAnonymousPropertyChangeEvent)
/*      */         throws PropertyVetoException
/*      */       {
/* 1320 */         BeanContextSupport.this.vetoableChange(paramAnonymousPropertyChangeEvent);
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   protected final Object[] copyChildren()
/*      */   {
/* 1330 */     synchronized (this.children) { return this.children.keySet().toArray(); }
/*      */ 
/*      */   }
/*      */ 
/*      */   protected static final boolean classEquals(Class paramClass1, Class paramClass2)
/*      */   {
/* 1341 */     return (paramClass1.equals(paramClass2)) || (paramClass1.getName().equals(paramClass2.getName()));
/*      */   }
/*      */ 
/*      */   protected class BCSChild
/*      */     implements Serializable
/*      */   {
/*      */     private static final long serialVersionUID = -5815286101609939109L;
/*      */     private Object child;
/*      */     private Object proxyPeer;
/*      */     private transient boolean removePending;
/*      */ 
/*      */     BCSChild(Object paramObject1, Object arg3)
/*      */     {
/*  324 */       this.child = paramObject1;
/*      */       Object localObject;
/*  325 */       this.proxyPeer = localObject;
/*      */     }
/*      */     Object getChild() {
/*  328 */       return this.child;
/*      */     }
/*  330 */     void setRemovePending(boolean paramBoolean) { this.removePending = paramBoolean; } 
/*      */     boolean isRemovePending() {
/*  332 */       return this.removePending;
/*      */     }
/*  334 */     boolean isProxyPeer() { return this.proxyPeer != null; } 
/*      */     Object getProxyPeer() {
/*  336 */       return this.proxyPeer;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static final class BCSIterator
/*      */     implements Iterator
/*      */   {
/*      */     private Iterator src;
/*      */ 
/*      */     BCSIterator(Iterator paramIterator)
/*      */     {
/*  297 */       this.src = paramIterator;
/*      */     }
/*  299 */     public boolean hasNext() { return this.src.hasNext(); } 
/*  300 */     public Object next() { return this.src.next(); }
/*      */ 
/*      */ 
/*      */     public void remove()
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.beancontext.BeanContextSupport
 * JD-Core Version:    0.6.2
 */