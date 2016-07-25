/*     */ package com.sun.imageio.plugins.gif;
/*     */ 
/*     */ import javax.imageio.metadata.IIOInvalidTreeException;
/*     */ import javax.imageio.metadata.IIOMetadata;
/*     */ import org.w3c.dom.NamedNodeMap;
/*     */ import org.w3c.dom.Node;
/*     */ 
/*     */ abstract class GIFMetadata extends IIOMetadata
/*     */ {
/*     */   static final int UNDEFINED_INTEGER_VALUE = -1;
/*     */ 
/*     */   protected static void fatal(Node paramNode, String paramString)
/*     */     throws IIOInvalidTreeException
/*     */   {
/*  52 */     throw new IIOInvalidTreeException(paramString, paramNode);
/*     */   }
/*     */ 
/*     */   protected static String getStringAttribute(Node paramNode, String paramString1, String paramString2, boolean paramBoolean, String[] paramArrayOfString)
/*     */     throws IIOInvalidTreeException
/*     */   {
/*  61 */     Node localNode = paramNode.getAttributes().getNamedItem(paramString1);
/*  62 */     if (localNode == null) {
/*  63 */       if (!paramBoolean) {
/*  64 */         return paramString2;
/*     */       }
/*  66 */       fatal(paramNode, "Required attribute " + paramString1 + " not present!");
/*     */     }
/*     */ 
/*  69 */     String str = localNode.getNodeValue();
/*     */ 
/*  71 */     if (paramArrayOfString != null) {
/*  72 */       if (str == null) {
/*  73 */         fatal(paramNode, "Null value for " + paramNode.getNodeName() + " attribute " + paramString1 + "!");
/*     */       }
/*     */ 
/*  77 */       int i = 0;
/*  78 */       int j = paramArrayOfString.length;
/*  79 */       for (int k = 0; k < j; k++) {
/*  80 */         if (str.equals(paramArrayOfString[k])) {
/*  81 */           i = 1;
/*  82 */           break;
/*     */         }
/*     */       }
/*  85 */       if (i == 0) {
/*  86 */         fatal(paramNode, "Bad value for " + paramNode.getNodeName() + " attribute " + paramString1 + "!");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  92 */     return str;
/*     */   }
/*     */ 
/*     */   protected static int getIntAttribute(Node paramNode, String paramString, int paramInt1, boolean paramBoolean1, boolean paramBoolean2, int paramInt2, int paramInt3)
/*     */     throws IIOInvalidTreeException
/*     */   {
/* 101 */     String str = getStringAttribute(paramNode, paramString, null, paramBoolean1, null);
/* 102 */     if ((str == null) || ("".equals(str))) {
/* 103 */       return paramInt1;
/*     */     }
/*     */ 
/* 106 */     int i = paramInt1;
/*     */     try {
/* 108 */       i = Integer.parseInt(str);
/*     */     } catch (NumberFormatException localNumberFormatException) {
/* 110 */       fatal(paramNode, "Bad value for " + paramNode.getNodeName() + " attribute " + paramString + "!");
/*     */     }
/*     */ 
/* 114 */     if ((paramBoolean2) && ((i < paramInt2) || (i > paramInt3))) {
/* 115 */       fatal(paramNode, "Bad value for " + paramNode.getNodeName() + " attribute " + paramString + "!");
/*     */     }
/*     */ 
/* 119 */     return i;
/*     */   }
/*     */ 
/*     */   protected static float getFloatAttribute(Node paramNode, String paramString, float paramFloat, boolean paramBoolean)
/*     */     throws IIOInvalidTreeException
/*     */   {
/* 127 */     String str = getStringAttribute(paramNode, paramString, null, paramBoolean, null);
/* 128 */     if (str == null) {
/* 129 */       return paramFloat;
/*     */     }
/* 131 */     return Float.parseFloat(str);
/*     */   }
/*     */ 
/*     */   protected static int getIntAttribute(Node paramNode, String paramString, boolean paramBoolean, int paramInt1, int paramInt2)
/*     */     throws IIOInvalidTreeException
/*     */   {
/* 138 */     return getIntAttribute(paramNode, paramString, -1, true, paramBoolean, paramInt1, paramInt2);
/*     */   }
/*     */ 
/*     */   protected static float getFloatAttribute(Node paramNode, String paramString)
/*     */     throws IIOInvalidTreeException
/*     */   {
/* 144 */     return getFloatAttribute(paramNode, paramString, -1.0F, true);
/*     */   }
/*     */ 
/*     */   protected static boolean getBooleanAttribute(Node paramNode, String paramString, boolean paramBoolean1, boolean paramBoolean2)
/*     */     throws IIOInvalidTreeException
/*     */   {
/* 152 */     Node localNode = paramNode.getAttributes().getNamedItem(paramString);
/* 153 */     if (localNode == null) {
/* 154 */       if (!paramBoolean2) {
/* 155 */         return paramBoolean1;
/*     */       }
/* 157 */       fatal(paramNode, "Required attribute " + paramString + " not present!");
/*     */     }
/*     */ 
/* 160 */     String str = localNode.getNodeValue();
/*     */ 
/* 162 */     if ((str.equals("TRUE")) || (str.equals("true")))
/* 163 */       return true;
/* 164 */     if ((str.equals("FALSE")) || (str.equals("false"))) {
/* 165 */       return false;
/*     */     }
/* 167 */     fatal(paramNode, "Attribute " + paramString + " must be 'TRUE' or 'FALSE'!");
/* 168 */     return false;
/*     */   }
/*     */ 
/*     */   protected static boolean getBooleanAttribute(Node paramNode, String paramString)
/*     */     throws IIOInvalidTreeException
/*     */   {
/* 175 */     return getBooleanAttribute(paramNode, paramString, false, true);
/*     */   }
/*     */ 
/*     */   protected static int getEnumeratedAttribute(Node paramNode, String paramString, String[] paramArrayOfString, int paramInt, boolean paramBoolean)
/*     */     throws IIOInvalidTreeException
/*     */   {
/* 185 */     Node localNode = paramNode.getAttributes().getNamedItem(paramString);
/* 186 */     if (localNode == null) {
/* 187 */       if (!paramBoolean) {
/* 188 */         return paramInt;
/*     */       }
/* 190 */       fatal(paramNode, "Required attribute " + paramString + " not present!");
/*     */     }
/*     */ 
/* 193 */     String str = localNode.getNodeValue();
/* 194 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 195 */       if (str.equals(paramArrayOfString[i])) {
/* 196 */         return i;
/*     */       }
/*     */     }
/*     */ 
/* 200 */     fatal(paramNode, "Illegal value for attribute " + paramString + "!");
/* 201 */     return -1;
/*     */   }
/*     */ 
/*     */   protected static int getEnumeratedAttribute(Node paramNode, String paramString, String[] paramArrayOfString)
/*     */     throws IIOInvalidTreeException
/*     */   {
/* 209 */     return getEnumeratedAttribute(paramNode, paramString, paramArrayOfString, -1, true);
/*     */   }
/*     */ 
/*     */   protected static String getAttribute(Node paramNode, String paramString1, String paramString2, boolean paramBoolean)
/*     */     throws IIOInvalidTreeException
/*     */   {
/* 216 */     Node localNode = paramNode.getAttributes().getNamedItem(paramString1);
/* 217 */     if (localNode == null) {
/* 218 */       if (!paramBoolean) {
/* 219 */         return paramString2;
/*     */       }
/* 221 */       fatal(paramNode, "Required attribute " + paramString1 + " not present!");
/*     */     }
/*     */ 
/* 224 */     return localNode.getNodeValue();
/*     */   }
/*     */ 
/*     */   protected static String getAttribute(Node paramNode, String paramString)
/*     */     throws IIOInvalidTreeException
/*     */   {
/* 230 */     return getAttribute(paramNode, paramString, null, true);
/*     */   }
/*     */ 
/*     */   protected GIFMetadata(boolean paramBoolean, String paramString1, String paramString2, String[] paramArrayOfString1, String[] paramArrayOfString2)
/*     */   {
/* 238 */     super(paramBoolean, paramString1, paramString2, paramArrayOfString1, paramArrayOfString2);
/*     */   }
/*     */ 
/*     */   public void mergeTree(String paramString, Node paramNode)
/*     */     throws IIOInvalidTreeException
/*     */   {
/* 247 */     if (paramString.equals(this.nativeMetadataFormatName)) {
/* 248 */       if (paramNode == null) {
/* 249 */         throw new IllegalArgumentException("root == null!");
/*     */       }
/* 251 */       mergeNativeTree(paramNode);
/* 252 */     } else if (paramString.equals("javax_imageio_1.0"))
/*     */     {
/* 254 */       if (paramNode == null) {
/* 255 */         throw new IllegalArgumentException("root == null!");
/*     */       }
/* 257 */       mergeStandardTree(paramNode);
/*     */     } else {
/* 259 */       throw new IllegalArgumentException("Not a recognized format!");
/*     */     }
/*     */   }
/*     */ 
/*     */   protected byte[] getColorTable(Node paramNode, String paramString, boolean paramBoolean, int paramInt)
/*     */     throws IIOInvalidTreeException
/*     */   {
/* 268 */     byte[] arrayOfByte1 = new byte[256];
/* 269 */     byte[] arrayOfByte2 = new byte[256];
/* 270 */     byte[] arrayOfByte3 = new byte[256];
/* 271 */     int i = -1;
/*     */ 
/* 273 */     Node localNode = paramNode.getFirstChild();
/* 274 */     if (localNode == null) {
/* 275 */       fatal(paramNode, "Palette has no entries!");
/*     */     }
/*     */ 
/* 278 */     while (localNode != null) {
/* 279 */       if (!localNode.getNodeName().equals(paramString)) {
/* 280 */         fatal(paramNode, "Only a " + paramString + " may be a child of a " + localNode.getNodeName() + "!");
/*     */       }
/*     */ 
/* 285 */       j = getIntAttribute(localNode, "index", true, 0, 255);
/* 286 */       if (j > i) {
/* 287 */         i = j;
/*     */       }
/* 289 */       arrayOfByte1[j] = ((byte)getIntAttribute(localNode, "red", true, 0, 255));
/* 290 */       arrayOfByte2[j] = ((byte)getIntAttribute(localNode, "green", true, 0, 255));
/* 291 */       arrayOfByte3[j] = ((byte)getIntAttribute(localNode, "blue", true, 0, 255));
/*     */ 
/* 293 */       localNode = localNode.getNextSibling();
/*     */     }
/*     */ 
/* 296 */     int j = i + 1;
/*     */ 
/* 298 */     if ((paramBoolean) && (j != paramInt)) {
/* 299 */       fatal(paramNode, "Unexpected length for palette!");
/*     */     }
/*     */ 
/* 302 */     byte[] arrayOfByte4 = new byte[3 * j];
/* 303 */     int k = 0; for (int m = 0; k < j; k++) {
/* 304 */       arrayOfByte4[(m++)] = arrayOfByte1[k];
/* 305 */       arrayOfByte4[(m++)] = arrayOfByte2[k];
/* 306 */       arrayOfByte4[(m++)] = arrayOfByte3[k];
/*     */     }
/*     */ 
/* 309 */     return arrayOfByte4;
/*     */   }
/*     */ 
/*     */   protected abstract void mergeNativeTree(Node paramNode)
/*     */     throws IIOInvalidTreeException;
/*     */ 
/*     */   protected abstract void mergeStandardTree(Node paramNode)
/*     */     throws IIOInvalidTreeException;
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.imageio.plugins.gif.GIFMetadata
 * JD-Core Version:    0.6.2
 */