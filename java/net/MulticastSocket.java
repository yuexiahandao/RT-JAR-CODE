/*     */ package java.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ public class MulticastSocket extends DatagramSocket
/*     */ {
/*     */   private boolean interfaceSet;
/* 180 */   private Object ttlLock = new Object();
/*     */ 
/* 186 */   private Object infLock = new Object();
/*     */ 
/* 191 */   private InetAddress infAddress = null;
/*     */ 
/*     */   public MulticastSocket()
/*     */     throws IOException
/*     */   {
/* 112 */     this(new InetSocketAddress(0));
/*     */   }
/*     */ 
/*     */   public MulticastSocket(int paramInt)
/*     */     throws IOException
/*     */   {
/* 137 */     this(new InetSocketAddress(paramInt));
/*     */   }
/*     */ 
/*     */   public MulticastSocket(SocketAddress paramSocketAddress)
/*     */     throws IOException
/*     */   {
/* 166 */     super((SocketAddress)null);
/*     */ 
/* 169 */     setReuseAddress(true);
/*     */ 
/* 171 */     if (paramSocketAddress != null)
/* 172 */       bind(paramSocketAddress);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void setTTL(byte paramByte)
/*     */     throws IOException
/*     */   {
/* 211 */     if (isClosed())
/* 212 */       throw new SocketException("Socket is closed");
/* 213 */     getImpl().setTTL(paramByte);
/*     */   }
/*     */ 
/*     */   public void setTimeToLive(int paramInt)
/*     */     throws IOException
/*     */   {
/* 236 */     if ((paramInt < 0) || (paramInt > 255)) {
/* 237 */       throw new IllegalArgumentException("ttl out of range");
/*     */     }
/* 239 */     if (isClosed())
/* 240 */       throw new SocketException("Socket is closed");
/* 241 */     getImpl().setTimeToLive(paramInt);
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public byte getTTL()
/*     */     throws IOException
/*     */   {
/* 257 */     if (isClosed())
/* 258 */       throw new SocketException("Socket is closed");
/* 259 */     return getImpl().getTTL();
/*     */   }
/*     */ 
/*     */   public int getTimeToLive()
/*     */     throws IOException
/*     */   {
/* 271 */     if (isClosed())
/* 272 */       throw new SocketException("Socket is closed");
/* 273 */     return getImpl().getTimeToLive();
/*     */   }
/*     */ 
/*     */   public void joinGroup(InetAddress paramInetAddress)
/*     */     throws IOException
/*     */   {
/* 295 */     if (isClosed()) {
/* 296 */       throw new SocketException("Socket is closed");
/*     */     }
/*     */ 
/* 299 */     checkAddress(paramInetAddress, "joinGroup");
/* 300 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 301 */     if (localSecurityManager != null) {
/* 302 */       localSecurityManager.checkMulticast(paramInetAddress);
/*     */     }
/*     */ 
/* 305 */     if (!paramInetAddress.isMulticastAddress()) {
/* 306 */       throw new SocketException("Not a multicast address");
/*     */     }
/*     */ 
/* 313 */     NetworkInterface localNetworkInterface = NetworkInterface.getDefault();
/*     */ 
/* 315 */     if ((!this.interfaceSet) && (localNetworkInterface != null)) {
/* 316 */       setNetworkInterface(localNetworkInterface);
/*     */     }
/*     */ 
/* 319 */     getImpl().join(paramInetAddress);
/*     */   }
/*     */ 
/*     */   public void leaveGroup(InetAddress paramInetAddress)
/*     */     throws IOException
/*     */   {
/* 340 */     if (isClosed()) {
/* 341 */       throw new SocketException("Socket is closed");
/*     */     }
/*     */ 
/* 344 */     checkAddress(paramInetAddress, "leaveGroup");
/* 345 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 346 */     if (localSecurityManager != null) {
/* 347 */       localSecurityManager.checkMulticast(paramInetAddress);
/*     */     }
/*     */ 
/* 350 */     if (!paramInetAddress.isMulticastAddress()) {
/* 351 */       throw new SocketException("Not a multicast address");
/*     */     }
/*     */ 
/* 354 */     getImpl().leave(paramInetAddress);
/*     */   }
/*     */ 
/*     */   public void joinGroup(SocketAddress paramSocketAddress, NetworkInterface paramNetworkInterface)
/*     */     throws IOException
/*     */   {
/* 383 */     if (isClosed()) {
/* 384 */       throw new SocketException("Socket is closed");
/*     */     }
/* 386 */     if ((paramSocketAddress == null) || (!(paramSocketAddress instanceof InetSocketAddress))) {
/* 387 */       throw new IllegalArgumentException("Unsupported address type");
/*     */     }
/* 389 */     if (this.oldImpl) {
/* 390 */       throw new UnsupportedOperationException();
/*     */     }
/* 392 */     checkAddress(((InetSocketAddress)paramSocketAddress).getAddress(), "joinGroup");
/* 393 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 394 */     if (localSecurityManager != null) {
/* 395 */       localSecurityManager.checkMulticast(((InetSocketAddress)paramSocketAddress).getAddress());
/*     */     }
/*     */ 
/* 398 */     if (!((InetSocketAddress)paramSocketAddress).getAddress().isMulticastAddress()) {
/* 399 */       throw new SocketException("Not a multicast address");
/*     */     }
/*     */ 
/* 402 */     getImpl().joinGroup(paramSocketAddress, paramNetworkInterface);
/*     */   }
/*     */ 
/*     */   public void leaveGroup(SocketAddress paramSocketAddress, NetworkInterface paramNetworkInterface)
/*     */     throws IOException
/*     */   {
/* 430 */     if (isClosed()) {
/* 431 */       throw new SocketException("Socket is closed");
/*     */     }
/* 433 */     if ((paramSocketAddress == null) || (!(paramSocketAddress instanceof InetSocketAddress))) {
/* 434 */       throw new IllegalArgumentException("Unsupported address type");
/*     */     }
/* 436 */     if (this.oldImpl) {
/* 437 */       throw new UnsupportedOperationException();
/*     */     }
/* 439 */     checkAddress(((InetSocketAddress)paramSocketAddress).getAddress(), "leaveGroup");
/* 440 */     SecurityManager localSecurityManager = System.getSecurityManager();
/* 441 */     if (localSecurityManager != null) {
/* 442 */       localSecurityManager.checkMulticast(((InetSocketAddress)paramSocketAddress).getAddress());
/*     */     }
/*     */ 
/* 445 */     if (!((InetSocketAddress)paramSocketAddress).getAddress().isMulticastAddress()) {
/* 446 */       throw new SocketException("Not a multicast address");
/*     */     }
/*     */ 
/* 449 */     getImpl().leaveGroup(paramSocketAddress, paramNetworkInterface);
/*     */   }
/*     */ 
/*     */   public void setInterface(InetAddress paramInetAddress)
/*     */     throws SocketException
/*     */   {
/* 462 */     if (isClosed()) {
/* 463 */       throw new SocketException("Socket is closed");
/*     */     }
/* 465 */     checkAddress(paramInetAddress, "setInterface");
/* 466 */     synchronized (this.infLock) {
/* 467 */       getImpl().setOption(16, paramInetAddress);
/* 468 */       this.infAddress = paramInetAddress;
/* 469 */       this.interfaceSet = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public InetAddress getInterface()
/*     */     throws SocketException
/*     */   {
/* 487 */     if (isClosed()) {
/* 488 */       throw new SocketException("Socket is closed");
/*     */     }
/* 490 */     synchronized (this.infLock) {
/* 491 */       InetAddress localInetAddress1 = (InetAddress)getImpl().getOption(16);
/*     */ 
/* 498 */       if (this.infAddress == null) {
/* 499 */         return localInetAddress1;
/*     */       }
/*     */ 
/* 505 */       if (localInetAddress1.equals(this.infAddress)) {
/* 506 */         return localInetAddress1;
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 515 */         NetworkInterface localNetworkInterface = NetworkInterface.getByInetAddress(localInetAddress1);
/* 516 */         Enumeration localEnumeration = localNetworkInterface.getInetAddresses();
/* 517 */         while (localEnumeration.hasMoreElements()) {
/* 518 */           InetAddress localInetAddress2 = (InetAddress)localEnumeration.nextElement();
/* 519 */           if (localInetAddress2.equals(this.infAddress)) {
/* 520 */             return this.infAddress;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 528 */         this.infAddress = null;
/* 529 */         return localInetAddress1;
/*     */       } catch (Exception localException) {
/* 531 */         return localInetAddress1;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setNetworkInterface(NetworkInterface paramNetworkInterface)
/*     */     throws SocketException
/*     */   {
/* 549 */     synchronized (this.infLock) {
/* 550 */       getImpl().setOption(31, paramNetworkInterface);
/* 551 */       this.infAddress = null;
/* 552 */       this.interfaceSet = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public NetworkInterface getNetworkInterface()
/*     */     throws SocketException
/*     */   {
/* 566 */     NetworkInterface localNetworkInterface = (NetworkInterface)getImpl().getOption(31);
/*     */ 
/* 568 */     if ((localNetworkInterface.getIndex() == 0) || (localNetworkInterface.getIndex() == -1)) {
/* 569 */       InetAddress[] arrayOfInetAddress = new InetAddress[1];
/* 570 */       arrayOfInetAddress[0] = InetAddress.anyLocalAddress();
/* 571 */       return new NetworkInterface(arrayOfInetAddress[0].getHostName(), 0, arrayOfInetAddress);
/*     */     }
/* 573 */     return localNetworkInterface;
/*     */   }
/*     */ 
/*     */   public void setLoopbackMode(boolean paramBoolean)
/*     */     throws SocketException
/*     */   {
/* 592 */     getImpl().setOption(18, Boolean.valueOf(paramBoolean));
/*     */   }
/*     */ 
/*     */   public boolean getLoopbackMode()
/*     */     throws SocketException
/*     */   {
/* 604 */     return ((Boolean)getImpl().getOption(18)).booleanValue();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   public void send(DatagramPacket paramDatagramPacket, byte paramByte)
/*     */     throws IOException
/*     */   {
/* 657 */     if (isClosed())
/* 658 */       throw new SocketException("Socket is closed");
/* 659 */     checkAddress(paramDatagramPacket.getAddress(), "send");
/* 660 */     synchronized (this.ttlLock) {
/* 661 */       synchronized (paramDatagramPacket)
/*     */       {
/*     */         Object localObject1;
/* 662 */         if (this.connectState == 0)
/*     */         {
/* 666 */           localObject1 = System.getSecurityManager();
/* 667 */           if (localObject1 != null) {
/* 668 */             if (paramDatagramPacket.getAddress().isMulticastAddress())
/* 669 */               ((SecurityManager)localObject1).checkMulticast(paramDatagramPacket.getAddress(), paramByte);
/*     */             else {
/* 671 */               ((SecurityManager)localObject1).checkConnect(paramDatagramPacket.getAddress().getHostAddress(), paramDatagramPacket.getPort());
/*     */             }
/*     */           }
/*     */         }
/*     */         else
/*     */         {
/* 677 */           localObject1 = null;
/* 678 */           localObject1 = paramDatagramPacket.getAddress();
/* 679 */           if (localObject1 == null) {
/* 680 */             paramDatagramPacket.setAddress(this.connectedAddress);
/* 681 */             paramDatagramPacket.setPort(this.connectedPort);
/* 682 */           } else if ((!((InetAddress)localObject1).equals(this.connectedAddress)) || (paramDatagramPacket.getPort() != this.connectedPort))
/*     */           {
/* 684 */             throw new SecurityException("connected address and packet address differ");
/*     */           }
/*     */         }
/*     */ 
/* 688 */         byte b = getTTL();
/*     */         try {
/* 690 */           if (paramByte != b)
/*     */           {
/* 692 */             getImpl().setTTL(paramByte);
/*     */           }
/*     */ 
/* 695 */           getImpl().send(paramDatagramPacket);
/*     */ 
/* 698 */           if (paramByte != b)
/* 699 */             getImpl().setTTL(b);
/*     */         }
/*     */         finally
/*     */         {
/* 698 */           if (paramByte != b)
/* 699 */             getImpl().setTTL(b);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.net.MulticastSocket
 * JD-Core Version:    0.6.2
 */