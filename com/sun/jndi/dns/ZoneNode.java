/*     */ package com.sun.jndi.dns;
/*     */ 
/*     */ import java.lang.ref.SoftReference;
/*     */ import java.util.Date;
/*     */ import java.util.Vector;
/*     */ 
/*     */ class ZoneNode extends NameNode
/*     */ {
/*  58 */   private SoftReference contentsRef = null;
/*  59 */   private long serialNumber = -1L;
/*  60 */   private Date expiration = null;
/*     */ 
/*     */   ZoneNode(String paramString) {
/*  63 */     super(paramString);
/*     */   }
/*     */ 
/*     */   protected NameNode newNameNode(String paramString) {
/*  67 */     return new ZoneNode(paramString);
/*     */   }
/*     */ 
/*     */   synchronized void depopulate()
/*     */   {
/*  75 */     this.contentsRef = null;
/*  76 */     this.serialNumber = -1L;
/*     */   }
/*     */ 
/*     */   synchronized boolean isPopulated()
/*     */   {
/*  83 */     return getContents() != null;
/*     */   }
/*     */ 
/*     */   synchronized NameNode getContents()
/*     */   {
/*  90 */     return this.contentsRef != null ? (NameNode)this.contentsRef.get() : null;
/*     */   }
/*     */ 
/*     */   synchronized boolean isExpired()
/*     */   {
/*  99 */     return (this.expiration != null) && (this.expiration.before(new Date()));
/*     */   }
/*     */ 
/*     */   ZoneNode getDeepestPopulated(DnsName paramDnsName)
/*     */   {
/* 109 */     ZoneNode localZoneNode1 = this;
/* 110 */     ZoneNode localZoneNode2 = isPopulated() ? this : null;
/* 111 */     for (int i = 1; i < paramDnsName.size(); i++) {
/* 112 */       localZoneNode1 = (ZoneNode)localZoneNode1.get(paramDnsName.getKey(i));
/* 113 */       if (localZoneNode1 == null)
/*     */         break;
/* 115 */       if (localZoneNode1.isPopulated()) {
/* 116 */         localZoneNode2 = localZoneNode1;
/*     */       }
/*     */     }
/* 119 */     return localZoneNode2;
/*     */   }
/*     */ 
/*     */   NameNode populate(DnsName paramDnsName, ResourceRecords paramResourceRecords)
/*     */   {
/* 130 */     NameNode localNameNode1 = new NameNode(null);
/*     */ 
/* 132 */     for (int i = 0; i < paramResourceRecords.answer.size(); i++) {
/* 133 */       ResourceRecord localResourceRecord2 = (ResourceRecord)paramResourceRecords.answer.elementAt(i);
/* 134 */       DnsName localDnsName = localResourceRecord2.getName();
/*     */ 
/* 139 */       if ((localDnsName.size() > paramDnsName.size()) && (localDnsName.startsWith(paramDnsName))) {
/* 140 */         NameNode localNameNode2 = localNameNode1.add(localDnsName, paramDnsName.size());
/* 141 */         if (localResourceRecord2.getType() == 2) {
/* 142 */           localNameNode2.setZoneCut(true);
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 147 */     ResourceRecord localResourceRecord1 = (ResourceRecord)paramResourceRecords.answer.firstElement();
/* 148 */     synchronized (this) {
/* 149 */       this.contentsRef = new SoftReference(localNameNode1);
/* 150 */       this.serialNumber = getSerialNumber(localResourceRecord1);
/* 151 */       setExpiration(getMinimumTtl(localResourceRecord1));
/* 152 */       return localNameNode1;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void setExpiration(long paramLong)
/*     */   {
/* 160 */     this.expiration = new Date(System.currentTimeMillis() + 1000L * paramLong);
/*     */   }
/*     */ 
/*     */   private static long getMinimumTtl(ResourceRecord paramResourceRecord)
/*     */   {
/* 168 */     String str = (String)paramResourceRecord.getRdata();
/* 169 */     int i = str.lastIndexOf(' ') + 1;
/* 170 */     return Long.parseLong(str.substring(i));
/*     */   }
/*     */ 
/*     */   int compareSerialNumberTo(ResourceRecord paramResourceRecord)
/*     */   {
/* 184 */     return ResourceRecord.compareSerialNumbers(this.serialNumber, getSerialNumber(paramResourceRecord));
/*     */   }
/*     */ 
/*     */   private static long getSerialNumber(ResourceRecord paramResourceRecord)
/*     */   {
/* 192 */     String str = (String)paramResourceRecord.getRdata();
/*     */ 
/* 197 */     int i = str.length();
/* 198 */     int j = -1;
/* 199 */     for (int k = 0; k < 5; k++) {
/* 200 */       j = i;
/* 201 */       i = str.lastIndexOf(' ', j - 1);
/*     */     }
/* 203 */     return Long.parseLong(str.substring(i + 1, j));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.dns.ZoneNode
 * JD-Core Version:    0.6.2
 */