package org.example.handler.mybatisplus;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.example.utils.SecurityUtils;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 设置Mp字段自动填充
 * 业务情况一：评论创建
 * 业务情况二：评论更新
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * 添加填充
     * @param metaObject 元对象
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        Long userId = null;
//        try {
            userId = SecurityUtils.getUserId();
//        } catch (Exception e) {
//            e.printStackTrace();
//            userId = -1L;//表示是自己创建
//        }
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("createBy",userId , metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("updateBy", userId, metaObject);
    }

    /**
     * 更新填充
     * @param metaObject 元对象
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName(" ", SecurityUtils.getUserId(), metaObject);
    }
}
