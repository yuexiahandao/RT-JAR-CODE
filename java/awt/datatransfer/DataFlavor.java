/*      */ package java.awt.datatransfer;
/*      */ 
/*      */ import java.io.ByteArrayInputStream;
/*      */ import java.io.CharArrayReader;
/*      */ import java.io.Externalizable;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.InputStreamReader;
/*      */ import java.io.ObjectInput;
/*      */ import java.io.ObjectOutput;
/*      */ import java.io.OptionalDataException;
/*      */ import java.io.Reader;
/*      */ import java.io.Serializable;
/*      */ import java.io.StringReader;
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.CharBuffer;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.List;
/*      */ import sun.awt.datatransfer.DataTransferer;
/*      */ import sun.awt.datatransfer.DataTransferer.DataFlavorComparator;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ import sun.security.util.SecurityConstants;
/*      */ 
/*      */ public class DataFlavor
/*      */   implements Externalizable, Cloneable
/*      */ {
/*      */   private static final long serialVersionUID = 8367026044764648243L;
/*  107 */   private static final Class ioInputStreamClass = InputStream.class;
/*      */ 
/*  181 */   public static final DataFlavor stringFlavor = createConstant(String.class, "Unicode String");
/*      */ 
/*  191 */   public static final DataFlavor imageFlavor = createConstant("image/x-java-image; class=java.awt.Image", "Image");
/*      */ 
/*      */   @Deprecated
/*  210 */   public static final DataFlavor plainTextFlavor = createConstant("text/plain; charset=unicode; class=java.io.InputStream", "Plain Text");
/*      */   public static final String javaSerializedObjectMimeType = "application/x-java-serialized-object";
/*  229 */   public static final DataFlavor javaFileListFlavor = createConstant("application/x-java-file-list;class=java.util.List", null);
/*      */   public static final String javaJVMLocalObjectMimeType = "application/x-java-jvm-local-objectref";
/*      */   public static final String javaRemoteObjectMimeType = "application/x-java-remote-object";
/*      */   private static Comparator textFlavorComparator;
/*      */   transient int atom;
/*      */   MimeType mimeType;
/*      */   private String humanPresentableName;
/*      */   private Class representationClass;
/*      */ 
/*      */   protected static final Class<?> tryToLoadClass(String paramString, ClassLoader paramClassLoader)
/*      */     throws ClassNotFoundException
/*      */   {
/*  122 */     ReflectUtil.checkPackageAccess(paramString);
/*      */     try {
/*  124 */       SecurityManager localSecurityManager = System.getSecurityManager();
/*  125 */       if (localSecurityManager != null) {
/*  126 */         localSecurityManager.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
/*      */       }
/*  128 */       ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
/*      */       try
/*      */       {
/*  131 */         return Class.forName(paramString, true, localClassLoader);
/*      */       }
/*      */       catch (ClassNotFoundException localClassNotFoundException1)
/*      */       {
/*  135 */         localClassLoader = Thread.currentThread().getContextClassLoader();
/*  136 */         if (localClassLoader != null)
/*      */           try {
/*  138 */             return Class.forName(paramString, true, localClassLoader);
/*      */           }
/*      */           catch (ClassNotFoundException localClassNotFoundException2)
/*      */           {
/*      */           }
/*      */       }
/*      */     }
/*      */     catch (SecurityException localSecurityException)
/*      */     {
/*      */     }
/*  148 */     return Class.forName(paramString, true, paramClassLoader);
/*      */   }
/*      */ 
/*      */   private static DataFlavor createConstant(Class paramClass, String paramString)
/*      */   {
/*      */     try
/*      */     {
/*  156 */       return new DataFlavor(paramClass, paramString); } catch (Exception localException) {
/*      */     }
/*  158 */     return null;
/*      */   }
/*      */ 
/*      */   private static DataFlavor createConstant(String paramString1, String paramString2)
/*      */   {
/*      */     try
/*      */     {
/*  167 */       return new DataFlavor(paramString1, paramString2); } catch (Exception localException) {
/*      */     }
/*  169 */     return null;
/*      */   }
/*      */ 
/*      */   public DataFlavor()
/*      */   {
/*      */   }
/*      */ 
/*      */   private DataFlavor(String paramString1, String paramString2, MimeTypeParameterList paramMimeTypeParameterList, Class paramClass, String paramString3)
/*      */   {
/*  276 */     if (paramString1 == null) {
/*  277 */       throw new NullPointerException("primaryType");
/*      */     }
/*  279 */     if (paramString2 == null) {
/*  280 */       throw new NullPointerException("subType");
/*      */     }
/*  282 */     if (paramClass == null) {
/*  283 */       throw new NullPointerException("representationClass");
/*      */     }
/*      */ 
/*  286 */     if (paramMimeTypeParameterList == null) paramMimeTypeParameterList = new MimeTypeParameterList();
/*      */ 
/*  288 */     paramMimeTypeParameterList.set("class", paramClass.getName());
/*      */ 
/*  290 */     if (paramString3 == null) {
/*  291 */       paramString3 = paramMimeTypeParameterList.get("humanPresentableName");
/*      */ 
/*  293 */       if (paramString3 == null)
/*  294 */         paramString3 = paramString1 + "/" + paramString2;
/*      */     }
/*      */     try
/*      */     {
/*  298 */       this.mimeType = new MimeType(paramString1, paramString2, paramMimeTypeParameterList);
/*      */     } catch (MimeTypeParseException localMimeTypeParseException) {
/*  300 */       throw new IllegalArgumentException("MimeType Parse Exception: " + localMimeTypeParseException.getMessage());
/*      */     }
/*      */ 
/*  303 */     this.representationClass = paramClass;
/*  304 */     this.humanPresentableName = paramString3;
/*      */ 
/*  306 */     this.mimeType.removeParameter("humanPresentableName");
/*      */   }
/*      */ 
/*      */   public DataFlavor(Class<?> paramClass, String paramString)
/*      */   {
/*  325 */     this("application", "x-java-serialized-object", null, paramClass, paramString);
/*  326 */     if (paramClass == null)
/*  327 */       throw new NullPointerException("representationClass");
/*      */   }
/*      */ 
/*      */   public DataFlavor(String paramString1, String paramString2)
/*      */   {
/*  362 */     if (paramString1 == null)
/*  363 */       throw new NullPointerException("mimeType");
/*      */     try
/*      */     {
/*  366 */       initialize(paramString1, paramString2, getClass().getClassLoader());
/*      */     } catch (MimeTypeParseException localMimeTypeParseException) {
/*  368 */       throw new IllegalArgumentException("failed to parse:" + paramString1);
/*      */     } catch (ClassNotFoundException localClassNotFoundException) {
/*  370 */       throw new IllegalArgumentException("can't find specified class: " + localClassNotFoundException.getMessage());
/*      */     }
/*      */   }
/*      */ 
/*      */   public DataFlavor(String paramString1, String paramString2, ClassLoader paramClassLoader)
/*      */     throws ClassNotFoundException
/*      */   {
/*  402 */     if (paramString1 == null)
/*  403 */       throw new NullPointerException("mimeType");
/*      */     try
/*      */     {
/*  406 */       initialize(paramString1, paramString2, paramClassLoader);
/*      */     } catch (MimeTypeParseException localMimeTypeParseException) {
/*  408 */       throw new IllegalArgumentException("failed to parse:" + paramString1);
/*      */     }
/*      */   }
/*      */ 
/*      */   public DataFlavor(String paramString)
/*      */     throws ClassNotFoundException
/*      */   {
/*  430 */     if (paramString == null)
/*  431 */       throw new NullPointerException("mimeType");
/*      */     try
/*      */     {
/*  434 */       initialize(paramString, null, getClass().getClassLoader());
/*      */     } catch (MimeTypeParseException localMimeTypeParseException) {
/*  436 */       throw new IllegalArgumentException("failed to parse:" + paramString);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void initialize(String paramString1, String paramString2, ClassLoader paramClassLoader)
/*      */     throws MimeTypeParseException, ClassNotFoundException
/*      */   {
/*  455 */     if (paramString1 == null) {
/*  456 */       throw new NullPointerException("mimeType");
/*      */     }
/*      */ 
/*  459 */     this.mimeType = new MimeType(paramString1);
/*      */ 
/*  461 */     String str = getParameter("class");
/*      */ 
/*  463 */     if (str == null) {
/*  464 */       if ("application/x-java-serialized-object".equals(this.mimeType.getBaseType()))
/*      */       {
/*  466 */         throw new IllegalArgumentException("no representation class specified for:" + paramString1);
/*      */       }
/*  468 */       this.representationClass = InputStream.class;
/*      */     } else {
/*  470 */       this.representationClass = tryToLoadClass(str, paramClassLoader);
/*      */     }
/*      */ 
/*  473 */     this.mimeType.setParameter("class", this.representationClass.getName());
/*      */ 
/*  475 */     if (paramString2 == null) {
/*  476 */       paramString2 = this.mimeType.getParameter("humanPresentableName");
/*  477 */       if (paramString2 == null) {
/*  478 */         paramString2 = this.mimeType.getPrimaryType() + "/" + this.mimeType.getSubType();
/*      */       }
/*      */     }
/*  481 */     this.humanPresentableName = paramString2;
/*      */ 
/*  483 */     this.mimeType.removeParameter("humanPresentableName");
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  499 */     String str = getClass().getName();
/*  500 */     str = str + "[" + paramString() + "]";
/*  501 */     return str;
/*      */   }
/*      */ 
/*      */   private String paramString() {
/*  505 */     String str = "";
/*  506 */     str = str + "mimetype=";
/*  507 */     if (this.mimeType == null)
/*  508 */       str = str + "null";
/*      */     else {
/*  510 */       str = str + this.mimeType.getBaseType();
/*      */     }
/*  512 */     str = str + ";representationclass=";
/*  513 */     if (this.representationClass == null)
/*  514 */       str = str + "null";
/*      */     else {
/*  516 */       str = str + this.representationClass.getName();
/*      */     }
/*  518 */     if ((DataTransferer.isFlavorCharsetTextType(this)) && ((isRepresentationClassInputStream()) || (isRepresentationClassByteBuffer()) || (DataTransferer.byteArrayClass.equals(this.representationClass))))
/*      */     {
/*  523 */       str = str + ";charset=" + DataTransferer.getTextCharset(this);
/*      */     }
/*  525 */     return str;
/*      */   }
/*      */ 
/*      */   public static final DataFlavor getTextPlainUnicodeFlavor()
/*      */   {
/*  545 */     String str = null;
/*  546 */     DataTransferer localDataTransferer = DataTransferer.getInstance();
/*  547 */     if (localDataTransferer != null) {
/*  548 */       str = localDataTransferer.getDefaultUnicodeEncoding();
/*      */     }
/*  550 */     return new DataFlavor("text/plain;charset=" + str + ";class=java.io.InputStream", "Plain Text");
/*      */   }
/*      */ 
/*      */   public static final DataFlavor selectBestTextFlavor(DataFlavor[] paramArrayOfDataFlavor)
/*      */   {
/*  673 */     if ((paramArrayOfDataFlavor == null) || (paramArrayOfDataFlavor.length == 0)) {
/*  674 */       return null;
/*      */     }
/*      */ 
/*  677 */     if (textFlavorComparator == null) {
/*  678 */       textFlavorComparator = new TextFlavorComparator();
/*      */     }
/*      */ 
/*  681 */     DataFlavor localDataFlavor = (DataFlavor)Collections.max(Arrays.asList(paramArrayOfDataFlavor), textFlavorComparator);
/*      */ 
/*  685 */     if (!localDataFlavor.isFlavorTextType()) {
/*  686 */       return null;
/*      */     }
/*      */ 
/*  689 */     return localDataFlavor;
/*      */   }
/*      */ 
/*      */   public Reader getReaderForText(Transferable paramTransferable)
/*      */     throws UnsupportedFlavorException, IOException
/*      */   {
/*  778 */     Object localObject1 = paramTransferable.getTransferData(this);
/*  779 */     if (localObject1 == null) {
/*  780 */       throw new IllegalArgumentException("getTransferData() returned null");
/*      */     }
/*      */ 
/*  784 */     if ((localObject1 instanceof Reader))
/*  785 */       return (Reader)localObject1;
/*  786 */     if ((localObject1 instanceof String))
/*  787 */       return new StringReader((String)localObject1);
/*  788 */     if ((localObject1 instanceof CharBuffer)) {
/*  789 */       localObject2 = (CharBuffer)localObject1;
/*  790 */       int i = ((CharBuffer)localObject2).remaining();
/*  791 */       char[] arrayOfChar = new char[i];
/*  792 */       ((CharBuffer)localObject2).get(arrayOfChar, 0, i);
/*  793 */       return new CharArrayReader(arrayOfChar);
/*  794 */     }if ((localObject1 instanceof char[])) {
/*  795 */       return new CharArrayReader((char[])localObject1);
/*      */     }
/*      */ 
/*  798 */     Object localObject2 = null;
/*      */ 
/*  800 */     if ((localObject1 instanceof InputStream)) {
/*  801 */       localObject2 = (InputStream)localObject1;
/*  802 */     } else if ((localObject1 instanceof ByteBuffer)) {
/*  803 */       localObject3 = (ByteBuffer)localObject1;
/*  804 */       int j = ((ByteBuffer)localObject3).remaining();
/*  805 */       byte[] arrayOfByte = new byte[j];
/*  806 */       ((ByteBuffer)localObject3).get(arrayOfByte, 0, j);
/*  807 */       localObject2 = new ByteArrayInputStream(arrayOfByte);
/*  808 */     } else if ((localObject1 instanceof byte[])) {
/*  809 */       localObject2 = new ByteArrayInputStream((byte[])localObject1);
/*      */     }
/*      */ 
/*  812 */     if (localObject2 == null) {
/*  813 */       throw new IllegalArgumentException("transfer data is not Reader, String, CharBuffer, char array, InputStream, ByteBuffer, or byte array");
/*      */     }
/*      */ 
/*  816 */     Object localObject3 = getParameter("charset");
/*  817 */     return localObject3 == null ? new InputStreamReader((InputStream)localObject2) : new InputStreamReader((InputStream)localObject2, (String)localObject3);
/*      */   }
/*      */ 
/*      */   public String getMimeType()
/*      */   {
/*  827 */     return this.mimeType != null ? this.mimeType.toString() : null;
/*      */   }
/*      */ 
/*      */   public Class<?> getRepresentationClass()
/*      */   {
/*  839 */     return this.representationClass;
/*      */   }
/*      */ 
/*      */   public String getHumanPresentableName()
/*      */   {
/*  850 */     return this.humanPresentableName;
/*      */   }
/*      */ 
/*      */   public String getPrimaryType()
/*      */   {
/*  858 */     return this.mimeType != null ? this.mimeType.getPrimaryType() : null;
/*      */   }
/*      */ 
/*      */   public String getSubType()
/*      */   {
/*  866 */     return this.mimeType != null ? this.mimeType.getSubType() : null;
/*      */   }
/*      */ 
/*      */   public String getParameter(String paramString)
/*      */   {
/*  879 */     if (paramString.equals("humanPresentableName")) {
/*  880 */       return this.humanPresentableName;
/*      */     }
/*  882 */     return this.mimeType != null ? this.mimeType.getParameter(paramString) : null;
/*      */   }
/*      */ 
/*      */   public void setHumanPresentableName(String paramString)
/*      */   {
/*  894 */     this.humanPresentableName = paramString;
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/*  919 */     return ((paramObject instanceof DataFlavor)) && (equals((DataFlavor)paramObject));
/*      */   }
/*      */ 
/*      */   public boolean equals(DataFlavor paramDataFlavor)
/*      */   {
/*  934 */     if (paramDataFlavor == null) {
/*  935 */       return false;
/*      */     }
/*  937 */     if (this == paramDataFlavor) {
/*  938 */       return true;
/*      */     }
/*      */ 
/*  941 */     if (this.representationClass == null) {
/*  942 */       if (paramDataFlavor.getRepresentationClass() != null) {
/*  943 */         return false;
/*      */       }
/*      */     }
/*  946 */     else if (!this.representationClass.equals(paramDataFlavor.getRepresentationClass())) {
/*  947 */       return false;
/*      */     }
/*      */ 
/*  951 */     if (this.mimeType == null) {
/*  952 */       if (paramDataFlavor.mimeType != null)
/*  953 */         return false;
/*      */     }
/*      */     else {
/*  956 */       if (!this.mimeType.match(paramDataFlavor.mimeType)) {
/*  957 */         return false;
/*      */       }
/*      */ 
/*  960 */       if (("text".equals(getPrimaryType())) && (DataTransferer.doesSubtypeSupportCharset(this)) && (this.representationClass != null) && (!isRepresentationClassReader()) && (!String.class.equals(this.representationClass)) && (!isRepresentationClassCharBuffer()) && (!DataTransferer.charArrayClass.equals(this.representationClass)))
/*      */       {
/*  968 */         String str1 = DataTransferer.canonicalName(getParameter("charset"));
/*      */ 
/*  970 */         String str2 = DataTransferer.canonicalName(paramDataFlavor.getParameter("charset"));
/*      */ 
/*  972 */         if (str1 == null) {
/*  973 */           if (str2 != null) {
/*  974 */             return false;
/*      */           }
/*      */         }
/*  977 */         else if (!str1.equals(str2)) {
/*  978 */           return false;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  984 */     return true;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   public boolean equals(String paramString)
/*      */   {
/* 1002 */     if ((paramString == null) || (this.mimeType == null))
/* 1003 */       return false;
/* 1004 */     return isMimeTypeEqual(paramString);
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1018 */     int i = 0;
/*      */ 
/* 1020 */     if (this.representationClass != null) {
/* 1021 */       i += this.representationClass.hashCode();
/*      */     }
/*      */ 
/* 1024 */     if (this.mimeType != null) {
/* 1025 */       String str1 = this.mimeType.getPrimaryType();
/* 1026 */       if (str1 != null) {
/* 1027 */         i += str1.hashCode();
/*      */       }
/*      */ 
/* 1034 */       if (("text".equals(str1)) && (DataTransferer.doesSubtypeSupportCharset(this)) && (this.representationClass != null) && (!isRepresentationClassReader()) && (!String.class.equals(this.representationClass)) && (!isRepresentationClassCharBuffer()) && (!DataTransferer.charArrayClass.equals(this.representationClass)))
/*      */       {
/* 1043 */         String str2 = DataTransferer.canonicalName(getParameter("charset"));
/*      */ 
/* 1045 */         if (str2 != null) {
/* 1046 */           i += str2.hashCode();
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1051 */     return i;
/*      */   }
/*      */ 
/*      */   public boolean match(DataFlavor paramDataFlavor)
/*      */   {
/* 1065 */     return equals(paramDataFlavor);
/*      */   }
/*      */ 
/*      */   public boolean isMimeTypeEqual(String paramString)
/*      */   {
/* 1081 */     if (paramString == null) {
/* 1082 */       throw new NullPointerException("mimeType");
/*      */     }
/* 1084 */     if (this.mimeType == null)
/* 1085 */       return false;
/*      */     try
/*      */     {
/* 1088 */       return this.mimeType.match(new MimeType(paramString)); } catch (MimeTypeParseException localMimeTypeParseException) {
/*      */     }
/* 1090 */     return false;
/*      */   }
/*      */ 
/*      */   public final boolean isMimeTypeEqual(DataFlavor paramDataFlavor)
/*      */   {
/* 1104 */     return isMimeTypeEqual(paramDataFlavor.mimeType);
/*      */   }
/*      */ 
/*      */   private boolean isMimeTypeEqual(MimeType paramMimeType)
/*      */   {
/* 1116 */     if (this.mimeType == null) {
/* 1117 */       return paramMimeType == null;
/*      */     }
/* 1119 */     return this.mimeType.match(paramMimeType);
/*      */   }
/*      */ 
/*      */   public boolean isMimeTypeSerializedObject()
/*      */   {
/* 1127 */     return isMimeTypeEqual("application/x-java-serialized-object");
/*      */   }
/*      */ 
/*      */   public final Class<?> getDefaultRepresentationClass() {
/* 1131 */     return ioInputStreamClass;
/*      */   }
/*      */ 
/*      */   public final String getDefaultRepresentationClassAsString() {
/* 1135 */     return getDefaultRepresentationClass().getName();
/*      */   }
/*      */ 
/*      */   public boolean isRepresentationClassInputStream()
/*      */   {
/* 1144 */     return ioInputStreamClass.isAssignableFrom(this.representationClass);
/*      */   }
/*      */ 
/*      */   public boolean isRepresentationClassReader()
/*      */   {
/* 1155 */     return Reader.class.isAssignableFrom(this.representationClass);
/*      */   }
/*      */ 
/*      */   public boolean isRepresentationClassCharBuffer()
/*      */   {
/* 1166 */     return CharBuffer.class.isAssignableFrom(this.representationClass);
/*      */   }
/*      */ 
/*      */   public boolean isRepresentationClassByteBuffer()
/*      */   {
/* 1177 */     return ByteBuffer.class.isAssignableFrom(this.representationClass);
/*      */   }
/*      */ 
/*      */   public boolean isRepresentationClassSerializable()
/*      */   {
/* 1186 */     return Serializable.class.isAssignableFrom(this.representationClass);
/*      */   }
/*      */ 
/*      */   public boolean isRepresentationClassRemote()
/*      */   {
/* 1195 */     return DataTransferer.isRemote(this.representationClass);
/*      */   }
/*      */ 
/*      */   public boolean isFlavorSerializedObjectType()
/*      */   {
/* 1206 */     return (isRepresentationClassSerializable()) && (isMimeTypeEqual("application/x-java-serialized-object"));
/*      */   }
/*      */ 
/*      */   public boolean isFlavorRemoteObjectType()
/*      */   {
/* 1217 */     return (isRepresentationClassRemote()) && (isRepresentationClassSerializable()) && (isMimeTypeEqual("application/x-java-remote-object"));
/*      */   }
/*      */ 
/*      */   public boolean isFlavorJavaFileListType()
/*      */   {
/* 1231 */     if ((this.mimeType == null) || (this.representationClass == null))
/* 1232 */       return false;
/* 1233 */     return (List.class.isAssignableFrom(this.representationClass)) && (this.mimeType.match(javaFileListFlavor.mimeType));
/*      */   }
/*      */ 
/*      */   public boolean isFlavorTextType()
/*      */   {
/* 1269 */     return (DataTransferer.isFlavorCharsetTextType(this)) || (DataTransferer.isFlavorNoncharsetTextType(this));
/*      */   }
/*      */ 
/*      */   public synchronized void writeExternal(ObjectOutput paramObjectOutput)
/*      */     throws IOException
/*      */   {
/* 1278 */     if (this.mimeType != null) {
/* 1279 */       this.mimeType.setParameter("humanPresentableName", this.humanPresentableName);
/* 1280 */       paramObjectOutput.writeObject(this.mimeType);
/* 1281 */       this.mimeType.removeParameter("humanPresentableName");
/*      */     } else {
/* 1283 */       paramObjectOutput.writeObject(null);
/*      */     }
/*      */ 
/* 1286 */     paramObjectOutput.writeObject(this.representationClass);
/*      */   }
/*      */ 
/*      */   public synchronized void readExternal(ObjectInput paramObjectInput)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1294 */     String str = null;
/* 1295 */     this.mimeType = ((MimeType)paramObjectInput.readObject());
/*      */ 
/* 1297 */     if (this.mimeType != null) {
/* 1298 */       this.humanPresentableName = this.mimeType.getParameter("humanPresentableName");
/*      */ 
/* 1300 */       this.mimeType.removeParameter("humanPresentableName");
/* 1301 */       str = this.mimeType.getParameter("class");
/* 1302 */       if (str == null) {
/* 1303 */         throw new IOException("no class parameter specified in: " + this.mimeType);
/*      */       }
/*      */     }
/*      */ 
/*      */     try
/*      */     {
/* 1309 */       this.representationClass = ((Class)paramObjectInput.readObject());
/*      */     } catch (OptionalDataException localOptionalDataException) {
/* 1311 */       if ((!localOptionalDataException.eof) || (localOptionalDataException.length != 0)) {
/* 1312 */         throw localOptionalDataException;
/*      */       }
/*      */ 
/* 1316 */       if (str != null)
/* 1317 */         this.representationClass = tryToLoadClass(str, getClass().getClassLoader());
/*      */     }
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */     throws CloneNotSupportedException
/*      */   {
/* 1329 */     Object localObject = super.clone();
/* 1330 */     if (this.mimeType != null) {
/* 1331 */       ((DataFlavor)localObject).mimeType = ((MimeType)this.mimeType.clone());
/*      */     }
/* 1333 */     return localObject;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   protected String normalizeMimeTypeParameter(String paramString1, String paramString2)
/*      */   {
/* 1352 */     return paramString2;
/*      */   }
/*      */ 
/*      */   @Deprecated
/*      */   protected String normalizeMimeType(String paramString)
/*      */   {
/* 1368 */     return paramString;
/*      */   }
/*      */ 
/*      */   static class TextFlavorComparator extends DataTransferer.DataFlavorComparator
/*      */   {
/*      */     public int compare(Object paramObject1, Object paramObject2)
/*      */     {
/*  718 */       DataFlavor localDataFlavor1 = (DataFlavor)paramObject1;
/*  719 */       DataFlavor localDataFlavor2 = (DataFlavor)paramObject2;
/*      */ 
/*  721 */       if (localDataFlavor1.isFlavorTextType()) {
/*  722 */         if (localDataFlavor2.isFlavorTextType()) {
/*  723 */           return super.compare(paramObject1, paramObject2);
/*      */         }
/*  725 */         return 1;
/*      */       }
/*  727 */       if (localDataFlavor2.isFlavorTextType()) {
/*  728 */         return -1;
/*      */       }
/*  730 */       return 0;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.datatransfer.DataFlavor
 * JD-Core Version:    0.6.2
 */