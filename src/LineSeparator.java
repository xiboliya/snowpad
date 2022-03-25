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

/**
 * 用于标识换行符格式的枚举
 * 
 * @author 冰原
 * 
 */
public enum LineSeparator {
  /**
   * 当前操作系统的行分隔符
   */
  DEFAULT,
  /**
   * Unix/Linux操作系统的行分隔符
   */
  UNIX,
  /**
   * Macintosh操作系统的行分隔符
   */
  MACINTOSH,
  /**
   * Windows操作系统的行分隔符
   */
  WINDOWS;
  /**
   * 重写父类的方法
   */
  public String toString() {
    switch (this) {
    case UNIX:
      return "\n";
    case MACINTOSH:
      return "\r";
    case WINDOWS:
      return "\r\n";
    case DEFAULT:
    default:
      return Util.LINE_SEPARATOR;
    }
  }

  /**
   * 获取换行符格式的名称
   * 
   * @return 换行符格式的名称
   */
  public String getName() {
    switch (this) {
    case UNIX:
      return "Unix";
    case MACINTOSH:
      return "Macintosh";
    case WINDOWS:
      return "Windows";
    default:
      if (Util.LINE_SEPARATOR.equals("\n")) {
        return "Unix";
      } else if (Util.LINE_SEPARATOR.equals("\r")) {
        return "Macintosh";
      } else {
        return "Windows";
      }
    }
  }
}
