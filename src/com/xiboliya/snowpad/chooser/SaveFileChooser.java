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

package com.xiboliya.snowpad.chooser;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

import com.xiboliya.snowpad.base.BaseFileFilter;
import com.xiboliya.snowpad.util.Util;

/**
 * "保存"文件选择器
 * 
 * @author 冰原
 * 
 */
public class SaveFileChooser extends JFileChooser {
  private static final long serialVersionUID = 1L;

  public SaveFileChooser() {
    super();
    Util.addChoosableFileFilters(this);
  }

  /**
   * 当用户确认时将调用此方法
   */
  @Override
  public void approveSelection() {
    File file = this.getSelectedFile();
    FileFilter fileFilter = this.getFileFilter(); // 获取当前的文件过滤器
    if (fileFilter instanceof BaseFileFilter) { // 避免当前为默认过滤器时，而产生异常
      BaseFileFilter baseFileFilter = (BaseFileFilter) fileFilter;
      file = Util.checkFileName(file.getAbsolutePath(), baseFileFilter,
          baseFileFilter.getExt());
    }
    this.setSelectedFile(file);
    if (file != null && file.exists()) { // 当用户选择的文件已经存在时，将弹出提示框
      int result = JOptionPane.showConfirmDialog(this, file + " 已存在。\n是否覆盖？",
          "保存", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
      if (JOptionPane.YES_OPTION == result) { // 用户选择覆盖
        super.approveSelection();
      }
    } else {
      super.approveSelection();
    }
  }
}
