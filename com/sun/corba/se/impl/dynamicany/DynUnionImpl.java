/*     */ package com.sun.corba.se.impl.dynamicany;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.TCKind;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.TypeCodePackage.BadKind;
/*     */ import org.omg.CORBA.TypeCodePackage.Bounds;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.DynamicAny.DynAny;
/*     */ import org.omg.DynamicAny.DynAnyFactoryPackage.InconsistentTypeCode;
/*     */ import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
/*     */ import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;
/*     */ import org.omg.DynamicAny.DynUnion;
/*     */ 
/*     */ public class DynUnionImpl extends DynAnyConstructedImpl
/*     */   implements DynUnion
/*     */ {
/*  49 */   DynAny discriminator = null;
/*     */ 
/*  52 */   DynAny currentMember = null;
/*  53 */   int currentMemberIndex = -1;
/*     */ 
/*     */   private DynUnionImpl()
/*     */   {
/*  60 */     this(null, (Any)null, false);
/*     */   }
/*     */ 
/*     */   protected DynUnionImpl(ORB paramORB, Any paramAny, boolean paramBoolean)
/*     */   {
/*  65 */     super(paramORB, paramAny, paramBoolean);
/*     */   }
/*     */ 
/*     */   protected DynUnionImpl(ORB paramORB, TypeCode paramTypeCode)
/*     */   {
/*  70 */     super(paramORB, paramTypeCode);
/*     */   }
/*     */ 
/*     */   protected boolean initializeComponentsFromAny() {
/*     */     try {
/*  75 */       InputStream localInputStream = this.any.create_input_stream();
/*  76 */       Any localAny1 = DynAnyUtil.extractAnyFromStream(discriminatorType(), localInputStream, this.orb);
/*  77 */       this.discriminator = DynAnyUtil.createMostDerivedDynAny(localAny1, this.orb, false);
/*  78 */       this.currentMemberIndex = currentUnionMemberIndex(localAny1);
/*  79 */       Any localAny2 = DynAnyUtil.extractAnyFromStream(memberType(this.currentMemberIndex), localInputStream, this.orb);
/*  80 */       this.currentMember = DynAnyUtil.createMostDerivedDynAny(localAny2, this.orb, false);
/*  81 */       this.components = new DynAny[] { this.discriminator, this.currentMember };
/*     */     } catch (InconsistentTypeCode localInconsistentTypeCode) {
/*     */     }
/*  84 */     return true;
/*     */   }
/*     */ 
/*     */   protected boolean initializeComponentsFromTypeCode()
/*     */   {
/*     */     try
/*     */     {
/*  94 */       this.discriminator = DynAnyUtil.createMostDerivedDynAny(memberLabel(0), this.orb, false);
/*  95 */       this.index = 0;
/*  96 */       this.currentMemberIndex = 0;
/*  97 */       this.currentMember = DynAnyUtil.createMostDerivedDynAny(memberType(0), this.orb);
/*  98 */       this.components = new DynAny[] { this.discriminator, this.currentMember };
/*     */     } catch (InconsistentTypeCode localInconsistentTypeCode) {
/*     */     }
/* 101 */     return true;
/*     */   }
/*     */ 
/*     */   private TypeCode discriminatorType()
/*     */   {
/* 109 */     TypeCode localTypeCode = null;
/*     */     try {
/* 111 */       localTypeCode = this.any.type().discriminator_type();
/*     */     } catch (BadKind localBadKind) {
/*     */     }
/* 114 */     return localTypeCode;
/*     */   }
/*     */ 
/*     */   private int memberCount() {
/* 118 */     int i = 0;
/*     */     try {
/* 120 */       i = this.any.type().member_count();
/*     */     } catch (BadKind localBadKind) {
/*     */     }
/* 123 */     return i;
/*     */   }
/*     */ 
/*     */   private Any memberLabel(int paramInt) {
/* 127 */     Any localAny = null;
/*     */     try {
/* 129 */       localAny = this.any.type().member_label(paramInt);
/*     */     } catch (BadKind localBadKind) {
/*     */     } catch (Bounds localBounds) {
/*     */     }
/* 133 */     return localAny;
/*     */   }
/*     */ 
/*     */   private TypeCode memberType(int paramInt) {
/* 137 */     TypeCode localTypeCode = null;
/*     */     try {
/* 139 */       localTypeCode = this.any.type().member_type(paramInt);
/*     */     } catch (BadKind localBadKind) {
/*     */     } catch (Bounds localBounds) {
/*     */     }
/* 143 */     return localTypeCode;
/*     */   }
/*     */ 
/*     */   private String memberName(int paramInt) {
/* 147 */     String str = null;
/*     */     try {
/* 149 */       str = this.any.type().member_name(paramInt);
/*     */     } catch (BadKind localBadKind) {
/*     */     } catch (Bounds localBounds) {
/*     */     }
/* 153 */     return str;
/*     */   }
/*     */ 
/*     */   private int defaultIndex() {
/* 157 */     int i = -1;
/*     */     try {
/* 159 */       i = this.any.type().default_index();
/*     */     } catch (BadKind localBadKind) {
/*     */     }
/* 162 */     return i;
/*     */   }
/*     */ 
/*     */   private int currentUnionMemberIndex(Any paramAny) {
/* 166 */     int i = memberCount();
/*     */ 
/* 168 */     for (int j = 0; j < i; j++) {
/* 169 */       Any localAny = memberLabel(j);
/* 170 */       if (localAny.equal(paramAny)) {
/* 171 */         return j;
/*     */       }
/*     */     }
/* 174 */     if (defaultIndex() != -1) {
/* 175 */       return defaultIndex();
/*     */     }
/* 177 */     return -1;
/*     */   }
/*     */ 
/*     */   protected void clearData() {
/* 181 */     super.clearData();
/* 182 */     this.discriminator = null;
/*     */ 
/* 184 */     this.currentMember.destroy();
/* 185 */     this.currentMember = null;
/* 186 */     this.currentMemberIndex = -1;
/*     */   }
/*     */ 
/*     */   public DynAny get_discriminator()
/*     */   {
/* 203 */     if (this.status == 2) {
/* 204 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 206 */     return checkInitComponents() ? this.discriminator : null;
/*     */   }
/*     */ 
/*     */   public void set_discriminator(DynAny paramDynAny)
/*     */     throws TypeMismatch
/*     */   {
/* 228 */     if (this.status == 2) {
/* 229 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 231 */     if (!paramDynAny.type().equal(discriminatorType())) {
/* 232 */       throw new TypeMismatch();
/*     */     }
/* 234 */     paramDynAny = DynAnyUtil.convertToNative(paramDynAny, this.orb);
/* 235 */     Any localAny = getAny(paramDynAny);
/* 236 */     int i = currentUnionMemberIndex(localAny);
/* 237 */     if (i == -1) {
/* 238 */       clearData();
/* 239 */       this.index = 0;
/*     */     }
/*     */     else {
/* 242 */       checkInitComponents();
/* 243 */       if ((this.currentMemberIndex == -1) || (i != this.currentMemberIndex)) {
/* 244 */         clearData();
/* 245 */         this.index = 1;
/* 246 */         this.currentMemberIndex = i;
/*     */         try {
/* 248 */           this.currentMember = DynAnyUtil.createMostDerivedDynAny(memberType(this.currentMemberIndex), this.orb); } catch (InconsistentTypeCode localInconsistentTypeCode) {
/*     */         }
/* 250 */         this.discriminator = paramDynAny;
/* 251 */         this.components = new DynAny[] { this.discriminator, this.currentMember };
/* 252 */         this.representations = 4;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void set_to_default_member()
/*     */     throws TypeMismatch
/*     */   {
/* 265 */     if (this.status == 2) {
/* 266 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 268 */     int i = defaultIndex();
/* 269 */     if (i == -1)
/* 270 */       throw new TypeMismatch();
/*     */     try
/*     */     {
/* 273 */       clearData();
/* 274 */       this.index = 1;
/* 275 */       this.currentMemberIndex = i;
/* 276 */       this.currentMember = DynAnyUtil.createMostDerivedDynAny(memberType(i), this.orb);
/* 277 */       this.components = new DynAny[] { this.discriminator, this.currentMember };
/* 278 */       Any localAny = this.orb.create_any();
/* 279 */       localAny.insert_octet((byte)0);
/* 280 */       this.discriminator = DynAnyUtil.createMostDerivedDynAny(localAny, this.orb, false);
/* 281 */       this.representations = 4;
/*     */     }
/*     */     catch (InconsistentTypeCode localInconsistentTypeCode)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public void set_to_no_active_member()
/*     */     throws TypeMismatch
/*     */   {
/* 294 */     if (this.status == 2) {
/* 295 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/*     */ 
/* 298 */     if (defaultIndex() != -1) {
/* 299 */       throw new TypeMismatch();
/*     */     }
/* 301 */     checkInitComponents();
/* 302 */     Any localAny = getAny(this.discriminator);
/*     */ 
/* 305 */     localAny.type(localAny.type());
/* 306 */     this.index = 0;
/* 307 */     this.currentMemberIndex = -1;
/*     */ 
/* 309 */     this.currentMember.destroy();
/* 310 */     this.currentMember = null;
/* 311 */     this.components[0] = this.discriminator;
/* 312 */     this.representations = 4;
/*     */   }
/*     */ 
/*     */   public boolean has_no_active_member()
/*     */   {
/* 322 */     if (this.status == 2) {
/* 323 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/*     */ 
/* 326 */     if (defaultIndex() != -1) {
/* 327 */       return false;
/*     */     }
/* 329 */     checkInitComponents();
/* 330 */     return this.currentMemberIndex == -1;
/*     */   }
/*     */ 
/*     */   public TCKind discriminator_kind() {
/* 334 */     if (this.status == 2) {
/* 335 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 337 */     return discriminatorType().kind();
/*     */   }
/*     */ 
/*     */   public DynAny member()
/*     */     throws InvalidValue
/*     */   {
/* 349 */     if (this.status == 2) {
/* 350 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 352 */     if ((!checkInitComponents()) || (this.currentMemberIndex == -1))
/* 353 */       throw new InvalidValue();
/* 354 */     return this.currentMember;
/*     */   }
/*     */ 
/*     */   public String member_name()
/*     */     throws InvalidValue
/*     */   {
/* 364 */     if (this.status == 2) {
/* 365 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 367 */     if ((!checkInitComponents()) || (this.currentMemberIndex == -1))
/* 368 */       throw new InvalidValue();
/* 369 */     String str = memberName(this.currentMemberIndex);
/* 370 */     return str == null ? "" : str;
/*     */   }
/*     */ 
/*     */   public TCKind member_kind()
/*     */     throws InvalidValue
/*     */   {
/* 378 */     if (this.status == 2) {
/* 379 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 381 */     if ((!checkInitComponents()) || (this.currentMemberIndex == -1))
/* 382 */       throw new InvalidValue();
/* 383 */     return memberType(this.currentMemberIndex).kind();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.dynamicany.DynUnionImpl
 * JD-Core Version:    0.6.2
 */