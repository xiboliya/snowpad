/**
 * Copyright (C) 2020 冰原
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
 * 保存已打开的文件配置类
 * 
 * @author 冰原
 * 
 */
public class FileHistoryBean {
  private String fileName = null;
  private boolean isFrozen = false;

  /**
   * 构造方法
   */
  public FileHistoryBean() {
  }

  /**
   * 构造方法
   * 
   * @param fileName
   *          文件名
   * @param isFrozen
   *          是否冻结文件
   */
  public FileHistoryBean(String fileName, boolean isFrozen) {
    this.setFileName(fileName);
    this.setFrozen(isFrozen);
  }

  /**
   * 获取文件名
   * 
   * @return 文件名
   */
  public String getFileName() {
    return this.fileName;
  }

  /**
   * 获取是否冻结文件
   * 
   * @return 是否冻结文件
   */
  public boolean getFrozen() {
    return this.isFrozen;
  }

  /**
   * 设置文件名
   * 
   * @param fileName
   *          文件名
   */
  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  /**
   * 设置是否冻结文件
   * 
   * @param isFrozen
   *          是否冻结文件
   */
  public void setFrozen(boolean isFrozen) {
    this.isFrozen = isFrozen;
  }
}
