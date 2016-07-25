/*     */ package java.net;
/*     */ 
/*     */ public final class StandardSocketOptions
/*     */ {
/*  64 */   public static final SocketOption<Boolean> SO_BROADCAST = new StdSocketOption("SO_BROADCAST", Boolean.class);
/*     */ 
/*  84 */   public static final SocketOption<Boolean> SO_KEEPALIVE = new StdSocketOption("SO_KEEPALIVE", Boolean.class);
/*     */ 
/* 115 */   public static final SocketOption<Integer> SO_SNDBUF = new StdSocketOption("SO_SNDBUF", Integer.class);
/*     */ 
/* 155 */   public static final SocketOption<Integer> SO_RCVBUF = new StdSocketOption("SO_RCVBUF", Integer.class);
/*     */ 
/* 186 */   public static final SocketOption<Boolean> SO_REUSEADDR = new StdSocketOption("SO_REUSEADDR", Boolean.class);
/*     */ 
/* 218 */   public static final SocketOption<Integer> SO_LINGER = new StdSocketOption("SO_LINGER", Integer.class);
/*     */ 
/* 250 */   public static final SocketOption<Integer> IP_TOS = new StdSocketOption("IP_TOS", Integer.class);
/*     */ 
/* 273 */   public static final SocketOption<NetworkInterface> IP_MULTICAST_IF = new StdSocketOption("IP_MULTICAST_IF", NetworkInterface.class);
/*     */ 
/* 300 */   public static final SocketOption<Integer> IP_MULTICAST_TTL = new StdSocketOption("IP_MULTICAST_TTL", Integer.class);
/*     */ 
/* 325 */   public static final SocketOption<Boolean> IP_MULTICAST_LOOP = new StdSocketOption("IP_MULTICAST_LOOP", Boolean.class);
/*     */ 
/* 352 */   public static final SocketOption<Boolean> TCP_NODELAY = new StdSocketOption("TCP_NODELAY", Boolean.class);
/*     */ 
/*     */   private static class StdSocketOption<T> implements SocketOption<T> {
/*     */     private final String name;
/*     */     private final Class<T> type;
/*     */ 
/*     */     StdSocketOption(String paramString, Class<T> paramClass) {
/* 360 */       this.name = paramString;
/* 361 */       this.type = paramClass;
/*     */     }
/* 363 */     public String name() { return this.name; } 
/* 364 */     public Class<T> type() { return this.type; } 
/* 365 */     public String toString() { return this.name; }
/*     */ 
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.StandardSocketOptions
 * JD-Core Version:    0.6.2
 */