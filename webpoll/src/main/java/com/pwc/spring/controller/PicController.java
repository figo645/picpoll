package com.pwc.spring.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pwc.commons.FileHelper;
import com.pwc.hibernate.Pic;
import com.pwc.hibernate.dao.IPPoolDao;
import com.pwc.hibernate.dao.PicDao;

@Controller
public class PicController {
	@Autowired
	private SessionFactory sessionFactory;

	@Resource
	private IPPoolDao ipPoolDao;

	@Resource
	private PicDao picDao;

	// @Resource
	// private PicDao picDao;

	@RequestMapping(value = "/listalljson.do")
	@ResponseBody
	public Object listAllJson() {
		Map map = new HashMap();
		// C3P0ConnectionProvider
		Session session = null;
		try {
			session = sessionFactory.openSession();
			List<Pic> pics = picDao.findAll();
			map.put("apiVersion", "1.0");
			map.put("data", pics);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return map;
	}

	@RequestMapping(value = "/listsortedjson.do")
	@ResponseBody
	public Object listSortedJson() {
		Map map = new HashMap();
		List<Pic> pics = picDao.findAllSorted();
		map.put("apiVersion", "1.0");
		map.put("data", pics);
		return map;
	}

	@RequestMapping(value = "/findbyid.do")
	@ResponseBody
	public Object findById(int id) {
		Map map = new HashMap();
		Pic pic = picDao.findById(id);
		map.put("apiVersion", "1.0");
		map.put("data", pic);
		
		return map;
	}

	private String getHostAddress(HttpServletRequest request) {
		System.out.println("getRequestURI" + request.getRequestURI());
		System.out.println("getContextPath" + request.getContextPath());
		return null;
	}

	@RequestMapping(value = "/votebyidwitherror.do")
	@ResponseBody
	public Object voteByIdwithErrorCode(int id, HttpServletRequest request) {
		System.out.println("voteByIdwithErrorCode===>>");
		Session session = null;
		Map returnMap = new Hashtable();
		Map errorMap = new Hashtable();
		int errorCode = 0;
		String message = "success";
		String ip;
		try {
			ip = getIpAddr(request);
			System.out.println("mac:"+this.getMACAddress(ip));
			if (isDuplicate(request, ip, id)) {
				errorCode = 2;
				message = "You could only vote for once!";
				returnMap.put("error", errorMap);
				returnMap.put("apiVersion", "1.0");
				errorMap.put("message", message);
				errorMap.put("code", errorCode);
				return returnMap;
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Pic pic = picDao.findById(id);
		pic.setPicCount(pic.getPicCount() + 1);
		picDao.update(pic);

		returnMap.put("error", errorMap);
		returnMap.put("apiVersion", "1.0");
		errorMap.put("message", message);
		errorMap.put("code", errorCode);
		return returnMap;
	}

	

	/**
	 * 通过HttpServletRequest返回IP地址
	 * 
	 * @param request
	 *            HttpServletRequest
	 * @return ip String
	 * @throws Exception
	 */
	private String getIpAddr(HttpServletRequest request) throws Exception {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	private String getMACAddress(String ip) {  
        String str = "";  
        String macAddress = "";  
        try {  
            Process p = Runtime.getRuntime().exec("netstat -A " + ip);  
            InputStreamReader ir = new InputStreamReader(p.getInputStream());  
            LineNumberReader input = new LineNumberReader(ir);  
            for (int i = 1; i < 100; i++) {  
                str = input.readLine();  
                if (str != null) {  
                    if (str.indexOf("MAC Address") > 1) {  
                        macAddress = str.substring(  
                                str.indexOf("MAC Address") + 14, str.length());  
                        break;  
                    }  
                }  
            }  
        } catch (IOException e) {  
            e.printStackTrace(System.out);  
        }  
        return macAddress;  
    } 
	
	
	/**
	 * if true then duplicated in application or database if false then not
	 * duplicated either in db or in application
	 * 
	 * @param request
	 * @param ip
	 * @param picid
	 * @return
	 */
	private boolean isDuplicate(HttpServletRequest request, String ip, int picid) {
		// ServletContext application =
		// RequestContextUtils.getWebApplicationContext(request).getServletContext();
		boolean duplicateFlg = true;

		if (!ipPoolDao.isExistedInPool(ip)) {
			// 2. put into db pool
			ipPoolDao.putPool(ip, picid);
			duplicateFlg = false;
		} else {
			duplicateFlg = true;
		}
		
		return duplicateFlg;
	}

	@RequestMapping(method = RequestMethod.POST, value = "/uploadpicnoreturn.do")
	public String uploadPicNoReturn(@RequestParam("imageFile") MultipartFile file, Pic pic, HttpServletRequest request)
			throws Exception {
		String url = request.getSession().getServletContext().getRealPath("") + "/temp/";

		String remoteurl = request.getRequestURL() + "/temp/";
		File dir = new File(url);
		if (!dir.exists() && !dir.isDirectory()) {
			System.out.println("directory not existed");
			dir.mkdir();
		}
		getHostAddress(request);

		url = url.replace("/uploadpicnoreturn.do", "");
		String filename = UUID.randomUUID().toString() + getFileExtension(file.getOriginalFilename());
		String path = url + filename;
		remoteurl = remoteurl.replace("/uploadpicnoreturn.do", "");
		String remotepath = remoteurl + filename;
		File fileName = new File(path);
		if (!fileName.exists()) {
			try {
				fileName.createNewFile();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// set pic url
		try {
			FileHelper.save(path, file.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
		// remotepath = remotepath.replace("localhost", getIpAddr(request));
		remotepath = request.getContextPath() + "/temp/" + filename;
		pic.setPicUrl(remotepath);
		picDao.add(pic);

		return "upload";
	}

	private static String getFileExtension(String fileName) {
		String[] str = fileName.split("\\.");
		return "." + str[str.length - 1];
	}


}
