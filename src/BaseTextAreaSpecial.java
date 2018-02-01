/**
 * Copyright (C) 2018 冰原
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package com.xiboliya.snowpad;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.undo.UndoManager;
import java.awt.Dimension;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * 实现撤销管理器和右键快捷菜单的JTextArea控件
 * 
 * @author 冰原
 * 
 */
public class BaseTextAreaSpecial extends JTextArea implements ActionListener,
    CaretListener, UndoableEditListener, MouseListener, FocusListener {
  private static final long serialVersionUID = 1L;
  private UndoManager undoManager = new UndoManager(); // 撤销管理器
  private Clipboard clip = this.getToolkit().getSystemClipboard(); // 剪贴板
  private JPopupMenu popMenu = new JPopupMenu();
  private JMenuItem itemPopUnDo = new JMenuItem("撤销(U)", 'U');
  private JMenuItem itemPopReDo = new JMenuItem("重做(Y)", 'Y');
  private JMenuItem itemPopCut = new JMenuItem("剪切(T)", 'T');
  private JMenuItem itemPopCopy = new JMenuItem("复制(C)", 'C');
  private JMenuItem itemPopPaste = new JMenuItem("粘贴(P)", 'P');
  private JMenuItem itemPopDel = new JMenuItem("删除(D)", 'D');
  private JMenuItem itemPopSelAll = new JMenuItem("全选(A)", 'A');
  // 用于撤销的动作
  private Action actUndo = new AbstractAction() {
    private static final long serialVersionUID = 1L;

    public void actionPerformed(ActionEvent e) {
      undoAction();
    }
  };
  // 用于重做的动作
  private Action actRedo = new AbstractAction() {
    private static final long serialVersionUID = 1L;

    public void actionPerformed(ActionEvent e) {
      redoAction();
    }
  };

  /**
   * 默认的构造方法
   */
  public BaseTextAreaSpecial() {
    super();
    this.init();
  }

  /**
   * 带参数的构造方法
   * 
   * @param str
   *          初始化的字符串
   */
  public BaseTextAreaSpecial(String str) {
    super(str);
    this.init();
  }

  /**
   * 带参数的构造方法
   * 
   * @param isSetDocument
   *          是否重新设置Document文档
   * @param pattern
   *          限制用户输入的正则表达式
   */
  public BaseTextAreaSpecial(boolean isSetDocument, String pattern) {
    this();
    if (isSetDocument) {
      this.setDocument(new BasePlainDocument(pattern));
    }
  }

  @Override
  public void setEditable(boolean editable) {
    super.setEditable(editable);
    if (!editable) {
      this.itemPopUnDo.setEnabled(false);
      this.itemPopReDo.setEnabled(false);
      this.itemPopCut.setEnabled(false);
      this.itemPopPaste.setEnabled(false);
      this.itemPopDel.setEnabled(false);
    }
  }

  /**
   * 初始化界面和设置
   */
  private void init() {
    this.addPopMenu();
    this.setMenuDefault();
    this.addListeners();
    this.setInputActionMap();
  }

  /**
   * 初始化快捷菜单
   */
  private void addPopMenu() {
    this.popMenu.add(this.itemPopUnDo);
    this.popMenu.add(this.itemPopReDo);
    this.popMenu.addSeparator();
    this.popMenu.add(this.itemPopCut);
    this.popMenu.add(this.itemPopCopy);
    this.popMenu.add(this.itemPopPaste);
    this.popMenu.add(this.itemPopDel);
    this.popMenu.addSeparator();
    this.popMenu.add(this.itemPopSelAll);
    Dimension popSize = this.popMenu.getPreferredSize();
    popSize.width += popSize.width / 5; // 为了美观，适当加宽菜单的显示
    this.popMenu.setPopupSize(popSize);
  }

  /**
   * 实现快捷键与动作的绑定
   */
  private void setInputActionMap() {
    InputMap inputMap = this.getInputMap();
    ActionMap actionMap = this.getActionMap();
    // 将组合键与特定字符串绑定（如果未建立与Action的映射，则会屏蔽该组合键）
    inputMap.put(
        KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK),
        Util.CTRL_Z);
    inputMap.put(
        KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK),
        Util.CTRL_Y);
    inputMap.put(
        KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK),
        Util.CTRL_H);
    // 将特定字符串与Action建立映射
    actionMap.put(Util.CTRL_Z, this.actUndo);
    actionMap.put(Util.CTRL_Y, this.actRedo);
  }

  /**
   * 设置快捷菜单的初始状态
   */
  private void setMenuDefault() {
    this.itemPopCopy.setEnabled(false);
    this.itemPopCut.setEnabled(false);
    this.itemPopDel.setEnabled(false);
    this.itemPopUnDo.setEnabled(false);
    this.itemPopReDo.setEnabled(false);
  }

  /**
   * 为本控件和快捷菜单添加各种事件监听器
   */
  private void addListeners() {
    this.addMouseListener(this);
    this.addCaretListener(this);
    this.getDocument().addUndoableEditListener(this);
    this.addFocusListener(this);
    this.itemPopCopy.addActionListener(this);
    this.itemPopCut.addActionListener(this);
    this.itemPopDel.addActionListener(this);
    this.itemPopPaste.addActionListener(this);
    this.itemPopReDo.addActionListener(this);
    this.itemPopSelAll.addActionListener(this);
    this.itemPopUnDo.addActionListener(this);
  }

  /**
   * 为各菜单项添加事件的处理方法
   */
  public void actionPerformed(ActionEvent e) {
    if (this.itemPopUnDo.equals(e.getSource())) {
      this.undoAction();
    } else if (this.itemPopReDo.equals(e.getSource())) {
      this.redoAction();
    } else if (this.itemPopCut.equals(e.getSource())) {
      this.cut();
    } else if (this.itemPopCopy.equals(e.getSource())) {
      this.copy();
    } else if (this.itemPopPaste.equals(e.getSource())) {
      this.paste();
    } else if (this.itemPopDel.equals(e.getSource())) {
      this.deleteText();
    } else if (this.itemPopSelAll.equals(e.getSource())) {
      this.selectAll();
    }
  }

  @Override
  public void paste() {
    try {
      Transferable tf = this.clip.getContents(this);
      if (tf != null) {
        String str = tf.getTransferData(DataFlavor.stringFlavor).toString(); // 如果剪贴板内的内容不是文本，则将抛出异常
        if (str != null) {
          str = str.replaceAll(LineSeparator.WINDOWS.toString(),
              LineSeparator.UNIX.toString()); // 将Windows格式的换行符\r\n，转换为UNIX/Linux格式
          str = str.replaceAll(LineSeparator.MACINTOSH.toString(),
              LineSeparator.UNIX.toString()); // 为了容错，将可能残余的\r字符替换为\n
          this.replaceSelection(str);
        }
      }
    } catch (Exception x) {
      // 剪贴板异常
      // x.printStackTrace();
    }
  }

  /**
   * "删除"的处理方法
   */
  private void deleteText() {
    this.replaceSelection("");
  }

  /**
   * "撤销"的处理方法
   */
  private void undoAction() {
    if (this.undoManager.canUndo()) { // 判断是否可以撤销
      this.undoManager.undo(); // 执行撤销操作
    }
    this.setMenuStateUndoRedo();
  }

  /**
   * "重做"的处理方法
   */
  private void redoAction() {
    if (this.undoManager.canRedo()) { // 判断是否可以重做
      this.undoManager.redo(); // 执行重做操作
    }
    this.setMenuStateUndoRedo();
  }

  /**
   * 设置撤销和重做菜单的状态
   */
  private void setMenuStateUndoRedo() {
    if (!this.isEditable()) {
      this.itemPopUnDo.setEnabled(false);
      this.itemPopReDo.setEnabled(false);
    } else {
      this.itemPopUnDo.setEnabled(this.undoManager.canUndo());
      this.itemPopReDo.setEnabled(this.undoManager.canRedo());
    }
  }

  /**
   * 根据本控件中选择的字符串是否为空，设置相关菜单的状态
   * 
   * @param isNull
   *          选择是否为空
   */
  private void setMenuStateBySelectedText(boolean isNull) {
    if (!this.isEditable()) {
      this.itemPopCut.setEnabled(false);
      this.itemPopDel.setEnabled(false);
    } else {
      this.itemPopCut.setEnabled(isNull);
      this.itemPopDel.setEnabled(isNull);
    }
    this.itemPopCopy.setEnabled(isNull);
  }

  /**
   * 当本控件中的光标变化时，将触发此事件
   */
  public void caretUpdate(CaretEvent e) {
    String selText = this.getSelectedText();
    if (selText != null && selText.length() > 0) {
      this.setMenuStateBySelectedText(true);
    } else {
      this.setMenuStateBySelectedText(false);
    }
  }

  /**
   * 当本控件中的文本发生变化时，将触发此事件
   */
  public void undoableEditHappened(UndoableEditEvent e) {
    this.undoManager.addEdit(e.getEdit());
    this.setMenuStateUndoRedo();
  }

  /**
   * 组件获得键盘焦点时调用
   */
  public void focusGained(FocusEvent e) {
    try {
      Transferable tf = this.clip.getContents(this);
      if (tf == null) {
        this.itemPopPaste.setEnabled(false);
      } else {
        String str = tf.getTransferData(DataFlavor.stringFlavor).toString(); // 如果剪贴板内的内容不是文本，则将抛出异常
        if (str != null && str.length() > 0 && this.isEditable()) {
          this.itemPopPaste.setEnabled(true);
        }
      }
    } catch (Exception x) {
      // 剪贴板异常
      // x.printStackTrace();
      this.itemPopPaste.setEnabled(false);
    }
  }

  /**
   * 组件失去键盘焦点时调用
   */
  public void focusLost(FocusEvent e) {
  }

  /**
   * 鼠标按键在组件上单击（按下并释放）时调用
   */
  public void mouseClicked(MouseEvent e) {
    this.requestFocus(); // 当鼠标点击时（左键或右键），获得焦点
    if (e.getButton() == MouseEvent.BUTTON3) { // 点击右键时，显示快捷菜单
      if (this.isEnabled() && this.isFocusable()) {
        this.popMenu.show(this, e.getX(), e.getY());
      }
    }
  }

  /**
   * 鼠标进入到组件上时调用
   */
  public void mouseEntered(MouseEvent e) {
  }

  /**
   * 鼠标离开组件时调用
   */
  public void mouseExited(MouseEvent e) {
  }

  /**
   * 鼠标按键在组件上按下时调用
   */
  public void mousePressed(MouseEvent e) {
  }

  /**
   * 鼠标按钮在组件上释放时调用
   */
  public void mouseReleased(MouseEvent e) {
  }

}
