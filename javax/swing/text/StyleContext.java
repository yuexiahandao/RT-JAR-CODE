/*      */ package javax.swing.text;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Font;
/*      */ import java.awt.FontMetrics;
/*      */ import java.awt.Toolkit;
/*      */ import java.io.IOException;
/*      */ import java.io.NotSerializableException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.Serializable;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.Collections;
/*      */ import java.util.Enumeration;
/*      */ import java.util.EventListener;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Map;
/*      */ import java.util.NoSuchElementException;
/*      */ import java.util.Vector;
/*      */ import java.util.WeakHashMap;
/*      */ import javax.swing.SwingUtilities;
/*      */ import javax.swing.event.ChangeEvent;
/*      */ import javax.swing.event.ChangeListener;
/*      */ import javax.swing.event.EventListenerList;
/*      */ import sun.font.FontUtilities;
/*      */ 
/*      */ public class StyleContext
/*      */   implements Serializable, AbstractDocument.AttributeContext
/*      */ {
/*      */   private static StyleContext defaultContext;
/*      */   public static final String DEFAULT_STYLE = "default";
/*      */   private static Hashtable<Object, String> freezeKeyMap;
/*      */   private static Hashtable<String, Object> thawKeyMap;
/*      */   private Style styles;
/*  735 */   private transient FontKey fontSearch = new FontKey(null, 0, 0);
/*  736 */   private transient Hashtable<FontKey, Font> fontTable = new Hashtable();
/*      */ 
/*  738 */   private transient Map<SmallAttributeSet, WeakReference<SmallAttributeSet>> attributesPool = Collections.synchronizedMap(new WeakHashMap());
/*      */ 
/*  740 */   private transient MutableAttributeSet search = new SimpleAttributeSet();
/*      */   private int unusedSets;
/*      */   static final int THRESHOLD = 9;
/*      */ 
/*      */   public static final StyleContext getDefaultStyleContext()
/*      */   {
/*   74 */     if (defaultContext == null) {
/*   75 */       defaultContext = new StyleContext();
/*      */     }
/*   77 */     return defaultContext;
/*      */   }
/*      */ 
/*      */   public StyleContext()
/*      */   {
/*   86 */     this.styles = new NamedStyle(null);
/*   87 */     addStyle("default", null);
/*      */   }
/*      */ 
/*      */   public Style addStyle(String paramString, Style paramStyle)
/*      */   {
/*  107 */     NamedStyle localNamedStyle = new NamedStyle(paramString, paramStyle);
/*  108 */     if (paramString != null)
/*      */     {
/*  110 */       this.styles.addAttribute(paramString, localNamedStyle);
/*      */     }
/*  112 */     return localNamedStyle;
/*      */   }
/*      */ 
/*      */   public void removeStyle(String paramString)
/*      */   {
/*  121 */     this.styles.removeAttribute(paramString);
/*      */   }
/*      */ 
/*      */   public Style getStyle(String paramString)
/*      */   {
/*  131 */     return (Style)this.styles.getAttribute(paramString);
/*      */   }
/*      */ 
/*      */   public Enumeration<?> getStyleNames()
/*      */   {
/*  140 */     return this.styles.getAttributeNames();
/*      */   }
/*      */ 
/*      */   public void addChangeListener(ChangeListener paramChangeListener)
/*      */   {
/*  150 */     this.styles.addChangeListener(paramChangeListener);
/*      */   }
/*      */ 
/*      */   public void removeChangeListener(ChangeListener paramChangeListener)
/*      */   {
/*  160 */     this.styles.removeChangeListener(paramChangeListener);
/*      */   }
/*      */ 
/*      */   public ChangeListener[] getChangeListeners()
/*      */   {
/*  172 */     return ((NamedStyle)this.styles).getChangeListeners();
/*      */   }
/*      */ 
/*      */   public Font getFont(AttributeSet paramAttributeSet)
/*      */   {
/*  187 */     int i = 0;
/*  188 */     if (StyleConstants.isBold(paramAttributeSet)) {
/*  189 */       i |= 1;
/*      */     }
/*  191 */     if (StyleConstants.isItalic(paramAttributeSet)) {
/*  192 */       i |= 2;
/*      */     }
/*  194 */     String str = StyleConstants.getFontFamily(paramAttributeSet);
/*  195 */     int j = StyleConstants.getFontSize(paramAttributeSet);
/*      */ 
/*  202 */     if ((StyleConstants.isSuperscript(paramAttributeSet)) || (StyleConstants.isSubscript(paramAttributeSet)))
/*      */     {
/*  204 */       j -= 2;
/*      */     }
/*      */ 
/*  207 */     return getFont(str, i, j);
/*      */   }
/*      */ 
/*      */   public Color getForeground(AttributeSet paramAttributeSet)
/*      */   {
/*  220 */     return StyleConstants.getForeground(paramAttributeSet);
/*      */   }
/*      */ 
/*      */   public Color getBackground(AttributeSet paramAttributeSet)
/*      */   {
/*  233 */     return StyleConstants.getBackground(paramAttributeSet);
/*      */   }
/*      */ 
/*      */   public Font getFont(String paramString, int paramInt1, int paramInt2)
/*      */   {
/*  248 */     this.fontSearch.setValue(paramString, paramInt1, paramInt2);
/*  249 */     Object localObject = (Font)this.fontTable.get(this.fontSearch);
/*  250 */     if (localObject == null)
/*      */     {
/*  252 */       Style localStyle = getStyle("default");
/*      */ 
/*  254 */       if (localStyle != null)
/*      */       {
/*  256 */         Font localFont = (Font)localStyle.getAttribute("FONT_ATTRIBUTE_KEY");
/*      */ 
/*  258 */         if ((localFont != null) && (localFont.getFamily().equalsIgnoreCase(paramString)))
/*      */         {
/*  260 */           localObject = localFont.deriveFont(paramInt1, paramInt2);
/*      */         }
/*      */       }
/*  263 */       if (localObject == null) {
/*  264 */         localObject = new Font(paramString, paramInt1, paramInt2);
/*      */       }
/*  266 */       if (!FontUtilities.fontSupportsDefaultEncoding((Font)localObject)) {
/*  267 */         localObject = FontUtilities.getCompositeFontUIResource((Font)localObject);
/*      */       }
/*  269 */       FontKey localFontKey = new FontKey(paramString, paramInt1, paramInt2);
/*  270 */       this.fontTable.put(localFontKey, localObject);
/*      */     }
/*  272 */     return localObject;
/*      */   }
/*      */ 
/*      */   public FontMetrics getFontMetrics(Font paramFont)
/*      */   {
/*  284 */     return Toolkit.getDefaultToolkit().getFontMetrics(paramFont);
/*      */   }
/*      */ 
/*      */   public synchronized AttributeSet addAttribute(AttributeSet paramAttributeSet, Object paramObject1, Object paramObject2)
/*      */   {
/*  305 */     if (paramAttributeSet.getAttributeCount() + 1 <= getCompressionThreshold())
/*      */     {
/*  308 */       this.search.removeAttributes(this.search);
/*  309 */       this.search.addAttributes(paramAttributeSet);
/*  310 */       this.search.addAttribute(paramObject1, paramObject2);
/*  311 */       reclaim(paramAttributeSet);
/*  312 */       return getImmutableUniqueSet();
/*      */     }
/*  314 */     MutableAttributeSet localMutableAttributeSet = getMutableAttributeSet(paramAttributeSet);
/*  315 */     localMutableAttributeSet.addAttribute(paramObject1, paramObject2);
/*  316 */     return localMutableAttributeSet;
/*      */   }
/*      */ 
/*      */   public synchronized AttributeSet addAttributes(AttributeSet paramAttributeSet1, AttributeSet paramAttributeSet2)
/*      */   {
/*  333 */     if (paramAttributeSet1.getAttributeCount() + paramAttributeSet2.getAttributeCount() <= getCompressionThreshold())
/*      */     {
/*  336 */       this.search.removeAttributes(this.search);
/*  337 */       this.search.addAttributes(paramAttributeSet1);
/*  338 */       this.search.addAttributes(paramAttributeSet2);
/*  339 */       reclaim(paramAttributeSet1);
/*  340 */       return getImmutableUniqueSet();
/*      */     }
/*  342 */     MutableAttributeSet localMutableAttributeSet = getMutableAttributeSet(paramAttributeSet1);
/*  343 */     localMutableAttributeSet.addAttributes(paramAttributeSet2);
/*  344 */     return localMutableAttributeSet;
/*      */   }
/*      */ 
/*      */   public synchronized AttributeSet removeAttribute(AttributeSet paramAttributeSet, Object paramObject)
/*      */   {
/*  361 */     if (paramAttributeSet.getAttributeCount() - 1 <= getCompressionThreshold())
/*      */     {
/*  364 */       this.search.removeAttributes(this.search);
/*  365 */       this.search.addAttributes(paramAttributeSet);
/*  366 */       this.search.removeAttribute(paramObject);
/*  367 */       reclaim(paramAttributeSet);
/*  368 */       return getImmutableUniqueSet();
/*      */     }
/*  370 */     MutableAttributeSet localMutableAttributeSet = getMutableAttributeSet(paramAttributeSet);
/*  371 */     localMutableAttributeSet.removeAttribute(paramObject);
/*  372 */     return localMutableAttributeSet;
/*      */   }
/*      */ 
/*      */   public synchronized AttributeSet removeAttributes(AttributeSet paramAttributeSet, Enumeration<?> paramEnumeration)
/*      */   {
/*  389 */     if (paramAttributeSet.getAttributeCount() <= getCompressionThreshold())
/*      */     {
/*  392 */       this.search.removeAttributes(this.search);
/*  393 */       this.search.addAttributes(paramAttributeSet);
/*  394 */       this.search.removeAttributes(paramEnumeration);
/*  395 */       reclaim(paramAttributeSet);
/*  396 */       return getImmutableUniqueSet();
/*      */     }
/*  398 */     MutableAttributeSet localMutableAttributeSet = getMutableAttributeSet(paramAttributeSet);
/*  399 */     localMutableAttributeSet.removeAttributes(paramEnumeration);
/*  400 */     return localMutableAttributeSet;
/*      */   }
/*      */ 
/*      */   public synchronized AttributeSet removeAttributes(AttributeSet paramAttributeSet1, AttributeSet paramAttributeSet2)
/*      */   {
/*  417 */     if (paramAttributeSet1.getAttributeCount() <= getCompressionThreshold())
/*      */     {
/*  420 */       this.search.removeAttributes(this.search);
/*  421 */       this.search.addAttributes(paramAttributeSet1);
/*  422 */       this.search.removeAttributes(paramAttributeSet2);
/*  423 */       reclaim(paramAttributeSet1);
/*  424 */       return getImmutableUniqueSet();
/*      */     }
/*  426 */     MutableAttributeSet localMutableAttributeSet = getMutableAttributeSet(paramAttributeSet1);
/*  427 */     localMutableAttributeSet.removeAttributes(paramAttributeSet2);
/*  428 */     return localMutableAttributeSet;
/*      */   }
/*      */ 
/*      */   public AttributeSet getEmptySet()
/*      */   {
/*  437 */     return SimpleAttributeSet.EMPTY;
/*      */   }
/*      */ 
/*      */   public void reclaim(AttributeSet paramAttributeSet)
/*      */   {
/*  454 */     if (SwingUtilities.isEventDispatchThread())
/*  455 */       this.attributesPool.size();
/*      */   }
/*      */ 
/*      */   protected int getCompressionThreshold()
/*      */   {
/*  471 */     return 9;
/*      */   }
/*      */ 
/*      */   protected SmallAttributeSet createSmallAttributeSet(AttributeSet paramAttributeSet)
/*      */   {
/*  485 */     return new SmallAttributeSet(paramAttributeSet);
/*      */   }
/*      */ 
/*      */   protected MutableAttributeSet createLargeAttributeSet(AttributeSet paramAttributeSet)
/*      */   {
/*  501 */     return new SimpleAttributeSet(paramAttributeSet);
/*      */   }
/*      */ 
/*      */   synchronized void removeUnusedSets()
/*      */   {
/*  508 */     this.attributesPool.size();
/*      */   }
/*      */ 
/*      */   AttributeSet getImmutableUniqueSet()
/*      */   {
/*  519 */     SmallAttributeSet localSmallAttributeSet1 = createSmallAttributeSet(this.search);
/*  520 */     WeakReference localWeakReference = (WeakReference)this.attributesPool.get(localSmallAttributeSet1);
/*      */     SmallAttributeSet localSmallAttributeSet2;
/*  522 */     if ((localWeakReference == null) || ((localSmallAttributeSet2 = (SmallAttributeSet)localWeakReference.get()) == null)) {
/*  523 */       localSmallAttributeSet2 = localSmallAttributeSet1;
/*  524 */       this.attributesPool.put(localSmallAttributeSet2, new WeakReference(localSmallAttributeSet2));
/*      */     }
/*  526 */     return localSmallAttributeSet2;
/*      */   }
/*      */ 
/*      */   MutableAttributeSet getMutableAttributeSet(AttributeSet paramAttributeSet)
/*      */   {
/*  534 */     if (((paramAttributeSet instanceof MutableAttributeSet)) && (paramAttributeSet != SimpleAttributeSet.EMPTY))
/*      */     {
/*  536 */       return (MutableAttributeSet)paramAttributeSet;
/*      */     }
/*  538 */     return createLargeAttributeSet(paramAttributeSet);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/*  547 */     removeUnusedSets();
/*  548 */     String str = "";
/*  549 */     for (SmallAttributeSet localSmallAttributeSet : this.attributesPool.keySet()) {
/*  550 */       str = str + localSmallAttributeSet + "\n";
/*      */     }
/*  552 */     return str;
/*      */   }
/*      */ 
/*      */   public void writeAttributes(ObjectOutputStream paramObjectOutputStream, AttributeSet paramAttributeSet)
/*      */     throws IOException
/*      */   {
/*  562 */     writeAttributeSet(paramObjectOutputStream, paramAttributeSet);
/*      */   }
/*      */ 
/*      */   public void readAttributes(ObjectInputStream paramObjectInputStream, MutableAttributeSet paramMutableAttributeSet)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/*  570 */     readAttributeSet(paramObjectInputStream, paramMutableAttributeSet);
/*      */   }
/*      */ 
/*      */   public static void writeAttributeSet(ObjectOutputStream paramObjectOutputStream, AttributeSet paramAttributeSet)
/*      */     throws IOException
/*      */   {
/*  589 */     int i = paramAttributeSet.getAttributeCount();
/*  590 */     paramObjectOutputStream.writeInt(i);
/*  591 */     Enumeration localEnumeration = paramAttributeSet.getAttributeNames();
/*  592 */     while (localEnumeration.hasMoreElements()) {
/*  593 */       Object localObject1 = localEnumeration.nextElement();
/*  594 */       if ((localObject1 instanceof AbstractDocument.AttributeContext)) {
/*  595 */         paramObjectOutputStream.writeObject(localObject1);
/*      */       } else {
/*  597 */         localObject2 = freezeKeyMap.get(localObject1);
/*  598 */         if (localObject2 == null) {
/*  599 */           throw new NotSerializableException(localObject1.getClass().getName() + " is not serializable as a key in an AttributeSet");
/*      */         }
/*      */ 
/*  602 */         paramObjectOutputStream.writeObject(localObject2);
/*      */       }
/*  604 */       Object localObject2 = paramAttributeSet.getAttribute(localObject1);
/*  605 */       Object localObject3 = freezeKeyMap.get(localObject2);
/*  606 */       if ((localObject2 instanceof AbstractDocument.AttributeContext)) {
/*  607 */         paramObjectOutputStream.writeObject(localObject3 != null ? localObject3 : localObject2);
/*      */       } else {
/*  609 */         if (localObject3 == null) {
/*  610 */           throw new NotSerializableException(localObject2.getClass().getName() + " is not serializable as a value in an AttributeSet");
/*      */         }
/*      */ 
/*  613 */         paramObjectOutputStream.writeObject(localObject3);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void readAttributeSet(ObjectInputStream paramObjectInputStream, MutableAttributeSet paramMutableAttributeSet)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/*  640 */     int i = paramObjectInputStream.readInt();
/*  641 */     for (int j = 0; j < i; j++) {
/*  642 */       Object localObject1 = paramObjectInputStream.readObject();
/*  643 */       Object localObject2 = paramObjectInputStream.readObject();
/*  644 */       if (thawKeyMap != null) {
/*  645 */         Object localObject3 = thawKeyMap.get(localObject1);
/*  646 */         if (localObject3 != null) {
/*  647 */           localObject1 = localObject3;
/*      */         }
/*  649 */         Object localObject4 = thawKeyMap.get(localObject2);
/*  650 */         if (localObject4 != null) {
/*  651 */           localObject2 = localObject4;
/*      */         }
/*      */       }
/*  654 */       paramMutableAttributeSet.addAttribute(localObject1, localObject2);
/*      */     }
/*      */   }
/*      */ 
/*      */   public static void registerStaticAttributeKey(Object paramObject)
/*      */   {
/*  674 */     String str = paramObject.getClass().getName() + "." + paramObject.toString();
/*  675 */     if (freezeKeyMap == null) {
/*  676 */       freezeKeyMap = new Hashtable();
/*  677 */       thawKeyMap = new Hashtable();
/*      */     }
/*  679 */     freezeKeyMap.put(paramObject, str);
/*  680 */     thawKeyMap.put(str, paramObject);
/*      */   }
/*      */ 
/*      */   public static Object getStaticAttribute(Object paramObject)
/*      */   {
/*  688 */     if ((thawKeyMap == null) || (paramObject == null)) {
/*  689 */       return null;
/*      */     }
/*  691 */     return thawKeyMap.get(paramObject);
/*      */   }
/*      */ 
/*      */   public static Object getStaticAttributeKey(Object paramObject)
/*      */   {
/*  700 */     return paramObject.getClass().getName() + "." + paramObject.toString();
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/*  707 */     removeUnusedSets();
/*      */ 
/*  709 */     paramObjectOutputStream.defaultWriteObject();
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws ClassNotFoundException, IOException
/*      */   {
/*  715 */     this.fontSearch = new FontKey(null, 0, 0);
/*  716 */     this.fontTable = new Hashtable();
/*  717 */     this.search = new SimpleAttributeSet();
/*  718 */     this.attributesPool = Collections.synchronizedMap(new WeakHashMap());
/*      */ 
/*  720 */     paramObjectInputStream.defaultReadObject();
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/* 1611 */       int i = StyleConstants.keys.length;
/* 1612 */       for (int j = 0; j < i; j++)
/* 1613 */         registerStaticAttributeKey(StyleConstants.keys[j]);
/*      */     }
/*      */     catch (Throwable localThrowable) {
/* 1616 */       localThrowable.printStackTrace();
/*      */     }
/*      */   }
/*      */ 
/*      */   static class FontKey
/*      */   {
/*      */     private String family;
/*      */     private int style;
/*      */     private int size;
/*      */ 
/*      */     public FontKey(String paramString, int paramInt1, int paramInt2)
/*      */     {
/* 1193 */       setValue(paramString, paramInt1, paramInt2);
/*      */     }
/*      */ 
/*      */     public void setValue(String paramString, int paramInt1, int paramInt2) {
/* 1197 */       this.family = (paramString != null ? paramString.intern() : null);
/* 1198 */       this.style = paramInt1;
/* 1199 */       this.size = paramInt2;
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 1207 */       int i = this.family != null ? this.family.hashCode() : 0;
/* 1208 */       return i ^ this.style ^ this.size;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/* 1221 */       if ((paramObject instanceof FontKey)) {
/* 1222 */         FontKey localFontKey = (FontKey)paramObject;
/* 1223 */         return (this.size == localFontKey.size) && (this.style == localFontKey.style) && (this.family == localFontKey.family);
/*      */       }
/* 1225 */       return false;
/*      */     }
/*      */   }
/*      */ 
/*      */   class KeyBuilder
/*      */   {
/* 1176 */     private Vector<Object> keys = new Vector();
/* 1177 */     private Vector<Object> data = new Vector();
/*      */ 
/*      */     KeyBuilder()
/*      */     {
/*      */     }
/*      */ 
/*      */     public void initialize(AttributeSet paramAttributeSet)
/*      */     {
/* 1048 */       if ((paramAttributeSet instanceof StyleContext.SmallAttributeSet)) {
/* 1049 */         initialize(((StyleContext.SmallAttributeSet)paramAttributeSet).attributes);
/*      */       } else {
/* 1051 */         this.keys.removeAllElements();
/* 1052 */         this.data.removeAllElements();
/* 1053 */         Enumeration localEnumeration = paramAttributeSet.getAttributeNames();
/* 1054 */         while (localEnumeration.hasMoreElements()) {
/* 1055 */           Object localObject = localEnumeration.nextElement();
/* 1056 */           addAttribute(localObject, paramAttributeSet.getAttribute(localObject));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     private void initialize(Object[] paramArrayOfObject)
/*      */     {
/* 1066 */       this.keys.removeAllElements();
/* 1067 */       this.data.removeAllElements();
/* 1068 */       int i = paramArrayOfObject.length;
/* 1069 */       for (int j = 0; j < i; j += 2) {
/* 1070 */         this.keys.addElement(paramArrayOfObject[j]);
/* 1071 */         this.data.addElement(paramArrayOfObject[(j + 1)]);
/*      */       }
/*      */     }
/*      */ 
/*      */     public Object[] createTable()
/*      */     {
/* 1081 */       int i = this.keys.size();
/* 1082 */       Object[] arrayOfObject = new Object[2 * i];
/* 1083 */       for (int j = 0; j < i; j++) {
/* 1084 */         int k = 2 * j;
/* 1085 */         arrayOfObject[k] = this.keys.elementAt(j);
/* 1086 */         arrayOfObject[(k + 1)] = this.data.elementAt(j);
/*      */       }
/* 1088 */       return arrayOfObject;
/*      */     }
/*      */ 
/*      */     int getCount()
/*      */     {
/* 1096 */       return this.keys.size();
/*      */     }
/*      */ 
/*      */     public void addAttribute(Object paramObject1, Object paramObject2)
/*      */     {
/* 1103 */       this.keys.addElement(paramObject1);
/* 1104 */       this.data.addElement(paramObject2);
/*      */     }
/*      */ 
/*      */     public void addAttributes(AttributeSet paramAttributeSet)
/*      */     {
/*      */       Object localObject1;
/* 1111 */       if ((paramAttributeSet instanceof StyleContext.SmallAttributeSet))
/*      */       {
/* 1113 */         localObject1 = ((StyleContext.SmallAttributeSet)paramAttributeSet).attributes;
/* 1114 */         int i = localObject1.length;
/* 1115 */         for (int j = 0; j < i; j += 2)
/* 1116 */           addAttribute(localObject1[j], localObject1[(j + 1)]);
/*      */       }
/*      */       else {
/* 1119 */         localObject1 = paramAttributeSet.getAttributeNames();
/* 1120 */         while (((Enumeration)localObject1).hasMoreElements()) {
/* 1121 */           Object localObject2 = ((Enumeration)localObject1).nextElement();
/* 1122 */           addAttribute(localObject2, paramAttributeSet.getAttribute(localObject2));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*      */     public void removeAttribute(Object paramObject)
/*      */     {
/* 1131 */       int i = this.keys.size();
/* 1132 */       for (int j = 0; j < i; j++)
/* 1133 */         if (this.keys.elementAt(j).equals(paramObject)) {
/* 1134 */           this.keys.removeElementAt(j);
/* 1135 */           this.data.removeElementAt(j);
/* 1136 */           return;
/*      */         }
/*      */     }
/*      */ 
/*      */     public void removeAttributes(Enumeration paramEnumeration)
/*      */     {
/* 1145 */       while (paramEnumeration.hasMoreElements()) {
/* 1146 */         Object localObject = paramEnumeration.nextElement();
/* 1147 */         removeAttribute(localObject);
/*      */       }
/*      */     }
/*      */ 
/*      */     public void removeAttributes(AttributeSet paramAttributeSet)
/*      */     {
/* 1155 */       Enumeration localEnumeration = paramAttributeSet.getAttributeNames();
/* 1156 */       while (localEnumeration.hasMoreElements()) {
/* 1157 */         Object localObject1 = localEnumeration.nextElement();
/* 1158 */         Object localObject2 = paramAttributeSet.getAttribute(localObject1);
/* 1159 */         removeSearchAttribute(localObject1, localObject2);
/*      */       }
/*      */     }
/*      */ 
/*      */     private void removeSearchAttribute(Object paramObject1, Object paramObject2) {
/* 1164 */       int i = this.keys.size();
/* 1165 */       for (int j = 0; j < i; j++)
/* 1166 */         if (this.keys.elementAt(j).equals(paramObject1)) {
/* 1167 */           if (this.data.elementAt(j).equals(paramObject2)) {
/* 1168 */             this.keys.removeElementAt(j);
/* 1169 */             this.data.removeElementAt(j);
/*      */           }
/* 1171 */           return;
/*      */         }
/*      */     }
/*      */   }
/*      */ 
/*      */   class KeyEnumeration
/*      */     implements Enumeration<Object>
/*      */   {
/*      */     Object[] attr;
/*      */     int i;
/*      */ 
/*      */     KeyEnumeration(Object[] arg2)
/*      */     {
/*      */       Object localObject;
/* 1006 */       this.attr = localObject;
/* 1007 */       this.i = 0;
/*      */     }
/*      */ 
/*      */     public boolean hasMoreElements()
/*      */     {
/* 1018 */       return this.i < this.attr.length;
/*      */     }
/*      */ 
/*      */     public Object nextElement()
/*      */     {
/* 1029 */       if (this.i < this.attr.length) {
/* 1030 */         Object localObject = this.attr[this.i];
/* 1031 */         this.i += 2;
/* 1032 */         return localObject;
/*      */       }
/* 1034 */       throw new NoSuchElementException();
/*      */     }
/*      */   }
/*      */ 
/*      */   public class NamedStyle
/*      */     implements Style, Serializable
/*      */   {
/* 1591 */     protected EventListenerList listenerList = new EventListenerList();
/*      */ 
/* 1598 */     protected transient ChangeEvent changeEvent = null;
/*      */     private transient AttributeSet attributes;
/*      */ 
/*      */     public NamedStyle(String paramStyle, Style arg3)
/*      */     {
/* 1257 */       this.attributes = StyleContext.this.getEmptySet();
/* 1258 */       if (paramStyle != null)
/* 1259 */         setName(paramStyle);
/*      */       AttributeSet localAttributeSet;
/* 1261 */       if (localAttributeSet != null)
/* 1262 */         setResolveParent(localAttributeSet);
/*      */     }
/*      */ 
/*      */     public NamedStyle(Style arg2)
/*      */     {
/* 1273 */       this(null, localStyle);
/*      */     }
/*      */ 
/*      */     public NamedStyle()
/*      */     {
/* 1280 */       this.attributes = StyleContext.this.getEmptySet();
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1289 */       return "NamedStyle:" + getName() + " " + this.attributes;
/*      */     }
/*      */ 
/*      */     public String getName()
/*      */     {
/* 1299 */       if (isDefined(StyleConstants.NameAttribute)) {
/* 1300 */         return getAttribute(StyleConstants.NameAttribute).toString();
/*      */       }
/* 1302 */       return null;
/*      */     }
/*      */ 
/*      */     public void setName(String paramString)
/*      */     {
/* 1311 */       if (paramString != null)
/* 1312 */         addAttribute(StyleConstants.NameAttribute, paramString);
/*      */     }
/*      */ 
/*      */     public void addChangeListener(ChangeListener paramChangeListener)
/*      */     {
/* 1322 */       this.listenerList.add(ChangeListener.class, paramChangeListener);
/*      */     }
/*      */ 
/*      */     public void removeChangeListener(ChangeListener paramChangeListener)
/*      */     {
/* 1331 */       this.listenerList.remove(ChangeListener.class, paramChangeListener);
/*      */     }
/*      */ 
/*      */     public ChangeListener[] getChangeListeners()
/*      */     {
/* 1344 */       return (ChangeListener[])this.listenerList.getListeners(ChangeListener.class);
/*      */     }
/*      */ 
/*      */     protected void fireStateChanged()
/*      */     {
/* 1358 */       Object[] arrayOfObject = this.listenerList.getListenerList();
/*      */ 
/* 1361 */       for (int i = arrayOfObject.length - 2; i >= 0; i -= 2)
/* 1362 */         if (arrayOfObject[i] == ChangeListener.class)
/*      */         {
/* 1364 */           if (this.changeEvent == null)
/* 1365 */             this.changeEvent = new ChangeEvent(this);
/* 1366 */           ((ChangeListener)arrayOfObject[(i + 1)]).stateChanged(this.changeEvent);
/*      */         }
/*      */     }
/*      */ 
/*      */     public <T extends EventListener> T[] getListeners(Class<T> paramClass)
/*      */     {
/* 1381 */       return this.listenerList.getListeners(paramClass);
/*      */     }
/*      */ 
/*      */     public int getAttributeCount()
/*      */     {
/* 1394 */       return this.attributes.getAttributeCount();
/*      */     }
/*      */ 
/*      */     public boolean isDefined(Object paramObject)
/*      */     {
/* 1405 */       return this.attributes.isDefined(paramObject);
/*      */     }
/*      */ 
/*      */     public boolean isEqual(AttributeSet paramAttributeSet)
/*      */     {
/* 1416 */       return this.attributes.isEqual(paramAttributeSet);
/*      */     }
/*      */ 
/*      */     public AttributeSet copyAttributes()
/*      */     {
/* 1426 */       NamedStyle localNamedStyle = new NamedStyle(StyleContext.this);
/* 1427 */       localNamedStyle.attributes = this.attributes.copyAttributes();
/* 1428 */       return localNamedStyle;
/*      */     }
/*      */ 
/*      */     public Object getAttribute(Object paramObject)
/*      */     {
/* 1439 */       return this.attributes.getAttribute(paramObject);
/*      */     }
/*      */ 
/*      */     public Enumeration<?> getAttributeNames()
/*      */     {
/* 1449 */       return this.attributes.getAttributeNames();
/*      */     }
/*      */ 
/*      */     public boolean containsAttribute(Object paramObject1, Object paramObject2)
/*      */     {
/* 1461 */       return this.attributes.containsAttribute(paramObject1, paramObject2);
/*      */     }
/*      */ 
/*      */     public boolean containsAttributes(AttributeSet paramAttributeSet)
/*      */     {
/* 1473 */       return this.attributes.containsAttributes(paramAttributeSet);
/*      */     }
/*      */ 
/*      */     public AttributeSet getResolveParent()
/*      */     {
/* 1485 */       return this.attributes.getResolveParent();
/*      */     }
/*      */ 
/*      */     public void addAttribute(Object paramObject1, Object paramObject2)
/*      */     {
/* 1500 */       StyleContext localStyleContext = StyleContext.this;
/* 1501 */       this.attributes = localStyleContext.addAttribute(this.attributes, paramObject1, paramObject2);
/* 1502 */       fireStateChanged();
/*      */     }
/*      */ 
/*      */     public void addAttributes(AttributeSet paramAttributeSet)
/*      */     {
/* 1512 */       StyleContext localStyleContext = StyleContext.this;
/* 1513 */       this.attributes = localStyleContext.addAttributes(this.attributes, paramAttributeSet);
/* 1514 */       fireStateChanged();
/*      */     }
/*      */ 
/*      */     public void removeAttribute(Object paramObject)
/*      */     {
/* 1524 */       StyleContext localStyleContext = StyleContext.this;
/* 1525 */       this.attributes = localStyleContext.removeAttribute(this.attributes, paramObject);
/* 1526 */       fireStateChanged();
/*      */     }
/*      */ 
/*      */     public void removeAttributes(Enumeration<?> paramEnumeration)
/*      */     {
/* 1536 */       StyleContext localStyleContext = StyleContext.this;
/* 1537 */       this.attributes = localStyleContext.removeAttributes(this.attributes, paramEnumeration);
/* 1538 */       fireStateChanged();
/*      */     }
/*      */ 
/*      */     public void removeAttributes(AttributeSet paramAttributeSet)
/*      */     {
/* 1548 */       StyleContext localStyleContext = StyleContext.this;
/* 1549 */       if (paramAttributeSet == this)
/* 1550 */         this.attributes = localStyleContext.getEmptySet();
/*      */       else {
/* 1552 */         this.attributes = localStyleContext.removeAttributes(this.attributes, paramAttributeSet);
/*      */       }
/* 1554 */       fireStateChanged();
/*      */     }
/*      */ 
/*      */     public void setResolveParent(AttributeSet paramAttributeSet)
/*      */     {
/* 1564 */       if (paramAttributeSet != null)
/* 1565 */         addAttribute(StyleConstants.ResolveAttribute, paramAttributeSet);
/*      */       else
/* 1567 */         removeAttribute(StyleConstants.ResolveAttribute);
/*      */     }
/*      */ 
/*      */     private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */       throws IOException
/*      */     {
/* 1574 */       paramObjectOutputStream.defaultWriteObject();
/* 1575 */       StyleContext.writeAttributeSet(paramObjectOutputStream, this.attributes);
/*      */     }
/*      */ 
/*      */     private void readObject(ObjectInputStream paramObjectInputStream)
/*      */       throws ClassNotFoundException, IOException
/*      */     {
/* 1581 */       paramObjectInputStream.defaultReadObject();
/* 1582 */       this.attributes = SimpleAttributeSet.EMPTY;
/* 1583 */       StyleContext.readAttributeSet(paramObjectInputStream, this);
/*      */     }
/*      */   }
/*      */ 
/*      */   public class SmallAttributeSet
/*      */     implements AttributeSet
/*      */   {
/*      */     Object[] attributes;
/*      */     AttributeSet resolveParent;
/*      */ 
/*      */     public SmallAttributeSet(Object[] arg2)
/*      */     {
/*      */       Object localObject;
/*  766 */       this.attributes = localObject;
/*  767 */       updateResolveParent();
/*      */     }
/*      */ 
/*      */     public SmallAttributeSet(AttributeSet arg2)
/*      */     {
/*      */       Object localObject;
/*  771 */       int i = localObject.getAttributeCount();
/*  772 */       Object[] arrayOfObject = new Object[2 * i];
/*  773 */       Enumeration localEnumeration = localObject.getAttributeNames();
/*  774 */       int j = 0;
/*  775 */       while (localEnumeration.hasMoreElements()) {
/*  776 */         arrayOfObject[j] = localEnumeration.nextElement();
/*  777 */         arrayOfObject[(j + 1)] = localObject.getAttribute(arrayOfObject[j]);
/*  778 */         j += 2;
/*      */       }
/*  780 */       this.attributes = arrayOfObject;
/*  781 */       updateResolveParent();
/*      */     }
/*      */ 
/*      */     private void updateResolveParent() {
/*  785 */       this.resolveParent = null;
/*  786 */       Object[] arrayOfObject = this.attributes;
/*  787 */       for (int i = 0; i < arrayOfObject.length; i += 2)
/*  788 */         if (arrayOfObject[i] == StyleConstants.ResolveAttribute) {
/*  789 */           this.resolveParent = ((AttributeSet)arrayOfObject[(i + 1)]);
/*  790 */           break;
/*      */         }
/*      */     }
/*      */ 
/*      */     Object getLocalAttribute(Object paramObject)
/*      */     {
/*  796 */       if (paramObject == StyleConstants.ResolveAttribute) {
/*  797 */         return this.resolveParent;
/*      */       }
/*  799 */       Object[] arrayOfObject = this.attributes;
/*  800 */       for (int i = 0; i < arrayOfObject.length; i += 2) {
/*  801 */         if (paramObject.equals(arrayOfObject[i])) {
/*  802 */           return arrayOfObject[(i + 1)];
/*      */         }
/*      */       }
/*  805 */       return null;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/*  814 */       String str = "{";
/*  815 */       Object[] arrayOfObject = this.attributes;
/*  816 */       for (int i = 0; i < arrayOfObject.length; i += 2) {
/*  817 */         if ((arrayOfObject[(i + 1)] instanceof AttributeSet))
/*      */         {
/*  819 */           str = str + arrayOfObject[i] + "=" + "AttributeSet" + ",";
/*      */         }
/*  821 */         else str = str + arrayOfObject[i] + "=" + arrayOfObject[(i + 1)] + ",";
/*      */       }
/*      */ 
/*  824 */       str = str + "}";
/*  825 */       return str;
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/*  833 */       int i = 0;
/*  834 */       Object[] arrayOfObject = this.attributes;
/*  835 */       for (int j = 1; j < arrayOfObject.length; j += 2) {
/*  836 */         i ^= arrayOfObject[j].hashCode();
/*      */       }
/*  838 */       return i;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/*  850 */       if ((paramObject instanceof AttributeSet)) {
/*  851 */         AttributeSet localAttributeSet = (AttributeSet)paramObject;
/*  852 */         return (getAttributeCount() == localAttributeSet.getAttributeCount()) && (containsAttributes(localAttributeSet));
/*      */       }
/*      */ 
/*  855 */       return false;
/*      */     }
/*      */ 
/*      */     public Object clone()
/*      */     {
/*  865 */       return this;
/*      */     }
/*      */ 
/*      */     public int getAttributeCount()
/*      */     {
/*  877 */       return this.attributes.length / 2;
/*      */     }
/*      */ 
/*      */     public boolean isDefined(Object paramObject)
/*      */     {
/*  888 */       Object[] arrayOfObject = this.attributes;
/*  889 */       int i = arrayOfObject.length;
/*  890 */       for (int j = 0; j < i; j += 2) {
/*  891 */         if (paramObject.equals(arrayOfObject[j])) {
/*  892 */           return true;
/*      */         }
/*      */       }
/*  895 */       return false;
/*      */     }
/*      */ 
/*      */     public boolean isEqual(AttributeSet paramAttributeSet)
/*      */     {
/*  906 */       if ((paramAttributeSet instanceof SmallAttributeSet)) {
/*  907 */         return paramAttributeSet == this;
/*      */       }
/*  909 */       return (getAttributeCount() == paramAttributeSet.getAttributeCount()) && (containsAttributes(paramAttributeSet));
/*      */     }
/*      */ 
/*      */     public AttributeSet copyAttributes()
/*      */     {
/*  920 */       return this;
/*      */     }
/*      */ 
/*      */     public Object getAttribute(Object paramObject)
/*      */     {
/*  931 */       Object localObject = getLocalAttribute(paramObject);
/*  932 */       if (localObject == null) {
/*  933 */         AttributeSet localAttributeSet = getResolveParent();
/*  934 */         if (localAttributeSet != null)
/*  935 */           localObject = localAttributeSet.getAttribute(paramObject);
/*      */       }
/*  937 */       return localObject;
/*      */     }
/*      */ 
/*      */     public Enumeration<?> getAttributeNames()
/*      */     {
/*  947 */       return new StyleContext.KeyEnumeration(StyleContext.this, this.attributes);
/*      */     }
/*      */ 
/*      */     public boolean containsAttribute(Object paramObject1, Object paramObject2)
/*      */     {
/*  959 */       return paramObject2.equals(getAttribute(paramObject1));
/*      */     }
/*      */ 
/*      */     public boolean containsAttributes(AttributeSet paramAttributeSet)
/*      */     {
/*  971 */       boolean bool = true;
/*      */ 
/*  973 */       Enumeration localEnumeration = paramAttributeSet.getAttributeNames();
/*  974 */       while ((bool) && (localEnumeration.hasMoreElements())) {
/*  975 */         Object localObject = localEnumeration.nextElement();
/*  976 */         bool = paramAttributeSet.getAttribute(localObject).equals(getAttribute(localObject));
/*      */       }
/*      */ 
/*  979 */       return bool;
/*      */     }
/*      */ 
/*      */     public AttributeSet getResolveParent()
/*      */     {
/*  990 */       return this.resolveParent;
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.StyleContext
 * JD-Core Version:    0.6.2
 */