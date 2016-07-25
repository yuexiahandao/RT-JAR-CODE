/*      */ package com.sun.corba.se.impl.encoding;
/*      */ 
/*      */ import com.sun.corba.se.impl.corba.PrincipalImpl;
/*      */ import com.sun.corba.se.impl.corba.TypeCodeImpl;
/*      */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*      */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*      */ import com.sun.corba.se.impl.util.RepositoryId;
/*      */ import com.sun.corba.se.impl.util.RepositoryIdCache;
/*      */ import com.sun.corba.se.spi.ior.IOR;
/*      */ import com.sun.corba.se.spi.ior.IORFactories;
/*      */ import com.sun.corba.se.spi.ior.iiop.GIOPVersion;
/*      */ import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
/*      */ import com.sun.corba.se.spi.presentation.rmi.PresentationDefaults;
/*      */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactory;
/*      */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactoryFactory;
/*      */ import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
/*      */ import com.sun.org.omg.SendingContext.CodeBase;
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.io.Serializable;
/*      */ import java.math.BigDecimal;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.rmi.RemoteException;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.LinkedList;
/*      */ import org.omg.CORBA.Any;
/*      */ import org.omg.CORBA.AnySeqHolder;
/*      */ import org.omg.CORBA.BooleanSeqHolder;
/*      */ import org.omg.CORBA.CharSeqHolder;
/*      */ import org.omg.CORBA.DoubleSeqHolder;
/*      */ import org.omg.CORBA.FloatSeqHolder;
/*      */ import org.omg.CORBA.LongLongSeqHolder;
/*      */ import org.omg.CORBA.LongSeqHolder;
/*      */ import org.omg.CORBA.MARSHAL;
/*      */ import org.omg.CORBA.OctetSeqHolder;
/*      */ import org.omg.CORBA.Principal;
/*      */ import org.omg.CORBA.ShortSeqHolder;
/*      */ import org.omg.CORBA.TCKind;
/*      */ import org.omg.CORBA.TypeCode;
/*      */ import org.omg.CORBA.ULongLongSeqHolder;
/*      */ import org.omg.CORBA.ULongSeqHolder;
/*      */ import org.omg.CORBA.UShortSeqHolder;
/*      */ import org.omg.CORBA.WCharSeqHolder;
/*      */ import org.omg.CORBA.portable.BoxedValueHelper;
/*      */ import org.omg.CORBA.portable.IDLEntity;
/*      */ 
/*      */ public class IDLJavaSerializationInputStream extends CDRInputStreamBase
/*      */ {
/*      */   private com.sun.corba.se.spi.orb.ORB orb;
/*      */   private int bufSize;
/*      */   private ByteBuffer buffer;
/*      */   private byte encodingVersion;
/*      */   private ObjectInputStream is;
/*      */   private _ByteArrayInputStream bis;
/*      */   private BufferManagerRead bufferManager;
/*   87 */   private final int directReadLength = 16;
/*      */   private boolean markOn;
/*      */   private int peekIndex;
/*      */   private int peekCount;
/*   92 */   private LinkedList markedItemQ = new LinkedList();
/*      */   protected ORBUtilSystemException wrapper;
/*      */ 
/*      */   public IDLJavaSerializationInputStream(byte paramByte)
/*      */   {
/*  154 */     this.encodingVersion = paramByte;
/*      */   }
/*      */ 
/*      */   public void init(org.omg.CORBA.ORB paramORB, ByteBuffer paramByteBuffer, int paramInt, boolean paramBoolean, BufferManagerRead paramBufferManagerRead)
/*      */   {
/*  162 */     this.orb = ((com.sun.corba.se.spi.orb.ORB)paramORB);
/*  163 */     this.bufSize = paramInt;
/*  164 */     this.bufferManager = paramBufferManagerRead;
/*  165 */     this.buffer = paramByteBuffer;
/*  166 */     this.wrapper = ORBUtilSystemException.get((com.sun.corba.se.spi.orb.ORB)paramORB, "rpc.encoding");
/*      */     byte[] arrayOfByte;
/*  170 */     if (this.buffer.hasArray()) {
/*  171 */       arrayOfByte = this.buffer.array();
/*      */     } else {
/*  173 */       arrayOfByte = new byte[paramInt];
/*  174 */       this.buffer.get(arrayOfByte);
/*      */     }
/*      */ 
/*  178 */     this.bis = new _ByteArrayInputStream(arrayOfByte);
/*      */   }
/*      */ 
/*      */   private void initObjectInputStream()
/*      */   {
/*  184 */     if (this.is != null)
/*  185 */       throw this.wrapper.javaStreamInitFailed();
/*      */     try
/*      */     {
/*  188 */       this.is = new MarshalObjectInputStream(this.bis, this.orb);
/*      */     } catch (Exception localException) {
/*  190 */       throw this.wrapper.javaStreamInitFailed(localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean read_boolean()
/*      */   {
/*  199 */     if ((!this.markOn) && (!this.markedItemQ.isEmpty())) {
/*  200 */       return ((Boolean)this.markedItemQ.removeFirst()).booleanValue();
/*      */     }
/*  202 */     if ((this.markOn) && (!this.markedItemQ.isEmpty()) && (this.peekIndex < this.peekCount))
/*      */     {
/*  204 */       return ((Boolean)this.markedItemQ.get(this.peekIndex++)).booleanValue();
/*      */     }
/*      */     try {
/*  207 */       boolean bool = this.is.readBoolean();
/*  208 */       if (this.markOn) {
/*  209 */         this.markedItemQ.addLast(Boolean.valueOf(bool));
/*      */       }
/*  211 */       return bool;
/*      */     } catch (Exception localException) {
/*  213 */       throw this.wrapper.javaSerializationException(localException, "read_boolean");
/*      */     }
/*      */   }
/*      */ 
/*      */   public char read_char() {
/*  218 */     if ((!this.markOn) && (!this.markedItemQ.isEmpty())) {
/*  219 */       return ((Character)this.markedItemQ.removeFirst()).charValue();
/*      */     }
/*  221 */     if ((this.markOn) && (!this.markedItemQ.isEmpty()) && (this.peekIndex < this.peekCount))
/*      */     {
/*  223 */       return ((Character)this.markedItemQ.get(this.peekIndex++)).charValue();
/*      */     }
/*      */     try {
/*  226 */       char c = this.is.readChar();
/*  227 */       if (this.markOn) {
/*  228 */         this.markedItemQ.addLast(new Character(c));
/*      */       }
/*  230 */       return c;
/*      */     } catch (Exception localException) {
/*  232 */       throw this.wrapper.javaSerializationException(localException, "read_char");
/*      */     }
/*      */   }
/*      */ 
/*      */   public char read_wchar() {
/*  237 */     return read_char();
/*      */   }
/*      */ 
/*      */   public byte read_octet()
/*      */   {
/*      */     byte b;
/*  243 */     if (this.bis.getPosition() < 16) {
/*  244 */       b = (byte)this.bis.read();
/*  245 */       if (this.bis.getPosition() == 16) {
/*  246 */         initObjectInputStream();
/*      */       }
/*  248 */       return b;
/*      */     }
/*      */ 
/*  251 */     if ((!this.markOn) && (!this.markedItemQ.isEmpty())) {
/*  252 */       return ((Byte)this.markedItemQ.removeFirst()).byteValue();
/*      */     }
/*      */ 
/*  255 */     if ((this.markOn) && (!this.markedItemQ.isEmpty()) && (this.peekIndex < this.peekCount))
/*      */     {
/*  257 */       return ((Byte)this.markedItemQ.get(this.peekIndex++)).byteValue();
/*      */     }
/*      */     try
/*      */     {
/*  261 */       b = this.is.readByte();
/*  262 */       if (this.markOn)
/*      */       {
/*  264 */         this.markedItemQ.addLast(new Byte(b));
/*      */       }
/*  266 */       return b;
/*      */     } catch (Exception localException) {
/*  268 */       throw this.wrapper.javaSerializationException(localException, "read_octet");
/*      */     }
/*      */   }
/*      */ 
/*      */   public short read_short() {
/*  273 */     if ((!this.markOn) && (!this.markedItemQ.isEmpty())) {
/*  274 */       return ((Short)this.markedItemQ.removeFirst()).shortValue();
/*      */     }
/*  276 */     if ((this.markOn) && (!this.markedItemQ.isEmpty()) && (this.peekIndex < this.peekCount))
/*      */     {
/*  278 */       return ((Short)this.markedItemQ.get(this.peekIndex++)).shortValue();
/*      */     }
/*      */     try
/*      */     {
/*  282 */       short s = this.is.readShort();
/*  283 */       if (this.markOn) {
/*  284 */         this.markedItemQ.addLast(new Short(s));
/*      */       }
/*  286 */       return s;
/*      */     } catch (Exception localException) {
/*  288 */       throw this.wrapper.javaSerializationException(localException, "read_short");
/*      */     }
/*      */   }
/*      */ 
/*      */   public short read_ushort() {
/*  293 */     return read_short();
/*      */   }
/*      */ 
/*      */   public int read_long()
/*      */   {
/*      */     int i;
/*  299 */     if (this.bis.getPosition() < 16)
/*      */     {
/*  303 */       i = this.bis.read() << 24 & 0xFF000000;
/*  304 */       int j = this.bis.read() << 16 & 0xFF0000;
/*  305 */       int k = this.bis.read() << 8 & 0xFF00;
/*  306 */       int m = this.bis.read() << 0 & 0xFF;
/*      */ 
/*  308 */       if (this.bis.getPosition() == 16)
/*  309 */         initObjectInputStream();
/*  310 */       else if (this.bis.getPosition() > 16)
/*      */       {
/*  313 */         this.wrapper.javaSerializationException("read_long");
/*      */       }
/*      */ 
/*  316 */       return i | j | k | m;
/*      */     }
/*      */ 
/*  319 */     if ((!this.markOn) && (!this.markedItemQ.isEmpty())) {
/*  320 */       return ((Integer)this.markedItemQ.removeFirst()).intValue();
/*      */     }
/*      */ 
/*  323 */     if ((this.markOn) && (!this.markedItemQ.isEmpty()) && (this.peekIndex < this.peekCount))
/*      */     {
/*  325 */       return ((Integer)this.markedItemQ.get(this.peekIndex++)).intValue();
/*      */     }
/*      */     try
/*      */     {
/*  329 */       i = this.is.readInt();
/*  330 */       if (this.markOn) {
/*  331 */         this.markedItemQ.addLast(new Integer(i));
/*      */       }
/*  333 */       return i;
/*      */     } catch (Exception localException) {
/*  335 */       throw this.wrapper.javaSerializationException(localException, "read_long");
/*      */     }
/*      */   }
/*      */ 
/*      */   public int read_ulong() {
/*  340 */     return read_long();
/*      */   }
/*      */ 
/*      */   public long read_longlong() {
/*  344 */     if ((!this.markOn) && (!this.markedItemQ.isEmpty())) {
/*  345 */       return ((Long)this.markedItemQ.removeFirst()).longValue();
/*      */     }
/*  347 */     if ((this.markOn) && (!this.markedItemQ.isEmpty()) && (this.peekIndex < this.peekCount))
/*      */     {
/*  349 */       return ((Long)this.markedItemQ.get(this.peekIndex++)).longValue();
/*      */     }
/*      */     try
/*      */     {
/*  353 */       long l = this.is.readLong();
/*  354 */       if (this.markOn) {
/*  355 */         this.markedItemQ.addLast(new Long(l));
/*      */       }
/*  357 */       return l;
/*      */     } catch (Exception localException) {
/*  359 */       throw this.wrapper.javaSerializationException(localException, "read_longlong");
/*      */     }
/*      */   }
/*      */ 
/*      */   public long read_ulonglong() {
/*  364 */     return read_longlong();
/*      */   }
/*      */ 
/*      */   public float read_float() {
/*  368 */     if ((!this.markOn) && (!this.markedItemQ.isEmpty())) {
/*  369 */       return ((Float)this.markedItemQ.removeFirst()).floatValue();
/*      */     }
/*  371 */     if ((this.markOn) && (!this.markedItemQ.isEmpty()) && (this.peekIndex < this.peekCount))
/*      */     {
/*  373 */       return ((Float)this.markedItemQ.get(this.peekIndex++)).floatValue();
/*      */     }
/*      */     try
/*      */     {
/*  377 */       float f = this.is.readFloat();
/*  378 */       if (this.markOn) {
/*  379 */         this.markedItemQ.addLast(new Float(f));
/*      */       }
/*  381 */       return f;
/*      */     } catch (Exception localException) {
/*  383 */       throw this.wrapper.javaSerializationException(localException, "read_float");
/*      */     }
/*      */   }
/*      */ 
/*      */   public double read_double() {
/*  388 */     if ((!this.markOn) && (!this.markedItemQ.isEmpty())) {
/*  389 */       return ((Double)this.markedItemQ.removeFirst()).doubleValue();
/*      */     }
/*  391 */     if ((this.markOn) && (!this.markedItemQ.isEmpty()) && (this.peekIndex < this.peekCount))
/*      */     {
/*  393 */       return ((Double)this.markedItemQ.get(this.peekIndex++)).doubleValue();
/*      */     }
/*      */     try
/*      */     {
/*  397 */       double d = this.is.readDouble();
/*  398 */       if (this.markOn) {
/*  399 */         this.markedItemQ.addLast(new Double(d));
/*      */       }
/*  401 */       return d;
/*      */     } catch (Exception localException) {
/*  403 */       throw this.wrapper.javaSerializationException(localException, "read_double");
/*      */     }
/*      */   }
/*      */ 
/*      */   public String read_string()
/*      */   {
/*  410 */     if ((!this.markOn) && (!this.markedItemQ.isEmpty())) {
/*  411 */       return (String)this.markedItemQ.removeFirst();
/*      */     }
/*  413 */     if ((this.markOn) && (!this.markedItemQ.isEmpty()) && (this.peekIndex < this.peekCount))
/*      */     {
/*  415 */       return (String)this.markedItemQ.get(this.peekIndex++);
/*      */     }
/*      */     try {
/*  418 */       String str = this.is.readUTF();
/*  419 */       if (this.markOn) {
/*  420 */         this.markedItemQ.addLast(str);
/*      */       }
/*  422 */       return str;
/*      */     } catch (Exception localException) {
/*  424 */       throw this.wrapper.javaSerializationException(localException, "read_string");
/*      */     }
/*      */   }
/*      */ 
/*      */   public String read_wstring() {
/*  429 */     if ((!this.markOn) && (!this.markedItemQ.isEmpty())) {
/*  430 */       return (String)this.markedItemQ.removeFirst();
/*      */     }
/*  432 */     if ((this.markOn) && (!this.markedItemQ.isEmpty()) && (this.peekIndex < this.peekCount))
/*      */     {
/*  434 */       return (String)this.markedItemQ.get(this.peekIndex++);
/*      */     }
/*      */     try {
/*  437 */       String str = (String)this.is.readObject();
/*  438 */       if (this.markOn) {
/*  439 */         this.markedItemQ.addLast(str);
/*      */       }
/*  441 */       return str;
/*      */     } catch (Exception localException) {
/*  443 */       throw this.wrapper.javaSerializationException(localException, "read_wstring");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void read_boolean_array(boolean[] paramArrayOfBoolean, int paramInt1, int paramInt2)
/*      */   {
/*  450 */     for (int i = 0; i < paramInt2; i++)
/*  451 */       paramArrayOfBoolean[(i + paramInt1)] = read_boolean();
/*      */   }
/*      */ 
/*      */   public void read_char_array(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/*  456 */     for (int i = 0; i < paramInt2; i++)
/*  457 */       paramArrayOfChar[(i + paramInt1)] = read_char();
/*      */   }
/*      */ 
/*      */   public void read_wchar_array(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/*  462 */     read_char_array(paramArrayOfChar, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_octet_array(byte[] paramArrayOfByte, int paramInt1, int paramInt2) {
/*  466 */     for (int i = 0; i < paramInt2; i++)
/*  467 */       paramArrayOfByte[(i + paramInt1)] = read_octet();
/*      */   }
/*      */ 
/*      */   public void read_short_array(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*      */   {
/*  483 */     for (int i = 0; i < paramInt2; i++)
/*  484 */       paramArrayOfShort[(i + paramInt1)] = read_short();
/*      */   }
/*      */ 
/*      */   public void read_ushort_array(short[] paramArrayOfShort, int paramInt1, int paramInt2)
/*      */   {
/*  489 */     read_short_array(paramArrayOfShort, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_long_array(int[] paramArrayOfInt, int paramInt1, int paramInt2) {
/*  493 */     for (int i = 0; i < paramInt2; i++)
/*  494 */       paramArrayOfInt[(i + paramInt1)] = read_long();
/*      */   }
/*      */ 
/*      */   public void read_ulong_array(int[] paramArrayOfInt, int paramInt1, int paramInt2)
/*      */   {
/*  499 */     read_long_array(paramArrayOfInt, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_longlong_array(long[] paramArrayOfLong, int paramInt1, int paramInt2) {
/*  503 */     for (int i = 0; i < paramInt2; i++)
/*  504 */       paramArrayOfLong[(i + paramInt1)] = read_longlong();
/*      */   }
/*      */ 
/*      */   public void read_ulonglong_array(long[] paramArrayOfLong, int paramInt1, int paramInt2)
/*      */   {
/*  509 */     read_longlong_array(paramArrayOfLong, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_float_array(float[] paramArrayOfFloat, int paramInt1, int paramInt2) {
/*  513 */     for (int i = 0; i < paramInt2; i++)
/*  514 */       paramArrayOfFloat[(i + paramInt1)] = read_float();
/*      */   }
/*      */ 
/*      */   public void read_double_array(double[] paramArrayOfDouble, int paramInt1, int paramInt2)
/*      */   {
/*  519 */     for (int i = 0; i < paramInt2; i++)
/*  520 */       paramArrayOfDouble[(i + paramInt1)] = read_double();
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object read_Object()
/*      */   {
/*  527 */     return read_Object(null);
/*      */   }
/*      */ 
/*      */   public TypeCode read_TypeCode() {
/*  531 */     TypeCodeImpl localTypeCodeImpl = new TypeCodeImpl(this.orb);
/*  532 */     localTypeCodeImpl.read_value(this.parent);
/*  533 */     return localTypeCodeImpl;
/*      */   }
/*      */ 
/*      */   public Any read_any()
/*      */   {
/*  538 */     Any localAny = this.orb.create_any();
/*  539 */     TypeCodeImpl localTypeCodeImpl = new TypeCodeImpl(this.orb);
/*      */     try
/*      */     {
/*  550 */       localTypeCodeImpl.read_value(this.parent);
/*      */     } catch (MARSHAL localMARSHAL) {
/*  552 */       if (localTypeCodeImpl.kind().value() != 29) {
/*  553 */         throw localMARSHAL;
/*      */       }
/*      */ 
/*  557 */       localMARSHAL.printStackTrace();
/*      */     }
/*      */ 
/*  561 */     localAny.read_value(this.parent, localTypeCodeImpl);
/*      */ 
/*  563 */     return localAny;
/*      */   }
/*      */ 
/*      */   public Principal read_Principal()
/*      */   {
/*  569 */     int i = read_long();
/*  570 */     byte[] arrayOfByte = new byte[i];
/*  571 */     read_octet_array(arrayOfByte, 0, i);
/*  572 */     PrincipalImpl localPrincipalImpl = new PrincipalImpl();
/*  573 */     localPrincipalImpl.name(arrayOfByte);
/*  574 */     return localPrincipalImpl;
/*      */   }
/*      */ 
/*      */   public BigDecimal read_fixed() {
/*  578 */     return new BigDecimal(read_fixed_buffer().toString());
/*      */   }
/*      */ 
/*      */   private StringBuffer read_fixed_buffer()
/*      */   {
/*  589 */     StringBuffer localStringBuffer = new StringBuffer(64);
/*      */ 
/*  593 */     int m = 0;
/*  594 */     int n = 1;
/*  595 */     while (n != 0) {
/*  596 */       int i = read_octet();
/*  597 */       int j = (i & 0xF0) >> 4;
/*  598 */       int k = i & 0xF;
/*  599 */       if ((m != 0) || (j != 0)) {
/*  600 */         localStringBuffer.append(Character.forDigit(j, 10));
/*  601 */         m = 1;
/*      */       }
/*  603 */       if (k == 12)
/*      */       {
/*  605 */         if (m == 0)
/*      */         {
/*  607 */           return new StringBuffer("0.0");
/*      */         }
/*      */ 
/*  612 */         n = 0;
/*  613 */       } else if (k == 13)
/*      */       {
/*  615 */         localStringBuffer.insert(0, '-');
/*  616 */         n = 0;
/*      */       } else {
/*  618 */         localStringBuffer.append(Character.forDigit(k, 10));
/*  619 */         m = 1;
/*      */       }
/*      */     }
/*  622 */     return localStringBuffer;
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object read_Object(Class paramClass)
/*      */   {
/*  628 */     IOR localIOR = IORFactories.makeIOR(this.parent);
/*  629 */     if (localIOR.isNil()) {
/*  630 */       return null;
/*      */     }
/*      */ 
/*  633 */     PresentationManager.StubFactoryFactory localStubFactoryFactory = com.sun.corba.se.spi.orb.ORB.getStubFactoryFactory();
/*      */ 
/*  635 */     String str1 = localIOR.getProfile().getCodebase();
/*  636 */     PresentationManager.StubFactory localStubFactory = null;
/*      */ 
/*  638 */     if (paramClass == null) {
/*  639 */       RepositoryId localRepositoryId = RepositoryId.cache.getId(localIOR.getTypeId());
/*  640 */       String str2 = localRepositoryId.getClassName();
/*  641 */       boolean bool2 = localRepositoryId.isIDLType();
/*      */ 
/*  643 */       if ((str2 == null) || (str2.equals("")))
/*  644 */         localStubFactory = null;
/*      */       else
/*      */         try {
/*  647 */           localStubFactory = localStubFactoryFactory.createStubFactory(str2, bool2, str1, (Class)null, (ClassLoader)null);
/*      */         }
/*      */         catch (Exception localException)
/*      */         {
/*  655 */           localStubFactory = null;
/*      */         }
/*      */     }
/*  658 */     else if (StubAdapter.isStubClass(paramClass)) {
/*  659 */       localStubFactory = PresentationDefaults.makeStaticStubFactory(paramClass);
/*      */     }
/*      */     else {
/*  662 */       boolean bool1 = IDLEntity.class.isAssignableFrom(paramClass);
/*      */ 
/*  664 */       localStubFactory = localStubFactoryFactory.createStubFactory(paramClass.getName(), bool1, str1, paramClass, paramClass.getClassLoader());
/*      */     }
/*      */ 
/*  668 */     return CDRInputStream_1_0.internalIORToObject(localIOR, localStubFactory, this.orb);
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.ORB orb() {
/*  672 */     return this.orb;
/*      */   }
/*      */ 
/*      */   public Serializable read_value()
/*      */   {
/*  678 */     if ((!this.markOn) && (!this.markedItemQ.isEmpty())) {
/*  679 */       return (Serializable)this.markedItemQ.removeFirst();
/*      */     }
/*  681 */     if ((this.markOn) && (!this.markedItemQ.isEmpty()) && (this.peekIndex < this.peekCount))
/*      */     {
/*  683 */       return (Serializable)this.markedItemQ.get(this.peekIndex++);
/*      */     }
/*      */     try {
/*  686 */       Serializable localSerializable = (Serializable)this.is.readObject();
/*  687 */       if (this.markOn) {
/*  688 */         this.markedItemQ.addLast(localSerializable);
/*      */       }
/*  690 */       return localSerializable;
/*      */     } catch (Exception localException) {
/*  692 */       throw this.wrapper.javaSerializationException(localException, "read_value");
/*      */     }
/*      */   }
/*      */ 
/*      */   public Serializable read_value(Class paramClass) {
/*  697 */     return read_value();
/*      */   }
/*      */ 
/*      */   public Serializable read_value(BoxedValueHelper paramBoxedValueHelper)
/*      */   {
/*  702 */     return read_value();
/*      */   }
/*      */ 
/*      */   public Serializable read_value(String paramString) {
/*  706 */     return read_value();
/*      */   }
/*      */ 
/*      */   public Serializable read_value(Serializable paramSerializable) {
/*  710 */     return read_value();
/*      */   }
/*      */ 
/*      */   public java.lang.Object read_abstract_interface() {
/*  714 */     return read_abstract_interface(null);
/*      */   }
/*      */ 
/*      */   public java.lang.Object read_abstract_interface(Class paramClass) {
/*  718 */     boolean bool = read_boolean();
/*  719 */     if (bool) {
/*  720 */       return read_Object(paramClass);
/*      */     }
/*  722 */     return read_value();
/*      */   }
/*      */ 
/*      */   public void consumeEndian()
/*      */   {
/*  728 */     throw this.wrapper.giopVersionError();
/*      */   }
/*      */ 
/*      */   public int getPosition() {
/*      */     try {
/*  733 */       return this.bis.getPosition();
/*      */     } catch (Exception localException) {
/*  735 */       throw this.wrapper.javaSerializationException(localException, "getPosition");
/*      */     }
/*      */   }
/*      */ 
/*      */   public java.lang.Object read_Abstract()
/*      */   {
/*  741 */     return read_abstract_interface();
/*      */   }
/*      */ 
/*      */   public Serializable read_Value() {
/*  745 */     return read_value();
/*      */   }
/*      */ 
/*      */   public void read_any_array(AnySeqHolder paramAnySeqHolder, int paramInt1, int paramInt2)
/*      */   {
/*  750 */     read_any_array(paramAnySeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   private final void read_any_array(Any[] paramArrayOfAny, int paramInt1, int paramInt2)
/*      */   {
/*  755 */     for (int i = 0; i < paramInt2; i++)
/*  756 */       paramArrayOfAny[(i + paramInt1)] = read_any();
/*      */   }
/*      */ 
/*      */   public void read_boolean_array(BooleanSeqHolder paramBooleanSeqHolder, int paramInt1, int paramInt2)
/*      */   {
/*  762 */     read_boolean_array(paramBooleanSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_char_array(CharSeqHolder paramCharSeqHolder, int paramInt1, int paramInt2)
/*      */   {
/*  767 */     read_char_array(paramCharSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_wchar_array(WCharSeqHolder paramWCharSeqHolder, int paramInt1, int paramInt2)
/*      */   {
/*  772 */     read_wchar_array(paramWCharSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_octet_array(OctetSeqHolder paramOctetSeqHolder, int paramInt1, int paramInt2)
/*      */   {
/*  777 */     read_octet_array(paramOctetSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_short_array(ShortSeqHolder paramShortSeqHolder, int paramInt1, int paramInt2)
/*      */   {
/*  782 */     read_short_array(paramShortSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_ushort_array(UShortSeqHolder paramUShortSeqHolder, int paramInt1, int paramInt2)
/*      */   {
/*  787 */     read_ushort_array(paramUShortSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_long_array(LongSeqHolder paramLongSeqHolder, int paramInt1, int paramInt2)
/*      */   {
/*  792 */     read_long_array(paramLongSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_ulong_array(ULongSeqHolder paramULongSeqHolder, int paramInt1, int paramInt2)
/*      */   {
/*  797 */     read_ulong_array(paramULongSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_ulonglong_array(ULongLongSeqHolder paramULongLongSeqHolder, int paramInt1, int paramInt2)
/*      */   {
/*  802 */     read_ulonglong_array(paramULongLongSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_longlong_array(LongLongSeqHolder paramLongLongSeqHolder, int paramInt1, int paramInt2)
/*      */   {
/*  807 */     read_longlong_array(paramLongLongSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_float_array(FloatSeqHolder paramFloatSeqHolder, int paramInt1, int paramInt2)
/*      */   {
/*  812 */     read_float_array(paramFloatSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void read_double_array(DoubleSeqHolder paramDoubleSeqHolder, int paramInt1, int paramInt2)
/*      */   {
/*  817 */     read_double_array(paramDoubleSeqHolder.value, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public String[] _truncatable_ids()
/*      */   {
/*  823 */     throw this.wrapper.giopVersionError();
/*      */   }
/*      */ 
/*      */   public void mark(int paramInt)
/*      */   {
/*  839 */     if ((this.markOn) || (this.is == null)) {
/*  840 */       throw this.wrapper.javaSerializationException("mark");
/*      */     }
/*  842 */     this.markOn = true;
/*  843 */     if (!this.markedItemQ.isEmpty()) {
/*  844 */       this.peekIndex = 0;
/*  845 */       this.peekCount = this.markedItemQ.size();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void reset()
/*      */   {
/*  857 */     this.markOn = false;
/*  858 */     this.peekIndex = 0;
/*  859 */     this.peekCount = 0;
/*      */   }
/*      */ 
/*      */   public boolean markSupported()
/*      */   {
/*  884 */     return true;
/*      */   }
/*      */ 
/*      */   public CDRInputStreamBase dup()
/*      */   {
/*  890 */     CDRInputStreamBase localCDRInputStreamBase = null;
/*      */     try
/*      */     {
/*  893 */       localCDRInputStreamBase = (CDRInputStreamBase)getClass().newInstance();
/*      */     } catch (Exception localException) {
/*  895 */       throw this.wrapper.couldNotDuplicateCdrInputStream(localException);
/*      */     }
/*      */ 
/*  898 */     localCDRInputStreamBase.init(this.orb, this.buffer, this.bufSize, false, null);
/*      */ 
/*  901 */     ((IDLJavaSerializationInputStream)localCDRInputStreamBase).skipBytes(getPosition());
/*      */ 
/*  904 */     ((IDLJavaSerializationInputStream)localCDRInputStreamBase).setMarkData(this.markOn, this.peekIndex, this.peekCount, (LinkedList)this.markedItemQ.clone());
/*      */ 
/*  908 */     return localCDRInputStreamBase;
/*      */   }
/*      */ 
/*      */   void skipBytes(int paramInt)
/*      */   {
/*      */     try {
/*  914 */       this.is.skipBytes(paramInt);
/*      */     } catch (Exception localException) {
/*  916 */       throw this.wrapper.javaSerializationException(localException, "skipBytes");
/*      */     }
/*      */   }
/*      */ 
/*      */   void setMarkData(boolean paramBoolean, int paramInt1, int paramInt2, LinkedList paramLinkedList)
/*      */   {
/*  923 */     this.markOn = paramBoolean;
/*  924 */     this.peekIndex = paramInt1;
/*  925 */     this.peekCount = paramInt2;
/*  926 */     this.markedItemQ = paramLinkedList;
/*      */   }
/*      */ 
/*      */   public BigDecimal read_fixed(short paramShort1, short paramShort2)
/*      */   {
/*  932 */     StringBuffer localStringBuffer = read_fixed_buffer();
/*  933 */     if (paramShort1 != localStringBuffer.length()) {
/*  934 */       throw this.wrapper.badFixed(new Integer(paramShort1), new Integer(localStringBuffer.length()));
/*      */     }
/*  936 */     localStringBuffer.insert(paramShort1 - paramShort2, '.');
/*  937 */     return new BigDecimal(localStringBuffer.toString());
/*      */   }
/*      */ 
/*      */   public boolean isLittleEndian()
/*      */   {
/*  942 */     throw this.wrapper.giopVersionError();
/*      */   }
/*      */ 
/*      */   void setHeaderPadding(boolean paramBoolean)
/*      */   {
/*      */   }
/*      */ 
/*      */   public ByteBuffer getByteBuffer()
/*      */   {
/*  954 */     throw this.wrapper.giopVersionError();
/*      */   }
/*      */ 
/*      */   public void setByteBuffer(ByteBuffer paramByteBuffer) {
/*  958 */     throw this.wrapper.giopVersionError();
/*      */   }
/*      */ 
/*      */   public void setByteBufferWithInfo(ByteBufferWithInfo paramByteBufferWithInfo) {
/*  962 */     throw this.wrapper.giopVersionError();
/*      */   }
/*      */ 
/*      */   public int getBufferLength() {
/*  966 */     return this.bufSize;
/*      */   }
/*      */ 
/*      */   public void setBufferLength(int paramInt)
/*      */   {
/*      */   }
/*      */ 
/*      */   public int getIndex()
/*      */   {
/*  975 */     return this.bis.getPosition();
/*      */   }
/*      */ 
/*      */   public void setIndex(int paramInt) {
/*      */     try {
/*  980 */       this.bis.setPosition(paramInt);
/*      */     } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/*  982 */       throw this.wrapper.javaSerializationException(localIndexOutOfBoundsException, "setIndex");
/*      */     }
/*      */   }
/*      */ 
/*      */   public void orb(org.omg.CORBA.ORB paramORB) {
/*  987 */     this.orb = ((com.sun.corba.se.spi.orb.ORB)paramORB);
/*      */   }
/*      */ 
/*      */   public BufferManagerRead getBufferManager() {
/*  991 */     return this.bufferManager;
/*      */   }
/*      */ 
/*      */   public GIOPVersion getGIOPVersion() {
/*  995 */     return GIOPVersion.V1_2;
/*      */   }
/*      */ 
/*      */   CodeBase getCodeBase() {
/*  999 */     return this.parent.getCodeBase();
/*      */   }
/*      */ 
/*      */   void printBuffer() {
/* 1003 */     byte[] arrayOfByte = this.buffer.array();
/*      */ 
/* 1005 */     System.out.println("+++++++ Input Buffer ++++++++");
/* 1006 */     System.out.println();
/* 1007 */     System.out.println("Current position: " + getPosition());
/* 1008 */     System.out.println("Total length : " + this.bufSize);
/* 1009 */     System.out.println();
/*      */ 
/* 1011 */     char[] arrayOfChar = new char[16];
/*      */     try
/*      */     {
/* 1015 */       for (int i = 0; i < arrayOfByte.length; i += 16)
/*      */       {
/* 1017 */         int j = 0;
/*      */ 
/* 1023 */         while ((j < 16) && (j + i < arrayOfByte.length)) {
/* 1024 */           k = arrayOfByte[(i + j)];
/* 1025 */           if (k < 0)
/* 1026 */             k = 256 + k;
/* 1027 */           String str = Integer.toHexString(k);
/* 1028 */           if (str.length() == 1)
/* 1029 */             str = "0" + str;
/* 1030 */           System.out.print(str + " ");
/* 1031 */           j++;
/*      */         }
/*      */ 
/* 1037 */         while (j < 16) {
/* 1038 */           System.out.print("   ");
/* 1039 */           j++;
/*      */         }
/*      */ 
/* 1044 */         int k = 0;
/*      */ 
/* 1046 */         while ((k < 16) && (k + i < arrayOfByte.length)) {
/* 1047 */           if (ORBUtility.isPrintable((char)arrayOfByte[(i + k)]))
/* 1048 */             arrayOfChar[k] = ((char)arrayOfByte[(i + k)]);
/*      */           else {
/* 1050 */             arrayOfChar[k] = '.';
/*      */           }
/* 1052 */           k++;
/*      */         }
/* 1054 */         System.out.println(new String(arrayOfChar, 0, k));
/*      */       }
/*      */     } catch (Throwable localThrowable) {
/* 1057 */       localThrowable.printStackTrace();
/*      */     }
/* 1059 */     System.out.println("++++++++++++++++++++++++++++++");
/*      */   }
/*      */ 
/*      */   void alignOnBoundary(int paramInt) {
/* 1063 */     throw this.wrapper.giopVersionError();
/*      */   }
/*      */ 
/*      */   void performORBVersionSpecificInit()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void resetCodeSetConverters()
/*      */   {
/*      */   }
/*      */ 
/*      */   public void start_value()
/*      */   {
/* 1077 */     throw this.wrapper.giopVersionError();
/*      */   }
/*      */ 
/*      */   public void end_value() {
/* 1081 */     throw this.wrapper.giopVersionError();
/*      */   }
/*      */ 
/*      */   class MarshalObjectInputStream extends ObjectInputStream
/*      */   {
/*      */     com.sun.corba.se.spi.orb.ORB orb;
/*      */ 
/*      */     MarshalObjectInputStream(InputStream paramORB, com.sun.corba.se.spi.orb.ORB arg3)
/*      */       throws IOException
/*      */     {
/*  121 */       super();
/*      */       java.lang.Object localObject;
/*  122 */       this.orb = localObject;
/*      */ 
/*  124 */       AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public java.lang.Object run()
/*      */         {
/*  128 */           IDLJavaSerializationInputStream.MarshalObjectInputStream.this.enableResolveObject(true);
/*  129 */           return null;
/*      */         }
/*      */       });
/*      */     }
/*      */ 
/*      */     protected final java.lang.Object resolveObject(java.lang.Object paramObject)
/*      */       throws IOException
/*      */     {
/*      */       try
/*      */       {
/*  140 */         if (StubAdapter.isStub(paramObject))
/*  141 */           StubAdapter.connect(paramObject, this.orb);
/*      */       }
/*      */       catch (RemoteException localRemoteException) {
/*  144 */         IOException localIOException = new IOException("resolveObject failed");
/*  145 */         localIOException.initCause(localRemoteException);
/*  146 */         throw localIOException;
/*      */       }
/*  148 */       return paramObject;
/*      */     }
/*      */   }
/*      */ 
/*      */   class _ByteArrayInputStream extends ByteArrayInputStream
/*      */   {
/*      */     _ByteArrayInputStream(byte[] arg2)
/*      */     {
/*   99 */       super();
/*      */     }
/*      */ 
/*      */     int getPosition() {
/*  103 */       return this.pos;
/*      */     }
/*      */ 
/*      */     void setPosition(int paramInt) {
/*  107 */       if ((paramInt < 0) || (paramInt > this.count)) {
/*  108 */         throw new IndexOutOfBoundsException();
/*      */       }
/*  110 */       this.pos = paramInt;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.IDLJavaSerializationInputStream
 * JD-Core Version:    0.6.2
 */