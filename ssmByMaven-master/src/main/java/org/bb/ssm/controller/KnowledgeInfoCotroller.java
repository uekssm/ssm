package org.bb.ssm.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.bb.ssm.model.Knowledge;
import org.bb.ssm.service.KnowledgeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = "/knowledge")
public class KnowledgeInfoCotroller {

	@Autowired
	private KnowledgeInfoService knowledgeInfoService;

	/**
	 * 用户列表页
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/index")
	public String index(Map<String, Object> map) {
		// List<knowledge> knowledgeList = knowledgeInfoService.findAll();
		// map.put("ALLknowledge", knowledgeList);
		return "bui/acdemic/knowledgeList";
	}
	
	/**
	 * 查看视频
	 * 
	 * @param map
	 * @return
	 */
	/*@RequestMapping(value = "/index")
	public String showVideo(int knowledgeId) {
		
		
		return "bui/acdemic/showVideo";
	}*/
	

	/**
	 * 得到所有用户信息
	 * 
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/getAllKnowledge", method = RequestMethod.GET)
	@ResponseBody
	public String getAllKnowledge(Object pageinfo, Map<String, Object> map) {
		System.out.println(pageinfo);
		List<Knowledge> knowledgeList = knowledgeInfoService.findAll();

		HashMap<String, Object> tknowledge = new HashMap<String, Object>();

		tknowledge.put("rows", knowledgeList);
		tknowledge.put("results", knowledgeList.size());

		ObjectMapper mapper = new ObjectMapper();
		try {
			String jsondata = mapper.writeValueAsString(tknowledge);

			// System.out.println(jsondata);

			return jsondata;

		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return null;
	}


	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
	public String upload(HttpServletRequest request) throws IOException {
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
		String targetFile = null;
		String fileName = null;
		for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
			MultipartFile myfile = entity.getValue();
			
			String uploadPath = request.getSession().getServletContext().getRealPath("/resourse/upload");
			fileName = myfile.getOriginalFilename();
			targetFile = uploadPath + "/" + fileName;
			
			
			byte[] bs = myfile.getBytes();
			File file = new File(targetFile);
			FileOutputStream fos = new FileOutputStream(file);
			fos.write(bs);
			fos.close();
		}
		
		return "{\"url\" : \"" + fileName + "\"}";
	}

	/**
	 * 添加用户操作
	 * 
	 * @param knowledgeinfo
	 * @return
	 */
	@RequestMapping(value = "/addKnowledge", method = RequestMethod.GET)
	public String addKnowledge(Knowledge knowledgeinfo) {
		System.out.println("xxxxxxxxxxxxxxxxxxxxxxxxxx"+knowledgeinfo);
		int result = knowledgeInfoService.insert(knowledgeinfo);
		
		System.out.println("添加知识点的操作结果为：" + result);
		
		return "redirect:/knowledge/getAllKnowledge";
	}

	/**
	 * 删除用户操作
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
	public String delete(@PathVariable(value = "id") int id) {
		int result = knowledgeInfoService.deleteByPrimaryKey(id);
		System.out.println("删除用户的操作结果为：" + result + "传递进来的id为：" + id);
		return "redirect:/knowledge/getAllKnowledge";
	}

	/**
	 * 更新前先根据id找到用户信息，回显到页面上
	 * 
	 * @param id
	 * @param map
	 * @return
	 */
	@RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
	public String input(@PathVariable(value = "id") Integer id,
			Map<String, Object> map) {
		map.put("command", knowledgeInfoService.selectByPrimaryKey(id));
		return "addknowledge";
	}

	@ModelAttribute
	public void getknowledgeInfo(
			@RequestParam(value = "knowledgeId", required = false) Integer id,
			Map<String, Object> map) {
		System.out.println("每个controller 方法都会先调用我哦");
		if (id != null) {
			System.out.println("update 操作");
			map.put("knowledgeInfo",
					knowledgeInfoService.selectByPrimaryKey(id));
		}
		System.out.println("insert  操作");
	}

	@RequestMapping(value = "/addknowledge", method = RequestMethod.PUT)
	public String update(Knowledge knowledgeinfo) {
		knowledgeInfoService.updateByPrimaryKey(knowledgeinfo);
		return "redirect:/knowledge/getAllKnowledge";
	}
}
