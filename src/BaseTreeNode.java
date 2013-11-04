package com.xiboliya.snowpad;

import javax.swing.tree.DefaultMutableTreeNode;

/**
 * 应用于JTree中的自定义节点
 * 
 * @author 冰原
 * 
 */
public class BaseTreeNode extends DefaultMutableTreeNode {
  private static final long serialVersionUID = 1L;
  private String content = null;

  public BaseTreeNode(String name) {
    super(name);
  }

  public BaseTreeNode(String name, String content) {
    super(name);
    this.setContent(content);
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
