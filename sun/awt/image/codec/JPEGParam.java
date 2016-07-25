/*      */ package sun.awt.image.codec;
/*      */ 
/*      */ import com.sun.image.codec.jpeg.JPEGDecodeParam;
/*      */ import com.sun.image.codec.jpeg.JPEGEncodeParam;
/*      */ import com.sun.image.codec.jpeg.JPEGHuffmanTable;
/*      */ import com.sun.image.codec.jpeg.JPEGQTable;
/*      */ import java.awt.color.ColorSpace;
/*      */ import java.awt.image.ColorModel;
/*      */ import java.util.Enumeration;
/*      */ import java.util.Vector;
/*      */ 
/*      */ public class JPEGParam
/*      */   implements JPEGEncodeParam, Cloneable
/*      */ {
/*   26 */   private static int[] defComponents = { -1, 1, 3, 3, 4, 3, 4, 4, 4, 4, 4, 4 };
/*      */ 
/*   39 */   private static int[][] stdCompMapping = { { 0, 0, 0, 0 }, { 0 }, { 0, 0, 0 }, { 0, 1, 1 }, { 0, 0, 0, 0 }, { 0, 1, 1 }, { 0, 0, 0, 0 }, { 0, 1, 1, 0 }, { 0, 0, 0, 0 }, { 0, 1, 1, 0 }, { 0, 1, 1, 0 }, { 0, 1, 1, 0 } };
/*      */ 
/*   54 */   private static int[][] stdSubsample = { { 1, 1, 1, 1 }, { 1 }, { 1, 1, 1 }, { 1, 2, 2 }, { 1, 1, 1, 1 }, { 1, 2, 2 }, { 1, 1, 1, 1 }, { 1, 2, 2, 1 }, { 1, 1, 1, 1 }, { 1, 2, 2, 1 }, { 1, 2, 2, 1 }, { 1, 2, 2, 1 } };
/*      */   private int width;
/*      */   private int height;
/*      */   private int encodedColorID;
/*      */   private int numComponents;
/*      */   private byte[][][] appMarkers;
/*      */   private byte[][] comMarker;
/*      */   private boolean imageInfoValid;
/*      */   private boolean tableInfoValid;
/*      */   private int[] horizontalSubsampling;
/*      */   private int[] verticalSubsampling;
/*      */   private JPEGQTable[] qTables;
/*      */   private int[] qTableMapping;
/*      */   private JPEGHuffmanTable[] dcHuffTables;
/*      */   private int[] dcHuffMapping;
/*      */   private JPEGHuffmanTable[] acHuffTables;
/*      */   private int[] acHuffMapping;
/*      */   private int restartInterval;
/*      */   private static final int app0Length = 14;
/*      */ 
/*      */   public JPEGParam(int paramInt)
/*      */   {
/*  118 */     this(paramInt, defComponents[paramInt]);
/*      */   }
/*      */ 
/*      */   public JPEGParam(JPEGDecodeParam paramJPEGDecodeParam)
/*      */   {
/*  126 */     this(paramJPEGDecodeParam.getEncodedColorID(), paramJPEGDecodeParam.getNumComponents());
/*  127 */     copy(paramJPEGDecodeParam);
/*      */   }
/*      */ 
/*      */   public JPEGParam(JPEGEncodeParam paramJPEGEncodeParam)
/*      */   {
/*  135 */     this(paramJPEGEncodeParam.getEncodedColorID(), paramJPEGEncodeParam.getNumComponents());
/*  136 */     copy(paramJPEGEncodeParam);
/*      */   }
/*      */ 
/*      */   public JPEGParam(int paramInt1, int paramInt2)
/*      */   {
/*  152 */     if ((paramInt1 != 0) && (paramInt2 != defComponents[paramInt1]))
/*      */     {
/*  154 */       throw new IllegalArgumentException("NumComponents not in sync with COLOR_ID");
/*      */     }
/*      */ 
/*  157 */     this.qTables = new JPEGQTable[4];
/*  158 */     this.acHuffTables = new JPEGHuffmanTable[4];
/*  159 */     this.dcHuffTables = new JPEGHuffmanTable[4];
/*  160 */     for (int i = 0; i < 4; i++) {
/*  161 */       this.qTables[i] = null;
/*  162 */       this.dcHuffTables[i] = null;
/*  163 */       this.acHuffTables[i] = null;
/*      */     }
/*  165 */     this.comMarker = ((byte[][])null);
/*  166 */     this.appMarkers = new byte[16][][];
/*      */ 
/*  168 */     this.numComponents = paramInt2;
/*  169 */     setDefaults(paramInt1);
/*      */   }
/*      */ 
/*      */   private void copy(JPEGDecodeParam paramJPEGDecodeParam) {
/*  173 */     if (getEncodedColorID() != paramJPEGDecodeParam.getEncodedColorID()) {
/*  174 */       throw new IllegalArgumentException("Argument to copy must match current COLOR_ID");
/*      */     }
/*  176 */     if (getNumComponents() != paramJPEGDecodeParam.getNumComponents()) {
/*  177 */       throw new IllegalArgumentException("Argument to copy must match in number of components");
/*      */     }
/*      */ 
/*  180 */     setWidth(paramJPEGDecodeParam.getWidth());
/*  181 */     setHeight(paramJPEGDecodeParam.getHeight());
/*      */ 
/*  183 */     for (int i = 224; i < 239; i++) {
/*  184 */       setMarkerData(i, copyArrays(paramJPEGDecodeParam.getMarkerData(i)));
/*      */     }
/*  186 */     setMarkerData(254, copyArrays(paramJPEGDecodeParam.getMarkerData(254)));
/*      */ 
/*  189 */     setTableInfoValid(paramJPEGDecodeParam.isTableInfoValid());
/*  190 */     setImageInfoValid(paramJPEGDecodeParam.isImageInfoValid());
/*      */ 
/*  192 */     setRestartInterval(paramJPEGDecodeParam.getRestartInterval());
/*      */ 
/*  194 */     for (i = 0; i < 4; i++) {
/*  195 */       setDCHuffmanTable(i, paramJPEGDecodeParam.getDCHuffmanTable(i));
/*  196 */       setACHuffmanTable(i, paramJPEGDecodeParam.getACHuffmanTable(i));
/*  197 */       setQTable(i, paramJPEGDecodeParam.getQTable(i));
/*      */     }
/*      */ 
/*  200 */     for (i = 0; i < paramJPEGDecodeParam.getNumComponents(); i++) {
/*  201 */       setDCHuffmanComponentMapping(i, paramJPEGDecodeParam.getDCHuffmanComponentMapping(i));
/*      */ 
/*  203 */       setACHuffmanComponentMapping(i, paramJPEGDecodeParam.getACHuffmanComponentMapping(i));
/*      */ 
/*  205 */       setQTableComponentMapping(i, paramJPEGDecodeParam.getQTableComponentMapping(i));
/*      */ 
/*  207 */       setHorizontalSubsampling(i, paramJPEGDecodeParam.getHorizontalSubsampling(i));
/*      */ 
/*  209 */       setVerticalSubsampling(i, paramJPEGDecodeParam.getVerticalSubsampling(i));
/*      */     }
/*      */   }
/*      */ 
/*      */   private void copy(JPEGEncodeParam paramJPEGEncodeParam)
/*      */   {
/*  215 */     copy(paramJPEGEncodeParam);
/*      */   }
/*      */ 
/*      */   protected void setDefaults(int paramInt)
/*      */   {
/*  237 */     this.encodedColorID = paramInt;
/*      */ 
/*  239 */     this.restartInterval = 0;
/*      */ 
/*  241 */     int i = 0;
/*  242 */     switch (this.numComponents) {
/*      */     case 1:
/*  244 */       if ((this.encodedColorID == 1) || (this.encodedColorID == 0))
/*      */       {
/*  246 */         i = 1; } break;
/*      */     case 3:
/*  250 */       if (this.encodedColorID == 3)
/*  251 */         i = 1; break;
/*      */     case 4:
/*  255 */       if (this.encodedColorID == 4)
/*  256 */         i = 1; break;
/*      */     case 2:
/*      */     }
/*      */ 
/*  262 */     if (i != 0) {
/*  263 */       addMarkerData(224, createDefaultAPP0Marker());
/*      */     }
/*  265 */     setTableInfoValid(true);
/*  266 */     setImageInfoValid(true);
/*      */ 
/*  268 */     this.dcHuffTables[0] = JPEGHuffmanTable.StdDCLuminance;
/*  269 */     this.dcHuffTables[1] = JPEGHuffmanTable.StdDCChrominance;
/*  270 */     this.dcHuffMapping = new int[getNumComponents()];
/*  271 */     System.arraycopy(stdCompMapping[this.encodedColorID], 0, this.dcHuffMapping, 0, getNumComponents());
/*      */ 
/*  274 */     this.acHuffTables[0] = JPEGHuffmanTable.StdACLuminance;
/*  275 */     this.acHuffTables[1] = JPEGHuffmanTable.StdACChrominance;
/*  276 */     this.acHuffMapping = new int[getNumComponents()];
/*  277 */     System.arraycopy(stdCompMapping[this.encodedColorID], 0, this.acHuffMapping, 0, getNumComponents());
/*      */ 
/*  280 */     this.qTables[0] = JPEGQTable.StdLuminance.getScaledInstance(0.5F, true);
/*  281 */     this.qTables[1] = JPEGQTable.StdChrominance.getScaledInstance(0.5F, true);
/*  282 */     this.qTableMapping = new int[getNumComponents()];
/*  283 */     System.arraycopy(stdCompMapping[this.encodedColorID], 0, this.qTableMapping, 0, getNumComponents());
/*      */ 
/*  286 */     this.horizontalSubsampling = new int[getNumComponents()];
/*  287 */     System.arraycopy(stdSubsample[this.encodedColorID], 0, this.horizontalSubsampling, 0, getNumComponents());
/*      */ 
/*  290 */     this.verticalSubsampling = new int[getNumComponents()];
/*  291 */     System.arraycopy(stdSubsample[this.encodedColorID], 0, this.verticalSubsampling, 0, getNumComponents());
/*      */   }
/*      */ 
/*      */   public Object clone()
/*      */   {
/*  296 */     JPEGParam localJPEGParam = new JPEGParam(getEncodedColorID(), getNumComponents());
/*      */ 
/*  298 */     localJPEGParam.copy(this);
/*  299 */     return localJPEGParam;
/*      */   }
/*      */ 
/*      */   public int getWidth()
/*      */   {
/*  306 */     return this.width;
/*      */   }
/*      */ 
/*      */   public int getHeight() {
/*  310 */     return this.height;
/*      */   }
/*      */ 
/*      */   public void setWidth(int paramInt)
/*      */   {
/*  318 */     this.width = paramInt;
/*      */   }
/*      */ 
/*      */   public void setHeight(int paramInt)
/*      */   {
/*  326 */     this.height = paramInt;
/*      */   }
/*      */ 
/*      */   public int getHorizontalSubsampling(int paramInt)
/*      */   {
/*  340 */     if ((paramInt < 0) || (paramInt >= getNumComponents())) {
/*  341 */       throw new IllegalArgumentException("Component must be between 0 and number of components");
/*      */     }
/*  343 */     return this.horizontalSubsampling[paramInt];
/*      */   }
/*      */ 
/*      */   public int getVerticalSubsampling(int paramInt)
/*      */   {
/*  358 */     if ((paramInt < 0) || (paramInt >= getNumComponents())) {
/*  359 */       throw new IllegalArgumentException("Component must be between 0 and number of components");
/*      */     }
/*  361 */     return this.verticalSubsampling[paramInt];
/*      */   }
/*      */ 
/*      */   public void setHorizontalSubsampling(int paramInt1, int paramInt2)
/*      */   {
/*  374 */     if ((paramInt1 < 0) || (paramInt1 >= getNumComponents())) {
/*  375 */       throw new IllegalArgumentException("Component must be between 0 and number of components: " + paramInt1);
/*      */     }
/*      */ 
/*  378 */     if (paramInt2 <= 0) {
/*  379 */       throw new IllegalArgumentException("SubSample factor must be positive: " + paramInt2);
/*      */     }
/*      */ 
/*  382 */     this.horizontalSubsampling[paramInt1] = paramInt2;
/*      */   }
/*      */ 
/*      */   public void setVerticalSubsampling(int paramInt1, int paramInt2)
/*      */   {
/*  394 */     if ((paramInt1 < 0) || (paramInt1 >= getNumComponents())) {
/*  395 */       throw new IllegalArgumentException("Component must be between 0 and number of components");
/*      */     }
/*  397 */     if (paramInt2 <= 0) {
/*  398 */       throw new IllegalArgumentException("SubSample factor must be positive.");
/*      */     }
/*      */ 
/*  401 */     this.verticalSubsampling[paramInt1] = paramInt2;
/*      */   }
/*      */ 
/*      */   public JPEGQTable getQTable(int paramInt)
/*      */   {
/*  411 */     if ((paramInt < 0) || (paramInt >= 4)) {
/*  412 */       throw new IllegalArgumentException("tableNum must be between 0 and 3.");
/*      */     }
/*  414 */     return this.qTables[paramInt];
/*      */   }
/*      */ 
/*      */   public JPEGQTable getQTableForComponent(int paramInt)
/*      */   {
/*  422 */     if ((paramInt < 0) || (paramInt >= this.qTableMapping.length)) {
/*  423 */       throw new IllegalArgumentException("Component must be between 0 and number of components");
/*      */     }
/*  425 */     return getQTable(this.qTableMapping[paramInt]);
/*      */   }
/*      */ 
/*      */   public JPEGHuffmanTable getDCHuffmanTable(int paramInt)
/*      */   {
/*  435 */     if ((paramInt < 0) || (paramInt >= 4)) {
/*  436 */       throw new IllegalArgumentException("tableNum must be 0-3.");
/*      */     }
/*  438 */     return this.dcHuffTables[paramInt];
/*      */   }
/*      */ 
/*      */   public JPEGHuffmanTable getDCHuffmanTableForComponent(int paramInt)
/*      */   {
/*  446 */     if ((paramInt < 0) || (paramInt >= this.dcHuffMapping.length)) {
/*  447 */       throw new IllegalArgumentException("Component must be between 0 and number of components");
/*      */     }
/*  449 */     return getDCHuffmanTable(this.dcHuffMapping[paramInt]);
/*      */   }
/*      */ 
/*      */   public JPEGHuffmanTable getACHuffmanTable(int paramInt)
/*      */   {
/*  460 */     if ((paramInt < 0) || (paramInt >= 4)) {
/*  461 */       throw new IllegalArgumentException("tableNum must be 0-3.");
/*      */     }
/*  463 */     return this.acHuffTables[paramInt];
/*      */   }
/*      */ 
/*      */   public JPEGHuffmanTable getACHuffmanTableForComponent(int paramInt)
/*      */   {
/*  471 */     if ((paramInt < 0) || (paramInt >= this.acHuffMapping.length)) {
/*  472 */       throw new IllegalArgumentException("Component must be between 0 and number of components");
/*      */     }
/*  474 */     return getACHuffmanTable(this.acHuffMapping[paramInt]);
/*      */   }
/*      */ 
/*      */   public void setQTable(int paramInt, JPEGQTable paramJPEGQTable)
/*      */   {
/*  484 */     if ((paramInt < 0) || (paramInt >= 4)) {
/*  485 */       throw new IllegalArgumentException("tableNum must be between 0 and 3.");
/*      */     }
/*  487 */     this.qTables[paramInt] = paramJPEGQTable;
/*      */   }
/*      */ 
/*      */   public void setDCHuffmanTable(int paramInt, JPEGHuffmanTable paramJPEGHuffmanTable)
/*      */   {
/*  499 */     if ((paramInt < 0) || (paramInt >= 4)) {
/*  500 */       throw new IllegalArgumentException("tableNum must be 0, 1, 2, or 3.");
/*      */     }
/*  502 */     this.dcHuffTables[paramInt] = paramJPEGHuffmanTable;
/*      */   }
/*      */ 
/*      */   public void setACHuffmanTable(int paramInt, JPEGHuffmanTable paramJPEGHuffmanTable)
/*      */   {
/*  514 */     if ((paramInt < 0) || (paramInt >= 4)) {
/*  515 */       throw new IllegalArgumentException("tableNum must be 0, 1, 2, or 3.");
/*      */     }
/*  517 */     this.acHuffTables[paramInt] = paramJPEGHuffmanTable;
/*      */   }
/*      */ 
/*      */   public int getDCHuffmanComponentMapping(int paramInt)
/*      */   {
/*  529 */     if ((paramInt < 0) || (paramInt >= getNumComponents())) {
/*  530 */       throw new IllegalArgumentException("Requested Component doesn't exist.");
/*      */     }
/*  532 */     return this.dcHuffMapping[paramInt];
/*      */   }
/*      */ 
/*      */   public int getACHuffmanComponentMapping(int paramInt)
/*      */   {
/*  542 */     if ((paramInt < 0) || (paramInt >= getNumComponents())) {
/*  543 */       throw new IllegalArgumentException("Requested Component doesn't exist.");
/*      */     }
/*  545 */     return this.acHuffMapping[paramInt];
/*      */   }
/*      */ 
/*      */   public int getQTableComponentMapping(int paramInt)
/*      */   {
/*  554 */     if ((paramInt < 0) || (paramInt >= getNumComponents())) {
/*  555 */       throw new IllegalArgumentException("Requested Component doesn't exist.");
/*      */     }
/*  557 */     return this.qTableMapping[paramInt];
/*      */   }
/*      */ 
/*      */   public void setDCHuffmanComponentMapping(int paramInt1, int paramInt2)
/*      */   {
/*  566 */     if ((paramInt1 < 0) || (paramInt1 >= getNumComponents())) {
/*  567 */       throw new IllegalArgumentException("Given Component doesn't exist.");
/*      */     }
/*      */ 
/*  570 */     if ((paramInt2 < 0) || (paramInt2 >= 4)) {
/*  571 */       throw new IllegalArgumentException("Tables must be 0, 1, 2, or 3.");
/*      */     }
/*      */ 
/*  574 */     this.dcHuffMapping[paramInt1] = paramInt2;
/*      */   }
/*      */ 
/*      */   public void setACHuffmanComponentMapping(int paramInt1, int paramInt2)
/*      */   {
/*  583 */     if ((paramInt1 < 0) || (paramInt1 >= getNumComponents())) {
/*  584 */       throw new IllegalArgumentException("Given Component doesn't exist.");
/*      */     }
/*      */ 
/*  587 */     if ((paramInt2 < 0) || (paramInt2 >= 4)) {
/*  588 */       throw new IllegalArgumentException("Tables must be 0, 1, 2, or 3.");
/*      */     }
/*      */ 
/*  591 */     this.acHuffMapping[paramInt1] = paramInt2;
/*      */   }
/*      */ 
/*      */   public void setQTableComponentMapping(int paramInt1, int paramInt2)
/*      */   {
/*  600 */     if ((paramInt1 < 0) || (paramInt1 >= getNumComponents())) {
/*  601 */       throw new IllegalArgumentException("Given Component doesn't exist.");
/*      */     }
/*      */ 
/*  604 */     if ((paramInt2 < 0) || (paramInt2 >= 4)) {
/*  605 */       throw new IllegalArgumentException("Tables must be 0, 1, 2, or 3.");
/*      */     }
/*      */ 
/*  608 */     this.qTableMapping[paramInt1] = paramInt2;
/*      */   }
/*      */ 
/*      */   public boolean isImageInfoValid() {
/*  612 */     return this.imageInfoValid;
/*      */   }
/*      */   public void setImageInfoValid(boolean paramBoolean) {
/*  615 */     this.imageInfoValid = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean isTableInfoValid()
/*      */   {
/*  620 */     return this.tableInfoValid;
/*      */   }
/*      */   public void setTableInfoValid(boolean paramBoolean) {
/*  623 */     this.tableInfoValid = paramBoolean;
/*      */   }
/*      */ 
/*      */   public boolean getMarker(int paramInt)
/*      */   {
/*  628 */     byte[][] arrayOfByte = (byte[][])null;
/*  629 */     if (paramInt == 254)
/*  630 */       arrayOfByte = this.comMarker;
/*  631 */     else if ((paramInt >= 224) && (paramInt <= 239))
/*      */     {
/*  633 */       arrayOfByte = this.appMarkers[(paramInt - 224)];
/*      */     }
/*  635 */     else throw new IllegalArgumentException("Invalid Marker ID:" + paramInt);
/*      */ 
/*  638 */     if (arrayOfByte == null) return false;
/*  639 */     if (arrayOfByte.length == 0) return false;
/*  640 */     return true;
/*      */   }
/*      */ 
/*      */   public byte[][] getMarkerData(int paramInt) {
/*  644 */     if (paramInt == 254)
/*  645 */       return this.comMarker;
/*  646 */     if ((paramInt >= 224) && (paramInt <= 239))
/*      */     {
/*  648 */       return this.appMarkers[(paramInt - 224)];
/*      */     }
/*  650 */     throw new IllegalArgumentException("Invalid Marker ID:" + paramInt);
/*      */   }
/*      */ 
/*      */   public void setMarkerData(int paramInt, byte[][] paramArrayOfByte)
/*      */   {
/*  657 */     if (paramInt == 254)
/*  658 */       this.comMarker = paramArrayOfByte;
/*  659 */     else if ((paramInt >= 224) && (paramInt <= 239))
/*      */     {
/*  661 */       this.appMarkers[(paramInt - 224)] = paramArrayOfByte;
/*      */     }
/*  663 */     else throw new IllegalArgumentException("Invalid Marker ID:" + paramInt);
/*      */   }
/*      */ 
/*      */   public void addMarkerData(int paramInt, byte[] paramArrayOfByte)
/*      */   {
/*  669 */     if (paramArrayOfByte == null) {
/*  670 */       return;
/*      */     }
/*  672 */     if (paramInt == 254)
/*  673 */       this.comMarker = appendArray(this.comMarker, paramArrayOfByte);
/*  674 */     else if ((paramInt >= 224) && (paramInt <= 239))
/*      */     {
/*  676 */       this.appMarkers[(paramInt - 224)] = appendArray(this.appMarkers[(paramInt - 224)], paramArrayOfByte);
/*      */     }
/*      */     else
/*  679 */       throw new IllegalArgumentException("Invalid Marker ID:" + paramInt);
/*      */   }
/*      */ 
/*      */   public int getEncodedColorID()
/*      */   {
/*  690 */     return this.encodedColorID;
/*      */   }
/*      */ 
/*      */   public int getNumComponents()
/*      */   {
/*  697 */     return this.numComponents;
/*      */   }
/*      */ 
/*      */   public static int getNumComponents(int paramInt)
/*      */   {
/*  709 */     if ((paramInt < 0) || (paramInt >= 12)) {
/*  710 */       throw new IllegalArgumentException("Invalid JPEGColorID.");
/*      */     }
/*  712 */     return defComponents[paramInt];
/*      */   }
/*      */ 
/*      */   public int getRestartInterval()
/*      */   {
/*  719 */     return this.restartInterval;
/*      */   }
/*      */ 
/*      */   public void setRestartInterval(int paramInt)
/*      */   {
/*  726 */     this.restartInterval = paramInt;
/*      */   }
/*      */ 
/*      */   public int getDensityUnit()
/*      */   {
/*  736 */     if (!getMarker(224)) {
/*  737 */       throw new IllegalArgumentException("No APP0 marker present");
/*      */     }
/*  739 */     byte[] arrayOfByte = findAPP0();
/*  740 */     if (arrayOfByte == null) {
/*  741 */       throw new IllegalArgumentException("Can't understand APP0 marker that is present");
/*      */     }
/*  743 */     return arrayOfByte[7];
/*      */   }
/*      */ 
/*      */   public int getXDensity()
/*      */   {
/*  753 */     if (!getMarker(224)) {
/*  754 */       throw new IllegalArgumentException("No APP0 marker present");
/*      */     }
/*  756 */     byte[] arrayOfByte = findAPP0();
/*  757 */     if (arrayOfByte == null) {
/*  758 */       throw new IllegalArgumentException("Can't understand APP0 marker that is present");
/*      */     }
/*      */ 
/*  761 */     int i = arrayOfByte[8] << 8 | arrayOfByte[9] & 0xFF;
/*  762 */     return i;
/*      */   }
/*      */ 
/*      */   public int getYDensity()
/*      */   {
/*  772 */     if (!getMarker(224)) {
/*  773 */       throw new IllegalArgumentException("No APP0 marker present");
/*      */     }
/*  775 */     byte[] arrayOfByte = findAPP0();
/*  776 */     if (arrayOfByte == null) {
/*  777 */       throw new IllegalArgumentException("Can't understand APP0 marker that is present");
/*      */     }
/*      */ 
/*  780 */     int i = arrayOfByte[10] << 8 | arrayOfByte[11] & 0xFF;
/*  781 */     return i;
/*      */   }
/*      */ 
/*      */   public void setDensityUnit(int paramInt)
/*      */   {
/*  789 */     byte[] arrayOfByte = null;
/*      */ 
/*  791 */     if (!getMarker(224)) {
/*  792 */       arrayOfByte = createDefaultAPP0Marker();
/*  793 */       addMarkerData(224, arrayOfByte);
/*      */     } else {
/*  795 */       arrayOfByte = findAPP0();
/*  796 */       if (arrayOfByte == null) {
/*  797 */         throw new IllegalArgumentException("Can't understand APP0 marker that is present");
/*      */       }
/*      */     }
/*  800 */     arrayOfByte[7] = ((byte)paramInt);
/*      */   }
/*      */ 
/*      */   public void setXDensity(int paramInt)
/*      */   {
/*  809 */     byte[] arrayOfByte = null;
/*      */ 
/*  811 */     if (!getMarker(224)) {
/*  812 */       arrayOfByte = createDefaultAPP0Marker();
/*  813 */       addMarkerData(224, arrayOfByte);
/*      */     } else {
/*  815 */       arrayOfByte = findAPP0();
/*  816 */       if (arrayOfByte == null) {
/*  817 */         throw new IllegalArgumentException("Can't understand APP0 marker that is present");
/*      */       }
/*      */     }
/*  820 */     arrayOfByte[8] = ((byte)(paramInt >>> 8 & 0xFF));
/*  821 */     arrayOfByte[9] = ((byte)(paramInt & 0xFF));
/*      */   }
/*      */ 
/*      */   public void setYDensity(int paramInt)
/*      */   {
/*  830 */     byte[] arrayOfByte = null;
/*      */ 
/*  832 */     if (!getMarker(224)) {
/*  833 */       arrayOfByte = createDefaultAPP0Marker();
/*  834 */       addMarkerData(224, arrayOfByte);
/*      */     } else {
/*  836 */       arrayOfByte = findAPP0();
/*  837 */       if (arrayOfByte == null) {
/*  838 */         throw new IllegalArgumentException("Can't understand APP0 marker that is present");
/*      */       }
/*      */     }
/*  841 */     arrayOfByte[10] = ((byte)(paramInt >>> 8 & 0xFF));
/*  842 */     arrayOfByte[11] = ((byte)(paramInt & 0xFF));
/*      */   }
/*      */ 
/*      */   public void setQuality(float paramFloat, boolean paramBoolean)
/*      */   {
/*  870 */     double d = paramFloat;
/*      */ 
/*  873 */     if (d <= 0.01D) d = 0.01D;
/*  874 */     if (d > 1.0D) d = 1.0D;
/*      */ 
/*  883 */     if (d < 0.5D) d = 0.5D / d; else {
/*  884 */       d = 2.0D - d * 2.0D;
/*      */     }
/*  886 */     this.qTableMapping = new int[getNumComponents()];
/*  887 */     System.arraycopy(stdCompMapping[this.encodedColorID], 0, this.qTableMapping, 0, getNumComponents());
/*      */ 
/*  891 */     JPEGQTable localJPEGQTable = JPEGQTable.StdLuminance;
/*  892 */     this.qTables[0] = localJPEGQTable.getScaledInstance((float)d, paramBoolean);
/*      */ 
/*  894 */     localJPEGQTable = JPEGQTable.StdChrominance;
/*  895 */     this.qTables[1] = localJPEGQTable.getScaledInstance((float)d, paramBoolean);
/*  896 */     this.qTables[2] = null;
/*  897 */     this.qTables[3] = null;
/*      */   }
/*      */ 
/*      */   byte[] findAPP0()
/*      */   {
/*  904 */     byte[][] arrayOfByte = (byte[][])null;
/*      */ 
/*  906 */     arrayOfByte = getMarkerData(224);
/*  907 */     if (arrayOfByte == null) return null;
/*      */ 
/*  909 */     for (int i = 0; i < arrayOfByte.length; i++) {
/*  910 */       if ((arrayOfByte[i] != null) && (checkAPP0(arrayOfByte[i]))) {
/*  911 */         return arrayOfByte[i];
/*      */       }
/*      */     }
/*  914 */     return null;
/*      */   }
/*      */ 
/*      */   static boolean checkAPP0(byte[] paramArrayOfByte)
/*      */   {
/*  919 */     if (paramArrayOfByte.length < 14) return false;
/*  920 */     if ((paramArrayOfByte[0] != 74) || (paramArrayOfByte[1] != 70) || (paramArrayOfByte[2] != 73) || (paramArrayOfByte[3] != 70) || (paramArrayOfByte[4] != 0))
/*      */     {
/*  925 */       return false;
/*      */     }
/*      */ 
/*  928 */     if (paramArrayOfByte[5] < 1) return false;
/*      */ 
/*  933 */     return true;
/*      */   }
/*      */ 
/*      */   static byte[] createDefaultAPP0Marker()
/*      */   {
/*  940 */     byte[] arrayOfByte = new byte[14];
/*      */ 
/*  942 */     arrayOfByte[0] = 74;
/*  943 */     arrayOfByte[1] = 70;
/*  944 */     arrayOfByte[2] = 73;
/*  945 */     arrayOfByte[3] = 70;
/*  946 */     arrayOfByte[4] = 0;
/*      */ 
/*  948 */     arrayOfByte[5] = 1;
/*  949 */     arrayOfByte[6] = 1;
/*      */ 
/*  951 */     arrayOfByte[7] = 0;
/*      */ 
/*  953 */     arrayOfByte[8] = 0; arrayOfByte[9] = 1;
/*  954 */     arrayOfByte[10] = 0; arrayOfByte[11] = 1;
/*      */ 
/*  956 */     arrayOfByte[12] = 0; arrayOfByte[13] = 0;
/*  957 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   static byte[] copyArray(byte[] paramArrayOfByte) {
/*  961 */     if (paramArrayOfByte == null) return null;
/*  962 */     byte[] arrayOfByte = new byte[paramArrayOfByte.length];
/*  963 */     System.arraycopy(paramArrayOfByte, 0, arrayOfByte, 0, paramArrayOfByte.length);
/*  964 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   static byte[][] copyArrays(byte[][] paramArrayOfByte) {
/*  968 */     if (paramArrayOfByte == null) return (byte[][])null;
/*  969 */     byte[][] arrayOfByte = new byte[paramArrayOfByte.length][];
/*      */ 
/*  971 */     for (int i = 0; i < paramArrayOfByte.length; i++) {
/*  972 */       if (paramArrayOfByte[i] != null) arrayOfByte[i] = copyArray(paramArrayOfByte[i]);
/*      */     }
/*  974 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   static byte[][] appendArray(byte[][] paramArrayOfByte, byte[] paramArrayOfByte1) {
/*  978 */     int i = 0;
/*      */ 
/*  980 */     if (paramArrayOfByte != null) i = paramArrayOfByte.length;
/*      */ 
/*  982 */     byte[][] arrayOfByte = new byte[i + 1][];
/*  983 */     for (int j = 0; j < i; j++) {
/*  984 */       arrayOfByte[j] = paramArrayOfByte[j];
/*      */     }
/*  986 */     if (paramArrayOfByte1 != null)
/*  987 */       arrayOfByte[i] = copyArray(paramArrayOfByte1);
/*  988 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   static byte[][] buildArray(Vector paramVector) {
/*  992 */     if (paramVector == null) return (byte[][])null;
/*      */ 
/*  994 */     int i = 0;
/*  995 */     byte[][] arrayOfByte = new byte[paramVector.size()][];
/*  996 */     Enumeration localEnumeration = paramVector.elements();
/*  997 */     while (localEnumeration.hasMoreElements()) {
/*  998 */       byte[] arrayOfByte1 = (byte[])localEnumeration.nextElement();
/*  999 */       if (arrayOfByte1 != null) arrayOfByte[(i++)] = copyArray(arrayOfByte1);
/*      */     }
/* 1001 */     return arrayOfByte;
/*      */   }
/*      */ 
/*      */   public static int getDefaultColorId(ColorModel paramColorModel)
/*      */   {
/* 1016 */     boolean bool = paramColorModel.hasAlpha();
/* 1017 */     ColorSpace localColorSpace1 = paramColorModel.getColorSpace();
/* 1018 */     ColorSpace localColorSpace2 = null;
/* 1019 */     switch (localColorSpace1.getType()) {
/*      */     case 6:
/* 1021 */       return 1;
/*      */     case 5:
/* 1024 */       if (bool) {
/* 1025 */         return 7;
/*      */       }
/* 1027 */       return 3;
/*      */     case 3:
/* 1030 */       if (localColorSpace2 == null) {
/*      */         try {
/* 1032 */           localColorSpace2 = ColorSpace.getInstance(1002);
/*      */         }
/*      */         catch (IllegalArgumentException localIllegalArgumentException)
/*      */         {
/*      */         }
/*      */       }
/*      */ 
/* 1039 */       if (localColorSpace1 == localColorSpace2) {
/* 1040 */         return bool ? 10 : 5;
/*      */       }
/*      */ 
/* 1044 */       return bool ? 7 : 3;
/*      */     case 9:
/* 1049 */       return 4;
/*      */     case 4:
/*      */     case 7:
/* 1051 */     case 8: } return 0;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.image.codec.JPEGParam
 * JD-Core Version:    0.6.2
 */