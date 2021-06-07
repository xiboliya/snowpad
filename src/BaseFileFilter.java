/**
 * Copyright (C) 2013 ��ԭ
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

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * �����õ��ļ�������
 * 
 * @author ��ԭ
 * 
 */
public class BaseFileFilter extends FileFilter {
  private String ext = ""; // �ļ���չ��
  private String description = ""; // ��������
  private boolean isAcceptDirectory = true; // �Ƿ���ʾĿ¼�������ʾ��Ϊtrue

  public BaseFileFilter(String ext, String description) {
    this(ext, description, true);
  }

  public BaseFileFilter(String ext, String description,
      boolean isAcceptDirectory) {
    this.ext = ext;
    this.description = description;
    this.isAcceptDirectory = isAcceptDirectory;
  }

  public boolean accept(File file) {
    if (this.isAcceptDirectory && file.isDirectory()) { // ��ʾĿ¼
      return true;
    } else if (file.isFile() &&
        file.getName().toLowerCase().endsWith(this.ext.toLowerCase())) { // ��ʾָ����չ�����ļ�
      return true;
    } else {
      return false;
    }
  }

  public String getDescription() {
    return this.description; // �����ļ�ѡ��������ʾ����������
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setExt(String ext) {
    this.ext = ext;
  }

  public String getExt() {
    return this.ext;
  }
}
