package com.pwc.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.pwc.commons.HibernateDao;
import com.pwc.hibernate.Pic;

public class UploadFileLocal {
	static final int BUFFER = 2048;
	private static HibernateDao dao = new HibernateDao();
	public static void main(String[] args) {
		String fileName = "C:\\Users\\wxu046\\Downloads\\BatchUpload\\documents-export-2015-11-03.zip";
		String filePath = "C:\\Users\\wxu046\\Downloads\\BatchUpload\\";
		// TODO Auto-generated method stub
		// 1. unzip
		// UploadFileLocal.unzipPictures(fileName, filePath);

		// 2. list up unzipped files and folders
		Map picMap = UploadFileLocal.listUpFoldersAndFiles(filePath);
		Set keySet = picMap.keySet();

		Object[] objs = keySet.toArray();
		// Iterator iterator = keySet.iterator();
		for (int j = 0; j < objs.length; j++) {
			String folderUserName = String.valueOf(objs[j]);
			System.out.println(folderUserName + ">>>>>>>>>");

			List list = (List) picMap.get(objs[j]);
			for (int k = 0; k < list.size(); k++) {
				File file = (File)list.get(k);
				System.out.println(file.getName());
				// 2.1 upload files
				// rules: 1) folder name as username
				// 2) file name as title
				Pic pic = new Pic();
				pic.setPicCount(0);
				pic.setPictitle(folderUserName);
				pic.setPicUrl(file.getAbsolutePath());
				pic.setUsername(folderUserName);
				dao.save(pic);
			}
			
		}

	}

	public static Map listUpFoldersAndFiles(String pictureFolderPath) {
		Map returnMap = new Hashtable();
		// key: foldername
		// value: List<filePath>
		File folders = new File(pictureFolderPath);
		File[] fileFolders = folders.listFiles();
		for (int i = 0; i < fileFolders.length; i++) {
			if (fileFolders[i].isDirectory()) {
				String key = fileFolders[i].getName();
				String fileFolderPath = fileFolders[i].getPath();

				File subFileFolder = new File(fileFolderPath);
				File[] subFiles = subFileFolder.listFiles();
				List fileLists = new ArrayList();
				for (int k = 0; k < subFiles.length; k++) {
					File value = subFiles[k];
					fileLists.add(value);
				}
				returnMap.put(key, fileLists);
			}
		}

		return returnMap;
	}

	/**
	 * unzipPictures
	 */
	public static void unzipPictures(String fileName, String filePath) {
		// String unzippedPath = "";
		try {
			// String fileName = "E:\\test\\myfiles.zip";
			// String filePath = "E:\\test\\";
			ZipFile zipFile = new ZipFile(fileName);
			Enumeration emu = zipFile.entries();
			int i = 0;
			while (emu.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) emu.nextElement();
				// 会把目录作为一个file读出一次，所以只建立目录就可以，之下的文件还会被迭代到。
				if (entry.isDirectory()) {
					new File(filePath + entry.getName()).mkdirs();
					continue;
				}
				BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));
				File file = new File(filePath + entry.getName());
				// 加入这个的原因是zipfile读取文件是随机读取的，这就造成可能先读取一个文件
				// 而这个文件所在的目录还没有出现过，所以要建出目录来。
				File parent = file.getParentFile();
				if (parent != null && (!parent.exists())) {
					parent.mkdirs();
				}
				FileOutputStream fos = new FileOutputStream(file);
				BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);

				int count;
				byte data[] = new byte[BUFFER];
				while ((count = bis.read(data, 0, BUFFER)) != -1) {
					bos.write(data, 0, count);
				}
				bos.flush();
				bos.close();
				bis.close();
			}
			zipFile.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// return unzippedPath;
	}

}
