<#--@formatter:off-->
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${package.mapper}.${table.className}Mapper">
    <!--@formatter:off-->

    <!-- 通用查询映射结果 -->
    <resultMap id="BaseResultMap" type="${package.entity}.${table.className}">
<#list columns as column>
    <#if column.primaryKey><#--生成主键排在第一位-->
        <id column="${column.columnName}" property="${column.javaField}" />
    </#if>
</#list>
<#list columns as column>
    <#if !column.primaryKey><#--生成普通字段 -->
        <result column="${column.columnName}" property="${column.javaField}" />
    </#if>
</#list>
    </resultMap>

    <!-- 通用查询结果列 -->
    <sql id="Base_Column_List">
        ${columns?map(item -> item.columnName)?join(', ')}
    </sql>
</mapper>