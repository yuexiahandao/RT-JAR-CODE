/*     */ package com.sun.corba.se.impl.encoding;
/*     */ 
/*     */ import com.sun.corba.se.impl.corba.TypeCodeImpl;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*     */ import com.sun.corba.se.impl.util.Utility;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.IORFactories;
/*     */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*     */ import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.Serializable;
/*     */ import java.math.BigDecimal;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.rmi.Remote;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.LocalObject;
/*     */ import org.omg.CORBA.Principal;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.portable.BoxedValueHelper;
/*     */ 
/*     */ final class IDLJavaSerializationOutputStream extends CDROutputStreamBase
/*     */ {
/*     */   private com.sun.corba.se.spi.orb.ORB orb;
/*     */   private byte encodingVersion;
/*     */   private ObjectOutputStream os;
/*     */   private _ByteArrayOutputStream bos;
/*     */   private BufferManagerWrite bufferManager;
/*  78 */   private final int directWriteLength = 16;
/*     */   protected ORBUtilSystemException wrapper;
/*     */ 
/*     */   public IDLJavaSerializationOutputStream(byte paramByte)
/*     */   {
/* 134 */     this.encodingVersion = paramByte;
/*     */   }
/*     */ 
/*     */   public void init(org.omg.CORBA.ORB paramORB, boolean paramBoolean1, BufferManagerWrite paramBufferManagerWrite, byte paramByte, boolean paramBoolean2)
/*     */   {
/* 141 */     this.orb = ((com.sun.corba.se.spi.orb.ORB)paramORB);
/* 142 */     this.bufferManager = paramBufferManagerWrite;
/* 143 */     this.wrapper = ORBUtilSystemException.get((com.sun.corba.se.spi.orb.ORB)paramORB, "rpc.encoding");
/*     */ 
/* 145 */     this.bos = new _ByteArrayOutputStream(1024);
/*     */   }
/*     */ 
/*     */   private void initObjectOutputStream()
/*     */   {
/* 152 */     if (this.os != null)
/* 153 */       throw this.wrapper.javaStreamInitFailed();
/*     */     try
/*     */     {
/* 156 */       this.os = new MarshalObjectOutputStream(this.bos, this.orb);
/*     */     } catch (Exception localException) {
/* 158 */       throw this.wrapper.javaStreamInitFailed(localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void write_boolean(boolean paramBoolean)
/*     */   {
/*     */     try
/*     */     {
/* 168 */       this.os.writeBoolean(paramBoolean);
/*     */     } catch (Exception localException) {
/* 170 */       throw this.wrapper.javaSerializationException(localException, "write_boolean");
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void write_char(char paramChar) {
/*     */     try {
/* 176 */       this.os.writeChar(paramChar);
/*     */     } catch (Exception localException) {
/* 178 */       throw this.wrapper.javaSerializationException(localException, "write_char");
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void write_wchar(char paramChar) {
/* 183 */     write_char(paramChar);
/*     */   }
/*     */ 
/*     */   public final void write_octet(byte paramByte)
/*     */   {
/* 189 */     if (this.bos.size() < 16) {
/* 190 */       this.bos.write(paramByte);
/* 191 */       if (this.bos.size() == 16) {
/* 192 */         initObjectOutputStream();
/*     */       }
/* 194 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 198 */       this.os.writeByte(paramByte);
/*     */     } catch (Exception localException) {
/* 200 */       throw this.wrapper.javaSerializationException(localException, "write_octet");
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void write_short(short paramShort) {
/*     */     try {
/* 206 */       this.os.writeShort(paramShort);
/*     */     } catch (Exception localException) {
/* 208 */       throw this.wrapper.javaSerializationException(localException, "write_short");
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void write_ushort(short paramShort) {
/* 213 */     write_short(paramShort);
/*     */   }
/*     */ 
/*     */   public final void write_long(int paramInt)
/*     */   {
/* 219 */     if (this.bos.size() < 16)
/*     */     {
/* 223 */       this.bos.write((byte)(paramInt >>> 24 & 0xFF));
/* 224 */       this.bos.write((byte)(paramInt >>> 16 & 0xFF));
/* 225 */       this.bos.write((byte)(paramInt >>> 8 & 0xFF));
/* 226 */       this.bos.write((byte)(paramInt >>> 0 & 0xFF));
/*     */ 
/* 228 */       if (this.bos.size() == 16)
/* 229 */         initObjectOutputStream();
/* 230 */       else if (this.bos.size() > 16)
/*     */       {
/* 233 */         this.wrapper.javaSerializationException("write_long");
/*     */       }
/* 235 */       return;
/*     */     }
/*     */     try
/*     */     {
/* 239 */       this.os.writeInt(paramInt);
/*     */     } catch (Exception localException) {
/* 241 */       throw this.wrapper.javaSerializationException(localException, "write_long");
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void write_ulong(int paramInt) {
/* 246 */     write_long(paramInt);
/*     */   }
/*     */ 
/*     */   public final void write_longlong(long paramLong) {
/*     */     try {
/* 251 */       this.os.writeLong(paramLong);
/*     */     } catch (Exception localException) {
/* 253 */       throw this.wrapper.javaSerializationException(localException, "write_longlong");
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void write_ulonglong(long paramLong) {
/* 258 */     write_longlong(paramLong);
/*     */   }
/*     */ 
/*     */   public final void write_float(float paramFloat) {
/*     */     try {
/* 263 */       this.os.writeFloat(paramFloat);
/*     */     } catch (Exception localException) {
/* 265 */       throw this.wrapper.javaSerializationException(localException, "write_float");
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void write_double(double paramDouble) {
/*     */     try {
/* 271 */       this.os.writeDouble(paramDouble);
/*     */     } catch (Exception localException) {
/* 273 */       throw this.wrapper.javaSerializationException(localException, "write_double");
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void write_string(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 281 */       this.os.writeUTF(paramString);
/*     */     } catch (Exception localException) {
/* 283 */       throw this.wrapper.javaSerializationException(localException, "write_string");
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void write_wstring(String paramString) {
/*     */     try {
/* 289 */       this.os.writeObject(paramString);
/*     */     } catch (Exception localException) {
/* 291 */       throw this.wrapper.javaSerializationException(localException, "write_wstring");
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void write_boolean_array(boolean[] paramArrayOfBoolean, int paramInt1, int paramInt2)
/*     */   {
/* 299 */     for (int i = 0; i < paramInt2; i++)
/* 300 */       write_boolean(paramArrayOfBoolean[(paramInt1 + i)]);
/*     */   }
/*     */ 
/*     */   public final void write_char_array(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 305 */     for (int i = 0; i < paramInt2; i++)
/* 306 */       write_char(paramArrayOfChar[(paramInt1 + i)]);
/*     */   }
/*     */ 
/*     */   public final void write_wchar_array(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 311 */     write_char_array(paramArrayOfChar, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void write_octet_array(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/*     */     try {
/* 316 */       this.os.write(paramArrayOfByte, paramInt1, paramInt2);
/*     */     } catch (Exception localException) {
/* 318 */       throw this.wrapper.javaSerializationException(localException, "write_octet_array");
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void write_short_array(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*     */   {
/* 324 */     for (int i = 0; i < paramInt2; i++)
/* 325 */       write_short(paramArrayOfShort[(paramInt1 + i)]);
/*     */   }
/*     */ 
/*     */   public final void write_ushort_array(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*     */   {
/* 331 */     write_short_array(paramArrayOfShort, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void write_long_array(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
/* 335 */     for (int i = 0; i < paramInt2; i++)
/* 336 */       write_long(paramArrayOfInt[(paramInt1 + i)]);
/*     */   }
/*     */ 
/*     */   public final void write_ulong_array(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*     */   {
/* 341 */     write_long_array(paramArrayOfInt, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void write_longlong_array(long[] paramArrayOfLong, int paramInt1, int paramInt2)
/*     */   {
/* 346 */     for (int i = 0; i < paramInt2; i++)
/* 347 */       write_longlong(paramArrayOfLong[(paramInt1 + i)]);
/*     */   }
/*     */ 
/*     */   public final void write_ulonglong_array(long[] paramArrayOfLong, int paramInt1, int paramInt2)
/*     */   {
/* 353 */     write_longlong_array(paramArrayOfLong, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public final void write_float_array(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
/*     */   {
/* 358 */     for (int i = 0; i < paramInt2; i++)
/* 359 */       write_float(paramArrayOfFloat[(paramInt1 + i)]);
/*     */   }
/*     */ 
/*     */   public final void write_double_array(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
/*     */   {
/* 365 */     for (int i = 0; i < paramInt2; i++)
/* 366 */       write_double(paramArrayOfDouble[(paramInt1 + i)]);
/*     */   }
/*     */ 
/*     */   public final void write_Object(org.omg.CORBA.Object paramObject)
/*     */   {
/* 373 */     if (paramObject == null) {
/* 374 */       localIOR = IORFactories.makeIOR(this.orb);
/* 375 */       localIOR.write(this.parent);
/* 376 */       return;
/*     */     }
/*     */ 
/* 379 */     if ((paramObject instanceof LocalObject)) {
/* 380 */       throw this.wrapper.writeLocalObject(CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/* 382 */     IOR localIOR = ORBUtility.connectAndGetIOR(this.orb, paramObject);
/* 383 */     localIOR.write(this.parent);
/*     */   }
/*     */ 
/*     */   public final void write_TypeCode(TypeCode paramTypeCode)
/*     */   {
/* 388 */     if (paramTypeCode == null)
/* 389 */       throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
/*     */     TypeCodeImpl localTypeCodeImpl;
/* 392 */     if ((paramTypeCode instanceof TypeCodeImpl))
/* 393 */       localTypeCodeImpl = (TypeCodeImpl)paramTypeCode;
/*     */     else {
/* 395 */       localTypeCodeImpl = new TypeCodeImpl(this.orb, paramTypeCode);
/*     */     }
/* 397 */     localTypeCodeImpl.write_value(this.parent);
/*     */   }
/*     */ 
/*     */   public final void write_any(Any paramAny) {
/* 401 */     if (paramAny == null) {
/* 402 */       throw this.wrapper.nullParam(CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/* 404 */     write_TypeCode(paramAny.type());
/* 405 */     paramAny.write_value(this.parent);
/*     */   }
/*     */ 
/*     */   public final void write_Principal(Principal paramPrincipal)
/*     */   {
/* 411 */     write_long(paramPrincipal.name().length);
/* 412 */     write_octet_array(paramPrincipal.name(), 0, paramPrincipal.name().length);
/*     */   }
/*     */ 
/*     */   public final void write_fixed(BigDecimal paramBigDecimal)
/*     */   {
/* 417 */     write_fixed(paramBigDecimal.toString(), paramBigDecimal.signum());
/*     */   }
/*     */ 
/*     */   private void write_fixed(String paramString, int paramInt)
/*     */   {
/* 423 */     int i = paramString.length();
/*     */ 
/* 426 */     int j = 0;
/*     */ 
/* 431 */     int m = 0;
/*     */     char c;
/* 432 */     for (int n = 0; n < i; n++) {
/* 433 */       c = paramString.charAt(n);
/* 434 */       if ((c != '-') && (c != '+') && (c != '.'))
/*     */       {
/* 436 */         m++;
/*     */       }
/*     */     }
/*     */     byte b;
/* 439 */     for (n = 0; n < i; n++) {
/* 440 */       c = paramString.charAt(n);
/* 441 */       if ((c != '-') && (c != '+') && (c != '.'))
/*     */       {
/* 443 */         int k = (byte)Character.digit(c, 10);
/* 444 */         if (k == -1) {
/* 445 */           throw this.wrapper.badDigitInFixed(CompletionStatus.COMPLETED_MAYBE);
/*     */         }
/*     */ 
/* 452 */         if (m % 2 == 0) {
/* 453 */           j = (byte)(j | k);
/* 454 */           write_octet(j);
/* 455 */           j = 0;
/*     */         } else {
/* 457 */           b = (byte)(j | k << 4);
/*     */         }
/* 459 */         m--;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 464 */     if (paramInt == -1)
/* 465 */       b = (byte)(b | 0xD);
/*     */     else {
/* 467 */       b = (byte)(b | 0xC);
/*     */     }
/* 469 */     write_octet(b);
/*     */   }
/*     */ 
/*     */   public final org.omg.CORBA.ORB orb() {
/* 473 */     return this.orb;
/*     */   }
/*     */ 
/*     */   public final void write_value(Serializable paramSerializable)
/*     */   {
/* 479 */     write_value(paramSerializable, (String)null);
/*     */   }
/*     */ 
/*     */   public final void write_value(Serializable paramSerializable, Class paramClass)
/*     */   {
/* 484 */     write_value(paramSerializable);
/*     */   }
/*     */ 
/*     */   public final void write_value(Serializable paramSerializable, String paramString)
/*     */   {
/*     */     try {
/* 490 */       this.os.writeObject(paramSerializable);
/*     */     } catch (Exception localException) {
/* 492 */       throw this.wrapper.javaSerializationException(localException, "write_value");
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void write_value(Serializable paramSerializable, BoxedValueHelper paramBoxedValueHelper)
/*     */   {
/* 498 */     write_value(paramSerializable, (String)null);
/*     */   }
/*     */ 
/*     */   public final void write_abstract_interface(java.lang.Object paramObject)
/*     */   {
/* 503 */     boolean bool = false;
/* 504 */     org.omg.CORBA.Object localObject = null;
/*     */ 
/* 507 */     if ((paramObject != null) && ((paramObject instanceof org.omg.CORBA.Object))) {
/* 508 */       localObject = (org.omg.CORBA.Object)paramObject;
/* 509 */       bool = true;
/*     */     }
/*     */ 
/* 513 */     write_boolean(bool);
/*     */ 
/* 516 */     if (bool)
/* 517 */       write_Object(localObject);
/*     */     else
/*     */       try {
/* 520 */         write_value((Serializable)paramObject);
/*     */       } catch (ClassCastException localClassCastException) {
/* 522 */         if ((paramObject instanceof Serializable)) {
/* 523 */           throw localClassCastException;
/*     */         }
/* 525 */         ORBUtility.throwNotSerializableForCorba(paramObject.getClass().getName());
/*     */       }
/*     */   }
/*     */ 
/*     */   public final void start_block()
/*     */   {
/* 535 */     throw this.wrapper.giopVersionError();
/*     */   }
/*     */ 
/*     */   public final void end_block() {
/* 539 */     throw this.wrapper.giopVersionError();
/*     */   }
/*     */ 
/*     */   public final void putEndian() {
/* 543 */     throw this.wrapper.giopVersionError();
/*     */   }
/*     */ 
/*     */   public void writeTo(java.io.OutputStream paramOutputStream) throws IOException {
/*     */     try {
/* 548 */       this.os.flush();
/* 549 */       this.bos.writeTo(paramOutputStream);
/*     */     } catch (Exception localException) {
/* 551 */       throw this.wrapper.javaSerializationException(localException, "writeTo");
/*     */     }
/*     */   }
/*     */ 
/*     */   public final byte[] toByteArray() {
/*     */     try {
/* 557 */       this.os.flush();
/* 558 */       return this.bos.toByteArray();
/*     */     } catch (Exception localException) {
/* 560 */       throw this.wrapper.javaSerializationException(localException, "toByteArray");
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void write_Abstract(java.lang.Object paramObject)
/*     */   {
/* 567 */     write_abstract_interface(paramObject);
/*     */   }
/*     */ 
/*     */   public final void write_Value(Serializable paramSerializable) {
/* 571 */     write_value(paramSerializable);
/*     */   }
/*     */ 
/*     */   public final void write_any_array(Any[] paramArrayOfAny, int paramInt1, int paramInt2)
/*     */   {
/* 576 */     for (int i = 0; i < paramInt2; i++)
/* 577 */       write_any(paramArrayOfAny[(paramInt1 + i)]);
/*     */   }
/*     */ 
/*     */   public final String[] _truncatable_ids()
/*     */   {
/* 584 */     throw this.wrapper.giopVersionError();
/*     */   }
/*     */ 
/*     */   public final int getSize()
/*     */   {
/*     */     try
/*     */     {
/* 591 */       this.os.flush();
/* 592 */       return this.bos.size();
/*     */     } catch (Exception localException) {
/* 594 */       throw this.wrapper.javaSerializationException(localException, "write_boolean");
/*     */     }
/*     */   }
/*     */ 
/*     */   public final int getIndex() {
/* 599 */     return getSize();
/*     */   }
/*     */ 
/*     */   protected int getRealIndex(int paramInt) {
/* 603 */     return getSize();
/*     */   }
/*     */ 
/*     */   public final void setIndex(int paramInt) {
/* 607 */     throw this.wrapper.giopVersionError();
/*     */   }
/*     */ 
/*     */   public final ByteBuffer getByteBuffer() {
/* 611 */     throw this.wrapper.giopVersionError();
/*     */   }
/*     */ 
/*     */   public final void setByteBuffer(ByteBuffer paramByteBuffer) {
/* 615 */     throw this.wrapper.giopVersionError();
/*     */   }
/*     */ 
/*     */   public final boolean isLittleEndian()
/*     */   {
/* 620 */     return false;
/*     */   }
/*     */ 
/*     */   public ByteBufferWithInfo getByteBufferWithInfo() {
/*     */     try {
/* 625 */       this.os.flush();
/*     */     } catch (Exception localException) {
/* 627 */       throw this.wrapper.javaSerializationException(localException, "getByteBufferWithInfo");
/*     */     }
/*     */ 
/* 630 */     ByteBuffer localByteBuffer = ByteBuffer.wrap(this.bos.getByteArray());
/* 631 */     localByteBuffer.limit(this.bos.size());
/* 632 */     return new ByteBufferWithInfo(this.orb, localByteBuffer, this.bos.size());
/*     */   }
/*     */ 
/*     */   public void setByteBufferWithInfo(ByteBufferWithInfo paramByteBufferWithInfo) {
/* 636 */     throw this.wrapper.giopVersionError();
/*     */   }
/*     */ 
/*     */   public final BufferManagerWrite getBufferManager() {
/* 640 */     return this.bufferManager;
/*     */   }
/*     */ 
/*     */   public final void write_fixed(BigDecimal paramBigDecimal, short paramShort1, short paramShort2)
/*     */   {
/* 650 */     String str1 = paramBigDecimal.toString();
/*     */ 
/* 656 */     if ((str1.charAt(0) == '-') || (str1.charAt(0) == '+')) {
/* 657 */       str1 = str1.substring(1);
/*     */     }
/*     */ 
/* 661 */     int i = str1.indexOf('.');
/*     */     String str2;
/*     */     String str3;
/* 662 */     if (i == -1) {
/* 663 */       str2 = str1;
/* 664 */       str3 = null;
/* 665 */     } else if (i == 0) {
/* 666 */       str2 = null;
/* 667 */       str3 = str1;
/*     */     } else {
/* 669 */       str2 = str1.substring(0, i);
/* 670 */       str3 = str1.substring(i + 1);
/*     */     }
/*     */ 
/* 674 */     StringBuffer localStringBuffer = new StringBuffer(paramShort1);
/* 675 */     if (str3 != null) {
/* 676 */       localStringBuffer.append(str3);
/*     */     }
/* 678 */     while (localStringBuffer.length() < paramShort2) {
/* 679 */       localStringBuffer.append('0');
/*     */     }
/* 681 */     if (str2 != null) {
/* 682 */       localStringBuffer.insert(0, str2);
/*     */     }
/* 684 */     while (localStringBuffer.length() < paramShort1) {
/* 685 */       localStringBuffer.insert(0, '0');
/*     */     }
/*     */ 
/* 689 */     write_fixed(localStringBuffer.toString(), paramBigDecimal.signum());
/*     */   }
/*     */ 
/*     */   public final void writeOctetSequenceTo(org.omg.CORBA.portable.OutputStream paramOutputStream)
/*     */   {
/* 694 */     byte[] arrayOfByte = toByteArray();
/* 695 */     paramOutputStream.write_long(arrayOfByte.length);
/* 696 */     paramOutputStream.write_octet_array(arrayOfByte, 0, arrayOfByte.length);
/*     */   }
/*     */ 
/*     */   public final GIOPVersion getGIOPVersion() {
/* 700 */     return GIOPVersion.V1_2;
/*     */   }
/*     */ 
/*     */   public final void writeIndirection(int paramInt1, int paramInt2) {
/* 704 */     throw this.wrapper.giopVersionError();
/*     */   }
/*     */   void freeInternalCaches() {
/*     */   }
/*     */ 
/*     */   void printBuffer() {
/* 710 */     byte[] arrayOfByte = toByteArray();
/*     */ 
/* 712 */     System.out.println("+++++++ Output Buffer ++++++++");
/* 713 */     System.out.println();
/* 714 */     System.out.println("Current position: " + arrayOfByte.length);
/*     */ 
/* 716 */     System.out.println();
/*     */ 
/* 718 */     char[] arrayOfChar = new char[16];
/*     */     try
/*     */     {
/* 722 */       for (int i = 0; i < arrayOfByte.length; i += 16)
/*     */       {
/* 724 */         int j = 0;
/*     */ 
/* 730 */         while ((j < 16) && (j + i < arrayOfByte.length)) {
/* 731 */           k = arrayOfByte[(i + j)];
/* 732 */           if (k < 0)
/* 733 */             k = 256 + k;
/* 734 */           String str = Integer.toHexString(k);
/* 735 */           if (str.length() == 1)
/* 736 */             str = "0" + str;
/* 737 */           System.out.print(str + " ");
/* 738 */           j++;
/*     */         }
/*     */ 
/* 744 */         while (j < 16) {
/* 745 */           System.out.print("   ");
/* 746 */           j++;
/*     */         }
/*     */ 
/* 751 */         int k = 0;
/*     */ 
/* 753 */         while ((k < 16) && (k + i < arrayOfByte.length)) {
/* 754 */           if (ORBUtility.isPrintable((char)arrayOfByte[(i + k)]))
/* 755 */             arrayOfChar[k] = ((char)arrayOfByte[(i + k)]);
/*     */           else {
/* 757 */             arrayOfChar[k] = '.';
/*     */           }
/* 759 */           k++;
/*     */         }
/* 761 */         System.out.println(new String(arrayOfChar, 0, k));
/*     */       }
/*     */     } catch (Throwable localThrowable) {
/* 764 */       localThrowable.printStackTrace();
/*     */     }
/* 766 */     System.out.println("++++++++++++++++++++++++++++++");
/*     */   }
/*     */ 
/*     */   public void alignOnBoundary(int paramInt) {
/* 770 */     throw this.wrapper.giopVersionError();
/*     */   }
/*     */ 
/*     */   public void setHeaderPadding(boolean paramBoolean)
/*     */   {
/*     */   }
/*     */ 
/*     */   public void start_value(String paramString)
/*     */   {
/* 782 */     throw this.wrapper.giopVersionError();
/*     */   }
/*     */ 
/*     */   public void end_value() {
/* 786 */     throw this.wrapper.giopVersionError();
/*     */   }
/*     */ 
/*     */   class MarshalObjectOutputStream extends ObjectOutputStream
/*     */   {
/*     */     com.sun.corba.se.spi.orb.ORB orb;
/*     */ 
/*     */     MarshalObjectOutputStream(java.io.OutputStream paramORB, com.sun.corba.se.spi.orb.ORB arg3)
/*     */       throws IOException
/*     */     {
/* 100 */       super();
/*     */       java.lang.Object localObject;
/* 101 */       this.orb = localObject;
/* 102 */       AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public java.lang.Object run()
/*     */         {
/* 106 */           IDLJavaSerializationOutputStream.MarshalObjectOutputStream.this.enableReplaceObject(true);
/* 107 */           return null;
/*     */         }
/*     */       });
/*     */     }
/*     */ 
/*     */     protected final java.lang.Object replaceObject(java.lang.Object paramObject)
/*     */       throws IOException
/*     */     {
/*     */       try
/*     */       {
/* 119 */         if (((paramObject instanceof Remote)) && (!StubAdapter.isStub(paramObject)))
/*     */         {
/* 121 */           return Utility.autoConnect(paramObject, this.orb, true);
/*     */         }
/*     */       } catch (Exception localException) {
/* 124 */         IOException localIOException = new IOException("replaceObject failed");
/* 125 */         localIOException.initCause(localException);
/* 126 */         throw localIOException;
/*     */       }
/* 128 */       return paramObject;
/*     */     }
/*     */   }
/*     */ 
/*     */   class _ByteArrayOutputStream extends ByteArrayOutputStream
/*     */   {
/*     */     _ByteArrayOutputStream(int arg2)
/*     */     {
/*  85 */       super();
/*     */     }
/*     */ 
/*     */     byte[] getByteArray() {
/*  89 */       return this.buf;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.IDLJavaSerializationOutputStream
 * JD-Core Version:    0.6.2
 */