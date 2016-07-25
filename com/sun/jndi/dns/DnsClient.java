/*     */ package com.sun.jndi.dns;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketException;
/*     */ import java.net.UnknownHostException;
/*     */ import java.security.SecureRandom;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Vector;
/*     */ import javax.naming.CommunicationException;
/*     */ import javax.naming.ConfigurationException;
/*     */ import javax.naming.NameNotFoundException;
/*     */ import javax.naming.NamingException;
/*     */ import javax.naming.OperationNotSupportedException;
/*     */ import javax.naming.ServiceUnavailableException;
/*     */ import sun.security.jca.JCAUtil;
/*     */ 
/*     */ public class DnsClient
/*     */ {
/*     */   private static final int IDENT_OFFSET = 0;
/*     */   private static final int FLAGS_OFFSET = 2;
/*     */   private static final int NUMQ_OFFSET = 4;
/*     */   private static final int NUMANS_OFFSET = 6;
/*     */   private static final int NUMAUTH_OFFSET = 8;
/*     */   private static final int NUMADD_OFFSET = 10;
/*     */   private static final int DNS_HDR_SIZE = 12;
/*     */   private static final int NO_ERROR = 0;
/*     */   private static final int FORMAT_ERROR = 1;
/*     */   private static final int SERVER_FAILURE = 2;
/*     */   private static final int NAME_ERROR = 3;
/*     */   private static final int NOT_IMPL = 4;
/*     */   private static final int REFUSED = 5;
/*  71 */   private static final String[] rcodeDescription = { "No error", "DNS format error", "DNS server failure", "DNS name not found", "DNS operation not supported", "DNS service refused" };
/*     */   private static final int DEFAULT_PORT = 53;
/*     */   private static final int TRANSACTION_ID_BOUND = 65536;
/*  82 */   private static final SecureRandom random = JCAUtil.getSecureRandom();
/*     */   private InetAddress[] servers;
/*     */   private int[] serverPorts;
/*     */   private int timeout;
/*     */   private int retries;
/*     */   private DatagramSocket udpSocket;
/*     */   private Map<Integer, ResourceRecord> reqs;
/*     */   private Map<Integer, byte[]> resps;
/* 150 */   private Object queuesLock = new Object();
/*     */   private static final boolean debug = false;
/*     */ 
/*     */   public DnsClient(String[] paramArrayOfString, int paramInt1, int paramInt2)
/*     */     throws NamingException
/*     */   {
/* 106 */     this.timeout = paramInt1;
/* 107 */     this.retries = paramInt2;
/*     */     try {
/* 109 */       this.udpSocket = new DatagramSocket();
/*     */     } catch (SocketException localSocketException) {
/* 111 */       ConfigurationException localConfigurationException1 = new ConfigurationException();
/* 112 */       localConfigurationException1.setRootCause(localSocketException);
/* 113 */       throw localConfigurationException1;
/*     */     }
/*     */ 
/* 116 */     this.servers = new InetAddress[paramArrayOfString.length];
/* 117 */     this.serverPorts = new int[paramArrayOfString.length];
/*     */ 
/* 119 */     for (int i = 0; i < paramArrayOfString.length; i++)
/*     */     {
/* 122 */       int j = paramArrayOfString[i].indexOf(':', paramArrayOfString[i].indexOf(93) + 1);
/*     */ 
/* 125 */       this.serverPorts[i] = (j < 0 ? 53 : Integer.parseInt(paramArrayOfString[i].substring(j + 1)));
/*     */ 
/* 128 */       String str = j < 0 ? paramArrayOfString[i] : paramArrayOfString[i].substring(0, j);
/*     */       try
/*     */       {
/* 132 */         this.servers[i] = InetAddress.getByName(str);
/*     */       } catch (UnknownHostException localUnknownHostException) {
/* 134 */         ConfigurationException localConfigurationException2 = new ConfigurationException("Unknown DNS server: " + str);
/*     */ 
/* 136 */         localConfigurationException2.setRootCause(localUnknownHostException);
/* 137 */         throw localConfigurationException2;
/*     */       }
/*     */     }
/* 140 */     this.reqs = Collections.synchronizedMap(new HashMap());
/*     */ 
/* 142 */     this.resps = Collections.synchronizedMap(new HashMap());
/*     */   }
/*     */ 
/*     */   protected void finalize() {
/* 146 */     close();
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 153 */     this.udpSocket.close();
/* 154 */     synchronized (this.queuesLock) {
/* 155 */       this.reqs.clear();
/* 156 */       this.resps.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   ResourceRecords query(DnsName paramDnsName, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
/*     */     throws NamingException
/*     */   {
/*     */     int i;
/*     */     Packet localPacket;
/*     */     ResourceRecord localResourceRecord;
/*     */     do
/*     */     {
/* 175 */       i = random.nextInt(65536);
/* 176 */       localPacket = makeQueryPacket(paramDnsName, i, paramInt1, paramInt2, paramBoolean1);
/*     */ 
/* 179 */       synchronized (this.reqs) {
/* 180 */         if ((localResourceRecord = (ResourceRecord)this.reqs.put(Integer.valueOf(i), new ResourceRecord(localPacket.getData(), localPacket.length(), 12, true, false))) != null)
/*     */         {
/* 182 */           this.reqs.put(Integer.valueOf(i), localResourceRecord);
/*     */         }
/*     */       }
/*     */     }
/* 185 */     while (localResourceRecord != null);
/*     */ 
/* 187 */     ??? = null;
/* 188 */     boolean[] arrayOfBoolean = new boolean[this.servers.length];
/*     */ 
/* 195 */     for (int j = 0; j < this.retries; j++)
/*     */     {
/* 198 */       for (int k = 0; k < this.servers.length; k++) {
/* 199 */         if (arrayOfBoolean[k] == 0)
/*     */         {
/*     */           try
/*     */           {
/* 209 */             Object localObject2 = null;
/* 210 */             localObject2 = doUdpQuery(localPacket, this.servers[k], this.serverPorts[k], j, i);
/*     */ 
/* 218 */             if (localObject2 == null) {
/* 219 */               if (this.resps.size() > 0) {
/* 220 */                 localObject2 = lookupResponse(Integer.valueOf(i));
/*     */               }
/* 222 */               if (localObject2 == null);
/*     */             }
/*     */             else
/*     */             {
/* 226 */               Object localObject3 = new Header((byte[])localObject2, localObject2.length);
/*     */ 
/* 228 */               if ((paramBoolean2) && (!((Header)localObject3).authoritative)) {
/* 229 */                 ??? = new NameNotFoundException("DNS response not authoritative");
/*     */ 
/* 231 */                 arrayOfBoolean[k] = true;
/*     */               }
/*     */               else {
/* 234 */                 if (((Header)localObject3).truncated)
/*     */                 {
/* 238 */                   for (int m = 0; m < this.servers.length; m++) {
/* 239 */                     int n = (k + m) % this.servers.length;
/* 240 */                     if (arrayOfBoolean[n] == 0)
/*     */                     {
/*     */                       try
/*     */                       {
/* 244 */                         Tcp localTcp = new Tcp(this.servers[n], this.serverPorts[n]);
/*     */                         byte[] arrayOfByte;
/*     */                         try {
/* 248 */                           arrayOfByte = doTcpQuery(localTcp, localPacket);
/*     */                         } finally {
/* 250 */                           localTcp.close();
/*     */                         }
/* 252 */                         Header localHeader = new Header(arrayOfByte, arrayOfByte.length);
/* 253 */                         if (localHeader.query) {
/* 254 */                           throw new CommunicationException("DNS error: expecting response");
/*     */                         }
/*     */ 
/* 257 */                         checkResponseCode(localHeader);
/*     */ 
/* 259 */                         if ((!paramBoolean2) || (localHeader.authoritative))
/*     */                         {
/* 261 */                           localObject3 = localHeader;
/* 262 */                           localObject2 = arrayOfByte;
/* 263 */                           break;
/*     */                         }
/* 265 */                         arrayOfBoolean[n] = true;
/*     */                       }
/*     */                       catch (Exception localException) {
/*     */                       }
/*     */                     }
/*     */                   }
/*     */                 }
/* 272 */                 return new ResourceRecords((byte[])localObject2, localObject2.length, (Header)localObject3, false);
/*     */               }
/*     */             }
/*     */           }
/*     */           catch (IOException localIOException)
/*     */           {
/* 278 */             if (??? == null) {
/* 279 */               ??? = localIOException;
/*     */             }
/*     */ 
/* 283 */             if (localIOException.getClass().getName().equals("java.net.PortUnreachableException"))
/*     */             {
/* 285 */               arrayOfBoolean[k] = true;
/*     */             }
/*     */           } catch (NameNotFoundException localNameNotFoundException) {
/* 288 */             throw localNameNotFoundException;
/*     */           } catch (CommunicationException localCommunicationException2) {
/* 290 */             if (??? == null)
/* 291 */               ??? = localCommunicationException2;
/*     */           }
/*     */           catch (NamingException localNamingException) {
/* 294 */             if (??? == null) {
/* 295 */               ??? = localNamingException;
/*     */             }
/* 297 */             arrayOfBoolean[k] = true;
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/* 302 */     this.reqs.remove(Integer.valueOf(i));
/* 303 */     if ((??? instanceof NamingException)) {
/* 304 */       throw ((NamingException)???);
/*     */     }
/*     */ 
/* 307 */     CommunicationException localCommunicationException1 = new CommunicationException("DNS error");
/* 308 */     localCommunicationException1.setRootCause((Throwable)???);
/* 309 */     throw localCommunicationException1;
/*     */   }
/*     */ 
/*     */   ResourceRecords queryZone(DnsName paramDnsName, int paramInt, boolean paramBoolean)
/*     */     throws NamingException
/*     */   {
/* 315 */     int i = random.nextInt(65536);
/*     */ 
/* 317 */     Packet localPacket = makeQueryPacket(paramDnsName, i, paramInt, 252, paramBoolean);
/*     */ 
/* 319 */     Object localObject1 = null;
/*     */ 
/* 322 */     for (int j = 0; j < this.servers.length; j++) {
/*     */       try {
/* 324 */         Tcp localTcp = new Tcp(this.servers[j], this.serverPorts[j]);
/*     */         try
/*     */         {
/* 327 */           byte[] arrayOfByte = doTcpQuery(localTcp, localPacket);
/* 328 */           Header localHeader = new Header(arrayOfByte, arrayOfByte.length);
/*     */ 
/* 331 */           checkResponseCode(localHeader);
/* 332 */           ResourceRecords localResourceRecords1 = new ResourceRecords(arrayOfByte, arrayOfByte.length, localHeader, true);
/*     */ 
/* 334 */           if (localResourceRecords1.getFirstAnsType() != 6) {
/* 335 */             throw new CommunicationException("DNS error: zone xfer doesn't begin with SOA");
/*     */           }
/*     */ 
/* 339 */           if ((localResourceRecords1.answer.size() == 1) || (localResourceRecords1.getLastAnsType() != 6))
/*     */           {
/*     */             do
/*     */             {
/* 343 */               arrayOfByte = continueTcpQuery(localTcp);
/* 344 */               if (arrayOfByte == null) {
/* 345 */                 throw new CommunicationException("DNS error: incomplete zone transfer");
/*     */               }
/*     */ 
/* 348 */               localHeader = new Header(arrayOfByte, arrayOfByte.length);
/* 349 */               checkResponseCode(localHeader);
/* 350 */               localResourceRecords1.add(arrayOfByte, arrayOfByte.length, localHeader);
/* 351 */             }while (localResourceRecords1.getLastAnsType() != 6);
/*     */           }
/*     */ 
/* 356 */           localResourceRecords1.answer.removeElementAt(localResourceRecords1.answer.size() - 1);
/* 357 */           return localResourceRecords1;
/*     */         }
/*     */         finally {
/* 360 */           localTcp.close();
/*     */         }
/*     */       }
/*     */       catch (IOException localIOException) {
/* 364 */         localObject1 = localIOException;
/*     */       } catch (NameNotFoundException localNameNotFoundException) {
/* 366 */         throw localNameNotFoundException;
/*     */       } catch (NamingException localNamingException) {
/* 368 */         localObject1 = localNamingException;
/*     */       }
/*     */     }
/* 371 */     if ((localObject1 instanceof NamingException)) {
/* 372 */       throw ((NamingException)localObject1);
/*     */     }
/* 374 */     CommunicationException localCommunicationException = new CommunicationException("DNS error during zone transfer");
/*     */ 
/* 376 */     localCommunicationException.setRootCause(localObject1);
/* 377 */     throw localCommunicationException;
/*     */   }
/*     */ 
/*     */   private byte[] doUdpQuery(Packet paramPacket, InetAddress paramInetAddress, int paramInt1, int paramInt2, int paramInt3)
/*     */     throws IOException, NamingException
/*     */   {
/* 391 */     int i = 50;
/*     */ 
/* 393 */     synchronized (this.udpSocket) {
/* 394 */       DatagramPacket localDatagramPacket1 = new DatagramPacket(paramPacket.getData(), paramPacket.length(), paramInetAddress, paramInt1);
/*     */ 
/* 396 */       DatagramPacket localDatagramPacket2 = new DatagramPacket(new byte[8000], 8000);
/*     */ 
/* 398 */       this.udpSocket.connect(paramInetAddress, paramInt1);
/* 399 */       int j = this.timeout * (1 << paramInt2);
/*     */       try {
/* 401 */         this.udpSocket.send(localDatagramPacket1);
/*     */ 
/* 404 */         int k = j;
/* 405 */         int m = 0;
/*     */         do
/*     */         {
/* 414 */           this.udpSocket.setSoTimeout(k);
/* 415 */           long l1 = System.currentTimeMillis();
/* 416 */           this.udpSocket.receive(localDatagramPacket2);
/* 417 */           long l2 = System.currentTimeMillis();
/*     */ 
/* 419 */           byte[] arrayOfByte1 = new byte[localDatagramPacket2.getLength()];
/* 420 */           arrayOfByte1 = localDatagramPacket2.getData();
/* 421 */           if (isMatchResponse(arrayOfByte1, paramInt3)) {
/* 422 */             byte[] arrayOfByte2 = arrayOfByte1;
/*     */ 
/* 428 */             this.udpSocket.disconnect(); return arrayOfByte2;
/*     */           }
/* 424 */           k = j - (int)(l2 - l1);
/* 425 */         }while (k > i);
/*     */       }
/*     */       finally {
/* 428 */         this.udpSocket.disconnect();
/*     */       }
/* 430 */       return null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private byte[] doTcpQuery(Tcp paramTcp, Packet paramPacket)
/*     */     throws IOException
/*     */   {
/* 439 */     int i = paramPacket.length();
/*     */ 
/* 441 */     paramTcp.out.write(i >> 8);
/* 442 */     paramTcp.out.write(i);
/* 443 */     paramTcp.out.write(paramPacket.getData(), 0, i);
/* 444 */     paramTcp.out.flush();
/*     */ 
/* 446 */     byte[] arrayOfByte = continueTcpQuery(paramTcp);
/* 447 */     if (arrayOfByte == null) {
/* 448 */       throw new IOException("DNS error: no response");
/*     */     }
/* 450 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   private byte[] continueTcpQuery(Tcp paramTcp)
/*     */     throws IOException
/*     */   {
/* 458 */     int i = paramTcp.in.read();
/* 459 */     if (i == -1) {
/* 460 */       return null;
/*     */     }
/* 462 */     int j = paramTcp.in.read();
/* 463 */     if (j == -1) {
/* 464 */       throw new IOException("Corrupted DNS response: bad length");
/*     */     }
/* 466 */     int k = i << 8 | j;
/* 467 */     byte[] arrayOfByte = new byte[k];
/* 468 */     int m = 0;
/* 469 */     while (k > 0) {
/* 470 */       int n = paramTcp.in.read(arrayOfByte, m, k);
/* 471 */       if (n == -1) {
/* 472 */         throw new IOException("Corrupted DNS response: too little data");
/*     */       }
/*     */ 
/* 475 */       k -= n;
/* 476 */       m += n;
/*     */     }
/* 478 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   private Packet makeQueryPacket(DnsName paramDnsName, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean)
/*     */   {
/* 483 */     int i = paramDnsName.getOctets();
/* 484 */     int j = 12 + i + 4;
/* 485 */     Packet localPacket = new Packet(j);
/*     */ 
/* 487 */     int k = paramBoolean ? 256 : 0;
/*     */ 
/* 489 */     localPacket.putShort(paramInt1, 0);
/* 490 */     localPacket.putShort(k, 2);
/* 491 */     localPacket.putShort(1, 4);
/* 492 */     localPacket.putShort(0, 6);
/* 493 */     localPacket.putInt(0, 8);
/*     */ 
/* 495 */     makeQueryName(paramDnsName, localPacket, 12);
/* 496 */     localPacket.putShort(paramInt3, 12 + i);
/* 497 */     localPacket.putShort(paramInt2, 12 + i + 2);
/*     */ 
/* 499 */     return localPacket;
/*     */   }
/*     */ 
/*     */   private void makeQueryName(DnsName paramDnsName, Packet paramPacket, int paramInt)
/*     */   {
/* 506 */     for (int i = paramDnsName.size() - 1; i >= 0; i--) {
/* 507 */       String str = paramDnsName.get(i);
/* 508 */       int j = str.length();
/*     */ 
/* 510 */       paramPacket.putByte(j, paramInt++);
/* 511 */       for (int k = 0; k < j; k++) {
/* 512 */         paramPacket.putByte(str.charAt(k), paramInt++);
/*     */       }
/*     */     }
/* 515 */     if (!paramDnsName.hasRootLabel())
/* 516 */       paramPacket.putByte(0, paramInt);
/*     */   }
/*     */ 
/*     */   private byte[] lookupResponse(Integer paramInteger)
/*     */     throws NamingException
/*     */   {
/*     */     byte[] arrayOfByte;
/* 532 */     if ((arrayOfByte = (byte[])this.resps.get(paramInteger)) != null) {
/* 533 */       checkResponseCode(new Header(arrayOfByte, arrayOfByte.length));
/* 534 */       synchronized (this.queuesLock) {
/* 535 */         this.resps.remove(paramInteger);
/* 536 */         this.reqs.remove(paramInteger);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 544 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   private boolean isMatchResponse(byte[] paramArrayOfByte, int paramInt)
/*     */     throws NamingException
/*     */   {
/* 558 */     Header localHeader = new Header(paramArrayOfByte, paramArrayOfByte.length);
/* 559 */     if (localHeader.query) {
/* 560 */       throw new CommunicationException("DNS error: expecting response");
/*     */     }
/*     */ 
/* 563 */     if (!this.reqs.containsKey(Integer.valueOf(paramInt))) {
/* 564 */       return false;
/*     */     }
/*     */ 
/* 568 */     if (localHeader.xid == paramInt)
/*     */     {
/* 572 */       checkResponseCode(localHeader);
/* 573 */       if ((!localHeader.query) && (localHeader.numQuestions == 1))
/*     */       {
/* 575 */         ResourceRecord localResourceRecord1 = new ResourceRecord(paramArrayOfByte, paramArrayOfByte.length, 12, true, false);
/*     */ 
/* 579 */         ResourceRecord localResourceRecord2 = (ResourceRecord)this.reqs.get(Integer.valueOf(paramInt));
/* 580 */         int i = localResourceRecord2.getType();
/* 581 */         int j = localResourceRecord2.getRrclass();
/* 582 */         DnsName localDnsName = localResourceRecord2.getName();
/*     */ 
/* 586 */         if (((i == 255) || (i == localResourceRecord1.getType())) && ((j == 255) || (j == localResourceRecord1.getRrclass())) && (localDnsName.equals(localResourceRecord1.getName())))
/*     */         {
/* 599 */           synchronized (this.queuesLock) {
/* 600 */             this.resps.remove(Integer.valueOf(paramInt));
/* 601 */             this.reqs.remove(Integer.valueOf(paramInt));
/*     */           }
/* 603 */           return true;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 612 */       return false;
/*     */     }
/*     */ 
/* 620 */     synchronized (this.queuesLock) {
/* 621 */       if (this.reqs.containsKey(Integer.valueOf(localHeader.xid))) {
/* 622 */         this.resps.put(Integer.valueOf(localHeader.xid), paramArrayOfByte);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 632 */     return false;
/*     */   }
/*     */ 
/*     */   private void checkResponseCode(Header paramHeader)
/*     */     throws NamingException
/*     */   {
/* 641 */     int i = paramHeader.rcode;
/* 642 */     if (i == 0) {
/* 643 */       return;
/*     */     }
/* 645 */     String str = i < rcodeDescription.length ? rcodeDescription[i] : "DNS error";
/*     */ 
/* 648 */     str = str + " [response code " + i + "]";
/*     */ 
/* 650 */     switch (i) {
/*     */     case 2:
/* 652 */       throw new ServiceUnavailableException(str);
/*     */     case 3:
/* 654 */       throw new NameNotFoundException(str);
/*     */     case 4:
/*     */     case 5:
/* 657 */       throw new OperationNotSupportedException(str);
/*     */     case 1:
/*     */     }
/* 660 */     throw new NamingException(str);
/*     */   }
/*     */ 
/*     */   private static void dprint(String paramString)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jndi.dns.DnsClient
 * JD-Core Version:    0.6.2
 */