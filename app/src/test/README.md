# 单元测试说明

本项目包含完整的单元测试套件，覆盖了应用的核心业务逻辑。

## 测试依赖

项目使用以下测试框架和库：

- **JUnit 4**: 基础测试框架
- **Mockito Kotlin**: Mock框架，用于创建测试替身
- **Kotlin Coroutines Test**: 协程测试支持
- **Turbine**: Flow测试库，用于测试Kotlin Flow
- **Arch Core Testing**: Android架构组件测试支持

## 测试结构

### 1. 数据层测试 (`data/`)

#### `DatabasePopulatorTest`
- 测试数据库初始化数据填充逻辑
- 验证空数据库时是否正确填充默认数据
- 验证非空数据库时不会重复填充
- 验证主分类和子分类的插入顺序和关系

#### `BillRepositoryImplTest`
- 测试账单仓库的所有CRUD操作
- 验证数据访问层的正确委托给DAO
- 测试日期范围查询功能

#### `ClassifyRepositoryImplTest`
- 测试分类仓库的所有CRUD操作
- 验证按类型、级别、父ID等条件查询
- 测试分类的增删改功能

### 2. 业务逻辑层测试 (`domain/usecase/`)

#### `BillUseCaseTest`
- 测试所有账单相关的UseCase
- 包括：获取账单、按日期范围查询、插入、删除、更新

#### `ClassifyUseCaseTest`
- 测试所有分类相关的UseCase
- 包括：获取所有分类、按类型查询、获取主分类、获取子分类

### 3. 表示层测试 (`presentation/viewmodel/`)

#### `BillViewModelTest`
- 测试BillViewModel的业务逻辑
- 验证月份账单加载功能
- 测试UI状态管理
- 验证错误处理
- 测试MonthlyBill的创建逻辑

### 4. 实体类测试 (`Entity/`)

#### `BillTest`
- 测试Bill实体的创建方法
- 验证属性设置正确性

#### `ClassifyTest`
- 测试Classify实体的创建方法
- 验证主分类和子分类的区别
- 验证收入和支出类型的分类

#### `DailyBillTest`
- 测试DailyBill的计算逻辑
- 验证日支出、日收入、日总金额的计算
- 测试日期验证逻辑

#### `MonthlyBillTest`
- 测试MonthlyBill的计算逻辑
- 验证月支出、月收入的计算
- 测试月份验证逻辑

## 运行测试

### 运行所有测试
```bash
./gradlew test
```

### 运行特定测试类
```bash
./gradlew test --tests "com.leng.jingzhekt.data.local.DatabasePopulatorTest"
```

### 在Android Studio中运行
1. 右键点击测试文件或测试方法
2. 选择 "Run 'TestName'"

## 测试覆盖率

建议使用以下工具查看测试覆盖率：

```bash
./gradlew testDebugUnitTestCoverage
```

## 测试最佳实践

1. **命名规范**: 测试方法使用 `should_expectedBehavior_whenStateUnderTest` 格式
2. **AAA模式**: Arrange-Act-Assert（准备-执行-断言）
3. **Mock使用**: 使用Mockito创建测试替身，避免依赖真实数据库或网络
4. **协程测试**: 使用 `runTest` 进行协程测试
5. **Flow测试**: 使用Turbine测试Flow的发射值

## 注意事项

- 所有测试都是单元测试，不依赖Android框架
- 使用Mock对象替代真实的DAO和Repository
- 测试数据使用工厂方法创建，保持测试代码简洁
- 每个测试方法应该独立，不依赖其他测试的执行顺序

