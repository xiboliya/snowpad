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

package com.xiboliya.snowpad.view;

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

import com.xiboliya.snowpad.base.BaseTextArea;
import com.xiboliya.snowpad.util.Util;

/**
 * 文本域的行号组件
 * 
 * @author 冰原
 * 
 */
public class LineNumberView extends JComponent {
  private static final long serialVersionUID = 1L;
  private static final int LINE_NUMBER_HEIGHT = 2000000000; // 行号组件支持的最大高度
  private static final int LINE_NUMBER_MARGIN = 3; // 行号组件的左边距
  private static final int LINE_NUMBER_MARGIN_RIGHT = 12; // 行号组件的左边距
  private static final int LINE_NUMBER_START_OFFSET = 2; // 行号组件的起始垂直偏移量，用于对齐文本域的各行
  private static final Color COLOR_BOOKMARK = new Color(128, 0, 128); // 书签的颜色
  private int lineHeight = 0; // 当前字体中文本行的标准高度。它是相邻文本行基线之间的距离，是leading、ascent和descent的总和。
  private int maxRowWidth = 0; // 组件显示区域中，最宽一行的字符串宽度
  private FontMetrics fontMetrics = null; // 定义字体规格的对象，该对象封装将在屏幕上显示的字体的有关信息
  private BaseTextArea txaSource = null; // 想要显示行号的文本域
  private MouseAdapter mouseAdapter = null; // 接收鼠标事件的适配器

  /**
   * 自定义的构造方法
   * 
   * @param txaSource
   *          将要显示此行号组件的文本域
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
   * 为本控件添加各种事件监听器
   */
  private void addListeners() {
    this.mouseAdapter = new MouseAdapter() {
      public void mouseClicked(MouseEvent e) {
        if (txaSource == null) {
          return;
        }
        int y = e.getY();
        int lineNumber = y / lineHeight; // 当前点击的行号
        if (e.getButton() == MouseEvent.BUTTON3) { // 点击右键时，设置/取消书签
          txaSource.switchBookmark(lineNumber);
          repaint();
        } else { // 其他情况，选中当前行
          int lineCount = txaSource.getLineCount(); // 文本域总行数
          lineNumber++;
          if (lineNumber > lineCount) {
            return;
          }
          try {
            int offsetStart = txaSource.getLineStartOffset(lineNumber - 1);
            int offsetEnd = txaSource.getLineEndOffset(lineNumber - 1);
            txaSource.select(offsetStart, offsetEnd); // 选中当前行号的文本
          } catch (BadLocationException x) {
            // x.printStackTrace();
          }
        }
      }
    };
    this.addMouseListener(this.mouseAdapter);
  }

  /**
   * 设置最大显示行数，并根据此行数设置此组件的首选大小
   * 
   * @param row
   *          最大显示行数
   */
  private void setPreferredLine(int row) {
    // 为了解决不在前台的情况下初始化时，行号栏宽度较小的问题，最小设置两位数行号的宽度
    if (row < 10) {
      row = 10;
    }
    int width = this.fontMetrics.stringWidth(String.valueOf(row));
    if (this.maxRowWidth != width) {
      this.maxRowWidth = width;
      this.setPreferredSize(new Dimension(LINE_NUMBER_MARGIN + LINE_NUMBER_MARGIN_RIGHT +
          this.maxRowWidth, LINE_NUMBER_HEIGHT));
    }
  }

  /**
   * 设置此组件的字体
   * 
   * @param font
   *          将设置的字体
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
   * 绘制此组件
   * 
   * @param g
   *          Graphics类是所有图形上下文的抽象基类，允许应用程序在组件以及闭屏图像上进行绘制。
   */
  @Override
  protected synchronized void paintComponent(Graphics g) {
    Rectangle rect = g.getClipBounds();
    int startLineNum = (rect.y / this.lineHeight) + 1;
    int endLineNum = startLineNum + (rect.height / this.lineHeight);
    int start = (rect.y / this.lineHeight) * this.lineHeight + this.lineHeight - LINE_NUMBER_START_OFFSET;
    this.setPreferredLine(endLineNum);
    for (int i = startLineNum; i <= endLineNum; i++) {
      String lineNum = String.valueOf(i);
      int stringWidth = this.fontMetrics.stringWidth(lineNum);
      // 绘制行号
      g.drawString(lineNum, LINE_NUMBER_MARGIN + this.maxRowWidth - stringWidth, start);
      start += this.lineHeight;
    }
    LinkedList<Integer> bookmarks = txaSource.getBookmarks();
    int size = bookmarks.size();
    if (size <= 0) {
      return;
    }
    Color color = g.getColor();
    g.setColor(COLOR_BOOKMARK);
    int width = this.getWidth();
    int bookmarkWidth = LINE_NUMBER_MARGIN_RIGHT - 2;
    for (int i = 0; i < size; i++) {
      int height = bookmarks.get(i) * this.lineHeight + (this.lineHeight - bookmarkWidth) / 2 + LINE_NUMBER_START_OFFSET;
      // 绘制书签
      g.fillOval(width - LINE_NUMBER_MARGIN_RIGHT, height, bookmarkWidth, bookmarkWidth);
    }
    g.setColor(color);
  }
}
