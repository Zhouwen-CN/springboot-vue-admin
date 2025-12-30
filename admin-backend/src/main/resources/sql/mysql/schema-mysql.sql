create table t_user(
    id            bigint primary key auto_increment comment '主键',
    username      varchar(15)      not null comment '用户名',
    password      varchar(60)      not null comment '密码',
    refresh_token varchar(200) default null comment '刷新token',
    create_user varchar(15) default null comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_user varchar(15) default null comment '更新者',
    update_time datetime default current_timestamp comment '更新时间',
    constraint uniq_user_name unique (username)
) comment '用户表';

create table t_user_role(
    id          bigint primary key auto_increment comment '主键',
    user_id     bigint not null comment '用户id',
    role_id     bigint not null comment '角色id',
    create_user varchar(15) default null comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_user varchar(15) default null comment '更新者',
    update_time datetime default current_timestamp comment '更新时间'
) comment '用户角色关系表';

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

create table t_role_menu(
    id          bigint primary key auto_increment comment '主键',
    role_id     bigint not null comment '角色id',
    menu_id     bigint not null comment '菜单id',
    create_user varchar(15) default null comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_user varchar(15) default null comment '更新者',
    update_time datetime default current_timestamp comment '更新时间'
) comment '角色菜单关系表';

create table t_menu(
    id          bigint primary key auto_increment comment '主键',
    title       varchar(20) not null comment '标题',
    access_path varchar(40) not null comment '访问路径',
    icon        varchar(15) not null comment '图标',
    pid         bigint      not null comment '父级菜单id',
    keep_alive  bit(1) not null comment '是否缓存',
    menu_type   tinyint not null comment '菜单类型：0-目录，1-菜单',
    sort_id int default 0 comment '菜单排序',
    create_user varchar(15) default null comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_user varchar(15) default null comment '更新者',
    update_time datetime default current_timestamp comment '更新时间',
    constraint uniq_enum_title unique (title)
) comment '菜单表';

create table t_login_log(
    id          bigint primary key auto_increment comment '主键',
    operation   tinyint not null comment '操作类型',
    status      tinyint not null comment '操作状态',
    ip          varchar(32)  not null comment 'ip地址',
    user_agent  varchar(200) not null comment '用户代理',
    create_user varchar(15) default null comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    index idx_login_log_create_time (create_time desc)
) comment '登录日志表';

create table t_operation_log(
    id        bigint primary key auto_increment comment '主键',
    operation varchar(50) not null comment '操作',
    url         varchar(50) not null comment '请求地址',
    method      tinyint not null comment '请求方式',
    params      text default null comment '请求参数',
    time        bigint          not null comment '请求耗时',
    status      tinyint not null comment '操作状态',
    ip          varchar(32)  not null comment 'ip地址',
    user_agent  varchar(200) not null comment '用户代理',
    create_user varchar(15) default null comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    index idx_ops_log_create_time (create_time desc)
) comment '操作日志表';

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
    index idx_error_log_create_time (create_time desc)
) comment '错误日志表';

create table t_dict_type(
    id bigint primary key auto_increment comment '主键',
    name varchar(40) not null comment '字典名称',
    dict_enable bit(1) not null comment '字典是否启用：0-禁用，1-启用',
    create_user varchar(15) default null comment '创建者',
    create_time datetime default current_timestamp comment '创建时间',
    update_user varchar(15) default null comment '更新者',
    update_time datetime default current_timestamp comment '更新时间',
    constraint uniq_dict_type_name unique (name)
) comment '字典类型表';

create table t_dict_data(
    id bigint primary key auto_increment comment '主键',
    type_id bigint not null comment '字典类型id',
    label varchar(15) not null comment '字典键',
    data int not null comment '字典值',
    sort_id int default 0 comment '字典排序',
    create_user varchar(15) default null comment '创建者',
    create_time datetime not null default current_timestamp comment '创建时间',
    update_user varchar(15) default null comment '更新者',
    update_time datetime default current_timestamp comment '更新时间',
    constraint uniq_dict_type_label unique (type_id, label)
) comment '字典数据表';

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

