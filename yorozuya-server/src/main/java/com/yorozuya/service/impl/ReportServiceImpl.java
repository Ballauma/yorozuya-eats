package com.yorozuya.service.impl;

import com.yorozuya.entity.Orders;
import com.yorozuya.mapper.OrderMapper;
import com.yorozuya.mapper.UserMapper;
import com.yorozuya.service.ReportService;
import com.yorozuya.vo.TurnoverReportVO;
import com.yorozuya.vo.UserReportVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Ballauma
 */
@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private UserMapper userMapper;


    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        // 1. 生成日期列表 (这步不变)
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 2. 【关键】一次性查出所有每天的新增数据，转为 Map 方便查找
        // 结果类似: {"2026-02-01": 5, "2026-02-03": 2} (注意：没有数据的日期数据库不会返回)
        List<Map<String, Object>> mapList = userMapper.countUserByDate(dateList.get(0), dateList.get(dateList.size() - 1));

        // 把 List<Map> 转成 Map<LocalDate, Integer> 方便 O(1) 获取
        Map<String, Integer> newUsersMap = mapList.stream().collect(Collectors.toMap(
                m -> (String) m.get("date"),
                m -> ((Long) m.get("count")).intValue()
        ));

        // 3. 【关键】查出起始日期之前的“用户总基数”
        Integer currentTotalUsers = userMapper.countBeforeDate(LocalDateTime.of(dateList.get(0), LocalTime.MIN));

        // 4. 在 Java 内存中进行拼装
        List<Integer> newUserList = new ArrayList<>();
        List<Integer> totalUserList = new ArrayList<>();

        for (LocalDate date : dateList) {
            String dateStr = date.toString();

            // 获取当天新增 (如果在 Map 里没找到，说明当天新增为 0)
            Integer newUser = newUsersMap.getOrDefault(dateStr, 0);

            // 计算当天总数 = 前一天的总数 + 当天新增
            currentTotalUsers += newUser;

            newUserList.add(newUser);
            totalUserList.add(currentTotalUsers);
        }


        return UserReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .totalUserList(StringUtils.join(totalUserList, ","))
                .newUserList(StringUtils.join(newUserList, ","))
                .build();
    }

    @Override
    public TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end) {
        // 1. 生成完整的日期列表 (你的原有逻辑，保持不变)
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 2. 【降维打击】一次性查出数据库里的营业额数据
        // 注意：这里要把 LocalDate 转成 LocalDateTime，确保包含一整天
        LocalDateTime beginTime = LocalDateTime.of(dateList.get(0), LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(dateList.get(dateList.size() - 1), LocalTime.MAX);

        // 查出来的数据类似：[{"date": "2026-02-14", "turnover": 100.0}, {"date": "2026-02-16", "turnover": 50.0}]
        List<Map<String, Object>> mapList = orderMapper.sumTurnoverByDate(beginTime, endTime, Orders.COMPLETED);

        // 3. 将 List<Map> 转为 Map< 日期字符串, 营业额 >，方便后续 O(1) 查找
        Map<String, Double> turnoverMap = mapList.stream().collect(Collectors.toMap(
                m -> (String) m.get("date"),
                m -> ((BigDecimal) m.get("turnover")).doubleValue() // 注意：MySQL SUM 出来的通常是 BigDecimal
        ));

        // 4. 【数据对齐】遍历完整日期，填坑 (如果当天没数据，就补 0.0)
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            String dateStr = date.toString();

            // 如果 Map 里有这一天，就取值；没有，就默认 0.0
            Double turnover = turnoverMap.getOrDefault(dateStr, 0.0);

            turnoverList.add(turnover);
        }

        // 5. 封装返回
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }
}
