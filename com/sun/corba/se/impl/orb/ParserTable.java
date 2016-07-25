/*      */ package com.sun.corba.se.impl.orb;
/*      */ 
/*      */ import com.sun.corba.se.impl.encoding.CodeSetComponentInfo;
/*      */ import com.sun.corba.se.impl.legacy.connection.USLPort;
/*      */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*      */ import com.sun.corba.se.impl.oa.poa.BadServerIdHandler;
/*      */ import com.sun.corba.se.impl.transport.DefaultIORToSocketInfoImpl;
/*      */ import com.sun.corba.se.impl.transport.DefaultSocketFactoryImpl;
/*      */ import com.sun.corba.se.pept.broker.Broker;
/*      */ import com.sun.corba.se.pept.encoding.InputObject;
/*      */ import com.sun.corba.se.pept.encoding.OutputObject;
/*      */ import com.sun.corba.se.pept.protocol.MessageMediator;
/*      */ import com.sun.corba.se.pept.transport.Acceptor;
/*      */ import com.sun.corba.se.pept.transport.Connection;
/*      */ import com.sun.corba.se.pept.transport.ContactInfo;
/*      */ import com.sun.corba.se.pept.transport.EventHandler;
/*      */ import com.sun.corba.se.pept.transport.InboundConnectionCache;
/*      */ import com.sun.corba.se.spi.ior.IOR;
/*      */ import com.sun.corba.se.spi.ior.ObjectKey;
/*      */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*      */ import com.sun.corba.se.spi.orb.Operation;
/*      */ import com.sun.corba.se.spi.orb.OperationFactory;
/*      */ import com.sun.corba.se.spi.orb.ParserData;
/*      */ import com.sun.corba.se.spi.orb.ParserDataFactory;
/*      */ import com.sun.corba.se.spi.orb.StringPair;
/*      */ import com.sun.corba.se.spi.transport.CorbaContactInfoList;
/*      */ import com.sun.corba.se.spi.transport.CorbaContactInfoListFactory;
/*      */ import com.sun.corba.se.spi.transport.IIOPPrimaryToContactInfo;
/*      */ import com.sun.corba.se.spi.transport.IORToSocketInfo;
/*      */ import com.sun.corba.se.spi.transport.ReadTimeouts;
/*      */ import com.sun.corba.se.spi.transport.ReadTimeoutsFactory;
/*      */ import com.sun.corba.se.spi.transport.SocketInfo;
/*      */ import com.sun.corba.se.spi.transport.TransportDefault;
/*      */ import java.net.InetSocketAddress;
/*      */ import java.net.ServerSocket;
/*      */ import java.net.Socket;
/*      */ import java.net.URL;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import org.omg.CORBA.LocalObject;
/*      */ import org.omg.PortableInterceptor.ORBInitInfo;
/*      */ import org.omg.PortableInterceptor.ORBInitializer;
/*      */ import sun.corba.JavaCorbaAccess;
/*      */ import sun.corba.SharedSecrets;
/*      */ 
/*      */ public class ParserTable
/*      */ {
/*   95 */   private static String MY_CLASS_NAME = ParserTable.class.getName();
/*      */ 
/*   97 */   private static ParserTable myInstance = new ParserTable();
/*      */   private ORBUtilSystemException wrapper;
/*      */   private ParserData[] parserData;
/*      */ 
/*      */   public static ParserTable get()
/*      */   {
/*  103 */     return myInstance;
/*      */   }
/*      */ 
/*      */   public ParserData[] getParserData()
/*      */   {
/*  110 */     ParserData[] arrayOfParserData = new ParserData[this.parserData.length];
/*  111 */     System.arraycopy(this.parserData, 0, arrayOfParserData, 0, this.parserData.length);
/*  112 */     return arrayOfParserData;
/*      */   }
/*      */ 
/*      */   private ParserTable() {
/*  116 */     this.wrapper = ORBUtilSystemException.get("orb.lifecycle");
/*      */ 
/*  118 */     String str1 = "65537,65801,65568";
/*      */ 
/*  123 */     String[] arrayOfString = { "subcontract", "poa", "transport" };
/*      */ 
/*  125 */     USLPort[] arrayOfUSLPort = { new USLPort("FOO", 2701), new USLPort("BAR", 3333) };
/*      */ 
/*  127 */     ReadTimeouts localReadTimeouts = TransportDefault.makeReadTimeoutsFactory().create(100, 3000, 300, 20);
/*      */ 
/*  134 */     ORBInitializer[] arrayOfORBInitializer = { null, new TestORBInitializer1(), new TestORBInitializer2() };
/*      */ 
/*  138 */     StringPair[] arrayOfStringPair1 = { new StringPair("foo.bar.blech.NonExistent", "dummy"), new StringPair(MY_CLASS_NAME + "$TestORBInitializer1", "dummy"), new StringPair(MY_CLASS_NAME + "$TestORBInitializer2", "dummy") };
/*      */ 
/*  143 */     Acceptor[] arrayOfAcceptor = { new TestAcceptor2(), new TestAcceptor1(), null };
/*      */ 
/*  149 */     StringPair[] arrayOfStringPair2 = { new StringPair("foo.bar.blech.NonExistent", "dummy"), new StringPair(MY_CLASS_NAME + "$TestAcceptor1", "dummy"), new StringPair(MY_CLASS_NAME + "$TestAcceptor2", "dummy") };
/*      */ 
/*  154 */     StringPair[] arrayOfStringPair3 = { new StringPair("Foo", "ior:930492049394"), new StringPair("Bar", "ior:3453465785633576") };
/*      */ 
/*  158 */     URL localURL = null;
/*  159 */     String str2 = "corbaloc::camelot/NameService";
/*      */     try
/*      */     {
/*  162 */       localURL = new URL(str2);
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*      */     }
/*      */ 
/*  170 */     ParserData[] arrayOfParserData = { ParserDataFactory.make("com.sun.CORBA.ORBDebug", OperationFactory.listAction(",", OperationFactory.stringAction()), "debugFlags", new String[0], arrayOfString, "subcontract,poa,transport"), ParserDataFactory.make("org.omg.CORBA.ORBInitialHost", OperationFactory.stringAction(), "ORBInitialHost", "", "Foo", "Foo"), ParserDataFactory.make("org.omg.CORBA.ORBInitialPort", OperationFactory.integerAction(), "ORBInitialPort", new Integer(900), new Integer(27314), "27314"), ParserDataFactory.make("com.sun.CORBA.ORBServerHost", OperationFactory.stringAction(), "ORBServerHost", "", "camelot", "camelot"), ParserDataFactory.make("com.sun.CORBA.ORBServerPort", OperationFactory.integerAction(), "ORBServerPort", new Integer(0), new Integer(38143), "38143"), ParserDataFactory.make("com.sun.CORBA.INTERNAL USE ONLY: listen on all interfaces", OperationFactory.stringAction(), "listenOnAllInterfaces", "com.sun.CORBA.INTERNAL USE ONLY: listen on all interfaces", "foo", "foo"), ParserDataFactory.make("org.omg.CORBA.ORBId", OperationFactory.stringAction(), "orbId", "", "foo", "foo"), ParserDataFactory.make("com.sun.CORBA.ORBid", OperationFactory.stringAction(), "orbId", "", "foo", "foo"), ParserDataFactory.make("org.omg.CORBA.ORBServerId", OperationFactory.integerAction(), "persistentServerId", new Integer(-1), new Integer(1234), "1234"), ParserDataFactory.make("org.omg.CORBA.ORBServerId", OperationFactory.setFlagAction(), "persistentServerIdInitialized", Boolean.FALSE, Boolean.TRUE, "1234"), ParserDataFactory.make("org.omg.CORBA.ORBServerId", OperationFactory.setFlagAction(), "orbServerIdPropertySpecified", Boolean.FALSE, Boolean.TRUE, "1234"), ParserDataFactory.make("com.sun.CORBA.connection.ORBHighWaterMark", OperationFactory.integerAction(), "highWaterMark", new Integer(240), new Integer(3745), "3745"), ParserDataFactory.make("com.sun.CORBA.connection.ORBLowWaterMark", OperationFactory.integerAction(), "lowWaterMark", new Integer(100), new Integer(12), "12"), ParserDataFactory.make("com.sun.CORBA.connection.ORBNumberToReclaim", OperationFactory.integerAction(), "numberToReclaim", new Integer(5), new Integer(231), "231"), ParserDataFactory.make("com.sun.CORBA.giop.ORBGIOPVersion", makeGVOperation(), "giopVersion", GIOPVersion.DEFAULT_VERSION, new GIOPVersion(2, 3), "2.3"), ParserDataFactory.make("com.sun.CORBA.giop.ORBFragmentSize", makeFSOperation(), "giopFragmentSize", new Integer(1024), new Integer(65536), "65536"), ParserDataFactory.make("com.sun.CORBA.giop.ORBBufferSize", OperationFactory.integerAction(), "giopBufferSize", new Integer(1024), new Integer(234000), "234000"), ParserDataFactory.make("com.sun.CORBA.giop.ORBGIOP11BuffMgr", makeBMGROperation(), "giop11BuffMgr", new Integer(0), new Integer(1), "CLCT"), ParserDataFactory.make("com.sun.CORBA.giop.ORBGIOP12BuffMgr", makeBMGROperation(), "giop12BuffMgr", new Integer(2), new Integer(0), "GROW"), ParserDataFactory.make("com.sun.CORBA.giop.ORBTargetAddressing", OperationFactory.compose(OperationFactory.integerRangeAction(0, 3), OperationFactory.convertIntegerToShort()), "giopTargetAddressPreference", new Short(3), new Short(2), "2"), ParserDataFactory.make("com.sun.CORBA.giop.ORBTargetAddressing", makeADOperation(), "giopAddressDisposition", new Short(0), new Short(2), "2"), ParserDataFactory.make("com.sun.CORBA.codeset.AlwaysSendCodeSetCtx", OperationFactory.booleanAction(), "alwaysSendCodeSetCtx", Boolean.TRUE, Boolean.FALSE, "false"), ParserDataFactory.make("com.sun.CORBA.codeset.UseByteOrderMarkers", OperationFactory.booleanAction(), "useByteOrderMarkers", Boolean.valueOf(true), Boolean.FALSE, "false"), ParserDataFactory.make("com.sun.CORBA.codeset.UseByteOrderMarkersInEncaps", OperationFactory.booleanAction(), "useByteOrderMarkersInEncaps", Boolean.valueOf(false), Boolean.FALSE, "false"), ParserDataFactory.make("com.sun.CORBA.codeset.charsets", makeCSOperation(), "charData", CodeSetComponentInfo.JAVASOFT_DEFAULT_CODESETS.getCharComponent(), CodeSetComponentInfo.createFromString(str1), str1), ParserDataFactory.make("com.sun.CORBA.codeset.wcharsets", makeCSOperation(), "wcharData", CodeSetComponentInfo.JAVASOFT_DEFAULT_CODESETS.getWCharComponent(), CodeSetComponentInfo.createFromString(str1), str1), ParserDataFactory.make("com.sun.CORBA.ORBAllowLocalOptimization", OperationFactory.booleanAction(), "allowLocalOptimization", Boolean.FALSE, Boolean.TRUE, "true"), ParserDataFactory.make("com.sun.CORBA.legacy.connection.ORBSocketFactoryClass", makeLegacySocketFactoryOperation(), "legacySocketFactory", null, new TestLegacyORBSocketFactory(), MY_CLASS_NAME + "$TestLegacyORBSocketFactory"), ParserDataFactory.make("com.sun.CORBA.transport.ORBSocketFactoryClass", makeSocketFactoryOperation(), "socketFactory", new DefaultSocketFactoryImpl(), new TestORBSocketFactory(), MY_CLASS_NAME + "$TestORBSocketFactory"), ParserDataFactory.make("com.sun.CORBA.transport.ORBListenSocket", makeUSLOperation(), "userSpecifiedListenPorts", new USLPort[0], arrayOfUSLPort, "FOO:2701,BAR:3333"), ParserDataFactory.make("com.sun.CORBA.transport.ORBIORToSocketInfoClass", makeIORToSocketInfoOperation(), "iorToSocketInfo", new DefaultIORToSocketInfoImpl(), new TestIORToSocketInfo(), MY_CLASS_NAME + "$TestIORToSocketInfo"), ParserDataFactory.make("com.sun.CORBA.transport.ORBIIOPPrimaryToContactInfoClass", makeIIOPPrimaryToContactInfoOperation(), "iiopPrimaryToContactInfo", null, new TestIIOPPrimaryToContactInfo(), MY_CLASS_NAME + "$TestIIOPPrimaryToContactInfo"), ParserDataFactory.make("com.sun.CORBA.transport.ORBContactInfoList", makeContactInfoListFactoryOperation(), "corbaContactInfoListFactory", null, new TestContactInfoListFactory(), MY_CLASS_NAME + "$TestContactInfoListFactory"), ParserDataFactory.make("com.sun.CORBA.POA.ORBPersistentServerPort", OperationFactory.integerAction(), "persistentServerPort", new Integer(0), new Integer(2743), "2743"), ParserDataFactory.make("com.sun.CORBA.POA.ORBPersistentServerPort", OperationFactory.setFlagAction(), "persistentPortInitialized", Boolean.FALSE, Boolean.TRUE, "2743"), ParserDataFactory.make("com.sun.CORBA.POA.ORBServerId", OperationFactory.integerAction(), "persistentServerId", new Integer(0), new Integer(294), "294"), ParserDataFactory.make("com.sun.CORBA.POA.ORBServerId", OperationFactory.setFlagAction(), "persistentServerIdInitialized", Boolean.FALSE, Boolean.TRUE, "294"), ParserDataFactory.make("com.sun.CORBA.POA.ORBServerId", OperationFactory.setFlagAction(), "orbServerIdPropertySpecified", Boolean.FALSE, Boolean.TRUE, "294"), ParserDataFactory.make("com.sun.CORBA.POA.ORBActivated", OperationFactory.booleanAction(), "serverIsORBActivated", Boolean.FALSE, Boolean.TRUE, "true"), ParserDataFactory.make("com.sun.CORBA.POA.ORBBadServerIdHandlerClass", OperationFactory.classAction(), "badServerIdHandlerClass", null, TestBadServerIdHandler.class, MY_CLASS_NAME + "$TestBadServerIdHandler"), ParserDataFactory.make("org.omg.PortableInterceptor.ORBInitializerClass.", makeROIOperation(), "orbInitializers", new ORBInitializer[0], arrayOfORBInitializer, arrayOfStringPair1, ORBInitializer.class), ParserDataFactory.make("com.sun.CORBA.transport.ORBAcceptor", makeAcceptorInstantiationOperation(), "acceptors", new Acceptor[0], arrayOfAcceptor, arrayOfStringPair2, Acceptor.class), ParserDataFactory.make("com.sun.CORBA.transport.ORBAcceptorSocketType", OperationFactory.stringAction(), "acceptorSocketType", "SocketChannel", "foo", "foo"), ParserDataFactory.make("com.sun.CORBA.transport.ORBUseNIOSelectToWait", OperationFactory.booleanAction(), "acceptorSocketUseSelectThreadToWait", Boolean.TRUE, Boolean.TRUE, "true"), ParserDataFactory.make("com.sun.CORBA.transport.ORBAcceptorSocketUseWorkerThreadForEvent", OperationFactory.booleanAction(), "acceptorSocketUseWorkerThreadForEvent", Boolean.TRUE, Boolean.TRUE, "true"), ParserDataFactory.make("com.sun.CORBA.transport.ORBConnectionSocketType", OperationFactory.stringAction(), "connectionSocketType", "SocketChannel", "foo", "foo"), ParserDataFactory.make("com.sun.CORBA.transport.ORBUseNIOSelectToWait", OperationFactory.booleanAction(), "connectionSocketUseSelectThreadToWait", Boolean.TRUE, Boolean.TRUE, "true"), ParserDataFactory.make("com.sun.CORBA.transport.ORBConnectionSocketUseWorkerThreadForEvent", OperationFactory.booleanAction(), "connectionSocketUseWorkerThreadForEvent", Boolean.TRUE, Boolean.TRUE, "true"), ParserDataFactory.make("com.sun.CORBA.transport.ORBDisableDirectByteBufferUse", OperationFactory.booleanAction(), "disableDirectByteBufferUse", Boolean.FALSE, Boolean.TRUE, "true"), ParserDataFactory.make("com.sun.CORBA.transport.ORBTCPReadTimeouts", makeTTCPRTOperation(), "readTimeouts", TransportDefault.makeReadTimeoutsFactory().create(100, 3000, 300, 20), localReadTimeouts, "100:3000:300:20"), ParserDataFactory.make("com.sun.CORBA.encoding.ORBEnableJavaSerialization", OperationFactory.booleanAction(), "enableJavaSerialization", Boolean.FALSE, Boolean.FALSE, "false"), ParserDataFactory.make("com.sun.CORBA.ORBUseRepId", OperationFactory.booleanAction(), "useRepId", Boolean.TRUE, Boolean.TRUE, "true"), ParserDataFactory.make("org.omg.CORBA.ORBInitRef", OperationFactory.identityAction(), "orbInitialReferences", new StringPair[0], arrayOfStringPair3, arrayOfStringPair3, StringPair.class) };
/*      */ 
/*  449 */     this.parserData = arrayOfParserData;
/*      */   }
/*      */ 
/*      */   private Operation makeTTCPRTOperation()
/*      */   {
/*  466 */     Operation[] arrayOfOperation = { OperationFactory.integerAction(), OperationFactory.integerAction(), OperationFactory.integerAction(), OperationFactory.integerAction() };
/*      */ 
/*  471 */     Operation localOperation1 = OperationFactory.sequenceAction(":", arrayOfOperation);
/*      */ 
/*  473 */     Operation local1 = new Operation()
/*      */     {
/*      */       public Object operate(Object paramAnonymousObject) {
/*  476 */         Object[] arrayOfObject = (Object[])paramAnonymousObject;
/*  477 */         Integer localInteger1 = (Integer)arrayOfObject[0];
/*  478 */         Integer localInteger2 = (Integer)arrayOfObject[1];
/*  479 */         Integer localInteger3 = (Integer)arrayOfObject[2];
/*  480 */         Integer localInteger4 = (Integer)arrayOfObject[3];
/*  481 */         return TransportDefault.makeReadTimeoutsFactory().create(localInteger1.intValue(), localInteger2.intValue(), localInteger3.intValue(), localInteger4.intValue());
/*      */       }
/*      */     };
/*  489 */     Operation localOperation2 = OperationFactory.compose(localOperation1, local1);
/*  490 */     return localOperation2;
/*      */   }
/*      */ 
/*      */   private Operation makeUSLOperation()
/*      */   {
/*  495 */     Operation[] arrayOfOperation = { OperationFactory.stringAction(), OperationFactory.integerAction() };
/*      */ 
/*  497 */     Operation localOperation1 = OperationFactory.sequenceAction(":", arrayOfOperation);
/*      */ 
/*  499 */     Operation local2 = new Operation()
/*      */     {
/*      */       public Object operate(Object paramAnonymousObject) {
/*  502 */         Object[] arrayOfObject = (Object[])paramAnonymousObject;
/*  503 */         String str = (String)arrayOfObject[0];
/*  504 */         Integer localInteger = (Integer)arrayOfObject[1];
/*  505 */         return new USLPort(str, localInteger.intValue());
/*      */       }
/*      */     };
/*  509 */     Operation localOperation2 = OperationFactory.compose(localOperation1, local2);
/*  510 */     Operation localOperation3 = OperationFactory.listAction(",", localOperation2);
/*  511 */     return localOperation3;
/*      */   }
/*      */ 
/*      */   private Operation makeMapOperation(final Map paramMap)
/*      */   {
/*  619 */     return new Operation()
/*      */     {
/*      */       public Object operate(Object paramAnonymousObject) {
/*  622 */         return paramMap.get(paramAnonymousObject);
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   private Operation makeBMGROperation()
/*      */   {
/*  629 */     HashMap localHashMap = new HashMap();
/*  630 */     localHashMap.put("GROW", new Integer(0));
/*  631 */     localHashMap.put("CLCT", new Integer(1));
/*  632 */     localHashMap.put("STRM", new Integer(2));
/*  633 */     return makeMapOperation(localHashMap);
/*      */   }
/*      */ 
/*      */   private Operation makeLegacySocketFactoryOperation()
/*      */   {
/*  638 */     Operation local4 = new Operation()
/*      */     {
/*      */       public Object operate(Object paramAnonymousObject) {
/*  641 */         String str = (String)paramAnonymousObject;
/*      */         try
/*      */         {
/*  644 */           Class localClass = SharedSecrets.getJavaCorbaAccess().loadClass(str);
/*      */ 
/*  649 */           if (com.sun.corba.se.spi.legacy.connection.ORBSocketFactory.class.isAssignableFrom(localClass)) {
/*  650 */             return localClass.newInstance();
/*      */           }
/*  652 */           throw ParserTable.this.wrapper.illegalSocketFactoryType(localClass.toString());
/*      */         }
/*      */         catch (Exception localException)
/*      */         {
/*  658 */           throw ParserTable.this.wrapper.badCustomSocketFactory(localException, str);
/*      */         }
/*      */       }
/*      */     };
/*  663 */     return local4;
/*      */   }
/*      */ 
/*      */   private Operation makeSocketFactoryOperation()
/*      */   {
/*  668 */     Operation local5 = new Operation()
/*      */     {
/*      */       public Object operate(Object paramAnonymousObject) {
/*  671 */         String str = (String)paramAnonymousObject;
/*      */         try
/*      */         {
/*  674 */           Class localClass = SharedSecrets.getJavaCorbaAccess().loadClass(str);
/*      */ 
/*  679 */           if (com.sun.corba.se.spi.transport.ORBSocketFactory.class.isAssignableFrom(localClass)) {
/*  680 */             return localClass.newInstance();
/*      */           }
/*  682 */           throw ParserTable.this.wrapper.illegalSocketFactoryType(localClass.toString());
/*      */         }
/*      */         catch (Exception localException)
/*      */         {
/*  688 */           throw ParserTable.this.wrapper.badCustomSocketFactory(localException, str);
/*      */         }
/*      */       }
/*      */     };
/*  693 */     return local5;
/*      */   }
/*      */ 
/*      */   private Operation makeIORToSocketInfoOperation()
/*      */   {
/*  698 */     Operation local6 = new Operation()
/*      */     {
/*      */       public Object operate(Object paramAnonymousObject) {
/*  701 */         String str = (String)paramAnonymousObject;
/*      */         try
/*      */         {
/*  704 */           Class localClass = SharedSecrets.getJavaCorbaAccess().loadClass(str);
/*      */ 
/*  709 */           if (IORToSocketInfo.class.isAssignableFrom(localClass)) {
/*  710 */             return localClass.newInstance();
/*      */           }
/*  712 */           throw ParserTable.this.wrapper.illegalIorToSocketInfoType(localClass.toString());
/*      */         }
/*      */         catch (Exception localException)
/*      */         {
/*  718 */           throw ParserTable.this.wrapper.badCustomIorToSocketInfo(localException, str);
/*      */         }
/*      */       }
/*      */     };
/*  723 */     return local6;
/*      */   }
/*      */ 
/*      */   private Operation makeIIOPPrimaryToContactInfoOperation()
/*      */   {
/*  728 */     Operation local7 = new Operation()
/*      */     {
/*      */       public Object operate(Object paramAnonymousObject) {
/*  731 */         String str = (String)paramAnonymousObject;
/*      */         try
/*      */         {
/*  734 */           Class localClass = SharedSecrets.getJavaCorbaAccess().loadClass(str);
/*      */ 
/*  739 */           if (IIOPPrimaryToContactInfo.class.isAssignableFrom(localClass)) {
/*  740 */             return localClass.newInstance();
/*      */           }
/*  742 */           throw ParserTable.this.wrapper.illegalIiopPrimaryToContactInfoType(localClass.toString());
/*      */         }
/*      */         catch (Exception localException)
/*      */         {
/*  748 */           throw ParserTable.this.wrapper.badCustomIiopPrimaryToContactInfo(localException, str);
/*      */         }
/*      */       }
/*      */     };
/*  753 */     return local7;
/*      */   }
/*      */ 
/*      */   private Operation makeContactInfoListFactoryOperation()
/*      */   {
/*  758 */     Operation local8 = new Operation()
/*      */     {
/*      */       public Object operate(Object paramAnonymousObject) {
/*  761 */         String str = (String)paramAnonymousObject;
/*      */         try
/*      */         {
/*  764 */           Class localClass = SharedSecrets.getJavaCorbaAccess().loadClass(str);
/*      */ 
/*  769 */           if (CorbaContactInfoListFactory.class.isAssignableFrom(localClass))
/*      */           {
/*  771 */             return localClass.newInstance();
/*      */           }
/*  773 */           throw ParserTable.this.wrapper.illegalContactInfoListFactoryType(localClass.toString());
/*      */         }
/*      */         catch (Exception localException)
/*      */         {
/*  780 */           throw ParserTable.this.wrapper.badContactInfoListFactory(localException, str);
/*      */         }
/*      */       }
/*      */     };
/*  785 */     return local8;
/*      */   }
/*      */ 
/*      */   private Operation makeCSOperation()
/*      */   {
/*  790 */     Operation local9 = new Operation()
/*      */     {
/*      */       public Object operate(Object paramAnonymousObject) {
/*  793 */         String str = (String)paramAnonymousObject;
/*  794 */         return CodeSetComponentInfo.createFromString(str);
/*      */       }
/*      */     };
/*  798 */     return local9;
/*      */   }
/*      */ 
/*      */   private Operation makeADOperation()
/*      */   {
/*  803 */     Operation local10 = new Operation() {
/*  804 */       private Integer[] map = { new Integer(0), new Integer(1), new Integer(2), new Integer(0) };
/*      */ 
/*      */       public Object operate(Object paramAnonymousObject)
/*      */       {
/*  812 */         int i = ((Integer)paramAnonymousObject).intValue();
/*  813 */         return this.map[i];
/*      */       }
/*      */     };
/*  817 */     Operation localOperation1 = OperationFactory.integerRangeAction(0, 3);
/*  818 */     Operation localOperation2 = OperationFactory.compose(localOperation1, local10);
/*  819 */     Operation localOperation3 = OperationFactory.compose(localOperation2, OperationFactory.convertIntegerToShort());
/*  820 */     return localOperation3;
/*      */   }
/*      */ 
/*      */   private Operation makeFSOperation() {
/*  824 */     Operation local11 = new Operation()
/*      */     {
/*      */       public Object operate(Object paramAnonymousObject) {
/*  827 */         int i = ((Integer)paramAnonymousObject).intValue();
/*  828 */         if (i < 32) {
/*  829 */           throw ParserTable.this.wrapper.fragmentSizeMinimum(new Integer(i), new Integer(32));
/*      */         }
/*      */ 
/*  833 */         if (i % 8 != 0) {
/*  834 */           throw ParserTable.this.wrapper.fragmentSizeDiv(new Integer(i), new Integer(8));
/*      */         }
/*      */ 
/*  837 */         return paramAnonymousObject;
/*      */       }
/*      */     };
/*  841 */     Operation localOperation = OperationFactory.compose(OperationFactory.integerAction(), local11);
/*      */ 
/*  843 */     return localOperation;
/*      */   }
/*      */ 
/*      */   private Operation makeGVOperation() {
/*  847 */     Operation localOperation1 = OperationFactory.listAction(".", OperationFactory.integerAction());
/*      */ 
/*  849 */     Operation local12 = new Operation()
/*      */     {
/*      */       public Object operate(Object paramAnonymousObject) {
/*  852 */         Object[] arrayOfObject = (Object[])paramAnonymousObject;
/*  853 */         int i = ((Integer)arrayOfObject[0]).intValue();
/*  854 */         int j = ((Integer)arrayOfObject[1]).intValue();
/*      */ 
/*  856 */         return new GIOPVersion(i, j);
/*      */       }
/*      */     };
/*  860 */     Operation localOperation2 = OperationFactory.compose(localOperation1, local12);
/*  861 */     return localOperation2;
/*      */   }
/*      */ 
/*      */   private Operation makeROIOperation()
/*      */   {
/*  899 */     Operation localOperation1 = OperationFactory.classAction();
/*  900 */     Operation localOperation2 = OperationFactory.suffixAction();
/*  901 */     Operation localOperation3 = OperationFactory.compose(localOperation2, localOperation1);
/*  902 */     Operation localOperation4 = OperationFactory.maskErrorAction(localOperation3);
/*      */ 
/*  904 */     Operation local13 = new Operation()
/*      */     {
/*      */       public Object operate(Object paramAnonymousObject) {
/*  907 */         final Class localClass = (Class)paramAnonymousObject;
/*  908 */         if (localClass == null) {
/*  909 */           return null;
/*      */         }
/*      */ 
/*  914 */         if (ORBInitializer.class.isAssignableFrom(localClass))
/*      */         {
/*  918 */           ORBInitializer localORBInitializer = null;
/*      */           try
/*      */           {
/*  921 */             localORBInitializer = (ORBInitializer)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */             {
/*      */               public Object run()
/*      */                 throws InstantiationException, IllegalAccessException
/*      */               {
/*  926 */                 return localClass.newInstance();
/*      */               }
/*      */             });
/*      */           }
/*      */           catch (PrivilegedActionException localPrivilegedActionException)
/*      */           {
/*  932 */             throw ParserTable.this.wrapper.orbInitializerFailure(localPrivilegedActionException.getException(), localClass.getName());
/*      */           }
/*      */           catch (Exception localException) {
/*  935 */             throw ParserTable.this.wrapper.orbInitializerFailure(localException, localClass.getName());
/*      */           }
/*      */ 
/*  938 */           return localORBInitializer;
/*      */         }
/*  940 */         throw ParserTable.this.wrapper.orbInitializerType(localClass.getName());
/*      */       }
/*      */     };
/*  945 */     Operation localOperation5 = OperationFactory.compose(localOperation4, local13);
/*      */ 
/*  947 */     return localOperation5;
/*      */   }
/*      */ 
/*      */   private Operation makeAcceptorInstantiationOperation()
/*      */   {
/* 1014 */     Operation localOperation1 = OperationFactory.classAction();
/* 1015 */     Operation localOperation2 = OperationFactory.suffixAction();
/* 1016 */     Operation localOperation3 = OperationFactory.compose(localOperation2, localOperation1);
/* 1017 */     Operation localOperation4 = OperationFactory.maskErrorAction(localOperation3);
/*      */ 
/* 1019 */     Operation local14 = new Operation()
/*      */     {
/*      */       public Object operate(Object paramAnonymousObject) {
/* 1022 */         final Class localClass = (Class)paramAnonymousObject;
/* 1023 */         if (localClass == null) {
/* 1024 */           return null;
/*      */         }
/*      */ 
/* 1029 */         if (Acceptor.class.isAssignableFrom(localClass))
/*      */         {
/* 1032 */           Acceptor localAcceptor = null;
/*      */           try
/*      */           {
/* 1035 */             localAcceptor = (Acceptor)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */             {
/*      */               public Object run()
/*      */                 throws InstantiationException, IllegalAccessException
/*      */               {
/* 1040 */                 return localClass.newInstance();
/*      */               }
/*      */             });
/*      */           }
/*      */           catch (PrivilegedActionException localPrivilegedActionException)
/*      */           {
/* 1046 */             throw ParserTable.this.wrapper.acceptorInstantiationFailure(localPrivilegedActionException.getException(), localClass.getName());
/*      */           }
/*      */           catch (Exception localException) {
/* 1049 */             throw ParserTable.this.wrapper.acceptorInstantiationFailure(localException, localClass.getName());
/*      */           }
/*      */ 
/* 1052 */           return localAcceptor;
/*      */         }
/* 1054 */         throw ParserTable.this.wrapper.acceptorInstantiationTypeFailure(localClass.getName());
/*      */       }
/*      */     };
/* 1059 */     Operation localOperation5 = OperationFactory.compose(localOperation4, local14);
/*      */ 
/* 1061 */     return localOperation5;
/*      */   }
/*      */ 
/*      */   private Operation makeInitRefOperation() {
/* 1065 */     return new Operation()
/*      */     {
/*      */       public Object operate(Object paramAnonymousObject)
/*      */       {
/* 1069 */         String[] arrayOfString = (String[])paramAnonymousObject;
/* 1070 */         if (arrayOfString.length != 2) {
/* 1071 */           throw ParserTable.this.wrapper.orbInitialreferenceSyntax();
/*      */         }
/* 1073 */         return arrayOfString[0] + "=" + arrayOfString[1];
/*      */       }
/*      */     };
/*      */   }
/*      */ 
/*      */   public static final class TestAcceptor1
/*      */     implements Acceptor
/*      */   {
/*      */     public boolean equals(Object paramObject)
/*      */     {
/*  955 */       return paramObject instanceof TestAcceptor1;
/*      */     }
/*  957 */     public boolean initialize() { return true; } 
/*  958 */     public boolean initialized() { return true; } 
/*  959 */     public String getConnectionCacheType() { return "FOO"; } 
/*      */     public void setConnectionCache(InboundConnectionCache paramInboundConnectionCache) {  } 
/*  961 */     public InboundConnectionCache getConnectionCache() { return null; } 
/*  962 */     public boolean shouldRegisterAcceptEvent() { return true; } 
/*      */     public void setUseSelectThreadForConnections(boolean paramBoolean) {  } 
/*  964 */     public boolean shouldUseSelectThreadForConnections() { return true; } 
/*      */     public void setUseWorkerThreadForConnections(boolean paramBoolean) {  } 
/*  966 */     public boolean shouldUseWorkerThreadForConnections() { return true; } 
/*      */     public void accept() {  } 
/*      */     public void close() {  } 
/*  969 */     public EventHandler getEventHandler() { return null; } 
/*      */     public MessageMediator createMessageMediator(Broker paramBroker, Connection paramConnection) {
/*  971 */       return null;
/*      */     }
/*      */     public MessageMediator finishCreatingMessageMediator(Broker paramBroker, Connection paramConnection, MessageMediator paramMessageMediator) {
/*  974 */       return null;
/*      */     }
/*  976 */     public InputObject createInputObject(Broker paramBroker, MessageMediator paramMessageMediator) { return null; } 
/*      */     public OutputObject createOutputObject(Broker paramBroker, MessageMediator paramMessageMediator) {
/*  978 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class TestAcceptor2 implements Acceptor
/*      */   {
/*      */     public boolean equals(Object paramObject)
/*      */     {
/*  986 */       return paramObject instanceof TestAcceptor2;
/*      */     }
/*  988 */     public boolean initialize() { return true; } 
/*  989 */     public boolean initialized() { return true; } 
/*  990 */     public String getConnectionCacheType() { return "FOO"; } 
/*      */     public void setConnectionCache(InboundConnectionCache paramInboundConnectionCache) {  } 
/*  992 */     public InboundConnectionCache getConnectionCache() { return null; } 
/*  993 */     public boolean shouldRegisterAcceptEvent() { return true; } 
/*      */     public void setUseSelectThreadForConnections(boolean paramBoolean) {  } 
/*  995 */     public boolean shouldUseSelectThreadForConnections() { return true; } 
/*      */     public void setUseWorkerThreadForConnections(boolean paramBoolean) {  } 
/*  997 */     public boolean shouldUseWorkerThreadForConnections() { return true; } 
/*      */     public void accept() {  } 
/*      */     public void close() {  } 
/* 1000 */     public EventHandler getEventHandler() { return null; } 
/*      */     public MessageMediator createMessageMediator(Broker paramBroker, Connection paramConnection) {
/* 1002 */       return null;
/*      */     }
/*      */     public MessageMediator finishCreatingMessageMediator(Broker paramBroker, Connection paramConnection, MessageMediator paramMessageMediator) {
/* 1005 */       return null;
/*      */     }
/* 1007 */     public InputObject createInputObject(Broker paramBroker, MessageMediator paramMessageMediator) { return null; } 
/*      */     public OutputObject createOutputObject(Broker paramBroker, MessageMediator paramMessageMediator) {
/* 1009 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final class TestBadServerIdHandler
/*      */     implements BadServerIdHandler
/*      */   {
/*      */     public TestBadServerIdHandler()
/*      */     {
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/*  456 */       return paramObject instanceof TestBadServerIdHandler;
/*      */     }
/*      */ 
/*      */     public void handle(ObjectKey paramObjectKey)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class TestContactInfoListFactory
/*      */     implements CorbaContactInfoListFactory
/*      */   {
/*      */     public boolean equals(Object paramObject)
/*      */     {
/*  609 */       return paramObject instanceof TestContactInfoListFactory;
/*      */     }
/*      */     public void setORB(com.sun.corba.se.spi.orb.ORB paramORB) {
/*      */     }
/*      */     public CorbaContactInfoList create(IOR paramIOR) {
/*  614 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class TestIIOPPrimaryToContactInfo
/*      */     implements IIOPPrimaryToContactInfo
/*      */   {
/*      */     public void reset(ContactInfo paramContactInfo)
/*      */     {
/*      */     }
/*      */ 
/*      */     public boolean hasNext(ContactInfo paramContactInfo1, ContactInfo paramContactInfo2, List paramList)
/*      */     {
/*  593 */       return true;
/*      */     }
/*      */ 
/*      */     public ContactInfo next(ContactInfo paramContactInfo1, ContactInfo paramContactInfo2, List paramList)
/*      */     {
/*  600 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class TestIORToSocketInfo
/*      */     implements IORToSocketInfo
/*      */   {
/*      */     public boolean equals(Object paramObject)
/*      */     {
/*  573 */       return paramObject instanceof TestIORToSocketInfo;
/*      */     }
/*      */ 
/*      */     public List getSocketInfo(IOR paramIOR)
/*      */     {
/*  578 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class TestLegacyORBSocketFactory
/*      */     implements com.sun.corba.se.spi.legacy.connection.ORBSocketFactory
/*      */   {
/*      */     public boolean equals(Object paramObject)
/*      */     {
/*  519 */       return paramObject instanceof TestLegacyORBSocketFactory;
/*      */     }
/*      */ 
/*      */     public ServerSocket createServerSocket(String paramString, int paramInt)
/*      */     {
/*  524 */       return null;
/*      */     }
/*      */ 
/*      */     public SocketInfo getEndPointInfo(org.omg.CORBA.ORB paramORB, IOR paramIOR, SocketInfo paramSocketInfo)
/*      */     {
/*  530 */       return null;
/*      */     }
/*      */ 
/*      */     public Socket createSocket(SocketInfo paramSocketInfo)
/*      */     {
/*  535 */       return null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class TestORBInitializer1 extends LocalObject
/*      */     implements ORBInitializer
/*      */   {
/*      */     public boolean equals(Object paramObject)
/*      */     {
/*  869 */       return paramObject instanceof TestORBInitializer1;
/*      */     }
/*      */ 
/*      */     public void pre_init(ORBInitInfo paramORBInitInfo)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void post_init(ORBInitInfo paramORBInitInfo)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class TestORBInitializer2 extends LocalObject
/*      */     implements ORBInitializer
/*      */   {
/*      */     public boolean equals(Object paramObject)
/*      */     {
/*  886 */       return paramObject instanceof TestORBInitializer2;
/*      */     }
/*      */ 
/*      */     public void pre_init(ORBInitInfo paramORBInitInfo)
/*      */     {
/*      */     }
/*      */ 
/*      */     public void post_init(ORBInitInfo paramORBInitInfo)
/*      */     {
/*      */     }
/*      */   }
/*      */ 
/*      */   public static final class TestORBSocketFactory
/*      */     implements com.sun.corba.se.spi.transport.ORBSocketFactory
/*      */   {
/*      */     public boolean equals(Object paramObject)
/*      */     {
/*  544 */       return paramObject instanceof TestORBSocketFactory;
/*      */     }
/*      */ 
/*      */     public void setORB(com.sun.corba.se.spi.orb.ORB paramORB)
/*      */     {
/*      */     }
/*      */ 
/*      */     public ServerSocket createServerSocket(String paramString, InetSocketAddress paramInetSocketAddress)
/*      */     {
/*  553 */       return null;
/*      */     }
/*      */ 
/*      */     public Socket createSocket(String paramString, InetSocketAddress paramInetSocketAddress)
/*      */     {
/*  558 */       return null;
/*      */     }
/*      */ 
/*      */     public void setAcceptedSocketOptions(Acceptor paramAcceptor, ServerSocket paramServerSocket, Socket paramSocket)
/*      */     {
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orb.ParserTable
 * JD-Core Version:    0.6.2
 */