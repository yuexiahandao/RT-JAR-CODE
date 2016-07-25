/*      */ package java.net;
/*      */ 
/*      */ class InetAddressImplFactory
/*      */ {
/*      */   static InetAddressImpl create()
/*      */   {
/* 1615 */     return InetAddress.loadImpl(isIPv6Supported() ? "Inet6AddressImpl" : "Inet4AddressImpl");
/*      */   }
/*      */ 
/*      */   static native boolean isIPv6Supported();
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.InetAddressImplFactory
 * JD-Core Version:    0.6.2
 */