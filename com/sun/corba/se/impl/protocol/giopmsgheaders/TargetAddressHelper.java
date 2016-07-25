/*     */ package com.sun.corba.se.impl.protocol.giopmsgheaders;
/*     */ 
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.BAD_OPERATION;
/*     */ import org.omg.CORBA.ORB;
/*     */ import org.omg.CORBA.TCKind;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.UnionMember;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.IOP.TaggedProfile;
/*     */ import org.omg.IOP.TaggedProfileHelper;
/*     */ 
/*     */ public abstract class TargetAddressHelper
/*     */ {
/*  38 */   private static String _id = "IDL:messages/TargetAddress:1.0";
/*     */ 
/*  53 */   private static TypeCode __typeCode = null;
/*     */ 
/*     */   public static void insert(Any paramAny, TargetAddress paramTargetAddress)
/*     */   {
/*  42 */     OutputStream localOutputStream = paramAny.create_output_stream();
/*  43 */     paramAny.type(type());
/*  44 */     write(localOutputStream, paramTargetAddress);
/*  45 */     paramAny.read_value(localOutputStream.create_input_stream(), type());
/*     */   }
/*     */ 
/*     */   public static TargetAddress extract(Any paramAny)
/*     */   {
/*  50 */     return read(paramAny.create_input_stream());
/*     */   }
/*     */ 
/*     */   public static synchronized TypeCode type()
/*     */   {
/*  56 */     if (__typeCode == null)
/*     */     {
/*  59 */       TypeCode localTypeCode1 = ORB.init().get_primitive_tc(TCKind.tk_short);
/*  60 */       localTypeCode1 = ORB.init().create_alias_tc(AddressingDispositionHelper.id(), "AddressingDisposition", localTypeCode1);
/*  61 */       UnionMember[] arrayOfUnionMember = new UnionMember[3];
/*     */ 
/*  66 */       Any localAny = ORB.init().create_any();
/*  67 */       localAny.insert_short((short)0);
/*  68 */       TypeCode localTypeCode2 = ORB.init().get_primitive_tc(TCKind.tk_octet);
/*  69 */       localTypeCode2 = ORB.init().create_sequence_tc(0, localTypeCode2);
/*  70 */       arrayOfUnionMember[0] = new UnionMember("object_key", localAny, localTypeCode2, null);
/*     */ 
/*  77 */       localAny = ORB.init().create_any();
/*  78 */       localAny.insert_short((short)1);
/*  79 */       localTypeCode2 = TaggedProfileHelper.type();
/*  80 */       arrayOfUnionMember[1] = new UnionMember("profile", localAny, localTypeCode2, null);
/*     */ 
/*  87 */       localAny = ORB.init().create_any();
/*  88 */       localAny.insert_short((short)2);
/*  89 */       localTypeCode2 = IORAddressingInfoHelper.type();
/*  90 */       arrayOfUnionMember[2] = new UnionMember("ior", localAny, localTypeCode2, null);
/*     */ 
/*  95 */       __typeCode = ORB.init().create_union_tc(id(), "TargetAddress", localTypeCode1, arrayOfUnionMember);
/*     */     }
/*  97 */     return __typeCode;
/*     */   }
/*     */ 
/*     */   public static String id()
/*     */   {
/* 102 */     return _id;
/*     */   }
/*     */ 
/*     */   public static TargetAddress read(InputStream paramInputStream)
/*     */   {
/* 107 */     TargetAddress localTargetAddress = new TargetAddress();
/* 108 */     int i = 0;
/* 109 */     i = paramInputStream.read_short();
/* 110 */     switch (i)
/*     */     {
/*     */     case 0:
/* 113 */       byte[] arrayOfByte = null;
/* 114 */       int j = paramInputStream.read_long();
/* 115 */       arrayOfByte = new byte[j];
/* 116 */       paramInputStream.read_octet_array(arrayOfByte, 0, j);
/* 117 */       localTargetAddress.object_key(arrayOfByte);
/* 118 */       break;
/*     */     case 1:
/* 120 */       TaggedProfile localTaggedProfile = null;
/* 121 */       localTaggedProfile = TaggedProfileHelper.read(paramInputStream);
/* 122 */       localTargetAddress.profile(localTaggedProfile);
/* 123 */       break;
/*     */     case 2:
/* 125 */       IORAddressingInfo localIORAddressingInfo = null;
/* 126 */       localIORAddressingInfo = IORAddressingInfoHelper.read(paramInputStream);
/* 127 */       localTargetAddress.ior(localIORAddressingInfo);
/* 128 */       break;
/*     */     default:
/* 130 */       throw new BAD_OPERATION();
/*     */     }
/* 132 */     return localTargetAddress;
/*     */   }
/*     */ 
/*     */   public static void write(OutputStream paramOutputStream, TargetAddress paramTargetAddress)
/*     */   {
/* 137 */     paramOutputStream.write_short(paramTargetAddress.discriminator());
/* 138 */     switch (paramTargetAddress.discriminator())
/*     */     {
/*     */     case 0:
/* 141 */       paramOutputStream.write_long(paramTargetAddress.object_key().length);
/* 142 */       paramOutputStream.write_octet_array(paramTargetAddress.object_key(), 0, paramTargetAddress.object_key().length);
/* 143 */       break;
/*     */     case 1:
/* 145 */       TaggedProfileHelper.write(paramOutputStream, paramTargetAddress.profile());
/* 146 */       break;
/*     */     case 2:
/* 148 */       IORAddressingInfoHelper.write(paramOutputStream, paramTargetAddress.ior());
/* 149 */       break;
/*     */     default:
/* 151 */       throw new BAD_OPERATION();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.protocol.giopmsgheaders.TargetAddressHelper
 * JD-Core Version:    0.6.2
 */