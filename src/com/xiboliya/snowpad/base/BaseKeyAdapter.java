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

package com.xiboliya.snowpad.base;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * 当用户按下Esc键时关闭当前对话框，按Enter键时执行相应操作的键盘事件类
 * 
 * @author 冰原
 * 
 */
public class BaseKeyAdapter extends KeyAdapter {
  private BaseDialog dialog = null;
  private boolean applyEnter = true;

  public BaseKeyAdapter(BaseDialog dialog) {
    this.dialog = dialog;
  }

  public BaseKeyAdapter(BaseDialog dialog, boolean applyEnter) {
    this.dialog = dialog;
    this.applyEnter = applyEnter;
  }

  public void keyPressed(KeyEvent e) {
    if (this.dialog == null) {
      return;
    }
    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
      this.dialog.onCancel();
    } else if (e.getKeyCode() == KeyEvent.VK_ENTER && this.applyEnter) {
      this.dialog.onEnter();
    }
  }
}
