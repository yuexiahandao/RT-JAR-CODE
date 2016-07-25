/*     */ package com.sun.corba.se.impl.legacy.connection;
/*     */ 
/*     */ import com.sun.corba.se.spi.legacy.connection.LegacyServerSocketEndPointInfo;
/*     */ import com.sun.corba.se.spi.transport.SocketInfo;
/*     */ 
/*     */ public class EndPointInfoImpl
/*     */   implements SocketInfo, LegacyServerSocketEndPointInfo
/*     */ {
/*     */   protected String type;
/*     */   protected String hostname;
/*     */   protected int port;
/*     */   protected int locatorPort;
/*     */   protected String name;
/*     */ 
/*     */   public EndPointInfoImpl(String paramString1, int paramInt, String paramString2)
/*     */   {
/*  44 */     this.type = paramString1;
/*  45 */     this.port = paramInt;
/*  46 */     this.hostname = paramString2;
/*  47 */     this.locatorPort = -1;
/*  48 */     this.name = "NO_NAME";
/*     */   }
/*     */ 
/*     */   public String getType() {
/*  52 */     return this.type;
/*     */   }
/*     */ 
/*     */   public String getHost() {
/*  56 */     return this.hostname;
/*     */   }
/*     */ 
/*     */   public String getHostName() {
/*  60 */     return this.hostname;
/*     */   }
/*     */ 
/*     */   public int getPort() {
/*  64 */     return this.port;
/*     */   }
/*     */ 
/*     */   public int getLocatorPort()
/*     */   {
/*  69 */     return this.locatorPort;
/*     */   }
/*     */ 
/*     */   public void setLocatorPort(int paramInt)
/*     */   {
/*  74 */     this.locatorPort = paramInt;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/*  79 */     return this.name;
/*     */   }
/*     */ 
/*     */   public int hashCode() {
/*  83 */     return this.type.hashCode() ^ this.hostname.hashCode() ^ this.port;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject) {
/*  87 */     if (!(paramObject instanceof EndPointInfoImpl)) {
/*  88 */       return false;
/*     */     }
/*  90 */     EndPointInfoImpl localEndPointInfoImpl = (EndPointInfoImpl)paramObject;
/*  91 */     if (this.type == null) {
/*  92 */       if (localEndPointInfoImpl.type != null)
/*  93 */         return false;
/*     */     }
/*  95 */     else if (!this.type.equals(localEndPointInfoImpl.type)) {
/*  96 */       return false;
/*     */     }
/*  98 */     if (this.port != localEndPointInfoImpl.port) {
/*  99 */       return false;
/*     */     }
/* 101 */     if (!this.hostname.equals(localEndPointInfoImpl.hostname)) {
/* 102 */       return false;
/*     */     }
/* 104 */     return true;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 109 */     return this.type + " " + this.name + " " + this.hostname + " " + this.port;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.legacy.connection.EndPointInfoImpl
 * JD-Core Version:    0.6.2
 */