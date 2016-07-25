/*     */ package com.sun.media.sound;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.Properties;
/*     */ import javax.sound.midi.Receiver;
/*     */ import javax.sound.midi.Sequencer;
/*     */ import javax.sound.midi.Synthesizer;
/*     */ import javax.sound.midi.Transmitter;
/*     */ import javax.sound.midi.spi.MidiDeviceProvider;
/*     */ import javax.sound.midi.spi.MidiFileReader;
/*     */ import javax.sound.midi.spi.MidiFileWriter;
/*     */ import javax.sound.midi.spi.SoundbankReader;
/*     */ import javax.sound.sampled.Clip;
/*     */ import javax.sound.sampled.Port;
/*     */ import javax.sound.sampled.SourceDataLine;
/*     */ import javax.sound.sampled.TargetDataLine;
/*     */ import javax.sound.sampled.spi.AudioFileReader;
/*     */ import javax.sound.sampled.spi.AudioFileWriter;
/*     */ import javax.sound.sampled.spi.FormatConversionProvider;
/*     */ import javax.sound.sampled.spi.MixerProvider;
/*     */ 
/*     */ public final class JDK13Services
/*     */ {
/*     */   private static final String PROPERTIES_FILENAME = "sound.properties";
/*     */   private static Properties properties;
/*     */ 
/*     */   public static List<?> getProviders(Class<?> paramClass)
/*     */   {
/*     */     Object localObject;
/*  97 */     if ((!MixerProvider.class.equals(paramClass)) && (!FormatConversionProvider.class.equals(paramClass)) && (!AudioFileReader.class.equals(paramClass)) && (!AudioFileWriter.class.equals(paramClass)) && (!MidiDeviceProvider.class.equals(paramClass)) && (!SoundbankReader.class.equals(paramClass)) && (!MidiFileWriter.class.equals(paramClass)) && (!MidiFileReader.class.equals(paramClass)))
/*     */     {
/* 105 */       localObject = new ArrayList(0);
/*     */     }
/* 107 */     else localObject = JSSecurityManager.getProviders(paramClass);
/*     */ 
/* 109 */     return Collections.unmodifiableList((List)localObject);
/*     */   }
/*     */ 
/*     */   public static synchronized String getDefaultProviderClassName(Class paramClass)
/*     */   {
/* 122 */     Object localObject = null;
/* 123 */     String str = getDefaultProvider(paramClass);
/* 124 */     if (str != null) {
/* 125 */       int i = str.indexOf('#');
/* 126 */       if (i != 0)
/*     */       {
/* 128 */         if (i > 0)
/* 129 */           localObject = str.substring(0, i);
/*     */         else
/* 131 */           localObject = str;
/*     */       }
/*     */     }
/* 134 */     return localObject;
/*     */   }
/*     */ 
/*     */   public static synchronized String getDefaultInstanceName(Class paramClass)
/*     */   {
/* 148 */     String str1 = null;
/* 149 */     String str2 = getDefaultProvider(paramClass);
/* 150 */     if (str2 != null) {
/* 151 */       int i = str2.indexOf('#');
/* 152 */       if ((i >= 0) && (i < str2.length() - 1)) {
/* 153 */         str1 = str2.substring(i + 1);
/*     */       }
/*     */     }
/* 156 */     return str1;
/*     */   }
/*     */ 
/*     */   private static synchronized String getDefaultProvider(Class paramClass)
/*     */   {
/* 169 */     if ((!SourceDataLine.class.equals(paramClass)) && (!TargetDataLine.class.equals(paramClass)) && (!Clip.class.equals(paramClass)) && (!Port.class.equals(paramClass)) && (!Receiver.class.equals(paramClass)) && (!Transmitter.class.equals(paramClass)) && (!Synthesizer.class.equals(paramClass)) && (!Sequencer.class.equals(paramClass)))
/*     */     {
/* 177 */       return null;
/*     */     }
/*     */ 
/* 180 */     String str2 = paramClass.getName();
/* 181 */     String str1 = JSSecurityManager.getProperty(str2);
/* 182 */     if (str1 == null) {
/* 183 */       str1 = getProperties().getProperty(str2);
/*     */     }
/* 185 */     if ("".equals(str1)) {
/* 186 */       str1 = null;
/*     */     }
/* 188 */     return str1;
/*     */   }
/*     */ 
/*     */   private static synchronized Properties getProperties()
/*     */   {
/* 197 */     if (properties == null) {
/* 198 */       properties = new Properties();
/* 199 */       JSSecurityManager.loadProperties(properties, "sound.properties");
/*     */     }
/* 201 */     return properties;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.JDK13Services
 * JD-Core Version:    0.6.2
 */