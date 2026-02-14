package com.yorozuya.service;

import com.yorozuya.vo.OrderReportVO;
import com.yorozuya.vo.SalesTop10ReportVO;
import com.yorozuya.vo.TurnoverReportVO;
import com.yorozuya.vo.UserReportVO;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * @author Ballauma
 */
@Service
public interface ReportService {
    /**
     * 获取营业额报表
     *
     * @param begin
     * @param end
     * @return
     */
    TurnoverReportVO getTurnoverStatistics(LocalDate begin, LocalDate end);

    /**
     * 获取用户报表
     *
     * @param begin
     * @param end
     * @return
     */
    UserReportVO getUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 获取订单报表
     *
     * @param begin
     * @param end
     * @return
     */
    OrderReportVO getOrderStatistics(LocalDate begin, LocalDate end);

    /**
     * 获取销量 Top 10 报表
     *
     * @param begin
     * @param end
     * @return
     */
    SalesTop10ReportVO getSalesTop10(LocalDate begin, LocalDate end);
}
