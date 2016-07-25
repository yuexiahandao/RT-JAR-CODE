/*    */ package sun.awt.image;
/*    */ 
/*    */ import java.awt.image.ImageConsumer;
/*    */ 
/*    */ class ImageConsumerQueue
/*    */ {
/*    */   ImageConsumerQueue next;
/*    */   ImageConsumer consumer;
/*    */   boolean interested;
/*    */   Object securityContext;
/*    */   boolean secure;
/*    */ 
/*    */   static ImageConsumerQueue removeConsumer(ImageConsumerQueue paramImageConsumerQueue, ImageConsumer paramImageConsumer, boolean paramBoolean)
/*    */   {
/* 43 */     Object localObject = null;
/* 44 */     for (ImageConsumerQueue localImageConsumerQueue = paramImageConsumerQueue; localImageConsumerQueue != null; localImageConsumerQueue = localImageConsumerQueue.next) {
/* 45 */       if (localImageConsumerQueue.consumer == paramImageConsumer) {
/* 46 */         if (localObject == null)
/* 47 */           paramImageConsumerQueue = localImageConsumerQueue.next;
/*    */         else {
/* 49 */           localObject.next = localImageConsumerQueue.next;
/*    */         }
/* 51 */         localImageConsumerQueue.interested = paramBoolean;
/* 52 */         break;
/*    */       }
/* 54 */       localObject = localImageConsumerQueue;
/*    */     }
/* 56 */     return paramImageConsumerQueue;
/*    */   }
/*    */ 
/*    */   static boolean isConsumer(ImageConsumerQueue paramImageConsumerQueue, ImageConsumer paramImageConsumer) {
/* 60 */     for (ImageConsumerQueue localImageConsumerQueue = paramImageConsumerQueue; localImageConsumerQueue != null; localImageConsumerQueue = localImageConsumerQueue.next) {
/* 61 */       if (localImageConsumerQueue.consumer == paramImageConsumer) {
/* 62 */         return true;
/*    */       }
/*    */     }
/* 65 */     return false;
/*    */   }
/*    */ 
/*    */   ImageConsumerQueue(InputStreamImageSource paramInputStreamImageSource, ImageConsumer paramImageConsumer) {
/* 69 */     this.consumer = paramImageConsumer;
/* 70 */     this.interested = true;
/*    */     Object localObject;
/* 72 */     if ((paramImageConsumer instanceof ImageRepresentation)) {
/* 73 */       localObject = (ImageRepresentation)paramImageConsumer;
/* 74 */       if (((ImageRepresentation)localObject).image.source != paramInputStreamImageSource) {
/* 75 */         throw new SecurityException("ImageRep added to wrong image source");
/*    */       }
/* 77 */       this.secure = true;
/*    */     } else {
/* 79 */       localObject = System.getSecurityManager();
/* 80 */       if (localObject != null)
/* 81 */         this.securityContext = ((SecurityManager)localObject).getSecurityContext();
/*    */       else
/* 83 */         this.securityContext = null;
/*    */     }
/*    */   }
/*    */ 
/*    */   public String toString()
/*    */   {
/* 89 */     return "[" + this.consumer + ", " + (this.interested ? "" : "not ") + "interested" + (this.securityContext != null ? ", " + this.securityContext : "") + "]";
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.ImageConsumerQueue
 * JD-Core Version:    0.6.2
 */