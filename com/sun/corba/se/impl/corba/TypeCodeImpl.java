/*      */ package com.sun.corba.se.impl.corba;
/*      */ 
/*      */ import com.sun.corba.se.impl.encoding.CDRInputStream;
/*      */ import com.sun.corba.se.impl.encoding.CDROutputStream;
/*      */ import com.sun.corba.se.impl.encoding.TypeCodeInputStream;
/*      */ import com.sun.corba.se.impl.encoding.TypeCodeOutputStream;
/*      */ import com.sun.corba.se.impl.encoding.TypeCodeReader;
/*      */ import com.sun.corba.se.impl.encoding.WrapperInputStream;
/*      */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.io.PrintStream;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import org.omg.CORBA.Any;
/*      */ import org.omg.CORBA.StructMember;
/*      */ import org.omg.CORBA.TCKind;
/*      */ import org.omg.CORBA.TypeCode;
/*      */ import org.omg.CORBA.TypeCodePackage.BadKind;
/*      */ import org.omg.CORBA.TypeCodePackage.Bounds;
/*      */ import org.omg.CORBA.UnionMember;
/*      */ import org.omg.CORBA.ValueMember;
/*      */ import sun.corba.OutputStreamFactory;
/*      */ 
/*      */ public final class TypeCodeImpl extends TypeCode
/*      */ {
/*      */   protected static final int tk_indirect = -1;
/*      */   private static final int EMPTY = 0;
/*      */   private static final int SIMPLE = 1;
/*      */   private static final int COMPLEX = 2;
/*   91 */   private static final int[] typeTable = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 1, 2, 2, 2, 2, 0, 0, 0, 0, 1, 1, 2, 2, 2, 2 };
/*      */ 
/*  129 */   static final String[] kindNames = { "null", "void", "short", "long", "ushort", "ulong", "float", "double", "boolean", "char", "octet", "any", "typecode", "principal", "objref", "struct", "union", "enum", "string", "sequence", "array", "alias", "exception", "longlong", "ulonglong", "longdouble", "wchar", "wstring", "fixed", "value", "valueBox", "native", "abstractInterface" };
/*      */ 
/*  165 */   private int _kind = 0;
/*      */ 
/*  168 */   private String _id = "";
/*  169 */   private String _name = "";
/*  170 */   private int _memberCount = 0;
/*  171 */   private String[] _memberNames = null;
/*  172 */   private TypeCodeImpl[] _memberTypes = null;
/*  173 */   private AnyImpl[] _unionLabels = null;
/*  174 */   private TypeCodeImpl _discriminator = null;
/*  175 */   private int _defaultIndex = -1;
/*  176 */   private int _length = 0;
/*  177 */   private TypeCodeImpl _contentType = null;
/*      */ 
/*  179 */   private short _digits = 0;
/*  180 */   private short _scale = 0;
/*      */ 
/*  185 */   private short _type_modifier = -1;
/*      */ 
/*  187 */   private TypeCodeImpl _concrete_base = null;
/*  188 */   private short[] _memberAccess = null;
/*      */ 
/*  190 */   private TypeCodeImpl _parent = null;
/*  191 */   private int _parentOffset = 0;
/*      */ 
/*  193 */   private TypeCodeImpl _indirectType = null;
/*      */ 
/*  196 */   private byte[] outBuffer = null;
/*      */ 
/*  198 */   private boolean cachingEnabled = false;
/*      */   private com.sun.corba.se.spi.orb.ORB _orb;
/*      */   private ORBUtilSystemException wrapper;
/*      */ 
/*      */   public TypeCodeImpl(com.sun.corba.se.spi.orb.ORB paramORB)
/*      */   {
/*  210 */     this._orb = paramORB;
/*  211 */     this.wrapper = ORBUtilSystemException.get(paramORB, "rpc.presentation");
/*      */   }
/*      */ 
/*      */   public TypeCodeImpl(com.sun.corba.se.spi.orb.ORB paramORB, TypeCode paramTypeCode)
/*      */   {
/*  219 */     this(paramORB);
/*      */     Object localObject;
/*  225 */     if ((paramTypeCode instanceof TypeCodeImpl)) {
/*  226 */       localObject = (TypeCodeImpl)paramTypeCode;
/*  227 */       if (((TypeCodeImpl)localObject)._kind == -1)
/*  228 */         throw this.wrapper.badRemoteTypecode();
/*  229 */       if ((((TypeCodeImpl)localObject)._kind == 19) && (((TypeCodeImpl)localObject)._contentType == null)) {
/*  230 */         throw this.wrapper.badRemoteTypecode();
/*      */       }
/*      */     }
/*      */ 
/*  234 */     this._kind = paramTypeCode.kind().value();
/*      */     try
/*      */     {
/*      */       int j;
/*  238 */       switch (this._kind) {
/*      */       case 29:
/*  240 */         this._type_modifier = paramTypeCode.type_modifier();
/*      */ 
/*  242 */         localObject = paramTypeCode.concrete_base_type();
/*  243 */         if (localObject != null)
/*  244 */           this._concrete_base = convertToNative(this._orb, (TypeCode)localObject);
/*      */         else {
/*  246 */           this._concrete_base = null;
/*      */         }
/*      */ 
/*  250 */         this._memberAccess = new short[paramTypeCode.member_count()];
/*  251 */         for (j = 0; j < paramTypeCode.member_count(); j++) {
/*  252 */           this._memberAccess[j] = paramTypeCode.member_visibility(j);
/*      */         }
/*      */ 
/*      */       case 15:
/*      */       case 16:
/*      */       case 22:
/*  258 */         this._memberTypes = new TypeCodeImpl[paramTypeCode.member_count()];
/*  259 */         for (j = 0; j < paramTypeCode.member_count(); j++) {
/*  260 */           this._memberTypes[j] = convertToNative(this._orb, paramTypeCode.member_type(j));
/*  261 */           this._memberTypes[j].setParent(this);
/*      */         }
/*      */ 
/*      */       case 17:
/*  265 */         this._memberNames = new String[paramTypeCode.member_count()];
/*  266 */         for (j = 0; j < paramTypeCode.member_count(); j++) {
/*  267 */           this._memberNames[j] = paramTypeCode.member_name(j);
/*      */         }
/*      */ 
/*  270 */         this._memberCount = paramTypeCode.member_count();
/*      */       case 14:
/*      */       case 21:
/*      */       case 30:
/*      */       case 31:
/*      */       case 32:
/*  276 */         setId(paramTypeCode.id());
/*  277 */         this._name = paramTypeCode.name();
/*      */       case 18:
/*      */       case 19:
/*      */       case 20:
/*      */       case 23:
/*      */       case 24:
/*      */       case 25:
/*      */       case 26:
/*      */       case 27:
/*  282 */       case 28: } switch (this._kind) {
/*      */       case 16:
/*  284 */         this._discriminator = convertToNative(this._orb, paramTypeCode.discriminator_type());
/*  285 */         this._defaultIndex = paramTypeCode.default_index();
/*  286 */         this._unionLabels = new AnyImpl[this._memberCount];
/*  287 */         for (int i = 0; i < this._memberCount; i++) {
/*  288 */           this._unionLabels[i] = new AnyImpl(this._orb, paramTypeCode.member_label(i));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  293 */       switch (this._kind) {
/*      */       case 18:
/*      */       case 19:
/*      */       case 20:
/*      */       case 27:
/*  298 */         this._length = paramTypeCode.length();
/*      */       case 21:
/*      */       case 22:
/*      */       case 23:
/*      */       case 24:
/*      */       case 25:
/*  302 */       case 26: } switch (this._kind) {
/*      */       case 19:
/*      */       case 20:
/*      */       case 21:
/*      */       case 30:
/*  307 */         this._contentType = convertToNative(this._orb, paramTypeCode.content_type());
/*      */       }
/*      */     }
/*      */     catch (Bounds localBounds) {
/*      */     }
/*      */     catch (BadKind localBadKind) {
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCodeImpl(com.sun.corba.se.spi.orb.ORB paramORB, int paramInt) {
/*  317 */     this(paramORB);
/*      */ 
/*  322 */     this._kind = paramInt;
/*      */ 
/*  325 */     switch (this._kind)
/*      */     {
/*      */     case 14:
/*  329 */       setId("IDL:omg.org/CORBA/Object:1.0");
/*  330 */       this._name = "Object";
/*  331 */       break;
/*      */     case 18:
/*      */     case 27:
/*  337 */       this._length = 0;
/*  338 */       break;
/*      */     case 29:
/*  343 */       this._concrete_base = null;
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCodeImpl(com.sun.corba.se.spi.orb.ORB paramORB, int paramInt, String paramString1, String paramString2, StructMember[] paramArrayOfStructMember)
/*      */   {
/*  356 */     this(paramORB);
/*      */ 
/*  358 */     if ((paramInt == 15) || (paramInt == 22)) {
/*  359 */       this._kind = paramInt;
/*  360 */       setId(paramString1);
/*  361 */       this._name = paramString2;
/*  362 */       this._memberCount = paramArrayOfStructMember.length;
/*      */ 
/*  364 */       this._memberNames = new String[this._memberCount];
/*  365 */       this._memberTypes = new TypeCodeImpl[this._memberCount];
/*      */ 
/*  367 */       for (int i = 0; i < this._memberCount; i++) {
/*  368 */         this._memberNames[i] = paramArrayOfStructMember[i].name;
/*  369 */         this._memberTypes[i] = convertToNative(this._orb, paramArrayOfStructMember[i].type);
/*  370 */         this._memberTypes[i].setParent(this);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCodeImpl(com.sun.corba.se.spi.orb.ORB paramORB, int paramInt, String paramString1, String paramString2, TypeCode paramTypeCode, UnionMember[] paramArrayOfUnionMember)
/*      */   {
/*  383 */     this(paramORB);
/*      */ 
/*  385 */     if (paramInt == 16) {
/*  386 */       this._kind = paramInt;
/*  387 */       setId(paramString1);
/*  388 */       this._name = paramString2;
/*  389 */       this._memberCount = paramArrayOfUnionMember.length;
/*  390 */       this._discriminator = convertToNative(this._orb, paramTypeCode);
/*      */ 
/*  392 */       this._memberNames = new String[this._memberCount];
/*  393 */       this._memberTypes = new TypeCodeImpl[this._memberCount];
/*  394 */       this._unionLabels = new AnyImpl[this._memberCount];
/*      */ 
/*  396 */       for (int i = 0; i < this._memberCount; i++) {
/*  397 */         this._memberNames[i] = paramArrayOfUnionMember[i].name;
/*  398 */         this._memberTypes[i] = convertToNative(this._orb, paramArrayOfUnionMember[i].type);
/*  399 */         this._memberTypes[i].setParent(this);
/*  400 */         this._unionLabels[i] = new AnyImpl(this._orb, paramArrayOfUnionMember[i].label);
/*      */ 
/*  402 */         if ((this._unionLabels[i].type().kind() == TCKind.tk_octet) && 
/*  403 */           (this._unionLabels[i].extract_octet() == 0))
/*  404 */           this._defaultIndex = i;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCodeImpl(com.sun.corba.se.spi.orb.ORB paramORB, int paramInt, String paramString1, String paramString2, short paramShort, TypeCode paramTypeCode, ValueMember[] paramArrayOfValueMember)
/*      */   {
/*  420 */     this(paramORB);
/*      */ 
/*  422 */     if (paramInt == 29) {
/*  423 */       this._kind = paramInt;
/*  424 */       setId(paramString1);
/*  425 */       this._name = paramString2;
/*  426 */       this._type_modifier = paramShort;
/*  427 */       if (paramTypeCode != null) {
/*  428 */         this._concrete_base = convertToNative(this._orb, paramTypeCode);
/*      */       }
/*  430 */       this._memberCount = paramArrayOfValueMember.length;
/*      */ 
/*  432 */       this._memberNames = new String[this._memberCount];
/*  433 */       this._memberTypes = new TypeCodeImpl[this._memberCount];
/*  434 */       this._memberAccess = new short[this._memberCount];
/*      */ 
/*  436 */       for (int i = 0; i < this._memberCount; i++) {
/*  437 */         this._memberNames[i] = paramArrayOfValueMember[i].name;
/*  438 */         this._memberTypes[i] = convertToNative(this._orb, paramArrayOfValueMember[i].type);
/*  439 */         this._memberTypes[i].setParent(this);
/*  440 */         this._memberAccess[i] = paramArrayOfValueMember[i].access;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCodeImpl(com.sun.corba.se.spi.orb.ORB paramORB, int paramInt, String paramString1, String paramString2, String[] paramArrayOfString)
/*      */   {
/*  453 */     this(paramORB);
/*      */ 
/*  455 */     if (paramInt == 17)
/*      */     {
/*  457 */       this._kind = paramInt;
/*  458 */       setId(paramString1);
/*  459 */       this._name = paramString2;
/*  460 */       this._memberCount = paramArrayOfString.length;
/*      */ 
/*  462 */       this._memberNames = new String[this._memberCount];
/*      */ 
/*  464 */       for (int i = 0; i < this._memberCount; i++)
/*  465 */         this._memberNames[i] = paramArrayOfString[i];
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCodeImpl(com.sun.corba.se.spi.orb.ORB paramORB, int paramInt, String paramString1, String paramString2, TypeCode paramTypeCode)
/*      */   {
/*  476 */     this(paramORB);
/*      */ 
/*  478 */     if ((paramInt == 21) || (paramInt == 30))
/*      */     {
/*  480 */       this._kind = paramInt;
/*  481 */       setId(paramString1);
/*  482 */       this._name = paramString2;
/*  483 */       this._contentType = convertToNative(this._orb, paramTypeCode);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCodeImpl(com.sun.corba.se.spi.orb.ORB paramORB, int paramInt, String paramString1, String paramString2)
/*      */   {
/*  494 */     this(paramORB);
/*      */ 
/*  496 */     if ((paramInt == 14) || (paramInt == 31) || (paramInt == 32))
/*      */     {
/*  500 */       this._kind = paramInt;
/*  501 */       setId(paramString1);
/*  502 */       this._name = paramString2;
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCodeImpl(com.sun.corba.se.spi.orb.ORB paramORB, int paramInt1, int paramInt2)
/*      */   {
/*  512 */     this(paramORB);
/*      */ 
/*  514 */     if (paramInt2 < 0) {
/*  515 */       throw this.wrapper.negativeBounds();
/*      */     }
/*  517 */     if ((paramInt1 == 18) || (paramInt1 == 27)) {
/*  518 */       this._kind = paramInt1;
/*  519 */       this._length = paramInt2;
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCodeImpl(com.sun.corba.se.spi.orb.ORB paramORB, int paramInt1, int paramInt2, TypeCode paramTypeCode)
/*      */   {
/*  529 */     this(paramORB);
/*      */ 
/*  531 */     if ((paramInt1 == 19) || (paramInt1 == 20)) {
/*  532 */       this._kind = paramInt1;
/*  533 */       this._length = paramInt2;
/*  534 */       this._contentType = convertToNative(this._orb, paramTypeCode);
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCodeImpl(com.sun.corba.se.spi.orb.ORB paramORB, int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  544 */     this(paramORB);
/*      */ 
/*  546 */     if (paramInt1 == 19) {
/*  547 */       this._kind = paramInt1;
/*  548 */       this._length = paramInt2;
/*  549 */       this._parentOffset = paramInt3;
/*      */     }
/*      */   }
/*      */ 
/*      */   public TypeCodeImpl(com.sun.corba.se.spi.orb.ORB paramORB, String paramString)
/*      */   {
/*  557 */     this(paramORB);
/*      */ 
/*  559 */     this._kind = -1;
/*      */ 
/*  561 */     this._id = paramString;
/*      */ 
/*  564 */     tryIndirectType();
/*      */   }
/*      */ 
/*      */   public TypeCodeImpl(com.sun.corba.se.spi.orb.ORB paramORB, int paramInt, short paramShort1, short paramShort2)
/*      */   {
/*  573 */     this(paramORB);
/*      */ 
/*  578 */     if (paramInt == 28) {
/*  579 */       this._kind = paramInt;
/*  580 */       this._digits = paramShort1;
/*  581 */       this._scale = paramShort2;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static TypeCodeImpl convertToNative(com.sun.corba.se.spi.orb.ORB paramORB, TypeCode paramTypeCode)
/*      */   {
/*  596 */     if ((paramTypeCode instanceof TypeCodeImpl)) {
/*  597 */       return (TypeCodeImpl)paramTypeCode;
/*      */     }
/*  599 */     return new TypeCodeImpl(paramORB, paramTypeCode);
/*      */   }
/*      */ 
/*      */   public static CDROutputStream newOutputStream(com.sun.corba.se.spi.orb.ORB paramORB) {
/*  603 */     TypeCodeOutputStream localTypeCodeOutputStream = OutputStreamFactory.newTypeCodeOutputStream(paramORB);
/*      */ 
/*  607 */     return localTypeCodeOutputStream;
/*      */   }
/*      */ 
/*      */   private TypeCodeImpl indirectType()
/*      */   {
/*  613 */     this._indirectType = tryIndirectType();
/*  614 */     if (this._indirectType == null)
/*      */     {
/*  616 */       throw this.wrapper.unresolvedRecursiveTypecode();
/*      */     }
/*  618 */     return this._indirectType;
/*      */   }
/*      */ 
/*      */   private TypeCodeImpl tryIndirectType()
/*      */   {
/*  623 */     if (this._indirectType != null) {
/*  624 */       return this._indirectType;
/*      */     }
/*  626 */     setIndirectType(this._orb.getTypeCode(this._id));
/*      */ 
/*  628 */     return this._indirectType;
/*      */   }
/*      */ 
/*      */   private void setIndirectType(TypeCodeImpl paramTypeCodeImpl) {
/*  632 */     this._indirectType = paramTypeCodeImpl;
/*  633 */     if (this._indirectType != null)
/*      */       try {
/*  635 */         this._id = this._indirectType.id();
/*      */       }
/*      */       catch (BadKind localBadKind) {
/*  638 */         throw this.wrapper.badkindCannotOccur();
/*      */       }
/*      */   }
/*      */ 
/*      */   private void setId(String paramString)
/*      */   {
/*  644 */     this._id = paramString;
/*  645 */     if ((this._orb instanceof TypeCodeFactory))
/*  646 */       this._orb.setTypeCode(this._id, this);
/*      */   }
/*      */ 
/*      */   private void setParent(TypeCodeImpl paramTypeCodeImpl)
/*      */   {
/*  653 */     this._parent = paramTypeCodeImpl;
/*      */   }
/*      */ 
/*      */   private TypeCodeImpl getParentAtLevel(int paramInt) {
/*  657 */     if (paramInt == 0) {
/*  658 */       return this;
/*      */     }
/*  660 */     if (this._parent == null) {
/*  661 */       throw this.wrapper.unresolvedRecursiveTypecode();
/*      */     }
/*  663 */     return this._parent.getParentAtLevel(paramInt - 1);
/*      */   }
/*      */ 
/*      */   private TypeCodeImpl lazy_content_type() {
/*  667 */     if ((this._contentType == null) && 
/*  668 */       (this._kind == 19) && (this._parentOffset > 0) && (this._parent != null))
/*      */     {
/*  671 */       TypeCodeImpl localTypeCodeImpl = getParentAtLevel(this._parentOffset);
/*  672 */       if ((localTypeCodeImpl != null) && (localTypeCodeImpl._id != null))
/*      */       {
/*  676 */         this._contentType = new TypeCodeImpl(this._orb, localTypeCodeImpl._id);
/*      */       }
/*      */     }
/*      */ 
/*  680 */     return this._contentType;
/*      */   }
/*      */ 
/*      */   private TypeCode realType(TypeCode paramTypeCode)
/*      */   {
/*  686 */     TypeCode localTypeCode = paramTypeCode;
/*      */     try
/*      */     {
/*  689 */       while (localTypeCode.kind().value() == 21)
/*  690 */         localTypeCode = localTypeCode.content_type();
/*      */     }
/*      */     catch (BadKind localBadKind)
/*      */     {
/*  694 */       throw this.wrapper.badkindCannotOccur();
/*      */     }
/*  696 */     return localTypeCode;
/*      */   }
/*      */ 
/*      */   public final boolean equal(TypeCode paramTypeCode)
/*      */   {
/*  706 */     if (paramTypeCode == this) {
/*  707 */       return true;
/*      */     }
/*      */     try
/*      */     {
/*  711 */       if (this._kind == -1)
/*      */       {
/*  713 */         if ((this._id != null) && (paramTypeCode.id() != null))
/*  714 */           return this._id.equals(paramTypeCode.id());
/*  715 */         return (this._id == null) && (paramTypeCode.id() == null);
/*      */       }
/*      */ 
/*  719 */       if (this._kind != paramTypeCode.kind().value()) {
/*  720 */         return false;
/*      */       }
/*      */ 
/*  723 */       switch (typeTable[this._kind])
/*      */       {
/*      */       case 0:
/*  726 */         return true;
/*      */       case 1:
/*  729 */         switch (this._kind)
/*      */         {
/*      */         case 18:
/*      */         case 27:
/*  733 */           return this._length == paramTypeCode.length();
/*      */         case 28:
/*  736 */           return (this._digits == paramTypeCode.fixed_digits()) && (this._scale == paramTypeCode.fixed_scale());
/*      */         }
/*  738 */         return false;
/*      */       case 2:
/*      */         int i;
/*  743 */         switch (this._kind)
/*      */         {
/*      */         case 14:
/*  748 */           if (this._id.compareTo(paramTypeCode.id()) == 0) {
/*  749 */             return true;
/*      */           }
/*      */ 
/*  752 */           if (this._id.compareTo(this._orb.get_primitive_tc(this._kind).id()) == 0)
/*      */           {
/*  755 */             return true;
/*      */           }
/*      */ 
/*  758 */           if (paramTypeCode.id().compareTo(this._orb.get_primitive_tc(this._kind).id()) == 0)
/*      */           {
/*  761 */             return true;
/*      */           }
/*      */ 
/*  764 */           return false;
/*      */         case 31:
/*      */         case 32:
/*  771 */           if (this._id.compareTo(paramTypeCode.id()) != 0) {
/*  772 */             return false;
/*      */           }
/*      */ 
/*  776 */           return true;
/*      */         case 15:
/*      */         case 22:
/*  783 */           if (this._memberCount != paramTypeCode.member_count()) {
/*  784 */             return false;
/*      */           }
/*  786 */           if (this._id.compareTo(paramTypeCode.id()) != 0) {
/*  787 */             return false;
/*      */           }
/*  789 */           for (i = 0; i < this._memberCount; i++) {
/*  790 */             if (!this._memberTypes[i].equal(paramTypeCode.member_type(i)))
/*  791 */               return false;
/*      */           }
/*  793 */           return true;
/*      */         case 16:
/*  799 */           if (this._memberCount != paramTypeCode.member_count()) {
/*  800 */             return false;
/*      */           }
/*  802 */           if (this._id.compareTo(paramTypeCode.id()) != 0) {
/*  803 */             return false;
/*      */           }
/*  805 */           if (this._defaultIndex != paramTypeCode.default_index()) {
/*  806 */             return false;
/*      */           }
/*  808 */           if (!this._discriminator.equal(paramTypeCode.discriminator_type())) {
/*  809 */             return false;
/*      */           }
/*  811 */           for (i = 0; i < this._memberCount; i++) {
/*  812 */             if (!this._unionLabels[i].equal(paramTypeCode.member_label(i)))
/*  813 */               return false;
/*      */           }
/*  815 */           for (i = 0; i < this._memberCount; i++) {
/*  816 */             if (!this._memberTypes[i].equal(paramTypeCode.member_type(i)))
/*  817 */               return false;
/*      */           }
/*  819 */           return true;
/*      */         case 17:
/*  825 */           if (this._id.compareTo(paramTypeCode.id()) != 0) {
/*  826 */             return false;
/*      */           }
/*  828 */           if (this._memberCount != paramTypeCode.member_count()) {
/*  829 */             return false;
/*      */           }
/*  831 */           return true;
/*      */         case 19:
/*      */         case 20:
/*  838 */           if (this._length != paramTypeCode.length()) {
/*  839 */             return false;
/*      */           }
/*      */ 
/*  842 */           if (!lazy_content_type().equal(paramTypeCode.content_type())) {
/*  843 */             return false;
/*      */           }
/*      */ 
/*  846 */           return true;
/*      */         case 29:
/*  852 */           if (this._memberCount != paramTypeCode.member_count()) {
/*  853 */             return false;
/*      */           }
/*  855 */           if (this._id.compareTo(paramTypeCode.id()) != 0) {
/*  856 */             return false;
/*      */           }
/*  858 */           for (i = 0; i < this._memberCount; i++)
/*  859 */             if ((this._memberAccess[i] != paramTypeCode.member_visibility(i)) || (!this._memberTypes[i].equal(paramTypeCode.member_type(i))))
/*      */             {
/*  861 */               return false;
/*      */             }
/*  862 */           if (this._type_modifier == paramTypeCode.type_modifier()) {
/*  863 */             return false;
/*      */           }
/*  865 */           TypeCode localTypeCode = paramTypeCode.concrete_base_type();
/*  866 */           if (((this._concrete_base == null) && (localTypeCode != null)) || ((this._concrete_base != null) && (localTypeCode == null)) || (!this._concrete_base.equal(localTypeCode)))
/*      */           {
/*  870 */             return false;
/*      */           }
/*      */ 
/*  873 */           return true;
/*      */         case 21:
/*      */         case 30:
/*  880 */           if (this._id.compareTo(paramTypeCode.id()) != 0) {
/*  881 */             return false;
/*      */           }
/*      */ 
/*  884 */           return this._contentType.equal(paramTypeCode.content_type());
/*      */         case 18:
/*      */         case 23:
/*      */         case 24:
/*      */         case 25:
/*      */         case 26:
/*      */         case 27:
/*  891 */         case 28: } break; }  } catch (Bounds localBounds) {  } catch (BadKind localBadKind) {  } return false;
/*      */   }
/*      */ 
/*      */   public boolean equivalent(TypeCode paramTypeCode)
/*      */   {
/*  899 */     if (paramTypeCode == this) {
/*  900 */       return true;
/*      */     }
/*      */ 
/*  907 */     Object localObject = this._kind == -1 ? indirectType() : this;
/*  908 */     localObject = realType((TypeCode)localObject);
/*  909 */     TypeCode localTypeCode = realType(paramTypeCode);
/*      */ 
/*  913 */     if (((TypeCode)localObject).kind().value() != localTypeCode.kind().value()) {
/*  914 */       return false;
/*      */     }
/*      */ 
/*  917 */     String str1 = null;
/*  918 */     String str2 = null;
/*      */     try {
/*  920 */       str1 = id();
/*  921 */       str2 = paramTypeCode.id();
/*      */ 
/*  927 */       if ((str1 != null) && (str2 != null)) {
/*  928 */         return str1.equals(str2);
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (BadKind localBadKind1)
/*      */     {
/*      */     }
/*      */ 
/*  937 */     int i = ((TypeCode)localObject).kind().value();
/*      */     try {
/*  939 */       if ((i == 15) || (i == 16) || (i == 17) || (i == 22) || (i == 29))
/*      */       {
/*  945 */         if (((TypeCode)localObject).member_count() != localTypeCode.member_count())
/*  946 */           return false;
/*      */       }
/*  948 */       if (i == 16)
/*      */       {
/*  950 */         if (((TypeCode)localObject).default_index() != localTypeCode.default_index())
/*  951 */           return false;
/*      */       }
/*  953 */       if ((i == 18) || (i == 27) || (i == 19) || (i == 20))
/*      */       {
/*  958 */         if (((TypeCode)localObject).length() != localTypeCode.length())
/*  959 */           return false;
/*      */       }
/*  961 */       if (i == 28)
/*      */       {
/*  963 */         if ((((TypeCode)localObject).fixed_digits() != localTypeCode.fixed_digits()) || (((TypeCode)localObject).fixed_scale() != localTypeCode.fixed_scale()))
/*      */         {
/*  965 */           return false;
/*      */         }
/*      */       }
/*      */       int j;
/*  967 */       if (i == 16)
/*      */       {
/*  969 */         for (j = 0; j < ((TypeCode)localObject).member_count(); j++) {
/*  970 */           if (((TypeCode)localObject).member_label(j) != localTypeCode.member_label(j))
/*  971 */             return false;
/*      */         }
/*  973 */         if (!((TypeCode)localObject).discriminator_type().equivalent(localTypeCode.discriminator_type()))
/*      */         {
/*  975 */           return false;
/*      */         }
/*      */       }
/*  977 */       if ((i == 21) || (i == 30) || (i == 19) || (i == 20))
/*      */       {
/*  982 */         if (!((TypeCode)localObject).content_type().equivalent(localTypeCode.content_type()))
/*  983 */           return false;
/*      */       }
/*  985 */       if ((i == 15) || (i == 16) || (i == 22) || (i == 29))
/*      */       {
/*  990 */         for (j = 0; j < ((TypeCode)localObject).member_count(); j++)
/*  991 */           if (!((TypeCode)localObject).member_type(j).equivalent(localTypeCode.member_type(j)))
/*      */           {
/*  993 */             return false;
/*      */           }
/*      */       }
/*      */     }
/*      */     catch (BadKind localBadKind2) {
/*  998 */       throw this.wrapper.badkindCannotOccur();
/*      */     }
/*      */     catch (Bounds localBounds) {
/* 1001 */       throw this.wrapper.boundsCannotOccur();
/*      */     }
/*      */ 
/* 1005 */     return true;
/*      */   }
/*      */ 
/*      */   public TypeCode get_compact_typecode()
/*      */   {
/* 1012 */     return this;
/*      */   }
/*      */ 
/*      */   public TCKind kind()
/*      */   {
/* 1017 */     if (this._kind == -1)
/* 1018 */       return indirectType().kind();
/* 1019 */     return TCKind.from_int(this._kind);
/*      */   }
/*      */ 
/*      */   public boolean is_recursive()
/*      */   {
/* 1026 */     return this._kind == -1;
/*      */   }
/*      */ 
/*      */   public String id()
/*      */     throws BadKind
/*      */   {
/* 1032 */     switch (this._kind)
/*      */     {
/*      */     case -1:
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 21:
/*      */     case 22:
/*      */     case 29:
/*      */     case 30:
/*      */     case 31:
/*      */     case 32:
/* 1047 */       return this._id;
/*      */     case 0:
/*      */     case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/*      */     case 13:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*      */     case 26:
/*      */     case 27:
/* 1050 */     case 28: } throw new BadKind();
/*      */   }
/*      */ 
/*      */   public String name()
/*      */     throws BadKind
/*      */   {
/* 1057 */     switch (this._kind) {
/*      */     case -1:
/* 1059 */       return indirectType().name();
/*      */     case 14:
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 21:
/*      */     case 22:
/*      */     case 29:
/*      */     case 30:
/*      */     case 31:
/*      */     case 32:
/* 1070 */       return this._name;
/*      */     case 0:
/*      */     case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/*      */     case 13:
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*      */     case 26:
/*      */     case 27:
/* 1072 */     case 28: } throw new BadKind();
/*      */   }
/*      */ 
/*      */   public int member_count()
/*      */     throws BadKind
/*      */   {
/* 1079 */     switch (this._kind) {
/*      */     case -1:
/* 1081 */       return indirectType().member_count();
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 22:
/*      */     case 29:
/* 1087 */       return this._memberCount;
/*      */     }
/* 1089 */     throw new BadKind();
/*      */   }
/*      */ 
/*      */   public String member_name(int paramInt)
/*      */     throws BadKind, Bounds
/*      */   {
/* 1096 */     switch (this._kind) {
/*      */     case -1:
/* 1098 */       return indirectType().member_name(paramInt);
/*      */     case 15:
/*      */     case 16:
/*      */     case 17:
/*      */     case 22:
/*      */     case 29:
/*      */       try {
/* 1105 */         return this._memberNames[paramInt];
/*      */       } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/* 1107 */         throw new Bounds();
/*      */       }
/*      */     }
/* 1110 */     throw new BadKind();
/*      */   }
/*      */ 
/*      */   public TypeCode member_type(int paramInt)
/*      */     throws BadKind, Bounds
/*      */   {
/* 1117 */     switch (this._kind) {
/*      */     case -1:
/* 1119 */       return indirectType().member_type(paramInt);
/*      */     case 15:
/*      */     case 16:
/*      */     case 22:
/*      */     case 29:
/*      */       try {
/* 1125 */         return this._memberTypes[paramInt];
/*      */       } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/* 1127 */         throw new Bounds();
/*      */       }
/*      */     }
/* 1130 */     throw new BadKind();
/*      */   }
/*      */ 
/*      */   public Any member_label(int paramInt)
/*      */     throws BadKind, Bounds
/*      */   {
/* 1137 */     switch (this._kind) {
/*      */     case -1:
/* 1139 */       return indirectType().member_label(paramInt);
/*      */     case 16:
/*      */       try
/*      */       {
/* 1143 */         return new AnyImpl(this._orb, this._unionLabels[paramInt]);
/*      */       } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/* 1145 */         throw new Bounds();
/*      */       }
/*      */     }
/* 1148 */     throw new BadKind();
/*      */   }
/*      */ 
/*      */   public TypeCode discriminator_type()
/*      */     throws BadKind
/*      */   {
/* 1155 */     switch (this._kind) {
/*      */     case -1:
/* 1157 */       return indirectType().discriminator_type();
/*      */     case 16:
/* 1159 */       return this._discriminator;
/*      */     }
/* 1161 */     throw new BadKind();
/*      */   }
/*      */ 
/*      */   public int default_index()
/*      */     throws BadKind
/*      */   {
/* 1168 */     switch (this._kind) {
/*      */     case -1:
/* 1170 */       return indirectType().default_index();
/*      */     case 16:
/* 1172 */       return this._defaultIndex;
/*      */     }
/* 1174 */     throw new BadKind();
/*      */   }
/*      */ 
/*      */   public int length()
/*      */     throws BadKind
/*      */   {
/* 1181 */     switch (this._kind) {
/*      */     case -1:
/* 1183 */       return indirectType().length();
/*      */     case 18:
/*      */     case 19:
/*      */     case 20:
/*      */     case 27:
/* 1188 */       return this._length;
/*      */     }
/* 1190 */     throw new BadKind();
/*      */   }
/*      */ 
/*      */   public TypeCode content_type()
/*      */     throws BadKind
/*      */   {
/* 1197 */     switch (this._kind) {
/*      */     case -1:
/* 1199 */       return indirectType().content_type();
/*      */     case 19:
/* 1201 */       return lazy_content_type();
/*      */     case 20:
/*      */     case 21:
/*      */     case 30:
/* 1205 */       return this._contentType;
/*      */     }
/* 1207 */     throw new BadKind();
/*      */   }
/*      */ 
/*      */   public short fixed_digits() throws BadKind
/*      */   {
/* 1212 */     switch (this._kind) {
/*      */     case 28:
/* 1214 */       return this._digits;
/*      */     }
/* 1216 */     throw new BadKind();
/*      */   }
/*      */ 
/*      */   public short fixed_scale() throws BadKind
/*      */   {
/* 1221 */     switch (this._kind) {
/*      */     case 28:
/* 1223 */       return this._scale;
/*      */     }
/* 1225 */     throw new BadKind();
/*      */   }
/*      */ 
/*      */   public short member_visibility(int paramInt)
/*      */     throws BadKind, Bounds
/*      */   {
/* 1231 */     switch (this._kind) {
/*      */     case -1:
/* 1233 */       return indirectType().member_visibility(paramInt);
/*      */     case 29:
/*      */       try {
/* 1236 */         return this._memberAccess[paramInt];
/*      */       } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/* 1238 */         throw new Bounds();
/*      */       }
/*      */     }
/* 1241 */     throw new BadKind();
/*      */   }
/*      */ 
/*      */   public short type_modifier() throws BadKind
/*      */   {
/* 1246 */     switch (this._kind) {
/*      */     case -1:
/* 1248 */       return indirectType().type_modifier();
/*      */     case 29:
/* 1250 */       return this._type_modifier;
/*      */     }
/* 1252 */     throw new BadKind();
/*      */   }
/*      */ 
/*      */   public TypeCode concrete_base_type() throws BadKind
/*      */   {
/* 1257 */     switch (this._kind) {
/*      */     case -1:
/* 1259 */       return indirectType().concrete_base_type();
/*      */     case 29:
/* 1261 */       return this._concrete_base;
/*      */     }
/* 1263 */     throw new BadKind();
/*      */   }
/*      */ 
/*      */   public void read_value(org.omg.CORBA_2_3.portable.InputStream paramInputStream)
/*      */   {
/* 1268 */     if ((paramInputStream instanceof TypeCodeReader))
/*      */     {
/* 1270 */       if (read_value_kind((TypeCodeReader)paramInputStream))
/* 1271 */         read_value_body(paramInputStream);
/* 1272 */     } else if ((paramInputStream instanceof CDRInputStream)) {
/* 1273 */       WrapperInputStream localWrapperInputStream = new WrapperInputStream((CDRInputStream)paramInputStream);
/*      */ 
/* 1276 */       if (read_value_kind(localWrapperInputStream))
/* 1277 */         read_value_body(localWrapperInputStream);
/*      */     } else {
/* 1279 */       read_value_kind(paramInputStream);
/* 1280 */       read_value_body(paramInputStream);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void read_value_recursive(TypeCodeInputStream paramTypeCodeInputStream)
/*      */   {
/* 1286 */     if ((paramTypeCodeInputStream instanceof TypeCodeReader)) {
/* 1287 */       if (read_value_kind(paramTypeCodeInputStream))
/* 1288 */         read_value_body(paramTypeCodeInputStream);
/*      */     } else {
/* 1290 */       read_value_kind(paramTypeCodeInputStream);
/* 1291 */       read_value_body(paramTypeCodeInputStream);
/*      */     }
/*      */   }
/*      */ 
/*      */   boolean read_value_kind(TypeCodeReader paramTypeCodeReader)
/*      */   {
/* 1297 */     this._kind = paramTypeCodeReader.read_long();
/*      */ 
/* 1300 */     int i = paramTypeCodeReader.getTopLevelPosition() - 4;
/*      */ 
/* 1303 */     if (((this._kind < 0) || (this._kind > typeTable.length)) && (this._kind != -1)) {
/* 1304 */       throw this.wrapper.cannotMarshalBadTckind();
/*      */     }
/*      */ 
/* 1308 */     if (this._kind == 31) {
/* 1309 */       throw this.wrapper.cannotMarshalNative();
/*      */     }
/*      */ 
/* 1313 */     TypeCodeReader localTypeCodeReader = paramTypeCodeReader.getTopLevelStream();
/*      */ 
/* 1315 */     if (this._kind == -1) {
/* 1316 */       int j = paramTypeCodeReader.read_long();
/* 1317 */       if (j > -4) {
/* 1318 */         throw this.wrapper.invalidIndirection(new Integer(j));
/*      */       }
/*      */ 
/* 1323 */       int k = paramTypeCodeReader.getTopLevelPosition();
/*      */ 
/* 1325 */       int m = k - 4 + j;
/*      */ 
/* 1332 */       TypeCodeImpl localTypeCodeImpl = localTypeCodeReader.getTypeCodeAtPosition(m);
/* 1333 */       if (localTypeCodeImpl == null)
/* 1334 */         throw this.wrapper.indirectionNotFound(new Integer(m));
/* 1335 */       setIndirectType(localTypeCodeImpl);
/* 1336 */       return false;
/*      */     }
/*      */ 
/* 1339 */     localTypeCodeReader.addTypeCodeAtPosition(this, i);
/* 1340 */     return true;
/*      */   }
/*      */ 
/*      */   void read_value_kind(org.omg.CORBA_2_3.portable.InputStream paramInputStream)
/*      */   {
/* 1345 */     this._kind = paramInputStream.read_long();
/*      */ 
/* 1348 */     if (((this._kind < 0) || (this._kind > typeTable.length)) && (this._kind != -1)) {
/* 1349 */       throw this.wrapper.cannotMarshalBadTckind();
/*      */     }
/*      */ 
/* 1352 */     if (this._kind == 31) {
/* 1353 */       throw this.wrapper.cannotMarshalNative();
/*      */     }
/* 1355 */     if (this._kind == -1)
/* 1356 */       throw this.wrapper.recursiveTypecodeError();
/*      */   }
/*      */ 
/*      */   void read_value_body(org.omg.CORBA_2_3.portable.InputStream paramInputStream)
/*      */   {
/* 1364 */     switch (typeTable[this._kind])
/*      */     {
/*      */     case 0:
/* 1367 */       break;
/*      */     case 1:
/* 1370 */       switch (this._kind) {
/*      */       case 18:
/*      */       case 27:
/* 1373 */         this._length = paramInputStream.read_long();
/* 1374 */         break;
/*      */       case 28:
/* 1376 */         this._digits = paramInputStream.read_ushort();
/* 1377 */         this._scale = paramInputStream.read_short();
/* 1378 */         break;
/*      */       default:
/* 1380 */         throw this.wrapper.invalidSimpleTypecode();
/*      */       }
/*      */ 
/*      */       break;
/*      */     case 2:
/* 1386 */       TypeCodeInputStream localTypeCodeInputStream = TypeCodeInputStream.readEncapsulation(paramInputStream, paramInputStream.orb());
/*      */       int i;
/* 1389 */       switch (this._kind)
/*      */       {
/*      */       case 14:
/*      */       case 32:
/* 1395 */         setId(localTypeCodeInputStream.read_string());
/*      */ 
/* 1397 */         this._name = localTypeCodeInputStream.read_string();
/*      */ 
/* 1399 */         break;
/*      */       case 16:
/* 1404 */         setId(localTypeCodeInputStream.read_string());
/*      */ 
/* 1407 */         this._name = localTypeCodeInputStream.read_string();
/*      */ 
/* 1410 */         this._discriminator = new TypeCodeImpl((com.sun.corba.se.spi.orb.ORB)paramInputStream.orb());
/* 1411 */         this._discriminator.read_value_recursive(localTypeCodeInputStream);
/*      */ 
/* 1414 */         this._defaultIndex = localTypeCodeInputStream.read_long();
/*      */ 
/* 1417 */         this._memberCount = localTypeCodeInputStream.read_long();
/*      */ 
/* 1420 */         this._unionLabels = new AnyImpl[this._memberCount];
/* 1421 */         this._memberNames = new String[this._memberCount];
/* 1422 */         this._memberTypes = new TypeCodeImpl[this._memberCount];
/*      */ 
/* 1425 */         for (i = 0; i < this._memberCount; i++) {
/* 1426 */           this._unionLabels[i] = new AnyImpl((com.sun.corba.se.spi.orb.ORB)paramInputStream.orb());
/* 1427 */           if (i == this._defaultIndex)
/*      */           {
/* 1429 */             this._unionLabels[i].insert_octet(localTypeCodeInputStream.read_octet());
/*      */           }
/* 1431 */           else switch (realType(this._discriminator).kind().value()) {
/*      */             case 2:
/* 1433 */               this._unionLabels[i].insert_short(localTypeCodeInputStream.read_short());
/* 1434 */               break;
/*      */             case 3:
/* 1436 */               this._unionLabels[i].insert_long(localTypeCodeInputStream.read_long());
/* 1437 */               break;
/*      */             case 4:
/* 1439 */               this._unionLabels[i].insert_ushort(localTypeCodeInputStream.read_short());
/* 1440 */               break;
/*      */             case 5:
/* 1442 */               this._unionLabels[i].insert_ulong(localTypeCodeInputStream.read_long());
/* 1443 */               break;
/*      */             case 6:
/* 1445 */               this._unionLabels[i].insert_float(localTypeCodeInputStream.read_float());
/* 1446 */               break;
/*      */             case 7:
/* 1448 */               this._unionLabels[i].insert_double(localTypeCodeInputStream.read_double());
/* 1449 */               break;
/*      */             case 8:
/* 1451 */               this._unionLabels[i].insert_boolean(localTypeCodeInputStream.read_boolean());
/* 1452 */               break;
/*      */             case 9:
/* 1454 */               this._unionLabels[i].insert_char(localTypeCodeInputStream.read_char());
/* 1455 */               break;
/*      */             case 17:
/* 1457 */               this._unionLabels[i].type(this._discriminator);
/* 1458 */               this._unionLabels[i].insert_long(localTypeCodeInputStream.read_long());
/* 1459 */               break;
/*      */             case 23:
/* 1461 */               this._unionLabels[i].insert_longlong(localTypeCodeInputStream.read_longlong());
/* 1462 */               break;
/*      */             case 24:
/* 1464 */               this._unionLabels[i].insert_ulonglong(localTypeCodeInputStream.read_longlong());
/* 1465 */               break;
/*      */             case 26:
/* 1471 */               this._unionLabels[i].insert_wchar(localTypeCodeInputStream.read_wchar());
/* 1472 */               break;
/*      */             case 10:
/*      */             case 11:
/*      */             case 12:
/*      */             case 13:
/*      */             case 14:
/*      */             case 15:
/*      */             case 16:
/*      */             case 18:
/*      */             case 19:
/*      */             case 20:
/*      */             case 21:
/*      */             case 22:
/*      */             case 25:
/*      */             default:
/* 1474 */               throw this.wrapper.invalidComplexTypecode();
/*      */             }
/*      */ 
/* 1477 */           this._memberNames[i] = localTypeCodeInputStream.read_string();
/* 1478 */           this._memberTypes[i] = new TypeCodeImpl((com.sun.corba.se.spi.orb.ORB)paramInputStream.orb());
/* 1479 */           this._memberTypes[i].read_value_recursive(localTypeCodeInputStream);
/* 1480 */           this._memberTypes[i].setParent(this);
/*      */         }
/*      */ 
/* 1483 */         break;
/*      */       case 17:
/* 1488 */         setId(localTypeCodeInputStream.read_string());
/*      */ 
/* 1491 */         this._name = localTypeCodeInputStream.read_string();
/*      */ 
/* 1494 */         this._memberCount = localTypeCodeInputStream.read_long();
/*      */ 
/* 1497 */         this._memberNames = new String[this._memberCount];
/*      */ 
/* 1500 */         for (i = 0; i < this._memberCount; i++) {
/* 1501 */           this._memberNames[i] = localTypeCodeInputStream.read_string();
/*      */         }
/* 1503 */         break;
/*      */       case 19:
/* 1508 */         this._contentType = new TypeCodeImpl((com.sun.corba.se.spi.orb.ORB)paramInputStream.orb());
/* 1509 */         this._contentType.read_value_recursive(localTypeCodeInputStream);
/*      */ 
/* 1512 */         this._length = localTypeCodeInputStream.read_long();
/*      */ 
/* 1514 */         break;
/*      */       case 20:
/* 1519 */         this._contentType = new TypeCodeImpl((com.sun.corba.se.spi.orb.ORB)paramInputStream.orb());
/* 1520 */         this._contentType.read_value_recursive(localTypeCodeInputStream);
/*      */ 
/* 1523 */         this._length = localTypeCodeInputStream.read_long();
/*      */ 
/* 1525 */         break;
/*      */       case 21:
/*      */       case 30:
/* 1531 */         setId(localTypeCodeInputStream.read_string());
/*      */ 
/* 1534 */         this._name = localTypeCodeInputStream.read_string();
/*      */ 
/* 1537 */         this._contentType = new TypeCodeImpl((com.sun.corba.se.spi.orb.ORB)paramInputStream.orb());
/* 1538 */         this._contentType.read_value_recursive(localTypeCodeInputStream);
/*      */ 
/* 1540 */         break;
/*      */       case 15:
/*      */       case 22:
/* 1546 */         setId(localTypeCodeInputStream.read_string());
/*      */ 
/* 1549 */         this._name = localTypeCodeInputStream.read_string();
/*      */ 
/* 1552 */         this._memberCount = localTypeCodeInputStream.read_long();
/*      */ 
/* 1555 */         this._memberNames = new String[this._memberCount];
/* 1556 */         this._memberTypes = new TypeCodeImpl[this._memberCount];
/*      */ 
/* 1559 */         for (i = 0; i < this._memberCount; i++) {
/* 1560 */           this._memberNames[i] = localTypeCodeInputStream.read_string();
/* 1561 */           this._memberTypes[i] = new TypeCodeImpl((com.sun.corba.se.spi.orb.ORB)paramInputStream.orb());
/*      */ 
/* 1564 */           this._memberTypes[i].read_value_recursive(localTypeCodeInputStream);
/* 1565 */           this._memberTypes[i].setParent(this);
/*      */         }
/*      */ 
/* 1568 */         break;
/*      */       case 29:
/* 1573 */         setId(localTypeCodeInputStream.read_string());
/*      */ 
/* 1576 */         this._name = localTypeCodeInputStream.read_string();
/*      */ 
/* 1579 */         this._type_modifier = localTypeCodeInputStream.read_short();
/*      */ 
/* 1582 */         this._concrete_base = new TypeCodeImpl((com.sun.corba.se.spi.orb.ORB)paramInputStream.orb());
/* 1583 */         this._concrete_base.read_value_recursive(localTypeCodeInputStream);
/* 1584 */         if (this._concrete_base.kind().value() == 0) {
/* 1585 */           this._concrete_base = null;
/*      */         }
/*      */ 
/* 1589 */         this._memberCount = localTypeCodeInputStream.read_long();
/*      */ 
/* 1592 */         this._memberNames = new String[this._memberCount];
/* 1593 */         this._memberTypes = new TypeCodeImpl[this._memberCount];
/* 1594 */         this._memberAccess = new short[this._memberCount];
/*      */ 
/* 1597 */         for (i = 0; i < this._memberCount; i++) {
/* 1598 */           this._memberNames[i] = localTypeCodeInputStream.read_string();
/* 1599 */           this._memberTypes[i] = new TypeCodeImpl((com.sun.corba.se.spi.orb.ORB)paramInputStream.orb());
/*      */ 
/* 1602 */           this._memberTypes[i].read_value_recursive(localTypeCodeInputStream);
/* 1603 */           this._memberTypes[i].setParent(this);
/* 1604 */           this._memberAccess[i] = localTypeCodeInputStream.read_short();
/*      */         }
/*      */ 
/* 1607 */         break;
/*      */       case 18:
/*      */       case 23:
/*      */       case 24:
/*      */       case 25:
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 31:
/*      */       default:
/* 1610 */         throw this.wrapper.invalidTypecodeKindMarshal();
/*      */       }
/*      */       break;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void write_value(org.omg.CORBA_2_3.portable.OutputStream paramOutputStream)
/*      */   {
/* 1620 */     if ((paramOutputStream instanceof TypeCodeOutputStream)) {
/* 1621 */       write_value((TypeCodeOutputStream)paramOutputStream);
/*      */     } else {
/* 1623 */       TypeCodeOutputStream localTypeCodeOutputStream = null;
/*      */ 
/* 1625 */       if (this.outBuffer == null) {
/* 1626 */         localTypeCodeOutputStream = TypeCodeOutputStream.wrapOutputStream(paramOutputStream);
/* 1627 */         write_value(localTypeCodeOutputStream);
/* 1628 */         if (this.cachingEnabled)
/*      */         {
/* 1630 */           this.outBuffer = localTypeCodeOutputStream.getTypeCodeBuffer();
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1641 */       if ((this.cachingEnabled) && (this.outBuffer != null)) {
/* 1642 */         paramOutputStream.write_long(this._kind);
/* 1643 */         paramOutputStream.write_octet_array(this.outBuffer, 0, this.outBuffer.length);
/*      */       }
/*      */       else {
/* 1646 */         localTypeCodeOutputStream.writeRawBuffer(paramOutputStream, this._kind);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void write_value(TypeCodeOutputStream paramTypeCodeOutputStream)
/*      */   {
/* 1654 */     if (this._kind == 31) {
/* 1655 */       throw this.wrapper.cannotMarshalNative();
/*      */     }
/* 1657 */     TypeCodeOutputStream localTypeCodeOutputStream1 = paramTypeCodeOutputStream.getTopLevelStream();
/*      */     int j;
/* 1660 */     if (this._kind == -1)
/*      */     {
/* 1665 */       int i = localTypeCodeOutputStream1.getPositionForID(this._id);
/* 1666 */       j = paramTypeCodeOutputStream.getTopLevelPosition();
/*      */ 
/* 1670 */       paramTypeCodeOutputStream.writeIndirection(-1, i);
/*      */ 
/* 1672 */       return;
/*      */     }
/*      */ 
/* 1679 */     paramTypeCodeOutputStream.write_long(this._kind);
/*      */ 
/* 1688 */     localTypeCodeOutputStream1.addIDAtPosition(this._id, paramTypeCodeOutputStream.getTopLevelPosition() - 4);
/*      */ 
/* 1690 */     switch (typeTable[this._kind])
/*      */     {
/*      */     case 0:
/* 1693 */       break;
/*      */     case 1:
/* 1696 */       switch (this._kind)
/*      */       {
/*      */       case 18:
/*      */       case 27:
/* 1700 */         paramTypeCodeOutputStream.write_long(this._length);
/* 1701 */         break;
/*      */       case 28:
/* 1703 */         paramTypeCodeOutputStream.write_ushort(this._digits);
/* 1704 */         paramTypeCodeOutputStream.write_short(this._scale);
/* 1705 */         break;
/*      */       default:
/* 1708 */         throw this.wrapper.invalidSimpleTypecode();
/*      */       }
/*      */ 
/*      */       break;
/*      */     case 2:
/* 1715 */       TypeCodeOutputStream localTypeCodeOutputStream2 = paramTypeCodeOutputStream.createEncapsulation(paramTypeCodeOutputStream.orb());
/*      */ 
/* 1717 */       switch (this._kind)
/*      */       {
/*      */       case 14:
/*      */       case 32:
/* 1723 */         localTypeCodeOutputStream2.write_string(this._id);
/*      */ 
/* 1726 */         localTypeCodeOutputStream2.write_string(this._name);
/*      */ 
/* 1728 */         break;
/*      */       case 16:
/* 1733 */         localTypeCodeOutputStream2.write_string(this._id);
/*      */ 
/* 1736 */         localTypeCodeOutputStream2.write_string(this._name);
/*      */ 
/* 1739 */         this._discriminator.write_value(localTypeCodeOutputStream2);
/*      */ 
/* 1742 */         localTypeCodeOutputStream2.write_long(this._defaultIndex);
/*      */ 
/* 1745 */         localTypeCodeOutputStream2.write_long(this._memberCount);
/*      */ 
/* 1748 */         for (j = 0; j < this._memberCount; j++)
/*      */         {
/* 1751 */           if (j == this._defaultIndex) {
/* 1752 */             localTypeCodeOutputStream2.write_octet(this._unionLabels[j].extract_octet());
/*      */           }
/*      */           else {
/* 1755 */             switch (realType(this._discriminator).kind().value()) {
/*      */             case 2:
/* 1757 */               localTypeCodeOutputStream2.write_short(this._unionLabels[j].extract_short());
/* 1758 */               break;
/*      */             case 3:
/* 1760 */               localTypeCodeOutputStream2.write_long(this._unionLabels[j].extract_long());
/* 1761 */               break;
/*      */             case 4:
/* 1763 */               localTypeCodeOutputStream2.write_short(this._unionLabels[j].extract_ushort());
/* 1764 */               break;
/*      */             case 5:
/* 1766 */               localTypeCodeOutputStream2.write_long(this._unionLabels[j].extract_ulong());
/* 1767 */               break;
/*      */             case 6:
/* 1769 */               localTypeCodeOutputStream2.write_float(this._unionLabels[j].extract_float());
/* 1770 */               break;
/*      */             case 7:
/* 1772 */               localTypeCodeOutputStream2.write_double(this._unionLabels[j].extract_double());
/* 1773 */               break;
/*      */             case 8:
/* 1775 */               localTypeCodeOutputStream2.write_boolean(this._unionLabels[j].extract_boolean());
/* 1776 */               break;
/*      */             case 9:
/* 1778 */               localTypeCodeOutputStream2.write_char(this._unionLabels[j].extract_char());
/* 1779 */               break;
/*      */             case 17:
/* 1781 */               localTypeCodeOutputStream2.write_long(this._unionLabels[j].extract_long());
/* 1782 */               break;
/*      */             case 23:
/* 1784 */               localTypeCodeOutputStream2.write_longlong(this._unionLabels[j].extract_longlong());
/* 1785 */               break;
/*      */             case 24:
/* 1787 */               localTypeCodeOutputStream2.write_longlong(this._unionLabels[j].extract_ulonglong());
/* 1788 */               break;
/*      */             case 26:
/* 1794 */               localTypeCodeOutputStream2.write_wchar(this._unionLabels[j].extract_wchar());
/* 1795 */               break;
/*      */             case 10:
/*      */             case 11:
/*      */             case 12:
/*      */             case 13:
/*      */             case 14:
/*      */             case 15:
/*      */             case 16:
/*      */             case 18:
/*      */             case 19:
/*      */             case 20:
/*      */             case 21:
/*      */             case 22:
/*      */             case 25:
/*      */             default:
/* 1797 */               throw this.wrapper.invalidComplexTypecode();
/*      */             }
/*      */           }
/* 1800 */           localTypeCodeOutputStream2.write_string(this._memberNames[j]);
/* 1801 */           this._memberTypes[j].write_value(localTypeCodeOutputStream2);
/*      */         }
/*      */ 
/* 1804 */         break;
/*      */       case 17:
/* 1809 */         localTypeCodeOutputStream2.write_string(this._id);
/*      */ 
/* 1812 */         localTypeCodeOutputStream2.write_string(this._name);
/*      */ 
/* 1815 */         localTypeCodeOutputStream2.write_long(this._memberCount);
/*      */ 
/* 1818 */         for (j = 0; j < this._memberCount; j++) {
/* 1819 */           localTypeCodeOutputStream2.write_string(this._memberNames[j]);
/*      */         }
/* 1821 */         break;
/*      */       case 19:
/* 1826 */         lazy_content_type().write_value(localTypeCodeOutputStream2);
/*      */ 
/* 1829 */         localTypeCodeOutputStream2.write_long(this._length);
/*      */ 
/* 1831 */         break;
/*      */       case 20:
/* 1836 */         this._contentType.write_value(localTypeCodeOutputStream2);
/*      */ 
/* 1839 */         localTypeCodeOutputStream2.write_long(this._length);
/*      */ 
/* 1841 */         break;
/*      */       case 21:
/*      */       case 30:
/* 1847 */         localTypeCodeOutputStream2.write_string(this._id);
/*      */ 
/* 1850 */         localTypeCodeOutputStream2.write_string(this._name);
/*      */ 
/* 1853 */         this._contentType.write_value(localTypeCodeOutputStream2);
/*      */ 
/* 1855 */         break;
/*      */       case 15:
/*      */       case 22:
/* 1861 */         localTypeCodeOutputStream2.write_string(this._id);
/*      */ 
/* 1864 */         localTypeCodeOutputStream2.write_string(this._name);
/*      */ 
/* 1867 */         localTypeCodeOutputStream2.write_long(this._memberCount);
/*      */ 
/* 1870 */         for (j = 0; j < this._memberCount; j++) {
/* 1871 */           localTypeCodeOutputStream2.write_string(this._memberNames[j]);
/*      */ 
/* 1874 */           this._memberTypes[j].write_value(localTypeCodeOutputStream2);
/*      */         }
/*      */ 
/* 1877 */         break;
/*      */       case 29:
/* 1882 */         localTypeCodeOutputStream2.write_string(this._id);
/*      */ 
/* 1885 */         localTypeCodeOutputStream2.write_string(this._name);
/*      */ 
/* 1888 */         localTypeCodeOutputStream2.write_short(this._type_modifier);
/*      */ 
/* 1891 */         if (this._concrete_base == null)
/* 1892 */           this._orb.get_primitive_tc(0).write_value(localTypeCodeOutputStream2);
/*      */         else {
/* 1894 */           this._concrete_base.write_value(localTypeCodeOutputStream2);
/*      */         }
/*      */ 
/* 1898 */         localTypeCodeOutputStream2.write_long(this._memberCount);
/*      */ 
/* 1901 */         for (j = 0; j < this._memberCount; j++) {
/* 1902 */           localTypeCodeOutputStream2.write_string(this._memberNames[j]);
/*      */ 
/* 1905 */           this._memberTypes[j].write_value(localTypeCodeOutputStream2);
/* 1906 */           localTypeCodeOutputStream2.write_short(this._memberAccess[j]);
/*      */         }
/*      */ 
/* 1909 */         break;
/*      */       case 18:
/*      */       case 23:
/*      */       case 24:
/*      */       case 25:
/*      */       case 26:
/*      */       case 27:
/*      */       case 28:
/*      */       case 31:
/*      */       default:
/* 1912 */         throw this.wrapper.invalidTypecodeKindMarshal();
/*      */       }
/*      */ 
/* 1916 */       localTypeCodeOutputStream2.writeOctetSequenceTo(paramTypeCodeOutputStream);
/* 1917 */       break;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected void copy(org.omg.CORBA.portable.InputStream paramInputStream, org.omg.CORBA.portable.OutputStream paramOutputStream)
/*      */   {
/*      */     Object localObject;
/*      */     int i2;
/* 1932 */     switch (this._kind)
/*      */     {
/*      */     case 0:
/*      */     case 1:
/*      */     case 31:
/*      */     case 32:
/* 1938 */       break;
/*      */     case 2:
/*      */     case 4:
/* 1942 */       paramOutputStream.write_short(paramInputStream.read_short());
/* 1943 */       break;
/*      */     case 3:
/*      */     case 5:
/* 1947 */       paramOutputStream.write_long(paramInputStream.read_long());
/* 1948 */       break;
/*      */     case 6:
/* 1951 */       paramOutputStream.write_float(paramInputStream.read_float());
/* 1952 */       break;
/*      */     case 7:
/* 1955 */       paramOutputStream.write_double(paramInputStream.read_double());
/* 1956 */       break;
/*      */     case 23:
/*      */     case 24:
/* 1960 */       paramOutputStream.write_longlong(paramInputStream.read_longlong());
/* 1961 */       break;
/*      */     case 25:
/* 1964 */       throw this.wrapper.tkLongDoubleNotSupported();
/*      */     case 8:
/* 1967 */       paramOutputStream.write_boolean(paramInputStream.read_boolean());
/* 1968 */       break;
/*      */     case 9:
/* 1971 */       paramOutputStream.write_char(paramInputStream.read_char());
/* 1972 */       break;
/*      */     case 26:
/* 1975 */       paramOutputStream.write_wchar(paramInputStream.read_wchar());
/* 1976 */       break;
/*      */     case 10:
/* 1979 */       paramOutputStream.write_octet(paramInputStream.read_octet());
/* 1980 */       break;
/*      */     case 18:
/* 1985 */       localObject = paramInputStream.read_string();
/*      */ 
/* 1987 */       if ((this._length != 0) && (((String)localObject).length() > this._length)) {
/* 1988 */         throw this.wrapper.badStringBounds(new Integer(((String)localObject).length()), new Integer(this._length));
/*      */       }
/* 1990 */       paramOutputStream.write_string((String)localObject);
/*      */ 
/* 1992 */       break;
/*      */     case 27:
/* 1997 */       localObject = paramInputStream.read_wstring();
/*      */ 
/* 1999 */       if ((this._length != 0) && (((String)localObject).length() > this._length)) {
/* 2000 */         throw this.wrapper.badStringBounds(new Integer(((String)localObject).length()), new Integer(this._length));
/*      */       }
/* 2002 */       paramOutputStream.write_wstring((String)localObject);
/*      */ 
/* 2004 */       break;
/*      */     case 28:
/* 2008 */       paramOutputStream.write_ushort(paramInputStream.read_ushort());
/* 2009 */       paramOutputStream.write_short(paramInputStream.read_short());
/*      */ 
/* 2011 */       break;
/*      */     case 11:
/* 2016 */       localObject = ((CDRInputStream)paramInputStream).orb().create_any();
/* 2017 */       TypeCodeImpl localTypeCodeImpl = new TypeCodeImpl((com.sun.corba.se.spi.orb.ORB)paramOutputStream.orb());
/* 2018 */       localTypeCodeImpl.read_value((org.omg.CORBA_2_3.portable.InputStream)paramInputStream);
/* 2019 */       localTypeCodeImpl.write_value((org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream);
/* 2020 */       ((Any)localObject).read_value(paramInputStream, localTypeCodeImpl);
/* 2021 */       ((Any)localObject).write_value(paramOutputStream);
/* 2022 */       break;
/*      */     case 12:
/* 2027 */       paramOutputStream.write_TypeCode(paramInputStream.read_TypeCode());
/* 2028 */       break;
/*      */     case 13:
/* 2033 */       paramOutputStream.write_Principal(paramInputStream.read_Principal());
/* 2034 */       break;
/*      */     case 14:
/* 2039 */       paramOutputStream.write_Object(paramInputStream.read_Object());
/* 2040 */       break;
/*      */     case 22:
/* 2045 */       paramOutputStream.write_string(paramInputStream.read_string());
/*      */     case 15:
/*      */     case 29:
/* 2053 */       for (int i = 0; i < this._memberTypes.length; i++) {
/* 2054 */         this._memberTypes[i].copy(paramInputStream, paramOutputStream);
/*      */       }
/* 2056 */       break;
/*      */     case 16:
/* 2079 */       AnyImpl localAnyImpl = new AnyImpl((com.sun.corba.se.spi.orb.ORB)paramInputStream.orb());
/*      */       int k;
/*      */       long l;
/* 2081 */       switch (realType(this._discriminator).kind().value())
/*      */       {
/*      */       case 2:
/* 2084 */         short s = paramInputStream.read_short();
/* 2085 */         localAnyImpl.insert_short(s);
/* 2086 */         paramOutputStream.write_short(s);
/* 2087 */         break;
/*      */       case 3:
/* 2091 */         k = paramInputStream.read_long();
/* 2092 */         localAnyImpl.insert_long(k);
/* 2093 */         paramOutputStream.write_long(k);
/* 2094 */         break;
/*      */       case 4:
/* 2098 */         k = paramInputStream.read_short();
/* 2099 */         localAnyImpl.insert_ushort(k);
/* 2100 */         paramOutputStream.write_short(k);
/* 2101 */         break;
/*      */       case 5:
/* 2105 */         int m = paramInputStream.read_long();
/* 2106 */         localAnyImpl.insert_ulong(m);
/* 2107 */         paramOutputStream.write_long(m);
/* 2108 */         break;
/*      */       case 6:
/* 2112 */         float f = paramInputStream.read_float();
/* 2113 */         localAnyImpl.insert_float(f);
/* 2114 */         paramOutputStream.write_float(f);
/* 2115 */         break;
/*      */       case 7:
/* 2119 */         double d = paramInputStream.read_double();
/* 2120 */         localAnyImpl.insert_double(d);
/* 2121 */         paramOutputStream.write_double(d);
/* 2122 */         break;
/*      */       case 8:
/* 2126 */         boolean bool = paramInputStream.read_boolean();
/* 2127 */         localAnyImpl.insert_boolean(bool);
/* 2128 */         paramOutputStream.write_boolean(bool);
/* 2129 */         break;
/*      */       case 9:
/* 2133 */         char c = paramInputStream.read_char();
/* 2134 */         localAnyImpl.insert_char(c);
/* 2135 */         paramOutputStream.write_char(c);
/* 2136 */         break;
/*      */       case 17:
/* 2140 */         int n = paramInputStream.read_long();
/* 2141 */         localAnyImpl.type(this._discriminator);
/* 2142 */         localAnyImpl.insert_long(n);
/* 2143 */         paramOutputStream.write_long(n);
/* 2144 */         break;
/*      */       case 23:
/* 2148 */         l = paramInputStream.read_longlong();
/* 2149 */         localAnyImpl.insert_longlong(l);
/* 2150 */         paramOutputStream.write_longlong(l);
/* 2151 */         break;
/*      */       case 24:
/* 2155 */         l = paramInputStream.read_longlong();
/* 2156 */         localAnyImpl.insert_ulonglong(l);
/* 2157 */         paramOutputStream.write_longlong(l);
/* 2158 */         break;
/*      */       case 26:
/* 2170 */         i1 = paramInputStream.read_wchar();
/* 2171 */         localAnyImpl.insert_wchar(i1);
/* 2172 */         paramOutputStream.write_wchar(i1);
/* 2173 */         break;
/*      */       case 10:
/*      */       case 11:
/*      */       case 12:
/*      */       case 13:
/*      */       case 14:
/*      */       case 15:
/*      */       case 16:
/*      */       case 18:
/*      */       case 19:
/*      */       case 20:
/*      */       case 21:
/*      */       case 22:
/*      */       case 25:
/*      */       default:
/* 2176 */         throw this.wrapper.illegalUnionDiscriminatorType();
/*      */       }
/*      */ 
/* 2183 */       for (int i1 = 0; i1 < this._unionLabels.length; i1++)
/*      */       {
/* 2185 */         if (localAnyImpl.equal(this._unionLabels[i1])) {
/* 2186 */           this._memberTypes[i1].copy(paramInputStream, paramOutputStream);
/* 2187 */           break;
/*      */         }
/*      */       }
/*      */ 
/* 2191 */       if (i1 == this._unionLabels.length)
/*      */       {
/* 2193 */         if (this._defaultIndex != -1)
/*      */         {
/* 2195 */           this._memberTypes[this._defaultIndex].copy(paramInputStream, paramOutputStream); }  } break;
/*      */     case 17:
/* 2201 */       paramOutputStream.write_long(paramInputStream.read_long());
/* 2202 */       break;
/*      */     case 19:
/* 2206 */       int j = paramInputStream.read_long();
/*      */ 
/* 2209 */       if ((this._length != 0) && (j > this._length)) {
/* 2210 */         throw this.wrapper.badSequenceBounds(new Integer(j), new Integer(this._length));
/*      */       }
/*      */ 
/* 2214 */       paramOutputStream.write_long(j);
/*      */ 
/* 2217 */       lazy_content_type();
/* 2218 */       for (i2 = 0; i2 < j; i2++)
/* 2219 */         this._contentType.copy(paramInputStream, paramOutputStream);
/* 2220 */       break;
/*      */     case 20:
/* 2224 */       for (i2 = 0; i2 < this._length; i2++)
/* 2225 */         this._contentType.copy(paramInputStream, paramOutputStream);
/* 2226 */       break;
/*      */     case 21:
/*      */     case 30:
/* 2231 */       this._contentType.copy(paramInputStream, paramOutputStream);
/* 2232 */       break;
/*      */     case -1:
/* 2239 */       indirectType().copy(paramInputStream, paramOutputStream);
/* 2240 */       break;
/*      */     default:
/* 2243 */       throw this.wrapper.invalidTypecodeKindMarshal();
/*      */     }
/*      */   }
/*      */ 
/*      */   protected static short digits(BigDecimal paramBigDecimal)
/*      */   {
/* 2249 */     if (paramBigDecimal == null)
/* 2250 */       return 0;
/* 2251 */     short s = (short)paramBigDecimal.unscaledValue().toString().length();
/* 2252 */     if (paramBigDecimal.signum() == -1)
/* 2253 */       s = (short)(s - 1);
/* 2254 */     return s;
/*      */   }
/*      */ 
/*      */   protected static short scale(BigDecimal paramBigDecimal) {
/* 2258 */     if (paramBigDecimal == null)
/* 2259 */       return 0;
/* 2260 */     return (short)paramBigDecimal.scale();
/*      */   }
/*      */ 
/*      */   int currentUnionMemberIndex(Any paramAny)
/*      */     throws BadKind
/*      */   {
/* 2269 */     if (this._kind != 16)
/* 2270 */       throw new BadKind();
/*      */     try
/*      */     {
/* 2273 */       for (int i = 0; i < member_count(); i++) {
/* 2274 */         if (member_label(i).equal(paramAny)) {
/* 2275 */           return i;
/*      */         }
/*      */       }
/* 2278 */       if (this._defaultIndex != -1)
/* 2279 */         return this._defaultIndex;
/*      */     } catch (BadKind localBadKind) {
/*      */     }
/*      */     catch (Bounds localBounds) {
/*      */     }
/* 2284 */     return -1;
/*      */   }
/*      */ 
/*      */   public String description() {
/* 2288 */     return "TypeCodeImpl with kind " + this._kind + " and id " + this._id;
/*      */   }
/*      */ 
/*      */   public String toString() {
/* 2292 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream(1024);
/* 2293 */     PrintStream localPrintStream = new PrintStream(localByteArrayOutputStream, true);
/* 2294 */     printStream(localPrintStream);
/* 2295 */     return super.toString() + " =\n" + localByteArrayOutputStream.toString();
/*      */   }
/*      */ 
/*      */   public void printStream(PrintStream paramPrintStream) {
/* 2299 */     printStream(paramPrintStream, 0);
/*      */   }
/*      */ 
/*      */   private void printStream(PrintStream paramPrintStream, int paramInt) {
/* 2303 */     if (this._kind == -1) {
/* 2304 */       paramPrintStream.print("indirect " + this._id);
/* 2305 */       return;
/*      */     }
/*      */ 
/* 2308 */     switch (this._kind) {
/*      */     case 0:
/*      */     case 1:
/*      */     case 2:
/*      */     case 3:
/*      */     case 4:
/*      */     case 5:
/*      */     case 6:
/*      */     case 7:
/*      */     case 8:
/*      */     case 9:
/*      */     case 10:
/*      */     case 11:
/*      */     case 12:
/*      */     case 13:
/*      */     case 14:
/*      */     case 23:
/*      */     case 24:
/*      */     case 25:
/*      */     case 26:
/*      */     case 31:
/* 2329 */       paramPrintStream.print(kindNames[this._kind] + " " + this._name);
/* 2330 */       break;
/*      */     case 15:
/*      */     case 22:
/*      */     case 29:
/* 2335 */       paramPrintStream.println(kindNames[this._kind] + " " + this._name + " = {");
/* 2336 */       for (int i = 0; i < this._memberCount; i++)
/*      */       {
/* 2338 */         paramPrintStream.print(indent(paramInt + 1));
/* 2339 */         if (this._memberTypes[i] != null)
/* 2340 */           this._memberTypes[i].printStream(paramPrintStream, paramInt + 1);
/*      */         else
/* 2342 */           paramPrintStream.print("<unknown type>");
/* 2343 */         paramPrintStream.println(" " + this._memberNames[i] + ";");
/*      */       }
/* 2345 */       paramPrintStream.print(indent(paramInt) + "}");
/* 2346 */       break;
/*      */     case 16:
/* 2349 */       paramPrintStream.print("union " + this._name + "...");
/* 2350 */       break;
/*      */     case 17:
/* 2353 */       paramPrintStream.print("enum " + this._name + "...");
/* 2354 */       break;
/*      */     case 18:
/* 2357 */       if (this._length == 0)
/* 2358 */         paramPrintStream.print("unbounded string " + this._name);
/*      */       else
/* 2360 */         paramPrintStream.print("bounded string(" + this._length + ") " + this._name);
/* 2361 */       break;
/*      */     case 19:
/*      */     case 20:
/* 2365 */       paramPrintStream.println(kindNames[this._kind] + "[" + this._length + "] " + this._name + " = {");
/* 2366 */       paramPrintStream.print(indent(paramInt + 1));
/* 2367 */       if (lazy_content_type() != null) {
/* 2368 */         lazy_content_type().printStream(paramPrintStream, paramInt + 1);
/*      */       }
/* 2370 */       paramPrintStream.println(indent(paramInt) + "}");
/* 2371 */       break;
/*      */     case 21:
/* 2374 */       paramPrintStream.print("alias " + this._name + " = " + (this._contentType != null ? this._contentType._name : "<unresolved>"));
/*      */ 
/* 2376 */       break;
/*      */     case 27:
/* 2379 */       paramPrintStream.print("wstring[" + this._length + "] " + this._name);
/* 2380 */       break;
/*      */     case 28:
/* 2383 */       paramPrintStream.print("fixed(" + this._digits + ", " + this._scale + ") " + this._name);
/* 2384 */       break;
/*      */     case 30:
/* 2387 */       paramPrintStream.print("valueBox " + this._name + "...");
/* 2388 */       break;
/*      */     case 32:
/* 2391 */       paramPrintStream.print("abstractInterface " + this._name + "...");
/* 2392 */       break;
/*      */     default:
/* 2395 */       paramPrintStream.print("<unknown type>");
/*      */     }
/*      */   }
/*      */ 
/*      */   private String indent(int paramInt)
/*      */   {
/* 2401 */     String str = "";
/* 2402 */     for (int i = 0; i < paramInt; i++) {
/* 2403 */       str = str + "  ";
/*      */     }
/* 2405 */     return str;
/*      */   }
/*      */ 
/*      */   protected void setCaching(boolean paramBoolean) {
/* 2409 */     this.cachingEnabled = paramBoolean;
/* 2410 */     if (!paramBoolean)
/* 2411 */       this.outBuffer = null;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.corba.TypeCodeImpl
 * JD-Core Version:    0.6.2
 */