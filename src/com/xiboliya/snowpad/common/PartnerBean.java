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
 * 保存广义键/值对的简单类
 * 
 * @author 冰原
 * 
 */
public class PartnerBean {
  private Object object = null;
  private int index = -1;

  /**
   * 构造方法
   */
  public PartnerBean() {
  }

  /**
   * 构造方法
   * 
   * @param object 将要保存的值
   * @param index 将要保存的键
   */
  public PartnerBean(Object object, int index) {
    this.setObject(object);
    this.setIndex(index);
  }

  /**
   * 获取当前值
   * 
   * @return 当前值
   */
  public Object getObject() {
    return this.object;
  }

  /**
   * 获取当前键
   * 
   * @return 当前键
   */
  public int getIndex() {
    return this.index;
  }

  /**
   * 设置当前值
   * 
   * @param object 将要保存的值
   */
  public void setObject(Object object) {
    this.object = object;
  }

  /**
   * 设置当前键
   * 
   * @param index 将要保存的键
   */
  public void setIndex(int index) {
    this.index = index;
  }
}
