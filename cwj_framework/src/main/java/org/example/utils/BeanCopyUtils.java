package org.example.utils;

import org.example.domain.entity.Article;
import org.example.domain.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 自定义Bean拷贝方法
 */
public class BeanCopyUtils {

    /**
     * 工具类 构造方法 私有方法
     */
    private BeanCopyUtils() {

    }

    public static <V> V copyBean(Object source, Class<V> clazz) {
        //创建目标对象
        V result = null;
        try {
            result = clazz.newInstance();
            //实现属性copy
            BeanUtils.copyProperties(source, result);
            //返回结果

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static <O,V> List<V> copyBeanList(List<O> list, Class<V> clazz) {
//        List<V> vs = new ArrayList<>();
//        for (Object o : list) {
//            try {
//                V result = clazz.newInstance();
//                BeanUtils.copyProperties(o,result);
//                vs.add(result);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        return vs;
        return list.stream()
                .map(o -> copyBean(o,clazz))
                .collect(Collectors.toList());
    }

    public static void main(String[] args) {
        Article article = new Article();
        article.setId(1l);
        article.setTitle("ss");


        HotArticleVo hotArticleVo = copyBean(article, HotArticleVo.class);
        System.out.println(hotArticleVo);
    }

}
