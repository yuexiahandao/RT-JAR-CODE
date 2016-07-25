/*      */ package com.sun.jmx.snmp.daemon;
/*      */ 
/*      */ import com.sun.jmx.defaults.JmxProperties;
/*      */ import com.sun.jmx.snmp.IPAcl.SnmpAcl;
/*      */ import com.sun.jmx.snmp.InetAddressAcl;
/*      */ import com.sun.jmx.snmp.SnmpDefinitions;
/*      */ import com.sun.jmx.snmp.SnmpIpAddress;
/*      */ import com.sun.jmx.snmp.SnmpMessage;
/*      */ import com.sun.jmx.snmp.SnmpOid;
/*      */ import com.sun.jmx.snmp.SnmpParameters;
/*      */ import com.sun.jmx.snmp.SnmpPduFactory;
/*      */ import com.sun.jmx.snmp.SnmpPduFactoryBER;
/*      */ import com.sun.jmx.snmp.SnmpPduPacket;
/*      */ import com.sun.jmx.snmp.SnmpPduRequest;
/*      */ import com.sun.jmx.snmp.SnmpPduTrap;
/*      */ import com.sun.jmx.snmp.SnmpPeer;
/*      */ import com.sun.jmx.snmp.SnmpStatusException;
/*      */ import com.sun.jmx.snmp.SnmpTimeticks;
/*      */ import com.sun.jmx.snmp.SnmpTooBigException;
/*      */ import com.sun.jmx.snmp.SnmpVarBind;
/*      */ import com.sun.jmx.snmp.SnmpVarBindList;
/*      */ import com.sun.jmx.snmp.agent.SnmpErrorHandlerAgent;
/*      */ import com.sun.jmx.snmp.agent.SnmpMibAgent;
/*      */ import com.sun.jmx.snmp.agent.SnmpMibHandler;
/*      */ import com.sun.jmx.snmp.agent.SnmpUserDataFactory;
/*      */ import com.sun.jmx.snmp.tasks.ThreadService;
/*      */ import java.io.IOException;
/*      */ import java.io.InterruptedIOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.net.DatagramPacket;
/*      */ import java.net.DatagramSocket;
/*      */ import java.net.InetAddress;
/*      */ import java.net.SocketException;
/*      */ import java.net.UnknownHostException;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Vector;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.management.MBeanRegistration;
/*      */ import javax.management.MBeanServer;
/*      */ import javax.management.ObjectName;
/*      */ 
/*      */ public class SnmpAdaptorServer extends CommunicatorServer
/*      */   implements SnmpAdaptorServerMBean, MBeanRegistration, SnmpDefinitions, SnmpMibHandler
/*      */ {
/*  142 */   private int trapPort = 162;
/*      */ 
/*  148 */   private int informPort = 162;
/*      */ 
/*  155 */   InetAddress address = null;
/*      */ 
/*  160 */   private Object ipacl = null;
/*      */ 
/*  165 */   private SnmpPduFactory pduFactory = null;
/*      */ 
/*  170 */   private SnmpUserDataFactory userDataFactory = null;
/*      */ 
/*  176 */   private boolean authRespEnabled = true;
/*      */ 
/*  181 */   private boolean authTrapEnabled = true;
/*      */ 
/*  187 */   private SnmpOid enterpriseOid = new SnmpOid("1.3.6.1.4.1.42");
/*      */ 
/*  195 */   int bufferSize = 1024;
/*      */ 
/*  197 */   private transient long startUpTime = 0L;
/*  198 */   private transient DatagramSocket socket = null;
/*  199 */   transient DatagramSocket trapSocket = null;
/*  200 */   private transient SnmpSession informSession = null;
/*  201 */   private transient DatagramPacket packet = null;
/*  202 */   transient Vector<SnmpMibAgent> mibs = new Vector();
/*      */   private transient SnmpMibTree root;
/*  208 */   private transient boolean useAcl = true;
/*      */ 
/*  218 */   private int maxTries = 3;
/*      */ 
/*  224 */   private int timeout = 3000;
/*      */ 
/*  232 */   int snmpOutTraps = 0;
/*      */ 
/*  237 */   private int snmpOutGetResponses = 0;
/*      */ 
/*  242 */   private int snmpOutGenErrs = 0;
/*      */ 
/*  247 */   private int snmpOutBadValues = 0;
/*      */ 
/*  252 */   private int snmpOutNoSuchNames = 0;
/*      */ 
/*  257 */   private int snmpOutTooBigs = 0;
/*      */ 
/*  262 */   int snmpOutPkts = 0;
/*      */ 
/*  267 */   private int snmpInASNParseErrs = 0;
/*      */ 
/*  272 */   private int snmpInBadCommunityUses = 0;
/*      */ 
/*  277 */   private int snmpInBadCommunityNames = 0;
/*      */ 
/*  282 */   private int snmpInBadVersions = 0;
/*      */ 
/*  287 */   private int snmpInGetRequests = 0;
/*      */ 
/*  292 */   private int snmpInGetNexts = 0;
/*      */ 
/*  297 */   private int snmpInSetRequests = 0;
/*      */ 
/*  302 */   private int snmpInPkts = 0;
/*      */ 
/*  307 */   private int snmpInTotalReqVars = 0;
/*      */ 
/*  312 */   private int snmpInTotalSetVars = 0;
/*      */ 
/*  317 */   private int snmpSilentDrops = 0;
/*      */   private static final String InterruptSysCallMsg = "Interrupted system call";
/*  321 */   static final SnmpOid sysUpTimeOid = new SnmpOid("1.3.6.1.2.1.1.3.0");
/*  322 */   static final SnmpOid snmpTrapOidOid = new SnmpOid("1.3.6.1.6.3.1.1.4.1.0");
/*      */   private ThreadService threadService;
/*  326 */   private static int threadNumber = 6;
/*      */ 
/*      */   public SnmpAdaptorServer()
/*      */   {
/*  353 */     this(true, null, 161, null);
/*      */   }
/*      */ 
/*      */   public SnmpAdaptorServer(int paramInt)
/*      */   {
/*  365 */     this(true, null, paramInt, null);
/*      */   }
/*      */ 
/*      */   public SnmpAdaptorServer(InetAddressAcl paramInetAddressAcl)
/*      */   {
/*  378 */     this(false, paramInetAddressAcl, 161, null);
/*      */   }
/*      */ 
/*      */   public SnmpAdaptorServer(InetAddress paramInetAddress)
/*      */   {
/*  392 */     this(true, null, 161, paramInetAddress);
/*      */   }
/*      */ 
/*      */   public SnmpAdaptorServer(InetAddressAcl paramInetAddressAcl, int paramInt)
/*      */   {
/*  407 */     this(false, paramInetAddressAcl, paramInt, null);
/*      */   }
/*      */ 
/*      */   public SnmpAdaptorServer(int paramInt, InetAddress paramInetAddress)
/*      */   {
/*  420 */     this(true, null, paramInt, paramInetAddress);
/*      */   }
/*      */ 
/*      */   public SnmpAdaptorServer(InetAddressAcl paramInetAddressAcl, InetAddress paramInetAddress)
/*      */   {
/*  434 */     this(false, paramInetAddressAcl, 161, paramInetAddress);
/*      */   }
/*      */ 
/*      */   public SnmpAdaptorServer(InetAddressAcl paramInetAddressAcl, int paramInt, InetAddress paramInetAddress)
/*      */   {
/*  450 */     this(false, paramInetAddressAcl, paramInt, paramInetAddress);
/*      */   }
/*      */ 
/*      */   public SnmpAdaptorServer(boolean paramBoolean, int paramInt, InetAddress paramInetAddress)
/*      */   {
/*  470 */     this(paramBoolean, null, paramInt, paramInetAddress);
/*      */   }
/*      */ 
/*      */   private SnmpAdaptorServer(boolean paramBoolean, InetAddressAcl paramInetAddressAcl, int paramInt, InetAddress paramInetAddress)
/*      */   {
/*  478 */     super(4);
/*      */ 
/*  483 */     if ((paramInetAddressAcl == null) && (paramBoolean)) {
/*      */       try {
/*  485 */         paramInetAddressAcl = new SnmpAcl("SNMP protocol adaptor IP ACL");
/*      */       }
/*      */       catch (UnknownHostException localUnknownHostException) {
/*  488 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  489 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "constructor", "UnknowHostException when creating ACL", localUnknownHostException);
/*      */         }
/*      */       }
/*      */     }
/*      */     else {
/*  494 */       this.useAcl = ((paramInetAddressAcl != null) || (paramBoolean));
/*      */     }
/*      */ 
/*  497 */     init(paramInetAddressAcl, paramInt, paramInetAddress);
/*      */   }
/*      */ 
/*      */   public int getServedClientCount()
/*      */   {
/*  512 */     return super.getServedClientCount();
/*      */   }
/*      */ 
/*      */   public int getActiveClientCount()
/*      */   {
/*  523 */     return super.getActiveClientCount();
/*      */   }
/*      */ 
/*      */   public int getMaxActiveClientCount()
/*      */   {
/*  534 */     return super.getMaxActiveClientCount();
/*      */   }
/*      */ 
/*      */   public void setMaxActiveClientCount(int paramInt)
/*      */     throws IllegalStateException
/*      */   {
/*  548 */     super.setMaxActiveClientCount(paramInt);
/*      */   }
/*      */ 
/*      */   public InetAddressAcl getInetAddressAcl()
/*      */   {
/*  558 */     return (InetAddressAcl)this.ipacl;
/*      */   }
/*      */ 
/*      */   public Integer getTrapPort()
/*      */   {
/*  568 */     return new Integer(this.trapPort);
/*      */   }
/*      */ 
/*      */   public void setTrapPort(Integer paramInteger)
/*      */   {
/*  577 */     setTrapPort(paramInteger.intValue());
/*      */   }
/*      */ 
/*      */   public void setTrapPort(int paramInt)
/*      */   {
/*  586 */     int i = paramInt;
/*  587 */     if (i < 0) throw new IllegalArgumentException("Trap port cannot be a negative value");
/*      */ 
/*  589 */     this.trapPort = i;
/*      */   }
/*      */ 
/*      */   public int getInformPort()
/*      */   {
/*  599 */     return this.informPort;
/*      */   }
/*      */ 
/*      */   public void setInformPort(int paramInt)
/*      */   {
/*  609 */     if (paramInt < 0) {
/*  610 */       throw new IllegalArgumentException("Inform request port cannot be a negative value");
/*      */     }
/*  612 */     this.informPort = paramInt;
/*      */   }
/*      */ 
/*      */   public String getProtocol()
/*      */   {
/*  621 */     return "snmp";
/*      */   }
/*      */ 
/*      */   public Integer getBufferSize()
/*      */   {
/*  633 */     return new Integer(this.bufferSize);
/*      */   }
/*      */ 
/*      */   public void setBufferSize(Integer paramInteger)
/*      */     throws IllegalStateException
/*      */   {
/*  648 */     if ((this.state == 0) || (this.state == 3)) {
/*  649 */       throw new IllegalStateException("Stop server before carrying out this operation");
/*      */     }
/*      */ 
/*  652 */     this.bufferSize = paramInteger.intValue();
/*      */   }
/*      */ 
/*      */   public final int getMaxTries()
/*      */   {
/*  662 */     return this.maxTries;
/*      */   }
/*      */ 
/*      */   public final synchronized void setMaxTries(int paramInt)
/*      */   {
/*  671 */     if (paramInt < 0)
/*  672 */       throw new IllegalArgumentException();
/*  673 */     this.maxTries = paramInt;
/*      */   }
/*      */ 
/*      */   public final int getTimeout()
/*      */   {
/*  682 */     return this.timeout;
/*      */   }
/*      */ 
/*      */   public final synchronized void setTimeout(int paramInt)
/*      */   {
/*  690 */     if (paramInt < 0)
/*  691 */       throw new IllegalArgumentException();
/*  692 */     this.timeout = paramInt;
/*      */   }
/*      */ 
/*      */   public SnmpPduFactory getPduFactory()
/*      */   {
/*  701 */     return this.pduFactory;
/*      */   }
/*      */ 
/*      */   public void setPduFactory(SnmpPduFactory paramSnmpPduFactory)
/*      */   {
/*  710 */     if (paramSnmpPduFactory == null)
/*  711 */       this.pduFactory = new SnmpPduFactoryBER();
/*      */     else
/*  713 */       this.pduFactory = paramSnmpPduFactory;
/*      */   }
/*      */ 
/*      */   public void setUserDataFactory(SnmpUserDataFactory paramSnmpUserDataFactory)
/*      */   {
/*  723 */     this.userDataFactory = paramSnmpUserDataFactory;
/*      */   }
/*      */ 
/*      */   public SnmpUserDataFactory getUserDataFactory()
/*      */   {
/*  733 */     return this.userDataFactory;
/*      */   }
/*      */ 
/*      */   public boolean getAuthTrapEnabled()
/*      */   {
/*  749 */     return this.authTrapEnabled;
/*      */   }
/*      */ 
/*      */   public void setAuthTrapEnabled(boolean paramBoolean)
/*      */   {
/*  759 */     this.authTrapEnabled = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getAuthRespEnabled()
/*      */   {
/*  776 */     return this.authRespEnabled;
/*      */   }
/*      */ 
/*      */   public void setAuthRespEnabled(boolean paramBoolean)
/*      */   {
/*  786 */     this.authRespEnabled = paramBoolean;
/*      */   }
/*      */ 
/*      */   public String getEnterpriseOid()
/*      */   {
/*  797 */     return this.enterpriseOid.toString();
/*      */   }
/*      */ 
/*      */   public void setEnterpriseOid(String paramString)
/*      */     throws IllegalArgumentException
/*      */   {
/*  808 */     this.enterpriseOid = new SnmpOid(paramString);
/*      */   }
/*      */ 
/*      */   public String[] getMibs()
/*      */   {
/*  817 */     String[] arrayOfString = new String[this.mibs.size()];
/*  818 */     int i = 0;
/*  819 */     for (Enumeration localEnumeration = this.mibs.elements(); localEnumeration.hasMoreElements(); ) {
/*  820 */       SnmpMibAgent localSnmpMibAgent = (SnmpMibAgent)localEnumeration.nextElement();
/*  821 */       arrayOfString[(i++)] = localSnmpMibAgent.getMibName();
/*      */     }
/*  823 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   public Long getSnmpOutTraps()
/*      */   {
/*  835 */     return new Long(this.snmpOutTraps);
/*      */   }
/*      */ 
/*      */   public Long getSnmpOutGetResponses()
/*      */   {
/*  844 */     return new Long(this.snmpOutGetResponses);
/*      */   }
/*      */ 
/*      */   public Long getSnmpOutGenErrs()
/*      */   {
/*  853 */     return new Long(this.snmpOutGenErrs);
/*      */   }
/*      */ 
/*      */   public Long getSnmpOutBadValues()
/*      */   {
/*  862 */     return new Long(this.snmpOutBadValues);
/*      */   }
/*      */ 
/*      */   public Long getSnmpOutNoSuchNames()
/*      */   {
/*  871 */     return new Long(this.snmpOutNoSuchNames);
/*      */   }
/*      */ 
/*      */   public Long getSnmpOutTooBigs()
/*      */   {
/*  880 */     return new Long(this.snmpOutTooBigs);
/*      */   }
/*      */ 
/*      */   public Long getSnmpInASNParseErrs()
/*      */   {
/*  889 */     return new Long(this.snmpInASNParseErrs);
/*      */   }
/*      */ 
/*      */   public Long getSnmpInBadCommunityUses()
/*      */   {
/*  898 */     return new Long(this.snmpInBadCommunityUses);
/*      */   }
/*      */ 
/*      */   public Long getSnmpInBadCommunityNames()
/*      */   {
/*  908 */     return new Long(this.snmpInBadCommunityNames);
/*      */   }
/*      */ 
/*      */   public Long getSnmpInBadVersions()
/*      */   {
/*  917 */     return new Long(this.snmpInBadVersions);
/*      */   }
/*      */ 
/*      */   public Long getSnmpOutPkts()
/*      */   {
/*  926 */     return new Long(this.snmpOutPkts);
/*      */   }
/*      */ 
/*      */   public Long getSnmpInPkts()
/*      */   {
/*  935 */     return new Long(this.snmpInPkts);
/*      */   }
/*      */ 
/*      */   public Long getSnmpInGetRequests()
/*      */   {
/*  944 */     return new Long(this.snmpInGetRequests);
/*      */   }
/*      */ 
/*      */   public Long getSnmpInGetNexts()
/*      */   {
/*  953 */     return new Long(this.snmpInGetNexts);
/*      */   }
/*      */ 
/*      */   public Long getSnmpInSetRequests()
/*      */   {
/*  962 */     return new Long(this.snmpInSetRequests);
/*      */   }
/*      */ 
/*      */   public Long getSnmpInTotalSetVars()
/*      */   {
/*  971 */     return new Long(this.snmpInTotalSetVars);
/*      */   }
/*      */ 
/*      */   public Long getSnmpInTotalReqVars()
/*      */   {
/*  980 */     return new Long(this.snmpInTotalReqVars);
/*      */   }
/*      */ 
/*      */   public Long getSnmpSilentDrops()
/*      */   {
/*  992 */     return new Long(this.snmpSilentDrops);
/*      */   }
/*      */ 
/*      */   public Long getSnmpProxyDrops()
/*      */   {
/* 1004 */     return new Long(0L);
/*      */   }
/*      */ 
/*      */   public ObjectName preRegister(MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*      */     throws Exception
/*      */   {
/* 1033 */     if (paramObjectName == null) {
/* 1034 */       paramObjectName = new ObjectName(paramMBeanServer.getDefaultDomain() + ":" + "name=SnmpAdaptorServer");
/*      */     }
/*      */ 
/* 1037 */     return super.preRegister(paramMBeanServer, paramObjectName);
/*      */   }
/*      */ 
/*      */   public void postRegister(Boolean paramBoolean)
/*      */   {
/* 1044 */     super.postRegister(paramBoolean);
/*      */   }
/*      */ 
/*      */   public void preDeregister()
/*      */     throws Exception
/*      */   {
/* 1051 */     super.preDeregister();
/*      */   }
/*      */ 
/*      */   public void postDeregister()
/*      */   {
/* 1058 */     super.postDeregister();
/*      */   }
/*      */ 
/*      */   public SnmpMibHandler addMib(SnmpMibAgent paramSnmpMibAgent)
/*      */     throws IllegalArgumentException
/*      */   {
/* 1072 */     if (paramSnmpMibAgent == null) {
/* 1073 */       throw new IllegalArgumentException();
/*      */     }
/*      */ 
/* 1076 */     if (!this.mibs.contains(paramSnmpMibAgent)) {
/* 1077 */       this.mibs.addElement(paramSnmpMibAgent);
/*      */     }
/* 1079 */     this.root.register(paramSnmpMibAgent);
/*      */ 
/* 1081 */     return this;
/*      */   }
/*      */ 
/*      */   public SnmpMibHandler addMib(SnmpMibAgent paramSnmpMibAgent, SnmpOid[] paramArrayOfSnmpOid)
/*      */     throws IllegalArgumentException
/*      */   {
/* 1102 */     if (paramSnmpMibAgent == null) {
/* 1103 */       throw new IllegalArgumentException();
/*      */     }
/*      */ 
/* 1107 */     if (paramArrayOfSnmpOid == null) {
/* 1108 */       return addMib(paramSnmpMibAgent);
/*      */     }
/* 1110 */     if (!this.mibs.contains(paramSnmpMibAgent)) {
/* 1111 */       this.mibs.addElement(paramSnmpMibAgent);
/*      */     }
/* 1113 */     for (int i = 0; i < paramArrayOfSnmpOid.length; i++) {
/* 1114 */       this.root.register(paramSnmpMibAgent, paramArrayOfSnmpOid[i].longValue());
/*      */     }
/* 1116 */     return this;
/*      */   }
/*      */ 
/*      */   public SnmpMibHandler addMib(SnmpMibAgent paramSnmpMibAgent, String paramString)
/*      */     throws IllegalArgumentException
/*      */   {
/* 1134 */     return addMib(paramSnmpMibAgent);
/*      */   }
/*      */ 
/*      */   public SnmpMibHandler addMib(SnmpMibAgent paramSnmpMibAgent, String paramString, SnmpOid[] paramArrayOfSnmpOid)
/*      */     throws IllegalArgumentException
/*      */   {
/* 1157 */     return addMib(paramSnmpMibAgent, paramArrayOfSnmpOid);
/*      */   }
/*      */ 
/*      */   public boolean removeMib(SnmpMibAgent paramSnmpMibAgent, String paramString)
/*      */   {
/* 1175 */     return removeMib(paramSnmpMibAgent);
/*      */   }
/*      */ 
/*      */   public boolean removeMib(SnmpMibAgent paramSnmpMibAgent)
/*      */   {
/* 1187 */     this.root.unregister(paramSnmpMibAgent);
/* 1188 */     return this.mibs.removeElement(paramSnmpMibAgent);
/*      */   }
/*      */ 
/*      */   public boolean removeMib(SnmpMibAgent paramSnmpMibAgent, SnmpOid[] paramArrayOfSnmpOid)
/*      */   {
/* 1203 */     this.root.unregister(paramSnmpMibAgent, paramArrayOfSnmpOid);
/* 1204 */     return this.mibs.removeElement(paramSnmpMibAgent);
/*      */   }
/*      */ 
/*      */   public boolean removeMib(SnmpMibAgent paramSnmpMibAgent, String paramString, SnmpOid[] paramArrayOfSnmpOid)
/*      */   {
/* 1222 */     return removeMib(paramSnmpMibAgent, paramArrayOfSnmpOid);
/*      */   }
/*      */ 
/*      */   protected void doBind()
/*      */     throws CommunicationException, InterruptedException
/*      */   {
/*      */     try
/*      */     {
/* 1235 */       synchronized (this) {
/* 1236 */         this.socket = new DatagramSocket(this.port, this.address);
/*      */       }
/* 1238 */       this.dbgTag = makeDebugTag();
/*      */     } catch (SocketException localSocketException) {
/* 1240 */       if (localSocketException.getMessage().equals("Interrupted system call")) {
/* 1241 */         throw new InterruptedException(localSocketException.toString());
/*      */       }
/* 1243 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1244 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "doBind", "cannot bind on port " + this.port);
/*      */       }
/*      */ 
/* 1247 */       throw new CommunicationException(localSocketException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getPort()
/*      */   {
/* 1259 */     synchronized (this) {
/* 1260 */       if (this.socket != null) return this.socket.getLocalPort();
/*      */     }
/* 1262 */     return super.getPort();
/*      */   }
/*      */ 
/*      */   protected void doUnbind()
/*      */     throws CommunicationException, InterruptedException
/*      */   {
/* 1270 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 1271 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "doUnbind", "Finally close the socket");
/*      */     }
/*      */ 
/* 1274 */     synchronized (this) {
/* 1275 */       if (this.socket != null) {
/* 1276 */         this.socket.close();
/* 1277 */         this.socket = null;
/*      */       }
/*      */     }
/*      */ 
/* 1281 */     closeTrapSocketIfNeeded();
/* 1282 */     closeInformSocketIfNeeded();
/*      */   }
/*      */ 
/*      */   void createSnmpRequestHandler(SnmpAdaptorServer paramSnmpAdaptorServer, int paramInt, DatagramSocket paramDatagramSocket, DatagramPacket paramDatagramPacket, SnmpMibTree paramSnmpMibTree, Vector paramVector, Object paramObject, SnmpPduFactory paramSnmpPduFactory, SnmpUserDataFactory paramSnmpUserDataFactory, MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*      */   {
/* 1291 */     SnmpRequestHandler localSnmpRequestHandler = new SnmpRequestHandler(this, paramInt, paramDatagramSocket, paramDatagramPacket, paramSnmpMibTree, paramVector, paramObject, paramSnmpPduFactory, paramSnmpUserDataFactory, paramMBeanServer, paramObjectName);
/*      */ 
/* 1294 */     this.threadService.submitTask(localSnmpRequestHandler);
/*      */   }
/*      */ 
/*      */   protected void doReceive()
/*      */     throws CommunicationException, InterruptedException
/*      */   {
/*      */     try
/*      */     {
/* 1307 */       this.packet = new DatagramPacket(new byte[this.bufferSize], this.bufferSize);
/* 1308 */       this.socket.receive(this.packet);
/* 1309 */       int i = getState();
/*      */ 
/* 1311 */       if (i != 0) {
/* 1312 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 1313 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "doReceive", "received a message but state not online, returning.");
/*      */         }
/*      */ 
/* 1316 */         return;
/*      */       }
/*      */ 
/* 1319 */       createSnmpRequestHandler(this, this.servedClientCount, this.socket, this.packet, this.root, this.mibs, this.ipacl, this.pduFactory, this.userDataFactory, this.topMBS, this.objectName);
/*      */     }
/*      */     catch (SocketException localSocketException)
/*      */     {
/* 1325 */       if (localSocketException.getMessage().equals("Interrupted system call")) {
/* 1326 */         throw new InterruptedException(localSocketException.toString());
/*      */       }
/* 1328 */       throw new CommunicationException(localSocketException);
/*      */     } catch (InterruptedIOException localInterruptedIOException) {
/* 1330 */       throw new InterruptedException(localInterruptedIOException.toString());
/*      */     } catch (CommunicationException localCommunicationException) {
/* 1332 */       throw localCommunicationException;
/*      */     } catch (Exception localException) {
/* 1334 */       throw new CommunicationException(localException);
/*      */     }
/* 1336 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER))
/* 1337 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "doReceive", "received a message");
/*      */   }
/*      */ 
/*      */   protected void doError(Exception paramException)
/*      */     throws CommunicationException
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void doProcess()
/*      */     throws CommunicationException, InterruptedException
/*      */   {
/*      */   }
/*      */ 
/*      */   protected int getBindTries()
/*      */   {
/* 1361 */     return 1;
/*      */   }
/*      */ 
/*      */   public void stop()
/*      */   {
/* 1373 */     int i = getPort();
/* 1374 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 1375 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "stop", "Stopping: using port " + i);
/*      */     }
/*      */ 
/* 1378 */     if ((this.state == 0) || (this.state == 3)) {
/* 1379 */       super.stop();
/*      */       try {
/* 1381 */         DatagramSocket localDatagramSocket = new DatagramSocket(0);
/*      */         try {
/* 1383 */           byte[] arrayOfByte = new byte[1];
/*      */           DatagramPacket localDatagramPacket;
/* 1386 */           if (this.address != null)
/* 1387 */             localDatagramPacket = new DatagramPacket(arrayOfByte, 1, this.address, i);
/*      */           else {
/* 1389 */             localDatagramPacket = new DatagramPacket(arrayOfByte, 1, InetAddress.getLocalHost(), i);
/*      */           }
/*      */ 
/* 1392 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 1393 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "stop", "Sending: using port " + i);
/*      */           }
/*      */ 
/* 1396 */           localDatagramSocket.send(localDatagramPacket);
/*      */         } finally {
/* 1398 */           localDatagramSocket.close();
/*      */         }
/*      */       } catch (Throwable localThrowable) {
/* 1401 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))
/* 1402 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "stop", "Got unexpected Throwable", localThrowable);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void snmpV1Trap(int paramInt1, int paramInt2, SnmpVarBindList paramSnmpVarBindList)
/*      */     throws IOException, SnmpStatusException
/*      */   {
/* 1431 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 1432 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "snmpV1Trap", "generic=" + paramInt1 + ", specific=" + paramInt2);
/*      */     }
/*      */ 
/* 1439 */     SnmpPduTrap localSnmpPduTrap = new SnmpPduTrap();
/* 1440 */     localSnmpPduTrap.address = null;
/* 1441 */     localSnmpPduTrap.port = this.trapPort;
/* 1442 */     localSnmpPduTrap.type = 164;
/* 1443 */     localSnmpPduTrap.version = 0;
/* 1444 */     localSnmpPduTrap.community = null;
/* 1445 */     localSnmpPduTrap.enterprise = this.enterpriseOid;
/* 1446 */     localSnmpPduTrap.genericTrap = paramInt1;
/* 1447 */     localSnmpPduTrap.specificTrap = paramInt2;
/* 1448 */     localSnmpPduTrap.timeStamp = getSysUpTime();
/*      */ 
/* 1450 */     if (paramSnmpVarBindList != null) {
/* 1451 */       localSnmpPduTrap.varBindList = new SnmpVarBind[paramSnmpVarBindList.size()];
/* 1452 */       paramSnmpVarBindList.copyInto(localSnmpPduTrap.varBindList);
/*      */     }
/*      */     else {
/* 1455 */       localSnmpPduTrap.varBindList = null;
/*      */     }
/*      */     try
/*      */     {
/* 1459 */       if (this.address != null)
/* 1460 */         localSnmpPduTrap.agentAddr = handleMultipleIpVersion(this.address.getAddress());
/* 1461 */       else localSnmpPduTrap.agentAddr = handleMultipleIpVersion(InetAddress.getLocalHost().getAddress()); 
/*      */     }
/*      */     catch (UnknownHostException localUnknownHostException)
/*      */     {
/* 1464 */       byte[] arrayOfByte = new byte[4];
/* 1465 */       localSnmpPduTrap.agentAddr = handleMultipleIpVersion(arrayOfByte);
/*      */     }
/*      */ 
/* 1470 */     sendTrapPdu(localSnmpPduTrap);
/*      */   }
/*      */ 
/*      */   private SnmpIpAddress handleMultipleIpVersion(byte[] paramArrayOfByte) {
/* 1474 */     if (paramArrayOfByte.length == 4) {
/* 1475 */       return new SnmpIpAddress(paramArrayOfByte);
/*      */     }
/* 1477 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 1478 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "handleMultipleIPVersion", "Not an IPv4 address, return null");
/*      */     }
/*      */ 
/* 1482 */     return null;
/*      */   }
/*      */ 
/*      */   public void snmpV1Trap(InetAddress paramInetAddress, String paramString, int paramInt1, int paramInt2, SnmpVarBindList paramSnmpVarBindList)
/*      */     throws IOException, SnmpStatusException
/*      */   {
/* 1506 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 1507 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "snmpV1Trap", "generic=" + paramInt1 + ", specific=" + paramInt2);
/*      */     }
/*      */ 
/* 1514 */     SnmpPduTrap localSnmpPduTrap = new SnmpPduTrap();
/* 1515 */     localSnmpPduTrap.address = null;
/* 1516 */     localSnmpPduTrap.port = this.trapPort;
/* 1517 */     localSnmpPduTrap.type = 164;
/* 1518 */     localSnmpPduTrap.version = 0;
/*      */ 
/* 1520 */     if (paramString != null)
/* 1521 */       localSnmpPduTrap.community = paramString.getBytes();
/*      */     else {
/* 1523 */       localSnmpPduTrap.community = null;
/*      */     }
/* 1525 */     localSnmpPduTrap.enterprise = this.enterpriseOid;
/* 1526 */     localSnmpPduTrap.genericTrap = paramInt1;
/* 1527 */     localSnmpPduTrap.specificTrap = paramInt2;
/* 1528 */     localSnmpPduTrap.timeStamp = getSysUpTime();
/*      */ 
/* 1530 */     if (paramSnmpVarBindList != null) {
/* 1531 */       localSnmpPduTrap.varBindList = new SnmpVarBind[paramSnmpVarBindList.size()];
/* 1532 */       paramSnmpVarBindList.copyInto(localSnmpPduTrap.varBindList);
/*      */     }
/*      */     else {
/* 1535 */       localSnmpPduTrap.varBindList = null;
/*      */     }
/*      */     try
/*      */     {
/* 1539 */       if (this.address != null)
/* 1540 */         localSnmpPduTrap.agentAddr = handleMultipleIpVersion(this.address.getAddress());
/* 1541 */       else localSnmpPduTrap.agentAddr = handleMultipleIpVersion(InetAddress.getLocalHost().getAddress()); 
/*      */     }
/*      */     catch (UnknownHostException localUnknownHostException)
/*      */     {
/* 1544 */       byte[] arrayOfByte = new byte[4];
/* 1545 */       localSnmpPduTrap.agentAddr = handleMultipleIpVersion(arrayOfByte);
/*      */     }
/*      */ 
/* 1550 */     if (paramInetAddress != null)
/* 1551 */       sendTrapPdu(paramInetAddress, localSnmpPduTrap);
/*      */     else
/* 1553 */       sendTrapPdu(localSnmpPduTrap);
/*      */   }
/*      */ 
/*      */   public void snmpV1Trap(InetAddress paramInetAddress, SnmpIpAddress paramSnmpIpAddress, String paramString, SnmpOid paramSnmpOid, int paramInt1, int paramInt2, SnmpVarBindList paramSnmpVarBindList, SnmpTimeticks paramSnmpTimeticks)
/*      */     throws IOException, SnmpStatusException
/*      */   {
/* 1588 */     snmpV1Trap(paramInetAddress, this.trapPort, paramSnmpIpAddress, paramString, paramSnmpOid, paramInt1, paramInt2, paramSnmpVarBindList, paramSnmpTimeticks);
/*      */   }
/*      */ 
/*      */   public void snmpV1Trap(SnmpPeer paramSnmpPeer, SnmpIpAddress paramSnmpIpAddress, SnmpOid paramSnmpOid, int paramInt1, int paramInt2, SnmpVarBindList paramSnmpVarBindList, SnmpTimeticks paramSnmpTimeticks)
/*      */     throws IOException, SnmpStatusException
/*      */   {
/* 1628 */     SnmpParameters localSnmpParameters = (SnmpParameters)paramSnmpPeer.getParams();
/* 1629 */     snmpV1Trap(paramSnmpPeer.getDestAddr(), paramSnmpPeer.getDestPort(), paramSnmpIpAddress, localSnmpParameters.getRdCommunity(), paramSnmpOid, paramInt1, paramInt2, paramSnmpVarBindList, paramSnmpTimeticks);
/*      */   }
/*      */ 
/*      */   private void snmpV1Trap(InetAddress paramInetAddress, int paramInt1, SnmpIpAddress paramSnmpIpAddress, String paramString, SnmpOid paramSnmpOid, int paramInt2, int paramInt3, SnmpVarBindList paramSnmpVarBindList, SnmpTimeticks paramSnmpTimeticks)
/*      */     throws IOException, SnmpStatusException
/*      */   {
/* 1651 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 1652 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "snmpV1Trap", "generic=" + paramInt2 + ", specific=" + paramInt3);
/*      */     }
/*      */ 
/* 1659 */     SnmpPduTrap localSnmpPduTrap = new SnmpPduTrap();
/* 1660 */     localSnmpPduTrap.address = null;
/* 1661 */     localSnmpPduTrap.port = paramInt1;
/* 1662 */     localSnmpPduTrap.type = 164;
/* 1663 */     localSnmpPduTrap.version = 0;
/*      */ 
/* 1666 */     if (paramString != null)
/* 1667 */       localSnmpPduTrap.community = paramString.getBytes();
/*      */     else {
/* 1669 */       localSnmpPduTrap.community = null;
/*      */     }
/*      */ 
/* 1673 */     if (paramSnmpOid != null)
/* 1674 */       localSnmpPduTrap.enterprise = paramSnmpOid;
/*      */     else {
/* 1676 */       localSnmpPduTrap.enterprise = this.enterpriseOid;
/*      */     }
/* 1678 */     localSnmpPduTrap.genericTrap = paramInt2;
/* 1679 */     localSnmpPduTrap.specificTrap = paramInt3;
/*      */ 
/* 1681 */     if (paramSnmpTimeticks != null)
/* 1682 */       localSnmpPduTrap.timeStamp = paramSnmpTimeticks.longValue();
/*      */     else {
/* 1684 */       localSnmpPduTrap.timeStamp = getSysUpTime();
/*      */     }
/*      */ 
/* 1687 */     if (paramSnmpVarBindList != null) {
/* 1688 */       localSnmpPduTrap.varBindList = new SnmpVarBind[paramSnmpVarBindList.size()];
/* 1689 */       paramSnmpVarBindList.copyInto(localSnmpPduTrap.varBindList);
/*      */     }
/*      */     else {
/* 1692 */       localSnmpPduTrap.varBindList = null;
/*      */     }
/* 1694 */     if (paramSnmpIpAddress == null)
/*      */     {
/*      */       try
/*      */       {
/* 1698 */         InetAddress localInetAddress = this.address != null ? this.address : InetAddress.getLocalHost();
/*      */ 
/* 1700 */         paramSnmpIpAddress = handleMultipleIpVersion(localInetAddress.getAddress());
/*      */       } catch (UnknownHostException localUnknownHostException) {
/* 1702 */         byte[] arrayOfByte = new byte[4];
/* 1703 */         paramSnmpIpAddress = handleMultipleIpVersion(arrayOfByte);
/*      */       }
/*      */     }
/*      */ 
/* 1707 */     localSnmpPduTrap.agentAddr = paramSnmpIpAddress;
/*      */ 
/* 1712 */     if (paramInetAddress != null)
/* 1713 */       sendTrapPdu(paramInetAddress, localSnmpPduTrap);
/*      */     else
/* 1715 */       sendTrapPdu(localSnmpPduTrap);
/*      */   }
/*      */ 
/*      */   public void snmpV2Trap(SnmpPeer paramSnmpPeer, SnmpOid paramSnmpOid, SnmpVarBindList paramSnmpVarBindList, SnmpTimeticks paramSnmpTimeticks)
/*      */     throws IOException, SnmpStatusException
/*      */   {
/* 1753 */     SnmpParameters localSnmpParameters = (SnmpParameters)paramSnmpPeer.getParams();
/* 1754 */     snmpV2Trap(paramSnmpPeer.getDestAddr(), paramSnmpPeer.getDestPort(), localSnmpParameters.getRdCommunity(), paramSnmpOid, paramSnmpVarBindList, paramSnmpTimeticks);
/*      */   }
/*      */ 
/*      */   public void snmpV2Trap(SnmpOid paramSnmpOid, SnmpVarBindList paramSnmpVarBindList)
/*      */     throws IOException, SnmpStatusException
/*      */   {
/* 1787 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 1788 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "snmpV2Trap", "trapOid=" + paramSnmpOid);
/*      */     }
/*      */ 
/* 1795 */     SnmpPduRequest localSnmpPduRequest = new SnmpPduRequest();
/* 1796 */     localSnmpPduRequest.address = null;
/* 1797 */     localSnmpPduRequest.port = this.trapPort;
/* 1798 */     localSnmpPduRequest.type = 167;
/* 1799 */     localSnmpPduRequest.version = 1;
/* 1800 */     localSnmpPduRequest.community = null;
/*      */     SnmpVarBindList localSnmpVarBindList;
/* 1803 */     if (paramSnmpVarBindList != null)
/* 1804 */       localSnmpVarBindList = (SnmpVarBindList)paramSnmpVarBindList.clone();
/*      */     else
/* 1806 */       localSnmpVarBindList = new SnmpVarBindList(2);
/* 1807 */     SnmpTimeticks localSnmpTimeticks = new SnmpTimeticks(getSysUpTime());
/* 1808 */     localSnmpVarBindList.insertElementAt(new SnmpVarBind(snmpTrapOidOid, paramSnmpOid), 0);
/* 1809 */     localSnmpVarBindList.insertElementAt(new SnmpVarBind(sysUpTimeOid, localSnmpTimeticks), 0);
/*      */ 
/* 1811 */     localSnmpPduRequest.varBindList = new SnmpVarBind[localSnmpVarBindList.size()];
/* 1812 */     localSnmpVarBindList.copyInto(localSnmpPduRequest.varBindList);
/*      */ 
/* 1816 */     sendTrapPdu(localSnmpPduRequest);
/*      */   }
/*      */ 
/*      */   public void snmpV2Trap(InetAddress paramInetAddress, String paramString, SnmpOid paramSnmpOid, SnmpVarBindList paramSnmpVarBindList)
/*      */     throws IOException, SnmpStatusException
/*      */   {
/* 1847 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 1848 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "snmpV2Trap", "trapOid=" + paramSnmpOid);
/*      */     }
/*      */ 
/* 1855 */     SnmpPduRequest localSnmpPduRequest = new SnmpPduRequest();
/* 1856 */     localSnmpPduRequest.address = null;
/* 1857 */     localSnmpPduRequest.port = this.trapPort;
/* 1858 */     localSnmpPduRequest.type = 167;
/* 1859 */     localSnmpPduRequest.version = 1;
/*      */ 
/* 1861 */     if (paramString != null)
/* 1862 */       localSnmpPduRequest.community = paramString.getBytes();
/*      */     else
/* 1864 */       localSnmpPduRequest.community = null;
/*      */     SnmpVarBindList localSnmpVarBindList;
/* 1867 */     if (paramSnmpVarBindList != null)
/* 1868 */       localSnmpVarBindList = (SnmpVarBindList)paramSnmpVarBindList.clone();
/*      */     else
/* 1870 */       localSnmpVarBindList = new SnmpVarBindList(2);
/* 1871 */     SnmpTimeticks localSnmpTimeticks = new SnmpTimeticks(getSysUpTime());
/* 1872 */     localSnmpVarBindList.insertElementAt(new SnmpVarBind(snmpTrapOidOid, paramSnmpOid), 0);
/* 1873 */     localSnmpVarBindList.insertElementAt(new SnmpVarBind(sysUpTimeOid, localSnmpTimeticks), 0);
/*      */ 
/* 1875 */     localSnmpPduRequest.varBindList = new SnmpVarBind[localSnmpVarBindList.size()];
/* 1876 */     localSnmpVarBindList.copyInto(localSnmpPduRequest.varBindList);
/*      */ 
/* 1880 */     if (paramInetAddress != null)
/* 1881 */       sendTrapPdu(paramInetAddress, localSnmpPduRequest);
/*      */     else
/* 1883 */       sendTrapPdu(localSnmpPduRequest);
/*      */   }
/*      */ 
/*      */   public void snmpV2Trap(InetAddress paramInetAddress, String paramString, SnmpOid paramSnmpOid, SnmpVarBindList paramSnmpVarBindList, SnmpTimeticks paramSnmpTimeticks)
/*      */     throws IOException, SnmpStatusException
/*      */   {
/* 1923 */     snmpV2Trap(paramInetAddress, this.trapPort, paramString, paramSnmpOid, paramSnmpVarBindList, paramSnmpTimeticks);
/*      */   }
/*      */ 
/*      */   private void snmpV2Trap(InetAddress paramInetAddress, int paramInt, String paramString, SnmpOid paramSnmpOid, SnmpVarBindList paramSnmpVarBindList, SnmpTimeticks paramSnmpTimeticks)
/*      */     throws IOException, SnmpStatusException
/*      */   {
/* 1939 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 1940 */       localObject = new StringBuilder().append("trapOid=").append(paramSnmpOid).append("\ncommunity=").append(paramString).append("\naddr=").append(paramInetAddress).append("\nvarBindList=").append(paramSnmpVarBindList).append("\ntime=").append(paramSnmpTimeticks).append("\ntrapPort=").append(paramInt);
/*      */ 
/* 1947 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "snmpV2Trap", ((StringBuilder)localObject).toString());
/*      */     }
/*      */ 
/* 1954 */     Object localObject = new SnmpPduRequest();
/* 1955 */     ((SnmpPduRequest)localObject).address = null;
/* 1956 */     ((SnmpPduRequest)localObject).port = paramInt;
/* 1957 */     ((SnmpPduRequest)localObject).type = 167;
/* 1958 */     ((SnmpPduRequest)localObject).version = 1;
/*      */ 
/* 1960 */     if (paramString != null)
/* 1961 */       ((SnmpPduRequest)localObject).community = paramString.getBytes();
/*      */     else
/* 1963 */       ((SnmpPduRequest)localObject).community = null;
/*      */     SnmpVarBindList localSnmpVarBindList;
/* 1966 */     if (paramSnmpVarBindList != null)
/* 1967 */       localSnmpVarBindList = (SnmpVarBindList)paramSnmpVarBindList.clone();
/*      */     else {
/* 1969 */       localSnmpVarBindList = new SnmpVarBindList(2);
/*      */     }
/*      */ 
/* 1972 */     SnmpTimeticks localSnmpTimeticks = null;
/* 1973 */     if (paramSnmpTimeticks != null)
/* 1974 */       localSnmpTimeticks = paramSnmpTimeticks;
/*      */     else {
/* 1976 */       localSnmpTimeticks = new SnmpTimeticks(getSysUpTime());
/*      */     }
/*      */ 
/* 1979 */     localSnmpVarBindList.insertElementAt(new SnmpVarBind(snmpTrapOidOid, paramSnmpOid), 0);
/* 1980 */     localSnmpVarBindList.insertElementAt(new SnmpVarBind(sysUpTimeOid, localSnmpTimeticks), 0);
/*      */ 
/* 1982 */     ((SnmpPduRequest)localObject).varBindList = new SnmpVarBind[localSnmpVarBindList.size()];
/* 1983 */     localSnmpVarBindList.copyInto(((SnmpPduRequest)localObject).varBindList);
/*      */ 
/* 1988 */     if (paramInetAddress != null)
/* 1989 */       sendTrapPdu(paramInetAddress, (SnmpPduPacket)localObject);
/*      */     else
/* 1991 */       sendTrapPdu((SnmpPduPacket)localObject);
/*      */   }
/*      */ 
/*      */   public void snmpPduTrap(InetAddress paramInetAddress, SnmpPduPacket paramSnmpPduPacket)
/*      */     throws IOException, SnmpStatusException
/*      */   {
/* 2008 */     if (paramInetAddress != null)
/* 2009 */       sendTrapPdu(paramInetAddress, paramSnmpPduPacket);
/*      */     else
/* 2011 */       sendTrapPdu(paramSnmpPduPacket);
/*      */   }
/*      */ 
/*      */   public void snmpPduTrap(SnmpPeer paramSnmpPeer, SnmpPduPacket paramSnmpPduPacket)
/*      */     throws IOException, SnmpStatusException
/*      */   {
/* 2027 */     if (paramSnmpPeer != null) {
/* 2028 */       paramSnmpPduPacket.port = paramSnmpPeer.getDestPort();
/* 2029 */       sendTrapPdu(paramSnmpPeer.getDestAddr(), paramSnmpPduPacket);
/*      */     }
/*      */     else {
/* 2032 */       paramSnmpPduPacket.port = getTrapPort().intValue();
/* 2033 */       sendTrapPdu(paramSnmpPduPacket);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void sendTrapPdu(SnmpPduPacket paramSnmpPduPacket)
/*      */     throws SnmpStatusException, IOException
/*      */   {
/* 2045 */     SnmpMessage localSnmpMessage = null;
/*      */     try {
/* 2047 */       localSnmpMessage = (SnmpMessage)this.pduFactory.encodeSnmpPdu(paramSnmpPduPacket, this.bufferSize);
/* 2048 */       if (localSnmpMessage == null) {
/* 2049 */         throw new SnmpStatusException(16);
/*      */       }
/*      */     }
/*      */     catch (SnmpTooBigException localSnmpTooBigException1)
/*      */     {
/* 2054 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 2055 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "sendTrapPdu", "Trap pdu is too big. Trap hasn't been sent to anyone");
/*      */       }
/*      */ 
/* 2059 */       throw new SnmpStatusException(1);
/*      */     }
/*      */ 
/* 2066 */     int i = 0;
/* 2067 */     openTrapSocketIfNeeded();
/* 2068 */     if (this.ipacl != null) {
/* 2069 */       Enumeration localEnumeration1 = ((InetAddressAcl)this.ipacl).getTrapDestinations();
/* 2070 */       while (localEnumeration1.hasMoreElements()) {
/* 2071 */         localSnmpMessage.address = ((InetAddress)localEnumeration1.nextElement());
/* 2072 */         Enumeration localEnumeration2 = ((InetAddressAcl)this.ipacl).getTrapCommunities(localSnmpMessage.address);
/*      */ 
/* 2074 */         while (localEnumeration2.hasMoreElements()) {
/* 2075 */           localSnmpMessage.community = ((String)localEnumeration2.nextElement()).getBytes();
/*      */           try {
/* 2077 */             sendTrapMessage(localSnmpMessage);
/* 2078 */             i++;
/*      */           }
/*      */           catch (SnmpTooBigException localSnmpTooBigException3) {
/* 2081 */             if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 2082 */               JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "sendTrapPdu", "Trap pdu is too big. Trap hasn't been sent to " + localSnmpMessage.address);
/*      */             }
/*      */ 
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2095 */     if (i == 0) {
/*      */       try {
/* 2097 */         localSnmpMessage.address = InetAddress.getLocalHost();
/* 2098 */         sendTrapMessage(localSnmpMessage);
/*      */       } catch (SnmpTooBigException localSnmpTooBigException2) {
/* 2100 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 2101 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "sendTrapPdu", "Trap pdu is too big. Trap hasn't been sent.");
/*      */         }
/*      */       }
/*      */       catch (UnknownHostException localUnknownHostException)
/*      */       {
/* 2106 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 2107 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "sendTrapPdu", "Trap pdu is too big. Trap hasn't been sent.");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2114 */     closeTrapSocketIfNeeded();
/*      */   }
/*      */ 
/*      */   private void sendTrapPdu(InetAddress paramInetAddress, SnmpPduPacket paramSnmpPduPacket)
/*      */     throws SnmpStatusException, IOException
/*      */   {
/* 2125 */     SnmpMessage localSnmpMessage = null;
/*      */     try {
/* 2127 */       localSnmpMessage = (SnmpMessage)this.pduFactory.encodeSnmpPdu(paramSnmpPduPacket, this.bufferSize);
/* 2128 */       if (localSnmpMessage == null)
/* 2129 */         throw new SnmpStatusException(16);
/*      */     }
/*      */     catch (SnmpTooBigException localSnmpTooBigException1)
/*      */     {
/* 2133 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 2134 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "sendTrapPdu", "Trap pdu is too big. Trap hasn't been sent to the specified host.");
/*      */       }
/*      */ 
/* 2138 */       throw new SnmpStatusException(1);
/*      */     }
/*      */ 
/* 2145 */     openTrapSocketIfNeeded();
/* 2146 */     if (paramInetAddress != null) {
/* 2147 */       localSnmpMessage.address = paramInetAddress;
/*      */       try {
/* 2149 */         sendTrapMessage(localSnmpMessage);
/*      */       } catch (SnmpTooBigException localSnmpTooBigException2) {
/* 2151 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 2152 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, this.dbgTag, "sendTrapPdu", "Trap pdu is too big. Trap hasn't been sent to " + localSnmpMessage.address);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2159 */     closeTrapSocketIfNeeded();
/*      */   }
/*      */ 
/*      */   private void sendTrapMessage(SnmpMessage paramSnmpMessage)
/*      */     throws IOException, SnmpTooBigException
/*      */   {
/* 2167 */     byte[] arrayOfByte = new byte[this.bufferSize];
/* 2168 */     DatagramPacket localDatagramPacket = new DatagramPacket(arrayOfByte, arrayOfByte.length);
/* 2169 */     int i = paramSnmpMessage.encodeMessage(arrayOfByte);
/* 2170 */     localDatagramPacket.setLength(i);
/* 2171 */     localDatagramPacket.setAddress(paramSnmpMessage.address);
/* 2172 */     localDatagramPacket.setPort(paramSnmpMessage.port);
/* 2173 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 2174 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "sendTrapMessage", "sending trap to " + paramSnmpMessage.address + ":" + paramSnmpMessage.port);
/*      */     }
/*      */ 
/* 2178 */     this.trapSocket.send(localDatagramPacket);
/* 2179 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 2180 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "sendTrapMessage", "sent to " + paramSnmpMessage.address + ":" + paramSnmpMessage.port);
/*      */     }
/*      */ 
/* 2184 */     this.snmpOutTraps += 1;
/* 2185 */     this.snmpOutPkts += 1;
/*      */   }
/*      */ 
/*      */   synchronized void openTrapSocketIfNeeded()
/*      */     throws SocketException
/*      */   {
/* 2192 */     if (this.trapSocket == null) {
/* 2193 */       this.trapSocket = new DatagramSocket(0, this.address);
/* 2194 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER))
/* 2195 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "openTrapSocketIfNeeded", "using port " + this.trapSocket.getLocalPort() + " to send traps");
/*      */     }
/*      */   }
/*      */ 
/*      */   synchronized void closeTrapSocketIfNeeded()
/*      */   {
/* 2206 */     if ((this.trapSocket != null) && (this.state != 0)) {
/* 2207 */       this.trapSocket.close();
/* 2208 */       this.trapSocket = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Vector snmpInformRequest(SnmpInformHandler paramSnmpInformHandler, SnmpOid paramSnmpOid, SnmpVarBindList paramSnmpVarBindList)
/*      */     throws IllegalStateException, IOException, SnmpStatusException
/*      */   {
/* 2252 */     if (!isActive()) {
/* 2253 */       throw new IllegalStateException("Start SNMP adaptor server before carrying out this operation");
/*      */     }
/*      */ 
/* 2256 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER))
/* 2257 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "snmpInformRequest", "trapOid=" + paramSnmpOid);
/*      */     SnmpVarBindList localSnmpVarBindList;
/* 2265 */     if (paramSnmpVarBindList != null)
/* 2266 */       localSnmpVarBindList = (SnmpVarBindList)paramSnmpVarBindList.clone();
/*      */     else
/* 2268 */       localSnmpVarBindList = new SnmpVarBindList(2);
/* 2269 */     SnmpTimeticks localSnmpTimeticks = new SnmpTimeticks(getSysUpTime());
/* 2270 */     localSnmpVarBindList.insertElementAt(new SnmpVarBind(snmpTrapOidOid, paramSnmpOid), 0);
/* 2271 */     localSnmpVarBindList.insertElementAt(new SnmpVarBind(sysUpTimeOid, localSnmpTimeticks), 0);
/*      */ 
/* 2276 */     openInformSocketIfNeeded();
/*      */ 
/* 2280 */     Vector localVector = new Vector();
/* 2281 */     InetAddress localInetAddress = null;
/* 2282 */     String str = null;
/* 2283 */     if (this.ipacl != null) {
/* 2284 */       Enumeration localEnumeration1 = ((InetAddressAcl)this.ipacl).getInformDestinations();
/* 2285 */       while (localEnumeration1.hasMoreElements()) {
/* 2286 */         localInetAddress = (InetAddress)localEnumeration1.nextElement();
/* 2287 */         Enumeration localEnumeration2 = ((InetAddressAcl)this.ipacl).getInformCommunities(localInetAddress);
/*      */ 
/* 2289 */         while (localEnumeration2.hasMoreElements()) {
/* 2290 */           str = (String)localEnumeration2.nextElement();
/* 2291 */           localVector.addElement(this.informSession.makeAsyncRequest(localInetAddress, str, paramSnmpInformHandler, localSnmpVarBindList, getInformPort()));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2298 */     return localVector;
/*      */   }
/*      */ 
/*      */   public SnmpInformRequest snmpInformRequest(InetAddress paramInetAddress, String paramString, SnmpInformHandler paramSnmpInformHandler, SnmpOid paramSnmpOid, SnmpVarBindList paramSnmpVarBindList)
/*      */     throws IllegalStateException, IOException, SnmpStatusException
/*      */   {
/* 2340 */     return snmpInformRequest(paramInetAddress, getInformPort(), paramString, paramSnmpInformHandler, paramSnmpOid, paramSnmpVarBindList);
/*      */   }
/*      */ 
/*      */   public SnmpInformRequest snmpInformRequest(SnmpPeer paramSnmpPeer, SnmpInformHandler paramSnmpInformHandler, SnmpOid paramSnmpOid, SnmpVarBindList paramSnmpVarBindList)
/*      */     throws IllegalStateException, IOException, SnmpStatusException
/*      */   {
/* 2388 */     SnmpParameters localSnmpParameters = (SnmpParameters)paramSnmpPeer.getParams();
/* 2389 */     return snmpInformRequest(paramSnmpPeer.getDestAddr(), paramSnmpPeer.getDestPort(), localSnmpParameters.getInformCommunity(), paramSnmpInformHandler, paramSnmpOid, paramSnmpVarBindList);
/*      */   }
/*      */ 
/*      */   public static final int mapErrorStatus(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/* 2407 */     return SnmpSubRequestHandler.mapErrorStatus(paramInt1, paramInt2, paramInt3);
/*      */   }
/*      */ 
/*      */   private SnmpInformRequest snmpInformRequest(InetAddress paramInetAddress, int paramInt, String paramString, SnmpInformHandler paramSnmpInformHandler, SnmpOid paramSnmpOid, SnmpVarBindList paramSnmpVarBindList)
/*      */     throws IllegalStateException, IOException, SnmpStatusException
/*      */   {
/* 2419 */     if (!isActive()) {
/* 2420 */       throw new IllegalStateException("Start SNMP adaptor server before carrying out this operation");
/*      */     }
/*      */ 
/* 2423 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER))
/* 2424 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "snmpInformRequest", "trapOid=" + paramSnmpOid);
/*      */     SnmpVarBindList localSnmpVarBindList;
/* 2432 */     if (paramSnmpVarBindList != null)
/* 2433 */       localSnmpVarBindList = (SnmpVarBindList)paramSnmpVarBindList.clone();
/*      */     else
/* 2435 */       localSnmpVarBindList = new SnmpVarBindList(2);
/* 2436 */     SnmpTimeticks localSnmpTimeticks = new SnmpTimeticks(getSysUpTime());
/* 2437 */     localSnmpVarBindList.insertElementAt(new SnmpVarBind(snmpTrapOidOid, paramSnmpOid), 0);
/* 2438 */     localSnmpVarBindList.insertElementAt(new SnmpVarBind(sysUpTimeOid, localSnmpTimeticks), 0);
/*      */ 
/* 2443 */     openInformSocketIfNeeded();
/* 2444 */     return this.informSession.makeAsyncRequest(paramInetAddress, paramString, paramSnmpInformHandler, localSnmpVarBindList, paramInt);
/*      */   }
/*      */ 
/*      */   synchronized void openInformSocketIfNeeded()
/*      */     throws SocketException
/*      */   {
/* 2452 */     if (this.informSession == null) {
/* 2453 */       this.informSession = new SnmpSession(this);
/* 2454 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER))
/* 2455 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "openInformSocketIfNeeded", "to send inform requests and receive inform responses");
/*      */     }
/*      */   }
/*      */ 
/*      */   synchronized void closeInformSocketIfNeeded()
/*      */   {
/* 2466 */     if ((this.informSession != null) && (this.state != 0)) {
/* 2467 */       this.informSession.destroySession();
/* 2468 */       this.informSession = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   InetAddress getAddress()
/*      */   {
/* 2478 */     return this.address;
/*      */   }
/*      */ 
/*      */   protected void finalize()
/*      */   {
/*      */     try
/*      */     {
/* 2494 */       if (this.socket != null) {
/* 2495 */         this.socket.close();
/* 2496 */         this.socket = null;
/*      */       }
/*      */ 
/* 2499 */       this.threadService.terminate();
/*      */     } catch (Exception localException) {
/* 2501 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER))
/* 2502 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, this.dbgTag, "finalize", "Exception in finalizer", localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   String makeDebugTag()
/*      */   {
/* 2515 */     return "SnmpAdaptorServer[" + getProtocol() + ":" + getPort() + "]";
/*      */   }
/*      */ 
/*      */   void updateRequestCounters(int paramInt) {
/* 2519 */     switch (paramInt)
/*      */     {
/*      */     case 160:
/* 2522 */       this.snmpInGetRequests += 1;
/* 2523 */       break;
/*      */     case 161:
/* 2525 */       this.snmpInGetNexts += 1;
/* 2526 */       break;
/*      */     case 163:
/* 2528 */       this.snmpInSetRequests += 1;
/* 2529 */       break;
/*      */     case 162:
/*      */     }
/*      */ 
/* 2533 */     this.snmpInPkts += 1;
/*      */   }
/*      */ 
/*      */   void updateErrorCounters(int paramInt) {
/* 2537 */     switch (paramInt)
/*      */     {
/*      */     case 0:
/* 2540 */       this.snmpOutGetResponses += 1;
/* 2541 */       break;
/*      */     case 5:
/* 2543 */       this.snmpOutGenErrs += 1;
/* 2544 */       break;
/*      */     case 3:
/* 2546 */       this.snmpOutBadValues += 1;
/* 2547 */       break;
/*      */     case 2:
/* 2549 */       this.snmpOutNoSuchNames += 1;
/* 2550 */       break;
/*      */     case 1:
/* 2552 */       this.snmpOutTooBigs += 1;
/* 2553 */       break;
/*      */     case 4:
/*      */     }
/*      */ 
/* 2557 */     this.snmpOutPkts += 1;
/*      */   }
/*      */ 
/*      */   void updateVarCounters(int paramInt1, int paramInt2) {
/* 2561 */     switch (paramInt1)
/*      */     {
/*      */     case 160:
/*      */     case 161:
/*      */     case 165:
/* 2566 */       this.snmpInTotalReqVars += paramInt2;
/* 2567 */       break;
/*      */     case 163:
/* 2569 */       this.snmpInTotalSetVars += paramInt2;
/*      */     case 162:
/*      */     case 164:
/*      */     }
/*      */   }
/*      */ 
/* 2575 */   void incSnmpInASNParseErrs(int paramInt) { this.snmpInASNParseErrs += paramInt; }
/*      */ 
/*      */   void incSnmpInBadVersions(int paramInt)
/*      */   {
/* 2579 */     this.snmpInBadVersions += paramInt;
/*      */   }
/*      */ 
/*      */   void incSnmpInBadCommunityUses(int paramInt) {
/* 2583 */     this.snmpInBadCommunityUses += paramInt;
/*      */   }
/*      */ 
/*      */   void incSnmpInBadCommunityNames(int paramInt) {
/* 2587 */     this.snmpInBadCommunityNames += paramInt;
/*      */   }
/*      */ 
/*      */   void incSnmpSilentDrops(int paramInt) {
/* 2591 */     this.snmpSilentDrops += paramInt;
/*      */   }
/*      */ 
/*      */   long getSysUpTime()
/*      */   {
/* 2601 */     return (System.currentTimeMillis() - this.startUpTime) / 10L;
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 2612 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 2618 */     this.mibs = new Vector();
/*      */   }
/*      */ 
/*      */   private void init(Object paramObject, int paramInt, InetAddress paramInetAddress)
/*      */   {
/* 2626 */     this.root = new SnmpMibTree();
/*      */ 
/* 2629 */     this.root.setDefaultAgent(new SnmpErrorHandlerAgent());
/*      */ 
/* 2633 */     this.startUpTime = System.currentTimeMillis();
/* 2634 */     this.maxActiveClientCount = 10;
/*      */ 
/* 2637 */     this.pduFactory = new SnmpPduFactoryBER();
/*      */ 
/* 2639 */     this.port = paramInt;
/* 2640 */     this.ipacl = paramObject;
/* 2641 */     this.address = paramInetAddress;
/*      */ 
/* 2643 */     if ((this.ipacl == null) && (this.useAcl == true)) {
/* 2644 */       throw new IllegalArgumentException("ACL object cannot be null");
/*      */     }
/* 2646 */     this.threadService = new ThreadService(threadNumber);
/*      */   }
/*      */ 
/*      */   SnmpMibAgent getAgentMib(SnmpOid paramSnmpOid) {
/* 2650 */     return this.root.getAgentMib(paramSnmpOid);
/*      */   }
/*      */ 
/*      */   protected Thread createMainThread() {
/* 2654 */     Thread localThread = super.createMainThread();
/* 2655 */     localThread.setDaemon(true);
/* 2656 */     return localThread;
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  329 */     String str = System.getProperty("com.sun.jmx.snmp.threadnumber");
/*      */ 
/*  331 */     if (str != null)
/*      */       try {
/*  333 */         threadNumber = Integer.parseInt(System.getProperty(str));
/*      */       } catch (Exception localException) {
/*  335 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpAdaptorServer.class.getName(), "<static init>", "Got wrong value for com.sun.jmx.snmp.threadnumber: " + str + ". Use the default value: " + threadNumber);
/*      */       }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.daemon.SnmpAdaptorServer
 * JD-Core Version:    0.6.2
 */