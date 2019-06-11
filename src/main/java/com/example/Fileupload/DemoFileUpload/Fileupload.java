package com.example.Fileupload.DemoFileUpload;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class Fileupload {

	private static String filepath = "C:\\Demo/";

	@RequestMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public void uploadFile(@RequestAttribute("file") MultipartFile file) throws IOException {
		File saveFile = new File(filepath);
		if (!saveFile.exists()) {
			System.out.println(saveFile.mkdir());

		}
		File saveFile1 = new File(filepath + file.getOriginalFilename());
		System.out.println("file1 " + saveFile.exists());
		saveFile1.createNewFile();
		FileOutputStream fs = new FileOutputStream(saveFile1);
		fs.write(file.getBytes());
		fs.close();

	}

	private static final String EXTERNAL_FILE_PATH = "C:\\Demo/";

	@RequestMapping("/file/{fileName:.+}")
	public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("fileName") String fileName) throws IOException {

		File file = new File(EXTERNAL_FILE_PATH + fileName);
		System.out.println(file.exists());
		if (file.exists()) {

			String mimeType = URLConnection.guessContentTypeFromName(file.getName());
			if (mimeType == null) {
				mimeType = "application/octet-stream";
			}
			response.setContentType(mimeType);
			response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
			response.setContentLength((int) file.length());
			response.getOutputStream().flush();

			InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
			FileCopyUtils.copy(inputStream, response.getOutputStream());
		} else {
			System.out.println("Folder Not Exixt");
		}
	}

}
