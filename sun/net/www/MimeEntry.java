/*     */ package sun.net.www;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.net.URLConnection;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public class MimeEntry
/*     */   implements Cloneable
/*     */ {
/*     */   private String typeName;
/*     */   private String tempFileNameTemplate;
/*     */   private int action;
/*     */   private String command;
/*     */   private String description;
/*     */   private String imageFileName;
/*     */   private String[] fileExtensions;
/*     */   boolean starred;
/*     */   public static final int UNKNOWN = 0;
/*     */   public static final int LOAD_INTO_BROWSER = 1;
/*     */   public static final int SAVE_TO_FILE = 2;
/*     */   public static final int LAUNCH_APPLICATION = 3;
/*  49 */   static final String[] actionKeywords = { "unknown", "browser", "save", "application" };
/*     */ 
/*     */   public MimeEntry(String paramString)
/*     */   {
/*  62 */     this(paramString, 0, null, null, null);
/*     */   }
/*     */ 
/*     */   MimeEntry(String paramString1, String paramString2, String paramString3)
/*     */   {
/*  73 */     this.typeName = paramString1.toLowerCase();
/*  74 */     this.action = 0;
/*  75 */     this.command = null;
/*  76 */     this.imageFileName = paramString2;
/*  77 */     setExtensions(paramString3);
/*  78 */     this.starred = isStarred(this.typeName);
/*     */   }
/*     */ 
/*     */   MimeEntry(String paramString1, int paramInt, String paramString2, String paramString3)
/*     */   {
/*  84 */     this.typeName = paramString1.toLowerCase();
/*  85 */     this.action = paramInt;
/*  86 */     this.command = paramString2;
/*  87 */     this.imageFileName = null;
/*  88 */     this.fileExtensions = null;
/*     */ 
/*  90 */     this.tempFileNameTemplate = paramString3;
/*     */   }
/*     */ 
/*     */   MimeEntry(String paramString1, int paramInt, String paramString2, String paramString3, String[] paramArrayOfString)
/*     */   {
/*  97 */     this.typeName = paramString1.toLowerCase();
/*  98 */     this.action = paramInt;
/*  99 */     this.command = paramString2;
/* 100 */     this.imageFileName = paramString3;
/* 101 */     this.fileExtensions = paramArrayOfString;
/*     */ 
/* 103 */     this.starred = isStarred(paramString1);
/*     */   }
/*     */ 
/*     */   public synchronized String getType()
/*     */   {
/* 108 */     return this.typeName;
/*     */   }
/*     */ 
/*     */   public synchronized void setType(String paramString) {
/* 112 */     this.typeName = paramString.toLowerCase();
/*     */   }
/*     */ 
/*     */   public synchronized int getAction() {
/* 116 */     return this.action;
/*     */   }
/*     */ 
/*     */   public synchronized void setAction(int paramInt, String paramString) {
/* 120 */     this.action = paramInt;
/* 121 */     this.command = paramString;
/*     */   }
/*     */ 
/*     */   public synchronized void setAction(int paramInt) {
/* 125 */     this.action = paramInt;
/*     */   }
/*     */ 
/*     */   public synchronized String getLaunchString() {
/* 129 */     return this.command;
/*     */   }
/*     */ 
/*     */   public synchronized void setCommand(String paramString) {
/* 133 */     this.command = paramString;
/*     */   }
/*     */ 
/*     */   public synchronized String getDescription() {
/* 137 */     return this.description != null ? this.description : this.typeName;
/*     */   }
/*     */ 
/*     */   public synchronized void setDescription(String paramString) {
/* 141 */     this.description = paramString;
/*     */   }
/*     */ 
/*     */   public String getImageFileName()
/*     */   {
/* 149 */     return this.imageFileName;
/*     */   }
/*     */ 
/*     */   public synchronized void setImageFileName(String paramString) {
/* 153 */     File localFile = new File(paramString);
/* 154 */     if (localFile.getParent() == null) {
/* 155 */       this.imageFileName = System.getProperty("java.net.ftp.imagepath." + paramString);
/*     */     }
/*     */     else
/*     */     {
/* 159 */       this.imageFileName = paramString;
/*     */     }
/*     */ 
/* 162 */     if (paramString.lastIndexOf('.') < 0)
/* 163 */       this.imageFileName += ".gif";
/*     */   }
/*     */ 
/*     */   public String getTempFileTemplate()
/*     */   {
/* 168 */     return this.tempFileNameTemplate;
/*     */   }
/*     */ 
/*     */   public synchronized String[] getExtensions() {
/* 172 */     return this.fileExtensions;
/*     */   }
/*     */ 
/*     */   public synchronized String getExtensionsAsList() {
/* 176 */     String str = "";
/* 177 */     if (this.fileExtensions != null) {
/* 178 */       for (int i = 0; i < this.fileExtensions.length; i++) {
/* 179 */         str = str + this.fileExtensions[i];
/* 180 */         if (i < this.fileExtensions.length - 1) {
/* 181 */           str = str + ",";
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/* 186 */     return str;
/*     */   }
/*     */ 
/*     */   public synchronized void setExtensions(String paramString) {
/* 190 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ",");
/* 191 */     int i = localStringTokenizer.countTokens();
/* 192 */     String[] arrayOfString = new String[i];
/*     */ 
/* 194 */     for (int j = 0; j < i; j++) {
/* 195 */       String str = (String)localStringTokenizer.nextElement();
/* 196 */       arrayOfString[j] = str.trim();
/*     */     }
/*     */ 
/* 199 */     this.fileExtensions = arrayOfString;
/*     */   }
/*     */ 
/*     */   private boolean isStarred(String paramString) {
/* 203 */     return (paramString != null) && (paramString.length() > 0) && (paramString.endsWith("/*"));
/*     */   }
/*     */ 
/*     */   public Object launch(URLConnection paramURLConnection, InputStream paramInputStream, MimeTable paramMimeTable)
/*     */     throws ApplicationLaunchException
/*     */   {
/* 220 */     switch (this.action)
/*     */     {
/*     */     case 2:
/*     */       try {
/* 224 */         return paramInputStream;
/*     */       }
/*     */       catch (Exception localException1) {
/* 227 */         return "Load to file failed:\n" + localException1;
/*     */       }
/*     */ 
/*     */     case 1:
/*     */       try
/*     */       {
/* 236 */         return paramURLConnection.getContent();
/*     */       } catch (Exception localException2) {
/* 238 */         return null;
/*     */       }
/*     */ 
/*     */     case 3:
/* 243 */       String str = this.command;
/* 244 */       int i = str.indexOf(' ');
/* 245 */       if (i > 0) {
/* 246 */         str = str.substring(0, i);
/*     */       }
/*     */ 
/* 249 */       return new MimeLauncher(this, paramURLConnection, paramInputStream, paramMimeTable.getTempFileTemplate(), str);
/*     */     case 0:
/* 255 */       return null;
/*     */     }
/*     */ 
/* 258 */     return null;
/*     */   }
/*     */ 
/*     */   public boolean matches(String paramString) {
/* 262 */     if (this.starred)
/*     */     {
/* 264 */       return paramString.startsWith(this.typeName);
/*     */     }
/* 266 */     return paramString.equals(this.typeName);
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 272 */     MimeEntry localMimeEntry = new MimeEntry(this.typeName);
/* 273 */     localMimeEntry.action = this.action;
/* 274 */     localMimeEntry.command = this.command;
/* 275 */     localMimeEntry.description = this.description;
/* 276 */     localMimeEntry.imageFileName = this.imageFileName;
/* 277 */     localMimeEntry.tempFileNameTemplate = this.tempFileNameTemplate;
/* 278 */     localMimeEntry.fileExtensions = this.fileExtensions;
/*     */ 
/* 280 */     return localMimeEntry;
/*     */   }
/*     */ 
/*     */   public synchronized String toProperty() {
/* 284 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 286 */     String str1 = "; ";
/* 287 */     int i = 0;
/*     */ 
/* 289 */     int j = getAction();
/* 290 */     if (j != 0) {
/* 291 */       localStringBuffer.append("action=" + actionKeywords[j]);
/* 292 */       i = 1;
/*     */     }
/*     */ 
/* 295 */     String str2 = getLaunchString();
/* 296 */     if ((str2 != null) && (str2.length() > 0)) {
/* 297 */       if (i != 0) {
/* 298 */         localStringBuffer.append(str1);
/*     */       }
/* 300 */       localStringBuffer.append("application=" + str2);
/* 301 */       i = 1;
/*     */     }
/*     */ 
/* 304 */     if (getImageFileName() != null) {
/* 305 */       if (i != 0) {
/* 306 */         localStringBuffer.append(str1);
/*     */       }
/* 308 */       localStringBuffer.append("icon=" + getImageFileName());
/* 309 */       i = 1;
/*     */     }
/*     */ 
/* 312 */     String str3 = getExtensionsAsList();
/* 313 */     if (str3.length() > 0) {
/* 314 */       if (i != 0) {
/* 315 */         localStringBuffer.append(str1);
/*     */       }
/* 317 */       localStringBuffer.append("file_extensions=" + str3);
/* 318 */       i = 1;
/*     */     }
/*     */ 
/* 321 */     String str4 = getDescription();
/* 322 */     if ((str4 != null) && (!str4.equals(getType()))) {
/* 323 */       if (i != 0) {
/* 324 */         localStringBuffer.append(str1);
/*     */       }
/* 326 */       localStringBuffer.append("description=" + str4);
/*     */     }
/*     */ 
/* 329 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 333 */     return "MimeEntry[contentType=" + this.typeName + ", image=" + this.imageFileName + ", action=" + this.action + ", command=" + this.command + ", extensions=" + getExtensionsAsList() + "]";
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.www.MimeEntry
 * JD-Core Version:    0.6.2
 */