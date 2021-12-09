/**
 * Copyright (C) 2013 ��ԭ
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

import javax.swing.InputMap;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.undo.UndoManager;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * ʵ�ֳ�����������JTextArea�ؼ�
 * 
 * @author ��ԭ
 * 
 */
public class BaseTextArea extends JTextArea {
  private static final long serialVersionUID = 1L;
  private int newFileIndex = 0; // �½��ļ������
  private File file = null; // �༭���ļ�
  private FileExt fileExt = FileExt.TXT; // �ļ���չ������
  private String title = Util.NEW_FILE_NAME; // �ڱ�ǩ����ʾ�ı���
  private LinkedList<PartnerBean> highlighterList = new LinkedList<PartnerBean>(); // ����ı��������и������������
  private LineSeparator lineSeparator = LineSeparator.DEFAULT; // ���з���ʽ
  private CharEncoding charEncoding = CharEncoding.BASE; // �ַ������ʽ
  private KeyAdapter autoIndentKeyAdapter = null; // �����Զ������ļ���������
  private KeyAdapter tabReplaceKeyAdapter = null; // ���������Կո����Tab���ļ���������
  private KeyAdapter autoCompleteKeyAdapter = null; // ���������Զ���ɵļ���������
  private Color[] colorStyle = null; // ��ɫ����
  private boolean isSaved = false; // �ļ��Ƿ��ѱ��棬����ѱ�����Ϊtrue
  private boolean isTextChanged = false; // �ı������Ƿ����޸ģ�������޸���Ϊtrue
  private boolean isStyleChanged = false; // �ı���ʽ�Ƿ����޸ģ�������޸���Ϊtrue
  private boolean fileExistsLabel = false; // ���ļ�ɾ�����ƶ������ڱ�ʶ�Ƿ��ѵ�������ʾ��
  private boolean fileChangedLabel = false; // ���ļ������������޸ĺ����ڱ�ʶ�Ƿ��ѵ�������ʾ��
  private boolean tabReplaceBySpace = false; // �Ƿ��Կո����Tab��
  private boolean autoIndent = false; // �Ƿ���Զ�����
  private boolean isLineNumberView = false; // �Ƿ���ʾ�к���
  private boolean autoComplete = false; // �Ƿ��Զ����
  private boolean isFrozen = false; // �Ƿ񱻶���
  private BaseDocument document = new BaseDocument(); // �ı�ģ��
  private UndoManager undoManager = new UndoManager(); // ����������
  private int undoIndex = Util.DEFAULT_UNDO_INDEX; // ������ʶ������ʼ��ΪĬ��ֵ����ֵ���ı��ʾ�ı����޸�
  private Color bracketBackColor = Util.COLOR_BRACKET; // ����Ƶ�ƥ�����ŵı�����ɫ
  private Color lineBackColor = Util.COLOR_CURRENT_LINE; // ����Ƶĵ�ǰ�еı�����ɫ
  private LinkedList<PartnerBean> backForwardList = new LinkedList<PartnerBean>(); // ��Ź�����ı�������ʷλ�õ�����
  private int backForwardIndex = Util.DEFAULT_BACK_FORWARD_INDEX; // �����ʷλ�ã���ʼ��ΪĬ��ֵ
  private long fileLastModified = 0L; // �ļ�����޸ĵ�ʱ���������ļ�Ϊ�գ����ֵΪ0L

  /**
   * Ĭ�ϵĹ��췽��
   */
  public BaseTextArea() {
    super();
    this.init();
  }

  public BaseTextArea(TextAreaSetting textAreaSetting) {
    this();
    this.loadSetting(textAreaSetting);
  }

