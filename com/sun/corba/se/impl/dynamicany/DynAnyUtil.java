/*     */ package com.sun.corba.se.impl.dynamicany;
/*     */ 
/*     */ import com.sun.corba.se.impl.corba.AnyImpl;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.math.BigDecimal;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.TCKind;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.DynamicAny.DynAny;
/*     */ import org.omg.DynamicAny.DynAnyFactoryPackage.InconsistentTypeCode;
/*     */ import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;
/*     */ 
/*     */ public class DynAnyUtil
/*     */ {
/*     */   static boolean isConsistentType(TypeCode paramTypeCode)
/*     */   {
/*  49 */     int i = paramTypeCode.kind().value();
/*  50 */     return (i != 13) && (i != 31) && (i != 32);
/*     */   }
/*     */ 
/*     */   static boolean isConstructedDynAny(DynAny paramDynAny)
/*     */   {
/*  58 */     int i = paramDynAny.type().kind().value();
/*  59 */     return (i == 19) || (i == 15) || (i == 20) || (i == 16) || (i == 17) || (i == 28) || (i == 29) || (i == 30);
/*     */   }
/*     */ 
/*     */   static DynAny createMostDerivedDynAny(Any paramAny, ORB paramORB, boolean paramBoolean)
/*     */     throws InconsistentTypeCode
/*     */   {
/*  72 */     if ((paramAny == null) || (!isConsistentType(paramAny.type()))) {
/*  73 */       throw new InconsistentTypeCode();
/*     */     }
/*  75 */     switch (paramAny.type().kind().value()) {
/*     */     case 19:
/*  77 */       return new DynSequenceImpl(paramORB, paramAny, paramBoolean);
/*     */     case 15:
/*  79 */       return new DynStructImpl(paramORB, paramAny, paramBoolean);
/*     */     case 20:
/*  81 */       return new DynArrayImpl(paramORB, paramAny, paramBoolean);
/*     */     case 16:
/*  83 */       return new DynUnionImpl(paramORB, paramAny, paramBoolean);
/*     */     case 17:
/*  85 */       return new DynEnumImpl(paramORB, paramAny, paramBoolean);
/*     */     case 28:
/*  87 */       return new DynFixedImpl(paramORB, paramAny, paramBoolean);
/*     */     case 29:
/*  89 */       return new DynValueImpl(paramORB, paramAny, paramBoolean);
/*     */     case 30:
/*  91 */       return new DynValueBoxImpl(paramORB, paramAny, paramBoolean);
/*     */     case 18:
/*     */     case 21:
/*     */     case 22:
/*     */     case 23:
/*     */     case 24:
/*     */     case 25:
/*     */     case 26:
/*  93 */     case 27: } return new DynAnyBasicImpl(paramORB, paramAny, paramBoolean);
/*     */   }
/*     */ 
/*     */   static DynAny createMostDerivedDynAny(TypeCode paramTypeCode, ORB paramORB)
/*     */     throws InconsistentTypeCode
/*     */   {
/* 100 */     if ((paramTypeCode == null) || (!isConsistentType(paramTypeCode))) {
/* 101 */       throw new InconsistentTypeCode();
/*     */     }
/* 103 */     switch (paramTypeCode.kind().value()) {
/*     */     case 19:
/* 105 */       return new DynSequenceImpl(paramORB, paramTypeCode);
/*     */     case 15:
/* 107 */       return new DynStructImpl(paramORB, paramTypeCode);
/*     */     case 20:
/* 109 */       return new DynArrayImpl(paramORB, paramTypeCode);
/*     */     case 16:
/* 111 */       return new DynUnionImpl(paramORB, paramTypeCode);
/*     */     case 17:
/* 113 */       return new DynEnumImpl(paramORB, paramTypeCode);
/*     */     case 28:
/* 115 */       return new DynFixedImpl(paramORB, paramTypeCode);
/*     */     case 29:
/* 117 */       return new DynValueImpl(paramORB, paramTypeCode);
/*     */     case 30:
/* 119 */       return new DynValueBoxImpl(paramORB, paramTypeCode);
/*     */     case 18:
/*     */     case 21:
/*     */     case 22:
/*     */     case 23:
/*     */     case 24:
/*     */     case 25:
/*     */     case 26:
/* 121 */     case 27: } return new DynAnyBasicImpl(paramORB, paramTypeCode);
/*     */   }
/*     */ 
/*     */   static Any extractAnyFromStream(TypeCode paramTypeCode, InputStream paramInputStream, ORB paramORB)
/*     */   {
/* 139 */     return AnyImpl.extractAnyFromStream(paramTypeCode, paramInputStream, paramORB);
/*     */   }
/*     */ 
/*     */   static Any createDefaultAnyOfType(TypeCode paramTypeCode, ORB paramORB)
/*     */   {
/* 144 */     ORBUtilSystemException localORBUtilSystemException = ORBUtilSystemException.get(paramORB, "rpc.presentation");
/*     */ 
/* 147 */     Any localAny = paramORB.create_any();
/*     */ 
/* 157 */     switch (paramTypeCode.kind().value())
/*     */     {
/*     */     case 8:
/* 160 */       localAny.insert_boolean(false);
/* 161 */       break;
/*     */     case 2:
/* 164 */       localAny.insert_short((short)0);
/* 165 */       break;
/*     */     case 4:
/* 168 */       localAny.insert_ushort((short)0);
/* 169 */       break;
/*     */     case 3:
/* 172 */       localAny.insert_long(0);
/* 173 */       break;
/*     */     case 5:
/* 176 */       localAny.insert_ulong(0);
/* 177 */       break;
/*     */     case 23:
/* 180 */       localAny.insert_longlong(0L);
/* 181 */       break;
/*     */     case 24:
/* 184 */       localAny.insert_ulonglong(0L);
/* 185 */       break;
/*     */     case 6:
/* 188 */       localAny.insert_float(0.0F);
/* 189 */       break;
/*     */     case 7:
/* 192 */       localAny.insert_double(0.0D);
/* 193 */       break;
/*     */     case 10:
/* 196 */       localAny.insert_octet((byte)0);
/* 197 */       break;
/*     */     case 9:
/* 200 */       localAny.insert_char('\000');
/* 201 */       break;
/*     */     case 26:
/* 204 */       localAny.insert_wchar('\000');
/* 205 */       break;
/*     */     case 18:
/* 209 */       localAny.type(paramTypeCode);
/*     */ 
/* 211 */       localAny.insert_string("");
/* 212 */       break;
/*     */     case 27:
/* 216 */       localAny.type(paramTypeCode);
/*     */ 
/* 218 */       localAny.insert_wstring("");
/* 219 */       break;
/*     */     case 14:
/* 222 */       localAny.insert_Object(null);
/* 223 */       break;
/*     */     case 12:
/* 227 */       localAny.insert_TypeCode(localAny.type());
/* 228 */       break;
/*     */     case 11:
/* 234 */       localAny.insert_any(paramORB.create_any());
/* 235 */       break;
/*     */     case 15:
/*     */     case 16:
/*     */     case 17:
/*     */     case 19:
/*     */     case 20:
/*     */     case 22:
/*     */     case 29:
/*     */     case 30:
/* 249 */       localAny.type(paramTypeCode);
/* 250 */       break;
/*     */     case 28:
/* 252 */       localAny.insert_fixed(new BigDecimal("0.0"), paramTypeCode);
/* 253 */       break;
/*     */     case 1:
/*     */     case 13:
/*     */     case 21:
/*     */     case 31:
/*     */     case 32:
/* 259 */       localAny.type(paramTypeCode);
/* 260 */       break;
/*     */     case 0:
/* 263 */       break;
/*     */     case 25:
/* 266 */       throw localORBUtilSystemException.tkLongDoubleNotSupported();
/*     */     default:
/* 268 */       throw localORBUtilSystemException.typecodeNotSupported();
/*     */     }
/* 270 */     return localAny;
/*     */   }
/*     */ 
/*     */   static Any copy(Any paramAny, ORB paramORB)
/*     */   {
/* 281 */     return new AnyImpl(paramORB, paramAny);
/*     */   }
/*     */ 
/*     */   static DynAny convertToNative(DynAny paramDynAny, ORB paramORB)
/*     */   {
/* 297 */     if ((paramDynAny instanceof DynAnyImpl)) {
/* 298 */       return paramDynAny;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 303 */       return createMostDerivedDynAny(paramDynAny.to_any(), paramORB, true); } catch (InconsistentTypeCode localInconsistentTypeCode) {
/*     */     }
/* 305 */     return null;
/*     */   }
/*     */ 
/*     */   static boolean isInitialized(Any paramAny)
/*     */   {
/* 314 */     boolean bool = ((AnyImpl)paramAny).isInitialized();
/* 315 */     switch (paramAny.type().kind().value()) {
/*     */     case 18:
/* 317 */       return (bool) && (paramAny.extract_string() != null);
/*     */     case 27:
/* 319 */       return (bool) && (paramAny.extract_wstring() != null);
/*     */     }
/* 321 */     return bool;
/*     */   }
/*     */ 
/*     */   static boolean set_current_component(DynAny paramDynAny1, DynAny paramDynAny2)
/*     */   {
/* 327 */     if (paramDynAny2 != null)
/*     */       try {
/* 329 */         paramDynAny1.rewind();
/*     */         do
/* 331 */           if (paramDynAny1.current_component() == paramDynAny2)
/* 332 */             return true;
/* 333 */         while (paramDynAny1.next());
/*     */       } catch (TypeMismatch localTypeMismatch) {
/*     */       }
/* 336 */     return false;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.dynamicany.DynAnyUtil
 * JD-Core Version:    0.6.2
 */