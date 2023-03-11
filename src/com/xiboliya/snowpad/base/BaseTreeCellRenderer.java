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

import java.awt.Component;
import java.io.File;
import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

import com.xiboliya.snowpad.base.BaseTreeNode;
import com.xiboliya.snowpad.util.Util;

/**
 * 自定义的文件树条目渲染器
 * 
 * @author 冰原
 * 
 */
public class BaseTreeCellRenderer extends DefaultTreeCellRenderer {
  @Override
  public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) {
    super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
    BaseTreeNode node = (BaseTreeNode)value;
    String content = node.getContent();
    if (!Util.isTextEmpty(content)) {
      File file = new File(content);
      if (file.isDirectory()) {
        if (expanded) {
          this.setIcon(this.getDefaultOpenIcon());
        } else {
          this.setIcon(this.getDefaultClosedIcon());
        }
      } else {
        this.setIcon(this.getDefaultLeafIcon());
      }
    } else {
      if (expanded) {
        this.setIcon(this.getDefaultOpenIcon());
      } else {
        this.setIcon(this.getDefaultClosedIcon());
      }
    }
    return this;
  }
}
