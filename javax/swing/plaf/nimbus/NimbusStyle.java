/*      */ package javax.swing.plaf.nimbus;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Font;
/*      */ import java.awt.Insets;
/*      */ import java.lang.ref.WeakReference;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.TreeMap;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.Painter;
/*      */ import javax.swing.UIDefaults;
/*      */ import javax.swing.UIDefaults.LazyValue;
/*      */ import javax.swing.UIManager;
/*      */ import javax.swing.plaf.ColorUIResource;
/*      */ import javax.swing.plaf.synth.ColorType;
/*      */ import javax.swing.plaf.synth.SynthContext;
/*      */ import javax.swing.plaf.synth.SynthPainter;
/*      */ import javax.swing.plaf.synth.SynthStyle;
/*      */ 
/*      */ public final class NimbusStyle extends SynthStyle
/*      */ {
/*      */   public static final String LARGE_KEY = "large";
/*      */   public static final String SMALL_KEY = "small";
/*      */   public static final String MINI_KEY = "mini";
/*      */   public static final double LARGE_SCALE = 1.15D;
/*      */   public static final double SMALL_SCALE = 0.857D;
/*      */   public static final double MINI_SCALE = 0.714D;
/*  137 */   private static final Object NULL = Character.valueOf('\000');
/*      */ 
/*  150 */   private static final Color DEFAULT_COLOR = new ColorUIResource(Color.BLACK);
/*      */ 
/*  155 */   private static final Comparator<RuntimeState> STATE_COMPARATOR = new Comparator()
/*      */   {
/*      */     public int compare(NimbusStyle.RuntimeState paramAnonymousRuntimeState1, NimbusStyle.RuntimeState paramAnonymousRuntimeState2)
/*      */     {
/*  159 */       return paramAnonymousRuntimeState1.state - paramAnonymousRuntimeState2.state;
/*      */     }
/*  155 */   };
/*      */   private String prefix;
/*      */   private SynthPainter painter;
/*      */   private Values values;
/*  189 */   private CacheKey tmpKey = new CacheKey("", 0);
/*      */   private WeakReference<JComponent> component;
/*      */ 
/*      */   NimbusStyle(String paramString, JComponent paramJComponent)
/*      */   {
/*  213 */     if (paramJComponent != null) {
/*  214 */       this.component = new WeakReference(paramJComponent);
/*      */     }
/*  216 */     this.prefix = paramString;
/*  217 */     this.painter = new SynthPainterImpl(this);
/*      */   }
/*      */ 
/*      */   public void installDefaults(SynthContext paramSynthContext)
/*      */   {
/*  227 */     validate();
/*      */ 
/*  231 */     super.installDefaults(paramSynthContext);
/*      */   }
/*      */ 
/*      */   private void validate()
/*      */   {
/*  241 */     if (this.values != null) return;
/*      */ 
/*  247 */     this.values = new Values(null);
/*      */ 
/*  249 */     Object localObject1 = ((NimbusLookAndFeel)UIManager.getLookAndFeel()).getDefaultsForPrefix(this.prefix);
/*      */     Object localObject6;
/*      */     Object localObject7;
/*  256 */     if (this.component != null)
/*      */     {
/*  259 */       localObject2 = ((JComponent)this.component.get()).getClientProperty("Nimbus.Overrides");
/*  260 */       if ((localObject2 instanceof UIDefaults)) {
/*  261 */         localObject3 = ((JComponent)this.component.get()).getClientProperty("Nimbus.Overrides.InheritDefaults");
/*      */ 
/*  263 */         int i = (localObject3 instanceof Boolean) ? ((Boolean)localObject3).booleanValue() : 1;
/*  264 */         localObject4 = (UIDefaults)localObject2;
/*  265 */         localObject5 = new TreeMap();
/*  266 */         for (Iterator localIterator1 = ((UIDefaults)localObject4).keySet().iterator(); localIterator1.hasNext(); ) { localObject6 = localIterator1.next();
/*  267 */           if ((localObject6 instanceof String)) {
/*  268 */             localObject7 = (String)localObject6;
/*  269 */             if (((String)localObject7).startsWith(this.prefix)) {
/*  270 */               ((TreeMap)localObject5).put(localObject7, ((UIDefaults)localObject4).get(localObject7));
/*      */             }
/*      */           }
/*      */         }
/*  274 */         if (i != 0)
/*  275 */           ((Map)localObject1).putAll((Map)localObject5);
/*      */         else {
/*  277 */           localObject1 = localObject5;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  286 */     Object localObject2 = new ArrayList();
/*      */ 
/*  288 */     Object localObject3 = new HashMap();
/*      */ 
/*  291 */     ArrayList localArrayList = new ArrayList();
/*      */ 
/*  298 */     Object localObject4 = (String)((Map)localObject1).get(this.prefix + ".States");
/*      */     int j;
/*  299 */     if (localObject4 != null) {
/*  300 */       localObject5 = ((String)localObject4).split(",");
/*  301 */       for (j = 0; j < localObject5.length; j++) {
/*  302 */         localObject5[j] = localObject5[j].trim();
/*  303 */         if (!State.isStandardStateName(localObject5[j]))
/*      */         {
/*  306 */           localObject6 = this.prefix + "." + localObject5[j];
/*  307 */           localObject7 = (State)((Map)localObject1).get(localObject6);
/*  308 */           if (localObject7 != null)
/*  309 */             ((List)localObject2).add(localObject7);
/*      */         }
/*      */         else {
/*  312 */           ((List)localObject2).add(State.getStandardState(localObject5[j]));
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*  319 */       if (((List)localObject2).size() > 0) {
/*  320 */         this.values.stateTypes = ((State[])((List)localObject2).toArray(new State[((List)localObject2).size()]));
/*      */       }
/*      */ 
/*  324 */       j = 1;
/*  325 */       for (localObject6 = ((List)localObject2).iterator(); ((Iterator)localObject6).hasNext(); ) { localObject7 = (State)((Iterator)localObject6).next();
/*  326 */         ((Map)localObject3).put(((State)localObject7).getName(), Integer.valueOf(j));
/*  327 */         j <<= 1;
/*      */       }
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  336 */       ((List)localObject2).add(State.Enabled);
/*  337 */       ((List)localObject2).add(State.MouseOver);
/*  338 */       ((List)localObject2).add(State.Pressed);
/*  339 */       ((List)localObject2).add(State.Disabled);
/*  340 */       ((List)localObject2).add(State.Focused);
/*  341 */       ((List)localObject2).add(State.Selected);
/*  342 */       ((List)localObject2).add(State.Default);
/*      */ 
/*  345 */       ((Map)localObject3).put("Enabled", Integer.valueOf(1));
/*  346 */       ((Map)localObject3).put("MouseOver", Integer.valueOf(2));
/*  347 */       ((Map)localObject3).put("Pressed", Integer.valueOf(4));
/*  348 */       ((Map)localObject3).put("Disabled", Integer.valueOf(8));
/*  349 */       ((Map)localObject3).put("Focused", Integer.valueOf(256));
/*  350 */       ((Map)localObject3).put("Selected", Integer.valueOf(512));
/*  351 */       ((Map)localObject3).put("Default", Integer.valueOf(1024));
/*      */     }
/*      */ 
/*  355 */     for (Object localObject5 = ((Map)localObject1).keySet().iterator(); ((Iterator)localObject5).hasNext(); ) { String str = (String)((Iterator)localObject5).next();
/*      */ 
/*  359 */       localObject6 = str.substring(this.prefix.length());
/*      */ 
/*  362 */       if ((((String)localObject6).indexOf('"') == -1) && (((String)localObject6).indexOf(':') == -1))
/*      */       {
/*  364 */         localObject6 = ((String)localObject6).substring(1);
/*      */ 
/*  372 */         localObject7 = null;
/*  373 */         Object localObject8 = null;
/*  374 */         int k = ((String)localObject6).indexOf(']');
/*  375 */         if (k < 0)
/*      */         {
/*  377 */           localObject8 = localObject6;
/*      */         } else {
/*  379 */           localObject7 = ((String)localObject6).substring(0, k);
/*  380 */           localObject8 = ((String)localObject6).substring(k + 2);
/*      */         }
/*      */ 
/*  385 */         if (localObject7 == null)
/*      */         {
/*  392 */           if ("contentMargins".equals(localObject8))
/*  393 */             this.values.contentMargins = ((Insets)((Map)localObject1).get(str));
/*  394 */           else if (!"States".equals(localObject8))
/*      */           {
/*  397 */             this.values.defaults.put(localObject8, ((Map)localObject1).get(str));
/*      */           }
/*      */ 
/*      */         }
/*      */         else
/*      */         {
/*  404 */           int m = 0;
/*      */ 
/*  407 */           int n = 0;
/*      */ 
/*  410 */           String[] arrayOfString = ((String)localObject7).split("\\+");
/*      */ 
/*  413 */           for (Object localObject10 : arrayOfString) {
/*  414 */             if (((Map)localObject3).containsKey(localObject10)) {
/*  415 */               n |= ((Integer)((Map)localObject3).get(localObject10)).intValue();
/*      */             }
/*      */             else
/*      */             {
/*  419 */               m = 1;
/*  420 */               break;
/*      */             }
/*      */           }
/*      */ 
/*  424 */           if (m == 0)
/*      */           {
/*  427 */             ??? = null;
/*  428 */             for (RuntimeState localRuntimeState : localArrayList) {
/*  429 */               if (localRuntimeState.state == n) {
/*  430 */                 ??? = localRuntimeState;
/*  431 */                 break;
/*      */               }
/*      */ 
/*      */             }
/*      */ 
/*  436 */             if (??? == null) {
/*  437 */               ??? = new RuntimeState(n, (String)localObject7, null);
/*  438 */               localArrayList.add(???);
/*      */             }
/*      */ 
/*  446 */             if ("backgroundPainter".equals(localObject8))
/*  447 */               ((RuntimeState)???).backgroundPainter = getPainter((Map)localObject1, str);
/*  448 */             else if ("foregroundPainter".equals(localObject8))
/*  449 */               ((RuntimeState)???).foregroundPainter = getPainter((Map)localObject1, str);
/*  450 */             else if ("borderPainter".equals(localObject8))
/*  451 */               ((RuntimeState)???).borderPainter = getPainter((Map)localObject1, str);
/*      */             else {
/*  453 */               ((RuntimeState)???).defaults.put(localObject8, ((Map)localObject1).get(str));
/*      */             }
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  460 */     Collections.sort(localArrayList, STATE_COMPARATOR);
/*      */ 
/*  463 */     this.values.states = ((RuntimeState[])localArrayList.toArray(new RuntimeState[localArrayList.size()]));
/*      */   }
/*      */ 
/*      */   private Painter getPainter(Map<String, Object> paramMap, String paramString) {
/*  467 */     Object localObject = paramMap.get(paramString);
/*  468 */     if ((localObject instanceof UIDefaults.LazyValue)) {
/*  469 */       localObject = ((UIDefaults.LazyValue)localObject).createValue(UIManager.getDefaults());
/*      */     }
/*  471 */     return (localObject instanceof Painter) ? (Painter)localObject : null;
/*      */   }
/*      */ 
/*      */   public Insets getInsets(SynthContext paramSynthContext, Insets paramInsets)
/*      */   {
/*  481 */     if (paramInsets == null) {
/*  482 */       paramInsets = new Insets(0, 0, 0, 0);
/*      */     }
/*      */ 
/*  485 */     Values localValues = getValues(paramSynthContext);
/*      */ 
/*  487 */     if (localValues.contentMargins == null) {
/*  488 */       paramInsets.bottom = (paramInsets.top = paramInsets.left = paramInsets.right = 0);
/*  489 */       return paramInsets;
/*      */     }
/*  491 */     paramInsets.bottom = localValues.contentMargins.bottom;
/*  492 */     paramInsets.top = localValues.contentMargins.top;
/*  493 */     paramInsets.left = localValues.contentMargins.left;
/*  494 */     paramInsets.right = localValues.contentMargins.right;
/*      */ 
/*  497 */     String str = (String)paramSynthContext.getComponent().getClientProperty("JComponent.sizeVariant");
/*      */ 
/*  499 */     if (str != null) {
/*  500 */       if ("large".equals(str))
/*      */       {
/*      */         Insets tmp125_124 = paramInsets; tmp125_124.bottom = ((int)(tmp125_124.bottom * 1.15D));
/*      */         Insets tmp139_138 = paramInsets; tmp139_138.top = ((int)(tmp139_138.top * 1.15D));
/*      */         Insets tmp153_152 = paramInsets; tmp153_152.left = ((int)(tmp153_152.left * 1.15D));
/*      */         Insets tmp167_166 = paramInsets; tmp167_166.right = ((int)(tmp167_166.right * 1.15D));
/*  505 */       } else if ("small".equals(str))
/*      */       {
/*      */         Insets tmp194_193 = paramInsets; tmp194_193.bottom = ((int)(tmp194_193.bottom * 0.857D));
/*      */         Insets tmp208_207 = paramInsets; tmp208_207.top = ((int)(tmp208_207.top * 0.857D));
/*      */         Insets tmp222_221 = paramInsets; tmp222_221.left = ((int)(tmp222_221.left * 0.857D));
/*      */         Insets tmp236_235 = paramInsets; tmp236_235.right = ((int)(tmp236_235.right * 0.857D));
/*  510 */       } else if ("mini".equals(str))
/*      */       {
/*      */         Insets tmp263_262 = paramInsets; tmp263_262.bottom = ((int)(tmp263_262.bottom * 0.714D));
/*      */         Insets tmp277_276 = paramInsets; tmp277_276.top = ((int)(tmp277_276.top * 0.714D));
/*      */         Insets tmp291_290 = paramInsets; tmp291_290.left = ((int)(tmp291_290.left * 0.714D));
/*      */         Insets tmp305_304 = paramInsets; tmp305_304.right = ((int)(tmp305_304.right * 0.714D));
/*      */       }
/*      */     }
/*  517 */     return paramInsets;
/*      */   }
/*      */ 
/*      */   protected Color getColorForState(SynthContext paramSynthContext, ColorType paramColorType)
/*      */   {
/*  541 */     String str = null;
/*  542 */     if (paramColorType == ColorType.BACKGROUND)
/*  543 */       str = "background";
/*  544 */     else if (paramColorType == ColorType.FOREGROUND)
/*      */     {
/*  546 */       str = "textForeground";
/*  547 */     } else if (paramColorType == ColorType.TEXT_BACKGROUND)
/*  548 */       str = "textBackground";
/*  549 */     else if (paramColorType == ColorType.TEXT_FOREGROUND)
/*  550 */       str = "textForeground";
/*  551 */     else if (paramColorType == ColorType.FOCUS)
/*  552 */       str = "focus";
/*  553 */     else if (paramColorType != null)
/*  554 */       str = paramColorType.toString();
/*      */     else {
/*  556 */       return DEFAULT_COLOR;
/*      */     }
/*  558 */     Color localColor = (Color)get(paramSynthContext, str);
/*      */ 
/*  560 */     if (localColor == null) localColor = DEFAULT_COLOR;
/*  561 */     return localColor;
/*      */   }
/*      */ 
/*      */   protected Font getFontForState(SynthContext paramSynthContext)
/*      */   {
/*  573 */     Font localFont = (Font)get(paramSynthContext, "font");
/*  574 */     if (localFont == null) localFont = UIManager.getFont("defaultFont");
/*      */ 
/*  578 */     String str = (String)paramSynthContext.getComponent().getClientProperty("JComponent.sizeVariant");
/*      */ 
/*  580 */     if (str != null) {
/*  581 */       if ("large".equals(str))
/*  582 */         localFont = localFont.deriveFont((float)Math.round(localFont.getSize2D() * 1.15D));
/*  583 */       else if ("small".equals(str))
/*  584 */         localFont = localFont.deriveFont((float)Math.round(localFont.getSize2D() * 0.857D));
/*  585 */       else if ("mini".equals(str)) {
/*  586 */         localFont = localFont.deriveFont((float)Math.round(localFont.getSize2D() * 0.714D));
/*      */       }
/*      */     }
/*  589 */     return localFont;
/*      */   }
/*      */ 
/*      */   public SynthPainter getPainter(SynthContext paramSynthContext)
/*      */   {
/*  599 */     return this.painter;
/*      */   }
/*      */ 
/*      */   public boolean isOpaque(SynthContext paramSynthContext)
/*      */   {
/*  611 */     if ("Table.cellRenderer".equals(paramSynthContext.getComponent().getName())) {
/*  612 */       return true;
/*      */     }
/*  614 */     Boolean localBoolean = (Boolean)get(paramSynthContext, "opaque");
/*  615 */     return localBoolean == null ? false : localBoolean.booleanValue();
/*      */   }
/*      */ 
/*      */   public Object get(SynthContext paramSynthContext, Object paramObject)
/*      */   {
/*  652 */     Values localValues = getValues(paramSynthContext);
/*      */ 
/*  655 */     String str1 = paramObject.toString();
/*  656 */     String str2 = str1.substring(str1.indexOf(".") + 1);
/*      */ 
/*  658 */     Object localObject = null;
/*  659 */     int i = getExtendedState(paramSynthContext, localValues);
/*      */ 
/*  662 */     this.tmpKey.init(str2, i);
/*  663 */     localObject = localValues.cache.get(this.tmpKey);
/*  664 */     int j = localObject != null ? 1 : 0;
/*  665 */     if (j == 0)
/*      */     {
/*  667 */       RuntimeState localRuntimeState = null;
/*  668 */       int[] arrayOfInt = { -1 };
/*  669 */       while ((localObject == null) && ((localRuntimeState = getNextState(localValues.states, arrayOfInt, i)) != null))
/*      */       {
/*  671 */         localObject = localRuntimeState.defaults.get(str2);
/*      */       }
/*      */ 
/*  674 */       if ((localObject == null) && (localValues.defaults != null)) {
/*  675 */         localObject = localValues.defaults.get(str2);
/*      */       }
/*      */ 
/*  679 */       if (localObject == null) localObject = UIManager.get(str1);
/*      */ 
/*  681 */       if ((localObject == null) && (str2.equals("focusInputMap"))) {
/*  682 */         localObject = super.get(paramSynthContext, str1);
/*      */       }
/*      */ 
/*  685 */       localValues.cache.put(new CacheKey(str2, i), localObject == null ? NULL : localObject);
/*      */     }
/*      */ 
/*  689 */     return localObject == NULL ? null : localObject;
/*      */   }
/*      */ 
/*      */   public Painter getBackgroundPainter(SynthContext paramSynthContext)
/*      */   {
/*  702 */     Values localValues = getValues(paramSynthContext);
/*  703 */     int i = getExtendedState(paramSynthContext, localValues);
/*  704 */     Painter localPainter = null;
/*      */ 
/*  707 */     this.tmpKey.init("backgroundPainter$$instance", i);
/*  708 */     localPainter = (Painter)localValues.cache.get(this.tmpKey);
/*  709 */     if (localPainter != null) return localPainter;
/*      */ 
/*  712 */     RuntimeState localRuntimeState = null;
/*  713 */     int[] arrayOfInt = { -1 };
/*  714 */     while ((localRuntimeState = getNextState(localValues.states, arrayOfInt, i)) != null) {
/*  715 */       if (localRuntimeState.backgroundPainter != null) {
/*  716 */         localPainter = localRuntimeState.backgroundPainter;
/*      */       }
/*      */     }
/*      */ 
/*  720 */     if (localPainter == null) localPainter = (Painter)get(paramSynthContext, "backgroundPainter");
/*  721 */     if (localPainter != null) {
/*  722 */       localValues.cache.put(new CacheKey("backgroundPainter$$instance", i), localPainter);
/*      */     }
/*  724 */     return localPainter;
/*      */   }
/*      */ 
/*      */   public Painter getForegroundPainter(SynthContext paramSynthContext)
/*      */   {
/*  737 */     Values localValues = getValues(paramSynthContext);
/*  738 */     int i = getExtendedState(paramSynthContext, localValues);
/*  739 */     Painter localPainter = null;
/*      */ 
/*  742 */     this.tmpKey.init("foregroundPainter$$instance", i);
/*  743 */     localPainter = (Painter)localValues.cache.get(this.tmpKey);
/*  744 */     if (localPainter != null) return localPainter;
/*      */ 
/*  747 */     RuntimeState localRuntimeState = null;
/*  748 */     int[] arrayOfInt = { -1 };
/*  749 */     while ((localRuntimeState = getNextState(localValues.states, arrayOfInt, i)) != null) {
/*  750 */       if (localRuntimeState.foregroundPainter != null) {
/*  751 */         localPainter = localRuntimeState.foregroundPainter;
/*      */       }
/*      */     }
/*      */ 
/*  755 */     if (localPainter == null) localPainter = (Painter)get(paramSynthContext, "foregroundPainter");
/*  756 */     if (localPainter != null) {
/*  757 */       localValues.cache.put(new CacheKey("foregroundPainter$$instance", i), localPainter);
/*      */     }
/*  759 */     return localPainter;
/*      */   }
/*      */ 
/*      */   public Painter getBorderPainter(SynthContext paramSynthContext)
/*      */   {
/*  772 */     Values localValues = getValues(paramSynthContext);
/*  773 */     int i = getExtendedState(paramSynthContext, localValues);
/*  774 */     Painter localPainter = null;
/*      */ 
/*  777 */     this.tmpKey.init("borderPainter$$instance", i);
/*  778 */     localPainter = (Painter)localValues.cache.get(this.tmpKey);
/*  779 */     if (localPainter != null) return localPainter;
/*      */ 
/*  782 */     RuntimeState localRuntimeState = null;
/*  783 */     int[] arrayOfInt = { -1 };
/*  784 */     while ((localRuntimeState = getNextState(localValues.states, arrayOfInt, i)) != null) {
/*  785 */       if (localRuntimeState.borderPainter != null) {
/*  786 */         localPainter = localRuntimeState.borderPainter;
/*      */       }
/*      */     }
/*      */ 
/*  790 */     if (localPainter == null) localPainter = (Painter)get(paramSynthContext, "borderPainter");
/*  791 */     if (localPainter != null) {
/*  792 */       localValues.cache.put(new CacheKey("borderPainter$$instance", i), localPainter);
/*      */     }
/*  794 */     return localPainter;
/*      */   }
/*      */ 
/*      */   private Values getValues(SynthContext paramSynthContext)
/*      */   {
/*  806 */     validate();
/*  807 */     return this.values;
/*      */   }
/*      */ 
/*      */   private boolean contains(String[] paramArrayOfString, String paramString)
/*      */   {
/*  822 */     assert (paramString != null);
/*  823 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/*  824 */       if (paramString.equals(paramArrayOfString[i])) {
/*  825 */         return true;
/*      */       }
/*      */     }
/*  828 */     return false;
/*      */   }
/*      */ 
/*      */   private int getExtendedState(SynthContext paramSynthContext, Values paramValues)
/*      */   {
/*  855 */     JComponent localJComponent = paramSynthContext.getComponent();
/*  856 */     int i = 0;
/*  857 */     int j = 1;
/*      */ 
/*  862 */     Object localObject1 = localJComponent.getClientProperty("Nimbus.State");
/*      */     Object localObject2;
/*  863 */     if (localObject1 != null) {
/*  864 */       String str1 = localObject1.toString();
/*  865 */       localObject2 = str1.split("\\+");
/*      */       String str2;
/*  866 */       if (paramValues.stateTypes == null)
/*      */       {
/*  868 */         for (str2 : localObject2) {
/*  869 */           State.StandardState localStandardState = State.getStandardState(str2);
/*  870 */           if (localStandardState != null) i |= localStandardState.getState();
/*      */         }
/*      */       }
/*      */       else {
/*  874 */         for (str2 : paramValues.stateTypes) {
/*  875 */           if (contains((String[])localObject2, str2.getName())) {
/*  876 */             i |= j;
/*      */           }
/*  878 */           j <<= 1;
/*      */         }
/*      */       }
/*      */     }
/*      */     else
/*      */     {
/*  884 */       if (paramValues.stateTypes == null) return paramSynthContext.getComponentState();
/*      */ 
/*  888 */       int k = paramSynthContext.getComponentState();
/*  889 */       for (Object localObject4 : paramValues.stateTypes) {
/*  890 */         if (localObject4.isInState(localJComponent, k)) {
/*  891 */           i |= j;
/*      */         }
/*  893 */         j <<= 1;
/*      */       }
/*      */     }
/*  896 */     return i;
/*      */   }
/*      */ 
/*      */   private RuntimeState getNextState(RuntimeState[] paramArrayOfRuntimeState, int[] paramArrayOfInt, int paramInt)
/*      */   {
/*  944 */     if ((paramArrayOfRuntimeState != null) && (paramArrayOfRuntimeState.length > 0)) {
/*  945 */       int i = 0;
/*  946 */       int j = -1;
/*  947 */       int k = -1;
/*      */ 
/*  951 */       if (paramInt == 0) {
/*  952 */         for (m = paramArrayOfRuntimeState.length - 1; m >= 0; m--) {
/*  953 */           if (paramArrayOfRuntimeState[m].state == 0) {
/*  954 */             paramArrayOfInt[0] = m;
/*  955 */             return paramArrayOfRuntimeState[m];
/*      */           }
/*      */         }
/*      */ 
/*  959 */         paramArrayOfInt[0] = -1;
/*  960 */         return null;
/*      */       }
/*      */ 
/*  968 */       int m = (paramArrayOfInt == null) || (paramArrayOfInt[0] == -1) ? paramArrayOfRuntimeState.length : paramArrayOfInt[0];
/*      */ 
/*  971 */       for (int n = m - 1; n >= 0; n--) {
/*  972 */         int i1 = paramArrayOfRuntimeState[n].state;
/*      */ 
/*  974 */         if (i1 == 0) {
/*  975 */           if (k == -1)
/*  976 */             k = n;
/*      */         }
/*  978 */         else if ((paramInt & i1) == i1)
/*      */         {
/*  985 */           int i2 = i1;
/*  986 */           i2 -= ((0xAAAAAAAA & i2) >>> 1);
/*  987 */           i2 = (i2 & 0x33333333) + (i2 >>> 2 & 0x33333333);
/*      */ 
/*  989 */           i2 = i2 + (i2 >>> 4) & 0xF0F0F0F;
/*  990 */           i2 += (i2 >>> 8);
/*  991 */           i2 += (i2 >>> 16);
/*  992 */           i2 &= 255;
/*  993 */           if (i2 > i) {
/*  994 */             j = n;
/*  995 */             i = i2;
/*      */           }
/*      */         }
/*      */       }
/*  999 */       if (j != -1) {
/* 1000 */         paramArrayOfInt[0] = j;
/* 1001 */         return paramArrayOfRuntimeState[j];
/*      */       }
/* 1003 */       if (k != -1) {
/* 1004 */         paramArrayOfInt[0] = k;
/* 1005 */         return paramArrayOfRuntimeState[k];
/*      */       }
/*      */     }
/* 1008 */     paramArrayOfInt[0] = -1;
/* 1009 */     return null;
/*      */   }
/*      */ 
/*      */   private static final class CacheKey
/*      */   {
/*      */     private String key;
/*      */     private int xstate;
/*      */ 
/*      */     CacheKey(Object paramObject, int paramInt)
/*      */     {
/* 1093 */       init(paramObject, paramInt);
/*      */     }
/*      */ 
/*      */     void init(Object paramObject, int paramInt) {
/* 1097 */       this.key = paramObject.toString();
/* 1098 */       this.xstate = paramInt;
/*      */     }
/*      */ 
/*      */     public boolean equals(Object paramObject)
/*      */     {
/* 1103 */       CacheKey localCacheKey = (CacheKey)paramObject;
/* 1104 */       if (paramObject == null) return false;
/* 1105 */       if (this.xstate != localCacheKey.xstate) return false;
/* 1106 */       if (!this.key.equals(localCacheKey.key)) return false;
/* 1107 */       return true;
/*      */     }
/*      */ 
/*      */     public int hashCode()
/*      */     {
/* 1112 */       int i = 3;
/* 1113 */       i = 29 * i + this.key.hashCode();
/* 1114 */       i = 29 * i + this.xstate;
/* 1115 */       return i;
/*      */     }
/*      */   }
/*      */ 
/*      */   private final class RuntimeState
/*      */     implements Cloneable
/*      */   {
/*      */     int state;
/*      */     Painter backgroundPainter;
/*      */     Painter foregroundPainter;
/*      */     Painter borderPainter;
/*      */     String stateName;
/* 1025 */     UIDefaults defaults = new UIDefaults(10, 0.7F);
/*      */ 
/*      */     private RuntimeState(int paramString, String arg3) {
/* 1028 */       this.state = paramString;
/*      */       Object localObject;
/* 1029 */       this.stateName = localObject;
/*      */     }
/*      */ 
/*      */     public String toString()
/*      */     {
/* 1034 */       return this.stateName;
/*      */     }
/*      */ 
/*      */     public RuntimeState clone()
/*      */     {
/* 1039 */       RuntimeState localRuntimeState = new RuntimeState(NimbusStyle.this, this.state, this.stateName);
/* 1040 */       localRuntimeState.backgroundPainter = this.backgroundPainter;
/* 1041 */       localRuntimeState.foregroundPainter = this.foregroundPainter;
/* 1042 */       localRuntimeState.borderPainter = this.borderPainter;
/* 1043 */       localRuntimeState.defaults.putAll(this.defaults);
/* 1044 */       return localRuntimeState;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static final class Values
/*      */   {
/* 1058 */     State[] stateTypes = null;
/*      */ 
/* 1064 */     NimbusStyle.RuntimeState[] states = null;
/*      */     Insets contentMargins;
/* 1072 */     UIDefaults defaults = new UIDefaults(10, 0.7F);
/*      */ 
/* 1081 */     Map<NimbusStyle.CacheKey, Object> cache = new HashMap();
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.nimbus.NimbusStyle
 * JD-Core Version:    0.6.2
 */