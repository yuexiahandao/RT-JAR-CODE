/*     */ package com.sun.corba.se.impl.encoding;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.NoSuchElementException;
/*     */ import java.util.StringTokenizer;
/*     */ 
/*     */ public final class CodeSetComponentInfo
/*     */ {
/*     */   private CodeSetComponent forCharData;
/*     */   private CodeSetComponent forWCharData;
/* 270 */   public static final CodeSetComponentInfo JAVASOFT_DEFAULT_CODESETS = new CodeSetComponentInfo(localCodeSetComponent1, localCodeSetComponent2);
/*     */ 
/* 337 */   public static final CodeSetContext LOCAL_CODE_SETS = new CodeSetContext(OSFCodeSetRegistry.ISO_8859_1.getNumber(), OSFCodeSetRegistry.UTF_16.getNumber());
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 127 */     if (this == paramObject) {
/* 128 */       return true;
/*     */     }
/* 130 */     if (!(paramObject instanceof CodeSetComponentInfo)) {
/* 131 */       return false;
/*     */     }
/* 133 */     CodeSetComponentInfo localCodeSetComponentInfo = (CodeSetComponentInfo)paramObject;
/* 134 */     return (this.forCharData.equals(localCodeSetComponentInfo.forCharData)) && (this.forWCharData.equals(localCodeSetComponentInfo.forWCharData));
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 140 */     return this.forCharData.hashCode() ^ this.forWCharData.hashCode();
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 144 */     StringBuffer localStringBuffer = new StringBuffer("CodeSetComponentInfo(");
/*     */ 
/* 146 */     localStringBuffer.append("char_data:");
/* 147 */     localStringBuffer.append(this.forCharData.toString());
/* 148 */     localStringBuffer.append(" wchar_data:");
/* 149 */     localStringBuffer.append(this.forWCharData.toString());
/* 150 */     localStringBuffer.append(")");
/*     */ 
/* 152 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   public CodeSetComponentInfo() {
/* 156 */     this.forCharData = JAVASOFT_DEFAULT_CODESETS.forCharData;
/* 157 */     this.forWCharData = JAVASOFT_DEFAULT_CODESETS.forWCharData;
/*     */   }
/*     */ 
/*     */   public CodeSetComponentInfo(CodeSetComponent paramCodeSetComponent1, CodeSetComponent paramCodeSetComponent2)
/*     */   {
/* 162 */     this.forCharData = paramCodeSetComponent1;
/* 163 */     this.forWCharData = paramCodeSetComponent2;
/*     */   }
/*     */ 
/*     */   public void read(MarshalInputStream paramMarshalInputStream) {
/* 167 */     this.forCharData = new CodeSetComponent();
/* 168 */     this.forCharData.read(paramMarshalInputStream);
/* 169 */     this.forWCharData = new CodeSetComponent();
/* 170 */     this.forWCharData.read(paramMarshalInputStream);
/*     */   }
/*     */ 
/*     */   public void write(MarshalOutputStream paramMarshalOutputStream) {
/* 174 */     this.forCharData.write(paramMarshalOutputStream);
/* 175 */     this.forWCharData.write(paramMarshalOutputStream);
/*     */   }
/*     */ 
/*     */   public CodeSetComponent getCharComponent() {
/* 179 */     return this.forCharData;
/*     */   }
/*     */ 
/*     */   public CodeSetComponent getWCharComponent() {
/* 183 */     return this.forWCharData;
/*     */   }
/*     */ 
/*     */   public static CodeSetComponent createFromString(String paramString)
/*     */   {
/* 285 */     ORBUtilSystemException localORBUtilSystemException = ORBUtilSystemException.get("rpc.encoding");
/*     */ 
/* 288 */     if ((paramString == null) || (paramString.length() == 0)) {
/* 289 */       throw localORBUtilSystemException.badCodeSetString();
/*     */     }
/* 291 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString, ", ", false);
/* 292 */     int i = 0;
/* 293 */     int[] arrayOfInt = null;
/*     */     try
/*     */     {
/* 298 */       i = Integer.decode(localStringTokenizer.nextToken()).intValue();
/*     */ 
/* 300 */       if (OSFCodeSetRegistry.lookupEntry(i) == null) {
/* 301 */         throw localORBUtilSystemException.unknownNativeCodeset(new Integer(i));
/*     */       }
/* 303 */       ArrayList localArrayList = new ArrayList(10);
/*     */ 
/* 307 */       while (localStringTokenizer.hasMoreTokens())
/*     */       {
/* 310 */         Integer localInteger = Integer.decode(localStringTokenizer.nextToken());
/*     */ 
/* 312 */         if (OSFCodeSetRegistry.lookupEntry(localInteger.intValue()) == null) {
/* 313 */           throw localORBUtilSystemException.unknownConversionCodeSet(localInteger);
/*     */         }
/* 315 */         localArrayList.add(localInteger);
/*     */       }
/*     */ 
/* 318 */       arrayOfInt = new int[localArrayList.size()];
/*     */ 
/* 320 */       for (int j = 0; j < arrayOfInt.length; j++)
/* 321 */         arrayOfInt[j] = ((Integer)localArrayList.get(j)).intValue();
/*     */     }
/*     */     catch (NumberFormatException localNumberFormatException) {
/* 324 */       throw localORBUtilSystemException.invalidCodeSetNumber(localNumberFormatException);
/*     */     } catch (NoSuchElementException localNoSuchElementException) {
/* 326 */       throw localORBUtilSystemException.invalidCodeSetString(localNoSuchElementException, paramString);
/*     */     }
/*     */ 
/* 331 */     return new CodeSetComponent(i, arrayOfInt);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 256 */     CodeSetComponent localCodeSetComponent1 = new CodeSetComponent(OSFCodeSetRegistry.ISO_8859_1.getNumber(), new int[] { OSFCodeSetRegistry.UTF_8.getNumber(), OSFCodeSetRegistry.ISO_646.getNumber() });
/*     */ 
/* 263 */     CodeSetComponent localCodeSetComponent2 = new CodeSetComponent(OSFCodeSetRegistry.UTF_16.getNumber(), new int[] { OSFCodeSetRegistry.UCS_2.getNumber() });
/*     */   }
/*     */ 
/*     */   public static final class CodeSetComponent
/*     */   {
/*     */     int nativeCodeSet;
/*     */     int[] conversionCodeSets;
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/*  58 */       if (this == paramObject) {
/*  59 */         return true;
/*     */       }
/*  61 */       if (!(paramObject instanceof CodeSetComponent)) {
/*  62 */         return false;
/*     */       }
/*  64 */       CodeSetComponent localCodeSetComponent = (CodeSetComponent)paramObject;
/*     */ 
/*  66 */       return (this.nativeCodeSet == localCodeSetComponent.nativeCodeSet) && (Arrays.equals(this.conversionCodeSets, localCodeSetComponent.conversionCodeSets));
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/*  72 */       int i = this.nativeCodeSet;
/*  73 */       for (int j = 0; j < this.conversionCodeSets.length; j++)
/*  74 */         i = 37 * i + this.conversionCodeSets[j];
/*  75 */       return i;
/*     */     }
/*     */     public CodeSetComponent() {
/*     */     }
/*     */ 
/*     */     public CodeSetComponent(int paramInt, int[] paramArrayOfInt) {
/*  81 */       this.nativeCodeSet = paramInt;
/*  82 */       if (paramArrayOfInt == null)
/*  83 */         this.conversionCodeSets = new int[0];
/*     */       else
/*  85 */         this.conversionCodeSets = paramArrayOfInt;
/*     */     }
/*     */ 
/*     */     public void read(MarshalInputStream paramMarshalInputStream) {
/*  89 */       this.nativeCodeSet = paramMarshalInputStream.read_ulong();
/*  90 */       int i = paramMarshalInputStream.read_long();
/*  91 */       this.conversionCodeSets = new int[i];
/*  92 */       paramMarshalInputStream.read_ulong_array(this.conversionCodeSets, 0, i);
/*     */     }
/*     */ 
/*     */     public void write(MarshalOutputStream paramMarshalOutputStream)
/*     */     {
/*  97 */       paramMarshalOutputStream.write_ulong(this.nativeCodeSet);
/*  98 */       paramMarshalOutputStream.write_long(this.conversionCodeSets.length);
/*  99 */       paramMarshalOutputStream.write_ulong_array(this.conversionCodeSets, 0, this.conversionCodeSets.length);
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 103 */       StringBuffer localStringBuffer = new StringBuffer("CodeSetComponent(");
/*     */ 
/* 105 */       localStringBuffer.append("native:");
/* 106 */       localStringBuffer.append(Integer.toHexString(this.nativeCodeSet));
/* 107 */       localStringBuffer.append(" conversion:");
/* 108 */       if (this.conversionCodeSets == null)
/* 109 */         localStringBuffer.append("null");
/*     */       else {
/* 111 */         for (int i = 0; i < this.conversionCodeSets.length; i++) {
/* 112 */           localStringBuffer.append(Integer.toHexString(this.conversionCodeSets[i]));
/* 113 */           localStringBuffer.append(' ');
/*     */         }
/*     */       }
/* 116 */       localStringBuffer.append(")");
/*     */ 
/* 118 */       return localStringBuffer.toString();
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class CodeSetContext
/*     */   {
/*     */     private int char_data;
/*     */     private int wchar_data;
/*     */ 
/*     */     public CodeSetContext()
/*     */     {
/*     */     }
/*     */ 
/*     */     public CodeSetContext(int paramInt1, int paramInt2)
/*     */     {
/* 196 */       this.char_data = paramInt1;
/* 197 */       this.wchar_data = paramInt2;
/*     */     }
/*     */ 
/*     */     public void read(MarshalInputStream paramMarshalInputStream) {
/* 201 */       this.char_data = paramMarshalInputStream.read_ulong();
/* 202 */       this.wchar_data = paramMarshalInputStream.read_ulong();
/*     */     }
/*     */ 
/*     */     public void write(MarshalOutputStream paramMarshalOutputStream) {
/* 206 */       paramMarshalOutputStream.write_ulong(this.char_data);
/* 207 */       paramMarshalOutputStream.write_ulong(this.wchar_data);
/*     */     }
/*     */ 
/*     */     public int getCharCodeSet() {
/* 211 */       return this.char_data;
/*     */     }
/*     */ 
/*     */     public int getWCharCodeSet() {
/* 215 */       return this.wchar_data;
/*     */     }
/*     */ 
/*     */     public String toString() {
/* 219 */       StringBuffer localStringBuffer = new StringBuffer();
/* 220 */       localStringBuffer.append("CodeSetContext char set: ");
/* 221 */       localStringBuffer.append(Integer.toHexString(this.char_data));
/* 222 */       localStringBuffer.append(" wchar set: ");
/* 223 */       localStringBuffer.append(Integer.toHexString(this.wchar_data));
/* 224 */       return localStringBuffer.toString();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.encoding.CodeSetComponentInfo
 * JD-Core Version:    0.6.2
 */