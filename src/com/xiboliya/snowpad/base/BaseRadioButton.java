/**
 * Copyright (C) 2025 冰原
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
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JRadioButton;

/**
 * 用于统一行为的JRadioButton单选按钮类
 * 
 * @author 冰原
 * 
 */
public class BaseRadioButton extends JRadioButton {
  private static final long serialVersionUID = 1L;

  public BaseRadioButton() {
    super();
    this.init();
  }

  public BaseRadioButton(Action action) {
    super(action);
    this.init();
  }

  public BaseRadioButton(Icon icon) {
    super(icon);
    this.init();
  }

  public BaseRadioButton(Icon icon, boolean selected) {
    super(icon, selected);
    this.init();
  }

  public BaseRadioButton(String text) {
    super(text);
    this.init();
  }

  public BaseRadioButton(String text, boolean selected) {
    super(text, selected);
    this.init();
  }

  public BaseRadioButton(String text, Icon icon) {
    super(text, icon);
    this.init();
  }

  public BaseRadioButton(String text, Icon icon, boolean selected) {
    super(text, icon, selected);
    this.init();
  }

  private void init() {
    // 设置默认边距为0
    this.setMargin(new Insets(0, 0, 0, 0));
  }
}
