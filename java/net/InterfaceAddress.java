/*     */ package java.net;
/*     */ 
/*     */ public class InterfaceAddress
/*     */ {
/*  38 */   private InetAddress address = null;
/*  39 */   private Inet4Address broadcast = null;
/*  40 */   private short maskLength = 0;
/*     */ 
/*     */   public InetAddress getAddress()
/*     */   {
/*  55 */     return this.address;
/*     */   }
/*     */ 
/*     */   public InetAddress getBroadcast()
/*     */   {
/*  69 */     return this.broadcast;
/*     */   }
/*     */ 
/*     */   public short getNetworkPrefixLength()
/*     */   {
/*  83 */     return this.maskLength;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 102 */     if (!(paramObject instanceof InterfaceAddress)) {
/* 103 */       return false;
/*     */     }
/* 105 */     InterfaceAddress localInterfaceAddress = (InterfaceAddress)paramObject;
/* 106 */     if (this.address == null ? localInterfaceAddress.address != null : !this.address.equals(localInterfaceAddress.address))
/* 107 */       return false;
/* 108 */     if (this.broadcast == null ? localInterfaceAddress.broadcast != null : !this.broadcast.equals(localInterfaceAddress.broadcast))
/* 109 */       return false;
/* 110 */     if (this.maskLength != localInterfaceAddress.maskLength)
/* 111 */       return false;
/* 112 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 121 */     return this.address.hashCode() + (this.broadcast != null ? this.broadcast.hashCode() : 0) + this.maskLength;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 131 */     return this.address + "/" + this.maskLength + " [" + this.broadcast + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.InterfaceAddress
 * JD-Core Version:    0.6.2
 */