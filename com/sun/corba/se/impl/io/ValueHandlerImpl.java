/*     */ package com.sun.corba.se.impl.io;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*     */ import com.sun.corba.se.impl.logging.UtilSystemException;
/*     */ import com.sun.corba.se.impl.util.RepositoryId;
/*     */ import com.sun.corba.se.impl.util.RepositoryIdCache;
/*     */ import com.sun.corba.se.impl.util.Utility;
/*     */ import com.sun.org.omg.SendingContext.CodeBase;
/*     */ import com.sun.org.omg.SendingContext.CodeBaseHelper;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.lang.reflect.Array;
/*     */ import java.rmi.Remote;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.PrivilegedActionException;
/*     */ import java.security.PrivilegedExceptionAction;
/*     */ import java.util.Hashtable;
/*     */ import javax.rmi.CORBA.Util;
/*     */ import javax.rmi.CORBA.ValueHandlerMultiFormat;
/*     */ import org.omg.CORBA.TCKind;
/*     */ import org.omg.CORBA.portable.IndirectionException;
/*     */ import org.omg.CORBA.portable.ValueOutputStream;
/*     */ import org.omg.SendingContext.RunTime;
/*     */ 
/*     */ public final class ValueHandlerImpl
/*     */   implements ValueHandlerMultiFormat
/*     */ {
/*     */   public static final String FORMAT_VERSION_PROPERTY = "com.sun.CORBA.MaxStreamFormatVersion";
/*     */   private static final byte MAX_SUPPORTED_FORMAT_VERSION = 2;
/*     */   private static final byte STREAM_FORMAT_VERSION_1 = 1;
/*  70 */   private static final byte MAX_STREAM_FORMAT_VERSION = getMaxStreamFormatVersion();
/*     */   public static final short kRemoteType = 0;
/*     */   public static final short kAbstractType = 1;
/*     */   public static final short kValueType = 2;
/* 119 */   private Hashtable inputStreamPairs = null;
/* 120 */   private Hashtable outputStreamPairs = null;
/* 121 */   private CodeBase codeBase = null;
/* 122 */   private boolean useHashtables = true;
/* 123 */   private boolean isInputStream = true;
/* 124 */   private IIOPOutputStream outputStreamBridge = null;
/* 125 */   private IIOPInputStream inputStreamBridge = null;
/* 126 */   private OMGSystemException omgWrapper = OMGSystemException.get("rpc.encoding");
/*     */ 
/* 128 */   private UtilSystemException utilWrapper = UtilSystemException.get("rpc.encoding");
/*     */ 
/*     */   private static byte getMaxStreamFormatVersion()
/*     */   {
/*     */     try
/*     */     {
/*  81 */       String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */       {
/*     */         public java.lang.Object run() {
/*  84 */           return System.getProperty("com.sun.CORBA.MaxStreamFormatVersion");
/*     */         }
/*     */       });
/*  89 */       if (str == null) {
/*  90 */         return 2;
/*     */       }
/*  92 */       byte b = Byte.parseByte(str);
/*     */ 
/*  96 */       if ((b < 1) || (b > 2))
/*     */       {
/*  98 */         throw new ExceptionInInitializerError("Invalid stream format version: " + b + ".  Valid range is 1 through " + 2);
/*     */       }
/*     */ 
/* 103 */       return b;
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/* 109 */       ExceptionInInitializerError localExceptionInInitializerError = new ExceptionInInitializerError(localException);
/* 110 */       localExceptionInInitializerError.initCause(localException);
/* 111 */       throw localExceptionInInitializerError;
/*     */     }
/*     */   }
/*     */ 
/*     */   public byte getMaximumStreamFormatVersion()
/*     */   {
/* 133 */     return MAX_STREAM_FORMAT_VERSION;
/*     */   }
/*     */ 
/*     */   public void writeValue(org.omg.CORBA.portable.OutputStream paramOutputStream, Serializable paramSerializable, byte paramByte)
/*     */   {
/* 141 */     if (paramByte == 2) {
/* 142 */       if (!(paramOutputStream instanceof ValueOutputStream))
/* 143 */         throw this.omgWrapper.notAValueoutputstream();
/*     */     }
/* 145 */     else if (paramByte != 1) {
/* 146 */       throw this.omgWrapper.invalidStreamFormatVersion(new Integer(paramByte));
/*     */     }
/*     */ 
/* 150 */     writeValueWithVersion(paramOutputStream, paramSerializable, paramByte);
/*     */   }
/*     */   private ValueHandlerImpl() {
/*     */   }
/*     */ 
/*     */   private ValueHandlerImpl(boolean paramBoolean) {
/* 156 */     this();
/* 157 */     this.useHashtables = false;
/* 158 */     this.isInputStream = paramBoolean;
/*     */   }
/*     */ 
/*     */   static ValueHandlerImpl getInstance() {
/* 162 */     return new ValueHandlerImpl();
/*     */   }
/*     */ 
/*     */   static ValueHandlerImpl getInstance(boolean paramBoolean) {
/* 166 */     return new ValueHandlerImpl(paramBoolean);
/*     */   }
/*     */ 
/*     */   public void writeValue(org.omg.CORBA.portable.OutputStream paramOutputStream, Serializable paramSerializable)
/*     */   {
/* 176 */     writeValueWithVersion(paramOutputStream, paramSerializable, (byte)1);
/*     */   }
/*     */ 
/*     */   private void writeValueWithVersion(org.omg.CORBA.portable.OutputStream paramOutputStream, Serializable paramSerializable, byte paramByte)
/*     */   {
/* 183 */     org.omg.CORBA_2_3.portable.OutputStream localOutputStream = (org.omg.CORBA_2_3.portable.OutputStream)paramOutputStream;
/*     */ 
/* 186 */     if (!this.useHashtables) {
/* 187 */       if (this.outputStreamBridge == null) {
/* 188 */         this.outputStreamBridge = createOutputStream();
/* 189 */         this.outputStreamBridge.setOrbStream(localOutputStream);
/*     */       }
/*     */       try
/*     */       {
/* 193 */         this.outputStreamBridge.increaseRecursionDepth();
/* 194 */         writeValueInternal(this.outputStreamBridge, localOutputStream, paramSerializable, paramByte);
/*     */       } finally {
/* 196 */         this.outputStreamBridge.decreaseRecursionDepth();
/*     */       }
/*     */ 
/* 199 */       return;
/*     */     }
/*     */ 
/* 202 */     IIOPOutputStream localIIOPOutputStream = null;
/*     */ 
/* 204 */     if (this.outputStreamPairs == null) {
/* 205 */       this.outputStreamPairs = new Hashtable();
/*     */     }
/* 207 */     localIIOPOutputStream = (IIOPOutputStream)this.outputStreamPairs.get(paramOutputStream);
/*     */ 
/* 209 */     if (localIIOPOutputStream == null) {
/* 210 */       localIIOPOutputStream = createOutputStream();
/* 211 */       localIIOPOutputStream.setOrbStream(localOutputStream);
/* 212 */       this.outputStreamPairs.put(paramOutputStream, localIIOPOutputStream);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 217 */       localIIOPOutputStream.increaseRecursionDepth();
/* 218 */       writeValueInternal(localIIOPOutputStream, localOutputStream, paramSerializable, paramByte);
/*     */     } finally {
/* 220 */       if (localIIOPOutputStream.decreaseRecursionDepth() == 0)
/* 221 */         this.outputStreamPairs.remove(paramOutputStream);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void writeValueInternal(IIOPOutputStream paramIIOPOutputStream, org.omg.CORBA_2_3.portable.OutputStream paramOutputStream, Serializable paramSerializable, byte paramByte)
/*     */   {
/* 231 */     Class localClass = paramSerializable.getClass();
/*     */ 
/* 233 */     if (localClass.isArray())
/* 234 */       write_Array(paramOutputStream, paramSerializable, localClass.getComponentType());
/*     */     else
/* 236 */       paramIIOPOutputStream.simpleWriteObject(paramSerializable, paramByte);
/*     */   }
/*     */ 
/*     */   public Serializable readValue(org.omg.CORBA.portable.InputStream paramInputStream, int paramInt, Class paramClass, String paramString, RunTime paramRunTime)
/*     */   {
/* 253 */     CodeBase localCodeBase = CodeBaseHelper.narrow(paramRunTime);
/*     */ 
/* 255 */     org.omg.CORBA_2_3.portable.InputStream localInputStream = (org.omg.CORBA_2_3.portable.InputStream)paramInputStream;
/*     */ 
/* 258 */     if (!this.useHashtables) {
/* 259 */       if (this.inputStreamBridge == null) {
/* 260 */         this.inputStreamBridge = createInputStream();
/* 261 */         this.inputStreamBridge.setOrbStream(localInputStream);
/* 262 */         this.inputStreamBridge.setSender(localCodeBase);
/*     */ 
/* 264 */         this.inputStreamBridge.setValueHandler(this);
/*     */       }
/*     */ 
/* 267 */       localObject1 = null;
/*     */       try
/*     */       {
/* 271 */         this.inputStreamBridge.increaseRecursionDepth();
/* 272 */         localObject1 = readValueInternal(this.inputStreamBridge, localInputStream, paramInt, paramClass, paramString, localCodeBase);
/*     */       }
/*     */       finally
/*     */       {
/* 276 */         if (this.inputStreamBridge.decreaseRecursionDepth() != 0);
/*     */       }
/*     */ 
/* 283 */       return localObject1;
/*     */     }
/*     */ 
/* 286 */     java.lang.Object localObject1 = null;
/* 287 */     if (this.inputStreamPairs == null) {
/* 288 */       this.inputStreamPairs = new Hashtable();
/*     */     }
/* 290 */     localObject1 = (IIOPInputStream)this.inputStreamPairs.get(paramInputStream);
/*     */ 
/* 292 */     if (localObject1 == null)
/*     */     {
/* 294 */       localObject1 = createInputStream();
/* 295 */       ((IIOPInputStream)localObject1).setOrbStream(localInputStream);
/* 296 */       ((IIOPInputStream)localObject1).setSender(localCodeBase);
/*     */ 
/* 298 */       ((IIOPInputStream)localObject1).setValueHandler(this);
/* 299 */       this.inputStreamPairs.put(paramInputStream, localObject1);
/*     */     }
/*     */ 
/* 302 */     Serializable localSerializable = null;
/*     */     try
/*     */     {
/* 306 */       ((IIOPInputStream)localObject1).increaseRecursionDepth();
/* 307 */       localSerializable = readValueInternal((IIOPInputStream)localObject1, localInputStream, paramInt, paramClass, paramString, localCodeBase);
/*     */     }
/*     */     finally
/*     */     {
/* 311 */       if (((IIOPInputStream)localObject1).decreaseRecursionDepth() == 0) {
/* 312 */         this.inputStreamPairs.remove(paramInputStream);
/*     */       }
/*     */     }
/*     */ 
/* 316 */     return localSerializable;
/*     */   }
/*     */ 
/*     */   private Serializable readValueInternal(IIOPInputStream paramIIOPInputStream, org.omg.CORBA_2_3.portable.InputStream paramInputStream, int paramInt, Class paramClass, String paramString, CodeBase paramCodeBase)
/*     */   {
/* 326 */     Serializable localSerializable = null;
/*     */ 
/* 328 */     if (paramClass == null)
/*     */     {
/* 330 */       if (isArray(paramString))
/* 331 */         read_Array(paramIIOPInputStream, paramInputStream, null, paramCodeBase, paramInt);
/*     */       else {
/* 333 */         paramIIOPInputStream.simpleSkipObject(paramString, paramCodeBase);
/*     */       }
/* 335 */       return localSerializable;
/*     */     }
/*     */ 
/* 338 */     if (paramClass.isArray())
/* 339 */       localSerializable = (Serializable)read_Array(paramIIOPInputStream, paramInputStream, paramClass, paramCodeBase, paramInt);
/*     */     else {
/* 341 */       localSerializable = (Serializable)paramIIOPInputStream.simpleReadObject(paramClass, paramString, paramCodeBase, paramInt);
/*     */     }
/*     */ 
/* 344 */     return localSerializable;
/*     */   }
/*     */ 
/*     */   public String getRMIRepositoryID(Class paramClass)
/*     */   {
/* 353 */     return RepositoryId.createForJavaType(paramClass);
/*     */   }
/*     */ 
/*     */   public boolean isCustomMarshaled(Class paramClass)
/*     */   {
/* 364 */     return ObjectStreamClass.lookup(paramClass).isCustomMarshaled();
/*     */   }
/*     */ 
/*     */   public RunTime getRunTimeCodeBase()
/*     */   {
/* 375 */     if (this.codeBase != null) {
/* 376 */       return this.codeBase;
/*     */     }
/* 378 */     this.codeBase = new FVDCodeBaseImpl();
/*     */ 
/* 383 */     FVDCodeBaseImpl localFVDCodeBaseImpl = (FVDCodeBaseImpl)this.codeBase;
/* 384 */     localFVDCodeBaseImpl.setValueHandler(this);
/* 385 */     return this.codeBase;
/*     */   }
/*     */ 
/*     */   public boolean useFullValueDescription(Class paramClass, String paramString)
/*     */     throws IOException
/*     */   {
/* 402 */     return RepositoryId.useFullValueDescription(paramClass, paramString);
/*     */   }
/*     */ 
/*     */   public String getClassName(String paramString)
/*     */   {
/* 407 */     RepositoryId localRepositoryId = RepositoryId.cache.getId(paramString);
/* 408 */     return localRepositoryId.getClassName();
/*     */   }
/*     */ 
/*     */   public Class getClassFromType(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/* 414 */     RepositoryId localRepositoryId = RepositoryId.cache.getId(paramString);
/* 415 */     return localRepositoryId.getClassFromType();
/*     */   }
/*     */ 
/*     */   public Class getAnyClassFromType(String paramString)
/*     */     throws ClassNotFoundException
/*     */   {
/* 421 */     RepositoryId localRepositoryId = RepositoryId.cache.getId(paramString);
/* 422 */     return localRepositoryId.getAnyClassFromType();
/*     */   }
/*     */ 
/*     */   public String createForAnyType(Class paramClass)
/*     */   {
/* 427 */     return RepositoryId.createForAnyType(paramClass);
/*     */   }
/*     */ 
/*     */   public String getDefinedInId(String paramString)
/*     */   {
/* 432 */     RepositoryId localRepositoryId = RepositoryId.cache.getId(paramString);
/* 433 */     return localRepositoryId.getDefinedInId();
/*     */   }
/*     */ 
/*     */   public String getUnqualifiedName(String paramString)
/*     */   {
/* 438 */     RepositoryId localRepositoryId = RepositoryId.cache.getId(paramString);
/* 439 */     return localRepositoryId.getUnqualifiedName();
/*     */   }
/*     */ 
/*     */   public String getSerialVersionUID(String paramString)
/*     */   {
/* 444 */     RepositoryId localRepositoryId = RepositoryId.cache.getId(paramString);
/* 445 */     return localRepositoryId.getSerialVersionUID();
/*     */   }
/*     */ 
/*     */   public boolean isAbstractBase(Class paramClass)
/*     */   {
/* 451 */     return RepositoryId.isAbstractBase(paramClass);
/*     */   }
/*     */ 
/*     */   public boolean isSequence(String paramString)
/*     */   {
/* 456 */     RepositoryId localRepositoryId = RepositoryId.cache.getId(paramString);
/* 457 */     return localRepositoryId.isSequence();
/*     */   }
/*     */ 
/*     */   public Serializable writeReplace(Serializable paramSerializable)
/*     */   {
/* 466 */     return ObjectStreamClass.lookup(paramSerializable.getClass()).writeReplace(paramSerializable);
/*     */   }
/*     */ 
/*     */   private void writeCharArray(org.omg.CORBA_2_3.portable.OutputStream paramOutputStream, char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 474 */     paramOutputStream.write_wchar_array(paramArrayOfChar, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   private void write_Array(org.omg.CORBA_2_3.portable.OutputStream paramOutputStream, Serializable paramSerializable, Class paramClass)
/*     */   {
/*     */     java.lang.Object localObject;
/*     */     int j;
/* 481 */     if (paramClass.isPrimitive()) {
/* 482 */       if (paramClass == Integer.TYPE) {
/* 483 */         localObject = (int[])paramSerializable;
/* 484 */         j = localObject.length;
/* 485 */         paramOutputStream.write_ulong(j);
/* 486 */         paramOutputStream.write_long_array((int[])localObject, 0, j);
/* 487 */       } else if (paramClass == Byte.TYPE) {
/* 488 */         localObject = (byte[])paramSerializable;
/* 489 */         j = localObject.length;
/* 490 */         paramOutputStream.write_ulong(j);
/* 491 */         paramOutputStream.write_octet_array((byte[])localObject, 0, j);
/* 492 */       } else if (paramClass == Long.TYPE) {
/* 493 */         localObject = (long[])paramSerializable;
/* 494 */         j = localObject.length;
/* 495 */         paramOutputStream.write_ulong(j);
/* 496 */         paramOutputStream.write_longlong_array((long[])localObject, 0, j);
/* 497 */       } else if (paramClass == Float.TYPE) {
/* 498 */         localObject = (float[])paramSerializable;
/* 499 */         j = localObject.length;
/* 500 */         paramOutputStream.write_ulong(j);
/* 501 */         paramOutputStream.write_float_array((float[])localObject, 0, j);
/* 502 */       } else if (paramClass == Double.TYPE) {
/* 503 */         localObject = (double[])paramSerializable;
/* 504 */         j = localObject.length;
/* 505 */         paramOutputStream.write_ulong(j);
/* 506 */         paramOutputStream.write_double_array((double[])localObject, 0, j);
/* 507 */       } else if (paramClass == Short.TYPE) {
/* 508 */         localObject = (short[])paramSerializable;
/* 509 */         j = localObject.length;
/* 510 */         paramOutputStream.write_ulong(j);
/* 511 */         paramOutputStream.write_short_array((short[])localObject, 0, j);
/* 512 */       } else if (paramClass == Character.TYPE) {
/* 513 */         localObject = (char[])paramSerializable;
/* 514 */         j = localObject.length;
/* 515 */         paramOutputStream.write_ulong(j);
/* 516 */         writeCharArray(paramOutputStream, (char[])localObject, 0, j);
/* 517 */       } else if (paramClass == Boolean.TYPE) {
/* 518 */         localObject = (boolean[])paramSerializable;
/* 519 */         j = localObject.length;
/* 520 */         paramOutputStream.write_ulong(j);
/* 521 */         paramOutputStream.write_boolean_array((boolean[])localObject, 0, j);
/*     */       }
/*     */       else {
/* 524 */         throw new Error("Invalid primitive type : " + paramSerializable.getClass().getName());
/*     */       }
/*     */     }
/*     */     else
/*     */     {
/*     */       int i;
/* 527 */       if (paramClass == java.lang.Object.class) {
/* 528 */         localObject = (java.lang.Object[])paramSerializable;
/* 529 */         j = localObject.length;
/* 530 */         paramOutputStream.write_ulong(j);
/* 531 */         for (i = 0; i < j; i++)
/* 532 */           Util.writeAny(paramOutputStream, localObject[i]);
/*     */       }
/*     */       else {
/* 535 */         localObject = (java.lang.Object[])paramSerializable;
/* 536 */         j = localObject.length;
/* 537 */         paramOutputStream.write_ulong(j);
/* 538 */         int k = 2;
/*     */ 
/* 540 */         if (paramClass.isInterface()) {
/* 541 */           String str = paramClass.getName();
/*     */ 
/* 543 */           if (Remote.class.isAssignableFrom(paramClass))
/*     */           {
/* 545 */             k = 0;
/* 546 */           } else if (org.omg.CORBA.Object.class.isAssignableFrom(paramClass))
/*     */           {
/* 548 */             k = 0;
/* 549 */           } else if (RepositoryId.isAbstractBase(paramClass))
/*     */           {
/* 551 */             k = 1;
/* 552 */           } else if (ObjectStreamClassCorbaExt.isAbstractInterface(paramClass)) {
/* 553 */             k = 1;
/*     */           }
/*     */         }
/*     */ 
/* 557 */         for (i = 0; i < j; i++)
/* 558 */           switch (k) {
/*     */           case 0:
/* 560 */             Util.writeRemoteObject(paramOutputStream, localObject[i]);
/* 561 */             break;
/*     */           case 1:
/* 563 */             Util.writeAbstractObject(paramOutputStream, localObject[i]);
/* 564 */             break;
/*     */           case 2:
/*     */             try {
/* 567 */               paramOutputStream.write_value((Serializable)localObject[i]);
/*     */             } catch (ClassCastException localClassCastException) {
/* 569 */               if ((localObject[i] instanceof Serializable)) {
/* 570 */                 throw localClassCastException;
/*     */               }
/* 572 */               Utility.throwNotSerializableForCorba(localObject[i].getClass().getName());
/*     */             }
/*     */           }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void readCharArray(org.omg.CORBA_2_3.portable.InputStream paramInputStream, char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 587 */     paramInputStream.read_wchar_array(paramArrayOfChar, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   private java.lang.Object read_Array(IIOPInputStream paramIIOPInputStream, org.omg.CORBA_2_3.portable.InputStream paramInputStream, Class paramClass, CodeBase paramCodeBase, int paramInt)
/*     */   {
/*     */     try
/*     */     {
/* 598 */       int i = paramInputStream.read_ulong();
/*     */ 
/* 601 */       if (paramClass == null) {
/* 602 */         for (j = 0; j < i; j++) {
/* 603 */           paramInputStream.read_value();
/*     */         }
/* 605 */         return null;
/*     */       }
/*     */ 
/* 608 */       java.lang.Object localObject1 = paramClass.getComponentType();
/* 609 */       java.lang.Object localObject2 = localObject1;
/*     */       java.lang.Object localObject4;
/* 612 */       if (((Class)localObject1).isPrimitive()) {
/* 613 */         if (localObject1 == Integer.TYPE) {
/* 614 */           localObject3 = new int[i];
/* 615 */           paramInputStream.read_long_array((int[])localObject3, 0, i);
/* 616 */           return (Serializable)localObject3;
/* 617 */         }if (localObject1 == Byte.TYPE) {
/* 618 */           localObject3 = new byte[i];
/* 619 */           paramInputStream.read_octet_array((byte[])localObject3, 0, i);
/* 620 */           return (Serializable)localObject3;
/* 621 */         }if (localObject1 == Long.TYPE) {
/* 622 */           localObject3 = new long[i];
/* 623 */           paramInputStream.read_longlong_array((long[])localObject3, 0, i);
/* 624 */           return (Serializable)localObject3;
/* 625 */         }if (localObject1 == Float.TYPE) {
/* 626 */           localObject3 = new float[i];
/* 627 */           paramInputStream.read_float_array((float[])localObject3, 0, i);
/* 628 */           return (Serializable)localObject3;
/* 629 */         }if (localObject1 == Double.TYPE) {
/* 630 */           localObject3 = new double[i];
/* 631 */           paramInputStream.read_double_array((double[])localObject3, 0, i);
/* 632 */           return (Serializable)localObject3;
/* 633 */         }if (localObject1 == Short.TYPE) {
/* 634 */           localObject3 = new short[i];
/* 635 */           paramInputStream.read_short_array((short[])localObject3, 0, i);
/* 636 */           return (Serializable)localObject3;
/* 637 */         }if (localObject1 == Character.TYPE) {
/* 638 */           localObject3 = new char[i];
/* 639 */           readCharArray(paramInputStream, (char[])localObject3, 0, i);
/* 640 */           return (Serializable)localObject3;
/* 641 */         }if (localObject1 == Boolean.TYPE) {
/* 642 */           localObject3 = new boolean[i];
/* 643 */           paramInputStream.read_boolean_array((boolean[])localObject3, 0, i);
/* 644 */           return (Serializable)localObject3;
/*     */         }
/*     */ 
/* 647 */         throw new Error("Invalid primitive componentType : " + paramClass.getName());
/*     */       }
/* 649 */       if (localObject1 == java.lang.Object.class) {
/* 650 */         localObject3 = (java.lang.Object[])Array.newInstance((Class)localObject1, i);
/*     */ 
/* 656 */         paramIIOPInputStream.activeRecursionMgr.addObject(paramInt, localObject3);
/*     */ 
/* 658 */         for (j = 0; j < i; j++) {
/* 659 */           localObject4 = null;
/*     */           try {
/* 661 */             localObject4 = Util.readAny(paramInputStream);
/*     */           }
/*     */           catch (IndirectionException localIndirectionException1)
/*     */           {
/*     */             try
/*     */             {
/* 668 */               localObject4 = paramIIOPInputStream.activeRecursionMgr.getObject(localIndirectionException1.offset);
/*     */             }
/*     */             catch (IOException localIOException1)
/*     */             {
/* 674 */               throw this.utilWrapper.invalidIndirection(localIOException1, new Integer(localIndirectionException1.offset));
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 679 */           localObject3[j] = localObject4;
/*     */         }
/* 681 */         return (Serializable)localObject3;
/*     */       }
/* 683 */       java.lang.Object localObject3 = (java.lang.Object[])Array.newInstance((Class)localObject1, i);
/*     */ 
/* 688 */       paramIIOPInputStream.activeRecursionMgr.addObject(paramInt, localObject3);
/*     */ 
/* 694 */       int k = 2;
/* 695 */       int m = 0;
/*     */ 
/* 697 */       if (((Class)localObject1).isInterface()) {
/* 698 */         int n = 0;
/*     */ 
/* 701 */         if (Remote.class.isAssignableFrom((Class)localObject1))
/*     */         {
/* 704 */           k = 0;
/*     */ 
/* 708 */           n = 1;
/* 709 */         } else if (org.omg.CORBA.Object.class.isAssignableFrom((Class)localObject1))
/*     */         {
/* 711 */           k = 0;
/* 712 */           n = 1;
/* 713 */         } else if (RepositoryId.isAbstractBase((Class)localObject1))
/*     */         {
/* 715 */           k = 1;
/* 716 */           n = 1;
/* 717 */         } else if (ObjectStreamClassCorbaExt.isAbstractInterface((Class)localObject1))
/*     */         {
/* 722 */           k = 1;
/*     */         }
/*     */ 
/* 725 */         if (n != 0)
/*     */           try {
/* 727 */             String str1 = Util.getCodebase((Class)localObject1);
/* 728 */             String str2 = RepositoryId.createForAnyType((Class)localObject1);
/* 729 */             Class localClass = Utility.loadStubClass(str2, str1, (Class)localObject1);
/*     */ 
/* 731 */             localObject2 = localClass;
/*     */           } catch (ClassNotFoundException localClassNotFoundException) {
/* 733 */             m = 1;
/*     */           }
/*     */         else {
/* 736 */           m = 1;
/*     */         }
/*     */       }
/*     */ 
/* 740 */       for (int j = 0; j < i; j++) {
/*     */         try
/*     */         {
/* 743 */           switch (k) {
/*     */           case 0:
/* 745 */             if (m == 0)
/* 746 */               localObject3[j] = paramInputStream.read_Object(localObject2);
/*     */             else {
/* 748 */               localObject3[j] = Utility.readObjectAndNarrow(paramInputStream, localObject2);
/*     */             }
/*     */ 
/* 751 */             break;
/*     */           case 1:
/* 753 */             if (m == 0)
/* 754 */               localObject3[j] = paramInputStream.read_abstract_interface(localObject2);
/*     */             else {
/* 756 */               localObject3[j] = Utility.readAbstractAndNarrow(paramInputStream, localObject2);
/*     */             }
/* 758 */             break;
/*     */           case 2:
/* 760 */             localObject3[j] = paramInputStream.read_value(localObject2);
/*     */           }
/*     */ 
/*     */         }
/*     */         catch (IndirectionException localIndirectionException2)
/*     */         {
/*     */           try
/*     */           {
/* 768 */             localObject3[j] = paramIIOPInputStream.activeRecursionMgr.getObject(localIndirectionException2.offset);
/*     */           }
/*     */           catch (IOException localIOException2)
/*     */           {
/* 774 */             throw this.utilWrapper.invalidIndirection(localIOException2, new Integer(localIndirectionException2.offset));
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 781 */       return (Serializable)localObject3;
/*     */     }
/*     */     finally
/*     */     {
/* 788 */       paramIIOPInputStream.activeRecursionMgr.removeObject(paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   private boolean isArray(String paramString) {
/* 793 */     return RepositoryId.cache.getId(paramString).isSequence();
/*     */   }
/*     */ 
/*     */   private String getOutputStreamClassName() {
/* 797 */     return "com.sun.corba.se.impl.io.IIOPOutputStream";
/*     */   }
/*     */ 
/*     */   private IIOPOutputStream createOutputStream() {
/* 801 */     String str = getOutputStreamClassName();
/*     */     try {
/* 803 */       IIOPOutputStream localIIOPOutputStream = createOutputStreamBuiltIn(str);
/* 804 */       if (localIIOPOutputStream != null) {
/* 805 */         return localIIOPOutputStream;
/*     */       }
/* 807 */       return (IIOPOutputStream)createCustom(IIOPOutputStream.class, str);
/*     */     }
/*     */     catch (Throwable localThrowable) {
/* 810 */       InternalError localInternalError = new InternalError("Error loading " + str);
/*     */ 
/* 813 */       localInternalError.initCause(localThrowable);
/* 814 */       throw localInternalError;
/*     */     }
/*     */   }
/*     */ 
/*     */   private IIOPOutputStream createOutputStreamBuiltIn(final String paramString)
/*     */     throws Throwable
/*     */   {
/*     */     try
/*     */     {
/* 826 */       return (IIOPOutputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public IIOPOutputStream run() throws IOException {
/* 829 */           return ValueHandlerImpl.this.createOutputStreamBuiltInNoPriv(paramString);
/*     */         }
/*     */       });
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 834 */       throw localPrivilegedActionException.getCause();
/*     */     }
/*     */   }
/*     */ 
/*     */   private IIOPOutputStream createOutputStreamBuiltInNoPriv(String paramString)
/*     */     throws IOException
/*     */   {
/* 844 */     return paramString.equals(IIOPOutputStream.class.getName()) ? new IIOPOutputStream() : null;
/*     */   }
/*     */ 
/*     */   private String getInputStreamClassName()
/*     */   {
/* 849 */     return "com.sun.corba.se.impl.io.IIOPInputStream";
/*     */   }
/*     */ 
/*     */   private IIOPInputStream createInputStream() {
/* 853 */     String str = getInputStreamClassName();
/*     */     try {
/* 855 */       IIOPInputStream localIIOPInputStream = createInputStreamBuiltIn(str);
/* 856 */       if (localIIOPInputStream != null) {
/* 857 */         return localIIOPInputStream;
/*     */       }
/* 859 */       return (IIOPInputStream)createCustom(IIOPInputStream.class, str);
/*     */     }
/*     */     catch (Throwable localThrowable) {
/* 862 */       InternalError localInternalError = new InternalError("Error loading " + str);
/*     */ 
/* 865 */       localInternalError.initCause(localThrowable);
/* 866 */       throw localInternalError;
/*     */     }
/*     */   }
/*     */ 
/*     */   private IIOPInputStream createInputStreamBuiltIn(final String paramString)
/*     */     throws Throwable
/*     */   {
/*     */     try
/*     */     {
/* 878 */       return (IIOPInputStream)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*     */       {
/*     */         public IIOPInputStream run() throws IOException {
/* 881 */           return ValueHandlerImpl.this.createInputStreamBuiltInNoPriv(paramString);
/*     */         }
/*     */       });
/*     */     }
/*     */     catch (PrivilegedActionException localPrivilegedActionException) {
/* 886 */       throw localPrivilegedActionException.getCause();
/*     */     }
/*     */   }
/*     */ 
/*     */   private IIOPInputStream createInputStreamBuiltInNoPriv(String paramString)
/*     */     throws IOException
/*     */   {
/* 896 */     return paramString.equals(IIOPInputStream.class.getName()) ? new IIOPInputStream() : null;
/*     */   }
/*     */ 
/*     */   private <T> T createCustom(Class<T> paramClass, String paramString)
/*     */     throws Throwable
/*     */   {
/* 910 */     ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/* 911 */     if (localClassLoader == null) {
/* 912 */       localClassLoader = ClassLoader.getSystemClassLoader();
/*     */     }
/* 914 */     Class localClass1 = localClassLoader.loadClass(paramString);
/* 915 */     Class localClass2 = localClass1.asSubclass(paramClass);
/*     */ 
/* 919 */     return localClass2.newInstance();
/*     */   }
/*     */ 
/*     */   TCKind getJavaCharTCKind()
/*     */   {
/* 924 */     return TCKind.tk_wchar;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.io.ValueHandlerImpl
 * JD-Core Version:    0.6.2
 */