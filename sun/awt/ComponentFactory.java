package sun.awt;

import java.awt.AWTException;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.CheckboxMenuItem;
import java.awt.Choice;
import java.awt.Dialog;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.List;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.PopupMenu;
import java.awt.Robot;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.Window;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.InvalidDnDOperationException;
import java.awt.dnd.peer.DragSourceContextPeer;
import java.awt.peer.ButtonPeer;
import java.awt.peer.CanvasPeer;
import java.awt.peer.CheckboxMenuItemPeer;
import java.awt.peer.CheckboxPeer;
import java.awt.peer.ChoicePeer;
import java.awt.peer.DialogPeer;
import java.awt.peer.FileDialogPeer;
import java.awt.peer.FontPeer;
import java.awt.peer.FramePeer;
import java.awt.peer.LabelPeer;
import java.awt.peer.ListPeer;
import java.awt.peer.MenuBarPeer;
import java.awt.peer.MenuItemPeer;
import java.awt.peer.MenuPeer;
import java.awt.peer.PanelPeer;
import java.awt.peer.PopupMenuPeer;
import java.awt.peer.RobotPeer;
import java.awt.peer.ScrollPanePeer;
import java.awt.peer.ScrollbarPeer;
import java.awt.peer.TextAreaPeer;
import java.awt.peer.TextFieldPeer;
import java.awt.peer.WindowPeer;

public abstract interface ComponentFactory
{
  public abstract CanvasPeer createCanvas(Canvas paramCanvas)
    throws HeadlessException;

  public abstract PanelPeer createPanel(Panel paramPanel)
    throws HeadlessException;

  public abstract WindowPeer createWindow(Window paramWindow)
    throws HeadlessException;

  public abstract FramePeer createFrame(Frame paramFrame)
    throws HeadlessException;

  public abstract DialogPeer createDialog(Dialog paramDialog)
    throws HeadlessException;

  public abstract ButtonPeer createButton(Button paramButton)
    throws HeadlessException;

  public abstract TextFieldPeer createTextField(TextField paramTextField)
    throws HeadlessException;

  public abstract ChoicePeer createChoice(Choice paramChoice)
    throws HeadlessException;

  public abstract LabelPeer createLabel(Label paramLabel)
    throws HeadlessException;

  public abstract ListPeer createList(List paramList)
    throws HeadlessException;

  public abstract CheckboxPeer createCheckbox(Checkbox paramCheckbox)
    throws HeadlessException;

  public abstract ScrollbarPeer createScrollbar(Scrollbar paramScrollbar)
    throws HeadlessException;

  public abstract ScrollPanePeer createScrollPane(ScrollPane paramScrollPane)
    throws HeadlessException;

  public abstract TextAreaPeer createTextArea(TextArea paramTextArea)
    throws HeadlessException;

  public abstract FileDialogPeer createFileDialog(FileDialog paramFileDialog)
    throws HeadlessException;

  public abstract MenuBarPeer createMenuBar(MenuBar paramMenuBar)
    throws HeadlessException;

  public abstract MenuPeer createMenu(Menu paramMenu)
    throws HeadlessException;

  public abstract PopupMenuPeer createPopupMenu(PopupMenu paramPopupMenu)
    throws HeadlessException;

  public abstract MenuItemPeer createMenuItem(MenuItem paramMenuItem)
    throws HeadlessException;

  public abstract CheckboxMenuItemPeer createCheckboxMenuItem(CheckboxMenuItem paramCheckboxMenuItem)
    throws HeadlessException;

  public abstract DragSourceContextPeer createDragSourceContextPeer(DragGestureEvent paramDragGestureEvent)
    throws InvalidDnDOperationException, HeadlessException;

  public abstract FontPeer getFontPeer(String paramString, int paramInt);

  public abstract RobotPeer createRobot(Robot paramRobot, GraphicsDevice paramGraphicsDevice)
    throws AWTException, HeadlessException;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.ComponentFactory
 * JD-Core Version:    0.6.2
 */