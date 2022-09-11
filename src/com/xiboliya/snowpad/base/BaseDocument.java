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

package com.xiboliya.snowpad.base;

import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;

/**
 * 用于统一行为的文本模型类
 * 
 * @author 冰原
 * 
 */
public class BaseDocument extends PlainDocument {
  private boolean isFrozen = false; // 是否被冻结

  /**
   * 将字符串插入文档
   * 
   * @param offset
   *          起始偏移量
   * @param str
   *          要插入的字符串
   * @param attr
   *          插入内容的属性
   */
  public void insertString(int offset, String str, AttributeSet attr) {
    if (this.isFrozen) {
      return;
    }
    try {
      super.insertString(offset, str, attr);
    } catch (Exception x) {
      // x.printStackTrace();
    }
  }

  /**
   * 从文档中移除字符串
   * 
   * @param offset
   *          起始偏移量
   * @param len
   *          要移除的字符数
   */
  public void remove(int offset, int len) {
    if (this.isFrozen) {
      return;
    }
    try {
      super.remove(offset, len);
    } catch (Exception x) {
      // x.printStackTrace();
    }
  }

  public void setFrozen(boolean isFrozen) {
    this.isFrozen = isFrozen;
  }

  public boolean getFrozen() {
    return this.isFrozen;
  }

}
