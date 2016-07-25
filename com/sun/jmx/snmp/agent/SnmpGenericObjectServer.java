/*     */ package com.sun.jmx.snmp.agent;
/*     */ 
/*     */ import com.sun.jmx.snmp.SnmpOid;
/*     */ import com.sun.jmx.snmp.SnmpStatusException;
/*     */ import com.sun.jmx.snmp.SnmpValue;
/*     */ import com.sun.jmx.snmp.SnmpVarBind;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Iterator;
/*     */ import javax.management.Attribute;
/*     */ import javax.management.AttributeList;
/*     */ import javax.management.InstanceAlreadyExistsException;
/*     */ import javax.management.InstanceNotFoundException;
/*     */ import javax.management.InvalidAttributeValueException;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MBeanRegistrationException;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.NotCompliantMBeanException;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.ReflectionException;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ 
/*     */ public class SnmpGenericObjectServer
/*     */ {
/*     */   protected final MBeanServer server;
/*     */ 
/*     */   public SnmpGenericObjectServer(MBeanServer paramMBeanServer)
/*     */   {
/* 103 */     this.server = paramMBeanServer;
/*     */   }
/*     */ 
/*     */   public void get(SnmpGenericMetaServer paramSnmpGenericMetaServer, ObjectName paramObjectName, SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 145 */     int i = paramSnmpMibSubRequest.getSize();
/* 146 */     Object localObject1 = paramSnmpMibSubRequest.getUserData();
/* 147 */     String[] arrayOfString = new String[i];
/* 148 */     SnmpVarBind[] arrayOfSnmpVarBind = new SnmpVarBind[i];
/* 149 */     long[] arrayOfLong = new long[i];
/* 150 */     int j = 0;
/*     */ 
/* 152 */     for (Object localObject2 = paramSnmpMibSubRequest.getElements(); ((Enumeration)localObject2).hasMoreElements(); ) {
/* 153 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)((Enumeration)localObject2).nextElement();
/*     */       try {
/* 155 */         long l = localSnmpVarBind.oid.getOidArc(paramInt);
/* 156 */         arrayOfString[j] = paramSnmpGenericMetaServer.getAttributeName(l);
/* 157 */         arrayOfSnmpVarBind[j] = localSnmpVarBind;
/* 158 */         arrayOfLong[j] = l;
/*     */ 
/* 164 */         paramSnmpGenericMetaServer.checkGetAccess(l, localObject1);
/*     */ 
/* 167 */         j++;
/*     */       }
/*     */       catch (SnmpStatusException localSnmpStatusException1)
/*     */       {
/* 171 */         paramSnmpMibSubRequest.registerGetException(localSnmpVarBind, localSnmpStatusException1);
/*     */       }
/*     */     }
/*     */ 
/* 175 */     localObject2 = null;
/* 176 */     int k = 224;
/*     */     try
/*     */     {
/* 179 */       localObject2 = this.server.getAttributes(paramObjectName, arrayOfString);
/*     */     }
/*     */     catch (InstanceNotFoundException localInstanceNotFoundException)
/*     */     {
/* 183 */       localObject2 = new AttributeList();
/*     */     }
/*     */     catch (ReflectionException localReflectionException)
/*     */     {
/* 187 */       localObject2 = new AttributeList();
/*     */     } catch (Exception localException) {
/* 189 */       localObject2 = new AttributeList();
/*     */     }
/*     */ 
/* 193 */     Iterator localIterator = ((AttributeList)localObject2).iterator();
/*     */ 
/* 195 */     for (int m = 0; m < j; m++)
/*     */     {
/*     */       Object localObject3;
/* 196 */       if (!localIterator.hasNext())
/*     */       {
/* 199 */         localObject3 = new SnmpStatusException(k);
/*     */ 
/* 201 */         paramSnmpMibSubRequest.registerGetException(arrayOfSnmpVarBind[m], (SnmpStatusException)localObject3);
/*     */       }
/*     */       else
/*     */       {
/* 205 */         localObject3 = (Attribute)localIterator.next();
/*     */ 
/* 207 */         while ((m < j) && (!arrayOfString[m].equals(((Attribute)localObject3).getName())))
/*     */         {
/* 210 */           SnmpStatusException localSnmpStatusException2 = new SnmpStatusException(k);
/*     */ 
/* 212 */           paramSnmpMibSubRequest.registerGetException(arrayOfSnmpVarBind[m], localSnmpStatusException2);
/* 213 */           m++;
/*     */         }
/*     */ 
/* 216 */         if (m == j)
/*     */           break;
/*     */         try {
/* 219 */           arrayOfSnmpVarBind[m].value = paramSnmpGenericMetaServer.buildSnmpValue(arrayOfLong[m], ((Attribute)localObject3).getValue());
/*     */         }
/*     */         catch (SnmpStatusException localSnmpStatusException3) {
/* 222 */           paramSnmpMibSubRequest.registerGetException(arrayOfSnmpVarBind[m], localSnmpStatusException3);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public SnmpValue get(SnmpGenericMetaServer paramSnmpGenericMetaServer, ObjectName paramObjectName, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 254 */     String str = paramSnmpGenericMetaServer.getAttributeName(paramLong);
/* 255 */     Object localObject = null;
/*     */     try
/*     */     {
/* 258 */       localObject = this.server.getAttribute(paramObjectName, str);
/*     */     } catch (MBeanException localMBeanException) {
/* 260 */       Exception localException2 = localMBeanException.getTargetException();
/* 261 */       if ((localException2 instanceof SnmpStatusException))
/* 262 */         throw ((SnmpStatusException)localException2);
/* 263 */       throw new SnmpStatusException(224);
/*     */     } catch (Exception localException1) {
/* 265 */       throw new SnmpStatusException(224);
/*     */     }
/*     */ 
/* 268 */     return paramSnmpGenericMetaServer.buildSnmpValue(paramLong, localObject);
/*     */   }
/*     */ 
/*     */   public void set(SnmpGenericMetaServer paramSnmpGenericMetaServer, ObjectName paramObjectName, SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 308 */     int i = paramSnmpMibSubRequest.getSize();
/* 309 */     AttributeList localAttributeList = new AttributeList(i);
/* 310 */     String[] arrayOfString = new String[i];
/* 311 */     SnmpVarBind[] arrayOfSnmpVarBind = new SnmpVarBind[i];
/* 312 */     long[] arrayOfLong = new long[i];
/* 313 */     int j = 0;
/*     */ 
/* 315 */     for (Object localObject1 = paramSnmpMibSubRequest.getElements(); ((Enumeration)localObject1).hasMoreElements(); ) {
/* 316 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)((Enumeration)localObject1).nextElement();
/*     */       try {
/* 318 */         long l = localSnmpVarBind.oid.getOidArc(paramInt);
/* 319 */         localObject2 = paramSnmpGenericMetaServer.getAttributeName(l);
/* 320 */         localObject3 = paramSnmpGenericMetaServer.buildAttributeValue(l, localSnmpVarBind.value);
/*     */ 
/* 322 */         Attribute localAttribute = new Attribute((String)localObject2, localObject3);
/* 323 */         localAttributeList.add(localAttribute);
/* 324 */         arrayOfString[j] = localObject2;
/* 325 */         arrayOfSnmpVarBind[j] = localSnmpVarBind;
/* 326 */         arrayOfLong[j] = l;
/* 327 */         j++;
/*     */       } catch (SnmpStatusException localSnmpStatusException1) {
/* 329 */         paramSnmpMibSubRequest.registerSetException(localSnmpVarBind, localSnmpStatusException1);
/*     */       }
/*     */     }
/*     */     Object localObject2;
/*     */     Object localObject3;
/* 333 */     localObject1 = null;
/* 334 */     int k = 6;
/*     */     try
/*     */     {
/* 337 */       localObject1 = this.server.setAttributes(paramObjectName, localAttributeList);
/*     */     } catch (InstanceNotFoundException localInstanceNotFoundException) {
/* 339 */       localObject1 = new AttributeList();
/* 340 */       k = 18;
/*     */     } catch (ReflectionException localReflectionException) {
/* 342 */       k = 18;
/* 343 */       localObject1 = new AttributeList();
/*     */     } catch (Exception localException) {
/* 345 */       localObject1 = new AttributeList();
/*     */     }
/*     */ 
/* 348 */     Iterator localIterator = ((AttributeList)localObject1).iterator();
/*     */ 
/* 350 */     for (int m = 0; m < j; m++)
/* 351 */       if (!localIterator.hasNext()) {
/* 352 */         localObject2 = new SnmpStatusException(k);
/*     */ 
/* 354 */         paramSnmpMibSubRequest.registerSetException(arrayOfSnmpVarBind[m], (SnmpStatusException)localObject2);
/*     */       }
/*     */       else
/*     */       {
/* 358 */         localObject2 = (Attribute)localIterator.next();
/*     */ 
/* 360 */         while ((m < j) && (!arrayOfString[m].equals(((Attribute)localObject2).getName()))) {
/* 361 */           localObject3 = new SnmpStatusException(6);
/*     */ 
/* 363 */           paramSnmpMibSubRequest.registerSetException(arrayOfSnmpVarBind[m], (SnmpStatusException)localObject3);
/* 364 */           m++;
/*     */         }
/*     */ 
/* 367 */         if (m == j)
/*     */           break;
/*     */         try {
/* 370 */           arrayOfSnmpVarBind[m].value = paramSnmpGenericMetaServer.buildSnmpValue(arrayOfLong[m], ((Attribute)localObject2).getValue());
/*     */         }
/*     */         catch (SnmpStatusException localSnmpStatusException2) {
/* 373 */           paramSnmpMibSubRequest.registerSetException(arrayOfSnmpVarBind[m], localSnmpStatusException2);
/*     */         }
/*     */       }
/*     */   }
/*     */ 
/*     */   public SnmpValue set(SnmpGenericMetaServer paramSnmpGenericMetaServer, ObjectName paramObjectName, SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 405 */     String str = paramSnmpGenericMetaServer.getAttributeName(paramLong);
/* 406 */     Object localObject1 = paramSnmpGenericMetaServer.buildAttributeValue(paramLong, paramSnmpValue);
/*     */ 
/* 408 */     Attribute localAttribute = new Attribute(str, localObject1);
/*     */ 
/* 410 */     Object localObject2 = null;
/*     */     try
/*     */     {
/* 413 */       this.server.setAttribute(paramObjectName, localAttribute);
/* 414 */       localObject2 = this.server.getAttribute(paramObjectName, str);
/*     */     } catch (InvalidAttributeValueException localInvalidAttributeValueException) {
/* 416 */       throw new SnmpStatusException(10);
/*     */     }
/*     */     catch (InstanceNotFoundException localInstanceNotFoundException) {
/* 419 */       throw new SnmpStatusException(18);
/*     */     }
/*     */     catch (ReflectionException localReflectionException) {
/* 422 */       throw new SnmpStatusException(18);
/*     */     }
/*     */     catch (MBeanException localMBeanException) {
/* 425 */       Exception localException2 = localMBeanException.getTargetException();
/* 426 */       if ((localException2 instanceof SnmpStatusException))
/* 427 */         throw ((SnmpStatusException)localException2);
/* 428 */       throw new SnmpStatusException(6);
/*     */     }
/*     */     catch (Exception localException1) {
/* 431 */       throw new SnmpStatusException(6);
/*     */     }
/*     */ 
/* 435 */     return paramSnmpGenericMetaServer.buildSnmpValue(paramLong, localObject2);
/*     */   }
/*     */ 
/*     */   public void check(SnmpGenericMetaServer paramSnmpGenericMetaServer, ObjectName paramObjectName, SnmpMibSubRequest paramSnmpMibSubRequest, int paramInt)
/*     */     throws SnmpStatusException
/*     */   {
/* 470 */     Object localObject = paramSnmpMibSubRequest.getUserData();
/*     */ 
/* 472 */     for (Enumeration localEnumeration = paramSnmpMibSubRequest.getElements(); localEnumeration.hasMoreElements(); ) {
/* 473 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)localEnumeration.nextElement();
/*     */       try {
/* 475 */         long l = localSnmpVarBind.oid.getOidArc(paramInt);
/*     */ 
/* 477 */         check(paramSnmpGenericMetaServer, paramObjectName, localSnmpVarBind.value, l, localObject);
/*     */       } catch (SnmpStatusException localSnmpStatusException) {
/* 479 */         paramSnmpMibSubRequest.registerCheckException(localSnmpVarBind, localSnmpStatusException);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void check(SnmpGenericMetaServer paramSnmpGenericMetaServer, ObjectName paramObjectName, SnmpValue paramSnmpValue, long paramLong, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 520 */     paramSnmpGenericMetaServer.checkSetAccess(paramSnmpValue, paramLong, paramObject);
/*     */     try {
/* 522 */       String str = paramSnmpGenericMetaServer.getAttributeName(paramLong);
/* 523 */       localObject = paramSnmpGenericMetaServer.buildAttributeValue(paramLong, paramSnmpValue);
/* 524 */       Object[] arrayOfObject = new Object[1];
/* 525 */       String[] arrayOfString = new String[1];
/*     */ 
/* 527 */       arrayOfObject[0] = localObject;
/* 528 */       arrayOfString[0] = localObject.getClass().getName();
/* 529 */       this.server.invoke(paramObjectName, "check" + str, arrayOfObject, arrayOfString);
/*     */     }
/*     */     catch (SnmpStatusException localSnmpStatusException) {
/* 532 */       throw localSnmpStatusException;
/*     */     }
/*     */     catch (InstanceNotFoundException localInstanceNotFoundException) {
/* 535 */       throw new SnmpStatusException(18);
/*     */     }
/*     */     catch (ReflectionException localReflectionException) {
/*     */     }
/*     */     catch (MBeanException localMBeanException) {
/* 540 */       Object localObject = localMBeanException.getTargetException();
/* 541 */       if ((localObject instanceof SnmpStatusException))
/* 542 */         throw ((SnmpStatusException)localObject);
/* 543 */       throw new SnmpStatusException(6);
/*     */     } catch (Exception localException) {
/* 545 */       throw new SnmpStatusException(6);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void registerTableEntry(SnmpMibTable paramSnmpMibTable, SnmpOid paramSnmpOid, ObjectName paramObjectName, Object paramObject)
/*     */     throws SnmpStatusException
/*     */   {
/* 553 */     if (paramObjectName == null)
/* 554 */       throw new SnmpStatusException(18);
/*     */     try
/*     */     {
/* 557 */       if ((paramObject != null) && (!this.server.isRegistered(paramObjectName)))
/* 558 */         this.server.registerMBean(paramObject, paramObjectName);
/*     */     } catch (InstanceAlreadyExistsException localInstanceAlreadyExistsException) {
/* 560 */       throw new SnmpStatusException(18);
/*     */     }
/*     */     catch (MBeanRegistrationException localMBeanRegistrationException) {
/* 563 */       throw new SnmpStatusException(6);
/*     */     } catch (NotCompliantMBeanException localNotCompliantMBeanException) {
/* 565 */       throw new SnmpStatusException(5);
/*     */     } catch (RuntimeOperationsException localRuntimeOperationsException) {
/* 567 */       throw new SnmpStatusException(5);
/*     */     } catch (Exception localException) {
/* 569 */       throw new SnmpStatusException(5);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.agent.SnmpGenericObjectServer
 * JD-Core Version:    0.6.2
 */