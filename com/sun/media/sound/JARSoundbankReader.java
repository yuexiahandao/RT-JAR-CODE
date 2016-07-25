/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.net.URI;
/*     */ import java.net.URL;
/*     */ import java.net.URLClassLoader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Iterator;
/*     */ import javax.sound.midi.InvalidMidiDataException;
/*     */ import javax.sound.midi.Soundbank;
/*     */ import javax.sound.midi.spi.SoundbankReader;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public final class JARSoundbankReader extends SoundbankReader
/*     */ {
/*     */   private static boolean isZIP(URL paramURL)
/*     */   {
/*  49 */     boolean bool = false;
/*     */     try {
/*  51 */       InputStream localInputStream = paramURL.openStream();
/*     */       try {
/*  53 */         byte[] arrayOfByte = new byte[4];
/*  54 */         bool = localInputStream.read(arrayOfByte) == 4;
/*  55 */         if (bool) {
/*  56 */           bool = (arrayOfByte[0] == 80) && (arrayOfByte[1] == 75) && (arrayOfByte[2] == 3) && (arrayOfByte[3] == 4);
/*     */         }
/*     */ 
/*     */       }
/*     */       finally
/*     */       {
/*  62 */         localInputStream.close();
/*     */       }
/*     */     } catch (IOException localIOException) {
/*     */     }
/*  66 */     return bool;
/*     */   }
/*     */ 
/*     */   public Soundbank getSoundbank(URL paramURL) throws InvalidMidiDataException, IOException
/*     */   {
/*  71 */     if (!isZIP(paramURL))
/*  72 */       return null;
/*  73 */     ArrayList localArrayList = new ArrayList();
/*  74 */     URLClassLoader localURLClassLoader = URLClassLoader.newInstance(new URL[] { paramURL });
/*  75 */     InputStream localInputStream = localURLClassLoader.getResourceAsStream("META-INF/services/javax.sound.midi.Soundbank");
/*     */ 
/*  77 */     if (localInputStream == null)
/*  78 */       return null;
/*     */     try
/*     */     {
/*  81 */       localObject1 = new BufferedReader(new InputStreamReader(localInputStream));
/*  82 */       localObject2 = ((BufferedReader)localObject1).readLine();
/*  83 */       while (localObject2 != null) {
/*  84 */         if (!((String)localObject2).startsWith("#"))
/*     */           try {
/*  86 */             Class localClass = Class.forName(((String)localObject2).trim(), false, localURLClassLoader);
/*  87 */             if (Soundbank.class.isAssignableFrom(localClass)) {
/*  88 */               Object localObject3 = ReflectUtil.newInstance(localClass);
/*  89 */               localArrayList.add((Soundbank)localObject3);
/*     */             }
/*     */           } catch (ClassNotFoundException localClassNotFoundException) {
/*     */           } catch (InstantiationException localInstantiationException) {
/*     */           }
/*     */           catch (IllegalAccessException localIllegalAccessException) {
/*     */           }
/*  96 */         localObject2 = ((BufferedReader)localObject1).readLine();
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 101 */       localInputStream.close();
/*     */     }
/* 103 */     if (localArrayList.size() == 0)
/* 104 */       return null;
/* 105 */     if (localArrayList.size() == 1)
/* 106 */       return (Soundbank)localArrayList.get(0);
/* 107 */     Object localObject1 = new SimpleSoundbank();
/* 108 */     for (Object localObject2 = localArrayList.iterator(); ((Iterator)localObject2).hasNext(); ) { Soundbank localSoundbank = (Soundbank)((Iterator)localObject2).next();
/* 109 */       ((SimpleSoundbank)localObject1).addAllInstruments(localSoundbank); }
/* 110 */     return localObject1;
/*     */   }
/*     */ 
/*     */   public Soundbank getSoundbank(InputStream paramInputStream) throws InvalidMidiDataException, IOException
/*     */   {
/* 115 */     return null;
/*     */   }
/*     */ 
/*     */   public Soundbank getSoundbank(File paramFile) throws InvalidMidiDataException, IOException
/*     */   {
/* 120 */     return getSoundbank(paramFile.toURI().toURL());
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.JARSoundbankReader
 * JD-Core Version:    0.6.2
 */