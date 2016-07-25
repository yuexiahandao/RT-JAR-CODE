/*      */ package com.sun.corba.se.impl.io;
/*      */ 
/*      */ import com.sun.corba.se.impl.logging.OMGSystemException;
/*      */ import com.sun.corba.se.impl.logging.UtilSystemException;
/*      */ import com.sun.corba.se.impl.util.Utility;
/*      */ import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescription;
/*      */ import com.sun.org.omg.SendingContext.CodeBase;
/*      */ import java.io.EOFException;
/*      */ import java.io.Externalizable;
/*      */ import java.io.IOException;
/*      */ import java.io.InvalidClassException;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.NotActiveException;
/*      */ import java.io.ObjectInputValidation;
/*      */ import java.io.OptionalDataException;
/*      */ import java.io.StreamCorruptedException;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.InvocationTargetException;
/*      */ import java.lang.reflect.Method;
/*      */ import java.rmi.Remote;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.security.PrivilegedExceptionAction;
/*      */ import java.util.Enumeration;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import java.util.Vector;
/*      */ import javax.rmi.CORBA.Util;
/*      */ import javax.rmi.CORBA.ValueHandler;
/*      */ import org.omg.CORBA.CompletionStatus;
/*      */ import org.omg.CORBA.MARSHAL;
/*      */ import org.omg.CORBA.ORB;
/*      */ import org.omg.CORBA.TCKind;
/*      */ import org.omg.CORBA.TypeCode;
/*      */ import org.omg.CORBA.ValueMember;
/*      */ import org.omg.CORBA.portable.IndirectionException;
/*      */ import org.omg.CORBA.portable.ValueInputStream;
/*      */ import sun.corba.Bridge;
/*      */ 
/*      */ public class IIOPInputStream extends InputStreamHook
/*      */ {
/*   98 */   private static Bridge bridge = (Bridge)AccessController.doPrivileged(new PrivilegedAction()
/*      */   {
/*      */     public java.lang.Object run()
/*      */     {
/*  102 */       return Bridge.get();
/*      */     }
/*      */   });
/*      */ 
/*  107 */   private static OMGSystemException omgWrapper = OMGSystemException.get("rpc.encoding");
/*      */ 
/*  109 */   private static UtilSystemException utilWrapper = UtilSystemException.get("rpc.encoding");
/*      */ 
/*  123 */   private ValueMember[] defaultReadObjectFVDMembers = null;
/*      */   private org.omg.CORBA_2_3.portable.InputStream orbStream;
/*      */   private CodeBase cbSender;
/*      */   private ValueHandlerImpl vhandler;
/*  131 */   private java.lang.Object currentObject = null;
/*      */ 
/*  133 */   private ObjectStreamClass currentClassDesc = null;
/*      */ 
/*  135 */   private Class currentClass = null;
/*      */ 
/*  137 */   private int recursionDepth = 0;
/*      */ 
/*  139 */   private int simpleReadDepth = 0;
/*      */ 
/*  149 */   ActiveRecursionManager activeRecursionMgr = new ActiveRecursionManager();
/*      */ 
/*  151 */   private IOException abortIOException = null;
/*      */ 
/*  154 */   private ClassNotFoundException abortClassNotFoundException = null;
/*      */   private Vector callbacks;
/*      */   ObjectStreamClass[] classdesc;
/*      */   Class[] classes;
/*      */   int spClass;
/*      */   private static final String kEmptyStr = "";
/*  176 */   public static final TypeCode kRemoteTypeCode = ORB.init().get_primitive_tc(TCKind.tk_objref);
/*  177 */   public static final TypeCode kValueTypeCode = ORB.init().get_primitive_tc(TCKind.tk_value);
/*      */   private static final boolean useFVDOnly = false;
/*      */   private byte streamFormatVersion;
/*  193 */   private static final Constructor OPT_DATA_EXCEPTION_CTOR = getOptDataExceptionCtor();
/*      */ 
/*  190 */   private java.lang.Object[] readObjectArgList = { this };
/*      */ 
/*      */   private static Constructor getOptDataExceptionCtor()
/*      */   {
/*      */     try
/*      */     {
/*  203 */       Constructor localConstructor = (Constructor)AccessController.doPrivileged(new PrivilegedExceptionAction()
/*      */       {
/*      */         public java.lang.Object run()
/*      */           throws NoSuchMethodException, SecurityException
/*      */         {
/*  211 */           Constructor localConstructor = OptionalDataException.class.getDeclaredConstructor(new Class[] { Boolean.TYPE });
/*      */ 
/*  216 */           localConstructor.setAccessible(true);
/*      */ 
/*  218 */           return localConstructor;
/*      */         }
/*      */       });
/*  221 */       if (localConstructor == null)
/*      */       {
/*  223 */         throw new Error("Unable to find OptionalDataException constructor");
/*      */       }
/*  225 */       return localConstructor;
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*  229 */       throw new ExceptionInInitializerError(localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   private OptionalDataException createOptionalDataException()
/*      */   {
/*      */     try
/*      */     {
/*  237 */       OptionalDataException localOptionalDataException = (OptionalDataException)OPT_DATA_EXCEPTION_CTOR.newInstance(new java.lang.Object[] { Boolean.TRUE });
/*      */ 
/*  242 */       if (localOptionalDataException == null)
/*      */       {
/*  244 */         throw new Error("Created null OptionalDataException");
/*      */       }
/*  246 */       return localOptionalDataException;
/*      */     }
/*      */     catch (Exception localException)
/*      */     {
/*  250 */       throw new Error("Couldn't create OptionalDataException", localException);
/*      */     }
/*      */   }
/*      */ 
/*      */   protected byte getStreamFormatVersion()
/*      */   {
/*  257 */     return this.streamFormatVersion;
/*      */   }
/*      */ 
/*      */   private void readFormatVersion()
/*      */     throws IOException
/*      */   {
/*  265 */     this.streamFormatVersion = this.orbStream.read_octet();
/*      */     java.lang.Object localObject;
/*      */     IOException localIOException;
/*  267 */     if ((this.streamFormatVersion < 1) || (this.streamFormatVersion > this.vhandler.getMaximumStreamFormatVersion()))
/*      */     {
/*  269 */       localObject = omgWrapper.unsupportedFormatVersion(CompletionStatus.COMPLETED_MAYBE);
/*      */ 
/*  272 */       localIOException = new IOException("Unsupported format version: " + this.streamFormatVersion);
/*      */ 
/*  274 */       localIOException.initCause((Throwable)localObject);
/*  275 */       throw localIOException;
/*      */     }
/*      */ 
/*  278 */     if ((this.streamFormatVersion == 2) && 
/*  279 */       (!(this.orbStream instanceof ValueInputStream))) {
/*  280 */       localObject = omgWrapper.notAValueinputstream(CompletionStatus.COMPLETED_MAYBE);
/*      */ 
/*  283 */       localIOException = new IOException("Not a ValueInputStream");
/*  284 */       localIOException.initCause((Throwable)localObject);
/*  285 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void setTestFVDFlag(boolean paramBoolean)
/*      */   {
/*      */   }
/*      */ 
/*      */   public IIOPInputStream()
/*      */     throws IOException
/*      */   {
/*  300 */     resetStream();
/*      */   }
/*      */ 
/*      */   final void setOrbStream(org.omg.CORBA_2_3.portable.InputStream paramInputStream) {
/*  304 */     this.orbStream = paramInputStream;
/*      */   }
/*      */ 
/*      */   final org.omg.CORBA_2_3.portable.InputStream getOrbStream() {
/*  308 */     return this.orbStream;
/*      */   }
/*      */ 
/*      */   public final void setSender(CodeBase paramCodeBase)
/*      */   {
/*  313 */     this.cbSender = paramCodeBase;
/*      */   }
/*      */ 
/*      */   public final CodeBase getSender() {
/*  317 */     return this.cbSender;
/*      */   }
/*      */ 
/*      */   public final void setValueHandler(ValueHandler paramValueHandler)
/*      */   {
/*  323 */     this.vhandler = ((ValueHandlerImpl)paramValueHandler);
/*      */   }
/*      */ 
/*      */   public final ValueHandler getValueHandler() {
/*  327 */     return this.vhandler;
/*      */   }
/*      */ 
/*      */   final void increaseRecursionDepth() {
/*  331 */     this.recursionDepth += 1;
/*      */   }
/*      */ 
/*      */   final int decreaseRecursionDepth() {
/*  335 */     return --this.recursionDepth;
/*      */   }
/*      */ 
/*      */   public final synchronized java.lang.Object readObjectDelegate()
/*      */     throws IOException
/*      */   {
/*      */     try
/*      */     {
/*  377 */       this.readObjectState.readData(this);
/*      */ 
/*  379 */       return this.orbStream.read_abstract_interface();
/*      */     } catch (MARSHAL localMARSHAL) {
/*  381 */       handleOptionalDataMarshalException(localMARSHAL, true);
/*  382 */       throw localMARSHAL;
/*      */     }
/*      */     catch (IndirectionException localIndirectionException)
/*      */     {
/*  388 */       return this.activeRecursionMgr.getObject(localIndirectionException.offset);
/*      */     }
/*      */   }
/*      */ 
/*      */   final synchronized java.lang.Object simpleReadObject(Class paramClass, String paramString, CodeBase paramCodeBase, int paramInt)
/*      */   {
/*  400 */     java.lang.Object localObject1 = this.currentObject;
/*  401 */     ObjectStreamClass localObjectStreamClass = this.currentClassDesc;
/*  402 */     Class localClass = this.currentClass;
/*  403 */     byte b = this.streamFormatVersion;
/*      */ 
/*  405 */     this.simpleReadDepth += 1;
/*  406 */     java.lang.Object localObject2 = null;
/*      */     try
/*      */     {
/*  413 */       if (this.vhandler.useFullValueDescription(paramClass, paramString))
/*  414 */         localObject2 = inputObjectUsingFVD(paramClass, paramString, paramCodeBase, paramInt);
/*      */       else {
/*  416 */         localObject2 = inputObject(paramClass, paramString, paramCodeBase, paramInt);
/*      */       }
/*      */ 
/*  419 */       localObject2 = this.currentClassDesc.readResolve(localObject2);
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException)
/*      */     {
/*  423 */       bridge.throwException(localClassNotFoundException);
/*  424 */       return null;
/*      */     }
/*      */     catch (IOException localIOException1)
/*      */     {
/*  429 */       bridge.throwException(localIOException1);
/*  430 */       return null;
/*      */     }
/*      */     finally {
/*  433 */       this.simpleReadDepth -= 1;
/*  434 */       this.currentObject = localObject1;
/*  435 */       this.currentClassDesc = localObjectStreamClass;
/*  436 */       this.currentClass = localClass;
/*  437 */       this.streamFormatVersion = b;
/*      */     }
/*      */ 
/*  444 */     IOException localIOException2 = this.abortIOException;
/*  445 */     if (this.simpleReadDepth == 0)
/*  446 */       this.abortIOException = null;
/*  447 */     if (localIOException2 != null) {
/*  448 */       bridge.throwException(localIOException2);
/*  449 */       return null;
/*      */     }
/*      */ 
/*  453 */     java.lang.Object localObject3 = this.abortClassNotFoundException;
/*  454 */     if (this.simpleReadDepth == 0)
/*  455 */       this.abortClassNotFoundException = null;
/*  456 */     if (localObject3 != null) {
/*  457 */       bridge.throwException((Throwable)localObject3);
/*  458 */       return null;
/*      */     }
/*      */ 
/*  461 */     return localObject2;
/*      */   }
/*      */ 
/*      */   public final synchronized void simpleSkipObject(String paramString, CodeBase paramCodeBase)
/*      */   {
/*  470 */     java.lang.Object localObject1 = this.currentObject;
/*  471 */     ObjectStreamClass localObjectStreamClass = this.currentClassDesc;
/*  472 */     Class localClass = this.currentClass;
/*  473 */     byte b = this.streamFormatVersion;
/*      */ 
/*  475 */     this.simpleReadDepth += 1;
/*  476 */     java.lang.Object localObject2 = null;
/*      */     try
/*      */     {
/*  482 */       skipObjectUsingFVD(paramString, paramCodeBase);
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException1)
/*      */     {
/*  486 */       bridge.throwException(localClassNotFoundException1);
/*      */       return;
/*      */     }
/*  491 */     catch (IOException localIOException1) { bridge.throwException(localIOException1);
/*      */       return;
/*      */     }
/*      */     finally {
/*  495 */       this.simpleReadDepth -= 1;
/*  496 */       this.streamFormatVersion = b;
/*  497 */       this.currentObject = localObject1;
/*  498 */       this.currentClassDesc = localObjectStreamClass;
/*  499 */       this.currentClass = localClass;
/*      */     }
/*      */ 
/*  506 */     IOException localIOException2 = this.abortIOException;
/*  507 */     if (this.simpleReadDepth == 0)
/*  508 */       this.abortIOException = null;
/*  509 */     if (localIOException2 != null) {
/*  510 */       bridge.throwException(localIOException2);
/*  511 */       return;
/*      */     }
/*      */ 
/*  515 */     ClassNotFoundException localClassNotFoundException2 = this.abortClassNotFoundException;
/*  516 */     if (this.simpleReadDepth == 0)
/*  517 */       this.abortClassNotFoundException = null;
/*  518 */     if (localClassNotFoundException2 != null) {
/*  519 */       bridge.throwException(localClassNotFoundException2);
/*  520 */       return;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final java.lang.Object readObjectOverride()
/*      */     throws OptionalDataException, ClassNotFoundException, IOException
/*      */   {
/*  542 */     return readObjectDelegate();
/*      */   }
/*      */ 
/*      */   final synchronized void defaultReadObjectDelegate()
/*      */   {
/*      */     try
/*      */     {
/*  566 */       if ((this.currentObject == null) || (this.currentClassDesc == null))
/*      */       {
/*  568 */         throw new NotActiveException("defaultReadObjectDelegate");
/*      */       }
/*      */ 
/*  574 */       if ((this.defaultReadObjectFVDMembers != null) && (this.defaultReadObjectFVDMembers.length > 0))
/*      */       {
/*  586 */         inputClassFields(this.currentObject, this.currentClass, this.currentClassDesc, this.defaultReadObjectFVDMembers, this.cbSender);
/*      */       }
/*      */       else
/*      */       {
/*  595 */         ObjectStreamField[] arrayOfObjectStreamField = this.currentClassDesc.getFieldsNoCopy();
/*      */ 
/*  597 */         if (arrayOfObjectStreamField.length > 0) {
/*  598 */           inputClassFields(this.currentObject, this.currentClass, arrayOfObjectStreamField, this.cbSender);
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (NotActiveException localNotActiveException)
/*      */     {
/*  604 */       bridge.throwException(localNotActiveException);
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*  608 */       bridge.throwException(localIOException);
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException)
/*      */     {
/*  612 */       bridge.throwException(localClassNotFoundException);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final boolean enableResolveObjectDelegate(boolean paramBoolean)
/*      */   {
/*  635 */     return false;
/*      */   }
/*      */ 
/*      */   public final void mark(int paramInt)
/*      */   {
/*  642 */     this.orbStream.mark(paramInt);
/*      */   }
/*      */ 
/*      */   public final boolean markSupported() {
/*  646 */     return this.orbStream.markSupported();
/*      */   }
/*      */ 
/*      */   public final void reset() throws IOException {
/*      */     try {
/*  651 */       this.orbStream.reset();
/*      */     } catch (Error localError) {
/*  653 */       IOException localIOException = new IOException(localError.getMessage());
/*  654 */       localIOException.initCause(localError);
/*  655 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final int available() throws IOException {
/*  660 */     return 0;
/*      */   }
/*      */ 
/*      */   public final void close() throws IOException
/*      */   {
/*      */   }
/*      */ 
/*      */   public final int read() throws IOException {
/*      */     try {
/*  669 */       this.readObjectState.readData(this);
/*      */ 
/*  671 */       return this.orbStream.read_octet() << 0 & 0xFF;
/*      */     } catch (MARSHAL localMARSHAL) {
/*  673 */       if (localMARSHAL.minor == 1330446344)
/*      */       {
/*  675 */         setState(IN_READ_OBJECT_NO_MORE_OPT_DATA);
/*  676 */         return -1;
/*      */       }
/*      */ 
/*  679 */       throw localMARSHAL;
/*      */     } catch (Error localError) {
/*  681 */       IOException localIOException = new IOException(localError.getMessage());
/*  682 */       localIOException.initCause(localError);
/*  683 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException {
/*      */     try {
/*  689 */       this.readObjectState.readData(this);
/*      */ 
/*  691 */       this.orbStream.read_octet_array(paramArrayOfByte, paramInt1, paramInt2);
/*  692 */       return paramInt2;
/*      */     } catch (MARSHAL localMARSHAL) {
/*  694 */       if (localMARSHAL.minor == 1330446344)
/*      */       {
/*  696 */         setState(IN_READ_OBJECT_NO_MORE_OPT_DATA);
/*  697 */         return -1;
/*      */       }
/*      */ 
/*  700 */       throw localMARSHAL;
/*      */     } catch (Error localError) {
/*  702 */       IOException localIOException = new IOException(localError.getMessage());
/*  703 */       localIOException.initCause(localError);
/*  704 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final boolean readBoolean() throws IOException
/*      */   {
/*      */     try {
/*  711 */       this.readObjectState.readData(this);
/*      */ 
/*  713 */       return this.orbStream.read_boolean();
/*      */     } catch (MARSHAL localMARSHAL) {
/*  715 */       handleOptionalDataMarshalException(localMARSHAL, false);
/*  716 */       throw localMARSHAL;
/*      */     }
/*      */     catch (Error localError) {
/*  719 */       IOException localIOException = new IOException(localError.getMessage());
/*  720 */       localIOException.initCause(localError);
/*  721 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final byte readByte() throws IOException {
/*      */     try {
/*  727 */       this.readObjectState.readData(this);
/*      */ 
/*  729 */       return this.orbStream.read_octet();
/*      */     } catch (MARSHAL localMARSHAL) {
/*  731 */       handleOptionalDataMarshalException(localMARSHAL, false);
/*  732 */       throw localMARSHAL;
/*      */     }
/*      */     catch (Error localError) {
/*  735 */       IOException localIOException = new IOException(localError.getMessage());
/*  736 */       localIOException.initCause(localError);
/*  737 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final char readChar() throws IOException {
/*      */     try {
/*  743 */       this.readObjectState.readData(this);
/*      */ 
/*  745 */       return this.orbStream.read_wchar();
/*      */     } catch (MARSHAL localMARSHAL) {
/*  747 */       handleOptionalDataMarshalException(localMARSHAL, false);
/*  748 */       throw localMARSHAL;
/*      */     }
/*      */     catch (Error localError) {
/*  751 */       IOException localIOException = new IOException(localError.getMessage());
/*  752 */       localIOException.initCause(localError);
/*  753 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final double readDouble() throws IOException {
/*      */     try {
/*  759 */       this.readObjectState.readData(this);
/*      */ 
/*  761 */       return this.orbStream.read_double();
/*      */     } catch (MARSHAL localMARSHAL) {
/*  763 */       handleOptionalDataMarshalException(localMARSHAL, false);
/*  764 */       throw localMARSHAL;
/*      */     } catch (Error localError) {
/*  766 */       IOException localIOException = new IOException(localError.getMessage());
/*  767 */       localIOException.initCause(localError);
/*  768 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final float readFloat() throws IOException {
/*      */     try {
/*  774 */       this.readObjectState.readData(this);
/*      */ 
/*  776 */       return this.orbStream.read_float();
/*      */     } catch (MARSHAL localMARSHAL) {
/*  778 */       handleOptionalDataMarshalException(localMARSHAL, false);
/*  779 */       throw localMARSHAL;
/*      */     } catch (Error localError) {
/*  781 */       IOException localIOException = new IOException(localError.getMessage());
/*  782 */       localIOException.initCause(localError);
/*  783 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final void readFully(byte[] paramArrayOfByte)
/*      */     throws IOException
/*      */   {
/*  790 */     readFully(paramArrayOfByte, 0, paramArrayOfByte.length);
/*      */   }
/*      */ 
/*      */   public final void readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException
/*      */   {
/*      */     try {
/*  796 */       this.readObjectState.readData(this);
/*      */ 
/*  798 */       this.orbStream.read_octet_array(paramArrayOfByte, paramInt1, paramInt2);
/*      */     } catch (MARSHAL localMARSHAL) {
/*  800 */       handleOptionalDataMarshalException(localMARSHAL, false);
/*      */ 
/*  802 */       throw localMARSHAL;
/*      */     } catch (Error localError) {
/*  804 */       IOException localIOException = new IOException(localError.getMessage());
/*  805 */       localIOException.initCause(localError);
/*  806 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final int readInt() throws IOException {
/*      */     try {
/*  812 */       this.readObjectState.readData(this);
/*      */ 
/*  814 */       return this.orbStream.read_long();
/*      */     } catch (MARSHAL localMARSHAL) {
/*  816 */       handleOptionalDataMarshalException(localMARSHAL, false);
/*  817 */       throw localMARSHAL;
/*      */     } catch (Error localError) {
/*  819 */       IOException localIOException = new IOException(localError.getMessage());
/*  820 */       localIOException.initCause(localError);
/*  821 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final String readLine() throws IOException
/*      */   {
/*  827 */     throw new IOException("Method readLine not supported");
/*      */   }
/*      */ 
/*      */   public final long readLong() throws IOException {
/*      */     try {
/*  832 */       this.readObjectState.readData(this);
/*      */ 
/*  834 */       return this.orbStream.read_longlong();
/*      */     } catch (MARSHAL localMARSHAL) {
/*  836 */       handleOptionalDataMarshalException(localMARSHAL, false);
/*  837 */       throw localMARSHAL;
/*      */     } catch (Error localError) {
/*  839 */       IOException localIOException = new IOException(localError.getMessage());
/*  840 */       localIOException.initCause(localError);
/*  841 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final short readShort() throws IOException {
/*      */     try {
/*  847 */       this.readObjectState.readData(this);
/*      */ 
/*  849 */       return this.orbStream.read_short();
/*      */     } catch (MARSHAL localMARSHAL) {
/*  851 */       handleOptionalDataMarshalException(localMARSHAL, false);
/*  852 */       throw localMARSHAL;
/*      */     } catch (Error localError) {
/*  854 */       IOException localIOException = new IOException(localError.getMessage());
/*  855 */       localIOException.initCause(localError);
/*  856 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected final void readStreamHeader() throws IOException, StreamCorruptedException
/*      */   {
/*      */   }
/*      */ 
/*      */   public final int readUnsignedByte() throws IOException {
/*      */     try {
/*  866 */       this.readObjectState.readData(this);
/*      */ 
/*  868 */       return this.orbStream.read_octet() << 0 & 0xFF;
/*      */     } catch (MARSHAL localMARSHAL) {
/*  870 */       handleOptionalDataMarshalException(localMARSHAL, false);
/*  871 */       throw localMARSHAL;
/*      */     } catch (Error localError) {
/*  873 */       IOException localIOException = new IOException(localError.getMessage());
/*  874 */       localIOException.initCause(localError);
/*  875 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public final int readUnsignedShort() throws IOException {
/*      */     try {
/*  881 */       this.readObjectState.readData(this);
/*      */ 
/*  883 */       return this.orbStream.read_ushort() << 0 & 0xFFFF;
/*      */     } catch (MARSHAL localMARSHAL) {
/*  885 */       handleOptionalDataMarshalException(localMARSHAL, false);
/*  886 */       throw localMARSHAL;
/*      */     } catch (Error localError) {
/*  888 */       IOException localIOException = new IOException(localError.getMessage());
/*  889 */       localIOException.initCause(localError);
/*  890 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   protected String internalReadUTF(org.omg.CORBA.portable.InputStream paramInputStream)
/*      */   {
/*  902 */     return paramInputStream.read_wstring();
/*      */   }
/*      */ 
/*      */   public final String readUTF() throws IOException {
/*      */     try {
/*  907 */       this.readObjectState.readData(this);
/*      */ 
/*  909 */       return internalReadUTF(this.orbStream);
/*      */     } catch (MARSHAL localMARSHAL) {
/*  911 */       handleOptionalDataMarshalException(localMARSHAL, false);
/*  912 */       throw localMARSHAL;
/*      */     } catch (Error localError) {
/*  914 */       IOException localIOException = new IOException(localError.getMessage());
/*  915 */       localIOException.initCause(localError);
/*  916 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void handleOptionalDataMarshalException(MARSHAL paramMARSHAL, boolean paramBoolean)
/*      */     throws IOException
/*      */   {
/*  937 */     if (paramMARSHAL.minor == 1330446344)
/*      */     {
/*      */       java.lang.Object localObject;
/*  942 */       if (!paramBoolean)
/*  943 */         localObject = new EOFException("No more optional data");
/*      */       else {
/*  945 */         localObject = createOptionalDataException();
/*      */       }
/*  947 */       ((IOException)localObject).initCause(paramMARSHAL);
/*      */ 
/*  949 */       setState(IN_READ_OBJECT_NO_MORE_OPT_DATA);
/*      */ 
/*  951 */       throw ((Throwable)localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   public final synchronized void registerValidation(ObjectInputValidation paramObjectInputValidation, int paramInt)
/*      */     throws NotActiveException, InvalidObjectException
/*      */   {
/*  959 */     throw new Error("Method registerValidation not supported");
/*      */   }
/*      */ 
/*      */   protected final Class resolveClass(ObjectStreamClass paramObjectStreamClass)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*  965 */     throw new IOException("Method resolveClass not supported");
/*      */   }
/*      */ 
/*      */   protected final java.lang.Object resolveObject(java.lang.Object paramObject) throws IOException
/*      */   {
/*  970 */     throw new IOException("Method resolveObject not supported");
/*      */   }
/*      */ 
/*      */   public final int skipBytes(int paramInt) throws IOException {
/*      */     try {
/*  975 */       this.readObjectState.readData(this);
/*      */ 
/*  977 */       byte[] arrayOfByte = new byte[paramInt];
/*  978 */       this.orbStream.read_octet_array(arrayOfByte, 0, paramInt);
/*  979 */       return paramInt;
/*      */     } catch (MARSHAL localMARSHAL) {
/*  981 */       handleOptionalDataMarshalException(localMARSHAL, false);
/*      */ 
/*  983 */       throw localMARSHAL;
/*      */     } catch (Error localError) {
/*  985 */       IOException localIOException = new IOException(localError.getMessage());
/*  986 */       localIOException.initCause(localError);
/*  987 */       throw localIOException;
/*      */     }
/*      */   }
/*      */ 
/*      */   private synchronized java.lang.Object inputObject(Class paramClass, String paramString, CodeBase paramCodeBase, int paramInt)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1002 */     this.currentClassDesc = ObjectStreamClass.lookup(paramClass);
/* 1003 */     this.currentClass = this.currentClassDesc.forClass();
/*      */ 
/* 1005 */     if (this.currentClass == null)
/*      */     {
/* 1007 */       throw new ClassNotFoundException(this.currentClassDesc.getName());
/*      */     }
/*      */     try
/*      */     {
/*      */       java.lang.Object localObject1;
/* 1015 */       if (Enum.class.isAssignableFrom(paramClass)) {
/* 1016 */         int i = this.orbStream.read_long();
/* 1017 */         localObject1 = (String)this.orbStream.read_value(String.class);
/* 1018 */         return Enum.valueOf(paramClass, (String)localObject1);
/* 1019 */       }if (this.currentClassDesc.isExternalizable()) {
/*      */         try {
/* 1021 */           this.currentObject = (this.currentClass == null ? null : this.currentClassDesc.newInstance());
/*      */ 
/* 1023 */           if (this.currentObject != null)
/*      */           {
/* 1028 */             this.activeRecursionMgr.addObject(paramInt, this.currentObject);
/*      */ 
/* 1031 */             readFormatVersion();
/*      */ 
/* 1033 */             Externalizable localExternalizable = (Externalizable)this.currentObject;
/* 1034 */             localExternalizable.readExternal(this);
/*      */           }
/*      */         } catch (InvocationTargetException localInvocationTargetException1) {
/* 1037 */           localObject1 = new InvalidClassException(this.currentClass.getName(), "InvocationTargetException accessing no-arg constructor");
/*      */ 
/* 1040 */           ((InvalidClassException)localObject1).initCause(localInvocationTargetException1);
/* 1041 */           throw ((Throwable)localObject1);
/*      */         } catch (UnsupportedOperationException localUnsupportedOperationException1) {
/* 1043 */           localObject1 = new InvalidClassException(this.currentClass.getName(), "UnsupportedOperationException accessing no-arg constructor");
/*      */ 
/* 1046 */           ((InvalidClassException)localObject1).initCause(localUnsupportedOperationException1);
/* 1047 */           throw ((Throwable)localObject1);
/*      */         } catch (InstantiationException localInstantiationException1) {
/* 1049 */           localObject1 = new InvalidClassException(this.currentClass.getName(), "InstantiationException accessing no-arg constructor");
/*      */ 
/* 1052 */           ((InvalidClassException)localObject1).initCause(localInstantiationException1);
/* 1053 */           throw ((Throwable)localObject1);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1061 */         ObjectStreamClass localObjectStreamClass = this.currentClassDesc;
/* 1062 */         localObject1 = this.currentClass;
/*      */ 
/* 1064 */         int j = this.spClass;
/*      */ 
/* 1095 */         localObjectStreamClass = this.currentClassDesc; localObject1 = this.currentClass;
/*      */         java.lang.Object localObject3;
/* 1097 */         for (; (localObjectStreamClass != null) && (localObjectStreamClass.isSerializable()); 
/* 1097 */           localObjectStreamClass = localObjectStreamClass.getSuperclass())
/*      */         {
/* 1106 */           Class localClass = localObjectStreamClass.forClass();
/*      */ 
/* 1108 */           for (localObject3 = localObject1; (localObject3 != null) && 
/* 1109 */             (localClass != localObject3); localObject3 = ((Class)localObject3).getSuperclass());
/* 1122 */           this.spClass += 1;
/* 1123 */           if (this.spClass >= this.classes.length) {
/* 1124 */             int k = this.classes.length * 2;
/* 1125 */             Class[] arrayOfClass = new Class[k];
/* 1126 */             ObjectStreamClass[] arrayOfObjectStreamClass = new ObjectStreamClass[k];
/*      */ 
/* 1128 */             System.arraycopy(this.classes, 0, arrayOfClass, 0, this.classes.length);
/*      */ 
/* 1131 */             System.arraycopy(this.classdesc, 0, arrayOfObjectStreamClass, 0, this.classes.length);
/*      */ 
/* 1135 */             this.classes = arrayOfClass;
/* 1136 */             this.classdesc = arrayOfObjectStreamClass;
/*      */           }
/*      */ 
/* 1139 */           if (localObject3 == null)
/*      */           {
/* 1144 */             this.classdesc[this.spClass] = localObjectStreamClass;
/* 1145 */             this.classes[this.spClass] = null;
/*      */           }
/*      */           else
/*      */           {
/* 1152 */             this.classdesc[this.spClass] = localObjectStreamClass;
/* 1153 */             this.classes[this.spClass] = localObject3;
/* 1154 */             localObject1 = ((Class)localObject3).getSuperclass();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/* 1163 */           this.currentObject = (this.currentClass == null ? null : this.currentClassDesc.newInstance());
/*      */ 
/* 1169 */           this.activeRecursionMgr.addObject(paramInt, this.currentObject);
/*      */         } catch (InvocationTargetException localInvocationTargetException2) {
/* 1171 */           localObject3 = new InvalidClassException(this.currentClass.getName(), "InvocationTargetException accessing no-arg constructor");
/*      */ 
/* 1174 */           ((InvalidClassException)localObject3).initCause(localInvocationTargetException2);
/* 1175 */           throw ((Throwable)localObject3);
/*      */         } catch (UnsupportedOperationException localUnsupportedOperationException2) {
/* 1177 */           localObject3 = new InvalidClassException(this.currentClass.getName(), "UnsupportedOperationException accessing no-arg constructor");
/*      */ 
/* 1180 */           ((InvalidClassException)localObject3).initCause(localUnsupportedOperationException2);
/* 1181 */           throw ((Throwable)localObject3);
/*      */         } catch (InstantiationException localInstantiationException2) {
/* 1183 */           localObject3 = new InvalidClassException(this.currentClass.getName(), "InstantiationException accessing no-arg constructor");
/*      */ 
/* 1186 */           ((InvalidClassException)localObject3).initCause(localInstantiationException2);
/* 1187 */           throw ((Throwable)localObject3);
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/* 1198 */           for (this.spClass = this.spClass; this.spClass > j; this.spClass -= 1)
/*      */           {
/* 1202 */             this.currentClassDesc = this.classdesc[this.spClass];
/* 1203 */             this.currentClass = this.classes[this.spClass];
/*      */             java.lang.Object localObject2;
/* 1204 */             if (this.classes[this.spClass] != null)
/*      */             {
/* 1209 */               localObject2 = this.readObjectState;
/* 1210 */               setState(DEFAULT_STATE);
/*      */               try
/*      */               {
/* 1215 */                 if (this.currentClassDesc.hasWriteObject())
/*      */                 {
/* 1218 */                   readFormatVersion();
/*      */ 
/* 1221 */                   boolean bool = readBoolean();
/*      */ 
/* 1223 */                   this.readObjectState.beginUnmarshalCustomValue(this, bool, this.currentClassDesc.readObjectMethod != null);
/*      */                 }
/* 1228 */                 else if (this.currentClassDesc.hasReadObject()) {
/* 1229 */                   setState(IN_READ_OBJECT_REMOTE_NOT_CUSTOM_MARSHALED);
/*      */                 }
/*      */ 
/* 1232 */                 if ((!invokeObjectReader(this.currentClassDesc, this.currentObject, this.currentClass)) || (this.readObjectState == IN_READ_OBJECT_DEFAULTS_SENT))
/*      */                 {
/* 1238 */                   ObjectStreamField[] arrayOfObjectStreamField = this.currentClassDesc.getFieldsNoCopy();
/*      */ 
/* 1240 */                   if (arrayOfObjectStreamField.length > 0) {
/* 1241 */                     inputClassFields(this.currentObject, this.currentClass, arrayOfObjectStreamField, paramCodeBase);
/*      */                   }
/*      */                 }
/*      */ 
/* 1245 */                 if (this.currentClassDesc.hasWriteObject())
/* 1246 */                   this.readObjectState.endUnmarshalCustomValue(this);
/*      */               }
/*      */               finally {
/* 1249 */                 setState((InputStreamHook.ReadObjectState)localObject2);
/*      */               }
/*      */ 
/*      */             }
/*      */             else
/*      */             {
/* 1260 */               localObject2 = this.currentClassDesc.getFieldsNoCopy();
/*      */ 
/* 1262 */               if (localObject2.length > 0) {
/* 1263 */                 inputClassFields(null, this.currentClass, (ObjectStreamField[])localObject2, paramCodeBase);
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/*      */         }
/*      */         finally
/*      */         {
/* 1271 */           this.spClass = j;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */     finally
/*      */     {
/* 1279 */       this.activeRecursionMgr.removeObject(paramInt);
/*      */     }
/*      */ 
/* 1282 */     return this.currentObject;
/*      */   }
/*      */ 
/*      */   private Vector getOrderedDescriptions(String paramString, CodeBase paramCodeBase)
/*      */   {
/* 1290 */     Vector localVector = new Vector();
/*      */ 
/* 1292 */     if (paramCodeBase == null) {
/* 1293 */       return localVector;
/*      */     }
/*      */ 
/* 1296 */     FullValueDescription localFullValueDescription = paramCodeBase.meta(paramString);
/* 1297 */     while (localFullValueDescription != null) {
/* 1298 */       localVector.insertElementAt(localFullValueDescription, 0);
/* 1299 */       if ((localFullValueDescription.base_value != null) && (!"".equals(localFullValueDescription.base_value)))
/* 1300 */         localFullValueDescription = paramCodeBase.meta(localFullValueDescription.base_value);
/*      */       else {
/* 1302 */         return localVector;
/*      */       }
/*      */     }
/* 1305 */     return localVector;
/*      */   }
/*      */ 
/*      */   private synchronized java.lang.Object inputObjectUsingFVD(Class paramClass, String paramString, CodeBase paramCodeBase, int paramInt)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1326 */     int i = this.spClass;
/*      */     try
/*      */     {
/* 1333 */       ObjectStreamClass localObjectStreamClass = this.currentClassDesc = ObjectStreamClass.lookup(paramClass);
/* 1334 */       Class localClass1 = this.currentClass = paramClass;
/*      */       java.lang.Object localObject2;
/*      */       java.lang.Object localObject1;
/* 1341 */       if (this.currentClassDesc.isExternalizable()) {
/*      */         try {
/* 1343 */           this.currentObject = (this.currentClass == null ? null : this.currentClassDesc.newInstance());
/*      */ 
/* 1345 */           if (this.currentObject != null)
/*      */           {
/* 1349 */             this.activeRecursionMgr.addObject(paramInt, this.currentObject);
/*      */ 
/* 1352 */             readFormatVersion();
/*      */ 
/* 1354 */             Externalizable localExternalizable = (Externalizable)this.currentObject;
/* 1355 */             localExternalizable.readExternal(this);
/*      */           }
/*      */         } catch (InvocationTargetException localInvocationTargetException1) {
/* 1358 */           localObject2 = new InvalidClassException(this.currentClass.getName(), "InvocationTargetException accessing no-arg constructor");
/*      */ 
/* 1361 */           ((InvalidClassException)localObject2).initCause(localInvocationTargetException1);
/* 1362 */           throw ((Throwable)localObject2);
/*      */         } catch (UnsupportedOperationException localUnsupportedOperationException1) {
/* 1364 */           localObject2 = new InvalidClassException(this.currentClass.getName(), "UnsupportedOperationException accessing no-arg constructor");
/*      */ 
/* 1367 */           ((InvalidClassException)localObject2).initCause(localUnsupportedOperationException1);
/* 1368 */           throw ((Throwable)localObject2);
/*      */         } catch (InstantiationException localInstantiationException1) {
/* 1370 */           localObject2 = new InvalidClassException(this.currentClass.getName(), "InstantiationException accessing no-arg constructor");
/*      */ 
/* 1373 */           ((InvalidClassException)localObject2).initCause(localInstantiationException1);
/* 1374 */           throw ((Throwable)localObject2);
/*      */         }
/*      */ 
/*      */       }
/*      */       else
/*      */       {
/* 1381 */         localObjectStreamClass = this.currentClassDesc; localClass1 = this.currentClass;
/*      */         java.lang.Object localObject3;
/* 1384 */         for (; (localObjectStreamClass != null) && (localObjectStreamClass.isSerializable()); 
/* 1384 */           localObjectStreamClass = localObjectStreamClass.getSuperclass())
/*      */         {
/* 1393 */           Class localClass2 = localObjectStreamClass.forClass();
/*      */ 
/* 1395 */           for (localObject2 = localClass1; (localObject2 != null) && 
/* 1396 */             (localClass2 != localObject2); localObject2 = ((Class)localObject2).getSuperclass());
/* 1409 */           this.spClass += 1;
/* 1410 */           if (this.spClass >= this.classes.length) {
/* 1411 */             int j = this.classes.length * 2;
/* 1412 */             localObject3 = new Class[j];
/* 1413 */             ObjectStreamClass[] arrayOfObjectStreamClass = new ObjectStreamClass[j];
/*      */ 
/* 1415 */             System.arraycopy(this.classes, 0, localObject3, 0, this.classes.length);
/*      */ 
/* 1418 */             System.arraycopy(this.classdesc, 0, arrayOfObjectStreamClass, 0, this.classes.length);
/*      */ 
/* 1422 */             this.classes = ((Class[])localObject3);
/* 1423 */             this.classdesc = arrayOfObjectStreamClass;
/*      */           }
/*      */ 
/* 1426 */           if (localObject2 == null)
/*      */           {
/* 1431 */             this.classdesc[this.spClass] = localObjectStreamClass;
/* 1432 */             this.classes[this.spClass] = null;
/*      */           }
/*      */           else
/*      */           {
/* 1439 */             this.classdesc[this.spClass] = localObjectStreamClass;
/* 1440 */             this.classes[this.spClass] = localObject2;
/* 1441 */             localClass1 = ((Class)localObject2).getSuperclass();
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */         try
/*      */         {
/* 1448 */           this.currentObject = (this.currentClass == null ? null : this.currentClassDesc.newInstance());
/*      */ 
/* 1454 */           this.activeRecursionMgr.addObject(paramInt, this.currentObject);
/*      */         } catch (InvocationTargetException localInvocationTargetException2) {
/* 1456 */           localObject2 = new InvalidClassException(this.currentClass.getName(), "InvocationTargetException accessing no-arg constructor");
/*      */ 
/* 1459 */           ((InvalidClassException)localObject2).initCause(localInvocationTargetException2);
/* 1460 */           throw ((Throwable)localObject2);
/*      */         } catch (UnsupportedOperationException localUnsupportedOperationException2) {
/* 1462 */           localObject2 = new InvalidClassException(this.currentClass.getName(), "UnsupportedOperationException accessing no-arg constructor");
/*      */ 
/* 1465 */           ((InvalidClassException)localObject2).initCause(localUnsupportedOperationException2);
/* 1466 */           throw ((Throwable)localObject2);
/*      */         } catch (InstantiationException localInstantiationException2) {
/* 1468 */           localObject2 = new InvalidClassException(this.currentClass.getName(), "InstantiationException accessing no-arg constructor");
/*      */ 
/* 1471 */           ((InvalidClassException)localObject2).initCause(localInstantiationException2);
/* 1472 */           throw ((Throwable)localObject2);
/*      */         }
/*      */ 
/* 1475 */         localObject1 = getOrderedDescriptions(paramString, paramCodeBase).elements();
/*      */ 
/* 1477 */         while ((((Enumeration)localObject1).hasMoreElements()) && (this.spClass > i)) {
/* 1478 */           localObject2 = (FullValueDescription)((Enumeration)localObject1).nextElement();
/*      */ 
/* 1480 */           String str = this.vhandler.getClassName(((FullValueDescription)localObject2).id);
/* 1481 */           localObject3 = this.vhandler.getClassName(this.vhandler.getRMIRepositoryID(this.currentClass));
/*      */ 
/* 1483 */           while ((this.spClass > i) && (!str.equals(localObject3)))
/*      */           {
/* 1485 */             int k = findNextClass(str, this.classes, this.spClass, i);
/* 1486 */             if (k != -1) {
/* 1487 */               this.spClass = k;
/* 1488 */               localClass1 = this.currentClass = this.classes[this.spClass];
/* 1489 */               localObject3 = this.vhandler.getClassName(this.vhandler.getRMIRepositoryID(this.currentClass));
/*      */             }
/*      */             else
/*      */             {
/* 1496 */               if (((FullValueDescription)localObject2).is_custom)
/*      */               {
/* 1498 */                 readFormatVersion();
/* 1499 */                 boolean bool1 = readBoolean();
/*      */ 
/* 1501 */                 if (bool1) {
/* 1502 */                   inputClassFields(null, null, null, ((FullValueDescription)localObject2).members, paramCodeBase);
/*      */                 }
/* 1504 */                 if (getStreamFormatVersion() == 2)
/*      */                 {
/* 1506 */                   ((ValueInputStream)getOrbStream()).start_value();
/* 1507 */                   ((ValueInputStream)getOrbStream()).end_value();
/*      */                 }
/*      */ 
/*      */               }
/*      */               else
/*      */               {
/* 1516 */                 inputClassFields(null, this.currentClass, null, ((FullValueDescription)localObject2).members, paramCodeBase);
/*      */               }
/*      */ 
/* 1519 */               if (((Enumeration)localObject1).hasMoreElements()) {
/* 1520 */                 localObject2 = (FullValueDescription)((Enumeration)localObject1).nextElement();
/* 1521 */                 str = this.vhandler.getClassName(((FullValueDescription)localObject2).id);
/*      */               } else {
/* 1523 */                 return this.currentObject;
/*      */               }
/*      */             }
/*      */           }
/* 1527 */           localObjectStreamClass = this.currentClassDesc = ObjectStreamClass.lookup(this.currentClass);
/*      */ 
/* 1529 */           if (!((String)localObject3).equals("java.lang.Object"))
/*      */           {
/* 1536 */             InputStreamHook.ReadObjectState localReadObjectState = this.readObjectState;
/* 1537 */             setState(DEFAULT_STATE);
/*      */             try
/*      */             {
/* 1541 */               if (((FullValueDescription)localObject2).is_custom)
/*      */               {
/* 1544 */                 readFormatVersion();
/*      */ 
/* 1547 */                 bool2 = readBoolean();
/*      */ 
/* 1549 */                 this.readObjectState.beginUnmarshalCustomValue(this, bool2, this.currentClassDesc.readObjectMethod != null);
/*      */               }
/*      */ 
/* 1555 */               boolean bool2 = false;
/*      */               try
/*      */               {
/* 1561 */                 if ((!((FullValueDescription)localObject2).is_custom) && (this.currentClassDesc.hasReadObject())) {
/* 1562 */                   setState(IN_READ_OBJECT_REMOTE_NOT_CUSTOM_MARSHALED);
/*      */                 }
/*      */ 
/* 1567 */                 this.defaultReadObjectFVDMembers = ((FullValueDescription)localObject2).members;
/* 1568 */                 bool2 = invokeObjectReader(this.currentClassDesc, this.currentObject, this.currentClass);
/*      */               }
/*      */               finally
/*      */               {
/* 1573 */                 this.defaultReadObjectFVDMembers = null;
/*      */               }
/*      */ 
/* 1579 */               if ((!bool2) || (this.readObjectState == IN_READ_OBJECT_DEFAULTS_SENT)) {
/* 1580 */                 inputClassFields(this.currentObject, this.currentClass, localObjectStreamClass, ((FullValueDescription)localObject2).members, paramCodeBase);
/*      */               }
/* 1582 */               if (((FullValueDescription)localObject2).is_custom)
/* 1583 */                 this.readObjectState.endUnmarshalCustomValue(this);
/*      */             }
/*      */             finally {
/* 1586 */               setState(localReadObjectState);
/*      */             }
/*      */ 
/* 1589 */             localClass1 = this.currentClass = this.classes[(--this.spClass)];
/*      */           }
/*      */           else
/*      */           {
/* 1596 */             inputClassFields(null, this.currentClass, null, ((FullValueDescription)localObject2).members, paramCodeBase);
/*      */ 
/* 1598 */             while (((Enumeration)localObject1).hasMoreElements()) {
/* 1599 */               localObject2 = (FullValueDescription)((Enumeration)localObject1).nextElement();
/*      */ 
/* 1601 */               if (((FullValueDescription)localObject2).is_custom)
/* 1602 */                 skipCustomUsingFVD(((FullValueDescription)localObject2).members, paramCodeBase);
/*      */               else {
/* 1604 */                 inputClassFields(null, this.currentClass, null, ((FullValueDescription)localObject2).members, paramCodeBase);
/*      */               }
/*      */             }
/*      */           }
/*      */         }
/*      */ 
/* 1610 */         while (((Enumeration)localObject1).hasMoreElements())
/*      */         {
/* 1612 */           localObject2 = (FullValueDescription)((Enumeration)localObject1).nextElement();
/* 1613 */           if (((FullValueDescription)localObject2).is_custom)
/* 1614 */             skipCustomUsingFVD(((FullValueDescription)localObject2).members, paramCodeBase);
/*      */           else {
/* 1616 */             throwAwayData(((FullValueDescription)localObject2).members, paramCodeBase);
/*      */           }
/*      */         }
/*      */       }
/* 1620 */       return this.currentObject;
/*      */     }
/*      */     finally
/*      */     {
/* 1624 */       this.spClass = i;
/*      */ 
/* 1630 */       this.activeRecursionMgr.removeObject(paramInt);
/*      */     }
/*      */   }
/*      */ 
/*      */   private java.lang.Object skipObjectUsingFVD(String paramString, CodeBase paramCodeBase)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1650 */     Enumeration localEnumeration = getOrderedDescriptions(paramString, paramCodeBase).elements();
/*      */ 
/* 1652 */     while (localEnumeration.hasMoreElements()) {
/* 1653 */       FullValueDescription localFullValueDescription = (FullValueDescription)localEnumeration.nextElement();
/* 1654 */       String str = this.vhandler.getClassName(localFullValueDescription.id);
/*      */ 
/* 1656 */       if (!str.equals("java.lang.Object")) {
/* 1657 */         if (localFullValueDescription.is_custom)
/*      */         {
/* 1659 */           readFormatVersion();
/*      */ 
/* 1661 */           boolean bool = readBoolean();
/*      */ 
/* 1663 */           if (bool) {
/* 1664 */             inputClassFields(null, null, null, localFullValueDescription.members, paramCodeBase);
/*      */           }
/* 1666 */           if (getStreamFormatVersion() == 2)
/*      */           {
/* 1668 */             ((ValueInputStream)getOrbStream()).start_value();
/* 1669 */             ((ValueInputStream)getOrbStream()).end_value();
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/* 1678 */           inputClassFields(null, null, null, localFullValueDescription.members, paramCodeBase);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1683 */     return null;
/*      */   }
/*      */ 
/*      */   private int findNextClass(String paramString, Class[] paramArrayOfClass, int paramInt1, int paramInt2)
/*      */   {
/* 1691 */     for (int i = paramInt1; i > paramInt2; i--) {
/* 1692 */       if (paramString.equals(paramArrayOfClass[i].getName())) {
/* 1693 */         return i;
/*      */       }
/*      */     }
/*      */ 
/* 1697 */     return -1;
/*      */   }
/*      */ 
/*      */   private boolean invokeObjectReader(ObjectStreamClass paramObjectStreamClass, java.lang.Object paramObject, Class paramClass)
/*      */     throws InvalidClassException, StreamCorruptedException, ClassNotFoundException, IOException
/*      */   {
/* 1709 */     if (paramObjectStreamClass.readObjectMethod == null) {
/* 1710 */       return false;
/*      */     }
/*      */     try
/*      */     {
/* 1714 */       paramObjectStreamClass.readObjectMethod.invoke(paramObject, this.readObjectArgList);
/* 1715 */       return true;
/*      */     } catch (InvocationTargetException localInvocationTargetException) {
/* 1717 */       Throwable localThrowable = localInvocationTargetException.getTargetException();
/* 1718 */       if ((localThrowable instanceof ClassNotFoundException))
/* 1719 */         throw ((ClassNotFoundException)localThrowable);
/* 1720 */       if ((localThrowable instanceof IOException))
/* 1721 */         throw ((IOException)localThrowable);
/* 1722 */       if ((localThrowable instanceof RuntimeException))
/* 1723 */         throw ((RuntimeException)localThrowable);
/* 1724 */       if ((localThrowable instanceof Error)) {
/* 1725 */         throw ((Error)localThrowable);
/*      */       }
/*      */ 
/* 1728 */       throw new Error("internal error"); } catch (IllegalAccessException localIllegalAccessException) {
/*      */     }
/* 1730 */     return false;
/*      */   }
/*      */ 
/*      */   private void resetStream()
/*      */     throws IOException
/*      */   {
/*      */     int i;
/* 1739 */     if (this.classes == null)
/* 1740 */       this.classes = new Class[20];
/*      */     else {
/* 1742 */       for (i = 0; i < this.classes.length; i++)
/* 1743 */         this.classes[i] = null;
/*      */     }
/* 1745 */     if (this.classdesc == null)
/* 1746 */       this.classdesc = new ObjectStreamClass[20];
/*      */     else {
/* 1748 */       for (i = 0; i < this.classdesc.length; i++)
/* 1749 */         this.classdesc[i] = null;
/*      */     }
/* 1751 */     this.spClass = 0;
/*      */ 
/* 1753 */     if (this.callbacks != null)
/* 1754 */       this.callbacks.setSize(0);
/*      */   }
/*      */ 
/*      */   private void inputPrimitiveField(java.lang.Object paramObject, Class paramClass, ObjectStreamField paramObjectStreamField)
/*      */     throws InvalidClassException, IOException
/*      */   {
/*      */     try
/*      */     {
/* 1768 */       switch (paramObjectStreamField.getTypeCode()) {
/*      */       case 'B':
/* 1770 */         byte b = this.orbStream.read_octet();
/* 1771 */         bridge.putByte(paramObject, paramObjectStreamField.getFieldID(), b);
/*      */ 
/* 1773 */         break;
/*      */       case 'Z':
/* 1775 */         boolean bool = this.orbStream.read_boolean();
/* 1776 */         bridge.putBoolean(paramObject, paramObjectStreamField.getFieldID(), bool);
/*      */ 
/* 1778 */         break;
/*      */       case 'C':
/* 1780 */         char c = this.orbStream.read_wchar();
/* 1781 */         bridge.putChar(paramObject, paramObjectStreamField.getFieldID(), c);
/*      */ 
/* 1783 */         break;
/*      */       case 'S':
/* 1785 */         short s = this.orbStream.read_short();
/* 1786 */         bridge.putShort(paramObject, paramObjectStreamField.getFieldID(), s);
/*      */ 
/* 1788 */         break;
/*      */       case 'I':
/* 1790 */         int i = this.orbStream.read_long();
/* 1791 */         bridge.putInt(paramObject, paramObjectStreamField.getFieldID(), i);
/*      */ 
/* 1793 */         break;
/*      */       case 'J':
/* 1795 */         long l = this.orbStream.read_longlong();
/* 1796 */         bridge.putLong(paramObject, paramObjectStreamField.getFieldID(), l);
/*      */ 
/* 1798 */         break;
/*      */       case 'F':
/* 1800 */         float f = this.orbStream.read_float();
/* 1801 */         bridge.putFloat(paramObject, paramObjectStreamField.getFieldID(), f);
/*      */ 
/* 1803 */         break;
/*      */       case 'D':
/* 1805 */         double d = this.orbStream.read_double();
/* 1806 */         bridge.putDouble(paramObject, paramObjectStreamField.getFieldID(), d);
/*      */ 
/* 1808 */         break;
/*      */       case 'E':
/*      */       case 'G':
/*      */       case 'H':
/*      */       case 'K':
/*      */       case 'L':
/*      */       case 'M':
/*      */       case 'N':
/*      */       case 'O':
/*      */       case 'P':
/*      */       case 'Q':
/*      */       case 'R':
/*      */       case 'T':
/*      */       case 'U':
/*      */       case 'V':
/*      */       case 'W':
/*      */       case 'X':
/*      */       case 'Y':
/*      */       default:
/* 1811 */         throw new InvalidClassException(paramClass.getName());
/*      */       }
/*      */ 
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException)
/*      */     {
/* 1817 */       ClassCastException localClassCastException = new ClassCastException("Assigning instance of class " + paramObjectStreamField.getType().getName() + " to field " + this.currentClassDesc.getName() + '#' + paramObjectStreamField.getField().getName());
/*      */ 
/* 1822 */       localClassCastException.initCause(localIllegalArgumentException);
/* 1823 */       throw localClassCastException;
/*      */     }
/*      */   }
/*      */ 
/*      */   private java.lang.Object inputObjectField(ValueMember paramValueMember, CodeBase paramCodeBase)
/*      */     throws IndirectionException, ClassNotFoundException, IOException, StreamCorruptedException
/*      */   {
/* 1832 */     java.lang.Object localObject = null;
/* 1833 */     Class localClass = null;
/* 1834 */     String str1 = paramValueMember.id;
/*      */     try
/*      */     {
/* 1837 */       localClass = this.vhandler.getClassFromType(str1);
/*      */     }
/*      */     catch (ClassNotFoundException localClassNotFoundException) {
/* 1840 */       localClass = null;
/*      */     }
/*      */ 
/* 1843 */     String str2 = null;
/* 1844 */     if (localClass != null) {
/* 1845 */       str2 = ValueUtility.getSignature(paramValueMember);
/*      */     }
/* 1847 */     if ((str2 != null) && ((str2.equals("Ljava/lang/Object;")) || (str2.equals("Ljava/io/Serializable;")) || (str2.equals("Ljava/io/Externalizable;"))))
/*      */     {
/* 1850 */       localObject = Util.readAny(this.orbStream);
/*      */     }
/*      */     else
/*      */     {
/* 1860 */       int i = 2;
/*      */ 
/* 1862 */       if (!this.vhandler.isSequence(str1))
/*      */       {
/* 1864 */         if (paramValueMember.type.kind().value() == kRemoteTypeCode.kind().value())
/*      */         {
/* 1867 */           i = 0;
/*      */         }
/* 1884 */         else if ((localClass != null) && (localClass.isInterface()) && ((this.vhandler.isAbstractBase(localClass)) || (ObjectStreamClassCorbaExt.isAbstractInterface(localClass))))
/*      */         {
/* 1888 */           i = 1;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1897 */       switch (i) {
/*      */       case 0:
/* 1899 */         if (localClass != null)
/* 1900 */           localObject = Utility.readObjectAndNarrow(this.orbStream, localClass);
/*      */         else
/* 1902 */           localObject = this.orbStream.read_Object();
/* 1903 */         break;
/*      */       case 1:
/* 1905 */         if (localClass != null)
/* 1906 */           localObject = Utility.readAbstractAndNarrow(this.orbStream, localClass);
/*      */         else
/* 1908 */           localObject = this.orbStream.read_abstract_interface();
/* 1909 */         break;
/*      */       case 2:
/* 1911 */         if (localClass != null)
/* 1912 */           localObject = this.orbStream.read_value(localClass);
/*      */         else
/* 1914 */           localObject = this.orbStream.read_value();
/* 1915 */         break;
/*      */       default:
/* 1918 */         throw new StreamCorruptedException("Unknown callType: " + i);
/*      */       }
/*      */     }
/*      */ 
/* 1922 */     return localObject;
/*      */   }
/*      */ 
/*      */   private java.lang.Object inputObjectField(ObjectStreamField paramObjectStreamField)
/*      */     throws InvalidClassException, StreamCorruptedException, ClassNotFoundException, IndirectionException, IOException
/*      */   {
/* 1936 */     if (ObjectStreamClassCorbaExt.isAny(paramObjectStreamField.getTypeString())) {
/* 1937 */       return Util.readAny(this.orbStream);
/*      */     }
/*      */ 
/* 1940 */     java.lang.Object localObject1 = null;
/*      */ 
/* 1945 */     Class localClass1 = paramObjectStreamField.getType();
/* 1946 */     java.lang.Object localObject2 = localClass1;
/*      */ 
/* 1952 */     int i = 2;
/* 1953 */     int j = 0;
/*      */ 
/* 1955 */     if (localClass1.isInterface()) {
/* 1956 */       int k = 0;
/*      */ 
/* 1958 */       if (Remote.class.isAssignableFrom(localClass1))
/*      */       {
/* 1961 */         i = 0;
/*      */       }
/* 1963 */       else if (org.omg.CORBA.Object.class.isAssignableFrom(localClass1))
/*      */       {
/* 1966 */         i = 0;
/* 1967 */         k = 1;
/*      */       }
/* 1969 */       else if (this.vhandler.isAbstractBase(localClass1))
/*      */       {
/* 1972 */         i = 1;
/* 1973 */         k = 1;
/* 1974 */       } else if (ObjectStreamClassCorbaExt.isAbstractInterface(localClass1))
/*      */       {
/* 1977 */         i = 1;
/*      */       }
/*      */ 
/* 1980 */       if (k != 0)
/*      */         try {
/* 1982 */           String str1 = Util.getCodebase(localClass1);
/* 1983 */           String str2 = this.vhandler.createForAnyType(localClass1);
/* 1984 */           Class localClass2 = Utility.loadStubClass(str2, str1, localClass1);
/*      */ 
/* 1986 */           localObject2 = localClass2;
/*      */         } catch (ClassNotFoundException localClassNotFoundException) {
/* 1988 */           j = 1;
/*      */         }
/*      */       else {
/* 1991 */         j = 1;
/*      */       }
/*      */     }
/*      */ 
/* 1995 */     switch (i) {
/*      */     case 0:
/* 1997 */       if (j == 0)
/* 1998 */         localObject1 = this.orbStream.read_Object((Class)localObject2);
/*      */       else
/* 2000 */         localObject1 = Utility.readObjectAndNarrow(this.orbStream, (Class)localObject2);
/* 2001 */       break;
/*      */     case 1:
/* 2003 */       if (j == 0)
/* 2004 */         localObject1 = this.orbStream.read_abstract_interface((Class)localObject2);
/*      */       else
/* 2006 */         localObject1 = Utility.readAbstractAndNarrow(this.orbStream, (Class)localObject2);
/* 2007 */       break;
/*      */     case 2:
/* 2009 */       localObject1 = this.orbStream.read_value((Class)localObject2);
/* 2010 */       break;
/*      */     default:
/* 2013 */       throw new StreamCorruptedException("Unknown callType: " + i);
/*      */     }
/*      */ 
/* 2016 */     return localObject1;
/*      */   }
/*      */ 
/*      */   private final boolean mustUseRemoteValueMembers() {
/* 2020 */     return this.defaultReadObjectFVDMembers != null;
/*      */   }
/*      */ 
/*      */   void readFields(Map paramMap)
/*      */     throws InvalidClassException, StreamCorruptedException, ClassNotFoundException, IOException
/*      */   {
/* 2027 */     if (mustUseRemoteValueMembers())
/* 2028 */       inputRemoteMembersForReadFields(paramMap);
/*      */     else
/* 2030 */       inputCurrentClassFieldsForReadFields(paramMap);
/*      */   }
/*      */ 
/*      */   private final void inputRemoteMembersForReadFields(Map paramMap)
/*      */     throws InvalidClassException, StreamCorruptedException, ClassNotFoundException, IOException
/*      */   {
/* 2039 */     ValueMember[] arrayOfValueMember = this.defaultReadObjectFVDMembers;
/*      */     try
/*      */     {
/* 2043 */       for (int i = 0; i < arrayOfValueMember.length; i++)
/*      */       {
/* 2045 */         switch (arrayOfValueMember[i].type.kind().value())
/*      */         {
/*      */         case 10:
/* 2048 */           byte b = this.orbStream.read_octet();
/* 2049 */           paramMap.put(arrayOfValueMember[i].name, new Byte(b));
/* 2050 */           break;
/*      */         case 8:
/* 2052 */           boolean bool = this.orbStream.read_boolean();
/* 2053 */           paramMap.put(arrayOfValueMember[i].name, new Boolean(bool));
/* 2054 */           break;
/*      */         case 9:
/*      */         case 26:
/* 2062 */           char c = this.orbStream.read_wchar();
/* 2063 */           paramMap.put(arrayOfValueMember[i].name, new Character(c));
/* 2064 */           break;
/*      */         case 2:
/* 2066 */           short s = this.orbStream.read_short();
/* 2067 */           paramMap.put(arrayOfValueMember[i].name, new Short(s));
/* 2068 */           break;
/*      */         case 3:
/* 2070 */           int j = this.orbStream.read_long();
/* 2071 */           paramMap.put(arrayOfValueMember[i].name, new Integer(j));
/* 2072 */           break;
/*      */         case 23:
/* 2074 */           long l = this.orbStream.read_longlong();
/* 2075 */           paramMap.put(arrayOfValueMember[i].name, new Long(l));
/* 2076 */           break;
/*      */         case 6:
/* 2078 */           float f = this.orbStream.read_float();
/* 2079 */           paramMap.put(arrayOfValueMember[i].name, new Float(f));
/* 2080 */           break;
/*      */         case 7:
/* 2082 */           double d = this.orbStream.read_double();
/* 2083 */           paramMap.put(arrayOfValueMember[i].name, new Double(d));
/* 2084 */           break;
/*      */         case 14:
/*      */         case 29:
/*      */         case 30:
/* 2088 */           java.lang.Object localObject = null;
/*      */           try {
/* 2090 */             localObject = inputObjectField(arrayOfValueMember[i], this.cbSender);
/*      */           }
/*      */           catch (IndirectionException localIndirectionException)
/*      */           {
/* 2097 */             localObject = this.activeRecursionMgr.getObject(localIndirectionException.offset);
/*      */           }
/*      */ 
/* 2100 */           paramMap.put(arrayOfValueMember[i].name, localObject);
/* 2101 */           break;
/*      */         case 4:
/*      */         case 5:
/*      */         case 11:
/*      */         case 12:
/*      */         case 13:
/*      */         case 15:
/*      */         case 16:
/*      */         case 17:
/*      */         case 18:
/*      */         case 19:
/*      */         case 20:
/*      */         case 21:
/*      */         case 22:
/*      */         case 24:
/*      */         case 25:
/*      */         case 27:
/*      */         case 28:
/*      */         default:
/* 2104 */           throw new StreamCorruptedException("Unknown kind: " + arrayOfValueMember[i].type.kind().value());
/*      */         }
/*      */       }
/*      */     }
/*      */     catch (Throwable localThrowable) {
/* 2109 */       StreamCorruptedException localStreamCorruptedException = new StreamCorruptedException(localThrowable.getMessage());
/* 2110 */       localStreamCorruptedException.initCause(localThrowable);
/* 2111 */       throw localStreamCorruptedException;
/*      */     }
/*      */   }
/*      */ 
/*      */   private final void inputCurrentClassFieldsForReadFields(Map paramMap)
/*      */     throws InvalidClassException, StreamCorruptedException, ClassNotFoundException, IOException
/*      */   {
/* 2127 */     ObjectStreamField[] arrayOfObjectStreamField = this.currentClassDesc.getFieldsNoCopy();
/*      */ 
/* 2129 */     int i = arrayOfObjectStreamField.length - this.currentClassDesc.objFields;
/*      */ 
/* 2132 */     for (int j = 0; j < i; j++)
/*      */     {
/* 2134 */       switch (arrayOfObjectStreamField[j].getTypeCode()) {
/*      */       case 'B':
/* 2136 */         byte b = this.orbStream.read_octet();
/* 2137 */         paramMap.put(arrayOfObjectStreamField[j].getName(), new Byte(b));
/*      */ 
/* 2139 */         break;
/*      */       case 'Z':
/* 2141 */         boolean bool = this.orbStream.read_boolean();
/* 2142 */         paramMap.put(arrayOfObjectStreamField[j].getName(), new Boolean(bool));
/*      */ 
/* 2144 */         break;
/*      */       case 'C':
/* 2146 */         char c = this.orbStream.read_wchar();
/* 2147 */         paramMap.put(arrayOfObjectStreamField[j].getName(), new Character(c));
/*      */ 
/* 2149 */         break;
/*      */       case 'S':
/* 2151 */         short s = this.orbStream.read_short();
/* 2152 */         paramMap.put(arrayOfObjectStreamField[j].getName(), new Short(s));
/*      */ 
/* 2154 */         break;
/*      */       case 'I':
/* 2156 */         int k = this.orbStream.read_long();
/* 2157 */         paramMap.put(arrayOfObjectStreamField[j].getName(), new Integer(k));
/*      */ 
/* 2159 */         break;
/*      */       case 'J':
/* 2161 */         long l = this.orbStream.read_longlong();
/* 2162 */         paramMap.put(arrayOfObjectStreamField[j].getName(), new Long(l));
/*      */ 
/* 2164 */         break;
/*      */       case 'F':
/* 2166 */         float f = this.orbStream.read_float();
/* 2167 */         paramMap.put(arrayOfObjectStreamField[j].getName(), new Float(f));
/*      */ 
/* 2169 */         break;
/*      */       case 'D':
/* 2171 */         double d = this.orbStream.read_double();
/* 2172 */         paramMap.put(arrayOfObjectStreamField[j].getName(), new Double(d));
/*      */ 
/* 2174 */         break;
/*      */       case 'E':
/*      */       case 'G':
/*      */       case 'H':
/*      */       case 'K':
/*      */       case 'L':
/*      */       case 'M':
/*      */       case 'N':
/*      */       case 'O':
/*      */       case 'P':
/*      */       case 'Q':
/*      */       case 'R':
/*      */       case 'T':
/*      */       case 'U':
/*      */       case 'V':
/*      */       case 'W':
/*      */       case 'X':
/*      */       case 'Y':
/*      */       default:
/* 2177 */         throw new InvalidClassException(this.currentClassDesc.getName());
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 2182 */     if (this.currentClassDesc.objFields > 0)
/* 2183 */       for (j = i; j < arrayOfObjectStreamField.length; j++) {
/* 2184 */         java.lang.Object localObject = null;
/*      */         try {
/* 2186 */           localObject = inputObjectField(arrayOfObjectStreamField[j]);
/*      */         }
/*      */         catch (IndirectionException localIndirectionException)
/*      */         {
/* 2191 */           localObject = this.activeRecursionMgr.getObject(localIndirectionException.offset);
/*      */         }
/*      */ 
/* 2194 */         paramMap.put(arrayOfObjectStreamField[j].getName(), localObject);
/*      */       }
/*      */   }
/*      */ 
/*      */   private void inputClassFields(java.lang.Object paramObject, Class paramClass, ObjectStreamField[] paramArrayOfObjectStreamField, CodeBase paramCodeBase)
/*      */     throws InvalidClassException, StreamCorruptedException, ClassNotFoundException, IOException
/*      */   {
/* 2216 */     int i = paramArrayOfObjectStreamField.length - this.currentClassDesc.objFields;
/*      */     int j;
/* 2218 */     if (paramObject != null) {
/* 2219 */       for (j = 0; j < i; j++) {
/* 2220 */         if (paramArrayOfObjectStreamField[j].getField() != null)
/*      */         {
/* 2223 */           inputPrimitiveField(paramObject, paramClass, paramArrayOfObjectStreamField[j]);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 2228 */     if (this.currentClassDesc.objFields > 0)
/* 2229 */       for (j = i; j < paramArrayOfObjectStreamField.length; j++) {
/* 2230 */         java.lang.Object localObject = null;
/*      */         try
/*      */         {
/* 2233 */           localObject = inputObjectField(paramArrayOfObjectStreamField[j]);
/*      */         }
/*      */         catch (IndirectionException localIndirectionException)
/*      */         {
/* 2238 */           localObject = this.activeRecursionMgr.getObject(localIndirectionException.offset);
/*      */         }
/*      */ 
/* 2241 */         if ((paramObject != null) && (paramArrayOfObjectStreamField[j].getField() != null))
/*      */         {
/*      */           try
/*      */           {
/* 2246 */             Class localClass = paramArrayOfObjectStreamField[j].getClazz();
/* 2247 */             if ((localObject != null) && (!localClass.isInstance(localObject))) {
/* 2248 */               throw new IllegalArgumentException();
/*      */             }
/* 2250 */             bridge.putObject(paramObject, paramArrayOfObjectStreamField[j].getFieldID(), localObject);
/*      */           }
/*      */           catch (IllegalArgumentException localIllegalArgumentException) {
/* 2253 */             ClassCastException localClassCastException = new ClassCastException("Assigning instance of class " + localObject.getClass().getName() + " to field " + this.currentClassDesc.getName() + '#' + paramArrayOfObjectStreamField[j].getField().getName());
/*      */ 
/* 2259 */             localClassCastException.initCause(localIllegalArgumentException);
/* 2260 */             throw localClassCastException;
/*      */           }
/*      */         }
/*      */       }
/*      */   }
/*      */ 
/*      */   private void inputClassFields(java.lang.Object paramObject, Class paramClass, ObjectStreamClass paramObjectStreamClass, ValueMember[] paramArrayOfValueMember, CodeBase paramCodeBase)
/*      */     throws InvalidClassException, StreamCorruptedException, ClassNotFoundException, IOException
/*      */   {
/*      */     try
/*      */     {
/* 2281 */       for (int i = 0; i < paramArrayOfValueMember.length; i++)
/*      */         try {
/* 2283 */           switch (paramArrayOfValueMember[i].type.kind().value()) {
/*      */           case 10:
/* 2285 */             byte b = this.orbStream.read_octet();
/* 2286 */             if ((paramObject != null) && (paramObjectStreamClass.hasField(paramArrayOfValueMember[i])))
/* 2287 */               setByteField(paramObject, paramClass, paramArrayOfValueMember[i].name, b); break;
/*      */           case 8:
/* 2290 */             boolean bool = this.orbStream.read_boolean();
/* 2291 */             if ((paramObject != null) && (paramObjectStreamClass.hasField(paramArrayOfValueMember[i])))
/* 2292 */               setBooleanField(paramObject, paramClass, paramArrayOfValueMember[i].name, bool); break;
/*      */           case 9:
/*      */           case 26:
/* 2301 */             char c = this.orbStream.read_wchar();
/* 2302 */             if ((paramObject != null) && (paramObjectStreamClass.hasField(paramArrayOfValueMember[i])))
/* 2303 */               setCharField(paramObject, paramClass, paramArrayOfValueMember[i].name, c); break;
/*      */           case 2:
/* 2306 */             short s = this.orbStream.read_short();
/* 2307 */             if ((paramObject != null) && (paramObjectStreamClass.hasField(paramArrayOfValueMember[i])))
/* 2308 */               setShortField(paramObject, paramClass, paramArrayOfValueMember[i].name, s); break;
/*      */           case 3:
/* 2311 */             int j = this.orbStream.read_long();
/* 2312 */             if ((paramObject != null) && (paramObjectStreamClass.hasField(paramArrayOfValueMember[i])))
/* 2313 */               setIntField(paramObject, paramClass, paramArrayOfValueMember[i].name, j); break;
/*      */           case 23:
/* 2316 */             long l = this.orbStream.read_longlong();
/* 2317 */             if ((paramObject != null) && (paramObjectStreamClass.hasField(paramArrayOfValueMember[i])))
/* 2318 */               setLongField(paramObject, paramClass, paramArrayOfValueMember[i].name, l); break;
/*      */           case 6:
/* 2321 */             float f = this.orbStream.read_float();
/* 2322 */             if ((paramObject != null) && (paramObjectStreamClass.hasField(paramArrayOfValueMember[i])))
/* 2323 */               setFloatField(paramObject, paramClass, paramArrayOfValueMember[i].name, f); break;
/*      */           case 7:
/* 2326 */             double d = this.orbStream.read_double();
/* 2327 */             if ((paramObject != null) && (paramObjectStreamClass.hasField(paramArrayOfValueMember[i])))
/* 2328 */               setDoubleField(paramObject, paramClass, paramArrayOfValueMember[i].name, d); break;
/*      */           case 14:
/*      */           case 29:
/*      */           case 30:
/* 2333 */             java.lang.Object localObject = null;
/*      */             try {
/* 2335 */               localObject = inputObjectField(paramArrayOfValueMember[i], paramCodeBase);
/*      */             }
/*      */             catch (IndirectionException localIndirectionException)
/*      */             {
/* 2340 */               localObject = this.activeRecursionMgr.getObject(localIndirectionException.offset);
/*      */             }
/*      */ 
/* 2343 */             if (paramObject != null)
/*      */               try
/*      */               {
/* 2346 */                 if (paramObjectStreamClass.hasField(paramArrayOfValueMember[i])) {
/* 2347 */                   setObjectField(paramObject, paramClass, paramArrayOfValueMember[i].name, localObject);
/*      */                 }
/*      */ 
/*      */               }
/*      */               catch (IllegalArgumentException localIllegalArgumentException2)
/*      */               {
/* 2365 */                 ClassCastException localClassCastException2 = new ClassCastException("Assigning instance of class " + localObject.getClass().getName() + " to field " + paramArrayOfValueMember[i].name);
/*      */ 
/* 2367 */                 localClassCastException2.initCause(localIllegalArgumentException2);
/* 2368 */                 throw localClassCastException2; }  break;
/*      */           case 4:
/*      */           case 5:
/*      */           case 11:
/*      */           case 12:
/*      */           case 13:
/*      */           case 15:
/*      */           case 16:
/*      */           case 17:
/*      */           case 18:
/*      */           case 19:
/*      */           case 20:
/*      */           case 21:
/*      */           case 22:
/*      */           case 24:
/*      */           case 25:
/*      */           case 27:
/*      */           case 28:
/*      */           default:
/* 2373 */             throw new StreamCorruptedException("Unknown kind: " + paramArrayOfValueMember[i].type.kind().value());
/*      */           }
/*      */ 
/*      */         }
/*      */         catch (IllegalArgumentException localIllegalArgumentException1)
/*      */         {
/* 2381 */           ClassCastException localClassCastException1 = new ClassCastException("Assigning instance of class " + paramArrayOfValueMember[i].id + " to field " + this.currentClassDesc.getName() + '#' + paramArrayOfValueMember[i].name);
/*      */ 
/* 2383 */           localClassCastException1.initCause(localIllegalArgumentException1);
/* 2384 */           throw localClassCastException1;
/*      */         }
/*      */     }
/*      */     catch (Throwable localThrowable)
/*      */     {
/* 2389 */       StreamCorruptedException localStreamCorruptedException = new StreamCorruptedException(localThrowable.getMessage());
/* 2390 */       localStreamCorruptedException.initCause(localThrowable);
/* 2391 */       throw localStreamCorruptedException;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void skipCustomUsingFVD(ValueMember[] paramArrayOfValueMember, CodeBase paramCodeBase)
/*      */     throws InvalidClassException, StreamCorruptedException, ClassNotFoundException, IOException
/*      */   {
/* 2400 */     readFormatVersion();
/* 2401 */     boolean bool = readBoolean();
/*      */ 
/* 2403 */     if (bool) {
/* 2404 */       throwAwayData(paramArrayOfValueMember, paramCodeBase);
/*      */     }
/* 2406 */     if (getStreamFormatVersion() == 2)
/*      */     {
/* 2408 */       ((ValueInputStream)getOrbStream()).start_value();
/* 2409 */       ((ValueInputStream)getOrbStream()).end_value();
/*      */     }
/*      */   }
/*      */ 
/*      */   private void throwAwayData(ValueMember[] paramArrayOfValueMember, CodeBase paramCodeBase)
/*      */     throws InvalidClassException, StreamCorruptedException, ClassNotFoundException, IOException
/*      */   {
/* 2422 */     for (int i = 0; i < paramArrayOfValueMember.length; i++)
/*      */     {
/*      */       try
/*      */       {
/* 2426 */         switch (paramArrayOfValueMember[i].type.kind().value()) {
/*      */         case 10:
/* 2428 */           this.orbStream.read_octet();
/* 2429 */           break;
/*      */         case 8:
/* 2431 */           this.orbStream.read_boolean();
/* 2432 */           break;
/*      */         case 9:
/*      */         case 26:
/* 2440 */           this.orbStream.read_wchar();
/* 2441 */           break;
/*      */         case 2:
/* 2443 */           this.orbStream.read_short();
/* 2444 */           break;
/*      */         case 3:
/* 2446 */           this.orbStream.read_long();
/* 2447 */           break;
/*      */         case 23:
/* 2449 */           this.orbStream.read_longlong();
/* 2450 */           break;
/*      */         case 6:
/* 2452 */           this.orbStream.read_float();
/* 2453 */           break;
/*      */         case 7:
/* 2455 */           this.orbStream.read_double();
/* 2456 */           break;
/*      */         case 14:
/*      */         case 29:
/*      */         case 30:
/* 2460 */           Class localClass = null;
/* 2461 */           localObject = paramArrayOfValueMember[i].id;
/*      */           try
/*      */           {
/* 2464 */             localClass = this.vhandler.getClassFromType((String)localObject);
/*      */           }
/*      */           catch (ClassNotFoundException localClassNotFoundException)
/*      */           {
/* 2468 */             localClass = null;
/*      */           }
/* 2470 */           String str = null;
/* 2471 */           if (localClass != null) {
/* 2472 */             str = ValueUtility.getSignature(paramArrayOfValueMember[i]);
/*      */           }
/*      */           try
/*      */           {
/* 2476 */             if ((str != null) && ((str.equals("Ljava/lang/Object;")) || (str.equals("Ljava/io/Serializable;")) || (str.equals("Ljava/io/Externalizable;"))))
/*      */             {
/* 2479 */               Util.readAny(this.orbStream);
/*      */             }
/*      */             else
/*      */             {
/* 2488 */               int j = 2;
/*      */ 
/* 2490 */               if (!this.vhandler.isSequence((String)localObject)) {
/* 2491 */                 FullValueDescription localFullValueDescription = paramCodeBase.meta(paramArrayOfValueMember[i].id);
/* 2492 */                 if (kRemoteTypeCode == paramArrayOfValueMember[i].type)
/*      */                 {
/* 2495 */                   j = 0;
/* 2496 */                 } else if (localFullValueDescription.is_abstract)
/*      */                 {
/* 2499 */                   j = 1;
/*      */                 }
/*      */ 
/*      */               }
/*      */ 
/* 2507 */               switch (j) {
/*      */               case 0:
/* 2509 */                 this.orbStream.read_Object();
/* 2510 */                 break;
/*      */               case 1:
/* 2512 */                 this.orbStream.read_abstract_interface();
/* 2513 */                 break;
/*      */               case 2:
/* 2515 */                 if (localClass != null)
/* 2516 */                   this.orbStream.read_value(localClass);
/*      */                 else {
/* 2518 */                   this.orbStream.read_value();
/*      */                 }
/* 2520 */                 break;
/*      */               default:
/* 2523 */                 throw new StreamCorruptedException("Unknown callType: " + j); }  }  } catch (IndirectionException localIndirectionException) {  } case 4:
/*      */         case 5:
/*      */         case 11:
/*      */         case 12:
/*      */         case 13:
/*      */         case 15:
/*      */         case 16:
/*      */         case 17:
/*      */         case 18:
/*      */         case 19:
/*      */         case 20:
/*      */         case 21:
/*      */         case 22:
/*      */         case 24:
/*      */         case 25:
/*      */         case 27:
/*      */         case 28:
/*      */         default:
/* 2537 */           throw new StreamCorruptedException("Unknown kind: " + paramArrayOfValueMember[i].type.kind().value());
/*      */         }
/*      */ 
/*      */       }
/*      */       catch (IllegalArgumentException localIllegalArgumentException)
/*      */       {
/* 2546 */         java.lang.Object localObject = new ClassCastException("Assigning instance of class " + paramArrayOfValueMember[i].id + " to field " + this.currentClassDesc.getName() + '#' + paramArrayOfValueMember[i].name);
/*      */ 
/* 2549 */         ((ClassCastException)localObject).initCause(localIllegalArgumentException);
/* 2550 */         throw ((Throwable)localObject);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void setObjectField(java.lang.Object paramObject1, Class paramClass, String paramString, java.lang.Object paramObject2)
/*      */   {
/*      */     try
/*      */     {
/* 2559 */       Field localField = paramClass.getDeclaredField(paramString);
/* 2560 */       Class localClass = localField.getType();
/* 2561 */       if ((paramObject2 != null) && (!localClass.isInstance(paramObject2))) {
/* 2562 */         throw new Exception();
/*      */       }
/* 2564 */       long l = bridge.objectFieldOffset(localField);
/* 2565 */       bridge.putObject(paramObject1, l, paramObject2);
/*      */     } catch (Exception localException) {
/* 2567 */       throw utilWrapper.errorSetObjectField(localException, paramString, paramObject1.toString(), paramObject2.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void setBooleanField(java.lang.Object paramObject, Class paramClass, String paramString, boolean paramBoolean)
/*      */   {
/*      */     try
/*      */     {
/* 2576 */       Field localField = paramClass.getDeclaredField(paramString);
/* 2577 */       long l = bridge.objectFieldOffset(localField);
/* 2578 */       bridge.putBoolean(paramObject, l, paramBoolean);
/*      */     } catch (Exception localException) {
/* 2580 */       throw utilWrapper.errorSetBooleanField(localException, paramString, paramObject.toString(), new Boolean(paramBoolean));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void setByteField(java.lang.Object paramObject, Class paramClass, String paramString, byte paramByte)
/*      */   {
/*      */     try
/*      */     {
/* 2589 */       Field localField = paramClass.getDeclaredField(paramString);
/* 2590 */       long l = bridge.objectFieldOffset(localField);
/* 2591 */       bridge.putByte(paramObject, l, paramByte);
/*      */     } catch (Exception localException) {
/* 2593 */       throw utilWrapper.errorSetByteField(localException, paramString, paramObject.toString(), new Byte(paramByte));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void setCharField(java.lang.Object paramObject, Class paramClass, String paramString, char paramChar)
/*      */   {
/*      */     try
/*      */     {
/* 2602 */       Field localField = paramClass.getDeclaredField(paramString);
/* 2603 */       long l = bridge.objectFieldOffset(localField);
/* 2604 */       bridge.putChar(paramObject, l, paramChar);
/*      */     } catch (Exception localException) {
/* 2606 */       throw utilWrapper.errorSetCharField(localException, paramString, paramObject.toString(), new Character(paramChar));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void setShortField(java.lang.Object paramObject, Class paramClass, String paramString, short paramShort)
/*      */   {
/*      */     try
/*      */     {
/* 2615 */       Field localField = paramClass.getDeclaredField(paramString);
/* 2616 */       long l = bridge.objectFieldOffset(localField);
/* 2617 */       bridge.putShort(paramObject, l, paramShort);
/*      */     } catch (Exception localException) {
/* 2619 */       throw utilWrapper.errorSetShortField(localException, paramString, paramObject.toString(), new Short(paramShort));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void setIntField(java.lang.Object paramObject, Class paramClass, String paramString, int paramInt)
/*      */   {
/*      */     try
/*      */     {
/* 2628 */       Field localField = paramClass.getDeclaredField(paramString);
/* 2629 */       long l = bridge.objectFieldOffset(localField);
/* 2630 */       bridge.putInt(paramObject, l, paramInt);
/*      */     } catch (Exception localException) {
/* 2632 */       throw utilWrapper.errorSetIntField(localException, paramString, paramObject.toString(), new Integer(paramInt));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void setLongField(java.lang.Object paramObject, Class paramClass, String paramString, long paramLong)
/*      */   {
/*      */     try
/*      */     {
/* 2641 */       Field localField = paramClass.getDeclaredField(paramString);
/* 2642 */       long l = bridge.objectFieldOffset(localField);
/* 2643 */       bridge.putLong(paramObject, l, paramLong);
/*      */     } catch (Exception localException) {
/* 2645 */       throw utilWrapper.errorSetLongField(localException, paramString, paramObject.toString(), new Long(paramLong));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void setFloatField(java.lang.Object paramObject, Class paramClass, String paramString, float paramFloat)
/*      */   {
/*      */     try
/*      */     {
/* 2654 */       Field localField = paramClass.getDeclaredField(paramString);
/* 2655 */       long l = bridge.objectFieldOffset(localField);
/* 2656 */       bridge.putFloat(paramObject, l, paramFloat);
/*      */     } catch (Exception localException) {
/* 2658 */       throw utilWrapper.errorSetFloatField(localException, paramString, paramObject.toString(), new Float(paramFloat));
/*      */     }
/*      */   }
/*      */ 
/*      */   private static void setDoubleField(java.lang.Object paramObject, Class paramClass, String paramString, double paramDouble)
/*      */   {
/*      */     try
/*      */     {
/* 2667 */       Field localField = paramClass.getDeclaredField(paramString);
/* 2668 */       long l = bridge.objectFieldOffset(localField);
/* 2669 */       bridge.putDouble(paramObject, l, paramDouble);
/*      */     } catch (Exception localException) {
/* 2671 */       throw utilWrapper.errorSetDoubleField(localException, paramString, paramObject.toString(), new Double(paramDouble));
/*      */     }
/*      */   }
/*      */ 
/*      */   static class ActiveRecursionManager
/*      */   {
/*      */     private Map offsetToObjectMap;
/*      */ 
/*      */     public ActiveRecursionManager()
/*      */     {
/* 2692 */       this.offsetToObjectMap = new HashMap();
/*      */     }
/*      */ 
/*      */     public void addObject(int paramInt, java.lang.Object paramObject)
/*      */     {
/* 2699 */       this.offsetToObjectMap.put(new Integer(paramInt), paramObject);
/*      */     }
/*      */ 
/*      */     public java.lang.Object getObject(int paramInt)
/*      */       throws IOException
/*      */     {
/* 2708 */       Integer localInteger = new Integer(paramInt);
/*      */ 
/* 2710 */       if (!this.offsetToObjectMap.containsKey(localInteger))
/*      */       {
/* 2712 */         throw new IOException("Invalid indirection to offset " + paramInt);
/*      */       }
/*      */ 
/* 2715 */       return this.offsetToObjectMap.get(localInteger);
/*      */     }
/*      */ 
/*      */     public void removeObject(int paramInt)
/*      */     {
/* 2723 */       this.offsetToObjectMap.remove(new Integer(paramInt));
/*      */     }
/*      */ 
/*      */     public boolean containsObject(int paramInt)
/*      */     {
/* 2730 */       return this.offsetToObjectMap.containsKey(new Integer(paramInt));
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.io.IIOPInputStream
 * JD-Core Version:    0.6.2
 */