  private void init() {
    this.undoManager.setLimit(-1); // ���ô˳������������ֵ����༭����С��0��ֵ��ʾ�༭���������ƣ�Ĭ��ֵΪ100��
    this.disableShortcut();
    // ��ʼ�������Զ������ļ���������
    this.autoIndentKeyAdapter = new KeyAdapter() {
      // Ϊ�˽��ĳЩ���ܣ����磺ͨ�����س���ת��ĳ��ʱ��������Զ����������⡣��Ϊ����keyTyped�����º͵��𰴼����¼���
      public void keyTyped(KeyEvent e) {
        // keyTyped�¼��������getKeyCode()��ʽ����ȡ��������0������Ҫʹ��getKeyChar()��ʽ�жϡ�
        if (e.getKeyChar() == '\n') {
          toAutoIndent();
        }
      }
    };
    // ��ʼ�����������Կո����Tab���ļ���������
    this.tabReplaceKeyAdapter = new KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_TAB) {
          toTabReplace();
        }
      }
    };
    // ��ʼ�����������Զ���ɵļ���������
    this.autoCompleteKeyAdapter = new KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
          case KeyEvent.VK_9:
          case KeyEvent.VK_OPEN_BRACKET:
          case KeyEvent.VK_COMMA:
          case KeyEvent.VK_QUOTE:
            toAutoComplete();
            break;
        }
      }
    };
    this.setDocument(this.document);
  }

  private void loadSetting(TextAreaSetting textAreaSetting) {
    if (textAreaSetting == null) {
      return;
    }
    this.setLineWrap(textAreaSetting.isLineWrap);
    this.setWrapStyleWord(textAreaSetting.isWrapStyleWord);
    this.setLineSeparator(textAreaSetting.lineSeparator);
    this.setCharEncoding(textAreaSetting.charEncoding);
    this.setFont(textAreaSetting.font);
    this.setAutoIndent(textAreaSetting.autoIndent);
    this.setTabReplaceBySpace(textAreaSetting.tabReplaceBySpace);
    this.setColorStyle(textAreaSetting.colorStyle);
    this.setTabSize(textAreaSetting.tabSize);
    this.setAutoComplete(textAreaSetting.autoComplete);
    this.setSaved(textAreaSetting.isSaved);
    this.setTextChanged(textAreaSetting.isTextChanged);
    this.setStyleChanged(textAreaSetting.isStyleChanged);
    this.setFileExistsLabel(textAreaSetting.fileExistsLabel);
    this.setFileChangedLabel(textAreaSetting.fileChangedLabel);
    this.setLineNumberView(textAreaSetting.isLineNumberView);
  }

  private void disableShortcut() {
    // ����JTextArea�����Ĭ���ȼ���Ctrl+C��Ctrl+H��Ctrl+V��Ctrl+X
    InputMap inputMap = this.getInputMap();
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), "CTRL_C");
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK), "CTRL_H");
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK), "CTRL_V");
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK), "CTRL_X");
  }

  /**
   * ���س���ʱ��������
   */
  private void toAutoIndent() {
    CurrentLines currentLines = new CurrentLines(this,
        CurrentLines.LineExtend.EXTEND_UP);
    String strContentExtend = currentLines.getStrContentExtend();
    if (strContentExtend == null) {
      return;
    }
    int currentIndex = currentLines.getCurrentIndex();
    if (this.getText().charAt(currentIndex - 1) != '\n') { // ������ǻ��в���������������
      return;
    }
    String strSpace = "";
    for (int i = 0; i < strContentExtend.length(); i++) {
      switch (strContentExtend.charAt(i)) {
      case ' ':
      case '\t':
      case '��':
        strSpace += strContentExtend.charAt(i);
        break;
      default:
        break;
      }
      if (strSpace.length() == i) {
        break;
      }
    }
    if (!Util.isTextEmpty(strSpace)) {
      this.insert(strSpace, this.getCaretPosition());
    }
  }

  /**
   * ��Tab��ʱ�����滻Ϊ�����Ŀո�
   */
  private void toTabReplace() {
    int currentIndex = this.getCaretPosition();
    if (this.getText().charAt(currentIndex - 1) != '\t') { // �ı����������ǰһ���ַ��Ƿ�ΪTab
      return;
    }
    int tabSize = this.getTabSize();
    String strSpace = "";
    for (int i = 0; i < tabSize; i++) {
      strSpace += " ";
    }
    this.replaceRange(strSpace, currentIndex - 1, currentIndex);
  }

  /**
   * �Զ����
   */
  private void toAutoComplete() {
    char currentChar = this.getText().charAt(this.getCaretPosition() - 1);
    for (int i = 0; i < Util.AUTO_COMPLETE_BRACKETS_LEFT.length(); i++) {
      char ch = Util.AUTO_COMPLETE_BRACKETS_LEFT.charAt(i);
      if (currentChar == ch) {
        this.insert(String.valueOf(Util.AUTO_COMPLETE_BRACKETS_RIGHT.charAt(i)), this.getCaretPosition());
        this.setCaretPosition(this.getCaretPosition() - 1);
        break;
      }
    }
  }

  /**
   * ��ȡ���ı����ʽ�޸Ĳ����ı�����ǰ׺�ַ�"*"��"��"
   * 
   * @return ������ǰ׺�ַ�������Ϊ��""��"*"��"��"��"*��"
   */
  public String getPrefix() {
    String prefix = "";
    if (this.getTextChanged() && this.getStyleChanged()) {
      prefix = Util.TEXT_PREFIX + Util.STYLE_PREFIX;
    } else if (this.getTextChanged()) {
      prefix = Util.TEXT_PREFIX;
    } else if (this.getStyleChanged()) {
      prefix = Util.STYLE_PREFIX;
    }
    return prefix;
  }

  /**
   * �������
   */
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics graphics = g.create(); // ʹ��Graphics�ĸ������л���
    graphics.setColor(this.lineBackColor);
    try {
      Rectangle rectangle = this.modelToView(this.getCaretPosition());
      if (rectangle != null) {
        double x = rectangle.getX();
        double y = rectangle.getY();
        graphics.fillRect(0, (int)y, this.getWidth(), this.getFont().getSize());
      }
    } catch (Exception x) {
      // x.printStackTrace();
    } finally {
      graphics.dispose();
    }
  }

  /**
   * �жϵ�ǰ�Ƿ���ע�������
   * 
   * @param keyListener
   *          ���̼�����
   * @return �Ƿ���ע�������������ע�᷵��true
   */
  public boolean hasKeyListener(KeyListener keyListener) {
    KeyListener[] keyListeners = this.getKeyListeners();
    return Arrays.asList(keyListeners).contains(keyListener);
  }

  public void setFile(File file) {
    if (file != null) {
      try {
        this.file = file.getCanonicalFile(); // ��ȡ�˳���·�����Ĺ淶��ʽ
        this.setTitle(this.file.getName());
      } catch (IOException x) {
        // x.printStackTrace();
      }
    } else {
      this.file = file;
    }
  }

  public File getFile() {
    return this.file;
  }

  public String getFileName() {
    if (this.file != null) {
      return file.getAbsolutePath();
    }
    return null;
  }

  public long getFileLastModified() {
    return this.fileLastModified;
  }

  public void setFileLastModified(long fileLastModified) {
    this.fileLastModified = fileLastModified;
  }

  public void setTitle(String title) {
    if (title != null) {
      this.title = title;
    }
  }

  public String getTitle() {
    return this.title;
  }

  public FileExt getFileExt() {
    return this.fileExt;
  }

  public void setFileExt(FileExt fileExt) {
    this.fileExt = fileExt;
  }

  public LinkedList<PartnerBean> getHighlighterList() {
    return this.highlighterList;
  }

  public void setLineSeparator(LineSeparator lineSeparator) {
    this.lineSeparator = lineSeparator;
  }

  public LineSeparator getLineSeparator() {
    return this.lineSeparator;
  }

  public void setCharEncoding(CharEncoding charEncoding) {
    this.charEncoding = charEncoding;
  }

  public CharEncoding getCharEncoding() {
    return this.charEncoding;
  }

  public void setSaved(boolean isSaved) {
    this.isSaved = isSaved;
  }

  public boolean getSaved() {
    return this.isSaved;
  }

  public void setTextChanged(boolean isTextChanged) {
    this.isTextChanged = isTextChanged;
  }

  public boolean getTextChanged() {
    return this.isTextChanged;
  }

  public void setStyleChanged(boolean isStyleChanged) {
    this.isStyleChanged = isStyleChanged;
  }

  public boolean getStyleChanged() {
    return this.isStyleChanged;
  }

  public void setFileExistsLabel(boolean fileExistsLabel) {
    this.fileExistsLabel = fileExistsLabel;
  }

  public boolean getFileExistsLabel() {
    return this.fileExistsLabel;
  }

  public void setFileChangedLabel(boolean fileChangedLabel) {
    this.fileChangedLabel = fileChangedLabel;
  }

  public boolean getFileChangedLabel() {
    return this.fileChangedLabel;
  }

  public void setColorStyle(Color[] colorStyle) {
    if (colorStyle == null) {
      return;
    }
    this.colorStyle = colorStyle;
    this.setForeground(colorStyle[0]);
    this.setBackground(colorStyle[1]);
    this.setCaretColor(colorStyle[2]);
    this.setSelectedTextColor(colorStyle[3]);
    this.setSelectionColor(colorStyle[4]);
    this.setBracketBackColor(colorStyle[5]);
    this.setLineBackColor(colorStyle[6]);
  }

  public Color[] getColorStyle() {
    return this.colorStyle;
  }

  public void setAutoIndent(boolean enable) {
    if (enable) {
      if (!this.hasKeyListener(this.autoIndentKeyAdapter)) {
        this.addKeyListener(this.autoIndentKeyAdapter);
      }
    } else {
      this.removeKeyListener(this.autoIndentKeyAdapter);
    }
    this.autoIndent = enable;
  }

  public boolean getAutoIndent() {
    return this.autoIndent;
  }

  public void setTabReplaceBySpace(boolean enable) {
    if (enable) {
      if (!this.hasKeyListener(this.tabReplaceKeyAdapter)) {
        this.addKeyListener(this.tabReplaceKeyAdapter);
      }
    } else {
      this.removeKeyListener(this.tabReplaceKeyAdapter);
    }
    this.tabReplaceBySpace = enable;
  }

  public boolean getTabReplaceBySpace() {
    return this.tabReplaceBySpace;
  }

  public void setAutoComplete(boolean enable) {
    if (enable) {
      if (!this.hasKeyListener(this.autoCompleteKeyAdapter)) {
        this.addKeyListener(this.autoCompleteKeyAdapter);
      }
    } else {
      this.removeKeyListener(this.autoCompleteKeyAdapter);
    }
    this.autoComplete = enable;
  }

  public boolean getAutoComplete() {
    return this.autoComplete;
  }

  public void setFrozen(boolean isFrozen) {
    this.isFrozen = isFrozen;
    this.document.setFrozen(this.isFrozen);
    this.setEditable(!this.isFrozen);
  }

  public boolean getFrozen() {
    return this.isFrozen;
  }

  public UndoManager getUndoManager() {
    return this.undoManager;
  }

  public int getUndoIndex() {
    return this.undoIndex;
  }

  public void setUndoIndex(int undoIndex) {
    this.undoIndex = undoIndex;
  }

  public void setNewFileIndex(int newFileIndex) {
    this.newFileIndex = newFileIndex;
  }

  public int getNewFileIndex() {
    return this.newFileIndex;
  }

  public boolean getLineNumberView() {
    return this.isLineNumberView;
  }

  public void setLineNumberView(boolean isLineNumberView) {
    this.isLineNumberView = isLineNumberView;
  }

  public Color getBracketBackColor() {
    return this.bracketBackColor;
  }

  public void setBracketBackColor(Color bracketBackColor) {
    this.bracketBackColor = bracketBackColor;
  }

  public Color getLineBackColor() {
    return this.lineBackColor;
  }

  public void setLineBackColor(Color lineBackColor) {
    this.lineBackColor = lineBackColor;
  }

  public LinkedList<PartnerBean> getBackForwardList() {
    return this.backForwardList;
  }

  public int getBackForwardIndex() {
    return this.backForwardIndex;
  }

  public void setBackForwardIndex(int backForwardIndex) {
    this.backForwardIndex = backForwardIndex;
  }
}
