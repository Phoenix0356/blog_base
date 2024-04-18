package com.phoenix.blog.core.service.impl;

import com.phoenix.blog.core.mapper.TagMapper;
import com.phoenix.blog.core.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TagServiceImpl implements TagService {
    @Autowired
    private TagMapper tagMapper;
}
