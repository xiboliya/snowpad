/**
 * Copyright (C) 2020 ��ԭ
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
 * �����Ѵ򿪵��ļ�������
 * 
 * @author ��ԭ
 * 
 */
public class FileHistoryBean {
  private String fileName = null;
  private boolean isFrozen = false;

  /**
   * ���췽��
   */
  public FileHistoryBean() {
  }

  /**
   * ���췽��
   * 
   * @param fileName
   *          �ļ���
   * @param isFrozen
   *          �Ƿ񶳽��ļ�
   */
  public FileHistoryBean(String fileName, boolean isFrozen) {
    this.setFileName(fileName);
    this.setFrozen(isFrozen);
  }

  /**
   * ��ȡ�ļ���
   * 
   * @return �ļ���
   */
  public String getFileName() {
    return this.fileName;
  }

  /**
   * ��ȡ�Ƿ񶳽��ļ�
   * 
   * @return �Ƿ񶳽��ļ�
   */
  public boolean getFrozen() {
    return this.isFrozen;
  }

  /**
   * �����ļ���
   * 
   * @param fileName
   *          �ļ���
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * �����Ƿ񶳽��ļ�
   * 
   * @param isFrozen
   *          �Ƿ񶳽��ļ�
   */
  public void setFrozen(boolean isFrozen) {
    this.isFrozen = isFrozen;
  }
}
