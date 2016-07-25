/*      */ package java.util.prefs;
/*      */ 
/*      */ import java.io.ByteArrayOutputStream;
/*      */ import java.util.StringTokenizer;
/*      */ import sun.util.logging.PlatformLogger;
/*      */ 
/*      */ class WindowsPreferences extends AbstractPreferences
/*      */ {
/*      */   private static PlatformLogger logger;
/*   56 */   private static final byte[] WINDOWS_ROOT_PATH = stringToByteArray("Software\\JavaSoft\\Prefs");
/*      */   private static final int HKEY_CURRENT_USER = -2147483647;
/*      */   private static final int HKEY_LOCAL_MACHINE = -2147483646;
/*      */   private static final int USER_ROOT_NATIVE_HANDLE = -2147483647;
/*      */   private static final int SYSTEM_ROOT_NATIVE_HANDLE = -2147483646;
/*      */   private static final int MAX_WINDOWS_PATH_LENGTH = 256;
/*   85 */   static final Preferences userRoot = new WindowsPreferences(-2147483647, WINDOWS_ROOT_PATH);
/*      */ 
/*   91 */   static final Preferences systemRoot = new WindowsPreferences(-2147483646, WINDOWS_ROOT_PATH);
/*      */   private static final int ERROR_SUCCESS = 0;
/*      */   private static final int ERROR_FILE_NOT_FOUND = 2;
/*      */   private static final int ERROR_ACCESS_DENIED = 5;
/*      */   private static final int NATIVE_HANDLE = 0;
/*      */   private static final int ERROR_CODE = 1;
/*      */   private static final int SUBKEYS_NUMBER = 0;
/*      */   private static final int VALUES_NUMBER = 2;
/*      */   private static final int MAX_KEY_LENGTH = 3;
/*      */   private static final int MAX_VALUE_NAME_LENGTH = 4;
/*      */   private static final int DISPOSITION = 2;
/*      */   private static final int REG_CREATED_NEW_KEY = 1;
/*      */   private static final int REG_OPENED_EXISTING_KEY = 2;
/*      */   private static final int NULL_NATIVE_HANDLE = 0;
/*      */   private static final int DELETE = 65536;
/*      */   private static final int KEY_QUERY_VALUE = 1;
/*      */   private static final int KEY_SET_VALUE = 2;
/*      */   private static final int KEY_CREATE_SUB_KEY = 4;
/*      */   private static final int KEY_ENUMERATE_SUB_KEYS = 8;
/*      */   private static final int KEY_READ = 131097;
/*      */   private static final int KEY_WRITE = 131078;
/*      */   private static final int KEY_ALL_ACCESS = 983103;
/*  125 */   private static int INIT_SLEEP_TIME = 50;
/*      */ 
/*  130 */   private static int MAX_ATTEMPTS = 5;
/*      */ 
/*  135 */   private boolean isBackingStoreAvailable = true;
/*      */ 
/*      */   private static native int[] WindowsRegOpenKey(int paramInt1, byte[] paramArrayOfByte, int paramInt2);
/*      */ 
/*      */   private static int[] WindowsRegOpenKey1(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*      */   {
/*  147 */     int[] arrayOfInt = WindowsRegOpenKey(paramInt1, paramArrayOfByte, paramInt2);
/*  148 */     if (arrayOfInt[1] == 0)
/*  149 */       return arrayOfInt;
/*  150 */     if (arrayOfInt[1] == 2) {
/*  151 */       logger().warning("Trying to recreate Windows registry node " + byteArrayToString(paramArrayOfByte) + " at root 0x" + Integer.toHexString(paramInt1) + ".");
/*      */ 
/*  155 */       int i = WindowsRegCreateKeyEx(paramInt1, paramArrayOfByte)[0];
/*  156 */       WindowsRegCloseKey(i);
/*  157 */       return WindowsRegOpenKey(paramInt1, paramArrayOfByte, paramInt2);
/*  158 */     }if (arrayOfInt[1] != 5) {
/*  159 */       long l = INIT_SLEEP_TIME;
/*  160 */       for (int j = 0; j < MAX_ATTEMPTS; j++) {
/*      */         try {
/*  162 */           Thread.sleep(l);
/*      */         } catch (InterruptedException localInterruptedException) {
/*  164 */           return arrayOfInt;
/*      */         }
/*  166 */         l *= 2L;
/*  167 */         arrayOfInt = WindowsRegOpenKey(paramInt1, paramArrayOfByte, paramInt2);
/*  168 */         if (arrayOfInt[1] == 0) {
/*  169 */           return arrayOfInt;
/*      */         }
/*      */       }
/*      */     }
/*  173 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   private static native int WindowsRegCloseKey(int paramInt);
/*      */ 
/*      */   private static native int[] WindowsRegCreateKeyEx(int paramInt, byte[] paramArrayOfByte);
/*      */ 
/*      */   private static int[] WindowsRegCreateKeyEx1(int paramInt, byte[] paramArrayOfByte)
/*      */   {
/*  190 */     int[] arrayOfInt = WindowsRegCreateKeyEx(paramInt, paramArrayOfByte);
/*  191 */     if (arrayOfInt[1] == 0) {
/*  192 */       return arrayOfInt;
/*      */     }
/*  194 */     long l = INIT_SLEEP_TIME;
/*  195 */     for (int i = 0; i < MAX_ATTEMPTS; i++) {
/*      */       try {
/*  197 */         Thread.sleep(l);
/*      */       } catch (InterruptedException localInterruptedException) {
/*  199 */         return arrayOfInt;
/*      */       }
/*  201 */       l *= 2L;
/*  202 */       arrayOfInt = WindowsRegCreateKeyEx(paramInt, paramArrayOfByte);
/*  203 */       if (arrayOfInt[1] == 0) {
/*  204 */         return arrayOfInt;
/*      */       }
/*      */     }
/*      */ 
/*  208 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   private static native int WindowsRegDeleteKey(int paramInt, byte[] paramArrayOfByte);
/*      */ 
/*      */   private static native int WindowsRegFlushKey(int paramInt);
/*      */ 
/*      */   private static int WindowsRegFlushKey1(int paramInt)
/*      */   {
/*  224 */     int i = WindowsRegFlushKey(paramInt);
/*  225 */     if (i == 0) {
/*  226 */       return i;
/*      */     }
/*  228 */     long l = INIT_SLEEP_TIME;
/*  229 */     for (int j = 0; j < MAX_ATTEMPTS; j++) {
/*      */       try {
/*  231 */         Thread.sleep(l);
/*      */       } catch (InterruptedException localInterruptedException) {
/*  233 */         return i;
/*      */       }
/*  235 */       l *= 2L;
/*  236 */       i = WindowsRegFlushKey(paramInt);
/*  237 */       if (i == 0) {
/*  238 */         return i;
/*      */       }
/*      */     }
/*      */ 
/*  242 */     return i;
/*      */   }
/*      */ 
/*      */   private static native byte[] WindowsRegQueryValueEx(int paramInt, byte[] paramArrayOfByte);
/*      */ 
/*      */   private static native int WindowsRegSetValueEx(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2);
/*      */ 
/*      */   private static int WindowsRegSetValueEx1(int paramInt, byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
/*      */   {
/*  260 */     int i = WindowsRegSetValueEx(paramInt, paramArrayOfByte1, paramArrayOfByte2);
/*  261 */     if (i == 0) {
/*  262 */       return i;
/*      */     }
/*  264 */     long l = INIT_SLEEP_TIME;
/*  265 */     for (int j = 0; j < MAX_ATTEMPTS; j++) {
/*      */       try {
/*  267 */         Thread.sleep(l);
/*      */       } catch (InterruptedException localInterruptedException) {
/*  269 */         return i;
/*      */       }
/*  271 */       l *= 2L;
/*  272 */       i = WindowsRegSetValueEx(paramInt, paramArrayOfByte1, paramArrayOfByte2);
/*  273 */       if (i == 0) {
/*  274 */         return i;
/*      */       }
/*      */     }
/*      */ 
/*  278 */     return i;
/*      */   }
/*      */ 
/*      */   private static native int WindowsRegDeleteValue(int paramInt, byte[] paramArrayOfByte);
/*      */ 
/*      */   private static native int[] WindowsRegQueryInfoKey(int paramInt);
/*      */ 
/*      */   private static int[] WindowsRegQueryInfoKey1(int paramInt)
/*      */   {
/*  295 */     int[] arrayOfInt = WindowsRegQueryInfoKey(paramInt);
/*  296 */     if (arrayOfInt[1] == 0) {
/*  297 */       return arrayOfInt;
/*      */     }
/*  299 */     long l = INIT_SLEEP_TIME;
/*  300 */     for (int i = 0; i < MAX_ATTEMPTS; i++) {
/*      */       try {
/*  302 */         Thread.sleep(l);
/*      */       } catch (InterruptedException localInterruptedException) {
/*  304 */         return arrayOfInt;
/*      */       }
/*  306 */       l *= 2L;
/*  307 */       arrayOfInt = WindowsRegQueryInfoKey(paramInt);
/*  308 */       if (arrayOfInt[1] == 0) {
/*  309 */         return arrayOfInt;
/*      */       }
/*      */     }
/*      */ 
/*  313 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   private static native byte[] WindowsRegEnumKeyEx(int paramInt1, int paramInt2, int paramInt3);
/*      */ 
/*      */   private static byte[] WindowsRegEnumKeyEx1(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  327 */     byte[] arrayOfByte = WindowsRegEnumKeyEx(paramInt1, paramInt2, paramInt3);
/*  328 */     if (arrayOfByte != null) {
/*  329 */       return arrayOfByte;
/*      */     }
/*  331 */     long l = INIT_SLEEP_TIME;
/*  332 */     for (int i = 0; i < MAX_ATTEMPTS; i++) {
/*      */       try {
/*  334 */         Thread.sleep(l);
/*      */       } catch (InterruptedException localInterruptedException) {
/*  336 */         return arrayOfByte;
/*      */       }
/*  338 */       l *= 2L;
/*  339 */       arrayOfByte = WindowsRegEnumKeyEx(paramInt1, paramInt2, paramInt3);
/*  340 */       if (arrayOfByte != null) {
/*  341 */         return arrayOfByte;
/*      */       }
/*      */     }
/*      */ 
/*  345 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   private static native byte[] WindowsRegEnumValue(int paramInt1, int paramInt2, int paramInt3);
/*      */ 
/*      */   private static byte[] WindowsRegEnumValue1(int paramInt1, int paramInt2, int paramInt3)
/*      */   {
/*  358 */     byte[] arrayOfByte = WindowsRegEnumValue(paramInt1, paramInt2, paramInt3);
/*      */ 
/*  360 */     if (arrayOfByte != null) {
/*  361 */       return arrayOfByte;
/*      */     }
/*  363 */     long l = INIT_SLEEP_TIME;
/*  364 */     for (int i = 0; i < MAX_ATTEMPTS; i++) {
/*      */       try {
/*  366 */         Thread.sleep(l);
/*      */       } catch (InterruptedException localInterruptedException) {
/*  368 */         return arrayOfByte;
/*      */       }
/*  370 */       l *= 2L;
/*  371 */       arrayOfByte = WindowsRegEnumValue(paramInt1, paramInt2, paramInt3);
/*      */ 
/*  373 */       if (arrayOfByte != null) {
/*  374 */         return arrayOfByte;
/*      */       }
/*      */     }
/*      */ 
/*  378 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   private WindowsPreferences(WindowsPreferences paramWindowsPreferences, String paramString)
/*      */   {
/*  388 */     super(paramWindowsPreferences, paramString);
/*  389 */     int i = paramWindowsPreferences.openKey(4, 131097);
/*  390 */     if (i == 0)
/*      */     {
/*  392 */       this.isBackingStoreAvailable = false;
/*  393 */       return;
/*      */     }
/*  395 */     int[] arrayOfInt = WindowsRegCreateKeyEx1(i, toWindowsName(paramString));
/*      */ 
/*  397 */     if (arrayOfInt[1] != 0) {
/*  398 */       logger().warning("Could not create windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". Windows RegCreateKeyEx(...) returned error code " + arrayOfInt[1] + ".");
/*      */ 
/*  403 */       this.isBackingStoreAvailable = false;
/*  404 */       return;
/*      */     }
/*  406 */     this.newNode = (arrayOfInt[2] == 1);
/*  407 */     closeKey(i);
/*  408 */     closeKey(arrayOfInt[0]);
/*      */   }
/*      */ 
/*      */   private WindowsPreferences(int paramInt, byte[] paramArrayOfByte)
/*      */   {
/*  420 */     super(null, "");
/*  421 */     int[] arrayOfInt = WindowsRegCreateKeyEx1(paramInt, paramArrayOfByte);
/*      */ 
/*  423 */     if (arrayOfInt[1] != 0) {
/*  424 */       logger().warning("Could not open/create prefs root node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". Windows RegCreateKeyEx(...) returned error code " + arrayOfInt[1] + ".");
/*      */ 
/*  429 */       this.isBackingStoreAvailable = false;
/*  430 */       return;
/*      */     }
/*      */ 
/*  433 */     this.newNode = (arrayOfInt[2] == 1);
/*  434 */     closeKey(arrayOfInt[0]);
/*      */   }
/*      */ 
/*      */   private byte[] windowsAbsolutePath()
/*      */   {
/*  443 */     ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/*  444 */     localByteArrayOutputStream.write(WINDOWS_ROOT_PATH, 0, WINDOWS_ROOT_PATH.length - 1);
/*  445 */     StringTokenizer localStringTokenizer = new StringTokenizer(absolutePath(), "/");
/*  446 */     while (localStringTokenizer.hasMoreTokens()) {
/*  447 */       localByteArrayOutputStream.write(92);
/*  448 */       String str = localStringTokenizer.nextToken();
/*  449 */       byte[] arrayOfByte = toWindowsName(str);
/*  450 */       localByteArrayOutputStream.write(arrayOfByte, 0, arrayOfByte.length - 1);
/*      */     }
/*  452 */     localByteArrayOutputStream.write(0);
/*  453 */     return localByteArrayOutputStream.toByteArray();
/*      */   }
/*      */ 
/*      */   private int openKey(int paramInt)
/*      */   {
/*  466 */     return openKey(paramInt, paramInt);
/*      */   }
/*      */ 
/*      */   private int openKey(int paramInt1, int paramInt2)
/*      */   {
/*  480 */     return openKey(windowsAbsolutePath(), paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   private int openKey(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */   {
/*  498 */     if (paramArrayOfByte.length <= 257) {
/*  499 */       int[] arrayOfInt = WindowsRegOpenKey1(rootNativeHandle(), paramArrayOfByte, paramInt1);
/*      */ 
/*  501 */       if ((arrayOfInt[1] == 5) && (paramInt2 != paramInt1)) {
/*  502 */         arrayOfInt = WindowsRegOpenKey1(rootNativeHandle(), paramArrayOfByte, paramInt2);
/*      */       }
/*      */ 
/*  505 */       if (arrayOfInt[1] != 0) {
/*  506 */         logger().warning("Could not open windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". Windows RegOpenKey(...) returned error code " + arrayOfInt[1] + ".");
/*      */ 
/*  511 */         arrayOfInt[0] = 0;
/*  512 */         if (arrayOfInt[1] == 5) {
/*  513 */           throw new SecurityException("Could not open windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ": Access denied");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  519 */       return arrayOfInt[0];
/*      */     }
/*  521 */     return openKey(rootNativeHandle(), paramArrayOfByte, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   private int openKey(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
/*      */   {
/*  541 */     if (paramArrayOfByte.length <= 257) {
/*  542 */       int[] arrayOfInt = WindowsRegOpenKey1(paramInt1, paramArrayOfByte, paramInt2);
/*      */ 
/*  544 */       if ((arrayOfInt[1] == 5) && (paramInt3 != paramInt2)) {
/*  545 */         arrayOfInt = WindowsRegOpenKey1(paramInt1, paramArrayOfByte, paramInt3);
/*      */       }
/*      */ 
/*  548 */       if (arrayOfInt[1] != 0) {
/*  549 */         logger().warning("Could not open windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(paramInt1) + ". Windows RegOpenKey(...) returned error code " + arrayOfInt[1] + ".");
/*      */ 
/*  554 */         arrayOfInt[0] = 0;
/*      */       }
/*  556 */       return arrayOfInt[0];
/*      */     }
/*  558 */     int i = -1;
/*      */ 
/*  560 */     for (int j = 256; j > 0; j--) {
/*  561 */       if (paramArrayOfByte[j] == 92) {
/*  562 */         i = j;
/*  563 */         break;
/*      */       }
/*      */     }
/*      */ 
/*  567 */     byte[] arrayOfByte1 = new byte[i + 1];
/*  568 */     System.arraycopy(paramArrayOfByte, 0, arrayOfByte1, 0, i);
/*      */ 
/*  570 */     arrayOfByte1[i] = 0;
/*  571 */     byte[] arrayOfByte2 = new byte[paramArrayOfByte.length - i - 1];
/*      */ 
/*  573 */     System.arraycopy(paramArrayOfByte, i + 1, arrayOfByte2, 0, arrayOfByte2.length);
/*      */ 
/*  575 */     int k = openKey(paramInt1, arrayOfByte1, paramInt2, paramInt3);
/*      */ 
/*  577 */     if (k == 0) {
/*  578 */       return 0;
/*      */     }
/*  580 */     int m = openKey(k, arrayOfByte2, paramInt2, paramInt3);
/*      */ 
/*  582 */     closeKey(k);
/*  583 */     return m;
/*      */   }
/*      */ 
/*      */   private void closeKey(int paramInt)
/*      */   {
/*  596 */     int i = WindowsRegCloseKey(paramInt);
/*  597 */     if (i != 0)
/*  598 */       logger().warning("Could not close windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". Windows RegCloseKey(...) returned error code " + i + ".");
/*      */   }
/*      */ 
/*      */   protected void putSpi(String paramString1, String paramString2)
/*      */   {
/*  612 */     int i = openKey(2);
/*  613 */     if (i == 0) {
/*  614 */       this.isBackingStoreAvailable = false;
/*  615 */       return;
/*      */     }
/*  617 */     int j = WindowsRegSetValueEx1(i, toWindowsName(paramString1), toWindowsValueString(paramString2));
/*      */ 
/*  619 */     if (j != 0) {
/*  620 */       logger().warning("Could not assign value to key " + byteArrayToString(toWindowsName(paramString1)) + " at Windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". Windows RegSetValueEx(...) returned error code " + j + ".");
/*      */ 
/*  625 */       this.isBackingStoreAvailable = false;
/*      */     }
/*  627 */     closeKey(i);
/*      */   }
/*      */ 
/*      */   protected String getSpi(String paramString)
/*      */   {
/*  637 */     int i = openKey(1);
/*  638 */     if (i == 0) {
/*  639 */       return null;
/*      */     }
/*  641 */     byte[] arrayOfByte = WindowsRegQueryValueEx(i, toWindowsName(paramString));
/*      */ 
/*  643 */     if (arrayOfByte == null) {
/*  644 */       closeKey(i);
/*  645 */       return null;
/*      */     }
/*  647 */     closeKey(i);
/*  648 */     return toJavaValueString((byte[])arrayOfByte);
/*      */   }
/*      */ 
/*      */   protected void removeSpi(String paramString)
/*      */   {
/*  659 */     int i = openKey(2);
/*  660 */     if (i == 0) {
/*  661 */       return;
/*      */     }
/*  663 */     int j = WindowsRegDeleteValue(i, toWindowsName(paramString));
/*      */ 
/*  665 */     if ((j != 0) && (j != 2)) {
/*  666 */       logger().warning("Could not delete windows registry value " + byteArrayToString(windowsAbsolutePath()) + "\\" + toWindowsName(paramString) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". Windows RegDeleteValue(...) returned error code " + j + ".");
/*      */ 
/*  672 */       this.isBackingStoreAvailable = false;
/*      */     }
/*  674 */     closeKey(i);
/*      */   }
/*      */ 
/*      */   protected String[] keysSpi()
/*      */     throws BackingStoreException
/*      */   {
/*  685 */     int i = openKey(1);
/*  686 */     if (i == 0) {
/*  687 */       throw new BackingStoreException("Could not open windowsregistry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ".");
/*      */     }
/*      */ 
/*  691 */     int[] arrayOfInt = WindowsRegQueryInfoKey1(i);
/*  692 */     if (arrayOfInt[1] != 0) {
/*  693 */       String str1 = "Could not query windowsregistry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". Windows RegQueryInfoKeyEx(...) returned error code " + arrayOfInt[1] + ".";
/*      */ 
/*  698 */       logger().warning(str1);
/*  699 */       throw new BackingStoreException(str1);
/*      */     }
/*  701 */     int j = arrayOfInt[4];
/*  702 */     int k = arrayOfInt[2];
/*  703 */     if (k == 0) {
/*  704 */       closeKey(i);
/*  705 */       return new String[0];
/*      */     }
/*      */ 
/*  708 */     String[] arrayOfString = new String[k];
/*  709 */     for (int m = 0; m < k; m++) {
/*  710 */       byte[] arrayOfByte = WindowsRegEnumValue1(i, m, j + 1);
/*      */ 
/*  712 */       if (arrayOfByte == null) {
/*  713 */         String str2 = "Could not enumerate value #" + m + "  of windows node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ".";
/*      */ 
/*  717 */         logger().warning(str2);
/*  718 */         throw new BackingStoreException(str2);
/*      */       }
/*  720 */       arrayOfString[m] = toJavaName(arrayOfByte);
/*      */     }
/*  722 */     closeKey(i);
/*  723 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   protected String[] childrenNamesSpi()
/*      */     throws BackingStoreException
/*      */   {
/*  734 */     int i = openKey(9);
/*  735 */     if (i == 0) {
/*  736 */       throw new BackingStoreException("Could not open windowsregistry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ".");
/*      */     }
/*      */ 
/*  741 */     int[] arrayOfInt = WindowsRegQueryInfoKey1(i);
/*  742 */     if (arrayOfInt[1] != 0) {
/*  743 */       String str1 = "Could not query windowsregistry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". Windows RegQueryInfoKeyEx(...) returned error code " + arrayOfInt[1] + ".";
/*      */ 
/*  748 */       logger().warning(str1);
/*  749 */       throw new BackingStoreException(str1);
/*      */     }
/*  751 */     int j = arrayOfInt[3];
/*  752 */     int k = arrayOfInt[0];
/*  753 */     if (k == 0) {
/*  754 */       closeKey(i);
/*  755 */       return new String[0];
/*      */     }
/*  757 */     String[] arrayOfString1 = new String[k];
/*  758 */     String[] arrayOfString2 = new String[k];
/*      */ 
/*  760 */     for (int m = 0; m < k; m++) {
/*  761 */       byte[] arrayOfByte = WindowsRegEnumKeyEx1(i, m, j + 1);
/*      */ 
/*  763 */       if (arrayOfByte == null) {
/*  764 */         str2 = "Could not enumerate key #" + m + "  of windows node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". ";
/*      */ 
/*  768 */         logger().warning(str2);
/*  769 */         throw new BackingStoreException(str2);
/*      */       }
/*  771 */       String str2 = toJavaName(arrayOfByte);
/*  772 */       arrayOfString2[m] = str2;
/*      */     }
/*  774 */     closeKey(i);
/*  775 */     return arrayOfString2;
/*      */   }
/*      */ 
/*      */   public void flush()
/*      */     throws BackingStoreException
/*      */   {
/*  786 */     if (isRemoved()) {
/*  787 */       this.parent.flush();
/*  788 */       return;
/*      */     }
/*  790 */     if (!this.isBackingStoreAvailable) {
/*  791 */       throw new BackingStoreException("flush(): Backing store not available.");
/*      */     }
/*      */ 
/*  794 */     int i = openKey(131097);
/*  795 */     if (i == 0) {
/*  796 */       throw new BackingStoreException("Could not open windowsregistry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ".");
/*      */     }
/*      */ 
/*  800 */     int j = WindowsRegFlushKey1(i);
/*  801 */     if (j != 0) {
/*  802 */       String str = "Could not flush windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". Windows RegFlushKey(...) returned error code " + j + ".";
/*      */ 
/*  806 */       logger().warning(str);
/*  807 */       throw new BackingStoreException(str);
/*      */     }
/*  809 */     closeKey(i);
/*      */   }
/*      */ 
/*      */   public void sync()
/*      */     throws BackingStoreException
/*      */   {
/*  819 */     if (isRemoved())
/*  820 */       throw new IllegalStateException("Node has been removed");
/*  821 */     flush();
/*      */   }
/*      */ 
/*      */   protected AbstractPreferences childSpi(String paramString)
/*      */   {
/*  832 */     return new WindowsPreferences(this, paramString);
/*      */   }
/*      */ 
/*      */   public void removeNodeSpi()
/*      */     throws BackingStoreException
/*      */   {
/*  842 */     int i = ((WindowsPreferences)parent()).openKey(65536);
/*      */ 
/*  844 */     if (i == 0) {
/*  845 */       throw new BackingStoreException("Could not open parent windowsregistry node of " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ".");
/*      */     }
/*      */ 
/*  849 */     int j = WindowsRegDeleteKey(i, toWindowsName(name()));
/*      */ 
/*  851 */     if (j != 0) {
/*  852 */       String str = "Could not delete windows registry node " + byteArrayToString(windowsAbsolutePath()) + " at root 0x" + Integer.toHexString(rootNativeHandle()) + ". Windows RegDeleteKeyEx(...) returned error code " + j + ".";
/*      */ 
/*  857 */       logger().warning(str);
/*  858 */       throw new BackingStoreException(str);
/*      */     }
/*  860 */     closeKey(i);
/*      */   }
/*      */ 
/*      */   private static String toJavaName(byte[] paramArrayOfByte)
/*      */   {
/*  871 */     String str = byteArrayToString(paramArrayOfByte);
/*      */ 
/*  873 */     if ((str.length() > 1) && (str.substring(0, 2).equals("/!")))
/*      */     {
/*  875 */       return toJavaAlt64Name(str);
/*      */     }
/*  877 */     StringBuffer localStringBuffer = new StringBuffer();
/*      */ 
/*  880 */     for (int i = 0; i < str.length(); i++)
/*      */     {
/*      */       char c1;
/*  881 */       if ((c1 = str.charAt(i)) == '/') {
/*  882 */         char c2 = ' ';
/*  883 */         if ((str.length() > i + 1) && ((c2 = str.charAt(i + 1)) >= 'A') && (c2 <= 'Z'))
/*      */         {
/*  885 */           c1 = c2;
/*  886 */           i++;
/*  887 */         } else if ((str.length() > i + 1) && (c2 == '/')) {
/*  888 */           c1 = '\\';
/*  889 */           i++;
/*      */         }
/*  891 */       } else if (c1 == '\\') {
/*  892 */         c1 = '/';
/*      */       }
/*  894 */       localStringBuffer.append(c1);
/*      */     }
/*  896 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private static String toJavaAlt64Name(String paramString)
/*      */   {
/*  907 */     byte[] arrayOfByte = Base64.altBase64ToByteArray(paramString.substring(2));
/*      */ 
/*  909 */     StringBuffer localStringBuffer = new StringBuffer();
/*  910 */     for (int i = 0; i < arrayOfByte.length; i++) {
/*  911 */       int j = arrayOfByte[(i++)] & 0xFF;
/*  912 */       int k = arrayOfByte[i] & 0xFF;
/*  913 */       localStringBuffer.append((char)((j << 8) + k));
/*      */     }
/*  915 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private static byte[] toWindowsName(String paramString)
/*      */   {
/*  939 */     StringBuffer localStringBuffer = new StringBuffer();
/*  940 */     for (int i = 0; i < paramString.length(); i++) {
/*  941 */       char c = paramString.charAt(i);
/*  942 */       if ((c < ' ') || (c > ''))
/*      */       {
/*  944 */         return toWindowsAlt64Name(paramString);
/*      */       }
/*  946 */       if (c == '\\')
/*  947 */         localStringBuffer.append("//");
/*  948 */       else if (c == '/')
/*  949 */         localStringBuffer.append('\\');
/*  950 */       else if ((c >= 'A') && (c <= 'Z'))
/*  951 */         localStringBuffer.append("/" + c);
/*      */       else {
/*  953 */         localStringBuffer.append(c);
/*      */       }
/*      */     }
/*  956 */     return stringToByteArray(localStringBuffer.toString());
/*      */   }
/*      */ 
/*      */   private static byte[] toWindowsAlt64Name(String paramString)
/*      */   {
/*  966 */     byte[] arrayOfByte = new byte[2 * paramString.length()];
/*      */ 
/*  968 */     int i = 0;
/*  969 */     for (int j = 0; j < paramString.length(); j++) {
/*  970 */       int k = paramString.charAt(j);
/*  971 */       arrayOfByte[(i++)] = ((byte)(k >>> 8));
/*  972 */       arrayOfByte[(i++)] = ((byte)k);
/*      */     }
/*      */ 
/*  975 */     return stringToByteArray("/!" + Base64.byteArrayToAltBase64(arrayOfByte));
/*      */   }
/*      */ 
/*      */   private static String toJavaValueString(byte[] paramArrayOfByte)
/*      */   {
/*  987 */     String str = byteArrayToString(paramArrayOfByte);
/*  988 */     StringBuffer localStringBuffer = new StringBuffer();
/*      */ 
/*  990 */     for (int i = 0; i < str.length(); i++)
/*      */     {
/*      */       char c1;
/*  991 */       if ((c1 = str.charAt(i)) == '/') {
/*  992 */         char c2 = ' ';
/*      */ 
/*  994 */         if ((str.length() > i + 1) && ((c2 = str.charAt(i + 1)) == 'u'))
/*      */         {
/*  996 */           if (str.length() < i + 6) {
/*      */             break;
/*      */           }
/*  999 */           c1 = (char)Integer.parseInt(str.substring(i + 2, i + 6), 16);
/*      */ 
/* 1001 */           i += 5;
/*      */         }
/* 1004 */         else if ((str.length() > i + 1) && (str.charAt(i + 1) >= 'A') && (c2 <= 'Z'))
/*      */         {
/* 1006 */           c1 = c2;
/* 1007 */           i++;
/* 1008 */         } else if ((str.length() > i + 1) && (c2 == '/'))
/*      */         {
/* 1010 */           c1 = '\\';
/* 1011 */           i++;
/*      */         }
/* 1013 */       } else if (c1 == '\\') {
/* 1014 */         c1 = '/';
/*      */       }
/* 1016 */       localStringBuffer.append(c1);
/*      */     }
/* 1018 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   private static byte[] toWindowsValueString(String paramString)
/*      */   {
/* 1031 */     StringBuffer localStringBuffer1 = new StringBuffer();
/* 1032 */     for (int i = 0; i < paramString.length(); i++) {
/* 1033 */       char c = paramString.charAt(i);
/* 1034 */       if ((c < ' ') || (c > ''))
/*      */       {
/* 1036 */         localStringBuffer1.append("/u");
/* 1037 */         String str = Integer.toHexString(paramString.charAt(i));
/* 1038 */         StringBuffer localStringBuffer2 = new StringBuffer(str);
/* 1039 */         localStringBuffer2.reverse();
/* 1040 */         int j = 4 - localStringBuffer2.length();
/* 1041 */         for (int k = 0; k < j; k++) {
/* 1042 */           localStringBuffer2.append('0');
/*      */         }
/* 1044 */         for (k = 0; k < 4; k++)
/* 1045 */           localStringBuffer1.append(localStringBuffer2.charAt(3 - k));
/*      */       }
/* 1047 */       else if (c == '\\') {
/* 1048 */         localStringBuffer1.append("//");
/* 1049 */       } else if (c == '/') {
/* 1050 */         localStringBuffer1.append('\\');
/* 1051 */       } else if ((c >= 'A') && (c <= 'Z')) {
/* 1052 */         localStringBuffer1.append("/" + c);
/*      */       } else {
/* 1054 */         localStringBuffer1.append(c);
/*      */       }
/*      */     }
/* 1057 */     return stringToByteArray(localStringBuffer1.toString());
/*      */   }
/*      */ 
/*      */   private int rootNativeHandle()
/*      */   {
/* 1064 */     return isUserNode() ? -2147483647 : -2147483646;
/*      */   }
/*      */ 
/*      */   private static byte[] stringToByteArray(String paramString)
/*      */   {
/* 1072 */     byte[] arrayOfByte = new byte[paramString.length() + 1];
/* 1073 */     for (int i = 0; i < paramString.length(); i++) {
/* 1074 */       arrayOfByte[i] = ((byte)paramString.charAt(i));
/*      */     }
/* 1076 */     arrayOfByte[paramString.length()] = 0;
/* 1077 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   private static String byteArrayToString(byte[] paramArrayOfByte)
/*      */   {
/* 1084 */     StringBuffer localStringBuffer = new StringBuffer();
/* 1085 */     for (int i = 0; i < paramArrayOfByte.length - 1; i++) {
/* 1086 */       localStringBuffer.append((char)paramArrayOfByte[i]);
/*      */     }
/* 1088 */     return localStringBuffer.toString();
/*      */   }
/*      */ 
/*      */   protected void flushSpi()
/*      */     throws BackingStoreException
/*      */   {
/*      */   }
/*      */ 
/*      */   protected void syncSpi()
/*      */     throws BackingStoreException
/*      */   {
/*      */   }
/*      */ 
/*      */   private static synchronized PlatformLogger logger()
/*      */   {
/* 1106 */     if (logger == null) {
/* 1107 */       logger = PlatformLogger.getLogger("java.util.prefs");
/*      */     }
/* 1109 */     return logger;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.prefs.WindowsPreferences
 * JD-Core Version:    0.6.2
 */