/*     */ package sun.awt;
/*     */ 
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class OSInfo
/*     */ {
/*  51 */   public static final WindowsVersion WINDOWS_UNKNOWN = new WindowsVersion(-1, -1, null);
/*  52 */   public static final WindowsVersion WINDOWS_95 = new WindowsVersion(4, 0, null);
/*  53 */   public static final WindowsVersion WINDOWS_98 = new WindowsVersion(4, 10, null);
/*  54 */   public static final WindowsVersion WINDOWS_ME = new WindowsVersion(4, 90, null);
/*  55 */   public static final WindowsVersion WINDOWS_2000 = new WindowsVersion(5, 0, null);
/*  56 */   public static final WindowsVersion WINDOWS_XP = new WindowsVersion(5, 1, null);
/*  57 */   public static final WindowsVersion WINDOWS_2003 = new WindowsVersion(5, 2, null);
/*  58 */   public static final WindowsVersion WINDOWS_VISTA = new WindowsVersion(6, 0, null);
/*     */   private static final String OS_NAME = "os.name";
/*     */   private static final String OS_VERSION = "os.version";
/*  63 */   private static final Map<String, WindowsVersion> windowsVersionMap = new HashMap();
/*     */ 
/*  75 */   private static final PrivilegedAction<OSType> osTypeAction = new PrivilegedAction() {
/*     */     public OSInfo.OSType run() {
/*  77 */       return OSInfo.getOSType();
/*     */     }
/*  75 */   };
/*     */ 
/*     */   public static OSType getOSType()
/*     */     throws SecurityException
/*     */   {
/*  89 */     String str = System.getProperty("os.name");
/*     */ 
/*  91 */     if (str != null) {
/*  92 */       if (str.contains("Windows")) {
/*  93 */         return OSType.WINDOWS;
/*     */       }
/*     */ 
/*  96 */       if (str.contains("Linux")) {
/*  97 */         return OSType.LINUX;
/*     */       }
/*     */ 
/* 100 */       if ((str.contains("Solaris")) || (str.contains("SunOS"))) {
/* 101 */         return OSType.SOLARIS;
/*     */       }
/*     */ 
/* 104 */       if (str.contains("OS X")) {
/* 105 */         return OSType.MACOSX;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 111 */     return OSType.UNKNOWN;
/*     */   }
/*     */ 
/*     */   public static PrivilegedAction<OSType> getOSTypeAction() {
/* 115 */     return osTypeAction;
/*     */   }
/*     */ 
/*     */   public static WindowsVersion getWindowsVersion() throws SecurityException {
/* 119 */     String str = System.getProperty("os.version");
/*     */ 
/* 121 */     if (str == null) {
/* 122 */       return WINDOWS_UNKNOWN;
/*     */     }
/*     */ 
/* 125 */     synchronized (windowsVersionMap) {
/* 126 */       WindowsVersion localWindowsVersion = (WindowsVersion)windowsVersionMap.get(str);
/*     */ 
/* 128 */       if (localWindowsVersion == null)
/*     */       {
/* 130 */         String[] arrayOfString = str.split("\\.");
/*     */ 
/* 132 */         if (arrayOfString.length == 2)
/*     */           try {
/* 134 */             localWindowsVersion = new WindowsVersion(Integer.parseInt(arrayOfString[0]), Integer.parseInt(arrayOfString[1]), null);
/*     */           } catch (NumberFormatException localNumberFormatException) {
/* 136 */             return WINDOWS_UNKNOWN;
/*     */           }
/*     */         else {
/* 139 */           return WINDOWS_UNKNOWN;
/*     */         }
/*     */ 
/* 142 */         windowsVersionMap.put(str, localWindowsVersion);
/*     */       }
/*     */ 
/* 145 */       return localWindowsVersion;
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  66 */     windowsVersionMap.put(WINDOWS_95.toString(), WINDOWS_95);
/*  67 */     windowsVersionMap.put(WINDOWS_98.toString(), WINDOWS_98);
/*  68 */     windowsVersionMap.put(WINDOWS_ME.toString(), WINDOWS_ME);
/*  69 */     windowsVersionMap.put(WINDOWS_2000.toString(), WINDOWS_2000);
/*  70 */     windowsVersionMap.put(WINDOWS_XP.toString(), WINDOWS_XP);
/*  71 */     windowsVersionMap.put(WINDOWS_2003.toString(), WINDOWS_2003);
/*  72 */     windowsVersionMap.put(WINDOWS_VISTA.toString(), WINDOWS_VISTA);
/*     */   }
/*     */ 
/*     */   public static enum OSType
/*     */   {
/*  39 */     WINDOWS, 
/*  40 */     LINUX, 
/*  41 */     SOLARIS, 
/*  42 */     MACOSX, 
/*  43 */     UNKNOWN;
/*     */   }
/*     */ 
/*     */   public static class WindowsVersion
/*     */     implements Comparable<WindowsVersion>
/*     */   {
/*     */     private final int major;
/*     */     private final int minor;
/*     */ 
/*     */     private WindowsVersion(int paramInt1, int paramInt2)
/*     */     {
/* 155 */       this.major = paramInt1;
/* 156 */       this.minor = paramInt2;
/*     */     }
/*     */ 
/*     */     public int getMajor() {
/* 160 */       return this.major;
/*     */     }
/*     */ 
/*     */     public int getMinor() {
/* 164 */       return this.minor;
/*     */     }
/*     */ 
/*     */     public int compareTo(WindowsVersion paramWindowsVersion) {
/* 168 */       int i = this.major - paramWindowsVersion.getMajor();
/*     */ 
/* 170 */       if (i == 0) {
/* 171 */         i = this.minor - paramWindowsVersion.getMinor();
/*     */       }
/*     */ 
/* 174 */       return i;
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject) {
/* 178 */       return ((paramObject instanceof WindowsVersion)) && (compareTo((WindowsVersion)paramObject) == 0);
/*     */     }
/*     */ 
/*     */     public int hashCode() {
/* 182 */       return 31 * this.major + this.minor;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 186 */       return this.major + "." + this.minor;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.OSInfo
 * JD-Core Version:    0.6.2
 */