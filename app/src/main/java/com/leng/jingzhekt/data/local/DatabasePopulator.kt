package com.leng.jingzhekt.data.local

import com.leng.jingzhekt.Entity.Bill
import com.leng.jingzhekt.Entity.BillType
import com.leng.jingzhekt.Entity.Classify
import com.leng.jingzhekt.Entity.Level
import com.leng.jingzhekt.Entity.Type
import com.leng.jingzhekt.R
import com.leng.jingzhekt.data.local.dao.BillDao
import com.leng.jingzhekt.data.local.dao.ClassifyDao
import kotlinx.coroutines.flow.first
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabasePopulator @Inject constructor(
    private val classifyDao: ClassifyDao,
    private val billDao: BillDao
) {
    
    suspend fun populateIfEmpty() {
        // 检查数据库是否为空
        val classifies = classifyDao.getAllClassifies().first()
        if (classifies.isEmpty()) {
            populateDefaultData()
        }
    }
    
    private suspend fun populateDefaultData() {
        // 插入默认分类数据
        insertDefaultClassifies()
        
        // 插入默认账单数据
        insertDefaultBills()
    }
    
    private suspend fun insertDefaultClassifies() {
        val defaultClassifies = listOf(
            // 主要支出分类
            Classify.create("餐饮", R.drawable.icon_food, Level.Major, Type.Expend),
            Classify.create("购物", R.drawable.icon_shopping, Level.Major,Type.Expend),
            Classify.create("交通", R.drawable.icon_traffic, Level.Major,Type.Expend),
            Classify.create("娱乐", R.drawable.icon_entertainment, Level.Major,Type.Expend),
            Classify.create("医疗", R.drawable.icon_medicine, Level.Major,Type.Expend),
            Classify.create("教育", R.drawable.icon_study, Level.Major,Type.Expend),
            Classify.create("住房", R.drawable.icon_houserent, Level.Major,Type.Expend),
            
            // 主要收入分类
            Classify.create("工资", R.drawable.icon_salary, Level.Major,Type.Income),
            Classify.create("奖金", R.drawable.icon_winning, Level.Major,Type.Income),
            Classify.create("投资", R.drawable.icon_investment, Level.Major,Type.Income),
            
            // 次要分类
            Classify.create("早餐", R.drawable.icon_food, Level.Minor,Type.Expend),
            Classify.create("午餐", R.drawable.icon_food, Level.Minor,Type.Expend),
            Classify.create("晚餐", R.drawable.icon_food, Level.Minor,Type.Expend),
            Classify.create("服装", R.drawable.icon_shopping, Level.Minor,Type.Expend),
            Classify.create("日用品", R.drawable.icon_daily, Level.Minor,Type.Expend),
            Classify.create("公交", R.drawable.icon_traffic, Level.Minor,Type.Expend),
            Classify.create("打车", R.drawable.icon_traffic, Level.Minor,Type.Expend)
        )
        
        defaultClassifies.forEach { classify ->
            classifyDao.insertClassify(classify)
        }
    }
    
    private suspend fun insertDefaultBills() {
        val now = LocalDateTime.now()
        
        // 获取已插入的分类ID（由于自增ID，我们需要重新查询）
        val allClassifies = classifyDao.getAllClassifies().first()
        val classifyMap = allClassifies.associateBy { it.name }
        
        val defaultBills = listOf(
            // 支出记录
            Bill.create(
                classifyId = classifyMap["早餐"]?.id ?: 1,
                type = BillType.EXPEND,
                time = now.minusDays(1),
                amount = 15.5f,
                remarks = "豆浆油条"
            ),
            Bill.create(
                classifyId = classifyMap["午餐"]?.id ?: 1,
                type = BillType.EXPEND,
                time = now.minusDays(1),
                amount = 32.0f,
                remarks = "麻辣烫"
            ),
            Bill.create(
                classifyId = classifyMap["晚餐"]?.id ?: 1,
                type = BillType.EXPEND,
                time = now.minusDays(1),
                amount = 45.8f,
                remarks = "川菜馆"
            ),
            Bill.create(
                classifyId = classifyMap["公交"]?.id ?: 3,
                type = BillType.EXPEND,
                time = now.minusDays(2),
                amount = 2.0f,
                remarks = "地铁出行"
            ),
            Bill.create(
                classifyId = classifyMap["服装"]?.id ?: 2,
                type = BillType.EXPEND,
                time = now.minusDays(3),
                amount = 299.0f,
                remarks = "优衣库T恤"
            ),
            Bill.create(
                classifyId = classifyMap["日用品"]?.id ?: 2,
                type = BillType.EXPEND,
                time = now.minusDays(4),
                amount = 68.5f,
                remarks = "洗发水沐浴露"
            ),
            
            // 收入记录
            Bill.create(
                classifyId = classifyMap["工资"]?.id ?: 8,
                type = BillType.INCOME,
                time = now.minusDays(5),
                amount = 8500.0f,
                remarks = "月度工资"
            ),
            Bill.create(
                classifyId = classifyMap["奖金"]?.id ?: 9,
                type = BillType.INCOME,
                time = now.minusDays(10),
                amount = 1200.0f,
                remarks = "季度奖金"
            ),
            Bill.create(
                classifyId = classifyMap["投资"]?.id ?: 10,
                type = BillType.INCOME,
                time = now.minusDays(7),
                amount = 156.8f,
                remarks = "基金收益"
            )
        )
        
        defaultBills.forEach { bill ->
            billDao.insertBill(bill)
        }
    }
}
