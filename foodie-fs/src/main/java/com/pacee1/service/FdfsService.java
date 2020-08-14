package com.pacee1.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * <p>Fdfs</p>
 *
 * @author : Pace
 * @date : 2020-08-14 15:56
 **/
public interface FdfsService {

    String upload(MultipartFile file, String fileExtName) throws Exception;
}
