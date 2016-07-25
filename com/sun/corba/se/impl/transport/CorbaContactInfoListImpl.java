/*     */ package com.sun.corba.se.impl.transport;
/*     */ 
/*     */ import com.sun.corba.se.impl.protocol.NotLocalLocalCRDImpl;
/*     */ import com.sun.corba.se.pept.transport.ContactInfo;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.ObjectKeyTemplate;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPAddress;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.protocol.LocalClientRequestDispatcher;
/*     */ import com.sun.corba.se.spi.protocol.LocalClientRequestDispatcherFactory;
/*     */ import com.sun.corba.se.spi.protocol.RequestDispatcherRegistry;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfoList;
/*     */ import com.sun.corba.se.spi.transport.IIOPPrimaryToContactInfo;
/*     */ import com.sun.corba.se.spi.transport.IORToSocketInfo;
/*     */ import com.sun.corba.se.spi.transport.SocketInfo;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ 
/*     */ public class CorbaContactInfoListImpl
/*     */   implements CorbaContactInfoList
/*     */ {
/*     */   protected ORB orb;
/*     */   protected LocalClientRequestDispatcher LocalClientRequestDispatcher;
/*     */   protected IOR targetIOR;
/*     */   protected IOR effectiveTargetIOR;
/*     */   protected List effectiveTargetIORContactInfoList;
/*     */   protected ContactInfo primaryContactInfo;
/*     */ 
/*     */   public CorbaContactInfoListImpl(ORB paramORB)
/*     */   {
/*  64 */     this.orb = paramORB;
/*     */   }
/*     */ 
/*     */   public CorbaContactInfoListImpl(ORB paramORB, IOR paramIOR)
/*     */   {
/*  69 */     this(paramORB);
/*  70 */     setTargetIOR(paramIOR);
/*     */   }
/*     */ 
/*     */   public synchronized Iterator iterator()
/*     */   {
/*  80 */     createContactInfoList();
/*  81 */     return new CorbaContactInfoListIteratorImpl(this.orb, this, this.primaryContactInfo, this.effectiveTargetIORContactInfoList);
/*     */   }
/*     */ 
/*     */   public synchronized void setTargetIOR(IOR paramIOR)
/*     */   {
/*  93 */     this.targetIOR = paramIOR;
/*  94 */     setEffectiveTargetIOR(paramIOR);
/*     */   }
/*     */ 
/*     */   public synchronized IOR getTargetIOR()
/*     */   {
/*  99 */     return this.targetIOR;
/*     */   }
/*     */ 
/*     */   public synchronized void setEffectiveTargetIOR(IOR paramIOR)
/*     */   {
/* 104 */     this.effectiveTargetIOR = paramIOR;
/* 105 */     this.effectiveTargetIORContactInfoList = null;
/* 106 */     if ((this.primaryContactInfo != null) && (this.orb.getORBData().getIIOPPrimaryToContactInfo() != null))
/*     */     {
/* 109 */       this.orb.getORBData().getIIOPPrimaryToContactInfo().reset(this.primaryContactInfo);
/*     */     }
/*     */ 
/* 112 */     this.primaryContactInfo = null;
/* 113 */     setLocalSubcontract();
/*     */   }
/*     */ 
/*     */   public synchronized IOR getEffectiveTargetIOR()
/*     */   {
/* 118 */     return this.effectiveTargetIOR;
/*     */   }
/*     */ 
/*     */   public synchronized LocalClientRequestDispatcher getLocalClientRequestDispatcher()
/*     */   {
/* 123 */     return this.LocalClientRequestDispatcher;
/*     */   }
/*     */ 
/*     */   public synchronized int hashCode()
/*     */   {
/* 140 */     return this.targetIOR.hashCode();
/*     */   }
/*     */ 
/*     */   protected void createContactInfoList()
/*     */   {
/* 150 */     if (this.effectiveTargetIORContactInfoList != null) {
/* 151 */       return;
/*     */     }
/*     */ 
/* 154 */     this.effectiveTargetIORContactInfoList = new ArrayList();
/*     */ 
/* 156 */     IIOPProfile localIIOPProfile = this.effectiveTargetIOR.getProfile();
/* 157 */     String str = ((IIOPProfileTemplate)localIIOPProfile.getTaggedProfileTemplate()).getPrimaryAddress().getHost().toLowerCase();
/*     */ 
/* 160 */     int i = ((IIOPProfileTemplate)localIIOPProfile.getTaggedProfileTemplate()).getPrimaryAddress().getPort();
/*     */ 
/* 164 */     this.primaryContactInfo = createContactInfo("IIOP_CLEAR_TEXT", str, i);
/*     */ 
/* 167 */     if (localIIOPProfile.isLocal())
/*     */     {
/* 173 */       SharedCDRContactInfoImpl localSharedCDRContactInfoImpl = new SharedCDRContactInfoImpl(this.orb, this, this.effectiveTargetIOR, this.orb.getORBData().getGIOPAddressDisposition());
/*     */ 
/* 176 */       this.effectiveTargetIORContactInfoList.add(localSharedCDRContactInfoImpl);
/*     */     } else {
/* 178 */       addRemoteContactInfos(this.effectiveTargetIOR, this.effectiveTargetIORContactInfoList);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void addRemoteContactInfos(IOR paramIOR, List paramList)
/*     */   {
/* 188 */     List localList = this.orb.getORBData().getIORToSocketInfo().getSocketInfo(paramIOR);
/*     */ 
/* 190 */     Iterator localIterator = localList.iterator();
/* 191 */     while (localIterator.hasNext()) {
/* 192 */       SocketInfo localSocketInfo = (SocketInfo)localIterator.next();
/* 193 */       String str1 = localSocketInfo.getType();
/* 194 */       String str2 = localSocketInfo.getHost().toLowerCase();
/* 195 */       int i = localSocketInfo.getPort();
/* 196 */       ContactInfo localContactInfo = createContactInfo(str1, str2, i);
/* 197 */       paramList.add(localContactInfo);
/*     */     }
/*     */   }
/*     */ 
/*     */   protected ContactInfo createContactInfo(String paramString1, String paramString2, int paramInt)
/*     */   {
/* 204 */     return new SocketOrChannelContactInfoImpl(this.orb, this, this.effectiveTargetIOR, this.orb.getORBData().getGIOPAddressDisposition(), paramString1, paramString2, paramInt);
/*     */   }
/*     */ 
/*     */   protected void setLocalSubcontract()
/*     */   {
/* 222 */     if (!this.effectiveTargetIOR.getProfile().isLocal()) {
/* 223 */       this.LocalClientRequestDispatcher = new NotLocalLocalCRDImpl();
/* 224 */       return;
/*     */     }
/*     */ 
/* 233 */     int i = this.effectiveTargetIOR.getProfile().getObjectKeyTemplate().getSubcontractId();
/*     */ 
/* 235 */     LocalClientRequestDispatcherFactory localLocalClientRequestDispatcherFactory = this.orb.getRequestDispatcherRegistry().getLocalClientRequestDispatcherFactory(i);
/* 236 */     this.LocalClientRequestDispatcher = localLocalClientRequestDispatcherFactory.create(i, this.effectiveTargetIOR);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.transport.CorbaContactInfoListImpl
 * JD-Core Version:    0.6.2
 */