package com.easycodebox.common.file;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.*;
import org.springframework.core.type.classreading.*;

import java.io.*;
import java.net.URL;
import java.util.*;

public class Resources {
	
	/**
	 * 扫描class文件
	 */
	private static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	public static List<Class<?>> scanClass(String... packages) throws IOException, ClassNotFoundException {
		List<Class<?>> classes = new ArrayList<>();
		
		if(packages != null && packages.length > 0) {
			for(int i = 0; i < packages.length; i++) {
				packages[i] = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
						+ org.springframework.util.ClassUtils.convertClassNameToResourcePath(packages[i])
						+ "/**/*.class";
			}
		}
		List<Resource> resources = Resources.scan(packages);
		MetadataReaderFactory readerFactory = new CachingMetadataReaderFactory(
				resourcePatternResolver);
		for (Resource resource : resources) {
			if (resource.isReadable()) {
				MetadataReader reader = readerFactory
						.getMetadataReader(resource);
				String className = reader.getClassMetadata()
						.getClassName();
				Class<?> clazz = resourcePatternResolver.getClassLoader()
						.loadClass(className);
				classes.add(clazz);
			}
		}
		return classes;
	}
	
	/**
	 * 扫描class文件
	 */
	public static List<Resource> scan(String... locationPattern) throws IOException {
		List<Resource> list = new ArrayList<>();
		if (locationPattern != null && locationPattern.length > 0) {
			for (String location : locationPattern) {
				Resource[] resources = resourcePatternResolver.getResources(location);
				for (Resource resource : resources) {
					if (resource.isReadable()) {
						list.add(resource);
					}
				}
			}
		}
		return list;
	}
	
	public static List<File> scan2File(String... locationPattern) throws IOException, ClassNotFoundException {
		List<File> rs = new ArrayList<>();
		List<Resource> resources = scan(locationPattern);
		for(Resource r : resources) {
			rs.add(r.getFile());
		}
		return rs;
	}
	
	public static List<InputStream> scan2InputStream(String... locationPattern) throws IOException, ClassNotFoundException {
		List<InputStream> rs = new ArrayList<>();
		List<Resource> resources = scan(locationPattern);
		for(Resource r : resources) {
			rs.add(r.getInputStream());
		}
		return rs;
	}
	
	public static List<URL> scan2URL(String... locationPattern) throws IOException, ClassNotFoundException {
		List<URL> rs = new ArrayList<>();
		List<Resource> resources = scan(locationPattern);
		for(Resource r : resources) {
			rs.add(r.getURL());
		}
		return rs;
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(scan("classpath*:com/easycodebox/common/*.class"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
