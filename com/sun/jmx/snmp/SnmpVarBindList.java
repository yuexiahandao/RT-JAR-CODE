/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class SnmpVarBindList extends Vector<SnmpVarBind>
/*     */ {
/*     */   private static final long serialVersionUID = -7203997794636430321L;
/*  29 */   public String identity = "VarBindList ";
/*     */   Timestamp timestamp;
/*     */ 
/*     */   public SnmpVarBindList()
/*     */   {
/*  48 */     super(5, 5);
/*     */   }
/*     */ 
/*     */   public SnmpVarBindList(int paramInt)
/*     */   {
/*  56 */     super(paramInt);
/*     */   }
/*     */ 
/*     */   public SnmpVarBindList(String paramString)
/*     */   {
/*  64 */     super(5, 5);
/*  65 */     this.identity = paramString;
/*     */   }
/*     */ 
/*     */   public SnmpVarBindList(SnmpVarBindList paramSnmpVarBindList)
/*     */   {
/*  74 */     super(paramSnmpVarBindList.size(), 5);
/*  75 */     paramSnmpVarBindList.copyInto(this.elementData);
/*  76 */     this.elementCount = paramSnmpVarBindList.size();
/*     */   }
/*     */ 
/*     */   public SnmpVarBindList(Vector<SnmpVarBind> paramVector)
/*     */   {
/*  85 */     super(paramVector.size(), 5);
/*  86 */     for (Enumeration localEnumeration = paramVector.elements(); localEnumeration.hasMoreElements(); ) {
/*  87 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)localEnumeration.nextElement();
/*  88 */       addElement((SnmpVarBind)localSnmpVarBind.clone());
/*     */     }
/*     */   }
/*     */ 
/*     */   public SnmpVarBindList(String paramString, Vector<SnmpVarBind> paramVector)
/*     */   {
/*  99 */     this(paramVector);
/* 100 */     this.identity = paramString;
/*     */   }
/*     */ 
/*     */   public Timestamp getTimestamp()
/*     */   {
/* 112 */     return this.timestamp;
/*     */   }
/*     */ 
/*     */   public void setTimestamp(Timestamp paramTimestamp)
/*     */   {
/* 122 */     this.timestamp = paramTimestamp;
/*     */   }
/*     */ 
/*     */   public final synchronized SnmpVarBind getVarBindAt(int paramInt)
/*     */   {
/* 132 */     return (SnmpVarBind)elementAt(paramInt);
/*     */   }
/*     */ 
/*     */   public synchronized int getVarBindCount()
/*     */   {
/* 140 */     return size();
/*     */   }
/*     */ 
/*     */   public synchronized Enumeration getVarBindList()
/*     */   {
/* 149 */     return elements();
/*     */   }
/*     */ 
/*     */   public final synchronized void setVarBindList(Vector paramVector)
/*     */   {
/* 160 */     setVarBindList(paramVector, false);
/*     */   }
/*     */ 
/*     */   public final synchronized void setVarBindList(Vector paramVector, boolean paramBoolean)
/*     */   {
/* 172 */     synchronized (paramVector) {
/* 173 */       int i = paramVector.size();
/* 174 */       setSize(i);
/* 175 */       paramVector.copyInto(this.elementData);
/* 176 */       if (paramBoolean)
/* 177 */         for (int j = 0; j < i; j++) {
/* 178 */           SnmpVarBind localSnmpVarBind = (SnmpVarBind)this.elementData[j];
/* 179 */           this.elementData[j] = localSnmpVarBind.clone();
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void addVarBindList(SnmpVarBindList paramSnmpVarBindList)
/*     */   {
/* 194 */     ensureCapacity(paramSnmpVarBindList.size() + size());
/* 195 */     for (int i = 0; i < paramSnmpVarBindList.size(); i++)
/* 196 */       addElement(paramSnmpVarBindList.getVarBindAt(i));
/*     */   }
/*     */ 
/*     */   public synchronized boolean removeVarBindList(SnmpVarBindList paramSnmpVarBindList)
/*     */   {
/* 208 */     boolean bool = true;
/* 209 */     for (int i = 0; i < paramSnmpVarBindList.size(); i++) {
/* 210 */       bool = removeElement(paramSnmpVarBindList.getVarBindAt(i));
/*     */     }
/* 212 */     return bool;
/*     */   }
/*     */ 
/*     */   public final synchronized void replaceVarBind(SnmpVarBind paramSnmpVarBind, int paramInt)
/*     */   {
/* 222 */     setElementAt(paramSnmpVarBind, paramInt);
/*     */   }
/*     */ 
/*     */   public final synchronized void addVarBind(String[] paramArrayOfString, String paramString)
/*     */     throws SnmpStatusException
/*     */   {
/* 232 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 233 */       SnmpVarBind localSnmpVarBind = new SnmpVarBind(paramArrayOfString[i]);
/* 234 */       localSnmpVarBind.addInstance(paramString);
/* 235 */       addElement(localSnmpVarBind);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized boolean removeVarBind(String[] paramArrayOfString, String paramString)
/*     */     throws SnmpStatusException
/*     */   {
/* 248 */     boolean bool = true;
/* 249 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 250 */       SnmpVarBind localSnmpVarBind = new SnmpVarBind(paramArrayOfString[i]);
/* 251 */       localSnmpVarBind.addInstance(paramString);
/* 252 */       int j = indexOfOid(localSnmpVarBind);
/*     */       try {
/* 254 */         removeElementAt(j);
/*     */       } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/* 256 */         bool = false;
/*     */       }
/*     */     }
/* 259 */     return bool;
/*     */   }
/*     */ 
/*     */   public synchronized void addVarBind(String[] paramArrayOfString)
/*     */     throws SnmpStatusException
/*     */   {
/* 275 */     addVarBind(paramArrayOfString, null);
/*     */   }
/*     */ 
/*     */   public synchronized boolean removeVarBind(String[] paramArrayOfString)
/*     */     throws SnmpStatusException
/*     */   {
/* 286 */     return removeVarBind(paramArrayOfString, null);
/*     */   }
/*     */ 
/*     */   public synchronized void addVarBind(String paramString)
/*     */     throws SnmpStatusException
/*     */   {
/* 298 */     SnmpVarBind localSnmpVarBind = new SnmpVarBind(paramString);
/* 299 */     addVarBind(localSnmpVarBind);
/*     */   }
/*     */ 
/*     */   public synchronized boolean removeVarBind(String paramString)
/*     */     throws SnmpStatusException
/*     */   {
/* 313 */     SnmpVarBind localSnmpVarBind = new SnmpVarBind(paramString);
/* 314 */     int i = indexOfOid(localSnmpVarBind);
/*     */     try {
/* 316 */       removeElementAt(i);
/* 317 */       return true; } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/*     */     }
/* 319 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized void addVarBind(SnmpVarBind paramSnmpVarBind)
/*     */   {
/* 329 */     addElement(paramSnmpVarBind);
/*     */   }
/*     */ 
/*     */   public synchronized boolean removeVarBind(SnmpVarBind paramSnmpVarBind)
/*     */   {
/* 339 */     return removeElement(paramSnmpVarBind);
/*     */   }
/*     */ 
/*     */   public synchronized void addInstance(String paramString)
/*     */     throws SnmpStatusException
/*     */   {
/* 349 */     int i = size();
/* 350 */     for (int j = 0; j < i; j++)
/* 351 */       ((SnmpVarBind)this.elementData[j]).addInstance(paramString);
/*     */   }
/*     */ 
/*     */   public final synchronized void concat(Vector<SnmpVarBind> paramVector)
/*     */   {
/* 361 */     ensureCapacity(size() + paramVector.size());
/* 362 */     for (Enumeration localEnumeration = paramVector.elements(); localEnumeration.hasMoreElements(); )
/* 363 */       addElement(localEnumeration.nextElement());
/*     */   }
/*     */ 
/*     */   public synchronized boolean checkForValidValues()
/*     */   {
/* 373 */     int i = size();
/* 374 */     for (int j = 0; j < i; j++) {
/* 375 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)this.elementData[j];
/* 376 */       if (!localSnmpVarBind.isValidValue())
/* 377 */         return false;
/*     */     }
/* 379 */     return true;
/*     */   }
/*     */ 
/*     */   public synchronized boolean checkForUnspecifiedValue()
/*     */   {
/* 387 */     int i = size();
/* 388 */     for (int j = 0; j < i; j++) {
/* 389 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)this.elementData[j];
/* 390 */       if (localSnmpVarBind.isUnspecifiedValue())
/* 391 */         return true;
/*     */     }
/* 393 */     return false;
/*     */   }
/*     */ 
/*     */   public synchronized SnmpVarBindList splitAt(int paramInt)
/*     */   {
/* 402 */     SnmpVarBindList localSnmpVarBindList = null;
/* 403 */     if (paramInt > this.elementCount)
/* 404 */       return localSnmpVarBindList;
/* 405 */     localSnmpVarBindList = new SnmpVarBindList();
/* 406 */     int i = size();
/* 407 */     for (int j = paramInt; j < i; j++)
/* 408 */       localSnmpVarBindList.addElement((SnmpVarBind)this.elementData[j]);
/* 409 */     this.elementCount = paramInt;
/* 410 */     trimToSize();
/* 411 */     return localSnmpVarBindList;
/*     */   }
/*     */ 
/*     */   public synchronized int indexOfOid(SnmpVarBind paramSnmpVarBind, int paramInt1, int paramInt2)
/*     */   {
/* 424 */     SnmpOid localSnmpOid = paramSnmpVarBind.getOid();
/* 425 */     for (int i = paramInt1; i < paramInt2; i++) {
/* 426 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)this.elementData[i];
/* 427 */       if (localSnmpOid.equals(localSnmpVarBind.getOid()))
/* 428 */         return i;
/*     */     }
/* 430 */     return -1;
/*     */   }
/*     */ 
/*     */   public synchronized int indexOfOid(SnmpVarBind paramSnmpVarBind)
/*     */   {
/* 439 */     return indexOfOid(paramSnmpVarBind, 0, size());
/*     */   }
/*     */ 
/*     */   public synchronized int indexOfOid(SnmpOid paramSnmpOid)
/*     */   {
/* 448 */     int i = size();
/* 449 */     for (int j = 0; j < i; j++) {
/* 450 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)this.elementData[j];
/* 451 */       if (paramSnmpOid.equals(localSnmpVarBind.getOid()))
/* 452 */         return j;
/*     */     }
/* 454 */     return -1;
/*     */   }
/*     */ 
/*     */   public synchronized SnmpVarBindList cloneWithValue()
/*     */   {
/* 463 */     SnmpVarBindList localSnmpVarBindList = new SnmpVarBindList();
/* 464 */     localSnmpVarBindList.setTimestamp(getTimestamp());
/* 465 */     localSnmpVarBindList.ensureCapacity(size());
/* 466 */     for (int i = 0; i < size(); i++) {
/* 467 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)this.elementData[i];
/* 468 */       localSnmpVarBindList.addElement((SnmpVarBind)localSnmpVarBind.clone());
/*     */     }
/* 470 */     return localSnmpVarBindList;
/*     */   }
/*     */ 
/*     */   public synchronized SnmpVarBindList cloneWithoutValue()
/*     */   {
/* 479 */     SnmpVarBindList localSnmpVarBindList = new SnmpVarBindList();
/* 480 */     int i = size();
/* 481 */     localSnmpVarBindList.ensureCapacity(i);
/* 482 */     for (int j = 0; j < i; j++) {
/* 483 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)this.elementData[j];
/* 484 */       localSnmpVarBindList.addElement((SnmpVarBind)localSnmpVarBind.cloneWithoutValue());
/*     */     }
/* 486 */     return localSnmpVarBindList;
/*     */   }
/*     */ 
/*     */   public synchronized Object clone()
/*     */   {
/* 495 */     return cloneWithValue();
/*     */   }
/*     */ 
/*     */   public synchronized Vector toVector(boolean paramBoolean)
/*     */   {
/* 506 */     int i = this.elementCount;
/* 507 */     if (!paramBoolean) return (Vector)super.clone();
/* 508 */     Vector localVector = new Vector(i, 5);
/* 509 */     for (int j = 0; j < i; j++) {
/* 510 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)this.elementData[j];
/* 511 */       localVector.addElement((SnmpVarBind)localSnmpVarBind.clone());
/*     */     }
/* 513 */     return localVector;
/*     */   }
/*     */ 
/*     */   public String oidListToString()
/*     */   {
/* 521 */     StringBuffer localStringBuffer = new StringBuffer(300);
/* 522 */     for (int i = 0; i < this.elementCount; i++) {
/* 523 */       SnmpVarBind localSnmpVarBind = (SnmpVarBind)this.elementData[i];
/* 524 */       localStringBuffer.append(localSnmpVarBind.getOid().toString() + "\n");
/*     */     }
/* 526 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public synchronized String varBindListToString()
/*     */   {
/* 535 */     StringBuffer localStringBuffer = new StringBuffer(300);
/* 536 */     for (int i = 0; i < this.elementCount; i++) {
/* 537 */       localStringBuffer.append(this.elementData[i].toString() + "\n");
/*     */     }
/* 539 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public void finalize()
/*     */   {
/* 549 */     removeAllElements();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpVarBindList
 * JD-Core Version:    0.6.2
 */