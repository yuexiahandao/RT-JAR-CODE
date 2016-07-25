/*      */ package com.sun.jmx.snmp.agent;
/*      */ 
/*      */ import com.sun.jmx.defaults.JmxProperties;
/*      */ import com.sun.jmx.snmp.SnmpInt;
/*      */ import com.sun.jmx.snmp.SnmpOid;
/*      */ import com.sun.jmx.snmp.SnmpStatusException;
/*      */ import com.sun.jmx.snmp.SnmpValue;
/*      */ import com.sun.jmx.snmp.SnmpVarBind;
/*      */ import java.io.Serializable;
/*      */ import java.util.Date;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Vector;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.management.ListenerNotFoundException;
/*      */ import javax.management.MBeanNotificationInfo;
/*      */ import javax.management.Notification;
/*      */ import javax.management.NotificationBroadcaster;
/*      */ import javax.management.NotificationFilter;
/*      */ import javax.management.NotificationListener;
/*      */ import javax.management.ObjectName;
/*      */ 
/*      */ public abstract class SnmpMibTable extends SnmpMibNode
/*      */   implements NotificationBroadcaster, Serializable
/*      */ {
/* 2486 */   protected int nodeId = 1;
/*      */   protected SnmpMib theMib;
/* 2500 */   protected boolean creationEnabled = false;
/*      */ 
/* 2505 */   protected SnmpTableEntryFactory factory = null;
/*      */ 
/* 2515 */   private int size = 0;
/*      */   private static final int Delta = 16;
/* 2529 */   private int tablecount = 0;
/* 2530 */   private int tablesize = 16;
/* 2531 */   private SnmpOid[] tableoids = new SnmpOid[this.tablesize];
/*      */ 
/* 2537 */   private final Vector<Object> entries = new Vector();
/*      */ 
/* 2543 */   private final Vector<ObjectName> entrynames = new Vector();
/*      */ 
/* 2553 */   private Hashtable<NotificationListener, Vector<Object>> handbackTable = new Hashtable();
/*      */ 
/* 2559 */   private Hashtable<NotificationListener, Vector<NotificationFilter>> filterTable = new Hashtable();
/*      */ 
/* 2569 */   transient long sequenceNumber = 0L;
/*      */ 
/*      */   public SnmpMibTable(SnmpMib paramSnmpMib)
/*      */   {
/*   98 */     this.theMib = paramSnmpMib;
/*   99 */     setCreationEnabled(false);
/*      */   }
/*      */ 
/*      */   public abstract void createNewEntry(SnmpMibSubRequest paramSnmpMibSubRequest, SnmpOid paramSnmpOid, int paramInt)
/*      */     throws SnmpStatusException;
/*      */ 
/*      */   public abstract boolean isRegistrationRequired();
/*      */ 
/*      */   public boolean isCreationEnabled()
/*      */   {
/*  156 */     return this.creationEnabled;
/*      */   }
/*      */ 
/*      */   public void setCreationEnabled(boolean paramBoolean)
/*      */   {
/*  176 */     this.creationEnabled = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean hasRowStatus()
/*      */   {
/*  217 */     return false;
/*      */   }
/*      */ 
/*      */   public void get(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*      */     throws SnmpStatusException
/*      */   {
/*  272 */     boolean bool = paramSnmpMibSubRequest.isNewEntry();
/*  273 */     SnmpMibSubRequest localSnmpMibSubRequest = paramSnmpMibSubRequest;
/*      */     Enumeration localEnumeration;
/*  278 */     if (bool) {
/*  279 */       localObject = null;
/*  280 */       for (localEnumeration = localSnmpMibSubRequest.getElements(); localEnumeration.hasMoreElements(); ) {
/*  281 */         localObject = (SnmpVarBind)localEnumeration.nextElement();
/*  282 */         localSnmpMibSubRequest.registerGetException((SnmpVarBind)localObject, new SnmpStatusException(224));
/*      */       }
/*      */     }
/*      */ 
/*  286 */     Object localObject = localSnmpMibSubRequest.getEntryOid();
/*      */ 
/*  291 */     get(paramSnmpMibSubRequest, (SnmpOid)localObject, paramInt + 1);
/*      */   }
/*      */ 
/*      */   public void check(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*      */     throws SnmpStatusException
/*      */   {
/*  334 */     SnmpOid localSnmpOid = paramSnmpMibSubRequest.getEntryOid();
/*  335 */     int i = getRowAction(paramSnmpMibSubRequest, localSnmpOid, paramInt + 1);
/*      */ 
/*  337 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  338 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "check", "Calling beginRowAction");
/*      */     }
/*      */ 
/*  342 */     beginRowAction(paramSnmpMibSubRequest, localSnmpOid, paramInt + 1, i);
/*      */ 
/*  344 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  345 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "check", "Calling check for " + paramSnmpMibSubRequest.getSize() + " varbinds");
/*      */     }
/*      */ 
/*  350 */     check(paramSnmpMibSubRequest, localSnmpOid, paramInt + 1);
/*      */ 
/*  352 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))
/*  353 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "check", "check finished");
/*      */   }
/*      */ 
/*      */   public void set(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*      */     throws SnmpStatusException
/*      */   {
/*  396 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  397 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "set", "Entering set");
/*      */     }
/*      */ 
/*  401 */     SnmpOid localSnmpOid = paramSnmpMibSubRequest.getEntryOid();
/*  402 */     int i = getRowAction(paramSnmpMibSubRequest, localSnmpOid, paramInt + 1);
/*      */ 
/*  404 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  405 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "set", "Calling set for " + paramSnmpMibSubRequest.getSize() + " varbinds");
/*      */     }
/*      */ 
/*  409 */     set(paramSnmpMibSubRequest, localSnmpOid, paramInt + 1);
/*      */ 
/*  411 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  412 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "set", "Calling endRowAction");
/*      */     }
/*      */ 
/*  416 */     endRowAction(paramSnmpMibSubRequest, localSnmpOid, paramInt + 1, i);
/*      */ 
/*  418 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))
/*  419 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "set", "RowAction finished");
/*      */   }
/*      */ 
/*      */   public void addEntry(SnmpOid paramSnmpOid, Object paramObject)
/*      */     throws SnmpStatusException
/*      */   {
/*  452 */     addEntry(paramSnmpOid, null, paramObject);
/*      */   }
/*      */ 
/*      */   public synchronized void addEntry(SnmpOid paramSnmpOid, ObjectName paramObjectName, Object paramObject)
/*      */     throws SnmpStatusException
/*      */   {
/*  482 */     if ((isRegistrationRequired() == true) && (paramObjectName == null)) {
/*  483 */       throw new SnmpStatusException(3);
/*      */     }
/*  485 */     if (this.size == 0)
/*      */     {
/*  488 */       insertOid(0, paramSnmpOid);
/*  489 */       if (this.entries != null)
/*  490 */         this.entries.addElement(paramObject);
/*  491 */       if (this.entrynames != null)
/*  492 */         this.entrynames.addElement(paramObjectName);
/*  493 */       this.size += 1;
/*      */ 
/*  497 */       if (this.factory != null) {
/*      */         try {
/*  499 */           this.factory.addEntryCb(0, paramSnmpOid, paramObjectName, paramObject, this);
/*      */         } catch (SnmpStatusException localSnmpStatusException1) {
/*  501 */           removeOid(0);
/*  502 */           if (this.entries != null)
/*  503 */             this.entries.removeElementAt(0);
/*  504 */           if (this.entrynames != null)
/*  505 */             this.entrynames.removeElementAt(0);
/*  506 */           throw localSnmpStatusException1;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  512 */       sendNotification("jmx.snmp.table.entry.added", new Date().getTime(), paramObject, paramObjectName);
/*      */ 
/*  514 */       return;
/*      */     }
/*      */ 
/*  519 */     int i = 0;
/*      */ 
/*  523 */     i = getInsertionPoint(paramSnmpOid, true);
/*  524 */     if (i == this.size)
/*      */     {
/*  529 */       insertOid(this.tablecount, paramSnmpOid);
/*  530 */       if (this.entries != null)
/*  531 */         this.entries.addElement(paramObject);
/*  532 */       if (this.entrynames != null)
/*  533 */         this.entrynames.addElement(paramObjectName);
/*  534 */       this.size += 1;
/*      */     }
/*      */     else
/*      */     {
/*      */       try
/*      */       {
/*  541 */         insertOid(i, paramSnmpOid);
/*  542 */         if (this.entries != null)
/*  543 */           this.entries.insertElementAt(paramObject, i);
/*  544 */         if (this.entrynames != null)
/*  545 */           this.entrynames.insertElementAt(paramObjectName, i);
/*  546 */         this.size += 1;
/*      */       }
/*      */       catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*  553 */     if (this.factory != null) {
/*      */       try {
/*  555 */         this.factory.addEntryCb(i, paramSnmpOid, paramObjectName, paramObject, this);
/*      */       } catch (SnmpStatusException localSnmpStatusException2) {
/*  557 */         removeOid(i);
/*  558 */         if (this.entries != null)
/*  559 */           this.entries.removeElementAt(i);
/*  560 */         if (this.entrynames != null)
/*  561 */           this.entrynames.removeElementAt(i);
/*  562 */         throw localSnmpStatusException2;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  568 */     sendNotification("jmx.snmp.table.entry.added", new Date().getTime(), paramObject, paramObjectName);
/*      */   }
/*      */ 
/*      */   public synchronized void removeEntry(SnmpOid paramSnmpOid, Object paramObject)
/*      */     throws SnmpStatusException
/*      */   {
/*  592 */     int i = findObject(paramSnmpOid);
/*  593 */     if (i == -1)
/*  594 */       return;
/*  595 */     removeEntry(i, paramObject);
/*      */   }
/*      */ 
/*      */   public void removeEntry(SnmpOid paramSnmpOid)
/*      */     throws SnmpStatusException
/*      */   {
/*  614 */     int i = findObject(paramSnmpOid);
/*  615 */     if (i == -1)
/*  616 */       return;
/*  617 */     removeEntry(i, null);
/*      */   }
/*      */ 
/*      */   public synchronized void removeEntry(int paramInt, Object paramObject)
/*      */     throws SnmpStatusException
/*      */   {
/*  638 */     if (paramInt == -1)
/*  639 */       return;
/*  640 */     if (paramInt >= this.size) return;
/*      */ 
/*  642 */     Object localObject = paramObject;
/*  643 */     if ((this.entries != null) && (this.entries.size() > paramInt)) {
/*  644 */       localObject = this.entries.elementAt(paramInt);
/*  645 */       this.entries.removeElementAt(paramInt);
/*      */     }
/*      */ 
/*  648 */     ObjectName localObjectName = null;
/*  649 */     if ((this.entrynames != null) && (this.entrynames.size() > paramInt)) {
/*  650 */       localObjectName = (ObjectName)this.entrynames.elementAt(paramInt);
/*  651 */       this.entrynames.removeElementAt(paramInt);
/*      */     }
/*      */ 
/*  654 */     SnmpOid localSnmpOid = this.tableoids[paramInt];
/*  655 */     removeOid(paramInt);
/*  656 */     this.size -= 1;
/*      */ 
/*  658 */     if (localObject == null) localObject = paramObject;
/*      */ 
/*  660 */     if (this.factory != null) {
/*  661 */       this.factory.removeEntryCb(paramInt, localSnmpOid, localObjectName, localObject, this);
/*      */     }
/*  663 */     sendNotification("jmx.snmp.table.entry.removed", new Date().getTime(), localObject, localObjectName);
/*      */   }
/*      */ 
/*      */   public synchronized Object getEntry(SnmpOid paramSnmpOid)
/*      */     throws SnmpStatusException
/*      */   {
/*  681 */     int i = findObject(paramSnmpOid);
/*  682 */     if (i == -1)
/*  683 */       throw new SnmpStatusException(224);
/*  684 */     return this.entries.elementAt(i);
/*      */   }
/*      */ 
/*      */   public synchronized ObjectName getEntryName(SnmpOid paramSnmpOid)
/*      */     throws SnmpStatusException
/*      */   {
/*  704 */     int i = findObject(paramSnmpOid);
/*  705 */     if (this.entrynames == null) return null;
/*  706 */     if ((i == -1) || (i >= this.entrynames.size()))
/*  707 */       throw new SnmpStatusException(224);
/*  708 */     return (ObjectName)this.entrynames.elementAt(i);
/*      */   }
/*      */ 
/*      */   public Object[] getBasicEntries()
/*      */   {
/*  725 */     Object[] arrayOfObject = new Object[this.size];
/*  726 */     this.entries.copyInto(arrayOfObject);
/*  727 */     return arrayOfObject;
/*      */   }
/*      */ 
/*      */   public int getSize()
/*      */   {
/*  736 */     return this.size;
/*      */   }
/*      */ 
/*      */   public synchronized void addNotificationListener(NotificationListener paramNotificationListener, NotificationFilter paramNotificationFilter, Object paramObject)
/*      */   {
/*  764 */     if (paramNotificationListener == null) {
/*  765 */       throw new IllegalArgumentException("Listener can't be null");
/*      */     }
/*      */ 
/*  771 */     Vector localVector1 = (Vector)this.handbackTable.get(paramNotificationListener);
/*      */ 
/*  773 */     Vector localVector2 = (Vector)this.filterTable.get(paramNotificationListener);
/*      */ 
/*  775 */     if (localVector1 == null) {
/*  776 */       localVector1 = new Vector();
/*  777 */       localVector2 = new Vector();
/*  778 */       this.handbackTable.put(paramNotificationListener, localVector1);
/*  779 */       this.filterTable.put(paramNotificationListener, localVector2);
/*      */     }
/*      */ 
/*  784 */     localVector1.addElement(paramObject);
/*  785 */     localVector2.addElement(paramNotificationFilter);
/*      */   }
/*      */ 
/*      */   public synchronized void removeNotificationListener(NotificationListener paramNotificationListener)
/*      */     throws ListenerNotFoundException
/*      */   {
/*  806 */     Vector localVector1 = (Vector)this.handbackTable.get(paramNotificationListener);
/*      */ 
/*  808 */     Vector localVector2 = (Vector)this.filterTable.get(paramNotificationListener);
/*      */ 
/*  810 */     if (localVector1 == null) {
/*  811 */       throw new ListenerNotFoundException("listener");
/*      */     }
/*      */ 
/*  816 */     this.handbackTable.remove(paramNotificationListener);
/*  817 */     this.filterTable.remove(paramNotificationListener);
/*      */   }
/*      */ 
/*      */   public MBeanNotificationInfo[] getNotificationInfo()
/*      */   {
/*  827 */     String[] arrayOfString = { "jmx.snmp.table.entry.added", "jmx.snmp.table.entry.removed" };
/*      */ 
/*  830 */     MBeanNotificationInfo[] arrayOfMBeanNotificationInfo = { new MBeanNotificationInfo(arrayOfString, "com.sun.jmx.snmp.agent.SnmpTableEntryNotification", "Notifications sent by the SnmpMibTable") };
/*      */ 
/*  836 */     return arrayOfMBeanNotificationInfo;
/*      */   }
/*      */ 
/*      */   public void registerEntryFactory(SnmpTableEntryFactory paramSnmpTableEntryFactory)
/*      */   {
/*  851 */     this.factory = paramSnmpTableEntryFactory;
/*      */   }
/*      */ 
/*      */   protected boolean isRowStatus(SnmpOid paramSnmpOid, long paramLong, Object paramObject)
/*      */   {
/*  888 */     return false;
/*      */   }
/*      */ 
/*      */   protected int getRowAction(SnmpMibSubRequest paramSnmpMibSubRequest, SnmpOid paramSnmpOid, int paramInt)
/*      */     throws SnmpStatusException
/*      */   {
/*  931 */     boolean bool = paramSnmpMibSubRequest.isNewEntry();
/*  932 */     SnmpVarBind localSnmpVarBind = paramSnmpMibSubRequest.getRowStatusVarBind();
/*  933 */     if (localSnmpVarBind == null) {
/*  934 */       if ((bool) && (!hasRowStatus()))
/*  935 */         return 4;
/*  936 */       return 0;
/*      */     }
/*      */     try
/*      */     {
/*  940 */       return mapRowStatus(paramSnmpOid, localSnmpVarBind, paramSnmpMibSubRequest.getUserData());
/*      */     } catch (SnmpStatusException localSnmpStatusException) {
/*  942 */       checkRowStatusFail(paramSnmpMibSubRequest, localSnmpStatusException.getStatus());
/*      */     }
/*  944 */     return 0;
/*      */   }
/*      */ 
/*      */   protected int mapRowStatus(SnmpOid paramSnmpOid, SnmpVarBind paramSnmpVarBind, Object paramObject)
/*      */     throws SnmpStatusException
/*      */   {
/*  989 */     SnmpValue localSnmpValue = paramSnmpVarBind.value;
/*      */ 
/*  991 */     if ((localSnmpValue instanceof SnmpInt)) {
/*  992 */       return ((SnmpInt)localSnmpValue).intValue();
/*      */     }
/*  994 */     throw new SnmpStatusException(12);
/*      */   }
/*      */ 
/*      */   protected SnmpValue setRowStatus(SnmpOid paramSnmpOid, int paramInt, Object paramObject)
/*      */     throws SnmpStatusException
/*      */   {
/* 1047 */     return null;
/*      */   }
/*      */ 
/*      */   protected boolean isRowReady(SnmpOid paramSnmpOid, Object paramObject)
/*      */     throws SnmpStatusException
/*      */   {
/* 1098 */     return true;
/*      */   }
/*      */ 
/*      */   protected void checkRowStatusChange(SnmpMibSubRequest paramSnmpMibSubRequest, SnmpOid paramSnmpOid, int paramInt1, int paramInt2)
/*      */     throws SnmpStatusException
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void checkRemoveTableRow(SnmpMibSubRequest paramSnmpMibSubRequest, SnmpOid paramSnmpOid, int paramInt)
/*      */     throws SnmpStatusException
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void removeTableRow(SnmpMibSubRequest paramSnmpMibSubRequest, SnmpOid paramSnmpOid, int paramInt)
/*      */     throws SnmpStatusException
/*      */   {
/* 1212 */     removeEntry(paramSnmpOid);
/*      */   }
/*      */ 
/*      */   protected synchronized void beginRowAction(SnmpMibSubRequest paramSnmpMibSubRequest, SnmpOid paramSnmpOid, int paramInt1, int paramInt2)
/*      */     throws SnmpStatusException
/*      */   {
/* 1265 */     boolean bool = paramSnmpMibSubRequest.isNewEntry();
/* 1266 */     SnmpOid localSnmpOid = paramSnmpOid;
/* 1267 */     int i = paramInt2;
/*      */ 
/* 1269 */     switch (i) {
/*      */     case 0:
/* 1271 */       if (bool) {
/* 1272 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1273 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "beginRowAction", "Failed to create row[" + paramSnmpOid + "] : RowStatus = unspecified");
/*      */         }
/*      */ 
/* 1278 */         checkRowStatusFail(paramSnmpMibSubRequest, 6); } break;
/*      */     case 4:
/*      */     case 5:
/* 1283 */       if (bool) {
/* 1284 */         if (isCreationEnabled()) {
/* 1285 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1286 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "beginRowAction", "Creating row[" + paramSnmpOid + "] : RowStatus = createAndGo | createAndWait");
/*      */           }
/*      */ 
/* 1291 */           createNewEntry(paramSnmpMibSubRequest, localSnmpOid, paramInt1);
/*      */         } else {
/* 1293 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1294 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "beginRowAction", "Can't create row[" + paramSnmpOid + "] : RowStatus = createAndGo | createAndWait " + "but creation is disabled");
/*      */           }
/*      */ 
/* 1300 */           checkRowStatusFail(paramSnmpMibSubRequest, 6);
/*      */         }
/*      */       }
/*      */       else {
/* 1304 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1305 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "beginRowAction", "Can't create row[" + paramSnmpOid + "] : RowStatus = createAndGo | createAndWait " + "but row already exists");
/*      */         }
/*      */ 
/* 1311 */         checkRowStatusFail(paramSnmpMibSubRequest, 12);
/*      */       }
/*      */ 
/* 1314 */       break;
/*      */     case 6:
/* 1316 */       if (bool) {
/* 1317 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1318 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "beginRowAction", "Warning: can't destroy row[" + paramSnmpOid + "] : RowStatus = destroy but row does not exist");
/*      */         }
/*      */ 
/*      */       }
/* 1324 */       else if (!isCreationEnabled()) {
/* 1325 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1326 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "beginRowAction", "Can't destroy row[" + paramSnmpOid + "] : " + "RowStatus = destroy but creation is disabled");
/*      */         }
/*      */ 
/* 1332 */         checkRowStatusFail(paramSnmpMibSubRequest, 6);
/*      */       }
/* 1334 */       checkRemoveTableRow(paramSnmpMibSubRequest, paramSnmpOid, paramInt1);
/* 1335 */       break;
/*      */     case 1:
/*      */     case 2:
/* 1338 */       if (bool) {
/* 1339 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1340 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "beginRowAction", "Can't switch state of row[" + paramSnmpOid + "] : specified RowStatus = active | " + "notInService but row does not exist");
/*      */         }
/*      */ 
/* 1346 */         checkRowStatusFail(paramSnmpMibSubRequest, 12);
/*      */       }
/*      */ 
/* 1349 */       checkRowStatusChange(paramSnmpMibSubRequest, paramSnmpOid, paramInt1, i);
/* 1350 */       break;
/*      */     case 3:
/*      */     default:
/* 1353 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1354 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "beginRowAction", "Invalid RowStatus value for row[" + paramSnmpOid + "] : specified RowStatus = " + i);
/*      */       }
/*      */ 
/* 1359 */       checkRowStatusFail(paramSnmpMibSubRequest, 12);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void endRowAction(SnmpMibSubRequest paramSnmpMibSubRequest, SnmpOid paramSnmpOid, int paramInt1, int paramInt2)
/*      */     throws SnmpStatusException
/*      */   {
/* 1415 */     boolean bool = paramSnmpMibSubRequest.isNewEntry();
/* 1416 */     SnmpOid localSnmpOid = paramSnmpOid;
/* 1417 */     int i = paramInt2;
/* 1418 */     Object localObject = paramSnmpMibSubRequest.getUserData();
/* 1419 */     SnmpValue localSnmpValue = null;
/*      */ 
/* 1421 */     switch (i) {
/*      */     case 0:
/* 1423 */       break;
/*      */     case 4:
/* 1425 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1426 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "endRowAction", "Setting RowStatus to 'active' for row[" + paramSnmpOid + "] : requested RowStatus = " + "createAndGo");
/*      */       }
/*      */ 
/* 1432 */       localSnmpValue = setRowStatus(localSnmpOid, 1, localObject);
/* 1433 */       break;
/*      */     case 5:
/* 1435 */       if (isRowReady(localSnmpOid, localObject)) {
/* 1436 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1437 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "endRowAction", "Setting RowStatus to 'notInService' for row[" + paramSnmpOid + "] : requested RowStatus = createAndWait");
/*      */         }
/*      */ 
/* 1443 */         localSnmpValue = setRowStatus(localSnmpOid, 2, localObject);
/*      */       } else {
/* 1445 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1446 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "endRowAction", "Setting RowStatus to 'notReady' for row[" + paramSnmpOid + "] : requested RowStatus = " + "createAndWait");
/*      */         }
/*      */ 
/* 1452 */         localSnmpValue = setRowStatus(localSnmpOid, 3, localObject);
/*      */       }
/* 1454 */       break;
/*      */     case 6:
/* 1456 */       if (bool) {
/* 1457 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1458 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "endRowAction", "Warning: requested RowStatus = destroy, but row[" + paramSnmpOid + "] does not exist");
/*      */         }
/*      */ 
/*      */       }
/* 1465 */       else if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1466 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "endRowAction", "Destroying row[" + paramSnmpOid + "] : requested RowStatus = destroy");
/*      */       }
/*      */ 
/* 1472 */       removeTableRow(paramSnmpMibSubRequest, localSnmpOid, paramInt1);
/* 1473 */       break;
/*      */     case 1:
/* 1475 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1476 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "endRowAction", "Setting RowStatus to 'active' for row[" + paramSnmpOid + "] : requested RowStatus = active");
/*      */       }
/*      */ 
/* 1482 */       localSnmpValue = setRowStatus(localSnmpOid, 1, localObject);
/* 1483 */       break;
/*      */     case 2:
/* 1485 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1486 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "endRowAction", "Setting RowStatus to 'notInService' for row[" + paramSnmpOid + "] : requested RowStatus = notInService");
/*      */       }
/*      */ 
/* 1492 */       localSnmpValue = setRowStatus(localSnmpOid, 2, localObject);
/* 1493 */       break;
/*      */     case 3:
/*      */     default:
/* 1496 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1497 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMibTable.class.getName(), "endRowAction", "Invalid RowStatus value for row[" + paramSnmpOid + "] : specified RowStatus = " + i);
/*      */       }
/*      */ 
/* 1502 */       setRowStatusFail(paramSnmpMibSubRequest, 12);
/*      */     }
/*      */ 
/* 1505 */     if (localSnmpValue != null) {
/* 1506 */       SnmpVarBind localSnmpVarBind = paramSnmpMibSubRequest.getRowStatusVarBind();
/* 1507 */       if (localSnmpVarBind != null) localSnmpVarBind.value = localSnmpValue;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected long getNextVarEntryId(SnmpOid paramSnmpOid, long paramLong, Object paramObject, int paramInt)
/*      */     throws SnmpStatusException
/*      */   {
/* 1549 */     long l = paramLong;
/*      */     do
/* 1551 */       l = getNextVarEntryId(paramSnmpOid, l, paramObject);
/* 1552 */     while (skipEntryVariable(paramSnmpOid, l, paramObject, paramInt));
/*      */ 
/* 1554 */     return l;
/*      */   }
/*      */ 
/*      */   protected boolean skipEntryVariable(SnmpOid paramSnmpOid, long paramLong, Object paramObject, int paramInt)
/*      */   {
/* 1585 */     return false;
/*      */   }
/*      */ 
/*      */   protected SnmpOid getNextOid(SnmpOid paramSnmpOid, Object paramObject)
/*      */     throws SnmpStatusException
/*      */   {
/* 1609 */     if (this.size == 0) {
/* 1610 */       throw new SnmpStatusException(224);
/*      */     }
/* 1612 */     SnmpOid localSnmpOid1 = paramSnmpOid;
/*      */ 
/* 1617 */     SnmpOid localSnmpOid2 = this.tableoids[(this.tablecount - 1)];
/* 1618 */     if (localSnmpOid2.equals(localSnmpOid1))
/*      */     {
/* 1621 */       throw new SnmpStatusException(224);
/*      */     }
/*      */ 
/* 1633 */     int i = getInsertionPoint(localSnmpOid1, false);
/*      */ 
/* 1638 */     if ((i > -1) && (i < this.size)) {
/*      */       try
/*      */       {
/* 1641 */         localSnmpOid2 = this.tableoids[i];
/*      */       } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/* 1643 */         throw new SnmpStatusException(224);
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/* 1648 */       throw new SnmpStatusException(224);
/*      */     }
/*      */ 
/* 1652 */     return localSnmpOid2;
/*      */   }
/*      */ 
/*      */   protected SnmpOid getNextOid(Object paramObject)
/*      */     throws SnmpStatusException
/*      */   {
/* 1670 */     if (this.size == 0) {
/* 1671 */       throw new SnmpStatusException(224);
/*      */     }
/* 1673 */     return this.tableoids[0];
/*      */   }
/*      */ 
/*      */   protected abstract long getNextVarEntryId(SnmpOid paramSnmpOid, long paramLong, Object paramObject)
/*      */     throws SnmpStatusException;
/*      */ 
/*      */   protected abstract void validateVarEntryId(SnmpOid paramSnmpOid, long paramLong, Object paramObject)
/*      */     throws SnmpStatusException;
/*      */ 
/*      */   protected abstract boolean isReadableEntryId(SnmpOid paramSnmpOid, long paramLong, Object paramObject)
/*      */     throws SnmpStatusException;
/*      */ 
/*      */   protected abstract void get(SnmpMibSubRequest paramSnmpMibSubRequest, SnmpOid paramSnmpOid, int paramInt)
/*      */     throws SnmpStatusException;
/*      */ 
/*      */   protected abstract void check(SnmpMibSubRequest paramSnmpMibSubRequest, SnmpOid paramSnmpOid, int paramInt)
/*      */     throws SnmpStatusException;
/*      */ 
/*      */   protected abstract void set(SnmpMibSubRequest paramSnmpMibSubRequest, SnmpOid paramSnmpOid, int paramInt)
/*      */     throws SnmpStatusException;
/*      */ 
/*      */   SnmpOid getNextOid(long[] paramArrayOfLong, int paramInt, Object paramObject)
/*      */     throws SnmpStatusException
/*      */   {
/* 1805 */     SnmpEntryOid localSnmpEntryOid = new SnmpEntryOid(paramArrayOfLong, paramInt);
/*      */ 
/* 1807 */     return getNextOid(localSnmpEntryOid, paramObject);
/*      */   }
/*      */ 
/*      */   static final void checkRowStatusFail(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*      */     throws SnmpStatusException
/*      */   {
/* 1819 */     SnmpVarBind localSnmpVarBind = paramSnmpMibSubRequest.getRowStatusVarBind();
/* 1820 */     SnmpStatusException localSnmpStatusException = new SnmpStatusException(paramInt);
/* 1821 */     paramSnmpMibSubRequest.registerCheckException(localSnmpVarBind, localSnmpStatusException);
/*      */   }
/*      */ 
/*      */   static final void setRowStatusFail(SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*      */     throws SnmpStatusException
/*      */   {
/* 1833 */     SnmpVarBind localSnmpVarBind = paramSnmpMibSubRequest.getRowStatusVarBind();
/* 1834 */     SnmpStatusException localSnmpStatusException = new SnmpStatusException(paramInt);
/* 1835 */     paramSnmpMibSubRequest.registerSetException(localSnmpVarBind, localSnmpStatusException);
/*      */   }
/*      */ 
/*      */   final synchronized void findHandlingNode(SnmpVarBind paramSnmpVarBind, long[] paramArrayOfLong, int paramInt, SnmpRequestTree paramSnmpRequestTree)
/*      */     throws SnmpStatusException
/*      */   {
/* 1848 */     int i = paramArrayOfLong.length;
/*      */ 
/* 1850 */     if (paramSnmpRequestTree == null) {
/* 1851 */       throw new SnmpStatusException(5);
/*      */     }
/* 1853 */     if (paramInt >= i) {
/* 1854 */       throw new SnmpStatusException(6);
/*      */     }
/* 1856 */     if (paramArrayOfLong[paramInt] != this.nodeId) {
/* 1857 */       throw new SnmpStatusException(6);
/*      */     }
/* 1859 */     if (paramInt + 2 >= i) {
/* 1860 */       throw new SnmpStatusException(6);
/*      */     }
/*      */ 
/* 1866 */     SnmpEntryOid localSnmpEntryOid = new SnmpEntryOid(paramArrayOfLong, paramInt + 2);
/*      */ 
/* 1869 */     Object localObject = paramSnmpRequestTree.getUserData();
/* 1870 */     boolean bool = contains(localSnmpEntryOid, localObject);
/*      */ 
/* 1875 */     if (!bool) {
/* 1876 */       if (!paramSnmpRequestTree.isCreationAllowed())
/*      */       {
/* 1878 */         throw new SnmpStatusException(224);
/* 1879 */       }if (!isCreationEnabled())
/*      */       {
/* 1881 */         throw new SnmpStatusException(6);
/*      */       }
/*      */     }
/*      */ 
/* 1885 */     long l = paramArrayOfLong[(paramInt + 1)];
/*      */ 
/* 1888 */     if (bool)
/*      */     {
/* 1890 */       validateVarEntryId(localSnmpEntryOid, l, localObject);
/*      */     }
/*      */ 
/* 1895 */     if ((paramSnmpRequestTree.isSetRequest()) && (isRowStatus(localSnmpEntryOid, l, localObject)))
/*      */     {
/* 1899 */       paramSnmpRequestTree.add(this, paramInt, localSnmpEntryOid, paramSnmpVarBind, !bool, paramSnmpVarBind);
/*      */     }
/*      */     else
/* 1902 */       paramSnmpRequestTree.add(this, paramInt, localSnmpEntryOid, paramSnmpVarBind, !bool);
/*      */   }
/*      */ 
/*      */   final synchronized long[] findNextHandlingNode(SnmpVarBind paramSnmpVarBind, long[] paramArrayOfLong, int paramInt1, int paramInt2, SnmpRequestTree paramSnmpRequestTree, AcmChecker paramAcmChecker)
/*      */     throws SnmpStatusException
/*      */   {
/* 1917 */     int i = paramArrayOfLong.length;
/*      */ 
/* 1919 */     if (paramSnmpRequestTree == null)
/*      */     {
/* 1924 */       throw new SnmpStatusException(225);
/*      */     }
/* 1926 */     Object localObject = paramSnmpRequestTree.getUserData();
/* 1927 */     int j = paramSnmpRequestTree.getRequestPduVersion();
/*      */ 
/* 1929 */     long l = -1L;
/*      */ 
/* 1939 */     if (paramInt1 >= i)
/*      */     {
/* 1948 */       paramArrayOfLong = new long[1];
/* 1949 */       paramArrayOfLong[0] = this.nodeId;
/* 1950 */       paramInt1 = 0;
/* 1951 */       i = 1; } else {
/* 1952 */       if (paramArrayOfLong[paramInt1] > this.nodeId)
/*      */       {
/* 1958 */         throw new SnmpStatusException(225);
/* 1959 */       }if (paramArrayOfLong[paramInt1] < this.nodeId)
/*      */       {
/* 1965 */         paramArrayOfLong = new long[1];
/* 1966 */         paramArrayOfLong[0] = this.nodeId;
/* 1967 */         paramInt1 = 0;
/* 1968 */         i = 0;
/* 1969 */       } else if (paramInt1 + 1 < i)
/*      */       {
/* 1973 */         l = paramArrayOfLong[(paramInt1 + 1)];
/*      */       }
/*      */     }
/*      */ 
/* 1977 */     SnmpOid localSnmpOid = null;
/*      */ 
/* 1979 */     if (paramInt1 == i - 1)
/*      */     {
/* 1992 */       localSnmpOid = getNextOid(localObject);
/* 1993 */       l = getNextVarEntryId(localSnmpOid, l, localObject, j);
/* 1994 */     } else if (paramInt1 == i - 2)
/*      */     {
/* 2004 */       localSnmpOid = getNextOid(localObject);
/*      */ 
/* 2012 */       if (skipEntryVariable(localSnmpOid, l, localObject, j)) {
/* 2013 */         l = getNextVarEntryId(localSnmpOid, l, localObject, j);
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*      */       try
/*      */       {
/* 2038 */         localSnmpOid = getNextOid(paramArrayOfLong, paramInt1 + 2, localObject);
/*      */ 
/* 2048 */         if (skipEntryVariable(localSnmpOid, l, localObject, j))
/* 2049 */           throw new SnmpStatusException(225);
/*      */       } catch (SnmpStatusException localSnmpStatusException) {
/* 2051 */         localSnmpOid = getNextOid(localObject);
/* 2052 */         l = getNextVarEntryId(localSnmpOid, l, localObject, j);
/*      */       }
/*      */     }
/*      */ 
/* 2056 */     return findNextAccessibleOid(localSnmpOid, paramSnmpVarBind, paramArrayOfLong, paramInt2, paramSnmpRequestTree, paramAcmChecker, localObject, l);
/*      */   }
/*      */ 
/*      */   private long[] findNextAccessibleOid(SnmpOid paramSnmpOid, SnmpVarBind paramSnmpVarBind, long[] paramArrayOfLong, int paramInt, SnmpRequestTree paramSnmpRequestTree, AcmChecker paramAcmChecker, Object paramObject, long paramLong)
/*      */     throws SnmpStatusException
/*      */   {
/* 2072 */     int i = paramSnmpRequestTree.getRequestPduVersion();
/*      */     do
/*      */     {
/* 2082 */       if ((paramSnmpOid == null) || (paramLong == -1L)) throw new SnmpStatusException(225);
/*      */ 
/*      */       try
/*      */       {
/* 2094 */         if (!isReadableEntryId(paramSnmpOid, paramLong, paramObject)) {
/* 2095 */           throw new SnmpStatusException(225);
/*      */         }
/*      */ 
/* 2099 */         long[] arrayOfLong1 = paramSnmpOid.longValue(false);
/* 2100 */         int j = arrayOfLong1.length;
/* 2101 */         long[] arrayOfLong2 = new long[paramInt + 2 + j];
/* 2102 */         arrayOfLong2[0] = -1L;
/*      */ 
/* 2106 */         System.arraycopy(arrayOfLong1, 0, arrayOfLong2, paramInt + 2, j);
/*      */ 
/* 2111 */         arrayOfLong2[paramInt] = this.nodeId;
/* 2112 */         arrayOfLong2[(paramInt + 1)] = paramLong;
/*      */ 
/* 2116 */         paramAcmChecker.add(paramInt, arrayOfLong2, paramInt, j + 2);
/*      */         try
/*      */         {
/* 2120 */           paramAcmChecker.checkCurrentOid();
/*      */ 
/* 2126 */           paramSnmpRequestTree.add(this, paramInt, paramSnmpOid, paramSnmpVarBind, false);
/* 2127 */           return arrayOfLong2;
/*      */         }
/*      */         catch (SnmpStatusException localSnmpStatusException2)
/*      */         {
/* 2133 */           paramSnmpOid = getNextOid(paramSnmpOid, paramObject);
/*      */         }
/*      */         finally
/*      */         {
/* 2137 */           paramAcmChecker.remove(paramInt, j + 2);
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (SnmpStatusException localSnmpStatusException1)
/*      */       {
/* 2144 */         paramSnmpOid = getNextOid(paramObject);
/*      */ 
/* 2148 */         paramLong = getNextVarEntryId(paramSnmpOid, paramLong, paramObject, i);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2158 */     while ((paramSnmpOid != null) && (paramLong != -1L));
/* 2159 */     throw new SnmpStatusException(225);
/*      */   }
/*      */ 
/*      */   final void validateOid(long[] paramArrayOfLong, int paramInt)
/*      */     throws SnmpStatusException
/*      */   {
/* 2175 */     int i = paramArrayOfLong.length;
/*      */ 
/* 2179 */     if (paramInt + 2 >= i) {
/* 2180 */       throw new SnmpStatusException(224);
/*      */     }
/*      */ 
/* 2184 */     if (paramArrayOfLong[paramInt] != this.nodeId)
/* 2185 */       throw new SnmpStatusException(225);
/*      */   }
/*      */ 
/*      */   private synchronized void sendNotification(Notification paramNotification)
/*      */   {
/* 2203 */     Enumeration localEnumeration1 = this.handbackTable.keys();
/* 2204 */     while (localEnumeration1.hasMoreElements())
/*      */     {
/* 2206 */       NotificationListener localNotificationListener = (NotificationListener)localEnumeration1.nextElement();
/*      */ 
/* 2211 */       Vector localVector1 = (Vector)this.handbackTable.get(localNotificationListener);
/*      */ 
/* 2213 */       Vector localVector2 = (Vector)this.filterTable.get(localNotificationListener);
/*      */ 
/* 2218 */       Enumeration localEnumeration2 = localVector2.elements();
/* 2219 */       Enumeration localEnumeration3 = localVector1.elements();
/* 2220 */       while (localEnumeration3.hasMoreElements())
/*      */       {
/* 2222 */         Object localObject = localEnumeration3.nextElement();
/* 2223 */         NotificationFilter localNotificationFilter = (NotificationFilter)localEnumeration2.nextElement();
/*      */ 
/* 2226 */         if ((localNotificationFilter == null) || (localNotificationFilter.isNotificationEnabled(paramNotification)))
/*      */         {
/* 2229 */           localNotificationListener.handleNotification(paramNotification, localObject);
/*      */         }
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void sendNotification(String paramString, long paramLong, Object paramObject, ObjectName paramObjectName)
/*      */   {
/* 2250 */     synchronized (this) {
/* 2251 */       this.sequenceNumber += 1L;
/*      */     }
/*      */ 
/* 2254 */     ??? = new SnmpTableEntryNotification(paramString, this, this.sequenceNumber, paramLong, paramObject, paramObjectName);
/*      */ 
/* 2258 */     sendNotification((Notification)???);
/*      */   }
/*      */ 
/*      */   protected boolean contains(SnmpOid paramSnmpOid, Object paramObject)
/*      */   {
/* 2289 */     return findObject(paramSnmpOid) > -1;
/*      */   }
/*      */ 
/*      */   private final int findObject(SnmpOid paramSnmpOid)
/*      */   {
/* 2304 */     int i = 0;
/* 2305 */     int j = this.size - 1;
/*      */ 
/* 2308 */     int m = i + (j - i) / 2;
/*      */ 
/* 2310 */     while (i <= j)
/*      */     {
/* 2313 */       SnmpOid localSnmpOid = this.tableoids[m];
/*      */ 
/* 2318 */       int k = paramSnmpOid.compareTo(localSnmpOid);
/* 2319 */       if (k == 0) {
/* 2320 */         return m;
/*      */       }
/* 2322 */       if (paramSnmpOid.equals(localSnmpOid) == true) {
/* 2323 */         return m;
/*      */       }
/* 2325 */       if (k > 0)
/* 2326 */         i = m + 1;
/*      */       else {
/* 2328 */         j = m - 1;
/*      */       }
/* 2330 */       m = i + (j - i) / 2;
/*      */     }
/* 2332 */     return -1;
/*      */   }
/*      */ 
/*      */   private final int getInsertionPoint(SnmpOid paramSnmpOid)
/*      */     throws SnmpStatusException
/*      */   {
/* 2351 */     return getInsertionPoint(paramSnmpOid, true);
/*      */   }
/*      */ 
/*      */   private final int getInsertionPoint(SnmpOid paramSnmpOid, boolean paramBoolean)
/*      */     throws SnmpStatusException
/*      */   {
/* 2378 */     int i = 0;
/* 2379 */     int j = this.size - 1;
/*      */ 
/* 2382 */     int m = i + (j - i) / 2;
/* 2383 */     while (i <= j)
/*      */     {
/* 2386 */       SnmpOid localSnmpOid = this.tableoids[m];
/*      */ 
/* 2390 */       int k = paramSnmpOid.compareTo(localSnmpOid);
/*      */ 
/* 2392 */       if (k == 0) {
/* 2393 */         if (paramBoolean) {
/* 2394 */           throw new SnmpStatusException(17, m);
/*      */         }
/* 2396 */         return m + 1;
/*      */       }
/*      */ 
/* 2399 */       if (k > 0)
/* 2400 */         i = m + 1;
/*      */       else {
/* 2402 */         j = m - 1;
/*      */       }
/* 2404 */       m = i + (j - i) / 2;
/*      */     }
/* 2406 */     return m;
/*      */   }
/*      */ 
/*      */   private final void removeOid(int paramInt)
/*      */   {
/* 2417 */     if (paramInt >= this.tablecount) return;
/* 2418 */     if (paramInt < 0) return;
/* 2419 */     int i = --this.tablecount - paramInt;
/* 2420 */     this.tableoids[paramInt] = null;
/* 2421 */     if (i > 0)
/* 2422 */       System.arraycopy(this.tableoids, paramInt + 1, this.tableoids, paramInt, i);
/* 2423 */     this.tableoids[this.tablecount] = null;
/*      */   }
/*      */ 
/*      */   private final void insertOid(int paramInt, SnmpOid paramSnmpOid)
/*      */   {
/* 2435 */     if ((paramInt >= this.tablesize) || (this.tablecount == this.tablesize))
/*      */     {
/* 2439 */       SnmpOid[] arrayOfSnmpOid = this.tableoids;
/*      */ 
/* 2442 */       this.tablesize += 16;
/* 2443 */       this.tableoids = new SnmpOid[this.tablesize];
/*      */ 
/* 2446 */       if (paramInt > this.tablecount) paramInt = this.tablecount;
/* 2447 */       if (paramInt < 0) paramInt = 0;
/*      */ 
/* 2449 */       int i = paramInt;
/* 2450 */       int j = this.tablecount - paramInt;
/*      */ 
/* 2453 */       if (i > 0) {
/* 2454 */         System.arraycopy(arrayOfSnmpOid, 0, this.tableoids, 0, i);
/*      */       }
/*      */ 
/* 2458 */       if (j > 0) {
/* 2459 */         System.arraycopy(arrayOfSnmpOid, i, this.tableoids, i + 1, j);
/*      */       }
/*      */     }
/* 2462 */     else if (paramInt < this.tablecount)
/*      */     {
/* 2468 */       System.arraycopy(this.tableoids, paramInt, this.tableoids, paramInt + 1, this.tablecount - paramInt);
/*      */     }
/*      */ 
/* 2473 */     this.tableoids[paramInt] = paramSnmpOid;
/* 2474 */     this.tablecount += 1;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpMibTable
 * JD-Core Version:    0.6.2
 */