package com.xiboliya.snowpad;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

/**
 * 表示当前行的属性类
 * 
 * @author 冰原
 * 
 */
public class CurrentLine {
  private JTextArea txaSource = null; // 当前行所在的文本域
  private int lineNum = -1; // 当前行号
  private int startIndex = -1; // 当前行首的偏移量
  private int endIndex = -1; // 当前行尾的偏移量
  private int currentIndex = -1; // 当前光标的偏移量
  private String strLine = null; // 当前行的文本

  /**
   * 构造方法
   * 
   * @param txaSource
   *          当前的文本域
   */
  public CurrentLine(JTextArea txaSource) {
    this.txaSource = txaSource;
    this.init();
  }

  /**
   * 初始化各个属性值
   */
  private void init() {
    if (this.txaSource == null) {
      return;
    }
    try {
      // 获取当前光标的偏移量
      this.currentIndex = this.txaSource.getCaretPosition();
      // 获取指定偏移量处的行号，返回行号的取值范围：x>=0 && x<文本域总行数
      this.lineNum = this.txaSource.getLineOfOffset(this.currentIndex);
      // 获取指定行起始处的偏移量，指定行号的取值范围：x>=0 && x<文本域总行数
      this.startIndex = this.txaSource.getLineStartOffset(this.lineNum);
      // 获取指定行结尾处的偏移量，指定行号的取值范围：x>=0 && x<文本域总行数
      this.endIndex = this.txaSource.getLineEndOffset(this.lineNum);
      // 获取当前行的文本
      this.strLine = this.txaSource.getText().substring(this.startIndex,
          this.endIndex);
    } catch (BadLocationException x) {
      x.printStackTrace();
    }
  }

  /**
   * 获取当前行首的偏移量
   * 
   * @return 偏移量
   */
  public int getStartIndex() {
    return this.startIndex;
  }

  /**
   * 获取当前行尾的偏移量
   * 
   * @return 偏移量
   */
  public int getEndIndex() {
    return this.endIndex;
  }

  /**
   * 获取当前光标的偏移量
   * 
   * @return 偏移量
   */
  public int getCurrentIndex() {
    return this.currentIndex;
  }

  /**
   * 获取当前行号
   * 
   * @return 当前行号
   */
  public int getLineNum() {
    return this.lineNum;
  }

  /**
   * 获取当前行的文本
   * 
   * @return 文本
   */
  public String getStrLine() {
    return this.strLine;
  }
}
