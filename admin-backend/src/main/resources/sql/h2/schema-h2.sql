-- 用户表
CREATE TABLE t_user (
    id            NUMBER(20) PRIMARY KEY,
    username      VARCHAR2(15) NOT NULL,
    password      VARCHAR2(60) NOT NULL,
    token_version NUMBER(20) DEFAULT 0 NOT NULL,
    create_time   DATE DEFAULT SYSDATE NOT NULL,
    update_time   DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT uniq_username UNIQUE (username)
);

COMMENT ON TABLE t_user IS '用户表';
COMMENT ON COLUMN t_user.id IS '主键';
COMMENT ON COLUMN t_user.username IS '用户名';
COMMENT ON COLUMN t_user.password IS '密码';
COMMENT ON COLUMN t_user.token_version IS 'token版本';
COMMENT ON COLUMN t_user.create_time IS '创建时间';
COMMENT ON COLUMN t_user.update_time IS '更新时间';

CREATE SEQUENCE t_user_seq
START WITH 2
INCREMENT BY 1;

-- 用户角色关系表
CREATE TABLE t_user_role (
    id            NUMBER(20) PRIMARY KEY,
    user_id       NUMBER(20) NOT NULL,
    role_id       NUMBER(20) NOT NULL,
    create_time   DATE DEFAULT SYSDATE NOT NULL,
    update_time   DATE DEFAULT SYSDATE NOT NULL
);

COMMENT ON TABLE t_user_role IS '用户角色关系表';
COMMENT ON COLUMN t_user_role.id IS '主键';
COMMENT ON COLUMN t_user_role.user_id IS '用户id';
COMMENT ON COLUMN t_user_role.role_id IS '角色id';
COMMENT ON COLUMN t_user_role.create_time IS '创建时间';
COMMENT ON COLUMN t_user_role.update_time IS '更新时间';

CREATE SEQUENCE t_user_role_seq
START WITH 2
INCREMENT BY 1;

-- 角色表
CREATE TABLE t_role (
    id            NUMBER(20) PRIMARY KEY,
    role_name     VARCHAR2(15) NOT NULL,
    description   VARCHAR2(40) NOT NULL,
    create_time   DATE DEFAULT SYSDATE NOT NULL,
    update_time   DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT uniq_role_name UNIQUE (role_name)
);

COMMENT ON TABLE t_role IS '角色表';
COMMENT ON COLUMN t_role.id IS '主键';
COMMENT ON COLUMN t_role.role_name IS '角色名称';
COMMENT ON COLUMN t_role.description IS '角色说明';
COMMENT ON COLUMN t_role.create_time IS '创建时间';
COMMENT ON COLUMN t_role.update_time IS '更新时间';

CREATE SEQUENCE t_role_seq
START WITH 7
INCREMENT BY 1;

-- 用户菜单关系表
CREATE TABLE t_role_menu (
    id            NUMBER(20) PRIMARY KEY,
    role_id       NUMBER(20) NOT NULL,
    menu_id       NUMBER(20) NOT NULL,
    create_time   DATE DEFAULT SYSDATE NOT NULL,
    update_time   DATE DEFAULT SYSDATE NOT NULL
);

COMMENT ON TABLE t_role_menu IS '用户菜单关系表';
COMMENT ON COLUMN t_role_menu.id IS '主键';
COMMENT ON COLUMN t_role_menu.role_id IS '角色id';
COMMENT ON COLUMN t_role_menu.menu_id IS '菜单id';
COMMENT ON COLUMN t_role_menu.create_time IS '创建时间';
COMMENT ON COLUMN t_role_menu.update_time IS '更新时间';

CREATE SEQUENCE t_role_menu_seq
START WITH 17
INCREMENT BY 1;

-- 菜单表
CREATE TABLE t_menu (
    id            NUMBER(20) PRIMARY KEY,
    title         VARCHAR2(15) NOT NULL,
    access_path   VARCHAR2(40) NOT NULL,
    file_path     VARCHAR2(60),
    icon          VARCHAR2(15) NOT NULL,
    pid           NUMBER(20) NOT NULL,
    keep_alive NUMBER(1) DEFAULT 0,
    menu_type          NUMBER(1) NOT NULL,
    create_time   DATE DEFAULT SYSDATE NOT NULL,
    update_time   DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT uniq_access_path UNIQUE (access_path)
);

