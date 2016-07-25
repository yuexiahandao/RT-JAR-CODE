/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.util.Arrays;
/*     */ import javax.sound.midi.MidiChannel;
/*     */ import javax.sound.midi.VoiceStatus;
/*     */ import javax.sound.sampled.AudioFormat;
/*     */ 
/*     */ public abstract class SoftAbstractResampler
/*     */   implements SoftResampler
/*     */ {
/*     */   public abstract int getPadding();
/*     */ 
/*     */   public abstract void interpolate(float[] paramArrayOfFloat1, float[] paramArrayOfFloat2, float paramFloat1, float[] paramArrayOfFloat3, float paramFloat2, float[] paramArrayOfFloat4, int[] paramArrayOfInt, int paramInt);
/*     */ 
/*     */   public final SoftResamplerStreamer openStreamer()
/*     */   {
/* 388 */     return new ModelAbstractResamplerStream();
/*     */   }
/*     */ 
/*     */   private class ModelAbstractResamplerStream
/*     */     implements SoftResamplerStreamer
/*     */   {
/*     */     AudioFloatInputStream stream;
/*  43 */     boolean stream_eof = false;
/*     */     int loopmode;
/*  45 */     boolean loopdirection = true;
/*     */     float loopstart;
/*     */     float looplen;
/*     */     float target_pitch;
/*  49 */     float[] current_pitch = new float[1];
/*     */     boolean started;
/*     */     boolean eof;
/*  52 */     int sector_pos = 0;
/*  53 */     int sector_size = 400;
/*  54 */     int sector_loopstart = -1;
/*  55 */     boolean markset = false;
/*  56 */     int marklimit = 0;
/*  57 */     int streampos = 0;
/*  58 */     int nrofchannels = 2;
/*  59 */     boolean noteOff_flag = false;
/*     */     float[][] ibuffer;
/*  61 */     boolean ibuffer_order = true;
/*     */     float[] sbuffer;
/*     */     int pad;
/*     */     int pad2;
/*  65 */     float[] ix = new float[1];
/*  66 */     int[] ox = new int[1];
/*  67 */     float samplerateconv = 1.0F;
/*  68 */     float pitchcorrection = 0.0F;
/*     */ 
/*     */     ModelAbstractResamplerStream() {
/*  71 */       this.pad = SoftAbstractResampler.this.getPadding();
/*  72 */       this.pad2 = (SoftAbstractResampler.this.getPadding() * 2);
/*  73 */       this.ibuffer = new float[2][this.sector_size + this.pad2];
/*  74 */       this.ibuffer_order = true;
/*     */     }
/*     */ 
/*     */     public void noteOn(MidiChannel paramMidiChannel, VoiceStatus paramVoiceStatus, int paramInt1, int paramInt2)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void noteOff(int paramInt) {
/*  82 */       this.noteOff_flag = true;
/*     */     }
/*     */ 
/*     */     public void open(ModelWavetable paramModelWavetable, float paramFloat)
/*     */       throws IOException
/*     */     {
/*  88 */       this.eof = false;
/*  89 */       this.nrofchannels = paramModelWavetable.getChannels();
/*  90 */       if (this.ibuffer.length < this.nrofchannels) {
/*  91 */         this.ibuffer = new float[this.nrofchannels][this.sector_size + this.pad2];
/*     */       }
/*     */ 
/*  94 */       this.stream = paramModelWavetable.openStream();
/*  95 */       this.streampos = 0;
/*  96 */       this.stream_eof = false;
/*  97 */       this.pitchcorrection = paramModelWavetable.getPitchcorrection();
/*  98 */       this.samplerateconv = (this.stream.getFormat().getSampleRate() / paramFloat);
/*     */ 
/* 100 */       this.looplen = paramModelWavetable.getLoopLength();
/* 101 */       this.loopstart = paramModelWavetable.getLoopStart();
/* 102 */       this.sector_loopstart = ((int)(this.loopstart / this.sector_size));
/* 103 */       this.sector_loopstart -= 1;
/*     */ 
/* 105 */       this.sector_pos = 0;
/*     */ 
/* 107 */       if (this.sector_loopstart < 0)
/* 108 */         this.sector_loopstart = 0;
/* 109 */       this.started = false;
/* 110 */       this.loopmode = paramModelWavetable.getLoopType();
/*     */ 
/* 112 */       if (this.loopmode != 0) {
/* 113 */         this.markset = false;
/* 114 */         this.marklimit = (this.nrofchannels * (int)(this.looplen + this.pad2 + 1.0F));
/*     */       } else {
/* 116 */         this.markset = true;
/*     */       }
/*     */ 
/* 119 */       this.target_pitch = this.samplerateconv;
/* 120 */       this.current_pitch[0] = this.samplerateconv;
/*     */ 
/* 122 */       this.ibuffer_order = true;
/* 123 */       this.loopdirection = true;
/* 124 */       this.noteOff_flag = false;
/*     */ 
/* 126 */       for (int i = 0; i < this.nrofchannels; i++)
/* 127 */         Arrays.fill(this.ibuffer[i], this.sector_size, this.sector_size + this.pad2, 0.0F);
/* 128 */       this.ix[0] = this.pad;
/* 129 */       this.eof = false;
/*     */ 
/* 131 */       this.ix[0] = (this.sector_size + this.pad);
/* 132 */       this.sector_pos = -1;
/* 133 */       this.streampos = (-this.sector_size);
/*     */ 
/* 135 */       nextBuffer();
/*     */     }
/*     */ 
/*     */     public void setPitch(float paramFloat)
/*     */     {
/* 144 */       this.target_pitch = ((float)Math.exp((this.pitchcorrection + paramFloat) * (Math.log(2.0D) / 1200.0D)) * this.samplerateconv);
/*     */ 
/* 148 */       if (!this.started)
/* 149 */         this.current_pitch[0] = this.target_pitch;
/*     */     }
/*     */ 
/*     */     public void nextBuffer() throws IOException {
/* 153 */       if ((this.ix[0] < this.pad) && 
/* 154 */         (this.markset))
/*     */       {
/* 156 */         this.stream.reset();
/* 157 */         this.ix[0] += this.streampos - this.sector_loopstart * this.sector_size;
/* 158 */         this.sector_pos = this.sector_loopstart;
/* 159 */         this.streampos = (this.sector_pos * this.sector_size);
/*     */ 
/* 162 */         this.ix[0] += this.sector_size;
/* 163 */         this.sector_pos -= 1;
/* 164 */         this.streampos -= this.sector_size;
/* 165 */         this.stream_eof = false;
/*     */       }
/*     */ 
/* 169 */       if ((this.ix[0] >= this.sector_size + this.pad) && 
/* 170 */         (this.stream_eof)) {
/* 171 */         this.eof = true;
/*     */         return;
/*     */       }
/*     */       int i;
/* 176 */       if (this.ix[0] >= this.sector_size * 4 + this.pad) {
/* 177 */         i = (int)((this.ix[0] - this.sector_size * 4 + this.pad) / this.sector_size);
/* 178 */         this.ix[0] -= this.sector_size * i;
/* 179 */         this.sector_pos += i;
/* 180 */         this.streampos += this.sector_size * i;
/* 181 */         this.stream.skip(this.sector_size * i);
/*     */       }
/*     */ 
/* 184 */       while (this.ix[0] >= this.sector_size + this.pad) {
/* 185 */         if ((!this.markset) && 
/* 186 */           (this.sector_pos + 1 == this.sector_loopstart)) {
/* 187 */           this.stream.mark(this.marklimit);
/* 188 */           this.markset = true;
/*     */         }
/*     */ 
/* 191 */         this.ix[0] -= this.sector_size;
/* 192 */         this.sector_pos += 1;
/* 193 */         this.streampos += this.sector_size;
/*     */         int k;
/* 195 */         for (i = 0; i < this.nrofchannels; i++) {
/* 196 */           float[] arrayOfFloat1 = this.ibuffer[i];
/* 197 */           for (k = 0; k < this.pad2; k++)
/* 198 */             arrayOfFloat1[k] = arrayOfFloat1[(k + this.sector_size)];
/*     */         }
/*     */         int j;
/* 202 */         if (this.nrofchannels == 1) {
/* 203 */           i = this.stream.read(this.ibuffer[0], this.pad2, this.sector_size);
/*     */         } else {
/* 205 */           j = this.sector_size * this.nrofchannels;
/* 206 */           if ((this.sbuffer == null) || (this.sbuffer.length < j))
/* 207 */             this.sbuffer = new float[j];
/* 208 */           k = this.stream.read(this.sbuffer, 0, j);
/* 209 */           if (k == -1) {
/* 210 */             i = -1;
/*     */           } else {
/* 212 */             i = k / this.nrofchannels;
/* 213 */             for (int m = 0; m < this.nrofchannels; m++) {
/* 214 */               float[] arrayOfFloat2 = this.ibuffer[m];
/* 215 */               int n = m;
/* 216 */               int i1 = this.nrofchannels;
/* 217 */               int i2 = this.pad2;
/* 218 */               for (int i3 = 0; i3 < i; i2++) {
/* 219 */                 arrayOfFloat2[i2] = this.sbuffer[n];
/*     */ 
/* 218 */                 i3++; n += i1;
/*     */               }
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/*     */ 
/* 225 */         if (i == -1) {
/* 226 */           i = 0;
/* 227 */           this.stream_eof = true;
/* 228 */           for (j = 0; j < this.nrofchannels; j++)
/* 229 */             Arrays.fill(this.ibuffer[j], this.pad2, this.pad2 + this.sector_size, 0.0F);
/* 230 */           return;
/*     */         }
/* 232 */         if (i != this.sector_size) {
/* 233 */           for (j = 0; j < this.nrofchannels; j++) {
/* 234 */             Arrays.fill(this.ibuffer[j], this.pad2 + i, this.pad2 + this.sector_size, 0.0F);
/*     */           }
/*     */         }
/* 237 */         this.ibuffer_order = true;
/*     */       }
/*     */     }
/*     */ 
/*     */     public void reverseBuffers()
/*     */     {
/* 244 */       this.ibuffer_order = (!this.ibuffer_order);
/* 245 */       for (int i = 0; i < this.nrofchannels; i++) {
/* 246 */         float[] arrayOfFloat = this.ibuffer[i];
/* 247 */         int j = arrayOfFloat.length - 1;
/* 248 */         int k = arrayOfFloat.length / 2;
/* 249 */         for (int m = 0; m < k; m++) {
/* 250 */           float f = arrayOfFloat[m];
/* 251 */           arrayOfFloat[m] = arrayOfFloat[(j - m)];
/* 252 */           arrayOfFloat[(j - m)] = f;
/*     */         }
/*     */       }
/*     */     }
/*     */ 
/*     */     public int read(float[][] paramArrayOfFloat, int paramInt1, int paramInt2)
/*     */       throws IOException
/*     */     {
/* 260 */       if (this.eof) {
/* 261 */         return -1;
/*     */       }
/* 263 */       if ((this.noteOff_flag) && 
/* 264 */         ((this.loopmode & 0x2) != 0) && 
/* 265 */         (this.loopdirection)) {
/* 266 */         this.loopmode = 0;
/*     */       }
/*     */ 
/* 269 */       float f1 = (this.target_pitch - this.current_pitch[0]) / paramInt2;
/* 270 */       float[] arrayOfFloat = this.current_pitch;
/* 271 */       this.started = true;
/*     */ 
/* 273 */       int[] arrayOfInt = this.ox;
/* 274 */       arrayOfInt[0] = paramInt1;
/* 275 */       int i = paramInt2 + paramInt1;
/*     */ 
/* 277 */       float f2 = this.sector_size + this.pad;
/* 278 */       if (!this.loopdirection)
/* 279 */         f2 = this.pad;
/* 280 */       while (arrayOfInt[0] != i) {
/* 281 */         nextBuffer();
/*     */         float f3;
/*     */         int j;
/*     */         float f4;
/*     */         int k;
/* 282 */         if (!this.loopdirection)
/*     */         {
/* 286 */           if (this.streampos < this.loopstart + this.pad) {
/* 287 */             f2 = this.loopstart - this.streampos + this.pad2;
/* 288 */             if (this.ix[0] <= f2) {
/* 289 */               if ((this.loopmode & 0x4) != 0)
/*     */               {
/* 291 */                 this.loopdirection = true;
/* 292 */                 f2 = this.sector_size + this.pad;
/* 293 */                 continue;
/*     */               }
/*     */ 
/* 296 */               this.ix[0] += this.looplen;
/* 297 */               f2 = this.pad;
/*     */             }
/*     */           }
/*     */           else
/*     */           {
/* 302 */             if (this.ibuffer_order != this.loopdirection) {
/* 303 */               reverseBuffers();
/*     */             }
/* 305 */             this.ix[0] = (this.sector_size + this.pad2 - this.ix[0]);
/* 306 */             f2 = this.sector_size + this.pad2 - f2;
/* 307 */             f2 += 1.0F;
/*     */ 
/* 309 */             f3 = this.ix[0];
/* 310 */             j = arrayOfInt[0];
/* 311 */             f4 = arrayOfFloat[0];
/* 312 */             for (k = 0; k < this.nrofchannels; k++) {
/* 313 */               if (paramArrayOfFloat[k] != null) {
/* 314 */                 this.ix[0] = f3;
/* 315 */                 arrayOfInt[0] = j;
/* 316 */                 arrayOfFloat[0] = f4;
/* 317 */                 SoftAbstractResampler.this.interpolate(this.ibuffer[k], this.ix, f2, arrayOfFloat, f1, paramArrayOfFloat[k], arrayOfInt, i);
/*     */               }
/*     */ 
/*     */             }
/*     */ 
/* 322 */             this.ix[0] = (this.sector_size + this.pad2 - this.ix[0]);
/* 323 */             f2 -= 1.0F;
/* 324 */             f2 = this.sector_size + this.pad2 - f2;
/*     */ 
/* 326 */             if (this.eof) {
/* 327 */               arrayOfFloat[0] = this.target_pitch;
/* 328 */               return arrayOfInt[0] - paramInt1;
/*     */             }
/*     */           }
/*     */ 
/*     */         }
/* 333 */         else if ((this.loopmode != 0) && 
/* 334 */           (this.streampos + this.sector_size > this.looplen + this.loopstart + this.pad)) {
/* 335 */           f2 = this.loopstart + this.looplen - this.streampos + this.pad2;
/* 336 */           if (this.ix[0] >= f2) {
/* 337 */             if (((this.loopmode & 0x4) != 0) || ((this.loopmode & 0x8) != 0))
/*     */             {
/* 339 */               this.loopdirection = false;
/* 340 */               f2 = this.pad;
/* 341 */               continue;
/*     */             }
/* 343 */             f2 = this.sector_size + this.pad;
/* 344 */             this.ix[0] -= this.looplen;
/*     */           }
/*     */ 
/*     */         }
/*     */         else
/*     */         {
/* 350 */           if (this.ibuffer_order != this.loopdirection) {
/* 351 */             reverseBuffers();
/*     */           }
/* 353 */           f3 = this.ix[0];
/* 354 */           j = arrayOfInt[0];
/* 355 */           f4 = arrayOfFloat[0];
/* 356 */           for (k = 0; k < this.nrofchannels; k++) {
/* 357 */             if (paramArrayOfFloat[k] != null) {
/* 358 */               this.ix[0] = f3;
/* 359 */               arrayOfInt[0] = j;
/* 360 */               arrayOfFloat[0] = f4;
/* 361 */               SoftAbstractResampler.this.interpolate(this.ibuffer[k], this.ix, f2, arrayOfFloat, f1, paramArrayOfFloat[k], arrayOfInt, i);
/*     */             }
/*     */ 
/*     */           }
/*     */ 
/* 366 */           if (this.eof) {
/* 367 */             arrayOfFloat[0] = this.target_pitch;
/* 368 */             return arrayOfInt[0] - paramInt1;
/*     */           }
/*     */         }
/*     */       }
/* 372 */       arrayOfFloat[0] = this.target_pitch;
/* 373 */       return paramInt2;
/*     */     }
/*     */ 
/*     */     public void close() throws IOException {
/* 377 */       this.stream.close();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.SoftAbstractResampler
 * JD-Core Version:    0.6.2
 */