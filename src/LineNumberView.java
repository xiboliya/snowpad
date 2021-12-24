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
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.util.LinkedList;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;

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
  private BaseTextArea txaSource = null; // ��Ҫ��ʾ�кŵ��ı���
  private MouseAdapter mouseAdapter = null; // ��������¼���������

  /**
   * �Զ���Ĺ��췽��
   * 
   * @param txaSource
   *          ��Ҫ��ʾ���к�������ı���
   */
  public LineNumberView(JTextArea txaSource) {
    if (txaSource != null) {
      this.txaSource = (BaseTextArea)txaSource;
      this.setFont(this.txaSource.getFont());
      this.setPreferredLine(this.txaSource.getLineCount());
    } else {
      this.setFont(Util.TEXT_FONT);
      this.setPreferredLine(0);
    }
    this.setForeground(Color.GRAY);
    this.addListeners();
  }

  /**
   * Ϊ���ؼ���Ӹ����¼�������
   */
  private void addListeners() {
    this.mouseAdapter = new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (txaSource == null) {
          return;
        }
        int y = e.getY();
        int lineNumber = y / lineHeight; // ��ǰ������к�
        if (e.getButton() == MouseEvent.BUTTON3) { // ����Ҽ�ʱ������/ȡ����ǩ
          txaSource.switchBookmark(lineNumber);
          repaint();
        } else { // ���������ѡ�е�ǰ��
          int lineCount = txaSource.getLineCount(); // �ı���������
          lineNumber++;
          if (lineNumber > lineCount) {
            return;
          }
          try {
            int offsetStart = txaSource.getLineStartOffset(lineNumber - 1);
            int offsetEnd = txaSource.getLineEndOffset(lineNumber - 1);
            txaSource.select(offsetStart, offsetEnd); // ѡ�е�ǰ�кŵ��ı�
          } catch (BadLocationException x) {
            // x.printStackTrace();
          }
        }
      }
    };
    this.addMouseListener(this.mouseAdapter);
  }

  /**
   * ���������ʾ�����������ݴ��������ô��������ѡ��С
   * 
   * @param row
   *          �����ʾ����
   */
  private void setPreferredLine(int row) {
    // Ϊ�˽������ǰ̨������³�ʼ��ʱ���к�����Ƚ�С�����⣬��С������λ���кŵĿ��
    if (row < 10) {
      row = 10;
    }
    int width = this.fontMetrics.stringWidth(String.valueOf(row));
    if (this.maxRowWidth != width) {
      this.maxRowWidth = width;
      this.setPreferredSize(new Dimension(Util.LINE_NUMBER_MARGIN + Util.LINE_NUMBER_MARGIN_RIGHT +
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
  protected synchronized void paintComponent(Graphics g) {
    Rectangle rect = g.getClipBounds();
    int startLineNum = (rect.y / this.lineHeight) + 1;
    int endLineNum = startLineNum + (rect.height / this.lineHeight);
    int start = (rect.y / this.lineHeight) * this.lineHeight + this.lineHeight -
        Util.LINE_NUMBER_START_OFFSET;
    this.setPreferredLine(endLineNum);
    for (int i = startLineNum; i <= endLineNum; i++) {
      String lineNum = String.valueOf(i);
      int stringWidth = this.fontMetrics.stringWidth(lineNum);
      // �����к�
      g.drawString(lineNum, Util.LINE_NUMBER_MARGIN + this.maxRowWidth - stringWidth, start);
      start += this.lineHeight;
    }
    LinkedList<Integer> bookmarks = txaSource.getBookmarks();
    int size = bookmarks.size();
    if (size <= 0) {
      return;
    }
    Color color = g.getColor();
    g.setColor(Util.COLOR_BOOKMARK);
    int width = this.getWidth();
    int bookmarkWidth = Util.LINE_NUMBER_MARGIN_RIGHT - 2;
    for (int i = 0; i < size; i++) {
      int height = bookmarks.get(i) * this.lineHeight + (this.lineHeight - bookmarkWidth) / 2 + Util.LINE_NUMBER_START_OFFSET;
      // ������ǩ
      g.fillOval(width - Util.LINE_NUMBER_MARGIN_RIGHT, height,
          bookmarkWidth, bookmarkWidth);
    }
    g.setColor(color);
  }
}
