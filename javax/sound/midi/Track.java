/*     */ package javax.sound.midi;
/*     */ 
/*     */ import com.sun.media.sound.MidiUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashSet;
/*     */ 
/*     */ public class Track
/*     */ {
/*  69 */   private ArrayList eventsList = new ArrayList();
/*     */ 
/*  72 */   private HashSet set = new HashSet();
/*     */   private MidiEvent eotEvent;
/*     */ 
/*     */   Track()
/*     */   {
/*  83 */     ImmutableEndOfTrack localImmutableEndOfTrack = new ImmutableEndOfTrack(null);
/*  84 */     this.eotEvent = new MidiEvent(localImmutableEndOfTrack, 0L);
/*  85 */     this.eventsList.add(this.eotEvent);
/*  86 */     this.set.add(this.eotEvent);
/*     */   }
/*     */ 
/*     */   public boolean add(MidiEvent paramMidiEvent)
/*     */   {
/* 100 */     if (paramMidiEvent == null) {
/* 101 */       return false;
/*     */     }
/* 103 */     synchronized (this.eventsList)
/*     */     {
/* 105 */       if (!this.set.contains(paramMidiEvent)) {
/* 106 */         int i = this.eventsList.size();
/*     */ 
/* 109 */         MidiEvent localMidiEvent = null;
/* 110 */         if (i > 0) {
/* 111 */           localMidiEvent = (MidiEvent)this.eventsList.get(i - 1);
/*     */         }
/*     */ 
/* 114 */         if (localMidiEvent != this.eotEvent)
/*     */         {
/* 116 */           if (localMidiEvent != null)
/*     */           {
/* 118 */             this.eotEvent.setTick(localMidiEvent.getTick());
/*     */           }
/*     */           else {
/* 121 */             this.eotEvent.setTick(0L);
/*     */           }
/*     */ 
/* 125 */           this.eventsList.add(this.eotEvent);
/* 126 */           this.set.add(this.eotEvent);
/* 127 */           i = this.eventsList.size();
/*     */         }
/*     */ 
/* 132 */         if (MidiUtils.isMetaEndOfTrack(paramMidiEvent.getMessage()))
/*     */         {
/* 138 */           if (paramMidiEvent.getTick() > this.eotEvent.getTick()) {
/* 139 */             this.eotEvent.setTick(paramMidiEvent.getTick());
/*     */           }
/* 141 */           return true;
/*     */         }
/*     */ 
/* 145 */         this.set.add(paramMidiEvent);
/*     */ 
/* 149 */         int j = i;
/* 150 */         while ((j > 0) && 
/* 151 */           (paramMidiEvent.getTick() < ((MidiEvent)this.eventsList.get(j - 1)).getTick())) {
/* 150 */           j--;
/*     */         }
/*     */ 
/* 155 */         if (j == i)
/*     */         {
/* 162 */           this.eventsList.set(i - 1, paramMidiEvent);
/*     */ 
/* 164 */           if (this.eotEvent.getTick() < paramMidiEvent.getTick()) {
/* 165 */             this.eotEvent.setTick(paramMidiEvent.getTick());
/*     */           }
/*     */ 
/* 168 */           this.eventsList.add(this.eotEvent);
/*     */         } else {
/* 170 */           this.eventsList.add(j, paramMidiEvent);
/*     */         }
/* 172 */         return true;
/*     */       }
/*     */     }
/*     */ 
/* 176 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean remove(MidiEvent paramMidiEvent)
/*     */   {
/* 198 */     synchronized (this.eventsList) {
/* 199 */       if (this.set.remove(paramMidiEvent)) {
/* 200 */         int i = this.eventsList.indexOf(paramMidiEvent);
/* 201 */         if (i >= 0) {
/* 202 */           this.eventsList.remove(i);
/* 203 */           return true;
/*     */         }
/*     */       }
/*     */     }
/* 207 */     return false;
/*     */   }
/*     */ 
/*     */   public MidiEvent get(int paramInt)
/*     */     throws ArrayIndexOutOfBoundsException
/*     */   {
/*     */     try
/*     */     {
/* 221 */       synchronized (this.eventsList) {
/* 222 */         return (MidiEvent)this.eventsList.get(paramInt);
/*     */       }
/*     */     } catch (IndexOutOfBoundsException localIndexOutOfBoundsException) {
/* 225 */       throw new ArrayIndexOutOfBoundsException(localIndexOutOfBoundsException.getMessage());
/*     */     }
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 235 */     synchronized (this.eventsList) {
/* 236 */       return this.eventsList.size();
/*     */     }
/*     */   }
/*     */ 
/*     */   public long ticks()
/*     */   {
/* 252 */     long l = 0L;
/* 253 */     synchronized (this.eventsList) {
/* 254 */       if (this.eventsList.size() > 0) {
/* 255 */         l = ((MidiEvent)this.eventsList.get(this.eventsList.size() - 1)).getTick();
/*     */       }
/*     */     }
/* 258 */     return l;
/*     */   }
/*     */ 
/*     */   private static class ImmutableEndOfTrack extends MetaMessage {
/*     */     private ImmutableEndOfTrack() {
/* 263 */       super();
/* 264 */       this.data[0] = -1;
/* 265 */       this.data[1] = 47;
/* 266 */       this.data[2] = 0;
/*     */     }
/*     */ 
/*     */     public void setMessage(int paramInt1, byte[] paramArrayOfByte, int paramInt2) throws InvalidMidiDataException {
/* 270 */       throw new InvalidMidiDataException("cannot modify end of track message");
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.midi.Track
 * JD-Core Version:    0.6.2
 */