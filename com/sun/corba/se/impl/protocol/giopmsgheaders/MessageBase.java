/*     */ package com.sun.corba.se.impl.protocol.giopmsgheaders;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.ByteBufferWithInfo;
/*     */ import com.sun.corba.se.impl.encoding.CDRInputStream_1_0;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.impl.protocol.AddressingDispositionException;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.ObjectKey;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyFactory;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPFactories;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
/*     */ import com.sun.corba.se.spi.ior.iiop.RequestPartitioningComponent;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.servicecontext.ServiceContexts;
/*     */ import com.sun.corba.se.spi.transport.CorbaConnection;
/*     */ import com.sun.corba.se.spi.transport.ReadTimeouts;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.nio.Buffer;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.util.Iterator;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.Principal;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.IOP.TaggedProfile;
/*     */ import sun.corba.JavaCorbaAccess;
/*     */ import sun.corba.SharedSecrets;
/*     */ 
/*     */ public abstract class MessageBase
/*     */   implements Message
/*     */ {
/*     */   public byte[] giopHeader;
/*     */   private ByteBuffer byteBuffer;
/*     */   private int threadPoolToUse;
/*  85 */   byte encodingVersion = 0;
/*     */ 
/*  87 */   private static ORBUtilSystemException wrapper = ORBUtilSystemException.get("rpc.protocol");
/*     */ 
/*     */   public static String typeToString(int paramInt)
/*     */   {
/*  94 */     return typeToString((byte)paramInt);
/*     */   }
/*     */ 
/*     */   public static String typeToString(byte paramByte)
/*     */   {
/*  99 */     String str = paramByte + "/";
/* 100 */     switch (paramByte) { case 0:
/* 101 */       str = str + "GIOPRequest"; break;
/*     */     case 1:
/* 102 */       str = str + "GIOPReply"; break;
/*     */     case 2:
/* 103 */       str = str + "GIOPCancelRequest"; break;
/*     */     case 3:
/* 104 */       str = str + "GIOPLocateRequest"; break;
/*     */     case 4:
/* 105 */       str = str + "GIOPLocateReply"; break;
/*     */     case 5:
/* 106 */       str = str + "GIOPCloseConnection"; break;
/*     */     case 6:
/* 107 */       str = str + "GIOPMessageError"; break;
/*     */     case 7:
/* 108 */       str = str + "GIOPFragment"; break;
/*     */     default:
/* 109 */       str = str + "Unknown";
/*     */     }
/* 111 */     return str;
/*     */   }
/*     */ 
/*     */   public static MessageBase readGIOPMessage(ORB paramORB, CorbaConnection paramCorbaConnection)
/*     */   {
/* 116 */     MessageBase localMessageBase = readGIOPHeader(paramORB, paramCorbaConnection);
/* 117 */     localMessageBase = (MessageBase)readGIOPBody(paramORB, paramCorbaConnection, localMessageBase);
/* 118 */     return localMessageBase;
/*     */   }
/*     */ 
/*     */   public static MessageBase readGIOPHeader(ORB paramORB, CorbaConnection paramCorbaConnection)
/*     */   {
/* 123 */     Object localObject1 = null;
/* 124 */     ReadTimeouts localReadTimeouts = paramORB.getORBData().getTransportTCPReadTimeouts();
/*     */ 
/* 127 */     ByteBuffer localByteBuffer1 = null;
/*     */     try
/*     */     {
/* 130 */       localByteBuffer1 = paramCorbaConnection.read(12, 0, 12, localReadTimeouts.get_max_giop_header_time_to_wait());
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 134 */       throw wrapper.ioexceptionWhenReadingConnection(localIOException);
/*     */     }
/*     */ 
/* 137 */     if (paramORB.giopDebugFlag)
/*     */     {
/* 142 */       dprint(".readGIOPHeader: " + typeToString(localByteBuffer1.get(7)));
/* 143 */       dprint(".readGIOPHeader: GIOP header is: ");
/* 144 */       ByteBuffer localByteBuffer2 = localByteBuffer1.asReadOnlyBuffer();
/* 145 */       localByteBuffer2.position(0).limit(12);
/* 146 */       ByteBufferWithInfo localByteBufferWithInfo = new ByteBufferWithInfo(paramORB, localByteBuffer2);
/* 147 */       localByteBufferWithInfo.buflen = 12;
/* 148 */       CDRInputStream_1_0.printBuffer(localByteBufferWithInfo);
/*     */     }
/*     */ 
/* 165 */     int i = localByteBuffer1.get(0) << 24 & 0xFF000000;
/* 166 */     int j = localByteBuffer1.get(1) << 16 & 0xFF0000;
/* 167 */     int k = localByteBuffer1.get(2) << 8 & 0xFF00;
/* 168 */     int m = localByteBuffer1.get(3) << 0 & 0xFF;
/* 169 */     int n = i | j | k | m;
/*     */ 
/* 171 */     if (n != 1195986768)
/*     */     {
/* 174 */       throw wrapper.giopMagicError(CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*     */ 
/* 180 */     byte b = 0;
/* 181 */     if ((localByteBuffer1.get(4) == 13) && (localByteBuffer1.get(5) <= 1) && (localByteBuffer1.get(5) > 0) && (paramORB.getORBData().isJavaSerializationEnabled()))
/*     */     {
/* 187 */       b = localByteBuffer1.get(5);
/* 188 */       localByteBuffer1.put(4, (byte)1);
/* 189 */       localByteBuffer1.put(5, (byte)2);
/*     */     }
/*     */ 
/* 192 */     GIOPVersion localGIOPVersion = paramORB.getORBData().getGIOPVersion();
/*     */ 
/* 194 */     if (paramORB.giopDebugFlag) {
/* 195 */       dprint(".readGIOPHeader: Message GIOP version: " + localByteBuffer1.get(4) + '.' + localByteBuffer1.get(5));
/*     */ 
/* 197 */       dprint(".readGIOPHeader: ORB Max GIOP Version: " + localGIOPVersion);
/*     */     }
/*     */ 
/* 201 */     if ((localByteBuffer1.get(4) > localGIOPVersion.getMajor()) || ((localByteBuffer1.get(4) == localGIOPVersion.getMajor()) && (localByteBuffer1.get(5) > localGIOPVersion.getMinor())))
/*     */     {
/* 214 */       if (localByteBuffer1.get(7) != 6) {
/* 215 */         throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
/*     */       }
/*     */     }
/*     */ 
/* 219 */     AreFragmentsAllowed(localByteBuffer1.get(4), localByteBuffer1.get(5), localByteBuffer1.get(6), localByteBuffer1.get(7));
/*     */ 
/* 223 */     switch (localByteBuffer1.get(7))
/*     */     {
/*     */     case 0:
/* 226 */       if (paramORB.giopDebugFlag) {
/* 227 */         dprint(".readGIOPHeader: creating RequestMessage");
/*     */       }
/*     */ 
/* 230 */       if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 0))
/* 231 */         localObject1 = new RequestMessage_1_0(paramORB);
/* 232 */       else if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 1))
/* 233 */         localObject1 = new RequestMessage_1_1(paramORB);
/* 234 */       else if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 2))
/* 235 */         localObject1 = new RequestMessage_1_2(paramORB);
/*     */       else {
/* 237 */         throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 3:
/* 243 */       if (paramORB.giopDebugFlag) {
/* 244 */         dprint(".readGIOPHeader: creating LocateRequestMessage");
/*     */       }
/*     */ 
/* 247 */       if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 0))
/* 248 */         localObject1 = new LocateRequestMessage_1_0(paramORB);
/* 249 */       else if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 1))
/* 250 */         localObject1 = new LocateRequestMessage_1_1(paramORB);
/* 251 */       else if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 2))
/* 252 */         localObject1 = new LocateRequestMessage_1_2(paramORB);
/*     */       else {
/* 254 */         throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 2:
/* 260 */       if (paramORB.giopDebugFlag) {
/* 261 */         dprint(".readGIOPHeader: creating CancelRequestMessage");
/*     */       }
/*     */ 
/* 264 */       if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 0))
/* 265 */         localObject1 = new CancelRequestMessage_1_0();
/* 266 */       else if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 1))
/* 267 */         localObject1 = new CancelRequestMessage_1_1();
/* 268 */       else if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 2))
/* 269 */         localObject1 = new CancelRequestMessage_1_2();
/*     */       else {
/* 271 */         throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 1:
/* 277 */       if (paramORB.giopDebugFlag) {
/* 278 */         dprint(".readGIOPHeader: creating ReplyMessage");
/*     */       }
/*     */ 
/* 281 */       if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 0))
/* 282 */         localObject1 = new ReplyMessage_1_0(paramORB);
/* 283 */       else if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 1))
/* 284 */         localObject1 = new ReplyMessage_1_1(paramORB);
/* 285 */       else if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 2))
/* 286 */         localObject1 = new ReplyMessage_1_2(paramORB);
/*     */       else {
/* 288 */         throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 4:
/* 294 */       if (paramORB.giopDebugFlag) {
/* 295 */         dprint(".readGIOPHeader: creating LocateReplyMessage");
/*     */       }
/*     */ 
/* 298 */       if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 0))
/* 299 */         localObject1 = new LocateReplyMessage_1_0(paramORB);
/* 300 */       else if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 1))
/* 301 */         localObject1 = new LocateReplyMessage_1_1(paramORB);
/* 302 */       else if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 2))
/* 303 */         localObject1 = new LocateReplyMessage_1_2(paramORB);
/*     */       else {
/* 305 */         throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 5:
/*     */     case 6:
/* 312 */       if (paramORB.giopDebugFlag) {
/* 313 */         dprint(".readGIOPHeader: creating Message for CloseConnection or MessageError");
/*     */       }
/*     */ 
/* 320 */       if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 0))
/* 321 */         localObject1 = new Message_1_0();
/* 322 */       else if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 1))
/* 323 */         localObject1 = new Message_1_1();
/* 324 */       else if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 2))
/* 325 */         localObject1 = new Message_1_1();
/*     */       else {
/* 327 */         throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
/*     */       }
/*     */ 
/*     */       break;
/*     */     case 7:
/* 333 */       if (paramORB.giopDebugFlag) {
/* 334 */         dprint(".readGIOPHeader: creating FragmentMessage");
/*     */       }
/*     */ 
/* 337 */       if ((localByteBuffer1.get(4) != 1) || (localByteBuffer1.get(5) != 0))
/*     */       {
/* 339 */         if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 1))
/* 340 */           localObject1 = new FragmentMessage_1_1();
/* 341 */         else if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 2))
/* 342 */           localObject1 = new FragmentMessage_1_2();
/*     */         else {
/* 344 */           throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
/*     */         }
/*     */       }
/*     */ 
/*     */       break;
/*     */     default:
/* 350 */       if (paramORB.giopDebugFlag) {
/* 351 */         dprint(".readGIOPHeader: UNKNOWN MESSAGE TYPE: " + localByteBuffer1.get(7));
/*     */       }
/*     */ 
/* 355 */       throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*     */     Object localObject2;
/* 363 */     if ((localByteBuffer1.get(4) == 1) && (localByteBuffer1.get(5) == 0)) {
/* 364 */       localObject2 = (Message_1_0)localObject1;
/* 365 */       ((Message_1_0)localObject2).magic = n;
/* 366 */       ((Message_1_0)localObject2).GIOP_version = new GIOPVersion(localByteBuffer1.get(4), localByteBuffer1.get(5));
/* 367 */       ((Message_1_0)localObject2).byte_order = (localByteBuffer1.get(6) == 1);
/*     */ 
/* 370 */       ((MessageBase)localObject1).threadPoolToUse = 0;
/* 371 */       ((Message_1_0)localObject2).message_type = localByteBuffer1.get(7);
/* 372 */       ((Message_1_0)localObject2).message_size = (readSize(localByteBuffer1.get(8), localByteBuffer1.get(9), localByteBuffer1.get(10), localByteBuffer1.get(11), ((Message_1_0)localObject2).isLittleEndian()) + 12);
/*     */     }
/*     */     else
/*     */     {
/* 376 */       localObject2 = (Message_1_1)localObject1;
/* 377 */       ((Message_1_1)localObject2).magic = n;
/* 378 */       ((Message_1_1)localObject2).GIOP_version = new GIOPVersion(localByteBuffer1.get(4), localByteBuffer1.get(5));
/* 379 */       ((Message_1_1)localObject2).flags = ((byte)(localByteBuffer1.get(6) & 0x3));
/*     */ 
/* 389 */       ((MessageBase)localObject1).threadPoolToUse = (localByteBuffer1.get(6) >>> 2 & 0x3F);
/* 390 */       ((Message_1_1)localObject2).message_type = localByteBuffer1.get(7);
/* 391 */       ((Message_1_1)localObject2).message_size = (readSize(localByteBuffer1.get(8), localByteBuffer1.get(9), localByteBuffer1.get(10), localByteBuffer1.get(11), ((Message_1_1)localObject2).isLittleEndian()) + 12);
/*     */     }
/*     */ 
/* 397 */     if (paramORB.giopDebugFlag)
/*     */     {
/* 402 */       dprint(".readGIOPHeader: header construction complete.");
/*     */ 
/* 405 */       localObject2 = localByteBuffer1.asReadOnlyBuffer();
/* 406 */       byte[] arrayOfByte = new byte[12];
/* 407 */       ((ByteBuffer)localObject2).position(0).limit(12);
/* 408 */       ((ByteBuffer)localObject2).get(arrayOfByte, 0, arrayOfByte.length);
/*     */ 
/* 410 */       ((MessageBase)localObject1).giopHeader = arrayOfByte;
/*     */     }
/*     */ 
/* 413 */     ((MessageBase)localObject1).setByteBuffer(localByteBuffer1);
/* 414 */     ((MessageBase)localObject1).setEncodingVersion(b);
/*     */ 
/* 416 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public static Message readGIOPBody(ORB paramORB, CorbaConnection paramCorbaConnection, Message paramMessage)
/*     */   {
/* 423 */     ReadTimeouts localReadTimeouts = paramORB.getORBData().getTransportTCPReadTimeouts();
/*     */ 
/* 425 */     ByteBuffer localByteBuffer1 = paramMessage.getByteBuffer();
/*     */ 
/* 427 */     localByteBuffer1.position(12);
/* 428 */     int i = paramMessage.getSize() - 12;
/*     */     try
/*     */     {
/* 431 */       localByteBuffer1 = paramCorbaConnection.read(localByteBuffer1, 12, i, localReadTimeouts.get_max_time_to_wait());
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 435 */       throw wrapper.ioexceptionWhenReadingConnection(localIOException);
/*     */     }
/*     */ 
/* 438 */     paramMessage.setByteBuffer(localByteBuffer1);
/*     */ 
/* 440 */     if (paramORB.giopDebugFlag) {
/* 441 */       dprint(".readGIOPBody: received message:");
/* 442 */       ByteBuffer localByteBuffer2 = localByteBuffer1.asReadOnlyBuffer();
/* 443 */       localByteBuffer2.position(0).limit(paramMessage.getSize());
/* 444 */       ByteBufferWithInfo localByteBufferWithInfo = new ByteBufferWithInfo(paramORB, localByteBuffer2);
/* 445 */       CDRInputStream_1_0.printBuffer(localByteBufferWithInfo);
/*     */     }
/*     */ 
/* 448 */     return paramMessage;
/*     */   }
/*     */ 
/*     */   private static RequestMessage createRequest(ORB paramORB, GIOPVersion paramGIOPVersion, byte paramByte, int paramInt, boolean paramBoolean, byte[] paramArrayOfByte, String paramString, ServiceContexts paramServiceContexts, Principal paramPrincipal)
/*     */   {
/* 456 */     if (paramGIOPVersion.equals(GIOPVersion.V1_0)) {
/* 457 */       return new RequestMessage_1_0(paramORB, paramServiceContexts, paramInt, paramBoolean, paramArrayOfByte, paramString, paramPrincipal);
/*     */     }
/*     */ 
/* 460 */     if (paramGIOPVersion.equals(GIOPVersion.V1_1)) {
/* 461 */       return new RequestMessage_1_1(paramORB, paramServiceContexts, paramInt, paramBoolean, new byte[] { 0, 0, 0 }, paramArrayOfByte, paramString, paramPrincipal);
/*     */     }
/*     */ 
/* 464 */     if (paramGIOPVersion.equals(GIOPVersion.V1_2))
/*     */     {
/* 468 */       byte b = 3;
/* 469 */       if (paramBoolean)
/* 470 */         b = 3;
/*     */       else {
/* 472 */         b = 0;
/*     */       }
/*     */ 
/* 490 */       TargetAddress localTargetAddress = new TargetAddress();
/* 491 */       localTargetAddress.object_key(paramArrayOfByte);
/* 492 */       RequestMessage_1_2 localRequestMessage_1_2 = new RequestMessage_1_2(paramORB, paramInt, b, new byte[] { 0, 0, 0 }, localTargetAddress, paramString, paramServiceContexts);
/*     */ 
/* 496 */       localRequestMessage_1_2.setEncodingVersion(paramByte);
/* 497 */       return localRequestMessage_1_2;
/*     */     }
/* 499 */     throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
/*     */   }
/*     */ 
/*     */   public static RequestMessage createRequest(ORB paramORB, GIOPVersion paramGIOPVersion, byte paramByte, int paramInt, boolean paramBoolean, IOR paramIOR, short paramShort, String paramString, ServiceContexts paramServiceContexts, Principal paramPrincipal)
/*     */   {
/* 510 */     Object localObject1 = null;
/* 511 */     IIOPProfile localIIOPProfile = paramIOR.getProfile();
/*     */     Object localObject2;
/*     */     byte b;
/*     */     Object localObject3;
/* 513 */     if (paramShort == 0)
/*     */     {
/* 515 */       localIIOPProfile = paramIOR.getProfile();
/* 516 */       ObjectKey localObjectKey = localIIOPProfile.getObjectKey();
/* 517 */       localObject2 = localObjectKey.getBytes(paramORB);
/* 518 */       localObject1 = createRequest(paramORB, paramGIOPVersion, paramByte, paramInt, paramBoolean, (byte[])localObject2, paramString, paramServiceContexts, paramPrincipal);
/*     */     }
/*     */     else
/*     */     {
/* 525 */       if (!paramGIOPVersion.equals(GIOPVersion.V1_2))
/*     */       {
/* 528 */         throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
/*     */       }
/*     */ 
/* 535 */       b = 3;
/* 536 */       if (paramBoolean)
/* 537 */         b = 3;
/*     */       else {
/* 539 */         b = 0;
/*     */       }
/*     */ 
/* 542 */       localObject2 = new TargetAddress();
/* 543 */       if (paramShort == 1) {
/* 544 */         localIIOPProfile = paramIOR.getProfile();
/* 545 */         ((TargetAddress)localObject2).profile(localIIOPProfile.getIOPProfile());
/* 546 */       } else if (paramShort == 2) {
/* 547 */         localObject3 = new IORAddressingInfo(0, paramIOR.getIOPIOR());
/*     */ 
/* 550 */         ((TargetAddress)localObject2).ior((IORAddressingInfo)localObject3);
/*     */       }
/*     */       else {
/* 553 */         throw wrapper.illegalTargetAddressDisposition(CompletionStatus.COMPLETED_NO);
/*     */       }
/*     */ 
/* 557 */       localObject1 = new RequestMessage_1_2(paramORB, paramInt, b, new byte[] { 0, 0, 0 }, (TargetAddress)localObject2, paramString, paramServiceContexts);
/*     */ 
/* 561 */       ((RequestMessage)localObject1).setEncodingVersion(paramByte);
/*     */     }
/*     */ 
/* 564 */     if (paramGIOPVersion.supportsIORIIOPProfileComponents())
/*     */     {
/* 566 */       b = 0;
/* 567 */       localObject2 = (IIOPProfileTemplate)localIIOPProfile.getTaggedProfileTemplate();
/*     */ 
/* 569 */       localObject3 = ((IIOPProfileTemplate)localObject2).iteratorById(1398099457);
/*     */       int i;
/* 571 */       if (((Iterator)localObject3).hasNext()) {
/* 572 */         i = ((RequestPartitioningComponent)((Iterator)localObject3).next()).getRequestPartitioningId();
/*     */       }
/*     */ 
/* 576 */       if ((i < 0) || (i > 63))
/*     */       {
/* 578 */         throw wrapper.invalidRequestPartitioningId(new Integer(i), new Integer(0), new Integer(63));
/*     */       }
/*     */ 
/* 582 */       ((RequestMessage)localObject1).setThreadPoolToUse(i);
/*     */     }
/*     */ 
/* 585 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public static ReplyMessage createReply(ORB paramORB, GIOPVersion paramGIOPVersion, byte paramByte, int paramInt1, int paramInt2, ServiceContexts paramServiceContexts, IOR paramIOR)
/*     */   {
/* 592 */     if (paramGIOPVersion.equals(GIOPVersion.V1_0)) {
/* 593 */       return new ReplyMessage_1_0(paramORB, paramServiceContexts, paramInt1, paramInt2, paramIOR);
/*     */     }
/* 595 */     if (paramGIOPVersion.equals(GIOPVersion.V1_1)) {
/* 596 */       return new ReplyMessage_1_1(paramORB, paramServiceContexts, paramInt1, paramInt2, paramIOR);
/*     */     }
/* 598 */     if (paramGIOPVersion.equals(GIOPVersion.V1_2)) {
/* 599 */       ReplyMessage_1_2 localReplyMessage_1_2 = new ReplyMessage_1_2(paramORB, paramInt1, paramInt2, paramServiceContexts, paramIOR);
/*     */ 
/* 602 */       localReplyMessage_1_2.setEncodingVersion(paramByte);
/* 603 */       return localReplyMessage_1_2;
/*     */     }
/* 605 */     throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
/*     */   }
/*     */ 
/*     */   public static LocateRequestMessage createLocateRequest(ORB paramORB, GIOPVersion paramGIOPVersion, byte paramByte, int paramInt, byte[] paramArrayOfByte)
/*     */   {
/* 614 */     if (paramGIOPVersion.equals(GIOPVersion.V1_0))
/* 615 */       return new LocateRequestMessage_1_0(paramORB, paramInt, paramArrayOfByte);
/* 616 */     if (paramGIOPVersion.equals(GIOPVersion.V1_1))
/* 617 */       return new LocateRequestMessage_1_1(paramORB, paramInt, paramArrayOfByte);
/* 618 */     if (paramGIOPVersion.equals(GIOPVersion.V1_2)) {
/* 619 */       TargetAddress localTargetAddress = new TargetAddress();
/* 620 */       localTargetAddress.object_key(paramArrayOfByte);
/* 621 */       LocateRequestMessage_1_2 localLocateRequestMessage_1_2 = new LocateRequestMessage_1_2(paramORB, paramInt, localTargetAddress);
/*     */ 
/* 623 */       localLocateRequestMessage_1_2.setEncodingVersion(paramByte);
/* 624 */       return localLocateRequestMessage_1_2;
/*     */     }
/* 626 */     throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
/*     */   }
/*     */ 
/*     */   public static LocateReplyMessage createLocateReply(ORB paramORB, GIOPVersion paramGIOPVersion, byte paramByte, int paramInt1, int paramInt2, IOR paramIOR)
/*     */   {
/* 635 */     if (paramGIOPVersion.equals(GIOPVersion.V1_0)) {
/* 636 */       return new LocateReplyMessage_1_0(paramORB, paramInt1, paramInt2, paramIOR);
/*     */     }
/* 638 */     if (paramGIOPVersion.equals(GIOPVersion.V1_1)) {
/* 639 */       return new LocateReplyMessage_1_1(paramORB, paramInt1, paramInt2, paramIOR);
/*     */     }
/* 641 */     if (paramGIOPVersion.equals(GIOPVersion.V1_2)) {
/* 642 */       LocateReplyMessage_1_2 localLocateReplyMessage_1_2 = new LocateReplyMessage_1_2(paramORB, paramInt1, paramInt2, paramIOR);
/*     */ 
/* 645 */       localLocateReplyMessage_1_2.setEncodingVersion(paramByte);
/* 646 */       return localLocateReplyMessage_1_2;
/*     */     }
/* 648 */     throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
/*     */   }
/*     */ 
/*     */   public static CancelRequestMessage createCancelRequest(GIOPVersion paramGIOPVersion, int paramInt)
/*     */   {
/* 656 */     if (paramGIOPVersion.equals(GIOPVersion.V1_0))
/* 657 */       return new CancelRequestMessage_1_0(paramInt);
/* 658 */     if (paramGIOPVersion.equals(GIOPVersion.V1_1))
/* 659 */       return new CancelRequestMessage_1_1(paramInt);
/* 660 */     if (paramGIOPVersion.equals(GIOPVersion.V1_2)) {
/* 661 */       return new CancelRequestMessage_1_2(paramInt);
/*     */     }
/* 663 */     throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
/*     */   }
/*     */ 
/*     */   public static Message createCloseConnection(GIOPVersion paramGIOPVersion)
/*     */   {
/* 669 */     if (paramGIOPVersion.equals(GIOPVersion.V1_0)) {
/* 670 */       return new Message_1_0(1195986768, false, (byte)5, 0);
/*     */     }
/* 672 */     if (paramGIOPVersion.equals(GIOPVersion.V1_1)) {
/* 673 */       return new Message_1_1(1195986768, GIOPVersion.V1_1, (byte)0, (byte)5, 0);
/*     */     }
/*     */ 
/* 676 */     if (paramGIOPVersion.equals(GIOPVersion.V1_2)) {
/* 677 */       return new Message_1_1(1195986768, GIOPVersion.V1_2, (byte)0, (byte)5, 0);
/*     */     }
/*     */ 
/* 681 */     throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
/*     */   }
/*     */ 
/*     */   public static Message createMessageError(GIOPVersion paramGIOPVersion)
/*     */   {
/* 687 */     if (paramGIOPVersion.equals(GIOPVersion.V1_0)) {
/* 688 */       return new Message_1_0(1195986768, false, (byte)6, 0);
/*     */     }
/* 690 */     if (paramGIOPVersion.equals(GIOPVersion.V1_1)) {
/* 691 */       return new Message_1_1(1195986768, GIOPVersion.V1_1, (byte)0, (byte)6, 0);
/*     */     }
/*     */ 
/* 694 */     if (paramGIOPVersion.equals(GIOPVersion.V1_2)) {
/* 695 */       return new Message_1_1(1195986768, GIOPVersion.V1_2, (byte)0, (byte)6, 0);
/*     */     }
/*     */ 
/* 699 */     throw wrapper.giopVersionError(CompletionStatus.COMPLETED_MAYBE);
/*     */   }
/*     */ 
/*     */   public static FragmentMessage createFragmentMessage(GIOPVersion paramGIOPVersion)
/*     */   {
/* 709 */     return null;
/*     */   }
/*     */ 
/*     */   public static int getRequestId(Message paramMessage) {
/* 713 */     switch (paramMessage.getType()) {
/*     */     case 0:
/* 715 */       return ((RequestMessage)paramMessage).getRequestId();
/*     */     case 1:
/* 717 */       return ((ReplyMessage)paramMessage).getRequestId();
/*     */     case 3:
/* 719 */       return ((LocateRequestMessage)paramMessage).getRequestId();
/*     */     case 4:
/* 721 */       return ((LocateReplyMessage)paramMessage).getRequestId();
/*     */     case 2:
/* 723 */       return ((CancelRequestMessage)paramMessage).getRequestId();
/*     */     case 7:
/* 725 */       return ((FragmentMessage)paramMessage).getRequestId();
/*     */     case 5:
/*     */     case 6:
/* 728 */     }throw wrapper.illegalGiopMsgType(CompletionStatus.COMPLETED_MAYBE);
/*     */   }
/*     */ 
/*     */   public static void setFlag(ByteBuffer paramByteBuffer, int paramInt)
/*     */   {
/* 736 */     int i = paramByteBuffer.get(6);
/* 737 */     i = (byte)(i | paramInt);
/* 738 */     paramByteBuffer.put(6, i);
/*     */   }
/*     */ 
/*     */   public static void clearFlag(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 745 */     paramArrayOfByte[6] = ((byte)(paramArrayOfByte[6] & (0xFF ^ paramInt)));
/*     */   }
/*     */ 
/*     */   private static void AreFragmentsAllowed(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4)
/*     */   {
/* 751 */     if ((paramByte1 == 1) && (paramByte2 == 0) && 
/* 752 */       (paramByte4 == 7)) {
/* 753 */       throw wrapper.fragmentationDisallowed(CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/*     */ 
/* 758 */     if ((paramByte3 & 0x2) == 2)
/* 759 */       switch (paramByte4) {
/*     */       case 2:
/*     */       case 5:
/*     */       case 6:
/* 763 */         throw wrapper.fragmentationDisallowed(CompletionStatus.COMPLETED_MAYBE);
/*     */       case 3:
/*     */       case 4:
/* 767 */         if ((paramByte1 == 1) && (paramByte2 == 1))
/* 768 */           throw wrapper.fragmentationDisallowed(CompletionStatus.COMPLETED_MAYBE);
/*     */         break;
/*     */       }
/*     */   }
/*     */ 
/*     */   static ObjectKey extractObjectKey(byte[] paramArrayOfByte, ORB paramORB)
/*     */   {
/*     */     try
/*     */     {
/* 784 */       if (paramArrayOfByte != null) {
/* 785 */         ObjectKey localObjectKey = paramORB.getObjectKeyFactory().create(paramArrayOfByte);
/*     */ 
/* 787 */         if (localObjectKey != null) {
/* 788 */           return localObjectKey;
/*     */         }
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */ 
/* 797 */     throw wrapper.invalidObjectKey();
/*     */   }
/*     */ 
/*     */   static ObjectKey extractObjectKey(TargetAddress paramTargetAddress, ORB paramORB)
/*     */   {
/* 807 */     int i = paramORB.getORBData().getGIOPTargetAddressPreference();
/* 808 */     int j = paramTargetAddress.discriminator();
/*     */ 
/* 810 */     switch (i) {
/*     */     case 0:
/* 812 */       if (j != 0) {
/* 813 */         throw new AddressingDispositionException((short)0);
/*     */       }
/*     */       break;
/*     */     case 1:
/* 817 */       if (j != 1) {
/* 818 */         throw new AddressingDispositionException((short)1);
/*     */       }
/*     */       break;
/*     */     case 2:
/* 822 */       if (j != 2) {
/* 823 */         throw new AddressingDispositionException((short)2);
/*     */       }
/*     */       break;
/*     */     case 3:
/* 827 */       break;
/*     */     default:
/* 829 */       throw wrapper.orbTargetAddrPreferenceInExtractObjectkeyInvalid();
/*     */     }
/*     */     try
/*     */     {
/*     */       Object localObject1;
/*     */       TaggedProfile localTaggedProfile;
/*     */       Object localObject2;
/* 833 */       switch (j) {
/*     */       case 0:
/* 835 */         byte[] arrayOfByte = paramTargetAddress.object_key();
/* 836 */         if (arrayOfByte != null) {
/* 837 */           localObject1 = paramORB.getObjectKeyFactory().create(arrayOfByte);
/*     */ 
/* 839 */           if (localObject1 != null)
/* 840 */             return localObject1;
/*     */         }
/* 842 */         break;
/*     */       case 1:
/* 845 */         localObject1 = null;
/* 846 */         localTaggedProfile = paramTargetAddress.profile();
/* 847 */         if (localTaggedProfile != null) {
/* 848 */           localObject1 = IIOPFactories.makeIIOPProfile(paramORB, localTaggedProfile);
/* 849 */           localObject2 = ((IIOPProfile)localObject1).getObjectKey();
/* 850 */           if (localObject2 != null)
/* 851 */             return localObject2;
/*     */         }
/* 853 */         break;
/*     */       case 2:
/* 856 */         localObject2 = paramTargetAddress.ior();
/* 857 */         if (localObject2 != null) {
/* 858 */           localTaggedProfile = localObject2.ior.profiles[localObject2.selected_profile_index];
/* 859 */           localObject1 = IIOPFactories.makeIIOPProfile(paramORB, localTaggedProfile);
/* 860 */           ObjectKey localObjectKey = ((IIOPProfile)localObject1).getObjectKey();
/* 861 */           if (localObjectKey != null) {
/* 862 */             return localObjectKey;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */         break;
/*     */       }
/*     */ 
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */ 
/* 877 */     throw wrapper.invalidObjectKey();
/*     */   }
/*     */ 
/*     */   private static int readSize(byte paramByte1, byte paramByte2, byte paramByte3, byte paramByte4, boolean paramBoolean)
/*     */   {
/*     */     int i;
/*     */     int j;
/*     */     int k;
/*     */     int m;
/* 885 */     if (!paramBoolean) {
/* 886 */       i = paramByte1 << 24 & 0xFF000000;
/* 887 */       j = paramByte2 << 16 & 0xFF0000;
/* 888 */       k = paramByte3 << 8 & 0xFF00;
/* 889 */       m = paramByte4 << 0 & 0xFF;
/*     */     } else {
/* 891 */       i = paramByte4 << 24 & 0xFF000000;
/* 892 */       j = paramByte3 << 16 & 0xFF0000;
/* 893 */       k = paramByte2 << 8 & 0xFF00;
/* 894 */       m = paramByte1 << 0 & 0xFF;
/*     */     }
/*     */ 
/* 897 */     return i | j | k | m;
/*     */   }
/*     */ 
/*     */   static void nullCheck(Object paramObject) {
/* 901 */     if (paramObject == null)
/* 902 */       throw wrapper.nullNotAllowed();
/*     */   }
/*     */ 
/*     */   static SystemException getSystemException(String paramString1, int paramInt, CompletionStatus paramCompletionStatus, String paramString2, ORBUtilSystemException paramORBUtilSystemException)
/*     */   {
/* 910 */     SystemException localSystemException = null;
/*     */     try
/*     */     {
/* 913 */       Class localClass = SharedSecrets.getJavaCorbaAccess().loadClass(paramString1);
/*     */ 
/* 915 */       if (paramString2 == null) {
/* 916 */         localSystemException = (SystemException)localClass.newInstance();
/*     */       } else {
/* 918 */         Class[] arrayOfClass = { String.class };
/* 919 */         Constructor localConstructor = localClass.getConstructor(arrayOfClass);
/* 920 */         Object[] arrayOfObject = { paramString2 };
/* 921 */         localSystemException = (SystemException)localConstructor.newInstance(arrayOfObject);
/*     */       }
/*     */     } catch (Exception localException) {
/* 924 */       throw paramORBUtilSystemException.badSystemExceptionInReply(CompletionStatus.COMPLETED_MAYBE, localException);
/*     */     }
/*     */ 
/* 928 */     localSystemException.minor = paramInt;
/* 929 */     localSystemException.completed = paramCompletionStatus;
/*     */ 
/* 931 */     return localSystemException;
/*     */   }
/*     */ 
/*     */   public void callback(MessageHandler paramMessageHandler)
/*     */     throws IOException
/*     */   {
/* 937 */     paramMessageHandler.handleInput(this);
/*     */   }
/*     */ 
/*     */   public ByteBuffer getByteBuffer()
/*     */   {
/* 942 */     return this.byteBuffer;
/*     */   }
/*     */ 
/*     */   public void setByteBuffer(ByteBuffer paramByteBuffer)
/*     */   {
/* 947 */     this.byteBuffer = paramByteBuffer;
/*     */   }
/*     */ 
/*     */   public int getThreadPoolToUse()
/*     */   {
/* 952 */     return this.threadPoolToUse;
/*     */   }
/*     */ 
/*     */   public byte getEncodingVersion() {
/* 956 */     return this.encodingVersion;
/*     */   }
/*     */ 
/*     */   public void setEncodingVersion(byte paramByte) {
/* 960 */     this.encodingVersion = paramByte;
/*     */   }
/*     */ 
/*     */   private static void dprint(String paramString)
/*     */   {
/* 965 */     ORBUtility.dprint("MessageBase", paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase
 * JD-Core Version:    0.6.2
 */