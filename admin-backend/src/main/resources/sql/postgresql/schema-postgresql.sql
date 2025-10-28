-- 用户表
CREATE TABLE t_user (
    id            BIGINT PRIMARY KEY,
    username      VARCHAR(15) NOT NULL,
    password      VARCHAR(60) NOT NULL,
    token_version BIGINT DEFAULT 0,
    create_user   VARCHAR(15) DEFAULT NULL,
    create_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_user   VARCHAR(15) DEFAULT NULL,
    update_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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
    id            BIGINT PRIMARY KEY,
    user_id       BIGINT NOT NULL,
    role_id       BIGINT NOT NULL,
    create_user   VARCHAR(15) DEFAULT NULL,
    create_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_user   VARCHAR(15) DEFAULT NULL,
    update_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
    id            BIGINT PRIMARY KEY,
    role_name     VARCHAR(15) NOT NULL,
    description   VARCHAR(40) NOT NULL,
    create_user   VARCHAR(15) DEFAULT NULL,
    create_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_user   VARCHAR(15) DEFAULT NULL,
    update_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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

-- 角色菜单关系表
CREATE TABLE t_role_menu (
    id            BIGINT PRIMARY KEY,
    role_id       BIGINT NOT NULL,
    menu_id       BIGINT NOT NULL,
    create_user   VARCHAR(15) DEFAULT NULL,
    create_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_user   VARCHAR(15) DEFAULT NULL,
    update_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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
START WITH 19
INCREMENT BY 1;

-- 菜单表
CREATE TABLE t_menu (
    id            BIGINT PRIMARY KEY,
    title         VARCHAR(20) NOT NULL,
    access_path   VARCHAR(40) NOT NULL,
    icon          VARCHAR(15) NOT NULL,
    pid           BIGINT NOT NULL,
    keep_alive    BOOLEAN NOT NULL,
    menu_type     SMALLINT NOT NULL,
    sort_id       INTEGER DEFAULT 0,
    create_user   VARCHAR(15) DEFAULT NULL,
    create_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_user   VARCHAR(15) DEFAULT NULL,
    update_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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
COMMENT ON COLUMN t_menu.sort_id IS '菜单排序';
COMMENT ON COLUMN t_menu.create_user IS '创建者';
COMMENT ON COLUMN t_menu.create_time IS '创建时间';
COMMENT ON COLUMN t_menu.update_user IS '更新者';
COMMENT ON COLUMN t_menu.update_time IS '更新时间';

CREATE SEQUENCE t_menu_seq
START WITH 19
INCREMENT BY 1;

-- 登录日志表
CREATE TABLE t_login_log (
    id            BIGINT PRIMARY KEY,
    operation     SMALLINT NOT NULL,
    status        SMALLINT NOT NULL,
    ip            VARCHAR(32) NOT NULL,
    user_agent    VARCHAR(200) NOT NULL,
    create_user   VARCHAR(15) DEFAULT NULL,
    create_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_login_log_create_time ON t_login_log(create_time DESC);

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
    id            BIGINT PRIMARY KEY,
    operation     VARCHAR(50) NOT NULL,
    url           VARCHAR(50) NOT NULL,
    method        SMALLINT NOT NULL,
    params        TEXT DEFAULT NULL,
    time          BIGINT NOT NULL,
    status        SMALLINT NOT NULL,
    ip            VARCHAR(32) NOT NULL,
    user_agent    VARCHAR(200) NOT NULL,
    create_user   VARCHAR(15) DEFAULT NULL,
    create_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_ops_log_create_time ON t_operation_log(create_time DESC);

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
    id            BIGINT PRIMARY KEY,
    url           VARCHAR(50) NOT NULL,
    method        SMALLINT NOT NULL,
    params        TEXT DEFAULT NULL,
    ip            VARCHAR(32) NOT NULL,
    user_agent    VARCHAR(200) NOT NULL,
    error_msg     TEXT NOT NULL,
    create_user   VARCHAR(15) DEFAULT NULL,
    create_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_error_log_create_time ON t_error_log(create_time DESC);

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
    id            BIGINT PRIMARY KEY,
    name          VARCHAR(40) NOT NULL,
    dict_enable   BOOLEAN NOT NULL,
    create_user   VARCHAR(15) DEFAULT NULL,
    create_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_user   VARCHAR(15) DEFAULT NULL,
    update_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uniq_dict_type_name UNIQUE (name)
);

COMMENT ON TABLE t_dict_type IS '字典类型表';
COMMENT ON COLUMN t_dict_type.id IS '主键';
COMMENT ON COLUMN t_dict_type.name IS '字典名称';
COMMENT ON COLUMN t_dict_type.dict_enable IS '字典是否启用：0-禁用，1-启用';
COMMENT ON COLUMN t_dict_type.create_user IS '创建者';
COMMENT ON COLUMN t_dict_type.create_time IS '创建时间';
COMMENT ON COLUMN t_dict_type.update_user IS '更新者';
COMMENT ON COLUMN t_dict_type.update_time IS '更新时间';

CREATE SEQUENCE t_dict_type_seq
START WITH 2
INCREMENT BY 1;

-- 字典数据表
CREATE TABLE t_dict_data (
    id            BIGINT PRIMARY KEY,
    type_id       BIGINT NOT NULL,
    label         VARCHAR(15) NOT NULL,
    data          INTEGER NOT NULL,
    sort_id       INTEGER DEFAULT 0,
    create_user   VARCHAR(15) DEFAULT NULL,
    create_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_user   VARCHAR(15) DEFAULT NULL,
    update_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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
    id            BIGINT PRIMARY KEY,
    name          VARCHAR(15) NOT NULL,
    url           VARCHAR(200) NOT NULL,
    username      VARCHAR(15) NOT NULL,
    password      VARCHAR(15) NOT NULL,
    create_user   VARCHAR(15) DEFAULT NULL,
    create_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_user   VARCHAR(15) DEFAULT NULL,
    update_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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
CREATE TABLE t_codegen_table (
    id                    BIGINT PRIMARY KEY,
    data_source_id        BIGINT NOT NULL,
    parent_menu_id        BIGINT NOT NULL,
    table_name            VARCHAR(50) NOT NULL,
    table_comment         VARCHAR(100) NOT NULL,
    class_name            VARCHAR(100) NOT NULL,
    author                VARCHAR(20) NOT NULL,
    business_name         VARCHAR(50) NOT NULL,
    ignore_table_prefix   VARCHAR(20) DEFAULT NULL,
    ignore_column_prefix  VARCHAR(20) DEFAULT NULL,
    create_user           VARCHAR(15) DEFAULT NULL,
    create_time           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_user           VARCHAR(15) DEFAULT NULL,
    update_time           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
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
CREATE TABLE t_codegen_column (
    id                    BIGINT PRIMARY KEY,
    table_id              BIGINT NOT NULL,
    column_name           VARCHAR(50) NOT NULL,
    column_comment        VARCHAR(100) NOT NULL,
    db_type               VARCHAR(20) NOT NULL,
    column_length         INTEGER DEFAULT NULL,
    java_field            VARCHAR(50) NOT NULL,
    java_type             VARCHAR(20) NOT NULL,
    js_type               VARCHAR(20) NOT NULL,
    html_type             VARCHAR(20) NOT NULL,
    sort_id               INTEGER NOT NULL,
    dict_type_id          BIGINT DEFAULT NULL,
    select_condition      VARCHAR(20) DEFAULT NULL,
    primary_key           BOOLEAN NOT NULL,
    nullable              BOOLEAN NOT NULL,
    select_condition_field BOOLEAN NOT NULL,
    select_result_field   BOOLEAN NOT NULL,
    insert_field          BOOLEAN NOT NULL,
    update_field          BOOLEAN NOT NULL,
    create_user           VARCHAR(15) DEFAULT NULL,
    create_time           TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_user           VARCHAR(15) DEFAULT NULL,
    update_time           TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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

-- 定时任务表
CREATE TABLE t_job (
    id              BIGINT PRIMARY KEY,
    name            VARCHAR(32) NOT NULL,
    handler_name    VARCHAR(32) NOT NULL,
    handler_param   VARCHAR(500) DEFAULT NULL,
    cron_expression VARCHAR(32) NOT NULL,
    retry_count     INTEGER DEFAULT 0,
    retry_interval  INTEGER DEFAULT 0,
    job_enable      BOOLEAN DEFAULT false,
    create_user     VARCHAR(15) DEFAULT NULL,
    create_time     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_user     VARCHAR(15) DEFAULT NULL,
    update_time     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
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

-- 定时任务日志表
CREATE TABLE t_job_log (
    id            BIGINT PRIMARY KEY,
    job_id        BIGINT NOT NULL,
    handler_name  VARCHAR(32) NOT NULL,
    handler_param VARCHAR(500) DEFAULT NULL,
    fire_num      SMALLINT NOT NULL,
    time          BIGINT DEFAULT NULL,
    status        SMALLINT NOT NULL,
    result        TEXT DEFAULT NULL,
    create_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_job_log_create_time ON t_job_log(create_time DESC);

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

CREATE SEQUENCE t_job_log_seq
START WITH 1
INCREMENT BY 1;