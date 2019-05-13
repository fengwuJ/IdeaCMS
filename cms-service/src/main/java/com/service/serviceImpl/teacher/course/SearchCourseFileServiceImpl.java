package com.service.serviceImpl.teacher.course;

import java.io.File;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.entity.course.CourseFile;
import com.mapper.teacher.course.TeachCourseFileMapper;
import com.service.serviceInterface.teacher.course.SearchCourseFileServiceInf;
import com.utils.FileUtils;

@Service("scfsImpl")
public class SearchCourseFileServiceImpl implements SearchCourseFileServiceInf{

	@Resource
	private TeachCourseFileMapper tcfMapper;
	
	@Override
	public CourseFile searchCourseFile(String tid, String cid) {
		// TODO Auto-generated method stub
		CourseFile courseFile = tcfMapper.findCourseFile(tid,cid);
		courseFile.setCfileUrl("");
		return courseFile;
	}

	//缺少文件重名检测,数据库状态写入
	@Override
	public String uploadCourseFile(String tid, String cid, MultipartFile file){
		// TODO Auto-generated method stub
		String status = null;
		String url = "cfile"+File.separator+cid+File.separator+tid;
		String path = FileUtils.createDir(url);
		boolean flag = FileUtils.writeIntoFile(path, file);
		tcfMapper.updateCourseFileStatus(path,tid,cid,"已上传");
		if (flag == true) {
			status = "上传成功";
		}else {
			status = "上传失败，请重新上传";
		}
		return status;
	}

	/**
	 * @param tid 教师编号
	 * @param cid 教师的课程编号
	 * @Param fileName 前端传来的文件名
	 * @return OK 可以上传，else 不可以上传
	 */
	@Override
	public String checkCFileExsist(String tid, String cid, String fileName) {
		// TODO Auto-generated method stub
		String flag = "OK";
		String cFUrl = tcfMapper.findCFileUrl(tid,cid);
		if (cFUrl != null) {
			File dirFile = new File(cFUrl);
			File[] fs = dirFile.listFiles();
			if(fs != null){
				for (File f : fs) {
					//若是文件，则将其进行对比
					if (!f.isDirectory()) {
						if (f.getName().equals(fileName)) {
							flag = "no";
						}
					}
				}
			}
		}else {
			flag = "OK";
		}
		return flag;
	}
	
}