COMMENT ON TABLE t_menu IS '菜单表';
COMMENT ON COLUMN t_menu.id IS '主键';
COMMENT ON COLUMN t_menu.title IS '标题';
COMMENT ON COLUMN t_menu.access_path IS '访问路径';
COMMENT ON COLUMN t_menu.file_path IS '文件路径';
COMMENT ON COLUMN t_menu.icon IS '图标';
COMMENT ON COLUMN t_menu.pid IS '父级菜单id';
COMMENT ON COLUMN t_menu.keep_alive IS '是否缓存';
COMMENT ON COLUMN t_menu.menu_type IS '菜单类型：0-目录，1-菜单';
COMMENT ON COLUMN t_menu.create_time IS '创建时间';
COMMENT ON COLUMN t_menu.update_time IS '更新时间';

CREATE SEQUENCE t_menu_seq
START WITH 17
INCREMENT BY 1;

-- 登录日志表
CREATE TABLE t_login_log (
    id            NUMBER(20) PRIMARY KEY,
    username      VARCHAR2(15) NOT NULL,
    operation     NUMBER(1) NOT NULL,
    status        NUMBER(1) NOT NULL,
    ip            VARCHAR2(32) NOT NULL,
    user_agent    VARCHAR2(255) NOT NULL,
    create_time   DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT pk_login_log PRIMARY KEY (id)
);

CREATE INDEX idx_login_create_time ON t_login_log(create_time DESC);

COMMENT ON TABLE t_login_log IS '登录日志表';
COMMENT ON COLUMN t_login_log.id IS '主键';
COMMENT ON COLUMN t_login_log.username IS '用户名';
COMMENT ON COLUMN t_login_log.operation IS '操作类型';
COMMENT ON COLUMN t_login_log.status IS '操作状态';
COMMENT ON COLUMN t_login_log.ip IS 'IP地址';
COMMENT ON COLUMN t_login_log.user_agent IS '用户代理';
COMMENT ON COLUMN t_login_log.create_time IS '创建时间';

CREATE SEQUENCE t_login_log_seq
START WITH 1
INCREMENT BY 1;

-- 操作日志表
CREATE TABLE t_operation_log (
    id            NUMBER(20) PRIMARY KEY,
    username      VARCHAR2(15) NOT NULL,
    operation     VARCHAR2(10) NOT NULL,
    url           VARCHAR2(50) NOT NULL,
    method        NUMBER(1) NOT NULL,
    params        VARCHAR2(500),
    time        NUMBER(20) NOT NULL,
    status        NUMBER(1) NOT NULL,
    ip            VARCHAR2(32) NOT NULL,
    user_agent    VARCHAR2(255) NOT NULL,
    create_time   DATE DEFAULT SYSDATE NOT NULL
);

CREATE INDEX idx_op_create_time ON t_operation_log(create_time DESC);

COMMENT ON TABLE t_operation_log IS '操作日志表';
COMMENT ON COLUMN t_operation_log.id IS '主键';
COMMENT ON COLUMN t_operation_log.username IS '用户名';
COMMENT ON COLUMN t_operation_log.operation IS '操作';
COMMENT ON COLUMN t_operation_log.url IS '请求地址';
COMMENT ON COLUMN t_operation_log.method IS '请求方式';
COMMENT ON COLUMN t_operation_log.params IS '请求参数';
COMMENT ON COLUMN t_operation_log.time IS '请求耗时';
COMMENT ON COLUMN t_operation_log.status IS '操作状态';
COMMENT ON COLUMN t_operation_log.ip IS 'ip地址';
COMMENT ON COLUMN t_operation_log.user_agent IS '用户代理';
COMMENT ON COLUMN t_operation_log.create_time IS '创建时间';

CREATE SEQUENCE t_operation_log_seq
START WITH 1
INCREMENT BY 1;

-- 错误日志表
CREATE TABLE t_error_log (
    id            NUMBER(20) PRIMARY KEY,
    username      VARCHAR2(15) NOT NULL,
    url           VARCHAR2(50) NOT NULL,
    method        NUMBER(1) NOT NULL,
    params        VARCHAR2(500),
    ip            VARCHAR2(32) NOT NULL,
    user_agent    VARCHAR2(255) NOT NULL,
    error_msg     CLOB NOT NULL,
    create_time   TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    CONSTRAINT pk_error_log PRIMARY KEY (id)
);

