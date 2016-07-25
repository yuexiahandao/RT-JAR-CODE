/*     */ package java.beans.beancontext;
/*     */ 
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.beans.PropertyChangeSupport;
/*     */ import java.beans.PropertyVetoException;
/*     */ import java.beans.VetoableChangeListener;
/*     */ import java.beans.VetoableChangeSupport;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class BeanContextChildSupport
/*     */   implements BeanContextChild, BeanContextServicesListener, Serializable
/*     */ {
/*     */   static final long serialVersionUID = 6328947014421475877L;
/*     */   public BeanContextChild beanContextChildPeer;
/*     */   protected PropertyChangeSupport pcSupport;
/*     */   protected VetoableChangeSupport vcSupport;
/*     */   protected transient BeanContext beanContext;
/*     */   protected transient boolean rejectedSetBCOnce;
/*     */ 
/*     */   public BeanContextChildSupport()
/*     */   {
/*  71 */     this.beanContextChildPeer = this;
/*     */ 
/*  73 */     this.pcSupport = new PropertyChangeSupport(this.beanContextChildPeer);
/*  74 */     this.vcSupport = new VetoableChangeSupport(this.beanContextChildPeer);
/*     */   }
/*     */ 
/*     */   public BeanContextChildSupport(BeanContextChild paramBeanContextChild)
/*     */   {
/*  86 */     this.beanContextChildPeer = (paramBeanContextChild != null ? paramBeanContextChild : this);
/*     */ 
/*  88 */     this.pcSupport = new PropertyChangeSupport(this.beanContextChildPeer);
/*  89 */     this.vcSupport = new VetoableChangeSupport(this.beanContextChildPeer);
/*     */   }
/*     */ 
/*     */   public synchronized void setBeanContext(BeanContext paramBeanContext)
/*     */     throws PropertyVetoException
/*     */   {
/* 100 */     if (paramBeanContext == this.beanContext) return;
/*     */ 
/* 102 */     BeanContext localBeanContext1 = this.beanContext;
/* 103 */     BeanContext localBeanContext2 = paramBeanContext;
/*     */ 
/* 105 */     if (!this.rejectedSetBCOnce) {
/* 106 */       if ((this.rejectedSetBCOnce = !validatePendingSetBeanContext(paramBeanContext) ? 1 : 0) != 0) {
/* 107 */         throw new PropertyVetoException("setBeanContext() change rejected:", new PropertyChangeEvent(this.beanContextChildPeer, "beanContext", localBeanContext1, localBeanContext2));
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 114 */         fireVetoableChange("beanContext", localBeanContext1, localBeanContext2);
/*     */       }
/*     */       catch (PropertyVetoException localPropertyVetoException)
/*     */       {
/* 119 */         this.rejectedSetBCOnce = true;
/*     */ 
/* 121 */         throw localPropertyVetoException;
/*     */       }
/*     */     }
/*     */ 
/* 125 */     if (this.beanContext != null) releaseBeanContextResources();
/*     */ 
/* 127 */     this.beanContext = localBeanContext2;
/* 128 */     this.rejectedSetBCOnce = false;
/*     */ 
/* 130 */     firePropertyChange("beanContext", localBeanContext1, localBeanContext2);
/*     */ 
/* 135 */     if (this.beanContext != null) initializeBeanContextResources();
/*     */   }
/*     */ 
/*     */   public synchronized BeanContext getBeanContext()
/*     */   {
/* 144 */     return this.beanContext;
/*     */   }
/*     */ 
/*     */   public void addPropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 158 */     this.pcSupport.addPropertyChangeListener(paramString, paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public void removePropertyChangeListener(String paramString, PropertyChangeListener paramPropertyChangeListener)
/*     */   {
/* 175 */     this.pcSupport.removePropertyChangeListener(paramString, paramPropertyChangeListener);
/*     */   }
/*     */ 
/*     */   public void addVetoableChangeListener(String paramString, VetoableChangeListener paramVetoableChangeListener)
/*     */   {
/* 190 */     this.vcSupport.addVetoableChangeListener(paramString, paramVetoableChangeListener);
/*     */   }
/*     */ 
/*     */   public void removeVetoableChangeListener(String paramString, VetoableChangeListener paramVetoableChangeListener)
/*     */   {
/* 207 */     this.vcSupport.removeVetoableChangeListener(paramString, paramVetoableChangeListener);
/*     */   }
/*     */ 
/*     */   public void serviceRevoked(BeanContextServiceRevokedEvent paramBeanContextServiceRevokedEvent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void serviceAvailable(BeanContextServiceAvailableEvent paramBeanContextServiceAvailableEvent)
/*     */   {
/*     */   }
/*     */ 
/*     */   public BeanContextChild getBeanContextChildPeer()
/*     */   {
/* 237 */     return this.beanContextChildPeer;
/*     */   }
/*     */ 
/*     */   public boolean isDelegated()
/*     */   {
/* 244 */     return !equals(this.beanContextChildPeer);
/*     */   }
/*     */ 
/*     */   public void firePropertyChange(String paramString, Object paramObject1, Object paramObject2)
/*     */   {
/* 254 */     this.pcSupport.firePropertyChange(paramString, paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public void fireVetoableChange(String paramString, Object paramObject1, Object paramObject2)
/*     */     throws PropertyVetoException
/*     */   {
/* 275 */     this.vcSupport.fireVetoableChange(paramString, paramObject1, paramObject2);
/*     */   }
/*     */ 
/*     */   public boolean validatePendingSetBeanContext(BeanContext paramBeanContext)
/*     */   {
/* 288 */     return true;
/*     */   }
/*     */ 
/*     */   protected void releaseBeanContextResources()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void initializeBeanContextResources()
/*     */   {
/*     */   }
/*     */ 
/*     */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*     */     throws IOException
/*     */   {
/* 323 */     if ((!equals(this.beanContextChildPeer)) && (!(this.beanContextChildPeer instanceof Serializable))) {
/* 324 */       throw new IOException("BeanContextChildSupport beanContextChildPeer not Serializable");
/*     */     }
/*     */ 
/* 327 */     paramObjectOutputStream.defaultWriteObject();
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 339 */     paramObjectInputStream.defaultReadObject();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.beancontext.BeanContextChildSupport
 * JD-Core Version:    0.6.2
 */