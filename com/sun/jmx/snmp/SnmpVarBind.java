/*     */ package com.sun.jmx.snmp;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class SnmpVarBind
/*     */   implements SnmpDataTypeEnums, Cloneable, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 491778383240759376L;
/*  38 */   private static final String[] statusLegend = { "Status Mapper", "Value not initialized", "Valid Value", "No such object", "No such Instance", "End of Mib View" };
/*     */   public static final int stValueUnspecified = 1;
/*     */   public static final int stValueOk = 2;
/*     */   public static final int stValueNoSuchObject = 3;
/*     */   public static final int stValueNoSuchInstance = 4;
/*     */   public static final int stValueEndOfMibView = 5;
/*  82 */   public static final SnmpNull noSuchObject = new SnmpNull(128);
/*     */ 
/*  87 */   public static final SnmpNull noSuchInstance = new SnmpNull(129);
/*     */ 
/*  92 */   public static final SnmpNull endOfMibView = new SnmpNull(130);
/*     */ 
/* 100 */   public SnmpOid oid = null;
/*     */ 
/* 108 */   public SnmpValue value = null;
/*     */ 
/* 115 */   public int status = 1;
/*     */ 
/*     */   public SnmpVarBind()
/*     */   {
/*     */   }
/*     */ 
/*     */   public SnmpVarBind(SnmpOid paramSnmpOid)
/*     */   {
/* 132 */     this.oid = paramSnmpOid;
/*     */   }
/*     */ 
/*     */   public SnmpVarBind(SnmpOid paramSnmpOid, SnmpValue paramSnmpValue)
/*     */   {
/* 142 */     this.oid = paramSnmpOid;
/* 143 */     setSnmpValue(paramSnmpValue);
/*     */   }
/*     */ 
/*     */   public SnmpVarBind(String paramString)
/*     */     throws SnmpStatusException
/*     */   {
/* 154 */     if (paramString.startsWith(".")) {
/* 155 */       this.oid = new SnmpOid(paramString);
/*     */     } else {
/* 157 */       SnmpOidRecord localSnmpOidRecord = null;
/*     */       try {
/* 159 */         int i = paramString.indexOf('.');
/* 160 */         handleLong(paramString, i);
/* 161 */         this.oid = new SnmpOid(paramString);
/*     */       }
/*     */       catch (NumberFormatException localNumberFormatException) {
/* 164 */         int j = paramString.indexOf('.');
/* 165 */         if (j <= 0) {
/* 166 */           localSnmpOidRecord = resolveVarName(paramString);
/* 167 */           this.oid = new SnmpOid(localSnmpOidRecord.getName());
/*     */         } else {
/* 169 */           localSnmpOidRecord = resolveVarName(paramString.substring(0, j));
/* 170 */           this.oid = new SnmpOid(localSnmpOidRecord.getName() + paramString.substring(j));
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public final SnmpOid getOid()
/*     */   {
/* 185 */     return this.oid;
/*     */   }
/*     */ 
/*     */   public final void setOid(SnmpOid paramSnmpOid)
/*     */   {
/* 194 */     this.oid = paramSnmpOid;
/* 195 */     clearValue();
/*     */   }
/*     */ 
/*     */   public final synchronized SnmpValue getSnmpValue()
/*     */   {
/* 203 */     return this.value;
/*     */   }
/*     */ 
/*     */   public final void setSnmpValue(SnmpValue paramSnmpValue)
/*     */   {
/* 212 */     this.value = paramSnmpValue;
/* 213 */     setValueValid();
/*     */   }
/*     */ 
/*     */   public final SnmpCounter64 getSnmpCounter64Value()
/*     */     throws ClassCastException
/*     */   {
/* 223 */     return (SnmpCounter64)this.value;
/*     */   }
/*     */ 
/*     */   public final void setSnmpCounter64Value(long paramLong)
/*     */     throws IllegalArgumentException
/*     */   {
/* 235 */     clearValue();
/* 236 */     this.value = new SnmpCounter64(paramLong);
/* 237 */     setValueValid();
/*     */   }
/*     */ 
/*     */   public final SnmpInt getSnmpIntValue()
/*     */     throws ClassCastException
/*     */   {
/* 247 */     return (SnmpInt)this.value;
/*     */   }
/*     */ 
/*     */   public final void setSnmpIntValue(long paramLong)
/*     */     throws IllegalArgumentException
/*     */   {
/* 260 */     clearValue();
/* 261 */     this.value = new SnmpInt(paramLong);
/* 262 */     setValueValid();
/*     */   }
/*     */ 
/*     */   public final SnmpCounter getSnmpCounterValue()
/*     */     throws ClassCastException
/*     */   {
/* 272 */     return (SnmpCounter)this.value;
/*     */   }
/*     */ 
/*     */   public final void setSnmpCounterValue(long paramLong)
/*     */     throws IllegalArgumentException
/*     */   {
/* 285 */     clearValue();
/* 286 */     this.value = new SnmpCounter(paramLong);
/* 287 */     setValueValid();
/*     */   }
/*     */ 
/*     */   public final SnmpGauge getSnmpGaugeValue()
/*     */     throws ClassCastException
/*     */   {
/* 297 */     return (SnmpGauge)this.value;
/*     */   }
/*     */ 
/*     */   public final void setSnmpGaugeValue(long paramLong)
/*     */     throws IllegalArgumentException
/*     */   {
/* 310 */     clearValue();
/* 311 */     this.value = new SnmpGauge(paramLong);
/* 312 */     setValueValid();
/*     */   }
/*     */ 
/*     */   public final SnmpTimeticks getSnmpTimeticksValue()
/*     */     throws ClassCastException
/*     */   {
/* 322 */     return (SnmpTimeticks)this.value;
/*     */   }
/*     */ 
/*     */   public final void setSnmpTimeticksValue(long paramLong)
/*     */     throws IllegalArgumentException
/*     */   {
/* 335 */     clearValue();
/* 336 */     this.value = new SnmpTimeticks(paramLong);
/* 337 */     setValueValid();
/*     */   }
/*     */ 
/*     */   public final SnmpOid getSnmpOidValue()
/*     */     throws ClassCastException
/*     */   {
/* 347 */     return (SnmpOid)this.value;
/*     */   }
/*     */ 
/*     */   public final void setSnmpOidValue(String paramString)
/*     */     throws IllegalArgumentException
/*     */   {
/* 360 */     clearValue();
/* 361 */     this.value = new SnmpOid(paramString);
/* 362 */     setValueValid();
/*     */   }
/*     */ 
/*     */   public final SnmpIpAddress getSnmpIpAddressValue()
/*     */     throws ClassCastException
/*     */   {
/* 372 */     return (SnmpIpAddress)this.value;
/*     */   }
/*     */ 
/*     */   public final void setSnmpIpAddressValue(String paramString)
/*     */     throws IllegalArgumentException
/*     */   {
/* 384 */     clearValue();
/* 385 */     this.value = new SnmpIpAddress(paramString);
/* 386 */     setValueValid();
/*     */   }
/*     */ 
/*     */   public final SnmpString getSnmpStringValue()
/*     */     throws ClassCastException
/*     */   {
/* 396 */     return (SnmpString)this.value;
/*     */   }
/*     */ 
/*     */   public final void setSnmpStringValue(String paramString)
/*     */   {
/* 407 */     clearValue();
/* 408 */     this.value = new SnmpString(paramString);
/* 409 */     setValueValid();
/*     */   }
/*     */ 
/*     */   public final SnmpOpaque getSnmpOpaqueValue()
/*     */     throws ClassCastException
/*     */   {
/* 419 */     return (SnmpOpaque)this.value;
/*     */   }
/*     */ 
/*     */   public final void setSnmpOpaqueValue(byte[] paramArrayOfByte)
/*     */   {
/* 430 */     clearValue();
/* 431 */     this.value = new SnmpOpaque(paramArrayOfByte);
/* 432 */     setValueValid();
/*     */   }
/*     */ 
/*     */   public final SnmpStringFixed getSnmpStringFixedValue()
/*     */     throws ClassCastException
/*     */   {
/* 442 */     return (SnmpStringFixed)this.value;
/*     */   }
/*     */ 
/*     */   public final void setSnmpStringFixedValue(String paramString)
/*     */   {
/* 453 */     clearValue();
/* 454 */     this.value = new SnmpStringFixed(paramString);
/* 455 */     setValueValid();
/*     */   }
/*     */ 
/*     */   public SnmpOidRecord resolveVarName(String paramString)
/*     */     throws SnmpStatusException
/*     */   {
/* 470 */     SnmpOidTable localSnmpOidTable = SnmpOid.getSnmpOidTable();
/* 471 */     if (localSnmpOidTable == null)
/* 472 */       throw new SnmpStatusException(2);
/* 473 */     int i = paramString.indexOf('.');
/* 474 */     if (i < 0) {
/* 475 */       return localSnmpOidTable.resolveVarName(paramString);
/*     */     }
/* 477 */     return localSnmpOidTable.resolveVarOid(paramString);
/*     */   }
/*     */ 
/*     */   public final int getValueStatus()
/*     */   {
/* 488 */     return this.status;
/*     */   }
/*     */ 
/*     */   public final String getValueStatusLegend()
/*     */   {
/* 499 */     return statusLegend[this.status];
/*     */   }
/*     */ 
/*     */   public final boolean isValidValue()
/*     */   {
/* 507 */     return this.status == 2;
/*     */   }
/*     */ 
/*     */   public final boolean isUnspecifiedValue()
/*     */   {
/* 515 */     return this.status == 1;
/*     */   }
/*     */ 
/*     */   public final void clearValue()
/*     */   {
/* 523 */     this.value = null;
/* 524 */     this.status = 1;
/*     */   }
/*     */ 
/*     */   public final boolean isOidEqual(SnmpVarBind paramSnmpVarBind)
/*     */   {
/* 534 */     return this.oid.equals(paramSnmpVarBind.oid);
/*     */   }
/*     */ 
/*     */   public final void addInstance(long paramLong)
/*     */   {
/* 544 */     this.oid.append(paramLong);
/*     */   }
/*     */ 
/*     */   public final void addInstance(long[] paramArrayOfLong)
/*     */     throws SnmpStatusException
/*     */   {
/* 556 */     this.oid.addToOid(paramArrayOfLong);
/*     */   }
/*     */ 
/*     */   public final void addInstance(String paramString)
/*     */     throws SnmpStatusException
/*     */   {
/* 568 */     if (paramString != null)
/* 569 */       this.oid.addToOid(paramString);
/*     */   }
/*     */ 
/*     */   public void insertInOid(int paramInt)
/*     */   {
/* 579 */     this.oid.insert(paramInt);
/*     */   }
/*     */ 
/*     */   public void appendInOid(SnmpOid paramSnmpOid)
/*     */   {
/* 587 */     this.oid.append(paramSnmpOid);
/*     */   }
/*     */ 
/*     */   public final synchronized boolean hasVarBindException()
/*     */   {
/* 597 */     switch (this.status) {
/*     */     case 1:
/*     */     case 3:
/*     */     case 4:
/*     */     case 5:
/* 602 */       return true;
/*     */     case 2:
/* 604 */     }return false;
/*     */   }
/*     */ 
/*     */   public void copyValueAndOid(SnmpVarBind paramSnmpVarBind)
/*     */   {
/* 612 */     setOid((SnmpOid)paramSnmpVarBind.oid.clone());
/* 613 */     copyValue(paramSnmpVarBind);
/*     */   }
/*     */ 
/*     */   public void copyValue(SnmpVarBind paramSnmpVarBind)
/*     */   {
/* 621 */     if (paramSnmpVarBind.isValidValue()) {
/* 622 */       this.value = paramSnmpVarBind.getSnmpValue().duplicate();
/* 623 */       setValueValid();
/*     */     } else {
/* 625 */       this.status = paramSnmpVarBind.getValueStatus();
/* 626 */       if (this.status == 5) this.value = endOfMibView;
/* 627 */       else if (this.status == 3) this.value = noSuchObject;
/* 628 */       else if (this.status == 4) this.value = noSuchInstance;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object cloneWithoutValue()
/*     */   {
/* 637 */     SnmpOid localSnmpOid = (SnmpOid)this.oid.clone();
/* 638 */     return new SnmpVarBind(localSnmpOid);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 650 */     SnmpVarBind localSnmpVarBind = new SnmpVarBind();
/* 651 */     localSnmpVarBind.copyValueAndOid(this);
/* 652 */     return localSnmpVarBind;
/*     */   }
/*     */ 
/*     */   public final String getStringValue()
/*     */   {
/* 660 */     return this.value.toString();
/*     */   }
/*     */ 
/*     */   public final void setNoSuchObject()
/*     */   {
/* 668 */     this.value = noSuchObject;
/* 669 */     this.status = 3;
/*     */   }
/*     */ 
/*     */   public final void setNoSuchInstance()
/*     */   {
/* 677 */     this.value = noSuchInstance;
/* 678 */     this.status = 4;
/*     */   }
/*     */ 
/*     */   public final void setEndOfMibView()
/*     */   {
/* 686 */     this.value = endOfMibView;
/* 687 */     this.status = 5;
/*     */   }
/*     */ 
/*     */   public final String toString()
/*     */   {
/* 696 */     StringBuffer localStringBuffer = new StringBuffer(400);
/* 697 */     localStringBuffer.append("Object ID : " + this.oid.toString());
/*     */ 
/* 699 */     if (isValidValue()) {
/* 700 */       localStringBuffer.append("  (Syntax : " + this.value.getTypeName() + ")\n");
/* 701 */       localStringBuffer.append("Value : " + this.value.toString());
/*     */     } else {
/* 703 */       localStringBuffer.append("\nValue Exception : " + getValueStatusLegend());
/*     */     }
/* 705 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private void setValueValid()
/*     */   {
/* 716 */     if (this.value == endOfMibView) this.status = 5;
/* 717 */     else if (this.value == noSuchObject) this.status = 3;
/* 718 */     else if (this.value == noSuchInstance) this.status = 4; else
/* 719 */       this.status = 2;
/*     */   }
/*     */ 
/*     */   private void handleLong(String paramString, int paramInt)
/*     */     throws NumberFormatException, SnmpStatusException
/*     */   {
/*     */     String str;
/* 725 */     if (paramInt > 0)
/* 726 */       str = paramString.substring(0, paramInt);
/*     */     else {
/* 728 */       str = paramString;
/*     */     }
/*     */ 
/* 733 */     Long.parseLong(str);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.SnmpVarBind
 * JD-Core Version:    0.6.2
 */