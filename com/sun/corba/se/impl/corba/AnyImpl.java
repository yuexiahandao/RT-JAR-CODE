/*      */ package com.sun.corba.se.impl.corba;
/*      */ 
/*      */ import com.sun.corba.se.impl.encoding.CDRInputStream;
/*      */ import com.sun.corba.se.impl.encoding.EncapsInputStream;
/*      */ import com.sun.corba.se.impl.encoding.EncapsOutputStream;
/*      */ import com.sun.corba.se.impl.io.ValueUtility;
/*      */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*      */ import com.sun.corba.se.impl.orbutil.ORBUtility;
/*      */ import com.sun.corba.se.impl.orbutil.RepositoryIdFactory;
/*      */ import com.sun.corba.se.impl.orbutil.RepositoryIdStrings;
/*      */ import com.sun.corba.se.spi.orb.ORB;
/*      */ import com.sun.corba.se.spi.orb.ORBVersion;
/*      */ import com.sun.corba.se.spi.orb.ORBVersionFactory;
/*      */ import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
/*      */ import java.io.Serializable;
/*      */ import java.math.BigDecimal;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.ArrayList;
/*      */ import java.util.List;
/*      */ import org.omg.CORBA.Any;
/*      */ import org.omg.CORBA.CompletionStatus;
/*      */ import org.omg.CORBA.Principal;
/*      */ import org.omg.CORBA.TCKind;
/*      */ import org.omg.CORBA.TypeCode;
/*      */ import org.omg.CORBA.TypeCodePackage.BadKind;
/*      */ import org.omg.CORBA.TypeCodePackage.Bounds;
/*      */ import org.omg.CORBA.portable.Streamable;
/*      */ 
/*      */ public class AnyImpl extends Any
/*      */ {
/*      */   private TypeCodeImpl typeCode;
/*      */   protected ORB orb;
/*      */   private ORBUtilSystemException wrapper;
/*      */   private CDRInputStream stream;
/*      */   private long value;
/*      */   private java.lang.Object object;
/*  122 */   private boolean isInitialized = false;
/*      */   private static final int DEFAULT_BUFFER_SIZE = 32;
/*  130 */   static boolean[] isStreamed = { false, false, false, false, false, false, false, false, false, false, false, false, false, true, false, true, true, false, false, true, true, true, true, false, false, false, false, false, false, false, false, false, false };
/*      */ 
/*      */   static AnyImpl convertToNative(ORB paramORB, Any paramAny)
/*      */   {
/*  167 */     if ((paramAny instanceof AnyImpl)) {
/*  168 */       return (AnyImpl)paramAny;
/*      */     }
/*  170 */     AnyImpl localAnyImpl = new AnyImpl(paramORB, paramAny);
/*  171 */     localAnyImpl.typeCode = TypeCodeImpl.convertToNative(paramORB, localAnyImpl.typeCode);
/*  172 */     return localAnyImpl;
/*      */   }
/*      */ 
/*      */   public AnyImpl(ORB paramORB)
/*      */   {
/*  186 */     this.orb = paramORB;
/*  187 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.presentation");
/*      */ 
/*  190 */     this.typeCode = paramORB.get_primitive_tc(0);
/*  191 */     this.stream = null;
/*  192 */     this.object = null;
/*  193 */     this.value = 0L;
/*      */ 
/*  195 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public AnyImpl(ORB paramORB, Any paramAny)
/*      */   {
/*  202 */     this(paramORB);
/*      */ 
/*  204 */     if ((paramAny instanceof AnyImpl)) {
/*  205 */       AnyImpl localAnyImpl = (AnyImpl)paramAny;
/*  206 */       this.typeCode = localAnyImpl.typeCode;
/*  207 */       this.value = localAnyImpl.value;
/*  208 */       this.object = localAnyImpl.object;
/*  209 */       this.isInitialized = localAnyImpl.isInitialized;
/*      */ 
/*  211 */       if (localAnyImpl.stream != null)
/*  212 */         this.stream = localAnyImpl.stream.dup();
/*      */     }
/*      */     else {
/*  215 */       read_value(paramAny.create_input_stream(), paramAny.type());
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCode type()
/*      */   {
/*  228 */     return this.typeCode;
/*      */   }
/*      */ 
/*      */   private TypeCode realType() {
/*  232 */     return realType(this.typeCode);
/*      */   }
/*      */ 
/*      */   private TypeCode realType(TypeCode paramTypeCode) {
/*  236 */     TypeCode localTypeCode = paramTypeCode;
/*      */     try
/*      */     {
/*  239 */       while (localTypeCode.kind().value() == 21)
/*  240 */         localTypeCode = localTypeCode.content_type();
/*      */     }
/*      */     catch (BadKind localBadKind) {
/*  243 */       throw this.wrapper.badkindCannotOccur(localBadKind);
/*      */     }
/*  245 */     return localTypeCode;
/*      */   }
/*      */ 
/*      */   public void type(TypeCode paramTypeCode)
/*      */   {
/*  257 */     this.typeCode = TypeCodeImpl.convertToNative(this.orb, paramTypeCode);
/*      */ 
/*  259 */     this.stream = null;
/*  260 */     this.value = 0L;
/*  261 */     this.object = null;
/*      */ 
/*  263 */     this.isInitialized = (paramTypeCode.kind().value() == 0);
/*      */   }
/*      */ 
/*      */   public boolean equal(Any paramAny)
/*      */   {
/*  276 */     if (paramAny == this) {
/*  277 */       return true;
/*      */     }
/*      */ 
/*  281 */     if (!this.typeCode.equal(paramAny.type())) {
/*  282 */       return false;
/*      */     }
/*      */ 
/*  285 */     TypeCode localTypeCode = realType();
/*      */ 
/*  299 */     switch (localTypeCode.kind().value())
/*      */     {
/*      */     case 0:
/*      */     case 1:
/*  303 */       return true;
/*      */     case 2:
/*  305 */       return extract_short() == paramAny.extract_short();
/*      */     case 3:
/*  307 */       return extract_long() == paramAny.extract_long();
/*      */     case 4:
/*  309 */       return extract_ushort() == paramAny.extract_ushort();
/*      */     case 5:
/*  311 */       return extract_ulong() == paramAny.extract_ulong();
/*      */     case 6:
/*  313 */       return extract_float() == paramAny.extract_float();
/*      */     case 7:
/*  315 */       return extract_double() == paramAny.extract_double();
/*      */     case 8:
/*  317 */       return extract_boolean() == paramAny.extract_boolean();
/*      */     case 9:
/*  319 */       return extract_char() == paramAny.extract_char();
/*      */     case 26:
/*  321 */       return extract_wchar() == paramAny.extract_wchar();
/*      */     case 10:
/*  323 */       return extract_octet() == paramAny.extract_octet();
/*      */     case 11:
/*  325 */       return extract_any().equal(paramAny.extract_any());
/*      */     case 12:
/*  327 */       return extract_TypeCode().equal(paramAny.extract_TypeCode());
/*      */     case 18:
/*  329 */       return extract_string().equals(paramAny.extract_string());
/*      */     case 27:
/*  331 */       return extract_wstring().equals(paramAny.extract_wstring());
/*      */     case 23:
/*  333 */       return extract_longlong() == paramAny.extract_longlong();
/*      */     case 24:
/*  335 */       return extract_ulonglong() == paramAny.extract_ulonglong();
/*      */     case 14:
/*  338 */       return extract_Object().equals(paramAny.extract_Object());
/*      */     case 13:
/*  340 */       return extract_Principal().equals(paramAny.extract_Principal());
/*      */     case 17:
/*  343 */       return extract_long() == paramAny.extract_long();
/*      */     case 28:
/*  345 */       return extract_fixed().compareTo(paramAny.extract_fixed()) == 0;
/*      */     case 15:
/*      */     case 16:
/*      */     case 19:
/*      */     case 20:
/*      */     case 22:
/*  351 */       org.omg.CORBA.portable.InputStream localInputStream1 = create_input_stream();
/*  352 */       org.omg.CORBA.portable.InputStream localInputStream2 = paramAny.create_input_stream();
/*  353 */       return equalMember(localTypeCode, localInputStream1, localInputStream2);
/*      */     case 29:
/*      */     case 30:
/*  360 */       return extract_Value().equals(paramAny.extract_Value());
/*      */     case 21:
/*  363 */       throw this.wrapper.errorResolvingAlias();
/*      */     case 25:
/*  367 */       throw this.wrapper.tkLongDoubleNotSupported();
/*      */     }
/*      */ 
/*  370 */     throw this.wrapper.typecodeNotSupported();
/*      */   }
/*      */ 
/*      */   private boolean equalMember(TypeCode paramTypeCode, org.omg.CORBA.portable.InputStream paramInputStream1, org.omg.CORBA.portable.InputStream paramInputStream2)
/*      */   {
/*  378 */     TypeCode localTypeCode = realType(paramTypeCode);
/*      */     try
/*      */     {
/*      */       int j;
/*      */       int m;
/*  381 */       switch (localTypeCode.kind().value())
/*      */       {
/*      */       case 0:
/*      */       case 1:
/*  385 */         return true;
/*      */       case 2:
/*  387 */         return paramInputStream1.read_short() == paramInputStream2.read_short();
/*      */       case 3:
/*  389 */         return paramInputStream1.read_long() == paramInputStream2.read_long();
/*      */       case 4:
/*  391 */         return paramInputStream1.read_ushort() == paramInputStream2.read_ushort();
/*      */       case 5:
/*  393 */         return paramInputStream1.read_ulong() == paramInputStream2.read_ulong();
/*      */       case 6:
/*  395 */         return paramInputStream1.read_float() == paramInputStream2.read_float();
/*      */       case 7:
/*  397 */         return paramInputStream1.read_double() == paramInputStream2.read_double();
/*      */       case 8:
/*  399 */         return paramInputStream1.read_boolean() == paramInputStream2.read_boolean();
/*      */       case 9:
/*  401 */         return paramInputStream1.read_char() == paramInputStream2.read_char();
/*      */       case 26:
/*  403 */         return paramInputStream1.read_wchar() == paramInputStream2.read_wchar();
/*      */       case 10:
/*  405 */         return paramInputStream1.read_octet() == paramInputStream2.read_octet();
/*      */       case 11:
/*  407 */         return paramInputStream1.read_any().equal(paramInputStream2.read_any());
/*      */       case 12:
/*  409 */         return paramInputStream1.read_TypeCode().equal(paramInputStream2.read_TypeCode());
/*      */       case 18:
/*  411 */         return paramInputStream1.read_string().equals(paramInputStream2.read_string());
/*      */       case 27:
/*  413 */         return paramInputStream1.read_wstring().equals(paramInputStream2.read_wstring());
/*      */       case 23:
/*  415 */         return paramInputStream1.read_longlong() == paramInputStream2.read_longlong();
/*      */       case 24:
/*  417 */         return paramInputStream1.read_ulonglong() == paramInputStream2.read_ulonglong();
/*      */       case 14:
/*  420 */         return paramInputStream1.read_Object().equals(paramInputStream2.read_Object());
/*      */       case 13:
/*  422 */         return paramInputStream1.read_Principal().equals(paramInputStream2.read_Principal());
/*      */       case 17:
/*  425 */         return paramInputStream1.read_long() == paramInputStream2.read_long();
/*      */       case 28:
/*  427 */         return paramInputStream1.read_fixed().compareTo(paramInputStream2.read_fixed()) == 0;
/*      */       case 15:
/*      */       case 22:
/*  430 */         int i = localTypeCode.member_count();
/*  431 */         for (int k = 0; k < i; k++) {
/*  432 */           if (!equalMember(localTypeCode.member_type(k), paramInputStream1, paramInputStream2)) {
/*  433 */             return false;
/*      */           }
/*      */         }
/*  436 */         return true;
/*      */       case 16:
/*  439 */         Any localAny1 = this.orb.create_any();
/*  440 */         Any localAny2 = this.orb.create_any();
/*  441 */         localAny1.read_value(paramInputStream1, localTypeCode.discriminator_type());
/*  442 */         localAny2.read_value(paramInputStream2, localTypeCode.discriminator_type());
/*      */ 
/*  444 */         if (!localAny1.equal(localAny2)) {
/*  445 */           return false;
/*      */         }
/*  447 */         TypeCodeImpl localTypeCodeImpl = TypeCodeImpl.convertToNative(this.orb, localTypeCode);
/*  448 */         int n = localTypeCodeImpl.currentUnionMemberIndex(localAny1);
/*  449 */         if (n == -1) {
/*  450 */           throw this.wrapper.unionDiscriminatorError();
/*      */         }
/*  452 */         if (!equalMember(localTypeCode.member_type(n), paramInputStream1, paramInputStream2)) {
/*  453 */           return false;
/*      */         }
/*  455 */         return true;
/*      */       case 19:
/*  458 */         j = paramInputStream1.read_long();
/*  459 */         paramInputStream2.read_long();
/*  460 */         for (m = 0; m < j; m++) {
/*  461 */           if (!equalMember(localTypeCode.content_type(), paramInputStream1, paramInputStream2)) {
/*  462 */             return false;
/*      */           }
/*      */         }
/*  465 */         return true;
/*      */       case 20:
/*  468 */         j = localTypeCode.member_count();
/*  469 */         for (m = 0; m < j; m++) {
/*  470 */           if (!equalMember(localTypeCode.content_type(), paramInputStream1, paramInputStream2)) {
/*  471 */             return false;
/*      */           }
/*      */         }
/*  474 */         return true;
/*      */       case 29:
/*      */       case 30:
/*  482 */         org.omg.CORBA_2_3.portable.InputStream localInputStream1 = (org.omg.CORBA_2_3.portable.InputStream)paramInputStream1;
/*      */ 
/*  484 */         org.omg.CORBA_2_3.portable.InputStream localInputStream2 = (org.omg.CORBA_2_3.portable.InputStream)paramInputStream2;
/*      */ 
/*  486 */         return localInputStream1.read_value().equals(localInputStream2.read_value());
/*      */       case 21:
/*  490 */         throw this.wrapper.errorResolvingAlias();
/*      */       case 25:
/*  493 */         throw this.wrapper.tkLongDoubleNotSupported();
/*      */       }
/*      */ 
/*  496 */       throw this.wrapper.typecodeNotSupported();
/*      */     }
/*      */     catch (BadKind localBadKind) {
/*  499 */       throw this.wrapper.badkindCannotOccur(); } catch (Bounds localBounds) {
/*      */     }
/*  501 */     throw this.wrapper.boundsCannotOccur();
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.portable.OutputStream create_output_stream()
/*      */   {
/*  516 */     final ORB localORB = this.orb;
/*  517 */     return (org.omg.CORBA.portable.OutputStream)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public AnyImpl.AnyOutputStream run() {
/*  520 */         return new AnyImpl.AnyOutputStream(localORB);
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.portable.InputStream create_input_stream()
/*      */   {
/*  537 */     if (isStreamed[realType().kind().value()] != 0) {
/*  538 */       return this.stream.dup();
/*      */     }
/*  540 */     org.omg.CORBA.portable.OutputStream localOutputStream = this.orb.create_output_stream();
/*  541 */     TCUtility.marshalIn(localOutputStream, realType(), this.value, this.object);
/*      */ 
/*  543 */     return localOutputStream.create_input_stream();
/*      */   }
/*      */ 
/*      */   public void read_value(org.omg.CORBA.portable.InputStream paramInputStream, TypeCode paramTypeCode)
/*      */   {
/*  569 */     this.typeCode = TypeCodeImpl.convertToNative(this.orb, paramTypeCode);
/*  570 */     int i = realType().kind().value();
/*  571 */     if (i >= isStreamed.length)
/*  572 */       throw this.wrapper.invalidIsstreamedTckind(CompletionStatus.COMPLETED_MAYBE, new Integer(i));
/*      */     java.lang.Object localObject;
/*  576 */     if (isStreamed[i] != 0) {
/*  577 */       if ((paramInputStream instanceof AnyInputStream))
/*      */       {
/*  579 */         this.stream = ((CDRInputStream)paramInputStream);
/*      */       } else {
/*  581 */         localObject = (org.omg.CORBA_2_3.portable.OutputStream)this.orb.create_output_stream();
/*      */ 
/*  583 */         this.typeCode.copy((org.omg.CORBA_2_3.portable.InputStream)paramInputStream, (org.omg.CORBA.portable.OutputStream)localObject);
/*  584 */         this.stream = ((CDRInputStream)((org.omg.CORBA_2_3.portable.OutputStream)localObject).create_input_stream());
/*      */       }
/*      */     } else {
/*  587 */       localObject = new java.lang.Object[1];
/*  588 */       localObject[0] = this.object;
/*  589 */       long[] arrayOfLong = new long[1];
/*  590 */       TCUtility.unmarshalIn(paramInputStream, realType(), arrayOfLong, (java.lang.Object[])localObject);
/*  591 */       this.value = arrayOfLong[0];
/*  592 */       this.object = localObject[0];
/*  593 */       this.stream = null;
/*      */     }
/*  595 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public void write_value(org.omg.CORBA.portable.OutputStream paramOutputStream)
/*      */   {
/*  609 */     if (isStreamed[realType().kind().value()] != 0) {
/*  610 */       this.typeCode.copy(this.stream.dup(), paramOutputStream);
/*      */     }
/*      */     else
/*  613 */       TCUtility.marshalIn(paramOutputStream, realType(), this.value, this.object);
/*      */   }
/*      */ 
/*      */   public void insert_Streamable(Streamable paramStreamable)
/*      */   {
/*  625 */     this.typeCode = TypeCodeImpl.convertToNative(this.orb, paramStreamable._type());
/*  626 */     this.object = paramStreamable;
/*  627 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public Streamable extract_Streamable()
/*      */   {
/*  633 */     return (Streamable)this.object;
/*      */   }
/*      */ 
/*      */   public void insert_short(short paramShort)
/*      */   {
/*  645 */     this.typeCode = this.orb.get_primitive_tc(2);
/*  646 */     this.value = paramShort;
/*  647 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   private String getTCKindName(int paramInt)
/*      */   {
/*  652 */     if ((paramInt >= 0) && (paramInt < TypeCodeImpl.kindNames.length)) {
/*  653 */       return TypeCodeImpl.kindNames[paramInt];
/*      */     }
/*  655 */     return "UNKNOWN(" + paramInt + ")";
/*      */   }
/*      */ 
/*      */   private void checkExtractBadOperation(int paramInt)
/*      */   {
/*  660 */     if (!this.isInitialized) {
/*  661 */       throw this.wrapper.extractNotInitialized();
/*      */     }
/*  663 */     int i = realType().kind().value();
/*  664 */     if (i != paramInt) {
/*  665 */       String str1 = getTCKindName(i);
/*  666 */       String str2 = getTCKindName(paramInt);
/*  667 */       throw this.wrapper.extractWrongType(str2, str1);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void checkExtractBadOperationList(int[] paramArrayOfInt)
/*      */   {
/*  673 */     if (!this.isInitialized) {
/*  674 */       throw this.wrapper.extractNotInitialized();
/*      */     }
/*  676 */     int i = realType().kind().value();
/*  677 */     for (int j = 0; j < paramArrayOfInt.length; j++) {
/*  678 */       if (i == paramArrayOfInt[j])
/*  679 */         return;
/*      */     }
/*  681 */     ArrayList localArrayList = new ArrayList();
/*  682 */     for (int k = 0; k < paramArrayOfInt.length; k++) {
/*  683 */       localArrayList.add(getTCKindName(paramArrayOfInt[k]));
/*      */     }
/*  685 */     String str = getTCKindName(i);
/*  686 */     throw this.wrapper.extractWrongTypeList(localArrayList, str);
/*      */   }
/*      */ 
/*      */   public short extract_short()
/*      */   {
/*  695 */     checkExtractBadOperation(2);
/*  696 */     return (short)(int)this.value;
/*      */   }
/*      */ 
/*      */   public void insert_long(int paramInt)
/*      */   {
/*  707 */     int i = realType().kind().value();
/*  708 */     if ((i != 3) && (i != 17)) {
/*  709 */       this.typeCode = this.orb.get_primitive_tc(3);
/*      */     }
/*  711 */     this.value = paramInt;
/*  712 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public int extract_long()
/*      */   {
/*  721 */     checkExtractBadOperationList(new int[] { 3, 17 });
/*  722 */     return (int)this.value;
/*      */   }
/*      */ 
/*      */   public void insert_ushort(short paramShort)
/*      */   {
/*  731 */     this.typeCode = this.orb.get_primitive_tc(4);
/*  732 */     this.value = paramShort;
/*  733 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public short extract_ushort()
/*      */   {
/*  742 */     checkExtractBadOperation(4);
/*  743 */     return (short)(int)this.value;
/*      */   }
/*      */ 
/*      */   public void insert_ulong(int paramInt)
/*      */   {
/*  752 */     this.typeCode = this.orb.get_primitive_tc(5);
/*  753 */     this.value = paramInt;
/*  754 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public int extract_ulong()
/*      */   {
/*  763 */     checkExtractBadOperation(5);
/*  764 */     return (int)this.value;
/*      */   }
/*      */ 
/*      */   public void insert_float(float paramFloat)
/*      */   {
/*  773 */     this.typeCode = this.orb.get_primitive_tc(6);
/*  774 */     this.value = Float.floatToIntBits(paramFloat);
/*  775 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public float extract_float()
/*      */   {
/*  784 */     checkExtractBadOperation(6);
/*  785 */     return Float.intBitsToFloat((int)this.value);
/*      */   }
/*      */ 
/*      */   public void insert_double(double paramDouble)
/*      */   {
/*  794 */     this.typeCode = this.orb.get_primitive_tc(7);
/*  795 */     this.value = Double.doubleToLongBits(paramDouble);
/*  796 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public double extract_double()
/*      */   {
/*  805 */     checkExtractBadOperation(7);
/*  806 */     return Double.longBitsToDouble(this.value);
/*      */   }
/*      */ 
/*      */   public void insert_longlong(long paramLong)
/*      */   {
/*  815 */     this.typeCode = this.orb.get_primitive_tc(23);
/*  816 */     this.value = paramLong;
/*  817 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public long extract_longlong()
/*      */   {
/*  826 */     checkExtractBadOperation(23);
/*  827 */     return this.value;
/*      */   }
/*      */ 
/*      */   public void insert_ulonglong(long paramLong)
/*      */   {
/*  836 */     this.typeCode = this.orb.get_primitive_tc(24);
/*  837 */     this.value = paramLong;
/*  838 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public long extract_ulonglong()
/*      */   {
/*  847 */     checkExtractBadOperation(24);
/*  848 */     return this.value;
/*      */   }
/*      */ 
/*      */   public void insert_boolean(boolean paramBoolean)
/*      */   {
/*  857 */     this.typeCode = this.orb.get_primitive_tc(8);
/*  858 */     this.value = (paramBoolean ? 1L : 0L);
/*  859 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public boolean extract_boolean()
/*      */   {
/*  868 */     checkExtractBadOperation(8);
/*  869 */     return this.value != 0L;
/*      */   }
/*      */ 
/*      */   public void insert_char(char paramChar)
/*      */   {
/*  878 */     this.typeCode = this.orb.get_primitive_tc(9);
/*  879 */     this.value = paramChar;
/*  880 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public char extract_char()
/*      */   {
/*  889 */     checkExtractBadOperation(9);
/*  890 */     return (char)(int)this.value;
/*      */   }
/*      */ 
/*      */   public void insert_wchar(char paramChar)
/*      */   {
/*  899 */     this.typeCode = this.orb.get_primitive_tc(26);
/*  900 */     this.value = paramChar;
/*  901 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public char extract_wchar()
/*      */   {
/*  910 */     checkExtractBadOperation(26);
/*  911 */     return (char)(int)this.value;
/*      */   }
/*      */ 
/*      */   public void insert_octet(byte paramByte)
/*      */   {
/*  921 */     this.typeCode = this.orb.get_primitive_tc(10);
/*  922 */     this.value = paramByte;
/*  923 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public byte extract_octet()
/*      */   {
/*  932 */     checkExtractBadOperation(10);
/*  933 */     return (byte)(int)this.value;
/*      */   }
/*      */ 
/*      */   public void insert_string(String paramString)
/*      */   {
/*  943 */     if (this.typeCode.kind() == TCKind.tk_string) {
/*  944 */       int i = 0;
/*      */       try {
/*  946 */         i = this.typeCode.length();
/*      */       } catch (BadKind localBadKind) {
/*  948 */         throw this.wrapper.badkindCannotOccur();
/*      */       }
/*      */ 
/*  952 */       if ((i != 0) && (paramString != null) && (paramString.length() > i))
/*  953 */         throw this.wrapper.badStringBounds(new Integer(paramString.length()), new Integer(i));
/*      */     }
/*      */     else
/*      */     {
/*  957 */       this.typeCode = this.orb.get_primitive_tc(18);
/*      */     }
/*  959 */     this.object = paramString;
/*  960 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public String extract_string()
/*      */   {
/*  969 */     checkExtractBadOperation(18);
/*  970 */     return (String)this.object;
/*      */   }
/*      */ 
/*      */   public void insert_wstring(String paramString)
/*      */   {
/*  980 */     if (this.typeCode.kind() == TCKind.tk_wstring) {
/*  981 */       int i = 0;
/*      */       try {
/*  983 */         i = this.typeCode.length();
/*      */       } catch (BadKind localBadKind) {
/*  985 */         throw this.wrapper.badkindCannotOccur();
/*      */       }
/*      */ 
/*  989 */       if ((i != 0) && (paramString != null) && (paramString.length() > i))
/*  990 */         throw this.wrapper.badStringBounds(new Integer(paramString.length()), new Integer(i));
/*      */     }
/*      */     else
/*      */     {
/*  994 */       this.typeCode = this.orb.get_primitive_tc(27);
/*      */     }
/*  996 */     this.object = paramString;
/*  997 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public String extract_wstring()
/*      */   {
/* 1006 */     checkExtractBadOperation(27);
/* 1007 */     return (String)this.object;
/*      */   }
/*      */ 
/*      */   public void insert_any(Any paramAny)
/*      */   {
/* 1016 */     this.typeCode = this.orb.get_primitive_tc(11);
/* 1017 */     this.object = paramAny;
/* 1018 */     this.stream = null;
/* 1019 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public Any extract_any()
/*      */   {
/* 1028 */     checkExtractBadOperation(11);
/* 1029 */     return (Any)this.object;
/*      */   }
/*      */ 
/*      */   public void insert_Object(org.omg.CORBA.Object paramObject)
/*      */   {
/* 1038 */     if (paramObject == null) {
/* 1039 */       this.typeCode = this.orb.get_primitive_tc(14);
/*      */     }
/* 1041 */     else if (StubAdapter.isStub(paramObject)) {
/* 1042 */       String[] arrayOfString = StubAdapter.getTypeIds(paramObject);
/* 1043 */       this.typeCode = new TypeCodeImpl(this.orb, 14, arrayOfString[0], "");
/*      */     } else {
/* 1045 */       throw this.wrapper.badInsertobjParam(CompletionStatus.COMPLETED_MAYBE, paramObject.getClass().getName());
/*      */     }
/*      */ 
/* 1050 */     this.object = paramObject;
/* 1051 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public void insert_Object(org.omg.CORBA.Object paramObject, TypeCode paramTypeCode)
/*      */   {
/*      */     try
/*      */     {
/* 1062 */       if ((paramTypeCode.id().equals("IDL:omg.org/CORBA/Object:1.0")) || (paramObject._is_a(paramTypeCode.id())))
/*      */       {
/* 1064 */         this.typeCode = TypeCodeImpl.convertToNative(this.orb, paramTypeCode);
/* 1065 */         this.object = paramObject;
/*      */       }
/*      */       else {
/* 1068 */         throw this.wrapper.insertObjectIncompatible();
/*      */       }
/*      */     } catch (Exception localException) {
/* 1071 */       throw this.wrapper.insertObjectFailed(localException);
/*      */     }
/* 1073 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public org.omg.CORBA.Object extract_Object()
/*      */   {
/* 1082 */     if (!this.isInitialized) {
/* 1083 */       throw this.wrapper.extractNotInitialized();
/*      */     }
/*      */ 
/* 1086 */     org.omg.CORBA.Object localObject = null;
/*      */     try {
/* 1088 */       localObject = (org.omg.CORBA.Object)this.object;
/* 1089 */       if ((this.typeCode.id().equals("IDL:omg.org/CORBA/Object:1.0")) || (localObject._is_a(this.typeCode.id()))) {
/* 1090 */         return localObject;
/*      */       }
/* 1092 */       throw this.wrapper.extractObjectIncompatible();
/*      */     }
/*      */     catch (Exception localException) {
/* 1095 */       throw this.wrapper.extractObjectFailed(localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void insert_TypeCode(TypeCode paramTypeCode)
/*      */   {
/* 1105 */     this.typeCode = this.orb.get_primitive_tc(12);
/* 1106 */     this.object = paramTypeCode;
/* 1107 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public TypeCode extract_TypeCode()
/*      */   {
/* 1116 */     checkExtractBadOperation(12);
/* 1117 */     return (TypeCode)this.object;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public void insert_Principal(Principal paramPrincipal)
/*      */   {
/* 1126 */     this.typeCode = this.orb.get_primitive_tc(13);
/* 1127 */     this.object = paramPrincipal;
/* 1128 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public Principal extract_Principal()
/*      */   {
/* 1137 */     checkExtractBadOperation(13);
/* 1138 */     return (Principal)this.object;
/*      */   }
/*      */ 
/*      */   public Serializable extract_Value()
/*      */   {
/* 1150 */     checkExtractBadOperationList(new int[] { 29, 30, 32 });
/*      */ 
/* 1152 */     return (Serializable)this.object;
/*      */   }
/*      */ 
/*      */   public void insert_Value(Serializable paramSerializable)
/*      */   {
/* 1158 */     this.object = paramSerializable;
/*      */     TypeCode localTypeCode;
/* 1162 */     if (paramSerializable == null) {
/* 1163 */       localTypeCode = this.orb.get_primitive_tc(TCKind.tk_value);
/*      */     }
/*      */     else
/*      */     {
/* 1174 */       localTypeCode = createTypeCodeForClass(paramSerializable.getClass(), (ORB)ORB.init());
/*      */     }
/*      */ 
/* 1177 */     this.typeCode = TypeCodeImpl.convertToNative(this.orb, localTypeCode);
/* 1178 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public void insert_Value(Serializable paramSerializable, TypeCode paramTypeCode)
/*      */   {
/* 1184 */     this.object = paramSerializable;
/* 1185 */     this.typeCode = TypeCodeImpl.convertToNative(this.orb, paramTypeCode);
/* 1186 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public void insert_fixed(BigDecimal paramBigDecimal) {
/* 1190 */     this.typeCode = TypeCodeImpl.convertToNative(this.orb, this.orb.create_fixed_tc(TypeCodeImpl.digits(paramBigDecimal), TypeCodeImpl.scale(paramBigDecimal)));
/*      */ 
/* 1192 */     this.object = paramBigDecimal;
/* 1193 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public void insert_fixed(BigDecimal paramBigDecimal, TypeCode paramTypeCode)
/*      */   {
/*      */     try {
/* 1199 */       if ((TypeCodeImpl.digits(paramBigDecimal) > paramTypeCode.fixed_digits()) || (TypeCodeImpl.scale(paramBigDecimal) > paramTypeCode.fixed_scale()))
/*      */       {
/* 1202 */         throw this.wrapper.fixedNotMatch();
/*      */       }
/*      */     }
/*      */     catch (BadKind localBadKind) {
/* 1206 */       throw this.wrapper.fixedBadTypecode(localBadKind);
/*      */     }
/* 1208 */     this.typeCode = TypeCodeImpl.convertToNative(this.orb, paramTypeCode);
/* 1209 */     this.object = paramBigDecimal;
/* 1210 */     this.isInitialized = true;
/*      */   }
/*      */ 
/*      */   public BigDecimal extract_fixed() {
/* 1214 */     checkExtractBadOperation(28);
/* 1215 */     return (BigDecimal)this.object;
/*      */   }
/*      */ 
/*      */   public TypeCode createTypeCodeForClass(Class paramClass, ORB paramORB)
/*      */   {
/* 1227 */     TypeCodeImpl localTypeCodeImpl = paramORB.getTypeCodeForClass(paramClass);
/* 1228 */     if (localTypeCodeImpl != null) {
/* 1229 */       return localTypeCodeImpl;
/*      */     }
/*      */ 
/* 1235 */     RepositoryIdStrings localRepositoryIdStrings = RepositoryIdFactory.getRepIdStringsFactory();
/*      */     java.lang.Object localObject1;
/*      */     java.lang.Object localObject2;
/* 1241 */     if (paramClass.isArray())
/*      */     {
/* 1243 */       localObject1 = paramClass.getComponentType();
/*      */ 
/* 1245 */       if (((Class)localObject1).isPrimitive()) {
/* 1246 */         localObject2 = getPrimitiveTypeCodeForClass((Class)localObject1, paramORB);
/*      */       }
/*      */       else {
/* 1249 */         localObject2 = createTypeCodeForClass((Class)localObject1, paramORB);
/*      */       }
/*      */ 
/* 1252 */       TypeCode localTypeCode = paramORB.create_sequence_tc(0, (TypeCode)localObject2);
/*      */ 
/* 1254 */       String str = localRepositoryIdStrings.createForJavaType(paramClass);
/*      */ 
/* 1256 */       return paramORB.create_value_box_tc(str, "Sequence", localTypeCode);
/* 1257 */     }if (paramClass == String.class)
/*      */     {
/* 1259 */       localObject1 = paramORB.create_string_tc(0);
/*      */ 
/* 1261 */       localObject2 = localRepositoryIdStrings.createForJavaType(paramClass);
/*      */ 
/* 1263 */       return paramORB.create_value_box_tc((String)localObject2, "StringValue", (TypeCode)localObject1);
/*      */     }
/*      */ 
/* 1268 */     localTypeCodeImpl = (TypeCodeImpl)ValueUtility.createTypeCodeForClass(paramORB, paramClass, ORBUtility.createValueHandler());
/*      */ 
/* 1271 */     localTypeCodeImpl.setCaching(true);
/*      */ 
/* 1273 */     paramORB.setTypeCodeForClass(paramClass, localTypeCodeImpl);
/* 1274 */     return localTypeCodeImpl;
/*      */   }
/*      */ 
/*      */   private TypeCode getPrimitiveTypeCodeForClass(Class paramClass, ORB paramORB)
/*      */   {
/* 1289 */     if (paramClass == Integer.TYPE)
/* 1290 */       return paramORB.get_primitive_tc(TCKind.tk_long);
/* 1291 */     if (paramClass == Byte.TYPE)
/* 1292 */       return paramORB.get_primitive_tc(TCKind.tk_octet);
/* 1293 */     if (paramClass == Long.TYPE)
/* 1294 */       return paramORB.get_primitive_tc(TCKind.tk_longlong);
/* 1295 */     if (paramClass == Float.TYPE)
/* 1296 */       return paramORB.get_primitive_tc(TCKind.tk_float);
/* 1297 */     if (paramClass == Double.TYPE)
/* 1298 */       return paramORB.get_primitive_tc(TCKind.tk_double);
/* 1299 */     if (paramClass == Short.TYPE)
/* 1300 */       return paramORB.get_primitive_tc(TCKind.tk_short);
/* 1301 */     if (paramClass == Character.TYPE)
/*      */     {
/* 1313 */       if ((ORBVersionFactory.getFOREIGN().compareTo(paramORB.getORBVersion()) == 0) || (ORBVersionFactory.getNEWER().compareTo(paramORB.getORBVersion()) <= 0))
/*      */       {
/* 1315 */         return paramORB.get_primitive_tc(TCKind.tk_wchar);
/*      */       }
/* 1317 */       return paramORB.get_primitive_tc(TCKind.tk_char);
/* 1318 */     }if (paramClass == Boolean.TYPE) {
/* 1319 */       return paramORB.get_primitive_tc(TCKind.tk_boolean);
/*      */     }
/*      */ 
/* 1322 */     return paramORB.get_primitive_tc(TCKind.tk_any);
/*      */   }
/*      */ 
/*      */   public Any extractAny(TypeCode paramTypeCode, ORB paramORB)
/*      */   {
/* 1330 */     Any localAny = paramORB.create_any();
/* 1331 */     org.omg.CORBA.portable.OutputStream localOutputStream = localAny.create_output_stream();
/* 1332 */     TypeCodeImpl.convertToNative(paramORB, paramTypeCode).copy(this.stream, localOutputStream);
/* 1333 */     localAny.read_value(localOutputStream.create_input_stream(), paramTypeCode);
/* 1334 */     return localAny;
/*      */   }
/*      */ 
/*      */   public static Any extractAnyFromStream(TypeCode paramTypeCode, org.omg.CORBA.portable.InputStream paramInputStream, ORB paramORB)
/*      */   {
/* 1340 */     Any localAny = paramORB.create_any();
/* 1341 */     org.omg.CORBA.portable.OutputStream localOutputStream = localAny.create_output_stream();
/* 1342 */     TypeCodeImpl.convertToNative(paramORB, paramTypeCode).copy(paramInputStream, localOutputStream);
/* 1343 */     localAny.read_value(localOutputStream.create_input_stream(), paramTypeCode);
/* 1344 */     return localAny;
/*      */   }
/*      */ 
/*      */   public boolean isInitialized()
/*      */   {
/* 1349 */     return this.isInitialized;
/*      */   }
/*      */ 
/*      */   private static final class AnyInputStream extends EncapsInputStream
/*      */   {
/*      */     public AnyInputStream(EncapsInputStream paramEncapsInputStream)
/*      */     {
/*   74 */       super();
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class AnyOutputStream extends EncapsOutputStream
/*      */   {
/*      */     public AnyOutputStream(ORB paramORB)
/*      */     {
/*   82 */       super();
/*      */     }
/*      */ 
/*      */     public org.omg.CORBA.portable.InputStream create_input_stream() {
/*   86 */       final org.omg.CORBA.portable.InputStream localInputStream = super.create_input_stream();
/*      */ 
/*   88 */       AnyImpl.AnyInputStream localAnyInputStream = (AnyImpl.AnyInputStream)AccessController.doPrivileged(new PrivilegedAction()
/*      */       {
/*      */         public AnyImpl.AnyInputStream run()
/*      */         {
/*   92 */           return new AnyImpl.AnyInputStream((EncapsInputStream)localInputStream);
/*      */         }
/*      */       });
/*   96 */       return localAnyInputStream;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.corba.AnyImpl
 * JD-Core Version:    0.6.2
 */