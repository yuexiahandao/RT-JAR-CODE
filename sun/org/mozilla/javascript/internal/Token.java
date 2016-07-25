/*     */ package sun.org.mozilla.javascript.internal;
/*     */ 
/*     */ public class Token
/*     */ {
/*     */   public static final boolean printTrees = false;
/*     */   static final boolean printICode = false;
/*     */   static final boolean printNames = false;
/*     */   public static final int ERROR = -1;
/*     */   public static final int EOF = 0;
/*     */   public static final int EOL = 1;
/*     */   public static final int FIRST_BYTECODE_TOKEN = 2;
/*     */   public static final int ENTERWITH = 2;
/*     */   public static final int LEAVEWITH = 3;
/*     */   public static final int RETURN = 4;
/*     */   public static final int GOTO = 5;
/*     */   public static final int IFEQ = 6;
/*     */   public static final int IFNE = 7;
/*     */   public static final int SETNAME = 8;
/*     */   public static final int BITOR = 9;
/*     */   public static final int BITXOR = 10;
/*     */   public static final int BITAND = 11;
/*     */   public static final int EQ = 12;
/*     */   public static final int NE = 13;
/*     */   public static final int LT = 14;
/*     */   public static final int LE = 15;
/*     */   public static final int GT = 16;
/*     */   public static final int GE = 17;
/*     */   public static final int LSH = 18;
/*     */   public static final int RSH = 19;
/*     */   public static final int URSH = 20;
/*     */   public static final int ADD = 21;
/*     */   public static final int SUB = 22;
/*     */   public static final int MUL = 23;
/*     */   public static final int DIV = 24;
/*     */   public static final int MOD = 25;
/*     */   public static final int NOT = 26;
/*     */   public static final int BITNOT = 27;
/*     */   public static final int POS = 28;
/*     */   public static final int NEG = 29;
/*     */   public static final int NEW = 30;
/*     */   public static final int DELPROP = 31;
/*     */   public static final int TYPEOF = 32;
/*     */   public static final int GETPROP = 33;
/*     */   public static final int GETPROPNOWARN = 34;
/*     */   public static final int SETPROP = 35;
/*     */   public static final int GETELEM = 36;
/*     */   public static final int SETELEM = 37;
/*     */   public static final int CALL = 38;
/*     */   public static final int NAME = 39;
/*     */   public static final int NUMBER = 40;
/*     */   public static final int STRING = 41;
/*     */   public static final int NULL = 42;
/*     */   public static final int THIS = 43;
/*     */   public static final int FALSE = 44;
/*     */   public static final int TRUE = 45;
/*     */   public static final int SHEQ = 46;
/*     */   public static final int SHNE = 47;
/*     */   public static final int REGEXP = 48;
/*     */   public static final int BINDNAME = 49;
/*     */   public static final int THROW = 50;
/*     */   public static final int RETHROW = 51;
/*     */   public static final int IN = 52;
/*     */   public static final int INSTANCEOF = 53;
/*     */   public static final int LOCAL_LOAD = 54;
/*     */   public static final int GETVAR = 55;
/*     */   public static final int SETVAR = 56;
/*     */   public static final int CATCH_SCOPE = 57;
/*     */   public static final int ENUM_INIT_KEYS = 58;
/*     */   public static final int ENUM_INIT_VALUES = 59;
/*     */   public static final int ENUM_INIT_ARRAY = 60;
/*     */   public static final int ENUM_NEXT = 61;
/*     */   public static final int ENUM_ID = 62;
/*     */   public static final int THISFN = 63;
/*     */   public static final int RETURN_RESULT = 64;
/*     */   public static final int ARRAYLIT = 65;
/*     */   public static final int OBJECTLIT = 66;
/*     */   public static final int GET_REF = 67;
/*     */   public static final int SET_REF = 68;
/*     */   public static final int DEL_REF = 69;
/*     */   public static final int REF_CALL = 70;
/*     */   public static final int REF_SPECIAL = 71;
/*     */   public static final int YIELD = 72;
/*     */   public static final int STRICT_SETNAME = 73;
/*     */   public static final int DEFAULTNAMESPACE = 74;
/*     */   public static final int ESCXMLATTR = 75;
/*     */   public static final int ESCXMLTEXT = 76;
/*     */   public static final int REF_MEMBER = 77;
/*     */   public static final int REF_NS_MEMBER = 78;
/*     */   public static final int REF_NAME = 79;
/*     */   public static final int REF_NS_NAME = 80;
/*     */   public static final int LAST_BYTECODE_TOKEN = 80;
/*     */   public static final int TRY = 81;
/*     */   public static final int SEMI = 82;
/*     */   public static final int LB = 83;
/*     */   public static final int RB = 84;
/*     */   public static final int LC = 85;
/*     */   public static final int RC = 86;
/*     */   public static final int LP = 87;
/*     */   public static final int RP = 88;
/*     */   public static final int COMMA = 89;
/*     */   public static final int ASSIGN = 90;
/*     */   public static final int ASSIGN_BITOR = 91;
/*     */   public static final int ASSIGN_BITXOR = 92;
/*     */   public static final int ASSIGN_BITAND = 93;
/*     */   public static final int ASSIGN_LSH = 94;
/*     */   public static final int ASSIGN_RSH = 95;
/*     */   public static final int ASSIGN_URSH = 96;
/*     */   public static final int ASSIGN_ADD = 97;
/*     */   public static final int ASSIGN_SUB = 98;
/*     */   public static final int ASSIGN_MUL = 99;
/*     */   public static final int ASSIGN_DIV = 100;
/*     */   public static final int ASSIGN_MOD = 101;
/*     */   public static final int FIRST_ASSIGN = 90;
/*     */   public static final int LAST_ASSIGN = 101;
/*     */   public static final int HOOK = 102;
/*     */   public static final int COLON = 103;
/*     */   public static final int OR = 104;
/*     */   public static final int AND = 105;
/*     */   public static final int INC = 106;
/*     */   public static final int DEC = 107;
/*     */   public static final int DOT = 108;
/*     */   public static final int FUNCTION = 109;
/*     */   public static final int EXPORT = 110;
/*     */   public static final int IMPORT = 111;
/*     */   public static final int IF = 112;
/*     */   public static final int ELSE = 113;
/*     */   public static final int SWITCH = 114;
/*     */   public static final int CASE = 115;
/*     */   public static final int DEFAULT = 116;
/*     */   public static final int WHILE = 117;
/*     */   public static final int DO = 118;
/*     */   public static final int FOR = 119;
/*     */   public static final int BREAK = 120;
/*     */   public static final int CONTINUE = 121;
/*     */   public static final int VAR = 122;
/*     */   public static final int WITH = 123;
/*     */   public static final int CATCH = 124;
/*     */   public static final int FINALLY = 125;
/*     */   public static final int VOID = 126;
/*     */   public static final int RESERVED = 127;
/*     */   public static final int EMPTY = 128;
/*     */   public static final int BLOCK = 129;
/*     */   public static final int LABEL = 130;
/*     */   public static final int TARGET = 131;
/*     */   public static final int LOOP = 132;
/*     */   public static final int EXPR_VOID = 133;
/*     */   public static final int EXPR_RESULT = 134;
/*     */   public static final int JSR = 135;
/*     */   public static final int SCRIPT = 136;
/*     */   public static final int TYPEOFNAME = 137;
/*     */   public static final int USE_STACK = 138;
/*     */   public static final int SETPROP_OP = 139;
/*     */   public static final int SETELEM_OP = 140;
/*     */   public static final int LOCAL_BLOCK = 141;
/*     */   public static final int SET_REF_OP = 142;
/*     */   public static final int DOTDOT = 143;
/*     */   public static final int COLONCOLON = 144;
/*     */   public static final int XML = 145;
/*     */   public static final int DOTQUERY = 146;
/*     */   public static final int XMLATTR = 147;
/*     */   public static final int XMLEND = 148;
/*     */   public static final int TO_OBJECT = 149;
/*     */   public static final int TO_DOUBLE = 150;
/*     */   public static final int GET = 151;
/*     */   public static final int SET = 152;
/*     */   public static final int LET = 153;
/*     */   public static final int CONST = 154;
/*     */   public static final int SETCONST = 155;
/*     */   public static final int SETCONSTVAR = 156;
/*     */   public static final int ARRAYCOMP = 157;
/*     */   public static final int LETEXPR = 158;
/*     */   public static final int WITHEXPR = 159;
/*     */   public static final int DEBUGGER = 160;
/*     */   public static final int COMMENT = 161;
/*     */   public static final int LAST_TOKEN = 162;
/*     */ 
/*     */   public static String name(int paramInt)
/*     */   {
/* 276 */     return String.valueOf(paramInt);
/*     */   }
/*     */ 
/*     */   public static String typeToName(int paramInt)
/*     */   {
/* 288 */     switch (paramInt) { case -1:
/* 289 */       return "ERROR";
/*     */     case 0:
/* 290 */       return "EOF";
/*     */     case 1:
/* 291 */       return "EOL";
/*     */     case 2:
/* 292 */       return "ENTERWITH";
/*     */     case 3:
/* 293 */       return "LEAVEWITH";
/*     */     case 4:
/* 294 */       return "RETURN";
/*     */     case 5:
/* 295 */       return "GOTO";
/*     */     case 6:
/* 296 */       return "IFEQ";
/*     */     case 7:
/* 297 */       return "IFNE";
/*     */     case 8:
/* 298 */       return "SETNAME";
/*     */     case 9:
/* 299 */       return "BITOR";
/*     */     case 10:
/* 300 */       return "BITXOR";
/*     */     case 11:
/* 301 */       return "BITAND";
/*     */     case 12:
/* 302 */       return "EQ";
/*     */     case 13:
/* 303 */       return "NE";
/*     */     case 14:
/* 304 */       return "LT";
/*     */     case 15:
/* 305 */       return "LE";
/*     */     case 16:
/* 306 */       return "GT";
/*     */     case 17:
/* 307 */       return "GE";
/*     */     case 18:
/* 308 */       return "LSH";
/*     */     case 19:
/* 309 */       return "RSH";
/*     */     case 20:
/* 310 */       return "URSH";
/*     */     case 21:
/* 311 */       return "ADD";
/*     */     case 22:
/* 312 */       return "SUB";
/*     */     case 23:
/* 313 */       return "MUL";
/*     */     case 24:
/* 314 */       return "DIV";
/*     */     case 25:
/* 315 */       return "MOD";
/*     */     case 26:
/* 316 */       return "NOT";
/*     */     case 27:
/* 317 */       return "BITNOT";
/*     */     case 28:
/* 318 */       return "POS";
/*     */     case 29:
/* 319 */       return "NEG";
/*     */     case 30:
/* 320 */       return "NEW";
/*     */     case 31:
/* 321 */       return "DELPROP";
/*     */     case 32:
/* 322 */       return "TYPEOF";
/*     */     case 33:
/* 323 */       return "GETPROP";
/*     */     case 34:
/* 324 */       return "GETPROPNOWARN";
/*     */     case 35:
/* 325 */       return "SETPROP";
/*     */     case 36:
/* 326 */       return "GETELEM";
/*     */     case 37:
/* 327 */       return "SETELEM";
/*     */     case 38:
/* 328 */       return "CALL";
/*     */     case 39:
/* 329 */       return "NAME";
/*     */     case 40:
/* 330 */       return "NUMBER";
/*     */     case 41:
/* 331 */       return "STRING";
/*     */     case 42:
/* 332 */       return "NULL";
/*     */     case 43:
/* 333 */       return "THIS";
/*     */     case 44:
/* 334 */       return "FALSE";
/*     */     case 45:
/* 335 */       return "TRUE";
/*     */     case 46:
/* 336 */       return "SHEQ";
/*     */     case 47:
/* 337 */       return "SHNE";
/*     */     case 48:
/* 338 */       return "REGEXP";
/*     */     case 49:
/* 339 */       return "BINDNAME";
/*     */     case 50:
/* 340 */       return "THROW";
/*     */     case 51:
/* 341 */       return "RETHROW";
/*     */     case 52:
/* 342 */       return "IN";
/*     */     case 53:
/* 343 */       return "INSTANCEOF";
/*     */     case 54:
/* 344 */       return "LOCAL_LOAD";
/*     */     case 55:
/* 345 */       return "GETVAR";
/*     */     case 56:
/* 346 */       return "SETVAR";
/*     */     case 57:
/* 347 */       return "CATCH_SCOPE";
/*     */     case 58:
/* 348 */       return "ENUM_INIT_KEYS";
/*     */     case 59:
/* 349 */       return "ENUM_INIT_VALUES";
/*     */     case 60:
/* 350 */       return "ENUM_INIT_ARRAY";
/*     */     case 61:
/* 351 */       return "ENUM_NEXT";
/*     */     case 62:
/* 352 */       return "ENUM_ID";
/*     */     case 63:
/* 353 */       return "THISFN";
/*     */     case 64:
/* 354 */       return "RETURN_RESULT";
/*     */     case 65:
/* 355 */       return "ARRAYLIT";
/*     */     case 66:
/* 356 */       return "OBJECTLIT";
/*     */     case 67:
/* 357 */       return "GET_REF";
/*     */     case 68:
/* 358 */       return "SET_REF";
/*     */     case 69:
/* 359 */       return "DEL_REF";
/*     */     case 70:
/* 360 */       return "REF_CALL";
/*     */     case 71:
/* 361 */       return "REF_SPECIAL";
/*     */     case 74:
/* 362 */       return "DEFAULTNAMESPACE";
/*     */     case 76:
/* 363 */       return "ESCXMLTEXT";
/*     */     case 75:
/* 364 */       return "ESCXMLATTR";
/*     */     case 77:
/* 365 */       return "REF_MEMBER";
/*     */     case 78:
/* 366 */       return "REF_NS_MEMBER";
/*     */     case 79:
/* 367 */       return "REF_NAME";
/*     */     case 80:
/* 368 */       return "REF_NS_NAME";
/*     */     case 81:
/* 369 */       return "TRY";
/*     */     case 82:
/* 370 */       return "SEMI";
/*     */     case 83:
/* 371 */       return "LB";
/*     */     case 84:
/* 372 */       return "RB";
/*     */     case 85:
/* 373 */       return "LC";
/*     */     case 86:
/* 374 */       return "RC";
/*     */     case 87:
/* 375 */       return "LP";
/*     */     case 88:
/* 376 */       return "RP";
/*     */     case 89:
/* 377 */       return "COMMA";
/*     */     case 90:
/* 378 */       return "ASSIGN";
/*     */     case 91:
/* 379 */       return "ASSIGN_BITOR";
/*     */     case 92:
/* 380 */       return "ASSIGN_BITXOR";
/*     */     case 93:
/* 381 */       return "ASSIGN_BITAND";
/*     */     case 94:
/* 382 */       return "ASSIGN_LSH";
/*     */     case 95:
/* 383 */       return "ASSIGN_RSH";
/*     */     case 96:
/* 384 */       return "ASSIGN_URSH";
/*     */     case 97:
/* 385 */       return "ASSIGN_ADD";
/*     */     case 98:
/* 386 */       return "ASSIGN_SUB";
/*     */     case 99:
/* 387 */       return "ASSIGN_MUL";
/*     */     case 100:
/* 388 */       return "ASSIGN_DIV";
/*     */     case 101:
/* 389 */       return "ASSIGN_MOD";
/*     */     case 102:
/* 390 */       return "HOOK";
/*     */     case 103:
/* 391 */       return "COLON";
/*     */     case 104:
/* 392 */       return "OR";
/*     */     case 105:
/* 393 */       return "AND";
/*     */     case 106:
/* 394 */       return "INC";
/*     */     case 107:
/* 395 */       return "DEC";
/*     */     case 108:
/* 396 */       return "DOT";
/*     */     case 109:
/* 397 */       return "FUNCTION";
/*     */     case 110:
/* 398 */       return "EXPORT";
/*     */     case 111:
/* 399 */       return "IMPORT";
/*     */     case 112:
/* 400 */       return "IF";
/*     */     case 113:
/* 401 */       return "ELSE";
/*     */     case 114:
/* 402 */       return "SWITCH";
/*     */     case 115:
/* 403 */       return "CASE";
/*     */     case 116:
/* 404 */       return "DEFAULT";
/*     */     case 117:
/* 405 */       return "WHILE";
/*     */     case 118:
/* 406 */       return "DO";
/*     */     case 119:
/* 407 */       return "FOR";
/*     */     case 120:
/* 408 */       return "BREAK";
/*     */     case 121:
/* 409 */       return "CONTINUE";
/*     */     case 122:
/* 410 */       return "VAR";
/*     */     case 123:
/* 411 */       return "WITH";
/*     */     case 124:
/* 412 */       return "CATCH";
/*     */     case 125:
/* 413 */       return "FINALLY";
/*     */     case 126:
/* 414 */       return "VOID";
/*     */     case 127:
/* 415 */       return "RESERVED";
/*     */     case 128:
/* 416 */       return "EMPTY";
/*     */     case 129:
/* 417 */       return "BLOCK";
/*     */     case 130:
/* 418 */       return "LABEL";
/*     */     case 131:
/* 419 */       return "TARGET";
/*     */     case 132:
/* 420 */       return "LOOP";
/*     */     case 133:
/* 421 */       return "EXPR_VOID";
/*     */     case 134:
/* 422 */       return "EXPR_RESULT";
/*     */     case 135:
/* 423 */       return "JSR";
/*     */     case 136:
/* 424 */       return "SCRIPT";
/*     */     case 137:
/* 425 */       return "TYPEOFNAME";
/*     */     case 138:
/* 426 */       return "USE_STACK";
/*     */     case 139:
/* 427 */       return "SETPROP_OP";
/*     */     case 140:
/* 428 */       return "SETELEM_OP";
/*     */     case 141:
/* 429 */       return "LOCAL_BLOCK";
/*     */     case 142:
/* 430 */       return "SET_REF_OP";
/*     */     case 143:
/* 431 */       return "DOTDOT";
/*     */     case 144:
/* 432 */       return "COLONCOLON";
/*     */     case 145:
/* 433 */       return "XML";
/*     */     case 146:
/* 434 */       return "DOTQUERY";
/*     */     case 147:
/* 435 */       return "XMLATTR";
/*     */     case 148:
/* 436 */       return "XMLEND";
/*     */     case 149:
/* 437 */       return "TO_OBJECT";
/*     */     case 150:
/* 438 */       return "TO_DOUBLE";
/*     */     case 151:
/* 439 */       return "GET";
/*     */     case 152:
/* 440 */       return "SET";
/*     */     case 153:
/* 441 */       return "LET";
/*     */     case 72:
/* 442 */       return "YIELD";
/*     */     case 154:
/* 443 */       return "CONST";
/*     */     case 155:
/* 444 */       return "SETCONST";
/*     */     case 157:
/* 445 */       return "ARRAYCOMP";
/*     */     case 159:
/* 446 */       return "WITHEXPR";
/*     */     case 158:
/* 447 */       return "LETEXPR";
/*     */     case 160:
/* 448 */       return "DEBUGGER";
/*     */     case 161:
/* 449 */       return "COMMENT";
/*     */     case 73:
/*     */     case 156:
/*     */     }
/* 453 */     throw new IllegalStateException(String.valueOf(paramInt));
/*     */   }
/*     */ 
/*     */   public static boolean isValidToken(int paramInt)
/*     */   {
/* 462 */     return (paramInt >= -1) && (paramInt <= 162);
/*     */   }
/*     */ 
/*     */   public static enum CommentType
/*     */   {
/*  60 */     LINE, BLOCK_COMMENT, JSDOC, HTML;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.Token
 * JD-Core Version:    0.6.2
 */