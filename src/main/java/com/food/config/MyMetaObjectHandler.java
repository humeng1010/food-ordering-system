package com.food.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.food.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.function.Consumer;

/**
 * 自动自动填充
 */
@Slf4j
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        log.info("start insert fill ....");
        Long employeeId = (Long) ThreadLocalUtil.get("employee");
        fillProperties(metaObject, employeeId);

        Long userId = (Long) ThreadLocalUtil.get("user");
        fillProperties(metaObject, userId);

    }

    private static void fillProperties(MetaObject metaObject, Long id) {
        if (id!=null){
            hasSetter(metaObject,"createTime",metaObject1 -> metaObject1.setValue("createTime", LocalDateTime.now()));
            hasSetter(metaObject,"updateTime",metaObject1 -> metaObject1.setValue("updateTime",LocalDateTime.now()));
            hasSetter(metaObject,"createUser",metaObject1 -> metaObject1.setValue("createUser",id));
            hasSetter(metaObject,"updateUser",metaObject1 -> metaObject1.setValue("updateUser",id));
        }
    }

    private static void hasSetter(MetaObject metaObject,String name, Consumer<MetaObject> consumer){
        if (metaObject.hasSetter(name)){
            consumer.accept(metaObject);
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        log.info("start update fill ....");
        Long employeeId = (Long) ThreadLocalUtil.get("employee");
        if (employeeId!=null){
            metaObject.setValue("updateTime",LocalDateTime.now());
            metaObject.setValue("updateUser",employeeId);
        }

        Long userId = (Long) ThreadLocalUtil.get("user");
        if (userId!=null){
            metaObject.setValue("updateTime",LocalDateTime.now());
            metaObject.setValue("updateUser",userId);
        }

    }
}