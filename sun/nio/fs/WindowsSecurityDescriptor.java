/*     */ package sun.nio.fs;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.nio.file.ProviderMismatchException;
/*     */ import java.nio.file.attribute.AclEntry;
/*     */ import java.nio.file.attribute.AclEntry.Builder;
/*     */ import java.nio.file.attribute.AclEntryFlag;
/*     */ import java.nio.file.attribute.AclEntryPermission;
/*     */ import java.nio.file.attribute.AclEntryType;
/*     */ import java.nio.file.attribute.FileAttribute;
/*     */ import java.nio.file.attribute.UserPrincipal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.EnumSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import sun.misc.Unsafe;
/*     */ 
/*     */ class WindowsSecurityDescriptor
/*     */ {
/*  43 */   private static final Unsafe unsafe = Unsafe.getUnsafe();
/*     */   private static final short SIZEOF_ACL = 8;
/*     */   private static final short SIZEOF_ACCESS_ALLOWED_ACE = 12;
/*     */   private static final short SIZEOF_ACCESS_DENIED_ACE = 12;
/*     */   private static final short SIZEOF_SECURITY_DESCRIPTOR = 20;
/*     */   private static final short OFFSETOF_TYPE = 0;
/*     */   private static final short OFFSETOF_FLAGS = 1;
/*     */   private static final short OFFSETOF_ACCESS_MASK = 4;
/*     */   private static final short OFFSETOF_SID = 8;
/*  93 */   private static final WindowsSecurityDescriptor NULL_DESCRIPTOR = new WindowsSecurityDescriptor();
/*     */   private final List<Long> sidList;
/*     */   private final NativeBuffer aclBuffer;
/*     */   private final NativeBuffer sdBuffer;
/*     */ 
/*     */   private WindowsSecurityDescriptor()
/*     */   {
/* 104 */     this.sidList = null;
/* 105 */     this.aclBuffer = null;
/* 106 */     this.sdBuffer = null;
/*     */   }
/*     */ 
/*     */   private WindowsSecurityDescriptor(List<AclEntry> paramList)
/*     */     throws IOException
/*     */   {
/* 113 */     int i = 0;
/*     */ 
/* 116 */     paramList = new ArrayList(paramList);
/*     */ 
/* 119 */     this.sidList = new ArrayList(paramList.size());
/*     */     try
/*     */     {
/* 122 */       int j = 8;
/*     */ 
/* 125 */       for (Iterator localIterator = paramList.iterator(); localIterator.hasNext(); ) { localAclEntry = (AclEntry)localIterator.next();
/* 126 */         UserPrincipal localUserPrincipal = localAclEntry.principal();
/* 127 */         if (!(localUserPrincipal instanceof WindowsUserPrincipals.User))
/* 128 */           throw new ProviderMismatchException();
/* 129 */         String str = ((WindowsUserPrincipals.User)localUserPrincipal).sidString();
/*     */         try {
/* 131 */           long l2 = WindowsNativeDispatcher.ConvertStringSidToSid(str);
/* 132 */           this.sidList.add(Long.valueOf(l2));
/*     */ 
/* 135 */           j += WindowsNativeDispatcher.GetLengthSid(l2) + Math.max(12, 12);
/*     */         }
/*     */         catch (WindowsException localWindowsException2)
/*     */         {
/* 139 */           throw new IOException("Failed to get SID for " + localUserPrincipal.getName() + ": " + localWindowsException2.errorString());
/*     */         }
/*     */       }
/*     */       AclEntry localAclEntry;
/* 145 */       this.aclBuffer = NativeBuffers.getNativeBuffer(j);
/* 146 */       this.sdBuffer = NativeBuffers.getNativeBuffer(20);
/*     */ 
/* 148 */       WindowsNativeDispatcher.InitializeAcl(this.aclBuffer.address(), j);
/*     */ 
/* 151 */       int k = 0;
/* 152 */       while (k < paramList.size()) {
/* 153 */         localAclEntry = (AclEntry)paramList.get(k);
/* 154 */         long l1 = ((Long)this.sidList.get(k)).longValue();
/*     */         try {
/* 156 */           encode(localAclEntry, l1, this.aclBuffer.address());
/*     */         } catch (WindowsException localWindowsException3) {
/* 158 */           throw new IOException("Failed to encode ACE: " + localWindowsException3.errorString());
/*     */         }
/*     */ 
/* 161 */         k++;
/*     */       }
/*     */ 
/* 165 */       WindowsNativeDispatcher.InitializeSecurityDescriptor(this.sdBuffer.address());
/* 166 */       WindowsNativeDispatcher.SetSecurityDescriptorDacl(this.sdBuffer.address(), this.aclBuffer.address());
/* 167 */       i = 1;
/*     */     } catch (WindowsException localWindowsException1) {
/* 169 */       throw new IOException(localWindowsException1.getMessage());
/*     */     }
/*     */     finally {
/* 172 */       if (i == 0)
/* 173 */         release();
/*     */     }
/*     */   }
/*     */ 
/*     */   void release()
/*     */   {
/* 181 */     if (this.sdBuffer != null)
/* 182 */       this.sdBuffer.release();
/* 183 */     if (this.aclBuffer != null)
/* 184 */       this.aclBuffer.release();
/* 185 */     if (this.sidList != null)
/*     */     {
/* 187 */       for (Long localLong : this.sidList)
/* 188 */         WindowsNativeDispatcher.LocalFree(localLong.longValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   long address()
/*     */   {
/* 197 */     return this.sdBuffer == null ? 0L : this.sdBuffer.address();
/*     */   }
/*     */ 
/*     */   private static AclEntry decode(long paramLong)
/*     */     throws IOException
/*     */   {
/* 205 */     int i = unsafe.getByte(paramLong + 0L);
/* 206 */     if ((i != 0) && (i != 1))
/* 207 */       return null;
/*     */     AclEntryType localAclEntryType;
/* 209 */     if (i == 0)
/* 210 */       localAclEntryType = AclEntryType.ALLOW;
/*     */     else {
/* 212 */       localAclEntryType = AclEntryType.DENY;
/*     */     }
/*     */ 
/* 216 */     int j = unsafe.getByte(paramLong + 1L);
/* 217 */     EnumSet localEnumSet1 = EnumSet.noneOf(AclEntryFlag.class);
/* 218 */     if ((j & 0x1) != 0)
/* 219 */       localEnumSet1.add(AclEntryFlag.FILE_INHERIT);
/* 220 */     if ((j & 0x2) != 0)
/* 221 */       localEnumSet1.add(AclEntryFlag.DIRECTORY_INHERIT);
/* 222 */     if ((j & 0x4) != 0)
/* 223 */       localEnumSet1.add(AclEntryFlag.NO_PROPAGATE_INHERIT);
/* 224 */     if ((j & 0x8) != 0) {
/* 225 */       localEnumSet1.add(AclEntryFlag.INHERIT_ONLY);
/*     */     }
/*     */ 
/* 228 */     int k = unsafe.getInt(paramLong + 4L);
/* 229 */     EnumSet localEnumSet2 = EnumSet.noneOf(AclEntryPermission.class);
/* 230 */     if ((k & 0x1) > 0)
/* 231 */       localEnumSet2.add(AclEntryPermission.READ_DATA);
/* 232 */     if ((k & 0x2) > 0)
/* 233 */       localEnumSet2.add(AclEntryPermission.WRITE_DATA);
/* 234 */     if ((k & 0x4) > 0)
/* 235 */       localEnumSet2.add(AclEntryPermission.APPEND_DATA);
/* 236 */     if ((k & 0x8) > 0)
/* 237 */       localEnumSet2.add(AclEntryPermission.READ_NAMED_ATTRS);
/* 238 */     if ((k & 0x10) > 0)
/* 239 */       localEnumSet2.add(AclEntryPermission.WRITE_NAMED_ATTRS);
/* 240 */     if ((k & 0x20) > 0)
/* 241 */       localEnumSet2.add(AclEntryPermission.EXECUTE);
/* 242 */     if ((k & 0x40) > 0)
/* 243 */       localEnumSet2.add(AclEntryPermission.DELETE_CHILD);
/* 244 */     if ((k & 0x80) > 0)
/* 245 */       localEnumSet2.add(AclEntryPermission.READ_ATTRIBUTES);
/* 246 */     if ((k & 0x100) > 0)
/* 247 */       localEnumSet2.add(AclEntryPermission.WRITE_ATTRIBUTES);
/* 248 */     if ((k & 0x10000) > 0)
/* 249 */       localEnumSet2.add(AclEntryPermission.DELETE);
/* 250 */     if ((k & 0x20000) > 0)
/* 251 */       localEnumSet2.add(AclEntryPermission.READ_ACL);
/* 252 */     if ((k & 0x40000) > 0)
/* 253 */       localEnumSet2.add(AclEntryPermission.WRITE_ACL);
/* 254 */     if ((k & 0x80000) > 0)
/* 255 */       localEnumSet2.add(AclEntryPermission.WRITE_OWNER);
/* 256 */     if ((k & 0x100000) > 0) {
/* 257 */       localEnumSet2.add(AclEntryPermission.SYNCHRONIZE);
/*     */     }
/*     */ 
/* 260 */     long l = paramLong + 8L;
/* 261 */     UserPrincipal localUserPrincipal = WindowsUserPrincipals.fromSid(l);
/*     */ 
/* 263 */     return AclEntry.newBuilder().setType(localAclEntryType).setPrincipal(localUserPrincipal).setFlags(localEnumSet1).setPermissions(localEnumSet2).build();
/*     */   }
/*     */ 
/*     */   private static void encode(AclEntry paramAclEntry, long paramLong1, long paramLong2)
/*     */     throws WindowsException
/*     */   {
/* 274 */     if ((paramAclEntry.type() != AclEntryType.ALLOW) && (paramAclEntry.type() != AclEntryType.DENY))
/* 275 */       return;
/* 276 */     int i = paramAclEntry.type() == AclEntryType.ALLOW ? 1 : 0;
/*     */ 
/* 279 */     Set localSet1 = paramAclEntry.permissions();
/* 280 */     int j = 0;
/* 281 */     if (localSet1.contains(AclEntryPermission.READ_DATA))
/* 282 */       j |= 1;
/* 283 */     if (localSet1.contains(AclEntryPermission.WRITE_DATA))
/* 284 */       j |= 2;
/* 285 */     if (localSet1.contains(AclEntryPermission.APPEND_DATA))
/* 286 */       j |= 4;
/* 287 */     if (localSet1.contains(AclEntryPermission.READ_NAMED_ATTRS))
/* 288 */       j |= 8;
/* 289 */     if (localSet1.contains(AclEntryPermission.WRITE_NAMED_ATTRS))
/* 290 */       j |= 16;
/* 291 */     if (localSet1.contains(AclEntryPermission.EXECUTE))
/* 292 */       j |= 32;
/* 293 */     if (localSet1.contains(AclEntryPermission.DELETE_CHILD))
/* 294 */       j |= 64;
/* 295 */     if (localSet1.contains(AclEntryPermission.READ_ATTRIBUTES))
/* 296 */       j |= 128;
/* 297 */     if (localSet1.contains(AclEntryPermission.WRITE_ATTRIBUTES))
/* 298 */       j |= 256;
/* 299 */     if (localSet1.contains(AclEntryPermission.DELETE))
/* 300 */       j |= 65536;
/* 301 */     if (localSet1.contains(AclEntryPermission.READ_ACL))
/* 302 */       j |= 131072;
/* 303 */     if (localSet1.contains(AclEntryPermission.WRITE_ACL))
/* 304 */       j |= 262144;
/* 305 */     if (localSet1.contains(AclEntryPermission.WRITE_OWNER))
/* 306 */       j |= 524288;
/* 307 */     if (localSet1.contains(AclEntryPermission.SYNCHRONIZE)) {
/* 308 */       j |= 1048576;
/*     */     }
/*     */ 
/* 311 */     Set localSet2 = paramAclEntry.flags();
/* 312 */     int k = 0;
/* 313 */     if (localSet2.contains(AclEntryFlag.FILE_INHERIT))
/* 314 */       k = (byte)(k | 0x1);
/* 315 */     if (localSet2.contains(AclEntryFlag.DIRECTORY_INHERIT))
/* 316 */       k = (byte)(k | 0x2);
/* 317 */     if (localSet2.contains(AclEntryFlag.NO_PROPAGATE_INHERIT))
/* 318 */       k = (byte)(k | 0x4);
/* 319 */     if (localSet2.contains(AclEntryFlag.INHERIT_ONLY)) {
/* 320 */       k = (byte)(k | 0x8);
/*     */     }
/* 322 */     if (i != 0)
/* 323 */       WindowsNativeDispatcher.AddAccessAllowedAceEx(paramLong2, k, j, paramLong1);
/*     */     else
/* 325 */       WindowsNativeDispatcher.AddAccessDeniedAceEx(paramLong2, k, j, paramLong1);
/*     */   }
/*     */ 
/*     */   static WindowsSecurityDescriptor create(List<AclEntry> paramList)
/*     */     throws IOException
/*     */   {
/* 335 */     return new WindowsSecurityDescriptor(paramList);
/*     */   }
/*     */ 
/*     */   static WindowsSecurityDescriptor fromAttribute(FileAttribute<?>[] paramArrayOfFileAttribute)
/*     */     throws IOException
/*     */   {
/* 347 */     WindowsSecurityDescriptor localWindowsSecurityDescriptor = NULL_DESCRIPTOR;
/* 348 */     for (FileAttribute<?> localFileAttribute : paramArrayOfFileAttribute)
/*     */     {
/* 350 */       if (localWindowsSecurityDescriptor != NULL_DESCRIPTOR)
/* 351 */         localWindowsSecurityDescriptor.release();
/* 352 */       if (localFileAttribute == null)
/* 353 */         throw new NullPointerException();
/* 354 */       if (localFileAttribute.name().equals("acl:acl")) {
/* 355 */         List localList = (List)localFileAttribute.value();
/* 356 */         localWindowsSecurityDescriptor = new WindowsSecurityDescriptor(localList);
/*     */       } else {
/* 358 */         throw new UnsupportedOperationException("'" + localFileAttribute.name() + "' not supported as initial attribute");
/*     */       }
/*     */     }
/*     */ 
/* 362 */     return localWindowsSecurityDescriptor;
/*     */   }
/*     */ 
/*     */   static List<AclEntry> getAcl(long paramLong)
/*     */     throws IOException
/*     */   {
/* 370 */     long l1 = WindowsNativeDispatcher.GetSecurityDescriptorDacl(paramLong);
/*     */ 
/* 373 */     int i = 0;
/* 374 */     if (l1 == 0L)
/*     */     {
/* 376 */       i = 0;
/*     */     } else {
/* 378 */       localObject = WindowsNativeDispatcher.GetAclInformation(l1);
/* 379 */       i = ((WindowsNativeDispatcher.AclInformation)localObject).aceCount();
/*     */     }
/* 381 */     Object localObject = new ArrayList(i);
/*     */ 
/* 384 */     for (int j = 0; j < i; j++) {
/* 385 */       long l2 = WindowsNativeDispatcher.GetAce(l1, j);
/* 386 */       AclEntry localAclEntry = decode(l2);
/* 387 */       if (localAclEntry != null)
/* 388 */         ((ArrayList)localObject).add(localAclEntry);
/*     */     }
/* 390 */     return localObject;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.fs.WindowsSecurityDescriptor
 * JD-Core Version:    0.6.2
 */