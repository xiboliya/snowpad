/**
 * Copyright (C) 2014 ��ԭ
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
 * �������ģ��
 * 
 * @author ��ԭ
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
   * ����еĵ�Ԫ���Ƿ�ɱ��༭
   */
  public boolean isCellEditable(int row, int column) {
    return false; // ��������еĵ�Ԫ�񶼲��ɱ��༭
  }

}
