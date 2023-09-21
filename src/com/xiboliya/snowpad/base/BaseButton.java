/**
 * Copyright (C) 2023 冰原
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

import java.awt.Insets;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * 用于统一行为的JButton按钮类
 * 
 * @author 冰原
 * 
 */
public class BaseButton extends JButton {
  private static final long serialVersionUID = 1L;

  public BaseButton() {
    super();
    this.init();
  }

  public BaseButton(String text) {
    super(text);
    this.init();
  }

  public BaseButton(Icon icon) {
    super(icon);
    this.init();
  }

  public BaseButton(String text, Icon icon) {
    super(text, icon);
    this.init();
  }

  private void init() {
    // 设置默认边距为0
    this.setMargin(new Insets(0, 0, 0, 0));
  }
}
