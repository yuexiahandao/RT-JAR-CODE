/*      */ package java.awt.color;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.FileInputStream;
/*      */ import java.io.FileNotFoundException;
/*      */ import java.io.FileOutputStream;
/*      */ import java.io.IOException;
/*      */ import java.io.InputStream;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.ObjectStreamException;
/*      */ import java.io.OutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.security.AccessController;
/*      */ import java.security.PrivilegedAction;
/*      */ import java.util.StringTokenizer;
/*      */ import sun.java2d.cmm.CMSManager;
/*      */ import sun.java2d.cmm.PCMM;
/*      */ import sun.java2d.cmm.ProfileActivator;
/*      */ import sun.java2d.cmm.ProfileDeferralInfo;
/*      */ import sun.java2d.cmm.ProfileDeferralMgr;
/*      */ 
/*      */ public class ICC_Profile
/*      */   implements Serializable
/*      */ {
/*      */   private static final long serialVersionUID = -3938515861990936766L;
/*      */   transient long ID;
/*      */   private transient ProfileDeferralInfo deferralInfo;
/*      */   private transient ProfileActivator profileActivator;
/*      */   private static ICC_Profile sRGBprofile;
/*      */   private static ICC_Profile XYZprofile;
/*      */   private static ICC_Profile PYCCprofile;
/*      */   private static ICC_Profile GRAYprofile;
/*      */   private static ICC_Profile LINEAR_RGBprofile;
/*      */   public static final int CLASS_INPUT = 0;
/*      */   public static final int CLASS_DISPLAY = 1;
/*      */   public static final int CLASS_OUTPUT = 2;
/*      */   public static final int CLASS_DEVICELINK = 3;
/*      */   public static final int CLASS_COLORSPACECONVERSION = 4;
/*      */   public static final int CLASS_ABSTRACT = 5;
/*      */   public static final int CLASS_NAMEDCOLOR = 6;
/*      */   public static final int icSigXYZData = 1482250784;
/*      */   public static final int icSigLabData = 1281450528;
/*      */   public static final int icSigLuvData = 1282766368;
/*      */   public static final int icSigYCbCrData = 1497588338;
/*      */   public static final int icSigYxyData = 1501067552;
/*      */   public static final int icSigRgbData = 1380401696;
/*      */   public static final int icSigGrayData = 1196573017;
/*      */   public static final int icSigHsvData = 1213421088;
/*      */   public static final int icSigHlsData = 1212961568;
/*      */   public static final int icSigCmykData = 1129142603;
/*      */   public static final int icSigCmyData = 1129142560;
/*      */   public static final int icSigSpace2CLR = 843271250;
/*      */   public static final int icSigSpace3CLR = 860048466;
/*      */   public static final int icSigSpace4CLR = 876825682;
/*      */   public static final int icSigSpace5CLR = 893602898;
/*      */   public static final int icSigSpace6CLR = 910380114;
/*      */   public static final int icSigSpace7CLR = 927157330;
/*      */   public static final int icSigSpace8CLR = 943934546;
/*      */   public static final int icSigSpace9CLR = 960711762;
/*      */   public static final int icSigSpaceACLR = 1094929490;
/*      */   public static final int icSigSpaceBCLR = 1111706706;
/*      */   public static final int icSigSpaceCCLR = 1128483922;
/*      */   public static final int icSigSpaceDCLR = 1145261138;
/*      */   public static final int icSigSpaceECLR = 1162038354;
/*      */   public static final int icSigSpaceFCLR = 1178815570;
/*      */   public static final int icSigInputClass = 1935896178;
/*      */   public static final int icSigDisplayClass = 1835955314;
/*      */   public static final int icSigOutputClass = 1886549106;
/*      */   public static final int icSigLinkClass = 1818848875;
/*      */   public static final int icSigAbstractClass = 1633842036;
/*      */   public static final int icSigColorSpaceClass = 1936744803;
/*      */   public static final int icSigNamedColorClass = 1852662636;
/*      */   public static final int icPerceptual = 0;
/*      */   public static final int icRelativeColorimetric = 1;
/*      */   public static final int icMediaRelativeColorimetric = 1;
/*      */   public static final int icSaturation = 2;
/*      */   public static final int icAbsoluteColorimetric = 3;
/*      */   public static final int icICCAbsoluteColorimetric = 3;
/*      */   public static final int icSigHead = 1751474532;
/*      */   public static final int icSigAToB0Tag = 1093812784;
/*      */   public static final int icSigAToB1Tag = 1093812785;
/*      */   public static final int icSigAToB2Tag = 1093812786;
/*      */   public static final int icSigBlueColorantTag = 1649957210;
/*      */   public static final int icSigBlueMatrixColumnTag = 1649957210;
/*      */   public static final int icSigBlueTRCTag = 1649693251;
/*      */   public static final int icSigBToA0Tag = 1110589744;
/*      */   public static final int icSigBToA1Tag = 1110589745;
/*      */   public static final int icSigBToA2Tag = 1110589746;
/*      */   public static final int icSigCalibrationDateTimeTag = 1667329140;
/*      */   public static final int icSigCharTargetTag = 1952543335;
/*      */   public static final int icSigCopyrightTag = 1668313716;
/*      */   public static final int icSigCrdInfoTag = 1668441193;
/*      */   public static final int icSigDeviceMfgDescTag = 1684893284;
/*      */   public static final int icSigDeviceModelDescTag = 1684890724;
/*      */   public static final int icSigDeviceSettingsTag = 1684371059;
/*      */   public static final int icSigGamutTag = 1734438260;
/*      */   public static final int icSigGrayTRCTag = 1800688195;
/*      */   public static final int icSigGreenColorantTag = 1733843290;
/*      */   public static final int icSigGreenMatrixColumnTag = 1733843290;
/*      */   public static final int icSigGreenTRCTag = 1733579331;
/*      */   public static final int icSigLuminanceTag = 1819635049;
/*      */   public static final int icSigMeasurementTag = 1835360627;
/*      */   public static final int icSigMediaBlackPointTag = 1651208308;
/*      */   public static final int icSigMediaWhitePointTag = 2004119668;
/*      */   public static final int icSigNamedColor2Tag = 1852009522;
/*      */   public static final int icSigOutputResponseTag = 1919251312;
/*      */   public static final int icSigPreview0Tag = 1886545200;
/*      */   public static final int icSigPreview1Tag = 1886545201;
/*      */   public static final int icSigPreview2Tag = 1886545202;
/*      */   public static final int icSigProfileDescriptionTag = 1684370275;
/*      */   public static final int icSigProfileSequenceDescTag = 1886610801;
/*      */   public static final int icSigPs2CRD0Tag = 1886610480;
/*      */   public static final int icSigPs2CRD1Tag = 1886610481;
/*      */   public static final int icSigPs2CRD2Tag = 1886610482;
/*      */   public static final int icSigPs2CRD3Tag = 1886610483;
/*      */   public static final int icSigPs2CSATag = 1886597747;
/*      */   public static final int icSigPs2RenderingIntentTag = 1886597737;
/*      */   public static final int icSigRedColorantTag = 1918392666;
/*      */   public static final int icSigRedMatrixColumnTag = 1918392666;
/*      */   public static final int icSigRedTRCTag = 1918128707;
/*      */   public static final int icSigScreeningDescTag = 1935897188;
/*      */   public static final int icSigScreeningTag = 1935897198;
/*      */   public static final int icSigTechnologyTag = 1952801640;
/*      */   public static final int icSigUcrBgTag = 1650877472;
/*      */   public static final int icSigViewingCondDescTag = 1987405156;
/*      */   public static final int icSigViewingConditionsTag = 1986618743;
/*      */   public static final int icSigChromaticityTag = 1667789421;
/*      */   public static final int icSigChromaticAdaptationTag = 1667785060;
/*      */   public static final int icSigColorantOrderTag = 1668051567;
/*      */   public static final int icSigColorantTableTag = 1668051572;
/*      */   public static final int icHdrSize = 0;
/*      */   public static final int icHdrCmmId = 4;
/*      */   public static final int icHdrVersion = 8;
/*      */   public static final int icHdrDeviceClass = 12;
/*      */   public static final int icHdrColorSpace = 16;
/*      */   public static final int icHdrPcs = 20;
/*      */   public static final int icHdrDate = 24;
/*      */   public static final int icHdrMagic = 36;
/*      */   public static final int icHdrPlatform = 40;
/*      */   public static final int icHdrFlags = 44;
/*      */   public static final int icHdrManufacturer = 48;
/*      */   public static final int icHdrModel = 52;
/*      */   public static final int icHdrAttributes = 56;
/*      */   public static final int icHdrRenderingIntent = 64;
/*      */   public static final int icHdrIlluminant = 68;
/*      */   public static final int icHdrCreator = 80;
/*      */   public static final int icHdrProfileID = 84;
/*      */   public static final int icTagType = 0;
/*      */   public static final int icTagReserved = 4;
/*      */   public static final int icCurveCount = 8;
/*      */   public static final int icCurveData = 12;
/*      */   public static final int icXYZNumberX = 8;
/* 1933 */   private int iccProfileSerializedDataVersion = 1;
/*      */   private transient ICC_Profile resolvedDeserializedProfile;
/*      */ 
/*      */   ICC_Profile(long paramLong)
/*      */   {
/*  730 */     this.ID = paramLong;
/*      */   }
/*      */ 
/*      */   ICC_Profile(ProfileDeferralInfo paramProfileDeferralInfo)
/*      */   {
/*  739 */     this.deferralInfo = paramProfileDeferralInfo;
/*  740 */     this.profileActivator = new ProfileActivator() {
/*      */       public void activate() throws ProfileDataException {
/*  742 */         ICC_Profile.this.activateDeferredProfile();
/*      */       }
/*      */     };
/*  745 */     ProfileDeferralMgr.registerDeferral(this.profileActivator);
/*      */   }
/*      */ 
/*      */   protected void finalize()
/*      */   {
/*  753 */     if (this.ID != 0L)
/*  754 */       CMSManager.getModule().freeProfile(this.ID);
/*  755 */     else if (this.profileActivator != null)
/*  756 */       ProfileDeferralMgr.unregisterDeferral(this.profileActivator);
/*      */   }
/*      */ 
/*      */   public static ICC_Profile getInstance(byte[] paramArrayOfByte)
/*      */   {
/*  774 */     if (ProfileDeferralMgr.deferring)
/*  775 */       ProfileDeferralMgr.activateProfiles();
/*      */     long l;
/*      */     try
/*      */     {
/*  779 */       l = CMSManager.getModule().loadProfile(paramArrayOfByte);
/*      */     } catch (CMMException localCMMException1) {
/*  781 */       throw new IllegalArgumentException("Invalid ICC Profile Data");
/*      */     }
/*      */     Object localObject;
/*      */     try {
/*  785 */       if ((getColorSpaceType(l) == 6) && (getData(l, 2004119668) != null) && (getData(l, 1800688195) != null))
/*      */       {
/*  788 */         localObject = new ICC_ProfileGray(l);
/*      */       }
/*  790 */       else if ((getColorSpaceType(l) == 5) && (getData(l, 2004119668) != null) && (getData(l, 1918392666) != null) && (getData(l, 1733843290) != null) && (getData(l, 1649957210) != null) && (getData(l, 1918128707) != null) && (getData(l, 1733579331) != null) && (getData(l, 1649693251) != null))
/*      */       {
/*  798 */         localObject = new ICC_ProfileRGB(l);
/*      */       }
/*      */       else
/*  801 */         localObject = new ICC_Profile(l);
/*      */     }
/*      */     catch (CMMException localCMMException2) {
/*  804 */       localObject = new ICC_Profile(l);
/*      */     }
/*  806 */     return localObject;
/*      */   }
/*      */ 
/*      */   public static ICC_Profile getInstance(int paramInt)
/*      */   {
/*  827 */     ICC_Profile localICC_Profile = null;
/*      */     ProfileDeferralInfo localProfileDeferralInfo;
/*  830 */     switch (paramInt) {
/*      */     case 1000:
/*  832 */       synchronized (ICC_Profile.class) {
/*  833 */         if (sRGBprofile == null)
/*      */         {
/*  839 */           localProfileDeferralInfo = new ProfileDeferralInfo("sRGB.pf", 5, 3, 1);
/*      */ 
/*  843 */           sRGBprofile = getDeferredInstance(localProfileDeferralInfo);
/*      */         }
/*  845 */         localICC_Profile = sRGBprofile;
/*      */       }
/*      */ 
/*  848 */       break;
/*      */     case 1001:
/*  851 */       synchronized (ICC_Profile.class) {
/*  852 */         if (XYZprofile == null) {
/*  853 */           localProfileDeferralInfo = new ProfileDeferralInfo("CIEXYZ.pf", 0, 3, 1);
/*      */ 
/*  857 */           XYZprofile = getDeferredInstance(localProfileDeferralInfo);
/*      */         }
/*  859 */         localICC_Profile = XYZprofile;
/*      */       }
/*      */ 
/*  862 */       break;
/*      */     case 1002:
/*  865 */       synchronized (ICC_Profile.class) {
/*  866 */         if (PYCCprofile == null) {
/*  867 */           if (standardProfileExists("PYCC.pf"))
/*      */           {
/*  869 */             localProfileDeferralInfo = new ProfileDeferralInfo("PYCC.pf", 13, 3, 1);
/*      */ 
/*  873 */             PYCCprofile = getDeferredInstance(localProfileDeferralInfo);
/*      */           } else {
/*  875 */             throw new IllegalArgumentException("Can't load standard profile: PYCC.pf");
/*      */           }
/*      */         }
/*      */ 
/*  879 */         localICC_Profile = PYCCprofile;
/*      */       }
/*      */ 
/*  882 */       break;
/*      */     case 1003:
/*  885 */       synchronized (ICC_Profile.class) {
/*  886 */         if (GRAYprofile == null) {
/*  887 */           localProfileDeferralInfo = new ProfileDeferralInfo("GRAY.pf", 6, 1, 1);
/*      */ 
/*  891 */           GRAYprofile = getDeferredInstance(localProfileDeferralInfo);
/*      */         }
/*  893 */         localICC_Profile = GRAYprofile;
/*      */       }
/*      */ 
/*  896 */       break;
/*      */     case 1004:
/*  899 */       synchronized (ICC_Profile.class) {
/*  900 */         if (LINEAR_RGBprofile == null) {
/*  901 */           localProfileDeferralInfo = new ProfileDeferralInfo("LINEAR_RGB.pf", 5, 3, 1);
/*      */ 
/*  905 */           LINEAR_RGBprofile = getDeferredInstance(localProfileDeferralInfo);
/*      */         }
/*  907 */         localICC_Profile = LINEAR_RGBprofile;
/*      */       }
/*      */ 
/*  910 */       break;
/*      */     default:
/*  913 */       throw new IllegalArgumentException("Unknown color space");
/*      */     }
/*      */ 
/*  916 */     return localICC_Profile;
/*      */   }
/*      */ 
/*      */   private static ICC_Profile getStandardProfile(String paramString)
/*      */   {
/*  924 */     return (ICC_Profile)AccessController.doPrivileged(new PrivilegedAction()
/*      */     {
/*      */       public Object run() {
/*  927 */         ICC_Profile localICC_Profile = null;
/*      */         try {
/*  929 */           localICC_Profile = ICC_Profile.getInstance(this.val$name);
/*      */         } catch (IOException localIOException) {
/*  931 */           throw new IllegalArgumentException("Can't load standard profile: " + this.val$name);
/*      */         }
/*      */ 
/*  934 */         return localICC_Profile;
/*      */       }
/*      */     });
/*      */   }
/*      */ 
/*      */   public static ICC_Profile getInstance(String paramString)
/*      */     throws IOException
/*      */   {
/*  967 */     FileInputStream localFileInputStream = null;
/*      */ 
/*  970 */     File localFile = getProfileFile(paramString);
/*  971 */     if (localFile != null) {
/*  972 */       localFileInputStream = new FileInputStream(localFile);
/*      */     }
/*  974 */     if (localFileInputStream == null) {
/*  975 */       throw new IOException("Cannot open file " + paramString);
/*      */     }
/*      */ 
/*  978 */     ICC_Profile localICC_Profile = getInstance(localFileInputStream);
/*      */ 
/*  980 */     localFileInputStream.close();
/*      */ 
/*  982 */     return localICC_Profile;
/*      */   }
/*      */ 
/*      */   public static ICC_Profile getInstance(InputStream paramInputStream)
/*      */     throws IOException
/*      */   {
/* 1004 */     if ((paramInputStream instanceof ProfileDeferralInfo))
/*      */     {
/* 1006 */       return getDeferredInstance((ProfileDeferralInfo)paramInputStream);
/*      */     }
/*      */     byte[] arrayOfByte;
/* 1009 */     if ((arrayOfByte = getProfileDataFromStream(paramInputStream)) == null) {
/* 1010 */       throw new IllegalArgumentException("Invalid ICC Profile Data");
/*      */     }
/*      */ 
/* 1013 */     return getInstance(arrayOfByte);
/*      */   }
/*      */ 
/*      */   static byte[] getProfileDataFromStream(InputStream paramInputStream)
/*      */     throws IOException
/*      */   {
/* 1021 */     byte[] arrayOfByte2 = new byte[''];
/* 1022 */     int j = 128;
/* 1023 */     int k = 0;
/*      */     int m;
/* 1026 */     while (j != 0) {
/* 1027 */       if ((m = paramInputStream.read(arrayOfByte2, k, j)) < 0) {
/* 1028 */         return null;
/*      */       }
/* 1030 */       k += m;
/* 1031 */       j -= m;
/*      */     }
/* 1033 */     if ((arrayOfByte2[36] != 97) || (arrayOfByte2[37] != 99) || (arrayOfByte2[38] != 115) || (arrayOfByte2[39] != 112))
/*      */     {
/* 1035 */       return null;
/*      */     }
/* 1037 */     int i = (arrayOfByte2[0] & 0xFF) << 24 | (arrayOfByte2[1] & 0xFF) << 16 | (arrayOfByte2[2] & 0xFF) << 8 | arrayOfByte2[3] & 0xFF;
/*      */ 
/* 1041 */     byte[] arrayOfByte1 = new byte[i];
/* 1042 */     System.arraycopy(arrayOfByte2, 0, arrayOfByte1, 0, 128);
/* 1043 */     j = i - 128;
/* 1044 */     k = 128;
/* 1045 */     while (j != 0) {
/* 1046 */       if ((m = paramInputStream.read(arrayOfByte1, k, j)) < 0) {
/* 1047 */         return null;
/*      */       }
/* 1049 */       k += m;
/* 1050 */       j -= m;
/*      */     }
/*      */ 
/* 1053 */     return arrayOfByte1;
/*      */   }
/*      */ 
/*      */   static ICC_Profile getDeferredInstance(ProfileDeferralInfo paramProfileDeferralInfo)
/*      */   {
/* 1070 */     if (!ProfileDeferralMgr.deferring) {
/* 1071 */       return getStandardProfile(paramProfileDeferralInfo.filename);
/*      */     }
/* 1073 */     if (paramProfileDeferralInfo.colorSpaceType == 5)
/* 1074 */       return new ICC_ProfileRGB(paramProfileDeferralInfo);
/* 1075 */     if (paramProfileDeferralInfo.colorSpaceType == 6) {
/* 1076 */       return new ICC_ProfileGray(paramProfileDeferralInfo);
/*      */     }
/* 1078 */     return new ICC_Profile(paramProfileDeferralInfo);
/*      */   }
/*      */ 
/*      */   void activateDeferredProfile()
/*      */     throws ProfileDataException
/*      */   {
/* 1086 */     final String str = this.deferralInfo.filename;
/*      */ 
/* 1088 */     this.profileActivator = null;
/* 1089 */     this.deferralInfo = null;
/* 1090 */     PrivilegedAction local3 = new PrivilegedAction() {
/*      */       public FileInputStream run() {
/* 1092 */         File localFile = ICC_Profile.getStandardProfileFile(str);
/* 1093 */         if (localFile != null)
/*      */           try {
/* 1095 */             return new FileInputStream(localFile);
/*      */           } catch (FileNotFoundException localFileNotFoundException) {
/*      */           }
/* 1098 */         return null;
/*      */       }
/*      */     };
/*      */     FileInputStream localFileInputStream;
/* 1101 */     if ((localFileInputStream = (FileInputStream)AccessController.doPrivileged(local3)) == null)
/* 1102 */       throw new ProfileDataException("Cannot open file " + str); byte[] arrayOfByte;
/*      */     ProfileDataException localProfileDataException;
/*      */     try { arrayOfByte = getProfileDataFromStream(localFileInputStream);
/* 1106 */       localFileInputStream.close();
/*      */     } catch (IOException localIOException)
/*      */     {
/* 1109 */       localProfileDataException = new ProfileDataException("Invalid ICC Profile Data" + str);
/*      */ 
/* 1111 */       localProfileDataException.initCause(localIOException);
/* 1112 */       throw localProfileDataException;
/*      */     }
/* 1114 */     if (arrayOfByte == null) {
/* 1115 */       throw new ProfileDataException("Invalid ICC Profile Data" + str);
/*      */     }
/*      */     try
/*      */     {
/* 1119 */       this.ID = CMSManager.getModule().loadProfile(arrayOfByte);
/*      */     } catch (CMMException localCMMException) {
/* 1121 */       localProfileDataException = new ProfileDataException("Invalid ICC Profile Data" + str);
/*      */ 
/* 1123 */       localProfileDataException.initCause(localCMMException);
/* 1124 */       throw localProfileDataException;
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getMajorVersion()
/*      */   {
/* 1136 */     byte[] arrayOfByte = getData(1751474532);
/*      */ 
/* 1139 */     return arrayOfByte[8];
/*      */   }
/*      */ 
/*      */   public int getMinorVersion()
/*      */   {
/* 1149 */     byte[] arrayOfByte = getData(1751474532);
/*      */ 
/* 1152 */     return arrayOfByte[9];
/*      */   }
/*      */ 
/*      */   public int getProfileClass()
/*      */   {
/* 1163 */     if (this.deferralInfo != null) {
/* 1164 */       return this.deferralInfo.profileClass;
/*      */     }
/*      */ 
/* 1170 */     byte[] arrayOfByte = getData(1751474532);
/*      */ 
/* 1172 */     int i = intFromBigEndian(arrayOfByte, 12);
/*      */     int j;
/* 1174 */     switch (i) {
/*      */     case 1935896178:
/* 1176 */       j = 0;
/* 1177 */       break;
/*      */     case 1835955314:
/* 1180 */       j = 1;
/* 1181 */       break;
/*      */     case 1886549106:
/* 1184 */       j = 2;
/* 1185 */       break;
/*      */     case 1818848875:
/* 1188 */       j = 3;
/* 1189 */       break;
/*      */     case 1936744803:
/* 1192 */       j = 4;
/* 1193 */       break;
/*      */     case 1633842036:
/* 1196 */       j = 5;
/* 1197 */       break;
/*      */     case 1852662636:
/* 1200 */       j = 6;
/* 1201 */       break;
/*      */     default:
/* 1204 */       throw new IllegalArgumentException("Unknown profile class");
/*      */     }
/*      */ 
/* 1207 */     return j;
/*      */   }
/*      */ 
/*      */   public int getColorSpaceType()
/*      */   {
/* 1223 */     if (this.deferralInfo != null) {
/* 1224 */       return this.deferralInfo.colorSpaceType;
/*      */     }
/*      */ 
/* 1229 */     return getColorSpaceType(this.ID);
/*      */   }
/*      */ 
/*      */   static int getColorSpaceType(long paramLong)
/*      */   {
/* 1236 */     byte[] arrayOfByte = getData(paramLong, 1751474532);
/* 1237 */     int i = intFromBigEndian(arrayOfByte, 16);
/* 1238 */     int j = iccCStoJCS(i);
/* 1239 */     return j;
/*      */   }
/*      */ 
/*      */   public int getPCSType()
/*      */   {
/* 1255 */     if (ProfileDeferralMgr.deferring) {
/* 1256 */       ProfileDeferralMgr.activateProfiles();
/*      */     }
/* 1258 */     return getPCSType(this.ID);
/*      */   }
/*      */ 
/*      */   static int getPCSType(long paramLong)
/*      */   {
/* 1266 */     byte[] arrayOfByte = getData(paramLong, 1751474532);
/* 1267 */     int i = intFromBigEndian(arrayOfByte, 20);
/* 1268 */     int j = iccCStoJCS(i);
/* 1269 */     return j;
/*      */   }
/*      */ 
/*      */   public void write(String paramString)
/*      */     throws IOException
/*      */   {
/* 1285 */     byte[] arrayOfByte = getData();
/*      */ 
/* 1287 */     FileOutputStream localFileOutputStream = new FileOutputStream(paramString);
/* 1288 */     localFileOutputStream.write(arrayOfByte);
/* 1289 */     localFileOutputStream.close();
/*      */   }
/*      */ 
/*      */   public void write(OutputStream paramOutputStream)
/*      */     throws IOException
/*      */   {
/* 1304 */     byte[] arrayOfByte = getData();
/*      */ 
/* 1306 */     paramOutputStream.write(arrayOfByte);
/*      */   }
/*      */ 
/*      */   public byte[] getData()
/*      */   {
/* 1319 */     if (ProfileDeferralMgr.deferring) {
/* 1320 */       ProfileDeferralMgr.activateProfiles();
/*      */     }
/*      */ 
/* 1323 */     PCMM localPCMM = CMSManager.getModule();
/*      */ 
/* 1326 */     int i = localPCMM.getProfileSize(this.ID);
/*      */ 
/* 1328 */     byte[] arrayOfByte = new byte[i];
/*      */ 
/* 1331 */     localPCMM.getProfileData(this.ID, arrayOfByte);
/*      */ 
/* 1333 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public byte[] getData(int paramInt)
/*      */   {
/* 1354 */     if (ProfileDeferralMgr.deferring) {
/* 1355 */       ProfileDeferralMgr.activateProfiles();
/*      */     }
/*      */ 
/* 1358 */     return getData(this.ID, paramInt);
/*      */   }
/*      */ 
/*      */   static byte[] getData(long paramLong, int paramInt)
/*      */   {
/*      */     byte[] arrayOfByte;
/*      */     try
/*      */     {
/* 1367 */       PCMM localPCMM = CMSManager.getModule();
/*      */ 
/* 1370 */       int i = localPCMM.getTagSize(paramLong, paramInt);
/*      */ 
/* 1372 */       arrayOfByte = new byte[i];
/*      */ 
/* 1375 */       localPCMM.getTagData(paramLong, paramInt, arrayOfByte);
/*      */     } catch (CMMException localCMMException) {
/* 1377 */       arrayOfByte = null;
/*      */     }
/*      */ 
/* 1380 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public void setData(int paramInt, byte[] paramArrayOfByte)
/*      */   {
/* 1402 */     if (ProfileDeferralMgr.deferring) {
/* 1403 */       ProfileDeferralMgr.activateProfiles();
/*      */     }
/*      */ 
/* 1406 */     CMSManager.getModule().setTagData(this.ID, paramInt, paramArrayOfByte);
/*      */   }
/*      */ 
/*      */   void setRenderingIntent(int paramInt)
/*      */   {
/* 1415 */     byte[] arrayOfByte = getData(1751474532);
/*      */ 
/* 1417 */     intToBigEndian(paramInt, arrayOfByte, 64);
/*      */ 
/* 1419 */     setData(1751474532, arrayOfByte);
/*      */   }
/*      */ 
/*      */   int getRenderingIntent()
/*      */   {
/* 1430 */     byte[] arrayOfByte = getData(1751474532);
/*      */ 
/* 1433 */     int i = intFromBigEndian(arrayOfByte, 64);
/*      */ 
/* 1435 */     return i;
/*      */   }
/*      */ 
/*      */   public int getNumComponents()
/*      */   {
/* 1454 */     if (this.deferralInfo != null) {
/* 1455 */       return this.deferralInfo.numComponents;
/*      */     }
/*      */ 
/* 1460 */     byte[] arrayOfByte = getData(1751474532);
/*      */ 
/* 1462 */     int i = intFromBigEndian(arrayOfByte, 16);
/*      */     int j;
/* 1464 */     switch (i) {
/*      */     case 1196573017:
/* 1466 */       j = 1;
/* 1467 */       break;
/*      */     case 843271250:
/* 1470 */       j = 2;
/* 1471 */       break;
/*      */     case 860048466:
/*      */     case 1129142560:
/*      */     case 1212961568:
/*      */     case 1213421088:
/*      */     case 1281450528:
/*      */     case 1282766368:
/*      */     case 1380401696:
/*      */     case 1482250784:
/*      */     case 1497588338:
/*      */     case 1501067552:
/* 1483 */       j = 3;
/* 1484 */       break;
/*      */     case 876825682:
/*      */     case 1129142603:
/* 1488 */       j = 4;
/* 1489 */       break;
/*      */     case 893602898:
/* 1492 */       j = 5;
/* 1493 */       break;
/*      */     case 910380114:
/* 1496 */       j = 6;
/* 1497 */       break;
/*      */     case 927157330:
/* 1500 */       j = 7;
/* 1501 */       break;
/*      */     case 943934546:
/* 1504 */       j = 8;
/* 1505 */       break;
/*      */     case 960711762:
/* 1508 */       j = 9;
/* 1509 */       break;
/*      */     case 1094929490:
/* 1512 */       j = 10;
/* 1513 */       break;
/*      */     case 1111706706:
/* 1516 */       j = 11;
/* 1517 */       break;
/*      */     case 1128483922:
/* 1520 */       j = 12;
/* 1521 */       break;
/*      */     case 1145261138:
/* 1524 */       j = 13;
/* 1525 */       break;
/*      */     case 1162038354:
/* 1528 */       j = 14;
/* 1529 */       break;
/*      */     case 1178815570:
/* 1532 */       j = 15;
/* 1533 */       break;
/*      */     default:
/* 1536 */       throw new ProfileDataException("invalid ICC color space");
/*      */     }
/*      */ 
/* 1539 */     return j;
/*      */   }
/*      */ 
/*      */   float[] getMediaWhitePoint()
/*      */   {
/* 1548 */     return getXYZTag(2004119668);
/*      */   }
/*      */ 
/*      */   float[] getXYZTag(int paramInt)
/*      */   {
/* 1562 */     byte[] arrayOfByte = getData(paramInt);
/*      */ 
/* 1566 */     float[] arrayOfFloat = new float[3];
/*      */ 
/* 1569 */     int i = 0; for (int j = 8; i < 3; j += 4) {
/* 1570 */       int k = intFromBigEndian(arrayOfByte, j);
/* 1571 */       arrayOfFloat[i] = (k / 65536.0F);
/*      */ 
/* 1569 */       i++;
/*      */     }
/*      */ 
/* 1573 */     return arrayOfFloat;
/*      */   }
/*      */ 
/*      */   float getGamma(int paramInt)
/*      */   {
/* 1593 */     byte[] arrayOfByte = getData(paramInt);
/*      */ 
/* 1597 */     if (intFromBigEndian(arrayOfByte, 8) != 1) {
/* 1598 */       throw new ProfileDataException("TRC is not a gamma");
/*      */     }
/*      */ 
/* 1602 */     int i = shortFromBigEndian(arrayOfByte, 12) & 0xFFFF;
/*      */ 
/* 1604 */     float f = i / 256.0F;
/*      */ 
/* 1606 */     return f;
/*      */   }
/*      */ 
/*      */   short[] getTRC(int paramInt)
/*      */   {
/* 1635 */     byte[] arrayOfByte = getData(paramInt);
/*      */ 
/* 1639 */     int k = intFromBigEndian(arrayOfByte, 8);
/*      */ 
/* 1641 */     if (k == 1) {
/* 1642 */       throw new ProfileDataException("TRC is not a table");
/*      */     }
/*      */ 
/* 1646 */     short[] arrayOfShort = new short[k];
/*      */ 
/* 1648 */     int i = 0; for (int j = 12; i < k; j += 2) {
/* 1649 */       arrayOfShort[i] = shortFromBigEndian(arrayOfByte, j);
/*      */ 
/* 1648 */       i++;
/*      */     }
/*      */ 
/* 1652 */     return arrayOfShort;
/*      */   }
/*      */ 
/*      */   static int iccCStoJCS(int paramInt)
/*      */   {
/*      */     int i;
/* 1660 */     switch (paramInt) {
/*      */     case 1482250784:
/* 1662 */       i = 0;
/* 1663 */       break;
/*      */     case 1281450528:
/* 1666 */       i = 1;
/* 1667 */       break;
/*      */     case 1282766368:
/* 1670 */       i = 2;
/* 1671 */       break;
/*      */     case 1497588338:
/* 1674 */       i = 3;
/* 1675 */       break;
/*      */     case 1501067552:
/* 1678 */       i = 4;
/* 1679 */       break;
/*      */     case 1380401696:
/* 1682 */       i = 5;
/* 1683 */       break;
/*      */     case 1196573017:
/* 1686 */       i = 6;
/* 1687 */       break;
/*      */     case 1213421088:
/* 1690 */       i = 7;
/* 1691 */       break;
/*      */     case 1212961568:
/* 1694 */       i = 8;
/* 1695 */       break;
/*      */     case 1129142603:
/* 1698 */       i = 9;
/* 1699 */       break;
/*      */     case 1129142560:
/* 1702 */       i = 11;
/* 1703 */       break;
/*      */     case 843271250:
/* 1706 */       i = 12;
/* 1707 */       break;
/*      */     case 860048466:
/* 1710 */       i = 13;
/* 1711 */       break;
/*      */     case 876825682:
/* 1714 */       i = 14;
/* 1715 */       break;
/*      */     case 893602898:
/* 1718 */       i = 15;
/* 1719 */       break;
/*      */     case 910380114:
/* 1722 */       i = 16;
/* 1723 */       break;
/*      */     case 927157330:
/* 1726 */       i = 17;
/* 1727 */       break;
/*      */     case 943934546:
/* 1730 */       i = 18;
/* 1731 */       break;
/*      */     case 960711762:
/* 1734 */       i = 19;
/* 1735 */       break;
/*      */     case 1094929490:
/* 1738 */       i = 20;
/* 1739 */       break;
/*      */     case 1111706706:
/* 1742 */       i = 21;
/* 1743 */       break;
/*      */     case 1128483922:
/* 1746 */       i = 22;
/* 1747 */       break;
/*      */     case 1145261138:
/* 1750 */       i = 23;
/* 1751 */       break;
/*      */     case 1162038354:
/* 1754 */       i = 24;
/* 1755 */       break;
/*      */     case 1178815570:
/* 1758 */       i = 25;
/* 1759 */       break;
/*      */     default:
/* 1762 */       throw new IllegalArgumentException("Unknown color space");
/*      */     }
/*      */ 
/* 1765 */     return i;
/*      */   }
/*      */ 
/*      */   static int intFromBigEndian(byte[] paramArrayOfByte, int paramInt)
/*      */   {
/* 1770 */     return (paramArrayOfByte[paramInt] & 0xFF) << 24 | (paramArrayOfByte[(paramInt + 1)] & 0xFF) << 16 | (paramArrayOfByte[(paramInt + 2)] & 0xFF) << 8 | paramArrayOfByte[(paramInt + 3)] & 0xFF;
/*      */   }
/*      */ 
/*      */   static void intToBigEndian(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*      */   {
/* 1778 */     paramArrayOfByte[paramInt2] = ((byte)(paramInt1 >> 24));
/* 1779 */     paramArrayOfByte[(paramInt2 + 1)] = ((byte)(paramInt1 >> 16));
/* 1780 */     paramArrayOfByte[(paramInt2 + 2)] = ((byte)(paramInt1 >> 8));
/* 1781 */     paramArrayOfByte[(paramInt2 + 3)] = ((byte)paramInt1);
/*      */   }
/*      */ 
/*      */   static short shortFromBigEndian(byte[] paramArrayOfByte, int paramInt)
/*      */   {
/* 1786 */     return (short)((paramArrayOfByte[paramInt] & 0xFF) << 8 | paramArrayOfByte[(paramInt + 1)] & 0xFF);
/*      */   }
/*      */ 
/*      */   static void shortToBigEndian(short paramShort, byte[] paramArrayOfByte, int paramInt)
/*      */   {
/* 1792 */     paramArrayOfByte[paramInt] = ((byte)(paramShort >> 8));
/* 1793 */     paramArrayOfByte[(paramInt + 1)] = ((byte)paramShort);
/*      */   }
/*      */ 
/*      */   private static File getProfileFile(String paramString)
/*      */   {
/* 1809 */     File localFile = new File(paramString);
/* 1810 */     if (localFile.isAbsolute())
/*      */     {
/* 1813 */       return localFile.isFile() ? localFile : null;
/*      */     }
/*      */     String str1;
/*      */     StringTokenizer localStringTokenizer;
/*      */     String str2;
/*      */     String str3;
/* 1815 */     if ((!localFile.isFile()) && ((str1 = System.getProperty("java.iccprofile.path")) != null))
/*      */     {
/* 1818 */       localStringTokenizer = new StringTokenizer(str1, File.pathSeparator);
/*      */ 
/* 1820 */       while ((localStringTokenizer.hasMoreTokens()) && ((localFile == null) || (!localFile.isFile()))) {
/* 1821 */         str2 = localStringTokenizer.nextToken();
/* 1822 */         str3 = str2 + File.separatorChar + paramString;
/* 1823 */         localFile = new File(str3);
/* 1824 */         if (!isChildOf(localFile, str2)) {
/* 1825 */           localFile = null;
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1830 */     if (((localFile == null) || (!localFile.isFile())) && ((str1 = System.getProperty("java.class.path")) != null))
/*      */     {
/* 1833 */       localStringTokenizer = new StringTokenizer(str1, File.pathSeparator);
/*      */ 
/* 1835 */       while ((localStringTokenizer.hasMoreTokens()) && ((localFile == null) || (!localFile.isFile()))) {
/* 1836 */         str2 = localStringTokenizer.nextToken();
/* 1837 */         str3 = str2 + File.separatorChar + paramString;
/* 1838 */         localFile = new File(str3);
/*      */       }
/*      */     }
/*      */ 
/* 1842 */     if ((localFile == null) || (!localFile.isFile()))
/*      */     {
/* 1844 */       localFile = getStandardProfileFile(paramString);
/*      */     }
/* 1846 */     if ((localFile != null) && (localFile.isFile())) {
/* 1847 */       return localFile;
/*      */     }
/* 1849 */     return null;
/*      */   }
/*      */ 
/*      */   private static File getStandardProfileFile(String paramString)
/*      */   {
/* 1859 */     String str1 = System.getProperty("java.home") + File.separatorChar + "lib" + File.separatorChar + "cmm";
/*      */ 
/* 1861 */     String str2 = str1 + File.separatorChar + paramString;
/* 1862 */     File localFile = new File(str2);
/* 1863 */     return (localFile.isFile()) && (isChildOf(localFile, str1)) ? localFile : null;
/*      */   }
/*      */ 
/*      */   private static boolean isChildOf(File paramFile, String paramString)
/*      */   {
/*      */     try
/*      */     {
/* 1871 */       File localFile = new File(paramString);
/* 1872 */       String str1 = localFile.getCanonicalPath();
/* 1873 */       if (!str1.endsWith(File.separator)) {
/* 1874 */         str1 = str1 + File.separator;
/*      */       }
/* 1876 */       String str2 = paramFile.getCanonicalPath();
/* 1877 */       return str2.startsWith(str1);
/*      */     }
/*      */     catch (IOException localIOException)
/*      */     {
/*      */     }
/* 1882 */     return false;
/*      */   }
/*      */ 
/*      */   private static boolean standardProfileExists(String paramString)
/*      */   {
/* 1890 */     return ((Boolean)AccessController.doPrivileged(new PrivilegedAction() {
/*      */       public Boolean run() {
/* 1892 */         return Boolean.valueOf(ICC_Profile.getStandardProfileFile(this.val$fileName) != null);
/*      */       }
/*      */     })).booleanValue();
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1963 */     paramObjectOutputStream.defaultWriteObject();
/*      */ 
/* 1965 */     String str = null;
/* 1966 */     if (this == sRGBprofile)
/* 1967 */       str = "CS_sRGB";
/* 1968 */     else if (this == XYZprofile)
/* 1969 */       str = "CS_CIEXYZ";
/* 1970 */     else if (this == PYCCprofile)
/* 1971 */       str = "CS_PYCC";
/* 1972 */     else if (this == GRAYprofile)
/* 1973 */       str = "CS_GRAY";
/* 1974 */     else if (this == LINEAR_RGBprofile) {
/* 1975 */       str = "CS_LINEAR_RGB";
/*      */     }
/*      */ 
/* 1982 */     byte[] arrayOfByte = null;
/* 1983 */     if (str == null)
/*      */     {
/* 1985 */       arrayOfByte = getData();
/*      */     }
/*      */ 
/* 1988 */     paramObjectOutputStream.writeObject(str);
/* 1989 */     paramObjectOutputStream.writeObject(arrayOfByte);
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 2031 */     paramObjectInputStream.defaultReadObject();
/*      */ 
/* 2033 */     String str = (String)paramObjectInputStream.readObject();
/* 2034 */     byte[] arrayOfByte = (byte[])paramObjectInputStream.readObject();
/*      */ 
/* 2036 */     int i = 0;
/* 2037 */     int j = 0;
/* 2038 */     if (str != null) {
/* 2039 */       j = 1;
/* 2040 */       if (str.equals("CS_sRGB"))
/* 2041 */         i = 1000;
/* 2042 */       else if (str.equals("CS_CIEXYZ"))
/* 2043 */         i = 1001;
/* 2044 */       else if (str.equals("CS_PYCC"))
/* 2045 */         i = 1002;
/* 2046 */       else if (str.equals("CS_GRAY"))
/* 2047 */         i = 1003;
/* 2048 */       else if (str.equals("CS_LINEAR_RGB"))
/* 2049 */         i = 1004;
/*      */       else {
/* 2051 */         j = 0;
/*      */       }
/*      */     }
/*      */ 
/* 2055 */     if (j != 0)
/* 2056 */       this.resolvedDeserializedProfile = getInstance(i);
/*      */     else
/* 2058 */       this.resolvedDeserializedProfile = getInstance(arrayOfByte);
/*      */   }
/*      */ 
/*      */   protected Object readResolve()
/*      */     throws ObjectStreamException
/*      */   {
/* 2071 */     return this.resolvedDeserializedProfile;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.awt.color.ICC_Profile
 * JD-Core Version:    0.6.2
 */