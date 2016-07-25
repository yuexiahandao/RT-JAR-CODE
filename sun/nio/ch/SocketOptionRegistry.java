/*    */ package sun.nio.ch;
/*    */ 
/*    */ import java.net.ProtocolFamily;
/*    */ import java.net.SocketOption;
/*    */ import java.net.StandardProtocolFamily;
/*    */ import java.net.StandardSocketOptions;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ class SocketOptionRegistry
/*    */ {
/*    */   public static OptionKey findOption(SocketOption<?> paramSocketOption, ProtocolFamily paramProtocolFamily)
/*    */   {
/* 80 */     RegistryKey localRegistryKey = new RegistryKey(paramSocketOption, paramProtocolFamily);
/* 81 */     return (OptionKey)LazyInitialization.options.get(localRegistryKey);
/*    */   }
/*    */ 
/*    */   private static class LazyInitialization
/*    */   {
/* 57 */     static final Map<SocketOptionRegistry.RegistryKey, OptionKey> options = options();
/*    */ 
/* 59 */     private static Map<SocketOptionRegistry.RegistryKey, OptionKey> options() { HashMap localHashMap = new HashMap();
/*    */ 
/* 61 */       localHashMap.put(new SocketOptionRegistry.RegistryKey(StandardSocketOptions.SO_BROADCAST, Net.UNSPEC), new OptionKey(65535, 32));
/* 62 */       localHashMap.put(new SocketOptionRegistry.RegistryKey(StandardSocketOptions.SO_KEEPALIVE, Net.UNSPEC), new OptionKey(65535, 8));
/* 63 */       localHashMap.put(new SocketOptionRegistry.RegistryKey(StandardSocketOptions.SO_LINGER, Net.UNSPEC), new OptionKey(65535, 128));
/* 64 */       localHashMap.put(new SocketOptionRegistry.RegistryKey(StandardSocketOptions.SO_SNDBUF, Net.UNSPEC), new OptionKey(65535, 4097));
/* 65 */       localHashMap.put(new SocketOptionRegistry.RegistryKey(StandardSocketOptions.SO_RCVBUF, Net.UNSPEC), new OptionKey(65535, 4098));
/* 66 */       localHashMap.put(new SocketOptionRegistry.RegistryKey(StandardSocketOptions.SO_REUSEADDR, Net.UNSPEC), new OptionKey(65535, 4));
/* 67 */       localHashMap.put(new SocketOptionRegistry.RegistryKey(StandardSocketOptions.TCP_NODELAY, Net.UNSPEC), new OptionKey(6, 1));
/* 68 */       localHashMap.put(new SocketOptionRegistry.RegistryKey(StandardSocketOptions.IP_TOS, StandardProtocolFamily.INET), new OptionKey(0, 3));
/* 69 */       localHashMap.put(new SocketOptionRegistry.RegistryKey(StandardSocketOptions.IP_MULTICAST_IF, StandardProtocolFamily.INET), new OptionKey(0, 9));
/* 70 */       localHashMap.put(new SocketOptionRegistry.RegistryKey(StandardSocketOptions.IP_MULTICAST_TTL, StandardProtocolFamily.INET), new OptionKey(0, 10));
/* 71 */       localHashMap.put(new SocketOptionRegistry.RegistryKey(StandardSocketOptions.IP_MULTICAST_LOOP, StandardProtocolFamily.INET), new OptionKey(0, 11));
/* 72 */       localHashMap.put(new SocketOptionRegistry.RegistryKey(StandardSocketOptions.IP_MULTICAST_IF, StandardProtocolFamily.INET6), new OptionKey(41, 9));
/* 73 */       localHashMap.put(new SocketOptionRegistry.RegistryKey(StandardSocketOptions.IP_MULTICAST_TTL, StandardProtocolFamily.INET6), new OptionKey(41, 10));
/* 74 */       localHashMap.put(new SocketOptionRegistry.RegistryKey(StandardSocketOptions.IP_MULTICAST_LOOP, StandardProtocolFamily.INET6), new OptionKey(41, 11));
/* 75 */       localHashMap.put(new SocketOptionRegistry.RegistryKey(ExtendedSocketOption.SO_OOBINLINE, Net.UNSPEC), new OptionKey(65535, 256));
/* 76 */       return localHashMap;
/*    */     }
/*    */   }
/*    */ 
/*    */   private static class RegistryKey
/*    */   {
/*    */     private final SocketOption<?> name;
/*    */     private final ProtocolFamily family;
/*    */ 
/*    */     RegistryKey(SocketOption<?> paramSocketOption, ProtocolFamily paramProtocolFamily)
/*    */     {
/* 41 */       this.name = paramSocketOption;
/* 42 */       this.family = paramProtocolFamily;
/*    */     }
/*    */     public int hashCode() {
/* 45 */       return this.name.hashCode() + this.family.hashCode();
/*    */     }
/*    */     public boolean equals(Object paramObject) {
/* 48 */       if (paramObject == null) return false;
/* 49 */       if (!(paramObject instanceof RegistryKey)) return false;
/* 50 */       RegistryKey localRegistryKey = (RegistryKey)paramObject;
/* 51 */       if (this.name != localRegistryKey.name) return false;
/* 52 */       if (this.family != localRegistryKey.family) return false;
/* 53 */       return true;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.SocketOptionRegistry
 * JD-Core Version:    0.6.2
 */