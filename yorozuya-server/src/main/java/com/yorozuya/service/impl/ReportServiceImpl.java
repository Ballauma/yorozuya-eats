package com.yorozuya.service.impl;

import com.yorozuya.entity.Orders;
import com.yorozuya.mapper.OrderMapper;
import com.yorozuya.mapper.UserMapper;
import com.yorozuya.service.ReportService;
import com.yorozuya.service.WorkspaceService;
import com.yorozuya.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Autowired
    private WorkspaceService workspaceService;


    @Override
    public UserReportVO getUserStatistics(LocalDate begin, LocalDate end) {
        // 生成日期列表
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 一次性查出所有每天的新增数据，转为 Map 方便查找
        // 结果类似: {"2026-02-01": 5, "2026-02-03": 2} (注意：没有数据的日期数据库不会返回)
        List<Map<String, Object>> mapList = userMapper.countUserByDate(dateList.get(0), dateList.get(dateList.size() - 1));

        // 把 List<Map> 转成 Map<LocalDate, Integer> 方便 O(1) 获取
        Map<String, Integer> newUsersMap = mapList.stream().collect(Collectors.toMap(
                m -> (String) m.get("date"),
                m -> ((Long) m.get("count")).intValue()
        ));

        // 查出起始日期之前的“用户总基数”
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
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 把 LocalDate 转成 LocalDateTime，确保包含一整天
        LocalDateTime beginTime = LocalDateTime.of(dateList.get(0), LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(dateList.get(dateList.size() - 1), LocalTime.MAX);

        List<Map<String, Object>> mapList = orderMapper.sumTurnoverByDate(beginTime, endTime, Orders.COMPLETED);

        Map<String, Double> turnoverMap = mapList.stream().collect(Collectors.toMap(
                m -> (String) m.get("date"),
                m -> ((BigDecimal) m.get("turnover")).doubleValue() // 注意：MySQL SUM 出来的通常是 BigDecimal
        ));

        // 遍历完整日期，填坑 (如果当天没数据，就补 0.0)
        List<Double> turnoverList = new ArrayList<>();
        for (LocalDate date : dateList) {
            String dateStr = date.toString();

            // 如果 Map 里有这一天，就取值；没有，就默认 0.0
            Double turnover = turnoverMap.getOrDefault(dateStr, 0.0);

            turnoverList.add(turnover);
        }

        //  封装返回
        return TurnoverReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .turnoverList(StringUtils.join(turnoverList, ","))
                .build();
    }


    @Override
    public OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end) {
        // 1. 生成完整的日期列表 (你的原有逻辑，保持不变)
        List<LocalDate> dateList = new ArrayList<>();
        dateList.add(begin);
        while (!begin.equals(end)) {
            begin = begin.plusDays(1);
            dateList.add(begin);
        }

        // 2. 【核心优化】一次性从数据库查出指定时间段内的每日订单统计数据
        LocalDateTime queryBeginTime = LocalDateTime.of(dateList.get(0), LocalTime.MIN);
        LocalDateTime queryEndTime = LocalDateTime.of(dateList.get(dateList.size() - 1), LocalTime.MAX);

        // 调用 Mapper 方法获取原始数据，传递订单完成状态
        List<Map<String, Object>> rawOrderData = orderMapper.countOrderByDate(queryBeginTime, queryEndTime, Orders.COMPLETED);

        // 3. 将 List<Map> 转换为 Map< 日期字符串, Map< 统计项, 数量 >>，方便查找
        Map<String, Map<String, Long>> dailyOrderStatsMap = rawOrderData.stream()
                .collect(Collectors.toMap(
                        m -> (String) m.get("date"), // Key 是日期字符串
                        m -> {
                            Map<String, Long> stats = new HashMap<>();
                            stats.put("totalOrders", (Long) m.get("totalOrders"));
                            stats.put("validOrders", (Long) m.get("validOrders"));
                            return stats;
                        }
                ));

        // 4. 【数据对齐与补全】遍历完整的日期列表，补齐 0 值，并计算总和
        List<Integer> totalOrderList = new ArrayList<>();
        List<Integer> validOrderList = new ArrayList<>();

        // 新增这两个变量，用于累加计算最终的总订单数和有效订单数
        Integer totalOrderCount = 0;
        Integer validOrderCount = 0;

        for (LocalDate date : dateList) {
            String dateStr = date.toString();
            Map<String, Long> stats = dailyOrderStatsMap.get(dateStr);

            if (stats != null) {
                Integer currentTotal = stats.getOrDefault("totalOrders", 0L).intValue();
                Integer currentValid = stats.getOrDefault("validOrders", 0L).intValue();

                totalOrderList.add(currentTotal);
                validOrderList.add(currentValid);

                // 累加到总数中
                totalOrderCount += currentTotal;
                validOrderCount += currentValid;

            } else {
                // 当天没有订单数据，默认为 0
                totalOrderList.add(0);
                validOrderList.add(0);
            }
        }

        // 5. 【新增计算】计算订单完成率
        Double orderCompletionRate = 0.0;
        if (totalOrderCount != 0) { // 避免除以零的错误
            orderCompletionRate = validOrderCount.doubleValue() / totalOrderCount.doubleValue();
        }

        // 6. 封装返回 OrderReportVO
        return OrderReportVO.builder()
                .dateList(StringUtils.join(dateList, ","))
                .orderCountList(StringUtils.join(totalOrderList, ",")) // 对应 VO 中的 orderCountList
                .validOrderCountList(StringUtils.join(validOrderList, ",")) // 对应 VO 中的 validOrderCountList
                .totalOrderCount(totalOrderCount) // 赋值总订单数
                .validOrderCount(validOrderCount) // 赋值总有效订单数
                .orderCompletionRate(orderCompletionRate) // 赋值订单完成率
                .build();
    }


    @Override
    public SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end) {
        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end, LocalTime.MAX);

        // 1. 查出来一堆 Map
        List<Map<String, Object>> salesTop10 = orderMapper.getSalesTop10(beginTime, endTime);

        // 2. 提取名称列表 ( 从 Map 的 "name" key 取值 )
        List<String> names = salesTop10.stream()
                .map(m -> (String) m.get("name"))
                .collect(Collectors.toList());

        // 3. 提取销量列表 ( 从 Map 的 "number" key 取值 )
        List<Integer> numbers = salesTop10.stream()
                .map(m -> {
                    // 这种写法最稳：先转 Number 再取 intValue
                    return ((Number) m.get("number")).intValue();
                })
                .collect(Collectors.toList());

        // 4. 拼装成字符串并返回 VO
        return SalesTop10ReportVO.builder()
                .nameList(StringUtils.join(names, ","))
                .numberList(StringUtils.join(numbers, ","))
                .build();
    }

    @Override
    public void export(HttpServletResponse response) {
        // 1. 查询数据库，获取营业数据 --- 查询最近 30 天的运营数据
        LocalDate dateBegin = LocalDate.now().minusDays(30);
        LocalDate dateEnd = LocalDate.now().minusDays(1);

        // 查询概览数据
        BusinessDataVO businessDataVO = workspaceService.getBusinessData(LocalDateTime.of(dateBegin, LocalTime.MIN), LocalDateTime.of(dateEnd, LocalTime.MAX));

        // 2. 通过 POI 将数据写入到 Excel 文件中
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("template/运营数据报表模板.xlsx");

        try {
            // 基于模板文件创建一个新的 Excel 文件
            XSSFWorkbook excel = new XSSFWorkbook(in);

            // 获取表格文件的 Sheet 页
            XSSFSheet sheet = excel.getSheet("Sheet1");

            // 填充数据 -- 时间
            sheet.getRow(1).getCell(1).setCellValue("时间：" + dateBegin + "至" + dateEnd);

            // 获得第 4 行
            XSSFRow row = sheet.getRow(3);
            row.getCell(2).setCellValue(businessDataVO.getTurnover());
            row.getCell(4).setCellValue(businessDataVO.getOrderCompletionRate());
            row.getCell(6).setCellValue(businessDataVO.getNewUsers());

            // 获得第 5 行
            row = sheet.getRow(4);
            row.getCell(2).setCellValue(businessDataVO.getValidOrderCount());
            row.getCell(4).setCellValue(businessDataVO.getUnitPrice());

            // 填充明细数据
            for (int i = 0; i < 30; i++) {
                LocalDate date = dateBegin.plusDays(i);
                // 查询某一天的营业数据
                BusinessDataVO businessData = workspaceService.getBusinessData(LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX));

                // 获得某一行
                row = sheet.getRow(7 + i);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessData.getTurnover());
                row.getCell(3).setCellValue(businessData.getValidOrderCount());
                row.getCell(4).setCellValue(businessData.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessData.getUnitPrice());
                row.getCell(6).setCellValue(businessData.getNewUsers());
            }

            // 3. 通过输出流将 Excel 文件下载到客户端浏览器
            ServletOutputStream out = response.getOutputStream();
            excel.write(out);

            // 关闭资源
            out.close();
            excel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
