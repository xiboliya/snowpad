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

/**
 * �����ļ��в��ҵ�ƫ�����ļ���
 * 
 * @author ��ԭ
 * 
 */
public class SearchBean {
  private int start = -1;
  private int end = -1;

  /**
   * ���췽��
   */
  public SearchBean() {
  }

  /**
   * ��ȡ�ļ���startƫ����
   * 
   * @return �ļ���startƫ����
   */
  public int getStart() {
    return this.start;
  }

  /**
   * ��ȡ�ļ���endƫ����
   * 
   * @return �ļ���endƫ����
   */
  public int getEnd() {
    return this.end;
  }

  /**
   * �����ļ���startƫ����
   * 
   * @param start
   *          �ļ���startƫ����
   */
  public void setStart(int start) {
    this.start = start;
  }

  /**
   * �����ļ���endƫ����
   * 
   * @param end
   *          �ļ���endƫ����
   */
  public void setEnd(int end) {
    this.end = end;
  }
}
