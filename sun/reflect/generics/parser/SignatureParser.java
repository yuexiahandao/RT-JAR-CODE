/*     */ package sun.reflect.generics.parser;
/*     */ 
/*     */ import java.lang.reflect.GenericSignatureFormatError;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import sun.reflect.generics.tree.ArrayTypeSignature;
/*     */ import sun.reflect.generics.tree.BaseType;
/*     */ import sun.reflect.generics.tree.BooleanSignature;
/*     */ import sun.reflect.generics.tree.BottomSignature;
/*     */ import sun.reflect.generics.tree.ByteSignature;
/*     */ import sun.reflect.generics.tree.CharSignature;
/*     */ import sun.reflect.generics.tree.ClassSignature;
/*     */ import sun.reflect.generics.tree.ClassTypeSignature;
/*     */ import sun.reflect.generics.tree.DoubleSignature;
/*     */ import sun.reflect.generics.tree.FieldTypeSignature;
/*     */ import sun.reflect.generics.tree.FloatSignature;
/*     */ import sun.reflect.generics.tree.FormalTypeParameter;
/*     */ import sun.reflect.generics.tree.IntSignature;
/*     */ import sun.reflect.generics.tree.LongSignature;
/*     */ import sun.reflect.generics.tree.MethodTypeSignature;
/*     */ import sun.reflect.generics.tree.ReturnType;
/*     */ import sun.reflect.generics.tree.ShortSignature;
/*     */ import sun.reflect.generics.tree.SimpleClassTypeSignature;
/*     */ import sun.reflect.generics.tree.TypeArgument;
/*     */ import sun.reflect.generics.tree.TypeSignature;
/*     */ import sun.reflect.generics.tree.TypeVariableSignature;
/*     */ import sun.reflect.generics.tree.VoidDescriptor;
/*     */ import sun.reflect.generics.tree.Wildcard;
/*     */ 
/*     */ public class SignatureParser
/*     */ {
/*     */   private char[] input;
/*  68 */   private int index = 0;
/*     */   private static final char EOI = ':';
/*     */   private static final boolean DEBUG = false;
/*     */ 
/*     */   private char getNext()
/*     */   {
/*  85 */     assert (this.index <= this.input.length);
/*     */     try {
/*  87 */       return this.input[(this.index++)]; } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/*  88 */     }return ':';
/*     */   }
/*     */ 
/*     */   private char current()
/*     */   {
/*  93 */     assert (this.index <= this.input.length);
/*     */     try {
/*  95 */       return this.input[this.index]; } catch (ArrayIndexOutOfBoundsException localArrayIndexOutOfBoundsException) {
/*  96 */     }return ':';
/*     */   }
/*     */ 
/*     */   private void advance()
/*     */   {
/* 101 */     assert (this.index <= this.input.length);
/* 102 */     this.index += 1;
/*     */   }
/*     */ 
/*     */   private String remainder()
/*     */   {
/* 107 */     return new String(this.input, this.index, this.input.length - this.index);
/*     */   }
/*     */ 
/*     */   private boolean matches(char paramChar, char[] paramArrayOfChar)
/*     */   {
/* 112 */     for (char c : paramArrayOfChar) {
/* 113 */       if (paramChar == c) return true;
/*     */     }
/* 115 */     return false;
/*     */   }
/*     */ 
/*     */   private Error error(String paramString)
/*     */   {
/* 126 */     return new GenericSignatureFormatError();
/*     */   }
/*     */ 
/*     */   private void progress(int paramInt)
/*     */   {
/* 134 */     if (this.index <= paramInt)
/* 135 */       throw error("Failure to make progress!");
/*     */   }
/*     */ 
/*     */   public static SignatureParser make()
/*     */   {
/* 143 */     return new SignatureParser();
/*     */   }
/*     */ 
/*     */   public ClassSignature parseClassSig(String paramString)
/*     */   {
/* 157 */     this.input = paramString.toCharArray();
/* 158 */     return parseClassSignature();
/*     */   }
/*     */ 
/*     */   public MethodTypeSignature parseMethodSig(String paramString)
/*     */   {
/* 172 */     this.input = paramString.toCharArray();
/* 173 */     return parseMethodTypeSignature();
/*     */   }
/*     */ 
/*     */   public TypeSignature parseTypeSig(String paramString)
/*     */   {
/* 189 */     this.input = paramString.toCharArray();
/* 190 */     return parseTypeSignature();
/*     */   }
/*     */ 
/*     */   private ClassSignature parseClassSignature()
/*     */   {
/* 213 */     assert (this.index == 0);
/* 214 */     return ClassSignature.make(parseZeroOrMoreFormalTypeParameters(), parseClassTypeSignature(), parseSuperInterfaces());
/*     */   }
/*     */ 
/*     */   private FormalTypeParameter[] parseZeroOrMoreFormalTypeParameters()
/*     */   {
/* 220 */     if (current() == '<') {
/* 221 */       return parseFormalTypeParameters();
/*     */     }
/* 223 */     return new FormalTypeParameter[0];
/*     */   }
/*     */ 
/*     */   private FormalTypeParameter[] parseFormalTypeParameters()
/*     */   {
/* 232 */     ArrayList localArrayList = new ArrayList(3);
/* 233 */     assert (current() == '<');
/* 234 */     if (current() != '<') throw error("expected '<'");
/* 235 */     advance();
/* 236 */     localArrayList.add(parseFormalTypeParameter());
/* 237 */     while (current() != '>') {
/* 238 */       int i = this.index;
/* 239 */       localArrayList.add(parseFormalTypeParameter());
/* 240 */       progress(i);
/*     */     }
/* 242 */     advance();
/* 243 */     return (FormalTypeParameter[])localArrayList.toArray(new FormalTypeParameter[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   private FormalTypeParameter parseFormalTypeParameter()
/*     */   {
/* 251 */     String str = parseIdentifier();
/* 252 */     FieldTypeSignature[] arrayOfFieldTypeSignature = parseBounds();
/* 253 */     return FormalTypeParameter.make(str, arrayOfFieldTypeSignature);
/*     */   }
/*     */ 
/*     */   private String parseIdentifier() {
/* 257 */     StringBuilder localStringBuilder = new StringBuilder();
/* 258 */     while (!Character.isWhitespace(current())) {
/* 259 */       char c = current();
/* 260 */       switch (c) {
/*     */       case '.':
/*     */       case '/':
/*     */       case ':':
/*     */       case ';':
/*     */       case '<':
/*     */       case '>':
/*     */       case '[':
/* 268 */         return localStringBuilder.toString();
/*     */       }
/* 270 */       localStringBuilder.append(c);
/* 271 */       advance();
/*     */     }
/*     */ 
/* 276 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private FieldTypeSignature parseFieldTypeSignature()
/*     */   {
/* 285 */     return parseFieldTypeSignature(true);
/*     */   }
/*     */ 
/*     */   private FieldTypeSignature parseFieldTypeSignature(boolean paramBoolean) {
/* 289 */     switch (current()) {
/*     */     case 'L':
/* 291 */       return parseClassTypeSignature();
/*     */     case 'T':
/* 293 */       return parseTypeVariableSignature();
/*     */     case '[':
/* 295 */       if (paramBoolean) {
/* 296 */         return parseArrayTypeSignature();
/*     */       }
/* 298 */       throw error("Array signature not allowed here.");
/* 299 */     }throw error("Expected Field Type Signature");
/*     */   }
/*     */ 
/*     */   private ClassTypeSignature parseClassTypeSignature()
/*     */   {
/* 308 */     assert (current() == 'L');
/* 309 */     if (current() != 'L') throw error("expected a class type");
/* 310 */     advance();
/* 311 */     ArrayList localArrayList = new ArrayList(5);
/* 312 */     localArrayList.add(parsePackageNameAndSimpleClassTypeSignature());
/*     */ 
/* 314 */     parseClassTypeSignatureSuffix(localArrayList);
/* 315 */     if (current() != ';') {
/* 316 */       throw error("expected ';' got '" + current() + "'");
/*     */     }
/* 318 */     advance();
/* 319 */     return ClassTypeSignature.make(localArrayList);
/*     */   }
/*     */ 
/*     */   private SimpleClassTypeSignature parsePackageNameAndSimpleClassTypeSignature()
/*     */   {
/* 330 */     String str = parseIdentifier();
/*     */ 
/* 332 */     if (current() == '/') {
/* 333 */       StringBuilder localStringBuilder = new StringBuilder(str);
/*     */ 
/* 335 */       while (current() == '/') {
/* 336 */         advance();
/* 337 */         localStringBuilder.append(".");
/* 338 */         localStringBuilder.append(parseIdentifier());
/*     */       }
/* 340 */       str = localStringBuilder.toString();
/*     */     }
/*     */ 
/* 343 */     switch (current()) {
/*     */     case ';':
/* 345 */       return SimpleClassTypeSignature.make(str, false, new TypeArgument[0]);
/*     */     case '<':
/* 348 */       return SimpleClassTypeSignature.make(str, false, parseTypeArguments());
/*     */     }
/* 350 */     throw error("expected '<' or ';' but got " + current());
/*     */   }
/*     */ 
/*     */   private SimpleClassTypeSignature parseSimpleClassTypeSignature(boolean paramBoolean)
/*     */   {
/* 359 */     String str = parseIdentifier();
/* 360 */     char c = current();
/*     */ 
/* 362 */     switch (c) {
/*     */     case '.':
/*     */     case ';':
/* 365 */       return SimpleClassTypeSignature.make(str, paramBoolean, new TypeArgument[0]);
/*     */     case '<':
/* 367 */       return SimpleClassTypeSignature.make(str, paramBoolean, parseTypeArguments());
/*     */     }
/* 369 */     throw error("expected '<' or ';' or '.', got '" + c + "'.");
/*     */   }
/*     */ 
/*     */   private void parseClassTypeSignatureSuffix(List<SimpleClassTypeSignature> paramList)
/*     */   {
/* 378 */     while (current() == '.') {
/* 379 */       advance();
/* 380 */       paramList.add(parseSimpleClassTypeSignature(true));
/*     */     }
/*     */   }
/*     */ 
/*     */   private TypeArgument[] parseTypeArgumentsOpt() {
/* 385 */     if (current() == '<') return parseTypeArguments();
/* 386 */     return new TypeArgument[0];
/*     */   }
/*     */ 
/*     */   private TypeArgument[] parseTypeArguments()
/*     */   {
/* 394 */     ArrayList localArrayList = new ArrayList(3);
/* 395 */     assert (current() == '<');
/* 396 */     if (current() != '<') throw error("expected '<'");
/* 397 */     advance();
/* 398 */     localArrayList.add(parseTypeArgument());
/* 399 */     while (current() != '>')
/*     */     {
/* 401 */       localArrayList.add(parseTypeArgument());
/*     */     }
/* 403 */     advance();
/* 404 */     return (TypeArgument[])localArrayList.toArray(new TypeArgument[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   private TypeArgument parseTypeArgument()
/*     */   {
/* 414 */     FieldTypeSignature[] arrayOfFieldTypeSignature1 = new FieldTypeSignature[1];
/* 415 */     FieldTypeSignature[] arrayOfFieldTypeSignature2 = new FieldTypeSignature[1];
/* 416 */     TypeArgument[] arrayOfTypeArgument = new TypeArgument[0];
/* 417 */     int i = current();
/* 418 */     switch (i) {
/*     */     case 43:
/* 420 */       advance();
/* 421 */       arrayOfFieldTypeSignature1[0] = parseFieldTypeSignature();
/* 422 */       arrayOfFieldTypeSignature2[0] = BottomSignature.make();
/* 423 */       return Wildcard.make(arrayOfFieldTypeSignature1, arrayOfFieldTypeSignature2);
/*     */     case 42:
/* 426 */       advance();
/* 427 */       arrayOfFieldTypeSignature1[0] = SimpleClassTypeSignature.make("java.lang.Object", false, arrayOfTypeArgument);
/* 428 */       arrayOfFieldTypeSignature2[0] = BottomSignature.make();
/* 429 */       return Wildcard.make(arrayOfFieldTypeSignature1, arrayOfFieldTypeSignature2);
/*     */     case 45:
/* 432 */       advance();
/* 433 */       arrayOfFieldTypeSignature2[0] = parseFieldTypeSignature();
/* 434 */       arrayOfFieldTypeSignature1[0] = SimpleClassTypeSignature.make("java.lang.Object", false, arrayOfTypeArgument);
/* 435 */       return Wildcard.make(arrayOfFieldTypeSignature1, arrayOfFieldTypeSignature2);
/*     */     case 44:
/*     */     }
/* 438 */     return parseFieldTypeSignature();
/*     */   }
/*     */ 
/*     */   private TypeVariableSignature parseTypeVariableSignature()
/*     */   {
/* 447 */     assert (current() == 'T');
/* 448 */     if (current() != 'T') throw error("expected a type variable usage");
/* 449 */     advance();
/* 450 */     TypeVariableSignature localTypeVariableSignature = TypeVariableSignature.make(parseIdentifier());
/* 451 */     if (current() != ';') {
/* 452 */       throw error("; expected in signature of type variable named" + localTypeVariableSignature.getIdentifier());
/*     */     }
/*     */ 
/* 455 */     advance();
/* 456 */     return localTypeVariableSignature;
/*     */   }
/*     */ 
/*     */   private ArrayTypeSignature parseArrayTypeSignature()
/*     */   {
/* 464 */     if (current() != '[') throw error("expected array type signature");
/* 465 */     advance();
/* 466 */     return ArrayTypeSignature.make(parseTypeSignature());
/*     */   }
/*     */ 
/*     */   private TypeSignature parseTypeSignature()
/*     */   {
/* 475 */     switch (current()) {
/*     */     case 'B':
/*     */     case 'C':
/*     */     case 'D':
/*     */     case 'F':
/*     */     case 'I':
/*     */     case 'J':
/*     */     case 'S':
/*     */     case 'Z':
/* 484 */       return parseBaseType();
/*     */     case 'E':
/*     */     case 'G':
/*     */     case 'H':
/*     */     case 'K':
/*     */     case 'L':
/*     */     case 'M':
/*     */     case 'N':
/*     */     case 'O':
/*     */     case 'P':
/*     */     case 'Q':
/*     */     case 'R':
/*     */     case 'T':
/*     */     case 'U':
/*     */     case 'V':
/*     */     case 'W':
/*     */     case 'X':
/* 487 */     case 'Y': } return parseFieldTypeSignature();
/*     */   }
/*     */ 
/*     */   private BaseType parseBaseType()
/*     */   {
/* 492 */     switch (current()) {
/*     */     case 'B':
/* 494 */       advance();
/* 495 */       return ByteSignature.make();
/*     */     case 'C':
/* 497 */       advance();
/* 498 */       return CharSignature.make();
/*     */     case 'D':
/* 500 */       advance();
/* 501 */       return DoubleSignature.make();
/*     */     case 'F':
/* 503 */       advance();
/* 504 */       return FloatSignature.make();
/*     */     case 'I':
/* 506 */       advance();
/* 507 */       return IntSignature.make();
/*     */     case 'J':
/* 509 */       advance();
/* 510 */       return LongSignature.make();
/*     */     case 'S':
/* 512 */       advance();
/* 513 */       return ShortSignature.make();
/*     */     case 'Z':
/* 515 */       advance();
/* 516 */       return BooleanSignature.make();
/*     */     case 'E':
/*     */     case 'G':
/*     */     case 'H':
/*     */     case 'K':
/*     */     case 'L':
/*     */     case 'M':
/*     */     case 'N':
/*     */     case 'O':
/*     */     case 'P':
/*     */     case 'Q':
/*     */     case 'R':
/*     */     case 'T':
/*     */     case 'U':
/*     */     case 'V':
/*     */     case 'W':
/*     */     case 'X':
/* 518 */     case 'Y': } if (!$assertionsDisabled) throw new AssertionError();
/* 519 */     throw error("expected primitive type");
/*     */   }
/*     */ 
/*     */   private FieldTypeSignature[] parseBounds()
/*     */   {
/* 532 */     ArrayList localArrayList = new ArrayList(3);
/*     */ 
/* 534 */     if (current() == ':') {
/* 535 */       advance();
/* 536 */       switch (current()) {
/*     */       case ':':
/* 538 */         break;
/*     */       default:
/* 541 */         localArrayList.add(parseFieldTypeSignature());
/*     */       }
/*     */ 
/* 545 */       while (current() == ':') {
/* 546 */         advance();
/* 547 */         localArrayList.add(parseFieldTypeSignature());
/*     */       }
/*     */     }
/* 550 */     error("Bound expected");
/*     */ 
/* 552 */     return (FieldTypeSignature[])localArrayList.toArray(new FieldTypeSignature[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   private ClassTypeSignature[] parseSuperInterfaces()
/*     */   {
/* 560 */     ArrayList localArrayList = new ArrayList(5);
/* 561 */     while (current() == 'L') {
/* 562 */       localArrayList.add(parseClassTypeSignature());
/*     */     }
/* 564 */     return (ClassTypeSignature[])localArrayList.toArray(new ClassTypeSignature[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   private MethodTypeSignature parseMethodTypeSignature()
/*     */   {
/* 576 */     assert (this.index == 0);
/* 577 */     return MethodTypeSignature.make(parseZeroOrMoreFormalTypeParameters(), parseFormalParameters(), parseReturnType(), parseZeroOrMoreThrowsSignatures());
/*     */   }
/*     */ 
/*     */   private TypeSignature[] parseFormalParameters()
/*     */   {
/* 585 */     if (current() != '(') throw error("expected '('");
/* 586 */     advance();
/* 587 */     TypeSignature[] arrayOfTypeSignature = parseZeroOrMoreTypeSignatures();
/* 588 */     if (current() != ')') throw error("expected ')'");
/* 589 */     advance();
/* 590 */     return arrayOfTypeSignature;
/*     */   }
/*     */ 
/*     */   private TypeSignature[] parseZeroOrMoreTypeSignatures()
/*     */   {
/* 595 */     ArrayList localArrayList = new ArrayList();
/* 596 */     int i = 0;
/* 597 */     while (i == 0) {
/* 598 */       switch (current()) {
/*     */       case 'B':
/*     */       case 'C':
/*     */       case 'D':
/*     */       case 'F':
/*     */       case 'I':
/*     */       case 'J':
/*     */       case 'L':
/*     */       case 'S':
/*     */       case 'T':
/*     */       case 'Z':
/*     */       case '[':
/* 610 */         localArrayList.add(parseTypeSignature());
/* 611 */         break;
/*     */       case 'E':
/*     */       case 'G':
/*     */       case 'H':
/*     */       case 'K':
/*     */       case 'M':
/*     */       case 'N':
/*     */       case 'O':
/*     */       case 'P':
/*     */       case 'Q':
/*     */       case 'R':
/*     */       case 'U':
/*     */       case 'V':
/*     */       case 'W':
/*     */       case 'X':
/*     */       case 'Y':
/*     */       default:
/* 613 */         i = 1;
/*     */       }
/*     */     }
/* 616 */     return (TypeSignature[])localArrayList.toArray(new TypeSignature[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   private ReturnType parseReturnType()
/*     */   {
/* 625 */     if (current() == 'V') {
/* 626 */       advance();
/* 627 */       return VoidDescriptor.make();
/*     */     }
/* 629 */     return parseTypeSignature();
/*     */   }
/*     */ 
/*     */   private FieldTypeSignature[] parseZeroOrMoreThrowsSignatures()
/*     */   {
/* 634 */     ArrayList localArrayList = new ArrayList(3);
/* 635 */     while (current() == '^') {
/* 636 */       localArrayList.add(parseThrowsSignature());
/*     */     }
/* 638 */     return (FieldTypeSignature[])localArrayList.toArray(new FieldTypeSignature[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   private FieldTypeSignature parseThrowsSignature()
/*     */   {
/* 647 */     assert (current() == '^');
/* 648 */     if (current() != '^') throw error("expected throws signature");
/* 649 */     advance();
/* 650 */     return parseFieldTypeSignature(false);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.parser.SignatureParser
 * JD-Core Version:    0.6.2
 */