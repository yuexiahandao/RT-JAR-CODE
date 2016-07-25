/*     */ package com.sun.corba.se.impl.encoding;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.pept.protocol.MessageMediator;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*     */ import com.sun.org.omg.SendingContext.CodeBase;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.math.BigDecimal;
/*     */ import java.nio.ByteBuffer;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.AnySeqHolder;
/*     */ import org.omg.CORBA.BooleanSeqHolder;
/*     */ import org.omg.CORBA.CharSeqHolder;
/*     */ import org.omg.CORBA.Context;
/*     */ import org.omg.CORBA.DataInputStream;
/*     */ import org.omg.CORBA.DoubleSeqHolder;
/*     */ import org.omg.CORBA.FloatSeqHolder;
/*     */ import org.omg.CORBA.LongLongSeqHolder;
/*     */ import org.omg.CORBA.LongSeqHolder;
/*     */ import org.omg.CORBA.OctetSeqHolder;
/*     */ import org.omg.CORBA.Principal;
/*     */ import org.omg.CORBA.ShortSeqHolder;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.ULongLongSeqHolder;
/*     */ import org.omg.CORBA.ULongSeqHolder;
/*     */ import org.omg.CORBA.UShortSeqHolder;
/*     */ import org.omg.CORBA.WCharSeqHolder;
/*     */ import org.omg.CORBA.portable.BoxedValueHelper;
/*     */ import org.omg.CORBA.portable.ValueInputStream;
/*     */ import org.omg.CORBA_2_3.portable.InputStream;
/*     */ 
/*     */ public abstract class CDRInputStream extends InputStream
/*     */   implements MarshalInputStream, DataInputStream, ValueInputStream
/*     */ {
/*     */   protected CorbaMessageMediator messageMediator;
/*     */   private CDRInputStreamBase impl;
/*     */ 
/*     */   public CDRInputStream()
/*     */   {
/*     */   }
/*     */ 
/*     */   public CDRInputStream(CDRInputStream paramCDRInputStream)
/*     */   {
/* 105 */     this.impl = paramCDRInputStream.impl.dup();
/* 106 */     this.impl.setParent(this);
/*     */   }
/*     */ 
/*     */   public CDRInputStream(org.omg.CORBA.ORB paramORB, ByteBuffer paramByteBuffer, int paramInt, boolean paramBoolean, GIOPVersion paramGIOPVersion, byte paramByte, BufferManagerRead paramBufferManagerRead)
/*     */   {
/* 117 */     this.impl = InputStreamFactory.newInputStream((com.sun.corba.se.spi.orb.ORB)paramORB, paramGIOPVersion, paramByte);
/*     */ 
/* 120 */     this.impl.init(paramORB, paramByteBuffer, paramInt, paramBoolean, paramBufferManagerRead);
/*     */ 
/* 122 */     this.impl.setParent(this);
/*     */   }
/*     */ 
/*     */   public final boolean read_boolean()
/*     */   {
/* 127 */     return this.impl.read_boolean();
/*     */   }
/*     */ 
/*     */   public final char read_char() {
/* 131 */     return this.impl.read_char();
/*     */   }
/*     */ 
/*     */   public final char read_wchar() {
/* 135 */     return this.impl.read_wchar();
/*     */   }
/*     */ 
/*     */   public final byte read_octet() {
/* 139 */     return this.impl.read_octet();
/*     */   }
/*     */ 
/*     */   public final short read_short() {
/* 143 */     return this.impl.read_short();
/*     */   }
/*     */ 
/*     */   public final short read_ushort() {
/* 147 */     return this.impl.read_ushort();
/*     */   }
/*     */ 
/*     */   public final int read_long() {
/* 151 */     return this.impl.read_long();
/*     */   }
/*     */ 
/*     */   public final int read_ulong() {
/* 155 */     return this.impl.read_ulong();
/*     */   }
/*     */ 
/*     */   public final long read_longlong() {
/* 159 */     return this.impl.read_longlong();
/*     */   }
/*     */ 
/*     */   public final long read_ulonglong() {
/* 163 */     return this.impl.read_ulonglong();
/*     */   }
/*     */ 
/*     */   public final float read_float() {
/* 167 */     return this.impl.read_float();
/*     */   }
/*     */ 
/*     */   public final double read_double() {
/* 171 */     return this.impl.read_double();
/*     */   }
/*     */ 
/*     */   public final String read_string() {
/* 175 */     return this.impl.read_string();
/*     */   }
/*     */ 
/*     */   public final String read_wstring() {
/* 179 */     return this.impl.read_wstring();
/*     */   }
/*     */ 
/*     */   public final void read_boolean_array(boolean[] paramArrayOfBoolean, int paramInt1, int paramInt2) {
/* 183 */     this.impl.read_boolean_array(paramArrayOfBoolean, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_char_array(char[] paramArrayOfChar, int paramInt1, int paramInt2) {
/* 187 */     this.impl.read_char_array(paramArrayOfChar, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_wchar_array(char[] paramArrayOfChar, int paramInt1, int paramInt2) {
/* 191 */     this.impl.read_wchar_array(paramArrayOfChar, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_octet_array(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/* 195 */     this.impl.read_octet_array(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_short_array(short[] paramArrayOfShort, int paramInt1, int paramInt2) {
/* 199 */     this.impl.read_short_array(paramArrayOfShort, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_ushort_array(short[] paramArrayOfShort, int paramInt1, int paramInt2) {
/* 203 */     this.impl.read_ushort_array(paramArrayOfShort, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_long_array(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
/* 207 */     this.impl.read_long_array(paramArrayOfInt, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_ulong_array(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
/* 211 */     this.impl.read_ulong_array(paramArrayOfInt, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_longlong_array(long[] paramArrayOfLong, int paramInt1, int paramInt2) {
/* 215 */     this.impl.read_longlong_array(paramArrayOfLong, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_ulonglong_array(long[] paramArrayOfLong, int paramInt1, int paramInt2) {
/* 219 */     this.impl.read_ulonglong_array(paramArrayOfLong, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_float_array(float[] paramArrayOfFloat, int paramInt1, int paramInt2) {
/* 223 */     this.impl.read_float_array(paramArrayOfFloat, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_double_array(double[] paramArrayOfDouble, int paramInt1, int paramInt2) {
/* 227 */     this.impl.read_double_array(paramArrayOfDouble, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final org.omg.CORBA.Object read_Object() {
/* 231 */     return this.impl.read_Object();
/*     */   }
/*     */ 
/*     */   public final TypeCode read_TypeCode() {
/* 235 */     return this.impl.read_TypeCode();
/*     */   }
/*     */   public final Any read_any() {
/* 238 */     return this.impl.read_any();
/*     */   }
/*     */ 
/*     */   public final Principal read_Principal() {
/* 242 */     return this.impl.read_Principal();
/*     */   }
/*     */ 
/*     */   public final int read() throws IOException {
/* 246 */     return this.impl.read();
/*     */   }
/*     */ 
/*     */   public final BigDecimal read_fixed() {
/* 250 */     return this.impl.read_fixed();
/*     */   }
/*     */ 
/*     */   public final Context read_Context() {
/* 254 */     return this.impl.read_Context();
/*     */   }
/*     */ 
/*     */   public final org.omg.CORBA.Object read_Object(Class paramClass) {
/* 258 */     return this.impl.read_Object(paramClass);
/*     */   }
/*     */ 
/*     */   public final org.omg.CORBA.ORB orb() {
/* 262 */     return this.impl.orb();
/*     */   }
/*     */ 
/*     */   public final Serializable read_value()
/*     */   {
/* 267 */     return this.impl.read_value();
/*     */   }
/*     */ 
/*     */   public final Serializable read_value(Class paramClass) {
/* 271 */     return this.impl.read_value(paramClass);
/*     */   }
/*     */ 
/*     */   public final Serializable read_value(BoxedValueHelper paramBoxedValueHelper) {
/* 275 */     return this.impl.read_value(paramBoxedValueHelper);
/*     */   }
/*     */ 
/*     */   public final Serializable read_value(String paramString) {
/* 279 */     return this.impl.read_value(paramString);
/*     */   }
/*     */ 
/*     */   public final Serializable read_value(Serializable paramSerializable) {
/* 283 */     return this.impl.read_value(paramSerializable);
/*     */   }
/*     */ 
/*     */   public final java.lang.Object read_abstract_interface() {
/* 287 */     return this.impl.read_abstract_interface();
/*     */   }
/*     */ 
/*     */   public final java.lang.Object read_abstract_interface(Class paramClass) {
/* 291 */     return this.impl.read_abstract_interface(paramClass);
/*     */   }
/*     */ 
/*     */   public final void consumeEndian()
/*     */   {
/* 296 */     this.impl.consumeEndian();
/*     */   }
/*     */ 
/*     */   public final int getPosition() {
/* 300 */     return this.impl.getPosition();
/*     */   }
/*     */ 
/*     */   public final java.lang.Object read_Abstract()
/*     */   {
/* 306 */     return this.impl.read_Abstract();
/*     */   }
/*     */ 
/*     */   public final Serializable read_Value() {
/* 310 */     return this.impl.read_Value();
/*     */   }
/*     */ 
/*     */   public final void read_any_array(AnySeqHolder paramAnySeqHolder, int paramInt1, int paramInt2) {
/* 314 */     this.impl.read_any_array(paramAnySeqHolder, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_boolean_array(BooleanSeqHolder paramBooleanSeqHolder, int paramInt1, int paramInt2) {
/* 318 */     this.impl.read_boolean_array(paramBooleanSeqHolder, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_char_array(CharSeqHolder paramCharSeqHolder, int paramInt1, int paramInt2) {
/* 322 */     this.impl.read_char_array(paramCharSeqHolder, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_wchar_array(WCharSeqHolder paramWCharSeqHolder, int paramInt1, int paramInt2) {
/* 326 */     this.impl.read_wchar_array(paramWCharSeqHolder, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_octet_array(OctetSeqHolder paramOctetSeqHolder, int paramInt1, int paramInt2) {
/* 330 */     this.impl.read_octet_array(paramOctetSeqHolder, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_short_array(ShortSeqHolder paramShortSeqHolder, int paramInt1, int paramInt2) {
/* 334 */     this.impl.read_short_array(paramShortSeqHolder, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_ushort_array(UShortSeqHolder paramUShortSeqHolder, int paramInt1, int paramInt2) {
/* 338 */     this.impl.read_ushort_array(paramUShortSeqHolder, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_long_array(LongSeqHolder paramLongSeqHolder, int paramInt1, int paramInt2) {
/* 342 */     this.impl.read_long_array(paramLongSeqHolder, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_ulong_array(ULongSeqHolder paramULongSeqHolder, int paramInt1, int paramInt2) {
/* 346 */     this.impl.read_ulong_array(paramULongSeqHolder, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_ulonglong_array(ULongLongSeqHolder paramULongLongSeqHolder, int paramInt1, int paramInt2) {
/* 350 */     this.impl.read_ulonglong_array(paramULongLongSeqHolder, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_longlong_array(LongLongSeqHolder paramLongLongSeqHolder, int paramInt1, int paramInt2) {
/* 354 */     this.impl.read_longlong_array(paramLongLongSeqHolder, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_float_array(FloatSeqHolder paramFloatSeqHolder, int paramInt1, int paramInt2) {
/* 358 */     this.impl.read_float_array(paramFloatSeqHolder, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void read_double_array(DoubleSeqHolder paramDoubleSeqHolder, int paramInt1, int paramInt2) {
/* 362 */     this.impl.read_double_array(paramDoubleSeqHolder, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final String[] _truncatable_ids()
/*     */   {
/* 367 */     return this.impl._truncatable_ids();
/*     */   }
/*     */ 
/*     */   public final int read(byte[] paramArrayOfByte) throws IOException
/*     */   {
/* 372 */     return this.impl.read(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public final int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/* 376 */     return this.impl.read(paramArrayOfByte, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final long skip(long paramLong) throws IOException {
/* 380 */     return this.impl.skip(paramLong);
/*     */   }
/*     */ 
/*     */   public final int available() throws IOException {
/* 384 */     return this.impl.available();
/*     */   }
/*     */ 
/*     */   public final void close() throws IOException {
/* 388 */     this.impl.close();
/*     */   }
/*     */ 
/*     */   public final void mark(int paramInt) {
/* 392 */     this.impl.mark(paramInt);
/*     */   }
/*     */ 
/*     */   public final void reset() {
/* 396 */     this.impl.reset();
/*     */   }
/*     */ 
/*     */   public final boolean markSupported() {
/* 400 */     return this.impl.markSupported();
/*     */   }
/*     */ 
/*     */   public abstract CDRInputStream dup();
/*     */ 
/*     */   public final BigDecimal read_fixed(short paramShort1, short paramShort2)
/*     */   {
/* 407 */     return this.impl.read_fixed(paramShort1, paramShort2);
/*     */   }
/*     */ 
/*     */   public final boolean isLittleEndian() {
/* 411 */     return this.impl.isLittleEndian();
/*     */   }
/*     */ 
/*     */   protected final ByteBuffer getByteBuffer() {
/* 415 */     return this.impl.getByteBuffer();
/*     */   }
/*     */ 
/*     */   protected final void setByteBuffer(ByteBuffer paramByteBuffer) {
/* 419 */     this.impl.setByteBuffer(paramByteBuffer);
/*     */   }
/*     */ 
/*     */   protected final void setByteBufferWithInfo(ByteBufferWithInfo paramByteBufferWithInfo) {
/* 423 */     this.impl.setByteBufferWithInfo(paramByteBufferWithInfo);
/*     */   }
/*     */ 
/*     */   protected final boolean isSharing(ByteBuffer paramByteBuffer)
/*     */   {
/* 430 */     return getByteBuffer() == paramByteBuffer;
/*     */   }
/*     */ 
/*     */   public final int getBufferLength() {
/* 434 */     return this.impl.getBufferLength();
/*     */   }
/*     */ 
/*     */   protected final void setBufferLength(int paramInt) {
/* 438 */     this.impl.setBufferLength(paramInt);
/*     */   }
/*     */ 
/*     */   protected final int getIndex() {
/* 442 */     return this.impl.getIndex();
/*     */   }
/*     */ 
/*     */   protected final void setIndex(int paramInt) {
/* 446 */     this.impl.setIndex(paramInt);
/*     */   }
/*     */ 
/*     */   public final void orb(org.omg.CORBA.ORB paramORB) {
/* 450 */     this.impl.orb(paramORB);
/*     */   }
/*     */ 
/*     */   public final GIOPVersion getGIOPVersion() {
/* 454 */     return this.impl.getGIOPVersion();
/*     */   }
/*     */ 
/*     */   public final BufferManagerRead getBufferManager() {
/* 458 */     return this.impl.getBufferManager();
/*     */   }
/*     */ 
/*     */   public CodeBase getCodeBase()
/*     */   {
/* 465 */     return null;
/*     */   }
/*     */ 
/*     */   protected CodeSetConversion.BTCConverter createCharBTCConverter()
/*     */   {
/* 471 */     return CodeSetConversion.impl().getBTCConverter(OSFCodeSetRegistry.ISO_8859_1, this.impl.isLittleEndian());
/*     */   }
/*     */ 
/*     */   protected abstract CodeSetConversion.BTCConverter createWCharBTCConverter();
/*     */ 
/*     */   void printBuffer()
/*     */   {
/* 481 */     this.impl.printBuffer();
/*     */   }
/*     */ 
/*     */   public void alignOnBoundary(int paramInt)
/*     */   {
/* 491 */     this.impl.alignOnBoundary(paramInt);
/*     */   }
/*     */ 
/*     */   public void setHeaderPadding(boolean paramBoolean)
/*     */   {
/* 496 */     this.impl.setHeaderPadding(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void performORBVersionSpecificInit()
/*     */   {
/* 511 */     if (this.impl != null)
/* 512 */       this.impl.performORBVersionSpecificInit();
/*     */   }
/*     */ 
/*     */   public void resetCodeSetConverters()
/*     */   {
/* 522 */     this.impl.resetCodeSetConverters();
/*     */   }
/*     */ 
/*     */   public void setMessageMediator(MessageMediator paramMessageMediator)
/*     */   {
/* 527 */     this.messageMediator = ((CorbaMessageMediator)paramMessageMediator);
/*     */   }
/*     */ 
/*     */   public MessageMediator getMessageMediator()
/*     */   {
/* 532 */     return this.messageMediator;
/*     */   }
/*     */ 
/*     */   public void start_value()
/*     */   {
/* 538 */     this.impl.start_value();
/*     */   }
/*     */ 
/*     */   public void end_value() {
/* 542 */     this.impl.end_value();
/*     */   }
/*     */ 
/*     */   private static class InputStreamFactory
/*     */   {
/*     */     public static CDRInputStreamBase newInputStream(com.sun.corba.se.spi.orb.ORB paramORB, GIOPVersion paramGIOPVersion, byte paramByte)
/*     */     {
/*  73 */       switch (paramGIOPVersion.intValue()) {
/*     */       case 256:
/*  75 */         return new CDRInputStream_1_0();
/*     */       case 257:
/*  77 */         return new CDRInputStream_1_1();
/*     */       case 258:
/*  79 */         if (paramByte != 0) {
/*  80 */           return new IDLJavaSerializationInputStream(paramByte);
/*     */         }
/*     */ 
/*  83 */         return new CDRInputStream_1_2();
/*     */       }
/*     */ 
/*  86 */       ORBUtilSystemException localORBUtilSystemException = ORBUtilSystemException.get(paramORB, "rpc.encoding");
/*     */ 
/*  88 */       throw localORBUtilSystemException.unsupportedGiopVersion(paramGIOPVersion);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.CDRInputStream
 * JD-Core Version:    0.6.2
 */