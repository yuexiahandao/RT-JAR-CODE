/*     */ package com.sun.jndi.ldap;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InterruptedIOException;
/*     */ import java.io.OutputStream;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.net.Socket;
/*     */ import javax.naming.CommunicationException;
/*     */ import javax.naming.InterruptedNamingException;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.ServiceUnavailableException;
/*     */ import javax.naming.ldap.Control;
/*     */ import javax.net.ssl.SSLSocket;
/*     */ 
/*     */ public final class Connection
/*     */   implements Runnable
/*     */ {
/*     */   private static final boolean debug = false;
/*     */   private static final int dump = 0;
/*     */   private final Thread worker;
/* 118 */   private boolean v3 = true;
/*     */   public final String host;
/*     */   public final int port;
/* 125 */   private boolean bound = false;
/*     */ 
/* 128 */   private OutputStream traceFile = null;
/* 129 */   private String traceTagIn = null;
/* 130 */   private String traceTagOut = null;
/*     */   public InputStream inStream;
/*     */   public OutputStream outStream;
/*     */   public Socket sock;
/*     */   private final LdapClient parent;
/* 149 */   private int outMsgId = 0;
/*     */ 
/* 155 */   private LdapRequest pendingRequests = null;
/*     */ 
/* 157 */   volatile IOException closureReason = null;
/* 158 */   volatile boolean useable = true;
/*     */   int readTimeout;
/*     */   int connectTimeout;
/* 779 */   private Object pauseLock = new Object();
/* 780 */   private boolean paused = false;
/*     */ 
/*     */   void setV3(boolean paramBoolean)
/*     */   {
/* 167 */     this.v3 = paramBoolean;
/*     */   }
/*     */ 
/*     */   void setBound()
/*     */   {
/* 175 */     this.bound = true;
/*     */   }
/*     */ 
/*     */   Connection(LdapClient paramLdapClient, String paramString1, int paramInt1, String paramString2, int paramInt2, int paramInt3, OutputStream paramOutputStream)
/*     */     throws NamingException
/*     */   {
/* 187 */     this.host = paramString1;
/* 188 */     this.port = paramInt1;
/* 189 */     this.parent = paramLdapClient;
/* 190 */     this.readTimeout = paramInt3;
/* 191 */     this.connectTimeout = paramInt2;
/*     */ 
/* 193 */     if (paramOutputStream != null) {
/* 194 */       this.traceFile = paramOutputStream;
/* 195 */       this.traceTagIn = ("<- " + paramString1 + ":" + paramInt1 + "\n\n");
/* 196 */       this.traceTagOut = ("-> " + paramString1 + ":" + paramInt1 + "\n\n");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 203 */       this.sock = createSocket(paramString1, paramInt1, paramString2, paramInt2);
/*     */ 
/* 209 */       this.inStream = new BufferedInputStream(this.sock.getInputStream());
/* 210 */       this.outStream = new BufferedOutputStream(this.sock.getOutputStream());
/*     */     }
/*     */     catch (InvocationTargetException localInvocationTargetException) {
/* 213 */       localObject = localInvocationTargetException.getTargetException();
/*     */ 
/* 216 */       CommunicationException localCommunicationException = new CommunicationException(paramString1 + ":" + paramInt1);
/*     */ 
/* 218 */       localCommunicationException.setRootCause((Throwable)localObject);
/* 219 */       throw localCommunicationException;
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 226 */       Object localObject = new CommunicationException(paramString1 + ":" + paramInt1);
/*     */ 
/* 228 */       ((CommunicationException)localObject).setRootCause(localException);
/* 229 */       throw ((Throwable)localObject);
/*     */     }
/*     */ 
/* 232 */     this.worker = Obj.helper.createThread(this);
/* 233 */     this.worker.setDaemon(true);
/* 234 */     this.worker.start();
/*     */   }
/*     */ 
/*     */   private Object createInetSocketAddress(String paramString, int paramInt)
/*     */     throws NoSuchMethodException
/*     */   {
/*     */     try
/*     */     {
/* 244 */       Class localClass = Class.forName("java.net.InetSocketAddress");
/*     */ 
/* 247 */       Constructor localConstructor = localClass.getConstructor(new Class[] { String.class, Integer.TYPE });
/*     */ 
/* 251 */       return localConstructor.newInstance(new Object[] { paramString, new Integer(paramInt) });
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException)
/*     */     {
/* 255 */       throw new NoSuchMethodException();
/*     */     }
/*     */     catch (InstantiationException localInstantiationException) {
/* 258 */       throw new NoSuchMethodException();
/*     */     }
/*     */     catch (InvocationTargetException localInvocationTargetException) {
/* 261 */       throw new NoSuchMethodException();
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/*     */     }
/* 264 */     throw new NoSuchMethodException();
/*     */   }
/*     */ 
/*     */   private Socket createSocket(String paramString1, int paramInt1, String paramString2, int paramInt2)
/*     */     throws Exception
/*     */   {
/* 280 */     Socket localSocket = null;
/*     */     Object localObject1;
/*     */     Method localMethod1;
/*     */     Object localObject2;
/* 282 */     if (paramString2 != null)
/*     */     {
/* 286 */       localObject1 = Obj.helper.loadClass(paramString2);
/* 287 */       localMethod1 = ((Class)localObject1).getMethod("getDefault", new Class[0]);
/*     */ 
/* 289 */       localObject2 = localMethod1.invoke(null, new Object[0]);
/*     */ 
/* 293 */       Method localMethod2 = null;
/*     */ 
/* 295 */       if (paramInt2 > 0) {
/*     */         try
/*     */         {
/* 298 */           localMethod2 = ((Class)localObject1).getMethod("createSocket", new Class[0]);
/*     */ 
/* 301 */           Method localMethod3 = Socket.class.getMethod("connect", new Class[] { Class.forName("java.net.SocketAddress"), Integer.TYPE });
/*     */ 
/* 304 */           Object localObject3 = createInetSocketAddress(paramString1, paramInt1);
/*     */ 
/* 307 */           localSocket = (Socket)localMethod2.invoke(localObject2, new Object[0]);
/*     */ 
/* 316 */           localMethod3.invoke(localSocket, new Object[] { localObject3, new Integer(paramInt2) });
/*     */         }
/*     */         catch (NoSuchMethodException localNoSuchMethodException2)
/*     */         {
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 324 */       if (localSocket == null) {
/* 325 */         localMethod2 = ((Class)localObject1).getMethod("createSocket", new Class[] { String.class, Integer.TYPE });
/*     */ 
/* 333 */         localSocket = (Socket)localMethod2.invoke(localObject2, new Object[] { paramString1, new Integer(paramInt1) });
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/* 338 */       if (paramInt2 > 0) {
/*     */         try
/*     */         {
/* 341 */           localObject1 = Socket.class.getConstructor(new Class[0]);
/*     */ 
/* 344 */           localMethod1 = Socket.class.getMethod("connect", new Class[] { Class.forName("java.net.SocketAddress"), Integer.TYPE });
/*     */ 
/* 347 */           localObject2 = createInetSocketAddress(paramString1, paramInt1);
/*     */ 
/* 349 */           localSocket = (Socket)((Constructor)localObject1).newInstance(new Object[0]);
/*     */ 
/* 355 */           localMethod1.invoke(localSocket, new Object[] { localObject2, new Integer(paramInt2) });
/*     */         }
/*     */         catch (NoSuchMethodException localNoSuchMethodException1)
/*     */         {
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 363 */       if (localSocket == null)
/*     */       {
/* 368 */         localSocket = new Socket(paramString1, paramInt1);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 376 */     if ((paramInt2 > 0) && ((localSocket instanceof SSLSocket))) {
/* 377 */       SSLSocket localSSLSocket = (SSLSocket)localSocket;
/* 378 */       int i = localSSLSocket.getSoTimeout();
/*     */ 
/* 380 */       localSSLSocket.setSoTimeout(paramInt2);
/* 381 */       localSSLSocket.startHandshake();
/* 382 */       localSSLSocket.setSoTimeout(i);
/*     */     }
/*     */ 
/* 385 */     return localSocket;
/*     */   }
/*     */ 
/*     */   synchronized int getMsgId()
/*     */   {
/* 395 */     return ++this.outMsgId;
/*     */   }
/*     */ 
/*     */   LdapRequest writeRequest(BerEncoder paramBerEncoder, int paramInt) throws IOException {
/* 399 */     return writeRequest(paramBerEncoder, paramInt, false, -1);
/*     */   }
/*     */ 
/*     */   LdapRequest writeRequest(BerEncoder paramBerEncoder, int paramInt, boolean paramBoolean) throws IOException
/*     */   {
/* 404 */     return writeRequest(paramBerEncoder, paramInt, paramBoolean, -1);
/*     */   }
/*     */ 
/*     */   LdapRequest writeRequest(BerEncoder paramBerEncoder, int paramInt1, boolean paramBoolean, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 410 */     LdapRequest localLdapRequest = new LdapRequest(paramInt1, paramBoolean, paramInt2);
/*     */ 
/* 412 */     addRequest(localLdapRequest);
/*     */ 
/* 414 */     if (this.traceFile != null) {
/* 415 */       Ber.dumpBER(this.traceFile, this.traceTagOut, paramBerEncoder.getBuf(), 0, paramBerEncoder.getDataLen());
/*     */     }
/*     */ 
/* 422 */     unpauseReader();
/*     */     try
/*     */     {
/* 429 */       synchronized (this) {
/* 430 */         this.outStream.write(paramBerEncoder.getBuf(), 0, paramBerEncoder.getDataLen());
/* 431 */         this.outStream.flush();
/*     */       }
/*     */     } catch (IOException localIOException) {
/* 434 */       cleanup(null, true);
/* 435 */       throw (this.closureReason = localIOException);
/*     */     }
/*     */ 
/* 438 */     return localLdapRequest;
/*     */   }
/*     */ 
/*     */   BerDecoder readReply(LdapRequest paramLdapRequest)
/*     */     throws IOException, NamingException
/*     */   {
/* 447 */     int i = 0;
/*     */     BerDecoder localBerDecoder;
/* 449 */     while (((localBerDecoder = paramLdapRequest.getReplyBer()) == null) && (i == 0)) {
/*     */       try
/*     */       {
/* 452 */         synchronized (this) {
/* 453 */           if (this.sock == null) {
/* 454 */             throw new ServiceUnavailableException(this.host + ":" + this.port + "; socket closed");
/*     */           }
/*     */         }
/*     */ 
/* 458 */         synchronized (paramLdapRequest)
/*     */         {
/* 460 */           localBerDecoder = paramLdapRequest.getReplyBer();
/* 461 */           if (localBerDecoder == null) {
/* 462 */             if (this.readTimeout > 0)
/*     */             {
/* 466 */               paramLdapRequest.wait(this.readTimeout);
/*     */             }
/* 468 */             else paramLdapRequest.wait(15000L);
/*     */ 
/* 470 */             i = 1;
/*     */           } else {
/* 472 */             break;
/*     */           }
/*     */         }
/*     */       } catch (InterruptedException localInterruptedException) {
/* 476 */         throw new InterruptedNamingException("Interrupted during LDAP operation");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 481 */     if ((localBerDecoder == null) && (i != 0)) {
/* 482 */       abandonRequest(paramLdapRequest, null);
/* 483 */       throw new NamingException("LDAP response read timed out, timeout used:" + this.readTimeout + "ms.");
/*     */     }
/*     */ 
/* 487 */     return localBerDecoder;
/*     */   }
/*     */ 
/*     */   private synchronized void addRequest(LdapRequest paramLdapRequest)
/*     */   {
/* 499 */     LdapRequest localLdapRequest = this.pendingRequests;
/* 500 */     if (localLdapRequest == null) {
/* 501 */       this.pendingRequests = paramLdapRequest;
/* 502 */       paramLdapRequest.next = null;
/*     */     } else {
/* 504 */       paramLdapRequest.next = this.pendingRequests;
/* 505 */       this.pendingRequests = paramLdapRequest;
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized LdapRequest findRequest(int paramInt)
/*     */   {
/* 511 */     LdapRequest localLdapRequest = this.pendingRequests;
/* 512 */     while (localLdapRequest != null) {
/* 513 */       if (localLdapRequest.msgId == paramInt) {
/* 514 */         return localLdapRequest;
/*     */       }
/* 516 */       localLdapRequest = localLdapRequest.next;
/*     */     }
/* 518 */     return null;
/*     */   }
/*     */ 
/*     */   synchronized void removeRequest(LdapRequest paramLdapRequest)
/*     */   {
/* 523 */     LdapRequest localLdapRequest1 = this.pendingRequests;
/* 524 */     LdapRequest localLdapRequest2 = null;
/*     */ 
/* 526 */     while (localLdapRequest1 != null) {
/* 527 */       if (localLdapRequest1 == paramLdapRequest) {
/* 528 */         localLdapRequest1.cancel();
/*     */ 
/* 530 */         if (localLdapRequest2 != null)
/* 531 */           localLdapRequest2.next = localLdapRequest1.next;
/*     */         else {
/* 533 */           this.pendingRequests = localLdapRequest1.next;
/*     */         }
/* 535 */         localLdapRequest1.next = null;
/*     */       }
/* 537 */       localLdapRequest2 = localLdapRequest1;
/* 538 */       localLdapRequest1 = localLdapRequest1.next;
/*     */     }
/*     */   }
/*     */ 
/*     */   void abandonRequest(LdapRequest paramLdapRequest, Control[] paramArrayOfControl)
/*     */   {
/* 544 */     removeRequest(paramLdapRequest);
/*     */ 
/* 546 */     BerEncoder localBerEncoder = new BerEncoder(256);
/* 547 */     int i = getMsgId();
/*     */     try
/*     */     {
/* 553 */       localBerEncoder.beginSeq(48);
/* 554 */       localBerEncoder.encodeInt(i);
/* 555 */       localBerEncoder.encodeInt(paramLdapRequest.msgId, 80);
/*     */ 
/* 557 */       if (this.v3) {
/* 558 */         LdapClient.encodeControls(localBerEncoder, paramArrayOfControl);
/*     */       }
/* 560 */       localBerEncoder.endSeq();
/*     */ 
/* 562 */       if (this.traceFile != null) {
/* 563 */         Ber.dumpBER(this.traceFile, this.traceTagOut, localBerEncoder.getBuf(), 0, localBerEncoder.getDataLen());
/*     */       }
/*     */ 
/* 567 */       synchronized (this) {
/* 568 */         this.outStream.write(localBerEncoder.getBuf(), 0, localBerEncoder.getDataLen());
/* 569 */         this.outStream.flush();
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   synchronized void abandonOutstandingReqs(Control[] paramArrayOfControl)
/*     */   {
/* 580 */     LdapRequest localLdapRequest = this.pendingRequests;
/*     */ 
/* 582 */     while (localLdapRequest != null) {
/* 583 */       abandonRequest(localLdapRequest, paramArrayOfControl);
/* 584 */       this.pendingRequests = (localLdapRequest = localLdapRequest.next);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void ldapUnbind(Control[] paramArrayOfControl)
/*     */   {
/* 597 */     BerEncoder localBerEncoder = new BerEncoder(256);
/* 598 */     int i = getMsgId();
/*     */     try
/*     */     {
/* 606 */       localBerEncoder.beginSeq(48);
/* 607 */       localBerEncoder.encodeInt(i);
/*     */ 
/* 609 */       localBerEncoder.encodeByte(66);
/* 610 */       localBerEncoder.encodeByte(0);
/*     */ 
/* 612 */       if (this.v3) {
/* 613 */         LdapClient.encodeControls(localBerEncoder, paramArrayOfControl);
/*     */       }
/* 615 */       localBerEncoder.endSeq();
/*     */ 
/* 617 */       if (this.traceFile != null) {
/* 618 */         Ber.dumpBER(this.traceFile, this.traceTagOut, localBerEncoder.getBuf(), 0, localBerEncoder.getDataLen());
/*     */       }
/*     */ 
/* 622 */       synchronized (this) {
/* 623 */         this.outStream.write(localBerEncoder.getBuf(), 0, localBerEncoder.getDataLen());
/* 624 */         this.outStream.flush();
/*     */       }
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   void cleanup(Control[] paramArrayOfControl, boolean paramBoolean)
/*     */   {
/* 645 */     boolean bool = false;
/*     */ 
/* 647 */     synchronized (this) {
/* 648 */       this.useable = false;
/*     */       LdapRequest localLdapRequest1;
/* 650 */       if (this.sock != null)
/*     */       {
/*     */         try
/*     */         {
/* 655 */           if (!paramBoolean) {
/* 656 */             abandonOutstandingReqs(paramArrayOfControl);
/*     */           }
/* 658 */           if (this.bound)
/* 659 */             ldapUnbind(paramArrayOfControl);
/*     */         }
/*     */         finally {
/*     */           try {
/* 663 */             this.outStream.flush();
/* 664 */             this.sock.close();
/* 665 */             unpauseReader();
/*     */           }
/*     */           catch (IOException localIOException2)
/*     */           {
/*     */           }
/* 670 */           if (!paramBoolean) {
/* 671 */             LdapRequest localLdapRequest2 = this.pendingRequests;
/* 672 */             while (localLdapRequest2 != null) {
/* 673 */               localLdapRequest2.cancel();
/* 674 */               localLdapRequest2 = localLdapRequest2.next;
/*     */             }
/*     */           }
/* 677 */           this.sock = null;
/*     */         }
/* 679 */         bool = paramBoolean;
/*     */       }
/* 681 */       if (bool) {
/* 682 */         localLdapRequest1 = this.pendingRequests;
/* 683 */         while (localLdapRequest1 != null)
/*     */         {
/* 685 */           synchronized (localLdapRequest1) {
/* 686 */             localLdapRequest1.notify();
/* 687 */             localLdapRequest1 = localLdapRequest1.next;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 692 */     if (bool)
/* 693 */       this.parent.processConnectionClosure();
/*     */   }
/*     */ 
/*     */   public synchronized void replaceStreams(InputStream paramInputStream, OutputStream paramOutputStream)
/*     */   {
/* 708 */     this.inStream = paramInputStream;
/*     */     try
/*     */     {
/* 712 */       this.outStream.flush();
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/*     */ 
/* 719 */     this.outStream = paramOutputStream;
/*     */   }
/*     */ 
/*     */   private synchronized InputStream getInputStream()
/*     */   {
/* 728 */     return this.inStream;
/*     */   }
/*     */ 
/*     */   private void unpauseReader()
/*     */     throws IOException
/*     */   {
/* 786 */     synchronized (this.pauseLock) {
/* 787 */       if (this.paused)
/*     */       {
/* 792 */         this.paused = false;
/* 793 */         this.pauseLock.notify();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void pauseReader()
/*     */     throws IOException
/*     */   {
/* 808 */     this.paused = true;
/*     */     try {
/* 810 */       while (this.paused)
/* 811 */         this.pauseLock.wait();
/*     */     }
/*     */     catch (InterruptedException localInterruptedException) {
/* 814 */       throw new InterruptedIOException("Pause/unpause reader has problems.");
/*     */     }
/*     */   }
/*     */ 
/*     */   // ERROR //
/*     */   public void run()
/*     */   {
/*     */     // Byte code:
/*     */     //   0: aconst_null
/*     */     //   1: astore 10
/*     */     //   3: sipush 129
/*     */     //   6: newarray byte
/*     */     //   8: astore_1
/*     */     //   9: iconst_0
/*     */     //   10: istore 5
/*     */     //   12: iconst_0
/*     */     //   13: istore 6
/*     */     //   15: iconst_0
/*     */     //   16: istore 7
/*     */     //   18: aload_0
/*     */     //   19: invokespecial 408	com/sun/jndi/ldap/Connection:getInputStream	()Ljava/io/InputStream;
/*     */     //   22: astore 10
/*     */     //   24: aload 10
/*     */     //   26: aload_1
/*     */     //   27: iload 5
/*     */     //   29: iconst_1
/*     */     //   30: invokevirtual 427	java/io/InputStream:read	([BII)I
/*     */     //   33: istore_3
/*     */     //   34: iload_3
/*     */     //   35: ifge +18 -> 53
/*     */     //   38: aload 10
/*     */     //   40: aload_0
/*     */     //   41: invokespecial 408	com/sun/jndi/ldap/Connection:getInputStream	()Ljava/io/InputStream;
/*     */     //   44: if_acmpeq +6 -> 50
/*     */     //   47: goto -44 -> 3
/*     */     //   50: goto +354 -> 404
/*     */     //   53: aload_1
/*     */     //   54: iload 5
/*     */     //   56: iinc 5 1
/*     */     //   59: baload
/*     */     //   60: bipush 48
/*     */     //   62: if_icmpeq +6 -> 68
/*     */     //   65: goto -62 -> 3
/*     */     //   68: aload 10
/*     */     //   70: aload_1
/*     */     //   71: iload 5
/*     */     //   73: iconst_1
/*     */     //   74: invokevirtual 427	java/io/InputStream:read	([BII)I
/*     */     //   77: istore_3
/*     */     //   78: iload_3
/*     */     //   79: ifge +6 -> 85
/*     */     //   82: goto +322 -> 404
/*     */     //   85: aload_1
/*     */     //   86: iload 5
/*     */     //   88: iinc 5 1
/*     */     //   91: baload
/*     */     //   92: istore 6
/*     */     //   94: iload 6
/*     */     //   96: sipush 128
/*     */     //   99: iand
/*     */     //   100: sipush 128
/*     */     //   103: if_icmpne +108 -> 211
/*     */     //   106: iload 6
/*     */     //   108: bipush 127
/*     */     //   110: iand
/*     */     //   111: istore 7
/*     */     //   113: iconst_0
/*     */     //   114: istore_3
/*     */     //   115: iconst_0
/*     */     //   116: istore 8
/*     */     //   118: iload_3
/*     */     //   119: iload 7
/*     */     //   121: if_icmpge +38 -> 159
/*     */     //   124: aload 10
/*     */     //   126: aload_1
/*     */     //   127: iload 5
/*     */     //   129: iload_3
/*     */     //   130: iadd
/*     */     //   131: iload 7
/*     */     //   133: iload_3
/*     */     //   134: isub
/*     */     //   135: invokevirtual 427	java/io/InputStream:read	([BII)I
/*     */     //   138: istore 4
/*     */     //   140: iload 4
/*     */     //   142: ifge +9 -> 151
/*     */     //   145: iconst_1
/*     */     //   146: istore 8
/*     */     //   148: goto +11 -> 159
/*     */     //   151: iload_3
/*     */     //   152: iload 4
/*     */     //   154: iadd
/*     */     //   155: istore_3
/*     */     //   156: goto -38 -> 118
/*     */     //   159: iload 8
/*     */     //   161: ifeq +6 -> 167
/*     */     //   164: goto +240 -> 404
/*     */     //   167: iconst_0
/*     */     //   168: istore 6
/*     */     //   170: iconst_0
/*     */     //   171: istore 11
/*     */     //   173: iload 11
/*     */     //   175: iload 7
/*     */     //   177: if_icmpge +28 -> 205
/*     */     //   180: iload 6
/*     */     //   182: bipush 8
/*     */     //   184: ishl
/*     */     //   185: aload_1
/*     */     //   186: iload 5
/*     */     //   188: iload 11
/*     */     //   190: iadd
/*     */     //   191: baload
/*     */     //   192: sipush 255
/*     */     //   195: iand
/*     */     //   196: iadd
/*     */     //   197: istore 6
/*     */     //   199: iinc 11 1
/*     */     //   202: goto -29 -> 173
/*     */     //   205: iload 5
/*     */     //   207: iload_3
/*     */     //   208: iadd
/*     */     //   209: istore 5
/*     */     //   211: aload 10
/*     */     //   213: iload 6
/*     */     //   215: iconst_0
/*     */     //   216: invokestatic 463	sun/misc/IOUtils:readFully	(Ljava/io/InputStream;IZ)[B
/*     */     //   219: astore 11
/*     */     //   221: aload_1
/*     */     //   222: iload 5
/*     */     //   224: aload 11
/*     */     //   226: arraylength
/*     */     //   227: iadd
/*     */     //   228: invokestatic 454	java/util/Arrays:copyOf	([BI)[B
/*     */     //   231: astore_1
/*     */     //   232: aload 11
/*     */     //   234: iconst_0
/*     */     //   235: aload_1
/*     */     //   236: iload 5
/*     */     //   238: aload 11
/*     */     //   240: arraylength
/*     */     //   241: invokestatic 444	java/lang/System:arraycopy	(Ljava/lang/Object;ILjava/lang/Object;II)V
/*     */     //   244: iload 5
/*     */     //   246: aload 11
/*     */     //   248: arraylength
/*     */     //   249: iadd
/*     */     //   250: istore 5
/*     */     //   252: new 197	com/sun/jndi/ldap/BerDecoder
/*     */     //   255: dup
/*     */     //   256: aload_1
/*     */     //   257: iconst_0
/*     */     //   258: iload 5
/*     */     //   260: invokespecial 392	com/sun/jndi/ldap/BerDecoder:<init>	([BII)V
/*     */     //   263: astore 9
/*     */     //   265: aload_0
/*     */     //   266: getfield 378	com/sun/jndi/ldap/Connection:traceFile	Ljava/io/OutputStream;
/*     */     //   269: ifnull +18 -> 287
/*     */     //   272: aload_0
/*     */     //   273: getfield 378	com/sun/jndi/ldap/Connection:traceFile	Ljava/io/OutputStream;
/*     */     //   276: aload_0
/*     */     //   277: getfield 381	com/sun/jndi/ldap/Connection:traceTagIn	Ljava/lang/String;
/*     */     //   280: aload_1
/*     */     //   281: iconst_0
/*     */     //   282: iload 5
/*     */     //   284: invokestatic 389	com/sun/jndi/ldap/Ber:dumpBER	(Ljava/io/OutputStream;Ljava/lang/String;[BII)V
/*     */     //   287: aload 9
/*     */     //   289: aconst_null
/*     */     //   290: invokevirtual 393	com/sun/jndi/ldap/BerDecoder:parseSeq	([I)I
/*     */     //   293: pop
/*     */     //   294: aload 9
/*     */     //   296: invokevirtual 390	com/sun/jndi/ldap/BerDecoder:parseInt	()I
/*     */     //   299: istore_2
/*     */     //   300: aload 9
/*     */     //   302: invokevirtual 391	com/sun/jndi/ldap/BerDecoder:reset	()V
/*     */     //   305: iconst_0
/*     */     //   306: istore 12
/*     */     //   308: iload_2
/*     */     //   309: ifne +15 -> 324
/*     */     //   312: aload_0
/*     */     //   313: getfield 373	com/sun/jndi/ldap/Connection:parent	Lcom/sun/jndi/ldap/LdapClient;
/*     */     //   316: aload 9
/*     */     //   318: invokevirtual 417	com/sun/jndi/ldap/LdapClient:processUnsolicited	(Lcom/sun/jndi/ldap/BerDecoder;)V
/*     */     //   321: goto +55 -> 376
/*     */     //   324: aload_0
/*     */     //   325: iload_2
/*     */     //   326: invokevirtual 405	com/sun/jndi/ldap/Connection:findRequest	(I)Lcom/sun/jndi/ldap/LdapRequest;
/*     */     //   329: astore 13
/*     */     //   331: aload 13
/*     */     //   333: ifnull +43 -> 376
/*     */     //   336: aload_0
/*     */     //   337: getfield 379	com/sun/jndi/ldap/Connection:pauseLock	Ljava/lang/Object;
/*     */     //   340: dup
/*     */     //   341: astore 14
/*     */     //   343: monitorenter
/*     */     //   344: aload 13
/*     */     //   346: aload 9
/*     */     //   348: invokevirtual 422	com/sun/jndi/ldap/LdapRequest:addReplyBer	(Lcom/sun/jndi/ldap/BerDecoder;)Z
/*     */     //   351: istore 12
/*     */     //   353: iload 12
/*     */     //   355: ifeq +7 -> 362
/*     */     //   358: aload_0
/*     */     //   359: invokespecial 403	com/sun/jndi/ldap/Connection:pauseReader	()V
/*     */     //   362: aload 14
/*     */     //   364: monitorexit
/*     */     //   365: goto +11 -> 376
/*     */     //   368: astore 15
/*     */     //   370: aload 14
/*     */     //   372: monitorexit
/*     */     //   373: aload 15
/*     */     //   375: athrow
/*     */     //   376: goto +5 -> 381
/*     */     //   379: astore 12
/*     */     //   381: goto -378 -> 3
/*     */     //   384: astore 11
/*     */     //   386: aload 10
/*     */     //   388: aload_0
/*     */     //   389: invokespecial 408	com/sun/jndi/ldap/Connection:getInputStream	()Ljava/io/InputStream;
/*     */     //   392: if_acmpeq +6 -> 398
/*     */     //   395: goto +6 -> 401
/*     */     //   398: aload 11
/*     */     //   400: athrow
/*     */     //   401: goto -398 -> 3
/*     */     //   404: aload_0
/*     */     //   405: aconst_null
/*     */     //   406: iconst_1
/*     */     //   407: invokevirtual 411	com/sun/jndi/ldap/Connection:cleanup	([Ljavax/naming/ldap/Control;Z)V
/*     */     //   410: goto +31 -> 441
/*     */     //   413: astore 11
/*     */     //   415: aload_0
/*     */     //   416: aload 11
/*     */     //   418: putfield 375	com/sun/jndi/ldap/Connection:closureReason	Ljava/io/IOException;
/*     */     //   421: aload_0
/*     */     //   422: aconst_null
/*     */     //   423: iconst_1
/*     */     //   424: invokevirtual 411	com/sun/jndi/ldap/Connection:cleanup	([Ljavax/naming/ldap/Control;Z)V
/*     */     //   427: goto +14 -> 441
/*     */     //   430: astore 16
/*     */     //   432: aload_0
/*     */     //   433: aconst_null
/*     */     //   434: iconst_1
/*     */     //   435: invokevirtual 411	com/sun/jndi/ldap/Connection:cleanup	([Ljavax/naming/ldap/Control;Z)V
/*     */     //   438: aload 16
/*     */     //   440: athrow
/*     */     //   441: return
/*     */     //
/*     */     // Exception table:
/*     */     //   from	to	target	type
/*     */     //   344	365	368	finally
/*     */     //   368	373	368	finally
/*     */     //   252	376	379	com/sun/jndi/ldap/Ber$DecodeException
/*     */     //   3	47	384	java/io/IOException
/*     */     //   53	65	384	java/io/IOException
/*     */     //   68	82	384	java/io/IOException
/*     */     //   85	164	384	java/io/IOException
/*     */     //   167	381	384	java/io/IOException
/*     */     //   3	404	413	java/io/IOException
/*     */     //   3	404	430	finally
/*     */     //   413	421	430	finally
/*     */     //   430	432	430	finally
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.ldap.Connection
 * JD-Core Version:    0.6.2
 */