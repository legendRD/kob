package com.ke.kob.admin.core.mapper;

import com.ke.kob.admin.core.model.db.ProjectUser;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * mybatis mapper 操作数据库表kob_project_user_ 项目用户表
 *
 * @Author: zhaoyuguang
 * @Date: 2018/7/29 下午5:36
 */
@Mapper
public interface ProjectUserMapper {

    String COLUMN = " id, user_code, user_name, project_code, project_name, project_mode, owner, configuration, version, gmt_created, gmt_modified ";

    String TABLE = " kob_project_user_${cluster} ";

    /**
     * 初始化项目用户
     *
     * @param projectUser 项目用户信息
     * @param cluster     集群
     * @return 影响行数
     */
    @Insert("insert into " + TABLE + " (user_code, user_name, project_code, project_name, project_mode, owner, configuration) " +
            "values (#{pu.userCode}, #{pu.userName}, #{pu.projectCode}, #{pu.projectName}, #{pu.projectMode}, #{pu.owner}, #{pu.configuration}) ")
    int insertOne(@Param("pu") ProjectUser projectUser, @Param("cluster") String cluster);

    /**
     * 查询项目用户数量
     *
     * @param projectCode 项目标识
     * @param cluster     集群
     * @return 影响行数
     */
    @Select("select count(1)" +
            "from " + TABLE +
            "where project_code = #{projectCode} ")
    int selectCountByProjectCode(@Param("projectCode") String projectCode, @Param("cluster") String cluster);

    /**
     * 分页查询用户量
     *
     * @param projectCode 项目标识
     * @param start       起始行数
     * @param limit       数量
     * @param cluster     集群
     * @return 用户列表
     */
    @Select("select " + COLUMN +
            "from " + TABLE +
            "where project_code = #{projectCode} " +
            "limit ${start},${limit} ")
    List<ProjectUser> selectPageByProjectCode(@Param("projectCode") String projectCode,
                                              @Param("start") Integer start,
                                              @Param("limit") Integer limit,
                                              @Param("cluster") String cluster);

    /**
     * 获取当前用户所有项目
     *
     * @param userCode 用户标识
     * @param cluster  集群名称
     * @return 项目成员列表
     */
    @Select("select " + COLUMN +
            "from " + TABLE +
            "where user_code = #{userCode} " +
            "order by id desc ")
    List<ProjectUser> selectByUserCode(@Param("userCode") String userCode, @Param("cluster") String cluster);

    /**
     * 项目删除用户
     *
     * @param projectCode 项目表示
     * @param id          唯一主键
     * @param cluster     集群名称
     * @return 影响行数
     */
    @Delete("delete " +
            "from " + TABLE +
            "where project_code = #{projectCode} and id = #{id} ")
    int delete(@Param("projectCode") String projectCode, @Param("id") String id, @Param("cluster") String cluster);

    /**
     * 获取接入项目，通过owner=1查询而出 这里不想考虑极端并发出现一个项目有两个owner
     *
     * @param cluster 集群
     * @return 接入项目列表
     */
    @Select("select " + COLUMN +
            "from " + TABLE +
            "where owner = 1 " +
            "order by id desc ")
    List<ProjectUser> selectProjectIsOwner(@Param("cluster") String cluster);
}
