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

package com.xiboliya.snowpad.view;

import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;

/**
 * 颜色显示组件
 * 
 * @author 冰原
 * 
 */
public class ColorView extends JComponent {
  private static final long serialVersionUID = 1L;
  private Color color = null;

  public ColorView() {
  }

  public ColorView(Color color) {
    this.color = color;
  }

  public void setColor(Color color) {
    this.color = color;
    this.repaint();
  }

  /**
   * 绘制此组件
   * 
   * @param g Graphics类是所有图形上下文的抽象基类，允许应用程序在组件以及闭屏图像上进行绘制。
   */
  @Override
  protected void paintComponent(Graphics g) {
    Color color = g.getColor();
    if (this.color != null) {
      g.setColor(this.color);
    }
    g.fillRect(0, 0, this.getWidth(), this.getHeight());
    g.setColor(color);
  }
}
