package com.xiboliya.snowpad;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

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
