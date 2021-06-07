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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JTextArea;

/**
 * �ı�����к����
 * 
 * @author ��ԭ
 * 
 */
public class LineNumberView extends JComponent {
  private static final long serialVersionUID = 1L;
  private int lineHeight = 0; // ��ǰ�������ı��еı�׼�߶ȡ����������ı��л���֮��ľ��룬��leading��ascent��descent���ܺ͡�
  private int maxRowWidth = 0; // �����ʾ�����У����һ�е��ַ������
  private FontMetrics fontMetrics = null; // ����������Ķ��󣬸ö����װ������Ļ����ʾ��������й���Ϣ
  private JTextArea txaSource = null; // ��Ҫ��ʾ�кŵ��ı���

  /**
   * �Զ���Ĺ��췽��
   * 
   * @param txaSource
   *          ��Ҫ��ʾ���к�������ı���
   */
  public LineNumberView(JTextArea txaSource) {
    if (txaSource != null) {
      this.txaSource = txaSource;
      this.setFont(this.txaSource.getFont());
      this.setPreferredLine(this.txaSource.getLineCount());
    } else {
      this.setFont(Util.TEXT_FONT);
      this.setPreferredLine(0);
    }
    this.setForeground(Color.GRAY);
  }

  /**
   * ���������ʾ�����������ݴ��������ô��������ѡ��С
   * 
   * @param row
   *          �����ʾ����
   */
  private void setPreferredLine(int row) {
    int width = this.fontMetrics.stringWidth(String.valueOf(row));
    if (this.maxRowWidth < width) {
      this.maxRowWidth = width;
      this.setPreferredSize(new Dimension(3 * Util.LINE_NUMBER_MARGIN +
          this.maxRowWidth, Util.LINE_NUMBER_HEIGHT));
    }
  }

  /**
   * ���ô����������
   * 
   * @param font
   *          �����õ�����
   */
  @Override
  public void setFont(Font font) {
    super.setFont(font);
    this.fontMetrics = this.getFontMetrics(this.getFont());
    this.lineHeight = this.fontMetrics.getHeight();
    if (this.txaSource != null) {
      this.setPreferredLine(this.txaSource.getLineCount());
    } else {
      this.setPreferredLine(0);
    }
  }

  /**
   * ���ƴ����
   * 
   * @param g
   *          Graphics��������ͼ�������ĵĳ�����࣬����Ӧ�ó���������Լ�����ͼ���Ͻ��л��ơ�
   */
  @Override
  protected void paintComponent(Graphics g) {
    Rectangle rect = g.getClipBounds();
    int startLineNum = (rect.y / this.lineHeight) + 1;
    int endLineNum = startLineNum + (rect.height / this.lineHeight);
    int start = (rect.y / this.lineHeight) * this.lineHeight + this.lineHeight -
        Util.LINE_NUMBER_START_OFFSET;
    this.setPreferredLine(endLineNum);
    for (int i = startLineNum; i <= endLineNum; i++) {
      String lineNum = String.valueOf(i);
      int width = this.fontMetrics.stringWidth(lineNum);
      g.drawString(lineNum, Util.LINE_NUMBER_MARGIN + this.maxRowWidth - width,
          start);
      start += this.lineHeight;
    }
  }
}