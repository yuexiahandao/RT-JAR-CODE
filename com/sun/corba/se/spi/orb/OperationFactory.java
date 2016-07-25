/*     */ package com.sun.corba.se.spi.orb;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import java.lang.reflect.Array;
/*     */ import java.net.MalformedURLException;
/*     */ import java.net.URL;
/*     */ import java.util.Arrays;
/*     */ import java.util.StringTokenizer;
/*     */ import sun.corba.JavaCorbaAccess;
/*     */ import sun.corba.SharedSecrets;
/*     */ 
/*     */ public abstract class OperationFactory
/*     */ {
/* 182 */   private static Operation suffixActionImpl = new SuffixAction(null);
/*     */ 
/* 194 */   private static Operation valueActionImpl = new ValueAction(null);
/*     */ 
/* 206 */   private static Operation identityActionImpl = new IdentityAction(null);
/*     */ 
/* 218 */   private static Operation booleanActionImpl = new BooleanAction(null);
/*     */ 
/* 230 */   private static Operation integerActionImpl = new IntegerAction(null);
/*     */ 
/* 242 */   private static Operation stringActionImpl = new StringAction(null);
/*     */ 
/* 264 */   private static Operation classActionImpl = new ClassAction(null);
/*     */ 
/* 276 */   private static Operation setFlagActionImpl = new SetFlagAction(null);
/*     */ 
/* 295 */   private static Operation URLActionImpl = new URLAction(null);
/*     */ 
/* 561 */   private static Operation convertIntegerToShortImpl = new ConvertIntegerToShort(null);
/*     */ 
/*     */   private static String getString(Object paramObject)
/*     */   {
/*  76 */     if ((paramObject instanceof String)) {
/*  77 */       return (String)paramObject;
/*     */     }
/*  79 */     throw new Error("String expected");
/*     */   }
/*     */ 
/*     */   private static Object[] getObjectArray(Object paramObject)
/*     */   {
/*  84 */     if ((paramObject instanceof Object[])) {
/*  85 */       return (Object[])paramObject;
/*     */     }
/*  87 */     throw new Error("Object[] expected");
/*     */   }
/*     */ 
/*     */   private static StringPair getStringPair(Object paramObject)
/*     */   {
/*  92 */     if ((paramObject instanceof StringPair)) {
/*  93 */       return (StringPair)paramObject;
/*     */     }
/*  95 */     throw new Error("StringPair expected");
/*     */   }
/*     */ 
/*     */   public static Operation maskErrorAction(Operation paramOperation)
/*     */   {
/* 144 */     return new MaskErrorAction(paramOperation);
/*     */   }
/*     */ 
/*     */   public static Operation indexAction(int paramInt)
/*     */   {
/* 169 */     return new IndexAction(paramInt);
/*     */   }
/*     */ 
/*     */   public static Operation identityAction()
/*     */   {
/* 299 */     return identityActionImpl;
/*     */   }
/*     */ 
/*     */   public static Operation suffixAction()
/*     */   {
/* 304 */     return suffixActionImpl;
/*     */   }
/*     */ 
/*     */   public static Operation valueAction()
/*     */   {
/* 309 */     return valueActionImpl;
/*     */   }
/*     */ 
/*     */   public static Operation booleanAction()
/*     */   {
/* 314 */     return booleanActionImpl;
/*     */   }
/*     */ 
/*     */   public static Operation integerAction()
/*     */   {
/* 319 */     return integerActionImpl;
/*     */   }
/*     */ 
/*     */   public static Operation stringAction()
/*     */   {
/* 324 */     return stringActionImpl;
/*     */   }
/*     */ 
/*     */   public static Operation classAction()
/*     */   {
/* 329 */     return classActionImpl;
/*     */   }
/*     */ 
/*     */   public static Operation setFlagAction()
/*     */   {
/* 334 */     return setFlagActionImpl;
/*     */   }
/*     */ 
/*     */   public static Operation URLAction()
/*     */   {
/* 339 */     return URLActionImpl;
/*     */   }
/*     */ 
/*     */   public static Operation integerRangeAction(int paramInt1, int paramInt2)
/*     */   {
/* 371 */     return new IntegerRangeAction(paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   public static Operation listAction(String paramString, Operation paramOperation)
/*     */   {
/* 414 */     return new ListAction(paramString, paramOperation);
/*     */   }
/*     */ 
/*     */   public static Operation sequenceAction(String paramString, Operation[] paramArrayOfOperation)
/*     */   {
/* 459 */     return new SequenceAction(paramString, paramArrayOfOperation);
/*     */   }
/*     */ 
/*     */   public static Operation compose(Operation paramOperation1, Operation paramOperation2)
/*     */   {
/* 485 */     return new ComposeAction(paramOperation1, paramOperation2);
/*     */   }
/*     */ 
/*     */   public static Operation mapAction(Operation paramOperation)
/*     */   {
/* 513 */     return new MapAction(paramOperation);
/*     */   }
/*     */ 
/*     */   public static Operation mapSequenceAction(Operation[] paramArrayOfOperation)
/*     */   {
/* 545 */     return new MapSequenceAction(paramArrayOfOperation);
/*     */   }
/*     */ 
/*     */   public static Operation convertIntegerToShort()
/*     */   {
/* 565 */     return convertIntegerToShortImpl;
/*     */   }
/*     */ 
/*     */   private static class BooleanAction extends OperationFactory.OperationBase
/*     */   {
/*     */     private BooleanAction()
/*     */     {
/* 208 */       super();
/*     */     }
/*     */ 
/*     */     public Object operate(Object paramObject) {
/* 212 */       return new Boolean(OperationFactory.getString(paramObject));
/*     */     }
/*     */     public String toString() {
/* 215 */       return "booleanAction";
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ClassAction extends OperationFactory.OperationBase
/*     */   {
/*     */     private ClassAction()
/*     */     {
/* 244 */       super();
/*     */     }
/*     */ 
/*     */     public Object operate(Object paramObject) {
/* 248 */       String str = OperationFactory.getString(paramObject);
/*     */       try
/*     */       {
/* 251 */         return SharedSecrets.getJavaCorbaAccess().loadClass(str);
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 255 */         ORBUtilSystemException localORBUtilSystemException = ORBUtilSystemException.get("orb.lifecycle");
/*     */ 
/* 257 */         throw localORBUtilSystemException.couldNotLoadClass(localException, str);
/*     */       }
/*     */     }
/*     */ 
/* 261 */     public String toString() { return "classAction"; }
/*     */ 
/*     */   }
/*     */ 
/*     */   private static class ComposeAction extends OperationFactory.OperationBase
/*     */   {
/*     */     private Operation op1;
/*     */     private Operation op2;
/*     */ 
/*     */     ComposeAction(Operation paramOperation1, Operation paramOperation2)
/*     */     {
/* 468 */       super();
/* 469 */       this.op1 = paramOperation1;
/* 470 */       this.op2 = paramOperation2;
/*     */     }
/*     */ 
/*     */     public Object operate(Object paramObject)
/*     */     {
/* 475 */       return this.op2.operate(this.op1.operate(paramObject));
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 479 */       return "composition(" + this.op1 + "," + this.op2 + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ConvertIntegerToShort extends OperationFactory.OperationBase
/*     */   {
/*     */     private ConvertIntegerToShort()
/*     */     {
/* 548 */       super();
/*     */     }
/*     */ 
/*     */     public Object operate(Object paramObject) {
/* 552 */       Integer localInteger = (Integer)paramObject;
/* 553 */       return new Short(localInteger.shortValue());
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 557 */       return "ConvertIntegerToShort";
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class IdentityAction extends OperationFactory.OperationBase
/*     */   {
/*     */     private IdentityAction()
/*     */     {
/* 196 */       super();
/*     */     }
/*     */ 
/*     */     public Object operate(Object paramObject) {
/* 200 */       return paramObject;
/*     */     }
/*     */     public String toString() {
/* 203 */       return "identityAction";
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class IndexAction extends OperationFactory.OperationBase
/*     */   {
/*     */     private int index;
/*     */ 
/*     */     public IndexAction(int paramInt)
/*     */     {
/* 152 */       super();
/* 153 */       this.index = paramInt;
/*     */     }
/*     */ 
/*     */     public Object operate(Object paramObject)
/*     */     {
/* 158 */       return OperationFactory.getObjectArray(paramObject)[this.index];
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 163 */       return "indexAction(" + this.index + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class IntegerAction extends OperationFactory.OperationBase
/*     */   {
/*     */     private IntegerAction()
/*     */     {
/* 220 */       super();
/*     */     }
/*     */ 
/*     */     public Object operate(Object paramObject) {
/* 224 */       return new Integer(OperationFactory.getString(paramObject));
/*     */     }
/*     */     public String toString() {
/* 227 */       return "integerAction";
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class IntegerRangeAction extends OperationFactory.OperationBase
/*     */   {
/*     */     private int min;
/*     */     private int max;
/*     */ 
/*     */     IntegerRangeAction(int paramInt1, int paramInt2)
/*     */     {
/* 348 */       super();
/* 349 */       this.min = paramInt1;
/* 350 */       this.max = paramInt2;
/*     */     }
/*     */ 
/*     */     public Object operate(Object paramObject)
/*     */     {
/* 355 */       int i = Integer.parseInt(OperationFactory.getString(paramObject));
/* 356 */       if ((i >= this.min) && (i <= this.max)) {
/* 357 */         return new Integer(i);
/*     */       }
/* 359 */       throw new IllegalArgumentException("Property value " + i + " is not in the range " + this.min + " to " + this.max);
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 365 */       return "integerRangeAction(" + this.min + "," + this.max + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class ListAction extends OperationFactory.OperationBase
/*     */   {
/*     */     private String sep;
/*     */     private Operation act;
/*     */ 
/*     */     ListAction(String paramString, Operation paramOperation)
/*     */     {
/* 379 */       super();
/* 380 */       this.sep = paramString;
/* 381 */       this.act = paramOperation;
/*     */     }
/*     */ 
/*     */     public Object operate(Object paramObject)
/*     */     {
/* 390 */       StringTokenizer localStringTokenizer = new StringTokenizer(OperationFactory.getString(paramObject), this.sep);
/*     */ 
/* 392 */       int i = localStringTokenizer.countTokens();
/* 393 */       Object localObject1 = null;
/* 394 */       int j = 0;
/* 395 */       while (localStringTokenizer.hasMoreTokens()) {
/* 396 */         String str = localStringTokenizer.nextToken();
/* 397 */         Object localObject2 = this.act.operate(str);
/* 398 */         if (localObject1 == null)
/* 399 */           localObject1 = Array.newInstance(localObject2.getClass(), i);
/* 400 */         Array.set(localObject1, j++, localObject2);
/*     */       }
/*     */ 
/* 403 */       return localObject1;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 407 */       return "listAction(separator=\"" + this.sep + "\",action=" + this.act + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class MapAction extends OperationFactory.OperationBase
/*     */   {
/*     */     Operation op;
/*     */ 
/*     */     MapAction(Operation paramOperation)
/*     */     {
/* 493 */       super();
/* 494 */       this.op = paramOperation;
/*     */     }
/*     */ 
/*     */     public Object operate(Object paramObject)
/*     */     {
/* 499 */       Object[] arrayOfObject1 = (Object[])paramObject;
/* 500 */       Object[] arrayOfObject2 = new Object[arrayOfObject1.length];
/* 501 */       for (int i = 0; i < arrayOfObject1.length; i++)
/* 502 */         arrayOfObject2[i] = this.op.operate(arrayOfObject1[i]);
/* 503 */       return arrayOfObject2;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 507 */       return "mapAction(" + this.op + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class MapSequenceAction extends OperationFactory.OperationBase
/*     */   {
/*     */     private Operation[] op;
/*     */ 
/*     */     public MapSequenceAction(Operation[] paramArrayOfOperation)
/*     */     {
/* 521 */       super();
/* 522 */       this.op = paramArrayOfOperation;
/*     */     }
/*     */ 
/*     */     public Object operate(Object paramObject)
/*     */     {
/* 530 */       Object[] arrayOfObject1 = (Object[])paramObject;
/* 531 */       Object[] arrayOfObject2 = new Object[arrayOfObject1.length];
/* 532 */       for (int i = 0; i < arrayOfObject1.length; i++)
/* 533 */         arrayOfObject2[i] = this.op[i].operate(arrayOfObject1[i]);
/* 534 */       return arrayOfObject2;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 538 */       return "mapSequenceAction(" + Arrays.toString(this.op) + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class MaskErrorAction extends OperationFactory.OperationBase
/*     */   {
/*     */     private Operation op;
/*     */ 
/*     */     public MaskErrorAction(Operation paramOperation)
/*     */     {
/* 123 */       super();
/* 124 */       this.op = paramOperation;
/*     */     }
/*     */ 
/*     */     public Object operate(Object paramObject)
/*     */     {
/*     */       try {
/* 130 */         return this.op.operate(paramObject); } catch (Exception localException) {
/*     */       }
/* 132 */       return null;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 138 */       return "maskErrorAction(" + this.op + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   private static abstract class OperationBase
/*     */     implements Operation
/*     */   {
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 101 */       if (this == paramObject) {
/* 102 */         return true;
/*     */       }
/* 104 */       if (!(paramObject instanceof OperationBase)) {
/* 105 */         return false;
/*     */       }
/* 107 */       OperationBase localOperationBase = (OperationBase)paramObject;
/*     */ 
/* 109 */       return toString().equals(localOperationBase.toString());
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 114 */       return toString().hashCode();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SequenceAction extends OperationFactory.OperationBase
/*     */   {
/*     */     private String sep;
/*     */     private Operation[] actions;
/*     */ 
/*     */     SequenceAction(String paramString, Operation[] paramArrayOfOperation)
/*     */     {
/* 423 */       super();
/* 424 */       this.sep = paramString;
/* 425 */       this.actions = paramArrayOfOperation;
/*     */     }
/*     */ 
/*     */     public Object operate(Object paramObject)
/*     */     {
/* 430 */       StringTokenizer localStringTokenizer = new StringTokenizer(OperationFactory.getString(paramObject), this.sep);
/*     */ 
/* 433 */       int i = localStringTokenizer.countTokens();
/* 434 */       if (i != this.actions.length) {
/* 435 */         throw new Error("Number of tokens and number of actions do not match");
/*     */       }
/*     */ 
/* 438 */       int j = 0;
/* 439 */       Object[] arrayOfObject = new Object[i];
/* 440 */       while (localStringTokenizer.hasMoreTokens()) {
/* 441 */         Operation localOperation = this.actions[j];
/* 442 */         String str = localStringTokenizer.nextToken();
/* 443 */         arrayOfObject[(j++)] = localOperation.operate(str);
/*     */       }
/*     */ 
/* 446 */       return arrayOfObject;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 450 */       return "sequenceAction(separator=\"" + this.sep + "\",actions=" + Arrays.toString(this.actions) + ")";
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SetFlagAction extends OperationFactory.OperationBase
/*     */   {
/*     */     private SetFlagAction()
/*     */     {
/* 266 */       super();
/*     */     }
/*     */ 
/*     */     public Object operate(Object paramObject) {
/* 270 */       return Boolean.TRUE;
/*     */     }
/*     */     public String toString() {
/* 273 */       return "setFlagAction";
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class StringAction extends OperationFactory.OperationBase
/*     */   {
/*     */     private StringAction()
/*     */     {
/* 232 */       super();
/*     */     }
/*     */ 
/*     */     public Object operate(Object paramObject) {
/* 236 */       return paramObject;
/*     */     }
/*     */     public String toString() {
/* 239 */       return "stringAction";
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SuffixAction extends OperationFactory.OperationBase
/*     */   {
/*     */     private SuffixAction()
/*     */     {
/* 172 */       super();
/*     */     }
/*     */ 
/*     */     public Object operate(Object paramObject) {
/* 176 */       return OperationFactory.getStringPair(paramObject).getFirst();
/*     */     }
/*     */     public String toString() {
/* 179 */       return "suffixAction";
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class URLAction extends OperationFactory.OperationBase
/*     */   {
/*     */     private URLAction()
/*     */     {
/* 278 */       super();
/*     */     }
/*     */ 
/*     */     public Object operate(Object paramObject) {
/* 282 */       String str = (String)paramObject;
/*     */       try {
/* 284 */         return new URL(str);
/*     */       } catch (MalformedURLException localMalformedURLException) {
/* 286 */         ORBUtilSystemException localORBUtilSystemException = ORBUtilSystemException.get("orb.lifecycle");
/*     */ 
/* 288 */         throw localORBUtilSystemException.badUrl(localMalformedURLException, str);
/*     */       }
/*     */     }
/*     */ 
/* 292 */     public String toString() { return "URLAction"; }
/*     */ 
/*     */   }
/*     */ 
/*     */   private static class ValueAction extends OperationFactory.OperationBase
/*     */   {
/*     */     private ValueAction()
/*     */     {
/* 184 */       super();
/*     */     }
/*     */ 
/*     */     public Object operate(Object paramObject) {
/* 188 */       return OperationFactory.getStringPair(paramObject).getSecond();
/*     */     }
/*     */     public String toString() {
/* 191 */       return "valueAction";
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.spi.orb.OperationFactory
 * JD-Core Version:    0.6.2
 */