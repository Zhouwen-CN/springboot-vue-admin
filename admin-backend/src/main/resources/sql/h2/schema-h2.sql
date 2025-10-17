/*
    number 18 对应 mysql bigint 类型
    number 9  对应 mysql int 类型
    link: com.baomidou.mybatisplus.generator.type.TypeRegistry
 */

-- 用户表
CREATE TABLE t_user (
    id            NUMBER(18) PRIMARY KEY,
    username      VARCHAR2(15) NOT NULL,
    password      VARCHAR2(60) NOT NULL,
    token_version NUMBER(18) DEFAULT 0,
    create_user   VARCHAR2(15) DEFAULT NULL,
    create_time   DATE DEFAULT SYSDATE,
    update_user   VARCHAR2(15) DEFAULT NULL,
    update_time   DATE DEFAULT SYSDATE,
    CONSTRAINT uniq_user_name UNIQUE (username)
);

COMMENT ON TABLE t_user IS '用户表';
COMMENT ON COLUMN t_user.id IS '主键';
COMMENT ON COLUMN t_user.username IS '用户名';
COMMENT ON COLUMN t_user.password IS '密码';
COMMENT ON COLUMN t_user.token_version IS 'token版本';
COMMENT ON COLUMN t_user.create_user IS '创建者';
COMMENT ON COLUMN t_user.create_time IS '创建时间';
COMMENT ON COLUMN t_user.update_user IS '更新者';
COMMENT ON COLUMN t_user.update_time IS '更新时间';

CREATE SEQUENCE t_user_seq
START WITH 2
INCREMENT BY 1;

-- 用户角色关系表
CREATE TABLE t_user_role (
    id            NUMBER(18) PRIMARY KEY,
    user_id       NUMBER(18) NOT NULL,
    role_id       NUMBER(18) NOT NULL,
    create_user   VARCHAR2(15) DEFAULT NULL,
    create_time   DATE DEFAULT SYSDATE,
    update_user   VARCHAR2(15) DEFAULT NULL,
    update_time   DATE DEFAULT SYSDATE
);

COMMENT ON TABLE t_user_role IS '用户角色关系表';
COMMENT ON COLUMN t_user_role.id IS '主键';
COMMENT ON COLUMN t_user_role.user_id IS '用户id';
COMMENT ON COLUMN t_user_role.role_id IS '角色id';
COMMENT ON COLUMN t_user_role.create_user IS '创建者';
COMMENT ON COLUMN t_user_role.create_time IS '创建时间';
COMMENT ON COLUMN t_user_role.update_user IS '更新者';
COMMENT ON COLUMN t_user_role.update_time IS '更新时间';

CREATE SEQUENCE t_user_role_seq
START WITH 2
INCREMENT BY 1;

-- 角色表
CREATE TABLE t_role (
    id            NUMBER(18) PRIMARY KEY,
    role_name     VARCHAR2(15) NOT NULL,
    description   VARCHAR2(40) NOT NULL,
    create_user   VARCHAR2(15) DEFAULT NULL,
    create_time   DATE DEFAULT SYSDATE,
    update_user   VARCHAR2(15) DEFAULT NULL,
    update_time   DATE DEFAULT SYSDATE,
    CONSTRAINT uniq_role_name UNIQUE (role_name)
);

COMMENT ON TABLE t_role IS '角色表';
COMMENT ON COLUMN t_role.id IS '主键';
COMMENT ON COLUMN t_role.role_name IS '角色名称';
COMMENT ON COLUMN t_role.description IS '角色说明';
COMMENT ON COLUMN t_role.create_user IS '创建者';
COMMENT ON COLUMN t_role.create_time IS '创建时间';
COMMENT ON COLUMN t_role.update_user IS '更新者';
COMMENT ON COLUMN t_role.update_time IS '更新时间';

CREATE SEQUENCE t_role_seq
START WITH 7
INCREMENT BY 1;

