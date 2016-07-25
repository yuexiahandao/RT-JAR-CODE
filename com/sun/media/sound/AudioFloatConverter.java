/*      */ package com.sun.media.sound;
/*      */ 
/*      */ import java.nio.ByteBuffer;
/*      */ import java.nio.ByteOrder;
/*      */ import java.nio.DoubleBuffer;
/*      */ import java.nio.FloatBuffer;
/*      */ import javax.sound.sampled.AudioFormat;
/*      */ import javax.sound.sampled.AudioFormat.Encoding;
/*      */ 
/*      */ public abstract class AudioFloatConverter
/*      */ {
/*      */   private AudioFormat format;
/*      */ 
/*      */   public static AudioFloatConverter getConverter(AudioFormat paramAudioFormat)
/*      */   {
/*  907 */     Object localObject = null;
/*  908 */     if (paramAudioFormat.getFrameSize() == 0)
/*  909 */       return null;
/*  910 */     if (paramAudioFormat.getFrameSize() != (paramAudioFormat.getSampleSizeInBits() + 7) / 8 * paramAudioFormat.getChannels())
/*      */     {
/*  912 */       return null;
/*      */     }
/*  914 */     if (paramAudioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) {
/*  915 */       if (paramAudioFormat.isBigEndian()) {
/*  916 */         if (paramAudioFormat.getSampleSizeInBits() <= 8)
/*  917 */           localObject = new AudioFloatConversion8S(null);
/*  918 */         else if ((paramAudioFormat.getSampleSizeInBits() > 8) && (paramAudioFormat.getSampleSizeInBits() <= 16))
/*      */         {
/*  920 */           localObject = new AudioFloatConversion16SB(null);
/*  921 */         } else if ((paramAudioFormat.getSampleSizeInBits() > 16) && (paramAudioFormat.getSampleSizeInBits() <= 24))
/*      */         {
/*  923 */           localObject = new AudioFloatConversion24SB(null);
/*  924 */         } else if ((paramAudioFormat.getSampleSizeInBits() > 24) && (paramAudioFormat.getSampleSizeInBits() <= 32))
/*      */         {
/*  926 */           localObject = new AudioFloatConversion32SB(null);
/*  927 */         } else if (paramAudioFormat.getSampleSizeInBits() > 32) {
/*  928 */           localObject = new AudioFloatConversion32xSB((paramAudioFormat.getSampleSizeInBits() + 7) / 8 - 4);
/*      */         }
/*      */ 
/*      */       }
/*  932 */       else if (paramAudioFormat.getSampleSizeInBits() <= 8)
/*  933 */         localObject = new AudioFloatConversion8S(null);
/*  934 */       else if ((paramAudioFormat.getSampleSizeInBits() > 8) && (paramAudioFormat.getSampleSizeInBits() <= 16))
/*      */       {
/*  936 */         localObject = new AudioFloatConversion16SL(null);
/*  937 */       } else if ((paramAudioFormat.getSampleSizeInBits() > 16) && (paramAudioFormat.getSampleSizeInBits() <= 24))
/*      */       {
/*  939 */         localObject = new AudioFloatConversion24SL(null);
/*  940 */       } else if ((paramAudioFormat.getSampleSizeInBits() > 24) && (paramAudioFormat.getSampleSizeInBits() <= 32))
/*      */       {
/*  942 */         localObject = new AudioFloatConversion32SL(null);
/*  943 */       } else if (paramAudioFormat.getSampleSizeInBits() > 32) {
/*  944 */         localObject = new AudioFloatConversion32xSL((paramAudioFormat.getSampleSizeInBits() + 7) / 8 - 4);
/*      */       }
/*      */ 
/*      */     }
/*  948 */     else if (paramAudioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED)) {
/*  949 */       if (paramAudioFormat.isBigEndian()) {
/*  950 */         if (paramAudioFormat.getSampleSizeInBits() <= 8)
/*  951 */           localObject = new AudioFloatConversion8U(null);
/*  952 */         else if ((paramAudioFormat.getSampleSizeInBits() > 8) && (paramAudioFormat.getSampleSizeInBits() <= 16))
/*      */         {
/*  954 */           localObject = new AudioFloatConversion16UB(null);
/*  955 */         } else if ((paramAudioFormat.getSampleSizeInBits() > 16) && (paramAudioFormat.getSampleSizeInBits() <= 24))
/*      */         {
/*  957 */           localObject = new AudioFloatConversion24UB(null);
/*  958 */         } else if ((paramAudioFormat.getSampleSizeInBits() > 24) && (paramAudioFormat.getSampleSizeInBits() <= 32))
/*      */         {
/*  960 */           localObject = new AudioFloatConversion32UB(null);
/*  961 */         } else if (paramAudioFormat.getSampleSizeInBits() > 32) {
/*  962 */           localObject = new AudioFloatConversion32xUB((paramAudioFormat.getSampleSizeInBits() + 7) / 8 - 4);
/*      */         }
/*      */ 
/*      */       }
/*  966 */       else if (paramAudioFormat.getSampleSizeInBits() <= 8)
/*  967 */         localObject = new AudioFloatConversion8U(null);
/*  968 */       else if ((paramAudioFormat.getSampleSizeInBits() > 8) && (paramAudioFormat.getSampleSizeInBits() <= 16))
/*      */       {
/*  970 */         localObject = new AudioFloatConversion16UL(null);
/*  971 */       } else if ((paramAudioFormat.getSampleSizeInBits() > 16) && (paramAudioFormat.getSampleSizeInBits() <= 24))
/*      */       {
/*  973 */         localObject = new AudioFloatConversion24UL(null);
/*  974 */       } else if ((paramAudioFormat.getSampleSizeInBits() > 24) && (paramAudioFormat.getSampleSizeInBits() <= 32))
/*      */       {
/*  976 */         localObject = new AudioFloatConversion32UL(null);
/*  977 */       } else if (paramAudioFormat.getSampleSizeInBits() > 32) {
/*  978 */         localObject = new AudioFloatConversion32xUL((paramAudioFormat.getSampleSizeInBits() + 7) / 8 - 4);
/*      */       }
/*      */ 
/*      */     }
/*  982 */     else if (paramAudioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_FLOAT)) {
/*  983 */       if (paramAudioFormat.getSampleSizeInBits() == 32) {
/*  984 */         if (paramAudioFormat.isBigEndian())
/*  985 */           localObject = new AudioFloatConversion32B(null);
/*      */         else
/*  987 */           localObject = new AudioFloatConversion32L(null);
/*  988 */       } else if (paramAudioFormat.getSampleSizeInBits() == 64) {
/*  989 */         if (paramAudioFormat.isBigEndian())
/*  990 */           localObject = new AudioFloatConversion64B(null);
/*      */         else {
/*  992 */           localObject = new AudioFloatConversion64L(null);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  997 */     if (((paramAudioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_SIGNED)) || (paramAudioFormat.getEncoding().equals(AudioFormat.Encoding.PCM_UNSIGNED))) && (paramAudioFormat.getSampleSizeInBits() % 8 != 0))
/*      */     {
/* 1000 */       localObject = new AudioFloatLSBFilter((AudioFloatConverter)localObject, paramAudioFormat);
/*      */     }
/*      */ 
/* 1003 */     if (localObject != null)
/* 1004 */       ((AudioFloatConverter)localObject).format = paramAudioFormat;
/* 1005 */     return localObject;
/*      */   }
/*      */ 
/*      */   public final AudioFormat getFormat()
/*      */   {
/* 1011 */     return this.format;
/*      */   }
/*      */ 
/*      */   public abstract float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3);
/*      */ 
/*      */   public final float[] toFloatArray(byte[] paramArrayOfByte, float[] paramArrayOfFloat, int paramInt1, int paramInt2)
/*      */   {
/* 1019 */     return toFloatArray(paramArrayOfByte, 0, paramArrayOfFloat, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public final float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2)
/*      */   {
/* 1024 */     return toFloatArray(paramArrayOfByte, paramInt1, paramArrayOfFloat, 0, paramInt2);
/*      */   }
/*      */ 
/*      */   public final float[] toFloatArray(byte[] paramArrayOfByte, float[] paramArrayOfFloat, int paramInt)
/*      */   {
/* 1029 */     return toFloatArray(paramArrayOfByte, 0, paramArrayOfFloat, 0, paramInt);
/*      */   }
/*      */ 
/*      */   public final float[] toFloatArray(byte[] paramArrayOfByte, float[] paramArrayOfFloat) {
/* 1033 */     return toFloatArray(paramArrayOfByte, 0, paramArrayOfFloat, 0, paramArrayOfFloat.length);
/*      */   }
/*      */ 
/*      */   public abstract byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3);
/*      */ 
/*      */   public final byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*      */   {
/* 1041 */     return toByteArray(paramArrayOfFloat, 0, paramInt1, paramArrayOfByte, paramInt2);
/*      */   }
/*      */ 
/*      */   public final byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte)
/*      */   {
/* 1046 */     return toByteArray(paramArrayOfFloat, paramInt1, paramInt2, paramArrayOfByte, 0);
/*      */   }
/*      */ 
/*      */   public final byte[] toByteArray(float[] paramArrayOfFloat, int paramInt, byte[] paramArrayOfByte)
/*      */   {
/* 1051 */     return toByteArray(paramArrayOfFloat, 0, paramInt, paramArrayOfByte, 0);
/*      */   }
/*      */ 
/*      */   public final byte[] toByteArray(float[] paramArrayOfFloat, byte[] paramArrayOfByte) {
/* 1055 */     return toByteArray(paramArrayOfFloat, 0, paramArrayOfFloat.length, paramArrayOfByte, 0);
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion16SB extends AudioFloatConverter
/*      */   {
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  396 */       int i = paramInt1;
/*  397 */       int j = paramInt2;
/*  398 */       for (int k = 0; k < paramInt3; k++) {
/*  399 */         paramArrayOfFloat[(j++)] = ((short)(paramArrayOfByte[(i++)] << 8 | paramArrayOfByte[(i++)] & 0xFF) * 3.051851E-005F);
/*      */       }
/*      */ 
/*  402 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  407 */       int i = paramInt1;
/*  408 */       int j = paramInt3;
/*  409 */       for (int k = 0; k < paramInt2; k++) {
/*  410 */         int m = (int)(paramArrayOfFloat[(i++)] * 32767.0D);
/*  411 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 8));
/*  412 */         paramArrayOfByte[(j++)] = ((byte)m);
/*      */       }
/*  414 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion16SL extends AudioFloatConverter
/*      */   {
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  369 */       int i = paramInt1;
/*  370 */       int j = paramInt2 + paramInt3;
/*  371 */       for (int k = paramInt2; k < j; k++) {
/*  372 */         paramArrayOfFloat[k] = ((short)(paramArrayOfByte[(i++)] & 0xFF | paramArrayOfByte[(i++)] << 8) * 3.051851E-005F);
/*      */       }
/*      */ 
/*  376 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  381 */       int i = paramInt3;
/*  382 */       int j = paramInt1 + paramInt2;
/*  383 */       for (int k = paramInt1; k < j; k++) {
/*  384 */         int m = (int)(paramArrayOfFloat[k] * 32767.0D);
/*  385 */         paramArrayOfByte[(i++)] = ((byte)m);
/*  386 */         paramArrayOfByte[(i++)] = ((byte)(m >>> 8));
/*      */       }
/*  388 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion16UB extends AudioFloatConverter
/*      */   {
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  448 */       int i = paramInt1;
/*  449 */       int j = paramInt2;
/*  450 */       for (int k = 0; k < paramInt3; k++) {
/*  451 */         int m = (paramArrayOfByte[(i++)] & 0xFF) << 8 | paramArrayOfByte[(i++)] & 0xFF;
/*  452 */         paramArrayOfFloat[(j++)] = ((m - 32767) * 3.051851E-005F);
/*      */       }
/*  454 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  459 */       int i = paramInt1;
/*  460 */       int j = paramInt3;
/*  461 */       for (int k = 0; k < paramInt2; k++) {
/*  462 */         int m = 32767 + (int)(paramArrayOfFloat[(i++)] * 32767.0D);
/*  463 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 8));
/*  464 */         paramArrayOfByte[(j++)] = ((byte)m);
/*      */       }
/*  466 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion16UL extends AudioFloatConverter
/*      */   {
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  422 */       int i = paramInt1;
/*  423 */       int j = paramInt2;
/*  424 */       for (int k = 0; k < paramInt3; k++) {
/*  425 */         int m = paramArrayOfByte[(i++)] & 0xFF | (paramArrayOfByte[(i++)] & 0xFF) << 8;
/*  426 */         paramArrayOfFloat[(j++)] = ((m - 32767) * 3.051851E-005F);
/*      */       }
/*  428 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  433 */       int i = paramInt1;
/*  434 */       int j = paramInt3;
/*  435 */       for (int k = 0; k < paramInt2; k++) {
/*  436 */         int m = 32767 + (int)(paramArrayOfFloat[(i++)] * 32767.0D);
/*  437 */         paramArrayOfByte[(j++)] = ((byte)m);
/*  438 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 8));
/*      */       }
/*  440 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion24SB extends AudioFloatConverter
/*      */   {
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  512 */       int i = paramInt1;
/*  513 */       int j = paramInt2;
/*  514 */       for (int k = 0; k < paramInt3; k++) {
/*  515 */         int m = (paramArrayOfByte[(i++)] & 0xFF) << 16 | (paramArrayOfByte[(i++)] & 0xFF) << 8 | paramArrayOfByte[(i++)] & 0xFF;
/*      */ 
/*  517 */         if (m > 8388607)
/*  518 */           m -= 16777216;
/*  519 */         paramArrayOfFloat[(j++)] = (m * 1.192093E-007F);
/*      */       }
/*  521 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  526 */       int i = paramInt1;
/*  527 */       int j = paramInt3;
/*  528 */       for (int k = 0; k < paramInt2; k++) {
/*  529 */         int m = (int)(paramArrayOfFloat[(i++)] * 8388607.0F);
/*  530 */         if (m < 0)
/*  531 */           m += 16777216;
/*  532 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 16));
/*  533 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 8));
/*  534 */         paramArrayOfByte[(j++)] = ((byte)m);
/*      */       }
/*  536 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion24SL extends AudioFloatConverter
/*      */   {
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  480 */       int i = paramInt1;
/*  481 */       int j = paramInt2;
/*  482 */       for (int k = 0; k < paramInt3; k++) {
/*  483 */         int m = paramArrayOfByte[(i++)] & 0xFF | (paramArrayOfByte[(i++)] & 0xFF) << 8 | (paramArrayOfByte[(i++)] & 0xFF) << 16;
/*      */ 
/*  485 */         if (m > 8388607)
/*  486 */           m -= 16777216;
/*  487 */         paramArrayOfFloat[(j++)] = (m * 1.192093E-007F);
/*      */       }
/*  489 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  494 */       int i = paramInt1;
/*  495 */       int j = paramInt3;
/*  496 */       for (int k = 0; k < paramInt2; k++) {
/*  497 */         int m = (int)(paramArrayOfFloat[(i++)] * 8388607.0F);
/*  498 */         if (m < 0)
/*  499 */           m += 16777216;
/*  500 */         paramArrayOfByte[(j++)] = ((byte)m);
/*  501 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 8));
/*  502 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 16));
/*      */       }
/*  504 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion24UB extends AudioFloatConverter
/*      */   {
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  574 */       int i = paramInt1;
/*  575 */       int j = paramInt2;
/*  576 */       for (int k = 0; k < paramInt3; k++) {
/*  577 */         int m = (paramArrayOfByte[(i++)] & 0xFF) << 16 | (paramArrayOfByte[(i++)] & 0xFF) << 8 | paramArrayOfByte[(i++)] & 0xFF;
/*      */ 
/*  579 */         m -= 8388607;
/*  580 */         paramArrayOfFloat[(j++)] = (m * 1.192093E-007F);
/*      */       }
/*  582 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  587 */       int i = paramInt1;
/*  588 */       int j = paramInt3;
/*  589 */       for (int k = 0; k < paramInt2; k++) {
/*  590 */         int m = (int)(paramArrayOfFloat[(i++)] * 8388607.0F);
/*  591 */         m += 8388607;
/*  592 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 16));
/*  593 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 8));
/*  594 */         paramArrayOfByte[(j++)] = ((byte)m);
/*      */       }
/*  596 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion24UL extends AudioFloatConverter
/*      */   {
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  544 */       int i = paramInt1;
/*  545 */       int j = paramInt2;
/*  546 */       for (int k = 0; k < paramInt3; k++) {
/*  547 */         int m = paramArrayOfByte[(i++)] & 0xFF | (paramArrayOfByte[(i++)] & 0xFF) << 8 | (paramArrayOfByte[(i++)] & 0xFF) << 16;
/*      */ 
/*  549 */         m -= 8388607;
/*  550 */         paramArrayOfFloat[(j++)] = (m * 1.192093E-007F);
/*      */       }
/*  552 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  557 */       int i = paramInt1;
/*  558 */       int j = paramInt3;
/*  559 */       for (int k = 0; k < paramInt2; k++) {
/*  560 */         int m = (int)(paramArrayOfFloat[(i++)] * 8388607.0F);
/*  561 */         m += 8388607;
/*  562 */         paramArrayOfByte[(j++)] = ((byte)m);
/*  563 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 8));
/*  564 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 16));
/*      */       }
/*  566 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion32B extends AudioFloatConverter
/*      */   {
/*  275 */     ByteBuffer bytebuffer = null;
/*      */ 
/*  277 */     FloatBuffer floatbuffer = null;
/*      */ 
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  281 */       int i = paramInt3 * 4;
/*  282 */       if ((this.bytebuffer == null) || (this.bytebuffer.capacity() < i)) {
/*  283 */         this.bytebuffer = ByteBuffer.allocate(i).order(ByteOrder.BIG_ENDIAN);
/*      */ 
/*  285 */         this.floatbuffer = this.bytebuffer.asFloatBuffer();
/*      */       }
/*  287 */       this.bytebuffer.position(0);
/*  288 */       this.floatbuffer.position(0);
/*  289 */       this.bytebuffer.put(paramArrayOfByte, paramInt1, i);
/*  290 */       this.floatbuffer.get(paramArrayOfFloat, paramInt2, paramInt3);
/*  291 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  296 */       int i = paramInt2 * 4;
/*  297 */       if ((this.bytebuffer == null) || (this.bytebuffer.capacity() < i)) {
/*  298 */         this.bytebuffer = ByteBuffer.allocate(i).order(ByteOrder.BIG_ENDIAN);
/*      */ 
/*  300 */         this.floatbuffer = this.bytebuffer.asFloatBuffer();
/*      */       }
/*  302 */       this.floatbuffer.position(0);
/*  303 */       this.bytebuffer.position(0);
/*  304 */       this.floatbuffer.put(paramArrayOfFloat, paramInt1, paramInt2);
/*  305 */       this.bytebuffer.get(paramArrayOfByte, paramInt3, i);
/*  306 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion32L extends AudioFloatConverter
/*      */   {
/*  238 */     ByteBuffer bytebuffer = null;
/*      */ 
/*  240 */     FloatBuffer floatbuffer = null;
/*      */ 
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  244 */       int i = paramInt3 * 4;
/*  245 */       if ((this.bytebuffer == null) || (this.bytebuffer.capacity() < i)) {
/*  246 */         this.bytebuffer = ByteBuffer.allocate(i).order(ByteOrder.LITTLE_ENDIAN);
/*      */ 
/*  248 */         this.floatbuffer = this.bytebuffer.asFloatBuffer();
/*      */       }
/*  250 */       this.bytebuffer.position(0);
/*  251 */       this.floatbuffer.position(0);
/*  252 */       this.bytebuffer.put(paramArrayOfByte, paramInt1, i);
/*  253 */       this.floatbuffer.get(paramArrayOfFloat, paramInt2, paramInt3);
/*  254 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  259 */       int i = paramInt2 * 4;
/*  260 */       if ((this.bytebuffer == null) || (this.bytebuffer.capacity() < i)) {
/*  261 */         this.bytebuffer = ByteBuffer.allocate(i).order(ByteOrder.LITTLE_ENDIAN);
/*      */ 
/*  263 */         this.floatbuffer = this.bytebuffer.asFloatBuffer();
/*      */       }
/*  265 */       this.floatbuffer.position(0);
/*  266 */       this.bytebuffer.position(0);
/*  267 */       this.floatbuffer.put(paramArrayOfFloat, paramInt1, paramInt2);
/*  268 */       this.bytebuffer.get(paramArrayOfByte, paramInt3, i);
/*  269 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion32SB extends AudioFloatConverter
/*      */   {
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  640 */       int i = paramInt1;
/*  641 */       int j = paramInt2;
/*  642 */       for (int k = 0; k < paramInt3; k++) {
/*  643 */         int m = (paramArrayOfByte[(i++)] & 0xFF) << 24 | (paramArrayOfByte[(i++)] & 0xFF) << 16 | (paramArrayOfByte[(i++)] & 0xFF) << 8 | paramArrayOfByte[(i++)] & 0xFF;
/*      */ 
/*  646 */         paramArrayOfFloat[(j++)] = (m * 4.656613E-010F);
/*      */       }
/*  648 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  653 */       int i = paramInt1;
/*  654 */       int j = paramInt3;
/*  655 */       for (int k = 0; k < paramInt2; k++) {
/*  656 */         int m = (int)(paramArrayOfFloat[(i++)] * 2.147484E+009F);
/*  657 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 24));
/*  658 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 16));
/*  659 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 8));
/*  660 */         paramArrayOfByte[(j++)] = ((byte)m);
/*      */       }
/*  662 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion32SL extends AudioFloatConverter
/*      */   {
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  610 */       int i = paramInt1;
/*  611 */       int j = paramInt2;
/*  612 */       for (int k = 0; k < paramInt3; k++) {
/*  613 */         int m = paramArrayOfByte[(i++)] & 0xFF | (paramArrayOfByte[(i++)] & 0xFF) << 8 | (paramArrayOfByte[(i++)] & 0xFF) << 16 | (paramArrayOfByte[(i++)] & 0xFF) << 24;
/*      */ 
/*  616 */         paramArrayOfFloat[(j++)] = (m * 4.656613E-010F);
/*      */       }
/*  618 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  623 */       int i = paramInt1;
/*  624 */       int j = paramInt3;
/*  625 */       for (int k = 0; k < paramInt2; k++) {
/*  626 */         int m = (int)(paramArrayOfFloat[(i++)] * 2.147484E+009F);
/*  627 */         paramArrayOfByte[(j++)] = ((byte)m);
/*  628 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 8));
/*  629 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 16));
/*  630 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 24));
/*      */       }
/*  632 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion32UB extends AudioFloatConverter
/*      */   {
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  703 */       int i = paramInt1;
/*  704 */       int j = paramInt2;
/*  705 */       for (int k = 0; k < paramInt3; k++) {
/*  706 */         int m = (paramArrayOfByte[(i++)] & 0xFF) << 24 | (paramArrayOfByte[(i++)] & 0xFF) << 16 | (paramArrayOfByte[(i++)] & 0xFF) << 8 | paramArrayOfByte[(i++)] & 0xFF;
/*      */ 
/*  709 */         m -= 2147483647;
/*  710 */         paramArrayOfFloat[(j++)] = (m * 4.656613E-010F);
/*      */       }
/*  712 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  717 */       int i = paramInt1;
/*  718 */       int j = paramInt3;
/*  719 */       for (int k = 0; k < paramInt2; k++) {
/*  720 */         int m = (int)(paramArrayOfFloat[(i++)] * 2.147484E+009F);
/*  721 */         m += 2147483647;
/*  722 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 24));
/*  723 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 16));
/*  724 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 8));
/*  725 */         paramArrayOfByte[(j++)] = ((byte)m);
/*      */       }
/*  727 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion32UL extends AudioFloatConverter
/*      */   {
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  670 */       int i = paramInt1;
/*  671 */       int j = paramInt2;
/*  672 */       for (int k = 0; k < paramInt3; k++) {
/*  673 */         int m = paramArrayOfByte[(i++)] & 0xFF | (paramArrayOfByte[(i++)] & 0xFF) << 8 | (paramArrayOfByte[(i++)] & 0xFF) << 16 | (paramArrayOfByte[(i++)] & 0xFF) << 24;
/*      */ 
/*  676 */         m -= 2147483647;
/*  677 */         paramArrayOfFloat[(j++)] = (m * 4.656613E-010F);
/*      */       }
/*  679 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  684 */       int i = paramInt1;
/*  685 */       int j = paramInt3;
/*  686 */       for (int k = 0; k < paramInt2; k++) {
/*  687 */         int m = (int)(paramArrayOfFloat[(i++)] * 2.147484E+009F);
/*  688 */         m += 2147483647;
/*  689 */         paramArrayOfByte[(j++)] = ((byte)m);
/*  690 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 8));
/*  691 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 16));
/*  692 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 24));
/*      */       }
/*  694 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion32xSB extends AudioFloatConverter
/*      */   {
/*      */     final int xbytes;
/*      */ 
/*      */     AudioFloatConversion32xSB(int paramInt)
/*      */     {
/*  784 */       this.xbytes = paramInt;
/*      */     }
/*      */ 
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  789 */       int i = paramInt1;
/*  790 */       int j = paramInt2;
/*  791 */       for (int k = 0; k < paramInt3; k++) {
/*  792 */         int m = (paramArrayOfByte[(i++)] & 0xFF) << 24 | (paramArrayOfByte[(i++)] & 0xFF) << 16 | (paramArrayOfByte[(i++)] & 0xFF) << 8 | paramArrayOfByte[(i++)] & 0xFF;
/*      */ 
/*  796 */         i += this.xbytes;
/*  797 */         paramArrayOfFloat[(j++)] = (m * 4.656613E-010F);
/*      */       }
/*  799 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  804 */       int i = paramInt1;
/*  805 */       int j = paramInt3;
/*  806 */       for (int k = 0; k < paramInt2; k++) {
/*  807 */         int m = (int)(paramArrayOfFloat[(i++)] * 2.147484E+009F);
/*  808 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 24));
/*  809 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 16));
/*  810 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 8));
/*  811 */         paramArrayOfByte[(j++)] = ((byte)m);
/*  812 */         for (int n = 0; n < this.xbytes; n++) {
/*  813 */           paramArrayOfByte[(j++)] = 0;
/*      */         }
/*      */       }
/*  816 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion32xSL extends AudioFloatConverter
/*      */   {
/*      */     final int xbytes;
/*      */ 
/*      */     AudioFloatConversion32xSL(int paramInt)
/*      */     {
/*  743 */       this.xbytes = paramInt;
/*      */     }
/*      */ 
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  748 */       int i = paramInt1;
/*  749 */       int j = paramInt2;
/*  750 */       for (int k = 0; k < paramInt3; k++) {
/*  751 */         i += this.xbytes;
/*  752 */         int m = paramArrayOfByte[(i++)] & 0xFF | (paramArrayOfByte[(i++)] & 0xFF) << 8 | (paramArrayOfByte[(i++)] & 0xFF) << 16 | (paramArrayOfByte[(i++)] & 0xFF) << 24;
/*      */ 
/*  755 */         paramArrayOfFloat[(j++)] = (m * 4.656613E-010F);
/*      */       }
/*  757 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  762 */       int i = paramInt1;
/*  763 */       int j = paramInt3;
/*  764 */       for (int k = 0; k < paramInt2; k++) {
/*  765 */         int m = (int)(paramArrayOfFloat[(i++)] * 2.147484E+009F);
/*  766 */         for (int n = 0; n < this.xbytes; n++) {
/*  767 */           paramArrayOfByte[(j++)] = 0;
/*      */         }
/*  769 */         paramArrayOfByte[(j++)] = ((byte)m);
/*  770 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 8));
/*  771 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 16));
/*  772 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 24));
/*      */       }
/*  774 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion32xUB extends AudioFloatConverter
/*      */   {
/*      */     final int xbytes;
/*      */ 
/*      */     AudioFloatConversion32xUB(int paramInt)
/*      */     {
/*  869 */       this.xbytes = paramInt;
/*      */     }
/*      */ 
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  874 */       int i = paramInt1;
/*  875 */       int j = paramInt2;
/*  876 */       for (int k = 0; k < paramInt3; k++) {
/*  877 */         int m = (paramArrayOfByte[(i++)] & 0xFF) << 24 | (paramArrayOfByte[(i++)] & 0xFF) << 16 | (paramArrayOfByte[(i++)] & 0xFF) << 8 | paramArrayOfByte[(i++)] & 0xFF;
/*      */ 
/*  880 */         i += this.xbytes;
/*  881 */         m -= 2147483647;
/*  882 */         paramArrayOfFloat[(j++)] = (m * 4.656613E-010F);
/*      */       }
/*  884 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  889 */       int i = paramInt1;
/*  890 */       int j = paramInt3;
/*  891 */       for (int k = 0; k < paramInt2; k++) {
/*  892 */         int m = (int)(paramArrayOfFloat[(i++)] * 2147483647.0D);
/*  893 */         m += 2147483647;
/*  894 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 24));
/*  895 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 16));
/*  896 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 8));
/*  897 */         paramArrayOfByte[(j++)] = ((byte)m);
/*  898 */         for (int n = 0; n < this.xbytes; n++) {
/*  899 */           paramArrayOfByte[(j++)] = 0;
/*      */         }
/*      */       }
/*  902 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion32xUL extends AudioFloatConverter
/*      */   {
/*      */     final int xbytes;
/*      */ 
/*      */     AudioFloatConversion32xUL(int paramInt)
/*      */     {
/*  826 */       this.xbytes = paramInt;
/*      */     }
/*      */ 
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  831 */       int i = paramInt1;
/*  832 */       int j = paramInt2;
/*  833 */       for (int k = 0; k < paramInt3; k++) {
/*  834 */         i += this.xbytes;
/*  835 */         int m = paramArrayOfByte[(i++)] & 0xFF | (paramArrayOfByte[(i++)] & 0xFF) << 8 | (paramArrayOfByte[(i++)] & 0xFF) << 16 | (paramArrayOfByte[(i++)] & 0xFF) << 24;
/*      */ 
/*  838 */         m -= 2147483647;
/*  839 */         paramArrayOfFloat[(j++)] = (m * 4.656613E-010F);
/*      */       }
/*  841 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  846 */       int i = paramInt1;
/*  847 */       int j = paramInt3;
/*  848 */       for (int k = 0; k < paramInt2; k++) {
/*  849 */         int m = (int)(paramArrayOfFloat[(i++)] * 2.147484E+009F);
/*  850 */         m += 2147483647;
/*  851 */         for (int n = 0; n < this.xbytes; n++) {
/*  852 */           paramArrayOfByte[(j++)] = 0;
/*      */         }
/*  854 */         paramArrayOfByte[(j++)] = ((byte)m);
/*  855 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 8));
/*  856 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 16));
/*  857 */         paramArrayOfByte[(j++)] = ((byte)(m >>> 24));
/*      */       }
/*  859 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion64B extends AudioFloatConverter
/*      */   {
/*  180 */     ByteBuffer bytebuffer = null;
/*      */ 
/*  182 */     DoubleBuffer floatbuffer = null;
/*      */ 
/*  184 */     double[] double_buff = null;
/*      */ 
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  188 */       int i = paramInt3 * 8;
/*  189 */       if ((this.bytebuffer == null) || (this.bytebuffer.capacity() < i)) {
/*  190 */         this.bytebuffer = ByteBuffer.allocate(i).order(ByteOrder.BIG_ENDIAN);
/*      */ 
/*  192 */         this.floatbuffer = this.bytebuffer.asDoubleBuffer();
/*      */       }
/*  194 */       this.bytebuffer.position(0);
/*  195 */       this.floatbuffer.position(0);
/*  196 */       this.bytebuffer.put(paramArrayOfByte, paramInt1, i);
/*  197 */       if ((this.double_buff == null) || (this.double_buff.length < paramInt3 + paramInt2))
/*      */       {
/*  199 */         this.double_buff = new double[paramInt3 + paramInt2];
/*  200 */       }this.floatbuffer.get(this.double_buff, paramInt2, paramInt3);
/*  201 */       int j = paramInt2 + paramInt3;
/*  202 */       for (int k = paramInt2; k < j; k++) {
/*  203 */         paramArrayOfFloat[k] = ((float)this.double_buff[k]);
/*      */       }
/*  205 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  210 */       int i = paramInt2 * 8;
/*  211 */       if ((this.bytebuffer == null) || (this.bytebuffer.capacity() < i)) {
/*  212 */         this.bytebuffer = ByteBuffer.allocate(i).order(ByteOrder.BIG_ENDIAN);
/*      */ 
/*  214 */         this.floatbuffer = this.bytebuffer.asDoubleBuffer();
/*      */       }
/*  216 */       this.floatbuffer.position(0);
/*  217 */       this.bytebuffer.position(0);
/*  218 */       if ((this.double_buff == null) || (this.double_buff.length < paramInt1 + paramInt2))
/*  219 */         this.double_buff = new double[paramInt1 + paramInt2];
/*  220 */       int j = paramInt1 + paramInt2;
/*  221 */       for (int k = paramInt1; k < j; k++) {
/*  222 */         this.double_buff[k] = paramArrayOfFloat[k];
/*      */       }
/*  224 */       this.floatbuffer.put(this.double_buff, paramInt1, paramInt2);
/*  225 */       this.bytebuffer.get(paramArrayOfByte, paramInt3, i);
/*  226 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion64L extends AudioFloatConverter
/*      */   {
/*  128 */     ByteBuffer bytebuffer = null;
/*      */ 
/*  130 */     DoubleBuffer floatbuffer = null;
/*      */ 
/*  132 */     double[] double_buff = null;
/*      */ 
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  136 */       int i = paramInt3 * 8;
/*  137 */       if ((this.bytebuffer == null) || (this.bytebuffer.capacity() < i)) {
/*  138 */         this.bytebuffer = ByteBuffer.allocate(i).order(ByteOrder.LITTLE_ENDIAN);
/*      */ 
/*  140 */         this.floatbuffer = this.bytebuffer.asDoubleBuffer();
/*      */       }
/*  142 */       this.bytebuffer.position(0);
/*  143 */       this.floatbuffer.position(0);
/*  144 */       this.bytebuffer.put(paramArrayOfByte, paramInt1, i);
/*  145 */       if ((this.double_buff == null) || (this.double_buff.length < paramInt3 + paramInt2))
/*      */       {
/*  147 */         this.double_buff = new double[paramInt3 + paramInt2];
/*  148 */       }this.floatbuffer.get(this.double_buff, paramInt2, paramInt3);
/*  149 */       int j = paramInt2 + paramInt3;
/*  150 */       for (int k = paramInt2; k < j; k++) {
/*  151 */         paramArrayOfFloat[k] = ((float)this.double_buff[k]);
/*      */       }
/*  153 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  158 */       int i = paramInt2 * 8;
/*  159 */       if ((this.bytebuffer == null) || (this.bytebuffer.capacity() < i)) {
/*  160 */         this.bytebuffer = ByteBuffer.allocate(i).order(ByteOrder.LITTLE_ENDIAN);
/*      */ 
/*  162 */         this.floatbuffer = this.bytebuffer.asDoubleBuffer();
/*      */       }
/*  164 */       this.floatbuffer.position(0);
/*  165 */       this.bytebuffer.position(0);
/*  166 */       if ((this.double_buff == null) || (this.double_buff.length < paramInt1 + paramInt2))
/*  167 */         this.double_buff = new double[paramInt1 + paramInt2];
/*  168 */       int j = paramInt1 + paramInt2;
/*  169 */       for (int k = paramInt1; k < j; k++) {
/*  170 */         this.double_buff[k] = paramArrayOfFloat[k];
/*      */       }
/*  172 */       this.floatbuffer.put(this.double_buff, paramInt1, paramInt2);
/*  173 */       this.bytebuffer.get(paramArrayOfByte, paramInt3, i);
/*  174 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion8S extends AudioFloatConverter
/*      */   {
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  320 */       int i = paramInt1;
/*  321 */       int j = paramInt2;
/*  322 */       for (int k = 0; k < paramInt3; k++)
/*  323 */         paramArrayOfFloat[(j++)] = (paramArrayOfByte[(i++)] * 0.007874016F);
/*  324 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  329 */       int i = paramInt1;
/*  330 */       int j = paramInt3;
/*  331 */       for (int k = 0; k < paramInt2; k++)
/*  332 */         paramArrayOfByte[(j++)] = ((byte)(int)(paramArrayOfFloat[(i++)] * 127.0F));
/*  333 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatConversion8U extends AudioFloatConverter
/*      */   {
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  341 */       int i = paramInt1;
/*  342 */       int j = paramInt2;
/*  343 */       for (int k = 0; k < paramInt3; k++) {
/*  344 */         paramArrayOfFloat[(j++)] = (((paramArrayOfByte[(i++)] & 0xFF) - 127) * 0.007874016F);
/*      */       }
/*  346 */       return paramArrayOfFloat;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*  351 */       int i = paramInt1;
/*  352 */       int j = paramInt3;
/*  353 */       for (int k = 0; k < paramInt2; k++)
/*  354 */         paramArrayOfByte[(j++)] = ((byte)(int)(127.0F + paramArrayOfFloat[(i++)] * 127.0F));
/*  355 */       return paramArrayOfByte;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class AudioFloatLSBFilter extends AudioFloatConverter
/*      */   {
/*      */     private final AudioFloatConverter converter;
/*      */     private final int offset;
/*      */     private final int stepsize;
/*      */     private final byte mask;
/*      */     private byte[] mask_buffer;
/*      */ 
/*      */     AudioFloatLSBFilter(AudioFloatConverter paramAudioFloatConverter, AudioFormat paramAudioFormat)
/*      */     {
/*   65 */       int i = paramAudioFormat.getSampleSizeInBits();
/*   66 */       boolean bool = paramAudioFormat.isBigEndian();
/*   67 */       this.converter = paramAudioFloatConverter;
/*   68 */       this.stepsize = ((i + 7) / 8);
/*   69 */       this.offset = (bool ? this.stepsize - 1 : 0);
/*   70 */       int j = i % 8;
/*   71 */       if (j == 0)
/*   72 */         this.mask = 0;
/*   73 */       else if (j == 1)
/*   74 */         this.mask = -128;
/*   75 */       else if (j == 2)
/*   76 */         this.mask = -64;
/*   77 */       else if (j == 3)
/*   78 */         this.mask = -32;
/*   79 */       else if (j == 4)
/*   80 */         this.mask = -16;
/*   81 */       else if (j == 5)
/*   82 */         this.mask = -8;
/*   83 */       else if (j == 6)
/*   84 */         this.mask = -4;
/*   85 */       else if (j == 7)
/*   86 */         this.mask = -2;
/*      */       else
/*   88 */         this.mask = -1;
/*      */     }
/*      */ 
/*      */     public byte[] toByteArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2, byte[] paramArrayOfByte, int paramInt3)
/*      */     {
/*   93 */       byte[] arrayOfByte = this.converter.toByteArray(paramArrayOfFloat, paramInt1, paramInt2, paramArrayOfByte, paramInt3);
/*      */ 
/*   96 */       int i = paramInt2 * this.stepsize;
/*   97 */       for (int j = paramInt3 + this.offset; j < i; j += this.stepsize) {
/*   98 */         paramArrayOfByte[j] = ((byte)(paramArrayOfByte[j] & this.mask));
/*      */       }
/*      */ 
/*  101 */       return arrayOfByte;
/*      */     }
/*      */ 
/*      */     public float[] toFloatArray(byte[] paramArrayOfByte, int paramInt1, float[] paramArrayOfFloat, int paramInt2, int paramInt3)
/*      */     {
/*  106 */       if ((this.mask_buffer == null) || (this.mask_buffer.length < paramArrayOfByte.length))
/*  107 */         this.mask_buffer = new byte[paramArrayOfByte.length];
/*  108 */       System.arraycopy(paramArrayOfByte, 0, this.mask_buffer, 0, paramArrayOfByte.length);
/*  109 */       int i = paramInt3 * this.stepsize;
/*  110 */       for (int j = paramInt1 + this.offset; j < i; j += this.stepsize) {
/*  111 */         this.mask_buffer[j] = ((byte)(this.mask_buffer[j] & this.mask));
/*      */       }
/*  113 */       float[] arrayOfFloat = this.converter.toFloatArray(this.mask_buffer, paramInt1, paramArrayOfFloat, paramInt2, paramInt3);
/*      */ 
/*  115 */       return arrayOfFloat;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.AudioFloatConverter
 * JD-Core Version:    0.6.2
 */