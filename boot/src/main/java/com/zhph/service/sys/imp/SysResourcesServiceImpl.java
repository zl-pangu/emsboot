package com.zhph.service.sys.imp;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhph.mapper.sys.SysResourcesMapper;
import com.zhph.model.sys.SysResources;
import com.zhph.model.sys.vo.SysResourcesUpdateVo;
import com.zhph.model.vo.ResultVo;
import com.zhph.model.vo.TreeVo;
import com.zhph.service.sys.SysResourcesService;


@Service
@Transactional(rollbackFor=Exception.class)
public class SysResourcesServiceImpl implements SysResourcesService {

	@Autowired
	private SysResourcesMapper resourcesMapper;
	 
	
	/**
	 * 描述：查询树
	 */
	public TreeVo getZtreeRoot(Long id,int check_state, boolean checked) {
		SysResources resources = resourcesMapper.findById(id);
		TreeVo tree = wrapper(resources, resources.getResourcesChildSize(), check_state, checked);
		
		List<SysResources> child = resourcesMapper.findByParentId(id);
		if (child != null && child.size() > 0) {
			for (SysResources r : child) {
				tree.getChildren().add(wrapper(r, r.getResourcesChildSize(), check_state, checked));
			}
		}
		
		
 		return tree;
	}
	
	
	private TreeVo wrapper(SysResources r, int childSize, int check_state, boolean checked) {
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("resourcesSortId", r.getResourcesSortId());
		attributes.put("check_state", check_state);
		return new TreeVo(r.getResourcesId().longValue(), r.getResourcesName(), childSize, attributes, checked);
	}
	
	
	/**
	 * 
	 * @Title findTreeBySysType
	 * @Description TODO 根据所属系统查询 树
	 * @param @param resourcesSysType
	 * @param @return    参数
	 * @return TreeVo    返回类型
	 *
	 */
	public TreeVo findTreeBySysType(int resourcesSysType){
		
		TreeVo treeVo = new TreeVo();
		
		//获取资源根节点
		SysResources resources = resourcesMapper.findById(1l);
		
		List<SysResources> resourcesList = resourcesMapper.findAllByResourcesSysType(resourcesSysType);
		
		treeVo.setId(resources.getResourcesId().toString());
		treeVo.setText(resources.getResourcesName());
		treeVo.setData(resources.getResourcesUrl());
		
		createChileRule(treeVo, resources, resourcesList);
		
		return treeVo;
	}
	
	/**
	 * 
	 * @Title findById
	 * @Description TODO 根据主键查询资源
	 * @param  id
	 * @param 
	 * @return Resources    返回类型
	 *
	 */
	public SysResources findById(Long id){
		
		return resourcesMapper.findById(id);
	}
	
	
	/**
	 * 
	 * @Title insert
	 * @Description 新增资源
	 * @param @param resources    参数
	 * @return Resources    返回类型
	 *
	 *
	 */
	public SysResources insert(SysResources resources, Long userId){
		
		resources.setResourcesIsPopedoms(1); //是否是权限菜单 应该需要从页面获取

		//查询父节点
		SysResources parent = resourcesMapper.findById(resources.getResourcesParentId());
		resources.setResourcesLevelNumber(parent.getResourcesLevelNumber()+1);
		resources.setResourcesChildSize(0);
		resources.setResourcesSortId(parent.getResourcesChildSize()+1);
		resources.setCreateTime(new Date());
		resources.setCreatorId(userId);
		resourcesMapper.insert(resources);
		
		
		//插入后修改full_id ,full_name
		resources.setResourcesFullId(parent.getResourcesFullId()+ resources.getResourcesId() + ".");
		resources.setResourcesFullName(parent.getResourcesFullName()+ resources.getResourcesName() + ".");
		resourcesMapper.update(resources);
		

		
		//更新parent信息
		parent.setResourcesChildSize(parent.getResourcesChildSize()+1);
		resourcesMapper.update(parent);
		return resources;
		
	}
	
	
	/**
	 * 描述：更新实体，并且更新排序信息
	 */
	public ResultVo update(SysResourcesUpdateVo resourcesUpdateDomain,Long userId){
		ResultVo resultVo = new ResultVo();

		SysResources newSysResources = resourcesUpdateDomain.getResources();
		
		SysResources sysResources = resourcesMapper.findById(newSysResources.getResourcesId());

		
		//如果资源要启用 要判断父级菜单是否是启用状态 否则不能 启用
		if(newSysResources.getResourcesIsUse().equals(SysResources.RESOURCES_IS_USE_YES)){
			SysResources parent = resourcesMapper.findById(newSysResources.getResourcesParentId());
			
			if(parent.getResourcesIsUse().equals(SysResources.RESOURCES_IS_USE_NO)){
				resultVo.setStatus(0);
				resultVo.setInfo("修改资源信息失败！父级菜单已经被停用，此节点不能启用！");
				resultVo.setData(sysResources);
				
				return resultVo;
			}
		}
		
		
		// 修改时 如果是 子节点则需要判断其 有没有子元素。如果有子元素不能进行修改。
		if(newSysResources.getResourcesIsEnd().equals(SysResources.RESOURCES_IS_END_YES)){
			if(sysResources.getResourcesChildSize()>0){ //存在子元素
				resultVo.setStatus(0);
				resultVo.setInfo("修改资源信息失败！该资源下面存在子节点，不能变成末子节点");
				resultVo.setData(sysResources);
				return resultVo;
			 }
		} 
		
		updateEntity(resourcesUpdateDomain,userId);
		resultVo.setStatus(1);
		resultVo.setInfo("修改资源信息成功！");
		resultVo.setData(resourcesUpdateDomain.getResources());
		
		return resultVo;
	}
	