-- 用户菜单关系表
CREATE TABLE t_role_menu (
    id            NUMBER(18) PRIMARY KEY,
    role_id       NUMBER(18) NOT NULL,
    menu_id       NUMBER(18) NOT NULL,
    create_user   VARCHAR2(15) DEFAULT NULL,
    create_time   DATE DEFAULT SYSDATE,
    update_user   VARCHAR2(15) DEFAULT NULL,
    update_time   DATE DEFAULT SYSDATE
);

COMMENT ON TABLE t_role_menu IS '角色菜单关系表';
COMMENT ON COLUMN t_role_menu.id IS '主键';
COMMENT ON COLUMN t_role_menu.role_id IS '角色id';
COMMENT ON COLUMN t_role_menu.menu_id IS '菜单id';
COMMENT ON COLUMN t_role_menu.create_user IS '创建者';
COMMENT ON COLUMN t_role_menu.create_time IS '创建时间';
COMMENT ON COLUMN t_role_menu.update_user IS '更新者';
COMMENT ON COLUMN t_role_menu.update_time IS '更新时间';

CREATE SEQUENCE t_role_menu_seq
START WITH 18
INCREMENT BY 1;

-- 菜单表
CREATE TABLE t_menu (
    id            NUMBER(18) PRIMARY KEY,
    title         VARCHAR2(15) NOT NULL,
    access_path   VARCHAR2(40) NOT NULL,
    icon          VARCHAR2(15) NOT NULL,
    pid           NUMBER(18) NOT NULL,
    keep_alive    NUMBER(1) DEFAULT 0,
    menu_type     NUMBER(1) NOT NULL,
    sort_id       NUMBER(9) DEFAULT 0,
    create_user   VARCHAR2(15) DEFAULT NULL,
    create_time   DATE DEFAULT SYSDATE,
    update_user   VARCHAR2(15) DEFAULT NULL,
    update_time   DATE DEFAULT SYSDATE,
    CONSTRAINT uniq_enum_title UNIQUE (title)
);

COMMENT ON TABLE t_menu IS '菜单表';
COMMENT ON COLUMN t_menu.id IS '主键';
COMMENT ON COLUMN t_menu.title IS '标题';
COMMENT ON COLUMN t_menu.access_path IS '访问路径';
COMMENT ON COLUMN t_menu.icon IS '图标';
COMMENT ON COLUMN t_menu.pid IS '父级菜单id';
COMMENT ON COLUMN t_menu.keep_alive IS '是否缓存';
COMMENT ON COLUMN t_menu.menu_type IS '菜单类型：0-目录，1-菜单';
COMMENT
ON COLUMN t_menu.sort_id IS '菜单排序';
COMMENT ON COLUMN t_menu.create_user IS '创建者';
COMMENT ON COLUMN t_menu.create_time IS '创建时间';
COMMENT ON COLUMN t_menu.update_user IS '更新者';
COMMENT ON COLUMN t_menu.update_time IS '更新时间';

CREATE SEQUENCE t_menu_seq
START WITH 18
INCREMENT BY 1;

-- 登录日志表
CREATE TABLE t_login_log (
    id            NUMBER(18) PRIMARY KEY,
    operation     NUMBER(1) NOT NULL,
    status        NUMBER(1) NOT NULL,
    ip            VARCHAR2(32) NOT NULL,
    user_agent    VARCHAR2(200) NOT NULL,
    create_user   VARCHAR2(15) DEFAULT NULL,
    create_time   DATE DEFAULT SYSDATE,
    CONSTRAINT pk_login_log PRIMARY KEY (id)
);

CREATE INDEX idx_login_create_time ON t_login_log(create_time DESC);

COMMENT ON TABLE t_login_log IS '登录日志表';
COMMENT ON COLUMN t_login_log.id IS '主键';
COMMENT ON COLUMN t_login_log.operation IS '操作类型';
COMMENT ON COLUMN t_login_log.status IS '操作状态';
COMMENT ON COLUMN t_login_log.ip IS 'IP地址';
COMMENT ON COLUMN t_login_log.user_agent IS '用户代理';
COMMENT ON COLUMN t_login_log.create_user IS '创建者';
COMMENT ON COLUMN t_login_log.create_time IS '创建时间';

