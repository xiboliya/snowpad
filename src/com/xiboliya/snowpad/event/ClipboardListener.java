/**
 * Copyright (C) 2024 冰原
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

package com.xiboliya.snowpad.event;

/**
 * 用于监听剪贴板变化的接口
 * 
 * @author 冰原
 * 
 */
public interface ClipboardListener {

  /**
   * 剪贴板内容变化事件
   * @param text 待设置到剪贴板的文本
   */
  void contentChanged(String text);
}
