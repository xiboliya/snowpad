/**
 * Copyright (C) 2014 冰原
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

import javax.swing.table.DefaultTableModel;
import java.util.Vector;

/**
 * 表格数据模型
 * 
 * @author 冰原
 * 
 */
public class BaseDefaultTableModel extends DefaultTableModel {
  private static final long serialVersionUID = 1L;

  public BaseDefaultTableModel() {
    super();
  }

  public BaseDefaultTableModel(Object[][] data, Object[] columnNames) {
    super(data, columnNames);
  }

  public BaseDefaultTableModel(Vector<Vector<String>> data, Vector<String> columnNames) {
    super(data, columnNames);
  }

  /**
   * 表格中的单元格是否可被编辑
   */
  public boolean isCellEditable(int row, int column) {
    return false; // 表格中所有的单元格都不可被编辑
  }

}