CREATE SEQUENCE t_login_log_seq
START WITH 1
INCREMENT BY 1;

-- 操作日志表
CREATE TABLE t_operation_log (
    id            NUMBER(18) PRIMARY KEY,
    operation     VARCHAR2(50) NOT NULL,
    url           VARCHAR2(50) NOT NULL,
    method        NUMBER(1) NOT NULL,
    params        CLOB DEFAULT NULL,
    time          NUMBER(18) NOT NULL,
    status        NUMBER(1) NOT NULL,
    ip            VARCHAR2(32) NOT NULL,
    user_agent    VARCHAR2(200) NOT NULL,
    create_user   VARCHAR2(15) DEFAULT NULL,
    create_time   DATE DEFAULT SYSDATE
);

CREATE INDEX idx_ops_create_time ON t_operation_log(create_time DESC);

COMMENT ON TABLE t_operation_log IS '操作日志表';
COMMENT ON COLUMN t_operation_log.id IS '主键';
COMMENT ON COLUMN t_operation_log.operation IS '操作';
COMMENT ON COLUMN t_operation_log.url IS '请求地址';
COMMENT ON COLUMN t_operation_log.method IS '请求方式';
COMMENT ON COLUMN t_operation_log.params IS '请求参数';
COMMENT ON COLUMN t_operation_log.time IS '请求耗时';
COMMENT ON COLUMN t_operation_log.status IS '操作状态';
COMMENT ON COLUMN t_operation_log.ip IS 'ip地址';
COMMENT ON COLUMN t_operation_log.user_agent IS '用户代理';
COMMENT ON COLUMN t_operation_log.create_user IS '创建者';
COMMENT ON COLUMN t_operation_log.create_time IS '创建时间';

CREATE SEQUENCE t_operation_log_seq
START WITH 1
INCREMENT BY 1;

-- 错误日志表
CREATE TABLE t_error_log (
    id            NUMBER(18) PRIMARY KEY,
    url           VARCHAR2(50) NOT NULL,
    method        NUMBER(1) NOT NULL,
    params        CLOB DEFAULT NULL,
    ip            VARCHAR2(32) NOT NULL,
    user_agent    VARCHAR2(200) NOT NULL,
    error_msg     CLOB NOT NULL,
    create_user   VARCHAR2(15) DEFAULT NULL,
    create_time   DATE DEFAULT SYSDATE,
    CONSTRAINT pk_error_log PRIMARY KEY (id)
);

CREATE INDEX idx_error_create_time ON t_error_log(create_time DESC);

COMMENT ON TABLE t_error_log IS '错误日志表';
COMMENT ON COLUMN t_error_log.id IS '主键';
COMMENT ON COLUMN t_error_log.url IS '请求地址';
COMMENT ON COLUMN t_error_log.method IS '请求方式';
COMMENT ON COLUMN t_error_log.params IS '请求参数';
COMMENT ON COLUMN t_error_log.ip IS 'IP地址';
COMMENT ON COLUMN t_error_log.user_agent IS '用户代理';
COMMENT ON COLUMN t_error_log.error_msg IS '错误信息';
COMMENT ON COLUMN t_error_log.create_user IS '创建者';
COMMENT ON COLUMN t_error_log.create_time IS '创建时间';

CREATE SEQUENCE t_error_log_seq
START WITH 1
INCREMENT BY 1;

-- 字典类型表
CREATE TABLE t_dict_type (
    id            NUMBER(18) PRIMARY KEY,
    dict_type     VARCHAR2(15) NOT NULL,
    name          VARCHAR2(40) NOT NULL,
    create_user   VARCHAR2(15) DEFAULT NULL,
    create_time   DATE DEFAULT SYSDATE,
    update_user   VARCHAR2(15) DEFAULT NULL,
    update_time   DATE DEFAULT SYSDATE,
    CONSTRAINT uniq_dict_type UNIQUE (dict_type)
);

