-- Jingzhe 记账系统数据库创建脚本
-- 基于数据库设计文档生成

-- 删除已存在的数据库（如果存在）
DROP DATABASE IF EXISTS jingzhe;

-- 创建数据库
CREATE DATABASE jingzhe 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE jingzhe;

-- ===========================================
-- 1. 创建表结构
-- ===========================================

-- 1.1 创建账本表 (ledgers)
CREATE TABLE IF NOT EXISTS ledgers (
    ledger_id INT PRIMARY KEY AUTO_INCREMENT,
    ledger_name VARCHAR(50) NOT NULL UNIQUE,
    description TEXT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 1.2 创建账户表 (accounts)
CREATE TABLE IF NOT EXISTS accounts (
    account_id INT PRIMARY KEY AUTO_INCREMENT,
    account_name VARCHAR(50) NOT NULL UNIQUE,
    account_type ENUM('现金', '银行卡', '信用卡', '电子支付', '投资账户', '其他') NOT NULL,
    initial_balance DECIMAL(12,2) DEFAULT 0.00,
    current_balance DECIMAL(12,2) DEFAULT 0.00,
    currency VARCHAR(10) DEFAULT 'CNY',
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 1.3 创建分类表 (categories)
CREATE TABLE IF NOT EXISTS categories (
    category_id INT PRIMARY KEY AUTO_INCREMENT,
    category_name VARCHAR(50) NOT NULL,
    parent_id INT NULL,
    category_type ENUM('支出', '收入', '转账') NOT NULL,
    icon VARCHAR(100) NULL,
    sort_order INT DEFAULT 0,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES categories(category_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 1.4 创建交易类型表 (transaction_types)
CREATE TABLE IF NOT EXISTS transaction_types (
    type_id INT PRIMARY KEY AUTO_INCREMENT,
    type_name VARCHAR(20) NOT NULL UNIQUE,
    type_code VARCHAR(10) NOT NULL UNIQUE,
    description VARCHAR(100) NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 1.5 创建交易记录表 (transactions)
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INT PRIMARY KEY AUTO_INCREMENT,
    transaction_time DATETIME NOT NULL,
    ledger_id INT NOT NULL,
    type_id INT NOT NULL,
    category_id INT NOT NULL,
    amount DECIMAL(12,2) NOT NULL,
    account_id INT NOT NULL,
    transfer_account_id INT NULL,
    reimbursement DECIMAL(12,2) DEFAULT 0.00,
    is_reimbursed BOOLEAN DEFAULT FALSE,
    notes TEXT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (ledger_id) REFERENCES ledgers(ledger_id) ON DELETE CASCADE,
    FOREIGN KEY (type_id) REFERENCES transaction_types(type_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE,
    FOREIGN KEY (transfer_account_id) REFERENCES accounts(account_id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 1.6 创建预算表 (budgets) - 可选扩展
CREATE TABLE IF NOT EXISTS budgets (
    budget_id INT PRIMARY KEY AUTO_INCREMENT,
    ledger_id INT NOT NULL,
    category_id INT NOT NULL,
    budget_amount DECIMAL(12,2) NOT NULL,
    budget_month DATE NOT NULL,
    actual_amount DECIMAL(12,2) DEFAULT 0.00,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (ledger_id) REFERENCES ledgers(ledger_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE,
    UNIQUE KEY unique_budget (ledger_id, category_id, budget_month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ===========================================
-- 2. 创建索引优化查询性能
-- ===========================================

-- transactions 表索引
CREATE INDEX idx_transactions_time ON transactions(transaction_time);
CREATE INDEX idx_transactions_ledger ON transactions(ledger_id);
CREATE INDEX idx_transactions_type ON transactions(type_id);
CREATE INDEX idx_transactions_category ON transactions(category_id);
CREATE INDEX idx_transactions_account ON transactions(account_id);
CREATE INDEX idx_transactions_transfer_account ON transactions(transfer_account_id);
CREATE INDEX idx_transactions_created_at ON transactions(created_at);

-- categories 表索引
CREATE INDEX idx_categories_parent ON categories(parent_id);
CREATE INDEX idx_categories_type ON categories(category_type);
CREATE INDEX idx_categories_sort ON categories(sort_order);
CREATE INDEX idx_categories_active ON categories(is_active);

-- accounts 表索引
CREATE INDEX idx_accounts_type ON accounts(account_type);
CREATE INDEX idx_accounts_active ON accounts(is_active);

-- ledgers 表索引
CREATE INDEX idx_ledgers_active ON ledgers(is_active);

-- budgets 表索引
CREATE INDEX idx_budgets_month ON budgets(budget_month);
CREATE INDEX idx_budgets_ledger_category ON budgets(ledger_id, category_id);

-- ===========================================
-- 3. 创建视图
-- ===========================================

-- 3.1 交易详情视图
CREATE OR REPLACE VIEW transaction_details AS
SELECT 
    t.transaction_id,
    t.transaction_time as '时间',
    l.ledger_name as '账本',
    tt.type_name as '类型',
    COALESCE(pc.category_name, c.category_name) as '一级分类',
    CASE WHEN pc.category_id IS NOT NULL THEN c.category_name ELSE NULL END as '二级分类',
    t.amount as '金额',
    a.account_name as '账户',
    t.reimbursement as '报销',
    t.is_reimbursed as '是否已报销',
    t.notes as '备注',
    t.created_at as '记录时间'
FROM transactions t
LEFT JOIN ledgers l ON t.ledger_id = l.ledger_id
LEFT JOIN transaction_types tt ON t.type_id = tt.type_id
LEFT JOIN categories c ON t.category_id = c.category_id
LEFT JOIN categories pc ON c.parent_id = pc.category_id
LEFT JOIN accounts a ON t.account_id = a.account_id
WHERE l.is_active = TRUE AND c.is_active = TRUE AND a.is_active = TRUE
ORDER BY t.transaction_time DESC;

-- 3.2 账户余额视图
CREATE OR REPLACE VIEW account_balances AS
SELECT 
    a.account_id,
    a.account_name,
    a.account_type,
    a.initial_balance,
    a.current_balance,
    COALESCE(SUM(t.amount), 0) as '交易总额',
    (a.initial_balance + COALESCE(SUM(t.amount), 0)) as '计算余额'
FROM accounts a
LEFT JOIN transactions t ON a.account_id = t.account_id
WHERE a.is_active = TRUE
GROUP BY a.account_id, a.account_name, a.account_type, a.initial_balance, a.current_balance;

-- ===========================================
-- 4. 插入基础数据
-- ===========================================

-- 4.1 插入默认账本
INSERT INTO ledgers (ledger_name, description) VALUES 
('默认账本', '个人日常记账账本');

-- 4.2 插入交易类型
INSERT INTO transaction_types (type_name, type_code, description) VALUES 
('支出', 'EXPENSE', '资金流出'),
('收入', 'INCOME', '资金流入'),
('转账', 'TRANSFER', '账户间资金转移');

-- 4.3 插入基础账户
INSERT INTO accounts (account_name, account_type, initial_balance, current_balance) VALUES 
('现金', '现金', 1000.00, 1000.00),
('支付宝', '电子支付', 5000.00, 5000.00),
('招商银行卡', '银行卡', 10000.00, 10000.00),
('信用卡', '信用卡', 0.00, 0.00);

-- 4.4 插入基础分类（支出）
INSERT INTO categories (category_name, parent_id, category_type, sort_order) VALUES 
-- 一级支出分类
('餐饮', NULL, '支出', 1),
('购物', NULL, '支出', 2),
('交通', NULL, '支出', 3),
('娱乐', NULL, '支出', 4),
('医疗', NULL, '支出', 5),
('教育', NULL, '支出', 6),
('住房', NULL, '支出', 7),
('其他支出', NULL, '支出', 99);

-- 4.5 插入餐饮二级分类
INSERT INTO categories (category_name, parent_id, category_type, sort_order) VALUES 
('早餐', 1, '支出', 1),
('午餐', 1, '支出', 2),
('晚餐', 1, '支出', 3),
('零食', 1, '支出', 4),
('饮料', 1, '支出', 5);

-- 4.6 插入购物二级分类
INSERT INTO categories (category_name, parent_id, category_type, sort_order) VALUES 
('服装', 2, '支出', 1),
('日用品', 2, '支出', 2),
('电子产品', 2, '支出', 3),
('书籍', 2, '支出', 4);

-- 4.7 插入基础分类（收入）
INSERT INTO categories (category_name, parent_id, category_type, sort_order) VALUES 
('工资', NULL, '收入', 1),
('奖金', NULL, '收入', 2),
('投资', NULL, '收入', 3),
('兼职', NULL, '收入', 4),
('其他收入', NULL, '收入', 99);

-- 4.8 插入转账分类
INSERT INTO categories (category_name, parent_id, category_type, sort_order) VALUES 
('账户转账', NULL, '转账', 1);

-- ===========================================
-- 5. 插入示例数据
-- ===========================================

-- 5.1 插入示例交易记录
INSERT INTO transactions (transaction_time, ledger_id, type_id, category_id, amount, account_id, notes) VALUES 
('2025-07-29 19:29:00', 1, 1, 1, -22.90, 2, '肯德基'),
('2025-07-29 12:15:00', 1, 1, 2, -15.50, 2, '午餐'),
('2025-07-28 18:30:00', 1, 1, 3, -8.00, 1, '地铁'),
('2025-07-28 09:00:00', 1, 1, 6, -5.00, 1, '早餐'),
('2025-07-27 20:00:00', 1, 1, 4, -35.00, 2, '电影票'),
('2025-07-27 15:30:00', 1, 1, 8, -120.00, 3, '超市购物'),
('2025-07-26 10:00:00', 1, 2, 9, 5000.00, 3, '工资'),
('2025-07-25 14:20:00', 1, 1, 5, -200.00, 2, '医院挂号费');

-- ===========================================
-- 6. 创建存储过程和函数（可选）
-- ===========================================

-- 6.1 更新账户余额的存储过程
DELIMITER //
CREATE PROCEDURE UpdateAccountBalance(IN account_id_param INT)
BEGIN
    DECLARE total_amount DECIMAL(12,2) DEFAULT 0;
    
    SELECT COALESCE(SUM(amount), 0) INTO total_amount
    FROM transactions 
    WHERE account_id = account_id_param;
    
    UPDATE accounts 
    SET current_balance = initial_balance + total_amount,
        updated_at = CURRENT_TIMESTAMP
    WHERE account_id = account_id_param;
END //
DELIMITER ;

-- 6.2 获取月度统计的函数
DELIMITER //
CREATE FUNCTION GetMonthlyTotal(p_ledger_id INT, p_year_month VARCHAR(7), p_transaction_type VARCHAR(10))
RETURNS DECIMAL(12,2)
READS SQL DATA
DETERMINISTIC
BEGIN
    DECLARE total DECIMAL(12,2) DEFAULT 0;
    
    SELECT COALESCE(SUM(t.amount), 0) INTO total
    FROM transactions t
    JOIN transaction_types tt ON t.type_id = tt.type_id
    WHERE t.ledger_id = p_ledger_id
    AND DATE_FORMAT(t.transaction_time, '%Y-%m') = p_year_month
    AND tt.type_code = p_transaction_type;
    
    RETURN total;
END //
DELIMITER ;

-- ===========================================
-- 7. 触发器说明（已移除）
-- ===========================================

-- 注意：由于MariaDB的限制，触发器不能调用存储过程来更新accounts表
-- 账户余额需要通过应用程序或手动调用UpdateAccountBalance存储过程来更新
-- 或者使用视图account_balances来实时计算余额

-- ===========================================
-- 8. 权限设置（可选）
-- ===========================================

-- 创建应用用户（根据实际需要调整）
-- CREATE USER 'jingzhe_app'@'localhost' IDENTIFIED BY 'your_password_here';
-- GRANT SELECT, INSERT, UPDATE, DELETE ON jingzhe_db.* TO 'jingzhe_app'@'localhost';
-- FLUSH PRIVILEGES;

-- ===========================================
-- 完成提示
-- ===========================================

SELECT 'Jingzhe 数据库创建完成！' as message;
SELECT '数据库名称: jingzhe' as info;
SELECT '字符集: utf8mb4' as info;
SELECT '存储引擎: InnoDB' as info;
SELECT '已创建表: ledgers, accounts, categories, transaction_types, transactions, budgets' as info;
SELECT '已创建视图: transaction_details, account_balances' as info;
SELECT '已创建存储过程: UpdateAccountBalance' as info;
SELECT '已创建函数: GetMonthlyTotal' as info;
SELECT '触发器: 已移除（避免MariaDB限制）' as info;