CREATE INDEX idx_error_create_time ON t_error_log(create_time DESC);

COMMENT ON TABLE t_error_log IS '错误日志表';
COMMENT ON COLUMN t_error_log.id IS '主键';
COMMENT ON COLUMN t_error_log.username IS '用户名';
COMMENT ON COLUMN t_error_log.url IS '请求地址';
COMMENT ON COLUMN t_error_log.method IS '请求方式';
COMMENT ON COLUMN t_error_log.params IS '请求参数';
COMMENT ON COLUMN t_error_log.ip IS 'IP地址';
COMMENT ON COLUMN t_error_log.user_agent IS '用户代理';
COMMENT ON COLUMN t_error_log.error_msg IS '错误信息';
COMMENT ON COLUMN t_error_log.create_time IS '创建时间';

CREATE SEQUENCE t_error_log_seq
START WITH 1
INCREMENT BY 1;

-- 字典类型表
CREATE TABLE t_dict_type (
    id            NUMBER(20) PRIMARY KEY,
    dict_type          VARCHAR2(50) NOT NULL,
    name          VARCHAR2(50) NOT NULL,
    create_time   DATE DEFAULT SYSDATE NOT NULL,
    update_time   DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT uniq_dict_type UNIQUE (dict_type)
);

COMMENT ON TABLE t_dict_type IS '字典类型表';
COMMENT ON COLUMN t_dict_type.id IS '主键';
COMMENT ON COLUMN t_dict_type.dict_type IS '字典类型';
COMMENT ON COLUMN t_dict_type.name IS '字典名称';
COMMENT ON COLUMN t_dict_type.create_time IS '创建时间';
COMMENT ON COLUMN t_dict_type.update_time IS '更新时间';

CREATE SEQUENCE t_dict_type_seq
START WITH 2
INCREMENT BY 1;


-- 字典数据表
CREATE TABLE t_dict_data (
    id            NUMBER(20) PRIMARY KEY,
    type_id       NUMBER(20) NOT NULL,
    label         VARCHAR2(50) NOT NULL,
    data         NUMBER(10) NOT NULL,
    sort          NUMBER(10) DEFAULT 0,
    create_time   DATE DEFAULT SYSDATE NOT NULL,
    update_time   DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT uniq_dict_type_label UNIQUE (type_id, label)
);

COMMENT ON TABLE t_dict_data IS '字典数据表';
COMMENT ON COLUMN t_dict_data.id IS '主键';
COMMENT ON COLUMN t_dict_data.type_id IS '字典类型id';
COMMENT ON COLUMN t_dict_data.label IS '字典键';
COMMENT ON COLUMN t_dict_data.data IS '字典值';
COMMENT ON COLUMN t_dict_data.sort IS '字典排序';
COMMENT ON COLUMN t_dict_data.create_time IS '创建时间';
COMMENT ON COLUMN t_dict_data.update_time IS '更新时间';

CREATE SEQUENCE t_dict_data_seq
START WITH 3
INCREMENT BY 1;

-- 数据源配置表
CREATE TABLE t_data_source (
    id            NUMBER(20) PRIMARY KEY,
    name          VARCHAR2(50) NOT NULL,
    url           VARCHAR2(500) NOT NULL,
    username      VARCHAR2(50) NOT NULL,
    password      VARCHAR2(50) NOT NULL,
    create_time   DATE DEFAULT SYSDATE NOT NULL,
    update_time   DATE DEFAULT SYSDATE NOT NULL,
    CONSTRAINT uniq_data_source_name UNIQUE (name)
);

COMMENT ON TABLE t_data_source IS '数据源配置表';
COMMENT ON COLUMN t_data_source.id IS '主键';
COMMENT ON COLUMN t_data_source.name IS '数据源名称';
COMMENT ON COLUMN t_data_source.url IS '数据源连接';
COMMENT ON COLUMN t_data_source.username IS '用户名';
COMMENT ON COLUMN t_data_source.password IS '密码';
COMMENT ON COLUMN t_data_source.create_time IS '创建时间';
COMMENT ON COLUMN t_data_source.update_time IS '更新时间';

CREATE SEQUENCE t_data_source_seq
START WITH 1
INCREMENT BY 1;