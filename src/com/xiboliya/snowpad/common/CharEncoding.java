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

package com.xiboliya.snowpad.common;

/**
 * 用于标识文件编码格式的枚举
 * 
 * @author 冰原
 * 
 */
public enum CharEncoding {
  /**
   * GB18030格式编码
   */
  GB18030,
  /**
   * ANSI格式编码
   */
  ANSI,
  /**
   * Unicode Big Endian格式编码
   */
  UBE,
  /**
   * Unicode Little Endian格式编码
   */
  ULE,
  /**
   * UTF-8格式编码
   */
  UTF8,
  /**
   * UTF-8无BOM格式编码
   */
  UTF8_NO_BOM;
  /**
   * 重写父类的方法
   */
  public String toString() {
    switch (this) {
    case GB18030:
      return "GB18030";
    case ANSI:
      return "US-ASCII";
    case UBE:
      return "UTF-16BE";
    case ULE:
      return "UTF-16LE";
    case UTF8:
    case UTF8_NO_BOM:
      return "UTF-8";
    default:
      return "GB18030";
    }
  }

  /**
   * 获取编码格式的名称
   * 
   * @return 编码格式的名称
   */
  public String getName() {
    switch (this) {
    case GB18030:
      return "GB18030";
    case ANSI:
      return "US-ASCII";
    case UBE:
      return "UTF-16BE";
    case ULE:
      return "UTF-16LE";
    case UTF8:
      return "UTF-8";
    case UTF8_NO_BOM:
      return "UTF-8-NO-BOM";
    default:
      return "GB18030";
    }
  }
}
