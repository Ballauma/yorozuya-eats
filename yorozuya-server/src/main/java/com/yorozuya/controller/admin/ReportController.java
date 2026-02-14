package com.yorozuya.controller.admin;

import com.yorozuya.result.Result;
import com.yorozuya.service.ReportService;
import com.yorozuya.vo.OrderReportVO;
import com.yorozuya.vo.SalesTop10ReportVO;
import com.yorozuya.vo.TurnoverReportVO;
import com.yorozuya.vo.UserReportVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

/**
 * 报表控制器
 *
 * @author Ballauma
 */
@RestController
@Slf4j
@RequestMapping("/admin/report")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * 获取营业额报表
     *
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/turnoverStatistics")
    public Result<TurnoverReportVO> turnoverStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("获取营业额报表");
        TurnoverReportVO turnoverReportVO = reportService.getTurnoverStatistics(begin, end);

        return Result.success(turnoverReportVO);
    }

    /**
     * 获取用户报表
     *
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/userStatistics")
    public Result<UserReportVO> userStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("获取用户报表");
        UserReportVO userReportVO = reportService.getUserStatistics(begin, end);

        return Result.success(userReportVO);
    }


    /**
     * 获取订单报表
     *
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/ordersStatistics")
    public Result<OrderReportVO> orderStatistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("获取订单报表");
        OrderReportVO orderReportVO = reportService.getOrderStatistics(begin, end);

        return Result.success(orderReportVO);
    }


    /**
     * 获取销量 Top10 报表
     *
     * @param begin
     * @param end
     * @return
     */
    @GetMapping("/top10")
    public Result<SalesTop10ReportVO> salesTop10Statistics(
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate begin,
            @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate end) {
        log.info("获取销量 Top10 报表");
        SalesTop10ReportVO salesTop10ReportVO = reportService.getSalesTop10(begin, end);

        return Result.success(salesTop10ReportVO);
    }
}
