package com.controller.teacher.course;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import com.entity.course.CourseWareName;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.entity.course.CourseFile;
import com.mapper.teacher.course.TeachCourseFileMapper;
import com.service.serviceInterface.teacher.course.SearchCourseFileServiceInf;
import com.utils.FileUtils;

@Controller
@RequestMapping("/teacherview/mCourse")
public class TeachCourseController {
	@Resource(name="scfsImpl")
	private SearchCourseFileServiceInf scfsInf;
	
	@Resource
	private TeachCourseFileMapper tcfMapper;
	
	@RequestMapping("/searchCourseFile")
	@ResponseBody
	public Map<String, Object> searchCourseFile(String tid,String cid){
		Map<String, Object> map = new HashMap<String, Object>();
		CourseFile courseFile = scfsInf.searchCourseFile(tid,cid);
		map.put("courseFile", courseFile);
		return map;
	}
	
	@RequestMapping("/uploadCourseFile")
	@ResponseBody
	public String uploadCourseFile(@RequestParam(value = "file", required = false)MultipartFile file,String cid,String tid){
		String result = scfsInf.uploadCourseFile(tid,cid,file);
		return result;
	}
	
	@RequestMapping("/checkCFileExsist")
	@ResponseBody
	public String checkCFileExsist(String tid,String cid,String fileName) {
		String result = scfsInf.checkCFileExsist(tid,cid,fileName);
		return result;
	}

	@RequestMapping("/getCourseWareList")
	@ResponseBody
	public Map<String,Object> getCourseWareList(String tid,String cid,int offset,int pageSize){
		Map<String,Object> map = new HashMap<>();
		List<CourseWareName> list = new ArrayList<>();
		String url = tcfMapper.findCFileUrl(tid, cid);
		File file = new File(url);
		File[] fs = file.listFiles();
		map.put("total",fs.length);
		if (offset*pageSize < fs.length){
			int count = 0;
			for (int i = offset*pageSize; count < pageSize; count++,i++){
				if (!fs[i].isDirectory()){
					CourseWareName cwn = new CourseWareName();
					cwn.setCourseWareName(fs[i].getName());
					list.add(cwn);
				}
			}
			map.put("rows", list);
		}
		return map;
	}

	@RequestMapping("/downloadCourseFile")
	@ResponseBody
	public void downloadCourseFile(String courseWareName,String tid,String cid,HttpServletResponse response){
		String url = tcfMapper.findCFileUrl(tid, cid);
		url += File.separator+courseWareName;
		FileUtils.downloadFile(url,response);
	}
	
}
