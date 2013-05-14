package com.xiboliya.snowpad;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;

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
    if (file != null && file.exists()) {
      this.setSelectedFile(file);
      super.approveSelection();
    } else { // 当用户未选择文件或选择的文件不存在时，将弹出提示框
      JOptionPane.showMessageDialog(this, "文件不存在，请重新选择！", Util.SOFTWARE,
          JOptionPane.CANCEL_OPTION);
    }
  }
}
