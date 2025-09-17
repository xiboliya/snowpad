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

  /**
   * 默认的构造方法
   */
  public ColorStyle() {
  }

  /**
   * 带参数的构造方法
   */
  public ColorStyle(Color[] colors) {
    if (colors.length < 8) {
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
