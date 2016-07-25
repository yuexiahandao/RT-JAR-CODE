/*     */ package java.beans;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.OutputStreamWriter;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.Field;
/*     */ import java.nio.charset.Charset;
/*     */ import java.nio.charset.CharsetEncoder;
/*     */ import java.util.ArrayList;
/*     */ import java.util.IdentityHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class XMLEncoder extends Encoder
/*     */   implements AutoCloseable
/*     */ {
/*     */   private final CharsetEncoder encoder;
/*     */   private final String charset;
/*     */   private final boolean declaration;
/*     */   private OutputStreamWriter out;
/*     */   private Object owner;
/* 215 */   private int indentation = 0;
/* 216 */   private boolean internal = false;
/*     */   private Map<Object, ValueData> valueToExpression;
/*     */   private Map<Object, List<Statement>> targetToStatementList;
/* 219 */   private boolean preambleWritten = false;
/*     */   private NameGenerator nameGenerator;
/*     */ 
/*     */   public XMLEncoder(OutputStream paramOutputStream)
/*     */   {
/* 242 */     this(paramOutputStream, "UTF-8", true, 0);
/*     */   }
/*     */ 
/*     */   public XMLEncoder(OutputStream paramOutputStream, String paramString, boolean paramBoolean, int paramInt)
/*     */   {
/* 278 */     if (paramOutputStream == null) {
/* 279 */       throw new IllegalArgumentException("the output stream cannot be null");
/*     */     }
/* 281 */     if (paramInt < 0) {
/* 282 */       throw new IllegalArgumentException("the indentation must be >= 0");
/*     */     }
/* 284 */     Charset localCharset = Charset.forName(paramString);
/* 285 */     this.encoder = localCharset.newEncoder();
/* 286 */     this.charset = paramString;
/* 287 */     this.declaration = paramBoolean;
/* 288 */     this.indentation = paramInt;
/* 289 */     this.out = new OutputStreamWriter(paramOutputStream, localCharset.newEncoder());
/* 290 */     this.valueToExpression = new IdentityHashMap();
/* 291 */     this.targetToStatementList = new IdentityHashMap();
/* 292 */     this.nameGenerator = new NameGenerator();
/*     */   }
/*     */ 
/*     */   public void setOwner(Object paramObject)
/*     */   {
/* 303 */     this.owner = paramObject;
/* 304 */     writeExpression(new Expression(this, "getOwner", new Object[0]));
/*     */   }
/*     */ 
/*     */   public Object getOwner()
/*     */   {
/* 315 */     return this.owner;
/*     */   }
/*     */ 
/*     */   public void writeObject(Object paramObject)
/*     */   {
/* 326 */     if (this.internal) {
/* 327 */       super.writeObject(paramObject);
/*     */     }
/*     */     else
/* 330 */       writeStatement(new Statement(this, "writeObject", new Object[] { paramObject }));
/*     */   }
/*     */ 
/*     */   private List<Statement> statementList(Object paramObject)
/*     */   {
/* 335 */     Object localObject = (List)this.targetToStatementList.get(paramObject);
/* 336 */     if (localObject == null) {
/* 337 */       localObject = new ArrayList();
/* 338 */       this.targetToStatementList.put(paramObject, localObject);
/*     */     }
/* 340 */     return localObject;
/*     */   }
/*     */ 
/*     */   private void mark(Object paramObject, boolean paramBoolean)
/*     */   {
/* 345 */     if ((paramObject == null) || (paramObject == this)) {
/* 346 */       return;
/*     */     }
/* 348 */     ValueData localValueData = getValueData(paramObject);
/* 349 */     Expression localExpression = localValueData.exp;
/*     */ 
/* 352 */     if ((paramObject.getClass() == String.class) && (localExpression == null)) {
/* 353 */       return;
/*     */     }
/*     */ 
/* 357 */     if (paramBoolean) {
/* 358 */       localValueData.refs += 1;
/*     */     }
/* 360 */     if (localValueData.marked) {
/* 361 */       return;
/*     */     }
/* 363 */     localValueData.marked = true;
/* 364 */     Object localObject = localExpression.getTarget();
/* 365 */     mark(localExpression);
/* 366 */     if (!(localObject instanceof Class)) {
/* 367 */       statementList(localObject).add(localExpression);
/*     */ 
/* 370 */       localValueData.refs += 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   private void mark(Statement paramStatement) {
/* 375 */     Object[] arrayOfObject = paramStatement.getArguments();
/* 376 */     for (int i = 0; i < arrayOfObject.length; i++) {
/* 377 */       Object localObject = arrayOfObject[i];
/* 378 */       mark(localObject, true);
/*     */     }
/* 380 */     mark(paramStatement.getTarget(), paramStatement instanceof Expression);
/*     */   }
/*     */ 
/*     */   public void writeStatement(Statement paramStatement)
/*     */   {
/* 397 */     boolean bool = this.internal;
/* 398 */     this.internal = true;
/*     */     try {
/* 400 */       super.writeStatement(paramStatement);
/*     */ 
/* 409 */       mark(paramStatement);
/* 410 */       Object localObject = paramStatement.getTarget();
/* 411 */       if ((localObject instanceof Field)) {
/* 412 */         String str = paramStatement.getMethodName();
/* 413 */         Object[] arrayOfObject = paramStatement.getArguments();
/* 414 */         if ((str != null) && (arrayOfObject != null))
/*     */         {
/* 416 */           if ((str.equals("get")) && (arrayOfObject.length == 1)) {
/* 417 */             localObject = arrayOfObject[0];
/*     */           }
/* 419 */           else if ((str.equals("set")) && (arrayOfObject.length == 2))
/* 420 */             localObject = arrayOfObject[0];
/*     */         }
/*     */       }
/* 423 */       statementList(localObject).add(paramStatement);
/*     */     }
/*     */     catch (Exception localException) {
/* 426 */       getExceptionListener().exceptionThrown(new Exception("XMLEncoder: discarding statement " + paramStatement, localException));
/*     */     }
/* 428 */     this.internal = bool;
/*     */   }
/*     */ 
/*     */   public void writeExpression(Expression paramExpression)
/*     */   {
/* 449 */     boolean bool = this.internal;
/* 450 */     this.internal = true;
/* 451 */     Object localObject = getValue(paramExpression);
/* 452 */     if ((get(localObject) == null) || (((localObject instanceof String)) && (!bool))) {
/* 453 */       getValueData(localObject).exp = paramExpression;
/* 454 */       super.writeExpression(paramExpression);
/*     */     }
/* 456 */     this.internal = bool;
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/* 468 */     if (!this.preambleWritten) {
/* 469 */       if (this.declaration) {
/* 470 */         writeln("<?xml version=" + quote("1.0") + " encoding=" + quote(this.charset) + "?>");
/*     */       }
/*     */ 
/* 473 */       writeln("<java version=" + quote(System.getProperty("java.version")) + " class=" + quote(XMLDecoder.class.getName()) + ">");
/*     */ 
/* 475 */       this.preambleWritten = true;
/*     */     }
/* 477 */     this.indentation += 1;
/* 478 */     List localList = statementList(this);
/* 479 */     while (!localList.isEmpty()) {
/* 480 */       localStatement = (Statement)localList.remove(0);
/* 481 */       if ("writeObject".equals(localStatement.getMethodName())) {
/* 482 */         outputValue(localStatement.getArguments()[0], this, true);
/*     */       }
/*     */       else {
/* 485 */         outputStatement(localStatement, this, false);
/*     */       }
/*     */     }
/* 488 */     this.indentation -= 1;
/*     */ 
/* 490 */     Statement localStatement = getMissedStatement();
/* 491 */     while (localStatement != null) {
/* 492 */       outputStatement(localStatement, this, false);
/* 493 */       localStatement = getMissedStatement();
/*     */     }
/*     */     try
/*     */     {
/* 497 */       this.out.flush();
/*     */     }
/*     */     catch (IOException localIOException) {
/* 500 */       getExceptionListener().exceptionThrown(localIOException);
/*     */     }
/* 502 */     clear();
/*     */   }
/*     */ 
/*     */   void clear() {
/* 506 */     super.clear();
/* 507 */     this.nameGenerator.clear();
/* 508 */     this.valueToExpression.clear();
/* 509 */     this.targetToStatementList.clear();
/*     */   }
/*     */ 
/*     */   Statement getMissedStatement() {
/* 513 */     for (List localList : this.targetToStatementList.values()) {
/* 514 */       for (int i = 0; i < localList.size(); i++) {
/* 515 */         if (Statement.class == ((Statement)localList.get(i)).getClass()) {
/* 516 */           return (Statement)localList.remove(i);
/*     */         }
/*     */       }
/*     */     }
/* 520 */     return null;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 530 */     flush();
/* 531 */     writeln("</java>");
/*     */     try {
/* 533 */       this.out.close();
/*     */     }
/*     */     catch (IOException localIOException) {
/* 536 */       getExceptionListener().exceptionThrown(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private String quote(String paramString) {
/* 541 */     return "\"" + paramString + "\"";
/*     */   }
/*     */ 
/*     */   private ValueData getValueData(Object paramObject) {
/* 545 */     ValueData localValueData = (ValueData)this.valueToExpression.get(paramObject);
/* 546 */     if (localValueData == null) {
/* 547 */       localValueData = new ValueData(null);
/* 548 */       this.valueToExpression.put(paramObject, localValueData);
/*     */     }
/* 550 */     return localValueData;
/*     */   }
/*     */ 
/*     */   private static boolean isValidCharCode(int paramInt)
/*     */   {
/* 573 */     return ((32 <= paramInt) && (paramInt <= 55295)) || (10 == paramInt) || (9 == paramInt) || (13 == paramInt) || ((57344 <= paramInt) && (paramInt <= 65533)) || ((65536 <= paramInt) && (paramInt <= 1114111));
/*     */   }
/*     */ 
/*     */   private void writeln(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 583 */       StringBuilder localStringBuilder = new StringBuilder();
/* 584 */       for (int i = 0; i < this.indentation; i++) {
/* 585 */         localStringBuilder.append(' ');
/*     */       }
/* 587 */       localStringBuilder.append(paramString);
/* 588 */       localStringBuilder.append('\n');
/* 589 */       this.out.write(localStringBuilder.toString());
/*     */     }
/*     */     catch (IOException localIOException) {
/* 592 */       getExceptionListener().exceptionThrown(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void outputValue(Object paramObject1, Object paramObject2, boolean paramBoolean) {
/* 597 */     if (paramObject1 == null) {
/* 598 */       writeln("<null/>");
/* 599 */       return;
/*     */     }
/*     */ 
/* 602 */     if ((paramObject1 instanceof Class)) {
/* 603 */       writeln("<class>" + ((Class)paramObject1).getName() + "</class>");
/* 604 */       return;
/*     */     }
/*     */ 
/* 607 */     ValueData localValueData = getValueData(paramObject1);
/* 608 */     if (localValueData.exp != null) {
/* 609 */       Object localObject1 = localValueData.exp.getTarget();
/* 610 */       String str1 = localValueData.exp.getMethodName();
/*     */ 
/* 612 */       if ((localObject1 == null) || (str1 == null)) {
/* 613 */         throw new NullPointerException((localObject1 == null ? "target" : "methodName") + " should not be null");
/*     */       }
/*     */ 
/* 617 */       if ((paramBoolean) && ((localObject1 instanceof Field)) && (str1.equals("get"))) {
/* 618 */         localObject2 = (Field)localObject1;
/* 619 */         writeln("<object class=" + quote(((Field)localObject2).getDeclaringClass().getName()) + " field=" + quote(((Field)localObject2).getName()) + "/>");
/*     */ 
/* 621 */         return;
/*     */       }
/*     */ 
/* 624 */       Object localObject2 = primitiveTypeFor(paramObject1.getClass());
/* 625 */       if ((localObject2 != null) && (localObject1 == paramObject1.getClass()) && (str1.equals("new")))
/*     */       {
/* 627 */         String str2 = ((Class)localObject2).getName();
/*     */ 
/* 629 */         if (localObject2 == Character.TYPE) {
/* 630 */           char c = ((Character)paramObject1).charValue();
/* 631 */           if (!isValidCharCode(c)) {
/* 632 */             writeln(createString(c));
/* 633 */             return;
/*     */           }
/* 635 */           paramObject1 = quoteCharCode(c);
/* 636 */           if (paramObject1 == null) {
/* 637 */             paramObject1 = Character.valueOf(c);
/*     */           }
/*     */         }
/* 640 */         writeln("<" + str2 + ">" + paramObject1 + "</" + str2 + ">");
/*     */ 
/* 642 */         return;
/*     */       }
/*     */     }
/* 645 */     else if ((paramObject1 instanceof String)) {
/* 646 */       writeln(createString((String)paramObject1));
/* 647 */       return;
/*     */     }
/*     */ 
/* 650 */     if (localValueData.name != null) {
/* 651 */       if (paramBoolean) {
/* 652 */         writeln("<object idref=" + quote(localValueData.name) + "/>");
/*     */       }
/*     */       else {
/* 655 */         outputXML("void", " idref=" + quote(localValueData.name), paramObject1, new Object[0]);
/*     */       }
/*     */     }
/* 658 */     else if (localValueData.exp != null)
/* 659 */       outputStatement(localValueData.exp, paramObject2, paramBoolean);
/*     */   }
/*     */ 
/*     */   private static String quoteCharCode(int paramInt)
/*     */   {
/* 664 */     switch (paramInt) { case 38:
/* 665 */       return "&amp;";
/*     */     case 60:
/* 666 */       return "&lt;";
/*     */     case 62:
/* 667 */       return "&gt;";
/*     */     case 34:
/* 668 */       return "&quot;";
/*     */     case 39:
/* 669 */       return "&apos;";
/*     */     case 13:
/* 670 */       return "&#13;"; }
/* 671 */     return null;
/*     */   }
/*     */ 
/*     */   private static String createString(int paramInt)
/*     */   {
/* 676 */     return "<char code=\"#" + Integer.toString(paramInt, 16) + "\"/>";
/*     */   }
/*     */ 
/*     */   private String createString(String paramString) {
/* 680 */     StringBuilder localStringBuilder = new StringBuilder();
/* 681 */     localStringBuilder.append("<string>");
/* 682 */     int i = 0;
/* 683 */     while (i < paramString.length()) {
/* 684 */       int j = paramString.codePointAt(i);
/* 685 */       int k = Character.charCount(j);
/*     */ 
/* 687 */       if ((isValidCharCode(j)) && (this.encoder.canEncode(paramString.substring(i, i + k)))) {
/* 688 */         String str = quoteCharCode(j);
/* 689 */         if (str != null)
/* 690 */           localStringBuilder.append(str);
/*     */         else {
/* 692 */           localStringBuilder.appendCodePoint(j);
/*     */         }
/* 694 */         i += k;
/*     */       } else {
/* 696 */         localStringBuilder.append(createString(paramString.charAt(i)));
/* 697 */         i++;
/*     */       }
/*     */     }
/* 700 */     localStringBuilder.append("</string>");
/* 701 */     return localStringBuilder.toString();
/*     */   }
/*     */ 
/*     */   private void outputStatement(Statement paramStatement, Object paramObject, boolean paramBoolean) {
/* 705 */     Object localObject1 = paramStatement.getTarget();
/* 706 */     String str1 = paramStatement.getMethodName();
/*     */ 
/* 708 */     if ((localObject1 == null) || (str1 == null)) {
/* 709 */       throw new NullPointerException((localObject1 == null ? "target" : "methodName") + " should not be null");
/*     */     }
/*     */ 
/* 713 */     Object[] arrayOfObject = paramStatement.getArguments();
/* 714 */     int i = paramStatement.getClass() == Expression.class ? 1 : 0;
/* 715 */     Object localObject2 = i != 0 ? getValue((Expression)paramStatement) : null;
/*     */ 
/* 717 */     String str2 = (i != 0) && (paramBoolean) ? "object" : "void";
/* 718 */     String str3 = "";
/* 719 */     ValueData localValueData = getValueData(localObject2);
/*     */     Object localObject3;
/* 722 */     if (localObject1 != paramObject)
/*     */     {
/* 724 */       if ((localObject1 == Array.class) && (str1.equals("newInstance"))) {
/* 725 */         str2 = "array";
/* 726 */         str3 = str3 + " class=" + quote(((Class)arrayOfObject[0]).getName());
/* 727 */         str3 = str3 + " length=" + quote(arrayOfObject[1].toString());
/* 728 */         arrayOfObject = new Object[0];
/*     */       }
/* 730 */       else if (localObject1.getClass() == Class.class) {
/* 731 */         str3 = str3 + " class=" + quote(((Class)localObject1).getName());
/*     */       }
/*     */       else {
/* 734 */         localValueData.refs = 2;
/* 735 */         if (localValueData.name == null) {
/* 736 */           getValueData(localObject1).refs += 1;
/* 737 */           localObject3 = statementList(localObject1);
/* 738 */           if (!((List)localObject3).contains(paramStatement)) {
/* 739 */             ((List)localObject3).add(paramStatement);
/*     */           }
/* 741 */           outputValue(localObject1, paramObject, false);
/*     */         }
/* 743 */         if (i != 0) {
/* 744 */           outputValue(localObject2, paramObject, paramBoolean);
/*     */         }
/* 746 */         return;
/*     */       }
/*     */     }
/* 748 */     if ((i != 0) && (localValueData.refs > 1)) {
/* 749 */       localObject3 = this.nameGenerator.instanceName(localObject2);
/* 750 */       localValueData.name = ((String)localObject3);
/* 751 */       str3 = str3 + " id=" + quote((String)localObject3);
/*     */     }
/*     */ 
/* 755 */     if (((i == 0) && (str1.equals("set")) && (arrayOfObject.length == 2) && ((arrayOfObject[0] instanceof Integer))) || ((i != 0) && (str1.equals("get")) && (arrayOfObject.length == 1) && ((arrayOfObject[0] instanceof Integer))))
/*     */     {
/* 759 */       str3 = str3 + " index=" + quote(arrayOfObject[0].toString());
/* 760 */       arrayOfObject = new Object[] { arrayOfObject.length == 1 ? new Object[0] : arrayOfObject[1] };
/*     */     }
/* 762 */     else if (((i == 0) && (str1.startsWith("set")) && (arrayOfObject.length == 1)) || ((i != 0) && (str1.startsWith("get")) && (arrayOfObject.length == 0)))
/*     */     {
/* 764 */       if (3 < str1.length()) {
/* 765 */         str3 = str3 + " property=" + quote(Introspector.decapitalize(str1.substring(3)));
/*     */       }
/*     */ 
/*     */     }
/* 769 */     else if ((!str1.equals("new")) && (!str1.equals("newInstance"))) {
/* 770 */       str3 = str3 + " method=" + quote(str1);
/*     */     }
/* 772 */     outputXML(str2, str3, localObject2, arrayOfObject);
/*     */   }
/*     */ 
/*     */   private void outputXML(String paramString1, String paramString2, Object paramObject, Object[] paramArrayOfObject) {
/* 776 */     List localList = statementList(paramObject);
/*     */ 
/* 778 */     if ((paramArrayOfObject.length == 0) && (localList.size() == 0)) {
/* 779 */       writeln("<" + paramString1 + paramString2 + "/>");
/* 780 */       return;
/*     */     }
/*     */ 
/* 783 */     writeln("<" + paramString1 + paramString2 + ">");
/* 784 */     this.indentation += 1;
/*     */ 
/* 786 */     for (int i = 0; i < paramArrayOfObject.length; i++) {
/* 787 */       outputValue(paramArrayOfObject[i], null, true);
/*     */     }
/*     */ 
/* 790 */     while (!localList.isEmpty()) {
/* 791 */       Statement localStatement = (Statement)localList.remove(0);
/* 792 */       outputStatement(localStatement, paramObject, false);
/*     */     }
/*     */ 
/* 795 */     this.indentation -= 1;
/* 796 */     writeln("</" + paramString1 + ">");
/*     */   }
/*     */ 
/*     */   static Class primitiveTypeFor(Class paramClass)
/*     */   {
/* 801 */     if (paramClass == Boolean.class) return Boolean.TYPE;
/* 802 */     if (paramClass == Byte.class) return Byte.TYPE;
/* 803 */     if (paramClass == Character.class) return Character.TYPE;
/* 804 */     if (paramClass == Short.class) return Short.TYPE;
/* 805 */     if (paramClass == Integer.class) return Integer.TYPE;
/* 806 */     if (paramClass == Long.class) return Long.TYPE;
/* 807 */     if (paramClass == Float.class) return Float.TYPE;
/* 808 */     if (paramClass == Double.class) return Double.TYPE;
/* 809 */     if (paramClass == Void.class) return Void.TYPE;
/* 810 */     return null;
/*     */   }
/*     */ 
/*     */   private class ValueData
/*     */   {
/* 223 */     public int refs = 0;
/* 224 */     public boolean marked = false;
/* 225 */     public String name = null;
/* 226 */     public Expression exp = null;
/*     */ 
/*     */     private ValueData()
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.XMLEncoder
 * JD-Core Version:    0.6.2
 */