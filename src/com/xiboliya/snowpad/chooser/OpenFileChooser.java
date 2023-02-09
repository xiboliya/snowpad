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
 * "打开"文件选择器
 * 
 * @author 冰原
 * 
 */
public class OpenFileChooser extends JFileChooser {
  private static final long serialVersionUID = 1L;

  public OpenFileChooser() {
    super();
    this.setMultiSelectionEnabled(true);
    this.setFileHidingEnabled(false);
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
    if (file != null && file.exists()) {
      this.setSelectedFile(file);
      super.approveSelection();
    } else { // 当用户未选择文件或选择的文件不存在时，将弹出提示框
      JOptionPane.showMessageDialog(this, "文件不存在，请重新选择！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
    }
  }
}
