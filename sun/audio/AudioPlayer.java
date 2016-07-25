/*     */ package sun.audio;
/*     */ 
/*     */ import java.io.InputStream;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ 
/*     */ public final class AudioPlayer extends Thread
/*     */ {
/*     */   private final AudioDevice devAudio;
/*     */   private static final boolean DEBUG = false;
/*  75 */   public static final AudioPlayer player = getAudioPlayer();
/*     */ 
/*     */   private static ThreadGroup getAudioThreadGroup()
/*     */   {
/*  80 */     ThreadGroup localThreadGroup = currentThread().getThreadGroup();
/*  81 */     while ((localThreadGroup.getParent() != null) && (localThreadGroup.getParent().getParent() != null))
/*     */     {
/*  83 */       localThreadGroup = localThreadGroup.getParent();
/*     */     }
/*  85 */     return localThreadGroup;
/*     */   }
/*     */ 
/*     */   private static AudioPlayer getAudioPlayer()
/*     */   {
/*  96 */     PrivilegedAction local1 = new PrivilegedAction() {
/*     */       public Object run() {
/*  98 */         AudioPlayer localAudioPlayer = new AudioPlayer(null);
/*  99 */         localAudioPlayer.setPriority(10);
/* 100 */         localAudioPlayer.setDaemon(true);
/* 101 */         localAudioPlayer.start();
/* 102 */         return localAudioPlayer;
/*     */       }
/*     */     };
/* 105 */     AudioPlayer localAudioPlayer = (AudioPlayer)AccessController.doPrivileged(local1);
/* 106 */     return localAudioPlayer;
/*     */   }
/*     */ 
/*     */   private AudioPlayer()
/*     */   {
/* 114 */     super(getAudioThreadGroup(), "Audio Player");
/*     */ 
/* 116 */     this.devAudio = AudioDevice.device;
/* 117 */     this.devAudio.open();
/*     */   }
/*     */ 
/*     */   public synchronized void start(InputStream paramInputStream)
/*     */   {
/* 133 */     this.devAudio.openChannel(paramInputStream);
/* 134 */     notify();
/*     */   }
/*     */ 
/*     */   public synchronized void stop(InputStream paramInputStream)
/*     */   {
/* 152 */     this.devAudio.closeChannel(paramInputStream);
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 170 */     this.devAudio.play();
/*     */     try
/*     */     {
/*     */       while (true)
/*     */       {
/* 176 */         Thread.sleep(5000L);
/*     */       }
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.audio.AudioPlayer
 * JD-Core Version:    0.6.2
 */