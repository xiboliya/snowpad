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

package com.xiboliya.snowpad.base;

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * 可重用的文件过滤器
 * 
 * @author 冰原
 * 
 */
public class BaseFileFilter extends FileFilter {
  private String ext = ""; // 文件扩展名
  private String description = ""; // 文字描述
  private boolean isAcceptDirectory = true; // 是否显示目录，如果显示则为true

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
    if (this.isAcceptDirectory && file.isDirectory()) { // 显示目录
      return true;
    } else if (file.isFile() &&
        file.getName().toLowerCase().endsWith(this.ext.toLowerCase())) { // 显示指定扩展名的文件
      return true;
    } else {
      return false;
    }
  }

  public String getDescription() {
    return this.description; // 返回文件选择器中显示的文字描述
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
