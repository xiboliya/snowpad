/**
 * Copyright (C) 2018 ��ԭ
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

import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;

/**
 * ����ͳһ��Ϊ���ı�ģ����
 * 
 * @author ��ԭ
 * 
 */
public class BaseDocument extends PlainDocument {
  private boolean isFrozen = false; // �Ƿ񱻶���

  /**
   * ���ַ��������ĵ�
   * 
   * @param offset
   *          ��ʼƫ����
   * @param str
   *          Ҫ������ַ���
   * @param attr
   *          �������ݵ�����
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
   * ���ĵ����Ƴ��ַ���
   * 
   * @param offset
   *          ��ʼƫ����
   * @param len
   *          Ҫ�Ƴ����ַ���
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
