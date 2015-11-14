package com.pwc.commons;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileHelper {
	public static void save(String path, byte[] bytes) {
		System.out.println(bytes);
		FileOutputStream fos = null;
		File file = new File(path);

		try {
			fos = new FileOutputStream(file);
			byte[] numBytes = bytes;
			fos.write(numBytes);
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}