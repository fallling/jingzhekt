-- 基于CSV文件生成交易记录数据的SQL脚本
-- 文件：cashbook_record_20251008_204553.csv
-- 注意：由于CSV文件编码问题，以下数据是基于可识别的部分重新整理的

USE jingzhe;

-- ===========================================
-- 1. 首先需要添加新的分类和账户
-- ===========================================

-- 添加CSV中出现的分类（只添加不存在的分类）
INSERT IGNORE INTO categories (category_name, parent_id, category_type, sort_order) VALUES 
-- 收入分类
('投资理财', NULL, '收入', 5),
('退款', NULL, '收入', 6),

-- 支出分类
('还款', NULL, '支出', 8),
('学习', NULL, '支出', 9),
('酒店住宿', NULL, '支出', 11),
('运动', NULL, '支出', 12),
('通讯', NULL, '支出', 13);

-- 添加CSV中出现的账户（只添加不存在的账户）
INSERT IGNORE INTO accounts (account_name, account_type, initial_balance, current_balance) VALUES 
('微信支付', '电子支付', 0.00, 0.00),
('中国建设银行', '银行卡', 0.00, 0.00),
('中国银行', '银行卡', 0.00, 0.00);

-- ===========================================
-- 2. 生成交易记录插入语句
-- ===========================================

-- 基于CSV文件内容生成的交易记录
-- 注意：由于CSV文件编码问题，以下数据是基于可识别的部分重新整理的

INSERT INTO transactions (transaction_time, ledger_id, type_id, category_id, amount, account_id, notes) VALUES 

