/*     */ package com.sun.corba.se.impl.orb;
/*     */ 
/*     */ import com.sun.corba.se.impl.encoding.CodeSetComponentInfo;
/*     */ import com.sun.corba.se.impl.encoding.CodeSetComponentInfo.CodeSetComponent;
/*     */ import com.sun.corba.se.impl.legacy.connection.USLPort;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.pept.transport.Acceptor;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.orb.DataCollector;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.orb.ParserImplTableBase;
/*     */ import com.sun.corba.se.spi.orb.StringPair;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfoListFactory;
/*     */ import com.sun.corba.se.spi.transport.IIOPPrimaryToContactInfo;
/*     */ import com.sun.corba.se.spi.transport.IORToSocketInfo;
/*     */ import com.sun.corba.se.spi.transport.ReadTimeouts;
/*     */ import java.net.URL;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.PortableInterceptor.ORBInitializer;
/*     */ 
/*     */ public class ORBDataParserImpl extends ParserImplTableBase
/*     */   implements ORBData
/*     */ {
/*     */   private ORB orb;
/*     */   private ORBUtilSystemException wrapper;
/*     */   private String ORBInitialHost;
/*     */   private int ORBInitialPort;
/*     */   private String ORBServerHost;
/*     */   private int ORBServerPort;
/*     */   private String listenOnAllInterfaces;
/*     */   private com.sun.corba.se.spi.legacy.connection.ORBSocketFactory legacySocketFactory;
/*     */   private com.sun.corba.se.spi.transport.ORBSocketFactory socketFactory;
/*     */   private USLPort[] userSpecifiedListenPorts;
/*     */   private IORToSocketInfo iorToSocketInfo;
/*     */   private IIOPPrimaryToContactInfo iiopPrimaryToContactInfo;
/*     */   private String orbId;
/*     */   private boolean orbServerIdPropertySpecified;
/*     */   private URL servicesURL;
/*     */   private String propertyInitRef;
/*     */   private boolean allowLocalOptimization;
/*     */   private GIOPVersion giopVersion;
/*     */   private int highWaterMark;
/*     */   private int lowWaterMark;
/*     */   private int numberToReclaim;
/*     */   private int giopFragmentSize;
/*     */   private int giopBufferSize;
/*     */   private int giop11BuffMgr;
/*     */   private int giop12BuffMgr;
/*     */   private short giopTargetAddressPreference;
/*     */   private short giopAddressDisposition;
/*     */   private boolean useByteOrderMarkers;
/*     */   private boolean useByteOrderMarkersInEncaps;
/*     */   private boolean alwaysSendCodeSetCtx;
/*     */   private boolean persistentPortInitialized;
/*     */   private int persistentServerPort;
/*     */   private boolean persistentServerIdInitialized;
/*     */   private int persistentServerId;
/*     */   private boolean serverIsORBActivated;
/*     */   private Class badServerIdHandlerClass;
/*     */   private CodeSetComponentInfo.CodeSetComponent charData;
/*     */   private CodeSetComponentInfo.CodeSetComponent wcharData;
/*     */   private ORBInitializer[] orbInitializers;
/*     */   private StringPair[] orbInitialReferences;
/*     */   private String defaultInitRef;
/*     */   private String[] debugFlags;
/*     */   private Acceptor[] acceptors;
/*     */   private CorbaContactInfoListFactory corbaContactInfoListFactory;
/*     */   private String acceptorSocketType;
/*     */   private boolean acceptorSocketUseSelectThreadToWait;
/*     */   private boolean acceptorSocketUseWorkerThreadForEvent;
/*     */   private String connectionSocketType;
/*     */   private boolean connectionSocketUseSelectThreadToWait;
/*     */   private boolean connectionSocketUseWorkerThreadForEvent;
/*     */   private ReadTimeouts readTimeouts;
/*     */   private boolean disableDirectByteBufferUse;
/*     */   private boolean enableJavaSerialization;
/*     */   private boolean useRepId;
/*     */   private CodeSetComponentInfo codesets;
/*     */ 
/*     */   public String getORBInitialHost()
/*     */   {
/* 119 */     return this.ORBInitialHost;
/*     */   }
/*     */ 
/*     */   public int getORBInitialPort()
/*     */   {
/* 124 */     return this.ORBInitialPort;
/*     */   }
/*     */ 
/*     */   public String getORBServerHost()
/*     */   {
/* 129 */     return this.ORBServerHost;
/*     */   }
/*     */ 
/*     */   public String getListenOnAllInterfaces()
/*     */   {
/* 134 */     return this.listenOnAllInterfaces;
/*     */   }
/*     */ 
/*     */   public int getORBServerPort()
/*     */   {
/* 139 */     return this.ORBServerPort;
/*     */   }
/*     */ 
/*     */   public com.sun.corba.se.spi.legacy.connection.ORBSocketFactory getLegacySocketFactory()
/*     */   {
/* 144 */     return this.legacySocketFactory;
/*     */   }
/*     */ 
/*     */   public com.sun.corba.se.spi.transport.ORBSocketFactory getSocketFactory()
/*     */   {
/* 149 */     return this.socketFactory;
/*     */   }
/*     */ 
/*     */   public USLPort[] getUserSpecifiedListenPorts()
/*     */   {
/* 154 */     return this.userSpecifiedListenPorts;
/*     */   }
/*     */ 
/*     */   public IORToSocketInfo getIORToSocketInfo()
/*     */   {
/* 159 */     return this.iorToSocketInfo;
/*     */   }
/*     */ 
/*     */   public IIOPPrimaryToContactInfo getIIOPPrimaryToContactInfo()
/*     */   {
/* 164 */     return this.iiopPrimaryToContactInfo;
/*     */   }
/*     */ 
/*     */   public String getORBId()
/*     */   {
/* 169 */     return this.orbId;
/*     */   }
/*     */ 
/*     */   public boolean getORBServerIdPropertySpecified()
/*     */   {
/* 174 */     return this.orbServerIdPropertySpecified;
/*     */   }
/*     */ 
/*     */   public boolean isLocalOptimizationAllowed()
/*     */   {
/* 179 */     return this.allowLocalOptimization;
/*     */   }
/*     */ 
/*     */   public GIOPVersion getGIOPVersion()
/*     */   {
/* 184 */     return this.giopVersion;
/*     */   }
/*     */ 
/*     */   public int getHighWaterMark()
/*     */   {
/* 189 */     return this.highWaterMark;
/*     */   }
/*     */ 
/*     */   public int getLowWaterMark()
/*     */   {
/* 194 */     return this.lowWaterMark;
/*     */   }
/*     */ 
/*     */   public int getNumberToReclaim()
/*     */   {
/* 199 */     return this.numberToReclaim;
/*     */   }
/*     */ 
/*     */   public int getGIOPFragmentSize()
/*     */   {
/* 204 */     return this.giopFragmentSize;
/*     */   }
/*     */ 
/*     */   public int getGIOPBufferSize()
/*     */   {
/* 209 */     return this.giopBufferSize;
/*     */   }
/*     */ 
/*     */   public int getGIOPBuffMgrStrategy(GIOPVersion paramGIOPVersion)
/*     */   {
/* 214 */     if (paramGIOPVersion != null) {
/* 215 */       if (paramGIOPVersion.equals(GIOPVersion.V1_0)) return 0;
/* 216 */       if (paramGIOPVersion.equals(GIOPVersion.V1_1)) return this.giop11BuffMgr;
/* 217 */       if (paramGIOPVersion.equals(GIOPVersion.V1_2)) return this.giop12BuffMgr;
/*     */     }
/*     */ 
/* 220 */     return 0;
/*     */   }
/*     */ 
/*     */   public short getGIOPTargetAddressPreference()
/*     */   {
/* 230 */     return this.giopTargetAddressPreference;
/*     */   }
/*     */ 
/*     */   public short getGIOPAddressDisposition()
/*     */   {
/* 235 */     return this.giopAddressDisposition;
/*     */   }
/*     */ 
/*     */   public boolean useByteOrderMarkers()
/*     */   {
/* 240 */     return this.useByteOrderMarkers;
/*     */   }
/*     */ 
/*     */   public boolean useByteOrderMarkersInEncapsulations()
/*     */   {
/* 245 */     return this.useByteOrderMarkersInEncaps;
/*     */   }
/*     */ 
/*     */   public boolean alwaysSendCodeSetServiceContext()
/*     */   {
/* 250 */     return this.alwaysSendCodeSetCtx;
/*     */   }
/*     */ 
/*     */   public boolean getPersistentPortInitialized()
/*     */   {
/* 255 */     return this.persistentPortInitialized;
/*     */   }
/*     */ 
/*     */   public int getPersistentServerPort()
/*     */   {
/* 266 */     if (this.persistentPortInitialized) {
/* 267 */       return this.persistentServerPort;
/*     */     }
/* 269 */     throw this.wrapper.persistentServerportNotSet(CompletionStatus.COMPLETED_MAYBE);
/*     */   }
/*     */ 
/*     */   public boolean getPersistentServerIdInitialized()
/*     */   {
/* 276 */     return this.persistentServerIdInitialized;
/*     */   }
/*     */ 
/*     */   public int getPersistentServerId()
/*     */   {
/* 303 */     if (this.persistentServerIdInitialized) {
/* 304 */       return this.persistentServerId;
/*     */     }
/* 306 */     throw this.wrapper.persistentServeridNotSet(CompletionStatus.COMPLETED_MAYBE);
/*     */   }
/*     */ 
/*     */   public boolean getServerIsORBActivated()
/*     */   {
/* 313 */     return this.serverIsORBActivated;
/*     */   }
/*     */ 
/*     */   public Class getBadServerIdHandler()
/*     */   {
/* 318 */     return this.badServerIdHandlerClass;
/*     */   }
/*     */ 
/*     */   public CodeSetComponentInfo getCodeSetComponentInfo()
/*     */   {
/* 327 */     return this.codesets;
/*     */   }
/*     */ 
/*     */   public ORBInitializer[] getORBInitializers()
/*     */   {
/* 332 */     return this.orbInitializers;
/*     */   }
/*     */ 
/*     */   public StringPair[] getORBInitialReferences()
/*     */   {
/* 337 */     return this.orbInitialReferences;
/*     */   }
/*     */ 
/*     */   public String getORBDefaultInitialReference()
/*     */   {
/* 342 */     return this.defaultInitRef;
/*     */   }
/*     */ 
/*     */   public String[] getORBDebugFlags()
/*     */   {
/* 347 */     return this.debugFlags;
/*     */   }
/*     */ 
/*     */   public Acceptor[] getAcceptors()
/*     */   {
/* 352 */     return this.acceptors;
/*     */   }
/*     */ 
/*     */   public CorbaContactInfoListFactory getCorbaContactInfoListFactory()
/*     */   {
/* 357 */     return this.corbaContactInfoListFactory;
/*     */   }
/*     */ 
/*     */   public String acceptorSocketType()
/*     */   {
/* 362 */     return this.acceptorSocketType;
/*     */   }
/*     */ 
/*     */   public boolean acceptorSocketUseSelectThreadToWait() {
/* 366 */     return this.acceptorSocketUseSelectThreadToWait;
/*     */   }
/*     */ 
/*     */   public boolean acceptorSocketUseWorkerThreadForEvent() {
/* 370 */     return this.acceptorSocketUseWorkerThreadForEvent;
/*     */   }
/*     */ 
/*     */   public String connectionSocketType() {
/* 374 */     return this.connectionSocketType;
/*     */   }
/*     */ 
/*     */   public boolean connectionSocketUseSelectThreadToWait() {
/* 378 */     return this.connectionSocketUseSelectThreadToWait;
/*     */   }
/*     */ 
/*     */   public boolean connectionSocketUseWorkerThreadForEvent() {
/* 382 */     return this.connectionSocketUseWorkerThreadForEvent;
/*     */   }
/*     */ 
/*     */   public boolean isJavaSerializationEnabled() {
/* 386 */     return this.enableJavaSerialization;
/*     */   }
/*     */ 
/*     */   public ReadTimeouts getTransportTCPReadTimeouts() {
/* 390 */     return this.readTimeouts;
/*     */   }
/*     */ 
/*     */   public boolean disableDirectByteBufferUse() {
/* 394 */     return this.disableDirectByteBufferUse;
/*     */   }
/*     */ 
/*     */   public boolean useRepId() {
/* 398 */     return this.useRepId;
/*     */   }
/*     */ 
/*     */   public ORBDataParserImpl(ORB paramORB, DataCollector paramDataCollector)
/*     */   {
/* 405 */     super(ParserTable.get().getParserData());
/* 406 */     this.orb = paramORB;
/* 407 */     this.wrapper = ORBUtilSystemException.get(paramORB, "orb.lifecycle");
/* 408 */     init(paramDataCollector);
/* 409 */     complete();
/*     */   }
/*     */ 
/*     */   public void complete()
/*     */   {
/* 414 */     this.codesets = new CodeSetComponentInfo(this.charData, this.wcharData);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orb.ORBDataParserImpl
 * JD-Core Version:    0.6.2
 */