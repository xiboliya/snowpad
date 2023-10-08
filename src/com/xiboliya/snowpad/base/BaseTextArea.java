/**
 * Copyright (C) 2013 冰原
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

package com.xiboliya.snowpad.base;

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

import com.xiboliya.snowpad.common.CharEncoding;
import com.xiboliya.snowpad.common.CurrentLines;
import com.xiboliya.snowpad.common.FileExt;
import com.xiboliya.snowpad.common.LineSeparator;
import com.xiboliya.snowpad.common.PartnerBean;
import com.xiboliya.snowpad.setting.Setting;
import com.xiboliya.snowpad.util.Util;

/**
 * 实现撤销管理器的JTextArea控件
 * 
 * @author 冰原
 * 
 */
public class BaseTextArea extends JTextArea {
  private static final long serialVersionUID = 1L;
  private int newFileIndex = 0; // 新建文件的序号
  private File file = null; // 编辑的文件
  private FileExt fileExt = FileExt.TXT; // 文件扩展名类型
  private String title = Util.NEW_FILE_NAME; // 在标签中显示的标题
  private LinkedList<PartnerBean> highlighterList = new LinkedList<PartnerBean>(); // 存放文本域中所有高亮对象的链表
  private LineSeparator lineSeparator = LineSeparator.DEFAULT; // 换行符格式
  private CharEncoding charEncoding = CharEncoding.GB18030; // 字符编码格式
  private KeyAdapter autoIndentKeyAdapter = null; // 用于自动缩进的键盘适配器
  private KeyAdapter tabReplaceKeyAdapter = null; // 用于设置以空格代替Tab键的键盘适配器
  private KeyAdapter autoCompleteKeyAdapter = null; // 用于设置自动完成的键盘适配器
  private Color[] colorStyle = null; // 配色方案
  private boolean isSaved = false; // 文件是否已保存，如果已保存则为true
  private boolean isTextChanged = false; // 文本内容是否已修改，如果已修改则为true
  private boolean isStyleChanged = false; // 文本格式是否已修改，如果已修改则为true
  private boolean fileExistsLabel = false; // 当文件删除或移动后，用于标识是否已弹出过提示框
  private boolean fileChangedLabel = false; // 当文件被其他程序修改后，用于标识是否已弹出过提示框
  private boolean tabReplaceBySpace = false; // 是否以空格代替Tab键
  private boolean autoIndent = false; // 是否可自动缩进
  private boolean isLineNumberView = false; // 是否显示行号栏
  private boolean autoComplete = false; // 是否自动完成
  private boolean isFrozen = false; // 是否被冻结
  private boolean isBinary = false; // 是否为二进制文件
  private BaseDocument document = new BaseDocument(); // 文本模型
  private UndoManager undoManager = new UndoManager(); // 撤销管理器
  private int undoIndex = Util.DEFAULT_UNDO_INDEX; // 撤销标识符，初始化为默认值，此值若改变表示文本已修改
  private Color bracketBackColor = Util.COLOR_BRACKET; // 需绘制的匹配括号的背景颜色
  private Color lineBackColor = Util.COLOR_CURRENT_LINE; // 需绘制的当前行的背景颜色
  private Color wordBackColor = Util.COLOR_CURRENT_LINE; // 需绘制的匹配文本的背景颜色
  private LinkedList<PartnerBean> backForwardList = new LinkedList<PartnerBean>(); // 存放光标在文本域中历史位置的链表
  private int backForwardIndex = Util.DEFAULT_BACK_FORWARD_INDEX; // 光标历史位置，初始化为默认值
  private long fileLastModified = 0L; // 文件最后修改的时间戳，如果文件为空，则此值为0L
  private LinkedList<Integer> bookmarks = new LinkedList<Integer>(); // 书签列表，即为被记录的行号列表

  /**
   * 默认的构造方法
   */
  public BaseTextArea() {
    super();
    this.init();
  }

  public BaseTextArea(Setting setting) {
    this();
    this.loadSetting(setting);
  }

