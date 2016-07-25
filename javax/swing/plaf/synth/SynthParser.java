/*      */ package javax.swing.plaf.synth;
/*      */ 
/*      */ import com.sun.beans.decoder.DocumentHandler;
/*      */ import java.awt.Color;
/*      */ import java.awt.Component;
/*      */ import java.awt.Font;
/*      */ import java.awt.Graphics;
/*      */ import java.awt.Image;
/*      */ import java.awt.Insets;
/*      */ import java.awt.Toolkit;
/*      */ import java.io.IOException;
/*      */ import java.lang.reflect.Field;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URL;
/*      */ import java.net.URLClassLoader;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.regex.PatternSyntaxException;
/*      */ import javax.swing.ImageIcon;
/*      */ import javax.swing.UIDefaults.LazyInputMap;
/*      */ import javax.swing.plaf.ColorUIResource;
/*      */ import javax.swing.plaf.DimensionUIResource;
/*      */ import javax.swing.plaf.FontUIResource;
/*      */ import javax.swing.plaf.InsetsUIResource;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import org.xml.sax.Attributes;
/*      */ import org.xml.sax.InputSource;
/*      */ import org.xml.sax.Locator;
/*      */ import org.xml.sax.SAXException;
/*      */ import org.xml.sax.SAXParseException;
/*      */ import org.xml.sax.helpers.DefaultHandler;
/*      */ import sun.swing.plaf.synth.DefaultSynthStyle.StateInfo;
/*      */ 
/*      */ class SynthParser extends DefaultHandler
/*      */ {
/*      */   private static final String ELEMENT_SYNTH = "synth";
/*      */   private static final String ELEMENT_STYLE = "style";
/*      */   private static final String ELEMENT_STATE = "state";
/*      */   private static final String ELEMENT_FONT = "font";
/*      */   private static final String ELEMENT_COLOR = "color";
/*      */   private static final String ELEMENT_IMAGE_PAINTER = "imagePainter";
/*      */   private static final String ELEMENT_PAINTER = "painter";
/*      */   private static final String ELEMENT_PROPERTY = "property";
/*      */   private static final String ELEMENT_SYNTH_GRAPHICS = "graphicsUtils";
/*      */   private static final String ELEMENT_IMAGE_ICON = "imageIcon";
/*      */   private static final String ELEMENT_BIND = "bind";
/*      */   private static final String ELEMENT_BIND_KEY = "bindKey";
/*      */   private static final String ELEMENT_INSETS = "insets";
/*      */   private static final String ELEMENT_OPAQUE = "opaque";
/*      */   private static final String ELEMENT_DEFAULTS_PROPERTY = "defaultsProperty";
/*      */   private static final String ELEMENT_INPUT_MAP = "inputMap";
/*      */   private static final String ATTRIBUTE_ACTION = "action";
/*      */   private static final String ATTRIBUTE_ID = "id";
/*      */   private static final String ATTRIBUTE_IDREF = "idref";
/*      */   private static final String ATTRIBUTE_CLONE = "clone";
/*      */   private static final String ATTRIBUTE_VALUE = "value";
/*      */   private static final String ATTRIBUTE_NAME = "name";
/*      */   private static final String ATTRIBUTE_STYLE = "style";
/*      */   private static final String ATTRIBUTE_SIZE = "size";
/*      */   private static final String ATTRIBUTE_TYPE = "type";
/*      */   private static final String ATTRIBUTE_TOP = "top";
/*      */   private static final String ATTRIBUTE_LEFT = "left";
/*      */   private static final String ATTRIBUTE_BOTTOM = "bottom";
/*      */   private static final String ATTRIBUTE_RIGHT = "right";
/*      */   private static final String ATTRIBUTE_KEY = "key";
/*      */   private static final String ATTRIBUTE_SOURCE_INSETS = "sourceInsets";
/*      */   private static final String ATTRIBUTE_DEST_INSETS = "destinationInsets";
/*      */   private static final String ATTRIBUTE_PATH = "path";
/*      */   private static final String ATTRIBUTE_STRETCH = "stretch";
/*      */   private static final String ATTRIBUTE_PAINT_CENTER = "paintCenter";
/*      */   private static final String ATTRIBUTE_METHOD = "method";
/*      */   private static final String ATTRIBUTE_DIRECTION = "direction";
/*      */   private static final String ATTRIBUTE_CENTER = "center";
/*      */   private DocumentHandler _handler;
/*      */   private int _depth;
/*      */   private DefaultSynthStyleFactory _factory;
/*      */   private List<ParsedSynthStyle.StateInfo> _stateInfos;
/*      */   private ParsedSynthStyle _style;
/*      */   private ParsedSynthStyle.StateInfo _stateInfo;
/*      */   private List<String> _inputMapBindings;
/*      */   private String _inputMapID;
/*      */   private Map<String, Object> _mapping;
/*      */   private URL _urlResourceBase;
/*      */   private Class<?> _classResourceBase;
/*      */   private List<ColorType> _colorTypes;
/*      */   private Map<String, Object> _defaultsMap;
/*      */   private List<ParsedSynthStyle.PainterInfo> _stylePainters;
/*      */   private List<ParsedSynthStyle.PainterInfo> _statePainters;
/*      */ 
/*      */   SynthParser()
/*      */   {
/*  199 */     this._mapping = new HashMap();
/*  200 */     this._stateInfos = new ArrayList();
/*  201 */     this._colorTypes = new ArrayList();
/*  202 */     this._inputMapBindings = new ArrayList();
/*  203 */     this._stylePainters = new ArrayList();
/*  204 */     this._statePainters = new ArrayList(); } 
/*      */   // ERROR //
/*      */   public void parse(java.io.InputStream paramInputStream, DefaultSynthStyleFactory paramDefaultSynthStyleFactory, URL paramURL, Class<?> paramClass, Map<String, Object> paramMap) throws java.text.ParseException, java.lang.IllegalArgumentException { // Byte code:
/*      */     //   0: aload_1
/*      */     //   1: ifnull +16 -> 17
/*      */     //   4: aload_2
/*      */     //   5: ifnull +12 -> 17
/*      */     //   8: aload_3
/*      */     //   9: ifnonnull +18 -> 27
/*      */     //   12: aload 4
/*      */     //   14: ifnonnull +13 -> 27
/*      */     //   17: new 495	java/lang/IllegalArgumentException
/*      */     //   20: dup
/*      */     //   21: ldc 61
/*      */     //   23: invokespecial 813	java/lang/IllegalArgumentException:<init>	(Ljava/lang/String;)V
/*      */     //   26: athrow
/*      */     //   27: getstatic 769	javax/swing/plaf/synth/SynthParser:$assertionsDisabled	Z
/*      */     //   30: ifne +20 -> 50
/*      */     //   33: aload_3
/*      */     //   34: ifnull +16 -> 50
/*      */     //   37: aload 4
/*      */     //   39: ifnull +11 -> 50
/*      */     //   42: new 490	java/lang/AssertionError
/*      */     //   45: dup
/*      */     //   46: invokespecial 806	java/lang/AssertionError:<init>	()V
/*      */     //   49: athrow
/*      */     //   50: aload_0
/*      */     //   51: aload_2
/*      */     //   52: putfield 781	javax/swing/plaf/synth/SynthParser:_factory	Ljavax/swing/plaf/synth/DefaultSynthStyleFactory;
/*      */     //   55: aload_0
/*      */     //   56: aload 4
/*      */     //   58: putfield 771	javax/swing/plaf/synth/SynthParser:_classResourceBase	Ljava/lang/Class;
/*      */     //   61: aload_0
/*      */     //   62: aload_3
/*      */     //   63: putfield 773	javax/swing/plaf/synth/SynthParser:_urlResourceBase	Ljava/net/URL;
/*      */     //   66: aload_0
/*      */     //   67: aload 5
/*      */     //   69: putfield 779	javax/swing/plaf/synth/SynthParser:_defaultsMap	Ljava/util/Map;
/*      */     //   72: invokestatic 907	javax/xml/parsers/SAXParserFactory:newInstance	()Ljavax/xml/parsers/SAXParserFactory;
/*      */     //   75: invokevirtual 906	javax/xml/parsers/SAXParserFactory:newSAXParser	()Ljavax/xml/parsers/SAXParser;
/*      */     //   78: astore 6
/*      */     //   80: aload 6
/*      */     //   82: new 488	java/io/BufferedInputStream
/*      */     //   85: dup
/*      */     //   86: aload_1
/*      */     //   87: invokespecial 805	java/io/BufferedInputStream:<init>	(Ljava/io/InputStream;)V
/*      */     //   90: aload_0
/*      */     //   91: invokevirtual 905	javax/xml/parsers/SAXParser:parse	(Ljava/io/InputStream;Lorg/xml/sax/helpers/DefaultHandler;)V
/*      */     //   94: goto +109 -> 203
/*      */     //   97: astore 6
/*      */     //   99: new 510	java/text/ParseException
/*      */     //   102: dup
/*      */     //   103: new 502	java/lang/StringBuilder
/*      */     //   106: dup
/*      */     //   107: invokespecial 830	java/lang/StringBuilder:<init>	()V
/*      */     //   110: ldc 25
/*      */     //   112: invokevirtual 835	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   115: aload 6
/*      */     //   117: invokevirtual 834	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */     //   120: invokevirtual 831	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   123: iconst_0
/*      */     //   124: invokespecial 842	java/text/ParseException:<init>	(Ljava/lang/String;I)V
/*      */     //   127: athrow
/*      */     //   128: astore 6
/*      */     //   130: new 510	java/text/ParseException
/*      */     //   133: dup
/*      */     //   134: new 502	java/lang/StringBuilder
/*      */     //   137: dup
/*      */     //   138: invokespecial 830	java/lang/StringBuilder:<init>	()V
/*      */     //   141: ldc 25
/*      */     //   143: invokevirtual 835	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   146: aload 6
/*      */     //   148: invokevirtual 834	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */     //   151: ldc 1
/*      */     //   153: invokevirtual 835	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   156: aload 6
/*      */     //   158: invokevirtual 908	org/xml/sax/SAXException:getException	()Ljava/lang/Exception;
/*      */     //   161: invokevirtual 834	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */     //   164: invokevirtual 831	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   167: iconst_0
/*      */     //   168: invokespecial 842	java/text/ParseException:<init>	(Ljava/lang/String;I)V
/*      */     //   171: athrow
/*      */     //   172: astore 6
/*      */     //   174: new 510	java/text/ParseException
/*      */     //   177: dup
/*      */     //   178: new 502	java/lang/StringBuilder
/*      */     //   181: dup
/*      */     //   182: invokespecial 830	java/lang/StringBuilder:<init>	()V
/*      */     //   185: ldc 25
/*      */     //   187: invokevirtual 835	java/lang/StringBuilder:append	(Ljava/lang/String;)Ljava/lang/StringBuilder;
/*      */     //   190: aload 6
/*      */     //   192: invokevirtual 834	java/lang/StringBuilder:append	(Ljava/lang/Object;)Ljava/lang/StringBuilder;
/*      */     //   195: invokevirtual 831	java/lang/StringBuilder:toString	()Ljava/lang/String;
/*      */     //   198: iconst_0
/*      */     //   199: invokespecial 842	java/text/ParseException:<init>	(Ljava/lang/String;I)V
/*      */     //   202: athrow
/*      */     //   203: aload_0
/*      */     //   204: invokespecial 881	javax/swing/plaf/synth/SynthParser:reset	()V
/*      */     //   207: goto +12 -> 219
/*      */     //   210: astore 7
/*      */     //   212: aload_0
/*      */     //   213: invokespecial 881	javax/swing/plaf/synth/SynthParser:reset	()V
/*      */     //   216: aload 7
/*      */     //   218: athrow
/*      */     //   219: return
/*      */     //
/*      */     // Exception table:
/*      */     //   from	to	target	type
/*      */     //   72	94	97	javax/xml/parsers/ParserConfigurationException
/*      */     //   72	94	128	org/xml/sax/SAXException
/*      */     //   72	94	172	java/io/IOException
/*      */     //   72	203	210	finally
/*      */     //   210	212	210	finally } 
/*  261 */   private URL getResource(String paramString) { if (this._classResourceBase != null)
/*  262 */       return this._classResourceBase.getResource(paramString);
/*      */     try
/*      */     {
/*  265 */       return new URL(this._urlResourceBase, paramString); } catch (MalformedURLException localMalformedURLException) {
/*      */     }
/*  267 */     return null;
/*      */   }
/*      */ 
/*      */   private void reset()
/*      */   {
/*  276 */     this._handler = null;
/*  277 */     this._depth = 0;
/*  278 */     this._mapping.clear();
/*  279 */     this._stateInfos.clear();
/*  280 */     this._colorTypes.clear();
/*  281 */     this._statePainters.clear();
/*  282 */     this._stylePainters.clear();
/*      */   }
/*      */ 
/*      */   private boolean isForwarding()
/*      */   {
/*  289 */     return this._depth > 0;
/*      */   }
/*      */ 
/*      */   private DocumentHandler getHandler()
/*      */   {
/*      */     Object localObject1;
/*      */     Object localObject2;
/*  296 */     if (this._handler == null) {
/*  297 */       this._handler = new DocumentHandler();
/*  298 */       if (this._urlResourceBase != null)
/*      */       {
/*  305 */         localObject1 = new URL[] { getResource(".") };
/*  306 */         localObject2 = Thread.currentThread().getContextClassLoader();
/*  307 */         URLClassLoader localURLClassLoader = new URLClassLoader((URL[])localObject1, (ClassLoader)localObject2);
/*  308 */         this._handler.setClassLoader(localURLClassLoader);
/*      */       } else {
/*  310 */         this._handler.setClassLoader(this._classResourceBase.getClassLoader());
/*      */       }
/*      */ 
/*  313 */       for (localObject1 = this._mapping.keySet().iterator(); ((Iterator)localObject1).hasNext(); ) { localObject2 = (String)((Iterator)localObject1).next();
/*  314 */         this._handler.setVariable((String)localObject2, this._mapping.get(localObject2));
/*      */       }
/*      */     }
/*  317 */     return this._handler;
/*      */   }
/*      */ 
/*      */   private Object checkCast(Object paramObject, Class paramClass)
/*      */     throws SAXException
/*      */   {
/*  325 */     if (!paramClass.isInstance(paramObject)) {
/*  326 */       throw new SAXException("Expected type " + paramClass + " got " + paramObject.getClass());
/*      */     }
/*      */ 
/*  329 */     return paramObject;
/*      */   }
/*      */ 
/*      */   private Object lookup(String paramString, Class paramClass)
/*      */     throws SAXException
/*      */   {
/*  338 */     if ((this._handler != null) && 
/*  339 */       (this._handler.hasVariable(paramString))) {
/*  340 */       return checkCast(this._handler.getVariable(paramString), paramClass);
/*      */     }
/*      */ 
/*  343 */     Object localObject = this._mapping.get(paramString);
/*  344 */     if (localObject == null) {
/*  345 */       throw new SAXException("ID " + paramString + " has not been defined");
/*      */     }
/*  347 */     return checkCast(localObject, paramClass);
/*      */   }
/*      */ 
/*      */   private void register(String paramString, Object paramObject)
/*      */     throws SAXException
/*      */   {
/*  355 */     if (paramString != null) {
/*  356 */       if ((this._mapping.get(paramString) != null) || ((this._handler != null) && (this._handler.hasVariable(paramString))))
/*      */       {
/*  358 */         throw new SAXException("ID " + paramString + " is already defined");
/*      */       }
/*  360 */       if (this._handler != null) {
/*  361 */         this._handler.setVariable(paramString, paramObject);
/*      */       }
/*      */       else
/*  364 */         this._mapping.put(paramString, paramObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   private int nextInt(StringTokenizer paramStringTokenizer, String paramString)
/*      */     throws SAXException
/*      */   {
/*  375 */     if (!paramStringTokenizer.hasMoreTokens())
/*  376 */       throw new SAXException(paramString);
/*      */     try
/*      */     {
/*  379 */       return Integer.parseInt(paramStringTokenizer.nextToken()); } catch (NumberFormatException localNumberFormatException) {
/*      */     }
/*  381 */     throw new SAXException(paramString);
/*      */   }
/*      */ 
/*      */   private Insets parseInsets(String paramString1, String paramString2)
/*      */     throws SAXException
/*      */   {
/*  390 */     StringTokenizer localStringTokenizer = new StringTokenizer(paramString1);
/*  391 */     return new Insets(nextInt(localStringTokenizer, paramString2), nextInt(localStringTokenizer, paramString2), nextInt(localStringTokenizer, paramString2), nextInt(localStringTokenizer, paramString2));
/*      */   }
/*      */ 
/*      */   private void startStyle(Attributes paramAttributes)
/*      */     throws SAXException
/*      */   {
/*  404 */     String str1 = null;
/*      */ 
/*  406 */     this._style = null;
/*  407 */     for (int i = paramAttributes.getLength() - 1; i >= 0; i--) {
/*  408 */       String str2 = paramAttributes.getQName(i);
/*  409 */       if (str2.equals("clone")) {
/*  410 */         this._style = ((ParsedSynthStyle)((ParsedSynthStyle)lookup(paramAttributes.getValue(i), ParsedSynthStyle.class)).clone());
/*      */       }
/*  414 */       else if (str2.equals("id")) {
/*  415 */         str1 = paramAttributes.getValue(i);
/*      */       }
/*      */     }
/*  418 */     if (this._style == null) {
/*  419 */       this._style = new ParsedSynthStyle();
/*      */     }
/*  421 */     register(str1, this._style);
/*      */   }
/*      */ 
/*      */   private void endStyle() {
/*  425 */     int i = this._stylePainters.size();
/*  426 */     if (i > 0) {
/*  427 */       this._style.setPainters((ParsedSynthStyle.PainterInfo[])this._stylePainters.toArray(new ParsedSynthStyle.PainterInfo[i]));
/*  428 */       this._stylePainters.clear();
/*      */     }
/*  430 */     i = this._stateInfos.size();
/*  431 */     if (i > 0) {
/*  432 */       this._style.setStateInfo((DefaultSynthStyle.StateInfo[])this._stateInfos.toArray(new ParsedSynthStyle.StateInfo[i]));
/*  433 */       this._stateInfos.clear();
/*      */     }
/*  435 */     this._style = null;
/*      */   }
/*      */ 
/*      */   private void startState(Attributes paramAttributes) throws SAXException {
/*  439 */     Object localObject = null;
/*  440 */     int i = 0;
/*  441 */     String str1 = null;
/*      */ 
/*  443 */     this._stateInfo = null;
/*  444 */     for (int j = paramAttributes.getLength() - 1; j >= 0; j--) {
/*  445 */       String str2 = paramAttributes.getQName(j);
/*  446 */       if (str2.equals("id")) {
/*  447 */         str1 = paramAttributes.getValue(j);
/*      */       }
/*  449 */       else if (str2.equals("idref")) {
/*  450 */         this._stateInfo = ((ParsedSynthStyle.StateInfo)lookup(paramAttributes.getValue(j), ParsedSynthStyle.StateInfo.class));
/*      */       }
/*  453 */       else if (str2.equals("clone")) {
/*  454 */         this._stateInfo = ((ParsedSynthStyle.StateInfo)((ParsedSynthStyle.StateInfo)lookup(paramAttributes.getValue(j), ParsedSynthStyle.StateInfo.class)).clone());
/*      */       }
/*  458 */       else if (str2.equals("value")) {
/*  459 */         StringTokenizer localStringTokenizer = new StringTokenizer(paramAttributes.getValue(j));
/*      */ 
/*  461 */         while (localStringTokenizer.hasMoreTokens()) {
/*  462 */           String str3 = localStringTokenizer.nextToken().toUpperCase().intern();
/*      */ 
/*  464 */           if (str3 == "ENABLED") {
/*  465 */             i |= 1;
/*      */           }
/*  467 */           else if (str3 == "MOUSE_OVER") {
/*  468 */             i |= 2;
/*      */           }
/*  470 */           else if (str3 == "PRESSED") {
/*  471 */             i |= 4;
/*      */           }
/*  473 */           else if (str3 == "DISABLED") {
/*  474 */             i |= 8;
/*      */           }
/*  476 */           else if (str3 == "FOCUSED") {
/*  477 */             i |= 256;
/*      */           }
/*  479 */           else if (str3 == "SELECTED") {
/*  480 */             i |= 512;
/*      */           }
/*  482 */           else if (str3 == "DEFAULT") {
/*  483 */             i |= 1024;
/*      */           }
/*  485 */           else if (str3 != "AND") {
/*  486 */             throw new SAXException("Unknown state: " + i);
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  491 */     if (this._stateInfo == null) {
/*  492 */       this._stateInfo = new ParsedSynthStyle.StateInfo();
/*      */     }
/*  494 */     this._stateInfo.setComponentState(i);
/*  495 */     register(str1, this._stateInfo);
/*  496 */     this._stateInfos.add(this._stateInfo);
/*      */   }
/*      */ 
/*      */   private void endState() {
/*  500 */     int i = this._statePainters.size();
/*  501 */     if (i > 0) {
/*  502 */       this._stateInfo.setPainters((ParsedSynthStyle.PainterInfo[])this._statePainters.toArray(new ParsedSynthStyle.PainterInfo[i]));
/*  503 */       this._statePainters.clear();
/*      */     }
/*  505 */     this._stateInfo = null;
/*      */   }
/*      */ 
/*      */   private void startFont(Attributes paramAttributes) throws SAXException {
/*  509 */     Object localObject = null;
/*  510 */     int i = 0;
/*  511 */     int j = 0;
/*  512 */     String str1 = null;
/*  513 */     String str2 = null;
/*      */ 
/*  515 */     for (int k = paramAttributes.getLength() - 1; k >= 0; k--) {
/*  516 */       String str3 = paramAttributes.getQName(k);
/*  517 */       if (str3.equals("id")) {
/*  518 */         str1 = paramAttributes.getValue(k);
/*      */       }
/*  520 */       else if (str3.equals("idref")) {
/*  521 */         localObject = (Font)lookup(paramAttributes.getValue(k), Font.class);
/*      */       }
/*  523 */       else if (str3.equals("name")) {
/*  524 */         str2 = paramAttributes.getValue(k);
/*      */       }
/*  526 */       else if (str3.equals("size")) {
/*      */         try {
/*  528 */           j = Integer.parseInt(paramAttributes.getValue(k));
/*      */         } catch (NumberFormatException localNumberFormatException) {
/*  530 */           throw new SAXException("Invalid font size: " + paramAttributes.getValue(k));
/*      */         }
/*      */ 
/*      */       }
/*  534 */       else if (str3.equals("style")) {
/*  535 */         StringTokenizer localStringTokenizer = new StringTokenizer(paramAttributes.getValue(k));
/*      */ 
/*  537 */         while (localStringTokenizer.hasMoreTokens()) {
/*  538 */           String str4 = localStringTokenizer.nextToken().intern();
/*  539 */           if (str4 == "BOLD") {
/*  540 */             i = (i | 0x0) ^ 0x0 | 0x1;
/*      */           }
/*  543 */           else if (str4 == "ITALIC") {
/*  544 */             i |= 2;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  549 */     if (localObject == null) {
/*  550 */       if (str2 == null) {
/*  551 */         throw new SAXException("You must define a name for the font");
/*      */       }
/*  553 */       if (j == 0) {
/*  554 */         throw new SAXException("You must define a size for the font");
/*      */       }
/*  556 */       localObject = new FontUIResource(str2, i, j);
/*      */     }
/*  558 */     else if ((str2 != null) || (j != 0) || (i != 0)) {
/*  559 */       throw new SAXException("Name, size and style are not for use with idref");
/*      */     }
/*      */ 
/*  562 */     register(str1, localObject);
/*  563 */     if (this._stateInfo != null) {
/*  564 */       this._stateInfo.setFont((Font)localObject);
/*      */     }
/*  566 */     else if (this._style != null)
/*  567 */       this._style.setFont((Font)localObject);
/*      */   }
/*      */ 
/*      */   private void startColor(Attributes paramAttributes) throws SAXException
/*      */   {
/*  572 */     Object localObject1 = null;
/*  573 */     String str1 = null;
/*      */ 
/*  575 */     this._colorTypes.clear();
/*  576 */     for (int i = paramAttributes.getLength() - 1; i >= 0; i--) {
/*  577 */       String str2 = paramAttributes.getQName(i);
/*  578 */       if (str2.equals("id")) {
/*  579 */         str1 = paramAttributes.getValue(i);
/*      */       }
/*  581 */       else if (str2.equals("idref")) {
/*  582 */         localObject1 = (Color)lookup(paramAttributes.getValue(i), Color.class);
/*      */       }
/*  584 */       else if (!str2.equals("name"))
/*      */       {
/*      */         Object localObject3;
/*  586 */         if (str2.equals("value")) {
/*  587 */           localObject3 = paramAttributes.getValue(i);
/*      */ 
/*  589 */           if (((String)localObject3).startsWith("#"))
/*      */           {
/*      */             try
/*      */             {
/*  594 */               int i2 = ((String)localObject3).length();
/*      */               int n;
/*      */               boolean bool;
/*  595 */               if (i2 < 8)
/*      */               {
/*  597 */                 n = Integer.decode((String)localObject3).intValue();
/*  598 */                 bool = false;
/*  599 */               } else if (i2 == 8)
/*      */               {
/*  601 */                 n = Integer.decode((String)localObject3).intValue();
/*  602 */                 bool = true;
/*  603 */               } else if (i2 == 9)
/*      */               {
/*  611 */                 int i3 = Integer.decode('#' + ((String)localObject3).substring(3, 9)).intValue();
/*      */ 
/*  613 */                 int i4 = Integer.decode(((String)localObject3).substring(0, 3)).intValue();
/*  614 */                 n = i4 << 24 | i3;
/*  615 */                 bool = true;
/*      */               } else {
/*  617 */                 throw new SAXException("Invalid Color value: " + (String)localObject3);
/*      */               }
/*      */ 
/*  621 */               localObject1 = new ColorUIResource(new Color(n, bool));
/*      */             } catch (NumberFormatException localNumberFormatException) {
/*  623 */               throw new SAXException("Invalid Color value: " + (String)localObject3);
/*      */             }
/*      */           }
/*      */           else {
/*      */             try {
/*  628 */               localObject1 = new ColorUIResource((Color)Color.class.getField(((String)localObject3).toUpperCase()).get(Color.class));
/*      */             }
/*      */             catch (NoSuchFieldException localNoSuchFieldException1) {
/*  631 */               throw new SAXException("Invalid color name: " + (String)localObject3);
/*      */             } catch (IllegalAccessException localIllegalAccessException1) {
/*  633 */               throw new SAXException("Invalid color name: " + (String)localObject3);
/*      */             }
/*      */           }
/*      */         }
/*  637 */         else if (str2.equals("type")) {
/*  638 */           localObject3 = new StringTokenizer(paramAttributes.getValue(i));
/*      */ 
/*  640 */           while (((StringTokenizer)localObject3).hasMoreTokens()) {
/*  641 */             String str3 = ((StringTokenizer)localObject3).nextToken();
/*  642 */             int i1 = str3.lastIndexOf('.');
/*      */             Object localObject4;
/*  645 */             if (i1 == -1) {
/*  646 */               localObject4 = ColorType.class;
/*  647 */               i1 = 0;
/*      */             }
/*      */             else {
/*      */               try {
/*  651 */                 localObject4 = Class.forName(str3.substring(0, i1));
/*      */               }
/*      */               catch (ClassNotFoundException localClassNotFoundException) {
/*  654 */                 throw new SAXException("Unknown class: " + str3.substring(0, i1));
/*      */               }
/*      */ 
/*  657 */               i1++;
/*      */             }
/*      */             try {
/*  660 */               this._colorTypes.add((ColorType)checkCast(((Class)localObject4).getField(str3.substring(i1)).get(localObject4), ColorType.class));
/*      */             }
/*      */             catch (NoSuchFieldException localNoSuchFieldException2)
/*      */             {
/*  664 */               throw new SAXException("Unable to find color type: " + str3);
/*      */             }
/*      */             catch (IllegalAccessException localIllegalAccessException2) {
/*  667 */               throw new SAXException("Unable to find color type: " + str3);
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*  673 */     if (localObject1 == null) {
/*  674 */       throw new SAXException("color: you must specificy a value");
/*      */     }
/*  676 */     register(str1, localObject1);
/*  677 */     if ((this._stateInfo != null) && (this._colorTypes.size() > 0)) {
/*  678 */       Object localObject2 = this._stateInfo.getColors();
/*  679 */       int j = 0;
/*  680 */       for (int k = this._colorTypes.size() - 1; k >= 0; 
/*  681 */         k--) {
/*  682 */         j = Math.max(j, ((ColorType)this._colorTypes.get(k)).getID());
/*      */       }
/*  684 */       if ((localObject2 == null) || (localObject2.length <= j)) {
/*  685 */         Color[] arrayOfColor = new Color[j + 1];
/*  686 */         if (localObject2 != null) {
/*  687 */           System.arraycopy(localObject2, 0, arrayOfColor, 0, localObject2.length);
/*      */         }
/*  689 */         localObject2 = arrayOfColor;
/*      */       }
/*  691 */       for (int m = this._colorTypes.size() - 1; m >= 0; 
/*  692 */         m--) {
/*  693 */         localObject2[((ColorType)this._colorTypes.get(m)).getID()] = localObject1;
/*      */       }
/*  695 */       this._stateInfo.setColors((Color[])localObject2);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void startProperty(Attributes paramAttributes, Object paramObject) throws SAXException
/*      */   {
/*  701 */     Object localObject = null;
/*  702 */     String str1 = null;
/*      */ 
/*  705 */     int i = 0;
/*  706 */     String str2 = null;
/*      */ 
/*  708 */     for (int j = paramAttributes.getLength() - 1; j >= 0; j--) {
/*  709 */       String str3 = paramAttributes.getQName(j);
/*  710 */       if (str3.equals("type")) {
/*  711 */         String str4 = paramAttributes.getValue(j).toUpperCase();
/*  712 */         if (str4.equals("IDREF")) {
/*  713 */           i = 0;
/*      */         }
/*  715 */         else if (str4.equals("BOOLEAN")) {
/*  716 */           i = 1;
/*      */         }
/*  718 */         else if (str4.equals("DIMENSION")) {
/*  719 */           i = 2;
/*      */         }
/*  721 */         else if (str4.equals("INSETS")) {
/*  722 */           i = 3;
/*      */         }
/*  724 */         else if (str4.equals("INTEGER")) {
/*  725 */           i = 4;
/*      */         }
/*  727 */         else if (str4.equals("STRING")) {
/*  728 */           i = 5;
/*      */         }
/*      */         else {
/*  731 */           throw new SAXException(paramObject + " unknown type, use" + "idref, boolean, dimension, insets or integer");
/*      */         }
/*      */ 
/*      */       }
/*  735 */       else if (str3.equals("value")) {
/*  736 */         str2 = paramAttributes.getValue(j);
/*      */       }
/*  738 */       else if (str3.equals("key")) {
/*  739 */         str1 = paramAttributes.getValue(j);
/*      */       }
/*      */     }
/*  742 */     if (str2 != null) {
/*  743 */       switch (i) {
/*      */       case 0:
/*  745 */         localObject = lookup(str2, Object.class);
/*  746 */         break;
/*      */       case 1:
/*  748 */         if (str2.toUpperCase().equals("TRUE")) {
/*  749 */           localObject = Boolean.TRUE;
/*      */         }
/*      */         else {
/*  752 */           localObject = Boolean.FALSE;
/*      */         }
/*  754 */         break;
/*      */       case 2:
/*  756 */         StringTokenizer localStringTokenizer = new StringTokenizer(str2);
/*  757 */         localObject = new DimensionUIResource(nextInt(localStringTokenizer, "Invalid dimension"), nextInt(localStringTokenizer, "Invalid dimension"));
/*      */ 
/*  760 */         break;
/*      */       case 3:
/*  762 */         localObject = parseInsets(str2, paramObject + " invalid insets");
/*  763 */         break;
/*      */       case 4:
/*      */         try {
/*  766 */           localObject = new Integer(Integer.parseInt(str2));
/*      */         } catch (NumberFormatException localNumberFormatException) {
/*  768 */           throw new SAXException(paramObject + " invalid value");
/*      */         }
/*      */ 
/*      */       case 5:
/*  772 */         localObject = str2;
/*      */       }
/*      */     }
/*      */ 
/*  776 */     if ((localObject == null) || (str1 == null)) {
/*  777 */       throw new SAXException(paramObject + ": you must supply a " + "key and value");
/*      */     }
/*      */ 
/*  780 */     if (paramObject == "defaultsProperty") {
/*  781 */       this._defaultsMap.put(str1, localObject);
/*      */     }
/*  783 */     else if (this._stateInfo != null) {
/*  784 */       if (this._stateInfo.getData() == null) {
/*  785 */         this._stateInfo.setData(new HashMap());
/*      */       }
/*  787 */       this._stateInfo.getData().put(str1, localObject);
/*      */     }
/*  789 */     else if (this._style != null) {
/*  790 */       if (this._style.getData() == null) {
/*  791 */         this._style.setData(new HashMap());
/*      */       }
/*  793 */       this._style.getData().put(str1, localObject);
/*      */     }
/*      */   }
/*      */ 
/*      */   private void startGraphics(Attributes paramAttributes) throws SAXException {
/*  798 */     SynthGraphicsUtils localSynthGraphicsUtils = null;
/*      */ 
/*  800 */     for (int i = paramAttributes.getLength() - 1; i >= 0; i--) {
/*  801 */       String str = paramAttributes.getQName(i);
/*  802 */       if (str.equals("idref")) {
/*  803 */         localSynthGraphicsUtils = (SynthGraphicsUtils)lookup(paramAttributes.getValue(i), SynthGraphicsUtils.class);
/*      */       }
/*      */     }
/*      */ 
/*  807 */     if (localSynthGraphicsUtils == null) {
/*  808 */       throw new SAXException("graphicsUtils: you must supply an idref");
/*      */     }
/*  810 */     if (this._style != null)
/*  811 */       this._style.setGraphicsUtils(localSynthGraphicsUtils);
/*      */   }
/*      */ 
/*      */   private void startInsets(Attributes paramAttributes) throws SAXException
/*      */   {
/*  816 */     int i = 0;
/*  817 */     int j = 0;
/*  818 */     int k = 0;
/*  819 */     int m = 0;
/*  820 */     Object localObject = null;
/*  821 */     String str1 = null;
/*      */ 
/*  823 */     for (int n = paramAttributes.getLength() - 1; n >= 0; n--) {
/*  824 */       String str2 = paramAttributes.getQName(n);
/*      */       try
/*      */       {
/*  827 */         if (str2.equals("idref")) {
/*  828 */           localObject = (Insets)lookup(paramAttributes.getValue(n), Insets.class);
/*      */         }
/*  831 */         else if (str2.equals("id")) {
/*  832 */           str1 = paramAttributes.getValue(n);
/*      */         }
/*  834 */         else if (str2.equals("top")) {
/*  835 */           i = Integer.parseInt(paramAttributes.getValue(n));
/*      */         }
/*  837 */         else if (str2.equals("left")) {
/*  838 */           k = Integer.parseInt(paramAttributes.getValue(n));
/*      */         }
/*  840 */         else if (str2.equals("bottom")) {
/*  841 */           j = Integer.parseInt(paramAttributes.getValue(n));
/*      */         }
/*  843 */         else if (str2.equals("right"))
/*  844 */           m = Integer.parseInt(paramAttributes.getValue(n));
/*      */       }
/*      */       catch (NumberFormatException localNumberFormatException) {
/*  847 */         throw new SAXException("insets: bad integer value for " + paramAttributes.getValue(n));
/*      */       }
/*      */     }
/*      */ 
/*  851 */     if (localObject == null) {
/*  852 */       localObject = new InsetsUIResource(i, k, j, m);
/*      */     }
/*  854 */     register(str1, localObject);
/*  855 */     if (this._style != null)
/*  856 */       this._style.setInsets((Insets)localObject);
/*      */   }
/*      */ 
/*      */   private void startBind(Attributes paramAttributes) throws SAXException
/*      */   {
/*  861 */     ParsedSynthStyle localParsedSynthStyle = null;
/*  862 */     String str1 = null;
/*  863 */     int i = -1;
/*      */ 
/*  865 */     for (int j = paramAttributes.getLength() - 1; j >= 0; j--) {
/*  866 */       String str2 = paramAttributes.getQName(j);
/*      */ 
/*  868 */       if (str2.equals("style")) {
/*  869 */         localParsedSynthStyle = (ParsedSynthStyle)lookup(paramAttributes.getValue(j), ParsedSynthStyle.class);
/*      */       }
/*  872 */       else if (str2.equals("type")) {
/*  873 */         String str3 = paramAttributes.getValue(j).toUpperCase();
/*      */ 
/*  875 */         if (str3.equals("NAME")) {
/*  876 */           i = 0;
/*      */         }
/*  878 */         else if (str3.equals("REGION")) {
/*  879 */           i = 1;
/*      */         }
/*      */         else {
/*  882 */           throw new SAXException("bind: unknown type " + str3);
/*      */         }
/*      */       }
/*  885 */       else if (str2.equals("key")) {
/*  886 */         str1 = paramAttributes.getValue(j);
/*      */       }
/*      */     }
/*  889 */     if ((localParsedSynthStyle == null) || (str1 == null) || (i == -1)) {
/*  890 */       throw new SAXException("bind: you must specify a style, type and key");
/*      */     }
/*      */     try
/*      */     {
/*  894 */       this._factory.addStyle(localParsedSynthStyle, str1, i);
/*      */     } catch (PatternSyntaxException localPatternSyntaxException) {
/*  896 */       throw new SAXException("bind: " + str1 + " is not a valid " + "regular expression");
/*      */     }
/*      */   }
/*      */ 
/*      */   private void startPainter(Attributes paramAttributes, String paramString) throws SAXException
/*      */   {
/*  902 */     Insets localInsets1 = null;
/*  903 */     Insets localInsets2 = null;
/*  904 */     Object localObject1 = null;
/*  905 */     boolean bool1 = true;
/*  906 */     boolean bool2 = true;
/*  907 */     Object localObject2 = null;
/*  908 */     String str1 = null;
/*  909 */     Object localObject3 = null;
/*  910 */     int i = -1;
/*  911 */     boolean bool3 = false;
/*      */ 
/*  913 */     int j = 0;
/*  914 */     int k = 0;
/*      */ 
/*  916 */     for (int m = paramAttributes.getLength() - 1; m >= 0; m--) {
/*  917 */       String str2 = paramAttributes.getQName(m);
/*  918 */       String str3 = paramAttributes.getValue(m);
/*      */ 
/*  920 */       if (str2.equals("id")) {
/*  921 */         localObject3 = str3;
/*      */       }
/*  923 */       else if (str2.equals("method")) {
/*  924 */         str1 = str3.toLowerCase(Locale.ENGLISH);
/*      */       }
/*  926 */       else if (str2.equals("idref")) {
/*  927 */         localObject2 = (SynthPainter)lookup(str3, SynthPainter.class);
/*      */       }
/*  929 */       else if (str2.equals("path")) {
/*  930 */         localObject1 = str3;
/*      */       }
/*  932 */       else if (str2.equals("sourceInsets")) {
/*  933 */         localInsets1 = parseInsets(str3, paramString + ": sourceInsets must be top left bottom right");
/*      */       }
/*  936 */       else if (str2.equals("destinationInsets")) {
/*  937 */         localInsets2 = parseInsets(str3, paramString + ": destinationInsets must be top left bottom right");
/*      */       }
/*  940 */       else if (str2.equals("paintCenter")) {
/*  941 */         bool1 = str3.toLowerCase().equals("true");
/*  942 */         k = 1;
/*      */       }
/*  944 */       else if (str2.equals("stretch")) {
/*  945 */         bool2 = str3.toLowerCase().equals("true");
/*  946 */         j = 1;
/*      */       }
/*  948 */       else if (str2.equals("direction")) {
/*  949 */         str3 = str3.toUpperCase().intern();
/*  950 */         if (str3 == "EAST") {
/*  951 */           i = 3;
/*      */         }
/*  953 */         else if (str3 == "NORTH") {
/*  954 */           i = 1;
/*      */         }
/*  956 */         else if (str3 == "SOUTH") {
/*  957 */           i = 5;
/*      */         }
/*  959 */         else if (str3 == "WEST") {
/*  960 */           i = 7;
/*      */         }
/*  962 */         else if (str3 == "TOP") {
/*  963 */           i = 1;
/*      */         }
/*  965 */         else if (str3 == "LEFT") {
/*  966 */           i = 2;
/*      */         }
/*  968 */         else if (str3 == "BOTTOM") {
/*  969 */           i = 3;
/*      */         }
/*  971 */         else if (str3 == "RIGHT") {
/*  972 */           i = 4;
/*      */         }
/*  974 */         else if (str3 == "HORIZONTAL") {
/*  975 */           i = 0;
/*      */         }
/*  977 */         else if (str3 == "VERTICAL") {
/*  978 */           i = 1;
/*      */         }
/*  980 */         else if (str3 == "HORIZONTAL_SPLIT") {
/*  981 */           i = 1;
/*      */         }
/*  983 */         else if (str3 == "VERTICAL_SPLIT") {
/*  984 */           i = 0;
/*      */         }
/*      */         else {
/*  987 */           throw new SAXException(paramString + ": unknown direction");
/*      */         }
/*      */       }
/*  990 */       else if (str2.equals("center")) {
/*  991 */         bool3 = str3.toLowerCase().equals("true");
/*      */       }
/*      */     }
/*  994 */     if (localObject2 == null) {
/*  995 */       if (paramString == "painter") {
/*  996 */         throw new SAXException(paramString + ": you must specify an idref");
/*      */       }
/*      */ 
/*  999 */       if ((localInsets1 == null) && (!bool3)) {
/* 1000 */         throw new SAXException("property: you must specify sourceInsets");
/*      */       }
/*      */ 
/* 1003 */       if (localObject1 == null) {
/* 1004 */         throw new SAXException("property: you must specify a path");
/*      */       }
/* 1006 */       if ((bool3) && ((localInsets1 != null) || (localInsets2 != null) || (k != 0) || (j != 0)))
/*      */       {
/* 1008 */         throw new SAXException("The attributes: sourceInsets, destinationInsets, paintCenter and stretch  are not legal when center is true");
/*      */       }
/*      */ 
/* 1012 */       localObject2 = new ImagePainter(!bool2, bool1, localInsets1, localInsets2, getResource(localObject1), bool3);
/*      */     }
/*      */ 
/* 1015 */     register(localObject3, localObject2);
/* 1016 */     if (this._stateInfo != null) {
/* 1017 */       addPainterOrMerge(this._statePainters, str1, (SynthPainter)localObject2, i);
/*      */     }
/* 1019 */     else if (this._style != null)
/* 1020 */       addPainterOrMerge(this._stylePainters, str1, (SynthPainter)localObject2, i);
/*      */   }
/*      */ 
/*      */   private void addPainterOrMerge(List<ParsedSynthStyle.PainterInfo> paramList, String paramString, SynthPainter paramSynthPainter, int paramInt)
/*      */   {
/* 1027 */     ParsedSynthStyle.PainterInfo localPainterInfo1 = new ParsedSynthStyle.PainterInfo(paramString, paramSynthPainter, paramInt);
/*      */ 
/* 1031 */     for (Iterator localIterator = paramList.iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/*      */ 
/* 1033 */       ParsedSynthStyle.PainterInfo localPainterInfo2 = (ParsedSynthStyle.PainterInfo)localObject;
/*      */ 
/* 1035 */       if (localPainterInfo1.equalsPainter(localPainterInfo2)) {
/* 1036 */         localPainterInfo2.addPainter(paramSynthPainter);
/* 1037 */         return;
/*      */       }
/*      */     }
/*      */ 
/* 1041 */     paramList.add(localPainterInfo1);
/*      */   }
/*      */ 
/*      */   private void startImageIcon(Attributes paramAttributes) throws SAXException {
/* 1045 */     String str1 = null;
/* 1046 */     String str2 = null;
/*      */ 
/* 1048 */     for (int i = paramAttributes.getLength() - 1; i >= 0; i--) {
/* 1049 */       String str3 = paramAttributes.getQName(i);
/*      */ 
/* 1051 */       if (str3.equals("id")) {
/* 1052 */         str2 = paramAttributes.getValue(i);
/*      */       }
/* 1054 */       else if (str3.equals("path")) {
/* 1055 */         str1 = paramAttributes.getValue(i);
/*      */       }
/*      */     }
/* 1058 */     if (str1 == null) {
/* 1059 */       throw new SAXException("imageIcon: you must specify a path");
/*      */     }
/* 1061 */     register(str2, new LazyImageIcon(getResource(str1)));
/*      */   }
/*      */ 
/*      */   private void startOpaque(Attributes paramAttributes) {
/* 1065 */     if (this._style != null) {
/* 1066 */       this._style.setOpaque(true);
/* 1067 */       for (int i = paramAttributes.getLength() - 1; i >= 0; i--) {
/* 1068 */         String str = paramAttributes.getQName(i);
/*      */ 
/* 1070 */         if (str.equals("value"))
/* 1071 */           this._style.setOpaque("true".equals(paramAttributes.getValue(i).toLowerCase()));
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   private void startInputMap(Attributes paramAttributes)
/*      */     throws SAXException
/*      */   {
/* 1079 */     this._inputMapBindings.clear();
/* 1080 */     this._inputMapID = null;
/* 1081 */     if (this._style != null)
/* 1082 */       for (int i = paramAttributes.getLength() - 1; i >= 0; i--) {
/* 1083 */         String str = paramAttributes.getQName(i);
/*      */ 
/* 1085 */         if (str.equals("id"))
/* 1086 */           this._inputMapID = paramAttributes.getValue(i);
/*      */       }
/*      */   }
/*      */ 
/*      */   private void endInputMap()
/*      */     throws SAXException
/*      */   {
/* 1093 */     if (this._inputMapID != null) {
/* 1094 */       register(this._inputMapID, new UIDefaults.LazyInputMap(this._inputMapBindings.toArray(new Object[this._inputMapBindings.size()])));
/*      */     }
/*      */ 
/* 1098 */     this._inputMapBindings.clear();
/* 1099 */     this._inputMapID = null;
/*      */   }
/*      */ 
/*      */   private void startBindKey(Attributes paramAttributes) throws SAXException {
/* 1103 */     if (this._inputMapID == null)
/*      */     {
/* 1105 */       return;
/*      */     }
/* 1107 */     if (this._style != null) {
/* 1108 */       String str1 = null;
/* 1109 */       String str2 = null;
/* 1110 */       for (int i = paramAttributes.getLength() - 1; i >= 0; i--) {
/* 1111 */         String str3 = paramAttributes.getQName(i);
/*      */ 
/* 1113 */         if (str3.equals("key")) {
/* 1114 */           str1 = paramAttributes.getValue(i);
/*      */         }
/* 1116 */         else if (str3.equals("action")) {
/* 1117 */           str2 = paramAttributes.getValue(i);
/*      */         }
/*      */       }
/* 1120 */       if ((str1 == null) || (str2 == null)) {
/* 1121 */         throw new SAXException("bindKey: you must supply a key and action");
/*      */       }
/*      */ 
/* 1124 */       this._inputMapBindings.add(str1);
/* 1125 */       this._inputMapBindings.add(str2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public InputSource resolveEntity(String paramString1, String paramString2)
/*      */     throws IOException, SAXException
/*      */   {
/* 1136 */     if (isForwarding()) {
/* 1137 */       return getHandler().resolveEntity(paramString1, paramString2);
/*      */     }
/* 1139 */     return null;
/*      */   }
/*      */ 
/*      */   public void notationDecl(String paramString1, String paramString2, String paramString3) throws SAXException {
/* 1143 */     if (isForwarding())
/* 1144 */       getHandler().notationDecl(paramString1, paramString2, paramString3);
/*      */   }
/*      */ 
/*      */   public void unparsedEntityDecl(String paramString1, String paramString2, String paramString3, String paramString4)
/*      */     throws SAXException
/*      */   {
/* 1150 */     if (isForwarding())
/* 1151 */       getHandler().unparsedEntityDecl(paramString1, paramString2, paramString3, paramString4);
/*      */   }
/*      */ 
/*      */   public void setDocumentLocator(Locator paramLocator)
/*      */   {
/* 1157 */     if (isForwarding())
/* 1158 */       getHandler().setDocumentLocator(paramLocator);
/*      */   }
/*      */ 
/*      */   public void startDocument() throws SAXException
/*      */   {
/* 1163 */     if (isForwarding())
/* 1164 */       getHandler().startDocument();
/*      */   }
/*      */ 
/*      */   public void endDocument() throws SAXException
/*      */   {
/* 1169 */     if (isForwarding())
/* 1170 */       getHandler().endDocument();
/*      */   }
/*      */ 
/*      */   public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
/*      */     throws SAXException
/*      */   {
/* 1176 */     paramString3 = paramString3.intern();
/* 1177 */     if (paramString3 == "style") {
/* 1178 */       startStyle(paramAttributes);
/*      */     }
/* 1180 */     else if (paramString3 == "state") {
/* 1181 */       startState(paramAttributes);
/*      */     }
/* 1183 */     else if (paramString3 == "font") {
/* 1184 */       startFont(paramAttributes);
/*      */     }
/* 1186 */     else if (paramString3 == "color") {
/* 1187 */       startColor(paramAttributes);
/*      */     }
/* 1189 */     else if (paramString3 == "painter") {
/* 1190 */       startPainter(paramAttributes, paramString3);
/*      */     }
/* 1192 */     else if (paramString3 == "imagePainter") {
/* 1193 */       startPainter(paramAttributes, paramString3);
/*      */     }
/* 1195 */     else if (paramString3 == "property") {
/* 1196 */       startProperty(paramAttributes, "property");
/*      */     }
/* 1198 */     else if (paramString3 == "defaultsProperty") {
/* 1199 */       startProperty(paramAttributes, "defaultsProperty");
/*      */     }
/* 1201 */     else if (paramString3 == "graphicsUtils") {
/* 1202 */       startGraphics(paramAttributes);
/*      */     }
/* 1204 */     else if (paramString3 == "insets") {
/* 1205 */       startInsets(paramAttributes);
/*      */     }
/* 1207 */     else if (paramString3 == "bind") {
/* 1208 */       startBind(paramAttributes);
/*      */     }
/* 1210 */     else if (paramString3 == "bindKey") {
/* 1211 */       startBindKey(paramAttributes);
/*      */     }
/* 1213 */     else if (paramString3 == "imageIcon") {
/* 1214 */       startImageIcon(paramAttributes);
/*      */     }
/* 1216 */     else if (paramString3 == "opaque") {
/* 1217 */       startOpaque(paramAttributes);
/*      */     }
/* 1219 */     else if (paramString3 == "inputMap") {
/* 1220 */       startInputMap(paramAttributes);
/*      */     }
/* 1222 */     else if (paramString3 != "synth") {
/* 1223 */       if (this._depth++ == 0) {
/* 1224 */         getHandler().startDocument();
/*      */       }
/* 1226 */       getHandler().startElement(paramString1, paramString2, paramString3, paramAttributes);
/*      */     }
/*      */   }
/*      */ 
/*      */   public void endElement(String paramString1, String paramString2, String paramString3) throws SAXException {
/* 1231 */     if (isForwarding()) {
/* 1232 */       getHandler().endElement(paramString1, paramString2, paramString3);
/* 1233 */       this._depth -= 1;
/* 1234 */       if (!isForwarding())
/* 1235 */         getHandler().startDocument();
/*      */     }
/*      */     else
/*      */     {
/* 1239 */       paramString3 = paramString3.intern();
/* 1240 */       if (paramString3 == "style") {
/* 1241 */         endStyle();
/*      */       }
/* 1243 */       else if (paramString3 == "state") {
/* 1244 */         endState();
/*      */       }
/* 1246 */       else if (paramString3 == "inputMap")
/* 1247 */         endInputMap();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */     throws SAXException
/*      */   {
/* 1254 */     if (isForwarding())
/* 1255 */       getHandler().characters(paramArrayOfChar, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void ignorableWhitespace(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */     throws SAXException
/*      */   {
/* 1261 */     if (isForwarding())
/* 1262 */       getHandler().ignorableWhitespace(paramArrayOfChar, paramInt1, paramInt2);
/*      */   }
/*      */ 
/*      */   public void processingInstruction(String paramString1, String paramString2)
/*      */     throws SAXException
/*      */   {
/* 1268 */     if (isForwarding())
/* 1269 */       getHandler().processingInstruction(paramString1, paramString2);
/*      */   }
/*      */ 
/*      */   public void warning(SAXParseException paramSAXParseException) throws SAXException
/*      */   {
/* 1274 */     if (isForwarding())
/* 1275 */       getHandler().warning(paramSAXParseException);
/*      */   }
/*      */ 
/*      */   public void error(SAXParseException paramSAXParseException) throws SAXException
/*      */   {
/* 1280 */     if (isForwarding())
/* 1281 */       getHandler().error(paramSAXParseException);
/*      */   }
/*      */ 
/*      */   public void fatalError(SAXParseException paramSAXParseException)
/*      */     throws SAXException
/*      */   {
/* 1287 */     if (isForwarding()) {
/* 1288 */       getHandler().fatalError(paramSAXParseException);
/*      */     }
/* 1290 */     throw paramSAXParseException;
/*      */   }
/*      */ 
/*      */   private static class LazyImageIcon extends ImageIcon
/*      */     implements UIResource
/*      */   {
/*      */     private URL location;
/*      */ 
/*      */     public LazyImageIcon(URL paramURL)
/*      */     {
/* 1302 */       this.location = paramURL;
/*      */     }
/*      */ 
/*      */     public void paintIcon(Component paramComponent, Graphics paramGraphics, int paramInt1, int paramInt2) {
/* 1306 */       if (getImage() != null)
/* 1307 */         super.paintIcon(paramComponent, paramGraphics, paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     public int getIconWidth()
/*      */     {
/* 1312 */       if (getImage() != null) {
/* 1313 */         return super.getIconWidth();
/*      */       }
/* 1315 */       return 0;
/*      */     }
/*      */ 
/*      */     public int getIconHeight() {
/* 1319 */       if (getImage() != null) {
/* 1320 */         return super.getIconHeight();
/*      */       }
/* 1322 */       return 0;
/*      */     }
/*      */ 
/*      */     public Image getImage() {
/* 1326 */       if (this.location != null) {
/* 1327 */         setImage(Toolkit.getDefaultToolkit().getImage(this.location));
/* 1328 */         this.location = null;
/*      */       }
/* 1330 */       return super.getImage();
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthParser
 * JD-Core Version:    0.6.2
 */