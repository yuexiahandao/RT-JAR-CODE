/*     */ package org.ietf.jgss;
/*     */ 
/*     */ import java.net.InetAddress;
/*     */ import java.util.Arrays;
/*     */ 
/*     */ public class ChannelBinding
/*     */ {
/*     */   private InetAddress initiator;
/*     */   private InetAddress acceptor;
/*     */   private byte[] appData;
/*     */ 
/*     */   public ChannelBinding(InetAddress paramInetAddress1, InetAddress paramInetAddress2, byte[] paramArrayOfByte)
/*     */   {
/* 104 */     this.initiator = paramInetAddress1;
/* 105 */     this.acceptor = paramInetAddress2;
/*     */ 
/* 107 */     if (paramArrayOfByte != null) {
/* 108 */       this.appData = new byte[paramArrayOfByte.length];
/* 109 */       System.arraycopy(paramArrayOfByte, 0, this.appData, 0, paramArrayOfByte.length);
/*     */     }
/*     */   }
/*     */ 
/*     */   public ChannelBinding(byte[] paramArrayOfByte)
/*     */   {
/* 121 */     this(null, null, paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public InetAddress getInitiatorAddress()
/*     */   {
/* 131 */     return this.initiator;
/*     */   }
/*     */ 
/*     */   public InetAddress getAcceptorAddress()
/*     */   {
/* 141 */     return this.acceptor;
/*     */   }
/*     */ 
/*     */   public byte[] getApplicationData()
/*     */   {
/* 153 */     if (this.appData == null) {
/* 154 */       return null;
/*     */     }
/*     */ 
/* 157 */     byte[] arrayOfByte = new byte[this.appData.length];
/* 158 */     System.arraycopy(this.appData, 0, arrayOfByte, 0, this.appData.length);
/* 159 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 172 */     if (this == paramObject) {
/* 173 */       return true;
/*     */     }
/* 175 */     if (!(paramObject instanceof ChannelBinding)) {
/* 176 */       return false;
/*     */     }
/* 178 */     ChannelBinding localChannelBinding = (ChannelBinding)paramObject;
/*     */ 
/* 180 */     if (((this.initiator != null) && (localChannelBinding.initiator == null)) || ((this.initiator == null) && (localChannelBinding.initiator != null)))
/*     */     {
/* 182 */       return false;
/*     */     }
/* 184 */     if ((this.initiator != null) && (!this.initiator.equals(localChannelBinding.initiator))) {
/* 185 */       return false;
/*     */     }
/* 187 */     if (((this.acceptor != null) && (localChannelBinding.acceptor == null)) || ((this.acceptor == null) && (localChannelBinding.acceptor != null)))
/*     */     {
/* 189 */       return false;
/*     */     }
/* 191 */     if ((this.acceptor != null) && (!this.acceptor.equals(localChannelBinding.acceptor))) {
/* 192 */       return false;
/*     */     }
/* 194 */     return Arrays.equals(this.appData, localChannelBinding.appData);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 203 */     if (this.initiator != null)
/* 204 */       return this.initiator.hashCode();
/* 205 */     if (this.acceptor != null)
/* 206 */       return this.acceptor.hashCode();
/* 207 */     if (this.appData != null) {
/* 208 */       return new String(this.appData).hashCode();
/*     */     }
/* 210 */     return 1;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.ietf.jgss.ChannelBinding
 * JD-Core Version:    0.6.2
 */