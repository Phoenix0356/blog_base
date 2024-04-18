package com.phoenix.blog.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.sql.Time;
import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tag")
@Accessors(chain = true)
public class Tag {
    @TableId(value = "tag_id", type = IdType.ASSIGN_UUID)
    private String tagId;

    @TableField("tag_content")
    private String tagContent;

    @TableField("tag_add_time")
    private Timestamp tagAddTime;
}
