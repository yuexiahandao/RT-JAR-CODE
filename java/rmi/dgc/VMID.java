/*     */ package java.rmi.dgc;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ import java.rmi.server.UID;
/*     */ import java.security.SecureRandom;
/*     */ 
/*     */ public final class VMID
/*     */   implements Serializable
/*     */ {
/*  61 */   private static final byte[] randomBytes = arrayOfByte;
/*     */   private byte[] addr;
/*     */   private UID uid;
/*     */   private static final long serialVersionUID = -538642295484486218L;
/*     */ 
/*     */   public VMID()
/*     */   {
/*  73 */     this.addr = randomBytes;
/*  74 */     this.uid = new UID();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public static boolean isUnique()
/*     */   {
/*  85 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/*  92 */     return this.uid.hashCode();
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 100 */     if ((paramObject instanceof VMID)) {
/* 101 */       VMID localVMID = (VMID)paramObject;
/* 102 */       if (!this.uid.equals(localVMID.uid))
/* 103 */         return false;
/* 104 */       if (((this.addr == null ? 1 : 0) ^ (localVMID.addr == null ? 1 : 0)) != 0)
/* 105 */         return false;
/* 106 */       if (this.addr != null) {
/* 107 */         if (this.addr.length != localVMID.addr.length)
/* 108 */           return false;
/* 109 */         for (int i = 0; i < this.addr.length; i++)
/* 110 */           if (this.addr[i] != localVMID.addr[i])
/* 111 */             return false;
/*     */       }
/* 113 */       return true;
/*     */     }
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 123 */     StringBuffer localStringBuffer = new StringBuffer();
/* 124 */     if (this.addr != null) {
/* 125 */       for (int i = 0; i < this.addr.length; i++) {
/* 126 */         int j = this.addr[i] & 0xFF;
/* 127 */         localStringBuffer.append((j < 16 ? "0" : "") + Integer.toString(j, 16));
/*     */       }
/*     */     }
/* 130 */     localStringBuffer.append(':');
/* 131 */     localStringBuffer.append(this.uid.toString());
/* 132 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  58 */     SecureRandom localSecureRandom = new SecureRandom();
/*  59 */     byte[] arrayOfByte = new byte[8];
/*  60 */     localSecureRandom.nextBytes(arrayOfByte);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.rmi.dgc.VMID
 * JD-Core Version:    0.6.2
 */