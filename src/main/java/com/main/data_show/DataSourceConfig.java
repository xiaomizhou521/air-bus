package com.main.data_show;

import com.dangdang.ddframe.rdb.sharding.api.ShardingDataSourceFactory;
import com.dangdang.ddframe.rdb.sharding.api.rule.BindingTableRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.DataSourceRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.ShardingRule;
import com.dangdang.ddframe.rdb.sharding.api.rule.TableRule;
import com.dangdang.ddframe.rdb.sharding.api.strategy.table.TableShardingStrategy;
import com.main.data_show.strategy.ModuloTableShardingAlgorithm;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.*;

/**
 *  数据源及分表配置
 * Created by Kane on 2018/1/17.
 */
@Configuration
@MapperScan(basePackages = "com.main.data_show.mapper", sqlSessionTemplateRef  = "test1SqlSessionTemplate")
public class DataSourceConfig {


    /**
     * 配置数据源0，数据源的名称最好要有一定的规则，方便配置分库的计算规则
     * @return
     */
    @Bean(name="dataSource0")
    @ConfigurationProperties(prefix = "spring.datasource.test1")
    public DataSource dataSource0(){
        return DataSourceBuilder.create().build();
    }


    /**
     * 配置数据源规则，即将多个数据源交给sharding-jdbc管理，并且可以设置默认的数据源，
     * 当表没有配置分库规则时会使用默认的数据源
     * @param dataSource0
     * @return
     */
    @Bean
    public DataSourceRule dataSourceRule(@Qualifier("dataSource0") DataSource dataSource0){
        Map<String, DataSource> dataSourceMap = new HashMap<>(); //设置分库映射
        dataSourceMap.put("dataSource0", dataSource0);
        return new DataSourceRule(dataSourceMap, "dataSource0"); //设置默认库，两个库以上时必须设置默认库。默认库的数据源名称必须是dataSourceMap的key之一
    }

    /**
     * 配置数据源策略和表策略，具体策略需要自己实现
     * @param dataSourceRule
     * @return
     */
    @Bean
    public ShardingRule shardingRule(DataSourceRule dataSourceRule){
        //具体分库分表策略
        TableRule taAllPointDataTableRule = TableRule.builder("ta_all_point_data")
                .actualTables(Arrays.asList("ta_all_point_data_0", "ta_all_point_data_1", "ta_all_point_data_2", "ta_all_point_data_3", "ta_all_point_data_4", "ta_all_point_data_5", "ta_all_point_data_6", "ta_all_point_data_7", "ta_all_point_data_8", "ta_all_point_data_9"))
                .tableShardingStrategy(new TableShardingStrategy("point_id", new ModuloTableShardingAlgorithm()))
                .dataSourceRule(dataSourceRule)
                .build();
        TableRule taInstantPointDataTableRule = TableRule.builder("ta_instant_point_data")
                .actualTables(Arrays.asList("ta_instant_point_data_0", "ta_instant_point_data_1", "ta_instant_point_data_2", "ta_instant_point_data_3", "ta_instant_point_data_4", "ta_instant_point_data_5", "ta_instant_point_data_6", "ta_instant_point_data_7", "ta_instant_point_data_8", "ta_instant_point_data_9"))
                .tableShardingStrategy(new TableShardingStrategy("point_id", new ModuloTableShardingAlgorithm()))
                .dataSourceRule(dataSourceRule)
                .build();
        TableRule taUsagePointDataTableRule = TableRule.builder("ta_usage_point_data")
                .actualTables(Arrays.asList("ta_usage_point_data_0", "ta_usage_point_data_1", "ta_usage_point_data_2", "ta_usage_point_data_3", "ta_usage_point_data_4", "ta_usage_point_data_5", "ta_usage_point_data_6", "ta_usage_point_data_7", "ta_usage_point_data_8", "ta_usage_point_data_9"))
                .tableShardingStrategy(new TableShardingStrategy("point_id", new ModuloTableShardingAlgorithm()))
                .dataSourceRule(dataSourceRule)
                .build();

        //绑定表策略，在查询时会使用主表策略计算路由的数据源，因此需要约定绑定表策略的表的规则需要一致，可以一定程度提高效率
        List<BindingTableRule> bindingTableRules = new ArrayList<BindingTableRule>();
        bindingTableRules.add(new BindingTableRule(Arrays.asList(taAllPointDataTableRule,taInstantPointDataTableRule,taUsagePointDataTableRule)));
        return ShardingRule.builder()
                .dataSourceRule(dataSourceRule)
                .tableRules(Arrays.asList(taAllPointDataTableRule,taInstantPointDataTableRule,taUsagePointDataTableRule))
                .bindingTableRules(bindingTableRules)
                .tableShardingStrategy(new TableShardingStrategy("point_id", new ModuloTableShardingAlgorithm()))
                .tableShardingStrategy(new TableShardingStrategy("point_id", new ModuloTableShardingAlgorithm()))
                .tableShardingStrategy(new TableShardingStrategy("point_id", new ModuloTableShardingAlgorithm()))
                .build();
    }

    /**
     * 创建sharding-jdbc的数据源DataSource，MybatisAutoConfiguration会使用此数据源
     * @param shardingRule
     * @return
     * @throws SQLException
     */
    @Bean(name="dataSource")
    public DataSource shardingDataSource(ShardingRule shardingRule) throws SQLException {
        return ShardingDataSourceFactory.createDataSource(shardingRule);
    }

    /**
     * 需要手动配置事务管理器
     * @param dataSource
     * @return
     */
    @Bean
    public DataSourceTransactionManager transactitonManager(@Qualifier("dataSource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "test1SqlSessionFactory")
    @Primary
    public SqlSessionFactory testSqlSessionFactory(@Qualifier("dataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mybatis/mapper/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "test1SqlSessionTemplate")
    @Primary
    public SqlSessionTemplate testSqlSessionTemplate(@Qualifier("test1SqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
