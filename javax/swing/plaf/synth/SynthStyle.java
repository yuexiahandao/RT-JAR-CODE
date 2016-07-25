/*      */ package javax.swing.plaf.synth;
/*      */ 
/*      */ import java.awt.Color;
/*      */ import java.awt.Font;
/*      */ import java.awt.Insets;
/*      */ import java.util.HashMap;
/*      */ import java.util.Map;
/*      */ import javax.swing.Icon;
/*      */ import javax.swing.JComponent;
/*      */ import javax.swing.JLabel;
/*      */ import javax.swing.LookAndFeel;
/*      */ import javax.swing.UIDefaults.LazyInputMap;
/*      */ import javax.swing.UIDefaults.LazyValue;
/*      */ import javax.swing.border.Border;
/*      */ import javax.swing.plaf.UIResource;
/*      */ import javax.swing.plaf.basic.BasicLookAndFeel;
/*      */ import javax.swing.text.JTextComponent;
/*      */ 
/*      */ public abstract class SynthStyle
/*      */ {
/*      */   private static Map<Object, Object> DEFAULT_VALUES;
/*   61 */   private static final SynthGraphicsUtils SYNTH_GRAPHICS = new SynthGraphicsUtils();
/*      */ 
/*      */   private static void populateDefaultValues()
/*      */   {
/*   68 */     UIDefaults.LazyInputMap localLazyInputMap1 = new UIDefaults.LazyInputMap(new Object[] { "SPACE", "pressed", "released SPACE", "released" });
/*      */ 
/*   72 */     DEFAULT_VALUES.put("Button.focusInputMap", localLazyInputMap1);
/*   73 */     DEFAULT_VALUES.put("CheckBox.focusInputMap", localLazyInputMap1);
/*   74 */     DEFAULT_VALUES.put("RadioButton.focusInputMap", localLazyInputMap1);
/*   75 */     DEFAULT_VALUES.put("ToggleButton.focusInputMap", localLazyInputMap1);
/*   76 */     DEFAULT_VALUES.put("SynthArrowButton.focusInputMap", localLazyInputMap1);
/*   77 */     DEFAULT_VALUES.put("List.dropLineColor", Color.BLACK);
/*   78 */     DEFAULT_VALUES.put("Tree.dropLineColor", Color.BLACK);
/*   79 */     DEFAULT_VALUES.put("Table.dropLineColor", Color.BLACK);
/*   80 */     DEFAULT_VALUES.put("Table.dropLineShortColor", Color.RED);
/*      */ 
/*   82 */     UIDefaults.LazyInputMap localLazyInputMap2 = new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy-to-clipboard", "ctrl V", "paste-from-clipboard", "ctrl X", "cut-to-clipboard", "COPY", "copy-to-clipboard", "PASTE", "paste-from-clipboard", "CUT", "cut-to-clipboard", "control INSERT", "copy-to-clipboard", "shift INSERT", "paste-from-clipboard", "shift DELETE", "cut-to-clipboard", "shift LEFT", "selection-backward", "shift KP_LEFT", "selection-backward", "shift RIGHT", "selection-forward", "shift KP_RIGHT", "selection-forward", "ctrl LEFT", "caret-previous-word", "ctrl KP_LEFT", "caret-previous-word", "ctrl RIGHT", "caret-next-word", "ctrl KP_RIGHT", "caret-next-word", "ctrl shift LEFT", "selection-previous-word", "ctrl shift KP_LEFT", "selection-previous-word", "ctrl shift RIGHT", "selection-next-word", "ctrl shift KP_RIGHT", "selection-next-word", "ctrl A", "select-all", "HOME", "caret-begin-line", "END", "caret-end-line", "shift HOME", "selection-begin-line", "shift END", "selection-end-line", "UP", "caret-up", "KP_UP", "caret-up", "DOWN", "caret-down", "KP_DOWN", "caret-down", "PAGE_UP", "page-up", "PAGE_DOWN", "page-down", "shift PAGE_UP", "selection-page-up", "shift PAGE_DOWN", "selection-page-down", "ctrl shift PAGE_UP", "selection-page-left", "ctrl shift PAGE_DOWN", "selection-page-right", "shift UP", "selection-up", "shift KP_UP", "selection-up", "shift DOWN", "selection-down", "shift KP_DOWN", "selection-down", "ENTER", "insert-break", "BACK_SPACE", "delete-previous", "shift BACK_SPACE", "delete-previous", "ctrl H", "delete-previous", "DELETE", "delete-next", "ctrl DELETE", "delete-next-word", "ctrl BACK_SPACE", "delete-previous-word", "RIGHT", "caret-forward", "LEFT", "caret-backward", "KP_RIGHT", "caret-forward", "KP_LEFT", "caret-backward", "TAB", "insert-tab", "ctrl BACK_SLASH", "unselect", "ctrl HOME", "caret-begin", "ctrl END", "caret-end", "ctrl shift HOME", "selection-begin", "ctrl shift END", "selection-end", "ctrl T", "next-link-action", "ctrl shift T", "previous-link-action", "ctrl SPACE", "activate-link-action", "control shift O", "toggle-componentOrientation" });
/*      */ 
/*  146 */     DEFAULT_VALUES.put("EditorPane.focusInputMap", localLazyInputMap2);
/*  147 */     DEFAULT_VALUES.put("TextArea.focusInputMap", localLazyInputMap2);
/*  148 */     DEFAULT_VALUES.put("TextPane.focusInputMap", localLazyInputMap2);
/*      */ 
/*  150 */     UIDefaults.LazyInputMap localLazyInputMap3 = new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy-to-clipboard", "ctrl V", "paste-from-clipboard", "ctrl X", "cut-to-clipboard", "COPY", "copy-to-clipboard", "PASTE", "paste-from-clipboard", "CUT", "cut-to-clipboard", "control INSERT", "copy-to-clipboard", "shift INSERT", "paste-from-clipboard", "shift DELETE", "cut-to-clipboard", "shift LEFT", "selection-backward", "shift KP_LEFT", "selection-backward", "shift RIGHT", "selection-forward", "shift KP_RIGHT", "selection-forward", "ctrl LEFT", "caret-previous-word", "ctrl KP_LEFT", "caret-previous-word", "ctrl RIGHT", "caret-next-word", "ctrl KP_RIGHT", "caret-next-word", "ctrl shift LEFT", "selection-previous-word", "ctrl shift KP_LEFT", "selection-previous-word", "ctrl shift RIGHT", "selection-next-word", "ctrl shift KP_RIGHT", "selection-next-word", "ctrl A", "select-all", "HOME", "caret-begin-line", "END", "caret-end-line", "shift HOME", "selection-begin-line", "shift END", "selection-end-line", "BACK_SPACE", "delete-previous", "shift BACK_SPACE", "delete-previous", "ctrl H", "delete-previous", "DELETE", "delete-next", "ctrl DELETE", "delete-next-word", "ctrl BACK_SPACE", "delete-previous-word", "RIGHT", "caret-forward", "LEFT", "caret-backward", "KP_RIGHT", "caret-forward", "KP_LEFT", "caret-backward", "ENTER", "notify-field-accept", "ctrl BACK_SLASH", "unselect", "control shift O", "toggle-componentOrientation" });
/*      */ 
/*  191 */     DEFAULT_VALUES.put("TextField.focusInputMap", localLazyInputMap3);
/*  192 */     DEFAULT_VALUES.put("PasswordField.focusInputMap", localLazyInputMap3);
/*      */ 
/*  195 */     DEFAULT_VALUES.put("ComboBox.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ESCAPE", "hidePopup", "PAGE_UP", "pageUpPassThrough", "PAGE_DOWN", "pageDownPassThrough", "HOME", "homePassThrough", "END", "endPassThrough", "DOWN", "selectNext", "KP_DOWN", "selectNext", "alt DOWN", "togglePopup", "alt KP_DOWN", "togglePopup", "alt UP", "togglePopup", "alt KP_UP", "togglePopup", "SPACE", "spacePopup", "ENTER", "enterPressed", "UP", "selectPrevious", "KP_UP", "selectPrevious" }));
/*      */ 
/*  214 */     DEFAULT_VALUES.put("Desktop.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl F5", "restore", "ctrl F4", "close", "ctrl F7", "move", "ctrl F8", "resize", "RIGHT", "right", "KP_RIGHT", "right", "shift RIGHT", "shrinkRight", "shift KP_RIGHT", "shrinkRight", "LEFT", "left", "KP_LEFT", "left", "shift LEFT", "shrinkLeft", "shift KP_LEFT", "shrinkLeft", "UP", "up", "KP_UP", "up", "shift UP", "shrinkUp", "shift KP_UP", "shrinkUp", "DOWN", "down", "KP_DOWN", "down", "shift DOWN", "shrinkDown", "shift KP_DOWN", "shrinkDown", "ESCAPE", "escape", "ctrl F9", "minimize", "ctrl F10", "maximize", "ctrl F6", "selectNextFrame", "ctrl TAB", "selectNextFrame", "ctrl alt F6", "selectNextFrame", "shift ctrl alt F6", "selectPreviousFrame", "ctrl F12", "navigateNext", "shift ctrl F12", "navigatePrevious" }));
/*      */ 
/*  247 */     DEFAULT_VALUES.put("FileChooser.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ESCAPE", "cancelSelection", "F2", "editFileName", "F5", "refresh", "BACK_SPACE", "Go Up", "ENTER", "approveSelection", "ctrl ENTER", "approveSelection" }));
/*      */ 
/*  257 */     DEFAULT_VALUES.put("FormattedTextField.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy-to-clipboard", "ctrl V", "paste-from-clipboard", "ctrl X", "cut-to-clipboard", "COPY", "copy-to-clipboard", "PASTE", "paste-from-clipboard", "CUT", "cut-to-clipboard", "control INSERT", "copy-to-clipboard", "shift INSERT", "paste-from-clipboard", "shift DELETE", "cut-to-clipboard", "shift LEFT", "selection-backward", "shift KP_LEFT", "selection-backward", "shift RIGHT", "selection-forward", "shift KP_RIGHT", "selection-forward", "ctrl LEFT", "caret-previous-word", "ctrl KP_LEFT", "caret-previous-word", "ctrl RIGHT", "caret-next-word", "ctrl KP_RIGHT", "caret-next-word", "ctrl shift LEFT", "selection-previous-word", "ctrl shift KP_LEFT", "selection-previous-word", "ctrl shift RIGHT", "selection-next-word", "ctrl shift KP_RIGHT", "selection-next-word", "ctrl A", "select-all", "HOME", "caret-begin-line", "END", "caret-end-line", "shift HOME", "selection-begin-line", "shift END", "selection-end-line", "BACK_SPACE", "delete-previous", "shift BACK_SPACE", "delete-previous", "ctrl H", "delete-previous", "DELETE", "delete-next", "ctrl DELETE", "delete-next-word", "ctrl BACK_SPACE", "delete-previous-word", "RIGHT", "caret-forward", "LEFT", "caret-backward", "KP_RIGHT", "caret-forward", "KP_LEFT", "caret-backward", "ENTER", "notify-field-accept", "ctrl BACK_SLASH", "unselect", "control shift O", "toggle-componentOrientation", "ESCAPE", "reset-field-edit", "UP", "increment", "KP_UP", "increment", "DOWN", "decrement", "KP_DOWN", "decrement" }));
/*      */ 
/*  305 */     DEFAULT_VALUES.put("InternalFrame.icon", LookAndFeel.makeIcon(BasicLookAndFeel.class, "icons/JavaCup16.png"));
/*      */ 
/*  309 */     DEFAULT_VALUES.put("InternalFrame.windowBindings", new Object[] { "shift ESCAPE", "showSystemMenu", "ctrl SPACE", "showSystemMenu", "ESCAPE", "hideSystemMenu" });
/*      */ 
/*  315 */     DEFAULT_VALUES.put("List.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "UP", "selectPreviousRow", "KP_UP", "selectPreviousRow", "shift UP", "selectPreviousRowExtendSelection", "shift KP_UP", "selectPreviousRowExtendSelection", "ctrl shift UP", "selectPreviousRowExtendSelection", "ctrl shift KP_UP", "selectPreviousRowExtendSelection", "ctrl UP", "selectPreviousRowChangeLead", "ctrl KP_UP", "selectPreviousRowChangeLead", "DOWN", "selectNextRow", "KP_DOWN", "selectNextRow", "shift DOWN", "selectNextRowExtendSelection", "shift KP_DOWN", "selectNextRowExtendSelection", "ctrl shift DOWN", "selectNextRowExtendSelection", "ctrl shift KP_DOWN", "selectNextRowExtendSelection", "ctrl DOWN", "selectNextRowChangeLead", "ctrl KP_DOWN", "selectNextRowChangeLead", "LEFT", "selectPreviousColumn", "KP_LEFT", "selectPreviousColumn", "shift LEFT", "selectPreviousColumnExtendSelection", "shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl shift LEFT", "selectPreviousColumnExtendSelection", "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl LEFT", "selectPreviousColumnChangeLead", "ctrl KP_LEFT", "selectPreviousColumnChangeLead", "RIGHT", "selectNextColumn", "KP_RIGHT", "selectNextColumn", "shift RIGHT", "selectNextColumnExtendSelection", "shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl shift RIGHT", "selectNextColumnExtendSelection", "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl RIGHT", "selectNextColumnChangeLead", "ctrl KP_RIGHT", "selectNextColumnChangeLead", "HOME", "selectFirstRow", "shift HOME", "selectFirstRowExtendSelection", "ctrl shift HOME", "selectFirstRowExtendSelection", "ctrl HOME", "selectFirstRowChangeLead", "END", "selectLastRow", "shift END", "selectLastRowExtendSelection", "ctrl shift END", "selectLastRowExtendSelection", "ctrl END", "selectLastRowChangeLead", "PAGE_UP", "scrollUp", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollUpExtendSelection", "ctrl PAGE_UP", "scrollUpChangeLead", "PAGE_DOWN", "scrollDown", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl PAGE_DOWN", "scrollDownChangeLead", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo" }));
/*      */ 
/*  383 */     DEFAULT_VALUES.put("List.focusInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[] { "LEFT", "selectNextColumn", "KP_LEFT", "selectNextColumn", "shift LEFT", "selectNextColumnExtendSelection", "shift KP_LEFT", "selectNextColumnExtendSelection", "ctrl shift LEFT", "selectNextColumnExtendSelection", "ctrl shift KP_LEFT", "selectNextColumnExtendSelection", "ctrl LEFT", "selectNextColumnChangeLead", "ctrl KP_LEFT", "selectNextColumnChangeLead", "RIGHT", "selectPreviousColumn", "KP_RIGHT", "selectPreviousColumn", "shift RIGHT", "selectPreviousColumnExtendSelection", "shift KP_RIGHT", "selectPreviousColumnExtendSelection", "ctrl shift RIGHT", "selectPreviousColumnExtendSelection", "ctrl shift KP_RIGHT", "selectPreviousColumnExtendSelection", "ctrl RIGHT", "selectPreviousColumnChangeLead", "ctrl KP_RIGHT", "selectPreviousColumnChangeLead" }));
/*      */ 
/*  403 */     DEFAULT_VALUES.put("MenuBar.windowBindings", new Object[] { "F10", "takeFocus" });
/*      */ 
/*  406 */     DEFAULT_VALUES.put("OptionPane.windowBindings", new Object[] { "ESCAPE", "close" });
/*      */ 
/*  409 */     DEFAULT_VALUES.put("RootPane.defaultButtonWindowKeyBindings", new Object[] { "ENTER", "press", "released ENTER", "release", "ctrl ENTER", "press", "ctrl released ENTER", "release" });
/*      */ 
/*  417 */     DEFAULT_VALUES.put("RootPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "shift F10", "postPopup" }));
/*      */ 
/*  422 */     DEFAULT_VALUES.put("ScrollBar.anecstorInputMap", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "positiveUnitIncrement", "KP_RIGHT", "positiveUnitIncrement", "DOWN", "positiveUnitIncrement", "KP_DOWN", "positiveUnitIncrement", "PAGE_DOWN", "positiveBlockIncrement", "LEFT", "negativeUnitIncrement", "KP_LEFT", "negativeUnitIncrement", "UP", "negativeUnitIncrement", "KP_UP", "negativeUnitIncrement", "PAGE_UP", "negativeBlockIncrement", "HOME", "minScroll", "END", "maxScroll" }));
/*      */ 
/*  438 */     DEFAULT_VALUES.put("ScrollBar.ancestorInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "negativeUnitIncrement", "KP_RIGHT", "negativeUnitIncrement", "LEFT", "positiveUnitIncrement", "KP_LEFT", "positiveUnitIncrement" }));
/*      */ 
/*  446 */     DEFAULT_VALUES.put("ScrollPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "unitScrollRight", "KP_RIGHT", "unitScrollRight", "DOWN", "unitScrollDown", "KP_DOWN", "unitScrollDown", "LEFT", "unitScrollLeft", "KP_LEFT", "unitScrollLeft", "UP", "unitScrollUp", "KP_UP", "unitScrollUp", "PAGE_UP", "scrollUp", "PAGE_DOWN", "scrollDown", "ctrl PAGE_UP", "scrollLeft", "ctrl PAGE_DOWN", "scrollRight", "ctrl HOME", "scrollHome", "ctrl END", "scrollEnd" }));
/*      */ 
/*  463 */     DEFAULT_VALUES.put("ScrollPane.ancestorInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[] { "ctrl PAGE_UP", "scrollRight", "ctrl PAGE_DOWN", "scrollLeft" }));
/*      */ 
/*  469 */     DEFAULT_VALUES.put("SplitPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "UP", "negativeIncrement", "DOWN", "positiveIncrement", "LEFT", "negativeIncrement", "RIGHT", "positiveIncrement", "KP_UP", "negativeIncrement", "KP_DOWN", "positiveIncrement", "KP_LEFT", "negativeIncrement", "KP_RIGHT", "positiveIncrement", "HOME", "selectMin", "END", "selectMax", "F8", "startResize", "F6", "toggleFocus", "ctrl TAB", "focusOutForward", "ctrl shift TAB", "focusOutBackward" }));
/*      */ 
/*  487 */     DEFAULT_VALUES.put("Spinner.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "UP", "increment", "KP_UP", "increment", "DOWN", "decrement", "KP_DOWN", "decrement" }));
/*      */ 
/*  495 */     DEFAULT_VALUES.put("Slider.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "positiveUnitIncrement", "KP_RIGHT", "positiveUnitIncrement", "DOWN", "negativeUnitIncrement", "KP_DOWN", "negativeUnitIncrement", "PAGE_DOWN", "negativeBlockIncrement", "ctrl PAGE_DOWN", "negativeBlockIncrement", "LEFT", "negativeUnitIncrement", "KP_LEFT", "negativeUnitIncrement", "UP", "positiveUnitIncrement", "KP_UP", "positiveUnitIncrement", "PAGE_UP", "positiveBlockIncrement", "ctrl PAGE_UP", "positiveBlockIncrement", "HOME", "minScroll", "END", "maxScroll" }));
/*      */ 
/*  513 */     DEFAULT_VALUES.put("Slider.focusInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "negativeUnitIncrement", "KP_RIGHT", "negativeUnitIncrement", "LEFT", "positiveUnitIncrement", "KP_LEFT", "positiveUnitIncrement" }));
/*      */ 
/*  521 */     DEFAULT_VALUES.put("TabbedPane.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl PAGE_DOWN", "navigatePageDown", "ctrl PAGE_UP", "navigatePageUp", "ctrl UP", "requestFocus", "ctrl KP_UP", "requestFocus" }));
/*      */ 
/*  529 */     DEFAULT_VALUES.put("TabbedPane.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "navigateRight", "KP_RIGHT", "navigateRight", "LEFT", "navigateLeft", "KP_LEFT", "navigateLeft", "UP", "navigateUp", "KP_UP", "navigateUp", "DOWN", "navigateDown", "KP_DOWN", "navigateDown", "ctrl DOWN", "requestFocusForVisibleComponent", "ctrl KP_DOWN", "requestFocusForVisibleComponent" }));
/*      */ 
/*  543 */     DEFAULT_VALUES.put("Table.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "RIGHT", "selectNextColumn", "KP_RIGHT", "selectNextColumn", "shift RIGHT", "selectNextColumnExtendSelection", "shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl shift RIGHT", "selectNextColumnExtendSelection", "ctrl shift KP_RIGHT", "selectNextColumnExtendSelection", "ctrl RIGHT", "selectNextColumnChangeLead", "ctrl KP_RIGHT", "selectNextColumnChangeLead", "LEFT", "selectPreviousColumn", "KP_LEFT", "selectPreviousColumn", "shift LEFT", "selectPreviousColumnExtendSelection", "shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl shift LEFT", "selectPreviousColumnExtendSelection", "ctrl shift KP_LEFT", "selectPreviousColumnExtendSelection", "ctrl LEFT", "selectPreviousColumnChangeLead", "ctrl KP_LEFT", "selectPreviousColumnChangeLead", "DOWN", "selectNextRow", "KP_DOWN", "selectNextRow", "shift DOWN", "selectNextRowExtendSelection", "shift KP_DOWN", "selectNextRowExtendSelection", "ctrl shift DOWN", "selectNextRowExtendSelection", "ctrl shift KP_DOWN", "selectNextRowExtendSelection", "ctrl DOWN", "selectNextRowChangeLead", "ctrl KP_DOWN", "selectNextRowChangeLead", "UP", "selectPreviousRow", "KP_UP", "selectPreviousRow", "shift UP", "selectPreviousRowExtendSelection", "shift KP_UP", "selectPreviousRowExtendSelection", "ctrl shift UP", "selectPreviousRowExtendSelection", "ctrl shift KP_UP", "selectPreviousRowExtendSelection", "ctrl UP", "selectPreviousRowChangeLead", "ctrl KP_UP", "selectPreviousRowChangeLead", "HOME", "selectFirstColumn", "shift HOME", "selectFirstColumnExtendSelection", "ctrl shift HOME", "selectFirstRowExtendSelection", "ctrl HOME", "selectFirstRow", "END", "selectLastColumn", "shift END", "selectLastColumnExtendSelection", "ctrl shift END", "selectLastRowExtendSelection", "ctrl END", "selectLastRow", "PAGE_UP", "scrollUpChangeSelection", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollLeftExtendSelection", "ctrl PAGE_UP", "scrollLeftChangeSelection", "PAGE_DOWN", "scrollDownChangeSelection", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollRightExtendSelection", "ctrl PAGE_DOWN", "scrollRightChangeSelection", "TAB", "selectNextColumnCell", "shift TAB", "selectPreviousColumnCell", "ENTER", "selectNextRowCell", "shift ENTER", "selectPreviousRowCell", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "ESCAPE", "cancel", "F2", "startEditing", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo", "F8", "focusHeader" }));
/*      */ 
/*  618 */     DEFAULT_VALUES.put("TableHeader.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "SPACE", "toggleSortOrder", "LEFT", "selectColumnToLeft", "KP_LEFT", "selectColumnToLeft", "RIGHT", "selectColumnToRight", "KP_RIGHT", "selectColumnToRight", "alt LEFT", "moveColumnLeft", "alt KP_LEFT", "moveColumnLeft", "alt RIGHT", "moveColumnRight", "alt KP_RIGHT", "moveColumnRight", "alt shift LEFT", "resizeLeft", "alt shift KP_LEFT", "resizeLeft", "alt shift RIGHT", "resizeRight", "alt shift KP_RIGHT", "resizeRight", "ESCAPE", "focusTable" }));
/*      */ 
/*  636 */     DEFAULT_VALUES.put("Tree.ancestorInputMap", new UIDefaults.LazyInputMap(new Object[] { "ESCAPE", "cancel" }));
/*      */ 
/*  640 */     DEFAULT_VALUES.put("Tree.focusInputMap", new UIDefaults.LazyInputMap(new Object[] { "ADD", "expand", "SUBTRACT", "collapse", "ctrl C", "copy", "ctrl V", "paste", "ctrl X", "cut", "COPY", "copy", "PASTE", "paste", "CUT", "cut", "control INSERT", "copy", "shift INSERT", "paste", "shift DELETE", "cut", "UP", "selectPrevious", "KP_UP", "selectPrevious", "shift UP", "selectPreviousExtendSelection", "shift KP_UP", "selectPreviousExtendSelection", "ctrl shift UP", "selectPreviousExtendSelection", "ctrl shift KP_UP", "selectPreviousExtendSelection", "ctrl UP", "selectPreviousChangeLead", "ctrl KP_UP", "selectPreviousChangeLead", "DOWN", "selectNext", "KP_DOWN", "selectNext", "shift DOWN", "selectNextExtendSelection", "shift KP_DOWN", "selectNextExtendSelection", "ctrl shift DOWN", "selectNextExtendSelection", "ctrl shift KP_DOWN", "selectNextExtendSelection", "ctrl DOWN", "selectNextChangeLead", "ctrl KP_DOWN", "selectNextChangeLead", "RIGHT", "selectChild", "KP_RIGHT", "selectChild", "LEFT", "selectParent", "KP_LEFT", "selectParent", "PAGE_UP", "scrollUpChangeSelection", "shift PAGE_UP", "scrollUpExtendSelection", "ctrl shift PAGE_UP", "scrollUpExtendSelection", "ctrl PAGE_UP", "scrollUpChangeLead", "PAGE_DOWN", "scrollDownChangeSelection", "shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl shift PAGE_DOWN", "scrollDownExtendSelection", "ctrl PAGE_DOWN", "scrollDownChangeLead", "HOME", "selectFirst", "shift HOME", "selectFirstExtendSelection", "ctrl shift HOME", "selectFirstExtendSelection", "ctrl HOME", "selectFirstChangeLead", "END", "selectLast", "shift END", "selectLastExtendSelection", "ctrl shift END", "selectLastExtendSelection", "ctrl END", "selectLastChangeLead", "F2", "startEditing", "ctrl A", "selectAll", "ctrl SLASH", "selectAll", "ctrl BACK_SLASH", "clearSelection", "ctrl LEFT", "scrollLeft", "ctrl KP_LEFT", "scrollLeft", "ctrl RIGHT", "scrollRight", "ctrl KP_RIGHT", "scrollRight", "SPACE", "addToSelection", "ctrl SPACE", "toggleAndAnchor", "shift SPACE", "extendTo", "ctrl shift SPACE", "moveSelectionTo" }));
/*      */ 
/*  702 */     DEFAULT_VALUES.put("Tree.focusInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[] { "RIGHT", "selectParent", "KP_RIGHT", "selectParent", "LEFT", "selectChild", "KP_LEFT", "selectChild" }));
/*      */   }
/*      */ 
/*      */   private static Object getDefaultValue(Object paramObject)
/*      */   {
/*  716 */     synchronized (SynthStyle.class) {
/*  717 */       if (DEFAULT_VALUES == null) {
/*  718 */         DEFAULT_VALUES = new HashMap();
/*  719 */         populateDefaultValues();
/*      */       }
/*  721 */       Object localObject1 = DEFAULT_VALUES.get(paramObject);
/*  722 */       if ((localObject1 instanceof UIDefaults.LazyValue)) {
/*  723 */         localObject1 = ((UIDefaults.LazyValue)localObject1).createValue(null);
/*  724 */         DEFAULT_VALUES.put(paramObject, localObject1);
/*      */       }
/*  726 */       return localObject1;
/*      */     }
/*      */   }
/*      */ 
/*      */   public SynthGraphicsUtils getGraphicsUtils(SynthContext paramSynthContext)
/*      */   {
/*  743 */     return SYNTH_GRAPHICS;
/*      */   }
/*      */ 
/*      */   public Color getColor(SynthContext paramSynthContext, ColorType paramColorType)
/*      */   {
/*  759 */     JComponent localJComponent = paramSynthContext.getComponent();
/*  760 */     Region localRegion = paramSynthContext.getRegion();
/*      */ 
/*  762 */     if ((paramSynthContext.getComponentState() & 0x8) != 0)
/*      */     {
/*  770 */       if ((localJComponent instanceof JTextComponent)) {
/*  771 */         localObject = (JTextComponent)localJComponent;
/*  772 */         Color localColor = ((JTextComponent)localObject).getDisabledTextColor();
/*  773 */         if ((localColor == null) || ((localColor instanceof UIResource)))
/*  774 */           return getColorForState(paramSynthContext, paramColorType);
/*      */       }
/*  776 */       else if (((localJComponent instanceof JLabel)) && ((paramColorType == ColorType.FOREGROUND) || (paramColorType == ColorType.TEXT_FOREGROUND)))
/*      */       {
/*  779 */         return getColorForState(paramSynthContext, paramColorType);
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  785 */     Object localObject = null;
/*  786 */     if (!localRegion.isSubregion()) {
/*  787 */       if (paramColorType == ColorType.BACKGROUND) {
/*  788 */         localObject = localJComponent.getBackground();
/*      */       }
/*  790 */       else if (paramColorType == ColorType.FOREGROUND) {
/*  791 */         localObject = localJComponent.getForeground();
/*      */       }
/*  793 */       else if (paramColorType == ColorType.TEXT_FOREGROUND) {
/*  794 */         localObject = localJComponent.getForeground();
/*      */       }
/*      */     }
/*      */ 
/*  798 */     if ((localObject == null) || ((localObject instanceof UIResource)))
/*      */     {
/*  800 */       localObject = getColorForState(paramSynthContext, paramColorType);
/*      */     }
/*      */ 
/*  803 */     if (localObject == null)
/*      */     {
/*  805 */       if ((paramColorType == ColorType.BACKGROUND) || (paramColorType == ColorType.TEXT_BACKGROUND))
/*      */       {
/*  807 */         return localJComponent.getBackground();
/*      */       }
/*  809 */       if ((paramColorType == ColorType.FOREGROUND) || (paramColorType == ColorType.TEXT_FOREGROUND))
/*      */       {
/*  811 */         return localJComponent.getForeground();
/*      */       }
/*      */     }
/*  814 */     return localObject;
/*      */   }
/*      */ 
/*      */   protected abstract Color getColorForState(SynthContext paramSynthContext, ColorType paramColorType);
/*      */ 
/*      */   public Font getFont(SynthContext paramSynthContext)
/*      */   {
/*  838 */     JComponent localJComponent = paramSynthContext.getComponent();
/*  839 */     if (paramSynthContext.getComponentState() == 1) {
/*  840 */       return localJComponent.getFont();
/*      */     }
/*  842 */     Font localFont = localJComponent.getFont();
/*  843 */     if ((localFont != null) && (!(localFont instanceof UIResource))) {
/*  844 */       return localFont;
/*      */     }
/*  846 */     return getFontForState(paramSynthContext);
/*      */   }
/*      */ 
/*      */   protected abstract Font getFontForState(SynthContext paramSynthContext);
/*      */ 
/*      */   public Insets getInsets(SynthContext paramSynthContext, Insets paramInsets)
/*      */   {
/*  866 */     if (paramInsets == null) {
/*  867 */       paramInsets = new Insets(0, 0, 0, 0);
/*      */     }
/*  869 */     paramInsets.top = (paramInsets.bottom = paramInsets.left = paramInsets.right = 0);
/*  870 */     return paramInsets;
/*      */   }
/*      */ 
/*      */   public SynthPainter getPainter(SynthContext paramSynthContext)
/*      */   {
/*  881 */     return null;
/*      */   }
/*      */ 
/*      */   public boolean isOpaque(SynthContext paramSynthContext)
/*      */   {
/*  891 */     return true;
/*      */   }
/*      */ 
/*      */   public Object get(SynthContext paramSynthContext, Object paramObject)
/*      */   {
/*  902 */     return getDefaultValue(paramObject);
/*      */   }
/*      */ 
/*      */   void installDefaults(SynthContext paramSynthContext, SynthUI paramSynthUI)
/*      */   {
/*  908 */     if (!paramSynthContext.isSubregion()) {
/*  909 */       JComponent localJComponent = paramSynthContext.getComponent();
/*  910 */       Border localBorder = localJComponent.getBorder();
/*      */ 
/*  912 */       if ((localBorder == null) || ((localBorder instanceof UIResource))) {
/*  913 */         localJComponent.setBorder(new SynthBorder(paramSynthUI, getInsets(paramSynthContext, null)));
/*      */       }
/*      */     }
/*  916 */     installDefaults(paramSynthContext);
/*      */   }
/*      */ 
/*      */   public void installDefaults(SynthContext paramSynthContext)
/*      */   {
/*  927 */     if (!paramSynthContext.isSubregion()) {
/*  928 */       JComponent localJComponent = paramSynthContext.getComponent();
/*  929 */       Region localRegion = paramSynthContext.getRegion();
/*  930 */       Font localFont = localJComponent.getFont();
/*      */ 
/*  932 */       if ((localFont == null) || ((localFont instanceof UIResource))) {
/*  933 */         localJComponent.setFont(getFontForState(paramSynthContext));
/*      */       }
/*  935 */       Color localColor1 = localJComponent.getBackground();
/*  936 */       if ((localColor1 == null) || ((localColor1 instanceof UIResource))) {
/*  937 */         localJComponent.setBackground(getColorForState(paramSynthContext, ColorType.BACKGROUND));
/*      */       }
/*      */ 
/*  940 */       Color localColor2 = localJComponent.getForeground();
/*  941 */       if ((localColor2 == null) || ((localColor2 instanceof UIResource))) {
/*  942 */         localJComponent.setForeground(getColorForState(paramSynthContext, ColorType.FOREGROUND));
/*      */       }
/*      */ 
/*  945 */       LookAndFeel.installProperty(localJComponent, "opaque", Boolean.valueOf(isOpaque(paramSynthContext)));
/*      */     }
/*      */   }
/*      */ 
/*      */   public void uninstallDefaults(SynthContext paramSynthContext)
/*      */   {
/*  960 */     if (!paramSynthContext.isSubregion())
/*      */     {
/*  967 */       JComponent localJComponent = paramSynthContext.getComponent();
/*  968 */       Border localBorder = localJComponent.getBorder();
/*      */ 
/*  970 */       if ((localBorder instanceof UIResource))
/*  971 */         localJComponent.setBorder(null);
/*      */     }
/*      */   }
/*      */ 
/*      */   public int getInt(SynthContext paramSynthContext, Object paramObject, int paramInt)
/*      */   {
/*  989 */     Object localObject = get(paramSynthContext, paramObject);
/*      */ 
/*  991 */     if ((localObject instanceof Number)) {
/*  992 */       return ((Number)localObject).intValue();
/*      */     }
/*  994 */     return paramInt;
/*      */   }
/*      */ 
/*      */   public boolean getBoolean(SynthContext paramSynthContext, Object paramObject, boolean paramBoolean)
/*      */   {
/* 1009 */     Object localObject = get(paramSynthContext, paramObject);
/*      */ 
/* 1011 */     if ((localObject instanceof Boolean)) {
/* 1012 */       return ((Boolean)localObject).booleanValue();
/*      */     }
/* 1014 */     return paramBoolean;
/*      */   }
/*      */ 
/*      */   public Icon getIcon(SynthContext paramSynthContext, Object paramObject)
/*      */   {
/* 1026 */     Object localObject = get(paramSynthContext, paramObject);
/*      */ 
/* 1028 */     if ((localObject instanceof Icon)) {
/* 1029 */       return (Icon)localObject;
/*      */     }
/* 1031 */     return null;
/*      */   }
/*      */ 
/*      */   public String getString(SynthContext paramSynthContext, Object paramObject, String paramString)
/*      */   {
/* 1046 */     Object localObject = get(paramSynthContext, paramObject);
/*      */ 
/* 1048 */     if ((localObject instanceof String)) {
/* 1049 */       return (String)localObject;
/*      */     }
/* 1051 */     return paramString;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.plaf.synth.SynthStyle
 * JD-Core Version:    0.6.2
 */