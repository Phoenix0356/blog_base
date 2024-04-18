package com.phoenix.blog.core.service.impl;

import com.phoenix.blog.core.mapper.PictureMapper;
import com.phoenix.blog.core.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PictureServiceImpl implements PictureService {
    @Autowired
    private PictureMapper pictureMapper;
}