-- 2025年10月记录
('2025-10-08 12:12:27', 1, 2, (SELECT category_id FROM categories WHERE category_name = '投资理财' AND category_type = '收入' LIMIT 1), 0.11, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '理财收益-盛京银行5号'),
('2025-10-07 10:09:13', 1, 1, (SELECT category_id FROM categories WHERE category_name = '还款' AND category_type = '支出' LIMIT 1), -1293.39, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '信用卡还款-2025年10月账单'),
('2025-10-07 10:04:45', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -16.40, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '手机配件购买'),
('2025-10-07 09:46:55', 1, 1, (SELECT category_id FROM categories WHERE category_name = '学习' AND category_type = '支出' LIMIT 1), -20.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '学习课程费用'),
('2025-10-06 19:52:32', 1, 2, (SELECT category_id FROM categories WHERE category_name = '投资理财' AND category_type = '收入' LIMIT 1), 40000.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '投资理财-资金转入'),
('2025-10-06 18:24:14', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -10999.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), 'Apple iPhone 17 Pro'),
('2025-10-06 18:20:40', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -146.00, (SELECT account_id FROM accounts WHERE account_name = '微信支付' LIMIT 1), '电子产品购买'),
('2025-10-06 12:21:45', 1, 1, (SELECT category_id FROM categories WHERE category_name = '交通' AND category_type = '支出' LIMIT 1), -52.06, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '打车费用'),
('2025-10-05 19:59:18', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -14.90, (SELECT account_id FROM accounts WHERE account_name = '微信支付' LIMIT 1), '外卖订单'),
('2025-10-05 19:49:40', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -15.00, (SELECT account_id FROM accounts WHERE account_name = '微信支付' LIMIT 1), '外卖订单'),
('2025-10-05 19:47:48', 1, 1, (SELECT category_id FROM categories WHERE category_name = '交通' AND category_type = '支出' LIMIT 1), -44.57, (SELECT account_id FROM accounts WHERE account_name = '微信支付' LIMIT 1), '打车费用'),
('2025-10-05 19:20:11', 1, 1, (SELECT category_id FROM categories WHERE category_name = '交通' AND category_type = '支出' LIMIT 1), -23.51, (SELECT account_id FROM accounts WHERE account_name = '微信支付' LIMIT 1), '滴滴出行'),
('2025-10-05 18:09:41', 1, 1, (SELECT category_id FROM categories WHERE category_name = '其他支出' AND category_type = '支出' LIMIT 1), -2300.00, (SELECT account_id FROM accounts WHERE account_name = '微信支付' LIMIT 1), '其他支出'),
('2025-10-05 16:50:18', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -300.00, (SELECT account_id FROM accounts WHERE account_name = '微信支付' LIMIT 1), '美团外卖'),
('2025-10-05 11:55:10', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -29.26, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '午餐费用'),
('2025-10-05 07:10:12', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -3.00, (SELECT account_id FROM accounts WHERE account_name = '微信支付' LIMIT 1), '早餐费用'),
('2025-10-04 12:00:34', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -3.00, (SELECT account_id FROM accounts WHERE account_name = '微信支付' LIMIT 1), '充电宝租赁'),
('2025-10-03 15:25:10', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -5.00, (SELECT account_id FROM accounts WHERE account_name = '微信支付' LIMIT 1), '零食购买'),
('2025-10-03 09:57:42', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -460.00, (SELECT account_id FROM accounts WHERE account_name = '微信支付' LIMIT 1), '购物消费'),
('2025-10-03 00:52:15', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -192.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-10-02 12:50:15', 1, 1, (SELECT category_id FROM categories WHERE category_name = '通讯' AND category_type = '支出' LIMIT 1), -49.89, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '话费充值'),
('2025-10-02 02:21:26', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -99.00, (SELECT account_id FROM accounts WHERE account_name = '微信支付' LIMIT 1), '商品购买'),
('2025-10-01 19:37:07', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -5.00, (SELECT account_id FROM accounts WHERE account_name = '微信支付' LIMIT 1), '零食购买'),
('2025-10-01 15:50:43', 1, 1, (SELECT category_id FROM categories WHERE category_name = '住房' AND category_type = '支出' LIMIT 1), -2867.39, (SELECT account_id FROM accounts WHERE account_name = '微信支付' LIMIT 1), '房租费用'),
('2025-10-01 12:13:39', 1, 2, (SELECT category_id FROM categories WHERE category_name = '投资理财' AND category_type = '收入' LIMIT 1), 0.11, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '理财收益-盛京银行5号'),

-- 2025年9月记录
('2025-09-30 17:24:02', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -17.64, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-09-27 18:50:28', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -112.80, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-09-26 16:47:36', 1, 1, (SELECT category_id FROM categories WHERE category_name = '娱乐' AND category_type = '支出' LIMIT 1), -68.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '游戏消费'),
('2025-09-26 00:55:17', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -1538.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '华为手表购买'),
('2025-09-25 23:08:00', 1, 2, (SELECT category_id FROM categories WHERE category_name = '退款' AND category_type = '收入' LIMIT 1), 37.50, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '退款-机票'),
('2025-09-25 18:24:54', 1, 1, (SELECT category_id FROM categories WHERE category_name = '交通' AND category_type = '支出' LIMIT 1), -388.50, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '机票费用'),
('2025-09-25 12:15:42', 1, 1, (SELECT category_id FROM categories WHERE category_name = '交通' AND category_type = '支出' LIMIT 1), -459.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '机票费用'),
('2025-09-24 18:23:07', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -33.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-09-24 12:12:54', 1, 2, (SELECT category_id FROM categories WHERE category_name = '投资理财' AND category_type = '收入' LIMIT 1), 0.11, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '理财收益-盛京银行5号'),
('2025-09-23 21:17:33', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -5.80, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-09-23 19:09:09', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -30.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-09-22 22:35:36', 1, 1, (SELECT category_id FROM categories WHERE category_name = '通讯' AND category_type = '支出' LIMIT 1), -49.90, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '话费充值'),
('2025-09-22 05:49:39', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -10.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '早餐费用'),
('2025-09-21 18:38:25', 1, 1, (SELECT category_id FROM categories WHERE category_name = '还款' AND category_type = '支出' LIMIT 1), -696.68, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '信用卡还款-2025年10月账单'),

-- 2025年8月记录
('2025-08-31 11:42:04', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -36.50, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-08-30 19:53:01', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -12.99, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-08-30 11:14:41', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -32.76, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-08-29 10:57:59', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -15.76, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-08-28 11:08:38', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -17.20, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-08-27 12:14:29', 1, 2, (SELECT category_id FROM categories WHERE category_name = '投资理财' AND category_type = '收入' LIMIT 1), 0.11, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '理财收益-盛京银行5号'),
('2025-08-27 10:55:43', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -16.80, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-08-26 11:02:20', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -15.80, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-08-24 20:03:54', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -13.90, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-08-24 11:03:36', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -14.20, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-08-23 20:30:55', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -13.90, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-08-23 11:13:02', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -17.20, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-08-23 09:11:17', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -519.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-08-22 10:50:37', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -14.80, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-08-22 05:53:24', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -10.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '早餐费用'),
('2025-08-21 11:00:35', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -18.80, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-08-20 22:31:36', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -30.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-08-20 12:14:24', 1, 2, (SELECT category_id FROM categories WHERE category_name = '投资理财' AND category_type = '收入' LIMIT 1), 0.11, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '理财收益-盛京银行5号'),
('2025-08-20 11:17:05', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -17.50, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-08-19 11:03:44', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -20.60, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-08-17 12:08:09', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -16.10, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-08-16 20:05:28', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -14.40, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-08-16 10:57:55', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -34.66, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-08-14 19:27:38', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -9.99, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-08-13 12:15:58', 1, 2, (SELECT category_id FROM categories WHERE category_name = '投资理财' AND category_type = '收入' LIMIT 1), 0.11, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '理财收益-盛京银行5号'),
('2025-08-13 00:40:09', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -31.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-08-12 11:06:00', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -14.40, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-08-12 09:27:33', 1, 1, (SELECT category_id FROM categories WHERE category_name = '还款' AND category_type = '支出' LIMIT 1), -1020.24, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '信用卡还款-2025年08月账单'),
('2025-08-11 15:49:15', 1, 2, (SELECT category_id FROM categories WHERE category_name = '退款' AND category_type = '收入' LIMIT 1), 39.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '退款-机票费用'),
('2025-08-11 15:49:09', 1, 2, (SELECT category_id FROM categories WHERE category_name = '退款' AND category_type = '收入' LIMIT 1), 640.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '退款-机票费用'),
('2025-08-11 11:26:48', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -1099.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '华为耳机购买'),
('2025-08-11 11:11:27', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -14.70, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),

-- 2025年7月记录
('2025-07-31 14:47:03', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -94.91, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-07-31 11:20:45', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -20.49, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-07-30 22:29:30', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -38.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-07-30 19:59:29', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -22.90, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '肯德基'),
('2025-07-30 12:14:55', 1, 2, (SELECT category_id FROM categories WHERE category_name = '投资理财' AND category_type = '收入' LIMIT 1), 0.11, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '理财收益-盛京银行5号'),
('2025-07-30 11:11:05', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -21.50, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-07-30 11:08:38', 1, 1, (SELECT category_id FROM categories WHERE category_name = '运动' AND category_type = '支出' LIMIT 1), -47.31, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '运动用品购买'),
('2025-07-30 10:51:34', 1, 2, (SELECT category_id FROM categories WHERE category_name = '退款' AND category_type = '收入' LIMIT 1), 115.32, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '退款-商品购买'),
('2025-07-29 19:29:29', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -22.90, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '肯德基'),
('2025-07-29 10:43:24', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -14.50, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-07-28 11:01:34', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -13.70, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-07-27 23:35:03', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -115.32, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-07-27 12:38:15', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -17.20, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-07-25 19:51:19', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -10.10, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-07-25 19:05:31', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -27.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-07-25 11:10:27', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -8.50, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-07-24 22:26:25', 1, 1, (SELECT category_id FROM categories WHERE category_name = '交通' AND category_type = '支出' LIMIT 1), -156.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '交通费用'),
('2025-07-24 22:26:25', 1, 1, (SELECT category_id FROM categories WHERE category_name = '酒店住宿' AND category_type = '支出' LIMIT 1), -200.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '酒店住宿费用'),
('2025-07-24 22:26:25', 1, 1, (SELECT category_id FROM categories WHERE category_name = '交通' AND category_type = '支出' LIMIT 1), -3012.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '机票费用'),
('2025-07-24 21:50:47', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -4.95, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-07-24 14:31:52', 1, 2, (SELECT category_id FROM categories WHERE category_name = '退款' AND category_type = '收入' LIMIT 1), 21.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '退款-餐饮费用'),
('2025-07-24 14:30:50', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -21.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-07-24 10:46:49', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -20.81, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-07-23 23:18:47', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -33.80, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-07-23 12:20:24', 1, 2, (SELECT category_id FROM categories WHERE category_name = '投资理财' AND category_type = '收入' LIMIT 1), 0.11, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '理财收益-盛京银行5号'),
('2025-07-23 10:58:54', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -21.29, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-07-22 12:14:52', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -22.75, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '肯德基'),
('2025-07-22 05:53:18', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -10.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '早餐费用'),
('2025-07-21 19:32:11', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -22.90, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '肯德基'),
('2025-07-21 10:57:21', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -20.81, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-07-20 11:16:44', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -92.18, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-07-19 17:50:28', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -48.94, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-07-18 21:36:13', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -9.22, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-07-17 17:05:22', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -42.90, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-07-17 11:02:15', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -19.60, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-07-16 12:14:41', 1, 2, (SELECT category_id FROM categories WHERE category_name = '投资理财' AND category_type = '收入' LIMIT 1), 0.11, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '理财收益-盛京银行5号'),
('2025-07-16 10:46:49', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -22.50, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-07-15 13:54:24', 1, 1, (SELECT category_id FROM categories WHERE category_name = '还款' AND category_type = '支出' LIMIT 1), -317.06, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '信用卡还款-2025年07月账单'),
('2025-07-14 19:46:01', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -9.90, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-07-14 15:26:21', 1, 2, (SELECT category_id FROM categories WHERE category_name = '投资理财' AND category_type = '收入' LIMIT 1), 0.75, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '理财收益'),
('2025-07-14 10:57:03', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -17.90, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-07-13 17:40:25', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -25.30, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-07-12 09:24:29', 1, 1, (SELECT category_id FROM categories WHERE category_name = '购物' AND category_type = '支出' LIMIT 1), -189.33, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '商品购买'),
('2025-07-11 10:43:08', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -12.70, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-07-10 10:42:27', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -20.70, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费'),
('2025-07-09 12:16:47', 1, 2, (SELECT category_id FROM categories WHERE category_name = '投资理财' AND category_type = '收入' LIMIT 1), 0.11, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '理财收益-盛京银行5号'),
('2025-07-09 10:44:54', 1, 1, (SELECT category_id FROM categories WHERE category_name = '餐饮' AND category_type = '支出' LIMIT 1), -16.00, (SELECT account_id FROM accounts WHERE account_name = '支付宝' LIMIT 1), '餐饮消费');

-- ===========================================
-- 3. 更新账户余额
-- ===========================================

-- 手动更新所有账户余额（避免存储过程限制）
UPDATE accounts SET current_balance = (
    SELECT initial_balance + COALESCE(SUM(amount), 0)
    FROM transactions 
    WHERE account_id = accounts.account_id
);

-- ===========================================
-- 4. 验证数据
-- ===========================================

-- 查看交易记录总数
SELECT COUNT(*) as '交易记录总数' FROM transactions;

-- 查看各类型交易统计
SELECT 
    tt.type_name as '交易类型',
    COUNT(*) as '记录数',
    SUM(t.amount) as '总金额'
FROM transactions t
JOIN transaction_types tt ON t.type_id = tt.type_id
GROUP BY tt.type_name;

-- 查看各分类支出统计
SELECT 
    c.category_name as '分类',
    COUNT(*) as '记录数',
    SUM(t.amount) as '总金额'
FROM transactions t
JOIN categories c ON t.category_id = c.category_id
WHERE c.category_type = '支出'
GROUP BY c.category_name
ORDER BY SUM(t.amount) DESC;

-- 查看账户余额
SELECT * FROM account_balances;

SELECT 'CSV数据导入完成！' as message;
SELECT '已导入交易记录数据' as info;
SELECT '请检查数据是否正确' as info;