/*     */ package sun.nio.ch;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.lang.ref.ReferenceQueue;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.nio.channels.Channel;
/*     */ import java.nio.channels.FileLock;
/*     */ import java.nio.channels.OverlappingFileLockException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ 
/*     */ class SharedFileLockTable extends FileLockTable
/*     */ {
/* 106 */   private static ConcurrentHashMap<FileKey, List<FileLockReference>> lockMap = new ConcurrentHashMap();
/*     */ 
/* 110 */   private static ReferenceQueue<FileLock> queue = new ReferenceQueue();
/*     */   private final Channel channel;
/*     */   private final FileKey fileKey;
/*     */ 
/*     */   SharedFileLockTable(Channel paramChannel, FileDescriptor paramFileDescriptor)
/*     */     throws IOException
/*     */   {
/* 119 */     this.channel = paramChannel;
/* 120 */     this.fileKey = FileKey.create(paramFileDescriptor);
/*     */   }
/*     */ 
/*     */   public void add(FileLock paramFileLock) throws OverlappingFileLockException
/*     */   {
/* 125 */     Object localObject1 = (List)lockMap.get(this.fileKey);
/*     */     while (true)
/*     */     {
/* 130 */       if (localObject1 == null) {
/* 131 */         localObject1 = new ArrayList(2);
/*     */         List localList;
/* 133 */         synchronized (localObject1) {
/* 134 */           localList = (List)lockMap.putIfAbsent(this.fileKey, localObject1);
/* 135 */           if (localList == null)
/*     */           {
/* 137 */             ((List)localObject1).add(new FileLockReference(paramFileLock, queue, this.fileKey));
/* 138 */             break;
/*     */           }
/*     */         }
/*     */ 
/* 142 */         localObject1 = localList;
/*     */       }
/*     */ 
/* 149 */       synchronized (localObject1) {
/* 150 */         ??? = (List)lockMap.get(this.fileKey);
/* 151 */         if (localObject1 == ???) {
/* 152 */           checkList((List)localObject1, paramFileLock.position(), paramFileLock.size());
/* 153 */           ((List)localObject1).add(new FileLockReference(paramFileLock, queue, this.fileKey));
/* 154 */           break;
/*     */         }
/* 156 */         localObject1 = ???;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 162 */     removeStaleEntries();
/*     */   }
/*     */ 
/*     */   private void removeKeyIfEmpty(FileKey paramFileKey, List<FileLockReference> paramList) {
/* 166 */     assert (Thread.holdsLock(paramList));
/* 167 */     assert (lockMap.get(paramFileKey) == paramList);
/* 168 */     if (paramList.isEmpty())
/* 169 */       lockMap.remove(paramFileKey);
/*     */   }
/*     */ 
/*     */   public void remove(FileLock paramFileLock)
/*     */   {
/* 175 */     assert (paramFileLock != null);
/*     */ 
/* 178 */     List localList = (List)lockMap.get(this.fileKey);
/* 179 */     if (localList == null) return;
/*     */ 
/* 181 */     synchronized (localList) {
/* 182 */       int i = 0;
/* 183 */       while (i < localList.size()) {
/* 184 */         FileLockReference localFileLockReference = (FileLockReference)localList.get(i);
/* 185 */         FileLock localFileLock = (FileLock)localFileLockReference.get();
/* 186 */         if (localFileLock == paramFileLock) {
/* 187 */           assert ((localFileLock != null) && (localFileLock.acquiredBy() == this.channel));
/* 188 */           localFileLockReference.clear();
/* 189 */           localList.remove(i);
/* 190 */           break;
/*     */         }
/* 192 */         i++;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public List<FileLock> removeAll()
/*     */   {
/* 199 */     ArrayList localArrayList = new ArrayList();
/* 200 */     List localList = (List)lockMap.get(this.fileKey);
/* 201 */     if (localList != null) {
/* 202 */       synchronized (localList) {
/* 203 */         int i = 0;
/* 204 */         while (i < localList.size()) {
/* 205 */           FileLockReference localFileLockReference = (FileLockReference)localList.get(i);
/* 206 */           FileLock localFileLock = (FileLock)localFileLockReference.get();
/*     */ 
/* 209 */           if ((localFileLock != null) && (localFileLock.acquiredBy() == this.channel))
/*     */           {
/* 211 */             localFileLockReference.clear();
/* 212 */             localList.remove(i);
/*     */ 
/* 215 */             localArrayList.add(localFileLock);
/*     */           } else {
/* 217 */             i++;
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 222 */         removeKeyIfEmpty(this.fileKey, localList);
/*     */       }
/*     */     }
/* 225 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public void replace(FileLock paramFileLock1, FileLock paramFileLock2)
/*     */   {
/* 231 */     List localList = (List)lockMap.get(this.fileKey);
/* 232 */     assert (localList != null);
/*     */ 
/* 234 */     synchronized (localList) {
/* 235 */       for (int i = 0; i < localList.size(); i++) {
/* 236 */         FileLockReference localFileLockReference = (FileLockReference)localList.get(i);
/* 237 */         FileLock localFileLock = (FileLock)localFileLockReference.get();
/* 238 */         if (localFileLock == paramFileLock1) {
/* 239 */           localFileLockReference.clear();
/* 240 */           localList.set(i, new FileLockReference(paramFileLock2, queue, this.fileKey));
/* 241 */           break;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkList(List<FileLockReference> paramList, long paramLong1, long paramLong2)
/*     */     throws OverlappingFileLockException
/*     */   {
/* 251 */     assert (Thread.holdsLock(paramList));
/* 252 */     for (FileLockReference localFileLockReference : paramList) {
/* 253 */       FileLock localFileLock = (FileLock)localFileLockReference.get();
/* 254 */       if ((localFileLock != null) && (localFileLock.overlaps(paramLong1, paramLong2)))
/* 255 */         throw new OverlappingFileLockException();
/*     */     }
/*     */   }
/*     */ 
/*     */   private void removeStaleEntries()
/*     */   {
/*     */     FileLockReference localFileLockReference;
/* 262 */     while ((localFileLockReference = (FileLockReference)queue.poll()) != null) {
/* 263 */       FileKey localFileKey = localFileLockReference.fileKey();
/* 264 */       List localList = (List)lockMap.get(localFileKey);
/* 265 */       if (localList != null)
/* 266 */         synchronized (localList) {
/* 267 */           localList.remove(localFileLockReference);
/* 268 */           removeKeyIfEmpty(localFileKey, localList);
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class FileLockReference extends WeakReference<FileLock>
/*     */   {
/*     */     private FileKey fileKey;
/*     */ 
/*     */     FileLockReference(FileLock paramFileLock, ReferenceQueue<FileLock> paramReferenceQueue, FileKey paramFileKey)
/*     */     {
/*  94 */       super(paramReferenceQueue);
/*  95 */       this.fileKey = paramFileKey;
/*     */     }
/*     */ 
/*     */     FileKey fileKey() {
/*  99 */       return this.fileKey;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.SharedFileLockTable
 * JD-Core Version:    0.6.2
 */