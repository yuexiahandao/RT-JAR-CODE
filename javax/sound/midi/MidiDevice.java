/*     */ package javax.sound.midi;
/*     */ 
/*     */ import java.util.List;
/*     */ 
/*     */ public abstract interface MidiDevice extends AutoCloseable
/*     */ {
/*     */   public abstract Info getDeviceInfo();
/*     */ 
/*     */   public abstract void open()
/*     */     throws MidiUnavailableException;
/*     */ 
/*     */   public abstract void close();
/*     */ 
/*     */   public abstract boolean isOpen();
/*     */ 
/*     */   public abstract long getMicrosecondPosition();
/*     */ 
/*     */   public abstract int getMaxReceivers();
/*     */ 
/*     */   public abstract int getMaxTransmitters();
/*     */ 
/*     */   public abstract Receiver getReceiver()
/*     */     throws MidiUnavailableException;
/*     */ 
/*     */   public abstract List<Receiver> getReceivers();
/*     */ 
/*     */   public abstract Transmitter getTransmitter()
/*     */     throws MidiUnavailableException;
/*     */ 
/*     */   public abstract List<Transmitter> getTransmitters();
/*     */ 
/*     */   public static class Info
/*     */   {
/*     */     private String name;
/*     */     private String vendor;
/*     */     private String description;
/*     */     private String version;
/*     */ 
/*     */     protected Info(String paramString1, String paramString2, String paramString3, String paramString4)
/*     */     {
/* 317 */       this.name = paramString1;
/* 318 */       this.vendor = paramString2;
/* 319 */       this.description = paramString3;
/* 320 */       this.version = paramString4;
/*     */     }
/*     */ 
/*     */     public final boolean equals(Object paramObject)
/*     */     {
/* 333 */       return super.equals(paramObject);
/*     */     }
/*     */ 
/*     */     public final int hashCode()
/*     */     {
/* 341 */       return super.hashCode();
/*     */     }
/*     */ 
/*     */     public final String getName()
/*     */     {
/* 351 */       return this.name;
/*     */     }
/*     */ 
/*     */     public final String getVendor()
/*     */     {
/* 360 */       return this.vendor;
/*     */     }
/*     */ 
/*     */     public final String getDescription()
/*     */     {
/* 369 */       return this.description;
/*     */     }
/*     */ 
/*     */     public final String getVersion()
/*     */     {
/* 378 */       return this.version;
/*     */     }
/*     */ 
/*     */     public final String toString()
/*     */     {
/* 388 */       return this.name;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.midi.MidiDevice
 * JD-Core Version:    0.6.2
 */