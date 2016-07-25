/*     */ package com.sun.jndi.dns;
/*     */ 
/*     */ import java.util.Vector;
/*     */ import javax.naming.CommunicationException;
/*     */ import javax.naming.NameNotFoundException;
/*     */ import javax.naming.NamingException;
/*     */ 
/*     */ class Resolver
/*     */ {
/*     */   private DnsClient dnsClient;
/*     */   private int timeout;
/*     */   private int retries;
/*     */ 
/*     */   Resolver(String[] paramArrayOfString, int paramInt1, int paramInt2)
/*     */     throws NamingException
/*     */   {
/*  59 */     this.timeout = paramInt1;
/*  60 */     this.retries = paramInt2;
/*  61 */     this.dnsClient = new DnsClient(paramArrayOfString, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public void close() {
/*  65 */     this.dnsClient.close();
/*  66 */     this.dnsClient = null;
/*     */   }
/*     */ 
/*     */   ResourceRecords query(DnsName paramDnsName, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
/*     */     throws NamingException
/*     */   {
/*  81 */     return this.dnsClient.query(paramDnsName, paramInt1, paramInt2, paramBoolean1, paramBoolean2);
/*     */   }
/*     */ 
/*     */   ResourceRecords queryZone(DnsName paramDnsName, int paramInt, boolean paramBoolean)
/*     */     throws NamingException
/*     */   {
/*  92 */     DnsClient localDnsClient = new DnsClient(findNameServers(paramDnsName, paramBoolean), this.timeout, this.retries);
/*     */     try
/*     */     {
/*  95 */       return localDnsClient.queryZone(paramDnsName, paramInt, paramBoolean);
/*     */     } finally {
/*  97 */       localDnsClient.close();
/*     */     }
/*     */   }
/*     */ 
/*     */   DnsName findZoneName(DnsName paramDnsName, int paramInt, boolean paramBoolean)
/*     */     throws NamingException
/*     */   {
/* 111 */     paramDnsName = (DnsName)paramDnsName.clone();
/* 112 */     while (paramDnsName.size() > 1) {
/* 113 */       ResourceRecords localResourceRecords = null;
/*     */       try {
/* 115 */         localResourceRecords = query(paramDnsName, paramInt, 6, paramBoolean, false);
/*     */       }
/*     */       catch (NameNotFoundException localNameNotFoundException) {
/* 118 */         throw localNameNotFoundException;
/*     */       }
/*     */       catch (NamingException localNamingException) {
/*     */       }
/* 122 */       if (localResourceRecords != null) {
/* 123 */         if (localResourceRecords.answer.size() > 0) {
/* 124 */           return paramDnsName;
/*     */         }
/*     */ 
/* 127 */         for (int i = 0; i < localResourceRecords.authority.size(); i++) {
/* 128 */           ResourceRecord localResourceRecord = (ResourceRecord)localResourceRecords.authority.elementAt(i);
/*     */ 
/* 130 */           if (localResourceRecord.getType() == 6) {
/* 131 */             DnsName localDnsName = localResourceRecord.getName();
/* 132 */             if (paramDnsName.endsWith(localDnsName)) {
/* 133 */               return localDnsName;
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/* 138 */       paramDnsName.remove(paramDnsName.size() - 1);
/*     */     }
/* 140 */     return paramDnsName;
/*     */   }
/*     */ 
/*     */   ResourceRecord findSoa(DnsName paramDnsName, int paramInt, boolean paramBoolean)
/*     */     throws NamingException
/*     */   {
/* 152 */     ResourceRecords localResourceRecords = query(paramDnsName, paramInt, 6, paramBoolean, false);
/*     */ 
/* 154 */     for (int i = 0; i < localResourceRecords.answer.size(); i++) {
/* 155 */       ResourceRecord localResourceRecord = (ResourceRecord)localResourceRecords.answer.elementAt(i);
/* 156 */       if (localResourceRecord.getType() == 6) {
/* 157 */         return localResourceRecord;
/*     */       }
/*     */     }
/* 160 */     return null;
/*     */   }
/*     */ 
/*     */   private String[] findNameServers(DnsName paramDnsName, boolean paramBoolean)
/*     */     throws NamingException
/*     */   {
/* 173 */     ResourceRecords localResourceRecords = query(paramDnsName, 1, 2, paramBoolean, false);
/*     */ 
/* 176 */     String[] arrayOfString = new String[localResourceRecords.answer.size()];
/* 177 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 178 */       ResourceRecord localResourceRecord = (ResourceRecord)localResourceRecords.answer.elementAt(i);
/*     */ 
/* 180 */       if (localResourceRecord.getType() != 2) {
/* 181 */         throw new CommunicationException("Corrupted DNS message");
/*     */       }
/* 183 */       arrayOfString[i] = ((String)localResourceRecord.getRdata());
/*     */ 
/* 188 */       arrayOfString[i] = arrayOfString[i].substring(0, arrayOfString[i].length() - 1);
/*     */     }
/* 190 */     return arrayOfString;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.dns.Resolver
 * JD-Core Version:    0.6.2
 */