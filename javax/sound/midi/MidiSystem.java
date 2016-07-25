/*      */ package javax.sound.midi;
/*      */ 
/*      */ import com.sun.media.sound.AutoConnectSequencer;
/*      */ import com.sun.media.sound.JDK13Services;
/*      */ import com.sun.media.sound.MidiDeviceReceiverEnvelope;
/*      */ import com.sun.media.sound.MidiDeviceTransmitterEnvelope;
/*      */ import com.sun.media.sound.ReferenceCountingDevice;
/*      */ import java.io.File;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.OutputStream;
/*      */ import java.net.URL;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashSet;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Set;
/*      */ import javax.sound.midi.spi.MidiDeviceProvider;
/*      */ import javax.sound.midi.spi.MidiFileReader;
/*      */ import javax.sound.midi.spi.MidiFileWriter;
/*      */ import javax.sound.midi.spi.SoundbankReader;
/*      */ 
/*      */ public class MidiSystem
/*      */ {
/*      */   public static MidiDevice.Info[] getMidiDeviceInfo()
/*      */   {
/*  187 */     ArrayList localArrayList = new ArrayList();
/*  188 */     List localList = getMidiDeviceProviders();
/*      */ 
/*  190 */     for (int i = 0; i < localList.size(); i++) {
/*  191 */       MidiDeviceProvider localMidiDeviceProvider = (MidiDeviceProvider)localList.get(i);
/*  192 */       MidiDevice.Info[] arrayOfInfo2 = localMidiDeviceProvider.getDeviceInfo();
/*  193 */       for (int j = 0; j < arrayOfInfo2.length; j++) {
/*  194 */         localArrayList.add(arrayOfInfo2[j]);
/*      */       }
/*      */     }
/*  197 */     MidiDevice.Info[] arrayOfInfo1 = (MidiDevice.Info[])localArrayList.toArray(new MidiDevice.Info[0]);
/*  198 */     return arrayOfInfo1;
/*      */   }
/*      */ 
/*      */   public static MidiDevice getMidiDevice(MidiDevice.Info paramInfo)
/*      */     throws MidiUnavailableException
/*      */   {
/*  214 */     List localList = getMidiDeviceProviders();
/*      */ 
/*  216 */     for (int i = 0; i < localList.size(); i++) {
/*  217 */       MidiDeviceProvider localMidiDeviceProvider = (MidiDeviceProvider)localList.get(i);
/*  218 */       if (localMidiDeviceProvider.isDeviceSupported(paramInfo)) {
/*  219 */         MidiDevice localMidiDevice = localMidiDeviceProvider.getDevice(paramInfo);
/*  220 */         return localMidiDevice;
/*      */       }
/*      */     }
/*  223 */     throw new IllegalArgumentException("Requested device not installed: " + paramInfo);
/*      */   }
/*      */ 
/*      */   public static Receiver getReceiver()
/*      */     throws MidiUnavailableException
/*      */   {
/*  267 */     MidiDevice localMidiDevice = getDefaultDeviceWrapper(Receiver.class);
/*      */     Object localObject;
/*  269 */     if ((localMidiDevice instanceof ReferenceCountingDevice))
/*  270 */       localObject = ((ReferenceCountingDevice)localMidiDevice).getReceiverReferenceCounting();
/*      */     else {
/*  272 */       localObject = localMidiDevice.getReceiver();
/*      */     }
/*  274 */     if (!(localObject instanceof MidiDeviceReceiver)) {
/*  275 */       localObject = new MidiDeviceReceiverEnvelope(localMidiDevice, (Receiver)localObject);
/*      */     }
/*  277 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static Transmitter getTransmitter()
/*      */     throws MidiUnavailableException
/*      */   {
/*  318 */     MidiDevice localMidiDevice = getDefaultDeviceWrapper(Transmitter.class);
/*      */     Object localObject;
/*  320 */     if ((localMidiDevice instanceof ReferenceCountingDevice))
/*  321 */       localObject = ((ReferenceCountingDevice)localMidiDevice).getTransmitterReferenceCounting();
/*      */     else {
/*  323 */       localObject = localMidiDevice.getTransmitter();
/*      */     }
/*  325 */     if (!(localObject instanceof MidiDeviceTransmitter)) {
/*  326 */       localObject = new MidiDeviceTransmitterEnvelope(localMidiDevice, (Transmitter)localObject);
/*      */     }
/*  328 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static Synthesizer getSynthesizer()
/*      */     throws MidiUnavailableException
/*      */   {
/*  348 */     return (Synthesizer)getDefaultDeviceWrapper(Synthesizer.class);
/*      */   }
/*      */ 
/*      */   public static Sequencer getSequencer()
/*      */     throws MidiUnavailableException
/*      */   {
/*  389 */     return getSequencer(true);
/*      */   }
/*      */ 
/*      */   public static Sequencer getSequencer(boolean paramBoolean)
/*      */     throws MidiUnavailableException
/*      */   {
/*  439 */     Sequencer localSequencer = (Sequencer)getDefaultDeviceWrapper(Sequencer.class);
/*      */ 
/*  441 */     if (paramBoolean)
/*      */     {
/*  448 */       Receiver localReceiver = null;
/*  449 */       Object localObject1 = null;
/*      */       try
/*      */       {
/*  453 */         Synthesizer localSynthesizer = getSynthesizer();
/*  454 */         if ((localSynthesizer instanceof ReferenceCountingDevice)) {
/*  455 */           localReceiver = ((ReferenceCountingDevice)localSynthesizer).getReceiverReferenceCounting();
/*      */         } else {
/*  457 */           localSynthesizer.open();
/*      */           try {
/*  459 */             localReceiver = localSynthesizer.getReceiver();
/*      */           }
/*      */           finally {
/*  462 */             if (localReceiver == null)
/*  463 */               localSynthesizer.close();
/*      */           }
/*      */         }
/*      */       }
/*      */       catch (MidiUnavailableException localMidiUnavailableException)
/*      */       {
/*  469 */         if ((localMidiUnavailableException instanceof MidiUnavailableException)) {
/*  470 */           localObject1 = localMidiUnavailableException;
/*      */         }
/*      */       }
/*  473 */       if (localReceiver == null) {
/*      */         try
/*      */         {
/*  476 */           localReceiver = getReceiver();
/*      */         }
/*      */         catch (Exception localException) {
/*  479 */           if ((localException instanceof MidiUnavailableException)) {
/*  480 */             localObject1 = (MidiUnavailableException)localException;
/*      */           }
/*      */         }
/*      */       }
/*  484 */       if (localReceiver != null) {
/*  485 */         localSequencer.getTransmitter().setReceiver(localReceiver);
/*  486 */         if ((localSequencer instanceof AutoConnectSequencer))
/*  487 */           ((AutoConnectSequencer)localSequencer).setAutoConnect(localReceiver);
/*      */       }
/*      */       else {
/*  490 */         if (localObject1 != null) {
/*  491 */           throw ((Throwable)localObject1);
/*      */         }
/*  493 */         throw new MidiUnavailableException("no receiver available");
/*      */       }
/*      */     }
/*  496 */     return localSequencer;
/*      */   }
/*      */ 
/*      */   public static Soundbank getSoundbank(InputStream paramInputStream)
/*      */     throws InvalidMidiDataException, IOException
/*      */   {
/*  523 */     SoundbankReader localSoundbankReader = null;
/*  524 */     Soundbank localSoundbank = null;
/*      */ 
/*  526 */     List localList = getSoundbankReaders();
/*      */ 
/*  528 */     for (int i = 0; i < localList.size(); i++) {
/*  529 */       localSoundbankReader = (SoundbankReader)localList.get(i);
/*  530 */       localSoundbank = localSoundbankReader.getSoundbank(paramInputStream);
/*      */ 
/*  532 */       if (localSoundbank != null) {
/*  533 */         return localSoundbank;
/*      */       }
/*      */     }
/*  536 */     throw new InvalidMidiDataException("cannot get soundbank from stream");
/*      */   }
/*      */ 
/*      */   public static Soundbank getSoundbank(URL paramURL)
/*      */     throws InvalidMidiDataException, IOException
/*      */   {
/*  554 */     SoundbankReader localSoundbankReader = null;
/*  555 */     Soundbank localSoundbank = null;
/*      */ 
/*  557 */     List localList = getSoundbankReaders();
/*      */ 
/*  559 */     for (int i = 0; i < localList.size(); i++) {
/*  560 */       localSoundbankReader = (SoundbankReader)localList.get(i);
/*  561 */       localSoundbank = localSoundbankReader.getSoundbank(paramURL);
/*      */ 
/*  563 */       if (localSoundbank != null) {
/*  564 */         return localSoundbank;
/*      */       }
/*      */     }
/*  567 */     throw new InvalidMidiDataException("cannot get soundbank from stream");
/*      */   }
/*      */ 
/*      */   public static Soundbank getSoundbank(File paramFile)
/*      */     throws InvalidMidiDataException, IOException
/*      */   {
/*  586 */     SoundbankReader localSoundbankReader = null;
/*  587 */     Soundbank localSoundbank = null;
/*      */ 
/*  589 */     List localList = getSoundbankReaders();
/*      */ 
/*  591 */     for (int i = 0; i < localList.size(); i++) {
/*  592 */       localSoundbankReader = (SoundbankReader)localList.get(i);
/*  593 */       localSoundbank = localSoundbankReader.getSoundbank(paramFile);
/*      */ 
/*  595 */       if (localSoundbank != null) {
/*  596 */         return localSoundbank;
/*      */       }
/*      */     }
/*  599 */     throw new InvalidMidiDataException("cannot get soundbank from stream");
/*      */   }
/*      */ 
/*      */   public static MidiFileFormat getMidiFileFormat(InputStream paramInputStream)
/*      */     throws InvalidMidiDataException, IOException
/*      */   {
/*  639 */     List localList = getMidiFileReaders();
/*  640 */     MidiFileFormat localMidiFileFormat = null;
/*      */ 
/*  642 */     for (int i = 0; i < localList.size(); i++) {
/*  643 */       MidiFileReader localMidiFileReader = (MidiFileReader)localList.get(i);
/*      */       try {
/*  645 */         localMidiFileFormat = localMidiFileReader.getMidiFileFormat(paramInputStream);
/*      */       }
/*      */       catch (InvalidMidiDataException localInvalidMidiDataException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*  652 */     if (localMidiFileFormat == null) {
/*  653 */       throw new InvalidMidiDataException("input stream is not a supported file type");
/*      */     }
/*  655 */     return localMidiFileFormat;
/*      */   }
/*      */ 
/*      */   public static MidiFileFormat getMidiFileFormat(URL paramURL)
/*      */     throws InvalidMidiDataException, IOException
/*      */   {
/*  685 */     List localList = getMidiFileReaders();
/*  686 */     MidiFileFormat localMidiFileFormat = null;
/*      */ 
/*  688 */     for (int i = 0; i < localList.size(); i++) {
/*  689 */       MidiFileReader localMidiFileReader = (MidiFileReader)localList.get(i);
/*      */       try {
/*  691 */         localMidiFileFormat = localMidiFileReader.getMidiFileFormat(paramURL);
/*      */       }
/*      */       catch (InvalidMidiDataException localInvalidMidiDataException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*  698 */     if (localMidiFileFormat == null) {
/*  699 */       throw new InvalidMidiDataException("url is not a supported file type");
/*      */     }
/*  701 */     return localMidiFileFormat;
/*      */   }
/*      */ 
/*      */   public static MidiFileFormat getMidiFileFormat(File paramFile)
/*      */     throws InvalidMidiDataException, IOException
/*      */   {
/*  731 */     List localList = getMidiFileReaders();
/*  732 */     MidiFileFormat localMidiFileFormat = null;
/*      */ 
/*  734 */     for (int i = 0; i < localList.size(); i++) {
/*  735 */       MidiFileReader localMidiFileReader = (MidiFileReader)localList.get(i);
/*      */       try {
/*  737 */         localMidiFileFormat = localMidiFileReader.getMidiFileFormat(paramFile);
/*      */       }
/*      */       catch (InvalidMidiDataException localInvalidMidiDataException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*  744 */     if (localMidiFileFormat == null) {
/*  745 */       throw new InvalidMidiDataException("file is not a supported file type");
/*      */     }
/*  747 */     return localMidiFileFormat;
/*      */   }
/*      */ 
/*      */   public static Sequence getSequence(InputStream paramInputStream)
/*      */     throws InvalidMidiDataException, IOException
/*      */   {
/*  786 */     List localList = getMidiFileReaders();
/*  787 */     Sequence localSequence = null;
/*      */ 
/*  789 */     for (int i = 0; i < localList.size(); i++) {
/*  790 */       MidiFileReader localMidiFileReader = (MidiFileReader)localList.get(i);
/*      */       try {
/*  792 */         localSequence = localMidiFileReader.getSequence(paramInputStream);
/*      */       }
/*      */       catch (InvalidMidiDataException localInvalidMidiDataException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*  799 */     if (localSequence == null) {
/*  800 */       throw new InvalidMidiDataException("could not get sequence from input stream");
/*      */     }
/*  802 */     return localSequence;
/*      */   }
/*      */ 
/*      */   public static Sequence getSequence(URL paramURL)
/*      */     throws InvalidMidiDataException, IOException
/*      */   {
/*  830 */     List localList = getMidiFileReaders();
/*  831 */     Sequence localSequence = null;
/*      */ 
/*  833 */     for (int i = 0; i < localList.size(); i++) {
/*  834 */       MidiFileReader localMidiFileReader = (MidiFileReader)localList.get(i);
/*      */       try {
/*  836 */         localSequence = localMidiFileReader.getSequence(paramURL);
/*      */       }
/*      */       catch (InvalidMidiDataException localInvalidMidiDataException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*  843 */     if (localSequence == null) {
/*  844 */       throw new InvalidMidiDataException("could not get sequence from URL");
/*      */     }
/*  846 */     return localSequence;
/*      */   }
/*      */ 
/*      */   public static Sequence getSequence(File paramFile)
/*      */     throws InvalidMidiDataException, IOException
/*      */   {
/*  874 */     List localList = getMidiFileReaders();
/*  875 */     Sequence localSequence = null;
/*      */ 
/*  877 */     for (int i = 0; i < localList.size(); i++) {
/*  878 */       MidiFileReader localMidiFileReader = (MidiFileReader)localList.get(i);
/*      */       try {
/*  880 */         localSequence = localMidiFileReader.getSequence(paramFile);
/*      */       }
/*      */       catch (InvalidMidiDataException localInvalidMidiDataException)
/*      */       {
/*      */       }
/*      */     }
/*      */ 
/*  887 */     if (localSequence == null) {
/*  888 */       throw new InvalidMidiDataException("could not get sequence from file");
/*      */     }
/*  890 */     return localSequence;
/*      */   }
/*      */ 
/*      */   public static int[] getMidiFileTypes()
/*      */   {
/*  903 */     List localList = getMidiFileWriters();
/*  904 */     HashSet localHashSet = new HashSet();
/*      */ 
/*  908 */     for (int i = 0; i < localList.size(); i++) {
/*  909 */       MidiFileWriter localMidiFileWriter = (MidiFileWriter)localList.get(i);
/*  910 */       localObject = localMidiFileWriter.getMidiFileTypes();
/*  911 */       for (int k = 0; k < localObject.length; k++) {
/*  912 */         localHashSet.add(new Integer(localObject[k]));
/*      */       }
/*      */     }
/*  915 */     int[] arrayOfInt = new int[localHashSet.size()];
/*  916 */     int j = 0;
/*  917 */     Object localObject = localHashSet.iterator();
/*  918 */     while (((Iterator)localObject).hasNext()) {
/*  919 */       Integer localInteger = (Integer)((Iterator)localObject).next();
/*  920 */       arrayOfInt[(j++)] = localInteger.intValue();
/*      */     }
/*  922 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public static boolean isFileTypeSupported(int paramInt)
/*      */   {
/*  935 */     List localList = getMidiFileWriters();
/*      */ 
/*  937 */     for (int i = 0; i < localList.size(); i++) {
/*  938 */       MidiFileWriter localMidiFileWriter = (MidiFileWriter)localList.get(i);
/*  939 */       if (localMidiFileWriter.isFileTypeSupported(paramInt)) {
/*  940 */         return true;
/*      */       }
/*      */     }
/*  943 */     return false;
/*      */   }
/*      */ 
/*      */   public static int[] getMidiFileTypes(Sequence paramSequence)
/*      */   {
/*  957 */     List localList = getMidiFileWriters();
/*  958 */     HashSet localHashSet = new HashSet();
/*      */ 
/*  962 */     for (int i = 0; i < localList.size(); i++) {
/*  963 */       MidiFileWriter localMidiFileWriter = (MidiFileWriter)localList.get(i);
/*  964 */       localObject = localMidiFileWriter.getMidiFileTypes(paramSequence);
/*  965 */       for (int k = 0; k < localObject.length; k++) {
/*  966 */         localHashSet.add(new Integer(localObject[k]));
/*      */       }
/*      */     }
/*  969 */     int[] arrayOfInt = new int[localHashSet.size()];
/*  970 */     int j = 0;
/*  971 */     Object localObject = localHashSet.iterator();
/*  972 */     while (((Iterator)localObject).hasNext()) {
/*  973 */       Integer localInteger = (Integer)((Iterator)localObject).next();
/*  974 */       arrayOfInt[(j++)] = localInteger.intValue();
/*      */     }
/*  976 */     return arrayOfInt;
/*      */   }
/*      */ 
/*      */   public static boolean isFileTypeSupported(int paramInt, Sequence paramSequence)
/*      */   {
/*  991 */     List localList = getMidiFileWriters();
/*      */ 
/*  993 */     for (int i = 0; i < localList.size(); i++) {
/*  994 */       MidiFileWriter localMidiFileWriter = (MidiFileWriter)localList.get(i);
/*  995 */       if (localMidiFileWriter.isFileTypeSupported(paramInt, paramSequence)) {
/*  996 */         return true;
/*      */       }
/*      */     }
/*  999 */     return false;
/*      */   }
/*      */ 
/*      */   public static int write(Sequence paramSequence, int paramInt, OutputStream paramOutputStream)
/*      */     throws IOException
/*      */   {
/* 1018 */     List localList = getMidiFileWriters();
/*      */ 
/* 1020 */     int i = -2;
/*      */ 
/* 1022 */     for (int j = 0; j < localList.size(); j++) {
/* 1023 */       MidiFileWriter localMidiFileWriter = (MidiFileWriter)localList.get(j);
/* 1024 */       if (localMidiFileWriter.isFileTypeSupported(paramInt, paramSequence))
/*      */       {
/* 1026 */         i = localMidiFileWriter.write(paramSequence, paramInt, paramOutputStream);
/* 1027 */         break;
/*      */       }
/*      */     }
/* 1030 */     if (i == -2) {
/* 1031 */       throw new IllegalArgumentException("MIDI file type is not supported");
/*      */     }
/* 1033 */     return i;
/*      */   }
/*      */ 
/*      */   public static int write(Sequence paramSequence, int paramInt, File paramFile)
/*      */     throws IOException
/*      */   {
/* 1052 */     List localList = getMidiFileWriters();
/*      */ 
/* 1054 */     int i = -2;
/*      */ 
/* 1056 */     for (int j = 0; j < localList.size(); j++) {
/* 1057 */       MidiFileWriter localMidiFileWriter = (MidiFileWriter)localList.get(j);
/* 1058 */       if (localMidiFileWriter.isFileTypeSupported(paramInt, paramSequence))
/*      */       {
/* 1060 */         i = localMidiFileWriter.write(paramSequence, paramInt, paramFile);
/* 1061 */         break;
/*      */       }
/*      */     }
/* 1064 */     if (i == -2) {
/* 1065 */       throw new IllegalArgumentException("MIDI file type is not supported");
/*      */     }
/* 1067 */     return i;
/*      */   }
/*      */ 
/*      */   private static List getMidiDeviceProviders()
/*      */   {
/* 1075 */     return getProviders(MidiDeviceProvider.class);
/*      */   }
/*      */ 
/*      */   private static List getSoundbankReaders()
/*      */   {
/* 1080 */     return getProviders(SoundbankReader.class);
/*      */   }
/*      */ 
/*      */   private static List getMidiFileWriters()
/*      */   {
/* 1085 */     return getProviders(MidiFileWriter.class);
/*      */   }
/*      */ 
/*      */   private static List getMidiFileReaders()
/*      */   {
/* 1090 */     return getProviders(MidiFileReader.class);
/*      */   }
/*      */ 
/*      */   private static MidiDevice getDefaultDeviceWrapper(Class paramClass)
/*      */     throws MidiUnavailableException
/*      */   {
/*      */     try
/*      */     {
/* 1110 */       return getDefaultDevice(paramClass);
/*      */     } catch (IllegalArgumentException localIllegalArgumentException) {
/* 1112 */       MidiUnavailableException localMidiUnavailableException = new MidiUnavailableException();
/* 1113 */       localMidiUnavailableException.initCause(localIllegalArgumentException);
/* 1114 */       throw localMidiUnavailableException;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static MidiDevice getDefaultDevice(Class paramClass)
/*      */   {
/* 1127 */     List localList = getMidiDeviceProviders();
/* 1128 */     String str1 = JDK13Services.getDefaultProviderClassName(paramClass);
/* 1129 */     String str2 = JDK13Services.getDefaultInstanceName(paramClass);
/*      */ 
/* 1132 */     if (str1 != null) {
/* 1133 */       MidiDeviceProvider localMidiDeviceProvider = getNamedProvider(str1, localList);
/* 1134 */       if (localMidiDeviceProvider != null) {
/* 1135 */         if (str2 != null) {
/* 1136 */           localMidiDevice = getNamedDevice(str2, localMidiDeviceProvider, paramClass);
/* 1137 */           if (localMidiDevice != null) {
/* 1138 */             return localMidiDevice;
/*      */           }
/*      */         }
/* 1141 */         localMidiDevice = getFirstDevice(localMidiDeviceProvider, paramClass);
/* 1142 */         if (localMidiDevice != null) {
/* 1143 */           return localMidiDevice;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1151 */     if (str2 != null) {
/* 1152 */       localMidiDevice = getNamedDevice(str2, localList, paramClass);
/* 1153 */       if (localMidiDevice != null) {
/* 1154 */         return localMidiDevice;
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/* 1160 */     MidiDevice localMidiDevice = getFirstDevice(localList, paramClass);
/* 1161 */     if (localMidiDevice != null) {
/* 1162 */       return localMidiDevice;
/*      */     }
/* 1164 */     throw new IllegalArgumentException("Requested device not installed");
/*      */   }
/*      */ 
/*      */   private static MidiDeviceProvider getNamedProvider(String paramString, List paramList)
/*      */   {
/* 1178 */     for (int i = 0; i < paramList.size(); i++) {
/* 1179 */       MidiDeviceProvider localMidiDeviceProvider = (MidiDeviceProvider)paramList.get(i);
/* 1180 */       if (localMidiDeviceProvider.getClass().getName().equals(paramString)) {
/* 1181 */         return localMidiDeviceProvider;
/*      */       }
/*      */     }
/* 1184 */     return null;
/*      */   }
/*      */ 
/*      */   private static MidiDevice getNamedDevice(String paramString, MidiDeviceProvider paramMidiDeviceProvider, Class paramClass)
/*      */   {
/* 1201 */     MidiDevice localMidiDevice = getNamedDevice(paramString, paramMidiDeviceProvider, paramClass, false, false);
/*      */ 
/* 1203 */     if (localMidiDevice != null) {
/* 1204 */       return localMidiDevice;
/*      */     }
/*      */ 
/* 1207 */     if (paramClass == Receiver.class)
/*      */     {
/* 1209 */       localMidiDevice = getNamedDevice(paramString, paramMidiDeviceProvider, paramClass, true, false);
/*      */ 
/* 1211 */       if (localMidiDevice != null) {
/* 1212 */         return localMidiDevice;
/*      */       }
/*      */     }
/*      */ 
/* 1216 */     return null;
/*      */   }
/*      */ 
/*      */   private static MidiDevice getNamedDevice(String paramString, MidiDeviceProvider paramMidiDeviceProvider, Class paramClass, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 1233 */     MidiDevice.Info[] arrayOfInfo = paramMidiDeviceProvider.getDeviceInfo();
/* 1234 */     for (int i = 0; i < arrayOfInfo.length; i++) {
/* 1235 */       if (arrayOfInfo[i].getName().equals(paramString)) {
/* 1236 */         MidiDevice localMidiDevice = paramMidiDeviceProvider.getDevice(arrayOfInfo[i]);
/* 1237 */         if (isAppropriateDevice(localMidiDevice, paramClass, paramBoolean1, paramBoolean2))
/*      */         {
/* 1239 */           return localMidiDevice;
/*      */         }
/*      */       }
/*      */     }
/* 1243 */     return null;
/*      */   }
/*      */ 
/*      */   private static MidiDevice getNamedDevice(String paramString, List paramList, Class paramClass)
/*      */   {
/* 1261 */     MidiDevice localMidiDevice = getNamedDevice(paramString, paramList, paramClass, false, false);
/*      */ 
/* 1263 */     if (localMidiDevice != null) {
/* 1264 */       return localMidiDevice;
/*      */     }
/*      */ 
/* 1267 */     if (paramClass == Receiver.class)
/*      */     {
/* 1269 */       localMidiDevice = getNamedDevice(paramString, paramList, paramClass, true, false);
/*      */ 
/* 1271 */       if (localMidiDevice != null) {
/* 1272 */         return localMidiDevice;
/*      */       }
/*      */     }
/*      */ 
/* 1276 */     return null;
/*      */   }
/*      */ 
/*      */   private static MidiDevice getNamedDevice(String paramString, List paramList, Class paramClass, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 1294 */     for (int i = 0; i < paramList.size(); i++) {
/* 1295 */       MidiDeviceProvider localMidiDeviceProvider = (MidiDeviceProvider)paramList.get(i);
/* 1296 */       MidiDevice localMidiDevice = getNamedDevice(paramString, localMidiDeviceProvider, paramClass, paramBoolean1, paramBoolean2);
/*      */ 
/* 1300 */       if (localMidiDevice != null) {
/* 1301 */         return localMidiDevice;
/*      */       }
/*      */     }
/* 1304 */     return null;
/*      */   }
/*      */ 
/*      */   private static MidiDevice getFirstDevice(MidiDeviceProvider paramMidiDeviceProvider, Class paramClass)
/*      */   {
/* 1319 */     MidiDevice localMidiDevice = getFirstDevice(paramMidiDeviceProvider, paramClass, false, false);
/*      */ 
/* 1321 */     if (localMidiDevice != null) {
/* 1322 */       return localMidiDevice;
/*      */     }
/*      */ 
/* 1325 */     if (paramClass == Receiver.class)
/*      */     {
/* 1327 */       localMidiDevice = getFirstDevice(paramMidiDeviceProvider, paramClass, true, false);
/*      */ 
/* 1329 */       if (localMidiDevice != null) {
/* 1330 */         return localMidiDevice;
/*      */       }
/*      */     }
/*      */ 
/* 1334 */     return null;
/*      */   }
/*      */ 
/*      */   private static MidiDevice getFirstDevice(MidiDeviceProvider paramMidiDeviceProvider, Class paramClass, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 1349 */     MidiDevice.Info[] arrayOfInfo = paramMidiDeviceProvider.getDeviceInfo();
/* 1350 */     for (int i = 0; i < arrayOfInfo.length; i++) {
/* 1351 */       MidiDevice localMidiDevice = paramMidiDeviceProvider.getDevice(arrayOfInfo[i]);
/* 1352 */       if (isAppropriateDevice(localMidiDevice, paramClass, paramBoolean1, paramBoolean2))
/*      */       {
/* 1354 */         return localMidiDevice;
/*      */       }
/*      */     }
/* 1357 */     return null;
/*      */   }
/*      */ 
/*      */   private static MidiDevice getFirstDevice(List paramList, Class paramClass)
/*      */   {
/* 1373 */     MidiDevice localMidiDevice = getFirstDevice(paramList, paramClass, false, false);
/*      */ 
/* 1375 */     if (localMidiDevice != null) {
/* 1376 */       return localMidiDevice;
/*      */     }
/*      */ 
/* 1379 */     if (paramClass == Receiver.class)
/*      */     {
/* 1381 */       localMidiDevice = getFirstDevice(paramList, paramClass, true, false);
/*      */ 
/* 1383 */       if (localMidiDevice != null) {
/* 1384 */         return localMidiDevice;
/*      */       }
/*      */     }
/*      */ 
/* 1388 */     return null;
/*      */   }
/*      */ 
/*      */   private static MidiDevice getFirstDevice(List paramList, Class paramClass, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 1404 */     for (int i = 0; i < paramList.size(); i++) {
/* 1405 */       MidiDeviceProvider localMidiDeviceProvider = (MidiDeviceProvider)paramList.get(i);
/* 1406 */       MidiDevice localMidiDevice = getFirstDevice(localMidiDeviceProvider, paramClass, paramBoolean1, paramBoolean2);
/*      */ 
/* 1409 */       if (localMidiDevice != null) {
/* 1410 */         return localMidiDevice;
/*      */       }
/*      */     }
/* 1413 */     return null;
/*      */   }
/*      */ 
/*      */   private static boolean isAppropriateDevice(MidiDevice paramMidiDevice, Class paramClass, boolean paramBoolean1, boolean paramBoolean2)
/*      */   {
/* 1442 */     if (paramClass.isInstance(paramMidiDevice))
/*      */     {
/* 1445 */       return true;
/*      */     }
/*      */ 
/* 1453 */     if (((!(paramMidiDevice instanceof Sequencer)) && (!(paramMidiDevice instanceof Synthesizer))) || (((paramMidiDevice instanceof Sequencer)) && (paramBoolean2)) || (((paramMidiDevice instanceof Synthesizer)) && (paramBoolean1)))
/*      */     {
/* 1459 */       if (((paramClass == Receiver.class) && (paramMidiDevice.getMaxReceivers() != 0)) || ((paramClass == Transmitter.class) && (paramMidiDevice.getMaxTransmitters() != 0)))
/*      */       {
/* 1463 */         return true;
/*      */       }
/*      */     }
/*      */ 
/* 1467 */     return false;
/*      */   }
/*      */ 
/*      */   private static List getProviders(Class paramClass)
/*      */   {
/* 1478 */     return JDK13Services.getProviders(paramClass);
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.midi.MidiSystem
 * JD-Core Version:    0.6.2
 */