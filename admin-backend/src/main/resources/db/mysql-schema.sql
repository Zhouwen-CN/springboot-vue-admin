create database if not exists springboot_vue_admin;
use springboot_vue_admin;

DROP TABLE IF EXISTS `t_user`;
create table t_user
(
    `id`            bigint primary key auto_increment comment '主键',
    `username`      varchar(15)      not null comment '用户名',
    `password`      varchar(60)      not null comment '密码',
    `token_version` bigint default 0 not null comment 'token版本',
    `create_time`   timestamp default CURRENT_TIMESTAMP comment '创建时间',
    `update_time`   timestamp default CURRENT_TIMESTAMP comment '更新时间',
    constraint uniq_username unique (username)
) comment '用户表';


DROP TABLE IF EXISTS `t_user_role`;
create table t_user_role
(
    `id`          bigint primary key auto_increment comment '主键',
    `user_id`     bigint not null comment '用户id',
    `role_id`     bigint not null comment '角色id',
    `create_time` timestamp default CURRENT_TIMESTAMP comment '创建时间',
    `update_time` timestamp default CURRENT_TIMESTAMP comment '更新时间'
) comment '用户角色关系表';


DROP TABLE IF EXISTS `t_role`;
create table t_role
(
    `id`          bigint primary key auto_increment comment '主键',
    `role_name` varchar(15) not null comment '角色名称',
    `desc`    varchar(40) not null comment '角色说明',
    `create_time` timestamp default CURRENT_TIMESTAMP comment '创建时间',
    `update_time` timestamp default CURRENT_TIMESTAMP comment '更新时间',
    constraint uniq_role_name unique (role_name)
) comment '角色表';


DROP TABLE IF EXISTS `t_role_menu`;
create table t_role_menu
(
    `id`          bigint primary key auto_increment comment '主键',
    `role_id`     bigint not null comment '角色id',
    `menu_id`     bigint not null comment '菜单id',
    `create_time` timestamp default CURRENT_TIMESTAMP comment '创建时间',
    `update_time` timestamp default CURRENT_TIMESTAMP comment '更新时间'
) comment '用户菜单关系表';


DROP TABLE IF EXISTS `t_menu`;
create table t_menu
(
    `id`          bigint primary key auto_increment comment '主键',
    `title`       varchar(15) not null comment '标题',
    `access_path` varchar(40) not null comment '访问路径',
    `file_path`   varchar(60) null comment '文件路径',
    `icon`        varchar(15) not null comment '图标',
    `pid`         bigint      not null comment '父级菜单id',
    `is_keep_alive` tinyint(1) default 0 null comment '是否缓存',
    `create_time` timestamp default CURRENT_TIMESTAMP comment '创建时间',
    `update_time` timestamp default CURRENT_TIMESTAMP comment '更新时间',
    constraint uniq_access_path unique (access_path)
) comment '菜单表';


drop table if exists t_login_log;
create table t_login_log
(
    `id`          bigint primary key auto_increment comment '主键',
    `username`    varchar(15)  not null comment '用户名',
    `operation`   tinyint not null comment '操作类型',
    `status`      tinyint not null comment '操作状态',
    `ip`          varchar(32)  not null comment 'ip地址',
    `user_agent`  varchar(255) not null comment '用户代理',
    `create_time` timestamp default CURRENT_TIMESTAMP comment '创建时间',
    index idx_create_time(`create_time` desc)
) comment '登录日志表';


drop table if exists t_operation_log;
create table t_operation_log
(
    `id`        bigint primary key auto_increment comment '主键',
    `username`  varchar(15)  not null comment '用户名',
    `operation` varchar(10) not null comment '操作',
    `url`         varchar(50) not null comment '请求地址',
    `method`      tinyint not null comment '请求方式',
    `params`      varchar(500) default null comment '请求参数',
    `time`        bigint          not null comment '请求耗时',
    `status`      tinyint not null comment '操作状态',
    `ip`          varchar(32)  not null comment 'ip地址',
    `user_agent`  varchar(255) not null comment '用户代理',
    `create_time` timestamp    default CURRENT_TIMESTAMP comment '创建时间',
    index idx_create_time (`create_time` desc)
) comment '操作日志表';


drop table if exists t_error_log;
create table t_error_log
(
    `id`          bigint primary key auto_increment comment '主键',
    `username`    varchar(15)  not null comment '用户名',
    `url`         varchar(50) not null comment '请求地址',
    `method`      tinyint not null comment '请求方式',
    `params`      varchar(500) default null comment '请求参数',
    `ip`          varchar(32)  not null comment 'ip地址',
    `user_agent`  varchar(255) not null comment '用户代理',
    `error_msg`   text         not null comment '错误信息',
    `create_time` timestamp    default CURRENT_TIMESTAMP comment '创建时间',
    index idx_create_time (`create_time` desc)
) comment '错误日志表';


drop table if exists t_dict_type;
create table t_dict_type(
  `id` bigint primary key auto_increment comment '主键',
  `type` varchar(50) not null comment '字典类型',
  `name` varchar(50) not null comment '字典名称',
  `create_time` timestamp default CURRENT_TIMESTAMP comment '创建时间',
  `update_time` timestamp default CURRENT_TIMESTAMP comment '更新时间',
  constraint uniq_type unique (type)
) comment '字典类型表';


drop table if exists t_dict_data;
create table t_dict_data(
  `id` bigint primary key auto_increment comment '主键',
  `type_id` bigint not null comment '字典类型id',
  `label` varchar(50) not null comment '字典键',
  `value` int not null comment '字典值',
  `sort` int default 0 comment '字典排序',
  `create_time` timestamp default CURRENT_TIMESTAMP comment '创建时间',
  `update_time` timestamp default CURRENT_TIMESTAMP comment '更新时间',
  constraint uniq_type_label unique (type_id, label)
) comment '字典数据表';


create table t_endpoint(
    `id` bigint primary key auto_increment comment '主键',
	`service_id` bigint not null comment '服务ID',
    `name` varchar(20)  not null comment '接口名称',
    `desc` varchar(50)  not null comment '接口描述',
    `access_url` varchar(100) not null comment '访问路径',
    `backend_url` varchar(100) not null comment '后端接口路径',
	`method` tinyint not null comment '请求方法',
    `response_type` tinyint not null comment '响应类型',
    `pre_script` text comment '前置脚本',
    `status` tinyint(1) default 1 comment '接口状态',
    `create_time` timestamp default CURRENT_TIMESTAMP comment '创建时间',
    `update_time` timestamp default CURRENT_TIMESTAMP comment '更新时间',
    constraint uniq_name unique (name),
    constraint uniq_access_url unique (access_url)
) comment '接口配置表';


create table t_server(
	`id` bigint primary key auto_increment comment '主键',
    `name` varchar(20)  not null comment '服务名称',
	`protocol` tinyint not null comment '协议类型',
	`host` varchar(20) not null comment '域名或IP',
	`port` int not null comment '端口',
	`pre_script` text comment '前置脚本',
	`post_script` text comment '后置脚本',
	`status` tinyint(1) default 1 comment '服务状态',
	`create_time` timestamp default CURRENT_TIMESTAMP comment '创建时间',
    `update_time` timestamp default CURRENT_TIMESTAMP comment '更新时间',
    constraint uniq_name unique (name)
) comment '服务配置表';