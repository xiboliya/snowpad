/**
 * Copyright (C) 2018 冰原
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
 * 保存文件中查找的偏移量的简单类
 * 
 * @author 冰原
 * 
 */
public class SearchBean {
  private int start = -1;
  private int end = -1;

  /**
   * 构造方法
   */
  public SearchBean() {
  }

  /**
   * 获取文件中start偏移量
   * 
   * @return 文件中start偏移量
   */
  public int getStart() {
    return this.start;
  }

  /**
   * 获取文件中end偏移量
   * 
   * @return 文件中end偏移量
   */
  public int getEnd() {
    return this.end;
  }

  /**
   * 设置文件中start偏移量
   * 
   * @param start 文件中start偏移量
   */
  public void setStart(int start) {
    this.start = start;
  }

  /**
   * 设置文件中end偏移量
   * 
   * @param end 文件中end偏移量
   */
  public void setEnd(int end) {
    this.end = end;
  }
}
