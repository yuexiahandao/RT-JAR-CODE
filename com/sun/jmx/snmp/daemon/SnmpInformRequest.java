/*      */ package com.sun.jmx.snmp.daemon;
/*      */ 
/*      */ import com.sun.jmx.defaults.JmxProperties;
/*      */ import com.sun.jmx.snmp.SnmpDefinitions;
/*      */ import com.sun.jmx.snmp.SnmpMessage;
/*      */ import com.sun.jmx.snmp.SnmpPdu;
/*      */ import com.sun.jmx.snmp.SnmpPduFactory;
/*      */ import com.sun.jmx.snmp.SnmpPduPacket;
/*      */ import com.sun.jmx.snmp.SnmpPduRequest;
/*      */ import com.sun.jmx.snmp.SnmpPduRequestType;
/*      */ import com.sun.jmx.snmp.SnmpStatusException;
/*      */ import com.sun.jmx.snmp.SnmpTooBigException;
/*      */ import com.sun.jmx.snmp.SnmpVarBind;
/*      */ import com.sun.jmx.snmp.SnmpVarBindList;
/*      */ import java.io.IOException;
/*      */ import java.net.InetAddress;
/*      */ import java.util.Date;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ 
/*      */ public class SnmpInformRequest
/*      */   implements SnmpDefinitions
/*      */ {
/*   83 */   private static SnmpRequestCounter requestCounter = new SnmpRequestCounter();
/*      */ 
/*   88 */   private SnmpVarBindList varBindList = null;
/*      */ 
/*   93 */   int errorStatus = 0;
/*      */ 
/*   98 */   int errorIndex = 0;
/*      */ 
/*  101 */   SnmpVarBind[] internalVarBind = null;
/*      */ 
/*  104 */   String reason = null;
/*      */   private transient SnmpAdaptorServer adaptor;
/*      */   private transient SnmpSession informSession;
/*  119 */   private SnmpInformHandler callback = null;
/*      */   SnmpPdu requestPdu;
/*      */   SnmpPduRequestType responsePdu;
/*      */   private static final int stBase = 1;
/*      */   public static final int stInProgress = 1;
/*      */   public static final int stWaitingToSend = 3;
/*      */   public static final int stWaitingForReply = 5;
/*      */   public static final int stReceivedReply = 9;
/*      */   public static final int stAborted = 16;
/*      */   public static final int stTimeout = 32;
/*      */   public static final int stInternalError = 64;
/*      */   public static final int stResultsAvailable = 128;
/*      */   public static final int stNeverUsed = 256;
/*  186 */   private int numTries = 0;
/*      */ 
/*  192 */   private int timeout = 3000;
/*      */ 
/*  196 */   private int reqState = 256;
/*      */ 
/*  199 */   private long prevPollTime = 0L;
/*  200 */   private long nextPollTime = 0L;
/*      */   private long waitTimeForResponse;
/*  202 */   private Date debugDate = new Date();
/*      */ 
/*  207 */   private int requestId = 0;
/*      */ 
/*  209 */   private int port = 0;
/*      */ 
/*  211 */   private InetAddress address = null;
/*  212 */   private String communityString = null;
/*      */ 
/*      */   SnmpInformRequest(SnmpSession paramSnmpSession, SnmpAdaptorServer paramSnmpAdaptorServer, InetAddress paramInetAddress, String paramString, int paramInt, SnmpInformHandler paramSnmpInformHandler)
/*      */     throws SnmpStatusException
/*      */   {
/*  235 */     this.informSession = paramSnmpSession;
/*  236 */     this.adaptor = paramSnmpAdaptorServer;
/*  237 */     this.address = paramInetAddress;
/*  238 */     this.communityString = paramString;
/*  239 */     this.port = paramInt;
/*  240 */     this.callback = paramSnmpInformHandler;
/*  241 */     this.informSession.addInformRequest(this);
/*  242 */     setTimeout(this.adaptor.getTimeout());
/*      */   }
/*      */ 
/*      */   public final synchronized int getRequestId()
/*      */   {
/*  253 */     return this.requestId;
/*      */   }
/*      */ 
/*      */   synchronized InetAddress getAddress()
/*      */   {
/*  261 */     return this.address;
/*      */   }
/*      */ 
/*      */   public final synchronized int getRequestStatus()
/*      */   {
/*  269 */     return this.reqState;
/*      */   }
/*      */ 
/*      */   public final synchronized boolean isAborted()
/*      */   {
/*  277 */     return (this.reqState & 0x10) == 16;
/*      */   }
/*      */ 
/*      */   public final synchronized boolean inProgress()
/*      */   {
/*  285 */     return (this.reqState & 0x1) == 1;
/*      */   }
/*      */ 
/*      */   public final synchronized boolean isResultAvailable()
/*      */   {
/*  293 */     return this.reqState == 128;
/*      */   }
/*      */ 
/*      */   public final synchronized int getErrorStatus()
/*      */   {
/*  301 */     return this.errorStatus;
/*      */   }
/*      */ 
/*      */   public final synchronized int getErrorIndex()
/*      */   {
/*  310 */     return this.errorIndex;
/*      */   }
/*      */ 
/*      */   public final int getMaxTries()
/*      */   {
/*  318 */     return this.adaptor.getMaxTries();
/*      */   }
/*      */ 
/*      */   public final synchronized int getNumTries()
/*      */   {
/*  326 */     return this.numTries;
/*      */   }
/*      */ 
/*      */   final synchronized void setTimeout(int paramInt)
/*      */   {
/*  333 */     this.timeout = paramInt;
/*      */   }
/*      */ 
/*      */   public final synchronized long getAbsNextPollTime()
/*      */   {
/*  342 */     return this.nextPollTime;
/*      */   }
/*      */ 
/*      */   public final synchronized long getAbsMaxTimeToWait()
/*      */   {
/*  351 */     if (this.prevPollTime == 0L) {
/*  352 */       return System.currentTimeMillis();
/*      */     }
/*  354 */     return this.waitTimeForResponse;
/*      */   }
/*      */ 
/*      */   public final synchronized SnmpVarBindList getResponseVarBindList()
/*      */   {
/*  368 */     if (inProgress())
/*  369 */       return null;
/*  370 */     return this.varBindList;
/*      */   }
/*      */ 
/*      */   public final boolean waitForCompletion(long paramLong)
/*      */   {
/*  387 */     if (!inProgress()) {
/*  388 */       return true;
/*      */     }
/*  390 */     if (this.informSession.thisSessionContext())
/*      */     {
/*  393 */       SnmpInformHandler localSnmpInformHandler1 = this.callback;
/*  394 */       this.callback = null;
/*  395 */       this.informSession.waitForResponse(this, paramLong);
/*  396 */       this.callback = localSnmpInformHandler1;
/*      */     }
/*      */     else
/*      */     {
/*  400 */       synchronized (this) {
/*  401 */         SnmpInformHandler localSnmpInformHandler2 = this.callback;
/*      */         try {
/*  403 */           this.callback = null;
/*  404 */           wait(paramLong);
/*      */         } catch (InterruptedException localInterruptedException) {
/*      */         }
/*  407 */         this.callback = localSnmpInformHandler2;
/*      */       }
/*      */     }
/*      */ 
/*  411 */     return !inProgress();
/*      */   }
/*      */ 
/*      */   public final void cancelRequest()
/*      */   {
/*  418 */     this.errorStatus = 225;
/*  419 */     stopRequest();
/*  420 */     deleteRequest();
/*  421 */     notifyClient();
/*      */   }
/*      */ 
/*      */   public final synchronized void notifyClient()
/*      */   {
/*  428 */     notifyAll();
/*      */   }
/*      */ 
/*      */   public void finalize()
/*      */   {
/*  438 */     this.callback = null;
/*  439 */     this.varBindList = null;
/*  440 */     this.internalVarBind = null;
/*  441 */     this.adaptor = null;
/*  442 */     this.informSession = null;
/*  443 */     this.requestPdu = null;
/*  444 */     this.responsePdu = null;
/*      */   }
/*      */ 
/*      */   public static String snmpErrorToString(int paramInt)
/*      */   {
/*  453 */     switch (paramInt) {
/*      */     case 0:
/*  455 */       return "noError";
/*      */     case 1:
/*  457 */       return "tooBig";
/*      */     case 2:
/*  459 */       return "noSuchName";
/*      */     case 3:
/*  461 */       return "badValue";
/*      */     case 4:
/*  463 */       return "readOnly";
/*      */     case 5:
/*  465 */       return "genErr";
/*      */     case 6:
/*  467 */       return "noAccess";
/*      */     case 7:
/*  469 */       return "wrongType";
/*      */     case 8:
/*  471 */       return "wrongLength";
/*      */     case 9:
/*  473 */       return "wrongEncoding";
/*      */     case 10:
/*  475 */       return "wrongValue";
/*      */     case 11:
/*  477 */       return "noCreation";
/*      */     case 12:
/*  479 */       return "inconsistentValue";
/*      */     case 13:
/*  481 */       return "resourceUnavailable";
/*      */     case 14:
/*  483 */       return "commitFailed";
/*      */     case 15:
/*  485 */       return "undoFailed";
/*      */     case 16:
/*  487 */       return "authorizationError";
/*      */     case 17:
/*  489 */       return "notWritable";
/*      */     case 18:
/*  491 */       return "inconsistentName";
/*      */     case 224:
/*  493 */       return "reqTimeout";
/*      */     case 225:
/*  495 */       return "reqAborted";
/*      */     case 226:
/*  497 */       return "rspDecodingError";
/*      */     case 227:
/*  499 */       return "reqEncodingError";
/*      */     case 228:
/*  501 */       return "reqPacketOverflow";
/*      */     case 229:
/*  503 */       return "rspEndOfTable";
/*      */     case 230:
/*  505 */       return "reqRefireAfterVbFix";
/*      */     case 231:
/*  507 */       return "reqHandleTooBig";
/*      */     case 232:
/*  509 */       return "reqTooBigImpossible";
/*      */     case 240:
/*  511 */       return "reqInternalError";
/*      */     case 241:
/*  513 */       return "reqSocketIOError";
/*      */     case 242:
/*  515 */       return "reqUnknownError";
/*      */     case 243:
/*  517 */       return "wrongSnmpVersion";
/*      */     case 244:
/*  519 */       return "snmpUnknownPrincipal";
/*      */     case 245:
/*  521 */       return "snmpAuthNotSupported";
/*      */     case 246:
/*  523 */       return "snmpPrivNotSupported";
/*      */     case 249:
/*  525 */       return "snmpBadSecurityLevel";
/*      */     case 247:
/*  527 */       return "snmpUsmBadEngineId";
/*      */     case 248:
/*  529 */       return "snmpUsmInvalidTimeliness";
/*      */     }
/*  531 */     return "Unknown Error = " + paramInt;
/*      */   }
/*      */ 
/*      */   synchronized void start(SnmpVarBindList paramSnmpVarBindList)
/*      */     throws SnmpStatusException
/*      */   {
/*  545 */     if (inProgress())
/*  546 */       throw new SnmpStatusException("Inform request already in progress.");
/*  547 */     setVarBindList(paramSnmpVarBindList);
/*  548 */     initializeAndFire();
/*      */   }
/*      */ 
/*      */   private synchronized void initializeAndFire() {
/*  552 */     this.requestPdu = null;
/*  553 */     this.responsePdu = null;
/*  554 */     this.reason = null;
/*  555 */     startRequest(System.currentTimeMillis());
/*  556 */     setErrorStatusAndIndex(0, 0);
/*      */   }
/*      */ 
/*      */   private synchronized void startRequest(long paramLong)
/*      */   {
/*  566 */     this.nextPollTime = paramLong;
/*  567 */     this.prevPollTime = 0L;
/*  568 */     schedulePoll();
/*      */   }
/*      */ 
/*      */   private void schedulePoll()
/*      */   {
/*  575 */     this.numTries = 0;
/*  576 */     initNewRequest();
/*  577 */     setRequestStatus(3);
/*  578 */     this.informSession.getSnmpQManager().addRequest(this);
/*      */   }
/*      */ 
/*      */   void action()
/*      */   {
/*  587 */     if (!inProgress())
/*  588 */       return;
/*      */     while (true)
/*      */       try {
/*  591 */         if (this.numTries == 0)
/*  592 */           invokeOnReady();
/*  593 */         else if (this.numTries < getMaxTries())
/*  594 */           invokeOnRetry();
/*      */         else {
/*  596 */           invokeOnTimeout();
/*      */         }
/*  598 */         return;
/*      */       }
/*      */       catch (OutOfMemoryError localOutOfMemoryError)
/*      */       {
/*  602 */         this.numTries += 1;
/*  603 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  604 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpInformRequest.class.getName(), "action", "Inform request hit out of memory situation...");
/*      */         }
/*      */ 
/*  607 */         Thread.currentThread(); Thread.yield();
/*      */       }
/*      */   }
/*      */ 
/*      */   private final void invokeOnReady()
/*      */   {
/*  613 */     if (this.requestPdu == null) {
/*  614 */       this.requestPdu = constructPduPacket();
/*      */     }
/*  616 */     if ((this.requestPdu != null) && 
/*  617 */       (!sendPdu()))
/*  618 */       queueResponse();
/*      */   }
/*      */ 
/*      */   private final void invokeOnRetry()
/*      */   {
/*  623 */     invokeOnReady();
/*      */   }
/*      */ 
/*      */   private final void invokeOnTimeout() {
/*  627 */     this.errorStatus = 224;
/*  628 */     queueResponse();
/*      */   }
/*      */ 
/*      */   private final void queueResponse() {
/*  632 */     this.informSession.addResponse(this);
/*      */   }
/*      */ 
/*      */   synchronized SnmpPdu constructPduPacket()
/*      */   {
/*  639 */     SnmpPduRequest localSnmpPduRequest = null;
/*  640 */     Object localObject = null;
/*      */     try {
/*  642 */       localSnmpPduRequest = new SnmpPduRequest();
/*  643 */       localSnmpPduRequest.port = this.port;
/*  644 */       localSnmpPduRequest.type = 166;
/*  645 */       localSnmpPduRequest.version = 1;
/*  646 */       localSnmpPduRequest.community = this.communityString.getBytes("8859_1");
/*  647 */       localSnmpPduRequest.requestId = getRequestId();
/*  648 */       localSnmpPduRequest.varBindList = this.internalVarBind;
/*      */ 
/*  650 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  651 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpInformRequest.class.getName(), "constructPduPacket", "Packet built");
/*      */       }
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*  656 */       localObject = localException;
/*  657 */       this.errorStatus = 242;
/*  658 */       this.reason = localException.getMessage();
/*      */     }
/*  660 */     if (localObject != null) {
/*  661 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  662 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpInformRequest.class.getName(), "constructPduPacket", "Got unexpected exception", localObject);
/*      */       }
/*      */ 
/*  665 */       localSnmpPduRequest = null;
/*  666 */       queueResponse();
/*      */     }
/*  668 */     return localSnmpPduRequest;
/*      */   }
/*      */ 
/*      */   boolean sendPdu() {
/*      */     try {
/*  673 */       this.responsePdu = null;
/*      */ 
/*  675 */       SnmpPduFactory localSnmpPduFactory = this.adaptor.getPduFactory();
/*  676 */       SnmpMessage localSnmpMessage = (SnmpMessage)localSnmpPduFactory.encodeSnmpPdu((SnmpPduPacket)this.requestPdu, this.adaptor.getBufferSize().intValue());
/*      */ 
/*  678 */       if (localSnmpMessage == null) {
/*  679 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  680 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpInformRequest.class.getName(), "sendPdu", "pdu factory returned a null value");
/*      */         }
/*      */ 
/*  683 */         throw new SnmpStatusException(242);
/*      */       }
/*      */ 
/*  688 */       int i = this.adaptor.getBufferSize().intValue();
/*  689 */       byte[] arrayOfByte = new byte[i];
/*  690 */       int j = localSnmpMessage.encodeMessage(arrayOfByte);
/*      */ 
/*  692 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  693 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpInformRequest.class.getName(), "sendPdu", "Dump : \n" + localSnmpMessage.printMessage());
/*      */       }
/*      */ 
/*  697 */       sendPduPacket(arrayOfByte, j);
/*  698 */       return true;
/*      */     }
/*      */     catch (SnmpTooBigException localSnmpTooBigException) {
/*  701 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  702 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpInformRequest.class.getName(), "sendPdu", "Got unexpected exception", localSnmpTooBigException);
/*      */       }
/*      */ 
/*  706 */       setErrorStatusAndIndex(228, localSnmpTooBigException.getVarBindCount());
/*  707 */       this.requestPdu = null;
/*  708 */       this.reason = localSnmpTooBigException.getMessage();
/*  709 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))
/*  710 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpInformRequest.class.getName(), "sendPdu", "Packet Overflow while building inform request");
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*  714 */       setErrorStatusAndIndex(241, 0);
/*  715 */       this.reason = localIOException.getMessage();
/*      */     } catch (Exception localException) {
/*  717 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  718 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpInformRequest.class.getName(), "sendPdu", "Got unexpected exception", localException);
/*      */       }
/*      */ 
/*  721 */       setErrorStatusAndIndex(242, 0);
/*  722 */       this.reason = localException.getMessage();
/*      */     }
/*  724 */     return false;
/*      */   }
/*      */ 
/*      */   final void sendPduPacket(byte[] paramArrayOfByte, int paramInt)
/*      */     throws IOException
/*      */   {
/*  736 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  737 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpInformRequest.class.getName(), "sendPduPacket", "Send to peer. Peer/Port : " + this.address.getHostName() + "/" + this.port + ". Length = " + paramInt + "\nDump : \n" + SnmpMessage.dumpHexBuffer(paramArrayOfByte, 0, paramInt));
/*      */     }
/*      */ 
/*  742 */     SnmpSocket localSnmpSocket = this.informSession.getSocket();
/*  743 */     synchronized (localSnmpSocket) {
/*  744 */       localSnmpSocket.sendPacket(paramArrayOfByte, paramInt, this.address, this.port);
/*  745 */       setRequestSentTime(System.currentTimeMillis());
/*      */     }
/*      */   }
/*      */ 
/*      */   final void processResponse()
/*      */   {
/*  754 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  755 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpInformRequest.class.getName(), "processResponse", "errstatus = " + this.errorStatus);
/*      */     }
/*      */ 
/*  759 */     if (!inProgress()) {
/*  760 */       this.responsePdu = null;
/*  761 */       return;
/*      */     }
/*      */ 
/*  764 */     if (this.errorStatus >= 240) {
/*  765 */       handleInternalError("Internal Error...");
/*  766 */       return;
/*      */     }
/*      */     try
/*      */     {
/*  770 */       parsePduPacket(this.responsePdu);
/*      */ 
/*  774 */       switch (this.errorStatus) {
/*      */       case 0:
/*  776 */         handleSuccess();
/*  777 */         return;
/*      */       case 224:
/*  779 */         handleTimeout();
/*  780 */         return;
/*      */       case 240:
/*  782 */         handleInternalError("Unknown internal error.  deal with it later!");
/*  783 */         return;
/*      */       case 231:
/*  785 */         setErrorStatusAndIndex(1, 0);
/*  786 */         handleError("Cannot handle too-big situation...");
/*  787 */         return;
/*      */       case 230:
/*  790 */         initializeAndFire();
/*  791 */         return;
/*      */       }
/*  793 */       handleError("Error status set in packet...!!");
/*  794 */       return;
/*      */     }
/*      */     catch (Exception localException) {
/*  797 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  798 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpInformRequest.class.getName(), "processResponse", "Got unexpected exception", localException);
/*      */       }
/*      */ 
/*  801 */       this.reason = localException.getMessage();
/*      */ 
/*  803 */       handleInternalError(this.reason);
/*      */     }
/*      */   }
/*      */ 
/*      */   synchronized void parsePduPacket(SnmpPduRequestType paramSnmpPduRequestType)
/*      */   {
/*  812 */     if (paramSnmpPduRequestType == null) {
/*  813 */       return;
/*      */     }
/*  815 */     this.errorStatus = paramSnmpPduRequestType.getErrorStatus();
/*  816 */     this.errorIndex = paramSnmpPduRequestType.getErrorIndex();
/*      */ 
/*  818 */     if (this.errorStatus == 0) {
/*  819 */       updateInternalVarBindWithResult(((SnmpPdu)paramSnmpPduRequestType).varBindList);
/*  820 */       return;
/*      */     }
/*      */ 
/*  823 */     if (this.errorStatus != 0) {
/*  824 */       this.errorIndex -= 1;
/*      */     }
/*  826 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER))
/*  827 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpInformRequest.class.getName(), "parsePduPacket", "received inform response. ErrorStatus/ErrorIndex = " + this.errorStatus + "/" + this.errorIndex);
/*      */   }
/*      */ 
/*      */   private void handleSuccess()
/*      */   {
/*  838 */     setRequestStatus(128);
/*      */ 
/*  840 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  841 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpInformRequest.class.getName(), "handleSuccess", "Invoking user defined callback...");
/*      */     }
/*      */ 
/*  845 */     deleteRequest();
/*  846 */     notifyClient();
/*      */ 
/*  848 */     this.requestPdu = null;
/*      */ 
/*  850 */     this.internalVarBind = null;
/*      */     try
/*      */     {
/*  853 */       if (this.callback != null)
/*  854 */         this.callback.processSnmpPollData(this, this.errorStatus, this.errorIndex, getVarBindList());
/*      */     } catch (Exception localException) {
/*  856 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))
/*  857 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpInformRequest.class.getName(), "handleSuccess", "Exception generated by user callback", localException);
/*      */     }
/*      */     catch (OutOfMemoryError localOutOfMemoryError)
/*      */     {
/*  861 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  862 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpInformRequest.class.getName(), "handleSuccess", "OutOfMemory Error generated by user callback", localOutOfMemoryError);
/*      */       }
/*      */ 
/*  865 */       Thread.currentThread(); Thread.yield();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void handleTimeout()
/*      */   {
/*  875 */     setRequestStatus(32);
/*      */ 
/*  877 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  878 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpInformRequest.class.getName(), "handleTimeout", "Snmp error/index = " + snmpErrorToString(this.errorStatus) + "/" + this.errorIndex + ". Invoking timeout user defined callback...");
/*      */     }
/*      */ 
/*  882 */     deleteRequest();
/*  883 */     notifyClient();
/*      */ 
/*  885 */     this.requestPdu = null;
/*  886 */     this.responsePdu = null;
/*  887 */     this.internalVarBind = null;
/*      */     try
/*      */     {
/*  890 */       if (this.callback != null)
/*  891 */         this.callback.processSnmpPollTimeout(this);
/*      */     } catch (Exception localException) {
/*  893 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))
/*  894 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpInformRequest.class.getName(), "handleTimeout", "Exception generated by user callback", localException);
/*      */     }
/*      */     catch (OutOfMemoryError localOutOfMemoryError)
/*      */     {
/*  898 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  899 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpInformRequest.class.getName(), "handleTimeout", "OutOfMemory Error generated by user callback", localOutOfMemoryError);
/*      */       }
/*      */ 
/*  902 */       Thread.currentThread(); Thread.yield();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void handleError(String paramString)
/*      */   {
/*  912 */     setRequestStatus(128);
/*      */ 
/*  914 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  915 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpInformRequest.class.getName(), "handleError", "Snmp error/index = " + snmpErrorToString(this.errorStatus) + "/" + this.errorIndex + ". Invoking error user defined callback...\n" + getVarBindList());
/*      */     }
/*      */ 
/*  919 */     deleteRequest();
/*  920 */     notifyClient();
/*      */ 
/*  922 */     this.requestPdu = null;
/*  923 */     this.responsePdu = null;
/*  924 */     this.internalVarBind = null;
/*      */     try
/*      */     {
/*  927 */       if (this.callback != null)
/*  928 */         this.callback.processSnmpPollData(this, getErrorStatus(), getErrorIndex(), getVarBindList());
/*      */     } catch (Exception localException) {
/*  930 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))
/*  931 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpInformRequest.class.getName(), "handleError", "Exception generated by user callback", localException);
/*      */     }
/*      */     catch (OutOfMemoryError localOutOfMemoryError)
/*      */     {
/*  935 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  936 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpInformRequest.class.getName(), "handleError", "OutOfMemory Error generated by user callback", localOutOfMemoryError);
/*      */       }
/*      */ 
/*  939 */       Thread.currentThread(); Thread.yield();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void handleInternalError(String paramString)
/*      */   {
/*  948 */     setRequestStatus(64);
/*  949 */     if (this.reason == null) {
/*  950 */       this.reason = paramString;
/*      */     }
/*  952 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  953 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpInformRequest.class.getName(), "handleInternalError", "Snmp error/index = " + snmpErrorToString(this.errorStatus) + "/" + this.errorIndex + ". Invoking internal error user defined callback...\n" + getVarBindList());
/*      */     }
/*      */ 
/*  959 */     deleteRequest();
/*  960 */     notifyClient();
/*      */ 
/*  962 */     this.requestPdu = null;
/*  963 */     this.responsePdu = null;
/*  964 */     this.internalVarBind = null;
/*      */     try
/*      */     {
/*  967 */       if (this.callback != null)
/*  968 */         this.callback.processSnmpInternalError(this, this.reason);
/*      */     } catch (Exception localException) {
/*  970 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))
/*  971 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpInformRequest.class.getName(), "handleInternalError", "Exception generated by user callback", localException);
/*      */     }
/*      */     catch (OutOfMemoryError localOutOfMemoryError)
/*      */     {
/*  975 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/*  976 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpInformRequest.class.getName(), "handleInternalError", "OutOfMemory Error generated by user callback", localOutOfMemoryError);
/*      */       }
/*      */ 
/*  979 */       Thread.currentThread(); Thread.yield();
/*      */     }
/*      */   }
/*      */ 
/*      */   void updateInternalVarBindWithResult(SnmpVarBind[] paramArrayOfSnmpVarBind)
/*      */   {
/*  985 */     if ((paramArrayOfSnmpVarBind == null) || (paramArrayOfSnmpVarBind.length == 0)) {
/*  986 */       return;
/*      */     }
/*  988 */     int i = 0;
/*      */ 
/*  990 */     for (int j = 0; (j < this.internalVarBind.length) && (i < paramArrayOfSnmpVarBind.length); j++) {
/*  991 */       SnmpVarBind localSnmpVarBind1 = this.internalVarBind[j];
/*  992 */       if (localSnmpVarBind1 != null)
/*      */       {
/*  995 */         SnmpVarBind localSnmpVarBind2 = paramArrayOfSnmpVarBind[i];
/*  996 */         localSnmpVarBind1.setSnmpValue(localSnmpVarBind2.getSnmpValue());
/*  997 */         i++;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   final void invokeOnResponse(Object paramObject)
/*      */   {
/* 1005 */     if (paramObject != null) {
/* 1006 */       if ((paramObject instanceof SnmpPduRequestType))
/* 1007 */         this.responsePdu = ((SnmpPduRequestType)paramObject);
/*      */       else
/* 1009 */         return;
/*      */     }
/* 1011 */     setRequestStatus(9);
/* 1012 */     queueResponse();
/*      */   }
/*      */ 
/*      */   private void stopRequest()
/*      */   {
/* 1025 */     synchronized (this) {
/* 1026 */       setRequestStatus(16);
/*      */     }
/* 1028 */     this.informSession.getSnmpQManager().removeRequest(this);
/* 1029 */     synchronized (this) {
/* 1030 */       this.requestId = 0;
/*      */     }
/*      */   }
/*      */ 
/*      */   final synchronized void deleteRequest() {
/* 1035 */     this.informSession.removeInformRequest(this);
/*      */   }
/*      */ 
/*      */   final synchronized SnmpVarBindList getVarBindList()
/*      */   {
/* 1045 */     return this.varBindList;
/*      */   }
/*      */ 
/*      */   final synchronized void setVarBindList(SnmpVarBindList paramSnmpVarBindList)
/*      */   {
/* 1054 */     this.varBindList = paramSnmpVarBindList;
/* 1055 */     if ((this.internalVarBind == null) || (this.internalVarBind.length != this.varBindList.size())) {
/* 1056 */       this.internalVarBind = new SnmpVarBind[this.varBindList.size()];
/*      */     }
/* 1058 */     this.varBindList.copyInto(this.internalVarBind);
/*      */   }
/*      */ 
/*      */   final synchronized void setErrorStatusAndIndex(int paramInt1, int paramInt2)
/*      */   {
/* 1065 */     this.errorStatus = paramInt1;
/* 1066 */     this.errorIndex = paramInt2;
/*      */   }
/*      */ 
/*      */   final synchronized void setPrevPollTime(long paramLong)
/*      */   {
/* 1073 */     this.prevPollTime = paramLong;
/*      */   }
/*      */ 
/*      */   final void setRequestSentTime(long paramLong)
/*      */   {
/* 1080 */     this.numTries += 1;
/* 1081 */     setPrevPollTime(paramLong);
/* 1082 */     this.waitTimeForResponse = (this.prevPollTime + this.timeout * this.numTries);
/* 1083 */     setRequestStatus(5);
/*      */ 
/* 1085 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 1086 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpInformRequest.class.getName(), "setRequestSentTime", "Inform request Successfully sent");
/*      */     }
/*      */ 
/* 1090 */     this.informSession.getSnmpQManager().addWaiting(this);
/*      */   }
/*      */ 
/*      */   final synchronized void initNewRequest()
/*      */   {
/* 1097 */     this.requestId = requestCounter.getNewId();
/*      */   }
/*      */ 
/*      */   long timeRemainingForAction(long paramLong)
/*      */   {
/* 1104 */     switch (this.reqState) {
/*      */     case 3:
/* 1106 */       return this.nextPollTime - paramLong;
/*      */     case 5:
/* 1108 */       return this.waitTimeForResponse - paramLong;
/*      */     }
/* 1110 */     return -1L;
/*      */   }
/*      */ 
/*      */   static final String statusDescription(int paramInt)
/*      */   {
/* 1120 */     switch (paramInt) {
/*      */     case 3:
/* 1122 */       return "Waiting to send.";
/*      */     case 5:
/* 1124 */       return "Waiting for reply.";
/*      */     case 9:
/* 1126 */       return "Response arrived.";
/*      */     case 16:
/* 1128 */       return "Aborted by user.";
/*      */     case 32:
/* 1130 */       return "Timeout Occured.";
/*      */     case 64:
/* 1132 */       return "Internal error.";
/*      */     case 128:
/* 1134 */       return "Results available";
/*      */     case 256:
/* 1136 */       return "Inform request in createAndWait state";
/*      */     }
/* 1138 */     return "Unknown inform request state.";
/*      */   }
/*      */ 
/*      */   final synchronized void setRequestStatus(int paramInt)
/*      */   {
/* 1146 */     this.reqState = paramInt;
/*      */   }
/*      */ 
/*      */   public synchronized String toString()
/*      */   {
/* 1154 */     StringBuffer localStringBuffer = new StringBuffer(300);
/* 1155 */     localStringBuffer.append(tostring());
/* 1156 */     localStringBuffer.append("\nPeer/Port : " + this.address.getHostName() + "/" + this.port);
/*      */ 
/* 1158 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private synchronized String tostring() {
/* 1162 */     StringBuffer localStringBuffer = new StringBuffer("InformRequestId = " + this.requestId);
/* 1163 */     localStringBuffer.append("   Status = " + statusDescription(this.reqState));
/* 1164 */     localStringBuffer.append("  Timeout/MaxTries/NumTries = " + this.timeout * this.numTries + "/" + getMaxTries() + "/" + this.numTries);
/*      */ 
/* 1167 */     if (this.prevPollTime > 0L) {
/* 1168 */       this.debugDate.setTime(this.prevPollTime);
/* 1169 */       localStringBuffer.append("\nPrevPolled = " + this.debugDate.toString());
/*      */     } else {
/* 1171 */       localStringBuffer.append("\nNeverPolled");
/* 1172 */     }localStringBuffer.append(" / RemainingTime(millis) = " + timeRemainingForAction(System.currentTimeMillis()));
/*      */ 
/* 1175 */     return localStringBuffer.toString();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.daemon.SnmpInformRequest
 * JD-Core Version:    0.6.2
 */