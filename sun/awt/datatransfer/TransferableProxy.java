/*    */ package sun.awt.datatransfer;
/*    */ 
/*    */ import java.awt.datatransfer.DataFlavor;
/*    */ import java.awt.datatransfer.Transferable;
/*    */ import java.awt.datatransfer.UnsupportedFlavorException;
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ 
/*    */ public class TransferableProxy
/*    */   implements Transferable
/*    */ {
/*    */   protected final Transferable transferable;
/*    */   protected final boolean isLocal;
/*    */ 
/*    */   public TransferableProxy(Transferable paramTransferable, boolean paramBoolean)
/*    */   {
/* 61 */     this.transferable = paramTransferable;
/* 62 */     this.isLocal = paramBoolean;
/*    */   }
/*    */   public DataFlavor[] getTransferDataFlavors() {
/* 65 */     return this.transferable.getTransferDataFlavors();
/*    */   }
/*    */   public boolean isDataFlavorSupported(DataFlavor paramDataFlavor) {
/* 68 */     return this.transferable.isDataFlavorSupported(paramDataFlavor);
/*    */   }
/*    */ 
/*    */   public Object getTransferData(DataFlavor paramDataFlavor) throws UnsupportedFlavorException, IOException
/*    */   {
/* 73 */     Object localObject = this.transferable.getTransferData(paramDataFlavor);
/*    */ 
/* 78 */     if ((localObject != null) && (this.isLocal) && (paramDataFlavor.isFlavorSerializedObjectType())) {
/* 79 */       ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
/*    */ 
/* 81 */       ClassLoaderObjectOutputStream localClassLoaderObjectOutputStream = new ClassLoaderObjectOutputStream(localByteArrayOutputStream);
/*    */ 
/* 83 */       localClassLoaderObjectOutputStream.writeObject(localObject);
/*    */ 
/* 85 */       ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(localByteArrayOutputStream.toByteArray());
/*    */       try
/*    */       {
/* 89 */         ClassLoaderObjectInputStream localClassLoaderObjectInputStream = new ClassLoaderObjectInputStream(localByteArrayInputStream, localClassLoaderObjectOutputStream.getClassLoaderMap());
/*    */ 
/* 92 */         localObject = localClassLoaderObjectInputStream.readObject();
/*    */       } catch (ClassNotFoundException localClassNotFoundException) {
/* 94 */         throw ((IOException)new IOException().initCause(localClassNotFoundException));
/*    */       }
/*    */     }
/*    */ 
/* 98 */     return localObject;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.datatransfer.TransferableProxy
 * JD-Core Version:    0.6.2
 */