COMMENT ON TABLE t_dict_type IS '字典类型表';
COMMENT ON COLUMN t_dict_type.id IS '主键';
COMMENT ON COLUMN t_dict_type.dict_type IS '字典类型';
COMMENT ON COLUMN t_dict_type.name IS '字典名称';
COMMENT ON COLUMN t_dict_type.create_user IS '创建者';
COMMENT ON COLUMN t_dict_type.create_time IS '创建时间';
COMMENT ON COLUMN t_dict_type.update_user IS '更新者';
COMMENT ON COLUMN t_dict_type.update_time IS '更新时间';

CREATE SEQUENCE t_dict_type_seq
START WITH 2
INCREMENT BY 1;


-- 字典数据表
CREATE TABLE t_dict_data (
    id            NUMBER(18) PRIMARY KEY,
    type_id       NUMBER(18) NOT NULL,
    label         VARCHAR2(15) NOT NULL,
    data          NUMBER(9) NOT NULL,
    sort_id       NUMBER(9) DEFAULT 0,
    create_user   VARCHAR2(15) DEFAULT NULL,
    create_time   DATE DEFAULT SYSDATE,
    update_user   VARCHAR2(15) DEFAULT NULL,
    update_time   DATE DEFAULT SYSDATE,
    CONSTRAINT uniq_dict_type_label UNIQUE (type_id, label)
);

COMMENT ON TABLE t_dict_data IS '字典数据表';
COMMENT ON COLUMN t_dict_data.id IS '主键';
COMMENT ON COLUMN t_dict_data.type_id IS '字典类型id';
COMMENT ON COLUMN t_dict_data.label IS '字典键';
COMMENT ON COLUMN t_dict_data.data IS '字典值';
COMMENT ON COLUMN t_dict_data.sort_id IS '字典排序';
COMMENT ON COLUMN t_dict_data.create_user IS '创建者';
COMMENT ON COLUMN t_dict_data.create_time IS '创建时间';
COMMENT ON COLUMN t_dict_data.update_user IS '更新者';
COMMENT ON COLUMN t_dict_data.update_time IS '更新时间';

CREATE SEQUENCE t_dict_data_seq
START WITH 3
INCREMENT BY 1;

-- 数据源配置表
CREATE TABLE t_data_source (
    id            NUMBER(18) PRIMARY KEY,
    name          VARCHAR2(15) NOT NULL,
    url           VARCHAR2(200) NOT NULL,
    username      VARCHAR2(15) NOT NULL,
    password      VARCHAR2(15) NOT NULL,
    create_user   VARCHAR2(15) DEFAULT NULL,
    create_time   DATE DEFAULT SYSDATE,
    update_user   VARCHAR2(15) DEFAULT NULL,
    update_time   DATE DEFAULT SYSDATE,
    CONSTRAINT uniq_datasource_name UNIQUE (name)
);

COMMENT ON TABLE t_data_source IS '数据源配置表';
COMMENT ON COLUMN t_data_source.id IS '主键';
COMMENT ON COLUMN t_data_source.name IS '数据源名称';
COMMENT ON COLUMN t_data_source.url IS '数据源连接';
COMMENT ON COLUMN t_data_source.username IS '用户名';
COMMENT ON COLUMN t_data_source.password IS '密码';
COMMENT ON COLUMN t_data_source.create_user IS '创建者';
COMMENT ON COLUMN t_data_source.create_time IS '创建时间';
COMMENT ON COLUMN t_data_source.update_user IS '更新者';
COMMENT ON COLUMN t_data_source.update_time IS '更新时间';

CREATE SEQUENCE t_data_source_seq
START WITH 1
INCREMENT BY 1;

