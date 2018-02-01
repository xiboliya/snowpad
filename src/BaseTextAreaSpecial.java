/**
 * Copyright (C) 2018 ��ԭ
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
 * ʵ�ֳ������������Ҽ���ݲ˵���JTextArea�ؼ�
 * 
 * @author ��ԭ
 * 
 */
public class BaseTextAreaSpecial extends JTextArea implements ActionListener,
    CaretListener, UndoableEditListener, MouseListener, FocusListener {
  private static final long serialVersionUID = 1L;
  private UndoManager undoManager = new UndoManager(); // ����������
  private Clipboard clip = this.getToolkit().getSystemClipboard(); // ������
  private JPopupMenu popMenu = new JPopupMenu();
  private JMenuItem itemPopUnDo = new JMenuItem("����(U)", 'U');
  private JMenuItem itemPopReDo = new JMenuItem("����(Y)", 'Y');
  private JMenuItem itemPopCut = new JMenuItem("����(T)", 'T');
  private JMenuItem itemPopCopy = new JMenuItem("����(C)", 'C');
  private JMenuItem itemPopPaste = new JMenuItem("ճ��(P)", 'P');
  private JMenuItem itemPopDel = new JMenuItem("ɾ��(D)", 'D');
  private JMenuItem itemPopSelAll = new JMenuItem("ȫѡ(A)", 'A');
  // ���ڳ����Ķ���
  private Action actUndo = new AbstractAction() {
    private static final long serialVersionUID = 1L;

    public void actionPerformed(ActionEvent e) {
      undoAction();
    }
  };
  // ���������Ķ���
  private Action actRedo = new AbstractAction() {
    private static final long serialVersionUID = 1L;

    public void actionPerformed(ActionEvent e) {
      redoAction();
    }
  };

  /**
   * Ĭ�ϵĹ��췽��
   */
  public BaseTextAreaSpecial() {
    super();
    this.init();
  }

  /**
   * �������Ĺ��췽��
   * 
   * @param str
   *          ��ʼ�����ַ���
   */
  public BaseTextAreaSpecial(String str) {
    super(str);
    this.init();
  }

  /**
   * �������Ĺ��췽��
   * 
   * @param isSetDocument
   *          �Ƿ���������Document�ĵ�
   * @param pattern
   *          �����û������������ʽ
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
   * ��ʼ�����������
   */
  private void init() {
    this.addPopMenu();
    this.setMenuDefault();
    this.addListeners();
    this.setInputActionMap();
  }

  /**
   * ��ʼ����ݲ˵�
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
    popSize.width += popSize.width / 5; // Ϊ�����ۣ��ʵ��ӿ�˵�����ʾ
    this.popMenu.setPopupSize(popSize);
  }

  /**
   * ʵ�ֿ�ݼ��붯���İ�
   */
  private void setInputActionMap() {
    InputMap inputMap = this.getInputMap();
    ActionMap actionMap = this.getActionMap();
    // ����ϼ����ض��ַ����󶨣����δ������Action��ӳ�䣬������θ���ϼ���
    inputMap.put(
        KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK),
        Util.CTRL_Z);
    inputMap.put(
        KeyStroke.getKeyStroke(KeyEvent.VK_Y, KeyEvent.CTRL_DOWN_MASK),
        Util.CTRL_Y);
    inputMap.put(
        KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK),
        Util.CTRL_H);
    // ���ض��ַ�����Action����ӳ��
    actionMap.put(Util.CTRL_Z, this.actUndo);
    actionMap.put(Util.CTRL_Y, this.actRedo);
  }

  /**
   * ���ÿ�ݲ˵��ĳ�ʼ״̬
   */
  private void setMenuDefault() {
    this.itemPopCopy.setEnabled(false);
    this.itemPopCut.setEnabled(false);
    this.itemPopDel.setEnabled(false);
    this.itemPopUnDo.setEnabled(false);
    this.itemPopReDo.setEnabled(false);
  }

  /**
   * Ϊ���ؼ��Ϳ�ݲ˵���Ӹ����¼�������
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
   * Ϊ���˵�������¼��Ĵ�����
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
        String str = tf.getTransferData(DataFlavor.stringFlavor).toString(); // ����������ڵ����ݲ����ı������׳��쳣
        if (str != null) {
          str = str.replaceAll(LineSeparator.WINDOWS.toString(),
              LineSeparator.UNIX.toString()); // ��Windows��ʽ�Ļ��з�\r\n��ת��ΪUNIX/Linux��ʽ
          str = str.replaceAll(LineSeparator.MACINTOSH.toString(),
              LineSeparator.UNIX.toString()); // Ϊ���ݴ������ܲ����\r�ַ��滻Ϊ\n
          this.replaceSelection(str);
        }
      }
    } catch (Exception x) {
      // �������쳣
      // x.printStackTrace();
    }
  }

  /**
   * "ɾ��"�Ĵ�����
   */
  private void deleteText() {
    this.replaceSelection("");
  }

  /**
   * "����"�Ĵ�����
   */
  private void undoAction() {
    if (this.undoManager.canUndo()) { // �ж��Ƿ���Գ���
      this.undoManager.undo(); // ִ�г�������
    }
    this.setMenuStateUndoRedo();
  }

  /**
   * "����"�Ĵ�����
   */
  private void redoAction() {
    if (this.undoManager.canRedo()) { // �ж��Ƿ��������
      this.undoManager.redo(); // ִ����������
    }
    this.setMenuStateUndoRedo();
  }

  /**
   * ���ó����������˵���״̬
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
   * ���ݱ��ؼ���ѡ����ַ����Ƿ�Ϊ�գ�������ز˵���״̬
   * 
   * @param isNull
   *          ѡ���Ƿ�Ϊ��
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
   * �����ؼ��еĹ��仯ʱ�����������¼�
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
   * �����ؼ��е��ı������仯ʱ�����������¼�
   */
  public void undoableEditHappened(UndoableEditEvent e) {
    this.undoManager.addEdit(e.getEdit());
    this.setMenuStateUndoRedo();
  }

  /**
   * �����ü��̽���ʱ����
   */
  public void focusGained(FocusEvent e) {
    try {
      Transferable tf = this.clip.getContents(this);
      if (tf == null) {
        this.itemPopPaste.setEnabled(false);
      } else {
        String str = tf.getTransferData(DataFlavor.stringFlavor).toString(); // ����������ڵ����ݲ����ı������׳��쳣
        if (str != null && str.length() > 0 && this.isEditable()) {
          this.itemPopPaste.setEnabled(true);
        }
      }
    } catch (Exception x) {
      // �������쳣
      // x.printStackTrace();
      this.itemPopPaste.setEnabled(false);
    }
  }

  /**
   * ���ʧȥ���̽���ʱ����
   */
  public void focusLost(FocusEvent e) {
  }

  /**
   * ��갴��������ϵ��������²��ͷţ�ʱ����
   */
  public void mouseClicked(MouseEvent e) {
    this.requestFocus(); // �������ʱ��������Ҽ�������ý���
    if (e.getButton() == MouseEvent.BUTTON3) { // ����Ҽ�ʱ����ʾ��ݲ˵�
      if (this.isEnabled() && this.isFocusable()) {
        this.popMenu.show(this, e.getX(), e.getY());
      }
    }
  }

  /**
   * �����뵽�����ʱ����
   */
  public void mouseEntered(MouseEvent e) {
  }

  /**
   * ����뿪���ʱ����
   */
  public void mouseExited(MouseEvent e) {
  }

  /**
   * ��갴��������ϰ���ʱ����
   */
  public void mousePressed(MouseEvent e) {
  }

  /**
   * ��갴ť��������ͷ�ʱ����
   */
  public void mouseReleased(MouseEvent e) {
  }

}
