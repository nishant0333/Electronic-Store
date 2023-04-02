package com.lcwd.electronic.store.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.lcwd.electronic.store.exceptions.BadApiRequestException;
import com.lcwd.electronic.store.services.FileService;

@Service
public class FileServiceImpl implements FileService {

	Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);
	
	
	

	@Override
	public String uploadFile(MultipartFile file, String path)  {

		String originalFilename = file.getOriginalFilename();

		logger.info("Filename : {}", originalFilename);

		String filename = UUID.randomUUID().toString();
		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		String fileNameWithExtension = filename + extension;
		String fullpathWithFileName = path + fileNameWithExtension;

		logger.info("full image path : {} ",fullpathWithFileName);
		
		if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg")
				|| extension.equalsIgnoreCase(".jpeg")) {

			// file Save
			logger.info("file extension is {} ",extension);
			File folder = new File(path);

			if (!folder.exists()) {

				// create the folder
				folder.mkdirs();
			}

			// upload

			try {
				Files.copy(file.getInputStream(), Paths.get(fullpathWithFileName));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return fileNameWithExtension;
		} else {

			throw new BadApiRequestException("File with this " + extension + " not allowed !!");
		}

	}
	
	
	

	@Override
	public InputStream getResource(String path, String name) throws FileNotFoundException {

		String fullpath = path + File.separator + name;

		InputStream inputStream = new FileInputStream(fullpath);

		return inputStream;
	}

}
