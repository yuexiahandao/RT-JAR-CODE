/*     */ package com.sun.corba.se.impl.dynamicany;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.io.Serializable;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.Object;
/*     */ import org.omg.CORBA.TCKind;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.TypeCodePackage.BadKind;
/*     */ import org.omg.DynamicAny.DynAny;
/*     */ import org.omg.DynamicAny.DynAnyFactoryPackage.InconsistentTypeCode;
/*     */ import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
/*     */ import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;
/*     */ 
/*     */ public class DynAnyBasicImpl extends DynAnyImpl
/*     */ {
/*     */   private DynAnyBasicImpl()
/*     */   {
/*  49 */     this(null, (Any)null, false);
/*     */   }
/*     */ 
/*     */   protected DynAnyBasicImpl(ORB paramORB, Any paramAny, boolean paramBoolean) {
/*  53 */     super(paramORB, paramAny, paramBoolean);
/*     */ 
/*  55 */     this.index = -1;
/*     */   }
/*     */ 
/*     */   protected DynAnyBasicImpl(ORB paramORB, TypeCode paramTypeCode) {
/*  59 */     super(paramORB, paramTypeCode);
/*     */ 
/*  61 */     this.index = -1;
/*     */   }
/*     */ 
/*     */   public void assign(DynAny paramDynAny)
/*     */     throws TypeMismatch
/*     */   {
/*  71 */     if (this.status == 2) {
/*  72 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/*  74 */     super.assign(paramDynAny);
/*  75 */     this.index = -1;
/*     */   }
/*     */ 
/*     */   public void from_any(Any paramAny)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/*  82 */     if (this.status == 2) {
/*  83 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/*  85 */     super.from_any(paramAny);
/*  86 */     this.index = -1;
/*     */   }
/*     */ 
/*     */   public Any to_any()
/*     */   {
/*  91 */     if (this.status == 2) {
/*  92 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/*  94 */     return DynAnyUtil.copy(this.any, this.orb);
/*     */   }
/*     */ 
/*     */   public boolean equal(DynAny paramDynAny) {
/*  98 */     if (this.status == 2) {
/*  99 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 101 */     if (paramDynAny == this) {
/* 102 */       return true;
/*     */     }
/*     */ 
/* 106 */     if (!this.any.type().equal(paramDynAny.type())) {
/* 107 */       return false;
/*     */     }
/*     */ 
/* 110 */     return this.any.equal(getAny(paramDynAny));
/*     */   }
/*     */ 
/*     */   public void destroy() {
/* 114 */     if (this.status == 2) {
/* 115 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 117 */     if (this.status == 0)
/* 118 */       this.status = 2;
/*     */   }
/*     */ 
/*     */   public DynAny copy()
/*     */   {
/* 123 */     if (this.status == 2) {
/* 124 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/*     */     try
/*     */     {
/* 128 */       return DynAnyUtil.createMostDerivedDynAny(this.any, this.orb, true); } catch (InconsistentTypeCode localInconsistentTypeCode) {
/*     */     }
/* 130 */     return null;
/*     */   }
/*     */ 
/*     */   public DynAny current_component()
/*     */     throws TypeMismatch
/*     */   {
/* 137 */     return null;
/*     */   }
/*     */ 
/*     */   public int component_count() {
/* 141 */     return 0;
/*     */   }
/*     */ 
/*     */   public boolean next() {
/* 145 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean seek(int paramInt) {
/* 149 */     return false;
/*     */   }
/*     */ 
/*     */   public void rewind()
/*     */   {
/*     */   }
/*     */ 
/*     */   public void insert_boolean(boolean paramBoolean)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 159 */     if (this.status == 2) {
/* 160 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 162 */     if (this.any.type().kind().value() != 8)
/* 163 */       throw new TypeMismatch();
/* 164 */     this.any.insert_boolean(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void insert_octet(byte paramByte)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 171 */     if (this.status == 2) {
/* 172 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 174 */     if (this.any.type().kind().value() != 10)
/* 175 */       throw new TypeMismatch();
/* 176 */     this.any.insert_octet(paramByte);
/*     */   }
/*     */ 
/*     */   public void insert_char(char paramChar)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 183 */     if (this.status == 2) {
/* 184 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 186 */     if (this.any.type().kind().value() != 9)
/* 187 */       throw new TypeMismatch();
/* 188 */     this.any.insert_char(paramChar);
/*     */   }
/*     */ 
/*     */   public void insert_short(short paramShort)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 195 */     if (this.status == 2) {
/* 196 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 198 */     if (this.any.type().kind().value() != 2)
/* 199 */       throw new TypeMismatch();
/* 200 */     this.any.insert_short(paramShort);
/*     */   }
/*     */ 
/*     */   public void insert_ushort(short paramShort)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 207 */     if (this.status == 2) {
/* 208 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 210 */     if (this.any.type().kind().value() != 4)
/* 211 */       throw new TypeMismatch();
/* 212 */     this.any.insert_ushort(paramShort);
/*     */   }
/*     */ 
/*     */   public void insert_long(int paramInt)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 219 */     if (this.status == 2) {
/* 220 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 222 */     if (this.any.type().kind().value() != 3)
/* 223 */       throw new TypeMismatch();
/* 224 */     this.any.insert_long(paramInt);
/*     */   }
/*     */ 
/*     */   public void insert_ulong(int paramInt)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 231 */     if (this.status == 2) {
/* 232 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 234 */     if (this.any.type().kind().value() != 5)
/* 235 */       throw new TypeMismatch();
/* 236 */     this.any.insert_ulong(paramInt);
/*     */   }
/*     */ 
/*     */   public void insert_float(float paramFloat)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 243 */     if (this.status == 2) {
/* 244 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 246 */     if (this.any.type().kind().value() != 6)
/* 247 */       throw new TypeMismatch();
/* 248 */     this.any.insert_float(paramFloat);
/*     */   }
/*     */ 
/*     */   public void insert_double(double paramDouble)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 255 */     if (this.status == 2) {
/* 256 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 258 */     if (this.any.type().kind().value() != 7)
/* 259 */       throw new TypeMismatch();
/* 260 */     this.any.insert_double(paramDouble);
/*     */   }
/*     */ 
/*     */   public void insert_string(String paramString)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 267 */     if (this.status == 2) {
/* 268 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 270 */     if (this.any.type().kind().value() != 18)
/* 271 */       throw new TypeMismatch();
/* 272 */     if (paramString == null)
/* 273 */       throw new InvalidValue();
/*     */     try
/*     */     {
/* 276 */       if ((this.any.type().length() > 0) && (this.any.type().length() < paramString.length()))
/* 277 */         throw new InvalidValue();
/*     */     } catch (BadKind localBadKind) {
/*     */     }
/* 280 */     this.any.insert_string(paramString);
/*     */   }
/*     */ 
/*     */   public void insert_reference(Object paramObject)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 287 */     if (this.status == 2) {
/* 288 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 290 */     if (this.any.type().kind().value() != 14)
/* 291 */       throw new TypeMismatch();
/* 292 */     this.any.insert_Object(paramObject);
/*     */   }
/*     */ 
/*     */   public void insert_typecode(TypeCode paramTypeCode)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 299 */     if (this.status == 2) {
/* 300 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 302 */     if (this.any.type().kind().value() != 12)
/* 303 */       throw new TypeMismatch();
/* 304 */     this.any.insert_TypeCode(paramTypeCode);
/*     */   }
/*     */ 
/*     */   public void insert_longlong(long paramLong)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 311 */     if (this.status == 2) {
/* 312 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 314 */     if (this.any.type().kind().value() != 23)
/* 315 */       throw new TypeMismatch();
/* 316 */     this.any.insert_longlong(paramLong);
/*     */   }
/*     */ 
/*     */   public void insert_ulonglong(long paramLong)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 323 */     if (this.status == 2) {
/* 324 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 326 */     if (this.any.type().kind().value() != 24)
/* 327 */       throw new TypeMismatch();
/* 328 */     this.any.insert_ulonglong(paramLong);
/*     */   }
/*     */ 
/*     */   public void insert_wchar(char paramChar)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 335 */     if (this.status == 2) {
/* 336 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 338 */     if (this.any.type().kind().value() != 26)
/* 339 */       throw new TypeMismatch();
/* 340 */     this.any.insert_wchar(paramChar);
/*     */   }
/*     */ 
/*     */   public void insert_wstring(String paramString)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 347 */     if (this.status == 2) {
/* 348 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 350 */     if (this.any.type().kind().value() != 27)
/* 351 */       throw new TypeMismatch();
/* 352 */     if (paramString == null)
/* 353 */       throw new InvalidValue();
/*     */     try
/*     */     {
/* 356 */       if ((this.any.type().length() > 0) && (this.any.type().length() < paramString.length()))
/* 357 */         throw new InvalidValue();
/*     */     } catch (BadKind localBadKind) {
/*     */     }
/* 360 */     this.any.insert_wstring(paramString);
/*     */   }
/*     */ 
/*     */   public void insert_any(Any paramAny)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 367 */     if (this.status == 2) {
/* 368 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 370 */     if (this.any.type().kind().value() != 11)
/* 371 */       throw new TypeMismatch();
/* 372 */     this.any.insert_any(paramAny);
/*     */   }
/*     */ 
/*     */   public void insert_dyn_any(DynAny paramDynAny)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 379 */     if (this.status == 2) {
/* 380 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 382 */     if (this.any.type().kind().value() != 11) {
/* 383 */       throw new TypeMismatch();
/*     */     }
/* 385 */     this.any.insert_any(paramDynAny.to_any());
/*     */   }
/*     */ 
/*     */   public void insert_val(Serializable paramSerializable)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 392 */     if (this.status == 2) {
/* 393 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 395 */     int i = this.any.type().kind().value();
/* 396 */     if ((i != 29) && (i != 30))
/* 397 */       throw new TypeMismatch();
/* 398 */     this.any.insert_Value(paramSerializable);
/*     */   }
/*     */ 
/*     */   public Serializable get_val()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 405 */     if (this.status == 2) {
/* 406 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 408 */     int i = this.any.type().kind().value();
/* 409 */     if ((i != 29) && (i != 30))
/* 410 */       throw new TypeMismatch();
/* 411 */     return this.any.extract_Value();
/*     */   }
/*     */ 
/*     */   public boolean get_boolean()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 418 */     if (this.status == 2) {
/* 419 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 421 */     if (this.any.type().kind().value() != 8)
/* 422 */       throw new TypeMismatch();
/* 423 */     return this.any.extract_boolean();
/*     */   }
/*     */ 
/*     */   public byte get_octet()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 430 */     if (this.status == 2) {
/* 431 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 433 */     if (this.any.type().kind().value() != 10)
/* 434 */       throw new TypeMismatch();
/* 435 */     return this.any.extract_octet();
/*     */   }
/*     */ 
/*     */   public char get_char()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 442 */     if (this.status == 2) {
/* 443 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 445 */     if (this.any.type().kind().value() != 9)
/* 446 */       throw new TypeMismatch();
/* 447 */     return this.any.extract_char();
/*     */   }
/*     */ 
/*     */   public short get_short()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 454 */     if (this.status == 2) {
/* 455 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 457 */     if (this.any.type().kind().value() != 2)
/* 458 */       throw new TypeMismatch();
/* 459 */     return this.any.extract_short();
/*     */   }
/*     */ 
/*     */   public short get_ushort()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 466 */     if (this.status == 2) {
/* 467 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 469 */     if (this.any.type().kind().value() != 4)
/* 470 */       throw new TypeMismatch();
/* 471 */     return this.any.extract_ushort();
/*     */   }
/*     */ 
/*     */   public int get_long()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 478 */     if (this.status == 2) {
/* 479 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 481 */     if (this.any.type().kind().value() != 3)
/* 482 */       throw new TypeMismatch();
/* 483 */     return this.any.extract_long();
/*     */   }
/*     */ 
/*     */   public int get_ulong()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 490 */     if (this.status == 2) {
/* 491 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 493 */     if (this.any.type().kind().value() != 5)
/* 494 */       throw new TypeMismatch();
/* 495 */     return this.any.extract_ulong();
/*     */   }
/*     */ 
/*     */   public float get_float()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 502 */     if (this.status == 2) {
/* 503 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 505 */     if (this.any.type().kind().value() != 6)
/* 506 */       throw new TypeMismatch();
/* 507 */     return this.any.extract_float();
/*     */   }
/*     */ 
/*     */   public double get_double()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 514 */     if (this.status == 2) {
/* 515 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 517 */     if (this.any.type().kind().value() != 7)
/* 518 */       throw new TypeMismatch();
/* 519 */     return this.any.extract_double();
/*     */   }
/*     */ 
/*     */   public String get_string()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 526 */     if (this.status == 2) {
/* 527 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 529 */     if (this.any.type().kind().value() != 18)
/* 530 */       throw new TypeMismatch();
/* 531 */     return this.any.extract_string();
/*     */   }
/*     */ 
/*     */   public Object get_reference()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 538 */     if (this.status == 2) {
/* 539 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 541 */     if (this.any.type().kind().value() != 14)
/* 542 */       throw new TypeMismatch();
/* 543 */     return this.any.extract_Object();
/*     */   }
/*     */ 
/*     */   public TypeCode get_typecode()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 550 */     if (this.status == 2) {
/* 551 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 553 */     if (this.any.type().kind().value() != 12)
/* 554 */       throw new TypeMismatch();
/* 555 */     return this.any.extract_TypeCode();
/*     */   }
/*     */ 
/*     */   public long get_longlong()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 562 */     if (this.status == 2) {
/* 563 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 565 */     if (this.any.type().kind().value() != 23)
/* 566 */       throw new TypeMismatch();
/* 567 */     return this.any.extract_longlong();
/*     */   }
/*     */ 
/*     */   public long get_ulonglong()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 574 */     if (this.status == 2) {
/* 575 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 577 */     if (this.any.type().kind().value() != 24)
/* 578 */       throw new TypeMismatch();
/* 579 */     return this.any.extract_ulonglong();
/*     */   }
/*     */ 
/*     */   public char get_wchar()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 586 */     if (this.status == 2) {
/* 587 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 589 */     if (this.any.type().kind().value() != 26)
/* 590 */       throw new TypeMismatch();
/* 591 */     return this.any.extract_wchar();
/*     */   }
/*     */ 
/*     */   public String get_wstring()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 598 */     if (this.status == 2) {
/* 599 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 601 */     if (this.any.type().kind().value() != 27)
/* 602 */       throw new TypeMismatch();
/* 603 */     return this.any.extract_wstring();
/*     */   }
/*     */ 
/*     */   public Any get_any()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 610 */     if (this.status == 2) {
/* 611 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 613 */     if (this.any.type().kind().value() != 11)
/* 614 */       throw new TypeMismatch();
/* 615 */     return this.any.extract_any();
/*     */   }
/*     */ 
/*     */   public DynAny get_dyn_any()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 622 */     if (this.status == 2) {
/* 623 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 625 */     if (this.any.type().kind().value() != 11)
/* 626 */       throw new TypeMismatch();
/*     */     try
/*     */     {
/* 629 */       return DynAnyUtil.createMostDerivedDynAny(this.any.extract_any(), this.orb, true);
/*     */     }
/*     */     catch (InconsistentTypeCode localInconsistentTypeCode) {
/*     */     }
/* 633 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.dynamicany.DynAnyBasicImpl
 * JD-Core Version:    0.6.2
 */