package com.zhph.model.sys.vo;

import java.util.List;

import com.zhph.model.sys.SysResources;


public class SysResourcesUpdateVo {
	
		//要修改的资源对象
		private SysResources resources;
		//要修改的资源排序对象
		private List<SysResources> resourcesSortable;

		public SysResources getResources() {
			return resources;
		}

		public void setResources(SysResources resources) {
			this.resources = resources;
		}

		public List<SysResources> getResourcesSortable() {
			return resourcesSortable;
		}

		public void setResourcesSortable(List<SysResources> resourcesSortable) {
			this.resourcesSortable = resourcesSortable;
		}
}
