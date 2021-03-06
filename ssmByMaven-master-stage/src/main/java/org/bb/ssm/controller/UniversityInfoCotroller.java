package org.bb.ssm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bb.ssm.model.University;
import org.bb.ssm.service.UniversityInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value="/university")
public class UniversityInfoCotroller {
	
	@Autowired
	private UniversityInfoService universityInfoService;
	
	/**
	 * 用户列表页
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/index")
	public String index(Map<String, Object> map){
		//List<university> universityList = universityInfoService.findAll();
		//map.put("ALLuniversity", universityList);
		return "bui/acdemic/universityList";
	}
	
	/**
	 * 得到所有用户信息
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/getAllUniversity",method=RequestMethod.POST)
	@ResponseBody
	public String getAlluniversity(Object pageinfo,Map<String, Object> map){
		System.out.println(pageinfo);
		List<University> universityList = universityInfoService.findAll();
		
		HashMap<String,Object > tuniversity = new HashMap<String,Object >();
		
		tuniversity.put("rows", universityList);
		tuniversity.put("results", universityList.size());
		
		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsondata = mapper.writeValueAsString(tuniversity);

			//System.out.println(jsondata);
			
			return jsondata;
			
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 通过handler前往添加用户页面
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/adduniversity",method= RequestMethod.GET)
	public String adduniversity(Map<String, Object> map){
		//因为页面使用spring的form标签，其中属性modelAttribute需要存在bean 要不会报错
		map.put("command", new University());
		return "adduniversity";
	}
	
	/**
	 * 添加用户操作
	 * @param universityinfo
	 * @return
	 */
	@RequestMapping(value="/adduniversity",method=RequestMethod.POST)
	public String save(University universityinfo){
		int result = universityInfoService.insert(universityinfo);
		System.out.println("添加用户的操作结果为："+result);
		return "redirect:/university/getAlluniversity";
	}
	/**
	 * 删除用户操作
	 * @param id
	 * @return
	 */
	@RequestMapping(value="/delete/{id}",method=RequestMethod.DELETE)
	public String delete(@PathVariable(value="id") int id){
		int result = universityInfoService.deleteByPrimaryKey(id);
		System.out.println("删除用户的操作结果为："+result+"传递进来的id为："+id);
		return "redirect:/university/getAlluniversity";
	}
	/**
	 * 更新前先根据id找到用户信息，回显到页面上
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping(value="/detail/{id}",method=RequestMethod.GET)
	public String input(@PathVariable(value="id") Integer id,Map<String, Object> map){
		map.put("command", universityInfoService.selectByPrimaryKey(id));
		return "adduniversity";
	}
	
	@ModelAttribute
	public void getuniversityInfo(@RequestParam(value="universityId",required=false) Integer id
			,Map<String, Object> map){
		System.out.println("每个controller 方法都会先调用我哦");
		if(id != null){
			System.out.println("update 操作");
			map.put("universityInfo", universityInfoService.selectByPrimaryKey(id));
		}
		System.out.println("insert  操作");
	}
	
	@RequestMapping(value="/adduniversity",method=RequestMethod.PUT)
	public String update(University universityinfo){
		universityInfoService.updateByPrimaryKey(universityinfo);
		return "redirect:/university/getAlluniversity";
	}
}
