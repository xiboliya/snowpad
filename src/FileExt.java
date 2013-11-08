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

package com.xiboliya.snowpad;

/**
 * 用于标识文件扩展名的枚举
 * 
 * @author 冰原
 * 
 */
public enum FileExt {
  /**
   * Ada语言源文件
   */
  ADA,
  /**
   * Active Server Page(动态服务器页面)
   */
  ASP,
  /**
   * Batch(批处理文件)
   */
  BAT,
  /**
   * C Source(C语言源文件)
   */
  C,
  /**
   * C++ Source(C++语言源文件)
   */
  CPP,
  /**
   * C# Source(C#语言源文件)
   */
  CS,
  /**
   * Cascading Style Sheet(层叠样式表)
   */
  CSS,
  /**
   * C/C++ Head(C/C++语言头文件)
   */
  H,
  /**
   * Hypertext Markup Language(超文本标记语言)
   */
  HTM,
  /**
   * Hypertext Markup Language(超文本标记语言)
   */
  HTML,
  /**
   * Device INFormation File(驱动程序信息文件)
   */
  INF,
  /**
   * Initialization File(初始化设置文件)
   */
  INI,
  /**
   * java语言源文件
   */
  JAVA,
  /**
   * javaScript脚本语言源文件
   */
  JS,
  /**
   * Java Server Page(java服务器页面)
   */
  JSP,
  /**
   * LISt Processor(列表处理语言)
   */
  LISP,
  /**
   * 日志文件
   */
  LOG,
  /**
   * Lua可嵌入式脚本语言
   */
  LUA,
  /**
   * Objective-C语言源文件
   */
  M,
  /**
   * Makefile布署文件
   */
  MK,
  /**
   * Pascal语言源文件
   */
  PAS,
  /**
   * 补丁文件
   */
  PATCH,
  /**
   * PHP:Hypertext Preprocessor(超文本预处理语言)
   */
  PHP,
  /**
   * Perl脚本语言源文件
   */
  PL,
  /**
   * Python脚本语言源文件
   */
  PY,
  /**
   * Ruby脚本语言源文件
   */
  RB,
  /**
   * 注册表操作文件
   */
  REG,
  /**
   * Shell脚本语言源文件
   */
  SH,
  /**
   * Structured Query Language(结构化查询语言)
   */
  SQL,
  /**
   * Smalltalk语言源文件
   */
  ST,
  /**
   * 普通文本文件
   */
  TXT,
  /**
   * Visual Basic(VB语言源文件)
   */
  VB,
  /**
   * Extensible Markup Language(可扩展标记语言)
   */
  XML;

  /**
   * 重写父类的方法
   */
  public String toString() {
    switch (this) {
    case ADA:
      return ".ada";
    case ASP:
      return ".asp";
    case BAT:
      return ".bat";
    case C:
      return ".c";
    case CPP:
      return ".cpp";
    case CS:
      return ".cs";
    case CSS:
      return ".css";
    case H:
      return ".h";
    case HTM:
      return ".htm";
    case HTML:
      return ".html";
    case INF:
      return ".inf";
    case INI:
      return ".ini";
    case JAVA:
      return ".java";
    case JS:
      return ".js";
    case JSP:
      return ".jsp";
    case LISP:
      return ".lisp";
    case LOG:
      return ".log";
    case LUA:
      return ".lua";
    case M:
      return ".m";
    case MK:
      return ".mk";
    case PAS:
      return ".pas";
    case PATCH:
      return ".patch";
    case PHP:
      return ".php";
    case PL:
      return ".pl";
    case PY:
      return ".py";
    case RB:
      return ".rb";
    case REG:
      return ".reg";
    case SH:
      return ".sh";
    case SQL:
      return ".sql";
    case ST:
      return ".st";
    case TXT:
      return ".txt";
    case VB:
      return ".vb";
    case XML:
      return ".xml";
    default:
      return ".txt";
    }
  }

  /**
   * 获取文件扩展名的文字描述
   * 
   * @return 文件扩展名的文字描述
   */
  public String getDescription() {
    switch (this) {
    case ADA:
      return "Ada语言源文件(.ada)";
    case ASP:
      return "动态服务器页面文件(.asp)";
    case BAT:
      return "批处理文件(.bat)";
    case C:
      return "C语言源文件(.c)";
    case CPP:
      return "C++语言源文件(.cpp)";
    case CS:
      return "C#语言源文件(.cs)";
    case CSS:
      return "层叠样式表文件(.css)";
    case H:
      return "C/C++语言头文件(.h)";
    case HTM:
      return "超文本标记语言文件(.htm)";
    case HTML:
      return "超文本标记语言文件(.html)";
    case INF:
      return "驱动程序信息文件(.inf)";
    case INI:
      return "初始化设置文件(.ini)";
    case JAVA:
      return "java语言源文件(.java)";
    case JS:
      return "javaScript脚本语言源文件(.js)";
    case JSP:
      return "java服务器页面文件(.jsp)";
    case LISP:
      return "列表处理语言文件(.lisp)";
    case LOG:
      return "日志文件(.log)";
    case LUA:
      return "Lua可嵌入式脚本语言文件(.lua)";
    case M:
      return "Objective-C语言源文件(.m)";
    case MK:
      return "Makefile布署文件(.mk)";
    case PAS:
      return "Pascal语言源文件(.pas)";
    case PATCH:
      return "补丁文件(.patch)";
    case PHP:
      return "超文本预处理语言文件(.php)";
    case PL:
      return "Perl脚本语言源文件(.pl)";
    case PY:
      return "Python脚本语言源文件(.py)";
    case RB:
      return "Ruby脚本语言源文件(.rb)";
    case REG:
      return "注册表操作文件(.reg)";
    case SH:
      return "Shell脚本语言源文件(.sh)";
    case SQL:
      return "结构化查询语言文件(.sql)";
    case ST:
      return "Smalltalk语言源文件(.st)";
    case TXT:
      return "普通文本文件(.txt)";
    case VB:
      return "VB语言源文件(.vb)";
    case XML:
      return "可扩展标记语言文件(.xml)";
    default:
      return "普通文本文件(.txt)";
    }
  }
}
