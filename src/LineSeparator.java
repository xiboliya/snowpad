package com.xiboliya.snowpad;

/**
 * 用于标识换行符格式的枚举
 * 
 * @author chen
 * 
 */
public enum LineSeparator {
  /**
   * 当前操作系统的行分隔符
   */
  DEFAULT,
  /**
   * Unix/Linux操作系统的行分隔符
   */
  UNIX,
  /**
   * Macintosh操作系统的行分隔符
   */
  MACINTOSH,
  /**
   * Windows操作系统的行分隔符
   */
  WINDOWS;
  /**
   * 重写父类的方法
   */
  public String toString() {
    switch (this) {
    case UNIX:
      return "\n";
    case MACINTOSH:
      return "\r";
    case WINDOWS:
      return "\r\n";
    case DEFAULT:
    default:
      return Util.LINE_SEPARATOR;
    }
  }

  /**
   * 获取换行符格式的名称
   * 
   * @return 换行符格式的名称
   */
  public String getName() {
    switch (this) {
    case UNIX:
      return "Unix/Linux";
    case MACINTOSH:
      return "Macintosh";
    case WINDOWS:
      return "Windows";
    default:
      if (Util.LINE_SEPARATOR.equals("\n")) {
        return "Unix/Linux";
      } else if (Util.LINE_SEPARATOR.equals("\r")) {
        return "Macintosh";
      } else {
        return "Windows";
      }
    }
  }
}
