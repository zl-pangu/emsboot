package com.zhph.model.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TreeVo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5401772599251813928L;
	public static String TREE_NODE_STATE_DEFAULT = "open";
	public static String TREE_NODE_STATE_CLOSED = "closed";

	private String id;
	private String text;
	private String state = TreeVo.TREE_NODE_STATE_DEFAULT;
	private boolean checked;
	private Object data;

	private String iconCls;

	public TreeVo(Object id, String text, int childSize, Map<String, Object> attributes, boolean checked,
			String iconCls) {
		this.id = String.valueOf(id);
		this.text = text;
		this.childSize = childSize;
		this.attributes = attributes;
		this.checked = checked;
		this.iconCls = iconCls;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public TreeVo() {
	}

	public TreeVo(String rootName) {
		this.id = "-1";
		this.text = rootName;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	private int childSize;
	private Map<String, Object> attributes = new HashMap<String, Object>();
	private List<TreeVo> children = new ArrayList<TreeVo>();

	@Override
	public String toString() {
		return "Tree [id=" + id + ", text=" + text + ", state=" + state + ", checked=" + checked + ", childSize="
				+ childSize + ", children=" + children + "]";
	}

	public TreeVo(String id, String text, int childSize) {
		this.id = id;
		this.text = text;
		this.childSize = childSize;
	}

	public TreeVo(Long id, String text, int childSize) {
		this.id = String.valueOf(id);
		this.text = text;
		this.childSize = childSize;
	}

	public TreeVo(Long id, String text, int childSize, Map<String, Object> attributes) {
		this.id = String.valueOf(id);
		this.text = text;
		this.childSize = childSize;
		this.attributes = attributes;
	}

	public TreeVo(Long id, String text, int childSize, Map<String, Object> attributes, boolean checked) {
		this.id = String.valueOf(id);
		this.text = text;
		this.childSize = childSize;
		this.attributes = attributes;
		this.checked = checked;
	}

	public TreeVo(String id, String text, String state, boolean checked, Map<String, Object> attributes) {
		this.id = id;
		this.text = text;
		this.state = state;
		this.checked = checked;
		this.attributes = attributes;
	}

	public TreeVo(String id, String text, String state) {
		this.id = id;
		this.text = text;
		this.state = state;
	}

	public TreeVo(String id, String text, String state, Object data) {
		this.id = id;
		this.text = text;
		this.state = state;
		this.data = data;
	}

	public TreeVo(String id, String text, String state, boolean checkState) {
		this.id = id;
		this.text = text;
		this.state = state;
		this.checked = checkState;
	}

	public TreeVo(Long id, String text, String state) {
		this.id = String.valueOf(id);
		this.text = text;
		this.state = state;
	}

	public TreeVo(String id, String text, int size, Map<String, Object> attributes) {
		this.id = id;
		this.text = text;
		this.childSize = size;
		this.attributes = attributes;
	}

	public int getChildSize() {
		return childSize;
	}

	public void setChildSize(int childSize) {
		this.childSize = childSize;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public List<TreeVo> getChildren() {
		return children;
	}

	public void setChildren(List<TreeVo> children) {
		this.children = children;
	}

}