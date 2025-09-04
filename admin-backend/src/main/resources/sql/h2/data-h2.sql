-- 用户表
INSERT INTO t_user(id, username, password, token_version)
VALUES (1, 'admin', '$2a$10$wTV12Fs5hQfhG9RzZuKd6.c135tv2y8PTJAtKRhlXnU.9xS5Qqcxu', 0);

-- 用户角色关系表
INSERT INTO t_user_role(id, user_id, role_id)
VALUES (1, 1, 1);

-- 角色表
INSERT INTO t_role(id, role_name, description)
VALUES (1, 'admin', '超级管理员'),
       (2, 'test', '测试'),
       (3, 'dev', '开发'),
       (4, 'frontend', '前端'),
       (5, 'backend', '后端'),
       (6, 'bigdata', '大数据');

-- 用户菜单关系表
INSERT INTO t_role_menu(id, role_id, menu_id)
VALUES (1, 1, 1),
       (2, 1, 2),
       (3, 1, 3),
       (4, 1, 4),
       (5, 1, 5),
       (6, 1, 6),
       (7, 1, 7),
       (8, 1, 8),
       (9, 1, 9),
       (10, 1, 10),
       (11, 1, 11),
       (12, 1, 12),
       (13, 1, 13),
       (14, 1, 14),
       (15, 1, 15),
       (16, 1, 16);

-- 菜单表
INSERT INTO t_menu(id, title, access_path, file_path, icon, pid, keep_alive, menu_type)
VALUES (1, '权限管理', '/auth', NULL, 'Lock', 0, 0, 0),
       (2, '用户管理', '/auth/user', '/auth/user/index.vue', 'User', 1, 0, 1),
       (3, '角色管理', '/auth/role', '/auth/role/index.vue', 'Avatar', 1, 0, 1),
       (4, '菜单管理', '/auth/menu', '/auth/menu/index.vue', 'Grid', 1, 0, 1),
       (5, '系统工具', '/tool', NULL, 'Tools', 0, 0, 0),
       (6, '接口文档', '/tool/swagger', '/tool/swagger/index.vue', 'Document', 5, 1, 1),
       (7, '字典管理', '/tool/dict', '/tool/dict/index.vue', 'Collection', 5, 0, 1),
       (8, '数据源管理', '/tool/datasource', '/tool/datasource/index.vue', 'SetUp', 5, 0, 1),
       (9, '日志管理', '/log', NULL, 'Management', 0, 0, 0),
       (10, '登入日志', '/log/login', '/log/login/index.vue', 'Checked', 9, 0, 1),
       (11, '操作日志', '/log/ops', '/log/ops/index.vue', 'List', 9, 0, 1),
       (12, '异常日志', '/log/error', '/log/error/index.vue', 'Failed', 9, 0, 1),
       (13, '菜单测试', '/test', NULL, 'CameraFilled', 0, 0, 0),
       (14, '菜单测试-sub1', '/test/sub1', NULL, 'Platform', 13, 0, 0),
       (15, '菜单测试-sub2', '/test/sub2', '/test/sub1.vue', 'SwitchFilled', 14, 0, 1),
       (16, '菜单测试-sub3', '/test/sub3', '/test/sub2.vue', 'PictureFilled', 13, 0, 1);

-- 字典类型表
insert into t_dict_type(id, dict_type, name)
values (1,'menu_type','菜单类型');

-- 字典数据表
insert into t_dict_data(id, type_id, label, data, sort)
values (1,1,'目录',0,0),
       (2,1,'菜单',1,1);