package com.pacee1.service.impl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.pacee1.service.FdfsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p></p>
 *
 * @author : Pace
 * @date : 2020-08-14 15:56
 **/
@Service
public class FdfsServiceImpl implements FdfsService {

    @Autowired
    private FastFileStorageClient fastFileStorageClient;

    /**
     * 使用FastDFS上传图片
     * @param file
     * @param fileExtName
     * @return
     * @throws Exception
     */
    @Override
    public String upload(MultipartFile file,String fileExtName) throws Exception {
        StorePath storePath = fastFileStorageClient.uploadFile(file.getInputStream(), file.getSize(), fileExtName, null);
        String fullPath = storePath.getFullPath();
        return fullPath;
    }
}
