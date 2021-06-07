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

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

/**
 * ��ʾ��ǰ�����ı���������
 * 
 * @author ��ԭ
 * 
 */
public class CurrentLines {
  private JTextArea txaSource = null; // ��ǰ�������ڵ��ı���
  private int currentIndex = -1; // ��ǰ����ƫ����
  private int startLineNum = -1; // ��ǰ���е��к�
  private int endLineNum = -1; // ��ǰĩ�е��к�
  private int lineCount = 0; // ��ǰ��ռ������
  private int startIndex = -1; // ��ǰ���е�����ƫ����
  private int endIndex = -1; // ��ǰĩ�е���βƫ����
  private int startIndexCurrent = -1; // ��ǰʵ��ѡ�������е�����ƫ����
  private int endIndexCurrent = -1; // ��ǰʵ��ѡ����ĩ�е���βƫ����
  private String strContent = null; // ��ǰ���е��ı�
  private String strContentCurrent = null; // ��ǰʵ��ѡ���ڶ��е��ı�
  private String strContentExtend = null; // ��չ�е��ı�
  private LineExtend lineExtend = LineExtend.EXTEND_WITHOUT; // �Ƿ���չ������

  /**
   * ��ʶ��չ�����з�ʽ��ö��
   * 
   * @author ��ԭ
   * 
   */
  public static enum LineExtend {
    /**
     * ��������չ
     */
    EXTEND_WITHOUT,
    /**
     * ������չ
     */
    EXTEND_UP,
    /**
     * ������չ
     */
    EXTEND_DOWN;
  }

  /**
   * ���췽��
   * 
   * @param txaSource
   *          ��ǰ���ı���
   */
  public CurrentLines(JTextArea txaSource) {
    this(txaSource, LineExtend.EXTEND_WITHOUT);
  }

  /**
   * ���췽��
   * 
   * @param txaSource
   *          ��ǰ���ı���
   * @param lineExtend
   *          �Ƿ���չ�����У����Ϊtrue���ڵ�ǰ���е�ǰ�����չһ�У���������еķ�Χ�����Ϊfalse����չ
   */
  public CurrentLines(JTextArea txaSource, LineExtend lineExtend) {
    this.txaSource = txaSource;
    this.lineExtend = lineExtend;
    this.init();
  }

  /**
   * ��ʼ����������ֵ�����������кŵ�ȡֵ��Χ��Ϊ��x>=0 && x<�ı���������
   */
  private void init() {
    if (this.txaSource == null) {
      return;
    }
    try {
      // ��ȡ��ǰ����ƫ����
      this.currentIndex = this.txaSource.getCaretPosition();
      // ��ȡѡ����ʼ�����ڵ��к�
      this.startLineNum = this.txaSource.getLineOfOffset(this.txaSource
          .getSelectionStart());
      // ��ȡѡ����β�����ڵ��к�
      this.endLineNum = this.txaSource.getLineOfOffset(this.txaSource
          .getSelectionEnd());
      // ��ȡʵ��ѡ����ʼ�������е�����ƫ����
      this.startIndexCurrent = this.txaSource
          .getLineStartOffset(this.startLineNum);
      // ��ȡʵ��ѡ����β�������е���ĩƫ����
      this.endIndexCurrent = this.txaSource.getLineEndOffset(this.endLineNum);
      boolean isExtend = false; // �Ƿ�ɹ���չ��������
      switch (this.lineExtend) {
      case EXTEND_UP:
        if (this.startLineNum > 0) {
          this.startLineNum--; // ����ǰѡ��������չһ��
          isExtend = true;
        }
        break;
      case EXTEND_DOWN:
        int lineCounts = this.txaSource.getLineCount(); // �ı���������
        if (this.endLineNum < lineCounts - 1) {
          this.endLineNum++; // ����ǰѡ��������չһ��
          isExtend = true;
        }
        break;
      case EXTEND_WITHOUT:
        break;
      }
      // ��ȡѡ����ʼ�������е�����ƫ����
      this.startIndex = this.txaSource.getLineStartOffset(this.startLineNum);
      // ��ȡѡ����β�������е���ĩƫ����
      this.endIndex = this.txaSource.getLineEndOffset(this.endLineNum);
      // ��ȡ��ǰѡ����ռ������
      this.lineCount = this.endLineNum - this.startLineNum + 1;
      // ��ȡ��ǰ���е��ı�
      this.strContent = this.txaSource.getText().substring(this.startIndex,
          this.endIndex);
      // ��ȡ��ǰʵ��ѡ���ڶ��е��ı�
      this.strContentCurrent = this.txaSource.getText().substring(
          this.startIndexCurrent, this.endIndexCurrent);
      // ��ȡ��չ�е��ı�
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
      // x.printStackTrace();
    }
  }

  /**
   * ��ȡ��չ�����еķ�ʽ
   * 
   * @return ��չ�����еķ�ʽ
   */
  public LineExtend getLineExtend() {
    return this.lineExtend;
  }

  /**
   * ��ȡ��ǰ���е�����ƫ����
   * 
   * @return ƫ����
   */
  public int getStartIndex() {
    return this.startIndex;
  }

  /**
   * ��ȡʵ��ѡ���ڵ�ǰ���е�����ƫ����
   * 
   * @return ƫ����
   */
  public int getStartIndexCurrent() {
    return this.startIndexCurrent;
  }

  /**
   * ��ȡ��ǰĩ�е���βƫ����
   * 
   * @return ƫ����
   */
  public int getEndIndex() {
    return this.endIndex;
  }

  /**
   * ��ȡʵ��ѡ���ڵ�ǰĩ�е���βƫ����
   * 
   * @return ƫ����
   */
  public int getEndIndexCurrent() {
    return this.endIndexCurrent;
  }

  /**
   * ��ȡ��ǰ����ƫ����
   * 
   * @return ƫ����
   */
  public int getCurrentIndex() {
    return this.currentIndex;
  }

  /**
   * ��ȡ��ǰ���е��к�
   * 
   * @return ��ǰ���е��к�
   */
  public int getStartLineNum() {
    return this.startLineNum;
  }

  /**
   * ��ȡ��ǰĩ�е��к�
   * 
   * @return ��ǰĩ�е��к�
   */
  public int getEndLineNum() {
    return this.endLineNum;
  }

  /**
   * ��ȡ��ǰ��ռ������
   * 
   * @return ��ǰ��ռ������
   */
  public int getLineCount() {
    return this.lineCount;
  }

  /**
   * ��ȡ��ǰ���е��ı�
   * 
   * @return ��ǰ�ı�
   */
  public String getStrContent() {
    return this.strContent;
  }

  /**
   * ��ȡ��ǰʵ��ѡ���ڶ��е��ı�
   * 
   * @return ��ǰ�ı�
   */
  public String getStrContentCurrent() {
    return this.strContentCurrent;
  }

  /**
   * ��ȡ��ǰ��չ�е��ı�
   * 
   * @return ��ǰ�ı�
   */
  public String getStrContentExtend() {
    return this.strContentExtend;
  }

}
