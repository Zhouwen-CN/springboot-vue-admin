create database if not exists springboot_vue_admin;
use springboot_vue_admin;

DROP TABLE IF EXISTS t_user;
create table t_user(
    id            bigint primary key auto_increment comment '主键',
    username      varchar(15)      not null comment '用户名',
    password      varchar(60)      not null comment '密码',
    token_version bigint default 0 not null comment 'token版本',
    create_user varchar(15) default null comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_user varchar(15) default null comment '更新者',
    update_time datetime default current_timestamp comment '更新时间',
    constraint uniq_user_name unique (username)
) comment '用户表';

DROP TABLE IF EXISTS t_user_role;
create table t_user_role(
    id          bigint primary key auto_increment comment '主键',
    user_id     bigint not null comment '用户id',
    role_id     bigint not null comment '角色id',
    create_user varchar(15) default null comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_user varchar(15) default null comment '更新者',
    update_time datetime default current_timestamp comment '更新时间'
) comment '用户角色关系表';

DROP TABLE IF EXISTS t_role;
create table t_role(
    id          bigint primary key auto_increment comment '主键',
    role_name varchar(15) not null comment '角色名称',
    description varchar(40) not null comment '角色说明',
    create_user varchar(15) default null comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_user varchar(15) default null comment '更新者',
    update_time datetime default current_timestamp comment '更新时间',
    constraint uniq_role_name unique (role_name)
) comment '角色表';

DROP TABLE IF EXISTS t_role_menu;
create table t_role_menu(
    id          bigint primary key auto_increment comment '主键',
    role_id     bigint not null comment '角色id',
    menu_id     bigint not null comment '菜单id',
    create_user varchar(15) default null comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_user varchar(15) default null comment '更新者',
    update_time datetime default current_timestamp comment '更新时间'
) comment '角色菜单关系表';

DROP TABLE IF EXISTS t_menu;
create table t_menu(
    id          bigint primary key auto_increment comment '主键',
    title       varchar(15) not null comment '标题',
    access_path varchar(40) not null comment '访问路径',
    file_path   varchar(60) default null comment '文件路径',
    icon        varchar(15) not null comment '图标',
    pid         bigint      not null comment '父级菜单id',
    keep_alive  bit(1) default 0 null comment '是否缓存',
    menu_type   tinyint not null comment '菜单类型：0-目录，1-菜单',
    create_user varchar(15) default null comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_user varchar(15) default null comment '更新者',
    update_time datetime default current_timestamp comment '更新时间',
    constraint uniq_enum_title unique (title)
) comment '菜单表';

drop table if exists t_login_log;
create table t_login_log(
    id          bigint primary key auto_increment comment '主键',
    operation   tinyint not null comment '操作类型',
    status      tinyint not null comment '操作状态',
    ip          varchar(32)  not null comment 'ip地址',
    user_agent  varchar(200) not null comment '用户代理',
    create_user varchar(15) default null comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    index idx_login_create_time(create_time desc)
) comment '登录日志表';

drop table if exists t_operation_log;
create table t_operation_log(
    id        bigint primary key auto_increment comment '主键',
    operation varchar(10) not null comment '操作',
    url         varchar(50) not null comment '请求地址',
    method      tinyint not null comment '请求方式',
    params      text default null comment '请求参数',
    time        bigint          not null comment '请求耗时',
    status      tinyint not null comment '操作状态',
    ip          varchar(32)  not null comment 'ip地址',
    user_agent  varchar(200) not null comment '用户代理',
    create_user varchar(15) default null comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    index idx_ops_create_time (create_time desc)
) comment '操作日志表';

drop table if exists t_error_log;
create table t_error_log(
    id          bigint primary key auto_increment comment '主键',
    url         varchar(50) not null comment '请求地址',
    method      tinyint not null comment '请求方式',
    params      text default null comment '请求参数',
    ip          varchar(32)  not null comment 'ip地址',
    user_agent  varchar(200) not null comment '用户代理',
    error_msg   text         not null comment '错误信息',
    create_user varchar(15) default null comment '创建者',
    create_time timestamp default current_timestamp comment '创建时间',
    index idx_error_create_time (create_time desc)
) comment '错误日志表';

drop table if exists t_dict_type;
create table t_dict_type(
    id bigint primary key auto_increment comment '主键',
    dict_type varchar(15) not null comment '字典类型',
    name varchar(40) not null comment '字典名称',
    create_user varchar(15) default null comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_user varchar(15) default null comment '更新者',
    update_time datetime default current_timestamp comment '更新时间',
    constraint uniq_dict_type unique (dict_type)
) comment '字典类型表';

