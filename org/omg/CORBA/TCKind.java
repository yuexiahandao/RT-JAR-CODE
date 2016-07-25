/*     */ package org.omg.CORBA;
/*     */ 
/*     */ public class TCKind
/*     */ {
/*     */   public static final int _tk_null = 0;
/*     */   public static final int _tk_void = 1;
/*     */   public static final int _tk_short = 2;
/*     */   public static final int _tk_long = 3;
/*     */   public static final int _tk_ushort = 4;
/*     */   public static final int _tk_ulong = 5;
/*     */   public static final int _tk_float = 6;
/*     */   public static final int _tk_double = 7;
/*     */   public static final int _tk_boolean = 8;
/*     */   public static final int _tk_char = 9;
/*     */   public static final int _tk_octet = 10;
/*     */   public static final int _tk_any = 11;
/*     */   public static final int _tk_TypeCode = 12;
/*     */   public static final int _tk_Principal = 13;
/*     */   public static final int _tk_objref = 14;
/*     */   public static final int _tk_struct = 15;
/*     */   public static final int _tk_union = 16;
/*     */   public static final int _tk_enum = 17;
/*     */   public static final int _tk_string = 18;
/*     */   public static final int _tk_sequence = 19;
/*     */   public static final int _tk_array = 20;
/*     */   public static final int _tk_alias = 21;
/*     */   public static final int _tk_except = 22;
/*     */   public static final int _tk_longlong = 23;
/*     */   public static final int _tk_ulonglong = 24;
/*     */   public static final int _tk_longdouble = 25;
/*     */   public static final int _tk_wchar = 26;
/*     */   public static final int _tk_wstring = 27;
/*     */   public static final int _tk_fixed = 28;
/*     */   public static final int _tk_value = 29;
/*     */   public static final int _tk_value_box = 30;
/*     */   public static final int _tk_native = 31;
/*     */   public static final int _tk_abstract_interface = 32;
/* 242 */   public static final TCKind tk_null = new TCKind(0);
/*     */ 
/* 248 */   public static final TCKind tk_void = new TCKind(1);
/*     */ 
/* 254 */   public static final TCKind tk_short = new TCKind(2);
/*     */ 
/* 260 */   public static final TCKind tk_long = new TCKind(3);
/*     */ 
/* 266 */   public static final TCKind tk_ushort = new TCKind(4);
/*     */ 
/* 272 */   public static final TCKind tk_ulong = new TCKind(5);
/*     */ 
/* 278 */   public static final TCKind tk_float = new TCKind(6);
/*     */ 
/* 284 */   public static final TCKind tk_double = new TCKind(7);
/*     */ 
/* 290 */   public static final TCKind tk_boolean = new TCKind(8);
/*     */ 
/* 296 */   public static final TCKind tk_char = new TCKind(9);
/*     */ 
/* 302 */   public static final TCKind tk_octet = new TCKind(10);
/*     */ 
/* 308 */   public static final TCKind tk_any = new TCKind(11);
/*     */ 
/* 314 */   public static final TCKind tk_TypeCode = new TCKind(12);
/*     */ 
/* 320 */   public static final TCKind tk_Principal = new TCKind(13);
/*     */ 
/* 326 */   public static final TCKind tk_objref = new TCKind(14);
/*     */ 
/* 332 */   public static final TCKind tk_struct = new TCKind(15);
/*     */ 
/* 338 */   public static final TCKind tk_union = new TCKind(16);
/*     */ 
/* 344 */   public static final TCKind tk_enum = new TCKind(17);
/*     */ 
/* 350 */   public static final TCKind tk_string = new TCKind(18);
/*     */ 
/* 356 */   public static final TCKind tk_sequence = new TCKind(19);
/*     */ 
/* 362 */   public static final TCKind tk_array = new TCKind(20);
/*     */ 
/* 368 */   public static final TCKind tk_alias = new TCKind(21);
/*     */ 
/* 374 */   public static final TCKind tk_except = new TCKind(22);
/*     */ 
/* 380 */   public static final TCKind tk_longlong = new TCKind(23);
/*     */ 
/* 386 */   public static final TCKind tk_ulonglong = new TCKind(24);
/*     */ 
/* 392 */   public static final TCKind tk_longdouble = new TCKind(25);
/*     */ 
/* 398 */   public static final TCKind tk_wchar = new TCKind(26);
/*     */ 
/* 404 */   public static final TCKind tk_wstring = new TCKind(27);
/*     */ 
/* 410 */   public static final TCKind tk_fixed = new TCKind(28);
/*     */ 
/* 418 */   public static final TCKind tk_value = new TCKind(29);
/*     */ 
/* 424 */   public static final TCKind tk_value_box = new TCKind(30);
/*     */ 
/* 431 */   public static final TCKind tk_native = new TCKind(31);
/*     */ 
/* 437 */   public static final TCKind tk_abstract_interface = new TCKind(32);
/*     */   private int _value;
/*     */ 
/*     */   public int value()
/*     */   {
/* 449 */     return this._value;
/*     */   }
/*     */ 
/*     */   public static TCKind from_int(int paramInt)
/*     */   {
/* 466 */     switch (paramInt) {
/*     */     case 0:
/* 468 */       return tk_null;
/*     */     case 1:
/* 470 */       return tk_void;
/*     */     case 2:
/* 472 */       return tk_short;
/*     */     case 3:
/* 474 */       return tk_long;
/*     */     case 4:
/* 476 */       return tk_ushort;
/*     */     case 5:
/* 478 */       return tk_ulong;
/*     */     case 6:
/* 480 */       return tk_float;
/*     */     case 7:
/* 482 */       return tk_double;
/*     */     case 8:
/* 484 */       return tk_boolean;
/*     */     case 9:
/* 486 */       return tk_char;
/*     */     case 10:
/* 488 */       return tk_octet;
/*     */     case 11:
/* 490 */       return tk_any;
/*     */     case 12:
/* 492 */       return tk_TypeCode;
/*     */     case 13:
/* 494 */       return tk_Principal;
/*     */     case 14:
/* 496 */       return tk_objref;
/*     */     case 15:
/* 498 */       return tk_struct;
/*     */     case 16:
/* 500 */       return tk_union;
/*     */     case 17:
/* 502 */       return tk_enum;
/*     */     case 18:
/* 504 */       return tk_string;
/*     */     case 19:
/* 506 */       return tk_sequence;
/*     */     case 20:
/* 508 */       return tk_array;
/*     */     case 21:
/* 510 */       return tk_alias;
/*     */     case 22:
/* 512 */       return tk_except;
/*     */     case 23:
/* 514 */       return tk_longlong;
/*     */     case 24:
/* 516 */       return tk_ulonglong;
/*     */     case 25:
/* 518 */       return tk_longdouble;
/*     */     case 26:
/* 520 */       return tk_wchar;
/*     */     case 27:
/* 522 */       return tk_wstring;
/*     */     case 28:
/* 524 */       return tk_fixed;
/*     */     case 29:
/* 526 */       return tk_value;
/*     */     case 30:
/* 528 */       return tk_value_box;
/*     */     case 31:
/* 530 */       return tk_native;
/*     */     case 32:
/* 532 */       return tk_abstract_interface;
/*     */     }
/* 534 */     throw new BAD_PARAM();
/*     */   }
/*     */ 
/*     */   @Deprecated
/*     */   protected TCKind(int paramInt)
/*     */   {
/* 551 */     this._value = paramInt;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.TCKind
 * JD-Core Version:    0.6.2
 */