/*    */ package sun.nio.ch;
/*    */ 
/*    */ import java.io.FileDescriptor;
/*    */ import java.io.IOException;
/*    */ import java.nio.channels.Channel;
/*    */ import java.nio.channels.FileLock;
/*    */ import java.nio.channels.OverlappingFileLockException;
/*    */ import java.util.List;
/*    */ 
/*    */ abstract class FileLockTable
/*    */ {
/*    */   public static FileLockTable newSharedFileLockTable(Channel paramChannel, FileDescriptor paramFileDescriptor)
/*    */     throws IOException
/*    */   {
/* 47 */     return new SharedFileLockTable(paramChannel, paramFileDescriptor);
/*    */   }
/*    */ 
/*    */   public abstract void add(FileLock paramFileLock)
/*    */     throws OverlappingFileLockException;
/*    */ 
/*    */   public abstract void remove(FileLock paramFileLock);
/*    */ 
/*    */   public abstract List<FileLock> removeAll();
/*    */ 
/*    */   public abstract void replace(FileLock paramFileLock1, FileLock paramFileLock2);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.nio.ch.FileLockTable
 * JD-Core Version:    0.6.2
 */