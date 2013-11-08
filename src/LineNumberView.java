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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;
import javax.swing.JComponent;
import javax.swing.JTextArea;

/**
 * 文本域的行号组件
 * 
 * @author 冰原
 * 
 */
public class LineNumberView extends JComponent {
  private static final long serialVersionUID = 1L;
  private int lineHeight = 0; // 当前字体中文本行的标准高度。它是相邻文本行基线之间的距离，是leading、ascent和descent的总和。
  private int maxRowWidth = 0; // 组件显示区域中，最宽一行的字符串宽度
  private FontMetrics fontMetrics = null; // 定义字体规格的对象，该对象封装将在屏幕上显示的字体的有关信息
  private JTextArea txaSource = null; // 想要显示行号的文本域

  /**
   * 自定义的构造方法
   * 
   * @param txaSource
   *          将要显示此行号组件的文本域
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
   * 设置最大显示行数，并根据此行数设置此组件的首选大小
   * 
   * @param row
   *          最大显示行数
   */
  private void setPreferredLine(int row) {
    int width = this.fontMetrics.stringWidth(String.valueOf(row));
    if (this.maxRowWidth < width) {
      this.maxRowWidth = width;
      this.setPreferredSize(new Dimension(3 * Util.LINE_NUMBER_MARGIN
          + this.maxRowWidth, Util.LINE_NUMBER_HEIGHT));
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
  protected void paintComponent(Graphics g) {
    Rectangle rect = g.getClipBounds();
    int startLineNum = (rect.y / this.lineHeight) + 1;
    int endLineNum = startLineNum + (rect.height / this.lineHeight);
    int start = (rect.y / this.lineHeight) * this.lineHeight + this.lineHeight
        - Util.LINE_NUMBER_START_OFFSET;
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