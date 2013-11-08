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

package com.xiboliya.snowpad;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

/**
 * 表示当前多行文本的属性类
 * 
 * @author 冰原
 * 
 */
public class CurrentLines {
  private JTextArea txaSource = null; // 当前多行所在的文本域
  private int currentIndex = -1; // 当前光标的偏移量
  private int startLineNum = -1; // 当前首行的行号
  private int endLineNum = -1; // 当前末行的行号
  private int lineCount = 0; // 当前所占的行数
  private int startIndex = -1; // 当前首行的行首偏移量
  private int endIndex = -1; // 当前末行的行尾偏移量
  private int startIndexCurrent = -1; // 当前实际选区内首行的行首偏移量
  private int endIndexCurrent = -1; // 当前实际选区内末行的行尾偏移量
  private String strContent = null; // 当前多行的文本
  private String strContentCurrent = null; // 当前实际选区内多行的文本
  private String strContentExtend = null; // 扩展行的文本
  private LineExtend lineExtend = LineExtend.EXTEND_WITHOUT; // 是否扩展上下行

  /**
   * 标识扩展上下行方式的枚举
   * 
   * @author 冰原
   * 
   */
  public static enum LineExtend {
    /**
     * 不进行扩展
     */
    EXTEND_WITHOUT,
    /**
     * 向上扩展
     */
    EXTEND_UP,
    /**
     * 向下扩展
     */
    EXTEND_DOWN;
  }

  /**
   * 构造方法
   * 
   * @param txaSource
   *          当前的文本域
   */
  public CurrentLines(JTextArea txaSource) {
    this(txaSource, LineExtend.EXTEND_WITHOUT);
  }

  /**
   * 构造方法
   * 
   * @param txaSource
   *          当前的文本域
   * @param lineExtend
   *          是否扩展上下行，如果为true则在当前多行的前后各扩展一行，即扩大多行的范围；如果为false则不扩展
   */
  public CurrentLines(JTextArea txaSource, LineExtend lineExtend) {
    this.txaSource = txaSource;
    this.lineExtend = lineExtend;
    this.init();
  }

  /**
   * 初始化各个属性值。 其中所有行号的取值范围均为：x>=0 && x<文本域总行数
   */
  private void init() {
    if (this.txaSource == null) {
      return;
    }
    try {
      // 获取当前光标的偏移量
      this.currentIndex = this.txaSource.getCaretPosition();
      // 获取选区起始处所在的行号
      this.startLineNum = this.txaSource.getLineOfOffset(this.txaSource
          .getSelectionStart());
      // 获取选区结尾处所在的行号
      this.endLineNum = this.txaSource.getLineOfOffset(this.txaSource
          .getSelectionEnd());
      // 获取实际选区起始处所在行的行首偏移量
      this.startIndexCurrent = this.txaSource
          .getLineStartOffset(this.startLineNum);
      // 获取实际选区结尾处所在行的行末偏移量
      this.endIndexCurrent = this.txaSource.getLineEndOffset(this.endLineNum);
      boolean isExtend = false; // 是否成功扩展了上下行
      switch (this.lineExtend) {
      case EXTEND_UP:
        if (this.startLineNum > 0) {
          this.startLineNum--; // 将当前选区向上扩展一行
          isExtend = true;
        }
        break;
      case EXTEND_DOWN:
        int lineCounts = this.txaSource.getLineCount(); // 文本域总行数
        if (this.endLineNum < lineCounts - 1) {
          this.endLineNum++; // 将当前选区向下扩展一行
          isExtend = true;
        }
        break;
      case EXTEND_WITHOUT:
        break;
      }
      // 获取选区起始处所在行的行首偏移量
      this.startIndex = this.txaSource.getLineStartOffset(this.startLineNum);
      // 获取选区结尾处所在行的行末偏移量
      this.endIndex = this.txaSource.getLineEndOffset(this.endLineNum);
      // 获取当前选区所占的行数
      this.lineCount = this.endLineNum - this.startLineNum + 1;
      // 获取当前多行的文本
      this.strContent = this.txaSource.getText().substring(this.startIndex,
          this.endIndex);
      // 获取当前实际选区内多行的文本
      this.strContentCurrent = this.txaSource.getText().substring(
          this.startIndexCurrent, this.endIndexCurrent);
      // 获取扩展行的文本
      if (isExtend) {
        if (this.startIndex < this.startIndexCurrent) {
          this.strContentExtend = this.txaSource.getText().substring(
              this.startIndex, this.startIndexCurrent);
        } else {
          this.strContentExtend = this.txaSource.getText().substring(
              this.endIndexCurrent, this.endIndex);
        }
      }
    } catch (BadLocationException x) {
      x.printStackTrace();
    }
  }

  /**
   * 获取扩展上下行的方式
   * 
   * @return 扩展上下行的方式
   */
  public LineExtend getLineExtend() {
    return this.lineExtend;
  }

  /**
   * 获取当前首行的行首偏移量
   * 
   * @return 偏移量
   */
  public int getStartIndex() {
    return this.startIndex;
  }

  /**
   * 获取实际选区内当前首行的行首偏移量
   * 
   * @return 偏移量
   */
  public int getStartIndexCurrent() {
    return this.startIndexCurrent;
  }

  /**
   * 获取当前末行的行尾偏移量
   * 
   * @return 偏移量
   */
  public int getEndIndex() {
    return this.endIndex;
  }

  /**
   * 获取实际选区内当前末行的行尾偏移量
   * 
   * @return 偏移量
   */
  public int getEndIndexCurrent() {
    return this.endIndexCurrent;
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
   * 获取当前首行的行号
   * 
   * @return 当前首行的行号
   */
  public int getStartLineNum() {
    return this.startLineNum;
  }

  /**
   * 获取当前末行的行号
   * 
   * @return 当前末行的行号
   */
  public int getEndLineNum() {
    return this.endLineNum;
  }

  /**
   * 获取当前所占的行数
   * 
   * @return 当前所占的行数
   */
  public int getLineCount() {
    return this.lineCount;
  }

  /**
   * 获取当前多行的文本
   * 
   * @return 当前文本
   */
  public String getStrContent() {
    return this.strContent;
  }

  /**
   * 获取当前实际选区内多行的文本
   * 
   * @return 当前文本
   */
  public String getStrContentCurrent() {
    return this.strContentCurrent;
  }

  /**
   * 获取当前扩展行的文本
   * 
   * @return 当前文本
   */
  public String getStrContentExtend() {
    return this.strContentExtend;
  }

}
