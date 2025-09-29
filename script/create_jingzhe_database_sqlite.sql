-- Jingzhe 记账系统数据库创建脚本 (SQLite版本)
-- 基于数据库设计文档生成，适配SQLite语法

-- ===========================================
-- 1. 创建表结构
-- ===========================================

-- 1.1 创建账本表 (ledgers)
CREATE TABLE IF NOT EXISTS ledgers (
    ledger_id INTEGER PRIMARY KEY AUTOINCREMENT,
    ledger_name TEXT NOT NULL UNIQUE,
    description TEXT,
    is_active INTEGER DEFAULT 1 CHECK (is_active IN (0, 1)),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 1.2 创建账户表 (accounts)
CREATE TABLE IF NOT EXISTS accounts (
    account_id INTEGER PRIMARY KEY AUTOINCREMENT,
    account_name TEXT NOT NULL UNIQUE,
    account_type TEXT NOT NULL CHECK (account_type IN ('现金', '银行卡', '信用卡', '电子支付', '投资账户', '其他')),
    initial_balance REAL DEFAULT 0.00,
    current_balance REAL DEFAULT 0.00,
    currency TEXT DEFAULT 'CNY',
    is_active INTEGER DEFAULT 1 CHECK (is_active IN (0, 1)),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 1.3 创建分类表 (categories)
CREATE TABLE IF NOT EXISTS categories (
    category_id INTEGER PRIMARY KEY AUTOINCREMENT,
    category_name TEXT NOT NULL,
    parent_id INTEGER,
    category_type TEXT NOT NULL CHECK (category_type IN ('支出', '收入', '转账')),
    icon TEXT,
    sort_order INTEGER DEFAULT 0,
    is_active INTEGER DEFAULT 1 CHECK (is_active IN (0, 1)),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (parent_id) REFERENCES categories(category_id) ON DELETE SET NULL
);

-- 1.4 创建交易类型表 (transaction_types)
CREATE TABLE IF NOT EXISTS transaction_types (
    type_id INTEGER PRIMARY KEY AUTOINCREMENT,
    type_name TEXT NOT NULL UNIQUE,
    type_code TEXT NOT NULL UNIQUE,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- 1.5 创建交易记录表 (transactions)
CREATE TABLE IF NOT EXISTS transactions (
    transaction_id INTEGER PRIMARY KEY AUTOINCREMENT,
    transaction_time DATETIME NOT NULL,
    ledger_id INTEGER NOT NULL,
    type_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    amount REAL NOT NULL,
    account_id INTEGER NOT NULL,
    transfer_account_id INTEGER,
    reimbursement REAL DEFAULT 0.00,
    is_reimbursed INTEGER DEFAULT 0 CHECK (is_reimbursed IN (0, 1)),
    notes TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ledger_id) REFERENCES ledgers(ledger_id) ON DELETE CASCADE,
    FOREIGN KEY (type_id) REFERENCES transaction_types(type_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE,
    FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE,
    FOREIGN KEY (transfer_account_id) REFERENCES accounts(account_id) ON DELETE SET NULL
);

-- 1.6 创建预算表 (budgets) - 可选扩展
CREATE TABLE IF NOT EXISTS budgets (
    budget_id INTEGER PRIMARY KEY AUTOINCREMENT,
    ledger_id INTEGER NOT NULL,
    category_id INTEGER NOT NULL,
    budget_amount REAL NOT NULL,
    budget_month DATE NOT NULL,
    actual_amount REAL DEFAULT 0.00,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (ledger_id) REFERENCES ledgers(ledger_id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE CASCADE,
    UNIQUE (ledger_id, category_id, budget_month)
);

-- ===========================================
-- 2. 创建索引优化查询性能
-- ===========================================

-- transactions 表索引
CREATE INDEX IF NOT EXISTS idx_transactions_time ON transactions(transaction_time);
CREATE INDEX IF NOT EXISTS idx_transactions_ledger ON transactions(ledger_id);
CREATE INDEX IF NOT EXISTS idx_transactions_type ON transactions(type_id);
CREATE INDEX IF NOT EXISTS idx_transactions_category ON transactions(category_id);
CREATE INDEX IF NOT EXISTS idx_transactions_account ON transactions(account_id);
CREATE INDEX IF NOT EXISTS idx_transactions_transfer_account ON transactions(transfer_account_id);
CREATE INDEX IF NOT EXISTS idx_transactions_created_at ON transactions(created_at);

-- categories 表索引
CREATE INDEX IF NOT EXISTS idx_categories_parent ON categories(parent_id);
CREATE INDEX IF NOT EXISTS idx_categories_type ON categories(category_type);
CREATE INDEX IF NOT EXISTS idx_categories_sort ON categories(sort_order);
CREATE INDEX IF NOT EXISTS idx_categories_active ON categories(is_active);

-- accounts 表索引
CREATE INDEX IF NOT EXISTS idx_accounts_type ON accounts(account_type);
CREATE INDEX IF NOT EXISTS idx_accounts_active ON accounts(is_active);

-- ledgers 表索引
CREATE INDEX IF NOT EXISTS idx_ledgers_active ON ledgers(is_active);

-- budgets 表索引
CREATE INDEX IF NOT EXISTS idx_budgets_month ON budgets(budget_month);
CREATE INDEX IF NOT EXISTS idx_budgets_ledger_category ON budgets(ledger_id, category_id);

-- ===========================================
-- 3. 创建视图
-- ===========================================

-- 3.1 交易详情视图
CREATE VIEW IF NOT EXISTS transaction_details AS
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
WHERE l.is_active = 1 AND c.is_active = 1 AND a.is_active = 1
ORDER BY t.transaction_time DESC;

-- 3.2 账户余额视图
CREATE VIEW IF NOT EXISTS account_balances AS
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
WHERE a.is_active = 1
GROUP BY a.account_id, a.account_name, a.account_type, a.initial_balance, a.current_balance;

-- ===========================================
-- 4. 插入基础数据
-- ===========================================

-- 4.1 插入默认账本
INSERT OR IGNORE INTO ledgers (ledger_name, description) VALUES 
('默认账本', '个人日常记账账本');

-- 4.2 插入交易类型
INSERT OR IGNORE INTO transaction_types (type_name, type_code, description) VALUES 
('支出', 'EXPENSE', '资金流出'),
('收入', 'INCOME', '资金流入'),
('转账', 'TRANSFER', '账户间资金转移');

-- 4.3 插入基础账户
INSERT OR IGNORE INTO accounts (account_name, account_type, initial_balance, current_balance) VALUES 
('现金', '现金', 1000.00, 1000.00),
('支付宝', '电子支付', 5000.00, 5000.00),
('招商银行卡', '银行卡', 10000.00, 10000.00),
('信用卡', '信用卡', 0.00, 0.00);

-- 4.4 插入基础分类（支出）
INSERT OR IGNORE INTO categories (category_name, parent_id, category_type, sort_order) VALUES 
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
INSERT OR IGNORE INTO categories (category_name, parent_id, category_type, sort_order) VALUES 
('早餐', 1, '支出', 1),
('午餐', 1, '支出', 2),
('晚餐', 1, '支出', 3),
('零食', 1, '支出', 4),
('饮料', 1, '支出', 5);

-- 4.6 插入购物二级分类
INSERT OR IGNORE INTO categories (category_name, parent_id, category_type, sort_order) VALUES 
('服装', 2, '支出', 1),
('日用品', 2, '支出', 2),
('电子产品', 2, '支出', 3),
('书籍', 2, '支出', 4);

-- 4.7 插入基础分类（收入）
INSERT OR IGNORE INTO categories (category_name, parent_id, category_type, sort_order) VALUES 
('工资', NULL, '收入', 1),
('奖金', NULL, '收入', 2),
('投资', NULL, '收入', 3),
('兼职', NULL, '收入', 4),
('其他收入', NULL, '收入', 99);

-- 4.8 插入转账分类
INSERT OR IGNORE INTO categories (category_name, parent_id, category_type, sort_order) VALUES 
('账户转账', NULL, '转账', 1);

-- ===========================================
-- 5. 插入示例数据
-- ===========================================

-- 5.1 插入示例交易记录
INSERT OR IGNORE INTO transactions (transaction_time, ledger_id, type_id, category_id, amount, account_id, notes) VALUES 
('2025-07-29 19:29:00', 1, 1, 1, -22.90, 2, '肯德基'),
('2025-07-29 12:15:00', 1, 1, 2, -15.50, 2, '午餐'),
('2025-07-28 18:30:00', 1, 1, 3, -8.00, 1, '地铁'),
('2025-07-28 09:00:00', 1, 1, 6, -5.00, 1, '早餐'),
('2025-07-27 20:00:00', 1, 1, 4, -35.00, 2, '电影票'),
('2025-07-27 15:30:00', 1, 1, 8, -120.00, 3, '超市购物'),
('2025-07-26 10:00:00', 1, 2, 9, 5000.00, 3, '工资'),
('2025-07-25 14:20:00', 1, 1, 5, -200.00, 2, '医院挂号费');

-- ===========================================
-- 6. 触发器（替代存储过程）
-- ===========================================

-- 6.1 创建触发器来更新账户余额
CREATE TRIGGER IF NOT EXISTS update_account_balance_after_insert
AFTER INSERT ON transactions
BEGIN
    UPDATE accounts 
    SET current_balance = (
        SELECT initial_balance + COALESCE(SUM(amount), 0)
        FROM transactions 
        WHERE account_id = NEW.account_id
    ),
    updated_at = CURRENT_TIMESTAMP
    WHERE account_id = NEW.account_id;
END;

CREATE TRIGGER IF NOT EXISTS update_account_balance_after_update
AFTER UPDATE ON transactions
BEGIN
    -- 更新旧账户余额
    UPDATE accounts 
    SET current_balance = (
        SELECT initial_balance + COALESCE(SUM(amount), 0)
        FROM transactions 
        WHERE account_id = OLD.account_id
    ),
    updated_at = CURRENT_TIMESTAMP
    WHERE account_id = OLD.account_id;
    
    -- 如果账户发生变化，更新新账户余额
    UPDATE accounts 
    SET current_balance = (
        SELECT initial_balance + COALESCE(SUM(amount), 0)
        FROM transactions 
        WHERE account_id = NEW.account_id
    ),
    updated_at = CURRENT_TIMESTAMP
    WHERE account_id = NEW.account_id;
END;

CREATE TRIGGER IF NOT EXISTS update_account_balance_after_delete
AFTER DELETE ON transactions
BEGIN
    UPDATE accounts 
    SET current_balance = (
        SELECT initial_balance + COALESCE(SUM(amount), 0)
        FROM transactions 
        WHERE account_id = OLD.account_id
    ),
    updated_at = CURRENT_TIMESTAMP
    WHERE account_id = OLD.account_id;
END;

-- ===========================================
-- 完成提示
-- ===========================================

-- SQLite不支持SELECT语句直接输出消息，这里用注释代替
-- Jingzhe 数据库创建完成！
-- 数据库类型: SQLite
-- 已创建表: ledgers, accounts, categories, transaction_types, transactions, budgets
-- 已创建视图: transaction_details, account_balances
-- 已创建触发器: update_account_balance_after_insert, update_account_balance_after_update, update_account_balance_after_delete
-- 注意: SQLite不支持存储过程和函数，已用触发器替代账户余额更新功能



