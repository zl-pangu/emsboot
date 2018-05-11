/* 
 * DaysNode.java created on 2018-01-04 下午 15:59:09 by roilatD
 */ 
package com.zhph.model.cf;

 * javadoc for com.zhph.daysNode.entity.DaysNode
 * @author: roilatD
 * @since: 2018-01-04 下午 15:59:10
 */
public class DaysNode {

	 * null
	 */
	private Long nodeId;

	/**
	 * null
	 */
	private String treeId;

	/**
	 * null
	 */
	private String nodeNo;

	/**
	 * null
	 */
	private String nodeValue;

	/**
	 * null
	 */
	private Long keyId;


	public Long getNodeId() {
		return nodeId;
	}

		this.nodeId = nodeId;
	}

		return treeId;
	}

		this.treeId = treeId;
	}

		return nodeNo;
	}

		this.nodeNo = nodeNo;
	}

		return nodeValue;
	}

		this.nodeValue = nodeValue;
	}

		return keyId;
	}

		this.keyId = keyId;
	}

	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nodeId == null) ? 0 : nodeId.hashCode());
		result = prime * result + ((treeId == null) ? 0 : treeId.hashCode());
		result = prime * result + ((nodeNo == null) ? 0 : nodeNo.hashCode());
		result = prime * result + ((nodeValue == null) ? 0 : nodeValue.hashCode());
		result = prime * result + ((keyId == null) ? 0 : keyId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DaysNode other = (DaysNode) obj;
		if (nodeId == null) {
			if (other.nodeId != null)
				return false;
		} else if (!nodeId.equals(other.nodeId))
			return false;
		if (treeId == null) {
			if (other.treeId != null)
				return false;
		} else if (!treeId.equals(other.treeId))
			return false;
		if (nodeNo == null) {
			if (other.nodeNo != null)
				return false;
		} else if (!nodeNo.equals(other.nodeNo))
			return false;
		if (nodeValue == null) {
			if (other.nodeValue != null)
				return false;
		} else if (!nodeValue.equals(other.nodeValue))
			return false;
		if (keyId == null) {
			if (other.keyId != null)
				return false;
		} else if (!keyId.equals(other.keyId))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DaysNode [ nodeId=" + nodeId + ", treeId=" + treeId + ", nodeNo=" + nodeNo + ", nodeValue=" + nodeValue + ", keyId=" + keyId + "]";
	}
}