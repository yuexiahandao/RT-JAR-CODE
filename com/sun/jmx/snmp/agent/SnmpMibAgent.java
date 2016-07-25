/*     */ package com.sun.jmx.snmp.agent;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpEngine;
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import com.sun.jmx.snmp.SnmpPdu;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.SnmpVarBind;
/*     */ import java.io.Serializable;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MBeanRegistration;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.ReflectionException;
/*     */ import javax.management.ServiceNotFoundException;
/*     */ 
/*     */ public abstract class SnmpMibAgent
/*     */   implements SnmpMibAgentMBean, MBeanRegistration, Serializable
/*     */ {
/*     */   protected String mibName;
/*     */   protected MBeanServer server;
/*     */   private ObjectName adaptorName;
/*     */   private transient SnmpMibHandler adaptor;
/*     */ 
/*     */   public abstract void init()
/*     */     throws IllegalAccessException;
/*     */ 
/*     */   public abstract ObjectName preRegister(MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*     */     throws Exception;
/*     */ 
/*     */   public void postRegister(Boolean paramBoolean)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void preDeregister()
/*     */     throws Exception
/*     */   {
/*     */   }
/*     */ 
/*     */   public void postDeregister()
/*     */   {
/*     */   }
/*     */ 
/*     */   public abstract void get(SnmpMibRequest paramSnmpMibRequest)
/*     */     throws SnmpStatusException;
/*     */ 
/*     */   public abstract void getNext(SnmpMibRequest paramSnmpMibRequest)
/*     */     throws SnmpStatusException;
/*     */ 
/*     */   public abstract void getBulk(SnmpMibRequest paramSnmpMibRequest, int paramInt1, int paramInt2)
/*     */     throws SnmpStatusException;
/*     */ 
/*     */   public abstract void set(SnmpMibRequest paramSnmpMibRequest)
/*     */     throws SnmpStatusException;
/*     */ 
/*     */   public abstract void check(SnmpMibRequest paramSnmpMibRequest)
/*     */     throws SnmpStatusException;
/*     */ 
/*     */   public abstract long[] getRootOid();
/*     */ 
/*     */   public MBeanServer getMBeanServer()
/*     */   {
/* 230 */     return this.server;
/*     */   }
/*     */ 
/*     */   public SnmpMibHandler getSnmpAdaptor()
/*     */   {
/* 240 */     return this.adaptor;
/*     */   }
/*     */ 
/*     */   public void setSnmpAdaptor(SnmpMibHandler paramSnmpMibHandler)
/*     */   {
/* 250 */     if (this.adaptor != null) {
/* 251 */       this.adaptor.removeMib(this);
/*     */     }
/* 253 */     this.adaptor = paramSnmpMibHandler;
/* 254 */     if (this.adaptor != null)
/* 255 */       this.adaptor.addMib(this);
/*     */   }
/*     */ 
/*     */   public void setSnmpAdaptor(SnmpMibHandler paramSnmpMibHandler, SnmpOid[] paramArrayOfSnmpOid)
/*     */   {
/* 270 */     if (this.adaptor != null) {
/* 271 */       this.adaptor.removeMib(this);
/*     */     }
/* 273 */     this.adaptor = paramSnmpMibHandler;
/* 274 */     if (this.adaptor != null)
/* 275 */       this.adaptor.addMib(this, paramArrayOfSnmpOid);
/*     */   }
/*     */ 
/*     */   public void setSnmpAdaptor(SnmpMibHandler paramSnmpMibHandler, String paramString)
/*     */   {
/* 292 */     if (this.adaptor != null) {
/* 293 */       this.adaptor.removeMib(this, paramString);
/*     */     }
/* 295 */     this.adaptor = paramSnmpMibHandler;
/* 296 */     if (this.adaptor != null)
/* 297 */       this.adaptor.addMib(this, paramString);
/*     */   }
/*     */ 
/*     */   public void setSnmpAdaptor(SnmpMibHandler paramSnmpMibHandler, String paramString, SnmpOid[] paramArrayOfSnmpOid)
/*     */   {
/* 315 */     if (this.adaptor != null) {
/* 316 */       this.adaptor.removeMib(this, paramString);
/*     */     }
/* 318 */     this.adaptor = paramSnmpMibHandler;
/* 319 */     if (this.adaptor != null)
/* 320 */       this.adaptor.addMib(this, paramString, paramArrayOfSnmpOid);
/*     */   }
/*     */ 
/*     */   public ObjectName getSnmpAdaptorName()
/*     */   {
/* 331 */     return this.adaptorName;
/*     */   }
/*     */ 
/*     */   public void setSnmpAdaptorName(ObjectName paramObjectName)
/*     */     throws InstanceNotFoundException, ServiceNotFoundException
/*     */   {
/* 350 */     if (this.server == null) {
/* 351 */       throw new ServiceNotFoundException(this.mibName + " is not registered in the MBean server");
/*     */     }
/*     */ 
/* 355 */     if (this.adaptor != null) {
/* 356 */       this.adaptor.removeMib(this);
/*     */     }
/*     */ 
/* 361 */     Object[] arrayOfObject = { this };
/* 362 */     String[] arrayOfString = { "com.sun.jmx.snmp.agent.SnmpMibAgent" };
/*     */     try {
/* 364 */       this.adaptor = ((SnmpMibHandler)this.server.invoke(paramObjectName, "addMib", arrayOfObject, arrayOfString));
/*     */     }
/*     */     catch (InstanceNotFoundException localInstanceNotFoundException) {
/* 367 */       throw new InstanceNotFoundException(paramObjectName.toString());
/*     */     } catch (ReflectionException localReflectionException) {
/* 369 */       throw new ServiceNotFoundException(paramObjectName.toString());
/*     */     }
/*     */     catch (MBeanException localMBeanException)
/*     */     {
/*     */     }
/* 374 */     this.adaptorName = paramObjectName;
/*     */   }
/*     */ 
/*     */   public void setSnmpAdaptorName(ObjectName paramObjectName, SnmpOid[] paramArrayOfSnmpOid)
/*     */     throws InstanceNotFoundException, ServiceNotFoundException
/*     */   {
/* 395 */     if (this.server == null) {
/* 396 */       throw new ServiceNotFoundException(this.mibName + " is not registered in the MBean server");
/*     */     }
/*     */ 
/* 400 */     if (this.adaptor != null) {
/* 401 */       this.adaptor.removeMib(this);
/*     */     }
/*     */ 
/* 406 */     Object[] arrayOfObject = { this, paramArrayOfSnmpOid };
/* 407 */     String[] arrayOfString = { "com.sun.jmx.snmp.agent.SnmpMibAgent", paramArrayOfSnmpOid.getClass().getName() };
/*     */     try
/*     */     {
/* 410 */       this.adaptor = ((SnmpMibHandler)this.server.invoke(paramObjectName, "addMib", arrayOfObject, arrayOfString));
/*     */     }
/*     */     catch (InstanceNotFoundException localInstanceNotFoundException) {
/* 413 */       throw new InstanceNotFoundException(paramObjectName.toString());
/*     */     } catch (ReflectionException localReflectionException) {
/* 415 */       throw new ServiceNotFoundException(paramObjectName.toString());
/*     */     }
/*     */     catch (MBeanException localMBeanException)
/*     */     {
/*     */     }
/* 420 */     this.adaptorName = paramObjectName;
/*     */   }
/*     */ 
/*     */   public void setSnmpAdaptorName(ObjectName paramObjectName, String paramString)
/*     */     throws InstanceNotFoundException, ServiceNotFoundException
/*     */   {
/* 440 */     if (this.server == null) {
/* 441 */       throw new ServiceNotFoundException(this.mibName + " is not registered in the MBean server");
/*     */     }
/*     */ 
/* 446 */     if (this.adaptor != null) {
/* 447 */       this.adaptor.removeMib(this, paramString);
/*     */     }
/*     */ 
/* 452 */     Object[] arrayOfObject = { this, paramString };
/* 453 */     String[] arrayOfString = { "com.sun.jmx.snmp.agent.SnmpMibAgent", "java.lang.String" };
/*     */     try {
/* 455 */       this.adaptor = ((SnmpMibHandler)this.server.invoke(paramObjectName, "addMib", arrayOfObject, arrayOfString));
/*     */     }
/*     */     catch (InstanceNotFoundException localInstanceNotFoundException) {
/* 458 */       throw new InstanceNotFoundException(paramObjectName.toString());
/*     */     } catch (ReflectionException localReflectionException) {
/* 460 */       throw new ServiceNotFoundException(paramObjectName.toString());
/*     */     }
/*     */     catch (MBeanException localMBeanException)
/*     */     {
/*     */     }
/* 465 */     this.adaptorName = paramObjectName;
/*     */   }
/*     */ 
/*     */   public void setSnmpAdaptorName(ObjectName paramObjectName, String paramString, SnmpOid[] paramArrayOfSnmpOid)
/*     */     throws InstanceNotFoundException, ServiceNotFoundException
/*     */   {
/* 488 */     if (this.server == null) {
/* 489 */       throw new ServiceNotFoundException(this.mibName + " is not registered in the MBean server");
/*     */     }
/*     */ 
/* 494 */     if (this.adaptor != null) {
/* 495 */       this.adaptor.removeMib(this, paramString);
/*     */     }
/*     */ 
/* 500 */     Object[] arrayOfObject = { this, paramString, paramArrayOfSnmpOid };
/* 501 */     String[] arrayOfString = { "com.sun.jmx.snmp.agent.SnmpMibAgent", "java.lang.String", paramArrayOfSnmpOid.getClass().getName() };
/*     */     try {
/* 503 */       this.adaptor = ((SnmpMibHandler)this.server.invoke(paramObjectName, "addMib", arrayOfObject, arrayOfString));
/*     */     }
/*     */     catch (InstanceNotFoundException localInstanceNotFoundException) {
/* 506 */       throw new InstanceNotFoundException(paramObjectName.toString());
/*     */     } catch (ReflectionException localReflectionException) {
/* 508 */       throw new ServiceNotFoundException(paramObjectName.toString());
/*     */     }
/*     */     catch (MBeanException localMBeanException)
/*     */     {
/*     */     }
/* 513 */     this.adaptorName = paramObjectName;
/*     */   }
/*     */ 
/*     */   public boolean getBindingState()
/*     */   {
/* 526 */     if (this.adaptor == null) {
/* 527 */       return false;
/*     */     }
/* 529 */     return true;
/*     */   }
/*     */ 
/*     */   public String getMibName()
/*     */   {
/* 538 */     return this.mibName;
/*     */   }
/*     */ 
/*     */   public static SnmpMibRequest newMibRequest(SnmpPdu paramSnmpPdu, Vector<SnmpVarBind> paramVector, int paramInt, Object paramObject)
/*     */   {
/* 558 */     return new SnmpMibRequestImpl(null, paramSnmpPdu, paramVector, paramInt, paramObject, null, 0, getSecurityModel(paramInt), null, null);
/*     */   }
/*     */ 
/*     */   public static SnmpMibRequest newMibRequest(SnmpEngine paramSnmpEngine, SnmpPdu paramSnmpPdu, Vector<SnmpVarBind> paramVector, int paramInt1, Object paramObject, String paramString, int paramInt2, int paramInt3, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*     */   {
/* 591 */     return new SnmpMibRequestImpl(paramSnmpEngine, paramSnmpPdu, paramVector, paramInt1, paramObject, paramString, paramInt2, paramInt3, paramArrayOfByte1, paramArrayOfByte2);
/*     */   }
/*     */ 
/*     */   void getBulkWithGetNext(SnmpMibRequest paramSnmpMibRequest, int paramInt1, int paramInt2)
/*     */     throws SnmpStatusException
/*     */   {
/* 630 */     Vector localVector1 = paramSnmpMibRequest.getSubList();
/*     */ 
/* 633 */     int i = localVector1.size();
/* 634 */     int j = Math.max(Math.min(paramInt1, i), 0);
/* 635 */     int k = Math.max(paramInt2, 0);
/* 636 */     int m = i - j;
/*     */ 
/* 642 */     if (i != 0)
/*     */     {
/* 646 */       getNext(paramSnmpMibRequest);
/*     */ 
/* 650 */       Vector localVector2 = splitFrom(localVector1, j);
/* 651 */       SnmpMibRequestImpl localSnmpMibRequestImpl = new SnmpMibRequestImpl(paramSnmpMibRequest.getEngine(), paramSnmpMibRequest.getPdu(), localVector2, 1, paramSnmpMibRequest.getUserData(), paramSnmpMibRequest.getPrincipal(), paramSnmpMibRequest.getSecurityLevel(), paramSnmpMibRequest.getSecurityModel(), paramSnmpMibRequest.getContextName(), paramSnmpMibRequest.getAccessContextName());
/*     */ 
/* 662 */       for (int n = 2; n <= k; n++) {
/* 663 */         getNext(localSnmpMibRequestImpl);
/* 664 */         concatVector(paramSnmpMibRequest, localVector2);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private Vector<SnmpVarBind> splitFrom(Vector<SnmpVarBind> paramVector, int paramInt)
/*     */   {
/* 683 */     int i = paramVector.size();
/* 684 */     Vector localVector = new Vector(i - paramInt);
/* 685 */     int j = paramInt;
/*     */ 
/* 691 */     for (Enumeration localEnumeration = paramVector.elements(); localEnumeration.hasMoreElements(); j--) {
/* 692 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)localEnumeration.nextElement();
/* 693 */       if (j <= 0)
/*     */       {
/* 695 */         localVector.addElement(new SnmpVarBind(localSnmpVarBind.oid, localSnmpVarBind.value));
/*     */       }
/*     */     }
/* 697 */     return localVector;
/*     */   }
/*     */ 
/*     */   private void concatVector(SnmpMibRequest paramSnmpMibRequest, Vector paramVector) {
/* 701 */     for (Enumeration localEnumeration = paramVector.elements(); localEnumeration.hasMoreElements(); ) {
/* 702 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)localEnumeration.nextElement();
/*     */ 
/* 705 */       paramSnmpMibRequest.addVarBind(new SnmpVarBind(localSnmpVarBind.oid, localSnmpVarBind.value));
/*     */     }
/*     */   }
/*     */ 
/*     */   private void concatVector(Vector<SnmpVarBind> paramVector1, Vector<SnmpVarBind> paramVector2) {
/* 710 */     for (Enumeration localEnumeration = paramVector2.elements(); localEnumeration.hasMoreElements(); ) {
/* 711 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)localEnumeration.nextElement();
/*     */ 
/* 714 */       paramVector1.addElement(new SnmpVarBind(localSnmpVarBind.oid, localSnmpVarBind.value));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int getSecurityModel(int paramInt) {
/* 719 */     switch (paramInt) {
/*     */     case 0:
/* 721 */       return 1;
/*     */     }
/* 723 */     return 2;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpMibAgent
 * JD-Core Version:    0.6.2
 */