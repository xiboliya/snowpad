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
  private int lineHeight = 0; // 当前字体中文本行的标准高度。它是相邻文本行基线之间的距离，是leading、ascent和descent的总和。
  private int maxRowWidth = 0; // 组件显示区域中，最宽一行的字符串宽度
  private FontMetrics fontMetrics = null; // 定义字体规格的对象，该对象封装将在屏幕上显示的字体的有关信息
  private BaseTextArea txaSource = null; // 想要显示行号的文本域
  private MouseAdapter mouseAdapter = null; // 接收鼠标事件的适配器

  /**
   * 自定义的构造方法
   * 
   * @param txaSource 将要显示此行号组件的文本域
   */
  public LineNumberView(BaseTextArea txaSource) {
    if (txaSource != null) {
      this.txaSource = txaSource;
      this.setFont(this.txaSource.getFont());
      this.setPreferredLine(this.txaSource.getLineCount());
    } else {
      this.setFont(Util.TEXT_FONT);
      this.setPreferredLine(0);
    }
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
        // 通过modelToView定位Y坐标对应的文档行号（从0开始）
        int lineNumber = getLineNumberAtY(y);
        if (lineNumber < 0) {
          return;
        }
        if (e.getButton() == MouseEvent.BUTTON3) { // 点击右键时，设置/取消书签
          txaSource.switchBookmark(lineNumber);
          repaint();
        } else { // 其他情况，选中当前行
          int lineCount = txaSource.getLineCount(); // 文本域总行数
          if (lineNumber >= lineCount) {
            return;
          }
          try {
            int offsetStart = txaSource.getLineStartOffset(lineNumber);
            int offsetEnd = txaSource.getLineEndOffset(lineNumber);
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
   * 根据Y坐标查找对应的文档行号（从0开始）
   * 使用modelToView进行二分查找，支持自动换行模式下行号的正确定位
   * 
   * @param y Y坐标
   * @return 文档行号（从0开始），如果找不到则返回-1
   */
  private int getLineNumberAtY(int y) {
    if (this.txaSource == null || this.txaSource.getLineCount() <= 0) {
      return -1;
    }
    int lineCount = this.txaSource.getLineCount();
    int lineStart = 0;
    int lineEnd = lineCount - 1;
    int result = -1;
    while (lineStart <= lineEnd) {
      int mid = (lineStart + lineEnd) / 2;
      try {
        int startOffset = this.txaSource.getLineStartOffset(mid);
        Rectangle lineRect = this.txaSource.modelToView(startOffset);
        if (lineRect == null) {
          lineEnd = mid - 1;
          continue;
        }
        // 计算当前行的底部Y坐标：如果存在下一行，取下一行的起始Y；否则用当前行Y加行高
        int lineBottom;
        if (mid + 1 < lineCount) {
          try {
            int nextStartOffset = this.txaSource.getLineStartOffset(mid + 1);
            Rectangle nextRect = this.txaSource.modelToView(nextStartOffset);
            if (nextRect != null) {
              lineBottom = nextRect.y;
            } else {
              lineBottom = lineRect.y + this.lineHeight;
            }
          } catch (Exception x) {
            lineBottom = lineRect.y + this.lineHeight;
          }
        } else {
          lineBottom = lineRect.y + this.lineHeight;
        }
        if (y >= lineRect.y && y < lineBottom) {
          result = mid;
          break;
        } else if (y < lineRect.y) {
          lineEnd = mid - 1;
        } else {
          lineStart = mid + 1;
        }
      } catch (Exception x) {
        lineEnd = mid - 1;
      }
    }
    return result;
  }

  /**
   * 设置最大显示行数，并根据此行数设置此组件的首选大小
   * 
   * @param row 最大显示行数
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
   * @param font 将设置的字体
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
   * 通过modelToView将每个文档行的起始偏移量映射到实际像素Y坐标，
   * 从而在自动换行模式下也能正确显示行号。
   * 
   * @param g Graphics类是所有图形上下文的抽象基类，允许应用程序在组件以及闭屏图像上进行绘制。
   */
  @Override
  protected synchronized void paintComponent(Graphics g) {
    if (this.txaSource == null) {
      return;
    }
    Color color = g.getColor();
    g.setColor(Util.setting.colorStyle.lineNumberViewBackColor);
    g.fillRect(0, 0, this.getWidth(), this.getHeight());
    Rectangle rect = g.getClipBounds();
    int totalLines = this.txaSource.getLineCount();
    this.setPreferredLine(totalLines);
    g.setColor(Util.setting.colorStyle.lineNumberViewFontColor);

    // 二分查找：找到可见区域内第一条文档行
    // 找到最后一个起始Y坐标小于裁剪区域顶部的行
    int startLine = 0;
    int lineStart = 0;
    int lineEnd = totalLines - 1;
    while (lineStart <= lineEnd) {
      int mid = (lineStart + lineEnd) / 2;
      try {
        int startOffset = this.txaSource.getLineStartOffset(mid);
        Rectangle lineRect = this.txaSource.modelToView(startOffset);
        if (lineRect == null) {
          lineEnd = mid - 1;
          continue;
        }
        if (lineRect.y < rect.y) {
          startLine = mid;
          lineStart = mid + 1;
        } else {
          lineEnd = mid - 1;
        }
      } catch (BadLocationException x) {
        lineEnd = mid - 1;
      }
    }

    // 从起始行开始向后绘制行号，直到超出可见区域
    for (int i = startLine; i < totalLines; i++) {
      try {
        int startOffset = this.txaSource.getLineStartOffset(i);
        Rectangle lineRect = this.txaSource.modelToView(startOffset);
        if (lineRect == null) {
          continue;
        }
        int lineY = lineRect.y + this.lineHeight - LINE_NUMBER_START_OFFSET;
        // 如果已超出可见区域底部，则停止绘制
        if (lineY > rect.y + rect.height) {
          break;
        }
        String lineNum = String.valueOf(i + 1);
        int stringWidth = this.fontMetrics.stringWidth(lineNum);
        // 绘制行号
        g.drawString(lineNum, LINE_NUMBER_MARGIN + this.maxRowWidth - stringWidth, lineY);
      } catch (BadLocationException x) {
        // x.printStackTrace();
      }
    }

    // 绘制书签
    LinkedList<Integer> bookmarks = this.txaSource.getBookmarks();
    int size = bookmarks.size();
    if (size <= 0) {
      g.setColor(color);
      return;
    }
    g.setColor(Util.setting.colorStyle.bookmarkColor);
    int width = this.getWidth();
    int bookmarkWidth = LINE_NUMBER_MARGIN_RIGHT - 2;
    for (int i = 0; i < size; i++) {
      int bookmark = bookmarks.get(i);
      try {
        int startOffset = this.txaSource.getLineStartOffset(bookmark);
        Rectangle lineRect = this.txaSource.modelToView(startOffset);
        if (lineRect == null) {
          continue;
        }
        int height = lineRect.y + (this.lineHeight - bookmarkWidth) / 2 + LINE_NUMBER_START_OFFSET;
        // 绘制书签
        g.fillOval(width - LINE_NUMBER_MARGIN_RIGHT, height, bookmarkWidth, bookmarkWidth);
      } catch (BadLocationException x) {
        // x.printStackTrace();
      }
    }
    g.setColor(color);
  }
}
