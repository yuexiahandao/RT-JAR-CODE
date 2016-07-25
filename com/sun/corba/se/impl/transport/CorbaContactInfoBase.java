/*     */ package com.sun.corba.se.impl.transport;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.CDRInputObject;
/*     */ import com.sun.corba.se.impl.encoding.CDROutputObject;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.impl.protocol.CorbaMessageMediatorImpl;
/*     */ import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;
/*     */ import com.sun.corba.se.impl.protocol.giopmsgheaders.MessageBase;
/*     */ import com.sun.corba.se.pept.broker.Broker;
/*     */ import com.sun.corba.se.pept.encoding.InputObject;
/*     */ import com.sun.corba.se.pept.encoding.OutputObject;
/*     */ import com.sun.corba.se.pept.protocol.ClientRequestDispatcher;
/*     */ import com.sun.corba.se.pept.protocol.MessageMediator;
/*     */ import com.sun.corba.se.pept.transport.Connection;
/*     */ import com.sun.corba.se.pept.transport.ContactInfo;
/*     */ import com.sun.corba.se.pept.transport.ContactInfoList;
/*     */ import com.sun.corba.se.pept.transport.OutboundConnectionCache;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*     */ import com.sun.corba.se.spi.protocol.RequestDispatcherRegistry;
/*     */ import com.sun.corba.se.spi.transport.CorbaConnection;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfo;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfoList;
/*     */ import java.nio.ByteBuffer;
/*     */ import sun.corba.OutputStreamFactory;
/*     */ 
/*     */ public abstract class CorbaContactInfoBase
/*     */   implements CorbaContactInfo
/*     */ {
/*     */   protected ORB orb;
/*     */   protected CorbaContactInfoList contactInfoList;
/*     */   protected IOR effectiveTargetIOR;
/*     */   protected short addressingDisposition;
/*     */   protected OutboundConnectionCache connectionCache;
/*     */ 
/*     */   public Broker getBroker()
/*     */   {
/*  90 */     return this.orb;
/*     */   }
/*     */ 
/*     */   public ContactInfoList getContactInfoList()
/*     */   {
/*  95 */     return this.contactInfoList;
/*     */   }
/*     */ 
/*     */   public ClientRequestDispatcher getClientRequestDispatcher()
/*     */   {
/* 100 */     int i = getEffectiveProfile().getObjectKeyTemplate().getSubcontractId();
/*     */ 
/* 102 */     RequestDispatcherRegistry localRequestDispatcherRegistry = this.orb.getRequestDispatcherRegistry();
/* 103 */     return localRequestDispatcherRegistry.getClientRequestDispatcher(i);
/*     */   }
/*     */ 
/*     */   public void setConnectionCache(OutboundConnectionCache paramOutboundConnectionCache)
/*     */   {
/* 110 */     this.connectionCache = paramOutboundConnectionCache;
/*     */   }
/*     */ 
/*     */   public OutboundConnectionCache getConnectionCache()
/*     */   {
/* 115 */     return this.connectionCache;
/*     */   }
/*     */ 
/*     */   public MessageMediator createMessageMediator(Broker paramBroker, ContactInfo paramContactInfo, Connection paramConnection, String paramString, boolean paramBoolean)
/*     */   {
/* 130 */     CorbaMessageMediatorImpl localCorbaMessageMediatorImpl = new CorbaMessageMediatorImpl((ORB)paramBroker, paramContactInfo, paramConnection, GIOPVersion.chooseRequestVersion((ORB)paramBroker, this.effectiveTargetIOR), this.effectiveTargetIOR, ((CorbaConnection)paramConnection).getNextRequestId(), getAddressingDisposition(), paramString, paramBoolean);
/*     */ 
/* 143 */     return localCorbaMessageMediatorImpl;
/*     */   }
/*     */ 
/*     */   public MessageMediator createMessageMediator(Broker paramBroker, Connection paramConnection)
/*     */   {
/* 149 */     ORB localORB = (ORB)paramBroker;
/* 150 */     CorbaConnection localCorbaConnection = (CorbaConnection)paramConnection;
/*     */ 
/* 152 */     if (localORB.transportDebugFlag) {
/* 153 */       if (localCorbaConnection.shouldReadGiopHeaderOnly()) {
/* 154 */         dprint(".createMessageMediator: waiting for message header on connection: " + localCorbaConnection);
/*     */       }
/*     */       else
/*     */       {
/* 158 */         dprint(".createMessageMediator: waiting for message on connection: " + localCorbaConnection);
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 164 */     MessageBase localMessageBase = null;
/*     */ 
/* 166 */     if (localCorbaConnection.shouldReadGiopHeaderOnly())
/*     */     {
/* 168 */       localMessageBase = MessageBase.readGIOPHeader(localORB, localCorbaConnection);
/*     */     }
/*     */     else {
/* 171 */       localMessageBase = MessageBase.readGIOPMessage(localORB, localCorbaConnection);
/*     */     }
/*     */ 
/* 174 */     ByteBuffer localByteBuffer = localMessageBase.getByteBuffer();
/* 175 */     localMessageBase.setByteBuffer(null);
/* 176 */     CorbaMessageMediatorImpl localCorbaMessageMediatorImpl = new CorbaMessageMediatorImpl(localORB, localCorbaConnection, localMessageBase, localByteBuffer);
/*     */ 
/* 179 */     return localCorbaMessageMediatorImpl;
/*     */   }
/*     */ 
/*     */   public MessageMediator finishCreatingMessageMediator(Broker paramBroker, Connection paramConnection, MessageMediator paramMessageMediator)
/*     */   {
/* 186 */     ORB localORB = (ORB)paramBroker;
/* 187 */     CorbaConnection localCorbaConnection = (CorbaConnection)paramConnection;
/* 188 */     CorbaMessageMediator localCorbaMessageMediator = (CorbaMessageMediator)paramMessageMediator;
/*     */ 
/* 191 */     if (localORB.transportDebugFlag) {
/* 192 */       dprint(".finishCreatingMessageMediator: waiting for message body on connection: " + localCorbaConnection);
/*     */     }
/*     */ 
/* 197 */     Message localMessage = localCorbaMessageMediator.getDispatchHeader();
/* 198 */     localMessage.setByteBuffer(localCorbaMessageMediator.getDispatchBuffer());
/*     */ 
/* 201 */     localMessage = MessageBase.readGIOPBody(localORB, localCorbaConnection, localMessage);
/*     */ 
/* 203 */     ByteBuffer localByteBuffer = localMessage.getByteBuffer();
/* 204 */     localMessage.setByteBuffer(null);
/* 205 */     localCorbaMessageMediator.setDispatchHeader(localMessage);
/* 206 */     localCorbaMessageMediator.setDispatchBuffer(localByteBuffer);
/*     */ 
/* 208 */     return localCorbaMessageMediator;
/*     */   }
/*     */ 
/*     */   public OutputObject createOutputObject(MessageMediator paramMessageMediator)
/*     */   {
/* 213 */     CorbaMessageMediator localCorbaMessageMediator = (CorbaMessageMediator)paramMessageMediator;
/*     */ 
/* 216 */     CDROutputObject localCDROutputObject = OutputStreamFactory.newCDROutputObject(this.orb, paramMessageMediator, localCorbaMessageMediator.getRequestHeader(), localCorbaMessageMediator.getStreamFormatVersion());
/*     */ 
/* 221 */     paramMessageMediator.setOutputObject(localCDROutputObject);
/* 222 */     return localCDROutputObject;
/*     */   }
/*     */ 
/*     */   public InputObject createInputObject(Broker paramBroker, MessageMediator paramMessageMediator)
/*     */   {
/* 229 */     CorbaMessageMediator localCorbaMessageMediator = (CorbaMessageMediator)paramMessageMediator;
/*     */ 
/* 231 */     return new CDRInputObject((ORB)paramBroker, (CorbaConnection)paramMessageMediator.getConnection(), localCorbaMessageMediator.getDispatchBuffer(), localCorbaMessageMediator.getDispatchHeader());
/*     */   }
/*     */ 
/*     */   public short getAddressingDisposition()
/*     */   {
/* 244 */     return this.addressingDisposition;
/*     */   }
/*     */ 
/*     */   public void setAddressingDisposition(short paramShort)
/*     */   {
/* 249 */     this.addressingDisposition = paramShort;
/*     */   }
/*     */ 
/*     */   public IOR getTargetIOR()
/*     */   {
/* 255 */     return this.contactInfoList.getTargetIOR();
/*     */   }
/*     */ 
/*     */   public IOR getEffectiveTargetIOR()
/*     */   {
/* 260 */     return this.effectiveTargetIOR;
/*     */   }
/*     */ 
/*     */   public IIOPProfile getEffectiveProfile()
/*     */   {
/* 265 */     return this.effectiveTargetIOR.getProfile();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 275 */     return "CorbaContactInfoBase[]";
/*     */   }
/*     */ 
/*     */   protected void dprint(String paramString)
/*     */   {
/* 288 */     ORBUtility.dprint("CorbaContactInfoBase", paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.transport.CorbaContactInfoBase
 * JD-Core Version:    0.6.2
 */