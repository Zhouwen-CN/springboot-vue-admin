use springboot_vue_admin;

delete from `t_user`;
INSERT INTO `t_user`
VALUES (1, 'admin', '$2a$10$wTV12Fs5hQfhG9RzZuKd6.c135tv2y8PTJAtKRhlXnU.9xS5Qqcxu', 0, '2024-05-06 13:45:21',
        '2024-05-16 02:13:17');


delete from `t_user_role`;
INSERT INTO `t_user_role`
VALUES (1, 1, 1, '2024-05-06 13:45:21', '2024-05-06 13:45:21');


delete from `t_role`;
INSERT INTO `t_role`
VALUES (1, 'admin', '超级管理员', '2024-05-06 13:45:21', '2024-05-16 03:21:03'),
       (2, 'test', '测试', '2024-05-06 13:45:21', '2024-05-16 01:31:23'),
       (3, 'dev', '开发', '2024-05-15 06:21:25', '2024-05-15 06:21:25'),
       (4, 'frontend', '前端', '2024-05-15 06:21:25', '2024-05-15 06:21:25'),
       (5, 'backend', '后端', '2024-05-15 06:21:25', '2024-05-15 06:21:25'),
       (6, 'bigdata', '大数据', '2024-05-15 06:21:25', '2024-05-15 06:21:25');


delete from `t_role_menu`;
INSERT INTO `t_role_menu`
VALUES (1, 1, 1, '2024-05-06 13:47:16', '2024-05-06 13:47:16'),
       (2, 1, 2, '2024-05-06 13:47:16', '2024-05-06 13:47:16'),
       (3, 1, 3, '2024-05-16 03:30:19', '2024-05-16 03:30:19'),
       (4, 1, 4, '2024-05-06 13:47:16', '2024-05-06 13:47:16'),
       (5, 1, 5, '2024-05-16 03:30:19', '2024-05-16 03:30:19'),
       (6, 1, 6, '2024-05-16 01:30:24', '2024-05-16 01:30:24'),
       (7, 1, 7, '2025-01-15 00:00:00', '2025-01-15 00:00:00'),
       (8, 1, 8, '2025-01-15 00:00:00', '2025-01-15 00:00:00'),
       (9, 1, 9, '2025-01-15 00:00:00', '2025-01-15 00:00:00'),
       (10, 1, 10, '2025-01-15 00:00:00', '2025-01-15 00:00:00'),
       (11, 1, 11, '2025-01-15 00:00:00', '2025-01-15 00:00:00'),
       (12, 1, 12, '2025-01-15 00:00:00', '2025-01-15 00:00:00'),
       (13, 1, 13, '2025-01-15 00:00:00', '2025-01-15 00:00:00');


delete from `t_menu`;
INSERT INTO `t_menu`
VALUES (1, '权限管理', '/auth', NULL, 'Lock', 0, 0, '2024-05-06 13:46:48', '2024-05-16 04:04:36'),
       (2, '用户管理', '/auth/user', '/auth/user/index.vue', 'User', 0, 1, '2024-05-06 13:46:48',
        '2024-05-06 13:46:48'),
       (3, '角色管理', '/auth/role', '/auth/role/index.vue', 'Avatar', 0, 1, '2024-05-06 13:46:48',
        '2024-05-06 13:46:48'),
       (4, '菜单管理', '/auth/menu', '/auth/menu/index.vue', 'Grid', 0, 1, '2024-05-06 13:46:48',
        '2024-05-06 13:46:48'),
       (5, '菜单测试', '/test', NULL, 'Location', 0, 0, '2024-05-13 00:50:29', '2024-05-13 00:50:29'),
       (6, '菜单测试-sub1', '/test/sub1', '/test/sub1.vue', 'Location', 0, 5, '2024-05-13 00:50:29',
        '2024-05-13 00:50:29'),
       (7, '菜单测试-sub2', '/test/sub2', '/test/sub2.vue', 'HomeFilled', 0, 5, '2024-05-13 00:50:29',
        '2024-05-13 00:50:29'),
        (8, '开发者工具', '/dev', NULL, 'Tools', 0, 0, '2025-01-17 00:00:00',
        '2025-01-17 00:00:00'),
       (9, '接口文档', '/dev/swagger', '/dev/swagger/index.vue', 'Document', 1, 8, '2025-01-17 00:00:00',
        '2025-01-17 00:00:00'),
        (10, '日志管理', '/log', NULL, 'Management', 0, 0, '2025-01-15 00:00:00',
        '2025-01-15 00:00:00'),
       (11, '登入日志', '/log/login', '/log/login/index.vue', 'Checked', 0, 10, '2025-01-15 00:00:00',
        '2025-01-15 00:00:00'),
       (12, '操作日志', '/log/ops', '/log/ops/index.vue', 'List', 0, 10, '2025-01-15 00:00:00',
        '2025-01-15 00:00:00'),
       (13, '异常日志', '/log/error', '/log/error/index.vue', 'Failed', 0, 10, '2025-01-15 00:00:00',
        '2025-01-15 00:00:00');