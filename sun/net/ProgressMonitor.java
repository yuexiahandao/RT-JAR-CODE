/*     */ package sun.net;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ 
/*     */ public class ProgressMonitor
/*     */ {
/* 223 */   private static ProgressMeteringPolicy meteringPolicy = new DefaultProgressMeteringPolicy();
/*     */ 
/* 226 */   private static ProgressMonitor pm = new ProgressMonitor();
/*     */ 
/* 229 */   private ArrayList<ProgressSource> progressSourceList = new ArrayList();
/*     */ 
/* 232 */   private ArrayList<ProgressListener> progressListenerList = new ArrayList();
/*     */ 
/*     */   public static synchronized ProgressMonitor getDefault()
/*     */   {
/*  43 */     return pm;
/*     */   }
/*     */ 
/*     */   public static synchronized void setDefault(ProgressMonitor paramProgressMonitor)
/*     */   {
/*  50 */     if (paramProgressMonitor != null)
/*  51 */       pm = paramProgressMonitor;
/*     */   }
/*     */ 
/*     */   public static synchronized void setMeteringPolicy(ProgressMeteringPolicy paramProgressMeteringPolicy)
/*     */   {
/*  58 */     if (paramProgressMeteringPolicy != null)
/*  59 */       meteringPolicy = paramProgressMeteringPolicy;
/*     */   }
/*     */ 
/*     */   public ArrayList<ProgressSource> getProgressSources()
/*     */   {
/*  67 */     ArrayList localArrayList = new ArrayList();
/*     */     try
/*     */     {
/*     */       Iterator localIterator;
/*  70 */       synchronized (this.progressSourceList) {
/*  71 */         for (localIterator = this.progressSourceList.iterator(); localIterator.hasNext(); ) {
/*  72 */           ProgressSource localProgressSource = (ProgressSource)localIterator.next();
/*     */ 
/*  75 */           localArrayList.add((ProgressSource)localProgressSource.clone());
/*     */         }
/*     */       }
/*     */     }
/*     */     catch (CloneNotSupportedException localCloneNotSupportedException) {
/*  80 */       localCloneNotSupportedException.printStackTrace();
/*     */     }
/*     */ 
/*  83 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   public synchronized int getProgressUpdateThreshold()
/*     */   {
/*  90 */     return meteringPolicy.getProgressUpdateThreshold();
/*     */   }
/*     */ 
/*     */   public boolean shouldMeterInput(URL paramURL, String paramString)
/*     */   {
/*  98 */     return meteringPolicy.shouldMeterInput(paramURL, paramString);
/*     */   }
/*     */ 
/*     */   public void registerSource(ProgressSource paramProgressSource)
/*     */   {
/* 106 */     synchronized (this.progressSourceList) {
/* 107 */       if (this.progressSourceList.contains(paramProgressSource)) {
/* 108 */         return;
/*     */       }
/* 110 */       this.progressSourceList.add(paramProgressSource);
/*     */     }
/*     */     Object localObject2;
/* 114 */     if (this.progressListenerList.size() > 0)
/*     */     {
/* 117 */       ??? = new ArrayList();
/*     */ 
/* 120 */       synchronized (this.progressListenerList) {
/* 121 */         for (localObject2 = this.progressListenerList.iterator(); ((Iterator)localObject2).hasNext(); ) {
/* 122 */           ((ArrayList)???).add(((Iterator)localObject2).next());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 127 */       for (??? = ((ArrayList)???).iterator(); ((Iterator)???).hasNext(); ) {
/* 128 */         localObject2 = (ProgressListener)((Iterator)???).next();
/* 129 */         ProgressEvent localProgressEvent = new ProgressEvent(paramProgressSource, paramProgressSource.getURL(), paramProgressSource.getMethod(), paramProgressSource.getContentType(), paramProgressSource.getState(), paramProgressSource.getProgress(), paramProgressSource.getExpected());
/* 130 */         ((ProgressListener)localObject2).progressStart(localProgressEvent);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void unregisterSource(ProgressSource paramProgressSource)
/*     */   {
/* 140 */     synchronized (this.progressSourceList)
/*     */     {
/* 142 */       if (!this.progressSourceList.contains(paramProgressSource)) {
/* 143 */         return;
/*     */       }
/*     */ 
/* 146 */       paramProgressSource.close();
/* 147 */       this.progressSourceList.remove(paramProgressSource);
/*     */     }
/*     */     Object localObject2;
/* 151 */     if (this.progressListenerList.size() > 0)
/*     */     {
/* 154 */       ??? = new ArrayList();
/*     */ 
/* 157 */       synchronized (this.progressListenerList) {
/* 158 */         for (localObject2 = this.progressListenerList.iterator(); ((Iterator)localObject2).hasNext(); ) {
/* 159 */           ((ArrayList)???).add(((Iterator)localObject2).next());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 164 */       for (??? = ((ArrayList)???).iterator(); ((Iterator)???).hasNext(); ) {
/* 165 */         localObject2 = (ProgressListener)((Iterator)???).next();
/* 166 */         ProgressEvent localProgressEvent = new ProgressEvent(paramProgressSource, paramProgressSource.getURL(), paramProgressSource.getMethod(), paramProgressSource.getContentType(), paramProgressSource.getState(), paramProgressSource.getProgress(), paramProgressSource.getExpected());
/* 167 */         ((ProgressListener)localObject2).progressFinish(localProgressEvent);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void updateProgress(ProgressSource paramProgressSource)
/*     */   {
/* 177 */     synchronized (this.progressSourceList) {
/* 178 */       if (!this.progressSourceList.contains(paramProgressSource))
/*     */         return;
/*     */     }
/*     */     Object localObject2;
/* 183 */     if (this.progressListenerList.size() > 0)
/*     */     {
/* 186 */       ??? = new ArrayList();
/*     */ 
/* 189 */       synchronized (this.progressListenerList) {
/* 190 */         for (localObject2 = this.progressListenerList.iterator(); ((Iterator)localObject2).hasNext(); ) {
/* 191 */           ((ArrayList)???).add(((Iterator)localObject2).next());
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 196 */       for (??? = ((ArrayList)???).iterator(); ((Iterator)???).hasNext(); ) {
/* 197 */         localObject2 = (ProgressListener)((Iterator)???).next();
/* 198 */         ProgressEvent localProgressEvent = new ProgressEvent(paramProgressSource, paramProgressSource.getURL(), paramProgressSource.getMethod(), paramProgressSource.getContentType(), paramProgressSource.getState(), paramProgressSource.getProgress(), paramProgressSource.getExpected());
/* 199 */         ((ProgressListener)localObject2).progressUpdate(localProgressEvent);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addProgressListener(ProgressListener paramProgressListener)
/*     */   {
/* 208 */     synchronized (this.progressListenerList) {
/* 209 */       this.progressListenerList.add(paramProgressListener);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void removeProgressListener(ProgressListener paramProgressListener)
/*     */   {
/* 217 */     synchronized (this.progressListenerList) {
/* 218 */       this.progressListenerList.remove(paramProgressListener);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.ProgressMonitor
 * JD-Core Version:    0.6.2
 */