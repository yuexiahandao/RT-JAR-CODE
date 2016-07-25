/*     */ package sun.nio.fs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.FileStore;
/*     */ import java.nio.file.attribute.AclFileAttributeView;
/*     */ import java.nio.file.attribute.BasicFileAttributeView;
/*     */ import java.nio.file.attribute.DosFileAttributeView;
/*     */ import java.nio.file.attribute.FileAttributeView;
/*     */ import java.nio.file.attribute.FileOwnerAttributeView;
/*     */ import java.nio.file.attribute.FileStoreAttributeView;
/*     */ import java.nio.file.attribute.UserDefinedFileAttributeView;
/*     */ 
/*     */ class WindowsFileStore extends FileStore
/*     */ {
/*     */   private final String root;
/*     */   private final WindowsNativeDispatcher.VolumeInformation volInfo;
/*     */   private final int volType;
/*     */   private final String displayName;
/*     */ 
/*     */   private WindowsFileStore(String paramString)
/*     */     throws WindowsException
/*     */   {
/*  48 */     assert (paramString.charAt(paramString.length() - 1) == '\\');
/*  49 */     this.root = paramString;
/*  50 */     this.volInfo = WindowsNativeDispatcher.GetVolumeInformation(paramString);
/*  51 */     this.volType = WindowsNativeDispatcher.GetDriveType(paramString);
/*     */ 
/*  54 */     String str = this.volInfo.volumeName();
/*  55 */     if (str.length() > 0) {
/*  56 */       this.displayName = str;
/*     */     }
/*     */     else
/*  59 */       this.displayName = (this.volType == 2 ? "Removable Disk" : "");
/*     */   }
/*     */ 
/*     */   static WindowsFileStore create(String paramString, boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*  67 */       return new WindowsFileStore(paramString);
/*     */     } catch (WindowsException localWindowsException) {
/*  69 */       if ((paramBoolean) && (localWindowsException.lastError() == 21))
/*  70 */         return null;
/*  71 */       localWindowsException.rethrowAsIOException(paramString);
/*  72 */     }return null;
/*     */   }
/*     */ 
/*     */   static WindowsFileStore create(WindowsPath paramWindowsPath)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/*     */       String str1;
/*  82 */       if (paramWindowsPath.getFileSystem().supportsLinks()) {
/*  83 */         str1 = WindowsLinkSupport.getFinalPath(paramWindowsPath, true);
/*     */       }
/*     */       else {
/*  86 */         WindowsFileAttributes.get(paramWindowsPath, true);
/*  87 */         str1 = paramWindowsPath.getPathForWin32Calls();
/*     */       }
/*  89 */       String str2 = WindowsNativeDispatcher.GetVolumePathName(str1);
/*  90 */       return new WindowsFileStore(str2);
/*     */     } catch (WindowsException localWindowsException) {
/*  92 */       localWindowsException.rethrowAsIOException(paramWindowsPath);
/*  93 */     }return null;
/*     */   }
/*     */ 
/*     */   WindowsNativeDispatcher.VolumeInformation volumeInformation()
/*     */   {
/*  98 */     return this.volInfo;
/*     */   }
/*     */ 
/*     */   int volumeType() {
/* 102 */     return this.volType;
/*     */   }
/*     */ 
/*     */   public String name()
/*     */   {
/* 107 */     return this.volInfo.volumeName();
/*     */   }
/*     */ 
/*     */   public String type()
/*     */   {
/* 112 */     return this.volInfo.fileSystemName();
/*     */   }
/*     */ 
/*     */   public boolean isReadOnly()
/*     */   {
/* 117 */     return (this.volInfo.flags() & 0x80000) != 0;
/*     */   }
/*     */ 
/*     */   private WindowsNativeDispatcher.DiskFreeSpace readDiskFreeSpace() throws IOException
/*     */   {
/*     */     try {
/* 123 */       return WindowsNativeDispatcher.GetDiskFreeSpaceEx(this.root);
/*     */     } catch (WindowsException localWindowsException) {
/* 125 */       localWindowsException.rethrowAsIOException(this.root);
/* 126 */     }return null;
/*     */   }
/*     */ 
/*     */   public long getTotalSpace()
/*     */     throws IOException
/*     */   {
/* 132 */     return readDiskFreeSpace().totalNumberOfBytes();
/*     */   }
/*     */ 
/*     */   public long getUsableSpace() throws IOException
/*     */   {
/* 137 */     return readDiskFreeSpace().freeBytesAvailable();
/*     */   }
/*     */ 
/*     */   public long getUnallocatedSpace() throws IOException
/*     */   {
/* 142 */     return readDiskFreeSpace().freeBytesAvailable();
/*     */   }
/*     */ 
/*     */   public <V extends FileStoreAttributeView> V getFileStoreAttributeView(Class<V> paramClass)
/*     */   {
/* 147 */     if (paramClass == null)
/* 148 */       throw new NullPointerException();
/* 149 */     return (FileStoreAttributeView)null;
/*     */   }
/*     */ 
/*     */   public Object getAttribute(String paramString)
/*     */     throws IOException
/*     */   {
/* 155 */     if (paramString.equals("totalSpace"))
/* 156 */       return Long.valueOf(getTotalSpace());
/* 157 */     if (paramString.equals("usableSpace"))
/* 158 */       return Long.valueOf(getUsableSpace());
/* 159 */     if (paramString.equals("unallocatedSpace")) {
/* 160 */       return Long.valueOf(getUnallocatedSpace());
/*     */     }
/* 162 */     if (paramString.equals("volume:vsn"))
/* 163 */       return Integer.valueOf(this.volInfo.volumeSerialNumber());
/* 164 */     if (paramString.equals("volume:isRemovable"))
/* 165 */       return Boolean.valueOf(this.volType == 2);
/* 166 */     if (paramString.equals("volume:isCdrom"))
/* 167 */       return Boolean.valueOf(this.volType == 5);
/* 168 */     throw new UnsupportedOperationException("'" + paramString + "' not recognized");
/*     */   }
/*     */ 
/*     */   public boolean supportsFileAttributeView(Class<? extends FileAttributeView> paramClass)
/*     */   {
/* 173 */     if (paramClass == null)
/* 174 */       throw new NullPointerException();
/* 175 */     if ((paramClass == BasicFileAttributeView.class) || (paramClass == DosFileAttributeView.class))
/* 176 */       return true;
/* 177 */     if ((paramClass == AclFileAttributeView.class) || (paramClass == FileOwnerAttributeView.class))
/* 178 */       return (this.volInfo.flags() & 0x8) != 0;
/* 179 */     if (paramClass == UserDefinedFileAttributeView.class)
/* 180 */       return (this.volInfo.flags() & 0x40000) != 0;
/* 181 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean supportsFileAttributeView(String paramString)
/*     */   {
/* 186 */     if ((paramString.equals("basic")) || (paramString.equals("dos")))
/* 187 */       return true;
/* 188 */     if (paramString.equals("acl"))
/* 189 */       return supportsFileAttributeView(AclFileAttributeView.class);
/* 190 */     if (paramString.equals("owner"))
/* 191 */       return supportsFileAttributeView(FileOwnerAttributeView.class);
/* 192 */     if (paramString.equals("user"))
/* 193 */       return supportsFileAttributeView(UserDefinedFileAttributeView.class);
/* 194 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 199 */     if (paramObject == this)
/* 200 */       return true;
/* 201 */     if (!(paramObject instanceof WindowsFileStore))
/* 202 */       return false;
/* 203 */     WindowsFileStore localWindowsFileStore = (WindowsFileStore)paramObject;
/* 204 */     return this.root.equals(localWindowsFileStore.root);
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 209 */     return this.root.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 214 */     StringBuilder localStringBuilder = new StringBuilder(this.displayName);
/* 215 */     if (localStringBuilder.length() > 0)
/* 216 */       localStringBuilder.append(" ");
/* 217 */     localStringBuilder.append("(");
/*     */ 
/* 219 */     localStringBuilder.append(this.root.subSequence(0, this.root.length() - 1));
/* 220 */     localStringBuilder.append(")");
/* 221 */     return localStringBuilder.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.WindowsFileStore
 * JD-Core Version:    0.6.2
 */