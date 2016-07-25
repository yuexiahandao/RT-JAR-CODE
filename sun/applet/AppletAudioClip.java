/*     */ package sun.applet;
/*     */ 
/*     */ import com.sun.media.sound.JavaSoundAudioClip;
/*     */ import java.applet.AudioClip;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.URL;
/*     */ import java.net.URLConnection;
/*     */ 
/*     */ public class AppletAudioClip
/*     */   implements AudioClip
/*     */ {
/*  47 */   private URL url = null;
/*     */ 
/*  50 */   private AudioClip audioClip = null;
/*     */ 
/*  52 */   boolean DEBUG = false;
/*     */ 
/*     */   public AppletAudioClip(URL paramURL)
/*     */   {
/*  60 */     this.url = paramURL;
/*     */     try
/*     */     {
/*  65 */       InputStream localInputStream = paramURL.openStream();
/*  66 */       createAppletAudioClip(localInputStream);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*  70 */       if (this.DEBUG)
/*  71 */         System.err.println("IOException creating AppletAudioClip" + localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public AppletAudioClip(URLConnection paramURLConnection)
/*     */   {
/*     */     try
/*     */     {
/*  84 */       createAppletAudioClip(paramURLConnection.getInputStream());
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*  88 */       if (this.DEBUG)
/*  89 */         System.err.println("IOException creating AppletAudioClip" + localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public AppletAudioClip(byte[] paramArrayOfByte)
/*     */   {
/*     */     try
/*     */     {
/* 105 */       ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
/*     */ 
/* 107 */       createAppletAudioClip(localByteArrayInputStream);
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/* 111 */       if (this.DEBUG)
/* 112 */         System.err.println("IOException creating AppletAudioClip " + localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   void createAppletAudioClip(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 125 */       this.audioClip = new JavaSoundAudioClip(paramInputStream);
/*     */     }
/*     */     catch (Exception localException) {
/* 128 */       throw new IOException("Failed to construct the AudioClip: " + localException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized void play()
/*     */   {
/* 135 */     if (this.audioClip != null)
/* 136 */       this.audioClip.play();
/*     */   }
/*     */ 
/*     */   public synchronized void loop()
/*     */   {
/* 142 */     if (this.audioClip != null)
/* 143 */       this.audioClip.loop();
/*     */   }
/*     */ 
/*     */   public synchronized void stop()
/*     */   {
/* 148 */     if (this.audioClip != null)
/* 149 */       this.audioClip.stop();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.applet.AppletAudioClip
 * JD-Core Version:    0.6.2
 */