  private void init() {
    this.undoManager.setLimit(-1); // 设置此撤销管理器保持的最大编辑数，小于0的值表示编辑数不受限制，默认值为100。
    this.disableShortcut();
    // 初始化用于自动缩进的键盘适配器
    this.autoIndentKeyAdapter = new KeyAdapter() {
      // 为了解决某些功能，比如：通过按回车键转到某行时，会进行自动缩进的问题。改为监听keyTyped（按下和弹起按键）事件。
      public void keyTyped(KeyEvent e) {
        // keyTyped事件中如果用getKeyCode()方式，获取到的总是0，所以要使用getKeyChar()方式判断。
        if (e.getKeyChar() == '\n') {
          toAutoIndent();
        }
      }
    };
    // 初始化用于设置以空格代替Tab键的键盘适配器
    this.tabReplaceKeyAdapter = new KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_TAB) {
          toTabReplace();
        }
      }
    };
    // 初始化用于设置自动完成的键盘适配器
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

  private void loadSetting(Setting setting) {
    if (setting == null) {
      return;
    }
    this.setLineWrap(setting.isLineWrap);
    this.setWrapStyleWord(setting.isWrapStyleWord);
    this.setLineSeparator(setting.defaultLineSeparator);
    this.setCharEncoding(setting.defaultCharEncoding);
    this.setFont(setting.font);
    this.setAutoIndent(setting.autoIndent);
    this.setTabReplaceBySpace(setting.tabReplaceBySpace);
    this.setColorStyle(setting.colorStyle);
    this.setTabSize(setting.tabSize);
    this.setAutoComplete(setting.autoComplete);
    this.setLineNumberView(setting.viewLineNumber);
  }

  private void disableShortcut() {
    // 屏蔽JTextArea组件的默认热键：Ctrl+C、Ctrl+H、Ctrl+V、Ctrl+X
    InputMap inputMap = this.getInputMap();
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK), "CTRL_C");
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, KeyEvent.CTRL_DOWN_MASK), "CTRL_H");
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK), "CTRL_V");
    inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK), "CTRL_X");
  }

  /**
   * 按回车键时进行缩进
   */
  private void toAutoIndent() {
    CurrentLines currentLines = new CurrentLines(this,
        CurrentLines.LineExtend.EXTEND_UP);
    String strContentExtend = currentLines.getStrContentExtend();
    if (strContentExtend == null) {
      return;
    }
    int currentIndex = currentLines.getCurrentIndex();
    if (this.getText().charAt(currentIndex - 1) != '\n') { // 如果不是换行操作，将不作处理
      return;
    }
    String strSpace = "";
    for (int i = 0; i < strContentExtend.length(); i++) {
      switch (strContentExtend.charAt(i)) {
      case ' ':
      case '\t':
      case '　':
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
   * 按Tab键时将其替换为等量的空格
   */
  private void toTabReplace() {
    int currentIndex = this.getCaretPosition();
    if (this.getText().charAt(currentIndex - 1) != '\t') { // 文本域中输入的前一个字符是否为Tab
      return;
    }
    int tabSize = this.getTabSize();
    StringBuilder stbSpace = new StringBuilder();
    for (int i = 0; i < tabSize; i++) {
      stbSpace.append(" ");
    }
    this.replaceRange(stbSpace.toString(), currentIndex - 1, currentIndex);
  }

  /**
   * 自动完成
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
   * 获取因文本或格式修改产生的标题栏前缀字符"*"和"※"
   * 
   * @return 标题栏前缀字符，可能为：""、"*"、"※"、"*※"
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
   * 绘制组件
   */
  @Override
  protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
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
   * 判断当前是否已注册监听器
   * 
   * @param keyListener 键盘监听器
   * @return 是否已注册过监听器，已注册返回true
   */
  public boolean hasKeyListener(KeyListener keyListener) {
    KeyListener[] keyListeners = this.getKeyListeners();
    return Arrays.asList(keyListeners).contains(keyListener);
  }

  public void setFile(File file) {
    if (file != null) {
      try {
        this.file = file.getCanonicalFile(); // 获取此抽象路径名的规范形式
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
    this.setWordBackColor(colorStyle[7]);
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

  public void setBinary(boolean isBinary) {
    this.isBinary = isBinary;
  }

  public boolean getBinary() {
    return this.isBinary;
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

  public Color getWordBackColor() {
    return this.wordBackColor;
  }

  public void setWordBackColor(Color wordBackColor) {
    this.wordBackColor = wordBackColor;
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

  public LinkedList<Integer> getBookmarks() {
    return this.bookmarks;
  }

  public void switchBookmark(int bookmark) {
    int size = this.bookmarks.size();
    if (size == 0) {
      this.bookmarks.addFirst(bookmark);
      return;
    }
    // 如果书签已存在，则删除
    for (int i = 0; i < size; i++) {
      int mark = this.bookmarks.get(i);
      if (bookmark == mark) {
        this.bookmarks.remove(i);
        return;
      }
    }
    // 如果书签不存在，则添加
    for (int i = size - 1; i >= 0; i--) {
      int mark = this.bookmarks.get(i);
      if (bookmark > mark) {
        this.bookmarks.add(i + 1, bookmark);
        return;
      }
    }
    this.bookmarks.addFirst(bookmark);
  }
}
