package org.example.constant;

/**
 * 常量类 以 替代 字面值
 */
public class SystemConstants {
    /**
     * 文章状态：草稿
     */
    public static final int ARTICLE_STATUS_DRAFT = 1;

    /**
     * 文章状态：正常发布
     */
    public static final int  ARTICLE_STATUS_NORMAL = 0;

    /**
     *分类/菜单/评论状态：正常
     */
    public static final String STATUS_NORMAL = "0";

    /**
     *  友链转台： 审核通过
     */
    public static final String Link_Status_Normal = "0";

    /**
     * 根评论的id:rootID=-1
     */
    public static final String ROOT_COMMENT = "-1";
    /**
     * 评论类型为：文章评论
     */
    public static final String ARTICLE_COMMENT = "0";
    /**
     * 评论类型为：友联评论
     */
    public static final String LINK_COMMENT = "1";

    /**
     * Menu类型为：菜单
     */
    public static final String MENU = "C";

    /**
     * Menu类型为：按钮
     */
    public static final String BUTTON = "F";

    /**
     * 后台用户
     */
    public static final String ADMIN = "1";

}
