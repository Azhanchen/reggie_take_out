package com.itljx.checkup.dto;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class UserInfoDto implements Serializable {
    private Long id;
    private Long userId;
    private Long infoId;
    private Long isRead;
    private String title;

    private String content;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    private String type;

    private Integer status;

    private Integer sort;

    public UserInfoDto(Long userId, Long infoId, Long isRead, String title, String content, LocalDateTime createTime, LocalDateTime updateTime, Long createUser, Long updateUser, String type, Integer status, Integer sort) {
        this.userId = userId;
        this.infoId = infoId;
        this.isRead = isRead;
        this.title = title;
        this.content = content;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.createUser = createUser;
        this.updateUser = updateUser;
        this.type = type;
        this.status = status;
        this.sort = sort;
    }
}
