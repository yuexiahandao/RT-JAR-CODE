/*      */ package javax.sound.sampled;
/*      */ 
/*      */ import com.sun.media.sound.JDK13Services;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.net.URL;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashSet;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import java.util.Vector;
/*      */ import javax.sound.sampled.spi.AudioFileReader;
/*      */ import javax.sound.sampled.spi.AudioFileWriter;
/*      */ import javax.sound.sampled.spi.FormatConversionProvider;
/*      */ import javax.sound.sampled.spi.MixerProvider;
/*      */ 
/*      */ public class AudioSystem
/*      */ {
/*      */   public static final int NOT_SPECIFIED = -1;
/*      */ 
/*      */   public static Mixer.Info[] getMixerInfo()
/*      */   {
/*  197 */     List localList = getMixerInfoList();
/*  198 */     Mixer.Info[] arrayOfInfo = (Mixer.Info[])localList.toArray(new Mixer.Info[localList.size()]);
/*  199 */     return arrayOfInfo;
/*      */   }
/*      */ 
/*      */   public static Mixer getMixer(Mixer.Info paramInfo)
/*      */   {
/*  216 */     Object localObject = null;
/*  217 */     List localList = getMixerProviders();
/*      */ 
/*  219 */     for (int i = 0; i < localList.size(); i++) {
/*      */       try
/*      */       {
/*  222 */         return ((MixerProvider)localList.get(i)).getMixer(paramInfo);
/*      */       }
/*      */       catch (IllegalArgumentException localIllegalArgumentException1)
/*      */       {
/*      */       }
/*      */       catch (NullPointerException localNullPointerException1)
/*      */       {
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  236 */     if (paramInfo == null)
/*  237 */       for (i = 0; i < localList.size(); i++)
/*      */         try {
/*  239 */           MixerProvider localMixerProvider = (MixerProvider)localList.get(i);
/*  240 */           Mixer.Info[] arrayOfInfo = localMixerProvider.getMixerInfo();
/*      */ 
/*  242 */           for (int j = 0; j < arrayOfInfo.length; j++)
/*      */             try {
/*  244 */               return localMixerProvider.getMixer(arrayOfInfo[j]);
/*      */             }
/*      */             catch (IllegalArgumentException localIllegalArgumentException3)
/*      */             {
/*      */             }
/*      */         }
/*      */         catch (IllegalArgumentException localIllegalArgumentException2)
/*      */         {
/*      */         }
/*      */         catch (NullPointerException localNullPointerException2)
/*      */         {
/*      */         }
/*  256 */     throw new IllegalArgumentException("Mixer not supported: " + (paramInfo != null ? paramInfo.toString() : "null"));
/*      */   }
/*      */ 
/*      */   public static Line.Info[] getSourceLineInfo(Line.Info paramInfo)
/*      */   {
/*  275 */     Vector localVector = new Vector();
/*      */ 
/*  279 */     Object localObject = null;
/*  280 */     Mixer.Info[] arrayOfInfo = getMixerInfo();
/*      */ 
/*  282 */     for (int i = 0; i < arrayOfInfo.length; i++)
/*      */     {
/*  284 */       Mixer localMixer = getMixer(arrayOfInfo[i]);
/*      */ 
/*  286 */       Line.Info[] arrayOfInfo1 = localMixer.getSourceLineInfo(paramInfo);
/*  287 */       for (j = 0; j < arrayOfInfo1.length; j++) {
/*  288 */         localVector.addElement(arrayOfInfo1[j]);
/*      */       }
/*      */     }
/*      */ 
/*  292 */     Line.Info[] arrayOfInfo2 = new Line.Info[localVector.size()];
/*      */ 
/*  294 */     for (int j = 0; j < arrayOfInfo2.length; j++) {
/*  295 */       arrayOfInfo2[j] = ((Line.Info)localVector.get(j));
/*      */     }
/*      */ 
/*  298 */     return arrayOfInfo2;
/*      */   }
/*      */ 
/*      */   public static Line.Info[] getTargetLineInfo(Line.Info paramInfo)
/*      */   {
/*  315 */     Vector localVector = new Vector();
/*      */ 
/*  319 */     Object localObject = null;
/*  320 */     Mixer.Info[] arrayOfInfo = getMixerInfo();
/*      */ 
/*  322 */     for (int i = 0; i < arrayOfInfo.length; i++)
/*      */     {
/*  324 */       Mixer localMixer = getMixer(arrayOfInfo[i]);
/*      */ 
/*  326 */       Line.Info[] arrayOfInfo1 = localMixer.getTargetLineInfo(paramInfo);
/*  327 */       for (j = 0; j < arrayOfInfo1.length; j++) {
/*  328 */         localVector.addElement(arrayOfInfo1[j]);
/*      */       }
/*      */     }
/*      */ 
/*  332 */     Line.Info[] arrayOfInfo2 = new Line.Info[localVector.size()];
/*      */ 
/*  334 */     for (int j = 0; j < arrayOfInfo2.length; j++) {
/*  335 */       arrayOfInfo2[j] = ((Line.Info)localVector.get(j));
/*      */     }
/*      */ 
/*  338 */     return arrayOfInfo2;
/*      */   }
/*      */ 
/*      */   public static boolean isLineSupported(Line.Info paramInfo)
/*      */   {
/*  355 */     Mixer.Info[] arrayOfInfo = getMixerInfo();
/*      */ 
/*  357 */     for (int i = 0; i < arrayOfInfo.length; i++)
/*      */     {
/*  359 */       if (arrayOfInfo[i] != null) {
/*  360 */         Mixer localMixer = getMixer(arrayOfInfo[i]);
/*  361 */         if (localMixer.isLineSupported(paramInfo)) {
/*  362 */           return true;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  367 */     return false;
/*      */   }
/*      */ 
/*      */   public static Line getLine(Line.Info paramInfo)
/*      */     throws LineUnavailableException
/*      */   {
/*  408 */     Object localObject = null;
/*  409 */     List localList = getMixerProviders();
/*      */     try
/*      */     {
/*  414 */       Mixer localMixer1 = getDefaultMixer(localList, paramInfo);
/*  415 */       if ((localMixer1 != null) && (localMixer1.isLineSupported(paramInfo)))
/*  416 */         return localMixer1.getLine(paramInfo);
/*      */     }
/*      */     catch (LineUnavailableException localLineUnavailableException1) {
/*  419 */       localObject = localLineUnavailableException1;
/*      */     }
/*      */     catch (IllegalArgumentException localIllegalArgumentException1)
/*      */     {
/*      */     }
/*      */     MixerProvider localMixerProvider;
/*      */     Mixer.Info[] arrayOfInfo;
/*      */     int j;
/*  427 */     for (int i = 0; i < localList.size(); i++) {
/*  428 */       localMixerProvider = (MixerProvider)localList.get(i);
/*  429 */       arrayOfInfo = localMixerProvider.getMixerInfo();
/*      */ 
/*  431 */       for (j = 0; j < arrayOfInfo.length; j++) {
/*      */         try {
/*  433 */           Mixer localMixer2 = localMixerProvider.getMixer(arrayOfInfo[j]);
/*      */ 
/*  435 */           if (isAppropriateMixer(localMixer2, paramInfo, true))
/*  436 */             return localMixer2.getLine(paramInfo);
/*      */         }
/*      */         catch (LineUnavailableException localLineUnavailableException2) {
/*  439 */           localObject = localLineUnavailableException2;
/*      */         }
/*      */         catch (IllegalArgumentException localIllegalArgumentException2)
/*      */         {
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  449 */     for (i = 0; i < localList.size(); i++) {
/*  450 */       localMixerProvider = (MixerProvider)localList.get(i);
/*  451 */       arrayOfInfo = localMixerProvider.getMixerInfo();
/*  452 */       for (j = 0; j < arrayOfInfo.length; j++) {
/*      */         try {
/*  454 */           Mixer localMixer3 = localMixerProvider.getMixer(arrayOfInfo[j]);
/*      */ 
/*  456 */           if (isAppropriateMixer(localMixer3, paramInfo, false))
/*  457 */             return localMixer3.getLine(paramInfo);
/*      */         }
/*      */         catch (LineUnavailableException localLineUnavailableException3) {
/*  460 */           localObject = localLineUnavailableException3;
/*      */         }
/*      */         catch (IllegalArgumentException localIllegalArgumentException3)
/*      */         {
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  470 */     if (localObject != null) {
/*  471 */       throw localObject;
/*      */     }
/*      */ 
/*  476 */     throw new IllegalArgumentException("No line matching " + paramInfo.toString() + " is supported.");
/*      */   }
/*      */ 
/*      */   public static Clip getClip()
/*      */     throws LineUnavailableException
/*      */   {
/*  515 */     AudioFormat localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, -1.0F, 16, 2, 4, -1.0F, true);
/*      */ 
/*  519 */     DataLine.Info localInfo = new DataLine.Info(Clip.class, localAudioFormat);
/*  520 */     return (Clip)getLine(localInfo);
/*      */   }
/*      */ 
/*      */   public static Clip getClip(Mixer.Info paramInfo)
/*      */     throws LineUnavailableException
/*      */   {
/*  550 */     AudioFormat localAudioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, -1.0F, 16, 2, 4, -1.0F, true);
/*      */ 
/*  554 */     DataLine.Info localInfo = new DataLine.Info(Clip.class, localAudioFormat);
/*  555 */     Mixer localMixer = getMixer(paramInfo);
/*  556 */     return (Clip)localMixer.getLine(localInfo);
/*      */   }
/*      */ 
/*      */   public static SourceDataLine getSourceDataLine(AudioFormat paramAudioFormat)
/*      */     throws LineUnavailableException
/*      */   {
/*  603 */     DataLine.Info localInfo = new DataLine.Info(SourceDataLine.class, paramAudioFormat);
/*  604 */     return (SourceDataLine)getLine(localInfo);
/*      */   }
/*      */ 
/*      */   public static SourceDataLine getSourceDataLine(AudioFormat paramAudioFormat, Mixer.Info paramInfo)
/*      */     throws LineUnavailableException
/*      */   {
/*  648 */     DataLine.Info localInfo = new DataLine.Info(SourceDataLine.class, paramAudioFormat);
/*  649 */     Mixer localMixer = getMixer(paramInfo);
/*  650 */     return (SourceDataLine)localMixer.getLine(localInfo);
/*      */   }
/*      */ 
/*      */   public static TargetDataLine getTargetDataLine(AudioFormat paramAudioFormat)
/*      */     throws LineUnavailableException
/*      */   {
/*  699 */     DataLine.Info localInfo = new DataLine.Info(TargetDataLine.class, paramAudioFormat);
/*  700 */     return (TargetDataLine)getLine(localInfo);
/*      */   }
/*      */ 
/*      */   public static TargetDataLine getTargetDataLine(AudioFormat paramAudioFormat, Mixer.Info paramInfo)
/*      */     throws LineUnavailableException
/*      */   {
/*  746 */     DataLine.Info localInfo = new DataLine.Info(TargetDataLine.class, paramAudioFormat);
/*  747 */     Mixer localMixer = getMixer(paramInfo);
/*  748 */     return (TargetDataLine)localMixer.getLine(localInfo);
/*      */   }
/*      */ 
/*      */   public static AudioFormat.Encoding[] getTargetEncodings(AudioFormat.Encoding paramEncoding)
/*      */   {
/*  765 */     List localList = getFormatConversionProviders();
/*  766 */     Vector localVector = new Vector();
/*      */ 
/*  768 */     AudioFormat.Encoding[] arrayOfEncoding1 = null;
/*      */ 
/*  771 */     for (int i = 0; i < localList.size(); i++) {
/*  772 */       FormatConversionProvider localFormatConversionProvider = (FormatConversionProvider)localList.get(i);
/*  773 */       if (localFormatConversionProvider.isSourceEncodingSupported(paramEncoding)) {
/*  774 */         arrayOfEncoding1 = localFormatConversionProvider.getTargetEncodings();
/*  775 */         for (int j = 0; j < arrayOfEncoding1.length; j++) {
/*  776 */           localVector.addElement(arrayOfEncoding1[j]);
/*      */         }
/*      */       }
/*      */     }
/*  780 */     AudioFormat.Encoding[] arrayOfEncoding2 = (AudioFormat.Encoding[])localVector.toArray(new AudioFormat.Encoding[0]);
/*  781 */     return arrayOfEncoding2;
/*      */   }
/*      */ 
/*      */   public static AudioFormat.Encoding[] getTargetEncodings(AudioFormat paramAudioFormat)
/*      */   {
/*  800 */     List localList = getFormatConversionProviders();
/*  801 */     Vector localVector = new Vector();
/*      */ 
/*  803 */     int i = 0;
/*  804 */     int j = 0;
/*  805 */     AudioFormat.Encoding[] arrayOfEncoding1 = null;
/*      */ 
/*  809 */     for (int k = 0; k < localList.size(); k++) {
/*  810 */       arrayOfEncoding1 = ((FormatConversionProvider)localList.get(k)).getTargetEncodings(paramAudioFormat);
/*  811 */       i += arrayOfEncoding1.length;
/*  812 */       localVector.addElement(arrayOfEncoding1);
/*      */     }
/*      */ 
/*  817 */     AudioFormat.Encoding[] arrayOfEncoding2 = new AudioFormat.Encoding[i];
/*  818 */     for (int m = 0; m < localVector.size(); m++) {
/*  819 */       arrayOfEncoding1 = (AudioFormat.Encoding[])localVector.get(m);
/*  820 */       for (int n = 0; n < arrayOfEncoding1.length; n++) {
/*  821 */         arrayOfEncoding2[(j++)] = arrayOfEncoding1[n];
/*      */       }
/*      */     }
/*  824 */     return arrayOfEncoding2;
/*      */   }
/*      */ 
/*      */   public static boolean isConversionSupported(AudioFormat.Encoding paramEncoding, AudioFormat paramAudioFormat)
/*      */   {
/*  840 */     List localList = getFormatConversionProviders();
/*      */ 
/*  842 */     for (int i = 0; i < localList.size(); i++) {
/*  843 */       FormatConversionProvider localFormatConversionProvider = (FormatConversionProvider)localList.get(i);
/*  844 */       if (localFormatConversionProvider.isConversionSupported(paramEncoding, paramAudioFormat)) {
/*  845 */         return true;
/*      */       }
/*      */     }
/*  848 */     return false;
/*      */   }
/*      */ 
/*      */   public static AudioInputStream getAudioInputStream(AudioFormat.Encoding paramEncoding, AudioInputStream paramAudioInputStream)
/*      */   {
/*  867 */     List localList = getFormatConversionProviders();
/*      */ 
/*  869 */     for (int i = 0; i < localList.size(); i++) {
/*  870 */       FormatConversionProvider localFormatConversionProvider = (FormatConversionProvider)localList.get(i);
/*  871 */       if (localFormatConversionProvider.isConversionSupported(paramEncoding, paramAudioInputStream.getFormat())) {
/*  872 */         return localFormatConversionProvider.getAudioInputStream(paramEncoding, paramAudioInputStream);
/*      */       }
/*      */     }
/*      */ 
/*  876 */     throw new IllegalArgumentException("Unsupported conversion: " + paramEncoding + " from " + paramAudioInputStream.getFormat());
/*      */   }
/*      */ 
/*      */   public static AudioFormat[] getTargetFormats(AudioFormat.Encoding paramEncoding, AudioFormat paramAudioFormat)
/*      */   {
/*  891 */     List localList = getFormatConversionProviders();
/*  892 */     Vector localVector = new Vector();
/*      */ 
/*  894 */     int i = 0;
/*  895 */     int j = 0;
/*  896 */     AudioFormat[] arrayOfAudioFormat1 = null;
/*      */ 
/*  900 */     for (int k = 0; k < localList.size(); k++) {
/*  901 */       FormatConversionProvider localFormatConversionProvider = (FormatConversionProvider)localList.get(k);
/*  902 */       arrayOfAudioFormat1 = localFormatConversionProvider.getTargetFormats(paramEncoding, paramAudioFormat);
/*  903 */       i += arrayOfAudioFormat1.length;
/*  904 */       localVector.addElement(arrayOfAudioFormat1);
/*      */     }
/*      */ 
/*  909 */     AudioFormat[] arrayOfAudioFormat2 = new AudioFormat[i];
/*  910 */     for (int m = 0; m < localVector.size(); m++) {
/*  911 */       arrayOfAudioFormat1 = (AudioFormat[])localVector.get(m);
/*  912 */       for (int n = 0; n < arrayOfAudioFormat1.length; n++) {
/*  913 */         arrayOfAudioFormat2[(j++)] = arrayOfAudioFormat1[n];
/*      */       }
/*      */     }
/*  916 */     return arrayOfAudioFormat2;
/*      */   }
/*      */ 
/*      */   public static boolean isConversionSupported(AudioFormat paramAudioFormat1, AudioFormat paramAudioFormat2)
/*      */   {
/*  931 */     List localList = getFormatConversionProviders();
/*      */ 
/*  933 */     for (int i = 0; i < localList.size(); i++) {
/*  934 */       FormatConversionProvider localFormatConversionProvider = (FormatConversionProvider)localList.get(i);
/*  935 */       if (localFormatConversionProvider.isConversionSupported(paramAudioFormat1, paramAudioFormat2)) {
/*  936 */         return true;
/*      */       }
/*      */     }
/*  939 */     return false;
/*      */   }
/*      */ 
/*      */   public static AudioInputStream getAudioInputStream(AudioFormat paramAudioFormat, AudioInputStream paramAudioInputStream)
/*      */   {
/*  958 */     if (paramAudioInputStream.getFormat().matches(paramAudioFormat)) {
/*  959 */       return paramAudioInputStream;
/*      */     }
/*      */ 
/*  962 */     List localList = getFormatConversionProviders();
/*      */ 
/*  964 */     for (int i = 0; i < localList.size(); i++) {
/*  965 */       FormatConversionProvider localFormatConversionProvider = (FormatConversionProvider)localList.get(i);
/*  966 */       if (localFormatConversionProvider.isConversionSupported(paramAudioFormat, paramAudioInputStream.getFormat())) {
/*  967 */         return localFormatConversionProvider.getAudioInputStream(paramAudioFormat, paramAudioInputStream);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  972 */     throw new IllegalArgumentException("Unsupported conversion: " + paramAudioFormat + " from " + paramAudioInputStream.getFormat());
/*      */   }
/*      */ 
/*      */   public static AudioFileFormat getAudioFileFormat(InputStream paramInputStream)
/*      */     throws UnsupportedAudioFileException, IOException
/*      */   {
/*  996 */     List localList = getAudioFileReaders();
/*  997 */     AudioFileFormat localAudioFileFormat = null;
/*      */ 
/*  999 */     for (int i = 0; i < localList.size(); i++) {
/* 1000 */       AudioFileReader localAudioFileReader = (AudioFileReader)localList.get(i);
/*      */       try {
/* 1002 */         localAudioFileFormat = localAudioFileReader.getAudioFileFormat(paramInputStream);
/*      */       }
/*      */       catch (UnsupportedAudioFileException localUnsupportedAudioFileException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/* 1009 */     if (localAudioFileFormat == null) {
/* 1010 */       throw new UnsupportedAudioFileException("file is not a supported file type");
/*      */     }
/* 1012 */     return localAudioFileFormat;
/*      */   }
/*      */ 
/*      */   public static AudioFileFormat getAudioFileFormat(URL paramURL)
/*      */     throws UnsupportedAudioFileException, IOException
/*      */   {
/* 1029 */     List localList = getAudioFileReaders();
/* 1030 */     AudioFileFormat localAudioFileFormat = null;
/*      */ 
/* 1032 */     for (int i = 0; i < localList.size(); i++) {
/* 1033 */       AudioFileReader localAudioFileReader = (AudioFileReader)localList.get(i);
/*      */       try {
/* 1035 */         localAudioFileFormat = localAudioFileReader.getAudioFileFormat(paramURL);
/*      */       }
/*      */       catch (UnsupportedAudioFileException localUnsupportedAudioFileException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/* 1042 */     if (localAudioFileFormat == null) {
/* 1043 */       throw new UnsupportedAudioFileException("file is not a supported file type");
/*      */     }
/* 1045 */     return localAudioFileFormat;
/*      */   }
/*      */ 
/*      */   public static AudioFileFormat getAudioFileFormat(File paramFile)
/*      */     throws UnsupportedAudioFileException, IOException
/*      */   {
/* 1062 */     List localList = getAudioFileReaders();
/* 1063 */     AudioFileFormat localAudioFileFormat = null;
/*      */ 
/* 1065 */     for (int i = 0; i < localList.size(); i++) {
/* 1066 */       AudioFileReader localAudioFileReader = (AudioFileReader)localList.get(i);
/*      */       try {
/* 1068 */         localAudioFileFormat = localAudioFileReader.getAudioFileFormat(paramFile);
/*      */       }
/*      */       catch (UnsupportedAudioFileException localUnsupportedAudioFileException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/* 1075 */     if (localAudioFileFormat == null) {
/* 1076 */       throw new UnsupportedAudioFileException("file is not a supported file type");
/*      */     }
/* 1078 */     return localAudioFileFormat;
/*      */   }
/*      */ 
/*      */   public static AudioInputStream getAudioInputStream(InputStream paramInputStream)
/*      */     throws UnsupportedAudioFileException, IOException
/*      */   {
/* 1105 */     List localList = getAudioFileReaders();
/* 1106 */     AudioInputStream localAudioInputStream = null;
/*      */ 
/* 1108 */     for (int i = 0; i < localList.size(); i++) {
/* 1109 */       AudioFileReader localAudioFileReader = (AudioFileReader)localList.get(i);
/*      */       try {
/* 1111 */         localAudioInputStream = localAudioFileReader.getAudioInputStream(paramInputStream);
/*      */       }
/*      */       catch (UnsupportedAudioFileException localUnsupportedAudioFileException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/* 1118 */     if (localAudioInputStream == null) {
/* 1119 */       throw new UnsupportedAudioFileException("could not get audio input stream from input stream");
/*      */     }
/* 1121 */     return localAudioInputStream;
/*      */   }
/*      */ 
/*      */   public static AudioInputStream getAudioInputStream(URL paramURL)
/*      */     throws UnsupportedAudioFileException, IOException
/*      */   {
/* 1139 */     List localList = getAudioFileReaders();
/* 1140 */     AudioInputStream localAudioInputStream = null;
/*      */ 
/* 1142 */     for (int i = 0; i < localList.size(); i++) {
/* 1143 */       AudioFileReader localAudioFileReader = (AudioFileReader)localList.get(i);
/*      */       try {
/* 1145 */         localAudioInputStream = localAudioFileReader.getAudioInputStream(paramURL);
/*      */       }
/*      */       catch (UnsupportedAudioFileException localUnsupportedAudioFileException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/* 1152 */     if (localAudioInputStream == null) {
/* 1153 */       throw new UnsupportedAudioFileException("could not get audio input stream from input URL");
/*      */     }
/* 1155 */     return localAudioInputStream;
/*      */   }
/*      */ 
/*      */   public static AudioInputStream getAudioInputStream(File paramFile)
/*      */     throws UnsupportedAudioFileException, IOException
/*      */   {
/* 1173 */     List localList = getAudioFileReaders();
/* 1174 */     AudioInputStream localAudioInputStream = null;
/*      */ 
/* 1176 */     for (int i = 0; i < localList.size(); i++) {
/* 1177 */       AudioFileReader localAudioFileReader = (AudioFileReader)localList.get(i);
/*      */       try {
/* 1179 */         localAudioInputStream = localAudioFileReader.getAudioInputStream(paramFile);
/*      */       }
/*      */       catch (UnsupportedAudioFileException localUnsupportedAudioFileException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/* 1186 */     if (localAudioInputStream == null) {
/* 1187 */       throw new UnsupportedAudioFileException("could not get audio input stream from input file");
/*      */     }
/* 1189 */     return localAudioInputStream;
/*      */   }
/*      */ 
/*      */   public static AudioFileFormat.Type[] getAudioFileTypes()
/*      */   {
/* 1200 */     List localList = getAudioFileWriters();
/* 1201 */     HashSet localHashSet = new HashSet();
/*      */ 
/* 1203 */     for (int i = 0; i < localList.size(); i++) {
/* 1204 */       AudioFileWriter localAudioFileWriter = (AudioFileWriter)localList.get(i);
/* 1205 */       AudioFileFormat.Type[] arrayOfType2 = localAudioFileWriter.getAudioFileTypes();
/* 1206 */       for (int j = 0; j < arrayOfType2.length; j++) {
/* 1207 */         localHashSet.add(arrayOfType2[j]);
/*      */       }
/*      */     }
/* 1210 */     AudioFileFormat.Type[] arrayOfType1 = (AudioFileFormat.Type[])localHashSet.toArray(new AudioFileFormat.Type[0]);
/*      */ 
/* 1212 */     return arrayOfType1;
/*      */   }
/*      */ 
/*      */   public static boolean isFileTypeSupported(AudioFileFormat.Type paramType)
/*      */   {
/* 1225 */     List localList = getAudioFileWriters();
/*      */ 
/* 1227 */     for (int i = 0; i < localList.size(); i++) {
/* 1228 */       AudioFileWriter localAudioFileWriter = (AudioFileWriter)localList.get(i);
/* 1229 */       if (localAudioFileWriter.isFileTypeSupported(paramType)) {
/* 1230 */         return true;
/*      */       }
/*      */     }
/* 1233 */     return false;
/*      */   }
/*      */ 
/*      */   public static AudioFileFormat.Type[] getAudioFileTypes(AudioInputStream paramAudioInputStream)
/*      */   {
/* 1246 */     List localList = getAudioFileWriters();
/* 1247 */     HashSet localHashSet = new HashSet();
/*      */ 
/* 1249 */     for (int i = 0; i < localList.size(); i++) {
/* 1250 */       AudioFileWriter localAudioFileWriter = (AudioFileWriter)localList.get(i);
/* 1251 */       AudioFileFormat.Type[] arrayOfType2 = localAudioFileWriter.getAudioFileTypes(paramAudioInputStream);
/* 1252 */       for (int j = 0; j < arrayOfType2.length; j++) {
/* 1253 */         localHashSet.add(arrayOfType2[j]);
/*      */       }
/*      */     }
/* 1256 */     AudioFileFormat.Type[] arrayOfType1 = (AudioFileFormat.Type[])localHashSet.toArray(new AudioFileFormat.Type[0]);
/*      */ 
/* 1258 */     return arrayOfType1;
/*      */   }
/*      */ 
/*      */   public static boolean isFileTypeSupported(AudioFileFormat.Type paramType, AudioInputStream paramAudioInputStream)
/*      */   {
/* 1273 */     List localList = getAudioFileWriters();
/*      */ 
/* 1275 */     for (int i = 0; i < localList.size(); i++) {
/* 1276 */       AudioFileWriter localAudioFileWriter = (AudioFileWriter)localList.get(i);
/* 1277 */       if (localAudioFileWriter.isFileTypeSupported(paramType, paramAudioInputStream)) {
/* 1278 */         return true;
/*      */       }
/*      */     }
/* 1281 */     return false;
/*      */   }
/*      */ 
/*      */   public static int write(AudioInputStream paramAudioInputStream, AudioFileFormat.Type paramType, OutputStream paramOutputStream)
/*      */     throws IOException
/*      */   {
/* 1307 */     List localList = getAudioFileWriters();
/* 1308 */     int i = 0;
/* 1309 */     int j = 0;
/*      */ 
/* 1311 */     for (int k = 0; k < localList.size(); k++) {
/* 1312 */       AudioFileWriter localAudioFileWriter = (AudioFileWriter)localList.get(k);
/*      */       try {
/* 1314 */         i = localAudioFileWriter.write(paramAudioInputStream, paramType, paramOutputStream);
/* 1315 */         j = 1;
/*      */       }
/*      */       catch (IllegalArgumentException localIllegalArgumentException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/* 1322 */     if (j == 0) {
/* 1323 */       throw new IllegalArgumentException("could not write audio file: file type not supported: " + paramType);
/*      */     }
/* 1325 */     return i;
/*      */   }
/*      */ 
/*      */   public static int write(AudioInputStream paramAudioInputStream, AudioFileFormat.Type paramType, File paramFile)
/*      */     throws IOException
/*      */   {
/* 1347 */     List localList = getAudioFileWriters();
/* 1348 */     int i = 0;
/* 1349 */     int j = 0;
/*      */ 
/* 1351 */     for (int k = 0; k < localList.size(); k++) {
/* 1352 */       AudioFileWriter localAudioFileWriter = (AudioFileWriter)localList.get(k);
/*      */       try {
/* 1354 */         i = localAudioFileWriter.write(paramAudioInputStream, paramType, paramFile);
/* 1355 */         j = 1;
/*      */       }
/*      */       catch (IllegalArgumentException localIllegalArgumentException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/* 1362 */     if (j == 0) {
/* 1363 */       throw new IllegalArgumentException("could not write audio file: file type not supported: " + paramType);
/*      */     }
/* 1365 */     return i;
/*      */   }
/*      */ 
/*      */   private static List getMixerProviders()
/*      */   {
/* 1376 */     return getProviders(MixerProvider.class);
/*      */   }
/*      */ 
/*      */   private static List getFormatConversionProviders()
/*      */   {
/* 1391 */     return getProviders(FormatConversionProvider.class);
/*      */   }
/*      */ 
/*      */   private static List getAudioFileReaders()
/*      */   {
/* 1404 */     return getProviders(AudioFileReader.class);
/*      */   }
/*      */ 
/*      */   private static List getAudioFileWriters()
/*      */   {
/* 1416 */     return getProviders(AudioFileWriter.class);
/*      */   }
/*      */ 
/*      */   private static Mixer getDefaultMixer(List paramList, Line.Info paramInfo)
/*      */   {
/* 1430 */     Class localClass = paramInfo.getLineClass();
/* 1431 */     String str1 = JDK13Services.getDefaultProviderClassName(localClass);
/* 1432 */     String str2 = JDK13Services.getDefaultInstanceName(localClass);
/*      */     Mixer localMixer;
/* 1435 */     if (str1 != null) {
/* 1436 */       MixerProvider localMixerProvider = getNamedProvider(str1, paramList);
/* 1437 */       if (localMixerProvider != null) {
/* 1438 */         if (str2 != null) {
/* 1439 */           localMixer = getNamedMixer(str2, localMixerProvider, paramInfo);
/* 1440 */           if (localMixer != null)
/* 1441 */             return localMixer;
/*      */         }
/*      */         else {
/* 1444 */           localMixer = getFirstMixer(localMixerProvider, paramInfo, false);
/*      */ 
/* 1446 */           if (localMixer != null) {
/* 1447 */             return localMixer;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1457 */     if (str2 != null) {
/* 1458 */       localMixer = getNamedMixer(str2, paramList, paramInfo);
/* 1459 */       if (localMixer != null) {
/* 1460 */         return localMixer;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1467 */     return null;
/*      */   }
/*      */ 
/*      */   private static MixerProvider getNamedProvider(String paramString, List paramList)
/*      */   {
/* 1483 */     for (int i = 0; i < paramList.size(); i++) {
/* 1484 */       MixerProvider localMixerProvider = (MixerProvider)paramList.get(i);
/* 1485 */       if (localMixerProvider.getClass().getName().equals(paramString)) {
/* 1486 */         return localMixerProvider;
/*      */       }
/*      */     }
/* 1489 */     return null;
/*      */   }
/*      */ 
/*      */   private static Mixer getNamedMixer(String paramString, MixerProvider paramMixerProvider, Line.Info paramInfo)
/*      */   {
/* 1505 */     Mixer.Info[] arrayOfInfo = paramMixerProvider.getMixerInfo();
/* 1506 */     for (int i = 0; i < arrayOfInfo.length; i++) {
/* 1507 */       if (arrayOfInfo[i].getName().equals(paramString)) {
/* 1508 */         Mixer localMixer = paramMixerProvider.getMixer(arrayOfInfo[i]);
/* 1509 */         if (isAppropriateMixer(localMixer, paramInfo, false)) {
/* 1510 */           return localMixer;
/*      */         }
/*      */       }
/*      */     }
/* 1514 */     return null;
/*      */   }
/*      */ 
/*      */   private static Mixer getNamedMixer(String paramString, List paramList, Line.Info paramInfo)
/*      */   {
/* 1529 */     for (int i = 0; i < paramList.size(); i++) {
/* 1530 */       MixerProvider localMixerProvider = (MixerProvider)paramList.get(i);
/* 1531 */       Mixer localMixer = getNamedMixer(paramString, localMixerProvider, paramInfo);
/* 1532 */       if (localMixer != null) {
/* 1533 */         return localMixer;
/*      */       }
/*      */     }
/* 1536 */     return null;
/*      */   }
/*      */ 
/*      */   private static Mixer getFirstMixer(MixerProvider paramMixerProvider, Line.Info paramInfo, boolean paramBoolean)
/*      */   {
/* 1553 */     Mixer.Info[] arrayOfInfo = paramMixerProvider.getMixerInfo();
/* 1554 */     for (int i = 0; i < arrayOfInfo.length; i++) {
/* 1555 */       Mixer localMixer = paramMixerProvider.getMixer(arrayOfInfo[i]);
/* 1556 */       if (isAppropriateMixer(localMixer, paramInfo, paramBoolean)) {
/* 1557 */         return localMixer;
/*      */       }
/*      */     }
/* 1560 */     return null;
/*      */   }
/*      */ 
/*      */   private static boolean isAppropriateMixer(Mixer paramMixer, Line.Info paramInfo, boolean paramBoolean)
/*      */   {
/* 1576 */     if (!paramMixer.isLineSupported(paramInfo)) {
/* 1577 */       return false;
/*      */     }
/* 1579 */     Class localClass = paramInfo.getLineClass();
/* 1580 */     if ((paramBoolean) && ((SourceDataLine.class.isAssignableFrom(localClass)) || (Clip.class.isAssignableFrom(localClass))))
/*      */     {
/* 1583 */       int i = paramMixer.getMaxLines(paramInfo);
/* 1584 */       return (i == -1) || (i > 1);
/*      */     }
/* 1586 */     return true;
/*      */   }
/*      */ 
/*      */   private static List getMixerInfoList()
/*      */   {
/* 1595 */     List localList = getMixerProviders();
/* 1596 */     return getMixerInfoList(localList);
/*      */   }
/*      */ 
/*      */   private static List getMixerInfoList(List paramList)
/*      */   {
/* 1604 */     ArrayList localArrayList = new ArrayList();
/*      */ 
/* 1609 */     for (int i = 0; i < paramList.size(); i++) {
/* 1610 */       Mixer.Info[] arrayOfInfo = (Mixer.Info[])((MixerProvider)paramList.get(i)).getMixerInfo();
/*      */ 
/* 1613 */       for (int j = 0; j < arrayOfInfo.length; j++) {
/* 1614 */         localArrayList.add(arrayOfInfo[j]);
/*      */       }
/*      */     }
/*      */ 
/* 1618 */     return localArrayList;
/*      */   }
/*      */ 
/*      */   private static List getProviders(Class paramClass)
/*      */   {
/* 1629 */     return JDK13Services.getProviders(paramClass);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.AudioSystem
 * JD-Core Version:    0.6.2
 */