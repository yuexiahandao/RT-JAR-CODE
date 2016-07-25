/*     */ package com.sun.corba.se.impl.dynamicany;
/*     */ 
/*     */ import com.sun.corba.se.impl.corba.TypeCodeImpl;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import java.io.Serializable;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import org.omg.DynamicAny.DynAny;
/*     */ import org.omg.DynamicAny.DynAnyFactoryPackage.InconsistentTypeCode;
/*     */ import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
/*     */ import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;
/*     */ 
/*     */ abstract class DynAnyConstructedImpl extends DynAnyImpl
/*     */ {
/*     */   protected static final byte REPRESENTATION_NONE = 0;
/*     */   protected static final byte REPRESENTATION_TYPECODE = 1;
/*     */   protected static final byte REPRESENTATION_ANY = 2;
/*     */   protected static final byte REPRESENTATION_COMPONENTS = 4;
/*     */   protected static final byte RECURSIVE_UNDEF = -1;
/*     */   protected static final byte RECURSIVE_NO = 0;
/*     */   protected static final byte RECURSIVE_YES = 1;
/*  52 */   protected static final DynAny[] emptyComponents = new DynAny[0];
/*     */ 
/*  58 */   DynAny[] components = emptyComponents;
/*  59 */   byte representations = 0;
/*  60 */   byte isRecursive = -1;
/*     */ 
/*     */   private DynAnyConstructedImpl()
/*     */   {
/*  67 */     this(null, (Any)null, false);
/*     */   }
/*     */ 
/*     */   protected DynAnyConstructedImpl(ORB paramORB, Any paramAny, boolean paramBoolean) {
/*  71 */     super(paramORB, paramAny, paramBoolean);
/*     */ 
/*  73 */     if (this.any != null) {
/*  74 */       this.representations = 2;
/*     */     }
/*     */ 
/*  77 */     this.index = 0;
/*     */   }
/*     */ 
/*     */   protected DynAnyConstructedImpl(ORB paramORB, TypeCode paramTypeCode)
/*     */   {
/*  83 */     super(paramORB, paramTypeCode);
/*  84 */     if (paramTypeCode != null) {
/*  85 */       this.representations = 1;
/*     */     }
/*     */ 
/*  88 */     this.index = -1;
/*     */   }
/*     */ 
/*     */   protected boolean isRecursive()
/*     */   {
/*  97 */     if (this.isRecursive == -1) {
/*  98 */       TypeCode localTypeCode = this.any.type();
/*  99 */       if ((localTypeCode instanceof TypeCodeImpl)) {
/* 100 */         if (((TypeCodeImpl)localTypeCode).is_recursive())
/* 101 */           this.isRecursive = 1;
/*     */         else
/* 103 */           this.isRecursive = 0;
/*     */       }
/*     */       else {
/* 106 */         this.isRecursive = 0;
/*     */       }
/*     */     }
/* 109 */     return this.isRecursive == 1;
/*     */   }
/*     */ 
/*     */   public DynAny current_component()
/*     */     throws TypeMismatch
/*     */   {
/* 119 */     if (this.status == 2) {
/* 120 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 122 */     if (this.index == -1) {
/* 123 */       return null;
/*     */     }
/* 125 */     return checkInitComponents() ? this.components[this.index] : null;
/*     */   }
/*     */ 
/*     */   public int component_count() {
/* 129 */     if (this.status == 2) {
/* 130 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 132 */     return checkInitComponents() ? this.components.length : 0;
/*     */   }
/*     */ 
/*     */   public boolean next() {
/* 136 */     if (this.status == 2) {
/* 137 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 139 */     if (!checkInitComponents()) {
/* 140 */       return false;
/*     */     }
/* 142 */     this.index += 1;
/* 143 */     if ((this.index >= 0) && (this.index < this.components.length)) {
/* 144 */       return true;
/*     */     }
/* 146 */     this.index = -1;
/* 147 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean seek(int paramInt)
/*     */   {
/* 152 */     if (this.status == 2) {
/* 153 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 155 */     if (paramInt < 0) {
/* 156 */       this.index = -1;
/* 157 */       return false;
/*     */     }
/* 159 */     if (!checkInitComponents()) {
/* 160 */       return false;
/*     */     }
/* 162 */     if (paramInt < this.components.length) {
/* 163 */       this.index = paramInt;
/* 164 */       return true;
/*     */     }
/* 166 */     return false;
/*     */   }
/*     */ 
/*     */   public void rewind() {
/* 170 */     if (this.status == 2) {
/* 171 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 173 */     seek(0);
/*     */   }
/*     */ 
/*     */   protected void clearData()
/*     */   {
/* 181 */     super.clearData();
/*     */ 
/* 183 */     this.components = emptyComponents;
/* 184 */     this.index = -1;
/* 185 */     this.representations = 0;
/*     */   }
/*     */ 
/*     */   protected void writeAny(OutputStream paramOutputStream)
/*     */   {
/* 192 */     checkInitAny();
/* 193 */     super.writeAny(paramOutputStream);
/*     */   }
/*     */ 
/*     */   protected boolean checkInitComponents()
/*     */   {
/* 198 */     if ((this.representations & 0x4) == 0) {
/* 199 */       if ((this.representations & 0x2) != 0) {
/* 200 */         if (initializeComponentsFromAny())
/* 201 */           this.representations = ((byte)(this.representations | 0x4));
/*     */         else
/* 203 */           return false;
/*     */       }
/* 205 */       else if ((this.representations & 0x1) != 0) {
/* 206 */         if (initializeComponentsFromTypeCode())
/* 207 */           this.representations = ((byte)(this.representations | 0x4));
/*     */         else {
/* 209 */           return false;
/*     */         }
/*     */       }
/*     */     }
/* 213 */     return true;
/*     */   }
/*     */ 
/*     */   protected void checkInitAny()
/*     */   {
/* 218 */     if ((this.representations & 0x2) == 0)
/*     */     {
/* 220 */       if ((this.representations & 0x4) != 0)
/*     */       {
/* 222 */         if (initializeAnyFromComponents())
/* 223 */           this.representations = ((byte)(this.representations | 0x2));
/*     */       }
/* 225 */       else if ((this.representations & 0x1) != 0)
/*     */       {
/* 227 */         if ((this.representations == 1) && (isRecursive()))
/* 228 */           return;
/* 229 */         if (initializeComponentsFromTypeCode()) {
/* 230 */           this.representations = ((byte)(this.representations | 0x4));
/*     */         }
/* 232 */         if (initializeAnyFromComponents())
/* 233 */           this.representations = ((byte)(this.representations | 0x2));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected abstract boolean initializeComponentsFromAny();
/*     */ 
/*     */   protected abstract boolean initializeComponentsFromTypeCode();
/*     */ 
/*     */   protected boolean initializeAnyFromComponents()
/*     */   {
/* 248 */     OutputStream localOutputStream = this.any.create_output_stream();
/* 249 */     for (int i = 0; i < this.components.length; i++) {
/* 250 */       if ((this.components[i] instanceof DynAnyImpl)) {
/* 251 */         ((DynAnyImpl)this.components[i]).writeAny(localOutputStream);
/*     */       }
/*     */       else {
/* 254 */         this.components[i].to_any().write_value(localOutputStream);
/*     */       }
/*     */     }
/* 257 */     this.any.read_value(localOutputStream.create_input_stream(), this.any.type());
/* 258 */     return true;
/*     */   }
/*     */ 
/*     */   public void assign(DynAny paramDynAny)
/*     */     throws TypeMismatch
/*     */   {
/* 268 */     if (this.status == 2) {
/* 269 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 271 */     clearData();
/* 272 */     super.assign(paramDynAny);
/* 273 */     this.representations = 2;
/* 274 */     this.index = 0;
/*     */   }
/*     */ 
/*     */   public void from_any(Any paramAny)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 281 */     if (this.status == 2) {
/* 282 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 284 */     clearData();
/* 285 */     super.from_any(paramAny);
/* 286 */     this.representations = 2;
/* 287 */     this.index = 0;
/*     */   }
/*     */ 
/*     */   public Any to_any()
/*     */   {
/* 293 */     if (this.status == 2) {
/* 294 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 296 */     checkInitAny();
/*     */ 
/* 298 */     return DynAnyUtil.copy(this.any, this.orb);
/*     */   }
/*     */ 
/*     */   public boolean equal(DynAny paramDynAny) {
/* 302 */     if (this.status == 2) {
/* 303 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 305 */     if (paramDynAny == this) {
/* 306 */       return true;
/*     */     }
/* 308 */     if (!this.any.type().equal(paramDynAny.type())) {
/* 309 */       return false;
/*     */     }
/*     */ 
/* 313 */     if (!checkInitComponents()) {
/* 314 */       return false;
/*     */     }
/* 316 */     DynAny localDynAny = null;
/*     */     try
/*     */     {
/* 319 */       localDynAny = paramDynAny.current_component();
/* 320 */       for (int i = 0; i < this.components.length; i++)
/*     */       {
/*     */         boolean bool;
/* 321 */         if (!paramDynAny.seek(i)) {
/* 322 */           return false;
/*     */         }
/*     */ 
/* 325 */         if (!this.components[i].equal(paramDynAny.current_component()))
/*     */         {
/* 327 */           return false;
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (TypeMismatch localTypeMismatch) {
/*     */     }
/*     */     finally {
/* 334 */       DynAnyUtil.set_current_component(paramDynAny, localDynAny);
/*     */     }
/* 336 */     return true;
/*     */   }
/*     */ 
/*     */   public void destroy() {
/* 340 */     if (this.status == 2) {
/* 341 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 343 */     if (this.status == 0) {
/* 344 */       this.status = 2;
/* 345 */       for (int i = 0; i < this.components.length; i++) {
/* 346 */         if ((this.components[i] instanceof DynAnyImpl)) {
/* 347 */           ((DynAnyImpl)this.components[i]).setStatus((byte)0);
/*     */         }
/* 349 */         this.components[i].destroy();
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public DynAny copy() {
/* 355 */     if (this.status == 2) {
/* 356 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 358 */     checkInitAny();
/*     */     try {
/* 360 */       return DynAnyUtil.createMostDerivedDynAny(this.any, this.orb, true); } catch (InconsistentTypeCode localInconsistentTypeCode) {
/*     */     }
/* 362 */     return null;
/*     */   }
/*     */ 
/*     */   public void insert_boolean(boolean paramBoolean)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 372 */     if (this.status == 2) {
/* 373 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 375 */     if (this.index == -1)
/* 376 */       throw new InvalidValue();
/* 377 */     DynAny localDynAny = current_component();
/* 378 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 379 */       throw new TypeMismatch();
/* 380 */     localDynAny.insert_boolean(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void insert_octet(byte paramByte)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 387 */     if (this.status == 2) {
/* 388 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 390 */     if (this.index == -1)
/* 391 */       throw new InvalidValue();
/* 392 */     DynAny localDynAny = current_component();
/* 393 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 394 */       throw new TypeMismatch();
/* 395 */     localDynAny.insert_octet(paramByte);
/*     */   }
/*     */ 
/*     */   public void insert_char(char paramChar)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 402 */     if (this.status == 2) {
/* 403 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 405 */     if (this.index == -1)
/* 406 */       throw new InvalidValue();
/* 407 */     DynAny localDynAny = current_component();
/* 408 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 409 */       throw new TypeMismatch();
/* 410 */     localDynAny.insert_char(paramChar);
/*     */   }
/*     */ 
/*     */   public void insert_short(short paramShort)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 417 */     if (this.status == 2) {
/* 418 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 420 */     if (this.index == -1)
/* 421 */       throw new InvalidValue();
/* 422 */     DynAny localDynAny = current_component();
/* 423 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 424 */       throw new TypeMismatch();
/* 425 */     localDynAny.insert_short(paramShort);
/*     */   }
/*     */ 
/*     */   public void insert_ushort(short paramShort)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 432 */     if (this.status == 2) {
/* 433 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 435 */     if (this.index == -1)
/* 436 */       throw new InvalidValue();
/* 437 */     DynAny localDynAny = current_component();
/* 438 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 439 */       throw new TypeMismatch();
/* 440 */     localDynAny.insert_ushort(paramShort);
/*     */   }
/*     */ 
/*     */   public void insert_long(int paramInt)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 447 */     if (this.status == 2) {
/* 448 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 450 */     if (this.index == -1)
/* 451 */       throw new InvalidValue();
/* 452 */     DynAny localDynAny = current_component();
/* 453 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 454 */       throw new TypeMismatch();
/* 455 */     localDynAny.insert_long(paramInt);
/*     */   }
/*     */ 
/*     */   public void insert_ulong(int paramInt)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 462 */     if (this.status == 2) {
/* 463 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 465 */     if (this.index == -1)
/* 466 */       throw new InvalidValue();
/* 467 */     DynAny localDynAny = current_component();
/* 468 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 469 */       throw new TypeMismatch();
/* 470 */     localDynAny.insert_ulong(paramInt);
/*     */   }
/*     */ 
/*     */   public void insert_float(float paramFloat)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 477 */     if (this.status == 2) {
/* 478 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 480 */     if (this.index == -1)
/* 481 */       throw new InvalidValue();
/* 482 */     DynAny localDynAny = current_component();
/* 483 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 484 */       throw new TypeMismatch();
/* 485 */     localDynAny.insert_float(paramFloat);
/*     */   }
/*     */ 
/*     */   public void insert_double(double paramDouble)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 492 */     if (this.status == 2) {
/* 493 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 495 */     if (this.index == -1)
/* 496 */       throw new InvalidValue();
/* 497 */     DynAny localDynAny = current_component();
/* 498 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 499 */       throw new TypeMismatch();
/* 500 */     localDynAny.insert_double(paramDouble);
/*     */   }
/*     */ 
/*     */   public void insert_string(String paramString)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 507 */     if (this.status == 2) {
/* 508 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 510 */     if (this.index == -1)
/* 511 */       throw new InvalidValue();
/* 512 */     DynAny localDynAny = current_component();
/* 513 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 514 */       throw new TypeMismatch();
/* 515 */     localDynAny.insert_string(paramString);
/*     */   }
/*     */ 
/*     */   public void insert_reference(org.omg.CORBA.Object paramObject)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 522 */     if (this.status == 2) {
/* 523 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 525 */     if (this.index == -1)
/* 526 */       throw new InvalidValue();
/* 527 */     DynAny localDynAny = current_component();
/* 528 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 529 */       throw new TypeMismatch();
/* 530 */     localDynAny.insert_reference(paramObject);
/*     */   }
/*     */ 
/*     */   public void insert_typecode(TypeCode paramTypeCode)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 537 */     if (this.status == 2) {
/* 538 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 540 */     if (this.index == -1)
/* 541 */       throw new InvalidValue();
/* 542 */     DynAny localDynAny = current_component();
/* 543 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 544 */       throw new TypeMismatch();
/* 545 */     localDynAny.insert_typecode(paramTypeCode);
/*     */   }
/*     */ 
/*     */   public void insert_longlong(long paramLong)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 552 */     if (this.status == 2) {
/* 553 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 555 */     if (this.index == -1)
/* 556 */       throw new InvalidValue();
/* 557 */     DynAny localDynAny = current_component();
/* 558 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 559 */       throw new TypeMismatch();
/* 560 */     localDynAny.insert_longlong(paramLong);
/*     */   }
/*     */ 
/*     */   public void insert_ulonglong(long paramLong)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 567 */     if (this.status == 2) {
/* 568 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 570 */     if (this.index == -1)
/* 571 */       throw new InvalidValue();
/* 572 */     DynAny localDynAny = current_component();
/* 573 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 574 */       throw new TypeMismatch();
/* 575 */     localDynAny.insert_ulonglong(paramLong);
/*     */   }
/*     */ 
/*     */   public void insert_wchar(char paramChar)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 582 */     if (this.status == 2) {
/* 583 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 585 */     if (this.index == -1)
/* 586 */       throw new InvalidValue();
/* 587 */     DynAny localDynAny = current_component();
/* 588 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 589 */       throw new TypeMismatch();
/* 590 */     localDynAny.insert_wchar(paramChar);
/*     */   }
/*     */ 
/*     */   public void insert_wstring(String paramString)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 597 */     if (this.status == 2) {
/* 598 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 600 */     if (this.index == -1)
/* 601 */       throw new InvalidValue();
/* 602 */     DynAny localDynAny = current_component();
/* 603 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 604 */       throw new TypeMismatch();
/* 605 */     localDynAny.insert_wstring(paramString);
/*     */   }
/*     */ 
/*     */   public void insert_any(Any paramAny)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 612 */     if (this.status == 2) {
/* 613 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 615 */     if (this.index == -1)
/* 616 */       throw new InvalidValue();
/* 617 */     DynAny localDynAny = current_component();
/* 618 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 619 */       throw new TypeMismatch();
/* 620 */     localDynAny.insert_any(paramAny);
/*     */   }
/*     */ 
/*     */   public void insert_dyn_any(DynAny paramDynAny)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 627 */     if (this.status == 2) {
/* 628 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 630 */     if (this.index == -1)
/* 631 */       throw new InvalidValue();
/* 632 */     DynAny localDynAny = current_component();
/* 633 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 634 */       throw new TypeMismatch();
/* 635 */     localDynAny.insert_dyn_any(paramDynAny);
/*     */   }
/*     */ 
/*     */   public void insert_val(Serializable paramSerializable)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 642 */     if (this.status == 2) {
/* 643 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 645 */     if (this.index == -1)
/* 646 */       throw new InvalidValue();
/* 647 */     DynAny localDynAny = current_component();
/* 648 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 649 */       throw new TypeMismatch();
/* 650 */     localDynAny.insert_val(paramSerializable);
/*     */   }
/*     */ 
/*     */   public Serializable get_val()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 657 */     if (this.status == 2) {
/* 658 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 660 */     if (this.index == -1)
/* 661 */       throw new InvalidValue();
/* 662 */     DynAny localDynAny = current_component();
/* 663 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 664 */       throw new TypeMismatch();
/* 665 */     return localDynAny.get_val();
/*     */   }
/*     */ 
/*     */   public boolean get_boolean()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 672 */     if (this.status == 2) {
/* 673 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 675 */     if (this.index == -1)
/* 676 */       throw new InvalidValue();
/* 677 */     DynAny localDynAny = current_component();
/* 678 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 679 */       throw new TypeMismatch();
/* 680 */     return localDynAny.get_boolean();
/*     */   }
/*     */ 
/*     */   public byte get_octet()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 687 */     if (this.status == 2) {
/* 688 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 690 */     if (this.index == -1)
/* 691 */       throw new InvalidValue();
/* 692 */     DynAny localDynAny = current_component();
/* 693 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 694 */       throw new TypeMismatch();
/* 695 */     return localDynAny.get_octet();
/*     */   }
/*     */ 
/*     */   public char get_char()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 702 */     if (this.status == 2) {
/* 703 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 705 */     if (this.index == -1)
/* 706 */       throw new InvalidValue();
/* 707 */     DynAny localDynAny = current_component();
/* 708 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 709 */       throw new TypeMismatch();
/* 710 */     return localDynAny.get_char();
/*     */   }
/*     */ 
/*     */   public short get_short()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 717 */     if (this.status == 2) {
/* 718 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 720 */     if (this.index == -1)
/* 721 */       throw new InvalidValue();
/* 722 */     DynAny localDynAny = current_component();
/* 723 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 724 */       throw new TypeMismatch();
/* 725 */     return localDynAny.get_short();
/*     */   }
/*     */ 
/*     */   public short get_ushort()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 732 */     if (this.status == 2) {
/* 733 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 735 */     if (this.index == -1)
/* 736 */       throw new InvalidValue();
/* 737 */     DynAny localDynAny = current_component();
/* 738 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 739 */       throw new TypeMismatch();
/* 740 */     return localDynAny.get_ushort();
/*     */   }
/*     */ 
/*     */   public int get_long()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 747 */     if (this.status == 2) {
/* 748 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 750 */     if (this.index == -1)
/* 751 */       throw new InvalidValue();
/* 752 */     DynAny localDynAny = current_component();
/* 753 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 754 */       throw new TypeMismatch();
/* 755 */     return localDynAny.get_long();
/*     */   }
/*     */ 
/*     */   public int get_ulong()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 762 */     if (this.status == 2) {
/* 763 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 765 */     if (this.index == -1)
/* 766 */       throw new InvalidValue();
/* 767 */     DynAny localDynAny = current_component();
/* 768 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 769 */       throw new TypeMismatch();
/* 770 */     return localDynAny.get_ulong();
/*     */   }
/*     */ 
/*     */   public float get_float()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 777 */     if (this.status == 2) {
/* 778 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 780 */     if (this.index == -1)
/* 781 */       throw new InvalidValue();
/* 782 */     DynAny localDynAny = current_component();
/* 783 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 784 */       throw new TypeMismatch();
/* 785 */     return localDynAny.get_float();
/*     */   }
/*     */ 
/*     */   public double get_double()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 792 */     if (this.status == 2) {
/* 793 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 795 */     if (this.index == -1)
/* 796 */       throw new InvalidValue();
/* 797 */     DynAny localDynAny = current_component();
/* 798 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 799 */       throw new TypeMismatch();
/* 800 */     return localDynAny.get_double();
/*     */   }
/*     */ 
/*     */   public String get_string()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 807 */     if (this.status == 2) {
/* 808 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 810 */     if (this.index == -1)
/* 811 */       throw new InvalidValue();
/* 812 */     DynAny localDynAny = current_component();
/* 813 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 814 */       throw new TypeMismatch();
/* 815 */     return localDynAny.get_string();
/*     */   }
/*     */ 
/*     */   public org.omg.CORBA.Object get_reference()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 822 */     if (this.status == 2) {
/* 823 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 825 */     if (this.index == -1)
/* 826 */       throw new InvalidValue();
/* 827 */     DynAny localDynAny = current_component();
/* 828 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 829 */       throw new TypeMismatch();
/* 830 */     return localDynAny.get_reference();
/*     */   }
/*     */ 
/*     */   public TypeCode get_typecode()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 837 */     if (this.status == 2) {
/* 838 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 840 */     if (this.index == -1)
/* 841 */       throw new InvalidValue();
/* 842 */     DynAny localDynAny = current_component();
/* 843 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 844 */       throw new TypeMismatch();
/* 845 */     return localDynAny.get_typecode();
/*     */   }
/*     */ 
/*     */   public long get_longlong()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 852 */     if (this.status == 2) {
/* 853 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 855 */     if (this.index == -1)
/* 856 */       throw new InvalidValue();
/* 857 */     DynAny localDynAny = current_component();
/* 858 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 859 */       throw new TypeMismatch();
/* 860 */     return localDynAny.get_longlong();
/*     */   }
/*     */ 
/*     */   public long get_ulonglong()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 867 */     if (this.status == 2) {
/* 868 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 870 */     if (this.index == -1)
/* 871 */       throw new InvalidValue();
/* 872 */     DynAny localDynAny = current_component();
/* 873 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 874 */       throw new TypeMismatch();
/* 875 */     return localDynAny.get_ulonglong();
/*     */   }
/*     */ 
/*     */   public char get_wchar()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 882 */     if (this.status == 2) {
/* 883 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 885 */     if (this.index == -1)
/* 886 */       throw new InvalidValue();
/* 887 */     DynAny localDynAny = current_component();
/* 888 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 889 */       throw new TypeMismatch();
/* 890 */     return localDynAny.get_wchar();
/*     */   }
/*     */ 
/*     */   public String get_wstring()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 897 */     if (this.status == 2) {
/* 898 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 900 */     if (this.index == -1)
/* 901 */       throw new InvalidValue();
/* 902 */     DynAny localDynAny = current_component();
/* 903 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 904 */       throw new TypeMismatch();
/* 905 */     return localDynAny.get_wstring();
/*     */   }
/*     */ 
/*     */   public Any get_any()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 912 */     if (this.status == 2) {
/* 913 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 915 */     if (this.index == -1)
/* 916 */       throw new InvalidValue();
/* 917 */     DynAny localDynAny = current_component();
/* 918 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 919 */       throw new TypeMismatch();
/* 920 */     return localDynAny.get_any();
/*     */   }
/*     */ 
/*     */   public DynAny get_dyn_any()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 927 */     if (this.status == 2) {
/* 928 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 930 */     if (this.index == -1)
/* 931 */       throw new InvalidValue();
/* 932 */     DynAny localDynAny = current_component();
/* 933 */     if (DynAnyUtil.isConstructedDynAny(localDynAny))
/* 934 */       throw new TypeMismatch();
/* 935 */     return localDynAny.get_dyn_any();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl
 * JD-Core Version:    0.6.2
 */