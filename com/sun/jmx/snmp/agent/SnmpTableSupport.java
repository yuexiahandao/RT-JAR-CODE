/*     */ package com.sun.jmx.snmp.agent;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.management.ListenerNotFoundException;
/*     */ import javax.management.MBeanNotificationInfo;
/*     */ import javax.management.NotificationFilter;
/*     */ import javax.management.NotificationListener;
/*     */ import javax.management.ObjectName;
/*     */ 
/*     */ public abstract class SnmpTableSupport
/*     */   implements SnmpTableEntryFactory, SnmpTableCallbackHandler, Serializable
/*     */ {
/*     */   protected List<Object> entries;
/*     */   protected SnmpMibTable meta;
/*     */   protected SnmpMib theMib;
/* 120 */   private boolean registrationRequired = false;
/*     */ 
/*     */   protected SnmpTableSupport(SnmpMib paramSnmpMib)
/*     */   {
/* 144 */     this.theMib = paramSnmpMib;
/* 145 */     this.meta = getRegisteredTableMeta(paramSnmpMib);
/* 146 */     bindWithTableMeta();
/* 147 */     this.entries = allocateTable();
/*     */   }
/*     */ 
/*     */   public abstract void createNewEntry(SnmpMibSubRequest paramSnmpMibSubRequest, SnmpOid paramSnmpOid, int paramInt, SnmpMibTable paramSnmpMibTable)
/*     */     throws SnmpStatusException;
/*     */ 
/*     */   public Object getEntry(int paramInt)
/*     */   {
/* 188 */     if (this.entries == null) return null;
/* 189 */     return this.entries.get(paramInt);
/*     */   }
/*     */ 
/*     */   public int getSize()
/*     */   {
/* 198 */     return this.meta.getSize();
/*     */   }
/*     */ 
/*     */   public void setCreationEnabled(boolean paramBoolean)
/*     */   {
/* 225 */     this.meta.setCreationEnabled(paramBoolean);
/*     */   }
/*     */ 
/*     */   public boolean isCreationEnabled()
/*     */   {
/* 240 */     return this.meta.isCreationEnabled();
/*     */   }
/*     */ 
/*     */   public boolean isRegistrationRequired()
/*     */   {
/* 252 */     return this.registrationRequired;
/*     */   }
/*     */ 
/*     */   public SnmpIndex buildSnmpIndex(SnmpOid paramSnmpOid)
/*     */     throws SnmpStatusException
/*     */   {
/* 269 */     return buildSnmpIndex(paramSnmpOid.longValue(false), 0);
/*     */   }
/*     */ 
/*     */   public abstract SnmpOid buildOidFromIndex(SnmpIndex paramSnmpIndex)
/*     */     throws SnmpStatusException;
/*     */ 
/*     */   public abstract ObjectName buildNameFromIndex(SnmpIndex paramSnmpIndex)
/*     */     throws SnmpStatusException;
/*     */ 
/*     */   public void addEntryCb(int paramInt, SnmpOid paramSnmpOid, ObjectName paramObjectName, Object paramObject, SnmpMibTable paramSnmpMibTable)
/*     */     throws SnmpStatusException
/*     */   {
/*     */     try
/*     */     {
/* 333 */       if (this.entries != null) this.entries.add(paramInt, paramObject); 
/*     */     }
/* 335 */     catch (Exception localException) { throw new SnmpStatusException(2); }
/*     */ 
/*     */   }
/*     */ 
/*     */   public void removeEntryCb(int paramInt, SnmpOid paramSnmpOid, ObjectName paramObjectName, Object paramObject, SnmpMibTable paramSnmpMibTable)
/*     */     throws SnmpStatusException
/*     */   {
/*     */     try
/*     */     {
/* 359 */       if (this.entries != null) this.entries.remove(paramInt);
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addNotificationListener(NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*     */   {
/* 384 */     this.meta.addNotificationListener(paramNotificationListener, paramNotificationFilter, paramObject);
/*     */   }
/*     */ 
/*     */   public synchronized void removeNotificationListener(NotificationListener paramNotificationListener)
/*     */     throws ListenerNotFoundException
/*     */   {
/* 402 */     this.meta.removeNotificationListener(paramNotificationListener);
/*     */   }
/*     */ 
/*     */   public MBeanNotificationInfo[] getNotificationInfo()
/*     */   {
/* 411 */     return this.meta.getNotificationInfo();
/*     */   }
/*     */ 
/*     */   protected abstract SnmpIndex buildSnmpIndex(long[] paramArrayOfLong, int paramInt)
/*     */     throws SnmpStatusException;
/*     */ 
/*     */   protected abstract SnmpMibTable getRegisteredTableMeta(SnmpMib paramSnmpMib);
/*     */ 
/*     */   protected List<Object> allocateTable()
/*     */   {
/* 469 */     return new ArrayList();
/*     */   }
/*     */ 
/*     */   protected void addEntry(SnmpIndex paramSnmpIndex, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 494 */     SnmpOid localSnmpOid = buildOidFromIndex(paramSnmpIndex);
/* 495 */     ObjectName localObjectName = null;
/* 496 */     if (isRegistrationRequired()) {
/* 497 */       localObjectName = buildNameFromIndex(paramSnmpIndex);
/*     */     }
/* 499 */     this.meta.addEntry(localSnmpOid, localObjectName, paramObject);
/*     */   }
/*     */ 
/*     */   protected void addEntry(SnmpIndex paramSnmpIndex, ObjectName paramObjectName, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 517 */     SnmpOid localSnmpOid = buildOidFromIndex(paramSnmpIndex);
/* 518 */     this.meta.addEntry(localSnmpOid, paramObjectName, paramObject);
/*     */   }
/*     */ 
/*     */   protected void removeEntry(SnmpIndex paramSnmpIndex, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 539 */     SnmpOid localSnmpOid = buildOidFromIndex(paramSnmpIndex);
/* 540 */     this.meta.removeEntry(localSnmpOid, paramObject);
/*     */   }
/*     */ 
/*     */   protected Object[] getBasicEntries()
/*     */   {
/* 555 */     if (this.entries == null) return null;
/* 556 */     Object[] arrayOfObject = new Object[this.entries.size()];
/* 557 */     this.entries.toArray(arrayOfObject);
/* 558 */     return arrayOfObject;
/*     */   }
/*     */ 
/*     */   protected void bindWithTableMeta()
/*     */   {
/* 566 */     if (this.meta == null) return;
/* 567 */     this.registrationRequired = this.meta.isRegistrationRequired();
/* 568 */     this.meta.registerEntryFactory(this);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpTableSupport
 * JD-Core Version:    0.6.2
 */