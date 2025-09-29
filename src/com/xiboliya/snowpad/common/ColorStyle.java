/**
 * Copyright (C) 2025 冰原
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

package com.xiboliya.snowpad.common;

import java.awt.Color;

import com.xiboliya.snowpad.util.Util;

/**
 * 配色方案属性类
 * 
 * @author 冰原
 * 
 */
public class ColorStyle implements Cloneable {

  // 字体颜色
  public Color fontColor = Util.COLOR_DEFAULT_FONT;
  // 背景颜色
  public Color backColor = Util.COLOR_DEFAULT_BACK;
  // 光标颜色
  public Color caretColor = Util.COLOR_DEFAULT_CARET;
  // 选区字体颜色
  public Color selFontColor = Util.COLOR_DEFAULT_SEL_FONT;
  // 选区背景颜色
  public Color selBackColor = Util.COLOR_DEFAULT_SEL_BACK;
  // 匹配括号背景颜色
  public Color bracketBackColor = Util.COLOR_DEFAULT_BRACKET_BACK;
  // 当前行背景颜色
  public Color lineBackColor = Util.COLOR_DEFAULT_LINE_BACK;
  // 匹配文本背景颜色
  public Color wordBackColor = Util.COLOR_DEFAULT_WORD_BACK;
  // 标记文本背景颜色
  public Color markBackColor = Util.COLOR_DEFAULT_MARK_BACK;
  // 书签颜色
  public Color bookmarkColor = Util.COLOR_DEFAULT_BOOKMARK;
  // 行号栏字体颜色
  public Color lineNumberViewFontColor = Util.COLOR_DEFAULT_LINE_NUMBER_VIEW_FONT;
  // 行号栏背景颜色
  public Color lineNumberViewBackColor = Util.COLOR_DEFAULT_LINE_NUMBER_VIEW_BACK;
  // 高亮显示颜色：格式1
  public Color highlightColor1 = Util.COLOR_DEFAULT_HIGHLIGHT_COLOR_1;
  // 高亮显示颜色：格式2
  public Color highlightColor2 = Util.COLOR_DEFAULT_HIGHLIGHT_COLOR_2;
  // 高亮显示颜色：格式3
  public Color highlightColor3 = Util.COLOR_DEFAULT_HIGHLIGHT_COLOR_3;
  // 高亮显示颜色：格式4
  public Color highlightColor4 = Util.COLOR_DEFAULT_HIGHLIGHT_COLOR_4;
  // 高亮显示颜色：格式5
  public Color highlightColor5 = Util.COLOR_DEFAULT_HIGHLIGHT_COLOR_5;

  /**
   * 默认的构造方法
   */
  public ColorStyle() {
  }

  /**
   * 带参数的构造方法
   */
  public ColorStyle(Color[] colors) {
    if (colors.length < 17) {
      return;
    }
    this.fontColor = colors[0];
    this.backColor = colors[1];
    this.caretColor = colors[2];
    this.selFontColor = colors[3];
    this.selBackColor = colors[4];
    this.bracketBackColor = colors[5];
    this.lineBackColor = colors[6];
    this.wordBackColor = colors[7];
    this.markBackColor = colors[8];
    this.bookmarkColor = colors[9];
    this.lineNumberViewFontColor = colors[10];
    this.lineNumberViewBackColor = colors[11];
    this.highlightColor1 = colors[12];
    this.highlightColor2 = colors[13];
    this.highlightColor3 = colors[14];
    this.highlightColor4 = colors[15];
    this.highlightColor5 = colors[16];
  }

  @Override
  public ColorStyle clone() {
    try {
      return (ColorStyle)super.clone();
    } catch (Exception x) {
      // x.printStackTrace();
      return new ColorStyle();
    }
  }
}
