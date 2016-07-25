/*     */ package org.omg.CORBA;
/*     */ 
/*     */ import org.omg.CORBA.portable.IDLEntity;
/*     */ 
/*     */ public class DefinitionKind
/*     */   implements IDLEntity
/*     */ {
/*     */   public static final int _dk_none = 0;
/*     */   public static final int _dk_all = 1;
/*     */   public static final int _dk_Attribute = 2;
/*     */   public static final int _dk_Constant = 3;
/*     */   public static final int _dk_Exception = 4;
/*     */   public static final int _dk_Interface = 5;
/*     */   public static final int _dk_Module = 6;
/*     */   public static final int _dk_Operation = 7;
/*     */   public static final int _dk_Typedef = 8;
/*     */   public static final int _dk_Alias = 9;
/*     */   public static final int _dk_Struct = 10;
/*     */   public static final int _dk_Union = 11;
/*     */   public static final int _dk_Enum = 12;
/*     */   public static final int _dk_Primitive = 13;
/*     */   public static final int _dk_String = 14;
/*     */   public static final int _dk_Sequence = 15;
/*     */   public static final int _dk_Array = 16;
/*     */   public static final int _dk_Repository = 17;
/*     */   public static final int _dk_Wstring = 18;
/*     */   public static final int _dk_Fixed = 19;
/*     */   public static final int _dk_Value = 20;
/*     */   public static final int _dk_ValueBox = 21;
/*     */   public static final int _dk_ValueMember = 22;
/*     */   public static final int _dk_Native = 23;
/*     */   public static final int _dk_AbstractInterface = 24;
/* 226 */   public static final DefinitionKind dk_none = new DefinitionKind(0);
/*     */ 
/* 236 */   public static final DefinitionKind dk_all = new DefinitionKind(1);
/*     */ 
/* 243 */   public static final DefinitionKind dk_Attribute = new DefinitionKind(2);
/*     */ 
/* 250 */   public static final DefinitionKind dk_Constant = new DefinitionKind(3);
/*     */ 
/* 258 */   public static final DefinitionKind dk_Exception = new DefinitionKind(4);
/*     */ 
/* 265 */   public static final DefinitionKind dk_Interface = new DefinitionKind(5);
/*     */ 
/* 272 */   public static final DefinitionKind dk_Module = new DefinitionKind(6);
/*     */ 
/* 279 */   public static final DefinitionKind dk_Operation = new DefinitionKind(7);
/*     */ 
/* 286 */   public static final DefinitionKind dk_Typedef = new DefinitionKind(8);
/*     */ 
/* 293 */   public static final DefinitionKind dk_Alias = new DefinitionKind(9);
/*     */ 
/* 300 */   public static final DefinitionKind dk_Struct = new DefinitionKind(10);
/*     */ 
/* 307 */   public static final DefinitionKind dk_Union = new DefinitionKind(11);
/*     */ 
/* 314 */   public static final DefinitionKind dk_Enum = new DefinitionKind(12);
/*     */ 
/* 321 */   public static final DefinitionKind dk_Primitive = new DefinitionKind(13);
/*     */ 
/* 328 */   public static final DefinitionKind dk_String = new DefinitionKind(14);
/*     */ 
/* 335 */   public static final DefinitionKind dk_Sequence = new DefinitionKind(15);
/*     */ 
/* 342 */   public static final DefinitionKind dk_Array = new DefinitionKind(16);
/*     */ 
/* 350 */   public static final DefinitionKind dk_Repository = new DefinitionKind(17);
/*     */ 
/* 358 */   public static final DefinitionKind dk_Wstring = new DefinitionKind(18);
/*     */ 
/* 365 */   public static final DefinitionKind dk_Fixed = new DefinitionKind(19);
/*     */ 
/* 372 */   public static final DefinitionKind dk_Value = new DefinitionKind(20);
/*     */ 
/* 379 */   public static final DefinitionKind dk_ValueBox = new DefinitionKind(21);
/*     */ 
/* 386 */   public static final DefinitionKind dk_ValueMember = new DefinitionKind(22);
/*     */ 
/* 394 */   public static final DefinitionKind dk_Native = new DefinitionKind(23);
/*     */ 
/* 401 */   public static final DefinitionKind dk_AbstractInterface = new DefinitionKind(24);
/*     */   private int _value;
/*     */ 
/*     */   public int value()
/*     */   {
/* 412 */     return this._value;
/*     */   }
/*     */ 
/*     */   public static DefinitionKind from_int(int paramInt)
/*     */   {
/* 430 */     switch (paramInt) {
/*     */     case 0:
/* 432 */       return dk_none;
/*     */     case 1:
/* 434 */       return dk_all;
/*     */     case 2:
/* 436 */       return dk_Attribute;
/*     */     case 3:
/* 438 */       return dk_Constant;
/*     */     case 4:
/* 440 */       return dk_Exception;
/*     */     case 5:
/* 442 */       return dk_Interface;
/*     */     case 6:
/* 444 */       return dk_Module;
/*     */     case 7:
/* 446 */       return dk_Operation;
/*     */     case 8:
/* 448 */       return dk_Typedef;
/*     */     case 9:
/* 450 */       return dk_Alias;
/*     */     case 10:
/* 452 */       return dk_Struct;
/*     */     case 11:
/* 454 */       return dk_Union;
/*     */     case 12:
/* 456 */       return dk_Enum;
/*     */     case 13:
/* 458 */       return dk_Primitive;
/*     */     case 14:
/* 460 */       return dk_String;
/*     */     case 15:
/* 462 */       return dk_Sequence;
/*     */     case 16:
/* 464 */       return dk_Array;
/*     */     case 17:
/* 466 */       return dk_Repository;
/*     */     case 18:
/* 468 */       return dk_Wstring;
/*     */     case 19:
/* 470 */       return dk_Fixed;
/*     */     case 20:
/* 472 */       return dk_Value;
/*     */     case 21:
/* 474 */       return dk_ValueBox;
/*     */     case 22:
/* 476 */       return dk_ValueMember;
/*     */     case 23:
/* 478 */       return dk_Native;
/*     */     }
/* 480 */     throw new BAD_PARAM();
/*     */   }
/*     */ 
/*     */   protected DefinitionKind(int paramInt)
/*     */   {
/* 492 */     this._value = paramInt;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.DefinitionKind
 * JD-Core Version:    0.6.2
 */