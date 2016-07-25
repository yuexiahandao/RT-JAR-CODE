/*      */ package com.sun.corba.se.impl.encoding;
/*      */ 
/*      */ import com.sun.corba.se.impl.corba.CORBAObjectImpl;
/*      */ import com.sun.corba.se.impl.corba.PrincipalImpl;
/*      */ import com.sun.corba.se.impl.corba.TypeCodeImpl;
/*      */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*      */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*      */ import com.sun.corba.se.impl.orbutil.CacheTable;
/*      */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*      */ import com.sun.corba.se.impl.orbutil.RepositoryIdFactory;
/*      */ import com.sun.corba.se.impl.orbutil.RepositoryIdInterface;
/*      */ import com.sun.corba.se.impl.orbutil.RepositoryIdStrings;
/*      */ import com.sun.corba.se.impl.orbutil.RepositoryIdUtility;
/*      */ import com.sun.corba.se.impl.util.RepositoryId;
/*      */ import com.sun.corba.se.impl.util.RepositoryIdCache;
/*      */ import com.sun.corba.se.impl.util.Utility;
/*      */ import com.sun.corba.se.pept.protocol.MessageMediator;
/*      */ import com.sun.corba.se.pept.transport.ByteBufferPool;
/*      */ import com.sun.corba.se.spi.ior.IOR;
/*      */ import com.sun.corba.se.spi.ior.IORFactories;
/*      */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*      */ import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
/*      */ import com.sun.corba.se.spi.orb.ORBVersion;
/*      */ import com.sun.corba.se.spi.orb.ORBVersionFactory;
/*      */ import com.sun.corba.se.spi.presentation.rmi.PresentationDefaults;
/*      */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactory;
/*      */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactoryFactory;
/*      */ import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
/*      */ import com.sun.org.omg.CORBA.portable.ValueHelper;
/*      */ import com.sun.org.omg.SendingContext.CodeBase;
/*      */ import java.io.IOException;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.math.BigDecimal;
/*      */ import java.net.MalformedURLException;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedActionException;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import javax.rmi.CORBA.Tie;
/*      */ import javax.rmi.CORBA.ValueHandler;
/*      */ import org.omg.CORBA.Any;
/*      */ import org.omg.CORBA.AnySeqHolder;
/*      */ import org.omg.CORBA.BooleanSeqHolder;
/*      */ import org.omg.CORBA.CharSeqHolder;
/*      */ import org.omg.CORBA.CompletionStatus;
/*      */ import org.omg.CORBA.CustomMarshal;
/*      */ import org.omg.CORBA.DoubleSeqHolder;
/*      */ import org.omg.CORBA.FloatSeqHolder;
/*      */ import org.omg.CORBA.LongLongSeqHolder;
/*      */ import org.omg.CORBA.LongSeqHolder;
/*      */ import org.omg.CORBA.MARSHAL;
/*      */ import org.omg.CORBA.OctetSeqHolder;
/*      */ import org.omg.CORBA.Principal;
/*      */ import org.omg.CORBA.ShortSeqHolder;
/*      */ import org.omg.CORBA.SystemException;
/*      */ import org.omg.CORBA.TCKind;
/*      */ import org.omg.CORBA.TypeCode;
/*      */ import org.omg.CORBA.TypeCodePackage.BadKind;
/*      */ import org.omg.CORBA.ULongLongSeqHolder;
/*      */ import org.omg.CORBA.ULongSeqHolder;
/*      */ import org.omg.CORBA.UShortSeqHolder;
/*      */ import org.omg.CORBA.WCharSeqHolder;
/*      */ import org.omg.CORBA.portable.BoxedValueHelper;
/*      */ import org.omg.CORBA.portable.CustomValue;
/*      */ import org.omg.CORBA.portable.Delegate;
/*      */ import org.omg.CORBA.portable.IDLEntity;
/*      */ import org.omg.CORBA.portable.IndirectionException;
/*      */ import org.omg.CORBA.portable.InputStream;
/*      */ import org.omg.CORBA.portable.InvokeHandler;
/*      */ import org.omg.CORBA.portable.StreamableValue;
/*      */ import org.omg.CORBA.portable.ValueBase;
/*      */ import org.omg.CORBA.portable.ValueFactory;
/*      */ 
/*      */ public class CDRInputStream_1_0 extends CDRInputStreamBase
/*      */   implements RestorableInputStream
/*      */ {
/*      */   private static final String kReadMethod = "read";
/*      */   private static final int maxBlockLength = 2147483392;
/*      */   protected BufferManagerRead bufferManagerRead;
/*      */   protected ByteBufferWithInfo bbwi;
/*      */   private boolean debug;
/*      */   protected boolean littleEndian;
/*      */   protected com.sun.corba.se.spi.orb.ORB orb;
/*      */   protected ORBUtilSystemException wrapper;
/*      */   protected OMGSystemException omgWrapper;
/*      */   protected ValueHandler valueHandler;
/*      */   private CacheTable valueCache;
/*      */   private CacheTable repositoryIdCache;
/*      */   private CacheTable codebaseCache;
/*      */   protected int blockLength;
/*      */   protected int end_flag;
/*      */   private int chunkedValueNestingLevel;
/*      */   protected int valueIndirection;
/*      */   protected int stringIndirection;
/*      */   protected boolean isChunked;
/*      */   private RepositoryIdUtility repIdUtil;
/*      */   private RepositoryIdStrings repIdStrs;
/*      */   private CodeSetConversion.BTCConverter charConverter;
/*      */   private CodeSetConversion.BTCConverter wcharConverter;
/*      */   private boolean specialNoOptionalDataState;
/*      */   private static final String _id = "IDL:omg.org/CORBA/DataInputStream:1.0";
/* 1871 */   private static final String[] _ids = { "IDL:omg.org/CORBA/DataInputStream:1.0" };
/*      */   protected MarkAndResetHandler markAndResetHandler;
/*      */ 
/*      */   public CDRInputStream_1_0()
/*      */   {
/*  148 */     this.debug = false;
/*      */ 
/*  154 */     this.valueHandler = null;
/*      */ 
/*  157 */     this.valueCache = null;
/*      */ 
/*  160 */     this.repositoryIdCache = null;
/*      */ 
/*  163 */     this.codebaseCache = null;
/*      */ 
/*  169 */     this.blockLength = 2147483392;
/*      */ 
/*  172 */     this.end_flag = 0;
/*      */ 
/*  179 */     this.chunkedValueNestingLevel = 0;
/*      */ 
/*  192 */     this.valueIndirection = 0;
/*      */ 
/*  196 */     this.stringIndirection = 0;
/*      */ 
/*  199 */     this.isChunked = false;
/*      */ 
/*  216 */     this.specialNoOptionalDataState = false;
/*      */ 
/* 2018 */     this.markAndResetHandler = null;
/*      */   }
/*      */ 
/*      */   public CDRInputStreamBase dup()
/*      */   {
/*  221 */     CDRInputStreamBase localCDRInputStreamBase = null;
/*      */     try
/*      */     {
/*  224 */       localCDRInputStreamBase = (CDRInputStreamBase)getClass().newInstance();
/*      */     } catch (Exception localException) {
/*  226 */       throw this.wrapper.couldNotDuplicateCdrInputStream(localException);
/*      */     }
/*  228 */     localCDRInputStreamBase.init(this.orb, this.bbwi.byteBuffer, this.bbwi.buflen, this.littleEndian, this.bufferManagerRead);
/*      */ 
/*  234 */     ((CDRInputStream_1_0)localCDRInputStreamBase).bbwi.position(this.bbwi.position());
/*      */ 
/*  236 */     ((CDRInputStream_1_0)localCDRInputStreamBase).bbwi.byteBuffer.limit(this.bbwi.buflen);
/*      */ 
/*  238 */     return localCDRInputStreamBase;
/*      */   }
/*      */ 
/*      */   public void init(org.omg.CORBA.ORB paramORB, ByteBuffer paramByteBuffer, int paramInt, boolean paramBoolean, BufferManagerRead paramBufferManagerRead)
/*      */   {
/*  250 */     this.orb = ((com.sun.corba.se.spi.orb.ORB)paramORB);
/*  251 */     this.wrapper = ORBUtilSystemException.get((com.sun.corba.se.spi.orb.ORB)paramORB, "rpc.encoding");
/*      */ 
/*  253 */     this.omgWrapper = OMGSystemException.get((com.sun.corba.se.spi.orb.ORB)paramORB, "rpc.encoding");
/*      */ 
/*  255 */     this.littleEndian = paramBoolean;
/*  256 */     this.bufferManagerRead = paramBufferManagerRead;
/*  257 */     this.bbwi = new ByteBufferWithInfo(paramORB, paramByteBuffer, 0);
/*  258 */     this.bbwi.buflen = paramInt;
/*  259 */     this.bbwi.byteBuffer.limit(this.bbwi.buflen);
/*  260 */     this.markAndResetHandler = this.bufferManagerRead.getMarkAndResetHandler();
/*      */ 
/*  262 */     this.debug = ((com.sun.corba.se.spi.orb.ORB)paramORB).transportDebugFlag;
/*      */   }
/*      */ 
/*      */   void performORBVersionSpecificInit()
/*      */   {
/*  267 */     createRepositoryIdHandlers();
/*      */   }
/*      */ 
/*      */   private final void createRepositoryIdHandlers()
/*      */   {
/*  272 */     this.repIdUtil = RepositoryIdFactory.getRepIdUtility();
/*  273 */     this.repIdStrs = RepositoryIdFactory.getRepIdStringsFactory();
/*      */   }
/*      */ 
/*      */   public GIOPVersion getGIOPVersion() {
/*  277 */     return GIOPVersion.V1_0;
/*      */   }
/*      */ 
/*      */   void setHeaderPadding(boolean paramBoolean)
/*      */   {
/*  283 */     throw this.wrapper.giopVersionError();
/*      */   }
/*      */ 
/*      */   protected final int computeAlignment(int paramInt1, int paramInt2) {
/*  287 */     if (paramInt2 > 1) {
/*  288 */       int i = paramInt1 & paramInt2 - 1;
/*  289 */       if (i != 0) {
/*  290 */         return paramInt2 - i;
/*      */       }
/*      */     }
/*  293 */     return 0;
/*      */   }
/*      */ 
/*      */   public int getSize()
/*      */   {
/*  298 */     return this.bbwi.position();
/*      */   }
/*      */ 
/*      */   protected void checkBlockLength(int paramInt1, int paramInt2)
/*      */   {
/*  306 */     if (!this.isChunked) {
/*  307 */       return;
/*      */     }
/*      */ 
/*  316 */     if (this.specialNoOptionalDataState) {
/*  317 */       throw this.omgWrapper.rmiiiopOptionalDataIncompatible1();
/*      */     }
/*      */ 
/*  320 */     int i = 0;
/*      */ 
/*  329 */     if (this.blockLength == get_offset())
/*      */     {
/*  331 */       this.blockLength = 2147483392;
/*  332 */       start_block();
/*      */ 
/*  340 */       if (this.blockLength == 2147483392) {
/*  341 */         i = 1;
/*      */       }
/*      */     }
/*  344 */     else if (this.blockLength < get_offset())
/*      */     {
/*  347 */       throw this.wrapper.chunkOverflow();
/*      */     }
/*      */ 
/*  356 */     int j = computeAlignment(this.bbwi.position(), paramInt1) + paramInt2;
/*      */ 
/*  359 */     if ((this.blockLength != 2147483392) && (this.blockLength < get_offset() + j))
/*      */     {
/*  361 */       throw this.omgWrapper.rmiiiopOptionalDataIncompatible2();
/*      */     }
/*      */ 
/*  369 */     if (i != 0) {
/*  370 */       int k = read_long();
/*  371 */       this.bbwi.position(this.bbwi.position() - 4);
/*      */ 
/*  376 */       if (k < 0)
/*  377 */         throw this.omgWrapper.rmiiiopOptionalDataIncompatible3();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void alignAndCheck(int paramInt1, int paramInt2)
/*      */   {
/*  383 */     checkBlockLength(paramInt1, paramInt2);
/*      */ 
/*  387 */     int i = computeAlignment(this.bbwi.position(), paramInt1);
/*  388 */     this.bbwi.position(this.bbwi.position() + i);
/*      */ 
/*  390 */     if (this.bbwi.position() + paramInt2 > this.bbwi.buflen)
/*  391 */       grow(paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   protected void grow(int paramInt1, int paramInt2)
/*      */   {
/*  399 */     this.bbwi.needed = paramInt2;
/*      */ 
/*  401 */     this.bbwi = this.bufferManagerRead.underflow(this.bbwi);
/*      */   }
/*      */ 
/*      */   public final void consumeEndian()
/*      */   {
/*  410 */     this.littleEndian = read_boolean();
/*      */   }
/*      */ 
/*      */   public final double read_longdouble()
/*      */   {
/*  415 */     throw this.wrapper.longDoubleNotImplemented(CompletionStatus.COMPLETED_MAYBE);
/*      */   }
/*      */ 
/*      */   public final boolean read_boolean() {
/*  419 */     return read_octet() != 0;
/*      */   }
/*      */ 
/*      */   public final char read_char() {
/*  423 */     alignAndCheck(1, 1);
/*      */ 
/*  425 */     return getConvertedChars(1, getCharConverter())[0];
/*      */   }
/*      */ 
/*      */   public char read_wchar()
/*      */   {
/*  432 */     if (ORBUtility.isForeignORB(this.orb)) {
/*  433 */       throw this.wrapper.wcharDataInGiop10(CompletionStatus.COMPLETED_MAYBE);
/*      */     }
/*      */ 
/*  440 */     alignAndCheck(2, 2);
/*      */     int j;
/*      */     int i;
/*  442 */     if (this.littleEndian) {
/*  443 */       j = this.bbwi.byteBuffer.get(this.bbwi.position()) & 0xFF;
/*  444 */       this.bbwi.position(this.bbwi.position() + 1);
/*  445 */       i = this.bbwi.byteBuffer.get(this.bbwi.position()) & 0xFF;
/*  446 */       this.bbwi.position(this.bbwi.position() + 1);
/*      */     } else {
/*  448 */       i = this.bbwi.byteBuffer.get(this.bbwi.position()) & 0xFF;
/*  449 */       this.bbwi.position(this.bbwi.position() + 1);
/*  450 */       j = this.bbwi.byteBuffer.get(this.bbwi.position()) & 0xFF;
/*  451 */       this.bbwi.position(this.bbwi.position() + 1);
/*      */     }
/*      */ 
/*  454 */     return (char)((i << 8) + (j << 0));
/*      */   }
/*      */ 
/*      */   public final byte read_octet()
/*      */   {
/*  459 */     alignAndCheck(1, 1);
/*      */ 
/*  461 */     byte b = this.bbwi.byteBuffer.get(this.bbwi.position());
/*  462 */     this.bbwi.position(this.bbwi.position() + 1);
/*      */ 
/*  464 */     return b;
/*      */   }
/*      */ 
/*      */   public final short read_short()
/*      */   {
/*  470 */     alignAndCheck(2, 2);
/*      */     int j;
/*      */     int i;
/*  472 */     if (this.littleEndian) {
/*  473 */       j = this.bbwi.byteBuffer.get(this.bbwi.position()) << 0 & 0xFF;
/*  474 */       this.bbwi.position(this.bbwi.position() + 1);
/*  475 */       i = this.bbwi.byteBuffer.get(this.bbwi.position()) << 8 & 0xFF00;
/*  476 */       this.bbwi.position(this.bbwi.position() + 1);
/*      */     } else {
/*  478 */       i = this.bbwi.byteBuffer.get(this.bbwi.position()) << 8 & 0xFF00;
/*  479 */       this.bbwi.position(this.bbwi.position() + 1);
/*  480 */       j = this.bbwi.byteBuffer.get(this.bbwi.position()) << 0 & 0xFF;
/*  481 */       this.bbwi.position(this.bbwi.position() + 1);
/*      */     }
/*      */ 
/*  484 */     return (short)(i | j);
/*      */   }
/*      */ 
/*      */   public final short read_ushort() {
/*  488 */     return read_short();
/*      */   }
/*      */ 
/*      */   public final int read_long()
/*      */   {
/*  494 */     alignAndCheck(4, 4);
/*      */ 
/*  496 */     int n = this.bbwi.position();
/*      */     int m;
/*      */     int k;
/*      */     int j;
/*      */     int i;
/*  497 */     if (this.littleEndian) {
/*  498 */       m = this.bbwi.byteBuffer.get(n++) & 0xFF;
/*  499 */       k = this.bbwi.byteBuffer.get(n++) & 0xFF;
/*  500 */       j = this.bbwi.byteBuffer.get(n++) & 0xFF;
/*  501 */       i = this.bbwi.byteBuffer.get(n++) & 0xFF;
/*      */     } else {
/*  503 */       i = this.bbwi.byteBuffer.get(n++) & 0xFF;
/*  504 */       j = this.bbwi.byteBuffer.get(n++) & 0xFF;
/*  505 */       k = this.bbwi.byteBuffer.get(n++) & 0xFF;
/*  506 */       m = this.bbwi.byteBuffer.get(n++) & 0xFF;
/*      */     }
/*  508 */     this.bbwi.position(n);
/*      */ 
/*  510 */     return i << 24 | j << 16 | k << 8 | m;
/*      */   }
/*      */ 
/*      */   public final int read_ulong() {
/*  514 */     return read_long();
/*      */   }
/*      */ 
/*      */   public final long read_longlong()
/*      */   {
/*  520 */     alignAndCheck(8, 8);
/*      */     long l2;
/*      */     long l1;
/*  522 */     if (this.littleEndian) {
/*  523 */       l2 = read_long() & 0xFFFFFFFF;
/*  524 */       l1 = read_long() << 32;
/*      */     } else {
/*  526 */       l1 = read_long() << 32;
/*  527 */       l2 = read_long() & 0xFFFFFFFF;
/*      */     }
/*      */ 
/*  530 */     return l1 | l2;
/*      */   }
/*      */ 
/*      */   public final long read_ulonglong() {
/*  534 */     return read_longlong();
/*      */   }
/*      */ 
/*      */   public final float read_float() {
/*  538 */     return Float.intBitsToFloat(read_long());
/*      */   }
/*      */ 
/*      */   public final double read_double() {
/*  542 */     return Double.longBitsToDouble(read_longlong());
/*      */   }
/*      */ 
/*      */   protected final void checkForNegativeLength(int paramInt) {
/*  546 */     if (paramInt < 0)
/*  547 */       throw this.wrapper.negativeStringLength(CompletionStatus.COMPLETED_MAYBE, new Integer(paramInt));
/*      */   }
/*      */ 
/*      */   protected final String readStringOrIndirection(boolean paramBoolean)
/*      */   {
/*  553 */     int i = read_long();
/*      */ 
/*  558 */     if (paramBoolean) {
/*  559 */       if (i == -1) {
/*  560 */         return null;
/*      */       }
/*  562 */       this.stringIndirection = (get_offset() - 4);
/*      */     }
/*      */ 
/*  565 */     checkForNegativeLength(i);
/*      */ 
/*  567 */     return internalReadString(i);
/*      */   }
/*      */ 
/*      */   private final String internalReadString(int paramInt)
/*      */   {
/*  577 */     if (paramInt == 0) {
/*  578 */       return new String("");
/*      */     }
/*  580 */     char[] arrayOfChar = getConvertedChars(paramInt - 1, getCharConverter());
/*      */ 
/*  583 */     read_octet();
/*      */ 
/*  585 */     return new String(arrayOfChar, 0, getCharConverter().getNumChars());
/*      */   }
/*      */ 
/*      */   public final String read_string() {
/*  589 */     return readStringOrIndirection(false);
/*      */   }
/*      */ 
/*      */   public String read_wstring()
/*      */   {
/*  595 */     if (ORBUtility.isForeignORB(this.orb)) {
/*  596 */       throw this.wrapper.wcharDataInGiop10(CompletionStatus.COMPLETED_MAYBE);
/*      */     }
/*      */ 
/*  599 */     int i = read_long();
/*      */ 
/*  609 */     if (i == 0) {
/*  610 */       return new String("");
/*      */     }
/*  612 */     checkForNegativeLength(i);
/*      */ 
/*  614 */     i--;
/*  615 */     char[] arrayOfChar = new char[i];
/*      */ 
/*  617 */     for (int j = 0; j < i; j++) {
/*  618 */       arrayOfChar[j] = read_wchar();
/*      */     }
/*      */ 
/*  621 */     read_wchar();
/*      */ 
/*  624 */     return new String(arrayOfChar);
/*      */   }
/*      */ 
/*      */   public final void read_octet_array(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/*  628 */     if (paramArrayOfByte == null) {
/*  629 */       throw this.wrapper.nullParam();
/*      */     }
/*      */ 
/*  636 */     if (paramInt2 == 0) {
/*  637 */       return;
/*      */     }
/*  639 */     alignAndCheck(1, 1);
/*      */ 
/*  641 */     int i = paramInt1;
/*  642 */     while (i < paramInt2 + paramInt1)
/*      */     {
/*  647 */       int j = this.bbwi.buflen - this.bbwi.position();
/*  648 */       if (j <= 0) {
/*  649 */         grow(1, 1);
/*  650 */         j = this.bbwi.buflen - this.bbwi.position();
/*      */       }
/*  652 */       int m = paramInt2 + paramInt1 - i;
/*  653 */       int k = m < j ? m : j;
/*      */ 
/*  656 */       for (int n = 0; n < k; n++) {
/*  657 */         paramArrayOfByte[(i + n)] = this.bbwi.byteBuffer.get(this.bbwi.position() + n);
/*      */       }
/*      */ 
/*  660 */       this.bbwi.position(this.bbwi.position() + k);
/*      */ 
/*  662 */       i += k;
/*      */     }
/*      */   }
/*      */ 
/*      */   public Principal read_Principal() {
/*  667 */     int i = read_long();
/*  668 */     byte[] arrayOfByte = new byte[i];
/*  669 */     read_octet_array(arrayOfByte, 0, i);
/*      */ 
/*  671 */     PrincipalImpl localPrincipalImpl = new PrincipalImpl();
/*  672 */     localPrincipalImpl.name(arrayOfByte);
/*  673 */     return localPrincipalImpl;
/*      */   }
/*      */ 
/*      */   public TypeCode read_TypeCode() {
/*  677 */     TypeCodeImpl localTypeCodeImpl = new TypeCodeImpl(this.orb);
/*  678 */     localTypeCodeImpl.read_value(this.parent);
/*  679 */     return localTypeCodeImpl;
/*      */   }
/*      */ 
/*      */   public Any read_any() {
/*  683 */     Any localAny = this.orb.create_any();
/*  684 */     TypeCodeImpl localTypeCodeImpl = new TypeCodeImpl(this.orb);
/*      */     try
/*      */     {
/*  695 */       localTypeCodeImpl.read_value(this.parent);
/*      */     } catch (MARSHAL localMARSHAL) {
/*  697 */       if (localTypeCodeImpl.kind().value() != 29) {
/*  698 */         throw localMARSHAL;
/*      */       }
/*      */ 
/*  701 */       dprintThrowable(localMARSHAL);
/*      */     }
/*      */ 
/*  704 */     localAny.read_value(this.parent, localTypeCodeImpl);
/*      */ 
/*  706 */     return localAny;
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object read_Object() {
/*  710 */     return read_Object(null);
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object read_Object(Class paramClass)
/*      */   {
/*  731 */     IOR localIOR = IORFactories.makeIOR(this.parent);
/*  732 */     if (localIOR.isNil()) {
/*  733 */       return null;
/*      */     }
/*  735 */     PresentationManager.StubFactoryFactory localStubFactoryFactory = com.sun.corba.se.spi.orb.ORB.getStubFactoryFactory();
/*  736 */     String str1 = localIOR.getProfile().getCodebase();
/*  737 */     PresentationManager.StubFactory localStubFactory = null;
/*      */ 
/*  739 */     if (paramClass == null) {
/*  740 */       RepositoryId localRepositoryId = RepositoryId.cache.getId(localIOR.getTypeId());
/*  741 */       String str2 = localRepositoryId.getClassName();
/*  742 */       boolean bool2 = localRepositoryId.isIDLType();
/*      */ 
/*  744 */       if ((str2 == null) || (str2.equals("")))
/*  745 */         localStubFactory = null;
/*      */       else
/*      */         try {
/*  748 */           localStubFactory = localStubFactoryFactory.createStubFactory(str2, bool2, str1, (Class)null, (ClassLoader)null);
/*      */         }
/*      */         catch (Exception localException)
/*      */         {
/*  756 */           localStubFactory = null;
/*      */         }
/*  758 */     } else if (StubAdapter.isStubClass(paramClass)) {
/*  759 */       localStubFactory = PresentationDefaults.makeStaticStubFactory(paramClass);
/*      */     }
/*      */     else
/*      */     {
/*  763 */       boolean bool1 = IDLEntity.class.isAssignableFrom(paramClass);
/*      */ 
/*  765 */       localStubFactory = localStubFactoryFactory.createStubFactory(paramClass.getName(), bool1, str1, paramClass, paramClass.getClassLoader());
/*      */     }
/*      */ 
/*  769 */     return internalIORToObject(localIOR, localStubFactory, this.orb);
/*      */   }
/*      */ 
/*      */   public static org.omg.CORBA.Object internalIORToObject(IOR paramIOR, PresentationManager.StubFactory paramStubFactory, com.sun.corba.se.spi.orb.ORB paramORB)
/*      */   {
/*  780 */     ORBUtilSystemException localORBUtilSystemException = ORBUtilSystemException.get(paramORB, "rpc.encoding");
/*      */ 
/*  783 */     java.lang.Object localObject1 = paramIOR.getProfile().getServant();
/*  784 */     if (localObject1 != null) {
/*  785 */       if ((localObject1 instanceof Tie)) {
/*  786 */         localObject2 = paramIOR.getProfile().getCodebase();
/*  787 */         localObject3 = (org.omg.CORBA.Object)Utility.loadStub((Tie)localObject1, paramStubFactory, (String)localObject2, false);
/*      */ 
/*  793 */         if (localObject3 != null) {
/*  794 */           return localObject3;
/*      */         }
/*  796 */         throw localORBUtilSystemException.readObjectException();
/*      */       }
/*  798 */       if ((localObject1 instanceof org.omg.CORBA.Object)) {
/*  799 */         if (!(localObject1 instanceof InvokeHandler))
/*      */         {
/*  801 */           return (org.omg.CORBA.Object)localObject1;
/*      */         }
/*      */       }
/*  804 */       else throw localORBUtilSystemException.badServantReadObject();
/*      */     }
/*      */ 
/*  807 */     java.lang.Object localObject2 = ORBUtility.makeClientDelegate(paramIOR);
/*  808 */     java.lang.Object localObject3 = null;
/*      */     try {
/*  810 */       localObject3 = paramStubFactory.makeStub();
/*      */     } catch (Throwable localThrowable) {
/*  812 */       localORBUtilSystemException.stubCreateError(localThrowable);
/*      */ 
/*  814 */       if ((localThrowable instanceof ThreadDeath)) {
/*  815 */         throw ((ThreadDeath)localThrowable);
/*      */       }
/*      */ 
/*  819 */       localObject3 = new CORBAObjectImpl();
/*      */     }
/*      */ 
/*  822 */     StubAdapter.setDelegate(localObject3, (Delegate)localObject2);
/*  823 */     return localObject3;
/*      */   }
/*      */ 
/*      */   public java.lang.Object read_abstract_interface()
/*      */   {
/*  828 */     return read_abstract_interface(null);
/*      */   }
/*      */ 
/*      */   public java.lang.Object read_abstract_interface(Class paramClass)
/*      */   {
/*  833 */     boolean bool = read_boolean();
/*      */ 
/*  835 */     if (bool) {
/*  836 */       return read_Object(paramClass);
/*      */     }
/*  838 */     return read_value();
/*      */   }
/*      */ 
/*      */   public Serializable read_value()
/*      */   {
/*  844 */     return read_value((Class)null);
/*      */   }
/*      */ 
/*      */   private Serializable handleIndirection() {
/*  848 */     int i = read_long() + get_offset() - 4;
/*  849 */     if ((this.valueCache != null) && (this.valueCache.containsVal(i)))
/*      */     {
/*  851 */       Serializable localSerializable = (Serializable)this.valueCache.getKey(i);
/*      */ 
/*  853 */       return localSerializable;
/*      */     }
/*      */ 
/*  859 */     throw new IndirectionException(i);
/*      */   }
/*      */ 
/*      */   private String readRepositoryIds(int paramInt, Class paramClass, String paramString)
/*      */   {
/*  866 */     return readRepositoryIds(paramInt, paramClass, paramString, null);
/*      */   }
/*      */ 
/*      */   private String readRepositoryIds(int paramInt, Class paramClass, String paramString, BoxedValueHelper paramBoxedValueHelper)
/*      */   {
/*  882 */     switch (this.repIdUtil.getTypeInfo(paramInt))
/*      */     {
/*      */     case 0:
/*  887 */       if (paramClass == null) {
/*  888 */         if (paramString != null)
/*  889 */           return paramString;
/*  890 */         if (paramBoxedValueHelper != null) {
/*  891 */           return paramBoxedValueHelper.get_id();
/*      */         }
/*  893 */         throw this.wrapper.expectedTypeNullAndNoRepId(CompletionStatus.COMPLETED_MAYBE);
/*      */       }
/*      */ 
/*  897 */       return this.repIdStrs.createForAnyType(paramClass);
/*      */     case 2:
/*  899 */       return read_repositoryId();
/*      */     case 6:
/*  901 */       return read_repositoryIds();
/*      */     }
/*  903 */     throw this.wrapper.badValueTag(CompletionStatus.COMPLETED_MAYBE, Integer.toHexString(paramInt));
/*      */   }
/*      */ 
/*      */   public Serializable read_value(Class paramClass)
/*      */   {
/*  911 */     int i = readValueTag();
/*      */ 
/*  914 */     if (i == 0) {
/*  915 */       return null;
/*      */     }
/*      */ 
/*  919 */     if (i == -1) {
/*  920 */       return handleIndirection();
/*      */     }
/*      */ 
/*  925 */     int j = get_offset() - 4;
/*      */ 
/*  929 */     boolean bool = this.isChunked;
/*      */ 
/*  931 */     this.isChunked = this.repIdUtil.isChunkedEncoding(i);
/*      */ 
/*  933 */     java.lang.Object localObject = null;
/*      */ 
/*  935 */     String str1 = null;
/*  936 */     if (this.repIdUtil.isCodeBasePresent(i)) {
/*  937 */       str1 = read_codebase_URL();
/*      */     }
/*      */ 
/*  941 */     String str2 = readRepositoryIds(i, paramClass, null);
/*      */ 
/*  946 */     start_block();
/*      */ 
/*  950 */     this.end_flag -= 1;
/*  951 */     if (this.isChunked) {
/*  952 */       this.chunkedValueNestingLevel -= 1;
/*      */     }
/*  954 */     if (str2.equals(this.repIdStrs.getWStringValueRepId())) {
/*  955 */       localObject = read_wstring();
/*      */     }
/*  957 */     else if (str2.equals(this.repIdStrs.getClassDescValueRepId()))
/*      */     {
/*  960 */       localObject = readClass();
/*      */     }
/*      */     else {
/*  963 */       Class localClass = paramClass;
/*      */ 
/*  967 */       if ((paramClass == null) || (!str2.equals(this.repIdStrs.createForAnyType(paramClass))))
/*      */       {
/*  970 */         localClass = getClassFromString(str2, str1, paramClass);
/*      */       }
/*      */ 
/*  975 */       if (localClass == null)
/*      */       {
/*  978 */         throw this.wrapper.couldNotFindClass(CompletionStatus.COMPLETED_MAYBE, new ClassNotFoundException());
/*      */       }
/*      */ 
/*  983 */       if ((localClass != null) && (IDLEntity.class.isAssignableFrom(localClass)))
/*      */       {
/*  986 */         localObject = readIDLValue(j, str2, localClass, str1);
/*      */       }
/*      */       else
/*      */       {
/*      */         try
/*      */         {
/*  996 */           if (this.valueHandler == null) {
/*  997 */             this.valueHandler = ORBUtility.createValueHandler();
/*      */           }
/*  999 */           localObject = this.valueHandler.readValue(this.parent, j, localClass, str2, getCodeBase());
/*      */         }
/*      */         catch (SystemException localSystemException)
/*      */         {
/* 1008 */           throw localSystemException;
/*      */         } catch (Exception localException) {
/* 1010 */           throw this.wrapper.valuehandlerReadException(CompletionStatus.COMPLETED_MAYBE, localException);
/*      */         }
/*      */         catch (Error localError) {
/* 1013 */           throw this.wrapper.valuehandlerReadError(CompletionStatus.COMPLETED_MAYBE, localError);
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1023 */     handleEndOfValue();
/*      */ 
/* 1028 */     readEndTag();
/*      */ 
/* 1031 */     if (this.valueCache == null)
/* 1032 */       this.valueCache = new CacheTable(this.orb, false);
/* 1033 */     this.valueCache.put(localObject, j);
/*      */ 
/* 1040 */     this.isChunked = bool;
/* 1041 */     start_block();
/*      */ 
/* 1043 */     return (Serializable)localObject;
/*      */   }
/*      */ 
/*      */   public Serializable read_value(BoxedValueHelper paramBoxedValueHelper)
/*      */   {
/* 1049 */     int i = readValueTag();
/*      */ 
/* 1051 */     if (i == 0)
/* 1052 */       return null;
/* 1053 */     if (i == -1) {
/* 1054 */       j = read_long() + get_offset() - 4;
/* 1055 */       if ((this.valueCache != null) && (this.valueCache.containsVal(j)))
/*      */       {
/* 1057 */         Serializable localSerializable = (Serializable)this.valueCache.getKey(j);
/*      */ 
/* 1059 */         return localSerializable;
/*      */       }
/*      */ 
/* 1062 */       throw new IndirectionException(j);
/*      */     }
/*      */ 
/* 1066 */     int j = get_offset() - 4;
/*      */ 
/* 1070 */     boolean bool = this.isChunked;
/* 1071 */     this.isChunked = this.repIdUtil.isChunkedEncoding(i);
/*      */ 
/* 1073 */     java.lang.Object localObject = null;
/*      */ 
/* 1075 */     String str1 = null;
/* 1076 */     if (this.repIdUtil.isCodeBasePresent(i)) {
/* 1077 */       str1 = read_codebase_URL();
/*      */     }
/*      */ 
/* 1081 */     String str2 = readRepositoryIds(i, null, null, paramBoxedValueHelper);
/*      */ 
/* 1085 */     if (!str2.equals(paramBoxedValueHelper.get_id())) {
/* 1086 */       paramBoxedValueHelper = Utility.getHelper(null, str1, str2);
/*      */     }
/* 1088 */     start_block();
/* 1089 */     this.end_flag -= 1;
/* 1090 */     if (this.isChunked) {
/* 1091 */       this.chunkedValueNestingLevel -= 1;
/*      */     }
/* 1093 */     if ((paramBoxedValueHelper instanceof ValueHelper)) {
/* 1094 */       localObject = readIDLValueWithHelper((ValueHelper)paramBoxedValueHelper, j);
/*      */     } else {
/* 1096 */       this.valueIndirection = j;
/* 1097 */       localObject = paramBoxedValueHelper.read_value(this.parent);
/*      */     }
/*      */ 
/* 1100 */     handleEndOfValue();
/* 1101 */     readEndTag();
/*      */ 
/* 1104 */     if (this.valueCache == null)
/* 1105 */       this.valueCache = new CacheTable(this.orb, false);
/* 1106 */     this.valueCache.put(localObject, j);
/*      */ 
/* 1109 */     this.isChunked = bool;
/* 1110 */     start_block();
/*      */ 
/* 1112 */     return (Serializable)localObject;
/*      */   }
/*      */ 
/*      */   private boolean isCustomType(ValueHelper paramValueHelper)
/*      */   {
/*      */     try {
/* 1118 */       TypeCode localTypeCode = paramValueHelper.get_type();
/* 1119 */       int i = localTypeCode.kind().value();
/* 1120 */       if (i == 29)
/* 1121 */         return localTypeCode.type_modifier() == 1;
/*      */     }
/*      */     catch (BadKind localBadKind) {
/* 1124 */       throw this.wrapper.badKind(localBadKind);
/*      */     }
/*      */ 
/* 1127 */     return false;
/*      */   }
/*      */ 
/*      */   public Serializable read_value(Serializable paramSerializable)
/*      */   {
/* 1137 */     if (this.valueCache == null)
/* 1138 */       this.valueCache = new CacheTable(this.orb, false);
/* 1139 */     this.valueCache.put(paramSerializable, this.valueIndirection);
/*      */ 
/* 1141 */     if ((paramSerializable instanceof StreamableValue))
/* 1142 */       ((StreamableValue)paramSerializable)._read(this.parent);
/* 1143 */     else if ((paramSerializable instanceof CustomValue)) {
/* 1144 */       ((CustomValue)paramSerializable).unmarshal(this.parent);
/*      */     }
/* 1146 */     return paramSerializable;
/*      */   }
/*      */ 
/*      */   public Serializable read_value(String paramString)
/*      */   {
/* 1155 */     int i = readValueTag();
/*      */ 
/* 1157 */     if (i == 0)
/* 1158 */       return null;
/* 1159 */     if (i == -1) {
/* 1160 */       j = read_long() + get_offset() - 4;
/* 1161 */       if ((this.valueCache != null) && (this.valueCache.containsVal(j)))
/*      */       {
/* 1163 */         Serializable localSerializable1 = (Serializable)this.valueCache.getKey(j);
/*      */ 
/* 1165 */         return localSerializable1;
/*      */       }
/*      */ 
/* 1168 */       throw new IndirectionException(j);
/*      */     }
/*      */ 
/* 1172 */     int j = get_offset() - 4;
/*      */ 
/* 1176 */     boolean bool = this.isChunked;
/* 1177 */     this.isChunked = this.repIdUtil.isChunkedEncoding(i);
/*      */ 
/* 1179 */     Serializable localSerializable2 = null;
/*      */ 
/* 1181 */     String str1 = null;
/* 1182 */     if (this.repIdUtil.isCodeBasePresent(i)) {
/* 1183 */       str1 = read_codebase_URL();
/*      */     }
/*      */ 
/* 1187 */     String str2 = readRepositoryIds(i, null, paramString);
/*      */ 
/* 1190 */     ValueFactory localValueFactory = Utility.getFactory(null, str1, this.orb, str2);
/*      */ 
/* 1193 */     start_block();
/* 1194 */     this.end_flag -= 1;
/* 1195 */     if (this.isChunked) {
/* 1196 */       this.chunkedValueNestingLevel -= 1;
/*      */     }
/* 1198 */     this.valueIndirection = j;
/* 1199 */     localSerializable2 = localValueFactory.read_value(this.parent);
/*      */ 
/* 1201 */     handleEndOfValue();
/* 1202 */     readEndTag();
/*      */ 
/* 1205 */     if (this.valueCache == null)
/* 1206 */       this.valueCache = new CacheTable(this.orb, false);
/* 1207 */     this.valueCache.put(localSerializable2, j);
/*      */ 
/* 1210 */     this.isChunked = bool;
/* 1211 */     start_block();
/*      */ 
/* 1213 */     return (Serializable)localSerializable2;
/*      */   }
/*      */ 
/*      */   private Class readClass()
/*      */   {
/* 1219 */     String str1 = null; String str2 = null;
/*      */ 
/* 1221 */     if ((this.orb == null) || (ORBVersionFactory.getFOREIGN().equals(this.orb.getORBVersion())) || (ORBVersionFactory.getNEWER().compareTo(this.orb.getORBVersion()) <= 0))
/*      */     {
/* 1225 */       str1 = (String)read_value(String.class);
/* 1226 */       str2 = (String)read_value(String.class);
/*      */     }
/*      */     else
/*      */     {
/* 1230 */       str2 = (String)read_value(String.class);
/* 1231 */       str1 = (String)read_value(String.class);
/*      */     }
/*      */ 
/* 1234 */     if (this.debug) {
/* 1235 */       dprint("readClass codebases: " + str1 + " rep Id: " + str2);
/*      */     }
/*      */ 
/* 1241 */     Class localClass = null;
/*      */ 
/* 1243 */     RepositoryIdInterface localRepositoryIdInterface = this.repIdStrs.getFromString(str2);
/*      */     try
/*      */     {
/* 1247 */       localClass = localRepositoryIdInterface.getClassFromType(str1);
/*      */     } catch (ClassNotFoundException localClassNotFoundException) {
/* 1249 */       throw this.wrapper.cnfeReadClass(CompletionStatus.COMPLETED_MAYBE, localClassNotFoundException, localRepositoryIdInterface.getClassName());
/*      */     }
/*      */     catch (MalformedURLException localMalformedURLException) {
/* 1252 */       throw this.wrapper.malformedUrl(CompletionStatus.COMPLETED_MAYBE, localMalformedURLException, localRepositoryIdInterface.getClassName(), str1);
/*      */     }
/*      */ 
/* 1256 */     return localClass;
/*      */   }
/*      */ 
/*      */   private java.lang.Object readIDLValueWithHelper(ValueHelper paramValueHelper, int paramInt)
/*      */   {
/*      */     Method localMethod;
/*      */     try
/*      */     {
/* 1264 */       Class[] arrayOfClass = { InputStream.class, paramValueHelper.get_class() };
/* 1265 */       localMethod = paramValueHelper.getClass().getDeclaredMethod("read", arrayOfClass);
/*      */     }
/*      */     catch (NoSuchMethodException localNoSuchMethodException) {
/* 1268 */       return paramValueHelper.read_value(this.parent);
/*      */     }
/*      */ 
/* 1274 */     java.lang.Object localObject = null;
/*      */     try {
/* 1276 */       localObject = paramValueHelper.get_class().newInstance();
/*      */     } catch (InstantiationException localInstantiationException) {
/* 1278 */       throw this.wrapper.couldNotInstantiateHelper(localInstantiationException, paramValueHelper.get_class());
/*      */     }
/*      */     catch (IllegalAccessException localIllegalAccessException1)
/*      */     {
/* 1287 */       return paramValueHelper.read_value(this.parent);
/*      */     }
/*      */ 
/* 1291 */     if (this.valueCache == null)
/* 1292 */       this.valueCache = new CacheTable(this.orb, false);
/* 1293 */     this.valueCache.put(localObject, paramInt);
/*      */ 
/* 1296 */     if (((localObject instanceof CustomMarshal)) && (isCustomType(paramValueHelper))) {
/* 1297 */       ((CustomMarshal)localObject).unmarshal(this.parent);
/* 1298 */       return localObject;
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1303 */       java.lang.Object[] arrayOfObject = { this.parent, localObject };
/* 1304 */       localMethod.invoke(paramValueHelper, arrayOfObject);
/* 1305 */       return localObject;
/*      */     } catch (IllegalAccessException localIllegalAccessException2) {
/* 1307 */       throw this.wrapper.couldNotInvokeHelperReadMethod(localIllegalAccessException2, paramValueHelper.get_class());
/*      */     } catch (InvocationTargetException localInvocationTargetException) {
/* 1309 */       throw this.wrapper.couldNotInvokeHelperReadMethod(localInvocationTargetException, paramValueHelper.get_class());
/*      */     }
/*      */   }
/*      */ 
/*      */   private java.lang.Object readBoxedIDLEntity(Class paramClass, String paramString)
/*      */   {
/* 1315 */     Class localClass1 = null;
/*      */     try
/*      */     {
/* 1318 */       ClassLoader localClassLoader = paramClass == null ? null : paramClass.getClassLoader();
/*      */ 
/* 1320 */       localClass1 = Utility.loadClassForClass(paramClass.getName() + "Helper", paramString, localClassLoader, paramClass, localClassLoader);
/*      */ 
/* 1322 */       final Class localClass2 = localClass1;
/*      */ 
/* 1324 */       final Class[] arrayOfClass = { InputStream.class };
/*      */ 
/* 1328 */       Method localMethod = null;
/*      */       try {
/* 1330 */         localMethod = (Method)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */         {
/*      */           public java.lang.Object run() throws NoSuchMethodException {
/* 1333 */             return localClass2.getDeclaredMethod("read", arrayOfClass);
/*      */           }
/*      */         });
/*      */       }
/*      */       catch (PrivilegedActionException localPrivilegedActionException)
/*      */       {
/* 1339 */         throw ((NoSuchMethodException)localPrivilegedActionException.getException());
/*      */       }
/*      */ 
/* 1342 */       java.lang.Object[] arrayOfObject = { this.parent };
/* 1343 */       return localMethod.invoke(null, arrayOfObject);
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException) {
/* 1346 */       throw this.wrapper.couldNotInvokeHelperReadMethod(localClassNotFoundException, localClass1);
/*      */     } catch (NoSuchMethodException localNoSuchMethodException) {
/* 1348 */       throw this.wrapper.couldNotInvokeHelperReadMethod(localNoSuchMethodException, localClass1);
/*      */     } catch (IllegalAccessException localIllegalAccessException) {
/* 1350 */       throw this.wrapper.couldNotInvokeHelperReadMethod(localIllegalAccessException, localClass1);
/*      */     } catch (InvocationTargetException localInvocationTargetException) {
/* 1352 */       throw this.wrapper.couldNotInvokeHelperReadMethod(localInvocationTargetException, localClass1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private java.lang.Object readIDLValue(int paramInt, String paramString1, Class paramClass, String paramString2)
/*      */   {
/*      */     ValueFactory localValueFactory;
/*      */     try
/*      */     {
/* 1371 */       localValueFactory = Utility.getFactory(paramClass, paramString2, this.orb, paramString1);
/*      */     }
/*      */     catch (MARSHAL localMARSHAL)
/*      */     {
/* 1376 */       if ((!StreamableValue.class.isAssignableFrom(paramClass)) && (!CustomValue.class.isAssignableFrom(paramClass)) && (ValueBase.class.isAssignableFrom(paramClass)))
/*      */       {
/* 1380 */         BoxedValueHelper localBoxedValueHelper = Utility.getHelper(paramClass, paramString2, paramString1);
/* 1381 */         if ((localBoxedValueHelper instanceof ValueHelper)) {
/* 1382 */           return readIDLValueWithHelper((ValueHelper)localBoxedValueHelper, paramInt);
/*      */         }
/* 1384 */         return localBoxedValueHelper.read_value(this.parent);
/*      */       }
/*      */ 
/* 1388 */       return readBoxedIDLEntity(paramClass, paramString2);
/*      */     }
/*      */ 
/* 1393 */     this.valueIndirection = paramInt;
/* 1394 */     return localValueFactory.read_value(this.parent);
/*      */   }
/*      */ 
/*      */   private void readEndTag()
/*      */   {
/* 1408 */     if (this.isChunked)
/*      */     {
/* 1411 */       int i = read_long();
/*      */ 
/* 1418 */       if (i >= 0) {
/* 1419 */         throw this.wrapper.positiveEndTag(CompletionStatus.COMPLETED_MAYBE, new Integer(i), new Integer(get_offset() - 4));
/*      */       }
/*      */ 
/* 1427 */       if ((this.orb == null) || (ORBVersionFactory.getFOREIGN().equals(this.orb.getORBVersion())) || (ORBVersionFactory.getNEWER().compareTo(this.orb.getORBVersion()) <= 0))
/*      */       {
/* 1434 */         if (i < this.chunkedValueNestingLevel) {
/* 1435 */           throw this.wrapper.unexpectedEnclosingValuetype(CompletionStatus.COMPLETED_MAYBE, new Integer(i), new Integer(this.chunkedValueNestingLevel));
/*      */         }
/*      */ 
/* 1445 */         if (i != this.chunkedValueNestingLevel) {
/* 1446 */           this.bbwi.position(this.bbwi.position() - 4);
/*      */         }
/*      */ 
/*      */       }
/* 1455 */       else if (i != this.end_flag) {
/* 1456 */         this.bbwi.position(this.bbwi.position() - 4);
/*      */       }
/*      */ 
/* 1462 */       this.chunkedValueNestingLevel += 1;
/*      */     }
/*      */ 
/* 1466 */     this.end_flag += 1;
/*      */   }
/*      */ 
/*      */   protected int get_offset() {
/* 1470 */     return this.bbwi.position();
/*      */   }
/*      */ 
/*      */   private void start_block()
/*      */   {
/* 1476 */     if (!this.isChunked) {
/* 1477 */       return;
/*      */     }
/*      */ 
/* 1481 */     this.blockLength = 2147483392;
/*      */ 
/* 1483 */     this.blockLength = read_long();
/*      */ 
/* 1488 */     if ((this.blockLength > 0) && (this.blockLength < 2147483392)) {
/* 1489 */       this.blockLength += get_offset();
/*      */     }
/*      */     else
/*      */     {
/* 1497 */       this.blockLength = 2147483392;
/*      */ 
/* 1499 */       this.bbwi.position(this.bbwi.position() - 4);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void handleEndOfValue()
/*      */   {
/* 1513 */     if (!this.isChunked) {
/* 1514 */       return;
/*      */     }
/*      */ 
/* 1517 */     while (this.blockLength != 2147483392) {
/* 1518 */       end_block();
/* 1519 */       start_block();
/*      */     }
/*      */ 
/* 1529 */     int i = read_long();
/* 1530 */     this.bbwi.position(this.bbwi.position() - 4);
/*      */ 
/* 1537 */     if (i < 0) {
/* 1538 */       return;
/*      */     }
/* 1540 */     if ((i == 0) || (i >= 2147483392))
/*      */     {
/* 1553 */       read_value();
/* 1554 */       handleEndOfValue();
/*      */     }
/*      */     else
/*      */     {
/* 1560 */       throw this.wrapper.couldNotSkipBytes(CompletionStatus.COMPLETED_MAYBE, new Integer(i), new Integer(get_offset()));
/*      */     }
/*      */   }
/*      */ 
/*      */   private void end_block()
/*      */   {
/* 1568 */     if (this.blockLength != 2147483392)
/* 1569 */       if (this.blockLength == get_offset())
/*      */       {
/* 1571 */         this.blockLength = 2147483392;
/*      */       }
/* 1575 */       else if (this.blockLength > get_offset())
/* 1576 */         skipToOffset(this.blockLength);
/*      */       else
/* 1578 */         throw this.wrapper.badChunkLength(new Integer(this.blockLength), new Integer(get_offset()));
/*      */   }
/*      */ 
/*      */   private int readValueTag()
/*      */   {
/* 1587 */     return read_long();
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.ORB orb() {
/* 1591 */     return this.orb;
/*      */   }
/*      */ 
/*      */   public final void read_boolean_array(boolean[] paramArrayOfBoolean, int paramInt1, int paramInt2)
/*      */   {
/* 1597 */     for (int i = 0; i < paramInt2; i++)
/* 1598 */       paramArrayOfBoolean[(i + paramInt1)] = read_boolean();
/*      */   }
/*      */ 
/*      */   public final void read_char_array(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/* 1603 */     for (int i = 0; i < paramInt2; i++)
/* 1604 */       paramArrayOfChar[(i + paramInt1)] = read_char();
/*      */   }
/*      */ 
/*      */   public final void read_wchar_array(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/* 1609 */     for (int i = 0; i < paramInt2; i++)
/* 1610 */       paramArrayOfChar[(i + paramInt1)] = read_wchar();
/*      */   }
/*      */ 
/*      */   public final void read_short_array(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*      */   {
/* 1615 */     for (int i = 0; i < paramInt2; i++)
/* 1616 */       paramArrayOfShort[(i + paramInt1)] = read_short();
/*      */   }
/*      */ 
/*      */   public final void read_ushort_array(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*      */   {
/* 1621 */     read_short_array(paramArrayOfShort, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public final void read_long_array(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
/* 1625 */     for (int i = 0; i < paramInt2; i++)
/* 1626 */       paramArrayOfInt[(i + paramInt1)] = read_long();
/*      */   }
/*      */ 
/*      */   public final void read_ulong_array(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*      */   {
/* 1631 */     read_long_array(paramArrayOfInt, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public final void read_longlong_array(long[] paramArrayOfLong, int paramInt1, int paramInt2) {
/* 1635 */     for (int i = 0; i < paramInt2; i++)
/* 1636 */       paramArrayOfLong[(i + paramInt1)] = read_longlong();
/*      */   }
/*      */ 
/*      */   public final void read_ulonglong_array(long[] paramArrayOfLong, int paramInt1, int paramInt2)
/*      */   {
/* 1641 */     read_longlong_array(paramArrayOfLong, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public final void read_float_array(float[] paramArrayOfFloat, int paramInt1, int paramInt2) {
/* 1645 */     for (int i = 0; i < paramInt2; i++)
/* 1646 */       paramArrayOfFloat[(i + paramInt1)] = read_float();
/*      */   }
/*      */ 
/*      */   public final void read_double_array(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
/*      */   {
/* 1651 */     for (int i = 0; i < paramInt2; i++)
/* 1652 */       paramArrayOfDouble[(i + paramInt1)] = read_double();
/*      */   }
/*      */ 
/*      */   public final void read_any_array(Any[] paramArrayOfAny, int paramInt1, int paramInt2)
/*      */   {
/* 1657 */     for (int i = 0; i < paramInt2; i++)
/* 1658 */       paramArrayOfAny[(i + paramInt1)] = read_any();
/*      */   }
/*      */ 
/*      */   private String read_repositoryIds()
/*      */   {
/* 1680 */     int i = read_long();
/* 1681 */     if (i == -1) {
/* 1682 */       j = read_long() + get_offset() - 4;
/* 1683 */       if ((this.repositoryIdCache != null) && (this.repositoryIdCache.containsOrderedVal(j))) {
/* 1684 */         return (String)this.repositoryIdCache.getKey(j);
/*      */       }
/* 1686 */       throw this.wrapper.unableToLocateRepIdArray(new Integer(j));
/*      */     }
/*      */ 
/* 1690 */     int j = get_offset();
/* 1691 */     String str = read_repositoryId();
/* 1692 */     if (this.repositoryIdCache == null)
/* 1693 */       this.repositoryIdCache = new CacheTable(this.orb, false);
/* 1694 */     this.repositoryIdCache.put(str, j);
/*      */ 
/* 1698 */     for (int k = 1; k < i; k++) {
/* 1699 */       read_repositoryId();
/*      */     }
/*      */ 
/* 1702 */     return str;
/*      */   }
/*      */ 
/*      */   private final String read_repositoryId()
/*      */   {
/* 1708 */     String str = readStringOrIndirection(true);
/*      */ 
/* 1710 */     if (str == null) {
/* 1711 */       int i = read_long() + get_offset() - 4;
/*      */ 
/* 1713 */       if ((this.repositoryIdCache != null) && (this.repositoryIdCache.containsOrderedVal(i))) {
/* 1714 */         return (String)this.repositoryIdCache.getKey(i);
/*      */       }
/* 1716 */       throw this.wrapper.badRepIdIndirection(CompletionStatus.COMPLETED_MAYBE, new Integer(this.bbwi.position()));
/*      */     }
/*      */ 
/* 1719 */     if (this.repositoryIdCache == null)
/* 1720 */       this.repositoryIdCache = new CacheTable(this.orb, false);
/* 1721 */     this.repositoryIdCache.put(str, this.stringIndirection);
/*      */ 
/* 1724 */     return str;
/*      */   }
/*      */ 
/*      */   private final String read_codebase_URL()
/*      */   {
/* 1729 */     String str = readStringOrIndirection(true);
/*      */ 
/* 1731 */     if (str == null) {
/* 1732 */       int i = read_long() + get_offset() - 4;
/*      */ 
/* 1734 */       if ((this.codebaseCache != null) && (this.codebaseCache.containsVal(i))) {
/* 1735 */         return (String)this.codebaseCache.getKey(i);
/*      */       }
/* 1737 */       throw this.wrapper.badCodebaseIndirection(CompletionStatus.COMPLETED_MAYBE, new Integer(this.bbwi.position()));
/*      */     }
/*      */ 
/* 1741 */     if (this.codebaseCache == null)
/* 1742 */       this.codebaseCache = new CacheTable(this.orb, false);
/* 1743 */     this.codebaseCache.put(str, this.stringIndirection);
/*      */ 
/* 1746 */     return str;
/*      */   }
/*      */ 
/*      */   public java.lang.Object read_Abstract()
/*      */   {
/* 1752 */     return read_abstract_interface();
/*      */   }
/*      */ 
/*      */   public Serializable read_Value() {
/* 1756 */     return read_value();
/*      */   }
/*      */ 
/*      */   public void read_any_array(AnySeqHolder paramAnySeqHolder, int paramInt1, int paramInt2) {
/* 1760 */     read_any_array(paramAnySeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_boolean_array(BooleanSeqHolder paramBooleanSeqHolder, int paramInt1, int paramInt2) {
/* 1764 */     read_boolean_array(paramBooleanSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_char_array(CharSeqHolder paramCharSeqHolder, int paramInt1, int paramInt2) {
/* 1768 */     read_char_array(paramCharSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_wchar_array(WCharSeqHolder paramWCharSeqHolder, int paramInt1, int paramInt2) {
/* 1772 */     read_wchar_array(paramWCharSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_octet_array(OctetSeqHolder paramOctetSeqHolder, int paramInt1, int paramInt2) {
/* 1776 */     read_octet_array(paramOctetSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_short_array(ShortSeqHolder paramShortSeqHolder, int paramInt1, int paramInt2) {
/* 1780 */     read_short_array(paramShortSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_ushort_array(UShortSeqHolder paramUShortSeqHolder, int paramInt1, int paramInt2) {
/* 1784 */     read_ushort_array(paramUShortSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_long_array(LongSeqHolder paramLongSeqHolder, int paramInt1, int paramInt2) {
/* 1788 */     read_long_array(paramLongSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_ulong_array(ULongSeqHolder paramULongSeqHolder, int paramInt1, int paramInt2) {
/* 1792 */     read_ulong_array(paramULongSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_ulonglong_array(ULongLongSeqHolder paramULongLongSeqHolder, int paramInt1, int paramInt2) {
/* 1796 */     read_ulonglong_array(paramULongLongSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_longlong_array(LongLongSeqHolder paramLongLongSeqHolder, int paramInt1, int paramInt2) {
/* 1800 */     read_longlong_array(paramLongLongSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_float_array(FloatSeqHolder paramFloatSeqHolder, int paramInt1, int paramInt2) {
/* 1804 */     read_float_array(paramFloatSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_double_array(DoubleSeqHolder paramDoubleSeqHolder, int paramInt1, int paramInt2) {
/* 1808 */     read_double_array(paramDoubleSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public BigDecimal read_fixed(short paramShort1, short paramShort2)
/*      */   {
/* 1813 */     StringBuffer localStringBuffer = read_fixed_buffer();
/* 1814 */     if (paramShort1 != localStringBuffer.length()) {
/* 1815 */       throw this.wrapper.badFixed(new Integer(paramShort1), new Integer(localStringBuffer.length()));
/*      */     }
/* 1817 */     localStringBuffer.insert(paramShort1 - paramShort2, '.');
/* 1818 */     return new BigDecimal(localStringBuffer.toString());
/*      */   }
/*      */ 
/*      */   public BigDecimal read_fixed()
/*      */   {
/* 1823 */     return new BigDecimal(read_fixed_buffer().toString());
/*      */   }
/*      */ 
/*      */   private StringBuffer read_fixed_buffer()
/*      */   {
/* 1834 */     StringBuffer localStringBuffer = new StringBuffer(64);
/*      */ 
/* 1838 */     int m = 0;
/* 1839 */     int n = 1;
/* 1840 */     while (n != 0) {
/* 1841 */       int i = read_octet();
/* 1842 */       int j = (i & 0xF0) >> 4;
/* 1843 */       int k = i & 0xF;
/* 1844 */       if ((m != 0) || (j != 0)) {
/* 1845 */         localStringBuffer.append(Character.forDigit(j, 10));
/* 1846 */         m = 1;
/*      */       }
/* 1848 */       if (k == 12)
/*      */       {
/* 1850 */         if (m == 0)
/*      */         {
/* 1852 */           return new StringBuffer("0.0");
/*      */         }
/*      */ 
/* 1857 */         n = 0;
/* 1858 */       } else if (k == 13)
/*      */       {
/* 1860 */         localStringBuffer.insert(0, '-');
/* 1861 */         n = 0;
/*      */       } else {
/* 1863 */         localStringBuffer.append(Character.forDigit(k, 10));
/* 1864 */         m = 1;
/*      */       }
/*      */     }
/* 1867 */     return localStringBuffer;
/*      */   }
/*      */ 
/*      */   public String[] _truncatable_ids()
/*      */   {
/* 1874 */     if (_ids == null) {
/* 1875 */       return null;
/*      */     }
/* 1877 */     return (String[])_ids.clone();
/*      */   }
/*      */ 
/*      */   public void printBuffer()
/*      */   {
/* 1883 */     printBuffer(this.bbwi);
/*      */   }
/*      */ 
/*      */   public static void printBuffer(ByteBufferWithInfo paramByteBufferWithInfo)
/*      */   {
/* 1888 */     System.out.println("----- Input Buffer -----");
/* 1889 */     System.out.println();
/* 1890 */     System.out.println("Current position: " + paramByteBufferWithInfo.position());
/* 1891 */     System.out.println("Total length : " + paramByteBufferWithInfo.buflen);
/* 1892 */     System.out.println();
/*      */     try
/*      */     {
/* 1896 */       char[] arrayOfChar = new char[16];
/*      */ 
/* 1898 */       for (int i = 0; i < paramByteBufferWithInfo.buflen; i += 16)
/*      */       {
/* 1900 */         int j = 0;
/*      */ 
/* 1906 */         while ((j < 16) && (j + i < paramByteBufferWithInfo.buflen)) {
/* 1907 */           k = paramByteBufferWithInfo.byteBuffer.get(i + j);
/* 1908 */           if (k < 0)
/* 1909 */             k = 256 + k;
/* 1910 */           String str = Integer.toHexString(k);
/* 1911 */           if (str.length() == 1)
/* 1912 */             str = "0" + str;
/* 1913 */           System.out.print(str + " ");
/* 1914 */           j++;
/*      */         }
/*      */ 
/* 1920 */         while (j < 16) {
/* 1921 */           System.out.print("   ");
/* 1922 */           j++;
/*      */         }
/*      */ 
/* 1927 */         int k = 0;
/* 1928 */         while ((k < 16) && (k + i < paramByteBufferWithInfo.buflen)) {
/* 1929 */           if (ORBUtility.isPrintable((char)paramByteBufferWithInfo.byteBuffer.get(i + k)))
/* 1930 */             arrayOfChar[k] = ((char)paramByteBufferWithInfo.byteBuffer.get(i + k));
/*      */           else
/* 1932 */             arrayOfChar[k] = '.';
/* 1933 */           k++;
/*      */         }
/* 1935 */         System.out.println(new String(arrayOfChar, 0, k));
/*      */       }
/*      */     }
/*      */     catch (Throwable localThrowable) {
/* 1939 */       localThrowable.printStackTrace();
/*      */     }
/*      */ 
/* 1942 */     System.out.println("------------------------");
/*      */   }
/*      */ 
/*      */   public ByteBuffer getByteBuffer() {
/* 1946 */     ByteBuffer localByteBuffer = null;
/* 1947 */     if (this.bbwi != null) {
/* 1948 */       localByteBuffer = this.bbwi.byteBuffer;
/*      */     }
/* 1950 */     return localByteBuffer;
/*      */   }
/*      */ 
/*      */   public int getBufferLength() {
/* 1954 */     return this.bbwi.buflen;
/*      */   }
/*      */ 
/*      */   public void setBufferLength(int paramInt) {
/* 1958 */     this.bbwi.buflen = paramInt;
/* 1959 */     this.bbwi.byteBuffer.limit(this.bbwi.buflen);
/*      */   }
/*      */ 
/*      */   public void setByteBufferWithInfo(ByteBufferWithInfo paramByteBufferWithInfo) {
/* 1963 */     this.bbwi = paramByteBufferWithInfo;
/*      */   }
/*      */ 
/*      */   public void setByteBuffer(ByteBuffer paramByteBuffer) {
/* 1967 */     this.bbwi.byteBuffer = paramByteBuffer;
/*      */   }
/*      */ 
/*      */   public int getIndex() {
/* 1971 */     return this.bbwi.position();
/*      */   }
/*      */ 
/*      */   public void setIndex(int paramInt) {
/* 1975 */     this.bbwi.position(paramInt);
/*      */   }
/*      */ 
/*      */   public boolean isLittleEndian() {
/* 1979 */     return this.littleEndian;
/*      */   }
/*      */ 
/*      */   public void orb(org.omg.CORBA.ORB paramORB) {
/* 1983 */     this.orb = ((com.sun.corba.se.spi.orb.ORB)paramORB);
/*      */   }
/*      */ 
/*      */   public BufferManagerRead getBufferManager() {
/* 1987 */     return this.bufferManagerRead;
/*      */   }
/*      */ 
/*      */   private void skipToOffset(int paramInt)
/*      */   {
/* 1993 */     int i = paramInt - get_offset();
/*      */ 
/* 1995 */     int j = 0;
/*      */ 
/* 1997 */     while (j < i)
/*      */     {
/* 2002 */       int k = this.bbwi.buflen - this.bbwi.position();
/* 2003 */       if (k <= 0) {
/* 2004 */         grow(1, 1);
/* 2005 */         k = this.bbwi.buflen - this.bbwi.position();
/*      */       }
/*      */ 
/* 2008 */       int n = i - j;
/* 2009 */       int m = n < k ? n : k;
/* 2010 */       this.bbwi.position(this.bbwi.position() + m);
/* 2011 */       j += m;
/*      */     }
/*      */   }
/*      */ 
/*      */   public java.lang.Object createStreamMemento()
/*      */   {
/* 2049 */     return new StreamMemento();
/*      */   }
/*      */ 
/*      */   public void restoreInternalState(java.lang.Object paramObject)
/*      */   {
/* 2054 */     StreamMemento localStreamMemento = (StreamMemento)paramObject;
/*      */ 
/* 2056 */     this.blockLength = localStreamMemento.blockLength_;
/* 2057 */     this.end_flag = localStreamMemento.end_flag_;
/* 2058 */     this.chunkedValueNestingLevel = localStreamMemento.chunkedValueNestingLevel_;
/* 2059 */     this.valueIndirection = localStreamMemento.valueIndirection_;
/* 2060 */     this.stringIndirection = localStreamMemento.stringIndirection_;
/* 2061 */     this.isChunked = localStreamMemento.isChunked_;
/* 2062 */     this.valueHandler = localStreamMemento.valueHandler_;
/* 2063 */     this.specialNoOptionalDataState = localStreamMemento.specialNoOptionalDataState_;
/* 2064 */     this.bbwi = localStreamMemento.bbwi_;
/*      */   }
/*      */ 
/*      */   public int getPosition() {
/* 2068 */     return get_offset();
/*      */   }
/*      */ 
/*      */   public void mark(int paramInt) {
/* 2072 */     this.markAndResetHandler.mark(this);
/*      */   }
/*      */ 
/*      */   public void reset() {
/* 2076 */     this.markAndResetHandler.reset();
/*      */   }
/*      */ 
/*      */   CodeBase getCodeBase()
/*      */   {
/* 2086 */     return this.parent.getCodeBase();
/*      */   }
/*      */ 
/*      */   private Class getClassFromString(String paramString1, String paramString2, Class paramClass)
/*      */   {
/* 2100 */     RepositoryIdInterface localRepositoryIdInterface = this.repIdStrs.getFromString(paramString1);
/*      */     try
/*      */     {
/* 2107 */       return localRepositoryIdInterface.getClassFromType(paramClass, paramString2);
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException1)
/*      */     {
/*      */       try
/*      */       {
/* 2113 */         if (getCodeBase() == null) {
/* 2114 */           return null;
/*      */         }
/*      */ 
/* 2118 */         paramString2 = getCodeBase().implementation(paramString1);
/*      */ 
/* 2122 */         if (paramString2 == null) {
/* 2123 */           return null;
/*      */         }
/* 2125 */         return localRepositoryIdInterface.getClassFromType(paramClass, paramString2);
/*      */       }
/*      */       catch (ClassNotFoundException localClassNotFoundException2) {
/* 2128 */         dprintThrowable(localClassNotFoundException2);
/*      */ 
/* 2130 */         return null;
/*      */       }
/*      */     }
/*      */     catch (MalformedURLException localMalformedURLException)
/*      */     {
/* 2135 */       throw this.wrapper.malformedUrl(CompletionStatus.COMPLETED_MAYBE, localMalformedURLException, paramString1, paramString2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private Class getClassFromString(String paramString1, String paramString2)
/*      */   {
/* 2149 */     RepositoryIdInterface localRepositoryIdInterface = this.repIdStrs.getFromString(paramString1);
/*      */ 
/* 2152 */     for (int i = 0; i < 3; i++)
/*      */     {
/*      */       try
/*      */       {
/* 2156 */         switch (i)
/*      */         {
/*      */         case 0:
/* 2160 */           return localRepositoryIdInterface.getClassFromType();
/*      */         case 1:
/* 2164 */           break;
/*      */         case 2:
/* 2168 */           paramString2 = getCodeBase().implementation(paramString1);
/*      */         }
/*      */ 
/* 2173 */         if (paramString2 != null)
/*      */         {
/* 2176 */           return localRepositoryIdInterface.getClassFromType(paramString2);
/*      */         }
/*      */       }
/*      */       catch (ClassNotFoundException localClassNotFoundException) {
/*      */       }
/*      */       catch (MalformedURLException localMalformedURLException) {
/* 2182 */         throw this.wrapper.malformedUrl(CompletionStatus.COMPLETED_MAYBE, localMalformedURLException, paramString1, paramString2);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2188 */     dprint("getClassFromString failed with rep id " + paramString1 + " and codebase " + paramString2);
/*      */ 
/* 2193 */     return null;
/*      */   }
/*      */ 
/*      */   char[] getConvertedChars(int paramInt, CodeSetConversion.BTCConverter paramBTCConverter)
/*      */   {
/* 2205 */     if (this.bbwi.buflen - this.bbwi.position() >= paramInt)
/*      */     {
/* 2210 */       if (this.bbwi.byteBuffer.hasArray())
/*      */       {
/* 2212 */         arrayOfByte = this.bbwi.byteBuffer.array();
/*      */       }
/*      */       else
/*      */       {
/* 2216 */         arrayOfByte = new byte[this.bbwi.buflen];
/*      */ 
/* 2219 */         for (int i = 0; i < this.bbwi.buflen; i++)
/* 2220 */           arrayOfByte[i] = this.bbwi.byteBuffer.get(i);
/*      */       }
/* 2222 */       char[] arrayOfChar = paramBTCConverter.getChars(arrayOfByte, this.bbwi.position(), paramInt);
/*      */ 
/* 2224 */       this.bbwi.position(this.bbwi.position() + paramInt);
/* 2225 */       return arrayOfChar;
/*      */     }
/*      */ 
/* 2230 */     byte[] arrayOfByte = new byte[paramInt];
/* 2231 */     read_octet_array(arrayOfByte, 0, arrayOfByte.length);
/*      */ 
/* 2233 */     return paramBTCConverter.getChars(arrayOfByte, 0, paramInt);
/*      */   }
/*      */ 
/*      */   protected CodeSetConversion.BTCConverter getCharConverter()
/*      */   {
/* 2238 */     if (this.charConverter == null) {
/* 2239 */       this.charConverter = this.parent.createCharBTCConverter();
/*      */     }
/* 2241 */     return this.charConverter;
/*      */   }
/*      */ 
/*      */   protected CodeSetConversion.BTCConverter getWCharConverter() {
/* 2245 */     if (this.wcharConverter == null) {
/* 2246 */       this.wcharConverter = this.parent.createWCharBTCConverter();
/*      */     }
/* 2248 */     return this.wcharConverter;
/*      */   }
/*      */ 
/*      */   protected void dprintThrowable(Throwable paramThrowable) {
/* 2252 */     if ((this.debug) && (paramThrowable != null))
/* 2253 */       paramThrowable.printStackTrace();
/*      */   }
/*      */ 
/*      */   protected void dprint(String paramString) {
/* 2257 */     if (this.debug)
/* 2258 */       ORBUtility.dprint(this, paramString);
/*      */   }
/*      */ 
/*      */   void alignOnBoundary(int paramInt)
/*      */   {
/* 2270 */     int i = computeAlignment(this.bbwi.position(), paramInt);
/*      */ 
/* 2272 */     if (this.bbwi.position() + i <= this.bbwi.buflen)
/*      */     {
/* 2274 */       this.bbwi.position(this.bbwi.position() + i);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void resetCodeSetConverters() {
/* 2279 */     this.charConverter = null;
/* 2280 */     this.wcharConverter = null;
/*      */   }
/*      */ 
/*      */   public void start_value()
/*      */   {
/* 2285 */     int i = readValueTag();
/*      */ 
/* 2287 */     if (i == 0)
/*      */     {
/* 2295 */       this.specialNoOptionalDataState = true;
/*      */ 
/* 2297 */       return;
/*      */     }
/*      */ 
/* 2300 */     if (i == -1)
/*      */     {
/* 2302 */       throw this.wrapper.customWrapperIndirection(CompletionStatus.COMPLETED_MAYBE);
/*      */     }
/*      */ 
/* 2306 */     if (this.repIdUtil.isCodeBasePresent(i)) {
/* 2307 */       throw this.wrapper.customWrapperWithCodebase(CompletionStatus.COMPLETED_MAYBE);
/*      */     }
/*      */ 
/* 2311 */     if (this.repIdUtil.getTypeInfo(i) != 2)
/*      */     {
/* 2313 */       throw this.wrapper.customWrapperNotSingleRepid(CompletionStatus.COMPLETED_MAYBE);
/*      */     }
/*      */ 
/* 2320 */     read_repositoryId();
/*      */ 
/* 2326 */     start_block();
/* 2327 */     this.end_flag -= 1;
/* 2328 */     this.chunkedValueNestingLevel -= 1;
/*      */   }
/*      */ 
/*      */   public void end_value()
/*      */   {
/* 2333 */     if (this.specialNoOptionalDataState) {
/* 2334 */       this.specialNoOptionalDataState = false;
/* 2335 */       return;
/*      */     }
/*      */ 
/* 2338 */     handleEndOfValue();
/* 2339 */     readEndTag();
/*      */ 
/* 2347 */     start_block();
/*      */   }
/*      */ 
/*      */   public void close()
/*      */     throws IOException
/*      */   {
/* 2354 */     getBufferManager().close(this.bbwi);
/*      */ 
/* 2362 */     if ((this.bbwi != null) && (getByteBuffer() != null))
/*      */     {
/* 2364 */       MessageMediator localMessageMediator = this.parent.getMessageMediator();
/* 2365 */       if (localMessageMediator != null)
/*      */       {
/* 2367 */         localObject = (CDROutputObject)localMessageMediator.getOutputObject();
/*      */ 
/* 2369 */         if (localObject != null)
/*      */         {
/* 2371 */           if (((CDROutputObject)localObject).isSharing(getByteBuffer()))
/*      */           {
/* 2375 */             ((CDROutputObject)localObject).setByteBuffer(null);
/* 2376 */             ((CDROutputObject)localObject).setByteBufferWithInfo(null);
/*      */           }
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 2382 */       java.lang.Object localObject = this.orb.getByteBufferPool();
/* 2383 */       if (this.debug)
/*      */       {
/* 2386 */         int i = System.identityHashCode(this.bbwi.byteBuffer);
/* 2387 */         StringBuffer localStringBuffer = new StringBuffer(80);
/* 2388 */         localStringBuffer.append(".close - releasing ByteBuffer id (");
/* 2389 */         localStringBuffer.append(i).append(") to ByteBufferPool.");
/* 2390 */         String str = localStringBuffer.toString();
/* 2391 */         dprint(str);
/*      */       }
/* 2393 */       ((ByteBufferPool)localObject).releaseByteBuffer(this.bbwi.byteBuffer);
/* 2394 */       this.bbwi.byteBuffer = null;
/* 2395 */       this.bbwi = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected class StreamMemento
/*      */   {
/*      */     private int blockLength_;
/*      */     private int end_flag_;
/*      */     private int chunkedValueNestingLevel_;
/*      */     private int valueIndirection_;
/*      */     private int stringIndirection_;
/*      */     private boolean isChunked_;
/*      */     private ValueHandler valueHandler_;
/*      */     private ByteBufferWithInfo bbwi_;
/*      */     private boolean specialNoOptionalDataState_;
/*      */ 
/*      */     public StreamMemento()
/*      */     {
/* 2036 */       this.blockLength_ = CDRInputStream_1_0.this.blockLength;
/* 2037 */       this.end_flag_ = CDRInputStream_1_0.this.end_flag;
/* 2038 */       this.chunkedValueNestingLevel_ = CDRInputStream_1_0.this.chunkedValueNestingLevel;
/* 2039 */       this.valueIndirection_ = CDRInputStream_1_0.this.valueIndirection;
/* 2040 */       this.stringIndirection_ = CDRInputStream_1_0.this.stringIndirection;
/* 2041 */       this.isChunked_ = CDRInputStream_1_0.this.isChunked;
/* 2042 */       this.valueHandler_ = CDRInputStream_1_0.this.valueHandler;
/* 2043 */       this.specialNoOptionalDataState_ = CDRInputStream_1_0.this.specialNoOptionalDataState;
/* 2044 */       this.bbwi_ = new ByteBufferWithInfo(CDRInputStream_1_0.this.bbwi);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.CDRInputStream_1_0
 * JD-Core Version:    0.6.2
 */