create table t_codegen_table  (
  id bigint primary key auto_increment comment '主键',
  data_source_id bigint not null comment '数据源配置的编号',
  parent_menu_id bigint not null comment '上级菜单编号',
  table_name varchar(50) not null comment '表名称',
  table_comment varchar(100) not null comment '表描述',
  class_name varchar(100) not null comment '类名称',
  java_base_package varchar(50) not null comment 'java基础包名',
  author varchar(20) not null comment '作者',
  business_name varchar(50) not null comment '业务名称',
  ignore_table_prefix varchar(20) default null comment '忽略表前缀',
  ignore_column_prefix varchar(20) default null comment '忽略字段前缀',
  create_user varchar(15) default null comment '创建者',
  create_time datetime default current_timestamp comment '创建时间',
  update_user varchar(15) default null comment '更新者',
  update_time datetime default current_timestamp comment '更新时间',
  constraint uniq_table_name unique (table_name)
) comment = '代码生成表';

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
  html_type varchar(20) not null comment 'html类型',
  sort_id int not null comment '排序',
  dict_type_id bigint default null comment '字典类型编号',
  select_condition varchar(20) default null comment 'select字段条件',
  primary_key bit(1) not null comment '是否主键',
  nullable bit(1) not null comment '是否允许为空',
  select_condition_field bit(1) not null comment '是否为select条件字段',
  select_result_field bit(1) not null comment '是否select结果字段',
  insert_field bit(1) not null comment '是否为insert字段',
  update_field bit(1) not null comment '是否为update字段',
  create_user varchar(15) default null comment '创建者',
  create_time datetime default current_timestamp comment '创建时间',
  update_user varchar(15) default null comment '更新者',
  update_time datetime default current_timestamp comment '更新时间'
) comment = '代码生成字段表';

create table t_job  (
  id bigint primary key auto_increment comment '主键',
  name varchar(32) not null comment '任务名称',
  handler_name varchar(32) not null comment '处理器名称',
  handler_param varchar(500) default null comment '处理器参数',
  cron_expression varchar(32) not null comment 'cron 表达式',
  retry_count int default 0 comment '重试次数',
  retry_interval int default 0 comment '重试间隔',
  job_enable bit(1) default 0 comment '是否启用',
  create_user varchar(15) default null comment '创建者',
  create_time datetime default current_timestamp comment '创建时间',
  update_user varchar(15) default null comment '更新者',
  update_time datetime default current_timestamp comment '更新时间'
) comment = '定时任务表';

create table t_job_log  (
  id bigint primary key auto_increment comment '主键',
  job_id bigint not null comment '任务编号',
  handler_name varchar(32) not null comment '处理器名称',
  handler_param varchar(500) default null comment '处理器参数',
  fire_num tinyint not null comment '第几次执行',
  time bigint default null comment '执行耗时',
  status tinyint not null comment '任务状态',
  result text default null comment '结果数据',
  create_time datetime default current_timestamp comment '创建时间',
  update_time datetime default current_timestamp comment '更新时间',
  index idx_job_log_create_time (create_time desc)
) comment = '定时任务日志表';

create table t_chat_history (
    id bigint primary key auto_increment comment '主键',
    user_id bigint not null comment '用户id',
    conversation_id varchar(25) not null comment '会话id',
    title varchar(255) not null comment '标题',
    create_time timestamp default CURRENT_TIMESTAMP comment '创建时间',
    update_time timestamp default CURRENT_TIMESTAMP comment '更新时间'
) comment 'AI对话记录表';

-- spring ai chat memory
CREATE TABLE IF NOT EXISTS SPRING_AI_CHAT_MEMORY (
    `conversation_id` VARCHAR(36) NOT NULL,
    `content` TEXT NOT NULL,
    `type` ENUM('USER', 'ASSISTANT', 'SYSTEM', 'TOOL') NOT NULL,
    `timestamp` TIMESTAMP NOT NULL,
    INDEX `SPRING_AI_CHAT_MEMORY_CONVERSATION_ID_TIMESTAMP_IDX` (`conversation_id`, `timestamp`)
);