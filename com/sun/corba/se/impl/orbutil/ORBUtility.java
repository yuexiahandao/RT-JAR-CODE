/*     */ package com.sun.corba.se.impl.orbutil;
/*     */ 
/*     */ import com.sun.corba.se.impl.corba.CORBAObjectImpl;
/*     */ import com.sun.corba.se.impl.ior.iiop.JavaSerializationComponent;
/*     */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.pept.transport.ContactInfoList;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.orb.ORBVersionFactory;
/*     */ import com.sun.corba.se.spi.presentation.rmi.StubAdapter;
/*     */ import com.sun.corba.se.spi.protocol.ClientDelegateFactory;
/*     */ import com.sun.corba.se.spi.protocol.CorbaClientDelegate;
/*     */ import com.sun.corba.se.spi.protocol.CorbaMessageMediator;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfoList;
/*     */ import com.sun.corba.se.spi.transport.CorbaContactInfoListFactory;
/*     */ import java.io.PrintStream;
/*     */ import java.rmi.RemoteException;
/*     */ import java.security.AccessController;
/*     */ import java.security.PermissionCollection;
/*     */ import java.security.Policy;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.security.ProtectionDomain;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Iterator;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.StringTokenizer;
/*     */ import javax.rmi.CORBA.Util;
/*     */ import javax.rmi.CORBA.ValueHandler;
/*     */ import javax.rmi.CORBA.ValueHandlerMultiFormat;
/*     */ import org.omg.CORBA.Any;
/*     */ import org.omg.CORBA.BAD_OPERATION;
/*     */ import org.omg.CORBA.CompletionStatus;
/*     */ import org.omg.CORBA.INTERNAL;
/*     */ import org.omg.CORBA.StructMember;
/*     */ import org.omg.CORBA.SystemException;
/*     */ import org.omg.CORBA.TCKind;
/*     */ import org.omg.CORBA.TypeCode;
/*     */ import org.omg.CORBA.TypeCodePackage.BadKind;
/*     */ import org.omg.CORBA.TypeCodePackage.Bounds;
/*     */ import org.omg.CORBA.portable.Delegate;
/*     */ import org.omg.CORBA.portable.InputStream;
/*     */ import org.omg.CORBA.portable.OutputStream;
/*     */ import sun.corba.JavaCorbaAccess;
/*     */ import sun.corba.SharedSecrets;
/*     */ 
/*     */ public final class ORBUtility
/*     */ {
/* 101 */   private static ORBUtilSystemException wrapper = ORBUtilSystemException.get("util");
/*     */ 
/* 103 */   private static OMGSystemException omgWrapper = OMGSystemException.get("util");
/*     */ 
/* 106 */   private static StructMember[] members = null;
/*     */ 
/* 362 */   private static final Hashtable exceptionClassNames = new Hashtable();
/* 363 */   private static final Hashtable exceptionRepositoryIds = new Hashtable();
/*     */ 
/*     */   private static StructMember[] systemExceptionMembers(ORB paramORB)
/*     */   {
/* 109 */     if (members == null) {
/* 110 */       members = new StructMember[3];
/* 111 */       members[0] = new StructMember("id", paramORB.create_string_tc(0), null);
/* 112 */       members[1] = new StructMember("minor", paramORB.get_primitive_tc(TCKind.tk_long), null);
/* 113 */       members[2] = new StructMember("completed", paramORB.get_primitive_tc(TCKind.tk_long), null);
/*     */     }
/* 115 */     return members;
/*     */   }
/*     */ 
/*     */   private static TypeCode getSystemExceptionTypeCode(ORB paramORB, String paramString1, String paramString2) {
/* 119 */     synchronized (TypeCode.class) {
/* 120 */       return paramORB.create_exception_tc(paramString1, paramString2, systemExceptionMembers(paramORB));
/*     */     }
/*     */   }
/*     */ 
/*     */   private static boolean isSystemExceptionTypeCode(TypeCode paramTypeCode, ORB paramORB) {
/* 125 */     StructMember[] arrayOfStructMember = systemExceptionMembers(paramORB);
/*     */     try {
/* 127 */       return (paramTypeCode.kind().value() == 22) && (paramTypeCode.member_count() == 3) && (paramTypeCode.member_type(0).equal(arrayOfStructMember[0].type)) && (paramTypeCode.member_type(1).equal(arrayOfStructMember[1].type)) && (paramTypeCode.member_type(2).equal(arrayOfStructMember[2].type));
/*     */     }
/*     */     catch (BadKind localBadKind)
/*     */     {
/* 133 */       return false; } catch (Bounds localBounds) {
/*     */     }
/* 135 */     return false;
/*     */   }
/*     */ 
/*     */   public static void insertSystemException(SystemException paramSystemException, Any paramAny)
/*     */   {
/* 144 */     OutputStream localOutputStream = paramAny.create_output_stream();
/* 145 */     ORB localORB = (ORB)localOutputStream.orb();
/* 146 */     String str1 = paramSystemException.getClass().getName();
/* 147 */     String str2 = repositoryIdOf(str1);
/* 148 */     localOutputStream.write_string(str2);
/* 149 */     localOutputStream.write_long(paramSystemException.minor);
/* 150 */     localOutputStream.write_long(paramSystemException.completed.value());
/* 151 */     paramAny.read_value(localOutputStream.create_input_stream(), getSystemExceptionTypeCode(localORB, str2, str1));
/*     */   }
/*     */ 
/*     */   public static SystemException extractSystemException(Any paramAny)
/*     */   {
/* 156 */     InputStream localInputStream = paramAny.create_input_stream();
/* 157 */     ORB localORB = (ORB)localInputStream.orb();
/* 158 */     if (!isSystemExceptionTypeCode(paramAny.type(), localORB)) {
/* 159 */       throw wrapper.unknownDsiSysex(CompletionStatus.COMPLETED_MAYBE);
/*     */     }
/* 161 */     return readSystemException(localInputStream);
/*     */   }
/*     */ 
/*     */   public static ValueHandler createValueHandler()
/*     */   {
/* 168 */     return Util.createValueHandler();
/*     */   }
/*     */ 
/*     */   public static boolean isForeignORB(ORB paramORB)
/*     */   {
/* 178 */     if (paramORB == null)
/* 179 */       return false;
/*     */     try
/*     */     {
/* 182 */       return paramORB.getORBVersion().equals(ORBVersionFactory.getFOREIGN()); } catch (SecurityException localSecurityException) {
/*     */     }
/* 184 */     return false;
/*     */   }
/*     */ 
/*     */   public static int bytesToInt(byte[] paramArrayOfByte, int paramInt)
/*     */   {
/* 199 */     int i = paramArrayOfByte[(paramInt++)] << 24 & 0xFF000000;
/* 200 */     int j = paramArrayOfByte[(paramInt++)] << 16 & 0xFF0000;
/* 201 */     int k = paramArrayOfByte[(paramInt++)] << 8 & 0xFF00;
/* 202 */     int m = paramArrayOfByte[(paramInt++)] << 0 & 0xFF;
/*     */ 
/* 204 */     return i | j | k | m;
/*     */   }
/*     */ 
/*     */   public static void intToBytes(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */   {
/* 216 */     paramArrayOfByte[(paramInt2++)] = ((byte)(paramInt1 >>> 24 & 0xFF));
/* 217 */     paramArrayOfByte[(paramInt2++)] = ((byte)(paramInt1 >>> 16 & 0xFF));
/* 218 */     paramArrayOfByte[(paramInt2++)] = ((byte)(paramInt1 >>> 8 & 0xFF));
/* 219 */     paramArrayOfByte[(paramInt2++)] = ((byte)(paramInt1 >>> 0 & 0xFF));
/*     */   }
/*     */ 
/*     */   public static int hexOf(char paramChar)
/*     */   {
/* 228 */     int i = paramChar - '0';
/* 229 */     if ((i >= 0) && (i <= 9)) {
/* 230 */       return i;
/*     */     }
/* 232 */     i = paramChar - 'a' + 10;
/* 233 */     if ((i >= 10) && (i <= 15)) {
/* 234 */       return i;
/*     */     }
/* 236 */     i = paramChar - 'A' + 10;
/* 237 */     if ((i >= 10) && (i <= 15)) {
/* 238 */       return i;
/*     */     }
/* 240 */     throw wrapper.badHexDigit();
/*     */   }
/*     */ 
/*     */   public static void writeSystemException(SystemException paramSystemException, OutputStream paramOutputStream)
/*     */   {
/* 253 */     String str = repositoryIdOf(paramSystemException.getClass().getName());
/* 254 */     paramOutputStream.write_string(str);
/* 255 */     paramOutputStream.write_long(paramSystemException.minor);
/* 256 */     paramOutputStream.write_long(paramSystemException.completed.value());
/*     */   }
/*     */ 
/*     */   public static SystemException readSystemException(InputStream paramInputStream)
/*     */   {
/*     */     try
/*     */     {
/* 266 */       String str = classNameOf(paramInputStream.read_string());
/* 267 */       SystemException localSystemException = (SystemException)SharedSecrets.getJavaCorbaAccess().loadClass(str).newInstance();
/*     */ 
/* 269 */       localSystemException.minor = paramInputStream.read_long();
/* 270 */       localSystemException.completed = CompletionStatus.from_int(paramInputStream.read_long());
/* 271 */       return localSystemException;
/*     */     } catch (Exception localException) {
/* 273 */       throw wrapper.unknownSysex(CompletionStatus.COMPLETED_MAYBE, localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static String classNameOf(String paramString)
/*     */   {
/* 286 */     String str = null;
/*     */ 
/* 288 */     str = (String)exceptionClassNames.get(paramString);
/* 289 */     if (str == null) {
/* 290 */       str = "org.omg.CORBA.UNKNOWN";
/*     */     }
/* 292 */     return str;
/*     */   }
/*     */ 
/*     */   public static boolean isSystemException(String paramString)
/*     */   {
/* 301 */     String str = null;
/*     */ 
/* 303 */     str = (String)exceptionClassNames.get(paramString);
/* 304 */     if (str == null) {
/* 305 */       return false;
/*     */     }
/* 307 */     return true;
/*     */   }
/*     */ 
/*     */   public static byte getEncodingVersion(ORB paramORB, IOR paramIOR)
/*     */   {
/* 322 */     if (paramORB.getORBData().isJavaSerializationEnabled()) {
/* 323 */       IIOPProfile localIIOPProfile = paramIOR.getProfile();
/* 324 */       IIOPProfileTemplate localIIOPProfileTemplate = (IIOPProfileTemplate)localIIOPProfile.getTaggedProfileTemplate();
/*     */ 
/* 326 */       Iterator localIterator = localIIOPProfileTemplate.iteratorById(1398099458);
/*     */ 
/* 328 */       if (localIterator.hasNext()) {
/* 329 */         JavaSerializationComponent localJavaSerializationComponent = (JavaSerializationComponent)localIterator.next();
/*     */ 
/* 331 */         int i = localJavaSerializationComponent.javaSerializationVersion();
/* 332 */         if (i >= 1)
/* 333 */           return 1;
/* 334 */         if (i > 0) {
/* 335 */           return localJavaSerializationComponent.javaSerializationVersion();
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 342 */     return 0;
/*     */   }
/*     */ 
/*     */   public static String repositoryIdOf(String paramString)
/*     */   {
/* 355 */     String str = (String)exceptionRepositoryIds.get(paramString);
/* 356 */     if (str == null) {
/* 357 */       str = "IDL:omg.org/CORBA/UNKNOWN:1.0";
/*     */     }
/* 359 */     return str;
/*     */   }
/*     */ 
/*     */   public static int[] parseVersion(String paramString)
/*     */   {
/* 483 */     if (paramString == null)
/* 484 */       return new int[0];
/* 485 */     char[] arrayOfChar = paramString.toCharArray();
/*     */ 
/* 487 */     for (int i = 0; 
/* 488 */       (i < arrayOfChar.length) && ((arrayOfChar[i] < '0') || (arrayOfChar[i] > '9')); i++)
/* 489 */       if (i == arrayOfChar.length)
/* 490 */         return new int[0];
/* 491 */     int j = i + 1;
/* 492 */     int k = 1;
/* 493 */     for (; j < arrayOfChar.length; j++)
/* 494 */       if (arrayOfChar[j] == '.')
/* 495 */         k++;
/* 496 */       else if ((arrayOfChar[j] < '0') || (arrayOfChar[j] > '9'))
/*     */           break;
/* 498 */     int[] arrayOfInt = new int[k];
/* 499 */     for (int m = 0; m < k; m++) {
/* 500 */       int n = paramString.indexOf('.', i);
/* 501 */       if ((n == -1) || (n > j))
/* 502 */         n = j;
/* 503 */       if (i >= n)
/* 504 */         arrayOfInt[m] = 0;
/*     */       else
/* 506 */         arrayOfInt[m] = Integer.parseInt(paramString.substring(i, n));
/* 507 */       i = n + 1;
/*     */     }
/* 509 */     return arrayOfInt;
/*     */   }
/*     */ 
/*     */   public static int compareVersion(int[] paramArrayOfInt1, int[] paramArrayOfInt2)
/*     */   {
/* 516 */     if (paramArrayOfInt1 == null)
/* 517 */       paramArrayOfInt1 = new int[0];
/* 518 */     if (paramArrayOfInt2 == null)
/* 519 */       paramArrayOfInt2 = new int[0];
/* 520 */     for (int i = 0; i < paramArrayOfInt1.length; i++) {
/* 521 */       if ((i >= paramArrayOfInt2.length) || (paramArrayOfInt1[i] > paramArrayOfInt2[i]))
/* 522 */         return 1;
/* 523 */       if (paramArrayOfInt1[i] < paramArrayOfInt2[i])
/* 524 */         return -1;
/*     */     }
/* 526 */     return paramArrayOfInt1.length == paramArrayOfInt2.length ? 0 : -1;
/*     */   }
/*     */ 
/*     */   public static synchronized int compareVersion(String paramString1, String paramString2)
/*     */   {
/* 533 */     return compareVersion(parseVersion(paramString1), parseVersion(paramString2));
/*     */   }
/*     */ 
/*     */   private static String compressClassName(String paramString)
/*     */   {
/* 539 */     String str = "com.sun.corba.se.";
/* 540 */     if (paramString.startsWith(str)) {
/* 541 */       return "(ORB)." + paramString.substring(str.length());
/*     */     }
/* 543 */     return paramString;
/*     */   }
/*     */ 
/*     */   public static String getThreadName(Thread paramThread)
/*     */   {
/* 551 */     if (paramThread == null) {
/* 552 */       return "null";
/*     */     }
/*     */ 
/* 558 */     String str = paramThread.getName();
/* 559 */     StringTokenizer localStringTokenizer = new StringTokenizer(str);
/* 560 */     int i = localStringTokenizer.countTokens();
/* 561 */     if (i != 5) {
/* 562 */       return str;
/*     */     }
/* 564 */     String[] arrayOfString = new String[i];
/* 565 */     for (int j = 0; j < i; j++) {
/* 566 */       arrayOfString[j] = localStringTokenizer.nextToken();
/*     */     }
/* 568 */     if (!arrayOfString[0].equals("SelectReaderThread")) {
/* 569 */       return str;
/*     */     }
/* 571 */     return "SelectReaderThread[" + arrayOfString[2] + ":" + arrayOfString[3] + "]";
/*     */   }
/*     */ 
/*     */   private static String formatStackTraceElement(StackTraceElement paramStackTraceElement)
/*     */   {
/* 576 */     return compressClassName(paramStackTraceElement.getClassName()) + "." + paramStackTraceElement.getMethodName() + (paramStackTraceElement.getFileName() != null ? "(" + paramStackTraceElement.getFileName() + ")" : (paramStackTraceElement.getFileName() != null) && (paramStackTraceElement.getLineNumber() >= 0) ? "(" + paramStackTraceElement.getFileName() + ":" + paramStackTraceElement.getLineNumber() + ")" : paramStackTraceElement.isNativeMethod() ? "(Native Method)" : "(Unknown Source)");
/*     */   }
/*     */ 
/*     */   private static void printStackTrace(StackTraceElement[] paramArrayOfStackTraceElement)
/*     */   {
/* 585 */     System.out.println("    Stack Trace:");
/*     */ 
/* 588 */     for (int i = 1; i < paramArrayOfStackTraceElement.length; i++) {
/* 589 */       System.out.print("        >");
/* 590 */       System.out.println(formatStackTraceElement(paramArrayOfStackTraceElement[i]));
/*     */     }
/*     */   }
/*     */ 
/*     */   public static synchronized void dprint(java.lang.Object paramObject, String paramString)
/*     */   {
/* 598 */     System.out.println(compressClassName(paramObject.getClass().getName()) + "(" + getThreadName(Thread.currentThread()) + "): " + paramString);
/*     */   }
/*     */ 
/*     */   public static synchronized void dprint(String paramString1, String paramString2)
/*     */   {
/* 604 */     System.out.println(compressClassName(paramString1) + "(" + getThreadName(Thread.currentThread()) + "): " + paramString2);
/*     */   }
/*     */ 
/*     */   public synchronized void dprint(String paramString)
/*     */   {
/* 610 */     dprint(this, paramString);
/*     */   }
/*     */ 
/*     */   public static synchronized void dprintTrace(java.lang.Object paramObject, String paramString) {
/* 614 */     dprint(paramObject, paramString);
/*     */ 
/* 616 */     Throwable localThrowable = new Throwable();
/* 617 */     printStackTrace(localThrowable.getStackTrace());
/*     */   }
/*     */ 
/*     */   public static synchronized void dprint(java.lang.Object paramObject, String paramString, Throwable paramThrowable)
/*     */   {
/* 623 */     System.out.println(compressClassName(paramObject.getClass().getName()) + '(' + Thread.currentThread() + "): " + paramString);
/*     */ 
/* 627 */     if (paramThrowable != null)
/* 628 */       printStackTrace(paramThrowable.getStackTrace());
/*     */   }
/*     */ 
/*     */   public static String[] concatenateStringArrays(String[] paramArrayOfString1, String[] paramArrayOfString2)
/*     */   {
/* 633 */     String[] arrayOfString = new String[paramArrayOfString1.length + paramArrayOfString2.length];
/*     */ 
/* 636 */     for (int i = 0; i < paramArrayOfString1.length; i++) {
/* 637 */       arrayOfString[i] = paramArrayOfString1[i];
/*     */     }
/* 639 */     for (i = 0; i < paramArrayOfString2.length; i++) {
/* 640 */       arrayOfString[(i + paramArrayOfString1.length)] = paramArrayOfString2[i];
/*     */     }
/* 642 */     return arrayOfString;
/*     */   }
/*     */ 
/*     */   public static void throwNotSerializableForCorba(String paramString)
/*     */   {
/* 658 */     throw omgWrapper.notSerializable(CompletionStatus.COMPLETED_MAYBE, paramString);
/*     */   }
/*     */ 
/*     */   public static byte getMaxStreamFormatVersion()
/*     */   {
/* 667 */     ValueHandler localValueHandler = Util.createValueHandler();
/*     */ 
/* 669 */     if (!(localValueHandler instanceof ValueHandlerMultiFormat)) {
/* 670 */       return 1;
/*     */     }
/* 672 */     return ((ValueHandlerMultiFormat)localValueHandler).getMaximumStreamFormatVersion();
/*     */   }
/*     */ 
/*     */   public static CorbaClientDelegate makeClientDelegate(IOR paramIOR)
/*     */   {
/* 677 */     ORB localORB = paramIOR.getORB();
/* 678 */     CorbaContactInfoList localCorbaContactInfoList = localORB.getCorbaContactInfoListFactory().create(paramIOR);
/* 679 */     CorbaClientDelegate localCorbaClientDelegate = localORB.getClientDelegateFactory().create(localCorbaContactInfoList);
/* 680 */     return localCorbaClientDelegate;
/*     */   }
/*     */ 
/*     */   public static org.omg.CORBA.Object makeObjectReference(IOR paramIOR)
/*     */   {
/* 687 */     CorbaClientDelegate localCorbaClientDelegate = makeClientDelegate(paramIOR);
/* 688 */     CORBAObjectImpl localCORBAObjectImpl = new CORBAObjectImpl();
/* 689 */     StubAdapter.setDelegate(localCORBAObjectImpl, localCorbaClientDelegate);
/* 690 */     return localCORBAObjectImpl;
/*     */   }
/*     */ 
/*     */   public static IOR getIOR(org.omg.CORBA.Object paramObject)
/*     */   {
/* 706 */     if (paramObject == null) {
/* 707 */       throw wrapper.nullObjectReference();
/*     */     }
/* 709 */     IOR localIOR = null;
/* 710 */     if (StubAdapter.isStub(paramObject)) {
/* 711 */       Delegate localDelegate = StubAdapter.getDelegate(paramObject);
/*     */ 
/* 714 */       if ((localDelegate instanceof CorbaClientDelegate)) {
/* 715 */         CorbaClientDelegate localCorbaClientDelegate = (CorbaClientDelegate)localDelegate;
/* 716 */         ContactInfoList localContactInfoList = localCorbaClientDelegate.getContactInfoList();
/*     */ 
/* 718 */         if ((localContactInfoList instanceof CorbaContactInfoList)) {
/* 719 */           CorbaContactInfoList localCorbaContactInfoList = (CorbaContactInfoList)localContactInfoList;
/* 720 */           localIOR = localCorbaContactInfoList.getTargetIOR();
/* 721 */           if (localIOR == null) {
/* 722 */             throw wrapper.nullIor();
/*     */           }
/* 724 */           return localIOR;
/*     */         }
/*     */ 
/* 732 */         throw new INTERNAL();
/*     */       }
/*     */ 
/* 743 */       throw wrapper.objrefFromForeignOrb();
/*     */     }
/* 745 */     throw wrapper.localObjectNotAllowed();
/*     */   }
/*     */ 
/*     */   public static IOR connectAndGetIOR(ORB paramORB, org.omg.CORBA.Object paramObject)
/*     */   {
/*     */     IOR localIOR;
/*     */     try
/*     */     {
/* 761 */       localIOR = getIOR(paramObject);
/*     */     } catch (BAD_OPERATION localBAD_OPERATION) {
/* 763 */       if (StubAdapter.isStub(paramObject))
/*     */         try {
/* 765 */           StubAdapter.connect(paramObject, paramORB);
/*     */         } catch (RemoteException localRemoteException) {
/* 767 */           throw wrapper.connectingServant(localRemoteException);
/*     */         }
/*     */       else {
/* 770 */         paramORB.connect(paramObject);
/*     */       }
/*     */ 
/* 773 */       localIOR = getIOR(paramObject);
/*     */     }
/*     */ 
/* 776 */     return localIOR;
/*     */   }
/*     */ 
/*     */   public static String operationNameAndRequestId(CorbaMessageMediator paramCorbaMessageMediator)
/*     */   {
/* 781 */     return "op/" + paramCorbaMessageMediator.getOperationName() + " id/" + paramCorbaMessageMediator.getRequestId();
/*     */   }
/*     */ 
/*     */   public static boolean isPrintable(char paramChar)
/*     */   {
/* 786 */     if (Character.isJavaIdentifierStart(paramChar))
/*     */     {
/* 788 */       return true;
/*     */     }
/* 790 */     if (Character.isDigit(paramChar)) {
/* 791 */       return true;
/*     */     }
/* 793 */     switch (Character.getType(paramChar)) { case 27:
/* 794 */       return true;
/*     */     case 20:
/* 795 */       return true;
/*     */     case 25:
/* 796 */       return true;
/*     */     case 24:
/* 797 */       return true;
/*     */     case 21:
/* 798 */       return true;
/*     */     case 22:
/* 799 */       return true;
/*     */     case 23:
/* 801 */     case 26: } return false;
/*     */   }
/*     */ 
/*     */   public static String getClassSecurityInfo(Class paramClass)
/*     */   {
/* 816 */     String str = (String)AccessController.doPrivileged(new PrivilegedAction()
/*     */     {
/*     */       public java.lang.Object run() {
/* 819 */         StringBuffer localStringBuffer = new StringBuffer(500);
/* 820 */         ProtectionDomain localProtectionDomain = this.val$cl.getProtectionDomain();
/* 821 */         Policy localPolicy = Policy.getPolicy();
/* 822 */         PermissionCollection localPermissionCollection = localPolicy.getPermissions(localProtectionDomain);
/* 823 */         localStringBuffer.append("\nPermissionCollection ");
/* 824 */         localStringBuffer.append(localPermissionCollection.toString());
/*     */ 
/* 827 */         localStringBuffer.append(localProtectionDomain.toString());
/* 828 */         return localStringBuffer.toString();
/*     */       }
/*     */     });
/* 831 */     return str;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 370 */     exceptionClassNames.put("IDL:omg.org/CORBA/BAD_CONTEXT:1.0", "org.omg.CORBA.BAD_CONTEXT");
/*     */ 
/* 372 */     exceptionClassNames.put("IDL:omg.org/CORBA/BAD_INV_ORDER:1.0", "org.omg.CORBA.BAD_INV_ORDER");
/*     */ 
/* 374 */     exceptionClassNames.put("IDL:omg.org/CORBA/BAD_OPERATION:1.0", "org.omg.CORBA.BAD_OPERATION");
/*     */ 
/* 376 */     exceptionClassNames.put("IDL:omg.org/CORBA/BAD_PARAM:1.0", "org.omg.CORBA.BAD_PARAM");
/*     */ 
/* 378 */     exceptionClassNames.put("IDL:omg.org/CORBA/BAD_TYPECODE:1.0", "org.omg.CORBA.BAD_TYPECODE");
/*     */ 
/* 380 */     exceptionClassNames.put("IDL:omg.org/CORBA/COMM_FAILURE:1.0", "org.omg.CORBA.COMM_FAILURE");
/*     */ 
/* 382 */     exceptionClassNames.put("IDL:omg.org/CORBA/DATA_CONVERSION:1.0", "org.omg.CORBA.DATA_CONVERSION");
/*     */ 
/* 384 */     exceptionClassNames.put("IDL:omg.org/CORBA/IMP_LIMIT:1.0", "org.omg.CORBA.IMP_LIMIT");
/*     */ 
/* 386 */     exceptionClassNames.put("IDL:omg.org/CORBA/INTF_REPOS:1.0", "org.omg.CORBA.INTF_REPOS");
/*     */ 
/* 388 */     exceptionClassNames.put("IDL:omg.org/CORBA/INTERNAL:1.0", "org.omg.CORBA.INTERNAL");
/*     */ 
/* 390 */     exceptionClassNames.put("IDL:omg.org/CORBA/INV_FLAG:1.0", "org.omg.CORBA.INV_FLAG");
/*     */ 
/* 392 */     exceptionClassNames.put("IDL:omg.org/CORBA/INV_IDENT:1.0", "org.omg.CORBA.INV_IDENT");
/*     */ 
/* 394 */     exceptionClassNames.put("IDL:omg.org/CORBA/INV_OBJREF:1.0", "org.omg.CORBA.INV_OBJREF");
/*     */ 
/* 396 */     exceptionClassNames.put("IDL:omg.org/CORBA/MARSHAL:1.0", "org.omg.CORBA.MARSHAL");
/*     */ 
/* 398 */     exceptionClassNames.put("IDL:omg.org/CORBA/NO_MEMORY:1.0", "org.omg.CORBA.NO_MEMORY");
/*     */ 
/* 400 */     exceptionClassNames.put("IDL:omg.org/CORBA/FREE_MEM:1.0", "org.omg.CORBA.FREE_MEM");
/*     */ 
/* 402 */     exceptionClassNames.put("IDL:omg.org/CORBA/NO_IMPLEMENT:1.0", "org.omg.CORBA.NO_IMPLEMENT");
/*     */ 
/* 404 */     exceptionClassNames.put("IDL:omg.org/CORBA/NO_PERMISSION:1.0", "org.omg.CORBA.NO_PERMISSION");
/*     */ 
/* 406 */     exceptionClassNames.put("IDL:omg.org/CORBA/NO_RESOURCES:1.0", "org.omg.CORBA.NO_RESOURCES");
/*     */ 
/* 408 */     exceptionClassNames.put("IDL:omg.org/CORBA/NO_RESPONSE:1.0", "org.omg.CORBA.NO_RESPONSE");
/*     */ 
/* 410 */     exceptionClassNames.put("IDL:omg.org/CORBA/OBJ_ADAPTER:1.0", "org.omg.CORBA.OBJ_ADAPTER");
/*     */ 
/* 412 */     exceptionClassNames.put("IDL:omg.org/CORBA/INITIALIZE:1.0", "org.omg.CORBA.INITIALIZE");
/*     */ 
/* 414 */     exceptionClassNames.put("IDL:omg.org/CORBA/PERSIST_STORE:1.0", "org.omg.CORBA.PERSIST_STORE");
/*     */ 
/* 416 */     exceptionClassNames.put("IDL:omg.org/CORBA/TRANSIENT:1.0", "org.omg.CORBA.TRANSIENT");
/*     */ 
/* 418 */     exceptionClassNames.put("IDL:omg.org/CORBA/UNKNOWN:1.0", "org.omg.CORBA.UNKNOWN");
/*     */ 
/* 420 */     exceptionClassNames.put("IDL:omg.org/CORBA/OBJECT_NOT_EXIST:1.0", "org.omg.CORBA.OBJECT_NOT_EXIST");
/*     */ 
/* 424 */     exceptionClassNames.put("IDL:omg.org/CORBA/INVALID_TRANSACTION:1.0", "org.omg.CORBA.INVALID_TRANSACTION");
/*     */ 
/* 426 */     exceptionClassNames.put("IDL:omg.org/CORBA/TRANSACTION_REQUIRED:1.0", "org.omg.CORBA.TRANSACTION_REQUIRED");
/*     */ 
/* 428 */     exceptionClassNames.put("IDL:omg.org/CORBA/TRANSACTION_ROLLEDBACK:1.0", "org.omg.CORBA.TRANSACTION_ROLLEDBACK");
/*     */ 
/* 432 */     exceptionClassNames.put("IDL:omg.org/CORBA/INV_POLICY:1.0", "org.omg.CORBA.INV_POLICY");
/*     */ 
/* 436 */     exceptionClassNames.put("IDL:omg.org/CORBA/TRANSACTION_UNAVAILABLE:1.0", "org.omg.CORBA.TRANSACTION_UNAVAILABLE");
/*     */ 
/* 439 */     exceptionClassNames.put("IDL:omg.org/CORBA/TRANSACTION_MODE:1.0", "org.omg.CORBA.TRANSACTION_MODE");
/*     */ 
/* 443 */     exceptionClassNames.put("IDL:omg.org/CORBA/CODESET_INCOMPATIBLE:1.0", "org.omg.CORBA.CODESET_INCOMPATIBLE");
/*     */ 
/* 445 */     exceptionClassNames.put("IDL:omg.org/CORBA/REBIND:1.0", "org.omg.CORBA.REBIND");
/*     */ 
/* 447 */     exceptionClassNames.put("IDL:omg.org/CORBA/TIMEOUT:1.0", "org.omg.CORBA.TIMEOUT");
/*     */ 
/* 449 */     exceptionClassNames.put("IDL:omg.org/CORBA/BAD_QOS:1.0", "org.omg.CORBA.BAD_QOS");
/*     */ 
/* 453 */     exceptionClassNames.put("IDL:omg.org/CORBA/INVALID_ACTIVITY:1.0", "org.omg.CORBA.INVALID_ACTIVITY");
/*     */ 
/* 455 */     exceptionClassNames.put("IDL:omg.org/CORBA/ACTIVITY_COMPLETED:1.0", "org.omg.CORBA.ACTIVITY_COMPLETED");
/*     */ 
/* 457 */     exceptionClassNames.put("IDL:omg.org/CORBA/ACTIVITY_REQUIRED:1.0", "org.omg.CORBA.ACTIVITY_REQUIRED");
/*     */ 
/* 463 */     Enumeration localEnumeration = exceptionClassNames.keys();
/*     */     try
/*     */     {
/* 469 */       while (localEnumeration.hasMoreElements()) {
/* 470 */         java.lang.Object localObject = localEnumeration.nextElement();
/* 471 */         String str1 = (String)localObject;
/* 472 */         String str2 = (String)exceptionClassNames.get(str1);
/* 473 */         exceptionRepositoryIds.put(str2, str1);
/*     */       }
/*     */     }
/*     */     catch (NoSuchElementException localNoSuchElementException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.orbutil.ORBUtility
 * JD-Core Version:    0.6.2
 */