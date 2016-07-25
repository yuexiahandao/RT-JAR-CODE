/*     */ package sun.nio.fs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.attribute.DosFileAttributeView;
/*     */ import java.nio.file.attribute.FileTime;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ class WindowsFileAttributeViews
/*     */ {
/*     */   static Basic createBasicView(WindowsPath paramWindowsPath, boolean paramBoolean)
/*     */   {
/* 289 */     return new Basic(paramWindowsPath, paramBoolean);
/*     */   }
/*     */ 
/*     */   static Dos createDosView(WindowsPath paramWindowsPath, boolean paramBoolean) {
/* 293 */     return new Dos(paramWindowsPath, paramBoolean);
/*     */   }
/*     */ 
/*     */   private static class Basic extends AbstractBasicFileAttributeView
/*     */   {
/*     */     final WindowsPath file;
/*     */     final boolean followLinks;
/*     */ 
/*     */     Basic(WindowsPath paramWindowsPath, boolean paramBoolean)
/*     */     {
/*  43 */       this.file = paramWindowsPath;
/*  44 */       this.followLinks = paramBoolean;
/*     */     }
/*     */ 
/*     */     public WindowsFileAttributes readAttributes() throws IOException
/*     */     {
/*  49 */       this.file.checkRead();
/*     */       try {
/*  51 */         return WindowsFileAttributes.get(this.file, this.followLinks);
/*     */       } catch (WindowsException localWindowsException) {
/*  53 */         localWindowsException.rethrowAsIOException(this.file);
/*  54 */       }return null;
/*     */     }
/*     */ 
/*     */     private long adjustForFatEpoch(long paramLong)
/*     */     {
/*  64 */       if ((paramLong != -1L) && (paramLong < 119600064000000000L)) {
/*  65 */         return 119600064000000000L;
/*     */       }
/*  67 */       return paramLong;
/*     */     }
/*     */ 
/*     */     void setFileTimes(long paramLong1, long paramLong2, long paramLong3)
/*     */       throws IOException
/*     */     {
/*  79 */       long l = -1L;
/*     */       try {
/*  81 */         int i = 33554432;
/*  82 */         if ((!this.followLinks) && (this.file.getFileSystem().supportsLinks())) {
/*  83 */           i |= 2097152;
/*     */         }
/*  85 */         l = WindowsNativeDispatcher.CreateFile(this.file.getPathForWin32Calls(), 256, 7, 3, i);
/*     */       }
/*     */       catch (WindowsException localWindowsException1)
/*     */       {
/*  91 */         localWindowsException1.rethrowAsIOException(this.file);
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/*  96 */         WindowsNativeDispatcher.SetFileTime(l, paramLong1, paramLong2, paramLong3);
/*     */       }
/*     */       catch (WindowsException localWindowsException2)
/*     */       {
/*     */         Object localObject1;
/* 103 */         if ((this.followLinks) && (localWindowsException2.lastError() == 87))
/*     */           try {
/* 105 */             if (WindowsFileStore.create(this.file).type().equals("FAT")) {
/* 106 */               WindowsNativeDispatcher.SetFileTime(l, adjustForFatEpoch(paramLong1), adjustForFatEpoch(paramLong2), adjustForFatEpoch(paramLong3));
/*     */ 
/* 111 */               localObject1 = null;
/*     */             }
/*     */           } catch (SecurityException localSecurityException) {
/*     */           }
/*     */           catch (WindowsException localWindowsException3) {
/*     */           }
/*     */           catch (IOException localIOException) {
/*     */           }
/* 119 */         if (localObject1 != null)
/* 120 */           localObject1.rethrowAsIOException(this.file);
/*     */       } finally {
/* 122 */         WindowsNativeDispatcher.CloseHandle(l);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void setTimes(FileTime paramFileTime1, FileTime paramFileTime2, FileTime paramFileTime3)
/*     */       throws IOException
/*     */     {
/* 132 */       if ((paramFileTime1 == null) && (paramFileTime2 == null) && (paramFileTime3 == null))
/*     */       {
/* 136 */         return;
/*     */       }
/*     */ 
/* 140 */       this.file.checkWrite();
/*     */ 
/* 143 */       long l1 = paramFileTime3 == null ? -1L : WindowsFileAttributes.toWindowsTime(paramFileTime3);
/*     */ 
/* 145 */       long l2 = paramFileTime2 == null ? -1L : WindowsFileAttributes.toWindowsTime(paramFileTime2);
/*     */ 
/* 147 */       long l3 = paramFileTime1 == null ? -1L : WindowsFileAttributes.toWindowsTime(paramFileTime1);
/*     */ 
/* 149 */       setFileTimes(l1, l2, l3);
/*     */     }
/*     */   }
/*     */ 
/*     */   static class Dos extends WindowsFileAttributeViews.Basic
/*     */     implements DosFileAttributeView
/*     */   {
/*     */     private static final String READONLY_NAME = "readonly";
/*     */     private static final String ARCHIVE_NAME = "archive";
/*     */     private static final String SYSTEM_NAME = "system";
/*     */     private static final String HIDDEN_NAME = "hidden";
/*     */     private static final String ATTRIBUTES_NAME = "attributes";
/* 161 */     static final Set<String> dosAttributeNames = Util.newSet(basicAttributeNames, new String[] { "readonly", "archive", "system", "hidden", "attributes" });
/*     */ 
/*     */     Dos(WindowsPath paramWindowsPath, boolean paramBoolean)
/*     */     {
/* 166 */       super(paramBoolean);
/*     */     }
/*     */ 
/*     */     public String name()
/*     */     {
/* 171 */       return "dos";
/*     */     }
/*     */ 
/*     */     public void setAttribute(String paramString, Object paramObject)
/*     */       throws IOException
/*     */     {
/* 178 */       if (paramString.equals("readonly")) {
/* 179 */         setReadOnly(((Boolean)paramObject).booleanValue());
/* 180 */         return;
/*     */       }
/* 182 */       if (paramString.equals("archive")) {
/* 183 */         setArchive(((Boolean)paramObject).booleanValue());
/* 184 */         return;
/*     */       }
/* 186 */       if (paramString.equals("system")) {
/* 187 */         setSystem(((Boolean)paramObject).booleanValue());
/* 188 */         return;
/*     */       }
/* 190 */       if (paramString.equals("hidden")) {
/* 191 */         setHidden(((Boolean)paramObject).booleanValue());
/* 192 */         return;
/*     */       }
/* 194 */       super.setAttribute(paramString, paramObject);
/*     */     }
/*     */ 
/*     */     public Map<String, Object> readAttributes(String[] paramArrayOfString)
/*     */       throws IOException
/*     */     {
/* 201 */       AbstractBasicFileAttributeView.AttributesBuilder localAttributesBuilder = AbstractBasicFileAttributeView.AttributesBuilder.create(dosAttributeNames, paramArrayOfString);
/*     */ 
/* 203 */       WindowsFileAttributes localWindowsFileAttributes = readAttributes();
/* 204 */       addRequestedBasicAttributes(localWindowsFileAttributes, localAttributesBuilder);
/* 205 */       if (localAttributesBuilder.match("readonly"))
/* 206 */         localAttributesBuilder.add("readonly", Boolean.valueOf(localWindowsFileAttributes.isReadOnly()));
/* 207 */       if (localAttributesBuilder.match("archive"))
/* 208 */         localAttributesBuilder.add("archive", Boolean.valueOf(localWindowsFileAttributes.isArchive()));
/* 209 */       if (localAttributesBuilder.match("system"))
/* 210 */         localAttributesBuilder.add("system", Boolean.valueOf(localWindowsFileAttributes.isSystem()));
/* 211 */       if (localAttributesBuilder.match("hidden"))
/* 212 */         localAttributesBuilder.add("hidden", Boolean.valueOf(localWindowsFileAttributes.isHidden()));
/* 213 */       if (localAttributesBuilder.match("attributes"))
/* 214 */         localAttributesBuilder.add("attributes", Integer.valueOf(localWindowsFileAttributes.attributes()));
/* 215 */       return localAttributesBuilder.unmodifiableMap();
/*     */     }
/*     */ 
/*     */     private void updateAttributes(int paramInt, boolean paramBoolean)
/*     */       throws IOException
/*     */     {
/* 224 */       this.file.checkWrite();
/*     */ 
/* 228 */       String str = WindowsLinkSupport.getFinalPath(this.file, this.followLinks);
/*     */       try {
/* 230 */         int i = WindowsNativeDispatcher.GetFileAttributes(str);
/* 231 */         int j = i;
/* 232 */         if (paramBoolean)
/* 233 */           j |= paramInt;
/*     */         else {
/* 235 */           j &= (paramInt ^ 0xFFFFFFFF);
/*     */         }
/* 237 */         if (j != i)
/* 238 */           WindowsNativeDispatcher.SetFileAttributes(str, j);
/*     */       }
/*     */       catch (WindowsException localWindowsException)
/*     */       {
/* 242 */         localWindowsException.rethrowAsIOException(this.file);
/*     */       }
/*     */     }
/*     */ 
/*     */     public void setReadOnly(boolean paramBoolean) throws IOException
/*     */     {
/* 248 */       updateAttributes(1, paramBoolean);
/*     */     }
/*     */ 
/*     */     public void setHidden(boolean paramBoolean) throws IOException
/*     */     {
/* 253 */       updateAttributes(2, paramBoolean);
/*     */     }
/*     */ 
/*     */     public void setArchive(boolean paramBoolean) throws IOException
/*     */     {
/* 258 */       updateAttributes(32, paramBoolean);
/*     */     }
/*     */ 
/*     */     public void setSystem(boolean paramBoolean) throws IOException
/*     */     {
/* 263 */       updateAttributes(4, paramBoolean);
/*     */     }
/*     */ 
/*     */     void setAttributes(WindowsFileAttributes paramWindowsFileAttributes)
/*     */       throws IOException
/*     */     {
/* 272 */       int i = 0;
/* 273 */       if (paramWindowsFileAttributes.isReadOnly()) i |= 1;
/* 274 */       if (paramWindowsFileAttributes.isHidden()) i |= 2;
/* 275 */       if (paramWindowsFileAttributes.isArchive()) i |= 32;
/* 276 */       if (paramWindowsFileAttributes.isSystem()) i |= 4;
/* 277 */       updateAttributes(i, true);
/*     */ 
/* 281 */       setFileTimes(WindowsFileAttributes.toWindowsTime(paramWindowsFileAttributes.creationTime()), WindowsFileAttributes.toWindowsTime(paramWindowsFileAttributes.lastModifiedTime()), WindowsFileAttributes.toWindowsTime(paramWindowsFileAttributes.lastAccessTime()));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.WindowsFileAttributeViews
 * JD-Core Version:    0.6.2
 */