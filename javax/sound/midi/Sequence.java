/*     */ package javax.sound.midi;
/*     */ 
/*     */ import com.sun.media.sound.MidiUtils;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class Sequence
/*     */ {
/*     */   public static final float PPQ = 0.0F;
/*     */   public static final float SMPTE_24 = 24.0F;
/*     */   public static final float SMPTE_25 = 25.0F;
/*     */   public static final float SMPTE_30DROP = 29.969999F;
/*     */   public static final float SMPTE_30 = 30.0F;
/*     */   protected float divisionType;
/*     */   protected int resolution;
/* 114 */   protected Vector<Track> tracks = new Vector();
/*     */ 
/*     */   public Sequence(float paramFloat, int paramInt)
/*     */     throws InvalidMidiDataException
/*     */   {
/* 144 */     if (paramFloat == 0.0F)
/* 145 */       this.divisionType = 0.0F;
/* 146 */     else if (paramFloat == 24.0F)
/* 147 */       this.divisionType = 24.0F;
/* 148 */     else if (paramFloat == 25.0F)
/* 149 */       this.divisionType = 25.0F;
/* 150 */     else if (paramFloat == 29.969999F)
/* 151 */       this.divisionType = 29.969999F;
/* 152 */     else if (paramFloat == 30.0F)
/* 153 */       this.divisionType = 30.0F;
/* 154 */     else throw new InvalidMidiDataException("Unsupported division type: " + paramFloat);
/*     */ 
/* 156 */     this.resolution = paramInt;
/*     */   }
/*     */ 
/*     */   public Sequence(float paramFloat, int paramInt1, int paramInt2)
/*     */     throws InvalidMidiDataException
/*     */   {
/* 190 */     if (paramFloat == 0.0F)
/* 191 */       this.divisionType = 0.0F;
/* 192 */     else if (paramFloat == 24.0F)
/* 193 */       this.divisionType = 24.0F;
/* 194 */     else if (paramFloat == 25.0F)
/* 195 */       this.divisionType = 25.0F;
/* 196 */     else if (paramFloat == 29.969999F)
/* 197 */       this.divisionType = 29.969999F;
/* 198 */     else if (paramFloat == 30.0F)
/* 199 */       this.divisionType = 30.0F;
/* 200 */     else throw new InvalidMidiDataException("Unsupported division type: " + paramFloat);
/*     */ 
/* 202 */     this.resolution = paramInt1;
/*     */ 
/* 204 */     for (int i = 0; i < paramInt2; i++)
/* 205 */       this.tracks.addElement(new Track());
/*     */   }
/*     */ 
/*     */   public float getDivisionType()
/*     */   {
/* 223 */     return this.divisionType;
/*     */   }
/*     */ 
/*     */   public int getResolution()
/*     */   {
/* 238 */     return this.resolution;
/*     */   }
/*     */ 
/*     */   public Track createTrack()
/*     */   {
/* 252 */     Track localTrack = new Track();
/* 253 */     this.tracks.addElement(localTrack);
/*     */ 
/* 255 */     return localTrack;
/*     */   }
/*     */ 
/*     */   public boolean deleteTrack(Track paramTrack)
/*     */   {
/* 270 */     synchronized (this.tracks)
/*     */     {
/* 272 */       return this.tracks.removeElement(paramTrack);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Track[] getTracks()
/*     */   {
/* 287 */     return (Track[])this.tracks.toArray(new Track[this.tracks.size()]);
/*     */   }
/*     */ 
/*     */   public long getMicrosecondLength()
/*     */   {
/* 297 */     return MidiUtils.tick2microsecond(this, getTickLength(), null);
/*     */   }
/*     */ 
/*     */   public long getTickLength()
/*     */   {
/* 310 */     long l1 = 0L;
/*     */ 
/* 312 */     synchronized (this.tracks)
/*     */     {
/* 314 */       for (int i = 0; i < this.tracks.size(); i++) {
/* 315 */         long l2 = ((Track)this.tracks.elementAt(i)).ticks();
/* 316 */         if (l2 > l1) {
/* 317 */           l1 = l2;
/*     */         }
/*     */       }
/* 320 */       return l1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public Patch[] getPatchList()
/*     */   {
/* 338 */     return new Patch[0];
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.midi.Sequence
 * JD-Core Version:    0.6.2
 */