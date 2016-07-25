/*      */ package com.sun.jmx.snmp.agent;
/*      */ 
/*      */ import com.sun.jmx.defaults.JmxProperties;
/*      */ import com.sun.jmx.snmp.SnmpOid;
/*      */ import com.sun.jmx.snmp.SnmpStatusException;
/*      */ import com.sun.jmx.snmp.SnmpVarBind;
/*      */ import java.io.Serializable;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Vector;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.management.InstanceAlreadyExistsException;
/*      */ import javax.management.MBeanRegistrationException;
/*      */ import javax.management.MBeanServer;
/*      */ import javax.management.MalformedObjectNameException;
/*      */ import javax.management.NotCompliantMBeanException;
/*      */ import javax.management.ObjectName;
/*      */ 
/*      */ public abstract class SnmpMib extends SnmpMibAgent
/*      */   implements Serializable
/*      */ {
/*      */   protected SnmpMibOid root;
/* 1025 */   private transient long[] rootOid = null;
/*      */ 
/*      */   public SnmpMib()
/*      */   {
/*  363 */     this.root = new SnmpMibOid();
/*      */   }
/*      */ 
/*      */   protected String getGroupOid(String paramString1, String paramString2)
/*      */   {
/*  399 */     return paramString2;
/*      */   }
/*      */ 
/*      */   protected ObjectName getGroupObjectName(String paramString1, String paramString2, String paramString3)
/*      */     throws MalformedObjectNameException
/*      */   {
/*  435 */     return new ObjectName(paramString3);
/*      */   }
/*      */ 
/*      */   protected void registerGroupNode(String paramString1, String paramString2, ObjectName paramObjectName, SnmpMibNode paramSnmpMibNode, Object paramObject, MBeanServer paramMBeanServer)
/*      */     throws NotCompliantMBeanException, MBeanRegistrationException, InstanceAlreadyExistsException, IllegalAccessException
/*      */   {
/*  482 */     this.root.registerNode(paramString2, paramSnmpMibNode);
/*  483 */     if ((paramMBeanServer != null) && (paramObjectName != null) && (paramObject != null))
/*  484 */       paramMBeanServer.registerMBean(paramObject, paramObjectName);
/*      */   }
/*      */ 
/*      */   public abstract void registerTableMeta(String paramString, SnmpMibTable paramSnmpMibTable);
/*      */ 
/*      */   public abstract SnmpMibTable getRegisteredTableMeta(String paramString);
/*      */ 
/*      */   public void get(SnmpMibRequest paramSnmpMibRequest)
/*      */     throws SnmpStatusException
/*      */   {
/*  540 */     SnmpRequestTree localSnmpRequestTree = getHandlers(paramSnmpMibRequest, false, false, 160);
/*      */ 
/*  542 */     SnmpRequestTree.Handler localHandler = null;
/*  543 */     SnmpMibNode localSnmpMibNode = null;
/*      */ 
/*  545 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  546 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(), "get", "Processing handlers for GET... ");
/*      */     }
/*      */ 
/*  552 */     for (Enumeration localEnumeration1 = localSnmpRequestTree.getHandlers(); localEnumeration1.hasMoreElements(); ) {
/*  553 */       localHandler = (SnmpRequestTree.Handler)localEnumeration1.nextElement();
/*      */ 
/*  558 */       localSnmpMibNode = localSnmpRequestTree.getMetaNode(localHandler);
/*      */ 
/*  561 */       int i = localSnmpRequestTree.getOidDepth(localHandler);
/*      */ 
/*  563 */       Enumeration localEnumeration2 = localSnmpRequestTree.getSubRequests(localHandler);
/*  564 */       while (localEnumeration2.hasMoreElements())
/*      */       {
/*  567 */         localSnmpMibNode.get((SnmpMibSubRequest)localEnumeration2.nextElement(), i);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void set(SnmpMibRequest paramSnmpMibRequest)
/*      */     throws SnmpStatusException
/*      */   {
/*  581 */     SnmpRequestTree localSnmpRequestTree = null;
/*      */ 
/*  587 */     if ((paramSnmpMibRequest instanceof SnmpMibRequestImpl)) {
/*  588 */       localSnmpRequestTree = ((SnmpMibRequestImpl)paramSnmpMibRequest).getRequestTree();
/*      */     }
/*      */ 
/*  596 */     if (localSnmpRequestTree == null) localSnmpRequestTree = getHandlers(paramSnmpMibRequest, false, true, 163);
/*  597 */     localSnmpRequestTree.switchCreationFlag(false);
/*  598 */     localSnmpRequestTree.setPduType(163);
/*      */ 
/*  600 */     SnmpRequestTree.Handler localHandler = null;
/*  601 */     SnmpMibNode localSnmpMibNode = null;
/*      */ 
/*  603 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  604 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(), "set", "Processing handlers for SET... ");
/*      */     }
/*      */ 
/*  610 */     for (Enumeration localEnumeration1 = localSnmpRequestTree.getHandlers(); localEnumeration1.hasMoreElements(); ) {
/*  611 */       localHandler = (SnmpRequestTree.Handler)localEnumeration1.nextElement();
/*      */ 
/*  616 */       localSnmpMibNode = localSnmpRequestTree.getMetaNode(localHandler);
/*      */ 
/*  619 */       int i = localSnmpRequestTree.getOidDepth(localHandler);
/*      */ 
/*  621 */       Enumeration localEnumeration2 = localSnmpRequestTree.getSubRequests(localHandler);
/*  622 */       while (localEnumeration2.hasMoreElements())
/*      */       {
/*  625 */         localSnmpMibNode.set((SnmpMibSubRequest)localEnumeration2.nextElement(), i);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void check(SnmpMibRequest paramSnmpMibRequest)
/*      */     throws SnmpStatusException
/*      */   {
/*  644 */     SnmpRequestTree localSnmpRequestTree = getHandlers(paramSnmpMibRequest, true, true, 253);
/*      */ 
/*  646 */     SnmpRequestTree.Handler localHandler = null;
/*  647 */     SnmpMibNode localSnmpMibNode = null;
/*      */ 
/*  649 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  650 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(), "check", "Processing handlers for CHECK... ");
/*      */     }
/*      */ 
/*  656 */     for (Enumeration localEnumeration1 = localSnmpRequestTree.getHandlers(); localEnumeration1.hasMoreElements(); ) {
/*  657 */       localHandler = (SnmpRequestTree.Handler)localEnumeration1.nextElement();
/*      */ 
/*  662 */       localSnmpMibNode = localSnmpRequestTree.getMetaNode(localHandler);
/*      */ 
/*  665 */       int i = localSnmpRequestTree.getOidDepth(localHandler);
/*      */ 
/*  667 */       Enumeration localEnumeration2 = localSnmpRequestTree.getSubRequests(localHandler);
/*  668 */       while (localEnumeration2.hasMoreElements())
/*      */       {
/*  671 */         localSnmpMibNode.check((SnmpMibSubRequest)localEnumeration2.nextElement(), i);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  678 */     if ((paramSnmpMibRequest instanceof SnmpMibRequestImpl))
/*  679 */       ((SnmpMibRequestImpl)paramSnmpMibRequest).setRequestTree(localSnmpRequestTree);
/*      */   }
/*      */ 
/*      */   public void getNext(SnmpMibRequest paramSnmpMibRequest)
/*      */     throws SnmpStatusException
/*      */   {
/*  694 */     SnmpRequestTree localSnmpRequestTree = getGetNextHandlers(paramSnmpMibRequest);
/*      */ 
/*  696 */     SnmpRequestTree.Handler localHandler = null;
/*  697 */     SnmpMibNode localSnmpMibNode = null;
/*      */ 
/*  699 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  700 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(), "getNext", "Processing handlers for GET-NEXT... ");
/*      */     }
/*      */ 
/*  705 */     for (Enumeration localEnumeration1 = localSnmpRequestTree.getHandlers(); localEnumeration1.hasMoreElements(); ) {
/*  706 */       localHandler = (SnmpRequestTree.Handler)localEnumeration1.nextElement();
/*      */ 
/*  711 */       localSnmpMibNode = localSnmpRequestTree.getMetaNode(localHandler);
/*      */ 
/*  714 */       int i = localSnmpRequestTree.getOidDepth(localHandler);
/*      */ 
/*  716 */       Enumeration localEnumeration2 = localSnmpRequestTree.getSubRequests(localHandler);
/*  717 */       while (localEnumeration2.hasMoreElements())
/*      */       {
/*  720 */         localSnmpMibNode.get((SnmpMibSubRequest)localEnumeration2.nextElement(), i);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void getBulk(SnmpMibRequest paramSnmpMibRequest, int paramInt1, int paramInt2)
/*      */     throws SnmpStatusException
/*      */   {
/*  738 */     getBulkWithGetNext(paramSnmpMibRequest, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public long[] getRootOid()
/*      */   {
/*      */     int i;
/*      */     Enumeration localEnumeration;
/*  751 */     if (this.rootOid == null) {
/*  752 */       Vector localVector = new Vector(10);
/*      */ 
/*  756 */       this.root.getRootOid(localVector);
/*      */ 
/*  760 */       this.rootOid = new long[localVector.size()];
/*  761 */       i = 0;
/*  762 */       for (localEnumeration = localVector.elements(); localEnumeration.hasMoreElements(); ) {
/*  763 */         Integer localInteger = (Integer)localEnumeration.nextElement();
/*  764 */         this.rootOid[(i++)] = localInteger.longValue();
/*      */       }
/*      */     }
/*  767 */     return this.rootOid;
/*      */   }
/*      */ 
/*      */   private SnmpRequestTree getHandlers(SnmpMibRequest paramSnmpMibRequest, boolean paramBoolean1, boolean paramBoolean2, int paramInt)
/*      */     throws SnmpStatusException
/*      */   {
/*  796 */     SnmpRequestTree localSnmpRequestTree = new SnmpRequestTree(paramSnmpMibRequest, paramBoolean1, paramInt);
/*      */ 
/*  799 */     int i = 0;
/*  800 */     SnmpVarBind localSnmpVarBind = null;
/*  801 */     int j = paramSnmpMibRequest.getVersion();
/*      */ 
/*  804 */     for (Enumeration localEnumeration = paramSnmpMibRequest.getElements(); localEnumeration.hasMoreElements(); i++)
/*      */     {
/*  806 */       localSnmpVarBind = (SnmpVarBind)localEnumeration.nextElement();
/*      */       try
/*      */       {
/*  810 */         this.root.findHandlingNode(localSnmpVarBind, localSnmpVarBind.oid.longValue(false), 0, localSnmpRequestTree);
/*      */       }
/*      */       catch (SnmpStatusException localSnmpStatusException1)
/*      */       {
/*  814 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  815 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(), "getHandlers", "Couldn't find a handling node for " + localSnmpVarBind.oid.toString());
/*      */         }
/*      */ 
/*  825 */         if (j == 0)
/*      */         {
/*  827 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  828 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(), "getHandlers", "\tV1: Throwing exception");
/*      */           }
/*      */ 
/*  836 */           SnmpStatusException localSnmpStatusException2 = new SnmpStatusException(localSnmpStatusException1, i + 1);
/*      */ 
/*  838 */           localSnmpStatusException2.initCause(localSnmpStatusException1);
/*  839 */           throw localSnmpStatusException2;
/*      */         }
/*      */         SnmpStatusException localSnmpStatusException4;
/*  840 */         if ((paramInt == 253) || (paramInt == 163))
/*      */         {
/*  842 */           int k = SnmpRequestTree.mapSetException(localSnmpStatusException1.getStatus(), j);
/*      */ 
/*  845 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  846 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(), "getHandlers", "\tSET: Throwing exception");
/*      */           }
/*      */ 
/*  851 */           localSnmpStatusException4 = new SnmpStatusException(k, i + 1);
/*      */ 
/*  853 */           localSnmpStatusException4.initCause(localSnmpStatusException1);
/*  854 */           throw localSnmpStatusException4;
/*  855 */         }if (paramBoolean2)
/*      */         {
/*  858 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  859 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(), "getHandlers", "\tATOMIC: Throwing exception");
/*      */           }
/*      */ 
/*  864 */           SnmpStatusException localSnmpStatusException3 = new SnmpStatusException(localSnmpStatusException1, i + 1);
/*      */ 
/*  866 */           localSnmpStatusException3.initCause(localSnmpStatusException1);
/*  867 */           throw localSnmpStatusException3;
/*      */         }
/*      */ 
/*  870 */         int m = SnmpRequestTree.mapGetException(localSnmpStatusException1.getStatus(), j);
/*      */ 
/*  873 */         if (m == 224)
/*      */         {
/*  875 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  876 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(), "getHandlers", "\tGET: Registering noSuchInstance");
/*      */           }
/*      */ 
/*  882 */           localSnmpVarBind.value = SnmpVarBind.noSuchInstance;
/*      */         }
/*  884 */         else if (m == 225) {
/*  885 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  886 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(), "getHandlers", "\tGET: Registering noSuchObject");
/*      */           }
/*      */ 
/*  892 */           localSnmpVarBind.value = SnmpVarBind.noSuchObject;
/*      */         }
/*      */         else
/*      */         {
/*  896 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  897 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(), "getHandlers", "\tGET: Registering global error: " + m);
/*      */           }
/*      */ 
/*  903 */           localSnmpStatusException4 = new SnmpStatusException(m, i + 1);
/*      */ 
/*  905 */           localSnmpStatusException4.initCause(localSnmpStatusException1);
/*  906 */           throw localSnmpStatusException4;
/*      */         }
/*      */       }
/*      */     }
/*  910 */     return localSnmpRequestTree;
/*      */   }
/*      */ 
/*      */   private SnmpRequestTree getGetNextHandlers(SnmpMibRequest paramSnmpMibRequest)
/*      */     throws SnmpStatusException
/*      */   {
/*  929 */     SnmpRequestTree localSnmpRequestTree = new SnmpRequestTree(paramSnmpMibRequest, false, 161);
/*      */ 
/*  934 */     localSnmpRequestTree.setGetNextFlag();
/*      */ 
/*  936 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  937 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(), "getGetNextHandlers", "Received MIB request : " + paramSnmpMibRequest);
/*      */     }
/*      */ 
/*  940 */     AcmChecker localAcmChecker = new AcmChecker(paramSnmpMibRequest);
/*  941 */     int i = 0;
/*  942 */     SnmpVarBind localSnmpVarBind = null;
/*  943 */     int j = paramSnmpMibRequest.getVersion();
/*  944 */     Object localObject = null;
/*      */ 
/*  950 */     for (Enumeration localEnumeration = paramSnmpMibRequest.getElements(); localEnumeration.hasMoreElements(); i++)
/*      */     {
/*  952 */       localSnmpVarBind = (SnmpVarBind)localEnumeration.nextElement();
/*  953 */       SnmpOid localSnmpOid = null;
/*      */       try
/*      */       {
/*  958 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  959 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(), "getGetNextHandlers", " Next OID of : " + localSnmpVarBind.oid);
/*      */         }
/*      */ 
/*  963 */         localSnmpOid = new SnmpOid(this.root.findNextHandlingNode(localSnmpVarBind, localSnmpVarBind.oid.longValue(false), 0, 0, localSnmpRequestTree, localAcmChecker));
/*      */ 
/*  967 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  968 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(), "getGetNextHandlers", " is : " + localSnmpOid);
/*      */         }
/*      */ 
/*  974 */         localSnmpVarBind.oid = localSnmpOid;
/*      */       }
/*      */       catch (SnmpStatusException localSnmpStatusException)
/*      */       {
/*  982 */         if (j == 0) {
/*  983 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  984 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(), "getGetNextHandlers", "\tThrowing exception " + localSnmpStatusException.toString());
/*      */           }
/*      */ 
/*  992 */           throw new SnmpStatusException(localSnmpStatusException, i + 1);
/*      */         }
/*  994 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  995 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpMib.class.getName(), "getGetNextHandlers", "Exception : " + localSnmpStatusException.getStatus());
/*      */         }
/*      */ 
/* 1001 */         localSnmpVarBind.setSnmpValue(SnmpVarBind.endOfMibView);
/*      */       }
/*      */     }
/* 1004 */     return localSnmpRequestTree;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpMib
 * JD-Core Version:    0.6.2
 */