-- 代码生成表
create table t_codegen_table(
  id NUMBER(18) primary key,
  data_source_id NUMBER(18) not null,
  parent_menu_id NUMBER(18) not null,
  table_name varchar2(50) not null,
  table_comment varchar2(100) not null,
  class_name varchar2(100) not null,
  author varchar2(20) not null,
  business_name varchar2(50) not null,
  ignore_table_prefix varchar2(20) default null,
  ignore_column_prefix varchar2(20) default null,
  create_user varchar2(15) default null,
  create_time DATE default SYSDATE,
  update_user varchar2(15) default null,
  update_time DATE default SYSDATE,
  CONSTRAINT uniq_table_name UNIQUE (table_name)
);

COMMENT ON TABLE t_codegen_table IS '代码生成表';
COMMENT ON COLUMN t_codegen_table.id IS '主键';
COMMENT ON COLUMN t_codegen_table.data_source_id IS '数据源配置编号';
COMMENT ON COLUMN t_codegen_table.parent_menu_id IS '上级菜单编号';
COMMENT ON COLUMN t_codegen_table.table_name IS '表名称';
COMMENT ON COLUMN t_codegen_table.table_comment IS '表描述';
COMMENT ON COLUMN t_codegen_table.class_name IS '类名称';
COMMENT ON COLUMN t_codegen_table.author IS '作者';
COMMENT ON COLUMN t_codegen_table.business_name IS '业务名称';
COMMENT ON COLUMN t_codegen_table.ignore_table_prefix IS '忽略表前缀';
COMMENT ON COLUMN t_codegen_table.ignore_column_prefix IS '忽略字段前缀';
COMMENT ON COLUMN t_codegen_table.create_user IS '创建者';
COMMENT ON COLUMN t_codegen_table.create_time IS '创建时间';
COMMENT ON COLUMN t_codegen_table.update_user IS '更新者';
COMMENT ON COLUMN t_codegen_table.update_time IS '更新时间';

CREATE SEQUENCE t_codegen_table_seq
START WITH 1
INCREMENT BY 1;

-- 代码生成字段表
create table t_codegen_column(
  id NUMBER(18) primary key,
  table_id NUMBER(18) not null,
  column_name varchar2(50) not null,
  column_comment varchar2(100) not null,
  db_type varchar2(20) not null,
  column_length NUMBER(9) default null,
  java_field varchar2(50) not null,
  java_type varchar2(20) not null,
  js_type varchar2(20) not null,
  html_type varchar2(20) not null,
  sort_id NUMBER(9) not null,
  dict_type_id number(18) default null,
  select_condition varchar2(20) default null,
  primary_key number(1) not null,
  nullable number(1) not null,
  select_condition_field number(1) not null,
  select_result_field number(1) not null,
  insert_field number(1) not null,
  update_field number(1) not null,
  create_user varchar2(15) default null,
  create_time DATE default SYSDATE,
  update_user varchar2(15) default null,
  update_time DATE default SYSDATE
);

COMMENT ON TABLE t_codegen_column IS '代码生成字段表';
COMMENT ON COLUMN t_codegen_column.id IS '主键';
COMMENT ON COLUMN t_codegen_column.table_id IS '代码生成表编号';
COMMENT ON COLUMN t_codegen_column.column_name IS '字段名';
COMMENT ON COLUMN t_codegen_column.column_comment IS '字段描述';
COMMENT ON COLUMN t_codegen_column.db_type IS '数据库类型';
COMMENT ON COLUMN t_codegen_column.column_length IS '字段长度';
COMMENT ON COLUMN t_codegen_column.java_field IS 'java属性名';
COMMENT ON COLUMN t_codegen_column.java_type IS 'java类型';
COMMENT ON COLUMN t_codegen_column.js_type IS 'js类型';
COMMENT ON COLUMN t_codegen_column.html_type IS 'html类型';
COMMENT ON COLUMN t_codegen_column.sort_id IS '排序';
COMMENT ON COLUMN t_codegen_column.dict_type_id IS '字典类型编号';
COMMENT ON COLUMN t_codegen_column.select_condition IS '查询条件';
COMMENT ON COLUMN t_codegen_column.primary_key IS '是否主键';
COMMENT ON COLUMN t_codegen_column.nullable IS '是否允许为空';
COMMENT ON COLUMN t_codegen_column.select_condition_field IS '是否为select条件字段';
COMMENT ON COLUMN t_codegen_column.select_result_field IS '是否为select结果字段';
COMMENT ON COLUMN t_codegen_column.insert_field IS '是否为insert字段';
COMMENT ON COLUMN t_codegen_column.update_field IS '是否为update字段';
COMMENT ON COLUMN t_codegen_column.create_user IS '创建者';
COMMENT ON COLUMN t_codegen_column.create_time IS '创建时间';
COMMENT ON COLUMN t_codegen_column.update_user IS '更新者';
COMMENT ON COLUMN t_codegen_column.update_time IS '更新时间';

