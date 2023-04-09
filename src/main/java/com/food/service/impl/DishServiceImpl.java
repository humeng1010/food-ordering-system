package com.food.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.food.dto.DishDto;
import com.food.entity.Category;
import com.food.entity.Dish;
import com.food.entity.DishFlavor;
import com.food.mapper.DishMapper;
import com.food.service.CategoryService;
import com.food.service.DishFlavorService;
import com.food.service.DishService;
import com.food.utils.RedisConstants;
import com.food.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result<IPage<Dish>> getDishByPageWithCondition(Long page, Long pageSize, String name) {
        IPage<Dish> dishPage = new Page<>(page,pageSize);

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper
                .like(StringUtils.hasText(name), Dish::getName,name)
                .orderByDesc(Dish::getUpdateTime);
        this.page(dishPage,dishLambdaQueryWrapper);

        return Result.success(dishPage);
    }


    @Autowired
    private DishFlavorService dishFlavorService;
    @Override
    public Result<String> saveDish(DishDto dishDto) {
        Long categoryId = dishDto.getCategoryId();
        if (categoryId!=null){
//            清空包含该菜品的redis缓存
            stringRedisTemplate.delete(RedisConstants.FOOD_DISH+categoryId);
        }
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
//        保存菜品
        this.save(dish);
//        获取菜品id
        Long dishId = dish.getId();

//        填充口味中的菜品id
        List<DishFlavor> dishFlavors = dishDto.getFlavors()
                .stream().peek(dishFlavor -> dishFlavor.setDishId(dishId))
                .collect(Collectors.toList());

//        批量保存
        dishFlavorService.saveBatch(dishFlavors);

        return Result.success("ok");
    }

    @Autowired
    private  CategoryService categoryService;


    @Override
    public Result<DishDto> getDishWithFlavorByDishId(Long id) {
//        查询dish
        Dish dish = this.getById(id);
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId,id);
//        查询flavor
        List<DishFlavor> dishFlavors = dishFlavorService.list(queryWrapper);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        dishDto.setFlavors(dishFlavors);

//        查询分类名称
        Long categoryId = dish.getCategoryId();
        Category category = categoryService.getById(categoryId);

        dishDto.setCategoryName(category.getName());

        return Result.success(dishDto);
    }

    @Override
    public Result<String> updateDishOrFlavor(DishDto dishDto) {
        Long categoryId = dishDto.getCategoryId();
        if (categoryId!=null){
//            清空包含该菜品的redis缓存
            stringRedisTemplate.delete(RedisConstants.FOOD_DISH+categoryId);
        }
        Dish dish_temp = this.getById(dishDto.getId());
        Long originCategoryId = dish_temp.getCategoryId();
//        如果原来的分类id和传递过来的分类id不一样则说明修改的数据包括了分类,我们这两个分类下的数据都需要清理
        if (!Objects.equals(originCategoryId, categoryId)){
            stringRedisTemplate.delete(RedisConstants.FOOD_DISH+originCategoryId);
        }
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDto,dish);
//        更新dish
        this.updateById(dish);
//        更新flavor
//        先删除全部的flavor 再 批量保存
        LambdaUpdateWrapper<DishFlavor> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(dishDto.getId()!=null,DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(updateWrapper);

        //        填充口味中的菜品id
        List<DishFlavor> dishFlavors = dishDto.getFlavors()
                .stream().peek(dishFlavor -> dishFlavor.setDishId(dishDto.getId())).collect(Collectors.toList());

        dishFlavorService.saveBatch(dishFlavors);
        return Result.success("ok");
    }

    @Override
    public Result<String> deleteDishAndFlavorByDishIds(List<Long> ids) {
//        根据ids获取菜品
        List<Dish> dishes = this.listByIds(ids);
//        遍历菜品获取分类id,删除redis缓存,保证数据库数据和缓存数据的一致性
        for (Dish dish : dishes) {
            Long categoryId = dish.getCategoryId();
            stringRedisTemplate.delete(RedisConstants.FOOD_DISH+categoryId);
        }
//        删除菜品
        this.removeByIds(ids);
//        删除菜品对应的口味
        LambdaUpdateWrapper<DishFlavor> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.in(DishFlavor::getDishId,ids);
        dishFlavorService.remove(updateWrapper);
        return Result.success("ok");
    }

    @Override
    public Result<String> updateStatusDishByIds(List<Long> ids, Integer status) {
        //        根据ids获取菜品
        List<Dish> dishes = this.listByIds(ids);
//        遍历菜品获取分类id,删除redis缓存,保证数据库数据和缓存数据的一致性
        for (Dish dish : dishes) {
            Long categoryId = dish.getCategoryId();
            stringRedisTemplate.delete(RedisConstants.FOOD_DISH+categoryId);
        }
        LambdaUpdateWrapper<Dish> updateWrapper = new LambdaUpdateWrapper<>();

        updateWrapper.set(Dish::getStatus,status).in(Dish::getId,ids);
        this.update(updateWrapper);
        return Result.success("ok");
    }

    /**
     * 获取菜品列表和相应的口味
     * @param categoryId 种类id
     * @return 带有相应口味的菜品列表
     */
    @Override
    public Result<List<DishDto>> getDishList(Long categoryId) {
        String redisKey = RedisConstants.FOOD_DISH +categoryId;
//        查询redis是否有缓存的菜品list
        String strResult = stringRedisTemplate.opsForValue().get(redisKey);
        if (Objects.nonNull(strResult)){
            List list = JSON.parseObject(strResult, List.class);
            return Result.success(list);
        }
//        根据种类查询菜品列表
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(categoryId!=null,Dish::getCategoryId,categoryId)
                .orderByAsc(Dish::getSort);
        List<Dish> dishList = this.list(queryWrapper);
//        根据id获取菜品种类
        Category category = categoryService.getById(categoryId);

//        遍历菜品
        List<DishDto> dtoList = dishList.stream().map(dish -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(dish, dishDto);
//            种类名称保存到DishDto中
            dishDto.setCategoryName(category.getName());
//            查询菜品口味
            Long dishId = dish.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
//            保存菜品口味到dishDto
            dishDto.setFlavors(dishFlavors);

            return dishDto;

        }).collect(Collectors.toList());
//        ttl设置为60分钟,防止更新数据忘记清除缓存造成缓存数据一直不正确的情况
        stringRedisTemplate.opsForValue().set(redisKey, JSON.toJSONString(dtoList),60, TimeUnit.MINUTES);

        return Result.success(dtoList);
    }

}
