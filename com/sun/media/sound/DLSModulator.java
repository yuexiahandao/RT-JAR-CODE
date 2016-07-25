/*     */ package com.sun.media.sound;
/*     */ 
/*     */ public final class DLSModulator
/*     */ {
/*     */   public static final int CONN_DST_NONE = 0;
/*     */   public static final int CONN_DST_GAIN = 1;
/*     */   public static final int CONN_DST_PITCH = 3;
/*     */   public static final int CONN_DST_PAN = 4;
/*     */   public static final int CONN_DST_LFO_FREQUENCY = 260;
/*     */   public static final int CONN_DST_LFO_STARTDELAY = 261;
/*     */   public static final int CONN_DST_EG1_ATTACKTIME = 518;
/*     */   public static final int CONN_DST_EG1_DECAYTIME = 519;
/*     */   public static final int CONN_DST_EG1_RELEASETIME = 521;
/*     */   public static final int CONN_DST_EG1_SUSTAINLEVEL = 522;
/*     */   public static final int CONN_DST_EG2_ATTACKTIME = 778;
/*     */   public static final int CONN_DST_EG2_DECAYTIME = 779;
/*     */   public static final int CONN_DST_EG2_RELEASETIME = 781;
/*     */   public static final int CONN_DST_EG2_SUSTAINLEVEL = 782;
/*     */   public static final int CONN_DST_KEYNUMBER = 5;
/*     */   public static final int CONN_DST_LEFT = 16;
/*     */   public static final int CONN_DST_RIGHT = 17;
/*     */   public static final int CONN_DST_CENTER = 18;
/*     */   public static final int CONN_DST_LEFTREAR = 19;
/*     */   public static final int CONN_DST_RIGHTREAR = 20;
/*     */   public static final int CONN_DST_LFE_CHANNEL = 21;
/*     */   public static final int CONN_DST_CHORUS = 128;
/*     */   public static final int CONN_DST_REVERB = 129;
/*     */   public static final int CONN_DST_VIB_FREQUENCY = 276;
/*     */   public static final int CONN_DST_VIB_STARTDELAY = 277;
/*     */   public static final int CONN_DST_EG1_DELAYTIME = 523;
/*     */   public static final int CONN_DST_EG1_HOLDTIME = 524;
/*     */   public static final int CONN_DST_EG1_SHUTDOWNTIME = 525;
/*     */   public static final int CONN_DST_EG2_DELAYTIME = 783;
/*     */   public static final int CONN_DST_EG2_HOLDTIME = 784;
/*     */   public static final int CONN_DST_FILTER_CUTOFF = 1280;
/*     */   public static final int CONN_DST_FILTER_Q = 1281;
/*     */   public static final int CONN_SRC_NONE = 0;
/*     */   public static final int CONN_SRC_LFO = 1;
/*     */   public static final int CONN_SRC_KEYONVELOCITY = 2;
/*     */   public static final int CONN_SRC_KEYNUMBER = 3;
/*     */   public static final int CONN_SRC_EG1 = 4;
/*     */   public static final int CONN_SRC_EG2 = 5;
/*     */   public static final int CONN_SRC_PITCHWHEEL = 6;
/*     */   public static final int CONN_SRC_CC1 = 129;
/*     */   public static final int CONN_SRC_CC7 = 135;
/*     */   public static final int CONN_SRC_CC10 = 138;
/*     */   public static final int CONN_SRC_CC11 = 139;
/*     */   public static final int CONN_SRC_RPN0 = 256;
/*     */   public static final int CONN_SRC_RPN1 = 257;
/*     */   public static final int CONN_SRC_RPN2 = 258;
/*     */   public static final int CONN_SRC_POLYPRESSURE = 7;
/*     */   public static final int CONN_SRC_CHANNELPRESSURE = 8;
/*     */   public static final int CONN_SRC_VIBRATO = 9;
/*     */   public static final int CONN_SRC_MONOPRESSURE = 10;
/*     */   public static final int CONN_SRC_CC91 = 219;
/*     */   public static final int CONN_SRC_CC93 = 221;
/*     */   public static final int CONN_TRN_NONE = 0;
/*     */   public static final int CONN_TRN_CONCAVE = 1;
/*     */   public static final int CONN_TRN_CONVEX = 2;
/*     */   public static final int CONN_TRN_SWITCH = 3;
/*     */   public static final int DST_FORMAT_CB = 1;
/*     */   public static final int DST_FORMAT_CENT = 1;
/*     */   public static final int DST_FORMAT_TIMECENT = 2;
/*     */   public static final int DST_FORMAT_PERCENT = 3;
/*     */   int source;
/*     */   int control;
/*     */   int destination;
/*     */   int transform;
/*     */   int scale;
/* 110 */   int version = 1;
/*     */ 
/*     */   public int getControl() {
/* 113 */     return this.control;
/*     */   }
/*     */ 
/*     */   public void setControl(int paramInt) {
/* 117 */     this.control = paramInt;
/*     */   }
/*     */ 
/*     */   public static int getDestinationFormat(int paramInt)
/*     */   {
/* 122 */     if (paramInt == 1)
/* 123 */       return 1;
/* 124 */     if (paramInt == 3)
/* 125 */       return 1;
/* 126 */     if (paramInt == 4) {
/* 127 */       return 3;
/*     */     }
/* 129 */     if (paramInt == 260)
/* 130 */       return 1;
/* 131 */     if (paramInt == 261) {
/* 132 */       return 2;
/*     */     }
/* 134 */     if (paramInt == 518)
/* 135 */       return 2;
/* 136 */     if (paramInt == 519)
/* 137 */       return 2;
/* 138 */     if (paramInt == 521)
/* 139 */       return 2;
/* 140 */     if (paramInt == 522) {
/* 141 */       return 3;
/*     */     }
/* 143 */     if (paramInt == 778)
/* 144 */       return 2;
/* 145 */     if (paramInt == 779)
/* 146 */       return 2;
/* 147 */     if (paramInt == 781)
/* 148 */       return 2;
/* 149 */     if (paramInt == 782) {
/* 150 */       return 3;
/*     */     }
/* 152 */     if (paramInt == 5)
/* 153 */       return 1;
/* 154 */     if (paramInt == 16)
/* 155 */       return 1;
/* 156 */     if (paramInt == 17)
/* 157 */       return 1;
/* 158 */     if (paramInt == 18)
/* 159 */       return 1;
/* 160 */     if (paramInt == 19)
/* 161 */       return 1;
/* 162 */     if (paramInt == 20)
/* 163 */       return 1;
/* 164 */     if (paramInt == 21)
/* 165 */       return 1;
/* 166 */     if (paramInt == 128)
/* 167 */       return 3;
/* 168 */     if (paramInt == 129) {
/* 169 */       return 3;
/*     */     }
/* 171 */     if (paramInt == 276)
/* 172 */       return 1;
/* 173 */     if (paramInt == 277) {
/* 174 */       return 2;
/*     */     }
/* 176 */     if (paramInt == 523)
/* 177 */       return 2;
/* 178 */     if (paramInt == 524)
/* 179 */       return 2;
/* 180 */     if (paramInt == 525) {
/* 181 */       return 2;
/*     */     }
/* 183 */     if (paramInt == 783)
/* 184 */       return 2;
/* 185 */     if (paramInt == 784) {
/* 186 */       return 2;
/*     */     }
/* 188 */     if (paramInt == 1280)
/* 189 */       return 1;
/* 190 */     if (paramInt == 1281) {
/* 191 */       return 1;
/*     */     }
/* 193 */     return -1;
/*     */   }
/*     */ 
/*     */   public static String getDestinationName(int paramInt)
/*     */   {
/* 198 */     if (paramInt == 1)
/* 199 */       return "gain";
/* 200 */     if (paramInt == 3)
/* 201 */       return "pitch";
/* 202 */     if (paramInt == 4) {
/* 203 */       return "pan";
/*     */     }
/* 205 */     if (paramInt == 260)
/* 206 */       return "lfo1.freq";
/* 207 */     if (paramInt == 261) {
/* 208 */       return "lfo1.delay";
/*     */     }
/* 210 */     if (paramInt == 518)
/* 211 */       return "eg1.attack";
/* 212 */     if (paramInt == 519)
/* 213 */       return "eg1.decay";
/* 214 */     if (paramInt == 521)
/* 215 */       return "eg1.release";
/* 216 */     if (paramInt == 522) {
/* 217 */       return "eg1.sustain";
/*     */     }
/* 219 */     if (paramInt == 778)
/* 220 */       return "eg2.attack";
/* 221 */     if (paramInt == 779)
/* 222 */       return "eg2.decay";
/* 223 */     if (paramInt == 781)
/* 224 */       return "eg2.release";
/* 225 */     if (paramInt == 782) {
/* 226 */       return "eg2.sustain";
/*     */     }
/* 228 */     if (paramInt == 5)
/* 229 */       return "keynumber";
/* 230 */     if (paramInt == 16)
/* 231 */       return "left";
/* 232 */     if (paramInt == 17)
/* 233 */       return "right";
/* 234 */     if (paramInt == 18)
/* 235 */       return "center";
/* 236 */     if (paramInt == 19)
/* 237 */       return "leftrear";
/* 238 */     if (paramInt == 20)
/* 239 */       return "rightrear";
/* 240 */     if (paramInt == 21)
/* 241 */       return "lfe_channel";
/* 242 */     if (paramInt == 128)
/* 243 */       return "chorus";
/* 244 */     if (paramInt == 129) {
/* 245 */       return "reverb";
/*     */     }
/* 247 */     if (paramInt == 276)
/* 248 */       return "vib.freq";
/* 249 */     if (paramInt == 277) {
/* 250 */       return "vib.delay";
/*     */     }
/* 252 */     if (paramInt == 523)
/* 253 */       return "eg1.delay";
/* 254 */     if (paramInt == 524)
/* 255 */       return "eg1.hold";
/* 256 */     if (paramInt == 525) {
/* 257 */       return "eg1.shutdown";
/*     */     }
/* 259 */     if (paramInt == 783)
/* 260 */       return "eg2.delay";
/* 261 */     if (paramInt == 784) {
/* 262 */       return "eg.2hold";
/*     */     }
/* 264 */     if (paramInt == 1280)
/* 265 */       return "filter.cutoff";
/* 266 */     if (paramInt == 1281) {
/* 267 */       return "filter.q";
/*     */     }
/* 269 */     return null;
/*     */   }
/*     */ 
/*     */   public static String getSourceName(int paramInt)
/*     */   {
/* 274 */     if (paramInt == 0)
/* 275 */       return "none";
/* 276 */     if (paramInt == 1)
/* 277 */       return "lfo";
/* 278 */     if (paramInt == 2)
/* 279 */       return "keyonvelocity";
/* 280 */     if (paramInt == 3)
/* 281 */       return "keynumber";
/* 282 */     if (paramInt == 4)
/* 283 */       return "eg1";
/* 284 */     if (paramInt == 5)
/* 285 */       return "eg2";
/* 286 */     if (paramInt == 6)
/* 287 */       return "pitchweel";
/* 288 */     if (paramInt == 129)
/* 289 */       return "cc1";
/* 290 */     if (paramInt == 135)
/* 291 */       return "cc7";
/* 292 */     if (paramInt == 138)
/* 293 */       return "c10";
/* 294 */     if (paramInt == 139) {
/* 295 */       return "cc11";
/*     */     }
/* 297 */     if (paramInt == 7)
/* 298 */       return "polypressure";
/* 299 */     if (paramInt == 8)
/* 300 */       return "channelpressure";
/* 301 */     if (paramInt == 9)
/* 302 */       return "vibrato";
/* 303 */     if (paramInt == 10)
/* 304 */       return "monopressure";
/* 305 */     if (paramInt == 219)
/* 306 */       return "cc91";
/* 307 */     if (paramInt == 221)
/* 308 */       return "cc93";
/* 309 */     return null;
/*     */   }
/*     */ 
/*     */   public int getDestination() {
/* 313 */     return this.destination;
/*     */   }
/*     */ 
/*     */   public void setDestination(int paramInt) {
/* 317 */     this.destination = paramInt;
/*     */   }
/*     */ 
/*     */   public int getScale() {
/* 321 */     return this.scale;
/*     */   }
/*     */ 
/*     */   public void setScale(int paramInt) {
/* 325 */     this.scale = paramInt;
/*     */   }
/*     */ 
/*     */   public int getSource() {
/* 329 */     return this.source;
/*     */   }
/*     */ 
/*     */   public void setSource(int paramInt) {
/* 333 */     this.source = paramInt;
/*     */   }
/*     */ 
/*     */   public int getVersion() {
/* 337 */     return this.version;
/*     */   }
/*     */ 
/*     */   public void setVersion(int paramInt) {
/* 341 */     this.version = paramInt;
/*     */   }
/*     */ 
/*     */   public int getTransform() {
/* 345 */     return this.transform;
/*     */   }
/*     */ 
/*     */   public void setTransform(int paramInt) {
/* 349 */     this.transform = paramInt;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.DLSModulator
 * JD-Core Version:    0.6.2
 */