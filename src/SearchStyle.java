/**
 * Copyright (C) 2014 冰原
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

/**
 * 用于标识搜索模式的枚举
 * 
 * @author 冰原
 * 
 */
public enum SearchStyle {
  /**
   * 普通模式，此为默认模式
   */
  DEFAULT,
  /**
   * 转义扩展模式，可以使用\n代替换行，\t代替Tab字符
   */
  TRANSFER,
  /**
   * 正则表达式模式
   */
  PATTERN;
}
