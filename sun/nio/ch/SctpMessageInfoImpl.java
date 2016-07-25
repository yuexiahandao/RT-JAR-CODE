/*     */ package sun.nio.ch;
/*     */ 
/*     */ import com.sun.nio.sctp.Association;
/*     */ import com.sun.nio.sctp.MessageInfo;
/*     */ import java.net.SocketAddress;
/*     */ 
/*     */ public class SctpMessageInfoImpl extends MessageInfo
/*     */ {
/*     */   private final SocketAddress address;
/*     */   private final int bytes;
/*     */   private Association association;
/*     */   private int assocId;
/*     */   private int streamNumber;
/*  41 */   private boolean complete = true;
/*     */   private boolean unordered;
/*     */   private long timeToLive;
/*     */   private int ppid;
/*     */ 
/*     */   public SctpMessageInfoImpl(Association paramAssociation, SocketAddress paramSocketAddress, int paramInt)
/*     */   {
/*  49 */     this.association = paramAssociation;
/*  50 */     this.address = paramSocketAddress;
/*  51 */     this.streamNumber = paramInt;
/*  52 */     this.bytes = 0;
/*     */   }
/*     */ 
/*     */   private SctpMessageInfoImpl(int paramInt1, SocketAddress paramSocketAddress, int paramInt2, int paramInt3, boolean paramBoolean1, boolean paramBoolean2, int paramInt4)
/*     */   {
/*  63 */     this.assocId = paramInt1;
/*  64 */     this.address = paramSocketAddress;
/*  65 */     this.bytes = paramInt2;
/*  66 */     this.streamNumber = paramInt3;
/*  67 */     this.complete = paramBoolean1;
/*  68 */     this.unordered = paramBoolean2;
/*  69 */     this.ppid = paramInt4;
/*     */   }
/*     */ 
/*     */   public Association association()
/*     */   {
/*  74 */     return this.association;
/*     */   }
/*     */ 
/*     */   void setAssociation(Association paramAssociation)
/*     */   {
/*  82 */     this.association = paramAssociation;
/*     */   }
/*     */ 
/*     */   int associationID() {
/*  86 */     return this.assocId;
/*     */   }
/*     */ 
/*     */   public SocketAddress address()
/*     */   {
/*  91 */     return this.address;
/*     */   }
/*     */ 
/*     */   public int bytes()
/*     */   {
/*  96 */     return this.bytes;
/*     */   }
/*     */ 
/*     */   public int streamNumber()
/*     */   {
/* 101 */     return this.streamNumber;
/*     */   }
/*     */ 
/*     */   public MessageInfo streamNumber(int paramInt)
/*     */   {
/* 106 */     if ((paramInt < 0) || (paramInt > 65536)) {
/* 107 */       throw new IllegalArgumentException("Invalid stream number");
/*     */     }
/* 109 */     this.streamNumber = paramInt;
/* 110 */     return this;
/*     */   }
/*     */ 
/*     */   public int payloadProtocolID()
/*     */   {
/* 115 */     return this.ppid;
/*     */   }
/*     */ 
/*     */   public MessageInfo payloadProtocolID(int paramInt)
/*     */   {
/* 120 */     this.ppid = paramInt;
/* 121 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean isComplete()
/*     */   {
/* 126 */     return this.complete;
/*     */   }
/*     */ 
/*     */   public MessageInfo complete(boolean paramBoolean)
/*     */   {
/* 131 */     this.complete = paramBoolean;
/* 132 */     return this;
/*     */   }
/*     */ 
/*     */   public boolean isUnordered()
/*     */   {
/* 137 */     return this.unordered;
/*     */   }
/*     */ 
/*     */   public MessageInfo unordered(boolean paramBoolean)
/*     */   {
/* 142 */     this.unordered = paramBoolean;
/* 143 */     return this;
/*     */   }
/*     */ 
/*     */   public long timeToLive()
/*     */   {
/* 148 */     return this.timeToLive;
/*     */   }
/*     */ 
/*     */   public MessageInfo timeToLive(long paramLong)
/*     */   {
/* 153 */     this.timeToLive = paramLong;
/* 154 */     return this;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 159 */     StringBuilder localStringBuilder = new StringBuilder(super.toString());
/* 160 */     localStringBuilder.append("[Address: ").append(this.address).append(", Association: ").append(this.association).append(", Assoc ID: ").append(this.assocId).append(", Bytes: ").append(this.bytes).append(", Stream Number: ").append(this.streamNumber).append(", Complete: ").append(this.complete).append(", isUnordered: ").append(this.unordered).append("]");
/*     */ 
/* 168 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.SctpMessageInfoImpl
 * JD-Core Version:    0.6.2
 */