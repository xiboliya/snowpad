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

/**
 * 用于标识高亮显示颜色的枚举
 * 
 * @author 冰原
 * 
 */
public enum HighlightColorStyle {
  /**
   * 高亮显示的颜色1
   */
  STYLE_1,
  /**
   * 高亮显示的颜色2
   */
  STYLE_2,
  /**
   * 高亮显示的颜色3
   */
  STYLE_3,
  /**
   * 高亮显示的颜色4
   */
  STYLE_4,
  /**
   * 高亮显示的颜色5
   */
  STYLE_5,
  /**
   * 高亮匹配括号的颜色
   */
  STYLE_BRACKET,
  /**
   * 高亮匹配双击文本的颜色
   */
  STYLE_WORD,
  /**
   * 高亮标记文本的颜色
   */
  STYLE_MARK,
  /**
   * 用于清理颜色1~颜色5的标识
   */
  EMPTY;

  /**
   * 获取高亮显示颜色的索引值
   * 
   * @return 索引值
   */
  public int getIndex() {
    switch (this) {
    case STYLE_1:
      return 0;
    case STYLE_2:
      return 1;
    case STYLE_3:
      return 2;
    case STYLE_4:
      return 3;
    case STYLE_5:
      return 4;
    case STYLE_BRACKET:
      return 5;
    case STYLE_WORD:
      return 6;
    case STYLE_MARK:
      return 7;
    default:
      return -1;
    }
  }
}
