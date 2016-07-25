/*     */ package javax.management.remote;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InvalidObjectException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class NotificationResult
/*     */   implements Serializable
/*     */ {
/*     */   private static final long serialVersionUID = 1191800228721395279L;
/*     */   private long earliestSequenceNumber;
/*     */   private long nextSequenceNumber;
/*     */   private TargetedNotification[] targetedNotifications;
/*     */ 
/*     */   public NotificationResult(long paramLong1, long paramLong2, TargetedNotification[] paramArrayOfTargetedNotification)
/*     */   {
/*  82 */     validate(paramArrayOfTargetedNotification, paramLong1, paramLong2);
/*  83 */     this.earliestSequenceNumber = paramLong1;
/*  84 */     this.nextSequenceNumber = paramLong2;
/*  85 */     this.targetedNotifications = (paramArrayOfTargetedNotification.length == 0 ? paramArrayOfTargetedNotification : (TargetedNotification[])paramArrayOfTargetedNotification.clone());
/*     */   }
/*     */ 
/*     */   public long getEarliestSequenceNumber()
/*     */   {
/*  96 */     return this.earliestSequenceNumber;
/*     */   }
/*     */ 
/*     */   public long getNextSequenceNumber()
/*     */   {
/* 107 */     return this.nextSequenceNumber;
/*     */   }
/*     */ 
/*     */   public TargetedNotification[] getTargetedNotifications()
/*     */   {
/* 118 */     return this.targetedNotifications.length == 0 ? this.targetedNotifications : (TargetedNotification[])this.targetedNotifications.clone();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 129 */     return "NotificationResult: earliest=" + getEarliestSequenceNumber() + "; next=" + getNextSequenceNumber() + "; nnotifs=" + getTargetedNotifications().length;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 135 */     paramObjectInputStream.defaultReadObject();
/*     */     try {
/* 137 */       validate(this.targetedNotifications, this.earliestSequenceNumber, this.nextSequenceNumber);
/*     */ 
/* 143 */       this.targetedNotifications = (this.targetedNotifications.length == 0 ? this.targetedNotifications : (TargetedNotification[])this.targetedNotifications.clone());
/*     */     }
/*     */     catch (IllegalArgumentException localIllegalArgumentException)
/*     */     {
/* 147 */       throw new InvalidObjectException(localIllegalArgumentException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   private static void validate(TargetedNotification[] paramArrayOfTargetedNotification, long paramLong1, long paramLong2)
/*     */     throws IllegalArgumentException
/*     */   {
/* 159 */     if (paramArrayOfTargetedNotification == null)
/*     */     {
/* 161 */       throw new IllegalArgumentException("Notifications null");
/*     */     }
/*     */ 
/* 164 */     if ((paramLong1 < 0L) || (paramLong2 < 0L))
/* 165 */       throw new IllegalArgumentException("Bad sequence numbers");
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.remote.NotificationResult
 * JD-Core Version:    0.6.2
 */