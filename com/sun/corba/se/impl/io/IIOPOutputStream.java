/*     */ package com.sun.corba.se.impl.io;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.UtilSystemException;
/*     */ import com.sun.corba.se.impl.util.RepositoryId;
/*     */ import com.sun.corba.se.impl.util.Utility;
/*     */ import java.io.Externalizable;
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidClassException;
/*     */ import java.io.NotActiveException;
/*     */ import java.io.NotSerializableException;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Field;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.rmi.Remote;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.Stack;
/*     */ import javax.rmi.CORBA.Util;
/*     */ import org.omg.CORBA.portable.ValueOutputStream;
/*     */ import sun.corba.Bridge;
/*     */ 
/*     */ public class IIOPOutputStream extends OutputStreamHook
/*     */ {
/*  77 */   private UtilSystemException wrapper = UtilSystemException.get("rpc.encoding");
/*     */ 
/*  80 */   private static Bridge bridge = (Bridge)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public java.lang.Object run()
/*     */     {
/*  84 */       return Bridge.get();
/*     */     }
/*     */   });
/*     */   private org.omg.CORBA_2_3.portable.OutputStream orbStream;
/*  91 */   private java.lang.Object currentObject = null;
/*     */ 
/*  93 */   private ObjectStreamClass currentClassDesc = null;
/*     */ 
/*  95 */   private int recursionDepth = 0;
/*     */ 
/*  97 */   private int simpleWriteDepth = 0;
/*     */ 
/*  99 */   private IOException abortIOException = null;
/*     */ 
/* 101 */   private Stack classDescStack = new Stack();
/*     */ 
/* 104 */   private java.lang.Object[] writeObjectArgList = { this };
/*     */ 
/*     */   public IIOPOutputStream()
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void beginOptionalCustomData()
/*     */   {
/* 118 */     if (this.streamFormatVersion == 2)
/*     */     {
/* 120 */       ValueOutputStream localValueOutputStream = (ValueOutputStream)this.orbStream;
/*     */ 
/* 123 */       localValueOutputStream.start_value(this.currentClassDesc.getRMIIIOPOptionalDataRepId());
/*     */     }
/*     */   }
/*     */ 
/*     */   final void setOrbStream(org.omg.CORBA_2_3.portable.OutputStream paramOutputStream) {
/* 128 */     this.orbStream = paramOutputStream;
/*     */   }
/*     */ 
/*     */   final org.omg.CORBA_2_3.portable.OutputStream getOrbStream() {
/* 132 */     return this.orbStream;
/*     */   }
/*     */ 
/*     */   final void increaseRecursionDepth() {
/* 136 */     this.recursionDepth += 1;
/*     */   }
/*     */ 
/*     */   final int decreaseRecursionDepth() {
/* 140 */     return --this.recursionDepth;
/*     */   }
/*     */ 
/*     */   public final void writeObjectOverride(java.lang.Object paramObject)
/*     */     throws IOException
/*     */   {
/* 151 */     this.writeObjectState.writeData(this);
/*     */ 
/* 153 */     Util.writeAbstractObject(this.orbStream, paramObject);
/*     */   }
/*     */ 
/*     */   public final void simpleWriteObject(java.lang.Object paramObject, byte paramByte)
/*     */   {
/* 164 */     byte b = this.streamFormatVersion;
/*     */ 
/* 166 */     this.streamFormatVersion = paramByte;
/*     */ 
/* 168 */     java.lang.Object localObject1 = this.currentObject;
/* 169 */     ObjectStreamClass localObjectStreamClass = this.currentClassDesc;
/* 170 */     this.simpleWriteDepth += 1;
/*     */     try
/*     */     {
/* 174 */       outputObject(paramObject);
/*     */     }
/*     */     catch (IOException localIOException1) {
/* 177 */       if (this.abortIOException == null)
/* 178 */         this.abortIOException = localIOException1;
/*     */     }
/*     */     finally {
/* 181 */       this.streamFormatVersion = b;
/* 182 */       this.simpleWriteDepth -= 1;
/* 183 */       this.currentObject = localObject1;
/* 184 */       this.currentClassDesc = localObjectStreamClass;
/*     */     }
/*     */ 
/* 190 */     IOException localIOException2 = this.abortIOException;
/* 191 */     if (this.simpleWriteDepth == 0)
/* 192 */       this.abortIOException = null;
/* 193 */     if (localIOException2 != null)
/* 194 */       bridge.throwException(localIOException2);
/*     */   }
/*     */ 
/*     */   ObjectStreamField[] getFieldsNoCopy()
/*     */   {
/* 200 */     return this.currentClassDesc.getFieldsNoCopy();
/*     */   }
/*     */ 
/*     */   public final void defaultWriteObjectDelegate()
/*     */   {
/*     */     try
/*     */     {
/* 212 */       if ((this.currentObject == null) || (this.currentClassDesc == null))
/*     */       {
/* 214 */         throw new NotActiveException("defaultWriteObjectDelegate");
/*     */       }
/* 216 */       ObjectStreamField[] arrayOfObjectStreamField = this.currentClassDesc.getFieldsNoCopy();
/*     */ 
/* 218 */       if (arrayOfObjectStreamField.length > 0)
/* 219 */         outputClassFields(this.currentObject, this.currentClassDesc.forClass(), arrayOfObjectStreamField);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 223 */       bridge.throwException(localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public final boolean enableReplaceObjectDelegate(boolean paramBoolean)
/*     */   {
/* 235 */     return false;
/*     */   }
/*     */ 
/*     */   protected final void annotateClass(Class<?> paramClass)
/*     */     throws IOException
/*     */   {
/* 242 */     throw new IOException("Method annotateClass not supported");
/*     */   }
/*     */ 
/*     */   public final void close() throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected final void drain() throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   public final void flush() throws IOException {
/*     */     try {
/* 255 */       this.orbStream.flush();
/*     */     } catch (Error localError) {
/* 257 */       IOException localIOException = new IOException(localError.getMessage());
/* 258 */       localIOException.initCause(localError);
/* 259 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final java.lang.Object replaceObject(java.lang.Object paramObject) throws IOException
/*     */   {
/* 265 */     throw new IOException("Method replaceObject not supported");
/*     */   }
/*     */ 
/*     */   public final void reset()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 282 */       if ((this.currentObject != null) || (this.currentClassDesc != null))
/*     */       {
/* 284 */         throw new IOException("Illegal call to reset");
/*     */       }
/* 286 */       this.abortIOException = null;
/*     */ 
/* 288 */       if (this.classDescStack == null)
/* 289 */         this.classDescStack = new Stack();
/*     */       else
/* 291 */         this.classDescStack.setSize(0);
/*     */     }
/*     */     catch (Error localError) {
/* 294 */       IOException localIOException = new IOException(localError.getMessage());
/* 295 */       localIOException.initCause(localError);
/* 296 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void write(byte[] paramArrayOfByte) throws IOException {
/*     */     try {
/* 302 */       this.writeObjectState.writeData(this);
/*     */ 
/* 304 */       this.orbStream.write_octet_array(paramArrayOfByte, 0, paramArrayOfByte.length);
/*     */     } catch (Error localError) {
/* 306 */       IOException localIOException = new IOException(localError.getMessage());
/* 307 */       localIOException.initCause(localError);
/* 308 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/*     */     try {
/* 314 */       this.writeObjectState.writeData(this);
/*     */ 
/* 316 */       this.orbStream.write_octet_array(paramArrayOfByte, paramInt1, paramInt2);
/*     */     } catch (Error localError) {
/* 318 */       IOException localIOException = new IOException(localError.getMessage());
/* 319 */       localIOException.initCause(localError);
/* 320 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void write(int paramInt) throws IOException {
/*     */     try {
/* 326 */       this.writeObjectState.writeData(this);
/*     */ 
/* 328 */       this.orbStream.write_octet((byte)(paramInt & 0xFF));
/*     */     } catch (Error localError) {
/* 330 */       IOException localIOException = new IOException(localError.getMessage());
/* 331 */       localIOException.initCause(localError);
/* 332 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void writeBoolean(boolean paramBoolean) throws IOException {
/*     */     try {
/* 338 */       this.writeObjectState.writeData(this);
/*     */ 
/* 340 */       this.orbStream.write_boolean(paramBoolean);
/*     */     } catch (Error localError) {
/* 342 */       IOException localIOException = new IOException(localError.getMessage());
/* 343 */       localIOException.initCause(localError);
/* 344 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void writeByte(int paramInt) throws IOException {
/*     */     try {
/* 350 */       this.writeObjectState.writeData(this);
/*     */ 
/* 352 */       this.orbStream.write_octet((byte)paramInt);
/*     */     } catch (Error localError) {
/* 354 */       IOException localIOException = new IOException(localError.getMessage());
/* 355 */       localIOException.initCause(localError);
/* 356 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void writeBytes(String paramString) throws IOException {
/*     */     try {
/* 362 */       this.writeObjectState.writeData(this);
/*     */ 
/* 364 */       byte[] arrayOfByte = paramString.getBytes();
/* 365 */       this.orbStream.write_octet_array(arrayOfByte, 0, arrayOfByte.length);
/*     */     } catch (Error localError) {
/* 367 */       IOException localIOException = new IOException(localError.getMessage());
/* 368 */       localIOException.initCause(localError);
/* 369 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void writeChar(int paramInt) throws IOException {
/*     */     try {
/* 375 */       this.writeObjectState.writeData(this);
/*     */ 
/* 377 */       this.orbStream.write_wchar((char)paramInt);
/*     */     } catch (Error localError) {
/* 379 */       IOException localIOException = new IOException(localError.getMessage());
/* 380 */       localIOException.initCause(localError);
/* 381 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void writeChars(String paramString) throws IOException {
/*     */     try {
/* 387 */       this.writeObjectState.writeData(this);
/*     */ 
/* 389 */       char[] arrayOfChar = paramString.toCharArray();
/* 390 */       this.orbStream.write_wchar_array(arrayOfChar, 0, arrayOfChar.length);
/*     */     } catch (Error localError) {
/* 392 */       IOException localIOException = new IOException(localError.getMessage());
/* 393 */       localIOException.initCause(localError);
/* 394 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void writeDouble(double paramDouble) throws IOException {
/*     */     try {
/* 400 */       this.writeObjectState.writeData(this);
/*     */ 
/* 402 */       this.orbStream.write_double(paramDouble);
/*     */     } catch (Error localError) {
/* 404 */       IOException localIOException = new IOException(localError.getMessage());
/* 405 */       localIOException.initCause(localError);
/* 406 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void writeFloat(float paramFloat) throws IOException {
/*     */     try {
/* 412 */       this.writeObjectState.writeData(this);
/*     */ 
/* 414 */       this.orbStream.write_float(paramFloat);
/*     */     } catch (Error localError) {
/* 416 */       IOException localIOException = new IOException(localError.getMessage());
/* 417 */       localIOException.initCause(localError);
/* 418 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void writeInt(int paramInt) throws IOException {
/*     */     try {
/* 424 */       this.writeObjectState.writeData(this);
/*     */ 
/* 426 */       this.orbStream.write_long(paramInt);
/*     */     } catch (Error localError) {
/* 428 */       IOException localIOException = new IOException(localError.getMessage());
/* 429 */       localIOException.initCause(localError);
/* 430 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void writeLong(long paramLong) throws IOException {
/*     */     try {
/* 436 */       this.writeObjectState.writeData(this);
/*     */ 
/* 438 */       this.orbStream.write_longlong(paramLong);
/*     */     } catch (Error localError) {
/* 440 */       IOException localIOException = new IOException(localError.getMessage());
/* 441 */       localIOException.initCause(localError);
/* 442 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void writeShort(int paramInt) throws IOException {
/*     */     try {
/* 448 */       this.writeObjectState.writeData(this);
/*     */ 
/* 450 */       this.orbStream.write_short((short)paramInt);
/*     */     } catch (Error localError) {
/* 452 */       IOException localIOException = new IOException(localError.getMessage());
/* 453 */       localIOException.initCause(localError);
/* 454 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   protected final void writeStreamHeader()
/*     */     throws IOException
/*     */   {
/*     */   }
/*     */ 
/*     */   protected void internalWriteUTF(org.omg.CORBA.portable.OutputStream paramOutputStream, String paramString)
/*     */   {
/* 471 */     paramOutputStream.write_wstring(paramString);
/*     */   }
/*     */ 
/*     */   public final void writeUTF(String paramString) throws IOException {
/*     */     try {
/* 476 */       this.writeObjectState.writeData(this);
/*     */ 
/* 478 */       internalWriteUTF(this.orbStream, paramString);
/*     */     } catch (Error localError) {
/* 480 */       IOException localIOException = new IOException(localError.getMessage());
/* 481 */       localIOException.initCause(localError);
/* 482 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean checkSpecialClasses(java.lang.Object paramObject)
/*     */     throws IOException
/*     */   {
/* 500 */     if ((paramObject instanceof ObjectStreamClass))
/*     */     {
/* 502 */       throw new IOException("Serialization of ObjectStreamClass not supported");
/*     */     }
/*     */ 
/* 505 */     return false;
/*     */   }
/*     */ 
/*     */   private boolean checkSubstitutableSpecialClasses(java.lang.Object paramObject)
/*     */     throws IOException
/*     */   {
/* 515 */     if ((paramObject instanceof String)) {
/* 516 */       this.orbStream.write_value((Serializable)paramObject);
/* 517 */       return true;
/*     */     }
/*     */ 
/* 525 */     return false;
/*     */   }
/*     */ 
/*     */   private void outputObject(java.lang.Object paramObject)
/*     */     throws IOException
/*     */   {
/* 533 */     this.currentObject = paramObject;
/* 534 */     Class localClass = paramObject.getClass();
/*     */ 
/* 539 */     this.currentClassDesc = ObjectStreamClass.lookup(localClass);
/* 540 */     if (this.currentClassDesc == null)
/*     */     {
/* 542 */       throw new NotSerializableException(localClass.getName());
/*     */     }
/*     */ 
/* 549 */     if (this.currentClassDesc.isExternalizable())
/*     */     {
/* 551 */       this.orbStream.write_octet(this.streamFormatVersion);
/*     */ 
/* 553 */       Externalizable localExternalizable = (Externalizable)paramObject;
/* 554 */       localExternalizable.writeExternal(this);
/*     */     }
/*     */     else
/*     */     {
/* 562 */       int i = this.classDescStack.size();
/*     */       try
/*     */       {
/*     */         ObjectStreamClass localObjectStreamClass;
/* 565 */         while ((localObjectStreamClass = this.currentClassDesc.getSuperclass()) != null) {
/* 566 */           this.classDescStack.push(this.currentClassDesc);
/* 567 */           this.currentClassDesc = localObjectStreamClass;
/*     */         }
/*     */ 
/*     */         do
/*     */         {
/* 579 */           OutputStreamHook.WriteObjectState localWriteObjectState = this.writeObjectState;
/*     */           try
/*     */           {
/* 583 */             setState(NOT_IN_WRITE_OBJECT);
/*     */ 
/* 585 */             if (this.currentClassDesc.hasWriteObject())
/* 586 */               invokeObjectWriter(this.currentClassDesc, paramObject);
/*     */             else
/* 588 */               defaultWriteObjectDelegate();
/*     */           }
/*     */           finally {
/* 591 */             setState(localWriteObjectState);
/*     */           }
/*     */ 
/* 594 */           if (this.classDescStack.size() <= i) break;  } while ((this.currentClassDesc = (ObjectStreamClass)this.classDescStack.pop()) != null);
/*     */       }
/*     */       finally {
/* 597 */         this.classDescStack.setSize(i);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void invokeObjectWriter(ObjectStreamClass paramObjectStreamClass, java.lang.Object paramObject)
/*     */     throws IOException
/*     */   {
/* 610 */     Class localClass = paramObjectStreamClass.forClass();
/*     */     try
/*     */     {
/* 615 */       this.orbStream.write_octet(this.streamFormatVersion);
/*     */ 
/* 617 */       this.writeObjectState.enterWriteObject(this);
/*     */ 
/* 620 */       paramObjectStreamClass.writeObjectMethod.invoke(paramObject, this.writeObjectArgList);
/*     */ 
/* 622 */       this.writeObjectState.exitWriteObject(this);
/*     */     }
/*     */     catch (InvocationTargetException localInvocationTargetException) {
/* 625 */       Throwable localThrowable = localInvocationTargetException.getTargetException();
/* 626 */       if ((localThrowable instanceof IOException))
/* 627 */         throw ((IOException)localThrowable);
/* 628 */       if ((localThrowable instanceof RuntimeException))
/* 629 */         throw ((RuntimeException)localThrowable);
/* 630 */       if ((localThrowable instanceof Error)) {
/* 631 */         throw ((Error)localThrowable);
/*     */       }
/*     */ 
/* 634 */       throw new Error("invokeObjectWriter internal error", localInvocationTargetException);
/*     */     }
/*     */     catch (IllegalAccessException localIllegalAccessException) {
/*     */     }
/*     */   }
/*     */ 
/*     */   void writeField(ObjectStreamField paramObjectStreamField, java.lang.Object paramObject) throws IOException {
/* 641 */     switch (paramObjectStreamField.getTypeCode()) {
/*     */     case 'B':
/* 643 */       if (paramObject == null)
/* 644 */         this.orbStream.write_octet((byte)0);
/*     */       else
/* 646 */         this.orbStream.write_octet(((Byte)paramObject).byteValue());
/* 647 */       break;
/*     */     case 'C':
/* 649 */       if (paramObject == null)
/* 650 */         this.orbStream.write_wchar('\000');
/*     */       else
/* 652 */         this.orbStream.write_wchar(((Character)paramObject).charValue());
/* 653 */       break;
/*     */     case 'F':
/* 655 */       if (paramObject == null)
/* 656 */         this.orbStream.write_float(0.0F);
/*     */       else
/* 658 */         this.orbStream.write_float(((Float)paramObject).floatValue());
/* 659 */       break;
/*     */     case 'D':
/* 661 */       if (paramObject == null)
/* 662 */         this.orbStream.write_double(0.0D);
/*     */       else
/* 664 */         this.orbStream.write_double(((Double)paramObject).doubleValue());
/* 665 */       break;
/*     */     case 'I':
/* 667 */       if (paramObject == null)
/* 668 */         this.orbStream.write_long(0);
/*     */       else
/* 670 */         this.orbStream.write_long(((Integer)paramObject).intValue());
/* 671 */       break;
/*     */     case 'J':
/* 673 */       if (paramObject == null)
/* 674 */         this.orbStream.write_longlong(0L);
/*     */       else
/* 676 */         this.orbStream.write_longlong(((Long)paramObject).longValue());
/* 677 */       break;
/*     */     case 'S':
/* 679 */       if (paramObject == null)
/* 680 */         this.orbStream.write_short((short)0);
/*     */       else
/* 682 */         this.orbStream.write_short(((Short)paramObject).shortValue());
/* 683 */       break;
/*     */     case 'Z':
/* 685 */       if (paramObject == null)
/* 686 */         this.orbStream.write_boolean(false);
/*     */       else
/* 688 */         this.orbStream.write_boolean(((Boolean)paramObject).booleanValue());
/* 689 */       break;
/*     */     case 'L':
/*     */     case '[':
/* 693 */       writeObjectField(paramObjectStreamField, paramObject);
/* 694 */       break;
/*     */     case 'E':
/*     */     case 'G':
/*     */     case 'H':
/*     */     case 'K':
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
/*     */     case 'Y':
/*     */     default:
/* 697 */       throw new InvalidClassException(this.currentClassDesc.getName());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeObjectField(ObjectStreamField paramObjectStreamField, java.lang.Object paramObject)
/*     */     throws IOException
/*     */   {
/* 704 */     if (ObjectStreamClassCorbaExt.isAny(paramObjectStreamField.getTypeString())) {
/* 705 */       Util.writeAny(this.orbStream, paramObject);
/*     */     }
/*     */     else {
/* 708 */       Class localClass = paramObjectStreamField.getType();
/* 709 */       int i = 2;
/*     */ 
/* 711 */       if (localClass.isInterface()) {
/* 712 */         String str = localClass.getName();
/*     */ 
/* 714 */         if (Remote.class.isAssignableFrom(localClass))
/*     */         {
/* 718 */           i = 0;
/*     */         }
/* 721 */         else if (org.omg.CORBA.Object.class.isAssignableFrom(localClass))
/*     */         {
/* 724 */           i = 0;
/*     */         }
/* 726 */         else if (RepositoryId.isAbstractBase(localClass))
/*     */         {
/* 728 */           i = 1;
/* 729 */         } else if (ObjectStreamClassCorbaExt.isAbstractInterface(localClass)) {
/* 730 */           i = 1;
/*     */         }
/*     */       }
/*     */ 
/* 734 */       switch (i) {
/*     */       case 0:
/* 736 */         Util.writeRemoteObject(this.orbStream, paramObject);
/* 737 */         break;
/*     */       case 1:
/* 739 */         Util.writeAbstractObject(this.orbStream, paramObject);
/* 740 */         break;
/*     */       case 2:
/*     */         try {
/* 743 */           this.orbStream.write_value((Serializable)paramObject, localClass);
/*     */         }
/*     */         catch (ClassCastException localClassCastException) {
/* 746 */           if ((paramObject instanceof Serializable)) {
/* 747 */             throw localClassCastException;
/*     */           }
/* 749 */           Utility.throwNotSerializableForCorba(paramObject.getClass().getName());
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void outputClassFields(java.lang.Object paramObject, Class paramClass, ObjectStreamField[] paramArrayOfObjectStreamField)
/*     */     throws IOException, InvalidClassException
/*     */   {
/* 762 */     for (int i = 0; i < paramArrayOfObjectStreamField.length; i++) {
/* 763 */       if (paramArrayOfObjectStreamField[i].getField() == null)
/*     */       {
/* 765 */         throw new InvalidClassException(paramClass.getName(), "Nonexistent field " + paramArrayOfObjectStreamField[i].getName());
/*     */       }
/*     */       try
/*     */       {
/* 769 */         switch (paramArrayOfObjectStreamField[i].getTypeCode()) {
/*     */         case 'B':
/* 771 */           byte b = paramArrayOfObjectStreamField[i].getField().getByte(paramObject);
/* 772 */           this.orbStream.write_octet(b);
/* 773 */           break;
/*     */         case 'C':
/* 775 */           char c = paramArrayOfObjectStreamField[i].getField().getChar(paramObject);
/* 776 */           this.orbStream.write_wchar(c);
/* 777 */           break;
/*     */         case 'F':
/* 779 */           float f = paramArrayOfObjectStreamField[i].getField().getFloat(paramObject);
/* 780 */           this.orbStream.write_float(f);
/* 781 */           break;
/*     */         case 'D':
/* 783 */           double d = paramArrayOfObjectStreamField[i].getField().getDouble(paramObject);
/* 784 */           this.orbStream.write_double(d);
/* 785 */           break;
/*     */         case 'I':
/* 787 */           int j = paramArrayOfObjectStreamField[i].getField().getInt(paramObject);
/* 788 */           this.orbStream.write_long(j);
/* 789 */           break;
/*     */         case 'J':
/* 791 */           long l = paramArrayOfObjectStreamField[i].getField().getLong(paramObject);
/* 792 */           this.orbStream.write_longlong(l);
/* 793 */           break;
/*     */         case 'S':
/* 795 */           short s = paramArrayOfObjectStreamField[i].getField().getShort(paramObject);
/* 796 */           this.orbStream.write_short(s);
/* 797 */           break;
/*     */         case 'Z':
/* 799 */           boolean bool = paramArrayOfObjectStreamField[i].getField().getBoolean(paramObject);
/* 800 */           this.orbStream.write_boolean(bool);
/* 801 */           break;
/*     */         case 'L':
/*     */         case '[':
/* 804 */           java.lang.Object localObject = paramArrayOfObjectStreamField[i].getField().get(paramObject);
/* 805 */           writeObjectField(paramArrayOfObjectStreamField[i], localObject);
/* 806 */           break;
/*     */         case 'E':
/*     */         case 'G':
/*     */         case 'H':
/*     */         case 'K':
/*     */         case 'M':
/*     */         case 'N':
/*     */         case 'O':
/*     */         case 'P':
/*     */         case 'Q':
/*     */         case 'R':
/*     */         case 'T':
/*     */         case 'U':
/*     */         case 'V':
/*     */         case 'W':
/*     */         case 'X':
/*     */         case 'Y':
/*     */         default:
/* 809 */           throw new InvalidClassException(paramClass.getName());
/*     */         }
/*     */       } catch (IllegalAccessException localIllegalAccessException) {
/* 812 */         throw this.wrapper.illegalFieldAccess(localIllegalAccessException, paramArrayOfObjectStreamField[i].getName());
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.io.IIOPOutputStream
 * JD-Core Version:    0.6.2
 */