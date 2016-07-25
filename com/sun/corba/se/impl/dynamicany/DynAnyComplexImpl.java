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
/*     */ import org.omg.DynamicAny.NameDynAnyPair;
/*     */ import org.omg.DynamicAny.NameValuePair;
/*     */ 
/*     */ abstract class DynAnyComplexImpl extends DynAnyConstructedImpl
/*     */ {
/*  49 */   String[] names = null;
/*     */ 
/*  52 */   NameValuePair[] nameValuePairs = null;
/*  53 */   NameDynAnyPair[] nameDynAnyPairs = null;
/*     */ 
/*     */   private DynAnyComplexImpl()
/*     */   {
/*  60 */     this(null, (Any)null, false);
/*     */   }
/*     */ 
/*     */   protected DynAnyComplexImpl(ORB paramORB, Any paramAny, boolean paramBoolean)
/*     */   {
/*  65 */     super(paramORB, paramAny, paramBoolean);
/*     */   }
/*     */ 
/*     */   protected DynAnyComplexImpl(ORB paramORB, TypeCode paramTypeCode)
/*     */   {
/*  72 */     super(paramORB, paramTypeCode);
/*     */ 
/*  76 */     this.index = 0;
/*     */   }
/*     */ 
/*     */   public String current_member_name()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 109 */     if (this.status == 2) {
/* 110 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 112 */     if ((!checkInitComponents()) || (this.index < 0) || (this.index >= this.names.length)) {
/* 113 */       throw new InvalidValue();
/*     */     }
/* 115 */     return this.names[this.index];
/*     */   }
/*     */ 
/*     */   public TCKind current_member_kind()
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 122 */     if (this.status == 2) {
/* 123 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 125 */     if ((!checkInitComponents()) || (this.index < 0) || (this.index >= this.components.length)) {
/* 126 */       throw new InvalidValue();
/*     */     }
/* 128 */     return this.components[this.index].type().kind();
/*     */   }
/*     */ 
/*     */   public void set_members(NameValuePair[] paramArrayOfNameValuePair)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 136 */     if (this.status == 2) {
/* 137 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 139 */     if ((paramArrayOfNameValuePair == null) || (paramArrayOfNameValuePair.length == 0)) {
/* 140 */       clearData();
/* 141 */       return;
/*     */     }
/*     */ 
/* 145 */     DynAny localDynAny = null;
/*     */ 
/* 148 */     TypeCode localTypeCode1 = this.any.type();
/*     */ 
/* 150 */     int i = 0;
/*     */     try {
/* 152 */       i = localTypeCode1.member_count();
/*     */     } catch (BadKind localBadKind1) {
/*     */     }
/* 155 */     if (i != paramArrayOfNameValuePair.length) {
/* 156 */       clearData();
/* 157 */       throw new InvalidValue();
/*     */     }
/*     */ 
/* 160 */     allocComponents(paramArrayOfNameValuePair);
/*     */ 
/* 162 */     for (int j = 0; j < paramArrayOfNameValuePair.length; j++) {
/* 163 */       if (paramArrayOfNameValuePair[j] != null) {
/* 164 */         String str1 = paramArrayOfNameValuePair[j].id;
/* 165 */         String str2 = null;
/*     */         try {
/* 167 */           str2 = localTypeCode1.member_name(j);
/*     */         } catch (BadKind localBadKind2) {
/*     */         } catch (Bounds localBounds1) {
/*     */         }
/* 171 */         if ((!str2.equals(str1)) && (!str1.equals(""))) {
/* 172 */           clearData();
/*     */ 
/* 174 */           throw new TypeMismatch();
/*     */         }
/* 176 */         Any localAny = paramArrayOfNameValuePair[j].value;
/* 177 */         TypeCode localTypeCode2 = null;
/*     */         try {
/* 179 */           localTypeCode2 = localTypeCode1.member_type(j);
/*     */         } catch (BadKind localBadKind3) {
/*     */         } catch (Bounds localBounds2) {
/*     */         }
/* 183 */         if (!localTypeCode2.equal(localAny.type())) {
/* 184 */           clearData();
/*     */ 
/* 186 */           throw new TypeMismatch();
/*     */         }
/*     */         try
/*     */         {
/* 190 */           localDynAny = DynAnyUtil.createMostDerivedDynAny(localAny, this.orb, false);
/*     */         } catch (InconsistentTypeCode localInconsistentTypeCode) {
/* 192 */           throw new InvalidValue();
/*     */         }
/* 194 */         addComponent(j, str1, localAny, localDynAny);
/*     */       } else {
/* 196 */         clearData();
/*     */ 
/* 198 */         throw new InvalidValue();
/*     */       }
/*     */     }
/* 201 */     this.index = (paramArrayOfNameValuePair.length == 0 ? -1 : 0);
/* 202 */     this.representations = 4;
/*     */   }
/*     */ 
/*     */   public void set_members_as_dyn_any(NameDynAnyPair[] paramArrayOfNameDynAnyPair)
/*     */     throws TypeMismatch, InvalidValue
/*     */   {
/* 210 */     if (this.status == 2) {
/* 211 */       throw this.wrapper.dynAnyDestroyed();
/*     */     }
/* 213 */     if ((paramArrayOfNameDynAnyPair == null) || (paramArrayOfNameDynAnyPair.length == 0)) {
/* 214 */       clearData();
/* 215 */       return;
/*     */     }
/*     */ 
/* 222 */     TypeCode localTypeCode1 = this.any.type();
/*     */ 
/* 224 */     int i = 0;
/*     */     try {
/* 226 */       i = localTypeCode1.member_count();
/*     */     } catch (BadKind localBadKind1) {
/*     */     }
/* 229 */     if (i != paramArrayOfNameDynAnyPair.length) {
/* 230 */       clearData();
/* 231 */       throw new InvalidValue();
/*     */     }
/*     */ 
/* 234 */     allocComponents(paramArrayOfNameDynAnyPair);
/*     */ 
/* 236 */     for (int j = 0; j < paramArrayOfNameDynAnyPair.length; j++) {
/* 237 */       if (paramArrayOfNameDynAnyPair[j] != null) {
/* 238 */         String str1 = paramArrayOfNameDynAnyPair[j].id;
/* 239 */         String str2 = null;
/*     */         try {
/* 241 */           str2 = localTypeCode1.member_name(j);
/*     */         } catch (BadKind localBadKind2) {
/*     */         } catch (Bounds localBounds1) {
/*     */         }
/* 245 */         if ((!str2.equals(str1)) && (!str1.equals(""))) {
/* 246 */           clearData();
/*     */ 
/* 248 */           throw new TypeMismatch();
/*     */         }
/* 250 */         DynAny localDynAny = paramArrayOfNameDynAnyPair[j].value;
/* 251 */         Any localAny = getAny(localDynAny);
/* 252 */         TypeCode localTypeCode2 = null;
/*     */         try {
/* 254 */           localTypeCode2 = localTypeCode1.member_type(j);
/*     */         } catch (BadKind localBadKind3) {
/*     */         } catch (Bounds localBounds2) {
/*     */         }
/* 258 */         if (!localTypeCode2.equal(localAny.type())) {
/* 259 */           clearData();
/*     */ 
/* 261 */           throw new TypeMismatch();
/*     */         }
/*     */ 
/* 264 */         addComponent(j, str1, localAny, localDynAny);
/*     */       } else {
/* 266 */         clearData();
/*     */ 
/* 268 */         throw new InvalidValue();
/*     */       }
/*     */     }
/* 271 */     this.index = (paramArrayOfNameDynAnyPair.length == 0 ? -1 : 0);
/* 272 */     this.representations = 4;
/*     */   }
/*     */ 
/*     */   private void allocComponents(int paramInt)
/*     */   {
/* 280 */     this.components = new DynAny[paramInt];
/* 281 */     this.names = new String[paramInt];
/* 282 */     this.nameValuePairs = new NameValuePair[paramInt];
/* 283 */     this.nameDynAnyPairs = new NameDynAnyPair[paramInt];
/* 284 */     for (int i = 0; i < paramInt; i++) {
/* 285 */       this.nameValuePairs[i] = new NameValuePair();
/* 286 */       this.nameDynAnyPairs[i] = new NameDynAnyPair();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void allocComponents(NameValuePair[] paramArrayOfNameValuePair) {
/* 291 */     this.components = new DynAny[paramArrayOfNameValuePair.length];
/* 292 */     this.names = new String[paramArrayOfNameValuePair.length];
/* 293 */     this.nameValuePairs = paramArrayOfNameValuePair;
/* 294 */     this.nameDynAnyPairs = new NameDynAnyPair[paramArrayOfNameValuePair.length];
/* 295 */     for (int i = 0; i < paramArrayOfNameValuePair.length; i++)
/* 296 */       this.nameDynAnyPairs[i] = new NameDynAnyPair();
/*     */   }
/*     */ 
/*     */   private void allocComponents(NameDynAnyPair[] paramArrayOfNameDynAnyPair)
/*     */   {
/* 301 */     this.components = new DynAny[paramArrayOfNameDynAnyPair.length];
/* 302 */     this.names = new String[paramArrayOfNameDynAnyPair.length];
/* 303 */     this.nameValuePairs = new NameValuePair[paramArrayOfNameDynAnyPair.length];
/* 304 */     for (int i = 0; i < paramArrayOfNameDynAnyPair.length; i++) {
/* 305 */       this.nameValuePairs[i] = new NameValuePair();
/*     */     }
/* 307 */     this.nameDynAnyPairs = paramArrayOfNameDynAnyPair;
/*     */   }
/*     */ 
/*     */   private void addComponent(int paramInt, String paramString, Any paramAny, DynAny paramDynAny) {
/* 311 */     this.components[paramInt] = paramDynAny;
/* 312 */     this.names[paramInt] = (paramString != null ? paramString : "");
/* 313 */     this.nameValuePairs[paramInt].id = paramString;
/* 314 */     this.nameValuePairs[paramInt].value = paramAny;
/* 315 */     this.nameDynAnyPairs[paramInt].id = paramString;
/* 316 */     this.nameDynAnyPairs[paramInt].value = paramDynAny;
/* 317 */     if ((paramDynAny instanceof DynAnyImpl))
/* 318 */       ((DynAnyImpl)paramDynAny).setStatus((byte)1);
/*     */   }
/*     */ 
/*     */   protected boolean initializeComponentsFromAny()
/*     */   {
/* 325 */     TypeCode localTypeCode1 = this.any.type();
/* 326 */     TypeCode localTypeCode2 = null;
/*     */ 
/* 328 */     DynAny localDynAny = null;
/* 329 */     String str = null;
/* 330 */     int i = 0;
/*     */     try
/*     */     {
/* 333 */       i = localTypeCode1.member_count();
/*     */     }
/*     */     catch (BadKind localBadKind1) {
/*     */     }
/* 337 */     InputStream localInputStream = this.any.create_input_stream();
/*     */ 
/* 339 */     allocComponents(i);
/*     */ 
/* 341 */     for (int j = 0; j < i; j++) {
/*     */       try {
/* 343 */         str = localTypeCode1.member_name(j);
/* 344 */         localTypeCode2 = localTypeCode1.member_type(j);
/*     */       } catch (BadKind localBadKind2) {
/*     */       } catch (Bounds localBounds) {
/*     */       }
/* 348 */       Any localAny = DynAnyUtil.extractAnyFromStream(localTypeCode2, localInputStream, this.orb);
/*     */       try
/*     */       {
/* 351 */         localDynAny = DynAnyUtil.createMostDerivedDynAny(localAny, this.orb, false);
/*     */       }
/*     */       catch (InconsistentTypeCode localInconsistentTypeCode)
/*     */       {
/*     */       }
/*     */ 
/* 357 */       addComponent(j, str, localAny, localDynAny);
/*     */     }
/* 359 */     return true;
/*     */   }
/*     */ 
/*     */   protected boolean initializeComponentsFromTypeCode()
/*     */   {
/* 368 */     TypeCode localTypeCode1 = this.any.type();
/* 369 */     TypeCode localTypeCode2 = null;
/*     */ 
/* 371 */     DynAny localDynAny = null;
/*     */ 
/* 373 */     int i = 0;
/*     */     try
/*     */     {
/* 376 */       i = localTypeCode1.member_count();
/*     */     }
/*     */     catch (BadKind localBadKind1) {
/*     */     }
/* 380 */     allocComponents(i);
/*     */ 
/* 382 */     for (int j = 0; j < i; j++) {
/* 383 */       String str = null;
/*     */       try {
/* 385 */         str = localTypeCode1.member_name(j);
/* 386 */         localTypeCode2 = localTypeCode1.member_type(j);
/*     */       } catch (BadKind localBadKind2) {
/*     */       } catch (Bounds localBounds) {
/*     */       }
/*     */       try {
/* 391 */         localDynAny = DynAnyUtil.createMostDerivedDynAny(localTypeCode2, this.orb);
/*     */       }
/*     */       catch (InconsistentTypeCode localInconsistentTypeCode)
/*     */       {
/*     */       }
/*     */ 
/* 406 */       Any localAny = getAny(localDynAny);
/* 407 */       addComponent(j, str, localAny, localDynAny);
/*     */     }
/* 409 */     return true;
/*     */   }
/*     */ 
/*     */   protected void clearData()
/*     */   {
/* 416 */     super.clearData();
/* 417 */     this.names = null;
/* 418 */     this.nameValuePairs = null;
/* 419 */     this.nameDynAnyPairs = null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.dynamicany.DynAnyComplexImpl
 * JD-Core Version:    0.6.2
 */