drop table if exists t_dict_data;
create table t_dict_data(
    id bigint primary key auto_increment comment '主键',
    type_id bigint not null comment '字典类型id',
    label varchar(15) not null comment '字典键',
    data int not null comment '字典值',
    sort int default 0 comment '字典排序',
    create_user varchar(15) default null comment '创建者',
    create_time datetime not null default current_timestamp comment '创建时间',
    update_user varchar(15) default null comment '更新者',
    update_time datetime default current_timestamp comment '更新时间',
    constraint uniq_dict_type_label unique (type_id, label)
) comment '字典数据表';

drop table if exists t_data_source;
create table t_data_source(
    id bigint primary key auto_increment comment '主键',
    name varchar(15) not null comment '数据源名称',
    url varchar(200) not null comment '数据源连接',
    username varchar(15) not null comment '用户名',
    password varchar(15) not null comment '密码',
    create_user varchar(15) default null comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_user varchar(15) default null comment '更新者',
    update_time datetime default current_timestamp comment '更新时间',
    constraint uniq_datasource_name unique (name)
) comment = '数据源配置表';

drop table if exists t_codegen_table;
create table t_codegen_table  (
  id bigint primary key auto_increment comment '主键',
  data_source_id bigint not null comment '数据源配置的编号',
  table_name varchar(50) not null comment '表名称',
  table_comment varchar(100) not null comment '表描述',
  class_name varchar(100) not null comment '类名称',
  author varchar(20) not null comment '作者',
  base_package varchar(50) not null comment '基础包名',
  ignore_table_prefix varchar(20) default null comment '忽略表前缀',
  ignore_column_prefix varchar(20) default null comment '忽略字段前缀',
  create_user varchar(15) default null comment '创建者',
  create_time datetime default current_timestamp comment '创建时间',
  update_user varchar(15) default null comment '更新者',
  update_time datetime default current_timestamp comment '更新时间',
  constraint uniq_table_name unique (table_name)
) comment = '代码生成表';

drop table if exists t_codegen_column;
create table t_codegen_column  (
  id bigint primary key auto_increment comment '主键',
  table_id bigint not null comment '代码生成表编号',
  column_name varchar(50) not null comment '字段名',
  column_comment varchar(100) not null comment '字段描述',
  db_type varchar(20) not null comment '数据库类型',
  column_length int default null comment '字段长度',
  java_field varchar(50) not null comment 'java属性名',
  java_type varchar(20) not null comment 'java类型',
  js_type varchar(20) not null comment 'js类型',
  ordinal_position int not null comment '排序',
  primary_key bit(1) not null comment '是否主键',
  nullable bit(1) not null comment '是否允许为空',
  insert_field bit(1) not null comment '是否为insert字段',
  update_field bit(1) not null comment '是否为update字段',
  select_field bit(1) not null comment '是否为select字段',
  create_user varchar(15) default null comment '创建者',
  create_time datetime default current_timestamp comment '创建时间',
  update_user varchar(15) default null comment '更新者',
  update_time datetime default current_timestamp comment '更新时间'
) comment = '代码生成字段表';

create table t_student_test(
  id bigint primary key auto_increment comment '主键',
  name varchar(20)  not null comment '名称',
  age int not null comment '年龄',
  gender varchar(10) not null comment '性别',
  create_user varchar(15) default null comment '创建者',
  create_time datetime default current_timestamp comment '创建时间',
  update_user varchar(15) default null comment '更新者',
  update_time datetime default current_timestamp comment '更新时间'
) comment = '学生表-代码生成测试';

# 尚未用到
create table t_endpoint(
    id bigint primary key auto_increment comment '主键',
	service_id bigint not null comment '服务ID',
    name varchar(20)  not null comment '接口名称',
    description varchar(50)  not null comment '接口描述',
    access_url varchar(100) not null comment '访问路径',
    backend_url varchar(100) not null comment '后端接口路径',
	method tinyint not null comment '请求方法',
    response_type tinyint not null comment '响应类型',
    pre_script text comment '前置脚本',
    status tinyint(1) default 1 comment '接口状态',
    create_time timestamp default CURRENT_TIMESTAMP comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP comment '更新时间',
    constraint uniq_name unique (name),
    constraint uniq_access_url unique (access_url)
) comment '接口配置表';

create table t_server(
	id bigint primary key auto_increment comment '主键',
    name varchar(20)  not null comment '服务名称',
	protocol tinyint not null comment '协议类型',
	host varchar(20) not null comment '域名或IP',
	port int not null comment '端口',
	pre_script text comment '前置脚本',
	post_script text comment '后置脚本',
	status tinyint(1) default 1 comment '服务状态',
	create_time timestamp default CURRENT_TIMESTAMP comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP comment '更新时间',
    constraint uniq_name unique (name)
) comment '服务配置表';