	private void updateEntity(SysResourcesUpdateVo resourcesUpdateDomain,Long userId){
		
		SysResources resources = resourcesUpdateDomain.getResources();
		resources.setUpdateId(userId);
		resources.setUpdateTime(new Date());
		
		resourcesMapper.update(resources);
		
		List<SysResources> sortable =resourcesUpdateDomain.getResourcesSortable();
		//更新排序信息
		if(sortable!=null&&sortable.size()>0)
		{
			for(int i=0,length=sortable.size();i<length;i++)
			{
				resourcesMapper.update(sortable.get(i));
			}
		}
		
		List<Integer> childrenIds=resourcesMapper.findAllChildrenByResourcesId("%."+resources.getResourcesId()+".%");
		
		resourcesMapper.updateResourcesIsUse(resources.getResourcesIsUse(), childrenIds);
		
	}
	
	/**
	 * 
	 * @Title delete
	 * @Description TODO 删除资源
	 * @param @param resorucesId
	 * @param     参数
	 * @return ResultVo    返回类型
	 *
	 */
	public ResultVo delete(Long resorucesId){
		ResultVo resultVo = new ResultVo();
		//查询有没有子级资源
		List<SysResources> childrenList = resourcesMapper.findByParentId(resorucesId);
		
		//如果存在子级资源
		if(childrenList.size()>0){
			resultVo.setInfo("还存在子级资源，不能进行删除");
			resultVo.setStatus(0);
		}else{
			
			if (resourcesMapper.findRoleCountByResourcesId(resorucesId)>0) {
				resultVo.setInfo("该资源已经和角色关联，不能进行删除");
				resultVo.setStatus(0);
			}else{
			
				SysResources rr =resourcesMapper.findById(resorucesId);
				
				//更新父节点 的childsize 个数
				SysResources parent = resourcesMapper.findById(rr.getResourcesParentId());
				parent.setResourcesChildSize(parent.getResourcesChildSize()-1);
				resourcesMapper.update(parent);
				
				//删除资源
				resourcesMapper.deleteById(resorucesId);
				resultVo.setInfo("资源删除成功！");
				resultVo.setStatus(1);
			}
		}
		
		return resultVo;
	}
	
	
	/**
	 * 
	 * @Title findMenuByResourcesId
	 * @Description TODO 获取权限菜单树
	 * @param @param resourcesIds
	 * @param @return    参数
	 * @return TreeVo    返回类型
	 *
	 */
	public TreeVo findMenuByResourcesId(List<Long> resourcesIds){
			
		TreeVo treeVo = new TreeVo();
		
		//获取资源根节点
		SysResources resources = resourcesMapper.findById(1l);
		
		List<SysResources> resourcesList = resourcesMapper.findMenuByResourcesId(resourcesIds);
		
		treeVo.setId(resources.getResourcesId().toString());
		treeVo.setText(resources.getResourcesName());
		treeVo.setData(resources.getResourcesUrl());
		
		createChileRule(treeVo, resources, resourcesList);
		
		return treeVo;
	}
	
	
	private void createChileRule(TreeVo treeVo,SysResources resources,List<SysResources> allResourcesList){
		if (isHaveChild(allResourcesList, resources)) {
			List<SysResources> child = findChildRule(allResourcesList,resources);
			
			List<TreeVo> childTreeList=new ArrayList<TreeVo>();
			for(SysResources childResource:child){
				TreeVo childTree = new TreeVo();
				childTree.setId(childResource.getResourcesId().toString());
				childTree.setText(childResource.getResourcesName());
				childTree.setData(childResource.getResourcesUrl());

				
				if (isHaveChild(allResourcesList, childResource)) {
					createChileRule(childTree,childResource,allResourcesList);
				}
				
				childTreeList.add(childTree);
			}
			
			treeVo.setChildren(childTreeList);
		}
	}
	
	
	private boolean isHaveChild(List<SysResources> allList,SysResources resources){
		boolean ishave=false;
		for (SysResources item : allList) {
			if (item.getResourcesParentId()!=null&&item.getResourcesParentId().intValue()==resources.getResourcesId()) {
				ishave=true;
				break;
			}
		}
		return ishave;
	}
	
	
	private  List<SysResources> findChildRule(List<SysResources> allList,SysResources resources){
		List<SysResources> child =new ArrayList<SysResources>();
		for (SysResources item : allList) {
			if (item.getResourcesParentId()!=null&&item.getResourcesParentId().intValue()==resources.getResourcesId()) {
				child.add(item);
			}
		}
		return child;
	}
	
	
	/**
	 * 
	 * @Title getResourcesIdByRoleIds
	 * @Description TODO 根据用户角色查询该用户所有的权限URL
 	 * @param  roleIds
	 * @return List<SysResources>    返回类型
	 *
	 */
	public List<SysResources> getResourcesByRoleIds(List<Integer> roleIds){
		
		List<SysResources> resourcesList = resourcesMapper.findResourcesByRoleId(roleIds);
		
		return resourcesList;
	}


	@Override
	public List<String> findAllURI() {
 		return resourcesMapper.findAllURI();
	}
	
	
	@Override
	public Integer checkNameRepeat(String resourcesName) {
		// TODO Auto-generated method stub
		return this.resourcesMapper.checkNameRepeat(resourcesName);
	}
	
	

	
}
