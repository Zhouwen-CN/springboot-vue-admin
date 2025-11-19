-- 用户表
INSERT INTO t_user(id, username, password) VALUES (1, 'admin', '$2a$10$wTV12Fs5hQfhG9RzZuKd6.c135tv2y8PTJAtKRhlXnU.9xS5Qqcxu');

-- 用户角色关系表
INSERT INTO t_user_role(id, user_id, role_id) VALUES (1, 1, 1);

-- 角色表
INSERT INTO t_role(id, role_name, description) VALUES (1, 'admin', '超级管理员');
INSERT INTO t_role(id, role_name, description) VALUES (2, 'test', '测试');
INSERT INTO t_role(id, role_name, description) VALUES (3, 'dev', '开发');
INSERT INTO t_role(id, role_name, description) VALUES (4, 'product', '产品');

-- 用户菜单关系表
INSERT INTO t_role_menu(id, role_id, menu_id) VALUES (1, 1, 1);
INSERT INTO t_role_menu(id, role_id, menu_id) VALUES (2, 1, 2);
INSERT INTO t_role_menu(id, role_id, menu_id) VALUES (3, 1, 3);
INSERT INTO t_role_menu(id, role_id, menu_id) VALUES (4, 1, 4);
INSERT INTO t_role_menu(id, role_id, menu_id) VALUES (5, 1, 5);
INSERT INTO t_role_menu(id, role_id, menu_id) VALUES (6, 1, 6);
INSERT INTO t_role_menu(id, role_id, menu_id) VALUES (7, 1, 7);
INSERT INTO t_role_menu(id, role_id, menu_id) VALUES (8, 1, 8);
INSERT INTO t_role_menu(id, role_id, menu_id) VALUES (9, 1, 9);
INSERT INTO t_role_menu(id, role_id, menu_id) VALUES (10, 1, 10);
INSERT INTO t_role_menu(id, role_id, menu_id) VALUES (11, 1, 11);
INSERT INTO t_role_menu(id, role_id, menu_id) VALUES (12, 1, 12);
INSERT INTO t_role_menu(id, role_id, menu_id) VALUES (13, 1, 13);
INSERT INTO t_role_menu(id, role_id, menu_id) VALUES (14, 1, 14);
INSERT INTO t_role_menu(id, role_id, menu_id) VALUES (15, 1, 15);
INSERT INTO t_role_menu(id, role_id, menu_id) VALUES (16, 1, 16);
INSERT INTO t_role_menu(id, role_id, menu_id) VALUES (17, 1, 17);
INSERT INTO t_role_menu(id, role_id, menu_id) VALUES (18, 1, 18);

-- 菜单表
INSERT INTO t_menu(id, title, access_path, icon, pid, keep_alive, menu_type) VALUES (1, '权限管理', '/auth', 'Lock', 0, 0, 0);
INSERT INTO t_menu(id, title, access_path, icon, pid, keep_alive, menu_type) VALUES (2, '用户管理', '/auth/user', 'User', 1, 0, 1);
INSERT INTO t_menu(id, title, access_path, icon, pid, keep_alive, menu_type) VALUES (3, '角色管理', '/auth/role', 'Avatar', 1, 0, 1);
INSERT INTO t_menu(id, title, access_path, icon, pid, keep_alive, menu_type) VALUES (4, '菜单管理', '/auth/menu', 'Grid', 1, 0, 1);
INSERT INTO t_menu(id, title, access_path, icon, pid, keep_alive, menu_type) VALUES (5, '系统工具', '/tool', 'Tools', 0, 0, 0);
INSERT INTO t_menu(id, title, access_path, icon, pid, keep_alive, menu_type) VALUES (6, '接口文档', '/tool/swagger', 'Document', 5, 1, 1);
INSERT INTO t_menu(id, title, access_path, icon, pid, keep_alive, menu_type) VALUES (7, '字典管理', '/tool/dict', 'Collection', 5, 0, 1);
INSERT INTO t_menu(id, title, access_path, icon, pid, keep_alive, menu_type) VALUES (8, '数据源管理', '/tool/datasource', 'SetUp', 5, 0, 1);
INSERT INTO t_menu(id, title, access_path, icon, pid, keep_alive, menu_type) VALUES (9, '代码生成', '/tool/codegen', 'Coffee', 5, 0, 1);
INSERT INTO t_menu(id, title, access_path, icon, pid, keep_alive, menu_type) VALUES (10, '定时任务', '/tool/job', 'Clock', 5, 0, 1);
INSERT INTO t_menu(id, title, access_path, icon, pid, keep_alive, menu_type) VALUES (11, '日志管理', '/log', 'Management', 0, 0, 0);
INSERT INTO t_menu(id, title, access_path, icon, pid, keep_alive, menu_type) VALUES (12, '登入日志', '/log/login', 'Checked', 11, 0, 1);
INSERT INTO t_menu(id, title, access_path, icon, pid, keep_alive, menu_type) VALUES (13, '操作日志', '/log/ops', 'List', 11, 0, 1);
INSERT INTO t_menu(id, title, access_path, icon, pid, keep_alive, menu_type) VALUES (14, '异常日志', '/log/error', 'Failed', 11, 0, 1);
INSERT INTO t_menu(id, title, access_path, icon, pid, keep_alive, menu_type) VALUES (15, '调度日志', '/log/job', 'BellFilled', 11, 0, 1);
INSERT INTO t_menu(id, title, access_path, icon, pid, keep_alive, menu_type) VALUES (16, '菜单测试', '/test', 'CameraFilled', 0, 0, 0);
INSERT INTO t_menu(id, title, access_path, icon, pid, keep_alive, menu_type) VALUES (17, '菜单测试-sub1', '/test/sub1', 'SwitchFilled', 16, 0, 1);
INSERT INTO t_menu(id, title, access_path, icon, pid, keep_alive, menu_type) VALUES (18, '菜单测试-sub2', '/test/sub2', 'PictureFilled', 16, 0, 1);

-- 字典类型表
insert into t_dict_type(id, name, dict_enable) values (1, '菜单类型', 1);

-- 字典数据表
insert into t_dict_data(id, type_id, label, data, sort_id) values (1, 1, '目录', 0, 0);
insert into t_dict_data(id, type_id, label, data, sort_id) values (2, 1, '菜单', 1, 1);

-- 定时任务表
insert into t_job(id, name, handler_name, handler_param, cron_expression, retry_count, retry_interval, job_enable) values (1, '随机抽签', 'http', '{"method":"get","url":"http://shanhe.kim/api/za/chouq.php"}', '0/5 * * * * ? *', 0, 0, 0);