CREATE SEQUENCE t_codegen_column_seq
START WITH 1
INCREMENT BY 1;

create table t_job(
  id NUMBER(18) primary key,
  name varchar2(32) not null,
  handler_name varchar2(32) not null,
  handler_param varchar2(500) default null,
  cron_expression varchar2(32) not null,
  retry_count NUMBER(9) default 0,
  retry_interval NUMBER(9) default 0,
  job_enable number(1) default 0,
  create_user varchar2(15) default null,
  create_time DATE default SYSDATE,
  update_user varchar2(15) default null,
  update_time DATE default SYSDATE
);
COMMENT ON TABLE t_job IS '定时任务表';
COMMENT ON COLUMN t_job.id IS '主键';
COMMENT ON COLUMN t_job.name IS '任务名称';
COMMENT ON COLUMN t_job.handler_name IS '处理器名称';
COMMENT ON COLUMN t_job.handler_param IS '处理器参数';
COMMENT ON COLUMN t_job.cron_expression IS 'cron 表达式';
COMMENT ON COLUMN t_job.retry_count IS '重试次数';
COMMENT ON COLUMN t_job.retry_interval IS '重试间隔';
COMMENT ON COLUMN t_job.job_enable IS '是否启用';
COMMENT ON COLUMN t_job.create_user IS '创建者';
COMMENT ON COLUMN t_job.create_time IS '创建时间';
COMMENT ON COLUMN t_job.update_user IS '更新者';
COMMENT ON COLUMN t_job.update_time IS '更新时间';

CREATE SEQUENCE t_job_seq
START WITH 1
INCREMENT BY 1;

create table t_job_log(
  id NUMBER(18) primary key,
  job_id NUMBER(18) not null,
  handler_name varchar2(32) not null,
  handler_param varchar2(500) default null,
  fire_num NUMBER(1) not null,
  time NUMBER(18) default null,
  status NUMBER(1) not null,
  result varchar2(500) default null,
  create_time DATE default SYSDATE,
  update_time DATE default SYSDATE
);
COMMENT ON TABLE t_job_log IS '定时任务日志表';
COMMENT ON COLUMN t_job_log.id IS '主键';
COMMENT ON COLUMN t_job_log.job_id IS '任务编号';
COMMENT ON COLUMN t_job_log.handler_name IS '处理器名称';
COMMENT ON COLUMN t_job_log.handler_param IS '处理器参数';
COMMENT ON COLUMN t_job_log.fire_num IS '第几次执行';
COMMENT ON COLUMN t_job_log.time IS '执行耗时';
COMMENT ON COLUMN t_job_log.status IS '任务状态';
COMMENT ON COLUMN t_job_log.result IS '结果数据';
COMMENT ON COLUMN t_job_log.create_time IS '创建时间';
COMMENT ON COLUMN t_job_log.update_time IS '更新时间';

CREATE INDEX idx_job_create_time ON t_job_log(create_time DESC);

CREATE SEQUENCE t_job_log_seq
START WITH 1
INCREMENT BY 1;