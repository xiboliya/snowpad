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

package com.xiboliya.snowpad.common;

/**
 * 用于标识文件扩展名类型的枚举
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
   * ActionScript脚本语言源文件
   */
  AS,
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
   * D语言源文件
   */
  D,
  /**
   * Dart语言源文件
   */
  DART,
  /**
   * Document Type Definition(文档类型定义)
   */
  DTD,
  /**
   * Go语言源文件
   */
  GO,
  /**
   * Groovy语言源文件
   */
  GROOVY,
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
   * JavaScript Object Notation(JavaScript对象表示法)
   */
  JSON,
  /**
   * Java Server Page(java服务器页面)
   */
  JSP,
  /**
   * Kotlin语言源文件
   */
  KT,
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
   * properties配置文件
   */
  PROPERTIES,
  /**
   * Python脚本语言源文件
   */
  PY,
  /**
   * R语言源文件
   */
  R,
  /**
   * Ruby脚本语言源文件
   */
  RB,
  /**
   * 注册表操作文件
   */
  REG,
  /**
   * Rust语言源文件
   */
  RUST,
  /**
   * Scala语言源文件
   */
  SCALA,
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
   * Swift语言源文件
   */
  SWIFT,
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
    case AS:
      return ".as";
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
    case D:
      return ".d";
    case DART:
      return ".dart";
    case DTD:
      return ".dtd";
    case GO:
      return ".go";
    case GROOVY:
      return ".groovy";
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
    case JSON:
      return ".json";
    case JSP:
      return ".jsp";
    case KT:
      return ".kt";
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
    case PROPERTIES:
      return ".properties";
    case PY:
      return ".py";
    case R:
      return ".R";
    case RB:
      return ".rb";
    case REG:
      return ".reg";
    case RUST:
      return ".rs";
    case SCALA:
      return ".scala";
    case SH:
      return ".sh";
    case SQL:
      return ".sql";
    case ST:
      return ".st";
    case SWIFT:
      return ".swift";
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
    case AS:
      return "ActionScript脚本语言源文件(.as)";
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
    case D:
      return "D语言源文件(.d)";
    case DART:
      return "Dart语言源文件(.dart)";
    case DTD:
      return "文档类型定义文件(.dtd)";
    case GO:
      return "Go语言源文件(.go)";
    case GROOVY:
      return "Groovy语言源文件(.groovy)";
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
    case JSON:
      return "javaScript对象表示法文件(.json)";
    case JSP:
      return "java服务器页面文件(.jsp)";
    case KT:
      return "Kotlin语言源文件(.kt)";
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
    case PROPERTIES:
      return "properties配置文件(.properties)";
    case PY:
      return "Python脚本语言源文件(.py)";
    case R:
      return "R语言源文件(.R)";
    case RB:
      return "Ruby脚本语言源文件(.rb)";
    case REG:
      return "注册表操作文件(.reg)";
    case RUST:
      return "Rust语言源文件(.rs)";
    case SCALA:
      return "Scala语言源文件(.scala)";
    case SH:
      return "Shell脚本语言源文件(.sh)";
    case SQL:
      return "结构化查询语言文件(.sql)";
    case ST:
      return "Smalltalk语言源文件(.st)";
    case SWIFT:
      return "Swift语言源文件(.swift)";
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

  /**
   * 获取当前文件类型的单行注释字符串
   * 
   * @return 单行注释字符串
   */
  public String getCommentForLine() {
    switch (this) {
    case ADA:
    case LUA:
    case SQL:
      return "-- ";
    case AS:
    case C:
    case CPP:
    case CS:
    case D:
    case DART:
    case GO:
    case GROOVY:
    case H:
    case JAVA:
    case JS:
    case JSON:
    case JSP:
    case KT:
    case M:
    case PAS:
    case PHP:
    case RUST:
    case SCALA:
    case SWIFT:
      return "// ";
    case INF:
    case INI:
    case LISP:
    case REG:
      return "; ";
    case MK:
    case PL:
    case PROPERTIES:
    case PY:
    case R:
    case RB:
    case SH:
      return "# ";
    case BAT:
      return ":: ";
    case ASP:
    case VB:
      return "' ";
    default:
      return null;
    }
  }

  /**
   * 获取当前文件类型的区块注释首部字符串
   * 
   * @return 区块注释首部字符串
   */
  public String getCommentForBlockBegin() {
    switch (this) {
    case AS:
    case C:
    case CPP:
    case CS:
    case CSS:
    case D:
    case DART:
    case GO:
    case GROOVY:
    case H:
    case JAVA:
    case JS:
    case JSON:
    case JSP:
    case KT:
    case M:
    case PHP:
    case RUST:
    case SCALA:
    case SQL:
    case SWIFT:
      return "/* ";
    case DTD:
    case HTM:
    case HTML:
    case XML:
      return "<!-- ";
    case LUA:
      return "--[[ ";
    case PAS:
      return "{ ";
    case PL:
      return "=pod\n";
    case PY:
      return "''' ";
    case RB:
      return "==begin\n";
    case SH:
      return ":<<'COMMENT'\n";
    case ST:
      return "\" ";
    default:
      return null;
    }
  }

  /**
   * 获取当前文件类型的区块注释尾部字符串
   * 
   * @return 区块注释尾部字符串
   */
  public String getCommentForBlockEnd() {
    switch (this) {
    case AS:
    case C:
    case CPP:
    case CS:
    case CSS:
    case D:
    case DART:
    case GO:
    case GROOVY:
    case H:
    case JAVA:
    case JS:
    case JSON:
    case JSP:
    case KT:
    case M:
    case PHP:
    case RUST:
    case SCALA:
    case SQL:
    case SWIFT:
      return " */";
    case DTD:
    case HTM:
    case HTML:
    case XML:
      return " -->";
    case LUA:
      return " --]]";
    case PAS:
      return "{ ";
    case PL:
      return "\n=cut";
    case PY:
      return " '''";
    case RB:
      return "\n==end";
    case SH:
      return "\nCOMMENT";
    case ST:
      return " \"";
    default:
      return null;
    }
  }

}
