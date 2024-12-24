package com.spring.framework.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("hh_user")
public class User {
    @TableId
    private Long id ;
    @TableField("user_name")
    private String username;//用户名
    @TableField("nick_name")
    private String nickName;//昵称

    private String password;//密码

    private String type;//用户类型：0代表普通用户，1代表管理员

    private String status;//账号状态（0正常 1停用）

    private String email;//邮箱

    private String phonenumber;//手机号

    private String sex;//用户性别（0男，1女，2未知）

    private String avatar;//头像
     @TableField("create_by")
    private Long createBy;//创建人的用户id
     @TableField("create_time")
    private Date createTime;//创建时间
     @TableField("update_by")
    private Long updateBy;//更新人
   @TableField("update_time")
    private Date updateTime;//更新时间
    @TableField("del_flag")
    private Integer delFlag;//删除标志（0代表未删除，1代表已删除）

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }

    public String getNickName() {
        return nickName;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getSex() {
        return sex;
    }

    public String getAvatar() {
        return avatar;
    }

    public Long getCreateBy() {
        return createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public Integer getDelFlag() {
        return delFlag;
    }
}
