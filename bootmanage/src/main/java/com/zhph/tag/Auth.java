package com.zhph.tag;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zhph.util.StringUtil;
import com.zhph.web.WebContext;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

@Component
public class Auth implements TemplateDirectiveModel {

	@Autowired
	private HttpServletRequest req;

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void execute(Environment env, Map map, TemplateModel[] templateModels, TemplateDirectiveBody body) throws TemplateException, IOException {
		if (req == null)
			req = WebContext.getRequest();
		if (req == null)
			throw new TemplateException("Http请求注入失败！", env);

		HttpSession session = req.getSession();

		// 所有的权限
		List<String> allResources = (List<String>) session.getAttribute("allResource");
		// 用户具有的权限
		List<String> resourcesUrlList = (List<String>) session.getAttribute("resourcesUrl");

		// 标签的属性
		String url = map.get("url") == null ? null : String.valueOf(map.get("url"));
		String var = map.get("var") == null ? null : String.valueOf(map.get("var"));
		if (StringUtil.isEmpty(url))
			throw new TemplateException("auth标签的url属性必须！", env);

		if (!allResources.contains(url)) { // 如果传入的权限 不再数据库中，则显示出来
			if (body != null){
				body.render(env.getOut());
				if (var != null){
					req.setAttribute(var, "true");
				}
			}
		} else {
			// 如果传入的权限存在数据库中， 则验证是否具有权限，
			if (resourcesUrlList.contains(url)) {
				if (body != null){
					body.render(env.getOut());
					if (var != null){
						req.setAttribute(var, "true");
					}
				}
			}
		}
	}
}
