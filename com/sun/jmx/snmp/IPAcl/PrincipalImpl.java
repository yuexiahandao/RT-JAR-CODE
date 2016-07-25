/*     */ package com.sun.jmx.snmp.IPAcl;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.Principal;
/*     */ 
/*     */ class PrincipalImpl
/*     */   implements Principal, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -7910027842878976761L;
/*  44 */   private InetAddress[] add = null;
/*     */ 
/*     */   public PrincipalImpl()
/*     */     throws UnknownHostException
/*     */   {
/*  50 */     this.add = new InetAddress[1];
/*  51 */     this.add[0] = InetAddress.getLocalHost();
/*     */   }
/*     */ 
/*     */   public PrincipalImpl(String paramString)
/*     */     throws UnknownHostException
/*     */   {
/*  66 */     if ((paramString.equals("localhost")) || (paramString.equals("127.0.0.1"))) {
/*  67 */       this.add = new InetAddress[1];
/*  68 */       this.add[0] = InetAddress.getByName(paramString);
/*     */     }
/*     */     else {
/*  71 */       this.add = InetAddress.getAllByName(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   public PrincipalImpl(InetAddress paramInetAddress)
/*     */   {
/*  80 */     this.add = new InetAddress[1];
/*  81 */     this.add[0] = paramInetAddress;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  90 */     return this.add[0].toString();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 102 */     if ((paramObject instanceof PrincipalImpl)) {
/* 103 */       for (int i = 0; i < this.add.length; i++) {
/* 104 */         if (this.add[i].equals(((PrincipalImpl)paramObject).getAddress()))
/* 105 */           return true;
/*     */       }
/* 107 */       return false;
/*     */     }
/* 109 */     return false;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 119 */     return this.add[0].hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 128 */     return "PrincipalImpl :" + this.add[0].toString();
/*     */   }
/*     */ 
/*     */   public InetAddress getAddress()
/*     */   {
/* 137 */     return this.add[0];
/*     */   }
/*     */ 
/*     */   public InetAddress[] getAddresses()
/*     */   {
/* 146 */     return this.add;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.PrincipalImpl
 * JD-Core Version:    0.6.2
 */